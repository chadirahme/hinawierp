package company;

import java.text.SimpleDateFormat;
import model.CustomerModel;
import model.EmailSignatureModel;
import model.ProspectiveModel;
import model.ReminderSettingsModel;

public class ReminderQueries 
{
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	StringBuffer query;
	public String getCompanyReminderQuery(int companyId,String remindername)
	{
		query=new StringBuffer();
		query.append("select * from remindersettings where companyId= " + companyId +  " and remindername='" + remindername + "'");
		return query.toString();		
	}

	public String getAllCompanyReminderQuery(int companyId)
	{
		query=new StringBuffer();
		query.append("select * from remindersettings where isactive=1 and companyId= " + companyId +  " ORDER BY reminderName ");
		return query.toString();		
	}

	public String getCompanyMailReminderQuery(int companyId,String remindername,int mailId)
	{
		query=new StringBuffer();
		query.append("select * from mailremindersettings where companyId= " + companyId +  " and remindername='" + remindername + "' and mailId= " + mailId + "");
		return query.toString();		
	}

	public String getAllScheduledEmails(int companyId)
	{
		query=new StringBuffer();
		query.append("select * from mailremindersettings where companyId= " + companyId +  "");
		return query.toString();		
	}


	@SuppressWarnings("static-access")
	public String updateCompanyReminderQuery(ReminderSettingsModel obj)
	{
		query=new StringBuffer();
		String reminderTime="";
		String startDate="";
		String expiryDate="";
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		if(obj.getRemindertime()!=null)
			reminderTime=tdf.format(obj.getRemindertime());
		if(obj.getRemindertime()!=null)
			startDate=dateFormat.format(obj.getStartdate());
		if(obj.getRemindertime()!=null)
			expiryDate=dateFormat.format(obj.getExpireddate());

		query.append(" update remindersettings set remindertype='%s' , startdate='%s' , remindertime=Time('%s') , remindersetting='%s' ,"
				+ "repeatedays='%s' , weekly='%s' , monthly='%s' , monthlydays='%s' , expireddate='%s' , enablereminder='%s' , sendtotype='%s', serviceListRefKey='%s' , ccemail='%s',allcustomers='%s'  ");
		query.append(" where reminderid=" +obj.getReminderid() );

		return query.toString().format(query.toString(), obj.getRemindertype() ,startDate,reminderTime,obj.getRemindersetting(),
				obj.getRepeatedays() , obj.getWeekly(),obj.getMonthly(),obj.getMonthlydays(),expiryDate,obj.isEnablereminder()?1:0,
						obj.getSendtotype(),obj.getServiceListRefKey(),obj.getCcemail(),obj.isAllcustomers()?1:0);	
	}




	@SuppressWarnings("static-access")
	public String updateCompanyMailReminderQuery(ReminderSettingsModel obj)
	{
		query=new StringBuffer();
		String reminderTime="";
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		if(obj.getRemindertime()!=null)
			reminderTime=tdf.format(obj.getRemindertime());

		query.append(" update mailremindersettings  set remindertype='%s' , startdate='%s' , remindertime=Time('%s') , remindersetting='%s' ,"
				+ "repeatedays='%s' , weekly='%s' , monthly='%s' , monthlydays='%s' , expireddate='%s' , enablereminder='%s' , sendtotype='%s', serviceListRefKey='%s' , ccemail='%s',mailId='%s'");
		query.append(" where reminderid=" +obj.getReminderid() );

		return query.toString().format(query.toString(), obj.getRemindertype() ,sdf1.format(obj.getStartdate()),reminderTime,obj.getRemindersetting(),
				obj.getRepeatedays() , obj.getWeekly(),obj.getMonthly(),obj.getMonthlydays(),sdf1.format(obj.getExpireddate()),obj.isEnablereminder()?1:0,
						obj.getSendtotype(),obj.getServiceListRefKey(),obj.getCcemail(),obj.getMailId());	
	}


	@SuppressWarnings({ "static-access", "unused" })
	public String updateCompanyMailReminderQueryById(ReminderSettingsModel obj)
	{
		query=new StringBuffer();
		//String reminderTime="";
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		//if(obj.getRemindertime()!=null)
		//	reminderTime=tdf.format(obj.getRemindertime());
		query.append(" update mailremindersettings  set  enablereminder='%s'");
		query.append(" where mailId=" +obj.getReminderid() );
		return query.toString().format(query.toString(),obj.isEnablereminder()?1:0);	
	}


	public String insertCompanyMailReminderQuery(ReminderSettingsModel obj)
	{

		query=new StringBuffer();
		String reminderTime="";
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		if(obj.getRemindertime()!=null)
			reminderTime=tdf.format(obj.getRemindertime());
		query.append(" insert into mailremindersettings(companyid,remindername,remindertype,");
		if(obj.getStartdate()!=null)
		query.append("startdate,");
		if(!reminderTime.equalsIgnoreCase(""))
		query.append("remindertime,");
		query.append("remindersetting,repeatedays,weekly,monthly,monthlydays,");
		if(obj.getExpireddate()!=null)
		query.append("expireddate,");
		query.append("enablereminder,");
		if(obj.getCreationDate()!=null)
		query.append("creationdate,");
		query.append("sendtotype,serviceListRefKey,ccemail,mailId) values(");
		query.append(obj.getCompanyid()+",");
		query.append("'"+obj.getRemindername()+"',");
		query.append(obj.getRemindertype()+",");
		if(obj.getStartdate()!=null)
		query.append("'"+sdf1.format(obj.getStartdate())+"',");
		if(!reminderTime.equalsIgnoreCase(""))
		query.append("'"+reminderTime+"',");
		
		query.append(obj.getRemindersetting()+",");
		query.append(obj.getRepeatedays()+",");
		query.append("'"+obj.getWeekly()+"',");
		query.append("'"+obj.getMonthly()+"',");
		query.append("'"+obj.getMonthlydays()+"',");
		if(obj.getExpireddate()!=null)
		query.append("'"+sdf1.format(obj.getExpireddate())+"',");
		if(obj.isEnablereminder()){
			query.append("1,");
		}else{
			query.append("0,");
		}
		if(obj.getCreationDate()!=null)
		query.append("'"+sdf1.format(obj.getCreationDate())+"',");
		query.append(obj.getSendtotype()+",");
		query.append(obj.getServiceListRefKey()+",");
		query.append("'"+obj.getCcemail()+"',");
		query.append(obj.getMailId()+")");
		return query.toString();	

	}


