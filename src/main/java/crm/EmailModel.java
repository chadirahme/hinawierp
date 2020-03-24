package crm;

import model.CustomerModel;

public class EmailModel 
{

	private String subject;
	private String fromEmail;
	private String body;
	private String dateReceived;
	private String sortDateReceived;
	private String emailFromAddress ;
	private String emailTo ;
	private String emailCc ;
	
	private CustomerModel emailCustomer;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFromEmail() {
		return fromEmail;
	}
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDateReceived() {
		return dateReceived;
	}
	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}
	public String getEmailFromAddress() {
		return emailFromAddress;
	}
	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
	}
	public CustomerModel getEmailCustomer() {
		return emailCustomer;
	}
	public void setEmailCustomer(CustomerModel emailCustomer) {
		this.emailCustomer = emailCustomer;
	}
	public String getSortDateReceived() {
		return sortDateReceived;
	}
	public void setSortDateReceived(String sortDateReceived) {
		this.sortDateReceived = sortDateReceived;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
}
