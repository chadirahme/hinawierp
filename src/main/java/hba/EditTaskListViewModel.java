package hba;
import home.MailClient;
import home.QuotationAttachmentModel;
import hr.HRData;
import hr.model.CompanyModel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javax.imageio.ImageIO;

import layout.MenuModel;
import model.CashInvoiceModel;
import model.CompSetupModel;
import model.CustomerFeedbackModel;
import model.CustomerModel;
import model.CustomerStatusHistoryModel;
import model.EmployeeModel;
import model.HRListValuesModel;
import model.ProjectModel;
import model.QbListsModel;
import model.SerialFields;

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
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import timesheet.TimeSheetData;
import admin.TasksModel;
import company.CompanyData;

public class EditTaskListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	CustomerData data=new CustomerData();
	HBAData hbadata=new HBAData();
	HRData hrData=new HRData();
	TimeSheetData timsheetData=new TimeSheetData();
	TaskData taskData=new TaskData();
	CompanyData companyData=new CompanyData();

	private TasksModel selectedTask;
	private boolean canSave;
	private boolean activeCustomer;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	DecimalFormat dcf=new DecimalFormat("0.00");

	TasksModel tempModel=new TasksModel();
	private String attFile4;
	private List<QuotationAttachmentModel> lstAtt;
	private QuotationAttachmentModel selectedAttchemnets;
	private String editPhotoPath="";

	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;

	WebusersModel creatoEmployeeIDTogetEmail=null;


	private List<HRListValuesModel> lstTaskType;
	private HRListValuesModel selectedTaskType;

	private List<HRListValuesModel> lstTaskStatus;
	private HRListValuesModel selectedTaskStatus;


	private List<HRListValuesModel> lstTaskPriority;
	private HRListValuesModel selectedTaskPriority; 


	private List<ProjectModel> lstProject;
	private ProjectModel lstSelectedProject;

	private List<QbListsModel> lstCustomerJob;
	private QbListsModel lstSelectedCustomerJob;

	private List<HRListValuesModel> lstService;
	private HRListValuesModel lstSelectedService;

	private List<String> hourOrDaysList;
	private String selectedHoursOrDays;

	private List<WebusersModel> lstUsers=new ArrayList<WebusersModel>();

	private Date taskDate;

	private Date expectedDateTofinish;

	private Date reminderDate;

	private double remindInNumber=0;

	private String taskName="";

	private String taskNumber="";

	private double estimatedEffort=0;

	private double actualEffort=0;

	private String memo="";

	private String comments="";

	private String history="";

	private String taskStep="";

	private List<EmployeeModel> lstAssignToEmployees;
	private EmployeeModel selectedAssignToEmployee;

	private List<EmployeeModel> lstCCToEmployees;
	private EmployeeModel selectedCCToEmployee;

	private List<TasksModel> lstLinkToPriviousTask;
	private TasksModel selectedLinkToPreviousTask;


	private List<String> listClientType;

	private String selectedClientType;

	WebusersModel dbUser=null;
	private int supervisorID=0;
	private int employeeKey=0;
	int taskKey=0;

	private int webUserID=0;
	private String webuserName="";

	private boolean adminUser;

	private MenuModel companyRole;

	private String employeeEmail="";

	private String ccEmployeemEmail="";

	private int creatorEmployeeId=0;

	private String creatorEmailadress="";

	List<MenuModel> list;

	private CompSetupModel emailcompSetup;//for activating email or no 

	List<TasksModel> taskHistory=new ArrayList<TasksModel>();

	private boolean disableTaskCreatorPanel=false;

	Calendar c = Calendar.getInstance();		


	//Group of tasks

	private List<TasksModel> lstTask=new ArrayList<TasksModel>();
	private List<TasksModel> lstAllTask=new ArrayList<TasksModel>();

	private int totalNumber;

	private int tempTotalNUmber;

	int serailNo=1;

	int customerKeyOtherForms=0;

	String customerTypeOtherForms="";

	private List<QuotationAttachmentModel> lstOtherFormAtt=new ArrayList<QuotationAttachmentModel>();

	private String memoOtherForms="";

	boolean otherformFalg=false;

	String actionTYpe;

	private String labelStatus="";

	private boolean visbaleCustomerNAmefromFeedback=false; 

	private String cutomerNamefromFeedBack="";

	private String cutomerContractExipryDate="No Data";

	List<CustomerStatusHistoryModel> customerStatus=new ArrayList<CustomerStatusHistoryModel>();

	String customerStatusStr="No Data";

	private String createdBy="";

	private String lastUpdatedBy="";

	private int feedbackKeyForTaskRelation=0;

	private boolean refreshTaskNumber=false; 

	private String customerExpiry="";

	private String customerCreated="";

	private String jobType="";

	private String balcklisted="";

	private String active="";

	private int firstTimeLoop=100;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public EditTaskListViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/taskModalDialog");
			attFile4="No file chosen";
			actionTYpe="Create";

			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
				lstUsers=companyData.getUsersList(dbUser.getCompanyid());

				if(adminUser)
				{
					webUserID=0;
					webuserName="Admin";
				}
				else
				{
					webUserID=dbUser.getEmployeeKey(); //i want to use Employeekey here to save and fitler by it and not by  dbUser.getUserid();
					webuserName=dbUser.getUsername();
				}
			}

			supervisorID=dbUser.getSupervisor();//logged in as supervisor
			employeeKey=dbUser.getEmployeeKey();
			if(employeeKey>0)
				supervisorID=employeeKey;//logged in as employee

			//not used now
			//if(supervisorID>0)
			//	webUserID=supervisorID;


			taskKey=(Integer)map.get("taskKey");

			listClientType=new ArrayList<String>();
			listClientType.add("Prospective Client");
			listClientType.add("Customer");
			selectedClientType=listClientType.get(1);

			//getCompanyRolePermessions(dbUser.getCompanyroleid());

			int defaultCompanyId=0;
			creatoEmployeeIDTogetEmail=new WebusersModel();
			defaultCompanyId=hrData.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=hrData.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
				if(item.getCompKey()==defaultCompanyId)
					selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
				selectedCompany=lstComapnies.get(0);
			if(type.equalsIgnoreCase("add"))
			{
				canSave=true;
				win.setTitle("Add Task Info");
			}
			else if(type.equalsIgnoreCase("edit"))
			{
				canSave=true;
				win.setTitle("Edit Task Info");
			}
			else if(type.equalsIgnoreCase("OtherForms"))
			{
				win.setTitle("Add Task Info");
				canSave=true;
				customerKeyOtherForms=(Integer)map.get("customerKey");

				customerTypeOtherForms=(String)map.get("cutomerType");

				memoOtherForms=(String)map.get("memo");

				if((ArrayList<QuotationAttachmentModel>)map.get("attchmnt")!=null)
				{
					lstOtherFormAtt=(ArrayList<QuotationAttachmentModel>)map.get("attchmnt");
				}
				otherformFalg=true;

			}
			else
			{
				canSave=false;
				win.setTitle("View Task Info");
			}


			taskDate=df.parse(sdf.format(c.getTime()));
			lstTaskType=hrData.getHRListValues(141,"Select");
			lstTaskStatus=hrData.getHRListValues(143,"Select");		
			lstTaskPriority=hrData.getHRListValues(142,"Select");
			lstProject=timsheetData.getProjectList(0,"Transfer",true,0);
			lstService=hrData.getHRListValues(47,"Select");

			if(selectedClientType!=null)
			{
				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
				{
					lstCustomerJob=hbadata.quotationPrecpectiveList();
				}
				else
				{
					lstCustomerJob=hbadata.fillQbList("'Customer'");
				}
			}

			lstAssignToEmployees=hrData.getEmployeeList(0,"Select","A",0);
			lstCCToEmployees=hrData.getEmployeeList(0,"Select","A",0);
			lstLinkToPriviousTask=new ArrayList<TasksModel>();//taskData.getAllTaskOtherThanCurrentTask(0);
			emailcompSetup=hrData.getLeaveCompanySetup(selectedCompany.getCompKey()); //check only if email_reqd=Y to send email

			hourOrDaysList=new ArrayList<String>();
			hourOrDaysList.add("Hours");
			hourOrDaysList.add("Days");
			selectedHoursOrDays=hourOrDaysList.get(0);
			lstAtt=new ArrayList<QuotationAttachmentModel>();

			if(lstLinkToPriviousTask.size()>0)
				selectedLinkToPreviousTask=lstLinkToPriviousTask.get(0);

			if(lstAssignToEmployees.size()>0)
				selectedAssignToEmployee=lstAssignToEmployees.get(0);

			if(lstCCToEmployees.size()>0)
				selectedCCToEmployee=lstCCToEmployees.get(0);

			if(lstTaskType.size()>0)
				selectedTaskType=lstTaskType.get(0);


			if(lstTaskStatus.size()>0)
				selectedTaskStatus=lstTaskStatus.get(0);


			if(lstTaskPriority.size()>0)
				selectedTaskPriority=lstTaskPriority.get(0);


			if(lstProject.size()>0)
				lstSelectedProject=lstProject.get(0);

			if(lstService.size()>0)
				lstSelectedService=lstService.get(0);

			if(lstCustomerJob.size()>0)
				lstSelectedCustomerJob=lstCustomerJob.get(0);

			//online customer key for Explorer=2232 local 901			
			for(QbListsModel cust:lstCustomerJob)
			{
				if(cust.getRecNo()==2232)
				{
					lstSelectedCustomerJob=cust;
					break;
				}
			}
			

			if(taskKey>0)
			{
				forEditTaskOnly();
			}
			else
			{
				labelStatus="Create";

				taskDate=df.parse(sdf.format(c.getTime()));

				expectedDateTofinish=df.parse(sdf.format(c.getTime()));

				reminderDate=df.parse(sdf.format(c.getTime()));	

				taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());

				taskName="";

				taskKey=0;

				estimatedEffort=0;

				actualEffort=0;

				memo="";

				comments="";

				history="";

				taskStep="";

				cutomerContractExipryDate="No Data";

				customerStatusStr="No Data";

				for(HRListValuesModel hrListValuesModel:lstTaskStatus)
				{
					if(hrListValuesModel.getEnDescription().equalsIgnoreCase("Created"))
					{
						selectedTaskStatus=hrListValuesModel;
						break;
					}
				}


				if(otherformFalg)
				{
					forOtherFormOnly();
				}

				visbaleCustomerNAmefromFeedback=false;
				cutomerNamefromFeedBack="";

				if(creatoEmployeeIDTogetEmail!=null)
				{
					creatoEmployeeIDTogetEmail=companyData.getuserById(dbUser.getCompanyid(),dbUser.getUserid());
					creatorEmployeeId=creatoEmployeeIDTogetEmail.getEmployeeKey();
					creatorEmailadress=hrData.getEmployeeEmail(creatorEmployeeId);
				}
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> init", ex);			
		}
	}


	private void forEditTaskOnly()
	{
		try
		{
			labelStatus="Edit";
			lstLinkToPriviousTask=taskData.getAllTaskOtherThanCurrentTask(taskKey);
			if(lstLinkToPriviousTask!=null  && lstLinkToPriviousTask.size()>0)
			{
				selectedLinkToPreviousTask=lstLinkToPriviousTask.get(0);
			}
			selectedTask=taskData.getTaskById(taskKey);
			if(selectedTask!=null)
			{
				taskDate=selectedTask.getCreationDate();
				if(selectedTask.getExpectedDatetofinish()!=null)
					expectedDateTofinish=selectedTask.getExpectedDatetofinish();
				else
					expectedDateTofinish=df.parse(sdf.format(c.getTime()));	

				if(selectedTask.getReminderDate()!=null)
					reminderDate=selectedTask.getReminderDate();
				else
					reminderDate=df.parse(sdf.format(c.getTime()));	

				remindInNumber=selectedTask.getRemindIn();

				taskNumber=selectedTask.getTaskNumber();
				taskName=selectedTask.getTaskName();
				estimatedEffort=selectedTask.getEstimatatedNumber();
				actualEffort=selectedTask.getActualNumber();
				memo=selectedTask.getMemo();
				feedbackKeyForTaskRelation=selectedTask.getFeedbackKey();
				comments="";
				taskStep=selectedTask.getTaskStep();
				taskHistory=taskData.getTaskDeatils(taskKey);
				lstAtt=taskData.getTaskAttchamnet(taskKey);
				if(lstAtt!=null && lstAtt.size()>0)
					selectedAttchemnets=lstAtt.get(0);
				
				if(taskHistory.size()>0)
				{
				 lastUpdatedBy=taskHistory.get(0).getCreatedUserName();						 
				}
				//no need i get direct from query
				/*
				for(TasksModel tasksModel:taskHistory)
				{
					history=history+tasksModel.getComments()+"      Date:"+tasksModel.getCreationDateStr()+"\n \n";

					for(HRListValuesModel hrListValuesModel:lstTaskStatus)
					{
						if(hrListValuesModel.getListId()==tasksModel.getStatusKey())
						{
							tasksModel.setStatusName(hrListValuesModel.getEnDescription());
							break;
						}
					}


					for(WebusersModel model:lstUsers)
					{											
						if(model.getSupervisor()==tasksModel.getCreatedUserID() || model.getEmployeeKey()==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName(model.getFirstname());
							if(firstTimeLoop==100)
							{
								lastUpdatedBy=model.getFirstname();
								firstTimeLoop++;
							}
							break;
						}
						if(0==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName("Admin");
							break;
						}
					}
				}
				*/

				String cleintType=selectedTask.getClientType();
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

				createdBy=selectedTask.getCreatedUserName();
				//no need i get direct from query
				/*
				for(WebusersModel model:lstUsers)
				{
					if(model.getSupervisor()==selectedTask.getCreatedUserID() || model.getEmployeeKey()==selectedTask.getCreatedUserID())
					{
						selectedTask.setCreatedUserName(model.getFirstname());
						createdBy=model.getFirstname();
						break;
					}
					if(0==selectedTask.getCreatedUserID())
					{
						selectedTask.setCreatedUserName("Admin");
						createdBy="Admin";
						break;
					}
				}
				*/
				
				if(selectedClientType!=null)
				{
					if(selectedClientType.equalsIgnoreCase("Prospective Client"))
					{
						lstCustomerJob=hbadata.quotationPrecpectiveList();
					}
					else
					{
						lstCustomerJob=hbadata.fillQbList("'Customer'");
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskType)
				{
					if(hrListValuesModel.getListId()==selectedTask.getTaskTypeId())
					{
						selectedTaskType=hrListValuesModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskStatus)
				{
					if(hrListValuesModel.getListId()==selectedTask.getStatusKey())
					{
						selectedTaskStatus=hrListValuesModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskPriority)
				{
					if(hrListValuesModel.getListId()==selectedTask.getPriorityRefKey())
					{
						selectedTaskPriority=hrListValuesModel;
						break;
					}
				}

				for(ProjectModel projectModel:lstProject)
				{
					if(projectModel.getProjectKey()==selectedTask.getProjectKey())
					{
						lstSelectedProject=projectModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstService)
				{
					if(hrListValuesModel.getListId()==selectedTask.getSreviceId())
					{
						lstSelectedService=hrListValuesModel;
						break;
					}
				}

				for(QbListsModel customerModel:lstCustomerJob)
				{
					if(customerModel.getRecNo()==selectedTask.getCustomerRefKey())
					{
						lstSelectedCustomerJob=customerModel;
						break;
					}
				}

				for(EmployeeModel employeeModel:lstAssignToEmployees)
				{
					if(employeeModel.getEmployeeKey()==selectedTask.getEmployeeid())
					{
						selectedAssignToEmployee=employeeModel;
						break;
					}
				}

				for(EmployeeModel employeeModel:lstCCToEmployees)
				{
					if(employeeModel.getEmployeeKey()==selectedTask.getCcEmployeeKey())
					{
						selectedCCToEmployee=employeeModel;
						break;
					}
				}

				for(TasksModel tasksModel:lstLinkToPriviousTask)
				{
					if(tasksModel.getTaskid()==selectedTask.getPrviousTaskLinkId())
					{
						selectedLinkToPreviousTask=tasksModel;
						break;
					}
				}
				if(selectedTask.getHoursOrDays().equalsIgnoreCase("D"))
				{
					selectedHoursOrDays=hourOrDaysList.get(1);
				}
				else if(selectedHoursOrDays.equalsIgnoreCase("H"))
				{
					selectedHoursOrDays=hourOrDaysList.get(0);
				}

				if(selectedAssignToEmployee!=null)
				{
					//when user log in check if the task assigned to user and created task user is equal or not to disable the panel
					if((webUserID!=0 && selectedTask!=null && selectedTask.getCreatedUserID()!=0 && selectedTask.getCreatedUserID()==webUserID) || adminUser)
					{
						disableTaskCreatorPanel=false;
					}
					else if(selectedTask.getCreatedAutommaticTask()!=null && !selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("") && selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("Y"))
					{
						disableTaskCreatorPanel=false;
					}
					else
					{
						disableTaskCreatorPanel=true;
					}
					creatorEmailadress=hrData.getEmployeeEmail(selectedTask.getCreatedUserID());
				}

				if(selectedTask.getCreatedAutommaticTask()!=null && !selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("") && selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("Y"))
				{
					if(selectedTask.getCustomerNamefromFeedback()!=null)
						cutomerNamefromFeedBack=selectedTask.getCustomerNamefromFeedback();
					visbaleCustomerNAmefromFeedback=true;
				}
				else
				{
					cutomerNamefromFeedBack="";
					visbaleCustomerNAmefromFeedback=false;
				}

				if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
				{
					CustomerModel temp=null;
					temp= hbadata.getCustomerByKey(lstSelectedCustomerJob.getRecNo());
					customerStatus= hbadata.getCustomerStatusById(lstSelectedCustomerJob.getRecNo());
					if(temp!=null && temp.getCustkey()>0)
					{
						if(temp.getCustomerContactExpiryDateStr()!=null && !temp.getCustomerContactExpiryDateStr().equalsIgnoreCase(""))
							cutomerContractExipryDate=temp.getCustomerContactExpiryDateStr();
						else
							cutomerContractExipryDate="No Data";

					}
					else
					{
						cutomerContractExipryDate="No Data";
					}

					if(customerStatus!=null && customerStatus.size()>0)
					{
						for(CustomerStatusHistoryModel model:customerStatus)
						{
							if(model.getStatusDescription()!=null && !model.getStatusDescription().equalsIgnoreCase(""))
							{
								customerStatusStr=model.getStatusDescription() +" -- Date : "+ model.getActionDatstr();//most recent description.
								break;
							}
							else
							{
								customerStatusStr="No Data";
							}
						}
						// customerStatusStr=customerStatus.get(0).getStatusDescription();//most recent description.
					}
					else
					{
						customerStatusStr="No Data";
					}
				}


			}

		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> init", ex);			
		}
	}
	private void forOtherFormOnly()
	{
		{
			//to create task from other form with Pre populating  some data 
			String cleintType=customerTypeOtherForms;
			if(cleintType!=null)
			{
				if(cleintType.equalsIgnoreCase("C") || cleintType.equals("Customer"))
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
					lstCustomerJob=hbadata.quotationPrecpectiveList();
				}
				else
				{
					lstCustomerJob=hbadata.fillQbList("'Customer'");
				}
			}

			for(QbListsModel customerModel:lstCustomerJob)
			{
				if(customerModel.getRecNo()==customerKeyOtherForms)
				{
					lstSelectedCustomerJob=customerModel;
					break;
				}
			}
			memo=memoOtherForms;
			lstAtt=lstOtherFormAtt;

			if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
			{
				CustomerModel temp=null;
				temp= hbadata.getCustomerByKey(lstSelectedCustomerJob.getRecNo());
				customerStatus= hbadata.getCustomerStatusById(lstSelectedCustomerJob.getRecNo());
				if(temp!=null && temp.getCustkey()>0)
				{
					if(temp.getCustomerContactExpiryDateStr()!=null && !temp.getCustomerContactExpiryDateStr().equalsIgnoreCase(""))
						cutomerContractExipryDate=temp.getCustomerContactExpiryDateStr();
					else
						cutomerContractExipryDate="No Data";
				}
				else
				{
					cutomerContractExipryDate="No Data";
				}
				if(customerStatus!=null && customerStatus.size()>0)
				{
					for(CustomerStatusHistoryModel model:customerStatus)
					{
						if(model.getStatusDescription()!=null && !model.getStatusDescription().equalsIgnoreCase(""))
						{
							customerStatusStr=model.getStatusDescription() +" -- Date : "+ model.getActionDatstr();//most recent description.
							break;
						}
						else
						{
							customerStatusStr="No Data";
						}
					}
					// customerStatusStr=customerStatus.get(0).getStatusDescription();//most recent description.
				}
				else
				{
					customerStatusStr="No Data";
				}
			}

		}	
	}
	@Command
	@NotifyChange({"lstTask"})
	public void pepareTheGridForGroupOfTask()
	{
		try {
			int i=0;
			int taskNUmberInInterger=0;
			if(lstTask.size()>20 || totalNumber>20)
			{
				Clients.showNotification("Maximum 20 tasks are allowed at a time",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			if(lstTask.size()>0)
			{
				serailNo=lstTask.size();
				taskNUmberInInterger=Integer.parseInt(taskNumber);
				taskNUmberInInterger=taskNUmberInInterger+1;
				serailNo= serailNo+1;
				taskNumber=String.valueOf(taskNUmberInInterger);
			}
			else
			{
				serailNo=1;
				taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());
			}
			if(IsInt_ByRegex(taskNumber))
				taskNUmberInInterger=Integer.parseInt(taskNumber);
			else
				taskNUmberInInterger=0;	

			if(totalNumber>0)
			{
				for(i=0;i<totalNumber;i++)
				{

					TasksModel task=new TasksModel();

					task.setExpectedDatetofinish(df.parse(sdf.format(c.getTime())));
					task.setCreationDate(df.parse(sdf.format(c.getTime())));
					task.setReminderDate(df.parse(sdf.format(c.getTime())));	
					task.setSerialNumber(serailNo);
					task.setTaskNumber(String.valueOf(taskNUmberInInterger));
					taskNumber=String.valueOf(taskNUmberInInterger);
					taskNUmberInInterger=taskNUmberInInterger+1;
					serailNo=serailNo+1;


					if(lstLinkToPriviousTask.size()>0)
						task.setSelectedLinkToPreviousTask(lstLinkToPriviousTask.get(0));

					if(lstAssignToEmployees.size()>0)
						task.setSelectedAssignToEmployee(lstAssignToEmployees.get(0));

					if(lstCCToEmployees.size()>0)
						task.setSelectedCCToEmployee(lstCCToEmployees.get(0));

					if(lstTaskType.size()>0)
						task.setSelectedTaskType(lstTaskType.get(0));


					if(lstTaskStatus.size()>0)
						task.setSelectedTaskStatus(lstTaskStatus.get(0));


					if(lstTaskPriority.size()>0)
						task.setSelectedTaskPriority(lstTaskPriority.get(0));


					if(lstProject.size()>0)
						task.setLstSelectedProject(lstProject.get(0));

					if(lstService.size()>0)
						task.setLstSelectedService(lstService.get(0));

					if(lstCustomerJob.size()>0)
						task.setLstSelectedCustomerJob(lstCustomerJob.get(0));

					task.setSelectedClientType(listClientType.get(1));

					task.setSelectedHoursOrDays(hourOrDaysList.get(0));


					lstTask.add(task);
				}

				//tempTotalNUmber=totalNumber;

			}
			else
			{
				Clients.showNotification("The number should be greater than 0 ",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		}

		catch (Exception e) {
			logger.error("ERROR in EditTaskListViewModel ----> pepareTheGridForGroupOfTask", e);			
		}
	}


	private boolean IsInt_ByRegex(String str)
	{
		return str.matches("^-?\\d+$");
	}


	@Command
	@NotifyChange({"memo","comments"})
	public void moveMemoTocomments()
	{
		comments=comments+"\n"+memo;
	}

	@Command
	@NotifyChange({"disableSubOf"})
	public void loadImage()
	{
		Window win1 = (Window)Path.getComponent("/customerModalDialog");
		Window win = (Window)win1.getFellow("uploadWindow");
		String filePath="";
		Image image=(Image)win.getFellow("image");
		if(editPhotoPath!=null && !editPhotoPath.equalsIgnoreCase(""))
		{
			File file=new File(editPhotoPath);
			filePath=file.getAbsolutePath();
			BufferedImage image1=null;
			try {
				image1 = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				logger.error("ERROR in EditCustomerViewModel ----> loadImage", e);			
			}
			image.setContent(image1);
			image.setWidth("100px");
			image.setHeight("80px");
		}
		else
		{
			Clients.showNotification("There is no photo to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	@NotifyChange({"lstCustomers","footer","refreshTaskNumber"})
	public void saveTask(@BindingParam("cmp") Window x,@BindingParam("cmp1") boolean flag)
	{
		try 
		{ 
			String desc="";
			int type=0;
			if(taskDate==null)	
			{
				Messagebox.show("Task Date cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION);
				return;
			}

			if(expectedDateTofinish==null)	
			{
				Messagebox.show("Task Expected Task Date cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION);
				return;
			}

			if(taskNumber==null || taskNumber.equalsIgnoreCase(""))
			{
				Messagebox.show("Task Number cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(estimatedEffort==0)	//Estimated Time
			{
				Messagebox.show("Task Estimated days/hours cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(remindInNumber>estimatedEffort)
			{
				Messagebox.show("Task Reminder days/hours cannot be greater than Estimated time ..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(selectedTaskStatus!=null && selectedTaskStatus.getEnDescription().equalsIgnoreCase("Done") && actualEffort==0)
			{
				Messagebox.show("Task Actual days/hours cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(taskName==null || taskName.equalsIgnoreCase(""))
			{
				Messagebox.show("Task Name cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			/*if(selectedTaskType!=null && selectedTaskType.getFieldId()==0)
			{
				Messagebox.show("Task Type cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}*/

			if(selectedTaskPriority!=null && selectedTaskPriority.getFieldId()==0)
			{
				Messagebox.show("Task Priority cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(selectedTaskStatus!=null && selectedTaskStatus.getFieldId()==0)
			{
				Messagebox.show("Task Status cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()==0)
			{
				Messagebox.show("Task Customer cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			/*if(lstSelectedProject!=null && lstSelectedProject.getProjectKey()==0)
			{
				Messagebox.show("Task Project cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}*/

			/*if(lstSelectedService!=null && lstSelectedService.getFieldId()==0)
			{
				Messagebox.show("Task Service cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}*/

			if(lstAssignToEmployees!=null && selectedAssignToEmployee.getEmployeeKey()==0)
			{
				Messagebox.show("Task Assign to Employee cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}

			if(comments!=null && comments.trim().equalsIgnoreCase(""))
			{
				Messagebox.show("Task Comment cannot be empty..","Task",Messagebox.OK , Messagebox.INFORMATION); 
				return;
			}


			TasksModel tasksModel=new TasksModel();
			int result=0;
			String customerOrProspective="";

			if(taskKey>0)
				tasksModel.setTaskid(taskKey);
			else
				tasksModel.setTaskid(hbadata.getMaxID("Tasks", "taskId"));

			tasksModel.setTaskName(taskName);

			if((taskNumber!=null) && (taskData.checkIfTaskNumberIsDuplicate(taskNumber,tasksModel.getTaskid())==true))
			{
				Messagebox.show("Duplicate Task Number,click refresh to get the latest Task Number!","Task",Messagebox.OK,Messagebox.INFORMATION);
				refreshTaskNumber=true;
				return ; 
			}

			if(selectedClientType.equalsIgnoreCase("Prospective Client"))
			{
				tasksModel.setClientType("P");
				customerOrProspective="P";
			}
			else
			{
				tasksModel.setClientType("C");
				customerOrProspective="C";
			}

			if(selectedTaskType!=null)
				tasksModel.setTaskTypeId(selectedTaskType.getListId());
			else
				tasksModel.setTaskTypeId(0);  

			if(selectedTaskPriority!=null)
				tasksModel.setPriorityRefKey(selectedTaskPriority.getListId());
			else
				tasksModel.setPriorityRefKey(0);	  

			if(selectedTaskStatus!=null)
				tasksModel.setStatusKey(selectedTaskStatus.getListId());
			else
				tasksModel.setStatusKey(0);

			if(lstSelectedService!=null)
				tasksModel.setSreviceId(lstSelectedService.getListId());
			else
				tasksModel.setSreviceId(0);

			if(lstSelectedCustomerJob!=null)
				tasksModel.setCustomerRefKey(lstSelectedCustomerJob.getRecNo());
			else
				tasksModel.setCustomerRefKey(0);	  

			if(lstSelectedProject!=null)
				tasksModel.setProjectKey(lstSelectedProject.getProjectKey());
			else
				tasksModel.setProjectKey(0);

			if(selectedLinkToPreviousTask!=null)
				tasksModel.setPrviousTaskLinkId(selectedLinkToPreviousTask.getTaskid());
			else
				tasksModel.setPrviousTaskLinkId(0);

			if(lstAssignToEmployees!=null)
			{
				tasksModel.setEmployeeid(selectedAssignToEmployee.getEmployeeKey()); 
				employeeEmail=hrData.getEmployeeEmail(selectedAssignToEmployee.getEmployeeKey());
			}
			else
				tasksModel.setEmployeeid(0); 

			if(selectedCCToEmployee!=null)
			{
				tasksModel.setCcEmployeeKey(selectedCCToEmployee.getEmployeeKey());
				ccEmployeemEmail=hrData.getEmployeeEmail(selectedCCToEmployee.getEmployeeKey());
				if(ccEmployeemEmail!=null && ccEmployeemEmail.equalsIgnoreCase(""))
				{
					ccEmployeemEmail=null;
				}
			}
			else
				tasksModel.setCcEmployeeKey(0);

			if(selectedHoursOrDays.equalsIgnoreCase("Days"))
			{
				tasksModel.setHoursOrDays("D");
			}
			else if(selectedHoursOrDays.equalsIgnoreCase("Hours"))
			{
				tasksModel.setHoursOrDays("H");
			}

			if(taskKey>0)
			{

				if(selectedAssignToEmployee!=null)
				{
					//when user log in check if the task assigned to user and created task user is equal or not to disable the panel
					if(selectedTaskStatus.getListId()==11)//143 is predefine as task closed status 
					{
						if((webUserID!=0 && selectedTask!=null &&  selectedTask.getCreatedUserID()!=0 && selectedTask.getCreatedUserID()==webUserID) || adminUser)
						{
							disableTaskCreatorPanel=false;
						}
						else
						{
							disableTaskCreatorPanel=true;
							Messagebox.show("Task Can only be closed by the Creator of Task..","Task",Messagebox.OK , Messagebox.INFORMATION); 
							return;

						}
					}
				}
			}

			tasksModel.setMemo(memo);

			tasksModel.setComments(comments);

			tasksModel.setHistory(history);

			tasksModel.setCreationDate(taskDate);

			tasksModel.setExpectedDatetofinish(expectedDateTofinish);

			tasksModel.setReminderDate(reminderDate);

			tasksModel.setActualNumber(actualEffort);

			tasksModel.setEstimatatedNumber(estimatedEffort);

			tasksModel.setTaskStep(taskStep);

			tasksModel.setTaskNumber(taskNumber);

			//chang to save employeekey of login
			tasksModel.setCreatedUserID(webUserID);

			tasksModel.setRemindIn(remindInNumber);

			Calendar c = Calendar.getInstance();		

			tasksModel.setUpdatedTime(df.parse(sdf.format(c.getTime())));


			if(taskKey>0)
			{

				result=taskData.editTask(tasksModel,lstAtt);
				desc="Task With Following Details Has Been";
				type=2;
				Messagebox.show("Task Updated sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
			}
			else
			{
				result=taskData.addTask(tasksModel,lstAtt);
				desc="Task With Following Details Has Been";
				type=2;
				Messagebox.show("Task Created sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
			}
			if(result>0)
			{
				if(taskKey==0)
					hbadata.ConfigSerialNumberCashInvoice(SerialFields.Task, taskNumber,0);

				
				CustomerFeedbackModel customerFeedback =new CustomerFeedbackModel();
				customerFeedback.setTaskID(tasksModel.getTaskid());
				customerFeedback.setCustomerType(tasksModel.getClientType());
				customerFeedback.setCustomerRefKey(tasksModel.getCustomerRefKey());	  
				if(selectedTaskStatus!=null)
					customerFeedback.setTaskStatusId(selectedTaskStatus.getListId());
				else
					customerFeedback.setTaskStatusId(0);
				customerFeedback.setFeedbackCreateDate(df.parse(sdf.format(c.getTime())));
				customerFeedback.setUserId(dbUser.getUserid());
				customerFeedback.setFeedbackKey(feedbackKeyForTaskRelation);
				taskData.saveFeedbackCustomerRelation(customerFeedback);
				taskData.updateFeedbackTabelwithCustomer(customerFeedback);

				if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
				{
					CustomerStatusHistoryModel model =new CustomerStatusHistoryModel();  
					model.setRecNo(hbadata.getMaxID("CustomerStatusHistory", "RecNo"));
					model.setCustKey(lstSelectedCustomerJob.getRecNo());
					model.setActionDate(df.parse(sdf.format(c.getTime())));
					if(taskKey==0)
						model.setCreatedFrom("Create Task Form Online");
					else
						model.setCreatedFrom("Edit Task Form Online");
					model.setStatusDescription(comments);
					model.setType(customerOrProspective);
					model.setTxnRecNo(tasksModel.getTaskid());
					model.setTxnRefNumber(tasksModel.getTaskNumber());
					hbadata.saveCustomerStatusHistroyfromFeedback(model,webUserID,webuserName);
					hbadata.updateCustomerStatusDescription(model);
				}

				if(!employeeEmail.equals("") && !creatorEmailadress.equals(""))
				{
					if(flag)
					{
						if(!employeeEmail.equalsIgnoreCase(creatorEmailadress))
						employeeEmail=employeeEmail+","+creatorEmailadress;
						
						sendEmail(employeeEmail,desc,type);
						// sendEmail(creatorEmailadress, desc,type);
					}
				}
				else if(!employeeEmail.equals("") && creatorEmailadress.equals(""))
				{
					if(flag)
					{
						sendEmail(employeeEmail,desc,type);
					}
				}

				if(flag)
				{
					if(feedbackKeyForTaskRelation>0)
					{
						CustomerFeedbackModel selectedCustomerFeedBack=new CustomerFeedbackModel();
						selectedCustomerFeedBack=taskData.getCutsomerFeedbackById(feedbackKeyForTaskRelation);
						if(selectedCustomerFeedBack!=null)
						{
							if(selectedTaskStatus!=null && (selectedTaskStatus.getEnDescription().equalsIgnoreCase("In Progress") || selectedTaskStatus.getEnDescription().equalsIgnoreCase("Done")))//send to feedbACK REVIDED CUSTOMERS
							sendEmailToCustomerIfFeedbackExist(selectedCustomerFeedBack,desc,type);
						}

					}
				}



			}
			x.detach();
			Map args = new HashMap();
			BindUtils.postGlobalCommand(null, null, "refreshParentTaskForm", args);


		} catch (ParseException e) {
			logger.error("ERROR in refreshParentTaskForm ----> saveTask", e);
		}

	}


	public TasksModel getNext(TasksModel uid) {
		int idx = lstTask.indexOf(uid);
		if (idx < 0 || idx+1 == lstTask.size()) return null;
		return lstTask.get(idx + 1);
	}

	public TasksModel getPrevious(TasksModel uid) {
		int idx = lstTask.indexOf(uid);
		if (idx <= 0) return null;
		return lstTask.get(idx - 1);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	//@NotifyChange()
	public void savegroupOfTask(@BindingParam("cmp") Window x,@BindingParam("cmp1") boolean flag)
	{
		try { 
			if(lstTask!=null)
			{
				for(TasksModel iterationNUmberCheckTaskMOdel:lstTask)
				{
					if((iterationNUmberCheckTaskMOdel.getTaskNumber()!=null) && (taskData.checkIfTaskNumberIsDuplicate(iterationNUmberCheckTaskMOdel.getTaskNumber(),hbadata.getMaxID("Tasks", "taskId"))==true))
					{
						Messagebox.show("Duplicate Task Number at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+" !","Task",Messagebox.OK,Messagebox.INFORMATION);
						return ; 
					}
					TasksModel tempPreviosuTask=getPrevious(iterationNUmberCheckTaskMOdel);
					if(tempPreviosuTask!=null && tempPreviosuTask.getTaskNumber()==iterationNUmberCheckTaskMOdel.getTaskNumber())
					{
						Messagebox.show("Duplicate Task Number at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+" and "+tempPreviosuTask.getSerialNumber()+" !","Task",Messagebox.OK,Messagebox.INFORMATION);
						return ; 
					}

					if(iterationNUmberCheckTaskMOdel.getCreationDate()==null)	
					{
						Messagebox.show("Task Date cannot be empty..at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getExpectedDatetofinish()==null)	
					{
						Messagebox.show("Task Expected Task Date cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getTaskNumber()==null || iterationNUmberCheckTaskMOdel.getTaskNumber().equalsIgnoreCase(""))
					{
						Messagebox.show("Task Number cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getEstimatatedNumber()==0)	
					{
						Messagebox.show("Task Estimated days/hours cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					/*  if(remindInNumber>estimatedEffort)
					   {
						   Messagebox.show("Task Reminder days/hours cannot be greater than Estimated time ..","Task",Messagebox.OK , Messagebox.INFORMATION); 
						   return;
					   }*/

					if(iterationNUmberCheckTaskMOdel.getSelectedTaskStatus()!=null && iterationNUmberCheckTaskMOdel.getSelectedTaskStatus().getEnDescription().equalsIgnoreCase("Done") && iterationNUmberCheckTaskMOdel.getActualNumber()==0)
					{
						Messagebox.show("Task Actual days/hours cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getTaskName()==null || iterationNUmberCheckTaskMOdel.getTaskName().equalsIgnoreCase(""))
					{
						Messagebox.show("Task Name cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getSelectedTaskType()!=null && iterationNUmberCheckTaskMOdel.getSelectedTaskType().getFieldId()==0)
					{
						Messagebox.show("Task Type cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getSelectedTaskPriority()!=null && iterationNUmberCheckTaskMOdel.getSelectedTaskPriority().getFieldId()==0)
					{
						Messagebox.show("Task Priority cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getSelectedTaskStatus()!=null && iterationNUmberCheckTaskMOdel.getSelectedTaskStatus().getFieldId()==0)
					{
						Messagebox.show("Task Status cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getLstSelectedCustomerJob()!=null && iterationNUmberCheckTaskMOdel.getLstSelectedCustomerJob().getRecNo()==0)
					{
						Messagebox.show("Task Customer cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getLstSelectedProject()!=null && iterationNUmberCheckTaskMOdel.getLstSelectedProject().getProjectKey()==0)
					{
						Messagebox.show("Task Project cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getLstSelectedService()!=null && iterationNUmberCheckTaskMOdel.getLstSelectedService().getFieldId()==0)
					{
						Messagebox.show("Task Service cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getSelectedAssignToEmployee()!=null && iterationNUmberCheckTaskMOdel.getSelectedAssignToEmployee().getEmployeeKey()==0)
					{
						Messagebox.show("Task Assign to Employee cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}

					if(iterationNUmberCheckTaskMOdel.getComments()==null && iterationNUmberCheckTaskMOdel.getComments().equalsIgnoreCase(""))
					{
						Messagebox.show("Task Comment cannot be empty...at task serail No "+iterationNUmberCheckTaskMOdel.getSerialNumber()+"","Task",Messagebox.OK , Messagebox.INFORMATION); 
						return;
					}
				}

				if(creatoEmployeeIDTogetEmail!=null)
				{
					creatoEmployeeIDTogetEmail=companyData.getuserById(dbUser.getCompanyid(),dbUser.getUserid());
					creatorEmployeeId=creatoEmployeeIDTogetEmail.getEmployeeKey();
					creatorEmailadress=hrData.getEmployeeEmail(creatorEmployeeId);
				}
				for(TasksModel iterationTaskMOdel:lstTask)
				{

					String desc="";
					int type=0;
					taskKey=0;

					TasksModel tasksModel=new TasksModel();
					int result=0;

					if(taskKey>0)
						tasksModel.setTaskid(taskKey);
					else
						tasksModel.setTaskid(hbadata.getMaxID("Tasks", "taskId"));

					tasksModel.setTaskName(iterationTaskMOdel.getTaskName());

					if((iterationTaskMOdel.getTaskNumber()!=null) && (taskData.checkIfTaskNumberIsDuplicate(iterationTaskMOdel.getTaskNumber(),hbadata.getMaxID("Tasks", "taskId"))==true))
					{
						Messagebox.show("Duplicate Task Number at task serail No "+iterationTaskMOdel.getSerialNumber()+" the records previous to this serial number has already been already saved,please close and re-enter again. !","Task",Messagebox.OK,Messagebox.INFORMATION);
						return ; 
					}

					if(iterationTaskMOdel.getSelectedClientType().equalsIgnoreCase("Prospective Client"))
					{
						tasksModel.setClientType("P");
					}
					else
					{
						tasksModel.setClientType("C");
					}

					if(iterationTaskMOdel.getSelectedTaskType()!=null)
						tasksModel.setTaskTypeId(iterationTaskMOdel.getSelectedTaskType().getListId());
					else
						tasksModel.setTaskTypeId(0);  

					if(iterationTaskMOdel.getSelectedTaskPriority()!=null)
						tasksModel.setPriorityRefKey(iterationTaskMOdel.getSelectedTaskPriority().getListId());
					else
						tasksModel.setPriorityRefKey(0);	  

					if(iterationTaskMOdel.getSelectedTaskStatus()!=null)
						tasksModel.setStatusKey(iterationTaskMOdel.getSelectedTaskStatus().getListId());
					else
						tasksModel.setStatusKey(0);

					if(iterationTaskMOdel.getLstSelectedService()!=null)
						tasksModel.setSreviceId(iterationTaskMOdel.getLstSelectedService().getListId());
					else
						tasksModel.setSreviceId(0);

					if(iterationTaskMOdel.getLstSelectedCustomerJob()!=null)
						tasksModel.setCustomerRefKey(iterationTaskMOdel.getLstSelectedCustomerJob().getRecNo());
					else
						tasksModel.setCustomerRefKey(0);	  

					if(iterationTaskMOdel.getLstSelectedProject()!=null)
						tasksModel.setProjectKey(iterationTaskMOdel.getLstSelectedProject().getProjectKey());
					else
						tasksModel.setProjectKey(0);

					if(iterationTaskMOdel.getSelectedLinkToPreviousTask()!=null)
						tasksModel.setPrviousTaskLinkId(iterationTaskMOdel.getSelectedLinkToPreviousTask().getTaskid());
					else
						tasksModel.setPrviousTaskLinkId(0);

					if(iterationTaskMOdel.getSelectedAssignToEmployee()!=null)
					{
						tasksModel.setEmployeeid(iterationTaskMOdel.getSelectedAssignToEmployee().getEmployeeKey()); 
						employeeEmail=hrData.getEmployeeEmail(iterationTaskMOdel.getSelectedAssignToEmployee().getEmployeeKey());
					}
					else
						tasksModel.setEmployeeid(0); 

					if(iterationTaskMOdel.getSelectedCCToEmployee()!=null)
					{
						tasksModel.setCcEmployeeKey(iterationTaskMOdel.getSelectedCCToEmployee().getEmployeeKey());
						ccEmployeemEmail=hrData.getEmployeeEmail(iterationTaskMOdel.getSelectedCCToEmployee().getEmployeeKey());
						if(ccEmployeemEmail!=null && ccEmployeemEmail.equalsIgnoreCase(""))
						{
							ccEmployeemEmail=null;
						}
					}
					else
						tasksModel.setCcEmployeeKey(0);

					if(iterationTaskMOdel.getSelectedHoursOrDays().equalsIgnoreCase("Days"))
					{
						tasksModel.setHoursOrDays("D");
					}
					else if(iterationTaskMOdel.getSelectedHoursOrDays().equalsIgnoreCase("Hours"))
					{
						tasksModel.setHoursOrDays("H");
					}

					if(taskKey>0)
					{

						if(iterationTaskMOdel.getSelectedAssignToEmployee()!=null)
						{
							//when user log in check if the task assigned to user and created task user is equal or not to disable the panel
							if(iterationTaskMOdel.getSelectedTaskStatus().getListId()==11)//143 is predefine as task closed status 
							{
								if((webUserID!=0 && iterationTaskMOdel!=null &&  iterationTaskMOdel.getCreatedUserID()!=0 && iterationTaskMOdel.getCreatedUserID()==webUserID) || adminUser)
								{
									disableTaskCreatorPanel=false;
								}
								else
								{
									disableTaskCreatorPanel=true;
									Messagebox.show("Task Can only be closed by the Creator of Task..","Task",Messagebox.OK , Messagebox.INFORMATION); 
									return;

								}
							}
						}
					}

					tasksModel.setMemo(iterationTaskMOdel.getMemo());

					tasksModel.setComments(iterationTaskMOdel.getComments());

					tasksModel.setHistory(iterationTaskMOdel.getHistory());

					tasksModel.setCreationDate(iterationTaskMOdel.getCreationDate());

					tasksModel.setExpectedDatetofinish(iterationTaskMOdel.getExpectedDatetofinish());

					tasksModel.setActualNumber(iterationTaskMOdel.getActualNumber());

					tasksModel.setEstimatatedNumber(iterationTaskMOdel.getEstimatatedNumber());

					tasksModel.setTaskStep(iterationTaskMOdel.getTaskStep());

					tasksModel.setTaskNumber(iterationTaskMOdel.getTaskNumber());

					tasksModel.setCreatedUserID(webUserID);

					tasksModel.setRemindIn(iterationTaskMOdel.getRemindIn());

					tasksModel.setReminderDate(iterationTaskMOdel.getReminderDate());



					Calendar c = Calendar.getInstance();		

					tasksModel.setUpdatedTime(df.parse(sdf.format(c.getTime())));


					if(taskKey>0)
					{

						result=taskData.editTask(tasksModel,iterationTaskMOdel.getListOfattchments());
						desc="Task With Following Details Has Been";
						type=2;
						//Messagebox.show("Task Updated sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
					}
					else
					{
						result=taskData.addTask(tasksModel,iterationTaskMOdel.getListOfattchments());
						desc="Task With Following Details Has Been";
						type=2;

					}
					if(result>0)
					{
						if(taskKey==0)
							hbadata.ConfigSerialNumberCashInvoice(SerialFields.Task, tasksModel.getTaskNumber(),0);
						if(!employeeEmail.equals("") && !creatorEmailadress.equals(""))
						{
							if(flag)
							{
								employeeEmail=employeeEmail+","+creatorEmailadress;
								sendEmail(employeeEmail,desc,type);
								// sendEmail(creatorEmailadress, desc,type);
							}
						}
						else if(!employeeEmail.equals("") && creatorEmailadress.equals(""))
						{
							if(flag)
							{
								sendEmail(employeeEmail,desc,type);
							}
						}

					}

				}

				x.detach();
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentTaskForm", args);
				Clients.showNotification("All the task are sucessfully saved",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			else
			{
				Clients.showNotification("There Is no data to save",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}


		} catch (ParseException e) {
			logger.error("ERROR in refreshParentTaskForm ----> saveTask", e);
		}

	}

	@Command
	public void openFeedback(@BindingParam("feedBackId") int feedBackKey)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("feedBackKey", feedBackKey);
			arg.put("compKey",0);
			arg.put("type","view");
			Executions.createComponents("/crm/editCustomerFeedback.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> openFeedback", ex);			
		}
	}


	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	public boolean isActiveCustomer() {
		return activeCustomer;
	}

	public void setActiveCustomer(boolean activeCustomer) {
		this.activeCustomer = activeCustomer;
	}

	public String getAttFile4() {
		return attFile4;
	}

	public void setAttFile4(String attFile4) {
		this.attFile4 = attFile4;
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
				Clients.showNotification("You can upload maximum 10 files per task.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
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
			logger.info("task dirPath >>>>>> " + dirPath);
			/*File dir = new File(dirPath);
		if(!dir.exists())
			dir.mkdirs();*/
			filePath=repository+"Tasks"+File.separator+taskNumber+File.separator+event.getMedia().getName();	 
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
			logger.error("ERROR in EditTaskListViewModel ----> uploadFile", e);			
		}
	}



	@SuppressWarnings("unused")
	@Command 
	@NotifyChange({"attFile4","lstTask"})
	public void uploadFileGroupOftasks(BindContext ctx,@BindingParam("attId") String attId,@BindingParam("row") TasksModel row )
	{
		try {
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
			if(row.getListOfattchments()!=null && row.getListOfattchments().size()>=10)
			{
				Clients.showNotification("The you can upload maximum 10 images per task.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;

			}

			for(QuotationAttachmentModel attachmentModel:row.getListOfattchments())
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
			filePath=repository+"Tasks"+File.separator+row.getTaskNumber()+File.separator+event.getMedia().getName();	 
			if(attId.equals("4"))
			{
				attFile4=event.getMedia().getName();
				QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
				objAtt.setFilename(attFile4);
				objAtt.setFilepath(filePath);
				objAtt.setSessionid(sessID);
				objAtt.setImageMedia(event.getMedia());
				objAtt.setSerialNumber(row.getSerialNumber());
				row.getListOfattchments().add(objAtt);
				if(row.getListOfattchments()!=null && row.getListOfattchments().size()>0)
					row.setSelectedAttchemnets(row.getListOfattchments().get(0));
			}
		}
		catch (Exception e) {
			logger.error("ERROR in EditTaskListViewModel ----> uploadFileGroupOftasks", e);			
		}
	}



	@Command 
	@NotifyChange({"attFile4","lstTask"})
	public void deleteFromAttchamentListGroupOftask(@BindingParam("row") QuotationAttachmentModel obj)
	{
		try {
			QuotationAttachmentModel tempModel=new QuotationAttachmentModel();
			for(TasksModel tasksModel:lstTask)
			{
				if(tasksModel.getSerialNumber()==obj.getSerialNumber())
				{
					for(QuotationAttachmentModel attachmentModel:tasksModel.getListOfattchments())
					{
						if(attachmentModel.getFilename().equalsIgnoreCase(obj.getFilename()))
						{
							tempModel=attachmentModel;
							break;
						}

					}
					tasksModel.getListOfattchments().remove(tempModel);
					break;
				}

			}
		}
		catch (Exception e) {
			logger.error("ERROR in EditTaskListViewModel ----> deleteFromAttchamentListGroupOftask", e);			
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
			logger.error("ERROR in EditTaskListViewModel ----> deleteFromAttchamentList", e);			
		}
	}


	@Command
	public void download(@BindingParam("row") QuotationAttachmentModel obj)
	{
		if(obj.getFilepath()!=null && !obj.getFilepath().equalsIgnoreCase(""))
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
				logger.error("ERROR in EditTaskListViewModel ----> download", e);	
			}

		}
		else
		{
			Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	}



	@Command
	public void addTaskCustomers()
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
			logger.error("ERROR in EditTaskListViewModel ----> addTaskCustomers", ex);			
		}
	}



	@Command
	public void addTaskQuotation()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("custKey", 0);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/hba/list/listOfQuotationForTask.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> addTaskQuotation", ex);			
		}
	}


	@Command
	public void customerInfoInEachGroupOfTask(@BindingParam("row") TasksModel row)
	{
		try
		{
			if(selectedClientType!=null)
			{
				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
				{
					/* if(row!=null && row.getLstSelectedCustomerJob()!=null && row.getLstSelectedCustomerJob().getRecNo()==0)
						   {

						   }

						 	this.setProspect(prospective);
							arg.put("prospectives",prospective);
							window = (Window) Executions.createComponents("/hba/list/prospectiveDetails.zul",null,arg);
							window.doModal();*/

					Clients.showNotification("Prospective view is Under implementation.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					return;
				}
				else
				{
					if(row!=null && row.getLstSelectedCustomerJob()!=null && row.getLstSelectedCustomerJob().getRecNo()==0)
					{
						Clients.showNotification("Please select the Customer.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						return;
					}

					Map<String,Object> arg = new HashMap<String,Object>();
					arg.put("custKey", row.getLstSelectedCustomerJob().getRecNo());
					arg.put("compKey",0);
					arg.put("type","view");
					Executions.createComponents("/hba/list/editcustomer.zul", null,arg);
				}
			}

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> customerInfoInEachGroupOfTask", ex);			
		}
	}


	@Command
	@NotifyChange({"lstCustomerJob"})
	public void selectedCustomerTypeInfoInEachGroupOfTask(@BindingParam("row") TasksModel row)
	{
		try
		{
			if(row!=null && row.getSelectedClientType()!=null && row.getSelectedClientType().equalsIgnoreCase("Prospective Client"))
			{
				lstCustomerJob=hbadata.quotationPrecpectiveList();
			}
			else
			{
				lstCustomerJob=hbadata.fillQbList("'Customer'");
			}

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> customerInfoInEachGroupOfTask", ex);			
		}
	}


	@Command
	public void customerInfoInEachEditTask()
	{
		try
		{

			if(selectedClientType!=null)
			{
				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
				{



					if((lstSelectedCustomerJob==null))
					{
						Clients.showNotification("Please select the Prospective.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						return;
					}

					if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()==0)
					{
						Clients.showNotification("Please select the Prospective.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						return;
					}

					Map<String, Object> arg = new HashMap<String, Object>();
					arg.put("prospectiveKey", lstSelectedCustomerJob.getRecNo());
					arg.put("compKey", 0);
					arg.put("type", "view");
					Executions.createComponents("/hba/list/editProspective.zul", null,arg);

				}
				else
				{
					if(lstSelectedCustomerJob==null)
					{
						Clients.showNotification("Please select the Customer.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						return;
					}

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
			logger.error("ERROR in EditTaskListViewModel ----> customerInfoInEachEditTask", ex);			
		}
	}




	public List<QuotationAttachmentModel> getLstAtt() {
		return lstAtt;
	}

	public void setLstAtt(List<QuotationAttachmentModel> lstAtt) {
		this.lstAtt = lstAtt;
	}

	public TasksModel getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(TasksModel selectedTask) {
		this.selectedTask = selectedTask;
	}

	public String getEditPhotoPath() {
		return editPhotoPath;
	}

	public void setEditPhotoPath(String editPhotoPath) {
		this.editPhotoPath = editPhotoPath;
	}

	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public List<HRListValuesModel> getLstTaskType() {
		return lstTaskType;
	}

	public void setLstTaskType(List<HRListValuesModel> lstTaskType) {
		this.lstTaskType = lstTaskType;
	}

	public HRListValuesModel getSelectedTaskType() {
		return selectedTaskType;
	}

	@NotifyChange({"memo"})
	public void setSelectedTaskType(HRListValuesModel selectedTaskType) {
		this.selectedTaskType = selectedTaskType;
		if(selectedTaskType!=null && selectedTaskType.getNotes()!=null)
			memo=memo+"\n"+selectedTaskType.getNotes();
	}

	public HRListValuesModel getSelectedTaskStatus() {
		return selectedTaskStatus;
	}

	public void setSelectedTaskStatus(HRListValuesModel selectedTaskStatus) {
		this.selectedTaskStatus = selectedTaskStatus;
	}

	public List<HRListValuesModel> getLstTaskPriority() {
		return lstTaskPriority;
	}

	public void setLstTaskPriority(List<HRListValuesModel> lstTaskPriority) {
		this.lstTaskPriority = lstTaskPriority;
	}

	public HRListValuesModel getSelectedTaskPriority() {
		return selectedTaskPriority;
	}

	public void setSelectedTaskPriority(HRListValuesModel selectedTaskPriority) {
		this.selectedTaskPriority = selectedTaskPriority;
	}

	public List<ProjectModel> getLstProject() {
		return lstProject;
	}

	public void setLstProject(List<ProjectModel> lstProject) {
		this.lstProject = lstProject;
	}

	public ProjectModel getLstSelectedProject() {
		return lstSelectedProject;
	}

	public void setLstSelectedProject(ProjectModel lstSelectedProject) {
		this.lstSelectedProject = lstSelectedProject;
	}

	public List<QbListsModel> getLstCustomerJob() {
		return lstCustomerJob;
	}

	public void setLstCustomerJob(List<QbListsModel> lstCustomerJob) {
		this.lstCustomerJob = lstCustomerJob;
	}

	public QbListsModel getLstSelectedCustomerJob() {
		return lstSelectedCustomerJob;
	}

	@NotifyChange({"lstSelectedCustomerJob","cutomerContractExipryDate","customerStatusStr","customerStatus"})
	public void setLstSelectedCustomerJob(QbListsModel lstSelectedCustomerJob) {
		if(lstSelectedCustomerJob!=null)
		{
			if(lstSelectedCustomerJob.getRecNo()==0)
			{
				Clients.showNotification("Invalid Cutomer Name",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				this.lstSelectedCustomerJob=null;
				return;
			}
			else
			{
				this.lstSelectedCustomerJob = lstSelectedCustomerJob;
			}

			if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
			{
				CustomerModel temp=null;
				temp= hbadata.getCustomerByKey(lstSelectedCustomerJob.getRecNo());
				customerStatus= hbadata.getCustomerStatusById(lstSelectedCustomerJob.getRecNo());
				if(temp!=null && temp.getCustkey()>0)
				{
					if(temp.getCustomerContactExpiryDateStr()!=null && !temp.getCustomerContactExpiryDateStr().equalsIgnoreCase(""))
						cutomerContractExipryDate=temp.getCustomerContactExpiryDateStr();
					else
						cutomerContractExipryDate="No Data";
				}
				else
				{
					cutomerContractExipryDate="No Data";
				}
				if(customerStatus!=null && customerStatus.size()>0)
				{
					for(CustomerStatusHistoryModel model:customerStatus)
					{
						if(model.getStatusDescription()!=null && !model.getStatusDescription().equalsIgnoreCase(""))
						{
							customerStatusStr=model.getStatusDescription() +" -- Date : "+ model.getActionDatstr();//most recent description.
							break;
						}
						else
						{
							customerStatusStr="No Data";
						}
					}
					// customerStatusStr=customerStatus.get(0).getStatusDescription();//most recent description.
				}
				else
				{
					customerStatusStr="No Data";
				}

			}
		}

	}

	public List<HRListValuesModel> getLstService() {
		return lstService;
	}

	public void setLstService(List<HRListValuesModel> lstService) {
		this.lstService = lstService;
	}

	public HRListValuesModel getLstSelectedService() {
		return lstSelectedService;
	}

	public void setLstSelectedService(HRListValuesModel lstSelectedService) {
		this.lstSelectedService = lstSelectedService;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public double getEstimatedEffort() {
		return estimatedEffort;
	}

	public void setEstimatedEffort(double estimatedEffort) {
		this.estimatedEffort = estimatedEffort;
	}

	public double getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(double actualEffort) {
		this.actualEffort = actualEffort;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getTaskStep() {
		return taskStep;
	}

	public void setTaskStep(String taskStep) {
		this.taskStep = taskStep;
	}

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

	public List<TasksModel> getLstLinkToPriviousTask() {
		return lstLinkToPriviousTask;
	}

	public void setLstLinkToPriviousTask(List<TasksModel> lstLinkToPriviousTask) {
		this.lstLinkToPriviousTask = lstLinkToPriviousTask;
	}

	public TasksModel getSelectedLinkToPreviousTask() {
		return selectedLinkToPreviousTask;
	}

	public void setSelectedLinkToPreviousTask(TasksModel selectedLinkToPreviousTask) {
		this.selectedLinkToPreviousTask = selectedLinkToPreviousTask;
	}

	public List<HRListValuesModel> getLstTaskStatus() {
		return lstTaskStatus;
	}

	public void setLstTaskStatus(List<HRListValuesModel> lstTaskStatus) {
		this.lstTaskStatus = lstTaskStatus;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public List<EmployeeModel> getLstCCToEmployees() {
		return lstCCToEmployees;
	}

	public void setLstCCToEmployees(List<EmployeeModel> lstCCToEmployees) {
		this.lstCCToEmployees = lstCCToEmployees;
	}

	public EmployeeModel getSelectedCCToEmployee() {
		return selectedCCToEmployee;
	}

	public void setSelectedCCToEmployee(EmployeeModel selectedCCToEmployee) {
		this.selectedCCToEmployee = selectedCCToEmployee;
	}

	public List<String> getHourOrDaysList() {
		return hourOrDaysList;
	}

	public void setHourOrDaysList(List<String> hourOrDaysList) {
		this.hourOrDaysList = hourOrDaysList;
	}

	public String getSelectedHoursOrDays() {
		return selectedHoursOrDays;
	}

	public void setSelectedHoursOrDays(String selectedHoursOrDays) {
		this.selectedHoursOrDays = selectedHoursOrDays;
	}



	public QuotationAttachmentModel getSelectedAttchemnets() {
		return selectedAttchemnets;
	}

	public void setSelectedAttchemnets(QuotationAttachmentModel selectedAttchemnets) {
		this.selectedAttchemnets = selectedAttchemnets;
	}



	public List<TasksModel> getTaskHistory() {
		return taskHistory;
	}

	public void setTaskHistory(List<TasksModel> taskHistory) {
		this.taskHistory = taskHistory;
	}




	public Date getExpectedDateTofinish() {
		return expectedDateTofinish;
	}




	public void setExpectedDateTofinish(Date expectedDateTofinish) {
		this.expectedDateTofinish = expectedDateTofinish;
	}




	public double getRemindInNumber() {
		return remindInNumber;
	}




	public void setRemindInNumber(double remindInNumber) {
		this.remindInNumber = remindInNumber;
	}




	public boolean isDisableTaskCreatorPanel() {
		return disableTaskCreatorPanel;
	}

	public void setDisableTaskCreatorPanel(boolean disableTaskCreatorPanel) {
		this.disableTaskCreatorPanel = disableTaskCreatorPanel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendEmail(String toMail,String leaveDescription,int type)
	{
		try
		{
			String[] to =null;
			to= toMail.split(",");	
			MailClient mc = new MailClient();
			String subject="Task Status By "+dbUser.getFirstname()+"";
			StringBuffer result=null;
			result=new StringBuffer();
			result.append("<table border='0'>");
			String address="";

			if(lstSelectedCustomerJob!=null)
			{
				if(lstSelectedCustomerJob.getListType()!=null && !lstSelectedCustomerJob.getListType().equalsIgnoreCase(""))
				{
					CashInvoiceModel obj=hbadata.getCashInvoiceCusomerInfo(lstSelectedCustomerJob.getListType(), lstSelectedCustomerJob.getRecNo());	

					if(lstSelectedCustomerJob.getListType().equalsIgnoreCase("Prospective"))
					{
						if(obj.getShipToAddress()!=null && !obj.getShipToAddress().equalsIgnoreCase(""))
							address=obj.getShipToAddress();
						else
							address="";

						jobType=lstSelectedCustomerJob.getListType();

					}
					else
					{
						if(obj.getBillAddress5()!=null && !obj.getBillAddress5().equalsIgnoreCase(""))
							address=obj.getBillAddress5();
						else
							address="";

						if(obj.getCustomerContactExpiryDateStr()!=null)
							customerExpiry=obj.getCustomerContactExpiryDateStr();
						else
							customerExpiry="";

						if(obj.getCustomerCreatedDate()!=null)
							customerCreated=obj.getCustomerCreatedDate();
						else
							customerCreated="";


						if(obj.getCustomerCreatedDate()!=null)
							balcklisted=obj.getCustomerCreatedDate();
						else
							customerCreated="";

						if(obj.getBalckListed()!=null)
							balcklisted=obj.getBalckListed();
						else
							balcklisted="";

						if(obj.getStatus()!=null)
							active=obj.getStatus();
						else
							active="";


						jobType=lstSelectedCustomerJob.getListType();



					}
				}
			}



			if(emailcompSetup!=null && emailcompSetup.getActivateEmail()!=null && emailcompSetup.getActivateEmail().equalsIgnoreCase("Y"))
			{
				if(type==2)//send email 
				{		  		


					result.append("<p><strong>From :&nbsp;</strong><strong style='line-height:1.6'>&nbsp;</strong><span style='line-height:1.6'>"+dbUser.getFirstname()+"</span></p>");

					//result.append("<p><strong>To :&nbsp;</strong><strong style='font-size:13px; line-height:1.6'>Employee Name </strong><span style='font-size:13px; line-height:1.6'>: Mohamad Iqbal Sab ,&nbsp;</span><strong style='font-size:13px; line-height:1.6'>Employee Number</strong><span style='font-size:13px; line-height:1.6'> : 5</span></p>");

					if(taskKey>0)
					{
						if(selectedTask.getCreatedUserID()!=webUserID)// in email from and to changes 
						{
							EmployeeModel empDetailsEditEmial=new EmployeeModel();
							empDetailsEditEmial=hrData.GetEmployeeDeatailsByEmployeeKeyQuery(selectedTask.getCreatedUserID());
							if(empDetailsEditEmial!=null && empDetailsEditEmial.getEmployeeKey()>0)
							{

								result.append("<p><strong>To :&nbsp;</strong><strong>Employee Name </strong><span >: "+empDetailsEditEmial.getFullName()+ " ,&nbsp;</span><strong>Employee Number</strong><span > : "+empDetailsEditEmial.getEmployeeNo()+ "</span></p>");

							}
							else
							{

								result.append("<p><strong>To :&nbsp;</strong><strong>Employee Name </strong><span >: Admin ,&nbsp;</span></p>");

							}
						}
						else
						{


							result.append("<p><strong>To :&nbsp;</strong><strong>Employee Name </strong><span >: "+selectedAssignToEmployee.getFullName()+ " ,&nbsp;</span><strong>Employee Number</strong><span > :  "+selectedAssignToEmployee.getEmployeeNo()+ "</span></p>");

						}
					}
					else
					{
						result.append("<p><strong>To :&nbsp;</strong><strong>Employee Name </strong><span >: "+selectedAssignToEmployee.getFullName()+ " ,&nbsp;</span><strong>Employee Number</strong><span > :  "+selectedAssignToEmployee.getEmployeeNo()+ "</span></p>");

					}



					if(taskKey>0)
					{

						result.append("<hr />");
						result.append("<p><span>"+leaveDescription+ "</span><strong> Updated </strong><span >.</span></p>");

						result.append("<hr />");

					}
					else
					{
						result.append("<hr />");
						result.append("<p><span >"+leaveDescription+ "</span><strong> Created </strong><span >.</span></p>");

						result.append("<hr />");

					}

					result.append("<p><strong>Task Name : </strong><span >"+taskName+"&nbsp;</span></p>");

					result.append("<p><strong>Task Number : </strong>"+taskNumber+"&nbsp; &nbsp; &nbsp; &nbsp;</p>");
					if(estimatedEffort>0)
						result.append("<p><span style='color:#FF0000'><strong>Estimated "+selectedHoursOrDays+"  : "+estimatedEffort+"</strong></span></p>");
					if(actualEffort>0)

						result.append("<p><span style='color:#FF0000'><strong>Actual "+selectedHoursOrDays+"  : "+actualEffort+"</strong></span></p>");

					result.append("<p><span style='color:#FF0000'><strong>Task Status : "+selectedTaskStatus.getEnDescription()+"&nbsp;</strong></span></p>");

					result.append("<p><strong>Task Priority : </strong>"+selectedTaskPriority.getEnDescription()+"</p>");

					result.append("<p><strong>Task Type :&nbsp;</strong>"+selectedTaskType.getEnDescription()+"</p>");

					result.append("<p><strong>Service Type :&nbsp;</strong>"+lstSelectedService.getEnDescription()+"</p>");

					result.append("<p><strong>Project Name :&nbsp;</strong>"+lstSelectedProject.getProjectName()+"</p>");

					result.append("<p><strong>Job To :&nbsp;</strong><span >"+lstSelectedCustomerJob.getFullName()+"</span></p>");

					result.append("<p><u><strong>Job Info</strong></u></p>");

					result.append("<p>Type - "+jobType+"</p>");
					result.append("<p><span >Created Date&nbsp;- <strong>"+customerCreated+"</strong></span></p>");

					result.append("<p>Expiry Date -<strong>"+customerExpiry+"</strong></p>");

					result.append("<p>Balck Listed ? -<strong>"+balcklisted+"</strong></p>");

					result.append("<p>Status -<strong>"+active+"</strong></p>");

					result.append("<p><span >"+address+"</span></p>");

					result.append("<p><u><strong>Memo</strong></u></p>");

					result.append("<p>"+memo+"&nbsp;</p>");
					result.append("<p><u><strong>Recent comment</strong></u></p>");

					result.append("<p>"+comments+"&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</p>");

					result.append("<p>&nbsp;</p>");

					result.append("<p><strong>Company&nbsp;</strong></p>");

					result.append("<p><strong>"+selectedCompany.getEnCompanyName()+"</strong></p>");
				}

				String messageBody=result.toString();	
				String[] bcc =null;

				if(ccEmployeemEmail==null)
					ccEmployeemEmail="hinawi@eim.ae";
				else if(!ccEmployeemEmail.contains("hinawi@eim.ae"))
					ccEmployeemEmail+=",hinawi@eim.ae";				
				
				String cc[]=ccEmployeemEmail.split(","); //{ccEmployeemEmail};
				ArrayList fileArray = new ArrayList();
				for(QuotationAttachmentModel attPath:lstAtt)
				{
					File dir = new File(attPath.getFilepath());
					if(dir.exists())
						fileArray.add(attPath.getFilepath());
				}
				mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,fileArray,true,"Task","");

			}

			//mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, null);
		}
		catch (Exception e) 
		{
			logger.info("error at EditTaskListViewModel----> sendEmail" + e);
		}
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void sendEmailToCustomerIfFeedbackExist(CustomerFeedbackModel feedbackModel,String leaveDescription,int type)
	{
		try
		{
			if(feedbackModel!=null)
			{
				String[] to =null;
				String toMail=feedbackModel.getEmail();
				to= toMail.split(",");	
				MailClient mc = new MailClient();
				String subject="Feedback Status";
				StringBuffer result=null;
				result=new StringBuffer();
				result.append("<p>Dear Customer,</p>");
				result.append("<p>Thank you for your feedback submitted with the following info.&nbsp;</p>");
				result.append("<p><strong style='font-size:12px; line-height:1.6'>FeedBack Number :- "+feedbackModel.getFeedbackNUmber()+"</strong></p>");
				result.append("<p><span style='font-size:12px'><strong>Created Task Number For FeedBack &nbsp;:- "+taskNumber+"</strong></span></p>");
				result.append("<p><strong style='font-size:12px; line-height:1.6'>Sumited &nbsp;Comapny Name :-  "+feedbackModel.getCompanyName()+"</strong></p>");
				result.append("<p><span style='font-size:12px'><strong>Sumited Contact Person Name :- "+feedbackModel.getContactPersonName()+"</strong></span></p>");
				result.append("<p><span style='font-size:12px'><strong>Summited Email Address &nbsp;:- "+feedbackModel.getEmail()+"</strong></span></p>");
				result.append("<p><span style='font-size:12px'><strong>Memo :-  "+memo+"</strong></span></p>");
				result.append("<p><strong><span style='color:rgb(255, 0, 0)'>Status &nbsp;:- "+selectedTaskStatus.getEnDescription()+"</span></strong></p>");
				if(selectedTaskStatus.getEnDescription().equalsIgnoreCase("In Progress"))
					result.append("<p><span style='color:#008000'><strong><span style='line-height:1.6'>The Task related to your above feedback is in Progress ,we will send you a email with the latest status soon.&nbsp;</span></strong></span></p>");
				else if(selectedTaskStatus.getEnDescription().equalsIgnoreCase("Done"))
					result.append("<p><span style='color:#008000'><strong>The Task related to your above feedback is Done ,Please respond to our official email mentioned in the signature with in 3 working days else we will consider as it to be Done.&nbsp;</strong></span></p>");
				result.append("<p><span style='line-height:1.6'>Thank you.</span></p>");
				result.append("<p>Regards,</p>");
				result.append("<p>Explorer Computer</p>");
				result.append("<p>_______________________________________________________</p>");
				result.append("<p>Office:&nbsp;&nbsp;<a href='tel:%2B971%202%206714242' target='_blank'><span style='color:rgb(0, 0, 255)'>+971 2 6714242</span></a><span style='color:rgb(0, 0, 255)'>&nbsp;&nbsp;</span>Mobile:&nbsp;<span style='color:rgb(0, 0, 255)'><u>+971 50 6228024</u></span>&nbsp;</p>");
				result.append("<p>P.O.Box: 29006 - Abu Dhabi, United Arab Emirates</p>");
				result.append("<p>Email: &nbsp; &nbsp; &nbsp;&nbsp;<a href='mailto:hinawi@eim.ae' target='_blank'><span style='color:rgb(0, 0, 128)'>hinawi@eim.ae</span></a></p>");
				result.append("<p>Skype ID:&nbsp;&nbsp;hinawisoftware</p>");
				result.append("<p>Facebook:&nbsp;<a href='https://t.yesware.com/tl/e46fdc2366c3a40c0374f9ec8cca64f472f5a138/c10046266f8198556c6b63d1ac99066b/d4a64b5cc0fdb46223ed64460a741e6d?ytl=https%3A%2F%2Fwww.facebook.com%2Fhinawisoftware' target='_blank'><span style='color:rgb(0, 0, 128)'>hinawisoftware</span></a></p>");
				result.append("<p>Abu Dhabi - United Arab Emirates</p>");
				result.append("<p><a href='mailto:Reza@hinawi.ae' target='_blank' title='blocked::mailto:Reza@hinawi.ae'>_____________ __________________________________________</a><a href='mailto:Reza@hinawi.ae' style='line-height: 20.7999992370605px;' target='_blank' title='blocked::mailto:Reza@hinawi.ae'><strong>___</strong></a></p>");
				result.append("<p><a href='mailto:Reza@hinawi.ae' target='_blank' title='blocked::mailto:Reza@hinawi.ae'><strong>Consultants, Certified &amp; Authorized QuickBooks Distributors</strong></a></p>");
				result.append("<p><a href='mailto:Reza@hinawi.ae' target='_blank' title='blocked::mailto:Reza@hinawi.ae'><strong>Member of ASCA<br />");
				result.append("_______________________________________________________</strong></a><a href='mailto:Reza@hinawi.ae' style='line-height: 20.7999992370605px;' target='_blank' title='blocked::mailto:Reza@hinawi.ae'><strong>___</strong></a></p>");
				result.append("<p><em><span style='font-size:16px'><span style='color:#ff0000'>This email has been sent from hinawi e-mail system&nbsp;</span><span style='color:#ff0000'><a href='outbind://96-00000000E9961FA389D587489CCC9363A5A9239D84E62400/www.hinawi.com' title='blocked::www.hinawi.com'>www.hinawi.com</a>, please don not reply to same e-mail address.</span></span></em></p>");
				result.append("<p>&nbsp;</p>");
				String messageBody=result.toString();	
				String[] bcc =null;
				String cc[]={null};
				ArrayList fileArray = new ArrayList();
				for(QuotationAttachmentModel attPath:lstAtt)
				{
					File dir = new File(attPath.getFilepath());
					if(dir.exists())
						fileArray.add(attPath.getFilepath());
				}
				mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,fileArray,true,"Task","");
			}
			//mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, null);
		}
		catch (Exception e) 
		{
			logger.info("error at EditTaskListViewModel----> sendEmailToCustomerIfFeedbackExist" + e);
		}
	}




	@Command
	@NotifyChange({"taskKey","lstSelectedProject","lastUpdatedBy","lstSelectedCustomerJob","createdBy","selectedAssignToEmployee","selectedCCToEmployee","selectedHoursOrDays","disableTaskCreatorPanel","creatorEmailadress","taskDate","selectedLinkToPreviousTask","selectedTask","taskDate","expectedDateTofinish","remindInNumber","taskNumber","taskName","estimatedEffort","actualEffort","memo","comments","taskStep","taskHistory","selectedAttchemnets","history","selectedTaskType","selectedTaskStatus","selectedTaskPriority"})
	public void copyFunctinality()
	{
		try
		{	if(taskKey>0)
		{
			lstLinkToPriviousTask=taskData.getAllTaskOtherThanCurrentTask(taskKey);
			if(lstLinkToPriviousTask!=null  && lstLinkToPriviousTask.size()>0)
			{
				selectedLinkToPreviousTask=lstLinkToPriviousTask.get(0);
			}
			selectedTask=taskData.getTaskById(taskKey);
			Calendar c = Calendar.getInstance();		
			if(selectedTask!=null)
			{
				//taskDate=selectedTask.getCreationDate();
				/*if(selectedTask.getExpectedDatetofinish()!=null)
					expectedDateTofinish=selectedTask.getExpectedDatetofinish();
					else
					expectedDateTofinish=df.parse(sdf.format(c.getTime()));	*/


				taskDate=df.parse(sdf.format(c.getTime()));

				expectedDateTofinish=df.parse(sdf.format(c.getTime()));

				if(selectedTask.getReminderDate()!=null)
					reminderDate=selectedTask.getReminderDate();
				else
					reminderDate=df.parse(sdf.format(c.getTime()));	

				taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());

				remindInNumber=selectedTask.getRemindIn();

				//	taskNumber=selectedTask.getTaskNumber();
				taskName=selectedTask.getTaskName();
				estimatedEffort=selectedTask.getEstimatatedNumber();
				actualEffort=selectedTask.getActualNumber();
				memo=selectedTask.getMemo();
				comments="";
				taskStep=selectedTask.getTaskStep();
				taskHistory=taskData.getTaskDeatils(taskKey);
				lstAtt=taskData.getTaskAttchamnet(taskKey);
				taskKey=0;
				feedbackKeyForTaskRelation=0;
				if(lstAtt!=null && lstAtt.size()>0)
					selectedAttchemnets=lstAtt.get(0);

				for(TasksModel tasksModel:taskHistory)
				{
					history=history+tasksModel.getComments()+"      Date:"+tasksModel.getCreationDateStr()+"\n \n";
					for(HRListValuesModel hrListValuesModel:lstTaskStatus)
					{
						if(hrListValuesModel.getListId()==tasksModel.getStatusKey())
						{
							tasksModel.setStatusName(hrListValuesModel.getEnDescription());
							break;
						}
					}

					for(WebusersModel model:lstUsers)
					{
						if(model.getSupervisor()==tasksModel.getCreatedUserID() || model.getEmployeeKey()==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName(model.getFirstname());
							if(firstTimeLoop==100)
							{
								lastUpdatedBy=model.getFirstname();
								firstTimeLoop++;
							}
							break;
						}
						if(0==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName("Admin");
							break;
						}

					}
				}



				String cleintType=selectedTask.getClientType();
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

				for(WebusersModel model:lstUsers)
				{
					if(model.getSupervisor()==selectedTask.getCreatedUserID() || model.getEmployeeKey()==selectedTask.getCreatedUserID())
					{
						selectedTask.setCreatedUserName(model.getFirstname());
						createdBy=model.getFirstname();
						break;
					}
					if(0==selectedTask.getCreatedUserID())
					{
						selectedTask.setCreatedUserName("Admin");
						createdBy="Admin";
						break;
					}

				}

				if(selectedClientType!=null)
				{
					if(selectedClientType.equalsIgnoreCase("Prospective Client"))
					{
						lstCustomerJob=hbadata.quotationPrecpectiveList();
					}
					else
					{
						lstCustomerJob=hbadata.fillQbList("'Customer'");
					}
				}


				for(HRListValuesModel hrListValuesModel:lstTaskType)
				{
					if(hrListValuesModel.getListId()==selectedTask.getTaskTypeId())
					{
						selectedTaskType=hrListValuesModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskStatus)
				{
					if(hrListValuesModel.getListId()==selectedTask.getStatusKey())
					{
						selectedTaskStatus=hrListValuesModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskPriority)
				{
					if(hrListValuesModel.getListId()==selectedTask.getPriorityRefKey())
					{
						selectedTaskPriority=hrListValuesModel;
						break;
					}
				}

				for(ProjectModel projectModel:lstProject)
				{
					if(projectModel.getProjectKey()==selectedTask.getProjectKey())
					{
						lstSelectedProject=projectModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstService)
				{
					if(hrListValuesModel.getListId()==selectedTask.getSreviceId())
					{
						lstSelectedService=hrListValuesModel;
						break;
					}
				}

				for(QbListsModel customerModel:lstCustomerJob)
				{
					if(customerModel.getRecNo()==selectedTask.getCustomerRefKey())
					{
						lstSelectedCustomerJob=customerModel;
						break;
					}
				}

				for(EmployeeModel employeeModel:lstAssignToEmployees)
				{
					if(employeeModel.getEmployeeKey()==selectedTask.getEmployeeid())
					{
						selectedAssignToEmployee=employeeModel;
						break;
					}
				}

				for(EmployeeModel employeeModel:lstCCToEmployees)
				{
					if(employeeModel.getEmployeeKey()==selectedTask.getCcEmployeeKey())
					{
						selectedCCToEmployee=employeeModel;
						break;
					}
				}

				for(TasksModel tasksModel:lstLinkToPriviousTask)
				{
					if(tasksModel.getTaskid()==selectedTask.getPrviousTaskLinkId())
					{
						selectedLinkToPreviousTask=tasksModel;
						break;
					}
				}
				if(selectedTask.getHoursOrDays().equalsIgnoreCase("D"))
				{
					selectedHoursOrDays=hourOrDaysList.get(1);
				}
				else if(selectedHoursOrDays.equalsIgnoreCase("H"))
				{
					selectedHoursOrDays=hourOrDaysList.get(0);
				}

				disableTaskCreatorPanel=false;
				if(creatoEmployeeIDTogetEmail!=null)
				{
					creatoEmployeeIDTogetEmail=companyData.getuserById(dbUser.getCompanyid(),dbUser.getUserid());
					creatorEmployeeId=creatoEmployeeIDTogetEmail.getEmployeeKey();
					creatorEmailadress=hrData.getEmployeeEmail(creatorEmployeeId);
				}

			}

		}
		else
		{
			Messagebox.show("You can only copy a existing Task","Task", Messagebox.OK , Messagebox.INFORMATION);
			return;
		}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> copyFunctinality", ex);			
		}
	}



	@GlobalCommand 
	@NotifyChange({"lstTask"})
	public void getCutomerIDsFormGroupOfTasks(@BindingParam("myData")String custKeys)
	{		
		try
		{
			if(lstTask.size()>20)
			{
				Clients.showNotification("Maximum 20 tasks are allowed at a time",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			int i=0;
			String reg=",";
			String[] tokens = custKeys.split(reg);
			int taskNUmberInInterger=0;

			if(lstTask.size()>0)
			{
				serailNo=lstTask.size();
				taskNUmberInInterger=Integer.parseInt(taskNumber);
				taskNUmberInInterger=taskNUmberInInterger+1;
				serailNo= serailNo+1;
				taskNumber=String.valueOf(taskNUmberInInterger);
			}
			else
			{
				serailNo=1;
				taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());
			}


			if(IsInt_ByRegex(taskNumber))
				taskNUmberInInterger=Integer.parseInt(taskNumber);
			else
				taskNUmberInInterger=0;	

			for(i=0;i<tokens.length;i++)
			{
				TasksModel task=new TasksModel();

				task.setExpectedDatetofinish(df.parse(sdf.format(c.getTime())));
				task.setCreationDate(df.parse(sdf.format(c.getTime())));
				task.setReminderDate(df.parse(sdf.format(c.getTime())));
				task.setSerialNumber(serailNo);
				task.setTaskNumber(String.valueOf(taskNUmberInInterger));
				taskNumber=String.valueOf(taskNUmberInInterger);
				taskNUmberInInterger=taskNUmberInInterger+1;
				serailNo=serailNo+1;

				if(lstLinkToPriviousTask.size()>0)
					task.setSelectedLinkToPreviousTask(lstLinkToPriviousTask.get(0));

				if(lstAssignToEmployees.size()>0)
					task.setSelectedAssignToEmployee(lstAssignToEmployees.get(0));

				if(lstCCToEmployees.size()>0)
					task.setSelectedCCToEmployee(lstCCToEmployees.get(0));

				if(lstTaskType.size()>0)
					task.setSelectedTaskType(lstTaskType.get(0));


				if(lstTaskStatus.size()>0)
					task.setSelectedTaskStatus(lstTaskStatus.get(0));


				if(lstTaskPriority.size()>0)
					task.setSelectedTaskPriority(lstTaskPriority.get(0));


				if(lstProject.size()>0)
					task.setLstSelectedProject(lstProject.get(0));

				if(lstService.size()>0)
					task.setLstSelectedService(lstService.get(0));

				if(lstCustomerJob.size()>0)
					task.setLstSelectedCustomerJob(lstCustomerJob.get(0));

				task.setSelectedClientType(listClientType.get(1));


				for(QbListsModel customerModel:lstCustomerJob)
				{
					if(customerModel.getRecNo()==Integer.parseInt(tokens[i]))
					{
						task.setLstSelectedCustomerJob(customerModel);
						break;
					}
				}

				task.setSelectedHoursOrDays(hourOrDaysList.get(0));


				lstTask.add(task);			

			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> getCutomerIDsFormGroupOfTasks", ex);			
		}
	}



	@GlobalCommand 
	@NotifyChange({"lstTask"})
	public void getCutomerIDsFormGroupOfTasksSelectedQuotations(@BindingParam("myData")String custKeys)
	{		
		try
		{
			if(lstTask.size()>20)
			{
				Clients.showNotification("Maximum 20 tasks are allowed at a time",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}

			int i=0;
			String reg=",";
			String[] tokens = custKeys.split(reg);
			int taskNUmberInInterger=0;
			if(lstTask.size()>0)
			{
				serailNo=lstTask.size();
				taskNUmberInInterger=Integer.parseInt(taskNumber);
				taskNUmberInInterger=taskNUmberInInterger+1;
				serailNo= serailNo+1;
				taskNumber=String.valueOf(taskNUmberInInterger);
			}
			else
			{
				serailNo=1;
				taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());
			}
			if(IsInt_ByRegex(taskNumber))
				taskNUmberInInterger=Integer.parseInt(taskNumber);
			else
				taskNUmberInInterger=0;	

			for(i=0;i<tokens.length;i++)
			{
				TasksModel task=new TasksModel();

				task.setExpectedDatetofinish(df.parse(sdf.format(c.getTime())));
				task.setCreationDate(df.parse(sdf.format(c.getTime())));
				task.setReminderDate(df.parse(sdf.format(c.getTime())));
				task.setSerialNumber(serailNo);
				task.setTaskNumber(String.valueOf(taskNUmberInInterger));
				taskNumber=String.valueOf(taskNUmberInInterger);
				taskNUmberInInterger=taskNUmberInInterger+1;
				serailNo=serailNo+1;

				if(lstLinkToPriviousTask.size()>0)
					task.setSelectedLinkToPreviousTask(lstLinkToPriviousTask.get(0));

				if(lstAssignToEmployees.size()>0)
					task.setSelectedAssignToEmployee(lstAssignToEmployees.get(0));

				if(lstCCToEmployees.size()>0)
					task.setSelectedCCToEmployee(lstCCToEmployees.get(0));

				if(lstTaskType.size()>0)
					task.setSelectedTaskType(lstTaskType.get(0));


				if(lstTaskStatus.size()>0)
					task.setSelectedTaskStatus(lstTaskStatus.get(0));


				if(lstTaskPriority.size()>0)
					task.setSelectedTaskPriority(lstTaskPriority.get(0));


				if(lstProject.size()>0)
					task.setLstSelectedProject(lstProject.get(0));

				if(lstService.size()>0)
					task.setLstSelectedService(lstService.get(0));

				if(lstCustomerJob.size()>0)
					task.setLstSelectedCustomerJob(lstCustomerJob.get(0));

				task.setSelectedClientType(listClientType.get(1));


				for(QbListsModel customerModel:lstCustomerJob)
				{
					if(customerModel.getRecNo()==Integer.parseInt(tokens[i]))
					{
						task.setLstSelectedCustomerJob(customerModel);
						break;
					}
				}

				task.setSelectedHoursOrDays(hourOrDaysList.get(0));


				lstTask.add(task);			

			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> getCutomerIDsFormGroupOfTasksSelectedQuotations", ex);			
		}
	}






	@Command
	@NotifyChange({"lstLinkToPriviousTask","lastUpdatedBy","taskDate","selectedTask","expectedDateTofinish","remindInNumber","taskNumber","taskName","estimatedEffort","memo","comments","taskHistory","lstAtt","lstSelectedCustomerJob",
		"selectedAttchemnets","history","selectedClientType", "lstUsers","listClientType","lstCustomerJob","selectedTaskType","selectedTaskPriority","lstSelectedProject","selectedTaskStatus","lstSelectedService","createdBy","customerStatusStr",
		"selectedAssignToEmployee","actionTYpe","labelStatus", "selectedCCToEmployee","selectedLinkToPreviousTask","selectedHoursOrDays","disableTaskCreatorPanel","creatorEmailadress","reminderDate","visbaleCustomerNAmefromFeedback","cutomerNamefromFeedBack","cutomerContractExipryDate"})
	public void navigationTask(@BindingParam("cmp") String navigation)
	{
		try
		{
			firstTimeLoop=100;
			selectedTask=taskData.navigationTask(taskKey,webUserID,navigation,actionTYpe);
			if(selectedTask!=null && selectedTask.getTaskid()>0)
			{
				actionTYpe="edit";
				labelStatus="Edit";
				taskKey=selectedTask.getTaskid();
				lstLinkToPriviousTask=taskData.getAllTaskOtherThanCurrentTask(taskKey);
				if(lstLinkToPriviousTask!=null  && lstLinkToPriviousTask.size()>0)
				{
					selectedLinkToPreviousTask=lstLinkToPriviousTask.get(0);
				}
				taskDate=selectedTask.getCreationDate();
				if(selectedTask.getExpectedDatetofinish()!=null)
					expectedDateTofinish=selectedTask.getExpectedDatetofinish();
				else
					expectedDateTofinish=df.parse(sdf.format(c.getTime()));	

				if(selectedTask.getReminderDate()!=null)
					reminderDate=selectedTask.getReminderDate();
				else
					reminderDate=df.parse(sdf.format(c.getTime()));	

				remindInNumber=selectedTask.getRemindIn();

				feedbackKeyForTaskRelation=selectedTask.getFeedbackKey();

				taskNumber=selectedTask.getTaskNumber();
				taskName=selectedTask.getTaskName();
				estimatedEffort=selectedTask.getEstimatatedNumber();
				actualEffort=selectedTask.getActualNumber();
				memo=selectedTask.getMemo();
				comments="";
				taskStep=selectedTask.getTaskStep();
				taskHistory=taskData.getTaskDeatils(taskKey);
				lstAtt=taskData.getTaskAttchamnet(taskKey);
				if(lstAtt!=null && lstAtt.size()>0)
					selectedAttchemnets=lstAtt.get(0);

				for(TasksModel tasksModel:taskHistory)
				{
					history=history+tasksModel.getComments()+"      Date:"+tasksModel.getCreationDateStr()+"\n \n";

					for(HRListValuesModel hrListValuesModel:lstTaskStatus)
					{
						if(hrListValuesModel.getListId()==tasksModel.getStatusKey())
						{
							tasksModel.setStatusName(hrListValuesModel.getEnDescription());
							break;
						}
					}


					for(WebusersModel model:lstUsers)
					{
						if(model.getSupervisor()==tasksModel.getCreatedUserID() || model.getEmployeeKey()==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName(model.getFirstname());
							if(firstTimeLoop==100)
							{
								lastUpdatedBy=model.getFirstname();
								firstTimeLoop++;
							}
							break;
						}
						if(0==tasksModel.getCreatedUserID())
						{
							tasksModel.setCreatedUserName("Admin");
							break;
						}

					}
				}

				String cleintType=selectedTask.getClientType();
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

				for(WebusersModel model:lstUsers)
				{
					if(model.getSupervisor()==selectedTask.getCreatedUserID() || model.getEmployeeKey()==selectedTask.getCreatedUserID())
					{
						selectedTask.setCreatedUserName(model.getFirstname());
						createdBy=model.getFirstname();
						break;
					}
					if(0==selectedTask.getCreatedUserID())
					{
						selectedTask.setCreatedUserName("Admin");
						createdBy="Admin";
						break;
					}

				}

				if(selectedClientType!=null)
				{
					if(selectedClientType.equalsIgnoreCase("Prospective Client"))
					{
						lstCustomerJob=hbadata.quotationPrecpectiveList();
					}
					else
					{
						lstCustomerJob=hbadata.fillQbList("'Customer'");
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskType)
				{
					if(hrListValuesModel.getListId()==selectedTask.getTaskTypeId())
					{
						selectedTaskType=hrListValuesModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskStatus)
				{
					if(hrListValuesModel.getListId()==selectedTask.getStatusKey())
					{
						selectedTaskStatus=hrListValuesModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstTaskPriority)
				{
					if(hrListValuesModel.getListId()==selectedTask.getPriorityRefKey())
					{
						selectedTaskPriority=hrListValuesModel;
						break;
					}
				}

				for(ProjectModel projectModel:lstProject)
				{
					if(projectModel.getProjectKey()==selectedTask.getProjectKey())
					{
						lstSelectedProject=projectModel;
						break;
					}
				}

				for(HRListValuesModel hrListValuesModel:lstService)
				{
					if(hrListValuesModel.getListId()==selectedTask.getSreviceId())
					{
						lstSelectedService=hrListValuesModel;
						break;
					}
				}

				for(QbListsModel customerModel:lstCustomerJob)
				{
					if(customerModel.getRecNo()==selectedTask.getCustomerRefKey())
					{
						lstSelectedCustomerJob=customerModel;
						break;
					}
				}

				for(EmployeeModel employeeModel:lstAssignToEmployees)
				{
					if(employeeModel.getEmployeeKey()==selectedTask.getEmployeeid())
					{
						selectedAssignToEmployee=employeeModel;
						break;
					}
				}

				for(EmployeeModel employeeModel:lstCCToEmployees)
				{
					if(employeeModel.getEmployeeKey()==selectedTask.getCcEmployeeKey())
					{
						selectedCCToEmployee=employeeModel;
						break;
					}
				}

				for(TasksModel tasksModel:lstLinkToPriviousTask)
				{
					if(tasksModel.getTaskid()==selectedTask.getPrviousTaskLinkId())
					{
						selectedLinkToPreviousTask=tasksModel;
						break;
					}
				}
				if(selectedTask.getHoursOrDays().equalsIgnoreCase("D"))
				{
					selectedHoursOrDays=hourOrDaysList.get(1);
				}
				else if(selectedHoursOrDays.equalsIgnoreCase("H"))
				{
					selectedHoursOrDays=hourOrDaysList.get(0);
				}

				if(selectedAssignToEmployee!=null)
				{
					//when user log in check if the task assigned to user and created task user is equal or not to disable the panel
					if((webUserID!=0 && selectedTask!=null && selectedTask.getCreatedUserID()!=0 && selectedTask.getCreatedUserID()==webUserID) || adminUser)
					{
						disableTaskCreatorPanel=false;
					}
					else if(selectedTask.getCreatedAutommaticTask()!=null && !selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("") && selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("Y"))
					{
						disableTaskCreatorPanel=false;

					}
					else
					{
						disableTaskCreatorPanel=true;
					}
					creatorEmailadress=hrData.getEmployeeEmail(selectedTask.getCreatedUserID());
				}

				if(selectedTask.getCreatedAutommaticTask()!=null && !selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("") && selectedTask.getCreatedAutommaticTask().equalsIgnoreCase("Y"))
				{
					if(selectedTask.getCustomerNamefromFeedback()!=null)
						cutomerNamefromFeedBack=selectedTask.getCustomerNamefromFeedback();
					visbaleCustomerNAmefromFeedback=true;
				}
				else
				{
					cutomerNamefromFeedBack="";
					visbaleCustomerNAmefromFeedback=false;
				}


				if(lstSelectedCustomerJob!=null && lstSelectedCustomerJob.getRecNo()>0)
				{
					CustomerModel temp=null;
					temp= hbadata.getCustomerByKey(lstSelectedCustomerJob.getRecNo());
					customerStatus= hbadata.getCustomerStatusById(lstSelectedCustomerJob.getRecNo());
					if(temp!=null && temp.getCustkey()>0)
					{
						if(temp.getCustomerContactExpiryDateStr()!=null && !temp.getCustomerContactExpiryDateStr().equalsIgnoreCase(""))
							cutomerContractExipryDate=temp.getCustomerContactExpiryDateStr();
						else
							cutomerContractExipryDate="No Data";
					}
					else
					{
						cutomerContractExipryDate="No Data";
					}
					if(customerStatus!=null && customerStatus.size()>0)
					{
						for(CustomerStatusHistoryModel model:customerStatus)
						{
							if(model.getStatusDescription()!=null && !model.getStatusDescription().equalsIgnoreCase(""))
							{
								customerStatusStr=model.getStatusDescription() +" -- Date : "+ model.getActionDatstr();//most recent description.
								break;
							}
							else
							{
								customerStatusStr="No Data";
							}
						}
						// customerStatusStr=customerStatus.get(0).getStatusDescription();//most recent description.
					}
					else
					{
						customerStatusStr="No Data";
					}
				}


			}
			else
			{

				labelStatus="Create";
				actionTYpe="create";

				cutomerContractExipryDate="No Data";

				customerStatusStr="No Data";

				feedbackKeyForTaskRelation=0;

				taskDate=df.parse(sdf.format(c.getTime()));

				expectedDateTofinish=df.parse(sdf.format(c.getTime()));

				reminderDate=df.parse(sdf.format(c.getTime()));	

				taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());

				taskName="";

				taskKey=0;

				estimatedEffort=0;

				actualEffort=0;

				memo="";

				comments="";

				history="";

				taskStep="";

				for(HRListValuesModel hrListValuesModel:lstTaskStatus)
				{
					if(hrListValuesModel.getEnDescription().equalsIgnoreCase("Created"))
					{
						selectedTaskStatus=hrListValuesModel;
						break;
					}
				}


				if(otherformFalg)
				{

					//to create task from other form with Pre populating  some data 
					String cleintType=customerTypeOtherForms;
					if(cleintType!=null)
					{
						if(cleintType.equalsIgnoreCase("C") || cleintType.equals("Customer"))
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
							lstCustomerJob=hbadata.quotationPrecpectiveList();
						}
						else
						{
							lstCustomerJob=hbadata.fillQbList("'Customer'");
						}
					}

					for(QbListsModel customerModel:lstCustomerJob)
					{
						if(customerModel.getRecNo()==customerKeyOtherForms)
						{
							lstSelectedCustomerJob=customerModel;
							break;
						}
					}
					memo=memoOtherForms;
					lstAtt=lstOtherFormAtt;
				}

				disableTaskCreatorPanel=false;

				visbaleCustomerNAmefromFeedback=false;
				cutomerNamefromFeedBack="";

				taskHistory=new ArrayList<TasksModel>();

				selectedClientType=listClientType.get(1);

				if(lstLinkToPriviousTask.size()>0)
					selectedLinkToPreviousTask=lstLinkToPriviousTask.get(0);

				if(lstAssignToEmployees.size()>0)
					selectedAssignToEmployee=lstAssignToEmployees.get(0);

				if(lstCCToEmployees.size()>0)
					selectedCCToEmployee=lstCCToEmployees.get(0);

				if(lstTaskType.size()>0)
					selectedTaskType=lstTaskType.get(0);


				if(lstTaskStatus.size()>0)
					selectedTaskStatus=lstTaskStatus.get(0);


				if(lstTaskPriority.size()>0)
					selectedTaskPriority=lstTaskPriority.get(0);


				if(lstProject.size()>0)
					lstSelectedProject=lstProject.get(0);

				if(lstService.size()>0)
					lstSelectedService=lstService.get(0);

				if(lstCustomerJob.size()>0)
					lstSelectedCustomerJob=lstCustomerJob.get(0);

				lstAtt=new ArrayList<QuotationAttachmentModel>();

				if(creatoEmployeeIDTogetEmail!=null)
				{
					creatoEmployeeIDTogetEmail=companyData.getuserById(dbUser.getCompanyid(),dbUser.getUserid());
					creatorEmployeeId=creatoEmployeeIDTogetEmail.getEmployeeKey();
					creatorEmailadress=hrData.getEmployeeEmail(creatorEmployeeId);
				}
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> navigationTask", ex);			
		}
	}

	/**
	 * @return the lstTask
	 */
	public List<TasksModel> getLstTask() {
		return lstTask;
	}




	/**
	 * @param lstTask the lstTask to set
	 */
	public void setLstTask(List<TasksModel> lstTask) {
		this.lstTask = lstTask;
	}




	/**
	 * @return the lstAllTask
	 */
	public List<TasksModel> getLstAllTask() {
		return lstAllTask;
	}




	/**
	 * @param lstAllTask the lstAllTask to set
	 */
	public void setLstAllTask(List<TasksModel> lstAllTask) {
		this.lstAllTask = lstAllTask;
	}




	/**
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		return totalNumber;
	}




	/**
	 * @param totalNumber the totalNumber to set
	 */
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
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
	@NotifyChange({"lstCustomerJob","lstSelectedCustomerJob"})
	public void setSelectedClientType(String selectedClientType) 
	{
		this.selectedClientType = selectedClientType;
		lstCustomerJob.clear();
		if(selectedClientType!=null)
		{
			if(selectedClientType.equalsIgnoreCase("Prospective Client"))
			{
				lstCustomerJob=hbadata.quotationPrecpectiveList();
			}
			else
			{
				lstCustomerJob=hbadata.fillQbList("'Customer'");
			}
		}
		
		if(lstCustomerJob.size()>0)
		lstSelectedCustomerJob=lstCustomerJob.get(0);		
	}



	/**
	 * @return the reminderDate
	 */
	public Date getReminderDate() {
		return reminderDate;
	}



	/**
	 * @param reminderDate the reminderDate to set
	 */
	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}



	/**
	 * @return the actionTYpe
	 */
	public String getActionTYpe() {
		return actionTYpe;
	}



	/**
	 * @param actionTYpe the actionTYpe to set
	 */
	public void setActionTYpe(String actionTYpe) {
		this.actionTYpe = actionTYpe;
	}



	/**
	 * @return the labelStatus
	 */
	public String getLabelStatus() {
		return labelStatus;
	}



	/**
	 * @param labelStatus the labelStatus to set
	 */
	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}



	/**
	 * @return the visbaleCustomerNAmefromFeedback
	 */
	public boolean isVisbaleCustomerNAmefromFeedback() {
		return visbaleCustomerNAmefromFeedback;
	}



	/**
	 * @param visbaleCustomerNAmefromFeedback the visbaleCustomerNAmefromFeedback to set
	 */
	public void setVisbaleCustomerNAmefromFeedback(
			boolean visbaleCustomerNAmefromFeedback) {
		this.visbaleCustomerNAmefromFeedback = visbaleCustomerNAmefromFeedback;
	}



	/**
	 * @return the cutomerNamefromFeedBack
	 */
	public String getCutomerNamefromFeedBack() {
		return cutomerNamefromFeedBack;
	}



	/**
	 * @param cutomerNamefromFeedBack the cutomerNamefromFeedBack to set
	 */
	public void setCutomerNamefromFeedBack(String cutomerNamefromFeedBack) {
		this.cutomerNamefromFeedBack = cutomerNamefromFeedBack;
	}



	/**
	 * @return the cutomerContractExipryDate
	 */
	public String getCutomerContractExipryDate() {
		return cutomerContractExipryDate;
	}



	/**
	 * @param cutomerContractExipryDate the cutomerContractExipryDate to set
	 */
	public void setCutomerContractExipryDate(String cutomerContractExipryDate) {
		this.cutomerContractExipryDate = cutomerContractExipryDate;
	}



	/**
	 * @return the customerStatus
	 */
	public List<CustomerStatusHistoryModel> getCustomerStatus() {
		return customerStatus;
	}



	/**
	 * @param customerStatus the customerStatus to set
	 */
	public void setCustomerStatus(List<CustomerStatusHistoryModel> customerStatus) {
		this.customerStatus = customerStatus;
	}



	/**
	 * @return the customerStatusStr
	 */
	public String getCustomerStatusStr() {
		return customerStatusStr;
	}



	/**
	 * @param customerStatusStr the customerStatusStr to set
	 */
	public void setCustomerStatusStr(String customerStatusStr) {
		this.customerStatusStr = customerStatusStr;
	}


	@Command
	public void viewCustomerStatusHistroy(@BindingParam("row") TasksModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			if(lstSelectedCustomerJob!=null)
			{
				arg.put("cutomerKey",lstSelectedCustomerJob.getRecNo());
				arg.put("customerName",lstSelectedCustomerJob.getFullName());
				arg.put("type","View");
				Executions.createComponents("/hba/report/customerStatusHistoryPopupFortask.zul", null,arg);
			}
			else
			{
				Clients.showNotification("Please select the Valid Customer.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> editTask", ex);			
		}
	}


	@Command
	public void replyFeedbackByEmail(@BindingParam("taskKey") int taskKey)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("taskId",taskKey);
			arg.put("feedBackKey",0);
			arg.put("type","FromTask");
			Executions.createComponents("/crm/editCustomerFeedbackSend.zul", null,arg);

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> replyFeedbackByEmail", ex);			
		}
	}



	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}



	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	/**
	 * @return the feedbackKeyForTaskRelation
	 */
	public int getFeedbackKeyForTaskRelation() {
		return feedbackKeyForTaskRelation;
	}



	/**
	 * @param feedbackKeyForTaskRelation the feedbackKeyForTaskRelation to set
	 */
	public void setFeedbackKeyForTaskRelation(int feedbackKeyForTaskRelation) {
		this.feedbackKeyForTaskRelation = feedbackKeyForTaskRelation;
	}



	/**
	 * @return the refreshTaskNumber
	 */
	public boolean isRefreshTaskNumber() {
		return refreshTaskNumber;
	}



	/**
	 * @return the taskKey
	 */
	public int getTaskKey() {
		return taskKey;
	}



	/**
	 * @param taskKey the taskKey to set
	 */
	public void setTaskKey(int taskKey) {
		this.taskKey = taskKey;
	}



	/**
	 * @param refreshTaskNumber the refreshTaskNumber to set
	 */
	public void setRefreshTaskNumber(boolean refreshTaskNumber) {
		this.refreshTaskNumber = refreshTaskNumber;
	}

	@Command
	@NotifyChange({"taskNumber"})
	public void getLatestTaskNumber()
	{
		try
		{
			taskNumber=hbadata.GetSaleNumber(SerialFields.Task.toString());
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditTaskListViewModel ----> getLatestTaskNumber", ex);			
		}
	}



	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}



	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}



	public Logger getLogger() {
		return logger;
	}



	public void setLogger(Logger logger) {
		this.logger = logger;
	}



	public CustomerData getData() {
		return data;
	}



	public void setData(CustomerData data) {
		this.data = data;
	}



	public HBAData getHbadata() {
		return hbadata;
	}



	public void setHbadata(HBAData hbadata) {
		this.hbadata = hbadata;
	}



	public HRData getHrData() {
		return hrData;
	}



	public void setHrData(HRData hrData) {
		this.hrData = hrData;
	}



	public TimeSheetData getTimsheetData() {
		return timsheetData;
	}



	public void setTimsheetData(TimeSheetData timsheetData) {
		this.timsheetData = timsheetData;
	}



	public TaskData getTaskData() {
		return taskData;
	}



	public void setTaskData(TaskData taskData) {
		this.taskData = taskData;
	}



	public CompanyData getCompanyData() {
		return companyData;
	}



	public void setCompanyData(CompanyData companyData) {
		this.companyData = companyData;
	}



	public DateFormat getDf() {
		return df;
	}



	public void setDf(DateFormat df) {
		this.df = df;
	}



	public SimpleDateFormat getSdf() {
		return sdf;
	}



	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}



	public DecimalFormat getDcf() {
		return dcf;
	}



	public void setDcf(DecimalFormat dcf) {
		this.dcf = dcf;
	}



	public TasksModel getTempModel() {
		return tempModel;
	}



	public void setTempModel(TasksModel tempModel) {
		this.tempModel = tempModel;
	}



	public WebusersModel getCreatoEmployeeIDTogetEmail() {
		return creatoEmployeeIDTogetEmail;
	}



	public void setCreatoEmployeeIDTogetEmail(
			WebusersModel creatoEmployeeIDTogetEmail) {
		this.creatoEmployeeIDTogetEmail = creatoEmployeeIDTogetEmail;
	}



	public List<WebusersModel> getLstUsers() {
		return lstUsers;
	}



	public void setLstUsers(List<WebusersModel> lstUsers) {
		this.lstUsers = lstUsers;
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



	public int getWebUserID() {
		return webUserID;
	}



	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
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



	public String getEmployeeEmail() {
		return employeeEmail;
	}



	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}



	public String getCcEmployeemEmail() {
		return ccEmployeemEmail;
	}



	public void setCcEmployeemEmail(String ccEmployeemEmail) {
		this.ccEmployeemEmail = ccEmployeemEmail;
	}



	public int getCreatorEmployeeId() {
		return creatorEmployeeId;
	}



	public void setCreatorEmployeeId(int creatorEmployeeId) {
		this.creatorEmployeeId = creatorEmployeeId;
	}



	public String getCreatorEmailadress() {
		return creatorEmailadress;
	}



	public void setCreatorEmailadress(String creatorEmailadress) {
		this.creatorEmailadress = creatorEmailadress;
	}



	public List<MenuModel> getList() {
		return list;
	}



	public void setList(List<MenuModel> list) {
		this.list = list;
	}



	public CompSetupModel getEmailcompSetup() {
		return emailcompSetup;
	}



	public void setEmailcompSetup(CompSetupModel emailcompSetup) {
		this.emailcompSetup = emailcompSetup;
	}



	public Calendar getC() {
		return c;
	}



	public void setC(Calendar c) {
		this.c = c;
	}



	public int getTempTotalNUmber() {
		return tempTotalNUmber;
	}



	public void setTempTotalNUmber(int tempTotalNUmber) {
		this.tempTotalNUmber = tempTotalNUmber;
	}



	public int getSerailNo() {
		return serailNo;
	}



	public void setSerailNo(int serailNo) {
		this.serailNo = serailNo;
	}



	public int getCustomerKeyOtherForms() {
		return customerKeyOtherForms;
	}



	public void setCustomerKeyOtherForms(int customerKeyOtherForms) {
		this.customerKeyOtherForms = customerKeyOtherForms;
	}



	public String getCustomerTypeOtherForms() {
		return customerTypeOtherForms;
	}



	public void setCustomerTypeOtherForms(String customerTypeOtherForms) {
		this.customerTypeOtherForms = customerTypeOtherForms;
	}



	public List<QuotationAttachmentModel> getLstOtherFormAtt() {
		return lstOtherFormAtt;
	}



	public void setLstOtherFormAtt(List<QuotationAttachmentModel> lstOtherFormAtt) {
		this.lstOtherFormAtt = lstOtherFormAtt;
	}



	public String getMemoOtherForms() {
		return memoOtherForms;
	}



	public void setMemoOtherForms(String memoOtherForms) {
		this.memoOtherForms = memoOtherForms;
	}



	public boolean isOtherformFalg() {
		return otherformFalg;
	}



	public void setOtherformFalg(boolean otherformFalg) {
		this.otherformFalg = otherformFalg;
	}



	public String getCustomerExpiry() {
		return customerExpiry;
	}



	public void setCustomerExpiry(String customerExpiry) {
		this.customerExpiry = customerExpiry;
	}



	public String getCustomerCreated() {
		return customerCreated;
	}



	public void setCustomerCreated(String customerCreated) {
		this.customerCreated = customerCreated;
	}



	public String getJobType() {
		return jobType;
	}



	public void setJobType(String jobType) {
		this.jobType = jobType;
	}



	public String getBalcklisted() {
		return balcklisted;
	}



	public void setBalcklisted(String balcklisted) {
		this.balcklisted = balcklisted;
	}



	public String getActive() {
		return active;
	}



	public void setActive(String active) {
		this.active = active;
	}



	public int getFirstTimeLoop() {
		return firstTimeLoop;
	}



	public void setFirstTimeLoop(int firstTimeLoop) {
		this.firstTimeLoop = firstTimeLoop;
	}





}
