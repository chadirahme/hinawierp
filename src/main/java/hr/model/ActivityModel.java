package hr.model;

import java.util.Date;

public class ActivityModel 
{

	private int empKey;
	private String empNo;
	private String employeeName;
	private Date activityDate;
	private String activity;
	private String activityItem;
	private Date fromDate;
	private Date toDate;
	private String noofDays;
	private double amount;
	private double balance;
	private String status;
	private String employeeStatus;
	
	private String department;	
	private String position;
	private Date employeementDate;
	private String nationality;//as country in EmployeeModel
	
	
	
	private String fromHour;
	private String toHour;
	
	public int getEmpKey() {
		return empKey;
	}
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getActivityItem() {
		return activityItem;
	}
	public void setActivityItem(String activityItem) {
		this.activityItem = activityItem;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getNoofDays() {
		return noofDays;
	}
	public void setNoofDays(String noofDays) {
		this.noofDays = noofDays;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	public String getFromHour() {
		return fromHour;
	}
	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}
	public String getToHour() {
		return toHour;
	}
	public void setToHour(String toHour) {
		this.toHour = toHour;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
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
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	
	
	
}
