package restdata;


import home.QuotationAttachmentModel;
import hr.EmployeeActivity;
import hr.HRQueries;
import hr.model.LeaveapproveOrRejectModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import admin.TasksModel;
import common.CookieController;
import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;
import model.CompanyDBModel;
import model.EmployeeModel;
import model.ExpirySettingsModel;
import model.LeaveExpiryModel;
import model.TimeSheetDataModel;

public class HRServiceData {

	//@Context private HttpServletRequest servletRequest;
	
	private Logger logger = Logger.getLogger(this.getClass());	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	WebusersModel dbUser=null;
	HRServiceQuieries hrQuieries=new HRServiceQuieries();
	int employeeKey=0;
	CookieController cookieController=new CookieController();
	
	public HRServiceData(HttpServletRequest servletRequest) throws Exception
	{
		//try
		{				
			HttpSession sess = servletRequest.getSession(true);	    	  				
			if(sess==null)
			{
				 throw new Exception("Session Expired !!");
			}
			
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				employeeKey=dbUser.getEmployeeKey();
				//HRQueries query=new HRQueries();
				String sql="Select * from companiesdb where dbtype='HR' and companyid=" + dbUser.getCompanyid() ;
				rs=mysqldb.executeNonQuery(sql);
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
			else
			{
				 throw new Exception("Session Expired !!");
			}
		}
		/*catch (Exception ex) 
		{
			logger.error("error in HRServiceData---Init-->" , ex);			
		}*/
	}
	
	public EmployeeModel getEmployeeProfile(int empkey)
	{
		EmployeeModel obj=new EmployeeModel();
		ResultSet rs = null;
		try 
		{
			
			//Date createDateNew;
			//Calendar c = Calendar.getInstance();
			//createDateNew=df.parse(sdf.format(c.getTime()));
			empkey=employeeKey;		
			rs=db.executeNonQuery(hrQuieries.getEmployeeInfoQuery(empkey));
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
				obj.setCompanyID(rs.getInt("CompKey"));
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
				}				
			}
			
			obj.setEmail(getEmployeeEmail(obj.getEmployeeKey()));
			
			ExpirySettingsModel objExpiry=getexpirySettings(obj.getCompanyID());
			
			LeaveExpiryModel  pssprtExpry=new LeaveExpiryModel();
			LeaveExpiryModel  reidnceExpry=new LeaveExpiryModel();
			LeaveExpiryModel  laborExpry=new LeaveExpiryModel();
			
			pssprtExpry=getEmployeePassportExpiryDate("A",obj.getEmployeeKey());			
			reidnceExpry=getEmployeeResidanceExpiryDate("A",obj.getEmployeeKey());			
			laborExpry=getEmployeeLabourCardExpiryDate("A",obj.getEmployeeKey());
			Date creationdate; 
			Calendar c = Calendar.getInstance();
			creationdate=df.parse(sdf.format(c.getTime()));
						
			int diffInDayslabour=999;
			int diffInDaysResidance=999;//a random initialization;
			int diffInDaysPassport=999;//a random initialization;
			String passportExpiry="",expPasprtMsg="",residanceExpiry="",labourCradExpiry="",expResidanceMSg="",expLbrCrdMsg="";
			
			if(pssprtExpry!=null)
			{
				diffInDaysPassport=999;
				passportExpiry=pssprtExpry.getExprityDateStr();
				if(pssprtExpry.getExprityDate()!=null)
					//diffInDaysPassport = ( (pssprtExpry.getExprityDate().getTime()-creationdate.getTime()) / (1000 * 60 * 60 * 24) );
				if(diffInDaysPassport<=objExpiry.getTotalDays() && diffInDaysPassport>=0)
				{
					 expPasprtMsg="About To Expire In " +diffInDaysPassport +" Days";					
				}
				else if(diffInDaysPassport<0)
				{
					 expPasprtMsg="Already Expired ";					
				}				
			}
			
