package reports;

public class AllowanceModel implements Comparable<AllowanceModel> {

	
	private int allowanceId;
	
	private String allowanceName;
	
	private String allowanceNameAr;
	
	private double amount;
	
	private String recNowithId;

	/**
	 * @return the allowanceId
	 */
	public int getAllowanceId() {
		return allowanceId;
	}

	/**
	 * @param allowanceId the allowanceId to set
	 */
	public void setAllowanceId(int allowanceId) {
		this.allowanceId = allowanceId;
	}

	/**
	 * @return the allowanceName
	 */
	public String getAllowanceName() {
		return allowanceName;
	}

	/**
	 * @param allowanceName the allowanceName to set
	 */
	public void setAllowanceName(String allowanceName) {
		this.allowanceName = allowanceName;
	}

	/**
	 * @return the allowanceNameAr
	 */
	public String getAllowanceNameAr() {
		return allowanceNameAr;
	}

	/**
	 * @param allowanceNameAr the allowanceNameAr to set
	 */
	public void setAllowanceNameAr(String allowanceNameAr) {
		this.allowanceNameAr = allowanceNameAr;
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

	/**
	 * @return the recNowithId
	 */
	public String getRecNowithId() {
		return recNowithId;
	}

	/**
	 * @param recNowithId the recNowithId to set
	 */
	public void setRecNowithId(String recNowithId) {
		this.recNowithId = recNowithId;
	}

	@Override
	public int compareTo(AllowanceModel o) {
		// TODO Auto-generated method stub
		return this.compareTo(o);
	}
	
	
	
}
