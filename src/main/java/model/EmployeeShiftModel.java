package model;

import java.util.Date;


public class EmployeeShiftModel implements Comparable<EmployeeShiftModel>
{

	 //@Override
	  public int compareTo(EmployeeShiftModel o) {
	    return getPosition().compareTo(o.getPosition());
	  }
	
	private int companyKey;
	private int employeeKey;
	private String employeeNo;
	private String englishName;
	private String arabicName;
	private int positionID;
	private String position;
	private Date employeementDate;
	private boolean employeeAssignShift[];
	private int shiftkey;
	private String shiftFromDate;
	private String shiftToDate;
	private boolean oldShift;
	
	public int getEmployeeKey() {
		return employeeKey;
	}
	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getArabicName() {
		return arabicName;
	}
	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}
	public int getPositionID() {
		return positionID;
	}
	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Date getEmployeementDate() {
		return employeementDate;
	}
	public void setEmployeementDate(Date employeementDate) {
		this.employeementDate = employeementDate;
	}
	public boolean[] getEmployeeAssignShift() {
		return employeeAssignShift;
	}
	public void setEmployeeAssignShift(boolean[] employeeAssignShift) {
		this.employeeAssignShift = employeeAssignShift;
	}
	public int getShiftkey() {
		return shiftkey;
	}
	public void setShiftkey(int shiftkey) {
		this.shiftkey = shiftkey;
	}
	public String getShiftFromDate() {
		return shiftFromDate;
	}
	public void setShiftFromDate(String shiftFromDate) {
		this.shiftFromDate = shiftFromDate;
	}
	public String getShiftToDate() {
		return shiftToDate;
	}
	public void setShiftToDate(String shiftToDate) {
		this.shiftToDate = shiftToDate;
	}
	public boolean isOldShift() {
		return oldShift;
	}
	public void setOldShift(boolean oldShift) {
		this.oldShift = oldShift;
	}
	public int getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(int companyKey) {
		this.companyKey = companyKey;
	}
	
	
}
