package model;

public class CompanySettingsModel 
{

	private int settingid;
	private int companyid;
	private boolean hidecalculate;
	private boolean hideservice;
	private boolean mandatoryservice;
	private boolean hideOverTime;
	private boolean hideTime;
	private boolean autoApprove;
	private String projecttype;
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
	
	
	public int getSettingid() {
		return settingid;
	}
	public void setSettingid(int settingid) {
		this.settingid = settingid;
	}
	public int getCompanyid() {
		return companyid;
	}
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	public boolean isHidecalculate() {
		return hidecalculate;
	}
	public void setHidecalculate(boolean hidecalculate) {
		this.hidecalculate = hidecalculate;
	}
	public boolean isHideservice() {
		return hideservice;
	}
	public void setHideservice(boolean hideservice) {
		this.hideservice = hideservice;
	}
	public String getProjecttype() {
		return projecttype;
	}
	public void setProjecttype(String projecttype) {
		this.projecttype = projecttype;
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
	public boolean isHideTime() {
		return hideTime;
	}
	public void setHideTime(boolean hideTime) {
		this.hideTime = hideTime;
	}
	public boolean isHideTomorrowPlan() {
		return hideTomorrowPlan;
	}
	public void setHideTomorrowPlan(boolean hideTomorrowPlan) {
		this.hideTomorrowPlan = hideTomorrowPlan;
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
	public void setHideCustomerJob(boolean hideCustomerJob) {
		this.hideCustomerJob = hideCustomerJob;
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
	public void setHideCustomerTask(boolean hideCustomerTask) {
		this.hideCustomerTask = hideCustomerTask;
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
	public void setHideAttachment(boolean hideAttachment) {
		this.hideAttachment = hideAttachment;
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
	public boolean isMandatoryservice() {
		return mandatoryservice;
	}
	public void setMandatoryservice(boolean mandatoryservice) {
		this.mandatoryservice = mandatoryservice;
	}
	
	
	
}
