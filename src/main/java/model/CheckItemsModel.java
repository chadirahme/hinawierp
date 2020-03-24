package model;

import java.util.Date;

public class CheckItemsModel 
{
	private int recNo;
	private int LineNo;
	private QbListsModel selectedItems;
	private int itemKey;
	private String Description;
	private double Quantity;
	private double Cost;
	private double Cost1;
	private double Amount;
	private QbListsModel selectedCustomer;
	private ClassModel selectedClass;
	private FixedAssetModel selectedFixedAsset;
	private Date InvoiceDate;
	private int InventorySiteKey;
	private String billNo;
	private String itemReceiptNo;
	private Date itemReceiptDate;
	private QbListsModel selectedInvcCutomerGridInvrtySiteNew;
	private boolean hideSite=false;
	//receipt item module
	private int selectedCustKey;
	private int selectedClassKey;
	private String Memo;
	private String status;
	private String IsAllocated="";
	private double  Allocated_Amount;
	private double  Allocated_UnitCost;
	private double  NetTotal;
	private String Billable="";
	private boolean billableChked=false;
	private boolean showBillable=false;
	private int fixedIteKey;
	private String itemType="";
	private int pORecNo;
	private int recivedQuantity;
	private int pOLineNo;

	private boolean readOnly=false;
	private int quantityInHand;

	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public int getLineNo() {
		return LineNo;
	}
	public void setLineNo(int lineNo) {
		LineNo = lineNo;
	}
	public int getItemKey() {
		return itemKey;
	}
	public void setItemKey(int itemKey) {
		this.itemKey = itemKey;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	public double getCost() {
		return Cost;
	}
	public void setCost(double cost) {
		Cost = cost;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public QbListsModel getSelectedCustomer() {
		return selectedCustomer;
	}
	public void setSelectedCustomer(QbListsModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}
	public ClassModel getSelectedClass() {
		return selectedClass;
	}
	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}
	public FixedAssetModel getSelectedFixedAsset() {
		return selectedFixedAsset;
	}
	public void setSelectedFixedAsset(FixedAssetModel selectedFixedAsset) {
		this.selectedFixedAsset = selectedFixedAsset;
	}
	public Date getInvoiceDate() {
		return InvoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		InvoiceDate = invoiceDate;
	}
	public int getInventorySiteKey() {
		return InventorySiteKey;
	}
	public void setInventorySiteKey(int inventorySiteKey) {
		InventorySiteKey = inventorySiteKey;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public QbListsModel getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(QbListsModel selectedItems) {
		this.selectedItems = selectedItems;
	}
	public String getItemReceiptNo() {
		return itemReceiptNo;
	}
	public void setItemReceiptNo(String itemReceiptNo) {
		this.itemReceiptNo = itemReceiptNo;
	}
	public Date getItemReceiptDate() {
		return itemReceiptDate;
	}
	public void setItemReceiptDate(Date itemReceiptDate) {
		this.itemReceiptDate = itemReceiptDate;
	}
	public QbListsModel getSelectedInvcCutomerGridInvrtySiteNew() {
		return selectedInvcCutomerGridInvrtySiteNew;
	}
	public void setSelectedInvcCutomerGridInvrtySiteNew(
			QbListsModel selectedInvcCutomerGridInvrtySiteNew) {
		this.selectedInvcCutomerGridInvrtySiteNew = selectedInvcCutomerGridInvrtySiteNew;
	}
	public boolean isHideSite() {
		return hideSite;
	}
	public void setHideSite(boolean hideSite) {
		this.hideSite = hideSite;
	}
	public int getSelectedCustKey() {
		return selectedCustKey;
	}
	public void setSelectedCustKey(int selectedCustKey) {
		this.selectedCustKey = selectedCustKey;
	}
	public int getSelectedClassKey() {
		return selectedClassKey;
	}
	public void setSelectedClassKey(int selectedClassKey) {
		this.selectedClassKey = selectedClassKey;
	}
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsAllocated() {
		return IsAllocated;
	}
	public void setIsAllocated(String isAllocated) {
		IsAllocated = isAllocated;
	}
	public double getAllocated_Amount() {
		return Allocated_Amount;
	}
	public void setAllocated_Amount(double allocated_Amount) {
		Allocated_Amount = allocated_Amount;
	}
	public double getAllocated_UnitCost() {
		return Allocated_UnitCost;
	}
	public void setAllocated_UnitCost(double allocated_UnitCost) {
		Allocated_UnitCost = allocated_UnitCost;
	}
	public double getNetTotal() {
		return NetTotal;
	}
	public void setNetTotal(double netTotal) {
		NetTotal = netTotal;
	}
	public String getBillable() {
		return Billable;
	}
	public void setBillable(String billable) {
		Billable = billable;
	}
	public boolean isBillableChked() {
		return billableChked;
	}
	public void setBillableChked(boolean billableChked) {
		this.billableChked = billableChked;
	}
	public int getFixedIteKey() {
		return fixedIteKey;
	}
	public void setFixedIteKey(int fixedIteKey) {
		this.fixedIteKey = fixedIteKey;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public boolean isShowBillable() {
		return showBillable;
	}
	public void setShowBillable(boolean showBillable) {
		this.showBillable = showBillable;
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
	public int getQuantityInHand() {
		return quantityInHand;
	}
	public void setQuantityInHand(int quantityInHand) {
		this.quantityInHand = quantityInHand;
	}
	public int getRecivedQuantity() {
		return recivedQuantity;
	}
	public void setRecivedQuantity(int recivedQuantity) {
		this.recivedQuantity = recivedQuantity;
	}
	public double getCost1() {
		return Cost1;
	}
	public void setCost1(double cost1) {
		Cost1 = cost1;
	}




}
