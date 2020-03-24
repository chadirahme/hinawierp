package model;

import java.util.Date;

public class LocationModel 
{

	private int empKey;
	private int projectkey;
	private Date effDate;
	private String projectName;
	
	
	public int getEmpKey() {
		return empKey;
	}
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	public int getProjectkey() {
		return projectkey;
	}
	public void setProjectkey(int projectkey) {
		this.projectkey = projectkey;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
