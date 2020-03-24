package hr;

import home.MailClient;
import home.QuotationAttachmentModel;
import hr.model.CompanyModel;
import hr.model.LeaveModel;
import hr.model.LeaveapproveOrRejectModel;
import hr.model.PassportModel;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import layout.MenuModel;
import model.AccountsModel;
import model.CashInvoiceGridData;
import model.CompSetupModel;
import model.EmployeeFilter;
import model.EmployeeModel;
import model.ExpirySettingsModel;
import model.HRListValuesModel;
import model.LeaveExpiryModel;
import model.PrintModel;
import model.ProspectiveModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import common.NumberToWord;
import setup.users.WebusersModel;
import timesheet.TimeSheetData;

public class LeaveRequestViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	TimeSheetData timesheetData=new TimeSheetData();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	
	private String employeeNumber;
	WorkGroupData workGroupData=new WorkGroupData();
	
	private Date creationdate; 
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	
	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
		
	private PassportModel employeePassport;
	
	private Date fromDate;	
	private Date toDate;
	private Date requestDate;	
	
	//for approve or reject leave
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	
	private boolean adminUser=false;
	
	private List<LeaveapproveOrRejectModel> leaveapproveOrRejectList;
	
	private List<LeaveapproveOrRejectModel> allLeaveapproveOrRejectList;
	
	private List<LeaveapproveOrRejectModel> selectedleaveapproveOrRejectList;
	
	private List<HRListValuesModel> lstType;
	private HRListValuesModel selectedType;
	
	private List<HRListValuesModel> lstReason;
	private HRListValuesModel selectedReason;
	private String otherReason;
	
	private String note;	
	private boolean canSubmit;
	private boolean useEncashLeave;
	private boolean checkUseEncash;
	private String checkUseEncashValue="";
	
	private boolean NotAllowToCreateLeave;
	private LeaveModel lastEmployeeLeave;
	
	private int leaveDays;
	private String empOSL;
	private double employeeOutStandingLoan;
	
	private int totalDays;
	private int maxLeaveDays;
	private String lblMaxLeaveDays;
	
	boolean allowMINUSLeaveDays;
	private int supervisorID;
	private int employeeKey;
	private String employeeEmail;
	private String supervisorEmail;
	private String super_SupervisorEmail="";
	int menuID=72;
	private MenuModel companyRole;
	private int UserId;
	
	private List<String> aprroveRejectType;
	private String selectedAprroveRejectType;
	private String typeStatus;
	
	private boolean approveVisible=true;
	private boolean rejectVisible=true;
	private boolean reasonReadOnly=false;
	
	private String passportExpiry="";
	
	private String residanceExpiry="";
	
	private String labourCradExpiry="";
	
	
	private boolean expPasprt=false;
	private boolean expResidance=false;
	private boolean expLbrCrd=false;
	
	private String expPasprtMsg="";
	private String expResidanceMSg="";
	private String expLbrCrdMsg="";
	private WebusersModel dbUser=null;
	
	ExpirySettingsModel obj=new ExpirySettingsModel(); 
	
	int diffInDayslabour=999;
	int diffInDaysResidance=999;//a random initialization;
	int diffInDaysPassport=999;//a random initialization;
	
	private CompSetupModel compSetup;
	private String attFileName;
	private String randomUUIDString="";
	private LeaveModel salaryCertificate;
	private boolean arabicSalaryCertificate=false;
	boolean isSuperSupervisor=false;
	private LeaveapproveOrRejectModel editLeaveRequest;
	
	public LeaveRequestViewModel()
	{
		try
		{	
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();			
			String type = (String) map.get("type")==null?"":"edit"; //for now only use edit
			
			
			totalDays=0;
			int defaultCompanyId=0;
			fillTYpeOfAproveOrReject();
			Session sess = Sessions.getCurrent();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
			adminUser=dbUser.getFirstname().equals("admin");
			}
			supervisorID=dbUser.getSupervisor();
			employeeKey=dbUser.getEmployeeKey();
			UserId=dbUser.getUserid();
		
			
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			
			if(lstComapnies.size()>=1 && selectedCompany==null)
			selectedCompany=lstComapnies.get(0);
			lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"Select","A",supervisorID);
			selectedCompEmployee=lstCompEmployees.get(0);	
			
			Calendar c = Calendar.getInstance();	
			fromDate=df.parse(sdf.format(c.getTime()));
			toDate=df.parse(sdf.format(c.getTime()));
			requestDate=df.parse(sdf.format(c.getTime()));
			setFromDate(df.parse(sdf.format(c.getTime())));									
			setToDate(df.parse(sdf.format(c.getTime())));	
								
			salaryCertificate=new LeaveModel();
			salaryCertificate.setLeaveStartDate(fromDate);
			salaryCertificate.setLeaveTypeDesc("");
			salaryCertificate.setStatus("");
			
			
			//lstType=data.getHRListValues(17,"Select type of leave..");
			//selectedType=lstType.get(0);
					
			lstReason=data.getHRListValues(16,"Select reason for request..");
			HRListValuesModel obj=new HRListValuesModel();			
			obj.setListId(-1);					
			obj.setEnDescription("Other");
			lstReason.add(obj);			
			
			selectedReason=lstReason.get(0);
			
			getCompanySetup(selectedCompany.getCompKey());
			leaveDays=1;
					
			employeeOutStandingLoan=0;
			supervisorEmail="";
			
			if (type.equalsIgnoreCase("edit")) 
			{
				int leaveId =(Integer)  map.get("leaveId");
				if(leaveId>0)
				{
					editLeaveRequest=data.getLeaveRequestById(leaveId);
					employeeKey = editLeaveRequest.getEmp_key();
					
					for (EmployeeModel emp : lstCompEmployees) 
					{
						if(emp.getEmployeeKey()==employeeKey)
						{
							setSelectedCompEmployee(emp);
							supervisorEmail=data.getEmployeeEmail(emp.getSupervisorId());
							employeeEmail=data.getEmployeeEmail(employeeKey);
							break;
						}
					}
					
				for(HRListValuesModel model:lstReason)
				{
					if(model!=null)
					{
						if(model.getListId()==editLeaveRequest.getReasonId())
						{
							selectedReason=model;
							break;
						}
					}
				}
				//other reason
				if(editLeaveRequest.getReasonId()==0)
				{
					selectedReason=lstReason.get(lstReason.size()-1);
				}
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");				
				lstType=data.getEmployeeLeavesAllowed(employeeKey,df.format(editLeaveRequest.getLeaveFromDate()),"Select type of leave..");
				for(HRListValuesModel model:lstType)
				{
					if(model!=null)
					{
						if(model.getListId()==editLeaveRequest.getLeaveId())
						{
							selectedType=model;
							break;
						}
					}
				}
											
				fromDate=editLeaveRequest.getLeaveFromDate();
				toDate=editLeaveRequest.getLeaveToDate();
				lastEmployeeLeave=data.GetLastEmployeeLeaveQuery(employeeKey);
				
				if(editLeaveRequest.getEnCashStatus().equals("Y"))
				{
					checkUseEncash=true;
					checkUseEncashValue="Y";
				}
				else
				{
					checkUseEncash=false;
					checkUseEncashValue="N";
				}
			
				}
				return;
			}
						
			if(employeeKey>0)
			{
				for (EmployeeModel emp : lstCompEmployees) 
				{
					if(emp.getEmployeeKey()==employeeKey)
					{
						setSelectedCompEmployee(emp);
						supervisorEmail=data.getEmployeeEmail(emp.getSupervisorId());
						employeeEmail=data.getEmployeeEmail(employeeKey);
						break;
					}
				}
				
				for(CompanyModel compny:lstComapnies)
				{
					if(selectedCompEmployee.getCompKey()==compny.getCompKey())
					{
						setSelectedCompany(compny);
						break;
					}
					
				}
				
				if(employeeEmail.equals(""))
				{
					Messagebox.show("You have to add you email before continue !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					Map<String,Object> arg = new HashMap<String,Object>();
					arg.put("empKey", employeeKey);
					arg.put("type", "email");
					arg.put("compKey", 0);
					Window window = (Window)Executions.createComponents("/hr/activity/updateemail.zul", null, arg);
				    window.doModal();					
				}
			}
			
			//at load to fill the approve/reject list
			
			if(supervisorID>0)
			{
				isSuperSupervisor=workGroupData.checkIfSuperSupervisor(supervisorID);
			}
			
			if(isSuperSupervisor && !adminUser)
			{
				leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SA",isSuperSupervisor);
			}
			else if (supervisorID>0)
			{
				leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
			}
			else
			{
				leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
			}
			allLeaveapproveOrRejectList=leaveapproveOrRejectList;
					
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in LeaveRequestViewModel ----> init", ex);			
		}
	}
	private void getCompanyRolePermessions(int companyRoleId)
	{
		setCompanyRole(new MenuModel());		
		List<MenuModel> lstRoles= data.getHRRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				setCompanyRole(item);
				break;
			}
		}
	}
	
	@GlobalCommand 
	@NotifyChange("employeeEmail")
	public void refreshEmailParent(@BindingParam("employeeEmail")String employeeEmail)
	{		
		 this.employeeEmail=employeeEmail;
	}
	
	@Command	
	@NotifyChange({"totalDays","lastEmployeeLeave"})
	public void calculateCommand()
	{
		if(selectedCompEmployee.getEmployeeKey()==0)
		{
			Messagebox.show("Invalid Employee No.","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;			
		}
		
		if(leaveDays<=0)
		{
			Messagebox.show("From Date cannot be greater than To Date, Please check !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(selectedType.getListId()==0)
		{
			Messagebox.show("Please select leave type.","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;			
		}
		
		EmployeeActivity activity=new EmployeeActivity();
		//totalDays=activity.GetLeaveBalanceDays(selectedCompEmployee.getEmployeeKey(), selectedType.getListId(), fromDate,selectedCompany.getCompKey());
		lastEmployeeLeave.setNoOfDays(totalDays);
	}
	
	@Command	
	@NotifyChange({"selectedCompany","lstCompEmployees","selectedCompEmployee","employeePassport"
		,"lastEmployeeLeave","employeeNumber","lstType","selectedType","otherReason","empOSL","passportExpiry","residanceExpiry","labourCradExpiry","expPasprtMsg","expPasprt","expesidanceMSg","expResidance","expLbrCrdMsg","expLbrCrd",
		"canSubmit","lblMaxLeaveDays","leaveDays","lstReason","selectedReason","totalDays","fromDate","toDate","note","totalDays","passportExpiry","residanceExpiry","labourCradExpiry","expPasprtMsg","expPasprt","expesidanceMSg","expResidance","expLbrCrdMsg","expLbrCrd"})
	public void searchCommand()
	{
		
	
		setCanSubmit(false);
		totalDays=0;
		
		EmployeeModel objEmp= data.GetEmployeeByEmployeeNumber(employeeNumber);
		String tempempNumber=employeeNumber;
		clearData();	
		employeeNumber=tempempNumber;
		if(objEmp.getEmployeeKey()==0)
		{
			Messagebox.show("Invalid Employee No.","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;			
		}
		
		for (CompanyModel item : lstComapnies) 
		{
		if(item.getCompKey()==objEmp.getCompanyID())
		{
			selectedCompany=item;
		}
		}
		
		
		lstCompEmployees=data.getEmployeeList(objEmp.getCompanyID(),"Select","A",supervisorID);
		for (EmployeeModel item : lstCompEmployees) 
		{
		if(item.getEmployeeKey()==objEmp.getEmployeeKey())
		{
			selectedCompEmployee=item;
			if(selectedCompEmployee!=null)
			setSelectedCompEmployee(selectedCompEmployee);
		}
	   }
		//selectedCompEmployee=lstCompEmployees.get(0);			
		getCompanySetup(objEmp.getCompanyID());
		
		
		employeePassport=data.GetEmployeePassport(objEmp.getEmployeeKey());		
		lastEmployeeLeave=data.GetLastEmployeeLeaveQuery(objEmp.getEmployeeKey());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		otherReason="";
		lstType=data.getEmployeeLeavesAllowed(objEmp.getEmployeeKey(),df.format(fromDate),"Select type of leave..");
		selectedType=lstType.get(0);			
		
		employeeOutStandingLoan=data.getEmployeeOutStandingLoans(objEmp.getEmployeeKey());
		DecimalFormat decf = new DecimalFormat("#0.00");				
		empOSL=decf.format(employeeOutStandingLoan);
		
		String leaveStatus="Regular";
		
		employeePassport.setStatus(leaveStatus);
		//check if Employee has already any Leave
		if(NotAllowToCreateLeave)
		{
			LeaveModel objLeave=data.GetEmployeeLeave(0, objEmp.getEmployeeKey(),"");			
			if(objLeave.getRecNO()>0)
			{
				if(objLeave.getStatus().equals("C"))
				{
					leaveStatus="Created";
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Created) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					this.selectedType=lstType.get(0);
				}
				if(objLeave.getStatus().equals("A"))
				{
					leaveStatus="Approved";					
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Approved) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					this.selectedType=lstType.get(0);
				}
				employeePassport.setStatus(leaveStatus);
			}
		}
		
		if(employeePassport.getStatus().equals("Regular"))
		{
			setCanSubmit(true);
		}
	}
	
	@Command 
	@NotifyChange({"attFileName"})
	public void attachFileCommand(BindContext ctx)
	{
		UploadEvent event = (UploadEvent)ctx.getTriggerEvent();
		String filePath="";
		String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"leaves"+File.separator+dbUser.getCompanyid()+File.separator;
		
		UUID uuid = UUID.randomUUID();
		randomUUIDString = uuid.toString();
		
		//Session sess = Sessions.getCurrent();
					
		//String sessID=(Executions.getCurrent()).getDesktop().getId();
		//logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
		
		String dirPath=repository+randomUUIDString;//+sessID;//session.getId();
		File dir = new File(dirPath);
		 
		//File dir = new File(System.getProperty("catalina.base"), "uploads");
		if(!dir.exists())
			dir.mkdirs();
			
		filePath = dirPath+File.separator +event.getMedia().getName(); //+ "." +event.getMedia().getFormat();	 
		//Messagebox.show(filePath);
		//if(event.getMedia().getFormat().equals("txt"))
		//createFile(event.getMedia().getByteData(), filePath);
		//else
		createFile(event.getMedia().getStreamData(), filePath);
		
		logger.info(filePath);
		attFileName=event.getMedia().getName();//+ "." +event.getMedia().getFormat();			
	}
	
	@Command
 	public void downloadFileCommand(@BindingParam("row") LeaveapproveOrRejectModel row)
 	{
		try
		{
			String filePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"leaves"+File.separator+dbUser.getCompanyid()+File.separator;
			filePath=repository+row.getAttachment_path();
			FileInputStream inputStream;
	        try {
	            File dosfile = new File(filePath);
	            if (dosfile.exists()) {
	                inputStream = new FileInputStream(dosfile);
	                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
	            } else
	            	Messagebox.show("Sorry, but the file path is Invalid");

	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	        	Messagebox.show(e.getMessage());
	        } 
			
			/*URL oracle = new URL(filePath);
		        BufferedReader in = new BufferedReader(
		        new InputStreamReader(oracle.openStream()));

		        String inputLine;
		        while ((inputLine = in.readLine()) != null)
		            System.out.println(inputLine);
		        in.close();*/
		
		}
		catch(Exception ex)
		{
			Messagebox.show(ex.getMessage());
		}
 	}
	
	private int createFile( InputStream is, String filePath)
	{
		int res=0;
		try
	    {
		  File file = new File(filePath);  
		  DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		  int c;
		  while((c = is.read()) != -1)
		  {
			  out.writeByte(c);
		  }
		  is.close();
		  out.close();
	    }
		catch(Exception ex)
		{
			res=1;
			Messagebox.show(ex.getMessage());
		}
		return res;
	}
	
	public void saveLeaveInnerFunction()
	{
		if(leaveDays<=0)
		{
			Messagebox.show("From Date cannot be greater than To Date, Please check !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(NotAllowToCreateLeave)
		{
			LeaveModel objLeave=data.GetEmployeeLeave(0, selectedCompEmployee.getEmployeeKey(),"");			
			if(objLeave.getRecNO()>0)
			{
				if(objLeave.getStatus().equals("C"))
				{
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Created) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(objLeave.getStatus().equals("SA") || objLeave.getStatus().equals("SSA"))
				{
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Approved) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		if(selectedReason.getListId()==0)
		{
			Messagebox.show("Please select request reason !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(selectedType.getListId()==0)
		{
			Messagebox.show("Please select type of leave !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(checkUseEncash==true)
		{
			checkUseEncashValue="Y";
		}
		else
		{
			checkUseEncashValue="N";
		}
		
	//check the Max leave days allowed
		int balance=totalDays-leaveDays;
		if(balance<0)
		{					
			//check if allowed minus leave
			if(allowMINUSLeaveDays)
			{
				//check how many days allowed over balance
				if(Math.abs(balance) > maxLeaveDays)
				{
					Messagebox.show("Max. days allowed over balance is "  + maxLeaveDays  + " days !! ","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
			else
			{
				Messagebox.show("Company not allowed negative leaved days!! ","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}	
		
		  Messagebox.show("Do you want to Create this leave request.?","Leave Request", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				    	if (evt.getName().equals("onYes")) 
				        {
				    		saveLeaveConfirmationSuccessfull();
				        }
				    	else
				    	{
				    		return;
				    	}
				    }
		  	});
		
		
	}
	
	public void  saveLeaveConfirmationSuccessfull()
	{
		
		EmployeeModel obj=workGroupData.checkEmployeeHasSupervisorandSuper_supervisor(selectedCompEmployee.getEmployeeKey());
		String status="";
		if(obj!=null)
		{
			if(obj.getSupervisorId()>0)
			{
				if(workGroupData.checkIfSuperSupervisor(obj.getSupervisorId()))
				{
					if(!adminUser)
						status="SA";
					else
						status="SSA";
				}
				else
				{
					if(!adminUser)
						status="C";
					else
						status="SSA";
				}
			}
			else
			{
				if(obj.getSuper_supervisorId()==0 && obj.getSupervisorId()==0)
				{
					if(!adminUser)
						status="SSA";
					else
						status="SSA";
					
				}
				else
				{
					if(!adminUser)
						status="C";
					else
						status="SSA";
				}
			}
		}
		
		//for Alwahda need always approve
		status="C";
		
		if(randomUUIDString.equals(""))
			attFileName="";
		else
		attFileName=randomUUIDString+File.separator +attFileName;
		
		int result=data.InsertLeaveRequest(selectedCompEmployee.getEmployeeKey(), fromDate, toDate,selectedReason.getListId(), note,selectedType.getListId(),otherReason,checkUseEncashValue,requestDate,status,attFileName);
		//save user activity
		//	String DESCRIPTION=selectedType.getEnDescription() + " from " + sdf.format(fromDate) + " To " + sdf.format(toDate) + " (" +selectedReason.getEnDescription() + ")" ;		
		String DESCRIPTION="Your "+selectedType.getEnDescription() +" you have requested From " + sdf.format(fromDate) +" To " + sdf.format(toDate) + " from web application has been ";
		data.addUserActivity(common.HREnum.HRFormNames.HRLeaves.getValue(),common.HREnum.HRStatus.HRNew.getValue(), selectedCompEmployee.getEmployeeKey(), selectedCompany.getCompKey(), DESCRIPTION, UserId);
		if(result==1)
		{
		//send email to supervisor
			supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());//if in case supervisor logs in and creates and leave for employee then mail should be sent to supervisor and employee.
			logger.info("supervisorEmail >> " + supervisorEmail);
			if(!supervisorEmail.equals(""))
			{
				String DESCRIPTIONSuper="The "+selectedType.getEnDescription() +" for "+ selectedCompEmployee.getFullName() +" requested From " + sdf.format(fromDate) +" To " + sdf.format(toDate) + " from web application has been ";
				sendEmail(supervisorEmail,DESCRIPTIONSuper,1);
			}
			
			if(adminUser)
	    	{
	    		//if admin approves then send email to supervisor as well as super supervisor 
	    		super_SupervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSuper_supervisorId());
	    		if(!super_SupervisorEmail.equals(""))
	    		{
	    			String DESCRIPTIONSuper_Super="The "+selectedType.getEnDescription() +" for "+ selectedCompEmployee.getFullName() +" requested From " + sdf.format(fromDate) +" To " + sdf.format(toDate) + " from web application has been ";
	    			sendEmail(super_SupervisorEmail,DESCRIPTIONSuper_Super,1);
	    		}
	    	}
			String empEmail=data.getEmployeeEmail(selectedCompEmployee.getEmployeeKey());
			logger.info("emp email >> " + empEmail);
	    	if(!empEmail.equals(""))
	    	{
	    		sendEmail(empEmail, DESCRIPTION,1);
	    	}
			Messagebox.show("New leave request has been created.","Leave Request", Messagebox.OK , Messagebox.INFORMATION);
			if(employeeKey>0)
			{
				searchCommand();//for level of access if employee logs in
			}
			else
			{
				clearData();
				employeeNumber="";
			}
		}
		else	
		{
				Messagebox.show("Error occured in new request !!","Leave Request", Messagebox.OK , Messagebox.ERROR);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Command	
	@NotifyChange({"employeeNumber","selectedCompEmployee","selectedReason","employeePassport","lastEmployeeLeave","empOSL","leaveDays","note","otherReason",
		"selectedType","totalDays","canSubmit","lblMaxLeaveDays","leaveDays","lstReason","totalDays","fromDate","toDate"})
	public void submitCommand()
	{
		try
		{
						
			if(selectedCompEmployee.getEmployeeKey()>0)
			{
				
				//checkOverlappingDates
				if(checkOverLapDates()==true)
				{
					Messagebox.show("A Leave already exists between these dates, Please check !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				
				if(fromDate.before(requestDate))
				{
					Messagebox.show("The Start From should not be before the Leave Request Date!!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				  String empInSalarySheet="";
				  boolean isSalaryGenerate=false;
				  boolean isSalaryApproved=false;
				
				  Calendar calVal = Calendar.getInstance();
				  calVal.setTime(fromDate);
				   int yearFrom = calVal.get(Calendar.YEAR);
				   int monthFrom = calVal.get(Calendar.MONTH);
				   int day = calVal.get(Calendar.DAY_OF_MONTH);
				  isSalaryGenerate=timesheetData.checkEmployeeSalarySheet(selectedCompEmployee.getEmployeeKey(), monthFrom+1, yearFrom);
				  isSalaryApproved=timesheetData.checkEmployeeSalarySheetApproved(selectedCompEmployee.getEmployeeKey(), monthFrom+1, yearFrom);
				  obj=data.getexpirySettings(selectedCompany.getCompKey());
				  if(obj!=null)
				  {
					  if(diffInDayslabour<=obj.getTotalDays() && obj.getStrLeaveLabourCrad()!=null && obj.getStrLeaveLabourCrad().equalsIgnoreCase("Y"))
					  {
						  Messagebox.show("You cannot create leave request as Labour Card of employee expires or already exipired by "+diffInDayslabour+" Day/Days","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
						  return;
					  }
					  
					  if(diffInDaysResidance<=obj.getTotalDays() && obj.getStrLeaveVisa()!=null && obj.getStrLeaveVisa().equalsIgnoreCase("Y"))
					  {
						  Messagebox.show("You cannot create leave request as Residence Visa of employee expires or already exipired by "+diffInDaysResidance+" Day/Days","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
						  return;
					  }
					  
					  if(diffInDaysPassport<=obj.getTotalDays() && obj.getStrLeavePassport()!=null && obj.getStrLeavePassport().equalsIgnoreCase("Y"))
					  {
						  Messagebox.show("You cannot create leave request as Passport of employee expires or already exipired by "+diffInDaysPassport+" Day/Days","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
						  return;
					  }
				  }
				  
				  				  
					if(isSalaryApproved)
					{
						Messagebox.show("Salary Sheet for the month has been Approved you cannot create leave!!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
						return;
					}
					
				
				  if(isSalaryGenerate)
		    	  {
					  Messagebox.show("Salary Sheet for this employee has been created for this month You should re-create it.Do you want to continue.?","Leave Request", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {						
							    public void onEvent(Event evt) throws InterruptedException {
							    	if (evt.getName().equals("onYes")) 
							        {
							    		saveLeaveInnerFunction();
							        }
							    	else
							    	{
							    		return;
							    	}
							    }
					  	});
							  
		    	  }
				  else{
					  saveLeaveInnerFunction(); 
				  }
			}
			else
			{
				Messagebox.show("Please select employee !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
		}
	 
		catch (Exception ex)
		{	
			logger.error("ERROR in LeaveRequestViewModel ----> submitCommand", ex);			
		}
	}
	
	private void sendEmail(String toMail,String leaveDescription,int type)
	{
		try
		{
			String[] to =null;
			String[] bcc =null;
			to= toMail.split(",");	
			MailClient mc = new MailClient();
			String subject="Leave Request Status";
			StringBuffer result=null;
			result=new StringBuffer();
			result.append("<table border='0'>");
			if(compSetup!=null && compSetup.getActivateEmail()!=null && compSetup.getActivateEmail().equalsIgnoreCase("Y"))
			{
		  	if(type==1) //Create Leave
		  	{
		  		result.append("<tr>");
		  		result.append("<td>Employee Name : "+selectedCompEmployee.getFullName()+ "</td></td><td rowspan='4'>Sent By :"+dbUser.getFirstname()+"</td>");
		  		result.append("</tr>");
		  		
		  		result.append("<tr>");			
			  	result.append("<td>Number : "+selectedCompEmployee.getEmployeeNo()+ "</td>");
			  	result.append("</tr>");
		  		
			  	result.append("<tr>");
		  		result.append("<td>Position : "+selectedCompEmployee.getPosition()+ "</td>");
		  		result.append("</tr>");
				
		  		result.append("<tr>");			
			  	result.append("<td>Department : "+selectedCompEmployee.getDepartment()+ "</td>");
			  	result.append("</tr>");

			  	
				result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
			  	
			  	result.append("<tr>");
		  		result.append("<td>"+leaveDescription+" <b>Created</b>.</td>");
		  		result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
				result.append("<tr>");			
			  	result.append("<td colspan='6'></td>");
			  	result.append("</tr>");
			  	
				result.append("<tr>");			
			  	result.append("<td colspan='2'>HR Department</td>");
			  	result.append("</tr>");
			  	result.append("<tr>");			
			  	result.append("<td>"+selectedCompany.getEnCompanyName()+"</td>");
			  	result.append("</tr>");
			  	
		  		
		  	}
		  	else if(type==2)//Approved
		  	{		  		
		  		result.append("<tr>");
		  		result.append("<td>Employee Name : "+selectedCompEmployee.getFullName()+ "</td><td rowspan='4'>Sent By :"+dbUser.getFirstname()+"</td>");
		  		result.append("</tr>");
		  
		  		result.append("<tr>");			
			  	result.append("<td>Number : "+selectedCompEmployee.getEmployeeNo()+ "</td>");
			  	result.append("</tr>");
		  	
			  	result.append("<tr>");
		  		result.append("<td>Position : "+selectedCompEmployee.getPosition()+ "</td>");
		  		result.append("</tr>");
			
		  		result.append("<tr>");			
			  	result.append("<td>Department : "+selectedCompEmployee.getDepartment()+ "</td>");
			  	result.append("</tr>");
		 		
		 		result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");	
			  	result.append("<td>"+leaveDescription+ "</td><td style='color : Green'><b>Approved.</b></td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
				result.append("<tr>");			
			  	result.append("<td colspan='6'></td>");
			  	result.append("</tr>");
			  	
				result.append("<tr>");			
			  	result.append("<td colspan='2'>HR Department</td>");
			  	result.append("</tr>");

			  	result.append("<tr>");			
			  	result.append("<td>"+selectedCompany.getEnCompanyName()+"</td>");
			  	result.append("</tr>");
		  	}
		  	else if(type==3)//Reject
		  	{	
		  		result.append("<tr>");
		  		result.append("<td>Employee Name : "+selectedCompEmployee.getFullName()+ "</td></td><td rowspan='4'>Sent By :"+dbUser.getFirstname()+"</td>");
		  		result.append("</tr>");
		  	
		  		result.append("<tr>");			
			  	result.append("<td>Number : "+selectedCompEmployee.getEmployeeNo()+ "</td>");
			  	result.append("</tr>");
		  	
			  	result.append("<tr>");
		  		result.append("<td>Position : "+selectedCompEmployee.getPosition()+ "</td>");
		  		result.append("</tr>");
				
		  		result.append("<tr>");			
			  	result.append("<td>Department : "+selectedCompEmployee.getDepartment()+ "</td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
		 		result.append("<tr>");	
			  	//result.append("<td>"+"We are sorry to inform you that "+leaveDescription+ "</td><td style='color : red'><b>Rejected.</b></td>");
			  	result.append("<td>"+"We are sorry to inform you that "+leaveDescription+ "<b style='color : red'>Rejected.</b></td>");
			  	result.append("</tr>");
			  	
				result.append("<tr>");			
			  	result.append("<td colspan='2'></td>");
			  	result.append("</tr>");
			  	
				result.append("<tr>");			
			  	result.append("<td colspan='6'></td>");
			  	result.append("</tr>");
			  	
			  	result.append("<tr>");			
			  	result.append("<td colspan='2'>HR Department</td>");
			  	result.append("</tr>");
			  	result.append("<tr>");			
			  	result.append("<td>"+selectedCompany.getEnCompanyName()+"</td>");
			  	result.append("</tr>");
		  	}		  
		  	
		 	String messageBody=result.toString();	
		 	String[] cc ={null};
		 	//we need to send the attachment also
			ArrayList fileArray = new ArrayList();
			if(randomUUIDString.equals(""))
				fileArray = new ArrayList();
			else
			{			
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"leaves"+File.separator+dbUser.getCompanyid()+File.separator;
			String filePath=repository+attFileName;
			fileArray.add(filePath);			
			}
			
			mc.sendMochaMail(to, cc,bcc,subject, messageBody,true,fileArray,true,"Leave","");
			}
			
			//mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, null);
		}
		catch (Exception e) 
			{
				 logger.info("error at LeaveRequestViewModel----> sendEmail" + e);
			}
	}
	
	@Command	
	@NotifyChange({"salaryCertificate","expPasprtMsg","expResidanceMSg","expLbrCrdMsg","expPasprt","expResidance","expLbrCrd","selectedCompEmployee","selectedReason","employeePassport","lastEmployeeLeave","employeeOutStandingLoan","empOSL","leaveDays","totalDays","selectedType","selectedReason","labourCradExpiry","residanceExpiry","passportExpiry","employeeNumber"})
	public void clearData()
	{
	   try
	   {
	  selectedCompEmployee=lstCompEmployees.get(0);
	  selectedReason=lstReason.get(0);
	  employeePassport=new PassportModel();
	  lastEmployeeLeave=new LeaveModel();	
	  employeeOutStandingLoan=0;
	  empOSL="";
	  passportExpiry="";
	  residanceExpiry="";
	  labourCradExpiry="";
	  employeeNumber="";
	  Calendar c = Calendar.getInstance();	
	  setFromDate(df.parse(sdf.format(c.getTime())));									
	  setToDate(df.parse(sdf.format(c.getTime())));	
	  leaveDays=1;
	  totalDays=0;
	  
	  	expPasprtMsg="";
		expResidanceMSg="";
		expLbrCrdMsg="";
		expPasprt=false;
		expResidance=false;
		expLbrCrd=false;
	  
	  setNote("");	  
	  otherReason="";
	  	 
	  if(lstType!=null && lstType.size()>0)
	  selectedType=lstType.get(0);
	  if(lstReason!=null && lstReason.size()>0)
		  selectedReason=lstReason.get(0);
	  
	  	totalDays=0;
	  	setCanSubmit(false);
	  	
	  	salaryCertificate=new LeaveModel();
		salaryCertificate.setLeaveStartDate(fromDate);
		salaryCertificate.setLeaveTypeDesc("");
		salaryCertificate.setStatus("");
	  	  	  
		}
		
	    catch (Exception ex)
		{	
			logger.error("ERROR in LeaveRequestViewModel ----> clearData", ex);			
		}
	}
	
	private boolean checkOverLapDates()
	{
		boolean checkOverlappingDates =false;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		LeaveModel obj=data.checkDupliacteLeaveQuery(selectedCompEmployee.getEmployeeKey(),df.format(fromDate),df.format(toDate));//changed this to read leave from leaveRquest.
		if(obj.getRecNO()>0)
		{
			checkOverlappingDates =true;
		}
		return checkOverlappingDates ;
	}
	
	@Command
	public void printSalaryCertificateCommand()
	{
		try
		{	
			if(salaryCertificate.getLeaveStartDate()==null)
			{
				Messagebox.show("Invalid Date","Salary Certificate", Messagebox.OK , Messagebox.EXCLAMATION);
				return;			
			}
			if(salaryCertificate.getLeaveTypeDesc().equals(""))
			{
				Messagebox.show("Invalid Attn. To","Salary Certificate", Messagebox.OK , Messagebox.EXCLAMATION);
				return;			
			}	
			
			/*if(salaryCertificate.getStatus().equals(""))
			{
				Messagebox.show("Invalid Subject","Salary Certificate", Messagebox.OK , Messagebox.EXCLAMATION);
				return;			
			}*/
			logger.info("getCompanyid>> " + dbUser.getCompanyid());
			
			if(dbUser.getCompanyid()==44) //Alwahdasg
			{
				arabicSalaryCertificate=false;
				createAlWahdaPdfForPrinting();
			}			
			else
			{
				//old format
			 //createPdfForPrinting();
				arabicSalaryCertificate=false;
				createAlWahdaPdfForPrinting();
			}
		}
		 catch (Exception ex)
		{	
			logger.error("ERROR in LeaveRequestViewModel ----> printSalaryCertificateCommand", ex);			
		}
		
	}
	
	@Command
	public void printArabicSalaryCertificateCommand()
	{
		try
		{	
			if(salaryCertificate.getLeaveStartDate()==null)
			{
				Messagebox.show("Invalid Date","Salary Certificate", Messagebox.OK , Messagebox.EXCLAMATION);
				return;			
			}
			if(salaryCertificate.getLeaveTypeDesc().equals(""))
			{
				Messagebox.show("Invalid Attn. To","Salary Certificate", Messagebox.OK , Messagebox.EXCLAMATION);
				return;			
			}	
			
			/*if(salaryCertificate.getStatus().equals(""))
			{
				Messagebox.show("Invalid Subject","Salary Certificate", Messagebox.OK , Messagebox.EXCLAMATION);
				return;			
			}*/
			

			if(dbUser.getCompanyid()==44) //Alwahdasg
			{
				arabicSalaryCertificate=true;
				createAlWahdaPdfForArabicPrinting();
			}	
			else
			{
				//createPdfForPrinting();
				arabicSalaryCertificate=true;
				createAlWahdaPdfForArabicPrinting();
			}
		}
		 catch (Exception ex)
		{	
			logger.error("ERROR in LeaveRequestViewModel ----> printArabicSalaryCertificateCommand", ex);			
		}
		
	}
	
	private void createAlWahdaPdfForArabicPrinting()
	{
		Document document = new Document(PageSize.A4, 40, 40, 108, 40);		
		try 
		{
		
			EmployeeModel empSalary= data.GetSalarySummary(selectedCompEmployee.getEmployeeKey());			
			Execution exec = Executions.getCurrent();
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(
							"C:/temp/SalaryCertificate.pdf"));
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		
			AlWahdaHeaderFooter event = new AlWahdaHeaderFooter();
			writer.setPageEvent(event);

			
			// various fonts
			//add this font for arabic text //trado.ttf
			BaseFont bfarabic = BaseFont.createFont("c://temp//bahijzarregular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			Font arfont = new Font(bfarabic, 16,Font.BOLD, BaseColor.BLACK );
								
		/*	BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA,
					"Cp1252", false);
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN,
					"Cp1252", false);
			BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER,
					"Cp1252", false);
			BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL,
					"Cp1252", false);
*/
		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			document.open();
			document.newPage();

			Paragraph paragraph = new Paragraph();
					
			Font f1 = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
					BaseColor.BLACK);
			
			//Chunk c = new Chunk("Salary Certificate");
			//c.setUnderline(0.1f, -2f);
			//c.setFont(f1);
			//Paragraph p = new Paragraph(c);
			//p.setAlignment(Element.ALIGN_CENTER);
			//document.add(p);
			
			//document.add( Chunk.NEWLINE );
			//document.add(new Chunk("\n"));
			
			Paragraph p = new Paragraph(new Chunk(" "));
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			
			/*------------------------------------------------------------------------*/
			PdfPTable tbl1 = new PdfPTable(1);
			//tbl1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			//tbl1.setWidths(new float[] { 1, 4 });
			tbl1.setWidthPercentage(100);

			//salaryCertificate.getLeaveTypeDesc() //Dubai Islamic Bank
			String att=salaryCertificate.getLeaveTypeDesc();			
			PdfPCell cell1 = new PdfPCell(new Phrase(att, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			String p1=" ";//add break lines
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setFixedHeight(30);
			
			tbl1.addCell(cell1);		
								
			p1="تحية طيبة و بعد ،،،";
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setFixedHeight(40);
			tbl1.addCell(cell1);									
			
			
			p1=" نشهد بأن السيد / ة";
			p1 += " "  + selectedCompEmployee.getArabicName();
			p1 +=" يعمل لدى";
			p1+=" " + selectedCompany.getArCompanyName();
			p1+=" إعتباراً  من تاريخ ";
			p1+=" " + new SimpleDateFormat("MMMM dd,yyyy").format(selectedCompEmployee.getEmployeementDate());
			p1+=" ولا يزال على رأس عمله حتى تاريخه بوظيفة ";
			p1+=" " + selectedCompEmployee.getArabicPosition();
			p1+=" ويتقاضى راتباً شهرياً و قدره ";
			p1+=" " + new DecimalFormat("#0.00").format(empSalary.getTotalSalary());
			p1+=" درهم لا غير . ";
			
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setFixedHeight(80);
			tbl1.addCell(cell1);	
			
			p1 ="وقد أعطي له هذا الكتاب بناء على طلبه لتقديمة إلى الجهة الموجهة إليه فقط دون أدنى مسؤولية على ";
			p1+=" " + selectedCompany.getArCompanyName();
			p1+=" او مسؤولية طرف الغير .";
			
			//p1=p1.replace("#Employee", selectedCompEmployee.getArabicName());
			//p1=p1.replace("#salary", new DecimalFormat("#0.00").format(empSalary.getTotalSalary()));
			//p1=p1.replace("#DOJ",new SimpleDateFormat("MMMM dd,yyyy").format(selectedCompEmployee.getEmployeementDate()));
						
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setFixedHeight(60);
			tbl1.addCell(cell1);										
			
			p1=" وتفضلوا بقبول فائق الإحترام و التقدير ،،، ";
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setFixedHeight(50);
			tbl1.addCell(cell1);	
			
			if(dbUser.getCompanyid()==44) //Alwahdasg
			{
			p1="                 دعاء عباس جاد الله";
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);			
			tbl1.addCell(cell1);	
			}
			
			p1="                ضابط إدارة الموارد البشرية";
			cell1 = new PdfPCell(new Phrase(p1, arfont)); 			
			cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setFixedHeight(50);
			tbl1.addCell(cell1);	
						
			document.add(tbl1);						
			
			//document.add( Chunk.NEWLINE );
			//document.add( Chunk.NEWLINE );						
				
			
			
			document.close();
			previewPdfForprintingInvoice();
		
		}				
		catch (Exception ex) {
			logger.error("ERROR in LeaveRequestViewModel----> createAlWahdaPdfForArabicPrinting", ex);
		}		
	}
	
	
	@SuppressWarnings("unused")
	public void createAlWahdaPdfForPrinting()
	{		
		Document document = new Document(PageSize.A4, 40, 40, 108, 40);
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		NumberToWord numbToWord=new NumberToWord();
		try 
		{
		
			EmployeeModel empSalary= data.GetSalarySummary(selectedCompEmployee.getEmployeeKey());			
			Execution exec = Executions.getCurrent();
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(
							"C:/temp/SalaryCertificate.pdf")); //	"C:/temp/invoicePDFWebApplication.pdf"));
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

			AlWahdaHeaderFooter event = new AlWahdaHeaderFooter();
			writer.setPageEvent(event);

			// various fonts
			BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA,
					"Cp1252", false);
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN,
					"Cp1252", false);
			BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER,
					"Cp1252", false);
			BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL,
					"Cp1252", false);

		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			document.open();
			document.newPage();

			Paragraph paragraph = new Paragraph();
					
			Font f1 = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
					BaseColor.BLACK);
			
			//Chunk c = new Chunk("Salary Certificate");
			//c.setUnderline(0.1f, -2f);
			//c.setFont(f1);
			//Paragraph p = new Paragraph(c);
			//p.setAlignment(Element.ALIGN_CENTER);
			//document.add(p);
			
			//document.add( Chunk.NEWLINE );
			//document.add(new Chunk("\n"));
			
			Paragraph p = new Paragraph(new Chunk(" "));
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			
			/*------------------------------------------------------------------------*/
			PdfPTable tbl1 = new PdfPTable(1);
			tbl1.setHorizontalAlignment(Element.ALIGN_LEFT);
			//tbl1.setWidths(new float[] { 1, 4 });
			tbl1.setWidthPercentage(100);

			//salaryCertificate.getLeaveTypeDesc() //Dubai Islamic Bank
			String att=salaryCertificate.getLeaveTypeDesc();
			PdfPCell cell1 = new PdfPCell(new Phrase(att, FontFactory.getFont(FontFactory.HELVETICA_BOLD))); 		
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
						
			/*cell1 = new PdfPCell(new Phrase("Abu Dhabi ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);*/
		
			
		/*	cell1 = new PdfPCell(new Phrase("United Arab Emirates ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);*/
						
			//document.add(tbl1);						
			//p = new Paragraph(new Chunk(" "));
			//p.setAlignment(Element.ALIGN_CENTER);
			//document.add(p);
			
			cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			//cell1.setColspan(2);
			tbl1.addCell(cell1);			
																
			document.add(tbl1);								

			document.add( Chunk.NEWLINE );
			document.add( Chunk.NEWLINE );
			
				paragraph=new Paragraph();	
				String p1="This to certify that Ms/Mr. #Employee has been employed with us since #DOJ . "
						+ " Her/His current monthly gross salary is AED #salary ";
				
				p1=p1.replace("#Employee", selectedCompEmployee.getFullName());
				p1=p1.replace("#salary", new DecimalFormat("#0.00").format(empSalary.getTotalSalary()));
				p1=p1.replace("#DOJ",new SimpleDateFormat("MMMM dd,yyyy").format(selectedCompEmployee.getEmployeementDate()));
				
				Chunk chunk = new Chunk(p1);				
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);					

				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );

				paragraph=new Paragraph();	
				String p2="The employee currently works for #company and Her/His present position is #position . ";
				p2=p2.replace("#company", selectedCompany.getEnCompanyName());
				p2=p2.replace("#position", selectedCompEmployee.getPosition());
				chunk = new Chunk(p2);
				
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);					
								
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );
				
				paragraph=new Paragraph();				//Al Wahda Club for Sports Games
				chunk = new Chunk("This certificate is issued at the request of the staff member for presentation to the entity addressed above only,  "
						+ " without any responsibility or liability on #company.");				
				paragraph.add(chunk.getContent().replace("#company", selectedCompany.getEnCompanyName()));				
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);
				
				document.add(new Chunk("\n\n"));
				document.add(new Chunk("\n\n"));
								
				paragraph=new Paragraph();
				Chunk chunk1 = new Chunk("Yours Sincerely,", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
				paragraph.add(chunk1);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);
				
				document.add(new Chunk("\n\n"));
				//document.add(new Chunk("\n\n"));

				if(dbUser.getCompanyid()==44) //Alwahdasg
				{
				paragraph=new Paragraph();
				chunk1 = new Chunk("Duaa Abbas Jadallah Mohd Salih", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
				paragraph.add(chunk1);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);
				}
				
				//document.add(new Chunk("\n\n"));
				
				paragraph=new Paragraph();
				chunk1 = new Chunk("HR Officer", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
				paragraph.add(chunk1);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);

				document.close();
				previewPdfForprintingInvoice();
	

		} catch (Exception ex) {
			logger.error("ERROR in CashInvoiceViewModel----> createPdfForPrinting", ex);
		}
		
	}
	
	@SuppressWarnings("unused")
	public void createPdfForPrinting()
	{		
		Document document = new Document(PageSize.A4, 40, 40, 108, 40);
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		NumberToWord numbToWord=new NumberToWord();
		try 
		{
		
			EmployeeModel empSalary= data.GetSalarySummary(selectedCompEmployee.getEmployeeKey());
			
			Execution exec = Executions.getCurrent();
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(
							"C:/temp/SalaryCertificate.pdf"));
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

			HeaderFooter event = new HeaderFooter();
			writer.setPageEvent(event);

			// various fonts
			BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA,
					"Cp1252", false);
			BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN,
					"Cp1252", false);
			BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER,
					"Cp1252", false);
			BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL,
					"Cp1252", false);

		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);

			document.open();
			document.newPage();

			Paragraph paragraph = new Paragraph();
					
			Font f1 = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
					BaseColor.BLACK);
			Chunk c = new Chunk("Salary Certificate");
			c.setUnderline(0.1f, -2f);
			c.setFont(f1);
			Paragraph p = new Paragraph(c);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			//document.add( Chunk.NEWLINE );
			//document.add(new Chunk("\n"));
			
			p = new Paragraph(new Chunk(" "));
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			
			/*------------------------------------------------------------------------*/
			PdfPTable tbl1 = new PdfPTable(2);
			tbl1.setHorizontalAlignment(Element.ALIGN_LEFT);
			tbl1.setWidths(new float[] { 1, 4 });
			tbl1.setWidthPercentage(100);

			PdfPCell cell1 = new PdfPCell(new Phrase("Date ", FontFactory.getFont(FontFactory.HELVETICA_BOLD))); 		
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase(new SimpleDateFormat("dd-MM-yyyy").format(salaryCertificate.getLeaveStartDate()), FontFactory.getFont(FontFactory.HELVETICA_BOLD))); 		
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);			
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
				

			cell1 = new PdfPCell(new Phrase("Ref. No. ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase(salaryCertificate.getRefNumber(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("Attn. To ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase(salaryCertificate.getLeaveTypeDesc(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			
			
			cell1 = new PdfPCell(new Phrase("Subject ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(salaryCertificate.getStatus(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			
			//document.add(tbl1);			
			
			//p = new Paragraph(new Chunk(" "));
			//p.setAlignment(Element.ALIGN_CENTER);
			//document.add(p);
			
			cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setColspan(2);
			tbl1.addCell(cell1);			
			
			//tbl1 = new PdfPTable(2);
			//tbl1.setWidths(new float[] { 1, 3 });			
			//tbl1.setWidthPercentage(100);
			//tbl1.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			cell1 = new PdfPCell(new Phrase("Employee No. ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(selectedCompEmployee.getEmployeeNo(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			//document.add(tbl1);
			
			cell1 = new PdfPCell(new Phrase("Employee Name ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(selectedCompEmployee.getFullName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			//document.add(tbl1);
			
			cell1 = new PdfPCell(new Phrase("Department ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(selectedCompEmployee.getDepartment(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			//document.add(tbl1);
			
			cell1 = new PdfPCell(new Phrase("Position ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(selectedCompEmployee.getPosition(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			//document.add(tbl1);
			
			cell1 = new PdfPCell(new Phrase("Joining Date ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(selectedCompEmployee.getEmployeementDateString(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			//document.add(tbl1);
		
			cell1 = new PdfPCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			cell1.setColspan(2);
			tbl1.addCell(cell1);	
			
			//p = new Paragraph(new Chunk(" "));
			//p.setAlignment(Element.ALIGN_CENTER);
			//document.add(p);
			
			//tbl1 = new PdfPTable(2);
			//tbl1.setHorizontalAlignment(Element.ALIGN_LEFT);
			//tbl1.setWidths(new float[] { 1, 3 });
			
			cell1 = new PdfPCell(new Phrase("Basic Salary ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(new DecimalFormat("#0.00").format(empSalary.getBasicSalary()), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("Total Allowance ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(new DecimalFormat("#0.00").format(empSalary.getTotalAllowance()), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("Total Salary ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(new DecimalFormat("#0.00").format(empSalary.getTotalSalary()), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			cell1 = new PdfPCell(new Phrase("In Words ", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			cell1 = new PdfPCell(new Phrase(numbToWord.GetFigToWord(empSalary.getTotalSalary()), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);
			
			document.add(tbl1);
			
			
			/*---------------------------------------------------------------*/ 

			/*PrintModel objPrint=new PrintModel();

			paragraph = new Paragraph();
			paragraph.setSpacingAfter(10);
			document.add(paragraph);
			BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
			PdfPTable table = new PdfPTable(5);
			objPrint.setSrNoWidth(0);
			objPrint.setQuantityWidth(0);
			objPrint.setRateWidth(0);
			objPrint.setAmountWidth(0);
			objPrint.setWordAmountWidth(0);
							
			if(!objPrint.isHideSrNo())
			objPrint.setSrNoWidth(40);
			if(!objPrint.isHideQuantity())
				objPrint.setQuantityWidth(40);
			if(!objPrint.isHideRate())
				objPrint.setRateWidth(60);
			if(!objPrint.isHideAmount())
				objPrint.setAmountWidth(60);
			if(!objPrint.isHideWordAmount())
				objPrint.setWordAmountWidth(350);
			

			document.add( Chunk.NEWLINE );
			document.add( Chunk.NEWLINE );
			paragraph.setSpacingAfter(10);
			document.add(paragraph);
			*/
//				paragraph=new Paragraph();
//				String amtStr1=BigDecimal.valueOf(11100).toPlainString();
//				double amtDbbl1=Double.parseDouble(amtStr1);
//				Chunk chunk = new Chunk("Amount Recived :"+formatter.format(amtDbbl1), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
//				paragraph.add(chunk);
//				paragraph.setAlignment(Element.ALIGN_RIGHT);
//				document.add(paragraph);
//				document.add( Chunk.NEWLINE );



				/*PdfPTable totaltbl = new PdfPTable(2);
				totaltbl.setWidthPercentage(100);
				totaltbl.getDefaultCell().setBorder(0);
				totaltbl.setWidths(new int[]{objPrint.getWordAmountWidth(),100});
				//totaltbl.setWidths(new int[]{350,100});
				cell1 = new PdfPCell(new Phrase("Amount in word: "+ numbToWord.GetFigToWord(54545), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setBackgroundColor(myColor);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				totaltbl.addCell(cell1);*/

				/*String amtStr2 = BigDecimal.valueOf(565656).toPlainString();
				double amtDbbl2 = Double.parseDouble(amtStr2);
				cell1 = new PdfPCell(new Phrase("Total :"+ formatter.format(amtDbbl2), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBackgroundColor(myColor);
				cell1.setBorder(0);
				totaltbl.addCell(cell1);
				if(!objPrint.isHideWordAmount())
				document.add(totaltbl);*/

				/*paragraph=new Paragraph();
				String amtStr3=BigDecimal.valueOf(87878).toPlainString();
				double amtDbbl3=Double.parseDouble(amtStr3);
				chunk = new Chunk("Balance/Ex-Change :"+formatter.format(amtDbbl3));
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_RIGHT);
				document.add(paragraph);*/

			document.add( Chunk.NEWLINE );
			document.add( Chunk.NEWLINE );
			
				paragraph=new Paragraph();				
				Chunk chunk = new Chunk("This to certify that the above employee is working in our company. We issued this certificate upon request without any responsibility toward others and dose not,in no way and under no circumstances, "
						+ "constitute and responsibility on the part of the company or any of its authorized signatories. ");
				
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);
			
			

				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );

			
				paragraph=new Paragraph();
				Chunk chunk1 = new Chunk("HR Department", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
				paragraph.add(chunk1);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);

			

			//document.add(new Chunk("\n\n"));

			PdfPTable endPage = new PdfPTable(2);
			endPage.setWidthPercentage(100);
			endPage.getDefaultCell().setBorder(0);
			endPage.setWidths(new int[]{330,120});
			
			cell1 = new PdfPCell(new Phrase("____________________\n\n "+compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);

			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			endPage.addCell(cell1);

			//String amtStr1 = BigDecimal.valueOf(toatlAmount).toPlainString();
			//double amtDbbl1 = Double.parseDouble(amtStr1);
			//cell1 = new PdfPCell(new Phrase("___________________\n\n  Customer Approval \n  Date:    /    /   "+printYear, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1 = new PdfPCell(new Phrase("____________Footer"));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			endPage.addCell(cell1);
			//document.add(endPage);

			document.close();
			previewPdfForprintingInvoice();
	

		} catch (Exception ex) {
			logger.error("ERROR in CashInvoiceViewModel----> createPdfForPrinting", ex);
		}
		
	}
	
	
	class AlWahdaHeaderFooter extends PdfPageEventHelper
	{
		@SuppressWarnings("hiding")
		public void onEndPage(PdfWriter writer, Document document) {
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = null;
			dbUser = (WebusersModel) sess.getAttribute("Authentication");
			Rectangle rect = writer.getBoxSize("art");
			Image logo = null;
			try {
				String repository = System.getProperty("catalina.base")
						+ File.separator + "uploads" + File.separator + "";
				File targetFile = new File(repository + "CompanyLogo"
						+ File.separator + dbUser.getCompanyName().trim() + "");
				File newFile = new File(targetFile.getAbsolutePath(), "logo.jpg");		
				String path = newFile.getAbsolutePath();
				logo = Image.getInstance(path);
				logo.scaleAbsolute(250, 100);
				
				
				float marginRight=rect.getRight()-100;
				
				
				 //Paragraph p = new Paragraph("Quick brown ");
				 //p.add(logo);
				    
				 if(arabicSalaryCertificate==false)
				{
				Chunk chunk = new Chunk(logo, 0, 45);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_UNDEFINED, new Phrase(chunk),marginRight, rect.getTop(), 180);				
				chunk = new Chunk( new SimpleDateFormat("MMMM dd,yyyy").format(salaryCertificate.getLeaveStartDate())); //, FontFactory.getFont(FontFactory.HELVETICA_BOLD));
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(chunk),rect.getLeft()+15, rect.getTop(), 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("Ref. No. "+salaryCertificate.getRefNumber()),rect.getLeft()+15, rect.getTop() - 15 , 0);
				}
				else					
				{
					Chunk chunk = new Chunk(logo, 0, 45);
					ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_UNDEFINED, new Phrase(chunk),20, rect.getTop(), 180);				
					chunk = new Chunk( new SimpleDateFormat("MMMM dd,yyyy").format(salaryCertificate.getLeaveStartDate())); //, FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(chunk),rect.getRight()-20, rect.getTop(), 0);	 
					ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(salaryCertificate.getRefNumber()),rect.getRight()-20, rect.getTop()-15, 0);	 
				}
				
				//chunk = new Chunk(logo,0, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(chunk),0, rect.getTop(), 0);
				
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(chunk),rect.getLeft()+15, rect.getTop(), 0);
							
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)), marginRight, rect.getTop(), 0);				
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("P.O.Box: " + compSetup.getAddress()),marginRight, rect.getTop() - 15, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("Phone: " + compSetup.getPhone1()),marginRight, rect.getTop() - 30, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("Fax: " + compSetup.getFax()),marginRight, rect.getTop() - 45, 0);
								
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("______________________________________________________________________________"),rect.getLeft(), rect.getTop() - 50, 0);
				//Calendar now = Calendar.getInstance();
				/*if (createPdfSendEmail){
					ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("This Document Does Not Require Signature")),rect.getLeft(), rect.getBottom() - 15, 0);
				}*/
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("HR Officer ")),rect.getRight(), rect.getBottom() - 30, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Printed by :"+ selectedUser.getFirstname())),(rect.getRight()), rect.getBottom() - 15, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("Powered by www.hinawi.com")),	rect.getLeft(), rect.getBottom() - 30, 0);

			} catch (BadElementException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			}
		}
	}
	
	/** Inner class to add a header and a footer. */
	class HeaderFooter extends PdfPageEventHelper {

		@SuppressWarnings("hiding")
		public void onEndPage(PdfWriter writer, Document document) {
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = null;
			dbUser = (WebusersModel) sess.getAttribute("Authentication");
			Rectangle rect = writer.getBoxSize("art");
			Image logo = null;
			try {
				String repository = System.getProperty("catalina.base")
						+ File.separator + "uploads" + File.separator + "";
				File targetFile = new File(repository + "CompanyLogo"
						+ File.separator + dbUser.getCompanyName().trim() + "");
				File newFile = new File(targetFile.getAbsolutePath(), "logo.jpg");		
				String path = newFile.getAbsolutePath();
				logo = Image.getInstance(path);
				logo.scaleAbsolute(250, 100);
				Chunk chunk = new Chunk(logo, 0, 45);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(chunk),rect.getLeft()+15, rect.getTop(), 180);
				float marginRight=rect.getRight()-150;
				
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)), marginRight, rect.getTop(), 0);
				
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("P.O.Box: " + compSetup.getAddress()),marginRight, rect.getTop() - 15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("Phone: " + compSetup.getPhone1()),marginRight, rect.getTop() - 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("Fax: " + compSetup.getFax()),marginRight, rect.getTop() - 45, 0);
								
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("______________________________________________________________________________"),rect.getLeft(), rect.getTop() - 50, 0);
				Calendar now = Calendar.getInstance();
				/*if (createPdfSendEmail){
					ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("This Document Does Not Require Signature")),rect.getLeft(), rect.getBottom() - 15, 0);
				}*/
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Date :"+ new SimpleDateFormat("dd-MM-yyyy").format(now.getTime()))),(rect.getRight()), rect.getBottom() - 30, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Printed by :"+ selectedUser.getFirstname())),(rect.getRight()), rect.getBottom() - 15, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("Powered by www.hinawi.com")),	rect.getLeft(), rect.getBottom() - 30, 0);

			} catch (BadElementException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			}
		}
	}
	
	public void previewPdfForprintingInvoice()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("filename", "SalaryCertificate");
			Executions.createComponents("/hba/payments/invoicePdfView.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> previewPdfForprintingInvoice", ex);			
		}
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


	@NotifyChange({"lstCompEmployees","selectedCompEmployee","useEncashLeave","checkUseEncash","lblMaxLeaveDays"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		checkUseEncash=false;
		NotAllowToCreateLeave=false;		
		this.selectedCompany = selectedCompany;
		compSetup=data.getLeaveCompanySetup(selectedCompany.getCompKey());
		lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"Select","A",supervisorID);
		selectedCompEmployee=lstCompEmployees.get(0);	
		
		getCompanySetup(selectedCompany.getCompKey());
		
	}

	private void getCompanySetup(int companyID)
	{
		boolean AllowMINUSLeaveDays=false; 
		int MaxMINUSLeaveDays =0;
		boolean UseEnCash =false;
		lblMaxLeaveDays="";
		//boolean NotAllowToCreateLeave =false;
		compSetup=data.getLeaveCompanySetup(companyID);
		if(compSetup.getAllowMinusLeave().equals("Y"))
			AllowMINUSLeaveDays=true;
		//MaxMINUSLeaveDays = compSetup.getMaxLeaveDays();
		
		maxLeaveDays=MaxMINUSLeaveDays;
		allowMINUSLeaveDays=AllowMINUSLeaveDays;
		
		//if(maxLeaveDays>0)
		{
			//lblMaxLeaveDays="Max. Days allowed  " + maxLeaveDays;
		}
		
		if(compSetup.getUseEncash().equals("Y"))
			UseEnCash=true;
		if(compSetup.getLeaveBeforeReturn()!=null)
		if(compSetup.getLeaveBeforeReturn().equals("Y"))
			NotAllowToCreateLeave =true;
		
		useEncashLeave=!UseEnCash;
	}

	public List<EmployeeModel> getLstCompEmployees() {
		return lstCompEmployees;
	}


	public void setLstCompEmployees(List<EmployeeModel> lstCompEmployees) {
		this.lstCompEmployees = lstCompEmployees;
	}


	public EmployeeModel getSelectedCompEmployee() {
		return selectedCompEmployee;
	}

	@NotifyChange({"selectedCompEmployee","employeePassport","lastEmployeeLeave","employeeNumber","lstType","selectedType","otherReason","empOSL"
					,"canSubmit","selectedReason","totalDays","passportExpiry","residanceExpiry","labourCradExpiry","expPasprtMsg","expPasprt","expesidanceMSg","expResidance","expLbrCrdMsg","expLbrCrd"})
	public void setSelectedCompEmployee(EmployeeModel selectedCompEmployee) 
	{	
		try
		{
		setCanSubmit(false);
		totalDays=0;
		this.selectedCompEmployee = selectedCompEmployee;
		employeePassport=data.GetEmployeePassport(selectedCompEmployee.getEmployeeKey());	
		obj=data.getexpirySettings(selectedCompany.getCompKey());
		lastEmployeeLeave=data.GetLastEmployeeLeaveQuery(selectedCompEmployee.getEmployeeKey());
		if(lastEmployeeLeave!=null)
		{
		lastEmployeeLeave.setNoOfDays(totalDays);
		if(lastEmployeeLeave.getReturnDate()!=null)
		lastEmployeeLeave.setLeavReturnDate(new SimpleDateFormat("dd-MM-yyyy").format(lastEmployeeLeave.getReturnDate()));
		else
		lastEmployeeLeave.setLeavReturnDate("");
		}
		employeeNumber=selectedCompEmployee.getEmployeeNo();
		
		LeaveExpiryModel  pssprtExpry=new LeaveExpiryModel();
		LeaveExpiryModel  reidnceExpry=new LeaveExpiryModel();
		LeaveExpiryModel  laborExpry=new LeaveExpiryModel();
		
		pssprtExpry=data.getEmployeePassportExpiryDate("A",selectedCompEmployee.getEmployeeKey());
		
		reidnceExpry=data.getEmployeeResidanceExpiryDate("A",selectedCompEmployee.getEmployeeKey());
		
		laborExpry=data.getEmployeeLabourCardExpiryDate("A",selectedCompEmployee.getEmployeeKey());
		
		Calendar c = Calendar.getInstance();		
		try {
			creationdate=df.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		expPasprtMsg="";
		expResidanceMSg="";
		expLbrCrdMsg="";
		expPasprt=false;
		expResidance=false;
		expLbrCrd=false;
		if(pssprtExpry!=null)
		{
			diffInDaysPassport=999;
			passportExpiry=pssprtExpry.getExprityDateStr();
			if(pssprtExpry.getExprityDate()!=null)
				//diffInDaysPassport = ( (pssprtExpry.getExprityDate().getTime()-creationdate.getTime()) / (1000 * 60 * 60 * 24) );
			if(diffInDaysPassport<=obj.getTotalDays() && diffInDaysPassport>=0)
			{
				 expPasprtMsg="About To Expire In " +diffInDaysPassport +" Days";
				 expPasprt=true;
			}
			else if(diffInDaysPassport<0)
			{
				 expPasprtMsg="Already Expired ";
				 expPasprt=true;
			}
			
		}
		if(reidnceExpry!=null)
		{
//			diffInDaysResidance=999;
//			residanceExpiry=reidnceExpry.getExprityDateStr();
//			if(reidnceExpry.getExprityDate()!=null)
//				diffInDaysResidance = ( (reidnceExpry.getExprityDate().getTime() - creationdate.getTime())
//	                 / (1000 * 60 * 60 * 24) );
//			if(diffInDaysResidance<=obj.getTotalDays() && diffInDaysResidance>=0)
//			{
//				expResidanceMSg="About To Expire In " + diffInDaysResidance +" Days";
//				 expResidance=true;
//			}
//			else if( diffInDaysResidance<0)
//			{
//				expResidanceMSg="Already Expired";
//				 expResidance=true;
//			}
			
		}
		
		if(laborExpry!=null)
		{
//			labourCradExpiry=laborExpry.getExprityDateStr();
//			residanceExpiry=reidnceExpry.getExprityDateStr();
//			diffInDayslabour=999;
//			if(laborExpry.getExprityDate()!=null)
//				diffInDayslabour = ( (laborExpry.getExprityDate().getTime() - creationdate.getTime())
//	                 / (1000 * 60 * 60 * 24) );
//			if(diffInDayslabour<=obj.getTotalDays() && diffInDayslabour>=0)
//			{
//				expLbrCrdMsg="About To Expire In "+diffInDayslabour+ " Days";
//				expLbrCrd=true;
//			}
//			else if( diffInDayslabour<0)
//			{
//				expLbrCrdMsg="Already Expired ";
//				expLbrCrd=true;
//			}
		}
			
			if(passportExpiry.equalsIgnoreCase(""))
			{
				passportExpiry="No Passport Available";
			}
			if(residanceExpiry.equalsIgnoreCase(""))
			{
				residanceExpiry="No Residence Available";
			}
			if(labourCradExpiry.equalsIgnoreCase(""))
			{
				labourCradExpiry="No Labor Card Available";
			}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		otherReason="";
		lstType=data.getEmployeeLeavesAllowed(selectedCompEmployee.getEmployeeKey(),df.format(fromDate),"Select type of leave..");
		selectedType=lstType.get(0);			
		if(lstReason!=null && lstReason.size()>0)
		selectedReason=lstReason.get(0);
		  
		DecimalFormat decf = new DecimalFormat("#0.00");			
		employeeOutStandingLoan=data.getEmployeeOutStandingLoans(selectedCompEmployee.getEmployeeKey());
		empOSL=decf.format(employeeOutStandingLoan);
		
		String leaveStatus="Regular";
		
		employeePassport.setStatus(leaveStatus);
		//check if Employee has already any Leave
		if(NotAllowToCreateLeave)
		{
			LeaveModel objLeave=data.GetEmployeeLeave(0, selectedCompEmployee.getEmployeeKey(),"");			
			if(objLeave.getRecNO()>0)
			{
				if(objLeave.getStatus().equals("C"))
				{
					leaveStatus="Created";
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Created) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					this.selectedType=lstType.get(0);
				}
				if(objLeave.getStatus().equals("A"))
				{
					leaveStatus="Approved";					
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Approved) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					this.selectedType=lstType.get(0);
				}
				employeePassport.setStatus(leaveStatus);
			}
		}
		
		if(employeePassport.getStatus().equals("Regular"))
		{
			setCanSubmit(true);
		}
	   }
		catch (Exception ex)
		{	
			logger.error("ERROR in LeaveRequestViewModel ----> setSelectedCompEmployee", ex);			
		}
	}
	
	@Command
	@NotifyChange({"leaveapproveOrRejectList","allLeaveapproveOrRejectList"})
	public void updateLeaveRequest(@BindingParam("cmp") Window x)
	{
	
		if(leaveDays<=0)
		{
			Messagebox.show("From Date cannot be greater than To Date, Please check !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(NotAllowToCreateLeave)
		{
			LeaveModel objLeave=data.GetEmployeeLeave(0, selectedCompEmployee.getEmployeeKey(),"");			
			if(objLeave.getRecNO()>0)
			{
				if(objLeave.getStatus().equals("C"))
				{
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Created) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(objLeave.getStatus().equals("SA") || objLeave.getStatus().equals("SSA"))
				{
					Messagebox.show(objLeave.getLeaveTypeDesc() +  " already exists for this employee.(Status : "+"Approved) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		if(selectedReason.getListId()==0)
		{
			Messagebox.show("Please select request reason !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(selectedType.getListId()==0)
		{
			Messagebox.show("Please select type of leave !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(checkUseEncash==true)
		{
			checkUseEncashValue="Y";
		}
		else
		{
			checkUseEncashValue="N";
		}
		
		int result=data.updateLeaveRequest(editLeaveRequest.getId(), fromDate, toDate,
				selectedReason.getListId(), editLeaveRequest.getReason() ,selectedType.getListId(),editLeaveRequest.getOtherLeaveReason(),checkUseEncashValue);			
		
		 if(result==1)
			{
				Clients.showNotification("The leave request Has Been Updated Successfully.",
	            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
								
				 Map args = new HashMap();
    			 BindUtils.postGlobalCommand(null, null, "refreshParentApproveOrReject", args);	
				x.detach();
			}
			else
				Clients.showNotification("Error at update leave request !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			x.detach();
	}
	
	public PassportModel getEmployeePassport() {
		return employeePassport;
	}
	public void setEmployeePassport(PassportModel employeePassport) {
		this.employeePassport = employeePassport;
	}
	public List<HRListValuesModel> getLstReason() {
		return lstReason;
	}


	public void setLstReason(List<HRListValuesModel> lstReason) {
		this.lstReason = lstReason;
	}


	public HRListValuesModel getSelectedReason() {
		return selectedReason;
	}


	public void setSelectedReason(HRListValuesModel selectedReason) {
		this.selectedReason = selectedReason;
	}
	public Date getFromDate() {
		return fromDate;
	}
	
	@NotifyChange({"toDate","fromDate","leaveDays","totalDays","lastEmployeeLeave"})
	public void setFromDate(Date fromDate) 
	{
		this.fromDate = fromDate;
			if(fromDate!=null && toDate!=null)
			{
			int Day=daysBetween(fromDate,toDate);						
			leaveDays=Day+1;
			}
			if(leaveDays<=0)
			{
				Messagebox.show("From Date cannot be greater than To Date","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
				leaveDays=0;
				return;
			}
			
			
			if(selectedType!=null && selectedType.getListId()!=0 && selectedCompEmployee!=null && selectedCompany!=null)
			{
			EmployeeActivity activity=new EmployeeActivity();
			//totalDays=activity.GetLeaveBalanceDays(selectedCompEmployee.getEmployeeKey(), selectedType.getListId(), fromDate,selectedCompany.getCompKey());
			lastEmployeeLeave.setNoOfDays(totalDays);
			setCanSubmit(true);
			}
		
		
		
	}
	public Date getToDate() {
		return toDate;
	}
	
	@NotifyChange({"toDate","fromDate","leaveDays","totalDays","lastEmployeeLeave"})
	public void setToDate(Date toDate) 
	{
		this.toDate = toDate;		
		if(fromDate!=null && toDate!=null)
		{
		int Day=daysBetween(fromDate,toDate);						
		leaveDays=Day+1;
		}
		if(selectedType!=null && selectedType.getListId()!=0 && selectedCompEmployee!=null && selectedCompany!=null)
		{
		EmployeeActivity activity=new EmployeeActivity();
		//totalDays=activity.GetLeaveBalanceDays(selectedCompEmployee.getEmployeeKey(), selectedType.getListId(), fromDate,selectedCompany.getCompKey());
		lastEmployeeLeave.setNoOfDays(totalDays);
		setCanSubmit(true);
		}
	}
	
	private int daysBetween(Date d1, Date d2){
        return 0;//( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
     }
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isCanSubmit() {
		return canSubmit;
	}
	public void setCanSubmit(boolean canSubmit) {
		this.canSubmit = canSubmit;
	}
	public List<HRListValuesModel> getLstType() {
		return lstType;
	}
	public void setLstType(List<HRListValuesModel> lstType) {
		this.lstType = lstType;
	}
	public HRListValuesModel getSelectedType() {
		return selectedType;
	}
	@NotifyChange({"selectedType","canSubmit","totalDays","lastEmployeeLeave"})
	public void setSelectedType(HRListValuesModel selectedType) 
		{
		this.selectedType = selectedType;
		
		if(selectedType.getListId()<=0)
			return;
		
		if(leaveDays<=0)
		{
			Messagebox.show("From Date cannot be greater than To Date, Please check !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		//'Checking HAJ LEAVE
		if(selectedType.getListId()==76)
		{
			LeaveModel objLeave=data.GetEmployeeLeave(selectedType.getListId(), selectedCompEmployee.getEmployeeKey(),"Haj");
			if(objLeave.getRecNO()>0)
			{
			Messagebox.show("Already taken by this employee. Please create another type of Leave !!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
			this.selectedType=lstType.get(0);
			setCanSubmit(false);
			}
			else
			{
				setCanSubmit(true);
			}
		}
		else if(selectedType.getListId()>0)
		{
		
			LeaveModel objLeave=data.GetEmployeeLeave(selectedType.getListId(), selectedCompEmployee.getEmployeeKey(),"");			
			if(objLeave.getRecNO()>0)
			{
				if(objLeave.getStatus().equals("C"))
				{
					Messagebox.show(selectedType.getEnDescription() +  " already exists for this employee.(Status : "+"Created) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					this.selectedType=lstType.get(0);
					setCanSubmit(false);
				}
				if(objLeave.getStatus().equals("A"))
				{
					Messagebox.show(selectedType.getEnDescription() +  " already exists for this employee.(Status : "+"Approved) Please complete the process of this leave or Create another type of Leave","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					this.selectedType=lstType.get(0);
					setCanSubmit(false);
				}
				
				if(objLeave.getStatus().equals("D"))
				{
					EmployeeActivity activity=new EmployeeActivity();
					//totalDays=activity.GetLeaveBalanceDays(selectedCompEmployee.getEmployeeKey(), selectedType.getListId(), fromDate,selectedCompany.getCompKey());
					lastEmployeeLeave.setNoOfDays(totalDays);
					setCanSubmit(true);
				}
				
			}
			else
			{
				EmployeeActivity activity=new EmployeeActivity();
				//totalDays=activity.GetLeaveBalanceDays(selectedCompEmployee.getEmployeeKey(), selectedType.getListId(), fromDate,selectedCompany.getCompKey());
				lastEmployeeLeave.setNoOfDays(totalDays);
				setCanSubmit(true);
			}
		}		
	}
	
	public boolean isUseEncashLeave() {
		return useEncashLeave;
	}
	public void setUseEncashLeave(boolean useEncashLeave) {
		this.useEncashLeave = useEncashLeave;
	}
	public boolean isCheckUseEncash() {
		return checkUseEncash;
	}
	public void setCheckUseEncash(boolean checkUseEncash) {
		this.checkUseEncash = checkUseEncash;
	}
	public LeaveModel getLastEmployeeLeave() {
		return lastEmployeeLeave;
	}
	public void setLastEmployeeLeave(LeaveModel lastEmployeeLeave) {
		this.lastEmployeeLeave = lastEmployeeLeave;
	}
	public int getLeaveDays() 
	{
		return leaveDays;
	}
	@NotifyChange({"toDate","leaveDays"})
	public void setLeaveDays(int leaveDays) 
	{
		this.leaveDays = leaveDays;
		 Calendar date1 = Calendar.getInstance();
		 try 
		 {
			int balance=totalDays-leaveDays;
			if(balance>0)
			{
				//there is no problem
				date1.setTime(df.parse(sdf.format(fromDate)));
				date1.add(Calendar.DAY_OF_MONTH, leaveDays-1);
				toDate=date1.getTime();	
			}
			else
			{
				//check if allowed minus leave
				if(allowMINUSLeaveDays)
				{
					//check how many days allowed over balance
					if(Math.abs(balance) > maxLeaveDays)
					{
						Messagebox.show("Max. days allowed over Leave balance is "  + maxLeaveDays  + " days !! ","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
						this.leaveDays=1;
						leaveDays=1;
						date1.setTime(df.parse(sdf.format(fromDate)));
						date1.add(Calendar.DAY_OF_MONTH, leaveDays-1);
						toDate=date1.getTime();						
					}
					else
					{
						date1.setTime(df.parse(sdf.format(fromDate)));
						date1.add(Calendar.DAY_OF_MONTH, leaveDays-1);
						toDate=date1.getTime();	
					}
				}
				else
				{
					Messagebox.show("Company not allowed negative leaved days!! ","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);	
					this.leaveDays=1;
					leaveDays=1;
					date1.setTime(df.parse(sdf.format(fromDate)));
					date1.add(Calendar.DAY_OF_MONTH, leaveDays-1);
					toDate=date1.getTime();	
				}
			}
			
			
		}
		 catch (ParseException e) 
		{
			 logger.info("error at setLeaveDays >>>" + e.getMessage());
		}
		
	}
	public double getEmployeeOutStandingLoan() {
		return employeeOutStandingLoan;
	}
	public void setEmployeeOutStandingLoan(double employeeOutStandingLoan) {
		this.employeeOutStandingLoan = employeeOutStandingLoan;
	}
	public String getOtherReason() {
		return otherReason;
	}
	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}
	public int getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmpOSL() {
		return empOSL;
	}

	public void setEmpOSL(String empOSL) {
		this.empOSL = empOSL;
	}

	public int getMaxLeaveDays() {
		return maxLeaveDays;
	}

	public void setMaxLeaveDays(int maxLeaveDays) {
		this.maxLeaveDays = maxLeaveDays;
	}

	public String getLblMaxLeaveDays() {
		return lblMaxLeaveDays;
	}

	public void setLblMaxLeaveDays(String lblMaxLeaveDays) {
		this.lblMaxLeaveDays = lblMaxLeaveDays;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	public int getEmployeeKey() {
		return employeeKey;
	}
	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}
	
	//for approve or reject leave
	
	@Command	
	@NotifyChange({"leaveapproveOrRejectList","allLeaveapproveOrRejectList","selectedReason","employeePassport","lastEmployeeLeave","empOSL","leaveDays","note","otherReason",
		"selectedType","totalDays","canSubmit","lblMaxLeaveDays","leaveDays","lstReason","totalDays","fromDate","toDate"})
	public void searchleaveapproveOrRejectList()
	{
		try
		{
			if(workGroupData.checkIfSuperSupervisor(supervisorID) && !adminUser)
			{
				leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SA",workGroupData.checkIfSuperSupervisor(supervisorID));
			}
			else if (supervisorID>0)
			{
				leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
			}
			else
			{
				leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),typeStatus,false);
			}
			allLeaveapproveOrRejectList=leaveapproveOrRejectList;
		}
	   catch (Exception e) 
		{
			 logger.info("error at LeaveRequestViewModel----> leaveapproveOrRejectList" + e.getMessage());
		}
	}
	public List<LeaveapproveOrRejectModel> getLeaveapproveOrRejectList() {
		return leaveapproveOrRejectList;
	}
	public void setLeaveapproveOrRejectList(
			List<LeaveapproveOrRejectModel> leaveapproveOrRejectList) {
		this.leaveapproveOrRejectList = leaveapproveOrRejectList;
	}
	public List<LeaveapproveOrRejectModel> getAllLeaveapproveOrRejectList() {
		return allLeaveapproveOrRejectList;
	}
	public void setAllLeaveapproveOrRejectList(
			List<LeaveapproveOrRejectModel> allLeaveapproveOrRejectList) {
		this.allLeaveapproveOrRejectList = allLeaveapproveOrRejectList;
	}
	public List<LeaveapproveOrRejectModel> getSelectedleaveapproveOrRejectList() {
		return selectedleaveapproveOrRejectList;
	}
	public void setSelectedleaveapproveOrRejectList(
			List<LeaveapproveOrRejectModel> selectedleaveapproveOrRejectList) {
		this.selectedleaveapproveOrRejectList = selectedleaveapproveOrRejectList;
	}
	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}
	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}
	public boolean isAdminUser() {
		return adminUser;
	}
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}
	
	private List<LeaveapproveOrRejectModel> filterData()
	{
		leaveapproveOrRejectList=allLeaveapproveOrRejectList;
		List<LeaveapproveOrRejectModel> lst=new ArrayList<LeaveapproveOrRejectModel>();
		for (Iterator<LeaveapproveOrRejectModel> i = leaveapproveOrRejectList.iterator(); i.hasNext();)
		{
			LeaveapproveOrRejectModel tmp=i.next();				
					if(tmp.getEmpNo().toLowerCase().contains(employeeFilter.getEmpNo().toLowerCase())&&
					tmp.getEmpName().toLowerCase().contains(employeeFilter.getEmpName().toLowerCase())&&
					tmp.getLeaveStartDate().toLowerCase().contains(employeeFilter.getLeaveStartDate().toLowerCase())&&
					tmp.getLeaveEndDate().toLowerCase().startsWith(employeeFilter.getLeaveEndDate().toLowerCase())&&
					tmp.getLeaveType().toLowerCase().contains(employeeFilter.getLeaveType().toLowerCase())&&
					tmp.getLeaveReason().toLowerCase().contains(employeeFilter.getLeaveReason().toLowerCase())&&
					tmp.getLeaveCreateDate().toLowerCase().contains(employeeFilter.getLeaveCreateDate().toLowerCase())&&
					tmp.getReason().toLowerCase().contains(employeeFilter.getReason().toLowerCase())&&
					tmp.getLeaveReason().toLowerCase().contains(employeeFilter.getLeaveReason().toLowerCase())&&
					tmp.getStatus().toLowerCase().contains(employeeFilter.getStatus().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	    @Command
	    @NotifyChange({"leaveapproveOrRejectList"})
	    public void changeFilter() 
	    {	      
	    	leaveapproveOrRejectList=filterData();
	    	
	    }
	
	    @Command
		public void editLeaveRequestCommand(@BindingParam("row") final LeaveapproveOrRejectModel row) 
	    {
			try 
			{
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("leaveId", row.getId());				
				arg.put("type", "edit");
				Executions.createComponents("/hr/reports/editLeaveRequest.zul", null,
						arg);
			} catch (Exception ex) {
				logger.error(
						"ERROR in LeaveRequestViewModel ----> editLeaveRequestCommand",
						ex);
			}
		}
	    
	    
	    @SuppressWarnings("unchecked")
		@Command
	    @NotifyChange({"leaveapproveOrRejectList","allLeaveapproveOrRejectList"})
	    public void approve(@BindingParam("row") final LeaveapproveOrRejectModel row) 
	    {	 
	    	
	    	Messagebox.show("Do you want to Approve the Employees Leave ?","Leave List", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				    	if (evt.getName().equals("onYes")) 
				        {
				    		if (row != null) 
				    		{
				    			selectedCompEmployee=data.GetEmployeeDeatailsByEmployeeKeyQuery(row.getEmp_key());
				    			row.setStatus("SSA"); // add it by default				    			
				    			if(isSuperSupervisor)
								{
				    				if(!adminUser)
				    					row.setStatus("SSA");
				    				else
				    					row.setStatus("SSA");
				    					
								}
				    			else if(supervisorID>0)
				    			{
				    				if(!adminUser)
				    					row.setStatus("SA");
				    				else
				    					row.setStatus("SSA");
				    			}
				    			else if(adminUser)
				    			{
				    				row.setStatus("SSA");
				    			}
				    	    	String empEmail=data.getEmployeeEmail(row.getEmp_key());
				    	    	data.updateLeaveForApproveOrReject(row);
				    	    	String desc="Your "+row.getLeaveType() +" you have requested From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " from web application has been ";
				    	    	String DESCRIPTIONSuper_super="The "+row.getLeaveType() +" for "+ row.getEmpName() +" requested From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " from web application has been ";
				    	    	String DESCRIPTIONSuper="The "+row.getLeaveType() +" for "+ row.getEmpName() +" requested From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " from web application has been ";
				    	    	if(!empEmail.equals(""))
				    	    	{
				    	    		//String desc="Your leave request "+row.getLeaveType() +" (" + row.getLeaveReason()+" ) From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " is approved.";
				    	    		
				    	    		sendEmail(empEmail, desc,2);
				    	    	}
				    	    	if(adminUser)
				    	    	{
				    	    		//if admin approves then send email to supervisor as well as super supervisor 
				    	    		super_SupervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSuper_supervisorId());
				    	    		if(!super_SupervisorEmail.equals(""))
				    					sendEmail(super_SupervisorEmail,DESCRIPTIONSuper_super,2);
				    	    		
				    	    		supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());
				    	    		if(!supervisorEmail.equals(""))
				    					sendEmail(supervisorEmail,DESCRIPTIONSuper,2);
				    	    	}
				    	    	else
				    	    	{
				    	    		if(workGroupData.checkIfSuperSupervisor(supervisorID))//send only to supervisor and employee if super supervisor approves.
									{
				    	    			supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());
					    	    		if(!supervisorEmail.equals(""))
					    					sendEmail(supervisorEmail,DESCRIPTIONSuper,2);
					    					
									}
					    			else if(supervisorID>0)//send only to super supervisor and employee if supervisor approves 
					    			{
					    				super_SupervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSuper_supervisorId());
					    	    		if(!super_SupervisorEmail.equals(""))
					    					sendEmail(super_SupervisorEmail,DESCRIPTIONSuper_super,2);
					    			}
				    	    		
				    	    	}
				    			 Clients.showNotification("The Leave Has Been Approved Successfully.",
				    			            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				    			 Map args = new HashMap();
				    			 BindUtils.postGlobalCommand(null, null, "refreshParentApproveOrReject", args);		
				    		}
				        }
				    	else
				    	{
				    		Map<String, Object> arg = new HashMap<String, Object>();
							arg.put("arg1112", 0);
				    	}
				    }
			});
	    	
	    
	    	
	    }
	    
	    @SuppressWarnings("unchecked")
		@Command
	    @NotifyChange({"leaveapproveOrRejectList","allLeaveapproveOrRejectList"})
	    public void reject(@BindingParam("row") final LeaveapproveOrRejectModel row) 
	    {	   
	    		    	
	    	Messagebox.show("Do you want to Reject The Employees Leave ?","Leave List", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				    	if (evt.getName().equals("onYes")) 
				        {
				    		if (row != null) {
				    			selectedCompEmployee=data.GetEmployeeDeatailsByEmployeeKeyQuery(row.getEmp_key());
				    			row.setStatus("D");
				    	    	String empEmail=data.getEmployeeEmail(row.getEmp_key());
				    	    	data.updateLeaveForApproveOrReject(row);
				    	    	String desc="your "+row.getLeaveType() +" you have requested From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " from web application has been ";
				    	    	String DESCRIPTIONSuper_super="The "+row.getLeaveType() +" for "+ row.getEmpName() +" requested From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " from web application has been ";
				    	    	String DESCRIPTIONSuper="The "+row.getLeaveType() +" for "+ row.getEmpName() +" requested From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " from web application has been ";
				    	    	if(!empEmail.equals(""))
				    	    	{
				    	    		//String desc="Your leave request "+row.getLeaveType() +" (" + row.getLeaveReason()+" ) From " + row.getLeaveStartDate() +" To " + row.getLeaveEndDate() + " is rejected.";
				    	    		
				    	    		sendEmail(empEmail, desc,3);	    		
				    	    	}
				    	    	
				    	    	if(adminUser)
				    	    	{
				    	    		//if admin approves then send email to supervisor as well as super supervisor 
				    	    		super_SupervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSuper_supervisorId());
				    	    		if(!super_SupervisorEmail.equals(""))
				    					sendEmail(super_SupervisorEmail,DESCRIPTIONSuper_super,3);
				    	    		
				    	    		supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());
				    	    		if(!supervisorEmail.equals(""))
				    					sendEmail(supervisorEmail,DESCRIPTIONSuper,3);
				    	    	}
				    	    	else
				    	    	{
				    	    		if(workGroupData.checkIfSuperSupervisor(supervisorID))//send only to supervisor and employee if super supervisor approves.
									{
				    	    			supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());
					    	    		if(!supervisorEmail.equals(""))
					    					sendEmail(supervisorEmail,DESCRIPTIONSuper,3);
					    					
									}
					    			/*else if(supervisorID>0)//send only to super supervisor and employee if supervisor approves 
					    			{
					    				super_SupervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSuper_supervisorId());
					    	    		if(!supervisorEmail.equals(""))
					    					sendEmail(super_SupervisorEmail,desc,3);
					    			}*/
				    	    		
				    	    	}
				    			 Clients.showNotification("The Leave Has Been Rejected Successfully.",
				    			            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				    			 Map args = new HashMap();
				    			 BindUtils.postGlobalCommand(null, null, "refreshParentApproveOrReject", args);		
								
				    		}
				        }
				    	else
				    	{
				    		Map<String, Object> arg = new HashMap<String, Object>();
							arg.put("arg111", 0);
				    	}
				    }
			});
	    
	    }
	    
	    
	    
	    @GlobalCommand 
		 @NotifyChange({"leaveapproveOrRejectList","allLeaveapproveOrRejectList"})
		  public void refreshParentApproveOrReject(@BindingParam("type")String type)
				  {		
					 try
					  {
							if(workGroupData.checkIfSuperSupervisor(supervisorID) && !adminUser)
							{
								leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SA",workGroupData.checkIfSuperSupervisor(supervisorID));
							}
							else if (supervisorID>0)
							{
								leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
							}
							else
							{
								leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
							}
						 allLeaveapproveOrRejectList=leaveapproveOrRejectList;
					  }
					 catch (Exception ex)
						{	
						logger.error("ERROR in LeaveRequestViewModel ----> refreshParentApproveOrReject", ex);			
						}
				  }
	 
		public String getEmployeeEmail() {
			return employeeEmail;
		}
		public void setEmployeeEmail(String employeeEmail) {
			this.employeeEmail = employeeEmail;
		}
	    
		
		public void fillTYpeOfAproveOrReject()
		{
			aprroveRejectType=new ArrayList<String>();
			aprroveRejectType.add("Make Approve/Reject List");
			aprroveRejectType.add("Only Approved List");
			aprroveRejectType.add("Only Rejected List");
			selectedAprroveRejectType=aprroveRejectType.get(0);
			typeStatus="C";
			
		}
		public List<String> getAprroveRejectType() {
			return aprroveRejectType;
		}
		public void setAprroveRejectType(List<String> aprroveRejectType) {
			this.aprroveRejectType = aprroveRejectType;
		}
		public String getSelectedAprroveRejectType() {
			return selectedAprroveRejectType;
		}
		
		 @NotifyChange({"rejectVisible","approveVisible","typeStatus","reasonReadOnly","leaveapproveOrRejectList","allLeaveapproveOrRejectList"})
		public void setSelectedAprroveRejectType(String selectedAprroveRejectType) 
		 {
			this.selectedAprroveRejectType = selectedAprroveRejectType;
			if(selectedAprroveRejectType!=null)
			{			
				if(selectedAprroveRejectType.equalsIgnoreCase("Make Approve/Reject List"))
				{
					typeStatus="C";
					approveVisible=true;
					rejectVisible=true;
					reasonReadOnly=false;					
										
					if(isSuperSupervisor && !adminUser)
					{
						leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SA",isSuperSupervisor);
					}
					else if (supervisorID>0)
					{
						leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
					}
					else
					{
						leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"C",false);
					}
					allLeaveapproveOrRejectList=leaveapproveOrRejectList;
				}
				else if(selectedAprroveRejectType.equalsIgnoreCase("Only Approved List"))
				{
					typeStatus="A";
					approveVisible=false;
					rejectVisible=false;
					reasonReadOnly=true;
					if(isSuperSupervisor && !adminUser)
					{
						leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SSA",isSuperSupervisor);
					}
					else if (supervisorID>0)
					{
						leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SA",false);
					}
					else
					{
						leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),"SSA",false);
					}
					allLeaveapproveOrRejectList=leaveapproveOrRejectList;
				}
				else if(selectedAprroveRejectType.equalsIgnoreCase("Only Rejected List"))
				{
					typeStatus="D";
					approveVisible=false;
					rejectVisible=false;
					reasonReadOnly=true;
					leaveapproveOrRejectList=data.leaveapproveOrRejectList(supervisorID,selectedCompany.getCompKey(),typeStatus,false);
					allLeaveapproveOrRejectList=leaveapproveOrRejectList;
				}
				
			}
		}
		public String getTypeStatus() {
			return typeStatus;
		}
		public void setTypeStatus(String typeStatus) {
			this.typeStatus = typeStatus;
		}
		public boolean isApproveVisible() {
			return approveVisible;
		}
		public void setApproveVisible(boolean approveVisible) {
			this.approveVisible = approveVisible;
		}
		public boolean isRejectVisible() {
			return rejectVisible;
		}
		public void setRejectVisible(boolean rejectVisible) {
			this.rejectVisible = rejectVisible;
		}
		public boolean isReasonReadOnly() {
			return reasonReadOnly;
		}
		public void setReasonReadOnly(boolean reasonReadOnly) {
			this.reasonReadOnly = reasonReadOnly;
		}
		public String getCheckUseEncashValue() {
			return checkUseEncashValue;
		}
		public void setCheckUseEncashValue(String checkUseEncashValue) {
			this.checkUseEncashValue = checkUseEncashValue;
		}
		public String getPassportExpiry() {
			return passportExpiry;
		}
		public void setPassportExpiry(String passportExpiry) {
			this.passportExpiry = passportExpiry;
		}
		public String getResidanceExpiry() {
			return residanceExpiry;
		}
		public void setResidanceExpiry(String residanceExpiry) {
			this.residanceExpiry = residanceExpiry;
		}
		public String getLabourCradExpiry() {
			return labourCradExpiry;
		}
		public void setLabourCradExpiry(String labourCradExpiry) {
			this.labourCradExpiry = labourCradExpiry;
		}
		public boolean isExpPasprt() {
			return expPasprt;
		}
		public void setExpPasprt(boolean expPasprt) {
			this.expPasprt = expPasprt;
		}
		public boolean isExpResidance() {
			return expResidance;
		}
		public void setExpResidance(boolean expResidance) {
			this.expResidance = expResidance;
		}
		public boolean isExpLbrCrd() {
			return expLbrCrd;
		}
		public void setExpLbrCrd(boolean expLbrCrd) {
			this.expLbrCrd = expLbrCrd;
		}
		public String getExpPasprtMsg() {
			return expPasprtMsg;
		}
		public void setExpPasprtMsg(String expPasprtMsg) {
			this.expPasprtMsg = expPasprtMsg;
		}
		public String getExpResidanceMSg() {
			return expResidanceMSg;
		}
		public void setExpResidanceMSg(String expResidanceMSg) {
			this.expResidanceMSg = expResidanceMSg;
		}
		public String getExpLbrCrdMsg() {
			return expLbrCrdMsg;
		}
		public void setExpLbrCrdMsg(String expLbrCrdMsg) {
			this.expLbrCrdMsg = expLbrCrdMsg;
		}
		public Date getCreationdate() {
			return creationdate;
		}
		public void setCreationdate(Date creationdate) {
			this.creationdate = creationdate;
		}
		public Date getRequestDate() {
			return requestDate;
		}
		public void setRequestDate(Date requestDate) {
			this.requestDate = requestDate;
		}
		public CompSetupModel getCompSetup() {
			return compSetup;
		}
		public void setCompSetup(CompSetupModel compSetup) {
			this.compSetup = compSetup;
		}
		public String getAttFileName() {
			return attFileName;
		}
		public void setAttFileName(String attFileName) {
			this.attFileName = attFileName;
		}
		public LeaveModel getSalaryCertificate() {
			return salaryCertificate;
		}
		public void setSalaryCertificate(LeaveModel salaryCertificate) {
			this.salaryCertificate = salaryCertificate;
		}
		
		//WebusersModel dbUser=null;
		public WebusersModel getDbUser() {
			return dbUser;
		}
		public void setDbUser(WebusersModel webusermodel) {
			this.dbUser = webusermodel;
		}
		public LeaveapproveOrRejectModel getEditLeaveRequest() {
			return editLeaveRequest;
		}
		public void setEditLeaveRequest(LeaveapproveOrRejectModel editLeaveRequest) {
			this.editLeaveRequest = editLeaveRequest;
		}
	
}
