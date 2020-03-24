package hr;

import hr.model.CompanyModel;
import hr.model.WorkGroupModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.CompanySettingsModel;
import model.EmployeeFilter;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class AssignEmployeeToShift {
	
	private Logger logger = Logger.getLogger(this.getClass());

	HRData data = new HRData();
	WorkGroupData workGroupData = new WorkGroupData();
	private List<EmployeeModel> lstAssignedEmployees;
	private EmployeeModel selectedAssignedEmployees;
	private List<EmployeeModel> lstAllAssignedEmployees;
	private EmployeeFilter employeeFilter = new EmployeeFilter();
	private boolean adminUser;
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	WebusersModel dbUser;
	
	
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
	
	
	private List<String> lstMonths;
	private String selectedMonth;
	private List<String> lstYears;
	private String selectedYear;
	private int selectedDateType;
	private boolean showMonth;
	private int supervisorID;
	private String footer;
	private int totalEmployeeNumber;
	private String searchText;
	private CompanySettingsModel compSettings;
	
	
	public AssignEmployeeToShift()
	{
		try {
		Session sess = Sessions.getCurrent();
		dbUser = (WebusersModel) sess.getAttribute("Authentication");
		if (dbUser != null) {
			adminUser = dbUser.getFirstname().equals("admin");
			supervisorID=dbUser.getSupervisor();
		}
		lstComapnies = data.getCompanyList(dbUser.getUserid());
		
		if (lstComapnies != null)
			selectedCompany = lstComapnies.get(0);
		
			Calendar c = Calendar.getInstance();	
			fromDate=df.parse(sdf.format(c.getTime()));
			toDate=df.parse(sdf.format(c.getTime()));
			lstMonths=new ArrayList<String>();
			//lstMonths.add("Select");
			for (int i = 1; i < 13; i++) 
			{
			lstMonths.add(String.valueOf(i));	
			}
			
			int month = c.get(Calendar.MONTH) +1 ;
			selectedMonth=String.valueOf(month);//lstMonths.get(0);
			
			
			lstYears=new ArrayList<String>();
			for(int i=c.get(Calendar.YEAR);i>1999;i--)
			{
				lstYears.add(String.valueOf(i));	
			}
			selectedYear=lstYears.get(0);
			selectedDateType=0;
			showMonth=true;
			footer=""+0+"";
		} 
		catch (ParseException e) {
			logger.error("ERROR in AssignEmployeeToShift ----> init", e);
		}		
	}
	
	
	@Command
	@NotifyChange({"lstAssignedEmployees","footer"})
	public void viewShiftEmployees()
	{
		 try
		 {
			 Date _fromDate;
			 Date  _toDate;
			 if(selectedDateType==1)//by dates
			 {
				 
				 int monthFrom=0;
				 int monthTo=0;
				 int yearFrom=0;
				 int yearTo=0;
				 Calendar c = Calendar.getInstance();	
				 c.setTime(fromDate);
				  monthFrom=c.get(Calendar.MONTH);
				  yearFrom=c.get(Calendar.YEAR);
				   c.setTime(toDate);
				   monthTo=c.get(Calendar.MONTH);
				   yearTo=c.get(Calendar.YEAR);
				   if(monthFrom!=monthTo || yearFrom !=yearTo)
				   {
						Messagebox.show("Month of From Date & Month of To date should be Same !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
						return;
				   }
				   
				   _fromDate=fromDate;
				   _toDate=toDate;
			 }
			 else
			 {
				 Calendar c = Calendar.getInstance();
				 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,1);
				 _fromDate=c.getTime();
				 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
				 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
				 _toDate=c.getTime();				 
			 }
			 lstAssignedEmployees=workGroupData.getAssignedEmployeesToShift(selectedCompany.getCompKey(),_fromDate, _toDate);		
			 lstAllAssignedEmployees=lstAssignedEmployees;
			 footer=""+lstAssignedEmployees.size()+"";
		 }
	     catch (Exception ex)
		{	
			logger.error("ERROR in AssignEmployeeToShift ----> ViewShiftEmployees", ex);			
		}
		
	}
	
	private List<EmployeeModel> filterData()
	{
		 lstAssignedEmployees=lstAllAssignedEmployees;
				
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		for (Iterator<EmployeeModel> i = lstAssignedEmployees.iterator(); i.hasNext();)
		{
			EmployeeModel tmp=i.next();	
			if(tmp.getWorkGroupName()==null)
				tmp.setWorkGroupName("");
			if(tmp.getSupervisorName()==null)
				tmp.setSupervisorName("");
			if(tmp.getFullName().toLowerCase().contains(employeeFilter.getFullName().toLowerCase())&&
					tmp.getDepartment().toLowerCase().contains(employeeFilter.getDepartment().toLowerCase())&&
					tmp.getPosition().toLowerCase().contains(employeeFilter.getPosition().toLowerCase())&&
					tmp.getCountry().toLowerCase().contains(employeeFilter.getCountry().toLowerCase())&&
					tmp.getAge().toLowerCase().startsWith(employeeFilter.getAge().toLowerCase())&&
					tmp.getEmployeeNo().toLowerCase().contains(employeeFilter.getEmployeeNo().toLowerCase())&&
					tmp.getWorkGroupName().toLowerCase().startsWith(employeeFilter.getGroupName().toLowerCase())&&
					tmp.getSupervisorName().toLowerCase().contains(employeeFilter.getSupervisorName().toLowerCase())&&
					tmp.getStatus().toLowerCase().startsWith(employeeFilter.getStatus().toLowerCase())&&
					tmp.getShiftType().toLowerCase().startsWith(employeeFilter.getShiftType().toLowerCase())&&
					
				/*	tmp.getShiftFromDate().toString().toLowerCase().contains(employeeFilter.getShiftFromDate().toLowerCase())&&
					tmp.getShiftToDate().toString().toLowerCase().contains(employeeFilter.getShiftToDate().toLowerCase())&&*/
					tmp.getEmployeeStatus().toLowerCase().contains(employeeFilter.getEmployeeStatus().toLowerCase())&&
					tmp.getStatusDescription().toLowerCase().contains(employeeFilter.getStatusDescription().toLowerCase())&&
					tmp.getShiftName().toLowerCase().contains(employeeFilter.getShiftName().toLowerCase())&&
					tmp.getProjectName().toLowerCase().contains(employeeFilter.getProjectName().toLowerCase())
					)
					
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	    @Command
	    @NotifyChange({"lstAssignedEmployees","footer"})
	    public void changeFilter() 
	    {	      
	    	lstAssignedEmployees=filterData();
	    	footer=""+lstAssignedEmployees.size()+"";
	    }
	
	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	/**
	 * @return the data
	 */
	public HRData getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(HRData data) {
		this.data = data;
	}
	/**
	 * @return the workGroupData
	 */
	public WorkGroupData getWorkGroupData() {
		return workGroupData;
	}
	/**
	 * @param workGroupData the workGroupData to set
	 */
	public void setWorkGroupData(WorkGroupData workGroupData) {
		this.workGroupData = workGroupData;
	}
	/**
	 * @return the lstAssignedEmployees
	 */
	public List<EmployeeModel> getLstAssignedEmployees() {
		return lstAssignedEmployees;
	}
	/**
	 * @param lstAssignedEmployees the lstAssignedEmployees to set
	 */
	public void setLstAssignedEmployees(List<EmployeeModel> lstAssignedEmployees) {
		this.lstAssignedEmployees = lstAssignedEmployees;
	}
	/**
	 * @return the selectedAssignedEmployees
	 */
	public EmployeeModel getSelectedAssignedEmployees() {
		return selectedAssignedEmployees;
	}
	/**
	 * @param selectedAssignedEmployees the selectedAssignedEmployees to set
	 */
	public void setSelectedAssignedEmployees(EmployeeModel selectedAssignedEmployees) {
		this.selectedAssignedEmployees = selectedAssignedEmployees;
	}
	/**
	 * @return the lstAllAssignedEmployees
	 */
	public List<EmployeeModel> getLstAllAssignedEmployees() {
		return lstAllAssignedEmployees;
	}
	/**
	 * @param lstAllAssignedEmployees the lstAllAssignedEmployees to set
	 */
	public void setLstAllAssignedEmployees(
			List<EmployeeModel> lstAllAssignedEmployees) {
		this.lstAllAssignedEmployees = lstAllAssignedEmployees;
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
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
	}
	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}
	/**
	 * @return the lstComapnies
	 */
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}
	/**
	 * @param lstComapnies the lstComapnies to set
	 */
	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}
	/**
	 * @return the selectedCompany
	 */
	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}
	/**
	 * @param selectedCompany the selectedCompany to set
	 */
	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
	}
	/**
	 * @return the dbUser
	 */
	public WebusersModel getDbUser() {
		return dbUser;
	}
	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(WebusersModel dbUser) {
		this.dbUser = dbUser;
	}


	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}


	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}


	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}


	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


	/**
	 * @return the df
	 */
	public DateFormat getDf() {
		return df;
	}


	/**
	 * @param df the df to set
	 */
	public void setDf(DateFormat df) {
		this.df = df;
	}


	/**
	 * @return the sdf
	 */
	public SimpleDateFormat getSdf() {
		return sdf;
	}


	/**
	 * @param sdf the sdf to set
	 */
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}


	/**
	 * @return the formatter
	 */
	public DecimalFormat getFormatter() {
		return formatter;
	}


	/**
	 * @param formatter the formatter to set
	 */
	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
	}


	/**
	 * @return the lstMonths
	 */
	public List<String> getLstMonths() {
		return lstMonths;
	}


	/**
	 * @param lstMonths the lstMonths to set
	 */
	public void setLstMonths(List<String> lstMonths) {
		this.lstMonths = lstMonths;
	}


	/**
	 * @return the selectedMonth
	 */
	public String getSelectedMonth() {
		return selectedMonth;
	}


	/**
	 * @param selectedMonth the selectedMonth to set
	 */
	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
	}


	/**
	 * @return the lstYears
	 */
	public List<String> getLstYears() {
		return lstYears;
	}


	/**
	 * @param lstYears the lstYears to set
	 */
	public void setLstYears(List<String> lstYears) {
		this.lstYears = lstYears;
	}


	/**
	 * @return the selectedYear
	 */
	public String getSelectedYear() {
		return selectedYear;
	}


	/**
	 * @param selectedYear the selectedYear to set
	 */
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}


	/**
	 * @return the selectedDateType
	 */
	public int getSelectedDateType() {
		return selectedDateType;
	}


	/**
	 * @param selectedDateType the selectedDateType to set
	 */
	@NotifyChange("showMonth")
	public void setSelectedDateType(int selectedDateType) {
		this.selectedDateType = selectedDateType;
		if(selectedDateType==0)
		{
			showMonth=true;
		}
		else
			showMonth=false;
	}


	/**
	 * @return the showMonth
	 */
	public boolean isShowMonth() {
		return showMonth;
	}


	/**
	 * @param showMonth the showMonth to set
	 */
	public void setShowMonth(boolean showMonth) {
		this.showMonth = showMonth;
	}


	/**
	 * @return the supervisorID
	 */
	public int getSupervisorID() {
		return supervisorID;
	}


	/**
	 * @param supervisorID the supervisorID to set
	 */
	public void setSupervisorID(int supervisorID) {
		this.supervisorID = supervisorID;
	}


	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}


	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}


	/**
	 * @return the totalEmployeeNumber
	 */
	public int getTotalEmployeeNumber() {
		return totalEmployeeNumber;
	}


	/**
	 * @param totalEmployeeNumber the totalEmployeeNumber to set
	 */
	public void setTotalEmployeeNumber(int totalEmployeeNumber) {
		this.totalEmployeeNumber = totalEmployeeNumber;
	}


	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}


	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}


	/**
	 * @return the compSettings
	 */
	public CompanySettingsModel getCompSettings() {
		return compSettings;
	}


	/**
	 * @param compSettings the compSettings to set
	 */
	public void setCompSettings(CompanySettingsModel compSettings) {
		this.compSettings = compSettings;
	}

	
	
	
	
}
