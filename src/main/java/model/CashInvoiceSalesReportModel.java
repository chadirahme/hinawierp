package model;

import java.util.Date;

public class CashInvoiceSalesReportModel implements Comparable<CashInvoiceSalesReportModel>{
	
	
	private int serialNumber;
	
	private String invoiceNumber;
	
	private Date invoiceDate;
	
	private String invoiceDateStr;
	
	private String customerName;
	
	private String depositeTo;
	
	private String checkNo;
	
	private Double invoiceAmount;
	
	private double totalSales;
	
	private String paymentType;
	
	private String salesRep;
	
		
	//credit Invoice
	
	private String dueDate;
	
	private String tremName;
	
	private String  invoiceSource;
	
	private String  notes;
	
	private String  status;
	
	private Double paidAmount;
	
	private int recNO;
	
	private double totalPaidAmount;
	
	private double unpaidPaidAmount;
	
	private int webuserId;
	
	private String userName;
	
	
	
	

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDepositeTo() {
		return depositeTo;
	}

	public void setDepositeTo(String depositeTo) {
		this.depositeTo = depositeTo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}

	public String getInvoiceDateStr() {
		return invoiceDateStr;
	}

	public void setInvoiceDateStr(String invoiceDateStr) {
		this.invoiceDateStr = invoiceDateStr;
	}
	
	 @Override
	    public int compareTo(CashInvoiceSalesReportModel cashInvoiceSalesReportModel) {
	         
	        return this.invoiceAmount.compareTo(cashInvoiceSalesReportModel.getInvoiceAmount());
	    }

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getTremName() {
		return tremName;
	}

	public void setTremName(String tremName) {
		this.tremName = tremName;
	}

	public String getInvoiceSource() {
		return invoiceSource;
	}

	public void setInvoiceSource(String invoiceSource) {
		this.invoiceSource = invoiceSource;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public int getRecNO() {
		return recNO;
	}

	public void setRecNO(int recNO) {
		this.recNO = recNO;
	}

	public double getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public void setTotalPaidAmount(double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}

	public double getUnpaidPaidAmount() {
		return unpaidPaidAmount;
	}

	public void setUnpaidPaidAmount(double unpaidPaidAmount) {
		this.unpaidPaidAmount = unpaidPaidAmount;
	}

	/**
	 * @return the webuserId
	 */
	public int getWebuserId() {
		return webuserId;
	}

	/**
	 * @param webuserId the webuserId to set
	 */
	public void setWebuserId(int webuserId) {
		this.webuserId = webuserId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	 
	 
	 
	 

}
