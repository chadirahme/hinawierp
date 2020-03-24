package payroll;

import hr.model.CompanyModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.DraftSalaryModel;

import org.apache.log4j.Logger;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;

import setup.users.WebusersModel;

public class SalarySheetViewModel
{
	private Logger logger = Logger.getLogger(this.getClass());	
	DraftSalarySheetData data=new DraftSalarySheetData();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstMonths;
	private List<String> lstYears;
	private int selectedMonth;
	private String selectedYear;
	private CompanyModel companySetup;
	boolean msgResult=false;
	private List<DraftSalaryModel> lstEmployeeDraftSalary;
	private List<String> lstOTCalculation;
	private ListModelList<String> columnUnitList;
	private ListModelList<String> columnAmountList;
	
	/*@Wire("#lstReport")
	Listbox lstReport;
	@Init 
	public void init(@ContextParam(ContextType.VIEW) org.zkoss.zk.ui.Component view){
	Selectors.wireComponents(view, this, false); 
	}*/
	/*@Init
	public void init(@ContextParam(ContextType.COMPONENT) Component component,
	@ContextParam(ContextType.VIEW) Component view) {
		//lstReport=(Listbox) component.getFellow("lstReport");//(Listbox) Executions.getCurrent().getAttribute("lstReport");
	}*/
	
	/*@AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view){
        Selectors.wireComponents(lstReport, this, false);
        //grdSalarySheet_Refresh();
    }*/
	
	@Init
	public void init(@BindingParam("compKey")int compKey)
	{
		
		logger.info(" compkey>>" + compKey);
	}
	
