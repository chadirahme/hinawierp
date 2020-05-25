package company;

import hba.HBAData;
import hba.HBAQueries;
import hba.ProspectiveData;
import home.CustomerFeedBackData;
import home.CustomerFeedBackQuerries;
import home.MailClient;
import home.QuotationAttachmentModel;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.CompanyDBModel;
import model.CustomerContact;
import model.CustomerFeedbackModel;
import model.CustomerModel;
import model.EmailSelectionModel;
import model.EmailSignatureModel;
import model.HRListValuesModel;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;
import model.ReminderSettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import common.EncryptEmail;
import db.DBHandler;
import setup.users.WebusersModel;

public class ReminderViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	WebusersModel dbUser=null;
	ReminderData data=new ReminderData();
	HBAData HBadata=new HBAData();
	CustomerFeedBackData feedBackData=null;

	ProspectiveData prospectivedata = new ProspectiveData();

	//Reminder
	private ListModelList<String> lstQuotationMonths;
	private Set<String> selectedQuotationMonths;
	private ListModelList<String> lstQuotationDays;
	private Set<String> selectedQuotationDays;	
	private ListModelList<String> lstQuotationWeekDays;
	private Set<String> selectedQuotationWeekDays;


	private List<HRListValuesModel> lstTemplates;
	private HRListValuesModel selectedTemplate;


	private List<ReminderSettingsModel> listReminderTypes=new ArrayList<ReminderSettingsModel>();
	private ReminderSettingsModel selectedReminderTypes;


	//list of Customers
	List<CustomerModel> customerList=new ArrayList<CustomerModel>();

	List<CustomerModel> tempCustomerList=new ArrayList<CustomerModel>();

	List<ProspectiveModel> prospectiveList=new ArrayList<ProspectiveModel>();

	List<ProspectiveModel> tempProspectiveList=new ArrayList<ProspectiveModel>();

	EmailSignatureModel selectedemailSignature= new EmailSignatureModel();


	Set<String> selectedTo=new HashSet<String>();

	Set<String> selectedCc=new HashSet<String>();

	Set<String> selectedBcc=new HashSet<String>();

	EmailSelectionModel temEmailpopUpModel;

	private List<EmailSelectionModel> tempEmailSelctionPopUpList=new ArrayList<EmailSelectionModel>();
	
	List<EmailSelectionModel> tempemailSelectionPopUpList = new ArrayList<EmailSelectionModel>();
	private String selectedContractCount;
	private boolean allcustomers;
	
	public ReminderViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser==null)
			{
				Executions.sendRedirect("/login.zul");
			}

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

			fillMonthList();

			selectedQuotationMonths=new HashSet<String>();
			selectedQuotationDays=new HashSet<String>();
			selectedQuotationWeekDays=new HashSet<String>();

			lstTemplates=data.getHRListValuesForTemplates(147, "Select");//get templates id=147 always constant 
			if(lstTemplates!=null && lstTemplates.size()>0)
				selectedTemplate=lstTemplates.get(0);

			listReminderTypes=data.getAllCompanyReminder(dbUser.getCompanyid());

			if(listReminderTypes.size()>0)
				selectedReminderTypes=listReminderTypes.get(0);//deafult setting 

			if(!selectedReminderTypes.getWeekly().equals(""))
			{
				List<String> weeklyList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getWeekly().split(",")));
				for (String item : weeklyList)
				{
					selectedQuotationWeekDays.add(item);	
				}							
			}	

			if(!selectedReminderTypes.getMonthly().equals(""))
			{
				List<String> monthList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getMonthly().split(",")));
				for (String item : monthList)
				{
					selectedQuotationMonths.add(item);	
				}							
			}	
			if(!selectedReminderTypes.getMonthlydays().equals(""))
			{
				List<String> monthDaysList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getMonthlydays().split(",")));
				for (String item : monthDaysList)
				{
					selectedQuotationDays.add(item);	
				}							
			}
			customerList = feedBackData.getCustomerList("");
			prospectiveList=prospectivedata.getProspectiveSearchRes("", 0);

		}
		catch (Exception ex)
		{
			logger.error("error at ReminderViewModel>>Init>> ",ex);
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

	@Command
	public void saveQuotationCommand()
	{
		try
		{	
			if(selectedQuotationWeekDays!=null && selectedReminderTypes.getReminderid()==0)
			{
				Clients.showNotification("Please select the reminder type.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}

			int result=0;
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

			if(temEmailpopUpModel!=null)
			{
				selectedReminderTypes.setSelectedCustomerEmails(tempemailSelectionPopUpList);
				logger.info("tempemailSelectionPopUpList>> " + tempemailSelectionPopUpList.size());				
			}

			if(selectedTemplate!=null && selectedTemplate.getListId()>0)
				selectedReminderTypes.setServiceListRefKey(selectedTemplate.getListId());

			selectedReminderTypes.setAllcustomers(allcustomers);
			
			result=data.saveReminderSettings(selectedReminderTypes,tempCustomerList,tempProspectiveList);
			if(result>0)
				Messagebox.show("Reminder settings is saved successfully.","Reminder Settings", Messagebox.OK , Messagebox.INFORMATION);
			else
				Messagebox.show("Error at save reminder settings !! ","Reminder Settings", Messagebox.OK , Messagebox.ERROR);	
		}
		catch (Exception ex)
		{
			logger.error("error at ReminderViewModel>>saveCommand>> ",ex);
		}
	}




	public Set<String> getSelectedQuotationMonths() {
		if(selectedQuotationMonths==null)
			selectedQuotationMonths=new HashSet<String>();
		return selectedQuotationMonths;
	}

	public void setSelectedQuotationMonths(Set<String> selectedQuotationMonths) {
		this.selectedQuotationMonths = selectedQuotationMonths;
	}

	public Set<String> getSelectedQuotationDays() {
		if(selectedQuotationDays==null)
			selectedQuotationDays=new HashSet<String>();
		return selectedQuotationDays;
	}

	public void setSelectedQuotationDays(Set<String> selectedQuotationDays) {
		this.selectedQuotationDays = selectedQuotationDays;
	}

	public Set<String> getSelectedQuotationWeekDays() {
		if(selectedQuotationWeekDays==null)
			selectedQuotationWeekDays=new HashSet<String>();
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

	public ListModelList<String> getLstQuotationDays() {
		return lstQuotationDays;
	}

	public void setLstQuotationDays(ListModelList<String> lstQuotationDays) {
		this.lstQuotationDays = lstQuotationDays;
	}

	public ListModelList<String> getLstQuotationWeekDays() {
		return lstQuotationWeekDays;
	}

	public void setLstQuotationWeekDays(ListModelList<String> lstQuotationWeekDays) {
		this.lstQuotationWeekDays = lstQuotationWeekDays;
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

	public void setSelectedTemplate(HRListValuesModel selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}


	@Command
	public void selectCustomerPopUp()
	{
		try
		{

			if(selectedQuotationWeekDays!=null && selectedReminderTypes.getReminderid()==0)
			{
				Clients.showNotification("Please select the reminder type.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("custKey", 0);
			arg.put("compKey",0);
			arg.put("type","fromOtherForms");
			arg.put("allowSelect","true");
			arg.put("selectedReminderTypes",selectedReminderTypes.getRemindername());
			//get saved Customers for reminders 
			String custKeys="";
			tempCustomerList=new ArrayList<CustomerModel>();
			custKeys=data.getSavedCustomerIds(selectedReminderTypes.getReminderid());
			if(custKeys!=null && !custKeys.equalsIgnoreCase(""))
			{
				getCustomerIdsForReminders(custKeys,0);
			}
			arg.put("selectedCustomers",tempCustomerList);
			
			//Executions.createComponents("/hba/list/listOfCustomersforTask.zul", null,arg);
			if(selectedReminderTypes.getRemindername().toLowerCase().contains("contract") || selectedReminderTypes.getRemindername().toLowerCase().contains("balance") || selectedReminderTypes.getRemindername().toLowerCase().contains("quotations"))
			Executions.createComponents("listCustomerContract.zul", null,arg);	
			else
			Executions.createComponents("/hba/list/listOfCustomersforTask.zul", null,arg);	

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> selectCustomerPopUp", ex);			
		}
	}

	@Command
	public void selectProspectivePopUp()
	{
		try
		{
			if(selectedQuotationWeekDays!=null && selectedReminderTypes.getReminderid()==0)
			{
				Clients.showNotification("Please select the reminder type.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("custKey", 0);
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			String prospKeys="";
			tempProspectiveList=new ArrayList<ProspectiveModel>();
			prospKeys=data.getSavedProspectiveIds(selectedReminderTypes.getReminderid());
			if(prospKeys!=null && !prospKeys.equalsIgnoreCase(""))
			{
				getProspectiveIdsForReminder(prospKeys);
			}
			arg.put("selectedProspective",tempProspectiveList);
			Executions.createComponents("/crm/listProspectiveForSendEmail.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> selectProspectivePopUp", ex);			
		}
	}


	@GlobalCommand 
	@NotifyChange({"selectedContractCount"})
	public void getCustomerIdsForReminders(@BindingParam("myData")String custKeys,@BindingParam("count")int count)
	{		
		try
		{
			int i=0;
			tempCustomerList=new ArrayList<CustomerModel>();
			String reg=",";
			String[] tokens = custKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(CustomerModel model:customerList)
				{
					if(model.getCustkey()==Integer.parseInt(tokens[i]))
					{
						tempCustomerList.add(model);
					}
				}

			}
			
			//use this to not use temp email window
		    if(selectedReminderTypes.getRemindername().contains("Contract") || selectedReminderTypes.getRemindername().contains("Balance") || selectedReminderTypes.getRemindername().contains("Quotations"))
			{
		    	tempemailSelectionPopUpList=new ArrayList<EmailSelectionModel>();
		    	for (CustomerModel item : tempCustomerList) 
		    	{
		    		temEmailpopUpModel=new EmailSelectionModel();
		    		temEmailpopUpModel.setEmail(item.getEmail());
		    		temEmailpopUpModel.setTo(true);
		    		temEmailpopUpModel.setCustOrProspKey(item.getCustkey());
					temEmailpopUpModel.setSourceType("C");
					temEmailpopUpModel.setCustomerName(item.getFullName());
					temEmailpopUpModel.setLocalBalance(item.getLocalBalance());
					temEmailpopUpModel.setCusContractExpiry(item.getCustomerContactExpiryDateStr());
					tempemailSelectionPopUpList.add(temEmailpopUpModel);		    		
				}				
			}
		    
		    selectedContractCount="Selected Customers = " + tokens.length;
			//List<CustomerModel> lstAllExpiryCustomersContract = HBadata.getCustomersContractExpiryList(selectedReminderTypes.getRemindername());
			selectedContractCount+=" of total " +count;// lstAllExpiryCustomersContract.size();	
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> getCutomerIDsForFeedbackSendTocustomer", ex);			
		}
	}

	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}


	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc"})
	public void getCutomerIDsForCustomerReminderEmailSelection(@BindingParam("myData")String custKeys)
	{		
		try
		{
			tempEmailSelctionPopUpList.clear();
			boolean tempMessage=false; 
			int i=0;
			String reg=",";
			String[] tokens = custKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(CustomerModel model:customerList)
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
									temEmailpopUpModel.setCustOrProspKey(model.getCustkey());
									temEmailpopUpModel.setSourceType("C");
									temEmailpopUpModel.setCustomerName(model.getFullName());
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
											temEmailpopUpModel.setCustOrProspKey(model.getCustkey());
											temEmailpopUpModel.setSourceType("C");
											temEmailpopUpModel.setCustomerName(model.getFullName());
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
			arg.put("reminderId", selectedReminderTypes.getReminderid());
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/company/reminderEmailSelection.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> getCutomerIDsForFeedbackSendTocustomer", ex);			
		}
	}


	
	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc"})
	public void getProspectiveIDsForRemindersEmailSelection(@BindingParam("myData")String prospKeys)
	{		
		try
		{
			tempEmailSelctionPopUpList.clear();
			boolean tempMessage=false; 
			int i=0;
			String reg=",";
			String[] tokens = prospKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(ProspectiveModel model:prospectiveList)
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
								temEmailpopUpModel.setCustOrProspKey(model.getRecNo());
								temEmailpopUpModel.setSourceType("P");
								temEmailpopUpModel.setCustomerName(model.getName());
								tempEmailSelctionPopUpList.add(temEmailpopUpModel);
							}
							if(model.getcC()!=null && !model.getcC().equalsIgnoreCase(""))
								if(isValidEmailAddress(model.getcC()))
								{
									//selectedBcc.add(model.getcC());
									temEmailpopUpModel=new EmailSelectionModel();
									temEmailpopUpModel.setEmail(model.getcC());
									temEmailpopUpModel.setCc(true);
									temEmailpopUpModel.setCustOrProspKey(model.getRecNo());
									temEmailpopUpModel.setSourceType("P");
									temEmailpopUpModel.setCustomerName(model.getName());
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
												temEmailpopUpModel.setCustOrProspKey(model.getRecNo());
												temEmailpopUpModel.setSourceType("P");
												temEmailpopUpModel.setCustomerName(model.getName());
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
			arg.put("reminderId", selectedReminderTypes.getReminderid());
			arg.put("compKey",0);
			arg.put("type","add");
			arg.put("allowSelect","true");
			Executions.createComponents("/company/reminderEmailSelection.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> getProspectiveIDsForSendEmail", ex);			
		}
	}

	
	

	@SuppressWarnings("unchecked")
	@GlobalCommand 
	@NotifyChange({"selectedBcc","showBcc","selectedCc","showCc","selectedTo"})
	public void getReminderPopUpEmailList(@SuppressWarnings("rawtypes") @BindingParam("selectedEmailSelectionPopUp")List selectedEmailSelectionPopUp)
	{		
		try
		{
			tempemailSelectionPopUpList=selectedEmailSelectionPopUp;

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> getPopUpEmailList", ex);			
		}
	}

	@GlobalCommand 
	public void getProspectiveIdsForReminder(@BindingParam("myData")String prospKeys)
	{		
		try
		{
			int i=0;
			tempProspectiveList=new ArrayList<ProspectiveModel>();
			String reg=",";
			String[] tokens = prospKeys.split(reg);
			for(i=0;i<tokens.length;i++)
			{
				for(ProspectiveModel model:prospectiveList)
				{
					if(model.getRecNo()==Integer.parseInt(tokens[i]))
					{
						tempProspectiveList.add(model);
					}
				}

			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> getProspectiveIdsForReminder", ex);			
		}
	}
	
	@Command
	public void SendTestEmailCommand()
	{
		try
		{					 
			String emailTo=selectedReminderTypes.getCcemail();
			if(emailTo.isEmpty())
			{
				Messagebox.show("Enter CC Email !!!");
				return;
			}
			
			CustomerFeedbackModel objTemplate=feedBackData.getNotesForModules(selectedTemplate.getListId());
			String emailSignature=feedBackData.getReminderSignatureByCompany(selectedReminderTypes.getReminderid());
			
			MailClient mc = new MailClient();
			//mc.sendTestEmail();
			if(!emailTo.equals("eng.chadi@gmail.com"))
				emailTo+=",eng.chadi@gmail.com";
			
			String tomail=emailTo;//"eng.chadi@gmail.com";
			String[] to=tomail.split(",");
			//mc.sendTestEmail();
			//mc.sendGmailMail("", "eng.chadi@gmail.com",to, "subject", "messageBody", null);
			
			String body=objTemplate.getMemoEn();	
			body=body.replace("{CustomerName}", "Hatem Hinawi");
			body=body.replace("{CusContractExpiry}", "31-12-2015");
			body=body.replace("{LocalBalace}", "10,000");
			if(!emailSignature.isEmpty())
			{
				body+="<br/><br/>";
				body+=emailSignature;
			}
						
			List<QuotationAttachmentModel> lstAtt=new ArrayList<QuotationAttachmentModel>();
			lstAtt=feedBackData.getNotesForModulesAttchamnet(objTemplate.getNoteID());
			ArrayList fileArray = new ArrayList();
			for(QuotationAttachmentModel attPath:lstAtt)
			{
				File dir = new File(attPath.getFilepath());
				if(dir.exists())
					fileArray.add(attPath.getFilepath());
			}
			String subject="Reminder Contract Expiry";
			if(selectedReminderTypes.getRemindername().contains("Contract"))
			{
				subject="Reminder Contract Expiry";
			}			
			else if(selectedReminderTypes.getRemindername().contains("Balance"))
			{
				subject="Customer Balance";
			}
			else if(selectedReminderTypes.getRemindername().contains("Quotations"))
			{
				subject="Follow up Quotation / Agreement";
			}
			
			mc.sendMochaMail(to, null, null, subject, body, true, fileArray, true, "", null);	
			Messagebox.show("Test Email is Send..");
		}
	
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> SendTestEmailCommand", ex);			
		}
	}
	
	@Command
	public void sendNowCommand()
	{
		try
		{	
			saveQuotationCommand();
			if(selectedReminderTypes.getReminderid()==0)
			{
				Clients.showNotification("Please select the reminder type.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			
			 String[] command = {"cmd.exe", "/C", "Start", "C:\\erpengines\\weberp.bat",selectedReminderTypes.getRemindername()};
			 Process p =  Runtime.getRuntime().exec(command); 
			  			
			// Process p =  Runtime.getRuntime().exec("C:\\erpengines\\erp.bat");
			 Messagebox.show("Customer Email Reminder is Send it..");
			 
		}
	
		catch (Exception ex)
		{	
			logger.error("ERROR in ContactEmailViewModel ----> sendNowCommand", ex);			
		}
	}
	/**
	 * @return the listReminderTypes
	 */
	public List<ReminderSettingsModel> getListReminderTypes() {
		return listReminderTypes;
	}

	/**
	 * @param listReminderTypes the listReminderTypes to set
	 */
	public void setListReminderTypes(List<ReminderSettingsModel> listReminderTypes) {
		this.listReminderTypes = listReminderTypes;
	}

	/**
	 * @return the selectedReminderTypes
	 */
	public ReminderSettingsModel getSelectedReminderTypes() {
		return selectedReminderTypes;
	}

	/**
	 * @param selectedReminderTypes the selectedReminderTypes to set
	 */
	@NotifyChange({"selectedReminderTypes","selectedTemplate","selectedQuotationMonths","selectedQuotationDays","selectedQuotationWeekDays","selectedContractCount","allcustomers"})
	public void setSelectedReminderTypes(ReminderSettingsModel selectedReminderTypes) {
		this.selectedReminderTypes = selectedReminderTypes;
		selectedContractCount="";
		allcustomers=selectedReminderTypes.isAllcustomers();
		
		if(selectedReminderTypes.getRemindername().equalsIgnoreCase("Tasks"))
		{
			selectedReminderTypes.setHideCustomer(false);
			selectedReminderTypes.setHideProspective(false);
			selectedReminderTypes.setHideOtherfileds(false);
		}
		else if(selectedReminderTypes.getRemindername().equalsIgnoreCase("Customer Balance"))
		{
			selectedReminderTypes.setHideProspective(false);
			selectedReminderTypes.setHideCustomer(true);
			selectedReminderTypes.setHideOtherfileds(true);
		}
		else if(selectedReminderTypes.getRemindername().contains("Contract"))
		{
			selectedReminderTypes.setHideProspective(false);
			selectedReminderTypes.setHideCustomer(true);
			selectedReminderTypes.setHideOtherfileds(true);
		}
		else
		{
			selectedReminderTypes.setHideCustomer(true);
			selectedReminderTypes.setHideProspective(true);
			selectedReminderTypes.setHideOtherfileds(true);
		}

		for(HRListValuesModel model:lstTemplates)
		{
			if(model.getListId()==selectedReminderTypes.getServiceListRefKey())
			{
				selectedTemplate=model;
				break;
			}
		}
		
		selectedQuotationMonths=new HashSet<String>();
		selectedQuotationWeekDays=new HashSet<String>();
		selectedQuotationDays=new HashSet<String>();
		
		if(!selectedReminderTypes.getMonthly().equals(""))
		{
			List<String> monthList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getMonthly().split(",")));
			for (String item : monthList)
			{
				selectedQuotationMonths.add(item);	
			}							
		}
				
		
		if(!selectedReminderTypes.getWeekly().equals(""))
		{
			List<String> weeklyList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getWeekly().split(",")));
			for (String item : weeklyList)
			{
				selectedQuotationWeekDays.add(item);	
			}							
		}	

				
		if(!selectedReminderTypes.getMonthlydays().equals(""))
		{
			List<String> monthDaysList = new ArrayList<String>(Arrays.asList(selectedReminderTypes.getMonthlydays().split(",")));
			for (String item : monthDaysList)
			{
				selectedQuotationDays.add(item);	
			}							
		}
		
		if(selectedReminderTypes.getRemindername().contains("Contract") || selectedReminderTypes.getRemindername().contains("Balance"))
		{
		String custKeys="";
		tempCustomerList=new ArrayList<CustomerModel>();
		custKeys=data.getSavedCustomerIds(selectedReminderTypes.getReminderid());
		if(custKeys.length()>0)
		{
			String[] selectedCustKeys= custKeys.split(",");		
			selectedContractCount="Selected Customers = " + selectedCustKeys.length;
			List<CustomerModel> lstAllExpiryCustomersContract = HBadata.getCustomersContractExpiryList(selectedReminderTypes.getRemindername());
			selectedContractCount+=" of total " + lstAllExpiryCustomersContract.size();				
		}
		else
		{
		selectedContractCount="Selected Customers = 0";
		List<CustomerModel> lstAllExpiryCustomersContract = HBadata.getCustomersContractExpiryList(selectedReminderTypes.getRemindername());
		selectedContractCount+=" of total " + lstAllExpiryCustomersContract.size();	
		}
		
		}
	}


	@Command
	public void setUpEmailSignature()
	{
		try
		{
			if(selectedReminderTypes!=null && selectedReminderTypes.getReminderid()==0)
			{
				Clients.showNotification("Please select the reminder type.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			selectedemailSignature=data.getEmailSignature(selectedReminderTypes.getReminderid());
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("type","add");
			arg.put("selectedReminderType",selectedReminderTypes);
			arg.put("slectedSignature",selectedemailSignature);
			Executions.createComponents("/company/reminderEmailSignature.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReminderViewModel ----> setUpEmailSignature", ex);			
		}
	}

	public String getSelectedContractCount() {
		return selectedContractCount;
	}

	public void setSelectedContractCount(String selectedContractCount) {
		this.selectedContractCount = selectedContractCount;
	}

	public boolean isAllcustomers() {
		return allcustomers;
	}

	@NotifyChange({"selectedReminderTypes","selectedContractCount"})
	public void setAllcustomers(boolean allcustomers)
	{
		this.allcustomers = allcustomers;
		if(allcustomers==true)
		{
			selectedReminderTypes.setHideProspective(false);
			selectedReminderTypes.setHideCustomer(false);
			List<CustomerModel> lstAllExpiryCustomersContract = HBadata.getCustomersContractExpiryList(selectedReminderTypes.getRemindername());
			selectedContractCount="Selected Customers = " + lstAllExpiryCustomersContract.size();
			selectedContractCount+=" of total " + lstAllExpiryCustomersContract.size();	
		}
		else
		{
			selectedReminderTypes.setHideProspective(false);
			selectedReminderTypes.setHideCustomer(true);
			selectedContractCount="";
		}
	}



}
