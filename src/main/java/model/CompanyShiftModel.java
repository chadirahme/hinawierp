package model;

import java.util.Date;

public class CompanyShiftModel 
{

	private int recNo;
	private int dayNo;
	private String offDay;
	private double hours;
	private int emp_key;
	private int comp_key;
	private Date fromDate;
	private Date toDate;
	private int shiftKey;
	private String timingFlag;
	private int unitkey;
	private String paidNormal;
	private String paidIfWork;
	private Date fromTime;
	private Date toTime;
	
	public int getDayNo() {
		return dayNo;
	}
	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}
	public String getOffDay() {
		return offDay;
	}
	public void setOffDay(String offDay) {
		this.offDay = offDay;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public int getShiftKey() {
		return shiftKey;
	}
	public void setShiftKey(int shiftKey) {
		this.shiftKey = shiftKey;
	}
	public String getTimingFlag() {
		return timingFlag;
	}
	public void setTimingFlag(String timingFlag) {
		this.timingFlag = timingFlag;
	}
	public int getUnitkey() {
		return unitkey;
	}
	public void setUnitkey(int unitkey) {
		this.unitkey = unitkey;
	}
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	/**
	 * @return the emp_key
	 */
	public int getEmp_key() {
		return emp_key;
	}
	/**
	 * @param emp_key the emp_key to set
	 */
	public void setEmp_key(int emp_key) {
		this.emp_key = emp_key;
	}
	/**
	 * @return the comp_key
	 */
	public int getComp_key() {
		return comp_key;
	}
	/**
	 * @param comp_key the comp_key to set
	 */
	public void setComp_key(int comp_key) {
		this.comp_key = comp_key;
	}
	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getPaidNormal() {
		return paidNormal;
	}
	public void setPaidNormal(String paidNormal) {
		this.paidNormal = paidNormal;
	}
	public String getPaidIfWork() {
		return paidIfWork;
	}
	public void setPaidIfWork(String paidIfWork) {
		this.paidIfWork = paidIfWork;
	}
	public Date getFromTime() {
		return fromTime;
	}
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public Date getToTime() {
		return toTime;
	}
	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}
	
	
}
