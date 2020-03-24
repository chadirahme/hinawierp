package hr;

import hr.model.ActivityModel;
import hr.model.CompanyModel;
import hr.model.ContactModel;
import hr.model.LeaveModel;
import hr.model.LeaveapproveOrRejectModel;
import hr.model.PassportModel;
import hr.model.SponsorModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import layout.MenuModel;
import model.CompSetupModel;
import model.CompanyDBModel;
import model.EmailSettingsModel;
import model.EmployeeModel;
import model.ExpirySettingsModel;
import model.HRListValuesModel;
import model.LeaveExpiryModel;
import model.LoanModel;
import model.PaymentMethod;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

import setup.users.WebusersModel;
import timesheet.TimeSheetQueries;
import company.CompanyData;
import db.DBHandler;
import db.SQLDBHandler;

public class HRData 
{
private Logger logger = Logger.getLogger(this.getClass());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		

		//LiveDBHandler db=new LiveDBHandler("");
		SQLDBHandler db=new SQLDBHandler("hinawi_hr");
		int parentID=1;
		WebusersModel dbUser=null;
		
		public HRData()
		{
			try
			{
				Session sess = Sessions.getCurrent();
				DBHandler mysqldb=new DBHandler();
				ResultSet rs=null;
				CompanyDBModel obj=new CompanyDBModel();
				dbUser=(WebusersModel)sess.getAttribute("Authentication");
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
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---Init-->" , ex);
			}
		}
		
		public List<MenuModel> getHRRoles(int companyRoleId)
		{
			List<MenuModel> lstRoles=new ArrayList<MenuModel>();
			try
			{
				CompanyData compData=new CompanyData();		
				lstRoles=compData.getRoleCredentials(companyRoleId, parentID);
				 
			}
			catch (Exception ex) {
				logger.error("error in HRData---getHRRoles-->" , ex);
			}
			return lstRoles;
		}
		
