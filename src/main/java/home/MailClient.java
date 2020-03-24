package home;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.zip.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.File;

public class MailClient

{
	private static Logger logger = Logger.getLogger(MailClient.class);
	public MailClient()
	{
	
	}	
	public static InternetAddress[] parse(String addresses)
    {
    	InternetAddress[] internetAddress = new InternetAddress[0];
    	List result = new LinkedList();
		  try
		  {
			  addresses = addresses.replaceAll(" ", "");
			  if(addresses.length() > 0 && addresses.substring(addresses.length()-1).equals(","))
				  addresses = addresses.substring(0, addresses.length()-1);

			  StringTokenizer tokenizer = new StringTokenizer(addresses, ",");
			  while (tokenizer.hasMoreTokens()) {
				  String address = tokenizer.nextToken();
				  result.add(new InternetAddress(address));
			  }
		  }
		  catch(Exception ex)
		  {
				StringWriter sw = new StringWriter();
				ex.printStackTrace(new PrintWriter(sw));
				logger.error("error in AZRatesFiles---parse-->"+sw.toString());
		  }
		  finally
		  {
			  return (InternetAddress[]) result.toArray(internetAddress);
		  }
    }
	static Message prepareHeader(String smtp_host, String from,InternetAddress[] to,
			InternetAddress[] cc, InternetAddress[] bcc, String subject, boolean withReceipt)
	{
		Message msg = null;
		try{
			
	  Properties props = new Properties();
	  props.put("mail.smtp.host", smtp_host);
	  Session session = Session.getDefaultInstance(props, null);


	  msg = new MimeMessage(session);


	  msg.addRecipients(Message.RecipientType.TO, to);
	  msg.addRecipients(Message.RecipientType.CC, cc);
	  msg.addRecipients(Message.RecipientType.BCC, bcc);
	

	  InternetAddress from_addr = new InternetAddress(from);
	  msg.setFrom(from_addr);
	  msg.setSubject(subject);
	  
		  if(withReceipt)
		  {
//			  msg.addHeader("Disposition-Notification-To", from);
			  msg.addHeader("Return-Receipt-To", from);
		  }
		  
		  Calendar calendar = Calendar.getInstance();
		  msg.setSentDate(calendar.getTime());
	  }
		catch(Exception ex){
			logger.error("error----Mailer.prepareHeader---->",ex);
		}finally{
			return msg;
		}
	}
	
		public static boolean sendMail(String smtp_host, String from,
			InternetAddress[] to, InternetAddress[] cc, InternetAddress[] bcc,  
			String subject, String message, boolean isHTML, ArrayList fileArray, boolean withReceipt)
			{
				boolean error = false;
				try{
					Message msg = prepareHeader(smtp_host, from, to, cc, bcc, subject, withReceipt);
					
		    		MimeBodyPart messageBodyPart = new MimeBodyPart();
		    		if(isHTML)
		    			messageBodyPart.setContent(message, "text/html");
		    		else
		    			messageBodyPart.setText(message);
					
					Multipart multipart = new MimeMultipart(); 
					multipart.addBodyPart(messageBodyPart);
					
					if(fileArray != null)
					{
						for(int i = 0; i < fileArray.size(); i++)
						{
			    			MimeBodyPart attachmentPart = new MimeBodyPart(); 
			    			FileDataSource fileDataSource = new FileDataSource((String)fileArray.get(i));
			    			attachmentPart.setDataHandler(new DataHandler(fileDataSource));     
			    			attachmentPart.setFileName(fileDataSource.getName());
			    			multipart.addBodyPart(attachmentPart);
						}
					}

					msg.setContent(multipart);

					Transport.send(msg);
				}catch(Exception ex){
					logger.error("error----Mailer.sendMail---->",ex);
					error = true;
				}
				finally
				{
					return error;
				}
			}
			
	public void sendTestEmail()
	{
		try
		{
		  Properties props = new Properties();
		  props.put("mail.smtp.host", "localhost");
		  props.put("mail.smtp.socketFactory.port", 465);
		  props.put("mail.transport.protocol", "smtp");  
		  props.put("mail.smtp.auth", "false");
		  props.put("mail.smtp.user", "info@hinawionline.com");
		  
		  Message msg = prepareHeader("localhost", "info@hinawionline.com", parse("eng.chadi@gmail.com"), null, null, "Test", false);
		  MimeBodyPart messageBodyPart = new MimeBodyPart();
		  messageBodyPart.setText("message");
		  
		  Multipart multipart = new MimeMultipart(); 
		  multipart.addBodyPart(messageBodyPart);
			
		  msg.setContent(multipart);

		 Transport.send(msg);
		 // MimeMessage message = new MimeMessage();
		 // message.setSubject(subject);
		}
		catch(Exception ex)
		{
			logger.error("error----Mailer.sendTestEmail---->",ex);			
		}
		  
	}
		
