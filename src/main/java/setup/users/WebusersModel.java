package setup.users;

public class WebusersModel {

	private int userid;
	private String username;
	private String userpwd;
	private String firstname;
	private String lastname;
	private String useremail;
	private String userEmailPassword;
	private String emailhost;
	private String usermobile;
	
	private String userip;
	private String dbname;
	private String dbuser;
	private String dbpwd;
	
	private int roleid;
	private int companyid;
	
	private String companyName;
	private String language;
	private int supervisor;
	private String adminuser;
	
	private int companyroleid;
	private String roleName;
	private String mergedDatabse="";//temporary code
	
	private String profileText;
	
	private int employeeKey;
	
	private String oldpwd;
	
	private String newPassWrd;
	
	private String confirmPasssword;
	
	private boolean isactive;
	private int desktopUserid;
	
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getUsermobile() {
		return usermobile;
	}
	public void setUsermobile(String usermobile) {
		this.usermobile = usermobile;
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
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getCompanyid() {
		return companyid;
	}
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(int supervisor) {
		this.supervisor = supervisor;
	}
	public String getAdminuser() {
		return adminuser;
	}
	public void setAdminuser(String adminuser) {
		this.adminuser = adminuser;
	}
	public int getCompanyroleid() {
		return companyroleid;
	}
	public void setCompanyroleid(int companyroleid) {
		this.companyroleid = companyroleid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getEmployeeKey() {
		return employeeKey;
	}
	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}
	public String getMergedDatabse() {
		return mergedDatabse;
	}
	public void setMergedDatabse(String mergedDatabse) {
		this.mergedDatabse = mergedDatabse;
	}
	public String getProfileText() {
		return profileText;
	}
	public void setProfileText(String profileText) {
		this.profileText = profileText;
	}
	public String getOldpwd() {
		return oldpwd;
	}
	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}
	public String getNewPassWrd() {
		return newPassWrd;
	}
	public void setNewPassWrd(String newPassWrd) {
		this.newPassWrd = newPassWrd;
	}
	public String getConfirmPasssword() {
		return confirmPasssword;
	}
	public void setConfirmPasssword(String confirmPasssword) {
		this.confirmPasssword = confirmPasssword;
	}
	public boolean getIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	public String getUserEmailPassword() {
		return userEmailPassword;
	}
	public void setUserEmailPassword(String userEmailPassword) {
		this.userEmailPassword = userEmailPassword;
	}
	public String getEmailhost() {
		return emailhost;
	}
	public void setEmailhost(String emailhost) {
		this.emailhost = emailhost;
	}
	public int getDesktopUserid() 
	{	
		return desktopUserid==0?1:desktopUserid;
	}
	public void setDesktopUserid(int desktopUserid) {
		this.desktopUserid = desktopUserid;
	}
	
	
	
	
}
