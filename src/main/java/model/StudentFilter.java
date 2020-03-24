package model;

public class StudentFilter 
{

	private String applicationNo="";	
	private String enFullName="";
	private String arFullName="";
	
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo==null?"":applicationNo;
	}
	public String getEnFullName() {
		return enFullName;
	}
	public void setEnFullName(String enFullName) {
		this.enFullName = enFullName==null?"":enFullName.trim();
	}
	public String getArFullName() {
		return arFullName;
	}
	public void setArFullName(String arFullName) {
		this.arFullName = arFullName==null?"":arFullName.trim();
	}
}
