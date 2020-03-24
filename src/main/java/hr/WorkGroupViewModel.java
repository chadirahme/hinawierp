package hr;

import hr.model.CompanyModel;
import hr.model.SponsorModel;
import hr.model.WorkGroupModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.EmployeeFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;



/**
 * This controller class is used for creating work groups for employees.
 * The employees can be under supervisor or super supervisor. 
 * The constructor loads all the data required for drop downs ,session. 
 */


public class WorkGroupViewModel {

	private Logger logger = Logger.getLogger(this.getClass());

	HRData data = new HRData();
	WorkGroupData workGroupData = new WorkGroupData();
	private List<WorkGroupModel> lstWorkGroup;
	private WorkGroupModel selectedWorkGroup;
	private List<WorkGroupModel> lstAllWorkGroup;
	private EmployeeFilter employeeFilter = new EmployeeFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private boolean adminUser;
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	WebusersModel dbUser;

	private List<String> lstStatus;
	private String selectedStatus;

	int menuID=254;
	private MenuModel companyRole;
	
	public WorkGroupViewModel() {

		lstStatus = new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		selectedStatus = lstStatus.get(0);
		Session sess = Sessions.getCurrent();
		dbUser = (WebusersModel) sess.getAttribute("Authentication");
		getCompanyRolePermessions(dbUser.getCompanyroleid());
		
		if (dbUser != null) {
			adminUser = dbUser.getFirstname().equals("admin");
		}
		int defaultCompanyId = 0;
		defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());
		lstComapnies = data.getCompanyList(dbUser.getUserid());
		
