package home;

import hba.HBAQueries;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.InternetAddress;

import layout.MenuModel;
import model.CompanyDBModel;
import model.CustomerContact;
import model.CustomerFeedbackModel;
import model.CustomerModel;
import model.CustomerStatusHistoryModel;
import model.EmployeeModel;
import model.HRListValuesModel;
import model.QbListsModel;
import model.SerialFields;
import model.TaskAndFeeddbackRelation;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import company.CompanyData;
import db.DBHandler;
import admin.TasksModel;
import setup.users.WebusersModel;

public class CustomerFeedback {
	private Logger logger = Logger.getLogger(this.getClass());

	//CustomerFeedBackData feedBackData=null;
	//HBAData hbadata=new HBAData();
	//TaskData taskData=new TaskData();

	private boolean custtype1=false;
	private boolean custtype2=false;
	private boolean custtype3=false;
	private boolean custtype4=false;

	private List<HRListValuesModel> lstPosition;
	private HRListValuesModel selectedPosition;

	private List<HRListValuesModel> lstCountry;
	private HRListValuesModel selectedCountry;

	private List<HRListValuesModel> lstCity;
	private HRListValuesModel selectedCity;

	private List<HRListValuesModel> lstFeedBackType;
	private List<HRListValuesModel> selectedFeedBackType;

	private List<String> lstSoftwareType;
	private String selectedSoftwareType;

	private List<HRListValuesModel> lstContactPersonIntial;
	private HRListValuesModel selectedContactPersonIntial;

	private List<QbListsModel> lstCustomerJob;

	private QbListsModel lstSelectedCustomerJob;

	private List<String> listClientType;

	private String selectedClientType;

	List<EmployeeModel> lstAssignToEmployees;	   
	private EmployeeModel selectedAssignToEmployee;
	private List<EmployeeModel> selectedAssignToEmployeeForEmail=new ArrayList<EmployeeModel>();

	private String attFile4;
	private List<QuotationAttachmentModel> lstAtt=new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets=new QuotationAttachmentModel();

	private CustomerFeedbackModel selectedCustomerFeedBack=new CustomerFeedbackModel();


	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	DecimalFormat dcf=new DecimalFormat("0.00");

	int feedBackKey=0;

	private String companyName="";

	private String contactPersonName="";

	private String mobileAreaCode1="";

	private String mobileAreaCode2="";

	private String mobile1="";

	private String mobile2="";


	private String telphoneAreaCode1="";

	private String telphoneAreaCode2="";

	private String telphone1="";

	private String telphone2="";

	private String email="";

	private String website="";

	private String memo="";

	private String instruction;

	private Date feedbackCreateDate;

	private Date feedBackModifiedDate;

	private String feedBackNumber="";

	private String address="";

	private int webuserID=0;

	WebusersModel dbUser=null;

	private int supervisorID=0;

	private int employeeKey=0;

	private boolean adminUser;

	private MenuModel companyRole;

	private boolean canSave;

	String taskNumber="";

	boolean isErrorFlag=false;

	CustomerModel slectedCustomer=null;

	private int feedbackKeyForTaskRelation=0;

	private List<TaskAndFeeddbackRelation> taskRelationlist=new ArrayList<TaskAndFeeddbackRelation>();

	private List<WebusersModel> lstUsers=new ArrayList<WebusersModel>();

	CompanyData companyData=new CompanyData();


	private String salesRepEmail="";
	private String webuserName="";





	@SuppressWarnings("rawtypes")
	public CustomerFeedback()
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
			
			if(false){ //(dbUser != null && dbUser.getCompanyid()==1) {
				CompanyDBModel objNew = new CompanyDBModel();
				objNew.setUserip("hinawi2.dyndns.org");
				objNew.setDbname("ECActualERPData");
				objNew.setDbuser("admin");
				objNew.setDbpwd("explorer654321");
				
				/*objNew.setUserip("CHADIRAHME-PC\\SQLEXPRESS");
				objNew.setDbname("ECActualERPData");
				objNew.setDbuser("sa");
				objNew.setDbpwd("123456");*/
				//feedBackData=new CustomerFeedBackData(objNew);
			}
			else
			{
				HBAQueries query = new HBAQueries();
				rs = mysqldb.executeNonQuery(query.getDBCompany(4)); //dbUser.getCompanyid()));
				while (rs.next()) {
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));
				}
				//feedBackData=new CustomerFeedBackData(obj);
			}

			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/customerFeedBackDialog");

			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
				lstUsers=companyData.getUsersList(dbUser.getCompanyid());

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
			employeeKey=dbUser.getEmployeeKey();
			if(employeeKey>0)
				supervisorID=employeeKey;//logged in as employee

			if(supervisorID>0)
				webuserID=supervisorID;

			if(win!=null)
			{

			//	feedBackKey=map.get("feedBackKey");

				if(type.equalsIgnoreCase("edit"))
				{
					canSave=true;
					win.setTitle("Edit Feedback Info");
				}
				else if (type.equalsIgnoreCase("view"))
				{
					canSave=false;
					win.setTitle("Edit Feedback Info");
				}
			}



			lstSoftwareType=new ArrayList<String>();
			lstCountry=new ArrayList<HRListValuesModel>();
			lstCity=new ArrayList<HRListValuesModel>();
			lstPosition=new ArrayList<HRListValuesModel>();

			attFile4="No file chosen";

			lstSoftwareType.add("Select");
			lstSoftwareType.add("Hinawi ERP Deskstop");
			lstSoftwareType.add("Hinawi Web Application");
			lstSoftwareType.add("QuickBooks");
			lstSoftwareType.add("Others");
			selectedSoftwareType=lstSoftwareType.get(0);

			listClientType=new ArrayList<String>();
			listClientType.add("Prospective Client");
			listClientType.add("Customer");
			selectedClientType=listClientType.get(1);

			if(selectedClientType!=null)
			{
//				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
//				{
//					lstCustomerJob=feedBackData.quotationPrecpectiveList();
//				}
//				else
//				{
//					lstCustomerJob=feedBackData.fillQbList("'Customer'");
//				}
			}

			if(lstCustomerJob.size()>0)
				lstSelectedCustomerJob=lstCustomerJob.get(0);

			//lstAssignToEmployees=feedBackData.getEmployeeList(0,"Select","A",0);//for automatic task

			/*HRListValuesModel addSelect=new HRListValuesModel();
			addSelect.setFieldId(0);
			addSelect.setEnDescription("Select");

			lstPosition.add(addSelect);
			lstCountry.add(addSelect);
			lstCity.add(addSelect);*/

			//load data from database

			//	lstPosition.addAll(hrData.getPostion(0,0));//get all positions
			//	lstCountry.addAll(fixedAstData.getHRListValues(2, "",0));
			//	lstCity.addAll(fixedAstData.getHRListValues(3, "",0));

