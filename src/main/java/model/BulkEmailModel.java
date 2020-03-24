package model;

import java.util.Date;

public class BulkEmailModel 
{
	private int BulkEmailId;
	private String Descreption;
	private String EmailsFilter;
	private int NumberofMails;
	private String Subject;
	private String EmailBody;
	private int EmailStatus;
	private String creationdate;
	private String status;
	private String lastTimeRun;
	private int NumberEmailsend;
	private boolean unsubscribe;
	private String emailType;
	private boolean withReminder;
	
	public int getBulkEmailId() {
		return BulkEmailId;
	}
	public void setBulkEmailId(int bulkEmailId) {
		BulkEmailId = bulkEmailId;
	}
	public String getDescreption() {
		return Descreption;
	}
	public void setDescreption(String descreption) {
		Descreption = descreption;
	}
	public String getEmailsFilter() {
		return EmailsFilter;
	}
	public void setEmailsFilter(String emailsFilter) {
		EmailsFilter = emailsFilter;
	}
	public int getNumberofMails() {
		return NumberofMails;
	}
	public void setNumberofMails(int numberofMails) {
		NumberofMails = numberofMails;
	}
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	public String getEmailBody() {
		return EmailBody;
	}
	public void setEmailBody(String emailBody) {
		EmailBody = emailBody;
	}
	public int getEmailStatus() {
		return EmailStatus;
	}
	public void setEmailStatus(int emailStatus) {
		EmailStatus = emailStatus;
	}
	public String getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(String creationdate) {
		this.creationdate = creationdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastTimeRun() {
		return lastTimeRun;
	}
	public void setLastTimeRun(String lastTimeRun) {
		this.lastTimeRun = lastTimeRun;
	}
	public int getNumberEmailsend() {
		return NumberEmailsend;
	}
	public void setNumberEmailsend(int numberEmailsend) {
		NumberEmailsend = numberEmailsend;
	}
	public boolean isUnsubscribe() {
		return unsubscribe;
	}
	public void setUnsubscribe(boolean unsubscribe) {
		this.unsubscribe = unsubscribe;
	}
	public String getEmailType() {
		return emailType;
	}
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public boolean isWithReminder() {
		return withReminder;
	}
	public void setWithReminder(boolean withReminder) {
		this.withReminder = withReminder;
	}


}
