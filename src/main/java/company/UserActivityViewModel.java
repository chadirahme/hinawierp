package company;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.HRListValuesModel;
import model.UserActivityModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

public class UserActivityViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	UserActivityData data=new UserActivityData();
	WebusersModel dbUser=null;
	
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private List<UserActivityModel> lstActivity;
	private List<String> lstUsers;
	private String selectedUser;
	private List<HRListValuesModel> lstActivityType;
	private HRListValuesModel selectedActivityType;
	
	public UserActivityViewModel()
	{
		try
		{
			Calendar c = Calendar.getInstance();	
			fromDate=df.parse(sdf.format(c.getTime()));		
			toDate=df.parse(sdf.format(c.getTime()));
			
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			
			lstUsers=data.getUsersList(dbUser.getCompanyid());
			selectedUser=lstUsers.get(0);
			
			fillFilter();
		}
		
		catch (Exception ex)
		{
		logger.error("error at UserActivityViewModel>>Init>> "+ex.getMessage());
		}
	}
	
	private void fillFilter()
	{
		lstActivityType=new ArrayList<HRListValuesModel>();
		HRListValuesModel obj=new HRListValuesModel();
		obj.setFieldId(0);
		obj.setEnDescription("All");
		lstActivityType.add(obj);
		
		obj=new HRListValuesModel();
		obj.setFieldId(68);
		obj.setEnDescription("Timesheet Detailed");
		lstActivityType.add(obj);
		
		obj=new HRListValuesModel();
		obj.setFieldId(49);
		obj.setEnDescription("Leave Request");
		lstActivityType.add(obj);
		
		obj=new HRListValuesModel();
		obj.setFieldId(47);
		obj.setEnDescription("Passport Request");
		lstActivityType.add(obj);
		
		obj=new HRListValuesModel();
		obj.setFieldId(55);
		obj.setEnDescription("Loan Request");
		lstActivityType.add(obj);	
		
		obj=new HRListValuesModel();
		obj.setFieldId(75);
		obj.setEnDescription("Timesheet Salary Sheet");
		lstActivityType.add(obj);
		
		selectedActivityType=lstActivityType.get(0);
	}
	
	
	@Command
	@NotifyChange({"lstActivity"})
	public void viewCommand()
	{
		try
		{
		lstActivity=data.getUsersActivity(fromDate,toDate,selectedUser,selectedActivityType.getFieldId());
			
		}
		catch (Exception ex)
		{
		logger.error("error at UserActivityViewModel>>viewCommand>> "+ex.getMessage());
		}
	}
	
	@Command
	@NotifyChange({"lstUsers","selectedUser"})
	public void refreshNew()
	{
		lstUsers=data.getUsersList(dbUser.getCompanyid());
		selectedUser=lstUsers.get(0);
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

	public List<UserActivityModel> getLstActivity() {
		return lstActivity;
	}

	public void setLstActivity(List<UserActivityModel> lstActivity) {
		this.lstActivity = lstActivity;
	}

	public List<String> getLstUsers() {
		return lstUsers;
	}

	public void setLstUsers(List<String> lstUsers) {
		this.lstUsers = lstUsers;
	}

	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<HRListValuesModel> getLstActivityType() {
		return lstActivityType;
	}

	public void setLstActivityType(List<HRListValuesModel> lstActivityType) {
		this.lstActivityType = lstActivityType;
	}

	public HRListValuesModel getSelectedActivityType() {
		return selectedActivityType;
	}

	public void setSelectedActivityType(HRListValuesModel selectedActivityType) {
		this.selectedActivityType = selectedActivityType;
	}
}
