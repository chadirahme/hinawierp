package company;

import java.util.List;

import layout.MenuModel;
import model.CompSetupModel;
import model.CompanySettingsModel;

public class CompanyQueries 
{
	StringBuffer query;
	public String getSupervisorListQuery()
	{
		query=new StringBuffer();
	    query.append("select distinct EMP_KEY,ENGLISH_FULL from WORKERGROUPS inner JOIN EMPMAST ON WORKERGROUPS.SUPERVISOR=EMPMAST.EMP_KEY ");
		return query.toString();		
	}
	
	public String getEmployeeListQuery()
	{
		query=new StringBuffer();
	    query.append("Select * from EmployeeDetails where Active ='A' order by fullname ");
		return query.toString();		
	}
	
	
	public String getCompanyUsersQuery(int companyID)
	{
		query=new StringBuffer();
	    query.append("Select * from webusers u LEFT JOIN companyroles r ON u.companyroleid=r.companyroleid where u.companyid="+companyID + " order by isadmin desc");
		return query.toString();		
	}
	
	public String getUserMenuQuery(int userID)
	{
		query=new StringBuffer();
	    query.append("SELECT m.menuid, w.title FROM usermenu m INNER JOIN webmenu w ON m.menuid=w.menuid where m.userid="+userID);
		return query.toString();		
	}
	
	public String deleteUserMenuQuery(int userID)
	{
		query=new StringBuffer();
	    query.append("DELETE FROM usermenu WHERE userid="+userID);
		return query.toString();		
	}
	
