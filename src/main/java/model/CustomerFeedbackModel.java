package model;

import home.QuotationAttachmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomerFeedbackModel {
	
	
	int feedbackKey;
	
	int cityKey;
	
	int countryKey;
	
	int positionKey;
	
	int feedBackTypeKey;
	
	int customerInitailKey;
	
	int customerRefKey;
	
	private String createdDateStr;
	
	private String modifeldDateStr;
	
	
	private String customerType;
	
	private String emailType;
	
	private String address;
	
	private List<HRListValuesModel> lstPosition;
	private HRListValuesModel selectedPosition;
	
	private List<HRListValuesModel> lstCountry;
	private HRListValuesModel selectedCountry;
	
	private List<HRListValuesModel> lstCity;
	private HRListValuesModel selectedCity;
	
	private List<HRListValuesModel> lstFeedBackType;
	private List<HRListValuesModel> selectedFeedBackType;
	
	private List<String> lstSoftwareType;
	private String selectedSoftwareType;
	
	private List<HRListValuesModel> lstContactPersonIntial;
	private HRListValuesModel selectedContactPersonIntial;
	
	private String attFile4;
	
	
	private List<QuotationAttachmentModel> lstAtt;
	
	private QuotationAttachmentModel selectedAttchemnets;
	
	
	private String companyName;
	
	private String contactPersonName;
	
	private String mobileAreaCode1;
	
	private String mobileAreaCode2;
	
	private String mobile1;
	
	private String mobile2;
	
	
	private String telphoneAreaCode1;
	
	private String telphoneAreaCode2;
	
	private String telphone1;
	
	private String telphone2;
	
	private String email;
	
	private String website;
	
	private String memo;
	
	private String instruction;
	
	private Date feedbackCreateDate;
	
	private Date feedBackModifiedDate;
	
	private int webuserID;
	
	private String feedbackNUmber;
	
	private String attchemnetPath="";
	
	private String fileName="";
	
	private String intialName;
	
	private String feedbackTypeName;
	
	private String customerName;
	
	Set<String> selectedTo=new HashSet<String>();
	
	Set<String> selectedCcs=new HashSet<String>();
	
	Set<String> selectedBcc=new HashSet<String>();
	
	private String subject;
	
	
	//Notes For each Module form 
	
	
	private String memoEn="";
	
	private String memoAr="";
	
	private int serviceRefKey;
	
	private int noteID;
	
	private int taskID;
	
	private int taskStatusId;
	
	private int userId;
	
	private String taskName;
	
	private String taskStatus;
	
	private String taskNo;
	
	private String sentFromEmail;
	
	private String isDrafted="N";
	
	private String isScheduled="N";
	
	private String isInbox="N";
	
	private String isSent="N";
	
	private List<CustomerFeedbackModel> listOfSentEmails=new ArrayList<CustomerFeedbackModel>(); 
	
		
	private List<TaskAndFeeddbackRelation> taskRelationlist=new ArrayList<TaskAndFeeddbackRelation>();
	
	private boolean enabaleReminder=false;
	
	private int schedulerId;
	
	private String sourceType;
	
	private int soourceId;
	
	private String serviceName;
	
	private Set<FeedbackSendSources> sourcesLIst=new  HashSet<FeedbackSendSources>();
	
	
	/**
	 * @return the lstPosition
	 */
	public List<HRListValuesModel> getLstPosition() {
		return lstPosition;
	}

	/**
	 * @param lstPosition the lstPosition to set
	 */
	public void setLstPosition(List<HRListValuesModel> lstPosition) {
		this.lstPosition = lstPosition;
	}

	/**
	 * @return the selectedPosition
	 */
	public HRListValuesModel getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * @param selectedPosition the selectedPosition to set
	 */
	public void setSelectedPosition(HRListValuesModel selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	/**
	 * @return the lstCountry
	 */
	public List<HRListValuesModel> getLstCountry() {
		return lstCountry;
	}

	/**
	 * @param lstCountry the lstCountry to set
	 */
	public void setLstCountry(List<HRListValuesModel> lstCountry) {
		this.lstCountry = lstCountry;
	}

	/**
	 * @return the selectedCountry
	 */
	public HRListValuesModel getSelectedCountry() {
		return selectedCountry;
	}

	/**
	 * @param selectedCountry the selectedCountry to set
	 */
	public void setSelectedCountry(HRListValuesModel selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	/**
	 * @return the lstCity
	 */
	public List<HRListValuesModel> getLstCity() {
		return lstCity;
	}

	/**
	 * @param lstCity the lstCity to set
	 */
	public void setLstCity(List<HRListValuesModel> lstCity) {
		this.lstCity = lstCity;
	}

	/**
	 * @return the selectedCity
	 */
	public HRListValuesModel getSelectedCity() {
		return selectedCity;
	}

	/**
	 * @param selectedCity the selectedCity to set
	 */
	public void setSelectedCity(HRListValuesModel selectedCity) {
		this.selectedCity = selectedCity;
	}

	/**
	 * @return the lstFeedBackType
	 */
	public List<HRListValuesModel> getLstFeedBackType() {
		return lstFeedBackType;
	}

	/**
	 * @param lstFeedBackType the lstFeedBackType to set
	 */
	public void setLstFeedBackType(List<HRListValuesModel> lstFeedBackType) {
		this.lstFeedBackType = lstFeedBackType;
	}

	/**
	 * @return the selectedFeedBackType
	 */
	public List<HRListValuesModel> getSelectedFeedBackType() {
		return selectedFeedBackType;
	}

	/**
	 * @param selectedFeedBackType the selectedFeedBackType to set
	 */
	public void setSelectedFeedBackType(List<HRListValuesModel> selectedFeedBackType) {
		this.selectedFeedBackType = selectedFeedBackType;
	}

	/**
	 * @return the lstSoftwareType
	 */
	public List<String> getLstSoftwareType() {
		return lstSoftwareType;
	}

	/**
	 * @param lstSoftwareType the lstSoftwareType to set
	 */
	public void setLstSoftwareType(List<String> lstSoftwareType) {
		this.lstSoftwareType = lstSoftwareType;
	}

	/**
	 * @return the selectedSoftwareType
	 */
	public String getSelectedSoftwareType() {
		return selectedSoftwareType;
	}

	/**
	 * @param selectedSoftwareType the selectedSoftwareType to set
	 */
	public void setSelectedSoftwareType(String selectedSoftwareType) {
		this.selectedSoftwareType = selectedSoftwareType;
	}

	/**
	 * @return the lstContactPersonIntial
	 */
	public List<HRListValuesModel> getLstContactPersonIntial() {
		return lstContactPersonIntial;
	}

	/**
	 * @param lstContactPersonIntial the lstContactPersonIntial to set
	 */
	public void setLstContactPersonIntial(
			List<HRListValuesModel> lstContactPersonIntial) {
		this.lstContactPersonIntial = lstContactPersonIntial;
	}

	/**
	 * @return the selectedContactPersonIntial
	 */
	public HRListValuesModel getSelectedContactPersonIntial() {
		return selectedContactPersonIntial;
	}

	/**
	 * @param selectedContactPersonIntial the selectedContactPersonIntial to set
	 */
	public void setSelectedContactPersonIntial(
			HRListValuesModel selectedContactPersonIntial) {
		this.selectedContactPersonIntial = selectedContactPersonIntial;
	}

	/**
	 * @return the attFile4
	 */
	public String getAttFile4() {
		return attFile4;
	}

	/**
	 * @param attFile4 the attFile4 to set
	 */
	public void setAttFile4(String attFile4) {
		this.attFile4 = attFile4;
	}

	/**
	 * @return the lstAtt
	 */
	public List<QuotationAttachmentModel> getLstAtt() {
		return lstAtt;
	}

	/**
	 * @param lstAtt the lstAtt to set
	 */
	public void setLstAtt(List<QuotationAttachmentModel> lstAtt) {
		this.lstAtt = lstAtt;
	}

	/**
	 * @return the selectedAttchemnets
	 */
	public QuotationAttachmentModel getSelectedAttchemnets() {
		return selectedAttchemnets;
	}

	/**
	 * @param selectedAttchemnets the selectedAttchemnets to set
	 */
	public void setSelectedAttchemnets(QuotationAttachmentModel selectedAttchemnets) {
		this.selectedAttchemnets = selectedAttchemnets;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the contactPersonName
	 */
	public String getContactPersonName() {
		return contactPersonName;
	}

	/**
	 * @param contactPersonName the contactPersonName to set
	 */
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	/**
	 * @return the mobileAreaCode1
	 */
	public String getMobileAreaCode1() {
		return mobileAreaCode1;
	}

	/**
	 * @param mobileAreaCode1 the mobileAreaCode1 to set
	 */
	public void setMobileAreaCode1(String mobileAreaCode1) {
		this.mobileAreaCode1 = mobileAreaCode1;
	}

	/**
	 * @return the mobileAreaCode2
	 */
	public String getMobileAreaCode2() {
		return mobileAreaCode2;
	}

	/**
	 * @param mobileAreaCode2 the mobileAreaCode2 to set
	 */
	public void setMobileAreaCode2(String mobileAreaCode2) {
		this.mobileAreaCode2 = mobileAreaCode2;
	}

	/**
	 * @return the mobile1
	 */
	public String getMobile1() {
		return mobile1;
	}

	/**
	 * @param mobile1 the mobile1 to set
	 */
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	/**
	 * @return the mobile2
	 */
	public String getMobile2() {
		return mobile2;
	}

	/**
	 * @param mobile2 the mobile2 to set
	 */
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	/**
	 * @return the telphoneAreaCode1
	 */
	public String getTelphoneAreaCode1() {
		return telphoneAreaCode1;
	}

	/**
	 * @param telphoneAreaCode1 the telphoneAreaCode1 to set
	 */
	public void setTelphoneAreaCode1(String telphoneAreaCode1) {
		this.telphoneAreaCode1 = telphoneAreaCode1;
	}

	/**
	 * @return the telphoneAreaCode2
	 */
	public String getTelphoneAreaCode2() {
		return telphoneAreaCode2;
	}

	/**
	 * @param telphoneAreaCode2 the telphoneAreaCode2 to set
	 */
	public void setTelphoneAreaCode2(String telphoneAreaCode2) {
		this.telphoneAreaCode2 = telphoneAreaCode2;
	}

	/**
	 * @return the telphone1
	 */
	public String getTelphone1() {
		return telphone1;
	}

	/**
	 * @param telphone1 the telphone1 to set
	 */
	public void setTelphone1(String telphone1) {
		this.telphone1 = telphone1;
	}

	/**
	 * @return the telphone2
	 */
	public String getTelphone2() {
		return telphone2;
	}

	/**
	 * @param telphone2 the telphone2 to set
	 */
	public void setTelphone2(String telphone2) {
		this.telphone2 = telphone2;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the instruction
	 */
	public String getInstruction() {
		return instruction;
	}

	/**
	 * @param instruction the instruction to set
	 */
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	/**
	 * @return the feedbackCreateDate
	 */
	public Date getFeedbackCreateDate() {
		return feedbackCreateDate;
	}

	/**
	 * @param feedbackCreateDate the feedbackCreateDate to set
	 */
	public void setFeedbackCreateDate(Date feedbackCreateDate) {
		this.feedbackCreateDate = feedbackCreateDate;
	}

	/**
	 * @return the feedBackModifiedDate
	 */
	public Date getFeedBackModifiedDate() {
		return feedBackModifiedDate;
	}

	/**
	 * @param feedBackModifiedDate the feedBackModifiedDate to set
	 */
	public void setFeedBackModifiedDate(Date feedBackModifiedDate) {
		this.feedBackModifiedDate = feedBackModifiedDate;
	}

	/**
	 * @return the webuserID
	 */
	public int getWebuserID() {
		return webuserID;
	}

	/**
	 * @param webuserID the webuserID to set
	 */
	public void setWebuserID(int webuserID) {
		this.webuserID = webuserID;
	}

	/**
	 * @return the feedbackNUmber
	 */
	public String getFeedbackNUmber() {
		return feedbackNUmber;
	}

	/**
	 * @param feedbackNUmber the feedbackNUmber to set
	 */
	public void setFeedbackNUmber(String feedbackNUmber) {
		this.feedbackNUmber = feedbackNUmber;
	}

	/**
	 * @return the feedbackKey
	 */
	public int getFeedbackKey() {
		return feedbackKey;
	}

	/**
	 * @param feedbackKey the feedbackKey to set
	 */
	public void setFeedbackKey(int feedbackKey) {
		this.feedbackKey = feedbackKey;
	}



	/**
	 * @return the customerType
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the cityKey
	 */
	public int getCityKey() {
		return cityKey;
	}

	/**
	 * @param cityKey the cityKey to set
	 */
	public void setCityKey(int cityKey) {
		this.cityKey = cityKey;
	}

	/**
	 * @return the countryKey
	 */
	public int getCountryKey() {
		return countryKey;
	}

	/**
	 * @param countryKey the countryKey to set
	 */
	public void setCountryKey(int countryKey) {
		this.countryKey = countryKey;
	}

	/**
	 * @return the positionKey
	 */
	public int getPositionKey() {
		return positionKey;
	}

	/**
	 * @param positionKey the positionKey to set
	 */
	public void setPositionKey(int positionKey) {
		this.positionKey = positionKey;
	}

	/**
	 * @return the feedBackTypeKey
	 */
	public int getFeedBackTypeKey() {
		return feedBackTypeKey;
	}

	/**
	 * @param feedBackTypeKey the feedBackTypeKey to set
	 */
	public void setFeedBackTypeKey(int feedBackTypeKey) {
		this.feedBackTypeKey = feedBackTypeKey;
	}

	/**
	 * @return the customerInitailKey
	 */
	public int getCustomerInitailKey() {
		return customerInitailKey;
	}

	/**
	 * @param customerInitailKey the customerInitailKey to set
	 */
	public void setCustomerInitailKey(int customerInitailKey) {
		this.customerInitailKey = customerInitailKey;
	}

	/**
	 * @return the customerRefKey
	 */
	public int getCustomerRefKey() {
		return customerRefKey;
	}

	/**
	 * @param customerRefKey the customerRefKey to set
	 */
	public void setCustomerRefKey(int customerRefKey) {
		this.customerRefKey = customerRefKey;
	}

	/**
	 * @return the attchemnetPath
	 */
	public String getAttchemnetPath() {
		return attchemnetPath;
	}

	/**
	 * @param attchemnetPath the attchemnetPath to set
	 */
	public void setAttchemnetPath(String attchemnetPath) {
		this.attchemnetPath = attchemnetPath;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the createdDateStr
	 */
	public String getCreatedDateStr() {
		return createdDateStr;
	}

	/**
	 * @param createdDateStr the createdDateStr to set
	 */
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	/**
	 * @return the modifeldDateStr
	 */
	public String getModifeldDateStr() {
		return modifeldDateStr;
	}

	/**
	 * @param modifeldDateStr the modifeldDateStr to set
	 */
	public void setModifeldDateStr(String modifeldDateStr) {
		this.modifeldDateStr = modifeldDateStr;
	}

	/**
	 * @return the intialName
	 */
	public String getIntialName() {
		return intialName;
	}

	/**
	 * @param intialName the intialName to set
	 */
	public void setIntialName(String intialName) {
		this.intialName = intialName;
	}

	/**
	 * @return the feedbackTypeName
	 */
	public String getFeedbackTypeName() {
		return feedbackTypeName;
	}

	/**
	 * @param feedbackTypeName the feedbackTypeName to set
	 */
	public void setFeedbackTypeName(String feedbackTypeName) {
		this.feedbackTypeName = feedbackTypeName;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the selectedTo
	 */
	public Set<String> getSelectedTo() {
		return selectedTo;
	}

	/**
	 * @param selectedTo the selectedTo to set
	 */
	public void setSelectedTo(Set<String> selectedTo) {
		this.selectedTo = selectedTo;
	}

	/**
	 * @return the selectedCcs
	 */
	public Set<String> getSelectedCcs() {
		return selectedCcs;
	}

	/**
	 * @param selectedCcs the selectedCcs to set
	 */
	public void setSelectedCcs(Set<String> selectedCcs) {
		this.selectedCcs = selectedCcs;
	}

	/**
	 * @return the selectedBcc
	 */
	public Set<String> getSelectedBcc() {
		return selectedBcc;
	}

	/**
	 * @param selectedBcc the selectedBcc to set
	 */
	public void setSelectedBcc(Set<String> selectedBcc) {
		this.selectedBcc = selectedBcc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the memoEn
	 */
	public String getMemoEn() {
		return memoEn;
	}

	/**
	 * @param memoEn the memoEn to set
	 */
	public void setMemoEn(String memoEn) {
		this.memoEn = memoEn;
	}

	/**
	 * @return the memoAr
	 */
	public String getMemoAr() {
		return memoAr;
	}

	/**
	 * @param memoAr the memoAr to set
	 */
	public void setMemoAr(String memoAr) {
		this.memoAr = memoAr;
	}

	/**
	 * @return the serviceRefKey
	 */
	public int getServiceRefKey() {
		return serviceRefKey;
	}

	/**
	 * @param serviceRefKey the serviceRefKey to set
	 */
	public void setServiceRefKey(int serviceRefKey) {
		this.serviceRefKey = serviceRefKey;
	}

	/**
	 * @return the noteID
	 */
	public int getNoteID() {
		return noteID;
	}

	/**
	 * @param noteID the noteID to set
	 */
	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	/**
	 * @return the taskID
	 */
	public int getTaskID() {
		return taskID;
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	/**
	 * @return the taskStatusId
	 */
	public int getTaskStatusId() {
		return taskStatusId;
	}

	/**
	 * @param taskStatusId the taskStatusId to set
	 */
	public void setTaskStatusId(int taskStatusId) {
		this.taskStatusId = taskStatusId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the taskStatus
	 */
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the taskNo
	 */
	public String getTaskNo() {
		return taskNo;
	}

	/**
	 * @param taskNo the taskNo to set
	 */
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	/**
	 * @return the taskRelationlist
	 */
	public List<TaskAndFeeddbackRelation> getTaskRelationlist() {
		return taskRelationlist;
	}

	/**
	 * @param taskRelationlist the taskRelationlist to set
	 */
	public void setTaskRelationlist(List<TaskAndFeeddbackRelation> taskRelationlist) {
		this.taskRelationlist = taskRelationlist;
	}

	/**
	 * @return the emailType
	 */
	public String getEmailType() {
		return emailType;
	}

	/**
	 * @param emailType the emailType to set
	 */
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	/**
	 * @return the sentFromEmail
	 */
	public String getSentFromEmail() {
		return sentFromEmail;
	}

	/**
	 * @param sentFromEmail the sentFromEmail to set
	 */
	public void setSentFromEmail(String sentFromEmail) {
		this.sentFromEmail = sentFromEmail;
	}

	/**
	 * @return the isDrafted
	 */
	public String getIsDrafted() {
		return isDrafted;
	}

	/**
	 * @param isDrafted the isDrafted to set
	 */
	public void setIsDrafted(String isDrafted) {
		this.isDrafted = isDrafted;
	}

	/**
	 * @return the isScheduled
	 */
	public String getIsScheduled() {
		return isScheduled;
	}

	/**
	 * @param isScheduled the isScheduled to set
	 */
	public void setIsScheduled(String isScheduled) {
		this.isScheduled = isScheduled;
	}

	/**
	 * @return the isInbox
	 */
	public String getIsInbox() {
		return isInbox;
	}

	/**
	 * @param isInbox the isInbox to set
	 */
	public void setIsInbox(String isInbox) {
		this.isInbox = isInbox;
	}

	/**
	 * @return the isSent
	 */
	public String getIsSent() {
		return isSent;
	}

	/**
	 * @param isSent the isSent to set
	 */
	public void setIsSent(String isSent) {
		this.isSent = isSent;
	}

	/**
	 * @return the listOfSentEmails
	 */
	public List<CustomerFeedbackModel> getListOfSentEmails() {
		return listOfSentEmails;
	}

	/**
	 * @param listOfSentEmails the listOfSentEmails to set
	 */
	public void setListOfSentEmails(List<CustomerFeedbackModel> listOfSentEmails) {
		this.listOfSentEmails = listOfSentEmails;
	}

	/**
	 * @return the enabaleReminder
	 */
	public boolean isEnabaleReminder() {
		return enabaleReminder;
	}

	/**
	 * @param enabaleReminder the enabaleReminder to set
	 */
	public void setEnabaleReminder(boolean enabaleReminder) {
		this.enabaleReminder = enabaleReminder;
	}

	/**
	 * @return the schedulerId
	 */
	public int getSchedulerId() {
		return schedulerId;
	}

	/**
	 * @param schedulerId the schedulerId to set
	 */
	public void setSchedulerId(int schedulerId) {
		this.schedulerId = schedulerId;
	}

	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the soourceId
	 */
	public int getSoourceId() {
		return soourceId;
	}

	/**
	 * @param soourceId the soourceId to set
	 */
	public void setSoourceId(int soourceId) {
		this.soourceId = soourceId;
	}

	/**
	 * @return the sourcesLIst
	 */
	public Set<FeedbackSendSources> getSourcesLIst() {
		return sourcesLIst;
	}

	/**
	 * @param sourcesLIst the sourcesLIst to set
	 */
	public void setSourcesLIst(Set<FeedbackSendSources> sourcesLIst) {
		this.sourcesLIst = sourcesLIst;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	
	

}
