/**
 * 
 */
package model;

import java.util.Date;

/**
 * @author IQBALMUFASIL
 * 
 */
public class CashInvoiceModel {

	/**
	 * 
	 */
	
	//cutomer information
	
	
	private String phone;
	private String fax;
	private String printChequeAs;
	private String name;
	
	private String fullname;
	
	private String email;
	
	private String bankName;
	private String accountName;
	private String accountNo;
	private String branchName;
	private String iBANNo;
	
	private String commCreated;
	
	private double totalBalance;
	

	private int recNo;
	
	private Integer bankKey;
	
	private Integer gradeId;

	private String TxnId;
	
	private String invoiceType;
	

	String tmpIsToBePrinted ;
	
	String tmpIsToEmailed ;
	
	String tmpIsTaxIncluded ;
	
	String tmpIsPending ;
	
	Integer tmpShipMethodRefKey ;
	
	Integer tmpItemSalesTaxRefKey ;
	
	Integer tmpCustomerMsgRefKey ;
	
	Integer tmpCustomerSalesTaxCodeRefKey;
	
	Integer tmpTemplateRefKey;

	String col_DescriptionHIDE ;
	
	String col_QtyHIDE ;
	
	String col_RateHIDE ;
	
	String col_ClassHIDE ;
	
	private Integer customerRefKey;

	private Integer classRefKey;
	
	private Integer depositAccountRefKey;
	
	private Integer TemplateRefKey;
	
	private String refNumber;
	
	private Date txnDate;
	
	private String billAddress1;
	
	private String billAddress2;
	
	private String billAddress3;
	
	private String billAddress4;
	
	private String billAddress5;
	
	private String billAddressCity;
	
	private String billAddressState;
	
	private String billAddressPostalCode;
	
	private String billAddressCountry;
	
	private String billAddressNote;
	
	private String shipAddress1;
	
	private String shipAddress2;
	
	private String shipAddress3;
	
	private String shipAddress4;
	
	private String shipAddress5;
	
	private String shipAddressCity;
	
	private String shipAddressState;
	
	private String shipAddressPostalCode;
	
	private String shipAddressCountry;
	
	private String shipAddressNote;
	
	private String isPending;
	
	private String checkNo;
	
	private Integer paymentMethodRefKey;
	
	private Integer salesRefKey;
	
	private String fOB;
	
	private Date shipDate;
	
	private Integer shipMethodRefKey;
	
	private Integer itemSalesTaxRefKey;
	
	private String memo;
	
	private Integer customerMsgRefKey;
	
	private String isToBePrinted;
	
	private String isToEmailed;
	
	private String isTaxIncluded;
	
	private Integer customerSalesTaxCodeRefKey;
	
	private String other;
	
	private double amount;
	
	private Integer quotationRecNo;
	
	private Integer sendViaReffKey;
	
	private String customField1;
	
	private String customField2;
	
	private String customField3;
	
	private String customField4;
	
	private String customField5;
	
	private String status;
	
	private String statusFlag;
	private boolean canChangeStatus;
	
	private String descriptionHIDE;
	
	private String qtyHIDE;
	
	private String classHIDE;
	
	private String rateHIDE;
	
	private double invoiceProfileNumber;
		
	private String invoiceAddress;
	
	private Integer invoiceDepositToAmmount;

	private String invoiceSaleNo;

	private String invoiceCheckNo;

	private String invoiceMemo;

	private int rowHieght;
	
	//credit Invoice
	private String creditLimit;
	
	private double invoiceAmount;
	
	private double paidAmount;
	
	private int accountRefKey;
	
	private int termRefKey;
	
	private Date dueDate;
	
	private String poNumber;
	
	private Date checkDate;
	
	//Quotation
	
	private String clientType;
	
	private String statusDesc;
	
	private String remingMeFalg;
	
	private Date remindMeDate;
	
	private int remindMedays;
	
	private String attachemnet;
	
	private String shipToAddress;
	
	private String letterTemplate;
	
	private double lineAmount;
	
