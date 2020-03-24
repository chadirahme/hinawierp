package home;

import org.apache.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class Mailer {

	private static Logger logger = Logger.getLogger(Mailer.class);

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
	
}
