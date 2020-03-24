package admin;

import home.QuotationAttachmentModel;
import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.EmployeeModel;
import model.HRListValuesModel;
import model.ProjectModel;
import model.QbListsModel;

public class TasksModel 
{

	private int taskid;
	
	private int employeeid;
	
	private String employeeName;
	
	private String description;
	
	private int priority;
	
	private String status;
	
	private Date openDate; 
	
	private Date closeDate;
	
	private Date creationDate;
	
	private String creationDateStr;
	
	private String taskNumber;
	
	private double estimatatedNumber;
	
	private double actualNumber;
	
	private String createdUserName;
	
	private int createdUserID;
	
	private int taskTypeId;
	
	private String taskType;
	
	private String taskStep;
	
	private int prviousTaskLinkId;
	
	private String previossTaskName;
	
	private int customerRefKey;
	
	private String customerNAme;
	
	private int projectKey;
	
	private String projectName;
	
	private int sreviceId;
	
	private String serviceName;
	
	private int CcEmployeeKey;
	
	private String ccEmployeeName;
	
	private int priorityRefKey;
	
	private String priorityNAme;
	
	private String memo;
	
	private String comments;
	
	private int statusKey;
	
	private String statusName;
	
	private String attchemnetPath;
	
	private EmployeeModel employee;
	
	private String taskName;
	
	private String history;
	
	private String hoursOrDays;
	
	private int webUserID;
	
	private Date updatedTime;
	
	private String fileName;
	
	private String mostRecentUpdate="";
	
	private List<QuotationAttachmentModel> listOfattchments=new ArrayList<QuotationAttachmentModel>();
	
	private QuotationAttachmentModel selectedAttachements;
	
	
	private String expectedDatetofinishStr;
	
	private Date expectedDatetofinish;
	
	private double remindIn;
	
	
	
	private CompanyModel selectedCompany;
	
	
	private HRListValuesModel selectedTaskType;
	
	private HRListValuesModel selectedTaskStatus;
	
	private HRListValuesModel selectedTaskPriority; 
	
	private ProjectModel lstSelectedProject;
	
	private QbListsModel lstSelectedCustomerJob;
	
	private HRListValuesModel lstSelectedService;
	
	private String selectedHoursOrDays;
	
	private EmployeeModel selectedAssignToEmployee;
	
	private EmployeeModel selectedCCToEmployee;
	
	private TasksModel selectedLinkToPreviousTask;
	
	private QuotationAttachmentModel selectedAttchemnets;
	
	private String selectedClientType="C";
	
	private int serialNumber=0;
	
	private String clientType;
	
	private String clientTypeFullName;
	
	private Date reminderDate;
	
	private String reminderDateStr;
	
	private String email="";
	
	private String customerNamefromFeedback;
	
	private String createdAutommaticTask="N";
	
	private int feedbackKey=0;
	
	private String feedbackNo;
	
	private boolean hideFeedbackButton;
	
	private String customerExpiryDate;
	
	private String customerAddress;
	
