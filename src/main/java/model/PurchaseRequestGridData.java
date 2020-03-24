package model;

public class PurchaseRequestGridData {
	
	
	private int recNo;
	
	private int lineNo;
	
	private int itemrefKey;
	
	private String decription;
	
	private int quantity;
	
	private int quantityInHand;
	
	private double rate;
	
	private double amount;
	
	private int entityRefKey;
	
	private int recivedQuantity;
	
	private int mimRecivedQuantity;
	
	private String isOrderd;
	
	private String ispo_source;
	
	private int isRecNO;
	
	private int isPoLinenumber;
	
	private QbListsModel selectedItem;
	
	private ClassModel slectedClass;
	
	private QbListsModel selctedCustomer;
	
	private int pORecNo;
	
	private int pOLineNo;
	
	private boolean readOnly=false;
	
	
	
	

	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getItemrefKey() {
		return itemrefKey;
	}

	public void setItemrefKey(int itemrefKey) {
		this.itemrefKey = itemrefKey;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getEntityRefKey() {
		return entityRefKey;
	}

	public void setEntityRefKey(int entityRefKey) {
		this.entityRefKey = entityRefKey;
	}

	public int getRecivedQuantity() {
		return recivedQuantity;
	}

	public void setRecivedQuantity(int recivedQuantity) {
		this.recivedQuantity = recivedQuantity;
	}

	public int getMimRecivedQuantity() {
		return mimRecivedQuantity;
	}

	public void setMimRecivedQuantity(int mimRecivedQuantity) {
		this.mimRecivedQuantity = mimRecivedQuantity;
	}

	public String getIsOrderd() {
		return isOrderd;
	}

	public void setIsOrderd(String isOrderd) {
		this.isOrderd = isOrderd;
	}

	public String getIspo_source() {
		return ispo_source;
	}

	public void setIspo_source(String ispo_source) {
		this.ispo_source = ispo_source;
	}

	public int getIsRecNO() {
		return isRecNO;
	}

	public void setIsRecNO(int isRecNO) {
		this.isRecNO = isRecNO;
	}

	public int getIsPoLinenumber() {
		return isPoLinenumber;
	}

	public void setIsPoLinenumber(int isPoLinenumber) {
		this.isPoLinenumber = isPoLinenumber;
	}

	public QbListsModel getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(QbListsModel selectedItem) {
		this.selectedItem = selectedItem;
	}

	public ClassModel getSlectedClass() {
		return slectedClass;
	}

	public void setSlectedClass(ClassModel slectedClass) {
		this.slectedClass = slectedClass;
	}

	public QbListsModel getSelctedCustomer() {
		return selctedCustomer;
	}

	public void setSelctedCustomer(QbListsModel selctedCustomer) {
		this.selctedCustomer = selctedCustomer;
	}

	public int getQuantityInHand() {
		return quantityInHand;
	}

	public void setQuantityInHand(int quantityInHand) {
		this.quantityInHand = quantityInHand;
	}

	public int getpORecNo() {
		return pORecNo;
	}

	public void setpORecNo(int pORecNo) {
		this.pORecNo = pORecNo;
	}

	public int getpOLineNo() {
		return pOLineNo;
	}

	public void setpOLineNo(int pOLineNo) {
		this.pOLineNo = pOLineNo;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	
	
	
	
}
