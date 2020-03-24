package model;

import java.util.Date;

public class CompanyDBModel 
{

	private int companyId;
	private String companyName;
	
	private int dbid;
	private String userip;
	private String dbname;
	private String dbuser;
	private String dbpwd;
	private String dbtype;
	
	private String contactname;
	private String telephone;
	private String mobile;
	private String email;
	private String mergedDatabse="";//temporary code
	
	private boolean mergeChecked=false;
	
	private String serverStatus;
	private boolean serverUp;
	
	private int maxNoUsers;
	private String ServerName;
	private boolean hr;
	private boolean accounting;
	private boolean realEstate;
	private boolean admin;
	private boolean active;
	private Date lastUpdated;

	
	
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getDbid() {
		return dbid;
	}
	public void setDbid(int dbid) {
		this.dbid = dbid;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public String getDbuser() {
		return dbuser;
	}
	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}
	public String getDbpwd() {
		return dbpwd;
	}
	public void setDbpwd(String dbpwd) {
		this.dbpwd = dbpwd;
	}
	public String getDbtype() {
		return dbtype;
	}
	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}
	public boolean isServerUp() {
		return serverUp;
	}
	public void setServerUp(boolean serverUp) {
		this.serverUp = serverUp;
	}
	public int getMaxNoUsers() {
		return maxNoUsers;
	}
	public void setMaxNoUsers(int maxNoUsers) {
		this.maxNoUsers = maxNoUsers;
	}
	public String getMergedDatabse() {
		return mergedDatabse;
	}
	public void setMergedDatabse(String mergedDatabse) {
		this.mergedDatabse = mergedDatabse;
	}
	public boolean isMergeChecked() {
		return mergeChecked;
	}
	public void setMergeChecked(boolean mergeChecked) {
		this.mergeChecked = mergeChecked;
	}
	public String getServerName() {
		return ServerName;
	}
	public void setServerName(String serverName) {
		ServerName = serverName;
	}
	public boolean getHr() {
		return hr;
	}
	public void setHr(boolean hr) {
		this.hr = hr;
	}
	public boolean getAccounting() {
		return accounting;
	}
	public void setAccounting(boolean accounting) {
		this.accounting = accounting;
	}
	public boolean getRealEstate() {
		return realEstate;
	}
	public void setRealEstate(boolean realEstate) {
		this.realEstate = realEstate;
	}
	public boolean getAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	
}
