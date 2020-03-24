package hr.model;

import java.util.Date;

public class CompanyModel 
{
	
private int compKey;
private String enCompanyName;
private String arCompanyName;
private boolean canView;

private Date createDate; 
private int totalDepartment;
private int totalPositions;

private int dayWorkHours;
private int salaryCalcDays;
private String showSalaryColumn;
private int empSalaryCalcDay;
private int yearPayroll;
private int monthPayroll;
private String calculateActualDays;
private String includeHolidayUnit;


public int getCompKey() {
	return compKey;
}
public void setCompKey(int compKey) {
	this.compKey = compKey;
}
public String getEnCompanyName() {
	return enCompanyName;
}
public void setEnCompanyName(String enCompanyName) {
	this.enCompanyName = enCompanyName;
}
public String getArCompanyName() {
	return arCompanyName;
}
public void setArCompanyName(String arCompanyName) {
	this.arCompanyName = arCompanyName;
}
public boolean isCanView() {
	return canView;
}
public void setCanView(boolean canView) {
	this.canView = canView;
}
public Date getCreateDate() {
	return createDate;
}
public void setCreateDate(Date createDate) {
	this.createDate = createDate;
}
public int getTotalDepartment() {
	return totalDepartment;
}
public void setTotalDepartment(int totalDepartment) {
	this.totalDepartment = totalDepartment;
}
public int getTotalPositions() {
	return totalPositions;
}
public void setTotalPositions(int totalPositions) {
	this.totalPositions = totalPositions;
}
public int getDayWorkHours() {
	return dayWorkHours;
}
public void setDayWorkHours(int dayWorkHours) {
	this.dayWorkHours = dayWorkHours;
}
public int getSalaryCalcDays() {
	return salaryCalcDays;
}
public void setSalaryCalcDays(int salaryCalcDays) {
	this.salaryCalcDays = salaryCalcDays;
}
public String getShowSalaryColumn() {
	return showSalaryColumn;
}
public void setShowSalaryColumn(String showSalaryColumn) {
	this.showSalaryColumn = showSalaryColumn;
}
public int getEmpSalaryCalcDay() {
	return empSalaryCalcDay;
}
public void setEmpSalaryCalcDay(int empSalaryCalcDay) {
	this.empSalaryCalcDay = empSalaryCalcDay;
}
public int getYearPayroll() {
	return yearPayroll;
}
public void setYearPayroll(int yearPayroll) {
	this.yearPayroll = yearPayroll;
}
public int getMonthPayroll() {
	return monthPayroll;
}
public void setMonthPayroll(int monthPayroll) {
	this.monthPayroll = monthPayroll;
}
public String getCalculateActualDays() {
	return calculateActualDays;
}
public void setCalculateActualDays(String calculateActualDays) {
	this.calculateActualDays = calculateActualDays;
}
public String getIncludeHolidayUnit() {
	return includeHolidayUnit;
}
public void setIncludeHolidayUnit(String includeHolidayUnit) {
	this.includeHolidayUnit = includeHolidayUnit;
}
	
}
