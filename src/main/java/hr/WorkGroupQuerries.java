package hr;

import hr.model.SponsorModel;
import hr.model.WorkGroupModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.EmployeeModel;

public class WorkGroupQuerries {
	
	StringBuffer query;
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat sdfNew = new SimpleDateFormat("yyyy/MM/dd");
	private Date createDateNew;
	
	
	//To get the list of Work Groups (i,e Assign employees to supervisor)
	public String fillWorkGroupInfo(int comapnyID,String status)
	{
		query=new StringBuffer();		
		query.append("Select WORKERGROUPS.*,EMPMAST.ENGLISH_FULL,EMPMAST.ARABIC_FULL,EMPMAST.emp_no FROM WORKERGROUPS"); 
		query.append(" LEFT JOIN EMPMAST ON WORKERGROUPS.SUPERVISOR=EMPMAST.EMP_KEY  Where WORKERGROUPS.COMP_KEY ="+comapnyID+"");
		if(!status.equalsIgnoreCase(""))
		{
			query.append("  and WORKERGROUPS.active='"+status+"'"); 
		}
		query.append("  order by WORKERGROUPS.group_name");
		return query.toString();		
	}
	
	//To get the  Work Groups by id  (i,e Assign employees to supervisor)
	public String getWorkGroupById(int recNO)
	{
		Calendar c = Calendar.getInstance();	
		try {
			createDateNew=df.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  query=new StringBuffer();
		  
		  
		  query.append("Select distinct WORKERGROUPS.GROUP_NAME,WORKERGROUPS.GROUP_NAMEAR,WORKERGROUPS.SUPERVISOR,WORKERGROUPS.comp_key,WORKERGROUPS.active,WORKERGROUPS.rec_No as wrRec_NO,supervisor.workgroupid,supervisor.supervisorid,effectivedate,supervisor.todate,EMPMAST.EMP_KEY,EMPMAST.ACTIVE as EMPACTIVE,EMPMAST.employeement_date,EMPMAST.ENGLISH_FULL,EMPMAST.ARABIC_FULL,EMPSUPERVISOR.ENGLISH_FULL As SupervisorName,EMPSUPERVISOR.ARABIC_FULL As SupervisorNameAR,EMPMAST.DEP_ID,EMPMAST.POS_ID,EMPMAST.EMP_NO,HRLISVALUES_DEPT .Description As DepartmentName, HRLISVALUES_DEPT .Arabic As DepartmentArabic,HRLISVALUES_POSITION .Description As PostionName,HRLISVALUES_POSITION .Arabic As PostionNameArabic FROM WORKERGROUPS"); 
				  query.append(" left join empsupervisor As supervisor ON WORKERGROUPS.supervisor= supervisor.supervisorid ");
						  query.append(" LEFT JOIN EMPMAST As EMPSUPERVISOR ON WORKERGROUPS.SUPERVISOR=EMPSUPERVISOR.EMP_KEY  ");
								  query.append(" LEFT JOIN EMPMAST ON supervisor.empId=EMPMAST.EMP_KEY ");
										  query.append(" LEFT JOIN HRLISTVALUES As HRLISVALUES_DEPT ON EMPMAST.DEP_ID= HRLISVALUES_DEPT.ID ");
												  query.append(" left JOIN LOCATIONTRANSFER As HRLISVALUES_LOCA ON EMPMAST.EMP_KEY= HRLISVALUES_LOCA.emp_key ");
														  query.append(" LEFT JOIN HRLISTVALUES As HRLISVALUES_POSITION ON EMPMAST.POS_ID= HRLISVALUES_POSITION.ID");
																  query.append(" Where WORKERGROUPS.REC_NO ="+recNO+" and supervisor.workgroupid ="+recNO+" and  '"+sdfNew.format(createDateNew)+"' Between effectivedate And Case When TODATE IS NULL THEN '"+sdfNew.format(createDateNew)+"' ELSE TODATE END");
		  
		  
		  
		  
		  
		  /*query.append("Select WORKERGROUPS.GROUP_NAME,WORKERGROUPS.GROUP_NAMEAR,WORKERGROUPS.SUPERVISOR,WORKERGROUPS.comp_key,WORKERGROUPS.active,WORKERGROUPS.rec_No as wrRec_NO,");
		  query.append("WORKERGROUPSDET.*,EMPMAST.EMP_KEY,EMPMAST.ENGLISH_FULL,EMPMAST.ARABIC_FULL,");
		  query.append("EMPSUPERVISOR.ENGLISH_FULL As SupervisorName,EMPSUPERVISOR.ARABIC_FULL As SupervisorNameAR,");
		  query.append("EMPMAST.DEP_ID,EMPMAST.POS_ID,EMPMAST.EMP_NO,");
		  query.append("HRLISVALUES_DEPT .Description As DepartmentName, ");
		  query.append("HRLISVALUES_DEPT .Arabic As DepartmentArabic,HRLISVALUES_POSITION .Description As PostionName,");
		  query.append("HRLISVALUES_POSITION .Arabic As PostionNameArabic FROM WORKERGROUPS LEFT JOIN WORKERGROUPSDET ON WORKERGROUPS.REC_NO=WORKERGROUPSDET.REC_NO ");
		  query.append("LEFT JOIN EMPMAST As EMPSUPERVISOR ON WORKERGROUPS.SUPERVISOR=EMPSUPERVISOR.EMP_KEY ");
		  query.append("LEFT JOIN EMPMAST ON WORKERGROUPSDET.EMP_KEY=EMPMAST.EMP_KEY ");
		  query.append("LEFT JOIN HRLISTVALUES As HRLISVALUES_DEPT ON EMPMAST.DEP_ID= HRLISVALUES_DEPT.ID ");
		  query.append("LEFT JOIN  HRLISTVALUES As HRLISVALUES_POSITION ON EMPMAST.POS_ID= HRLISVALUES_POSITION.ID ");
		  query.append("Where WORKERGROUPS.REC_NO ="+recNO+"");	*/	
		  return query.toString();
	}
	
	//To get the list of Work Groups by company id and work group recNo ,this method is used for get work group with no employees under it(i,e Assign employees to supervisor)
	public String getWorkGroupByIdForZeroEmployees(int recNO,int comp_key)
	{
		  query=new StringBuffer();
		  query.append("select distinct WORKERGROUPS.*,EMPSUPERVISOR.ENGLISH_FULL As SupervisorName,EMPSUPERVISOR.ARABIC_FULL As SupervisorNameAR from WORKERGROUPS ");
		  query.append("LEFT JOIN EMPMAST As EMPSUPERVISOR ON WORKERGROUPS.SUPERVISOR=EMPSUPERVISOR.EMP_KEY  where WORKERGROUPS.rec_no="+recNO+" and WORKERGROUPS.comp_key="+comp_key);
		  return query.toString();
	}
	/*public String getNameFromSponsorForValidation()
	{
		query=new StringBuffer();
		query.append("Select group_name,group_namear,rec_No from WORKERGROUPS");
		return query.toString();
		
	}*/
	//Update the existing work group
	public String updatetWorkGroup(WorkGroupModel obj)
	{
		query=new StringBuffer();		
		query.append("update WORKERGROUPS set group_name='"+obj.getGroupName()+"',group_namear='"+obj.getGroupNameAR() +"',supervisor='"+obj.getSupervisor()+"',active='"+obj.getIsActive()+"',comp_key="+obj.getCompKey()+" Where  rec_no='" + obj.getRecno()+"'");
		return query.toString();		
	}
	
	//Create the  work group and save to workgroupset table 
	public String InserIntoWorkGroupSet(int recno,int empkey)
	{
		query=new StringBuffer();		
		query.append("insert into WORKERGROUPSDET (rec_no,EMP_KEY)values(");
		  query.append(recno +"," + empkey + ")");
		return query.toString();		
	}
	
	//update the empMast table for work group
	public String upadteEmpMast(int workGroupRecNo,int empKey,String supervisorKey)
	{
		query=new StringBuffer();		
		query.append("update EMPMAST set WORKERS_GROUPID="+workGroupRecNo+",supervisor="+supervisorKey+"");
		query.append(" where EMP_KEY="+empKey+"");
		return query.toString();		
	}
	
	//update the empMast table with super_supervisor id(level of hierachy) for work group
	public String upadteEmpMastForSuper_Supervisor(int empKey,int super_Supervisorid)
	{
		query=new StringBuffer();		
		query.append("update EMPMAST set SuperSupervisorId="+super_Supervisorid+" where EMP_KEY="+empKey+"");
		return query.toString();		
	}
	
	//get all the employee under each supervisor 
	public String getEmployeesUnderSupervisor(int supervisorid)
	{
		query=new StringBuffer();		
		query.append("select * from EMPMAST where supervisor="+supervisorid+"");
		return query.toString();		
	}
	
	//get all the employee under each Super_supervisor
	public String getEmployeesUnderSuper_Supervisor(int super_Supervisorid)
	{
		query=new StringBuffer();		
		query.append("select * from EMPMAST where SuperSupervisorId="+super_Supervisorid+"");
		return query.toString();		
	}
	
	//get the maximum REco no for the work group creation
	public String getWorkGroupMaxRecQuerry()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(rec_no) as MaxRecNo from WORKERGROUPS");		
		  return query.toString();
	}
	
