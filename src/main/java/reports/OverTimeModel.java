package reports;

public class OverTimeModel implements Comparable<OverTimeModel>  {
	
	private double overtimeRate;
	
	private int recNo;
	
	private int monthNumber;
	
	private int yearNumber;
	
	
	private double otHours;
	
	private double amount;

	/**
	 * @return the overtimeRate
	 */
	public double getOvertimeRate() {
		return overtimeRate;
	}

	/**
	 * @param overtimeRate the overtimeRate to set
	 */
	public void setOvertimeRate(double overtimeRate) {
		this.overtimeRate = overtimeRate;
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
	 * @return the monthNumber
	 */
	public int getMonthNumber() {
		return monthNumber;
	}

	/**
	 * @param monthNumber the monthNumber to set
	 */
	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	/**
	 * @return the yearNumber
	 */
	public int getYearNumber() {
		return yearNumber;
	}

	/**
	 * @param yearNumber the yearNumber to set
	 */
	public void setYearNumber(int yearNumber) {
		this.yearNumber = yearNumber;
	}

	

	/**
	 * @return the otHours
	 */
	public double getOtHours() {
		return otHours;
	}

	/**
	 * @param otHours the otHours to set
	 */
	public void setOtHours(double otHours) {
		this.otHours = otHours;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int compareTo(OverTimeModel o) {
		// TODO Auto-generated method stub
		return this.compareTo(o);
	}
	
	
	
	
}
