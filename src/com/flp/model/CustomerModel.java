package com.flp.model;

public class CustomerModel {
	private int customerId;
	private String customerOrgName;
	private String customerAddr;
	private String customerContact;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int c_id) {
		this.customerId = c_id;
	}
	public String getCustomerOrgName() {
		return customerOrgName;
	}
	public void setCustomerOrgName(String c_org) {
		this.customerOrgName = c_org;
	}
	public String getCustomerAddr() {
		return customerAddr;
	}
	public void setCustomerAddr(String c_addr) {
		this.customerAddr = c_addr;
	}
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String c_contact) {
		this.customerContact = c_contact;
	}
}
