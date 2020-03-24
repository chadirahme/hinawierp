package model;

public class ProspectiveContactDetailsModel {
	private int LineNo;
	private Double recNo; 
	private String name="";
	private String position="";
	private String tel="";
	private String mobile="";
	private String extension="";
	private String fax="";
	private String email="";
	private String dob="";
	private QbListsModel selectedItems;
	private String defaultFlag;

	public Double getRecNo() {
		return recNo;
	}

	public void setRecNo(Double recNo) {
		this.recNo = recNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public QbListsModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(QbListsModel selectedItems) {
		this.selectedItems = selectedItems;
	}

	public int getLineNo() {
		return LineNo;
	}

	public void setLineNo(int lineNo) {
		LineNo = lineNo;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

}
