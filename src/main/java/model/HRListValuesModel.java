package model;

public class HRListValuesModel 
{

	private int listId;
	private String enDescription;
	private String arDescription;
	private int subId;
	private int fieldId;
	private String fieldName;
	private String required;
	private int priorityId;
	private String qbListId;
	private String notes="";
	private String modifeldDateStr;
	
	private boolean editingStatus;
	private HRListValuesModel selectedHRValue;
	
	private int employeeAllowed;
	
	public int getListId() {
		return listId;
	}
	public void setListId(int listId) {
		this.listId = listId;
	}
	public String getEnDescription() {
		return enDescription;
	}
	public void setEnDescription(String enDescription) {
		this.enDescription = enDescription;
	}
	public String getArDescription() {
		return arDescription;
	}
	public void setArDescription(String arDescription) {
		this.arDescription = arDescription;
	}
	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
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
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public int getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(int priorityId) {
		this.priorityId = priorityId;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	public String getQbListId() {
		return qbListId;
	}
	public void setQbListId(String qbListId) {
		this.qbListId = qbListId;
	}
	public HRListValuesModel getSelectedHRValue() {
		return selectedHRValue;
	}
	public void setSelectedHRValue(HRListValuesModel selectedHRValue) {
		this.selectedHRValue = selectedHRValue;
	}
	public int getEmployeeAllowed() {
		return employeeAllowed;
	}
	public void setEmployeeAllowed(int employeeAllowed) {
		this.employeeAllowed = employeeAllowed;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getModifeldDateStr() {
		return modifeldDateStr;
	}
	public void setModifeldDateStr(String modifeldDateStr) {
		this.modifeldDateStr = modifeldDateStr;
	}
	
	
}