	 public void sendMochaMail(String[] to,String[] cc,String[] bcc,String subject, String messageBody,boolean isHTML,ArrayList fileArray,boolean withReceipt,String type,String fromAdress) throws AddressException, MessagingException, IOException 
	 {
		 try
		 {
			 if(true)
				 return;
		 InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("resources/application.properties");			 
		 Properties prop = new Properties();
		 prop.load(inputStream);
		 String host=prop.getProperty("mailserver");
		 String port=prop.getProperty("port");
		 String mailuser=prop.getProperty("mailuser");
		 final String mailfrom=prop.getProperty("mailfrom");
		 final String mailpassword=prop.getProperty("mailpassword");
		 		 
		 // String to="eng.chadi@gmail.com";
		  Properties props = new Properties();
		  props.put("mail.smtp.host", host);
		  //props.put("mail.smtp.socketFactory.port", port);
		  props.put("mail.smtp.port", port);//587
		  props.put("mail.transport.protocol", "smtp");  
		  props.put("mail.smtp.auth", "true"); //use to send from local pc
		  //props.put("mail.smtp.auth", "false"); //use to send from host server with mailserver=localhost
		  props.put("mail.smtp.user", mailuser);   
		   
		   logger.info("host>> " + host);
		   logger.info("port>> " + port);
		   logger.info("mailuser>> " + mailuser);
		   logger.info("mailpassword>> " + mailpassword);
		   
			Session session = Session.getDefaultInstance(props,
					   new javax.mail.Authenticator() {
					   protected PasswordAuthentication getPasswordAuthentication() {
					   return new PasswordAuthentication(mailfrom,mailpassword);//change accordingly
					   }
					  });
			
			   String charset="UTF-8";
			   MimeMessage message = new MimeMessage(session);
			   message.setSubject(subject,charset);
			   
			   if(type.equalsIgnoreCase("feedback"))
			   message.setFrom(new InternetAddress("feedback@hinawi.ae","Hinawi Software"));//change accordingly
			   else if(type.equalsIgnoreCase("quotation"))
			   message.setFrom(new InternetAddress("contactus@hinawi.ae","Hinawi Software"));//change accordingly
			   else if(type.equalsIgnoreCase("task"))
			   message.setFrom(new InternetAddress("tasks@hinawi.ae","Hinawi Software"));//change accordingly
			   else if(type.equalsIgnoreCase("leave"))
			   message.setFrom(new InternetAddress("leave@hinawi.ae"));//change accordingly
			   else if(type.equalsIgnoreCase("passport"))
			   message.setFrom(new InternetAddress("passportRequest@hinawi.ae"));//change accordingly
			   else if(type.equalsIgnoreCase("Timesheet"))
			   message.setFrom(new InternetAddress("timesheet@hinawi.ae"));//change accordingly
			   else if(type.equalsIgnoreCase("cutomerfollowUp"))
			   message.setFrom(new InternetAddress(fromAdress,"Explorer Computer LLC"));//change accordingly
			   else if(type.equalsIgnoreCase("Reminder"))
			   message.setFrom(new InternetAddress("support@hinawi.ae"));//change accordingly
			   else 
			   message.setFrom(new InternetAddress(mailfrom,"Hinawi Software"));//change accordingly 
			  
			   if(type.equalsIgnoreCase("cutomerfollowUp"))
			   {
				   if(bcc!=null)
				   {
					   for(int i=0;i<bcc.length;i++)
					   {
						   message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));//Make to Address  as BCC
				    	  logger.info("BCC Email Address>> " + new InternetAddress(bcc[i]));
					   }
				   }
			   }
			   if(to!=null)
			   {
				   for(int i=0;i<to.length;i++)
			        {
				    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
				    	  logger.info("To Email Address>> " + new InternetAddress(to[i]));
			        }
				   
			   }
			   
			    if(cc!=null)
			    {
			    	for(int i=0;i<cc.length;i++)
			    	{
			    		 if(cc[i]!=null)
						    {
			    			 	message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			    			 	logger.info("CC Email Adress >> " + new InternetAddress(cc[i]));
						    }
			    	}
			    }
			 //  message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			    if(withReceipt)
				  {
			    	message.addHeader("Disposition-Notification-To", "hinawi@eim.ae,sales@hinawi.ae");
				  }
			    MimeBodyPart messageBodyPart = new MimeBodyPart();
	    		if(isHTML)
	    			messageBodyPart.setContent(messageBody, "text/html;charset=UTF-8");
	    		else
	    			messageBodyPart.setText(messageBody);
				
				Multipart multipart = new MimeMultipart(); 
				multipart.addBodyPart(messageBodyPart);
				
				if(fileArray != null)
				{
					for(int i = 0; i < fileArray.size(); i++)
					{
		    			MimeBodyPart attachmentPart = new MimeBodyPart(); 
		    			FileDataSource fileDataSource = new FileDataSource((String)fileArray.get(i));
		    			attachmentPart.setDataHandler(new DataHandler(fileDataSource));     
		    			attachmentPart.setFileName(fileDataSource.getName());
		    			multipart.addBodyPart(attachmentPart);
					}
				}

