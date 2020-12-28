package com.flp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.google.gson.Gson;

@WebServlet("/apis/utils")
public class Utils extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		PrintWriter out = resp.getWriter();
		List lstData = new ArrayList();
		HttpSession session = req.getSession();
		
		try {
			System.out.print("Servlet: Quotation doing >> ");
			String data = req.getParameter("get");
			
			if (data != null) {
				if (data.equals("count")) {
					System.out.println("add new quotation.");
					lstData = dashboardData();
				}	
			}
			
			if (lstData != null) {
				out.print(new Gson().toJson(lstData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List dashboardData() {
		List lstMsg = new ArrayList();
		DBConnector db = new DBConnector();
		
		int countQuotation = Quotation.count(db);
		int countWorkorder = Workorder.count(db);
		HashMap<String, Integer> lstData = new HashMap<String, Integer>();
		
		lstData.put("quotation", countQuotation);
		lstData.put("workorder", countWorkorder);
		lstMsg.add(lstData);
		
		if (db != null) db.close();
		
		return lstMsg;
	}
	
}
