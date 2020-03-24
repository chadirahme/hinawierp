package crm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;

import org.apache.log4j.Logger;


public class EmailParser 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private String emailFromAddress = null;
	private String emailFrom = null;
	private String emailTo = null;
	private String emailCc = null;
	private String emailSubject = null;
	private Date receivedDate = null;
	private Date sentDate = null;
	private String emailBody = null;
	private HashMap fileHash = new HashMap();
	private boolean errorOccured=false;
	

	
	//Parse the email and set all variables
	public EmailParser(Message msg)
	{
		//logger.info(" parsing the email ");
		Object content = null;
		try
		{	
			Address[] add= msg.getFrom();
			emailFromAddress=add[0].toString();
			
			emailFrom = InternetAddress.toString(msg.getReplyTo());			
			if(emailFrom != null && emailFrom.indexOf("<") != -1)
				emailFrom = emailFrom.substring(emailFrom.indexOf("<")+1, emailFrom.indexOf(">"));
			emailTo = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
			if(emailTo != null&&emailTo.indexOf(">")==1)
				  emailTo=emailTo.substring(2);
			
			if(emailTo != null && emailTo.indexOf("<") != -1)
				emailTo = emailTo.substring(emailTo.indexOf("<")+1, emailTo.indexOf(">"));
			emailCc = InternetAddress.toString(msg.getRecipients(Message.RecipientType.CC));
			if(emailCc != null && emailCc.indexOf("<") != -1)
				emailCc = emailCc.substring(emailCc.indexOf("<")+1, emailCc.indexOf(">"));
			emailSubject = msg.getSubject();
			emailSubject=emailSubject==null?"":emailSubject;
			if(emailSubject.equals(""))
			{
				emailSubject="(no subject)";
			}
			
			receivedDate = msg.getReceivedDate();
			sentDate = msg.getSentDate();
			
			content =  msg.getContent();
            
            if(content instanceof String)
            {
            	emailBody = (String) content;
    	       // logger.info(" Email contains only text with no attachement ");

            }
            else if(content instanceof Multipart)
            {
    	       // logger.info(" Email contains multipart with attachement ");
            	emailBody="Email contains multipart with attachement";
            	//parseMultipart((Multipart) content);
            }
		}
		catch(MessagingException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
		
			logger.info("EmailParser >> ",e);
			//emailsender.sendSimpleEmail(null, null, "An error occured in the RatesEmailsReader Engine: ", "An error occured in the RatesEmailsReader Engine: "+sw.toString());
			setErrorOccured(true);
		}
		catch(Exception ex)
        {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.info("EmailParser >> ",ex);
			setErrorOccured(true);
        }
	}
	
	//Parse the Multipart to find the body and attachments
	public void parseMultipart(Multipart multipart)
	{
		BodyPart bodyPart = null;
		String disposition = null;
		MimeBodyPart mimeBodyPart = null;
		DataHandler handler = null;
		String fileName = null;
		
		try
		{
			  //Loop through all of the BodyPart's
			  for(int i = 0; i < multipart.getCount(); i++)
			  {
				  //Grab the body part
				  bodyPart = multipart.getBodyPart(i);
				  //Grab the disposition for attachments
				  disposition = bodyPart.getDisposition();
				//  logger.info(" disposition "+i+"  = "+disposition);

				  //It's not an attachment
				  if(disposition == null && bodyPart instanceof MimeBodyPart)
				  {
					  mimeBodyPart = (MimeBodyPart) bodyPart;

					  //Check to see if we're in the screwy situation where
					  //the message text is buried in another Multipart
					  if(mimeBodyPart.getContent() instanceof Multipart)
					  {
						  //Use recursion to parse the sub-Multipart
						  parseMultipart((Multipart) mimeBodyPart.getContent());
					  }
					  else
					  {
						  //Time to grab and edit the body
						  if(mimeBodyPart.isMimeType("text/plain"))
						  {
							  //Grab the body containing the text version
							  emailBody = (String) mimeBodyPart.getContent();
						  }
						  else if(mimeBodyPart.isMimeType("text/html"))
						  {
							  //Grab the body containing the HTML version
							  emailBody = (String) mimeBodyPart.getContent();
						  }
					  }
				  }
				  else if(disposition != null && disposition.equalsIgnoreCase(BodyPart.ATTACHMENT))
				  {
		    	     // logger.info(" Found attachement disposition===>>>>>>>"+disposition);
					//  handler = bodyPart.getDataHandler();
					 // fileName = handler.getName();
					//  logger.info(" fileName ===>>>>>>>"+fileName);
                    //  fileHash.put(fileName, handler.getInputStream());
				  }
			  }
		}
		catch(Exception ex)
        {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error("EmailParser",ex);
        }
	}
	
	public String getEmailFrom()
	{
		return this.emailFrom;
	}
	
	public String getEmailTo()
	{
		return this.emailTo;
	}
	
	public String getEmailCc()
	{
		return this.emailCc;
	}
	
	public String getEmailSubject()
	{
		if(this.emailSubject != null)
		{
			this.emailSubject = this.emailSubject.replaceAll("'", "\"");
			this.emailSubject = this.emailSubject.replaceAll("\n", "");
			this.emailSubject = this.emailSubject.replaceAll("\r", "");
		}
		return this.emailSubject;
	}
	
	public Date getReceivedDate()
	{
		return this.receivedDate;
	}
	
	public Date getSentDate()
	{
		return this.sentDate;
	}

	public String getEmailBody()
	{
		if(this.emailBody != null)
			this.emailBody = this.emailBody.replaceAll("'", "\"");
		return this.emailBody;
	}
	
	public HashMap getFileHash()
	{
		return this.fileHash;
	}
	public boolean isErrorOccured() {
		return errorOccured;
	}

	public void setErrorOccured(boolean errorOccured) {
		this.errorOccured = errorOccured;
	}

	public String getEmailFromAddress() {
		return emailFromAddress;
	}

	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
	}

}