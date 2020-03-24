package hba;

import hr.HRData;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import layout.MenuModel;
import model.CustomerFeedbackModel;
import model.EmployeeModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Tabbox;

import common.HREnum;
import company.CompanyData;
import setup.users.WebusersModel;
import timesheet.TimeSheetData;
import admin.TasksModel;

public class TaskViewModel {

	HBAData hbadata=new HBAData();
	HRData hrData=new HRData();
	TimeSheetData timsheetData=new TimeSheetData();
	TaskData taskData=new TaskData();
	private Logger logger = Logger.getLogger(this.getClass());
	private List<TasksModel> lstTask=new ArrayList<TasksModel>();
	private List<TasksModel> lstAllTask=new ArrayList<TasksModel>();
	private TasksModel selectedTask;
	private boolean adminUser;

	CompanyData companyData=new CompanyData();

	private String footer;

	private TaskFilter filter=new TaskFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;

	private List<HRListValuesModel> lstTaskStatus;
	private HRListValuesModel selectedTaskStatus;

	private List<EmployeeModel> lstAssignToEmployees;
	private EmployeeModel selectedAssignToEmployee;

	private List<String> taskActivityFilter;
	private String selectedTaskActivityFilter;


	private int webUserID=0;

	WebusersModel dbUser=null;

	private int supervisorID=0;
	private int employeeKey=0;
	private int UserId=0;

	private List<WebusersModel> lstUsers=new ArrayList<WebusersModel>();


	private Date fromDate;

	private Date toDate;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	DecimalFormat dcf=new DecimalFormat("0.00");


	boolean notMappedTask=false;

	List<TasksModel> tempNotMappedAllTask=new ArrayList<TasksModel>();
	private MenuModel companyRole;

	@Init
	public void init(@BindingParam("type") String type)
	{
		try
		{
			lstPageSize=new ArrayList<Integer>();
			lstPageSize.add(15);
			lstPageSize.add(30);
			lstPageSize.add(50);

			lstAllPageSize=new ArrayList<String>();
			lstAllPageSize.add("15");
			lstAllPageSize.add("30");
			lstAllPageSize.add("50");
			lstAllPageSize.add("100");
			//lstAllPageSize.add("400");
			selectedAllPageSize=lstAllPageSize.get(2);

			taskActivityFilter=new ArrayList<String>();
			taskActivityFilter.add("All");
			taskActivityFilter.add("Tasks By You");
			taskActivityFilter.add("Tasks For You");
			selectedTaskActivityFilter=taskActivityFilter.get(0);


			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");

			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY,0);
			c.set(Calendar.MINUTE,0);
			c.set(Calendar.SECOND,0);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			c.set(Calendar.HOUR_OF_DAY,23);
			c.set(Calendar.MINUTE,59);
			c.set(Calendar.SECOND,59);
			toDate=df.parse(sdf.format(c.getTime()));

			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
				lstUsers=companyData.getUsersList(dbUser.getCompanyid());

				if(adminUser)
				{
					webUserID=0;
				}
				else
				{
					//to filter by employeekey if not admin to see only his tasks
					webUserID=dbUser.getEmployeeKey(); //dbUser.getUserid();
				}
			}

			supervisorID=dbUser.getSupervisor();//logged in as supervisor
			employeeKey=dbUser.getEmployeeKey();
			if(employeeKey>0)
				supervisorID=employeeKey;//logged in as employee

			//not use now
			//if(supervisorID>0)
			//	webUserID=supervisorID;

			lstTaskStatus=hrData.getHRListValues(143,"All");

			lstAssignToEmployees=hrData.getEmployeeList(0,"ALL","A",supervisorID);
			//default
			if(lstAssignToEmployees!=null &&lstAssignToEmployees.size()>0)
			{
				selectedAssignToEmployee=lstAssignToEmployees.get(0);
			}

			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");

