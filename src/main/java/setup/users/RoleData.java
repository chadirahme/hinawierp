package setup.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import layout.UserMenu;
import layout.WebMenu;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import db.DBHandler;

import HibernateUtilities.HibernateUtil;

public class RoleData 
{

	public List<MenuModel> getMenuList(int level)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 Transaction trns = null;		
		 Session session = HibernateUtil.getSessionFactory().openSession();	
		 
		 try{
			// Messagebox.show("begin");
	 		 trns = session.beginTransaction();
	 		 
	 		String sql="from WebMenu w where w.level= :level";	 			 		
	 		Query query = session.createQuery(sql);	 		
	 		query.setInteger("level", level);
	 		
	 		List<WebMenu> lstWebMenu=query.list();
	 		trns.commit();	
	 		for (WebMenu item : lstWebMenu) 
	 		{
	 			MenuModel obj=new MenuModel();
	 			obj.setMenuid(item.getMenuid());
	 			obj.setTitle(item.getTitle());	
	 			obj.setLevel(item.getLevel());
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
	
	public List<MenuModel> getDBMenuList(int level)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db = new DBHandler();
			ResultSet rs = null;
			String sqlQuery = null;
			try {				
				
				sqlQuery ="select * from webmenu w where w.level=0";	 
				rs = db.executeNonQuery(sqlQuery);			
				while (rs.next()) {
					MenuModel obj=new MenuModel();
					obj.setMenuid(rs.getInt("menuid"));
					obj.setTitle(rs.getString("title"));
					lstMenu.add(obj);	
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				System.out
						.println("error in Carrier---getDBMenuList() --for the query:");

			}
		   return lstMenu;
	}
	
}
