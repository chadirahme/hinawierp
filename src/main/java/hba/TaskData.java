package hba;

import home.CustomerFeedBackQuerries;
import home.QuotationAttachmentModel;
import hr.HRQueries;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CashInvoiceModel;
import model.CompanyDBModel;
import model.CustomerFeedbackModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import admin.TasksModel;
import db.DBHandler;
import db.SQLDBHandler;

public class TaskData 
{
	private Logger logger = Logger.getLogger(TaskData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	  CustomerData customerData;
	
	
	public TaskData()
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
	
	
	public List<TasksModel> getAllTask(int statusId,int webUserId,Date fromDate,Date toDate,String activity)
	{
	
		List<TasksModel> list=new ArrayList<TasksModel>();
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		
		
		List<TasksModel> taskHistory=new ArrayList<TasksModel>();
		List<QuotationAttachmentModel> taskAttchmnet=new ArrayList<QuotationAttachmentModel>();
		
		taskHistory=getTaskDeatils(0);
		taskAttchmnet=getTaskAttchamnet(0);
		
		
		try 
		{
			rs=db.executeNonQuery(query.getAllTask(statusId,webUserId,fromDate,toDate,activity));
			while(rs.next())
			{	
				TasksModel obj=new TasksModel();
				obj.setTaskid(rs.getInt("taskID"));
				obj.setCreationDate(rs.getDate("createDate"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createDate")));
				obj.setCreatedUserID(rs.getInt("createdUser"));
				obj.setCreatedUserName(rs.getString("CreatedbyUser"));
				obj.setTaskNumber(rs.getString("taskNo")==null?"":rs.getString("taskNo"));
				obj.setTaskTypeId(rs.getInt("tasktype"));
				obj.setTaskName(rs.getString("taskName")==null?"":rs.getString("taskName"));
				obj.setTaskStep(rs.getString("steps")==null?"":rs.getString("steps"));
				obj.setPrviousTaskLinkId(rs.getInt("linkid"));
				obj.setCustomerRefKey(rs.getInt("customerrefKey"));
				obj.setProjectKey(rs.getInt("projectrefKey"));
				obj.setSreviceId(rs.getInt("servicerefKey"));
				obj.setEmployeeid(rs.getInt("assignedUser"));
				obj.setCcEmployeeKey(rs.getInt("ccemployeeKey"));
				obj.setPriorityRefKey(rs.getInt("priorityrefKey"));
				obj.setEstimatatedNumber(rs.getDouble("estTime"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setActualNumber(rs.getDouble("actualTime"));
				obj.setStatusKey(rs.getInt("status"));
				obj.setPriorityNAme(rs.getString("TaskPriorityStr")==null?"":rs.getString("TaskPriorityStr"));
				obj.setTaskType(rs.getString("TaskTYpeStr")==null?"":rs.getString("TaskTYpeStr"));
				obj.setStatusName(rs.getString("TaskStatusStr")==null?"":rs.getString("TaskStatusStr"));
				obj.setProjectName(rs.getString("project_Name")==null?"":rs.getString("project_Name"));
				obj.setEmployeeName(rs.getString("employeename")==null?"":rs.getString("employeename"));
				obj.setHoursOrDays(rs.getString("hourOrDays")==null?"":rs.getString("hourOrDays"));
				obj.setServiceName(rs.getString("serviceNAme")==null?"":rs.getString("serviceNAme"));
				obj.setCcEmployeeName(rs.getString("employeeCcname")==null?"":rs.getString("employeeCcname"));
				obj.setExpectedDatetofinish(rs.getDate("expectedDateTofinsh"));
				obj.setClientType(rs.getString("customerType")==null?"":rs.getString("customerType"));
				obj.setCreatedAutommaticTask(rs.getString("createdAutomaticFeedback")==null?"":rs.getString("createdAutomaticFeedback"));
				obj.setCustomerNamefromFeedback(rs.getString("customerNameFeedback")==null?"":rs.getString("customerNameFeedback"));
				//obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				if(obj.getClientType().equalsIgnoreCase("P"))
				{
					obj.setCustomerNAme(rs.getString("prospectiveName")==null?"":rs.getString("prospectiveName"));
					obj.setClientTypeFullName("Prospective");
				}
				else
				{
					obj.setCustomerNAme(rs.getString("fullname")==null?"":rs.getString("fullname"));
					obj.setClientTypeFullName("Customer");
				}
				
				if(rs.getDate("expectedDateTofinsh")!=null)
				obj.setExpectedDatetofinishStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expectedDateTofinsh")));
				else
					obj.setExpectedDatetofinishStr("");	
				
				if(rs.getDate("reminderDate")!=null)
					obj.setReminderDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("reminderDate")));
					else
					obj.setReminderDateStr("");	
				obj.setReminderDate(rs.getDate("reminderDate"));
				obj.setFeedbackKey(rs.getInt("feedBackKey"));
				
				if(obj.getFeedbackKey()>0)
				{
					obj.setFeedbackNo(rs.getString("enquiry_no")==null?"":rs.getString("enquiry_no"));
					obj.setHideFeedbackButton(true);
				}
				else
				{
					obj.setFeedbackNo("Not From FeedBack");
					obj.setHideFeedbackButton(false);
				}
				obj.setRemindIn(rs.getDouble("toBeReminderIn"));
				obj.setPreviossTaskName("");
				if(obj.getTaskid()>0)
				{
					String History="";
					int firstTimeOnly=99;
					List<QuotationAttachmentModel> taskAttchmnetTemp=new ArrayList<QuotationAttachmentModel>();
					for(TasksModel tasksModel:taskHistory)
					{
						if(tasksModel.getTaskid()==obj.getTaskid())
						{
							History=History+tasksModel.getCreationDateStr()+"\n"+tasksModel.getComments()+"\n"+"--------------------------"+"\n \n";
							if(firstTimeOnly==99)
							{
								obj.setMostRecentUpdate(tasksModel.getCreationDateStr());	
								obj.setUpdatedTime(tasksModel.getCreationDate());							
								obj.setComments(tasksModel.getComments());
								firstTimeOnly=100;
							}
						}
					}
					for(QuotationAttachmentModel attachmentModel:taskAttchmnet)
					{
						if(attachmentModel.getAttachid()==obj.getTaskid())
						{
							taskAttchmnetTemp.add(attachmentModel);
						}
					}
					obj.setHistory(History);
					obj.setListOfattchments(taskAttchmnetTemp);
				
				}
				list.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in TaskData---getAllTask-->" , ex);
		}
		return list;
	}
	
	
	public List<TasksModel> getAllTaskOtherThanCurrentTask(int taskId)
	{
	
		List<TasksModel> list=new ArrayList<TasksModel>();
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		
		TasksModel obj=new TasksModel();
		obj.setTaskid(0);
		obj.setTaskName("Select");
		list.add(obj);
		
		try 
		{
			rs=db.executeNonQuery(query.getAllTaskOtherThanCurrentTask(taskId));
			while(rs.next())
			{	
				obj=new TasksModel();
				obj.setTaskid(rs.getInt("taskID"));
				obj.setCreationDate(rs.getDate("createDate"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createDate")));
				obj.setCreatedUserID(rs.getInt("createdUser"));
				obj.setTaskNumber(rs.getString("taskNo"));
				obj.setTaskTypeId(rs.getInt("tasktype"));
				obj.setTaskName(rs.getString("taskName")==null?"":rs.getString("taskName"));
				obj.setTaskStep(rs.getString("steps")==null?"":rs.getString("steps"));
				obj.setPrviousTaskLinkId(rs.getInt("linkid"));
				obj.setCustomerRefKey(rs.getInt("customerrefKey"));
				obj.setProjectKey(rs.getInt("projectrefKey"));
				obj.setSreviceId(rs.getInt("servicerefKey"));
				obj.setEmployeeid(rs.getInt("assignedUser"));
				obj.setCcEmployeeKey(rs.getInt("ccemployeeKey"));
				obj.setPriorityRefKey(rs.getInt("priorityrefKey"));
				obj.setEstimatatedNumber(rs.getDouble("estTime"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setActualNumber(rs.getDouble("actualTime"));
				obj.setStatusKey(rs.getInt("status"));
				obj.setComments(rs.getString("usercomments")==null?"":rs.getString("usercomments"));
				obj.setHoursOrDays(rs.getString("hourOrDays")==null?"":rs.getString("hourOrDays"));
				obj.setExpectedDatetofinish(rs.getDate("expectedDateTofinsh"));
				if(rs.getDate("expectedDateTofinsh")!=null)
				obj.setExpectedDatetofinishStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expectedDateTofinsh")));
				else
					obj.setExpectedDatetofinishStr("");	
				
				if(rs.getDate("reminderDate")!=null)
					obj.setReminderDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("reminderDate")));
					else
					obj.setReminderDateStr("");	
				obj.setReminderDate(rs.getDate("reminderDate"));
				
				obj.setRemindIn(rs.getDouble("toBeReminderIn"));
				obj.setFeedbackKey(rs.getInt("feedBackKey"));
				if(obj.getFeedbackKey()>0)
				{
					obj.setHideFeedbackButton(true);
				}
				else
				{
					obj.setHideFeedbackButton(false);
				}
				obj.setClientType(rs.getString("customerType")==null?"":rs.getString("customerType"));
				obj.setCreatedAutommaticTask(rs.getString("createdAutomaticFeedback")==null?"":rs.getString("createdAutomaticFeedback"));
				obj.setCustomerNamefromFeedback(rs.getString("customerNameFeedback")==null?"":rs.getString("customerNameFeedback"));
				list.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in TaskData---getAllTaskOtherThanCurrentTask-->" , ex);
		}
		return list;
	}
	
	public TasksModel getTaskById(int taskId)
	{
		TasksModel obj=new TasksModel();
		
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getTaskById(taskId));
			while(rs.next())
			{				
				obj.setTaskid(rs.getInt("taskID"));
				obj.setCreationDate(rs.getDate("createDate"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createDate")));
				obj.setCreatedUserID(rs.getInt("createdUser"));
				obj.setCreatedUserName(rs.getString("CreatedbyUser"));				
				obj.setTaskNumber(rs.getString("taskNo"));
				obj.setTaskTypeId(rs.getInt("tasktype"));
				obj.setTaskName(rs.getString("taskName")==null?"":rs.getString("taskName"));
				obj.setTaskStep(rs.getString("steps")==null?"":rs.getString("steps"));
				obj.setPrviousTaskLinkId(rs.getInt("linkid"));
				obj.setCustomerRefKey(rs.getInt("customerrefKey"));
				obj.setProjectKey(rs.getInt("projectrefKey"));
				obj.setSreviceId(rs.getInt("servicerefKey"));
				obj.setEmployeeid(rs.getInt("assignedUser"));
				obj.setCcEmployeeKey(rs.getInt("ccemployeeKey"));
				obj.setPriorityRefKey(rs.getInt("priorityrefKey"));
				obj.setEstimatatedNumber(rs.getDouble("estTime"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setActualNumber(rs.getDouble("actualTime"));
				obj.setStatusKey(rs.getInt("status"));
				obj.setStatusName(rs.getString("TaskStatusStr")==null?"":rs.getString("TaskStatusStr"));
				obj.setComments(rs.getString("usercomments")==null?"":rs.getString("usercomments"));
				obj.setHoursOrDays(rs.getString("hourOrDays")==null?"":rs.getString("hourOrDays"));
				obj.setExpectedDatetofinish(rs.getDate("expectedDateTofinsh"));
				if(rs.getDate("expectedDateTofinsh")!=null)
				obj.setExpectedDatetofinishStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expectedDateTofinsh")));
				else
				obj.setExpectedDatetofinishStr("");	
				
				if(rs.getDate("reminderDate")!=null)
					obj.setReminderDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("reminderDate")));
					else
					obj.setReminderDateStr("");	
				obj.setReminderDate(rs.getDate("reminderDate"));
				obj.setFeedbackKey(rs.getInt("feedBackKey"));
				if(obj.getFeedbackKey()>0)
				{
					obj.setHideFeedbackButton(true);
				}
				else
				{
					obj.setHideFeedbackButton(false);
				}
				obj.setRemindIn(rs.getDouble("toBeReminderIn"));
				obj.setClientType(rs.getString("customerType")==null?"":rs.getString("customerType"));
				obj.setCreatedAutommaticTask(rs.getString("createdAutomaticFeedback")==null?"":rs.getString("createdAutomaticFeedback"));
				obj.setCustomerNamefromFeedback(rs.getString("customerNameFeedback")==null?"":rs.getString("customerNameFeedback"));
			}
		}
		
		catch (Exception ex) {
			logger.error("error in TaskData---getTaskById-->" , ex);
		}
		return obj;
	}
	
	
	public List<TasksModel> getTaskDeatils(int taskId)
	{
		List<TasksModel> lst=new ArrayList<TasksModel>();
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getTaskDeatils(taskId));
			while(rs.next())
			{				
				TasksModel obj=new TasksModel();
				obj.setTaskid(rs.getInt("taskId"));
				obj.setStatusKey(rs.getInt("status"));
				obj.setComments(rs.getString("usercomments")==null?"":rs.getString("usercomments"));
				obj.setActualNumber(rs.getDouble("actualTime"));
				obj.setCreatedUserID(rs.getInt("UserId"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("DateTime")));
				obj.setCreationDate(rs.getDate("DateTime"));
				obj.setCreatedUserName(rs.getString("CreatedbyUser"));		
				obj.setStatusName(rs.getString("StatusName"));							
				lst.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in TaskData---getTaskDeatils-->" , ex);
		}
		return lst;
	}
	
	
	public List<QuotationAttachmentModel> getTaskAttchamnet(int taskId)
	{
		List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getTaskAttchamnet(taskId));
			while(rs.next())
			{			
				QuotationAttachmentModel obj=new QuotationAttachmentModel();
				obj.setAttachid(rs.getInt("taskID"));
				obj.setFilepath(rs.getString("attachmentpath")==null?"":rs.getString("attachmentpath"));
				obj.setFilename(rs.getString("fileName")==null?"":rs.getString("fileName"));
				lst.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in TaskData---getTaskAttchamnet-->" , ex);
		}
		return lst;
	}
	
	public int addTask(TasksModel obj,List<QuotationAttachmentModel> attachmentModels)
	{
		int result=0;
		
		TaskQueery query=new TaskQueery();		
		try 
		{			
			result=db.executeUpdateQuery(query.addTask(obj));		
			if(result>0)
			{
				result=db.executeUpdateQuery(query.addTaskDetails(obj));
				String path="";
				String creationPath="";
				String repository="";
				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"Tasks"+File.separator+obj.getTaskNumber()+File.separator+objAtt.getFilename();
						creationPath=repository+"Tasks"+File.separator+obj.getTaskNumber()+"";
						if(objAtt.getImageMedia()!=null)
						{
							customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						}
						else
						{
							File aFile = new File(objAtt.getFilepath());
							InputStream is = new FileInputStream(aFile);
							byte[] buff = new byte[8000];
							int bytesRead = 0;
							ByteArrayOutputStream bao = new ByteArrayOutputStream();
							while((bytesRead = is.read(buff)) != -1) {
					             bao.write(buff, 0, bytesRead);
					        }
					        byte[] data = bao.toByteArray();
					        ByteArrayInputStream bin = new ByteArrayInputStream(data);
					        customerData.createFile(bin,creationPath,objAtt.getFilename());
						}
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addTaskAttachmnetPath(obj));
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in TaskData---addTask-->" , ex);
		}
		return result;
		
	}
	
	public int editTask(TasksModel obj,List<QuotationAttachmentModel> attachmentModels)
	{
		int result=0;
		
		TaskQueery query=new TaskQueery();		
		try 
		{			
			result=db.executeUpdateQuery(query.editTask(obj));		
			if(result>0)
			{
				result=db.executeUpdateQuery(query.addTaskDetails(obj));
				String path="";
				String creationPath="";
				String repository="";
				if(attachmentModels!=null && attachmentModels.size()>0)
				db.executeUpdateQuery(query.deleteAllTaskAttachmnetPathbyId(obj));
				
				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"Tasks"+File.separator+obj.getTaskNumber()+File.separator+objAtt.getFilename();
						creationPath=repository+"Tasks"+File.separator+obj.getTaskNumber()+"";
						if(objAtt.getImageMedia()!=null)
						customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addTaskAttachmnetPath(obj));
						
						
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in TaskData---editTask-->" , ex);
		}
		return result;
		
	}
	
	
	
	public boolean checkIfTaskNumberIsDuplicate(String taskNumber,int taskID)
	{
		boolean hasSerialNumber=false;		
		
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfTaskNumberIsDuplicate(taskNumber,taskID));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in TaskData---checkIfTaskNumberIsDuplicate-->" , ex);
		}
		
		return hasSerialNumber;
	}
	
	
	public TasksModel navigationTask(int taskID,int webUserID,String navigation,String actionTYpe)
	{
		TasksModel obj=new TasksModel();
		
		TaskQueery query=new TaskQueery();
		ResultSet rs = null;
		try 
		{
			if(navigation.equalsIgnoreCase("prev") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view")))
			{
				rs=db.executeNonQuery(query.getPreviousRecordTask(taskID,webUserID));
			}
			else if(navigation.equalsIgnoreCase("next")&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view")))
			{
				rs=db.executeNonQuery(query.getNextRecordTask(taskID,webUserID));
			}
			else if(navigation.equalsIgnoreCase("next")&& actionTYpe.equalsIgnoreCase("create"))
			{
				rs=db.executeNonQuery(query.getFirstRecordTask(webUserID));
			}
			else if(navigation.equalsIgnoreCase("prev")&& actionTYpe.equalsIgnoreCase("create"))
			{
				rs=db.executeNonQuery(query.getLastRecordTask(webUserID));
			}
			while(rs.next())
			{
				obj.setTaskid(rs.getInt("taskID"));
				obj.setCreationDate(rs.getDate("createDate"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createDate")));
				obj.setCreatedUserID(rs.getInt("createdUser"));
				obj.setTaskNumber(rs.getString("taskNo"));
				obj.setTaskTypeId(rs.getInt("tasktype"));
				obj.setTaskName(rs.getString("taskName")==null?"":rs.getString("taskName"));
				obj.setTaskStep(rs.getString("steps")==null?"":rs.getString("steps"));
				obj.setPrviousTaskLinkId(rs.getInt("linkid"));
				obj.setCustomerRefKey(rs.getInt("customerrefKey"));
				obj.setProjectKey(rs.getInt("projectrefKey"));
				obj.setSreviceId(rs.getInt("servicerefKey"));
				obj.setEmployeeid(rs.getInt("assignedUser"));
				obj.setCcEmployeeKey(rs.getInt("ccemployeeKey"));
				obj.setPriorityRefKey(rs.getInt("priorityrefKey"));
				obj.setEstimatatedNumber(rs.getDouble("estTime"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setActualNumber(rs.getDouble("actualTime"));
				obj.setStatusKey(rs.getInt("status"));
				obj.setComments(rs.getString("usercomments")==null?"":rs.getString("usercomments"));
				obj.setHoursOrDays(rs.getString("hourOrDays")==null?"":rs.getString("hourOrDays"));
				obj.setExpectedDatetofinish(rs.getDate("expectedDateTofinsh"));
				if(rs.getDate("expectedDateTofinsh")!=null)
				obj.setExpectedDatetofinishStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expectedDateTofinsh")));
				else
				obj.setExpectedDatetofinishStr("");	
				
				if(rs.getDate("reminderDate")!=null)
					obj.setReminderDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("reminderDate")));
					else
					obj.setReminderDateStr("");	
				obj.setReminderDate(rs.getDate("reminderDate"));
				obj.setFeedbackKey(rs.getInt("feedBackKey"));
				if(obj.getFeedbackKey()>0)
				{
					obj.setHideFeedbackButton(true);
				}
				else
				{
					obj.setHideFeedbackButton(false);
				}
				obj.setRemindIn(rs.getDouble("toBeReminderIn"));
				obj.setClientType(rs.getString("customerType")==null?"":rs.getString("customerType"));
				obj.setCreatedAutommaticTask(rs.getString("createdAutomaticFeedback")==null?"":rs.getString("createdAutomaticFeedback"));
				obj.setCustomerNamefromFeedback(rs.getString("customerNameFeedback")==null?"":rs.getString("customerNameFeedback"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in TaskData---navigationTask-->" , ex);
		}
		return obj;
	}
	

	public int saveFeedbackCustomerRelation(CustomerFeedbackModel obj)
	{
		int result=0;
		
		TaskQueery query=new TaskQueery();		
		try 
		{			
			result=db.executeUpdateQuery(query.saveFeedbackCustomerRelation(obj));		
		}
		catch (Exception ex) {
			logger.error("error in TaskData---saveFeedbackCustomerRelation-->" , ex);
		}
		return result;
		
	}
	
	public int updateFeedbackTabelwithCustomer(CustomerFeedbackModel obj)
	{
		int result=0;
		
		TaskQueery query=new TaskQueery();		
		try 
		{			
			result=db.executeUpdateQuery(query.updateFeedbackTabelwithCustomer(obj));		
		}
		catch (Exception ex) {
			logger.error("error in TaskData---updateFeedbackTabelwithCustomer-->" , ex);
		}
		return result;
		
	}
	
	///get feedback Data related to task
	
	
	public CustomerFeedbackModel getCutsomerFeedbackById(int feedbackID)
	{

		TaskQueery query=new TaskQueery();
		CustomerFeedbackModel obj=new CustomerFeedbackModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCustomerFeedbackById(feedbackID));
			while(rs.next())
			{	

				obj.setFeedbackKey(rs.getInt("Enquiry_ID"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("ModifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("ModifiedDate")));
				obj.setCompanyName(rs.getString("Customer_Company_Name")==null?"":rs.getString("Customer_Company_Name"));
				obj.setFeedbackNUmber(rs.getString("Enquiry_No")==null?"":rs.getString("Enquiry_No"));
				obj.setContactPersonName(rs.getString("ContactPersonName")==null?"":rs.getString("ContactPersonName"));
				obj.setCustomerRefKey(rs.getInt("CustomerRefKey"));
				//	obj.setCustomerName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setCustomerInitailKey(rs.getInt("ContactPersonIntials"));
				obj.setMemo(rs.getString("Memo")==null?"":rs.getString("Memo"));
				obj.setMobile1(rs.getString("mobile1")==null?"":rs.getString("mobile1"));
				obj.setMobileAreaCode1(rs.getString("mobileAreaCode1")==null?"":rs.getString("mobileAreaCode1"));
				obj.setTelphoneAreaCode1(rs.getString("telephoneAreaCode1")==null?"":rs.getString("telephoneAreaCode1"));
				obj.setTelphone1(rs.getString("telephone1")==null?"":rs.getString("telephone1"));
				obj.setSelectedSoftwareType(rs.getString("Software_Type")==null?"":rs.getString("Software_Type"));
				//obj.setIntialName(rs.getString("description")==null?"":rs.getString("description"));
				obj.setWebsite(rs.getString("website")==null?"":rs.getString("website"));
				obj.setEmail(rs.getString("Email")==null?"":rs.getString("Email"));
				obj.setInstruction(rs.getString("instructions")==null?"":rs.getString("instructions"));
				obj.setCustomerType(rs.getString("TypeOfCutomer")==null?"":rs.getString("TypeOfCutomer"));
				
			}
		}

		catch (Exception ex) {
			logger.error("error in TaskData---getCutsomerFeedbackById-->" , ex);
		}
		return obj;
	}

	
	
	
	
}
					
					