	private String customerName;
	
	private String perspectiveName;
	
	private double ratePercent;
	
	private int quantity;
	
	private String itemName;
	
	private String lineMemo;
	
	private String lineNumber;
	
	private double avgCost;
	
	private double rate;
	
	private String invoiceDateStr; 
	
	private String sendVia;
	
	private String description;
	
	private String customerContactExpiryDateStr;
	
	private String customerCreatedDate;
	
	private String balckListed;
	private String transformD;
	

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
	 * @return the txnId
	 */
	public String getTxnId() {
		return TxnId;
	}

	/**
	 * @param txnId the txnId to set
	 */
	public void setTxnId(String txnId) {
		TxnId = txnId;
	}

	/**
	 * @return the tmpIsToBePrinted
	 */
	public String getTmpIsToBePrinted() {
		return tmpIsToBePrinted;
	}

	/**
	 * @param tmpIsToBePrinted the tmpIsToBePrinted to set
	 */
	public void setTmpIsToBePrinted(String tmpIsToBePrinted) {
		this.tmpIsToBePrinted = tmpIsToBePrinted;
	}

	/**
	 * @return the tmpIsToEmailed
	 */
	public String getTmpIsToEmailed() {
		return tmpIsToEmailed;
	}

	/**
	 * @param tmpIsToEmailed the tmpIsToEmailed to set
	 */
	public void setTmpIsToEmailed(String tmpIsToEmailed) {
		this.tmpIsToEmailed = tmpIsToEmailed;
	}

	/**
	 * @return the tmpIsTaxIncluded
	 */
	public String getTmpIsTaxIncluded() {
		return tmpIsTaxIncluded;
	}

	/**
	 * @param tmpIsTaxIncluded the tmpIsTaxIncluded to set
	 */
	public void setTmpIsTaxIncluded(String tmpIsTaxIncluded) {
		this.tmpIsTaxIncluded = tmpIsTaxIncluded;
	}

	/**
	 * @return the tmpIsPending
	 */
	public String getTmpIsPending() {
		return tmpIsPending;
	}

	/**
	 * @param tmpIsPending the tmpIsPending to set
	 */
	public void setTmpIsPending(String tmpIsPending) {
		this.tmpIsPending = tmpIsPending;
	}

	/**
	 * @return the tmpShipMethodRefKey
	 */
	public Integer getTmpShipMethodRefKey() {
		return tmpShipMethodRefKey;
	}

	/**
	 * @param tmpShipMethodRefKey the tmpShipMethodRefKey to set
	 */
	public void setTmpShipMethodRefKey(Integer tmpShipMethodRefKey) {
		this.tmpShipMethodRefKey = tmpShipMethodRefKey;
	}

	/**
	 * @return the tmpItemSalesTaxRefKey
	 */
	public Integer getTmpItemSalesTaxRefKey() {
		return tmpItemSalesTaxRefKey;
	}

	/**
	 * @param tmpItemSalesTaxRefKey the tmpItemSalesTaxRefKey to set
	 */
	public void setTmpItemSalesTaxRefKey(Integer tmpItemSalesTaxRefKey) {
		this.tmpItemSalesTaxRefKey = tmpItemSalesTaxRefKey;
	}

	/**
	 * @return the tmpCustomerMsgRefKey
	 */
	public Integer getTmpCustomerMsgRefKey() {
		return tmpCustomerMsgRefKey;
	}

	/**
	 * @param tmpCustomerMsgRefKey the tmpCustomerMsgRefKey to set
	 */
	public void setTmpCustomerMsgRefKey(Integer tmpCustomerMsgRefKey) {
		this.tmpCustomerMsgRefKey = tmpCustomerMsgRefKey;
	}

	/**
	 * @return the tmpCustomerSalesTaxCodeRefKey
	 */
	public Integer getTmpCustomerSalesTaxCodeRefKey() {
		return tmpCustomerSalesTaxCodeRefKey;
	}

