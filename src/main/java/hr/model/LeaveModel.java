package hr.model;

import java.util.Date;

public class LeaveModel 
{

	private int recNO;
	private int leaveid;
	private int empKey;
	private String status;
	private String leaveTypeDesc;
	private int noOfDays;
	private Date returnDate;
	
	private Date leaveStartDate;
	private Date leaveEndDate;
	
	private boolean returnStatus;

	private String payment;
	private String enCashStatus;
	
	private String leavReturnDate="";
	private String RefNumber;
	
	public int getRecNO() {
		return recNO;
	}
	public void setRecNO(int recNO) {
		this.recNO = recNO;
	}
	public int getLeaveid() {
		return leaveid;
	}
	public void setLeaveid(int leaveid) {
		this.leaveid = leaveid;
	}
	public int getEmpKey() {
		return empKey;
	}
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLeaveTypeDesc() {
		return leaveTypeDesc;
	}
	public void setLeaveTypeDesc(String leaveTypeDesc) {
		this.leaveTypeDesc = leaveTypeDesc;
	}
	public int getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public Date getLeaveStartDate() {
		return leaveStartDate;
	}
	public void setLeaveStartDate(Date leaveStartDate) {
		this.leaveStartDate = leaveStartDate;
	}
	public Date getLeaveEndDate() {
		return leaveEndDate;
	}
	public void setLeaveEndDate(Date leaveEndDate) {
		this.leaveEndDate = leaveEndDate;
	}
	public boolean isReturnStatus() {
		return returnStatus;
	}
	public void setReturnStatus(boolean returnStatus) {
		this.returnStatus = returnStatus;
	}
	
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getEnCashStatus() {
		return enCashStatus;
	}
	public void setEnCashStatus(String enCashStatus) {
		this.enCashStatus = enCashStatus;
	}
	public String getLeavReturnDate() {
		return leavReturnDate;
	}
	public void setLeavReturnDate(String leavReturnDate) {
		this.leavReturnDate = leavReturnDate;
	}
	public String getRefNumber() {
		return RefNumber;
	}
	public void setRefNumber(String refNumber) {
		RefNumber = refNumber;
	}
	
	
}
