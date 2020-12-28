package com.flp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.groovy.runtime.metaclass.MethodMetaProperty.GetMethodMetaProperty;

import com.flp.db.DBConnector;
import com.flp.model.ProductModel;
import com.flp.model.ProductSpecificationModel;
import com.flp.util.Util;
import com.google.gson.Gson;

@WebServlet(name = "product", urlPatterns = "/apis/product")
public class Product extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		PrintWriter out = resp.getWriter();
		List lstData = new ArrayList();
		HttpSession session = req.getSession(true);
		
		String data = req.getParameter("p");

		if (data != null) {
			System.out.print("Servlet: Product doing >> ");
			if (data.equals("addproduct")) {
				System.out.println("add new product.");
				addProduct(req, resp);
			} else {
				resp.setStatus(405);
				resp.addHeader("state", "[POST]METHOD_NOT_ALLOWED");
			}
		}
		if (lstData != null) {
			out.print(new Gson().toJson(lstData));
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		PrintWriter out = resp.getWriter();
		List lstData = new ArrayList();
		HttpSession session = req.getSession(true);
		
		String data = req.getParameter("p");
		
		if (data != null) {
			System.out.print("Servlet: Product doing >> ");
			if (data.equals("addproduct")) {
				System.out.println("add new product.");
				addProduct(req, resp);
			} else if (data.equals("getps")) {
				System.out.println("getting product specification.");
				lstData = getProductSpecificate(req, resp);
			} else {
				resp.setStatus(405);
				resp.addHeader("state", "[GET]METHOD_NOT_ALLOWED");
			}
		}
		if (lstData != null) {
			out.print(new Gson().toJson(lstData));
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		PrintWriter out = resp.getWriter();
		List lstData = new ArrayList();
		HttpSession session = req.getSession(true);
		
		String data = req.getParameter("p");
		
		if (data != null) {
			System.out.print("Servlet: Product doing >> ");
			if (data.equals("drop")) {
				System.out.println("drop product");
				dropProduct(req, resp);
			} else {
				resp.setStatus(405);
				resp.addHeader("state", "[DELETE]METHOD_NOT_ALLOWED");
			}
		}
		if (lstData != null) {
			out.print(new Gson().toJson(lstData));
		}
	}
	
	private static int getMaxId(DBConnector db) throws SQLException {
		int maxId = 0;
		String sql = "SELECT MAX(product_id) FROM product LIMIT 0, 1";
		ResultSet rs = db.query(sql);
		if (rs.next()) {
			maxId = rs.getInt(1)+1;
		} else {
			maxId = 1;
		}
		return maxId;
	}
	
	public static int addProduct(HttpServletRequest request, HttpServletResponse response) {
		int id = 1;
		DBConnector db = new DBConnector();
		
		try {
			id = addProduct(db, request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		
		return id;
	}

	public static int addProduct(DBConnector db, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int id = 1;
		String productName 		= Util.checkNullReturnDash(request.getParameter("productName"));
		String productQuatity 	= Util.checkNullReturnDash(request.getParameter("productQuantity"));
		String productTotalPage = Util.checkNullReturnDash(request.getParameter("productTpage"));
		int productConstat 		= Util.checkBoolReturnInt(Boolean.valueOf(request.getParameter("productConstat")));
		int productUnitSet 		= Util.checkBoolReturnInt(Boolean.valueOf(request.getParameter("productUnitSet")));
		String productOthers 	= Util.checkNullReturnDash(request.getParameter("productOthers"));
		String productHeight 	= Util.checkNullReturnDash(request.getParameter("productHeight"));
		String productWidth 	= Util.checkNullReturnDash(request.getParameter("productWidth"));
		String productStart 	= Util.checkNullReturnDash(request.getParameter("productStart"));
		String productEnd 		= Util.checkNullReturnDash(request.getParameter("productEnd"));

		String sql = "SELECT MAX(product.product_id)+1 FROM reportbuild.product LIMIT 0, 1";
		ResultSet rs = db.query(sql);
		if (rs.next()) {
			id = rs.getInt(1);
		}
		rs.close();
		sql = "INSERT INTO reportbuild.product"
			+ "(product_id, "
			+ "product_name, "
			+ "product_quantity, "
			+ "product_total_page, "
			+ "product_constat, "
			+ "product_other, "
			+ "product_unit_set, "
			+ "product_form_depth, "
			+ "product_form_width, "
			+ "product_start_page, "
			+ "product_end_page) "
			+ "VALUES "
			+ "("+id+", "
			+ "'"+productName+"', "
			+ "'"+productQuatity+"', "
			+ "'"+productTotalPage+"', "
			+ "'"+productConstat+"', "
			+ "'"+productOthers+"', "
			+ "'"+productUnitSet+"', "
			+ "'"+productHeight+"', "
			+ "'"+productWidth+"', "
			+ "'"+productStart+"', "
			+ "'"+productEnd+"')";
		db.execute(sql);
		addProductSpec(db, id, request);

		return id;
	}
	
	public static List getProduct(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(">> Grathering Product <<");
		List lstMsg = new ArrayList();
		ProductModel lstData;
		DBConnector db = new DBConnector();
		String productId = Util.checkNullReturnZero(request.getParameter("productId"));
		String sql = "SELECT "
				+ "product.product_id, "
				+ "product.product_name, "
				+ "product.product_quantity, "
				+ "product.product_total_page, "
				+ "product.product_constat, "
				+ "product.product_other, "
				+ "product.product_unit_set, "
				+ "product.product_form_depth, "
				+ "product.product_form_width, "
				+ "product.product_start_page, "
				+ "product.product_end_page "
				+ "FROM "
				+ "product "
				+ "WHERE product.product_id = " + productId;
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				lstData = new ProductModel();
				lstData.setProductId(rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:" + e.getMessage());
		}
		return lstMsg;
	}
	
	public static void updateProduct(DBConnector db, int productId, HttpServletRequest request) throws SQLException {
		System.out.println(">> Update Product <<");
		String productName 		= Util.checkNullReturnDash(request.getParameter("productName"));
		String productQuatity 	= Util.checkNullReturnDash(request.getParameter("productQuantity"));
		String productTotalPage = Util.checkNullReturnDash(request.getParameter("productTpage"));
		int productConstat 		= Util.checkBoolReturnInt(Boolean.valueOf(request.getParameter("productConstat")));
		int productUnitSet 		= Util.checkBoolReturnInt(Boolean.valueOf(request.getParameter("productUnitSet")));
		String productOthers 	= Util.checkNullReturnDash(request.getParameter("productOthers"));
		String productHeight 	= Util.checkNullReturnDash(request.getParameter("productHeight"));
		String productWidth 	= Util.checkNullReturnDash(request.getParameter("productWidth"));
		String productStart 	= Util.checkNullReturnDash(request.getParameter("productStart"));
		String productEnd 		= Util.checkNullReturnDash(request.getParameter("productEnd"));
		
		String sql = "UPDATE reportbuild.product "
			+ "SET "
			+ "product_name = '"+productName+"', "
			+ "product_quantity = '"+productQuatity+"', "
			+ "product_total_page = '"+productTotalPage+"', "
			+ "product_constat = '"+productConstat+"', "
			+ "product_other = '"+productOthers+"', "
			+ "product_unit_set = '"+productUnitSet+"', "
			+ "product_form_depth = '"+productHeight+"', "
			+ "product_form_width = '"+productWidth+"', "
			+ "product_start_page = '"+productStart+"', "
			+ "product_end_page = '"+productEnd+"' "
			+ "WHERE "
			+ "product_id = "+productId;
		
		db.execute(sql);
		dropProductSpec(db, productId);
		addProductSpec(db, productId, request);
	}

	private void dropProduct(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(">> Drop Product <<");
		DBConnector db = new DBConnector();
		int productId = Util.checkNullReturnZeroInt(request.getParameter("productId"));
		
		try {
			dropProductSpec(db, productId);
			dropProductFromId(db, productId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void dropProductFromId(DBConnector db, int productId) throws SQLException {
		db.execute("DELETE FROM reportbuild.product WHERE product_id = " + productId);
	}

	private static void addProductSpec(DBConnector db, int productId, HttpServletRequest request) throws SQLException {
		System.out.println(">> Adding Product Specification List <<");
		int psCounter = Integer.parseInt(request.getParameter("productTpage"));
		String sql = "";
		
		for (int i=0; i<psCounter; i++) {
			String psType 		 = Util.checkNullReturnDash(request.getParameter("psType"+i));
			String psInkOnFace 	 = Util.checkNullReturnDash(request.getParameter("psInkOnFace"+i));
			String psInkBackFace = Util.checkNullReturnDash(request.getParameter("psInkBackFace"+i));
			int psHorz 			 = Util.checkBoolReturnInt(Boolean.valueOf(request.getParameter("psHorz"+i)));
			int psVert 			 = Util.checkBoolReturnInt(Boolean.valueOf(request.getParameter("psVert"+i)));
			String psRemark 	 = Util.checkNullReturnDash(request.getParameter("psRemark"+i));
			sql = "INSERT INTO "
					+ "reportbuild.product_specification"
					+ "(product_id, "
					+ "paper_id, "
					+ "part_ink_onface, "
					+ "part_ink_backface, "
					+ "part_horz_perf, "
					+ "part_vert_perf, "
					+ "part_remark) "
					+ "VALUES "
					+ "("+productId+", "
					+ psType+", "
					+ "'"+psInkOnFace+"', "
					+ "'"+psInkBackFace+"', "
					+ "'"+psHorz+"', "
					+ "'"+psVert+"', "
					+ "'"+psRemark+"');";
			db.execute(sql);
			System.out.println("Query >> "+sql);
		}
	}
	
	public static List getProductSpecificate(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(">> Grathering Product Specificate <<");
		List lstMsg = new ArrayList();
		ProductSpecificationModel lstData;
		DBConnector db = new DBConnector();
		String productId = Util.checkNullReturnZero(request.getParameter("productId"));
		String sql = "SELECT " + 
				"product_specification.paper_id, " +  
				"product_specification.part_ink_onface, " + 
				"product_specification.part_ink_backface, " + 
				"product_specification.part_horz_perf, " + 
				"product_specification.part_vert_perf, " + 
				"product_specification.part_remark " + 
				"FROM " + 
				"product_specification " +
				"WHERE " + 
				"product_specification.product_id = " + productId;
		try {
			ResultSet rs = db.query(sql);
			while (rs.next()) {
				lstData = new ProductSpecificationModel();
				lstData.setPaperId(rs.getInt(1));
				lstData.setPsInkOnFace(rs.getString(2));
				lstData.setPsInkBackFace(rs.getString(3));
				lstData.setPsHorzPerf(rs.getString(4));
				lstData.setPsVertPerf(rs.getString(5));
				lstData.setPsRemark(rs.getString(6));
				lstMsg.add(lstData);
			}
			response.addHeader("state", "OKAY");
			if (rs != null) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
		return lstMsg;
	}
	
	private static void dropProductSpec(DBConnector db, int productId) throws SQLException {
		System.out.println(">> Drop Product <<");
		db.execute("DELETE FROM reportbuild.product_specification WHERE product_id = " + productId);
	}
}
