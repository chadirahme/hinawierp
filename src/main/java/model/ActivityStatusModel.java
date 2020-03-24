package model;

import java.util.Date;

public class ActivityStatusModel {
	private int recNo;
	private int activity;
	private int activityRecNo;
    private String status=""; 
    private String description="";
    private Date statusDate;	
    private String memo="";
    private String active="";
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public int getActivity() {
		return activity;
	}
	public void setActivity(int activity) {
		this.activity = activity;
	}
	public int getActivityRecNo() {
		return activityRecNo;
	}
	public void setActivityRecNo(int activityRecNo) {
		this.activityRecNo = activityRecNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}

    

}
