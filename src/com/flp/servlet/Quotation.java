package com.flp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
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
import com.flp.util.Util;
import com.google.gson.Gson;

@WebServlet("/apis/quotation")
public class Quotation extends HttpServlet {
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
			System.out.print("Servlet: Quotation doing >> ");
			String data = request.getParameter("quota");
			
			if (data != null) {
				if (data.equals("new")) {
					System.out.println("add new quotation.");
					addNewQuotation(request, response);
				}
				else if (data.equals("addorupdate")) {
					System.out.println("add new quotation.");
					addOrUpdateQuotation(request, response);
				}
				else if (data.equals("getmaxid")) {
					System.out.println("getting max id.");
					lstData = getMaxId();
				}
				else if (data.equals("list")) {
					System.out.println("getting quotation list.");
					lstData = getQuotationList(request, response);
				}
				else if (data.equals("approvement")) {
					System.out.println("change quotation status.");
					changeQuotationStatus(request, response);
				}
				else if (data.equals("getquota")) {
					System.out.println("getting quotation.");
					lstData = getQuotation(request, response);
				}
				else if (data.equals("update")) {
					System.out.println("update quotation");
					updateQuotation(request, response);
				}
				else if (data.equals("drop")) {
					System.out.println("drop quotation");
					dropQuotationFromId(Integer.parseInt(request.getParameter("quotationId")), response);
				}
			}
			
