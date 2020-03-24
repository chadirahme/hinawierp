package model;

import java.util.Date;

public class AllowanceModel 
{

	private Date effectiveDate;
	private int recNo;
	private int lineNo;
	private int allowanceId;
	private int companyKey;
	private int salaryId;
	private String salaryItem;
	private int allowanceType;
	
	private boolean headItem;
	private boolean editSalary;
	private boolean editPension;
	private boolean rowDisable;
	
	private int pensionEmployee;
	private int pensionCompany;
	private int minimum;
	private int maximum;
	private String setupDone;
	private boolean hasSetup;
	
	private int leaveId;
	private String leaveType;
	private int noOfDays;
	
	private int columnId;
	private String allowMultiple;
	
	private String activity;
	private int condNo;
	private String nationality;
	private String condValue;
	private String condition;
	private int payPeriod;
	private String payMode;
	
	private double minWorkPeriod;
	private String minWorkFlag;
	private String transfer2NextYear;
	private String salaryRow;
	private String condRow;
	private int encashItem;
	private String encashRow;
	private int depId;
	private int posId;
	private int secID; 
	private int classId;
	private String empType;
	private int sexId;
	private int religionId;
	private int maritalId;
	private double salaryPER ;
	
	private double monthFrom;
	private double monthTo;
	private double daysAllowed;
	private double duration;
	private String mode;
	private String calculate;
	
	private String mhFlag;
	private double deductionRate;
	private double deductionNo;
	private String deductionService;
	private String deductionFrom;
	private int deductionId;
	
	private String eosReason;
	private String salaryEosRow;
	private double periodFrom;
	private double periodTo;
	private double salaryDays;
	private String eosIsDependable;
	
	private boolean changeFromAdvanced;
	private String changeFromAdvancedSetup;
	
	private int fixed;
	 private int kidsNo;
	 private int kidsAgeFrom;
	 private int kidsAgeTo;
	 private int salaryNos;
	 private int basicPer;
	 private int distance;
	 private String houseType;
	 private int roomsNo;
	 
