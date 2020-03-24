package model;

public class CompanyRoleModel 
{

	private int companyroleid;
	private int companyid;
	private String rolename;
	
	public CompanyRoleModel()
	{
		
	}
	public CompanyRoleModel(int companyroleid,int companyid,String roleName)
	{
		this.companyroleid=companyroleid;
		this.companyid=companyid;
		this.rolename=roleName;
	}
	public int getCompanyroleid() {
		return companyroleid;
	}
	public void setCompanyroleid(int companyroleid) {
		this.companyroleid = companyroleid;
	}
	public int getCompanyid() {
		return companyid;
	}
	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	
}
