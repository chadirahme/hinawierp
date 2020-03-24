package model;

import java.util.Date;

public class CheckFAItemsModel 
{
	private int recNo;
	private int LineNo;
	private FixedAssetModel selectedFixedAsset;
	private String Description;
	private String billNo;
	private QbListsModel selectedCustomer;
	private QbListsModel selectedCustody;
	private Date InvoiceDate;
	
	private double Quantity;
	private double UnitPrice;
	private double OtherCharges;
	private double Amount;
	
	private int faItemKey;
	
	private int custodyKey;
	
	private int customerKey;
			
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
	public FixedAssetModel getSelectedFixedAsset() {
		return selectedFixedAsset;
	}
	public void setSelectedFixedAsset(FixedAssetModel selectedFixedAsset) {
		this.selectedFixedAsset = selectedFixedAsset;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public QbListsModel getSelectedCustomer() {
		return selectedCustomer;
	}
	public void setSelectedCustomer(QbListsModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}
	public QbListsModel getSelectedCustody() {
		return selectedCustody;
	}
	public void setSelectedCustody(QbListsModel selectedCustody) {
		this.selectedCustody = selectedCustody;
	}
	public Date getInvoiceDate() {
		return InvoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		InvoiceDate = invoiceDate;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	public double getUnitPrice() {
		return UnitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		UnitPrice = unitPrice;
	}
	public double getOtherCharges() {
		return OtherCharges;
	}
	public void setOtherCharges(double otherCharges) {
		OtherCharges = otherCharges;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public int getFaItemKey() {
		return faItemKey;
	}
	public void setFaItemKey(int faItemKey) {
		this.faItemKey = faItemKey;
	}
	public int getCustodyKey() {
		return custodyKey;
	}
	public void setCustodyKey(int custodyKey) {
		this.custodyKey = custodyKey;
	}
	public int getCustomerKey() {
		return customerKey;
	}
	public void setCustomerKey(int customerKey) {
		this.customerKey = customerKey;
	}
	
	
	
	
}