	public SalarySheetViewModel()
	{
		try
		{			
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			if(lstComapnies.size()>=1 && selectedCompany==null)		
				selectedCompany=lstComapnies.get(0);
			
			if(selectedCompany!=null)
			companySetup=data.getCompanySetup(selectedCompany.getCompKey());
			fillPeriods();
			grdSalarySheet_Refresh();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in SalarySheetViewModel ----> init", ex);			
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
	
	private void grdSalarySheet_Refresh()
	{
		
	  lstOTCalculation=data.getCompanyOTCalculation(selectedCompany.getCompKey());
	  columnUnitList=new ListModelList<String>();
	  columnAmountList=new ListModelList<String>();
	  
	  for (String item : lstOTCalculation)	
		{
			//if(!columnList.contains(item))
		  columnUnitList.add("Hrs. (" + item + ")");
		}
	  
	  for (String item : lstOTCalculation)	
		{
			//if(!columnList.contains(item))
		  columnAmountList.add("Amt. (" + item + ")");
		}
	  	  
	}
	
	@Command
	//@NotifyChange({"lstEmployeeDraftSalary","totalNoofEmployee","columnUnitList","columnAmountList"})
	//@NotifyChange("*")
	public void viewSalaryCommand()
	{
		try
		{
				
			Vlayout pnlList=  (Vlayout) Path.getComponent("/pnlList");
			pnlList.getChildren().clear();
			
			
			 int month=selectedMonth+1;			
			 int year=Integer.parseInt(selectedYear);
			 boolean tmpIncludeHolidayHrs=false;
			 String empKeys="";
			 if(companySetup.getIncludeHolidayUnit().equals("Y"))
			 {
				 tmpIncludeHolidayHrs=true;
			 }
			 
			 List<DraftSalaryModel> lstEmployees =data.getTimeSheetEmployee(selectedCompany.getCompKey(), month, year, 2,"");
			 for (DraftSalaryModel emp : lstEmployees) 
			 {
				if(empKeys.equals(""))
				{
					empKeys=emp.getEmpKey()+"";
				}
				else
				{
					empKeys+="," + emp.getEmpKey();
				}
			 }
			 
			 if(empKeys.equals(""))
			 {
				 Messagebox.show("No Time Sheet found for this month!!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			 }
			 
			 boolean isConditionFound=false;
			 
			 isConditionFound= data.checkEmployeeSalary(selectedCompany.getCompKey(), month, year, empKeys, "Leave");
			 if(isConditionFound)
			 {
				 Messagebox.show("Some Leave Payments are not Paid.Do you want to continue without Paying the Leaves?","Salary Sheet", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						    public void onEvent(Event evt) throws InterruptedException {
						    	if (evt.getName().equals("onYes")) 
						        {
						    		//continue
						    		//msgResult=true;
						    		changeMsgResult();
						        }
						    	else
						    	{
						    		//msgResult=false;					    	
						    	}
						    }
				  	});
			 }
			 
			 isConditionFound=false;
			 isConditionFound= data.checkEmployeeSalary(selectedCompany.getCompKey(), month, year, empKeys, "Additions");
			 if(isConditionFound)
			 {
				 Messagebox.show("Some Additions & Deductions are not approved, Please check and continue!!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
			 }
			 
			 isConditionFound=false;
			 isConditionFound= data.checkEmployeeSalary(selectedCompany.getCompKey(), month, year, empKeys, "Loans");
			 if(isConditionFound)
			 {
				 Messagebox.show("Some Loan(s) are not approved, Please check and continue!!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			 }
			 
			 isConditionFound=false;
			 isConditionFound= data.checkEmployeeSalary(selectedCompany.getCompKey(), month, year, empKeys, "EOS");
			 if(isConditionFound)
			 {
				 Messagebox.show("Some EOS are not approved, Please check and continue!!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			 }
			 
			 if(msgResult)
			 logger.info("conciutee >>> ");
			 
			 //grdSalarySheet_Refresh();				 
			 //lstEmployeeDraftSalary=data.getSummaryTimeSheetHistoryEmployee(selectedCompany.getCompKey(), month, year, 2,"");
			 Map<String,Object> arg = new HashMap<String,Object>();
			 arg.put("compKey", selectedCompany.getCompKey());
			 arg.put("month", month);
			 arg.put("year", year);
			 arg.put("empkeys", empKeys);
			 Executions.createComponents("salarydetails.zul", pnlList, arg);
				
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in SalarySheetViewModel ----> viewSalaryCommand", ex);			
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
	// @NotifyChange({"lstEmployeeDraftSalary","totalNoofEmployee"})
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
				 //lstEmployeeDraftSalary=data.getTimeSheetHistoryEmployee(selectedCompany.getCompKey(), month, year, 2,empKeys);
				 //totalNoofEmployee=lstEmployeeDraftSalary.size();
				 
				 Vlayout pnlList=  (Vlayout) Path.getComponent("/pnlList");
				 pnlList.getChildren().clear();
				 Map<String,Object> arg = new HashMap<String,Object>();
				 arg.put("compKey", selectedCompany.getCompKey());
				 arg.put("month", month);
				 arg.put("year", year);
				 arg.put("empkeys", empKeys);
				 Executions.createComponents("salarydetails.zul", pnlList, arg);
			 }
		 }
		 catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> filterWindowClose", ex);			
		}
	  }
	
	 
	private void changeMsgResult()
	{
		msgResult=true;
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

	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		companySetup=data.getCompanySetup(selectedCompany.getCompKey());
		
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

	public List<DraftSalaryModel> getLstEmployeeDraftSalary() {
		return lstEmployeeDraftSalary;
	}

	public void setLstEmployeeDraftSalary(List<DraftSalaryModel> lstEmployeeDraftSalary) {
		this.lstEmployeeDraftSalary = lstEmployeeDraftSalary;
	}

	public ListModelList<String> getColumnUnitList() {
		return columnUnitList;
	}

	public void setColumnUnitList(ListModelList<String> columnUnitList) {
		this.columnUnitList = columnUnitList;
	}

	public ListModelList<String> getColumnAmountList() {
		return columnAmountList;
	}

	public void setColumnAmountList(ListModelList<String> columnAmountList) {
		this.columnAmountList = columnAmountList;
	}
}
