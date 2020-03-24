package company;

import java.sql.ResultSet;
import java.util.List;

import hba.HBAQueries;
import layout.MenuModel;
import model.CompSetupModel;
import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import db.DBHandler;
import db.SQLDBHandler;
import setup.users.WebusersModel;

public class CompanySettingsViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private CompSetupModel companySettings;
	CompanyQueries query=new CompanyQueries();
	CompanyData data=new CompanyData();
	WebusersModel dbUser = null;
	SQLDBHandler db ;
	private boolean usePurchaseFlow;
	private boolean useSalesFlow;
	private boolean allowToSkip;
	private boolean allowToSkipPurchase;
	String tableName;
	int menuID=339;//336;
	private MenuModel companyRole;
	
	public CompanySettingsViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			dbUser = (WebusersModel) sess.getAttribute("Authentication");
			if(dbUser==null)
			{
				Executions.sendRedirect("/login.zul");
				return;
			}
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			if(dbUser!=null)
			{
				DBHandler mysqldb = new DBHandler();
				HBAQueries query = new HBAQueries();
				ResultSet rs = null;
				CompanyDBModel obj = new CompanyDBModel();
				rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				while (rs.next()) 
				{
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));
				}
				db = new SQLDBHandler(obj);
				fillCompanySettings();
			}
		}
		catch (Exception ex)
		{
		logger.error("error at CompanySettingsViewModel>>Init>> "+ex.getMessage());
		}
	}
	
	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();		
		List<MenuModel> lstRoles= 	data.getRoleCredentials(companyRoleId, 272);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	private void fillCompanySettings()
	{
		try
		{
			companySettings=new CompSetupModel();
			ResultSet rs = null;
			tableName="companySettings";
			if (dbUser.getMergedDatabse().equalsIgnoreCase("Yes")) 
			{
				tableName= "companySettings";
			} 
			else 
			{
				tableName="COMPSETUP";
			}
			rs = db.executeNonQuery(query.getERPCompanySettingsQuery(tableName));
			while (rs.next()) 
			{
				companySettings.setUsePurchaseFlow(rs.getString("usePurchaseFlow") == null ? "": rs.getString("usePurchaseFlow"));
				companySettings.setUseSalesFlow(rs.getString("UseSalesFlow") == null ? "": rs.getString("UseSalesFlow"));
				companySettings.setAllowToSkip(rs.getString("AllowToSkipSalesWorkFlow") == null ? "": rs.getString("AllowToSkipSalesWorkFlow"));
				companySettings.setAllowToSkipPurchaseWorkFlow(rs.getString("AllowToSkipPurchaseWorkFlow") == null ? "": rs.getString("AllowToSkipPurchaseWorkFlow"));
				
			}
			
			useSalesFlow=companySettings.getUseSalesFlow().toUpperCase().equals("Y");
			usePurchaseFlow=companySettings.getUsePurchaseFlow().toUpperCase().equals("Y");
			allowToSkip=companySettings.getAllowToSkip().toUpperCase().equals("Y");
			allowToSkipPurchase=companySettings.getAllowToSkipPurchaseWorkFlow().toUpperCase().equals("Y");
		}
		catch (Exception ex)
		{
		logger.error("error at CompanySettingsViewModel>>getCompanySettings>> "+ex.getMessage());
		}
	}
	
	@Command
	public void saveCommand()
	{
		try
		{
			companySettings.setUseSalesFlow(useSalesFlow?"Y":"N");	
			companySettings.setUsePurchaseFlow(usePurchaseFlow?"Y":"N");
			companySettings.setAllowToSkip(allowToSkip?"Y":"N");
			companySettings.setAllowToSkipPurchaseWorkFlow(allowToSkipPurchase?"Y":"N");
			
			db.executeUpdateQuery(query.updateERPComapnySettings(companySettings, tableName));
			
			Messagebox.show("Company settings saved..","Company settings", Messagebox.OK , Messagebox.INFORMATION);
		}
		catch (Exception ex)
		{
		logger.error("error at CompanySettingsViewModel>>saveCommand>> "+ex.getMessage());
		}
	}

	public CompSetupModel getCompanySettings() {
		return companySettings;
	}

	public void setCompanySettings(CompSetupModel companySettings) {
		this.companySettings = companySettings;
	}

	public boolean isUsePurchaseFlow() {
		return usePurchaseFlow;
	}

	public void setUsePurchaseFlow(boolean usePurchaseFlow) {
		this.usePurchaseFlow = usePurchaseFlow;
	}

	public boolean isUseSalesFlow() {
		return useSalesFlow;
	}

	public void setUseSalesFlow(boolean useSalesFlow) {
		this.useSalesFlow = useSalesFlow;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public boolean isAllowToSkip() {
		return allowToSkip;
	}

	public void setAllowToSkip(boolean allowToSkip) {
		this.allowToSkip = allowToSkip;
	}

	public boolean isAllowToSkipPurchase() {
		return allowToSkipPurchase;
	}

	public void setAllowToSkipPurchase(boolean allowToSkipPurchase) {
		this.allowToSkipPurchase = allowToSkipPurchase;
	}

	
}
