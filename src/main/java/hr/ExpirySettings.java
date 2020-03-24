package hr;

import hr.model.CompanyModel;

import java.util.List;

import layout.MenuModel;
import model.ExpirySettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

import setup.users.WebusersModel;

public class ExpirySettings 
{

	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	
	int menuID=313;
	private int UserId;	
	private MenuModel companyRole;
	//leave
	private boolean checkLeaveVisa=false;
	private boolean checkLeavePassport=false;
	private boolean checkLeaveResidence=false;
	private boolean checkLeaveLabourCrad=false;
	private boolean checkLeaveGovtId=false;
	private boolean checkLeaveHealthCard=false;
	//loan
	private boolean checkLoanVisa=false;
	private boolean checkLoanPassport=false;
	private boolean checkLoanResidence=false;
	private boolean checkLoanLabourCrad=false;
	private boolean checkLoanGovtId=false;
	private boolean checkLoanHealthCard=false;
	
	private int totalDays=0;
	
	ExpirySettingsModel obj=new ExpirySettingsModel(); 

	
 	public ExpirySettings()
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
			obj=data.getexpirySettings(selectedCompany.getCompKey());
			//leave			
			if(obj.getStrLeaveVisa().equalsIgnoreCase("Y"))
			{
				checkLeaveVisa=true;
			}
			else
			{
				checkLeaveVisa=false;
			}
			
			if(obj.getStrLeavePassport().equalsIgnoreCase("Y"))
			{
				checkLeavePassport=true;
			}
			else
			{
				checkLeavePassport=false;
			}
			
			if(obj.getStrLeaveResidence().equalsIgnoreCase("Y"))
			{
				checkLeaveResidence=true;
			}
			else
			{
				checkLeaveResidence=false;
			}
			
			if(obj.getStrLeaveLabourCrad().equalsIgnoreCase("Y"))
			{
				checkLeaveLabourCrad=true;
			}
			else
			{
				checkLeaveLabourCrad=false;
			}
			
			if(obj.getStrLeaveHealthCard().equalsIgnoreCase("Y"))
			{
				checkLeaveHealthCard=true;
			}
			else
			{
				checkLeaveHealthCard=false;
			}
			
			if(obj.getStrLeaveGovtId().equalsIgnoreCase("Y"))
			{
				checkLeaveGovtId=true;
			}
			else
			{
				checkLeaveGovtId=false;
			}
			
			//loan
			if(obj.getStrLoanVisa().equalsIgnoreCase("Y"))
			{
				checkLoanVisa=true;
			}
			else
			{
				checkLoanVisa=false;
			}
			
			if(obj.getStrLoanPassport().equalsIgnoreCase("Y"))
			{
				checkLoanPassport=true;
			}
			else
			{
				checkLoanPassport=false;
			}
			
			if(obj.getStrLoanResidence().equalsIgnoreCase("Y"))
			{
				checkLoanResidence=true;
			}
			else
			{
				checkLoanResidence=false;
			}
			
			if(obj.getStrLoanLabourCrad().equalsIgnoreCase("Y"))
			{
				checkLoanLabourCrad=true;
			}
			else
			{
				checkLoanLabourCrad=false;
			}
			
			if(obj.getStrLoanGovtId().equalsIgnoreCase("Y"))
			{
				checkLoanGovtId=true;
			}
			else
			{
				checkLoanGovtId=false;
			}
			
			if(obj.getStrLoanHealthCard().equalsIgnoreCase("Y"))
			{
				checkLoanHealthCard=true;
			}
			else
			{
				checkLoanHealthCard=false;
			}
			
			totalDays=obj.getTotalDays();
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
		if(checkLeaveVisa)
		{
			obj.setStrLeaveVisa("Y");
		}
		else
		{
			obj.setStrLeaveVisa("N");
		}
		
