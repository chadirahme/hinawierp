package model;

import java.util.Date;
import java.util.List;

public class TimeSheetDataModel 
{
	private int reprotsLineNo;
	private int reprotsRecNO;
	private int totalOverTime;
	private String dayOrHours;
	private String timesheetDate;
	private double absance;
	private double sick;
	private double leave;
	
	private int recNo;
	private Date tsDate;
	private String fromTime;
	private String toTime;
	
	private Date tsFromTime;
	private Date tstoTime;
	
	private int empKey;
	private String empNo;
	private String enFullName;
	private String dayofWeek;
	private int dayNo;
	private String offDay;
	private int unitId;
	private String unitName;
	private String timing;
	private double shiftKey;
	private String calcFlag;
	private double unitNO;
	private double normalUnitNO;
	private double totalUnitNo;
	private double totalNormalUnitNo;
	private String status;
	private String holidayDesc;
	private boolean holiday;
	private String notes;
	private String tomorrowPlan;
	private String attachPath;
	
	private double calculation;
	private double units;
	private double amount;
	
	private int tsMonth;
	private int tsYear;
	private String tsMonthName;
	
	private int serviceId;
	private int projectkey;
	
	private String tsStatus;
	
	private double presentDays;
	private double holidays;
	private int totalEmployees;
	
	private String projectName;
	private String enPositionName;
	private String department;
	
	private List<OverTimeModel> lstOverTime;
	private double ot1;
	private double ot2;
	private double ot3;
	private double totalOTUnits;
	
	private int lineNO;
	
	private int approved;
	private int supervisorId;
	private String supervisorName;
	
	private int noOfshifts;
	private int shiftRecNo;
	private String leaveFlag;
	private int customerJobRefKey;
	private boolean mainShift;
	
	//report
	private double totalWorkingHours;
	
	private int totalNoOfDays;
	
