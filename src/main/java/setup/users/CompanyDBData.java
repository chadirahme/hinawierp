package setup.users;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

import db.DBHandler;

public class CompanyDBData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	public List<CompanyDBModel> getCompanyDBList()
	{
		 List<CompanyDBModel> lst=new ArrayList<CompanyDBModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 CompanyDBModel obj=new CompanyDBModel();
		 obj.setCompanyId(0);
		 obj.setCompanyName("Select");
		 lst.add(obj);
		 try
			{
			 String sql="Select * from companies ORDER BY companyName";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 obj=new CompanyDBModel();
				 obj.setCompanyId(rs.getInt("companyid"));
				 obj.setCompanyName(rs.getString("companyname"));
				 lst.add(obj);
			 }
			}
		  catch (Exception ex) 
			{
			  logger.error("error in CompanyDBData---getCompanyDBList-->" , ex);			
			}
		 return lst;
	}
	
	public List<CompanyDBModel> getCompanyDBAccessList(int companyid)
	{
		 List<CompanyDBModel> lst=new ArrayList<CompanyDBModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 CompanyDBModel obj=new CompanyDBModel();		
		 try
			{
			 String sql="Select * from companiesdb where companyid=" + companyid ;
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 obj=new CompanyDBModel();
				 obj.setCompanyId(rs.getInt("companyid"));
				 obj.setDbid(rs.getInt("dbid"));
				obj.setUserip(rs.getString("userip"));
				obj.setDbname(rs.getString("dbname"));
				obj.setDbuser(rs.getString("dbuser"));
				obj.setDbpwd(rs.getString("dbpwd"));
				obj.setDbtype(rs.getString("dbtype"));
				lst.add(obj);
			 }
			}
		  catch (Exception ex) 
			{
			  logger.error("error in CompanyDBData---getCompanyDBList-->" , ex);			
			}
		 return lst;
	}
	
	public void saveHostInfo(CompanyDBModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 try
		{
			 obj.setUserip(obj.getUserip().replace("\\", "\\\\"));
			 if(obj.getDbid()==0)
			 {
				 sql="insert into companiesdb(companyid,userip,dbname,dbuser,dbpwd,dbtype) values (" + obj.getCompanyId() + " , '" +obj.getUserip() + "' , '"
			   + obj.getDbname() + "' , '" + obj.getDbuser() + "' , '" +obj.getDbpwd() + "' , '" + obj.getDbtype() +"' )";
				 db.executeUpdateQuery(sql);				 				
			 }
			 else
			 {
				 sql="update companiesdb set userip='"+obj.getUserip()+"', dbname='" + obj.getDbname()+"' , dbuser='" + obj.getDbuser()+"' ,dbpwd='" + obj.getDbpwd() 
						 + "' where dbid=" + obj.getDbid();
				 db.executeUpdateQuery(sql);	
			 }
		}
		  catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---saveHostInfo-->" , ex);			
		}
	}
	
	public void deleteHostInfo(CompanyDBModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 try
		{
			 if(obj.getDbid()>0)
			 {
				 sql="delete from companiesdb where dbid=" + obj.getDbid();
				 db.executeUpdateQuery(sql);				 				 
			 }
		}
		  catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---deleteHostInfo-->" , ex);			
		}
	}
	
	public List<CompanyDBModel> getCompanyInfoList()
	{
		 List<CompanyDBModel> lst=new ArrayList<CompanyDBModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 CompanyDBModel obj=new CompanyDBModel();		
		 try
			{
			 String sql="Select * from companies ORDER BY companyName ";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 obj=new CompanyDBModel();
				 obj.setCompanyId(rs.getInt("companyid"));
				 obj.setCompanyName(rs.getString("companyname"));
				 obj.setContactname(rs.getString("contactname"));
				 obj.setTelephone(rs.getString("telephone"));
				 obj.setMobile(rs.getString("mobile"));
				 obj.setEmail(rs.getString("email"));		
				 obj.setMaxNoUsers(rs.getInt("maxusers"));
				 obj.setMergedDatabse(rs.getString("mergedDatabase")==null?"":rs.getString("mergedDatabase"));//temporary code
				 lst.add(obj);
			 }
			}
		  catch (Exception ex) 
			{
			  logger.error("error in CompanyDBData---getCompanyInfoList-->" , ex);			
			}
		 return lst;
	}
	
	//Company setup
	public void saveCompanyInfo(CompanyDBModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 try
		{
			 if(obj.getCompanyId()==0)//temporary code merged database need to be removed later. added on 7/30/2014
			 {
				 sql="insert into companies(companyname,contactname,telephone,mobile,email,maxusers,mergedDatabase) values ('" + obj.getCompanyName() + "' , '" +obj.getContactname() + "' , '"
			   + obj.getTelephone() + "' , '" + obj.getMobile() + "' , '" +obj.getEmail() + "' , " + obj.getMaxNoUsers()  + ",'"+obj.getMergedDatabse() +"')";
				 int compId=db.executeUpdateQuery(sql);				 
				 //if new company add new role as default
				 if(compId>0)
				 {
					 sql="insert into companyroles(companyid,rolename) values ('" + compId + "' , '" +"Admin" + "') ";
					 int roleId=db.executeUpdateQuery(sql);
					 //add Admin user for this new company
					 sql="insert into webusers(username,userpwd,firstname,lastname,useremail,usermobile,roleid,companyid,supervisor,companyroleid,isadmin,isactive) " +
			 				  "values('admin','123456','admin','admin','','','%s','%s','0','%s','1','1')";
					 sql=sql.format(sql,2,compId,roleId);
					 db.executeUpdateQuery(sql);	
					 //add Admin menu access to this user
					 sql="insert into rolescredentials(createdby, companyroleid,menuid,parentId) " +
					 		"values('5','%s','%s','272')";
					 //sql=sql.format(sql,roleId,272,272);
					 db.executeUpdateQuery(sql.format(sql,roleId,272));
					 db.executeUpdateQuery(sql.format(sql,roleId,273));
					 db.executeUpdateQuery(sql.format(sql,roleId,274));
					 db.executeUpdateQuery(sql.format(sql,roleId,275));
					 db.executeUpdateQuery(sql.format(sql,roleId,276));
					 db.executeUpdateQuery(sql.format(sql,roleId,277));	
					 db.executeUpdateQuery(sql.format(sql,roleId,285));//User Activity	
					 
					 //add companysetting
					 sql="insert into companysettings(companyid, hidecalculate,hideservice,projecttype,datetype,hideovertime,autoapprove) " +
						 		"values('%s','0','0','3','1','0','0')";
					 db.executeUpdateQuery(sql.format(sql,compId));	
			    }				 				 
			 }
			 else
			 {
				 sql="update companies set companyname='"+obj.getCompanyName()+"', contactname='" + obj.getContactname()+"' , telephone='" + obj.getTelephone()+"' ,mobile='" + obj.getMobile() 
						 + "' , email='" + obj.getEmail() +"' , maxusers="+obj.getMaxNoUsers() +",mergedDatabase='"+obj.getMergedDatabse()+"' where companyid=" + obj.getCompanyId();
				 db.executeUpdateQuery(sql);	
			 }
		}
		  catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---saveCompanyInfo-->" , ex);			
		}
	}
	public void deleteCompanyInfo(CompanyDBModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 try
		{
			 if(obj.getCompanyId()>0)
			 {
				 sql="delete from companies where companyid=" + obj.getCompanyId();
				 db.executeUpdateQuery(sql);				 				 
			 }
		}
		  catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---deleteCompanyInfo-->" , ex);			
		}
	}
	
	//User Setup
	public List<WebusersModel> getUsersList(int companyID)
	{
		List<WebusersModel> lstDataModel=new ArrayList<WebusersModel>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {
			 String sql="Select * from webusers where companyid="+companyID;
		    rs = db.executeNonQuery(sql);	
		    while(rs.next())
		    {
		    	WebusersModel item=new WebusersModel();
		    	item.setCompanyid(companyID);
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
				item.setAdminuser(rs.getString("isadmin"));
				item.setSupervisor(rs.getInt("supervisor"));
				item.setCompanyroleid(rs.getInt("companyroleid"));			
				lstDataModel.add(item);			
		    }
			 
		 }
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in CompanyDBData---getUsersList-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lstDataModel;
		 }
	}
	
	public void saveWebUser(WebusersModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 try
		{
			 if(obj.getUserid()==0)
			 {
				 sql="insert into webusers(username,userpwd,firstname,lastname,useremail,usermobile,roleid,companyid) " +
				 				  "values('%s','%s','%s','%s','%s','%s','%s','%s')";
				 sql=sql.format(sql, obj.getUsername(),obj.getUserpwd(),obj.getFirstname(),obj.getLastname(),obj.getUseremail(),obj.getUsermobile(),2,obj.getCompanyid());
				 db.executeUpdateQuery(sql);				 				
			 }
			 else
			 {
				 sql="update webusers set username='%s' , userpwd='%s' ,firstname='%s',lastname='%s',useremail='%s',usermobile='%s' where userid=%d";
				 sql=sql.format(sql, obj.getUsername(),obj.getUserpwd(),obj.getFirstname(),obj.getLastname(),obj.getUseremail(),obj.getUsermobile(),obj.getUserid());
				 db.executeUpdateQuery(sql);
			 }
			 
		}
		  catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---saveWebUser-->" , ex);			
		}
	}
	public void deleteWebUser(WebusersModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 try
		{
			 if(obj.getUserid()>0)
			 {
				 sql="delete from webusers where userid=" + obj.getUserid();
				 db.executeUpdateQuery(sql);				 				 
			 }
		}
		  catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---deleteWebUser-->" , ex);			
		}
	}
	
	//Company File
	public List<MenuModel> getMainMenuList()
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 String sql="SELECT w.menuid,w.title,artitle,w.menuorder FROM webmenu w where w.parentid=0  ORDER BY menuorder";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setMenuorder(rs.getInt("menuorder"));				 				 			
		 			obj.setArtitle(rs.getString("artitle"));
		 			lstMenu.add(obj);		 			
			 }
			 						 
			}
		  catch (Exception ex) 
			{
			  logger.error("error in CompanyDBData---getMainMenuList-->" , ex);			
			}
		 return lstMenu;
	}
	
	public List<Integer> getCompanyFile(int companyID)
	{
		List<Integer> lstMenuIds=new ArrayList<Integer>();
		try
		{
			DBHandler db=new DBHandler();
			 ResultSet rs=null;
			String sql="select menuid from companyfile where companyid=" + companyID;
			rs=db.executeNonQuery(sql);
			while(rs.next())
			{
				lstMenuIds.add(rs.getInt("menuid"));
			}
		}
		catch (Exception ex) 
		{
		  logger.error("error in CompanyDBData---getCompanyFile-->" , ex);			
		}
		return lstMenuIds;
	}
	public void saveCompanyFile(int companyId,List<Integer> lstMenuIds)
	{
		try
		{
			DBHandler db=new DBHandler();
			String sql="delete from companyfile where companyid=" + companyId;
			db.executeUpdateQuery(sql);
			
			for (Integer menuID : lstMenuIds) 
			{
				sql="insert into companyfile(companyid,menuid) values('%s','%s')";
				sql=sql.format(sql, companyId,menuID);
				db.executeUpdateQuery(sql);
			}
		}
		catch (Exception ex) 
		{
		  logger.error("error in CompanyDBData---saveCompanyFile-->" , ex);			
		}
	}
	
		
}
