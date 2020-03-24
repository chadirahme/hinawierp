package model;

import java.util.Date;

public class ContactEmailModel 
{

	private int emailId;
	private String email;
	private boolean editingStatus;
	private boolean unsubscribe;
	private Date unsubscribedate;
	private String bulkGroup;
	private String industryGroup;
	
	
	public int getEmailId() {
		return emailId;
	}
	public void setEmailId(int emailId) {
		this.emailId = emailId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	public boolean isUnsubscribe() {
		return unsubscribe;
	}
	public void setUnsubscribe(boolean unsubscribe) {
		this.unsubscribe = unsubscribe;
	}
	public Date getUnsubscribedate() {
		return unsubscribedate;
	}
	public void setUnsubscribedate(Date unsubscribedate) {
		this.unsubscribedate = unsubscribedate;
	}
	public String getBulkGroup() {
		return bulkGroup;
	}
	public void setBulkGroup(String bulkGroup) {
		this.bulkGroup = bulkGroup;
	}
	public String getIndustryGroup() {
		return industryGroup;
	}
	public void setIndustryGroup(String industryGroup) {
		this.industryGroup = industryGroup;
	}
}