	//get the maximum Rec_no for the work group creation
	public String insertWorkGroup(WorkGroupModel obj,int recNo)
	{
		  query=new StringBuffer();
		  query.append("insert into WORKERGROUPS (rec_no,group_name,group_namear,supervisor,active,comp_key)values(");
		  query.append(recNo +",'" + obj.getGroupName() +"' , '" + obj.getGroupNameAR() + "', '" + obj.getSupervisor() + "', '" + obj.getIsActive() +"'," + obj.getCompKey() + ")");
		  return query.toString();
	}
	//get list with all supervisor
	public String getSuperVisorNameDropDown(int companyKey)
	{
		  query=new StringBuffer();
		  query.append("Select  * FROM EmployeeDetails ");
		  if(companyKey!=0)
		  query.append("Where CompKey in ("+companyKey+") ");	
		  query.append("order by fullname");
		  return query.toString();
	}
	//get employees by id 
	public String getEmployeeById(int compId)
	{
		  query=new StringBuffer();
		  query.append("Select * from EmployeeDetails  where CompKey="+compId+"");		
		  return query.toString();
	}
	
	//delete employee by id 
	public String deleteEmployeeById(int empId)
	{
		  query=new StringBuffer();
		  query.append("Delete From WORKERGROUPSDET Where EMP_KEY  ="+empId+"");		
		  return query.toString();
	}
	
