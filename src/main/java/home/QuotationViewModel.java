package home;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;










import javax.mail.internet.InternetAddress;










import model.*;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;


public class QuotationViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	QuotationData data=new QuotationData();
	private List<String> lstFromTime;
	private List<String> lstToTime;
	private List<String> lstFromDay;
	private List<String> lstToDay;
	private List<CountryModel> lstCountry;
	private CountryModel selectedCountry;
	private List<PositionModel> lstPositions;
	private PositionModel selectedPosition;
	
	private List<String> lstBusinessType;
	private String selectedBusinessType;
	
	private List<String> lstCompanySize;
	private String selectedCompanySize;
	
	private List<String> lstSoftwareLanguage;
	private String selectedSoftwareLanguage;
	
	private List<String> lstCompanyType;
	private String selectedCompanyType;
	
	private String selectedFromTime;
	private String selectedToTime;
	private String selectedFromDay;
	private String selectedToDay;
	
	private String attFile1;
	private String attFile2;
	private String attFile3;
	private String attFile4;
	private String attFile5;
	private List<String> lstHowyouknow;
	private Set<String> selectedHowyouknow;
	
	private List<String> lstModules;
	private Set<String> selectedModules;
	
	private boolean product1;
	private String nol1;
	private String pmemo1;
	private boolean product2;
	private String nol2;
	private String pmemo2;
	private boolean product3;
	private String nol3;
	private String pmemo3;
	private boolean product4;
	private String nol4;
	private String pmemo4;
	private boolean product5;
	private String nol5;
	private String pmemo5;
	private boolean product6;
	private String nol6;
	private String pmemo6;
	private boolean product7;
	private String nol7;
	private String pmemo7;
	private boolean product8;
	private String nol8;
	private String pmemo8;
	private boolean product9;
	private String nol9;
	private String pmemo9;
	private boolean product10;
	private String nol10;
	private String pmemo10;
	
	private boolean product11;
	private String nol11;
	private String pmemo11;
	
	
	private boolean product12;
	private String nol12;
	private String pmemo12;
	
	private boolean product13;
	private String nol13;
	private String pmemo13;
	
	
	private boolean product14;
	private String nol14;
	private String pmemo14;
	
	private boolean service1;
	private String smemo1;
	private boolean service2;
	private String smemo2;
	private boolean service3;
	private String smemo3;
	private boolean service4;
	private String smemo4;
	private boolean service5;
	private String smemo5;
	
	private boolean custtype1;
	private boolean custtype2;
	private boolean custtype3;
	private boolean custtype4;
	
	private String memo1;
	private String memo2;
	private String memo3;
	private String memo4;
	private String memo5;
	private String memo6;
	private String memo7;
	private String memo8;
	private String memo9;
	private String memo10;
	private String memo11;
	private String memo12;
	private String memo13;
	private String memo14;
	
	private String otherPosition;
	private String otherBusinessType;
	
	private List<QuotationAttachmentModel> lstAtt;
	
	private QuotationModel quotModel;
	
	public QuotationViewModel()
	{
		lstFromTime=new ArrayList<String>();
		lstFromTime.add("Select");
		lstToTime=new ArrayList<String>();
		lstToTime.add("Select");
		lstFromDay=new ArrayList<String>();
		lstFromDay.add("Select");
		lstToDay=new ArrayList<String>();
		lstToDay.add("Select");
		
	for (int i = 1; i <11; i++) 
	{
	lstFromTime.add(String.valueOf(i));
	lstToTime.add(String.valueOf(i));
	}
	
	selectedFromTime=lstFromTime.get(0);
	selectedToTime=lstToTime.get(0);

	
	
	String[] lstDays={"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
	for (int i = 0; i < lstDays.length; i++)
	{
		lstFromDay.add(lstDays[i]);
		lstToDay.add(lstDays[i]);	
	}
	selectedFromDay=lstFromDay.get(0);
	selectedToDay=lstToDay.get(0);
	
	lstCountry=data.getCountries();	
	selectedCountry=lstCountry.get(0);
	
	lstPositions=data.getPositions();
	selectedPosition=lstPositions.get(0);
	
	attFile1="No file chosen";
	attFile2="No file chosen";
	attFile3="No file chosen";
	attFile4="No file chosen";
	attFile5="No file chosen";
	
	quotModel=new QuotationModel();
	quotModel.setContactTitle("Mr.");
	//quotModel.setHowyouknow("1");
	//quotModel.setService1(true);
	
	lstHowyouknow=new ArrayList<String>();
	lstHowyouknow.add("Internet Search");
	lstHowyouknow.add("E-mail Marketing");
	lstHowyouknow.add("QuickBooks Site");
	lstHowyouknow.add("Customer");
	lstHowyouknow.add("Auditor");
	lstHowyouknow.add("Other");
	
	nol1="Select";
	pmemo1="Buy License only";
	nol2="Select";
	pmemo2="Buy License only";
	nol3="Select";
	pmemo3="Buy License only";
	nol4="Select";
	pmemo4="Buy License only";
	nol5="Select";
	pmemo5="Buy License only";
	nol6="Select";
	pmemo6="Buy License only";
	nol7="Select";
	pmemo7="Buy License only";
	nol8="Select";
	pmemo8="Buy License only";
	nol9="Select";
	pmemo9="Buy License only";
	nol10="Select";
	pmemo10="Buy License only";
	nol11="Select";
	pmemo11="Buy License only";
	nol12="Select";
	pmemo12="Buy License only";
	nol13="Select";
	pmemo13="Buy License only";
	nol13="Select";
	pmemo13="Buy License only";
	nol14="Select";
	pmemo14="Buy License only";

	
	lstAtt=new ArrayList<QuotationAttachmentModel>();
	
	lstBusinessType=new ArrayList<String>();
	lstBusinessType.add("Select");
	lstBusinessType.add("Auditing, Bookkeeping");
	lstBusinessType.add("Banking");
	lstBusinessType.add("Building Construction");
	lstBusinessType.add("Construction/Utilities/Contracting A/C & Heat");
	lstBusinessType.add("Concrete Manufacturing");
	lstBusinessType.add("College, Universities & Private School");
	lstBusinessType.add("Engineering/Drafting");
	lstBusinessType.add("Equipment Rental");
	lstBusinessType.add("Factories, Manufacturing");
	lstBusinessType.add("Hotels, Motels, Residence");
	lstBusinessType.add("Hospitals, Clinic, Health Services");
	lstBusinessType.add("Insurance");
	lstBusinessType.add("Landscape Services");
	lstBusinessType.add("Marketing/Advertising");
	lstBusinessType.add("Non Profit Organization");
	lstBusinessType.add("Online Business");
	lstBusinessType.add("Oil & Gas Distribution, Production");
	lstBusinessType.add("Plumbing, Remodeling, Repair & Maintenance");
	lstBusinessType.add("Real Estate");
	lstBusinessType.add("Restaurants");
	lstBusinessType.add("Retail Sales, Warehouse Storage");
	lstBusinessType.add("Technology Services");
	lstBusinessType.add("Travel Agency");
	lstBusinessType.add("Transportation, Taxi Services");
	lstBusinessType.add("Wholesale");
	lstBusinessType.add("Others");
	selectedBusinessType=lstBusinessType.get(0);
	
	lstCompanySize=new ArrayList<String>();
	lstCompanySize.add("Select");
	lstCompanySize.add("Large");
	lstCompanySize.add("Medium");
	lstCompanySize.add("Small");
	lstCompanySize.add("Group");
	selectedCompanySize=lstCompanySize.get(0);
	
	lstSoftwareLanguage=new ArrayList<String>();
	lstSoftwareLanguage.add("Select");
	lstSoftwareLanguage.add("Arabic Only");
	lstSoftwareLanguage.add("English Only");
	lstSoftwareLanguage.add("Prefer both Arabic & English");
	selectedSoftwareLanguage=lstSoftwareLanguage.get(0);
	
	lstCompanyType=new ArrayList<String>();
	lstCompanyType.add("Select");
	lstCompanyType.add("Private");
	lstCompanyType.add("Government");
	lstCompanyType.add("Semi government");
	selectedCompanyType=lstCompanyType.get(0);		
	
	lstModules=new ArrayList<String>();
	lstModules.add("HR");
	lstModules.add("Time Sheet");
	lstModules.add("Accounting");
	lstModules.add("CRM");
	lstModules.add("Fixed Assets");
	lstModules.add("Payroll");
	lstModules.add("Real Estate");
	lstModules.add("School");
	lstModules.add("Garage");	
	}
	
	@Command
	@NotifyChange({ "quotModel" })
	public void registerCommand()
	{
	  // Messagebox.show(quotModel.getContactName());
	  //Messagebox.show(String.valueOf(data.testINsert(quotModel)));
	  prepareRegisterMail();
	  quotModel=new QuotationModel();
	  Messagebox.show("Thank you for your register. We will contact you soon !!","Explorer Computer", Messagebox.OK , Messagebox.INFORMATION);
		//Executions.sendRedirect("index.zul");
	}
	private void prepareRegisterMail()
	{
		 try 
		 {			 						 
			    			    			  
				String[] to =null;
				String[] cc ={null};
				String[] bcc =null;
							
				String toMail="hinawi@eim.ae,sales@hinawi.ae"; //"chadi.rahme@teltacworldwide.com";
				String toMail1="eng.chadi@gmail.com"; //"chadi.rahme@teltacworldwide.com";
				ArrayList fileArray = new ArrayList();
				to= toMail.split(",");	
								
				InternetAddress[] toAdd= Mailer.parse(toMail);//(InternetAddress[]) result.toArray(internetAddress);
				
				MailClient mc = new MailClient();
				String subject="A new Register inquiry has been received at hinawi.com";
				
				String custType=prepateRegisterCustomerType();
				 if(custType.equals(""))
				 {
					  Clients.showNotification("Please select if you Visitor,Customer, QuickBooks User or Other  !");
					  return;
				 }
				 if(selectedPosition.getPositionid()==0)
					{
					  Clients.showNotification("Please select a position !");
					  return;
					}
				    
				    if(selectedCountry.getCountryid()==0)
					{
					  Clients.showNotification("Please select a country !");
					  return;
					}
				    quotModel.setCusttype(custType);				    
					quotModel.setPositionId(selectedPosition.getPositionid());
					 quotModel.setCountryId(selectedCountry.getCountryid());
					 quotModel.setTelephone1(selectedCountry.getCountrycode() + "-" + quotModel.getTelephone1());					
					 quotModel.setMobile1(selectedCountry.getCountrycode() + "-" + quotModel.getMobile1());
					 if(selectedPosition.getPositionname().equals("Other"))
						 quotModel.setOtherposition(otherPosition);
					 if(selectedModules!=null)
					 {
					  String howuknow=selectedModules.toString().replace("[", "").replace("]", "");
					  quotModel.setHowyouknow(howuknow);
					 }
				 
				String messageBody="";
				StringBuffer result=new StringBuffer();
				result.append("<table border='0'>");			
				result.append("<tr><td colspan='2'>A new Register inquiry has been received with following details : </td></tr>");
				 	
					result.append("<tr><td colspan='2' style='font-weight: bold;'>Company Information:<br/><br/></td></tr>");	
					result.append("<tr><td colspan='2' style='font-weight: bold;color:red;font-size:16px;'>I am :"+custType +"<br/><br/></td></tr>");
					result.append("<tr style='background:#f5f5f5'><td>Company Name :</td><td>" + quotModel.getCompanyName() + "</td></tr>");
					result.append("<tr><td>Sender Name :</td><td>" +quotModel.getContactTitle() + " " +  quotModel.getContactName() + "</td></tr>");
					result.append("<tr style='background:#f5f5f5'><td>Position :</td><td>" + selectedPosition.getPositionname() + "</td></tr>");
					result.append("<tr><td>City :</td><td>" + quotModel.getCity() + "</td></tr>");
					result.append("<tr style='background:#f5f5f5'><td>Country :</td><td>" + selectedCountry.getCountryname() + "</td></tr>");
					result.append("<tr style='background:#f5f5f5'><td>Mobile :</td><td>" + quotModel.getMobile1() + "</td></tr>");
					result.append("<tr><td>Email :</td><td>" + quotModel.getEmail() + "</td></tr>");
					
					result.append("<tr><td>Interested in the following modules :  :</td><td>" + quotModel.getHowyouknow() + "</td></tr>");
					result.append("<tr style='background:#f5f5f5'><td>Message :</td><td>" + quotModel.getNotes() + "</td></tr>");
					result.append("</table>");	
					messageBody=result.toString();
				fileArray=null;
				//Mailer.sendMail("exchangeukedge.teltacworldwide.co", "reports@teltacworldwide.com", toAdd, 
				//		null, null, subject, messageBody, true, fileArray, true);
				
				 //mc.sendMail("exchangeukedge.teltacworldwide.co", "reports@teltacworldwide.com", to, subject, messageBody, null);
				
				 mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,fileArray,false,"quotation","");
				//mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, null);
				//mc.sendMail(host,"eng.chadi@gmail.com", to,"subject","result", null);
			} 
			  catch (Exception ex) {
					StringWriter sw = new StringWriter();
					  ex.printStackTrace(new PrintWriter(sw)); 
					  Messagebox.show(sw.toString());
					  //logger.logErrorMsg(sw.toString(),"VendorIncreases-->SendEmail ");
			  }	
		 //Messagebox.show("Thank you.Your request is being sent.");
	}
	public Validator getRegisterTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				QuotationModel fx = (QuotationModel)ctx.getProperty().getValue();
				//get filed firstname of the form
				String companyName = fx.getCompanyName();
				String contactName = fx.getContactName();				
				String email = fx.getEmail();
				String mobile1 = fx.getMobile1();
				
				if(Strings.isBlank(contactName) || Strings.isBlank(email) || Strings.isBlank(companyName) || Strings.isBlank(mobile1))
				{
					Clients.showNotification("Please fill all the required fields (*)  !!");
					//mark the validation is invalid, so the data will not update to bean
					//and the further command will be skipped.
					ctx.setInvalid();
				}											
			}
		};
	}
	
	@Command 	
	public void testsendMail()
	{
		try
		{
		//prepareQuotationMail(30);
			String x="sds";
			int y=Integer.parseInt(x);
		MailClient mc = new MailClient();
		String[] to =null;
		String[] cc ={null};
		String[] bcc =null;
		
		String toMail1="eng.chadi@gmail.com,hinawi@eim.ae,sales@hinawi.ae"; //"chadi.rahme@teltacworldwide.com";
		String toMail="eng.chadi@gmail.com"; //"chadi.rahme@teltacworldwide.com";
		to= toMail.split(",");	
		
		
	//	mc.sendGmailMail("", "eng.chadi@gmail.com", to, "Test", "hinawi2.com", null);
		mc.sendMochaMail(to, cc,bcc,"Test Moca", "ssss", true, null, false,"quotation","");
		//int val=data.testINsert();
		Messagebox.show("Email is send..");
		}
		 catch (Exception ex) {
				StringWriter sw = new StringWriter();
				  ex.printStackTrace(new PrintWriter(sw)); 
				  Messagebox.show(sw.toString());
				  logger.error("ERROR in QuotationViewModel ----> testsendMail", ex);	
		  }	
		
	}
	
	@Command 	
	public void clearCommand()
	{
	  //Messagebox.show(quotModel.getCompanyName());
	  //Messagebox.show(String.valueOf(data.testINsert(quotModel)));
	   //old
		//Executions.sendRedirect("request.zul");
	   Executions.sendRedirect("quotationContactUsLayout.zul");
	   
	}
	
	@Command 	
	public void sendMail()
	{		
		 try
		 {		
			 String custType=prepateCustomerType();
			 if(custType.equals(""))
			 {
				  Clients.showNotification("Please select if you Visitor,Customer,Reseller or QuickBooks User  !");
				  return;
			 }
			 if(product1==false && product2==false && product3==false && product4==false && product5==false && product6==false && product7==false && product8==false && product9==false && product10==false && product11==false && product12==false && product13==false && product14==false)
			 {
				  Clients.showNotification("Please select the Product");
				  return;
			 }
			    if(selectedPosition.getPositionid()==0)
				{
				  Clients.showNotification("Please select a position !");
				  return;
				}
			    
			    if(selectedCountry.getCountryid()==0)
				{
				  Clients.showNotification("Please select a country !");
				  return;
				}
			    
			    if(checkProductList()==false)
			    	return;
			 
			 quotModel.setPositionId(selectedPosition.getPositionid());
			 quotModel.setPositionName(selectedPosition.getPositionname());
			 quotModel.setCountryId(selectedCountry.getCountryid());
			 quotModel.setCountryName(selectedCountry.getCountryname());			 
			 quotModel.setTelephone1(selectedCountry.getCountrycode() + "-" + quotModel.getTelephone1());
			 quotModel.setTelephone2(selectedCountry.getCountrycode() + "-" + quotModel.getTelephone2());
			 quotModel.setMobile1(selectedCountry.getCountrycode() + "-" + quotModel.getMobile1());
			 quotModel.setMobile2(selectedCountry.getCountrycode() + "-" + quotModel.getMobile2());
			 if(selectedHowyouknow!=null)
			 {
				String howuknow=selectedHowyouknow.toString().replace("[", "").replace("]", "");
			 quotModel.setHowyouknow(howuknow);
			 }
			 quotModel.setFromTime(selectedFromTime);
			 quotModel.setToTime(selectedToTime);
			 quotModel.setFromDay(selectedFromDay);
			 quotModel.setToDay(selectedToDay);
			 quotModel.setCusttype(custType);			 			
			 
			 quotModel.setBusinesstype(selectedBusinessType);
			 quotModel.setCompanysize(selectedCompanySize);
			 quotModel.setCompanytype(selectedCompanyType);
			 quotModel.setSoftwareLanguage(selectedSoftwareLanguage);
			 
			 if(selectedPosition.getPositionname().equals("Other"))
			 quotModel.setOtherposition(otherPosition);
			 if(selectedBusinessType.equals("Others"))
				quotModel.setBusinesstype(otherBusinessType);
			 
			 //save in online SQL tables
			 data.insertWebQuotation(quotModel,prepareProductList(),prepareServiceList(),lstAtt);
			 
			 int quotationid= data.insertQuotation(quotModel,prepareProductList(),prepareServiceList(),lstAtt);
			 if(quotationid>0)
			 {
				 //Send email
				 	prepareQuotationMail(quotationid);
				    sendClientEmail(quotModel.getEmail());
			 }
			 
			 //Messagebox.show("Done..");
			  Window window = (Window)Executions.createComponents(
		                "thankyou.zul", null, null);
		        window.doModal();
		 }
		 
		 catch (Exception e) 
	 		{		 	  
		 	   e.printStackTrace();
		 	   Messagebox.show(e.getMessage());
		 	  }		 	  		 	   		  	 		
	}
	private boolean checkProductList()
	{
		boolean isValid=true;
		if(product1)
		 {
			if(nol1.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Hinawi Business Managment ( Hinawi ERP-1 )) ","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product2)
		 {
			if(nol2.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Fixed Asset Module)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);					
				return false;
			}
		 }
		if(product3)
		 {
			if(nol3.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Real Estate Module)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		if(product4)
		 {
			if(nol4.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Garage Module)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product11)
		 {
			if(nol11.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (HRMS Module)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product8)
		 {
			if(nol8.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Hinawi Business Managment ( Hinawi ERP-1 )) ","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product9)
		 {
			if(nol9.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Fixed Asset Module Web Application)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);					
				return false;
			}
		 }
		if(product10)
		 {
			if(nol10.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Real Estate Module Web Application)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		if(product12)
		 {
			if(nol2.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Garage Module Web Application)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product13)
		 {
			if(nol13.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (HRMS Module Web Application)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product14)
		 {
			if(nol14.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (Others)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		
		if(product5)
		 {
			if(nol5.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (QuickBooks Pro)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		if(product6)
		 {
			if(nol6.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (QuickBooks Premier)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);				
				return false;
			}
		 }
		if(product7)
		 {
			if(nol7.equals("Select"))
			{
				Messagebox.show("Select No of License of Product (QuickBooks Enterprise)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);			
				return false;
			}
		 }
		
		if(service1)
		{
			if(Strings.isBlank(smemo1))
			{
				Messagebox.show("Please fill memo in service (Training and Support)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);			
				return false;
			}
		}
		if(service2)
		{
			if(Strings.isBlank(smemo2))
			{
				Messagebox.show("Please fill memo in service (Consulting and Analysis)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);			
				return false;
			}
		}
		if(service3)
		{
			if(Strings.isBlank(smemo3))
			{
				Messagebox.show("Please fill memo in service (Accounting Services)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);			
				return false;
			}
		}
		if(service4)
		{
			if(Strings.isBlank(smemo4))
			{
				Messagebox.show("Please fill memo in service (Special Programming)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);			
				return false;
			}
		}
		if(service5)
		{
			if(Strings.isBlank(smemo5))
			{
				Messagebox.show("Please fill memo in service (Converting and Importing of Data)","ContactUs", Messagebox.OK , Messagebox.EXCLAMATION);			
				return false;
			}
		}
		
		return isValid;
				
	}
	private List<QuotationProductModel> prepareProductList()
	{
		List<QuotationProductModel> lstProduct=new ArrayList<QuotationProductModel>();
		 if(product1)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Hinawi Business Managment ( Hinawi ERP-1 )");
			 obj.setNooflicense(nol1);
			 if(pmemo1.equals("Others"))
				 obj.setMemo(memo1);
			 else
			 obj.setMemo(pmemo1);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product2)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Fixed Asset Module");
			 obj.setNooflicense(nol2);
			 if(pmemo2.equals("Others"))
				 obj.setMemo(memo2);
			 else			 
			 obj.setMemo(pmemo2);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product3)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Real Estate Module");
			 obj.setNooflicense(nol3);
			 if(pmemo3.equals("Others"))
				 obj.setMemo(memo3);
			 else			 
			 obj.setMemo(pmemo3);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product4)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Garage Module");
			 obj.setNooflicense(nol4);
			 if(pmemo4.equals("Others"))
				 obj.setMemo(memo4);
			 else			 
			 obj.setMemo(pmemo4);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product5)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("QuickBooks Pro");
			 obj.setNooflicense(nol5);
			 if(pmemo5.equals("Others"))
				 obj.setMemo(memo5);
			 else			 
			 obj.setMemo(pmemo5);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product6)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("QuickBooks Premier");
			 obj.setNooflicense(nol6);
			 if(pmemo6.equals("Others"))
				 obj.setMemo(memo6);
			 else			 
			 obj.setMemo(pmemo6);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product7)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("QuickBooks Enterprise");
			 obj.setNooflicense(nol7);
			 if(pmemo7.equals("Others"))
				 obj.setMemo(memo7);
			 else			 
			 obj.setMemo(pmemo7);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product8)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Hinawi Web Application");
			 obj.setNooflicense(nol8);
			 if(pmemo8.equals("Others"))
				 obj.setMemo(memo8);
			 else			 
			 obj.setMemo(pmemo8);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product9)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Fixed Asset Module Online");
			 obj.setNooflicense(nol9);
			 if(pmemo9.equals("Others"))
				 obj.setMemo(memo9);
			 else			 
			 obj.setMemo(pmemo9);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 if(product10)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Real Estate Module Online");
			 obj.setNooflicense(nol10);
			 if(pmemo10.equals("Others"))
				 obj.setMemo(memo10);
			 else			 
			 obj.setMemo(pmemo10);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 
		 if(product11)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("HRMS Module Online");
			 obj.setNooflicense(nol11);
			 if(pmemo11.equals("Others"))
				 obj.setMemo(memo11);
			 else			 
			 obj.setMemo(pmemo11);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 
		 
		 if(product12)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Garage Module Online");
			 obj.setNooflicense(nol12);
			 if(pmemo11.equals("Others"))
				 obj.setMemo(memo12);
			 else			 
			 obj.setMemo(pmemo12);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 
		 
		 if(product13)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("HRMS Module Online");
			 obj.setNooflicense(nol13);
			 if(pmemo13.equals("Others"))
				 obj.setMemo(memo13);
			 else			 
			 obj.setMemo(pmemo13);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 
		 if(product14)
		 {
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Others");
			 obj.setNooflicense(nol14);
			 if(pmemo14.equals("Others"))
				 obj.setMemo(memo14);
			 else			 
			 obj.setMemo(pmemo14);
			 obj.setType("p");
			 lstProduct.add(obj);
		 }
		 
		 return lstProduct;
	}
	
	private List<QuotationProductModel> prepareServiceList()
	{
		List<QuotationProductModel> lstService=new ArrayList<QuotationProductModel>();
		if(service1)
		{
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Training and Support");
			 obj.setNooflicense("");
			 obj.setMemo(smemo1);
			 obj.setType("s");
			 lstService.add(obj);
		}
		if(service2)
		{
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Consulting and Analysis");
			 obj.setNooflicense("");
			 obj.setMemo(smemo2);
			 obj.setType("s");
			 lstService.add(obj);
		}
		if(service3)
		{
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Accounting Services");
			 obj.setNooflicense("");
			 obj.setMemo(smemo3);
			 obj.setType("s");
			 lstService.add(obj);
		}
		if(service4)
		{
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Special Programming");
			 obj.setNooflicense("");
			 obj.setMemo(smemo4);
			 obj.setType("s");
			 lstService.add(obj);
		}
		if(service5)
		{
			 QuotationProductModel obj=new QuotationProductModel();
			 obj.setDescription("Converting and Importing of Data");
			 obj.setNooflicense("");
			 obj.setMemo(smemo5);
			 obj.setType("s");
			 lstService.add(obj);
		}
		
		return lstService;
		
	}
	private String prepateRegisterCustomerType()
	{
		
		String type="";
		List<String> lstType=new ArrayList<String>();
		if(custtype1)
			lstType.add("Visitor");
		if(custtype2)
			lstType.add("Customer");
		if(custtype3)
			lstType.add("Others");
		if(custtype4)
			lstType.add("QuickBooks User");
		type=lstType.toString();
		
		if(type.length()>0)
		{
			type=type.replace("[", "").replace("]", "");
		}
		return type;
	}
	private String prepateCustomerType()
	{
		
		String type="";
		List<String> lstType=new ArrayList<String>();
		if(custtype1)
			lstType.add("Visitor");
		if(custtype2)
			lstType.add("Customer");
		if(custtype3)
			lstType.add("Reseller");
		if(custtype4)
			lstType.add("QuickBooks User");
		type=lstType.toString();
		
		if(type.length()>0)
		{
			type=type.replace("[", "").replace("]", "");
		}
		return type;
	}
	//the validator is the class to validate data before set ui data back to todo
			public Validator getTodoValidator(){
				return new AbstractValidator() {							
					public void validate(ValidationContext ctx) {
						//get the form that will be applied to todo
						//QuotationModel fx = (QuotationModel)ctx.getProperty().getValue();						
						//get filed firstname of the form
						String companyName =(String)ctx.getProperties("companyName")[0].getValue(); //fx.getCompanyName();
						String contactName = (String)ctx.getProperties("contactName")[0].getValue();//fx.getContactName();
						String city = (String)ctx.getProperties("city")[0].getValue();//fx.getCity();
						String telephone1 = (String)ctx.getProperties("telephone1")[0].getValue();//fx.getTelephone1();
						String mobile1 = (String)ctx.getProperties("mobile1")[0].getValue();//fx.getMobile1();
						String email =(String)ctx.getProperties("email")[0].getValue();// fx.getEmail();						
						
						if(Strings.isBlank(companyName) || Strings.isBlank(contactName) || Strings.isBlank(city) || Strings.isBlank(telephone1)|| Strings.isBlank(mobile1)
								|| Strings.isBlank(email))
						{
							Clients.showNotification("Please fill all the required fields (*)  !!");
							//mark the validation is invalid, so the data will not update to bean
							//and the further command will be skipped.
							ctx.setInvalid();
						}
						
						
						if(Strings.isBlank(companyName))
						{
							  addInvalidMessage(ctx, "lastnameContentError", "Company Name is required !");
						}
					}
				};
			}
	
	@Command 
	@NotifyChange({"attFile1","attFile2","attFile3","attFile4","attFile5"})
	public void uploadFile(BindContext ctx,@BindingParam("attId") String attId )
	{
		UploadEvent event = (UploadEvent)ctx.getTriggerEvent();
		String filePath="";
		String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator;
		//Session session = (Session) (Executions.getCurrent()).getDesktop().getId();//.getSession().getNativeSession();
	
		  Session sess = Sessions.getCurrent();
		
		
		 //logger.info("sessionId >> " + saa.);
		  //HttpSession hses = (HttpSession) sess.getNativeSession();
		
		 
		String sessID=(Executions.getCurrent()).getDesktop().getId();
		logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
		String dirPath=repository+sessID;//session.getId();
		File dir = new File(dirPath);
		 
		//File dir = new File(System.getProperty("catalina.base"), "uploads");
		if(!dir.exists())
			dir.mkdirs();
			
		filePath = dirPath +File.separator +attId + "." +event.getMedia().getFormat();	 
		//Messagebox.show(filePath);
		//if(event.getMedia().getFormat().equals("txt"))
		//createFile(event.getMedia().getByteData(), filePath);
		//else
		createFile(event.getMedia().getStreamData(), filePath);
		
		logger.info(filePath);
		
		if(attId.equals("1"))
		{
		attFile1=event.getMedia().getName();
		QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
		objAtt.setFilename(attFile1);
		objAtt.setFilepath(attId + "." +event.getMedia().getFormat());
		objAtt.setSessionid(sessID);
		lstAtt.add(objAtt);
		}
		if(attId.equals("2"))
		{
		attFile2=event.getMedia().getName();
		QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
		objAtt.setFilename(attFile2);
		objAtt.setFilepath(attId + "." +event.getMedia().getFormat());
		objAtt.setSessionid(sessID);
		lstAtt.add(objAtt);
		}
		if(attId.equals("3"))
		{
		attFile3=event.getMedia().getName();
		QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
		objAtt.setFilename(attFile3);
		objAtt.setFilepath(attId + "." +event.getMedia().getFormat());
		objAtt.setSessionid(sessID);
		lstAtt.add(objAtt);
		}
		if(attId.equals("4"))
		{
		attFile4=event.getMedia().getName();
		QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
		objAtt.setFilename(attFile4);
		objAtt.setFilepath(attId + "." +event.getMedia().getFormat());
		objAtt.setSessionid(sessID);
		lstAtt.add(objAtt);
		}
		if(attId.equals("5"))
		{
		attFile5=event.getMedia().getName();
		QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
		objAtt.setFilename(attFile5);
		objAtt.setFilepath(attId + "." +event.getMedia().getFormat());
		objAtt.setSessionid(sessID);
		lstAtt.add(objAtt);
		}
		/*
		if(attId.equals("2"))
			attFile2=event.getMedia().getName();
		if(attId.equals("3"))
			attFile3=event.getMedia().getName();
		if(attId.equals("4"))
			attFile4=event.getMedia().getName();
		if(attId.equals("5"))
			attFile5=event.getMedia().getName();
			*/
		//Messagebox.show(event.getMedia().getName());
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
	
	
	private void prepareQuotationMail(int quotationid)
	{
		 try 
		 {			 						 
			 	//String host = "smtp.gmail.com";			    			    			  
				String[] to =null;
				String[] cc ={null};
				String[] bcc =null;
				//Properties properties = new Properties();
				//properties.load(new FileInputStream(path+ "mail.properties"));				
				String toMail="hinawi@eim.ae,sales@hinawi.ae"; //"chadi.rahme@teltacworldwide.com";
				String toMail1="eng.chadi@gmail.com"; //"chadi.rahme@teltacworldwide.com";
				ArrayList fileArray = new ArrayList();
				to= toMail.split(",");	
				
				//InternetAddress[] internetAddress = new InternetAddress[0];
				//List result = new LinkedList();
				//result.add(new InternetAddress(toMail));
				
				InternetAddress[] toAdd= Mailer.parse(toMail);//(InternetAddress[]) result.toArray(internetAddress);
				
				MailClient mc = new MailClient();
				//String subject="A new feedback inquiry has been received at hinawi.com";
				String subject="A new Quotation/Contact request has been received at hinawi2.dyndns.org";
				
				
				String messageBody=data.getMailBody(quotationid);
				
				fileArray=data.getMailAttachment(quotationid);
				//Mailer.sendMail("exchangeukedge.teltacworldwide.co", "reports@teltacworldwide.com", toAdd, 
				//		null, null, subject, messageBody, true, fileArray, true);
				
				 //mc.sendMail("exchangeukedge.teltacworldwide.co", "reports@teltacworldwide.com", to, subject, messageBody, null);
				
				 //Stop at hinawi2.dynds 
				 mc.sendMochaMail(to,cc,bcc,subject, messageBody,true,fileArray,false,"quotation","");
				 //mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, fileArray);
				//mc.sendMail(host,"eng.chadi@gmail.com", to,"subject","result", null);
			} 
			  catch (Exception ex) {
					StringWriter sw = new StringWriter();
					  ex.printStackTrace(new PrintWriter(sw)); 
					  Messagebox.show(sw.toString());
					  //logger.logErrorMsg(sw.toString(),"VendorIncreases-->SendEmail ");
			  }	
		 //Messagebox.show("Thank you.Your request is being sent.");
	}

	private void sendClientEmail(String email)
	{
		try
		{
			String[] to =null;
			String[] cc ={null};
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
			String subject="Thanks you for your inquiry";
			StringBuffer result=null;
			result=new StringBuffer();
			result.append("<table border='0'>");
		  	 result.append("<tr>");
			// result.append("<td>"+ "Thank you for updating data and interested in our products and services. Please follow below for free software trial.<br/><br/>" + "</td>");
		  	 result.append("<td>"+ "Thank you for updating data and showing interest in our products and services.<br/><br/>" +
		  	 		"If you requested Demo,  kindly make sure to download Teamviewer 10 as attached or " +
		  	 		" visit this address http://download.teamviewer.com/download/TeamViewer_Setup.ex ." + "</td>");
		  	 result.append("</tr>");
			 
		  	// result.append("<tr><td>Please find the attached document for how to access the server.</td></tr>");
			// result.append("<tr><td>From start menu please go to Remote Desktop Connection then fill :<br/><br/></td></tr>");
		//	 result.append("<tr><td>hinawi2.dyndns.org:3340</td></tr>");
		//	 result.append("<tr><td>Then use any users name from hinawiuser01 To hinawiuser12</td></tr>");
		//	 result.append("<tr><td>Password: 321 <br/><br/></td></tr>");
			 result.append("<tr><td>Kindly contact us if you need any assistance at <a href='mailto:hinawi@eim.ae'>hinawi@eim.ae</a></td></tr>");
			 
			String messageBody=result.toString();			
			
			fileArray=data.getInstructionsAttachment();
			mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,fileArray,true,"quotation","");
			// mc.sendGmailMail("", "eng.chadi@gmail.com", to, subject, messageBody, fileArray);
		}
		catch (Exception ex) {
			StringWriter sw = new StringWriter();
			  ex.printStackTrace(new PrintWriter(sw)); 
			  Messagebox.show(sw.toString());
			  //logger.logErrorMsg(sw.toString(),"VendorIncreases-->SendEmail ");
	  }	
	}
	public List<CountryModel> getLstCountry() {
		return lstCountry;
	}

	public void setLstCountry(List<CountryModel> lstCountry) {
		this.lstCountry = lstCountry;
	}

	public CountryModel getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(CountryModel selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public List<PositionModel> getLstPositions() {
		return lstPositions;
	}

	public void setLstPositions(List<PositionModel> lstPositions) {
		this.lstPositions = lstPositions;
	}

	public PositionModel getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(PositionModel selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public String getAttFile1() {
		return attFile1;
	}

	public void setAttFile1(String attFile1) {
		this.attFile1 = attFile1;
	}

	public String getAttFile2() {
		return attFile2;
	}

	public void setAttFile2(String attFile2) {
		this.attFile2 = attFile2;
	}

	public String getAttFile3() {
		return attFile3;
	}

	public void setAttFile3(String attFile3) {
		this.attFile3 = attFile3;
	}

	public String getAttFile4() {
		return attFile4;
	}

	public void setAttFile4(String attFile4) {
		this.attFile4 = attFile4;
	}

	public String getAttFile5() {
		return attFile5;
	}

	public void setAttFile5(String attFile5) {
		this.attFile5 = attFile5;
	}

	public String getSelectedFromTime() {
		return selectedFromTime;
	}

	public void setSelectedFromTime(String selectedFromTime) {
		this.selectedFromTime = selectedFromTime;
	}

	public String getSelectedToTime() {
		return selectedToTime;
	}

	public void setSelectedToTime(String selectedToTime) {
		this.selectedToTime = selectedToTime;
	}

	public String getSelectedFromDay() {
		return selectedFromDay;
	}

	public void setSelectedFromDay(String selectedFromDay) {
		this.selectedFromDay = selectedFromDay;
	}

	public String getSelectedToDay() {
		return selectedToDay;
	}

	public void setSelectedToDay(String selectedToDay) {
		this.selectedToDay = selectedToDay;
	}


	public List<String> getLstFromTime() {
		return lstFromTime;
	}


	public void setLstFromTime(List<String> lstFromTime) {
		this.lstFromTime = lstFromTime;
	}


	public List<String> getLstToTime() {
		return lstToTime;
	}


	public void setLstToTime(List<String> lstToTime) {
		this.lstToTime = lstToTime;
	}


	public List<String> getLstFromDay() {
		return lstFromDay;
	}


	public void setLstFromDay(List<String> lstFromDay) {
		this.lstFromDay = lstFromDay;
	}


	public List<String> getLstToDay() {
		return lstToDay;
	}


	public void setLstToDay(List<String> lstToDay) {
		this.lstToDay = lstToDay;
	}


	public QuotationModel getQuotModel() {
		return quotModel;
	}


	public void setQuotModel(QuotationModel quotModel) {
		this.quotModel = quotModel;
	}


	public List<String> getLstHowyouknow() {
		return lstHowyouknow;
	}


	public void setLstHowyouknow(List<String> lstHowyouknow) {
		this.lstHowyouknow = lstHowyouknow;
	}


	public Set<String> getSelectedHowyouknow() {
		return selectedHowyouknow;
	}


	public void setSelectedHowyouknow(Set<String> selectedHowyouknow) {
		this.selectedHowyouknow = selectedHowyouknow;
	}


	public boolean isProduct1() {
		return product1;
	}

	@NotifyChange({"product2","product3","product4","product11"})
	public void setProduct1(boolean product1) {
		this.product1 = product1;
		/*if(product1)
		{
			product2=true;
			product3=true;
			product4=true;
			product11=true;
		}
		else
		{
			product2=false;
			product3=false;
			product4=false;
			product11=false;
		}*/
	}


	public String getNol1() {
		return nol1;
	}


	public void setNol1(String nol1) {
		this.nol1 = nol1;
	}


	public String getPmemo1() {
		return pmemo1;
	}


	public void setPmemo1(String pmemo1) {
		this.pmemo1 = pmemo1;
	}


	public boolean isProduct2() {
		return product2;
	}


	@NotifyChange("product1")
	public void setProduct2(boolean product2) {
		this.product2 = product2;
		/*if(product2==true && product3==true && product4==true && product11==true)
		{
			product1=true;
		}
		else
		{
			product1=false;
		}*/
	}


	public String getNol2() {
		return nol2;
	}


	public void setNol2(String nol2) {
		this.nol2 = nol2;
	}


	public String getPmemo2() {
		return pmemo2;
	}


	public void setPmemo2(String pmemo2) {
		this.pmemo2 = pmemo2;
	}


	public boolean isProduct3() {
		return product3;
	}


	@NotifyChange("product1")
	public void setProduct3(boolean product3) {
		this.product3 = product3;
	/*	if(product2==true && product3==true && product4==true && product11==true)
		{
			product1=true;
		}
		else
		{
			product1=false;
		}*/
	}


	public String getNol3() {
		return nol3;
	}


	public void setNol3(String nol3) {
		this.nol3 = nol3;
	}


	public String getPmemo3() {
		return pmemo3;
	}


	public void setPmemo3(String pmemo3) {
		this.pmemo3 = pmemo3;
	}


	public boolean isProduct4() {
		return product4;
	}

	@NotifyChange("product1")
	public void setProduct4(boolean product4) {
		this.product4 = product4;
		/*if(product2==true && product3==true && product4==true && product11==true)
		{
			product1=true;
		}
		else
		{
			product1=false;
		}*/
	}


	public String getNol4() {
		return nol4;
	}


	public void setNol4(String nol4) {
		this.nol4 = nol4;
	}


	public String getPmemo4() {
		return pmemo4;
	}


	public void setPmemo4(String pmemo4) {
		this.pmemo4 = pmemo4;
	}


	public boolean isProduct5() {
		return product5;
	}


	public void setProduct5(boolean product5) {
		this.product5 = product5;
	}


	public String getNol5() {
		return nol5;
	}


	public void setNol5(String nol5) {
		this.nol5 = nol5;
	}


	public String getPmemo5() {
		return pmemo5;
	}


	public void setPmemo5(String pmemo5) {
		this.pmemo5 = pmemo5;
	}


	public boolean isProduct6() {
		return product6;
	}


	public void setProduct6(boolean product6) {
		this.product6 = product6;
	}


	public String getNol6() {
		return nol6;
	}


	public void setNol6(String nol6) {
		this.nol6 = nol6;
	}


	public String getPmemo6() {
		return pmemo6;
	}


	public void setPmemo6(String pmemo6) {
		this.pmemo6 = pmemo6;
	}


	public boolean isProduct7() {
		return product7;
	}


	public void setProduct7(boolean product7) {
		this.product7 = product7;
	}


	public String getNol7() {
		return nol7;
	}


	public void setNol7(String nol7) {
		this.nol7 = nol7;
	}


	public String getPmemo7() {
		return pmemo7;
	}


	public void setPmemo7(String pmemo7) {
		this.pmemo7 = pmemo7;
	}


	public boolean isService1() {
		return service1;
	}


	public void setService1(boolean service1) {
		this.service1 = service1;
	}


	public String getSmemo1() {
		return smemo1;
	}


	public void setSmemo1(String smemo1) {
		this.smemo1 = smemo1;
	}


	public boolean isService2() {
		return service2;
	}


	public void setService2(boolean service2) {
		this.service2 = service2;
	}


	public String getSmemo2() {
		return smemo2;
	}


	public void setSmemo2(String smemo2) {
		this.smemo2 = smemo2;
	}


	public boolean isService3() {
		return service3;
	}


	public void setService3(boolean service3) {
		this.service3 = service3;
	}


	public String getSmemo3() {
		return smemo3;
	}


	public void setSmemo3(String smemo3) {
		this.smemo3 = smemo3;
	}


	public boolean isService4() {
		return service4;
	}


	public void setService4(boolean service4) {
		this.service4 = service4;
	}


	public String getSmemo4() {
		return smemo4;
	}


	public void setSmemo4(String smemo4) {
		this.smemo4 = smemo4;
	}


	public boolean isService5() {
		return service5;
	}


	public void setService5(boolean service5) {
		this.service5 = service5;
	}


	public String getSmemo5() {
		return smemo5;
	}


	public void setSmemo5(String smemo5) {
		this.smemo5 = smemo5;
	}

	public boolean isCusttype1() {
		return custtype1;
	}

	public void setCusttype1(boolean custtype1) {
		this.custtype1 = custtype1;
	}

	public boolean isCusttype2() {
		return custtype2;
	}

	public void setCusttype2(boolean custtype2) {
		this.custtype2 = custtype2;
	}

	public boolean isCusttype3() {
		return custtype3;
	}

	public void setCusttype3(boolean custtype3) {
		this.custtype3 = custtype3;
	}

	public boolean isCusttype4() {
		return custtype4;
	}

	public void setCusttype4(boolean custtype4) {
		this.custtype4 = custtype4;
	}

	public List<String> getLstBusinessType() {
		return lstBusinessType;
	}

	public void setLstBusinessType(List<String> lstBusinessType) {
		this.lstBusinessType = lstBusinessType;
	}

	public String getSelectedBusinessType() {
		return selectedBusinessType;
	}

	public void setSelectedBusinessType(String selectedBusinessType) {
		this.selectedBusinessType = selectedBusinessType;
	}

	public List<String> getLstCompanySize() {
		return lstCompanySize;
	}

	public void setLstCompanySize(List<String> lstCompanySize) {
		this.lstCompanySize = lstCompanySize;
	}

	public String getSelectedCompanySize() {
		return selectedCompanySize;
	}

	public void setSelectedCompanySize(String selectedCompanySize) {
		this.selectedCompanySize = selectedCompanySize;
	}

	public List<String> getLstSoftwareLanguage() {
		return lstSoftwareLanguage;
	}

	public void setLstSoftwareLanguage(List<String> lstSoftwareLanguage) {
		this.lstSoftwareLanguage = lstSoftwareLanguage;
	}

	public String getSelectedSoftwareLanguage() {
		return selectedSoftwareLanguage;
	}

	public void setSelectedSoftwareLanguage(String selectedSoftwareLanguage) {
		this.selectedSoftwareLanguage = selectedSoftwareLanguage;
	}

	public List<String> getLstCompanyType() {
		return lstCompanyType;
	}

	public void setLstCompanyType(List<String> lstCompanyType) {
		this.lstCompanyType = lstCompanyType;
	}

	public String getSelectedCompanyType() {
		return selectedCompanyType;
	}

	public void setSelectedCompanyType(String selectedCompanyType) {
		this.selectedCompanyType = selectedCompanyType;
	}

	public String getMemo1() {
		return memo1;
	}

	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}

	public String getMemo2() {
		return memo2;
	}

	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}

	public String getMemo3() {
		return memo3;
	}

	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}

	public String getMemo4() {
		return memo4;
	}

	public void setMemo4(String memo4) {
		this.memo4 = memo4;
	}

	public String getMemo5() {
		return memo5;
	}

	public void setMemo5(String memo5) {
		this.memo5 = memo5;
	}

	public String getMemo6() {
		return memo6;
	}

	public void setMemo6(String memo6) {
		this.memo6 = memo6;
	}

	public String getMemo7() {
		return memo7;
	}

	public void setMemo7(String memo7) {
		this.memo7 = memo7;
	}

	public String getOtherPosition() {
		return otherPosition;
	}

	public void setOtherPosition(String otherPosition) {
		this.otherPosition = otherPosition;
	}

	public String getOtherBusinessType() {
		return otherBusinessType;
	}

	public void setOtherBusinessType(String otherBusinessType) {
		this.otherBusinessType = otherBusinessType;
	}

	public List<String> getLstModules() {
		return lstModules;
	}

	public void setLstModules(List<String> lstModules) {
		this.lstModules = lstModules;
	}

	public Set<String> getSelectedModules() {
		return selectedModules;
	}

	public void setSelectedModules(Set<String> selectedModules) {
		this.selectedModules = selectedModules;
	}

	public boolean isProduct8() {
		return product8;
	}
	@NotifyChange({"product9","product10","product12","product13"})
	public void setProduct8(boolean product8) {
		this.product8 = product8;
		/*if(product8)
		{
			product9=true;
			product10=true;
			product12=true;
			product13=true;
		}
		else
		{
			product9=false;
			product10=false;
			product12=false;
			product13=false;
		}*/
		
	}

	public String getNol8() {
		return nol8;
	}

	public void setNol8(String nol8) {
		this.nol8 = nol8;
	}

	public String getPmemo8() {
		return pmemo8;
	}

	public void setPmemo8(String pmemo8) {
		this.pmemo8 = pmemo8;
	}

	public boolean isProduct9() {
		return product9;
	}

	@NotifyChange("product8")
	public void setProduct9(boolean product9) {
		this.product9 = product9;
		/*if(product9==true && product10==true && product12==true && product13==true)
		{
			product8=true;
		}
		else
		{
			product8=false;
		}*/
		
	}

	public String getNol9() {
		return nol9;
	}

	public void setNol9(String nol9) {
		this.nol9 = nol9;
	}

	public String getPmemo9() {
		return pmemo9;
	}

	public void setPmemo9(String pmemo9) {
		this.pmemo9 = pmemo9;
	}

	public boolean isProduct10() {
		return product10;
	}

	@NotifyChange("product8")
	public void setProduct10(boolean product10) {
		this.product10 = product10;
		/*if(product9==true && product10==true && product12==true && product13==true)
		{
			product8=true;
		}
		else
		{
			product8=false;
		}*/
	}

	public String getNol10() {
		return nol10;
	}

	public void setNol10(String nol10) {
		this.nol10 = nol10;
	}

	public String getPmemo10() {
		return pmemo10;
	}

	public void setPmemo10(String pmemo10) {
		this.pmemo10 = pmemo10;
	}

	public String getMemo8() {
		return memo8;
	}

	public void setMemo8(String memo8) {
		this.memo8 = memo8;
	}

	public String getMemo9() {
		return memo9;
	}

	public void setMemo9(String memo9) {
		this.memo9 = memo9;
	}

	public String getMemo10() {
		return memo10;
	}

	public void setMemo10(String memo10) {
		this.memo10 = memo10;
	}

	public boolean isProduct11() {
		return product11;
	}

	@NotifyChange("product1")
	public void setProduct11(boolean product11) {
		this.product11 = product11;
		/*if(product2==true && product3==true && product4==true && product11==true)
		{
			product1=true;
		}
		else
		{
			product1=false;
		}*/
	}

	public String getNol11() {
		return nol11;
	}

	public void setNol11(String nol11) {
		this.nol11 = nol11;
	}

	public String getPmemo11() {
		return pmemo11;
	}

	public void setPmemo11(String pmemo11) {
		this.pmemo11 = pmemo11;
	}

	public boolean isProduct12() {
		return product12;
	}

	@NotifyChange("product8")
	public void setProduct12(boolean product12) {
		this.product12 = product12;
		/*if(product9==true && product10==true && product12==true && product13==true)
		{
			product8=true;
		}
		else
		{
			product8=false;
		}*/
	}

	public String getNol12() {
		return nol12;
	}

	public void setNol12(String nol12) {
		this.nol12 = nol12;
	}

	public String getPmemo12() {
		return pmemo12;
	}

	public void setPmemo12(String pmemo12) {
		this.pmemo12 = pmemo12;
	}

	public boolean isProduct13() {
		return product13;
	}

	@NotifyChange("product8")
	public void setProduct13(boolean product13) {
		this.product13 = product13;
		/*if(product9==true && product10==true && product12==true && product13==true)
		{
			product8=true;
		}
		else
		{
			product8=false;
		}*/
	}

	public String getNol13() {
		return nol13;
	}

	public void setNol13(String nol13) {
		this.nol13 = nol13;
	}

	public String getPmemo13() {
		return pmemo13;
	}

	public void setPmemo13(String pmemo13) {
		this.pmemo13 = pmemo13;
	}

	public boolean isProduct14() {
		return product14;
	}

	public void setProduct14(boolean product14) {
		this.product14 = product14;
	}

	public String getNol14() {
		return nol14;
	}

	public void setNol14(String nol14) {
		this.nol14 = nol14;
	}

	public String getPmemo14() {
		return pmemo14;
	}

	public void setPmemo14(String pmemo14) {
		this.pmemo14 = pmemo14;
	}

	public String getMemo11() {
		return memo11;
	}

	public void setMemo11(String memo11) {
		this.memo11 = memo11;
	}

	public String getMemo12() {
		return memo12;
	}

	public void setMemo12(String memo12) {
		this.memo12 = memo12;
	}

	public String getMemo13() {
		return memo13;
	}

	public void setMemo13(String memo13) {
		this.memo13 = memo13;
	}

	public String getMemo14() {
		return memo14;
	}

	public void setMemo14(String memo14) {
		this.memo14 = memo14;
	}


	

}
