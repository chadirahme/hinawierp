package restdata;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HRServiceQuieries {

	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public String getEmployeeInfoQuery(int EmployeeKey) {
		query = new StringBuffer();
		query.append("Select * from EmployeeDetails");
		query.append(" where EmployeeKey=" + EmployeeKey);

		return query.toString();
	}
	
	public String getEmail(int empKey)
	{
		int contct_id=622;
		query=new StringBuffer();
		query.append("select * from empcontact where contact_id="+contct_id+" and emp_key="+empKey);		
		return query.toString();
	}
	
	public String getEmployeePassportExpiryDate(String status ,int empKEy)
	{
		query=new StringBuffer();
		query.append("select * from EMPPASSPORT where status='"+status+"' and emp_key="+empKEy+"");		
		return query.toString();
	}
	
	public String getEmployeeResidanceExpiryDate(String status ,int empKEy)
	{
		query=new StringBuffer();
		query.append("select * from EMPRESIDENCE where status='"+status+"' and emp_key="+empKEy+"");		
		return query.toString();
	}
	
	public String getEmployeeLabourCardExpiryDate(String status ,int empKEy)
	{
		query=new StringBuffer();
		query.append("	select * from EMPLABORCARD where status='"+status+"' and emp_key="+empKEy+"");		
		return query.toString();
	}
	
	public String getexpirySettings(int compId)
	{
		query=new StringBuffer();
		query.append("SELECT * FROM EXPIRYSETTINGS ");
		query.append(" where compId ="  + compId);
		return query.toString();		
	}
	
	public String getTomorrowPlanTimeSheet(int empKEy)
	{
		query=new StringBuffer();
		query.append(" SELECT top 10 d.TomorrowsPlan,LABOUR_KEY,TS_DATE  from DAILYTS d where LABOUR_KEY='"+empKEy+"' Order by d.TS_DATE desc");		
		return query.toString();
	}
	
	public String getLeaveRequest(int empKey)
	{
		query=new StringBuffer();	
		query.append("select top 5 LEAVEREQUEST.*,HRLISTVALUES.description as leaveReason,empmast.supervisor,empmast.emp_no,empmast.english_full as empName from (select LEAVEREQUEST.*,HRLISTVALUES.description as leaveType from LEAVEREQUEST ");
		query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.Leave_type_id=HRLISTVALUES.id)LEAVEREQUEST "); 
		query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.reason_id=HRLISTVALUES.id ");
		query.append(" LEFT JOIN empmast ON LEAVEREQUEST.emp_key=empmast.emp_key");
		query.append(" where empmast.emp_key="+empKey);		
		query.append(" order by LEAVEREQUEST.date desc");
		return query.toString();
	}
	
	public String getAllTask(int statusId,int employeeKey,String activity)
	{
		  query=new StringBuffer();		 
		  query.append("SELECT top 10  tasks.*,feedback.enquiry_no, ");
		  query.append(" HRLISTVALUES.description as TaskTYpeStr, ");
		  query.append(" abc.description as TaskPriorityStr, ");
		  query.append(" def.description as TaskStatusStr, ");
		  query.append(" customerList.fullname,prospectiveList.fullname as prospectiveName,projectList.project_Name, ");
		  query.append(" serviceList.description as serviceNAme, ");
		  query.append(" ccemployee.english_first as employeeCcname, ");
		  query.append(" employeeLIst.english_first as employeename ,isnull(employeeCreateLIst.ENGLISH_FIRST,'ADMIN') as 'CreatedbyUser' ");
		  query.append(" from tasks LEFT JOIN HRLISTVALUES ON tasks.taskType = HRLISTVALUES.id ");
		  query.append(" LEFT JOIN HRLISTVALUES as abc ON tasks.priorityRefKey = abc.id ");
		  query.append(" LEFT JOIN HRLISTVALUES as def ON tasks.status = def.id ");
		  query.append(" LEFT JOIN Customer as customerList ON tasks.customerrefKey = customerList.cust_key ");
		  query.append(" LEFT JOIN Prospective as prospectiveList ON tasks.customerrefKey = prospectiveList.recNo ");
		  query.append(" LEFT JOIN projectMast as projectList ON tasks.projectrefKey = projectList.project_key ");
		  query.append(" LEFT JOIN HRLISTVALUES as serviceList ON tasks.servicerefKey = serviceList.id ");
		  query.append(" LEFT JOIN empmast as ccemployee ON tasks.ccemployeeKey = ccemployee.emp_key ");
		  query.append(" LEFT JOIN CustomerEnquiry as feedback ON tasks.feedBackKey = feedback.enquiry_Id  ");
		  query.append(" LEFT JOIN empmast as employeeLIst ON tasks.assignedUser = employeeLIst.emp_key "); 
		  query.append(" LEFT JOIN empmast as employeeCreateLIst ON tasks.CreatedUser = employeeCreateLIst.emp_key ");
		  
		  if(statusId>0)
			  query.append(" where tasks.status="+statusId+"");	
		  else
			  query.append(" where tasks.status!="+statusId+"");	
		 
		  if(activity.equalsIgnoreCase("All") && employeeKey>0)
			  query.append(" and (tasks.createdUser="+employeeKey+" or tasks.assignedUser="+employeeKey+")");	
		  		 		  		
		  //query.append(" and tasks.createDate Between '"+sdf.format(fromDate)+"' And '"+sdf.format(toDate)+"' ");
		  query.append(" order by  tasks.createDate desc");	
		  return query.toString();
	}
	
}
