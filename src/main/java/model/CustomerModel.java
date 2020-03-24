package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerModel {

	private int custkey;
	private String listid="";
	private String name="";
	private String fullName="";
	private String companyName="";
	private String isactive="";
	private String billCountry="";
	private String billCity="";
	private String phone="";
	private String altphone="";
	private String fax="";
	private String email="";
	private String shipTo="";
	private String contact="";
	private String arName="";
	private Date createdDate;
	private int sublevel;
	private String listType="";
	private String parent="";
	private String photoPath="";
	private List<CustomerModel> subOfdropDown;
	private CustomerModel selectedSubOf;
	private String customerContactExpiryDateStr="";
	private String accountNumber="";
	private String balckListed="";
	private int priority;
	private String cC="";
	private List<CustomerContact> customerContacts = new ArrayList<CustomerContact>();
	private String statusDescription="";
	private Integer country;
	private Integer city;
	private String zipCode="";
	private Integer street;
	private String pobox="";
	private String printChequeAs="";
	private List<CustomerContact> contacts;
	private String altcontact="";
	private String website="";
	private String skypeId="";
	private String mobile="";
	private String note="";
	private Integer CompanyTypeRefKey;
	private Integer CompanySizeRefKey;
	private Integer SoftwareRefKey;
	private Integer userNos;
	private Integer empNos;
	private Integer workingHours;
	private String ownerName="";
	private String manageerName="";
	private String auditorName="";
	private String accountantName="";
	private Date lastTrialBalance;
	private int salesRepKey;
	private Integer paymentMethod;
	private Double creditLimit;
	private TenantModel tenant;
	
	private boolean to=false;
	private boolean cc=false;
	private boolean bcc=false;
	
	private boolean disable=false;//use for customer pop up in the other forms to disable the already selected. 
	//aslo i add this in model
	private boolean selected;
	
	private Double localBalance;
	private Integer howdidYouknowus;
	private String vatRegNo="";
	
	public int getCustkey() {
		return custkey;
	}

	public void setCustkey(int custkey) {
		this.custkey = custkey;
	}

	public String getListid() {
		return listid;
	}

	public void setListid(String listid) {
		this.listid = listid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getBillCountry() {
		return billCountry;
	}

	public void setBillCountry(String billCountry) {
		this.billCountry = billCountry;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAltphone() {
		return altphone;
	}

	public void setAltphone(String altphone) {
		this.altphone = altphone;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getArName() {
		return arName;
	}

	public void setArName(String arName) {
		this.arName = arName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getSublevel() {
		return sublevel;
	}

	public void setSublevel(int sublevel) {
		this.sublevel = sublevel;
	}

	/**
	 * @return the subOfdropDown
	 */
	public List<CustomerModel> getSubOfdropDown() {
		return subOfdropDown;
	}

	/**
	 * @param subOfdropDown
	 *            the subOfdropDown to set
	 */
	public void setSubOfdropDown(List<CustomerModel> subOfdropDown) {
		this.subOfdropDown = subOfdropDown;
	}

	/**
	 * @return the selectedSubOf
	 */
	public CustomerModel getSelectedSubOf() {
		return selectedSubOf;
	}

	/**
	 * @param selectedSubOf
	 *            the selectedSubOf to set
	 */
	public void setSelectedSubOf(CustomerModel selectedSubOf) {
		this.selectedSubOf = selectedSubOf;
	}

	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	/**
	 * @return the customerContactExpiryDateStr
	 */
	public String getCustomerContactExpiryDateStr() {
		return customerContactExpiryDateStr;
	}

	/**
	 * @param customerContactExpiryDateStr
	 *            the customerContactExpiryDateStr to set
	 */
	public void setCustomerContactExpiryDateStr(
			String customerContactExpiryDateStr) {
		this.customerContactExpiryDateStr = customerContactExpiryDateStr;
	}

	/**
	 * @return the salesRepKey
	 */
	public int getSalesRepKey() {
		return salesRepKey;
	}

	/**
	 * @param salesRepKey
	 *            the salesRepKey to set
	 */
	public void setSalesRepKey(int salesRepKey) {
		this.salesRepKey = salesRepKey;
	}

	/**
	 * @return the balckListed
	 */
	public String getBalckListed() {
		return balckListed;
	}

	/**
	 * @param balckListed
	 *            the balckListed to set
	 */
	public void setBalckListed(String balckListed) {
		this.balckListed = balckListed;
	}

	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * @param statusDescription
	 *            the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/**
	 * @return the customerContacts
	 */
	public List<CustomerContact> getCustomerContacts() {
		return customerContacts;
	}

	/**
	 * @param customerContacts
	 *            the customerContacts to set
	 */
	public void setCustomerContacts(List<CustomerContact> customerContacts) {
		this.customerContacts = customerContacts;
	}

	/**
	 * @return the cC
	 */
	public String getcC() {
		return cC;
	}

	/**
	 * @param cC
	 *            the cC to set
	 */
	public void setcC(String cC) {
		this.cC = cC;
	}

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Integer getStreet() {
		return street;
	}

	public void setStreet(Integer street) {
		this.street = street;
	}

	public String getPobox() {
		return pobox;
	}

	public void setPobox(String pobox) {
		this.pobox = pobox;
	}

	public List<CustomerContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<CustomerContact> contacts) {
		this.contacts = contacts;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPrintChequeAs() {
		return printChequeAs;
	}

	public void setPrintChequeAs(String printChequeAs) {
		this.printChequeAs = printChequeAs;
	}

	public String getAltcontact() {
		return altcontact;
	}

	public void setAltcontact(String altcontact) {
		this.altcontact = altcontact;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getCompanyTypeRefKey() {
		return CompanyTypeRefKey;
	}

	public void setCompanyTypeRefKey(Integer companyTypeRefKey) {
		CompanyTypeRefKey = companyTypeRefKey;
	}

	public Integer getCompanySizeRefKey() {
		return CompanySizeRefKey;
	}

	public void setCompanySizeRefKey(Integer companySizeRefKey) {
		CompanySizeRefKey = companySizeRefKey;
	}

	public Integer getSoftwareRefKey() {
		return SoftwareRefKey;
	}

	public void setSoftwareRefKey(Integer softwareRefKey) {
		SoftwareRefKey = softwareRefKey;
	}

	public Integer getUserNos() {
		return userNos;
	}

	public void setUserNos(Integer userNos) {
		this.userNos = userNos;
	}

	public Integer getEmpNos() {
		return empNos;
	}

	public void setEmpNos(Integer empNos) {
		this.empNos = empNos;
	}

	public Integer getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(Integer workingHours) {
		this.workingHours = workingHours;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getManageerName() {
		return manageerName;
	}

	public void setManageerName(String manageerName) {
		this.manageerName = manageerName;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAccountantName() {
		return accountantName;
	}

	public void setAccountantName(String accountantName) {
		this.accountantName = accountantName;
	}

	public Date getLastTrialBalance() {
		return lastTrialBalance;
	}

	public void setLastTrialBalance(Date lastTrialBalance) {
		this.lastTrialBalance = lastTrialBalance;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public TenantModel getTenant() {
		return tenant;
	}

	public void setTenant(TenantModel tenant) {
		this.tenant = tenant;
	}

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

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	/**
	 * @return the disable
	 */
	public boolean isDisable() {
		return disable;
	}

	/**
	 * @param disable the disable to set
	 */
	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Double getLocalBalance() {
		return localBalance;
	}

	public void setLocalBalance(Double localBalance) {
		this.localBalance = localBalance;
	}

	public String getBillCity() {
		return billCity;
	}

	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}

	public Integer getHowdidYouknowus() {
		return howdidYouknowus;
	}

	public void setHowdidYouknowus(Integer howdidYouknowus) {
		this.howdidYouknowus = howdidYouknowus;
	}

	public String getVatRegNo() {
		return vatRegNo;
	}

	public void setVatRegNo(String vatRegNo) {
		this.vatRegNo = vatRegNo;
	}
	
	

}
