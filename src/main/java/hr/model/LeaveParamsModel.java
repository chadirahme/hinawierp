package hr.model;

public class LeaveParamsModel 
{
	 private String  levelID;
	 private String depID;
	 private String posID;
	 private String secID; 
	 private String classID;
	 private String empType;
	 private String sexID; 
	 private String maritalID;
	 private String nationality;
	 private String religion;
	 private double minWorkPeriod;
	 private String minWorkMode;
	 private String transfer2NextYear ;
	 private double monthFrom ;                  //'Month From
	 private double monthTo ;                    //'Month To
	 private double daysAllowed ;                //'Allowed Days
	 private double duration ;                   //'In the Period
	 private String mode ;                       //'M-Month    | Y-Year
	 private String calculate ;                  //'Y-Yes      | N-No
	 private double salaryPER ;                  //'% of Salary
	 private int salaryItem ;                   //'Salary
	 private int encashItem ;
	 
	 private String kidsNo;
	 private String kidsAgeFrom;
	 private String kidsAgeTo;
	 private String allowanceType;
	 private String houseType;
	 private String roomsNo;
	 private String salaryNos;
	 private String fixed;
	 private String minimum;
	 private String maximum;
	 private String basicPer;
	 private String payPeriod;
	 private String payMode;
	 private String distance;
	 
	 
	private String execuse;
	private double maxAllowed;
	private String absenceType;
	private String calculateIn;
	private double dblRate;
	private double nos;
	private String deductService;
	private String deductFrom;
	private int deductionItem;
	
	
	private String additions;
	private String deductions;
	private String eosReason;
	private String contractType;
	private double maximumValue;
	private String maximumType;
	private String salaryEOS;
	private double periodFrom;
	private double periodTo;
	private double salaryDays;
	private double eosBases;
	private String eosIsDependable;
	
	
	public String getLevelID() {
		return levelID;
	}
	public void setLevelID(String levelID) {
		this.levelID = levelID;
	}
	public String getDepID() {
		return depID;
	}
	public void setDepID(String depID) {
		this.depID = depID;
	}
	public String getPosID() {
		return posID;
	}
	public void setPosID(String posID) {
		this.posID = posID;
	}
	public String getSecID() {
		return secID;
	}
	public void setSecID(String secID) {
		this.secID = secID;
	}
	public String getClassID() {
		return classID;
	}
	public void setClassID(String classID) {
		this.classID = classID;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getSexID() {
		return sexID;
	}
	public void setSexID(String sexID) {
		this.sexID = sexID;
	}
	public String getMaritalID() {
		return maritalID;
	}
	public void setMaritalID(String maritalID) {
		this.maritalID = maritalID;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}
	public double getMinWorkPeriod() {
		return minWorkPeriod;
	}
	public void setMinWorkPeriod(double minWorkPeriod) {
		this.minWorkPeriod = minWorkPeriod;
	}
	public String getMinWorkMode() {
		return minWorkMode;
	}
	public void setMinWorkMode(String minWorkMode) {
		this.minWorkMode = minWorkMode;
	}
	public String getTransfer2NextYear() {
		return transfer2NextYear;
	}
	public void setTransfer2NextYear(String transfer2NextYear) {
		this.transfer2NextYear = transfer2NextYear;
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
	public double getSalaryPER() {
		return salaryPER;
	}
	public void setSalaryPER(double salaryPER) {
		this.salaryPER = salaryPER;
	}
	public int getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(int salaryItem) {
		this.salaryItem = salaryItem;
	}
	public int getEncashItem() {
		return encashItem;
	}
	public void setEncashItem(int encashItem) {
		this.encashItem = encashItem;
	}
	public String getKidsNo() {
		return kidsNo;
	}
	public void setKidsNo(String kidsNo) {
		this.kidsNo = kidsNo;
	}
	public String getKidsAgeFrom() {
		return kidsAgeFrom;
	}
	public void setKidsAgeFrom(String kidsAgeFrom) {
		this.kidsAgeFrom = kidsAgeFrom;
	}
	public String getKidsAgeTo() {
		return kidsAgeTo;
	}
	public void setKidsAgeTo(String kidsAgeTo) {
		this.kidsAgeTo = kidsAgeTo;
	}
	public String getAllowanceType() {
		return allowanceType;
	}
	public void setAllowanceType(String allowanceType) {
		this.allowanceType = allowanceType;
	}
	public String getHouseType() {
		return houseType;
	}
	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}
	public String getRoomsNo() {
		return roomsNo;
	}
	public void setRoomsNo(String roomsNo) {
		this.roomsNo = roomsNo;
	}
	public String getSalaryNos() {
		return salaryNos;
	}
	public void setSalaryNos(String salaryNos) {
		this.salaryNos = salaryNos;
	}
	public String getFixed() {
		return fixed;
	}
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public String getMaximum() {
		return maximum;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	public String getBasicPer() {
		return basicPer;
	}
	public void setBasicPer(String basicPer) {
		this.basicPer = basicPer;
	}
	public String getPayPeriod() {
		return payPeriod;
	}
	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getExecuse() {
		return execuse;
	}
	public void setExecuse(String execuse) {
		this.execuse = execuse;
	}
	public double getMaxAllowed() {
		return maxAllowed;
	}
	public void setMaxAllowed(double maxAllowed) {
		this.maxAllowed = maxAllowed;
	}
	public String getAbsenceType() {
		return absenceType;
	}
	public void setAbsenceType(String absenceType) {
		this.absenceType = absenceType;
	}
	public String getCalculateIn() {
		return calculateIn;
	}
	public void setCalculateIn(String calculateIn) {
		this.calculateIn = calculateIn;
	}
	public double getDblRate() {
		return dblRate;
	}
	public void setDblRate(double dblRate) {
		this.dblRate = dblRate;
	}
	public double getNos() {
		return nos;
	}
	public void setNos(double nos) {
		this.nos = nos;
	}
	public String getDeductService() {
		return deductService;
	}
	public void setDeductService(String deductService) {
		this.deductService = deductService;
	}
	public String getDeductFrom() {
		return deductFrom;
	}
	public void setDeductFrom(String deductFrom) {
		this.deductFrom = deductFrom;
	}
	public int getDeductionItem() {
		return deductionItem;
	}
	public void setDeductionItem(int deductionItem) {
		this.deductionItem = deductionItem;
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
	public String getEosReason() {
		return eosReason;
	}
	public void setEosReason(String eosReason) {
		this.eosReason = eosReason;
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
	public double getEosBases() {
		return eosBases;
	}
	public void setEosBases(double eosBases) {
		this.eosBases = eosBases;
	}
	public String getEosIsDependable() {
		return eosIsDependable;
	}
	public void setEosIsDependable(String eosIsDependable) {
		this.eosIsDependable = eosIsDependable;
	}
	 
	 
	 
}
