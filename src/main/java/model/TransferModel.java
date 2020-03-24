package model;

import java.util.Date;

public class TransferModel 
{

	private int recNo;
	private int empKey;
	private int formNo;
	private int compKey;
	private int currProjectId;
	private int projectKey;
	private Date createDate;
	private Date effDate;
	private Date toDate;
	private String formName;
	private String notes;
	private String status;
	
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public int getEmpKey() {
		return empKey;
	}
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	public int getFormNo() {
		return formNo;
	}
	public void setFormNo(int formNo) {
		this.formNo = formNo;
	}
	public int getCompKey() {
		return compKey;
	}
	public void setCompKey(int compKey) {
		this.compKey = compKey;
	}
	public int getCurrProjectId() {
		return currProjectId;
	}
	public void setCurrProjectId(int currProjectId) {
		this.currProjectId = currProjectId;
	}
	public int getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(int projectKey) {
		this.projectKey = projectKey;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
