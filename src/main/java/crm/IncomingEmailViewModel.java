package crm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import model.CustomerModel;
import model.DataFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;


//http://www.vipan.com/htdocs/javamail.html


public class IncomingEmailViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	IncomingEmailData data=new IncomingEmailData();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfRec = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	private List<EmailModel> lstIncomingEmails;
	private List<EmailModel> lstAllIncomingEmails;
	private EmailModel selectedEmail;
	private String viewType;
	private int unreadEmails;
	private int UserId;	
	private WebusersModel userEmail;
	private String host;
	HashMap<String, CustomerModel> hmCustomerEmails = new HashMap<String, CustomerModel>();
	private List<CustomerModel> lstCustomers;
	private CustomerModel selectedCustomer;
	private DataFilter filter = new DataFilter();
	
	@Init	
	public void init(@BindingParam("type") String type)
	{
		viewType=type ==null ? "" : type;
		if(viewType.equals("body"))
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			if(map.get("selectedEmail") !=null)
			{
				selectedEmail= (EmailModel) map.get("selectedEmail");
			}
		}
		else if(viewType.equals("unknownEmail"))
		{
			lstCustomers=data.getCustomersList();
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			if(map.get("selectedEmail") !=null)
			{
				selectedEmail= (EmailModel) map.get("selectedEmail");
				if(selectedEmail.getEmailCustomer().getCustkey()>0)
				{
					for (CustomerModel item : lstCustomers) 
					{
						if(item.getCustkey()==selectedEmail.getEmailCustomer().getCustkey())
						{
							selectedCustomer=item;
						}
					}
				}
				else
				{
					selectedCustomer=lstCustomers.get(0);
				}
			}
		}
		else
		{
			org.zkoss.zk.ui.Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			UserId=dbUser.getUserid();
			userEmail=data.getUserEmail(UserId);
			//host=data.getCompanyHostEmail(dbUser.getCompanyid());
		}
	}
	
	public IncomingEmailViewModel()
	{
		try
		{
			
		}
		catch (Exception ex) 
		{
			logger.error("error in IncomingEmailViewModel---Init-->" , ex);
		}
	}
	
	
	@Command 	
	@NotifyChange({ "lstIncomingEmails","unreadEmails"})
	public void getEmailCommand()
	{
		try	
		{
			if(userEmail.getEmailhost().equals(""))
			{
				Messagebox.show("Email host not set !!","Incoming Emails", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}
			
			if(userEmail.getUseremail().equals(""))
			{
				Messagebox.show("User Email not set !!","Incoming Emails", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}
			
			if(userEmail.getUserEmailPassword().equals(""))
			{
				Messagebox.show("User Email Password not set !!","Incoming Emails", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}
			
		   String emailFrom = null, emailTo = null, emailCc = null, emailSubject = null, emailBody = null;
		   String dateReceived = null ,emailFromAddress =null , sortDateReceived="";
		   Date sentDate = null, receivedDate = null;
		   HashMap fileHash = new HashMap();
		   
		   lstIncomingEmails=new ArrayList<EmailModel>();
		   lstAllIncomingEmails=new ArrayList<EmailModel>();
		   //for testing purpose
		  
       /*	EmailModel obj2=new EmailModel();
       	obj2.setSubject("Subject");
       	obj2.setFromEmail("eng.chadi@gmail.com");
       	obj2.setEmailFromAddress("chadi:eng.chadi@gmail.com");
       	obj2.setDateReceived("2015-03-01");	        		        	
       	obj2.setBody("bodyyyyyyyyyyyyyyyyy");	    
    	obj2.setEmailCustomer(getCustomerFromEmail(obj2.getFromEmail()));	
       	lstIncomingEmails.add(obj2);
       	
       	obj2=new EmailModel();
       	obj2.setSubject("Subject 22222");
       	obj2.setFromEmail("bilal@bhs.ae");
       	obj2.setEmailFromAddress("bilal:bilal@bhs.ae");       	
       	obj2.setDateReceived("2015-03-02");	        		        	
       	obj2.setBody("bodyyyyyyyyyyyyyyyyy 2222222");	   
    	obj2.setEmailCustomer(getCustomerFromEmail(obj2.getFromEmail()));	
       	lstIncomingEmails.add(obj2);
       	
        lstAllIncomingEmails.addAll(lstIncomingEmails);
       	if(true)
       	return;*/
       	
		   
			int i=0;
			Session session = null;
	        Store store = null;
	        Folder folder = null;
	        Message[] msg = null;
	        
	    	Properties properties = System.getProperties();
	        session = Session.getInstance(properties); //.getDefaultInstance(properties);
	        
		    store = session.getStore("pop3");		 
		   // store.connect("mail.hinawi.ae", "chadi@hinawi.ae", "chadi321");
		    store.connect(userEmail.getEmailhost(),userEmail.getUseremail(),userEmail.getUserEmailPassword());
		    
		    //folder= store.getDefaultFolder();
		   // logger.info(folder.getFullName());
		    
		    folder = store.getFolder("INBOX");
		    folder.open(Folder.READ_WRITE);
		    unreadEmails=folder.getUnreadMessageCount();
		    logger.info("unreadEmails >>> " + unreadEmails);
		    FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            msg = folder.search(ft);
            
	      //  msg = folder.getMessages();
	        for(i = 0; i < msg.length; i++) 
	        {
	        	
	        	Message item=msg[i];
	        	EmailParser emailParser =new EmailParser(item);
	        	emailFromAddress=emailParser.getEmailFromAddress();
	        	emailFrom = emailParser.getEmailFrom();
	        	  emailTo = emailParser.getEmailTo();
		            emailCc = emailParser.getEmailCc();
		            emailSubject = emailParser.getEmailSubject();
		            emailBody = emailParser.getEmailBody();
		            receivedDate = emailParser.getReceivedDate();
		            sentDate = emailParser.getSentDate();
	        	
	            if(receivedDate != null)
	            {
	            dateReceived = sdfRec.format(receivedDate);
	            sortDateReceived=sdf.format(receivedDate);
	            }
	            else if(sentDate != null)
	            {
	            dateReceived = sdfRec.format(sentDate);
	            sortDateReceived=sdf.format(sentDate);
	            }
	           // fileHash = emailParser.getFileHash();
	           // logger.info("fileHash size = "+fileHash.size());
	            
	        	EmailModel obj=new EmailModel();
	        	obj.setSubject(emailSubject);
	        	obj.setEmailFromAddress(emailFromAddress);
	        	obj.setFromEmail(emailFrom);
	        	obj.setEmailTo(emailTo);
	        	obj.setEmailCc(emailCc);
	        	obj.setDateReceived(dateReceived);	        		        	
	        	obj.setBody(emailBody);	  
	        	obj.setSortDateReceived(sortDateReceived);	        	
	        	obj.setEmailCustomer(getCustomerFromEmail(emailFrom));	        		        		        
	        	lstIncomingEmails.add(obj);
	        	//logger.info("------------ Message "+(i + 1)+" ------------" + msg[i].getSubject());
	        }
	        
	        folder.close(true);	        	        
	        store.close();
		    
	        Collections.sort(lstIncomingEmails, new Comparator<EmailModel>(){
	            @Override
	            public int compare(EmailModel a1, EmailModel a2) {
	                 return a2.getSortDateReceived().compareTo(a1.getSortDateReceived());
	            }});
	        
	        lstAllIncomingEmails.addAll(lstIncomingEmails);
		}		
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		}
	}
		
	@Command
	public void openEmailBodyCommand()
	{
	try
	{		
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedEmail", selectedEmail);	
		//Executions.createComponents("updateEmailTemplate.zul", null,arg);	
		
		Window window = (Window)Executions.createComponents("incomingemailbody.zul", null, arg);
		window.doModal();
	}
	catch (Exception ex) 
	{
		logger.error("error in IncomingEmailViewModel---openEmailBodyCommand-->" , ex);
	}
		
	}
	
	@Command
	public void openUnknownEmailCommand()
	{
	try
	{		
		Map<String, Object> arg = new HashMap<String, Object>();	
		arg.put("selectedEmail", selectedEmail);	
		//Executions.createComponents("updateEmailTemplate.zul", null,arg);	
		
		Window window = (Window)Executions.createComponents("unknowemail.zul", null, arg);
		window.doModal();
	}
	catch (Exception ex) 
	{
		logger.error("error in IncomingEmailViewModel---openUnknownEmailCommand-->" , ex);
	}
		
	}
	
	@Command
	@NotifyChange({ "lstIncomingEmails","selectedEmail"})
	public void saveCusomerEmailCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			if(selectedCustomer.getCustkey()>0)
			{
				data.updateCustomerEmail(selectedCustomer.getCustkey(), selectedEmail.getFromEmail());				
				selectedEmail.setEmailCustomer(selectedCustomer);			
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParent", args);	
				comp.detach();						
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in IncomingEmailViewModel---saveCusomerEmailCommand-->" , ex);
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"lstIncomingEmails"})	
	 public void refreshParent()
	 {
		
	 }
	
	private CustomerModel getCustomerFromEmail(String email)
	{
		if(hmCustomerEmails.containsKey(email))
			return hmCustomerEmails.get(email);
		else
		{
			hmCustomerEmails.put(email,data.getCustomerFromEmail(email));
			return hmCustomerEmails.get(email);
		}		
	}

	@Command
	@NotifyChange({ "lstIncomingEmails"})
	public void changeFilter() {
		try {
			lstIncomingEmails = filterData();			

		} catch (Exception ex) {
			logger.error("error in IncomingEmailViewModel---changeFilter-->", ex);
		}
	}
	
	private List<EmailModel> filterData() 
	{
		lstIncomingEmails.clear();
		lstIncomingEmails.addAll(lstAllIncomingEmails);
		List<EmailModel> lst = new ArrayList<EmailModel>();
		for (Iterator<EmailModel> i = lstIncomingEmails.iterator(); i.hasNext();) 
		{
			EmailModel tmp = i.next();
			if (tmp.getEmailFromAddress().toLowerCase()
					.contains(filter.getEmail().toLowerCase())
					&& tmp.getEmailCustomer().getFullName().toLowerCase()
							.contains(filter.getFullname().toLowerCase())
								&& tmp.getEmailCustomer().getCompanyName().toLowerCase()
							.contains(filter.getCompanyName().toLowerCase())
							&& tmp.getSubject().toLowerCase()
							.contains(filter.getDescription().toLowerCase())
							&& tmp.getDateReceived().toLowerCase()
							.contains(filter.getDueDate().toLowerCase())
							
					)
						{
						lst.add(tmp);
						}
		}
		return lst;

	}
	
	public List<EmailModel> getLstIncomingEmails() {
		return lstIncomingEmails;
	}


	public void setLstIncomingEmails(List<EmailModel> lstIncomingEmails) {
		this.lstIncomingEmails = lstIncomingEmails;
	}


	public EmailModel getSelectedEmail() {
		return selectedEmail;
	}


	public void setSelectedEmail(EmailModel selectedEmail) {
		this.selectedEmail = selectedEmail;
	}

	public int getUnreadEmails() {
		return unreadEmails;
	}

	public void setUnreadEmails(int unreadEmails) {
		this.unreadEmails = unreadEmails;
	}

	public WebusersModel getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(WebusersModel userEmail) {
		this.userEmail = userEmail;
	}

	public List<CustomerModel> getLstCustomers() {
		return lstCustomers;
	}

	public void setLstCustomers(List<CustomerModel> lstCustomers) {
		this.lstCustomers = lstCustomers;
	}

	public CustomerModel getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CustomerModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
}
