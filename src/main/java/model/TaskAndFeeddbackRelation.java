package model;

public class TaskAndFeeddbackRelation {
	
	private int taskID;
	
	private int taskStatusId;
	
	private int userId;
	
	private String taskName;
	
	private String taskStatus;
	
	private String taskNo;
	
	private String createdDateStr;
	
	int feedbackKey;
	
	private String userName; 
	

	/**
	 * @return the taskID
	 */
	public int getTaskID() {
		return taskID;
	}

	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	/**
	 * @return the taskStatusId
	 */
	public int getTaskStatusId() {
		return taskStatusId;
	}

	/**
	 * @param taskStatusId the taskStatusId to set
	 */
	public void setTaskStatusId(int taskStatusId) {
		this.taskStatusId = taskStatusId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * @return the taskStatus
	 */
	public String getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus the taskStatus to set
	 */
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the taskNo
	 */
	public String getTaskNo() {
		return taskNo;
	}

	/**
	 * @param taskNo the taskNo to set
	 */
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	/**
	 * @return the createdDateStr
	 */
	public String getCreatedDateStr() {
		return createdDateStr;
	}

	/**
	 * @param createdDateStr the createdDateStr to set
	 */
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	/**
	 * @return the feedbackKey
	 */
	public int getFeedbackKey() {
		return feedbackKey;
	}

	/**
	 * @param feedbackKey the feedbackKey to set
	 */
	public void setFeedbackKey(int feedbackKey) {
		this.feedbackKey = feedbackKey;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	

}
