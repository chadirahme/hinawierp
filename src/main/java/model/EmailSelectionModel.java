package model;

public class EmailSelectionModel {
	
	private boolean to=false;
	private boolean cc=false;
	private boolean bcc=false;
	private String email="";
	private int custOrProspKey;
	private String sourceType;
	private String customerName;
	private boolean selectedEmail=false;
	private int reminderId;
	private Double localBalance;
	private String CusContractExpiry;
	
	private String emailsubject;
	private String emailbody;
	private String sendDate;
	
	/**
	 * @return the to
	 */
	public boolean isTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(boolean to) {
		this.to = to;
	}
	/**
	 * @return the cc
	 */
	public boolean isCc() {
		return cc;
	}
	/**
	 * @param cc the cc to set
	 */
	public void setCc(boolean cc) {
		this.cc = cc;
	}
	/**
	 * @return the bcc
	 */
	public boolean isBcc() {
		return bcc;
	}
	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(boolean bcc) {
		this.bcc = bcc;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the custOrProspKey
	 */
	public int getCustOrProspKey() {
		return custOrProspKey;
	}
	/**
	 * @param custOrProspKey the custOrProspKey to set
	 */
	public void setCustOrProspKey(int custOrProspKey) {
		this.custOrProspKey = custOrProspKey;
	}
	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}
	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the selectedEmail
	 */
	public boolean isSelectedEmail() {
		return selectedEmail;
	}
	/**
	 * @param selectedEmail the selectedEmail to set
	 */
	public void setSelectedEmail(boolean selectedEmail) {
		this.selectedEmail = selectedEmail;
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
	public Double getLocalBalance() {
		return localBalance;
	}
	public void setLocalBalance(Double localBalance) {
		this.localBalance = localBalance;
	}
	public String getCusContractExpiry() {
		return CusContractExpiry;
	}
	public void setCusContractExpiry(String cusContractExpiry) {
		CusContractExpiry = cusContractExpiry;
	}
	public String getEmailsubject() {
		return emailsubject;
	}
	public void setEmailsubject(String emailsubject) {
		this.emailsubject = emailsubject;
	}
	public String getEmailbody() {
		return emailbody;
	}
	public void setEmailbody(String emailbody) {
		this.emailbody = emailbody;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	
	
	
	
	

}
