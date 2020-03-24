package hr;

import home.MailClient;
import hr.model.CompanyModel;
import hr.model.ContactModel;
import hr.model.PassportModel;
import hr.model.SponsorModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import layout.MenuModel;
import model.CompSetupModel;
import model.EmployeeModel;
import model.ExpirySettingsModel;
import model.HRListValuesModel;
import model.LeaveExpiryModel;
import model.LoanModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;
import timesheet.TimeSheetData;

public class PassportRequestViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	
	private String employeeNumber;
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	
	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
		
	private PassportModel employeePassport;
	
	private String supervisorEmail;
	
	
	private Date creationdate; 
	
	private Date loanApplicationDate;
	
	private Date requestDate;
	private List<String> lstNoDays;
	private String selectedNoDays;
	private Date returnDate;
	private List<HRListValuesModel> lstReason;
	private HRListValuesModel selectedReason;
	
	private List<ContactModel> lstContacts;
	private SponsorModel employeeSponsor;
	
	private String note;
	
	private Date checkDate;
	private boolean canSubmit;
	
	private String empOSL;
	private double employeeOutStandingLoan;
	
	//Loan Request
	private String basicSalary;
	double totalEmpSalary;
	private String totalSalary;	
	private String salaryApproved;
	
	private double loanAmount;
	private List<String> lstMonths;
	private String selectedMonth;
	private List<String> lstYears;
	private String selectedYear;
	private List<String> lstNoOfInstallment;
	private String selectedNoOfInstallment;
	private double installmentAmount;
	private String expectedReturn;
	private  List<LoanModel> lstLoanHistory;
	private int supervisorID;
	int menuID=70;
	private int UserId;	
	private MenuModel companyRole;
	private int employeeKey;
	String viewType;
	
	private String passportExpiry="";
	
	private String residanceExpiry="";
	
	private String labourCradExpiry="";
	
	
	private boolean expPasprt=false;
	private boolean expResidance=false;
	private boolean expLbrCrd=false;
	
	private String expPasprtMsg="";
	private String expResidanceMSg="";
	private String expLbrCrdMsg="";
	
	ExpirySettingsModel obj=new ExpirySettingsModel(); 
	
	int diffInDayslabour=999;
	int diffInDaysResidance=999;//a random initialization;
	int diffInDaysPassport=999;//a random initialization;
	WebusersModel dbUser=null;
	
	private CompSetupModel compSetup;
	 	
		@Init
	    public void init(@BindingParam("type") String type)
	 	{
			try
			{
	 		logger.info("type>>> "+ type);
	 		viewType=type;
	 		
	 		if(viewType.equals("loan"))
	 			menuID=71;
	 		
	 		Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
	 		getCompanyRolePermessions(dbUser.getCompanyroleid());
	 		
	 		loadViewModel();
			}
			
			catch (Exception ex)
			{	
				logger.error("ERROR in PassportRequestViewModel ----> init", ex);			
			}
	    }
	 
		private void loadViewModel()
		{
			try
			{	
				Session sess = Sessions.getCurrent();
				dbUser=(WebusersModel)sess.getAttribute("Authentication");
				supervisorID=dbUser.getSupervisor();
				employeeKey=dbUser.getEmployeeKey();
				UserId=dbUser.getUserid();
				//getCompanyRolePermessions(dbUser.getCompanyroleid());
				
				int defaultCompanyId=0;
				defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());//we have to change later
				lstComapnies=data.getCompanyList(dbUser.getUserid());
				for (CompanyModel item : lstComapnies) 
				{
				if(item.getCompKey()==defaultCompanyId)
					selectedCompany=item;
				}
				
				if(lstComapnies.size()>=1 && selectedCompany==null)
				selectedCompany=lstComapnies.get(0);
				compSetup=data.getLeaveCompanySetup(selectedCompany.getCompKey());					
				lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"Select","A",supervisorID);
				selectedCompEmployee=lstCompEmployees.get(0);	
				
				Calendar c = Calendar.getInstance();	
				requestDate=df.parse(sdf.format(c.getTime()));	
				checkDate=df1.parse(sdf1.format(c.getTime()));
				loanApplicationDate=df.parse(sdf.format(c.getTime()));
				
				c.add(Calendar.DAY_OF_MONTH, 1);
				returnDate=df.parse(sdf.format(c.getTime()));	
									
				lstReason=data.getHRListValues(29,"Select reason for request..");
				selectedReason=lstReason.get(0);
				
				lstNoDays=new ArrayList<String>();
				for (int i = 1; i < 31; i++) 
				{
				lstNoDays.add(String.valueOf(i));	
				}
				selectedNoDays=lstNoDays.get(0);			
				employeeOutStandingLoan=0;
				
				//if(viewType.equals("loan"))
				{
					lstMonths=new ArrayList<String>();
					for (int i = 1; i < 13; i++) 
					{
					lstMonths.add(String.valueOf(i));	
					}
					selectedMonth=lstMonths.get(0);
					lstYears=new ArrayList<String>();
					for(int i=2014;i<2020;i++)
					{
						lstYears.add(String.valueOf(i));	
					}
					selectedYear=lstYears.get(0);
					lstNoOfInstallment=new ArrayList<String>();
					lstNoOfInstallment.add("Select");
					for (int i = 1; i < 51; i++) 
					{
					lstNoOfInstallment.add(String.valueOf(i));	
					}
					selectedNoOfInstallment=lstNoOfInstallment.get(0);
					
				}
				
				if(employeeKey>0)
				{
					for (EmployeeModel emp : lstCompEmployees) 
					{
						if(emp.getEmployeeKey()==employeeKey)
						{
							setSelectedCompEmployee(emp);							
							break;
						}
					} 
				}
			}
			catch (Exception ex)
			{	
				logger.error("ERROR in PassportRequestViewModel ----> loadViewModel", ex);			
			}
		}
	 	public PassportRequestViewModel()
	 	{
		
	 	}
	 	
	 	
	 	private void sendEmail(String toMail,String leaveDescription,int type,String form)
		{
			try
			{
				String[] to =null;
				String[] bcc =null;
				to= toMail.split(",");	
				MailClient mc = new MailClient();
				String subject="";
				if(form.equalsIgnoreCase("L"))
					subject="Loan Request Status";
				else
					subject="Passport Request Status";
				
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
				  	result.append("<td>"+"We are sorry to inform you that "+leaveDescription+ "</td><td style='color : red'><b>Rejected.</b></td>");
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
				mc.sendMochaMail(to, cc,bcc,subject, messageBody,true,null,true,"Passport","");
				
				//mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, null);
			}
			}
			catch (Exception e) 
				{
					 logger.info("error at LeaveRequestViewModel----> sendEmail" + e.getMessage());
				}
		}


	 	private void getCompanyRolePermessions(int companyRoleId)
		{
			setCompanyRole(new MenuModel());
			HRData data=new HRData();
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
	 	
	@Command	
	@NotifyChange({"selectedCompEmployee","employeeNumber","employeePassport","selectedReason","lstContacts","employeeSponsor","empOSL","canSubmit","basicSalary","totalSalary","salaryApproved","lstLoanHistory","note","selectedNoDays"})
	public void submitCommand()
	{
		try
		{
			if(selectedCompEmployee.getEmployeeKey()>0)
			{
				if(selectedReason.getListId()==0)
				{
					Messagebox.show("Please select request reason !!","Passport Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			
				
			 int result=data.insertPassportRequest(selectedCompEmployee.getEmployeeKey(), requestDate, returnDate, Integer.valueOf(selectedNoDays).intValue(),selectedReason.getListId(), note);
			 //String DESCRIPTION="Your Loan for "+selectedReason.getEnDescription() +" you have requested with Amount " +loanAmount+ " and Start From "+ selectedMonth + " / " + selectedYear + " and No of instalments " + selectedNoOfInstallment + " from web application has been ";
			 String DESCRIPTION="Your Passport Request for "+selectedReason.getEnDescription() +" from " + sdf.format(requestDate) + " To " + sdf.format(returnDate) + " from web application has been " ;			
			 data.addUserActivity(common.HREnum.HRFormNames.HRPassportRequest.getValue(),common.HREnum.HRStatus.HRNew.getValue(), selectedCompEmployee.getEmployeeKey(), selectedCompany.getCompKey(), DESCRIPTION, UserId);
				
			if(result==1)
			{
			
				supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());//if in case supervisor logs in and creates and leave for employee then mail should be sent to supervisor and employee.
				logger.info("supervisorEmail >> " + supervisorEmail);
				if(!supervisorEmail.equals(""))
					sendEmail(supervisorEmail,DESCRIPTION,1,"P");
				String empEmail=data.getEmployeeEmail(selectedCompEmployee.getEmployeeKey());
		    	if(!empEmail.equals(""))
		    	{
		    		sendEmail(empEmail, DESCRIPTION,1,"P");
		    	}
							
			Messagebox.show("Passport request has been created Sucessfully.","Passport Request", Messagebox.OK , Messagebox.INFORMATION);
			clearData();
			employeeNumber="";
			
			}			
			else
			{
			Messagebox.show("Error occured in passport request !!","Passport Request", Messagebox.OK , Messagebox.ERROR);
			}
		 
		 }
			else
			{
				Messagebox.show("Please select employee !!","Passport Request", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
		}
	   catch (Exception e) 
		{
			 logger.info("error at PassportRequestViewModel----> submitCommand" + e.getMessage());
		}
	}
	
	@Command	
	@NotifyChange({"selectedCompEmployee","employeeNumber","employeePassport","selectedReason","lstContacts","employeeSponsor","empOSL","canSubmit","basicSalary","totalSalary","salaryApproved","lstLoanHistory","note","selectedNoDays","loanAmount","installmentAmount","selectedNoOfInstallment","expectedReturn","selectedMonth","selectedYear","passportExpiry","residanceExpiry","labourCradExpiry","expPasprtMsg","expPasprt","expesidanceMSg","expResidance","expLbrCrdMsg","expLbrCrd"})
	public void submitLoanCommand()
	{
		try
		{
			if(selectedCompEmployee.getEmployeeKey()>0)
			{
				
								
				if(selectedReason.getListId()==0)
				{
					Messagebox.show("Please select request reason !!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
								
				Calendar c = Calendar.getInstance();
				c.setTime(selectedCompEmployee.getEmployeementDate());
				int empYear=c.get(Calendar.YEAR);
				int empMonth=c.get(Calendar.MONTH);
				if(empYear>Integer.valueOf(selectedYear) && empMonth>Integer.valueOf(selectedMonth))
				{
					Messagebox.show("Loan start before joining date !!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				
				if(loanAmount==0)
				{
					Messagebox.show("Please enter loan amount !!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(selectedNoOfInstallment.equals("Select"))
				{
					Messagebox.show("Please select No of Installment !!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				
				//check salary if generate and approved
				Calendar cal = Calendar.getInstance();
				cal.set(Integer.valueOf(selectedYear), Integer.valueOf(selectedMonth), 1);
				if(df.parse(sdf.format(cal.getTime())).before(loanApplicationDate))//loan start month is less than loan application date.
				{
					Messagebox.show("The Start From Month should not be before the Loan Request Date!!","Leave Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				cal.add(Calendar.MONTH, Integer.valueOf(selectedNoOfInstallment));				
				int fromMonth=Integer.valueOf(selectedMonth).intValue();
				int toMonth=cal.get(Calendar.MONTH);
				int fromYear=Integer.valueOf(selectedYear).intValue();
				int toYear=cal.get(Calendar.YEAR);
				if(toMonth==0)
				{
					toMonth=12;
					toYear=toYear-1;					
				}
				boolean isSalaryApproved=false;
				TimeSheetData timesheetData=new TimeSheetData();
				boolean isSalaryGenerate=false;
				isSalaryGenerate=timesheetData.checkEmployeeSalarySheet(selectedCompEmployee.getEmployeeKey(), fromMonth+1, fromYear);
				isSalaryApproved=timesheetData.checkForLoanEmployeeSalarySheetApproved(selectedCompEmployee.getEmployeeKey(),fromMonth,toMonth,fromYear,toYear);
				 obj=data.getexpirySettings(selectedCompany.getCompKey());
				  if(obj!=null)
				  {
					  if(diffInDayslabour<=obj.getTotalDays() && obj.getStrLoanLabourCrad()!=null && obj.getStrLoanLabourCrad().equalsIgnoreCase("Y"))
					  {
						  Messagebox.show("You cannot create leave request as Labour Card of employee expires or already exipired by "+diffInDayslabour+" Day/Days","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
						  return;
					  }
					  
					  if(diffInDaysResidance<=obj.getTotalDays() && obj.getStrLoanVisa()!=null && obj.getStrLoanVisa().equalsIgnoreCase("Y"))
					  {
						  Messagebox.show("You cannot create leave request as Residence Visa of employee expires or already exipired by "+diffInDaysResidance+" Day/Days","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
						  return;
					  }
					  
					  if(diffInDaysPassport<=obj.getTotalDays() && obj.getStrLoanPassport()!=null && obj.getStrLoanPassport().equalsIgnoreCase("Y"))
					  {
						  Messagebox.show("You cannot create leave request as Passport of employee expires or already exipired by "+diffInDaysPassport+" Day/Days","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
						  return;
					  }
				  }
				if(isSalaryApproved)
				{
					Messagebox.show("Salary Sheet for the loan Installments period has been Approved you cannot create loan!!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				
				 if(isSalaryGenerate)
		    	  {
						Messagebox.show(" Loan Installment start month/Year should be greater than Last Created Salary Sheet"+fromMonth+"/"+fromYear+"!!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
						return;
		    	  }
				  else{
					  loanInnerFunction(); 
				  }
		 }
			else
			{
				Messagebox.show("Please select employee !!","Loan Request", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
		}
	   catch (Exception e) 
		{
			 logger.info("error at PassportRequestViewModel----> submitCommand" + e.getMessage());
		}
	}
	
	public void loanInnerFunction()
	{
		 Messagebox.show("Do you want to Create this Loan request.?","Leave Request", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				    	if (evt.getName().equals("onYes")) 
				        {
				    		successfullConfirmatioLoad();
				        }
				    	else
				    	{
				    		return;
				    	}
				    }
		  	});
	}
	
	
	public void successfullConfirmatioLoad()
	{
		Double d1=Double.valueOf(loanAmount);
		Double d2=Double.valueOf(totalEmpSalary);
		int retval =d1.compareTo(d2);
		if(retval>0)
		{					
			//Messagebox.show("Your total slary less than loan amount !!","Loan Request", Messagebox.OK , Messagebox.INFORMATION);
			//return;
		}
		
		int result=data.InsertLoanRequest(selectedCompEmployee.getEmployeeKey(),selectedReason.getListId() ,loanAmount ,Integer.valueOf(selectedMonth).intValue(),
				Integer.valueOf(selectedYear),Integer.valueOf(selectedNoOfInstallment),installmentAmount, note);
		if(result==1)
		{	
			String DESCRIPTION="Your Loan for "+selectedReason.getEnDescription() +" you have requested with Amount " +loanAmount+ " and Start From "+ selectedMonth + " / " + selectedYear + " and No of instalments " + selectedNoOfInstallment + " from web application has been ";
		// String DESCRIPTION="Amount " +loanAmount+ " Start From "+ selectedMonth + " / " + selectedYear + " (" +selectedReason.getEnDescription() + ")" ;			
	     data.addUserActivity(common.HREnum.HRFormNames.HRLoanForm.getValue(),common.HREnum.HRStatus.HRNew.getValue(), selectedCompEmployee.getEmployeeKey(), selectedCompany.getCompKey(), DESCRIPTION, UserId);
	     
	 	supervisorEmail=data.getEmployeeEmail(selectedCompEmployee.getSupervisorId());//if in case supervisor logs in and creates and leave for employee then mail should be sent to supervisor and employee.
		logger.info("supervisorEmail >> " + supervisorEmail);
		if(!supervisorEmail.equals(""))
			sendEmail(supervisorEmail,DESCRIPTION,1,"L");
		
		String empEmail=data.getEmployeeEmail(selectedCompEmployee.getEmployeeKey());
    	if(!empEmail.equals(""))
    	{
    		sendEmail(empEmail, DESCRIPTION,1,"L");
    	}
				
		Messagebox.show("Loan request has been created sucessfully .","Loan Request", Messagebox.OK , Messagebox.INFORMATION);
		if(employeeKey>0)
		{
			searchCommand();//for level of access if employee logs in
		}
		else
		{
			clearData();
			employeeNumber="";
		}
		/*
		Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
		Center center = bl.getCenter();
		center.getChildren().clear();
		Executions.createComponents("/hr/activity/loanrequest.zul", center, null);
		*/
		}			
		else
		{
		Messagebox.show("Error occured in Loan request !!","Loan Request", Messagebox.OK , Messagebox.ERROR);
		}
	}
	
	
	
	@Command	
	@NotifyChange({"lstCompEmployees","selectedCompany","selectedCompEmployee","employeeNumber","employeePassport","lstContacts","employeeSponsor","empOSL","canSubmit","basicSalary","totalSalary","salaryApproved","lstLoanHistory","passportExpiry","residanceExpiry","labourCradExpiry","expPasprtMsg","expPasprt","expesidanceMSg","expResidance","expLbrCrdMsg","expLbrCrd"})
	public void searchCommand()
	{
		try
		{
		clearData();	
		setCanSubmit(false);
		EmployeeModel objEmp= data.GetEmployeeByEmployeeNumber(employeeNumber);
		if(objEmp.getEmployeeKey()==0)
		{
			Messagebox.show("Invalid Employee No.","Request", Messagebox.OK , Messagebox.EXCLAMATION);
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
			setSelectedCompEmployee(selectedCompEmployee);
		}
	   }
		
		employeePassport=data.GetEmployeePassport(selectedCompEmployee.getEmployeeKey());
		
		if(viewType.equals("passport"))
		{
		lstContacts=data.GetEmployeeContact(selectedCompEmployee.getEmployeeKey());
		employeeSponsor=data.GetEmployeeSponsor(selectedCompEmployee.getEmployeeKey());
		}
		
		employeeOutStandingLoan=data.getEmployeeOutStandingLoans(selectedCompEmployee.getEmployeeKey());
		DecimalFormat decf = new DecimalFormat("#0.00");
		empOSL=decf.format(employeeOutStandingLoan);
		
		if(viewType.equals("loan"))
		{
			Calendar c = Calendar.getInstance();	
			requestDate=df.parse(sdf.format(c.getTime()));	
			double[] BasicSalary= data.getEmployeeSalary(selectedCompEmployee.getEmployeeKey(), requestDate);
			basicSalary=decf.format(BasicSalary[0]);
			totalSalary=decf.format(BasicSalary[0] + BasicSalary[1]);
			totalEmpSalary=BasicSalary[0] + BasicSalary[1];
			//String strSalaryCreatedHR=data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"C");
			//if(strSalaryCreatedHR.equals(""))
			//	strSalaryCreatedHR="Not Created";
			
			String strSalaryApprovedHR= data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"A");
			if(strSalaryApprovedHR.equals(""))
				strSalaryApprovedHR="Not Approved";
			else
			strSalaryApprovedHR=strSalaryApprovedHR + " Approved";
			
			//String strSalaryPaidHR =data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"P");
			//if(strSalaryPaidHR.equals(""))
			//	strSalaryPaidHR="Not Paid";
			/*
			if(strSalaryCreatedHR.equals("Not Created"))
			{
				strSalaryCreatedHR =strSalaryApprovedHR.equals("Not Approved")?strSalaryCreatedHR : strSalaryApprovedHR;
			}
			
			if(strSalaryApprovedHR.equals("Not Approved"))
			{
				strSalaryApprovedHR =strSalaryPaidHR.equals("Not Paid")?strSalaryApprovedHR : strSalaryPaidHR;
			}
			
			if(strSalaryCreatedHR.equals("Not Created"))
			{
				strSalaryCreatedHR =strSalaryPaidHR.equals("Not Paid")?strSalaryCreatedHR : strSalaryPaidHR;
			}
			*/
			salaryApproved=strSalaryApprovedHR;
			//String GetLatestSalarySheet =strSalaryCreatedHR  +"~" + strSalaryApprovedHR  +"~" + strSalaryPaidHR;
			//logger.info(GetLatestSalarySheet);
			//logger.info(">>>"+data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"C"));
			
			lstLoanHistory=data.getLoanHistory(selectedCompEmployee.getEmployeeKey());
		}
		
		if(employeePassport.getStatus().equals("Regular"))
		{
			canSubmit=true;
		}
		}
		 catch (Exception e) 
		  {
		    logger.info("error at PassportRequestViewModel----> SearchCommand" + e.getMessage());
		  }
		
	}
	@Command
	@NotifyChange("*")//({"selectedCompEmployee","employeeNumber","employeePassport","selectedReason","lstContacts","employeeSponsor","empOSL","canSubmit","basicSalary","totalSalary","salaryApproved","lstLoanHistory","note","selectedNoDays","loanAmount"})
	public void clearDataCommand()
	{
	try 
	{
		clearData();
		employeeNumber="";
	}
	catch (Exception e) 
	{
	    logger.info("error at PassportRequestViewModel----> clearDataCommand" + e.getMessage());
	}
				
	}
	private void clearData()
	{
	  selectedCompEmployee=lstCompEmployees.get(0);
	  selectedReason=lstReason.get(0);
	  employeePassport=new PassportModel();
	  employeeSponsor=new SponsorModel();
	  lstContacts=new ArrayList<ContactModel>();
	  passportExpiry="";
	  residanceExpiry="";
	  labourCradExpiry="";
	  note="";	 
	  canSubmit=false;
	  empOSL="";
	  basicSalary="";
	  totalSalary="";
	  salaryApproved="";
	  lstLoanHistory=new ArrayList<LoanModel>();
	  selectedNoDays="1";
	  loanAmount=0;
	  installmentAmount=0;
	  selectedNoOfInstallment=lstNoOfInstallment.get(0);
	  expectedReturn="";
	  selectedMonth=lstMonths.get(0);
	  selectedYear=lstYears.get(0);
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


	@NotifyChange({"lstCompEmployees","selectedCompEmployee"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		
		this.selectedCompany = selectedCompany;
		compSetup=data.getLeaveCompanySetup(selectedCompany.getCompKey());
		lstCompEmployees=data.getEmployeeList(selectedCompany.getCompKey(),"Select","A",supervisorID);
		if(lstCompEmployees.size()>0)
		selectedCompEmployee=lstCompEmployees.get(0);		
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

	@NotifyChange({"selectedCompEmployee","employeeNumber","employeePassport","lstContacts","employeeSponsor","empOSL","canSubmit","basicSalary","totalSalary","salaryApproved","lstLoanHistory","passportExpiry","residanceExpiry","labourCradExpiry","expPasprtMsg","expPasprt","expesidanceMSg","expResidance","expLbrCrdMsg","expLbrCrd"})
	public void setSelectedCompEmployee(EmployeeModel selectedCompEmployee) 
	{	
		canSubmit=false;
		totalEmpSalary=0;
		try
		{
		this.selectedCompEmployee = selectedCompEmployee;
		employeeNumber=selectedCompEmployee.getEmployeeNo();
		employeePassport=data.GetEmployeePassport(selectedCompEmployee.getEmployeeKey());
		
		if(viewType.equals("passport"))
		{
		lstContacts=data.GetEmployeeContact(selectedCompEmployee.getEmployeeKey());
		employeeSponsor=data.GetEmployeeSponsor(selectedCompEmployee.getEmployeeKey());
		}
		
		obj=data.getexpirySettings(selectedCompany.getCompKey());
		
		employeeOutStandingLoan=data.getEmployeeOutStandingLoans(selectedCompEmployee.getEmployeeKey());
		DecimalFormat decf = new DecimalFormat("#0.00");
		empOSL=decf.format(employeeOutStandingLoan);
		
		
		if(viewType.equals("loan"))
		{
			Calendar c = Calendar.getInstance();	
			requestDate=df.parse(sdf.format(c.getTime()));	
			double[] BasicSalary= data.getEmployeeSalary(selectedCompEmployee.getEmployeeKey(), requestDate);
			basicSalary=decf.format(BasicSalary[0]);
			totalSalary=decf.format(BasicSalary[0] + BasicSalary[1]);
			totalEmpSalary=BasicSalary[0] + BasicSalary[1];
			//String strSalaryCreatedHR=data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"C");
			//if(strSalaryCreatedHR.equals(""))
			//	strSalaryCreatedHR="Not Created";
			
			String strSalaryApprovedHR= data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"A");
			if(strSalaryApprovedHR.equals(""))
				strSalaryApprovedHR="Not Approved";
			else
			strSalaryApprovedHR=strSalaryApprovedHR + " Approved";
			
			LeaveExpiryModel  pssprtExpry=new LeaveExpiryModel();
			LeaveExpiryModel  reidnceExpry=new LeaveExpiryModel();
			LeaveExpiryModel  laborExpry=new LeaveExpiryModel();
			
			pssprtExpry=data.getEmployeePassportExpiryDate("A",selectedCompEmployee.getEmployeeKey());
			
			reidnceExpry=data.getEmployeeResidanceExpiryDate("A",selectedCompEmployee.getEmployeeKey());
			
			laborExpry=data.getEmployeeLabourCardExpiryDate("A",selectedCompEmployee.getEmployeeKey());
			
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
				passportExpiry=pssprtExpry.getExprityDateStr();
				
				diffInDaysPassport=999;//a random initialization;
				if(pssprtExpry.getExprityDate()!=null)
					//diffInDaysPassport = ( (pssprtExpry.getExprityDate().getTime()-creationdate.getTime()) / (1000 * 60 * 60 * 24) );
				if(diffInDaysPassport<=obj.getTotalDays() && diffInDaysPassport>=0)
				{
					 expPasprtMsg="About To Expire In " +diffInDaysPassport +" Days";
					 expPasprt=true;
				}
				else if( diffInDaysPassport<0)
				{
					 expPasprtMsg="Already Expired ";
					 expPasprt=true;
				}
				
			}
			if(reidnceExpry!=null)
			{
				residanceExpiry=reidnceExpry.getExprityDateStr();
				 diffInDaysResidance=999;//a random initialization;
				if(reidnceExpry.getExprityDate()!=null)
					//diffInDaysResidance = ( (reidnceExpry.getExprityDate().getTime() - creationdate.getTime())
		           //      / (1000 * 60 * 60 * 24) );
				if(diffInDaysResidance<=obj.getTotalDays() && diffInDaysResidance>=0)
				{
					expResidanceMSg="About To Expire In " + diffInDaysResidance +" Days";
					 expResidance=true;
				}
				else if(diffInDaysResidance<0)
				{
					expResidanceMSg="Already Expired";
					 expResidance=true;
				}
				
			}
			
			if(laborExpry!=null)
			{
				labourCradExpiry=laborExpry.getExprityDateStr();
				
				residanceExpiry=reidnceExpry.getExprityDateStr();

				diffInDayslabour=999;
				if(laborExpry.getExprityDate()!=null)
//					diffInDayslabour = ( (laborExpry.getExprityDate().getTime() - creationdate.getTime())
//		                 / (1000 * 60 * 60 * 24) );
				if(diffInDayslabour<=obj.getTotalDays() && diffInDayslabour>=0)
				{
					expLbrCrdMsg="About To Expire In "+diffInDayslabour+ " Days";
					expLbrCrd=true;
				}
				else if(diffInDayslabour<0)
				{
					expLbrCrdMsg="Already Expired ";
					expLbrCrd=true;
				}
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
			
			//String strSalaryPaidHR =data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"P");
			//if(strSalaryPaidHR.equals(""))
			//	strSalaryPaidHR="Not Paid";
			/*
			if(strSalaryCreatedHR.equals("Not Created"))
			{
				strSalaryCreatedHR =strSalaryApprovedHR.equals("Not Approved")?strSalaryCreatedHR : strSalaryApprovedHR;
			}
			
			if(strSalaryApprovedHR.equals("Not Approved"))
			{
				strSalaryApprovedHR =strSalaryPaidHR.equals("Not Paid")?strSalaryApprovedHR : strSalaryPaidHR;
			}
			
			if(strSalaryCreatedHR.equals("Not Created"))
			{
				strSalaryCreatedHR =strSalaryPaidHR.equals("Not Paid")?strSalaryCreatedHR : strSalaryPaidHR;
			}
			*/
			salaryApproved=strSalaryApprovedHR;
			//String GetLatestSalarySheet =strSalaryCreatedHR  +"~" + strSalaryApprovedHR  +"~" + strSalaryPaidHR;
			//logger.info(GetLatestSalarySheet);
			//logger.info(">>>"+data.getEmployeeLastSalary(selectedCompEmployee.getEmployeeKey(),"C"));
			
			lstLoanHistory=data.getLoanHistory(selectedCompEmployee.getEmployeeKey());
		}
		
		if(employeePassport.getStatus().equals("Regular"))
		{
			canSubmit=true;
		}
		}
		 
		catch (Exception ex) 
		  {			 
		    logger.error("error at PassportRequestViewModel----> setSelectedCompEmployee",ex);
		  }
		
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


	public PassportModel getEmployeePassport() {
		return employeePassport;
	}


	public void setEmployeePassport(PassportModel employeePassport) {
		this.employeePassport = employeePassport;
	}


	public Date getRequestDate() {
		return requestDate;
	}

	@NotifyChange("returnDate")
	public void setRequestDate(Date requestDate) 
	{
		this.requestDate = requestDate;
		Calendar date1 = Calendar.getInstance();
		 try 
		 {
			date1.setTime(df.parse(sdf.format(requestDate)));
			date1.add(Calendar.DAY_OF_MONTH, Integer.valueOf(selectedNoDays).intValue());
			returnDate=date1.getTime();
		}
		 catch (ParseException e) 
		{
			 logger.info("error at setRequestDate >>>" + e.getMessage());
		}
		
	}


	public List<String> getLstNoDays() {
		return lstNoDays;
	}


	public void setLstNoDays(List<String> lstNoDays) {
		this.lstNoDays = lstNoDays;
	}


	public String getSelectedNoDays() {
		return selectedNoDays;
	}

	@NotifyChange("returnDate")
	public void setSelectedNoDays(String selectedNoDays) 
	{
		this.selectedNoDays = selectedNoDays;
		 Calendar date1 = Calendar.getInstance();
		 try 
		 {
			date1.setTime(df.parse(sdf.format(requestDate)));
			date1.add(Calendar.DAY_OF_MONTH, Integer.valueOf(selectedNoDays).intValue());
			returnDate=date1.getTime();
		}
		 catch (ParseException e) 
		{
			 logger.info("error at setSelectedNoDays >>>" + e.getMessage());
		}
		 
	}


	public Date getReturnDate() 
	{
		return returnDate;
	}

	@NotifyChange("selectedNoDays")
	public void setReturnDate(Date returnDate) 
	{
		
		try 
		 {
			int Day=daysBetween(requestDate,returnDate);
			//logger.info("error at setReturnDate >>>" + Day);
			if(Day>30)
			{
				Calendar date1 = Calendar.getInstance();
				date1.setTime(df.parse(sdf.format(requestDate)));
				date1.add(Calendar.DAY_OF_MONTH, 30);
				returnDate=date1.getTime();
				//logger.info("error at setReturnDate >>>" + returnDate);
				selectedNoDays="30";
			}
			else
			{			
			selectedNoDays=String.valueOf(Day);
			}
			
		 }
		catch (ParseException e) 
		{
				 logger.info("error at setReturnDate >>>" + e.getMessage());
		}	
		
		this.returnDate = returnDate;
	}
	private int daysBetween(Date d1, Date d2){
        return 0;// ( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
     }


	public Date getCheckDate() {
		return checkDate;
	}


	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}


	public List<ContactModel> getLstContacts() {
		return lstContacts;
	}


	public void setLstContacts(List<ContactModel> lstContacts) {
		this.lstContacts = lstContacts;
	}


	public SponsorModel getEmployeeSponsor() {
		return employeeSponsor;
	}


	public void setEmployeeSponsor(SponsorModel employeeSponsor) {
		this.employeeSponsor = employeeSponsor;
	}


	public boolean isCanSubmit() {
		return canSubmit;
	}


	public void setCanSubmit(boolean canSubmit) {
		this.canSubmit = canSubmit;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public double getEmployeeOutStandingLoan() {
		return employeeOutStandingLoan;
	}


	public void setEmployeeOutStandingLoan(double employeeOutStandingLoan) {
		this.employeeOutStandingLoan = employeeOutStandingLoan;
	}


	public String getEmpOSL() {
		return empOSL;
	}


	public void setEmpOSL(String empOSL) {
		this.empOSL = empOSL;
	}

	public String getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(String basicSalary) {
		this.basicSalary = basicSalary;
	}

	public String getTotalSalary() {
		return totalSalary;
	}

	public void setTotalSalary(String totalSalary) {
		this.totalSalary = totalSalary;
	}

	public String getSalaryApproved() {
		return salaryApproved;
	}

	public void setSalaryApproved(String salaryApproved) {
		this.salaryApproved = salaryApproved;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	@NotifyChange({"installmentAmount","loanAmount"})
	public void setLoanAmount(double loanAmount) 
	{
		this.loanAmount = loanAmount;
		if(totalSalary!=null && !totalSalary.equals(""))
		{
		Double d1=Double.valueOf(loanAmount);
		Double d2=Double.valueOf(totalEmpSalary);
		int retval =d1.compareTo(d2);
		if(retval>0)
		{
			//logger.info(loanAmount);
			//logger.info("totalsalary >>> "+Double.valueOf(totalSalary).doubleValue());
			//Messagebox.show("Your total slary less than loan amount !!","Loan Request", Messagebox.OK , Messagebox.INFORMATION);
			//loanAmount=Double.valueOf(totalSalary).doubleValue();
		}
		
		}
		getInstallmentExpectedReturn();
	}

	public List<String> getLstMonths() {
		return lstMonths;
	}

	public void setLstMonths(List<String> lstMonths) {
		this.lstMonths = lstMonths;
	}

	public String getSelectedMonth() {
		return selectedMonth;
	}

	@NotifyChange("expectedReturn")
	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
		getInstallmentExpectedReturn();	
	}

	public List<String> getLstYears() {
		return lstYears;
	}

	public void setLstYears(List<String> lstYears) {
		this.lstYears = lstYears;
	}

	public String getSelectedYear() {
		return selectedYear;
	}

	@NotifyChange("expectedReturn")
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
		getInstallmentExpectedReturn();	
	}

	public List<String> getLstNoOfInstallment() {
		return lstNoOfInstallment;
	}

	public void setLstNoOfInstallment(List<String> lstNoOfInstallment) {
		this.lstNoOfInstallment = lstNoOfInstallment;
	}

	public String getSelectedNoOfInstallment() {
		return selectedNoOfInstallment;
	}

	@NotifyChange({"expectedReturn","installmentAmount"})
	public void setSelectedNoOfInstallment(String selectedNoOfInstallment) 
	{
		this.selectedNoOfInstallment = selectedNoOfInstallment;
		getInstallmentExpectedReturn();			
	}
	
	
	private void getInstallmentExpectedReturn()
	{
		expectedReturn="";
		try 
		 {		
			DecimalFormat dcf=new DecimalFormat("0.00");
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.valueOf(selectedYear), Integer.valueOf(selectedMonth), 1);			
			cal.add(Calendar.MONTH, Integer.valueOf(selectedNoOfInstallment));
			if(cal.get(Calendar.MONTH)==0)
			{
				int year=cal.get(Calendar.YEAR)-1;
				expectedReturn="12" + "/" + year;
			}
			else
			{
			expectedReturn=cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
			}
			
			installmentAmount=loanAmount/Integer.valueOf(selectedNoOfInstallment);
			installmentAmount=Double.valueOf(dcf.format(installmentAmount));
		 }
		catch (Exception e) 
		{
	       logger.info("error at setSelectedNoOfInstallment >>>" + e.getMessage());
		}	
	}

	public double getInstallmentAmount() {
		return installmentAmount;
	}

	@NotifyChange({"selectedNoOfInstallment","expectedReturn"})
	public void setInstallmentAmount(double installmentAmount) 
	{
		this.installmentAmount = installmentAmount;
		try
		{
			double tmp= loanAmount/installmentAmount;
			int install=0;//  Math.round(tmp);
			logger.info("instal>>> " + install);
			selectedNoOfInstallment=String.valueOf(install); 
			
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.valueOf(selectedYear), Integer.valueOf(selectedMonth), 1);			
			cal.add(Calendar.MONTH, Integer.valueOf(selectedNoOfInstallment));
			expectedReturn=cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
		}
		catch (Exception e) 
		{
	       logger.info("error at setInstallmentAmount >>>" + e.getMessage());
		}	
	}

	public String getExpectedReturn() {
		return expectedReturn;
	}

	public void setExpectedReturn(String expectedReturn) {
		this.expectedReturn = expectedReturn;
	}

	public List<LoanModel> getLstLoanHistory() {
		return lstLoanHistory;
	}

	public void setLstLoanHistory(List<LoanModel> lstLoanHistory) {
		this.lstLoanHistory = lstLoanHistory;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
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

	public Date getLoanApplicationDate() {
		return loanApplicationDate;
	}

	public void setLoanApplicationDate(Date loanApplicationDate) {
		this.loanApplicationDate = loanApplicationDate;
	}
	
	
	
	
}