	private double checkinLatitude;
	private double checkinLongitude;
	private double checkoutLatitude;
	private double checkoutLongitude;
	private String checkinNote;
	private String checkoutNote;
	//Reason,Result,DestinaceKm,DestinaceMeter,CustomerType,CustomerName  
	private String reason;
	private String result;
	private String customerType;
	private String customerName;
	private int destinaceKm;
	private int destinaceMeter;
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getDestinaceKm() {
		return destinaceKm;
	}
	public void setDestinaceKm(int destinaceKm) {
		this.destinaceKm = destinaceKm;
	}
	public int getDestinaceMeter() {
		return destinaceMeter;
	}
	public void setDestinaceMeter(int destinaceMeter) {
		this.destinaceMeter = destinaceMeter;
	}
	
	
	public Date getTsDate() {
		return tsDate;
	}
	public void setTsDate(Date tsDate) {
		this.tsDate = tsDate;
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
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public double getShiftKey() {
		return shiftKey;
	}
	public void setShiftKey(double shiftKey) {
		this.shiftKey = shiftKey;
	}
	public String getCalcFlag() {
		return calcFlag;
	}
	public void setCalcFlag(String calcFlag) {
		this.calcFlag = calcFlag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHolidayDesc() {
		return holidayDesc;
	}
	public void setHolidayDesc(String holidayDesc) {
		this.holidayDesc = holidayDesc;
	}
	public boolean isHoliday() {
		return holiday;
	}
	public void setHoliday(boolean holiday) {
		this.holiday = holiday;
	}
	public double getCalculation() {
		return calculation;
	}
	public void setCalculation(double calculation) {
		this.calculation = calculation;
	}
	public double getUnits() {
		return units;
	}
	public void setUnits(double units) {
		this.units = units;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getTiming() {
		return timing;
	}
	public void setTiming(String timing) {
		this.timing = timing;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public double getUnitNO() {
		return unitNO;
	}
	public void setUnitNO(double unitNO) {
		this.unitNO = unitNO;
	}
	public double getTotalUnitNo() {
		return totalUnitNo;
	}
	public void setTotalUnitNo(double totalUnitNo) {
		this.totalUnitNo = totalUnitNo;
	}
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getProjectkey() {
		return projectkey;
	}
	public void setProjectkey(int projectkey) {
		this.projectkey = projectkey;
	}
	public int getTsMonth() {
		return tsMonth;
	}
	public void setTsMonth(int tsMonth) {
		this.tsMonth = tsMonth;
	}
	public int getTsYear() {
		return tsYear;
	}
	public void setTsYear(int tsYear) {
		this.tsYear = tsYear;
	}
	public String getTsStatus() {
		return tsStatus;
	}
	public void setTsStatus(String tsStatus) {
		this.tsStatus = tsStatus;
	}
	public double getPresentDays() {
		return presentDays;
	}
	public void setPresentDays(double presentDays) {
		this.presentDays = presentDays;
	}
	public double getHolidays() {
		return holidays;
	}
	public void setHolidays(double holidays) {
		this.holidays = holidays;
	}
	public int getTotalEmployees() {
		return totalEmployees;
	}
	public void setTotalEmployees(int totalEmployees) {
		this.totalEmployees = totalEmployees;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTsMonthName() {
		return tsMonthName;
	}
	public void setTsMonthName(String tsMonthName) {
		this.tsMonthName = tsMonthName;
	}
	public List<OverTimeModel> getLstOverTime() {
		return lstOverTime;
	}
	public void setLstOverTime(List<OverTimeModel> lstOverTime) {
		this.lstOverTime = lstOverTime;
	}
	public double getOt1() {
		return ot1;
	}
	public void setOt1(double ot1) {
		this.ot1 = ot1;
	}
	public double getOt2() {
		return ot2;
	}
	public void setOt2(double ot2) {
		this.ot2 = ot2;
	}
	public double getOt3() {
		return ot3;
	}
	public void setOt3(double ot3) {
		this.ot3 = ot3;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	public int getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
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
	 * @return the enPositionName
	 */
	public String getEnPositionName() {
		return enPositionName;
	}
	/**
	 * @param enPositionName the enPositionName to set
	 */
	public void setEnPositionName(String enPositionName) {
		this.enPositionName = enPositionName;
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
	 * @return the dayofWeek
	 */
	public String getDayofWeek() {
		return dayofWeek;
	}
	/**
	 * @param dayofWeek the dayofWeek to set
	 */
	public void setDayofWeek(String dayofWeek) {
		this.dayofWeek = dayofWeek;
	}
	/**
	 * @return the totalOverTime
	 */
	public int getTotalOverTime() {
		return totalOverTime;
	}
	/**
	 * @param totalOverTime the totalOverTime to set
	 */
	public void setTotalOverTime(int totalOverTime) {
		this.totalOverTime = totalOverTime;
	}
	/**
	 * @return the dayOrHours
	 */
	public String getDayOrHours() {
		return dayOrHours;
	}
	/**
	 * @param dayOrHours the dayOrHours to set
	 */
	public void setDayOrHours(String dayOrHours) {
		this.dayOrHours = dayOrHours;
	}
	/**
	 * @return the absance
	 */
	public double getAbsance() {
		return absance;
	}
	/**
	 * @param absance the absance to set
	 */
	public void setAbsance(double absance) {
		this.absance = absance;
	}
	/**
	 * @return the sick
	 */
	public double getSick() {
		return sick;
	}
	/**
	 * @param sick the sick to set
	 */
	public void setSick(double sick) {
		this.sick = sick;
	}
	/**
	 * @return the leave
	 */
	public double getLeave() {
		return leave;
	}
	/**
	 * @param leave the leave to set
	 */
	public void setLeave(double leave) {
		this.leave = leave;
	}
	/**
	 * @return the timesheetDate
	 */
	public String getTimesheetDate() {
		return timesheetDate;
	}
	/**
	 * @param timesheetDate the timesheetDate to set
	 */
	public void setTimesheetDate(String timesheetDate) {
		this.timesheetDate = timesheetDate;
	}
	public Date getTsFromTime() {
		return tsFromTime;
	}
	public void setTsFromTime(Date tsFromTime) {
		this.tsFromTime = tsFromTime;
	}
	public Date getTstoTime() {
		return tstoTime;
	}
	public void setTstoTime(Date tstoTime) {
		this.tstoTime = tstoTime;
	}
	public int getNoOfshifts() {
		return noOfshifts;
	}
	public void setNoOfshifts(int noOfshifts) {
		this.noOfshifts = noOfshifts;
	}
	public int getShiftRecNo() {
		return shiftRecNo;
	}
	public void setShiftRecNo(int shiftRecNo) {
		this.shiftRecNo = shiftRecNo;
	}
	public double getNormalUnitNO() {
		return normalUnitNO;
	}
	public void setNormalUnitNO(double normalUnitNO) {
		this.normalUnitNO = normalUnitNO;
	}
	public double getTotalNormalUnitNo() {
		return totalNormalUnitNo;
	}
	public void setTotalNormalUnitNo(double totalNormalUnitNo) {
		this.totalNormalUnitNo = totalNormalUnitNo;
	}
	public int getLineNO() {
		return lineNO;
	}
	public void setLineNO(int lineNO) {
		this.lineNO = lineNO;
	}
	public String getLeaveFlag() {
		return leaveFlag;
	}
	public void setLeaveFlag(String leaveFlag) {
		this.leaveFlag = leaveFlag;
	}
	public double getTotalOTUnits() {
		return totalOTUnits;
	}
	public void setTotalOTUnits(double totalOTUnits) {
		this.totalOTUnits = totalOTUnits;
	}
	public int getCustomerJobRefKey() {
		return customerJobRefKey;
	}
	public void setCustomerJobRefKey(int customerJobRefKey) {
		this.customerJobRefKey = customerJobRefKey;
	}
	public double getTotalWorkingHours() {
		return totalWorkingHours;
	}
	public void setTotalWorkingHours(double totalWorkingHours) {
		this.totalWorkingHours = totalWorkingHours;
	}
	public int getTotalNoOfDays() {
		return totalNoOfDays;
	}
	public void setTotalNoOfDays(int totalNoOfDays) {
		this.totalNoOfDays = totalNoOfDays;
	}
	public String getTomorrowPlan() {
		return tomorrowPlan;
	}
	public void setTomorrowPlan(String tomorrowPlan) {
		this.tomorrowPlan = tomorrowPlan;
	}
	public boolean isMainShift() {
		return mainShift;
	}
	public void setMainShift(boolean mainShift) {
		this.mainShift = mainShift;
	}
	public String getAttachPath() {
		return attachPath;
	}
	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}
	public double getCheckinLatitude() {
		return checkinLatitude;
	}
	public void setCheckinLatitude(double checkinLatitude) {
		this.checkinLatitude = checkinLatitude;
	}
	public double getCheckinLongitude() {
		return checkinLongitude;
	}
	public void setCheckinLongitude(double checkinLongitude) {
		this.checkinLongitude = checkinLongitude;
	}
	public double getCheckoutLatitude() {
		return checkoutLatitude;
	}
	public void setCheckoutLatitude(double checkoutLatitude) {
		this.checkoutLatitude = checkoutLatitude;
	}
	public double getCheckoutLongitude() {
		return checkoutLongitude;
	}
	public void setCheckoutLongitude(double checkoutLongitude) {
		this.checkoutLongitude = checkoutLongitude;
	}
	public String getCheckinNote() {
		return checkinNote;
	}
	public void setCheckinNote(String checkinNote) {
		this.checkinNote = checkinNote;
	}
	public String getCheckoutNote() {
		return checkoutNote;
	}
	public void setCheckoutNote(String checkoutNote) {
		this.checkoutNote = checkoutNote;
	}
	
	

}
