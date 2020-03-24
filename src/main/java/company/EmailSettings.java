package company;

import hr.HRData;
import hr.model.CompanyModel;

import java.util.List;

import layout.MenuModel;
import model.EmailSettingsModel;
import model.ExpirySettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

import setup.users.WebusersModel;

public class EmailSettings 
{

	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	
	int menuID=313;
	private int UserId;	
	private MenuModel companyRole;
	//leave
	private boolean checkEnableEmail=false;
	
	private String email1="";
	
	private String email2="";
	
	private String email3="";
	private String emailhost="";
	
	
	EmailSettingsModel obj=new EmailSettingsModel(); 

	
 	public EmailSettings()
	 	{
	 		try
	 		{
	 		Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			UserId=dbUser.getUserid();
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			//getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			int defaultCompanyId=0;
			defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());//we have to change later
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			
			if(lstComapnies.size()>=1 && selectedCompany==null)
			selectedCompany=lstComapnies.get(0);
			obj=data.getEmailSettings(selectedCompany.getCompKey());
			//leave			
			if(obj.getEnalbeMail().equalsIgnoreCase("Y"))
			{
				checkEnableEmail=true;
			}
			else
			{
				checkEnableEmail=false;
			}
			
			email1=obj.getEmail1();
			
			email2=obj.getEmail2();
			
			email3=obj.getEmail3();
			emailhost=obj.getEmailhost();
			
	 		}
	 		catch (Exception ex)
			{	
				logger.error("ERROR in ExpirySettings ----> init", ex);			
			}
	 	}

	 	private void getCompanyRolePermessions(int companyRoleId)
		{
			setCompanyRole(new MenuModel());
			HRData data=new HRData();
			List<MenuModel> lstRoles= data.getHRRoles(companyRoleId);
			for (MenuModel item : lstRoles) 
			{
				if(item.getMenuid()==menuID)
				{
					setCompanyRole(item);
					break;
				}
			}
		}
	 	
	@Command
	public void clearData()
	{
	  
	}
	
	@Command
	public void save()
	{
	  	try{
		//leave			
		if(checkEnableEmail)
		{
			obj.setEnalbeMail("Y");
		}
		else
		{
			obj.setEnalbeMail("N");
		}
		
		obj.setEmail1(email1);
		
		obj.setEmail2(email2);
		
		obj.setEmail3(email3);
		obj.setEmailhost(emailhost);
				
		data.updateEmailSettings(obj,selectedCompany.getCompKey());
		Clients.showNotification("The Email Settings Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
	  	}
	  	catch (Exception ex)
		{	
			logger.error("ERROR in ExpirySettings ----> Save", ex);			
		}
		
	}
	
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}


	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}


	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}


	@NotifyChange({"email1","email2","email3","checkEnableEmail","emailhost","","","","","","","",})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
				obj=data.getEmailSettings(selectedCompany.getCompKey());
		//leave			
		if(obj.getEnalbeMail().equalsIgnoreCase("Y"))
		{
			checkEnableEmail=true;
		}
		else
		{
			checkEnableEmail=false;
		}
		
		email1=obj.getEmail1();
		
		email2=obj.getEmail2();
		
		email3=obj.getEmail3();
		emailhost=obj.getEmailhost();
		
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}


	public int getUserId() {
		return UserId;
	}


	public void setUserId(int userId) {
		UserId = userId;
	}



	public boolean isCheckEnableEmail() {
		return checkEnableEmail;
	}

	public void setCheckEnableEmail(boolean checkEnableEmail) {
		this.checkEnableEmail = checkEnableEmail;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getEmailhost() {
		return emailhost;
	}

	public void setEmailhost(String emailhost) {
		this.emailhost = emailhost;
	}

	

	
	
}