		if(checkLeavePassport)
		{
			obj.setStrLeavePassport("Y");
		}
		else
		{
			obj.setStrLeavePassport("N");
		}
		
		if(checkLeaveResidence)
		{
			obj.setStrLeaveResidence("Y");
		}
		else
		{
			obj.setStrLeaveResidence("N");
		}
		
		if(checkLeaveLabourCrad)
		{
			obj.setStrLeaveLabourCrad("Y");
		}
		else
		{
			obj.setStrLeaveLabourCrad("N");
		}
		
		if(checkLeaveHealthCard)
		{
			obj.setStrLeaveHealthCard("Y");
		}
		else
		{
			obj.setStrLeaveHealthCard("N");
		}
		
		if(checkLeaveGovtId)
		{
			obj.setStrLeaveGovtId("Y");
		}
		else
		{
			obj.setStrLeaveGovtId("N");
		}
		
		//loan
		if(checkLoanVisa)
		{
			obj.setStrLoanVisa("Y");
		}
		else
		{
			obj.setStrLoanVisa("N");
		}
		
		if(checkLoanPassport)
		{
			obj.setStrLoanPassport("Y");
		}
		else
		{
			obj.setStrLoanPassport("N");
		}
		
		if(checkLoanResidence)
		{
			obj.setStrLoanResidence("Y");
		}
		else
		{
			obj.setStrLoanResidence("N");
		}
		
		if(checkLoanLabourCrad)
		{
			obj.setStrLoanLabourCrad("Y");
		}
		else
		{
			obj.setStrLoanLabourCrad("N");
		}
		
		if(checkLoanGovtId)
		{
			obj.setStrLoanGovtId("Y");
		}
		else
		{
			obj.setStrLoanGovtId("N");
		}
		
