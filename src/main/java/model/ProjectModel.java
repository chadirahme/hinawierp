package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class ProjectModel 
{
private Logger logger = Logger.getLogger(this.getClass());
DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
private int projectKey;
private String projectCode;
private String projectName;
private String projectArName;
private int companyKey;
private String qbListId;
private int statusId;
private String isActive;
private boolean activeStatus;
private String arActive;

private Date startDate;

private Date endDate;



public ProjectModel()
{
	Calendar c = Calendar.getInstance();	
	c.set(Calendar.DAY_OF_MONTH, 1);
	try {
		startDate=df.parse(sdf.format(c.getTime()));
		c = Calendar.getInstance();	
		endDate=df.parse(sdf.format(c.getTime()));
	} catch (ParseException e) {
		logger.error("ERROR in ProjectModel ----> init", e);			
	}
	
}
private boolean editingStatus;
private String enCodeName;
private String arCodeName;

public int getProjectKey() {
	return projectKey;
}
public void setProjectKey(int projectKey) {
	this.projectKey = projectKey;
}
public String getProjectCode() {
	return projectCode;
}
public void setProjectCode(String projectCode) {
	this.projectCode = projectCode;
}
public String getProjectName() {
	return projectName;
}
public void setProjectName(String projectName) {
	this.projectName = projectName;
}
public boolean isEditingStatus() {
	return editingStatus;
}
public void setEditingStatus(boolean editingStatus) {
	this.editingStatus = editingStatus;
}
public String getProjectArName() {
	return projectArName;
}
public void setProjectArName(String projectArName) {
	this.projectArName = projectArName;
}
public int getCompanyKey() {
	return companyKey;
}
public void setCompanyKey(int companyKey) {
	this.companyKey = companyKey;
}
public String getQbListId() {
	return qbListId;
}
public void setQbListId(String qbListId) {
	this.qbListId = qbListId;
}
public int getStatusId() {
	return statusId;
}
public void setStatusId(int statusId) {
	this.statusId = statusId;
}
public String getIsActive() {
	return isActive;
}
public void setIsActive(String isActive) {
	this.isActive = isActive;
}
public boolean isActiveStatus() {
	return activeStatus;
}
public void setActiveStatus(boolean activeStatus) {
	this.activeStatus = activeStatus;
}
public String getArActive() {
	return arActive;
}
public void setArActive(String arActive) {
	this.arActive = arActive;
}
public String getEnCodeName() {
	return enCodeName;
}
public void setEnCodeName(String enCodeName) {
	this.enCodeName = enCodeName;
}
public String getArCodeName() {
	return arCodeName;
}
public void setArCodeName(String arCodeName) {
	this.arCodeName = arCodeName;
}
public Date getStartDate() {
	return startDate;
}
public void setStartDate(Date startDate) {
	this.startDate = startDate;
}
public Date getEndDate() {
	return endDate;
}
public void setEndDate(Date endDate) {
	this.endDate = endDate;
}



}
