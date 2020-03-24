package timesheet;

import hr.HRData;
import hr.WorkGroupData;
import hr.model.CompanyModel;
import hr.model.ContactModel;
import hr.model.WorkGroupModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.EmployeeModel;
import model.ProjectModel;
import model.TransferModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;

public class TransferViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	WorkGroupData workGroupData=new WorkGroupData();
	HRData hrdata=new HRData();
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<ProjectModel> lstProject;
	private ProjectModel selectedProject;
	
	private List<WorkGroupModel> lstWorkGroup;
	private WorkGroupModel selectedWorkGroup=new WorkGroupModel();
	
	private Date createDate;
	private Date transferDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private String formNo;
	private String siteInCharge;
	private String contactNO;
	private int supervisorId;
	private int workGroupId;
	private String memo;
	
	private List<EmployeeModel> lstEmployee;
	List<String> lstAddedEmployeeKeys;
	
	private List<EmployeeModel> lstAssignEmployee;
	private int supervisorID;
	
	int menuID=271;
	private MenuModel companyRole;
	
	public TransferViewModel()
	{
		try
		{
			Calendar c = Calendar.getInstance();
			
			createDate=df.parse(sdf.format(c.getTime()));		
			transferDate=df.parse(sdf.format(c.getTime()));
			
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			supervisorID=dbUser.getSupervisor();
			
			int defaultCompanyId=0;
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>0 && selectedCompany==null)				
			selectedCompany=lstComapnies.get(0);
									
			lstProject=data.getProjectList(selectedCompany.getCompKey(),"Transfer",true,supervisorID);
			selectedProject=lstProject.get(0);
			lstWorkGroup=workGroupData.fillWorkGroupInfo(selectedCompany.getCompKey(),"");
			//selectedWorkGroup=lstWorkGroup.get(0);
			getNewFormNo();
			
			lstAddedEmployeeKeys=new ArrayList<String>();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TransferViewModel ----> init", ex);			
		}
	}

	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		
		List<MenuModel> lstRoles= data.getTimeSheetRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	private void getNewFormNo()
	{
	int seqNo= data.getNextTransferSequenceNo();
	formNo=String.valueOf(seqNo);
	}
	
	  @Command
	  @NotifyChange({"lstEmployee"})
	  public void clearCommand()
	  {	
		  lstAddedEmployeeKeys=new ArrayList<String>();
		  lstEmployee=new ArrayList<EmployeeModel>();
	  }
	
	@Command
	  public void findEmployeeCommand()
	  {				  
		if(selectedProject.getProjectKey()==0)
		{
			 Messagebox.show("Please select the Location/Project ","Transfer", Messagebox.OK , Messagebox.QUESTION);
			return;
		}
		if(selectedWorkGroup.getRecno()==0)
		  {
				Messagebox.show("Please Select A Team !!","Transfer", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
		 }
		  Map<String,Object> arg = new HashMap<String,Object>();
		  arg.put("compKey", selectedCompany.getCompKey());
		  arg.put("type", selectedProject.getProjectName());
		  arg.put("teamName", selectedWorkGroup.getGroupName());
		  Executions.createComponents("/timesheet/searchemployee.zul", null,arg);
	  }
	
	  @GlobalCommand 
	  @NotifyChange({"lstEmployee"})
	  public void dlgClose(@BindingParam("myData")String empKeys)
	  {	
		  try
		  {
			
			if(!empKeys.equals(""))
			{
				String[] lstKeys=empKeys.split(",");
				String tmpEmpKeys="";
				
				for (String key : lstKeys) 
				{
				if(!lstAddedEmployeeKeys.contains(key))
					lstAddedEmployeeKeys.add(key);
				}
				
				for (String newKey : lstAddedEmployeeKeys) 
				{
					if(tmpEmpKeys.equals(""))
					tmpEmpKeys+=newKey;
					else
					tmpEmpKeys+=","+newKey;
				}
			
				lstEmployee=new ArrayList<EmployeeModel>();
				//List<EmployeeModel> lstEmployeeNew= new ArrayList<EmployeeModel>();
				lstEmployee=data.GetEmployeeTransfer(tmpEmpKeys, selectedCompany.getCompKey());
				
				/*for(EmployeeModel employeeModel:lstEmployeeNew)
				{
					if(!employeeModel.getProjectName().equalsIgnoreCase(selectedProject.getProjectName()))
					{
						lstEmployee.add(employeeModel);
					}
				}
				*/
				/*
				if(lstEmployee!=null)
				{
				lstEmployee.addAll(data.GetEmployeeTransfer(tmpEmpKeys, selectedCompany.getCompKey()));	
				}
				else
				{	
				lstEmployee=data.GetEmployeeTransfer(tmpEmpKeys, selectedCompany.getCompKey());
				}
				*/
			}
			
		  }
		  
		  catch (Exception ex)
			{	
				logger.error("ERROR in TransferViewModel ----> dlgClose", ex);			
			}
	  }
	  
	 @Command
	  public void saveCommand()
	  {
		  try
		  {			
			  if(lstEmployee==null || lstEmployee.size()==0)
			  {
				  Messagebox.show("There is no employee selected !! ","Transfer", Messagebox.OK , Messagebox.QUESTION);
				return;
			  }
			  
			 /* if(createDate.after(transferDate))
			  {
				 Messagebox.show("Create Date should be greater than Transfer Date.","Transfer", Messagebox.OK , Messagebox.QUESTION);
				return;
			  }*/
			  
			  if(selectedProject.getProjectKey()==0)
			   {
					Messagebox.show("Please Select Location/Project !!","Transfer", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
			   }
			  
			  if(selectedWorkGroup.getRecno()==0)
			   {
					Messagebox.show("Please Select A Team !!","Transfer", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
			   }
			  
			  List<Integer> lstEmpKeys=data.checkIfTransferExists(transferDate);
			  for (EmployeeModel item : lstEmployee) 
			  {
				if(lstEmpKeys.contains(item.getEmployeeKey()))
				{
					Messagebox.show("There is one Transfer exists for this Employee " + item.getFullName() +" . Please Change and continue.","Transfer", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			  }
			  
			  int newFormNo=data.getNextTransferSequenceNo();
			  for (EmployeeModel item : lstEmployee) 
			  {
				  TransferModel obj=new TransferModel();
				  obj.setRecNo(data.getNextTransferRecNo());
				  obj.setFormNo(newFormNo);
				  obj.setFormName("G");
				  obj.setStatus("A");
				  obj.setNotes(memo);
				  obj.setCreateDate(createDate);
				  obj.setEffDate(transferDate);
				  obj.setCompKey(item.getCompanyID());
				  obj.setEmpKey(item.getEmployeeKey());
				  obj.setProjectKey(selectedProject.getProjectKey());
				  obj.setCurrProjectId(item.getProjectKey());				  
				  data.insertNewTransfer(obj);
				  int recNo= workGroupData.getEmpSupervisorMaxRecQuerry();
				
				  Calendar c2 = Calendar.getInstance();
				  Date systemDate=c2.getTime();
				  Calendar c = Calendar.getInstance();
				  c.setTime(transferDate);
				  c.add(Calendar.DATE, -1);
				  Date transferDateNew=c.getTime();
				  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				  SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
				  if(transferDate.compareTo(df.parse(sdf1.format(systemDate)))==1 || transferDate.compareTo(df.parse(sdf1.format(systemDate)))==0)
				  {
				  	workGroupData.updatePreviousEmp(item.getEmployeeKey(), transferDateNew);
				  	workGroupData.insertIntoNewTrasferTable(recNo,workGroupId,supervisorId, item.getEmployeeKey(), transferDate, null, createDate);
				  }
				  else
				  {
					  workGroupData.insertIntoNewTrasferTable(recNo,workGroupId,supervisorId, item.getEmployeeKey(), transferDate, transferDateNew, createDate);
				  }
				 
				  
				  
			  }
			  
			  Messagebox.show("Data Saved Successfully ","Transfer", Messagebox.OK , Messagebox.INFORMATION);
			  
			  			  
		  }
		  catch (Exception ex)
			{	
				logger.error("ERROR in TransferViewModel ----> saveCommand", ex);			
			}
	  }
	 
	 //Assign Employee
	 @Command
	 @NotifyChange({"lstAssignEmployee"})
	 public void findTimesheetEmployeeCommand()
	 {	
		 try
		 {
			 lstAssignEmployee=data.getAssignSalaryEmployeeList(selectedCompany.getCompKey(), "");			 			
		 }
	     catch (Exception ex)
		{	
			logger.error("ERROR in TransferViewModel ----> findTimesheetEmployeeCommand", ex);			
		}
		 
	 }
	 
	 @Command
	 @NotifyChange({"lstAssignEmployee"})
	 public void saveAssignedTimesheetToEmployeeCommand()
	 {	
		 try
		 {
			 String batchQuery="";
			 for (EmployeeModel item : lstAssignEmployee) 
			 {
				 batchQuery += data.generateUpdateSalaryQuery(item.getEmployeeKey(), item.isTimesheetSalary() ? "T" : "S");							
			 } 
			 
			 data.updateSalaryCreate(batchQuery);
			 
			 lstAssignEmployee=data.getAssignSalaryEmployeeList(selectedCompany.getCompKey(), "");	
			 Messagebox.show("Data Saved Successfully ","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
		 }
	     catch (Exception ex)
		{	
			logger.error("ERROR in TransferViewModel ----> findTimesheetEmployeeCommand", ex);			
		}
		 
	 }
	 
	
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}

	@NotifyChange({"lstProject","selectedProject","siteInCharge","contactNO","selectedWorkGroup","lstWorkGroup"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;		
		siteInCharge="";
		contactNO="";
		lstProject=null;
		selectedProject=null;
		lstProject=data.getProjectList(selectedCompany.getCompKey(),"Transfer",true,supervisorID);
		selectedProject=lstProject.get(0);
		lstWorkGroup=workGroupData.fillWorkGroupInfo(selectedCompany.getCompKey(),"");
		selectedWorkGroup=null;
		/*if(lstWorkGroup.size()>0)
			selectedWorkGroup=lstWorkGroup.get(0);*/
		//selectedProject=new ProjectModel();
	}

	public List<ProjectModel> getLstProject() {
		return lstProject;
	}

	public void setLstProject(List<ProjectModel> lstProject) {
		this.lstProject = lstProject;
	}

	public ProjectModel getSelectedProject() {
		return selectedProject;
	}

	@NotifyChange({"siteInCharge","contactNO","memo"})
	public void setSelectedProject(ProjectModel selectedProject) 
	{
		this.selectedProject = selectedProject;
		if(selectedProject!=null && selectedWorkGroup!=null && selectedWorkGroup.getRecno()>0 )
		memo="On "+sdf.format(transferDate)+" The following employees will be transfered to project : "+selectedProject.getProjectName()+" and Team : "+selectedWorkGroup.getGroupName()+" under supervisor : "+siteInCharge+"";
		/*ContactModel cont=data.getSiteInCharge(selectedProject.getProjectKey());
		siteInCharge=cont.getDescription();
		contactNO=cont.getDetails();*/
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getTransferDate() {
		return transferDate;
	}
	@NotifyChange({"memo"})
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
		if(selectedProject!=null && selectedWorkGroup!=null && selectedWorkGroup.getRecno()>0 )
		memo="On "+sdf.format(transferDate)+" The following employees will be transfered to project : "+selectedProject.getProjectName()+" and Team : "+selectedWorkGroup.getGroupName()+" under supervisor : "+siteInCharge+"";
	}

	public String getFormNo() {
		return formNo;
	}

	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}

	public String getSiteInCharge() {
		return siteInCharge;
	}

	public void setSiteInCharge(String siteInCharge) {
		this.siteInCharge = siteInCharge;
	}

	public String getContactNO() {
		return contactNO;
	}

	public void setContactNO(String contactNO) {
		this.contactNO = contactNO;
	}

	public List<EmployeeModel> getLstEmployee() {
		return lstEmployee;
	}

	public void setLstEmployee(List<EmployeeModel> lstEmployee) {
		this.lstEmployee = lstEmployee;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<EmployeeModel> getLstAssignEmployee() {
		return lstAssignEmployee;
	}

	public void setLstAssignEmployee(List<EmployeeModel> lstAssignEmployee) {
		this.lstAssignEmployee = lstAssignEmployee;
	}

	/**
	 * @return the lstWorkGroup
	 */
	public List<WorkGroupModel> getLstWorkGroup() {
		return lstWorkGroup;
	}

	/**
	 * @param lstWorkGroup the lstWorkGroup to set
	 */
	public void setLstWorkGroup(List<WorkGroupModel> lstWorkGroup) {
		this.lstWorkGroup = lstWorkGroup;
	}

	/**
	 * @return the selectedWorkGroup
	 */
	public WorkGroupModel getSelectedWorkGroup() {
		return selectedWorkGroup;
	}

	/**
	 * @param selectedWorkGroup the selectedWorkGroup to set
	 */
	@NotifyChange({"siteInCharge","contactNO","memo"})
	public void setSelectedWorkGroup(WorkGroupModel selectedWorkGroup) {
		this.selectedWorkGroup = selectedWorkGroup;
		Map<String, Object> mapWorkGroup=new HashMap<String, Object>();
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		WorkGroupModel cont=new WorkGroupModel();
		mapWorkGroup=workGroupData.getWorkGroupById(selectedWorkGroup.getRecno());
		if(mapWorkGroup!=null)
		{
			cont=(WorkGroupModel) mapWorkGroup.get("workGroupModel");
			lstEmployees=(List<EmployeeModel>) mapWorkGroup.get("empList");
		}
		if(cont.getCompKey()==0)
		{
			mapWorkGroup=workGroupData.getWorkGroupByIdForZeroEmployees(selectedWorkGroup.getRecno(),selectedCompany.getCompKey());
			cont=(WorkGroupModel) mapWorkGroup.get("workGroupModel");
		}
		siteInCharge=cont.getSupervisorName();
		contactNO=""+lstEmployees.size();
		supervisorId=Integer.parseInt(cont.getSupervisor());
		workGroupId=cont.getRecno();
		if(selectedProject!=null)
		memo="On "+sdf.format(transferDate)+" The following employees will be transfered to project : "+selectedProject.getProjectName()+" and Team : "+selectedWorkGroup.getGroupName()+" under supervisor : "+siteInCharge+"";
		}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	
	
	
	
}
