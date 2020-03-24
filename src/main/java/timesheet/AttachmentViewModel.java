package timesheet;

import home.QuotationAttachmentModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import model.CompanySettingsModel;
import model.TimeSheetGridModel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

public class AttachmentViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	private TimeSheetGridModel row;
	CompanySettingsModel compSettings;
	public AttachmentViewModel()
	{
		try		
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			row=(TimeSheetGridModel)map.get("row");
			compSettings=(CompanySettingsModel)map.get("compSettings");	
			if(row.getOldRecNo()>0 && row.getListOfattchments().size()==0)
			{
				row.setListOfattchments(data.getTimesheetAttchamnet(row.getOldRecNo()));
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AttachmentViewModel ----> init", ex);			
		}
	}
	
	@Command
	 @NotifyChange({"row"})
	public void attachFileCommand(BindContext ctx)
	 {
		try
		{
			
		 logger.info(row.getTsDate() + " >>> " + row.getEmpKey() +">>>> " + row.getRecNo());		 
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(row.getTsDate());
		 String monthName=new SimpleDateFormat("MMM").format(cal.getTime());
		 String yearName=new SimpleDateFormat("yyyy").format(cal.getTime());
		 //tsGroupModel=new TimeSheetGroupAdapter(lstGrid, new TimeSheetComparator(), true);
		 String filePath="",dirPath="";	
		/* //testing
		 UploadEvent testevent = (UploadEvent)ctx.getTriggerEvent();	
		 QuotationAttachmentModel testatt=new QuotationAttachmentModel();
			
         //use for go dady
         dirPath=File.separator + compSettings.getFtpDirectory()+File.separator;
         filePath=dirPath + testevent.getMedia().getName();
         testatt.setFilepath(filePath);
         testatt.setSerialNumber(row.getRecNo());
         testatt.setFilename(testevent.getMedia().getName());
   	  	 row.getListOfattchments().add(testatt);
   	  	 if(true)
			return;*/
	/*	 row.setAttachPath("test.123");
	 
		if(true)
			return;*/
	
		 
		 if(! compSettings.getFtpHost().equals(""))
		 {
			 int port = 21;
			 if(compSettings.getFtpPort()>0)
			 port=compSettings.getFtpPort();
			 FTPClient ftpClient = new FTPClient();
			 ftpClient.connect(compSettings.getFtpHost(), port);
			 int replyCode = ftpClient.getReplyCode();
			 if (!FTPReply.isPositiveCompletion(replyCode)) {
				 	logger.info("Operation failed. Server reply code: " + replyCode);		               
	                return;
	            }
			 	boolean success = ftpClient.login(compSettings.getFtpUser(),compSettings.getFtpPassword());		           
	            if (!success) {		              
	                logger.info("Could not login to the server");
	                return;
	            } else {		               
	                logger.info("LOGGED IN SERVER");
	            }
	            
	            ftpClient.enterLocalPassiveMode();	            
	            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	         
	            UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
	            dirPath=File.separator + compSettings.getFtpDirectory()+File.separator +yearName+File.separator + row.getEmpKey()+File.separator+monthName + File.separator;
	            ftpClient.changeWorkingDirectory(dirPath);
	            int returnCode = ftpClient.getReplyCode();
	            if (returnCode == 550) {
	                // file/directory is unavailable
	            	ftpClient.makeDirectory(dirPath);
	            }
	            
	             filePath="";		
	            //use for go dady
	            //dirPath=File.separator + compSettings.getFtpDirectory()+File.separator;
	            		
	            filePath=dirPath + event.getMedia().getName();
	            		           
	            String remoteFile =filePath; 
	            
	            logger.info("Start uploading file");
	            InputStream inputStream=null;
	            if(event.getMedia().isBinary())
	            {
	            	inputStream = event.getMedia().getStreamData();
	            	 boolean done = ftpClient.storeFile(remoteFile, inputStream);
			            inputStream.close();
			            if (done) {
			            	  logger.info("The media file is uploaded successfully.");
			            	  row.setAttachPath(filePath);
			            	  QuotationAttachmentModel att=new QuotationAttachmentModel();
			            	  att.setFilepath(remoteFile);
			            	  att.setFilename(event.getMedia().getName());
			            	  row.getListOfattchments().add(att);
			            	  }
	            }
	            else
	            {
	            	 String s = event.getMedia().getStringData();
	            	// logger.info(s);
	            	 inputStream =new ByteArrayInputStream(s.getBytes()); //event.getMedia().getStreamData();
	            	 //int size = inputStream.available(); 
	            	// byte [] buf = new byte[size];
	            	// inputStream.read(buf);
	            	 boolean done = ftpClient.storeFile(remoteFile, inputStream);
			            inputStream.close();
			            if (done) {
			            	  logger.info("The string data file is uploaded successfully.");
			            	  row.setAttachPath(filePath);
			            	  QuotationAttachmentModel att=new QuotationAttachmentModel();
			            	  att.setFilepath(remoteFile);
			            	  att.setFilename(event.getMedia().getName());
			            	  row.getListOfattchments().add(att);
			            }
	            }
	           // InputStream inputStream = event.getMedia().getStreamData();//new FileInputStream(firstLocalFile);
	 
		 }
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AttachmentViewModel ----> attachFileCommand", ex);			
		}
	 }
	
	@Command
 	public void downloadFileCommand(@BindingParam("row") QuotationAttachmentModel row)
 	{
 		FTPClient ftpClient = new FTPClient();
 		try
 		{
 			if(!compSettings.getFtpHost().equals(""))
			 {
				 int port = 21;
				 if(compSettings.getFtpPort()>0)
				 port=compSettings.getFtpPort();
				 
				 ftpClient.connect(compSettings.getFtpHost(), port);
				 int replyCode = ftpClient.getReplyCode();
				 if (!FTPReply.isPositiveCompletion(replyCode)) {
					 	logger.info("Operation failed. Server reply code: " + replyCode);		               
		                return;
		            }
				 	boolean success = ftpClient.login(compSettings.getFtpUser(),compSettings.getFtpPassword());		           
		            if (!success) {		              
		                logger.info("Could not login to the server");
		                return;
		            } else {		               
		                logger.info("LOGGED IN SERVER");
		            }
		            
		            ftpClient.enterLocalPassiveMode();	            
		            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		            
		            String remoteFile1 = row.getFilepath();//"/timesheet/3/Jan/vs 2013 install.txt";
		            InputStream stream = ftpClient.retrieveFileStream(remoteFile1);
		            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		        	String mimeType=mimeTypesMap.getContentType(row.getFilepath());
		        	logger.info("mimeType>>" +  mimeType);
		        	
		        	int index = remoteFile1.lastIndexOf("\\");
		        	String fileName=remoteFile1.substring(index+1);
		        	logger.info(fileName);
		            Filedownload.save(IOUtils.toByteArray(stream), mimeType,fileName);
		            
		          /*  BufferedReader reader = null;
		            String firstLine = null;
		            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		            firstLine = reader.readLine();
		            logger.info(">>>>> "+firstLine);
		            if (reader != null) 
		            	reader.close(); */
		            
		        /*   // File downloadFile1 = new File("D:/Downloads/video.mp4");
		            OutputStream local  = new FileOutputStream("c:/test.txt");
		         //   URL url = new URL("ftp://"+user+"":""+password+""@""+host+""/""+remoteFile+"";type=i");
		            	
		            //InputStream is = ftpClient.
		            BufferedInputStream bis = new BufferedInputStream(is);
		           // File file=new File(
		            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
					String mimeType="txt";//mimeTypesMap.getContentType(file);
		            Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, row.getAttachPath());
		            
					ftpClient.retrieveFile(remoteFile1, local);
					logger.info("file retrive from server");
					BufferedOutputStream bos = new BufferedOutputStream(local);
					byte[] buffer = new byte[1024];
				      int readCount;

				      while( (readCount = bis.read(buffer)) > 0)
				      {
				        bos.write(buffer, 0, readCount);
				      }
				      bos.close();*/
					
			 }
 		}
 		catch (Exception ex)
		{	
			logger.error("ERROR in AttachmentViewModel ----> downloadFileCommand", ex);			
		}
 		finally
 		{
 			try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (Exception ex) {
            	logger.error("ERROR in AttachmentViewModel Finally ----> downloadFileCommand", ex);			
            }
 		}
 	}
	
	@Command	
    public void okCommand(@ContextParam(ContextType.VIEW) Window comp) 
	{
		Map args = new HashMap();		
		args.put("row", row);
		BindUtils.postGlobalCommand(null, null, "attachFileClose", args);		
		comp.detach();
	}
	
	@Command 
	@NotifyChange({"row"})
	public void deleteFromAttchamentList(@BindingParam("row") QuotationAttachmentModel obj)
	{
		try {
			QuotationAttachmentModel tempModel=new QuotationAttachmentModel();
			for(QuotationAttachmentModel item:row.getListOfattchments())
			{
				if(obj.getAttachid()>0 && item.getAttachid()==obj.getAttachid())
				{
					data.deleteTimesheetAttachmnet(0, obj.getAttachid());
					tempModel=item;
					break;					
				}
				else if(obj.getAttachid()==0 && item.getFilename().equalsIgnoreCase(obj.getFilename()))
				{
					tempModel=item;
					break;
				}
			
			}
			row.getListOfattchments().remove(tempModel);
			
		}
		catch (Exception e) {
			logger.error("ERROR in EditTaskListViewModel ----> deleteFromAttchamentList", e);			
		}
		}

	public TimeSheetGridModel getRow() {
		return row;
	}

	public void setRow(TimeSheetGridModel row) {
		this.row = row;
	}
}
