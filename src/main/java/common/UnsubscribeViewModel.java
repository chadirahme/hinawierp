package common;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import db.DBHandler;

public class UnsubscribeViewModel 
{
	//http://localhost:8080/sata/Unsubscribe.zul?email=5uxR1dxTctEmahMSgzPJL5K1cR/rXFdMENeiVTbbMGw=
	//http://hinawi2.dyndns.org:8181/sata/Unsubscribe.zul?email=5uxR1dxTctEmahMSgzPJL5K1cR/rXFdMENeiVTbbMGw=
	
	private Logger logger = Logger.getLogger(this.getClass());
	String queryParam ="";
	private String email;
	private boolean subscribe;
	private String msg;
	int emailID=0;
	
	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	
	@Init
    public void init(@QueryParam("email") String emailparm)
	{
		try
		{
		subscribe=true;
        queryParam = emailparm;
        logger.info(queryParam);
        queryParam=queryParam==null?"":queryParam;
        
        if(!queryParam.equals(""))
        {	
        email=EncryptEmail.decrypt(queryParam);
    	logger.info(email);	
		if(email.equals(""))
		{
			msg="This email is not valid in our mail list";
			subscribe=true;
			return;
		}
		
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		if(!matcher.matches())
		{
			msg="This email is not valid !!";
			subscribe=true;
			return;
		}
		
		
    	//check if email is un subscribe
    	DBHandler db=new DBHandler();
		ResultSet rs=null;
		String sql="Select * from contactemails where email='" +email + "'";
		//sql=String.format(sql, email);
		rs=db.executeNonQuery(sql);
		
		while(rs.next())
		{
			emailID=rs.getInt("EmailId");
			subscribe=rs.getBoolean("unsubscribe");			 		
		}			
    	
		if(emailID==0)
		{
			msg="This email " + email + " not in our mail list";
			subscribe=true;
		}
		else
		{
			if(subscribe==false)
			{
				msg="Are you sure to Unsubscribe this email " + email + " from our system ?";
			}
			else
			{
				msg="This email " + email + " already Unsubscribe from our system .";
			}
		}
       }
    	
		}
		catch(Exception ex)
		{
			subscribe=true;
			Messagebox.show(ex.getMessage());
			logger.info("error----UnsubscribeViewModel---->",ex);		
		}
 
    }
	
	public UnsubscribeViewModel()
	{
		try
		{
			 logger.info(queryParam);
		}
		
		catch(Exception ex)
		{
			Messagebox.show(ex.getMessage());
			logger.error("error----UnsubscribeViewModel---->",ex);		
		}
	}
	
	@Command 
	public void UnSubscribeCommnand()
	{
		try
		{
			DBHandler db=new DBHandler();
			String sql="";
			 
				 if(emailID>0)
				 {
					 //sql="delete from contactemails where Email='" + email+"'";
					 sql="update contactemails set unsubscribedate=now() , unsubscribe=1" 
							 + " where EmailId=" + emailID;
					 db.executeUpdateQuery(sql);				 				 
				 }
	
				// Clients.showNotification("Your Email is unSubscribe from our mail list. ",
				 //           Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 4100);
				 
		int x= Messagebox.show("Your Email is unSubscribe from our mail list. ","unSubscribe Email", Messagebox.OK , Messagebox.INFORMATION);
		// logger.info(x);
		 if(x>0)
		 Executions.sendRedirect("index.zul");
		 
		}
		
		catch(Exception ex)
		{
			Messagebox.show(ex.getMessage());
			logger.error("error----UnSubscribeCommnand---->",ex);		
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
