package hr.model;

import java.util.Date;

public class PassportModel 
{

	private int recno;
	private int empkey;
	private String passportNumber;
	private int issureCountryId;
	private String issueCountry;
	private int issuePlaceId;
	private String issueCity;
	private String issueDate;
	private String fileNo;
	private String expiryDate;
	private String notes;
	private String status;
	private int remaindays;
	private String passportLocation;
	
	
	public int getRecno() {
		return recno;
	}
	public void setRecno(int recno) {
		this.recno = recno;
	}
	public int getEmpkey() {
		return empkey;
	}
	public void setEmpkey(int empkey) {
		this.empkey = empkey;
	}
	public String getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}
	public int getIssureCountryId() {
		return issureCountryId;
	}
	public void setIssureCountryId(int issureCountryId) {
		this.issureCountryId = issureCountryId;
	}
	public int getIssuePlaceId() {
		return issuePlaceId;
	}
	public void setIssuePlaceId(int issuePlaceId) {
		this.issuePlaceId = issuePlaceId;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getFileNo() {
		return fileNo;
	}
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRemaindays() {
		return remaindays;
	}
	public void setRemaindays(int remaindays) {
		this.remaindays = remaindays;
	}
	public String getPassportLocation() {
		return passportLocation;
	}
	public void setPassportLocation(String passportLocation) {
		this.passportLocation = passportLocation;
	}
	public String getIssueCountry() {
		return issueCountry;
	}
	public void setIssueCountry(String issueCountry) {
		this.issueCountry = issueCountry;
	}
	public String getIssueCity() {
		return issueCity;
	}
	public void setIssueCity(String issueCity) {
		this.issueCity = issueCity;
	}
	
	
	
}
