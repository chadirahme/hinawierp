package model;

public class AppraisalModel {

	public AppraisalModel()
	{
		
	}
	public AppraisalModel(String title)
	{
		this.title=title;
	}
	private String title;	
	private boolean poor;
	private boolean satisfactory;
	private boolean good;
	private boolean excellent;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isPoor() {
		return poor;
	}
	public void setPoor(boolean poor) {
		this.poor = poor;
	}
	public boolean isSatisfactory() {
		return satisfactory;
	}
	public void setSatisfactory(boolean satisfactory) {
		this.satisfactory = satisfactory;
	}
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public boolean isExcellent() {
		return excellent;
	}
	public void setExcellent(boolean excellent) {
		this.excellent = excellent;
	}
}