//			lstFeedBackType=feedBackData.getHRListValuesForFeedBack(144,"");       //GET FROM COMAPNY FILE
//			lstContactPersonIntial=feedBackData.getHRListValuesForFeedBack(79, "");


			//initialize to select
			if(lstContactPersonIntial!=null)
				selectedContactPersonIntial=lstContactPersonIntial.get(0);

			//	selectedCity=lstCity.get(0);
			//selectedCountry=lstCountry.get(0);
			//	selectedPosition=lstPosition.get(0);



			if(feedBackKey>0)
			{
				//selectedCustomerFeedBack=feedBackData.getCutsomerFeedbackById(feedBackKey);
				if(selectedCustomerFeedBack!=null)
				{
					companyName=selectedCustomerFeedBack.getCompanyName();
					contactPersonName=selectedCustomerFeedBack.getContactPersonName();
					mobile1=selectedCustomerFeedBack.getMobile1();
					telphone1=selectedCustomerFeedBack.getTelphone1();
					email=selectedCustomerFeedBack.getEmail();
					website=selectedCustomerFeedBack.getWebsite();
					memo=selectedCustomerFeedBack.getMemo();
					instruction=selectedCustomerFeedBack.getInstruction();
					feedBackNumber=selectedCustomerFeedBack.getFeedbackNUmber();

					for(HRListValuesModel hrListValuesModel:lstContactPersonIntial)
					{
						if(hrListValuesModel.getListId()==selectedCustomerFeedBack.getCustomerInitailKey())
						{
							selectedContactPersonIntial=hrListValuesModel;
							break;
						}
					}

					String cleintType=selectedCustomerFeedBack.getCustomerType();
					if(cleintType!=null)
					{
						if(cleintType.equalsIgnoreCase("C"))
						{
							selectedClientType=listClientType.get(1);
						}
						else if(cleintType.equalsIgnoreCase("P"))
						{
							selectedClientType=listClientType.get(0);
						}
					}

					if(selectedClientType!=null)
					{
						if(selectedClientType.equalsIgnoreCase("Prospective Client"))
						{
							//lstCustomerJob=feedBackData.quotationPrecpectiveList();
						}
						else
						{
							//lstCustomerJob=feedBackData.fillQbList("'Customer'");
						}
					}

					for(QbListsModel customerModel:lstCustomerJob)
					{
						if(customerModel.getRecNo()==selectedCustomerFeedBack.getCustomerRefKey())
						{
							lstSelectedCustomerJob=customerModel;
							break;
						}
					}

					if(selectedCustomerFeedBack.getSelectedFeedBackType()!=null && selectedCustomerFeedBack.getSelectedFeedBackType().size()>0)
					{
						selectedFeedBackType=selectedCustomerFeedBack.getSelectedFeedBackType();
					}


					if(selectedCustomerFeedBack.getTaskRelationlist()!=null && selectedCustomerFeedBack.getTaskRelationlist().size()>0)
					{
						taskRelationlist=selectedCustomerFeedBack.getTaskRelationlist();
					}

					for(TaskAndFeeddbackRelation modeltaskRelation:taskRelationlist)
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

					if(selectedCustomerFeedBack.getLstAtt()!=null && selectedCustomerFeedBack.getLstAtt().size()>0)
					{
						lstAtt=selectedCustomerFeedBack.getLstAtt();
					}

					if(selectedCustomerFeedBack.getSelectedSoftwareType()!=null && !selectedCustomerFeedBack.getSelectedSoftwareType().equalsIgnoreCase(""))
					{
						if(selectedCustomerFeedBack.getSelectedSoftwareType().equalsIgnoreCase("Q"))
						{
							selectedSoftwareType=lstSoftwareType.get(3);
						}
						else if(selectedCustomerFeedBack.getSelectedSoftwareType().equalsIgnoreCase("D"))
						{
							selectedSoftwareType=lstSoftwareType.get(1);
						}
						else if(selectedCustomerFeedBack.getSelectedSoftwareType().equalsIgnoreCase("W"))
						{
							selectedSoftwareType=lstSoftwareType.get(2);
						}
						else
						{
							selectedSoftwareType=lstSoftwareType.get(0);
						}
					}
				}

			}
			else
			{

				companyName="";
				contactPersonName="";
				mobile1="";
				telphone1="";
				email="";
				website="";
				memo="";
				instruction="";
				//feedBackNumber=feedBackData.GetSaleNumber(SerialFields.FeedBack.toString());

			}


		}
		catch(Exception e)
		{
			logger.error("ERROR in CustomerFeedback ----> init", e);			
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
			logger.error("ERROR in CustomerFeedback ----> openTask", ex);			
		}
	}


	@Command
	public void saveCustomerFeedback()
	{
		try {

			/* if(custtype1==false && custtype2==false && custtype3==false && custtype4==false)
		 {
			 Messagebox.show("Customer Type cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
			   return;
		 }*/

			if(companyName==null || companyName.equalsIgnoreCase(""))
			{
				Messagebox.show("Company Name cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(contactPersonName==null || contactPersonName.equalsIgnoreCase(""))
			{
				Messagebox.show("Contact Person Name cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			/* if((telphone1==null || telphone1.equalsIgnoreCase("")))
		   {
			   Messagebox.show("Telephone number cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
			   return;
		   }	

		   if(mobile1==null || mobile1.equalsIgnoreCase(""))
		   {
			   Messagebox.show("Mobile number cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
			   return;
		   }


		   if(selectedPosition!=null && selectedPosition.getFieldId()==0)
		   {
			   Messagebox.show("Position cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
			   return;
		   }
		   if(selectedCountry!=null && selectedCountry.getFieldId()==0)
		   {
			   Messagebox.show("Country cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
			   return;
		   }

		   if(selectedCity!=null && selectedCity.getFieldId()==0)
		   {
			   Messagebox.show("City cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
			   return;
		   }

			 */
			if(email!=null && email.equalsIgnoreCase(""))
			{
				Messagebox.show("email cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(selectedFeedBackType==null || selectedFeedBackType.size()==0)
			{
				Messagebox.show("Feed back Type cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(selectedSoftwareType==null || selectedSoftwareType.equalsIgnoreCase("select"))
			{
				Messagebox.show("Software Type cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}


			if(memo!=null && memo.equalsIgnoreCase(""))
			{
				Messagebox.show("memo cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			for(HRListValuesModel model:selectedFeedBackType)
			{
				if(model.getEnDescription().trim().equalsIgnoreCase("Error In the Application"))
				{
					isErrorFlag=true;
					break;
				}
			}

			if(isErrorFlag && lstAtt!=null && lstAtt.size()==0)
			{
				Messagebox.show("Attachment cannot be empty..","Feed Back",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}



			CustomerFeedbackModel feedbackModel=new CustomerFeedbackModel();
			int result=0;

			if(feedBackKey>0)
			{
				feedbackModel.setFeedbackKey(feedBackKey);
				feedbackKeyForTaskRelation=feedBackKey;
			}
			else
			{
				//feedbackModel.setFeedbackKey(feedBackData.getMaxID("CustomerEnquiry", "Enquiry_ID"));
				feedbackKeyForTaskRelation=feedbackModel.getFeedbackKey();
			}


			if(!email.equals("") && !email.equals(""))
			{

				List<CustomerModel> lstCustomer;

//				lstCustomer=feedBackData.getCustomerList("");
//
//				for(CustomerModel custModel:lstCustomer)
//				{
//					for(CustomerContact customerContact:custModel.getCustomerContacts())
//					{
//						if((customerContact.getEmail()!=null && !customerContact.getEmail().equalsIgnoreCase("") && customerContact.getEmail().trim().equalsIgnoreCase(email.trim())) || (custModel!=null && !custModel.getEmail().equalsIgnoreCase("") && custModel.getEmail().equalsIgnoreCase(email.trim())) || (custModel!=null && !custModel.getcC().equalsIgnoreCase("") && custModel.getcC().equalsIgnoreCase(email.trim())))
//						{
//							slectedCustomer=custModel;
//							salesRepEmail=feedBackData.getsalesRepListByID(slectedCustomer.getSalesRepKey());//get slaes rep email
//							break;
//						}
//					}
//				}

			}

			feedbackModel.setCompanyName(companyName);

			feedbackModel.setContactPersonName(contactPersonName);
			if(feedBackKey>0)//while edit
			{
//				if((feedBackNumber!=null) && (feedBackData.checkIfFeedBackNumberIsDuplicate(feedBackNumber,feedbackModel.getFeedbackKey())==true))
//				{
//					Messagebox.show("Duplicate Feed Back Number!","Feed Back",Messagebox.OK,Messagebox.INFORMATION);
//					return ;
//				}
			}
			else //while create
			{

//				if((feedBackNumber!=null) && (feedBackData.checkIfFeedBackNumberIsDuplicate(feedBackNumber,feedbackModel.getFeedbackKey())==true))
//				{
//					feedBackNumber=feedBackData.GetSaleNumber(SerialFields.FeedBack.toString());
//				}


			}

			feedbackModel.setFeedbackNUmber(feedBackNumber);

			/*  String custType="";
		  	if(custtype1)
			{
			  custType=custType+"V";
			}
			else if(custtype2)
			{
				 custType=custType+","+"C";	
			}
			else if(custtype3)
			{
				 custType=custType+","+"R";
			}
			else if(custtype4)
			{
				 custType=custType+","+"Q";
			}*/

			if(selectedClientType!=null)
			{
				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
				{
					feedbackModel.setCustomerType("P");
				}
				else
				{
					feedbackModel.setCustomerType("C");
				}

			}

			if(selectedPosition!=null)
				feedbackModel.setPositionKey(selectedPosition.getListId());
			else
				feedbackModel.setPositionKey(0);

			if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
				feedbackModel.setCustomerRefKey(lstSelectedCustomerJob.getRecNo());
			else if(slectedCustomer!=null && slectedCustomer.getCustkey()>0)
				feedbackModel.setCustomerRefKey(slectedCustomer.getCustkey());
			else
				feedbackModel.setCustomerRefKey(0);

			if(selectedContactPersonIntial!=null)
				feedbackModel.setCustomerInitailKey(selectedContactPersonIntial.getListId());
			else
				feedbackModel.setCustomerInitailKey(0); 

			if(selectedCountry!=null)
				feedbackModel.setCountryKey(selectedCountry.getListId());
			else
				feedbackModel.setCountryKey(0);	  

			if(selectedCity!=null)
				feedbackModel.setCityKey(selectedCity.getListId());
			else
				feedbackModel.setCityKey(0);

			if(selectedFeedBackType!=null)
				feedbackModel.setSelectedFeedBackType(selectedFeedBackType);
			else
				feedbackModel.setSelectedFeedBackType(null);

			if(taskRelationlist!=null && taskRelationlist.size()>0)
				feedbackModel.setTaskRelationlist(taskRelationlist);

			if(selectedSoftwareType!=null)
			{
				if(selectedSoftwareType.equalsIgnoreCase("Select"))
				{
					feedbackModel.setSelectedSoftwareType("S");
				}
				else if (selectedSoftwareType.equalsIgnoreCase("Hinawi ERP Deskstop"))
				{
					feedbackModel.setSelectedSoftwareType("D");
				}
				else if (selectedSoftwareType.equalsIgnoreCase("Hinawi Web Application"))
				{
					feedbackModel.setSelectedSoftwareType("W");
				}
				else if (selectedSoftwareType.equalsIgnoreCase("QuickBooks"))
				{
					feedbackModel.setSelectedSoftwareType("Q");
				}
				else
				{
					feedbackModel.setSelectedSoftwareType("O");
				}


			}


			feedbackModel.setMemo(memo);



			// feedbackModel.setAddress(address);

			feedbackModel.setEmail(email);

			feedbackModel.setWebsite(website);

			feedbackModel.setWebuserID(webuserID);


			feedbackModel.setMobile1(mobile1);

			feedbackModel.setMobileAreaCode1(mobileAreaCode1);

			feedbackModel.setTelphone1(telphone1);

			feedbackModel.setTelphoneAreaCode1(telphoneAreaCode1);

			feedbackModel.setInstruction(instruction);


			Calendar c = Calendar.getInstance();		


			feedbackModel.setFeedBackModifiedDate(df.parse(sdf.format(c.getTime())));

			feedbackModel.setFeedbackCreateDate(df.parse(sdf.format(c.getTime())));


			if(feedBackKey>0)
			{

//				result=feedBackData.editCustomerFeedBackData(feedbackModel,lstAtt);
//				if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
//				{
//					CustomerStatusHistoryModel model =new CustomerStatusHistoryModel();
//					model.setRecNo(feedBackData.getMaxID("CustomerStatusHistory", "RecNo"));
//					model.setCustKey(lstSelectedCustomerJob.getRecNo());
//					model.setActionDate(df.parse(sdf.format(c.getTime())));
//					model.setCreatedFrom("Edited Feed Back from Online");
//					model.setStatusDescription(memo);
//					model.setType("C");
//					model.setTxnRecNo(feedbackModel.getFeedbackKey());
//					model.setTxnRefNumber(feedbackModel.getFeedbackNUmber());
//					feedBackData.saveCustomerStatusHistroyfromFeedback(model,webuserID,webuserName);
//					feedBackData.updateCustomerStatusDescription(model);
//				}
				Messagebox.show("Feedback Updated sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
			}
			else
			{
//				result=feedBackData.saveCustomerFeedbackData(feedbackModel,lstAtt);
//				if(slectedCustomer!=null && slectedCustomer.getCustkey()>0)
//				{
//					CustomerStatusHistoryModel model =new CustomerStatusHistoryModel();
//					model.setRecNo(feedBackData.getMaxID("CustomerStatusHistory", "RecNo"));
//					model.setCustKey(slectedCustomer.getCustkey());
//					model.setActionDate(df.parse(sdf.format(c.getTime())));
//					model.setCreatedFrom("Created Feed Back from Online");
//					model.setStatusDescription(memo);
//					model.setType("C");
//					model.setTxnRecNo(feedbackModel.getFeedbackKey());
//					model.setTxnRefNumber(feedbackModel.getFeedbackNUmber());
//					feedBackData.saveCustomerStatusHistroyfromFeedback(model,webuserID,webuserName);
//					feedBackData.updateCustomerStatusDescription(model);
//				}
				//  desc="Feedback With Following Details Has Been";
				//  type=2;
				// Messagebox.show("Feedback Created sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
			}
			if(result>0)
			{
				if(feedBackKey==0)
				{
					//feedBackData.ConfigSerialNumberCashInvoice(SerialFields.FeedBack, feedBackNumber,0);
					if(!email.equals("") && !email.equals(""))
					{
						sendClientEmail(email,slectedCustomer);
						prepareFeedbackMail();
					}

					Window window = (Window)Executions.createComponents(
							"thankyou.zul", null, null);
					window.doModal();

				}


			}

		} catch (Exception e) {
			logger.error("ERROR in CustomerFeedback ----> saveCustomerFeedback", e);	
		}
		/*  x.detach();
		 Map args = new HashMap();
		 BindUtils.postGlobalCommand(null, null, "refreshParentTaskForm", args);*/




	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void saveCustomerFeedbackFromEdit(@BindingParam("cmp") Window x)
	{
		try 
		{
			if(feedBackKey>0)
			{
				saveCustomerFeedback();
				x.detach();
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentFeedBackForm", args);
			}
		}
		catch (Exception e) {
			logger.error("ERROR in CustomerFeedback ----> saveCustomerFeedbackFromEdit", e);	
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
			filePath=repository+"CustomerFeedBack"+File.separator+feedBackNumber+File.separator+event.getMedia().getName();	 
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
			logger.error("ERROR in CustomerFeedback ----> uploadFile", e);			
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
			logger.error("ERROR in CustomerFeedback ----> deleteFromAttchamentList", e);			
		}
	}



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
	 * @return the custtype1
	 */
	public boolean isCusttype1() {
		return custtype1;
	}

	/**
	 * @param custtype1 the custtype1 to set
	 */
	public void setCusttype1(boolean custtype1) {
		this.custtype1 = custtype1;
	}

	/**
	 * @return the custtype2
	 */
	public boolean isCusttype2() {
		return custtype2;
	}

	/**
	 * @param custtype2 the custtype2 to set
	 */
	public void setCusttype2(boolean custtype2) {
		this.custtype2 = custtype2;
	}

	/**
	 * @return the custtype3
	 */
	public boolean isCusttype3() {
		return custtype3;
	}

	/**
	 * @param custtype3 the custtype3 to set
	 */
	public void setCusttype3(boolean custtype3) {
		this.custtype3 = custtype3;
	}

	/**
	 * @return the custtype4
	 */
	public boolean isCusttype4() {
		return custtype4;
	}

	/**
	 * @param custtype4 the custtype4 to set
	 */
	public void setCusttype4(boolean custtype4) {
		this.custtype4 = custtype4;
	}

	/**
	 * @return the lstPosition
	 */
	public List<HRListValuesModel> getLstPosition() {
		return lstPosition;
	}

	/**
	 * @param lstPosition the lstPosition to set
	 */
	public void setLstPosition(List<HRListValuesModel> lstPosition) {
		this.lstPosition = lstPosition;
	}

	/**
	 * @return the selectedPosition
	 */
	public HRListValuesModel getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * @param selectedPosition the selectedPosition to set
	 */
	public void setSelectedPosition(HRListValuesModel selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	/**
	 * @return the lstCountry
	 */
	public List<HRListValuesModel> getLstCountry() {
		return lstCountry;
	}

	/**
	 * @param lstCountry the lstCountry to set
	 */
	public void setLstCountry(List<HRListValuesModel> lstCountry) {
		this.lstCountry = lstCountry;
	}

	/**
	 * @return the selectedCountry
	 */
	public HRListValuesModel getSelectedCountry() {
		return selectedCountry;
	}

	/**
	 * @param selectedCountry the selectedCountry to set
	 */
	public void setSelectedCountry(HRListValuesModel selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	/**
	 * @return the lstCity
	 */
	public List<HRListValuesModel> getLstCity() {
		return lstCity;
	}

	/**
	 * @param lstCity the lstCity to set
	 */
	public void setLstCity(List<HRListValuesModel> lstCity) {
		this.lstCity = lstCity;
	}

	/**
	 * @return the selectedCity
	 */
	public HRListValuesModel getSelectedCity() {
		return selectedCity;
	}

	/**
	 * @param selectedCity the selectedCity to set
	 */
	public void setSelectedCity(HRListValuesModel selectedCity) {
		this.selectedCity = selectedCity;
	}

	/**
	 * @return the lstFeedBackType
	 */
	public List<HRListValuesModel> getLstFeedBackType() {
		return lstFeedBackType;
	}

	/**
	 * @param lstFeedBackType the lstFeedBackType to set
	 */
	public void setLstFeedBackType(List<HRListValuesModel> lstFeedBackType) {
		this.lstFeedBackType = lstFeedBackType;
	}

	/**
	 * @return the selectedFeedBackType
	 */
	public List<HRListValuesModel> getSelectedFeedBackType() {
		return selectedFeedBackType;
	}

	/**
	 * @param selectedFeedBackType the selectedFeedBackType to set
	 */
	@NotifyChange({"instruction"})
	public void setSelectedFeedBackType(List<HRListValuesModel> selectedFeedBackType) {
		this.selectedFeedBackType = selectedFeedBackType;
		if(selectedFeedBackType!=null)
		{
			StringBuffer inst=new StringBuffer();
			for(HRListValuesModel hrListValuesModel:selectedFeedBackType)
			{
				if(hrListValuesModel.getNotes()!=null)
				{
					inst.append(hrListValuesModel.getNotes());
					inst.append("\n");
					inst.append("\n");
					inst.append("\n");
				}
			}
			instruction=inst.toString();
		}
	}


	/**
	 * @return the lstSoftwareType
	 */
	public List<String> getLstSoftwareType() {
		return lstSoftwareType;
	}

	/**
	 * @param lstSoftwareType the lstSoftwareType to set
	 */
	public void setLstSoftwareType(List<String> lstSoftwareType) {
		this.lstSoftwareType = lstSoftwareType;
	}

	/**
	 * @return the selectedSoftwareType
	 */
	public String getSelectedSoftwareType() {
		return selectedSoftwareType;
	}


	/**
	 * @param selectedSoftwareType the selectedSoftwareType to set
	 */
	public void setSelectedSoftwareType(String selectedSoftwareType) {
		this.selectedSoftwareType = selectedSoftwareType;
	}

	/**
	 * @return the lstContactPersonIntial
	 */
	public List<HRListValuesModel> getLstContactPersonIntial() {
		return lstContactPersonIntial;
	}

	/**
	 * @param lstContactPersonIntial the lstContactPersonIntial to set
	 */
	public void setLstContactPersonIntial(
			List<HRListValuesModel> lstContactPersonIntial) {
		this.lstContactPersonIntial = lstContactPersonIntial;
	}

	/**
	 * @return the selectedContactPersonIntial
	 */
	public HRListValuesModel getSelectedContactPersonIntial() {
		return selectedContactPersonIntial;
	}

	/**
	 * @param selectedContactPersonIntial the selectedContactPersonIntial to set
	 */
	public void setSelectedContactPersonIntial(
			HRListValuesModel selectedContactPersonIntial) {
		this.selectedContactPersonIntial = selectedContactPersonIntial;
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the contactPersonName
	 */
	public String getContactPersonName() {
		return contactPersonName;
	}

	/**
	 * @param contactPersonName the contactPersonName to set
	 */
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	/**
	 * @return the mobileAreaCode1
	 */
	public String getMobileAreaCode1() {
		return mobileAreaCode1;
	}

	/**
	 * @param mobileAreaCode1 the mobileAreaCode1 to set
	 */
	public void setMobileAreaCode1(String mobileAreaCode1) {
		this.mobileAreaCode1 = mobileAreaCode1;
	}

	/**
	 * @return the mobileAreaCode2
	 */
	public String getMobileAreaCode2() {
		return mobileAreaCode2;
	}

	/**
	 * @param mobileAreaCode2 the mobileAreaCode2 to set
	 */
	public void setMobileAreaCode2(String mobileAreaCode2) {
		this.mobileAreaCode2 = mobileAreaCode2;
	}

	/**
	 * @return the mobile1
	 */
	public String getMobile1() {
		return mobile1;
	}

	/**
	 * @param mobile1 the mobile1 to set
	 */
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}

	/**
	 * @return the mobile2
	 */
	public String getMobile2() {
		return mobile2;
	}

	/**
	 * @param mobile2 the mobile2 to set
	 */
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	/**
	 * @return the telphoneAreaCode1
	 */
	public String getTelphoneAreaCode1() {
		return telphoneAreaCode1;
	}

	/**
	 * @param telphoneAreaCode1 the telphoneAreaCode1 to set
	 */
	public void setTelphoneAreaCode1(String telphoneAreaCode1) {
		this.telphoneAreaCode1 = telphoneAreaCode1;
	}

	/**
	 * @return the telphoneAreaCode2
	 */
	public String getTelphoneAreaCode2() {
		return telphoneAreaCode2;
	}

	/**
	 * @param telphoneAreaCode2 the telphoneAreaCode2 to set
	 */
	public void setTelphoneAreaCode2(String telphoneAreaCode2) {
		this.telphoneAreaCode2 = telphoneAreaCode2;
	}

	/**
	 * @return the telphone1
	 */
	public String getTelphone1() {
		return telphone1;
	}

	/**
	 * @param telphone1 the telphone1 to set
	 */
	public void setTelphone1(String telphone1) {
		this.telphone1 = telphone1;
	}

	/**
	 * @return the telphone2
	 */
	public String getTelphone2() {
		return telphone2;
	}

	/**
	 * @param telphone2 the telphone2 to set
	 */
	public void setTelphone2(String telphone2) {
		this.telphone2 = telphone2;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
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
	 * @return the instruction
	 */
	public String getInstruction() {
		return instruction;
	}

	/**
	 * @param instruction the instruction to set
	 */
	public void setInstruction(String instruction) {
		this.instruction = instruction;
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
	 * @return the lstSelectedCustomerJob
	 */
	public QbListsModel getLstSelectedCustomerJob() {
		return lstSelectedCustomerJob;
	}


	/**
	 * @param lstSelectedCustomerJob the lstSelectedCustomerJob to set
	 */
	public void setLstSelectedCustomerJob(QbListsModel lstSelectedCustomerJob) {
		this.lstSelectedCustomerJob = lstSelectedCustomerJob;
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


	/**
	 * @return the listClientType
	 */
	public List<String> getListClientType() {
		return listClientType;
	}


	/**
	 * @param listClientType the listClientType to set
	 */
	public void setListClientType(List<String> listClientType) {
		this.listClientType = listClientType;
	}


	/**
	 * @return the selectedClientType
	 */
	public String getSelectedClientType() {
		return selectedClientType;
	}


	/**
	 * @param selectedClientType the selectedClientType to set
	 */
	@NotifyChange({"lstCustomerJob"})
	public void setSelectedClientType(String selectedClientType) {
		this.selectedClientType = selectedClientType;
		lstCustomerJob.clear();
		if(selectedClientType!=null)
		{
//			if(selectedClientType.equalsIgnoreCase("Prospective Client"))
//			{
//				lstCustomerJob=feedBackData.quotationPrecpectiveList();
//			}
//			else
//			{
//				lstCustomerJob=feedBackData.fillQbList("'Customer'");
//			}
		}
	}


	/**
	 * @return the lstCustomerJob
	 */
	public List<QbListsModel> getLstCustomerJob() {
		return lstCustomerJob;
	}


	/**
	 * @param lstCustomerJob the lstCustomerJob to set
	 */
	public void setLstCustomerJob(List<QbListsModel> lstCustomerJob) {
		this.lstCustomerJob = lstCustomerJob;
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

	@SuppressWarnings("rawtypes")
	private void sendClientEmail(String email,CustomerModel customer)
	{
		try
		{
			
			String[] to =null;
			String[] cc ="hinawi@eim.ae,sales@hinawi.ae".split(",");			
			String[] bcc =null;
			//Properties properties = new Properties();
			//properties.load(new FileInputStream(path+ "mail.properties"));				
			String toMail=email;
			//toMail+=",hinawi@eim.ae";
			ArrayList fileArray = new ArrayList();
			to= toMail.split(",");	

			//InternetAddress[] internetAddress = new InternetAddress[0];
			//List result = new LinkedList();
			//result.add(new InternetAddress(toMail));

			//InternetAddress[] toAdd= Mailer.parse(toMail);//(InternetAddress[]) result.toArray(internetAddress);

			MailClient mc = new MailClient();
			String subject="Thank you for your feedback";
			StringBuffer result=null;
			result=new StringBuffer();

			if(selectedFeedBackType!=null  && selectedFeedBackType.size()==1 && (selectedFeedBackType.get(0).getEnDescription().equalsIgnoreCase("Errors")||selectedFeedBackType.get(0).getEnDescription().trim().equalsIgnoreCase("Error In the Application")))
			{
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Dear Valued User,</strong><o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Thank you for your feedback ! Your request has been received.<br />");
				result.append("We are sorry for this, our staff will solve this immediately and contact you ASAP.<o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>To know more about the software Kindly visit our website <strong><a href='http://www.hinawi.com/' title='blocked::http://www.hinawi.com/");
				result.append("http://www.hinawi.com/'>www.hinawi.com</a></strong> and Hinawi Web Application at <a href='http://hinawi2.dyndns.org:8181/sata/demo.zul' title='blocked::http://hinawi2.dyndns.org:8181/sata/demo.zul");
				result.append("http://hinawi2.dyndns.org:8181/sata/demo.zul'> Please Click Here</a> which is integrated with Desktop.<o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>To view the Hinawi Desktop Menus kindly check attached.<o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>If request for Quotation you can go to contact us <a href='http://hinawi2.dyndns.org:8181/sata/quotationContactUsLayout.zul' title='blocked::http://hinawi2.dyndns.org:8181/sata/quotationContactUsLayout.zul http://hinawi2.dyndns.org:8181/sata/quotationContactUsLayout.zul'>Please Click Here</a>.<o:p></o:p></span></font></p>");
				/*result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Kindly contact us if you need any assistance at <strong><a href='mailto:hinawi@eim.ae,%20raza@hinawi.ae' title='blocked::mailto:hinawi@eim.ae, raza@hinawi.ae'>hinawi@eim.ae, raza@hinawi.ae</a></strong><o:p></o:p></span></font></p>");*/
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Your satisfaction is important to us, and we value your feedback.<o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;color:#FF0000'>Please use this feedback number for future refrence - FeedBack Number : "+feedBackNumber+".<br />");
				
				if(customer!=null)
				{
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 14pt;color:#FF0000'><b>Important:</b></span></font></p> ");
				
				result.append("<p><b>Below information is saved in our database. Kindly update your data below by sending email to: hinawi@eim.ae</b></p>");
				
				result.append("<p>Company Name :<b>" + customer.getFullName()+"</b></p>");
				result.append("<p>Contact Person :" + customer.getContact()+"</p>");
				result.append("<p>Phone :" + customer.getPhone()+"</p>");
				result.append("<p>Alt. Phone :" + customer.getAltphone()+"</p>");
				result.append("<p>Fax :" + customer.getFax()+"</p>");
				result.append("<p>Alt. Contact :" + customer.getAltcontact()+"</p>");
				result.append("<p>Mobile :" + customer.getMobile()+"</p>");
				result.append("<p>Email :" + customer.getEmail()+"</p>");
				result.append("<p>CC :" + customer.getcC()+"</p>");
				result.append("<p>Website :" + customer.getWebsite()+"</p>");
				result.append("<p>SkypeID :" + customer.getSkypeId()+"</p>");
				result.append("<p>Country :" + customer.getBillCountry()+"</p>");
				result.append("<p>City :" + customer.getBillCity()+"</p>");
				
				result.append("<p>Contract expiry : " + customer.getCustomerContactExpiryDateStr()+"</p>");
				DecimalFormat df=new DecimalFormat("#,###,##0.00");
				result.append("<p><span style='font-size: 12pt;color:#FF0000'>Outstanding Balance : " +df.format(customer.getLocalBalance())+"</span></p>");
								
				}
				
				result.append("<p><strong><font face='Calibri' size='3'><span style='font-size: 12pt;'>Shukran</span></font></strong><font face='Calibri' size='3'><span style='font-size: 12pt;'><o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Explorer Computer Team</strong><o:p></o:p></span></font></p>");



			}
			else
			{
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Dear Valued User,</strong><o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Thank you for your feedback ! </span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Your request has been received and one of our support team members will attend to your inquiry soon.</span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;color:#FF0000'><b>Your feedback Number is : "+feedBackNumber+".</b></span></font></p> ");
				
				//result.append("<li>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>- To keep updated about Hinawi Software, kindly visit our facebook page at <strong><a href='https://www.facebook.com/hinawisoftware/' >https://www.facebook.com/hinawisoftware/ </a></strong></font></p>");				
				//result.append("</li>");
				
				//result.append("<li>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>- Try Hinawi Web Application at <strong><a href='http://hinawi2.dyndns.org:8181/sata/demo.zul' >http://hinawi2.dyndns.org:8181/sata/demo.zul </a></strong> integrated with Hinawi desktop and QuickBooks.</font></p>");
				//result.append("</li>");
				
				//result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>To know more about the software Kindly visit our website <strong><a href='http://www.hinawi.com/' title='blocked::http://www.hinawi.com/");
				//result.append("http://www.hinawi.com/'>www.hinawi.com</a></strong> and Hinawi Web Application at <a href='http://hinawi2.dyndns.org:8181/sata/demo.zul' title='blocked::http://hinawi2.dyndns.org:8181/sata/demo.zul");
				//result.append("http://hinawi2.dyndns.org:8181/sata/demo.zul'>Please Click Here</a> which is integrated with Desktop.<o:p></o:p></span></font></p>");
				//result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>To view the Hinawi Desktop Menus kindly check attached.<o:p></o:p></span></font></p>");
				//result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>If request for Quotation you can go to contact us <a href='http://hinawi2.dyndns.org:8181/sata/quotationContactUsLayout.zul' title='blocked::http://hinawi2.dyndns.org:8181/sata/quotationContactUsLayout.zul http://hinawi2.dyndns.org:8181/sata/quotationContactUsLayout.zul'>Please Click Here</a>.<o:p></o:p></span></font></p>");
				/*result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Kindly contact us if you need any assistance at <strong><a href='mailto:hinawi@eim.ae,%20raza@hinawi.ae' title='blocked::mailto:hinawi@eim.ae, raza@hinawi.ae'>hinawi@eim.ae, raza@hinawi.ae</a></strong><o:p></o:p></span></font></p>");*/
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>Your satisfaction is important to us, and we value your feedback.<o:p></o:p></span></font></p>");
				//result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;color:#FF0000'>Please use this feedback number for future refrence - FeedBack Number : "+feedBackNumber+".<br />");
				
				if(customer!=null)
				{
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 14pt;color:#FF0000'><b>Important:</b></span></font></p> ");
				
				result.append("<p><b>Below information is saved in our database. Kindly update your data below by sending email to: hinawi@eim.ae</b></p>");
				
				result.append("<p>Company Name :<b>" + customer.getFullName()+"</b></p>");
				result.append("<p>Contact Person :" + customer.getContact()+"</p>");
				result.append("<p>Phone :" + customer.getPhone()+"</p>");
				result.append("<p>Alt. Phone :" + customer.getAltphone()+"</p>");
				result.append("<p>Fax :" + customer.getFax()+"</p>");
				result.append("<p>Alt. Contact :" + customer.getAltcontact()+"</p>");
				result.append("<p>Mobile :" + customer.getMobile()+"</p>");
				result.append("<p>Email :" + customer.getEmail()+"</p>");
				result.append("<p>CC :" + customer.getcC()+"</p>");
				result.append("<p>Website :" + customer.getWebsite()+"</p>");
				result.append("<p>SkypeID :" + customer.getSkypeId()+"</p>");
				result.append("<p>Country :" + customer.getBillCountry()+"</p>");
				result.append("<p>City :" + customer.getBillCity()+"</p>");
				
				
				result.append("<p>Contract expiry : " + customer.getCustomerContactExpiryDateStr()+"</p>");
				DecimalFormat df=new DecimalFormat("#,###,##0.00");
				result.append("<p><span style='font-size: 12pt;color:#FF0000'>Outstanding Balance : " +df.format(customer.getLocalBalance())+"</span></p>");
								
				}
				
				
				result.append("<p><strong><font face='Calibri' size='3'><span style='font-size: 12pt;'><br/><br/>Shukran</span></font></strong><font face='Calibri' size='3'><span style='font-size: 12pt;'><o:p></o:p></span></font></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Explorer Computer Team</strong><o:p></o:p></span></font></p>");
				result.append("<p><strong><a href='http://www.hinawi.com/'>www.hinawi.com</a></strong></p>");
				
			}


			String messageBody=result.toString();			

			/*	ArrayList fileArray1 = new ArrayList();
			 	for(QuotationAttachmentModel attPath:lstAtt)
			 	{
			 		File dir = new File(attPath.getFilepath());
					if(dir.exists())
						fileArray1.add(attPath.getFilepath());
			 	}*/
			//stop this when testing
		//	fileArray=feedBackData.getCilentAttachemnt();
			mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,fileArray,true,"feedback","");
			//mc.sendMochaMail(to,null,bcc, subject, messageBody,true,fileArray,true,"feedback","");
			// mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, fileArray);
		}
		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw)); 
			Messagebox.show(sw.toString());
			//logger.logErrorMsg(sw.toString(),"VendorIncreases-->SendEmail ");
		}	
	}


	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private void prepareFeedbackMail()
	{
		try 
		{			 						 
			String[] to =null;
			String[] cc ={};
			String[] bcc =null;
			String selectedSoft=""; 
			String toMail="";

			if(isErrorFlag && selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("Hinawi ERP Deskstop") && selectedFeedBackType!=null && selectedFeedBackType.size()==1)
			{
				toMail="sony@hinawi.ae,ajish@hinawi.ae";
				cc=new String[5];
				//cc[0]="dani@hinawi.ae";
				if(!salesRepEmail.equalsIgnoreCase(""))
				{
					cc[0]=salesRepEmail;
				}
				else
				{
					//cc[1]="raza@hinawi.ae";
					cc[0]="walaa@hinawi.ae";
				}

				saveTask("sony@hinawi.ae");
			}
			else if(isErrorFlag && selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("Hinawi Web Application") && selectedFeedBackType!=null && selectedFeedBackType.size()==1)
			{
				toMail="eng.chadi@gmail.com";//,amr@hinawi.ae
				cc=new String[3];
				//cc[0]="dani@hinawi.ae";
				if(!salesRepEmail.equalsIgnoreCase(""))
				{
					cc[0]=salesRepEmail;
				}
				else
				{
					//cc[1]="raza@hinawi.ae";
					//cc[1]="walaa@hinawi.ae";
				}
				saveTask("walaa@hinawi.ae");
			}
			else if(isErrorFlag && selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("Hinawi ERP Deskstop") && selectedFeedBackType!=null && selectedFeedBackType.size()>1)
			{
				cc=new String[3];
				//cc[0]="dani@hinawi.ae";
				
				if(!salesRepEmail.equalsIgnoreCase(""))
				{
					toMail="sony@hinawi.ae,ajish@hinawi.ae,"+salesRepEmail;
					saveTask(salesRepEmail);
				}
				else
				{
					toMail="sony@hinawi.ae,ajish@hinawi.ae";//,raza@hinawi.ae,odeh@hinawi.ae,walaa@hinawi.ae
					saveTask("raza@hinawi.ae");
				}
				saveTask("sony@hinawi.ae");

			}
			else if(isErrorFlag && selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("Hinawi Web Application") && selectedFeedBackType!=null && selectedFeedBackType.size()>1)
			{

				cc=new String[3];
				//cc[0]="dani@hinawi.ae";
				if(!salesRepEmail.equalsIgnoreCase(""))
				{
					toMail="eng.chadi@gmail.com,"+salesRepEmail;
					saveTask(salesRepEmail);
				}
				else
				{
					toMail="eng.chadi@gmail.com,walaa@hinawi.ae";//,raza@hinawi.ae,iqbal@hinawi.ae,
					saveTask("raza@hinawi.ae");
				}
				saveTask("walaa@hinawi.ae");
			}
			else if(selectedFeedBackType!=null  && selectedFeedBackType.size()<=2 && (selectedFeedBackType.get(0).getEnDescription().equalsIgnoreCase("Register QuickBooks (Validation Code)")||selectedFeedBackType.get(0).getEnDescription().trim().equalsIgnoreCase("Register Hinawi")))
			{
				toMail="hinawi@eim.ae";
				saveTask("hinawi@eim.ae");

			}
			else
			{


				cc=new String[3];
				//cc[0]="dani@hinawi.ae";
				if(selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("Hinawi Web Application"))
				{
					cc[0]="eng.chadi@gmail.com";
				}
				else if (selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("Hinawi ERP Deskstop"))
				{
					cc[0]="sony@hinawi.ae";
				}
				if(!salesRepEmail.equalsIgnoreCase(""))
				{
					toMail=salesRepEmail;
					saveTask(salesRepEmail);
				}
				else
				{
					toMail="walaa@hinawi.ae";//,raza@hinawi.ae
					saveTask("walaa@hinawi.ae");//raza@hinawi.ae
				}

			}


			if(selectedSoftwareType!=null && selectedSoftwareType.equalsIgnoreCase("select"))
			{
				selectedSoft="None";
			}
			else
			{
				selectedSoft=selectedSoftwareType;
			}
			//String toMail="raza@hinawi.ae,sony@hinawi.ae,iqbal@hinawi.ae,ajish@hinawi.ae"; //"chadi.rahme@teltacworldwide.com";
			ArrayList fileArray = new ArrayList();
			to= toMail.split(",");	
			InternetAddress[] toAdd= Mailer.parse(toMail);//(InternetAddress[]) result.toArray(internetAddress);
			MailClient mc = new MailClient();
			String subject="A new feedback inquiry has been received : Feedback Number - "+feedBackNumber+"  ";
			StringBuffer result=null;
			result=new StringBuffer();


			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong><u><span style='display: none;'>&nbsp;</span>Feedback Details</u></strong></p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>&nbsp;</p><p><font face='Calibri' size='3'><span style='font-size: 12pt;'>");
			result.append("<strong>Company Name</strong>&nbsp;<strong>:"+companyName+"</strong></p><p><font face='Calibri' size='3'><span style='font-size: 12pt;'>");
			result.append("<strong>Contact person : &nbsp;</strong>"+contactPersonName+"</p><p><font face='Calibri' size='3'><span style='font-size: 12pt;'>");
			result.append("<strong>Mobile No. :</strong><strong>&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; "+mobile1+" &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</strong><strong>Telephone No. :</strong><strong>&nbsp;"+telphone1+"</strong></p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><span id='qWDP6r'><strong>Email :</strong></span>&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "+email+".</p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><span id='qWDP930'><strong>Feedback Type :</strong></span>&nbsp;&nbsp;");
			for(HRListValuesModel model: selectedFeedBackType)
			{
				result.append(""+model.getEnDescription()+"<br/>");
			}
			result.append("</p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Software Type :</strong>&nbsp;&nbsp; &nbsp;"+selectedSoft+".</p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Instructions :</strong></p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>"+instruction+"</p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>&nbsp;</p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong>Memo :</strong></p>");
			result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'>"+memo+"</p>");
			for(EmployeeModel employeeModel:selectedAssignToEmployeeForEmail)
			{
				if(employeeModel!=null && employeeModel.getEmployeeKey()>0 && !employeeModel.getFullName().equalsIgnoreCase(""))
					result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong> A task with no. : "+employeeModel.getTaskNumber()+" Automaticaly Created for "+employeeModel.getFullName()+" </strong></p>");
			}
			if(slectedCustomer!=null && !slectedCustomer.getCustomerContactExpiryDateStr().equalsIgnoreCase(""))
			{
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong><u><span style='display: none;'>&nbsp;</span> Automatic Mapped Customer Details</u></strong></p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong> Customer Name : </strong>"+slectedCustomer.getFullName()+"</p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong> Customer Expiry Date : </strong>"+slectedCustomer.getCustomerContactExpiryDateStr()+"</p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong> Blacklisted ? : </strong>"+slectedCustomer.getBalckListed()+"</p>");
				result.append("<p><font face='Calibri' size='3'><span style='font-size: 12pt;'><strong> Status  : </strong>"+slectedCustomer.getIsactive()+"</p>");
			}

			String messageBody=result.toString();	
			ArrayList fileArray1 = new ArrayList();
			for(QuotationAttachmentModel attPath:lstAtt)
			{
				File dir = new File(attPath.getFilepath());
				if(dir.exists())
					fileArray1.add(attPath.getFilepath());
			}
			fileArray=fileArray1;
			mc.sendMochaMail(to,cc,bcc,subject, messageBody,true,fileArray1,false,"feedback","");


		} 
		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw)); 
			Messagebox.show(sw.toString());
			//logger.logErrorMsg(sw.toString(),"VendorIncreases-->SendEmail ");
		}	
		//Messagebox.show("Thank you.Your request is being sent.");
	}


	//------------------------------------------------------------------------------Create Task Automatically when feed back is created -------------------------------------------------------------------


	@Command
	public void saveTask(String employeeeMail)
	{
		try { 

			TasksModel tasksModel=new TasksModel();
			//taskNumber=feedBackData.GetSaleNumber(SerialFields.Task.toString());
			for(EmployeeModel model:lstAssignToEmployees)
			{

				for(String emailstring:model.getListEmails())
				{
					if(emailstring!=null && !emailstring.equalsIgnoreCase("") && emailstring.trim().equalsIgnoreCase(employeeeMail.trim()))
					{
						selectedAssignToEmployee=model;
						selectedAssignToEmployee.setTaskNumber(taskNumber);
						EmployeeModel tempEmp=new EmployeeModel();
						tempEmp.setEmployeeKey(selectedAssignToEmployee.getEmployeeKey());
						tempEmp.setTaskNumber(taskNumber);
						tempEmp.setFullName(selectedAssignToEmployee.getFullName());
						selectedAssignToEmployeeForEmail.add(tempEmp);
						break;
					}
				}

			}

			Calendar c = Calendar.getInstance();		


			int result=0;

			//tasksModel.setTaskid(feedBackData.getMaxID("Tasks", "taskId"));

			tasksModel.setTaskName("From FeedBack");



			tasksModel.setClientType("C");


			tasksModel.setTaskTypeId(0);  

			if(isErrorFlag)
				tasksModel.setPriorityRefKey(9810);
			else
				tasksModel.setPriorityRefKey(9812);	  


			tasksModel.setStatusKey(10);


			tasksModel.setSreviceId(0);


			if(slectedCustomer!=null)
				tasksModel.setCustomerRefKey(slectedCustomer.getCustkey());
			else
				tasksModel.setCustomerRefKey(0);	 


			tasksModel.setProjectKey(0);

			tasksModel.setPrviousTaskLinkId(0);

			if(selectedAssignToEmployee!=null)
			{
				tasksModel.setEmployeeid(selectedAssignToEmployee.getEmployeeKey()); 
			}
			else
				tasksModel.setEmployeeid(0); 


			tasksModel.setCcEmployeeKey(0);

			tasksModel.setHoursOrDays("H");

			tasksModel.setMemo(memo);

			tasksModel.setComments("");

			tasksModel.setHistory("");

			tasksModel.setCreationDate(df.parse(sdf.format(c.getTime())));

			tasksModel.setExpectedDatetofinish(df.parse(sdf.format(c.getTime())));

			tasksModel.setReminderDate(df.parse(sdf.format(c.getTime())));

			tasksModel.setActualNumber(0);

			tasksModel.setEstimatatedNumber(1);

			tasksModel.setTaskStep("");

			tasksModel.setTaskNumber(taskNumber);

			tasksModel.setCreatedUserID(0);//admin

			tasksModel.setRemindIn(0);//

			tasksModel.setUpdatedTime(df.parse(sdf.format(c.getTime())));

			tasksModel.setCustomerNamefromFeedback(companyName);

			tasksModel.setCreatedAutommaticTask("Y");

			tasksModel.setFeedbackKey(feedbackKeyForTaskRelation);

			//result=feedBackData.addTask(tasksModel,lstAtt);

			if(result>0)
			{
//				feedBackData.ConfigSerialNumberCashInvoice(SerialFields.Task, taskNumber,0);
//				CustomerFeedbackModel customerFeedback =new CustomerFeedbackModel();
//				customerFeedback.setTaskID(tasksModel.getTaskid());
//				customerFeedback.setTaskStatusId(10);
//				customerFeedback.setFeedbackCreateDate(df.parse(sdf.format(c.getTime())));
//				customerFeedback.setUserId(0);//admin
//				customerFeedback.setFeedbackKey(feedbackKeyForTaskRelation);
//				feedBackData.saveFeedbackCustomerRelation(customerFeedback);
//				if(slectedCustomer!=null && slectedCustomer.getCustkey()>0)
//				{
//					CustomerStatusHistoryModel model =new CustomerStatusHistoryModel();
//					model.setRecNo(feedBackData.getMaxID("CustomerStatusHistory", "RecNo"));
//					model.setCustKey(slectedCustomer.getCustkey());
//					model.setActionDate(df.parse(sdf.format(c.getTime())));
//					model.setCreatedFrom("Automatic Creation Of Task From FeedBack Online");
//					model.setStatusDescription(memo);
//					model.setType("C");
//					model.setTxnRecNo(tasksModel.getTaskid());
//					model.setTxnRefNumber(tasksModel.getTaskNumber());
//					feedBackData.saveCustomerStatusHistroyfromFeedback(model,webuserID,webuserName);
//					feedBackData.updateCustomerStatusDescription(model);
//				}

			}

		} catch (ParseException e) 
		{
			logger.error("ERROR in CustomerFeedback ----> saveTask", e);
		}
	}


	/**
	 * @return the taskRelationlist
	 */
	public List<TaskAndFeeddbackRelation> getTaskRelationlist() {
		return taskRelationlist;
	}


	/**
	 * @param taskRelationlist the taskRelationlist to set
	 */
	public void setTaskRelationlist(List<TaskAndFeeddbackRelation> taskRelationlist) {
		this.taskRelationlist = taskRelationlist;
	}


	/**
	 * @return the canSave
	 */
	public boolean isCanSave() {
		return canSave;
	}


	/**
	 * @param canSave the canSave to set
	 */
	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}


//	public CustomerFeedBackData getFeedBackData() {
//		return feedBackData;
//	}


//	public void setFeedBackData(CustomerFeedBackData feedBackData) {
//		this.feedBackData = feedBackData;
//	}


//	public HBAData getHbadata() {
//		return hbadata;
//	}
//
//
//	public void setHbadata(HBAData hbadata) {
//		this.hbadata = hbadata;
//	}
//
//
//	public TaskData getTaskData() {
//		return taskData;
//	}
//
//
//	public void setTaskData(TaskData taskData) {
//		this.taskData = taskData;
//	}


	public List<EmployeeModel> getLstAssignToEmployees() {
		return lstAssignToEmployees;
	}


	public void setLstAssignToEmployees(List<EmployeeModel> lstAssignToEmployees) {
		this.lstAssignToEmployees = lstAssignToEmployees;
	}


	public EmployeeModel getSelectedAssignToEmployee() {
		return selectedAssignToEmployee;
	}


	public void setSelectedAssignToEmployee(EmployeeModel selectedAssignToEmployee) {
		this.selectedAssignToEmployee = selectedAssignToEmployee;
	}


	public List<EmployeeModel> getSelectedAssignToEmployeeForEmail() {
		return selectedAssignToEmployeeForEmail;
	}


	public void setSelectedAssignToEmployeeForEmail(
			List<EmployeeModel> selectedAssignToEmployeeForEmail) {
		this.selectedAssignToEmployeeForEmail = selectedAssignToEmployeeForEmail;
	}


	public int getFeedBackKey() {
		return feedBackKey;
	}


	public void setFeedBackKey(int feedBackKey) {
		this.feedBackKey = feedBackKey;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
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


	public String getTaskNumber() {
		return taskNumber;
	}


	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}


	public boolean isErrorFlag() {
		return isErrorFlag;
	}


	public void setErrorFlag(boolean isErrorFlag) {
		this.isErrorFlag = isErrorFlag;
	}


	public CustomerModel getSlectedCustomer() {
		return slectedCustomer;
	}


	public void setSlectedCustomer(CustomerModel slectedCustomer) {
		this.slectedCustomer = slectedCustomer;
	}


	public int getFeedbackKeyForTaskRelation() {
		return feedbackKeyForTaskRelation;
	}


	public void setFeedbackKeyForTaskRelation(int feedbackKeyForTaskRelation) {
		this.feedbackKeyForTaskRelation = feedbackKeyForTaskRelation;
	}


	public List<WebusersModel> getLstUsers() {
		return lstUsers;
	}


	public void setLstUsers(List<WebusersModel> lstUsers) {
		this.lstUsers = lstUsers;
	}


	public CompanyData getCompanyData() {
		return companyData;
	}


	public void setCompanyData(CompanyData companyData) {
		this.companyData = companyData;
	}


	public String getSalesRepEmail() {
		return salesRepEmail;
	}


	public void setSalesRepEmail(String salesRepEmail) {
		this.salesRepEmail = salesRepEmail;
	}


	public String getWebuserName() {
		return webuserName;
	}


	public void setWebuserName(String webuserName) {
		this.webuserName = webuserName;
	}




}
