package model;

public class EmailSignatureModel {
	
	
	
	private int recNo;
	
	private int userId;
	
	private String signature="";
	
	//for reminders
	
	private int reminderId;
	
	private String reminderName;
	
	private int companyId;

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
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the reminderId
	 */
	public int getReminderId() {
		return reminderId;
	}

	/**
	 * @param reminderId the reminderId to set
	 */
	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	/**
	 * @return the reminderName
	 */
	public String getReminderName() {
		return reminderName;
	}

	/**
	 * @param reminderName the reminderName to set
	 */
	public void setReminderName(String reminderName) {
		this.reminderName = reminderName;
	}

	/**
	 * @return the companyId
	 */
	public int getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	
	
	
	

}
