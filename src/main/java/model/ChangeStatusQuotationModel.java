package model;

import java.util.Date;

public class ChangeStatusQuotationModel {
	private Date quotationDate;
	private String prospectiveName;
	private String customerName;
	private String status;
	private Date lastChangeDate;
	private String note;
	private String other;
	private Date changeDate;
	private double amount;
	
	
	public Date getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastChangeDate() {
		return lastChangeDate;
	}
	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getProspectiveName() {
		return prospectiveName;
	}
	public void setProspectiveName(String prospectiveName) {
		this.prospectiveName = prospectiveName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	

}
