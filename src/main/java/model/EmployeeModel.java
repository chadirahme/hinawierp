package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
public class EmployeeModel 
{

	private int reprotsLineNo;
	private int reprotsRecNO;
	private int lineNo;
	private int employeeKey;
	private String employeeNo;
	private String fullName;
	private String arabicName;
	private int departmentID;
	private String department;
	private String arabicDepartment;
	private int positionID;
	private String position;
	private String arabicPosition;
	private int nationalityID;
	private String country;
	private int companyID;
	private String companyName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date employeementDate;
	private String employeementDateString;
	private String Age;
	
	private String enFirstName;
	private String enMiddleName;
	private String enLastName;
	
	private String arFirstName;
	private String arMiddleName;
	private String arLastName;
	
	private String status;
	private String gender;
	private String marital;
	
	private int locationId;
	
	private boolean checkedEmployee;
	private int priorityNo;
	
	//Import
	private int compKey;
	private String enDepartmentName;
	private String enPositionName;
	private String enLocationName;
	private String joinDate;
	private String sex;
	private String nationality;
	private String dob;
	private String enReligion;
	private String maritalStatus;
	private int sexId;
	private int religionId;
	private int maritalID;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date joiningDate;
	
	private String shiftType;
	private String shiftFromDate;
	private String shiftToDate;
	
	//Transfer
	private int srNo;
	private int projectKey;
	private String lastTransferDate;
	private String projectName;
	
	private String employeeType;
	private String standardNo;
	private int bloodType;
	private int statusId;
	private String placeOfBirth;
	private int countryOfBirth;
	private String employeeStatus;
	private String local;
	
	//Assign Employee to Time sheet
	private String salaryStatus;
	private boolean hrSalary;
	private boolean timesheetSalary;
	
	private int supervisorId;
	private String supervisorName;
	private String supervisorNameAR;
	
	private int workGroupId;
	private String workGroupName;
	private String workGroupNameAR;
	
	
	private int shiftkey;
	private String shiftName;
	
	private String email;
	
	private String statusDescription;
	
	private ProjectModel selectedProject;
	
	private boolean employeeAssignShift[];
	
	private int qblistEmpKey;//for mergerd databse;
	
	private int hbaEmpKey;//for mergerd databse;
	
	private int super_supervisorId;
	
	private String super_supervisorName;
	
	private String super_supervisorNameAR;
	
	private List<String> listEmails=new ArrayList<String>();
	
	private String taskNumber="";
		
	private String passportExpiry="";
	private String expPasprtMsg="";
	private String residanceExpiry="";
	private String expResidanceMSg="";
	private String labourCradExpiry="";
	private String expLbrCrdMsg="";
	
