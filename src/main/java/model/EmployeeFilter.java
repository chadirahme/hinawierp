package model;

import java.util.Date;

public class EmployeeFilter 
{
	private String tsStatus="";
	private String EmployeeNo="";	
	private String FullName="";	
	private String Department="";	
	private String Position="";
	private String Country="";
	private String Age="";
	
	private String enFirstName="";
	private String enMiddleName="";
	private String enLastName="";
	private String employeeStatus="";
	private String gender="";
	private String maritalStatus="";
	
	private String empAllowed="";
	private String vacancies="";
	private String cvs="";
	private String available="";
	private String companyName="";
	
	private String groupName="";
	private String supervisorName="";
	private String isActive="";
	
	private String projectName="";
	
	private String shiftFromDate="";
	private String shiftToDate="";
	private String shiftName="";
	
	private String shiftType;
	
	private String statusDescription="";
	
	private String status="";
	
	private String supervisorNumber="";
	
	
	// approve or reject leave
			
	private String leaveCreateDate="";
	
	private String leaveStartDate="";
	
	private String leaveEndDate="";
	
	private String leaveType="";
	
	private String leaveReason="";
	
	private String empName="";
		
	private String empNo="";
	
	private String reason="";
	
	private String super_supervisorName="";
	
	
	
	public String getFullName() {
		return FullName;
	}
	public void setFullName(String fullName) {
		FullName = fullName==null?"":fullName.trim();
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department==null?"":department.trim();
	}
	public String getPosition() {
		return Position;
	}
	public void setPosition(String position) {
		Position = position==null?"":position.trim();
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country==null?"":country.trim();
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age==null?"":age.trim();
	}
	public String getEmployeeNo() {
		return EmployeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		EmployeeNo = employeeNo;
	}
	public String getEnFirstName() {
		return enFirstName;
	}
	public void setEnFirstName(String enFirstName) {
		this.enFirstName = enFirstName;
	}
	public String getEnMiddleName() {
		return enMiddleName;
	}
	public void setEnMiddleName(String enMiddleName) {
		this.enMiddleName = enMiddleName;
	}
	public String getEnLastName() {
		return enLastName;
	}
	public void setEnLastName(String enLastName) {
		this.enLastName = enLastName;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		//this.employeeStatus = employeeStatus;
		this.employeeStatus = employeeStatus==null?"":employeeStatus.trim();
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
		
	/**
	 * @return the empAllowed
	 */
	public String getEmpAllowed() {
		return empAllowed;
	}
	/**
	 * @param empAllowed the empAllowed to set
	 */
	public void setEmpAllowed(String empAllowed) {
		this.empAllowed = empAllowed==null?"":empAllowed.trim();
	}
	/**
	 * @return the vacancies
	 */
	public String getVacancies() {
		return vacancies;
	}
	/**
	 * @param vacancies the vacancies to set
	 */
	public void setVacancies(String vacancies) {
		this.vacancies = vacancies==null?"":vacancies.trim();
	}
	/**
	 * @return the cvs
	 */
	public String getCvs() {
		return cvs;
	}
	/**
	 * @param cvs the cvs to set
	 */
	public void setCvs(String cvs) {
		this.cvs = cvs==null?"":cvs.trim();
	}
	/**
	 * @return the available
	 */
	public String getAvailable() {
		return available;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(String available) {
		this.available = available==null?"":available.trim();
	}
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName==null?"":companyName.trim();
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName==null?"":groupName.trim();
	}
	/**
	 * @return the supervisorName
	 */
	public String getSupervisorName() {
		return supervisorName;
	}
	/**
	 * @param supervisorName the supervisorName to set
	 */
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName==null?"":supervisorName.trim();
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive==null?"":isActive.trim();
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName==null?"":projectName.trim();
	}
	/**
	 * @return the shiftFromDate
	 */
	public String getShiftFromDate() {
		return shiftFromDate;
	}
	/**
	 * @param shiftFromDate the shiftFromDate to set
	 */
	public void setShiftFromDate(String shiftFromDate) {
		this.shiftFromDate = shiftFromDate==null?"":shiftFromDate.trim();
	}
	/**
	 * @return the shiftToDate
	 */
	public String getShiftToDate() {
		return shiftToDate;
	}
	/**
	 * @param shiftToDate the shiftToDate to set
	 */
	public void setShiftToDate(String shiftToDate) {
		this.shiftToDate = shiftToDate==null?"":shiftToDate.trim();
	}
	/**
	 * @return the shiftName
	 */
	public String getShiftName() {
		return shiftName;
	}
	/**
	 * @param shiftName the shiftName to set
	 */
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName==null?"":shiftName.trim();
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status==null?"":status.trim();
	}
	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}
	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription==null?"":statusDescription.trim();
	}
	/**
	 * @return the supervisorNumber
	 */
	public String getSupervisorNumber() {
		return supervisorNumber;
	}
	/**
	 * @param supervisorNumber the supervisorNumber to set
	 */
	public void setSupervisorNumber(String supervisorNumber) {
		this.supervisorNumber = supervisorNumber==null?"":supervisorNumber.trim();
	}
	/**
	 * @return the tsStatus
	 */
	public String getTsStatus() {
		return tsStatus;
	}
	/**
	 * @param tsStatus the tsStatus to set
	 */
	public void setTsStatus(String tsStatus) {
		this.tsStatus = tsStatus==null?"":tsStatus.trim();
	}
	/**
	 * @return the shiftType
	 */
	public String getShiftType() {
		return shiftType;
	}
	/**
	 * @param shiftType the shiftType to set
	 */
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType==null?"":shiftType.trim();
	}
	public String getLeaveCreateDate() {
		return leaveCreateDate;
	}
	public void setLeaveCreateDate(String leaveCreateDate) {
		this.leaveCreateDate = leaveCreateDate==null?"":leaveCreateDate.trim();
		
	}
	public String getLeaveStartDate() {
		return leaveStartDate;
	}
	public void setLeaveStartDate(String leaveStartDate) {
		this.leaveStartDate = leaveStartDate==null?"":leaveStartDate.trim();
	}
	public String getLeaveEndDate() {
		return leaveEndDate;
	}
	public void setLeaveEndDate(String leaveEndDate) {
		this.leaveEndDate = leaveEndDate==null?"":leaveEndDate.trim();
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType==null?"":leaveType.trim();
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason==null?"":leaveReason.trim();
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName==null?"":empName.trim();
		
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo==null?"":empNo.trim();
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason==null?"":reason.trim();
	}
	public String getSuper_supervisorName() {
		return super_supervisorName;
	}
	public void setSuper_supervisorName(String super_supervisorName) {
		this.super_supervisorName = super_supervisorName==null?"":super_supervisorName.trim();
	}
	
	
	
}
