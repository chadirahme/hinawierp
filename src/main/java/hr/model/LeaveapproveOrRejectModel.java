package hr.model;

import java.util.Date;

public class LeaveapproveOrRejectModel {
	
	
	private int id;
	
	private int emp_key;
	
	private String status;
	
	private String leaveCreateDate;
	
	private String leaveStartDate;
	
	private String leaveEndDate;
	
	private int leaveId;
	private String leaveType;
	
	private int reasonId;
	private String leaveReason;
	
	private String empName;
	
	private int supervisorID;
	
	private String empNo;
	
	private String reason; //notes
	
	
	private int leaveDays;
	
	private String attachment_path;
	
	private Date leaveFromDate;
	private Date leaveToDate;
	private String enCashStatus;
	private String otherLeaveReason;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmp_key() {
		return emp_key;
	}

	public void setEmp_key(int emp_key) {
		this.emp_key = emp_key;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLeaveCreateDate() {
		return leaveCreateDate;
	}

	public void setLeaveCreateDate(String leaveCreateDate) {
		this.leaveCreateDate = leaveCreateDate;
	}

	public String getLeaveStartDate() {
		return leaveStartDate;
	}

	public void setLeaveStartDate(String leaveStartDate) {
		this.leaveStartDate = leaveStartDate;
	}

	public String getLeaveEndDate() {
		return leaveEndDate;
	}

	public void setLeaveEndDate(String leaveEndDate) {
		this.leaveEndDate = leaveEndDate;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public int getSupervisorID() {
		return supervisorID;
	}

	public void setSupervisorID(int supervisorID) {
		this.supervisorID = supervisorID;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the leaveDays
	 */
	public int getLeaveDays() {
		return leaveDays;
	}

	/**
	 * @param leaveDays the leaveDays to set
	 */
	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}

	public String getAttachment_path() {
		return attachment_path;
	}

	public void setAttachment_path(String attachment_path) {
		this.attachment_path = attachment_path;
	}

	public Date getLeaveFromDate() {
		return leaveFromDate;
	}

	public void setLeaveFromDate(Date leaveFromDate) {
		this.leaveFromDate = leaveFromDate;
	}

	public Date getLeaveToDate() {
		return leaveToDate;
	}

	public void setLeaveToDate(Date leaveToDate) {
		this.leaveToDate = leaveToDate;
	}

	public int getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}

	public int getReasonId() {
		return reasonId;
	}

	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}

	public String getEnCashStatus() {
		return enCashStatus;
	}

	public void setEnCashStatus(String enCashStatus) {
		this.enCashStatus = enCashStatus;
	}

	public String getOtherLeaveReason() {
		return otherLeaveReason;
	}

	public void setOtherLeaveReason(String otherLeaveReason) {
		this.otherLeaveReason = otherLeaveReason;
	}
	
	
	
	

}
