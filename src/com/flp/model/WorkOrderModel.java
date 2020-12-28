package com.flp.model;

public class WorkOrderModel {
	private int workorderId;
	private String workDate;
	private String workDispatch;
	private boolean workSprocketHole;
	private boolean workCrimping;
	private boolean workGlue;
	private String workFormFolding;
	private String workPackingSet;
	private String workPackingBox;
	private String workAdditional;
	private QuotationModel quotation;
	private CustomerModel customer;
	private ProductModel product;
	private SaleModel sale;
	
	public int getWorkorderId() {
		return workorderId;
	}
	public void setWorkorderId(int workorderId) {
		this.workorderId = workorderId;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getWorkDispatch() {
		return workDispatch;
	}
	public void setWorkDispatch(String workDispatch) {
		this.workDispatch = workDispatch;
	}
	public boolean isWorkSprocketHole() {
		return workSprocketHole;
	}
	public void setWorkSprocketHole(boolean workSprocketHole) {
		this.workSprocketHole = workSprocketHole;
	}
	public boolean isWorkCrimping() {
		return workCrimping;
	}
	public void setWorkCrimping(boolean workCrimping) {
		this.workCrimping = workCrimping;
	}
	public boolean isWorkGlue() {
		return workGlue;
	}
	public void setWorkGlue(boolean workGlue) {
		this.workGlue = workGlue;
	}
	public String getWorkFormFolding() {
		return workFormFolding;
	}
	public void setWorkFormFolding(String workFormFolding) {
		this.workFormFolding = workFormFolding;
	}
	public String getWorkPackingSet() {
		return workPackingSet;
	}
	public void setWorkPackingSet(String workPackingSet) {
		this.workPackingSet = workPackingSet;
	}
	public String getWorkPackingBox() {
		return workPackingBox;
	}
	public void setWorkPackingBox(String workPackingBox) {
		this.workPackingBox = workPackingBox;
	}
	public String getWorkAdditional() {
		return workAdditional;
	}
	public void setWorkAdditional(String workAdditional) {
		this.workAdditional = workAdditional;
	}
	public QuotationModel getQuotation() {
		return quotation;
	}
	public void setQuotation(QuotationModel quotation) {
		this.quotation = quotation;
	}
	public CustomerModel getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}
	public ProductModel getProduct() {
		return product;
	}
	public void setProduct(ProductModel product) {
		this.product = product;
	}
	public SaleModel getSale() {
		return sale;
	}
	public void setSale(SaleModel sale) {
		this.sale = sale;
	}
}
