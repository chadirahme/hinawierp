package timesheet;

import model.ProjectReportModel;
import home.QuotationAttachmentModel;
import hr.HRQueries;
import hr.model.CompanyModel;
import hr.model.ContactModel;
import hr.model.LeaveModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import admin.TasksModel;
import company.CompanyData;
import setup.users.WebusersModel;
import layout.MenuModel;
import model.CompSetupModel;
import model.CompanyDBModel;
import model.CompanyShiftModel;
import model.EmployeeModel;
import model.EmployeeShiftModel;
import model.HRListValuesModel;
import model.LocationModel;
import model.OverTimeModel;
import model.ProjectModel;
import model.QbListsModel;
import model.SalaryMasterModel;
import model.ShiftModel;
import model.TimeSheetDataModel;
import model.TimeSheetGridModel;
import model.TransferModel;
import db.DBHandler;
import db.SQLDBHandler;

public class TimeSheetData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	//LiveDBHandler db=new LiveDBHandler("");
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	int parentID=2;
	
	
	//changes  by iqbal to save user it in task details  
		CompanyData companyData=new CompanyData();
		private int webUserID=0;
		private int supervisorID=0;
		private int employeeKey=0;
		@SuppressWarnings("unused")
		private int UserId=0;
		private boolean adminUser;
		@SuppressWarnings("unused")
		private List<WebusersModel> lstUsers=new ArrayList<WebusersModel>();
	
	public TimeSheetData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HRQueries query=new HRQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
				  
				  if(dbUser!=null)
					{
						adminUser=dbUser.getFirstname().equals("admin");
						lstUsers=companyData.getUsersList(dbUser.getCompanyid());
					
						if(adminUser)
						{
							webUserID=0;
						}
						else
						{
							webUserID=dbUser.getUserid();
						}
					}
					
					supervisorID=dbUser.getSupervisor();//logged in as supervisor
					employeeKey=dbUser.getEmployeeKey();
					if(employeeKey>0)
						supervisorID=employeeKey;//logged in as employee
					
					if(supervisorID>0)
						webUserID=supervisorID;
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---Init-->" , ex);
		}
	}
	public List<MenuModel> getTimeSheetRoles(int companyRoleId)
	{
		List<MenuModel> lstRoles=new ArrayList<MenuModel>();
		try
		{
			CompanyData compData=new CompanyData();		
			lstRoles=compData.getRoleCredentials(companyRoleId, parentID);
			 
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getTimeSheetRoles-->" , ex);
		}
		return lstRoles;
	}
	
	public List<MenuModel> getRolesPermessions(int companyRoleId,int menuParentId)
	{
		List<MenuModel> lstRoles=new ArrayList<MenuModel>();
		try
		{
			CompanyData compData=new CompanyData();		
			lstRoles=compData.getRoleCredentials(companyRoleId, menuParentId);
			 
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getRolesPermessions-->" , ex);
		}
		return lstRoles;
	}
	
	
	public List<CompanyModel> getCompanyList(int userID)
	{
		 List<CompanyModel> lst=new ArrayList<CompanyModel>();
		 
		
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getCompanyListQuery(userID));
				while(rs.next())
				{
					CompanyModel obj=new CompanyModel();
					obj.setCompKey(rs.getInt("COMP_KEY"));					
					obj.setEnCompanyName(rs.getString("COMP_NAME"));
					obj.setArCompanyName(rs.getString("COMP_NAME_AR"));					
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getCompanyList-->" , ex);
			}
		 return lst;
	}
	
	
	public List<HRListValuesModel> getHRListValues(int fieldId,String type)
	{
		 List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		 
		 
			HRListValuesModel obj=new HRListValuesModel();
			if(!type.equals(""))
			{
			obj.setListId(0);					
			obj.setEnDescription(type);
			lst.add(obj);
			}
			
		   
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getHRListValuesQuery(fieldId));
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("ID"));					
					obj.setEnDescription(rs.getString("DESCRIPTION"));
					obj.setArDescription(rs.getString("ARABIC"));
					obj.setSubId(rs.getInt("SUB_ID"));
					obj.setFieldId(rs.getInt("FIELD_ID"));
					obj.setFieldName(rs.getString("FIELD_NAME"));
					obj.setPriorityId(rs.getInt("PRIORITY_ID"));
					obj.setRequired(rs.getString("REQUIRED"));
					obj.setQbListId(rs.getString("QBListID"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	
	public CompSetupModel getCompanySetup(int compkey)
	{
		CompSetupModel obj=new CompSetupModel();
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{						
				rs=db.executeNonQuery(query.GetCompanySetupQuery(compkey));
				while(rs.next())
				{										
					obj.setPayrollMonth(rs.getInt("MONTH_PAYROLL"));
					obj.setPayrollYear(rs.getInt("YEAR_PAYROLL"));		
					obj.setIncludeholidayUnit(rs.getString("INCLUDEHOLIDAY_UNIT")==null?"N":rs.getString("INCLUDEHOLIDAY_UNIT"));
					obj.setTimesheetOTChange(rs.getString("TIMESHEET_OTCHANGE"));
					obj.setTimesheetTimeAuto(rs.getString("TIMESHEET_TIMEAUTO"));
					obj.setTimesheetChangeTime(rs.getString("TIMESHEET_CHANGETIME").equals("Y")?true:false);
					obj.setTimesheetCalculateExtraHours(rs.getString("TIMESHEET_CALCEXTRAHOURS").equals("Y")?true:false);
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getCompanySetup-->" , ex);
			}		
		
		return obj;
	}
	
	public List<ProjectModel> getProjectListBySupervisorID(int superviorId)
	{
		List<ProjectModel> lst=new ArrayList<ProjectModel>();
		ProjectModel obj=new ProjectModel();
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{
				obj.setProjectKey(0);
				obj.setProjectName("All");
				lst.add(obj);
				rs=db.executeNonQuery(query.getProjectListBySupervisorID(superviorId));
				while(rs.next())
				{
					 obj=new ProjectModel();
					obj.setProjectKey(rs.getInt("Project_Key"));
					obj.setProjectCode(rs.getString("Project_Code"));
					obj.setProjectName(rs.getString("Project_Name"));
					obj.setProjectArName(rs.getString("Project_NameAR"));
					obj.setQbListId(rs.getString("QBLISTID"));
					obj.setStatusId(rs.getInt("Status_id"));
					//obj.setIsActive(rs.getString("IsActive"));
					//obj.setArActive(rs.getString("ArActive"));
					//obj.setActiveStatus(rs.getString("IsActive").equalsIgnoreCase("active")?true:false);
					obj.setEnCodeName(obj.getProjectCode() + " "  + obj.getProjectName());
					obj.setArCodeName(obj.getProjectCode() + " " + obj.getProjectArName());
					lst.add(obj);
				}				
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getProjectListBySupervisorID-->" , ex);
			}		
		
		return lst;
	}
	public Date getProjectLastModified()
	{
		Date lastModified=null;
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getProjectLastModifiedQuery());
			while(rs.next())
			{
				lastModified=rs.getDate("LastModified");						
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getProjectLastModified-->" , ex);
		}
		return lastModified;
	}
	public List<ProjectModel> getProjectList(int compkey,String type,boolean isActive,int supervisorID)
	{
		List<ProjectModel> lst=new ArrayList<ProjectModel>();
		ProjectModel obj=new ProjectModel();
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{
				if(type.equals("Transfer"))
				{
					obj.setProjectKey(0);
					obj.setProjectName("Select");
					lst.add(obj);
				}
				
				else if(type.equals("All"))
				{
					obj.setProjectKey(0);
					obj.setProjectName("All");
					lst.add(obj);
				}
				
				rs=db.executeNonQuery(query.GetProjectQuery(compkey,isActive,supervisorID));
				while(rs.next())
				{
					 obj=new ProjectModel();
					obj.setProjectKey(rs.getInt("Project_Key"));
					obj.setProjectCode(rs.getString("Project_Code"));
					obj.setProjectName(rs.getString("Project_Name"));
					obj.setProjectArName(rs.getString("Project_NameAR"));
					obj.setQbListId(rs.getString("QBLISTID"));
					obj.setStatusId(rs.getInt("Status_id"));
					obj.setIsActive(rs.getString("IsActive"));
					obj.setArActive(rs.getString("ArActive"));
					obj.setStartDate(rs.getDate("start_date"));
					obj.setEndDate(rs.getDate("end_date"));
					obj.setArActive(rs.getString("ArActive"));
					obj.setActiveStatus(rs.getString("IsActive").equalsIgnoreCase("active")?true:false);
					obj.setEnCodeName(obj.getProjectCode() + " "  + obj.getProjectName());
					obj.setArCodeName(obj.getProjectCode() + " " + obj.getProjectArName());
					lst.add(obj);
				}				
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getProjectList-->" , ex);
			}		
		
		return lst;
	}
	
	public List<QbListsModel> getCustomerJob()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();			
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try
		{
			QbListsModel obj=new QbListsModel();
			obj.setRecNo(0);
			obj.setFullName("Select");
			obj.setName("Select");
			obj.setListID("");
			obj.setSubLevel(0);
			lst.add(obj);
			
			rs=db.executeNonQuery(query.getCustomerJobQuery());
			while(rs.next())
			{
				obj=new QbListsModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setFullName(rs.getString("FullName"));
				obj.setName(rs.getString("Name"));
				obj.setListType(rs.getString("ListType"));
				obj.setSubLevel(rs.getInt("subLevel"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getCustomerJob-->" , ex);
		}	
		return lst;
	}
	
	
	public List<SalaryMasterModel> GetMasterSalarySheet(int salaryMonth,int salaryYear)
	{
		List<SalaryMasterModel> lst=new ArrayList<SalaryMasterModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{						
				rs=db.executeNonQuery(query.GetSalarySheetQuery(salaryMonth, salaryYear));
				while(rs.next())
				{					
					SalaryMasterModel obj=new SalaryMasterModel();
					obj.setEmpKey(rs.getInt("EMP_KEY"));
					obj.setSalaryStatus(rs.getString("SALARY_STATUS"));
					lst.add(obj);									
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetMasterSalarySheet-->" , ex);
			}		
		
		return lst;
		
	}
	private List<Integer> GetDistinctEmployeeInDailyTS(Date fromDate,Date toDate,int compKey)
	{
		List<Integer> lst=new ArrayList<Integer>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{	
				//get employee for supervisor
				
				
				rs=db.executeNonQuery(query.GetEmployeeInDailyTSQuery(fromDate, toDate, compKey));
				while(rs.next())
				{					
					lst.add(rs.getInt("LABOUR_KEY"));					
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetEmployeeInDailyTS-->" , ex);
			}		
		
		return lst;
	}
	
	public List<EmployeeModel> GetEmployeeListInDailyTS(Date fromDate,Date toDate,int compKey,int supervisorID)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{	
				List<Integer> lstSupKeys=new ArrayList<Integer>();
				List<EmployeeModel> lstSuperVisorEmployees=new ArrayList<EmployeeModel>();
				if(supervisorID>0)
				{
				lstSuperVisorEmployees=getEmployeeList(compKey,"T",supervisorID);			
				for (EmployeeModel emp : lstSuperVisorEmployees) 
				{
					lstSupKeys.add(emp.getEmployeeKey());
				}
			   }
				
				String empKeys="";
				List<Integer> lstEmpKeys=GetDistinctEmployeeInDailyTS(fromDate, toDate, compKey);
				for (Integer empKey : lstEmpKeys) 
				{
					if(supervisorID>0 && !lstSupKeys.contains(empKey))
						continue;
					
					if(empKeys.equals(""))
					empKeys+=String.valueOf(empKey);
					else
					empKeys+=","+String.valueOf(empKey);
				}
				if(!empKeys.equals(""))
				{
				rs=db.executeNonQuery(query.GetEmployeeListInDailyTSQuery(empKeys));
				while(rs.next())
				{					
					EmployeeModel obj=new EmployeeModel();
				/*	obj.setEmployeeKey(rs.getInt("EMP_KEY"));
					obj.setLocationId(rs.getInt("LOC_ID"));
					obj.setPositionID(rs.getInt("POS_ID"));
					lst.add(obj);	*/	
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo"));
					obj.setLocationId(rs.getInt("LocationID"));
					obj.setPositionID(rs.getInt("PositionId"));
					obj.setCompanyID(rs.getInt("compkey"));	
					obj.setFullName(rs.getString("fullName"));
					obj.setPosition(rs.getString("Position"));
					obj.setStatus(rs.getString("status"));
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));
					lst.add(obj);			
				}
			   }
				
				
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetEmployeeListInDailyTS-->" , ex);
			}		
		
		return lst;
	}
	
	public List<EmployeeModel> GetEmployeeListFromFilter(String empKeys)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{	
				
				if(!empKeys.equals(""))
				{
				rs=db.executeNonQuery(query.GetEmployeeListInDailyTSQuery(empKeys));
				while(rs.next())
				{					
					EmployeeModel obj=new EmployeeModel();
					/*
					obj.setEmployeeKey(rs.getInt("EMP_KEY"));
					obj.setLocationId(rs.getInt("LOC_ID"));
					obj.setPositionID(rs.getInt("POS_ID"));
					obj.setCompanyID(rs.getInt("Comp_Key"));	
					obj.setFullName(rs.getString("English_Full"));
					*/
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo"));
					obj.setLocationId(rs.getInt("LocationID"));
					obj.setPositionID(rs.getInt("PositionId"));
					obj.setCompanyID(rs.getInt("compkey"));	
					obj.setFullName(rs.getString("fullName"));
					obj.setPosition(rs.getString("Position"));
					obj.setStatus(rs.getString("status"));
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));
					lst.add(obj);					
				}
			  }				
				
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetEmployeeListFromFilter-->" , ex);
			}		
		
		return lst;
	}
	public void deletetmpTSDates()
	{
		try
		{
			TimeSheetQueries query=new TimeSheetQueries();
			
			db.executeUpdateQuery(query.deleteTmpTsDatesQuery());
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---deletetmpTSDates-->" , ex);
		}	
	}
	public void addtmpTSDates(Date fromDate,Date toDate)
	{
		try
		{	
			StringBuffer Allquery=new StringBuffer();
			TimeSheetQueries query=new TimeSheetQueries();
			int dayFrom=0;			 			 
			 int dayTo=0;
			  int diffDay=0;
			  
			  Calendar c = Calendar.getInstance();	
			  c.setTime(fromDate);
			  dayFrom=c.get(Calendar.DAY_OF_MONTH);			
			  c.setTime(toDate);				
			  dayTo=c.get(Calendar.DAY_OF_MONTH);			  
			  diffDay=dayTo-dayFrom +1;				
			  
			  for (int i = 0; i < diffDay; i++)
			  {
				  c.setTime(fromDate);
				  c.add(Calendar.DAY_OF_MONTH, i);
				  Allquery.append(query.addtmpTSDatesQuery(c.getTime(), c.get(Calendar.DAY_OF_WEEK)));				  
			  }
			  
			db.executeUpdateQuery(Allquery.toString());//query.addtmpTSDatesQuery(tsDate, dayNo));
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---addtmpTSDates-->" , ex);
		}	
	}
	public List<TimeSheetDataModel> GetDataFromSetup(int compKey,int empKey,Date fromDate,Date toDate)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		DateFormat hdf = new SimpleDateFormat("hh:mm a");
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{					
				rs=db.executeNonQuery(query.GetDataFromSetupQuery(compKey, empKey,fromDate,toDate));
				while(rs.next())
				{								
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setMainShift(true);
					obj.setNoOfshifts(rs.getInt("no_of_shifts"));
					obj.setEmpKey(rs.getInt("EMP_KEY"));
					obj.setEmpNo(rs.getString("EMP_NO"));
					obj.setTsDate(rs.getDate("TS_DATE"));
					obj.setDayNo(rs.getInt("DAY_NO"));
					obj.setEnFullName(rs.getString("ENGLISH_FULL"));
					obj.setOffDay(rs.getString("OFFDAY"));
					obj.setShiftKey(rs.getDouble("SHIFT_KEY"));
					obj.setUnitId(rs.getInt("UNIT_ID"));
					obj.setCalcFlag(rs.getString("CALC_FLAG"));
					obj.setTiming(rs.getString("TIMING_FLAG"));
					obj.setUnitNO(rs.getDouble("UNIT_NOS"));
					obj.setNormalUnitNO(rs.getDouble("NORMAL_UNITS"));
					//obj.setRecNo(rs.getInt("SHIFT_REC_NO"));
					obj.setShiftRecNo(rs.getInt("SHIFT_REC_NO"));
					obj.setHolidayDesc(rs.getString("HolidayDesc") ==null?"":rs.getString("HolidayDesc"));
					if(!obj.getHolidayDesc().equals(""))
						obj.setOffDay("Y");
					
					if(obj.getTiming().equals("Y"))
					{
						if(rs.getDate("FROM_TIME")!=null && rs.getDate("TO_TIME")!=null)
						{
						obj.setFromTime(hdf.format(rs.getDate("FROM_TIME")));
						obj.setToTime(hdf.format(rs.getDate("TO_TIME")));
						
						obj.setTsFromTime(rs.getDate("FROM_TIME"));
						obj.setTstoTime(rs.getDate("TO_TIME"));
						}
					}					
					lst.add(obj);										
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetDataFromSetup-->" , ex);
			}		
		
		return lst;
	}
	
	public List<TimeSheetDataModel> GetDataFromTimeSheet(int compkey,int empKey,Date fromDate,Date toDate)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		DateFormat hdf = new SimpleDateFormat("hh:mm a");
			try 
			{					
				rs=db.executeNonQuery(query.GetDataFromTimeSheetQuery(compkey, empKey, fromDate, toDate));
				while(rs.next())
				{								
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setNoOfshifts(rs.getInt("no_of_shifts"));
					obj.setShiftRecNo(rs.getInt("SHIFT_REC_NO"));
					obj.setRecNo(rs.getInt("REC_NO"));
					obj.setLineNO(rs.getInt("LINE_NO"));					
					obj.setEmpKey(rs.getInt("EMP_KEY"));					
					obj.setTsDate(rs.getDate("TS_DATE"));		
					obj.setOffDay(rs.getString("OFFDAY"));
					obj.setStatus(rs.getString("STATUS"));
					obj.setShiftKey(rs.getDouble("SHIFT_KEY"));
					obj.setUnitId(rs.getInt("UNIT_ID"));
					obj.setUnitName(rs.getString("UNIT_NAME")==null?"Hours":rs.getString("UNIT_NAME"));//add this for new line added left join
					obj.setUnitNO(rs.getDouble("UNIT_NOS"));
					obj.setNormalUnitNO(rs.getDouble("NORMAL_UNITS"));
					obj.setTotalUnitNo(rs.getDouble("TOTAL_UNIT_NO"));
					obj.setTotalNormalUnitNo(rs.getDouble("TOTAL_NORMAL_HRS"));
					obj.setTiming(rs.getString("TIMING")==null?"Y":rs.getString("TIMING"));//add this for new line added left join
					obj.setCalcFlag(rs.getString("CALC_FLAG"));
					obj.setLeaveFlag(rs.getString("LEAVE_FLAG"));
					obj.setHolidayDesc(rs.getString("HOLIDAYDESC")==null?"":rs.getString("HOLIDAYDESC"));
					obj.setNotes(rs.getString("NOTES"));
					obj.setServiceId(rs.getInt("SERVICE_ID"));
					obj.setProjectkey(rs.getInt("PROJECT_KEY"));
					obj.setApproved(rs.getInt("TS_STATUS"));
					obj.setTotalOTUnits(rs.getDouble("OT_NOS"));
					obj.setCustomerJobRefKey(rs.getInt("CustomerRefKey"));
					obj.setTomorrowPlan(rs.getString("TomorrowsPlan"));
					obj.setAttachPath(rs.getString("AttachmentPath")==null?"":rs.getString("AttachmentPath"));					
					
					if(obj.getTiming().equals("Y"))
					{
						if(rs.getDate("FROM_TIME")!=null && rs.getDate("TO_TIME")!=null)
						{
						obj.setFromTime(hdf.format(rs.getDate("FROM_TIME")));
						obj.setToTime(hdf.format(rs.getDate("TO_TIME")));
						
						obj.setTsFromTime(rs.getDate("FROM_TIME"));
						obj.setTstoTime(rs.getDate("TO_TIME"));
						}
					}
					lst.add(obj);			
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetDataFromTimeSheet-->" , ex);
			}		
		
		return lst;
	}
	
	public List<TimeSheetDataModel> GetDataFromOverTime(int empKey,Date fromDate,Date toDate)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{					
				rs=db.executeNonQuery(query.GetOverTimeSheetQuery(empKey, fromDate, toDate));
				while(rs.next())
				{								
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setRecNo(rs.getInt("Rec_NO"));
					obj.setLineNO(rs.getInt("LINE_NO"));
					obj.setEmpKey(rs.getInt("EMP_KEY"));					
					obj.setTsDate(rs.getDate("TS_DATE"));	
					obj.setCalculation(rs.getDouble("CALCULATION"));
					obj.setUnits(rs.getDouble("UNITS"));
					obj.setAmount(rs.getDouble("AMOUNT"));
					lst.add(obj);	
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetDataFromOverTime-->" , ex);
			}		
		
		return lst;
	}
	
	public List<TimeSheetDataModel> getLastTimeSheetCreated(int compKey)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{					
				rs=db.executeNonQuery(query.getLastTimeSheetCreatedQuery(compKey));
				while(rs.next())
				{								
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setEmpKey(rs.getInt("LABOUR_KEY"));					
					obj.setTsDate(rs.getDate("TS_DATE"));
					obj.setStatus(rs.getString("TS_STATUS"));				
					lst.add(obj);	
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getLastTimeSheetCreated-->" , ex);
			}		
		
		return lst;
	}
	
	public List<TimeSheetDataModel> getLastTimeSheetCreatedByUser(int compKey,int supervisorID)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{					
				rs=db.executeNonQuery(query.getLastTimeSheetCreatedByUserQuery(compKey,supervisorID));
				while(rs.next())
				{								
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setEmpKey(rs.getInt("LABOUR_KEY"));					
					obj.setTsDate(rs.getDate("TS_DATE"));
					obj.setStatus(rs.getString("TS_STATUS"));
					obj.setSupervisorId(rs.getInt("SUPERVISORID"));
					obj.setSupervisorName(rs.getString("english_full"));
					lst.add(obj);	
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getLastTimeSheetCreatedByUser-->" , ex);
			}		
		
		return lst;
	}
	
	//Filter Employee
	@SuppressWarnings("unused")
	public List<EmployeeModel> getEmployeeList(int compkey,String type,int supervisorId)
	{
		
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForInactiveStatus=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForActiveStatus=new ArrayList<EmployeeModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		HRQueries queryHR=new HRQueries();
		HRQueries queryHr=new HRQueries();
		ResultSet rs = null;
		try 
		{
		Date createDateNew;
		Calendar c = Calendar.getInstance();
		createDateNew=df.parse(sdf.format(c.getTime()));
		ResultSet newRsActive = null;
		ResultSet newRs = null;
		String inactiveSatus="";
		newRs=db.executeNonQuery(queryHR.getEmpStatusDescriptionforInactive(1));
		newRsActive=db.executeNonQuery(queryHR.getEmpStatusDescriptionForActive(1,createDateNew));
		while(newRsActive.next())
		{
			EmployeeModel obj=new EmployeeModel();
			//obj.setDateOfBirth(dateOfBirth)
			obj.setEmployeeKey(newRsActive.getInt("emp_key"));
			obj.setJoiningDate(newRsActive.getDate("from_date"));
			obj.setEmployeementDate(newRsActive.getDate("to_date"));
			obj.setStatusDescription(newRsActive.getString("leaveDesc"));
			lstOfEmployeesForActiveStatus.add(obj);
				
		}
		
		while(newRs.next())
		{
			EmployeeModel obj=new EmployeeModel();
			obj.setEmployeeKey(newRs.getInt("emp_key"));
			obj.setStatusDescription(newRs.getString("Activity"));
			lstOfEmployeesForInactiveStatus.add(obj);
		}
		
			
			rs=db.executeNonQuery(query.searchEmployeeQuery(compkey,type,supervisorId));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setEmployeeNo(rs.getString("EmployeeNo")==null?"":rs.getString("EmployeeNo"));
				obj.setFullName(rs.getString("FullName")==null?"":rs.getString("FullName"));
				obj.setArabicName(rs.getString("ArabicName")==null?"":rs.getString("ArabicName"));
				obj.setDepartmentID(rs.getInt("DepartmentID"));
				obj.setDepartment(rs.getString("Department")==null?"":rs.getString("Department"));
				obj.setArabicDepartment(rs.getString("ArabicDepartment")==null?"":rs.getString("ArabicDepartment"));
				obj.setPositionID(rs.getInt("PositionID"));
				obj.setPosition(rs.getString("Position")==null?"":rs.getString("Position"));
				obj.setArabicPosition(rs.getString("ArabicPosition"));
				obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"":rs.getString("Country"));
				obj.setCompanyName(rs.getString("CompanyName")==null?"":rs.getString("CompanyName"));
				obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				//obj.setEnFirstName(rs.getString("ENGLISH_FIRST"));
				//obj.setEnMiddleName(rs.getString("ENGLISH_MIDDLE"));
				//obj.setEnLastName(rs.getString("ENGLISH_LAST"));
				//obj.setArFirstName(rs.getString("ARABIC_FIRST"));
				//obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
				//obj.setArLastName(rs.getString("ARABIC_LAST"));
				obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
				obj.setProjectKey(rs.getInt("locationId"));
				obj.setEmployeementDate(rs.getDate("EmployeementDate"));
				obj.setGender(rs.getString("Sex")==null?"":rs.getString("Sex"));
				obj.setMarital(rs.getString("Marital")==null?"":rs.getString("Marital"));
				//obj.setCheckedEmployee(true);
				
				obj.setWorkGroupId(rs.getInt("workersgroupid"));
			    obj.setSupervisorId(rs.getInt("supervisorid"));
			    obj.setSupervisorName(rs.getString("supervisorname")==null?"":rs.getString("supervisorname"));
			    obj.setSupervisorNameAR(rs.getString("arabicsupervisorname")==null?"":rs.getString("arabicsupervisorname"));
			    obj.setWorkGroupName(rs.getString("workergroupname")==null?"":rs.getString("workergroupname"));
			    obj.setWorkGroupNameAR(rs.getString("arabicworkergroupname")==null?"":rs.getString("arabicworkergroupname"));
			    obj.setEmployeeStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				obj.setSuper_supervisorId(rs.getInt("SuperSupervisorId"));
				obj.setSuper_supervisorName(rs.getString("SuperAdminName")==null?"":rs.getString("SuperAdminName"));
				obj.setSuper_supervisorNameAR(rs.getString("ArabicSuperAdminName")==null?"":rs.getString("ArabicSuperAdminName"));

				String active=rs.getString("Active");
				String terminate=rs.getString("Terminate");
				obj.setStatusDescription("");//add as default for filter used
				
				if(active.equals("I"))
				{
					if(!terminate.equals("Y"))
					{
						obj.setStatus("Inactive");
						for(EmployeeModel model:lstOfEmployeesForInactiveStatus)
						{
								
							if(model.getEmployeeKey()==obj.getEmployeeKey())
							{
								if(model.getStatusDescription()!=null)
								{
									if(model.getStatusDescription().equalsIgnoreCase("A"))
									{
										obj.setStatusDescription("Absconded Worker");
									}
									else if(model.getStatusDescription().equalsIgnoreCase("E"))
									{
										obj.setStatusDescription("End of Service");
									}
									else
									{
										obj.setStatusDescription("Salary Stoped");
									}
								}
								else
								{
									obj.setStatusDescription("Status Changed");
								}
								break;
							}
							else
							{
									obj.setStatusDescription("Status Changed");
							}
						
						}
						
						
					}
					else
					{
						obj.setStatus("Inactive(EOS)");
						obj.setStatusDescription("End of Service");
					}
				}
				else
				{
					obj.setStatus("Active");
					for(EmployeeModel model:lstOfEmployeesForActiveStatus)
					{
						if(model.getEmployeeKey()==obj.getEmployeeKey())
						{
							Date fromDateNew=model.getJoiningDate();
							Date ToDateNew=model.getEmployeementDate();
							String leaveType=model.getStatusDescription();
							if((fromDateNew!=null && ToDateNew!=null))
							obj.setStatusDescription(""+leaveType+" : from "+new SimpleDateFormat("dd-MM-yyyy").format(fromDateNew)+" to "+new SimpleDateFormat("dd-MM-yyyy").format(ToDateNew)+"");
							break;
						}
						else
						{
							obj.setStatusDescription("On Work");
						}
					}
				}
				
				lstEmployees.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getEmployeeList-->" , ex);
		}
		return lstEmployees;
	}
	
	public List<EmployeeModel> getAutoFillEmployeeList(int compkey,String type,List<Integer> lstEmpKeys,int supervisorId)
	{
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			
			rs=db.executeNonQuery(query.searchEmployeeQuery(compkey,type,supervisorId));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setEmployeeNo(rs.getString("EmployeeNo"));
				obj.setFullName(rs.getString("FullName"));
				if(lstEmpKeys.contains(obj.getEmployeeKey()))
				lstEmployees.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getAutoFillEmployeeList-->" , ex);
		}
		return lstEmployees;
	}
	
	public CompanyShiftModel getEmployeeShiftKey(int empKey,Date fromDate,Date toDate,int compKey)
	{
		CompanyShiftModel obj=new CompanyShiftModel();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getEmployeeShiftQuery(empKey, fromDate, toDate,compKey));
			while(rs.next())
			{
				obj.setShiftKey(rs.getInt("SHIFT_KEY"));
				obj.setTimingFlag(rs.getString("TIMING_FLAG"));
				obj.setUnitkey(rs.getInt("Unit_Key"));
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getEmployeeShiftKey-->" , ex);
		}
		
		return obj;
	}
	
	public boolean checkEmployeeSalarySheet(int empKey,int month,int year)
	{
		boolean isSalaryGenerate=false;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkEmployeeSalarySheetQuery(empKey, month, year));
			while(rs.next())
			{
				isSalaryGenerate=true;
				return isSalaryGenerate;
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---checkEmployeeSalarySheet-->" , ex);
		}
		
		return isSalaryGenerate;
	}
	
	public boolean checkEmployeeSalarySheetApproved(int empKey,int month,int year)
	{
		boolean isSalaryGenerate=false;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkEmployeeSalarySheetApproved(empKey, month, year));
			while(rs.next())
			{
				isSalaryGenerate=true;
				return isSalaryGenerate;
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---checkEmployeeSalarySheetApproved-->" , ex);
		}
		
		return isSalaryGenerate;
	}
	public boolean checkForLoanEmployeeSalarySheetApproved(int empKey,int fromMonth,int toMonth,int fromYear,int toYear)
	{
		boolean isSalaryGenerate=false;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkForLoanEmployeeSalarySheetApprovedQuery(empKey, fromMonth, toMonth, fromYear, toYear));
			while(rs.next())
			{
				isSalaryGenerate=true;
				return isSalaryGenerate;
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---checkForLoanEmployeeSalarySheetApproved-->" , ex);
		}
		
		return isSalaryGenerate;
	}
	public int checkIfTimeSheetApproved(int empKey,int month,int year)
	{
		int count=0;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfTimeSheetApprovedQuery(empKey, month, year));
			while(rs.next())
			{
				count=rs.getInt("cnt");				
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---checkIfTimeSheetApproved-->" , ex);
		}
		
		return count;
	}
	
	
	@SuppressWarnings("unused")
	public List<LeaveModel> checkIfOnlineLeaveTaken(int empKey,Date fromDate,Date toDate)
	{
		List<LeaveModel> lstLeaves=new ArrayList<LeaveModel>();
		
		LeaveModel obj=new LeaveModel();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			/*
			rs=db.executeNonQuery(query.checkIfOnlineLeaveTakenQuery(empKey, fromDate, toDate));
			while(rs.next())
			{
				obj.setRecNO(rs.getInt("ID"));
				obj.setStatus(rs.getString("STATUS"));
				obj.setLeaveStartDate(rs.getDate("LEAVE_START_DATE"));
				obj.setLeaveEndDate(rs.getDate("LEAVE_END_DATE"));
			}
			*/
			Date tmpFromDate=null;
			
			rs=db.executeNonQuery(query.checkIfDesktopLeaveTakenQuery(empKey, fromDate, toDate));
			while(rs.next())
			{
				obj=new LeaveModel();
				obj.setRecNO(rs.getInt("Rec_No"));
				obj.setStatus(rs.getString("STATUS"));
				obj.setLeaveStartDate(rs.getDate("From_DATE"));
				/*if(tmpFromDate!=null)
				{
					if(tmpFromDate.before(obj.getLeaveStartDate()))
					{
					tmpFromDate=tmpFromDate;
					}
					else
					{
						tmpFromDate=obj.getLeaveStartDate();
					}
				}
				else
				{
				tmpFromDate=obj.getLeaveStartDate();
				}*/
				
				//obj.setLeaveStartDate(tmpFromDate);
				obj.setLeaveEndDate(rs.getDate("To_DATE"));
				obj.setLeaveTypeDesc(rs.getString("DESCRIPTION"));				
				obj.setReturnStatus(rs.getBoolean("RETURN_STATUS"));
				obj.setReturnDate(rs.getDate("RETURN_DATE"));
				if(obj.isReturnStatus())
				{
					 Calendar c = Calendar.getInstance();
					 c.setTime(obj.getReturnDate());
					 c.add(Calendar.DAY_OF_MONTH, -1);
					obj.setLeaveEndDate(c.getTime());
				}
				else
				{
					//	obj.setLeaveStartDate(fromDate);
					//changed by iqbal
					//obj.setLeaveStartDate(rs.getDate("From_DATE"));
					obj.setLeaveEndDate(toDate);
					
				}
				String status="";
				if(obj.getStatus().equals("A"))
					status="Approved";
				if(obj.getStatus().equals("C"))
					status="Created";
				if(obj.getStatus().equals("P"))
					status="Paid";
				if(obj.getStatus().equals("V"))
					status="Void";
				
				obj.setLeaveTypeDesc(rs.getString("DESCRIPTION")  +  " from " + sdf.format(obj.getLeaveStartDate()) + " To " + sdf.format(obj.getLeaveEndDate()) + " ( " + status + " )");
				
				 //by iqbal
			    obj.setPayment(rs.getString("payment"));
			    obj.setEnCashStatus(rs.getString("encash_status"));
			    lstLeaves.add(obj);
			}
			
			/*if(obj.getRecNO()==0)
			{
				rs=db.executeNonQuery(query.checkIfOnlineLeaveTakenQuery(empKey, fromDate, toDate));
				while(rs.next())
				{
					obj=new LeaveModel();
					obj.setRecNO(rs.getInt("ID"));
					obj.setStatus(rs.getString("STATUS"));
					obj.setLeaveStartDate(rs.getDate("LEAVE_START_DATE"));
				    obj.setLeaveEndDate(rs.getDate("LEAVE_END_DATE"));
				    obj.setLeaveTypeDesc(rs.getString("DESCRIPTION"));
				    
				    String status="";
					if(obj.getStatus().equals("A"))
						status="Approved";
					if(obj.getStatus().equals("C"))
						status="Created";
					if(obj.getStatus().equals("P"))
						status="Paid";
					if(obj.getStatus().equals("V"))
						status="Void";
					obj.setLeaveTypeDesc(rs.getString("DESCRIPTION")  +  " from " + sdf.format(obj.getLeaveStartDate()) + " To " + sdf.format(obj.getLeaveEndDate()) + " ( " + status + " )");
				   // obj.setLeaveTypeDesc(rs.getString("DESCRIPTION")  +  " from " + sdf.format(obj.getLeaveStartDate()) + " To " + sdf.format(obj.getLeaveEndDate()));
				    //by iqbal
				   // obj.setPayment(rs.getString("payment"));
				    obj.setPayment("P");
				    obj.setEnCashStatus(rs.getString("encash_status"));
				    lstLeaves.add(obj);
				}
			}*/
						
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---checkIfOnlineLeaveTaken-->" , ex);
		}
		return lstLeaves;
	}
	
	public List<OverTimeModel> getMaxOTList(int compKey)
	{
		List<OverTimeModel> lst=new ArrayList<OverTimeModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxOTQuery(compKey));
			while(rs.next())
			{
				OverTimeModel obj=new OverTimeModel();
				obj.setPositionId(rs.getInt("POS_ID"));				
				obj.setDayType(rs.getString("DAY_TYPE"));
				obj.setOtRate(rs.getDouble("OT_RATE"));				
				obj.setHours(rs.getDouble("HOURS"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getMaxOTList-->" , ex);
		}
		
		return lst;
	}
	
	public List<CompanyShiftModel> getCompanyShift(int compKey,int Shift_Key)
	{
		List<CompanyShiftModel> lst=new ArrayList<CompanyShiftModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCompanyShiftQuery(compKey, Shift_Key));
			while(rs.next())
			{
				CompanyShiftModel obj=new CompanyShiftModel();
				obj.setRecNo(rs.getInt("Rec_NO"));
				obj.setDayNo(rs.getInt("Day_NO"));
				obj.setOffDay(rs.getString("OffDay"));
				obj.setHours(rs.getDouble("Hours"));				
				obj.setShiftKey(Shift_Key);
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getCompanyShift-->" , ex);
		}
		
		return lst;
	}
	
	public ShiftModel getMinMaxTimeShift(int Shift_Key)
	{
		ShiftModel obj=new ShiftModel();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMinMaxTimeShiftQuery(Shift_Key));
			while(rs.next())
			{
				obj.setShiftKey(Shift_Key);
				obj.setFromTime(rs.getDate("MinDate"));
				obj.setToTime(rs.getDate("MaxDate"));							
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getMinMaxTimeShift-->" , ex);
		}
		
		return obj;
	}
	
	//Begin Save Time Sheet
	public int getNextSequenceNo()
	{
		int SequenceID=0;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNextSequenceNoQuery());
			while(rs.next())
			{
				SequenceID=rs.getInt("SequenceID");
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getNextSequenceNo-->" , ex);
		}
		
		return SequenceID;
	}
	
	public int deleteOldTimeSheets(int empKey,Date fromDate,Date toDate)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.deleteDAILYTSQuery(empKey, fromDate, toDate));
			result=db.executeUpdateQuery(query.deleteDAILYTIMINGQuery(empKey, fromDate, toDate));	
			result=db.executeUpdateQuery(query.deleteDAILYOTQuery(empKey, fromDate, toDate));	
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---deleteOldTimeSheets-->" , ex);
		}
		return result;		
	}
	public int deleteOldTimeSheetsByRecNO(int recNo)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.deleteDAILYTSByRecNoQuery(recNo));
			result=db.executeUpdateQuery(query.deleteDAILYTIMINGByRecNoQuery(recNo));	
			result=db.executeUpdateQuery(query.deleteDAILYOTByRecNoQuery(recNo));	
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---deleteOldTimeSheetsByRecNO-->" , ex);
		}
		return result;		
	}
	public int getMaxID(String tableName,String fieldName)
	{
		int result=0;
		
		TimeSheetQueries query=new TimeSheetQueries();	
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getMaxID-->" , ex);
		}	
		return result;
	}
	public int insertNewTimeSheets(TimeSheetGridModel obj)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.InsertDAILYTSQuery(obj));			
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---insertNewTimeSheets-->" , ex);
		}
		return result;		
	}
	
	
	public int InsertDailyTiming(TimeSheetGridModel obj,int webUserId,String webUserName)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		Date newDate;
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		try 
		{			
			result=db.executeUpdateQuery(query.InsertDailyTimingQuery(obj));
		
			if(obj.getCustomerJobRefKey()>0)
			{
				newDate=df.parse(sdf.format(c.getTime()));
				//UpdateStatusDescription
				String notes=obj.getNotes()==null?"":obj.getNotes().replace("'", "`");
				if(!notes.equals(""))
				{
				String sqlqQuery="Update Customer set StatusDesc='" + notes+"' where Cust_Key=" + obj.getCustomerJobRefKey();
				db.executeUpdateQuery(sqlqQuery);
				//check if same data enter before
				sqlqQuery="Select * from customerstatusHistory where RecNo =(select max(recNo) from customerstatushistory where " +
						" custKey =" + obj.getCustomerJobRefKey() + " and type ='C' ) ";
				ResultSet rs = db.executeNonQuery(sqlqQuery);
				boolean dataSame=false;
				while(rs.next())
				{
					if(rs.getInt("StatusID")==0 && rs.getString("StatusDescription").equals(notes))
					{
						dataSame=true;
					}
				}
				if(dataSame==false)
				{
				int recNO=getMaxID("CustomerStatusHistory", "RecNo");
				
				sqlqQuery="Insert into CustomerStatusHistory(RecNo,CustKey,ActionDate,StatusID,	StatusDescription,UserID,Type , CreatedFrom,WebUserID,WebUserName)" +
						" values (" + recNO +" , " + obj.getCustomerJobRefKey() + " ,'"+sdf.format(newDate)+"',0, '" + notes + "' , 1,'C','Timesheet',"+webUserId+",'"+webUserName+"' )";
				db.executeUpdateQuery(sqlqQuery);
				}
				
				}
				
				if(obj.getSelectedTask()!=null)
				{
					if(obj.getSelectedTaskStatus()!=null)
					{
						//update the tasks table  with status id selected.
						db.executeUpdateQuery(query.updateCustomerTaskStatusQuery(obj.getSelectedTask().getTaskid(), obj.getSelectedTaskStatus().getListId()));
						if(!notes.equals(""))
						db.executeUpdateQuery(query.addTaskDetailsQuery(obj.getSelectedTask().getTaskid(), obj.getSelectedTaskStatus().getListId(),notes,webUserID));
						
					}
				}
			}
					
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---InsertDailyTiming-->" , ex);
		}
		return result;		
	}
	
	public int InsertDAILYOT(TimeSheetGridModel obj)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.InsertDAILYOTQuery(obj));
					
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---InsertDAILYOT-->" , ex);
		}
		return result;		
	}
	public int addUserActivity(int FORM_ID,int ACTIVITY_ID,int EMP_KEY,int COMP_KEY,String DESCRIPTION,int WebUserID)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.addUserActivityQuery(FORM_ID, ACTIVITY_ID, EMP_KEY, COMP_KEY, DESCRIPTION, WebUserID));
					
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---addUserActivity-->" , ex);
		}
		return result;		
	}
	//Over Time
	public List<OverTimeModel> getOTCALCULATION(int compKey)
	{
		List<OverTimeModel> lst=new ArrayList<OverTimeModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getOTCALCULATIONQuery(compKey));
			while(rs.next())
			{
				OverTimeModel obj=new OverTimeModel();
				obj.setPositionId(rs.getInt("POS_ID"));				
				obj.setDayType(rs.getString("DAY_TYPE"));
				obj.setOtRate(rs.getDouble("OT_RATE"));				
				obj.setHours(rs.getDouble("HOURS"));
				obj.setOtCalc(rs.getString("OTCALC"));
				obj.setOtFlag(rs.getString("OTFLAG"));
				obj.setSalaryItem(rs.getDouble("Salary_Item"));
				obj.setCalcHours(rs.getDouble("CALC_HRS"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getOTCALCULATION-->" , ex);
		}
		
		return lst;
	}
	public List<Double> getOTEmployeeSalary(int empKey,Date tsDate)
	{
		List<Double> lst=new ArrayList<Double>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getOTEmployeeSalaryQuery(empKey, tsDate));
			while(rs.next())
			{				
				lst.add(rs.getDouble("Amount"));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getOTEmployeeSalary-->" , ex);
		}
		
		return lst;
	}
	//Transfer
	public int getNextTransferSequenceNo()
	{
		int SequenceID=0;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNextTransferSequenceNoQuery());
			while(rs.next())
			{
				SequenceID=rs.getInt("FormNo");
			}
			SequenceID+=1;
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getNextTransferSequenceNo-->" , ex);
		}
		
		return SequenceID;
	}
	
	public ContactModel getSiteInCharge(int projectKey)
	{
		TimeSheetQueries query=new TimeSheetQueries();
		ContactModel obj=new ContactModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSiteInChargeQuery(projectKey));
			while(rs.next())
			{
				obj.setContactId(rs.getInt("CONTACT_PERSON"));
				obj.setDescription(rs.getString("ENGLISH_FULL"));
			}		
			if(obj.getContactId()>0)
			{
				rs=db.executeNonQuery(query.getSiteContactQuery(obj.getContactId()));
				while(rs.next())
				{
					obj.setDetails(rs.getString("DETAILS"));
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getSiteInCharge-->" , ex);
		}
		return obj;		
	}
	
	public List<EmployeeModel> GetEmployeeTransfer(String empKeys,int compKey)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();				
		TimeSheetQueries query=new TimeSheetQueries();
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		ResultSet rs = null;
			try 
			{					
				if(!empKeys.equals(""))
				{
				int srNo=0;
				List<LocationModel> lstTransfer=getEmployeeCurrentLocation(empKeys);					
				rs=db.executeNonQuery(query.GetEmployeeTransferQuery(empKeys,compKey));
				while(rs.next())
				{
					srNo++;
					EmployeeModel obj=new EmployeeModel();
					obj.setSrNo(srNo);
					obj.setEmployeeKey(rs.getInt("EMP_KEY"));
					obj.setEmployeeNo(rs.getString("EMP_NO"));
					obj.setFullName(rs.getString("ENGLISH_FULL"));
					obj.setEmployeementDate(rs.getDate("Employeement_Date"));									
					obj.setLocationId(rs.getInt("LOC_ID"));
					obj.setPositionID(rs.getInt("POS_ID"));
					obj.setCompanyID(rs.getInt("Comp_Key"));
					
					for (LocationModel item : lstTransfer) 
					{
					if(item.getEmpKey()==obj.getEmployeeKey())
					{
						obj.setProjectKey(item.getProjectkey());
						obj.setProjectName(item.getProjectName());
						if(item.getEffDate()!=null)
						obj.setLastTransferDate(df.format(item.getEffDate()));
					}
					
					}
					
					lst.add(obj);					
				}
			  }				
				
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---GetEmployeeListFromFilter-->" , ex);
			}		
		
		return lst;
	}
	private List<LocationModel> getEmployeeCurrentLocation(String empKeys)
	{
		List<LocationModel> lst=new ArrayList<LocationModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{					
				if(!empKeys.equals(""))
				{
				rs=db.executeNonQuery(query.GetEmployeeCurrentLocationQuery(empKeys));
				while(rs.next())
				{					
					LocationModel obj=new LocationModel();
					obj.setEmpKey(rs.getInt("EMP_KEY"));
					obj.setProjectkey(rs.getInt("PROJECT_KEY"));					
					obj.setEffDate(rs.getDate("EFF_DATE"));									
					obj.setProjectName(rs.getString("Project_Name"));					
					lst.add(obj);					
				}
			  }				
				
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getEmployeeCurrentLocation-->" , ex);
			}		
		return lst;
	}
	
	public List<Integer> checkIfTransferExists(Date transferDate)
	{
		List<Integer> lstEmpKeys=new ArrayList<Integer>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
			try 
			{					
				rs=db.executeNonQuery(query.checkIfTransferExistsQuery(transferDate));
				while(rs.next())
				{
					lstEmpKeys.add(rs.getInt("EMP_KEY"));
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---checkIfTransferExists-->" , ex);
			}	
		return lstEmpKeys;
	}
	
	public int getNextTransferRecNo()
	{
		int SequenceID=0;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNextTransferRecNoQuery());
			while(rs.next())
			{
				SequenceID=rs.getInt("REC_NO");
			}
			SequenceID+=1;
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getNextTransferRecNo-->" , ex);
		}
		
		return SequenceID;
	}
	
	public int insertNewTransfer(TransferModel obj)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.insertNewTransferQuery(obj));
			db.executeUpdateQuery(query.updateEmployeeTableForTranfer(obj));
					
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---insertNewTransfer-->" , ex);
		}
		return result;		
	}
	
	//Project List
	private int getMaxProjectKey()
	{
		int SequenceID=0;
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxProjectKeyQuery());
			while(rs.next())
			{
				SequenceID=rs.getInt("REC_NO");
			}
			SequenceID+=1;
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getMaxProjectKey-->" , ex);
		}
		
		return SequenceID;
	}
	
	public List<Integer> getProjectUsedInTransfer()
	{
		List<Integer> lst=new ArrayList<Integer>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getProjectUsedInTransferQuery());
			while(rs.next())
			{
				lst.add(rs.getInt("Project_Key"));
			}
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getProjectUsedInTransfer-->" , ex);
		}
		
		return lst;
	}
	
	public int  insertNewProject(ProjectModel obj)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{
			if(obj.getProjectKey()==0)
			{
			obj.setProjectKey(getMaxProjectKey());
			result=db.executeUpdateQuery(query.insertNewProjectQuery(obj));
			}
			else
		   result=db.executeUpdateQuery(query.updateProjectQuery(obj));						
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---insertNewProject-->" , ex);
		}
		return result;		
	}
	
	public int  deleteProject(int Project_Key)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.deleteProjectQuery(Project_Key));
					
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---deleteProject-->" , ex);
		}
		return result;		
	}
	
	//Service List
	public List<Integer> getServiceUsedInTimesheet()
	{
		List<Integer> lst=new ArrayList<Integer>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getServiceUsedInTimesheetQuery());
			while(rs.next())
			{
				lst.add(rs.getInt("SERVICE_ID"));
			}
			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---getServiceUsedInTimesheet-->" , ex);
		}
		
		return lst;
	}
	public int  deleteService(int listID)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.deleteServiceQuery(listID));
					
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---deleteService-->" , ex);
		}
		return result;		
	}
	
	
	
	//Time sheet History
	public List<TimeSheetDataModel> getTimeSheetHistory(int compKey,int month,int year,int status)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		ResultSet rsEmp = null;
			try 
			{					
				rs=db.executeNonQuery(query.getTimeSheetHistoryQuery(compKey, month, year));
				while(rs.next())
				{	
					rsEmp=db.executeNonQuery(query.getTimeSheetHistoryEmployeeQuery(compKey, rs.getInt("tsMonth"), rs.getInt("tsYear"),0,status));
					while(rsEmp.next())
					{
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setEmpKey(rsEmp.getInt("emp_key"));
					obj.setEmpNo(rsEmp.getString("EMP_NO"));					
					obj.setDayNo(rsEmp.getInt("cnt"));
					obj.setEnFullName(rsEmp.getString("ENGLISH_FULL"));		
					obj.setTsMonth(rsEmp.getInt("tsMonth"));					
					obj.setTsYear(rsEmp.getInt("tsYear"));
					obj.setTsStatus(rsEmp.getInt("TS_STATUS")==0?"Created":"Approved");
					obj.setPresentDays(rsEmp.getDouble("Present"));
					obj.setHolidays(rsEmp.getInt("Holidays"));
					
					lst.add(obj);
					}
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getTimeSheetHistory-->" , ex);
			}		
		
		return lst;
	}
	public int  approveTimeSheet(TimeSheetDataModel obj,int type)
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{	
			if(type==1)
			result=db.executeUpdateQuery(query.approveTimeSheetQuery(obj));
			else
			result=db.executeUpdateQuery(query.approveMonthlyTimeSheetQuery(obj));	
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---approveTimeSheet-->" , ex);
		}
		return result;		
	}
	public int  autoApproveTimeSheet()
	{
		int result=0;		
		TimeSheetQueries query=new TimeSheetQueries();	
		try 
		{				
			result=db.executeUpdateQuery(query.autoApproveTimeSheetQuery());			
		}
		catch (Exception ex) {
			logger.error("error in TimeSheetData---autoApproveTimeSheet-->" , ex);
		}
		return result;		
	}
	
	//Reports
	public List<TimeSheetDataModel> getEmployeeTimeSheetHistory(int compKey,int fromMonth,int fromYear,int toMonth,int toYear,int projectKey,int empKey,String filterEmpKeys,int supervisorId,int userid)
	{
		
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();	
		List<TimeSheetDataModel> lstTiming=new ArrayList<TimeSheetDataModel>();	
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rsEmp = null;
		ResultSet dailyTiming = null;
		
		Calendar c = Calendar.getInstance();		
			try 
			{	
				c.set(toYear,toMonth-1,1);
			    int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			    String DateFrom = fromYear+"-"+fromMonth+"-"+1;
				String DateTo = toYear+"-"+toMonth+"-"+maxDay;
				dailyTiming=db.executeNonQuery(query.getEmployeesForReports(compKey, DateFrom,DateTo,empKey,filterEmpKeys,supervisorId,projectKey,userid));

				while(dailyTiming.next())
					{
						TimeSheetDataModel obj=new TimeSheetDataModel();
						obj.setEmpKey(dailyTiming.getInt("emp_key"));
						obj.setEmpNo(dailyTiming.getString("EMP_NO"));					
						obj.setDayNo(dailyTiming.getInt("cnt"));
						obj.setEnFullName(dailyTiming.getString("ENGLISH_FULL"));		
						obj.setTsMonth(dailyTiming.getInt("tsMonth"));				
						obj.setTsMonthName(months.get(obj.getTsMonth()-1));
						obj.setTsYear(dailyTiming.getInt("tsYear"));
						obj.setTsStatus(dailyTiming.getString("TSSTATUS"));
						obj.setPresentDays(dailyTiming.getDouble("Present"));
						obj.setHolidays(dailyTiming.getDouble("Holidays"));
						obj.setAbsance(dailyTiming.getDouble("absence"));
						obj.setLeave(dailyTiming.getDouble("leave"));
						obj.setSick(dailyTiming.getDouble("sick"));
						lstTiming.add(obj);
					}
					
					List<OverTimeModel> lstOT=getEmployeeOT(DateFrom, DateTo);
					rsEmp=db.executeNonQuery(query.getDetailTimeSheetHistoryQuery(compKey, DateFrom, DateTo,projectKey,empKey,filterEmpKeys,supervisorId));
					
					while(rsEmp.next())
					{
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setEmpKey(rsEmp.getInt("emp_key"));
					obj.setEmpNo(rsEmp.getString("EMP_NO"));					
					obj.setDayNo(rsEmp.getInt("cnt"));
					obj.setEnFullName(rsEmp.getString("ENGLISH_FULL"));		
					obj.setTsMonth(rsEmp.getInt("tsMonth"));				
					obj.setTsMonthName(months.get(obj.getTsMonth()-1));
					obj.setTsYear(rsEmp.getInt("tsYear"));
					obj.setTsStatus(rsEmp.getInt("TS_STATUS")==0?"Created":"Approved");
				//	obj.setProjectName(rsEmp.getString("project_name"));	
					
					for(TimeSheetDataModel model: lstTiming)
					{
						if(model.getEmpKey()==obj.getEmpKey() && model.getTsStatus().equalsIgnoreCase("P"))
						{
							obj.setPresentDays(model.getPresentDays());
						}
						else if(model.getEmpKey()==obj.getEmpKey() && model.getTsStatus().equalsIgnoreCase("H"))
						{
							obj.setHolidays(model.getHolidays());
						}
						else if (model.getEmpKey()==obj.getEmpKey() && model.getTsStatus().equalsIgnoreCase("L"))
						{
							obj.setLeave(model.getLeave());
						}
						else if(model.getEmpKey()==obj.getEmpKey() && model.getTsStatus().equalsIgnoreCase("S"))
						{
							obj.setSick(model.getSick());
						}
						else if(model.getEmpKey()==obj.getEmpKey() && model.getTsStatus().equalsIgnoreCase("A"))
						{
							obj.setAbsance(model.getAbsance());
							
						}
						
					}
					
					obj.setCalculation(obj.getPresentDays()+obj.getHolidays()+obj.getAbsance()+obj.getSick()+obj.getLeave());
					
					for (OverTimeModel item : lstOT) 
					{
					if(item.getOtRate()==1.25 && item.getLabourKey()==obj.getEmpKey() && item.getProjectKey()==obj.getProjectkey())
					{
						obj.setOt1(item.getTotalUnits());
					}
					if(item.getOtRate()==1.5 && item.getLabourKey()==obj.getEmpKey() && item.getProjectKey()==obj.getProjectkey())
					{
						obj.setOt2(item.getTotalUnits());
					}
					if(item.getOtRate()==2 && item.getLabourKey()==obj.getEmpKey() && item.getProjectKey()==obj.getProjectkey())
					{
						obj.setOt3(item.getTotalUnits());
					}
										
					}
					
					lst.add(obj);
					}
				}			   						
			//}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getTimeSheetHistory-->" , ex);
			}		
		
		return lst;
	}
	
	public List<ProjectReportModel> getProjectReport(int compKey,int fromMonth,int fromYear,int toMonth,int toYear,int empKey,String filterEmpKeys,int projectKey)
	{
		List<ProjectReportModel> lst=new ArrayList<ProjectReportModel>();		
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		TimeSheetQueries query=new TimeSheetQueries();
		Calendar c = Calendar.getInstance();		
		ResultSet rs = null;
			try 
			{		
				c.set(toYear,toMonth-1,1);
			    int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			    String DateFrom = fromYear+"-"+fromMonth+"-"+1;
				String DateTo = toYear+"-"+toMonth+"-"+maxDay;
				rs=db.executeNonQuery(query.getProjectReport(compKey, DateFrom, DateTo,empKey,filterEmpKeys,projectKey));
				while(rs.next())
				{	
					ProjectReportModel obj=new ProjectReportModel();
					obj.setProjectKey(rs.getInt("project_key"));
					obj.setProjectCode(rs.getString("project_code"));
					if(obj.getProjectCode()==null)
					{
						obj.setProjectCode("-");
					}
					obj.setProjectName(rs.getString("project_name"));	
					if(obj.getProjectName()==null || obj.getProjectName().equalsIgnoreCase(""))
					{
						obj.setProjectName("No Project Selected");
					}
					obj.setProjectArName(rs.getString("project_nameAR"));	
					obj.setTsMonth(rs.getInt("tsmonth"));	
					obj.setTsMonthDesc(months.get(obj.getTsMonth()-1));
					obj.setTsYear(rs.getInt("tsYear"));
					obj.setNormalHours(rs.getDouble("normal_hrs"));
					obj.setWorkWithPay(rs.getDouble("workwithpay"));
					obj.setAbsenceWithPay(rs.getDouble("absencewithpay"));
					obj.setHolidaysWithPay(rs.getDouble("holidayswithpay"));
					obj.setSickWithPay(rs.getDouble("sickwithpay"));
					obj.setLeave(rs.getDouble("leave"));	
					lst.add(obj);
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getProjectReport-->" , ex);
			}		
		
		return lst;
	}
	
	@SuppressWarnings("unused")
	public List<TimeSheetDataModel> getTimeSheetGenerated(int compKey,int fromMonth,int fromYear,int toMonth,int toYear,int empKey,String filterEmpKeys,int supervisorId,int projectId,int userId)
	{
		List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();		
		List<OverTimeModel> lstOverTime=new ArrayList<OverTimeModel>();
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		TimeSheetQueries query=new TimeSheetQueries();
		Calendar c = Calendar.getInstance();		
		ResultSet rs = null;
		ResultSet rsEmp = null;
			try 
			{			
				c.set(toYear,toMonth-1,1);
			    int maxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			    String DateFrom = fromYear+"-"+fromMonth+"-"+1;
				String DateTo = toYear+"-"+toMonth+"-"+maxDay;
				rs=db.executeNonQuery(query.getTimesheetGenartedReport(compKey, DateFrom,DateTo,empKey,filterEmpKeys,supervisorId,projectId,userId));
				lstOverTime=getTimesheetGenartedReportOvertIme(DateFrom, DateTo);
				while(rs.next())
				{	
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setReprotsRecNO(rs.getInt("rec_no"));
					obj.setReprotsLineNo(rs.getInt("line_no"));
					obj.setTiming(rs.getString("TIMING"));
					obj.setTsDate(rs.getDate("ts_date"));
					obj.setUnits(rs.getDouble("unit_nos"));
					obj.setUnitNO(rs.getDouble("normal_units"));
					obj.setEmpKey(rs.getInt("emp_key"));
					obj.setEmpKey(empKey);
					if(obj.getTiming()==null || obj.getTiming().equals("Y"))
					{
					
						if(rs.getDate("from_time")!=null && rs.getDate("to_time")!=null)
						{
							obj.setFromTime(new SimpleDateFormat("hh:mm a").format(rs.getDate("from_time")));
							obj.setToTime(new SimpleDateFormat("hh:mm a").format(rs.getDate("to_time")));
							//totalWorkedHours=totalWorkedHours+obj.getUnits();
						}
						
					}
					else
					{
						obj.setFromTime("-");
						obj.setToTime("-");
						obj.setTotalWorkingHours(rs.getDouble("unit_nos"));
					}
					
					obj.setEnPositionName(rs.getString("position"));
					obj.setDepartment(rs.getString("department"));
					obj.setProjectName(rs.getString("project_name"));
					if(obj.getProjectName()==null || obj.getProjectName().equalsIgnoreCase(""))
					{
						obj.setProjectName("No Project Selected");
					}
					obj.setEnFullName(rs.getString("ENGLISH_FULL"));		
					obj.setEmpNo(rs.getString("emp_no"));	
					obj.setStatus(rs.getString("leavestatus"));
					obj.setTimesheetDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("ts_date")));
					obj.setDayOrHours(rs.getString("unit_Name"));
					SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
					obj.setDayofWeek(simpleDateformat.format(obj.getTsDate()));
					if(obj.getStatus().equalsIgnoreCase("p"))
					{
						obj.setTsStatus("Present");
					}
					else if(obj.getStatus().equalsIgnoreCase("A"))
					{
						obj.setTsStatus("Absence");
					}
					else if(obj.getStatus().equalsIgnoreCase("L"))
					{
						obj.setTsStatus("Leave");
					}
					else if(obj.getStatus().equalsIgnoreCase("S"))
					{
						obj.setTsStatus("Sick");
					}
					else
					{
						obj.setTsStatus("Holiday");
					}
					String calculate=rs.getString("calc_flag");
					if(calculate.equalsIgnoreCase("Y"))
					{
						obj.setCalcFlag("Yes");
					}
					else
					{
						obj.setCalcFlag("No");
					}
					for(OverTimeModel model:lstOverTime)
					{
						//rsEmp=db.executeNonQuery(query.getTimesheetGenartedReportOvertIme(DateFrom, DateTo,rs.getInt("rec_no"),rs.getInt("line_no")));
					if(model.getOtRate()==1.25 && model.getRecNo()== rs.getInt("rec_no") && model.getLineNo()==rs.getInt("line_no"))
					{
						obj.setOt1((int)model.getTotalUnits());
					}
					if(model.getOtRate()==1.5 && model.getRecNo()== rs.getInt("rec_no") && model.getLineNo()==rs.getInt("line_no"))
					{
						obj.setOt2((int)model.getTotalUnits());
					}
					if(model.getOtRate()==2 && model.getRecNo()== rs.getInt("rec_no") && model.getLineNo()==rs.getInt("line_no"))
					{
						obj.setOt3((int)model.getTotalUnits());
					}
					int tot=(int) (obj.getOt1()+obj.getOt2()+obj.getOt3());
					obj.setTotalOverTime(tot);
										
					}
					lst.add(obj);
				}			   						
			}
			catch (Exception ex) 
			{
				logger.error("error in TimeSheetData---getTimeSheetGenerated-->" , ex);
			}		
		
		return lst;
	}
	
	public List<OverTimeModel> getEmployeeOT(String fromDate,String toDate)
	{
		List<OverTimeModel> lst=new ArrayList<OverTimeModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try
		{
			rs=db.executeNonQuery(query.getOTHistoryQuery(fromDate, toDate));
			while(rs.next())
			{
				OverTimeModel obj=new OverTimeModel();
				obj.setOtRate(rs.getDouble("CALCULATION"));
				obj.setTotalUnits(rs.getDouble("TotalUnit"));
				obj.setTotalAmount(rs.getDouble("TotalAmount"));
				obj.setLabourKey(rs.getInt("labour_key"));
				obj.setProjectKey(rs.getInt("project_key"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getEmployeeOT-->" , ex);
		}
		
		return lst;
	}
	
	public List<OverTimeModel> getTimesheetGenartedReportOvertIme(String DateFrom,String DateTo)
	{
		List<OverTimeModel> lst=new ArrayList<OverTimeModel>();
		TimeSheetQueries query=new TimeSheetQueries();
		ResultSet rs = null;
		try
		{
			rs=db.executeNonQuery(query.getTimesheetGenartedReportOvertIme(DateFrom,DateTo));
			while(rs.next())
			{
				OverTimeModel obj=new OverTimeModel();
				obj.setOtRate(rs.getDouble("CALCULATION"));
				obj.setLineNo(rs.getInt("line_no"));
				obj.setRecNo(rs.getInt("Rec_no"));
				obj.setTotalUnits(rs.getDouble("units"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TimeSheetData---getTimesheetGenartedReportOvertIme-->" , ex);
		}
		
		return lst;
	}
	
		public List<EmployeeModel> searchAdvancedEmployee(int compKey,String type,String employeeType,String empNo,String empName,int depId,int posId)
		{
			List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();		
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				
				rs=db.executeNonQuery(query.searchAdvancedEmployeeQuery(compKey, type, employeeType, empNo, empName, depId, posId));
				while(rs.next())
				{
					EmployeeModel obj=new EmployeeModel();
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo"));
					obj.setFullName(rs.getString("FullName"));
					obj.setArabicName(rs.getString("ArabicName"));
					obj.setDepartmentID(rs.getInt("DepartmentID"));
					obj.setDepartment(rs.getString("Department"));
					obj.setArabicDepartment(rs.getString("ArabicDepartment"));
					obj.setPositionID(rs.getInt("PositionID"));
					obj.setPosition(rs.getString("Position"));
					obj.setArabicPosition(rs.getString("ArabicPosition"));
					obj.setNationalityID(rs.getInt("CountryID"));
					obj.setCountry(rs.getString("Country")==null?"":rs.getString("Country"));
					obj.setCompanyName(rs.getString("CompanyName"));
					obj.setDateOfBirth(rs.getDate("DateOfBirth"));
					obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
					//obj.setEnFirstName(rs.getString("ENGLISH_FIRST"));
					//obj.setEnMiddleName(rs.getString("ENGLISH_MIDDLE"));
					//obj.setEnLastName(rs.getString("ENGLISH_LAST"));
					//obj.setArFirstName(rs.getString("ARABIC_FIRST"));
					//obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
					//obj.setArLastName(rs.getString("ARABIC_LAST"));
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));
					obj.setGender(rs.getString("Sex"));
					obj.setMarital(rs.getString("Marital"));
					//obj.setCheckedEmployee(true);
												
					lstEmployees.add(obj);
				}
				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---searchAdvancedEmployee-->" , ex);
			}
			return lstEmployees;
		}
		
		//Assign Employee To TimeSheet		
		public List<EmployeeModel> getAssignSalaryEmployeeList(int compkey,String type)
		{
			List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();		
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				
				rs=db.executeNonQuery(query.getAssignSalaryEmployeeQuery(compkey,type));
				while(rs.next())
				{
					EmployeeModel obj=new EmployeeModel();
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo"));
					obj.setFullName(rs.getString("FullName"));				
					obj.setDepartment(rs.getString("Department"));					
					obj.setPosition(rs.getString("Position"));					
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));					
					obj.setSalaryStatus(rs.getString("Salary_Create"));	
					obj.setHrSalary(obj.getSalaryStatus().equalsIgnoreCase("S") ? true : false);
					obj.setTimesheetSalary(!obj.isHrSalary());
					lstEmployees.add(obj);
				}
				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getAssignSalaryEmployeeList-->" , ex);
			}
			return lstEmployees;
		}
		
		public List<EmployeeModel> getAssignEmployeesToProjectByKeys(int compkey,String filterKeys,String filter)
		{
			List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();		
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				
				rs=db.executeNonQuery(query.getAssignEmployeesToProjectByKeys(compkey,filterKeys,filter));
				while(rs.next())
				{
					EmployeeModel obj=new EmployeeModel();
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo")==null?"":rs.getString("EmployeeNo"));
					obj.setFullName(rs.getString("FullName")==null?"":rs.getString("FullName"));				
					obj.setDepartment(rs.getString("Department")==null?"":rs.getString("Department"));					
					obj.setPosition(rs.getString("Position")==null?"":rs.getString("Position"));					
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));					
					obj.setSalaryStatus(rs.getString("Salary_Create")==null?"":rs.getString("Salary_Create"));	
					obj.setHrSalary(obj.getSalaryStatus().equalsIgnoreCase("S") ? true : false);
					obj.setProjectKey(rs.getInt("locationId"));
					obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
					obj.setJoiningDate(rs.getDate("employeementdate"));
					obj.setTimesheetSalary(!obj.isHrSalary());
					lstEmployees.add(obj);
				}
				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getAssignEmployeesToProjectByKeys-->" , ex);
			}
			return lstEmployees;
		}
		
		public String  updateAssignedEmployeesToProject(int locationId,int empId)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{			
				db.executeUpdateQuery(result=query.updateAssignedEmployeesToProject(locationId, empId));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---updateAssignedEmployeesToProject-->" , ex);
			}
			return result;		
		}
		
		public String  generateUpdateSalaryQuery(int empKey,String salaryCreate)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{			
				result=query.updateSalaryCreateQuery(empKey, salaryCreate) + ";";					
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---generateUpdateSalaryQuery-->" , ex);
			}
			return result;		
		}
		
		public int  updateSalaryCreate(String batchQuery)
		{
			int result=0;		
			//TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{			
				result=db.executeUpdateQuery(batchQuery);
						
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---updateSalaryCreate-->" , ex);
			}
			return result;		
		}
		
		//Assign Employee to Shift		
		public List<EmployeeShiftModel> getAssignedEmployeesToShift(int compId,Date fromDate,Date toDate,String empkeys)
		{
			List<EmployeeShiftModel> lstEmployees=new ArrayList<EmployeeShiftModel>();
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				
				rs=db.executeNonQuery(query.getAssignedEmployeesToShiftQuery(compId, fromDate, toDate,empkeys));
				while(rs.next())
				{
					EmployeeShiftModel obj=new EmployeeShiftModel();
					obj.setCompanyKey(compId);
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo"));
					obj.setEnglishName(rs.getString("FullName"));				
					obj.setPositionID(rs.getInt("PositionID"));
					obj.setPosition(rs.getString("Position"));											
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));													
					obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("from_date")));
					obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("to_date")));					
					obj.setShiftkey(rs.getInt("SHIFT_KEY"));
					obj.setOldShift(true);
					lstEmployees.add(obj);
				}			   				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getAssignedEmployeesToShift-->" , ex);
			}
			
			return lstEmployees;
			
		}
		public List<EmployeeShiftModel> getNewAssignedEmployeesToShift(String empkeys)
		{
			List<EmployeeShiftModel> lstEmployees=new ArrayList<EmployeeShiftModel>();
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				
				rs=db.executeNonQuery(query.getNewAssignedEmployeesToShiftQuery(empkeys));
				while(rs.next())
				{
					EmployeeShiftModel obj=new EmployeeShiftModel();	
					obj.setEmployeeKey(rs.getInt("EmployeeKey"));
					obj.setEmployeeNo(rs.getString("EmployeeNo"));
					obj.setEnglishName(rs.getString("FullName"));										
					obj.setPositionID(rs.getInt("PositionId"));
					obj.setPosition(rs.getString("Position"));																									
					obj.setEmployeementDate(rs.getDate("EmployeementDate"));
				     lstEmployees.add(obj);
				}
				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getNewAssignedEmployeesToShift-->" , ex);
			}
			
			return lstEmployees;
			
		}
		
		public List<EmployeeShiftModel> getAssignedPositionToShift(int compId,Date fromDate,Date toDate)
		{
			List<EmployeeShiftModel> lstEmployees=new ArrayList<EmployeeShiftModel>();
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				
				rs=db.executeNonQuery(query.getAssignedPositionToShiftQuery(compId, fromDate, toDate));
				while(rs.next())
				{
					EmployeeShiftModel obj=new EmployeeShiftModel();	
					obj.setCompanyKey(compId);
					obj.setPositionID(rs.getInt("POS_ID"));
					obj.setPosition(rs.getString("Position"));																									
					obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("from_date")));
					obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("to_date")));					
					obj.setShiftkey(rs.getInt("SHIFT_KEY"));
					obj.setOldShift(true);
					lstEmployees.add(obj);
				}
				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getAssignedPositionToShift-->" , ex);
			}
			
			return lstEmployees;			
		}
		
		public String addEmployeeShift(EmployeeShiftModel obj)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{	
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fromDate= df.parse(obj.getShiftFromDate());
				Date toDate= df.parse(obj.getShiftToDate());
				obj.setShiftFromDate(sdf.format(fromDate));
				obj.setShiftToDate(sdf.format(toDate));
				db.executeUpdateQuery(query.addEmployeeShiftQuery(obj));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---addEmployeeShift-->" , ex);
			}
			return result;		
		}
		
		public String updateEmployeeShift(EmployeeShiftModel obj,Date fromSelectedDate,Date toSelectedDate)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{					
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fromDate= df.parse(obj.getShiftFromDate());
				Date toDate= df.parse(obj.getShiftToDate());
				obj.setShiftFromDate(sdf.format(fromDate));
				obj.setShiftToDate(sdf.format(toDate));
				c.setTime(fromSelectedDate);
				c.add(Calendar.DAY_OF_MONTH,-1);	
				logger.info(sdf.format(c.getTime()));
				db.executeUpdateQuery(query.updateEmployeeShiftQuery(obj , sdf.format(c.getTime())));				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---updateEmployeeShift-->" , ex);
			}
			return result;		
		}
		public int  deleteEmployeeShift(EmployeeShiftModel obj)
		{
			int result=0;		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{			
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date fromDate= df.parse(obj.getShiftFromDate());
				Date toDate= df.parse(obj.getShiftToDate());
				obj.setShiftFromDate(sdf.format(fromDate));
				obj.setShiftToDate(sdf.format(toDate));
				result=db.executeUpdateQuery(query.deleteEmployeeShiftQuery(obj));
						
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---deleteEmployeeShift-->" , ex);
			}
			return result;		
		}
		
		//Department Setup
		public List<HRListValuesModel> getDepartmentList(int fieldId,String type)
		{
			 List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
			 TimeSheetQueries query=new TimeSheetQueries();	
			 HRListValuesModel obj=new HRListValuesModel();
				if(!type.equals(""))
				{
				obj.setListId(0);					
				obj.setEnDescription(type);		
				obj.setArDescription(type);
				lst.add(obj);
				}					   			
				ResultSet rs = null;
				try 
				{
					rs=db.executeNonQuery(query.getDepartmentListQuery(fieldId));
					while(rs.next())
					{
						obj=new HRListValuesModel();
						obj.setListId(rs.getInt("ID"));					
						obj.setEnDescription(rs.getString("DESCRIPTION"));
						obj.setArDescription(rs.getString("ARABIC"));
						obj.setSubId(rs.getInt("SUB_ID"));
						obj.setFieldId(rs.getInt("FIELD_ID"));
						obj.setFieldName(rs.getString("FIELD_NAME"));
						obj.setPriorityId(rs.getInt("PRIORITY_ID"));
						obj.setRequired(rs.getString("REQUIRED"));
						obj.setQbListId(rs.getString("QBListID"));
						lst.add(obj);
					}
				}
				catch (Exception ex) {
					logger.error("error in ListData---getDepartmentList-->" , ex);
				}
			 return lst;
		}
		
		public List<HRListValuesModel> getDepartmentPositionList(int COMP_KEY,int departmentID)
		{
			 List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
			 TimeSheetQueries query=new TimeSheetQueries();	
			 HRListValuesModel obj=new HRListValuesModel();
					
				ResultSet rs = null;
				try 
				{
					rs=db.executeNonQuery(query.getDepartmentPositionListQuery(COMP_KEY, departmentID));
					while(rs.next())
					{
						obj=new HRListValuesModel();
						obj.setListId(rs.getInt("ID"));					
						obj.setEnDescription(rs.getString("DESCRIPTION"));					
						obj.setSubId(rs.getInt("DEP_ID"));
						obj.setFieldId(rs.getInt("EMP_ALLOWED"));									
						lst.add(obj);
					}
				}
				catch (Exception ex) {
					logger.error("error in ListData---getDepartmentPositionList-->" , ex);
				}
			 return lst;
		}
		public String addCOMPSTANDPOS(int compKey,int depId,int posId,int empAllowed,String positionName)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{					
				db.executeUpdateQuery(query.addCOMPSTANDPOSQuery(compKey, depId, posId, empAllowed, positionName));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---addCOMPSTANDPOS-->" , ex);
			}
			return result;		
		}
		public String updateCOMPSTANDPOS(int compKey,int depId,int posId,int empAllowed,String positionName)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{					
				db.executeUpdateQuery(query.updateCOMPSTANDPOSQuery(compKey, depId, posId, empAllowed));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---updateCOMPSTANDPOS-->" , ex);
			}
			return result;		
		}
		public String updateNewCompanySetup(int compKey,int totalDep,int totalPos)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{					
				db.executeUpdateQuery(query.updateNewCompanySetupQuery(compKey, totalDep, totalPos));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---updateNewCompanySetup-->" , ex);
			}
			return result;		
		}
		
		public String deleteCOMPSTANDPOS(int compKey,int depId,int posId)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{					
				db.executeUpdateQuery(query.deleteCOMPSTANDPOSQuery(compKey, depId, posId));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---deleteCOMPSTANDPOS-->" , ex);
			}
			return result;		
		}
		
		public int checkIfDepartmentPositionUsed(int COMP_KEY,int departmentID,int positionId)
		{
			int count=0;
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.checkIfDepartmentPositionUsedQuery(COMP_KEY, departmentID, positionId));
				while(rs.next())
				{
					count=rs.getInt("cnt");
				}				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---checkIfDepartmentPositionUsed-->" , ex);
			}
			
			return count;
		}
		public int getTotalCompanyEmployees(int COMP_KEY)
		{
			int result=0;
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getTotalCompanyEmployeesQuery(COMP_KEY));
				while(rs.next())
				{
					result=rs.getInt("totEmp");
				}				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getTotalCompanyEmployees-->" , ex);
			}
			
			return result;
		}
		public int getTotalEmployeesAllowed(int COMP_KEY)
		{
			int result=0;
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getTotalEmployeesAllowedQuery(COMP_KEY));
				while(rs.next())
				{
					result=rs.getInt("empAllowed");
				}				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getTotalEmployeesAllowed-->" , ex);
			}
			
			return result;
		}
		public Integer[] getCompanyTotalDepartments(int COMP_KEY)
		{
			Integer[] result=new Integer[4];
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getCompanyTotalDepartmentsQuery(COMP_KEY));
				while(rs.next())
				{
					result[0]=rs.getInt("TOT_DEP");
					result[1]=rs.getInt("TOT_POS");
					result[2]=rs.getInt("TOT_EMP");
					result[3]=rs.getInt("TOT_VAC");
				}				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getCompanyTotalDepartments-->" , ex);
			}
			
			return result;
		}
		
		public Integer[] getNewCompanyTotalDepartments(int COMP_KEY)
		{
			Integer[] result=new Integer[2];
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getNewCompanyTotalDepartmentsQuery(COMP_KEY));
				while(rs.next())
				{
					result[0]=rs.getInt("TotalDepartments");
					result[1]=rs.getInt("TotalPositions");					
				}				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getNewCompanyTotalDepartments-->" , ex);
			}
			
			return result;
		}
		//HashMap<Integer, List<TasksModel>> hmCustomerTasks = new HashMap<Integer, List<TasksModel>>(); 
		
		public List<TasksModel> getCustomerTasks(int customerKey,String orderBy)
		{			
			ResultSet rs = null;
			TimeSheetQueries query=new TimeSheetQueries();
			List<TasksModel> lst=new ArrayList<TasksModel>();
			TasksModel objSelect=new TasksModel();
			objSelect.setTaskid(0);
			objSelect.setTaskNumber("0");
			objSelect.setTaskName("Select");
			objSelect.setStatusKey(0);
			lst.add(objSelect);
			
			try 
			{
				rs=db.executeNonQuery(query.getCustomerTasksQuery(customerKey,orderBy));
				while(rs.next())
				{
					TasksModel obj=new TasksModel();
					obj.setTaskid(rs.getInt("TaskID"));
					obj.setTaskNumber(rs.getString("taskNo"));
					obj.setTaskName(rs.getString("TaskName"));
					obj.setStatusKey(rs.getInt("status"));
					lst.add(obj);
				}
				
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getCustomerTasks-->" , ex);
			}
			return lst;
		}
		
		//time sheet attachment
		public List<QuotationAttachmentModel> getTimesheetAttchamnet(int recNo)
		{
			List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getTimesheetAttachmentQuery(recNo));
				while(rs.next())
				{			
					QuotationAttachmentModel obj=new QuotationAttachmentModel();
					obj.setAttachid(rs.getInt("AttachId"));
					obj.setSerialNumber(rs.getInt("recNo"));
					obj.setFilepath(rs.getString("attachmentpath")==null?"":rs.getString("attachmentpath"));
					obj.setFilename(rs.getString("fileName")==null?"":rs.getString("fileName"));
					lst.add(obj);
				}
			}
			
			catch (Exception ex) {
				logger.error("error in TimeSheetData---getTimesheetAttachment-->" , ex);
			}
			return lst;
		}
		
		public int addTimesheetAttachment(TimeSheetGridModel obj)
		{
			int result=0;		
			TimeSheetQueries query=new TimeSheetQueries();	
			if(obj.getOldRecNo()>0)
				deleteTimesheetAttachmnet(obj.getOldRecNo(), 0);
			try 
			{
				if(obj.getListOfattchments()!=null)
				{
					for (QuotationAttachmentModel item : obj.getListOfattchments())
					{
						item.setSerialNumber(obj.getRecNo());
						db.executeUpdateQuery(query.addTimesheetAttachmentQuery(item));		
					}
				}								
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---addTimesheetAttachment-->" , ex);
			}
			return result;		
		}
		public String deleteTimesheetAttachmnet(int recNo,int attachId)
		{
			String result="";		
			TimeSheetQueries query=new TimeSheetQueries();	
			try 
			{					
				db.executeUpdateQuery(query.deleteTimesheetAttachmnetQuery(recNo, attachId));
			}
			catch (Exception ex) {
				logger.error("error in TimeSheetData---deleteTimesheetAttachmnet-->" , ex);
			}
			return result;		
		}
		
		public List<String> getGCMUsersList(String topic)
		{
			 List<String> lst=new ArrayList<String>();			 					
			ResultSet rs = null;
				try 
				{
					rs=db.executeNonQuery("Select RegistrationId from gcmusers where topic='" + topic +"'");
					while(rs.next())
					{								
						lst.add(rs.getString("RegistrationId"));	
					}
				}
				catch (Exception ex) {
					logger.error("error in TimeSheetData---getGCMUsersList-->" , ex);
				}
			 return lst;
		}
		
		//MobileAttendance
		public List<TimeSheetDataModel> getMobileAttendance(int UserID)
		{
			List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();					
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;			
				try 
				{					
					rs=db.executeNonQuery(query.getMobileAttendanceQuery(UserID));
					while(rs.next())
					{									
						TimeSheetDataModel obj=new TimeSheetDataModel();						
						obj.setEnFullName(rs.getString("EmployeeName"));												
						if(rs.getDate("CheckinTime")!=null)
						{
							obj.setFromTime(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(rs.getDate("CheckinTime")));							
						}
						if(rs.getDate("CheckoutTime")!=null)
						{
							obj.setToTime(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(rs.getDate("CheckoutTime")));							
						}
						obj.setCheckinLatitude(rs.getDouble("CheckinLatitude"));
						obj.setCheckinLongitude(rs.getDouble("CheckinLongitude"));
						obj.setCheckoutLatitude(rs.getDouble("CheckoutLatitude"));
						obj.setCheckoutLongitude(rs.getDouble("CheckoutLongitude"));
						obj.setCheckinNote(rs.getString("CheckinNote"));
						obj.setCheckoutNote(rs.getString("CheckoutNote"));
						//Reason,Result,DestinaceKm,DestinaceMeter,CustomerType,CustomerName  
						obj.setReason(rs.getString("Reason"));
						obj.setResult(rs.getString("Result"));
						obj.setCustomerType(rs.getString("CustomerType"));
						obj.setCustomerName(rs.getString("CustomerName"));
						obj.setDestinaceMeter(rs.getInt("DestinaceMeter"));
						obj.setDestinaceKm(rs.getInt("DestinaceKm"));
						lst.add(obj);						
					}			   						
				}
				catch (Exception ex) 
				{
					logger.error("error in TimeSheetData---getMobileAttendance-->" , ex);
				}		
			
			return lst;
		}
		
		public List<EmployeeModel> GetUsersList()
		{
			List<EmployeeModel> lst=new ArrayList<EmployeeModel>();	
			EmployeeModel obj=new EmployeeModel();					
			obj.setEmployeeKey(0);					
			obj.setFullName("All");					
			lst.add(obj);					
			
			TimeSheetQueries query=new TimeSheetQueries();
			ResultSet rs = null;
				try 
				{	
					rs=db.executeNonQuery(query.getUsersQuery());
					while(rs.next())
					{					
						obj=new EmployeeModel();					
						obj.setEmployeeKey(rs.getInt("USERID"));					
						obj.setFullName(rs.getString("EmployeeName"));					
						lst.add(obj);					
					}				  							
				}
				catch (Exception ex) 
				{
					logger.error("error in TimeSheetData---GetUsersList-->" , ex);
				}		
			
			return lst;
		}
		
}