	/**
	 * @param tmpCustomerSalesTaxCodeRefKey the tmpCustomerSalesTaxCodeRefKey to set
	 */
	public void setTmpCustomerSalesTaxCodeRefKey(
			Integer tmpCustomerSalesTaxCodeRefKey) {
		this.tmpCustomerSalesTaxCodeRefKey = tmpCustomerSalesTaxCodeRefKey;
	}

	/**
	 * @return the tmpTemplateRefKey
	 */
	public Integer getTmpTemplateRefKey() {
		return tmpTemplateRefKey;
	}

	/**
	 * @param tmpTemplateRefKey the tmpTemplateRefKey to set
	 */
	public void setTmpTemplateRefKey(Integer tmpTemplateRefKey) {
		this.tmpTemplateRefKey = tmpTemplateRefKey;
	}

	/**
	 * @return the col_DescriptionHIDE
	 */
	public String getCol_DescriptionHIDE() {
		return col_DescriptionHIDE;
	}

	/**
	 * @param col_DescriptionHIDE the col_DescriptionHIDE to set
	 */
	public void setCol_DescriptionHIDE(String col_DescriptionHIDE) {
		this.col_DescriptionHIDE = col_DescriptionHIDE;
	}

	/**
	 * @return the col_QtyHIDE
	 */
	public String getCol_QtyHIDE() {
		return col_QtyHIDE;
	}

	/**
	 * @param col_QtyHIDE the col_QtyHIDE to set
	 */
	public void setCol_QtyHIDE(String col_QtyHIDE) {
		this.col_QtyHIDE = col_QtyHIDE;
	}

	/**
	 * @return the col_RateHIDE
	 */
	public String getCol_RateHIDE() {
		return col_RateHIDE;
	}

	/**
	 * @param col_RateHIDE the col_RateHIDE to set
	 */
	public void setCol_RateHIDE(String col_RateHIDE) {
		this.col_RateHIDE = col_RateHIDE;
	}

	/**
	 * @return the col_ClassHIDE
	 */
	public String getCol_ClassHIDE() {
		return col_ClassHIDE;
	}

	/**
	 * @param col_ClassHIDE the col_ClassHIDE to set
	 */
	public void setCol_ClassHIDE(String col_ClassHIDE) {
		this.col_ClassHIDE = col_ClassHIDE;
	}

	/**
	 * @return the customerRefKey
	 */
	public Integer getCustomerRefKey() {
		return customerRefKey;
	}

	/**
	 * @param customerRefKey the customerRefKey to set
	 */
	public void setCustomerRefKey(Integer customerRefKey) {
		this.customerRefKey = customerRefKey;
	}

	/**
	 * @return the classRefKey
	 */
	public Integer getClassRefKey() {
		return classRefKey;
	}

	/**
	 * @param classRefKey the classRefKey to set
	 */
	public void setClassRefKey(Integer classRefKey) {
		this.classRefKey = classRefKey;
	}

	/**
	 * @return the depositAccountRefKey
	 */
	public Integer getDepositAccountRefKey() {
		return depositAccountRefKey;
	}

	/**
	 * @param depositAccountRefKey the depositAccountRefKey to set
	 */
	public void setDepositAccountRefKey(Integer depositAccountRefKey) {
		this.depositAccountRefKey = depositAccountRefKey;
	}

	/**
	 * @return the templateRefKey
	 */
	public Integer getTemplateRefKey() {
		return TemplateRefKey;
	}

	/**
	 * @param templateRefKey the templateRefKey to set
	 */
	public void setTemplateRefKey(Integer templateRefKey) {
		TemplateRefKey = templateRefKey;
	}

	/**
	 * @return the refNumber
	 */
	public String getRefNumber() {
		return refNumber;
	}

	/**
	 * @param refNumber the refNumber to set
	 */
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	/**
	 * @return the txnDate
	 */
	public Date getTxnDate() {
		return txnDate;
	}

	/**
	 * @param txnDate the txnDate to set
	 */
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	/**
	 * @return the billAddress1
	 */
	public String getBillAddress1() {
		return billAddress1;
	}

