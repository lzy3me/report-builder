package com.flp.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.SimpleAttributeSet;

import com.flp.db.DBConnector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@WebServlet(name = "report", urlPatterns = "/apis/report")
public class JasperGenReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String CONTENT_TYPE = "application/pdf; charset=UTF-8";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType(CONTENT_TYPE);
		resp.setHeader("Content-Disposition", "inline; fileName=\""+req.getParameter("name")+"_NO_"+req.getParameter("id")+".pdf\"");
		OutputStream outStream = resp.getOutputStream();
		HttpSession session = req.getSession(true);
		
		DBConnector db = new DBConnector();
		
		String initDir = getServletContext().getRealPath("/report/src/");
		String fileName = req.getParameter("name")+"_print.jrxml";
		System.out.println(initDir + fileName);
		JasperReport jasperReport;
		try {
			jasperReport = JasperCompileManager.compileReport(initDir + fileName);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("REPORT_DOC_ID", req.getParameter("id"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, db.getConnection());
			JRPdfExporter exporter = new JRPdfExporter();
			ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outStream);
			SimplePdfExporterConfiguration conf = new SimplePdfExporterConfiguration();
			
			exporter.setExporterInput(exporterInput);
			exporter.setExporterOutput(exporterOutput);
			exporter.setConfiguration(conf);
			exporter.exportReport();
		} catch (ClassNotFoundException | JRException | SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
			outStream.close();
//			out.close();
		}
	}

}
