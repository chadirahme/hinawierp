package model;

import java.util.Date;

public class HRListFieldsModel 
{

	private int fieldId;
	private String fieldName;
	private String descreption;
	private String arabic;
	private String newFlag;
	private String editFlag;
	private String deleteFlag;
	private int parentlistId;
	private String required;
	private Date lastModified;	
	
	public int getFieldId() {
		return fieldId;
	}
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDescreption() {
		return descreption;
	}
	public void setDescreption(String descreption) {
		this.descreption = descreption;
	}
	public String getArabic() {
		return arabic;
	}
	public void setArabic(String arabic) {
		this.arabic = arabic;
	}
	public String getNewFlag() {
		return newFlag;
	}
	public void setNewFlag(String newFlag) {
		this.newFlag = newFlag;
	}
	public String getEditFlag() {
		return editFlag;
	}
	public void setEditFlag(String editFlag) {
		this.editFlag = editFlag;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public int getParentlistId() {
		return parentlistId;
	}
	public void setParentlistId(int parentlistId) {
		this.parentlistId = parentlistId;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	
}