		for (CompanyModel item : lstComapnies) 
		{
		if(item.getCompKey()==defaultCompanyId)
			selectedCompany=item;
		}
		/*
		 * for (CompanyModel item : lstComapnies) {
		 * if(item.getCompKey()==defaultCompanyId) selectedCompany=item; }
		 * if(lstComapnies.size()>1 && defaultCompanyId==0)
		 */
		if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);

		lstWorkGroup = workGroupData.fillWorkGroupInfo(
				selectedCompany.getCompKey(), "");

		if (lstWorkGroup.size() > 0)
			selectedWorkGroup = lstWorkGroup.get(0);
		lstAllWorkGroup = lstWorkGroup;

		lstPageSize = new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		lstPageSize.add(lstWorkGroup.size());

		selectedPageSize = lstPageSize.get(2);

		lstAllPageSize = new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize = lstAllPageSize.get(2);

	}
	
	
	/**
	 * This Method returns the roles for the logged in user. 
	 * 
	 * @param  companyRoleId  company role id from mysql database 
	 * @param 
	 * @return void
	 * @see         level of acess 
	 */
	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();		
		List<MenuModel> lstRoles= data.getHRRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				companyRole=item;
				break;
			}
		}
	}

	/**
	 * This Method used to make filter(Search) in the Listbox filters  
	 * 
	 * @param   
	 * @param 
	 * @return List<WorkGroupModel> list of filtered data
	 * 			
	 * @see         filtered data 
	 */
	private List<WorkGroupModel> filterData() {
		lstWorkGroup = lstAllWorkGroup;

		List<WorkGroupModel> lst = new ArrayList<WorkGroupModel>();
		for (Iterator<WorkGroupModel> i = lstWorkGroup.iterator(); i.hasNext();) {
			WorkGroupModel tmp = i.next();
			if (tmp.getGroupName().toLowerCase()
					.contains(employeeFilter.getGroupName().toLowerCase())
					&& tmp.getSupervisorName()
							.toLowerCase()
							.contains(
									employeeFilter.getSupervisorName()
											.toLowerCase())
					&& 
					 tmp.getSupervisorNumber()
					.toLowerCase()
					.contains(
							employeeFilter.getSupervisorNumber()
									.toLowerCase())
			&& tmp.getIsActive()
							.toLowerCase()
							.contains(
									employeeFilter.getIsActive().toLowerCase())) {
				lst.add(tmp);
			}
		}
		return lst;

	}

	
	/**
	 * This Method used to make reset the list to the default setup. 
	 * 
	 * @param   no arguments 
	 * @param 
	 * @return void
	 * 			
	 * @see        Default data 
	 */
	
	@Command
	public void resetWorkGroup() {
		try {
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox = (Tabbox) center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();
		} catch (Exception ex) {
			logger.error("ERROR in WorkGroupViewModel ----> resetWorkGroup", ex);
		}
	}

	
	/**
	 * This Method used to call edit of Work group.  
	 * 
	 * @param   WorkGroupModel row object containing the data for edit 
	 * @param 
	 * @return void
	 * 			
	 * @see        Edit existing work group. 
	 */
	@Command
	public void editWorkGroupCommand(@BindingParam("row") WorkGroupModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("workGroup", row.getRecno());
			arg.put("workGroupName", row.getGroupName());
			arg.put("compName", selectedCompany.getEnCompanyName());
			arg.put("compKey", selectedCompany.getCompKey());
			arg.put("type", "edit");
			Executions
					.createComponents("/hr/list/editWorkGroup.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in WorkGroupViewModel ----> editWorkGroupCommand",
					ex);
		}
	}

	/**
	 * This Method used to call View of Work group.  
	 * 
	 * @param   WorkGroupModel row object containing the data for edit 
	 * @param 
	 * @return void
	 * 			
	 * @see        View existing work group. 
	 */
	@Command
	public void viewWorkGroupCommand(@BindingParam("row") WorkGroupModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("workGroup", row.getRecno());
			arg.put("compKey", selectedCompany.getCompKey());
			arg.put("workGroupName", row.getGroupName());
			arg.put("compName", selectedCompany.getEnCompanyName());
			arg.put("type", "view");
			Executions
					.createComponents("/hr/list/editWorkGroup.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in WorkGroupViewModel ----> viewWorkGroupCommand",
					ex);
		}
	}

	/**
	 * This Method used to call Add of Work group.  
	 * 
	 * @param   no arguments  
	 * @param 
	 * @return void
	 * 			
	 * @see        Add new work group. 
	 */
	@Command
	public void addWorkGroupCommand() {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("workGroup", 0);
			arg.put("compKey", selectedCompany.getCompKey());
			arg.put("compName", selectedCompany.getEnCompanyName());
			arg.put("type", "Add");
			Executions
					.createComponents("/hr/list/editWorkGroup.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in WorkGroupViewModel ----> addWorkGroupCommand", ex);
		}
	}

	
	/**
	 * This Method used to call delete of Work group.  
	 * 
	 * @param  WorkGroupModel row object containing the data for delete  
	 * @param 
	 * @return void
	 * 			
	 * @see     Delete existing work group. 
	 */
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange({ "lstWorkGroup", "footer" })
	public void deleteWorkGroup(@BindingParam("row") final WorkGroupModel row) {
		
		Messagebox.show("Do you want to delete "+selectedWorkGroup.getGroupName()+" and employees assosiated with team ?","Team List", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
				new org.zkoss.zk.ui.event.EventListener() {						
			    public void onEvent(Event evt) throws InterruptedException {
			    	if (evt.getName().equals("onYes")) 
			        {
			    		if (row != null) {

			    			workGroupData.deleteWorkGroup(row.getRecno());
			    			 if(selectedWorkGroup.isWetherSupersupervisor())
			    			 {
			    					workGroupData.deleteWorkGroupSuperSupervisor(row.getSupersupervisor());
			    			 }
			    			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			    			Center center = bl.getCenter();
			    			Tabbox tabbox = (Tabbox) center.getFellow("mainContentTabbox");
			    			tabbox.getSelectedPanel().getLastChild().invalidate();
			    		}
			        }
			    	else
			    	{
			    		
			    	}
			    }
		});
			
		
		
	}

	/**
	 * This Method used to refresh the work group list to default data   
	 * 
	 * @param  no  arguments 
	 * @param 
	 * @return void
	 * 			
	 * @see     Refresh work group to default. 
	 */
	@GlobalCommand
	@NotifyChange({ "lstWorkGroup", "footer" })
	public void refreshParentWorkGroup(@BindingParam("type") String type) {
		try {
			lstWorkGroup = workGroupData.fillWorkGroupInfo(
					selectedCompany.getCompKey(), "");

			if (lstWorkGroup.size() > 0)
				selectedWorkGroup = lstWorkGroup.get(0);
			lstAllWorkGroup = lstWorkGroup;

		} catch (Exception ex) {
			logger.error(
					"ERROR in WorkGroupViewModel ----> refreshParentWorkGroup",
					ex);
		}
	}

	@Command
	@NotifyChange({ "lstWorkGroup", "footer" })
	public void changeFilter() {
		lstWorkGroup = filterData();
	}

	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	@NotifyChange({ "selectedPageSize" })
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if (selectedAllPageSize.equals("All"))
			selectedPageSize = lstWorkGroup.size();
		else
			selectedPageSize = Integer.parseInt(selectedAllPageSize);
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

	
	/**
	 * This Method used to get the work group data based on selected company key.   
	 * 
	 * @param  selectedCompany selected company. 
	 * @param 
	 * @return void
	 * 			
	 * @see     list work group data based on company key. 
	 */
	@NotifyChange({ "lstWorkGroup", "selectedWorkGroup", "footer" })
	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
		lstWorkGroup = workGroupData.fillWorkGroupInfo(
				selectedCompany.getCompKey(), "");
		if (lstWorkGroup.size() > 0)
			selectedWorkGroup = lstWorkGroup.get(0);
		lstAllWorkGroup = lstWorkGroup;

	}

	/**
	 * @return the lstWorkGroup
	 */
	public List<WorkGroupModel> getLstWorkGroup() {
		return lstWorkGroup;
	}

	/**
	 * @param lstWorkGroup
	 *            the lstWorkGroup to set
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
	 * @param selectedWorkGroup
	 *            the selectedWorkGroup to set
	 */
	public void setSelectedWorkGroup(WorkGroupModel selectedWorkGroup) {
		this.selectedWorkGroup = selectedWorkGroup;
	}

	/**
	 * @return the lstAllWorkGroup
	 */
	public List<WorkGroupModel> getLstAllWorkGroup() {
		return lstAllWorkGroup;
	}

	/**
	 * @param lstAllWorkGroup
	 *            the lstAllWorkGroup to set
	 */
	public void setLstAllWorkGroup(List<WorkGroupModel> lstAllWorkGroup) {
		this.lstAllWorkGroup = lstAllWorkGroup;
	}

	/**
	 * @return the employeeFilter
	 */
	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}

	/**
	 * @param employeeFilter
	 *            the employeeFilter to set
	 */
	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}

	/**
	 * @return the lstStatus
	 */
	public List<String> getLstStatus() {
		return lstStatus;
	}

	/**
	 * @param lstStatus
	 *            the lstStatus to set
	 */
	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * This Method used to get the work group data based on selected status(Active ,In Active ,All).   
	 * 
	 * @param  selectedStatus selected Status. 
	 * @param 
	 * @return void
	 * 			
	 * @see  list work group data based on Status key. 
	 */
	@NotifyChange({ "lstWorkGroup" })
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;

		String status = "";
		if (selectedStatus.equalsIgnoreCase("Active"))
			status = "Y";
		else if (selectedStatus.equalsIgnoreCase("Inactive"))
			status = "N";
		else if (selectedStatus.equalsIgnoreCase("All"))
			status = "";

		lstWorkGroup = workGroupData.fillWorkGroupInfo(
				selectedCompany.getCompKey(), status);

		if (lstWorkGroup.size() > 0)
			selectedWorkGroup = lstWorkGroup.get(0);
		lstAllWorkGroup = lstWorkGroup;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

}