	public String addUserMenuQuery(int userID,int menuID)
	{
		query=new StringBuffer();
		query.append("insert into usermenu(userid,menuid,menuorder,level) values('%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(), userID,menuID,0,0);		
	}
	
	public String addFullUserAccessMenuQuery(int userID,MenuModel objMenu)
	{
		query=new StringBuffer();
		query.append("insert into usermenu(userid,menuid,menuorder,level,canview,canmodify,candelete) values('%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(), userID,objMenu.getMenuid(),0,0,objMenu.isCanView()?1:0,objMenu.isCanModify()?1:0,objMenu.isCanDelete()?1:0);		
	}
	
	public String getDBCompany(int companyId)
	{
		 String sql="Select * from companiesdb where dbtype='HR' and companyid=" + companyId ;
		 return sql;
	}
	
	public String getCompanyListQuery()
	{
		query=new StringBuffer();
		 query.append("SELECT  COMPSETUP.COMP_KEY,COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR FROM ");
		 query.append(" COMPSETUP");		 
		 query.append(" ORDER BY COMPSETUP.COMP_NAME");
		 return query.toString();		
	}
	
	public String getUserCompanyQuery(int userID)
	{
		query=new StringBuffer();
	    query.append("SELECT companyid FROM usercompany where userid="+userID);
		return query.toString();		
	}
	
	public String deleteUserCompanyQuery(int userID)
	{
		query=new StringBuffer();
	    query.append("DELETE FROM usercompany WHERE userid="+userID);
		return query.toString();		
	}
	
	public String addUserCompanyQuery(int userID,int companyid)
	{
		query=new StringBuffer();
		query.append("insert into usercompany(userid,companyid) values('%s','%s')");				    
		return query.toString().format(query.toString(), userID,companyid);		
	}
	
	//Company File Access
	public String getCompanyFileMenuQuery(int companyId)
	{
		query=new StringBuffer();
	    query.append("SELECT m.menuid,m.artitle,m.title FROM companyfile f INNER JOIN webmenu m ON f.menuid=m.menuid where f.companyid="+companyId);
		return query.toString();		
	}
	
	public String getSubMenuListQuery(int parentId)
	{
		query=new StringBuffer();
	    query.append("SELECT menuid, title,artitle,parentid,level FROM webmenu where parentid="+parentId + " order by menuorder");
		return query.toString();		
	}
	
	//roles
	public String getCompanyRolesQuery(int companyid)
	{
		query=new StringBuffer();
	    query.append("SELECT companyroleid,companyid,rolename FROM companyroles where companyid="+companyid);
		return query.toString();		
	}
	
	public String deleteCompanyRoleQuery(int companyroleid)
	{
		query=new StringBuffer();
	    query.append("DELETE FROM companyroles WHERE companyroleid="+companyroleid);
		return query.toString();		
	}
	
	public String addCompanyRoleQuery(int companyid,String roleName)
	{
		query=new StringBuffer();
	    query.append("insert into companyroles (companyid,rolename) values('%s','%s')");		
	    return query.toString().format(query.toString(),companyid,roleName);		
	}
	
	public String updateCompanyRoleQuery(int companyroleid,String roleName)
	{
		query=new StringBuffer();
	    query.append("update companyroles set rolename='" + roleName + "' where companyroleid=" + companyroleid);		
	    return query.toString();
	}
	public String checkCompanyRolesUsedQuery()
	{
		query=new StringBuffer();
	    query.append("SELECT DISTINCT companyroleid FROM webusers WHERE companyroleid IS NOT NULL");
		return query.toString();		
	}
	
	//roles Credentials
	public String getRolesCredentialsQuery(int companyroleid,int parentId)
	{
		query=new StringBuffer();
	    query.append("SELECT * FROM rolescredentials where companyroleid="+companyroleid + " and parentId=" + parentId);
		return query.toString();		
	}
	
	public String deleteUserRoleCredentialQuery(int companyroleid,int parentId)
	{
		query=new StringBuffer();
	    query.append("DELETE FROM rolescredentials WHERE companyroleid="+companyroleid + " and parentId=" + parentId);
		return query.toString();		
	}
	public String addMainMenuToRolesCredentailsQuery(int userID,int companyroleid,MenuModel objMenu,int parentId)
	{
		query=new StringBuffer();
		query.append("insert into rolescredentials(createdby, companyroleid,menuid,canview,canmodify,candelete,parentId,canadd,canexport,canprint,allowToseeTrasaction,allowToActive,allowToInactive,canApprove,canChangeStatus,canConvert)"
				+ " values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s' ,'%s','%s','%s')");				    
		return query.toString().format(query.toString(), userID, companyroleid , objMenu.getMenuid(),objMenu.isCanView()?1:0,objMenu.isCanModify()?1:0
				,objMenu.isCanDelete()?1:0 , parentId , objMenu.isCanAdd()?1:0,objMenu.isCanExport()?1:0,objMenu.isCanPrint()?1:0,objMenu.isCanAllowToSeeAccountingTrasaction()?1:0,objMenu.isAllowToActive()?1:0,objMenu.isAllowToInActive()?1:0,
					objMenu.isCanApprove()?1:0,objMenu.isCanChangeStatus()?1:0,objMenu.isCanConvert()?1:0);		
	}
	
	//Company Settings
	public String getCompanySettingsQuery(int companyid)
	{
		query=new StringBuffer();
	    query.append("SELECT * FROM companysettings where companyid="+companyid);
		return query.toString();		
	}
	public String updateCompanySettingsQuery(CompanySettingsModel obj)
	{
		query=new StringBuffer();
	    query.append("update companysettings set hidecalculate='%s' , hideservice='%s' , mandatoryservice='%s' , projecttype='%s' , datetype='%s' , hideovertime='%s',autoapprove='%s',hidetime='%s', hidetomorrowplan='%s', mandatorytomorrowplan='%s' ," +
	    		" hidecustomerjob='%s' , mandatorycustomerjob='%s' , hidetask='%s' , mandatorytask='%s' ,hideattachment='%s',mandatoryattachment='%s' ,  " +
	    		" ftphost='%s' , ftpport='%s' , ftpuser='%s' , ftppassword='%s' , ftpdirectory='%s'  where settingid='%s'");
		return query.toString().format(query.toString(),obj.isHidecalculate()?1:0,obj.isHideservice()?1:0,obj.isMandatoryservice()?1:0,
				obj.getProjecttype(),obj.getDateType(),
				obj.isHideOverTime()?1:0 ,obj.isAutoApprove()?1:0 ,obj.isHideTime()?1:0,obj.isHideTomorrowPlan()?1:0,obj.isMandatoryTomorrowPlan()?1:0,
				obj.isHideCustomerJob()?1:0,obj.isMandatoryCustomerJob()?1:0 , obj.isHideCustomerTask()?1:0,obj.isMandatoryCustomerTask()?1:0,
				obj.isHideAttachment()?1:0,obj.isMandatoryAttachment()?1:0,obj.getFtpHost(),obj.getFtpPort(),obj.getFtpUser(),obj.getFtpPassword(),obj.getFtpDirectory(),		
						obj.getSettingid());		
	}
	
	public String getuserById(int companyid,int userID)
	{
		query=new StringBuffer();
	    query.append("SELECT * FROM webusers where companyid="+companyid +" and userid="+userID+"");
		return query.toString();		
	}
	
	//ERP SQL Company Settings
	public String getERPCompanySettingsQuery(String tableName)
	{
		query=new StringBuffer();
	    query.append("SELECT * FROM " + tableName);
		return query.toString();		
	}
	
	public String updateERPComapnySettings(CompSetupModel obj ,String tableName)
	{
		query=new StringBuffer();
		query.append("update " + tableName + " set UseSalesFlow='%s' , usePurchaseFlow='%s' , AllowToSkipSalesWorkFlow='%s' ,AllowToSkipPurchaseWorkFlow='%s'" );
		return query.toString().format(query.toString(),obj.getUseSalesFlow(),obj.getUsePurchaseFlow(),obj.getAllowToSkip(),obj.getAllowToSkipPurchaseWorkFlow()); 		 		
	}
	
}
