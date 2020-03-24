package model;

import java.util.Date;

public class DraftSalaryModel 
{

	private int srNO;
	private int compKey;
	private String enCompanyName;
	private String arCompanyName;
	private String createDate; 
	private int totalDepartment;
	private int totalPositions;
	private String lastSalaryCreated;
	private String lastSalaryApproved;
	private String lastSalaryPaid;
	private int totalEmployees;
	
	private int empKey;
	private String empNo;
	private String enFullName;
	private String department;
	private String position;
	private Date employeementDate;
	private int totalDays;
	private int totalPresentDays;
	private int totalLeaveDays;
	private double basicSalary;
	private double totalAllowance;
	private double totalSalary;
	private double otAmount;
	private double otUnits;
	private double otCalculation;
	private double netToPay;
	private double totalWorkingUnits;
	private double totalActualUnits;
	
	private double[] otArrayUnits;
	private double[] otArrayAmount;
	
	private double subTotal;
	private double loans;
	private double additions;
	private double deductions;
	private double actualSalary;
	private double paidAmount;
	private double balance;
	private String remarks;
	
	private double salaryActualHours;
	private double salaryWorkHours;
	
	private int missingDays;
	private String salaryStatus;
	private int recNo;
	
	private String fromDate;
	private String toDate;
	
	private String payMode;
	private int depId;
	private int bankId;
	private int branchId;
	private String accountNO;
	private int posId;
	private int gradeId;
	private int sectionId;
	private int classId;
	private int sifStatus;
	
	
	public int getSrNO() {
		return srNO;
	}
	public void setSrNO(int srNO) {
		this.srNO = srNO;
	}
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
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
	public String getLastSalaryCreated() {
		return lastSalaryCreated;
	}
	public void setLastSalaryCreated(String lastSalaryCreated) {
		this.lastSalaryCreated = lastSalaryCreated;
	}
	public String getLastSalaryApproved() {
		return lastSalaryApproved;
	}
	public void setLastSalaryApproved(String lastSalaryApproved) {
		this.lastSalaryApproved = lastSalaryApproved;
	}
	public String getLastSalaryPaid() {
		return lastSalaryPaid;
	}
	public void setLastSalaryPaid(String lastSalaryPaid) {
		this.lastSalaryPaid = lastSalaryPaid;
	}
	public int getTotalEmployees() {
		return totalEmployees;
	}
	public void setTotalEmployees(int totalEmployees) {
		this.totalEmployees = totalEmployees;
	}
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
	public String getEnFullName() {
		return enFullName;
	}
	public void setEnFullName(String enFullName) {
		this.enFullName = enFullName;
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
	public int getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}
	public int getTotalPresentDays() {
		return totalPresentDays;
	}
	public void setTotalPresentDays(int totalPresentDays) {
		this.totalPresentDays = totalPresentDays;
	}
	public int getTotalLeaveDays() {
		return totalLeaveDays;
	}
	public void setTotalLeaveDays(int totalLeaveDays) {
		this.totalLeaveDays = totalLeaveDays;
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
	public double getOtAmount() {
		return otAmount;
	}
	public void setOtAmount(double otAmount) {
		this.otAmount = otAmount;
	}
	public double getNetToPay() {
		return netToPay;
	}
	public void setNetToPay(double netToPay) {
		this.netToPay = netToPay;
	}
	public double getTotalWorkingUnits() {
		return totalWorkingUnits;
	}
	public void setTotalWorkingUnits(double totalWorkingUnits) {
		this.totalWorkingUnits = totalWorkingUnits;
	}
	public double getOtUnits() {
		return otUnits;
	}
	public void setOtUnits(double otUnits) {
		this.otUnits = otUnits;
	}
	public double getOtCalculation() {
		return otCalculation;
	}
	public void setOtCalculation(double otCalculation) {
		this.otCalculation = otCalculation;
	}
	public double[] getOtArrayUnits() {
		return otArrayUnits;
	}
	public void setOtArrayUnits(double[] otArrayUnits) {
		this.otArrayUnits = otArrayUnits;
	}
	public double[] getOtArrayAmount() {
		return otArrayAmount;
	}
	public void setOtArrayAmount(double[] otArrayAmount) {
		this.otArrayAmount = otArrayAmount;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public double getLoans() {
		return loans;
	}
	public void setLoans(double loans) {
		this.loans = loans;
	}
	public double getAdditions() {
		return additions;
	}
	public void setAdditions(double additions) {
		this.additions = additions;
	}
	public double getDeductions() {
		return deductions;
	}
	public void setDeductions(double deductions) {
		this.deductions = deductions;
	}
	public double getActualSalary() {
		return actualSalary;
	}
	public void setActualSalary(double actualSalary) {
		this.actualSalary = actualSalary;
	}
	public double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public double getSalaryActualHours() {
		return salaryActualHours;
	}
	public void setSalaryActualHours(double salaryActualHours) {
		this.salaryActualHours = salaryActualHours;
	}
	public double getSalaryWorkHours() {
		return salaryWorkHours;
	}
	public void setSalaryWorkHours(double salaryWorkHours) {
		this.salaryWorkHours = salaryWorkHours;
	}
	public int getMissingDays() {
		return missingDays;
	}
	public void setMissingDays(int missingDays) {
		this.missingDays = missingDays;
	}
	public String getSalaryStatus() {
		return salaryStatus;
	}
	public void setSalaryStatus(String salaryStatus) {
		this.salaryStatus = salaryStatus;
	}
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public int getDepId() {
		return depId;
	}
	public void setDepId(int depId) {
		this.depId = depId;
	}
	public int getBankId() {
		return bankId;
	}
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public String getAccountNO() {
		return accountNO;
	}
	public void setAccountNO(String accountNO) {
		this.accountNO = accountNO;
	}
	public int getPosId() {
		return posId;
	}
	public void setPosId(int posId) {
		this.posId = posId;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public int getSifStatus() {
		return sifStatus;
	}
	public void setSifStatus(int sifStatus) {
		this.sifStatus = sifStatus;
	}
	public double getTotalActualUnits() {
		return totalActualUnits;
	}
	public void setTotalActualUnits(double totalActualUnits) {
		this.totalActualUnits = totalActualUnits;
	}
	
	
	
	
}
