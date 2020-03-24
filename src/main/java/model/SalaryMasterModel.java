package model;

import java.util.List;

import reports.AdditionModel;
import reports.AllowanceModel;
import reports.DiductionModel;
import reports.OverTimeModel;

public class SalaryMasterModel {

	private int empKey;
	private String salaryStatus;
	
	private String branchName;
	
	private String bankName;

	private int workingDays;
	private int totalworkHours;

	private double basicSalary;

	private double totalAllowance;

	private double totalOT;

	private double totalAddition;

	private double totalDeduction;

	private int taxAmountMax;

	private double netToPay;

	private String arabicArabic;

	private String englishName;

	private int compId;

	private String department;

	private String departmentArabic;

	private String postion;

	private String positionArabic;

	private String empNo;

	private int recNo;

	private int month;

	private int year;

	private double totalLoan;

	private int bankId;

	private int branchId;

	private String accountNumber;

	private int currancyId;

	private double exchnageRate;

	private double paidAmount;

	private List<AllowanceModel> allowanceModels;
	private List<OverTimeModel> overTimeModels;
	private List<AdditionModel> additionModels;
	private List<DiductionModel> diductionModels;
	
	private double balance;
	
	private double balanceInDhr;
	
	private double netpayInDhrms;
	
	private String currancy;
	
private TimeSheetDataModel timeSheetDataModel;
	

	/**
	 * @return the workingDays
	 */
	public int getWorkingDays() {
		return workingDays;
	}

	/**
	 * @param workingDays
	 *            the workingDays to set
	 */
	public void setWorkingDays(int workingDays) {
		this.workingDays = workingDays;
	}

	/**
	 * @return the totalworkHours
	 */
	public int getTotalworkHours() {
		return totalworkHours;
	}

	/**
	 * @param totalworkHours
	 *            the totalworkHours to set
	 */
	public void setTotalworkHours(int totalworkHours) {
		this.totalworkHours = totalworkHours;
	}

	/**
	 * @return the basicSalary
	 */
	public double getBasicSalary() {
		return basicSalary;
	}

	/**
	 * @param basicSalary
	 *            the basicSalary to set
	 */
	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	/**
	 * @return the totalAllowance
	 */
	public double getTotalAllowance() {
		return totalAllowance;
	}

	/**
	 * @param totalAllowance
	 *            the totalAllowance to set
	 */
	public void setTotalAllowance(double totalAllowance) {
		this.totalAllowance = totalAllowance;
	}

	/**
	 * @return the totalOT
	 */
	public double getTotalOT() {
		return totalOT;
	}

	/**
	 * @param totalOT
	 *            the totalOT to set
	 */
	public void setTotalOT(double totalOT) {
		this.totalOT = totalOT;
	}

	/**
	 * @return the totalAddition
	 */
	public double getTotalAddition() {
		return totalAddition;
	}

	/**
	 * @param totalAddition
	 *            the totalAddition to set
	 */
	public void setTotalAddition(double totalAddition) {
		this.totalAddition = totalAddition;
	}

	/**
	 * @return the totalDeduction
	 */
	public double getTotalDeduction() {
		return totalDeduction;
	}

	/**
	 * @param totalDeduction
	 *            the totalDeduction to set
	 */
	public void setTotalDeduction(double totalDeduction) {
		this.totalDeduction = totalDeduction;
	}

	/**
	 * @return the taxAmountMax
	 */
	public int getTaxAmountMax() {
		return taxAmountMax;
	}

	/**
	 * @param taxAmountMax
	 *            the taxAmountMax to set
	 */
	public void setTaxAmountMax(int taxAmountMax) {
		this.taxAmountMax = taxAmountMax;
	}

	/**
	 * @return the netToPay
	 */
	public double getNetToPay() {
		return netToPay;
	}

	/**
	 * @param netToPay
	 *            the netToPay to set
	 */
	public void setNetToPay(double netToPay) {
		this.netToPay = netToPay;
	}

	/**
	 * @return the arabicArabic
	 */
	public String getArabicArabic() {
		return arabicArabic;
	}

	/**
	 * @param arabicArabic
	 *            the arabicArabic to set
	 */
	public void setArabicArabic(String arabicArabic) {
		this.arabicArabic = arabicArabic;
	}

	/**
	 * @return the englishName
	 */
	public String getEnglishName() {
		return englishName;
	}

	/**
	 * @param englishName
	 *            the englishName to set
	 */
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	/**
	 * @return the compId
	 */
	public int getCompId() {
		return compId;
	}

