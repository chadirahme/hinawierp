package model;

public class CustomerContact {
	
	private int cust_key;
	
	private int LineNo; 
	private String name="";
	private String position="";
	private String phone="";
	private String mobile="";
	private String extension="";
	private String fax="";
	private String email="";
	private String defaultFlag;
	private QbListsModel selectedItems;
	private String useVat;


	/**
	 * @return the defaultFlag
	 */
	public String getDefaultFlag() {
		return defaultFlag;
	}

	/**
	 * @param defaultFlag the defaultFlag to set
	 */
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	/**
	 * @return the cust_key
	 */
	public int getCust_key() {
		return cust_key;
	}

	/**
	 * @param cust_key the cust_key to set
	 */
	public void setCust_key(int cust_key) {
		this.cust_key = cust_key;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public int getLineNo() {
		return LineNo;
	}

	public void setLineNo(int lineNo) {
		LineNo = lineNo;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public QbListsModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(QbListsModel selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String getUseVat() {
		return useVat;
	}

	public void setUseVat(String useVat) {
		this.useVat = useVat;
	}
	
	
	

}
