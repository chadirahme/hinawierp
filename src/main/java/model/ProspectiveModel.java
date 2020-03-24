package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProspectiveModel {
	private String result="";
	private String name="";
	private String arName="";
	private String created_date="";
	private String has_quotation="";
	private String category="";
	private String email="";
	private String website="";
	private String phone="";
	private String directPhone="";
	private String fax="";
	private String shipTo="";
	private String isActive="";
	private String custRefKey="";
	private String rowColor="";
	private String printChequeAs="";
	private boolean priority;
	private int RecNo;
	private Date createdDate;
	private int subLevel;
	private String fullname="";
	private String adress1="";
	private String adrress2="";
	private String altPhone="";
	private int softwareLicensKeys;
	private String other1="";
	private String other2="";
	private String other3="";
	private String other4="";
	private String other5="";
	private String companyName="";
	private String skypeId="";
	private String notes="";
	private int companyTYpe;
	private int comapnySize;
	private String buisenessType="";
	private String pobox="";
	private int currentHrsoftware;
	private String currentAccountingSoftwaare="";
	private String softwareLanguages="";
	private int totalComapnyEmployee;
	private int noUsers;
	private int recNumber;
	private String contactPerson="";
	private String altContactPerson="";
	private Integer country;
	private Integer city;
	private String zipCode="";
	private Integer street;
	private Integer howdidYouknowus;
	private String levelOfInterset="";
	private String cC="";
	private String sendTo="";
	private List<ProspectiveModel> subOfdropDown;
	private ProspectiveModel selectedSubOf;
	private Integer parent;
	private List<ProspectiveContactDetailsModel> prospectiveContact;
	private String photoPath="";
	private String photoExist="";
	
	private int CompanyTypeRefKey;
	private int CompanySizeRefKey;
	private int SoftwareRefKey;
	private Integer UserNos;
	private Integer EmpNos;
	private Integer workingHours;
	private String ownerName="";
	private String manageerName="";
	private String auditorName="";
	private String accountantName="";
	private Date lastTrialBalance;
	private Integer salesRepKey;
	private Double creditLimit;
	
	List<ProspectiveContactDetailsModel> prospectiveContacts=new ArrayList<ProspectiveContactDetailsModel>();
	
	private boolean selected=false;

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public ProspectiveModel getSelectedSubOf() {
		return selectedSubOf;
	}

	public void setSelectedSubOf(ProspectiveModel selectedSubOf) {
		this.selectedSubOf = selectedSubOf;
	}

	public List<ProspectiveModel> getSubOfdropDown() {
		return subOfdropDown;
	}

	public void setSubOfdropDown(List<ProspectiveModel> subOfdropDown) {
		this.subOfdropDown = subOfdropDown;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getcC() {
		return cC;
	}

	public void setcC(String cC) {
		this.cC = cC;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getLevelOfInterset() {
		return levelOfInterset;
	}

	public void setLevelOfInterset(String levelOfInterset) {
		this.levelOfInterset = levelOfInterset;
	}

	public Integer getHowdidYouknowus() {
		return howdidYouknowus;
	}

	public void setHowdidYouknowus(Integer howdidYouknowus) {
		this.howdidYouknowus = howdidYouknowus;
	}

	public Integer getStreet() {
		return street;
	}

	public void setStreet(Integer street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAltContactPerson() {
		return altContactPerson;
	}

	public void setAltContactPerson(String altContactPerson) {
		this.altContactPerson = altContactPerson;
	}

	public String getDirectPhone() {
		return directPhone;
	}

	public void setDirectPhone(String directPhone) {
		this.directPhone = directPhone;
	}

	public String getAltPhone() {
		return altPhone;
	}

	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public void setArName(String arabic_name) {
		this.arName = arabic_name;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getHas_quotation() {
		return has_quotation;
	}

	public void setHas_quotation(String has_quotation) {
		this.has_quotation = has_quotation;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String telephone) {
		this.phone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCustRefKey() {
		return custRefKey;
	}

	public void setCustRefKey(String custRefKey) {
		this.custRefKey = custRefKey;
	}

	public String getRowColor() {
		return rowColor;
	}

	public void setRowColor(String rowColor) {
		this.rowColor = rowColor;
	}

	public String getPrintChequeAs() {
		return printChequeAs;
	}

	public void setPrintChequeAs(String PrintChequeAs) {
		this.printChequeAs = PrintChequeAs;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public int getRecNo() {
		return RecNo;
	}

	public void setRecNo(int recNo) {
		RecNo = recNo;
	}

	public int getSubLevel() {
		return subLevel;
	}

	public void setSubLevel(int subLevel) {
		this.subLevel = subLevel;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAdress1() {
		return adress1;
	}

	public void setAdress1(String adress1) {
		this.adress1 = adress1;
	}

	public String getAdrress2() {
		return adrress2;
	}

	public void setAdrress2(String adrress2) {
		this.adrress2 = adrress2;
	}

	public int getSoftwareLicensKeys() {
		return softwareLicensKeys;
	}

	public void setSoftwareLicensKeys(int softwareLicensKeys) {
		this.softwareLicensKeys = softwareLicensKeys;
	}

	public String getOther1() {
		return other1;
	}

	public void setOther1(String other1) {
		this.other1 = other1;
	}

	public String getOther2() {
		return other2;
	}

	public void setOther2(String other2) {
		this.other2 = other2;
	}

	public String getOther3() {
		return other3;
	}

	public void setOther3(String other3) {
		this.other3 = other3;
	}

	public String getOther4() {
		return other4;
	}

	public void setOther4(String other4) {
		this.other4 = other4;
	}

	public String getOther5() {
		return other5;
	}

	public void setOther5(String other5) {
		this.other5 = other5;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getCompanyTYpe() {
		return companyTYpe;
	}

	public void setCompanyTYpe(int companyTYpe) {
		this.companyTYpe = companyTYpe;
	}

	public int getComapnySize() {
		return comapnySize;
	}

	public void setComapnySize(int comapnySize) {
		this.comapnySize = comapnySize;
	}

	public String getBuisenessType() {
		return buisenessType;
	}

	public void setBuisenessType(String buisenessType) {
		this.buisenessType = buisenessType;
	}

	public String getPobox() {
		return pobox;
	}

	public void setPobox(String pobox) {
		this.pobox = pobox;
	}

	public int getCurrentHrsoftware() {
		return currentHrsoftware;
	}

	public void setCurrentHrsoftware(int currentHrsoftware) {
		this.currentHrsoftware = currentHrsoftware;
	}

	public String getCurrentAccountingSoftwaare() {
		return currentAccountingSoftwaare;
	}

	public void setCurrentAccountingSoftwaare(String currentAccountingSoftwaare) {
		this.currentAccountingSoftwaare = currentAccountingSoftwaare;
	}

	public String getSoftwareLanguages() {
		return softwareLanguages;
	}

	public void setSoftwareLanguages(String softwareLanguages) {
		this.softwareLanguages = softwareLanguages;
	}

	public int getTotalComapnyEmployee() {
		return totalComapnyEmployee;
	}

	public void setTotalComapnyEmployee(int totalComapnyEmployee) {
		this.totalComapnyEmployee = totalComapnyEmployee;
	}

	public int getRecNumber() {
		return recNumber;
	}

	public void setRecNumber(int recNumber) {
		this.recNumber = recNumber;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public List<ProspectiveContactDetailsModel> getProspectiveContact() {
		return prospectiveContact;
	}

	public void setProspectiveContact(
			List<ProspectiveContactDetailsModel> prospectiveContact) {
		this.prospectiveContact = prospectiveContact;
	}

	public int getNoUsers() {
		return noUsers;
	}

	public void setNoUsers(int noUsers) {
		this.noUsers = noUsers;
	}

	public int getCompanyTypeRefKey() {
		return CompanyTypeRefKey;
	}

	public void setCompanyTypeRefKey(int companyTypeRefKey) {
		CompanyTypeRefKey = companyTypeRefKey;
	}

	public int getCompanySizeRefKey() {
		return CompanySizeRefKey;
	}

	public void setCompanySizeRefKey(int companySizeRefKey) {
		CompanySizeRefKey = companySizeRefKey;
	}

	public int getSoftwareRefKey() {
		return SoftwareRefKey;
	}

	public void setSoftwareRefKey(int softwareRefKey) {
		SoftwareRefKey = softwareRefKey;
	}

	public Integer getUserNos() {
		return UserNos;
	}

	public void setUserNos(Integer userNos) {
		UserNos = userNos;
	}

	public Integer getEmpNos() {
		return EmpNos;
	}

	public void setEmpNos(Integer empNos) {
		EmpNos = empNos;
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

	public Integer getSalesRepKey() {
		return salesRepKey;
	}

	public void setSalesRepKey(Integer salesRepKey) {
		this.salesRepKey = salesRepKey;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getPhotoExist() {
		return photoExist;
	}

	public void setPhotoExist(String photoExist) {
		this.photoExist = photoExist;
	}
	
	
	

}
