package common;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;
import org.zkoss.util.media.Media;

import setup.users.WebusersModel;

public class CompanyProfile 
{

	public static int CompanyID()
	{
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser = (WebusersModel) sess.getAttribute("Authentication");
		int companyId = 0;
		if (dbUser != null) 
		{
			companyId=dbUser.getCompanyid();			
		}
		return companyId;
	}
	
	public static String CompanyMainRepository()
	{
		String repository = System.getProperty("catalina.base") + File.separator + "uploads" + File.separator + CompanyID() + File.separator;	
		return repository;
	}
	
	public static String CompanyProspectivesRepository()
	{
		String repository = CompanyMainRepository() + "Prospectives" + File.separator;	
		return repository;
	}
	
	public static String ProspectivesAttachmentRepository()
	{
		String repository = CompanyMainRepository() + "ProspectivesDoc" + File.separator;	
		return repository;
	}
	
	public static String CustomersAttachmentRepository()
	{
		String repository = CompanyMainRepository() + "CustomersDoc" + File.separator;	
		return repository;
	}
	
	public static int createFile(Media mediaFile , String filePath, String filename) 
	{
		int res = 0;
		try 
		{
			String path = "";
			InputStream is=null;
			if(filename.toLowerCase().contains(".txt"))
			{
				is =IOUtils.toInputStream(mediaFile.getStringData(), "UTF-8"); 
			}
			if(is==null)
			{
			if(mediaFile.isBinary())
			{
				is=mediaFile.getStreamData();
			}
			else
			{
				is=new ByteArrayInputStream(mediaFile.getByteData());
			}			
			}
			
			File targetFile = new File(filePath);
			targetFile.setExecutable(true, false);
			targetFile.setReadable(true, false);
			targetFile.setWritable(true, false);
			// using PosixFilePermission to set file permissions 777
			targetFile.mkdirs();
			File newFile = new File(targetFile.getAbsolutePath(), "" + filename);
			newFile.setExecutable(true, false);
			newFile.setReadable(true, false);
			newFile.setWritable(true, false);

			newFile.canWrite();
			newFile.createNewFile();
			path = newFile.getAbsolutePath();
			File parent = newFile.getAbsoluteFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: "
						+ parent);
			}
			File file = new File(path);
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(file)));
			int c;
			while ((c = is.read()) != -1) {
				out.writeByte(c);
			}
			is.close();
			out.close();
		} catch (Exception ex) {
			res = 1;
			Messagebox.show(ex.getMessage());
		}
		return res;
	}
	
	
}
