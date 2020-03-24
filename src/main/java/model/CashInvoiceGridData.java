package model;

import java.util.Date;


public class CashInvoiceGridData {
	
	
	
	
	private double ratePercent;
	
	private int priceLevelRefKey;
	
	private Date serviceDate;
	
	private double AvgCost;
	
	private int SalesTaxCodeRefKey;
	
	private String IsTaxable;
	
	private int overrideItemAccountRefKey;
	
	private String other1;
	
	private String other2;
	
	private int quotationLineNo;

	private int recNo;
	
	private int LineNo;
	
	private QbListsModel selectedItems;
	
	private QbListsModel selectedItem;
			
	private String invoiceDescription;
	
	private String invoicearabicDescription;
	
	private QbListsModel selectedInvcCutomerGridInvrtySiteNew;
	
	private int invoiceQtyOnHand;
	
	private	int invoiceQty;
	
	private double invoiceRate;
	
	private QbListsModel selectedInvcCutomerGridInvrtyClassNew;
	
	private double invoiceAmmount;
	
	private Date InvoiceDate;
	
	private String itemType;
	
	private int InventorySiteKey;
	
	private String barcode;
	
	private String isToBeprinted;
	
	private String discriptionAr;
	
	private int itemRefKey;
	
	private int invertySiteKey;
	
	private int selectedClass;
	
	private boolean hideSite=false;
	
	private String deliverdAs;
	
	private int deliverRecNo;
	private int deliveryLineNo;
	
	public CashInvoiceGridData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the recNo
	 */
	public int getRecNo() {
		return recNo;
	}

	/**
	 * @param recNo the recNo to set
	 */
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	/**
	 * @return the lineNo
	 */
	public int getLineNo() {
		return LineNo;
	}

	/**
	 * @param lineNo the lineNo to set
	 */
	public void setLineNo(int lineNo) {
		LineNo = lineNo;
	}

	/**
	 * @return the selectedItems
	 */
	public QbListsModel getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param selectedItems the selectedItems to set
	 */
	public void setSelectedItems(QbListsModel selectedItems) {
		this.selectedItems = selectedItems;
	}

	/**
	 * @return the invoiceDescription
	 */
	public String getInvoiceDescription() {
		return invoiceDescription;
	}

	/**
	 * @param invoiceDescription the invoiceDescription to set
	 */
	public void setInvoiceDescription(String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
	}

	/**
	 * @return the invoicearabicDescription
	 */
	public String getInvoicearabicDescription() {
		return invoicearabicDescription;
	}

	/**
	 * @param invoicearabicDescription the invoicearabicDescription to set
	 */
	public void setInvoicearabicDescription(String invoicearabicDescription) {
		this.invoicearabicDescription = invoicearabicDescription;
	}

	/**
	 * @return the selectedInvcCutomerGridInvrtySite
	 */

	/**
	 * @return the invoiceQtyOnHand
	 */
	public int getInvoiceQtyOnHand() {
		return invoiceQtyOnHand;
	}

	/**
	 * @param invoiceQtyOnHand the invoiceQtyOnHand to set
	 */
	public void setInvoiceQtyOnHand(int invoiceQtyOnHand) {
		this.invoiceQtyOnHand = invoiceQtyOnHand;
	}

	/**
	 * @return the invoiceQty
	 */
	public int getInvoiceQty() {
		return invoiceQty;
	}

	/**
	 * @param invoiceQty the invoiceQty to set
	 */
	public void setInvoiceQty(int invoiceQty) {
		this.invoiceQty = invoiceQty;
	}

	/**
	 * @return the invoiceRate
	 */
	public double getInvoiceRate() {
		return invoiceRate;
	}

	/**
	 * @param invoiceRate the invoiceRate to set
	 */
	public void setInvoiceRate(double invoiceRate) {
		this.invoiceRate = invoiceRate;
	}

	/**
	 * @return the selectedInvcCutomerGridInvrtyClass
	 */

	/**
	 * @return the invoiceAmmount
	 */
	public double getInvoiceAmmount() {
		return invoiceAmmount;
	}

	/**
	 * @param invoiceAmmount the invoiceAmmount to set
	 */
	public void setInvoiceAmmount(double invoiceAmmount) {
		this.invoiceAmmount = invoiceAmmount;
	}

	/**
	 * @return the invoiceDate
	 */
	public Date getInvoiceDate() {
		return InvoiceDate;
	}

	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(Date invoiceDate) {
		InvoiceDate = invoiceDate;
	}

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the inventorySiteKey
	 */
	public int getInventorySiteKey() {
		return InventorySiteKey;
	}

	/**
	 * @param inventorySiteKey the inventorySiteKey to set
	 */
	public void setInventorySiteKey(int inventorySiteKey) {
		InventorySiteKey = inventorySiteKey;
	}

	/**
	 * @return the selectedInvcCutomerGridInvrtySiteNew
	 */
	public QbListsModel getSelectedInvcCutomerGridInvrtySiteNew() {
		return selectedInvcCutomerGridInvrtySiteNew;
	}

	/**
	 * @param selectedInvcCutomerGridInvrtySiteNew the selectedInvcCutomerGridInvrtySiteNew to set
	 */
	public void setSelectedInvcCutomerGridInvrtySiteNew(
			QbListsModel selectedInvcCutomerGridInvrtySiteNew) {
		this.selectedInvcCutomerGridInvrtySiteNew = selectedInvcCutomerGridInvrtySiteNew;
	}