	 private String additions;
	 private String deductions;
	 private String contractType;
	 private double maximumValue;
	 private String maximumType;
	 private String salaryEOS;
	 private double eosBases;
	 private double dblRate;
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getAllowanceId() {
		return allowanceId;
	}
	public void setAllowanceId(int allowanceId) {
		this.allowanceId = allowanceId;
	}
	public int getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(int companyKey) {
		this.companyKey = companyKey;
	}
	public int getSalaryId() {
		return salaryId;
	}
	public void setSalaryId(int salaryId) {
		this.salaryId = salaryId;
	}
	public String getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(String salaryItem) {
		this.salaryItem = salaryItem;
	}
	public int getAllowanceType() {
		return allowanceType;
	}
	public void setAllowanceType(int allowanceType) {
		this.allowanceType = allowanceType;
	}
	public boolean isHeadItem() {
		return headItem;
	}
	public void setHeadItem(boolean headItem) {
		this.headItem = headItem;
	}
	public boolean isEditSalary() {
		return editSalary;
	}
	public void setEditSalary(boolean editSalary) {
		this.editSalary = editSalary;
	}
	public boolean isEditPension() {
		return editPension;
	}
	public void setEditPension(boolean editPension) {
		this.editPension = editPension;
	}
	public int getPensionEmployee() {
		return pensionEmployee;
	}
	public void setPensionEmployee(int pensionEmployee) {
		this.pensionEmployee = pensionEmployee;
	}
	public int getPensionCompany() {
		return pensionCompany;
	}
	public void setPensionCompany(int pensionCompany) {
		this.pensionCompany = pensionCompany;
	}
	public int getMinimum() {
		return minimum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	public String getSetupDone() {
		return setupDone;
	}
	public void setSetupDone(String setupDone) {
		this.setupDone = setupDone;
	}
	public boolean isHasSetup() {
		return hasSetup;
	}
	public void setHasSetup(boolean hasSetup) {
		this.hasSetup = hasSetup;
	}
	public int getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	public int getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}
	public int getColumnId() {
		return columnId;
	}
	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}
	public String getAllowMultiple() {
		return allowMultiple;
	}
	public void setAllowMultiple(String allowMultiple) {
		this.allowMultiple = allowMultiple;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public int getCondNo() {
		return condNo;
	}
	public void setCondNo(int condNo) {
		this.condNo = condNo;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getCondValue() {
		return condValue;
	}
	public void setCondValue(String condValue) {
		this.condValue = condValue;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(int payPeriod) {
		this.payPeriod = payPeriod;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public double getMinWorkPeriod() {
		return minWorkPeriod;
	}
	public void setMinWorkPeriod(double minWorkPeriod) {
		this.minWorkPeriod = minWorkPeriod;
	}
	public String getMinWorkFlag() {
		return minWorkFlag;
	}
	public void setMinWorkFlag(String minWorkFlag) {
		this.minWorkFlag = minWorkFlag;
	}
	public String getTransfer2NextYear() {
		return transfer2NextYear;
	}
	public void setTransfer2NextYear(String transfer2NextYear) {
		this.transfer2NextYear = transfer2NextYear;
	}
	public String getSalaryRow() {
		return salaryRow;
	}
	public void setSalaryRow(String salaryRow) {
		this.salaryRow = salaryRow;
	}
	public String getCondRow() {
		return condRow;
	}
	public void setCondRow(String condRow) {
		this.condRow = condRow;
	}
	public int getEncashItem() {
		return encashItem;
	}
	public void setEncashItem(int encashItem) {
		this.encashItem = encashItem;
	}
	public String getEncashRow() {
		return encashRow;
	}
	public void setEncashRow(String encashRow) {
		this.encashRow = encashRow;
	}
	public int getDepId() {
		return depId;
	}
	public void setDepId(int depId) {
		this.depId = depId;
	}
	public int getPosId() {
		return posId;
	}
	public void setPosId(int posId) {
		this.posId = posId;
	}
	public double getMonthFrom() {
		return monthFrom;
	}
	public void setMonthFrom(double monthFrom) {
		this.monthFrom = monthFrom;
	}
	public double getMonthTo() {
		return monthTo;
	}
	public void setMonthTo(double monthTo) {
		this.monthTo = monthTo;
	}
	public double getDaysAllowed() {
		return daysAllowed;
	}
	public void setDaysAllowed(double daysAllowed) {
		this.daysAllowed = daysAllowed;
	}
	public double getDuration() {
		return duration;
	}
	public void setDuration(double duration) {
		this.duration = duration;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCalculate() {
		return calculate;
	}
	public void setCalculate(String calculate) {
		this.calculate = calculate;
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
	public int getMaritalId() {
		return maritalId;
	}
	public void setMaritalId(int maritalId) {
		this.maritalId = maritalId;
	}
	public String getMhFlag() {
		return mhFlag;
	}
	public void setMhFlag(String mhFlag) {
		this.mhFlag = mhFlag;
	}
	public double getDeductionRate() {
		return deductionRate;
	}
	public void setDeductionRate(double deductionRate) {
		this.deductionRate = deductionRate;
	}
	public double getDeductionNo() {
		return deductionNo;
	}
	public void setDeductionNo(double deductionNo) {
		this.deductionNo = deductionNo;
	}
	public String getDeductionService() {
		return deductionService;
	}
	public void setDeductionService(String deductionService) {
		this.deductionService = deductionService;
	}
	public String getDeductionFrom() {
		return deductionFrom;
	}
	public void setDeductionFrom(String deductionFrom) {
		this.deductionFrom = deductionFrom;
	}
	public int getDeductionId() {
		return deductionId;
	}
	public void setDeductionId(int deductionId) {
		this.deductionId = deductionId;
	}
	public boolean isRowDisable() {
		return rowDisable;
	}
	public void setRowDisable(boolean rowDisable) {
		this.rowDisable = rowDisable;
	}
	public String getEosReason() {
		return eosReason;
	}
	public void setEosReason(String eosReason) {
		this.eosReason = eosReason;
	}
	public String getSalaryEosRow() {
		return salaryEosRow;
	}
	public void setSalaryEosRow(String salaryEosRow) {
		this.salaryEosRow = salaryEosRow;
	}
	public double getPeriodFrom() {
		return periodFrom;
	}
	public void setPeriodFrom(double periodFrom) {
		this.periodFrom = periodFrom;
	}
	public double getPeriodTo() {
		return periodTo;
	}
	public void setPeriodTo(double periodTo) {
		this.periodTo = periodTo;
	}
	public double getSalaryDays() {
		return salaryDays;
	}
	public void setSalaryDays(double salaryDays) {
		this.salaryDays = salaryDays;
	}
	public String getEosIsDependable() {
		return eosIsDependable;
	}
	public void setEosIsDependable(String eosIsDependable) {
		this.eosIsDependable = eosIsDependable;
	}
	public boolean isChangeFromAdvanced() {
		return changeFromAdvanced;
	}
	public void setChangeFromAdvanced(boolean changeFromAdvanced) {
		this.changeFromAdvanced = changeFromAdvanced;
	}
	public String getChangeFromAdvancedSetup() {
		return changeFromAdvancedSetup;
	}
	public void setChangeFromAdvancedSetup(String changeFromAdvancedSetup) {
		this.changeFromAdvancedSetup = changeFromAdvancedSetup;
	}
	public int getSecID() {
		return secID;
	}
	public void setSecID(int secID) {
		this.secID = secID;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public double getSalaryPER() {
		return salaryPER;
	}
	public void setSalaryPER(double salaryPER) {
		this.salaryPER = salaryPER;
	}
	public int getFixed() {
		return fixed;
	}
	public void setFixed(int fixed) {
		this.fixed = fixed;
	}
	public int getKidsNo() {
		return kidsNo;
	}
	public void setKidsNo(int kidsNo) {
		this.kidsNo = kidsNo;
	}
	public int getKidsAgeFrom() {
		return kidsAgeFrom;
	}
	public void setKidsAgeFrom(int kidsAgeFrom) {
		this.kidsAgeFrom = kidsAgeFrom;
	}
	public int getKidsAgeTo() {
		return kidsAgeTo;
	}
	public void setKidsAgeTo(int kidsAgeTo) {
		this.kidsAgeTo = kidsAgeTo;
	}
	public int getSalaryNos() {
		return salaryNos;
	}
	public void setSalaryNos(int salaryNos) {
		this.salaryNos = salaryNos;
	}
	public int getBasicPer() {
		return basicPer;
	}
	public void setBasicPer(int basicPer) {
		this.basicPer = basicPer;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public int getRoomsNo() {
		return roomsNo;
	}
	public void setRoomsNo(int roomsNo) {
		this.roomsNo = roomsNo;
	}
	public String getAdditions() {
		return additions;
	}
	public void setAdditions(String additions) {
		this.additions = additions;
	}
	public String getDeductions() {
		return deductions;
	}
	public void setDeductions(String deductions) {
		this.deductions = deductions;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public double getMaximumValue() {
		return maximumValue;
	}
	public void setMaximumValue(double maximumValue) {
		this.maximumValue = maximumValue;
	}
	public String getMaximumType() {
		return maximumType;
	}
	public void setMaximumType(String maximumType) {
		this.maximumType = maximumType;
	}
	public String getSalaryEOS() {
		return salaryEOS;
	}
	public void setSalaryEOS(String salaryEOS) {
		this.salaryEOS = salaryEOS;
	}
	public double getEosBases() {
		return eosBases;
	}
	public void setEosBases(double eosBases) {
		this.eosBases = eosBases;
	}
	public double getDblRate() {
		return dblRate;
	}
	public void setDblRate(double dblRate) {
		this.dblRate = dblRate;
	}
	
	
	
	
}
