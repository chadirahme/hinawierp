package timesheet;

import hba.HBAData;
import home.MailClient;
import hr.HRData;
import hr.model.CompanyModel;
import hr.model.LeaveModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import layout.MenuModel;
import model.CompSetupModel;
import model.CompanySettingsModel;
import model.CompanyShiftModel;
import model.ContentGCM;
import model.EmployeeFilter;
import model.EmployeeModel;
import model.HRListValuesModel;
import model.OverTimeModel;
import model.ProjectModel;
import model.QbListsModel;
import model.SalaryMasterModel;
import model.ShiftModel;
import model.TimeSheetDataModel;
import model.TimeSheetGridModel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.GroupRenderer;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.excel.ExcelExporter.ExportContext;
import org.zkoss.exporter.pdf.FontFactory;
import org.zkoss.exporter.pdf.PdfExporter;
import org.zkoss.exporter.pdf.PdfPCellFactory;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.xssf.usermodel.XSSFCellStyle;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;
import admin.TasksModel;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import common.POST2GCM;
import company.CompanyData;

public class DailyTimeSheetViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData hrdata=new HRData();
	TimeSheetData data=new TimeSheetData();
	HRData Hrdata=new HRData();
	HBAData HBadata=new HBAData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
	
	
	//private TimeSheetGroupModel tsGroupModel;
	private TimeSheetGroupAdapter tsGroupModel;
	
	private String lastTimeSheet;
	private String lastTimeSheetByUser;
	private boolean timingFlag;	
	private boolean canExport;
	
	private List<String> lstStatus;
	private String selectedStatus;
	private List<String> lstCalculate;
	private List<ProjectModel> lstProject;
	private List<QbListsModel> lstCustomerJob;
	private List<HRListValuesModel> lstPositions;
	private List<OverTimeModel> lstOTMax;
	
	private List<TimeSheetGridModel>  lstGrid;
	private boolean isOpenGroup;
	
	public List<TimeSheetGridModel> getLstGrid() {
		return lstGrid;
	}

	public void setLstGrid(List<TimeSheetGridModel> lstGrid) {
		this.lstGrid = lstGrid;
	}

	private List<String> lstMonths;
	private String selectedMonth;
	private List<String> lstYears;
	private String selectedYear;
	private int selectedDateType;
	private boolean showMonth;
	private int supervisorID;
	private int employeeKey;
	private String footer;
	@SuppressWarnings("unused")
	private int totalEmployeeNumber;
	private String searchText;
	private CompanySettingsModel compSettings;
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	private String INCLUDEHOLIDAY_UNIT;
	private CompSetupModel objCompanySetup;
	
	int menuID=134;
	private MenuModel companyRole;
	private int UserId;
	String viewType;
	
	private TimeSheetGridModel selectedRow;
	
	WebusersModel dbUser=null;
	
	private String supervisorEmail;
	
	private CompSetupModel compSetup;
	
	private boolean sendEmailFlag=false;
	
	private boolean adminUser=false;
	
	private boolean checkAllStatus;
	private boolean checkAllCalculate;
	
	private List<HRListValuesModel> lstTaskStatus;
	private List<TasksModel> lstCustomerTaks;
	
	private String tempTomrrowsPlanForEmail="";
	//HashMap<Integer, List<TasksModel>> hmCustomerTasks = new HashMap<Integer, List<TasksModel>>(); 
	private int webuserID=0;
	private String webuserName="";
	
 	@Init
    public void init(@BindingParam("type") String type)
 	{
 		logger.info("type> Init>> "+ type);
 		viewType=type;
 		if(viewType.equals("add"))
 			menuID=134;
 		else if(viewType.equals("edit"))
 			menuID=136;
 		else if(viewType.equals("view"))
 			menuID=135;
 		
 		Session sess = Sessions.getCurrent();		
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");		
		getCompanyRolePermessions(dbUser.getCompanyroleid());
 		
 		fillStatus();
    }
 	
	public DailyTimeSheetViewModel()
	{
		try
		{				
			Calendar c = Calendar.getInstance();	
			fromDate=df.parse(sdf.format(c.getTime()));		
			toDate=df.parse(sdf.format(c.getTime()));
			
			//fromDate=df.parse("01/10/2010");//df.parse(sdf.format(c.getTime()));		
			//toDate=df.parse("02/10/2010");//df.parse(sdf.format(c.getTime()));
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			supervisorID=dbUser.getSupervisor();
			setEmployeeKey(dbUser.getEmployeeKey());
			UserId=dbUser.getUserid();
			//getCompanyRolePermessions(dbUser.getCompanyroleid());
			if(dbUser!=null)
			{
				if(dbUser.getCompanyid()==1)
				adminUser=dbUser.getFirstname().equals("admin");

				if(adminUser)
				{
					webuserID=0;
					webuserName="Admin";
				}
				else
				{
					webuserID=dbUser.getUserid();
					webuserName=dbUser.getUsername();
				}
			}
			
			int defaultCompanyId=0;
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);
			
			//lstComapnies=data.getCompanyList(1);
			//if(lstComapnies.size()>1)
			//selectedCompany=lstComapnies.get(0);
			
			getCompanyTimeSheetSettings(dbUser.getCompanyid());
			
			
			if(selectedCompany!=null)
			{			
			getLastTimeSheetCreated();
			compSetup=Hrdata.getLeaveCompanySetup(selectedCompany.getCompKey());
			lstOTMax=data.getMaxOTList(selectedCompany.getCompKey());
			objCompanySetup=data.getCompanySetup(selectedCompany.getCompKey());	
			}
			
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
			}
			//fillStatus();
			
			//Auto Approve Timesheet
			if(c.get(Calendar.DAY_OF_WEEK)==7)//Saturday
			{
				if(compSettings!=null && compSettings.isAutoApprove())
				{			
					data.autoApproveTimeSheet();
				}
		   }
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DailyTimeSheetViewModel ----> init", ex);			
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
	private void getCompanyTimeSheetSettings(int companyid)
	{
		CompanyData compData=new CompanyData();
		setCompSettings(compData.getCompanySettings(companyid));
	}
	
	private void fillStatus()
	{
		lstStatus=new ArrayList<String>();
		lstStatus.add("Absence");
		lstStatus.add("Present");		
		
		//lstStatus.add("Sick");
		//lstStatus.add("Leave");
		//lstStatus.add("Holiday");
		
		lstCalculate=new ArrayList<String>();
		lstCalculate.add("Yes");
		lstCalculate.add("No");
		
		if(selectedCompany!=null)
		{
		if(viewType.equals("add"))
		lstProject=data.getProjectList(selectedCompany.getCompKey(),"",true,supervisorID);
		else
		lstProject=data.getProjectList(selectedCompany.getCompKey(),"",false,supervisorID);
		}
		
		lstPositions=data.getHRListValues(47,"Select");
		lstCustomerJob=HBadata.fillQbList("'Customer'");
		/*int j=0;
		lstCustomerJob=new ArrayList<QbListsModel>();
		while(j < 200)
		{
			QbListsModel obj=new QbListsModel();
			obj.setName("a" + j++);
			obj.setListType("vendor");
			lstCustomerJob.add(obj);
		}*/
		
		lstMonths=new ArrayList<String>();
		//lstMonths.add("Select");
		for (int i = 1; i < 13; i++) 
		{
		lstMonths.add(String.valueOf(i));	
		}
		
		Calendar c = Calendar.getInstance();
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
		
		lstTaskStatus=data.getHRListValues(143,"Select");
		
		//fill user task list
		int webUserID=0;
		int supervisorID=0;
		int employeeKey=0;
		webUserID=dbUser.getUserid();
		supervisorID=dbUser.getSupervisor();//logged in as supervisor
		employeeKey=dbUser.getEmployeeKey();
		if(employeeKey>0)
			supervisorID=employeeKey;//logged in as employee		
		if(supervisorID>0)
			webUserID=supervisorID;
		adminUser=dbUser.getFirstname().equals("admin");
		if(adminUser)
		{
			webUserID=0;
		}
		 lstCustomerTaks=data.getCustomerTasks(webUserID,"Number");
	}
	
	@SuppressWarnings("unused")
	@Command
	 @NotifyChange({"tsGroupModel","timingFlag","canExport","footer"})
	 public void viewCommand()
	 {	
		 try
		  {
			 this.setTimingFlag(false);
			 this.setCanExport(false);			
			 Date _fromDate;
			 Date  _toDate;
			
			 //default
			  _fromDate=fromDate;
			   _toDate=toDate;
			   
			 if(compSettings.getDateType().equals("4"))//today and yesterday
			   {
				   if(!adminUser)
				   {
				   Calendar ctoday = Calendar.getInstance();	
				   Date today=new Date();
				   Date yesterday=new Date();
				   ctoday.setTime(today);
					  
				   if(fromDate.after(today) || toDate.after(today))
				   {
					   Messagebox.show("Only today and yesterday !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   ctoday = Calendar.getInstance();		
				   ctoday.add(Calendar.DAY_OF_MONTH, -1);
				   //ctoday.setTime(yesterday);
				   
				   yesterday=df.parse(sdf.format(ctoday.getTime()));													
				   
				   if(fromDate.before(yesterday) || toDate.before(yesterday))
				   {
					   Messagebox.show("Only today and yesterday !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   _fromDate=fromDate;
				   _toDate=toDate;
				   }
			   }
			 
			 if(compSettings.getDateType().equals("5"))//today and prev week
			   {
				   if(!adminUser)
				   {
				   Calendar ctoday = Calendar.getInstance();	
				   Date today=new Date();
				   Date yesterday=new Date();
				   ctoday.setTime(today);
					  
				   if(fromDate.after(today) || toDate.after(today))
				   {
					   Messagebox.show("Only Today and Prev. week !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   ctoday = Calendar.getInstance();		
				   ctoday.add(Calendar.DAY_OF_MONTH, -6);
				   //ctoday.setTime(yesterday);
				   
				   yesterday=df.parse(sdf.format(ctoday.getTime()));						
					
				   //logger.info("yest >> " + yesterday);
				   
				   if(fromDate.before(yesterday) || toDate.before(yesterday))
				   {
					   Messagebox.show("Only Today and Prev. week !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   }
			   }
			 
			 if(compSettings.getDateType().equals("6"))//today and tomorrow
			   {
				   if(!adminUser)
				   {
				   Calendar ctoday = Calendar.getInstance();	
				   Date today=new Date();
				   Date nextday=new Date();
				   ctoday.setTime(today);
					
				   today=df.parse(sdf.format(ctoday.getTime()));	
				   
				   if(fromDate.before(today) || toDate.before(today))
				   {
					   Messagebox.show("Only Today and next day !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   ctoday = Calendar.getInstance();		
				   ctoday.add(Calendar.DAY_OF_MONTH, 1);
				   //ctoday.setTime(yesterday);
				   
				   nextday=df.parse(sdf.format(ctoday.getTime()));						
					
				   //logger.info("yest >> " + yesterday);
				   
				   if(fromDate.after(nextday) || toDate.after(nextday))
				   {
					   Messagebox.show("Only Today and next day !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }	
				   }
			   }
			 
			 
			 
			if(compSettings.getDateType().equals("3"))//data or month
			{
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
				 _fromDate=df.parse(sdf.format(c.getTime()));	//c.getTime();
				 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
				 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
				 _toDate=df.parse(sdf.format(c.getTime()));				 
			 }
		  }
			 
			 
			  lstGrid=new ArrayList<TimeSheetGridModel>();
			  int srNo=0;
			  totalEmployeeNumber=0;
			  int tempEmpKey=0;
			  int tempRecNO=0;
			  boolean tmpFirstRecNo;
			  
			  String empInSalarySheet="",empInApprovedSheet="";
			  Calendar c = Calendar.getInstance();
			  c.setTime(_fromDate);
			  int  monthFrom=c.get(Calendar.MONTH);
			  int  yearFrom=c.get(Calendar.YEAR);
			  int monthTotalDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			  
			  List<EmployeeModel> lstEmp=new ArrayList<EmployeeModel>();
			  if(employeeKey>0)
			  lstEmp=data.GetEmployeeListFromFilter(employeeKey+"");
			  else
			  lstEmp= data.GetEmployeeListInDailyTS(_fromDate, _toDate, selectedCompany.getCompKey(),supervisorID);
			  
			   for (EmployeeModel item : lstEmp)
			   {
				  
				   //String salaryStatus=getSalaryStatus(lstSalaryMaster, item.getEmployeeKey());
				   //ProjectModel projectName=getProjectName(item.getLocationId());
				   //HRListValuesModel serviceName=getServiceName(item.getPositionID());
				   	
				 //check if employee generate salary for this month
				   if(viewType.equals("edit"))
				   {
				      boolean isSalaryGenerate=false;
					  isSalaryGenerate=data.checkEmployeeSalarySheet(item.getEmployeeKey(), monthFrom+1, yearFrom);
					  if(isSalaryGenerate)
			    	  {
			    		  empInSalarySheet+=item.getFullName() + " , ";
			    		  continue;
			    	  }
					  int totalApprovedRecords=data.checkIfTimeSheetApproved(item.getEmployeeKey(), monthFrom+1, yearFrom);
					  if(totalApprovedRecords==monthTotalDays)
					  {
						  empInApprovedSheet+=item.getFullName() + " , ";
			    		  continue;
					  }
				   }
					  
					  				   				  
				   List<TimeSheetDataModel> lstDataFromTimeSheet=data.GetDataFromTimeSheet(selectedCompany.getCompKey(), item.getEmployeeKey(), _fromDate, _toDate); 
				   List<TimeSheetDataModel> lstDataFromOverTime=data.GetDataFromOverTime(item.getEmployeeKey(), _fromDate, _toDate);
				   								   
				 //check if has leave and approved
				  List<LeaveModel> lstLeaves=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
				  LeaveModel objLeave =null;							 
				   for (TimeSheetDataModel tsTimeSheet : lstDataFromTimeSheet) 
				   {
					   TimeSheetGridModel obj=new TimeSheetGridModel();
					   tmpFirstRecNo=false;
					   					 					   			
					   if(tempEmpKey!=tsTimeSheet.getEmpKey())
					   {
						   srNo++;
						   totalEmployeeNumber++;
						  tempEmpKey=tsTimeSheet.getEmpKey();
					   }
					   obj.setLstTaskStatus(lstTaskStatus);		
					   obj.setCompSettings(compSettings);
					   obj.setLstCustomerTaks(lstCustomerTaks);
					   obj.setRecNo(tsTimeSheet.getRecNo());
					   obj.setOldRecNo(obj.getRecNo());
					   obj.setListOfattchments(data.getTimesheetAttchamnet(obj.getOldRecNo()));
					   obj.setSrNo(srNo);
					   obj.setEmpKey(tsTimeSheet.getEmpKey());
					   obj.setCompKey(selectedCompany.getCompKey());					 
					   obj.setEmpNo(item.getEmployeeNo());
						obj.setEnFullName(item.getFullName());
						obj.setEmployeeStatus(item.getStatus());
						obj.setPosition(item.getPosition());
						obj.setEmployeementDate(item.getEmployeementDate());
						
						
						obj.setLineNo(tsTimeSheet.getLineNO());
						//obj.setNoOfshifts(tsTimeSheet.getNoOfshifts());
					    obj.setNoOfshifts(getNoOfShift(lstDataFromTimeSheet, tsTimeSheet.getTsDate(), obj.getEmpKey()));//tsTimeSheet.getNoOfshifts());
						obj.setShiftRecNo(tsTimeSheet.getShiftRecNo());
						  
						obj.setTsDate(tsTimeSheet.getTsDate());
						obj.setDayName(getDayName(tsTimeSheet.getTsDate()));					 
						obj.setTimingFlag(tsTimeSheet.getTiming().equals("Y"));
						  
						obj.setProject(getProjectName(tsTimeSheet.getProjectkey()));						
						obj.setService(getServiceName(tsTimeSheet.getServiceId()));	 
						obj.setCustomerJob(getCustomerJob(tsTimeSheet.getCustomerJobRefKey()));
						obj.setApproved(tsTimeSheet.getApproved());
						obj.setEmployeementDate(item.getEmployeementDate());					   					 
						obj.setTomorrowPlan(tsTimeSheet.getTomorrowPlan());
						obj.setAttachPath(tsTimeSheet.getAttachPath());
						
						String tsStatus="Present";
					   if(tsTimeSheet.getStatus().equals("P"))
					   tsStatus="Present";
					   else if(tsTimeSheet.getStatus().equals("H"))
					   {
					   tsStatus="Holiday";
					   obj.setCantChange(true);
					   }
					   else if(tsTimeSheet.getStatus().equals("S"))
					   tsStatus="Sick";  
					   else if(tsTimeSheet.getStatus().equals("L"))
					   {
					    obj.setCantChange(true);	
					   tsStatus="Leave";
					   }
					   else if(tsTimeSheet.getStatus().equals("A"))
					   tsStatus="Absence";
					   obj.setStatus(tsStatus);
					   obj.setNotes(tsTimeSheet.getNotes());
					   
					   obj.setHoliday(tsTimeSheet.getOffDay().equals("Y"));
					   obj.setUnits(tsTimeSheet.getUnitNO());
					   obj.setNormalHours(tsTimeSheet.getNormalUnitNO());
					   obj.setTotals(tsTimeSheet.getTotalUnitNo());
					   obj.setTotalNormalHours(tsTimeSheet.getTotalNormalUnitNo());
					   
					   obj.setHolidayDesc(getHolidayDesc(tsTimeSheet));
					  // obj.setHoliday(tsTimeSheet.getStatus().equals("P") ? false :true);
					   obj.setCalculate(tsTimeSheet.getCalcFlag().equals("Y")?"Yes" : "No");
					   obj.setUnitType(tsTimeSheet.getUnitName());
					   obj.setShiftkey((int)tsTimeSheet.getShiftKey());
					   obj.setUnitKey(tsTimeSheet.getUnitId());
					   obj.setLeaveFlag(tsTimeSheet.getLeaveFlag());
					   if(tsTimeSheet.getTiming().equals("Y"))
			    	   {
						   this.timingFlag=true;
						   obj.setTimingFlag(true);
						   obj.setFromTime(tsTimeSheet.getFromTime());
						   obj.setToTime(tsTimeSheet.getToTime());
						   obj.setTsFromTime(tsTimeSheet.getTsFromTime());
						   obj.setTstoTime(tsTimeSheet.getTstoTime());
						   
						   if(tempRecNO!=tsTimeSheet.getRecNo())
							{						
							   tempRecNO=tsTimeSheet.getRecNo();
							   obj.setFirstOfRecord(true);							
							}
			    	   }
					   
					   //check Over Time
					   List<TimeSheetDataModel> tmpOverTime=getDataFromOverTime(lstDataFromOverTime, tsTimeSheet.getRecNo());
						  for (TimeSheetDataModel otItem : tmpOverTime)
						   {
							   if(otItem.getCalculation()==1.25 && otItem.getLineNO()==obj.getLineNo())
								   obj.setOtUnit1((int)otItem.getUnits());
							   if(otItem.getCalculation()==1.5)
								   obj.setOtUnit2((int)otItem.getUnits());
							   if(otItem.getCalculation()==2)
								   obj.setOtUnit3((int)otItem.getUnits());						   
						   }
						 //  obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());
						   obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());
						   obj.setTotalOTUnits((int)tsTimeSheet.getTotalOTUnits());
						   
					   obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
					   obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
					   obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
					   
					   //check for leave
					   objLeave=checkIfEmployeeInLeave(lstLeaves, obj.getTsDate());
					   if(objLeave!=null && objLeave.getRecNO()>0)
						{							
						   if((objLeave.getLeaveStartDate().equals(obj.getTsDate()) || objLeave.getLeaveEndDate().equals(obj.getTsDate()))&& (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
							{		
							   if(obj.isHoliday())
								  {
									obj.setStatus("Holiday");
									obj.setLeaveFlag("Y");
									obj.setNotes("On leave");
								  }
								 else
								 {
								  obj.setStatus("Leave");
								  obj.setLeaveFlag("");		
								  obj.setNotes(objLeave.getLeaveTypeDesc());
								 }
						
							obj.setCalculate("No");							
							obj.setCantChange(true);	
							obj.setUnits(0);
							obj.setTotals(0);
							//obj.setHoliday(true);
							obj.setProject(null);
							}
							else if(objLeave.getLeaveStartDate().before(obj.getTsDate()) && objLeave.getLeaveEndDate().after(obj.getTsDate()) && (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
							{	
								 if(obj.isHoliday())
								  {
									obj.setStatus("Holiday");
									obj.setLeaveFlag("Y");
									obj.setNotes("On leave");
								  }
								 else
								 {
								  obj.setStatus("Leave");
								  obj.setLeaveFlag("");		
								  obj.setNotes(objLeave.getLeaveTypeDesc());
								 }
								 
							
							obj.setCalculate("No");					
							obj.setCantChange(true);
							obj.setUnits(0);
							obj.setTotals(0);
							//obj.setHoliday(true);
							obj.setProject(null);
							}
							
						}
					   					  					 										   					
					   if(obj.getEmpKey()>0)
					   {
						rowIndex++;
						obj.setRowIndex(rowIndex);
					   lstGrid.add(obj);					
					   }
				   }
				   
			   }
			   
			   if(!empInSalarySheet.equals(""))
			     {
			    	 Messagebox.show(empInSalarySheet + " Salary Sheet is Already created for the month, Please recreate the salary sheet if the changes needs to be incorporated in the Salary Sheet !! ","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			     }
			     
			     if(!empInApprovedSheet.equals(""))
			     {
			    	 Messagebox.show(empInApprovedSheet + " TimeSheet for the month has been created and Approved. !! ","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			     }
			     
			   tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
			   footer=" Total Employees: " + tsGroupModel.getGroupCount();
			   for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			   {
				   tsGroupModel.removeOpenGroup(i);
			   }
			   
			   if(lstGrid.size()>0)
				   this.setCanExport(true);
			  
		  }
		 catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> viewCommand", ex);			
			}
	 }
	private LeaveModel checkIfEmployeeInLeave( List<LeaveModel> lstLeaves,Date tsDate)
	{
		LeaveModel retLeave=null;
		if(lstLeaves==null || lstLeaves.size()==0)
			return null;
		try 
		{
			for (LeaveModel objLeave : lstLeaves) 
			{
				if((objLeave.getLeaveStartDate().equals(tsDate) || objLeave.getLeaveEndDate().equals(tsDate))&& (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
				{
					return objLeave;
				}
				else if(objLeave.getLeaveStartDate().before(tsDate) && objLeave.getLeaveEndDate().after(tsDate) && (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
				{
					return objLeave;
				}
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DailyTimeSheetViewModel ----> checkIfEmployeeInLeave", ex);			
		}
		return retLeave;
	}
	
	 @SuppressWarnings("unused")
	@Command
	 @NotifyChange({"tsGroupModel","timingFlag","canExport"})
	 public void searchCommandNotUsed()
	 {			
		  try
		  {
			  int monthFrom=0;
			  int monthTo=0;
			  int yearFrom=0;
			  int yearTo=0;
			  
			  Date tmpSelectDate;
			  int tmpSelectEMP=0;
			  
			  //lstHourlyGrid=new ListModelList<TimeSheetGridModel>();
			  this.setTimingFlag(false);
			  this.setCanExport(false);
			  
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
			   
			   CompSetupModel objCompany=data.getCompanySetup(selectedCompany.getCompKey());			  
			   c.set(objCompany.getPayrollYear(),objCompany.getPayrollMonth(),1);
			   if(c.getTime().after(fromDate))
			   {
				   Messagebox.show("Company payroll date starts from " + objCompany.getPayrollMonth() + " / " + objCompany.getPayrollYear(),"Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
			   }
			   
			   //Get Salary Master for this period
			   c.setTime(fromDate);
			   monthFrom=c.get(Calendar.MONTH);
			   yearFrom=c.get(Calendar.YEAR);
			  
			  // List<SalaryMasterModel> lstSalaryMaster=data.GetMasterSalarySheet(monthFrom+1, yearFrom);
			  // List<ProjectModel> lstProject=data.getProjectList(selectedCompany.getCompKey());
			 //  List<HRListValuesModel> lstPositions=data.getHRListValues(47,"");
				
			   lstGrid=new ArrayList<TimeSheetGridModel>();
			  int srNo=0;
			  int tempEmpKey=0;
			  int tempRecNO=0;
			  boolean tmpFirstRecNo;
			  
			   List<EmployeeModel> lstEmp= data.GetEmployeeListInDailyTS(fromDate, toDate, selectedCompany.getCompKey(),supervisorID);
			   for (EmployeeModel item : lstEmp)
			   {
				  
				   //String salaryStatus=getSalaryStatus(lstSalaryMaster, item.getEmployeeKey());
				   ProjectModel projectName=getProjectName(item.getLocationId());
				   HRListValuesModel serviceName=getServiceName(item.getPositionID());
				   
				  				   
				   List<TimeSheetDataModel> lstDataFromTimeSheet=data.GetDataFromTimeSheet(selectedCompany.getCompKey(), item.getEmployeeKey(), fromDate, toDate); 
				   List<TimeSheetDataModel> lstDataFromOverTime=data.GetDataFromOverTime(item.getEmployeeKey(), fromDate, toDate);
				   
				 //  List<TimeSheetDataModel> lstSetup =data.GetDataFromSetup(selectedCompany.getCompKey(), item.getEmployeeKey(),null,null);
				 //  TimeSheetDataModel tmpSetupData= getDataFromSetup(lstSetup, item.getEmployeeKey(),0,null);
				   
				   for (TimeSheetDataModel tsTimeSheet : lstDataFromTimeSheet) 
				   {
					   TimeSheetGridModel obj=new TimeSheetGridModel();
					   tmpFirstRecNo=false;
					   
					  // if(tmpSetupData.getEmpKey()>0)
					   {					
						   if(tempEmpKey!=tsTimeSheet.getEmpKey())
						   {
							   srNo++;
							  tempEmpKey=tsTimeSheet.getEmpKey();
						   }
						   						  					   						   					
						   obj.setSrNo(srNo);
						   obj.setEmpKey(tsTimeSheet.getEmpKey());
						   obj.setCompKey(selectedCompany.getCompKey());
						   obj.setShiftkey((int)tsTimeSheet.getShiftKey());
						   obj.setUnitKey(tsTimeSheet.getUnitId());
						//   obj.setSalaryStatus(salaryStatus);
						 //  obj.setEmpNo(tmpSetupData.getEmpNo());
						  // obj.setEnFullName(tmpSetupData.getEnFullName());
						   obj.setEmpNo(item.getEmployeeNo());
						   obj.setEnFullName(item.getFullName());
						   obj.setEmployeeStatus(item.getStatus());
						   obj.setPosition(item.getPosition());
						   projectName=getProjectName(tsTimeSheet.getProjectkey());
						   obj.setProject(projectName);
						   serviceName=getServiceName(tsTimeSheet.getServiceId());
						   obj.setService(serviceName);
						   
					   }
					 
					// tmpSelectDate=tsSetup.getTsDate();
					 //TimeSheetDataModel tmpTSData= getDataFromTimeSheet(lstDataFromTimeSheet, tsSetup.getEmpKey(), tmpSelectDate);
					
					   obj.setTsDate(tsTimeSheet.getTsDate());
					   if(tsTimeSheet.getTiming().equals("Y"))
					   {
						   obj.setTimingFlag(true);
						   this.setTimingFlag(true);
						   obj.setFromTime(tsTimeSheet.getFromTime());
						   obj.setToTime(tsTimeSheet.getToTime());
						   
						   if(tempRecNO!=tsTimeSheet.getRecNo())
						   {
							   tmpFirstRecNo=true;
							   tempRecNO=tsTimeSheet.getRecNo();
						   }	
					   }
					   obj.setFirstOfRecord(tmpFirstRecNo);
					   obj.setDayName(getDayName(tsTimeSheet.getTsDate()));					 				  
					   
					   String tsStatus="Present";
					   if(tsTimeSheet.getStatus().equals("P"))
					   obj.setStatus("Present");
					   else if(tsTimeSheet.getStatus().equals("H"))
					   tsStatus="Holiday";  
					   else if(tsTimeSheet.getStatus().equals("S"))
					   tsStatus="Sick";  
					   else if(tsTimeSheet.getStatus().equals("L"))
					   tsStatus="Leave";  
					   else if(tsTimeSheet.getStatus().equals("A"))
					   tsStatus="Absence";
					   
					   obj.setStatus(tsStatus);
					   obj.setHolidayDesc(getHolidayDesc(tsTimeSheet));
					   obj.setHoliday(tsTimeSheet.getStatus().equals("P") ? false :true);
					   obj.setCalculate(tsTimeSheet.getCalcFlag().equals("Y")?"Yes" : "No");
					   obj.setUnitType(tsTimeSheet.getUnitName());
					   obj.setUnits((int)tsTimeSheet.getUnitNO());
					   obj.setTotals((int)tsTimeSheet.getTotalUnitNo());
					   obj.setNotes(tsTimeSheet.getNotes());
					   
					   List<TimeSheetDataModel> tmpOverTime=getDataFromOverTime(lstDataFromOverTime, tsTimeSheet.getRecNo());
					   for (TimeSheetDataModel otItem : tmpOverTime)
					   {
						   if(otItem.getCalculation()==1.25)
							   obj.setOtUnit1((int)otItem.getUnits());
						   if(otItem.getCalculation()==1.5)
							   obj.setOtUnit2((int)otItem.getUnits());
						   if(otItem.getCalculation()==2)
							   obj.setOtUnit3((int)otItem.getUnits());						   
					   }
					   obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());
					   
					   obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
					   obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
					   obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
						
					   if(obj.getEmpKey()>0)
					   {											
					   lstGrid.add(obj);					
					   }
				   }
				   
			   }
			   
			   tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
			   for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			   {
				   tsGroupModel.removeOpenGroup(i);
			   }
			   
			   if(lstGrid.size()>0)
				   this.setCanExport(true);
		  }
		  catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> searchCommand", ex);			
			}
	 }
	 
	 @Command
	 @NotifyChange({"tsGroupModel","isOpenGroup"})
	 public void colseGroup()
	 {
		 if(lstGrid==null)
		 {   		   
   		   return;
		 }
		 
		 isOpenGroup=!isOpenGroup;
		 for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
		   {
			 if(isOpenGroup==false)
			   tsGroupModel.removeOpenGroup(i);
			 else
			   tsGroupModel.addOpenGroup(i);
		   } 
	 }
	  
	 //This not used
	 private List<TimeSheetDataModel> getDataFromSetup( List<TimeSheetDataModel> lstSetup,int empKey,int dayNo,Date tsDate)	  
	  {
		// logger.info("dayno>>> " + dayNo + " >>Date >>" + sdf.format(tsDate));
		List<TimeSheetDataModel> result=new ArrayList<TimeSheetDataModel>();
		  for (TimeSheetDataModel item : lstSetup) 
		  {
			  if(item.getTsDate().equals(tsDate) && item.getDayNo()==dayNo)
			  {
				result.add(item);
			  }
		  }
		  return result;
	  }
	 
	 private double getTotalHours(List<TimeSheetDataModel> lstSetup,int empKey,int dayNo,Date tsDate)	
	 {
		 double result=0;
		 for (TimeSheetDataModel item : lstSetup) 
		  {
			  if(item.getTsDate().equals(tsDate) && item.getDayNo()==dayNo)
			  {
				result+=item.getUnitNO();
			  }
		  }
		 return result;
	 }
	  
	  private  List<TimeSheetDataModel> getDataFromOverTime( List<TimeSheetDataModel> lstDataFromOverTime,int RecNO)	  
	  {
		  List<TimeSheetDataModel> result=new ArrayList<TimeSheetDataModel>();
		  for (TimeSheetDataModel item : lstDataFromOverTime) 
		  {
			  if(item.getRecNo()==RecNO)				  
			  {
				  result.add(item);				
			  }
		  }
		  return result;
	  }
	  
	  private String getHolidayDesc(TimeSheetDataModel item)
	  {
		  String holiday="";
		  boolean tmpHoliday=false;
		  if(!item.getHolidayDesc().equals(""))
			{
				tmpHoliday=true;
			}
			if(item.getStatus().equals("H") || tmpHoliday==true)
			{
				item.setHoliday(true);
			}
			if(tmpHoliday)
			{
				holiday=item.getHolidayDesc();
			}
			else if(item.getStatus().equals("H"))
			{
			    holiday="Offday";				
			}
		  
		  return holiday;
	  }
	  
	//This not used
	  @SuppressWarnings("unused")
	private TimeSheetDataModel getDataFromTimeSheet(List<TimeSheetDataModel> lstDataFromTimeSheet,int empKey,Date tsDate)	  
	  {
		  TimeSheetDataModel result=new TimeSheetDataModel();
		  boolean tmpHoliday=false;
		  for (TimeSheetDataModel item : lstDataFromTimeSheet) 
		  {
			if(item.getEmpKey()==empKey && item.getTsDate().equals(tsDate))
			{
				if(!item.getHolidayDesc().equals(""))
				{
					tmpHoliday=true;
				}
				if(item.getStatus().equals("H") || tmpHoliday==true)
				{
					item.setHoliday(true);
				}
				if(tmpHoliday)
				{
					item.setHolidayDesc(item.getHolidayDesc());
				}
				else if(item.getStatus().equals("H"))
				{
					item.setHolidayDesc(" Offday");
				}
				return item;
			}
		 }
		  return result;
	  }
	  
	  private String getDayName(Date tsDate)
	  {
		  String dayName="";
		  Calendar cal = Calendar.getInstance();
          cal.setTime(tsDate);
          //String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
          dayName =new SimpleDateFormat("EEEE").format(cal.getTime()); //days[calendar.get(Calendar.DAY_OF_WEEK-1)];
		  return dayName;
	  }
	  
	  @SuppressWarnings("unused")
	private String getSalaryStatus(List<SalaryMasterModel> lstMaster,int empKey)
	  {
		  String result="";
		  for (SalaryMasterModel item : lstMaster) 
		  {
			if(item.getEmpKey()==empKey)
			{
				if(item.getSalaryStatus().equals("A") || item.getSalaryStatus().equals("P"))
					result="Salary Sheet Created & Approved, Can't be Change";
				else
					result="Salary Sheet Created";
				 return result;
			}
		  }
		  
		  return result;
	  }
	  
	  private ProjectModel getProjectName(int locId)
	  {
		  ProjectModel result=null;
		  for (ProjectModel item : lstProject) 
		  {
			if(item.getProjectKey()==locId)
			{
				result=item;
				return result;
			}
		  }
		  
		  return result;
	  }
	  
	  private HRListValuesModel getServiceName(int positionId)
	  {
		  HRListValuesModel result=null;
		  if(positionId==0)
			  return result;
		  
		  for (HRListValuesModel item : lstPositions) 
		  {
			if(item.getListId()==positionId)
			{
				result=item;
				return result;
			}
		  }
		  
		  return result;
	  }
	  
	  private QbListsModel getCustomerJob(int cutomerJobRefKey)
	  {
		  QbListsModel result=null;
		  for (QbListsModel item : lstCustomerJob) 
		  {
			if(item.getRecNo()==cutomerJobRefKey)
			{
				result=item;
				return result;
			}
		  }
		  
		  return result;
	  }
	  
	 @SuppressWarnings("unused")
	private void getLastTimeSheetCreated()
	 {
		 try
		 {
		 List<TimeSheetDataModel> lst=data.getLastTimeSheetCreated(selectedCompany.getCompKey());
		 lastTimeSheet="No Time Sheet created..";
		 if(lst.size()>0)
		 {
		 TimeSheetDataModel obj=lst.get(0);
		  Calendar c = Calendar.getInstance();	
		  c.setTime(obj.getTsDate());
		  String monthName =new SimpleDateFormat("MMMM").format(c.getTime());
		  int year= c.get(Calendar.YEAR);		  
		  int totalEmp=0;
		  List<Integer> lstKey=new ArrayList<Integer>();
		  for (TimeSheetDataModel item : lst) 
		  {
			if(!lstKey.contains(item.getEmpKey()))
				lstKey.add(item.getEmpKey());
		  }
		  lastTimeSheet="Last Time sheet : " + monthName + "/" + String.valueOf(year) + String.format(" ( for %s Employees )", lstKey.size());
		  
		  /*
		    int month=c.get(Calendar.MONTH);
		    c.set(year,month,1);
		    fromDate=df.parse(sdf.format(c.getTime()));
			c.set(year, month,c.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate=df.parse(sdf.format(c.getTime()));	
			*/	  
		 }
		 
		 //get if time sheet Approved
		 List<String> lstStatus=new ArrayList<String>();
		 for (TimeSheetDataModel item : lst) 
		 {
			if(!lstStatus.contains(item.getStatus()))
				lstStatus.add(item.getStatus());
		 }
		 
		 if(lstStatus.size()==2)
		 {
			 lastTimeSheet+="  - Partly Approved ";
		 }
		 else if(lstStatus.size()==1)
		 {
			 if(lstStatus.get(0).equals("0"))
				 lastTimeSheet+="  - Created ";
			 if(lstStatus.get(0).equals("1"))
				 lastTimeSheet+="  - Approved ";
		 }
		 
		 if(supervisorID>0)			 
		 {
			 List<TimeSheetDataModel> lstUser=data.getLastTimeSheetCreatedByUser(selectedCompany.getCompKey(),supervisorID);
			 lastTimeSheetByUser="No Time Sheet created by you..";
			 if(lstUser.size()>0)
			 {
			 TimeSheetDataModel obj=lstUser.get(0);
			  Calendar c = Calendar.getInstance();	
			  c.setTime(obj.getTsDate());
			  String monthName =new SimpleDateFormat("MMMM").format(c.getTime());
			  int year= c.get(Calendar.YEAR);		  
			  lastTimeSheetByUser="Last Time sheet by : "+lstUser.get(0).getSupervisorName() + " " + monthName + "/" + String.valueOf(year) + String.format(" ( for %s Employees )", lstUser.size());			  			  
			 }
			 
			//get if time sheet Approved
			 List<String> lstUserStatus=new ArrayList<String>();
			 for (TimeSheetDataModel item : lstUser) 
			 {
				if(!lstUserStatus.contains(item.getStatus()))
					lstUserStatus.add(item.getStatus());
			 }
			 
			 if(lstUserStatus.size()==2)
			 {
				 lastTimeSheetByUser+="  - Partly Approved ";
			 }
			 else if(lstUserStatus.size()==1)
			 {
				 if(lstUserStatus.get(0).equals("0"))
					 lastTimeSheetByUser+="  - Created ";
				 if(lstUserStatus.get(0).equals("1"))
					 lastTimeSheetByUser+="  - Approved ";
			 }
			 			 			
		 }
		 
		}
		 catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> getLastTimeSheetCreated", ex);			
			}
	 }
	 
	 @Command
	 @NotifyChange({"tsGroupModel"})
	 public void refreshCommand()
	 {
		 
	 }
	 @Command
	 public void addDatesCommand()
	 {
		 data.deletetmpTSDates();
		 data.addtmpTSDates(fromDate, toDate);
		 		
	 }
	 
	 @Command
	 @NotifyChange({"tsGroupModel"})
	 public void changeCalculate(@BindingParam("row") TimeSheetGridModel row)
	 {
		 DateFormat htdf = new SimpleDateFormat("hh:mm a");
		 DateTimeFormatter  hdf = DateTimeFormat.forPattern ("hh:mm a");
			
		 if(row.getCalculate().equals("No"))
		  {												
				double totals=row.getTotals() - row.getUnits();
				if(totals<0)
					totals=0;
				row.setUnits(0);
				if(row.isTimingFlag())
				{
					 for (TimeSheetGridModel item : lstGrid) 
					 {
						 if(row.getEmpKey()==item.getEmpKey() && row.getTsDate().equals(item.getTsDate()))
						 {
							 item.setTotals(totals);
							 row.setTotals(totals);
							 //logger.info("total>> " + item.getTotals());
						 }						
					 }	
				}
				else
				{
				row.setTotals(totals);//row.getTotals() - row.getUnits());	
				row.setUnits(0);
				}
				
				if(checkAllCalculate)
				{
					for (TimeSheetGridModel item : lstGrid) 
					 {
						if(row.getEmpKey()==item.getEmpKey())
						 {
							item.setCalculate("No");
							item.setUnits(0);
							item.setTotals(0);
						 }
					 }
				}
			}
		 else
		 {
			 if(row.isTimingFlag())
			  {
				 if(row.getTsFromTime()!=null)
				  {								
				 DateTime dt1=hdf.parseDateTime(htdf.format(row.getTsFromTime()));
				 DateTime dt2=hdf.parseDateTime(htdf.format(row.getTstoTime()));
				 DecimalFormat twoDForm = new DecimalFormat("#.##");
				 Period period = new Period(dt1,dt2);
				 double units=(double)period.getHours() +(double)  period.getMinutes()/(double)60;
				 units=Double.valueOf(twoDForm.format(units));
				 if(units<0)
				 {
					 Messagebox.show("From time should not exceed To Time !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 row.setTstoTime(row.getTsFromTime());
					 row.setUnits(0);
				 }
				  				
				 else
				 {
				 row.setUnits(units);
				 }
				  }
				 
				//row.setUnits(row.getNormalHours()); //(row.getRealUnits());
				double totals=row.getTotals() + row.getUnits();
				for (TimeSheetGridModel item : lstGrid) 
				 {
					 if(row.getEmpKey()==item.getEmpKey() && row.getTsDate().equals(item.getTsDate()))
					 {
						 item.setTotals(totals);
						 row.setTotals(totals);
						// logger.info("total>> " + item.getTotals());
					 }						
				 }	
				}
				else
				{
					row.setUnits(1);
					row.setTotals(1);
				}	
			 
			   if(checkAllCalculate)
				{
					for (TimeSheetGridModel item : lstGrid) 
					 {
						if(row.getEmpKey()==item.getEmpKey())
						 {
							item.setCalculate("Yes");
							if(!row.isTimingFlag())
							 {
							item.setUnits(1);
							item.setTotals(1);
							 }
							else
							{
								if(item.getTsFromTime()!=null)
								{
								DateTime dt1=hdf.parseDateTime(htdf.format(item.getTsFromTime()));
								DateTime dt2=hdf.parseDateTime(htdf.format(item.getTstoTime()));
								Period period = new Period(dt1,dt2);
								double units=(double)period.getHours() +(double)  period.getMinutes()/(double)60;
								item.setUnits(units);
								}
								else
								{
									item.setUnits(0);
								}
							}
						 }
					 }
					
					//set total Hours
					 for (TimeSheetGridModel item : lstGrid) 
					 {
						 if(item.isTimingFlag())
						 item.setTotals(getTotalDateHours(item.getTsDate(), item.getEmpKey()));
					 }
				}
		 }
	 }
	 
	 @Command
	 @NotifyChange({"tsGroupModel"})
	 public void changeStatus(@BindingParam("row") TimeSheetGridModel row)
	 {
		 	DateTimeFormatter  hdf = DateTimeFormat.forPattern ("hh:mm a");
		 	  DateFormat htdf = new SimpleDateFormat("hh:mm a");
		 	if(row.getStatus().equals("Holiday"))
			{
				Messagebox.show("Can't set holiday,You change Shift Setup!!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				row.setStatus("Present");
				//status="Present";
			}
			if(row.getStatus().equals("Leave"))
			{
				Messagebox.show("Can't set Leave,Please Create Leave from Activities Menu!!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				row.setStatus("Present");
				//status="Present";
			}
			
			if(row.getStatus().equals("Sick"))
			{
				Messagebox.show("Can't set Sick,Please Create Sick Leave from Activities Menu!!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				row.setStatus("Present");
				//status="Present";
			}
			
			if(row.getStatus().equals("Present"))
			{
				row.setCalculate("Yes");
				if(row.isTimingFlag())
				{
				row.setUnits(row.getNormalHours()); //(row.getRealUnits());
				double totals=row.getTotals() + row.getUnits();
				for (TimeSheetGridModel item : lstGrid) 
				 {
					 if(row.getEmpKey()==item.getEmpKey() && row.getTsDate().equals(item.getTsDate()))
					 {
						 item.setTotals(totals);
						 row.setTotals(totals);
						// logger.info("total>> " + item.getTotals());
					 }						
				 }	
				}
				else
				{
					row.setUnits(1);
					row.setTotals(1);
				}	
				
				if(checkAllStatus)
				{
										
					for (TimeSheetGridModel item : lstGrid) 
					 {
						 if(row.getEmpKey()==item.getEmpKey())
						 {							
							 item.setStatus("Present");
							 item.setCalculate("Yes");
							 if(!item.isTimingFlag())
							 {
							 item.setTotals(1);
							 item.setUnits(1);
							 }
							 else
							 {
								 if(item.getTsFromTime()!=null)
								  {
								 DateTime dt1=hdf.parseDateTime(htdf.format(item.getTsFromTime()));
								 DateTime dt2=hdf.parseDateTime(htdf.format(item.getTstoTime()));
								 Period period = new Period(dt1,dt2);
								 double units=(double)period.getHours() +(double)  period.getMinutes()/(double)60;
								 item.setUnits(units);
								 //item.setTotals(getTotalDateHours(item.getTsDate(), item.getEmpKey()));
								  }
								
							 }
						 }
						 
					 }
					
					//set total Hours
					 for (TimeSheetGridModel item : lstGrid) 
					 {
						 if(item.isTimingFlag())
						 item.setTotals(getTotalDateHours(item.getTsDate(), item.getEmpKey()));
					 }
				}
			}
			if(row.getStatus().equals("Absence"))
			{								
				row.setCalculate(lstCalculate.get(1));
				double totals=row.getTotals() - row.getUnits();
				if(totals<0)
					totals=0;
				row.setUnits(0);
				if(row.isTimingFlag())
				{
					 for (TimeSheetGridModel item : lstGrid) 
					 {
						 if(row.getEmpKey()==item.getEmpKey() && row.getTsDate().equals(item.getTsDate()))
						 {
							 item.setTotals(totals);
							 row.setTotals(totals);
							 //logger.info("total>> " + item.getTotals());
						 }						
					 }	
				}
				else
				{
				row.setTotals(totals);//row.getTotals() - row.getUnits());	
				row.setUnits(0);
				}
				
				if(checkAllStatus)
				{
					for (TimeSheetGridModel item : lstGrid) 
					 {
						 if(row.getEmpKey()==item.getEmpKey())
						 {
							 item.setStatus("Absence");
							 item.setCalculate(lstCalculate.get(1));
							 item.setTotals(0);//row.getTotals() - row.getUnits());	
							 item.setUnits(0);
						 }
						 
					 }
				}
			}
	 }
	 private double getTotalDateHours(Date tsDate,int empKey)
	 {
		 double totalHours=0;
		 for (TimeSheetGridModel item : lstGrid) 
		 {
			 if(empKey==item.getEmpKey() && tsDate.equals(item.getTsDate()))
			 {
				 totalHours+=item.getUnits();
			 }
		 }
		 return totalHours;
	 }
	 
	 private boolean isOTDisable(double type,int shiftKey,boolean isHoliday)
	 {
		 boolean isDisableOT=true;
		 String dayType=isHoliday?"H":"N";
		 
		 for (OverTimeModel item : lstOTMax) 
		 {
			 if(item.getOtRate()==type &&item.getPositionId()==shiftKey)
			 {
				 if(item.getDayType().equals(""))
				 {
				 isDisableOT=false;
				 return isDisableOT;
				 }
				 else if(item.getDayType().equals(dayType))
				 {
					 isDisableOT=false;
					 return isDisableOT; 
				 }
			 }
		 
		 }				 		 
		 return isDisableOT;
	 }
	
	 @SuppressWarnings("unused")
	@Command
	 @NotifyChange({"tsGroupModel"})
	 public void checkOT(@BindingParam("row") TimeSheetGridModel row,@BindingParam("type") int type)
	 {
		 try
		 {
		 boolean isHoliday=row.isHoliday();
		 String dayType=isHoliday?"H":"N";
		 //logger.info("shiftkey>>> " + row.getShiftkey());
		 DateFormat htdf = new SimpleDateFormat("hh:mm a");
		 DateTimeFormatter  hdf = DateTimeFormat.forPattern ("hh:mm a");
		 if(type==1)
		 {
			 if(row.getOtUnit1()<0)
			 {
				 Messagebox.show("Only positive number or zero is allowed !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				 row.setOtUnit1(0);				
			 }
			 
			 for (OverTimeModel item : lstOTMax) 
			 {
				if(row.isTimingFlag()==false)
				{
					
				 if(item.getOtRate()==1.25 && (item.getDayType().equals("") || item.getDayType().equals(dayType) )  &&item.getPositionId()==row.getShiftkey())
				 {
					 row.setMaxOTAmount(item.getHours());
					 if(row.getOtUnit1()>item.getHours())
					 {
						 Messagebox.show("You can enter "+ item.getHours() + " Days or less","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setOtUnit1((int)item.getHours());						
					 }
				 }
				}
				else //check as total shift for rec 
				{
					int totalOTUnits=0;
					for (TimeSheetGridModel tsRecord : lstGrid)
					{
						if(row.getEmpKey()==tsRecord.getEmpKey() && row.getTsDate().equals(tsRecord.getTsDate()) && row.getDayName().equals(tsRecord.getDayName()))
						{
							totalOTUnits+=tsRecord.getOtUnit1();
						}
					}
					if(item.getOtRate()==1.25 && (item.getDayType().equals("") || item.getDayType().equals(dayType) )  &&item.getPositionId()==row.getShiftkey())
					{		
					 row.setMaxOTAmount(item.getHours());	
					 if(totalOTUnits>item.getHours())
					 {
						 Messagebox.show("You can enter "+ item.getHours() + " Days or less","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setOtUnit1(0);						
					 }
					}
					
					//change To Time
					if(objCompanySetup.getTimesheetTimeAuto().equals("Y"))
					{
					Calendar cFrom = Calendar.getInstance();
					int tmpMinutes=(int) ( (row.getUnits()+row.getOtUnit1())*60) ;
					cFrom.setTime(htdf.parse(htdf.format(row.getTsFromTime())));
					cFrom.add(Calendar.MINUTE, tmpMinutes);
					row.setTstoTime(cFrom.getTime());
					}
				}
			 }
		 }
		 if(type==2)
		 {
			 if(row.getOtUnit2()<0)
			 {
				 Messagebox.show("Only positive number or zero is allowed !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				 row.setOtUnit2(0);				
			 }
			 
			 for (OverTimeModel item : lstOTMax) 
			 {
				 if(row.isTimingFlag()==false)
				 {					 				
				 if(item.getOtRate()==1.5 && (item.getDayType().equals("") || item.getDayType().equals(dayType) ) &&item.getPositionId()==row.getShiftkey())
				 {
					 row.setMaxOTAmount(item.getHours());
					 if(row.getOtUnit2()>item.getHours())
					 {
						 Messagebox.show("You can enter "+ item.getHours() + " Days or less","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setOtUnit2((int)item.getHours());
					 }
				 }
				}
				 else //check as total shift for rec 
					{
						int totalOTUnits=0;
						for (TimeSheetGridModel tsRecord : lstGrid)
						{
							if(row.getEmpKey()==tsRecord.getEmpKey() && row.getRecNo() == tsRecord.getRecNo() && row.getDayName().equals(tsRecord.getDayName()))
							{
								totalOTUnits+=tsRecord.getOtUnit2();
							}
						}
						if(item.getOtRate()==1.5 && (item.getDayType().equals("") || item.getDayType().equals(dayType)) &&item.getPositionId()==row.getShiftkey())
						{		
						 row.setMaxOTAmount(item.getHours());	
						 if(totalOTUnits>item.getHours())
						 {
							 Messagebox.show("You can enter "+ item.getHours() + " Days or less","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
							 row.setOtUnit2(0);						
						 }
						}
					}
			 }
		 }
		 if(type==3)
		 {
			 if(row.getOtUnit3()<0)
			 {
				 Messagebox.show("Only positive number or zero is allowed !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				 row.setOtUnit3(0);				
			 }
			 
			 for (OverTimeModel item : lstOTMax) 
			 {
				if(row.isTimingFlag()==false)
				{	
				 if(item.getOtRate()==2 && (item.getDayType().equals("") || item.getDayType().equals(dayType) )  &&item.getPositionId()==row.getShiftkey())
				 {
					 row.setMaxOTAmount(item.getHours());
					 if(row.getOtUnit3()>item.getHours())
					 {
						 Messagebox.show("You can enter "+ item.getHours() + " Days or less","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setOtUnit3((int)item.getHours());
					 }
				 }
				}
				 else //check as total shift for rec 
					{
						int totalOTUnits=0;
						for (TimeSheetGridModel tsRecord : lstGrid)
						{
							if(row.getEmpKey()==tsRecord.getEmpKey() && row.getRecNo() == tsRecord.getRecNo() && row.getDayName().equals(tsRecord.getDayName()))
							{
								totalOTUnits+=tsRecord.getOtUnit3();
							}
						}
						if(item.getOtRate()==2 && (item.getDayType().equals("") || item.getDayType().equals(dayType) )  &&item.getPositionId()==row.getShiftkey())
						{		
						 row.setMaxOTAmount(item.getHours());	
						 if(totalOTUnits>item.getHours())
						 {
							 Messagebox.show("You can enter "+ item.getHours() + " Days or less","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
							 row.setOtUnit3(0);						
						 }
						}
					}
			 }
		 }
		 
		 int totalUnits=0;
		 for (TimeSheetGridModel item : lstGrid)
		 {					
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			{
				totalUnits+=item.getOtUnit1()+item.getOtUnit2()+item.getOtUnit3();
			}
		 }
		 
		 for (TimeSheetGridModel item : lstGrid)
		 {
			
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			{
				
				item.setTotalOTUnits(totalUnits);
			}
		 }
		 
		 }
		 catch(Exception ex)
		 {
			 logger.error("error at checkOT >> ", ex);
		 }
	 }
	 
	 @Command
	 public void autoFillCommand()
	 {
		 try
		 {
		 if(lstGrid==null)
		 {
			 Messagebox.show("Please select Employee !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		 }
		 
		  Date _fromDate;
		  Date  _toDate;
		  Calendar c = Calendar.getInstance();
		  if(selectedDateType==1)//by dates
		   {
			  _fromDate=fromDate;
			  _toDate=toDate;
		   }
		  else
		  {					 
			  c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,1);
			 _fromDate=df.parse(sdf.format(c.getTime()));
			 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
			 _toDate=df.parse(sdf.format(c.getTime()));			 		
		  }
		  
		  
		 Map<String,Object> arg = new HashMap<String,Object>();
		 arg.put("compKey", selectedCompany.getCompKey());
		 arg.put("fromDate", _fromDate);
		 arg.put("toDate", _toDate);
		 List<Integer> lstEmpKeys=new ArrayList<Integer>();
		 for (TimeSheetGridModel item : lstGrid) 
		 {
			lstEmpKeys.add(item.getEmpKey());   
		 }
		 arg.put("lstEmpKeys", lstEmpKeys);
		 Executions.createComponents("/timesheet/autofill.zul", null,arg);
		 }
		 catch(Exception ex)
		 {
			 
		 }
	 }
	 @GlobalCommand 
	 @NotifyChange({"tsGroupModel"})
	  public void autofillClose(@BindingParam("projectId")int projectId,@BindingParam("fromDate")Date fromDate,@BindingParam("toDate")Date toDate
			  ,@BindingParam("positionId") int positionId,@BindingParam("lstEmployeeId") List<Integer> lstEmployeeId
			  ,@BindingParam("ot1") int ot1,@BindingParam("ot2") int ot2,@BindingParam("ot3") int ot3)
	  {		
		 
		 if(lstGrid!=null)
		 {
			  int dayFrom=0;			 			 
			  int dayTo=0;
			  int diffDay=0;
			  
			  Calendar c = Calendar.getInstance();	
			  c.setTime(fromDate);
			  dayFrom=c.get(Calendar.DAY_OF_MONTH);			
			  c.setTime(toDate);				
			  dayTo=c.get(Calendar.DAY_OF_MONTH);			  
			  diffDay=dayTo-dayFrom +1;			  			
			  boolean isValidOT=true;			  			 
				 
			  for (TimeSheetGridModel item : lstGrid) 
			  {
				if(lstEmployeeId.contains(item.getEmpKey()))
				{	
				
					for (int i = 0; i < diffDay; i++) 
					   {			  
					   c.setTime(fromDate);
					   c.add(Calendar.DAY_OF_MONTH, i);
					   
					 //  logger.info(">>>" + item.getTsDate());
					  // logger.info(">>>" + c.getTime());
					   
					   if(item.getTsDate().equals(c.getTime()))
					   {
						if(item.isCantChange())
						{
							continue;
						}
						if(projectId>0)
						item.setProject(getProjectName(projectId));
						if(positionId>0)
						item.setService(getServiceName(positionId));
						if(ot1>=0)
						{
							if(checkAutoFillOT(item, 1.25,ot1))
							item.setOtUnit1(ot1);
							else 
							isValidOT=false;
						}
						if(ot2>=0)
						{
							if(checkAutoFillOT(item, 1.5,ot2))
							item.setOtUnit2(ot2);
							else 
								isValidOT=false;
						}
						if(ot3>=0)
						{
							if(checkAutoFillOT(item, 2,ot3))
							item.setOtUnit3(ot3);
							else 
								isValidOT=false;
						}
					   }
					 }
				}
			  }
			  
			  //update OT total
			  for (TimeSheetGridModel row : lstGrid)
			  {
			 	 int totalUnits=0;
				 for (TimeSheetGridModel item : lstGrid)
				 {					
					 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
					{
						totalUnits+=item.getOtUnit1()+item.getOtUnit2()+item.getOtUnit3();
					}
				 }
				 
				 for (TimeSheetGridModel item : lstGrid)
				 {
					
					 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
					{
						item.setTotalOTUnits(totalUnits);
					}
				 }
			  }
				 
			  if(isValidOT==false)
			  {
				  Messagebox.show("You entered over time hours more than values allowed in setup !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			  }
		 }
		
		 tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
	  }
	 
	 public boolean checkAutoFillOT(TimeSheetGridModel row,double type,int hours)
	 {
		 boolean isValid=false;
		 boolean isHoliday=row.isHoliday();
		 String dayType=isHoliday?"H":"N";
		
		 if(hours==0)
			 return true;
		 else
		 {
		 for (OverTimeModel item : lstOTMax) 
		 {			
			 if(item.getOtRate()==type && (item.getDayType().equals("") || item.getDayType().equals(dayType) ) &&item.getPositionId()==row.getShiftkey())
			 {
				 row.setMaxOTAmount(item.getHours());
				 if(hours<=item.getHours())
				 {
					 isValid=true;	
					 return isValid;
				 }
			 }
		 }
		 
		 return isValid;
		 }
	 }
	 
	 @Command
	 @NotifyChange({"tsGroupModel","timingFlag","canExport","footer"})
	 public void addTimesheetCommand()
	 {
		 try
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
			   
			   if(fromDate.after(toDate))
			   {
				   Messagebox.show("From date must be less than To Date !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }
			   
			   //check if there is data for other company
			  if(lstGrid!=null)
			  {
			   for (TimeSheetGridModel item : lstGrid) 
			   {
				if(item.getCompKey()!=selectedCompany.getCompKey())
				{
					Messagebox.show("You can't add employee from another company before save the current time sheet !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}			
			   }
			  }
			   
			   if(monthFrom!=monthTo || yearFrom !=yearTo)
			   {
					Messagebox.show("Month of From Date & Month of To date should be Same !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
			   }
			   		   		  
			   CompSetupModel objCompany=data.getCompanySetup(selectedCompany.getCompKey());
			   INCLUDEHOLIDAY_UNIT=objCompany.getIncludeholidayUnit();
			   c.set(objCompany.getPayrollYear(),objCompany.getPayrollMonth(),1);
			   if(c.getTime().after(fromDate))
			   {
				   Messagebox.show("Company payroll date starts from " + objCompany.getPayrollMonth() + " / " + objCompany.getPayrollYear(),"Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }
			   
			   if(compSettings.getDateType().equals("4"))//today and yesterday
			   {
				   if(!adminUser)
				   {
				   Calendar ctoday = Calendar.getInstance();	
				   Date today=new Date();
				   Date yesterday=new Date();
				   ctoday.setTime(today);
					  
				   if(fromDate.after(today) || toDate.after(today))
				   {
					   Messagebox.show("Only today and yesterday !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   ctoday = Calendar.getInstance();		
				   ctoday.add(Calendar.DAY_OF_MONTH, -1);
				   //ctoday.setTime(yesterday);
				   
				   yesterday=df.parse(sdf.format(ctoday.getTime()));						
					
				   //logger.info("yest >> " + yesterday);
				   
				   if(fromDate.before(yesterday) || toDate.before(yesterday))
				   {
					   Messagebox.show("Only today and yesterday !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   }
			   }
			   
			   if(compSettings.getDateType().equals("5"))//today and prev week
			   {
				   if(!adminUser)
				   {
				   Calendar ctoday = Calendar.getInstance();	
				   Date today=new Date();
				   Date yesterday=new Date();
				   ctoday.setTime(today);
					  
				   if(fromDate.after(today) || toDate.after(today))
				   {
					   Messagebox.show("Only Today and Prev. week !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   ctoday = Calendar.getInstance();		
				   ctoday.add(Calendar.DAY_OF_MONTH, -6);
				   //ctoday.setTime(yesterday);
				   
				   yesterday=df.parse(sdf.format(ctoday.getTime()));						
					
				   //logger.info("yest >> " + yesterday);
				   
				   if(fromDate.before(yesterday) || toDate.before(yesterday))
				   {
					   Messagebox.show("Only Today and Prev. week !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }	
				   }
			   }
			   
			   if(compSettings.getDateType().equals("6"))//today and tomorrow
			   {
				   if(!adminUser)
				   {
				   Calendar ctoday = Calendar.getInstance();	
				   Date today=new Date();
				   Date nextday=new Date();
				   ctoday.setTime(today);
					
				   today=df.parse(sdf.format(ctoday.getTime()));	
				   
				   if(fromDate.before(today) || toDate.before(today))
				   {
					   Messagebox.show("Only Today and next day !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }
				   ctoday = Calendar.getInstance();		
				   ctoday.add(Calendar.DAY_OF_MONTH, 1);
				   //ctoday.setTime(yesterday);
				   
				   nextday=df.parse(sdf.format(ctoday.getTime()));						
					
				   //logger.info("yest >> " + yesterday);
				   
				   if(fromDate.after(nextday) || toDate.after(nextday))
				   {
					   Messagebox.show("Only Today and next day !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
					   return;
				   }	
				   }
			   }
			   
			  int dayFrom=0;			 	 
			  int dayTo=0;
			  int diffDay=0;
			  String empNotInSetup="";
			  int tempEmpKey=0;
			  
			  Date _fromDate=fromDate;
			  Date  _toDate=toDate;
			  c = Calendar.getInstance();
				
			  if(compSettings.getDateType().equals("3"))//date or month
			  {
			  if(selectedDateType==0)//by Month
			   {
				  c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,1);
					 _fromDate=df.parse(sdf.format(c.getTime()));
					 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
					 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
					 _toDate=df.parse(sdf.format(c.getTime()));	
			   }				  
			 }
				
			  c.setTime(_fromDate);
			  dayFrom=c.get(Calendar.DAY_OF_MONTH);				
			  c.setTime(_toDate);				
			  dayTo=c.get(Calendar.DAY_OF_MONTH);				  
			  diffDay=dayTo-dayFrom +1;
			  
			  //add tmp dates in tmpTSDates
			  data.deletetmpTSDates();
			  data.addtmpTSDates(_fromDate, _toDate);
			  
			  String tmpNewKeys="";	  
			  tmpNewKeys=employeeKey+"";
			  lstGrid=new ArrayList<TimeSheetGridModel>();
			 
			  if(lstGrid==null)
			  {
			     lstGrid=new ArrayList<TimeSheetGridModel>();			   
			  }
				
			  List<EmployeeModel> lstEmp=data.GetEmployeeListFromFilter(tmpNewKeys);
			  for (EmployeeModel item : lstEmp)
			   {
				  boolean isOldDataFound=false;
				  boolean isOldDateFound=false;
				  List<TimeSheetDataModel> lstSetup=null;
				  
			      //get the shiftkey and TIMING_FLAG for employee from EMPSHIFT
			      CompanyShiftModel empShift=data.getEmployeeShiftKey(item.getEmployeeKey(), _fromDate, _toDate,selectedCompany.getCompKey());
			      if(empShift.getShiftKey()>0)//if no employee shift no need to check
			      {
			    	  ProjectModel employeeProject=getProjectName(item.getLocationId());
			    	  HRListValuesModel employeeService=getServiceName(item.getPositionID());
			    	//check if has leave and approved
				    // LeaveModel objLeave=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
			    	  List<LeaveModel> lstLeaves=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
					  LeaveModel objLeave =null;
					  
				  List<TimeSheetDataModel> lstDataFromTimeSheet=data.GetDataFromTimeSheet(selectedCompany.getCompKey(), item.getEmployeeKey(), _fromDate, _toDate); 
			      List<TimeSheetDataModel> lstDataFromOverTime=data.GetDataFromOverTime(item.getEmployeeKey(), _fromDate, _toDate);
			      lstSetup =data.GetDataFromSetup(selectedCompany.getCompKey(), item.getEmployeeKey(),_fromDate,_toDate);
			      if(lstDataFromTimeSheet.size()>0)
				  {
				    isOldDataFound=true;
				  }
			      			      
			    	  for (int i = 0; i < diffDay; i++) 
					  {
			    		  //begin add rows to grid day by day
			    		  isOldDateFound=false;
			    		  c.setTime(_fromDate);
						  c.add(Calendar.DAY_OF_MONTH, i);	
						//check EmployeementDate
							if(item.getEmployeementDate().after(c.getTime()))
									continue;
						
						   if(tempEmpKey!=item.getEmployeeKey())
							{									
								tempEmpKey=item.getEmployeeKey();						  						  						
								srNO++;									
							 }
						   	objLeave=checkIfEmployeeInLeave(lstLeaves, c.getTime());
						    if(isOldDataFound==false)
						    {
						    getRowDataFromSetup(lstSetup, item, c.get(Calendar.DAY_OF_WEEK), c.getTime(), employeeProject,employeeService,objLeave);
						    }
						   else
						   {
						     isOldDateFound= getRowDataFromOldData(lstDataFromTimeSheet,item, c.get(Calendar.DAY_OF_WEEK), c.getTime(), employeeProject,employeeService,objLeave,lstDataFromOverTime);
						     if(isOldDateFound==false)
						     {
						    	 //get the date from setup
						    	 getRowDataFromSetup(lstSetup, item, c.get(Calendar.DAY_OF_WEEK), c.getTime(), employeeProject,employeeService,objLeave);
						     }							    
						   }
						  							  
					  }
			      }
			      else
			      {
			         empNotInSetup+=item.getFullName() + " , ";
			      }
			    	
			   }
			 
			  //bind the Grid
			   tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
			     for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
				   {
					   tsGroupModel.removeOpenGroup(i);
				   }
			     
			     if(lstGrid.size()>0)
					   this.setCanExport(true);	
			     
			     footer=" Total Employees: " + tsGroupModel.getGroupCount();
			     
			     if(!empNotInSetup.equals(""))
			     {
			    	 Messagebox.show(empNotInSetup + "Not assigned to shift for scheduled period! \n To assign shift go to -> Timesheet ->Assign Employees To Shift","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			     } 
			   
		 }
		 catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> addTimesheetCommand", ex);			
			}	
	 }
	 
	  @Command
	  public void findEmployeeCommand()
	  {
		  try
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
		   
		   if(fromDate.after(toDate))
		   {
			   Messagebox.show("From date must be less than To Date !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			   return;
		   }
		   
		   //check if there is data for other company
		  if(lstGrid!=null)
		  {
		   for (TimeSheetGridModel item : lstGrid) 
		   {
			if(item.getCompKey()!=selectedCompany.getCompKey())
			{
				Messagebox.show("You can't add employee from another company before save the current time sheet !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}			
		   }
		  }
		   
		   if(monthFrom!=monthTo || yearFrom !=yearTo)
		   {
				Messagebox.show("Month of From Date & Month of To date should be Same !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
		   }
		   		   		  
		   CompSetupModel objCompany=data.getCompanySetup(selectedCompany.getCompKey());
		   INCLUDEHOLIDAY_UNIT=objCompany.getIncludeholidayUnit();
		   c.set(objCompany.getPayrollYear(),objCompany.getPayrollMonth(),1);
		   if(c.getTime().after(fromDate))
		   {
			   Messagebox.show("Company payroll date starts from " + objCompany.getPayrollMonth() + " / " + objCompany.getPayrollYear(),"Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			   return;
		   }
		   
		   if(compSettings.getDateType().equals("4"))//today and yesterday
		   {
			   if(!adminUser)
			   {
			   Calendar ctoday = Calendar.getInstance();	
			   Date today=new Date();
			   Date yesterday=new Date();
			   ctoday.setTime(today);
				  
			   if(fromDate.after(today) || toDate.after(today))
			   {
				   Messagebox.show("Only today and yesterday !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }
			   ctoday = Calendar.getInstance();		
			   ctoday.add(Calendar.DAY_OF_MONTH, -1);
			   //ctoday.setTime(yesterday);
			   
			   yesterday=df.parse(sdf.format(ctoday.getTime()));						
				
			   //logger.info("yest >> " + yesterday);
			   
			   if(fromDate.before(yesterday) || toDate.before(yesterday))
			   {
				   Messagebox.show("Only today and yesterday !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }		
			   }
		   }
		   
		   if(compSettings.getDateType().equals("5"))//today and prev week
		   {
			   if(!adminUser)
			   {
			   Calendar ctoday = Calendar.getInstance();	
			   Date today=new Date();
			   Date yesterday=new Date();
			   ctoday.setTime(today);
				  
			   if(fromDate.after(today) || toDate.after(today))
			   {
				   Messagebox.show("Only Today and Prev. week !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }
			   ctoday = Calendar.getInstance();		
			   ctoday.add(Calendar.DAY_OF_MONTH, -6);
			   //ctoday.setTime(yesterday);
			   
			   yesterday=df.parse(sdf.format(ctoday.getTime()));						
				
			   //logger.info("yest >> " + yesterday);
			   
			   if(fromDate.before(yesterday) || toDate.before(yesterday))
			   {
				   Messagebox.show("Only Today and Prev. week !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }
			   }
		   }
		   
		   if(compSettings.getDateType().equals("6"))//today and tomorrow
		   {
			   if(!adminUser)
			   {
			   Calendar ctoday = Calendar.getInstance();	
			   Date today=new Date();
			   Date nextday=new Date();
			   ctoday.setTime(today);
				
			   today=df.parse(sdf.format(ctoday.getTime()));	
			   
			   if(fromDate.before(today) || toDate.before(today))
			   {
				   Messagebox.show("Only Today and next day !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }
			   ctoday = Calendar.getInstance();		
			   ctoday.add(Calendar.DAY_OF_MONTH, 1);
			   //ctoday.setTime(yesterday);
			   
			   nextday=df.parse(sdf.format(ctoday.getTime()));						
				
			   //logger.info("yest >> " + yesterday);
			   
			   if(fromDate.after(nextday) || toDate.after(nextday))
			   {
				   Messagebox.show("Only Today and next day !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			   }	
			   }
		   }
		   
		  Map<String,Object> arg = new HashMap<String,Object>();
		  arg.put("compKey", selectedCompany.getCompKey());
		  arg.put("type", "T");
		  arg.put("viewType", viewType);
		  Executions.createComponents("/timesheet/searchemployee.zul", null,arg);
		 
		  }
		  
		  catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> findEmployeeCommand", ex);			
			}		  
	  }
	  
	  int srNO=0;
	  int rowIndex=0;
	  @GlobalCommand 
	  @NotifyChange({"tsGroupModel","timingFlag","canExport","footer","lstGrid"})
	  public void dlgClose(@BindingParam("myData")String empKeys,@BindingParam("viewType")String viewType)
	  {		
		  try
		  {
			  int dayFrom=0;			 	 
			  int dayTo=0;
			  int diffDay=0;
			  String empNotInSetup="";
			  String empInSalarySheet="";
			  String empInApprovedSheet="";
			  int tempEmpKey=0;
			  int monthTotalDays=0;
			  
			if(!empKeys.equals(""))
			{
				 Date _fromDate=fromDate;
				 Date  _toDate=toDate;
				 Calendar c = Calendar.getInstance();
				
				  if(compSettings.getDateType().equals("3"))//date or month
				  {
				  if(selectedDateType==0)//by Month
				   {
					  c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,1);
						 _fromDate=df.parse(sdf.format(c.getTime()));
						 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
						 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
						 _toDate=df.parse(sdf.format(c.getTime()));	
				   }				  
				 }
				 												  
				  c.setTime(_fromDate);
				  dayFrom=c.get(Calendar.DAY_OF_MONTH);				
				  c.setTime(_toDate);				
				  dayTo=c.get(Calendar.DAY_OF_MONTH);				  
				  diffDay=dayTo-dayFrom +1;
				  
				  monthTotalDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
				  
				  //add tmp dates in tmpTSDates
				  data.deletetmpTSDates();
				  data.addtmpTSDates(_fromDate, _toDate);
				
				  int  monthFrom=c.get(Calendar.MONTH);
				  int  yearFrom=c.get(Calendar.YEAR);
					
				  //check if Grid has data to get only the new employee
				  String tmpNewKeys="";	  
				  if(lstGrid==null)
				  {
				     lstGrid=new ArrayList<TimeSheetGridModel>();
				     tmpNewKeys=empKeys;
				  }
				  else
				  {
						
					List<Integer> lstOldEmpKeys=new ArrayList<Integer>();
					for (TimeSheetGridModel item : lstGrid) 
					{
						lstOldEmpKeys.add(item.getEmpKey());
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
				
				  
				  if(viewType.equals("edit") || viewType.equals("view"))
				  {
					  fillEditEmployee(tmpNewKeys, _fromDate, _toDate,viewType);
				  }
				  else
				  {  
				  //now we have the dates and employee keys...
				  //get the employee info from EmployeeDetails
				  List<EmployeeModel> lstEmp=data.GetEmployeeListFromFilter(tmpNewKeys);	
				  for (EmployeeModel item : lstEmp)
				   {
					  boolean isOldDataFound=false;
					  boolean isOldDateFound=false;
					  List<TimeSheetDataModel> lstSetup=null;
					  
					  //check if employee generate salary for this month
					  boolean isSalaryGenerate=false;
					  isSalaryGenerate=data.checkEmployeeSalarySheet(item.getEmployeeKey(), monthFrom+1, yearFrom);
					  if(isSalaryGenerate)
			    	  {
			    		  empInSalarySheet+=item.getFullName() + " , ";
			    		  continue;
			    	  }
					  int totalApprovedRecords=data.checkIfTimeSheetApproved(item.getEmployeeKey(), monthFrom+1, yearFrom);
					  if(totalApprovedRecords==monthTotalDays)
					  {
						  empInApprovedSheet+=item.getFullName() + " , ";
			    		  continue;
					  }
				      //get the shiftkey and TIMING_FLAG for employee from EMPSHIFT
				      CompanyShiftModel empShift=data.getEmployeeShiftKey(item.getEmployeeKey(), _fromDate, _toDate,selectedCompany.getCompKey());
				      if(empShift.getShiftKey()>0 && isSalaryGenerate==false)//if no employee shift no need to check
				      {
				    	  ProjectModel employeeProject=getProjectName(item.getLocationId());
				    	  HRListValuesModel employeeService=getServiceName(item.getPositionID());
				    	//check if has leave and approved
					     //LeaveModel objLeave=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
				    	  List<LeaveModel> lstLeaves=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
						  LeaveModel objLeave =null;
						  
					  List<TimeSheetDataModel> lstDataFromTimeSheet=data.GetDataFromTimeSheet(selectedCompany.getCompKey(), item.getEmployeeKey(), _fromDate, _toDate); 
				      List<TimeSheetDataModel> lstDataFromOverTime=data.GetDataFromOverTime(item.getEmployeeKey(), _fromDate, _toDate);
				      lstSetup =data.GetDataFromSetup(selectedCompany.getCompKey(), item.getEmployeeKey(),_fromDate,_toDate);
				      if(lstDataFromTimeSheet.size()>0)
					  {
					    isOldDataFound=true;
					  }	
				      
				      /*
				      if(lstDataFromTimeSheet.size()>0)
					 {
						 isOldDataFound=true;
					 }
					 else
					 {
				       lstSetup =data.GetDataFromSetup(selectedCompany.getCompKey(), item.getEmployeeKey(),_fromDate,_toDate);
					 }
				      */
				    	  for (int i = 0; i < diffDay; i++) 
						  {
				    		  //begin add rows to grid day by day
				    		  isOldDateFound=false;
				    		  c.setTime(_fromDate);
							  c.add(Calendar.DAY_OF_MONTH, i);	
							//check EmployeementDate
								if(item.getEmployeementDate().after(c.getTime()))
										continue;
							
							   if(tempEmpKey!=item.getEmployeeKey())
								{									
									tempEmpKey=item.getEmployeeKey();						  						  						
									srNO++;									
								 }
							    objLeave=checkIfEmployeeInLeave(lstLeaves, c.getTime());
							   							   
							    if(isOldDataFound==false)
							    {
							    getRowDataFromSetup(lstSetup, item, c.get(Calendar.DAY_OF_WEEK), c.getTime(), employeeProject,employeeService,objLeave);
							    }
							   else
							    {
							     isOldDateFound= getRowDataFromOldData(lstDataFromTimeSheet,item, c.get(Calendar.DAY_OF_WEEK), c.getTime(), employeeProject,employeeService,objLeave,lstDataFromOverTime);
							     if(isOldDateFound==false)
							     {
							    	 //get the date from setup
							    	 getRowDataFromSetup(lstSetup, item, c.get(Calendar.DAY_OF_WEEK), c.getTime(), employeeProject,employeeService,objLeave);
							     }							    
							   }
							  							  
						  }
				      }
				      else
				      {
				    	 
				         empNotInSetup+=item.getFullName() + " , ";
				      }
				    	 
				   }
				 }
				  //bind the Grid
				   tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
				     for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
					   {
						   tsGroupModel.removeOpenGroup(i);
					   }
				     
				     if(lstGrid.size()>0)
						   this.setCanExport(true);	
				     
				     footer=" Total Employees: " + tsGroupModel.getGroupCount();
				     
				     if(!empNotInSetup.equals(""))
				     {
				    	 Messagebox.show(empNotInSetup + " Not assigned to shift for scheduled period! \n To assign shift go to -> Timesheet ->Assign Employees To Shift","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				     }
				    
				     if(!empInSalarySheet.equals(""))
				     {
				    	 Messagebox.show(empInSalarySheet + " Salary Sheet is Already created for the month, Please recreate the salary sheet if the changes needs to be incorporated in the Salary Sheet !! ","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				     }
				     
				     if(!empInApprovedSheet.equals(""))
				     {
				    	 Messagebox.show(empInApprovedSheet + " TimeSheet for the month has been created and Approved. !! ","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				     }
					
			}
						
		  }
		  		  		 
		  catch (Exception ex)
	      {	
				logger.error("ERROR in DailyTimeSheetViewModel ----> dlgClose", ex);			
		  }
		  
	  }
	  
	  private void getRowDataFromSetup(List<TimeSheetDataModel> lstSetup,EmployeeModel emp,int noOfDay,Date tsDate, ProjectModel employeeProject, HRListValuesModel employeeService, LeaveModel objLeave)
	  {
		  List<TimeSheetDataModel> tmpSetupData= getDataFromSetup(lstSetup, emp.getEmployeeKey(),noOfDay,tsDate);
		  int tempRecNO=0;
		  for (TimeSheetDataModel item : tmpSetupData) 
		  {
			  TimeSheetGridModel obj=new TimeSheetGridModel();	
			  obj.setLstTaskStatus(lstTaskStatus);	
			  obj.setCompSettings(compSettings);
			  obj.setLstCustomerTaks(lstCustomerTaks);
			  obj.setMainShift(item.isMainShift());
			  obj.setRowFromSetup(true);
			 			  
			  obj.setNoOfshifts(item.getNoOfshifts());
			  obj.setEmpKey(emp.getEmployeeKey());
			  obj.setCompKey(emp.getCompanyID());					 
			  obj.setEmpNo(emp.getEmployeeNo());
			  obj.setEnFullName(emp.getFullName());
			  obj.setEmployeeStatus(emp.getStatus());
			  obj.setPosition(emp.getPosition());
			  obj.setEmployeementDate(emp.getEmployeementDate());
			  obj.setNotes("");
			  obj.setProject(employeeProject);
			  obj.setService(employeeService);
			  obj.setAttachPath("");
			  obj.setShiftRecNo(item.getShiftRecNo());
			  
			  obj.setTsDate(tsDate);
			  obj.setDayName(getDayName(tsDate));
			  obj.setShiftRecNo(item.getShiftRecNo());
			  obj.setTimingFlag(item.getTiming().equals("Y"));
			
			  obj.setCalculate("Yes");
			  obj.setStatus("Present");
			  obj.setHoliday(item.getOffDay().equals("Y"));	
			  obj.setLeaveFlag("");	
			  if(obj.isHoliday())
			  {
				obj.setStatus("Holiday");
				obj.setHolidayDesc("Offday");
				obj.setCantChange(true);
				if( INCLUDEHOLIDAY_UNIT.equals("N"))
				{
				obj.setUnits(0);
				obj.setTotals(0);
				}
				else
				{
				 obj.setUnits(item.getUnitNO());
			     obj.setTotals(getTotalHours(lstSetup,emp.getEmployeeKey(),noOfDay,obj.getTsDate()));					
				}			  			  
			  }
			  
			  else
			  {
				  obj.setUnits(item.getUnitNO());
				  obj.setTotals(getTotalHours(lstSetup,emp.getEmployeeKey(),noOfDay,obj.getTsDate()));
			  }
			  
			  obj.setNormalHours(item.getNormalUnitNO());
			  obj.setTotalNormalHours(obj.getTotals());
			  
			  obj.setUnitType(item.getTiming().equals("N")?"Days":"Hours");
			  obj.setShiftkey((int)item.getShiftKey());
			  obj.setUnitKey(item.getUnitId());
			
			  obj.setRecNo(item.getDayNo());
			  if(item.getTiming().equals("Y"))
	    	   {
				   this.timingFlag=true;
				   obj.setTimingFlag(true);
				   obj.setFromTime(item.getFromTime());
				   obj.setToTime(item.getToTime());
				   obj.setTsFromTime(item.getTsFromTime());
				   obj.setTstoTime(item.getTstoTime());
				   
				   if(tempRecNO!=item.getDayNo())
					{						
					   tempRecNO=item.getDayNo();
					   obj.setFirstOfRecord(true);							
					}
	    	   }
			  else
			  {
				  obj.setFirstOfRecord(true);	
			  }
			  
			  obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
			  obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
			  obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
			  
			  if(objLeave!=null && objLeave.getRecNO()>0)
			  {
				  if((objLeave.getLeaveStartDate().equals(obj.getTsDate()) || objLeave.getLeaveEndDate().equals(obj.getTsDate()))&& (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
					{
					  if(obj.isHoliday())
					  {
						obj.setStatus("Holiday");
						obj.setLeaveFlag("Y");
						obj.setNotes("On leave");
					  }
					 else
					 {
					  obj.setStatus("Leave");
					  obj.setLeaveFlag("");		
					  obj.setNotes(objLeave.getLeaveTypeDesc());
					 }
					obj.setCalculate("No");					
					obj.setCantChange(true);	
					obj.setUnits(0);
					obj.setTotals(0);
					//obj.setHoliday(true);
					obj.setProject(null);
					}
				   else if(objLeave.getLeaveStartDate().before(obj.getTsDate()) && objLeave.getLeaveEndDate().after(obj.getTsDate()) && (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
					{
					   if(obj.isHoliday())
						  {
							obj.setStatus("Holiday");
							obj.setLeaveFlag("Y");
							obj.setNotes("On leave");
						  }
						 else
						 {
						  obj.setStatus("Leave");
						  obj.setLeaveFlag("");		
						  obj.setNotes(objLeave.getLeaveTypeDesc());
						 }
					   				
					obj.setCalculate("No");					
					obj.setCantChange(true);
					obj.setUnits(0);
					obj.setTotals(0);
					//obj.setHoliday(true);
					obj.setProject(null);
					}
			  }
			  obj.setRealUnits(obj.getUnits());
			  obj.setSrNo(srNO);
			  
			  obj.setRowIndex(rowIndex);
			  rowIndex++;
			  lstGrid.add(obj);
			   
		  }		
	  }
	  	
	  private boolean getRowDataFromOldData( List<TimeSheetDataModel> lstDataFromTimeSheet,EmployeeModel emp,int noOfDay,Date tsDate, ProjectModel employeeProject, HRListValuesModel employeeService, LeaveModel objLeave, List<TimeSheetDataModel> lstDataFromOverTime)
	  {
		  SimpleDateFormat tmpsdf = new SimpleDateFormat("yyyy-MM-dd");
		  int tempRecNO=0;
		  int noOfShift=0;
		  boolean isOldDateFound=false;
		  for (TimeSheetDataModel oldData : lstDataFromTimeSheet) 
		   {
							  
			String tmpDate=tmpsdf.format(tsDate);					 			 
			if(tmpsdf.format(oldData.getTsDate()).equals(tmpDate))
			{
				TimeSheetGridModel obj=new TimeSheetGridModel();
				obj.setLstTaskStatus(lstTaskStatus);		
				obj.setCompSettings(compSettings);
				obj.setLstCustomerTaks(lstCustomerTaks);
				obj.setEmpKey(emp.getEmployeeKey());
				obj.setCompKey(emp.getCompanyID());					 
				obj.setEmpNo(emp.getEmployeeNo());
				obj.setEnFullName(emp.getFullName());
				obj.setEmployeeStatus(emp.getStatus());
				obj.setPosition(emp.getPosition());
				obj.setEmployeementDate(emp.getEmployeementDate());
				obj.setNotes("");
				//obj.setProject(employeeProject);
				//obj.setService(employeeService);
				obj.setLineNo(oldData.getLineNO());
				obj.setNoOfshifts(oldData.getNoOfshifts());
				obj.setShiftRecNo(oldData.getShiftRecNo());
				  
				  obj.setTsDate(tsDate);
				  obj.setDayName(getDayName(tsDate));				 
				  obj.setTimingFlag(oldData.getTiming().equals("Y"));
				  
				obj.setProject(getProjectName(oldData.getProjectkey()));						
				obj.setService(getServiceName(oldData.getServiceId()));	
				obj.setCustomerJob(getCustomerJob(oldData.getCustomerJobRefKey()));
				obj.setTomorrowPlan(oldData.getTomorrowPlan());
				obj.setAttachPath(oldData.getAttachPath());
				
			    obj.setApproved(oldData.getApproved());
				if(obj.getApproved()==1)
					 obj.setCantChange(true);
			    
			    String tsStatus="Present";
			   if(oldData.getStatus().equals("P"))
			   tsStatus="Present";
			   else if(oldData.getStatus().equals("H"))
			   {
			   tsStatus="Holiday";
			   obj.setCantChange(true);
			   }
			   else if(oldData.getStatus().equals("S"))
			   tsStatus="Sick";  
			   else if(oldData.getStatus().equals("L"))
			   {
			   obj.setCantChange(true);	   
			   tsStatus="Leave";
			   }
			   else if(oldData.getStatus().equals("A"))
			   tsStatus="Absence";
			   obj.setStatus(tsStatus);
			   obj.setNotes(oldData.getNotes());
			   
			   
			   obj.setHoliday(oldData.getOffDay().equals("Y"));
			   obj.setUnits(oldData.getUnitNO());
			   obj.setNormalHours(oldData.getNormalUnitNO());
			   obj.setTotals(oldData.getTotalUnitNo());
			   obj.setTotalNormalHours(oldData.getTotalNormalUnitNo());
			   
			   obj.setHolidayDesc(getHolidayDesc(oldData));
			  // obj.setHoliday(oldData.getStatus().equals("P") ? false :true);
			   obj.setCalculate(oldData.getCalcFlag().equals("Y")?"Yes" : "No");
			   obj.setUnitType(oldData.getUnitName());
			   obj.setShiftkey((int)oldData.getShiftKey());
			   obj.setUnitKey(oldData.getUnitId());
			   obj.setLeaveFlag(oldData.getLeaveFlag());
			   obj.setRecNo(oldData.getRecNo());
			   obj.setOldRecNo(oldData.getRecNo());
			   obj.setListOfattchments(data.getTimesheetAttchamnet(obj.getOldRecNo()));
			   if(oldData.getTiming().equals("Y"))
	    	   {
				   this.timingFlag=true;
				   obj.setTimingFlag(true);
				   obj.setFromTime(oldData.getFromTime());
				   obj.setToTime(oldData.getToTime());
				   obj.setTsFromTime(oldData.getTsFromTime());
				   obj.setTstoTime(oldData.getTstoTime());
				   
				   if(tempRecNO!=oldData.getRecNo())
					{						
					   tempRecNO=oldData.getRecNo();
					   obj.setFirstOfRecord(true);							
					}
	    	   }
			   
			   //check Over Time
			   List<TimeSheetDataModel> tmpOverTime=getDataFromOverTime(lstDataFromOverTime, oldData.getRecNo());
				  for (TimeSheetDataModel otItem : tmpOverTime)
				   {
					   if(otItem.getCalculation()==1.25 && otItem.getLineNO()==obj.getLineNo())
						   obj.setOtUnit1((int)otItem.getUnits());
					   if(otItem.getCalculation()==1.5)
						   obj.setOtUnit2((int)otItem.getUnits());
					   if(otItem.getCalculation()==2)
						   obj.setOtUnit3((int)otItem.getUnits());						   
				   }
				   obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());
				   obj.setTotalOTUnits((int)oldData.getTotalOTUnits());
				   
			   obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
			   obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
			   obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
			   
			 //check for leave
			   if(objLeave!=null && objLeave.getRecNO()>0)
				{							
				   if((objLeave.getLeaveStartDate().equals(obj.getTsDate()) || objLeave.getLeaveEndDate().equals(obj.getTsDate()))&& (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
					{
					   if(obj.isHoliday())
						  {
							obj.setStatus("Holiday");
							obj.setLeaveFlag("Y");
							obj.setNotes("On leave");
						  }
						 else
						 {
						  obj.setStatus("Leave");
						  obj.setLeaveFlag("");		
						  obj.setNotes(objLeave.getLeaveTypeDesc());
						 }
					
					obj.setCalculate("No");
					obj.setCantChange(true);	
					obj.setUnits(0);
					obj.setTotals(0);
					//obj.setHoliday(true);
					obj.setProject(null);
					}
					else if(objLeave.getLeaveStartDate().before(obj.getTsDate()) && objLeave.getLeaveEndDate().after(obj.getTsDate()) && (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
					{
						 if(obj.isHoliday())
						  {
							obj.setStatus("Holiday");
							obj.setLeaveFlag("Y");
							obj.setNotes("On leave");
						  }
						 else
						 {
						  obj.setStatus("Leave");
						  obj.setLeaveFlag("");		
						  obj.setNotes(objLeave.getLeaveTypeDesc());
						 }				
					obj.setCalculate("No");					
					obj.setCantChange(true);
					obj.setUnits(0);
					obj.setTotals(0);
					//obj.setHoliday(true);
					obj.setProject(null);
					}
					
				}
			   
			   
			   obj.setRealUnits(obj.getUnits());
			   obj.setSrNo(srNO);
			 
			   obj.setRowIndex(rowIndex);
			   rowIndex++;
			   lstGrid.add(obj);
			   isOldDateFound=true;
			   noOfShift++;
			}
			
		   }
		  
		  for (TimeSheetGridModel item : lstGrid) 
		  {
			if(item.getTsDate().equals(tsDate) && item.getEmpKey()==emp.getEmployeeKey())
			{
				item.setNoOfshifts(noOfShift);
			}
		  }
		  
		  return isOldDateFound;
	  }
	  
	  @SuppressWarnings("unused")
	private void fillEditEmployee(String tmpNewKeys,Date _fromDate,Date _toDate,String viewType)
	  {
		  
		  //get data only from exist timesheet
		  boolean tmpFirstRecNo;
		  int tempEmpKey=0;
		  int tempRecNO=0;
		  String empInSalarySheet="",empInApprovedSheet="";
		  Calendar c = Calendar.getInstance();
		  c.setTime(_fromDate);
		  int  monthFrom=c.get(Calendar.MONTH);
		  int  yearFrom=c.get(Calendar.YEAR);
		  int monthTotalDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		  
		  String[] tmpEditKeys= tmpNewKeys.split(",");						  
		  List<Integer> lstEditOldEmpKeys=new ArrayList<Integer>();
		  for (int i = 0; i < tmpEditKeys.length; i++) 
		   {	
			  if(!tmpEditKeys[i].equals(""))
			  lstEditOldEmpKeys.add(Integer.parseInt(tmpEditKeys[i]));
		   }
		  
		  if(lstEditOldEmpKeys.size()>0)
		  {
			  List<EmployeeModel> lstEmp= data.GetEmployeeListInDailyTS(_fromDate, _toDate, selectedCompany.getCompKey(),supervisorID);
			  for (EmployeeModel item : lstEmp)
			   {
				  if(lstEditOldEmpKeys.contains(item.getEmployeeKey()))
				  {
					  if(viewType.equals("edit"))
					  {
					  //check if employee generate salary for this month
					  boolean isSalaryGenerate=false;
					  isSalaryGenerate=data.checkEmployeeSalarySheet(item.getEmployeeKey(), monthFrom+1, yearFrom);
					  if(isSalaryGenerate)
			    	  {
			    		  empInSalarySheet+=item.getFullName() + " , ";
			    		  continue;
			    	  }
					  int totalApprovedRecords=data.checkIfTimeSheetApproved(item.getEmployeeKey(), monthFrom+1, yearFrom);
					  if(totalApprovedRecords==monthTotalDays)
					  {
						  empInApprovedSheet+=item.getFullName() + " , ";
			    		  continue;
					  }
					 }
					  
					   //ProjectModel projectName=getProjectName(item.getLocationId());
					   //HRListValuesModel serviceName=getServiceName(item.getPositionID());
					   List<TimeSheetDataModel> lstDataFromTimeSheet=data.GetDataFromTimeSheet(selectedCompany.getCompKey(), item.getEmployeeKey(), _fromDate, _toDate); 
					   List<TimeSheetDataModel> lstDataFromOverTime=data.GetDataFromOverTime(item.getEmployeeKey(), _fromDate, _toDate);
					   //check if has leave and approved
					   //LeaveModel objLeave=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
					   List<LeaveModel> lstLeaves=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
					   LeaveModel objLeave =null;
						  
					   for (TimeSheetDataModel tsTimeSheet : lstDataFromTimeSheet) 
					   {
						   TimeSheetGridModel obj=new TimeSheetGridModel();
						   tmpFirstRecNo=false;
						   if(tempEmpKey!=tsTimeSheet.getEmpKey())
						   {
							   srNO++;
							   totalEmployeeNumber++;
							  tempEmpKey=tsTimeSheet.getEmpKey();
						   }
						   obj.setRecNo(tsTimeSheet.getRecNo());
						   obj.setOldRecNo(tsTimeSheet.getRecNo());
						   obj.setListOfattchments(data.getTimesheetAttchamnet(obj.getOldRecNo()));
						   obj.setSrNo(srNO);
						   obj.setEmpKey(tsTimeSheet.getEmpKey());
						   obj.setCompKey(selectedCompany.getCompKey());					 
						   obj.setEmpNo(item.getEmployeeNo());
						   obj.setEnFullName(item.getFullName());
						   obj.setEmployeeStatus(item.getStatus());
						   obj.setPosition(item.getPosition());
						   obj.setEmployeementDate(item.getEmployeementDate());
						   
						   obj.setLineNo(tsTimeSheet.getLineNO());
						   obj.setNoOfshifts(getNoOfShift(lstDataFromTimeSheet, tsTimeSheet.getTsDate(), obj.getEmpKey()));//tsTimeSheet.getNoOfshifts());
						   obj.setShiftRecNo(tsTimeSheet.getShiftRecNo());
						   obj.setTsDate(tsTimeSheet.getTsDate());
						   obj.setDayName(getDayName(tsTimeSheet.getTsDate()));					 
						   obj.setTimingFlag(tsTimeSheet.getTiming().equals("Y"));
						   obj.setProject(getProjectName(tsTimeSheet.getProjectkey()));						
						   obj.setService(getServiceName(tsTimeSheet.getServiceId()));
						   obj.setCustomerJob(getCustomerJob(tsTimeSheet.getCustomerJobRefKey()));
						   obj.setTomorrowPlan(tsTimeSheet.getTomorrowPlan());
						   obj.setApproved(tsTimeSheet.getApproved());
						 
						   String tsStatus="Present";
						   if(tsTimeSheet.getStatus().equals("P"))
							   tsStatus="Present";
							   else if(tsTimeSheet.getStatus().equals("H"))
							   {
							   tsStatus="Holiday";
							   obj.setCantChange(true);
							   }
							   else if(tsTimeSheet.getStatus().equals("S"))
							   tsStatus="Sick";  
							   else if(tsTimeSheet.getStatus().equals("L"))
							   {
							   tsStatus="Leave";
							   obj.setCantChange(true);	
							   }
							   else if(tsTimeSheet.getStatus().equals("A"))
							   tsStatus="Absence";
							   obj.setStatus(tsStatus);
							   obj.setNotes(tsTimeSheet.getNotes());
							   
							   obj.setHoliday(tsTimeSheet.getOffDay().equals("Y"));
							   obj.setUnits(tsTimeSheet.getUnitNO());
							   obj.setNormalHours(tsTimeSheet.getNormalUnitNO());
							   obj.setTotals(tsTimeSheet.getTotalUnitNo());
							   obj.setTotalNormalHours(tsTimeSheet.getTotalNormalUnitNo());
							   
							   obj.setHolidayDesc(getHolidayDesc(tsTimeSheet));
							   //obj.setHoliday(tsTimeSheet.getStatus().equals("P") ? false :true);
							   obj.setCalculate(tsTimeSheet.getCalcFlag().equals("Y")?"Yes" : "No");
							   obj.setUnitType(tsTimeSheet.getUnitName());
							   obj.setShiftkey((int)tsTimeSheet.getShiftKey());
							   obj.setUnitKey(tsTimeSheet.getUnitId());
							   
							   if(tsTimeSheet.getTiming().equals("Y"))
					    	   {
								   this.timingFlag=true;
								   obj.setTimingFlag(true);
								   obj.setFromTime(tsTimeSheet.getFromTime());
								   obj.setToTime(tsTimeSheet.getToTime());
								   obj.setTsFromTime(tsTimeSheet.getTsFromTime());
								   obj.setTstoTime(tsTimeSheet.getTstoTime());
								   
								   if(tempRecNO!=tsTimeSheet.getRecNo())
									{						
									   tempRecNO=tsTimeSheet.getRecNo();
									   obj.setFirstOfRecord(true);							
									}
					    	   }
							   else
							   {
								   obj.setFirstOfRecord(true);			
							   }
							   
							 //check Over Time
							   List<TimeSheetDataModel> tmpOverTime=getDataFromOverTime(lstDataFromOverTime, tsTimeSheet.getRecNo());
								  for (TimeSheetDataModel otItem : tmpOverTime)
								   {
									   if(otItem.getCalculation()==1.25 && otItem.getLineNO()==obj.getLineNo())
										   obj.setOtUnit1((int)otItem.getUnits());
									   if(otItem.getCalculation()==1.5)
										   obj.setOtUnit2((int)otItem.getUnits());
									   if(otItem.getCalculation()==2)
										   obj.setOtUnit3((int)otItem.getUnits());						   
								   }
								  // obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());
								   obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());
								   obj.setTotalOTUnits((int)tsTimeSheet.getTotalOTUnits());
								   
							   obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
							   obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
							   obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
							   
							   //check for leave
							   objLeave=checkIfEmployeeInLeave(lstLeaves, obj.getTsDate());
							   boolean isLeaveFound=false;
							   if(objLeave!=null && objLeave.getRecNO()>0)
								{		
								   if((objLeave.getLeaveStartDate().equals(obj.getTsDate()) || objLeave.getLeaveEndDate().equals(obj.getTsDate())))
									{
									   isLeaveFound=true;
									}
								   else if(objLeave.getLeaveStartDate().before(obj.getTsDate()) && objLeave.getLeaveEndDate().after(obj.getTsDate()))
									{
									   isLeaveFound=true;
									}
								   
								   if(isLeaveFound)
								   {
									   if(obj.isHoliday())
										  {
											obj.setStatus("Holiday");
											obj.setLeaveFlag("Y");
											obj.setNotes("On leave");
										  }
										 else
										 {
										  obj.setStatus("Leave");
										  obj.setLeaveFlag("");		
										  obj.setNotes(objLeave.getLeaveTypeDesc());
										 }
									   obj.setCalculate("No");									
									   obj.setCantChange(true);	
									   obj.setUnits(0);
									   obj.setTotals(0);
									  //obj.setHoliday(true);
									  obj.setProject(null);									  
								   }
								   
								}
							   
							   if(obj.getEmpKey()>0)
							   {
								rowIndex++;
								obj.setRowIndex(rowIndex);
							    lstGrid.add(obj);					
							   }
						   
					   }
					   
				  }
				  
			   }
			  
		  }
		  
		  	if(!empInSalarySheet.equals(""))
		     {
		    	 Messagebox.show(empInSalarySheet + " Salary Sheet is Already created for the month, Please recreate the salary sheet if the changes needs to be incorporated in the Salary Sheet !! ","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
		     }
		     
		     if(!empInApprovedSheet.equals(""))
		     {
		    	 Messagebox.show(empInApprovedSheet + " TimeSheet for the month has been created and Approved. !! ","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
		     }
		  
	  }
	  private int getNoOfShift( List<TimeSheetDataModel> lstDataFromTimeSheet,Date tsDate,int empKey)
	  {
		  int noOfShift=0;
		  for (TimeSheetDataModel row : lstDataFromTimeSheet) 
		  {
			  if(row.getTsDate().equals(tsDate) && row.getEmpKey()==empKey)
			  {
				  noOfShift++;
			  }
		  }
		  return noOfShift;
	  }
	  @SuppressWarnings("unused")
	@GlobalCommand 
	  @NotifyChange({"tsGroupModel","timingFlag","canExport","footer"})
	  public void dlgClose1(@BindingParam("myData")String empKeys)
	  {		
		  try
		  {
			if(!empKeys.equals(""))
			{
				  int dayFrom=0;
				  int monthFrom=0;				
				  int yearFrom=0;				  
				  int dayTo=0;
				  int diffDay=0;
				  
				  Date tmpSelectDate;
				  int tmpSelectEMP=0;
				  
				  String empNotInSetup="";
				  //lstHourlyGrid=new ListModelList<TimeSheetGridModel>();
				  this.setTimingFlag(false);
				  this.setCanExport(false);
				  
				  Date _fromDate;
				  Date  _toDate;
				  Calendar c = Calendar.getInstance();
				  //asume as default
				  _fromDate=fromDate;
				  _toDate=toDate;
				  
				  if(compSettings.getDateType().equals("3"))//date and month
				  {
				  if(selectedDateType==1)//by dates
				   {
					  _fromDate=fromDate;
					  _toDate=toDate;
				   }
				  else
				  {					 
					  c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,1);
					 _fromDate=df.parse(sdf.format(c.getTime()));
					 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
					 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
					 _toDate=df.parse(sdf.format(c.getTime()));					 					
				  }
				 }
				  
				  else if(compSettings.getDateType().equals("1"))
				  {
					  _fromDate=fromDate;
					  _toDate=toDate;
				  }
				  
				 // Calendar c = Calendar.getInstance();	
				  c.setTime(_fromDate);
				  dayFrom=c.get(Calendar.DAY_OF_MONTH);
				  monthFrom=c.get(Calendar.MONTH);
				  yearFrom=c.get(Calendar.YEAR);
				  c.setTime(_toDate);				
				  dayTo=c.get(Calendar.DAY_OF_MONTH);				  
				  diffDay=dayTo-dayFrom +1;
				  
				  
				  //List<SalaryMasterModel> lstSalaryMaster=data.GetMasterSalarySheet(monthFrom+1, yearFrom);
				 
				  // List<ProjectModel> lstProject=data.getProjectList(selectedCompany.getCompKey());
				  //List<HRListValuesModel> lstPositions=data.getHRListValues(47,"");
			
		    String tmpNewKeys="";	  
			if(lstGrid==null)
			{
		     lstGrid=new ArrayList<TimeSheetGridModel>();
		     tmpNewKeys=empKeys;
			}
			else
			{
				
				List<Integer> lstOldEmpKeys=new ArrayList<Integer>();
				for (TimeSheetGridModel item : lstGrid) 
				{
					lstOldEmpKeys.add(item.getEmpKey());
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
				  
		     List<EmployeeModel> lstEmp=data.GetEmployeeListFromFilter(tmpNewKeys);
		    
		     int tempEmpKey=0;
		     for (EmployeeModel item : lstEmp)
			 {
		    	 
		    	 //get the shiftkey and TIMING_FLAG for employee
		    	 CompanyShiftModel empShift=data.getEmployeeShiftKey(item.getEmployeeKey(), _fromDate, _toDate,selectedCompany.getCompKey());
		    	 if(empShift.getShiftKey()>0)//if no employee shift no need to check
		    	 {
		    		if(empShift.getTimingFlag().equals("Y"))
		    		{
		    			this.timingFlag=true;
		    		}
		    	 // String salaryStatus=getSalaryStatus(lstSalaryMaster, item.getEmployeeKey());
		    	  ProjectModel projectName=getProjectName(item.getLocationId());
		    	  HRListValuesModel serviceName=getServiceName(item.getPositionID());
		    	  
		    	  //get Hours and offday from COMPSHIFT
		    	  List<CompanyShiftModel> lstCompanyShift=new ArrayList<CompanyShiftModel>();		    	  
				  lstCompanyShift=data.getCompanyShift(selectedCompany.getCompKey(), empShift.getShiftKey());
		    	  		    	  
				  //used to fill saved data
				  List<TimeSheetDataModel> lstDataFromTimeSheet=data.GetDataFromTimeSheet(selectedCompany.getCompKey(), item.getEmployeeKey(), _fromDate, _toDate); 
				  List<TimeSheetDataModel> lstDataFromOverTime=data.GetDataFromOverTime(item.getEmployeeKey(), _fromDate, _toDate);
				   
				  //no need to check from setup its enough to check empShift 
				  List<TimeSheetDataModel> lstSetup =data.GetDataFromSetup(selectedCompany.getCompKey(), item.getEmployeeKey(),_fromDate, _toDate);
				  //TimeSheetDataModel tmpSetupData= getDataFromSetup(lstSetup, item.getEmployeeKey());
				   
				   //c.setTime(fromDate);
				  	
				  //check if has leave and approved
				  //LeaveModel objLeave=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
				  List<LeaveModel> lstLeaves=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
				  LeaveModel objLeave =null;
				  
				  int tempRecNO=0;
				  boolean tmpFirstRecNo;
				  
				   for (int i = 0; i < diffDay; i++) 
				   {
				   TimeSheetGridModel obj=new TimeSheetGridModel();
				   c.setTime(_fromDate);
				   c.add(Calendar.DAY_OF_MONTH, i);
				   obj.setTsDate(c.getTime());
				   obj.setDayName(getDayName(c.getTime()));
				   					
					//check EmployeementDate
					if(item.getEmployeementDate().after(obj.getTsDate()))
							continue;
					
					   obj.setEmpKey(item.getEmployeeKey());
					   obj.setCompKey(item.getCompanyID());					 
					   obj.setEmpNo(item.getEmployeeNo());
					   obj.setEnFullName(item.getFullName());
					   obj.setEmployeeStatus(item.getStatus());
					   obj.setPosition(item.getPosition());
					   obj.setEmployeementDate(item.getEmployeementDate());
					   obj.setNotes("111");
					   if(empShift.getTimingFlag().equals("Y"))
			    	    {
						   obj.setTimingFlag(true);
			    		}
				   //check if there is data in this date
				   boolean isOldDataFound=false;
				   SimpleDateFormat tmpsdf = new SimpleDateFormat("yyyy-MM-dd");
				   for (TimeSheetDataModel oldData : lstDataFromTimeSheet) 
				   {
									  
					String tmpDate=tmpsdf.format(c.getTime());					 
					  
					if(tmpsdf.format(oldData.getTsDate()).equals(tmpDate) && isOldDataFound==false)
					{						
						isOldDataFound=true;
						obj.setProject(getProjectName(oldData.getProjectkey()));						
						obj.setService(getServiceName(oldData.getServiceId()));		
						
						 	String tsStatus="Present";
						   if(oldData.getStatus().equals("P"))
						   tsStatus="Present";
						   else if(oldData.getStatus().equals("H"))
						   {
						   tsStatus="Holiday";
						   obj.setCantChange(true);
						   }
						   else if(oldData.getStatus().equals("S"))
						   tsStatus="Sick";  
						   else if(oldData.getStatus().equals("L"))
						   tsStatus="Leave";  
						   else if(oldData.getStatus().equals("A"))
						   tsStatus="Absence";
						   obj.setStatus(tsStatus);
						   obj.setNotes(oldData.getNotes());
						 
						   obj.setHoliday(oldData.getOffDay().equals("Y"));
						   obj.setUnits((int)oldData.getUnitNO());
						   obj.setTotals((int)oldData.getTotalUnitNo());
						   
						   obj.setHolidayDesc(getHolidayDesc(oldData));
						   obj.setHoliday(oldData.getStatus().equals("P") ? false :true);
						   obj.setCalculate(oldData.getCalcFlag().equals("Y")?"Yes" : "No");
						   obj.setUnitType(oldData.getUnitName());
						   obj.setShiftkey((int)oldData.getShiftKey());
						   obj.setUnitKey(oldData.getUnitId());
						   
						   obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
						   obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
						   obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
							
						   //check for leave
						   objLeave=checkIfEmployeeInLeave(lstLeaves, obj.getTsDate());
						   if(objLeave!=null && objLeave.getRecNO()>0)
							{							
							   if((objLeave.getLeaveStartDate().equals(obj.getTsDate()) || objLeave.getLeaveEndDate().equals(obj.getTsDate()))&& (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
								{
								obj.setStatus("Leave");
								obj.setNotes(objLeave.getLeaveTypeDesc());
								obj.setCantChange(true);	
								obj.setUnits(0);
								obj.setTotals(0);
								obj.setHoliday(true);
								obj.setProject(null);
								}
								else if(objLeave.getLeaveStartDate().before(obj.getTsDate()) && objLeave.getLeaveEndDate().after(obj.getTsDate()) && (objLeave.getPayment().equalsIgnoreCase("P") || objLeave.getPayment().equalsIgnoreCase("N") || objLeave.getPayment().equalsIgnoreCase("W")) && !objLeave.getEnCashStatus().equalsIgnoreCase("Y"))
								{
								obj.setStatus("Leave");
								obj.setNotes(objLeave.getLeaveTypeDesc());
								obj.setCantChange(true);
								obj.setUnits(0);
								obj.setTotals(0);
								obj.setHoliday(true);
								obj.setProject(null);
								}
								else
								{
									//check for Absense
								  if(oldData.getStatus().equals("A"))	
								  {
								    obj.setStatus("Absence");
								    obj.setCantChange(false);
								  }
								  else if(oldData.getStatus().equals("H"))
								   {
								    obj.setStatus("Holiday");
								    obj.setCantChange(true);
								    obj.setHoliday(true);
								   }
								  else
								  {
									obj.setStatus("Present");
									obj.setUnits(1);
								    obj.setTotals(1);
								    obj.setCalculate("Yes");
								    obj.setHoliday(false);
								    obj.setCantChange(false);
								  }
								
								obj.setNotes("");
								}
							}
						   
						  
						   
						  //check Over Time
						  List<TimeSheetDataModel> tmpOverTime=getDataFromOverTime(lstDataFromOverTime, oldData.getRecNo());
						  for (TimeSheetDataModel otItem : tmpOverTime)
						   {
							   if(otItem.getCalculation()==1.25)
								   obj.setOtUnit1((int)otItem.getUnits());
							   if(otItem.getCalculation()==1.5)
								   obj.setOtUnit2((int)otItem.getUnits());
							   if(otItem.getCalculation()==2)
								   obj.setOtUnit3((int)otItem.getUnits());						   
						   }
						   obj.setTotalOT(obj.getOtUnit1() + obj.getOtUnit2() + obj.getOtUnit3());						    
					}
					
				   }				   				  				 
					   
					if(isOldDataFound==false)
					{
					   obj.setProject(projectName); //.setProjectName(projectName);
					   obj.setService(serviceName);
					  		
					//check data from setup					   
					List<TimeSheetDataModel> tmpSetupData= getDataFromSetup(lstSetup, item.getEmployeeKey(),c.get(Calendar.DAY_OF_WEEK),obj.getTsDate());
					if(obj.isTimingFlag())
					{
						//logger.info(">>> " + tmpSetupData.size());
						double totalHours=0;
						for (TimeSheetDataModel tmpSetup : tmpSetupData) 
						{
							obj.setShiftRecNo(tmpSetup.getRecNo());
							obj.setTimingFlag(tmpSetup.getTiming().equals("Y"));
							obj.setFromTime(tmpSetup.getFromTime());
							obj.setToTime(tmpSetup.getToTime());
							obj.setUnits((int)tmpSetup.getUnitNO());
							obj.setTotals(getTotalHours(lstSetup,item.getEmployeeKey(),c.get(Calendar.DAY_OF_WEEK),obj.getTsDate()));
							//obj.setTotals((int)tmpSetup.getUnitNO());
							obj.setCalculate("Yes");
							obj.setStatus("Present");	
							obj.setHoliday(tmpSetup.getOffDay().equals("Y"));
							if(obj.isHoliday())
							{
							obj.setStatus("Holiday");
							obj.setHolidayDesc("Offday");
							obj.setCantChange(true);
							}
							
							obj.setUnitType(tmpSetup.getTiming().equals("N")?"Days":"Hours");
							obj.setShiftkey((int)tmpSetup.getShiftKey());
							obj.setUnitKey(tmpSetup.getUnitId());
							
							if(tempRecNO!=tmpSetup.getDayNo())
							 {
								tmpFirstRecNo=true;
								tempRecNO=tmpSetup.getDayNo();
								obj.setFirstOfRecord(true);	
								totalHours=obj.getTotals();
							 }							
							
							obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
							obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
							obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
							
							lstGrid.add(obj);
							obj=new TimeSheetGridModel();
							obj.setTsDate(c.getTime());
							obj.setDayName(getDayName(c.getTime()));
							obj.setEmpKey(item.getEmployeeKey());
							obj.setCompKey(item.getCompanyID());					 
							obj.setEmpNo(item.getEmployeeNo());
						    obj.setEnFullName(item.getFullName());
						    obj.setEmployeeStatus(item.getStatus());
						    obj.setPosition(item.getPosition());
						    obj.setEmployeementDate(item.getEmployeementDate());
						    obj.setProject(projectName);
							obj.setService(serviceName);
							obj.setTimingFlag(true);
							obj.setNotes("222");
						}
					}
					
					
					/*   
				   	CompanyShiftModel tmpShift=checkIsHolidayShift(lstCompanyShift, c.get(Calendar.DAY_OF_WEEK));
					if(tmpShift!=null)
					{
						obj.setShiftRecNo(tmpShift.getRecNo());
						//obj.setHoliday(tmpShift.getOffDay().equals("Y"));
						obj.setUnits((int)tmpShift.getHours());
						obj.setTotals((int)tmpShift.getHours());
						obj.setCalculate("Yes");
						obj.setStatus("Present");	
						
						obj.setHoliday(tmpShift.getOffDay().equals("Y"));
						if(obj.isHoliday())
						{
						obj.setStatus("Holiday");
						obj.setHolidayDesc("Offday");
						obj.setCantChange(true);
						}
						
						//check if has leave and approved
						// LeaveModel objLeave=data.checkIfOnlineLeaveTaken(item.getEmployeeKey(), _fromDate, _toDate);
						if(objLeave!=null && objLeave.getRecNO()>0)
						{							
							if(objLeave.getLeaveStartDate().equals(obj.getTsDate()) || objLeave.getLeaveEndDate().equals(obj.getTsDate()))
							{
							obj.setStatus("Leave");
							obj.setNotes(objLeave.getLeaveTypeDesc());
							obj.setCantChange(true);	
							obj.setUnits(0);
							obj.setTotals(0);
							obj.setHoliday(true);
							obj.setProject(null);
							}
							else if(objLeave.getLeaveStartDate().before(obj.getTsDate()) && objLeave.getLeaveEndDate().after(obj.getTsDate()))
							{
							obj.setStatus("Leave");
							obj.setNotes(objLeave.getLeaveTypeDesc());
							obj.setCantChange(true);
							obj.setUnits(0);
							obj.setTotals(0);
							obj.setHoliday(true);
							obj.setProject(null);
						   }							
						}
																																
						obj.setUnitType(empShift.getTimingFlag().equals("N")?"Days":"Hours");
						obj.setShiftkey(tmpShift.getShiftKey());
						obj.setUnitKey(empShift.getUnitkey());
						
						obj.setOtUnit1Enable(isOTDisable(1.25, obj.getShiftkey(), obj.isHoliday()));
						obj.setOtUnit2Enable(isOTDisable(1.5, obj.getShiftkey(), obj.isHoliday()));
						obj.setOtUnit3Enable(isOTDisable(2, obj.getShiftkey(), obj.isHoliday()));
					}
					*/
				}
					  
				   /*
					 if(tempEmpKey!=item.getEmployeeKey())
					  {
						 // srNO++;
						tempEmpKey=item.getEmployeeKey();						  						  						
						srNO++;
						totalEmployeeNumber++;
					  }
					 
					 	obj.setSrNo(srNO);					    
					    lstGrid.add(obj);
				   
				   */
				   }
				   				   			  														 																									 				   
			 }
	    	 else
	    	 {
	    		 empNotInSetup+=item.getFullName() + " , ";
	    	 }
		    	 
		     	     		    		   
			 }//end for		     
		     
		     tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
		     for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			   {
				   tsGroupModel.removeOpenGroup(i);
			   }
		     
		     if(lstGrid.size()>0)
				   this.setCanExport(true);	
		     
		     footer=" Total Employees: " + tsGroupModel.getGroupCount();
		     
		     if(!empNotInSetup.equals(""))
		     {
		    	 Messagebox.show(empNotInSetup + " Not assigned to shift for scheduled period! \n To assign shift go to -> Timesheet ->Assign Employees To Shift","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
		     }
			}
		  }
		  catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> dlgClose", ex);			
			}
	  }
	  
	  @Command
	  @NotifyChange({"tsGroupModel"})
	  public void noprojectCommand()
	  {
		  if(lstGrid==null)
		  {
    		Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
    		return;
		  }
		  List<TimeSheetGridModel> lst=new ArrayList<TimeSheetGridModel>();
		  for (TimeSheetGridModel item : lstGrid) 
		  {
			if(item.getProject()==null)
			{				
				lst.add(item);
			}
		  }
		  
		  tsGroupModel=new TimeSheetGroupAdapter(lst, new TimeSheetComparator(), true);		  
	  }
	  
	  @Command
	  @NotifyChange({"tsGroupModel"})
	  public void showAllCommand()
	  {
		  if(lstGrid==null)
		  {
    		Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
    		return;
		  }
		  tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);	
	  }
	  
	  @Command
	  @NotifyChange({"tsGroupModel"})
	  public void changeFilter() 
	  {	      
		  if(lstGrid==null)
		  {
    		Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
    		return;
		  }
		  
		  List<TimeSheetGridModel> lst=new ArrayList<TimeSheetGridModel>();
		  for (Iterator<TimeSheetGridModel> i = lstGrid.iterator(); i.hasNext();)
			{
			  TimeSheetGridModel tmp=i.next();	
			  if(tmp.getEmpNo().toLowerCase().contains(employeeFilter.getEmployeeNo()) &&
			   tmp.getEnFullName().toLowerCase().startsWith(employeeFilter.getFullName().toLowerCase())	  
			   )
			  {
				  lst.add(tmp);
			  }
				  
			}
		  
		    tsGroupModel=new TimeSheetGroupAdapter(lst, new TimeSheetComparator(), true);
		    for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			  {				
				   tsGroupModel.removeOpenGroup(i);				
			  }
		  
	  }
	  
	  
	  @Command
	  public void searchforEmployeeCommand()
	  {
		  try
		  {
			  if(lstGrid==null)
			  {
	    		//Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	    		return;
			  }
			  
			  for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			  {				
				   tsGroupModel.removeOpenGroup(i);				
			  }
			  
			  boolean isEmpFound=false;
			  for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			   {
					 String[] header=new String[7];
					 header= tsGroupModel.getGroup(i);
					 if(header[0].equals(searchText) || header[1].toLowerCase().startsWith(searchText.toLowerCase()))
					 {
						 tsGroupModel.addOpenGroup(i);
						 isEmpFound=true;
						 break;
					 }					  
				}
			  
			 
				if(isEmpFound==false)
				{
					Messagebox.show("There is no Employee match in this time sheet","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);														
				}				
			  
			  
		  }
		  catch (Exception ex)
		  {	
				logger.error("ERROR in DailyTimeSheetViewModel ----> searchforEmployeeCommand", ex);			
		  }
	  }
	  
	  @Command
	  @NotifyChange({"tsGroupModel","timingFlag","canExport","footer"})
	  public void removeCommand(@BindingParam("emp") String empNo)
	  {
		  try
		  {
			  //Messagebox.show(emp + "");
			 List<TimeSheetGridModel>  lstRemove=new ArrayList<TimeSheetGridModel>();
			  for (TimeSheetGridModel item : lstGrid) 
			  {
				if(item.getEmpNo().equals(empNo))
				{	
					//tsGroupModel.removeFromSelection(item);
					//lstGrid.remove(item);
					lstRemove.add(item);
				}
			  }
			  
			  lstGrid.removeAll(lstRemove);
			  tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
			  for (int i = 0; i < tsGroupModel.getGroupCount(); i++)
			   {
				   tsGroupModel.removeOpenGroup(i);
			   }
			  footer=" Total Employees: " + tsGroupModel.getGroupCount();
		  }
		  catch (Exception ex)
		  {	
				logger.error("ERROR in DailyTimeSheetViewModel ----> removeCommand", ex);			
		  }
	  }
	  
	  @Command
	  @NotifyChange({"tsGroupModel","timingFlag","canExport","footer","timingFlag"})
	  public void clearCommand()
	  {
		  srNO=0;
		  rowIndex=0;
		  this.timingFlag=false;
		  lstGrid=new ArrayList<TimeSheetGridModel>();
		  tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
		  footer=" Total Employees: " + tsGroupModel.getGroupCount();
	  }
	  
	  private boolean isValidDataToSave()
	  {
		 boolean isValid=true;
		 try
		 {
			 for (TimeSheetGridModel item : lstGrid) 
			  {
				 if(item.getProject()==null && item.isCantChange()==false)
					{
						  Messagebox.show("Please set the project for all records !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
						  return false;
					}
			  }
			 
			 //check Tomorrow Plan
			 int tempEmpKey=0;
			 Date tmpRecDate=null;
			 if(compSettings.isHideTomorrowPlan()==false && compSettings.isMandatoryTomorrowPlan())
			 {
				 for (TimeSheetGridModel item : lstGrid) 
				  {
					 if(tempEmpKey!=item.getEmpKey())
					 {
						 tempEmpKey=item.getEmpKey();
						 tmpRecDate=null;
					 }
					 if(tmpRecDate==null || !item.getTsDate().equals(tmpRecDate))
					 {
						 tmpRecDate=item.getTsDate();
					 
						 
					 	String tomorrowPlan=item.getTomorrowPlan()==null?"":item.getTomorrowPlan();
					 	if(item.isCantChange()==false && tomorrowPlan.trim().equals(""))
						{
						  Messagebox.show("Tomorrow Plan is mandatory !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
						  return false;
						}
					 }	
				  }
				 
			 }
			 
			 //check Customer Job
			 if(compSettings.isHideCustomerJob()==false && compSettings.isMandatoryCustomerJob())
			 {
				 for (TimeSheetGridModel item : lstGrid) 
				  {
					 if(item.isCantChange()==false)
					 {
						 if(item.getCustomerJob()==null || item.getCustomerJob().getRecNo()==0)
						 {
							  Messagebox.show("Customer Job is mandatory !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
							  return false; 
						 }
					 }					 				 
				  }
			 }
			 
			 //check Customer Task
			if(!viewType.equals("edit"))
			{
			 if(compSettings.isHideCustomerTask()==false && compSettings.isMandatoryCustomerTask())
			 {
				 for (TimeSheetGridModel item : lstGrid) 
				  {
					 if(item.isCantChange()==false)
					 {
						 if(item.getLstCustomerTaks().size()>0)
						 {							
							 if(item.getSelectedTask()==null || item.getSelectedTask().getTaskid()==0)
							 {
							  Messagebox.show("Customer Task is mandatory !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
							  return false;
							 }
						 }
					 }					 				 
				  }
			 }
			}
			 
			//check attachment
			 if(compSettings.isHideAttachment()==false && compSettings.isMandatoryAttachment())
			 {
				 for (TimeSheetGridModel item : lstGrid) 
				  {
					 if(item.isCantChange()==false)
					 {
						 if(item.getListOfattchments().size()==0)
						 {							
							
						  Messagebox.show("Attachment is mandatory !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
						  return false;
							 
						 }
					 }					 				 
				  }
			 }
			 
		 }
		 catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> isValidDataToSave", ex);
				isValid=false;
			}
		  return isValid;
	  }
	  
	  int lineNo=0;
	  @Command
	  public void saveCommand()
	  {
		  try
		  {
			  StringBuffer EmailsNOtes=new StringBuffer();
			  if(lstGrid==null)
			  {
	    		Messagebox.show("No Data to save !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	    		return;
			  }
			  if(isValidDataToSave()==false)
			  {
	    		//Messagebox.show("No Data to save !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	    		return;
			  }
			  
			  List<Integer> lstEmpKey=new ArrayList<Integer>(); 
			  int tempEmpKey=0;
			  int tempTomroowsPLan=0;
			  //int lineNo=0;
			  lineNo=0;//used in dailytiming
			  
			  List<OverTimeModel> lstOTCalculation=data.getOTCALCULATION(selectedCompany.getCompKey());
			  Date _fromDate;
			  Date  _toDate;
			  Calendar c = Calendar.getInstance();
			  
			  Date tmpRecDate=null;//check to not save twice in DailyTS in case timing found
			  
			  //asume as default
			  _fromDate=fromDate;
			  _toDate=toDate;
			  
			  if(compSettings.getDateType().equals("3"))//date and month
			  {
			  if(selectedDateType==1)//by dates
			   {
				  _fromDate=fromDate;
				  _toDate=toDate;
			   }
			  else
			  {					 
				  c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,1);
				 _fromDate=df.parse(sdf.format(c.getTime()));
				 int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
				 c.set(Integer.parseInt(selectedYear),Integer.parseInt(selectedMonth)-1,maxDay);
				 _toDate=df.parse(sdf.format(c.getTime()));					 					
			  }
			 }
			  
			  else if(compSettings.getDateType().equals("1"))
			  {
				  _fromDate=fromDate;
				  _toDate=toDate;
			  }
			  		
			  //check if approved not to delete and add it again
			  for (TimeSheetGridModel item : lstGrid) 
			  {
				 /*if(item.getApproved()==0 && item.isRowFromSetup()==false)
				 {
					 data.deleteOldTimeSheetsByRecNO(item.getRecNo());
				 }*/
				  if(tempEmpKey!=item.getEmpKey())
				   {
					  tempEmpKey=item.getEmpKey();	
					  data.deleteOldTimeSheets(tempEmpKey, _fromDate, _toDate);
				   }
			
			  }
			  
			  tempEmpKey=0;
			  for (TimeSheetGridModel item : lstGrid) 
			  {
				//if(item.getApproved()==0) //stop when disable control from the zul if approved
				{
									
				//lineNo++;
				item.setHolidayDesc(item.isHoliday()?"Y":"N");		
				/*if(item.getProject()==null && item.isCantChange()==false)
				{
					  Messagebox.show("Please set the project for all records !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
					  return;
				}*/
				/*if(item.isCantChange()==false && compSettings.isHideTomorrowPlan()==false && compSettings.isMandatoryTomorrowPlan())
				{
					String tomorrowPlan=item.getTomorrowPlan()==null?"":item.getTomorrowPlan();
					if(tomorrowPlan.equals(""))
					{
					  Messagebox.show("Please enter tomorrows plan !!","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
					  return;
					}
				}*/
				
				
				if(item.getProject()!=null)
				item.setProjectId(item.getProject().getProjectKey());
				if(item.getCustomerJob()!=null)
					item.setCustomerJobRefKey(item.getCustomerJob().getRecNo());
				//item.setNormalHours(item.getTotals());
				item.setNormalOTHours(240);
				item.setSupervisorId(supervisorID);
				
				//check if approved not to delete and add it again
												
				if(tempEmpKey!=item.getEmpKey())
				{
					tempEmpKey=item.getEmpKey();	
					EmailsNOtes=new StringBuffer();
					//data.deleteOldTimeSheets(tempEmpKey, _fromDate, _toDate);
					//data.deleteOldTimeSheets(tempEmpKey, item.getTsDate(), item.getTsDate());
					
					if(!lstEmpKey.contains(item.getEmpKey()))
					lstEmpKey.add(item.getEmpKey());	
					tmpRecDate=null;
				}
				
				EmailsNOtes.append(item.getNotes());
				EmailsNOtes.append(" \n ");
				
				if(tempTomroowsPLan==0)
				{
					tempTomrrowsPlanForEmail=item.getTomorrowPlan()==null?"":item.getTomorrowPlan().replace("'", "`");
					tempTomroowsPLan++;
					
				}
				
				
				int SequenceID=0;
				int result=0;
				if(item.getApproved()==0)
				{
				if(tmpRecDate==null || !item.getTsDate().equals(tmpRecDate))
				{
					tmpRecDate=item.getTsDate();
					SequenceID=data.getNextSequenceNo();
					item.setRecNo(SequenceID);
					result=data.insertNewTimeSheets(item);
					if(result>0)
					{
					addTimingRecords(item.getTsDate(), item.getRecNo(),item.getEmpKey());
					//add attachment
					data.addTimesheetAttachment(item);
					}
				}
				
				 if(item.getCustomerJob()!=null)
						item.setCustomerJobRefKey(item.getCustomerJob().getRecNo());
				if(item.getOtUnit1()>0)
				{
					item.setOtCalculation(1.25);
					item.setOtUnits(item.getOtUnit1());
					item.setOtAmount(getOTAmount(item, lstOTCalculation,_fromDate, _toDate));
					data.InsertDAILYOT(item);
				}
				if(item.getOtUnit2()>0)
				{
					item.setOtCalculation(1.5);
					item.setOtUnits(item.getOtUnit2());
					item.setOtAmount(getOTAmount(item, lstOTCalculation,_fromDate, _toDate));
					data.InsertDAILYOT(item);
				}
				if(item.getOtUnit3()>0)
				{
					item.setOtCalculation(2);
					item.setOtUnits(item.getOtUnit3());
					item.setOtAmount(getOTAmount(item, lstOTCalculation,_fromDate, _toDate));					
					data.InsertDAILYOT(item);
				}
			}
				
			  }
			 }
			  
			  //email while creating/editing time sheet.
				//String DESCRIPTION="Your Timesheet that you have entered From " + sdf.format(fromDate) +" To " + sdf.format(toDate) + " from web application has been ";
				// String DESCRIPTION="Amount " +loanAmount+ " Start From "+ selectedMonth + " / " + selectedYear + " (" +selectedReason.getEnDescription() + ")" ;			
			 
			 
						
				String DESCRIPTION="";
			  //save user log
			  int activityID=0;	
			  int EMP_KEY=0;
			  if(viewType.equals("add"))
			  {	
				  activityID=common.HREnum.HRStatus.HRNew.getValue();
			  }
			  else if(viewType.equals("edit"))
			  {
				  activityID=common.HREnum.HRStatus.HREdit.getValue();
			  }
			  if(sendEmailFlag)
			  {
				 for(Integer empKey : lstEmpKey)
				 {	
				  	EmployeeModel selectedCompEmployee=new EmployeeModel();
				    DESCRIPTION ="Your Timesheet that you have entered From " + sdf.format(fromDate) +" To " + sdf.format(toDate) + " from web application has been ";
				  	selectedCompEmployee=Hrdata.GetEmployeeDeatailsByEmployeeKeyQuery(empKey);
				  	if(selectedCompEmployee.getSupervisorId()==0)
				  	{
				  		//supervisorEmail="hinawi@eim.ae";
				  		supervisorEmail="eng.chadi@gmail.com";
				  	}
				  	else
				  	{
				  		supervisorEmail=Hrdata.getEmployeeEmail(selectedCompEmployee.getSupervisorId());//if in case supervisor logs in and creates and leave for employee then mail should be sent to supervisor and employee.
				  		
				  	}
				  	logger.info("supervisorEmail >> " + supervisorEmail);
				  	if(!supervisorEmail.equals(""))
				  		sendEmail(supervisorEmail,DESCRIPTION,1,selectedCompEmployee,viewType,EmailsNOtes,true);
				
				  	String empEmail=Hrdata.getEmployeeEmail(selectedCompEmployee.getEmployeeKey());
				  	if(!empEmail.equals(""))
				  	{
				  		logger.info("empEmail >> " + empEmail);
				  		sendEmail(empEmail, DESCRIPTION,1,selectedCompEmployee,viewType,EmailsNOtes,false);
				  	}
				 }
			  }
			  if(lstEmpKey.size()==1)
			  {
				  EMP_KEY=lstEmpKey.get(0);
			  }
			  data.addUserActivity(common.HREnum.HRFormNames.HRTimesheetDetailed.getValue(),activityID, EMP_KEY, selectedCompany.getCompKey(), DESCRIPTION, UserId);
			  if(sendEmailFlag)
			  {
				  if(viewType.equals("add"))
					  Messagebox.show("Time Sheet is Saved Successfully,and a email has been sent.","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
			 		else if(viewType.equals("edit"))
				  Messagebox.show("Time Sheet is updated Successfully,and a email has been sent.","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
			  }
			  else
			  {
				  if(viewType.equals("add"))
					  Messagebox.show("Time Sheet is Saved Successfully","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
			 		else if(viewType.equals("edit"))
				  Messagebox.show("Time Sheet is updated Successfully","Time Sheet", Messagebox.OK , Messagebox.INFORMATION);
			  }
		 	
			  sendEmailFlag=false;
			  
			  
		  }
		  catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> saveCommand", ex);			
			}
		  
	  }
	  
	  private void addTimingRecords(Date tsDate,int recNO,int empKey)
	  {
		  for (TimeSheetGridModel item : lstGrid) 
		  {
			  if(item.getTsDate().equals(tsDate) && item.getEmpKey()==empKey)
			  {
				  lineNo++;
				  if(!item.isTimingFlag())
				  {
				    item.setFromTime("Null");
					item.setToTime("Null");
				  }
			
			//item.setNormalHours(item.getUnits());
			
			if(item.getProject()!=null)
		      item.setProjectId(item.getProject().getProjectKey());
								
			   if(item.getNotes()==null  || item.getNotes().equals("null"))
				   item.setNotes("");
			   if(item.getService()!=null)
				   item.setServiceId(item.getService().getListId());
			   if(item.getCustomerJob()!=null)
					item.setCustomerJobRefKey(item.getCustomerJob().getRecNo());
			   
				  item.setRecNo(recNO);
				  item.setLineNo(lineNo);
				  data.InsertDailyTiming(item,webuserID,webuserName);
			  }
		  }
	  }
	  @SuppressWarnings("unused")
	private double getOTAmount(TimeSheetGridModel row,List<OverTimeModel> lst,Date tsfromDate,Date tstoDate)
	  {
		 double totalAmount=0;
		 try
		 {	
			  int dayFrom,dayTo,diffDay;
			  Calendar c = Calendar.getInstance();	
			  c.setTime(tsfromDate);
			  dayFrom=c.get(Calendar.DAY_OF_MONTH);			 
			  c.setTime(tstoDate);				
			  dayTo=c.get(Calendar.DAY_OF_MONTH);				  
			  //diffDay=dayTo-dayFrom +1;
			  diffDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			  
			 for (OverTimeModel item : lst) 
			 {
				 String isHoliday=row.isHoliday()?"H":"N";
				 if(item.getOtRate()==row.getOtCalculation() && (item.getDayType().equals("") || item.getDayType().equals(isHoliday)) && item.getPositionId()==row.getShiftkey())
				 {
					 if(item.getCalcHours()>0)
					 {
					 List<Double> lstSalary= data.getOTEmployeeSalary(row.getEmpKey(), tstoDate);
					 for (Double amount : lstSalary) 
					 {						 
						totalAmount+= (amount/item.getCalcHours()) * item.getOtRate() * row.getOtUnits();
					 }
				   }
					 else
					 {
						 List<Double> lstSalary= data.getOTEmployeeSalary(row.getEmpKey(), tstoDate);
						 for (Double amount : lstSalary) 
						 {						 							 
							totalAmount+= (amount/(diffDay*row.getUnits())) * item.getOtRate() * row.getOtUnits();
						 }
					 }
				 }
			 }						 
		 }
		  
		catch (Exception ex)
		{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> getOTAmount", ex);			
		}
		 return totalAmount;
		 
	  }
	  
	  @SuppressWarnings("unused")
	private CompanyShiftModel checkIsHolidayShift(List<CompanyShiftModel> lstCompanyShift,int dayNO)
	  {
		  CompanyShiftModel result=null;
		  
		  for (CompanyShiftModel item : lstCompanyShift) 
		  {
			if(item.getDayNo()==dayNO)
			{
				return item;
			}
		  }
		  
		  return result;
	  }
	  
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	HashMap <Integer, ShiftModel> shiftMapper = new HashMap();
	 @SuppressWarnings("unused")
	@Command
	 @NotifyChange({"tsGroupModel"})
	 public void changeFromTime(@BindingParam("row") TimeSheetGridModel row,@BindingParam("type") int type)
	 {
	  
		 try
		 {	
			 if(row.getCalculate().equals("No"))
			 {
				row.setUnits(0);				 
				return;				 
			 }
			 
			ShiftModel objShift=null;
			if(row.isTimingFlag())
			{
				if(!shiftMapper.containsKey(row.getShiftkey()))
				{
				objShift=data.getMinMaxTimeShift(row.getShiftkey());				
				shiftMapper.put(row.getShiftkey(), objShift);
				}
				else
				{
					objShift=shiftMapper.get(row.getShiftkey());
				}
			}
			
			 DateFormat htdf = new SimpleDateFormat("hh:mm a");
			 DateTimeFormatter  hdf = DateTimeFormat.forPattern ("hh:mm a");
			 
			 if(row.getTsFromTime()==null)
				 row.setTsFromTime(objShift.getFromTime());
			 DateTime dt1=hdf.parseDateTime(htdf.format(row.getTsFromTime()));
			 
			 if(row.getTstoTime()==null)
				 row.setTstoTime(row.getTsFromTime());
			 
			 DateTime dt2=hdf.parseDateTime(htdf.format(row.getTstoTime()));
			 
			// objShift=shiftMapper.get(row.getShiftkey());
			 DateTime dtFrom=hdf.parseDateTime(htdf.format(objShift.getFromTime()));
			 DateTime dtTo=hdf.parseDateTime(htdf.format(objShift.getToTime()));
			 
			 
			 if(type==1)//fromTime or toTime
			 {							
				 if(dt1.isBefore(dtFrom))
				 {
					 Messagebox.show("From time should not less than setup from time !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 row.setTsFromTime(objShift.getFromTime());
					 return;
				 }
				/* if(dt2.isAfter(dtTo)) //(row.getTstoTime().after(objShift.getToTime()))
				 {
					 Messagebox.show("To time should not after than setup to time !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 row.setTstoTime(objShift.getToTime());
					 return;
				 }
				 */
				// logger.info("dt1>>  "+dt1);
				// logger.info("dt2>> "+dt2);
				 
				 //check if from time is less than totime ------>stop this after add row for shift holiday
				 /*for (TimeSheetGridModel item : lstGrid)
				 {				
					 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey() && row.getRowIndex()>item.getRowIndex())
					{  
						 DateTime dtRowFrom=hdf.parseDateTime(htdf.format(row.getTsFromTime()));
						 DateTime dtItemTo=hdf.parseDateTime(htdf.format(item.getTstoTime()));
						// logger.info("" + dtRowFrom);
						// logger.info("" + dtItemTo);
						 
						if(dtRowFrom.isBefore(dtItemTo))
						{
							 Messagebox.show("From time should not less than To Time of first shift !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
							 row.setTsFromTime(item.getTstoTime());
							 dt1=hdf.parseDateTime(htdf.format(row.getTsFromTime()));
							 dt2=hdf.parseDateTime(htdf.format(row.getTstoTime()));
							 Period period = new Period(dt1,dt2);
							 double units=(double)period.getHours() +(double)  period.getMinutes()/(double)60;
							 row.setUnits(units);
							 return;
						}							
					}					
				}*/
				 
				 
				 DecimalFormat twoDForm = new DecimalFormat("#.##");
				 Period period = new Period(dt1,dt2);
				 double units=(double)period.getHours() +(double)  period.getMinutes()/(double)60;
				 logger.info("units>> "+units);
				 units=Double.valueOf(twoDForm.format(units));
				 if(units<0)
				 {
					 Messagebox.show("From time should not exceed To Time !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 row.setTstoTime(row.getTsFromTime());
					 row.setUnits(0);
				 }
				
				 else
				 {
					 row.setUnits(units);
					 /*if(units>row.getNormalHours())
					 {
						 double diff =units-row.getNormalHours();
						 if(row.isOtUnit1Enable()==false)
							 row.setOtUnit1((int)diff);
						   else if(row.isOtUnit2Enable()==false)
							   row.setOtUnit2((int)diff);
						   else if(row.isOtUnit3Enable()==false)
							   row.setOtUnit3((int)diff);
					 }
					 else
					 {
						 row.setUnits(units);
					 }*/
				 }
				 
				 
				 //logger.info((double) period.getMinutes()/(double)60 + " units.");
				  
				 //Hours hours = Hours.hoursBetween(dt1, dt2);
				 //Minutes minutes = Minutes.minutesBetween(dt1, dt2);
				 //logger.info("H>>" + hours.getHours()%24 + " M>> " + minutes.getMinutes()%60);
				
				
			 }
			 else if(type==2)//Units
			 {				
				 Calendar cFrom = Calendar.getInstance();											
				 int tmpMinutes=(int) (row.getUnits()*60);				 								
				 cFrom.setTime(htdf.parse(htdf.format(row.getTsFromTime())));
				 cFrom.add(Calendar.MINUTE, tmpMinutes);
				 row.setTstoTime(cFrom.getTime());
				 //check if new toTime is greater than next line fromTime
				 for (TimeSheetGridModel item : lstGrid)
				 {				
					 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey() && row.getRowIndex()<item.getRowIndex())
					{  
						 DateTime dtRowTo=hdf.parseDateTime(htdf.format(row.getTstoTime()));
						 DateTime dtItemFrom=hdf.parseDateTime(htdf.format(item.getTsFromTime()));
						 if(dtRowTo.isAfter(dtItemFrom))
						  {
							 Messagebox.show("To time should less than From Time of second shift !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
							 row.setTstoTime(item.getTsFromTime());
							 dt1=hdf.parseDateTime(htdf.format(row.getTsFromTime()));
							 dt2=hdf.parseDateTime(htdf.format(row.getTstoTime()));
							 Period period = new Period(dt1,dt2);
							 double units=(double)period.getHours() +(double)  period.getMinutes()/(double)60;
							 row.setUnits(units);
							 
						  }
					}
				 }
			 }
			 
			 double totalUnits=0;
			 int totalOTUnits=0;
			 
			 totalUnits=0;
			 double diff=0;
			 double totalDiff=0;
			 for (TimeSheetGridModel item : lstGrid)
			 {
				
				 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
				{						
					totalUnits+=item.getUnits();
					if(totalUnits>row.getTotalNormalHours())
					{
					   diff=totalUnits - row.getTotalNormalHours() - totalDiff;
					   totalDiff+=diff;
					   if(item.isOtUnit1Enable()==false)
						   item.setOtUnit1((int)diff);
					   else if(item.isOtUnit2Enable()==false)
						   item.setOtUnit2((int)diff);
					   else if(item.isOtUnit3Enable()==false)
						   item.setOtUnit3((int)diff);
					}
				}
			 }
			 
			 totalUnits=0;
			 totalOTUnits=0;
			 for (TimeSheetGridModel item : lstGrid)
			 {							
				 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
				{
					totalUnits+=item.getUnits();
					totalOTUnits+=item.getOtUnit1()+item.getOtUnit2()+item.getOtUnit3();
				}
			 }
			 			 
			 for (TimeSheetGridModel item : lstGrid)
			 {
				
				 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
				{   //logger.info("totalOTUnits >>> " + totalOTUnits);
					item.setTotals(totalUnits);
					item.setTotalOTUnits((int)totalOTUnits);
				}
			 }
			 
		 }
		 catch (Exception ex)
		 {	
				logger.error("ERROR in DailyTimeSheetViewModel ----> changeFromTime", ex);			
		}
	 }
	 
	 @Command
	// @NotifyChange({"tsGroupModel"})
	 public void insertLineCommand(@BindingParam("row") TimeSheetGridModel row)
	 {
		 selectedRow=row;
		 logger.info(row + ">>>>>> "); //selectedRow.getRowIndex()+ "");
		 logger.info(selectedRow.getDayName() + ">>>>>> ");
		 //tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
	 }
	 
	 @Command
	 @NotifyChange({"tsGroupModel"})
	 public void deleteNewLineCommand(@BindingParam("row") TimeSheetGridModel row)
	 {
		 //TimeSheetGridModel tmpRow=row;
		 
		/* if(row.isNewRowAdded()==false)
		 {
			 Messagebox.show("You can't delete this line !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		 }*/
		 
		 if(row.isMainShift() || row.isFirstOfRecord() || row.getUnitKey()>0)
		 {
			 Messagebox.show("You can't delete this line !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		 }
		 				 
		 for (TimeSheetGridModel item : lstGrid)
		 {
			if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey() )
			{
				logger.info("no of shifts >> " + item.getNoOfshifts());
				item.setNoOfshifts(item.getNoOfshifts()-1);
			}
		 }
		 lstGrid.remove(row);
		 
		 double totalUnits=0;
		 for (TimeSheetGridModel item : lstGrid)
		 {
			
			//if(item.getDayName().equals(row.getDayName()) && item.getEmpKey()==row.getEmpKey() && item.getRowIndex()==row.getRowIndex())
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			{
				totalUnits+=item.getUnits();
			}
		 }
		 row.setOtUnit1(0);
		 if(totalUnits > row.getTotalNormalHours())
		 {			
			 if(row.isOtUnit1Enable()==false)
			 {
				 double diff=totalUnits - row.getTotalNormalHours();		
				 row.setOtUnit1((int)diff);
			 }
		 }
		 totalUnits=0;
		 double diff=0;
		 double totalDiff=0;
		 for (TimeSheetGridModel item : lstGrid)
		 {
			
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			{						
				totalUnits+=item.getUnits();
				if(totalUnits>row.getTotalNormalHours())
				{
				   diff=totalUnits - row.getTotalNormalHours() - totalDiff;
				   totalDiff+=diff;
				   item.setOtUnit1((int)diff);
				}
			}
		 }
		 
		 //recalcuale units and OT
		 double totalOTUnits=0;
		 totalUnits=0;
		 for (TimeSheetGridModel item : lstGrid)
		 {							
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			{
				totalUnits+=item.getUnits();
				totalOTUnits+=item.getOtUnit1()+item.getOtUnit2()+item.getOtUnit3();
			}
		 }
		 
		 for (TimeSheetGridModel item : lstGrid)
		 {			
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			{
				item.setTotals(totalUnits);
				item.setTotalOTUnits((int)totalOTUnits);
			}
		 }
		 
		 rowIndex=0;
		 for (TimeSheetGridModel item : lstGrid)
		 {
			item.setRowIndex(rowIndex);
			rowIndex++;
		 }
		
		tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
	 }
	 @SuppressWarnings("unused")
	@Command
	 @NotifyChange({"tsGroupModel"})
	 public void addNewLineCommand(@BindingParam("row") TimeSheetGridModel row)
	 {
		 try
		 {
		 int index=0;
		 boolean isRowFound=false;
		 if(row.isTimingFlag()==false)
			 return;
		 
		 for (TimeSheetGridModel item : lstGrid)
		 {
			 if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey())
			 {
				isRowFound=true;
				index++;
				//break;
			 }
			 if(isRowFound==false)
			 {
			 index++;
			 }
			 
		 }
		TimeSheetGridModel obj=new TimeSheetGridModel();
		obj.setLstTaskStatus(lstTaskStatus);	
		obj.setCompSettings(compSettings);
		obj.setLstCustomerTaks(lstCustomerTaks);
		obj.setEmpKey(row.getEmpKey());
		obj.setCompKey(row.getCompKey());
		obj.setEmpNo(row.getEmpNo());
		obj.setDayName(row.getDayName());
		obj.setEnFullName(row.getEnFullName());
		obj.setNotes("");
		obj.setEmployeeStatus(row.getEmployeeStatus());
		obj.setSrNo(row.getSrNo());
		obj.setSalaryStatus(row.getSalaryStatus());
		obj.setPosition(row.getPosition());
		obj.setApproved(row.getApproved());
		obj.setEmployeementDate(row.getEmployeementDate());
		obj.setTsDate(row.getTsDate());
		obj.setNoOfshifts(row.getNoOfshifts()+1);
		obj.setShiftkey(row.getShiftkey());
		obj.setShiftRecNo(row.getShiftRecNo());
		obj.setTimingFlag(true);
		obj.setProject(row.getProject());
		obj.setService(row.getService());
		obj.setStatus(row.getStatus()); //("Present");
		obj.setHoliday(row.isHoliday());	
		obj.setUnits(row.getUnits());
		obj.setUnitType(row.getUnitType());
		obj.setNormalHours(0);//(row.getNormalHours());
		obj.setTotals(row.getTotals());
		obj.setTotalNormalHours(row.getTotalNormalHours());
		obj.setCalculate(row.getCalculate());
		obj.setUnitKey(0);//(row.getUnitKey());
		obj.setLeaveFlag(row.getLeaveFlag());
		obj.setOtUnit1Enable(row.isOtUnit1Enable());
		obj.setOtUnit2Enable(row.isOtUnit2Enable());
		obj.setOtUnit3Enable(row.isOtUnit3Enable());
		obj.setAttachPath("");
		
		rowIndex++;
		obj.setRowIndex(rowIndex);
		
		
		for (TimeSheetGridModel item : lstGrid)
		 {
			if(item.getTsDate().equals(row.getTsDate()) && item.getRowIndex()==row.getRowIndex() && item.getEmpKey()==row.getEmpKey())
			{
				obj.setTsFromTime(item.getTstoTime());
				obj.setTstoTime(item.getTstoTime());
				obj.setUnits(0);
				break;
			}
		 }
		obj.setNewRowAdded(true);
		//lstGrid.add(index, obj);
		logger.info("row index added>> " + obj.getRowIndex());
		if(lstGrid.size()>row.getRowIndex())
		lstGrid.add(row.getRowIndex()+1, obj);
		
		 rowIndex=0;
		 for (TimeSheetGridModel item : lstGrid)
		 {
			item.setRowIndex(rowIndex);
			rowIndex++;
		 }
		
		for (TimeSheetGridModel item : lstGrid)
		 {
			if(item.getTsDate().equals(row.getTsDate()) && item.getEmpKey()==row.getEmpKey() )
			{
				item.setNoOfshifts(obj.getNoOfshifts());
			}
		 }
		
		tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
		}
		 
		 catch (Exception ex)
			{	
			 	Session sess = Sessions.getCurrent();		
				WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");	
				String error="Error happen for company>> " +dbUser.getCompanyid() + " " + dbUser.getCompanyName();
				error+=" user >> " + dbUser.getUserid() + " name: " + dbUser.getUsername();
				error+="lstGrid size = " +lstGrid.size() + " row index = "+ row.getRowIndex();
				error+=" rowIndex= " + rowIndex;
				error+="viewType >>> " + viewType;
				logger.error("ERROR in DailyTimeSheetViewModel ----> addNewLineCommand" + error, ex);			
			}
	 }
	 
	 	@Command
	 	public void attachFileCommand(@BindingParam("row") TimeSheetGridModel row)
	 	{
	 		 Map<String,Object> arg = new HashMap<String,Object>();
			 arg.put("row", row);
			 arg.put("compSettings", compSettings);
			 Executions.createComponents("/timesheet/attchment.zul", null,arg);
	 	}
	 	
	  @GlobalCommand 
	  @NotifyChange({"tsGroupModel"})
	   public void attachFileClose(@BindingParam("row")TimeSheetGridModel row)
	   {
		  for (TimeSheetGridModel item : lstGrid) 
		  {
			  if(item.getEmpKey()==row.getEmpKey() && item.getTsDate().equals(row.getTsDate()))
			  {
				  item.setListOfattchments(row.getListOfattchments());
			  }
		  }
	   }
	 	
	 	@Command
		@NotifyChange({"tsGroupModel"})
		 public void attachFileCommand1(BindContext ctx,@BindingParam("row") TimeSheetGridModel row)
		 {
	 		try
	 		{
			 selectedRow=row;
			 logger.info(row + ">>>>>> "); //selectedRow.getRowIndex()+ "");
			 logger.info(selectedRow.getDayName() + ">>>>>> ");
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(row.getTsDate());
			 String monthName=new SimpleDateFormat("MMM").format(cal.getTime());
			 String yearName=new SimpleDateFormat("yyyy").format(cal.getTime());
			 //tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
			 
		/*	 row.setAttachPath("test.123");
			if(true)
				return;*/
			
			 if(!compSettings.getFtpHost().equals(""))
			 {
				 int port = 21;
				 if(compSettings.getFtpPort()>0)
				 port=compSettings.getFtpPort();
				 FTPClient ftpClient = new FTPClient();
				 ftpClient.connect(compSettings.getFtpHost(), port);
				 int replyCode = ftpClient.getReplyCode();
				 if (!FTPReply.isPositiveCompletion(replyCode)) {
					 	logger.info("Operation failed. Server reply code: " + replyCode);		               
		                return;
		            }
				 	boolean success = ftpClient.login(compSettings.getFtpUser(),compSettings.getFtpPassword());		           
		            if (!success) {		              
		                logger.info("Could not login to the server");
		                return;
		            } else {		               
		                logger.info("LOGGED IN SERVER");
		            }
		            
		            ftpClient.enterLocalPassiveMode();	            
		            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		         
		            UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
		            String dirPath=File.separator + compSettings.getFtpDirectory()+File.separator +yearName+File.separator + row.getEmpKey()+File.separator+monthName + File.separator;
		            ftpClient.changeWorkingDirectory(dirPath);
		            int returnCode = ftpClient.getReplyCode();
		            if (returnCode == 550) {
		                // file/directory is unavailable
		            	ftpClient.makeDirectory(dirPath);
		            }
		            
		            String filePath="";		            		         
		            filePath=dirPath + event.getMedia().getName();
		            		           
		            String remoteFile =filePath; 
		            
		            logger.info("Start uploading file");
		            InputStream inputStream=null;
		            if(event.getMedia().isBinary())
		            {
		            	inputStream = event.getMedia().getStreamData();
		            	 boolean done = ftpClient.storeFile(remoteFile, inputStream);
				            inputStream.close();
				            if (done) {
				            	  logger.info("The media file is uploaded successfully.");
				            	  row.setAttachPath(filePath);
				            	  }
		            }
		            else
		            {
		            	 String s = event.getMedia().getStringData();
		            	// logger.info(s);
		            	 inputStream =new ByteArrayInputStream(s.getBytes()); //event.getMedia().getStreamData();
		            	 //int size = inputStream.available(); 
		            	// byte [] buf = new byte[size];
		            	// inputStream.read(buf);
		            	 boolean done = ftpClient.storeFile(remoteFile, inputStream);
				            inputStream.close();
				            if (done) {
				            	  logger.info("The string data file is uploaded successfully.");
				            	  row.setAttachPath(filePath);
				            }
		            }
		           // InputStream inputStream = event.getMedia().getStreamData();//new FileInputStream(firstLocalFile);
		 
			 }
	 		}
	 		catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> attachFileCommand", ex);			
			}
		 }
	 
	 	
	 	@Command
	 	public void downloadFileCommand(@BindingParam("row") TimeSheetGridModel row)
	 	{
	 		FTPClient ftpClient = new FTPClient();
	 		try
	 		{
	 			if(!compSettings.getFtpHost().equals(""))
				 {
					 int port = 21;
					 if(compSettings.getFtpPort()>0)
					 port=compSettings.getFtpPort();
					 
					 ftpClient.connect(compSettings.getFtpHost(), port);
					 int replyCode = ftpClient.getReplyCode();
					 if (!FTPReply.isPositiveCompletion(replyCode)) {
						 	logger.info("Operation failed. Server reply code: " + replyCode);		               
			                return;
			            }
					 	boolean success = ftpClient.login(compSettings.getFtpUser(),compSettings.getFtpPassword());		           
			            if (!success) {		              
			                logger.info("Could not login to the server");
			                return;
			            } else {		               
			                logger.info("LOGGED IN SERVER");
			            }
			            
			            ftpClient.enterLocalPassiveMode();	            
			            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			            
			            String remoteFile1 = row.getAttachPath();//"/timesheet/3/Jan/vs 2013 install.txt";
			            InputStream stream = ftpClient.retrieveFileStream(remoteFile1);
			            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			        	String mimeType=mimeTypesMap.getContentType(row.getAttachPath());
			        	logger.info("mimeType>>" +  mimeType);
			        	
			        	int index = remoteFile1.lastIndexOf("\\");
			        	String fileName=remoteFile1.substring(index+1);
			        	logger.info(fileName);
			            Filedownload.save(IOUtils.toByteArray(stream), mimeType,fileName);
			            
			          /*  BufferedReader reader = null;
			            String firstLine = null;
			            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			            firstLine = reader.readLine();
			            logger.info(">>>>> "+firstLine);
			            if (reader != null) 
			            	reader.close(); */
			            
			        /*   // File downloadFile1 = new File("D:/Downloads/video.mp4");
			            OutputStream local  = new FileOutputStream("c:/test.txt");
			         //   URL url = new URL("ftp://"+user+"":""+password+""@""+host+""/""+remoteFile+"";type=i");
			            	
			            //InputStream is = ftpClient.
			            BufferedInputStream bis = new BufferedInputStream(is);
			           // File file=new File(
			            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
						String mimeType="txt";//mimeTypesMap.getContentType(file);
			            Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, row.getAttachPath());
			            
						ftpClient.retrieveFile(remoteFile1, local);
						logger.info("file retrive from server");
						BufferedOutputStream bos = new BufferedOutputStream(local);
						byte[] buffer = new byte[1024];
					      int readCount;

					      while( (readCount = bis.read(buffer)) > 0)
					      {
					        bos.write(buffer, 0, readCount);
					      }
					      bos.close();*/
						
				 }
	 		}
	 		catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> downloadFileCommand", ex);			
			}
	 		finally
	 		{
	 			try {
	                if (ftpClient.isConnected()) {
	                    ftpClient.logout();
	                    ftpClient.disconnect();
	                }
	            } catch (Exception ex) {
	            	logger.error("ERROR in DailyTimeSheetViewModel ----> downloadFileCommand", ex);			
	            }
	 		}
	 	}
	 	@Command
	 	@NotifyChange({"tsGroupModel"})
	 	public void deleteFileCommand(@BindingParam("row") TimeSheetGridModel row)
	 	{
	 		row.setAttachPath("");
	 	}
	 	
	 
	    @Command
		public void exportListboxToExcel(@BindingParam("ref") Grid grid)throws Exception 
		{
	    	try
	    	{		
	    		
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ExcelExporter exporter = new ExcelExporter();
				exporter.export(grid, out);
				
				AMedia amedia = new AMedia("TimeSheetReport.xlsx", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();			
	    	}
	    	catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> exportListboxToExcel", ex);			
			}
		}
	    
	    @Command
		public void exportGrid(@BindingParam("ref") Grid grid) throws Exception {
			
	    	
	    	
	    	PdfExporter exporter = new PdfExporter();

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			exporter.export(grid, out);

			AMedia amedia = new AMedia("FirstReport.pdf", "pdf", "application/pdf", out.toByteArray());
			Filedownload.save(amedia);

			out.close();
			/*
			ExcelExporter exporter = new ExcelExporter();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			exporter.export(grid, out);
	
			AMedia amedia = new AMedia("AniReport.xlsx", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
	
			out.close();
			*/
		}
	    
	    @Command
		public void exportByDataModel() throws Exception 
		{
	    	if(this.canExport==false)
	    	{
	    		Messagebox.show("No Data to export !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	    		return;
	    	}
	    	
	    	final PdfExporter exporter = new PdfExporter();
			final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
			final FontFactory fontFactory = exporter.getFontFactory();
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String[] tsHeaders;
			if(timingFlag)
			{
				tsHeaders = new String[]{"","Date", "Day", "Status", "Calculate", "Unit Type","From Time","To Time" ,"Units","Total","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total","Customer Job","Project","Service Type","Notes"};			
			}
			else
			{
				tsHeaders = new String[]{"","Date", "Day", "Status", "Calculate", "Unit Type","Units","Total","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total","Customer Job","Project","Service Type","Notes"};
			}
			
			final String[] headers=tsHeaders;
			exporter.setInterceptor(new Interceptor <PdfPTable> () {
				
				//@Override
				public void beforeRendering(PdfPTable table) {
					for (int i = 0; i < headers.length; i++) {
						String header = headers[i];
						Font font = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
						
						PdfPCell cell = exporter.getPdfPCellFactory().getHeaderCell();
						cell.setPhrase(new Phrase(header, font));
						if ("Units".equals(header) || "Total".equals(header)) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						
						table.addCell(cell);
					}
					table.completeRow();
				}
				
				//@Override
				public void afterRendering(PdfPTable table) {
				}
			});
			
					
			  exporter.export(headers.length, tsGroupModel.getData(), new GroupRenderer<PdfPTable, TimeSheetGridModel>() {

				//@Override
				public void render(PdfPTable table, TimeSheetGridModel item, boolean isOddRow) {
					Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
					PdfPCell cell = cellFactory.getCell(isOddRow);
					
					cell.setPhrase(new Phrase(item.getHolidayDesc(), font));
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(sdf.format(item.getTsDate()), font));
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getDayName(), font));					
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getStatus(), font));					
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getCalculate(),font));
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getUnitType(),font));
					table.addCell(cell);
					
					if(isTimingFlag())
					{
						cell = cellFactory.getCell(isOddRow);
						cell.setPhrase(new Phrase(item.getFromTime(),font));
						table.addCell(cell);
						
						cell = cellFactory.getCell(isOddRow);
						cell.setPhrase(new Phrase(item.getToTime(),font));
						table.addCell(cell);
					}
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase("" +item.getUnits(),font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase("" +item.getTotals(),font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase("" +item.getOtUnit1(),font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase("" +item.getOtUnit2(),font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase("" +item.getOtUnit3(),font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase("" +item.getTotalOT(),font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);
					
					if(item.getCustomerJob()!=null)
					{
						cell = cellFactory.getCell(isOddRow);
						cell.setPhrase(new Phrase(item.getCustomerJob().getFullName(),font));					
						table.addCell(cell);
					}else
					{
						cell = cellFactory.getCell(isOddRow);
						cell.setPhrase(new Phrase("",font));					
						table.addCell(cell);
					}
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getProjectName(),font));					
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getServiceName(),font));					
					table.addCell(cell);
					
					cell = cellFactory.getCell(isOddRow);
					cell.setPhrase(new Phrase(item.getNotes(),font));					
					table.addCell(cell);
					
					table.completeRow();
				}

				//@Override
				public void renderGroup(PdfPTable table, Collection<TimeSheetGridModel> item) {
					Iterator<TimeSheetGridModel> iterator = item.iterator();
					if (iterator.hasNext()) {
						TimeSheetGridModel obj = iterator.next();
						Font font = fontFactory.getFont(FontFactory.FONT_TYPE_GROUP);
						PdfPCell cell = cellFactory.getGroupCell();
						
						cell.setPhrase(new Phrase(obj.getEmpNo() +" - " + obj.getEnFullName() + " : " + obj.getSalaryStatus(), font));
						cell.setColspan(headers.length);
						table.addCell(cell);
						
						table.completeRow();
					}
					
				}

				//@Override
				public void renderGroupfoot(PdfPTable table,Collection<TimeSheetGridModel> item) {
					Font font = fontFactory.getFont(FontFactory.FONT_TYPE_GROUPFOOT);
					PdfPCell cell = cellFactory.getGroupCell();
					cell.setPhrase(new Phrase("Total Transactions: " + (item != null ? item.size() : 0), font));
					//table.addCell(cell);
					
					//cell = cellFactory.getCell(false);
					cell.setColspan(headers.length - 1);
					table.addCell(cell);
					table.completeRow();
					
				}
			  }, out);
			  
			  
			AMedia amedia = new AMedia("FirstReport.pdf", "pdf", "application/pdf", out.toByteArray());
			Filedownload.save(amedia);
			
			out.close();
			
			
		  }
			
	    @Command
	    public void exportExcelByDataModel()
		{
	    	try
	    	{
	    			    
	    	if(this.canExport==false)
	    	{
	    		Messagebox.show("No Data to export !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	    		return;
	    	}
	    	
	    	 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 String[] tsHeaders;
			 tsHeaders = new String[]{"Emp No","Name", "Date", "Day", "Status", "Calculate", "Unit Type","From Time","To Time","Units","Total","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total","Customer Job","Project","Service Type","Notes"};
			 final String[] headers=tsHeaders;
			 final ExcelExporter exporter = new ExcelExporter();
			 			
			 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
			    @SuppressWarnings("static-access")
				public void beforeRendering(XSSFWorkbook target) {
			        ExportContext context = exporter.getExportContext();
			         
			        for (String header : headers) {
			            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
			            cell.setCellValue(header);
			             				           
			                CellStyle srcStyle = cell.getCellStyle();
			                if (srcStyle.getAlignment() != CellStyle.ALIGN_CENTER) {				                   
								XSSFCellStyle newCellStyle = target.createCellStyle();
			                    newCellStyle.cloneStyleFrom(srcStyle);
			                    newCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			                    cell.setCellStyle(newCellStyle);
			                }
			            
			        }
			    }
			    
			    public void afterRendering(XSSFWorkbook target) {			     
			    }				    				   
			});
	    	
			 // tsHeaders = new String[]{"Emp No","Name", "Date", "Day", "Status", "Calculate", "Unit Type","Units","Total",
			 //"OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total","Project","Service Type","Notes"};
			 
			        exporter.export(headers.length, lstGrid, new RowRenderer<Row, TimeSheetGridModel>() {
					@SuppressWarnings("static-access")
					public void render(Row table, TimeSheetGridModel item, boolean isOddRow) 
						 {
						 	ExportContext context = exporter.getExportContext();
					        XSSFSheet sheet = context.getSheet();				        
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEmpNo());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnFullName());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(sdf.format(item.getTsDate()));				     
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDayName());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getStatus());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCalculate());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getUnitType());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getFromTime());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getToTime());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getUnits());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTotals());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOtUnit1());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOtUnit2());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOtUnit3());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOtUnit1() + item.getOtUnit2() + item.getOtUnit3());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomerJob()!=null? item.getCustomerJob().getFullName() : "");
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getProject()!=null? item.getProject().getProjectName() : "");
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getService() !=null ? item.getService().getEnDescription() : "");
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getNotes());
						 }
						 
				    }, out);	
			 
			 	AMedia amedia = new AMedia("Timesheet.xlsx", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
			 
			
	    	}	    	
	    	catch (Exception ex)
			{	
				logger.error("ERROR in DailyTimeSheetViewModel ----> exportExcelByDataModel", ex);			
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

	@NotifyChange({"lastTimeSheet","lstProject","lastTimeSheetByUser","objCompanySetup","compSetup"})
	public void setSelectedCompany(CompanyModel selectedCompany)
	{
		this.selectedCompany = selectedCompany;
		lstOTMax=data.getMaxOTList(selectedCompany.getCompKey());
		compSetup=Hrdata.getLeaveCompanySetup(selectedCompany.getCompKey());
		objCompanySetup=data.getCompanySetup(selectedCompany.getCompKey());	
		getLastTimeSheetCreated();
		if(viewType.equals("add"))
		lstProject=data.getProjectList(selectedCompany.getCompKey(),"",true,supervisorID);
		else
		lstProject=data.getProjectList(selectedCompany.getCompKey(),"",false,supervisorID);			
	}

	public Date getFromDate() {
		return fromDate;
	}

	@NotifyChange("toDate")
	public void setFromDate(Date fromDate) 
	{
		try
		{
			/*
			Calendar c = Calendar.getInstance();	
			c.setTime(fromDate);
			int month=c.get(Calendar.MONTH);
			int year=c.get(Calendar.YEAR);
			c.set(year, month,c.getActualMaximum(Calendar.DAY_OF_MONTH));
			toDate=df.parse(sdf.format(c.getTime()));
			*/
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DailyTimeSheetViewModel ----> setFromDate", ex);			
		}
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public TimeSheetGroupAdapter getTsGroupModel() {
		return tsGroupModel;
	}

	public void setTsGroupModel(TimeSheetGroupAdapter tsGroupModel) {
		this.tsGroupModel = tsGroupModel;
	}

	public String getLastTimeSheet() {
		return lastTimeSheet;
	}

	public void setLastTimeSheet(String lastTimeSheet) {
		this.lastTimeSheet = lastTimeSheet;
	}

	public boolean isTimingFlag() {
		return timingFlag;
	}

	public void setTimingFlag(boolean timingFlag) {
		this.timingFlag = timingFlag;
	}


	public boolean isCanExport() {
		return canExport;
	}

	public void setCanExport(boolean canExport) {
		this.canExport = canExport;
	}

	public List<String> getLstStatus() {
		return lstStatus;
	}

	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	public List<String> getLstCalculate() {
		return lstCalculate;
	}

	public void setLstCalculate(List<String> lstCalculate) {
		this.lstCalculate = lstCalculate;
	}

	public List<ProjectModel> getLstProject() {
		return lstProject;
	}

	public void setLstProject(List<ProjectModel> lstProject) {
		this.lstProject = lstProject;
	}

	public List<HRListValuesModel> getLstPositions() {
		return lstPositions;
	}

	public void setLstPositions(List<HRListValuesModel> lstPositions) {
		this.lstPositions = lstPositions;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) 
	{
		this.selectedStatus = selectedStatus;
		if(selectedStatus.equals("Holiday"))
		{
			Messagebox.show("Can't set holiday,You change Shift Setup!!");
			selectedStatus="";
		}
		if(selectedStatus.equals("Leave"))
		{
			Messagebox.show("Can't set Leave,Please Create Leave from Activities Menu!!");
			selectedStatus="";
		}
	}

	public boolean isOpenGroup() {
		return isOpenGroup;
	}

	public void setOpenGroup(boolean isOpenGroup) {
		this.isOpenGroup = isOpenGroup;
	}

	public List<OverTimeModel> getLstOTMax() {
		return lstOTMax;
	}

	public void setLstOTMax(List<OverTimeModel> lstOTMax) {
		this.lstOTMax = lstOTMax;
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

	public boolean isShowMonth() {
		return showMonth;
	}

	public void setShowMonth(boolean showMonth) {
		this.showMonth = showMonth;
	}

	public String getLastTimeSheetByUser() {
		return lastTimeSheetByUser;
	}

	public void setLastTimeSheetByUser(String lastTimeSheetByUser) {
		this.lastTimeSheetByUser = lastTimeSheetByUser;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public CompanySettingsModel getCompSettings() {
		return compSettings;
	}

	public void setCompSettings(CompanySettingsModel compSettings) {
		this.compSettings = compSettings;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}

	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}

	public int getEmployeeKey() {
		return employeeKey;
	}

	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}

	public TimeSheetGridModel getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(TimeSheetGridModel selectedRow) {
		this.selectedRow = selectedRow;
	}

	public CompSetupModel getObjCompanySetup() {
		return objCompanySetup;
	}

	public void setObjCompanySetup(CompSetupModel objCompanySetup) {
		this.objCompanySetup = objCompanySetup;
	}
	
	
	private void sendEmail(String toMail,String leaveDescription,int type,EmployeeModel selectedCompEmployee,String viewType,StringBuffer EmailNotes,boolean withNotification)
	{
		try
		{
			String[] to =null;
			String[] bcc =null;
			to= toMail.split(",");	
			MailClient mc = new MailClient();
			String subject="";
			if(viewType.equalsIgnoreCase("add"))
				subject="Timesheet Submitted by  "+dbUser.getFirstname()+" for "+selectedCompEmployee.getFullName()+"("+selectedCompEmployee.getEmployeeNo()+")";
			else if(viewType.equalsIgnoreCase("edit"))
				 subject="Timesheet Edited by "+dbUser.getFirstname()+" for "+selectedCompEmployee.getFullName()+"("+selectedCompEmployee.getEmployeeNo()+")";
			StringBuffer result=null;
			
			result=new StringBuffer();
			result.append("<table border='0'>");
			if(compSetup!=null && compSetup.getActivateEmail()!=null && compSetup.getActivateEmail().equalsIgnoreCase("Y"))
			{
		  	if(type==1) //Create Leave
		  	{
			  	
			  	result.append("<table border='0' cellpadding='0' cellspacing='0' style='width: 100%;'>");
			  	result.append("<caption>");
			  	result.append("	<strong>Timesheet Online</strong></caption>");
			  	result.append("<tbody>");
			  	result.append("	<tr>");
			  	result.append("<td style='width:295px;'>");
			  	result.append("<p>");
			  	result.append("	<strong>Employee Name : </strong>"+selectedCompEmployee.getFullName()+"</p>");
			  	result.append("<p>");
			  	result.append("<strong>Employee No. : </strong>"+selectedCompEmployee.getEmployeeNo()+ "</p>");
			  	result.append("<p>");
				result.append("<p>");
				result.append("<strong>Company Name : </strong>"+selectedCompany.getEnCompanyName()+ "</p>");
				result.append("<p>");
			  	result.append("	<strong>Position : </strong>"+selectedCompEmployee.getPosition()+ "</p>");
			  	result.append("<p>");
			  	result.append("	<strong>Department :</strong>"+selectedCompEmployee.getDepartment()+ "</p>");
			  	result.append("	<p>");
			  	result.append("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</p>");
			  	result.append("</td>");
			  	result.append("<td style='width: 295px; text-align: right; vertical-align: top;'>");
			  	result.append("<p>");
			  	result.append("	<strong>User </strong>: "+dbUser.getFirstname()+"</p>");
			  	result.append("<p>");
			  	result.append("	&nbsp;</p>");
			  	result.append("</td>");
			  	result.append("</tr>");
			  	result.append("</tbody>");
			  	result.append("</table> ");

			  	
				//result.append("	<p>	<strong>"+leaveDescription+"");
				//if(viewType.equalsIgnoreCase("add"))
				//result.append(" Created .</strong></p>");
				//else if(viewType.equalsIgnoreCase("edit"))
				//result.append(" Edited .</strong></p>");
				result.append("<p><strong>Below is the entered timesheet detail</strong></p>");
				
			  		  	
			  double totalHours=0;
			  double totalOvertimeHours=0;	
			  double totalShiftHours=0;
			  double totalNotworkingHours=0;	
			  double unitsTotalByday=0;
				SimpleDateFormat tdf =new SimpleDateFormat("hh:mm a");
				Date tempDate=new Date();
				int caseNumber=100;
			  	 for (TimeSheetGridModel item : lstGrid) 
				 {
			  	if(item.getEmpKey()==selectedCompEmployee.getEmployeeKey())
				{
			  	 if(caseNumber==100)
			  	 {
			  		tempDate=item.getTsDate();
				  	
				  	result.append("<p><strong>Date : "+new SimpleDateFormat("dd-MM-yyyy").format(item.getTsDate()) +" (Day :"+item.getDayName()+")</strong></p>");
			  		
			  		caseNumber=caseNumber+1;
			  	 }
			  	 
			 	if(!tempDate.equals(item.getTsDate()))
			  	{
			  		tempDate=item.getTsDate();
			  		
				  	
				  	result.append("	<p> ");
				  	result.append("<strong>Tomorrow&#39;s Plan</strong> :&nbsp;"+tempTomrrowsPlanForEmail+"</p> ");
				  	result.append("<p> ");
				  	result.append("<strong>Total Shift (Hours/Days)</strong> &nbsp;:&nbsp;"+totalShiftHours+"</p> ");
				  	result.append("<p> ");
				  	result.append("<strong>Total Working (Hours/Days)</strong> :&nbsp;"+totalOvertimeHours+"</p> ");
				  	result.append("<p> ");
				  	result.append("<strong>Total Not Working (Hours/Days) </strong>&nbsp;:&nbsp;"+Double.parseDouble(new DecimalFormat("#.#").format(totalHours))+"</p> ");
				  	result.append("<p> ");
				  	result.append("<strong>Total OT(Over Time)</strong> &nbsp;:&nbsp;"+Double.parseDouble(new DecimalFormat("#.#").format(totalNotworkingHours))+".</p> ");
				  	result.append("<p>");
				  	result.append("&nbsp;</p> ");
				  	
				  	
					result.append("<p><kbd><big><span class='marker'><code><tt><span style='font-size:20px'><strong><span style='color:#FF0000'>------------------------------------------------------------------------</span></strong></span></tt></code></span></big></kbd></p>");
					
					result.append("<p>");
				  	result.append("&nbsp;</p> ");
				  	
			  		result.append("<p><strong>Date : "+new SimpleDateFormat("dd-MM-yyyy").format(item.getTsDate()) +" (Day :"+item.getDayName()+")</strong></p>");
				  	
				  	
					unitsTotalByday=0;
				  	
			  	}
			 	
			 	totalHours=item.getTotals();
				totalOvertimeHours=item.getTotalOTUnits();	
				totalShiftHours=item.getTotalNormalHours();
				if(totalShiftHours>=totalHours)
				totalNotworkingHours=totalShiftHours-totalHours;
				else
				totalNotworkingHours=0;
				String fromTime="";
				String ToTime="";
				if(item.getTsFromTime()!=null && item.getTstoTime()!=null)
				{
					fromTime=tdf.format(item.getTsFromTime());
					ToTime=tdf.format(item.getTstoTime());
				}
				else
				{
					fromTime="Holiday";
					ToTime="Holiday";
				}
				
				
			
				if(item.isTimingFlag())
				{
					
					if(!fromTime.equalsIgnoreCase("Holiday") && !fromTime.equalsIgnoreCase(""))
						unitsTotalByday=unitsTotalByday+item.getUnits();
					else
						totalHours=unitsTotalByday;
					
			  	result.append("<table border='0' cellpadding='0' cellspacing='0' style='width:100.0%;' width='100%'>");
			  	result.append("<tbody>");
			  	result.append("	<tr>");
			  	result.append("	<td style='width:33.34%;height:43px;background-color: #a2dafa;'>");
			  	result.append("	<p>");
			  	result.append("	<strong>From Time : </strong><strong>"+fromTime+"</strong></p>");
			  	result.append("</td>");
			  	result.append("<td style='width:33.34%;height:43px;background-color: #a2dafa;'>");
			  	result.append("	<p>");
			  	result.append("	<strong>To time :</strong><strong> "+ToTime+"</strong></p>");
			  	result.append("</td>");
			  	result.append("<td style='width:33.32%;height:43px;background-color: #a2dafa;'>");
			  	result.append("	<p>");
			  	result.append("		<strong>Hours :&nbsp;</strong>"+item.getUnits()+"</p>");
			  	result.append("</td>");
			  	result.append("</tr>");
			  
				}
				
			  	result.append("	<tr>");
			  	result.append("<td style='width:33.34%;height:26px;'>");
			  	result.append("<p>	&nbsp;</p>");
			  	result.append("<p><strong>Status</strong>:&nbsp;"+item.getStatus()+"</p>");
			  	result.append("<p>	&nbsp;</p>");
			  	result.append("</td><td style='width:33.34%;height:26px;'>");
			  	result.append("	<p><strong>Calculate :&nbsp;</strong>"+item.getCalculate()+"</p>");
			  	result.append("</td><td style='width:33.32%;height:26px;'>");
			  	result.append("	<p>&nbsp;</p>");
			  	result.append("</td>");
			  	result.append("</tr>");
			  	
			  	//if(item.getOtUnit1()>0 || item.getOtUnit2()>0 || item.getOtUnit3()>0)
			  	{
			  	result.append("<tr>");
			  	result.append("<td style='width:33.34%;height:16px;'>");
			  	result.append("	<p>&nbsp;</p>");
			  	result.append("	<p>	<strong>OT Unit 1.25 :&nbsp;</strong>"+item.getOtUnit1()+"</p>");
			  	result.append("	<p>&nbsp;</p>");
			  	result.append("</td>");
			  	result.append("<td style='width:33.34%;height:16px;'>");
			  	result.append("<p><strong>OT Unit 1.5 :&nbsp;</strong>"+item.getOtUnit2()+"</p>");
			  	result.append("</td>");
			  	result.append("<td style='width:33.32%;height:16px;'>");
			  	result.append("	<p><strong>OT Unit 2 :</strong> "+item.getOtUnit3()+"</p>");
			  	result.append("</td>");
			  	result.append("</tr>");
			  	}
			  	
			  	String serviceName="";
			  	if(item.getService()!=null)
			  	{
			  		serviceName=item.getService().getEnDescription();
			  		if(serviceName.equalsIgnoreCase("Select"))
			  		{
			  			serviceName="";
			  		}
			  	}
			  	else
			  	{
			  		serviceName="";
			  	}
			  	
			  	String customerJob="";
			  	if(item.getCustomerJob()!=null)
			  	{
			  		customerJob=item.getCustomerJob().getName();
			  		if(customerJob.equalsIgnoreCase("Select"))
			  		{
			  			customerJob="";
			  		}
			  	}
			  	else
			  	{
			  		customerJob="";
			  	}
			  	
			  	
			  	result.append("<tr>");
			  	result.append("<td style='width:33.34%;height:20px;'>");
			  	result.append("<p>	<strong>Customer Job :&nbsp;</strong>"+customerJob+"</p>");
			  	result.append("</td>");
			  	result.append("<td style='width:33.34%;height:20px;'>");
			  	result.append("<p>&nbsp;</p>");
			  	result.append("<p><strong>Project :&nbsp;</strong>"+item.getProject().getProjectName()+"</p>");
			  	result.append("<p>&nbsp;</p>");
			  	result.append("</td>");
			  	result.append("<td style='width:33.32%;height:20px;'>");
			  	result.append("<p><strong>Service Type :&nbsp;</strong>"+serviceName+"</p>");
			  	result.append("</td>");
			  	result.append("</tr>");
			  	
			  
			  	if(item!=null && item.getSelectedTask()!=null && item.getSelectedTaskStatus()!=null)
			  	{
			  	
				result.append("<tr> ");
				result.append("<td style='width:33.34%;height:20px;'>");
				result.append("	<p>&nbsp;</p>");
				result.append("<p><strong>Selected Task Name/Number :&nbsp;</strong>"+item.getSelectedTask().getTaskName()+" : "+ item.getSelectedTask().getTaskNumber()+"</p>");
				result.append("<p>&nbsp;</p>");
				result.append("</td>");
				result.append("<td style='width:33.34%;height:20px;'>");
				result.append("	<p>	<strong>Task Status :&nbsp;</strong>"+item.getSelectedTaskStatus().getEnDescription()+"</p>");
				result.append("</td>");
				result.append("<td style='width:33.32%;height:20px;'>");
				result.append("	<p>&nbsp;</p>");
				result.append("</td>");
				result.append("</tr>");
			  	}
			  	
			  	result.append("<tr> ");
			  	result.append("<td colspan='3' style='width:100.0%;'>");
			  	result.append("	<p>	&nbsp;</p>");
			  	result.append("<p><strong>Notes :&nbsp;</strong>"+item.getNotes()+".</p>");
			  	result.append("<p>	&nbsp;</p>");
			  	result.append("<p>	&nbsp;</p>");
			  	result.append("</td>");
			  	result.append("</tr>");
			  	result.append("</tbody>");
			  	result.append("</table>");
			  	
				 }
			  				
				 }
			  	 
			 	result.append("	<p> ");
			  	result.append("<strong>Tomorrow&#39;s Plan</strong> :&nbsp;"+tempTomrrowsPlanForEmail+"</p> ");
			  	result.append("<p> ");
			  	result.append("<strong>Total Shift (Hours/Days)</strong> &nbsp;:&nbsp;"+totalShiftHours+"</p> ");
			  	result.append("<p> ");
			  	result.append("<strong>Total Working (Hours/Days)</strong> :&nbsp;"+Double.parseDouble(new DecimalFormat("#.#").format(totalHours))+"</p>");
			  	result.append("<p> ");
			  	result.append("<strong>Total Not Working (Hours/Days) </strong>&nbsp;:&nbsp;"+Double.parseDouble(new DecimalFormat("#.#").format(totalNotworkingHours))+"</p>");
			  	result.append("<p> ");
			  	result.append("<strong>Total OT(Over Time)</strong> &nbsp;:&nbsp;"+totalOvertimeHours+"</p> ");
			  	result.append("<p>");
			  	result.append("&nbsp;</p> ");
			  	
			  	result.append("<p><strong>"+selectedCompEmployee.getFullName()+ "</strong></p>");
			  	result.append("<p><strong>"+selectedCompEmployee.getPosition()+"</strong></p>");
			  	result.append("<p><strong>"+selectedCompany.getEnCompanyName()+"</strong></p>");
			 		  		
		  	}
		  	
		 	String messageBody=result.toString();	

			String[] cc ={null};
			logger.info("begin send email to" + to.toString());
			
			mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,null,true,"Timesheet","");
			
			//to not send twice
			if(withNotification)
			{
			//sendGCMNotification(selectedCompEmployee.getFullName(), selectedCompEmployee.getPosition(), selectedCompEmployee.getDepartment(),
			//		leaveDescription + " Created.", tempTomrrowsPlanForEmail);
			}
			
		   }
			//mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, null);
		}
		catch (Exception e) 
			{
				logger.error("ERROR in DailyTimeSheetViewModel ----> DailyTimeSheetViewModel", e);			
				 logger.info("error at DailyTimeSheetViewModel----> sendEmail" + e);
			}
	}
	
	private void sendGCMNotification(String employeeName,String position,String department,String descreption,String tomorrowPlan)
	{
		try
		{
			List<String> lstGCMTokens=data.getGCMUsersList("timesheet");
			if(lstGCMTokens.size()==0)
				return;
			
			POST2GCM p=new POST2GCM();			
			ContentGCM c=new ContentGCM();
			for (String token : lstGCMTokens) 
			{
				c.addRegId(token);
				logger.info("GCM: " + token);
			}
			//c.addRegId("APA91bGqDLf1Rf_m8vQfPIPpx882548epFxgPkpqa-QTOaC3Ik5ybXhJ3ktIacq93UC9oLHCjR9ykrKI-arEoM0C-or3EyOu4sHk-EB3y7pIWBk-_6yjnpc");
			c.setEmployeeName(employeeName);
			c.setPosition(position);
			c.setDepartment(department);
			c.setDescreption(descreption);
			c.setTomorrowPlan(tomorrowPlan);
			
			c.createData("ERP", "Timesheet Submitted by " + employeeName);				
			p.post("AIzaSyCBdzcYFM71qpfPvuGn2VArWrYK3wFZRHU",c);
			
			logger.info("GCM send it...");
		}
		catch (Exception e) 
		{
			logger.error("ERROR in DailyTimeSheetViewModel ----> sendGCMNotification", e);			
			logger.info("error at DailyTimeSheetViewModel----> sendGCMNotification" + e);
		}
	}

	public List<QbListsModel> getLstCustomerJob() {
		return lstCustomerJob;
	}

	public void setLstCustomerJob(List<QbListsModel> lstCustomerJob) {
		this.lstCustomerJob = lstCustomerJob;
	}
	
	@Command
	public void clickButtonSendEmail()
	{
		sendEmailFlag=true;
		saveCommand();
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

	public boolean isCheckAllStatus() {
		return checkAllStatus;
	}

	public void setCheckAllStatus(boolean checkAllStatus) {
		this.checkAllStatus = checkAllStatus;
	}

	public boolean isCheckAllCalculate() {
		return checkAllCalculate;
	}

	public void setCheckAllCalculate(boolean checkAllCalculate) {
		this.checkAllCalculate = checkAllCalculate;
	}

	public List<HRListValuesModel> getLstTaskStatus() {
		return lstTaskStatus;
	}

	public void setLstTaskStatus(List<HRListValuesModel> lstTaskStatus) {
		this.lstTaskStatus = lstTaskStatus;
	}

	public List<TasksModel> getLstCustomerTaks() {
		return lstCustomerTaks;
	}

	public void setLstCustomerTaks(List<TasksModel> lstCustomerTaks) {
		this.lstCustomerTaks = lstCustomerTaks;
	}
	
	
	
}
