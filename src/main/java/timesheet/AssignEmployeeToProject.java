package timesheet;

import hr.HRData;
import hr.WorkGroupData;
import hr.model.CompanyModel;
import hr.model.WorkGroupModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.EmployeeFilter;
import model.EmployeeModel;
import model.ProjectModel;
import model.TransferModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class AssignEmployeeToProject {
	
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	WorkGroupData workGroupData=new WorkGroupData();
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	HRData hrdata=new HRData();
	
	private boolean checkedAll=false;
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<ProjectModel> lstProject;
	private ProjectModel selectedProject;
	
	private String convertedFilterOption="";
	
	private Date createDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private List<EmployeeModel> lstEmployeeHistory;
	
	private List<String> lstFilterValues;
	
	private String selectedFilter="";
	
	private List<EmployeeModel> lstAllEmployeeHistory;
	
	private int footer;
	private int supervisorID;
	
	int menuID=281;
	private MenuModel companyRole;
	
	public AssignEmployeeToProject()
	{
		try
		{
			Calendar c = Calendar.getInstance();
			
			createDate=df.parse(sdf.format(c.getTime()));		
			
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			supervisorID=dbUser.getSupervisor();
			
			
			lstFilterValues=new ArrayList<String>();
			lstFilterValues.add("Assigned");
			lstFilterValues.add("Not Assigned");
			lstFilterValues.add("All");
			selectedFilter=lstFilterValues.get(2);
			
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
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToProject ----> init", ex);			
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
	
	 @Command
	 public void filterCommand()
	 {
		 Map<String,Object> arg = new HashMap<String,Object>();
		 arg.put("compKey", selectedCompany.getCompKey());
		 arg.put("type", "T");
		 Executions.createComponents("/timesheet/employeefilter.zul", null,arg);
		 
	 }
	 
	 @GlobalCommand 
	 @NotifyChange({"lstEmployeeHistory","footer"})
	  public void filterWindowClose(@BindingParam("myData")String empKeys)
	  {		
		 try
		  {
			if(!empKeys.equals(""))
			{
				lstEmployeeHistory= data.getAssignEmployeesToProjectByKeys(selectedCompany.getCompKey(), empKeys,convertedFilterOption);
				for(EmployeeModel model:lstEmployeeHistory)
				{
					
					for(ProjectModel projectModel:lstProject)
					{
						if(projectModel.getProjectKey()==model.getProjectKey())
						{
							model.setSelectedProject(projectModel);
						}
						
					}
				}
				footer=lstEmployeeHistory.size();
				lstAllEmployeeHistory=lstEmployeeHistory;
			}
		  }
		 catch (Exception ex)
			{	
			logger.error("ERROR in AssignEmployeeToProject ----> filterWindowClose", ex);			
			}
	  }
	
	
	
	  @Command
	  @NotifyChange({"lstEmployeeHistory","footer"})
	  public void clearCommand()
	  {	
		  lstEmployeeHistory=new ArrayList<EmployeeModel>();
		  lstAllEmployeeHistory=new ArrayList<EmployeeModel>();
		  footer=lstEmployeeHistory.size();
	  }
	  
	  private List<EmployeeModel> filterData()
		{
		  lstEmployeeHistory=lstAllEmployeeHistory;
					
			List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
			for (Iterator<EmployeeModel> i = lstEmployeeHistory.iterator(); i.hasNext();)
			{
				EmployeeModel tmp=i.next();				
						if(tmp.getFullName().toLowerCase().contains(employeeFilter.getFullName().toLowerCase())&&
						tmp.getDepartment().toLowerCase().contains(employeeFilter.getDepartment().toLowerCase())&&
						tmp.getPosition().toLowerCase().contains(employeeFilter.getPosition().toLowerCase())&&
						//tmp.getCountry().toLowerCase().contains(employeeFilter.getCountry().toLowerCase())&&
						//tmp.getAge().toLowerCase().startsWith(employeeFilter.getAge().toLowerCase())&&
						tmp.getEmployeeNo().toLowerCase().contains(employeeFilter.getEmployeeNo().toLowerCase())
						//tmp.getEnFirstName().toLowerCase().contains(employeeFilter.getEnFirstName().toLowerCase())&&
						//tmp.getEnMiddleName().toLowerCase().contains(employeeFilter.getEnMiddleName().toLowerCase())&&
						//tmp.getStatus().toLowerCase().contains(employeeFilter.getStatus().toLowerCase())&&
					//	tmp.getGender().toLowerCase().contains(employeeFilter.getGender().toLowerCase())&&
					//	tmp.getMaritalStatus().toLowerCase().contains(employeeFilter.getMaritalStatus().toLowerCase())&&
					//	tmp.getEmployeeStatus().toLowerCase().contains(employeeFilter.getEmployeeStatus().toLowerCase())&&
					//	tmp.getWorkGroupName().toLowerCase().startsWith(employeeFilter.getGroupName().toLowerCase())&&
					//	tmp.getSupervisorName().toLowerCase().contains(employeeFilter.getSupervisorName().toLowerCase())&&
					//	tmp.getStatusDescription().toLowerCase().contains(employeeFilter.getStatusDescription().toLowerCase())&&
					//	tmp.getProjectName().toLowerCase().contains(employeeFilter.getProjectName().toLowerCase())
						
						)
				{
					lst.add(tmp);
				}
			}
			return lst;
			
		}
		
		    @Command
		    @NotifyChange({"lstEmployeeHistory","footer"})
		    public void changeFilter() 
		    {	      
		    	lstEmployeeHistory=filterData();
		    	footer=lstEmployeeHistory.size();
		    }
	
	/*@Command
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
				
				for(EmployeeModel employeeModel:lstEmployeeNew)
				{
					if(!employeeModel.getProjectName().equalsIgnoreCase(selectedProject.getProjectName()))
					{
						lstEmployee.add(employeeModel);
					}
				}
				
				
				if(lstEmployee!=null)
				{
				lstEmployee.addAll(data.GetEmployeeTransfer(tmpEmpKeys, selectedCompany.getCompKey()));	
				}
				else
				{	
				lstEmployee=data.GetEmployeeTransfer(tmpEmpKeys, selectedCompany.getCompKey());
				}
				
			}
			
		  }
		  
		  catch (Exception ex)
			{	
				logger.error("ERROR in TransferViewModel ----> dlgClose", ex);			
			}
	  }*/
	  
	
	 //Assign Employee
	 @Command
	 @NotifyChange({"lstEmployeeHistory","footer"})
	 public void findEmployeesForProjectCommand()
	 {	
		 try
		 {
			 lstEmployeeHistory=data.getAssignEmployeesToProjectByKeys(selectedCompany.getCompKey(), "",convertedFilterOption);		
			 for(EmployeeModel model:lstEmployeeHistory)
				{
					
					for(ProjectModel projectModel:lstProject)
					{
						if(projectModel.getProjectKey()==model.getProjectKey())
						{
							model.setSelectedProject(projectModel);
						}
						
					}
				}
			 footer=lstEmployeeHistory.size();
			 lstAllEmployeeHistory=lstEmployeeHistory;
		 }
	     catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToProject ----> findEmployeesForProjectCommand", ex);			
		}
		 
	 }
	 
	 @Command
	    @NotifyChange({"lstEmployeeHistory","checkedAll","footer"})
		public void checkProjectAll()
		{
		 
		 if(checkedAll==true)
		 {
		 
			ProjectModel selected=new ProjectModel();
			for (EmployeeModel item : lstEmployeeHistory) 
			{
				if(item.getSelectedProject().getProjectKey()>0)
				{
						selected=item.getSelectedProject();
						break;
				}
				else{
					selected=null;
				}
				
			}
			if(selected==null)
			{
				Messagebox.show("Please select the project Name","Assign Employees To Project",Messagebox.OK , Messagebox.INFORMATION);
				checkedAll=false;
				return;
			 
			}
			else
			{
				for (EmployeeModel item : lstEmployeeHistory) 
				{
						item.setSelectedProject(selected);
						
				}
					
					
			}
		 }
		 else{
			 for (EmployeeModel item : lstEmployeeHistory) 
				{
						item.setSelectedProject(lstProject.get(0));
						
				}
			 
		 }
		 footer=lstEmployeeHistory.size();
		 
		 
			
		}
	 
	 @SuppressWarnings("unchecked")
	@Command
	 @NotifyChange({"lstAssignEmployee"})
	 public void saveAssignedEmployeeToProjectCommand()
	 {	
		 try
		 {
			 boolean flag=false;
			 for (EmployeeModel item : lstEmployeeHistory) 
				{
				 
				    if(item.getSelectedProject()==null || item.getSelectedProject().getProjectKey()==0)
					{
				    	flag=true;
						break;
					}
					
				}
			 if(flag==true)
			 {
			 Messagebox.show("You have not selected the Project for some employees, Do you want continue.? ","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				        if (evt.getName().equals("onYes")) 
				        {
				       	 for (EmployeeModel item : lstEmployeeHistory) 
						 {
							 if(item.getSelectedProject()!=null && item.getSelectedProject().getProjectKey()>0)
								{
							 data.updateAssignedEmployeesToProject(item.getSelectedProject().getProjectKey(),item.getEmployeeKey());
							 int newFormNo=data.getNextTransferSequenceNo();
							 TransferModel obj=new TransferModel();
							  obj.setRecNo(data.getNextTransferRecNo());
							  obj.setFormNo(newFormNo);
							  obj.setFormName("O");
							  obj.setStatus("A");
							  obj.setNotes("First Time");
							  obj.setCreateDate(createDate);
							  obj.setEffDate(item.getJoiningDate());
							  obj.setCompKey(selectedCompany.getCompKey());
							  obj.setEmpKey(item.getEmployeeKey());
							  obj.setProjectKey(item.getSelectedProject().getProjectKey());
							  obj.setCurrProjectId(item.getSelectedProject().getProjectKey());				  
							  data.insertNewTransfer(obj);
								}
							
						 } 
				       	findEmployeesForProjectCommand();
				       	Messagebox.show("The Employees Have been Successfully Assigned to Project","Assign Employees To Project ", Messagebox.OK , Messagebox.INFORMATION);
						 
				        }
				        else 
				        {		 
				        	return;
				        }
				    }
				
				});
			 }
			 else
			 {
				 for (EmployeeModel item : lstEmployeeHistory) 
				 {
					 if(item.getSelectedProject()!=null && item.getSelectedProject().getProjectKey()>0)
						{
					 data.updateAssignedEmployeesToProject(item.getSelectedProject().getProjectKey(),item.getEmployeeKey());
					 int newFormNo=data.getNextTransferSequenceNo();
					 TransferModel obj=new TransferModel();
					  obj.setRecNo(data.getNextTransferRecNo());
					  obj.setFormNo(newFormNo);
					  obj.setFormName("O");
					  obj.setStatus("A");
					  obj.setNotes("First Time");
					  obj.setCreateDate(createDate);
					  obj.setEffDate(item.getJoiningDate());
					  obj.setCompKey(selectedCompany.getCompKey());
					  obj.setEmpKey(item.getEmployeeKey());
					  obj.setProjectKey(item.getSelectedProject().getProjectKey());
					  obj.setCurrProjectId(item.getSelectedProject().getProjectKey());				  
					  data.insertNewTransfer(obj);
						}
					
				 } 
				findEmployeesForProjectCommand();
		       	Messagebox.show("The Employees Have been Successfully Assigned to Project","Assign Employees To Project ", Messagebox.OK , Messagebox.INFORMATION);
				 
			 }
			 
			
		
		
			 
			 
		 }
	     catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToProject ----> saveAssignedEmployeeToProjectCommand", ex);			
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

	@NotifyChange({"lstProject"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;		
		lstProject=data.getProjectList(selectedCompany.getCompKey(),"Transfer",true,supervisorID);
	}

	public List<ProjectModel> getLstProject() {
		return lstProject;
	}

	public void setLstProject(List<ProjectModel> lstProject) {
		this.lstProject = lstProject;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the lstEmployeeHistory
	 */
	public List<EmployeeModel> getLstEmployeeHistory() {
		return lstEmployeeHistory;
	}

	/**
	 * @param lstEmployeeHistory the lstEmployeeHistory to set
	 */
	public void setLstEmployeeHistory(List<EmployeeModel> lstEmployeeHistory) {
		this.lstEmployeeHistory = lstEmployeeHistory;
	}

	/**
	 * @return the checkedAll
	 */
	public boolean isCheckedAll() {
		return checkedAll;
	}

	/**
	 * @param checkedAll the checkedAll to set
	 */
	public void setCheckedAll(boolean checkedAll) {
		this.checkedAll = checkedAll;
	}

	/**
	 * @return the footer
	 */
	public int getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(int footer) {
		this.footer = footer;
	}

	/**
	 * @return the employeeFilter
	 */
	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}

	/**
	 * @param employeeFilter the employeeFilter to set
	 */
	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}

	/**
	 * @return the lstAllEmployeeHistory
	 */
	public List<EmployeeModel> getLstAllEmployeeHistory() {
		return lstAllEmployeeHistory;
	}

	/**
	 * @param lstAllEmployeeHistory the lstAllEmployeeHistory to set
	 */
	public void setLstAllEmployeeHistory(List<EmployeeModel> lstAllEmployeeHistory) {
		this.lstAllEmployeeHistory = lstAllEmployeeHistory;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public List<String> getLstFilterValues() {
		return lstFilterValues;
	}

	public void setLstFilterValues(List<String> lstFilterValues) {
		this.lstFilterValues = lstFilterValues;
	}

	public String getSelectedFilter() {
		return selectedFilter;
	}

	@NotifyChange({"selectedFilter","convertedFilterOption","lstEmployeeHistory","footer","lstAllEmployeeHistory"})
	public void setSelectedFilter(String selectedFilter) {
		this.selectedFilter = selectedFilter;
		if(selectedFilter.equalsIgnoreCase("All"))
		{
			convertedFilterOption="";
		}
		else if(selectedFilter.equalsIgnoreCase("Not Assigned"))
		{
			convertedFilterOption="N";
		}
		
		else if(selectedFilter.equalsIgnoreCase("Assigned"))
		{
			convertedFilterOption="A";
		}
		lstEmployeeHistory= data.getAssignEmployeesToProjectByKeys(selectedCompany.getCompKey(),"",convertedFilterOption);
		for(EmployeeModel model:lstEmployeeHistory)
		{
			
			for(ProjectModel projectModel:lstProject)
			{
				if(projectModel.getProjectKey()==model.getProjectKey())
				{
					model.setSelectedProject(projectModel);
				}
				
			}
		}
		footer=lstEmployeeHistory.size();
		lstAllEmployeeHistory=lstEmployeeHistory;
	}

	public ProjectModel getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(ProjectModel selectedProject) {
		this.selectedProject = selectedProject;
	}

	public String getConvertedFilterOption() {
		return convertedFilterOption;
	}

	public void setConvertedFilterOption(String convertedFilterOption) {
		this.convertedFilterOption = convertedFilterOption;
	}

	

}