	//delete from work group set 
	public String deletefromWorkgropSetByWorkgroupId(int WorkgroupId)
	{
		  query=new StringBuffer();
		  query.append("Delete From WORKERGROUPSDET Where rec_no  ="+WorkgroupId+"");		
		  return query.toString();
	}
	
	//update the employee in empMast table with id on deletion of work group
	public String deleteemployesFromEmpMast(int empId)
	{
		  query=new StringBuffer();
		  query.append("UPDATE EMPMAST SET SUPERVISOR ='0',SuperSupervisorId=0,workers_GroupId=0 Where emp_key ="+empId+"");
		  return query.toString();
	}
	
	//delete from work group by id 
	public String deleteWorkGroup(int workgroupId)
	{
		  query=new StringBuffer();
		  query.append("Delete From WORKERGROUPS Where rec_no  ="+workgroupId+"");		
		  return query.toString();
	}
	//update the employee in empMast table with id on deletion of work group
	public String deleteemployesFromEmpMastByworkGroupId(int workgroupId)
	{
		  query=new StringBuffer();
		  query.append("UPDATE EMPMAST SET SUPERVISOR ='0',workers_GroupId=0 Where workers_GroupId ="+workgroupId+"");
		  return query.toString();
	}
	//update the employee in empMast table with id on deletion of work group
	public String deleteSuperSupervisorFromEmpMast(int super_SuperVisorID)
	{
		  query=new StringBuffer();
		  query.append("UPDATE EMPMAST SET SuperSupervisorId=0 Where SuperSupervisorId ="+super_SuperVisorID+"");
		  return query.toString();
	}
	
	
	//insert the values in to table used for transfer between the location or sites 
	public String insertIntoNewTrasferTable(int recNo,int workGroupId,int superviosrId,int empId,Date effectiveDate,Date Todate,Date createddate)
	{
		  query=new StringBuffer();
		  query.append("insert into empsupervisor(recno,workgroupid,supervisorid,empid,createddate,effectiveDate,todate)values("+recNo+","+workGroupId+","+superviosrId+","+empId+",'"+sdf.format(createddate)+"','"+sdf.format(effectiveDate)+"',");
		  if(Todate!=null)
		  query.append("'"+sdf.format(Todate)+"'");
		  else
		  query.append(""+Todate+"");
		  query.append(")");
		  return query.toString();
	}
	//update the values in to table used for transfer between the location or sites
	public String updatePreviousEmp(int empId,Date Todate)
	{
		  query=new StringBuffer();
		  query.append("UPDATE empsupervisor set todate='"+sdf.format(Todate)+"' where empid="+empId+"And TODATE is NULL ");
		  return query.toString();
	}
	//get the max value used for transfer between the location or sites
	public String getEmpSupervisorMaxRecQuerry()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(recno) as MaxRecNo from empsupervisor");		
		  return query.toString();
	}
	//delete the values from table used for transfer between the location or sites
	public String deleteWorkGroupfromEmpSupervisorTableBygroupId(int workgroupId)
	{
		  query=new StringBuffer();
		  query.append("Delete From empsupervisor Where workgroupid  ="+workgroupId+"");		
		  return query.toString();
	}
	//delete the values from table used for transfer between the location or sites
	public String deleteEmployeefromEmpSupervisorTableBygroupIdandEmpID(int empId,int workgroupId,int supervisorId)
	{
		  query=new StringBuffer();
		  query.append("Delete From empsupervisor Where empid  ="+empId+" and workgroupid="+workgroupId+" and supervisorId="+supervisorId+"");		
		  return query.toString();
	}
	//update the status of empoloyee
	public String updateEmployeeStatus(int empId)
	{
		  query=new StringBuffer();
		  query.append("UPDATE EMPMAST SET active ='A' Where emp_key ="+empId+"");		
		  return query.toString();
	}
	//get all employees who are been assigned to shift
	public String getAssignedEmployeesToShift(int compId,Date fromDate,Date toDate)
	{
		  query=new StringBuffer();
		  query.append("SELECT EMPSHIFT.*,shiftmast.* ,EmployeeDetails.* FROM EMPSHIFT  INNER JOIN EmployeeDetails ON EMPSHIFT.EMP_KEY = EmployeeDetails.employeeKey INNER JOIN shiftmast ON EMPSHIFT.shift_key = shiftmast.shift_Key "); 
		  query.append(" WHERE EMPSHIFT.COMP_KEY="+compId+" AND ((EMPSHIFT.FROM_DATE BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(fromDate)+"') OR (EMPSHIFT.TO_DATE BETWEEN '"+sdf.format(toDate)+"' AND '"+sdf.format(toDate)+"') OR (EMPSHIFT.TO_DATE >= '"+sdf.format(toDate)+"') and ( EMPSHIFT.FROM_DATE <= '"+sdf.format(fromDate)+"'))  ORDER BY EMPSHIFT.POS_ID,EMPSHIFT.EMP_KEY,EMPSHIFT.FROM_DATE");
		  return query.toString();
	}
	 
	//get emp_key,emp_no,SUPERVISOR,SuperSupervisorId user for level of hierachy 
	public String checkEmployeeHasSupervisorandSuper_supervisor(int empId)
	{
		  query=new StringBuffer();
		  query.append("select emp_key,emp_no,SUPERVISOR,SuperSupervisorId from empmast where emp_key="+empId+""); 
		  return query.toString();
	}
	 

}
