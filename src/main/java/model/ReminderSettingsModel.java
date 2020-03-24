package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReminderSettingsModel 
{

	private int reminderid;
	private int companyid;
	private String remindername="";
	private int remindertype;
	private Date startdate;
	private Date remindertime;
	private String remindersetting;
	private int repeatedays;
	private String weekly="";
	private String monthly="";
	private String monthlydays="";
	private Date expireddate;
	private boolean enablereminder;
	private String sendtotype;
	private int serviceListRefKey;
	private String ccemail="";
	private Date creationDate;
	private int mailId;
	private boolean hideCustomer=true;
	private boolean hideProspective=true;
	private boolean hideOtherfileds=true;
	private boolean allcustomers;

	List<EmailSelectionModel> selectedCustomerEmails = new ArrayList<EmailSelectionModel>();
	
	public int getReminderid() {
		return reminderid;
	}
	public void setReminderid(int reminderid) {
		this.reminderid = reminderid;
	}
	public int getCompanyid() {
		return companyid;
	}
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	public String getRemindername() {
		return remindername;
	}
	public void setRemindername(String remindername) {
		this.remindername = remindername;
	}
	public int getRemindertype() {
		return remindertype;
	}
	public void setRemindertype(int remindertype) {
		this.remindertype = remindertype;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getRemindertime() {
		return remindertime;
	}
	public void setRemindertime(Date remindertime) {
		this.remindertime = remindertime;
	}
	
	public String getRemindersetting() 
	{
		return remindersetting;
	}
	
	public void setRemindersetting(String remindersetting) {
		this.remindersetting = remindersetting;
	}
	public int getRepeatedays() {
		return repeatedays;
	}
	public void setRepeatedays(int repeatedays) {
		this.repeatedays = repeatedays;
	}
	public String getWeekly() {
		return weekly;
	}
	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}
	public String getMonthly() {
		return monthly;
	}
	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}
	public String getMonthlydays() {
		return monthlydays;
	}
	public void setMonthlydays(String monthlydays) {
		this.monthlydays = monthlydays;
	}
	public Date getExpireddate() {
		return expireddate;
	}
	public void setExpireddate(Date expireddate) {
		this.expireddate = expireddate;
	}
	public boolean isEnablereminder() {
		return enablereminder;
	}
	public void setEnablereminder(boolean enablereminder) {
		this.enablereminder = enablereminder;
	}
	public String getSendtotype() {
		return sendtotype;
	}
	public void setSendtotype(String sendtotype) {
		this.sendtotype = sendtotype;
	}
	public int getServiceListRefKey() {
		return serviceListRefKey;
	}
	public void setServiceListRefKey(int serviceListRefKey) {
		this.serviceListRefKey = serviceListRefKey;
	}
	public String getCcemail() {
		return ccemail;
	}
	public void setCcemail(String ccemail) {
		this.ccemail = ccemail;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the mailId
	 */
	public int getMailId() {
		return mailId;
	}
	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(int mailId) {
		this.mailId = mailId;
	}
	/**
	 * @return the hideCustomer
	 */
	public boolean isHideCustomer() {
		return hideCustomer;
	}
	/**
	 * @param hideCustomer the hideCustomer to set
	 */
	public void setHideCustomer(boolean hideCustomer) {
		this.hideCustomer = hideCustomer;
	}
	/**
	 * @return the hideProspective
	 */
	public boolean isHideProspective() {
		return hideProspective;
	}
	/**
	 * @param hideProspective the hideProspective to set
	 */
	public void setHideProspective(boolean hideProspective) {
		this.hideProspective = hideProspective;
	}
	/**
	 * @return the hideOtherfileds
	 */
	public boolean isHideOtherfileds() {
		return hideOtherfileds;
	}
	/**
	 * @param hideOtherfileds the hideOtherfileds to set
	 */
	public void setHideOtherfileds(boolean hideOtherfileds) {
		this.hideOtherfileds = hideOtherfileds;
	}
	/**
	 * @return the selectedCustomerEmails
	 */
	public List<EmailSelectionModel> getSelectedCustomerEmails() {
		return selectedCustomerEmails;
	}
	/**
	 * @param selectedCustomerEmails the selectedCustomerEmails to set
	 */
	public void setSelectedCustomerEmails(
			List<EmailSelectionModel> selectedCustomerEmails) {
		this.selectedCustomerEmails = selectedCustomerEmails;
	}
	public boolean isAllcustomers() {
		return allcustomers;
	}
	public void setAllcustomers(boolean allcustomers) {
		this.allcustomers = allcustomers;
	}
	
	
	
	
}
