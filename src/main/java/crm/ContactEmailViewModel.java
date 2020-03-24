package crm;

import hba.HBAQueries;
import home.MailClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.BulkEmailModel;
import model.CompanyDBModel;
import model.ContactEmailModel;
import model.CustomerFeedbackModel;
import model.EmailSignatureModel;
import model.HRListValuesModel;
import model.ReminderSettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;
import common.EncryptEmail;
import db.DBHandler;
import db.SQLDBHandler;

public class ContactEmailViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private List<ContactEmailModel> lstEmails;
	private String email;
	private String bulkmEails;
	private String viewType;
	private BulkEmailModel objBulkEmailModel;
	private List<BulkEmailModel> lstBulkEmails;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	private String emailTo;
	
	private List<String> selectionTYpe=new ArrayList<String>();
	private List<HRListValuesModel> lstTemplates;
	private HRListValuesModel selectedTemplate;
	
	private String selectedType="";
	private String emailCount;
	WebusersModel dbUser=null;
	CompanyDBModel objSQLDB;
	//CustomerFeedBackData feedBackData=null;
	private String emailBody ="";
	private String emailContains ="";
	private CustomerFeedbackModel selectedNotesForEachModule=new CustomerFeedbackModel();
	
	private List<String> bulkGroups;
	private String selectBulkGroup;
	private List<String> industryGroups;
	private String selectedIndustryGroup;
	
	private boolean withReminder;
	private ReminderSettingsModel selectedReminderTypes;
	private ListModelList<String> lstQuotationWeekDays;
	private Set<String> selectedQuotationWeekDays;
	private ListModelList<String> lstQuotationMonths;
	private Set<String> selectedQuotationMonths;
	private ListModelList<String> lstQuotationDays;
	private Set<String> selectedQuotationDays;	
	private int countSelectedEmails;
	
	private List<String> lstEmailStatus;
	private String selectedEmailStatus;
	int bulkEmailId=0;
	private boolean enableBulkGroup;
	
	@Init	
	public void init(@BindingParam("type") String type)
	{
		try
		{
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		String formtype= (String) map.get("formtype");
		formtype=formtype==null?"":formtype;
		logger.info("formtype>>> " + formtype);
		
			
		viewType=type ==null ? "" : type;
		logger.info(viewType);
		
		Session sess = Sessions.getCurrent();		
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		
		bulkmEails="";
		
		if(viewType.equals("list"))
		{
			
			fillEmailGroups();
		}
		
		if(viewType.equals("bulkEmail"))
		{
			getGdriveDir();
			
			selectionTYpe.add("Select E-mails");
			selectionTYpe.add("Customer");
			selectionTYpe.add("Prospective");
			selectionTYpe.add("Customer & Prospective");
			selectionTYpe.add("Employee");
			selectionTYpe.add("Vendor");
			selectionTYpe.add("Bulk");
			selectedType=selectionTYpe.get(0);					
			
			fillEmailGroups();
			
			lstEmailStatus=new ArrayList<String>();
			lstEmailStatus.add("All");
			lstEmailStatus.add("New");
			lstEmailStatus.add("Sending");
			lstEmailStatus.add("Complete");
			lstEmailStatus.add("Stopped");
			lstEmailStatus.add("Queued");
			selectedEmailStatus=lstEmailStatus.get(0);	
								
			
			objSQLDB=new CompanyDBModel();
			HBAQueries query = new HBAQueries();
			ResultSet rs=null;
			DBHandler mysqldb=new DBHandler();
			rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
			while (rs.next()) 
			{
				objSQLDB.setCompanyId(rs.getInt("companyid"));
				objSQLDB.setDbid(rs.getInt("dbid"));
				objSQLDB.setUserip(rs.getString("userip"));
				objSQLDB.setDbname(rs.getString("dbname"));
				objSQLDB.setDbuser(rs.getString("dbuser"));
				objSQLDB.setDbpwd(rs.getString("dbpwd"));
				objSQLDB.setDbtype(rs.getString("dbtype"));
			}
//			feedBackData=new CustomerFeedBackData(objSQLDB);
//			lstTemplates=feedBackData.getHRListValuesForFeedBack(147,"Select");
//			if(lstTemplates!=null && lstTemplates.size()>0)
//				selectedTemplate=lstTemplates.get(0);
			
			fillMonthList();
			selectedQuotationMonths=new HashSet<String>();
			selectedQuotationDays=new HashSet<String>();
			selectedQuotationWeekDays=new HashSet<String>();
			
			Calendar c = Calendar.getInstance();
			selectedReminderTypes=new ReminderSettingsModel();
			selectedReminderTypes.setReminderid(0);
			selectedReminderTypes.setCompanyid(0);
			selectedReminderTypes.setRemindername("Select");
			selectedReminderTypes.setRemindertype(1);
			selectedReminderTypes.setStartdate(df.parse(sdf.format(c.getTime())));
			selectedReminderTypes.setRemindertime(df.parse(sdf.format(c.getTime())));
			selectedReminderTypes.setRemindersetting(2+"");
			selectedReminderTypes.setRepeatedays(1);
			selectedReminderTypes.setWeekly("Thursday,Friday");
			selectedReminderTypes.setMonthly("January,February");				 
			selectedReminderTypes.setMonthlydays("1,2");
			selectedReminderTypes.setExpireddate(df.parse(sdf.format(c.getTime())));
			selectedReminderTypes.setEnablereminder(false);	
			selectedReminderTypes.setSendtotype("2");
			selectedReminderTypes.setServiceListRefKey(0);
			selectedReminderTypes.setCcemail("");
			selectedReminderTypes.setAllcustomers(false);
			
			if(!selectedReminderTypes.getWeekly().equals(""))
			{
				List<String> weeklyList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getWeekly().split(",")));
				for (String item : weeklyList)
				{
					selectedQuotationWeekDays.add(item);	
				}							
			}	
			
			
			if(formtype.equals("edit"))
			{
				//bulkEmailId= map.get("bulkEmailId");
				lstBulkEmails=new ArrayList<BulkEmailModel>();
				DBHandler db=new DBHandler();
				rs=null;
				String sql="Select * from bulkemail where bulkEmailId=" +bulkEmailId ;			
				rs=db.executeNonQuery(sql);
				BulkEmailModel obj=new BulkEmailModel();
				
				while(rs.next())
				{					
					 obj.setBulkEmailId(rs.getInt("BulkEmailId"));
					 obj.setDescreption(rs.getString("Descreption"));
					 obj.setEmailsFilter(rs.getString("EmailsFilter"));
					 obj.setEmailType(rs.getString("emailtype"));
					 selectedType=obj.getEmailType();
					 obj.setNumberofMails(rs.getInt("NumberofMails"));
					 countSelectedEmails=obj.getNumberofMails();
					 obj.setSubject(rs.getString("Subject"));
					 obj.setEmailBody(rs.getString("EmailBody"));
					 obj.setEmailStatus(rs.getInt("EmailStatus"));
					 obj.setCreationdate(sdf.format(rs.getDate("creationdate")));
					 if(obj.getEmailStatus()==1)
						 obj.setStatus("New");
					 if(obj.getEmailStatus()==2)
						 obj.setStatus("Sending");
					 if(obj.getEmailStatus()==3)
						 obj.setStatus("Complete");
					 if(obj.getEmailStatus()==4)
						 obj.setStatus("Stopped");					 				 
					 obj.setLastTimeRun(rs.getDate("lastTimeRun")==null?"": sdf.format(rs.getDate("lastTimeRun")));
					 obj.setNumberEmailsend(rs.getInt("NumberEmailsend"));
					 			
					 if(rs.getBoolean("withreminder"))
						 obj.setStatus("Queued");
					 
				 }
				
				objBulkEmailModel=obj;
				emailBody=obj.getEmailBody();
				//selectedType=obj.getEmailType();
				//emailCount=obj.getNumberofMails();
				
			}
		}
		
		}
		catch (Exception ex) 
		{
			 logger.error("error in ContactEmailViewModel---init-->" , ex);			
		}
	}
	private void getGdriveDir()
	{
		try
		{
			//http://gdriv.es/accountingcrm
			//https://developers.google.com/drive/v3/web/quickstart/java#prerequisites
			//https://developers.google.com/api-client-library/java/apis/drive/v3#sample
			
		}
		catch (Exception ex) 
		{
			 logger.info("error in ContactEmailViewModel---getGdriveDir-->" , ex);			
		}
	}
	
	private void fillMonthList()
	{

		lstQuotationWeekDays=new ListModelList<String>();
		lstQuotationWeekDays.add("Saturday");
		lstQuotationWeekDays.add("Sunday");
		lstQuotationWeekDays.add("Monday");
		lstQuotationWeekDays.add("Tuesday");
		lstQuotationWeekDays.add("Wednesday");
		lstQuotationWeekDays.add("Thursday");
		lstQuotationWeekDays.add("Friday");

		lstQuotationMonths=new ListModelList<String>();
		lstQuotationMonths.add("January");
		lstQuotationMonths.add("February");
		lstQuotationMonths.add("March");
		lstQuotationMonths.add("April");
		lstQuotationMonths.add("May");
		lstQuotationMonths.add("June");
		lstQuotationMonths.add("July");
		lstQuotationMonths.add("August");
		lstQuotationMonths.add("September");
		lstQuotationMonths.add("October");
		lstQuotationMonths.add("November");
		lstQuotationMonths.add("December");

		lstQuotationDays=new ListModelList<String>();
		for (int i = 1; i < 32; i++) 
		{
			lstQuotationDays.add(i+"");
		}
		lstQuotationDays.add("Last");

	}

	private void fillEmailGroups()
	{
		bulkGroups=new ArrayList<String>();
		bulkGroups.add("Select Group");
		bulkGroups.add("@eim.ae");
		bulkGroups.add("Saudi");
		bulkGroups.add("Qatar");
		
		bulkGroups.add("Gmail , Yahoo");
		bulkGroups.add(".Com");
		bulkGroups.add("GCC");
		bulkGroups.add("Jordan");
		bulkGroups.add("Auditors - Jordan");
		bulkGroups.add("Eygpt");
		bulkGroups.add("Libya");
		bulkGroups.add("Others");
		selectBulkGroup=bulkGroups.get(0);
		
		
		industryGroups=new ArrayList<String>();
		industryGroups.add("Select Industry");
		industryGroups.add("Auditors");
		industryGroups.add("Banks");
		industryGroups.add("Construction");
		industryGroups.add("Factories");
		industryGroups.add("Hospitals");
		industryGroups.add("Hotels & Motels");
		industryGroups.add("Manufacturing");
		industryGroups.add("Miscellaneous");
		industryGroups.add("Oil and Gas");
		industryGroups.add("Real Estate");
		industryGroups.add("Rent Car");
		industryGroups.add("Telecom");
		industryGroups.add("Wholesale Trade");
		industryGroups.add("Others");
		selectedIndustryGroup=industryGroups.get(0);
	}
	
	public ContactEmailViewModel()
	{
		try
		{
		email="";
		emailTo="hinawi@eim.ae";//"eng.chadi@gmail.com";
		objBulkEmailModel=new BulkEmailModel();
		objBulkEmailModel.setEmailsFilter("");
		objBulkEmailModel.setUnsubscribe(true);		
		//getContactEmailList();
		}
		catch (Exception ex) 
		{
			 logger.error("error in ContactEmailViewModel---init-->" , ex);			
		}		
	}
	@Command
	@NotifyChange("lstEmails") 
	public void getContactEmailList()
	{
		try 
		{
			lstEmails=new ArrayList<ContactEmailModel>();
			DBHandler db=new DBHandler();
			ResultSet rs=null;
			String sql="Select * from contactemails ORDER BY Email LIMIT 1000";
			rs=db.executeNonQuery(sql);
			ContactEmailModel obj;
			while(rs.next())
			{
				 obj=new ContactEmailModel();
				 obj.setEmailId(rs.getInt("EmailId"));
				 obj.setEmail(rs.getString("Email"));
				 lstEmails.add(obj);
			 }			
		}  
		catch (Exception ex) 
		{
			 logger.error("error in ContactEmailViewModel---getContactEmailList-->" , ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstEmails"})
	public void searchEmailCommand()
	{
	try
	{
		if(email.equals("") && selectBulkGroup.equals("Select Group") && selectedIndustryGroup.equals("Select Industry"))
		{
			Messagebox.show("Please select some filter !!","Email Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		
		lstEmails=new ArrayList<ContactEmailModel>();
		DBHandler db=new DBHandler();
		ResultSet rs=null;
		String sql="Select * from contactemails where 1=1  ";
		if(!email.equals(""))
		{
			sql+=" and email like '%" +email + "%'";
		}
		if(!selectBulkGroup.equals("Select Group"))
		{
			sql+=" and bulkgroup='" + selectBulkGroup +"'";
		}
		if(!selectedIndustryGroup.equals("Select Industry"))
		{
			sql+=" and industrygroup='" + selectedIndustryGroup +"'";
		}
		sql+=" ORDER BY Email LIMIT 1000";
		
		//sql=String.format(sql, email);
		rs=db.executeNonQuery(sql);
		ContactEmailModel obj;
		while(rs.next())
		{
			 obj=new ContactEmailModel();
			 obj.setEmailId(rs.getInt("EmailId"));
			 obj.setEmail(rs.getString("Email"));
			 obj.setUnsubscribe(rs.getBoolean("unsubscribe"));
			 obj.setUnsubscribedate(rs.getDate("unsubscribedate"));
			 obj.setBulkGroup(rs.getString("bulkgroup")==null?"" : rs.getString("bulkgroup"));	
			 obj.setIndustryGroup(rs.getString("industrygroup")==null?"" : rs.getString("industrygroup"));
			 lstEmails.add(obj);
		 }			
		
		
		}
		catch (Exception ex) 
		{
		 logger.error("error in ContactEmailViewModel---searchEmailCommand-->" , ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstEmails"})
	public void unSubscribeCommand(@BindingParam("row") ContactEmailModel item)
	{
		try
		{
			DBHandler db=new DBHandler();
			item.setUnsubscribe(!item.isUnsubscribe());
			int updateValue=item.isUnsubscribe()?1:0;
			
			String sql="update contactemails set unsubscribedate=now() , unsubscribe="+updateValue 
					 + " where EmailId=" + item.getEmailId();
			 db.executeUpdateQuery(sql);
			 			 
		}
		catch (Exception ex) 
		{
		 logger.error("error in ContactEmailViewModel---searchEmailCommand-->" , ex);			
	    }
	}
	
	@Command
	@NotifyChange({"lstEmails"})
	public void changeEditableStatus(@BindingParam("row") ContactEmailModel item)
	{
		item.setEditingStatus(!item.isEditingStatus());	
	}
	
	@Command
	@NotifyChange({"lstEmails"})
	public void addNewEmailCommand()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();		
			Executions.createComponents("/crm/newContactEmails.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> NewEmailCommand", ex);			
		}
		
		/*if(lstEmails==null)
			lstEmails=new ArrayList<ContactEmailModel>();
		
		
		for (ContactEmailModel item : lstEmails) 
		{
			if(item.getEmailId()==0)
			{
				Messagebox.show("Please save the added email first !!","Email Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		
		ContactEmailModel obj=new ContactEmailModel();
		obj.setEmailId(0);
		obj.setEmail("");	
		obj.setEditingStatus(true);
		lstEmails.add(0, obj);	*/			
	}
	
	@Command
	@NotifyChange({"bulkmEails"})
	public void saveBulkEmailsCommand()
	{
		int rec=0;
		try
		{
			Statement st = null;
			DBHandler db=new DBHandler();
			String sql="";						
			if(bulkmEails.equals(""))
			{
				Messagebox.show("Please enter bulk emails first !!","Email Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(selectBulkGroup.equals("Select Group"))
			{
				Messagebox.show("Please select bulk group !!","Email Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(selectedIndustryGroup.equals("Select Industry"))
			{
				Messagebox.show("Please select industry group !!","Email Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			
			String[] lstEmails=bulkmEails.split(",");
			
			 logger.info("Begin Batch insert");
			 db.connect();
			 Connection conn=db.getConnection();	
			 st = conn.createStatement();	
			 st.addBatch(" START TRANSACTION ");
			 for (String mail : lstEmails) 
			 	{
				 	mail=mail.replace("<", "").replace(">", "").replace("\"", "").trim();
				    String[] lstEmails2=mail.split(" ");
				 	for (String mail2 : lstEmails2) 
				 	{
				 		if(mail2.contains("@"))
					 	{
					 	rec++;
						sql="insert into contactemails(Email,bulkgroup,industrygroup) values ('" +mail2 +"' ,'" +selectBulkGroup +"' ,'"+selectedIndustryGroup  +"' )";
						st.addBatch(sql);
					 	} 
				 	}
				 	
				 	
					 //rec= db.executeUpdateQuery(sql);	
					 //logger.info("rec=" + rec);				
				}
			 st.addBatch(" COMMIT ");
			 st.executeBatch();
			 st.clearBatch();	
			 logger.info("End Batch insert");
			
			 Messagebox.show(rec + " Bulk Emails Saved.","Contact Email", Messagebox.OK , Messagebox.EXCLAMATION);
			 bulkmEails="";			
		}
		catch (Exception ex) 
		  {
			  bulkmEails="";	
			 // logger.error("error in ContactEmailViewModel---saveEmailInfo-->" , ex);	
			  Messagebox.show(rec + " Emails Saved.","Contact Email", Messagebox.OK , Messagebox.EXCLAMATION);
		  }
	}
	
	@Command
	@NotifyChange("lstEmails") 
	public void saveEmailInfo(@BindingParam("row") ContactEmailModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 int rec=0;
		 try
		{
			 obj.setEmail(obj.getEmail().replace("\\", ""));
			 obj.setEmail(obj.getEmail().replace("'", ""));
			 
			 if(obj.getEmailId()==0)
			 {
				 sql="insert into contactemails(Email) values ('" +obj.getEmail() +"')";
				 rec= db.executeUpdateQuery(sql);				 				
			 }
			 else
			 {
				 sql="update contactemails set Email='"+obj.getEmail()+"'" 
						 + " where EmailId=" + obj.getEmailId();
				rec= db.executeUpdateQuery(sql);	
				obj.setEmailId(rec);
			 }			 
			 obj.setEditingStatus(false);
			 if(rec>0)
			 Messagebox.show("Data Saved..","Contact Email", Messagebox.OK , Messagebox.EXCLAMATION);
			 else
		    Messagebox.show("Email already exsists !! ","Contact Email", Messagebox.OK , Messagebox.ERROR);	 
		}
		  catch (Exception ex) 
		  {
			  //logger.error("error in ContactEmailViewModel---saveEmailInfo-->" , ex);			
		}
	}
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange("lstEmails") 
	public void deleteEmailCommand(@BindingParam("row") final ContactEmailModel row)
	{		
		if(row.getEmailId()==0)	
		{
		 lstEmails.remove(row);		
		 return;
		}
		
		Messagebox.show("Are you sure to delete this email ?","Delete",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
                {					
					 if(row.getEmailId()>0)
						deleteEmail(row);					 
                }
			 }
			
		 });			
	}
	
	@NotifyChange("lstEmails") 
	private void deleteEmail(ContactEmailModel obj)
	{
	try
	{
		DBHandler db=new DBHandler();
		String sql="";
		 
			 if(obj.getEmailId()>0)
			 {
				 sql="delete from contactemails where EmailId=" + obj.getEmailId();
				 db.executeUpdateQuery(sql);	
				 lstEmails.remove(obj);
			 }				 
	}
	catch (Exception ex) 
	{
		 logger.error("error in ContactEmailViewModel---deleteEmail-->" , ex);			
	}
	}
	
	@Command
	public void NewEmailCommand()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("feedBackKey", 0);
			arg.put("compKey",0);
			arg.put("type","add");
			Executions.createComponents("/crm/newBulkEmail.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> NewEmailCommand", ex);			
		}
	}
	
	@Command
	public void setUpEmailSignature()
	{
		try
		{			
//			EmailSignatureModel selectedemailSignature=feedBackData.getEmailSignature(-1);
//			selectedemailSignature.setReminderId(100);
//			Map<String,Object> arg = new HashMap<String,Object>();
//			arg.put("type","add");
//			ReminderSettingsModel selectedReminderTypes=new ReminderSettingsModel();
//			selectedReminderTypes.setReminderid(100);
//			selectedReminderTypes.setRemindername("Bulk");
//			arg.put("selectedReminderType",selectedReminderTypes);
//			arg.put("slectedSignature",selectedemailSignature);
//			Executions.createComponents("/company/reminderEmailSignature.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> setUpEmailSignature", ex);			
		}
	}
	
	@Command
	@NotifyChange({"emailBody"})
	public void addEmailSignature()
	{
		try
		{
			//EmailSignatureModel selectedemailSignature=feedBackData.getEmailSignature(-1);
			//emailBody+= selectedemailSignature.getSignature();
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> setUpEmailSignature", ex);			
		}
	}
		
	@Command
	public void SendNewEmailCommand()
	{
		try
		{
			 Statement st = null;
			 DBHandler db=new DBHandler();
			 String sql="";
			 ResultSet rs=null;
			 
			 objBulkEmailModel.setEmailBody(emailBody.replaceAll("'","`"));
			 objBulkEmailModel.setEmailType(selectedType);
			 String emailFilter=emailContains;
			 if(!selectedIndustryGroup.equals("Select Industry"))
				 emailFilter+=" / " +selectedIndustryGroup;
			
			 objBulkEmailModel.setEmailsFilter(emailFilter);
			 objBulkEmailModel.setNumberofMails(countSelectedEmails);
			
			 if(bulkEmailId==0)
			 {
			 sql="insert into bulkemail(Descreption,EmailsFilter,NumberofMails,Subject,EmailBody,EmailStatus,unsubscribe,emailtype,withreminder) "
			 		+ "values ('%s','%s','%s','%s','%s','%s','%s','%s','%s') ";
			 sql=String.format(sql, objBulkEmailModel.getDescreption() , objBulkEmailModel.getEmailsFilter(),objBulkEmailModel.getNumberofMails(),
					 objBulkEmailModel.getSubject(),objBulkEmailModel.getEmailBody(),1,objBulkEmailModel.isUnsubscribe()?"1":"0",objBulkEmailModel.getEmailType(),withReminder?1:0);
			 
			 bulkEmailId=db.executeUpdateQuery(sql);				 				
			if(bulkEmailId>0)
			{
				/*if(!objBulkEmailModel.getEmailsFilter().equals(""))
				sql="Select * from contactemails where email like '%" +objBulkEmailModel.getEmailsFilter() + "%' ORDER BY Email LIMIT " +objBulkEmailModel.getNumberofMails();
				else
				sql="Select * from contactemails LIMIT " +objBulkEmailModel.getNumberofMails();*/
				
				List<ContactEmailModel> lstNewEmails=new ArrayList<ContactEmailModel>();
				/*rs=db.executeNonQuery(sql);
				ContactEmailModel obj;
				while(rs.next())
				{
					 obj=new ContactEmailModel();
					 obj.setEmailId(rs.getInt("EmailId"));
					 obj.setEmail(rs.getString("Email"));
					 lstNewEmails.add(obj);
				 }	*/
				
				lstNewEmails=getEmailTypeList();
				logger.info("Begin Batch insert");	 
				 db.connect();
				 Connection conn=db.getConnection();	
				 st = conn.createStatement();	
				 st.addBatch(" START TRANSACTION ");
				for (ContactEmailModel item : lstNewEmails) 
				{												
				 sql="insert into bulkemaildetail(BulkEmailId,EmailId,Email,EmailStatus) "
					 		+ "values ('%s','%s','%s','%s') ";
				 sql=String.format(sql, bulkEmailId,item.getEmailId(),item.getEmail(),1);
				 st.addBatch(sql);
				}
				 
				 st.addBatch(" COMMIT ");
				 st.executeBatch();
				 st.clearBatch();			 
				// dbw.closeStatement(st);
				 //dbw.closeConnection(conn);
				 logger.info("End Batch insert");	
				 
				 if(withReminder)
				 {
					 saveReminder(bulkEmailId);					 
				 }
				// sql="insert into bulkemaildetail(BulkEmailId,EmailId,Email,EmailStatus) "
				//	 		+ "values ('%s','%s','%s','%s') ";
				// sql=String.format(sql, BulkEmailId,item.getEmailId(),item.getEmail(),1);
				// db.executeUpdateQuery(sql);	
				 Messagebox.show("Bulk Email Saved to send.","Contact Email", Messagebox.OK , Messagebox.EXCLAMATION);
			}
			else
			{
				 Messagebox.show("Error in New Bulk Email Saved !!","Contact Email", Messagebox.OK , Messagebox.ERROR);
			}
			
			 }
			 else if(bulkEmailId>0)
			 {
				 sql="update bulkemail set Descreption='%s' ,  EmailBody='%s' , Subject='%s' where BulkEmailId='%s'";
				 sql=String.format(sql, objBulkEmailModel.getDescreption() ,objBulkEmailModel.getEmailBody(),
						 objBulkEmailModel.getSubject(),bulkEmailId);
				 
				 db.executeUpdateQuery(sql);		
				 Messagebox.show("Bulk Email Saved..","Contact Email", Messagebox.OK , Messagebox.EXCLAMATION);
			 }
								 								
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> SendNewEmailCommand", ex);			
		}
	}
	
	private void saveReminder(int BulkEmailId)
	{
		try
		{
			String addedQuotationWeekDays=""; 
			String addedQuotationMonths=""; 
			String addedQuotationMonthDays=""; 
			
			for (String item : selectedQuotationWeekDays)
			{
				addedQuotationWeekDays+=item+",";
			}
			if(!addedQuotationWeekDays.equals(""))
				addedQuotationWeekDays=addedQuotationWeekDays.substring(0, addedQuotationWeekDays.length()-1) ;

			for (String item : selectedQuotationMonths)
			{
				addedQuotationMonths+=item+",";
			}
			if(!addedQuotationMonths.equals(""))
				addedQuotationMonths=addedQuotationMonths.substring(0, addedQuotationMonths.length()-1) ;

			for (String item : selectedQuotationDays)
			{
				addedQuotationMonthDays+=item+",";
			}
			if(!addedQuotationMonthDays.equals(""))
				addedQuotationMonthDays=addedQuotationMonthDays.substring(0, addedQuotationMonthDays.length()-1) ;

			selectedReminderTypes.setWeekly(addedQuotationWeekDays);
			selectedReminderTypes.setMonthly(addedQuotationMonths);
			selectedReminderTypes.setMonthlydays(addedQuotationMonthDays);
			
			//data.saveReminderSettings(selectedReminderTypes,tempCustomerList,tempProspectiveList);
			
			 DBHandler db=new DBHandler();
			 String sql="";
			 sql="insert into remindersettings (companyid,remindername,bulkemailid,enablereminder,startdate,expireddate,remindersetting,repeatedays,weekly,monthly,monthlydays,remindertype)"
			 		+ " values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',1)";
			 sql=String.format(sql, dbUser.getCompanyid(),objBulkEmailModel.getSubject(),BulkEmailId,1,"2015-12-31","2016-12-31",
					 selectedReminderTypes.getRemindersetting(),1,selectedReminderTypes.getWeekly(),selectedReminderTypes.getMonthly(),selectedReminderTypes.getMonthlydays() );
			 db.executeUpdateQuery(sql);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> saveReminder", ex);			
		}
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) 
			{	
				String descreption =  (String)ctx.getProperties("descreption")[0].getValue();
				Integer numberofMails = countSelectedEmails; //ctx.getProperties("numberofMails")[0].getValue();
				String subject =  (String)ctx.getProperties("subject")[0].getValue();
				String emailbody = emailBody; //(String)ctx.getProperties("emailBody")[0].getValue();
				
				if(selectedType.equals("Select E-mails"))
				{
					 addInvalidMessage(ctx, "Email type is required !");
				}
				
				if(Strings.isBlank(descreption))
				{
					  addInvalidMessage(ctx, "Descreption is required !");
				}
				if(numberofMails==0)
				{
					  addInvalidMessage(ctx, "Number of Mails is required !");
				}
				if(Strings.isBlank(subject))
				{
					  addInvalidMessage(ctx, "Subject is required !");
				}
				if(Strings.isBlank(emailbody))
				{
					  addInvalidMessage(ctx, "EmailBody is required !");
				}
			}
			
		};	
	}
	
	@Command
	@NotifyChange({"lstBulkEmails"})
	public void searchBulkEmailSent()
	{
		try
		{
			lstBulkEmails=new ArrayList<BulkEmailModel>();
			DBHandler db=new DBHandler();
			ResultSet rs=null;
			String sql="Select * from bulkemail where isactive=1 ";	
			if(selectedEmailStatus.equals("New"))				
				sql+=" and EmailStatus=1";
			if(selectedEmailStatus.equals("Sending"))				
				sql+=" and EmailStatus=2";
			if(selectedEmailStatus.equals("Complete"))				
				sql+=" and EmailStatus=3";
			if(selectedEmailStatus.equals("Stopped"))				
				sql+=" and EmailStatus=4";
			if(selectedEmailStatus.equals("Queued"))			
				sql+=" and withreminder=1";
			
			sql+=" ORDER BY EmailStatus";
			
			rs=db.executeNonQuery(sql);
			BulkEmailModel obj;
			
			while(rs.next())
			{
				 obj=new BulkEmailModel();
				 obj.setBulkEmailId(rs.getInt("BulkEmailId"));
				 obj.setDescreption(rs.getString("Descreption"));
				 obj.setEmailsFilter(rs.getString("EmailsFilter"));
				 obj.setNumberofMails(rs.getInt("NumberofMails"));
				 obj.setSubject(rs.getString("Subject"));
				 obj.setEmailBody(rs.getString("EmailBody"));
				 obj.setEmailStatus(rs.getInt("EmailStatus"));
				 obj.setWithReminder(rs.getBoolean("withreminder"));
				 obj.setCreationdate(sdf.format(rs.getDate("creationdate")));
				 if(obj.getEmailStatus()==1)
					 obj.setStatus("New");
				 if(obj.getEmailStatus()==2)
					 obj.setStatus("Sending");
				 if(obj.getEmailStatus()==3)
					 obj.setStatus("Complete");
				 if(obj.getEmailStatus()==4)
					 obj.setStatus("Stopped");
				 				 				 
				 obj.setLastTimeRun(rs.getDate("lastTimeRun")==null?"": sdf.format(rs.getDate("lastTimeRun")));
				 obj.setNumberEmailsend(rs.getInt("NumberEmailsend"));
				 obj.setEmailType(rs.getString("emailtype"));
				 
				 //if(rs.getBoolean("withreminder"))
					// obj.setStatus("Queued");
				 
				 lstBulkEmails.add(obj);
			 }			
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> searchBulkEmailSent", ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstBulkEmails"})
	public void stopEmailCommand(@BindingParam("row") BulkEmailModel row)
	{
		try
		{
			String sql="";
			int status=0;
			DBHandler db=new DBHandler();
			if(row.getEmailStatus()==4)
				status=1;
			if(row.getEmailStatus()==1 || row.getEmailStatus()==2)
				status=4;
			if(row.getEmailStatus()==3)
				status=1;
			
			
			if(status>0)
			{
			sql="update bulkemail set EmailStatus=" + status 
					 + " where BulkEmailId=" + row.getBulkEmailId();
			int rec= db.executeUpdateQuery(sql);
			if(row.getEmailStatus()==3)
			{
				sql="update bulkemaildetail set EmailStatus=1"
						 + " where BulkEmailId=" + row.getBulkEmailId();
				rec= db.executeUpdateQuery(sql);
			}
			
			searchBulkEmailSent();
			}
			else
			{
				 Messagebox.show("Bulk Email already send..","Contact Email", Messagebox.OK , Messagebox.EXCLAMATION);
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> stopEmailCommand", ex);			
		}
	}
	
	@Command
	public void editEmailCommand(@BindingParam("row") BulkEmailModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("bulkEmailId", row.getBulkEmailId());
			arg.put("compKey",0);
			arg.put("formtype","edit");
			Executions.createComponents("/crm/newBulkEmail.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> NewEmailCommand", ex);			
		}
	}
	
	@Command
	public void SendTestEmailCommand()
	{
		try
		{
//			if(emailTo.isEmpty())
//			{
//				Messagebox.show("Enter Email !!!");
//				return;
//			}
			String encrypt=EncryptEmail.encrypt(emailTo);
			
			MailClient mc = new MailClient();
			//mc.sendTestEmail();
			if(!emailTo.equals("eng.chadi@gmail.com"))
				emailTo+=",eng.chadi@gmail.com";
			
			String tomail=emailTo;//"eng.chadi@gmail.com";
			String[] to=tomail.split(",");
			//mc.sendTestEmail();
			//mc.sendGmailMail("", "eng.chadi@gmail.com",to, "subject", "messageBody", null);
			
			if(objBulkEmailModel.isUnsubscribe())
			{
			String unSubscribe="<p>If you are no longer interested, you can "
					+ "<a href='http://hinawi2.dyndns.org:8181/sata/Unsubscribe.zul?email="+ encrypt+"'>Unsubscribe</a>"
							+ "  from our mail system.</p>";			
			objBulkEmailModel.setEmailBody(objBulkEmailModel.getEmailBody() + unSubscribe);
			}
			
			objBulkEmailModel.setEmailBody(emailBody);			
			mc.sendMochaMail(to, null, null, objBulkEmailModel.getSubject(), objBulkEmailModel.getEmailBody(), true, null, true, "", null);	
			Messagebox.show("Test Email is Send..");
		}
	
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> SendTestEmailCommand", ex);			
		}
	}
	
	private int getEmailTypeCount()
	{
		int count=0;
		ResultSet rs=null;
		String sql="";
				
		try
		{
			if(selectedType.equals("Bulk"))
			{
			DBHandler db=new DBHandler();			
			sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0";			
			rs=db.executeNonQuery(sql);			
			}
			
			else if(selectedType.equals("Customer"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select count(distinct EMail) as 'Count' from  Customer where EMail<>'' and IsActive='Y'";
				rs=db.executeNonQuery(sql);
			}
			
			else if(selectedType.equals("Prospective"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select count(*) as 'Count' from  ProspectiveCotact where EMail<>''";
				rs=db.executeNonQuery(sql);
			}
			
			else if(selectedType.equals("Customer & Prospective"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select count(distinct EMail) as 'Count' from  Customer where EMail<>'' and IsActive='Y'";
				rs=db.executeNonQuery(sql);
				while(rs.next())
				{
					count=rs.getInt("Count");
				}
				sql="Select count(*) as 'Count' from  ProspectiveCotact where EMail<>''";
				rs=db.executeNonQuery(sql);
				while(rs.next())
				{
					count+=rs.getInt("Count");
				}
				return count;				
			}
			
			else if(selectedType.equals("Vendor"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select count(*) as 'Count' from  Vendor where EMail<>'' and IsActive='Y' ";
				rs=db.executeNonQuery(sql);
			}
			
			else if(selectedType.equals("Employee"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select count(*) as 'Count' from  empcontact where Details<>'' and contact_id in (622) ";
				rs=db.executeNonQuery(sql);
			}
			
			if(rs!=null)
			{
				while(rs.next())
				{
					count=rs.getInt("Count");
				}
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> getEmailTypeCount", ex);			
		}
		return count;
	}
	
	private List<ContactEmailModel> getEmailTypeList()
	{
		List<ContactEmailModel> lstNewEmails=new ArrayList<ContactEmailModel>();
		ResultSet rs=null;
		String sql="";
		List<String> lstCheckEmails=new ArrayList<String>();	
		ContactEmailModel obj;
		try
		{			
			
			if(selectedType.equals("Bulk"))
			{
			DBHandler db=new DBHandler();	
			sql="Select EmailId,Email from contactemails where unsubscribe=0 ";	
			
			if(selectBulkGroup.equals("@eim.ae"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND (Email LIKE '%emirates.net.ae' OR Email LIKE '%eim.ae') ";	
			}
			
			else if(selectBulkGroup.equals("Gmail , Yahoo"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND (Email LIKE '%@gmail.com' OR Email LIKE '%@yahoo.com' OR Email LIKE '%@hotmail.com') ";	
			}
			else if(selectBulkGroup.equals(".Com"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND Email LIKE '%.com' ";	
			}
			else if(selectBulkGroup.equals("GCC"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND (Email LIKE '%.sa' OR Email LIKE '%.qa' OR Email LIKE '%.ly' OR Email LIKE '%.bh') ";	
			}
			else if(selectBulkGroup.equals("Jordan"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND (Email LIKE '%.jo') ";	
			}
			else if(selectBulkGroup.equals("Auditors - Jordan"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND bulkgroup='Auditors - Jordan'";	
			}
			
			else if(selectBulkGroup.equals("Eygpt"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND (Email LIKE '%.eg') ";	
			}
			else if(selectBulkGroup.equals("Libya"))
			{
				sql="Select EmailId,Email from  contactemails where unsubscribe=0 AND (Email LIKE '%.ly') ";
			}
			
			if(!selectedIndustryGroup.equals("Select Industry"))
			{
			if(!sql.equals(""))
			{				
				sql+=" and industrygroup='" + selectedIndustryGroup+"'";				
			}
			
			}
			
			//sql="Select EmailId,Email from contactemails where unsubscribe=0 ORDER BY RAND() LIMIT " +objBulkEmailModel.getNumberofMails();	
			sql+=" ORDER BY RAND() LIMIT " +objBulkEmailModel.getNumberofMails();
			rs=db.executeNonQuery(sql);			
			}
			
			else if(selectedType.equals("Customer"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select Top " +objBulkEmailModel.getNumberofMails() +  " Cust_Key as 'EmailId' ,Email  from  Customer where EMail<>'' and IsActive='Y' ";
				//sql="Select distinct Top " +objBulkEmailModel.getNumberofMails() +  " Email  from  Customer where EMail<>'' and IsActive='Y' ";
				rs=db.executeNonQuery(sql);
			}
			
			else if(selectedType.equals("Prospective"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select Top " +objBulkEmailModel.getNumberofMails() +  " RecNo as 'EmailId' ,Email  from  ProspectiveCotact where EMail<>'' ORDER BY NEWID()";				
				rs=db.executeNonQuery(sql);
			}
			else if(selectedType.equals("Customer & Prospective"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select Top " +objBulkEmailModel.getNumberofMails() +  " RecNo as 'EmailId' ,Email  from  ProspectiveCotact where EMail<>'' ORDER BY NEWID()";				
				rs=db.executeNonQuery(sql);
				while(rs.next())
				{
					if(!lstCheckEmails.contains(rs.getString("Email")))
					{
					 lstCheckEmails.add(rs.getString("Email"));
					 obj=new ContactEmailModel();					
					 obj.setEmailId(rs.getInt("EmailId"));
					 obj.setEmail(rs.getString("Email").trim());
					 lstNewEmails.add(obj);
					}					
				}
				sql="Select distinct Top " +objBulkEmailModel.getNumberofMails() +  " Cust_Key as 'EmailId' ,Email  from  Customer where EMail<>'' and IsActive='Y' ";
				rs=db.executeNonQuery(sql);
				while(rs.next())
				{
					if(!lstCheckEmails.contains(rs.getString("Email")))
					{
					 lstCheckEmails.add(rs.getString("Email"));
					 obj=new ContactEmailModel();					
					 obj.setEmailId(rs.getInt("EmailId"));
					 obj.setEmail(rs.getString("Email").trim());
					 lstNewEmails.add(obj);
					}					
				}
				return lstNewEmails;				
			}
			
			else if(selectedType.equals("Vendor"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);				
				sql="Select Top " +objBulkEmailModel.getNumberofMails() +  " Vend_Key as 'EmailId' ,Email  from  Vendor where EMail<>'' and IsActive='Y' ORDER BY NEWID()";
				rs=db.executeNonQuery(sql);
			}
			
			else if(selectedType.equals("Employee"))
			{
				SQLDBHandler db=new SQLDBHandler(objSQLDB);
				sql="Select Top " +objBulkEmailModel.getNumberofMails() +  " Emp_Key as 'EmailId' ,Details as 'Email'  from  empcontact where Details<>'' and contact_id in (622) ORDER BY NEWID()";			
				rs=db.executeNonQuery(sql);
			}
						
			while(rs.next())
			{
				if(!lstCheckEmails.contains(rs.getString("Email")))
				{
				 lstCheckEmails.add(rs.getString("Email"));
				 obj=new ContactEmailModel();					
				 obj.setEmailId(rs.getInt("EmailId"));
				 obj.setEmail(rs.getString("Email").trim());
				 lstNewEmails.add(obj);
				}					
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> getEmailTypeList", ex);			
		}
		return lstNewEmails;
	}
	
	@Command
	@NotifyChange("emailCount") 
	public int CountEmailsContainsCommand()
	{
		try
		{
			ResultSet rs=null;
			int count=0;
			String sql="";
			if(true)//(selectedType.equals("Bulk"))
			{
			emailContains=selectBulkGroup;
			
			if(selectBulkGroup.equals("@eim.ae"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND (Email LIKE '%emirates.net.ae' OR Email LIKE '%eim.ae') ";	
			}
			
			else if(selectBulkGroup.equals("Saudi"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND (Email LIKE '%.sa') ";	
			}
			else if(selectBulkGroup.equals("Qatar"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND (Email LIKE '%.qa') ";	
			}			
			
			else if(selectBulkGroup.equals("Gmail , Yahoo"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND (Email LIKE '%@gmail.com' OR Email LIKE '%@yahoo.com' OR Email LIKE '%@hotmail.com') ";	
			}
			else if(selectBulkGroup.equals(".Com"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND Email LIKE '%.com' ";	
			}
			else if(selectBulkGroup.equals("GCC"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND (Email LIKE '%.sa' OR Email LIKE '%.qa' OR Email LIKE '%.ly' OR Email LIKE '%.bh') ";	
			}
			else if(selectBulkGroup.equals("Jordan"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND Email LIKE '%.jo' ";	
			}
			else if(selectBulkGroup.equals("Auditors - Jordan"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND bulkgroup='Auditors - Jordan'";	
			}
			
			else if(selectBulkGroup.equals("Eygpt"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND Email LIKE '%.eg' ";	
			}
			else if(selectBulkGroup.equals("Libya"))
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND Email LIKE '%.ly' ";	
			}
			
			if(!selectedIndustryGroup.equals("Select Industry"))
			{
			if(!sql.equals(""))
			{				
				sql+=" and industrygroup='" + selectedIndustryGroup+"'";				
			}
			else
			{
				sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND industrygroup='" + selectedIndustryGroup+"'";
			}
			
			}
				
			if(!sql.equals(""))
			{
			DBHandler db=new DBHandler();			
			//sql="Select count(*) as 'Count' from  contactemails where unsubscribe=0 AND Email LIKE '%" +emailContains +"%'";			
			rs=db.executeNonQuery(sql);		
			while(rs.next())
			{
				count=rs.getInt("Count");
			}
			}
			
			emailCount=""+count + " Email has " + emailContains;		
			if(!selectedIndustryGroup.equals("Select Industry"))
			{
				emailCount+=" / " + selectedIndustryGroup;
			}
			
			}
			return count;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> CountEmailsContainsCommand", ex);			
		}
		return 0;
	}
	
	public List<ContactEmailModel> getLstEmails() {
		return lstEmails;
	}

	public void setLstEmails(List<ContactEmailModel> lstEmails) {
		this.lstEmails = lstEmails;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public BulkEmailModel getObjBulkEmailModel() {
		return objBulkEmailModel;
	}
	public void setObjBulkEmailModel(BulkEmailModel objBulkEmailModel) {
		this.objBulkEmailModel = objBulkEmailModel;
	}
	public List<BulkEmailModel> getLstBulkEmails() {
		return lstBulkEmails;
	}
	public void setLstBulkEmails(List<BulkEmailModel> lstBulkEmails) {
		this.lstBulkEmails = lstBulkEmails;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public List<String> getSelectionTYpe() {
		return selectionTYpe;
	}
	public void setSelectionTYpe(List<String> selectionTYpe) {
		this.selectionTYpe = selectionTYpe;
	}
	public String getSelectedType() {
		return selectedType;
	}
	
	@NotifyChange({"emailCount","countSelectedEmails","enableBulkGroup"}) 
	public void setSelectedType(String selectedType) 
	{		
		this.selectedType = selectedType;				
		enableBulkGroup=selectedType.equals("Bulk");		
		int count=0;		
		count=getEmailTypeCount();
		countSelectedEmails=count;
		emailCount=""+count + " (Active & has Email)";
	}
	
	public String getEmailCount() {
		return emailCount;
	}
	public void setEmailCount(String emailCount) {
		this.emailCount = emailCount;
	}
	public List<HRListValuesModel> getLstTemplates() {
		return lstTemplates;
	}
	public void setLstTemplates(List<HRListValuesModel> lstTemplates) {
		this.lstTemplates = lstTemplates;
	}
	public HRListValuesModel getSelectedTemplate() {
		return selectedTemplate;
	}
	
	@NotifyChange({"emailBody","objBulkEmailModel"})
	public void setSelectedTemplate(final HRListValuesModel selectedTemplate) 
	{
		this.selectedTemplate = selectedTemplate;
		if(selectedTemplate!=null && selectedTemplate.getListId()>0)
		{
			if(emailBody!=null && !emailBody.equalsIgnoreCase(""))
			{
				Messagebox.show("Do you want to append the the Template data in text area.?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onYes")) 
						{	 	
							//selectedNotesForEachModule=feedBackData.getNotesForModules(selectedTemplate.getListId());
							if(selectedNotesForEachModule!=null)
							{
								emailBody=selectedNotesForEachModule.getMemoEn()+emailBody;
								//lstAtt.addAll(feedBackData.getNotesForModulesAttchamnet(selectedNotesForEachModule.getNoteID()));
							}
							else
							{
								emailBody="";
							}
							objBulkEmailModel.setSubject(selectedTemplate.getEnDescription());							
							//subject=selectedService.getEnDescription();
							BindUtils.postNotifyChange(null, null, ContactEmailViewModel.this, "objBulkEmailModel");
							BindUtils.postNotifyChange(null, null, ContactEmailViewModel.this, "emailBody");
							//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "subject");

						}
						else 
						{		 
							//memo=memo;
							//subject="";
							//lstAtt=lstAtt;
							//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
							//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
							//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "subject");
						}
					}

				});
			}
			
			else
			{
				//selectedNotesForEachModule=feedBackData.getNotesForModules(selectedTemplate.getListId());
				if(selectedNotesForEachModule!=null)
				{
					emailBody=selectedNotesForEachModule.getMemoEn();
					//lstAtt.addAll(feedBackData.getNotesForModulesAttchamnet(selectedNotesForEachModule.getNoteID()));
				}
				else
				{
					emailBody="";

				}
				objBulkEmailModel.setSubject(selectedTemplate.getEnDescription());
				//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
				//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
				//BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "subject");

			}
		}
	}
	public String getEmailBody() {
		return emailBody;
	}
	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}
	public String getEmailContains() {
		return emailContains;
	}
	public void setEmailContains(String emailContains) {
		this.emailContains = emailContains;
	}
	public List<String> getBulkGroups() {
		return bulkGroups;
	}
	public void setBulkGroups(List<String> bulkGroups) {
		this.bulkGroups = bulkGroups;
	}
	public String getSelectBulkGroup() {
		return selectBulkGroup;
	}
		
	@NotifyChange({"emailCount","countSelectedEmails","enableBulkGroup"}) 
	public void setSelectBulkGroup(String selectBulkGroup) 
	{	
		this.selectBulkGroup = selectBulkGroup;
		int count=0;		
		//count=getEmailTypeCount();
		count=CountEmailsContainsCommand();
		countSelectedEmails=count;
		emailCount=""+count + " (Active & has Email)";
	}
	
	public ReminderSettingsModel getSelectedReminderTypes() {
		return selectedReminderTypes;
	}
	public void setSelectedReminderTypes(ReminderSettingsModel selectedReminderTypes) {
		this.selectedReminderTypes = selectedReminderTypes;
	}
	public ListModelList<String> getLstQuotationWeekDays() {
		return lstQuotationWeekDays;
	}
	public void setLstQuotationWeekDays(ListModelList<String> lstQuotationWeekDays) {
		this.lstQuotationWeekDays = lstQuotationWeekDays;
	}
	public Set<String> getSelectedQuotationWeekDays() {
		return selectedQuotationWeekDays;
	}
	public void setSelectedQuotationWeekDays(
			Set<String> selectedQuotationWeekDays) {
		this.selectedQuotationWeekDays = selectedQuotationWeekDays;
	}
	public ListModelList<String> getLstQuotationMonths() {
		return lstQuotationMonths;
	}
	public void setLstQuotationMonths(ListModelList<String> lstQuotationMonths) {
		this.lstQuotationMonths = lstQuotationMonths;
	}
	public Set<String> getSelectedQuotationMonths() {
		return selectedQuotationMonths;
	}
	public void setSelectedQuotationMonths(Set<String> selectedQuotationMonths) {
		this.selectedQuotationMonths = selectedQuotationMonths;
	}
	public ListModelList<String> getLstQuotationDays() {
		return lstQuotationDays;
	}
	public void setLstQuotationDays(ListModelList<String> lstQuotationDays) {
		this.lstQuotationDays = lstQuotationDays;
	}
	public Set<String> getSelectedQuotationDays() {
		return selectedQuotationDays;
	}
	public void setSelectedQuotationDays(Set<String> selectedQuotationDays) {
		this.selectedQuotationDays = selectedQuotationDays;
	}

	public boolean isWithReminder() {
		return withReminder;
	}

	public void setWithReminder(boolean withReminder) {
		this.withReminder = withReminder;
	}

	public int getCountSelectedEmails() {
		return countSelectedEmails;
	}

	public void setCountSelectedEmails(int countSelectedEmails) {
		this.countSelectedEmails = countSelectedEmails;
	}

	public String getBulkmEails() {
		return bulkmEails;
	}

	public void setBulkmEails(String bulkmEails) {
		this.bulkmEails = bulkmEails;
	}

	public List<String> getLstEmailStatus() {
		return lstEmailStatus;
	}

	public void setLstEmailStatus(List<String> lstEmailStatus) {
		this.lstEmailStatus = lstEmailStatus;
	}

	public String getSelectedEmailStatus() {
		return selectedEmailStatus;
	}

	public void setSelectedEmailStatus(String selectedEmailStatus) {
		this.selectedEmailStatus = selectedEmailStatus;
	}

	public List<String> getIndustryGroups() {
		return industryGroups;
	}

	public void setIndustryGroups(List<String> industryGroups) {
		this.industryGroups = industryGroups;
	}

	public String getSelectedIndustryGroup() {
		return selectedIndustryGroup;
	}

	public void setSelectedIndustryGroup(String selectedIndustryGroup) {
		this.selectedIndustryGroup = selectedIndustryGroup;
	}
	public boolean isEnableBulkGroup() {
		return enableBulkGroup;
	}
	public void setEnableBulkGroup(boolean enableBulkGroup) {
		this.enableBulkGroup = enableBulkGroup;
	}
}
