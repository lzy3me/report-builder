package com.flp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flp.db.DBConnector;
import com.flp.util.Util;
import com.google.gson.Gson;

@WebServlet("/apis/customer")
public class Customer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		List lstData = new ArrayList();
		HttpSession session = request.getSession(true);
		
		try {
			System.out.print("Servlet: Customer doing >> ");
			String data = request.getParameter("cus");
			
			if (data != null) {
				if (data.equals("new")) {
					System.out.println("add new customer.");
					addCustomer(request, response);
				} else if (data.equals("addorupdate")) {
					System.out.println("add or update customer.");
					addOrUpdate(request, response);
				} else if (data.equals("getmaxid")) {
					System.out.println("get max id.");
					HashMap hm = new HashMap<String, Integer>();
					hm.put("maxid", getMaxId());
					lstData.add(hm);
				} else if (data.equals("getcustomer")) {
					System.out.println("getting customer list.");
					lstData = getCustomerList(request, response, session);
				} else if (data.equals("drop")) {
					System.out.println("drop customer id " + request.getParameter("customerId"));
					dropCustomer(Integer.parseInt(request.getParameter("customerId")), response);
				}
			}
			
			if (lstData != null) {
				out.print(new Gson().toJson(lstData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isHasId(int id, DBConnector db) throws SQLException {
		if (id != 0) {
		
			ResultSet rs = db.query("SELECT customer_id FROM customer WHERE customer_id = " + id);
			if (rs.next()) {
				System.out.println("Yes");
				return true;
			}
			rs.close();
		}
		
		return false;
	}
	
	private int getMaxId() {
		DBConnector db = new DBConnector();
		int maxid = 0;
		
		try {
			maxid = getMaxId(db);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		return maxid;
	}
	
	private int getMaxId(DBConnector db) throws SQLException {
		int value = 0;
		String sql = "SELECT MAX(customer_id) FROM customer LIMIT 0, 1";
		ResultSet rs = db.query(sql);
		if (rs.next()) {
			value = rs.getInt(1)+1;
		} else {
			value = 1;
		}
		return value;
	}
	
	private List getCustomerList(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		List lstMsg = new ArrayList();
		com.flp.model.CustomerModel lstData;
		DBConnector db = new DBConnector();
		
		String sql = "";
		String customerId = request.getParameter("customerId");
		
		if (customerId == null || customerId == "") {
			sql = "SELECT * FROM Customer LIMIT 0, 100";
		} else {
			sql = "SELECT * FROM Customer WHERE customer_id = "+customerId+" LIMIT 0, 1";
		}
		System.out.println(sql);
		
		ResultSet rs = db.query(sql);
		try {
			while (rs.next()) {
				lstData = new com.flp.model.CustomerModel();
				lstData.setCustomerId(rs.getInt(1));
				lstData.setCustomerOrgName(rs.getString(2));
				lstData.setCustomerAddr(rs.getString(3));
				lstData.setCustomerContact(rs.getString(4));
				lstMsg.add(lstData);
			}
			if (rs != null) rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		
		return lstMsg;
	}
	
	private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		String customerId = request.getParameter("customerId");
		String sql = "SELECT * FROM reportbuild.customer WHERE customer.customer_id = "+ customerId;
		try {
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				updateCustomer(db, request, response);
			} else {
				addCustomer(db, request, response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		}
	}
	
	private void addCustomer(HttpServletRequest request, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		
		try {
			addCustomer(db, request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		}
	}
	
	private void addCustomer(DBConnector db, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int customerId = Util.checkNullReturnZeroInt(request.getParameter("customerName"));
		String customerOrgName = request.getParameter("customerName");
		String customerAddr = request.getParameter("customerAddr");
		String customerContact = request.getParameter("customerContact");
		
		String sql = "";
		if (customerOrgName == null || customerOrgName == "") {
			response.addHeader("state", "ORG_NAME_EMPTY");
		} else {
			sql = "INSERT INTO customer VALUES ("+customerId+", '"+customerOrgName+"', '"+customerAddr+"', '"+customerContact+"')";
			db.execute(sql);
			response.addHeader("state", "OKAY");
		}
	}
	
	private void updateCustomer(DBConnector db, HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int customerId = Util.checkNullReturnZeroInt(request.getParameter("customerId"));
		String customerOrgName = request.getParameter("customerName");
		String customerAddr = Util.checkNullReturnDash(request.getParameter("customerAddr"));
		String customerContact = Util.checkNullReturnDash(request.getParameter("customerContact"));
		
		String sql = "UPDATE reportbuild.customer "
			+ "SET "
			+ "c_organization_name = '"+customerOrgName+"', "
			+ "c_address = '"+customerAddr+"', "
			+ "c_contact = '"+customerContact+"' "
			+ "WHERE customer_id = "+customerId;
		db.execute(sql);
		response.addHeader("state", "UPDATED");
	}
	
	private void dropCustomer(int id, HttpServletResponse response) {
		DBConnector db = new DBConnector();
		String sql = "DELETE FROM reportbuild.customer WHERE customer.customer_id = "+id;
		
		try {
			db.execute(sql);
			response.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			response.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
}
