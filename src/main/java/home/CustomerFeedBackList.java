package home;

import hba.HBAQueries;
import hba.TaskFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import model.CompanyDBModel;
import model.CustomerFeedbackModel;
import model.TaskAndFeeddbackRelation;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Tabbox;

import company.CompanyData;
import db.DBHandler;
import admin.TasksModel;
import setup.users.WebusersModel;

public class CustomerFeedBackList {
	
	//CustomerFeedBackData feedbackData=null;
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CustomerFeedbackModel> lstCustomerFeedback=new ArrayList<CustomerFeedbackModel>();
	private List<CustomerFeedbackModel> lstAllCustomerFeedback=new ArrayList<CustomerFeedbackModel>();
	private CustomerFeedbackModel selectedCustomerFeedBack;
	private boolean adminUser;
	
	private String footer;
	
	private TaskFilter filter=new TaskFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	
	
	
	private int webUserID=0;
	
	WebusersModel dbUser=null;
	
	private int supervisorID=0;
	private int employeeKey=0;
	private int UserId=0;
	
	private List<WebusersModel> lstUsers=new ArrayList<WebusersModel>();
	 
	CompanyData companyData=new CompanyData();
	
	boolean notMappedFeedback=false;
	
	 List<CustomerFeedbackModel> tempNotMappedAllFeedback=new ArrayList<CustomerFeedbackModel>();
	 
	private Date fromDate;
		
