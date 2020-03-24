package hr;

import hr.model.SponsorModel;
import hr.model.WorkGroupModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.CompanyDBModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class WorkGroupData {
	
	private Logger logger = Logger.getLogger(WorkGroupData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public WorkGroupData()
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
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in SponsorData---Init-->" , ex);
		}
	}
	
	
	public List<WorkGroupModel> fillWorkGroupInfo(int comapnyID,String status)
	{
		List<WorkGroupModel> lst=new ArrayList<WorkGroupModel>();
		WorkGroupQuerries query=new WorkGroupQuerries();
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		ResultSet rs = null;
		Map<String, Object> mapWorkGroup=new HashMap<String, Object>();
		try 
		{
			rs=db.executeNonQuery(query.fillWorkGroupInfo(comapnyID,status));
			while(rs.next())
			{
				WorkGroupModel obj=new WorkGroupModel();
				obj.setRecno(rs.getInt("rec_no"));
				obj.setGroupName(rs.getString("group_name")==null?"":rs.getString("group_name"));					
				obj.setGroupNameAR(rs.getString("group_namear")==null?"":rs.getString("group_namear"));
				obj.setSupervisor(rs.getString("supervisor")==null?"":rs.getString("supervisor"));
				obj.setIsActive(rs.getString("active")==null?"":rs.getString("active"));
				obj.setSupervisorName(rs.getString("english_full")==null?"":rs.getString("english_full"));
				obj.setSupervisorNameAR(rs.getString("arabic_full")==null?"":rs.getString("arabic_full"));
				obj.setCompKey(rs.getInt("comp_key"));
				obj.setSupervisorNumber(rs.getString("emp_no")==null?"":rs.getString("emp_no"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else{
					obj.setIsActive("INActive");
				}
				if(obj.getRecno()>0)
				{
					mapWorkGroup=getWorkGroupById(obj.getRecno());
					lstEmployees=(List<EmployeeModel>) mapWorkGroup.get("empList");
					obj.setTotalNoOfEmployees(lstEmployees.size());
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---fillWorkGroupInfo-->" , ex);
		}
		return lst;
	}
	
	public Map<String, Object> getWorkGroupById(int workGroupId)
	{
		WorkGroupModel obj=new WorkGroupModel();
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		WorkGroupQuerries query=new WorkGroupQuerries();
		Map<String, Object> map=new HashMap<String, Object>();
		ResultSet rs = null;
		int i=0;
		try 
		{
			rs=db.executeNonQuery(query.getWorkGroupById(workGroupId));
			while(rs.next())
			{
				obj.setRecno(rs.getInt("wrrec_no"));
				obj.setGroupName(rs.getString("group_name"));					
				obj.setGroupNameAR(rs.getString("group_namear"));
				obj.setSupervisor(rs.getString("supervisor"));
				obj.setIsActive(rs.getString("active"));
				obj.setCompKey(rs.getInt("comp_key"));
				obj.setSupervisorName(rs.getString("supervisorName"));
				obj.setSupervisorNameAR(rs.getString("supervisorNameAR"));
				EmployeeModel emplOvj=new EmployeeModel();
				emplOvj.setLineNo(i+1);
				i++;
				emplOvj.setEmployeeKey(rs.getInt("emp_key"));
				emplOvj.setArabicName(rs.getString("arabic_full"));
				emplOvj.setFullName(rs.getString("english_full"));
				emplOvj.setDepartmentID(rs.getInt("dep_id"));
				emplOvj.setPositionID(rs.getInt("pos_id"));
				emplOvj.setEmployeeNo(rs.getString("emp_no"));
				emplOvj.setEmployeementDate(rs.getDate("employeement_date"));
				emplOvj.setDepartment(rs.getString("departmentName"));
				emplOvj.setArabicDepartment(rs.getString("departmentArabic"));
				emplOvj.setPosition(rs.getString("postionName"));
				emplOvj.setArabicPosition(rs.getString("postionNameArabic"));
				String status=rs.getString("EMPACTIVE");
				if(status.equalsIgnoreCase("A"))
				emplOvj.setStatus("Active");
				else
				emplOvj.setStatus("INActive");
				if(emplOvj.getEmployeeKey()>0)
				lst.add(emplOvj);
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getWorkGroupById-->" , ex);
		}
		map.put("workGroupModel", obj);
		map.put("empList", lst);
		return map;
	}
	public Map<String, Object> getWorkGroupByIdForZeroEmployees(int workGroupId,int comp_key)
	{
		WorkGroupModel obj=new WorkGroupModel();
		WorkGroupQuerries query=new WorkGroupQuerries();
		Map<String, Object> map=new HashMap<String, Object>();
		ResultSet rs = null;
		int i=0;
		try 
		{
			rs=db.executeNonQuery(query.getWorkGroupByIdForZeroEmployees(workGroupId,comp_key));
			while(rs.next())
			{
				obj.setRecno(rs.getInt("rec_No"));
				obj.setGroupName(rs.getString("group_name"));					
				obj.setGroupNameAR(rs.getString("group_namear"));
				obj.setSupervisor(rs.getString("supervisor"));
				obj.setIsActive(rs.getString("active"));
				obj.setCompKey(rs.getInt("comp_key"));
				obj.setSupervisorName(rs.getString("supervisorName"));
				obj.setSupervisorNameAR(rs.getString("supervisorNameAR"));
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getWorkGroupByIdForZeroEmployees-->" , ex);
		}
		map.put("workGroupModel", obj);
		return map;
	}
	
	public List<WorkGroupModel> getNameFromWorkGroupForValidation(int comapnyID,String status)
	{
		List<WorkGroupModel> lst=new ArrayList<WorkGroupModel>();
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillWorkGroupInfo(comapnyID,status));
			while(rs.next())
			{
				WorkGroupModel obj=new WorkGroupModel();
				obj.setRecno(rs.getInt("rec_no"));
				obj.setGroupName(rs.getString("group_name"));					
				obj.setGroupNameAR(rs.getString("group_namear"));
				obj.setSupervisor(rs.getString("supervisor"));
				obj.setIsActive(rs.getString("active"));
				obj.setSupervisorName(rs.getString("english_full"));
				obj.setSupervisorNameAR(rs.getString("arabic_full"));
				obj.setCompKey(rs.getInt("comp_key"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else{
					obj.setIsActive("INActive");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getNameFromWorkGroupForValidation-->" , ex);
		}
		return lst;
	}
	
	
	public int updatetWorkGroup(WorkGroupModel obj,List<EmployeeModel> lstEmployees)
	{
		int result=0;
		int result1=0;
		WorkGroupQuerries query=new WorkGroupQuerries();	
		try 
		{			
			result=db.executeUpdateQuery(query.updatetWorkGroup(obj));	
			if(result>0)
			{
				db.executeUpdateQuery(query.deletefromWorkgropSetByWorkgroupId(obj.getRecno()));
				for(EmployeeModel employeeModel:lstEmployees)
				{
					if(obj.isWetherSupersupervisor())
					{
						List<EmployeeModel> employeesUNderEachSupervisor=new ArrayList<EmployeeModel>();
						employeesUNderEachSupervisor=getEmployeesUnderSupervisor(employeeModel.getEmployeeKey());
						for(EmployeeModel employeeModelUpdateSupersupervisor:employeesUNderEachSupervisor)
						{
							db.executeUpdateQuery(query.upadteEmpMastForSuper_Supervisor(employeeModelUpdateSupersupervisor.getEmployeeKey(),obj.getSupersupervisor()));
						}
					}
					db.executeUpdateQuery(query.deleteEmployeeById(employeeModel.getEmployeeKey()));
					result1=db.executeUpdateQuery(query.InserIntoWorkGroupSet(obj.getRecno(),employeeModel.getEmployeeKey()));
					if(result1>0)
						db.executeUpdateQuery(query.upadteEmpMast(obj.getRecno(),employeeModel.getEmployeeKey(),obj.getSupervisor()));
				}
			}
			
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---updatetWorkGroup-->" , ex);
		}
		return result;
		
	}
	
	public int getWorkGroupMaxRecQuerry()
	{
		int MaxRecNo=1;
		
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getWorkGroupMaxRecQuerry());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getWorkGroupMaxRecQuerry-->" , ex);
		}
		return MaxRecNo;
	}
	
	public int insertWorkGroup(WorkGroupModel workGroupModel,int recNo,List<EmployeeModel> lstEmployees) 
	{
		int result=0;
		int result1=0;
		Date createDate=new Date();
		Calendar c = Calendar.getInstance();	
		try {
			createDate=df.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WorkGroupQuerries query=new WorkGroupQuerries();		
		try 
		{		
			String str=query.insertWorkGroup(workGroupModel,recNo);
			result=db.executeUpdateQuery(str);		
			if(result>0)
			{
				db.executeUpdateQuery(query.deletefromWorkgropSetByWorkgroupId(recNo));
				for(EmployeeModel employeeModel:lstEmployees)
				{
					if(workGroupModel.isWetherSupersupervisor())
					{
						List<EmployeeModel> employeesUNderEachSupervisor=new ArrayList<EmployeeModel>();
						employeesUNderEachSupervisor=getEmployeesUnderSupervisor(employeeModel.getEmployeeKey());
						for(EmployeeModel employeeModelUpdateSupersupervisor:employeesUNderEachSupervisor)
						{
							db.executeUpdateQuery(query.upadteEmpMastForSuper_Supervisor(employeeModelUpdateSupersupervisor.getEmployeeKey(),workGroupModel.getSupersupervisor()));
						}
					}
					db.executeUpdateQuery(query.deleteEmployeeById(employeeModel.getEmployeeKey()));
					result1=db.executeUpdateQuery(query.InserIntoWorkGroupSet(recNo,employeeModel.getEmployeeKey()));
					if(result1>0)
						db.executeUpdateQuery(query.upadteEmpMast(recNo,employeeModel.getEmployeeKey(),workGroupModel.getSupervisor()));
					int recNonew=getEmpSupervisorMaxRecQuerry();
					db.executeUpdateQuery(query.insertIntoNewTrasferTable(recNonew,recNo,Integer.parseInt(workGroupModel.getSupervisor()), employeeModel.getEmployeeKey(),df.parse(sdf.format(employeeModel.getEmployeementDate())),null,createDate));
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in WorkGroupData---insertWorkGroup-->" , ex);
		}
		return result;
	}
	
	public List<EmployeeModel> getSuperVisorNameDropDown(int comapnyID)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSuperVisorNameDropDown(comapnyID));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setFullName(rs.getString("fullname"));
				obj.setArabicName(rs.getString("arabicname"));
				obj.setEmployeeKey(rs.getInt("employeekey"));
				obj.setCompanyID(rs.getInt("compkey"));
				obj.setStandardNo(rs.getString("standardNo"));
				obj.setEmployeeNo(rs.getString("employeeno"));
				obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
				obj.setProjectKey(rs.getInt("locationId"));
				obj.setStatus(rs.getString("active"));
				if(obj.getStatus().equalsIgnoreCase("A"))
				{
					obj.setStatus("Active");
				}
				else{
					obj.setStatus("INActive");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getSuperVisorNameDropDown-->" , ex);
		}
		return lst;
	}
	
	
	public List<EmployeeModel> getEmployeeById(int empId)
	{
		List<EmployeeModel> list=new ArrayList<EmployeeModel>();
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			
			rs=db.executeNonQuery(query.getEmployeeById(empId));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setEmployeeNo(rs.getString("EmployeeNo")==null?"":rs.getString("EmployeeNo"));
				obj.setFullName(rs.getString("FullName")==null?"":rs.getString("FullName"));
				obj.setArabicName(rs.getString("ArabicName")==null?"":rs.getString("ArabicName"));
				obj.setDepartmentID(rs.getInt("DepartmentID"));
				obj.setDepartment(rs.getString("Department"));
				obj.setArabicDepartment(rs.getString("ArabicDepartment"));
				obj.setJoiningDate(rs.getDate("employeementdate"));
				obj.setPositionID(rs.getInt("PositionID"));
				obj.setPosition(rs.getString("Position")==null?"":rs.getString("Position"));
				obj.setArabicPosition(rs.getString("ArabicPosition")==null?"":rs.getString("ArabicPosition"));
				obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"Local":rs.getString("Country"));
				obj.setCompanyName(rs.getString("CompanyName")==null?"":rs.getString("CompanyName"));
				obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				obj.setEmployeementDate(rs.getDate("Employeementdate"));
				obj.setGender(rs.getString("Sex")==null?"":rs.getString("Sex"));
				obj.setMarital(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setWorkGroupId(rs.getInt("workersgroupid"));
				obj.setSupervisorId(rs.getInt("supervisorid"));
				obj.setSupervisorName(rs.getString("supervisorname")==null?"":rs.getString("supervisorname"));
				obj.setSupervisorNameAR(rs.getString("arabicsupervisorname")==null?"":rs.getString("arabicsupervisorname"));
				obj.setWorkGroupName(rs.getString("workergroupname")==null?"":rs.getString("workergroupname"));
				obj.setWorkGroupNameAR(rs.getString("arabicworkergroupname")==null?"":rs.getString("arabicworkergroupname"));
				obj.setStatus(rs.getString("active")==null?"":rs.getString("active"));
				if(obj.getStatus().equalsIgnoreCase("A"))
				{
					obj.setStatus("Active");
				}
				else{
					obj.setStatus("INActive");
				}
				list.add(obj);

			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getEmployeeById-->" , ex);
		}
		return list;
	}
	
	
	public void deleteemployesFromEmpMast(int empId,int workgroupId,int supervisorID)
	{
		WorkGroupQuerries query=new WorkGroupQuerries();
		try 
		{
			db.executeUpdateQuery(query.deleteEmployeeById(empId));
			db.executeUpdateQuery(query.deleteemployesFromEmpMast(empId));
			db.executeUpdateQuery(query.deleteEmployeefromEmpSupervisorTableBygroupIdandEmpID(empId,workgroupId,supervisorID));
					}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---deleteemployesFromEmpMast-->" , ex);
		}
	}
	
	public void deleteWorkGroup(int workGroupId)
	{
		WorkGroupQuerries query=new WorkGroupQuerries();
		try 
		{
			db.executeUpdateQuery(query.deleteWorkGroup(workGroupId));
			db.executeUpdateQuery(query.deletefromWorkgropSetByWorkgroupId(workGroupId));
			db.executeUpdateQuery(query.deleteemployesFromEmpMastByworkGroupId(workGroupId));
			db.executeUpdateQuery(query.deleteWorkGroupfromEmpSupervisorTableBygroupId(workGroupId));
					}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---deleteWorkGroup-->" , ex);
		}
	}
	
	public void deleteWorkGroupSuperSupervisor(int super_SuperVisorID)
	{
		WorkGroupQuerries query=new WorkGroupQuerries();
		try 
		{
			db.executeUpdateQuery(query.deleteSuperSupervisorFromEmpMast(super_SuperVisorID));
					}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---deleteWorkGroupSuperSupervisor-->" , ex);
		}
	}
	
	
	public void insertIntoNewTrasferTable(int recNo,int workGroupId,int superviosrId,int empId,Date effectiveDate,Date Todate,Date createddate)
	{
		int result1=0;
		WorkGroupQuerries query=new WorkGroupQuerries();
		try 
		{
			db.executeUpdateQuery(query.insertIntoNewTrasferTable(recNo,workGroupId,superviosrId, empId,effectiveDate,Todate,createddate));
			db.executeUpdateQuery(query.deleteEmployeeById(empId));
			result1=db.executeUpdateQuery(query.InserIntoWorkGroupSet(workGroupId,empId));
			if(result1>0)
			db.executeUpdateQuery(query.upadteEmpMast(workGroupId,empId,""+superviosrId));  
			EmployeeModel obj=new EmployeeModel();
			obj=checkEmployeeHasSupervisorandSuper_supervisor(superviosrId);
			if(obj.getSupervisorId()>0)
				db.executeUpdateQuery(query.upadteEmpMastForSuper_Supervisor(empId,obj.getSupervisorId()));
			else
				db.executeUpdateQuery(query.upadteEmpMastForSuper_Supervisor(empId,0));
			
					}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---insertIntoNewTrasferTable-->" , ex);
		} 
	}
	
	public void updatePreviousEmp(int empId,Date Todate)
	{
		WorkGroupQuerries query=new WorkGroupQuerries();
		try 
		{
			db.executeUpdateQuery(query.updatePreviousEmp(empId,Todate));
					}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---updatePreviousEmp-->" , ex);
		}
	}
	
	public int getEmpSupervisorMaxRecQuerry()
	{
		int MaxRecNo=1;
		
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getEmpSupervisorMaxRecQuerry());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getEmpSupervisorMaxRecQuerry-->" , ex);
		}
		return MaxRecNo;
	}
	
	public void updateEmployeeStatus(int empId)
	{
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			db.executeUpdateQuery(query.updateEmployeeStatus(empId));
			
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---updateEmployeeStatus-->" , ex);
		}
	}
	
	public List<EmployeeModel> getAssignedEmployeesToShift(int compId,Date fromDate,Date toDate)
	{
		List<EmployeeModel> list=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForInactiveStatus=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForActiveStatus=new ArrayList<EmployeeModel>();
		WorkGroupQuerries query=new WorkGroupQuerries();
		HRQueries queryHR=new HRQueries();
		ResultSet rs = null;
		int i=0;
		try 
		{
			Date createDateNew;
			Calendar c = Calendar.getInstance();
			createDateNew=df.parse(sdf.format(c.getTime()));
			ResultSet newRsActive = null;
			ResultSet newRs = null;
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
			rs=db.executeNonQuery(query.getAssignedEmployeesToShift(compId,fromDate,toDate));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setLineNo(i+1);
				i++;
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setEmployeeNo(rs.getString("EmployeeNo"));
				obj.setFullName(rs.getString("FullName"));
				obj.setArabicName(rs.getString("ArabicName"));
				obj.setDepartmentID(rs.getInt("DepartmentID"));
				obj.setDepartment(rs.getString("Department"));
				obj.setArabicDepartment(rs.getString("ArabicDepartment"));
				obj.setJoiningDate(rs.getDate("employeementdate"));
				obj.setShiftFromDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("from_date")));
				obj.setShiftToDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("to_date")));
				obj.setShiftkey(rs.getInt("shift_key"));
				obj.setShiftName(rs.getString("shift_code"));
				String timingflag=rs.getString("timing_flag");
				if(timingflag.equalsIgnoreCase("N"))
				{
					obj.setShiftType("Daily");
				}
				else
				{
					obj.setShiftType("Hourly");
				}
				obj.setPositionID(rs.getInt("PositionID"));
				obj.setPosition(rs.getString("Position"));
				obj.setArabicPosition(rs.getString("ArabicPosition"));
				obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"Local":rs.getString("Country"));
				obj.setCompanyName(rs.getString("CompanyName"));
				obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				obj.setEmployeementDate(rs.getDate("Employeementdate"));
				obj.setGender(rs.getString("Sex"));
				obj.setMarital(rs.getString("Marital"));
				obj.setWorkGroupId(rs.getInt("workersgroupid"));
				obj.setSupervisorId(rs.getInt("supervisorid"));
				obj.setSupervisorName(rs.getString("supervisorname"));
				obj.setSupervisorNameAR(rs.getString("arabicsupervisorname"));
				obj.setWorkGroupName(rs.getString("workergroupname"));
				obj.setWorkGroupNameAR(rs.getString("arabicworkergroupname"));
				obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
				obj.setProjectKey(rs.getInt("locationId"));
				 obj.setEmployeeStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				    

					String active=rs.getString("Active");
					String terminate=rs.getString("Terminate");
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
					
					list.add(obj);
				}
				
			}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---getAssignedEmployeesToShift-->" , ex);
		}
		return list;
	}
	
	
	
	public List<EmployeeModel> getEmployeesUnderSupervisor(int empKey)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		try
		{
			WorkGroupQuerries query=new WorkGroupQuerries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeesUnderSupervisor(empKey));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EMP_KEY"));
				obj.setFullName(rs.getString("ENGLISH_FULL"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
 		{		 	  
		 logger.error("error in WorkGroupData---getEmployeesUnderSupervisor-->" , ex);			 	  
	 	}
		return lst;			
	}
	
	public List<EmployeeModel> getEmployeesUnderSuper_Supervisor(int empKey)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		try
		{
			WorkGroupQuerries query=new WorkGroupQuerries();
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeesUnderSuper_Supervisor(empKey));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EMP_KEY"));
				obj.setFullName(rs.getString("ENGLISH_FULL"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
 		{		 	  
		 logger.error("error in WorkGroupData---getEmployeesUnderSuper_Supervisor-->" , ex);			 	  
	 	}
		return lst;		
	}
	
	
	public boolean checkIfSuperSupervisor(int superSupervisorID)
	{
		boolean isSuperSupervisor=false;	
		if(superSupervisorID==0)
			return false;
		
		WorkGroupQuerries query=new WorkGroupQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getEmployeesUnderSuper_Supervisor(superSupervisorID));
			while(rs.next())
			{
				isSuperSupervisor=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---checkIfSuperSupervisor-->" , ex);
		}
		return isSuperSupervisor;
	}
	
	public EmployeeModel checkEmployeeHasSupervisorandSuper_supervisor(int emp_key)
	{
		WorkGroupQuerries query=new WorkGroupQuerries();
		EmployeeModel obj=new EmployeeModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkEmployeeHasSupervisorandSuper_supervisor(emp_key));
			while(rs.next())
			{
				obj.setEmployeeKey(rs.getInt("emp_key"));
				obj.setEmployeeNo(rs.getString("emp_no"));
				obj.setSupervisorId(rs.getInt("SUPERVISOR"));
				obj.setSuper_supervisorId(rs.getInt("SuperSupervisorId"));
			}
		}
		catch (Exception ex) {
			logger.error("error in WorkGroupData---checkEmployeeHasSupervisorandSuper_supervisor-->" , ex);
		}
		
		return obj;
	}
	
	
	
	
}
