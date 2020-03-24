package model;

import java.util.Date;

public class VendorModel 
{

	
	private int vend_Key;
	private Date timeCreated;
	private Date timeModified;
	private String name;
	private String arName;
	private String accountName;
	private String fullName;
	private String isActive;
	private String subLevel;
	private String companyName;
	private String salutation;
	private String firstName;
	private String middleName;
	private String lastName;
	private String billAddress1;
	private String phone;
	private String altPhone;
	private String altContact;
	private double balance;
	private double totalBalance;
	private String  accountNumber;
	private String bankName;
	private String branchName;
	private String actNumber;
	private String SkypeId;
	private String ibanNo;
	private String fax;
	private String email;
	private String webSite;
	private String cC;
	private String contact;
	private String printChequeAs;
	private String note;
	private String vatRegNo="";
	
	public int getVend_Key() {
		return vend_Key;
	}
	public void setVend_Key(int vend_Key) {
		this.vend_Key = vend_Key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArName() {
		return arName;
	}
	public void setArName(String arName) {
		this.arName = arName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Date getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
	public String getBillAddress1() {
		return billAddress1;
	}
	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public String getcC() {
		return cC;
	}
	public void setcC(String cC) {
		this.cC = cC;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPrintChequeAs() {
		return printChequeAs;
	}
	public void setPrintChequeAs(String printChequeAs) {
		this.printChequeAs = printChequeAs;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the timeModified
	 */
	public Date getTimeModified() {
		return timeModified;
	}
	/**
	 * @param timeModified the timeModified to set
	 */
	public void setTimeModified(Date timeModified) {
		this.timeModified = timeModified;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the subLevel
	 */
	public String getSubLevel() {
		return subLevel;
	}
	/**
	 * @param subLevel the subLevel to set
	 */
	public void setSubLevel(String subLevel) {
		this.subLevel = subLevel;
	}
	/**
	 * @return the salutation
	 */
	public String getSalutation() {
		return salutation;
	}
	/**
	 * @param salutation the salutation to set
	 */
	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the altPhone
	 */
	public String getAltPhone() {
		return altPhone;
	}
	/**
	 * @param altPhone the altPhone to set
	 */
	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
	}
	/**
	 * @return the altContact
	 */
	public String getAltContact() {
		return altContact;
	}
	/**
	 * @param altContact the altContact to set
	 */
	public void setAltContact(String altContact) {
		this.altContact = altContact;
	}
	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	/**
	 * @return the totalBalance
	 */
	public double getTotalBalance() {
		return totalBalance;
	}
	/**
	 * @param totalBalance the totalBalance to set
	 */
	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * @return the actNumber
	 */
	public String getActNumber() {
		return actNumber;
	}
	/**
	 * @param actNumber the actNumber to set
	 */
	public void setActNumber(String actNumber) {
		this.actNumber = actNumber;
	}
	/**
	 * @return the skypeId
	 */
	public String getSkypeId() {
		return SkypeId;
	}
	/**
	 * @param skypeId the skypeId to set
	 */
	public void setSkypeId(String skypeId) {
		SkypeId = skypeId;
	}
	/**
	 * @return the ibanNo
	 */
	public String getIbanNo() {
		return ibanNo;
	}
	/**
	 * @param ibanNo the ibanNo to set
	 */
	public void setIbanNo(String ibanNo) {
		this.ibanNo = ibanNo;
	}
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getVatRegNo() {
		return vatRegNo;
	}
	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}
	
	
	
	
}
