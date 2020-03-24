package model;

import home.QuotationAttachmentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.DependsOn;

import admin.TasksModel;

public class TimeSheetGridModel 
{
	
	private int recNo;
	private int lineNo;
	private int srNo;
	private int compKey;
	private int empKey;
	private String empNo;
	private String enFullName;
	private Date tsDate;
	private String fromTime;
	private String toTime;
	
	private Date tsFromTime;
	private Date tstoTime;
		
	private boolean timingFlag;
	private String dayName;
	private String status;
	private String employeeStatus;
	private String position;
	private String calculate;
	private String unitType;
	private int unitKey;
	private double units;
	private double realUnits;
	private double totals;
	private double otCalculation;
	private double otAmount;
	private int otUnit1;
	private int otUnit2;
	private int otUnit3;
	private int otUnits;
	private int totalOTUnits;
	
	private boolean otUnit1Enable;
	private boolean otUnit2Enable;
	private boolean otUnit3Enable;
	
	private int totalOT;
	private String projectName;
	private int projectId;
	private int serviceId;
	private String serviceName;
	private String notes;
	private String tomorrowPlan;
	
	private int shiftkey;
	private int otNos;
	private double normalHours;
	private double totalNormalHours;
	private double normalOTHours;
	private double maxOTAmount;
	
	private String salaryStatus;
	
	private String holidayDesc;
	private boolean holiday;
	
	private int shiftRecNo;
	private boolean firstOfRecord;
	
	private ProjectModel project;
	private HRListValuesModel service;
	private QbListsModel customerJob;
	private int customerJobRefKey;
	
	private int approved;
	private int supervisorId;
	private Date EmployeementDate;
	
	private boolean cantChange;
	private int noOfshifts;
	private String leaveFlag;
	
	private int rowIndex;
	private boolean newRowAdded;
	private boolean rowFromSetup;
	private boolean mainShift;
	
	private List<TasksModel> lstCustomerTaks;
	private TasksModel selectedTask;
	private List<HRListValuesModel> lstTaskStatus;
	private HRListValuesModel selectedTaskStatus;
	private CompanySettingsModel compSettings;
	
	private String attachPath;
	private List<QuotationAttachmentModel> listOfattchments=new ArrayList<QuotationAttachmentModel>();
	private int oldRecNo;
	
