package layout;

import home.MailClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zul.Messagebox;

import db.DBHandler;
import db.SQLDBHandler;


import HibernateUtilities.HibernateUtil;

public class MenuData {

	private Logger logger = Logger.getLogger(this.getClass());
	public void getMenuData()
	{
		 		String connectionUrl = "jdbc:sqlserver://chadi-pc\\SQLEXPRESS;" +
		         "databaseName=SMDB;user=sa;password=123456";
				 	Connection con = null;
			      Statement stmt = null;
	 		   ResultSet rs = null;
	 		  try {
		          // Establish the connection.
		          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		          con = DriverManager.getConnection(connectionUrl);

		          // Create and execute an SQL statement that returns some data.
		          String SQL = "SELECT TOP 10 * FROM Grades";
		          stmt = con.createStatement();
		          rs = stmt.executeQuery(SQL);

		          // Iterate through the data in the result set and display it.
		          while (rs.next()) {
		             System.out.println(rs.getString("GradeID") + " " + rs.getString("EnGradeName"));
		          }
		       }

		       // Handle any errors that may have occurred.
		       catch (Exception e) {
		          e.printStackTrace();
		       }
		       finally {
		          if (rs != null) try { rs.close(); } catch(Exception e) {}
		          if (stmt != null) try { stmt.close(); } catch(Exception e) {}
		          if (con != null) try { con.close(); } catch(Exception e) {}
		       }			
	}
	
	public void checkSQLServerConnection()
	{
		try
		{
			SQLDBHandler db=new SQLDBHandler("hinawi_hr");
			ResultSet rs = null;
			rs=db.executeNonQuery("Select 1");
			boolean isConnect=false;
			while(rs.next())
			{
				isConnect=true;
				logger.info("connect to sql server..");
			}
			
			if(isConnect==false)
				sendErrorEmail("Connect to SQLServer failed..");
		}
		 catch (Exception ex) 
		{
		   logger.error("error in MenuData---checkSQLServerConnection-->" , ex);	
		   sendErrorEmail(ex.getMessage());
		}
	}
	private void sendErrorEmail(String message)
	{
		try
		{
			MailClient mc = new MailClient();
			String[] to ={"eng.chadi@gmail.com"};
			String subject="SQL connection failed";
			String[] cc ={null};
			String[] bcc =null;
			mc.sendMochaMail(to,cc,bcc, subject, message,true,null,false,"error","");			
			//mc.sendGmailMail("", "", to, subject, message, null);
			logger.info("Error email is send it..");
		}
		catch (Exception ex) 
		{
		   logger.error("error in MenuData---sendErrorEmail-->" , ex);			  
		}
	}
	