			  // message.setSubject(subject);
			  // message.setText(messageBody);
				message.setContent(multipart);
			   Transport.send(message);
			  //System.out.println("Done>>>>>>>>>");
		 }
		   catch(Exception ex)
			{
				logger.error("error----Mailer.sendTestEmail---->",ex);			
			}
	 }
	 
	 
   public void sendGmailMail(String mailServer, String from, String[] to,
                            String subject, String messageBody,
                            String[] attachments) throws
      MessagingException, AddressException
    {
	   try
	   {
        // Setup mail server
	   final  String user = "eng.chadi@gmail.com";
	   final  String pass = "gmail331980";
       Properties props =new Properties(); //System.getProperties();
        props.put("mail.smtp.starttls.enable", "true"); // added this line
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");//587
        props.put("mail.smtp.auth", "true");
        
        // Get a mail session
       // Session session = Session.getDefaultInstance(props, null);
        
        logger.info("from>> " + from);
		logger.info("to>> " + to);
		   
		   
        Session session = Session.getInstance(props,
      		  new javax.mail.Authenticator() {
      			protected PasswordAuthentication getPasswordAuthentication() {
      				return new PasswordAuthentication(user, pass);
      			}
      		  });
        
        
        // Define a new mail message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        for(int i=0;i<to.length;i++)
        {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
        }
        message.setSubject(subject);
        
        // Create a message part to represent the body text
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageBody, "text/html");
       
        
        //use a MimeMultipart as we need to handle the file attachments
        Multipart multipart = new MimeMultipart();
        
        //add the message body to the mime message
        multipart.addBodyPart(messageBodyPart);
        
        if(attachments!=null&&attachments.length>0)
        {
          // add any file attachments to the message
          addAtachments(attachments, multipart);
        }
        
        // Put all message parts in the message
        message.setContent(multipart);
        
        // Send the message
        //Transport transport = session.getTransport("smtp");
        //transport.connect(mailServer, user, pass);
        //transport.sendMessage(message, message.getAllRecipients());
        //transport.close();
                
       Transport.send(message);
	   }
        catch(Exception ex)
		{
			logger.error("error----Mailer.sendTestEmail---->",ex);			
		}

    }

    protected void addAtachments(String[] attachments, Multipart multipart)
                    throws MessagingException, AddressException
    {
        for(int i = 0; i<= attachments.length -1; i++)
        {
            String filename = attachments[i];
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            
            //use a JAF FileDataSource as it does MIME type detection
            DataSource source = new FileDataSource(filename);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            
            //assume that the filename you want to send is the same as the
            //actual file name - could alter this to remove the file path
            attachmentBodyPart.setFileName(filename);
            
            //add the attachment
            multipart.addBodyPart(attachmentBodyPart);
        }
    }
    
    public static void deleteDir(String path)
    {
  	  File dir = new File(path);
  	    
  	    String[] children = dir.list();
  	    if (children == null) {
  	        // Either dir does not exist or is not a directory
  	    } else {
  	        for (int i=0; i<children.length; i++) {
  	            // Get filename of file or directory
  	            String filename = children[i];
  	            File file=new File(path+filename);
  	            file.delete();
  	        }
  	    }
    }

    public   String zipTheFiles(String[] filenames,String path)
    {
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
 	    Date today=new Date();
    	//Logging log
    	String fileName= path+"/Files/RATES_"+df.format(today)+".zip";
    	// Create a buffer for reading the files
    	byte[] buf = new byte[1024];
    
    	try {
    		// Create the ZIP file
    		
    		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileName));
            
    		 out.setLevel(9);
    		// Compress the files
        for (int i=0; i<filenames.length; i++) {
            FileInputStream in = new FileInputStream(filenames[i]);
         
            // Add ZIP entry to output stream.
           String realFileName=filenames[i].substring(filenames[i].lastIndexOf("/")+1,filenames[i].length());
            
            out.putNextEntry(new ZipEntry(realFileName));
    
            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
    
           
            // Complete the entry
            out.closeEntry();
            in.close();
        }
    
		        // Complete the ZIP file
		        out.close();
		      
    		} 
    	catch (IOException e) 
    		{
    		try{
    		   MailClient mc=new MailClient();
    		   Properties properties = new Properties();
    		   properties.load(new FileInputStream(path+"ratesgenerator.properties"));
    		   StringWriter sw = new StringWriter();
			   e.printStackTrace(new PrintWriter(sw));
			   String [] problemReportTo=properties.getProperty("reportToAddress").split(",");  
               //mc.sendMail(properties.getProperty("mailServer"), properties.getProperty("from"), problemReportTo, "Exception in Set out rates",  sw.toString(), null);
    		}
    		catch(Exception ex)
    		{
    			//logger.logErrorMsg(ex.getMessage(), this.getClass().getName());
    		}
    		//logger.logErrorMsg(e.getMessage(), this.getClass().getName());
    		}
    
    	return fileName;

    }
    
}
 
