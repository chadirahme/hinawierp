package model;

public class SectionsInGradeModel 
{
	private int sgId;
	private int schoolId;
	private int programId;
	private int gradeId;
	private int sectionId;
	private int maxStudents;
	
	
	private String enGradeName;
	private String enSectionName;
	
	public int getSgId() {
		return sgId;
	}
	public void setSgId(int sgId) {
		this.sgId = sgId;
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
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public int getSectionId() {
		return sectionId;
	}
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}
	public int getMaxStudents() {
		return maxStudents;
	}
	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}
	public String getEnGradeName() {
		return enGradeName;
	}
	public void setEnGradeName(String enGradeName) {
		this.enGradeName = enGradeName;
	}
	public String getEnSectionName() {
		return enSectionName;
	}
	public void setEnSectionName(String enSectionName) {
		this.enSectionName = enSectionName;
	}
	
	
}
