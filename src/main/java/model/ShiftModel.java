package model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShiftModel 
{

	private int recNo;
	private int compKey;
	private int shiftKey;
	private int unitKey;
	private String shiftCode;
	private int noOfShifts;
	private String timingFlag;
	private String unitName;
	private double units;
	private Date fromTime;
	private Date toTime;
	private boolean timingShift;
	private boolean firstRecord;
	private boolean dayfirstRecord;
	private ShiftModel shiftType;
	private int shiftDetRecNo;
	
	private String dayName;
	private int shiftCodeRowspan;
	private int dayRowspan;
	
	private int dayNo;
	private boolean offDay;
	
	//use in holiday tabs
	private String fromDate;
	private String toDate;
	private String holidayType;
	private String holidayDescription;
	private int totalDays;
	private boolean shiftHolidays[];
	private String labelColor[];
	private Map mapShifts;
		
	//used in Overtime
	private int unitId;
	private int normalHours;
	private int maxOT;
	private String calculate;
	private double calcHours;
	private boolean calculateOT;
	
	private int overtimeNo;
	private String dayType;
	private double otRate;
	private double otHours;
	private String autoFill;
	private int salaryItem;
	private HRListValuesModel hrSalaryItem;
	
	private List<ShiftModel> lstOverTime;
	
	
	private boolean shiftHeaderChecked;
	
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public int getCompKey() {
		return compKey;
	}
	public void setCompKey(int compKey) {
		this.compKey = compKey;
	}
	public int getShiftKey() {
		return shiftKey;
	}
	public void setShiftKey(int shiftKey) {
		this.shiftKey = shiftKey;
	}
	public int getUnitKey() {
		return unitKey;
	}
	public void setUnitKey(int unitKey) {
		this.unitKey = unitKey;
	}
	public String getShiftCode() {
		return shiftCode;
	}
	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}
	public int getNoOfShifts() {
		return noOfShifts;
	}
	public void setNoOfShifts(int noOfShifts) {
		this.noOfShifts = noOfShifts;
	}
	public String getTimingFlag() {
		return timingFlag;
	}
	public void setTimingFlag(String timingFlag) {
		this.timingFlag = timingFlag;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public double getUnits() {
		return units;
	}
	public void setUnits(double units) {
		this.units = units;
	}
	public Date getFromTime() {
		return fromTime;
	}
	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}
	public Date getToTime() {
		return toTime;
	}
	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}
	
	public boolean isTimingShift() {
		return timingShift;
	}
	public void setTimingShift(boolean timingShift) {
		this.timingShift = timingShift;
	}
	public boolean isFirstRecord() {
		return firstRecord;
	}
	public void setFirstRecord(boolean firstRecord) {
		this.firstRecord = firstRecord;
	}
	public ShiftModel getShiftType() {
		return shiftType;
	}
	public void setShiftType(ShiftModel shiftType) {
		this.shiftType = shiftType;
	}
	public int getShiftDetRecNo() {
		return shiftDetRecNo;
	}
	public void setShiftDetRecNo(int shiftDetRecNo) {
		this.shiftDetRecNo = shiftDetRecNo;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	public int getShiftCodeRowspan() {
		return shiftCodeRowspan;
	}
	public void setShiftCodeRowspan(int shiftCodeRowspan) {
		this.shiftCodeRowspan = shiftCodeRowspan;
	}
	public int getDayRowspan() {
		return dayRowspan;
	}
	public void setDayRowspan(int dayRowspan) {
		this.dayRowspan = dayRowspan;
	}
	public boolean isDayfirstRecord() {
		return dayfirstRecord;
	}
	public void setDayfirstRecord(boolean dayfirstRecord) {
		this.dayfirstRecord = dayfirstRecord;
	}
	public int getDayNo() {
		return dayNo;
	}
	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}
	public boolean isOffDay() {
		return offDay;
	}
	public void setOffDay(boolean offDay) {
		this.offDay = offDay;
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
	public String getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}
	public String getHolidayDescription() {
		return holidayDescription;
	}
	public void setHolidayDescription(String holidayDescription) {
		this.holidayDescription = holidayDescription;
	}
	public int getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}
	public boolean[] getShiftHolidays() {
		return shiftHolidays;
	}
	public void setShiftHolidays(boolean[] shiftHolidays) {
		this.shiftHolidays = shiftHolidays;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getNormalHours() {
		return normalHours;
	}
	public void setNormalHours(int normalHours) {
		this.normalHours = normalHours;
	}
	public int getMaxOT() {
		return maxOT;
	}
	public void setMaxOT(int maxOT) {
		this.maxOT = maxOT;
	}
	public String getCalculate() {
		return calculate;
	}
	public void setCalculate(String calculate) {
		this.calculate = calculate;
	}
	public double getCalcHours() {
		return calcHours;
	}
	public void setCalcHours(double calcHours) {
		this.calcHours = calcHours;
	}
	public boolean isCalculateOT() {
		return calculateOT;
	}
	public void setCalculateOT(boolean calculateOT) {
		this.calculateOT = calculateOT;
	}
	public int getOvertimeNo() {
		return overtimeNo;
	}
	public void setOvertimeNo(int overtimeNo) {
		this.overtimeNo = overtimeNo;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	public double getOtRate() {
		return otRate;
	}
	public void setOtRate(double otRate) {
		this.otRate = otRate;
	}
	public double getOtHours() {
		return otHours;
	}
	public void setOtHours(double otHours) {
		this.otHours = otHours;
	}
	public String getAutoFill() {
		return autoFill;
	}
	public void setAutoFill(String autoFill) {
		this.autoFill = autoFill;
	}
	public int getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(int salaryItem) {
		this.salaryItem = salaryItem;
	}
	public List<ShiftModel> getLstOverTime() {
		return lstOverTime;
	}
	public void setLstOverTime(List<ShiftModel> lstOverTime) {
		this.lstOverTime = lstOverTime;
	}
	public HRListValuesModel getHrSalaryItem() {
		return hrSalaryItem;
	}
	public void setHrSalaryItem(HRListValuesModel hrSalaryItem) {
		this.hrSalaryItem = hrSalaryItem;
	}
	public Map getMapShifts() {
		return mapShifts;
	}
	public void setMapShifts(Map mapShifts) {
		this.mapShifts = mapShifts;
	}
	public String[] getLabelColor() {
		return labelColor;
	}
	public void setLabelColor(String labelColor[]) {
		this.labelColor = labelColor;
	}
	public boolean isShiftHeaderChecked() {
		return shiftHeaderChecked;
	}
	public void setShiftHeaderChecked(boolean shiftHeaderChecked) {
		this.shiftHeaderChecked = shiftHeaderChecked;
	}
	
	
	
}
