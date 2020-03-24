package model;

public class CustomerActivitiesModel {
	private int sno;
	private Integer recno;
	private String type="";
	private String refNumber="";
	private String date="";
	private String amount="";
	private String status="";
	private String memo="";
	private String user="";
	
	//used for Status History	
	private String statusDesc = "";
	private String createdFrom = "";
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRefNumber() {
		return refNumber;
	}
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public Integer getRecno() {
		return recno;
	}
	public void setRecno(Integer recno) {
		this.recno = recno;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(String createdFrom) {
		this.createdFrom = createdFrom;
	}
	

}