			if(reidnceExpry!=null)
			{
				diffInDaysResidance=999;
				residanceExpiry=reidnceExpry.getExprityDateStr();
				if(reidnceExpry.getExprityDate()!=null)
//					diffInDaysResidance = ( (reidnceExpry.getExprityDate().getTime() - creationdate.getTime())
//		                 / (1000 * 60 * 60 * 24) );
				if(diffInDaysResidance<=objExpiry.getTotalDays() && diffInDaysResidance>=0)
				{
					expResidanceMSg="About To Expire In " + diffInDaysResidance +" Days";					
				}
				else if( diffInDaysResidance<0)
				{
					expResidanceMSg="Already Expired";					 
				}
				
			}
			
			if(laborExpry!=null)
			{
				labourCradExpiry=laborExpry.getExprityDateStr();
				residanceExpiry=reidnceExpry.getExprityDateStr();
				diffInDayslabour=999;
				if(laborExpry.getExprityDate()!=null)
//					diffInDayslabour = ( (laborExpry.getExprityDate().getTime() - creationdate.getTime())
//		                 / (1000 * 60 * 60 * 24) );
				if(diffInDayslabour<=objExpiry.getTotalDays() && diffInDayslabour>=0)
				{
					expLbrCrdMsg="About To Expire In "+diffInDayslabour+ " Days";					
				}
				else if( diffInDayslabour<0)
				{
					expLbrCrdMsg="Already Expired ";					
				}
			}								
			
			if(passportExpiry.equalsIgnoreCase(""))
			{
				passportExpiry="No Passport Available";
			}
			if(residanceExpiry.equalsIgnoreCase(""))
			{
				residanceExpiry="No Residence Available";
			}
			if(labourCradExpiry.equalsIgnoreCase(""))
			{
				labourCradExpiry="No Labor Card Available";
			}
			