	private double basicSalary;
	private double totalAllowance;
	private double totalSalary;
	
	
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
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
	public String getArFirstName() {
		return arFirstName;
	}
	public void setArFirstName(String arFirstName) {
		this.arFirstName = arFirstName;
	}
	public String getArMiddleName() {
		return arMiddleName;
	}
	public void setArMiddleName(String arMiddleName) {
		this.arMiddleName = arMiddleName;
	}
	public String getArLastName() {
		return arLastName;
	}
	public void setArLastName(String arLastName) {
		this.arLastName = arLastName;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public int getPositionID() {
		return positionID;
	}
	public void setPositionID(int positionID) {
		this.positionID = positionID;
	}
	public int getNationalityID() {
		return nationalityID;
	}
	public void setNationalityID(int nationalityID) {
		this.nationalityID = nationalityID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the employeeKey
	 */
	public int getEmployeeKey() {
		return employeeKey;
	}
	/**
	 * @param employeeKey the employeeKey to set
	 */
	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}
	/**
	 * @return the employeeNo
	 */
	public String getEmployeeNo() {
		return employeeNo;
	}
	/**
	 * @param employeeNo the employeeNo to set
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the arabicName
	 */
	public String getArabicName() {
		return arabicName;
	}
	/**
	 * @param arabicName the arabicName to set
	 */
	public void setArabicName(String arabicName) {
		this.arabicName = arabicName;
	}
	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * @return the arabicDepartment
	 */
	public String getArabicDepartment() {
		return arabicDepartment;
	}
	/**
	 * @param arabicDepartment the arabicDepartment to set
	 */
	public void setArabicDepartment(String arabicDepartment) {
		this.arabicDepartment = arabicDepartment;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the arabicPosition
	 */
	public String getArabicPosition() {
		return arabicPosition;
	}
	/**
	 * @param arabicPosition the arabicPosition to set
	 */
	public void setArabicPosition(String arabicPosition) {
		this.arabicPosition = arabicPosition;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
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
		this.companyName = companyName;
	}
	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	/**
	 * @return the employeementDate
	 */
	public Date getEmployeementDate() {
		return employeementDate;
	}
	/**
	 * @param employeementDate the employeementDate to set
	 */
	public void setEmployeementDate(Date employeementDate) {
		this.employeementDate = employeementDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMarital() {
		return marital;
	}
	public void setMarital(String marital) {
		this.marital = marital;
	}
	public int getCompanyID() {
		return companyID;
	}
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public boolean isCheckedEmployee() {
		return checkedEmployee;
	}
	public void setCheckedEmployee(boolean checkedEmployee) {
		this.checkedEmployee = checkedEmployee;
	}
	public int getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(int projectKey) {
		this.projectKey = projectKey;
	}
	public String getLastTransferDate() {
		return lastTransferDate;
	}
	public void setLastTransferDate(String lastTransferDate) {
		this.lastTransferDate = lastTransferDate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public int getCompKey() {
		return compKey;
	}
	public void setCompKey(int compKey) {
		this.compKey = compKey;
	}
	public String getEnDepartmentName() {
		return enDepartmentName;
	}
	public void setEnDepartmentName(String enDepartmentName) {
		this.enDepartmentName = enDepartmentName;
	}
	public String getEnPositionName() {
		return enPositionName;
	}
	public void setEnPositionName(String enPositionName) {
		this.enPositionName = enPositionName;
	}
	public String getEnLocationName() {
		return enLocationName;
	}
	public void setEnLocationName(String enLocationName) {
		this.enLocationName = enLocationName;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getEnReligion() {
		return enReligion;
	}
	public void setEnReligion(String enReligion) {
		this.enReligion = enReligion;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public int getSexId() {
		return sexId;
	}
	public void setSexId(int sexId) {
		this.sexId = sexId;
	}
	public int getReligionId() {
		return religionId;
	}
	public void setReligionId(int religionId) {
		this.religionId = religionId;
	}
	public int getMaritalID() {
		return maritalID;
	}
	public void setMaritalID(int maritalID) {
		this.maritalID = maritalID;
	}
	public Date getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public String getStandardNo() {
		return standardNo;
	}
	public void setStandardNo(String standardNo) {
		this.standardNo = standardNo;
	}
	public int getBloodType() {
		return bloodType;
	}
	public void setBloodType(int bloodType) {
		this.bloodType = bloodType;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getPlaceOfBirth() {
		return placeOfBirth;
	}
	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}
	public int getCountryOfBirth() {
		return countryOfBirth;
	}
	public void setCountryOfBirth(int countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getSalaryStatus() {
		return salaryStatus;
	}
	public void setSalaryStatus(String salaryStatus) {
		this.salaryStatus = salaryStatus;
	}
	public boolean isHrSalary() {
		return hrSalary;
	}
	public void setHrSalary(boolean hrSalary) {
		this.hrSalary = hrSalary;
	}
	public boolean isTimesheetSalary() {
		return timesheetSalary;
	}
	public void setTimesheetSalary(boolean timesheetSalary) {
		this.timesheetSalary = timesheetSalary;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	/**
	 * @return the supervisorId
	 */
	public int getSupervisorId() {
		return supervisorId;
	}
	/**
	 * @param supervisorId the supervisorId to set
	 */
	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
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
		this.supervisorName = supervisorName;
	}
	/**
	 * @return the supervisorNameAR
	 */
	public String getSupervisorNameAR() {
		return supervisorNameAR;
	}
	/**
	 * @param supervisorNameAR the supervisorNameAR to set
	 */
	public void setSupervisorNameAR(String supervisorNameAR) {
		this.supervisorNameAR = supervisorNameAR;
	}
	/**
	 * @return the workGroupId
	 */
	public int getWorkGroupId() {
		return workGroupId;
	}
	/**
	 * @param workGroupId the workGroupId to set
	 */
	public void setWorkGroupId(int workGroupId) {
		this.workGroupId = workGroupId;
	}
	/**
	 * @return the workGroupName
	 */
	public String getWorkGroupName() {
		return workGroupName;
	}
	/**
	 * @param workGroupName the workGroupName to set
	 */
	public void setWorkGroupName(String workGroupName) {
		this.workGroupName = workGroupName;
	}
	/**
	 * @return the workGroupNameAR
	 */
	public String getWorkGroupNameAR() {
		return workGroupNameAR;
	}
	/**
	 * @param workGroupNameAR the workGroupNameAR to set
	 */
	public void setWorkGroupNameAR(String workGroupNameAR) {
		this.workGroupNameAR = workGroupNameAR;
	}
	/**
	 * @return the shiftFromDate
	 */
	
	/**
	 * @return the shiftkey
	 */
	public int getShiftkey() {
		return shiftkey;
	}
	/**
	 * @param shiftkey the shiftkey to set
	 */
	public void setShiftkey(int shiftkey) {
		this.shiftkey = shiftkey;
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
		this.shiftName = shiftName;
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
		this.statusDescription = statusDescription;
	}
	
	
	/**
	 * @return the selectedProject
	 */
	public ProjectModel getSelectedProject() {
		return selectedProject;
	}
	/**
	 * @param selectedProject the selectedProject to set
	 */
	public void setSelectedProject(ProjectModel selectedProject) {
		this.selectedProject = selectedProject;
	}
	/**
	 * @return the reprotsLineNo
	 */
	public int getReprotsLineNo() {
		return reprotsLineNo;
	}
	/**
	 * @param reprotsLineNo the reprotsLineNo to set
	 */
	public void setReprotsLineNo(int reprotsLineNo) {
		this.reprotsLineNo = reprotsLineNo;
	}
	/**
	 * @return the reprotsRecNO
	 */
	public int getReprotsRecNO() {
		return reprotsRecNO;
	}
	/**
	 * @param reprotsRecNO the reprotsRecNO to set
	 */
	public void setReprotsRecNO(int reprotsRecNO) {
		this.reprotsRecNO = reprotsRecNO;
	}
	/**
	 * @return the shiftType
	 */
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
		this.shiftType = shiftType;
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
		this.shiftFromDate = shiftFromDate;
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
		this.shiftToDate = shiftToDate;
	}
	public boolean[] getEmployeeAssignShift() {
		return employeeAssignShift;
	}
	public void setEmployeeAssignShift(boolean employeeAssignShift[]) {
		this.employeeAssignShift = employeeAssignShift;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getQblistEmpKey() {
		return qblistEmpKey;
	}
	public void setQblistEmpKey(int qblistEmpKey) {
		this.qblistEmpKey = qblistEmpKey;
	}
	public int getHbaEmpKey() {
		return hbaEmpKey;
	}
	public void setHbaEmpKey(int hbaEmpKey) {
		this.hbaEmpKey = hbaEmpKey;
	}
	public String getEmployeementDateString() {
		return employeementDateString;
	}
	public void setEmployeementDateString(String employeementDateString) {
		this.employeementDateString = employeementDateString;
	}
	public int getPriorityNo() {
		return priorityNo;
	}
	public void setPriorityNo(int priorityNo) {
		this.priorityNo = priorityNo;
	}
	public int getSuper_supervisorId() {
		return super_supervisorId;
	}
	public void setSuper_supervisorId(int super_supervisorId) {
		this.super_supervisorId = super_supervisorId;
	}
	public String getSuper_supervisorName() {
		return super_supervisorName;
	}
	public void setSuper_supervisorName(String super_supervisorName) {
		this.super_supervisorName = super_supervisorName;
	}
	public String getSuper_supervisorNameAR() {
		return super_supervisorNameAR;
	}
	public void setSuper_supervisorNameAR(String super_supervisorNameAR) {
		this.super_supervisorNameAR = super_supervisorNameAR;
	}
	/**
	 * @return the listEmails
	 */
	public List<String> getListEmails() {
		return listEmails;
	}
	/**
	 * @param listEmails the listEmails to set
	 */
	public void setListEmails(List<String> listEmails) {
		this.listEmails = listEmails;
	}
	/**
	 * @return the taskNumber
	 */
	public String getTaskNumber() {
		return taskNumber;
	}
	/**
	 * @param taskNumber the taskNumber to set
	 */
	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}
	public String getPassportExpiry() {
		return passportExpiry;
	}
	public void setPassportExpiry(String passportExpiry) {
		this.passportExpiry = passportExpiry;
	}
	public String getExpPasprtMsg() {
		return expPasprtMsg;
	}
	public void setExpPasprtMsg(String expPasprtMsg) {
		this.expPasprtMsg = expPasprtMsg;
	}
	public String getResidanceExpiry() {
		return residanceExpiry;
	}
	public void setResidanceExpiry(String residanceExpiry) {
		this.residanceExpiry = residanceExpiry;
	}
	public String getExpResidanceMSg() {
		return expResidanceMSg;
	}
	public void setExpResidanceMSg(String expResidanceMSg) {
		this.expResidanceMSg = expResidanceMSg;
	}
	public String getLabourCradExpiry() {
		return labourCradExpiry;
	}
	public void setLabourCradExpiry(String labourCradExpiry) {
		this.labourCradExpiry = labourCradExpiry;
	}
	public String getExpLbrCrdMsg() {
		return expLbrCrdMsg;
	}
	public void setExpLbrCrdMsg(String expLbrCrdMsg) {
		this.expLbrCrdMsg = expLbrCrdMsg;
	}
	public double getBasicSalary() {
		return basicSalary;
	}
	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}
	public double getTotalAllowance() {
		return totalAllowance;
	}
	public void setTotalAllowance(double totalAllowance) {
		this.totalAllowance = totalAllowance;
	}
	public double getTotalSalary() {
		return totalSalary;
	}
	public void setTotalSalary(double totalSalary) {
		this.totalSalary = totalSalary;
	}
	
	
	
	
}
