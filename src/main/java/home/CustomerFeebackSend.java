package home;


import hba.HBAQueries;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import hba.TaskFilter;
import layout.MenuModel;
import model.CompanyDBModel;
import model.CustomerFeedbackModel;
import model.EmailSignatureModel;
import model.ReminderSettingsModel;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import common.HREnum;
import company.ReminderData;
import db.DBHandler;
import setup.users.WebusersModel;
import timesheet.TimeSheetData;

public class CustomerFeebackSend {

	//CustomerFeedBackData feedbackData=null;
	//HBAData hbadata=new HBAData();
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CustomerFeedbackModel> lstCustomerFeedback=new ArrayList<CustomerFeedbackModel>();
	private List<CustomerFeedbackModel> lstAllCustomerFeedback=new ArrayList<CustomerFeedbackModel>();


	private List<CustomerFeedbackModel> lstDrafEmail=new ArrayList<CustomerFeedbackModel>();
	private List<CustomerFeedbackModel> lstDrafEmailAll=new ArrayList<CustomerFeedbackModel>();
	private CustomerFeedbackModel selectedDraftEmail;

	private List<CustomerFeedbackModel> lstScheduledEmail=new ArrayList<CustomerFeedbackModel>();
	private List<CustomerFeedbackModel> lstScheduledEmailAll=new ArrayList<CustomerFeedbackModel>();
	private CustomerFeedbackModel selectedScheduledEmail;

	private List<CustomerFeedbackModel> inBoxEmail=new ArrayList<CustomerFeedbackModel>();
	private List<CustomerFeedbackModel> inBoxEmailAll=new ArrayList<CustomerFeedbackModel>();
	private CustomerFeedbackModel selectedinBoxEmail;

	private CustomerFeedbackModel selectedCustomerFeedBack;
	private boolean adminUser;

	private String footer;
	private String footerDraft;
	private String footerScheduled;


	private TaskFilter filter=new TaskFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;

	ReminderData data=new ReminderData();




	private int webUserID=0;

	WebusersModel dbUser=null;

	private int supervisorID=0;
	private int employeeKey=0;
	private int UserId=0;
	
	private String sourceSerach="";
	
	private String sentEmailSearch="";
	private MenuModel companyRole;


