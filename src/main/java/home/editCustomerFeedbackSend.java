package home;

import hba.HBAQueries;
import hba.ProspectiveData;
import hba.TaskData;
import hba.VendorsData;
import hr.HRData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import layout.MenuModel;
import model.CompanyDBModel;
import model.CustomerContact;
import model.CustomerFeedbackModel;
import model.CustomerModel;
import model.EmailSelectionModel;
import model.EmailSignatureModel;
import model.EmployeeModel;
import model.FeedbackSendSources;
import model.HRListValuesModel;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;
import model.ReminderSettingsModel;
import model.SerialFields;
import model.VendorModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import timesheet.TimeSheetData;
import admin.TasksModel;
import company.ReminderData;
import db.DBHandler;

public class editCustomerFeedbackSend {
	private Logger logger = Logger.getLogger(this.getClass());

	CustomerFeedBackData feedBackData=null;
	ReminderData data=new ReminderData();
	HRData hrData=new HRData();
	TaskData taskData=new TaskData();
	VendorsData vendorsData=new VendorsData();
	ProspectiveData prospData = new ProspectiveData();

	TimeSheetData sheetData=new TimeSheetData();


	TasksModel taskDeatils=new TasksModel();


	private String attFile4;

	private List<QuotationAttachmentModel> lstAtt=new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets=new QuotationAttachmentModel();

	private CustomerFeedbackModel selectedCustomerFeedBack=new CustomerFeedbackModel();

	private CustomerFeedbackModel selectedCustomerFeedBackTemp=new CustomerFeedbackModel();

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	DecimalFormat dcf=new DecimalFormat("0.00");

	int feedBackKey=0;

	private String memo="";

	private Date feedbackCreateDate;

	private Date feedBackModifiedDate;

	private String feedBackNumber="";

	private String subject="";


	private int webuserID=0;
	private String webuserName="";

	WebusersModel dbUser=null;

	private int supervisorID=0;

	private int employeeKey=0;

	private boolean adminUser;

	private MenuModel companyRole;

	private boolean canSave;

	Set<String> selectedTo=new HashSet<String>();

	Set<String> listTo=new HashSet<String>();


	Set<String> selectedCc=new HashSet<String>();

	Set<String> cCList=new HashSet<String>();


	Set<String> selectedBcc=new HashSet<String>();

	Set<String> bccList=new HashSet<String>();



	List<CustomerModel> tempCustomerList=new ArrayList<CustomerModel>();
	List<VendorModel> tempVendorList=new ArrayList<VendorModel>();
	List<EmployeeModel> tempEmployeeList=new ArrayList<EmployeeModel>();
	List<ProspectiveModel> tempProsepctiveList=new ArrayList<ProspectiveModel>();

	List<CustomerFeedbackModel> listSelectedCustomers=new ArrayList<CustomerFeedbackModel>();

	private List<HRListValuesModel> lstService;

	private HRListValuesModel selectedService;

	private CustomerFeedbackModel selectedNotesForEachModule=new CustomerFeedbackModel();

	private List<TasksModel> lstCustomerTaks;

	private TasksModel selectedTask;

	private boolean sortByName=false;

	private boolean sortByNumber=false;

	List<String> selectionTYpe=new ArrayList<String>();

	String selectedType="";

	private ReminderSettingsModel sendMailReminder=new ReminderSettingsModel();

	//Mail Reminder 
	private ListModelList<String> lstMailMonths;
	private Set<String> selectedMailMonths;
	private ListModelList<String> lstMailDays;
	private Set<String> selectedMailDays;	
	private ListModelList<String> lstMailWeekDays;
	private Set<String> selectedMailWeekDays;


	private boolean showCc=false;

	private boolean showBcc=false;

	private String loggersEmail="";

	List<String> notValidemailAdress=new ArrayList<String>();	

	private boolean remiderSaveVisible=false;

	private int mailReminderKey=0;

	EmailSignatureModel emailSignatureModel = new EmailSignatureModel();

	//For customer model

	private List<EmailSelectionModel> tempEmailSelctionPopUpList=new ArrayList<EmailSelectionModel>();

	private List<FeedbackSendSources> selectedEmailSources=new ArrayList<FeedbackSendSources>();
	private Set<FeedbackSendSources> tempSelectedEmailSources=new HashSet<FeedbackSendSources>();

	private boolean visibleSend=true;
	EmailSelectionModel temEmailpopUpModel;
	
	
	private boolean appendFalg=true;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public editCustomerFeedbackSend()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
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
			feedBackData=new CustomerFeedBackData(obj);
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/customerFeedBackSendDialog");

			if(dbUser!=null)
			{
				if(dbUser.getCompanyid()==1)//hide send button from demo sample 
					visibleSend=false;
				adminUser=dbUser.getFirstname().equals("admin");

				if(adminUser)
				{
					webuserID=0;
					webuserName="Admin";
				}
				else
				{
					webuserID=dbUser.getUserid();
					webuserName=dbUser.getUsername();
				}
			}

			supervisorID=dbUser.getSupervisor();//logged in as supervisor
			employeeKey=dbUser.getEmployeeKey();//logged in as employee
			if(employeeKey>0)
				supervisorID=employeeKey;

			if(supervisorID>0)
				webuserID=supervisorID;

			if(dbUser!=null && !dbUser.getUseremail().equalsIgnoreCase(""))
				loggersEmail=dbUser.getUseremail();

			if(win!=null)
			{

				feedBackKey=(Integer)map.get("feedBackKey");

				if(type.equalsIgnoreCase("edit"))
				{
					canSave=true;
					win.setTitle("Edit E-mail");
				}
				else
				{
					canSave=false;
					win.setTitle("View E-mail");
				}

				if(type.equalsIgnoreCase("add") || type.equalsIgnoreCase("OtherForms"))
				{
					canSave=true;
					win.setTitle("New E-mail");
				}

			}

			attFile4="No file chosen";

			selectionTYpe.add("Select E-mails");
			selectionTYpe.add("Customer");
			selectionTYpe.add("Prospective");
			selectionTYpe.add("Employee");
			selectionTYpe.add("Vendor");
			selectedType=selectionTYpe.get(0);


			selectedMailMonths=new HashSet<String>();
			selectedMailDays=new HashSet<String>();
			selectedMailWeekDays=new HashSet<String>();

			fillMonthList();



			sendMailReminder=data.getCompanyMailReminder(dbUser.getCompanyid(), "SendEMail",feedBackKey);
			if(sendMailReminder.getMailId()==0)
			{
				//i dont know why to add if new email will add ..jus to bring the default values !!
				//sendMailReminder=data.getCompanyMailReminder(dbUser.getCompanyid(), "SendEMail",0);
				Calendar c = Calendar.getInstance();		
				sendMailReminder.setStartdate(df.parse(sdf.format(c.getTime())));
				sendMailReminder.setExpireddate(df.parse(sdf.format(c.getTime())));
				sendMailReminder.setRemindertime(df.parse(sdf.format(c.getTime())));				
			}
			mailReminderKey=sendMailReminder.getMailId();//get email id to decide save or update while saving reminder setting 
			if(sendMailReminder.getWeekly()!=null && !sendMailReminder.getWeekly().equals("")  )
			{
				List<String> weeklyList = new ArrayList<String>(Arrays.asList(sendMailReminder.getWeekly().split(",")));
				for (String item : weeklyList)
				{
					selectedMailWeekDays.add(item);	
				}							
			}	
			if(sendMailReminder.getMonthly()!=null && !sendMailReminder.getMonthly().equals(""))
			{
				List<String> monthList = new ArrayList<String>(Arrays.asList(sendMailReminder.getMonthly().split(",")));
				for (String item : monthList)
				{
					selectedMailMonths.add(item);	
				}							
			}
			if(sendMailReminder.getMonthlydays()!=null && !sendMailReminder.getMonthlydays().equals(""))
			{
				List<String> monthDaysList = new ArrayList<String>(Arrays.asList(sendMailReminder.getMonthlydays().split(",")));
				for (String item : monthDaysList)
				{
					selectedMailDays.add(item);	
				}							
			}