				//Status drop down is set to All ins case of admin logged in and set to respective user name when user log in 
				if(adminUser)
				{
					if(lstAssignToEmployees!=null &&lstAssignToEmployees.size()>0)
					{
						selectedAssignToEmployee=lstAssignToEmployees.get(0);
					}
				}
				
				else
				{
					if(lstAssignToEmployees!=null &&lstAssignToEmployees.size()>0)
					{
						for(EmployeeModel employeeModel:lstAssignToEmployees)
						{
							if(employeeModel.getEmployeeKey()==webUserID)//employee and supervuisor key is assigned to webuser id
							{
								selectedAssignToEmployee=employeeModel;
								break;
							}
						}
					}														
				}
			}


			for(HRListValuesModel hrListValuesModel:lstTaskStatus)
			{
				if(hrListValuesModel.getEnDescription().equalsIgnoreCase("Created"))
				{
					selectedTaskStatus=hrListValuesModel;
					break;
				}
			}

			/*if(lstTaskStatus!=null && lstTaskStatus.size()>0)
		{
			selectedTaskStatus=lstTaskStatus.get(0);
			if(selectedAssignToEmployee.getFullName().equalsIgnoreCase("All"))
				selectedAssignToEmployee.setEmployeeKey(webUserID);
			lstTask=taskData.getAllTask(selectedTaskStatus.getListId(),selectedAssignToEmployee.getEmployeeKey());
		}*/


			lstAllTask=lstTask;
			if(lstTask.size()>0)
				selectedTask=lstTask.get(0);
			selectedPageSize=lstPageSize.get(2);


			footer="Total No. of Tasks "+lstTask.size();

		}
		catch(Exception e)
		{
			logger.error("error in TaskViewModel---int-->" , e);
		}
		//Messagebox.show(type);
	}


	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		TimeSheetData data=new TimeSheetData();
		List<MenuModel> lstRoles= data.getRolesPermessions(companyRoleId,HREnum.MenuIds.CRM.getValue());
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==HREnum.MenuIds.CRMTasks.getValue())
			{
				companyRole=item;
				break;
			}
		}
	}

	@Command
	@NotifyChange({"lstTask","footer"})
	public void searchTask() 
	{
		try
		{
			if(lstTaskStatus!=null && lstTaskStatus.size()>0)
			{

				//selectedTaskStatus=lstTaskStatus.get(0);
				if(selectedAssignToEmployee.getFullName().equalsIgnoreCase("All"))
					selectedAssignToEmployee.setEmployeeKey(webUserID);
				
				lstTask=taskData.getAllTask(selectedTaskStatus.getListId(),selectedAssignToEmployee.getEmployeeKey(),fromDate,toDate,selectedTaskActivityFilter);
				
				//no need i get direct from 
				/*for(TasksModel tasksModel:lstTask)
				{
					for(WebusersModel model:lstUsers)
					{
						if(0==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName("Admin");
							break;
						}
						else if(model.getSupervisor()==tasksModel.getCreatedUserID() || model.getEmployeeKey()==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName(model.getFirstname());
							break;
						}
					}
				}*/
				
				lstAllTask.clear();
				lstAllTask.addAll(lstTask);
				tempNotMappedAllTask.clear();
				tempNotMappedAllTask.addAll(lstAllTask);
				footer="Total No. of Tasks "+lstTask.size();
			}

		}
		catch (Exception ex) {
			logger.error("error in TaskViewModel---changeFilter-->" , ex);
		}

	}




	private List<TasksModel> filterData()
	{
		lstTask=lstAllTask;
		List<TasksModel>  lst=new ArrayList<TasksModel>();
		for (Iterator<TasksModel> i = lstTask.iterator(); i.hasNext();)
		{
			TasksModel tmp=i.next();				
			if(tmp.getTaskName().toLowerCase().contains(filter.getTaskName().toLowerCase())&&
					tmp.getTaskNumber().toLowerCase().contains(filter.getTaskNumber().toLowerCase())&&
					tmp.getCreationDateStr().toLowerCase().contains(filter.getCreationDateStr().toLowerCase())&&
					tmp.getMostRecentUpdate().toLowerCase().contains(filter.getMostRecentUpdate().toLowerCase())&&
					tmp.getTaskName().toLowerCase().contains(filter.getTaskName().toLowerCase())&&
					//tmp.getCreatedUserName().toLowerCase().contains(filter.getCreatedUserName().toLowerCase())&&
					tmp.getTaskType().toLowerCase().contains(filter.getTaskType().toLowerCase())&&
					tmp.getPriorityNAme().toLowerCase().contains(filter.getPriorityNAme().toLowerCase())&&
					(tmp.getEstimatatedNumber()+"").toLowerCase().contains(filter.getEstimatatedNumber().toLowerCase())&&
					(tmp.getActualNumber()+"").toLowerCase().contains(filter.getActualNumber().toLowerCase())&&
					tmp.getCustomerNAme().toLowerCase().contains(filter.getCustomerNAme().toLowerCase())&&
					tmp.getProjectName().toLowerCase().contains(filter.getProjectName().toLowerCase())&&
					tmp.getServiceName().toLowerCase().contains(filter.getServiceName().toLowerCase())&&
					tmp.getEmployeeName().toLowerCase().contains(filter.getEmployeeName().toLowerCase())&&
					tmp.getCcEmployeeName().toLowerCase().contains(filter.getCcEmployeeName().toLowerCase())&&
					tmp.getMemo().toLowerCase().contains(filter.getMemo().toLowerCase())&&
					tmp.getComments().toLowerCase().contains(filter.getComments().toLowerCase())&&
					tmp.getTaskStep().toLowerCase().contains(filter.getTaskStep().toLowerCase())&&
					tmp.getClientTypeFullName().toLowerCase().contains(filter.getClientTypeFullName().toLowerCase())&&
					tmp.getHistory().toLowerCase().contains(filter.getHistory().toLowerCase())&&
					tmp.getPreviossTaskName().toLowerCase().contains(filter.getPreviossTaskName().toLowerCase())&&
					tmp.getFeedbackNo().toLowerCase().contains(filter.getFeedbackNo().toLowerCase())&&
					tmp.getReminderDateStr().toLowerCase().contains(filter.getReminderDateStr().toLowerCase())&&
					(tmp.getRemindIn()+"").toLowerCase().contains(filter.getRemindIn().toLowerCase())&&
					tmp.getExpectedDatetofinishStr().toLowerCase().contains(filter.getExpectedDatetofinishStr().toLowerCase())&&
					tmp.getStatusName().toLowerCase().contains(filter.getStatusName().toLowerCase())

					)
			{
				lst.add(tmp);
			}
		}
		return lst;

	}

	@Command
	@NotifyChange({"lstTask","footer"})
	public void changeFilter() 
	{
		try
		{
			lstTask=filterData();
			footer="Total no. of Tasks "+lstTask.size();

		}
		catch (Exception ex) {
			logger.error("error in TaskViewModel---changeFilter-->" , ex);
		}

	}

	@Command
	public void viewTask(@BindingParam("row") TasksModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("taskKey", row.getTaskid());
			arg.put("compKey",0);
			arg.put("type","view");
			Executions.createComponents("/hba/list/editTask.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> viewTask", ex);			
		}
	}

	@GlobalCommand 
	@NotifyChange({"lstTask","footer"})
	public void refreshParentTaskForm(@BindingParam("type")String type)
	{		
		try
		{
			if(selectedAssignToEmployee.getFullName().equalsIgnoreCase("All"))
				selectedAssignToEmployee.setEmployeeKey(webUserID);
			lstTask=taskData.getAllTask(selectedTaskStatus.getListId(),selectedAssignToEmployee.getEmployeeKey(),fromDate,toDate,selectedTaskActivityFilter);
			for(TasksModel tasksModel:lstTask)
			{
				for(WebusersModel model:lstUsers)
				{
					if(0==tasksModel.getCreatedUserID())
					{
						tasksModel.setCreatedUserName("Admin");
						break;
					}
					else if(model.getSupervisor()==tasksModel.getCreatedUserID() || model.getEmployeeKey()==tasksModel.getCreatedUserID())
					{
						tasksModel.setCreatedUserName(model.getFirstname());
						break;
					}

				}
			}
			lstAllTask.clear();
			lstAllTask.addAll(lstTask);
			tempNotMappedAllTask.clear();
			tempNotMappedAllTask.addAll(lstAllTask);
			footer="Total No. of Tasks "+lstTask.size();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> refreshParentTaskForm", ex);			
		}
	}

	@Command
	public void editTask(@BindingParam("row") TasksModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("taskKey", row.getTaskid());
			arg.put("compKey",0);
			arg.put("type","edit");
			Executions.createComponents("/hba/list/editTask.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> editTask", ex);			
		}
	}

	@Command
	public void addTask()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("taskKey", 0);
			arg.put("compKey",0);
			arg.put("type","add");
			Executions.createComponents("/hba/list/editTask.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> addTask", ex);			
		}
	}

	@Command
	public void resetTask()
	{
		try
		{
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> resetTask", ex);			
		}
	}



	@Command
	public void groupOfTask()
	{
		try
		{

			Clients.showNotification("Under implementation.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;

			/*  Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("taskKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","add");
			   Executions.createComponents("/hba/list/groupOfTask.zul", null,arg);*/
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> groupOfTask", ex);			
		}
	}


	@Command
	public void openFeedback(@BindingParam("row") TasksModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("feedBackKey", row.getFeedbackKey());
			arg.put("compKey",0);
			arg.put("type","view");
			Executions.createComponents("/crm/editCustomerFeedback.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TaskViewModel ----> editCustomerFeedBack", ex);			
		}
	}



	public List<TasksModel> getLstTask() {
		return lstTask;
	}

	public void setLstTask(List<TasksModel> lstTask) {
		this.lstTask = lstTask;
	}

	public List<TasksModel> getLstAllTask() {
		return lstAllTask;
	}

	public void setLstAllTask(List<TasksModel> lstAllTask) {
		this.lstAllTask = lstAllTask;
	}

	public TasksModel getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(TasksModel selectedTask) {
		this.selectedTask = selectedTask;
	}

	/**
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}


	/**
	 * @return the lstPageSize
	 */
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	/**
	 * @param lstPageSize the lstPageSize to set
	 */
	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	/**
	 * @return the selectedPageSize
	 */
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	/**
	 * @param selectedPageSize the selectedPageSize to set
	 */
	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	/**
	 * @return the lstAllPageSize
	 */
	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	/**
	 * @param lstAllPageSize the lstAllPageSize to set
	 */
	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	/**
	 * @return the selectedAllPageSize
	 */
	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	/**
	 * @param selectedAllPageSize the selectedAllPageSize to set
	 */
	@NotifyChange({"selectedPageSize"})	
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if(selectedAllPageSize.equalsIgnoreCase("All"))
		{
			selectedPageSize=lstTask.size();

		}
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}

	/**
	 * @return the filter
	 */
	public TaskFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(TaskFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	public List<HRListValuesModel> getLstTaskStatus() {
		return lstTaskStatus;
	}

	public void setLstTaskStatus(List<HRListValuesModel> lstTaskStatus) {
		this.lstTaskStatus = lstTaskStatus;
	}

	public HRListValuesModel getSelectedTaskStatus() {
		return selectedTaskStatus;
	}

	@NotifyChange({"lstTask","footer"})
	public void setSelectedTaskStatus(HRListValuesModel selectedTaskStatus) {
		this.selectedTaskStatus = selectedTaskStatus;
		/*	if(selectedAssignToEmployee.getFullName().equalsIgnoreCase("All"))
			selectedAssignToEmployee.setEmployeeKey(webUserID);
		 lstTask=taskData.getAllTask(selectedTaskStatus.getListId(),selectedAssignToEmployee.getEmployeeKey());
		 lstAllTask=lstTask;
		 footer="Total No. of Tasks "+lstTask.size();*/
	}


	public List<EmployeeModel> getLstAssignToEmployees() {
		return lstAssignToEmployees;
	}


	public void setLstAssignToEmployees(List<EmployeeModel> lstAssignToEmployees) {
		this.lstAssignToEmployees = lstAssignToEmployees;
	}


	public EmployeeModel getSelectedAssignToEmployee() {
		return selectedAssignToEmployee;
	}

	@NotifyChange({"lstTask","footer"})
	public void setSelectedAssignToEmployee(EmployeeModel selectedAssignToEmployee) {
		this.selectedAssignToEmployee = selectedAssignToEmployee;
		/*if(selectedAssignToEmployee!=null)
		{
		if(selectedAssignToEmployee.getFullName().equalsIgnoreCase("All"))
				selectedAssignToEmployee.setEmployeeKey(webUserID);
		 lstTask=taskData.getAllTask(selectedTaskStatus.getListId(),selectedAssignToEmployee.getEmployeeKey());
		 lstAllTask=lstTask;
		 footer="Total No. of Tasks "+lstTask.size();
		}*/
	}


	@Command
	public void download(@BindingParam("row") TasksModel obj)
	{
		if(obj.getSelectedAttachements()!=null && !obj.getSelectedAttachements().getFilepath().equalsIgnoreCase(""))
		{
			File file=new File(obj.getSelectedAttachements().getFilepath());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String mimeType=mimeTypesMap.getContentType(file);

			try {
				Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, obj.getSelectedAttachements().getFilename()); 

			}catch (FileNotFoundException e)
			{
				Clients.showNotification("There Is No Such File in server to download.(May be Deleted)",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR in TaskViewModel ----> download", e);	
			}

		}
		else
		{
			Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	}



	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}



	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}



	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}



	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	} 

	@Command
	@NotifyChange({"lstTask","footer"})
	public void getNotMappedTocustomerTasks() {
		List<TasksModel> tempNotMappedTask=new ArrayList<TasksModel>();
		if(notMappedTask)
		{
			for(TasksModel tasksModel:lstAllTask)
			{
				if(tasksModel.getCustomerRefKey()==0)
				{
					tempNotMappedTask.add(tasksModel);
				}

			}
			lstTask.clear();
			lstTask.addAll(tempNotMappedTask);
			lstAllTask.clear();
			lstAllTask.addAll(lstTask);

		}
		else
		{

			lstTask.clear();
			lstTask.addAll(tempNotMappedAllTask);
			lstAllTask.clear();
			lstAllTask.addAll(tempNotMappedAllTask);
		}
		footer="Total No. of Tasks "+lstTask.size();
	}



	/**
	 * @return the notMappedTask
	 */
	public boolean isNotMappedTask() {
		return notMappedTask;
	}



	/**
	 * @param notMappedTask the notMappedTask to set
	 */
	public void setNotMappedTask(boolean notMappedTask) {
		this.notMappedTask = notMappedTask;
	}



	/**
	 * @return the taskActivityFilter
	 */
	public List<String> getTaskActivityFilter() {
		return taskActivityFilter;
	}



	/**
	 * @param taskActivityFilter the taskActivityFilter to set
	 */
	public void setTaskActivityFilter(List<String> taskActivityFilter) {
		this.taskActivityFilter = taskActivityFilter;
	}



	/**
	 * @return the selectedTaskActivityFilter
	 */
	public String getSelectedTaskActivityFilter() {
		return selectedTaskActivityFilter;
	}



	/**
	 * @param selectedTaskActivityFilter the selectedTaskActivityFilter to set
	 */
	public void setSelectedTaskActivityFilter(String selectedTaskActivityFilter) {
		this.selectedTaskActivityFilter = selectedTaskActivityFilter;
	}



	public MenuModel getCompanyRole() {
		return companyRole;
	}



	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}  





}
