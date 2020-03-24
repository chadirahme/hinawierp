package company;

import hr.HRQueries;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;





import model.CompanyDBModel;
import model.CustomerModel;
import model.EmailSelectionModel;
import model.EmailSignatureModel;
import model.HRListValuesModel;
import model.ProspectiveModel;
import model.QbListsModel;
import model.ReminderSettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ReminderData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	ReminderQueries query=new ReminderQueries();
	SQLDBHandler sqldb=new SQLDBHandler("hinawi_hba");
	WebusersModel dbUser=null;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	public ReminderData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			query=new ReminderQueries();
			CompanyDBModel obj=new CompanyDBModel();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
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
				sqldb=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in ReminderData---Init-->" , ex);
		}
	}	


	public ReminderSettingsModel getCompanyReminder(int companyId,String remindername)
	{
		ReminderSettingsModel obj=new ReminderSettingsModel();
		try
		{
			DBHandler db = new DBHandler();
			ResultSet rs = null;
			rs = db.executeNonQuery(query.getCompanyReminderQuery(companyId,remindername));	
			while(rs.next())
			{
				obj.setReminderid(rs.getInt("reminderid"));
				obj.setCompanyid(rs.getInt("companyid"));
				obj.setRemindername(rs.getString("remindername"));
				obj.setRemindertype(rs.getInt("remindertype"));
				obj.setStartdate(rs.getDate("startdate"));
				obj.setRemindertime(rs.getTime("remindertime"));
				obj.setRemindersetting(rs.getString("remindersetting"));
				obj.setRepeatedays(rs.getInt("repeatedays"));
				obj.setWeekly(rs.getString("weekly")==null?"":rs.getString("weekly"));
				obj.setMonthly(rs.getString("monthly")==null?"":rs.getString("monthly"));				 
				obj.setMonthlydays(rs.getString("monthlydays")==null?"":rs.getString("monthlydays"));
				obj.setExpireddate(rs.getDate("expireddate"));
				obj.setEnablereminder(rs.getBoolean("enablereminder"));	
				obj.setSendtotype(rs.getString("sendtotype"));
				obj.setServiceListRefKey(rs.getInt("serviceListRefKey"));
				obj.setCcemail(rs.getString("ccemail"));
			}
		}
		catch (Exception ex) 
		{		 	  
			logger.error("error in ReminderData---getCompanyReminder-->" , ex);			 	  
		}
		return obj;

	}



	public List<ReminderSettingsModel> getAllCompanyReminder(int companyId)
	{
		List<ReminderSettingsModel> list=new ArrayList<ReminderSettingsModel>();
		try
		{
			DBHandler db = new DBHandler();
			ResultSet rs = null;
			//Default setting 
			ReminderSettingsModel deafultSetting=new ReminderSettingsModel();
			Calendar c = Calendar.getInstance();		
			deafultSetting.setReminderid(0);
			deafultSetting.setCompanyid(0);
			deafultSetting.setRemindername("Select");
			deafultSetting.setRemindertype(1);
			deafultSetting.setStartdate(df.parse(sdf.format(c.getTime())));
			deafultSetting.setRemindertime(df.parse(sdf.format(c.getTime())));
			deafultSetting.setRemindersetting(2+"");
			deafultSetting.setRepeatedays(1);
			deafultSetting.setWeekly("Thursday,Friday");
			deafultSetting.setMonthly("January,February");				 
			deafultSetting.setMonthlydays("1,2");
			deafultSetting.setExpireddate(df.parse(sdf.format(c.getTime())));
			deafultSetting.setEnablereminder(false);	
			deafultSetting.setSendtotype("2");
			deafultSetting.setServiceListRefKey(0);
			deafultSetting.setCcemail("");
			deafultSetting.setAllcustomers(false);
			list.add(deafultSetting);


			rs = db.executeNonQuery(query.getAllCompanyReminderQuery(companyId));	
			while(rs.next())
			{
				ReminderSettingsModel obj=new ReminderSettingsModel();
				obj.setReminderid(rs.getInt("reminderid"));
				obj.setCompanyid(rs.getInt("companyid"));
				obj.setRemindername(rs.getString("remindername"));
				obj.setRemindertype(rs.getInt("remindertype"));
				obj.setStartdate(rs.getDate("startdate"));
				obj.setRemindertime(rs.getTime("remindertime"));
				obj.setRemindersetting(rs.getString("remindersetting"));
				obj.setRepeatedays(rs.getInt("repeatedays"));
				obj.setWeekly(rs.getString("weekly")==null?"":rs.getString("weekly"));
				obj.setMonthly(rs.getString("monthly")==null?"":rs.getString("monthly"));				 
				obj.setMonthlydays(rs.getString("monthlydays")==null?"":rs.getString("monthlydays"));
				obj.setExpireddate(rs.getDate("expireddate"));
				obj.setEnablereminder(rs.getBoolean("enablereminder"));	
				obj.setSendtotype(rs.getString("sendtotype"));
				obj.setServiceListRefKey(rs.getInt("serviceListRefKey"));
				obj.setCcemail(rs.getString("ccemail"));
				obj.setAllcustomers(rs.getBoolean("allcustomers"));	
				list.add(obj);
			}
		}
		catch (Exception ex) 
		{		 	  
			logger.error("error in ReminderData---getAllCompanyReminder-->" , ex);			 	  
		}
		return list;

	}

	public ReminderSettingsModel getCompanyMailReminder(int companyId,String remindername,int mailId)
	{
		ReminderSettingsModel obj=new ReminderSettingsModel();
		try
		{
			DBHandler db = new DBHandler();
			ResultSet rs = null;
			rs = db.executeNonQuery(query.getCompanyMailReminderQuery(companyId,remindername,mailId));	
			while(rs.next())
			{
				obj.setReminderid(rs.getInt("reminderid"));
				obj.setCompanyid(rs.getInt("companyid"));
				obj.setRemindername(rs.getString("remindername"));
				obj.setRemindertype(rs.getInt("remindertype"));
				obj.setStartdate(rs.getDate("startdate"));
				obj.setRemindertime(rs.getTime("remindertime"));
				obj.setRemindersetting(rs.getString("remindersetting"));
				obj.setRepeatedays(rs.getInt("repeatedays"));
				obj.setWeekly(rs.getString("weekly")==null?"":rs.getString("weekly"));
				obj.setMonthly(rs.getString("monthly")==null?"":rs.getString("monthly"));				 
				obj.setMonthlydays(rs.getString("monthlydays")==null?"":rs.getString("monthlydays"));
				obj.setExpireddate(rs.getDate("expireddate"));
				obj.setEnablereminder(rs.getBoolean("enablereminder"));	
				obj.setSendtotype(rs.getString("sendtotype"));
				obj.setServiceListRefKey(rs.getInt("serviceListRefKey"));
				obj.setCcemail(rs.getString("ccemail"));
				obj.setMailId(rs.getInt("mailId"));
			}
		}
		catch (Exception ex) 
		{		 	  
			logger.error("error in ReminderData---getCompanyMailReminder-->" , ex);			 	  
		}
		return obj;

	}


	public List<ReminderSettingsModel> getAllScheduledEmails(int companyId)
	{
		List<ReminderSettingsModel> lst=new ArrayList<ReminderSettingsModel>();
		try
		{
			DBHandler db = new DBHandler();
			ResultSet rs = null;
			rs = db.executeNonQuery(query.getAllScheduledEmails(companyId));	
			while(rs.next())
			{
				ReminderSettingsModel obj=new ReminderSettingsModel();
				obj.setReminderid(rs.getInt("reminderid"));
				obj.setCompanyid(rs.getInt("companyid"));
				obj.setRemindername(rs.getString("remindername"));
				obj.setRemindertype(rs.getInt("remindertype"));
				obj.setStartdate(rs.getDate("startdate"));
				obj.setRemindertime(rs.getTime("remindertime"));
				obj.setRemindersetting(rs.getString("remindersetting"));
				obj.setRepeatedays(rs.getInt("repeatedays"));
				obj.setWeekly(rs.getString("weekly")==null?"":rs.getString("weekly"));
				obj.setMonthly(rs.getString("monthly")==null?"":rs.getString("monthly"));				 
				obj.setMonthlydays(rs.getString("monthlydays")==null?"":rs.getString("monthlydays"));
				obj.setExpireddate(rs.getDate("expireddate"));
				obj.setEnablereminder(rs.getBoolean("enablereminder"));	
				obj.setSendtotype(rs.getString("sendtotype"));
				obj.setServiceListRefKey(rs.getInt("serviceListRefKey"));
				obj.setCcemail(rs.getString("ccemail")==null?"":rs.getString("ccemail"));
				obj.setMailId(rs.getInt("mailId"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
		{		 	  
			logger.error("error in ReminderData---getAllScheduledEmails-->" , ex);			 	  
		}
		return lst;

	}


	public int saveReminderSettings(ReminderSettingsModel obj,List<CustomerModel> customerList,List<ProspectiveModel> prospectiveList)
	{
		int reminderId=0;
		try
		{
			DBHandler db = new DBHandler();
			reminderId= db.executeUpdateQuery(query.updateCompanyReminderQuery(obj));
			if(reminderId>0)
			{
				
				//store customer ids and prospective ids to pre poulate back in grid
				if (customerList.size() > 0) {
					sqldb.executeUpdateQuery(query.deleteSavedCustomerIds(
							obj.getReminderid(), obj.getRemindername()));
					for (CustomerModel model : customerList) {
						sqldb.executeUpdateQuery(query
								.insertCustomerListForReminder(obj, model));
					}
				}
				if (prospectiveList.size() > 0) {
					query.deleteSavedProspeciveIds(obj.getReminderid(),
							obj.getRemindername());
					for (ProspectiveModel model : prospectiveList) {
						sqldb.executeUpdateQuery(query
								.insertProspectiveListForReminder(obj, model));
					}
				}
				
				if (obj.getSelectedCustomerEmails().size() > 0) {
					sqldb.executeUpdateQuery(query
							.deleteAllCustomerOrProspectiveEmailsForreminders(obj
									.getReminderid()));
					// stored selected emails of customer or prospective
					logger.info("obj.getSelectedCustomerEmails()>> "
							+ obj.getSelectedCustomerEmails().size());
					for (EmailSelectionModel emailSelectionModel : obj
							.getSelectedCustomerEmails()) {
						if (emailSelectionModel.isTo()) {
							sqldb.executeUpdateQuery(query
									.addCustomerOrProspectiveEmailsForreminders(
											obj.getReminderid(),
											emailSelectionModel
													.getCustOrProspKey(),
											"TO",
											emailSelectionModel.getEmail(),
											emailSelectionModel.getSourceType(),
											emailSelectionModel
													.getCusContractExpiry(),
											emailSelectionModel
													.getLocalBalance()));
						}
						if (emailSelectionModel.isCc()) {
							sqldb.executeUpdateQuery(query
									.addCustomerOrProspectiveEmailsForreminders(
											obj.getReminderid(),
											emailSelectionModel
													.getCustOrProspKey(),
											"CC",
											emailSelectionModel.getEmail(),
											emailSelectionModel.getSourceType(),
											emailSelectionModel
													.getCusContractExpiry(),
											emailSelectionModel
													.getLocalBalance()));
						}
						if (emailSelectionModel.isBcc()) {
							sqldb.executeUpdateQuery(query
									.addCustomerOrProspectiveEmailsForreminders(
											obj.getReminderid(),
											emailSelectionModel
													.getCustOrProspKey(),
											"BCC",
											emailSelectionModel.getEmail(),
											emailSelectionModel.getSourceType(),
											emailSelectionModel
													.getCusContractExpiry(),
											emailSelectionModel
													.getLocalBalance()));
						}
					}
				}
				
			}
		}
		catch (Exception ex) 
		{
			reminderId=-1;
			logger.error("error in ReminderData---saveReminderSettings-->" , ex);			 	  
		}
		return reminderId;
	}

	public int saveMailReminderSettings(ReminderSettingsModel obj)
	{
		int reminderId=0;
		try
		{
			DBHandler db = new DBHandler();
			reminderId= db.executeUpdateQuery(query.insertCompanyMailReminderQuery(obj));
		}
		catch (Exception ex) 
		{
			reminderId=-1;
			logger.error("error in ReminderData---saveMailReminderSettings-->" , ex);			 	  
		}
		return reminderId;
	}

	public int updateMailReminderSettings(ReminderSettingsModel obj)
	{
		int reminderId=0;
		try
		{
			DBHandler db = new DBHandler();
			reminderId= db.executeUpdateQuery(query.updateCompanyMailReminderQuery(obj));
		}
		catch (Exception ex) 
		{
			reminderId=-1;
			logger.error("error in ReminderData---updateMailReminderSettings-->" , ex);			 	  
		}
		return reminderId;
	}

	public int updateMailReminderSettingsById(ReminderSettingsModel obj)
	{
		int reminderId=0;
		try
		{
			DBHandler db = new DBHandler();
			reminderId= db.executeUpdateQuery(query.updateCompanyMailReminderQueryById(obj));
		}
		catch (Exception ex) 
		{
			reminderId=-1;
			logger.error("error in ReminderData---updateMailReminderSettings-->" , ex);			 	  
		}
		return reminderId;
	}

	public List<HRListValuesModel> getHRListValuesForTemplates(int fieldId,String type)
	{
		List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();

		HRListValuesModel obj=new HRListValuesModel();
		if(!type.equals(""))
		{
			obj.setListId(0);					
			obj.setEnDescription(type);
			lst.add(obj);
		}

		ResultSet rs = null;
		try 
		{
			rs=sqldb.executeNonQuery(query.getHRListValuesQuery(fieldId));
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
			logger.error("error in ReminderData---getHRListValuesForTemplates-->" , ex);
		}
		return lst;
	}


	@SuppressWarnings("unused")
	public String getSavedCustomerIds(int reminderId)
	{
		StringBuilder buff = new StringBuilder();
		QbListsModel obj=null;
		String sep = "";
		ResultSet rs = null;
		try 
		{
			rs=sqldb.executeNonQuery(query.getSavedCustomerIds(reminderId));
			while(rs.next())
			{
				int Cust_ids=rs.getInt("customerId");					
				buff.append(sep);
				buff.append(Cust_ids);
				sep = ",";
			}
		}
		catch (Exception ex) {
			logger.error("error in ReminderData---getSavedCustomerIds-->" , ex);
		}
		return buff.toString();
	}


	@SuppressWarnings("unused")
	public String getSavedProspectiveIds(int reminderId)
	{
		StringBuilder buff = new StringBuilder();
		QbListsModel obj=null;
		String sep = "";
		ResultSet rs = null;
		try 
		{
			rs=sqldb.executeNonQuery(query.getSavedProspectiveIds(reminderId));
			while(rs.next())
			{
				int prosp_id=rs.getInt("prospectiverId");					
				buff.append(sep);
				buff.append(prosp_id);
				sep = ",";
			}
		}
		catch (Exception ex) {
			logger.error("error in ReminderData---getSavedProspectiveIds-->" , ex);
		}
		return buff.toString();
	}



	public int addSignature(EmailSignatureModel obj)
	{
		int result=0;
		try 
		{			
			result=sqldb.executeUpdateQuery(query.addSignature(obj));		
		}
		catch (Exception ex) {
			logger.error("error in ReminderData---addSignature-->" , ex);
		}
		return result;

	}


	public int updateSignature(EmailSignatureModel obj)
	{
		int result=0;
		try 
		{			
			result=sqldb.executeUpdateQuery(query.updateSignature(obj));	
			if(obj.getUserId()==-1)//use this for bulk email
			{
				sqldb.executeUpdateQuery(query.updateFeedbackSendSignature(obj));	
			}
		}
		catch (Exception ex) {
			logger.error("error in ReminderData---updateSignature-->" , ex);
		}
		return result;

	}


	public EmailSignatureModel getEmailSignature(int userId)
	{
		EmailSignatureModel obj=new EmailSignatureModel();
		ResultSet rs = null;
		try 
		{
			rs=sqldb.executeNonQuery(query.getEmailSignature(userId));
			while(rs.next())
			{			
				obj.setReminderId(rs.getInt("reminderId"));
				obj.setCompanyId(rs.getInt("CompnayId"));
				obj.setSignature(rs.getString("signature")==null?"":rs.getString("signature"));
				obj.setReminderName(rs.getString("ReminderName")==null?"":rs.getString("ReminderName"));
			}
		}

		catch (Exception ex) {
			logger.error("error in ReminderData---getEmailSignature-->" , ex);
		}
		return obj;

	}	

	
	
	public List<EmailSelectionModel> getSavedEmailsForReminderType(int reminderId)
	{
		List<EmailSelectionModel> lst=new ArrayList<EmailSelectionModel>();
		ResultSet rs = null;
		try 
		{
			rs=sqldb.executeNonQuery(query.getSavedEmailsForReminderType(reminderId));
			while(rs.next())
			{		
				EmailSelectionModel obj=new EmailSelectionModel();
				obj.setReminderId(rs.getInt("reminderId"));
				obj.setBcc(rs.getString("emailType").equalsIgnoreCase("BCC")?true:false);
				obj.setCc(rs.getString("emailType").equalsIgnoreCase("CC")?true:false);
				obj.setTo(rs.getString("emailType").equalsIgnoreCase("TO")?true:false);
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setCustOrProspKey(rs.getInt("customerOrProspectiveRefKey"));
				obj.setSourceType(rs.getString("sourceTYpe")==null?"":rs.getString("sourceTYpe"));
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in ReminderData---getSavedEmailsForReminderType-->" , ex);
		}
		return lst;

	}	
	
	
	public List<EmailSelectionModel> getCustomerreminderEmails(int companyId,int reminderId,String fromDate,String toDate)
	{
		List<EmailSelectionModel> lst=new ArrayList<EmailSelectionModel>();
		ResultSet rs = null;
		DBHandler db = new DBHandler();
		try 
		{
			rs=db.executeNonQuery(query.getCustomerreminderEmailsQuery(companyId, reminderId, fromDate, toDate));
			while(rs.next())
			{		
				EmailSelectionModel obj=new EmailSelectionModel();
				obj.setReminderId(rs.getInt("reminderId"));				
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setCustOrProspKey(rs.getInt("customerid"));
				obj.setCustomerName(rs.getString("customername"));
				obj.setEmailsubject(rs.getString("emailsubject"));
				obj.setEmailbody(rs.getString("emailbody"));
				obj.setSendDate(rs.getString("senddate"));							
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in ReminderData---getCustomerreminderEmails-->" , ex);
		}
		return lst;

	}	


}
