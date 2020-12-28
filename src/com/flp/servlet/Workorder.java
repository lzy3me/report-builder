package com.flp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flp.db.DBConnector;
import com.flp.model.CustomerModel;
import com.flp.model.ProductModel;
import com.flp.model.QuotationModel;
import com.flp.model.SaleModel;
import com.flp.model.WorkOrderModel;
import com.flp.util.Util;
import com.google.gson.Gson;

@WebServlet(name = "workorderApi", urlPatterns = "/apis/workorder")
public class Workorder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		List lstData = new ArrayList();
		HttpSession session = request.getSession(true);
		
		try {
			System.out.print("Servlet: Workorder doing >> ");
			String data = request.getParameter("order");

			if (data != null) {
				if (data.equals("new")) {
					System.out.println("add new order.");
					addNewWork(request, response);
				} else if (data.equals("getmaxid")) {
					System.out.println("getting max id.");
					HashMap hm = new HashMap<String, Integer>();
					hm.put("maxid", getMaxId());
					lstData.add(hm);
				} else if (data.equals("list")) {
					System.out.println("getting work list.");
					lstData = getWorkList(request, response);
				} else if (data.equals("getwork")) {					
					System.out.println("getting work.");
					lstData = getWorkorder(request, response);
				} else if (data.equals("delete")) {
					System.out.println("drop work.");
					dropWorkFromId(Integer.parseInt(request.getParameter("workorderId")), response);
				}
				
				if (lstData != null) {
					out.print(new Gson().toJson(lstData));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int count(DBConnector db) {
		int count = 0;
		System.out.println(">> Counting... <<");
		
		String sql = "SELECT COUNT(workorder_id) FROM workorder LIMIT 0, 1";
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	private int getMaxId() {
		DBConnector db = new DBConnector();
		int maxId = 0;
		
		try {
			maxId = getMaxId(db);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		return maxId;
	}
	
	private int getMaxId(DBConnector db) throws SQLException {
		int maxId = 0;
		String sql = "SELECT MAX(workorder_id) FROM workorder LIMIT 0, 1";
		ResultSet rs = db.query(sql);
		if (rs.next()) {
			maxId = rs.getInt(1)+1;
		} else {
			maxId = 1;
		}
		if (rs != null) rs.close();
		return maxId;
	}
	
	private void addWorkFromQuotation(DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		String workorderId = req.getParameter("workorderId");
		String quotationId = req.getParameter("quotationId");
		String sql = "SELECT * FROM reportbuild.quotation WHERE quotation_id = "+quotationId;
		ResultSet rsAdd = db.query(sql);
		if (rsAdd.next()) {
			int productId = rsAdd.getInt(2);
			String dispatchDate = req.getParameter("dispatch");
			int sprocketHole = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveSprocetHole")));
			int crimping = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveCrimping")));
			int glue = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveGlue")));
			String formFolding = req.getParameter("formFolding");
			String packingSet = req.getParameter("packingSet");
			String packingBox = req.getParameter("packingBox");
			String additionalDetail = req.getParameter("workAddition");
			
			sql = "INSERT INTO "
				+ "reportbuild.workorder"
				+ "(workorder_id, "
				+ "quotation_id, "
				+ "customer_id, "
				+ "product_id, "
				+ "sale_id, "
				+ "work_date, "
				+ "work_dispatch, "
				+ "work_sprocket_hole, "
				+ "work_crimping, "
				+ "work_glue, "
				+ "work_form_folding, "
				+ "work_packing_set, "
				+ "work_packing, "
				+ "work_additional_detail) "
				+ "VALUES "
				+ "("+workorderId+", "
				+ rsAdd.getInt(1)+", "
				+ rsAdd.getInt(3)+", "
				+ productId+", "
				+ rsAdd.getInt(4)+", "
				+ "NOW(), "
				+ "'"+dispatchDate+"', "
				+ sprocketHole+", "
				+ crimping+", "
				+ glue+", "
				+ "'"+formFolding+"', "
				+ "'"+packingSet+"', "
				+ "'"+packingBox+"', "
				+ "'"+additionalDetail+"')";
			System.out.println("Query >> "+sql);
			db.execute(sql);
			Product.updateProduct(db, productId, req);
			resp.addHeader("state", "OKAY");
		} else {
			resp.addHeader("state", "NOT_HAVE_QUOTATION");
		}
		if (rsAdd != null) rsAdd.close();
	}
	
	private void addNewWorkInsertToProduct(DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		String workorderId = req.getParameter("workorderId");
		String customerId = req.getParameter("customerId");
		String saleId = req.getParameter("saleId");
		String dispatchDate = req.getParameter("dispatch");
		int sprocketHole = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveSprocetHole")));
		int crimping = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveCrimping")));
		int glue = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveGlue")));
		String formFolding = req.getParameter("formFolding");
		String packingSet = req.getParameter("packingSet");
		String packingBox = req.getParameter("packingBox");
		String additionalDetail = req.getParameter("workAddition");
		
		int productId = 0;
		productId = Product.addProduct(db, req, resp);
		if (productId != 0) {
			String sql = "INSERT INTO "
				+ "reportbuild.workorder"
				+ "(workorder_id, "
				+ "quotation_id, "
				+ "customer_id, "
				+ "product_id, "
				+ "sale_id, "
				+ "work_date, "
				+ "work_dispatch, "
				+ "work_sprocket_hole, "
				+ "work_crimping, "
				+ "work_glue, "
				+ "work_form_folding, "
				+ "work_packing_set, "
				+ "work_packing, "
				+ "work_additional_detail) "
				+ "VALUES "
				+ "("+workorderId+", "
				+ "NULL, "
				+ customerId+", "
				+ productId+", "
				+ saleId+", "
				+ "NOW(), "
				+ "'"+dispatchDate+"', "
				+ sprocketHole+", "
				+ crimping+", "
				+ glue+", "
				+ "'"+formFolding+"', "
				+ "'"+packingSet+"', "
				+ "'"+packingBox+"', "
				+ "'"+additionalDetail+"')";
			db.execute(sql);
			resp.addHeader("state", "OKAY");
		} else {
			resp.addHeader("state", "ERR_WITH_PRODUCT");
		}
	}
	
	private void updateWorkorder(DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		String workorderId = req.getParameter("workorderId");
		int quotationId = Util.checkNullReturnZeroInt(req.getParameter("quotationId"));
		int customerId = Util.checkNullReturnZeroInt(req.getParameter("customerId"));
		int saleId = Util.checkNullReturnZeroInt(req.getParameter("saleNo"));
		int productId = Util.checkNullReturnZeroInt(req.getParameter("productId"));
		String dispatchDate = req.getParameter("dispatch");
		int sprocketHole = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveSprocetHole")));
		int crimping = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveCrimping")));
		int glue = Util.checkBoolReturnInt(Boolean.valueOf(req.getParameter("haveGlue")));
		String formFolding = req.getParameter("formFolding");
		String packingSet = req.getParameter("packingSet");
		String packingBox = req.getParameter("packingBox");
		String additionalDetail = req.getParameter("workAddition");
		String sql = "";

		sql = "UPDATE reportbuild.workorder "
			+ "SET ";
		if (quotationId != 0) {
			sql += "quotation_id = "+quotationId+", ";
		}
		sql += "customer_id = "+customerId+", "
			+ "product_id = "+productId+", "
			+ "sale_id = "+saleId+", "
			+ "work_date = NOW(), "
			+ "work_dispatch = '"+dispatchDate+"', "
			+ "work_sprocket_hole = "+sprocketHole+", "
			+ "work_crimping = "+crimping+", "
			+ "work_glue = "+glue+", "
			+ "work_form_folding = '"+formFolding+"', "
			+ "work_packing_set = '"+packingSet+"', "
			+ "work_packing = '"+packingBox+"', "
			+ "work_additional_detail = '"+additionalDetail+"' "
			+ "WHERE "
			+ "workorder_id = "+workorderId;
		db.execute(sql);
		Product.updateProduct(db, productId, req);
		resp.addHeader("state", "UPDATED");
	}
	
	private void addNewWork(HttpServletRequest request, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		String quotationId = request.getParameter("quotationId");
		String workorderId = request.getParameter("workorderId");
	
		try {
			String sql = "SELECT * FROM workorder WHERE workorder_id = "+workorderId;
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				updateWorkorder(db, request, response);
			} else {				
				if (quotationId == null || quotationId == "")
					addNewWorkInsertToProduct(db, request, response);
				else
					addWorkFromQuotation(db, request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION" + e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private List getWorkList(HttpServletRequest request, HttpServletResponse response) {
		List lstMsg = new ArrayList();
		DBConnector db = new DBConnector();
		WorkOrderModel lstData;
		
		try {
			String sql = "SELECT " + 
				"workorder.workorder_id, " + 
				"workorder.product_id, " + 
				"workorder.work_date, " + 
				"product.product_name " + 
				"FROM " + 
				"workorder " + 
				"INNER JOIN product ON workorder.product_id = product.product_id";
			ResultSet rs = db.query(sql);
			while (rs.next()) {
				ProductModel product = new ProductModel();
				product.setProductId(rs.getInt(2));
				product.setProductName(rs.getString(4));
				
				lstData = new WorkOrderModel();
				lstData.setWorkorderId(rs.getInt(1));
				lstData.setWorkDate(rs.getString(3));
				lstData.setProduct(product);
				lstMsg.add(lstData);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		
		return lstMsg;
	}
	
	private List getWorkorder(HttpServletRequest request, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		List lstMsg = new ArrayList();
		WorkOrderModel lstData;
		
		int workOrderId = Integer.parseInt(request.getParameter("workorderId"));
		try {
			String sql = "SELECT " + 
			"workorder.workorder_id, " + 
			"workorder.quotation_id, " + 
			"workorder.customer_id, " + 
			"workorder.product_id, " + 
			"workorder.sale_id, " + 
			"workorder.work_date, " + 
			"workorder.work_dispatch, " +
			"workorder.work_sprocket_hole, " + 
			"workorder.work_crimping, " + 
			"workorder.work_glue, " + 
			"workorder.work_form_folding, " + 
			"workorder.work_packing_set, " + 
			"workorder.work_packing, " + 
			"workorder.work_additional_detail, " + 
			"product.product_name, " + 
			"product.product_quantity, " + 
			"product.product_total_page, " + 
			"product.product_constat, " + 
			"product.product_other, " + 
			"product.product_unit_set, " + 
			"product.product_form_depth, " + 
			"product.product_form_width, " + 
			"product.product_start_page, " + 
			"product.product_end_page, " + 
			"customer.c_organization_name, " + 
			"customer.c_address, " + 
			"customer.c_contact, " + 
			"sale.sale_no " + 
			"FROM " + 
			"workorder " + 
			"INNER JOIN product ON workorder.product_id = product.product_id " + 
			"INNER JOIN customer ON workorder.customer_id = customer.customer_id " + 
			"INNER JOIN sale ON workorder.sale_id = sale.sale_id " +
			"WHERE workorder.workorder_id = " + workOrderId;
			ResultSet rs = db.query(sql);
			
			while (rs.next()) {
				QuotationModel quotation = new QuotationModel();
				quotation.setQuotationId(rs.getInt(2));
				
				CustomerModel customer = new CustomerModel();
				customer.setCustomerId(rs.getInt(3));
				customer.setCustomerOrgName(rs.getString(25));
				customer.setCustomerAddr(rs.getString(26));
				customer.setCustomerContact(rs.getString(27));
				
				ProductModel product = new ProductModel();
				product.setProductId(rs.getInt(4));
				product.setProductName(rs.getString(15));
				product.setProductQuantity(rs.getString(16));
				product.setProductNOofPart(rs.getString(17));
				product.setProductConstat(rs.getString(18));
				product.setProductOther(rs.getString(19));
				product.setProductUnitSet(rs.getString(20));
				product.setProductHeight(rs.getString(21));
				product.setProductWidth(rs.getString(22));
				product.setProductNOstart(rs.getString(23));
				product.setProductNOend(rs.getString(24));
				
				SaleModel sale = new SaleModel();
				sale.setSaleId(rs.getInt(5));
				sale.setSaleNo(rs.getString(28));
				
				lstData = new WorkOrderModel();
				lstData.setWorkorderId(rs.getInt(1));
				lstData.setWorkDate(rs.getString(6));
				lstData.setWorkDispatch(rs.getString(7));
				lstData.setWorkSprocketHole(rs.getBoolean(8));
				lstData.setWorkCrimping(rs.getBoolean(9));
				lstData.setWorkGlue(rs.getBoolean(10));
				lstData.setWorkFormFolding(rs.getString(11));
				lstData.setWorkPackingSet(rs.getString(12));
				lstData.setWorkPackingBox(rs.getString(13));
				lstData.setWorkAdditional(rs.getString(14));
				lstData.setProduct(product);
				lstData.setCustomer(customer);
				lstData.setQuotation(quotation);
				lstData.setSale(sale);
				lstMsg.add(lstData);
				response.addHeader("state", "OKAY");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_WITH_PRODUCT");
		} finally {
			if (db != null) db.close();
		}
		return lstMsg;
	}
	
	private void dropWorkFromId(int id, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		try {
			String sql = "DELETE FROM workorder WHERE workorder_id = "+ id;
			db.execute(sql);
			resp.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:" + e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
}
