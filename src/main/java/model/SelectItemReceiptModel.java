package model;

public class SelectItemReceiptModel {
	private int pORecNo;
	private int lineNo;
	private int itemRefKey;
	private String description;
	private double quantity;
	private double billedQuantity;
	private double remainingQuantity;
	private double cost;
	private double amount;
	private String ItemName;
	private String vendorName;
	private String irNo;
	private String txnDate;
	private String memo;
	private String level;
	private boolean show;
	private int custkey;
	private String irNoLocal;
	private int accountNumber;
	private String accName;
	private String accType;
	
	
	
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
	public double getBilledQuantity() {
		return billedQuantity;
	}
	public void setBilledQuantity(double rcvdQuantity) {
		this.billedQuantity = rcvdQuantity;
	}
	public double getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(double remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
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
	public String getIrNo() {
		return irNo;
	}
	public void setIrNo(String irNo) {
		this.irNo = irNo;
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
	public int getCustkey() {
		return custkey;
	}
	public void setCustkey(int custkey) {
		this.custkey = custkey;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public String getIrNoLocal() {
		return irNoLocal;
	}
	public void setIrNoLocal(String irNoLocal) {
		this.irNoLocal = irNoLocal;
	}
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}

}
