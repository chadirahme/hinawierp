package hba;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.CompanyDBModel;
import model.CustomerFeedbackModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import db.DBHandler;
import db.SQLDBHandler;
import setup.users.WebusersModel;
import admin.TasksModel;

public class TaskQueery 
{
	StringBuffer query;
	private Logger logger = Logger.getLogger(TaskQueery.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	WebusersModel dbUser=null;
	
	CustomerData customerData;
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	
	public TaskQueery()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=null;
			if(sess!=null && sess.getAttribute("Authentication")!=null)
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HBAQueries query=new HBAQueries();
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
				  
				  customerData=new CustomerData();
				  
			}
			else
			{
				CompanyDBModel objNew = new CompanyDBModel();
			 	objNew.setUserip("hinawi2.dyndns.org");
			 	objNew.setDbname("ECActualERPData");
			 	objNew.setDbuser("admin");
			 	objNew.setDbpwd("admin123");
				db = new SQLDBHandler(objNew);
				 // customerData=new CustomerData();
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in TaskData---Init-->" , ex);
		}
	}
	
	
	public String addTask(TasksModel obj)
	{
			//To remove '' i the SQL statements ;
			String memo=""; 
			if(obj.getMemo()!=null)
			{
				memo=obj.getMemo().replaceAll("'","`");
			}
			obj.setMemo(memo);
			
			String commnets=""; 
			if(obj.getComments()!=null)
			{
				commnets=obj.getComments().replaceAll("'","`");
			}
			obj.setComments(commnets);
				
			String stepsToReproduce=""; 
			if(obj.getTaskStep()!=null)
			{
				stepsToReproduce=obj.getTaskStep().replaceAll("'","`");
			}
			obj.setTaskStep(stepsToReproduce);
			
		  query=new StringBuffer();		 
		  query.append(" Insert into tasks (taskID,createDate,expectedDateTofinsh,toBeReminderIn,createdUser,taskNo,tasktype,taskName,steps,linkid,customerrefKey,projectrefKey,servicerefKey,assignedUser,ccemployeeKey,priorityrefKey,estTime,memo,actualTime,status,usercomments,hourOrDays,customerType,reminderDate,createdAutomaticFeedback,customerNameFeedback,feedBackKey)");
		  query.append(" values("+obj.getTaskid()+",'"+sdf.format(obj.getCreationDate())+"','"+sdf.format(obj.getExpectedDatetofinish())+"',"+obj.getRemindIn()+"," + obj.getCreatedUserID()+",'"+obj.getTaskNumber()+"',"+obj.getTaskTypeId()+",'"+obj.getTaskName()+"',");
		  query.append("'"+obj.getTaskStep()+"',"+obj.getPrviousTaskLinkId()+","+obj.getCustomerRefKey()+","+obj.getProjectKey()+","+obj.getSreviceId()+","+obj.getEmployeeid()+","+obj.getCcEmployeeKey()+","+obj.getPriorityRefKey()+",");
		  query.append(""+obj.getEstimatatedNumber()+",'"+obj.getMemo()+"',"+obj.getActualNumber()+","+obj.getStatusKey()+",'"+obj.getComments()+"','"+obj.getHoursOrDays()+"','"+obj.getClientType()+"','"+sdf.format(obj.getReminderDate())+"','"+obj.getCreatedAutommaticTask()+"','"+obj.getCustomerNamefromFeedback()+"',"+obj.getFeedbackKey()+")");
		  return query.toString();
	}
	
	
	public String editTask(TasksModel obj)
	{
		//To remove '' i the SQL statements ;
		String memo=""; 
		if(obj.getMemo()!=null)
		{
			memo=obj.getMemo().replaceAll("'","`");
		}
		obj.setMemo(memo);
		
		String commnets=""; 
		if(obj.getComments()!=null)
		{
			commnets=obj.getComments().replaceAll("'","`");
		}
		obj.setComments(commnets);
			
		String stepsToReproduce=""; 
		if(obj.getTaskStep()!=null)
		{
			stepsToReproduce=obj.getTaskStep().replaceAll("'","`");
		}
		obj.setTaskStep(stepsToReproduce);
		query=new StringBuffer();
		query.append("Update tasks set customerType='"+obj.getClientType()+"',taskNo='"+obj.getTaskNumber()+"',tasktype="+obj.getTaskTypeId()+",expectedDateTofinsh='"+sdf.format(obj.getExpectedDatetofinish())+"',reminderDate='"+sdf.format(obj.getReminderDate())+"',toBeReminderIn="+obj.getRemindIn()+",taskName='"+obj.getTaskName()+"',steps='"+obj.getTaskStep()+"',linkid="+obj.getPrviousTaskLinkId()+",customerrefKey="+obj.getCustomerRefKey()+",");	
		query.append("projectrefKey="+obj.getProjectKey()+",servicerefKey="+obj.getSreviceId()+",assignedUser="+obj.getEmployeeid()+",ccemployeeKey="+obj.getCcEmployeeKey()+",priorityrefKey="+obj.getPriorityRefKey()+",estTime="+obj.getEstimatatedNumber()+",memo='"+obj.getMemo()+"',actualTime="+obj.getActualNumber()+",status="+obj.getStatusKey()+",usercomments='"+obj.getComments()+"',hourOrDays='"+obj.getHoursOrDays()+"' where taskID="+obj.getTaskid()+"");
		query.append(" ");
		return query.toString();
	}
	
	
	
	public String addTaskDetails(TasksModel obj)
	{
			//To remove '' i the SQL statements ;
			String memo=""; 
			if(obj.getMemo()!=null)
			{
				memo=obj.getMemo().replaceAll("'","`");
			}
			obj.setMemo(memo);
			
			String commnets=""; 
			if(obj.getComments()!=null)
			{
				commnets=obj.getComments().replaceAll("'","`");
			}
			obj.setComments(commnets);
				
			String stepsToReproduce=""; 
			if(obj.getTaskStep()!=null)
			{
				stepsToReproduce=obj.getTaskStep().replaceAll("'","`");
			}
			obj.setTaskStep(stepsToReproduce);
			
			Calendar cal = Calendar.getInstance(); 
			Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
			
		  query=new StringBuffer();		 
		  query.append(" Insert into TaskDetails (taskID,actualTime,status,usercomments,DateTime,UserId)");
		  query.append(" values("+obj.getTaskid()+","+obj.getActualNumber()+","+obj.getStatusKey()+",'"+obj.getComments()+"','"+timestamp+"',"+obj.getCreatedUserID()+")");
		  return query.toString();
	}
	
	public String addTaskAttachmnetPath(TasksModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into TaskAttachments (taskID,attachmentpath,fileName)");
		  query.append(" values("+obj.getTaskid()+",'"+obj.getAttchemnetPath()+"','"+obj.getFileName()+"')");
		  return query.toString();
	}
	
	
	public String editTaskAttachmnetPath(TasksModel obj)//not used for now 
	{
		  query=new StringBuffer();		
		  query.append("Update TaskAttachments set attachmentpath='"+obj.getAttchemnetPath()+"' where taskID="+obj.getTaskid()+"");	
		  return query.toString();
	}
	
	public String deleteAllTaskAttachmnetPathbyId(TasksModel obj)
	{
		  query=new StringBuffer();		
		  query.append("delete from TaskAttachments where taskID="+obj.getTaskid()+"");  		
		  return query.toString();
	}
	
	public String getAllTask(int statusId,int webUserId,Date fromDate,Date toDate,String activity)
	{
		  query=new StringBuffer();		 
		  query.append("SELECT  tasks.*,feedback.enquiry_no, ");
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
		 
		  if(activity.equalsIgnoreCase("All") && webUserId>0)
			  query.append(" and (tasks.createdUser="+webUserId+" or tasks.assignedUser="+webUserId+")");	
		  else if(activity.equalsIgnoreCase("Tasks By You") && webUserId>=0)
			  query.append(" and (tasks.createdUser="+webUserId+")");
		  else if(activity.equalsIgnoreCase("Tasks For You") && webUserId>=0)	  
			  query.append(" and (tasks.assignedUser="+webUserId+")");
			  
		  query.append(" and tasks.createDate Between '"+sdf.format(fromDate)+"' And '"+sdf.format(toDate)+"' ");
		  query.append(" order by  tasks.createDate desc");	
		  return query.toString();
	}
	
	public String getAllTaskOtherThanCurrentTask(int taskID)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from tasks");
		  if(taskID>0)
		  query.append(" where taskID !="+taskID+"");	
		  query.append(" order by taskName");	
		  return query.toString();
	}
	
	public String getTaskById(int taskID)
	{
		  query=new StringBuffer();		 
		  query.append("Select  *,def.description as TaskStatusStr, ");
		  query.append(" isnull(employeeCreateLIst.ENGLISH_FIRST,'ADMIN') as 'CreatedbyUser' ");
		  query.append(" from tasks LEFT JOIN HRLISTVALUES as def ON tasks.status = def.id ");
		  query.append(" LEFT JOIN empmast as employeeCreateLIst ON tasks.CreatedUser = employeeCreateLIst.emp_key ");
		  
		  if(taskID>0)
		  query.append(" where taskID="+taskID+"");	  
		  return query.toString();
	}
	
	public String getTaskDeatils(int taskID)
	{
		  query=new StringBuffer();		 
		  query.append("select TaskDetails.* , isnull(employeeCreateLIst.ENGLISH_FIRST,'ADMIN') as 'CreatedbyUser' , HRLISTVALUES.DESCRIPTION as 'StatusName' ");
		  query.append(" from TaskDetails");
		  query.append(" LEFT JOIN empmast as employeeCreateLIst ON TaskDetails.UserId = employeeCreateLIst.emp_key ");
		  query.append(" LEFT JOIN HRLISTVALUES ON TaskDetails.Status = HRLISTVALUES.id ");
		  if(taskID>0)
		  query.append(" where  taskID="+taskID+"");	  
		  query.append(" order by dateTime desc");	  
		  return query.toString();
	}
	
	public String getTaskAttchamnet(int taskID)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from TaskAttachments");
		  if(taskID>0)
		  query.append(" where  taskID="+taskID+"");	  
		  return query.toString();
	}
	
	public String checkIfTaskNumberIsDuplicate(String taskNumber,int taskID)
	{
		query=new StringBuffer();
		 query.append(" Select * from tasks Where taskNo=" + taskNumber+" and taskID !="+taskID);
		 return query.toString();		
	}
	
	
	
	public String getNextRecordTask(int recNo,int webUserID)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM tasks  WHERE taskID >"+recNo+" ");
		  if(webUserID>0)
		  query.append(" and (tasks.createdUser="+webUserID+" or tasks.assignedUser="+webUserID+")");	
		  query.append(" ORDER BY taskID ");
		  return query.toString();
	}
	
	public String getPreviousRecordTask(int recNo,int webUserID)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM tasks  WHERE taskID <"+recNo+"");
		  if(webUserID>0)
		  query.append(" and (tasks.createdUser="+webUserID+" or tasks.assignedUser="+webUserID+")");	
		  query.append(" ORDER BY taskID  desc ");
		  return query.toString();
	}
	
	public String getFirstRecordTask(int webUserID)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM tasks ");
		  if(webUserID>0)
		  query.append(" where (tasks.createdUser="+webUserID+" or tasks.assignedUser="+webUserID+")");	
		  query.append(" ORDER BY taskID");
		  return query.toString();
	}
	
	
	public String getLastRecordTask(int webUserID)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM tasks ");
		  if(webUserID>0)
		  query.append(" where (tasks.createdUser="+webUserID+" or tasks.assignedUser="+webUserID+")");	
		  query.append(" ORDER BY taskID desc");
		  return query.toString();
	}
	
	public String saveFeedbackCustomerRelation(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into TaskFeedbackRelation (feedbackId,taskId,taskStatus,createdDate,userID)");
		  query.append(" values("+obj.getFeedbackKey()+","+obj.getTaskID()+","+obj.getTaskStatusId()+",'"+sdf.format(obj.getFeedbackCreateDate())+"','"+obj.getUserId()+"')");
		  return query.toString();
	}
	
	public String updateFeedbackTabelwithCustomer(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" update CustomerEnquiry set customerrefKey="+obj.getCustomerRefKey()+",TypeOfCutomer='"+obj.getCustomerType()+"' where Enquiry_ID="+obj.getFeedbackKey()+"");
		  return query.toString();
	}
	
	public String getCustomerFeedbackById(int feedBackId)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from CustomerEnquiry");
		  if(feedBackId>0)
		  query.append(" where Enquiry_ID="+feedBackId+"");	  
		  return query.toString();
	}
	
}
