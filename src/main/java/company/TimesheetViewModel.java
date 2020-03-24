package company;

import java.util.List;

import layout.MenuModel;
import model.CompanySettingsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;
import timesheet.TimeSheetData;

public class TimesheetViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	CompanyData data=new CompanyData();
	WebusersModel dbUser=null;
	
	private CompanySettingsModel objSettings;
	private boolean hideCalculate;
	private boolean hideService;
	private boolean mandatoryService;
	private boolean hideOverTime;
	private boolean hideTime;
	private boolean autoApprove;
	private String selectedProject;
	private String dateType;
	private boolean hideTomorrowPlan;
	private boolean mandatoryTomorrowPlan;
	private boolean hideCustomerJob;
	private boolean mandatoryCustomerJob;
	private boolean hideCustomerTask;
	private boolean mandatoryCustomerTask;
	private boolean hideAttachment;
	private boolean mandatoryAttachment;
	private String ftpHost;
	private int ftpPort;
	private String ftpUser;
	private String ftpPassword;
	private String ftpDirectory;
	
	int menuID=298;
	private MenuModel companyRole;
	
	public TimesheetViewModel()
	{
		try
		{				
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser==null)
		{
			Executions.sendRedirect("/login.zul");
		}
		
		getCompanyRolePermessions(dbUser.getCompanyroleid());
		
		objSettings=data.getCompanySettings(dbUser.getCompanyid());
		if(objSettings!=null)
		{
			hideCalculate=objSettings.isHidecalculate();
			hideService=objSettings.isHideservice();
			mandatoryService=objSettings.isMandatoryservice();
			selectedProject=objSettings.getProjecttype();
			dateType=objSettings.getDateType();
			hideOverTime=objSettings.isHideOverTime();
			autoApprove=objSettings.isAutoApprove();
			hideTime=objSettings.isHideTime();
			hideTomorrowPlan=objSettings.isHideTomorrowPlan();
			mandatoryTomorrowPlan=objSettings.isMandatoryTomorrowPlan();
			hideCustomerJob=objSettings.isHideCustomerJob();
			mandatoryCustomerJob=objSettings.isMandatoryCustomerJob();
			hideCustomerTask=objSettings.isHideCustomerTask();
			mandatoryCustomerTask=objSettings.isMandatoryCustomerTask();
			hideAttachment=objSettings.isHideAttachment();
			mandatoryAttachment=objSettings.isMandatoryAttachment();
			ftpHost=objSettings.getFtpHost();
			ftpPort=objSettings.getFtpPort();
			ftpUser=objSettings.getFtpUser();
			ftpPassword=objSettings.getFtpPassword();
			ftpDirectory=objSettings.getFtpDirectory();
		}
		
		}
		catch (Exception ex)
		{
		logger.error("error at TimesheetViewModel>>Init>> "+ex.getMessage());
		}
		
	}

	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		TimeSheetData data=new TimeSheetData();
		List<MenuModel> lstRoles= data.getTimeSheetRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				companyRole=item;
				break;
			}
		}
	}
	@Command
	public void saveCommand()
	{
		try
		{
			objSettings.setHidecalculate(hideCalculate);
			objSettings.setHideservice(hideService);
			objSettings.setMandatoryservice(mandatoryService);
			objSettings.setProjecttype(selectedProject);
			objSettings.setDateType(dateType);
			objSettings.setHideOverTime(hideOverTime);
			objSettings.setAutoApprove(autoApprove);
			objSettings.setHideTime(hideTime);
			objSettings.setHideTomorrowPlan(hideTomorrowPlan);
			objSettings.setMandatoryTomorrowPlan(mandatoryTomorrowPlan);
			objSettings.setHideCustomerJob(hideCustomerJob);
			objSettings.setMandatoryCustomerJob(mandatoryCustomerJob);
			objSettings.setHideCustomerTask(hideCustomerTask);
			objSettings.setMandatoryCustomerTask(mandatoryCustomerTask);
			objSettings.setHideAttachment(hideAttachment);
			objSettings.setMandatoryAttachment(mandatoryAttachment);
			objSettings.setFtpHost(ftpHost);
			objSettings.setFtpPort(ftpPort);
			objSettings.setFtpUser(ftpUser);
			objSettings.setFtpPassword(ftpPassword);
			objSettings.setFtpDirectory(ftpDirectory);
			data.updateCompanySettings(objSettings);
			
			Messagebox.show("Timesheet settings saved..","Timesheet", Messagebox.OK , Messagebox.INFORMATION);
		}
		catch (Exception ex)
		{
		logger.error("error at TimesheetViewModel>>saveCommand>> "+ex.getMessage());
		}
	}
	
	public String getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(String selectedProject) {
		this.selectedProject = selectedProject;
	}

	public CompanySettingsModel getObjSettings() {
		return objSettings;
	}

	public void setObjSettings(CompanySettingsModel objSettings) {
		this.objSettings = objSettings;
	}

	public boolean isHideCalculate() {
		return hideCalculate;
	}

	public void setHideCalculate(boolean hieCalculate) {
		this.hideCalculate = hieCalculate;
	}

	public boolean isHideService() {
		return hideService;
	}

	@NotifyChange({"mandatoryService","hideService"})
	public void setHideService(boolean hideService)
	{
		this.hideService = hideService;
		if(this.hideService)
		{
			this.mandatoryService=false;
		}
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public boolean isHideOverTime() {
		return hideOverTime;
	}

	public void setHideOverTime(boolean hideOverTime) {
		this.hideOverTime = hideOverTime;
	}

	public boolean isAutoApprove() {
		return autoApprove;
	}

	public void setAutoApprove(boolean autoApprove) {
		this.autoApprove = autoApprove;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public boolean isHideTime() {
		return hideTime;
	}

	public void setHideTime(boolean hideTime) {
		this.hideTime = hideTime;
	}

	public boolean isHideTomorrowPlan() {
		return hideTomorrowPlan;
	}

	@NotifyChange({"mandatoryTomorrowPlan","hideTomorrowPlan"})
	public void setHideTomorrowPlan(boolean hideTomorrowPlan) 
	{
		this.hideTomorrowPlan = hideTomorrowPlan;
		if(this.hideTomorrowPlan)
		{
			this.mandatoryTomorrowPlan=false;
		}
	}

	public boolean isMandatoryTomorrowPlan() {
		return mandatoryTomorrowPlan;
	}

	public void setMandatoryTomorrowPlan(boolean mandatoryTomorrowPlan) {
		this.mandatoryTomorrowPlan = mandatoryTomorrowPlan;
	}

	public boolean isHideCustomerJob() {
		return hideCustomerJob;
	}
	@NotifyChange({"mandatoryCustomerJob","hideCustomerJob","hideCustomerTask","mandatoryCustomerTask"})
	public void setHideCustomerJob(boolean hideCustomerJob) 
	{
		this.hideCustomerJob = hideCustomerJob;
		if(this.hideCustomerJob)
		{
			this.mandatoryCustomerJob=false;
			this.hideCustomerTask=true;
			this.mandatoryCustomerTask=false;
		}
	}

	public boolean isMandatoryCustomerJob() {
		return mandatoryCustomerJob;
	}

	public void setMandatoryCustomerJob(boolean mandatoryCustomerJob) {
		this.mandatoryCustomerJob = mandatoryCustomerJob;
	}

	public boolean isHideCustomerTask() {
		return hideCustomerTask;
	}

	@NotifyChange({"mandatoryCustomerTask","hideCustomerTask"})
	public void setHideCustomerTask(boolean hideCustomerTask) 
	{
		this.hideCustomerTask = hideCustomerTask;
		if(this.hideCustomerTask)
		{
			this.mandatoryCustomerTask=false;
		}
	}

	public boolean isMandatoryCustomerTask() {
		return mandatoryCustomerTask;
	}

	public void setMandatoryCustomerTask(boolean mandatoryCustomerTask) {
		this.mandatoryCustomerTask = mandatoryCustomerTask;
	}

	public boolean isHideAttachment() {
		return hideAttachment;
	}

	@NotifyChange({"mandatoryAttachment","hideAttachment"})
	public void setHideAttachment(boolean hideAttachment) 
	{
		this.hideAttachment = hideAttachment;
		if(this.hideAttachment)
		{
			this.mandatoryAttachment=false;
		}
	}

	public boolean isMandatoryAttachment() {
		return mandatoryAttachment;
	}

	public void setMandatoryAttachment(boolean mandatoryAttachment) {
		this.mandatoryAttachment = mandatoryAttachment;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getFtpDirectory() {
		return ftpDirectory;
	}

	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}

	public boolean isMandatoryService() {
		return mandatoryService;
	}

	public void setMandatoryService(boolean mandatoryService) {
		this.mandatoryService = mandatoryService;
	}
}
