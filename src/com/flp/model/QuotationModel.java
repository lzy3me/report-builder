package com.flp.model;

import java.util.Date;

import com.flp.servlet.Product;

public class QuotationModel {
	private int quotationId;
	private Date quotationDate;
	private int saleId;
	private String dispatch;
	private String additionalDetail;
	private String priceDetail;
	private String approve;
	private ProductModel productObject;
	private CustomerModel customerObject;
	
	public int getQuotationId() {
		return quotationId;
	}
	public void setQuotationId(int quotationId) {
		this.quotationId = quotationId;
	}
	public Date getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public String getAdditionalDetail() {
		return additionalDetail;
	}
	public void setAdditionalDetail(String additionalDetail) {
		this.additionalDetail = additionalDetail;
	}
	public String getPriceDetail() {
		return priceDetail;
	}
	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public ProductModel getProductObject() {
		return productObject;
	}
	public void setProductObject(ProductModel productObject) {
		this.productObject = productObject;
	}
	public CustomerModel getCustomerObject() {
		return customerObject;
	}
	public void setCustomerObject(CustomerModel customerObject) {
		this.customerObject = customerObject;
	}
	
}
