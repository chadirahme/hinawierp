package model;

public class ProgramModel {

	private int programid;
	private String enprogramname;
	private String arprogramname;
	private int fromgradeid;
	private int togradeid;
	private boolean editingStatus;
	
	public int getProgramid() {
		return programid;
	}
	public void setProgramid(int programid) {
		this.programid = programid;
	}
	public String getEnprogramname() {
		return enprogramname;
	}
	public void setEnprogramname(String enprogramname) {
		this.enprogramname = enprogramname;
	}
	public String getArprogramname() {
		return arprogramname;
	}
	public void setArprogramname(String arprogramname) {
		this.arprogramname = arprogramname;
	}
	public int getFromgradeid() {
		return fromgradeid;
	}
	public void setFromgradeid(int fromgradeid) {
		this.fromgradeid = fromgradeid;
	}
	public int getTogradeid() {
		return togradeid;
	}
	public void setTogradeid(int togradeid) {
		this.togradeid = togradeid;
	}
	public boolean isEditingStatus() {
		return editingStatus;
	}
	public void setEditingStatus(boolean editingStatus) {
		this.editingStatus = editingStatus;
	}
	
}
