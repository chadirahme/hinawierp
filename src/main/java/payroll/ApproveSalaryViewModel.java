package payroll;

import hr.HRData;
import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.DraftSalaryModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;

import setup.users.WebusersModel;

public class ApproveSalaryViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());	
	DraftSalarySheetData data=new DraftSalarySheetData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstMonths;
	private List<String> lstYears;
	private int selectedMonth;
	private String selectedYear;
	private List<DraftSalaryModel> lstApproveSalary;
	private Set<DraftSalaryModel> selectedEntities;	
	private int UserId;
	
	public ApproveSalaryViewModel()
	{
		try
		{			
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			if(lstComapnies.size()>=1 && selectedCompany==null)		
				selectedCompany=lstComapnies.get(0);			
			UserId=dbUser.getUserid();
			fillPeriods();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ApproveSalaryViewModel ----> init", ex);			
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
	
	 @Command
	 public void filterCommand()
	 {
		 Map<String,Object> arg = new HashMap<String,Object>();
		 arg.put("compKey", selectedCompany.getCompKey());
		 arg.put("type", "T");
		 Executions.createComponents("/timesheet/employeefilter.zul", null,arg);		 
	 }
	 @GlobalCommand 
	 @NotifyChange({"lstApproveSalary"})
		  public void filterWindowClose(@BindingParam("myData")String empKeys)
		  {		
			 try
			 {
				 if(!empKeys.equals(""))
				 {
					 if(selectedYear==null || selectedYear.equals(""))
						 return;
					 
					 int month=selectedMonth+1;			
					 int year=Integer.parseInt(selectedYear);
					 lstApproveSalary =data.getEmployeeApproveSalary(selectedCompany.getCompKey(),empKeys, month, year);					 				
				 }
			 }
			 catch (Exception ex)
			{	
				logger.error("ERROR in ApproveSalaryViewModel ----> filterWindowClose", ex);			
			}
		  }
	 
	 
	@Command
	@NotifyChange({"lstApproveSalary"})	
	public void viewSalaryCommand()
	{
		try
		{
		 int month=selectedMonth+1;			
		 int year=Integer.parseInt(selectedYear);
		 lstApproveSalary =data.getEmployeeApproveSalary(selectedCompany.getCompKey(),"", month, year);
		 
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ApproveSalaryViewModel ----> viewSalaryCommand", ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstApproveSalary"})
	public void saveCommand()
	{
		
		try
		{
			 int month=selectedMonth+1;			
			 int year=Integer.parseInt(selectedYear);
			List<Integer> lstRecNo=new ArrayList<Integer>();			
			if(selectedEntities!=null)
			{
				for (DraftSalaryModel item : selectedEntities) 
				{
					lstRecNo.add(item.getRecNo());
				}					
			}
			
			if(lstRecNo.size()==0)
			{
				Messagebox.show("Please Check at least one employee to save !!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			for (Integer recNo : lstRecNo) 
			{
				data.approveSalary(recNo,selectedCompany.getCompKey(),month,year);
			}
			//save user activity
			HRData hr=new HRData();
			String DESCRIPTION=selectedMonth+1 +"-" + selectedYear + " ( " + lstRecNo.size() + " employees)";			
			hr.addUserActivity(common.HREnum.HRFormNames.HRTSSalarySheet.getValue(),common.HREnum.HRStatus.HRNew.getValue(), 0, selectedCompany.getCompKey(), DESCRIPTION, UserId);
			
			Messagebox.show("Salary Sheet Approved Successfully !! ","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			selectedEntities=null;
			
			lstApproveSalary =data.getEmployeeApproveSalary(selectedCompany.getCompKey(),"", month, year);
			 
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ApproveSalaryViewModel ----> saveCommand", ex);			
		}
	}

	@Command
	@NotifyChange({"lstApproveSalary"})
	public void clearCommand()
	{
		lstApproveSalary=new ArrayList<DraftSalaryModel>();
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

	public List<DraftSalaryModel> getLstApproveSalary() {
		return lstApproveSalary;
	}

	public void setLstApproveSalary(List<DraftSalaryModel> lstApproveSalary) {
		this.lstApproveSalary = lstApproveSalary;
	}

	public Set<DraftSalaryModel> getSelectedEntities() {
		return selectedEntities;
	}

	public void setSelectedEntities(Set<DraftSalaryModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
	}
}
