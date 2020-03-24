package model;

import java.util.Date;

public class TenantModel {
	private int custkey;
	private Integer nationality;
	private Date tradeLicenseExpiry;
	private Date passportExpiry;
	private Date residenceExpiry;
	private String tradeLicenseNo="";
	private String passportNo="";
	private String placeOfIssue="";
	private String residenceVisaNo="";
	private String sponserName="";
	private String employmentDesignation="";
	private String salaryIncome="";

	public int getCustkey() {
		return custkey;
	}

	public void setCustkey(int custkey) {
		this.custkey = custkey;
	}

	public Integer getNationality() {
		return nationality;
	}

	public void setNationality(Integer nationality) {
		this.nationality = nationality;
	}

	public Date getTradeLicenseExpiry() {
		return tradeLicenseExpiry;
	}

	public void setTradeLicenseExpiry(Date tradeLicenseExpiry) {
		this.tradeLicenseExpiry = tradeLicenseExpiry;
	}

	public Date getPassportExpiry() {
		return passportExpiry;
	}

	public void setPassportExpiry(Date passportExpiry) {
		this.passportExpiry = passportExpiry;
	}

	public Date getResidenceExpiry() {
		return residenceExpiry;
	}

	public void setResidenceExpiry(Date residenceExpiry) {
		this.residenceExpiry = residenceExpiry;
	}

	public String getTradeLicenseNo() {
		return tradeLicenseNo;
	}

	public void setTradeLicenseNo(String tradeLicenseNo) {
		this.tradeLicenseNo = tradeLicenseNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public String getResidenceVisaNo() {
		return residenceVisaNo;
	}

	public void setResidenceVisaNo(String residenceVisaNo) {
		this.residenceVisaNo = residenceVisaNo;
	}

	public String getSponserName() {
		return sponserName;
	}

	public void setSponserName(String sponserName) {
		this.sponserName = sponserName;
	}

	public String getEmploymentDesignation() {
		return employmentDesignation;
	}

	public void setEmploymentDesignation(String employmentDesignation) {
		this.employmentDesignation = employmentDesignation;
	}

	public String getSalaryIncome() {
		return salaryIncome;
	}

	public void setSalaryIncome(String salaryIncome) {
		this.salaryIncome = salaryIncome;
	}
}
