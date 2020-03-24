package model;

import java.util.Date;

public class LoanModel 
{

	private int id;
	private int empKey;
	private Date loanDate;
	private String status;
	private String reason;
	private double loanAmount;
	private int month;
	private int year;
	private int noOfInstallment;
	private double installAmount;
	private String note;
	private String loanDateString="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEmpKey() {
		return empKey;
	}
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	public Date getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getNoOfInstallment() {
		return noOfInstallment;
	}
	public void setNoOfInstallment(int noOfInstallment) {
		this.noOfInstallment = noOfInstallment;
	}
	public double getInstallAmount() {
		return installAmount;
	}
	public void setInstallAmount(double installAmount) {
		this.installAmount = installAmount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getLoanDateString() {
		return loanDateString;
	}
	public void setLoanDateString(String loanDateString) {
		this.loanDateString = loanDateString;
	}
	
	
	
}
