package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import layout.MenuModel;
import model.EmployeeModel;
import model.TimeSheetDataModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class TimeSheetHistoryViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData hrdata=new HRData();
	TimeSheetData data=new TimeSheetData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstMonths;
	private String selectedMonth;
	private List<String> lstYears;
	private String selectedYear;
	private List<TimeSheetDataModel> lstHistory;
	private List<TimeSheetDataModel> lstMonthlyHistory;
	
	private int selectedDateType;
	private int selectedStatus;
	private boolean showMonth;
	private Set<TimeSheetDataModel> selectedEntities;
	private Set<TimeSheetDataModel> selectedMonths;
	
	int menuID=137;
	private MenuModel companyRole;
	private int UserId;
	
	public TimeSheetHistoryViewModel()
	{
		try
		{
			int defaultCompanyId=0;
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			UserId=dbUser.getUserid();
			
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());			
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);
			
			lstMonths=new ArrayList<String>();
			lstMonths.add("All");
			for (int i = 1; i < 13; i++) 
			{
			lstMonths.add(String.valueOf(i));	
			}
			selectedMonth=lstMonths.get(0);
			Calendar c = Calendar.getInstance();
			lstYears=new ArrayList<String>();
			for(int i=c.get(Calendar.YEAR);i>1999;i--)
			{
				lstYears.add(String.valueOf(i));	
			}
			selectedYear=lstYears.get(0);
			
			selectedDateType=0;
			showMonth=true;
			selectedStatus=0;
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TimeSheetHistoryViewModel ----> init", ex);			
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
	 @NotifyChange({"lstHistory","lstMonthlyHistory"})
	 public void searchCommand()
	 {
		 try
		 {
			 selectedEntities=null;
			 int month=selectedMonth.equals("All")?0:Integer.parseInt(selectedMonth);			 
			 lstHistory= data.getTimeSheetHistory(selectedCompany.getCompKey(),month,Integer.parseInt(selectedYear),selectedStatus);
			 
			 if(selectedDateType==0)//Monthly
			 {
				 int totalDays=0;
				 double totalPresent=0;
				 int totalHolidays=0;
				 int tsMonth=0;
				 int totalEmployees=0;
				 lstMonthlyHistory=new ArrayList<TimeSheetDataModel>();
				 
				 for (TimeSheetDataModel item : lstHistory)
				 {
					 if(tsMonth!=item.getTsMonth())
					 {
						 if(tsMonth>0)
						 {
						 TimeSheetDataModel obj=new TimeSheetDataModel();
						 obj.setTotalEmployees(totalEmployees);
						 obj.setTsMonth(tsMonth);
						 obj.setTsYear(Integer.parseInt(selectedYear));
						 obj.setDayNo(totalDays);
						 obj.setPresentDays(totalPresent);
						 obj.setHolidays(totalHolidays);
						 lstMonthlyHistory.add(obj);
						 }
						 
						 totalDays=0;
						 totalPresent=0;
						 totalHolidays=0;
						 totalEmployees=0;
						 tsMonth=item.getTsMonth();						 
					 }
					totalDays+=item.getDayNo();
					totalPresent+=item.getPresentDays();
					totalHolidays+=item.getHolidays();
					totalEmployees++;									
				}
				 
				 TimeSheetDataModel obj=new TimeSheetDataModel();
				 obj.setTotalEmployees(totalEmployees);
				 obj.setTsMonth(tsMonth);
				 obj.setTsYear(Integer.parseInt(selectedYear));
				 obj.setDayNo(totalDays);
				 obj.setPresentDays(totalPresent);
				 obj.setHolidays(totalHolidays);
				 lstMonthlyHistory.add(obj);
				 
				
				
			 }
			 
		 }
		 catch (Exception ex)
		{	
				logger.error("ERROR in TimeSheetHistoryViewModel ----> searchCommand", ex);			
		}
	 }
	 
	 @Command
	 @NotifyChange({"lstHistory"})
	 public void approveCommand(@BindingParam("row") TimeSheetDataModel row)
	 {
		 try
		 {
			 data.approveTimeSheet(row,selectedDateType);
			Messagebox.show("Time sheet is approved. ","Time sheet", Messagebox.OK , Messagebox.INFORMATION);
			 int month=selectedMonth.equals("All")?0:Integer.parseInt(selectedMonth);			 
			 lstHistory= data.getTimeSheetHistory(selectedCompany.getCompKey(),month,Integer.parseInt(selectedYear),selectedStatus);
		 }
		 catch (Exception ex)
			{	
			  logger.error("ERROR in TimeSheetHistoryViewModel ----> approveCommand", ex);			
			}
	 }
	
	 @Command
	 @NotifyChange({"lstHistory"})
	 public void approveAllCommand()
	 {
		 try
		 {
			  List<Integer> lstEmpKey=new ArrayList<Integer>(); 
			 if(selectedDateType==1)
			 {
			 List<TimeSheetDataModel> lstEmployeeId=new ArrayList<TimeSheetDataModel>();			
			if(selectedEntities!=null)
			{
				for (TimeSheetDataModel item : selectedEntities) 
				{
					lstEmployeeId.add(item);
					if(!lstEmpKey.contains(item.getEmpKey()))
					lstEmpKey.add(item.getEmpKey());
				}						
			}
			 			
			 if(lstEmployeeId.size()==0)
			 {
				   Messagebox.show("Please select employees to approve!!","Approve Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			 }
			 
			 for (TimeSheetDataModel row : lstEmployeeId) 
			 {
				 data.approveTimeSheet(row,selectedDateType);
			 }
			
			int EMP_KEY=0;
			if(lstEmpKey.size()==1)
			{
		      EMP_KEY=lstEmpKey.get(0);
			}
			String DESCRIPTION="Approve Time sheet for " + selectedMonth + "- " + selectedYear + " ( " + lstEmpKey.size() + " employees)";;
			data.addUserActivity(common.HREnum.HRFormNames.HRTimesheetDetailed.getValue(),common.HREnum.HRStatus.HRApprove.getValue(), EMP_KEY, selectedCompany.getCompKey(), DESCRIPTION, UserId);
			 
			Messagebox.show("Employees Time sheet is approved. ","Time sheet", Messagebox.OK , Messagebox.INFORMATION);
			int month=selectedMonth.equals("All")?0:Integer.parseInt(selectedMonth);			 
			lstHistory= data.getTimeSheetHistory(selectedCompany.getCompKey(),month,Integer.parseInt(selectedYear),selectedStatus);
			}
			 
			else //Monthly Approved
			{
				List<TimeSheetDataModel> lstMonths=new ArrayList<TimeSheetDataModel>();
				if(selectedMonths!=null)
				{
					for (TimeSheetDataModel item : selectedMonths) 
					{
						lstMonths.add(item);
					}	 					 
				}
				if(lstMonths.size()==0)
				{
					   Messagebox.show("Please select month to approve !!","Approve Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				}
				
				for (TimeSheetDataModel row : lstMonths) 
				 {
					 data.approveTimeSheet(row,selectedDateType);
				 }
				Messagebox.show("The selected months time sheet is approved. ","Time sheet", Messagebox.OK , Messagebox.INFORMATION);
				int month=selectedMonth.equals("All")?0:Integer.parseInt(selectedMonth);			 
				lstHistory= data.getTimeSheetHistory(selectedCompany.getCompKey(),month,Integer.parseInt(selectedYear),selectedStatus);
			}
			
		 }
		 catch (Exception ex)
			{	
			  logger.error("ERROR in TimeSheetHistoryViewModel ----> approveCommand", ex);			
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

	public String getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public List<String> getLstYears() {
		return lstYears;
	}

	public void setLstYears(List<String> lstYears) {
		this.lstYears = lstYears;
	}

	public String getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	public List<TimeSheetDataModel> getLstHistory() {
		return lstHistory;
	}

	public void setLstHistory(List<TimeSheetDataModel> lstHistory) {
		this.lstHistory = lstHistory;
	}

	public int getSelectedDateType() {
		return selectedDateType;
	}

	@NotifyChange("showMonth")
	public void setSelectedDateType(int selectedDateType) 
	{
		this.selectedDateType = selectedDateType;
		if(selectedDateType==0)
		{
			showMonth=true;
		}
		else
			showMonth=false;
	}
		
	public Set<TimeSheetDataModel> getSelectedEntities() {
		return selectedEntities;
	}

	public void setSelectedEntities(Set<TimeSheetDataModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
	}

	public List<TimeSheetDataModel> getLstMonthlyHistory() {
		return lstMonthlyHistory;
	}

	public void setLstMonthlyHistory(List<TimeSheetDataModel> lstMonthlyHistory) {
		this.lstMonthlyHistory = lstMonthlyHistory;
	}

	public boolean isShowMonth() {
		return showMonth;
	}

	public void setShowMonth(boolean showMonth) {
		this.showMonth = showMonth;
	}

	public Set<TimeSheetDataModel> getSelectedMonths() {
		return selectedMonths;
	}

	public void setSelectedMonths(Set<TimeSheetDataModel> selectedMonths) {
		this.selectedMonths = selectedMonths;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public int getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(int selectedStatus) {
		this.selectedStatus = selectedStatus;
	}
}
