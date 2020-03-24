package model;

public class OverTimeModel 
{
	
	private int lineNo;
	private int recNo;
	
	private int projectKey;
	private int labourKey;
	private int positionId;
	private double otRate;
	private String dayType;
	private double hours;
	private String otCalc;
	private String otFlag;
	private double salaryItem;
	private double calcHours;
	
	private double totalUnits;
	private double totalAmount;
	
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public double getOtRate() {
		return otRate;
	}
	public void setOtRate(double otRate) {
		this.otRate = otRate;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	public double getHours() {
		return hours;
	}
	public void setHours(double hours) {
		this.hours = hours;
	}
	public String getOtCalc() {
		return otCalc;
	}
	public void setOtCalc(String otCalc) {
		this.otCalc = otCalc;
	}
	public String getOtFlag() {
		return otFlag;
	}
	public void setOtFlag(String otFlag) {
		this.otFlag = otFlag;
	}
	public double getSalaryItem() {
		return salaryItem;
	}
	public void setSalaryItem(double salaryItem) {
		this.salaryItem = salaryItem;
	}
	public double getCalcHours() {
		return calcHours;
	}
	public void setCalcHours(double calcHours) {
		this.calcHours = calcHours;
	}
	public double getTotalUnits() {
		return totalUnits;
	}
	public void setTotalUnits(double totalUnits) {
		this.totalUnits = totalUnits;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * @return the lineNo
	 */
	public int getLineNo() {
		return lineNo;
	}
	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	/**
	 * @return the recNo
	 */
	public int getRecNo() {
		return recNo;
	}
	/**
	 * @param recNo the recNo to set
	 */
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	/**
	 * @return the projectKey
	 */
	public int getProjectKey() {
		return projectKey;
	}
	/**
	 * @param projectKey the projectKey to set
	 */
	public void setProjectKey(int projectKey) {
		this.projectKey = projectKey;
	}
	/**
	 * @return the labourKey
	 */
	public int getLabourKey() {
		return labourKey;
	}
	/**
	 * @param labourKey the labourKey to set
	 */
	public void setLabourKey(int labourKey) {
		this.labourKey = labourKey;
	}
	
	

}
