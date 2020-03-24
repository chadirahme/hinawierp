package hr.model;

public class ContactModel 
{

	private int empkey;
	private int contactId;
	private String details;
	private String notes;
	private String description;
	
	public int getEmpkey() {
		return empkey;
	}
	public void setEmpkey(int empkey) {
		this.empkey = empkey;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
