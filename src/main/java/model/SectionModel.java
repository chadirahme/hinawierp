package model;

public class SectionModel 
{
	private int sectionId;
	private String enSectionName;
	private String arSectionName;
	private boolean editingStatus;
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public String getEnSectionName() {
		return enSectionName;
	}
	public void setEnSectionName(String enSectionName) {
		this.enSectionName = enSectionName;
	}
	public String getArSectionName() {
		return arSectionName;
	}
	public void setArSectionName(String arSectionName) {
		this.arSectionName = arSectionName;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	
	
}