	@Init
	public void init(@BindingParam("type") String type)
	{
		try{
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

			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");			
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
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



			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");

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

			footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();

		}
		catch(Exception e)
		{
			logger.error("ERROR in SignatureViewModel ----> init", e);			
		}
		//Messagebox.show(type);
	}

	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		TimeSheetData data=new TimeSheetData();
		List<MenuModel> lstRoles= data.getRolesPermessions(companyRoleId,HREnum.MenuIds.CRM.getValue());
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==HREnum.MenuIds.CRMSendEmail.getValue())
			{
				companyRole=item;
				break;
			}
		}
	}

	@Command
	@NotifyChange({"lstCustomerFeedback","lstDrafEmail","lstScheduledEmail","footer","footerDraft","footerScheduled"})
	public void searchFeedBackSent() 
	{
		try
		{


//			lstCustomerFeedback=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"S");//sent emails
//			lstAllCustomerFeedback=lstCustomerFeedback;
//
//			lstDrafEmail=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"D");//drafted enmails
//			lstDrafEmailAll=lstDrafEmail;
//
//			lstScheduledEmail=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"SH");//scheduled Email's
//			lstScheduledEmailAll=lstScheduledEmail;
//			footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();
//			footerDraft="Total No. of E-mails Drafted "+lstDrafEmail.size();
//			footerScheduled="Total No. of E-mails Scheduled "+lstScheduledEmail.size();

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeebackSend---searchFeedBackSent-->" , ex);
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
					tmp.getCreatedDateStr().toLowerCase().contains(filter.getCreatedDateStr().toLowerCase())&&
					tmp.getModifeldDateStr().toLowerCase().contains(filter.getModifeldDateStr().toLowerCase())&&
					tmp.getSentFromEmail().toLowerCase().contains(filter.getSentFromEmail().toLowerCase())&&
					tmp.getSubject().toLowerCase().contains(filter.getSubject().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;

	}
	
	private List<CustomerFeedbackModel> filterDataDraft()
	{
		lstCustomerFeedback=lstDrafEmailAll;
		List<CustomerFeedbackModel>  lst=new ArrayList<CustomerFeedbackModel>();
		for (Iterator<CustomerFeedbackModel> i = lstCustomerFeedback.iterator(); i.hasNext();)
		{
			CustomerFeedbackModel tmp=i.next();				
			if(tmp.getFeedbackNUmber().toLowerCase().contains(filter.getFeedbackNUmber().toLowerCase())&&
					tmp.getCreatedDateStr().toLowerCase().contains(filter.getCreatedDateStr().toLowerCase())&&
					tmp.getModifeldDateStr().toLowerCase().contains(filter.getModifeldDateStr().toLowerCase())&&
					tmp.getSentFromEmail().toLowerCase().contains(filter.getSentFromEmail().toLowerCase())&&
					tmp.getSubject().toLowerCase().contains(filter.getSubject().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;

	}
	
	private List<CustomerFeedbackModel> filterDataReminder()
	{
		lstDrafEmail=lstScheduledEmailAll;
		List<CustomerFeedbackModel>  lst=new ArrayList<CustomerFeedbackModel>();
		for (Iterator<CustomerFeedbackModel> i = lstDrafEmail.iterator(); i.hasNext();)
		{
			CustomerFeedbackModel tmp=i.next();				
			if(tmp.getFeedbackNUmber().toLowerCase().contains(filter.getFeedbackNUmber().toLowerCase())&&
					tmp.getCreatedDateStr().toLowerCase().contains(filter.getCreatedDateStr().toLowerCase())&&
					tmp.getModifeldDateStr().toLowerCase().contains(filter.getModifeldDateStr().toLowerCase())&&
					tmp.getSentFromEmail().toLowerCase().contains(filter.getSentFromEmail().toLowerCase())&&
					tmp.getSubject().toLowerCase().contains(filter.getSubject().toLowerCase())
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
			footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeebackSend---changeFilter-->" , ex);
		}

	}
	
	
	@Command
	@NotifyChange({"footer","lstScheduledEmail"})
	public void changeFilterReminder() 
	{
		try
		{
			lstScheduledEmail=filterDataReminder();
			//footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeebackSend---changeFilter-->" , ex);
		}

	}
	
	
	@Command
	@NotifyChange({"footer","lstDrafEmail"})
	public void changeFilterDrfat() 
	{
		try
		{
			lstDrafEmail=filterDataDraft();
			//footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeebackSend---changeFilter-->" , ex);
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
			Executions.createComponents("/crm/editCustomerFeedbackSend.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerFeebackSend ----> viewCustomerFeedback", ex);			
		}
	}


	@Command
	public void AddCustomerFeedback(@BindingParam("row") CustomerFeedbackModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("feedBackKey", 0);
			arg.put("compKey",0);
			arg.put("type","add");
			Executions.createComponents("/crm/editCustomerFeedbackSend.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerFeebackSend ----> viewCustomerFeedback", ex);			
		}
	}

	@GlobalCommand 
	@NotifyChange({"lstCustomerFeedback","lstDrafEmail","lstScheduledEmail","footer","footerDraft","footerScheduled"})
	public void refreshParentFeedBackSentForm(@BindingParam("type")String type)
	{		
		try
		{
//			lstCustomerFeedback=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"S");//sent emails
//			lstAllCustomerFeedback=lstCustomerFeedback;
//
//			lstDrafEmail=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"D");//drafted enmails
//			lstDrafEmailAll=lstDrafEmail;
//
//			lstScheduledEmail=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"SH");//scheduled Email's
//			lstScheduledEmailAll=lstScheduledEmail;
//			footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();
//			footerDraft="Total No. of E-mails Drafted "+lstDrafEmail.size();
//			footerScheduled="Total No. of E-mails Scheduled "+lstScheduledEmail.size();

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerFeebackSend ----> refreshParentFeedBackSentForm", ex);			
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
			Executions.createComponents("/crm/editCustomerFeedbackSend.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerFeebackSend ----> editCustomerFeedBack", ex);			
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
			logger.error("ERROR in CustomerFeebackSend ----> addTask", ex);			
		}
	}

	@Command
	public void resetCustomerFeedBackSent()
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
			logger.error("ERROR in CustomerFeebackSend ----> resetCustomerFeedBackSent", ex);			
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
			logger.error("ERROR in CustomerFeebackSend ----> groupOfTask", ex);			
		}
	}


	@Command
	public void setUpEmailSignature()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			Executions.createComponents("/crm/emailSignature.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in setUpEmailSignature ----> groupOfTask", ex);			
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
	 * @return the lstDrafEmail
	 */
	public List<CustomerFeedbackModel> getLstDrafEmail() {
		return lstDrafEmail;
	}


	/**
	 * @param lstDrafEmail the lstDrafEmail to set
	 */
	public void setLstDrafEmail(List<CustomerFeedbackModel> lstDrafEmail) {
		this.lstDrafEmail = lstDrafEmail;
	}


	/**
	 * @return the selectedDraftEmail
	 */
	public CustomerFeedbackModel getSelectedDraftEmail() {
		return selectedDraftEmail;
	}


	/**
	 * @param selectedDraftEmail the selectedDraftEmail to set
	 */
	public void setSelectedDraftEmail(CustomerFeedbackModel selectedDraftEmail) {
		this.selectedDraftEmail = selectedDraftEmail;
	}


	/**
	 * @return the lstScheduledEmail
	 */
	public List<CustomerFeedbackModel> getLstScheduledEmail() {
		return lstScheduledEmail;
	}


	/**
	 * @param lstScheduledEmail the lstScheduledEmail to set
	 */
	public void setLstScheduledEmail(List<CustomerFeedbackModel> lstScheduledEmail) {
		this.lstScheduledEmail = lstScheduledEmail;
	}


	/**
	 * @return the selectedScheduledEmail
	 */
	public CustomerFeedbackModel getSelectedScheduledEmail() {
		return selectedScheduledEmail;
	}


	/**
	 * @param selectedScheduledEmail the selectedScheduledEmail to set
	 */
	public void setSelectedScheduledEmail(
			CustomerFeedbackModel selectedScheduledEmail) {
		this.selectedScheduledEmail = selectedScheduledEmail;
	}


	/**
	 * @return the inBoxEmail
	 */
	public List<CustomerFeedbackModel> getInBoxEmail() {
		return inBoxEmail;
	}


	/**
	 * @param inBoxEmail the inBoxEmail to set
	 */
	public void setInBoxEmail(List<CustomerFeedbackModel> inBoxEmail) {
		this.inBoxEmail = inBoxEmail;
	}


	/**
	 * @return the selectedinBoxEmail
	 */
	public CustomerFeedbackModel getSelectedinBoxEmail() {
		return selectedinBoxEmail;
	}


	/**
	 * @param selectedinBoxEmail the selectedinBoxEmail to set
	 */
	public void setSelectedinBoxEmail(CustomerFeedbackModel selectedinBoxEmail) {
		this.selectedinBoxEmail = selectedinBoxEmail;
	}


	/**
	 * @return the lstDrafEmailAll
	 */
	public List<CustomerFeedbackModel> getLstDrafEmailAll() {
		return lstDrafEmailAll;
	}


	/**
	 * @param lstDrafEmailAll the lstDrafEmailAll to set
	 */
	public void setLstDrafEmailAll(List<CustomerFeedbackModel> lstDrafEmailAll) {
		this.lstDrafEmailAll = lstDrafEmailAll;
	}


	/**
	 * @return the lstScheduledEmailAll
	 */
	public List<CustomerFeedbackModel> getLstScheduledEmailAll() {
		return lstScheduledEmailAll;
	}


	/**
	 * @param lstScheduledEmailAll the lstScheduledEmailAll to set
	 */
	public void setLstScheduledEmailAll(
			List<CustomerFeedbackModel> lstScheduledEmailAll) {
		this.lstScheduledEmailAll = lstScheduledEmailAll;
	}


	/**
	 * @return the inBoxEmailAll
	 */
	public List<CustomerFeedbackModel> getInBoxEmailAll() {
		return inBoxEmailAll;
	}


	/**
	 * @param inBoxEmailAll the inBoxEmailAll to set
	 */
	public void setInBoxEmailAll(List<CustomerFeedbackModel> inBoxEmailAll) {
		this.inBoxEmailAll = inBoxEmailAll;
	}


	/**
	 * @return the footerDraft
	 */
	public String getFooterDraft() {
		return footerDraft;
	}


	/**
	 * @param footerDraft the footerDraft to set
	 */
	public void setFooterDraft(String footerDraft) {
		this.footerDraft = footerDraft;
	}


	/**
	 * @return the footerScheduled
	 */
	public String getFooterScheduled() {
		return footerScheduled;
	}


	/**
	 * @param footerScheduled the footerScheduled to set
	 */
	public void setFooterScheduled(String footerScheduled) {
		this.footerScheduled = footerScheduled;
	}



	@Command
	public void updateMailReminderSchedulerSettings(@BindingParam("row") CustomerFeedbackModel obj)
	{
		try
		{	
			int result=0;
			if(null!=obj && obj.getFeedbackKey()>0)  //obj.getSchedulerId()>0)
			{
				ReminderSettingsModel reminderSettingsModel=new ReminderSettingsModel();
				reminderSettingsModel.setReminderid(obj.getFeedbackKey()); //getSchedulerId());
				reminderSettingsModel.setEnablereminder(obj.isEnabaleReminder());
				result=data.updateMailReminderSettingsById(reminderSettingsModel);
				if(result>0)
				{
					if(obj.isEnabaleReminder())
						Clients.showNotification("Reminder Started Sucessfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					else
						Clients.showNotification("Reminder Stopped Sucessfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);	
				}
				else
				{
					Clients.showNotification("operation count not be compleated.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}
			}


		}
		catch (Exception ex)
		{
			logger.error("error at editCustomerFeedbackSend>>updateMailReminderSchedulerSettings>> ",ex);
		}
	}


	/**
	 * @return the sourceSerach
	 */
	public String getSourceSerach() {
		return sourceSerach;
	}


	/**
	 * @param sourceSerach the sourceSerach to set
	 */
	public void setSourceSerach(String sourceSerach) {
		this.sourceSerach = sourceSerach;
	}

	
	
	
	/**
	 * @return the sentEmailSearch
	 */
	public String getSentEmailSearch() {
		return sentEmailSearch;
	}


	/**
	 * @param sentEmailSearch the sentEmailSearch to set
	 */
	public void setSentEmailSearch(String sentEmailSearch) {
		this.sentEmailSearch = sentEmailSearch;
	}


	@Command
	@NotifyChange({"lstCustomerFeedback","lstDrafEmail","lstScheduledEmail","footer","footerDraft","footerScheduled"})
	public void searchSources(@BindingParam("cmp") String search,@BindingParam("type") String sourceType)
	{
		try
		{	
			
//			if(!search.trim().equalsIgnoreCase("") && !sourceType.trim().equalsIgnoreCase(""))
//			{
//
//				lstCustomerFeedback=feedbackData.searchSources(dbUser.getCompanyid(),search,webUserID,"S",sourceType);//sent emails
//				lstAllCustomerFeedback=lstCustomerFeedback;
//
//				lstDrafEmail=feedbackData.searchSources(dbUser.getCompanyid(),search,webUserID,"D",sourceType);//drafted enmails
//				lstDrafEmailAll=lstDrafEmail;
//
//				lstScheduledEmail=feedbackData.searchSources(dbUser.getCompanyid(),search,webUserID,"SH",sourceType);//scheduled Email's
//				lstScheduledEmailAll=lstScheduledEmail;
//				footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();
//				footerDraft="Total No. of E-mails Drafted "+lstDrafEmail.size();
//				footerScheduled="Total No. of E-mails Scheduled "+lstScheduledEmail.size();
//
//			}
//			else
//			{
//				lstCustomerFeedback=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"S");//sent emails
//				lstAllCustomerFeedback=lstCustomerFeedback;
//
//				lstDrafEmail=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"D");//drafted enmails
//				lstDrafEmailAll=lstDrafEmail;
//
//				lstScheduledEmail=feedbackData.getAllCutsomerFeedbackSent(dbUser.getCompanyid(),webUserID,"SH");//scheduled Email's
//				lstScheduledEmailAll=lstScheduledEmail;
//				footer="Total No. of E-mails Sent "+lstCustomerFeedback.size();
//				footerDraft="Total No. of E-mails Drafted "+lstDrafEmail.size();
//				footerScheduled="Total No. of E-mails Scheduled "+lstScheduledEmail.size();
//			}

		}
		catch (Exception ex)
		{
			logger.error("error at editCustomerFeedbackSend>>updateMailReminderSchedulerSettings>> ",ex);
		}
	}


	public MenuModel getCompanyRole() {
		return companyRole;
	}


	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}



}
