package timesheet;

import hr.WorkGroupData;
import hr.model.WorkGroupModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.EmployeeFilter;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class SearchEmployeeViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	WorkGroupData workGroupData=new WorkGroupData();
	private String value;
	private  List<EmployeeModel> lstEmployees;
	private List<EmployeeModel> lstAllEmployees;
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	private boolean checkAllEmployee;
	private Set<EmployeeModel> selectedEntities;	 
	private int footer;
	private int footerAssigned;
	private int footerNotAssigned;
	
	private boolean all=true;
	private boolean assigned=false;
	private boolean notAssigned=false;
	
	private List<WorkGroupModel> lstWorkGroup;
	private WorkGroupModel selectedWorkGroup=new WorkGroupModel();
	
	
	private String viewType;
	
	@Init
	public void init(@BindingParam("compKey")int compKey,@BindingParam("type")String type,@BindingParam("teamName")String teamName,@BindingParam("viewType") String _viewType)
	{		
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		
		lstEmployees=data.getEmployeeList(compKey,type,dbUser.getSupervisor());
		lstAllEmployees=lstEmployees;
		footer=lstEmployees.size();
		
		viewType=_viewType;
		/*if(!type.equalsIgnoreCase(""))
		{
			 List<EmployeeModel> projectList=new ArrayList<EmployeeModel>();
			for(EmployeeModel model:lstAllEmployees)
			 {
				if(model.getProjectKey()==0)
				{
					model.setProjectName("");
				}
				if(model.getWorkGroupId()==0)
				{
					model.setWorkGroupName("");
				}
				if(!model.getProjectName().equalsIgnoreCase(type))
				{
					 projectList.add(model);
				}
			 }
			lstEmployees=projectList;
			lstAllEmployees=lstEmployees;
			footer=lstEmployees.size();
		}*/
		
		 List<EmployeeModel> lstAllEmployeesAssigned=new ArrayList<EmployeeModel>();
		 for(EmployeeModel model:lstAllEmployees)
		 {
			 if(model.getSupervisorId()>0)
			 {
				 lstAllEmployeesAssigned.add(model);
			 }
		 }
		 footerAssigned=lstAllEmployeesAssigned.size();
		 
		 List<EmployeeModel> lstAllEmployeesNotAssigned=new ArrayList<EmployeeModel>();
		 for(EmployeeModel model:lstAllEmployees)
		 {
			 if(model.getSupervisorId()==0)
			 {
				 lstAllEmployeesNotAssigned.add(model);
			 }
		 }
		 
		 footerNotAssigned=lstAllEmployeesNotAssigned.size();
		// lstWorkGroup=workGroupData.fillWorkGroupInfo(compKey,"");
		
	}
	
	
	@Command
	@NotifyChange({"lstEmployees","footerAssigned","footer","footerNotAssigned","assigned","notAssigned","all"})
	 public void AllEmployeesEmployees() 
	    {	      
		lstEmployees=lstAllEmployees;
		footer=lstEmployees.size();
		
		assigned=false;
		notAssigned=false;
		
		 List<EmployeeModel> lstAllEmployeesAssigned=new ArrayList<EmployeeModel>();
		 for(EmployeeModel model:lstAllEmployees)
		 {
			 if(model.getSupervisorId()>0)
			 {
				 lstAllEmployeesAssigned.add(model);
			 }
		 }
		 footerAssigned=lstAllEmployeesAssigned.size();
		 
		 List<EmployeeModel> lstAllEmployeesNotAssigned=new ArrayList<EmployeeModel>();
		 for(EmployeeModel model:lstAllEmployees)
		 {
			 if(model.getSupervisorId()==0)
			 {
				 lstAllEmployeesNotAssigned.add(model);
			 }
		 }
		 
		 footerNotAssigned=lstAllEmployeesNotAssigned.size();
	    }
	
	
	
	@Command
	@NotifyChange({"lstEmployees","footerAssigned","footer","footerNotAssigned","assigned","notAssigned","all"})
	 public void assignedEmployees() 
	    {	      
		
		assigned=true;
		notAssigned=false;
		all=false;
		  List<EmployeeModel> lstAllEmployeesAssigned=new ArrayList<EmployeeModel>();
		 for(EmployeeModel model:lstAllEmployees)
		 {
			 if(model.getSupervisorId()>0)
			 {
				 lstAllEmployeesAssigned.add(model);
			 }
		 }
		 	lstEmployees=lstAllEmployeesAssigned;
	    	footerAssigned=lstEmployees.size();
	    	footer=lstEmployees.size();
	    	footerNotAssigned=0;
	    }
	 
	@Command
	@NotifyChange({"lstEmployees","footerAssigned","footer","footerNotAssigned","assigned","notAssigned","all"})
	 public void NotassignedEmployees() 
	    {	 
		
		assigned=false;
		notAssigned=true;
		all=false;
		 List<EmployeeModel> lstAllEmployeesNotAssigned=new ArrayList<EmployeeModel>();
		 for(EmployeeModel model:lstAllEmployees)
		 {
			 if(model.getSupervisorId()==0)
			 {
				 lstAllEmployeesNotAssigned.add(model);
			 }
		 }
		 lstEmployees=lstAllEmployeesNotAssigned;
	    	footerNotAssigned=lstEmployees.size();
	    	footerAssigned=0;
	    	footer=lstEmployees.size();
	    }
			
	 	@Command
	    @NotifyChange({"lstEmployees","footer"})
	    public void changeFilter() 
	    {	      
	    	lstEmployees=filterData();
	    	footer=lstEmployees.size();
	    }
	 
	 	private List<EmployeeModel> filterData()
		{
			lstEmployees=lstAllEmployees;
					
			List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
			for (Iterator<EmployeeModel> i = lstEmployees.iterator(); i.hasNext();)
			{
				EmployeeModel tmp=i.next();	
				if(tmp.getWorkGroupName()==null)
					tmp.setWorkGroupName("");
				if(tmp.getSupervisorName()==null)
					tmp.setSupervisorName("");
					
						if(tmp.getFullName().toLowerCase().contains(employeeFilter.getFullName().toLowerCase())&&
						tmp.getDepartment().toLowerCase().contains(employeeFilter.getDepartment().toLowerCase())&&
						tmp.getPosition().toLowerCase().contains(employeeFilter.getPosition().toLowerCase())&&
						tmp.getSuper_supervisorName().toLowerCase().contains(employeeFilter.getSuper_supervisorName().toLowerCase())&&
					//	tmp.getCountry().toLowerCase().contains(employeeFilter.getCountry().toLowerCase())&&
					//	tmp.getAge().toLowerCase().startsWith(employeeFilter.getAge().toLowerCase())&&
						tmp.getEmployeeNo().toLowerCase().contains(employeeFilter.getEmployeeNo().toLowerCase())&&
						tmp.getWorkGroupName().toLowerCase().startsWith(employeeFilter.getGroupName().toLowerCase())&&
						tmp.getSupervisorName().toLowerCase().contains(employeeFilter.getSupervisorName().toLowerCase())&&
						tmp.getStatus().toLowerCase().contains(employeeFilter.getStatus().toLowerCase())&&
						tmp.getEmployeeStatus().toLowerCase().contains(employeeFilter.getEmployeeStatus().toLowerCase())&&
						tmp.getStatusDescription().toLowerCase().contains(employeeFilter.getStatusDescription().toLowerCase())&&
						tmp.getProjectName().toLowerCase().contains(employeeFilter.getProjectName().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
			return lst;
			
		}
	 	
	@Command
	public void sendData(@ContextParam(ContextType.VIEW) Window comp) 
	{
		List<Integer> lstEmployeeId=new ArrayList<Integer>();
		String empKeys="";
		
		/*
		for (EmployeeModel item : lstEmployees) 
		{
		if(item.isCheckedEmployee() && item.getEmployeeKey()>0)
		{
			lstEmployeeId.add(item.getEmployeeKey());
		}
		}
		*/
		
		if(selectedEntities!=null)
		{
			for (EmployeeModel item : selectedEntities) 
			{
				lstEmployeeId.add(item.getEmployeeKey());
			}
			
		for (Integer empKey : lstEmployeeId) 
		{
			if(empKeys.equals(""))
			empKeys+=String.valueOf(empKey);
			else
			empKeys+=","+String.valueOf(empKey);
		}					
		
		}
		
		else if(lstEmployees.size()==1)
		{
			empKeys=String.valueOf(lstEmployees.get(0).getEmployeeKey());
		}
		
		if(empKeys.equals(""))
		{
			Messagebox.show("Please select employee!!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		Map args = new HashMap();
		args.put("myData", empKeys);	
		args.put("viewType", viewType);
		BindUtils.postGlobalCommand(null, null, "dlgClose", args);
		// Post command for Workgroup
		BindUtils.postGlobalCommand(null, null, "getemployeesId", args);
		//Post command for Shift Assgin used in AssignEmployeeToShift VM
		BindUtils.postGlobalCommand(null, null, "addSearchedEmployees", args);
		
		comp.detach();
	}
	
	@Command
    @NotifyChange({"lstEmployees","footer"})
	public void checkEmployeeCommand()
	{
		for (EmployeeModel item : lstEmployees) 
		{
			item.setCheckedEmployee(checkAllEmployee);
		}
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}


	public List<EmployeeModel> getLstEmployees() {
		return lstEmployees;
	}


	public void setLstEmployees(List<EmployeeModel> lstEmployees) {
		this.lstEmployees = lstEmployees;
	}

	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}

	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}

	public List<EmployeeModel> getLstAllEmployees() {
		return lstAllEmployees;
	}

	public void setLstAllEmployees(List<EmployeeModel> lstAllEmployees) {
		this.lstAllEmployees = lstAllEmployees;
	}

	public boolean isCheckAllEmployee() {
		return checkAllEmployee;
	}

	public void setCheckAllEmployee(boolean checkAllEmployee) {
		this.checkAllEmployee = checkAllEmployee;
	}

	public Set<EmployeeModel> getSelectedEntities() {
		return selectedEntities;
	}

	public void setSelectedEntities(Set<EmployeeModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
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
	 * @return the footerAssigned
	 */
	public int getFooterAssigned() {
		return footerAssigned;
	}

	/**
	 * @param footerAssigned the footerAssigned to set
	 */
	public void setFooterAssigned(int footerAssigned) {
		this.footerAssigned = footerAssigned;
	}

	/**
	 * @return the footerNotAssigned
	 */
	public int getFooterNotAssigned() {
		return footerNotAssigned;
	}

	/**
	 * @param footerNotAssigned the footerNotAssigned to set
	 */
	public void setFooterNotAssigned(int footerNotAssigned) {
		this.footerNotAssigned = footerNotAssigned;
	}


	/**
	 * @return the all
	 */
	public boolean isAll() {
		return all;
	}


	/**
	 * @param all the all to set
	 */
	public void setAll(boolean all) {
		this.all = all;
	}


	/**
	 * @return the assigned
	 */
	public boolean isAssigned() {
		return assigned;
	}


	/**
	 * @param assigned the assigned to set
	 */
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}


	/**
	 * @return the notAssigned
	 */
	public boolean isNotAssigned() {
		return notAssigned;
	}


	/**
	 * @param notAssigned the notAssigned to set
	 */
	public void setNotAssigned(boolean notAssigned) {
		this.notAssigned = notAssigned;
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
	@NotifyChange({"lstEmployees"})
	public void setSelectedWorkGroup(WorkGroupModel selectedWorkGroup) {
		this.selectedWorkGroup = selectedWorkGroup;
		if(selectedWorkGroup!=null)
		{
		List<EmployeeModel> groupList=new ArrayList<EmployeeModel>();
		for(EmployeeModel model:lstAllEmployees)
		 {
			
			if(model.getWorkGroupId()==0)
			{
				model.setWorkGroupName("");
			}
			if(model.getWorkGroupName().equalsIgnoreCase(selectedWorkGroup.getGroupName()))
			{
				groupList.add(model);
			}
		
			
		 }
		lstEmployees.clear();
		lstEmployees=groupList;
		}
	}
	
	
}