	/**
	 * @param compId
	 *            the compId to set
	 */
	public void setCompId(int compId) {
		this.compId = compId;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the departmentArabic
	 */
	public String getDepartmentArabic() {
		return departmentArabic;
	}

	/**
	 * @param departmentArabic
	 *            the departmentArabic to set
	 */
	public void setDepartmentArabic(String departmentArabic) {
		this.departmentArabic = departmentArabic;
	}

	/**
	 * @return the postion
	 */
	public String getPostion() {
		return postion;
	}

	/**
	 * @param postion
	 *            the postion to set
	 */
	public void setPostion(String postion) {
		this.postion = postion;
	}

	/**
	 * @return the positionArabic
	 */
	public String getPositionArabic() {
		return positionArabic;
	}

	/**
	 * @param positionArabic
	 *            the positionArabic to set
	 */
	public void setPositionArabic(String positionArabic) {
		this.positionArabic = positionArabic;
	}

	/**
	 * @return the empNo
	 */
	public String getEmpNo() {
		return empNo;
	}

	/**
	 * @param empNo
	 *            the empNo to set
	 */
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	/**
	 * @return the recNo
	 */
	public int getRecNo() {
		return recNo;
	}

	/**
	 * @param recNo
	 *            the recNo to set
	 */
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the totalLoan
	 */
	public double getTotalLoan() {
		return totalLoan;
	}

	/**
	 * @param totalLoan
	 *            the totalLoan to set
	 */
	public void setTotalLoan(double totalLoan) {
		this.totalLoan = totalLoan;
	}

	/**
	 * @return the bankId
	 */
	public int getBankId() {
		return bankId;
	}

	/**
	 * @param bankId
	 *            the bankId to set
	 */
	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	/**
	 * @return the branchId
	 */
	public int getBranchId() {
		return branchId;
	}

	/**
	 * @param branchId
	 *            the branchId to set
	 */
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the currancyId
	 */
	public int getCurrancyId() {
		return currancyId;
	}

	/**
	 * @param currancyId
	 *            the currancyId to set
	 */
	public void setCurrancyId(int currancyId) {
		this.currancyId = currancyId;
	}

	/**
	 * @return the exchnageRate
	 */
	public double getExchnageRate() {
		return exchnageRate;
	}

	/**
	 * @param exchnageRate
	 *            the exchnageRate to set
	 */
	public void setExchnageRate(double exchnageRate) {
		this.exchnageRate = exchnageRate;
	}

	/**
	 * @return the paidAmount
	 */
	public double getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @param paidAmount
	 *            the paidAmount to set
	 */
	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public int getEmpKey() {
		return empKey;
	}

	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}

	public String getSalaryStatus() {
		return salaryStatus;
	}

	public void setSalaryStatus(String salaryStatus) {
		this.salaryStatus = salaryStatus;
	}

	/**
	 * @return the allowanceModels
	 */
	public List<AllowanceModel> getAllowanceModels() {
		return allowanceModels;
	}

	/**
	 * @param allowanceModels
	 *            the allowanceModels to set
	 */
	public void setAllowanceModels(List<AllowanceModel> allowanceModels) {
		this.allowanceModels = allowanceModels;
	}

	/**
	 * @return the overTimeModels
	 */
	public List<OverTimeModel> getOverTimeModels() {
		return overTimeModels;
	}

	/**
	 * @param overTimeModels
	 *            the overTimeModels to set
	 */
	public void setOverTimeModels(List<OverTimeModel> overTimeModels) {
		this.overTimeModels = overTimeModels;
	}

	/**
	 * @return the additionModels
	 */
	public List<AdditionModel> getAdditionModels() {
		return additionModels;
	}

	/**
	 * @param additionModels
	 *            the additionModels to set
	 */
	public void setAdditionModels(List<AdditionModel> additionModels) {
		this.additionModels = additionModels;
	}

	/**
	 * @return the diductionModels
	 */
	public List<DiductionModel> getDiductionModels() {
		return diductionModels;
	}

	/**
	 * @param diductionModels
	 *            the diductionModels to set
	 */
	public void setDiductionModels(List<DiductionModel> diductionModels) {
		this.diductionModels = diductionModels;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the balanceInDhr
	 */
	public double getBalanceInDhr() {
		return balanceInDhr;
	}

	/**
	 * @param balanceInDhr the balanceInDhr to set
	 */
	public void setBalanceInDhr(double balanceInDhr) {
		this.balanceInDhr = balanceInDhr;
	}

	/**
	 * @return the netpayInDhrms
	 */
	public double getNetpayInDhrms() {
		return netpayInDhrms;
	}

	/**
	 * @param netpayInDhrms the netpayInDhrms to set
	 */
	public void setNetpayInDhrms(double netpayInDhrms) {
		this.netpayInDhrms = netpayInDhrms;
	}

	/**
	 * @return the currancy
	 */
	public String getCurrancy() {
		return currancy;
	}

	/**
	 * @param currancy the currancy to set
	 */
	public void setCurrancy(String currancy) {
		this.currancy = currancy;
	}

	/**
	 * @return the timeSheetDataModel
	 */
	public TimeSheetDataModel getTimeSheetDataModel() {
		return timeSheetDataModel;
	}

	/**
	 * @param timeSheetDataModel the timeSheetDataModel to set
	 */
	public void setTimeSheetDataModel(TimeSheetDataModel timeSheetDataModel) {
		this.timeSheetDataModel = timeSheetDataModel;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	

}