			obj.setPassportExpiry(passportExpiry);
			obj.setExpPasprtMsg(expPasprtMsg);
			obj.setResidanceExpiry(residanceExpiry);
			obj.setExpResidanceMSg(expResidanceMSg);
			obj.setLabourCradExpiry(labourCradExpiry);
			obj.setExpLbrCrdMsg(expLbrCrdMsg);			
			
		}
		catch (Exception ex) {
			logger.error("error in HRServiceData---getEmployeeProfile-->" , ex);
		}
		return obj;
	}
	
	public String getEmployeeEmail(int empKey)
	{
		String email="";
		ResultSet rs = null;		
		try 
		{			
			rs=db.executeNonQuery(hrQuieries.getEmail(empKey));
			while(rs.next())
			{
				email=rs.getString("details");
			}
		}
		catch (Exception ex) {
			logger.error("error in HRServiceData---getEmployeeEmail-->" , ex);
		}
		
		return email;
	}
	
	public LeaveExpiryModel getEmployeePassportExpiryDate(String status, int empKey)
	{
		LeaveExpiryModel  expryDate=new LeaveExpiryModel();
		try
		{			
			ResultSet rs = null;
			rs=db.executeNonQuery(hrQuieries.getEmployeePassportExpiryDate(status,empKey));
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
			logger.error("error in HRServiceData---getEmployeePassportExpiryDate-->" , ex);
		}
		return expryDate;
	}
	
	public LeaveExpiryModel getEmployeeResidanceExpiryDate(String status, int empKey)
	{
		LeaveExpiryModel  expryDate=new LeaveExpiryModel();
		try
		{			
			ResultSet rs = null;
			rs=db.executeNonQuery(hrQuieries.getEmployeeResidanceExpiryDate(status,empKey));
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
			logger.error("error in HRServiceData---getEmployeeResidanceExpiryDate-->" , ex);
		}
		return expryDate;
	}
	
	
	public LeaveExpiryModel getEmployeeLabourCardExpiryDate(String status, int empKey)
	{
		LeaveExpiryModel  expryDate=new LeaveExpiryModel();
		try
		{			
			ResultSet rs = null;
			rs=db.executeNonQuery(hrQuieries.getEmployeeLabourCardExpiryDate(status,empKey));
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
			logger.error("error in HRServiceData---getEmployeeLabourCardExpiryDate-->" , ex);
		}
		return expryDate;
	}
	
	public ExpirySettingsModel getexpirySettings(int compId)
	{		 			
			ResultSet rs = null;
			ExpirySettingsModel obj=new ExpirySettingsModel();
			try 
			{
				rs=db.executeNonQuery(hrQuieries.getexpirySettings(compId));
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
				logger.error("error in HRServiceData---getexpirySettings-->" , ex);
			}
		 return obj;
	}
	
	public List<TimeSheetDataModel> getTomorrowPlanTimeSheet(int empkey)
	{		 			
			ResultSet rs = null;
			empkey=employeeKey;	
			List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();			
			try 
			{
				rs=db.executeNonQuery(hrQuieries.getTomorrowPlanTimeSheet(empkey));
				while(rs.next())
				{
					TimeSheetDataModel obj=new TimeSheetDataModel();
					obj.setTimesheetDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("TS_DATE")));					
					obj.setTomorrowPlan(rs.getString("TomorrowsPlan")==null?"":rs.getString("TomorrowsPlan"));	
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in HRServiceData---getTomorrowPlanTimeSheet-->" , ex);
			}
		 return lst;
	}
	
	public List<LeaveapproveOrRejectModel> getLeaveRequest(int empkey)
	{
		List<LeaveapproveOrRejectModel> lst=new ArrayList<LeaveapproveOrRejectModel>();
		try
		{
			empkey=employeeKey;	
			ResultSet rs = null;		
			rs=db.executeNonQuery(hrQuieries.getLeaveRequest(empkey));			
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
				obj.setSupervisorID(rs.getInt("supervisor"));
				obj.setEmpName(rs.getString("empName")==null?"":rs.getString("empName"));
				obj.setEmpNo(rs.getString("emp_No")==null?"":rs.getString("emp_No"));
				obj.setReason(rs.getString("notes").equalsIgnoreCase("null")?"":rs.getString("notes"));			
				lst.add(obj);
			}
		}
		
		catch (Exception ex) 
		{
			logger.error("error in HRData---leaveapproveOrRejectList-->" , ex);
		}	
		return lst;
	}
	
	public List<TasksModel> getUserTaskList()
	{	
		List<TasksModel> list=new ArrayList<TasksModel>();		
		ResultSet rs = null;			
		try 
		{
			rs=db.executeNonQuery(hrQuieries.getAllTask(0,employeeKey,"All"));
			while(rs.next())
			{	
				TasksModel obj=new TasksModel();
				obj.setTaskid(rs.getInt("taskID"));
				//obj.setCreationDate(rs.getDate("createDate"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createDate")));				
				obj.setCreatedUserName(rs.getString("CreatedbyUser"));
				obj.setTaskNumber(rs.getString("taskNo")==null?"":rs.getString("taskNo"));
				obj.setTaskTypeId(rs.getInt("tasktype"));
				obj.setTaskName(rs.getString("taskName")==null?"":rs.getString("taskName"));													
								
				obj.setPriorityNAme(rs.getString("TaskPriorityStr")==null?"":rs.getString("TaskPriorityStr"));
				obj.setTaskType(rs.getString("TaskTYpeStr")==null?"":rs.getString("TaskTYpeStr"));
				obj.setStatusName(rs.getString("TaskStatusStr")==null?"":rs.getString("TaskStatusStr"));
				obj.setProjectName(rs.getString("project_Name")==null?"":rs.getString("project_Name"));
				obj.setEmployeeName(rs.getString("employeename")==null?"":rs.getString("employeename"));
				obj.setHoursOrDays(rs.getString("hourOrDays")==null?"":rs.getString("hourOrDays"));
				obj.setServiceName(rs.getString("serviceNAme")==null?"":rs.getString("serviceNAme"));
				obj.setCcEmployeeName(rs.getString("employeeCcname")==null?"":rs.getString("employeeCcname"));
																
				if(rs.getDate("expectedDateTofinsh")!=null)
				obj.setExpectedDatetofinishStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expectedDateTofinsh")));
				else
					obj.setExpectedDatetofinishStr("");	
											
				list.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in TaskData---getAllTask-->" , ex);
		}
		return list;
	}
}