			if (lstData != null) {
				out.print(new Gson().toJson(lstData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int count(DBConnector db) {
		int count = 0;
		System.out.println(">> Counting... <<");
		
		String sql = "SELECT COUNT(quotation_id) FROM quotation LIMIT 0, 1";
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	private List getMaxId() {
		DBConnector db = new DBConnector();
		System.out.println(">> Grathering Max Id <<");
		List lstMsg = new ArrayList();
		HashMap<String, Integer> lstData = new HashMap<String, Integer>();
		int maxId = 0;
		String sql = "SELECT MAX(quotation_id) FROM quotation LIMIT 0, 1";
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				maxId = rs.getInt(1)+1;
			} else {
				maxId = 1;
			}
			lstData.put("id", maxId);
			lstMsg.add(lstData);
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		return lstMsg;
	}
	
	public static int getMaxId(DBConnector db) {
		System.out.println(">> Grathering Max Id <<");
		int maxId = 0;
		String sql = "SELECT MAX(quotation_id) FROM quotation LIMIT 0, 1";
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				maxId = rs.getInt(1)+1;
			} else {
				maxId = 1;
			}
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxId;
	}
	
	private List getQuotation (HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(">> Grathering Quotation <<");
		List lstMsg = new ArrayList();
		CustomerModel lstC;
		QuotationModel lstQ;
		ProductModel lstP;
		DBConnector db = new DBConnector();
		String quotationId = request.getParameter("id");
		System.out.println(quotationId);
		String sql = "SELECT " + 
				"	customer.customer_id, " + 
				"	customer.c_organization_name, " + 
				"	customer.c_address, " + 
				"	customer.c_contact, " + 
				"	quotation.quotation_id, " + 
				"	quotation.quotation_date, " + 
				"	quotation.despatch_require, " + 
				"	quotation.additional_detail, " + 
				"	quotation.sale_id, " +
				"	quotation.price_detail, " + 
				"	quotation.STATUS, " + 
				"	quotation.product_id, " + 
				"	product.product_name, " + 
				"	product.product_quantity, " + 
				"	product.product_total_page, " + 
				"	product.product_constat, " + 
				"	product.product_other, " + 
				"	product.product_unit_set, " + 
				"	product.product_form_depth, " + 
				"	product.product_form_width, " + 
				"	product.product_start_page, " + 
				"	product.product_end_page  " + 
				"FROM " + 
				"	customer " + 
				"	INNER JOIN quotation ON quotation.customer_id = customer.customer_id " + 
				"	INNER JOIN product ON quotation.product_id = product.product_id " + 
				"WHERE " + 
				"quotation.quotation_id = " + quotationId;
		ResultSet rs = db.query(sql);
		try {
			if (rs.next()) {
				lstC = new CustomerModel();
				lstC.setCustomerId(rs.getInt(1));
				lstC.setCustomerOrgName(rs.getString(2));
				lstC.setCustomerAddr(rs.getString(3));
				lstC.setCustomerContact(rs.getString(4));
				
				lstP = new ProductModel();
				lstP.setProductId(rs.getInt(12));
				lstP.setProductName(rs.getString(13));
				lstP.setProductQuantity(rs.getString(14));
				lstP.setProductNOofPart(rs.getString(15));
				lstP.setProductConstat(rs.getString(16));
				lstP.setProductOther(rs.getString(17));
				lstP.setProductUnitSet(rs.getString(18));
				lstP.setProductHeight(rs.getString(19));
				lstP.setProductWidth(rs.getString(20));
				lstP.setProductNOstart(rs.getString(21));
				lstP.setProductNOend(rs.getString(22));
				
				lstQ = new QuotationModel();
				lstQ.setQuotationId(rs.getInt(5));
				lstQ.setQuotationDate(rs.getDate(6));
				lstQ.setDispatch(rs.getString(7));
				lstQ.setAdditionalDetail(rs.getString(8));
				lstQ.setSaleId(rs.getInt(9));
				lstQ.setPriceDetail(rs.getString(10));
				lstQ.setApprove(rs.getString(11));
				lstQ.setProductObject(lstP);
				lstQ.setCustomerObject(lstC);
				lstMsg.add(lstQ);
			}
			response.addHeader("state", "OKAY");
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
		return lstMsg;
	}
	
	private List getQuotationList (HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(">> Grathering Quotation List <<");
		List lstMsg = new ArrayList();
		QuotationModel lstQ;
		ProductModel lstP;
		DBConnector db = new DBConnector();
		String sql = "";
		String approve = request.getParameter("isapprove");
		if (approve == null || approve == "") {
			sql = "SELECT " + 
				"quotation.quotation_id, " + 
				"quotation.quotation_date, " + 
				"product.product_name " + 
				"FROM " + 
				"quotation " + 
				"INNER JOIN product ON product.product_id = quotation.product_id";
		} else {
			if (approve.equals("yes")) {
				sql = "SELECT " + 
					"quotation.quotation_id, " + 
					"quotation.quotation_date, " + 
					"product.product_name " + 
					"FROM " + 
					"quotation " + 
					"INNER JOIN product ON product.product_id = quotation.product_id " + 
					"WHERE " + 
					"quotation.status = 'approve'";
			}
			else if (approve.equals("no")) {
				sql = "SELECT " + 
					"quotation.quotation_id, " + 
					"quotation.quotation_date, " + 
					"product.product_name " + 
					"FROM " + 
					"quotation " + 
					"INNER JOIN product ON product.product_id = quotation.product_id " +
					"WHERE " + 
					"quotation.status = 'pending'";
			}
		}
		
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				lstQ = new QuotationModel();
				lstP = new ProductModel();
				lstQ.setQuotationId(rs.getInt(1));
				lstQ.setQuotationDate(rs.getDate(2));
				lstP.setProductName(rs.getString(3));
				lstQ.setProductObject(lstP);
				lstMsg.add(lstQ);
			}
			response.addHeader("state", "OKAY");
			if (rs != null) rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
		
		return lstMsg;
	}
	
	private void addNewQuotation (HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(">> Creating New Quotation <<");
		DBConnector db = new DBConnector();
		
		try {
			addNewQuotation(db, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void addNewQuotation(DBConnector db, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int productId = 0;
		int quotationId = Util.checkNullReturnZeroInt(request.getParameter("quotationId"));
		int customerId = Util.checkNullReturnZeroInt(request.getParameter("customerId"));
		int saleId = Util.checkNullReturnZeroInt(request.getParameter("saleNo"));
		String approvement = request.getParameter("approve");
		String additionalDetail = Util.checkNullReturnDash(request.getParameter("quotationAdd"));
		String priceDetail = Util.checkNullReturnDash(request.getParameter("priceDetail"));
		String sql = "";
			if (Customer.isHasId(customerId, db)) {
				productId = Product.addProduct(db, request, response);
				if (productId != 0) {
					sql = "INSERT INTO "
						+ "reportbuild.quotation"
						+ "(quotation_id, "
						+ "product_id, "
						+ "customer_id, "
						+ "sale_id, "
						+ "quotation_date, "
						+ "despatch_require, "
						+ "additional_detail, "
						+ "c_order_id, "
						+ "your_ref, "
						+ "price_detail, "
						+ "status) "
						+ "VALUES "
						+ "("+quotationId+", "
						+ productId+", "
						+ customerId+", "
						+ saleId+", "
						+ "NOW(), "
						+ "'-', "
						+ "'"+additionalDetail+"', "
						+ "1, "
						+ "'-', "
						+ "'"+priceDetail+"', "
						+ "'"+approvement+"')";
					System.out.println("QUERY >>> " + sql);
					db.execute(sql);
					response.addHeader("state", "OKAY");
				}
			} else {
				response.addHeader("state", "FAILED_USER_NOT_FOUND");
			}
	}
	
	private void changeQuotationStatus(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(">> Changing Quotation State <<");
		DBConnector db = new DBConnector();
		String quotationId = request.getParameter("q_id");
		String approvement = request.getParameter("approve");
		String sql = "SELECT quotation_id FROM quotation WHERE quotation_id = " + quotationId;
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				if (approvement.equals("approve")) // Quotation approve
					sql = "UPDATE quotation SET status = 'approve' WHERE quotation_id = "+quotationId;
				else if (approvement.equals("disapprove")) // Quotation not approve
					sql = "UPDATE quotation SET status = 'notapprove' WHERE quotation_id = "+quotationId;
				else // Pending
					sql = "UPDATE quotation SET status = 'pending' WHERE quotation_id = "+quotationId;
				
				System.out.println(sql);
				db.execute(sql);
				response.addHeader("state", "OKAY");
			} else {
				response.addHeader("state", "FAILED_QUOTA_NOT_FOUND");
			}
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void updateQuotation(HttpServletRequest request, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		
		try {
			updateQuotation(db, request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void updateQuotation(DBConnector db, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int quotationId = Util.checkNullReturnZeroInt(request.getParameter("quotationId"));
		int customerId = Util.checkNullReturnZeroInt(request.getParameter("customerId"));
		int saleId = Util.checkNullReturnZeroInt(request.getParameter("saleNo"));
		int productId = Util.checkNullReturnZeroInt(request.getParameter("productId"));
		String additionalDetail = Util.checkNullReturnDash(request.getParameter("quotationAdd"));
		String priceDetail = Util.checkNullReturnDash(request.getParameter("priceDetail"));
		String approvement = request.getParameter("approve");
		
		String sql = "UPDATE reportbuild.quotation "
			+ "SET "
			+ "customer_id = "+customerId+", "
			+ "sale_id = "+saleId+", "
			+ "quotation_date = NOW(), "
			+ "despatch_require = '-', "
			+ "additional_detail = '"+additionalDetail+"', "
			+ "c_order_id = 1, "
			+ "your_ref = '-', "
			+ "price_detail = '"+priceDetail+"', "
			+ "status = '"+approvement+"' "
			+ "WHERE "
			+ "quotation_id = "+quotationId;
		
		db.execute(sql);
		if (productId == 0) {
			sql = "SELECT product_id FROM quotation WHERE quotation_id = "+quotationId;
			ResultSet rs = db.query(sql);
			if (rs.next())
				productId = rs.getInt(1);
		}
		
		Product.updateProduct(db, productId, request);
		response.addHeader("state", "UPDATED");
		
	}
	
	private void addOrUpdateQuotation(HttpServletRequest request, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		int quotationId = Util.checkNullReturnZeroInt(request.getParameter("quotationId"));
		try {
			String sql = "SELECT * FROM quotation WHERE quotation_id = "+quotationId;
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				updateQuotation(db, request, response);
			} else {
				addNewQuotation(db, request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void dropQuotationFromId(int id, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		try {
			String sql = "DELETE FROM Quotation WHERE quotation_id = " + id;
			db.execute(sql);
			response.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:" + e.getMessage());
		}
	}
	
}
