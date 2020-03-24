package hr;

import hr.model.ActivityModel;
import hr.model.CompanyModel;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.EmployeeModel;
import model.HRListValuesModel;
import model.TimeSheetGridModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.GroupRenderer;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.pdf.FontFactory;
import org.zkoss.exporter.pdf.PdfExporter;
import org.zkoss.exporter.pdf.PdfPCellFactory;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class EmployeeLedgerViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstPeriods;
	private String selectedPeriods;
	private List<String> lstStatus;
	private String selectedStatus;
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
	
	private boolean showGroup = true;
	
	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
	
	
	private  List<EmployeeModel> lstEmployee;
	private EmployeeLedgerModel activityModel;
	private List<String> lstActivity;
	private String selectedActivity;
	
	private List<HRListValuesModel> lstDepartment;
	private HRListValuesModel selectedDepartment;
	
	private List<HRListValuesModel> lstPosition;
	private HRListValuesModel selectedPosition;
	
	private boolean showEOS;
	private boolean enableEOS;
	
	private String message;
	private String totalRecords;
	private boolean isOpenGroup;
	
	private boolean canExport;
	private int supervisorID;
	
	public EmployeeLedgerViewModel()
	{
		
		try
		{					
		FillListPeriods();
		FillStatusList();
		FillActivityList();
		
		lstDepartment=data.getHRListValues(6,"All");
		selectedDepartment=lstDepartment.get(0);
		lstPosition=data.getHRListValues(7,"All");
		selectedPosition=lstPosition.get(0);
		
		Calendar c = Calendar.getInstance();			
		//fromDate=df.parse("01/01/2010");//df.parse(sdf.format(c.getTime()));		
		//toDate=df.parse("01/09/2012");//df.parse(sdf.format(c.getTime()));
		
		fromDate=df.parse(sdf.format(c.getTime()));		
		toDate=df.parse(sdf.format(c.getTime()));
		
		int defaultCompanyId=0;
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		
		defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());
		lstComapnies=data.getCompanyList(dbUser.getUserid());
		for (CompanyModel item : lstComapnies) 
		{
		if(item.getCompKey()==defaultCompanyId)
			selectedCompany=item;
		}
		
		if(lstComapnies.size()>=1 && selectedCompany==null)
		selectedCompany=lstComapnies.get(0);
				
		
		supervisorID=dbUser.getSupervisor();
		
		lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"All","A",supervisorID);
		selectedCompEmployee=lstCompEmployees.get(0);
		
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in EmployeeLedgerViewModel ----> init", ex);			
		}
		
	}

	private void FillStatusList()
	{
		lstStatus=new ArrayList<String>();
		lstStatus.add("Active");
		lstStatus.add("Inactive");
		lstStatus.add("EOS");
		lstStatus.add("All");
		selectedStatus=lstStatus.get(0);
	}
	private void FillListPeriods()
		{
			
		lstPeriods=new ArrayList<String>();	
		lstPeriods.add("Customize");
		lstPeriods.add("Today");
		lstPeriods.add("This Week");
		lstPeriods.add("This Month");
		lstPeriods.add("Yesterday");
		lstPeriods.add("Last Week");
		lstPeriods.add("Last Month");
		lstPeriods.add("Next Week");
		lstPeriods.add("Next Month");				
		//lstPeriods.add("All");
		selectedPeriods=lstPeriods.get(0);
		}
	private void FillActivityList()
	{
		lstActivity=new ArrayList<String>();
		lstActivity.add("All");
		lstActivity.add("Salary");
		lstActivity.add("Absence");	
		lstActivity.add("Leave");
		lstActivity.add("Loan");
		lstActivity.add("EOS");
		lstActivity.add("Addition/Deduction");
		
		selectedActivity=lstActivity.get(0);
	}		
	
		@Command
		@NotifyChange({"lstEmployee","activityModel","message","totalRecords","canExport"})
	    public void searchCommand()
		{			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
			List<ActivityModel> lstActivity=new ListModelList<ActivityModel>();
			int year=Integer.parseInt(simpleDateformat.format(toDate)) - Integer.parseInt(simpleDateformat.format(fromDate));
			message="";
			totalRecords="";
			this.setCanExport(false);
			
			if(selectedCompEmployee.getEmployeeKey()==0)
			{				 			      
			      if(year > 2)
			      {
			    	  message="Maximum two years allowed if you select all employees !!";
			    	  activityModel=new EmployeeLedgerModel(lstActivity, new ActivityComparator(), showGroup);
			    	  return;
			      }
			      
			}
			else
			{
				 if(year > 5)
			      {
			    	  message="Maximum five years periods allowed !!";
			    	  activityModel=new EmployeeLedgerModel(lstActivity, new ActivityComparator(), showGroup);
			    	  return;
			      }
			}
			
			
			if(selectedCompEmployee.getEmployeeKey()!=0)
			{
				if(selectedActivity.equals("All"))
				{
				lstActivity.addAll(data.GetEMPSalary(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));		
				lstActivity.addAll(data.GETEmpABSENCE(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				lstActivity.addAll(data.GetEMPLeave(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				lstActivity.addAll(data.GetEMPLoan(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				lstActivity.addAll(data.GetEMPEOS(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				lstActivity.addAll(data.GetEMPADTrx(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				lstActivity.addAll(data.GetPAIDSalary(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Salary"))
				{
					lstActivity.addAll(data.GetEMPSalary(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));			
					lstActivity.addAll(data.GetPAIDSalary(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Absence"))
				{
					lstActivity.addAll(data.GETEmpABSENCE(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Leave"))
				{
					lstActivity.addAll(data.GetEMPLeave(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Loan"))
				{
					lstActivity.addAll(data.GetEMPLoan(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("EOS"))
				{
					lstActivity.addAll(data.GetEMPEOS(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Addition/Deduction"))
				{
					lstActivity.addAll(data.GetEMPADTrx(selectedCompEmployee, sdf.format(fromDate),sdf.format(toDate)));
				}
			}
			else
			{
			List<Integer> lstEmpKey=data.GetAllEMPSalary(sdf.format(fromDate),sdf.format(toDate));
			List<Integer> lstAbscEmpKey=data.GETAllEmpABSENCE(sdf.format(fromDate),sdf.format(toDate));
			List<Integer> lstLeaveEmpKey=data.GetAllEMPLeave(sdf.format(fromDate),sdf.format(toDate));
			List<Integer> lstLoanEmpKey=data.GetAllEMPLoan(sdf.format(fromDate),sdf.format(toDate));
			List<Integer> lstEOSEmpKey=data.GetAllEMPEOS(sdf.format(fromDate),sdf.format(toDate));
			List<Integer> lstADEmpKey=data.GetAllEMPADTrx(sdf.format(fromDate),sdf.format(toDate));
			
			lstEmployee=data.GetEmployeeLedgerList(selectedCompany.getCompKey(),selectedStatus,showEOS,selectedDepartment.getListId(),selectedPosition.getListId());						
			for (EmployeeModel item : lstEmployee) 
			{
								
				if(selectedActivity.equals("All"))
				{
				if(lstEmpKey.contains(item.getEmployeeKey()))	
				lstActivity.addAll(data.GetEMPSalary(item,sdf.format(fromDate),sdf.format(toDate)));		
				if(lstAbscEmpKey.contains(item.getEmployeeKey()))
				lstActivity.addAll(data.GETEmpABSENCE(item, sdf.format(fromDate),sdf.format(toDate)));
				if(lstLeaveEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPLeave(item, sdf.format(fromDate),sdf.format(toDate)));
				if(lstLoanEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPLoan(item, sdf.format(fromDate),sdf.format(toDate)));
				if(lstEOSEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPEOS(item, sdf.format(fromDate),sdf.format(toDate)));
				if(lstADEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPADTrx(item, sdf.format(fromDate),sdf.format(toDate)));
				if(lstEmpKey.contains(item.getEmployeeKey()))	
					lstActivity.addAll(data.GetPAIDSalary(item, sdf.format(fromDate),sdf.format(toDate)));		
					
				
				}
				else if(selectedActivity.equals("Salary"))
				{
					if(lstEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPSalary(item, sdf.format(fromDate),sdf.format(toDate)));		
				}
				else if(selectedActivity.equals("Absence"))
				{
					if(lstAbscEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GETEmpABSENCE(item, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Leave"))
				{
				    if(lstLeaveEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPLeave(item, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("Loan"))
				{
				    if(lstLoanEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPLoan(item, sdf.format(fromDate),sdf.format(toDate)));
				}
				else if(selectedActivity.equals("EOS"))
				{
					if(lstEOSEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPEOS(item, sdf.format(fromDate),sdf.format(toDate)));					
				}	
				else if(selectedActivity.equals("Addition/Deduction"))
				{
					if(lstADEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetEMPADTrx(item, sdf.format(fromDate),sdf.format(toDate)));					
				}
				
				if(selectedActivity.equals("Salary"))
				{
					if(lstEmpKey.contains(item.getEmployeeKey()))
					lstActivity.addAll(data.GetPAIDSalary(item, sdf.format(fromDate),sdf.format(toDate)));		
				}
			 }
			}
			//activityModel=new EmployeeLedgerModel(data.GetEMPSalary(192, "192", sdf.format(fromDate),sdf.format(toDate), selectedCompEmployee.getStatus(),"Chadi Rahme"), new ActivityComparator(), showGroup);
			double totalBalance=0;
			int tmpEmpKey=0;
			int totalEmpCount=0;
			double totalEmpBalance=0;
			
			for (ActivityModel item : lstActivity) 
			{							
				if(item.getEmpKey()!=tmpEmpKey)
				{
					tmpEmpKey=item.getEmpKey();
					totalBalance=0;
					totalEmpCount++;
				}
				//ActivityModel obj=new ActivityModel();				
				totalBalance = totalBalance +(item.getAmount());
				item.setBalance(totalBalance);
				
				totalEmpBalance+=item.getAmount();
				//obj.setBalance(totalBalance);
			}	
			activityModel=new EmployeeLedgerModel(lstActivity, new ActivityComparator(), showGroup);
			
			  for (int i = 0; i < activityModel.getGroupCount(); i++)
			   {
				  activityModel.removeOpenGroup(i);
			   }
			  
			if(lstActivity.size()==0)
			{
				message="There is no data found for this criteria";
			}
			else
			{
				 this.setCanExport(true);
				totalRecords="Total Employees :" + String.valueOf(totalEmpCount) ;//+ " Total Employees Balance :" + formatter.format(totalEmpBalance);
			}
			
		}
		
		 @Command
		 @NotifyChange({"activityModel","isOpenGroup"})
		 public void colseGroup()
		 {
			 isOpenGroup=!isOpenGroup;
			 for (int i = 0; i < activityModel.getGroupCount(); i++)
			   {
				 if(isOpenGroup==false)
					 activityModel.removeOpenGroup(i);
				 else
					 activityModel.addOpenGroup(i);
			   } 
		 }
		 
		  
				
		
		private void sortActivityModel(List<ActivityModel> lstActivity)
		{
			double totalBalance=0;
			//List<ActivityModel> lstSort=new ListModelList<ActivityModel>();
			for (ActivityModel item : lstActivity) 
			{
				ActivityModel obj=new ActivityModel();				
				totalBalance = totalBalance +(item.getAmount());
				item.setBalance(totalBalance);
				obj.setBalance(totalBalance);
			}			
		}
				
		public EmployeeLedgerModel getActivityModel()
		{	     
			return activityModel;
		    //new EmployeeLedgerModel(data.GetEMPSalary(192, "192", sdf.format(fromDate),sdf.format(toDate), selectedCompEmployee.getStatus(),"Chadi Rahme"), new ActivityComparator(), showGroup);	     
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


	@NotifyChange({"lstCompEmployees","selectedCompEmployee"})
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
		lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"All",status,supervisorID);
		selectedCompEmployee=lstCompEmployees.get(0);
	}

	public List<String> getLstPeriods() {
		return lstPeriods;
	}

	public void setLstPeriods(List<String> lstPeriods) {
		this.lstPeriods = lstPeriods;
	}

	public String getSelectedPeriods() {
		return selectedPeriods;
	}
	
	@NotifyChange({"fromDate","toDate","selectedPeriods"})
	public void setSelectedPeriods(String selectedPeriods) 
	{
		this.selectedPeriods = selectedPeriods;
		try
		{
		//change DateFrom and DateTo
		Calendar c = Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH);
		int day=c.get(Calendar.DAY_OF_MONTH);
		
		if(selectedPeriods.equals("Today"))
		{						
			fromDate=df.parse(sdf.format(c.getTime()));		
			toDate=df.parse(sdf.format(c.getTime()));
		}
		if(selectedPeriods.equals("Yesterday"))
		{						
			c.add(Calendar.DAY_OF_MONTH,-1);			  
			fromDate=df.parse(sdf.format(c.getTime()));		
			toDate=df.parse(sdf.format(c.getTime()));
		}
		
		if(selectedPeriods.equals("This Month"))
		{									
			c.set(year,month,1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c.set(year, month,c.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate=df.parse(sdf.format(c.getTime()));
		}
				
		if(selectedPeriods.equals("Last Month"))
		{									
			c.set(year,month,1);
			c.add(Calendar.MONTH, -1);
			fromDate=df.parse(sdf.format(c.getTime()));
			 year=c.get(Calendar.YEAR);
			 month=c.get(Calendar.MONTH);			
			c.set(year, month,c.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate=df.parse(sdf.format(c.getTime()));
		}
		
		if(selectedPeriods.equals("Next Month"))
		{									
			c.set(year,month,1);
			c.add(Calendar.MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			 year=c.get(Calendar.YEAR);
			 month=c.get(Calendar.MONTH);
			c.set(year, month,c.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate=df.parse(sdf.format(c.getTime()));
		}
		
		
		if(selectedPeriods.equals("This Week"))
		{									
			c.setFirstDayOfWeek(Calendar.SATURDAY);	
			//c.add(Calendar.DAY_OF_MONTH, c.getFirstDayOfWeek() * -1);
			c.set(year,month,c.getFirstDayOfWeek());
			fromDate=df.parse(sdf.format(c.getTime()));			
			c.add(Calendar.DAY_OF_MONTH, 6);
			toDate=df.parse(sdf.format(c.getTime()));
		}
		if(selectedPeriods.equals("Last Week"))
		{									
			c.setFirstDayOfWeek(Calendar.SATURDAY);	
			c.add(Calendar.DAY_OF_MONTH, c.getFirstDayOfWeek() * -1);
			c.set(year,month,c.getFirstDayOfWeek());
			c.add(Calendar.DAY_OF_MONTH, (c.getFirstDayOfWeek() * -1) -7 );
			fromDate=df.parse(sdf.format(c.getTime()));			
			c.add(Calendar.DAY_OF_MONTH, 6);
			toDate=df.parse(sdf.format(c.getTime()));
		}
		if(selectedPeriods.equals("Next Week"))
		{									
			c.setFirstDayOfWeek(Calendar.SATURDAY);	
			c.add(Calendar.DAY_OF_MONTH, c.getFirstDayOfWeek() * -1);
			c.set(year,month,c.getFirstDayOfWeek());
			c.add(Calendar.DAY_OF_MONTH, (c.getFirstDayOfWeek()) + 7);
			fromDate=df.parse(sdf.format(c.getTime()));			
			c.add(Calendar.DAY_OF_MONTH, 6);
			toDate=df.parse(sdf.format(c.getTime()));
		}
		
		
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in setSelectedPeriods ----> init", ex);			
		}
		
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

	public List<String> getLstStatus() {
		return lstStatus;
	}

	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	@NotifyChange({"enableEOS","showEOS","lstCompEmployees","selectedCompEmployee"})
	public void setSelectedStatus(String selectedStatus) 
	{
		String status="";
		this.selectedStatus = selectedStatus;
		if(selectedStatus.equals("All"))
		{
			enableEOS=true;			
		}
		else if(selectedStatus.equals("EOS"))
		{
			status="Y";
			enableEOS=false;
			showEOS=true;
		}
		else if(selectedStatus.equals("Active"))
		{
			status="A";
			enableEOS=false;
			showEOS=false;
		}
		else if(selectedStatus.equals("Inactive"))
		{
			status="I";
			enableEOS=true;
			showEOS=false;
		}
		
		lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"All",status,supervisorID);
 		selectedCompEmployee=lstCompEmployees.get(0);
		
	}

	public List<EmployeeModel> getLstEmployee() {
		return lstEmployee;
	}

	public void setLstEmployee(List<EmployeeModel> lstEmployee) {
		this.lstEmployee = lstEmployee;
	}

	public List<String> getLstActivity() {
		return lstActivity;
	}

	public void setLstActivity(List<String> lstActivity) {
		this.lstActivity = lstActivity;
	}

	public String getSelectedActivity() {
		return selectedActivity;
	}

	public void setSelectedActivity(String selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public List<EmployeeModel> getLstCompEmployees() {
		return lstCompEmployees;
	}

	public void setLstCompEmployees(List<EmployeeModel> lstCompEmployees) {
		this.lstCompEmployees = lstCompEmployees;
	}

	public EmployeeModel getSelectedCompEmployee() {
		return selectedCompEmployee;
	}

	public void setSelectedCompEmployee(EmployeeModel selectedCompEmployee) 
	{
		this.selectedCompEmployee = selectedCompEmployee;
		
	}

	public boolean isShowEOS() {
		return showEOS;
	}

	public void setShowEOS(boolean showEOS) {
		this.showEOS = showEOS;
	}

	public boolean isEnableEOS() {
		return enableEOS;
	}

	public void setEnableEOS(boolean enableEOS) {
		this.enableEOS = enableEOS;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}

	public boolean isOpenGroup() {
		return isOpenGroup;
	}

	public void setOpenGroup(boolean isOpenGroup) {
		this.isOpenGroup = isOpenGroup;
	}

	public boolean isCanExport() {
		return canExport;
	}

	public void setCanExport(boolean canExport) {
		this.canExport = canExport;
	}

}