	public TimeSheetGridModel()
	{
		//lstTaskStatus=data.getHRListValues(143,"");					  
	}
	
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEnFullName() {
		return enFullName;
	}
	public void setEnFullName(String enFullName) {
		this.enFullName = enFullName;
	}
	public Date getTsDate() {
		return tsDate;
	}
	public void setTsDate(Date tsDate) {
		this.tsDate = tsDate;
	}
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
		/*
		if(status.equals("Holiday"))
		{
			Messagebox.show("Can't set holiday,You change Shift Setup!!");
			status="Present";
		}
		if(status.equals("Leave"))
		{
			Messagebox.show("Can't set Leave,Please Create Leave from Activities Menu!!");
			status="Present";
		}
		*/
	}
	public String getCalculate() {
		return calculate;
	}
	public void setCalculate(String calculate) {
		this.calculate = calculate;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public double getUnits() {
		return units;
	}
	public void setUnits(double units) {
		this.units = units;
	}
	public double getTotals() {
		return totals;
	}
	public void setTotals(double totals) {
		this.totals = totals;
	}
	public int getOtUnit1() {
		return otUnit1;
	}
	public void setOtUnit1(int otUnit1) {
		this.otUnit1 = otUnit1;
	}
	public int getOtUnit2() {
		return otUnit2;
	}
	public void setOtUnit2(int otUnit2) {
		this.otUnit2 = otUnit2;
	}
	public int getOtUnit3() {
		return otUnit3;
	}
	public void setOtUnit3(int otUnit3) {
		this.otUnit3 = otUnit3;
	}
	public int getTotalOT() {
		//totalOT= otUnit1+otUnit2+otUnit3;
		return totalOT;
	}
	public void setTotalOT(int totalOT) {
		this.totalOT = totalOT;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getSalaryStatus() {
		return salaryStatus;
	}
	public void setSalaryStatus(String salaryStatus) {
		this.salaryStatus = salaryStatus;
	}
	public String getHolidayDesc() {
		return holidayDesc;
	}
	public void setHolidayDesc(String holidayDesc) {
		this.holidayDesc = holidayDesc;
	}
	public boolean isHoliday() {
		return holiday;
	}
	public void setHoliday(boolean holiday) {
		this.holiday = holiday;
	}
	public int getEmpKey() {
		return empKey;
	}
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	
	public boolean isTimingFlag() {
		return timingFlag;
	}
	public void setTimingFlag(boolean timingFlag) {
		this.timingFlag = timingFlag;
	}
	public boolean isFirstOfRecord() {
		return firstOfRecord;
	}
	public void setFirstOfRecord(boolean firstOfRecord) {
		this.firstOfRecord = firstOfRecord;
	}
	public ProjectModel getProject() {
		return project;
	}
	public void setProject(ProjectModel project) {
		this.project = project;
	}
	public HRListValuesModel getService() {
		return service;
	}
	public void setService(HRListValuesModel service) {
		this.service = service;
	}
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public int getUnitKey() {
		return unitKey;
	}
	public void setUnitKey(int unitKey) {
		this.unitKey = unitKey;
	}
	public int getShiftkey() {
		return shiftkey;
	}
	public void setShiftkey(int shiftkey) {
		this.shiftkey = shiftkey;
	}
	public int getCompKey() {
		return compKey;
	}
	public void setCompKey(int compKey) {
		this.compKey = compKey;
	}
	public int getOtNos() {
		return otNos;
	}
	public void setOtNos(int otNos) {
		this.otNos = otNos;
	}
	public double getNormalHours() {
		return normalHours;
	}
	public void setNormalHours(double normalHours) {
		this.normalHours = normalHours;
	}
	public double getNormalOTHours() {
		return normalOTHours;
	}
	public void setNormalOTHours(double normalOTHours) {
		this.normalOTHours = normalOTHours;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getShiftRecNo() {
		return shiftRecNo;
	}
	public void setShiftRecNo(int shiftRecNo) {
		this.shiftRecNo = shiftRecNo;
	}
	public double getOtCalculation() {
		return otCalculation;
	}
	public void setOtCalculation(double otCalculation) {
		this.otCalculation = otCalculation;
	}
	public double getOtAmount() {
		return otAmount;
	}
	public void setOtAmount(double otAmount) {
		this.otAmount = otAmount;
	}
	public double getMaxOTAmount() {
		return maxOTAmount;
	}
	public void setMaxOTAmount(double maxOTAmount) {
		this.maxOTAmount = maxOTAmount;
	}
	public int getOtUnits() {
		return otUnits;
	}
	public void setOtUnits(int otUnits) {
		this.otUnits = otUnits;
	}
	public boolean isOtUnit1Enable() {
		return otUnit1Enable;
	}
	public void setOtUnit1Enable(boolean otUnit1Enable) {
		this.otUnit1Enable = otUnit1Enable;
	}
	public boolean isOtUnit2Enable() {
		return otUnit2Enable;
	}
	public void setOtUnit2Enable(boolean otUnit2Enable) {
		this.otUnit2Enable = otUnit2Enable;
	}
	public boolean isOtUnit3Enable() {
		return otUnit3Enable;
	}
	public void setOtUnit3Enable(boolean otUnit3Enable) {
		this.otUnit3Enable = otUnit3Enable;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	public int getSupervisorId() {
		return supervisorId;
	}
	public void setSupervisorId(int supervisorId) {
		this.supervisorId = supervisorId;
	}
	public Date getEmployeementDate() {
		return EmployeementDate;
	}
	public void setEmployeementDate(Date employeementDate) {
		EmployeementDate = employeementDate;
	}
	
	public boolean isCantChange() {
		return cantChange;
	}
	public void setCantChange(boolean cantChange) {
		this.cantChange = cantChange;
	}
	public Date getTsFromTime() {
		return tsFromTime;
	}
	public void setTsFromTime(Date tsFromTime) {
		this.tsFromTime = tsFromTime;
	}
	public Date getTstoTime() {
		return tstoTime;
	}
	public void setTstoTime(Date tstoTime) {
		this.tstoTime = tstoTime;
	}
	public double getRealUnits() {
		return realUnits;
	}
	public void setRealUnits(double realUnits) {
		this.realUnits = realUnits;
	}
	public int getNoOfshifts() {
		return noOfshifts;
	}
	public void setNoOfshifts(int noOfshifts) {
		this.noOfshifts = noOfshifts;
	}
	public double getTotalNormalHours() {
		return totalNormalHours;
	}
	public void setTotalNormalHours(double totalNormalHours) {
		this.totalNormalHours = totalNormalHours;
	}
	public String getLeaveFlag() {
		return leaveFlag;
	}
	public void setLeaveFlag(String leaveFlag) {
		this.leaveFlag = leaveFlag;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	//@DependsOn({"otUnit1","otUnit2","otUnit3"})
	public int getTotalOTUnits() {
		//totalOTUnits=otUnit1+otUnit2+otUnit3;
		return totalOTUnits;
	}
	public void setTotalOTUnits(int totalOTUnits) {
		this.totalOTUnits = totalOTUnits;
	}
	public boolean isNewRowAdded() {
		return newRowAdded;
	}
	public void setNewRowAdded(boolean newRowAdded) {
		this.newRowAdded = newRowAdded;
	}
	public QbListsModel getCustomerJob() {
		return customerJob;
	}
	
	public void setCustomerJob(QbListsModel customerJob) 
	{
		this.customerJob = customerJob;
		
	}
	public int getCustomerJobRefKey() {
		return customerJobRefKey;
	}
	public void setCustomerJobRefKey(int customerJobRefKey) {
		this.customerJobRefKey = customerJobRefKey;
	}
	public boolean isRowFromSetup() {
		return rowFromSetup;
	}
	public void setRowFromSetup(boolean rowFromSetup) {
		this.rowFromSetup = rowFromSetup;
	}
	public String getTomorrowPlan() {
		return tomorrowPlan;
	}
	public void setTomorrowPlan(String tomorrowPlan) {
		this.tomorrowPlan = tomorrowPlan;
	}
	public boolean isMainShift() {
		return mainShift;
	}
	public void setMainShift(boolean mainShift) {
		this.mainShift = mainShift;
	}
	public List<TasksModel> getLstCustomerTaks() {
		selectedTaskStatus=null;
		return lstCustomerTaks;
	}
	public void setLstCustomerTaks(List<TasksModel> lstCustomerTaks) {
		this.lstCustomerTaks = lstCustomerTaks;
	}
	public TasksModel getSelectedTask() {
		return selectedTask;
	}
	public void setSelectedTask(TasksModel selectedTask) {
		this.selectedTask = selectedTask;
	}
	public List<HRListValuesModel> getLstTaskStatus() {
		return lstTaskStatus;
	}
	public void setLstTaskStatus(List<HRListValuesModel> lstTaskStatus) {
		this.lstTaskStatus = lstTaskStatus;
	}
	@DependsOn({"selectedTask"})
	public HRListValuesModel getSelectedTaskStatus() 
	{
		if(selectedTask!=null)
		{			
			for (HRListValuesModel item : lstTaskStatus)
			{
				if(item.getListId()==selectedTask.getStatusKey())
					selectedTaskStatus=item;
			}			
		}
		return selectedTaskStatus;
	}
	
	public void setSelectedTaskStatus(HRListValuesModel selectedTaskStatus) 
	{		
		this.selectedTaskStatus = selectedTaskStatus;	
		if(selectedTask!=null)
		selectedTask.setStatusKey(selectedTaskStatus.getListId());
	}

	public CompanySettingsModel getCompSettings() {
		return compSettings;
	}

	public void setCompSettings(CompanySettingsModel compSettings) {
		this.compSettings = compSettings;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	public List<QuotationAttachmentModel> getListOfattchments() {
		return listOfattchments;
	}

	public void setListOfattchments(List<QuotationAttachmentModel> listOfattchments) {
		this.listOfattchments = listOfattchments;
	}

	public int getOldRecNo() {
		return oldRecNo;
	}

	public void setOldRecNo(int oldRecNo) {
		this.oldRecNo = oldRecNo;
	}
	
	
}