	public int getDefaultCompanyID(int UserID)
	{
		int companyID=0;
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try
		{
			rs=db.executeNonQuery(query.getDefaultCompanyQuery(UserID));
			while(rs.next())
			{
				companyID=rs.getInt("DEFAULT_COMP");
			}
		}
		catch (Exception ex) {
			logger.error("error in HRData---getEmployeeList-->" , ex);
		}
		return companyID;
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
			logger.error("error in HRData---addUserActivity-->" , ex);
		}
		return result;		
	}
	
	public List<EmployeeModel> getEmployeeList(int compkey,String listtype,String status,int supervisorId)
	{
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForInactiveStatus=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForActiveStatus=new ArrayList<EmployeeModel>();
		//
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			if(!listtype.equals(""))
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(0);
				obj.setEmployeeNo("");
				obj.setFullName(listtype);
				lstEmployees.add(obj);
			}
			Date createDateNew;
			Calendar c = Calendar.getInstance();
			createDateNew=df.parse(sdf.format(c.getTime()));
			ResultSet newRsActive = null;
			ResultSet newRs = null;
			String inactiveSatus="";
			newRs=db.executeNonQuery(query.getEmpStatusDescriptionforInactive(1));
			newRsActive=db.executeNonQuery(query.getEmpStatusDescriptionForActive(1,createDateNew));
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
			
			
			/*
			if(compkey!=0)
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(0);
				obj.setEmployeeNo("0");
				obj.setFullName("ALL");
				lstEmployees.add(obj);			
			}
			*/
			
			rs=db.executeNonQuery(query.getEmployeesQuery(compkey,status,supervisorId));
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
				obj.setArabicPosition(rs.getString("ArabicPosition")==null?"":rs.getString("ArabicPosition"));
				obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"Local":rs.getString("Country"));
				obj.setCompanyName(rs.getString("CompanyName"));
				obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				obj.setEnFirstName(rs.getString("EnglishFirstName")==null?"":rs.getString("EnglishFirstName"));
				obj.setEnMiddleName(rs.getString("EnglishMiddleName")==null?"":rs.getString("EnglishMiddleName"));
				obj.setEnLastName(rs.getString("EnglishLastName"));
				obj.setSupervisorId(rs.getInt("SupervisorID"));
				obj.setSupervisorName(rs.getString("supervisorName")==null?"":rs.getString("supervisorName"));
				obj.setWorkGroupName(rs.getString("workergroupname")==null?"":rs.getString("workergroupname"));
				obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
			//	obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
			//	obj.setArLastName(rs.getString("ARABIC_LAST"));
				obj.setEmployeementDate(rs.getDate("EmployeementDate"));
				obj.setEmployeementDateString(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("EmployeementDate")));
				obj.setGender(rs.getString("Sex")==null?"":rs.getString("Sex"));
				obj.setMaritalStatus(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setMarital(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setEmployeeStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				String active=rs.getString("Active");
				obj.setSuper_supervisorId(rs.getInt("SuperSupervisorId"));
				obj.setSuper_supervisorName(rs.getString("SuperAdminName")==null?"":rs.getString("SuperAdminName"));
				obj.setSuper_supervisorNameAR(rs.getString("ArabicSuperAdminName")==null?"":rs.getString("ArabicSuperAdminName"));
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
			logger.error("error in HRData---getEmployeeList-->" , ex);
		}
		return lstEmployees;
	}

	
	
	public List<EmployeeModel> getEmpMastList()
	{
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		//
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getEmpMastList());
			while(rs.next())
			{
			EmployeeModel obj=new EmployeeModel();
			obj.setEmployeeKey(rs.getInt("emp_key"));
			obj.setEmail(rs.getString("details")==null?"":rs.getString("details"));
			lstEmployees.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in HRData---getEmpMastList-->" , ex);
		}
		return lstEmployees;
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
			
		   // SQLDBHandler db=new SQLDBHandler("HRONLINE");
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
					obj.setNotes(rs.getString("notes"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	
	public List<HRListValuesModel> getHRListValuesWithSub(int fieldId,int sub_id,String type)
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		 
		 
			HRListValuesModel obj=new HRListValuesModel();
			if(!type.equals(""))
			{
			obj.setListId(0);					
			obj.setEnDescription(type);
			lst.add(obj);
			}
			
		   // SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getHRListValuesWithSubQuery(fieldId,sub_id));
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
					obj.setNotes(rs.getString("notes"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	

	
	public List<HRListValuesModel> getDepartemnet(int compId)
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		 
		 
			HRListValuesModel obj=new HRListValuesModel();
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getDepartemnet(compId));
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("dep_ID"));					
					obj.setEnDescription(rs.getString("dept"));
					/*obj.setArDescription(rs.getString("dept"));*/
				/*	obj.setSubId(rs.getInt("SUB_ID"));
					obj.setFieldId(rs.getInt("FIELD_ID"));*/
				/*	obj.setFieldName(rs.getString("FIELD_NAME"));
					obj.setPriorityId(rs.getInt("PRIORITY_ID"));
					obj.setRequired(rs.getString("REQUIRED"));*/
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	
	public List<HRListValuesModel> getPostion(int deptId,int compId)
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		 
		 
			HRListValuesModel obj=new HRListValuesModel();
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getPostion(deptId,compId));
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
					obj.setEmployeeAllowed(rs.getInt("EMP_ALLOWED"));//used in edit employee
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	
	public int UpdateEmployees(EmployeeModel obj)
	{
		int result=0;
		
		HRQueries query=new HRQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.updateEmployeesQuery(obj));		
		}
		catch (Exception ex) {
			logger.error("error in HRData---UpdateEmployees-->" , ex);
		}
		return result;		
	}
	
	//Employee Ledger
	public List<CompanyModel> getCompanyList(int userID)
	{
		 List<CompanyModel> lst=new ArrayList<CompanyModel>();
		 
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
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
				logger.error("error in HRData---getCompanyList-->" , ex);
			}
		 return lst;
	}
	public List<EmployeeModel> GetEmployeeLedgerList(int CompKey,String EmpFilterStatus,boolean includeEOS,int DEP_ID,int POS_ID)
	{
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetEmployeeLedgerQuery(CompKey, EmpFilterStatus, includeEOS, DEP_ID,POS_ID));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EMP_KEY"));
				obj.setEmployeeNo(rs.getString("EMP_NO"));
				obj.setFullName(rs.getString("ENGLISH_FULL"));
				obj.setArabicName(rs.getString("ARABIC_FULL"));
				obj.setDepartmentID(rs.getInt("DEP_ID"));
				obj.setDepartment(rs.getString("EMP_DEP"));
				obj.setArabicDepartment(rs.getString("EMP_DEPAR"));
				//obj.setPositionID(rs.getInt("PositionID"));
				obj.setPosition(rs.getString("Position"));
				//obj.setArabicPosition(rs.getString("ArabicPosition"));
				//obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"":rs.getString("Country"));
				obj.setCompanyName(rs.getString("COMP_NAME"));
				//obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				//obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				obj.setEnFirstName(rs.getString("ENGLISH_FIRST"));
				obj.setEnMiddleName(rs.getString("ENGLISH_MIDDLE"));
				obj.setEnLastName(rs.getString("ENGLISH_LAST"));
				obj.setArFirstName(rs.getString("ARABIC_FIRST"));
				obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
				obj.setArLastName(rs.getString("ARABIC_LAST"));
				obj.setEmployeementDate(rs.getDate("EMPLOYEEMENT_DATE"));
				
				String status=rs.getString("Active");
				String terminate=rs.getString("TERMINATED");
				if(status.equals("I"))
				{
					if(!terminate.equals("Y"))
					{
						obj.setStatus("Inactive");
						
					}
					else
					{
						obj.setStatus("EOS");
					}
				}
				else
				{
					obj.setStatus("Active");
					
				}
				
				lstEmployees.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in HRData---GetEmployeeLedgerList-->" , ex);
		}
		return lstEmployees;
	}
	public  List<Integer> GetAllEMPSalary(String fromDate,String toDate)
	{		
		// SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 List<Integer> lstEmpKey=new ArrayList<Integer>();  
			HRQueries query=new HRQueries();
			ResultSet rs = null;		
			try 
			{
				rs=db.executeNonQuery(query.GetAllEMPSalaryQuery(fromDate, toDate));
				while(rs.next())
				{
					lstEmpKey.add(rs.getInt("EMP_KEY"));					
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetAllEMPSalary-->" , ex);
			}
		 return lstEmpKey;
	}
	
	public ListModelList<ActivityModel> GetEMPSalary(EmployeeModel emp,String fromDate,String toDate)
	{
		 ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		 
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GetEMPSalaryQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
					
					obj.setActivityDate(rs.getDate("CREATE_DATE"));
					obj.setActivity("Salary");									
					obj.setActivityItem(getMonth(rs.getInt("SALARY_MONTH")) + "/" +rs.getString("SALARY_YEAR"));
					obj.setFromDate(rs.getDate("FROM_DATE"));
					obj.setToDate(rs.getDate("TO_DATE"));
					obj.setFromHour(df.format(rs.getDate("FROM_DATE")));
					obj.setToHour(df.format(rs.getDate("TO_DATE")));	
					obj.setNoofDays(rs.getString("WORKING_DAYS") +  " Days");
					double BASIC_SALARY=rs.getDouble("BASIC_SALARY");
					double TOTAL_ALLOWANCE=rs.getDouble("TOTAL_ALLOWANCE");
					obj.setAmount(BASIC_SALARY + TOTAL_ALLOWANCE);
					TotalBalance = TotalBalance +(BASIC_SALARY + TOTAL_ALLOWANCE);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("SALARY_STATUS")));
									
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetEMPSalary-->" , ex);
			}
		 return lst;
	}
	
	private String getMonth(int month) 
	{
		String monthName ="";
		Calendar cal=Calendar.getInstance();
		cal.set(2000, month-1,1);
		//int tempmonth = cal.get(Calendar.MONTH);
		SimpleDateFormat newformat = new SimpleDateFormat("MMM");
		monthName =newformat.format(cal.getTime());
		
	    return monthName ;//new DateFormatSymbols().getMonths()[month-1];
	}
	
	public  List<Integer> GETAllEmpABSENCE(String fromDate,String toDate)
	{		
		// SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 List<Integer> lstEmpKey=new ArrayList<Integer>();  
			HRQueries query=new HRQueries();
			ResultSet rs = null;		
			try 
			{
				rs=db.executeNonQuery(query.GETAllEmpABSENCEQuery(fromDate, toDate));
				while(rs.next())
				{
					lstEmpKey.add(rs.getInt("EMP_KEY"));					
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GETAllEmpABSENCE-->" , ex);
			}
		 return lstEmpKey;
	}
	public ListModelList<ActivityModel> GETEmpABSENCE(EmployeeModel emp ,String fromDate,String toDate)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		DateFormat hdf = new SimpleDateFormat("hh:'00' a");
		
		
		 ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GETEmpABSENCEQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
					
					obj.setActivityDate(rs.getDate("REQ_DATE"));
					obj.setActivity("Absence");
					
					obj.setActivityItem(rs.getString("AbsExecuse").equals("Y")?"With Excuse":"Without Excuse");
					String ABS_TYPE=rs.getString("ABS_TYPE").equals("D") ? " Days" : " Hrs.";
					if(ABS_TYPE.equals(" Days"))
					{
					obj.setFromHour(df.format(rs.getDate("ABS_FROM")));
					obj.setToHour(df.format(rs.getDate("ABS_TO")));					
					//obj.setFromDate(rs.getDate("ABS_FROM"));
					//obj.setToDate(rs.getDate("ABS_TO"));
					}
					else
					{
						obj.setFromHour(hdf.format(rs.getDate("ABS_FROM")));
						obj.setToHour(hdf.format(rs.getDate("ABS_TO")));		
						
						//obj.setFromDate(sdf.parse(hdf.format(rs.getDate("ABS_FROM"))));
						//obj.setToDate(rs.getDate("ABS_TO"));
					}
					
					
					obj.setNoofDays(rs.getString("ABS_TOTAL") + ABS_TYPE);
					
					double AMOUNT=rs.getDouble("AMOUNT");					
					obj.setAmount(AMOUNT);
					TotalBalance = TotalBalance +(AMOUNT);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("STATUS")));								
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetEMPSalary-->" , ex);
			}
		 return lst;
	}
	
	public  List<Integer> GetAllEMPLeave(String fromDate,String toDate)
	{		
		// SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 List<Integer> lstEmpKey=new ArrayList<Integer>();  
			HRQueries query=new HRQueries();
			ResultSet rs = null;		
			try 
			{
				rs=db.executeNonQuery(query.GetAllEMPLeaveQuery(fromDate, toDate));
				while(rs.next())
				{
					lstEmpKey.add(rs.getInt("EMP_KEY"));					
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetAllEMPLeave-->" , ex);
			}
		 return lstEmpKey;
	}
	public ListModelList<ActivityModel> GetEMPLeave(EmployeeModel emp ,String fromDate,String toDate)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		 ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		// SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GetEMPLeaveQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
					
					obj.setActivityDate(rs.getDate("REQ_DATE"));
					obj.setActivity("Leave");
					
					obj.setActivityItem(rs.getString("Description"));
					obj.setFromHour(df.format(rs.getDate("FROM_DATE")));
					obj.setToHour(df.format(rs.getDate("TO_Date")));	
									
					obj.setNoofDays(rs.getString("ACTUAL_DAYS") + " Days");
					
					double AMOUNT=rs.getDouble("SALARY");					
					obj.setAmount(AMOUNT);
					TotalBalance = TotalBalance +(AMOUNT);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("STATUS")));
								
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetEMPLeave-->" , ex);
			}
		 return lst;
	}
	
	public  List<Integer> GetAllEMPLoan(String fromDate,String toDate)
	{		
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 List<Integer> lstEmpKey=new ArrayList<Integer>();  
			HRQueries query=new HRQueries();
			ResultSet rs = null;		
			try 
			{
				rs=db.executeNonQuery(query.GetAllEMPLoanQuery(fromDate, toDate));
				while(rs.next())
				{
					lstEmpKey.add(rs.getInt("EMP_KEY"));					
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetAllEMPLoan-->" , ex);
			}
		 return lstEmpKey;
	}
	
	public ListModelList<ActivityModel> GetEMPLoan(EmployeeModel emp,String fromDate,String toDate)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		 ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GetEMPLoanQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
					
					obj.setActivityDate(rs.getDate("Loan_Date"));
					obj.setActivity("Loan");
					
					obj.setActivityItem(rs.getString("Description"));
					//obj.setFromHour(df.format(rs.getDate("FROM_DATE")));
					//obj.setToHour(df.format(rs.getDate("TO_Date")));	
									
					//obj.setNoofDays(rs.getString("ACTUAL_DAYS") + " Days");
					
					double AMOUNT=rs.getDouble("LOAN_AMOUNT");					
					obj.setAmount(AMOUNT);
					TotalBalance = TotalBalance +(AMOUNT);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("STATUS")));
					
					lst.add(obj);
					
					ResultSet rsRepay = null;
					rsRepay=db.executeNonQuery(query.GetLoanRepaymentQuery(emp.getEmployeeKey(), rs.getInt("rec_No")));
					while(rsRepay.next())
					{
						obj=new ActivityModel();
						obj.setEmpKey(emp.getEmployeeKey());
						obj.setEmpNo(emp.getEmployeeNo());
						obj.setEmployeeName(emp.getFullName());
						obj.setEmployeeStatus(emp.getStatus());	
						obj.setEmployeementDate(emp.getEmployeementDate());
						obj.setDepartment(emp.getDepartment());
						obj.setPosition(emp.getPosition());
						obj.setNationality(emp.getCountry());
						
						obj.setActivityDate(rsRepay.getDate("INST_DATE"));
						obj.setActivity("Loan Deduction");
						if(!rsRepay.getString("OPENING_ENTRY").equals("Y"))
						{
						if(rsRepay.getInt("SALARY_MONTH")!=0)
						{
							String monthName=getMonth(rsRepay.getInt("SALARY_MONTH"));
							obj.setActivityItem(monthName + "/" + rsRepay.getString("SALARY_YEAR"));							
						}
						
						}					
						//obj.setFromHour(df.format(rs.getDate("FROM_DATE")));
						//obj.setToHour(df.format(rs.getDate("TO_Date")));	
										
						//obj.setNoofDays(rs.getString("ACTUAL_DAYS") + " Days");
						
						double INST_AMOUNT=rsRepay.getDouble("INST_AMOUNT");					
						obj.setAmount(INST_AMOUNT * -1);
						TotalBalance = TotalBalance +(INST_AMOUNT*-1);
						obj.setBalance(TotalBalance);
						obj.setStatus(getStatusDescreption(rsRepay.getString("STATUS")));										
						lst.add(obj);
					}										
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetEMPLoan-->" , ex);
			}
		 return lst;
	}
	
	public  List<Integer> GetAllEMPEOS(String fromDate,String toDate)
	{		
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 List<Integer> lstEmpKey=new ArrayList<Integer>();  
			HRQueries query=new HRQueries();
			ResultSet rs = null;		
			try 
			{
				rs=db.executeNonQuery(query.GetAllEMPEOSQuery(fromDate, toDate));
				while(rs.next())
				{
					lstEmpKey.add(rs.getInt("EMP_KEY"));					
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetAllEMPEOS-->" , ex);
			}
		 return lstEmpKey;
	}
	
	public ListModelList<ActivityModel> GetEMPEOS(EmployeeModel emp ,String fromDate,String toDate)
	{
		//DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		 ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GetEMPeosQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
										
					obj.setActivityDate(rs.getDate("EOS_DATE"));
					obj.setActivity("EOS");
					String EOS_REASON=rs.getString("EOS_REASON");
					obj.setActivityItem(EOS_REASON.equals("T") ? "Terminate" : "Resign");
					//obj.setFromHour(df.parse(sdf.format(rs.getDate("EOS_DATE"))));
					//obj.setToHour(df.format(rs.getDate("TO_Date")));	
									
					//obj.setNoofDays(rs.getString("ACTUAL_DAYS") + " Days");
					
					double AMOUNT=rs.getDouble("NET2PAY");	
					
					obj.setAmount(AMOUNT);
					TotalBalance = TotalBalance +(AMOUNT);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("STATUS")));
									
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetEMPEOS-->" , ex);
			}
		 return lst;
	}
	
	
	public  List<Integer> GetAllEMPADTrx(String fromDate,String toDate)
	{		
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
		 List<Integer> lstEmpKey=new ArrayList<Integer>();  
			HRQueries query=new HRQueries();
			ResultSet rs = null;		
			try 
			{
				rs=db.executeNonQuery(query.GetAllEMPADTrxQuery(fromDate, toDate));
				while(rs.next())
				{
					lstEmpKey.add(rs.getInt("EMP_KEY"));					
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetAllEMPADTrx-->" , ex);
			}
		 return lstEmpKey;
	}
	
	public ListModelList<ActivityModel> GetEMPADTrx(EmployeeModel emp,String fromDate,String toDate)
	{
		ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		// SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GetEMPADTrxQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
					
					obj.setActivityDate(rs.getDate("AD_DATE"));
					
					String AD_FLAG=rs.getString("AD_FLAG");					
					obj.setActivity(AD_FLAG.equals("A") ? "Addition" : "Deduction");					
					obj.setActivityItem(rs.getString("Description"));
					//obj.setFromHour(df.parse(sdf.format(rs.getDate("EOS_DATE"))));
					//obj.setToHour(df.format(rs.getDate("TO_Date")));									
					//obj.setNoofDays(rs.getString("ACTUAL_DAYS") + " Days");
					
					double AMOUNT=rs.getDouble("AMT_PAYABLE");		
					if(AD_FLAG.equals("D"))
						AMOUNT=AMOUNT*-1;
						
					obj.setAmount(AMOUNT);
					TotalBalance = TotalBalance +(AMOUNT);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("STATUS")));
									
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetEMPADTrx-->" , ex);
			}
		 return lst;
	}
	
	public ListModelList<ActivityModel> GetPAIDSalary(EmployeeModel emp ,String fromDate,String toDate)
	{
		ListModelList<ActivityModel> lst=new ListModelList<ActivityModel>();
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			double TotalBalance=0;
			try 
			{
				rs=db.executeNonQuery(query.GetPAIDSalaryQuery(emp.getEmployeeKey(), fromDate, toDate));
				while(rs.next())
				{
					ActivityModel obj=new ActivityModel();
					obj.setEmpKey(emp.getEmployeeKey());
					obj.setEmpNo(emp.getEmployeeNo());
					obj.setEmployeeName(emp.getFullName());
					obj.setEmployeeStatus(emp.getStatus());	
					obj.setEmployeementDate(emp.getEmployeementDate());
					obj.setDepartment(emp.getDepartment());
					obj.setPosition(emp.getPosition());
					obj.setNationality(emp.getCountry());
					
					obj.setActivityDate(rs.getDate("pvdate"));
														
					obj.setActivity("Salary Paid");	
					
					String monthName ="";
					Calendar cal=Calendar.getInstance();
					cal.set(rs.getInt("SALARY_YEAR"), rs.getInt("SALARY_MONTH") -1 ,1);																			
					obj.setActivityItem(df.format(cal.getTime()));
					//obj.setFromHour(df.parse(sdf.format(rs.getDate("EOS_DATE"))));
					//obj.setToHour(df.format(rs.getDate("TO_Date")));									
					//obj.setNoofDays(rs.getString("ACTUAL_DAYS") + " Days");
					
					double AMOUNT=rs.getDouble("PAIDAMT");							
					AMOUNT=AMOUNT*-1;
						
					obj.setAmount(AMOUNT);
					TotalBalance = TotalBalance +(AMOUNT);
					obj.setBalance(TotalBalance);
					obj.setStatus(getStatusDescreption(rs.getString("STATUS")));
									
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---GetPAIDSalary-->" , ex);
			}
		 return lst;
	}
	
	private String getStatusDescreption(String status)
	{
		String desc="";
		if(status.toUpperCase().equals("A"))
			desc="Approved";
		if(status.toUpperCase().equals("C"))
			desc="Created";
		if(status.toUpperCase().equals("P"))
			desc="Paid";		
		return desc;
	}
	
	//Passport Request
	public PassportModel GetEmployeePassport(int empkey)
	{
		PassportModel obj=new PassportModel();
		
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{				
				rs=db.executeNonQuery(query.GetEmployeePassportQuery(empkey));
				while(rs.next())
				{
					obj.setRecno(rs.getInt("REC_NO"));
					obj.setEmpkey(rs.getInt("EMP_KEY"));
					obj.setPassportNumber(rs.getString("PASSPORT_NO"));
					obj.setIssureCountryId(rs.getInt("ISSUE_CTRY"));
					obj.setIssueCountry(rs.getString("country"));
					obj.setIssuePlaceId(rs.getInt("ISSUE_PLACE"));
					obj.setIssueCity(rs.getString("city"));
					String issueCity=rs.getString("city");
					issueCity=issueCity==null?"":issueCity;
					if(!issueCity.equals(""))
					{
						obj.setIssueCountry(rs.getString("country") + " - " + issueCity);
					}
					
					if(rs.getDate("ISSUE_DATE")!=null)
					obj.setIssueDate(df.format(rs.getDate("ISSUE_DATE")));
					obj.setFileNo(rs.getString("FILE_NO"));
					if(rs.getDate("EXPIRY_DATE")!=null)
					obj.setExpiryDate(df.format(rs.getDate("EXPIRY_DATE")));
					obj.setNotes(rs.getString("NOTES"));
					obj.setStatus("Regular");//(rs.getString("STATUS"));
					obj.setRemaindays(rs.getInt("REM_DAYS"));
					String passLoc=rs.getString("PASSPORT_LOC");
					if(passLoc!=null)
					{
						passLoc=passLoc.equals("C")?"Passport With Company":"Passport With Employee";
					}
					else
					{
						//passLoc="Passport Not Found";
					}
					obj.setPassportLocation(passLoc);					
				}
				
				//checkIfEmployeeHasPassportRequestQuery
				if(obj.getRecno()>0)
				{
					rs = null;
					int RequestCount=0;
					//check if on desktop has Passport Request
					rs=db.executeNonQuery(query.checkIfEmployeeHasPassportRequestQuery(empkey));
					while(rs.next())
					{
					   RequestCount =rs.getInt("RequestCount");
					}
					//check id online has Passport Requst
					if(RequestCount==0)
					{
						rs=db.executeNonQuery(query.checkIfEmployeeHasOnlinePassportRequestQuery(empkey));
						while(rs.next())
						{
						   RequestCount =rs.getInt("RequestCount");
						}
					}
					if(RequestCount>0)
					{
						obj.setStatus("Request already exists");
					}
					else
					{
						
					}
					
				}
				else
				{
					obj.setStatus("Passport is not available");
				}
				
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetEmployeePassport-->" , ex);
			}
		return obj;
	}
	
	public List<ContactModel> GetEmployeeContact(int empkey)
	{
		 List<ContactModel> lst=new ArrayList<ContactModel>();
		 
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			try 
			{				
				rs=db.executeNonQuery(query.GetEmployeeContactQuery(empkey));
				while(rs.next())
				{
					ContactModel obj=new ContactModel();
					obj.setEmpkey(rs.getInt("EMP_KEY"));
					obj.setContactId(rs.getInt("CONTACT_ID"));
					obj.setDescription(rs.getString("DESCRIPTION"));
					obj.setDetails(rs.getString("DETAILS"));
					obj.setNotes(rs.getString("notes1"));
					lst.add(obj);
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetEmployeeContact-->" , ex);
			}		
		 return lst;		 
	}
	
	public SponsorModel GetEmployeeSponsor(int empkey)
	{
		SponsorModel obj=new SponsorModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{				
				rs=db.executeNonQuery(query.GetEmployeeSponsorQuery(empkey));
				while(rs.next())
				{					
					obj.setEmpkey(empkey);
					obj.setSponsorType(rs.getString("SPOTYPE"));
					obj.setSponsorName(rs.getString("SPONSOR_NAME"));
					obj.setCompanyName(rs.getString("COMP_NAME"));
										
					String visaType=rs.getString("VISA_TYPE");
					visaType=visaType==null?"":visaType;
					obj.setVisaType(getVisaType(visaType));
					if(!visaType.equals("R"))
					{
						if(rs.getDate("VISA_EXPDATE")!=null)
							obj.setVisaExpiryDate(df.format(rs.getDate("VISA_EXPDATE")));
					}
					
					String SPOTYPE=rs.getString("SPOTYPE");
					SPOTYPE=SPOTYPE==null?"":SPOTYPE;
					if(SPOTYPE.equals("M") || SPOTYPE.equals("S"))
					{
						obj.setSponsorName(rs.getString("COMP_NAME"));
					}
					else if(SPOTYPE.equals("O"))
					{
						obj.setSponsorName(rs.getString("SPONSOR_NAME"));
					}
					
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetEmployeeSponsor-->" , ex);
			}		
		 return obj;		 
	}
	private String getVisaType(String visaType)
	{
		String type="";
		visaType=visaType==null?"":visaType;
		if(visaType.equals("R"))
			type="Residence";
		if(visaType.equals("V"))
			type="Visit";
		if(visaType.equals("E"))
			type="Employeement";
		if(visaType.equals("T"))
			type="Transit";
		if(visaType.equals("B"))
			type="Business";
		if(visaType.equals("O"))
			type="Others";
		if(visaType.equals("M"))
			type="Mission";
		
		return type;
	}
	
	private int getNewRecNoPassportRequest()
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNewRecNoPassportRequestQuery());
			while(rs.next())
			{
				result=rs.getInt("RecNO")+1;
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---getNewRecNoPassportRequest-->" , ex);
		}	
		return result;
	}
	
	public int insertPassportRequest(int EMP_KEY,Date RequestDate,Date ExpextedReturnDate,int NoOfDays,int Reason,String notes)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		try 
		{
			//int recNo=getNewRecNoPassportRequest();
			result=db.executeUpdateQuery(query.InsertPassportRequestQuery(EMP_KEY, RequestDate, ExpextedReturnDate, NoOfDays, Reason, notes));
			logger.info(" insertPassportRequest Rows >>>>>>> " + result);
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---insertPassportRequest-->" , ex);
		}	
		return result;
	}
	
	//Leave Request
	public EmployeeModel GetEmployeeByEmployeeNumber(String empNO)
	{
		EmployeeModel obj=new EmployeeModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetEmployeeByEmployeeNumberQuery(empNO));
			while(rs.next())
			{
				obj.setEmployeeKey(rs.getInt("Emp_Key"));
				obj.setCompanyID(rs.getInt("Comp_Key"));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetEmployeeByEmployeeNumber-->" , ex);
		}	
		return obj;
	}
	
	public CompSetupModel getLeaveCompanySetup(int compkey)
	{
		CompSetupModel obj=new CompSetupModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{						
				rs=db.executeNonQuery(query.GetCompanySetupQuery(compkey));
				while(rs.next())
				{										
					obj.setAllowMinusLeave(rs.getString("ALLOWMINUSLEAVE"));
					obj.setMaxLeaveDays(rs.getDouble("MAXLEAVEDAYS"));
					obj.setCalculateActualLeaveDays(rs.getString("CalculateActualLeaveDays"));
					obj.setAddLeaveSalary2TS(rs.getString("AddLeaveSalary2TS"));
					obj.setLeaveBasis(rs.getInt("LEAVE_BASIS"));
					obj.setUseEncash(rs.getString("USE_ENCASH"));
					obj.setLeaveBeforeReturn(rs.getString("LEAVE_BEFORE_RETURN"));	
					obj.setActivateEmail(rs.getString("EMAIL_REQD")==null?"":rs.getString("EMAIL_REQD"));
					obj.setEmail1(rs.getString("EDMAIL")==null?"":rs.getString("EDMAIL"));
					obj.setEmail2(rs.getString("Email2")==null?"":rs.getString("Email2"));
					obj.setEmail3(rs.getString("email3")==null?"":rs.getString("email3"));
					
					obj.setCompanyName(rs.getString("COMP_NAME")==null?"":rs.getString("COMP_NAME"));
					obj.setPhone1(rs.getString("PHONE1")==null?"":rs.getString("PHONE1"));
					obj.setAddress(rs.getString("PO_BOX")==null?"":rs.getString("PO_BOX"));
					obj.setFax(rs.getString("FAX1")==null?"":rs.getString("FAX1"));
					
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---getLeaveCompanySetup-->" , ex);
			}		
		
		return obj;
	}
	public LeaveModel GetEmployeeLeave(int LEAVE_ID,int empKey,String type)
	{
		LeaveModel obj=new LeaveModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{
				if(type.equals("Haj"))
				rs=db.executeNonQuery(query.GetEmployeeLeaveQuery(LEAVE_ID, empKey));
				else
				rs=db.executeNonQuery(query.checkIfLeaveTakenQuery(LEAVE_ID, empKey));	
				while(rs.next())
				{										
					obj.setRecNO(rs.getInt("REC_NO"));
					obj.setLeaveid(rs.getInt("LEAVE_ID"));
					obj.setEmpKey(rs.getInt("EMP_KEY"));
					obj.setStatus(rs.getString("STATUS"));
					obj.setLeaveTypeDesc(rs.getString("Description"));					
				}
				
				if(obj.getRecNO()==0)
				{
					//check Online Leave Request
					rs=db.executeNonQuery(query.checkIfOnlineLeaveTakenQuery(LEAVE_ID, empKey));
					while(rs.next())
					{
						obj.setRecNO(rs.getInt("ID"));
						obj.setStatus(rs.getString("STATUS"));
						obj.setEmpKey(rs.getInt("EMP_KEY"));
						obj.setLeaveTypeDesc(rs.getString("Description"));		
					}
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetEmployeeLeave-->" , ex);
			}		
		
		return obj;
	}
	
	public LeaveModel GetLastEmployeeLeaveQuery(int empKey)
	{
		LeaveModel obj=new LeaveModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{			
				rs=db.executeNonQuery(query.GetLastEmployeeLeaveQuery(empKey));			
				while(rs.next())
				{										
					obj.setRecNO(rs.getInt("REC_NO"));
					obj.setLeaveid(rs.getInt("LEAVE_ID"));
					obj.setEmpKey(rs.getInt("EMP_KEY"));
					obj.setStatus(rs.getString("STATUS"));
					obj.setLeaveTypeDesc(rs.getString("Description"));
					obj.setNoOfDays(rs.getInt("ACTUAL_DAYS"));
					//obj.setReturnDate(df.format(rs.getDate("RETURN_DATE")));
					obj.setReturnDate(rs.getDate("RETURN_DATE"));
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetLastEmployeeLeave-->" , ex);
			}		
		
		return obj;
	}
	
	public List<HRListValuesModel> getEmployeeLeavesAllowed(int empKey,String fromDate,String type)
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
				rs=db.executeNonQuery(query.getEmployeeLeavesAllowedQuery(empKey, fromDate));
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("LEAVE_KEY"));					
					obj.setEnDescription(rs.getString("DESCRIPTION"));
					obj.setArDescription(rs.getString("ARABIC"));					
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getEmployeeLeavesAllowed-->" , ex);
			}
		 return lst;
	}
	public LeaveModel GetOverLapDateEmployeeLeave(int empKey,String fromDate,String toDate)
	{
		LeaveModel obj=new LeaveModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{			
				rs=db.executeNonQuery(query.GetOverLapDateEmployeeLeaveQuery(empKey, fromDate, toDate));			
				while(rs.next())
				{										
					obj.setRecNO(rs.getInt("REC_NO"));				
					obj.setStatus(rs.getString("ABS_TYPE"));								
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetOverLapDateEmployeeLeave-->" , ex);
			}		
		
		return obj;
	}
	
	
	public LeaveModel checkDupliacteLeaveQuery(int empKey,String fromDate,String toDate)
	{
		LeaveModel obj=new LeaveModel();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{			
				rs=db.executeNonQuery(query.checkDupliacteLeaveQuery(empKey, fromDate, toDate));			
				while(rs.next())
				{										
					obj.setRecNO(rs.getInt("id"));				
					//obj.setStatus(rs.getString("ABS_TYPE"));								
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---GetOverLapDateEmployeeLeave-->" , ex);
			}		
		
		return obj;
	}
	
	public double getEmployeeOutStandingLoans(int empKey)
	{		
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		double amount=0;
			try 
			{			
				rs=db.executeNonQuery(query.getEmployeeOutStandingLoansQuery(empKey));			
				while(rs.next())
				{										
					amount= rs.getDouble("OutstandingAMT");																	
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---getEmployeeOutStandingLoans-->" , ex);
			}				
		return amount;
	}
	
	public int updateLeaveRequest(int Id,Date startDate,Date ExpextedReturnDate,int reasonId,String notes,int leaveTypeId,String other_leave_reason,
			String checkUseEncashValue)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		try 
		{			
			result=db.executeUpdateQuery(query.updateLeaveRequestQuery(Id, startDate, ExpextedReturnDate, reasonId, notes, leaveTypeId, other_leave_reason, checkUseEncashValue));			
			logger.info(" updateLeaveRequest Rows >>>>>>> " + result);
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---updateLeaveRequest-->" , ex);
		}	
		return result;
	}
	
	public int InsertLeaveRequest(int EMP_KEY,Date RequestDate,Date ExpextedReturnDate,int reasonId,String notes,int leaveTypeId,String other_leave_reason,String checkUseEncashValue,Date requestDate,String status,String attachment_path)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		try 
		{			
			result=db.executeUpdateQuery(query.InsertLeaveRequestQuery(EMP_KEY, RequestDate, ExpextedReturnDate, reasonId, notes,leaveTypeId,other_leave_reason,checkUseEncashValue,requestDate,status,attachment_path));
			logger.info(" InsertLeaveRequest Rows >>>>>>> " + result);
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---InsertLeaveRequest-->" , ex);
		}	
		return result;
	}
	
	//Loan Request
	public double[] getEmployeeSalary(int empKey,Date requestDate)
	{	
		double[] result=new double[2];
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		double amount=0;
			try 
			{			
				rs=db.executeNonQuery(query.getEmployeeSalaryQuery(empKey, requestDate));		
				while(rs.next())
				{										
					result[0]= rs.getDouble("BasicSal");
					result[1]= rs.getDouble("TotAllowance");
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---getEmployeeSalary-->" , ex);
			}				
		return result;
	}
	
	public String getEmployeeLastSalary(int empKey,String status)
	{
		String result="";
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{			
			rs=db.executeNonQuery(query.getEmployeeLastSalaryQuery(empKey, status));	
			while(rs.next())
			{										
				Date tmp=rs.getDate("MaxDate");
				if(tmp!=null)
				{
					Calendar c = Calendar.getInstance();
					c.setTime(tmp);
					result=getMonth(c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);					
					return result;
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---getEmployeeSalary-->" , ex);
		}		
		return result;
	}
	
	public int InsertLoanRequest(int EMP_KEY,int reasonId,double loanAmount,int month,int year,int installNo,double installAmount, String notes)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		try 
		{		
			result=db.executeUpdateQuery(query.InsertLoanRequestQuery(EMP_KEY, reasonId, loanAmount, month, year, installNo, installAmount, notes));
			logger.info(" InsertLoanRequest Rows >>>>>>> " + result);
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---InsertLoanRequest-->" , ex);
		}	
		return result;
	}
	
	public List<LoanModel> getLoanHistory(int empKey)
	{
		List<LoanModel> lst=new ArrayList<LoanModel>();
		
		HRQueries query=new HRQueries();
		ResultSet rs = null;
			try 
			{			
				rs=db.executeNonQuery(query.getLoanHistoryQuery(empKey));			
				while(rs.next())
				{	
					LoanModel obj=new LoanModel();
					obj.setId(rs.getInt("ID"));
					obj.setReason(rs.getString("DESCRIPTION"));
					obj.setInstallAmount(rs.getDouble("INSTALLMENTS_AMOUNT"));
					obj.setLoanAmount(rs.getDouble("LOAN_AMOUNT"));
					obj.setLoanDate(rs.getDate("DATE"));
					if(rs.getDate("DATE")!=null)
					obj.setLoanDateString(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("DATE")));
					obj.setMonth(rs.getInt("START_FROM_MONTH"));
					obj.setNoOfInstallment(rs.getInt("INSTALLMENTS_NO"));				
					obj.setNote(rs.getString("NOTES")==null?"":rs.getString("NOTES"));
					String status=rs.getString("STATUS");
					if(status.equals("C"))
						status="Created";
					if(status.equals("A"))
						status="Approved";
					if(status.equals("R"))
						status="Rejected";
					obj.setStatus(status);
					obj.setYear(rs.getInt("START_FROM_YEAR"));					
					lst.add(obj);
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in HRData---getLoanHistory-->" , ex);
			}		
		
		return lst;
	}
	
	//Import File
	public int getMaxID(String tableName,String fieldName)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
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
			logger.error("error in HRData---getMaxID-->" , ex);
		}	
		return result;
	}
	
	public int addNewHRListValue(String DeptName,String DeptNameAr,String fieldName,String fieldID)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		try 
		{	
			int newID=getMaxID("HRLISTVALUES", "ID");
			result=db.executeUpdateQuery(query.addNewHRListValueQuery(newID, DeptName, DeptNameAr, fieldName, fieldID));
			logger.info(" addNewHRListValue Rows >>>>>>> " + result);
			result=newID;
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---addNewHRListValue-->" , ex);
		}	
		return result;
	}
	public int updateHRListValue(int fieldID,String DeptName,String DeptNameAr)
	{
		int result=0;
		
		HRQueries query=new HRQueries();
		try 
		{			
			result=db.executeUpdateQuery(query.updateHRListValueQuery(fieldID,DeptName, DeptNameAr));						
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---updateHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int insertNewEmployee(EmployeeModel obj)
	{
		int result=0;		
		HRQueries query=new HRQueries();
		try 
		{	
			int newID=getMaxID("EMPMAST", "emp_key");
			obj.setEmployeeKey(newID);
			result=db.executeUpdateQuery(query.insertNewEmployeeQuery(obj));
			logger.info(" insertNewEmployee Rows >>>>>>> " + result);
			result=newID;
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---insertNewEmployee-->" , ex);
		}	
		return result;
	}
	
	
	//Edit Employee
	public String GetMaXEMPNO()
	{
		String GENERATE_EMPNO="";
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.GetGenerateNoTypeQuery());
			while(rs.next())
			{
				GENERATE_EMPNO=rs.getString("GENERATE_EMPNO");
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetMaXEMPNO-->" , ex);
		}
		return GENERATE_EMPNO;
	}
	public String GetEmpNoPrefix(int compKey)
	{
		String PREFIX_TEXT="";
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.GetEmpNoPrefixQuery(compKey));
			while(rs.next())
			{
				PREFIX_TEXT=rs.getString("PREFIX_TEXT");
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetEmpNoPrefix-->" , ex);
		}
		return PREFIX_TEXT;
	}
	public String GetSystemSerialNos(String serialField)
	{
		String LastNumber="";
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.SystemSerialNosQuery(serialField));
			while(rs.next())
			{
				LastNumber=rs.getString("LastNumber");
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetSystemSerialNos-->" , ex);
		}
		return LastNumber;
	}
	
	public List<Integer> GetEmployeeAgeRange(int compKey)
	{
		List<Integer> lstAges=new ArrayList<Integer>();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.GetEmployeeAgeRangeQuery(compKey));
			while(rs.next())
			{
				lstAges.add(rs.getInt("AGE_FROM"));
				lstAges.add(rs.getInt("AGE_TO"));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetEmployeeAgeRange-->" , ex);
		}
		return lstAges;
	}
	
	public Date getCompanyStartBussiness(int compKey)
	{
		Date result=null;
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			int YEAR_BUSINESS=0;
			int MONTH_BUSINESS=0;
			rs=db.executeNonQuery(query.getCompanyStartBussinessQuery(compKey));
			while(rs.next())
			{
				YEAR_BUSINESS=rs.getInt("YEAR_BUSINESS");
				MONTH_BUSINESS=rs.getInt("MONTH_BUSINESS");
			}
			 Calendar c = Calendar.getInstance();
			 c.set(YEAR_BUSINESS,MONTH_BUSINESS-1,1);
			 result=c.getTime();
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---getCompanyStartBussiness-->" , ex);
		}
		return result;
	}
	public List<EmployeeModel> checkEmployeeNoExist(int compKey)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.checkEmployeeNoExistQuery(compKey));
			while(rs.next())
			{
				EmployeeModel model=new EmployeeModel();
				model.setEmployeeNo(rs.getString("EMP_NO"));
				model.setEmployeeKey(rs.getInt("emp_key"));
				model.setFullName(rs.getString("english_full"));
				lst.add(model);			
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---checkEmployeeNoExist-->" , ex);
		}
		return lst;
	}
	
	public int UpdateSystemSerialNo(String SerialField,String LastNumber,boolean isNew)
	{
		int result=0;		
		HRQueries query=new HRQueries();
		try 
		{				
			result=db.executeUpdateQuery(query.UpdateSystemSerialNoQuery(SerialField, LastNumber,isNew));
			result=1;
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---UpdateSystemSerialNo-->" , ex);
		}	
		return result;
	}
	
	public EmployeeModel getEmployeeByKey(int empKey)
	{
		EmployeeModel obj=new EmployeeModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			ResultSet rs1 = null;
			rs=db.executeNonQuery(query.GetEmployeeByEmployeeKeyQuery(empKey));
			while(rs.next())
			{
				obj.setEmployeeKey(empKey);
				obj.setCompanyID(rs.getInt("COMP_KEY"));
				obj.setEmployeeType(rs.getString("EMP_TYPE"));
				obj.setDepartmentID(rs.getInt("DEP_ID"));
				obj.setPositionID(rs.getInt("POS_ID"));
				obj.setEmployeeNo(rs.getString("EMP_NO"));
				obj.setStandardNo(rs.getString("STD_NO"));
				
				obj.setEnFirstName(rs.getString("ENGLISH_FIRST"));
				obj.setEnMiddleName(rs.getString("ENGLISH_MIDDLE"));
				obj.setEnLastName(rs.getString("ENGLISH_LAST"));
				
				obj.setArFirstName(rs.getString("ARABIC_FIRST"));
				obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
				obj.setArLastName(rs.getString("ARABIC_LAST"));
				
				obj.setNationalityID(rs.getInt("NAT_ID"));
				obj.setSexId(rs.getInt("SEX_ID"));
				obj.setBloodType(rs.getInt("BLOOD_TYPE"));
				obj.setMaritalID(rs.getInt("MARITAL_ID"));
				obj.setStatusId(rs.getInt("STATUS"));
				obj.setPlaceOfBirth(rs.getString("BIRTH_PLACE"));
				obj.setCountryOfBirth(rs.getInt("BIRTH_COUNTRY"));
				obj.setReligionId(rs.getInt("RELIGION_ID"));
				obj.setLocal(rs.getString("NAT_FLAG").equals("L")?"1":"0");
				obj.setDateOfBirth(rs.getDate("Birth_Date"));
				obj.setAge(rs.getString("Age"));
				obj.setEmployeementDate(rs.getDate("EMPLOYEEMENT_DATE"));
				
				  if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
				  {
					  obj.setQblistEmpKey(rs.getInt("qblistemp_key"));
				  }
				
				rs1=db.executeNonQuery(query.getEmail(empKey));
				while(rs1.next())
				{
					obj.setEmail(rs1.getString("details")==null?"":rs1.getString("details"));
				}
				
				
			}
		}
		
		
		
		catch (Exception ex) 
		{
			logger.error("error in HRData---getEmployeeByKey-->" , ex);
		}	
		return obj;
	}
	
	public EmployeeModel GetEmployeeDeatailsByEmployeeKeyQuery(int empKey)
	{
		EmployeeModel obj=new EmployeeModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			ResultSet rs1 = null;
			rs=db.executeNonQuery(query.GetEmployeeDeatailsByEmployeeKeyQuery(empKey));
			while(rs.next())
			{
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setEmployeeNo(rs.getString("EmployeeNo")==null?"":rs.getString("EmployeeNo"));
				obj.setFullName(rs.getString("FullName")==null?"":rs.getString("FullName"));
				obj.setArabicName(rs.getString("ArabicName")==null?"":rs.getString("ArabicName"));
				obj.setDepartmentID(rs.getInt("DepartmentID"));
				obj.setDepartment(rs.getString("Department")==null?"":rs.getString("Department"));
				obj.setArabicDepartment(rs.getString("ArabicDepartment")==null?"":rs.getString("ArabicDepartment"));
				obj.setPositionID(rs.getInt("PositionID"));
				obj.setPosition(rs.getString("Position")==null?"":rs.getString("Position"));
				obj.setArabicPosition(rs.getString("ArabicPosition")==null?"":rs.getString("ArabicPosition"));
				obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"Local":rs.getString("Country"));
				obj.setCompanyName(rs.getString("CompanyName"));
				obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				obj.setEnFirstName(rs.getString("EnglishFirstName")==null?"":rs.getString("EnglishFirstName"));
				obj.setEnMiddleName(rs.getString("EnglishMiddleName")==null?"":rs.getString("EnglishMiddleName"));
				obj.setEnLastName(rs.getString("EnglishLastName"));
				obj.setSupervisorId(rs.getInt("SupervisorID"));
				obj.setSupervisorName(rs.getString("supervisorName")==null?"":rs.getString("supervisorName"));
				obj.setWorkGroupName(rs.getString("workergroupname")==null?"":rs.getString("workergroupname"));
				obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
			//	obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
			//	obj.setArLastName(rs.getString("ARABIC_LAST"));
				obj.setEmployeementDate(rs.getDate("EmployeementDate"));
				obj.setGender(rs.getString("Sex")==null?"":rs.getString("Sex"));
				obj.setMaritalStatus(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setMarital(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setEmployeeStatus(rs.getString("Status")==null?"":rs.getString("Status"));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetEmployeeDeatailsByEmployeeKeyQuery-->" , ex);
		}	
		return obj;
	}
	
	public int editEmployees(EmployeeModel obj)
	{
		int result=0;
		ResultSet rs1 = null;
		HRQueries query=new HRQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.editEmployeesQuery(obj));	
			if(result>0)
			{
			rs1=db.executeNonQuery(query.getEmail(obj.getEmployeeKey()));
			if(!rs1.next() )
			{
				db.executeUpdateQuery(query.addEmial(obj.getEmployeeKey(),obj.getEmail()));
			}
			else
			{
				db.executeUpdateQuery(query.updateEmail(obj.getEmployeeKey(),obj.getEmail()));
			}

			if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
			{
				db.executeUpdateQuery(query.updateEmployeesHBATableQuery(obj));
				db.executeUpdateQuery(query.updateQBListEmployee(obj));
			}
			result=1;
			}
			
		}
		catch (Exception ex) {
			logger.error("error in HRData---editEmployees-->" , ex);
		}
		return result;		
	}
	
	public String getEmployeeEmail(int empKey)
	{
		String email="";
		ResultSet rs = null;
		HRQueries query=new HRQueries();	
		try 
		{			
			rs=db.executeNonQuery(query.getEmail(empKey));
			while(rs.next())
			{
				email=rs.getString("details");
			}
		}
		catch (Exception ex) {
			logger.error("error in HRData---getEmployeeEmail-->" , ex);
		}
		
		return email;
	}
	public void addEmployeeEmail(int empKey,String email)
	{		
		HRQueries query=new HRQueries();	
		try 
		{			
			db.executeUpdateQuery(query.addEmial(empKey, email));			
		}
		catch (Exception ex) {
			logger.error("error in HRData---addEmployeeEmail-->" , ex);
		}			
	}
	
	public int addNewEmployee(EmployeeModel obj)
	{
		int result=0;		
		HRQueries query=new HRQueries();
		try 
		{	
			int newID=getMaxID("EMPMAST", "emp_key");
			if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
			{
			int qblistId=getMaxID("QBlists", "recNo");
			int hbaEmpId=getMaxID("employee", "emp_key");
			obj.setQblistEmpKey(qblistId);
			//obj.setHbaEmpKey(hbaEmpId);
			}
			
			obj.setEmployeeKey(newID);
			result=db.executeUpdateQuery(query.addNewEmployeeQuery(obj));
			if(result>0)
			{
			db.executeUpdateQuery(query.addEmial(obj.getEmployeeKey(),obj.getEmail()));
			
			if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
			{
				db.executeUpdateQuery(query.addNewEmployeeinHBATableQuery(obj));
				db.executeUpdateQuery(query.addQBListEmployee(obj));
			}
			result=1;
			}
			
			logger.info(" insertNewEmployee Rows >>>>>>> " + result);
			
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---addNewEmployee-->" , ex);
		}	
		return result;
	}
	
	public LeaveapproveOrRejectModel  getLeaveRequestById(int leaveId)
	{
		LeaveapproveOrRejectModel obj=new LeaveapproveOrRejectModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;					
			rs=db.executeNonQuery(query.getLeaveRequestByIdQuery(leaveId));					
			while(rs.next())
			{				
				obj.setId(rs.getInt("id"));
				obj.setEmp_key(rs.getInt("emp_KEY"));
				String stus=rs.getString("status")==null?"":rs.getString("status");
				if(stus.trim().equalsIgnoreCase("C"))
				{
				obj.setStatus("Created Online");
				}
				else if(stus.trim().equalsIgnoreCase("D"))
				{
					obj.setStatus("Rejected Online");
				}
				else if(stus.trim().equalsIgnoreCase("SA"))
				{
					obj.setStatus("Supervisor Approved Online");
				}
				else if(stus.trim().equalsIgnoreCase("SSA"))
				{
					obj.setStatus("Super-Supervisor Approved Online");
				}
				else
				{
					obj.setStatus("");
				}
				obj.setLeaveCreateDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date")));
				obj.setLeaveStartDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("leave_start_date")));
				obj.setLeaveEndDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("leave_end_date")));
				
				obj.setLeaveFromDate(rs.getDate("leave_start_date"));
				obj.setLeaveToDate(rs.getDate("leave_end_date"));
				
				obj.setLeaveId(rs.getInt("LEAVE_TYPE_ID"));
				obj.setReasonId(rs.getInt("REASON_ID"));
				
				obj.setLeaveType(rs.getString("leaveType")==null?"":rs.getString("leaveType"));
				obj.setLeaveReason(rs.getString("leaveReason")==null?"":rs.getString("leaveReason"));
				obj.setOtherLeaveReason(rs.getString("OTHER_LEAVE_REASON")==null?"":rs.getString("OTHER_LEAVE_REASON"));
						
				obj.setSupervisorID(rs.getInt("supervisor"));
				obj.setEmpName(rs.getString("empName")==null?"":rs.getString("empName"));
				obj.setEmpNo(rs.getString("emp_No")==null?"":rs.getString("emp_No"));
				obj.setReason(rs.getString("notes").equalsIgnoreCase("null")?"":rs.getString("notes"));
				obj.setAttachment_path(rs.getString("ATTACHMENT_PATH")==null?"":rs.getString("ATTACHMENT_PATH"));
				obj.setEnCashStatus(rs.getString("ENCASH_STATUS"));
				
				/*obj.setLeaveDays(activity.GetLeaveBalanceDays(obj.getEmp_key(), rs.getInt("Leave_type_id"), rs.getDate("leave_start_date"),compKey));*/
			}
		}
		
		catch (Exception ex) 
		{
			logger.error("error in HRData---getLeaveRequestById-->" , ex);
		}	
		return obj;
	}
	
	public List<LeaveapproveOrRejectModel> leaveapproveOrRejectList(int supervisorID,int compKey,String status,boolean isSuper_supervisor)
	{
		List<LeaveapproveOrRejectModel> lst=new ArrayList<LeaveapproveOrRejectModel>();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			ResultSet rs1 = null;
			EmployeeActivity activity=new EmployeeActivity();
			rs=db.executeNonQuery(query.leaveapproveOrRejectList(supervisorID,compKey,status,isSuper_supervisor));
			while(rs.next())
			{
				LeaveapproveOrRejectModel obj=new LeaveapproveOrRejectModel();
				obj.setId(rs.getInt("id"));
				obj.setEmp_key(rs.getInt("emp_KEY"));
				String stus=rs.getString("status")==null?"":rs.getString("status");
				if(stus.trim().equalsIgnoreCase("C"))
				{
				obj.setStatus("Created Online");
				}
				else if(stus.trim().equalsIgnoreCase("D"))
				{
					obj.setStatus("Rejected Online");
				}
				else if(stus.trim().equalsIgnoreCase("SA"))
				{
					obj.setStatus("Supervisor Approved Online");
				}
				else if(stus.trim().equalsIgnoreCase("SSA"))
				{
					obj.setStatus("Super-Supervisor Approved Online");
				}
				else
				{
					obj.setStatus("");
				}
				obj.setLeaveCreateDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("date")));
				obj.setLeaveStartDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("leave_start_date")));
				obj.setLeaveEndDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("leave_end_date")));
				obj.setLeaveType(rs.getString("leaveType")==null?"":rs.getString("leaveType"));
				obj.setLeaveReason(rs.getString("leaveReason")==null?"":rs.getString("leaveReason"));
				if(obj.getLeaveReason().equals(""))
				{
					obj.setLeaveReason(rs.getString("OTHER_LEAVE_REASON")==null?"":rs.getString("OTHER_LEAVE_REASON"));
				}
				obj.setSupervisorID(rs.getInt("supervisor"));
				obj.setEmpName(rs.getString("empName")==null?"":rs.getString("empName"));
				obj.setEmpNo(rs.getString("emp_No")==null?"":rs.getString("emp_No"));
				obj.setReason(rs.getString("notes").equalsIgnoreCase("null")?"":rs.getString("notes"));
				obj.setAttachment_path(rs.getString("ATTACHMENT_PATH")==null?"":rs.getString("ATTACHMENT_PATH"));
				/*obj.setLeaveDays(activity.GetLeaveBalanceDays(obj.getEmp_key(), rs.getInt("Leave_type_id"), rs.getDate("leave_start_date"),compKey));*/
				lst.add(obj);
			}
		}
		
		catch (Exception ex) 
		{
			logger.error("error in HRData---leaveapproveOrRejectList-->" , ex);
		}	
		return lst;
	}
	
	
	public int updateLeaveForApproveOrReject(LeaveapproveOrRejectModel obj)
	{
		int result=0;		
		HRQueries query=new HRQueries();
		try 
		{	
			result=db.executeUpdateQuery(query.updateLeaveForApproveOrReject(obj));
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---updateLeaveForApproveOrReject-->" , ex);
		}	
		return result;
	}
	
	
	public LeaveExpiryModel getEmployeePassportExpiryDate(String status, int empKey)
	{
		LeaveExpiryModel  expryDate=new LeaveExpiryModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeePassportExpiryDate(status,empKey));
			while(rs.next())
			{
				if(rs.getDate("expiry_date")!=null)
				{
					expryDate.setExprityDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expiry_date")));
					expryDate.setExprityDate(rs.getDate("expiry_date"));
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---getEmployeePassportExpiryDate-->" , ex);
		}
		return expryDate;
	}
	
	public LeaveExpiryModel getEmployeeResidanceExpiryDate(String status, int empKey)
	{
		LeaveExpiryModel  expryDate=new LeaveExpiryModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeResidanceExpiryDate(status,empKey));
			while(rs.next())
			{
				if(rs.getDate("expiry_date")!=null)
				{
					expryDate.setExprityDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expiry_date")));
					expryDate.setExprityDate(rs.getDate("expiry_date"));
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---getEmployeeResidanceExpiryDate-->" , ex);
		}
		return expryDate;
	}
	
	
	public LeaveExpiryModel getEmployeeLabourCardExpiryDate(String status, int empKey)
	{
		LeaveExpiryModel  expryDate=new LeaveExpiryModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeLabourCardExpiryDate(status,empKey));
			while(rs.next())
			{
				if(rs.getDate("expiry_date")!=null)
				{
				expryDate.setExprityDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expiry_date")));
				expryDate.setExprityDate(rs.getDate("expiry_date"));
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---getEmployeeLabourCardExpiryDate-->" , ex);
		}
		return expryDate;
	}
	
	
	public ExpirySettingsModel getexpirySettings(int compId)
	{
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			ExpirySettingsModel obj=new ExpirySettingsModel();
			try 
			{
				rs=db.executeNonQuery(query.getexpirySettings(compId));
				while(rs.next())
				{
					obj.setStrLeaveVisa(rs.getString("leaveVisa")==null?"":rs.getString("leaveVisa"));		
					obj.setStrLeavePassport(rs.getString("leavePassport")==null?"":rs.getString("leavePassport"));		
					obj.setStrLeaveResidence(rs.getString("leaveResidence")==null?"":rs.getString("leaveResidence"));		
					obj.setStrLeaveLabourCrad(rs.getString("leaveLabourCard")==null?"":rs.getString("leaveLabourCard"));		
					obj.setStrLeaveGovtId(rs.getString("leaveGovtId")==null?"":rs.getString("leaveGovtId"));		
					obj.setStrLeaveHealthCard(rs.getString("leaveHealthCard")==null?"":rs.getString("leaveHealthCard"));		
					obj.setStrLoanVisa(rs.getString("loanVisa")==null?"":rs.getString("loanVisa"));		
					obj.setStrLoanPassport(rs.getString("loanPassport")==null?"":rs.getString("loanPassport"));		
					obj.setStrLoanResidence(rs.getString("loanResidence")==null?"":rs.getString("loanResidence"));		
					obj.setStrLoanLabourCrad(rs.getString("loanLabourCard")==null?"":rs.getString("loanLabourCard"));		
					obj.setStrLoanGovtId(rs.getString("loanGovtId")==null?"":rs.getString("loanGovtId"));		
					obj.setStrLoanGovtId(rs.getString("loanVisaHeathCard")==null?"":rs.getString("loanVisaHeathCard"));	
					obj.setTotalDays(rs.getInt("noOfdays"));	
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getexpirySettings-->" , ex);
			}
		 return obj;
	}
	
	
	public int saveExpirySettings(ExpirySettingsModel obj,int compId)
	{
		int result=0;		
		HRQueries query=new HRQueries();
		try 
		{	
			db.executeUpdateQuery(query.deleteExpirySettings(compId));
			result=db.executeUpdateQuery(query.saveExpirySettings(obj,compId));
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---saveExpirySettings-->" , ex);
		}	
		return result;
	}
	
	
	public EmailSettingsModel getEmailSettings(int compId)
	{
		 //SQLDBHandler db=new SQLDBHandler("HRONLINE");
			HRQueries query=new HRQueries();
			ResultSet rs = null;
			EmailSettingsModel obj=new EmailSettingsModel();
			try 
			{
				rs=db.executeNonQuery(query.getEmailSettings(compId));
				while(rs.next())
				{
					obj.setEnalbeMail(rs.getString("EMAIL_REQD")==null?"":rs.getString("EMAIL_REQD"));		
					obj.setEmail1(rs.getString("EDMAIL")==null?"":rs.getString("EDMAIL"));		
					obj.setEmail2(rs.getString("Email2")==null?"":rs.getString("Email2"));		
					obj.setEmail3(rs.getString("email3")==null?"":rs.getString("email3"));	
					obj.setEmailhost("");//rs.getString("emailhost")==null?"":rs.getString("emailhost"));
				}
			}
			catch (Exception ex) {
				logger.error("error in HRData---getEmailSettings-->" , ex);
			}
		 return obj;
	}
	
	
	public int updateEmailSettings(EmailSettingsModel obj,int compId)
	{
		int result=0;		
		HRQueries query=new HRQueries();
		try 
		{	
			result=db.executeUpdateQuery(query.updateEmailSettings(obj,compId));
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---updateEmailSettings-->" , ex);
		}	
		return result;
	}
	
	public EmployeeModel GetSalarySummary(int empKey)
	{
		EmployeeModel obj=new EmployeeModel();
		try
		{
			HRQueries query=new HRQueries();
			ResultSet rs = null;			
			rs=db.executeNonQuery(query.GetSalarySummaryQuery(empKey));
			while(rs.next())
			{
				obj.setEmployeeKey(empKey);
				obj.setBasicSalary(rs.getDouble("BasicSal"));
				obj.setTotalAllowance(rs.getDouble("TotAllowance"));
				obj.setTotalSalary(obj.getBasicSalary() + obj.getTotalAllowance());				
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---GetSalarySummary-->" , ex);
		}	
		return obj;
	}
	
	
}