	public List<MenuModel> getMenuList(int userid,boolean isArrangeMenu)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 String sql="SELECT m.menuid,w.title,artitle,m.menuorder,href FROM usermenu m INNER JOIN webmenu w ON m.menuid=w.menuid where m.level=0 and m.userid=" + userid + " ORDER BY menuorder";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setMenuorder(rs.getInt("menuorder"));		
		 			obj.setHref(rs.getString("href"));
		 			obj.setSclassName("defaultmenuitem");
		 			obj.setArtitle(rs.getString("artitle"));
		 			lstMenu.add(obj);		 			
			 }
			 
			 /*
			  if(isArrangeMenu==false)
		 		{
		 		MenuModel obj=new MenuModel();
		 		obj.setMenuid(-1);
	 			obj.setTitle("Arrange Menu");
	 			lstMenu.add(obj);	 		 
		 		}
			 */
			 
			}
		  catch (Exception ex) 
			{
				//logger.error("error in QuotationData---insertQuotation-->" , ex);
			  Messagebox.show(ex.getMessage());
			}
		 return lstMenu;
	}
	
	public List<MenuModel> getMenuCompanyRoleList(int companyroleid)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 String sql="SELECT DISTINCT m.menuid,m.title,artitle,m.menuorder,href ,m.level" +
			 		" FROM webmenu m INNER JOIN rolescredentials r ON m.menuid=r.parentid where m.level=0 and r.companyroleid=" + companyroleid + " ORDER BY menuorder";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setMenuorder(rs.getInt("menuorder"));		
		 			obj.setHref(rs.getString("href"));
		 			obj.setSclassName("defaultmenuitem");
		 			obj.setArtitle(rs.getString("artitle"));
		 			lstMenu.add(obj);		 			
			 }
			 			 
			}
		  catch (Exception ex) 
			{
			  logger.error("error in MenuData---getMenuCompanyRoleList-->" , ex);
			  Messagebox.show(ex.getMessage());
			}
		 return lstMenu;
	}
	
	
	public List<MenuModel> getMenuList1(int userid,boolean isArrangeMenu)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 Transaction trns = null;		
		 Session session = HibernateUtil.getSessionFactory().openSession();	
		 
		 try{
			// Messagebox.show("begin");
	 		 trns = session.beginTransaction();
	 		 
	 		String sql="";//"from WebMenu w where w.level=0";
	 		sql="from UserMenu where level=0 and userid= :userid order by menuorder";	
	 		
	 		Query query = session.createQuery(sql);
	 		query.setInteger("userid", userid); 
	 		
	 		List<UserMenu> lstUserMenu=query.list(); //session.createQuery(query).list();
	 		trns.commit();	
	 		for (UserMenu item : lstUserMenu) {
	 			MenuModel obj=new MenuModel();
	 			obj.setMenuid(item.getWebmenu().getMenuid());
	 			obj.setTitle(item.getWebmenu().getTitle());
	 			obj.setMenuorder(item.getMenuorder());
	 			//obj.setChildren(getSubMenuList(webMenu.getMenuid()));
	 			// System.out.println(obj.getTitle());
	 			lstMenu.add(obj);
					 			
			}
	 		
	 		/*
	 		
	 		List<WebMenu> lstWebMenu= session.createQuery(query).list();
	 		
	 		for (WebMenu item : lstWebMenu) {
				//Messagebox.show(item.getTitle());
			}
	 		
	 		 List<WebMenu>  lstData = (List<WebMenu>)session.createQuery("from WebMenu w where w.level=0")		 		 
	 		 .list();	 	
	 		 
	 		// List<WebMenu>  lstData= (List<WebMenu>)session.createSQLQuery("select w.menuid,w.title from WebMenu w inner join UserMenu u on w.menuid=u.menuid where w.level=0")
	 		//.list();
	 		 
	 		 trns.commit();	
	 		 
	 		 for (WebMenu webMenu : lstData) {
	 			MenuModel obj=new MenuModel();
	 			obj.setMenuid(webMenu.getMenuid());
	 			obj.setTitle(webMenu.getTitle());
	 			//obj.setChildren(getSubMenuList(webMenu.getMenuid()));
	 			 System.out.println(obj.getTitle());
	 			lstMenu.add(obj);
			}
	 		 
	 		 */
	 		if(isArrangeMenu==false)
	 		{
	 		MenuModel obj=new MenuModel();
	 		obj.setMenuid(-1);
 			obj.setTitle("Arrange Menu");
 			lstMenu.add(obj);	 		 
	 		}
	 		
		 }catch (RuntimeException e) {
		 	   if(trns != null){
			 	    trns.rollback();
			 	   }
			 	   e.printStackTrace();
			 	  } finally{
			 	   session.flush();
			 	   session.close();
			 	   return lstMenu;
			 } 		 			
	}
	
	public List<MenuModel> getSubMenuList(int parentid,int level,int companyroleid)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 //String sql="SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentid +" order by menuorder";
			 String sql="SELECT m.menuid,title,artitle,href,level FROM webmenu m INNER JOIN rolescredentials r ON m.menuid = r.menuid" +
				 		" where level= " + level + " and  m.parentid=" + parentid + " AND r.companyroleid = " + companyroleid + " order by menuorder";
				 
			
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setHref(rs.getString("href"));
		 			obj.setLevel(rs.getInt("level"));		
		 			obj.setArtitle(rs.getString("artitle"));
		 			obj.setSclassName("defaultMenu");
		 			lstMenu.add(obj);
			 }
			 			 			 
			}
		  catch (Exception ex) 
			{
				//logger.error("error in QuotationData---insertQuotation-->" , ex);
			  Messagebox.show(ex.getMessage());
			}
		 return lstMenu;
	}
	
	public List<MenuModel> getSideBarSubMenuList(int parentid,int level,int companyroleid)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 //String sql="SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentid +" order by menuorder";
			 String sql="SELECT m.menuid,title,artitle,href,level FROM webmenu m INNER JOIN rolescredentials r ON m.menuid = r.menuid" +
			 		" where level= " + level + " and  m.parentid=" + parentid + " AND r.companyroleid = " + companyroleid + " order by menuorder";
			 
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setHref(rs.getString("href"));
		 			obj.setLevel(rs.getInt("level"));
		 			obj.setArtitle(rs.getString("artitle"));
		 			obj.setSclassName("defaultMenu");	
		 			obj.setChildren(getSubMenuList(obj.getMenuid(),2,companyroleid));
		 			lstMenu.add(obj);
			 }
			 			 			 
			}
		  catch (Exception ex) 
			{
				//logger.error("error in QuotationData---insertQuotation-->" , ex);
			  Messagebox.show(ex.getMessage());
			}
		 return lstMenu;
	}
	
	
	public List<MenuModel> getSubMenuList1(int parentid)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 Transaction trns = null;		
		 Session session = HibernateUtil.getSessionFactory().openSession();	
		
		
		 try{
	 		 trns = session.beginTransaction();
	 		 List<WebMenu>  lstData = (List<WebMenu>)session.createQuery("from WebMenu where parentid= :parentid")
	 				 .setInteger("parentid", parentid)
	 		 .list();	 		
	 		 trns.commit();	
	 		 
	 		 for (WebMenu webMenu : lstData) {
	 			MenuModel obj=new MenuModel();
	 			obj.setMenuid(webMenu.getMenuid());
	 			obj.setTitle(webMenu.getTitle());
	 			 System.out.println(obj.getTitle());
	 			lstMenu.add(obj);
			}
	 		 	 		 
	 		 
		 }catch (RuntimeException e) {
		 	   if(trns != null){
			 	    trns.rollback();
			 	   }
			 	   e.printStackTrace();
			 	  } finally{
			 	   session.flush();
			 	   session.close();
			 	   return lstMenu;
			 } 		 			
	}
	
	public void arrangeUserMenu(int firstMenuId,int secondMenuId,int userid)
	{
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
		 {
			 int firstMenuOrder=0;
			 int secondMenuOrder=0;
			 
			 int firstusermenuid=0;
			 int secondusermenuid=0;
			 
			 String sql="select usermenuid,menuorder from usermenu where menuid = "+firstMenuId + " and userid= "+userid;
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 firstusermenuid=rs.getInt("usermenuid");
				 firstMenuOrder=rs.getInt("menuorder");				 
			 }
			 sql="select usermenuid,menuorder from usermenu where menuid = "+secondMenuId + " and userid= "+userid;
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 secondusermenuid=rs.getInt("usermenuid");
				 secondMenuOrder=rs.getInt("menuorder");				 
			 }
			 
			 sql="update usermenu set menuorder=" + secondMenuOrder + " where usermenuid=" + firstusermenuid;
			 db.executeUpdateQuery(sql);
			 
			 sql="update usermenu set menuorder=" + firstMenuOrder + " where usermenuid=" + secondusermenuid;
			 db.executeUpdateQuery(sql);
			 
		 }
		 catch (Exception ex) 
			{
				//logger.error("error in QuotationData---insertQuotation-->" , ex);
			  Messagebox.show(ex.getMessage());
			}
	}
	public void arrangeUserMenu1(int firstMenuId,int secondMenuId,int userid)
	{
		 Transaction trns = null;		
		 Session session = HibernateUtil.getSessionFactory().openSession();	
		 try{
			 int firstMenuOrder=0;
			 int secondMenuOrder=0;
			 
	 		trns = session.beginTransaction();
	 		String sql="from UserMenu where menuid = :menuid and userid= :userid";
	 		Query query = session.createQuery(sql);
	 		query.setInteger("menuid", firstMenuId);
	 		query.setInteger("userid", userid);
	 		UserMenu objFirstMenu=(UserMenu)query.uniqueResult();
	 		firstMenuOrder=objFirstMenu.getMenuorder();
	 		
	 		 sql="from UserMenu where menuid = :menuid and userid= :userid";
	 		 query = session.createQuery(sql);
	 		 query.setInteger("menuid", secondMenuId);
	 		query.setInteger("userid", userid);	 		
		 	 UserMenu objSecondMenu=(UserMenu)query.uniqueResult();
		 	 secondMenuOrder=objSecondMenu.getMenuorder();
		 	 
		 	//Messagebox.show(String.valueOf(firstMenuOrder));
		 	//Messagebox.show(String.valueOf(secondMenuOrder));
		 	
		 	objFirstMenu.setMenuorder(secondMenuOrder);
		 	objSecondMenu.setMenuorder(firstMenuOrder);
		 	
		 	session.saveOrUpdate(objFirstMenu);
			session.saveOrUpdate(objSecondMenu);
	 		session.getTransaction().commit();
	 		
		 }
		 catch (RuntimeException e) {
		 	   if(trns != null){
			 	    trns.rollback();
			 	   }
			 	   e.printStackTrace();
			 	  } finally{
			 	   session.flush();
			 	   session.close();			 	  
			 } 		
	}
}