	/**
	 * @return the selectedInvcCutomerGridInvrtyClassNew
	 */
	public QbListsModel getSelectedInvcCutomerGridInvrtyClassNew() {
		return selectedInvcCutomerGridInvrtyClassNew;
	}

	/**
	 * @param selectedInvcCutomerGridInvrtyClassNew the selectedInvcCutomerGridInvrtyClassNew to set
	 */
	public void setSelectedInvcCutomerGridInvrtyClassNew(
			QbListsModel selectedInvcCutomerGridInvrtyClassNew) {
		this.selectedInvcCutomerGridInvrtyClassNew = selectedInvcCutomerGridInvrtyClassNew;
	}

	/**
	 * @return the ratePercent
	 */
	public double getRatePercent() {
		return ratePercent;
	}

	/**
	 * @param ratePercent the ratePercent to set
	 */
	public void setRatePercent(double ratePercent) {
		this.ratePercent = ratePercent;
	}

	/**
	 * @return the priceLevelRefKey
	 */
	public int getPriceLevelRefKey() {
		return priceLevelRefKey;
	}

	/**
	 * @param priceLevelRefKey the priceLevelRefKey to set
	 */
	public void setPriceLevelRefKey(int priceLevelRefKey) {
		this.priceLevelRefKey = priceLevelRefKey;
	}

	/**
	 * @return the serviceDate
	 */
	public Date getServiceDate() {
		return serviceDate;
	}

	/**
	 * @param serviceDate the serviceDate to set
	 */
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	/**
	 * @return the salesTaxCodeRefKey
	 */
	public int getSalesTaxCodeRefKey() {
		return SalesTaxCodeRefKey;
	}

	/**
	 * @param salesTaxCodeRefKey the salesTaxCodeRefKey to set
	 */
	public void setSalesTaxCodeRefKey(int salesTaxCodeRefKey) {
		SalesTaxCodeRefKey = salesTaxCodeRefKey;
	}

	/**
	 * @return the isTaxable
	 */
	public String getIsTaxable() {
		return IsTaxable;
	}

	/**
	 * @param isTaxable the isTaxable to set
	 */
	public void setIsTaxable(String isTaxable) {
		IsTaxable = isTaxable;
	}

	/**
	 * @return the overrideItemAccountRefKey
	 */
	public int getOverrideItemAccountRefKey() {
		return overrideItemAccountRefKey;
	}

	/**
	 * @param overrideItemAccountRefKey the overrideItemAccountRefKey to set
	 */
	public void setOverrideItemAccountRefKey(int overrideItemAccountRefKey) {
		this.overrideItemAccountRefKey = overrideItemAccountRefKey;
	}

	/**
	 * @return the other1
	 */
	public String getOther1() {
		return other1;
	}

	/**
	 * @param other1 the other1 to set
	 */
	public void setOther1(String other1) {
		this.other1 = other1;
	}

	/**
	 * @return the other2
	 */
	public String getOther2() {
		return other2;
	}

	/**
	 * @param other2 the other2 to set
	 */
	public void setOther2(String other2) {
		this.other2 = other2;
	}

	/**
	 * @return the quotationLineNo
	 */
	public int getQuotationLineNo() {
		return quotationLineNo;
	}

	/**
	 * @param quotationLineNo the quotationLineNo to set
	 */
	public void setQuotationLineNo(int quotationLineNo) {
		this.quotationLineNo = quotationLineNo;
	}

	/**
	 * @return the avgCost
	 */
	public double getAvgCost() {
		return AvgCost;
	}

	/**
	 * @param avgCost the avgCost to set
	 */
	public void setAvgCost(double avgCost) {
		AvgCost = avgCost;
	}

	/**
	 * @return the barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getIsToBeprinted() {
		return isToBeprinted;
	}

	public void setIsToBeprinted(String isToBeprinted) {
		this.isToBeprinted = isToBeprinted;
	}

	public String getDiscriptionAr() {
		return discriptionAr;
	}

	public void setDiscriptionAr(String discriptionAr) {
		this.discriptionAr = discriptionAr;
	}

	public int getItemRefKey() {
		return itemRefKey;
	}

	public void setItemRefKey(int itemRefKey) {
		this.itemRefKey = itemRefKey;
	}

	public int getInvertySiteKey() {
		return invertySiteKey;
	}

	public void setInvertySiteKey(int invertySiteKey) {
		this.invertySiteKey = invertySiteKey;
	}

	public int getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(int selectedClass) {
		this.selectedClass = selectedClass;
	}

	public boolean isHideSite() {
		return hideSite;
	}

	public void setHideSite(boolean hideSite) {
		this.hideSite = hideSite;
	}

	public String getDeliverdAs() {
		return deliverdAs;
	}

	public void setDeliverdAs(String deliverdAs) {
		this.deliverdAs = deliverdAs;
	}

	public int getDeliverRecNo() {
		return deliverRecNo;
	}

	public void setDeliverRecNo(int deliverRecNo) {
		this.deliverRecNo = deliverRecNo;
	}

	public int getDeliveryLineNo() {
		return deliveryLineNo;
	}

	public void setDeliveryLineNo(int deliveryLineNo) {
		this.deliveryLineNo = deliveryLineNo;
	}

	public QbListsModel getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(QbListsModel selectedItem) {
		this.selectedItem = selectedItem;
	}

	
	
	
}
