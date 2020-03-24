package model;

import java.util.Date;

public class DeliveryLineModel {
	private int recNo;
	private int lineNo;
	private int itemRefKey;
	private String description="";
	private int quantity;
	private	int quantityInvoice;
	private int quantityPost;
	private double rate;
	private double avgCost;
	private double ratePercent;
	private int priceLevelRefKey;
	private int classRefKey;
	private double amount;
	private Date serviceDate;
	private int salesTaxCodeRefKey;
	private String isTaxable="";
	private int overrideItemAccountRefKey;
	private String other1="";
	private String other2="";
	private int quotationNo;
	private int quotationLineNo;
	private int invertySiteKey;
	private String itemName="";
	private String itemNameAR="";
	private String itemType="";
	private int incomeAccountRef;
	private String className="";
	private String itemClassAR="";
	private int invoiceRecNo;
	private String txnType="";
	private int invoiceLineNo;
	private String inventorySite="";
	private String inventorySiteAR="";
	private boolean hideSite=false;
	private QbListsModel selectedItems;
	private QbListsModel selectedInvcCutomerGridInvrtySiteNew;
	private QbListsModel selectedInvcCutomerGridInvrtyClassNew;
	private boolean showCheck;
	
	
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
	public int getItemRefKey() {
		return itemRefKey;
	}
	public void setItemRefKey(int itemRefKey) {
		this.itemRefKey = itemRefKey;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String discription) {
		this.description = discription;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getQuantityInvoice() {
		return quantityInvoice;
	}
	public void setQuantityInvoice(int quantityInvoice) {
		this.quantityInvoice = quantityInvoice;
	}
	public int getQuantityPost() {
		return quantityPost;
	}
	public void setQuantityPost(int quantityPost) {
		this.quantityPost = quantityPost;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getAvgCost() {
		return avgCost;
	}
	public void setAvgCost(double avgCost) {
		this.avgCost = avgCost;
	}
	public double getRatePercent() {
		return ratePercent;
	}
	public void setRatePercent(double ratePercent) {
		this.ratePercent = ratePercent;
	}
	public int getPriceLevelRefKey() {
		return priceLevelRefKey;
	}
	public void setPriceLevelRefKey(int priceLevelRefKey) {
		this.priceLevelRefKey = priceLevelRefKey;
	}
	public int getClassRefKey() {
		return classRefKey;
	}
	public void setClassRefKey(int classRefKey) {
		this.classRefKey = classRefKey;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}
	public int getSalesTaxCodeRefKey() {
		return salesTaxCodeRefKey;
	}
	public void setSalesTaxCodeRefKey(int salesTaxCodeRefKey) {
		this.salesTaxCodeRefKey = salesTaxCodeRefKey;
	}
	public String getIsTaxable() {
		return isTaxable;
	}
	public void setIsTaxable(String isTaxable) {
		this.isTaxable = isTaxable;
	}
	public int getOverrideItemAccountRefKey() {
		return overrideItemAccountRefKey;
	}
	public void setOverrideItemAccountRefKey(int overrideItemAccountRefKey) {
		this.overrideItemAccountRefKey = overrideItemAccountRefKey;
	}
	public String getOther1() {
		return other1;
	}
	public void setOther1(String other1) {
		this.other1 = other1;
	}
	public String getOther2() {
		return other2;
	}
	public void setOther2(String other2) {
		this.other2 = other2;
	}
	public int getQuotationLineNo() {
		return quotationLineNo;
	}
	public void setQuotationLineNo(int quotationLineNo) {
		this.quotationLineNo = quotationLineNo;
	}
	public int getInvertySiteKey() {
		return invertySiteKey;
	}
	public void setInvertySiteKey(int invertySiteKey) {
		this.invertySiteKey = invertySiteKey;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemNameAR() {
		return itemNameAR;
	}
	public void setItemNameAR(String itemNameAR) {
		this.itemNameAR = itemNameAR;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public int getIncomeAccountRef() {
		return incomeAccountRef;
	}
	public void setIncomeAccountRef(int incomeAccountRef) {
		this.incomeAccountRef = incomeAccountRef;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getItemClassAR() {
		return itemClassAR;
	}
	public void setItemClassAR(String itemClassAR) {
		this.itemClassAR = itemClassAR;
	}
	public int getInvoiceRecNo() {
		return invoiceRecNo;
	}
	public void setInvoiceRecNo(int invoiceRecNo) {
		this.invoiceRecNo = invoiceRecNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public int getInvoiceLineNo() {
		return invoiceLineNo;
	}
	public void setInvoiceLineNo(int invoiceLineNo) {
		this.invoiceLineNo = invoiceLineNo;
	}
	public String getInventorySite() {
		return inventorySite;
	}
	public void setInventorySite(String inventorySite) {
		this.inventorySite = inventorySite;
	}
	public String getInventorySiteAR() {
		return inventorySiteAR;
	}
	public void setInventorySiteAR(String inventorySiteAR) {
		this.inventorySiteAR = inventorySiteAR;
	}
	public boolean isHideSite() {
		return hideSite;
	}
	public void setHideSite(boolean hideSite) {
		this.hideSite = hideSite;
	}
	public QbListsModel getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(QbListsModel selectedItems) {
		this.selectedItems = selectedItems;
	}
	public QbListsModel getSelectedInvcCutomerGridInvrtySiteNew() {
		return selectedInvcCutomerGridInvrtySiteNew;
	}
	public void setSelectedInvcCutomerGridInvrtySiteNew(
			QbListsModel selectedInvcCutomerGridInvrtySiteNew) {
		this.selectedInvcCutomerGridInvrtySiteNew = selectedInvcCutomerGridInvrtySiteNew;
	}
	public QbListsModel getSelectedInvcCutomerGridInvrtyClassNew() {
		return selectedInvcCutomerGridInvrtyClassNew;
	}
	public void setSelectedInvcCutomerGridInvrtyClassNew(
			QbListsModel selectedInvcCutomerGridInvrtyClassNew) {
		this.selectedInvcCutomerGridInvrtyClassNew = selectedInvcCutomerGridInvrtyClassNew;
	}
	public boolean isShowCheck() {
		return showCheck;
	}
	public void setShowCheck(boolean showCheck) {
		this.showCheck = showCheck;
	}
	public int getQuotationNo() {
		return quotationNo;
	}
	public void setQuotationNo(int quotationNo) {
		this.quotationNo = quotationNo;
	}

	
	
}
