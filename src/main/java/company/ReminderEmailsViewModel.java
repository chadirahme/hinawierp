package company;

import hba.HBAQueries;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.CompanyDBModel;
import model.EmailSelectionModel;
import model.ReminderSettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

import db.DBHandler;
import setup.users.WebusersModel;

public class ReminderEmailsViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	WebusersModel dbUser=null;
	ReminderData data=new ReminderData();
	private List<ReminderSettingsModel> listReminderTypes=new ArrayList<ReminderSettingsModel>();
	private ReminderSettingsModel selectedReminderTypes;
	private Date fromDate;
	private Date toDate;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private List<EmailSelectionModel> lstEmails;
	private String totalEmails;
	
	public ReminderEmailsViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser==null)
			{
				Executions.sendRedirect("/login.zul");
			}

			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			HBAQueries query = new HBAQueries();
			rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
					.getCompanyid()));
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
			
			listReminderTypes=data.getAllCompanyReminder(dbUser.getCompanyid());

			if(listReminderTypes.size()>0)
				selectedReminderTypes=listReminderTypes.get(0);//deafult setting 
			
			Calendar c = Calendar.getInstance();
			fromDate=c.getTime();
			toDate=c.getTime();
			
		}
		catch (Exception ex)
		{
			logger.error("error at ReminderEmailsViewModel>>Init>> ",ex);
		}
	}

	@Command
	@NotifyChange({"lstEmails","totalEmails"})
	public void searchCommand() 
	{
		try
		{
			if(selectedReminderTypes.getReminderid()==0)
			{
				Clients.showNotification("Please select the reminder type.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DAY_OF_MONTH, 1);			
			Date tmpToDate=c.getTime();
			lstEmails=data.getCustomerreminderEmails(dbUser.getCompanyid(), selectedReminderTypes.getReminderid(),sdf.format(fromDate),sdf.format(tmpToDate));
			totalEmails="Total Emails :"  + lstEmails.size();
		}
		catch (Exception ex)
		{
			logger.error("error at ReminderEmailsViewModel>>searchCommand>> ",ex);
		}
	}
	public List<ReminderSettingsModel> getListReminderTypes() {
		return listReminderTypes;
	}

	public void setListReminderTypes(List<ReminderSettingsModel> listReminderTypes) {
		this.listReminderTypes = listReminderTypes;
	}

	public ReminderSettingsModel getSelectedReminderTypes() {
		return selectedReminderTypes;
	}

	public void setSelectedReminderTypes(ReminderSettingsModel selectedReminderTypes) {
		this.selectedReminderTypes = selectedReminderTypes;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<EmailSelectionModel> getLstEmails() {
		return lstEmails;
	}

	public void setLstEmails(List<EmailSelectionModel> lstEmails) {
		this.lstEmails = lstEmails;
	}

	public String getTotalEmails() {
		return totalEmails;
	}

	public void setTotalEmails(String totalEmails) {
		this.totalEmails = totalEmails;
	}
}