			tempCustomerList=feedBackData.getCustomerList("");
			tempVendorList=vendorsData.getVendorList("");
			tempEmployeeList=hrData.getEmpMastList();
			tempProsepctiveList=prospData.getProspectiveSearchRes("",0);
			for(CustomerModel customerModel:tempCustomerList)
			{
				if(customerModel.getEmail()!=null && !customerModel.getEmail().equalsIgnoreCase(""))
				{
					bccList.add(customerModel.getEmail());
					cCList.add(customerModel.getEmail());
					listTo.add(customerModel.getEmail());
				}
				if(customerModel.getcC()!=null && !customerModel.getcC().equalsIgnoreCase("null") && !customerModel.getcC().equalsIgnoreCase(""))
				{
					bccList.add(customerModel.getcC());
					cCList.add(customerModel.getcC());
					listTo.add(customerModel.getcC());
				}
				if(customerModel.getCustomerContacts()!=null)
				{
					for(CustomerContact customerContact:customerModel.getCustomerContacts())
					{
						if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase("null") && !customerContact.getEmail().equalsIgnoreCase(""))
						{
							bccList.add(customerContact.getEmail());
							cCList.add(customerContact.getEmail());
							listTo.add(customerContact.getEmail());
						}
					}
				}
			}


			for(ProspectiveModel contactDetailsModel:tempProsepctiveList)
			{
				if(contactDetailsModel.getEmail()!=null && !contactDetailsModel.getEmail().equalsIgnoreCase(""))
				{
					bccList.add(contactDetailsModel.getEmail());
					cCList.add(contactDetailsModel.getEmail());
					listTo.add(contactDetailsModel.getEmail());
				}
				if(contactDetailsModel.getcC()!=null && !contactDetailsModel.getcC().equalsIgnoreCase(""))
				{
					bccList.add(contactDetailsModel.getcC());
					cCList.add(contactDetailsModel.getcC());
					listTo.add(contactDetailsModel.getcC());
				}
				if(contactDetailsModel.getProspectiveContact()!=null)
				{
					for(ProspectiveContactDetailsModel customerContact:contactDetailsModel.getProspectiveContact())
					{
						if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase(""))
						{
							bccList.add(customerContact.getEmail());
							cCList.add(customerContact.getEmail());
							listTo.add(customerContact.getEmail());
						}
					}
				}

			}


			for(VendorModel vendorModel:tempVendorList)
			{
				if(vendorModel.getEmail()!=null && !vendorModel.getEmail().equalsIgnoreCase(""))
				{
					bccList.add(vendorModel.getEmail());
					cCList.add(vendorModel.getEmail());
					listTo.add(vendorModel.getEmail());
				}
				if(vendorModel.getcC()!=null && !vendorModel.getcC().equalsIgnoreCase(""))
				{
					bccList.add(vendorModel.getcC());
					cCList.add(vendorModel.getcC());
					listTo.add(vendorModel.getcC());
				}

			}

			for(EmployeeModel employeeModel:tempEmployeeList)
			{
				if(employeeModel.getEmail()!=null && !employeeModel.getEmail().equalsIgnoreCase(""))
				{
					bccList.add(employeeModel.getEmail());
					cCList.add(employeeModel.getEmail());
					listTo.add(employeeModel.getEmail());
				}

			}

			lstService=feedBackData.getHRListValuesForFeedBack(147,"Select");      
			if(lstService!=null && lstService.size()>0)
				selectedService=lstService.get(0);


			lstCustomerTaks=sheetData.getCustomerTasks(webuserID,"Number");
			if(lstCustomerTaks!=null && lstCustomerTaks.size()>0)
				selectedTask=lstCustomerTaks.get(0);

			if(feedBackKey>0)
			{
				selectedCustomerFeedBack=feedBackData.getCustomerFeedBackSendById(feedBackKey);
				if(selectedCustomerFeedBack!=null)
				{
					memo=selectedCustomerFeedBack.getMemo();
					feedBackNumber=selectedCustomerFeedBack.getFeedbackNUmber().trim();
					subject=selectedCustomerFeedBack.getSubject();
					listSelectedCustomers=feedBackData.getFeedBackSentCusomersDetails(feedBackKey);//selected Customer in CustomerFeedbackModel
					for(CustomerFeedbackModel model : listSelectedCustomers)
					{
						if(model!=null && model.getEmailType().equalsIgnoreCase("BCC"))
						{
							bccList.add(model.getEmail());
							selectedBcc.add(model.getEmail());
							showBcc=true;
						}
						else if (model!=null && model.getEmailType().equalsIgnoreCase("CC"))
						{
							cCList.add(model.getEmail());
							selectedCc.add(model.getEmail());
							showCc=true;
						}
						else if (model!=null && model.getEmailType().equalsIgnoreCase("TO"))
						{
							listTo.add(model.getEmail());
							selectedTo.add(model.getEmail());
						}
						else 
						{
							bccList.add(model.getEmail());
							selectedBcc.add(model.getEmail());
						}
					}

					for(HRListValuesModel hrListValuesModel:lstService)
					{
						if(hrListValuesModel.getListId()==selectedCustomerFeedBack.getServiceRefKey())
						{
							selectedService=hrListValuesModel;
							break;
						}

					}
					for(TasksModel model:lstCustomerTaks)
					{
						if(model.getTaskid()==selectedCustomerFeedBack.getTaskID())
						{
							selectedTask=model;
							break;
						}

					}

					lstAtt=feedBackData.getFeedbacksendAttchamnet(feedBackKey);
				}

			}
			else
			{
				memo="";
				emailSignatureModel = feedBackData.getEmailSignature(webuserID);

				if (emailSignatureModel != null	&& emailSignatureModel.getSignature()!=null && !emailSignatureModel.getSignature().equalsIgnoreCase("")) {
					memo=emailSignatureModel.getSignature();
				} else {
					memo = "";
				}
				feedBackNumber=feedBackData.GetSaleNumber(SerialFields.feedBackSend.toString());
			}


			if(type!=null && type.equalsIgnoreCase("FromTask"))
			{
				int taskID=(Integer)map.get("taskId");
				for(TasksModel model:lstCustomerTaks)
				{
					if(model.getTaskid()==taskID)
					{
						appendFalg=false;
						setSelectedTask(model);
						break;
					}

				}
			}

			if(type!=null && type.equalsIgnoreCase("OtherForms"))
			{
				int id=(Integer)map.get("id");
				String formType=(String)map.get("formType");
				if(null!=map.get("lstAtt"))
				{
					lstAtt=(List<QuotationAttachmentModel>)map.get("lstAtt");
				}
				if(formType.equalsIgnoreCase("Customer"))
				{
					for(CustomerModel customerModel:tempCustomerList)
					{
						if(customerModel.getCustkey()==id)
						{
							if(customerModel.getEmail()!=null && !customerModel.getEmail().equalsIgnoreCase(""))
							{
								selectedTo.add(customerModel.getEmail());
							}
							if(customerModel.getcC()!=null && !customerModel.getcC().equalsIgnoreCase(""))
							{
								selectedCc.add(customerModel.getcC());
							}
							if(customerModel.getCustomerContacts()!=null)
							{
								for(CustomerContact customerContact:customerModel.getCustomerContacts())
								{
									if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase(""))
									{
										selectedCc.add(customerContact.getEmail());
									}
								}
							}
							break;
						}
					}
				}
				else if(formType.equalsIgnoreCase("Prospective"))
				{
					for(ProspectiveModel contactDetailsModel:tempProsepctiveList)
					{
						if(contactDetailsModel.getRecNo()==id)
						{
							if(contactDetailsModel.getEmail()!=null && !contactDetailsModel.getEmail().equalsIgnoreCase(""))
							{
								selectedTo.add(contactDetailsModel.getEmail());
							}

							if(contactDetailsModel.getcC()!=null && !contactDetailsModel.getcC().equalsIgnoreCase(""))
							{
								selectedCc.add(contactDetailsModel.getcC());
							}
							if(contactDetailsModel.getProspectiveContact()!=null)
							{
								for(ProspectiveContactDetailsModel customerContact:contactDetailsModel.getProspectiveContact())
								{
									if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase(""))
									{
										selectedCc.add(customerContact.getEmail());
									}
								}	
							}
							break;
						}

					}
				}

			}

			if(feedBackKey>0)
			{
				remiderSaveVisible=true;
			}
			else
			{
				remiderSaveVisible=false;
			}

		}
		catch(Exception e)
		{
			logger.error("ERROR in editCustomerFeedbackSend ----> init", e);			
		}

	}


	private void fillMonthList()
	{
		lstMailWeekDays=new ListModelList<String>();
		lstMailWeekDays.add("Saturday");
		lstMailWeekDays.add("Sunday");
		lstMailWeekDays.add("Monday");
		lstMailWeekDays.add("Tuesday");
		lstMailWeekDays.add("Wednesday");
		lstMailWeekDays.add("Thursday");
		lstMailWeekDays.add("Friday");

		lstMailMonths=new ListModelList<String>();		
		lstMailMonths.add("January");
		lstMailMonths.add("February");
		lstMailMonths.add("March");
		lstMailMonths.add("April");
		lstMailMonths.add("May");
		lstMailMonths.add("June");
		lstMailMonths.add("July");
		lstMailMonths.add("August");
		lstMailMonths.add("September");
		lstMailMonths.add("October");
		lstMailMonths.add("November");
		lstMailMonths.add("December");

		lstMailDays=new ListModelList<String>();
		for (int i = 1; i < 32; i++) 
		{
			lstMailDays.add(i+"");
		}
		lstMailDays.add("Last");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"remiderSaveVisible"})
	public void saveCustomerFeedbackSend(@BindingParam("cmp") final Window x,@BindingParam("draft") boolean draft)
	{
		try {

			if((selectedBcc==null || selectedBcc.size()==0) && (selectedTo==null || selectedTo.size()==0))
			{
				Messagebox.show("To cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(subject!=null && subject.equalsIgnoreCase(""))
			{
				Messagebox.show("Mail subject cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(memo!=null && memo.equalsIgnoreCase(""))
			{
				Messagebox.show("memo cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}


			CustomerFeedbackModel feedbackModel=new CustomerFeedbackModel();
			int result=0;

			if(feedBackKey>0)
			{
				feedbackModel.setFeedbackKey(feedBackKey);
			}
			else
			{
				feedbackModel.setFeedbackKey(feedBackData.getMaxID("FeedbackSend", "feedbackId"));
				memo=memo+"<p><em><span style='font-size:16px'><span style='color:#FF0000'>This email has been sent from hinawi e-mail system&nbsp;</span><a href='www.hinawi.com'><span style='color:#FF0000'>www.hinawi.com</span></a></span></em></p>";
			}
			selectedEmailSources=new ArrayList<FeedbackSendSources>();
			sourcePreporulation(selectedBcc);
			sourcePreporulation(selectedCc);
			sourcePreporulation(selectedTo);
			tempSelectedEmailSources=new HashSet<FeedbackSendSources>();
			tempSelectedEmailSources.addAll(selectedEmailSources);
			
			if((feedBackNumber!=null) && (feedBackData.checkIfFeedBackSendNumberIsDuplicate(feedBackNumber,feedbackModel.getFeedbackKey())==true))
			{
				feedBackNumber=feedBackData.GetSaleNumber(SerialFields.feedBackSend.toString());
			}

			feedbackModel.setFeedbackNUmber(feedBackNumber.trim());


			feedbackModel.setMemo(memo);

			if(loggersEmail!=null)
				feedbackModel.setSentFromEmail(loggersEmail);
			else
				feedbackModel.setSentFromEmail("");

			feedbackModel.setWebuserID(webuserID);

			Calendar c = Calendar.getInstance();		

			feedbackModel.setFeedBackModifiedDate(df.parse(sdf.format(c.getTime())));

			feedbackModel.setFeedbackCreateDate(df.parse(sdf.format(c.getTime())));

			feedbackModel.setSelectedBcc(selectedBcc);

			feedbackModel.setSelectedCcs(selectedCc);

			feedbackModel.setSelectedTo(selectedTo);

			if(selectedTask!=null && selectedTask.getTaskid()>0)
				feedbackModel.setTaskID(selectedTask.getTaskid());
			else
				feedbackModel.setTaskID(0);

			if(selectedService!=null && selectedService.getListId()>0)
				feedbackModel.setServiceRefKey(selectedService.getListId());
			else
				feedbackModel.setServiceRefKey(0);  

			feedbackModel.setSubject(subject);

			if(draft)
			{
				feedbackModel.setIsDrafted("Y");
				feedbackModel.setIsSent("N");
			}
			else
			{
				feedbackModel.setIsSent("Y");
				feedbackModel.setIsDrafted("N");
			}

			if(feedBackKey>0)
			{

				result=feedBackData.editCustomerFeedBackSentData(feedbackModel,lstAtt,tempSelectedEmailSources,webuserID,webuserName);
				if(draft)
					Clients.showNotification("E-mail Has Been Saved Sucessfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				else
					Clients.showNotification("E-mail Has Been Sent Sucessfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			else
			{
				result=feedBackData.saveCustomerFeedbackSentData(feedbackModel,lstAtt,tempSelectedEmailSources);
				if(draft)
					Clients.showNotification("E-mail Has Been Saved Sucessfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				else
					Clients.showNotification("E-mail Has Been Sent Sucessfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			if(result>0)
			{
				if(feedBackKey==0)
				{
					feedBackData.ConfigSerialNumberCashInvoice(SerialFields.feedBackSend, feedBackNumber,0);
					remiderSaveVisible=true;
				}
				StringBuffer comaSepartedCcEmails=new StringBuffer();
				StringBuffer comaSepartedToEmails=new StringBuffer();
				StringBuffer comaSepartedBccEmails=new StringBuffer();
				if(selectedTo!=null && selectedTo.size()>0)
				{

					for(String customerModel:selectedTo)
					{
						if(customerModel!=null && !customerModel.equalsIgnoreCase(""))
						{
							logger.info("cut email>>>" +  customerModel);
							comaSepartedToEmails.append(customerModel);
							if(selectedTo.size()>1)
								comaSepartedToEmails.append(",");

						}

					}

				}

				if(selectedCc!=null && selectedCc.size()>0)
				{

					for(String customerModel:selectedCc)
					{
						if(customerModel!=null && !customerModel.equalsIgnoreCase(""))
						{
							logger.info("cut email>>>" +  customerModel);
							comaSepartedCcEmails.append(customerModel);
							if(selectedCc.size()>1)
								comaSepartedCcEmails.append(",");

						}

					}

				}


				if(selectedBcc!=null && selectedBcc.size()>0)
				{

					for(String customerModel:selectedBcc)
					{
						if(customerModel!=null && !customerModel.equalsIgnoreCase(""))
						{
							logger.info("cut email>>>" +  customerModel);
							comaSepartedBccEmails.append(customerModel);
							if(selectedBcc.size()>1)
								comaSepartedBccEmails.append(",");

						}

					}

				}
				if(!draft)
				{
					sendClientEmail(comaSepartedToEmails.toString(),comaSepartedCcEmails.toString(),comaSepartedBccEmails.toString());
				}
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentFeedBackSentForm", args);
				feedBackKey=feedbackModel.getFeedbackKey();
			}
			if(!draft)
			{

				Messagebox.show("Do you want to make reminder for the E-mail sent ?","Reminder", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
					@SuppressWarnings({ "unused" })
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onYes")) 
						{	 	
							Map args = new HashMap();
						}
						else 
						{		 
							Map args = new HashMap();
							x.detach();
						}
					}

				});




			}

		} catch (Exception e) {
			logger.error("ERROR in editCustomerFeedbackSend ----> saveCustomerFeedbackSend", e);	
		}
		/*  x.detach();
		 Map args = new HashMap();
		 BindUtils.postGlobalCommand(null, null, "refreshParentTaskForm", args);*/




	}


	@Command
	public void addTaskCustomers()
	{
		try
		{
			if(selectedType.equalsIgnoreCase("Customer"))
			{
				Map<String,Object> arg = new HashMap<String,Object>();
				arg.put("custKey", 0);
				arg.put("compKey",0);
				arg.put("type","add");
				arg.put("allowSelect","true");
				Executions.createComponents("/hba/list/listOfCustomersforTask.zul", null,arg);
			}
			else if(selectedType.equalsIgnoreCase("Prospective"))
			{
				Map<String,Object> arg = new HashMap<String,Object>();
				arg.put("custKey", 0);
				arg.put("compKey",0);
				arg.put("type","add");
				arg.put("allowSelect","true");
				Executions.createComponents("/crm/listProspectiveForSendEmail.zul", null,arg);
			}
			else if(selectedType.equalsIgnoreCase("Employee"))
			{

				Map<String,Object> arg = new HashMap<String,Object>();
				arg.put("custKey", 0);
				arg.put("compKey",0);
				arg.put("type","add");
				arg.put("allowSelect","true");
				Executions.createComponents("/crm/listOfEmployeesForSendEmail.zul", null,arg);

			}
			else if(selectedType.equalsIgnoreCase("Vendor"))
			{
				Map<String,Object> arg = new HashMap<String,Object>();
				arg.put("custKey", 0);
				arg.put("compKey",0);
				arg.put("type","add");
				arg.put("allowSelect","true");
				Executions.createComponents("/crm/listVendorList.zul", null,arg);
			}
			else 
			{
				Clients.showNotification("Under Implimentation.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> addTaskCustomers", ex);			
		}
	}


	@Command
	public void addTaskProspective()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("custKey", 0);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/hba/list/listOfCustomersforTask.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> addTaskCustomers", ex);			
		}
	}


	@Command
	@NotifyChange({"sortByNumber","sortByName"})
	public void sortByNumber()
	{
		try
		{
			if(sortByNumber)
			{
				sortByName=false;
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> sortByNumber", ex);			
		}
	}

	@Command
	@NotifyChange({"sortByNumber","sortByName","lstCustomerTaks"})
	public void sortByName()
	{
		try
		{
			if(sortByName)
			{
				lstCustomerTaks=sheetData.getCustomerTasks(webuserID,"Name");
				sortByNumber=false;
			}
			else
			{
				lstCustomerTaks=sheetData.getCustomerTasks(webuserID,"Number");
			}

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> sortByName", ex);			
		}
	}




	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc"})
	public void getCutomerIDsForFeedbackSendTocustomer(@BindingParam("myData")String custKeys)
	{		
		try
		{
			boolean tempMessage=false; 
			int i=0;
			String reg=",";
			String[] tokens = custKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(CustomerModel model:tempCustomerList)
				{
					if(model.getCustkey()==Integer.parseInt(tokens[i]))
					{

						if(model.getEmail()!=null && !model.getEmail().equalsIgnoreCase(""))
						{	
							if(isValidEmailAddress(model.getEmail()))
							{
								//selectedBcc.add(model.getEmail());
							}
							if(model.getcC()!=null && !model.getcC().equalsIgnoreCase(""))
								if(isValidEmailAddress(model.getcC()))
								{
									//selectedBcc.add(model.getcC());
									temEmailpopUpModel=new EmailSelectionModel();
									temEmailpopUpModel.setEmail(model.getcC());
									temEmailpopUpModel.setCc(true);
									tempEmailSelctionPopUpList.add(temEmailpopUpModel);
								}
							if(model.getCustomerContacts()!=null)
							{
								for(CustomerContact customerContact:model.getCustomerContacts())
								{
									if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase(""))
									{
										if(isValidEmailAddress(customerContact.getEmail()))
										{
											//selectedBcc.add(customerContact.getEmail());
											temEmailpopUpModel=new EmailSelectionModel();
											temEmailpopUpModel.setEmail(customerContact.getEmail());
											if(customerContact.getDefaultFlag().equalsIgnoreCase("Y"))
											{
												temEmailpopUpModel.setTo(true);
											}
											else 
											{
												temEmailpopUpModel.setCc(true);
											}
											tempEmailSelctionPopUpList.add(temEmailpopUpModel);
										}
									}
								}
							}
							break;
						}
						else
						{
							tempMessage=true;
						}

					}
				}

			}
			if(tempMessage)
			{
				Clients.showNotification("Some Customers with no e-mail address have been ignored from list. ",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("listOfemails", tempEmailSelctionPopUpList);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/crm/customerEmailSelection.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> getCutomerIDsForFeedbackSendTocustomer", ex);			
		}
	}



	@SuppressWarnings("unchecked")
	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc","selectedCc","showCc","selectedTo"})
	public void getPopUpEmailList(@SuppressWarnings("rawtypes") @BindingParam("selectedEmailSelectionPopUp")List selectedEmailSelectionPopUp)
	{		
		try
		{
			List<EmailSelectionModel> tempemailSelectionPopUpList = new ArrayList<EmailSelectionModel>();
			tempemailSelectionPopUpList=selectedEmailSelectionPopUp;
			for(EmailSelectionModel model:tempemailSelectionPopUpList)
			{
				if(model.isBcc())
				{
					selectedBcc.add(model.getEmail());
					showBcc=true;
				}
				if(model.isCc())
				{
					selectedCc.add(model.getEmail());
					showCc=true;
				}
				if (model.isTo())
				{
					selectedTo.add(model.getEmail());
				}
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> getPopUpEmailList", ex);			
		}
	}



	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc"})
	public void getVendorIDsForSendEmail(@BindingParam("myData")String vendKeys)
	{		
		try
		{
			boolean tempMessage=false; 
			int i=0;
			String reg=",";
			String[] tokens = vendKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(VendorModel model:tempVendorList)
				{
					if(model.getVend_Key()==Integer.parseInt(tokens[i]))
					{

						if(model.getEmail()!=null && !model.getEmail().equalsIgnoreCase(""))
						{		
							if(isValidEmailAddress(model.getEmail()))
							{
								//selectedBcc.add(model.getEmail());
								temEmailpopUpModel=new EmailSelectionModel();
								temEmailpopUpModel.setEmail(model.getEmail());
								temEmailpopUpModel.setTo(true);
								tempEmailSelctionPopUpList.add(temEmailpopUpModel);
							}
							if(model.getcC()!=null && !model.getcC().equalsIgnoreCase(""))
								if(isValidEmailAddress(model.getcC()))
								{
									//selectedBcc.add(model.getcC());	 
									temEmailpopUpModel=new EmailSelectionModel();
									temEmailpopUpModel.setEmail(model.getcC());
									temEmailpopUpModel.setCc(true);
									tempEmailSelctionPopUpList.add(temEmailpopUpModel);
									break;
								}
						}
						else
						{
							tempMessage=true;
						}

					}
				}

			}
			if(tempMessage)
			{
				Clients.showNotification("Some Vendors with no e-mail address have been ignored from list. ",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			//showBcc=true;
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("listOfemails", tempEmailSelctionPopUpList);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/crm/customerEmailSelection.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> getVendorIDsForSendEmail", ex);			
		}
	}


	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc"})
	public void getProspectiveIDsForSendEmail(@BindingParam("myData")String prospKeys)
	{		
		try
		{
			boolean tempMessage=false; 
			int i=0;
			String reg=",";
			String[] tokens = prospKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(ProspectiveModel model:tempProsepctiveList)
				{
					if(model.getRecNo()==Integer.parseInt(tokens[i]))
					{

						if(model.getEmail()!=null && !model.getEmail().equalsIgnoreCase(""))
						{	


							if(isValidEmailAddress(model.getEmail()))
							{
								//selectedBcc.add(model.getEmail());
								temEmailpopUpModel=new EmailSelectionModel();
								temEmailpopUpModel.setEmail(model.getEmail());
								temEmailpopUpModel.setTo(true);
								tempEmailSelctionPopUpList.add(temEmailpopUpModel);
							}
							if(model.getcC()!=null && !model.getcC().equalsIgnoreCase(""))
								if(isValidEmailAddress(model.getcC()))
								{
									//selectedBcc.add(model.getcC());
									temEmailpopUpModel=new EmailSelectionModel();
									temEmailpopUpModel.setEmail(model.getcC());
									temEmailpopUpModel.setCc(true);
									tempEmailSelctionPopUpList.add(temEmailpopUpModel);
								}
							if(model.getProspectiveContact()!=null)
							{
								for(ProspectiveContactDetailsModel customerContact:model.getProspectiveContact())
								{
									if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase(""))
									{
										if(isValidEmailAddress(customerContact.getEmail()))
										{
											//selectedBcc.add(customerContact.getEmail());
											temEmailpopUpModel=new EmailSelectionModel();

											if(customerContact.getDefaultFlag().equalsIgnoreCase("N"))
											{
												temEmailpopUpModel.setEmail(customerContact.getEmail());
												temEmailpopUpModel.setCc(true);
												tempEmailSelctionPopUpList.add(temEmailpopUpModel);
											}

										}
									}
								}
							}
							break;
						}
						else
						{
							tempMessage=true;
						}

					}
				}

			}
			if(tempMessage)
			{
				Clients.showNotification("Some Prospective with no e-mail address have been ignored from list. ",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			//showBcc=true;
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("listOfemails", tempEmailSelctionPopUpList);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/crm/customerEmailSelection.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> getProspectiveIDsForSendEmail", ex);			
		}
	}


	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc"})
	public void getEmployeeIDsForSendEmail(@BindingParam("myData")String empKeys)
	{		
		try
		{

			boolean tempMessage=false; 
			int i=0;
			String reg=",";
			String[] tokens = empKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(EmployeeModel model:tempEmployeeList)
				{
					if(model.getEmployeeKey()==Integer.parseInt(tokens[i]))
					{

						if(model.getEmail()!=null && !model.getEmail().equalsIgnoreCase(""))
						{		
							if(isValidEmailAddress(model.getEmail()))
							{
								//selectedBcc.add(model.getEmail());
								temEmailpopUpModel=new EmailSelectionModel();
								temEmailpopUpModel.setEmail(model.getEmail());
								temEmailpopUpModel.setTo(true);
								tempEmailSelctionPopUpList.add(temEmailpopUpModel);
								break;
							}
						}
						else
						{
							tempMessage=true;
						}

					}
				}

			}
			if(tempMessage)
			{
				Clients.showNotification("Some Employees with no e-mail address have been ignored from list. ",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			//showBcc=true;
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("listOfemails", tempEmailSelctionPopUpList);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/crm/customerEmailSelection.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in editCustomerFeedbackSend ----> getEmployeeIDsForSendEmail", ex);			
		}
	}


	@SuppressWarnings("unused")
	@Command 
	@NotifyChange({"attFile4","lstAtt"})
	public void uploadFile(BindContext ctx,@BindingParam("attId") String attId )
	{
		try {
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
			if(lstAtt!=null && lstAtt.size()>=10)
			{
				Clients.showNotification("The you can upload maximum 10 images per task.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;

			}

			for(QuotationAttachmentModel attachmentModel:lstAtt)
			{
				if(attachmentModel.getFilename().equalsIgnoreCase(event.getMedia().getName()))
				{
					Clients.showNotification("The file already uploaded please select another file.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					return;
				}

			}

			String filePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator;
			Session sess = Sessions.getCurrent();
			String sessID=(Executions.getCurrent()).getDesktop().getId();
			logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
			String dirPath=repository+sessID;//session.getId();
			/*File dir = new File(dirPath);
		if(!dir.exists())
			dir.mkdirs();*/
			filePath=repository+"CustomerFeedBackSend"+File.separator+feedBackNumber+File.separator+event.getMedia().getName();	 
			if(attId.equals("4"))
			{
				attFile4=event.getMedia().getName();
				QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
				objAtt.setFilename(attFile4);
				objAtt.setFilepath(filePath);
				objAtt.setSessionid(sessID);
				objAtt.setImageMedia(event.getMedia());
				lstAtt.add(objAtt);
				if(lstAtt!=null && lstAtt.size()>0)
					selectedAttchemnets=lstAtt.get(0);
			}
		}
		catch (Exception e) {
			logger.error("ERROR in editCustomerFeedbackSend ----> uploadFile", e);			
		}
	}



	@Command 
	@NotifyChange({"attFile4","lstAtt"})
	public void deleteFromAttchamentList(@BindingParam("row") QuotationAttachmentModel obj)
	{
		try {
			QuotationAttachmentModel tempModel=new QuotationAttachmentModel();
			for(QuotationAttachmentModel attachmentModel:lstAtt)
			{
				if(attachmentModel.getFilename().equalsIgnoreCase(obj.getFilename()))
				{
					tempModel=attachmentModel;
					break;
				}

			}
			lstAtt.remove(tempModel);
		}
		catch (Exception e) {
			logger.error("ERROR in editCustomerFeedbackSend ----> deleteFromAttchamentList", e);			
		}
	}


	/*	@Command
	@NotifyChange({"selectedCustomers"})
    public void addAllCustomers()
		  {		
			 try
			  {
				 if(customerList!=null && customerList.size()>0)
				 {
						//selectedCustomers=(Set<CustomerModel>) customerList;
				 }

			  }
			 catch (Exception ex)
				{	
				logger.error("ERROR in editCustomerFeedbackSend ----> addAllCustomers", ex);			
				}
		  }
	 */


	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	/**
	 * @return the attFile4
	 */
	public String getAttFile4() {
		return attFile4;
	}

	/**
	 * @param attFile4 the attFile4 to set
	 */
	public void setAttFile4(String attFile4) {
		this.attFile4 = attFile4;
	}

	/**
	 * @return the lstAtt
	 */
	public List<QuotationAttachmentModel> getLstAtt() {
		return lstAtt;
	}

	/**
	 * @param lstAtt the lstAtt to set
	 */
	public void setLstAtt(List<QuotationAttachmentModel> lstAtt) {
		this.lstAtt = lstAtt;
	}

	/**
	 * @return the selectedAttchemnets
	 */
	public QuotationAttachmentModel getSelectedAttchemnets() {
		return selectedAttchemnets;
	}

	/**
	 * @param selectedAttchemnets the selectedAttchemnets to set
	 */
	public void setSelectedAttchemnets(QuotationAttachmentModel selectedAttchemnets) {
		this.selectedAttchemnets = selectedAttchemnets;
	}

	/**
	 * @return the df
	 */
	public DateFormat getDf() {
		return df;
	}

	/**
	 * @param df the df to set
	 */
	public void setDf(DateFormat df) {
		this.df = df;
	}

	/**
	 * @return the sdf
	 */
	public SimpleDateFormat getSdf() {
		return sdf;
	}

	/**
	 * @param sdf the sdf to set
	 */
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	/**
	 * @return the dcf
	 */
	public DecimalFormat getDcf() {
		return dcf;
	}

	/**
	 * @param dcf the dcf to set
	 */
	public void setDcf(DecimalFormat dcf) {
		this.dcf = dcf;
	}



	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the feedbackCreateDate
	 */
	public Date getFeedbackCreateDate() {
		return feedbackCreateDate;
	}

	/**
	 * @param feedbackCreateDate the feedbackCreateDate to set
	 */
	public void setFeedbackCreateDate(Date feedbackCreateDate) {
		this.feedbackCreateDate = feedbackCreateDate;
	}

	/**
	 * @return the feedBackModifiedDate
	 */
	public Date getFeedBackModifiedDate() {
		return feedBackModifiedDate;
	}

	/**
	 * @param feedBackModifiedDate the feedBackModifiedDate to set
	 */
	public void setFeedBackModifiedDate(Date feedBackModifiedDate) {
		this.feedBackModifiedDate = feedBackModifiedDate;
	}



	/**
	 * @return the feedBackNumber
	 */
	public String getFeedBackNumber() {
		return feedBackNumber;
	}



	/**
	 * @param feedBackNumber the feedBackNumber to set
	 */
	public void setFeedBackNumber(String feedBackNumber) {
		this.feedBackNumber = feedBackNumber;
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
	public void setSelectedCustomerFeedBack(
			CustomerFeedbackModel selectedCustomerFeedBack) {
		this.selectedCustomerFeedBack = selectedCustomerFeedBack;
	}



	@Command
	public void download(@BindingParam("row") QuotationAttachmentModel obj)
	{
		if(obj!=null && !obj.getFilepath().equalsIgnoreCase(""))
		{
			File file=new File(obj.getFilepath());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String mimeType=mimeTypesMap.getContentType(file);

			try {
				Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, obj.getFilename()); 

			}catch (FileNotFoundException e)
			{
				Clients.showNotification("There Is No Such File in server to download.(May be Deleted)",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			catch (Exception e) {
				logger.error("ERROR in CustomerFeedback ----> download", e);	
			}

		}
		else
		{
			Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	} 


	/*
	 @Command
	   public void customerInfoInEachEditFeedBack()
	   {
		   try
		   {

			   if(selectedClientType!=null)
				{
					if(selectedClientType.equalsIgnoreCase("Prospective Client"))
					{
						 Clients.showNotification("Prospective view is Under implementation.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						   return;

					}
					else
					{
							if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()==0)
							{
									Clients.showNotification("Please select the Customer.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
									return;
							}

							Map<String,Object> arg = new HashMap<String,Object>();
							arg.put("custKey", lstSelectedCustomerJob.getRecNo());
							arg.put("compKey",0);
							arg.put("type","view");
							Executions.createComponents("/hba/list/editcustomer.zul", null,arg);
					}
				}
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CustomerFeedback ----> customerInfoInEachEditFeedBack", ex);			
			}
	   }
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendClientEmail(String toEmails,String ccEmails,String bccEmails)
	{
		try
		{

			String[] to =null;
			String[] cc ={};
			cc=new String[50];
			String[] bcc =null;
			cc[0]="hinawi@eim.ae";
			String toMail="";
			String ccEmail="";
			String bccEmail="";
			if(toEmails!=null && !toEmails.equalsIgnoreCase(""))
			{
				toMail=toEmails;
				to= toMail.split(",");	
			}
			if(ccEmails!=null && !ccEmails.equalsIgnoreCase(""))
			{
				ccEmail=ccEmails;
				cc=ccEmail.split(",");	
			}
			if(bccEmails!=null && !bccEmails.equalsIgnoreCase(""))
			{
				bccEmail=bccEmails;
				bcc=bccEmail.split(",");	
			}

			ArrayList fileArray = new ArrayList();

			MailClient mc = new MailClient();

			String mailSubject=subject;

			StringBuffer result=null;

			result=new StringBuffer();

			result.append(memo);

			String messageBody=result.toString();			

			ArrayList fileArray1 = new ArrayList();
			for(QuotationAttachmentModel attPath:lstAtt)
			{
				File dir = new File(attPath.getFilepath());
				if(dir.exists())
					fileArray1.add(attPath.getFilepath());
			}
			fileArray=fileArray1;
			//mc.sendMochaMail("eng.chadi@gmail.com".split(","),bcc,bcc,mailSubject, messageBody,true,fileArray,true,"cutomerfollowUp",loggersEmail);
			mc.sendMochaMail(to,cc,bcc,mailSubject, messageBody,true,fileArray,true,"cutomerfollowUp",loggersEmail);
			// mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, fileArray);
		}
		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw)); 
			Messagebox.show(sw.toString());
			//logger.logErrorMsg(sw.toString(),"VendorIncreases-->SendEmail ");
		}	
	}


	/**
	 * @return the selectedTo
	 */
	public Set<String> getSelectedTo() {

		if(selectedTo==null)
			selectedTo=new  HashSet<String>();
		return selectedTo;
	}


	/**
	 * @param selectedTo the selectedTo to set
	 */
	public void setSelectedTo(Set<String> selectedTo) {
		this.selectedTo = selectedTo;
	}


	/**
	 * @return the listTo
	 */
	public Set<String> getListTo() {
		return listTo;
	}


	/**
	 * @param listTo the listTo to set
	 */
	public void setListTo(Set<String> listTo) {
		this.listTo = listTo;
	}


	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}


	/**
	 * @return the lstService
	 */
	public List<HRListValuesModel> getLstService() {
		return lstService;
	}


	/**
	 * @param lstService the lstService to set
	 */
	public void setLstService(List<HRListValuesModel> lstService) {
		this.lstService = lstService;
	}


	/**
	 * @return the selectedService
	 */
	public HRListValuesModel getSelectedService() {
		return selectedService;
	}


	/**
	 * @param selectedService the selectedService to set
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange({"memo","subject","lstAtt"})
	public void setSelectedService(final HRListValuesModel selectedService) {
		this.selectedService = selectedService;
		if(selectedService!=null && selectedService.getListId()>0)
		{
			if(memo!=null && !memo.equalsIgnoreCase(""))
			{		
				Messagebox.show("Do you want to append the the Template data in text area.?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onYes")) 
						{	 	
							selectedNotesForEachModule=feedBackData.getNotesForModules(selectedService.getListId());
							if(selectedNotesForEachModule!=null)
							{
								memo=selectedNotesForEachModule.getMemoEn()+memo;
								lstAtt.addAll(feedBackData.getNotesForModulesAttchamnet(selectedNotesForEachModule.getNoteID()));
							}
							else
							{
								memo="";
							}
							subject=selectedService.getEnDescription();
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "subject");

						}
						else 
						{		 
							//memo=memo;
							subject="";
							//lstAtt=lstAtt;
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "subject");
						}
					}

				});

			}else
			{
				selectedNotesForEachModule=feedBackData.getNotesForModules(selectedService.getListId());
				if(selectedNotesForEachModule!=null)
				{
					memo=selectedNotesForEachModule.getMemoEn()+memo;
					lstAtt.addAll(feedBackData.getNotesForModulesAttchamnet(selectedNotesForEachModule.getNoteID()));
				}
				else
				{
					memo="";

				}
				subject=selectedService.getEnDescription();
				BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
				BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
				BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "subject");

			}






		}
	}


	/**
	 * @return the lstCustomerTaks
	 */
	public List<TasksModel> getLstCustomerTaks() {
		return lstCustomerTaks;
	}


	/**
	 * @param lstCustomerTaks the lstCustomerTaks to set
	 */
	public void setLstCustomerTaks(List<TasksModel> lstCustomerTaks) {
		this.lstCustomerTaks = lstCustomerTaks;
	}


	/**
	 * @return the selectedTask
	 */
	public TasksModel getSelectedTask() {
		return selectedTask;
	}


	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	@Command
	@NotifyChange({"listTo","selectedTo"})
	public void newToContact(@BindingParam("contact") String contact)
	{
		String[] splited=null;
		if(contact!=null)
		{
			splited= contact.split("[,\\s]+");
		}
		for(String s:splited)
		{

			if(isValidEmailAddress(s))
			{
				listTo.add(s);
				selectedTo.add(s);
			}
			else
			{
				notValidemailAdress.add(s);
			}

		}
	}


	@Command
	@NotifyChange({"cCList","selectedCc"})
	public void newCcContact(@BindingParam("contact") String contact)
	{
		String[] splited=null;
		if(contact!=null)
		{
			splited= contact.split("[,\\s]+");
		}
		for(String s:splited)
		{
			if(isValidEmailAddress(s))
			{
				cCList.add(s);
				selectedCc.add(s);
			}
			else
			{
				notValidemailAdress.add(s);
			}
		}
	}

	@Command
	@NotifyChange({"bccList","selectedBcc"})
	public void newBccContact(@BindingParam("contact") String contact)
	{
		String[] splited=null;
		if(contact!=null)
		{
			splited= contact.split("[,\\s]+");
		}
		for(String s:splited)
		{
			if(isValidEmailAddress(s))
			{
				bccList.add(s);
				selectedBcc.add(s);
			}
			else
			{
				notValidemailAdress.add(s);
			}
		}
	}

	/**
	 * @param selectedTask the selectedTask to set
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange({"memo","lstAtt","listTo","selectedTo"})
	public void setSelectedTask(final TasksModel selectedTask) {
		this.selectedTask = selectedTask;
		if(selectedTask!=null && selectedTask.getTaskid()>0)
		{
			if(memo!=null && !memo.equalsIgnoreCase("")  && appendFalg)
			{
				Messagebox.show("Do you want to append the the task data in text area.?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onYes")) 
						{	 	
							taskDeatils=taskData.getTaskById(selectedTask.getTaskid());

							if(taskDeatils!=null)
							{
								StringBuffer buffer=new StringBuffer();


								buffer.append("<p>Dear Customer,</p> ");

								buffer.append("<p>Thank you for your feedback submitted with the following info.&nbsp;</p> ");

								buffer.append("<p>&nbsp;</p> ");

								if(taskDeatils.getFeedbackKey()>0)
									selectedCustomerFeedBackTemp=feedBackData.getCutsomerFeedbackById(taskDeatils.getFeedbackKey());

								if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
									buffer.append("<p><span style='font-size:12px'><strong>FeedBack Number :- "+selectedCustomerFeedBackTemp.getFeedbackNUmber()+"</strong></span></p>");

								buffer.append("<p><span style='font-size:12px'><strong><span style='line-height:1.6'>Created Task Number For FeedBack &nbsp;:- "+taskDeatils.getTaskNumber()+"</span></strong></span></p>");

								/*buffer.append("<p><span style='font-size:12px'><strong><span style='line-height:1.6;color:#FF0000'>Status &nbsp;:- "+taskDeatils.getStatusName()+"</span></strong></span></p>");*/

								if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
									buffer.append("<p><span style='font-size:12px'><strong>Sumited &nbsp;Comapny Name :- "+selectedCustomerFeedBackTemp.getCompanyName()+"</strong></span></p>");

								if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
									buffer.append("<p><span style='font-size:12px'><strong>Sumited Contact Person Name :- "+selectedCustomerFeedBackTemp.getContactPersonName()+"</strong></span></p>");

								if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
								{
									buffer.append("<p><span style='font-size:12px'><strong>Summited Email Address &nbsp;:- "+selectedCustomerFeedBackTemp.getEmail()+"</strong></span></p>");
									listTo.add(selectedCustomerFeedBackTemp.getEmail());
									selectedTo.add(selectedCustomerFeedBackTemp.getEmail());
								}

								if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
									buffer.append("<p><span style='font-size:12px'><strong>Memo :- "+selectedCustomerFeedBackTemp.getMemo()+"</strong></span></p>");
								else
									buffer.append("<p><span style='font-size:12px'><strong>Memo :- "+taskDeatils.getMemo()+"</strong></span></p>");	
								
								buffer.append("<p><strong><span style='color:rgb(255, 0, 0)'>Status &nbsp;:- "+taskDeatils.getStatusName()+"</span></strong></p>");
								if(taskDeatils.getStatusName().equalsIgnoreCase("In Progress"))
									buffer.append("<p><span style='color:#008000'><strong><span style='line-height:1.6'>The Task related to your above feedback is in Progress ,we will send you a email with the latest status soon.&nbsp;</span></strong></span></p>");
								else if(taskDeatils.getStatusName().equalsIgnoreCase("Done"))
									buffer.append("<p><span style='color:#008000'><strong>The Task related to your above feedback is Done ,Please respond to our official email mentioned in the signature within 3 working days else we will consider as it to be Done.&nbsp;</strong></span></p>");

								memo=buffer.toString()+memo;

								lstAtt=new ArrayList<QuotationAttachmentModel>();

								lstAtt=selectedCustomerFeedBackTemp.getLstAtt();

							}
							else
							{
								memo="";

							}

							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");

						}
						else 
						{		 
							//memo=memo;
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
							BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
						}
					}

				});

			}
			else
			{
				taskDeatils=taskData.getTaskById(selectedTask.getTaskid());

				if(taskDeatils!=null)
				{
					StringBuffer buffer=new StringBuffer();


					buffer.append("<p>Dear Customer,</p> ");

					buffer.append("<p>Thank you for your feedback submitted with the following info.&nbsp;</p> ");

					buffer.append("<p>&nbsp;</p> ");

					if(taskDeatils.getFeedbackKey()>0)
						selectedCustomerFeedBackTemp=feedBackData.getCutsomerFeedbackById(taskDeatils.getFeedbackKey());

					if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
						buffer.append("<p><span style='font-size:12px'><strong>FeedBack Number :- "+selectedCustomerFeedBackTemp.getFeedbackNUmber()+"</strong></span></p>");

					buffer.append("<p><span style='font-size:12px'><strong><span style='line-height:1.6'>Created Task Number For FeedBack &nbsp;:- "+taskDeatils.getTaskNumber()+"</span></strong></span></p>");

					/*buffer.append("<p><span style='font-size:12px'><strong><span style='line-height:1.6;color:#FF0000'>Status &nbsp;:- "+taskDeatils.getStatusName()+"</span></strong></span></p>");*/

					if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
						buffer.append("<p><span style='font-size:12px'><strong>Sumited &nbsp;Comapny Name :- "+selectedCustomerFeedBackTemp.getCompanyName()+"</strong></span></p>");

					if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
						buffer.append("<p><span style='font-size:12px'><strong>Sumited Contact Person Name :- "+selectedCustomerFeedBackTemp.getContactPersonName()+"</strong></span></p>");

					if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
					{
						buffer.append("<p><span style='font-size:12px'><strong>Summited Email Address &nbsp;:- "+selectedCustomerFeedBackTemp.getEmail()+"</strong></span></p>");
						listTo.add(selectedCustomerFeedBackTemp.getEmail());
						selectedTo.add(selectedCustomerFeedBackTemp.getEmail());
					}

					if(selectedCustomerFeedBackTemp!=null && selectedCustomerFeedBackTemp.getFeedbackKey()>0)
						buffer.append("<p><span style='font-size:12px'><strong>Memo :- "+selectedCustomerFeedBackTemp.getMemo()+"</strong></span></p>");
					else
						buffer.append("<p><span style='font-size:12px'><strong>Memo :- "+taskDeatils.getMemo()+"</strong></span></p>");	
					
					buffer.append("<p><strong><span style='color:rgb(255, 0, 0)'>Status &nbsp;:- "+taskDeatils.getStatusName()+"</span></strong></p>");
					if(taskDeatils.getStatusName().equalsIgnoreCase("In Progress"))
						buffer.append("<p><span style='color:#008000'><strong><span style='line-height:1.6'>The Task related to your above feedback is in Progress ,we will send you a email with the latest status soon.&nbsp;</span></strong></span></p>");
					else if(taskDeatils.getStatusName().equalsIgnoreCase("Done"))
						buffer.append("<p><span style='color:#008000'><strong>The Task related to your above feedback is Done ,Please respond to our official email mentioned in the signature with in 3 working days else we will consider as it to be Done.&nbsp;</strong></span></p>");


					memo=buffer.toString()+memo;

					lstAtt=new ArrayList<QuotationAttachmentModel>();

					lstAtt=selectedCustomerFeedBackTemp.getLstAtt();

				}
				else
				{
					memo="";

				}

				BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "memo");
				BindUtils.postNotifyChange(null, null, editCustomerFeedbackSend.this, "lstAtt");
			}


		}
	}


	/**
	 * @return the sortByName
	 */
	public boolean isSortByName() {
		return sortByName;
	}


	/**
	 * @param sortByName the sortByName to set
	 */
	public void setSortByName(boolean sortByName) {
		this.sortByName = sortByName;
	}


	/**
	 * @return the sortByNumber
	 */
	public boolean isSortByNumber() {
		return sortByNumber;
	}


	/**
	 * @param sortByNumber the sortByNumber to set
	 */
	public void setSortByNumber(boolean sortByNumber) {
		this.sortByNumber = sortByNumber;
	}


	/**
	 * @return the selectionTYpe
	 */
	public List<String> getSelectionTYpe() {
		return selectionTYpe;
	}


	/**
	 * @param selectionTYpe the selectionTYpe to set
	 */
	public void setSelectionTYpe(List<String> selectionTYpe) {
		this.selectionTYpe = selectionTYpe;
	}


	/**
	 * @return the selectedType
	 */
	public String getSelectedType() {
		return selectedType;
	}


	/**
	 * @param selectedType the selectedType to set
	 */
	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}


	/**
	 * @return the sendMailReminder
	 */
	public ReminderSettingsModel getSendMailReminder() {
		return sendMailReminder;
	}


	/**
	 * @param sendMailReminder the sendMailReminder to set
	 */
	public void setSendMailReminder(ReminderSettingsModel sendMailReminder) {
		this.sendMailReminder = sendMailReminder;
	}


	/**
	 * @return the lstMailMonths
	 */
	public ListModelList<String> getLstMailMonths() {
		return lstMailMonths;
	}


	/**
	 * @param lstMailMonths the lstMailMonths to set
	 */
	public void setLstMailMonths(ListModelList<String> lstMailMonths) {
		this.lstMailMonths = lstMailMonths;
	}


	/**
	 * @return the selectedMailMonths
	 */
	public Set<String> getSelectedMailMonths() {
		return selectedMailMonths;
	}


	/**
	 * @param selectedMailMonths the selectedMailMonths to set
	 */
	public void setSelectedMailMonths(Set<String> selectedMailMonths) {
		this.selectedMailMonths = selectedMailMonths;
	}


	/**
	 * @return the lstMailDays
	 */
	public ListModelList<String> getLstMailDays() {
		return lstMailDays;
	}


	/**
	 * @param lstMailDays the lstMailDays to set
	 */
	public void setLstMailDays(ListModelList<String> lstMailDays) {
		this.lstMailDays = lstMailDays;
	}


	/**
	 * @return the selectedMailDays
	 */
	public Set<String> getSelectedMailDays() {
		return selectedMailDays;
	}


	/**
	 * @param selectedMailDays the selectedMailDays to set
	 */
	public void setSelectedMailDays(Set<String> selectedMailDays) {
		this.selectedMailDays = selectedMailDays;
	}


	/**
	 * @return the lstMailWeekDays
	 */
	public ListModelList<String> getLstMailWeekDays() {
		return lstMailWeekDays;
	}


	/**
	 * @param lstMailWeekDays the lstMailWeekDays to set
	 */
	public void setLstMailWeekDays(ListModelList<String> lstMailWeekDays) {
		this.lstMailWeekDays = lstMailWeekDays;
	}


	/**
	 * @return the selectedMailWeekDays
	 */
	public Set<String> getSelectedMailWeekDays() {
		return selectedMailWeekDays;
	}


	/**
	 * @param selectedMailWeekDays the selectedMailWeekDays to set
	 */
	public void setSelectedMailWeekDays(Set<String> selectedMailWeekDays) {
		this.selectedMailWeekDays = selectedMailWeekDays;
	}


	/**
	 * @return the showCc
	 */
	public boolean isShowCc() {
		return showCc;
	}


	/**
	 * @param showCc the showCc to set
	 */
	public void setShowCc(boolean showCc) {
		this.showCc = showCc;
	}




	/**
	 * @return the showBcc
	 */
	public boolean isShowBcc() {
		return showBcc;
	}


	/**
	 * @param showBcc the showBcc to set
	 */
	public void setShowBcc(boolean showBcc) {
		this.showBcc = showBcc;
	}


	@Command 
	@NotifyChange({"showBcc"})
	public void showBccFunc()
	{
		if(showBcc)
		{
			showBcc=false;
		}
		else
		{
			showBcc=true;
		}

	}


	@Command 
	@NotifyChange({"showCc"})
	public void showCcFuc()
	{
		if(showCc)
		{
			showCc=false;
		}
		else
		{
			showCc=true;
		}

	}


	@Command 
	public void getSignature()
	{
		Clients.showNotification("Under Implimentation.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		return;

	}	

	@Command
	public void saveMailReminderSettings()
	{
		try
		{	

			//	sendMailReminder=data.getCompanyMailReminder(dbUser.getCompanyid(), "SendEMail",feedBackKey);
			if(sendMailReminder.getMailId()==0)
			{
				//no need to check
				//sendMailReminder=data.getCompanyMailReminder(dbUser.getCompanyid(), "SendEMail",0);
			}
			
			mailReminderKey=sendMailReminder.getMailId();//get email id to decide save or update while saving reminder setting 
			int result=0;
			String addedMailWeekDays=""; 
			String addedMailMonths=""; 
			String addedMailMonthDays=""; 

			for (String item : selectedMailWeekDays)
			{
				addedMailWeekDays+=item+",";
			}
			if(!addedMailWeekDays.equals(""))
				addedMailWeekDays=addedMailWeekDays.substring(0, addedMailWeekDays.length()-1) ;

			for (String item : selectedMailMonths)
			{
				addedMailMonths+=item+",";
			}
			if(!addedMailMonths.equals(""))
				addedMailMonths=addedMailMonths.substring(0, addedMailMonths.length()-1) ;

			for (String item : selectedMailDays)
			{
				addedMailMonthDays+=item+",";
			}
			if(!addedMailMonthDays.equals(""))
				addedMailMonthDays=addedMailMonthDays.substring(0, addedMailMonthDays.length()-1) ;

			sendMailReminder.setWeekly(addedMailWeekDays);
			sendMailReminder.setMonthly(addedMailMonths);
			sendMailReminder.setMonthlydays(addedMailMonthDays);
			sendMailReminder.setCompanyid(dbUser.getCompanyid());
			sendMailReminder.setMailId(feedBackKey);
			sendMailReminder.setRemindername("SendEmail");
			Calendar c = Calendar.getInstance();		
			sendMailReminder.setCreationDate(df.parse(sdf.format(c.getTime())));
			if(mailReminderKey>0)
			{
				result=data.updateMailReminderSettings(sendMailReminder);
			}else
			{
				result=data.saveMailReminderSettings(sendMailReminder);
			}
			if(result>0)
			{
				CustomerFeedbackModel obj=new CustomerFeedbackModel();
				obj.setFeedbackKey(feedBackKey);
				obj.setIsScheduled("Y");
				feedBackData.editCustomerFeedBackSentFromScheuler(obj) ;
				Messagebox.show("Reminder settings is saved successfully.","Reminder Settings", Messagebox.OK , Messagebox.INFORMATION);

			}
			else
				Messagebox.show("Error at save reminder settings !! ","Reminder Settings", Messagebox.OK , Messagebox.ERROR);
		}
		catch (Exception ex)
		{
			logger.error("error at editCustomerFeedbackSend>>saveCommand>> ",ex);
		}
	}





	/**
	 * @return the selectedBcc
	 */
	public Set<String> getSelectedBcc() {
		return selectedBcc;
	}


	/**
	 * @param selectedBcc the selectedBcc to set
	 */
	public void setSelectedBcc(Set<String> selectedBcc) {
		this.selectedBcc = selectedBcc;
	}


	/**
	 * @return the bccList
	 */
	public Set<String> getBccList() {
		return bccList;
	}


	/**
	 * @param bccList the bccList to set
	 */
	public void setBccList(Set<String> bccList) {
		this.bccList = bccList;
	}


	/**
	 * @return the selectedCc
	 */
	public Set<String> getSelectedCc() {
		return selectedCc;
	}


	/**
	 * @param selectedCc the selectedCc to set
	 */
	public void setSelectedCc(Set<String> selectedCc) {
		this.selectedCc = selectedCc;
	}


	/**
	 * @return the cCList
	 */
	public Set<String> getcCList() {
		return cCList;
	}


	/**
	 * @param cCList the cCList to set
	 */
	public void setcCList(Set<String> cCList) {
		this.cCList = cCList;
	}


	/**
	 * @return the loggersEmail
	 */
	public String getLoggersEmail() {
		return loggersEmail;
	}


	/**
	 * @param loggersEmail the loggersEmail to set
	 */
	public void setLoggersEmail(String loggersEmail) {
		this.loggersEmail = loggersEmail;
	}


	/**
	 * @return the remiderSaveVisible
	 */
	public boolean isRemiderSaveVisible() {
		return remiderSaveVisible;
	}


	/**
	 * @param remiderSaveVisible the remiderSaveVisible to set
	 */
	public void setRemiderSaveVisible(boolean remiderSaveVisible) {
		this.remiderSaveVisible = remiderSaveVisible;
	}





	/**
	 * @return the tempEmailSelctionPopUpList
	 */
	public List<EmailSelectionModel> getTempEmailSelctionPopUpList() {
		return tempEmailSelctionPopUpList;
	}


	/**
	 * @param tempEmailSelctionPopUpList the tempEmailSelctionPopUpList to set
	 */
	public void setTempEmailSelctionPopUpList(
			List<EmailSelectionModel> tempEmailSelctionPopUpList) {
		this.tempEmailSelctionPopUpList = tempEmailSelctionPopUpList;
	}


	/**
	 * @return the visibleSend
	 */
	public boolean isVisibleSend() {
		return visibleSend;
	}


	/**
	 * @param visibleSend the visibleSend to set
	 */
	public void setVisibleSend(boolean visibleSend) {
		this.visibleSend = visibleSend;
	}



	public void sourcePreporulation(Set<String> listOfEmails)
	{
		FeedbackSendSources customerFeedbackModel;
		for(String checkingEmail:listOfEmails)
		{
			for(CustomerModel customerModel:tempCustomerList)//customers
			{
				if(customerModel.getEmail()!=null && !customerModel.getEmail().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(customerModel.getEmail()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("C");
						customerFeedbackModel.setSourceId(customerModel.getCustkey());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}
				if(customerModel.getcC()!=null && !customerModel.getcC().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(customerModel.getcC()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("C");
						customerFeedbackModel.setSourceId(customerModel.getCustkey());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}
				if(customerModel.getCustomerContacts()!=null)
				{
					for(CustomerContact customerContact:customerModel.getCustomerContacts())
					{
						if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(customerContact.getEmail()))
						{
							if(customerContact.getDefaultFlag().equalsIgnoreCase("N"))
							{
								customerFeedbackModel=new FeedbackSendSources();
								customerFeedbackModel.setSourceType("C");
								customerFeedbackModel.setSourceId(customerContact.getCust_key());
								if(!selectedEmailSources.contains(customerFeedbackModel))
								selectedEmailSources.add(customerFeedbackModel);
								break;
							}
						}
					}
				}
			}


			for(ProspectiveModel contactDetailsModel:tempProsepctiveList)//prospectives
			{
				if(contactDetailsModel.getEmail()!=null && !contactDetailsModel.getEmail().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(contactDetailsModel.getEmail()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("P");
						customerFeedbackModel.setSourceId(contactDetailsModel.getRecNo());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}
				if(contactDetailsModel.getcC()!=null && !contactDetailsModel.getcC().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(contactDetailsModel.getcC()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("P");
						customerFeedbackModel.setSourceId(contactDetailsModel.getRecNo());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}
				if(contactDetailsModel.getProspectiveContact()!=null)
				{
					for(ProspectiveContactDetailsModel customerContact:contactDetailsModel.getProspectiveContact())
					{
						if(customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase(""))
						{
							if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(customerContact.getEmail()))
							{
								if(customerContact.getDefaultFlag().equalsIgnoreCase("N"))
								{
									customerFeedbackModel=new FeedbackSendSources();
									customerFeedbackModel.setSourceType("P");
									customerFeedbackModel.setSourceId(customerContact.getRecNo().intValue());
									if(!selectedEmailSources.contains(customerFeedbackModel))
									selectedEmailSources.add(customerFeedbackModel);
									break;
								}
							}
						}
					}
				}

			}


			for(VendorModel vendorModel:tempVendorList)//vendors
			{
				if(vendorModel.getEmail()!=null && !vendorModel.getEmail().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(vendorModel.getEmail()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("V");
						customerFeedbackModel.setSourceId(vendorModel.getVend_Key());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}
				if(vendorModel.getcC()!=null && !vendorModel.getcC().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(vendorModel.getEmail()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("V");
						customerFeedbackModel.setSourceId(vendorModel.getVend_Key());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}

			}

			for(EmployeeModel employeeModel:tempEmployeeList)//employees
			{
				if(employeeModel.getEmail()!=null && !employeeModel.getEmail().equalsIgnoreCase(""))
				{
					if(checkingEmail!=null && !checkingEmail.equalsIgnoreCase("") && checkingEmail.equalsIgnoreCase(employeeModel.getEmail()))
					{
						customerFeedbackModel=new FeedbackSendSources();
						customerFeedbackModel.setSourceType("E");
						customerFeedbackModel.setSourceId(employeeModel.getEmployeeKey());
						if(!selectedEmailSources.contains(customerFeedbackModel))
						selectedEmailSources.add(customerFeedbackModel);
						break;
					}
				}

			}
		}
	}


	public CustomerFeedBackData getFeedBackData() {
		return feedBackData;
	}


	public void setFeedBackData(CustomerFeedBackData feedBackData) {
		this.feedBackData = feedBackData;
	}


	public ReminderData getData() {
		return data;
	}


	public void setData(ReminderData data) {
		this.data = data;
	}


	public HRData getHrData() {
		return hrData;
	}


	public void setHrData(HRData hrData) {
		this.hrData = hrData;
	}


	public TaskData getTaskData() {
		return taskData;
	}


	public void setTaskData(TaskData taskData) {
		this.taskData = taskData;
	}


	public VendorsData getVendorsData() {
		return vendorsData;
	}


	public void setVendorsData(VendorsData vendorsData) {
		this.vendorsData = vendorsData;
	}


	public ProspectiveData getProspData() {
		return prospData;
	}


	public void setProspData(ProspectiveData prospData) {
		this.prospData = prospData;
	}


	public TimeSheetData getSheetData() {
		return sheetData;
	}


	public void setSheetData(TimeSheetData sheetData) {
		this.sheetData = sheetData;
	}


	public TasksModel getTaskDeatils() {
		return taskDeatils;
	}


	public void setTaskDeatils(TasksModel taskDeatils) {
		this.taskDeatils = taskDeatils;
	}


	public CustomerFeedbackModel getSelectedCustomerFeedBackTemp() {
		return selectedCustomerFeedBackTemp;
	}


	public void setSelectedCustomerFeedBackTemp(
			CustomerFeedbackModel selectedCustomerFeedBackTemp) {
		this.selectedCustomerFeedBackTemp = selectedCustomerFeedBackTemp;
	}


	public int getFeedBackKey() {
		return feedBackKey;
	}


	public void setFeedBackKey(int feedBackKey) {
		this.feedBackKey = feedBackKey;
	}


	public int getWebuserID() {
		return webuserID;
	}


	public void setWebuserID(int webuserID) {
		this.webuserID = webuserID;
	}


	public WebusersModel getDbUser() {
		return dbUser;
	}


	public void setDbUser(WebusersModel dbUser) {
		this.dbUser = dbUser;
	}


	public int getSupervisorID() {
		return supervisorID;
	}


	public void setSupervisorID(int supervisorID) {
		this.supervisorID = supervisorID;
	}


	public int getEmployeeKey() {
		return employeeKey;
	}


	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}


	public boolean isAdminUser() {
		return adminUser;
	}


	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}


	public MenuModel getCompanyRole() {
		return companyRole;
	}


	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}


	public boolean isCanSave() {
		return canSave;
	}


	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}


	public List<CustomerModel> getTempCustomerList() {
		return tempCustomerList;
	}


	public void setTempCustomerList(List<CustomerModel> tempCustomerList) {
		this.tempCustomerList = tempCustomerList;
	}


	public List<VendorModel> getTempVendorList() {
		return tempVendorList;
	}


	public void setTempVendorList(List<VendorModel> tempVendorList) {
		this.tempVendorList = tempVendorList;
	}


	public List<EmployeeModel> getTempEmployeeList() {
		return tempEmployeeList;
	}


	public void setTempEmployeeList(List<EmployeeModel> tempEmployeeList) {
		this.tempEmployeeList = tempEmployeeList;
	}


	public List<ProspectiveModel> getTempProsepctiveList() {
		return tempProsepctiveList;
	}


	public void setTempProsepctiveList(List<ProspectiveModel> tempProsepctiveList) {
		this.tempProsepctiveList = tempProsepctiveList;
	}


	public List<CustomerFeedbackModel> getListSelectedCustomers() {
		return listSelectedCustomers;
	}


	public void setListSelectedCustomers(
			List<CustomerFeedbackModel> listSelectedCustomers) {
		this.listSelectedCustomers = listSelectedCustomers;
	}


	public CustomerFeedbackModel getSelectedNotesForEachModule() {
		return selectedNotesForEachModule;
	}


	public void setSelectedNotesForEachModule(
			CustomerFeedbackModel selectedNotesForEachModule) {
		this.selectedNotesForEachModule = selectedNotesForEachModule;
	}


	public List<String> getNotValidemailAdress() {
		return notValidemailAdress;
	}


	public void setNotValidemailAdress(List<String> notValidemailAdress) {
		this.notValidemailAdress = notValidemailAdress;
	}


	public int getMailReminderKey() {
		return mailReminderKey;
	}


	public void setMailReminderKey(int mailReminderKey) {
		this.mailReminderKey = mailReminderKey;
	}


	public EmailSignatureModel getEmailSignatureModel() {
		return emailSignatureModel;
	}


	public void setEmailSignatureModel(EmailSignatureModel emailSignatureModel) {
		this.emailSignatureModel = emailSignatureModel;
	}


	public List<FeedbackSendSources> getSelectedEmailSources() {
		return selectedEmailSources;
	}


	public void setSelectedEmailSources(
			List<FeedbackSendSources> selectedEmailSources) {
		this.selectedEmailSources = selectedEmailSources;
	}


	public Set<FeedbackSendSources> getTempSelectedEmailSources() {
		return tempSelectedEmailSources;
	}


	public void setTempSelectedEmailSources(
			Set<FeedbackSendSources> tempSelectedEmailSources) {
		this.tempSelectedEmailSources = tempSelectedEmailSources;
	}


	public EmailSelectionModel getTemEmailpopUpModel() {
		return temEmailpopUpModel;
	}


	public void setTemEmailpopUpModel(EmailSelectionModel temEmailpopUpModel) {
		this.temEmailpopUpModel = temEmailpopUpModel;
	}


	public boolean isAppendFalg() {
		return appendFalg;
	}


	public void setAppendFalg(boolean appendFalg) {
		this.appendFalg = appendFalg;
	}

	
	



}
