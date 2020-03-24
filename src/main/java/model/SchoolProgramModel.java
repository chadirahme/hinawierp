package model;

public class SchoolProgramModel 
{
	private int schoolProgramId;
	private int schoolId;
	private int programId;
	private int fromGradeId;
	private int toGradeId;
	private int gender;
	
	private String schoolName;
	private String fromGradeName;
	private String toGradeName;
	private String programName;
	private boolean editingStatus;
	
	private String genderName;
	
	public int getSchoolProgramId() {
		return schoolProgramId;
	}
	public void setSchoolProgramId(int schoolProgramId) {
		this.schoolProgramId = schoolProgramId;
	}
	public int getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}
	public int getProgramId() {
		return programId;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
	}
	public int getFromGradeId() {
		return fromGradeId;
	}
	public void setFromGradeId(int fromGradeId) {
		this.fromGradeId = fromGradeId;
	}
	public int getToGradeId() {
		return toGradeId;
	}
	public void setToGradeId(int toGradeId) {
		this.toGradeId = toGradeId;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getFromGradeName() {
		return fromGradeName;
	}
	public void setFromGradeName(String fromGradeName) {
		this.fromGradeName = fromGradeName;
	}
	public String getToGradeName() {
		return toGradeName;
	}
	public void setToGradeName(String toGradeName) {
		this.toGradeName = toGradeName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getGenderName() {
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
	
	
}