	private String prospectiveAdress;
	
	
	public int getTaskid() {
		return taskid;
	}
	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}
	public int getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public EmployeeModel getEmployee() {
		return employee;
	}
	public void setEmployee(EmployeeModel employee) {
		this.employee = employee;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedUserName() {
		return createdUserName;
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	public int getCreatedUserID() {
		return createdUserID;
	}
	public void setCreatedUserID(int createdUserID) {
		this.createdUserID = createdUserID;
	}
	public int getTaskTypeId() {
		return taskTypeId;
	}
	public void setTaskTypeId(int taskTypeId) {
		this.taskTypeId = taskTypeId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskStep() {
		return taskStep;
	}
	public void setTaskStep(String taskStep) {
		this.taskStep = taskStep;
	}
	public int getPrviousTaskLinkId() {
		return prviousTaskLinkId;
	}
	public void setPrviousTaskLinkId(int prviousTaskLinkId) {
		this.prviousTaskLinkId = prviousTaskLinkId;
	}
	public String getPreviossTaskName() {
		return previossTaskName;
	}
	public void setPreviossTaskName(String previossTaskName) {
		this.previossTaskName = previossTaskName;
	}
	public int getCustomerRefKey() {
		return customerRefKey;
	}
	public void setCustomerRefKey(int customerRefKey) {
		this.customerRefKey = customerRefKey;
	}
	public String getCustomerNAme() {
		return customerNAme;
	}
	public void setCustomerNAme(String customerNAme) {
		this.customerNAme = customerNAme;
	}
	public int getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(int projectKey) {
		this.projectKey = projectKey;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getSreviceId() {
		return sreviceId;
	}
	public void setSreviceId(int sreviceId) {
		this.sreviceId = sreviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public int getCcEmployeeKey() {
		return CcEmployeeKey;
	}
	public void setCcEmployeeKey(int ccEmployeeKey) {
		CcEmployeeKey = ccEmployeeKey;
	}
	public int getPriorityRefKey() {
		return priorityRefKey;
	}
	public void setPriorityRefKey(int priorityRefKey) {
		this.priorityRefKey = priorityRefKey;
	}
	public String getPriorityNAme() {
		return priorityNAme;
	}
	public void setPriorityNAme(String priorityNAme) {
		this.priorityNAme = priorityNAme;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getStatusKey() {
		return statusKey;
	}
	public void setStatusKey(int statusKey) {
		this.statusKey = statusKey;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getAttchemnetPath() {
		return attchemnetPath;
	}
	public void setAttchemnetPath(String attchemnetPath) {
		this.attchemnetPath = attchemnetPath;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public double getEstimatatedNumber() {
		return estimatatedNumber;
	}
	public void setEstimatatedNumber(double estimatatedNumber) {
		this.estimatatedNumber = estimatatedNumber;
	}
	public double getActualNumber() {
		return actualNumber;
	}
	public void setActualNumber(double actualNumber) {
		this.actualNumber = actualNumber;
	}
	public String getTaskNumber() {
		return taskNumber;
	}
	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}
	public String getCreationDateStr() {
		return creationDateStr;
	}
	public void setCreationDateStr(String creationDateStr) {
		this.creationDateStr = creationDateStr;
	}
	public String getHoursOrDays() {
		return hoursOrDays;
	}
	public void setHoursOrDays(String hoursOrDays) {
		this.hoursOrDays = hoursOrDays;
	}
	public int getWebUserID() {
		return webUserID;
	}
	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCcEmployeeName() {
		return ccEmployeeName;
	}
	public void setCcEmployeeName(String ccEmployeeName) {
		this.ccEmployeeName = ccEmployeeName;
	}
	public String getMostRecentUpdate() {
		return mostRecentUpdate;
	}
	public void setMostRecentUpdate(String mostRecentUpdate) {
		this.mostRecentUpdate = mostRecentUpdate;
	}
	public List<QuotationAttachmentModel> getListOfattchments() {
		return listOfattchments;
	}
	public void setListOfattchments(List<QuotationAttachmentModel> listOfattchments) {
		this.listOfattchments = listOfattchments;
	}
	public QuotationAttachmentModel getSelectedAttachements() {
		return selectedAttachements;
	}
	public void setSelectedAttachements(
			QuotationAttachmentModel selectedAttachements) {
		this.selectedAttachements = selectedAttachements;
	}
	public String getExpectedDatetofinishStr() {
		return expectedDatetofinishStr;
	}
	public void setExpectedDatetofinishStr(String expectedDatetofinishStr) {
		this.expectedDatetofinishStr = expectedDatetofinishStr;
	}
	public Date getExpectedDatetofinish() {
		return expectedDatetofinish;
	}
	public void setExpectedDatetofinish(Date expectedDatetofinish) {
		this.expectedDatetofinish = expectedDatetofinish;
	}
	public double getRemindIn() {
		return remindIn;
	}
	public void setRemindIn(double remindIn) {
		this.remindIn = remindIn;
	}
	/**
	 * @return the selectedCompany
	 */
	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}
	/**
	 * @param selectedCompany the selectedCompany to set
	 */
	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
	}
	/**
	 * @return the selectedTaskType
	 */
	public HRListValuesModel getSelectedTaskType() {
		return selectedTaskType;
	}
	/**
	 * @param selectedTaskType the selectedTaskType to set
	 */
	public void setSelectedTaskType(HRListValuesModel selectedTaskType) {
		this.selectedTaskType = selectedTaskType;
	}
	/**
	 * @return the selectedTaskStatus
	 */
	public HRListValuesModel getSelectedTaskStatus() {
		return selectedTaskStatus;
	}
	/**
	 * @param selectedTaskStatus the selectedTaskStatus to set
	 */
	public void setSelectedTaskStatus(HRListValuesModel selectedTaskStatus) {
		this.selectedTaskStatus = selectedTaskStatus;
	}
	/**
	 * @return the selectedTaskPriority
	 */
	public HRListValuesModel getSelectedTaskPriority() {
		return selectedTaskPriority;
	}
	/**
	 * @param selectedTaskPriority the selectedTaskPriority to set
	 */
	public void setSelectedTaskPriority(HRListValuesModel selectedTaskPriority) {
		this.selectedTaskPriority = selectedTaskPriority;
	}
	/**
	 * @return the lstSelectedProject
	 */
	public ProjectModel getLstSelectedProject() {
		return lstSelectedProject;
	}
	/**
	 * @param lstSelectedProject the lstSelectedProject to set
	 */
	public void setLstSelectedProject(ProjectModel lstSelectedProject) {
		this.lstSelectedProject = lstSelectedProject;
	}
	/**
	 * @return the lstSelectedCustomerJob
	 */
	public QbListsModel getLstSelectedCustomerJob() {
		return lstSelectedCustomerJob;
	}
	/**
	 * @param lstSelectedCustomerJob the lstSelectedCustomerJob to set
	 */
	public void setLstSelectedCustomerJob(QbListsModel lstSelectedCustomerJob) {
		this.lstSelectedCustomerJob = lstSelectedCustomerJob;
	}
	/**
	 * @return the lstSelectedService
	 */
	public HRListValuesModel getLstSelectedService() {
		return lstSelectedService;
	}
	/**
	 * @param lstSelectedService the lstSelectedService to set
	 */
	public void setLstSelectedService(HRListValuesModel lstSelectedService) {
		this.lstSelectedService = lstSelectedService;
	}
	/**
	 * @return the selectedHoursOrDays
	 */
	public String getSelectedHoursOrDays() {
		return selectedHoursOrDays;
	}
	/**
	 * @param selectedHoursOrDays the selectedHoursOrDays to set
	 */
	public void setSelectedHoursOrDays(String selectedHoursOrDays) {
		this.selectedHoursOrDays = selectedHoursOrDays;
	}
	/**
	 * @return the selectedAssignToEmployee
	 */
	public EmployeeModel getSelectedAssignToEmployee() {
		return selectedAssignToEmployee;
	}
	/**
	 * @param selectedAssignToEmployee the selectedAssignToEmployee to set
	 */
	public void setSelectedAssignToEmployee(EmployeeModel selectedAssignToEmployee) {
		this.selectedAssignToEmployee = selectedAssignToEmployee;
	}
	/**
	 * @return the selectedCCToEmployee
	 */
	public EmployeeModel getSelectedCCToEmployee() {
		return selectedCCToEmployee;
	}
	/**
	 * @param selectedCCToEmployee the selectedCCToEmployee to set
	 */
	public void setSelectedCCToEmployee(EmployeeModel selectedCCToEmployee) {
		this.selectedCCToEmployee = selectedCCToEmployee;
	}
	/**
	 * @return the selectedLinkToPreviousTask
	 */
	public TasksModel getSelectedLinkToPreviousTask() {
		return selectedLinkToPreviousTask;
	}
	/**
	 * @param selectedLinkToPreviousTask the selectedLinkToPreviousTask to set
	 */
	public void setSelectedLinkToPreviousTask(TasksModel selectedLinkToPreviousTask) {
		this.selectedLinkToPreviousTask = selectedLinkToPreviousTask;
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
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the selectedClientType
	 */
	public String getSelectedClientType() {
		return selectedClientType;
	}
	/**
	 * @param selectedClientType the selectedClientType to set
	 */
	public void setSelectedClientType(String selectedClientType) {
		this.selectedClientType = selectedClientType;
	}
	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return clientType;
	}
	/**
	 * @param clientType the clientType to set
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	/**
	 * @return the clientTypeFullName
	 */
	public String getClientTypeFullName() {
		return clientTypeFullName;
	}
	/**
	 * @param clientTypeFullName the clientTypeFullName to set
	 */
	public void setClientTypeFullName(String clientTypeFullName) {
		this.clientTypeFullName = clientTypeFullName;
	}
	/**
	 * @return the reminderDate
	 */
	public Date getReminderDate() {
		return reminderDate;
	}
	/**
	 * @param reminderDate the reminderDate to set
	 */
	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}
	/**
	 * @return the reminderDateStr
	 */
	public String getReminderDateStr() {
		return reminderDateStr;
	}
	/**
	 * @param reminderDateStr the reminderDateStr to set
	 */
	public void setReminderDateStr(String reminderDateStr) {
		this.reminderDateStr = reminderDateStr;
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
	 * @return the customerNamefromFeedback
	 */
	public String getCustomerNamefromFeedback() {
		return customerNamefromFeedback;
	}
	/**
	 * @param customerNamefromFeedback the customerNamefromFeedback to set
	 */
	public void setCustomerNamefromFeedback(String customerNamefromFeedback) {
		this.customerNamefromFeedback = customerNamefromFeedback;
	}
	/**
	 * @return the createdAutommaticTask
	 */
	public String getCreatedAutommaticTask() {
		return createdAutommaticTask;
	}
	/**
	 * @param createdAutommaticTask the createdAutommaticTask to set
	 */
	public void setCreatedAutommaticTask(String createdAutommaticTask) {
		this.createdAutommaticTask = createdAutommaticTask;
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
	 * @return the feedbackNo
	 */
	public String getFeedbackNo() {
		return feedbackNo;
	}
	/**
	 * @param feedbackNo the feedbackNo to set
	 */
	public void setFeedbackNo(String feedbackNo) {
		this.feedbackNo = feedbackNo;
	}
	/**
	 * @return the hideFeedbackButton
	 */
	public boolean isHideFeedbackButton() {
		return hideFeedbackButton;
	}
	/**
	 * @param hideFeedbackButton the hideFeedbackButton to set
	 */
	public void setHideFeedbackButton(boolean hideFeedbackButton) {
		this.hideFeedbackButton = hideFeedbackButton;
	}
	/**
	 * @return the customerExpiryDate
	 */
	public String getCustomerExpiryDate() {
		return customerExpiryDate;
	}
	/**
	 * @param customerExpiryDate the customerExpiryDate to set
	 */
	public void setCustomerExpiryDate(String customerExpiryDate) {
		this.customerExpiryDate = customerExpiryDate;
	}
	/**
	 * @return the customerAddress
	 */
	public String getCustomerAddress() {
		return customerAddress;
	}
	/**
	 * @param customerAddress the customerAddress to set
	 */
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	/**
	 * @return the prospectiveAdress
	 */
	public String getProspectiveAdress() {
		return prospectiveAdress;
	}
	/**
	 * @param prospectiveAdress the prospectiveAdress to set
	 */
	public void setProspectiveAdress(String prospectiveAdress) {
		this.prospectiveAdress = prospectiveAdress;
	}
	
	
	
	
	
}
