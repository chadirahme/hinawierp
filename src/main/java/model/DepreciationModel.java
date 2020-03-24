package model;

import java.util.Date;

public class DepreciationModel 
{

	private int srNo;
	private Date PeriodStart;
	private Date PeriodEnd;
	private int Days;
	private double DepreciationAmount;
	private double AccDeprec;
	private double NetBookValue;
	private String Notes;
	
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public Date getPeriodStart() {
		return PeriodStart;
	}
	public void setPeriodStart(Date periodStart) {
		PeriodStart = periodStart;
	}
	public Date getPeriodEnd() {
		return PeriodEnd;
	}
	public void setPeriodEnd(Date periodEnd) {
		PeriodEnd = periodEnd;
	}
	public int getDays() {
		return Days;
	}
	public void setDays(int days) {
		Days = days;
	}
	public double getDepreciationAmount() {
		return DepreciationAmount;
	}
	public void setDepreciationAmount(double depreciationAmount) {
		DepreciationAmount = depreciationAmount;
	}
	public double getAccDeprec() {
		return AccDeprec;
	}
	public void setAccDeprec(double accDeprec) {
		AccDeprec = accDeprec;
	}
	public double getNetBookValue() {
		return NetBookValue;
	}
	public void setNetBookValue(double netBookValue) {
		NetBookValue = netBookValue;
	}
	public String getNotes() {
		return Notes;
	}
	public void setNotes(String notes) {
		Notes = notes;
	}
	
}