	private Date toDate;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{
		
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		
		
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		fromDate=df.parse(sdf.format(c.getTime()));
		c = Calendar.getInstance();	
		toDate=df.parse(sdf.format(c.getTime()));
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE,59);
		c.set(Calendar.SECOND,59);
		
		
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		Session sess = Sessions.getCurrent();		
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		DBHandler mysqldb=new DBHandler();
		ResultSet rs=null;
		CompanyDBModel obj=new CompanyDBModel();
		if (dbUser != null && dbUser.getCompanyid()==1) {
			CompanyDBModel objNew = new CompanyDBModel();
		 	objNew.setUserip("hinawi2.dyndns.org");
		 	objNew.setDbname("ECActualERPData");
		 	objNew.setDbuser("admin");
		 	objNew.setDbpwd("explorer654321");
		 	
		 	/*objNew.setUserip("CHADIRAHME-PC\\SQLEXPRESS");
			objNew.setDbname("ECActualERPData");
			objNew.setDbuser("sa");
			objNew.setDbpwd("123456");*/
			
		 	//feedbackData=new CustomerFeedBackData(objNew);
		}
		else
		{
			HBAQueries query = new HBAQueries();
			rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
					.getCompanyid()));
			while (rs.next()) {
				obj.setCompanyId(rs.getInt("companyid"));
				obj.setDbid(rs.getInt("dbid"));
				obj.setUserip(rs.getString("userip"));
				obj.setDbname(rs.getString("dbname"));
				obj.setDbuser(rs.getString("dbuser"));
				obj.setDbpwd(rs.getString("dbpwd"));
				obj.setDbtype(rs.getString("dbtype"));
			}
			//feedbackData=new CustomerFeedBackData(obj);
		}
		
		
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
		
	/*	lstTaskStatus=hrData.getHRListValues(143,"All");
		
		lstAssignToEmployees=hrData.getEmployeeList(0,"ALL","A",supervisorID);*/
		
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
			
		
		}
			
		footer="Total No. of Feedback "+lstCustomerFeedback.size();
		}
		catch(Exception e)
		{
			logger.error("error in CustomerFeedBackList---int-->" , e);
		}
		//Messagebox.show(type);
    }

	
	@Command
    @NotifyChange({"lstCustomerFeedback","footer"})
    public void searchFeedBack() 
    {
    	try
    	{
    		
    			
    		//lstCustomerFeedback=feedbackData.getAllCutsomerFeedback(0,fromDate,toDate);
    		for(CustomerFeedbackModel customerFeedbackModel:lstCustomerFeedback )
    		{
    			for(TaskAndFeeddbackRelation modeltaskRelation:customerFeedbackModel.getTaskRelationlist())
    			{
    				for(WebusersModel model:lstUsers)
    				{
    					if(modeltaskRelation.getUserId()==model.getUserid())
    					{
    						modeltaskRelation.setUserName(model.getFirstname());
    						break;
    					}
    					else if(modeltaskRelation.getUserId()==0)
    					{
    						modeltaskRelation.setUserName("Admin");
    						break;
    					}
    				}
    			}
    		}
    		
    		lstAllCustomerFeedback.clear();
    		lstAllCustomerFeedback.addAll(lstCustomerFeedback);
    		tempNotMappedAllFeedback.clear();
    		tempNotMappedAllFeedback.addAll(lstCustomerFeedback);
    			footer="Total No. of Feedbacks "+lstCustomerFeedback.size();
	  
    	}
    	catch (Exception ex) {
			logger.error("error in CustomerFeedBackList---searchFeedBack-->" , ex);
		}
    	
    }
	


	
	private List<CustomerFeedbackModel> filterData()
	{
		lstCustomerFeedback=lstAllCustomerFeedback;
		List<CustomerFeedbackModel>  lst=new ArrayList<CustomerFeedbackModel>();
		for (Iterator<CustomerFeedbackModel> i = lstCustomerFeedback.iterator(); i.hasNext();)
		{
			CustomerFeedbackModel tmp=i.next();				
			if(tmp.getFeedbackNUmber().toLowerCase().contains(filter.getFeedbackNUmber().toLowerCase())&&
					tmp.getCompanyName().toLowerCase().contains(filter.getCompanyName().toLowerCase())&&
					tmp.getIntialName().toLowerCase().contains(filter.getIntialName().toLowerCase())&&
					tmp.getCustomerType().toLowerCase().contains(filter.getCustomerType().toLowerCase())&&
					tmp.getContactPersonName().toLowerCase().contains(filter.getContactPersonName().toLowerCase())&&
					tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())&&
					tmp.getSelectedSoftwareType().toLowerCase().contains(filter.getSelectedSoftwareType().toLowerCase())&&
					tmp.getMobile1().toLowerCase().contains(filter.getMobile1().toLowerCase())&&
					tmp.getTelphone1().toLowerCase().contains(filter.getTelphone1().toLowerCase())&&
					tmp.getMemo().toLowerCase().contains(filter.getMemo().toLowerCase())&&
					tmp.getCreatedDateStr().toLowerCase().contains(filter.getCreatedDateStr().toLowerCase())&&
					tmp.getModifeldDateStr().toLowerCase().contains(filter.getModifeldDateStr().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	@Command
    @NotifyChange({"lstCustomerFeedback","footer"})
    public void changeFilter() 
    {
    	try
    	{
    		lstCustomerFeedback=filterData();
    		footer="Total No. of Feedbacks "+lstCustomerFeedback.size();
	  
    	}
    	catch (Exception ex) {
			logger.error("error in CustomerFeedBackList---changeFilter-->" , ex);
		}
    	
    }
	
	 @Command
	   public void viewCustomerFeedback(@BindingParam("row") CustomerFeedbackModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("feedBackKey", row.getFeedbackKey());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/crm/editCustomerFeedback.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> viewCustomerFeedback", ex);			
			}
	   }
	   
	   @GlobalCommand 
	   @NotifyChange({"lstCustomerFeedback","footer"})
	    public void refreshParentFeedBackForm(@BindingParam("type")String type)
			  {		
				 try
				  {
				
						//lstCustomerFeedback=feedbackData.getAllCutsomerFeedback(0,fromDate,toDate);
						for(CustomerFeedbackModel customerFeedbackModel:lstCustomerFeedback )
			    		{
			    			for(TaskAndFeeddbackRelation modeltaskRelation:customerFeedbackModel.getTaskRelationlist())
			    			{
			    				for(WebusersModel model:lstUsers)
			    				{
			    					if(modeltaskRelation.getUserId()==model.getUserid())
			    					{
			    						modeltaskRelation.setUserName(model.getFirstname());
			    						break;
			    					}
			    					else if(modeltaskRelation.getUserId()==0)
			    					{
			    						modeltaskRelation.setUserName("Admin");
			    						break;
			    					}
			    				}
			    			}
			    		}
						lstAllCustomerFeedback.clear();
			    		lstAllCustomerFeedback.addAll(lstCustomerFeedback);
			    		tempNotMappedAllFeedback.clear();
			    		tempNotMappedAllFeedback.addAll(lstCustomerFeedback);
			    			footer="Total No. of Feedbacks "+lstCustomerFeedback.size();
				  
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in CustomerFeedBackList ----> refreshParentFeedBackForm", ex);			
					}
			  }
	   
	   @Command
	   public void editCustomerFeedBack(@BindingParam("row") CustomerFeedbackModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("feedBackKey", row.getFeedbackKey());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/crm/editCustomerFeedback.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> editCustomerFeedBack", ex);			
			}
	   }
	   
	   @Command
	   public void addTask(@BindingParam("row") CustomerFeedbackModel row)
	   {
		   try
		   {
			   if(row!=null && row.getCustomerRefKey()>0)
			   {
				   Map<String,Object> arg = new HashMap<String,Object>();
				   arg.put("taskKey", 0);
				   arg.put("customerKey", row.getCustomerRefKey());
				   arg.put("cutomerType",row.getCustomerType());
				   arg.put("type","OtherForms");
				   arg.put("memo",row.getMemo());
				   arg.put("attchmnt",row.getLstAtt());
				   Executions.createComponents("/hba/list/editTask.zul", null,arg);
			   }
			   else
			   {
				   Clients.showNotification("Please Map the customer to this feed back before create task",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				   return;
			   }
			   
			  
			   
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> addTask", ex);			
			}
	   }
	   
	   
	   @Command
	   public void editTask(@BindingParam("row") CustomerFeedbackModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("taskKey", row.getTaskID());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editTask.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> editTask", ex);			
			}
	   }
	   
	   
	   @Command
	   public void resetCustomerFeedBack()
	   {
		   try
		   {
			   Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				 Center center = bl.getCenter();
				 Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				 tabbox.getSelectedPanel().getLastChild().invalidate();
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> resetCustomerFeedBack", ex);			
			}
	   }
	   
	   
	   
	   @Command
	   public void groupOfTask()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("taskKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","add");
			   Executions.createComponents("/hba/list/groupOfTask.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> groupOfTask", ex);			
			}
	   }
	   
	   
	   @Command
	   public void openTask(@BindingParam("taskId") int taskKey)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("taskKey", taskKey);
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/hba/list/editTask.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedBackList ----> viewTask", ex);			
			}
	   }
	   
	   




	/**
	 * @return the lstCustomerFeedback
	 */
	public List<CustomerFeedbackModel> getLstCustomerFeedback() {
		return lstCustomerFeedback;
	}


	/**
	 * @param lstCustomerFeedback the lstCustomerFeedback to set
	 */
	public void setLstCustomerFeedback(List<CustomerFeedbackModel> lstCustomerFeedback) {
		this.lstCustomerFeedback = lstCustomerFeedback;
	}


	/**
	 * @return the lstAllCustomerFeedback
	 */
	public List<CustomerFeedbackModel> getLstAllCustomerFeedback() {
		return lstAllCustomerFeedback;
	}


	/**
	 * @param lstAllCustomerFeedback the lstAllCustomerFeedback to set
	 */
	public void setLstAllCustomerFeedback(List<CustomerFeedbackModel> lstAllCustomerFeedback) {
		this.lstAllCustomerFeedback = lstAllCustomerFeedback;
	}


	/**
	 * @return the selectedCustomerFeedBack
	 */
	public CustomerFeedbackModel getSelectedCustomerFeedBack() {
		return selectedCustomerFeedBack;
	}


	/**
	 * @param selectedCustomerFeedBack the selectedCustomerFeedBack to set
	 */
	public void setSelectedCustomerFeedBack(CustomerFeedbackModel selectedCustomerFeedBack) {
		this.selectedCustomerFeedBack = selectedCustomerFeedBack;
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


	/**
	 * @return the lstPageSize
	 */
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	/**
	 * @param lstPageSize the lstPageSize to set
	 */
	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	/**
	 * @return the selectedPageSize
	 */
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	/**
	 * @param selectedPageSize the selectedPageSize to set
	 */
	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	/**
	 * @return the lstAllPageSize
	 */
	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	/**
	 * @param lstAllPageSize the lstAllPageSize to set
	 */
	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	/**
	 * @return the selectedAllPageSize
	 */
	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	/**
	 * @param selectedAllPageSize the selectedAllPageSize to set
	 */
	@NotifyChange({"selectedPageSize"})	
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if(selectedAllPageSize.equalsIgnoreCase("All"))
		{
				selectedPageSize=lstAllCustomerFeedback.size();
			
		}
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}

	/**
	 * @return the filter
	 */
	public TaskFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(TaskFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	   @Command
		public void download(@BindingParam("row") CustomerFeedbackModel obj)
		{
			if(obj.getLstAtt()!=null && !obj.getSelectedAttchemnets().getFilepath().equalsIgnoreCase(""))
			{
			File file=new File(obj.getSelectedAttchemnets().getFilepath());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String mimeType=mimeTypesMap.getContentType(file);

			try {
				Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, obj.getSelectedAttchemnets().getFilename()); 
				
			}catch (FileNotFoundException e)
			{
				 Clients.showNotification("There Is No Such File in server to download.(May be Deleted)",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				 return;
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("ERROR in TaskViewModel ----> download", e);	
			}
			
			}
			else
			{
				Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		}


	/**
	 * @return the notMappedFeedback
	 */
	public boolean isNotMappedFeedback() {
		return notMappedFeedback;
	}


	/**
	 * @param notMappedFeedback the notMappedFeedback to set
	 */
	public void setNotMappedFeedback(boolean notMappedFeedback) {
		this.notMappedFeedback = notMappedFeedback;
	}


	/**
	 * @return the tempNotMappedAllFeedback
	 */
	public List<CustomerFeedbackModel> getTempNotMappedAllFeedback() {
		return tempNotMappedAllFeedback;
	}


	/**
	 * @param tempNotMappedAllFeedback the tempNotMappedAllFeedback to set
	 */
	public void setTempNotMappedAllFeedback(
			List<CustomerFeedbackModel> tempNotMappedAllFeedback) {
		this.tempNotMappedAllFeedback = tempNotMappedAllFeedback;
	} 
	   
	   
	  @Command
	   @NotifyChange({"lstCustomerFeedback","footer"})
		public void getNotMappedTocustomerTasks() {
			 if(notMappedFeedback)
			 {
				 List<CustomerFeedbackModel> tempNotMappedFeedback=new ArrayList<CustomerFeedbackModel>();
				 for(CustomerFeedbackModel tasksModel:lstAllCustomerFeedback)
				 {
					 if(tasksModel.getCustomerRefKey()==0)
					 {
						 tempNotMappedFeedback.add(tasksModel);
					 }
				 
				 }
				 lstCustomerFeedback.clear();
				 lstCustomerFeedback.addAll(tempNotMappedFeedback);
				 lstAllCustomerFeedback.clear();
				 lstAllCustomerFeedback.addAll(lstCustomerFeedback);
				 
			 }
			 else
			 {
				 
				 lstCustomerFeedback.clear();
				 lstCustomerFeedback.addAll(tempNotMappedAllFeedback);
				 lstAllCustomerFeedback.clear();
				 lstAllCustomerFeedback.addAll(tempNotMappedAllFeedback);
			 }
			 footer="Total No. of Feedback "+lstCustomerFeedback.size();
		}


	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}


	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}


	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}


	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	   


}
