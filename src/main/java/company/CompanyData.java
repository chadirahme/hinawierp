package company;

import hr.model.CompanyModel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.CompanyDBModel;
import model.CompanyRoleModel;
import model.CompanySettingsModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class CompanyData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	CompanyQueries query;
	public CompanyData()
	{
		query=new CompanyQueries();
	}
	
	public List<EmployeeModel> getSupervisorList(CompanyDBModel objDB )
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		try
		{
			ResultSet rs = null;
			SQLDBHandler dbSQL=new SQLDBHandler(objDB);
			rs=dbSQL.executeNonQuery(query.getSupervisorListQuery());
			EmployeeModel obj=new EmployeeModel();
			obj.setEmployeeKey(0);
			obj.setFullName("Select");
			lst.add(obj);
			
			while(rs.next())
			{
				obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EMP_KEY"));
				obj.setFullName(rs.getString("ENGLISH_FULL"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
 		{		 	  
		 logger.error("error in CompanyData---getSupervisorList-->" , ex);			 	  
	 	}
		return lst;			
	}
	
	public List<EmployeeModel> getEmployeeList(CompanyDBModel objDB)
	{
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		try
		{
			ResultSet rs = null;
			SQLDBHandler dbSQL=new SQLDBHandler(objDB);
			rs=dbSQL.executeNonQuery(query.getEmployeeListQuery());
			EmployeeModel obj=new EmployeeModel();
			obj.setEmployeeKey(0);
			obj.setFullName("Select");
			lst.add(obj);
			
			while(rs.next())
			{
				obj=new EmployeeModel();
				obj.setEmployeeKey(rs.getInt("EmployeeKey"));
				obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		}
		catch (Exception ex) 
 		{		 	  
		 logger.error("error in CompanyData---getEmployeeList-->" , ex);			 	  
	 	}
		return lst;			
	}
	
	@SuppressWarnings("finally")
	public List<WebusersModel> getUsersList(int companyID)
	{
		List<WebusersModel> lstDataModel=new ArrayList<WebusersModel>();
		 DBHandler db = new DBHandler();
		 ResultSet rs = null;
		 try
		 {			
		    rs = db.executeNonQuery(query.getCompanyUsersQuery(companyID));		    
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
				item.setRoleName(rs.getString("rolename"));
				item.setEmployeeKey(rs.getInt("employeekey"));
				item.setIsactive(rs.getBoolean("isactive"));
				item.setUserEmailPassword(rs.getString("useremailpassword"));
				item.setEmailhost(rs.getString("emailhost"));
				lstDataModel.add(item);			
		    }
			 
		 }
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in CompanyData---getUsersList-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return lstDataModel;
		 }
	}
	
	public int getMaxUsersAllowed(int companyid)
	{
		int maxUsers=0;				 
		try
		{
			String sql="SELECT IFNULL(maxusers,0) AS 'maxusers' FROM companies WHERE companyid=" + companyid;
			DBHandler db=new DBHandler();	
			ResultSet rs = null;
			rs=db.executeNonQuery(sql);
			while(rs.next())
			{
				maxUsers=rs.getInt("maxusers");
			}
		}
		 catch (Exception ex) 
	 	 {		 	  
		  logger.error("error in CompanyData---getMaxUsersAllowed-->" , ex);			 	  
		 }
		return maxUsers;
	}
	@SuppressWarnings("static-access")
	public int saveWebUser(WebusersModel obj)
	{
		 DBHandler db=new DBHandler();
		 String sql="";
		 int userID=0;
		 try
		{
			 if(obj.getUserid()==0)
			 {
				 sql="insert into webusers(username,userpwd,firstname,lastname,useremail,usermobile,roleid,companyid,supervisor,companyroleid,isadmin,employeekey,useremailpassword,emailhost,isactive) " +
				 				  "values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";
				 sql=sql.format(sql, obj.getUsername(),obj.getUserpwd(),obj.getFirstname(),obj.getLastname(),obj.getUseremail(),obj.getUsermobile(),2,obj.getCompanyid(),obj.getSupervisor(),obj.getCompanyroleid(),"0",obj.getEmployeeKey(),obj.getUserEmailPassword(),obj.getEmailhost(),"1");
				 userID= db.executeUpdateQuery(sql);					
			 }
			 else
			 {
				 sql="update webusers set username='%s' , userpwd='%s' ,firstname='%s',lastname='%s',useremail='%s',usermobile='%s',supervisor=%d ,companyroleid=%d ,employeekey=%d,useremailpassword='%s',emailhost='%s'  where userid=%d";
				 sql=sql.format(sql, obj.getUsername(),obj.getUserpwd(),obj.getFirstname(),obj.getLastname(),obj.getUseremail(),obj.getUsermobile(),obj.getSupervisor() , obj.getCompanyroleid(),obj.getEmployeeKey(),obj.getUserEmailPassword(),obj.getEmailhost() ,obj.getUserid());
				 db.executeUpdateQuery(sql);
				 userID= obj.getUserid();
			 }			
		}
		  catch (Exception ex) 
		  {
			  userID=-1;
			  logger.error("error in CompanyDBData---saveWebUser-->" , ex);			
		  }
		 return userID;
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
	public void updateUserStatus(int userId,int isactive)
	{
		try
		{
		 DBHandler db=new DBHandler();			
		 String sql="update webusers set isactive=" + isactive + " where userid=" + userId;
		 db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			  logger.error("error in CompanyDBData---updateUserStatus-->" , ex);			
		}
	}
	
	public List<MenuModel> getUserMenuList(int userID)
	{
		List<MenuModel> lst=new ArrayList<MenuModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getUserMenuQuery(userID));
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
				 obj.setMenuid(rs.getInt("menuid"));
				 obj.setTitle(rs.getString("title"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getUserMenuList-->" , ex);			
		}
		return lst;
	}
	
	public void deleteUserMenuAccess(int userId)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.deleteUserMenuQuery(userId));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---deleteUserMenuAccess-->" , ex);			
		}
	}
	
	public void addUserMenuAccess(int userId,List<MenuModel> lstMainUserMenu)
	{
		try
		{
		  DBHandler db = new DBHandler();
		  db.executeUpdateQuery(query.deleteUserMenuQuery(userId));
		  for (MenuModel item : lstMainUserMenu) 
		  {
			if(item.isCanView())
			db.executeUpdateQuery(query.addUserMenuQuery(userId, item.getMenuid()));
		  }
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---addUserMenuAccess-->" , ex);			
		}
	}
	
	public void addParentUserMenuAccess(int userId,MenuModel objMenu)
	{
		try
		{
		  DBHandler db = new DBHandler();	
		  db.executeUpdateQuery(query.addUserMenuQuery(userId, objMenu.getMenuid()));
		 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---addParentUserMenuAccess-->" , ex);			
		}
	}
	
	public void addFullUserMenuAccess(int userId,List<MenuModel> lstMainUserMenu)
	{
		try
		{
		  DBHandler db = new DBHandler();		  
		  for (MenuModel item : lstMainUserMenu) 
		  {
			if(item.isCanView() || item.isCanModify() || item.isCanDelete())
			db.executeUpdateQuery(query.addFullUserAccessMenuQuery(userId, item));
		  }
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---addFullUserMenuAccess-->" , ex);			
		}
	}
	
	public CompanyDBModel getCompanyDataBase(int companyId)
	{
		CompanyDBModel obj=new CompanyDBModel();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getDBCompany(companyId));
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
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getCompanyDataBase-->" , ex);			
		}
		
		return obj;
	}
		
	public List<CompanyModel> getCompanyList(CompanyDBModel objDB )
	{
		 List<CompanyModel> lst=new ArrayList<CompanyModel>();		 			
		 ResultSet rs = null;
		 try 
		  {
			 	SQLDBHandler dbSQL=new SQLDBHandler(objDB);
				rs=dbSQL.executeNonQuery(query.getCompanyListQuery());
				CompanyModel obj1=new CompanyModel();
				obj1.setCompKey(0);					
				obj1.setEnCompanyName("Select");
				//obj.setArCompanyName(rs.getString("COMP_NAME_AR"));	
				lst.add(obj1);
				while(rs.next())
				{
					CompanyModel obj=new CompanyModel();
					obj.setCompKey(rs.getInt("COMP_KEY"));					
					obj.setEnCompanyName(rs.getString("COMP_NAME"));
					obj.setArCompanyName(rs.getString("COMP_NAME_AR"));					
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in CompanyDBData---getCompanyList-->" , ex);
			}
		 return lst;
	}
	
	public List<Integer> getUserCompanyList(int userID)
	{
		 List<Integer> lst=new ArrayList<Integer>();		
		 ResultSet rs = null;
		 try 
		  {		
			 	DBHandler db = new DBHandler();	 
				rs=db.executeNonQuery(query.getUserCompanyQuery(userID));
				while(rs.next())
				{								
					lst.add(rs.getInt("companyid"));
				}
			}
			catch (Exception ex) {
				logger.error("error in CompanyDBData---getUserCompanyList-->" , ex);
			}
		 return lst;
	}
	
	public void addUserCompanyAccess(int userId,List<CompanyModel> lstUserCompany,CompanyDBModel objDB,String UserName)
	{
		try
		{
		  DBHandler db = new DBHandler();
		  SQLDBHandler dbSQL=new SQLDBHandler(objDB);
		  db.executeUpdateQuery(query.deleteUserCompanyQuery(userId));
		  dbSQL.executeUpdateQuery("DELETE FROM USERWEBCOMPANY WHERE userid=" + userId);
		  
		  for (CompanyModel item : lstUserCompany) 
		  {
			if(item.isCanView())
			{
			db.executeUpdateQuery(query.addUserCompanyQuery(userId, item.getCompKey()));			
			dbSQL.executeUpdateQuery("insert into USERWEBCOMPANY(userid,companyid,UserName) values(" + userId + " , " + item.getCompKey() + " , '" + UserName + "' )");
			}
			
		  }
		  				 			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---addUserCompanyAccess-->" , ex);			
		  }
	}
	
	//Company File Access
	public List<MenuModel> getCompanyFileMenuList(int companyId)
	{
		List<MenuModel> lst=new ArrayList<MenuModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getCompanyFileMenuQuery(companyId));
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
				 obj.setMenuid(rs.getInt("menuid"));
				 obj.setTitle(rs.getString("title"));
				 obj.setArtitle(rs.getString("artitle"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getCompanyFileMenuList-->" , ex);			
		}
		return lst;
	}
	
	public List<MenuModel> getSubMenuList(int parentId)
	{
		List<MenuModel> lst=new ArrayList<MenuModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getSubMenuListQuery(parentId));
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
				 obj.setMenuid(rs.getInt("menuid"));
				 obj.setTitle(rs.getString("title"));
				 obj.setArtitle(rs.getString("artitle"));
				 obj.setParentid(rs.getInt("parentid"));
				 obj.setLevel(rs.getInt("level"));
				
				 //use this for Reports Menu caz dont have submenu
				 List<MenuModel> lstChildren=getSubMenuList(obj.getMenuid(),2);
				 if(lstChildren.size()>0)
				 {
				 obj.setChildren(lstChildren);
				 }
				 else
				 {
					 List<MenuModel>  lstSubMenu=new ArrayList<MenuModel>();
					 MenuModel sub=new MenuModel();
					 sub.setMenuid(obj.getMenuid());
					 sub.setTitle(obj.getTitle());
					 sub.setHref(obj.getHref());
					 sub.setLevel(obj.getLevel());		
					 sub.setArtitle(obj.getArtitle());
					 sub.setCanView(false);
					 sub.setCanModify(false);
					 sub.setCanDelete(false);
					 lstSubMenu.add(sub);
					 obj.setAddReportMenu(true);
					 obj.setChildren(lstSubMenu);
				 }
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getSubMenuList-->" , ex);			
		}
		return lst;
	}
	
	private List<MenuModel> getSubMenuList(int parentid,int level)
	{
		 List<MenuModel>  lstMenu=new ArrayList<MenuModel>();
		 DBHandler db=new DBHandler();
		 ResultSet rs=null;
		 try
			{
			 String sql="SELECT menuid,title,artitle,href,level FROM webmenu where level= " + level + " and  parentid=" + parentid +" order by menuorder";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 	MenuModel obj=new MenuModel();
		 			obj.setMenuid(rs.getInt("menuid"));
		 			obj.setTitle(rs.getString("title"));
		 			obj.setHref(rs.getString("href"));
		 			obj.setLevel(rs.getInt("level"));		
		 			obj.setArtitle(rs.getString("artitle"));
		 			obj.setCanView(false);
		 			obj.setCanModify(false);
		 			obj.setCanDelete(false);
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
	
	//roles
	public List<Integer> getCompanyRolesUsed()
	{
		List<Integer> lst=new ArrayList<Integer>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.checkCompanyRolesUsedQuery());
			 while(rs.next())
			 {
				 lst.add(rs.getInt("companyroleid"));
			 }
		}
		catch (Exception ex) 
		{
			logger.error("error in CompanyDBData---getCompanyRolesUsed-->" , ex);			
		}
		return lst;
	}
	
	public List<CompanyRoleModel> getCompanyRolesList(int companyid)
	{
		List<CompanyRoleModel> lst=new ArrayList<CompanyRoleModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getCompanyRolesQuery(companyid));
			 while(rs.next())
			 {
				 CompanyRoleModel obj=new CompanyRoleModel();
				 obj.setCompanyroleid(rs.getInt("companyroleid"));
				 obj.setCompanyid(rs.getInt("companyid"));
				 obj.setRolename(rs.getString("rolename"));
				 lst.add(obj);
			 }
		}
		 catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getCompanyRolesList-->" , ex);			
		}
		return lst;
	}
	
	public void deleteCompanyRole(int companyroleid)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.deleteCompanyRoleQuery(companyroleid));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---deleteCompanyRole-->" , ex);			
		  }
	}
	
	public void saveCompanyRole(CompanyRoleModel obj)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 if(obj.getCompanyroleid()>0)
			 db.executeUpdateQuery(query.updateCompanyRoleQuery(obj.getCompanyroleid(), obj.getRolename()));
			 else
			db.executeUpdateQuery(query.addCompanyRoleQuery(obj.getCompanyid(), obj.getRolename()));	 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---saveCompanyRole-->" , ex);			
		  }
	}
	
	//roles credentails
	public List<MenuModel> getRoleCredentials(int companyroleid,int parentId)
	{
		List<MenuModel> lst=new ListModelList<MenuModel>();
		try
		{
			 DBHandler db = new DBHandler();
			 ResultSet rs = null;
			 rs=db.executeNonQuery(query.getRolesCredentialsQuery(companyroleid,parentId));
			 while(rs.next())
			 {
				 MenuModel obj=new MenuModel();
				 obj.setMenuid(rs.getInt("menuid"));
				 obj.setCanView(rs.getBoolean("canview"));
				 obj.setCanModify(rs.getBoolean("canmodify"));
				 obj.setCanDelete(rs.getBoolean("candelete"));
				 obj.setCanAdd(rs.getBoolean("canadd"));
				 obj.setCanExport(rs.getBoolean("canexport"));
				 obj.setCanPrint(rs.getBoolean("canprint"));
				 obj.setCanAllowToSeeAccountingTrasaction(rs.getBoolean("allowToseeTrasaction"));
				 obj.setAllowToActive(rs.getBoolean("allowToActive"));
				 obj.setAllowToInActive(rs.getBoolean("allowToInactive"));
				 obj.setCanApprove(rs.getBoolean("canApprove"));
				 obj.setCanChangeStatus(rs.getBoolean("canChangeStatus"));
				 obj.setCanConvert(rs.getBoolean("canConvert"));
				 lst.add(obj);
			 }
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getRoleCredentials-->" , ex);			
		}
		return lst;
	}
	public void deleteUserRoleCredential(int companyroleid,int parentId)
	{
		try
		{
			 DBHandler db = new DBHandler();
			 db.executeUpdateQuery(query.deleteUserRoleCredentialQuery(companyroleid,parentId));
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---deleteUserRoleCredential-->" , ex);			
		}
	}
	public void addMainMenuToRolesCredentials(int userId,int companyroleid, MenuModel objMenu,int parentId)
	{
		try
		{
		  DBHandler db = new DBHandler();	
		  db.executeUpdateQuery(query.addMainMenuToRolesCredentailsQuery(userId, companyroleid, objMenu,parentId));
		 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---addMainMenuToRolesCredentails-->" , ex);			
		}
	}
	public void addFullUserRolesCredentials(int userId,int companyroleid, List<MenuModel> lstMainUserMenu,int parentId)
	{
		try
		{
		  DBHandler db = new DBHandler();		  
		  for (MenuModel item : lstMainUserMenu) 
		  {
			if(item.isCanView() || item.isCanModify() || item.isCanDelete() || item.isCanAdd() || item.isCanExport() || item.isCanPrint() || item.isCanAllowToSeeAccountingTrasaction() || item.isAllowToActive() || item.isAllowToInActive())
			db.executeUpdateQuery(query.addMainMenuToRolesCredentailsQuery(userId, companyroleid, item,parentId));
		  }
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---addFullUserRolesCredentials-->" , ex);			
		}
	}
	
	//Company Settings
	public CompanySettingsModel getCompanySettings(int companyid)
	{
		CompanySettingsModel obj=new CompanySettingsModel();
		try
		{
		  DBHandler db = new DBHandler();		  
		  ResultSet rs = null;
		  rs=db.executeNonQuery(query.getCompanySettingsQuery(companyid));
		  while(rs.next())
		  {
			  obj.setCompanyid(rs.getInt("companyid"));
			  obj.setSettingid(rs.getInt("settingid"));
			  obj.setHidecalculate(rs.getBoolean("hidecalculate"));
			  obj.setHideservice(rs.getBoolean("hideservice"));
			  obj.setMandatoryservice(rs.getBoolean("mandatoryservice"));
			  obj.setProjecttype(rs.getString("projecttype"));
			  obj.setDateType(rs.getString("datetype"));
			  obj.setHideOverTime(rs.getBoolean("hideovertime"));
			  obj.setAutoApprove(rs.getBoolean("autoapprove"));
			  obj.setHideTime(rs.getBoolean("hidetime"));
			  obj.setHideTomorrowPlan(rs.getBoolean("hidetomorrowplan"));
			  obj.setMandatoryTomorrowPlan(rs.getBoolean("mandatorytomorrowplan"));
			  obj.setHideCustomerJob(rs.getBoolean("hidecustomerjob"));
			  obj.setMandatoryCustomerJob(rs.getBoolean("mandatorycustomerjob"));
			  obj.setHideCustomerTask(rs.getBoolean("hidetask"));
			  obj.setMandatoryCustomerTask(rs.getBoolean("mandatorytask"));
			  
			  obj.setHideAttachment(rs.getBoolean("hideattachment"));
			  obj.setMandatoryAttachment(rs.getBoolean("mandatoryattachment"));
			  obj.setFtpHost(rs.getString("ftphost")==null?"":rs.getString("ftphost"));
			  obj.setFtpPort(rs.getInt("ftpport"));
			  obj.setFtpUser(rs.getString("ftpuser")==null?"":rs.getString("ftpuser"));
			  obj.setFtpPassword(rs.getString("ftppassword")==null?"":rs.getString("ftppassword"));
			  obj.setFtpDirectory(rs.getString("ftpdirectory")==null?"":rs.getString("ftpdirectory"));			  			
			  
		  }
			 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---getCompanySettings-->" , ex);			
		  }
		
		return obj;
	}
	public void updateCompanySettings(CompanySettingsModel obj)
	{
		try
		{
		  DBHandler db = new DBHandler();	
		  db.executeUpdateQuery(query.updateCompanySettingsQuery(obj));
		 
		}
		catch (Exception ex) 
		  {
			  logger.error("error in CompanyDBData---updateCompanySettings-->" , ex);			
		}
	}
	
	@SuppressWarnings("unused")
	public void updateDefaultComapny(int userId,CompanyModel defaultCompany,CompanyDBModel objDB)
	{
		try
		{
		  DBHandler db = new DBHandler();
		  SQLDBHandler dbSQL=new SQLDBHandler(objDB);
		  dbSQL.executeUpdateQuery("update  USERWEBCOMPANY set defaultCompanyId="+defaultCompany.getCompKey()+" where userId="+userId);
		}
		catch (Exception ex) 
		{
			  logger.error("error in CompanyDBData---updateDefaultComapny-->" , ex);			
		}
	}
	
	
	@SuppressWarnings("finally")
	public WebusersModel getuserById(int companyID,int userId)
	{
		 DBHandler db = new DBHandler();
		 WebusersModel item=new WebusersModel();
		 ResultSet rs = null;
		 try
		 {			
		    rs = db.executeNonQuery(query.getuserById(companyID,userId));		    
		    while(rs.next())
		    {
		    	
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
				//item.setRoleName(rs.getString("rolename"));
				item.setEmployeeKey(rs.getInt("employeekey"));
		    }
			 
		 }
		 catch (Exception ex) 
	 		{		 	  
			 logger.error("error in CompanyData---getuserById-->" , ex);			 	  
		 	 }
		 finally
		 {
			 return item;
		 }
	}
	
	
}
