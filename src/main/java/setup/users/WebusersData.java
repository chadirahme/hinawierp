package setup.users;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zul.Messagebox;

import db.DBHandler;

import HibernateUtilities.HibernateUtil;

public class WebusersData {

	public List<WebusersModel> getUsersList()
	{
		List<WebusersModel> lstDataModel=new ArrayList<WebusersModel>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {
			 String sql="Select * from Webusers ORDER BY username ";
		    rs = db.executeNonQuery(sql);	
		    while(rs.next())
		    {
		    	WebusersModel item=new WebusersModel();;
		    	item.setUserid(rs.getInt("userid"));
				item.setUsername(rs.getString("username"));
				item.setUserpwd(rs.getString("userpwd"));
				item.setFirstname(rs.getString("firstname"));
				item.setLastname(rs.getString("lastname"));
				item.setUseremail(rs.getString("useremail"));
				item.setUsermobile(rs.getString("usermobile"));
				item.setDbname(rs.getString("dbname"));
				item.setUserip(rs.getString("userip"));
				item.setDbuser(rs.getString("dbuser"));
				item.setDbpwd(rs.getString("dbpwd"));
				lstDataModel.add(item);			
		    }
			 
		 }catch (RuntimeException e) 
	 		{		 	  
		 	   e.printStackTrace();
		 	   Messagebox.show(e.getMessage());
		 	  }
		 finally
		 {
			 return lstDataModel;
		 }
	}
	
	public List<WebusersModel> getUsersList1()
	{
		List<WebusersModel> lstDataModel=new ArrayList<WebusersModel>();
		Transaction trns = null;		
	 	Session session = HibernateUtil.getSessionFactory().openSession();		
	 	try{
	 		 trns = session.beginTransaction();
	 		 List<Webusers>  lstData = (List<Webusers>)session.createQuery("from Webusers")		 		 
	 		 .list();	 		
	 		 trns.commit();	
	 		 	 		
	 		WebusersModel item;
	 		 for (Webusers obj : lstData) {
				item=new WebusersModel();
				item.setUserid(obj.getUserid());
				item.setUsername(obj.getUsername());
				item.setUserpwd(obj.getUserpwd());
				item.setFirstname(obj.getFirstname());
				item.setLastname(obj.getLastname());
				item.setUseremail(obj.getUseremail());
				item.setUsermobile(obj.getUsermobile());
				item.setDbname(obj.getDbname());
				item.setUserip(obj.getUserip());
				item.setDbuser(obj.getDbuser());
				item.setDbpwd(obj.getDbpwd());
				lstDataModel.add(item);				
			}
	 	}
	 	catch (RuntimeException e) {
	 	   if(trns != null){
	 	    trns.rollback();
	 	   }
	 	   e.printStackTrace();
	 	  } finally{
	 	   session.flush();
	 	   session.close();
	 	   return lstDataModel;
	 } 
	}
	
	public String saveWebUser(WebusersModel obj)
	{
		String result="";
		Transaction trns = null;		
	 	Session session = HibernateUtil.getSessionFactory().openSession();
	 	try
	 	{
	 		trns = session.beginTransaction();
	 		Webusers item=new Webusers();
	 		item.setUserid(obj.getUserid());
			item.setUsername(obj.getUsername());
			item.setUserpwd(obj.getUserpwd());
			item.setFirstname(obj.getFirstname());
			item.setLastname(obj.getLastname());
			item.setUseremail(obj.getUseremail());
			item.setUsermobile(obj.getUsermobile());
			item.setDbname(obj.getDbname());
			item.setUserip(obj.getUserip());
			item.setDbuser(obj.getDbuser());
			item.setDbpwd(obj.getDbpwd());
	 		//session.save(item);
	 		session.saveOrUpdate(item);
	 		session.getTransaction().commit();
	 	}
		catch (RuntimeException e) 
		{
		 	   if(trns != null)
		 	   {
		 	    trns.rollback();
		 	   }
		 	   e.printStackTrace();
		 	   result=e.getMessage();
		} 
	 	 finally{
		 	   session.flush();
		 	   session.close();
		 	   return result;
		 } 		
	}
	
	public String deleteWebUser(WebusersModel obj)
	{
		String result="";
		Transaction trns = null;		
		Session session=null;
	 	try
	 	{
	 		//Object objUser=session.load(Webusers.class, obj.getUserid());
	 		 String query = "delete from Webusers where userid = :userid";
	 		 session = HibernateUtil.getSessionFactory().openSession();
	 		trns=session.beginTransaction();	 		 
	 		session.createQuery(query).setInteger("userid", obj.getUserid()).executeUpdate();
	 		trns.commit();	 	    
	 		//session.delete(objUser);
	 	}
		catch (RuntimeException e) 
		{
		 	   if(trns != null)
		 	   {
		 	    trns.rollback();
		 	   }
		 	   e.printStackTrace();
		 	   result=e.getMessage();
		} 
	 	 finally
	 	 {
		 	   session.flush();
		 	   session.close();
		 	   return result;
		 } 		
	}
}
