package model;

public class ApprovePurchaseOrderModel {
	private int pORecNo;
	private int lineNo;
	private int itemRefKey;
	private String description;
	private double quantity;
	private double rcvdQuantity;
	private double remainingQuantity;
	private double rate;
	private double amount;
	private String ItemName;
	private String vendorName;
	private String refNumber;
	private String txnDate;
	private String memo;
	private String level;
	private boolean show;
	private int entityRefKey;
	public int getpORecNo() {
		return pORecNo;
	}
	public void setpORecNo(int pORecNo) {
		this.pORecNo = pORecNo;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getItemRefKey() {
		return itemRefKey;
	}
	public void setItemRefKey(int itemRefKey) {
		this.itemRefKey = itemRefKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getRcvdQuantity() {
		return rcvdQuantity;
	}
	public void setRcvdQuantity(double rcvdQuantity) {
		this.rcvdQuantity = rcvdQuantity;
	}
	public double getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(double remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
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
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getRefNumber() {
		return refNumber;
	}
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public int getEntityRefKey() {
		return entityRefKey;
	}
	public void setEntityRefKey(int entityRefKey) {
		this.entityRefKey = entityRefKey;
	}
	
	
	

}
