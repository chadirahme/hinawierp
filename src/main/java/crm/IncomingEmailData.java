package crm;

import hr.HRQueries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.CompanyDBModel;
import model.CustomerModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class IncomingEmailData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	WebusersModel dbUser=null;
	SQLDBHandler db;
	public IncomingEmailData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HRQueries query=new HRQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in IncomingEmailData---Init-->" , ex);
		}
	}
	
	public WebusersModel getUserEmail(int userid)
	{				
		WebusersModel obj=new WebusersModel();
		try
		{
			String sql="SELECT * FROM webusers WHERE userid=" + userid;
			DBHandler db=new DBHandler();	
			ResultSet rs = null;
			rs=db.executeNonQuery(sql);
			while(rs.next())
			{
				obj.setUseremail(rs.getString("useremail")==null?"":rs.getString("useremail"));
				obj.setUserEmailPassword(rs.getString("useremailpassword")==null?"":rs.getString("useremailpassword"));
				obj.setEmailhost(rs.getString("emailhost")==null?"":rs.getString("emailhost"));
			}
		}
		 catch (Exception ex) 
	 	 {		 	  
		  logger.error("error in IncomingEmailData---getUserEmail-->" , ex);			 	  
		 }
		return obj;
	}
	
	public String getCompanyHostEmail(int comp_key)
	{
		String host="";
		try
		{
			ResultSet rs = null;
			String query= "SELECT emailhost FROM compsetup where comp_key=" + comp_key;
			rs=db.executeNonQuery(query);
			while(rs.next())
			{
				host=rs.getString("emailhost")==null?"":rs.getString("emailhost");
			}
			
		}
		catch (Exception ex) 
	 	{		 	  
		  logger.error("error in IncomingEmailData---getCompanyHostEmail-->" , ex);			 	  
		}
		return host;
	}
	
	public CustomerModel getCustomerFromEmail(String email)
	{
		CustomerModel obj=new CustomerModel();
		obj.setCustkey(0);
		obj.setFullName("Unknown");
		obj.setCompanyName("Unknown");		
		
		if(true)
			return obj;
		
		try
		{
			ResultSet rs = null;
			String query= "SELECT * FROM Customer where email='" + email+"'";
			rs=db.executeNonQuery(query);
			while(rs.next())
			{
				obj=new CustomerModel();
				obj.setCustkey(rs.getInt("Cust_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setAltphone(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
			}
			
		}
		catch (Exception ex) 
	 	{		 	  
		  logger.error("error in IncomingEmailData---getCustomerFromEmail-->" , ex);			 	  
		}
		return obj;
	}
	
	public List<CustomerModel> getCustomersList()
	{
		List<CustomerModel> lst=new ArrayList<CustomerModel>();
		CustomerModel obj=new CustomerModel();
		obj.setCustkey(0);
		obj.setFullName("Select");
		obj.setCompanyName("");	
		lst.add(obj);
		try
		{
			ResultSet rs = null;
			String query= "SELECT * FROM Customer Order by Replace(FullName,':',':'),PriorityID Desc";
			rs=db.executeNonQuery(query);
			while(rs.next())
			{
				obj=new CustomerModel();
				obj.setCustkey(rs.getInt("Cust_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setAltphone(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				
				lst.add(obj);
				
			}
			
		}
		catch (Exception ex) 
	 	{		 	  
		  logger.error("error in IncomingEmailData---getCustomersList-->" , ex);			 	  
		}
		return lst;
	}
	
	public void updateCustomerEmail(int customerKey,String email)
	{
		try
		{
			String query="update Customer set email='"+email + "' where Cust_key=" + customerKey;
			db.executeUpdateQuery(query);
		}
		catch (Exception ex) 
	 	{		 	  
		  logger.error("error in IncomingEmailData---updateCustomerEmail-->" , ex);			 	  
		}
	}
}
