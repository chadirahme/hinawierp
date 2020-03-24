package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.EmployeeModel;
import model.EmployeeShiftModel;
import model.ShiftModel;
import model.TimeSheetGridModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class AssignEmployeeToShift 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	HRData hrdata=new HRData();
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<EmployeeShiftModel> lstEmployee;
	private ListModelList<ShiftModel> lstShiftMaster;
	private Date fromDate;
	private Date toDate;
	
	private int supervisorID;
	int menuID=292;
	private MenuModel companyRole;
	
	private EmployeeShiftGroupAdapter shiftGroup;
	private String selectedViewType;
	
	
	public AssignEmployeeToShift()
	{
		try
		{
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
			
			Calendar c = Calendar.getInstance();	
			fromDate=sdf.parse(sdf.format(c.getTime()));
			toDate=sdf.parse(sdf.format(c.getTime()));
			
			selectedViewType="1";
			bindShiftList();
			
			/*
			lstEmployee=data.getAssignedEmployeesToShift(selectedCompany.getCompKey(), fromDate, toDate);			
			bindEmployeeShift();
			
			shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);
			*/
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> init", ex);			
		}
	}
	private void bindShiftList()
	{
		ShiftCreationData shiftData=new ShiftCreationData();
		//lstShiftMaster=shiftData.getShiftMasterList(selectedCompany.getCompKey());	
		lstShiftMaster=new ListModelList<ShiftModel>(shiftData.getShiftMasterList(selectedCompany.getCompKey() ,selectedViewType));
	}
	private void bindEmployeeShift()
	{
		for (EmployeeShiftModel item : lstEmployee)
		{
			
		boolean shiftHolidays[]=new boolean[lstShiftMaster.size()];
		int i=0;
		for (ShiftModel col : lstShiftMaster) 
		{
			if(item.getShiftkey()==col.getShiftKey())
			{			
			  shiftHolidays[i]=true;												
			}
			i++;
		}
		
		item.setEmployeeAssignShift(shiftHolidays);
		}
	}
	
	private void bindAddedEmployeeShift(List<EmployeeShiftModel> lstAddedEmployees)
	{
		for (EmployeeShiftModel item : lstAddedEmployees)
		{
			
		boolean shiftHolidays[]=new boolean[lstShiftMaster.size()];
		int i=0;
		for (ShiftModel col : lstShiftMaster) 
		{
			if(item.getShiftkey()==col.getShiftKey())
			{			
			  shiftHolidays[i]=true;												
			}
			i++;
		}
		
		item.setEmployeeAssignShift(shiftHolidays);
		}
	}
	
	@Command
	@NotifyChange({"shiftGroup","lstEmployee"})
	public void changeShiftCommand(@BindingParam("row") EmployeeShiftModel row,@BindingParam("shift") int shiftKey)
	{
		try
		{
		boolean shiftHolidays[]=new boolean[lstShiftMaster.size()];
		int i=0;
		if(selectedViewType.equals("1"))
		{
		for (EmployeeShiftModel item : lstEmployee) 
		{
			i=0;
			if(item.getEmployeeKey()==row.getEmployeeKey() && item.isOldShift()==false)
			{
				item.setEmployeeAssignShift(shiftHolidays);
				
				for (ShiftModel col : lstShiftMaster) 
				{
					if(col.getShiftKey()==shiftKey)
					{
						 shiftHolidays[i]=true;
					}
					i++;
				}
			}
		 }
	   }
		
		else
		{
			for (EmployeeShiftModel item : lstEmployee) 
			{
				i=0;
				if(item.getPositionID()==row.getPositionID() && item.isOldShift()==false)
				{
					item.setEmployeeAssignShift(shiftHolidays);
					
					for (ShiftModel col : lstShiftMaster) 
					{
						if(col.getShiftKey()==shiftKey)
						{
							 shiftHolidays[i]=true;
						}
						i++;
					}
				}
			 }
		}
		
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> changeShiftCommand", ex);			
		}
	}
	
	@Command
	@NotifyChange({"shiftGroup","lstEmployee","lstShiftMaster"})
	public void checkAllShiftCommand(@BindingParam("shift") int shiftKey ,@BindingParam("checkedValue")  boolean checkedValue)
	{
		try
		{
		boolean shiftHolidays[]=new boolean[lstShiftMaster.size()];
		
		for (EmployeeShiftModel item : lstEmployee) 
		{
			int i=0;
			if(item.isOldShift()==false)
			{
				item.setEmployeeAssignShift(shiftHolidays);
				
				for (ShiftModel col : lstShiftMaster) 
				{
					col.setShiftHeaderChecked(false);
					if(col.getShiftKey()==shiftKey)
					{
						  col.setShiftHeaderChecked(true);
						 shiftHolidays[i]=checkedValue;
					}
					i++;
				}
			}
		}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> checkAllShiftCommand", ex);			
		}
	}
	
	@Command
	@NotifyChange({"shiftGroup","lstEmployee"})
	public void viewCommand()
	{
		try
		{		
			if(selectedViewType.equals("1"))
			{
			lstEmployee=data.getAssignedEmployeesToShift(selectedCompany.getCompKey(), fromDate, toDate,null);			
			bindEmployeeShift();	
			
			List<EmployeeShiftModel> lst=new ArrayList<EmployeeShiftModel>();
			int employeeKey=0;
			for (EmployeeShiftModel item : lstEmployee) 
			{
				if(employeeKey!=item.getEmployeeKey())
				{				   
					employeeKey=item.getEmployeeKey();
				
				EmployeeShiftModel obj=new EmployeeShiftModel();
				obj.setCompanyKey(selectedCompany.getCompKey());
				obj.setEmployeeNo(item.getEmployeeNo());
				obj.setEnglishName(item.getEnglishName());
				obj.setEmployeeKey(item.getEmployeeKey());
				obj.setPosition(item.getPosition());
				obj.setPositionID(item.getPositionID());
				obj.setEmployeementDate(item.getEmployeementDate());
				obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(fromDate));
				obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(toDate));			
				lst.add(obj);
			    }
				lst.add(item);
			}
			
			lstEmployee=lst;
			shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);
			}
			
			else
			{
				lstEmployee=data.getAssignedPositionToShift(selectedCompany.getCompKey(), fromDate, toDate);
				bindEmployeeShift();	
				List<EmployeeShiftModel> lst=new ArrayList<EmployeeShiftModel>();
				int positionId=0;
				for (EmployeeShiftModel item : lstEmployee) 
				{
					if(positionId!=item.getPositionID())
					{
					 positionId=item.getPositionID();
					
					EmployeeShiftModel obj=new EmployeeShiftModel();
					obj.setCompanyKey(selectedCompany.getCompKey());
					obj.setPosition(item.getPosition());
					obj.setPositionID(item.getPositionID());
					obj.setEmployeementDate(item.getEmployeementDate());
					obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(fromDate));
					obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(toDate));			
					lst.add(obj);
					}
					lst.add(item);
				}
				lstEmployee=lst;
				logger.info("" + lstEmployee.size());
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> viewCommand", ex);			
		}	
	}
	@Command
	@NotifyChange({"shiftGroup","lstEmployee","lstShiftMaster"})
	public void selectViewTypeCommand()
	{
		ShiftCreationData shiftData=new ShiftCreationData();
		lstEmployee=new ArrayList<EmployeeShiftModel>();
		shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);
		lstShiftMaster=new ListModelList<ShiftModel>(shiftData.getShiftMasterList(selectedCompany.getCompKey(),selectedViewType));
	}
	
	@Command	
	public void addEmployeeCommand()
	{
		if(selectedViewType.equals("1"))
		{
			  Map<String,Object> arg = new HashMap<String,Object>();
			  arg.put("compKey", selectedCompany.getCompKey());
			  arg.put("type", "T");
			  arg.put("viewType", "1");
			  Executions.createComponents("/timesheet/searchemployee.zul", null,arg);
		}
		else
		{
			 Messagebox.show("Please select dispaly by employee !!","Employee Shift", Messagebox.OK , Messagebox.EXCLAMATION);			
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"shiftGroup","lstEmployee"})
	public void addSearchedEmployees(@BindingParam("myData")String empKeys)
	{
		try
		{
		if(!empKeys.equals(""))
		{
			  String tmpNewKeys="";	
			  if(lstEmployee==null)
			  {
				 lstEmployee=new ArrayList<EmployeeShiftModel>();
			     tmpNewKeys=empKeys;
			  }
			  else
			  {
					List<Integer> lstOldEmpKeys=new ArrayList<Integer>();
					for (EmployeeShiftModel item : lstEmployee) 
					{
						lstOldEmpKeys.add(item.getEmployeeKey());
					}
					String[] tmpKeys= empKeys.split(",");	
					for (int i = 0; i < tmpKeys.length; i++) 
					{					
					    if(!lstOldEmpKeys.contains(Integer.parseInt(tmpKeys[i])))
					    {
					    	if(tmpNewKeys.equals(""))
					    		tmpNewKeys=tmpKeys[i];
					    	else
					    		tmpNewKeys+="," + tmpKeys[i];
					    }				    	
					}											
			  }
			  
			  //add employee to grid
			  if(!tmpNewKeys.equals(""))
			  {
			   List<EmployeeShiftModel> lstNewAddedEmployees=data.getNewAssignedEmployeesToShift(tmpNewKeys);
				  
			  List<EmployeeShiftModel> lstAddedEmployees=data.getAssignedEmployeesToShift(selectedCompany.getCompKey(), fromDate, toDate,tmpNewKeys);			
			  bindAddedEmployeeShift(lstAddedEmployees);
				
			  List<EmployeeShiftModel> lst=new ArrayList<EmployeeShiftModel>();
			  
			  for (EmployeeShiftModel emp : lstNewAddedEmployees) 
			  {
				    EmployeeShiftModel obj=new EmployeeShiftModel();
				    obj.setCompanyKey(selectedCompany.getCompKey());
					obj.setEmployeeNo(emp.getEmployeeNo());
					obj.setEnglishName(emp.getEnglishName());
					obj.setEmployeeKey(emp.getEmployeeKey());
					obj.setPosition(emp.getPosition());
					obj.setPositionID(emp.getPositionID());
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(fromDate));
					obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(toDate));	
					lst.add(obj);
			  
			  //check if there is old Data
				for (EmployeeShiftModel item : lstAddedEmployees) 
				{
					if(item.getEmployeeKey()==emp.getEmployeeKey())
					{
						lst.add(item);
					}				
				}
				
			  }
				lstEmployee.addAll(lst);
				Collections.sort(lstEmployee);
				shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);
			  }
		}
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> addSearchedEmployees", ex);			
		}
	}
	
	@Command
	@NotifyChange({"shiftGroup"})
	public void addShiftCommand_NOTUSED()
	{
		try
		{
		List<EmployeeShiftModel> lst=new ArrayList<EmployeeShiftModel>();
		
		for (EmployeeShiftModel item : lstEmployee) 
		{
			EmployeeShiftModel obj=new EmployeeShiftModel();
			obj.setEmployeeNo(item.getEmployeeNo());
			obj.setEnglishName(item.getEnglishName());
			obj.setEmployeeKey(item.getEmployeeKey());
			obj.setPosition(item.getPosition());
			obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(fromDate));
			obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(toDate));			
			lst.add(obj);
			
			lst.add(item);
		}
		
		lstEmployee=lst;
		shiftGroup=new EmployeeShiftGroupAdapter(lst, new EmployeeShiftComparator(), true);
		
		/*int len=lstEmployee.size();
		for (int i = 0; i < len; i++)
		{
			EmployeeShiftModel obj=new EmployeeShiftModel();
			obj.setEmployeeKey(2);
			obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(fromDate));
			obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(toDate));			
			lstEmployee.add(i+1,obj);
		}*/
		
		/*for (EmployeeModel item : lstEmployee) 
		{
			EmployeeModel obj=new EmployeeModel();
			obj.setEmployeeKey(item.getEmployeeKey());
			obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(fromDate));
			obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(toDate));			
			lstEmployee.add(obj);
		}*/
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> addShiftCommand", ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstEmployee","shiftGroup"})
	public void saveAssignedEmployeeToShiftCommand()
	{
		try
		{	
			String msg="";
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			if(checkSelectedShifts()==false)
			{
				 Messagebox.show("One or more employees have not been assigned any shift, please check.","Employee Shift", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			}
			for (EmployeeShiftModel item : lstEmployee) 
			{
				int i=0;
				boolean shiftHolidays[]= item.getEmployeeAssignShift();
				for (boolean b : shiftHolidays) 
				{					
					if(b)
					{
						//logger.info(item.getEmployeeKey() + " " + i);
						break;
					}
					i++;
				}
				
				item.setShiftkey(lstShiftMaster.get(i).getShiftKey());				
				//logger.info(item.getEmployeeKey() + " " + item.getShiftkey());				
			}
			
			for (EmployeeShiftModel item : lstEmployee) 
			{
				//check the employeent Date
				if(selectedViewType.equals("1") && item.getEmployeementDate().after(toDate))
				{
					msg="The Joining Date of Some Employees are after the Shift Assign To Date.These Employees will not Save in the Shift.";
					continue;
				}
				if(item.isOldShift()==false)
				data.addEmployeeShift(item);
				else
				{
					Date shiftFromDate= df.parse(item.getShiftFromDate());
					Date shiftToDate= df.parse(item.getShiftToDate());
					if(shiftFromDate.before(fromDate) && shiftToDate.after(toDate))
					{
						data.updateEmployeeShift(item,fromDate,toDate);
					}
					if(shiftFromDate.before(fromDate) && shiftToDate.before(toDate))
					{
						data.updateEmployeeShift(item,fromDate,toDate);
					}
					if(shiftFromDate.equals(fromDate) || shiftToDate.equals(toDate))
					{
						data.deleteEmployeeShift(item);
					}
					if(shiftFromDate.after(fromDate) && shiftToDate.before(toDate))
					{
						data.deleteEmployeeShift(item);
					}
				}
			}
			if(!msg.equals(""))
				Messagebox.show(msg,"Employee Shift", Messagebox.OK , Messagebox.INFORMATION);
			 else
			 Messagebox.show("Records Saved Successfully","Employee Shift", Messagebox.OK , Messagebox.INFORMATION);
			
			 lstEmployee=new ArrayList<EmployeeShiftModel>();
			  shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);	
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> saveAssignedEmployeeToShiftCommand", ex);			
		}
	}
	private boolean checkSelectedShifts()
	{
		boolean isValid=true;
		for (EmployeeShiftModel item : lstEmployee) 
		{
			if(item.getEmployeeAssignShift()==null)
				return false;
		}
		return isValid;
	}
	
	  @Command
	  @NotifyChange({"lstEmployee","shiftGroup"})
	  public void clearCommand()
	  {
		 
		  lstEmployee=new ArrayList<EmployeeShiftModel>();
		  shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);		 
	  }

	private void getCompanyRolePermessions(int companyRoleId)
	{
		setCompanyRole(new MenuModel());
		
		List<MenuModel> lstRoles= data.getTimeSheetRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				setCompanyRole(item);
				break;
			}
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

	
	@NotifyChange({"shiftGroup","lstEmployee","lstShiftMaster"})
	//@NotifyChange("*")
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		ShiftCreationData shiftData=new ShiftCreationData();
		lstShiftMaster=new ListModelList<ShiftModel>(shiftData.getShiftMasterList(selectedCompany.getCompKey(),selectedViewType));
		lstEmployee=new ArrayList<EmployeeShiftModel>();
		shiftGroup=new EmployeeShiftGroupAdapter(lstEmployee, new EmployeeShiftComparator(), true);	
	}
	
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	public List<EmployeeShiftModel> getLstEmployee() {
		return lstEmployee;
	}
	public void setLstEmployee(List<EmployeeShiftModel> lstEmployee) {
		this.lstEmployee = lstEmployee;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public ListModelList<ShiftModel> getLstShiftMaster() {
		return lstShiftMaster;
	}
	public void setLstShiftMaster(ListModelList<ShiftModel> lstShiftMaster) {
		this.lstShiftMaster = lstShiftMaster;
	}
	public EmployeeShiftGroupAdapter getShiftGroup() {
		return shiftGroup;
	}
	public void setShiftGroup(EmployeeShiftGroupAdapter shiftGroup) {
		this.shiftGroup = shiftGroup;
	}
	public String getSelectedViewType() {
		return selectedViewType;
	}
	public void setSelectedViewType(String selectedViewType) {
		this.selectedViewType = selectedViewType;
	}
}
