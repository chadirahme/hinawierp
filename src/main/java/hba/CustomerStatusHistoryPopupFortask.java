package hba;

import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.CustomerStatusHistoryModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class CustomerStatusHistoryPopupFortask {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private List<CustomerStatusHistoryModel> customerHistroyStatusReport;
	
	private String customerName;
	
	private String tempCustomerName;
	
	private int webUserID=0;
	
	private boolean adminUser;
	
	private MenuModel companyRole;
	
	WebusersModel dbUser=null;
	
	HBAData hbadata=new HBAData();
	
	@SuppressWarnings("unchecked")
	public CustomerStatusHistoryPopupFortask()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			String type=(String)map.get("type");
			int customerKey=(Integer) map.get("cutomerKey");
			tempCustomerName=(String)map.get("customerName");
			Window win = (Window)Path.getComponent("/customerStatusHistory");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
				if(adminUser)
				{
					webUserID=0;
				}
				else
				{
					webUserID=dbUser.getUserid();
				}
			}
			
			if(type.equalsIgnoreCase("View"))
			{
				if(tempCustomerName!=null)
				win.setTitle("Status History For : " +tempCustomerName);
			}
			
			if(customerKey>0)
			customerHistroyStatusReport=hbadata.getCustomerStatusById(customerKey);
			
			if(tempCustomerName!=null)
			customerName="Customer Status History Report : "+tempCustomerName;
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerStatusHistoryPopupFortask ----> init", ex);			
		}
	}
	
	
	

	/**
	 * @return the customerHistroyStatusReport
	 */
	public List<CustomerStatusHistoryModel> getCustomerHistroyStatusReport() {
		return customerHistroyStatusReport;
	}

	/**
	 * @param customerHistroyStatusReport the customerHistroyStatusReport to set
	 */
	public void setCustomerHistroyStatusReport(
			List<CustomerStatusHistoryModel> customerHistroyStatusReport) {
		this.customerHistroyStatusReport = customerHistroyStatusReport;
	}




	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}




	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
	
	
	
	

}
