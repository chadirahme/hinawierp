package hr;

import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import layout.MenuModel;
import model.CheckItemsModel;
import model.EmployeeFilter;
import model.EmployeeModel;
import model.FixedAssetModel;
import model.HRListValuesModel;
import model.VendorModel;

public class HRViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	private List<EmployeeModel> lstEmployees;
	private EmployeeModel selectedEmployee;
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	private List<EmployeeModel> lstAllEmployees;
	private static final String footerMessage = "A Total of %d Employees";
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private boolean adminUser;
	
	private List<HRListValuesModel> lstDepartment;
	private HRListValuesModel selectedDepartment;
	
	private List<HRListValuesModel> lstPosition;
	private HRListValuesModel selectedPosition;
	
	private List<HRListValuesModel> lstNationality;
	private HRListValuesModel selectedNationality;
	
	private Date dateofbirth;
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstStatus;
	private String selectedStatus;
	private int supervisorID;
	
	private int employeeKey;
	
	int menuID=14;
	private MenuModel companyRole;
	
	
	private Set<EmployeeModel> selectedEmployeeEntities;
	
	public HRViewModel()
	{
		try
		{
		//lstDepartment=data.getHRListValues(6);
		//lstPosition=data.getHRListValues(7);
		//lstNationality=data.getHRListValues(1);
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		getCompanyRolePermessions(dbUser.getCompanyroleid());
		
		int defaultCompanyId=0;
		defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());
		lstComapnies=data.getCompanyList(dbUser.getUserid());
		for (CompanyModel item : lstComapnies) 
		{
		if(item.getCompKey()==defaultCompanyId)
			selectedCompany=item;
		}
		if(lstComapnies.size()>=1 && defaultCompanyId==0)			
		selectedCompany=lstComapnies.get(0);
		
		FillStatusList();
		
		supervisorID=dbUser.getSupervisor();
		
		employeeKey=dbUser.getEmployeeKey();
		
		if(employeeKey>0)
			supervisorID=employeeKey;
		
		if(selectedCompany!=null)
		lstEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"","A",supervisorID);
		else
		lstEmployees=data.getEmployeeList(0,"","A",supervisorID);
		
		if(lstEmployees.size()>0)
		{
		selectedEmployee=lstEmployees.get(0);
		dateofbirth=selectedEmployee.getDateOfBirth();
		}
		lstAllEmployees=lstEmployees;	
		
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		lstPageSize.add(lstEmployees.size());
		
		selectedPageSize=lstPageSize.get(2);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		
		/*
		for (HRListValuesModel item : lstDepartment) 
		{
		if(item.getListId()==selectedEmployee.getDepartmentID())
		{
			selectedDepartment=item;
		}
		
		}
		
		for (HRListValuesModel item : lstPosition) 
		{
		if(item.getListId()==selectedEmployee.getPositionID())
		{
			selectedPosition=item;
		}
		
		}
		
		for (HRListValuesModel item : lstNationality) 
		{
		if(item.getListId()==selectedEmployee.getNationalityID())
		{
			selectedNationality=item;
		}
		
		}
		*/						
		//Session sess = Sessions.getCurrent();
		//WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
		}
	   }
		catch (Exception ex)
		{	
			logger.error("ERROR in HRViewModel ----> Init", ex);			
		}
		
	}
	
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

		private void FillStatusList()
		{
		lstStatus=new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("Inactive");
		lstStatus.add("EOS");
		selectedStatus=lstStatus.get(1);
		}
	
	   @Command
	   @NotifyChange({"lstEmployees","footer"})
	   public void updateEmployee()
	   {
		selectedEmployee.setDepartment(selectedDepartment.getEnDescription());
		selectedEmployee.setDepartmentID(selectedDepartment.getListId());
		selectedEmployee.setFullName(selectedEmployee.getEnFirstName() + " " + selectedEmployee.getEnMiddleName() + " " + selectedEmployee.getEnLastName());
		selectedEmployee.setArabicName(selectedEmployee.getArFirstName() + " " + selectedEmployee.getArMiddleName() + " " + selectedEmployee.getArLastName());
		selectedEmployee.setPosition(selectedPosition.getEnDescription());
		selectedEmployee.setPositionID(selectedPosition.getListId());
		selectedEmployee.setCountry(selectedNationality.getEnDescription());
		selectedEmployee.setNationalityID(selectedNationality.getListId());
		selectedEmployee.setDateOfBirth(dateofbirth);
		data.UpdateEmployees(selectedEmployee);
	
		Messagebox.show("Employee is Updated..","Update Employee",Messagebox.OK , Messagebox.INFORMATION);
		
	   }
	   
	   @Command
	   public void resetEmployee()
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
				logger.error("ERROR in HRViewModel ----> resetEmployee", ex);			
			}
	   }
	   
	   @Command
	   public void viewEmployeeCommand(@BindingParam("row") EmployeeModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("empKey", row.getEmployeeKey());
			   arg.put("compKey",selectedCompany.getCompKey());
			   arg.put("type","view");
			   Executions.createComponents("/hr/list/editemployee.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HRViewModel ----> viewEmployeeCommand", ex);			
			}
	   }
	   
	   @Command
	   public void editEmployeeCommand(@BindingParam("row") EmployeeModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("empKey", row.getEmployeeKey());
			   arg.put("compKey",selectedCompany.getCompKey());
			   arg.put("type","edit");
			   Executions.createComponents("/hr/list/editemployee.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HRViewModel ----> editEmployeeCommand", ex);			
			}
	   }
	   
	   @Command
	   public void addEmployeeCommand()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("empKey", 0);
			   arg.put("compKey", selectedCompany.getCompKey());
			   arg.put("type","add");
			   Executions.createComponents("/hr/list/editemployee.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HRViewModel ----> addEmployeeCommand", ex);			
			}
	   }
	   
	  @GlobalCommand 
	  @NotifyChange({"lstEmployees"})
		  public void refreshParent(@BindingParam("compKey")int compKey)
		  {		
			 try
			  {
				if(compKey>0)
				{					
					String status="";
					if(selectedStatus.equals("Active"))
						status="A";
					else if(selectedStatus.equals("Inactive"))
						status="I";
					else if(selectedStatus.equals("EOS"))
						status="Y";
					
					if(selectedCompany!=null)
					lstEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"",status,supervisorID);
					else
					lstEmployees=data.getEmployeeList(0,"",status,supervisorID);
					
					//lstEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"",status,supervisorID);								
					lstAllEmployees=lstEmployees;	
				}
			  }
			 catch (Exception ex)
				{	
				logger.error("ERROR in HRViewModel ----> refreshParent", ex);			
				}
		  }
	
	public String getFooter() {
        return String.format(footerMessage, lstEmployees.size());
    }
	
	public List<EmployeeModel> getLstEmployees() {
		return lstEmployees;
	}


	public void setLstEmployees(List<EmployeeModel> lstEmployees) {
		this.lstEmployees = lstEmployees;
	}


	public EmployeeModel getSelectedEmployee() {
		return selectedEmployee;
	}
	
	@Command
	@NotifyChange({"selectedEmployee"})	
	public void changeBirthDate()
	{		
		Calendar dob = Calendar.getInstance();
		dob.setTime(dateofbirth);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
		age--;
		
		if(age<=0)
			age=0;
		
		selectedEmployee.setAge(String.valueOf(age));
	}

	//@NotifyChange({"selectedDepartment","selectedEmployee","selectedPosition","selectedNationality","dateofbirth"})	
	public void setSelectedEmployee(EmployeeModel selectedEmployee) 
	{
		this.selectedEmployee = selectedEmployee;
		//dateofbirth=selectedEmployee.getDateOfBirth();
		
		/*
		for (HRListValuesModel item : lstDepartment) 
		{
		if(item.getListId()==selectedEmployee.getDepartmentID())
		{
			selectedDepartment=item;
		}
		
		}
		
		for (HRListValuesModel item : lstPosition) 
		{
		if(item.getListId()==selectedEmployee.getPositionID())
		{
			selectedPosition=item;
		}
		
		}
		
		for (HRListValuesModel item : lstNationality) 
		{
		if(item.getListId()==selectedEmployee.getNationalityID())
		{
			selectedNationality=item;
		}
		
		}
		*/
	}


	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}


	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}
	
	private List<EmployeeModel> filterData()
	{
		lstEmployees=lstAllEmployees;
				
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		for (Iterator<EmployeeModel> i = lstEmployees.iterator(); i.hasNext();)
		{
			EmployeeModel tmp=i.next();				
					if(tmp.getFullName().toLowerCase().contains(employeeFilter.getFullName().toLowerCase())&&
					tmp.getDepartment().toLowerCase().contains(employeeFilter.getDepartment().toLowerCase())&&
					tmp.getPosition().toLowerCase().contains(employeeFilter.getPosition().toLowerCase())&&
					tmp.getSuper_supervisorName().toLowerCase().contains(employeeFilter.getSuper_supervisorName().toLowerCase())&&
					tmp.getCountry().toLowerCase().contains(employeeFilter.getCountry().toLowerCase())&&
					tmp.getAge().toLowerCase().startsWith(employeeFilter.getAge().toLowerCase())&&
					tmp.getEmployeeNo().toLowerCase().contains(employeeFilter.getEmployeeNo().toLowerCase())&&
					//tmp.getEnFirstName().toLowerCase().contains(employeeFilter.getEnFirstName().toLowerCase())&&
					//tmp.getEnMiddleName().toLowerCase().contains(employeeFilter.getEnMiddleName().toLowerCase())&&
					tmp.getStatus().toLowerCase().contains(employeeFilter.getStatus().toLowerCase())&&
					tmp.getGender().toLowerCase().startsWith(employeeFilter.getGender().toLowerCase())&&
					tmp.getMaritalStatus().toLowerCase().contains(employeeFilter.getMaritalStatus().toLowerCase())&&
					tmp.getEmployeeStatus().toLowerCase().contains(employeeFilter.getEmployeeStatus().toLowerCase())&&
					tmp.getWorkGroupName().toLowerCase().startsWith(employeeFilter.getGroupName().toLowerCase())&&
					tmp.getSupervisorName().toLowerCase().contains(employeeFilter.getSupervisorName().toLowerCase())&&
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
	    @NotifyChange({"lstEmployees","footer"})
	    public void changeFilter() 
	    {	      
	    	lstEmployees=filterData();
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

		public List<HRListValuesModel> getLstDepartment() {
			return lstDepartment;
		}

		public void setLstDepartment(List<HRListValuesModel> lstDepartment) {
			this.lstDepartment = lstDepartment;
		}

		public HRListValuesModel getSelectedDepartment() {
			return selectedDepartment;
		}

		public void setSelectedDepartment(HRListValuesModel selectedDepartment) {
			this.selectedDepartment = selectedDepartment;
		}

		public List<HRListValuesModel> getLstPosition() {
			return lstPosition;
		}

		public void setLstPosition(List<HRListValuesModel> lstPosition) {
			this.lstPosition = lstPosition;
		}

		public HRListValuesModel getSelectedPosition() {
			return selectedPosition;
		}

		public void setSelectedPosition(HRListValuesModel selectedPosition) {
			this.selectedPosition = selectedPosition;
		}

		public List<HRListValuesModel> getLstNationality() {
			return lstNationality;
		}

		public void setLstNationality(List<HRListValuesModel> lstNationality) {
			this.lstNationality = lstNationality;
		}

		public HRListValuesModel getSelectedNationality() {
			return selectedNationality;
		}

		public void setSelectedNationality(HRListValuesModel selectedNationality) {
			this.selectedNationality = selectedNationality;
		}

		public Date getDateofbirth() {
			return dateofbirth;
		}

		public void setDateofbirth(Date dateofbirth) {
			this.dateofbirth = dateofbirth;
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
		@NotifyChange({"selectedPageSize"})	
		public void setSelectedAllPageSize(String selectedAllPageSize) 
		{
			this.selectedAllPageSize = selectedAllPageSize;
			if(selectedAllPageSize.equals("All"))
			{
				if(lstEmployees.size()>0)
				selectedPageSize=lstEmployees.size();
			}
			else
				selectedPageSize=Integer.parseInt(selectedAllPageSize);
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

		@NotifyChange({"lstEmployees","selectedEmployee","footer"})	
		public void setSelectedCompany(CompanyModel selectedCompany) 
		{
			this.selectedCompany = selectedCompany;
			String status="";
			if(selectedStatus.equals("Active"))
				status="A";
			else if(selectedStatus.equals("Inactive"))
				status="I";
			else if(selectedStatus.equals("EOS"))
				status="Y";
			
			lstEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"",status,supervisorID);			
			if(lstEmployees.size()>0)
			selectedEmployee=lstEmployees.get(0);
			lstAllEmployees=lstEmployees;	
		}

		public List<String> getLstStatus() {
			return lstStatus;
		}

		public void setLstStatus(List<String> lstStatus) {
			this.lstStatus = lstStatus;
		}

		public String getSelectedStatus() {
			return selectedStatus;
		}

		@NotifyChange({"lstEmployees","selectedEmployee","footer"})	
		public void setSelectedStatus(String selectedStatus) 
		{
			this.selectedStatus = selectedStatus;
			String status="";
			if(selectedStatus.equals("Active"))
				status="A";
			else if(selectedStatus.equals("Inactive"))
				status="I";
			else if(selectedStatus.equals("EOS"))
				status="Y";
			
			
			if(selectedCompany!=null)
				lstEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"",status,supervisorID);
				else
				lstEmployees=data.getEmployeeList(0,"",status,supervisorID);
			//lstEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"",status,supervisorID);			
			if(lstEmployees.size()>0)
			selectedEmployee=lstEmployees.get(0);
			lstAllEmployees=lstEmployees;	
		}

		public MenuModel getCompanyRole() {
			return companyRole;
		}

		public void setCompanyRole(MenuModel companyRole) {
			this.companyRole = companyRole;
		}

		/**
		 * @return the selectedEmployeeEntities
		 */
		public Set<EmployeeModel> getSelectedEmployeeEntities() {
			return selectedEmployeeEntities;
		}

		/**
		 * @param selectedEmployeeEntities the selectedEmployeeEntities to set
		 */
		public void setSelectedEmployeeEntities(
				Set<EmployeeModel> selectedEmployeeEntities) {
			this.selectedEmployeeEntities = selectedEmployeeEntities;
		}
		
		
		
		 @Command
			public void selectEmployeesForSendEmails(@BindingParam("cmp") Window comp) 
			{
				List<Integer> lstEmployeeKey=new ArrayList<Integer>();
				String EmpployeeKeys="";
				if(selectedEmployeeEntities!=null)
				{
					for (EmployeeModel item : selectedEmployeeEntities) 
					{
						lstEmployeeKey.add(item.getEmployeeKey());
					}
					
				for (Integer empKey : lstEmployeeKey) 
				{
					if(EmpployeeKeys.equals(""))
						EmpployeeKeys+=String.valueOf(empKey);
					else
						EmpployeeKeys+=","+String.valueOf(empKey);
				}					
				
				}
				
				else if(lstEmployeeKey.size()==1)
				{
					EmpployeeKeys=String.valueOf(lstEmployees.get(0).getEmployeeKey());
				}
				
				if(EmpployeeKeys.equals(""))
				{
					Messagebox.show("Please select Employees!!","Employees", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				Map args = new HashMap();
				args.put("myData", EmpployeeKeys);	
				args.put("slectedEmployeeObject", selectedEmployeeEntities);	
				BindUtils.postGlobalCommand(null, null, "getEmployeeIDsForSendEmail", args);
				comp.detach();
			}
		
		
		
}
