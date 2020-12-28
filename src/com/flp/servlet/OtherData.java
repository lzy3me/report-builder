package com.flp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flp.db.DBConnector;
import com.flp.model.PaperModel;
import com.flp.model.SaleModel;
import com.flp.util.Util;
import com.google.gson.Gson;

@WebServlet(name = "otherdata", urlPatterns = "/apis/all")
public class OtherData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json; charset=utf-8";
	private PrintWriter out;
	
	@Override
	protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		List lstData = new ArrayList();
		HttpSession session = req.getSession(true);
		out = resp.getWriter();
		
		String data = req.getParameter("p");

		if (data != null) {
			System.out.print("Servlet: Other doing >> ");
			// SALE
			if (data.equals("addsale")) {
				System.out.println("adding new sale.");
				addSale(req, resp);
			} else if (data.equals("getsalemaxid")) {
				System.out.println("get sale max id.");
				HashMap hm = new HashMap<String, Integer>();
				hm.put("saleId", getSaleMaxId());
				lstData.add(hm);
			} else if (data.equals("editsale")) {
				System.out.println("updating sale.");
				updateSale(req, resp);
			} else if (data.equals("addupdatesale")) {
				System.out.println("adding or updating sale.");
				addOrUpdateSale(req, resp);
			} else if (data.equals("salelist")) {
				System.out.println("getting sale list.");
				lstData = getSale(resp, true, 0);
			} else if (data.equals("sale")) {
				System.out.println("getting sale on: "+req.getParameter("id"));
				lstData = getSale(resp, false, Integer.parseInt(req.getParameter("id")));
			} else if (data.equals("dropsale")) {
				System.out.println("drop sale on: "+req.getParameter("id"));
				dropSale(Integer.parseInt(req.getParameter("id")), resp);
				
			// PAPER
			} else if (data.equals("paperlist")) {
				System.out.println("getting paper list.");
				lstData = getPaper(resp, true, 0);
			} else if (data.equals("getpapermaxid")) {
				System.out.println("get paper max id.");
				HashMap hm = new HashMap<String, Integer>();
				hm.put("paperId", getPaperMaxId());
				lstData.add(hm);
			} else if (data.equals("addupdatepaper")) {
				System.out.println("adding or updating paper.");
				addOrUpdatePaper(req, resp);
			} else if (data.equals("addpaper")) {
				System.out.println("adding new paper.");
				addPaper(req, resp);
			} else if (data.equals("editpaper")) {
				System.out.println("updating paper.");
				updatePaper(req, resp);
			} else if (data.equals("paper")) {
				System.out.println("getting paper on: "+req.getParameter("id"));
				lstData = getPaper(resp, false, Integer.parseInt(req.getParameter("id")));
			} else if (data.equals("paperdef")) {
				System.out.println("getting paper default");
				lstData = getPaper(resp, false, 0);
			} else if (data.equals("droppaper")) {
				System.out.println("drop paper on: "+req.getParameter("id"));
				dropPaper(Integer.parseInt(req.getParameter("id")), resp);
			} else {
				resp.setStatus(405);
				resp.addHeader("state", "[GET]METHOD_NOT_ALLOWED");
			}
		}
		if (lstData != null) out.print(new Gson().toJson(lstData));
	}
	
	@Override
	protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	/*
	 * Sale Section
	 */
	private int getSaleMaxId () {
		DBConnector db = new DBConnector();
		int saleId = 0;
		
		try {
			saleId = getSaleMaxId(db);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		
		return saleId;
	}
	
	private int getSaleMaxId (DBConnector db) throws SQLException {
		int saleId = 0;
		
		String sql = "SELECT MAX(sale_id) FROM reportbuild.sale LIMIT 0, 1";
		ResultSet rs = db.query(sql);
		if (rs.next()) {
			saleId = rs.getInt(1)+1;
		} else {
			saleId = 1;
		}
		
		if (rs != null) rs.close();
		return saleId;
	}
	
	private List getSale (HttpServletResponse resp, boolean isGetList, int id) {
		DBConnector db = new DBConnector();
		List lstMsg = new ArrayList();
		SaleModel lstData;
		String sql = "";
		
		try {
			if (isGetList == true) {
				sql = "SELECT * FROM reportbuild.sale";
			} else {
				sql = "SELECT * FROM reportbuild.sale WHERE sale.sale_id = " + id;
			}
			ResultSet rs = db.query(sql);
			while (rs.next()) {
				lstData = new SaleModel();
				lstData.setSaleId(rs.getInt(1));
				lstData.setSaleNo(rs.getString(2));
				lstData.setSaleFirstName(rs.getString(3));
				lstData.setSaleLastName(rs.getString(4));
				lstMsg.add(lstData);
			}
			rs.close();
			resp.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
		return lstMsg;
	}
	
	private void addOrUpdateSale (HttpServletRequest req, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		
		String saleId = req.getParameter("saleId");
		try {
			String sql = "SELECT * FROM reportbuild.sale WHERE sale.sale_id = "+ saleId;
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				updateSale(db, req, resp);
			} else {
				addSale(db, req, resp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void addSale (HttpServletRequest req, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		try {
			addSale(db, req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void addSale (DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		int saleId = Util.checkNullReturnZeroInt(req.getParameter("saleId"));
		String saleNo = req.getParameter("saleNo");
		String saleFirstName = req.getParameter("saleFirstName");
		String saleLastName = req.getParameter("saleLastName");
		
		String sql = "INSERT INTO "
			+ "reportbuild.sale "
			+ "(sale_id, "
			+ "sale_no, "
			+ "s_fname, "
			+ "s_lname) "
			+ "VALUES "
			+ "("+saleId+", "
			+ "'"+saleNo+"', "
			+ "'"+saleFirstName+"', "
			+ "'"+saleLastName+"')";
		db.execute(sql);
		resp.addHeader("state", "OKAY");
	}
	
	private void updateSale (HttpServletRequest req, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		try {
			updateSale(db, req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void updateSale (DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		int saleId = Util.checkNullReturnZeroInt(req.getParameter("saleId"));
		String saleNo = req.getParameter("saleNo");
		String saleFirstName = req.getParameter("saleFirstName");
		String saleLastName = req.getParameter("saleLastName");
		
		String sql = "UPDATE "
			+ "reportbuild.sale "
			+ "SET "
			+ "s_fname = '"+saleFirstName+"', "
			+ "s_lname = '"+saleLastName+"' "
			+ "WHERE "
			+ "sale_id = "+ saleId;
		db.execute(sql);
		resp.addHeader("state", "UPDATED");
	}
	
	private void dropSale (int id, HttpServletResponse resp) {
		DBConnector db = new DBConnector(); 
		String sql = "DELETE FROM reportbuild.sale WHERE sale.sale_id = "+id;
		
		try {
			db.execute(sql);
			resp.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	/*
	 * Peper Section
	 */
	private int getPaperMaxId () {
		DBConnector db = new DBConnector();
		int paperId = 0;
		
		try {
			paperId = getPaperMaxId(db);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (db != null) db.close();
		}
		
		return paperId;
	}
	
	private int getPaperMaxId (DBConnector db) throws SQLException {
		int paperId = 0;
		
		String sql = "SELECT MAX(paper_id) FROM reportbuild.paper LIMIT 0, 1";
		ResultSet rs = db.query(sql);
		if (rs.next()) {
			paperId = rs.getInt(1)+1;
		} else {
			paperId = 1;
		}
		
		if (rs != null) rs.close();
		return paperId;
	}
	
	private List getPaper (HttpServletResponse resp, boolean isGetList, int id) {
		DBConnector db = new DBConnector();
		List lstMsg = new ArrayList();
		PaperModel lstData;
		String sql = "";
		
		try {
			if (isGetList == true) {
				sql = "SELECT * FROM reportbuild.paper";
			} else if (id != 0) {
				sql = "SELECT * FROM reportbuild.paper WHERE paper.paper_id = " + id;
			} else {
				sql = "SELECT * FROM reportbuild.paper WHERE paper.paper_default = 'top' OR paper.paper_default = 'bottom'";
			}
			ResultSet rs = db.query(sql);
			while (rs.next()) {
				lstData = new PaperModel();
				lstData.setPaperId(rs.getInt(1));
				lstData.setPaperType(rs.getString(2));
				lstData.setPaperWeight(rs.getDouble(3));
				lstData.setPaperColor(rs.getString(4));
				lstData.setPaperDefault(rs.getString(5));
				lstMsg.add(lstData);
			}
			rs.close();
			resp.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
		return lstMsg;
	}
	
	private void addOrUpdatePaper (HttpServletRequest req, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		
		String paperId = req.getParameter("paperId");
		try {
			String sql = "SELECT * FROM reportbuild.paper WHERE paper.paper_id = "+ paperId;
			ResultSet rs = db.query(sql);
			if (rs.next()) {
				updatePaper(db, req, resp);
			} else {
				addPaper(db, req, resp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void addPaper (HttpServletRequest req, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		
		try {
			addPaper(db, req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void addPaper (DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		int paperId = Util.checkNullReturnZeroInt(req.getParameter("paperId"));
		String paperType = req.getParameter("paperType");
		double paperWeight = Double.parseDouble(req.getParameter("paperWeight")==null?"0":req.getParameter("paperWeight"));
		double paperPrice = Double.parseDouble(req.getParameter("paperPrice")==null?"0":req.getParameter("paperPrice"));
		String paperColor = req.getParameter("paperColor");
		String paperDefault = req.getParameter("paperDefault");
		String sql = "";
		
		
		sql = "INSERT INTO "
			+ "reportbuild.paper "
			+ "(paper_id, "
			+ "paper_name, "
			+ "paper_weight, "
			+ "paper_color, "
			+ "paper_default) "
			+ "VALUES "
			+ "("+paperId+", "
			+ "'"+paperType+"', "
			+ paperWeight+", "
			+ "'"+paperColor+"', ";
		if (paperDefault != null) {
			sql += "'"+paperDefault+"')";				
		} else {
			sql += "NULL)";
		}
		System.out.println(sql);
		db.execute(sql);
		resp.addHeader("state", "OKAY");
		
	}
	
	private void updatePaper (HttpServletRequest req, HttpServletResponse resp) {
		DBConnector db = new DBConnector();
		
		try {
			updatePaper(db, req, resp);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}
	
	private void updatePaper (DBConnector db, HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		int paperId = Util.checkNullReturnZeroInt(req.getParameter("paperId"));
		String paperType = req.getParameter("paperType");
		double paperWeight = Double.parseDouble(req.getParameter("paperWeight")==null?"0":req.getParameter("paperWeight"));
		String paperColor = req.getParameter("paperColor");
		String paperDefault = req.getParameter("paperDefault");
		
		String sql = "UPDATE "
			+ "reportbuild.paper "
			+ "SET ";
			if (paperType != null)
				sql += "paper_name = '"+paperType+"', ";
			if (paperWeight != 0)
				sql += "paper_weight = "+paperWeight+", ";
			if (paperColor != null)
				sql += "paper_color = '"+paperColor+"', ";
			if (paperDefault != null)
				sql += "paper_default = '"+paperDefault+"' ";
			else
				sql += "paper_default = NULL ";
			sql += "WHERE "
				+ "paper_id = "+ paperId;
		System.out.println(sql);
			
			db.execute(sql);
			resp.addHeader("state", "UPDATED");
		
	}
	
	private void dropPaper (int id, HttpServletResponse resp) {
		DBConnector db = new DBConnector(); 
		String sql = "DELETE FROM reportbuild.paper WHERE paper.paper_id = "+id;
		
		try {
			db.execute(sql);
			resp.addHeader("state", "OKAY");
		} catch (SQLException e) {
			e.printStackTrace();
			resp.addHeader("state", "ERR_EXCEPTION:"+e.getMessage());
		} finally {
			if (db != null) db.close();
		}
	}

}
