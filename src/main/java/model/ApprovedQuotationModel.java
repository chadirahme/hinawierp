package model;

import java.util.Date;

public class ApprovedQuotationModel {
	private int recNo;
	private double amount;
	private String itemName;
	private String custmorName;
	private String entity;
	private String refNumber;
	private String sendVia;
	private Date txnDate;
	private String memo;
	private boolean show;
	
	
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCustmorName() {
		return custmorName;
	}
	public void setCustmorName(String custmorName) {
		this.custmorName = custmorName;
	}
	public String getRefNumber() {
		return refNumber;
	}
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	public String getSendVia() {
		return sendVia;
	}
	public void setSendVia(String sendVia) {
		this.sendVia = sendVia;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	

}
