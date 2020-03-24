package hr.model;

public class WorkGroupModel {
	
	private int recno;
	private int compKey;
	private String groupName;
	private String groupNameAR;
	private String supervisor;
	private String supervisorName;
	private String supervisorNumber;
	private String supervisorNameAR;
	private String isActive;
	private int totalNoOfEmployees;
	private int supersupervisor;
	
	private boolean wetherSupersupervisor=false;
	/**
	 * @return the recno
	 */
	//sdsd
	public int getRecno() {
		return recno;
	}
	/**
	 * @param recno the recno to set
	 */
	public void setRecno(int recno) {
		this.recno = recno;
	}
	/**
	 * @return the compKey
	 */
	public int getCompKey() {
		return compKey;
	}
	/**
	 * @param compKey the compKey to set
	 */
	public void setCompKey(int compKey) {
		this.compKey = compKey;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the groupNameAR
	 */
	public String getGroupNameAR() {
		return groupNameAR;
	}
	/**
	 * @param groupNameAR the groupNameAR to set
	 */
	public void setGroupNameAR(String groupNameAR) {
		this.groupNameAR = groupNameAR;
	}
	/**
	 * @return the supervisor
	 */
	public String getSupervisor() {
		return supervisor;
	}
	/**
	 * @param supervisor the supervisor to set
	 */
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	/**
	 * @return the supervisorName
	 */
	public String getSupervisorName() {
		return supervisorName;
	}
	/**
	 * @param supervisorName the supervisorName to set
	 */
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	/**
	 * @return the supervisorNameAR
	 */
	public String getSupervisorNameAR() {
		return supervisorNameAR;
	}
	/**
	 * @param supervisorNameAR the supervisorNameAR to set
	 */
	public void setSupervisorNameAR(String supervisorNameAR) {
		this.supervisorNameAR = supervisorNameAR;
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the totalNoOfEmployees
	 */
	public int getTotalNoOfEmployees() {
		return totalNoOfEmployees;
	}
	/**
	 * @param totalNoOfEmployees the totalNoOfEmployees to set
	 */
	public void setTotalNoOfEmployees(int totalNoOfEmployees) {
		this.totalNoOfEmployees = totalNoOfEmployees;
	}
	/**
	 * @return the supervisorNumber
	 */
	public String getSupervisorNumber() {
		return supervisorNumber;
	}
	/**
	 * @param supervisorNumber the supervisorNumber to set
	 */
	public void setSupervisorNumber(String supervisorNumber) {
		this.supervisorNumber = supervisorNumber;
	}
	public int getSupersupervisor() {
		return supersupervisor;
	}
	public void setSupersupervisor(int supersupervisor) {
		this.supersupervisor = supersupervisor;
	}
	public boolean isWetherSupersupervisor() {
		return wetherSupersupervisor;
	}
	public void setWetherSupersupervisor(boolean wetherSupersupervisor) {
		this.wetherSupersupervisor = wetherSupersupervisor;
	}
	
	

}