		if(checkLoanHealthCard)
		{
			obj.setStrLoanHealthCard("Y");
		}
		else
		{
			obj.setStrLoanHealthCard("N");
		}
		obj.setTotalDays(totalDays);
		data.saveExpirySettings(obj,selectedCompany.getCompKey());
		Clients.showNotification("The Exipry Settings Has Been Updated Successfully.",
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


	@NotifyChange({"checkLeaveVisa","checkLeavePassport","checkLeaveResidence","checkLeaveLabourCrad","checkLeaveHealthCard","checkLeaveGovtId","checkLoanVisa","checkLoanPassport","checkLoanResidence","checkLoanLabourCrad","checkLoanGovtId","checkLoanHealthCard",})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
				obj=data.getexpirySettings(selectedCompany.getCompKey());
		//leave			
		if(obj.getStrLeaveVisa().equalsIgnoreCase("Y"))
		{
			checkLeaveVisa=true;
		}
		else
		{
			checkLeaveVisa=false;
		}
		
		if(obj.getStrLeavePassport().equalsIgnoreCase("Y"))
		{
			checkLeavePassport=true;
		}
		else
		{
			checkLeavePassport=false;
		}
		
		if(obj.getStrLeaveResidence().equalsIgnoreCase("Y"))
		{
			checkLeaveResidence=true;
		}
		else
		{
			checkLeaveResidence=false;
		}
		
		if(obj.getStrLeaveLabourCrad().equalsIgnoreCase("Y"))
		{
			checkLeaveLabourCrad=true;
		}
		else
		{
			checkLeaveLabourCrad=false;
		}
		
		if(obj.getStrLeaveHealthCard().equalsIgnoreCase("Y"))
		{
			checkLeaveHealthCard=true;
		}
		else
		{
			checkLeaveHealthCard=false;
		}
		
		if(obj.getStrLeaveGovtId().equalsIgnoreCase("Y"))
		{
			checkLeaveGovtId=true;
		}
		else
		{
			checkLeaveGovtId=false;
		}
		
		//loan
		if(obj.getStrLoanVisa().equalsIgnoreCase("Y"))
		{
			checkLoanVisa=true;
		}
		else
		{
			checkLoanVisa=false;
		}
		
		if(obj.getStrLoanPassport().equalsIgnoreCase("Y"))
		{
			checkLoanPassport=true;
		}
		else
		{
			checkLoanPassport=false;
		}
		
		if(obj.getStrLoanResidence().equalsIgnoreCase("Y"))
		{
			checkLoanResidence=true;
		}
		else
		{
			checkLoanResidence=false;
		}
		
		if(obj.getStrLoanLabourCrad().equalsIgnoreCase("Y"))
		{
			checkLoanLabourCrad=true;
		}
		else
		{
			checkLoanLabourCrad=false;
		}
		
		if(obj.getStrLoanGovtId().equalsIgnoreCase("Y"))
		{
			checkLoanGovtId=true;
		}
		else
		{
			checkLoanGovtId=false;
		}
		
		if(obj.getStrLoanHealthCard().equalsIgnoreCase("Y"))
		{
			checkLoanHealthCard=true;
		}
		else
		{
			checkLoanHealthCard=false;
		}
		
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


	public boolean isCheckLeaveVisa() {
		return checkLeaveVisa;
	}


	public void setCheckLeaveVisa(boolean checkLeaveVisa) {
		this.checkLeaveVisa = checkLeaveVisa;
	}


	public boolean isCheckLeavePassport() {
		return checkLeavePassport;
	}


	public void setCheckLeavePassport(boolean checkLeavePassport) {
		this.checkLeavePassport = checkLeavePassport;
	}


	public boolean isCheckLeaveResidence() {
		return checkLeaveResidence;
	}


	public void setCheckLeaveResidence(boolean checkLeaveResidence) {
		this.checkLeaveResidence = checkLeaveResidence;
	}


	public boolean isCheckLeaveGovtId() {
		return checkLeaveGovtId;
	}


	public void setCheckLeaveGovtId(boolean checkLeaveGovtId) {
		this.checkLeaveGovtId = checkLeaveGovtId;
	}

	public boolean isCheckLeaveLabourCrad() {
		return checkLeaveLabourCrad;
	}

	public void setCheckLeaveLabourCrad(boolean checkLeaveLabourCrad) {
		this.checkLeaveLabourCrad = checkLeaveLabourCrad;
	}

	public boolean isCheckLeaveHealthCard() {
		return checkLeaveHealthCard;
	}

	public void setCheckLeaveHealthCard(boolean checkLeaveHealthCard) {
		this.checkLeaveHealthCard = checkLeaveHealthCard;
	}

	public boolean isCheckLoanVisa() {
		return checkLoanVisa;
	}

	public void setCheckLoanVisa(boolean checkLoanVisa) {
		this.checkLoanVisa = checkLoanVisa;
	}

	public boolean isCheckLoanPassport() {
		return checkLoanPassport;
	}

	public void setCheckLoanPassport(boolean checkLoanPassport) {
		this.checkLoanPassport = checkLoanPassport;
	}

	public boolean isCheckLoanResidence() {
		return checkLoanResidence;
	}

	public void setCheckLoanResidence(boolean checkLoanResidence) {
		this.checkLoanResidence = checkLoanResidence;
	}

	public boolean isCheckLoanLabourCrad() {
		return checkLoanLabourCrad;
	}

	public void setCheckLoanLabourCrad(boolean checkLoanLabourCrad) {
		this.checkLoanLabourCrad = checkLoanLabourCrad;
	}

	public boolean isCheckLoanGovtId() {
		return checkLoanGovtId;
	}

	public void setCheckLoanGovtId(boolean checkLoanGovtId) {
		this.checkLoanGovtId = checkLoanGovtId;
	}

	public boolean isCheckLoanHealthCard() {
		return checkLoanHealthCard;
	}

	public void setCheckLoanHealthCard(boolean checkLoanHealthCard) {
		this.checkLoanHealthCard = checkLoanHealthCard;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	
	
}