	public String getHRListValuesQuery(int fieldId )
	{
		query=new StringBuffer();
		query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID,notes from HRLISTVALUES");
		query.append(" where FIELD_ID=" + fieldId);
		query.append(" order by DESCRIPTION ");
		//query.append(" order by PRIORITY_ID ,DESCRIPTION ");
		return query.toString();		
	}


	public String insertCustomerListForReminder(ReminderSettingsModel reminderSettingsModel,CustomerModel obj)
	{
		query=new StringBuffer();
		query.append("Insert into QuoationReminderCustomerList(reminderId,customerId,reminderType) values ("+reminderSettingsModel.getReminderid()+","+obj.getCustkey()+",'"+reminderSettingsModel.getRemindername()+"')");
		return query.toString();		
	}


	public String insertProspectiveListForReminder(ReminderSettingsModel reminderSettingsModel,ProspectiveModel obj)
	{
		query=new StringBuffer();
		query.append("Insert into QuoationReminderProspectiveList(reminderId,prospectiverId,reminderType) values ("+reminderSettingsModel.getReminderid()+","+obj.getRecNo()+",'"+reminderSettingsModel.getRemindername()+"')");
		return query.toString();		
	}

	public String getSavedCustomerIds(int reminderId)
	{
		query=new StringBuffer();
		query.append(" Select * from  QuoationReminderCustomerList where reminderid="+reminderId);
		return query.toString();		
	}

	public String deleteSavedCustomerIds(int reminderId,String reminderName)
	{
		query=new StringBuffer();
		query.append(" delete from  QuoationReminderCustomerList where reminderid="+reminderId+" and reminderType='"+reminderName+"'");
		return query.toString();		
	}

	public String getSavedProspectiveIds(int reminderId)
	{
		query=new StringBuffer();
		query.append(" Select * from  QuoationReminderProspectiveList where reminderid="+reminderId);
		return query.toString();		
	}

	public String deleteSavedProspeciveIds(int reminderId,String reminderName)
	{
		query=new StringBuffer();
		query.append(" delete from  QuoationReminderProspectiveList where reminderid="+reminderId+"and reminderType='"+reminderName+"'");
		return query.toString();		
	}

	public String addSignature(EmailSignatureModel obj)
	{
		query=new StringBuffer();		 
		query.append(" Insert into ReminderSignatureByCompany (reminderId,Signature,ReminderName,CompnayId)");
		query.append(" values("+obj.getReminderId()+",'"+obj.getSignature()+"','"+obj.getReminderName()+"',"+obj.getCompanyId()+")");
		return query.toString();
	}


	public String updateSignature(EmailSignatureModel obj)
	{
		query=new StringBuffer();		 
		query.append(" update ReminderSignatureByCompany set ReminderName='"+obj.getReminderName()+"',Signature='"+obj.getSignature()+"' where reminderId="+obj.getReminderId()+" and CompnayId="+obj.getCompanyId()+"");
		return query.toString();
	}
	public String updateFeedbackSendSignature(EmailSignatureModel obj)
	{
		query=new StringBuffer();		 
		query.append(" update feedbackSendSignature set signature='"+obj.getSignature()+"' where userId=-1");
		return query.toString();
	}


	public String getEmailSignature(int reminderID)
	{
		query=new StringBuffer();
		query.append("select * from ReminderSignatureByCompany");	
		query.append(" where reminderID="+reminderID);
		query.append(" ");
		return query.toString();
	}


	public String addCustomerOrProspectiveEmailsForreminders(int reminderId,int custOrprospectiveId,String type,String email,String sourceType,String CusContractExpiry,double LocalBalance)
	{
		query=new StringBuffer();		 
		query.append(" Insert into ReminderEmailList (reminderId,customerOrProspectiveRefKey,emailType,email,sourceTYpe,CusContractExpiry,LocalBalance)");
		query.append(" values("+reminderId+","+custOrprospectiveId+",'"+type+"','"+email+"','"+sourceType+"','" +CusContractExpiry +"','" +LocalBalance +"')");
		return query.toString();
	}


	public String getSavedEmailsForReminderType(int reminderId)
	{
		query=new StringBuffer();		
		query.append("select * from ReminderEmailList where reminderId="+reminderId+"");  		
		return query.toString();
	}

	public String deleteAllCustomerOrProspectiveEmailsForreminders(int reminderId)
	{
		query=new StringBuffer();		
		query.append("delete from ReminderEmailList where reminderId="+reminderId+"");  		
		return query.toString();
	}

	
	public String getCustomerreminderEmailsQuery(int companyId,int reminderId,String fromDate,String toDate)
	{
		query=new StringBuffer();
		query.append("select * from customerreminder where companyId= " + companyId);
		query.append(" and reminderid=" + reminderId);
		query.append(" and senddate BETWEEN '" + fromDate + "' and '" + toDate +"'");
		query.append(" ORDER BY customername");
		return query.toString();		
	}

}
