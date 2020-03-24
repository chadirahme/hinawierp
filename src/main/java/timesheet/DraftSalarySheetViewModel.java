package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.DraftSalaryModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class DraftSalarySheetViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData hrdata=new HRData();
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
	String viewType;
	private String msg;
	
	@Init
    public void init(@BindingParam("type") String type)
 	{
		try
		{
		//logger.info("type>>> "+ type);
		viewType=type;	
		
		Session sess = Sessions.getCurrent();		
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		
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
		for(int i=c.get(Calendar.YEAR);i>1999;i--)
		{
			lstYears.add(String.valueOf(i));			
		}
		selectedYear=lstYears.get(0);
		int month = c.get(Calendar.MONTH) ;
		selectedMonth=month;
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
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DraftSalarySheetViewModel ----> openDraftCommand", ex);			
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

	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
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
}