	/**
	 * @param billAddress1 the billAddress1 to set
	 */
	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}

	/**
	 * @return the billAddress2
	 */
	public String getBillAddress2() {
		return billAddress2;
	}

	/**
	 * @param billAddress2 the billAddress2 to set
	 */
	public void setBillAddress2(String billAddress2) {
		this.billAddress2 = billAddress2;
	}

	/**
	 * @return the billAddress3
	 */
	public String getBillAddress3() {
		return billAddress3;
	}

	/**
	 * @param billAddress3 the billAddress3 to set
	 */
	public void setBillAddress3(String billAddress3) {
		this.billAddress3 = billAddress3;
	}

	/**
	 * @return the billAddress4
	 */
	public String getBillAddress4() {
		return billAddress4;
	}

	/**
	 * @param billAddress4 the billAddress4 to set
	 */
	public void setBillAddress4(String billAddress4) {
		this.billAddress4 = billAddress4;
	}

	/**
	 * @return the billAddress5
	 */
	public String getBillAddress5() {
		return billAddress5;
	}

	/**
	 * @param billAddress5 the billAddress5 to set
	 */
	public void setBillAddress5(String billAddress5) {
		this.billAddress5 = billAddress5;
	}

	/**
	 * @return the billAddressCity
	 */
	public String getBillAddressCity() {
		return billAddressCity;
	}

	/**
	 * @param billAddressCity the billAddressCity to set
	 */
	public void setBillAddressCity(String billAddressCity) {
		this.billAddressCity = billAddressCity;
	}

	/**
	 * @return the billAddressState
	 */
	public String getBillAddressState() {
		return billAddressState;
	}

	/**
	 * @param billAddressState the billAddressState to set
	 */
	public void setBillAddressState(String billAddressState) {
		this.billAddressState = billAddressState;
	}

	/**
	 * @return the billAddressPostalCode
	 */
	public String getBillAddressPostalCode() {
		return billAddressPostalCode;
	}

	/**
	 * @param billAddressPostalCode the billAddressPostalCode to set
	 */
	public void setBillAddressPostalCode(String billAddressPostalCode) {
		this.billAddressPostalCode = billAddressPostalCode;
	}

	/**
	 * @return the billAddressCountry
	 */
	public String getBillAddressCountry() {
		return billAddressCountry;
	}

	/**
	 * @param billAddressCountry the billAddressCountry to set
	 */
	public void setBillAddressCountry(String billAddressCountry) {
		this.billAddressCountry = billAddressCountry;
	}

	/**
	 * @return the billAddressNote
	 */
	public String getBillAddressNote() {
		return billAddressNote;
	}

	/**
	 * @param billAddressNote the billAddressNote to set
	 */
	public void setBillAddressNote(String billAddressNote) {
		this.billAddressNote = billAddressNote;
	}

	/**
	 * @return the shipAddress1
	 */
	public String getShipAddress1() {
		return shipAddress1;
	}

	/**
	 * @param shipAddress1 the shipAddress1 to set
	 */
	public void setShipAddress1(String shipAddress1) {
		this.shipAddress1 = shipAddress1;
	}

	/**
	 * @return the shipAddress2
	 */
	public String getShipAddress2() {
		return shipAddress2;
	}

	/**
	 * @param shipAddress2 the shipAddress2 to set
	 */
	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}

	/**
	 * @return the shipAddress3
	 */
	public String getShipAddress3() {
		return shipAddress3;
	}

	/**
	 * @param shipAddress3 the shipAddress3 to set
	 */
	public void setShipAddress3(String shipAddress3) {
		this.shipAddress3 = shipAddress3;
	}

	/**
	 * @return the shipAddress4
	 */
	public String getShipAddress4() {
		return shipAddress4;
	}

	/**
	 * @param shipAddress4 the shipAddress4 to set
	 */
	public void setShipAddress4(String shipAddress4) {
		this.shipAddress4 = shipAddress4;
	}

	/**
	 * @return the shipAddress5
	 */
	public String getShipAddress5() {
		return shipAddress5;
	}

	/**
	 * @param shipAddress5 the shipAddress5 to set
	 */
	public void setShipAddress5(String shipAddress5) {
		this.shipAddress5 = shipAddress5;
	}

	/**
	 * @return the shipAddressCity
	 */
	public String getShipAddressCity() {
		return shipAddressCity;
	}

	/**
	 * @param shipAddressCity the shipAddressCity to set
	 */
	public void setShipAddressCity(String shipAddressCity) {
		this.shipAddressCity = shipAddressCity;
	}

	/**
	 * @return the shipAddressState
	 */
	public String getShipAddressState() {
		return shipAddressState;
	}

	/**
	 * @param shipAddressState the shipAddressState to set
	 */
	public void setShipAddressState(String shipAddressState) {
		this.shipAddressState = shipAddressState;
	}

	/**
	 * @return the shipAddressPostalCode
	 */
	public String getShipAddressPostalCode() {
		return shipAddressPostalCode;
	}

	/**
	 * @param shipAddressPostalCode the shipAddressPostalCode to set
	 */
	public void setShipAddressPostalCode(String shipAddressPostalCode) {
		this.shipAddressPostalCode = shipAddressPostalCode;
	}

	/**
	 * @return the shipAddressCountry
	 */
	public String getShipAddressCountry() {
		return shipAddressCountry;
	}

	/**
	 * @param shipAddressCountry the shipAddressCountry to set
	 */
	public void setShipAddressCountry(String shipAddressCountry) {
		this.shipAddressCountry = shipAddressCountry;
	}

	/**
	 * @return the shipAddressNote
	 */
	public String getShipAddressNote() {
		return shipAddressNote;
	}

	/**
	 * @param shipAddressNote the shipAddressNote to set
	 */
	public void setShipAddressNote(String shipAddressNote) {
		this.shipAddressNote = shipAddressNote;
	}

	/**
	 * @return the isPending
	 */
	public String getIsPending() {
		return isPending;
	}

	/**
	 * @param isPending the isPending to set
	 */
	public void setIsPending(String isPending) {
		this.isPending = isPending;
	}

	/**
	 * @return the checkNo
	 */
	public String getCheckNo() {
		return checkNo;
	}

	/**
	 * @param checkNo the checkNo to set
	 */
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	/**
	 * @return the paymentMethodRefKey
	 */
	public Integer getPaymentMethodRefKey() {
		return paymentMethodRefKey;
	}

	/**
	 * @param paymentMethodRefKey the paymentMethodRefKey to set
	 */
	public void setPaymentMethodRefKey(Integer paymentMethodRefKey) {
		this.paymentMethodRefKey = paymentMethodRefKey;
	}

	/**
	 * @return the salesRefKey
	 */
	public Integer getSalesRefKey() {
		return salesRefKey;
	}

	/**
	 * @param salesRefKey the salesRefKey to set
	 */
	public void setSalesRefKey(Integer salesRefKey) {
		this.salesRefKey = salesRefKey;
	}

	/**
	 * @return the fOB
	 */
	public String getfOB() {
		return fOB;
	}

	/**
	 * @param fOB the fOB to set
	 */
	public void setfOB(String fOB) {
		this.fOB = fOB;
	}

	/**
	 * @return the shipDate
	 */
	public Date getShipDate() {
		return shipDate;
	}

	/**
	 * @param shipDate the shipDate to set
	 */
	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	/**
	 * @return the shipMethodRefKey
	 */
	public Integer getShipMethodRefKey() {
		return shipMethodRefKey;
	}

	/**
	 * @param shipMethodRefKey the shipMethodRefKey to set
	 */
	public void setShipMethodRefKey(Integer shipMethodRefKey) {
		this.shipMethodRefKey = shipMethodRefKey;
	}

	/**
	 * @return the itemSalesTaxRefKey
	 */
	public Integer getItemSalesTaxRefKey() {
		return itemSalesTaxRefKey;
	}

	/**
	 * @param itemSalesTaxRefKey the itemSalesTaxRefKey to set
	 */
	public void setItemSalesTaxRefKey(Integer itemSalesTaxRefKey) {
		this.itemSalesTaxRefKey = itemSalesTaxRefKey;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the customerMsgRefKey
	 */
	public Integer getCustomerMsgRefKey() {
		return customerMsgRefKey;
	}

	/**
	 * @param customerMsgRefKey the customerMsgRefKey to set
	 */
	public void setCustomerMsgRefKey(Integer customerMsgRefKey) {
		this.customerMsgRefKey = customerMsgRefKey;
	}

	/**
	 * @return the isToBePrinted
	 */
	public String getIsToBePrinted() {
		return isToBePrinted;
	}

	/**
	 * @param isToBePrinted the isToBePrinted to set
	 */
	public void setIsToBePrinted(String isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	/**
	 * @return the isToEmailed
	 */
	public String getIsToEmailed() {
		return isToEmailed;
	}

	/**
	 * @param isToEmailed the isToEmailed to set
	 */
	public void setIsToEmailed(String isToEmailed) {
		this.isToEmailed = isToEmailed;
	}

	/**
	 * @return the isTaxIncluded
	 */
	public String getIsTaxIncluded() {
		return isTaxIncluded;
	}

	/**
	 * @param isTaxIncluded the isTaxIncluded to set
	 */
	public void setIsTaxIncluded(String isTaxIncluded) {
		this.isTaxIncluded = isTaxIncluded;
	}

	/**
	 * @return the customerSalesTaxCodeRefKey
	 */
	public Integer getCustomerSalesTaxCodeRefKey() {
		return customerSalesTaxCodeRefKey;
	}

	/**
	 * @param customerSalesTaxCodeRefKey the customerSalesTaxCodeRefKey to set
	 */
	public void setCustomerSalesTaxCodeRefKey(Integer customerSalesTaxCodeRefKey) {
		this.customerSalesTaxCodeRefKey = customerSalesTaxCodeRefKey;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the quotationRecNo
	 */
	public Integer getQuotationRecNo() {
		return quotationRecNo;
	}

	/**
	 * @param quotationRecNo the quotationRecNo to set
	 */
	public void setQuotationRecNo(Integer quotationRecNo) {
		this.quotationRecNo = quotationRecNo;
	}

	/**
	 * @return the sendViaReffKey
	 */
	public Integer getSendViaReffKey() {
		return sendViaReffKey;
	}

	/**
	 * @param sendViaReffKey the sendViaReffKey to set
	 */
	public void setSendViaReffKey(Integer sendViaReffKey) {
		this.sendViaReffKey = sendViaReffKey;
	}

	/**
	 * @return the customField1
	 */
	public String getCustomField1() {
		return customField1;
	}

	/**
	 * @param customField1 the customField1 to set
	 */
	public void setCustomField1(String customField1) {
		this.customField1 = customField1;
	}

	/**
	 * @return the customField2
	 */
	public String getCustomField2() {
		return customField2;
	}

	/**
	 * @param customField2 the customField2 to set
	 */
	public void setCustomField2(String customField2) {
		this.customField2 = customField2;
	}

	/**
	 * @return the customField3
	 */
	public String getCustomField3() {
		return customField3;
	}

	/**
	 * @param customField3 the customField3 to set
	 */
	public void setCustomField3(String customField3) {
		this.customField3 = customField3;
	}

	/**
	 * @return the customField4
	 */
	public String getCustomField4() {
		return customField4;
	}

	/**
	 * @param customField4 the customField4 to set
	 */
	public void setCustomField4(String customField4) {
		this.customField4 = customField4;
	}

	/**
	 * @return the customField5
	 */
	public String getCustomField5() {
		return customField5;
	}

	/**
	 * @param customField5 the customField5 to set
	 */
	public void setCustomField5(String customField5) {
		this.customField5 = customField5;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the descriptionHIDE
	 */
	public String getDescriptionHIDE() {
		return descriptionHIDE;
	}

	/**
	 * @param descriptionHIDE the descriptionHIDE to set
	 */
	public void setDescriptionHIDE(String descriptionHIDE) {
		this.descriptionHIDE = descriptionHIDE;
	}

	/**
	 * @return the qtyHIDE
	 */
	public String getQtyHIDE() {
		return qtyHIDE;
	}

	/**
	 * @param qtyHIDE the qtyHIDE to set
	 */
	public void setQtyHIDE(String qtyHIDE) {
		this.qtyHIDE = qtyHIDE;
	}

	/**
	 * @return the classHIDE
	 */
	public String getClassHIDE() {
		return classHIDE;
	}

	/**
	 * @param classHIDE the classHIDE to set
	 */
	public void setClassHIDE(String classHIDE) {
		this.classHIDE = classHIDE;
	}

	/**
	 * @return the rateHIDE
	 */
	public String getRateHIDE() {
		return rateHIDE;
	}

	/**
	 * @param rateHIDE the rateHIDE to set
	 */
	public void setRateHIDE(String rateHIDE) {
		this.rateHIDE = rateHIDE;
	}

	/**
	 * @return the invoiceAddress
	 */
	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	/**
	 * @param invoiceAddress the invoiceAddress to set
	 */
	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	/**
	 * @return the invoiceDepositToAmmount
	 */
	public Integer getInvoiceDepositToAmmount() {
		return invoiceDepositToAmmount;
	}

	/**
	 * @param invoiceDepositToAmmount the invoiceDepositToAmmount to set
	 */
	public void setInvoiceDepositToAmmount(Integer invoiceDepositToAmmount) {
		this.invoiceDepositToAmmount = invoiceDepositToAmmount;
	}

	/**
	 * @return the invoiceSaleNo
	 */
	public String getInvoiceSaleNo() {
		return invoiceSaleNo;
	}

	/**
	 * @param invoiceSaleNo the invoiceSaleNo to set
	 */
	public void setInvoiceSaleNo(String invoiceSaleNo) {
		this.invoiceSaleNo = invoiceSaleNo;
	}

	/**
	 * @return the invoiceCheckNo
	 */
	public String getInvoiceCheckNo() {
		return invoiceCheckNo;
	}

	/**
	 * @param invoiceCheckNo the invoiceCheckNo to set
	 */
	public void setInvoiceCheckNo(String invoiceCheckNo) {
		this.invoiceCheckNo = invoiceCheckNo;
	}

	/**
	 * @return the invoiceMemo
	 */
	public String getInvoiceMemo() {
		return invoiceMemo;
	}

	/**
	 * @param invoiceMemo the invoiceMemo to set
	 */
	public void setInvoiceMemo(String invoiceMemo) {
		this.invoiceMemo = invoiceMemo;
	}

	/**
	 * @return the rowHieght
	 */
	public int getRowHieght() {
		return rowHieght;
	}

	/**
	 * @param rowHieght the rowHieght to set
	 */
	public void setRowHieght(int rowHieght) {
		this.rowHieght = rowHieght;
	}

	/**
	 * @return the invoiceProfileNumber
	 */
	public double getInvoiceProfileNumber() {
		return invoiceProfileNumber;
	}

	/**
	 * @param invoiceProfileNumber the invoiceProfileNumber to set
	 */
	public void setInvoiceProfileNumber(double invoiceProfileNumber) {
		this.invoiceProfileNumber = invoiceProfileNumber;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the printChequeAs
	 */
	public String getPrintChequeAs() {
		return printChequeAs;
	}

	/**
	 * @param printChequeAs the printChequeAs to set
	 */
	public void setPrintChequeAs(String printChequeAs) {
		this.printChequeAs = printChequeAs;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the iBANNo
	 */
	public String getiBANNo() {
		return iBANNo;
	}

	/**
	 * @param iBANNo the iBANNo to set
	 */
	public void setiBANNo(String iBANNo) {
		this.iBANNo = iBANNo;
	}

	/**
	 * @return the totalBalance
	 */
	public double getTotalBalance() {
		return totalBalance;
	}

	/**
	 * @param totalBalance the totalBalance to set
	 */
	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	public double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public int getAccountRefKey() {
		return accountRefKey;
	}

	public void setAccountRefKey(int accountRefKey) {
		this.accountRefKey = accountRefKey;
	}

	public int getTermRefKey() {
		return termRefKey;
	}

	public void setTermRefKey(int termRefKey) {
		this.termRefKey = termRefKey;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Integer getBankKey() {
		return bankKey;
	}

	public void setBankKey(Integer bankKey) {
		this.bankKey = bankKey;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getCommCreated() {
		return commCreated;
	}

	public void setCommCreated(String commCreated) {
		this.commCreated = commCreated;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getRemingMeFalg() {
		return remingMeFalg;
	}

	public void setRemingMeFalg(String remingMeFalg) {
		this.remingMeFalg = remingMeFalg;
	}

	public Date getRemindMeDate() {
		return remindMeDate;
	}

	public void setRemindMeDate(Date remindMeDate) {
		this.remindMeDate = remindMeDate;
	}

	public int getRemindMedays() {
		return remindMedays;
	}

	public void setRemindMedays(int remindMedays) {
		this.remindMedays = remindMedays;
	}

	public String getAttachemnet() {
		return attachemnet;
	}

	public void setAttachemnet(String attachemnet) {
		this.attachemnet = attachemnet;
	}

	public String getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	public String getLetterTemplate() {
		return letterTemplate;
	}

	public void setLetterTemplate(String letterTemplate) {
		this.letterTemplate = letterTemplate;
	}

	public double getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(double lineAmount) {
		this.lineAmount = lineAmount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPerspectiveName() {
		return perspectiveName;
	}

	public void setPerspectiveName(String perspectiveName) {
		this.perspectiveName = perspectiveName;
	}

	public double getRatePercent() {
		return ratePercent;
	}

	public void setRatePercent(double ratePercent) {
		this.ratePercent = ratePercent;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getLineMemo() {
		return lineMemo;
	}

	public void setLineMemo(String lineMemo) {
		this.lineMemo = lineMemo;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public double getAvgCost() {
		return avgCost;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setAvgCost(double avgCost) {
		this.avgCost = avgCost;
	}

	public String getInvoiceDateStr() {
		return invoiceDateStr;
	}

	public void setInvoiceDateStr(String invoiceDateStr) {
		this.invoiceDateStr = invoiceDateStr;
	}

	public String getSendVia() {
		return sendVia;
	}

	public void setSendVia(String sendVia) {
		this.sendVia = sendVia;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the customerContactExpiryDateStr
	 */
	public String getCustomerContactExpiryDateStr() {
		return customerContactExpiryDateStr;
	}

	/**
	 * @param customerContactExpiryDateStr the customerContactExpiryDateStr to set
	 */
	public void setCustomerContactExpiryDateStr(String customerContactExpiryDateStr) {
		this.customerContactExpiryDateStr = customerContactExpiryDateStr;
	}

	/**
	 * @return the customerCreatedDate
	 */
	public String getCustomerCreatedDate() {
		return customerCreatedDate;
	}

	/**
	 * @param customerCreatedDate the customerCreatedDate to set
	 */
	public void setCustomerCreatedDate(String customerCreatedDate) {
		this.customerCreatedDate = customerCreatedDate;
	}

	/**
	 * @return the balckListed
	 */
	public String getBalckListed() {
		return balckListed;
	}

	/**
	 * @param balckListed the balckListed to set
	 */
	public void setBalckListed(String balckListed) {
		this.balckListed = balckListed;
	}

	public String getTransformD() {
		return transformD;
	}

	public void setTransformD(String transformD) {
		this.transformD = transformD;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public boolean isCanChangeStatus() {
		return canChangeStatus;
	}

	public void setCanChangeStatus(boolean canChangeStatus) {
		this.canChangeStatus = canChangeStatus;
	}
	
	
		
}
