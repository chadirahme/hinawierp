package payroll;


import hr.model.CompanyModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DraftSalaryModel;
import model.EmployeeModel;


import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class DraftSalarySheetViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	//HRData hrdata=new HRData();
	DraftSalarySheetData data=new DraftSalarySheetData();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstMonths;
	private List<String> lstYears;
	private int selectedMonth;
	private String selectedYear;
	private Date salaryDate;
	private int totalNoofEmployee;
	private List<DraftSalaryModel> lstDraftSalary;
	private DraftSalaryModel selectedDraftCompany;
	private  List<EmployeeModel> lstPriorityEmployees;
	String viewType;
	private String msg;
	
	private String lastSheetCreated;
	private String lastSheetApproved;
	private String lastSheetPaid;
	private String salaryCalculation;
	private CompanyModel companySetup;
	private List<DraftSalaryModel> lstEmployeeDraftSalary;
	
	@Init
    public void init(@BindingParam("SalaryFlag") String type,@BindingParam("compKey")int compKey,@BindingParam("selectedDraftCompany") DraftSalaryModel selectedDraftCompany)
 	{
		try
		{
		//logger.info("type>>> "+ type);
		viewType=type;	
		
		Session sess = Sessions.getCurrent();		
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		logger.info("viewType"+viewType);
		if(viewType.equals("D"))//salary select company
		{
			logger.info("viewType1"+viewType);
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			fillDraftSalary();
		}
		else if(viewType.equals("C"))//salary sheet draft
		{			
			logger.info("viewType2"+viewType);
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==compKey)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);
			
			Calendar c = Calendar.getInstance();	
			salaryDate=df.parse(sdf.format(c.getTime()));	
			fillPeriods();
			fillLastSalaryStatus(compKey,selectedDraftCompany);
			logger.info(""+compKey);
		}
		else if(viewType.equals("P"))//Set Priority
		{
			lstPriorityEmployees=data.getCompanyEmployees(compKey);
		}
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> init", ex);			
		}
 	}
	
	public DraftSalarySheetViewModel()
	{
		try
		{
			/*//int defaultCompanyId=0;
			logger.info("type>>> ");
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			//getCompanyRolePermessions(dbUser.getCompanyroleid());
			//UserId=dbUser.getUserid();
			
			//defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());			
			
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);
									
			if(viewType.equals("D"))
			{
				lstComapnies=data.getCompanyList(dbUser.getUserid());
				fillDraftSalary();
			}
			else
			{
				Calendar c = Calendar.getInstance();	
				salaryDate=df.parse(sdf.format(c.getTime()));	
				fillPeriods();
			}*/
			
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> init", ex);			
		}
	}

	private void fillDraftSalary()
	{
		try
		{
		List<DraftSalaryModel> lstTotalEmployees=data.getTotalCompanyEmployees();	
		lstDraftSalary=new ArrayList<DraftSalaryModel>();
		int srNO=0;
		for (CompanyModel item : lstComapnies)
		{
			srNO++;
			DraftSalaryModel obj=new DraftSalaryModel();			
			obj.setSrNO(srNO);
			obj.setCompKey(item.getCompKey());
			obj.setEnCompanyName(item.getEnCompanyName());
			obj.setArCompanyName(item.getArCompanyName());
			if(item.getCreateDate()!=null)
			obj.setCreateDate(sdf.format(item.getCreateDate()));			
			obj.setLastSalaryCreated(data.getLastCompanySalaryDate(item.getCompKey(), "C"));
			obj.setLastSalaryApproved(data.getLastCompanySalaryDate(item.getCompKey(), "A"));
			obj.setLastSalaryPaid(data.getLastCompanySalaryDate(item.getCompKey(), "P"));
			
			obj.setTotalDepartment(item.getTotalDepartment());
			obj.setTotalPositions(item.getTotalPositions());
			for (DraftSalaryModel emp : lstTotalEmployees)
			{
			if(emp.getCompKey()==item.getCompKey())
			{
				obj.setTotalEmployees(emp.getTotalEmployees());
				break;
			}
			
			}
			lstDraftSalary.add(obj);
			
		}
		
		}				
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> fillDraftSalary", ex);			
		}
	}
	
	private void fillPeriods()
	{
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		lstMonths=months;
		lstYears=new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		int currentYear=c.get(Calendar.YEAR);
		for (int i = currentYear-30; i < currentYear+30; i++) 
		{
			lstYears.add(String.valueOf(i));
		}
		
		selectedYear=String.valueOf(currentYear);
		int month = c.get(Calendar.MONTH) ;
		selectedMonth=month;
	}
	
	
	private void fillLastSalaryStatus(int compKey,DraftSalaryModel selectedDraftCompany)
	{
		if(selectedDraftCompany==null)
		{
		lastSheetCreated="Last Salary Created : "+data.getLastCompanySalaryDate(compKey, "C");
		lastSheetApproved="Last Salary Approved : "+data.getLastCompanySalaryDate(compKey, "A");
		lastSheetPaid="Last Salary Paid : "+data.getLastCompanySalaryDate(compKey, "P");
		}
		else
		{
			lastSheetCreated="Last Salary Created : "+selectedDraftCompany.getLastSalaryCreated();
			lastSheetApproved="Last Salary Approved : "+selectedDraftCompany.getLastSalaryApproved();
			lastSheetPaid="Last Salary Paid : "+selectedDraftCompany.getLastSalaryPaid();	
		}
		companySetup=data.getCompanySetup(compKey);
		if(companySetup.getSalaryCalcDays()!=0)
		{
			salaryCalculation="Salary Calculation : Fixed 30 days";
		}
		else
		{
			salaryCalculation="Salary Calculation : Actual Month days";
		}
		
	}
	@Command
	public void openDraftCommand()
	{	
		try
		{
			if(selectedDraftCompany==null)
			{
				 Messagebox.show("Company should select !!","Salary Sheet Draft", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			}
			else if(selectedDraftCompany.getTotalEmployees()==0)
			{
				 Messagebox.show("Employee doesn't exists,Check Employee List !!","Salary Sheet Draft", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			}
			else
			{	
			 Map<String,Object> arg = new HashMap<String,Object>();
			 arg.put("compKey", selectedDraftCompany.getCompKey());
			 arg.put("SalaryFlag", "C");
			 arg.put("SalaryDraft", "Y");
			 arg.put("selectedDraftCompany", selectedDraftCompany);
			 Executions.createComponents("/payroll/salarysheetdraft.zul", null,arg);				 		
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> openDraftCommand", ex);			
		}
	}
	
	@Command
	public void openSetPriorityCommand()
	{
		try
		{
			if(selectedCompany!=null)
			{
				 Map<String,Object> arg = new HashMap<String,Object>();
				 arg.put("compKey", selectedCompany.getCompKey());
				 arg.put("SalaryFlag", "P");				
				 Executions.createComponents("/payroll/setpriority.zul", null,arg);
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> openSetPriorityCommand", ex);			
		}
	}
	@Command
	@NotifyChange({"lstPriorityEmployees"})
	public void savePriorityCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			boolean tmpValidate=false;
			for (EmployeeModel item : lstPriorityEmployees) 
			{
				if(item.getPriorityNo()>0)
				{
					tmpValidate=true;
					break;
				}
			}
			
			if(tmpValidate==false)
			{
				 Messagebox.show("Nothing to Update !!","Set Priority for Employees in Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			}
			else
			{
				for (EmployeeModel item : lstPriorityEmployees) 
				{
					data.updateEmployeePriority(item);
				}
			Messagebox.show("Data Saved Successfully !!","Set Priority for Employees in Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);	
			
			//comp.detach();			
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> savePriorityCommand", ex);			
		}
		
	}
	
	@Command
	@NotifyChange({"lstEmployeeDraftSalary","totalNoofEmployee"})
	public void viewSalaryDraftCommand()
	{
		try
		{
			 int month=selectedMonth+1;			
			 int year=Integer.parseInt(selectedYear);
			 lstEmployeeDraftSalary=data.getTimeSheetHistoryEmployee(selectedCompany.getCompKey(), month, year, 2,"");
			 totalNoofEmployee=lstEmployeeDraftSalary.size();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> viewSalaryDraftCommand", ex);			
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
	 @NotifyChange({"lstEmployeeDraftSalary","totalNoofEmployee"})
	  public void filterWindowClose(@BindingParam("myData")String empKeys)
	  {		
		 try
		 {
			 if(!empKeys.equals(""))
			 {
				 if(selectedYear==null || selectedYear.equals(""))
					 return;
				 
				 int month=selectedMonth+1;
				 logger.info(selectedYear);
				 int year=Integer.parseInt(selectedYear);
				 lstEmployeeDraftSalary=data.getTimeSheetHistoryEmployee(selectedCompany.getCompKey(), month, year, 2,empKeys);
				 totalNoofEmployee=lstEmployeeDraftSalary.size();
			 }
		 }
		 catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> filterWindowClose", ex);			
		}
	  }
	
	//properties
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}

	@NotifyChange({"lastSheetCreated","lastSheetApproved","lastSheetPaid","salaryCalculation"})
	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
		fillLastSalaryStatus(selectedCompany.getCompKey(),selectedDraftCompany);
	}

	public List<String> getLstMonths() {
		return lstMonths;
	}

	public void setLstMonths(List<String> lstMonths) {
		this.lstMonths = lstMonths;
	}


	public List<String> getLstYears() {
		return lstYears;
	}


	public void setLstYears(List<String> lstYears) {
		this.lstYears = lstYears;
	}


	public int getSelectedMonth() {
		return selectedMonth;
	}


	public void setSelectedMonth(int selectedMonth) {
		this.selectedMonth = selectedMonth;
	}


	public String getSelectedYear() {
		return selectedYear;
	}


	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}


	public Date getSalaryDate() {
		return salaryDate;
	}


	public void setSalaryDate(Date salaryDate) {
		this.salaryDate = salaryDate;
	}


	public int getTotalNoofEmployee() {
		return totalNoofEmployee;
	}


	public void setTotalNoofEmployee(int totalNoofEmployee) {
		this.totalNoofEmployee = totalNoofEmployee;
	}

	public List<DraftSalaryModel> getLstDraftSalary() {
		return lstDraftSalary;
	}

	public void setLstDraftSalary(List<DraftSalaryModel> lstDraftSalary) {
		this.lstDraftSalary = lstDraftSalary;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DraftSalaryModel getSelectedDraftCompany() {
		return selectedDraftCompany;
	}

	public void setSelectedDraftCompany(DraftSalaryModel selectedDraftCompany) {
		this.selectedDraftCompany = selectedDraftCompany;
	}

	public List<EmployeeModel> getLstPriorityEmployees() {
		return lstPriorityEmployees;
	}

	public void setLstPriorityEmployees(List<EmployeeModel> lstPriorityEmployees) {
		this.lstPriorityEmployees = lstPriorityEmployees;
	}

	public String getLastSheetCreated() {
		return lastSheetCreated;
	}

	public void setLastSheetCreated(String lastSheetCreated) {
		this.lastSheetCreated = lastSheetCreated;
	}

	public String getLastSheetApproved() {
		return lastSheetApproved;
	}

	public void setLastSheetApproved(String lastSheetApproved) {
		this.lastSheetApproved = lastSheetApproved;
	}

	public String getLastSheetPaid() {
		return lastSheetPaid;
	}

	public void setLastSheetPaid(String lastSheetPaid) {
		this.lastSheetPaid = lastSheetPaid;
	}

	public String getSalaryCalculation() {
		return salaryCalculation;
	}

	public void setSalaryCalculation(String salaryCalculation) {
		this.salaryCalculation = salaryCalculation;
	}

	public CompanyModel getCompanySetup() {
		return companySetup;
	}

	public void setCompanySetup(CompanyModel companySetup) {
		this.companySetup = companySetup;
	}

	public List<DraftSalaryModel> getLstEmployeeDraftSalary() {
		return lstEmployeeDraftSalary;
	}

	public void setLstEmployeeDraftSalary(List<DraftSalaryModel> lstEmployeeDraftSalary) {
		this.lstEmployeeDraftSalary = lstEmployeeDraftSalary;
	}
}
