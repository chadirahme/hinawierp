package home;

import hba.CustomerData;
import hba.HBAQueries;
import hba.SalesRepQuerries;
import hba.TaskQueery;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.CompanyDBModel;
import model.CustomerContact;
import model.CustomerFeedbackModel;
import model.CustomerModel;
import model.CustomerStatusHistoryModel;
import model.EmailSignatureModel;
import model.EmployeeModel;
import model.FeedbackSendSources;
import model.HRListValuesModel;
import model.QbListsModel;
import model.ReminderSettingsModel;
import model.SalesRepModel;
import model.SerialFields;
import model.TaskAndFeeddbackRelation;

import org.apache.log4j.Logger;

import admin.TasksModel;


import company.ReminderData;
import db.SQLDBHandler;

public class CustomerFeedBackData {

	private Logger logger = Logger.getLogger(CustomerFeedBackData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	ReminderData data=new ReminderData();

	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");

	SQLDBHandler db=new SQLDBHandler("hinawi_hba");

	CustomerData customerData=new CustomerData();

	public CustomerFeedBackData(CompanyDBModel obj)
	{
		try
		{
			db = new SQLDBHandler(obj);
		}
		catch (Exception ex) 
		{
			logger.error("error in CustomerFeedBackData---Init-->" , ex);
		}
	}


	public int saveCustomerFeedbackData(CustomerFeedbackModel obj,List<QuotationAttachmentModel> attachmentModels)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.saveCustomerFeedback(obj));		
			if(result>0)
			{
				for(HRListValuesModel model :obj.getSelectedFeedBackType())
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackDetails(obj.getFeedbackKey(),model.getListId(),model.getEnDescription()));
				}
				String path="";
				String creationPath="";
				String repository="";
				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"CustomerFeedBack"+File.separator+obj.getFeedbackNUmber()+File.separator+objAtt.getFilename();
						creationPath=repository+"CustomerFeedBack"+File.separator+obj.getFeedbackNUmber()+"";
						if(objAtt.getImageMedia()!=null)
							customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addCustomerFeedbackAttachmnetPath(obj));
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---saveCustomerFeedbackData-->" , ex);
		}
		return result;

	}

	public int saveCustomerStatusHistroyfromFeedback(CustomerStatusHistoryModel obj,int webUserId,String webUserName)
	{
		int result=0;

		HBAQueries query=new HBAQueries();		
		try 
		{			
			result=db.executeUpdateQuery(query.saveCustomerStatusHistroyfromFeedback(obj,webUserId,webUserName));		
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---saveCustomerStatusHistroyfromFeedback-->" , ex);
		}
		return result;

	}

	public int updateCustomerStatusDescription(CustomerStatusHistoryModel obj)
	{
		int result=0;

		HBAQueries query=new HBAQueries();		
		try 
		{			
			result=db.executeUpdateQuery(query.updateCustomerStatusDescription(obj));		
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---updateCustomerStatusDescription-->" , ex);
		}
		return result;

	}

	public int editCustomerFeedBackData(CustomerFeedbackModel obj,List<QuotationAttachmentModel> attachmentModels)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.editCustomerFeedBackData(obj));		
			if(result>0)
			{
				if(obj.getSelectedFeedBackType().size()>0)
					db.executeUpdateQuery(query.deleteAllFeedbackDetailsById(obj));
				for(HRListValuesModel model :obj.getSelectedFeedBackType())
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackDetails(obj.getFeedbackKey(),model.getListId(),model.getEnDescription()));
				}
				String path="";
				String creationPath="";
				String repository="";
				if(attachmentModels!=null && attachmentModels.size()>0)
					db.executeUpdateQuery(query.deleteAllFeedbackAttachmnetPathbyId(obj));

				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"CustomerFeedBack"+File.separator+obj.getFeedbackNUmber()+File.separator+objAtt.getFilename();
						creationPath=repository+"CustomerFeedBack"+File.separator+obj.getFeedbackNUmber()+"";
						if(objAtt.getImageMedia()!=null)
							customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addCustomerFeedbackAttachmnetPath(obj));


					}
				}

				for(TaskAndFeeddbackRelation feeddbackRelation:obj.getTaskRelationlist())
				{
					String sqlqQuery="update tasks set customerRefKey ="+obj.getCustomerRefKey()+" where taskId ="+ feeddbackRelation.getTaskID()+"";
					db.executeUpdateQuery(sqlqQuery);
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---editCustomerFeedBackData-->" , ex);
		}
		return result;

	}



	public boolean checkIfFeedBackNumberIsDuplicate(String feedbackNumber,int feedbackID)
	{
		boolean hasSerialNumber=false;		

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfFeedBackNumberIsDuplicate(feedbackNumber,feedbackID));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---checkIfFeedBackNumberIsDuplicate-->" , ex);
		}

		return hasSerialNumber;
	}


	public List<CustomerFeedbackModel> getAllCutsomerFeedback(int webUserId,Date fromDate,Date toDate)
	{

		List<CustomerFeedbackModel> list=new ArrayList<CustomerFeedbackModel>();

		List<HRListValuesModel> feedbackType=new ArrayList<HRListValuesModel>();
		List<QuotationAttachmentModel> feedbackAttachment=new ArrayList<QuotationAttachmentModel>();
		List<TaskAndFeeddbackRelation> taskFeedBackRealtion=new ArrayList<TaskAndFeeddbackRelation>();

		//temp objects 


		//for performance 
		feedbackType.addAll(getFeedBackDetails(0));
		feedbackAttachment.addAll(getFeedbackAttchamnet(0));
		taskFeedBackRealtion.addAll(getTaskRelationData(0));


		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getAllCutsomerFeedback(webUserId,fromDate,toDate));
			while(rs.next())
			{	
				CustomerFeedbackModel obj=new CustomerFeedbackModel();
				obj.setFeedbackKey(rs.getInt("Enquiry_ID"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("ModifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("ModifiedDate")));
				obj.setCompanyName(rs.getString("Customer_Company_Name")==null?"":rs.getString("Customer_Company_Name"));
				obj.setCustomerName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setFeedbackNUmber(rs.getString("Enquiry_No")==null?"":rs.getString("Enquiry_No"));
				obj.setContactPersonName(rs.getString("ContactPersonName")==null?"":rs.getString("ContactPersonName"));
				obj.setCustomerRefKey(rs.getInt("CustomerRefKey"));
				obj.setCustomerInitailKey(rs.getInt("ContactPersonIntials"));
				obj.setMemo(rs.getString("Memo")==null?"":rs.getString("Memo"));
				obj.setMobile1(rs.getString("mobile1")==null?"":rs.getString("mobile1"));
				obj.setMobileAreaCode1(rs.getString("mobileAreaCode1")==null?"":rs.getString("mobileAreaCode1"));
				obj.setTelphoneAreaCode1(rs.getString("telephoneAreaCode1")==null?"":rs.getString("telephoneAreaCode1"));
				obj.setTelphone1(rs.getString("telephone1")==null?"":rs.getString("telephone1"));
				obj.setInstruction(rs.getString("instructions")==null?"":rs.getString("instructions"));
				obj.setEmail(rs.getString("Email")==null?"":rs.getString("Email"));
				String softType="";
				softType=rs.getString("Software_Type")==null?"":rs.getString("Software_Type");

				String customerType="";
				customerType=rs.getString("TypeOfCutomer")==null?"":rs.getString("TypeOfCutomer");
				if(customerType!=null && !customerType.equalsIgnoreCase(""))
				{
					if(customerType.equalsIgnoreCase("P"))
					{
						obj.setCustomerType("Prospective Client");
					}
					else
					{
						obj.setCustomerType("Customer");
					}
				}

				if(softType!=null && !softType.equalsIgnoreCase(""))
				{
					if(softType.equalsIgnoreCase("D"))
					{
						obj.setSelectedSoftwareType("Hinawi ERP Deskstop");
					}
					else if(softType.equalsIgnoreCase("W"))
					{
						obj.setSelectedSoftwareType("Hinawi Web Application");
					}
					else if(softType.equalsIgnoreCase("Q"))
					{
						obj.setSelectedSoftwareType("Quick Books");
					}
					else
					{
						obj.setSelectedSoftwareType("");
					}
				}
				obj.setIntialName(rs.getString("description")==null?"":rs.getString("description"));
				obj.setWebsite(rs.getString("website")==null?"":rs.getString("website"));
				if(obj.getFeedbackKey()>0)
				{
					List<HRListValuesModel> feedbackTypeTemp=new ArrayList<HRListValuesModel>();
					List<QuotationAttachmentModel> feedbackAttachmentTemp=new ArrayList<QuotationAttachmentModel>();
					List<TaskAndFeeddbackRelation> taskFeedBackRealtionTemp=new ArrayList<TaskAndFeeddbackRelation>();

					for(HRListValuesModel hrListValuesModel:feedbackType)
					{
						if(hrListValuesModel.getFieldId()==obj.getFeedbackKey())
						{
							feedbackTypeTemp.add(hrListValuesModel);
						}
					}
					for(QuotationAttachmentModel attachmentModel:feedbackAttachment)
					{
						if(attachmentModel.getAttachid()==obj.getFeedbackKey())
						{
							feedbackAttachmentTemp.add(attachmentModel);
						}

					}
					for(TaskAndFeeddbackRelation taskAndFeeddbackRelation:taskFeedBackRealtion)
					{
						if(taskAndFeeddbackRelation.getFeedbackKey()==obj.getFeedbackKey())
						{
							taskFeedBackRealtionTemp.add(taskAndFeeddbackRelation);

						}
					}

					obj.setSelectedFeedBackType(feedbackTypeTemp);
					obj.setLstAtt(feedbackAttachmentTemp);
					obj.setTaskRelationlist(taskFeedBackRealtionTemp);
				}
				list.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getAllCutsomerFeedback-->" , ex);
		}
		return list;
	}


	public CustomerFeedbackModel getCutsomerFeedbackById(int feedbackID)
	{

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
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
				if(obj.getFeedbackKey()>0)
				{

					obj.setSelectedFeedBackType(getFeedBackDetails(obj.getFeedbackKey()));
					obj.setLstAtt(getFeedbackAttchamnet(obj.getFeedbackKey()));
					obj.setTaskRelationlist(getTaskRelationData(obj.getFeedbackKey()));
				}
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getCutsomerFeedbackById-->" , ex);
		}
		return obj;
	}


	public List<QuotationAttachmentModel> getFeedbackAttchamnet(int feedbackID)
	{
		List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getFeedbackAttchamnet(feedbackID));
			while(rs.next())
			{			
				QuotationAttachmentModel obj=new QuotationAttachmentModel();
				obj.setAttachid(rs.getInt("Enquiry_ID"));
				obj.setFilepath(rs.getString("Attachment_Path")==null?"":rs.getString("Attachment_Path"));
				obj.setFilename(rs.getString("fileName")==null?"":rs.getString("fileName"));
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getFeedbackAttchamnet-->" , ex);
		}
		return lst;
	}


	public List<HRListValuesModel> getFeedBackDetails(int feedBackID)
	{
		List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getFeedBackDetails(feedBackID));
			while(rs.next())
			{				
				HRListValuesModel feedbackTypeDetails=new HRListValuesModel();
				feedbackTypeDetails.setFieldId(rs.getInt("enquiry_id"));
				feedbackTypeDetails.setListId(rs.getInt("feedBackTypeKey"));
				feedbackTypeDetails.setEnDescription(rs.getString("feedBackName")==null?"":rs.getString("feedBackName"));
				lst.add(feedbackTypeDetails);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getFeedBackDetails-->" , ex);
		}
		return lst;
	}


	public List<HRListValuesModel> getLocalItemListValuesForFeedBack(int itemTypeRef,String type)
	{
		List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		HRListValuesModel obj=new HRListValuesModel();
		if(!type.equals(""))
		{
			obj.setListId(0);					
			obj.setFieldName(type);
			lst.add(obj);
		}
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getLocalItemListValuesQuery(itemTypeRef));
			while(rs.next())
			{
				obj=new HRListValuesModel();
				obj.setListId(rs.getInt("RecNo"));					
				obj.setFieldName(rs.getString("FullName"));
				//obj.setArDescription(rs.getString("ARABIC"));
				//obj.setSubId(rs.getInt("SUB_ID"));
				//obj.setFieldId(rs.getInt("FIELD_ID"));
				//obj.setFieldName(rs.getString("FIELD_NAME"));
				obj.setPriorityId(rs.getInt("PRIORITY_ID"));
				obj.setEnDescription(rs.getString("Description"));
				obj.setArDescription(rs.getString("DescriptionAR"));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("ModifiedDate")));							
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in HRData---getHRListValuesForFeedBack-->" , ex);
		}
		return lst;
	}

	
	public List<HRListValuesModel> getHRListValuesForFeedBack(int fieldId,String type)
	{
		List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();

		HRListValuesModel obj=new HRListValuesModel();
		if(!type.equals(""))
		{
			obj.setListId(0);					
			obj.setEnDescription(type);
			lst.add(obj);
		}
		// SQLDBHandler db=new SQLDBHandler("HRONLINE");
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getHRListValuesQuery(fieldId));
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
			logger.error("error in HRData---getHRListValuesForFeedBack-->" , ex);
		}
		return lst;
	}


	public int getMaxID(String tableName,String fieldName)
	{
		int result=0;		
		HBAQueries query=new HBAQueries();	
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;

		}
		catch (Exception ex) 
		{
			logger.error("error in CustomerFeedBackData---getMaxID-->" , ex);
		}	
		return result;
	}


	///Quotation  HBA module
	public List<QbListsModel>  quotationPrecpectiveList()
	{

		List<QbListsModel> lst=new ArrayList<QbListsModel>();


		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		QbListsModel obj1=new QbListsModel();
		obj1.setRecNo(0);
		obj1.setName("Select");
		obj1.setFullName("Select");
		obj1.setSubLevel(0);
		obj1.setIsActive("None");
		obj1.setListType("None");
		lst.add(obj1);

		try 
		{
			rs=db.executeNonQuery(query.quotationPrecpectiveList());
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setName(rs.getString("name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSubLevel(rs.getInt("sublevel"));
				lst.add(obj);

			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---quotationPrecpectiveList-->" , ex);
		}
		return lst;

	}


	public List<QbListsModel> fillQbList(String ListType)
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();

		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		QbListsModel obj1=new QbListsModel();
		obj1.setRecNo(0);
		obj1.setName("Select");
		obj1.setFullName("Select");
		obj1.setSubLevel(0);
		obj1.setIsActive("None");
		obj1.setListType("None");
		lst.add(obj1);

		try 
		{
			rs=db.executeNonQuery(query.getQbListQuery(ListType));
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ListType"));
				if(rs.getString("isactive").equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else
				{
					obj.setIsActive("INActive");
				}

				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---fillQbList-->" , ex);
		}
		return lst;
	}




	//by iqbal for calculating next serial number
	public String ConfigSerialNumberCashInvoice(SerialFields field,String SerialNumber,int keyID)
	{
		String tmpSerialNumber = SerialNumber;
		String tmpFindLastIdx ;
		Boolean tmpStartInt   = false;
		String tmpFindNos  = "";
		double tmpRightPos  = 0.0;
		double tmpLeftPos  = 0.0;
		String tmpField; 
		String tmpConfigSerailNum="";
		Integer tmpX;
		try{


			if(keyID==0)
				tmpField=field.toString();
			else
				tmpField=field.toString()+"-"+String.valueOf(keyID);



			for(tmpX=SerialNumber.length();tmpX>=1;tmpX--)
			{
				tmpFindLastIdx = SerialNumber.substring(tmpX-1,tmpX);
				if("1234567890".indexOf(tmpFindLastIdx)!=-1)
				{
					if(tmpStartInt == false)
					{
						tmpStartInt = true;
						tmpRightPos = tmpX;
					}
					tmpFindNos = tmpFindLastIdx + tmpFindNos;

				}
				else if(tmpStartInt == true)
				{
					tmpLeftPos = tmpX + 1;
					break;

				}
			}


			if(tmpStartInt == true) 
			{
				String tmpSuffix  = "";
				String tmpPrefix = "";

				if(tmpLeftPos > 0)
					if( ((int) (long)Math.round(tmpLeftPos) - 1) <= SerialNumber.length())
						tmpSuffix =SerialNumber.substring(0, (int) (long)Math.round(tmpLeftPos) - 1);
				if(tmpRightPos > 0 )
					if(((int) (long)Math.round(tmpRightPos) + 1) <= SerialNumber.length())
						tmpPrefix = SerialNumber.substring((int) (long)Math.round(tmpRightPos) + 1);

				double tmpLastNumber  = Integer.parseInt(tmpFindNos) + 1;

				tmpConfigSerailNum = tmpSuffix + (int) (long)Math.round(tmpLastNumber) + tmpPrefix;
			}
			else
			{
				tmpConfigSerailNum = tmpSerialNumber;
			}

			HBAQueries query=new HBAQueries();		
			ResultSet rs = null;

			//check if dulicate exist serial number
			boolean isSerailFound=false;
			rs=db.executeNonQuery(query.GetSerialNumberQuery(tmpField));
			while(rs.next())
			{
				isSerailFound=true;
			}
			if(isSerailFound)
				db.executeUpdateQuery(query.updateSystemSerialNosQuery(tmpConfigSerailNum,tmpField));
			else
				db.executeUpdateQuery(query.insertSystemSerialNosQuery(tmpConfigSerailNum,tmpField));



		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---ConfigSerialNumberCashInvoice-->" , ex);
		}
		return tmpConfigSerailNum;					 																									
		//return SerialNumber;
	}



	// by iqbal--- for sale number-- cash invoice module--return type string
	public String GetSaleNumber(String serialField)
	{
		String LastNumber="1";

		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetSerialNumberQuery(serialField));
			while(rs.next())
			{
				LastNumber=rs.getString("LastNumber");						
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---GetSerialNumber-->" , ex);
		}
		return LastNumber;
	}

	//*****************************************************************************************************	                            *****************************************************************************************************
	//*****************************************************************************************************	Customer feedBack Send Data *****************************************************************************************************
	//*****************************************************************************************************                             *****************************************************************************************************	

	//Customer feedBack Send.

	@SuppressWarnings("unused")
	public List<CustomerFeedbackModel> getAllCutsomerFeedbackSent(int companyId,int webUserId,String type)
	{

		List<CustomerFeedbackModel> list=new ArrayList<CustomerFeedbackModel>();
		List<CustomerFeedbackModel> lstEmailDetails=new ArrayList<CustomerFeedbackModel>();
		List<ReminderSettingsModel> listScheduledEmailMysql=new ArrayList<ReminderSettingsModel>();
		List<FeedbackSendSources> listOfEmailSources=new ArrayList<FeedbackSendSources>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		ResultSet rsScheduled = null;
		List<CustomerFeedbackModel> listEmails=null;
		try 
		{
			lstEmailDetails=getFeedBackSentCusomersDetails(0);
			listScheduledEmailMysql=data.getAllScheduledEmails(companyId);
			listOfEmailSources=getAllEmailSources(0);
			
			rs=db.executeNonQuery(query.getAllCutsomerFeedbackSent(webUserId,type));
			while(rs.next())
			{	
				CustomerFeedbackModel obj=new CustomerFeedbackModel();
				obj.setFeedbackKey(rs.getInt("feedbackId"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("modifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("modifiedDate")));
				obj.setMemo(rs.getString("Memo")==null?"":rs.getString("Memo"));
				obj.setFeedbackNUmber(rs.getString("feedbackNumber")==null?"":rs.getString("feedbackNumber"));
				obj.setSubject(rs.getString("subject")==null?"":rs.getString("subject"));
				obj.setSentFromEmail(rs.getString("usedEmailToSend")==null?"":rs.getString("usedEmailToSend"));
				obj.setIsScheduled(rs.getString("isscheduled")==null?"":rs.getString("isscheduled"));
				if(obj.getFeedbackKey()>0)
				{
					listEmails=new ArrayList<CustomerFeedbackModel>(); 
					for(CustomerFeedbackModel model:lstEmailDetails)
					{
						if(obj.getFeedbackKey()==model.getFeedbackKey())
						{
							listEmails.add(model);
						}
						
					}
				}
				if(obj.getIsScheduled().equalsIgnoreCase("Y"))
				{
					
					for(ReminderSettingsModel model:listScheduledEmailMysql)
					{
						if(model.getMailId()==obj.getFeedbackKey())
						{
							if(model.isEnablereminder())
							{
								obj.setEnabaleReminder(true);
								obj.setSchedulerId(model.getReminderid());
							}
							else
							{
								obj.setEnabaleReminder(false);
								obj.setSchedulerId(model.getReminderid());
							}
						}
					}
					
				}
				Set<FeedbackSendSources> tempSources=new HashSet<FeedbackSendSources>(); 
				for(FeedbackSendSources model:listOfEmailSources)
				{
					if(model.getFeedbackKey()==obj.getFeedbackKey())
					{
					
						FeedbackSendSources surceName=new FeedbackSendSources();
						if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("C"))
						{
							surceName.setCustName(model.getCustName());
							surceName.setSourceType("Customer");
							surceName.setSourceId(model.getSourceId());
						}
						else if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("P"))
						{
							surceName.setCustName(model.getProsName());
							surceName.setSourceType("Prospective");
							surceName.setSourceId(model.getSourceId());
						}
						else if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("V"))
						{
							surceName.setCustName(model.getVendName());
							surceName.setSourceType("Vendor");
							surceName.setSourceId(model.getSourceId());
						}
						else if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("E"))
						{
							surceName.setCustName(model.getEmpNAme());
							surceName.setSourceType("Employee");
							surceName.setSourceId(model.getSourceId());
						}
						tempSources.add(surceName);
					}
				}
				obj.setSourcesLIst(tempSources);
				obj.setListOfSentEmails(listEmails);
				list.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getAllCutsomerFeedbackSent-->" , ex);
		}
		return list;
	}
	
	
	
	@SuppressWarnings("unused")
	public List<CustomerFeedbackModel> searchSources(int companyId,String search,int webUserId,String type,String searchType)
	{

		List<CustomerFeedbackModel> list=new ArrayList<CustomerFeedbackModel>();
		List<CustomerFeedbackModel> lstEmailDetails=new ArrayList<CustomerFeedbackModel>();
		List<ReminderSettingsModel> listScheduledEmailMysql=new ArrayList<ReminderSettingsModel>();
		Set<FeedbackSendSources> listOfEmailSources=new HashSet<FeedbackSendSources>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		ResultSet rsScheduled = null;
		List<CustomerFeedbackModel> listEmails=null;
		try 
		{
			lstEmailDetails=getFeedBackSentCusomersDetails(0);
			listScheduledEmailMysql=data.getAllScheduledEmails(companyId);
			listOfEmailSources=getAlluniqueEmailSourcesForSearch(0);
			
			if(searchType.equalsIgnoreCase("sourceType"))
			rs=db.executeNonQuery(query.searchSources(search,webUserId,type));
			else
			rs=db.executeNonQuery(query.SerachSendEmails(search,webUserId,type));	
			while(rs.next())
			{	
				CustomerFeedbackModel obj=new CustomerFeedbackModel();
				obj.setFeedbackKey(rs.getInt("feedbackId"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("modifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("modifiedDate")));
				obj.setMemo(rs.getString("Memo")==null?"":rs.getString("Memo"));
				obj.setFeedbackNUmber(rs.getString("feedbackNumber")==null?"":rs.getString("feedbackNumber"));
				obj.setSubject(rs.getString("subject")==null?"":rs.getString("subject"));
				obj.setSentFromEmail(rs.getString("usedEmailToSend")==null?"":rs.getString("usedEmailToSend"));
				obj.setIsScheduled(rs.getString("isscheduled")==null?"":rs.getString("isscheduled"));
				if(obj.getFeedbackKey()>0)
				{
					listEmails=new ArrayList<CustomerFeedbackModel>(); 
					for(CustomerFeedbackModel model:lstEmailDetails)
					{
						if(obj.getFeedbackKey()==model.getFeedbackKey())
						{
							listEmails.add(model);
						}
						
					}
				}
				if(obj.getIsScheduled().equalsIgnoreCase("Y"))
				{
					
					for(ReminderSettingsModel model:listScheduledEmailMysql)
					{
						if(model.getMailId()==obj.getFeedbackKey())
						{
							if(model.isEnablereminder())
							{
								obj.setEnabaleReminder(true);
								obj.setSchedulerId(model.getReminderid());
							}
							else
							{
								obj.setEnabaleReminder(false);
								obj.setSchedulerId(model.getReminderid());
							}
						}
					}
					
				}
				Set<FeedbackSendSources> tempSources=new HashSet<FeedbackSendSources>(); 
				for(FeedbackSendSources model:listOfEmailSources)
				{
					if(model.getFeedbackKey()==obj.getFeedbackKey())
					{
					
						FeedbackSendSources surceName=new FeedbackSendSources();
						if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("C"))
						{
							surceName.setCustName(model.getCustName());
							surceName.setSourceType("Customer");
							surceName.setSourceId(model.getSourceId());
						}
						else if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("P"))
						{
							surceName.setCustName(model.getProsName());
							surceName.setSourceType("Prospective");
							surceName.setSourceId(model.getSourceId());
						}
						else if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("V"))
						{
							surceName.setCustName(model.getVendName());
							surceName.setSourceType("Vendor");
							surceName.setSourceId(model.getSourceId());
						}
						else if(model.getSourceType()!=null && model.getSourceType().equalsIgnoreCase("E"))
						{
							surceName.setCustName(model.getEmpNAme());
							surceName.setSourceType("Employee");
							surceName.setSourceId(model.getSourceId());
						}
						tempSources.add(surceName);
					}
				}
				obj.setSourcesLIst(tempSources);
				obj.setListOfSentEmails(listEmails);
				list.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---searchSources-->" , ex);
		}
		return list;
	}
	
	
	


	
	
	public List<FeedbackSendSources> getAllEmailSources(int feedbackBy)
	{
		List<FeedbackSendSources> lst=new ArrayList<FeedbackSendSources>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		try
		{
			 ResultSet rs = null;
			 rs = db.executeNonQuery(query.getAllEmailSources(feedbackBy));	
			 while(rs.next())
			 {
				 FeedbackSendSources obj=new FeedbackSendSources();
				 obj.setFeedbackKey(rs.getInt("feedbackId"));
				 obj.setSourceType(rs.getString("sourceType"));
				 obj.setSourceId(rs.getInt("sourceId"));
				 obj.setCustName(rs.getString("custName")==null?"":rs.getString("custName"));
				 obj.setEmpNAme(rs.getString("empName")==null?"":rs.getString("empName"));
				 obj.setVendName(rs.getString("vendName")==null?"":rs.getString("vendName"));
				 obj.setProsName(rs.getString("prosName")==null?"":rs.getString("prosName"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in ReminderData---getAllEmailSources-->" , ex);			 	  
		 	 }
		return lst;
	
	}
	
	
	public Set<FeedbackSendSources> getAlluniqueEmailSourcesForSearch(int feedbackBy)//sourceswITH OUT DUPLICATES 
	{
		Set<FeedbackSendSources> lst=new HashSet<FeedbackSendSources>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		try
		{
			 ResultSet rs = null;
			 rs = db.executeNonQuery(query.getAllEmailSources(feedbackBy));	
			 while(rs.next())
			 {
				 FeedbackSendSources obj=new FeedbackSendSources();
				 obj.setFeedbackKey(rs.getInt("feedbackId"));
				 obj.setSourceType(rs.getString("sourceType"));
				 obj.setSourceId(rs.getInt("sourceId"));
				 obj.setCustName(rs.getString("custName")==null?"":rs.getString("custName"));
				 obj.setEmpNAme(rs.getString("empName")==null?"":rs.getString("empName"));
				 obj.setVendName(rs.getString("vendName")==null?"":rs.getString("vendName"));
				 obj.setProsName(rs.getString("prosName")==null?"":rs.getString("prosName"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in ReminderData---getAllEmailSources-->" , ex);			 	  
		 	 }
		return lst;
	
	}
	
	
	@SuppressWarnings("unused")
	public Set<FeedbackSendSources> searchEmailSources(String search)
	{
		Set<FeedbackSendSources> lst=new HashSet<FeedbackSendSources>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		try
		{
			 ResultSet rs = null;
			/* rs = db.executeNonQuery(query.getAllEmailSources());	
			 while(rs.next())
			 {
				 FeedbackSendSources obj=new FeedbackSendSources();
				 obj.setFeedbackKey(rs.getInt("feedbackId"));
				 obj.setSourceType(rs.getString("sourceType"));
				 obj.setSourceId(rs.getInt("sourceId"));
				 obj.setCustName(rs.getString("custName")==null?"":rs.getString("custName"));
				 obj.setEmpNAme(rs.getString("empName")==null?"":rs.getString("empName"));
				 obj.setVendName(rs.getString("vendName")==null?"":rs.getString("vendName"));
				 obj.setProsName(rs.getString("prosName")==null?"":rs.getString("prosName"));
				 lst.add(obj);
			 }*/
		}
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in ReminderData---getAllEmailSources-->" , ex);			 	  
		 	 }
		return lst;
	
	}

	@SuppressWarnings("resource")
	public int saveCustomerFeedbackSentData(CustomerFeedbackModel obj,List<QuotationAttachmentModel> attachmentModels,Set<FeedbackSendSources> tempSelectedEmailSources)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.saveCustomerFeedbackSentData(obj));		
			if(result>0)
			{
				for(String model :obj.getSelectedBcc())
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackCustomerDetails(obj.getFeedbackKey(),"BCC",model));
				}
				for(String model :obj.getSelectedCcs())
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackCustomerDetails(obj.getFeedbackKey(),"CC",model));
				}
				for(String model :obj.getSelectedTo())
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackCustomerDetails(obj.getFeedbackKey(),"TO",model));
				}
				for(FeedbackSendSources sources:tempSelectedEmailSources)
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackSourceDetails(obj.getFeedbackKey(),sources));
				}
				String path="";
				String creationPath="";
				String repository="";
				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"CustomerFeedBackSend"+File.separator+obj.getFeedbackNUmber()+File.separator+objAtt.getFilename();
						creationPath=repository+"CustomerFeedBackSend"+File.separator+obj.getFeedbackNUmber()+"";
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
						db.executeUpdateQuery(query.addCustomerFeedbackSendAttachmnetPath(obj));
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---saveCustomerFeedbackData-->" , ex);
		}
		return result;

	}

	@SuppressWarnings("resource")
	public int editCustomerFeedBackSentData(CustomerFeedbackModel obj,List<QuotationAttachmentModel> attachmentModels,Set<FeedbackSendSources> tempSelectedEmailSources,int webUserId,String webUserName)
	{
		int result=0;
		Date newDate;
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.editCustomerFeedBackSentData(obj));		
			if(result>0)
			{
				if(obj.getSelectedBcc().size()>0 || obj.getSelectedCcs().size()>0 || obj.getSelectedTo().size()>0)
				{
					db.executeUpdateQuery(query.deleteAllFeedbackCustomers(obj));
					for(String model :obj.getSelectedBcc())
					{
						result=db.executeUpdateQuery(query.addCustomerFeedbackCustomerDetails(obj.getFeedbackKey(),"BCC",model));
					}
					for(String model :obj.getSelectedCcs())
					{
						result=db.executeUpdateQuery(query.addCustomerFeedbackCustomerDetails(obj.getFeedbackKey(),"CC",model));
					}
					for(String model :obj.getSelectedTo())
					{
						result=db.executeUpdateQuery(query.addCustomerFeedbackCustomerDetails(obj.getFeedbackKey(),"TO",model));
					}
					
				}
				db.executeUpdateQuery(query.deleteAllFeedbackSourceDetails(obj.getFeedbackKey()));
				for(FeedbackSendSources sources:tempSelectedEmailSources)
				{
					result=db.executeUpdateQuery(query.addCustomerFeedbackSourceDetails(obj.getFeedbackKey(),sources));
					
					//update CustomerStatus
					if(sources!=null && sources.getSourceId()>0 && sources.getSourceType()!=null && sources.getSourceType().equalsIgnoreCase("C"))
					{
						newDate=df.parse(sdf.format(c.getTime()));
						//UpdateStatusDescription
						StringBuffer notes=new StringBuffer("");
						notes.append(obj.getSubject()==null?"":obj.getSubject().replace("'", "`"));
						notes.append(" / E-Mail Ref Number - ");
						notes.append(obj.getFeedbackNUmber()==null?"":obj.getFeedbackNUmber().replace("'", "`"));
						if(obj.getIsDrafted()!=null && obj.getIsDrafted().equalsIgnoreCase("Y"))
						{
							notes.append(" Drafated.");
						}
						else if(obj.getIsSent()!=null && obj.getIsSent().equalsIgnoreCase("Y"))
						{
							notes.append(" Sent.");
						}
						else if(obj.getIsScheduled()!=null && obj.getIsScheduled().equalsIgnoreCase("Y"))
						{
							notes.append(" Scheduled.");
						}
						if(!notes.equals(""))
						{
						String sqlqQuery="Update Customer set StatusDesc='" + notes+"' where Cust_Key=" + sources.getSourceId();
						db.executeUpdateQuery(sqlqQuery);
						//check if same data enter before
						sqlqQuery="Select * from customerstatusHistory where RecNo =(select max(recNo) from customerstatushistory where " +
								" custKey =" + sources.getSourceId() + " and type ='C' ) ";
						ResultSet rs = db.executeNonQuery(sqlqQuery);
						boolean dataSame=false;
						while(rs.next())
						{
							if(rs.getInt("StatusID")==0 && rs.getString("StatusDescription").equals(notes))
							{
								dataSame=true;
							}
						}
						if(dataSame==false)
						{
						int recNO=getMaxID("CustomerStatusHistory", "RecNo");
						
						sqlqQuery="Insert into CustomerStatusHistory(RecNo,CustKey,ActionDate,StatusID,	StatusDescription,UserID,Type , CreatedFrom,WebUserID,WebUserName)" +
								" values (" + recNO +" , " + sources.getSourceId() + " ,'"+sdf.format(newDate)+"',0, '" + notes + "' , 1,'C','Online Email',"+webUserId+",'"+webUserName+"' )";
						db.executeUpdateQuery(sqlqQuery);
						}
						
						}
					}
					
				}
				String path="";
				String creationPath="";
				String repository="";
				if(attachmentModels!=null && attachmentModels.size()>0)
					db.executeUpdateQuery(query.deleteAllFeedbackSendAttachmnetPath(obj));

				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"CustomerFeedBackSend"+File.separator+obj.getFeedbackNUmber()+File.separator+objAtt.getFilename();
						creationPath=repository+"CustomerFeedBackSend"+File.separator+obj.getFeedbackNUmber()+"";
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
						db.executeUpdateQuery(query.addCustomerFeedbackSendAttachmnetPath(obj));


					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---editCustomerFeedBackData-->" , ex);
		}
		return result;

	}


	public boolean checkIfFeedBackSendNumberIsDuplicate(String feedbackNumber,int feedbackID)
	{
		boolean hasSerialNumber=false;		

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfFeedBackSendNumberIsDuplicate(feedbackNumber,feedbackID));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---checkIfFeedBackNumberIsDuplicate-->" , ex);
		}

		return hasSerialNumber;
	}



	public CustomerFeedbackModel getCustomerFeedBackSendById(int feedbackID)
	{

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		CustomerFeedbackModel obj=new CustomerFeedbackModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCustomerFeedBackSendById(feedbackID));
			while(rs.next())
			{	

				obj.setFeedbackKey(rs.getInt("feedbackId"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("modifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("modifiedDate")));
				obj.setMemo(rs.getString("Memo")==null?"":rs.getString("Memo"));
				obj.setSubject(rs.getString("subject")==null?"":rs.getString("subject"));
				obj.setFeedbackNUmber(rs.getString("feedbackNumber")==null?"":rs.getString("feedbackNumber"));
				obj.setSubject(rs.getString("subject")==null?"":rs.getString("subject"));
				obj.setServiceRefKey(rs.getInt("serviceListKey"));
				obj.setTaskID(rs.getInt("taskKey"));
				/*	if(obj.getFeedbackKey()>0)
					{
						obj.setSelectedFeedBackType(getFeedBackDetails(obj.getFeedbackKey()));
						obj.setLstAtt(getFeedbackAttchamnet(obj.getFeedbackKey()));
					}
					list.add(obj);*/
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getCutsomerFeedbackById-->" , ex);
		}
		return obj;
	}


	public List<QuotationAttachmentModel> getFeedbacksendAttchamnet(int feedbackID)
	{
		List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getFeedbacksendAttchamnet(feedbackID));
			while(rs.next())
			{			
				QuotationAttachmentModel obj=new QuotationAttachmentModel();
				obj.setFilepath(rs.getString("path")==null?"":rs.getString("path"));
				obj.setFilename(rs.getString("fileName")==null?"":rs.getString("fileName"));
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getFeedbackAttchamnet-->" , ex);
		}
		return lst;
	}


	public List<CustomerFeedbackModel> getFeedBackSentCusomersDetails(int feedBackID)
	{
		List<CustomerFeedbackModel> lst=new ArrayList<CustomerFeedbackModel>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getFeedBackSentCusomersDetails(feedBackID));
			while(rs.next())
			{				
				CustomerFeedbackModel feedbackTypeDetails=new CustomerFeedbackModel();
				feedbackTypeDetails.setFeedbackKey(rs.getInt("feedbackId"));
				feedbackTypeDetails.setCustomerRefKey(rs.getInt("customerRefKey"));
				feedbackTypeDetails.setEmailType(rs.getString("emailType")==null?"":rs.getString("emailType"));
				feedbackTypeDetails.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				lst.add(feedbackTypeDetails);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getFeedBackDetails-->" , ex);
		}
		return lst;
	}


	public List<CustomerModel> getCustomerList(String status)
	{
		List<CustomerModel> lstCustomers=new ArrayList<CustomerModel>();	
		List<CustomerContact> lstCustomerContact=new ArrayList<CustomerContact>();	
		List<CustomerContact> lstCustomerContactTemp;
		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		lstCustomerContact=getCustomerContact();
		try 
		{
			rs=db.executeNonQuery(query.getCustomersQuery(status));
			while(rs.next())
			{
				CustomerModel obj=new CustomerModel();
				obj.setCustkey(rs.getInt("Cust_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name")==null?"":rs.getString("name"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName")==null?"":rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				obj.setBillCountry(rs.getString("billCountry")==null?"":rs.getString("billCountry"));
				obj.setPhone(rs.getString("phone")==null?"":rs.getString("phone"));
				obj.setAltphone(rs.getString("altphone")==null?"":rs.getString("altphone"));
				obj.setFax(rs.getString("fax")==null?"":rs.getString("fax"));
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setcC(rs.getString("cc")==null?"":rs.getString("cc"));
				obj.setContact(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setSalesRepKey(rs.getInt("SalesRepKey"));
				obj.setArName(rs.getString("ArName")==null?"":rs.getString("ArName"));
				obj.setBalckListed(rs.getString("blackListed")==null?"Not Set":rs.getString("blackListed"));
				obj.setAltcontact(rs.getString("AltContact"));
				obj.setMobile(rs.getString("Mobiletelphone2")==null?"":rs.getString("Mobiletelphone2"));
				obj.setcC(rs.getString("CC")==null?"":rs.getString("CC"));
				obj.setWebsite(rs.getString("Website"));
				obj.setSkypeId(rs.getString("SkypeID"));
				obj.setPobox(rs.getString("CompPoBox"));
				obj.setBillCity(rs.getString("BillCity"));
			
				
				if(obj.getBalckListed().equalsIgnoreCase("Y"))
				{
					obj.setBalckListed("Yes");
				}
				else if(obj.getBalckListed().equalsIgnoreCase("N"))
				{
					obj.setBalckListed("No");
				}

				if(rs.getDate("cusContractExpiry")!=null)
				{
					obj.setCustomerContactExpiryDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("cusContractExpiry"))==null?"":new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("cusContractExpiry")));
				}
				else
				{
					obj.setCustomerContactExpiryDateStr("");
				}
				obj.setSublevel(rs.getInt("Sublevel"));
				if(obj.getIsactive().equalsIgnoreCase("Y"))
				{
					obj.setIsactive("Active");
				}
				else
				{
					obj.setIsactive("InActive");
				}
				lstCustomerContactTemp=new ArrayList<CustomerContact>();
				for(CustomerContact customerContact:lstCustomerContact)//to get the customer contact information 
				{
					if(customerContact.getCust_key()==obj.getCustkey())
					{
						lstCustomerContactTemp.add(customerContact);
					}

				}
				obj.setCustomerContacts(lstCustomerContactTemp);
				obj.setLocalBalance(rs.getDouble("LocalBalance"));				
				lstCustomers.add(obj);
			}

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getCustomerList-->" , ex);
		}
		return lstCustomers;
	}

	public List<CustomerContact> getCustomerContact()
	{
		List<CustomerContact> lstCustomers=new ArrayList<CustomerContact>();		
		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCustomerContact());
			while(rs.next())
			{
				CustomerContact obj=new CustomerContact();
				obj.setCust_key(rs.getInt("recNO"));
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setDefaultFlag(rs.getString("defaultCont")==null?"":rs.getString("defaultCont"));
				lstCustomers.add(obj);
			}

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getCustomerContact-->" , ex);
		}
		return lstCustomers;
	}





	public int saveNotesForEachModule(CustomerFeedbackModel obj,List<QuotationAttachmentModel> attachmentModels)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.saveNotesForEachModule(obj));
			if(result>0)
			{
				db.executeUpdateQuery(query.saveNotesForEachModuleHistory(obj));//store notes history	
				String path="";
				String creationPath="";
				String repository="";
				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"NotesAndGerenralNotes"+File.separator+obj.getNoteID()+File.separator+objAtt.getFilename();
						creationPath=repository+"NotesAndGerenralNotes"+File.separator+obj.getNoteID()+"";
						if(objAtt.getImageMedia()!=null)
							customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addNotesForEachModuleAttachmnetPath(obj));
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---saveNotesForEachModule-->" , ex);
		}
		return result;

	}

	public int editLocalItem(CustomerFeedbackModel obj)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.editLocalItem(obj));	
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---editLocalItem-->" , ex);
		}
		return result;

	}
	
	public int editNotesForEachModule(CustomerFeedbackModel obj,List<QuotationAttachmentModel> attachmentModels)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.editNotesForEachModule(obj));	
			if(result>0)
			{
				db.executeUpdateQuery(query.saveNotesForEachModuleHistory(obj));//store notes history
				String path="";
				String creationPath="";
				String repository="";
				if(attachmentModels!=null && attachmentModels.size()>0)
					db.executeUpdateQuery(query.deleteNotesForEachModuleAttachmnetPath(obj));

				for(QuotationAttachmentModel objAtt :attachmentModels)
				{
					if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
					{
						repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"";
						path=repository+"NotesAndGerenralNotes"+File.separator+obj.getNoteID()+File.separator+objAtt.getFilename();
						creationPath=repository+"NotesAndGerenralNotes"+File.separator+obj.getNoteID()+"";
						if(objAtt.getImageMedia()!=null)
							customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addNotesForEachModuleAttachmnetPath(obj));


					}
				}
			}

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---editNotesForEachModule-->" , ex);
		}
		return result;

	}


	public CustomerFeedbackModel getNotesForModules(int serviceId)
	{

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		CustomerFeedbackModel obj=new CustomerFeedbackModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNotesForEachModule(serviceId));
			while(rs.next())
			{	

				obj.setNoteID(rs.getInt("noteId"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("modifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("modifiedDate")));
				obj.setMemoAr(rs.getString("memoAr")==null?"":rs.getString("memoAr"));
				obj.setMemoEn(rs.getString("memoEn")==null?"":rs.getString("memoEn"));
				obj.setServiceRefKey(rs.getInt("serviceListRefKey"));

			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getNotesForModules-->" , ex);
		}
		return obj;
	}

	public String getReminderSignatureByCompany(int reminderID)
	{

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		ResultSet rs = null;
		String emailSignature="";
		try 
		{
			rs=db.executeNonQuery(query.getReminderSignatureByCompanyQuery(reminderID));
			while(rs.next())
			{	
			    emailSignature=rs.getString("Signature")==null?"":rs.getString("Signature");				
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getReminderSignatureByCompany-->" , ex);
		}
		return emailSignature;
	}
	
	public List<CustomerFeedbackModel> getNotesForEachModuleHistory(int serviceId)
	{

		List<CustomerFeedbackModel> lst=new ArrayList<CustomerFeedbackModel>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			
			CustomerFeedbackModel objNew=new CustomerFeedbackModel();
			objNew.setNoteID(0);
			objNew.setServiceName("Select");
			lst.add(objNew);
			
			rs=db.executeNonQuery(query.getNotesForEachModuleHistory(serviceId));
			while(rs.next())
			{	
				CustomerFeedbackModel obj=new CustomerFeedbackModel();
				obj.setNoteID(rs.getInt("noteId"));
				obj.setFeedbackCreateDate(rs.getDate("createdDate"));
				obj.setFeedBackModifiedDate(rs.getDate("modifiedDate"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createdDate")));
				obj.setModifeldDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("modifiedDate")));
				obj.setMemoAr(rs.getString("memoAr")==null?"":rs.getString("memoAr"));
				obj.setMemoEn(rs.getString("memoEn")==null?"":rs.getString("memoEn"));
				obj.setServiceRefKey(rs.getInt("serviceListRefKey"));
				obj.setServiceName(rs.getString("description")==null?"":rs.getString("description")+" "+new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("createdDate")));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getNotesForEachModuleHistory-->" , ex);
		}
		return lst;
	}

	public List<QuotationAttachmentModel> getNotesForModulesAttchamnet(int notesID)
	{
		List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNotesForEachModuleAttchamnet(notesID));
			while(rs.next())
			{			
				QuotationAttachmentModel obj=new QuotationAttachmentModel();
				obj.setFilepath(rs.getString("path")==null?"":rs.getString("path"));
				obj.setFilename(rs.getString("fileName")==null?"":rs.getString("fileName"));
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getFeedbackAttchamnet-->" , ex);
		}
		return lst;
	}



	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public ArrayList getCilentAttachemnt()
	{

		StringBuffer result=null;		
		ArrayList fileArray = new ArrayList();
		try
		{
			String repository1 = System.getProperty("catalina.home")+File.separator+"uploads"+File.separator;
			logger.info("local>>> "+repository1);
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"ClientAttachment"+File.separator;
			logger.info("live>>> "+repository);
			//String filePath=repository+"HinawiERPMenus.pdf";	
			String filePath=repository+"HinawiBrochure.jpg";	
			File dir = new File(filePath);
			if(dir.exists())
				fileArray.add(filePath);	
			
			filePath=repository+"Relation Between Modules in Hinawi Software.jpg";	
			dir = new File(filePath);
			if(dir.exists())
				fileArray.add(filePath);
			
		}
		catch (Exception ex) 
		{
			logger.error("error in CustomerFeedBackData---getCilentAttachemnt-->" , ex);
		}

		return fileArray;
	}


	// company employees 

	@SuppressWarnings("unused")
	public List<EmployeeModel> getEmployeeList(int compkey,String listtype,String status,int supervisorId) 
	{
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForInactiveStatus=new ArrayList<EmployeeModel>();
		List<EmployeeModel> lstOfEmployeesForActiveStatus=new ArrayList<EmployeeModel>();
		//
		HRQueries query=new HRQueries();
		ResultSet rs = null;
		ResultSet rs1 = null;
		try 
		{
			if(!listtype.equals(""))
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(0);
				obj.setEmployeeNo("");
				obj.setFullName(listtype);
				lstEmployees.add(obj);
			}
			Date createDateNew;
			Calendar c = Calendar.getInstance();
			createDateNew=df.parse(sdf.format(c.getTime()));
			ResultSet newRsActive = null;
			ResultSet newRs = null;
			String inactiveSatus="";
			newRs=db.executeNonQuery(query.getEmpStatusDescriptionforInactive(1));
			newRsActive=db.executeNonQuery(query.getEmpStatusDescriptionForActive(1,createDateNew));
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


			/*
				if(compkey!=0)
				{
					EmployeeModel obj=new EmployeeModel();
					obj.setEmployeeKey(0);
					obj.setEmployeeNo("0");
					obj.setFullName("ALL");
					lstEmployees.add(obj);			
				}
			 */

			rs=db.executeNonQuery(query.getEmployeesQuery(compkey,status,supervisorId));
			while(rs.next())
			{
				EmployeeModel obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setEmployeeNo(rs.getString("EmployeeNo")==null?"":rs.getString("EmployeeNo"));
				obj.setFullName(rs.getString("FullName")==null?"":rs.getString("FullName"));
				obj.setArabicName(rs.getString("ArabicName")==null?"":rs.getString("ArabicName"));
				obj.setDepartmentID(rs.getInt("DepartmentID"));
				obj.setDepartment(rs.getString("Department")==null?"":rs.getString("Department"));
				obj.setArabicDepartment(rs.getString("ArabicDepartment")==null?"":rs.getString("ArabicDepartment"));
				obj.setPositionID(rs.getInt("PositionID"));
				obj.setPosition(rs.getString("Position")==null?"":rs.getString("Position"));
				obj.setArabicPosition(rs.getString("ArabicPosition")==null?"":rs.getString("ArabicPosition"));
				obj.setNationalityID(rs.getInt("CountryID"));
				obj.setCountry(rs.getString("Country")==null?"Local":rs.getString("Country"));
				obj.setCompanyName(rs.getString("CompanyName"));
				obj.setDateOfBirth(rs.getDate("DateOfBirth"));
				obj.setAge(rs.getString("Age")==null?"":rs.getString("Age"));
				obj.setEnFirstName(rs.getString("EnglishFirstName")==null?"":rs.getString("EnglishFirstName"));
				obj.setEnMiddleName(rs.getString("EnglishMiddleName")==null?"":rs.getString("EnglishMiddleName"));
				obj.setEnLastName(rs.getString("EnglishLastName"));
				obj.setSupervisorId(rs.getInt("SupervisorID"));
				obj.setSupervisorName(rs.getString("supervisorName")==null?"":rs.getString("supervisorName"));
				obj.setWorkGroupName(rs.getString("workergroupname")==null?"":rs.getString("workergroupname"));
				obj.setProjectName(rs.getString("location")==null?"":rs.getString("location"));
				//	obj.setArMiddleName(rs.getString("ARABIC_MIDDLE"));
				//	obj.setArLastName(rs.getString("ARABIC_LAST"));
				obj.setEmployeementDate(rs.getDate("EmployeementDate"));
				obj.setEmployeementDateString(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("EmployeementDate")));
				obj.setGender(rs.getString("Sex")==null?"":rs.getString("Sex"));
				obj.setMaritalStatus(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setMarital(rs.getString("Marital")==null?"":rs.getString("Marital"));
				obj.setEmployeeStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				String active=rs.getString("Active");
				obj.setSuper_supervisorId(rs.getInt("SuperSupervisorId"));
				obj.setSuper_supervisorName(rs.getString("SuperAdminName")==null?"":rs.getString("SuperAdminName"));
				obj.setSuper_supervisorNameAR(rs.getString("ArabicSuperAdminName")==null?"":rs.getString("ArabicSuperAdminName"));
				String terminate=rs.getString("Terminate");
				obj.setStatusDescription("");//add as default for filter used

				rs1=db.executeNonQuery(query.getEmail(obj.getEmployeeKey()));
				List<String> listEmailsTemp=new ArrayList<String>();
				while(rs1.next())
				{
					String email=rs1.getString("details")==null?"":rs1.getString("details");
					listEmailsTemp.add(email);
				}
				obj.setListEmails(listEmailsTemp);

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
				lstEmployees.add(obj);
			}

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getEmployeeList-->" , ex);
		}
		return lstEmployees;
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
							customerData.createFile(objAtt.getImageMedia().getStreamData(),creationPath,objAtt.getFilename());
						obj.setAttchemnetPath(path);
						obj.setFileName(objAtt.getFilename());
						db.executeUpdateQuery(query.addTaskAttachmnetPath(obj));
					}
				}
			}
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---addTask-->" , ex);
		}
		return result;

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
			logger.error("error in CustomerFeedBackData---saveFeedbackCustomerRelation-->" , ex);
		}
		return result;

	}


	public List<TaskAndFeeddbackRelation> getTaskRelationData(int feedbackID)
	{
		List<TaskAndFeeddbackRelation> lst=new ArrayList<TaskAndFeeddbackRelation>();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getTaskRelationData(feedbackID));
			while(rs.next())
			{			
				TaskAndFeeddbackRelation obj=new TaskAndFeeddbackRelation();
				obj.setTaskID(rs.getInt("taskId"));
				obj.setFeedbackKey(rs.getInt("feedbackId"));
				obj.setTaskName(rs.getString("TaskName")==null?"":rs.getString("TaskName"));
				obj.setTaskStatus(rs.getString("TaskStatusStr")==null?"":rs.getString("TaskStatusStr"));
				obj.setTaskNo(rs.getString("TaskNo")==null?"":rs.getString("TaskNo"));
				obj.setUserId(rs.getInt("userId"));
				obj.setCreatedDateStr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("createdDate")));
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getTaskRelationData-->" , ex);
		}
		return lst;
	}



	@SuppressWarnings("unused")
	public String getsalesRepListByID(int salesrepKey)
	{
		SalesRepModel obj=new SalesRepModel();
		SalesRepQuerries query=new SalesRepQuerries();
		QbListsModel objQb=new QbListsModel();
		String salesRepEmail="";
		String sqlqQuery="";

		ResultSet rs = null;
		ResultSet rsQb = null;
		ResultSet rsGetEmail =null;
		try 
		{
			//rs=db.executeNonQuery(query.getsalesRepListByID(salesrepKey));
			/*while(rs.next())
				{
					obj.setQbListKey(rs.getInt("qblistkey"));
				}*/

			sqlqQuery="select * from qblists where recNo=" + salesrepKey;
			rsQb=db.executeNonQuery(sqlqQuery);
			while(rsQb.next())
			{
				objQb.setRecNo(rsQb.getInt("recNo"));
				objQb.setListType(rsQb.getString("ListType")==null?"":rsQb.getString("ListType"));
				objQb.setHrEmpKey(rsQb.getInt("hremp_key"));
			}

			if(objQb.getListType()!=null && !objQb.getListType().equalsIgnoreCase("") && objQb.getListType().equalsIgnoreCase("Employee"))
			{
				sqlqQuery="select * from empcontact where contact_id=622 and emp_key="+objQb.getHrEmpKey();
				rsGetEmail=db.executeNonQuery(sqlqQuery);
				while(rsGetEmail.next())
				{
					salesRepEmail=rsGetEmail.getString("details")==null?"":rsGetEmail.getString("details");
				}

			}
			else if(objQb.getListType()!=null && !objQb.getListType().equalsIgnoreCase("") && objQb.getListType().equalsIgnoreCase("Vendor"))
			{
				sqlqQuery="select * from vendor where vend_key=" + objQb.getRecNo();
				rsGetEmail=db.executeNonQuery(sqlqQuery);
				while(rsGetEmail.next())
				{
					salesRepEmail=rsGetEmail.getString("email")==null?"":rsGetEmail.getString("email");
				}

			}
			else if (objQb.getListType()!=null && !objQb.getListType().equalsIgnoreCase("") && objQb.getListType().equalsIgnoreCase("Customer"))
			{
				sqlqQuery="select * from customer where cust_key=" + objQb.getRecNo();
				rsGetEmail=db.executeNonQuery(sqlqQuery);
				while(rsGetEmail.next())
				{
					salesRepEmail=rsGetEmail.getString("email")==null?"":rsGetEmail.getString("email");
				}

			}


		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getsalesRepListByID-->" , ex);
		}
		return salesRepEmail;
	}

	public int editCustomerFeedBackSentFromScheuler(CustomerFeedbackModel obj)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.editCustomerFeedBackSentFromScheuler(obj));		

		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---editCustomerFeedBackSentFromScheuler-->" , ex);
		}
		return result;

	}

	public EmailSignatureModel getEmailSignature(int userId)
	{
		EmailSignatureModel obj=new EmailSignatureModel();
		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getEmailSignature(userId));
			while(rs.next())
			{			

				obj.setRecNo(rs.getInt("recNo"));
				obj.setUserId(rs.getInt("userId"));
				obj.setSignature(rs.getString("signature")==null?"":rs.getString("signature"));
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---getEmailSignature-->" , ex);
		}
		return obj;

	}	


	public int addSignature(EmailSignatureModel obj)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.addSignature(obj));		
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---addSignature-->" , ex);
		}
		return result;

	}


	public int updateSignature(EmailSignatureModel obj)
	{
		int result=0;

		CustomerFeedBackQuerries query=new CustomerFeedBackQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.updateSignature(obj));		
		}
		catch (Exception ex) {
			logger.error("error in CustomerFeedBackData---updateSignature-->" , ex);
		}
		return result;

	}



}
