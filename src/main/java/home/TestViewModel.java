package home;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
//import javax.websocket.RemoteEndpoint;

import model.ContentGCM;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;

import common.EncryptEmail;
import common.POST2GCM;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class TestViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	int x=0;
	private String emailTo;
//	ZKWebSocketServer test;
	private String msg;
	
	public TestViewModel()
	{
		try
		{
		emailTo="eng.chadi@gmail.com";
		//test=new ZKWebSocketServer(new URI("ws://localhost:8080/sata/websocket"));
		
		//GoogleDrive g=new GoogleDrive();
		//g.getFiles();
		
		}
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			//logger.error(sw.toString());
			logger.info("inti test",ex);
		}
	}
	
	@Command 	
	public void sendEmailCommand()
	{
		try
		{
//			if(emailTo.isEmpty())
//			{
//				Messagebox.show("Enter Email !!!");
//				return;
//			}
			
			MailClient mc = new MailClient();
			//mc.sendTestEmail();
			String tomail=emailTo;//"eng.chadi@gmail.com";
			String[] to=tomail.split(",");
			//mc.sendTestEmail();
			//mc.sendGmailMail("", "eng.chadi@gmail.com",to, "subject", "messageBody", null);
			mc.sendMochaMail(to, null, null, "Subject", "Body", false, null, true, "", null);	
			Messagebox.show("Done..");
		}
		catch(Exception ex)
		{
			Messagebox.show(ex.getMessage());
			logger.error("error----Mailer.sendTestEmail---->",ex);		
		}
	}
	
	@Command 	
	public void sendGmailEmailCommand()
	{
		
		try 
		{
			if(emailTo.isEmpty())
			{
				Messagebox.show("Enter Email !!");
				return;
			}

			MailClient mc = new MailClient();
			//mc.sendTestEmail();
			String tomail=emailTo;//"eng.chadi@gmail.com";
			String[] to=tomail.split(",");
			//mc.sendTestEmail();
			mc.sendGmailMail("", "eng.chadi@gmail.com",to, "Gmail subject", "messageBody", null);
			//mc.sendMochaMail(to, null, null, "Subject", "Body", false, null, true, "", null);
			Messagebox.show("Done..");
		} 
		catch(Exception ex)
		{
			Messagebox.show(ex.getMessage());
			logger.error("error----Mailer.sendTestEmail---->",ex);		
		}		
	}
	
	@Command 	
	public void getEmailCommand()
	{
		try	
		{
			int i=0;
			Session session = null;
	        Store store = null;
	        Folder folder = null;
	        Message[] msg = null;
	        
	    	Properties properties = System.getProperties();
	        session = Session.getDefaultInstance(properties);
	        
		    store = session.getStore("pop3");
		  //  store.connect("EXCHANGEUK.teltacworldwide.co", "Chadi.Rahme@teltacworldwide.com", "ch@d1rahme!!");	
		    store.connect("mail.hinawi.ae", "chadi@hinawi.ae", "chadi321");
		    
		    
		    folder= store.getDefaultFolder();
		    logger.info(folder.getFullName());
		    
		    folder = store.getFolder("INBOX");
		    folder.open(Folder.READ_WRITE);
	        msg = folder.getMessages();
	        for(i = 0; i < msg.length; i++) 
	        {	        	
	        	  Message item=msg[i];	  
	        	  Address[] add= item.getFrom();
	        	 
	        	  logger.info("------------ Message "+(i + 1)+" ------------" +  add[0]);
	        	  logger.info("------------ Message "+(i + 1)+" ------------" +  add[0].toString());	        	 
	        	  
	        }
	        
	        folder.close(true);
	        
	        
	        store.close();
		    
		}		
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		}
	}

	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = 
		    new byte[] {'C','O','M','P','U','T','E','R','C','O','M','P','U','T','E','R'};//{ 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

	@Command 
	public void encryptCommand()
	{
		try	
		{
			String x=EncryptEmail.encrypt(emailTo);
			logger.info(x);			
			logger.info(EncryptEmail.decrypt(x));	
			
		}
		
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		}
	}
	
	@Command 
	public void testAlarm()
	{
		try
		{
			//org.zkoss.zk.ui.Session sess = Sessions.getCurrent();
			//RemoteEndpoint remote = sess.getr.getRemoteHost();
			//final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://real.okcoin.cn:10440/websocket/okcoinapi"));
		
			//test.sendMessage(emailTo);
					
						
		}
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		}
	}
	@Command 
	public void uploadImage()
	{
		try
		{
			Media media = Fileupload.get();
			String type = media.getContentType().split("/")[0];
			logger.info(type);
			
			//UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
			String filePath="";
			String fileFormat="";
			//String repository = System.getProperty("catalina.home")+File.separator+"uploads"+File.separator;
			//filePath=event.getMedia().getName();
			logger.info(filePath);
			
		}
		catch(Exception ex)
		{
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error(sw.toString());
		}
	}
	
	@Command("upload")	
	public void onImageUpload(BindContext ctx) 
	{
	   UploadEvent upEvent = null;
	   Object objUploadEvent = ctx.getTriggerEvent();
	   if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
	     upEvent = (UploadEvent) objUploadEvent;
	    }
	    if (upEvent != null) {
	     Media media = upEvent.getMedia();
	     int lengthofImage = media.getByteData().length;
	     media.getByteData();
	     
	     logger.info("" + lengthofImage);
	     
	    }
	 }
	
	
	@Command
	public void gcmCommand()
	{
		POST2GCM p=new POST2GCM();
		/*ContentGCM c=new ContentGCM();
		 c.addRegId("cuFFeEcTJGs:APA91bGCpOxsm3Z0hEEPhp7oJFWbSX6DyNBu0EXwjjX2QjMRyVCFFp1CWy-kTQabp7HcWYbc2Ry4E_Fj2GCK4r4OhnV1tcpe6dN2goeDKRLLzei5RJLFBodetpJywK7IFF3h30BUL7NF");
		c.createData("VOGELA", emailTo);		
		p.post("AIzaSyDHVny4R3Vfv86MOq-CR-kJ1kTkkPllz1A",c);	
		*/
		
		ContentGCM c=new ContentGCM();
		c.addRegId("APA91bF3C6YWaJU2j9gCJ34gnwno7NFwhUor1idZu2pbvsaujmNbPakCTXqxLpMSDzALej48YAUBWT4ymRdNKdo5OTtqUI4lKw4kLHJqkGW7XcZZpUq--c4");
		c.setEmployeeName("Chadi Rahme");
		c.setPosition("Developer");
		c.setDepartment("Development");
		c.setDescreption("Your Timesheet that you have entered From 04/06/2016 To 04/06/2016 from web application has been Created .");
		c.setTomorrowPlan("testing timesheet email going to Alnahda school then going to GHQ to install...");
		
		c.createData("ERP", "Timesheet Submitted by Chadi Rahme");		
	
		p.post("AIzaSyCBdzcYFM71qpfPvuGn2VArWrYK3wFZRHU",c);
	}
	
	
	private static Key generateKey() throws Exception {
	    Key key = new SecretKeySpec(keyValue, ALGORITHM);
	    return key;
	}
	
	public String encrypt(String valueToEnc) throws Exception {
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGORITHM);
	    c.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encValue = c.doFinal(valueToEnc.getBytes());
	    String encryptedValue = new BASE64Encoder().encode(encValue);
	    return encryptedValue;
	}
	
	public String decrypt(String encryptedValue) throws Exception {
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGORITHM);
	    c.init(Cipher.DECRYPT_MODE, key);
	    byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
	    byte[] decValue = c.doFinal(decordedValue);
	    String decryptedValue = new String(decValue);
	    return decryptedValue;
	}
	
	public String getEmailTo() {
		return emailTo;
	}

	@NotifyChange("msg")
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
