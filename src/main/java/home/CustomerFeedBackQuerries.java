package home;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.CustomerFeedbackModel;
import model.CustomerStatusHistoryModel;
import model.EmailSignatureModel;
import model.FeedbackSendSources;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

public class CustomerFeedBackQuerries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	WebusersModel dbUser=null;
	
	
	public CustomerFeedBackQuerries()
	{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
	}
	

	public String saveCustomerFeedback(CustomerFeedbackModel obj)
	{
			//To remove '' i the SQL statements ;
			String memo=""; 
			if(obj.getMemo()!=null)
			{
				memo=obj.getMemo().replaceAll("'","`");
			}
			obj.setMemo(memo);
			
			String addrs=""; 
			if(obj.getAddress()!=null)
			{
				addrs=obj.getAddress().replaceAll("'","`");
			}
			obj.setAddress(addrs);
			
			String instrction=""; 
			if(obj.getInstruction()!=null)
			{
				instrction=obj.getInstruction().replaceAll("'","`");
			}
			obj.setInstruction(instrction);
			
		  query=new StringBuffer();		 
		  query.append(" Insert into CustomerEnquiry (Enquiry_ID,Enquiry_No,Customer_Company_Name,ContactPersonName,Address,Email,website,Software_Type,CustomerRefKey,Memo,webUserID,mobile1,mobile2,mobileAreaCode1,mobileAreaCode2,telephone1,telephone2,telephoneAreaCode1,telephoneAreaCode2,ContactPersonIntials,TypeOfCutomer,cityKey,CountryKey,positionKey,createdDate,ModifiedDate,instructions)");
		  query.append(" values("+obj.getFeedbackKey()+",'"+obj.getFeedbackNUmber()+"','"+obj.getCompanyName()+"','"+obj.getContactPersonName()+"','" + obj.getAddress()+"','"+obj.getEmail()+"','"+obj.getWebsite()+"','"+obj.getSelectedSoftwareType()+"',");
		  query.append(""+obj.getCustomerRefKey()+",'"+memo+"',"+obj.getWebuserID()+",'"+obj.getMobile1()+"','"+obj.getMobile2()+"','"+obj.getMobileAreaCode1()+"','"+obj.getMobileAreaCode2()+"','"+obj.getTelphone1()+"',");
		  query.append("'"+obj.getTelphone2()+"','"+obj.getTelphoneAreaCode1()+"','"+obj.getTelphoneAreaCode2()+"',"+obj.getCustomerInitailKey()+",'"+obj.getCustomerType()+"',"+obj.getCityKey()+","+obj.getCountryKey()+","+obj.getPositionKey()+",'"+sdf.format(obj.getFeedbackCreateDate())+"','"+sdf.format(obj.getFeedBackModifiedDate())+"','"+obj.getInstruction()+"')");
		  return query.toString();
	}
	
	
	public String editCustomerFeedBackData(CustomerFeedbackModel obj)
	{
		//To remove '' i the SQL statements ;
		String memo=""; 
		if(obj.getMemo()!=null)
		{
			memo=obj.getMemo().replaceAll("'","`");
		}
		obj.setMemo(memo);
		
		String addrs=""; 
		if(obj.getAddress()!=null)
		{
			addrs=obj.getAddress().replaceAll("'","`");
		}
		obj.setAddress(addrs);
		
		
		String instrction=""; 
		if(obj.getInstruction()!=null)
		{
			instrction=obj.getInstruction().replaceAll("'","`");
		}
		obj.setInstruction(instrction);
		
		query=new StringBuffer();
		query.append("Update CustomerEnquiry set website='"+obj.getWebsite()+"',Enquiry_No='"+obj.getFeedbackNUmber()+"',Customer_Company_Name='"+obj.getCompanyName()+"',ContactPersonName='"+obj.getContactPersonName()+"',ModifiedDate='"+sdf.format(obj.getFeedBackModifiedDate())+"',mobile1='"+obj.getMobile1()+"',telephone1='"+obj.getTelphone1()+"',Email='"+obj.getEmail()+"',Memo='"+obj.getMemo()+"',Software_Type='"+obj.getSelectedSoftwareType()+"',customerrefKey="+obj.getCustomerRefKey()+",");	
		query.append("ContactPersonIntials="+obj.getCustomerInitailKey()+",TypeOfCutomer='"+obj.getCustomerType()+"',instructions='"+obj.getInstruction()+"' where Enquiry_ID="+obj.getFeedbackKey());
		query.append(" ");
		return query.toString();
	}
	
	
	
	public String addCustomerFeedbackDetails(int enquiryKey,int feedbackTypeKey,String Name)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into customerEnquiryDetails (Enquiry_ID,feedBackTypeKey,feedbackName)");
		  query.append(" values("+enquiryKey+","+feedbackTypeKey+",'"+Name+"')");
		  return query.toString();
	}
	
	public String deleteAllFeedbackDetailsById(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		
		  query.append("delete from customerEnquiryDetails where Enquiry_ID="+obj.getFeedbackKey()+"");  		
		  return query.toString();
	}
	
	
	public String addCustomerFeedbackAttachmnetPath(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into CustomerEnquiryAttachment (Enquiry_ID,Attachment_Path,fileName)");
		  query.append(" values("+obj.getFeedbackKey()+",'"+obj.getAttchemnetPath()+"','"+obj.getFileName()+"')");
		  return query.toString();
	}
	
	
	public String deleteAllFeedbackAttachmnetPathbyId(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		
		  query.append("delete from CustomerEnquiryAttachment where Enquiry_ID="+obj.getFeedbackKey()+"");  		
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
	
	
	public String getFeedbackAttchamnet(int feedBackId)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from CustomerEnquiryAttachment");
		  if(feedBackId>0)
		  query.append(" where  Enquiry_ID="+feedBackId+"");	  
		  return query.toString();
	}
	
	public String getTaskRelationData(int feedBackId)
	{
		  query=new StringBuffer();	
		  query.append("select TaskFeedbackRelation.*,def.description as TaskStatusStr,taskTable.TaskName,taskTable.TaskNo from dbo.TaskFeedbackRelation ");
		  query.append("left join tasks as taskTable ON TaskFeedbackRelation.taskId = taskTable.taskId ");
		  query.append("LEFT JOIN HRLISTVALUES as def ON TaskFeedbackRelation.taskStatus = def.id ");
		  if(feedBackId>0)
		  query.append(" where  feedbackId="+feedBackId+"");	   
		  query.append(" order by createdDate desc ");
		  return query.toString();
	}
	
	public String checkIfFeedBackNumberIsDuplicate(String feedbackNumber,int feedbackID)
	{
		query=new StringBuffer();
		 query.append(" Select * from CustomerEnquiry Where Enquiry_No=" + feedbackNumber+" and Enquiry_ID !="+feedbackID);
		 return query.toString();		
	}
	
	public String getAllCutsomerFeedback(int webUserId,Date fromDate,Date toDate)
	{
		  query=new StringBuffer();		 
		  query.append("Select CustomerEnquiry.*,customerList.fullname,intailName.description  from CustomerEnquiry ");
		  query.append(" LEFT JOIN Customer as customerList ON CustomerEnquiry.customerrefKey = customerList.cust_key "); 
		  query.append(" LEFT JOIN HRLISTVALUES as intailName ON CustomerEnquiry.ContactPersonIntials = intailName.id ");
		  query.append(" where CustomerEnquiry.createdDate Between '"+sdf.format(fromDate)+"' And '"+sdf.format(toDate)+"' ");
		  query.append(" order by  CustomerEnquiry.createdDate desc");	
		  return query.toString();
	}
	
	public String getFeedBackDetails(int feedBackID)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from customerEnquiryDetails");
		  if(feedBackID>0)
		  query.append(" where  Enquiry_ID="+feedBackID+"");	  
		  return query.toString();
	}
	
	
	
	
	
	//*****************************************************************************************************	                            *****************************************************************************************************
	//*****************************************************************************************************	Customer feedBack Send Data *****************************************************************************************************
	//*****************************************************************************************************                             *****************************************************************************************************	
			
			//Customer feedBack Send.
	
	public String getAllCutsomerFeedbackSent(int feedBackID,String type)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from feedbackSend where ");
		  if(feedBackID>0)
		  query.append("  userId="+feedBackID+" and ");	
		  if(type.equalsIgnoreCase("S"))
		  {
			  query.append("  isSent='Y'");  
		  }
		  else if (type.equalsIgnoreCase("D"))
		  {
			  query.append("  isDrafted='Y' and isSent!='Y' ");
		  }
		  else if (type.equalsIgnoreCase("SH"))
		  {
			  query.append("  isScheduled='Y' ");
		  }
		  query.append(" order by feedBackid desc");
		  return query.toString();
	}
	
	
	
	public String saveCustomerFeedbackSentData(CustomerFeedbackModel obj)
	{
			//To remove '' i the SQL statements ;
			String memo=""; 
			if(obj.getMemo()!=null)
			{
				memo=obj.getMemo().replaceAll("'","`");
			}
			obj.setMemo(memo);
			
			String subject=""; 
			if(obj.getSubject()!=null)
			{
				subject=obj.getSubject().replaceAll("'","`");
			}
			obj.setSubject(subject);
			
		  query=new StringBuffer();		 
		  query.append(" Insert into FeedbackSend (feedbackId,Memo,UserID,createdDate,modifiedDate,feedbackNumber,subject,taskKey,serviceListKey,usedEmailToSend,isDrafted,isSent)");
		  query.append(" values("+obj.getFeedbackKey()+",'"+obj.getMemo()+"',"+obj.getWebuserID()+",'"+sdf.format(obj.getFeedbackCreateDate())+"','" +sdf.format(obj.getFeedBackModifiedDate())+"','"+obj.getFeedbackNUmber()+"','"+obj.getSubject()+"',"+obj.getTaskID()+","+obj.getServiceRefKey()+",'"+obj.getSentFromEmail()+"','"+obj.getIsDrafted()+"','"+obj.getIsSent()+"')");
		  return query.toString();
	}
	
	
	public String editCustomerFeedBackSentData(CustomerFeedbackModel obj)
	{
		//To remove '' i the SQL statements ;
		String memo=""; 
		if(obj.getMemo()!=null)
		{
			memo=obj.getMemo().replaceAll("'","`");
		}
		obj.setMemo(memo);
		String subject=""; 
		if(obj.getSubject()!=null)
		{
			subject=obj.getSubject().replaceAll("'","`");
		}
		obj.setSubject(subject);
		query=new StringBuffer();
		query.append("Update FeedbackSend set subject='"+obj.getSubject()+"',usedEmailToSend='"+obj.getSentFromEmail()+"',Memo='"+obj.getMemo()+"',feedbackNumber='"+obj.getFeedbackNUmber()+"',ModifiedDate='"+sdf.format(obj.getFeedBackModifiedDate())+"',taskKey="+obj.getTaskID()+",serviceListKey="+obj.getServiceRefKey()+",isDrafted='"+obj.getIsDrafted()+"',isSent='"+obj.getIsSent()+"'");	
		query.append(" where feedbackId="+obj.getFeedbackKey());
		query.append(" ");
		return query.toString();
	}
	
	public String editCustomerFeedBackSentFromScheuler(CustomerFeedbackModel obj)
	{
		query=new StringBuffer();
		query.append("Update FeedbackSend set isScheduled='"+obj.getIsScheduled()+"'");	
		query.append(" where feedbackId="+obj.getFeedbackKey());
		query.append(" ");
		return query.toString();
	}
	
	
	public String getFeedBackSentCusomersDetails(int feedBackID)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from FeebackSendCustomerList");
		  if(feedBackID>0)
		  query.append(" where  feedbackId="+feedBackID+"");	
		  query.append(" order by emailType desc ");
		  return query.toString();
	}
	
	

	public String addCustomerFeedbackCustomerDetails(int feedBackId,String type,String email)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into FeebackSendCustomerList (feedbackId,emailType,email)");
		  query.append(" values("+feedBackId+",'"+type+"','"+email+"')");
		  return query.toString();
	}
	
	public String deleteAllFeedbackCustomers(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		
		  query.append("delete from FeebackSendCustomerList where feedbackId="+obj.getFeedbackKey()+"");  		
		  return query.toString();
	}
	
	
	public String addCustomerFeedbackSourceDetails(int feedBackId, FeedbackSendSources obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into FeebackSendSourceList (feedbackId,sourceType,sourceId)");
		  query.append(" values("+feedBackId+",'"+obj.getSourceType()+"',"+obj.getSourceId()+")");
		  return query.toString();
	}
	
	public String deleteAllFeedbackSourceDetails(int feedBackId)
	{
		  query=new StringBuffer();		
		  query.append("delete from FeebackSendSourceList where feedbackId="+feedBackId+"");  		
		  return query.toString();
	}
	
	
	public String addCustomerFeedbackSendAttachmnetPath(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into feedBackSendAttachments (feedbackId,path,fileName)");
		  query.append(" values("+obj.getFeedbackKey()+",'"+obj.getAttchemnetPath()+"','"+obj.getFileName()+"')");
		  return query.toString();
	}
	
	
	public String deleteAllFeedbackSendAttachmnetPath(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		
		  query.append("delete from feedBackSendAttachments where feedbackId="+obj.getFeedbackKey()+"");  		
		  return query.toString();
	}
	
	
	public String getCustomerFeedBackSendById(int feedBackId)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from FeedbackSend");
		  if(feedBackId>0)
		  query.append(" where feedbackId="+feedBackId+"");	  
		  return query.toString();
	}
	
	
	public String getFeedbacksendAttchamnet(int feedBackId)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from feedBackSendAttachments");
		  if(feedBackId>0)
		  query.append(" where  feedbackId="+feedBackId+"");	  
		  return query.toString();
	}
	
	public String checkIfFeedBackSendNumberIsDuplicate(String feedbackNumber,int feedbackID)
	{
		query=new StringBuffer();
		 query.append(" Select * from FeedbackSend Where feedbackNumber=" + feedbackNumber+" and feedbackId !="+feedbackID);
		 return query.toString();		
	}
	
	public String getAllEmailSources(int feedbackID)
	{
		query=new StringBuffer();
	    query.append(" select FeebackSendSourceList.*,employee.english_full as empName,vendor.name as vendName,Customer.fullname as custName,prospective.name as prosName from FeebackSendSourceList ");
	    query.append(" left join empmast as employee on FeebackSendSourceList.sourceId=employee.emp_key ");
	    query.append(" left join vendor as vendor on FeebackSendSourceList.sourceId=vendor.vend_key ");
	    query.append(" left join Customer on FeebackSendSourceList.sourceId=Customer.cust_key ");
	    query.append(" left join prospective on FeebackSendSourceList.sourceId=prospective.recNo");
	    if(feedbackID>0)
	    {
	    	  query.append(" where FeebackSendSourceList.feedbackId="+feedbackID);
	    }
	    
	    query.append(" order by FeebackSendSourceList.sourceType");
		return query.toString();		
	}
	
	public String searchSources(String serachString,int webUserId,String type)
	{
		  query=new StringBuffer();		 
		  query.append("select distinct feedbackSend.* from feedbackSend  ");
		  query.append("left join FeebackSendSourceList on FeebackSendSourceList.feedbackId=feedbackSend.feedbackId  ");
	      query.append("left join empmast as employee on FeebackSendSourceList.sourceId=employee.emp_key  ");
		  query.append("left join vendor as vendor on FeebackSendSourceList.sourceId=vendor.vend_key  ");
		  query.append("left join Customer on FeebackSendSourceList.sourceId=Customer.cust_key  ");
		  query.append("left join prospective on FeebackSendSourceList.sourceId=prospective.recNo "); 
		  query.append("where (Customer.fullname like '%"+serachString+"%' or prospective.name like '%"+serachString+"%' or employee.english_full like '%"+serachString+"%' or vendor.name like '%"+serachString+"%')");
		  if(webUserId>0)
		  query.append(" and  feedbackSend.userId="+webUserId+" ");	
		  if(type.equalsIgnoreCase("S"))
		  {
			  query.append(" and feedbackSend.isSent='Y'");  
		  }
		  else if (type.equalsIgnoreCase("D"))
		  {
			  query.append(" and feedbackSend.isDrafted='Y' and feedbackSend.isSent!='Y' ");
		  }
		  else if (type.equalsIgnoreCase("SH"))
		  {
			  query.append(" and feedbackSend.isScheduled='Y' ");
		  }
		  return query.toString();
	}
	
	
	public String SerachSendEmails(String serachString,int webUserId,String type)
	{
		  query=new StringBuffer();		 
		  query.append("  select distinct feedbackSend.* from feedbackSend ");
		  query.append("   left join FeebackSendCustomerList on FeebackSendCustomerList.feedbackId=feedbackSend.feedbackId");
		  query.append(" where FeebackSendCustomerList.email like '%"+serachString+"%' ");
		  if(webUserId>0)
		  query.append(" and  feedbackSend.userId="+webUserId+" ");	
		  if(type.equalsIgnoreCase("S"))
		  {
			  query.append(" and feedbackSend.isSent='Y'");  
		  }
		  else if (type.equalsIgnoreCase("D"))
		  {
			  query.append(" and feedbackSend.isDrafted='Y' and feedbackSend.isSent!='Y' ");
		  }
		  else if (type.equalsIgnoreCase("SH"))
		  {
			  query.append(" and feedbackSend.isScheduled='Y' ");
		  }
		  return query.toString();
	}
		
	
	
	//*****************************************************************************************************	                            *****************************************************************************************************
		//*****************************************************************************************************	 Notes For Each Module   *****************************************************************************************************
		//*****************************************************************************************************                             *****************************************************************************************************	
				
				//Customer feedBack Send.
	
	
	
	public String saveNotesForEachModule(CustomerFeedbackModel obj)
	{
			//To remove '' i the SQL statements ;
			String memoEn=""; 
			if(obj.getMemoEn()!=null)
			{
				memoEn=obj.getMemoEn().replaceAll("'","`");
			}
			obj.setMemoEn(memoEn);
			
			String memoAr=""; 
			if(obj.getMemoAr()!=null)
			{
				memoAr=obj.getMemoAr().replaceAll("'","`");
			}
			obj.setMemoAr(memoAr);
			
		  query=new StringBuffer();		 
		  query.append(" Insert into NotesForEachModule (noteId,serviceListRefKey,memoEn,memoAr,modifiedDate,createdDate,UserID)");
		  query.append(" values("+obj.getNoteID()+","+obj.getServiceRefKey()+",'"+obj.getMemoEn()+"','"+obj.getMemoAr()+"','"+sdf.format(obj.getFeedbackCreateDate())+"','" +sdf.format(obj.getFeedBackModifiedDate())+"',"+obj.getWebuserID()+")");
		  return query.toString();
	}
	
	public String editLocalItem(CustomerFeedbackModel obj)
	{
		//To remove '' i the SQL statements ;
		String memoEn=""; 
		if(obj.getMemoEn()!=null)
		{
			memoEn=obj.getMemoEn().replaceAll("'","`");
		}
		obj.setMemoEn(memoEn);
		
		String memoAr=""; 
		if(obj.getMemoAr()!=null)
		{
			memoAr=obj.getMemoAr().replaceAll("'","`");
		}
		obj.setMemoAr(memoAr);
		query=new StringBuffer();
		query.append("Update LocalItem set Description='"+obj.getMemoEn()+"',DescriptionAR='"+obj.getMemoAr()+"',UserID="+obj.getWebuserID()+",ModifiedDate='"+sdf.format(obj.getFeedBackModifiedDate())+"'");	
		query.append(" where RecNo="+obj.getNoteID());
		query.append(" ");
		return query.toString();
	}
	
	public String editNotesForEachModule(CustomerFeedbackModel obj)
	{
		//To remove '' i the SQL statements ;
		String memoEn=""; 
		if(obj.getMemoEn()!=null)
		{
			memoEn=obj.getMemoEn().replaceAll("'","`");
		}
		obj.setMemoEn(memoEn);
		
		String memoAr=""; 
		if(obj.getMemoAr()!=null)
		{
			memoAr=obj.getMemoAr().replaceAll("'","`");
		}
		obj.setMemoAr(memoAr);
		query=new StringBuffer();
		query.append("Update NotesForEachModule set memoEn='"+obj.getMemoEn()+"',memoAr='"+obj.getMemoAr()+"',UserID="+obj.getWebuserID()+",serviceListRefKey="+obj.getServiceRefKey()+",ModifiedDate='"+sdf.format(obj.getFeedBackModifiedDate())+"'");	
		query.append(" where noteId="+obj.getNoteID());
		query.append(" ");
		return query.toString();
	}
		
	public String saveNotesForEachModuleHistory(CustomerFeedbackModel obj)
	{
			//To remove '' i the SQL statements ;
			String memoEn=""; 
			if(obj.getMemoEn()!=null)
			{
				memoEn=obj.getMemoEn().replaceAll("'","`");
			}
			obj.setMemoEn(memoEn);
			
			String memoAr=""; 
			if(obj.getMemoAr()!=null)
			{
				memoAr=obj.getMemoAr().replaceAll("'","`");
			}
			obj.setMemoAr(memoAr);
			
		  query=new StringBuffer();		 
		  query.append(" Insert into NotesForEachModuleHistory (noteId,serviceListRefKey,memoEn,memoAr,modifiedDate,createdDate,UserID)");
		  query.append(" values("+obj.getNoteID()+","+obj.getServiceRefKey()+",'"+obj.getMemoEn()+"','"+obj.getMemoAr()+"','"+sdf.format(obj.getFeedbackCreateDate())+"','" +sdf.format(obj.getFeedBackModifiedDate())+"',"+obj.getWebuserID()+")");
		  return query.toString();
	}
	
	
	public String getNotesForEachModule(int serviceId)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from NotesForEachModule");
		  if(serviceId>0)
		  query.append(" where serviceListRefKey="+serviceId+"");	  
		  return query.toString();
	}
	
	public String getReminderSignatureByCompanyQuery(int reminderID)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from ReminderSignatureByCompany");
		  if(reminderID>0)
		  query.append(" where reminderID="+reminderID+"");	  
		  return query.toString();
	}
	
	public String getNotesForEachModuleHistory(int serviceId)
	{
		  query=new StringBuffer();		 
		  query.append("Select NotesForEachModuleHistory.*,HRLISTVALUES.description from dbo.NotesForEachModuleHistory ");
		  query.append("left join HRLISTVALUES on HRLISTVALUES.id=NotesForEachModuleHistory.serviceListrefKey");
		  if(serviceId>0)
		  query.append(" where NotesForEachModuleHistory.serviceListRefKey="+serviceId+"");	  
		  return query.toString();
	}
	

	public String addNotesForEachModuleAttachmnetPath(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into NotesForEachModuleAttachmnet (notesId,path,fileName)");
		  query.append(" values("+obj.getNoteID()+",'"+obj.getAttchemnetPath()+"','"+obj.getFileName()+"')");
		  return query.toString();
	}
	
	
	public String deleteNotesForEachModuleAttachmnetPath(CustomerFeedbackModel obj)
	{
		  query=new StringBuffer();		
		  query.append("delete from NotesForEachModuleAttachmnet where notesId="+obj.getNoteID()+"");  		
		  return query.toString();
	}
	
	public String getNotesForEachModuleAttchamnet(int notesId)
	{
		  query=new StringBuffer();		 
		  query.append("Select * from NotesForEachModuleAttachmnet");
		  query.append(" where  notesId="+notesId+"");	  
		  return query.toString();
	}
	
	
	public String getEmailSignature(int userId)
	{
		query=new StringBuffer();
		query.append("select * from feedbackSendSignature");	
		query.append(" where userId="+userId);
		query.append(" ");
		return query.toString();
	}
	
	
	public String addSignature(EmailSignatureModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" Insert into feedbackSendSignature (recNo,userId,signature)");
		  query.append(" values("+obj.getRecNo()+","+obj.getUserId()+",'"+obj.getSignature()+"')");
		  return query.toString();
	}
	
	
	public String updateSignature(EmailSignatureModel obj)
	{
		  query=new StringBuffer();		 
		  query.append(" update feedbackSendSignature set userId="+obj.getUserId()+",signature='"+obj.getSignature()+"' where recNo="+obj.getRecNo());
		  return query.toString();
	}
	
	

}
