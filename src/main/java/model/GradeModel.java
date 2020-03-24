package model;

public class GradeModel 
{

	private int gradeId;
	private String enGradeName;
	private String arGradeName;
	private boolean editingStatus;
	
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public String getEnGradeName() {
		return enGradeName;
	}
	public void setEnGradeName(String enGradeName) {
		this.enGradeName = enGradeName;
	}
	public String getArGradeName() {
		return arGradeName;
	}
	public void setArGradeName(String arGradeName) {
		this.arGradeName = arGradeName;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	
	
}
