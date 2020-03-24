package login;

import java.sql.ResultSet;
import java.util.List;

import db.SQLDBHandler;
import model.CompanyDBModel;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zul.Messagebox;

import db.DBHandler;
import HibernateUtilities.HibernateUtil;
import setup.users.Webusers;
import setup.users.WebusersModel;

public class LoginData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	public WebusersModel getUserLogin1(WebusersModel objLogin)
	{
		WebusersModel dbUser=new WebusersModel();
		
		Transaction trns = null;		
	 	Session session = HibernateUtil.getSessionFactory().openSession();	
	 	try{
	 		 trns = session.beginTransaction();
	 		 String sql="from Webusers where username = :username and userpwd= :userpwd";
	 		 Query query = session.createQuery(sql);
	 		 query.setString("username", objLogin.getUsername());
	 		 query.setString("userpwd", objLogin.getUserpwd());
	 		 Webusers data=(Webusers)query.uniqueResult();
	 		if(data!=null)
	 		{
	 			dbUser.setUserid(data.getUserid());
	 			dbUser.setFirstname(data.getFirstname());
	 			dbUser.setRoleid(data.getUserrole().getRoleid());
	 		}
	 		// List<Webusers>  lstData = (List<Webusers>)session.createQuery(query)		 		 
	 		 //.list();	 		
	 		 //trns.commit();	
	 	}
	 	catch (RuntimeException e) {
		 	   if(trns != null){
		 	    trns.rollback();
		 	   }
		 	   e.printStackTrace();
		 	   Messagebox.show(e.getMessage());
		 	  } finally{
		 	   session.flush();
		 	   session.close();
		 	   return dbUser;
		 } 	 	
		
	}

	public WebusersModel getUserLoginSQL(WebusersModel objLogin)
	{
		WebusersModel dbUser=new WebusersModel();
		CompanyDBModel obj=new CompanyDBModel();
		obj.setCompanyId(1);
		obj.setDbid(1);
		obj.setUserip("hinawi2.dyndns.org");
		obj.setDbname("WEBDEMO");
		obj.setDbuser("sa");
		obj.setDbpwd("P@ssw0rd@0nline");
		obj.setDbtype("HR");
		SQLDBHandler db=new SQLDBHandler(obj);
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery("Select * from users where email='demo@hinawi.com' and userpassword='Passw0rd!'");
			while (rs.next()) {
				dbUser.setUserid(rs.getInt("userid"));
				dbUser.setUsername(rs.getString("username"));
				dbUser.setAdminuser("1");
				dbUser.setRoleid(2);
//				dbUser.setFirstname(rs.getString("firstname"));
//				dbUser.setRoleid(rs.getInt("roleid"));
//				dbUser.setCompanyid(objLogin.getCompanyid());
//				dbUser.setCompanyName(rs.getString("companyname"));
//				//dbUser.setAdminuser(rs.getString("isadmin")==null?"0":"1");
//				dbUser.setUseremail(rs.getString("useremail") == null ? "" : rs.getString("useremail"));
//				dbUser.setMergedDatabse(rs.getString("mergedDatabase") == null ? "" : rs.getString("mergedDatabase"));//temporary code
//				dbUser.setAdminuser(rs.getString("isadmin"));
//				dbUser.setSupervisor(rs.getInt("supervisor"));
//				dbUser.setCompanyroleid(rs.getInt("companyroleid"));
//				dbUser.setEmployeeKey(rs.getInt("employeekey"));
//				dbUser.setProfileText(dbUser.getFirstname());
//				dbUser.setDesktopUserid(rs.getInt("desktopuserid"));
			}
		}
		catch (Exception ex)
		{
			logger.error("error in LoginData---getUserLogin-->" , ex);
		}

		return dbUser;

	}
	public WebusersModel getUserLogin(WebusersModel objLogin)
	{
		WebusersModel dbUser=new WebusersModel();
		
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
	 	try{
	 		
	 		String sql="Select * from webusers w INNER JOIN companies c ON w.companyid=c.companyid" +
	 				" where isactive=1 and  username ='" + objLogin.getUsername() +"' and userpwd='" + objLogin.getUserpwd() + "' and c.companyid=" + objLogin.getCompanyid();
	 		rs = db.executeNonQuery(sql);	
	 		
	 		while (rs.next())
	 		{
	 			dbUser.setUserid(rs.getInt("userid"));
	 			dbUser.setUsername(rs.getString("username"));
	 			dbUser.setFirstname(rs.getString("firstname"));
	 			dbUser.setRoleid(rs.getInt("roleid"));
	 			dbUser.setCompanyid(objLogin.getCompanyid());
	 			dbUser.setCompanyName(rs.getString("companyname"));
	 			//dbUser.setAdminuser(rs.getString("isadmin")==null?"0":"1");
	 			dbUser.setUseremail(rs.getString("useremail")==null?"":rs.getString("useremail"));
	 			dbUser.setMergedDatabse(rs.getString("mergedDatabase")==null?"":rs.getString("mergedDatabase"));//temporary code
	 			dbUser.setAdminuser(rs.getString("isadmin"));
	 			dbUser.setSupervisor(rs.getInt("supervisor"));
	 			dbUser.setCompanyroleid(rs.getInt("companyroleid"));
	 			dbUser.setEmployeeKey(rs.getInt("employeekey"));
	 			dbUser.setProfileText(dbUser.getFirstname());
	 			dbUser.setDesktopUserid(rs.getInt("desktopuserid"));	 			
	 		}
	 		
	 		if(dbUser.getUserid()>0)
	 		{
	 			 sql="update webusers set lastlogindate=NOW() where userid=%d";
				 sql=sql.format(sql,dbUser.getUserid());
				 db.executeUpdateQuery(sql);
	 		}
	 		
	 	}
	 	catch (Exception ex) 
	 		{		 	  
	 		 logger.error("error in LoginData---getUserLogin-->" , ex);	
		 	 }
	 	finally{		 	  
		 	   return dbUser;
		 } 	 			
	}
	
	public WebusersModel changePassword(WebusersModel obj)
	{
		WebusersModel dbUser=new WebusersModel();
		
		 DBHandler db = new DBHandler();
		 String sql="";
	 	try{
	 		
	 		 sql="update webusers set userpwd='%s' where userid=%d";
			 sql=sql.format(sql,obj.getNewPassWrd(),obj.getUserid());
			 db.executeUpdateQuery(sql);
	 		
	 	}
	 	catch (Exception ex) 
	 		{		 	  
	 		 logger.error("error in LoginData---changePassword-->" , ex);	
		 	 }
	 	finally{		 	  
		 	   return dbUser;
		 } 	 			
	}
	
	public int isCompanyExsists(String companyName)
	{		
		DBHandler db = new DBHandler();
		ResultSet rs = null;
		int companyid=0;
	 	try
	 	{	 		
	 		String sql="Select * from companies where companyname ='" + companyName +"'";
	 		rs = db.executeNonQuery(sql);	
	 		
	 		while (rs.next())
	 		{
	 			companyid=rs.getInt("companyid");	 		
	 		}	 		
	 	}
	 	 catch (Exception ex) 
	 	{		 	  		 	   
	 		 logger.error("error in LoginData---isCompanyExsists-->" , ex);		
		} 
		
		return companyid;
	}
	
	public String getOldPassword(String pssword,int userID)
	{		
		DBHandler db = new DBHandler();
		ResultSet rs = null;
		String oldpass="";
	 	try
	 	{	 		
	 		String sql="Select * from webusers where userpwd ='" + pssword +"' and userid="+userID;
	 		rs = db.executeNonQuery(sql);	
	 		
	 		while (rs.next())
	 		{
	 			oldpass=rs.getString("userpwd");	 		
	 		}	 		
	 	}
	 	 catch (Exception ex) 
	 	{		 	  		 	   
	 		 logger.error("error in LoginData---getOldPassword-->" , ex);		
		} 
		
		return oldpass;
	}

}
