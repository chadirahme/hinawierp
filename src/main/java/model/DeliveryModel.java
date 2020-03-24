package model;

import java.util.Date;

public class DeliveryModel {
	private int RecNo;
	private String txnID="";
	private int customerRefKey;
	private int classRefKey;
	private int aRAccountRefKey;
	private int templateRefKey;
	private Date txnDate;
	private String refNumber="";
	private String billAddress1="";
	private String billAddress2="";	
	private String billAddress3="";	  
	private String billAddress4="";
	private String billAddress5="";	
	private String billAddressCity="";	
	private String billAddressState="";
	private String billAddressPostalCode=""; 
	private String billAddressCountry="";
	private String billAddressNote="";
	private String shipAddress1="";
	private String shipAddress2="";
	private String shipAddress3="";
	private String shipAddress4="";
	private String shipAddress5="";
	private String shipAddressCity="";
	private String shipAddressState="";
	private String shipAddressPostalCode=""; 
	private String shipAddressCountry="";
	private String shipAddressNote="";
	private String pending=""; 	
	private String pONumber="";	
	private int sendViaReffKey;      
	private int termsRefKey;
	private Date  dueDate;
	private int salesRefKey;
	private String fOB="";
	private String shipDate="";
	private int shipMethodRefKey;
	private int ttemSalesTaxRefKey;
	private String memo=""; 
	private String memoArabic="";	
	private int customerMsgRefKey;	
	private String isToBePrinted="";
	private String isToEmailed="";	
	private String isTaxIncluded="";	  
	private int customerSalesTaxCodeRefKey;
	private String other="";    
	private double amount;
	private int quotationRecNo;
	private String customField1="";	
	private String customField2="";	
	private String customField3="";	
	private String customField4="";		
	private String customField5="";
	private String remindFalg="";
	private Date remindDate;
	private int remindDays;
	private String status="";
	private String attachment="";
	private String updateREG="";
	private String descriptionHIDE="";
	private String qtyHIDE="";
	private String classHIDE="";
	private int userID;
	private Date dateCreated;
	private int lineNo;
	private int itemrefkey;
	private double quantity;
	private double quantityInvoice;
	private String itemName="";
	private String level="";
	private String txnDateStr="";
	private boolean show;
	private String customerName="";
	private String salesRep="";
	private String userName="";
	private String paymentType="";
	private String depositeTo="";
	private String deliveryDateStr="";
	private String checkNo="";
	private String totalSales="";
	private String deliveryAmount="";
	private String email="";
	private double totalBalance;
	private String bankName="";
	private String accountName="";
	private String accountNo="";
	private String branchName="";
	private String iBANNo="";
	private String phone="";
	private String fax="";
	private String printChequeAs="";
	private String transformQ="";
	private String statusDesc="";
	private int invertySiteKey;

	
	
	public int getRecNo() {
		return RecNo;
	}
	public void setRecNo(int recNo) {
		RecNo = recNo;
	}
	public String getTxnID() {
		return txnID;
	}
	public void setTxnID(String txnID) {
		this.txnID = txnID;
	}
	public int getCustomerRefKey() {
		return customerRefKey;
	}
	public void setCustomerRefKey(int customerRefKey) {
		this.customerRefKey = customerRefKey;
	}
	public int getClassRefKey() {
		return classRefKey;
	}
	public void setClassRefKey(int classRefKey) {
		this.classRefKey = classRefKey;
	}
	public int getaRAccountRefKey() {
		return aRAccountRefKey;
	}
	public void setaRAccountRefKey(int aRAccountRefKey) {
		this.aRAccountRefKey = aRAccountRefKey;
	}
	public int getTemplateRefKey() {
		return templateRefKey;
	}
	public void setTemplateRefKey(int templateRefKey) {
		this.templateRefKey = templateRefKey;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
	public String getRefNumber() {
		return refNumber;
	}
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	public String getBillAddress1() {
		return billAddress1;
	}
	public void setBillAddress1(String billAddress1) {
		this.billAddress1 = billAddress1;
	}
	public String getBillAddress2() {
		return billAddress2;
	}
	public void setBillAddress2(String billAddress2) {
		this.billAddress2 = billAddress2;
	}
	public String getBillAddress3() {
		return billAddress3;
	}
	public void setBillAddress3(String billAddress3) {
		this.billAddress3 = billAddress3;
	}
	public String getBillAddress4() {
		return billAddress4;
	}
	public void setBillAddress4(String billAddress4) {
		this.billAddress4 = billAddress4;
	}
	public String getBillAddress5() {
		return billAddress5;
	}
	public void setBillAddress5(String billAddress5) {
		this.billAddress5 = billAddress5;
	}
	public String getBillAddressCity() {
		return billAddressCity;
	}
	public void setBillAddressCity(String billAddressCity) {
		this.billAddressCity = billAddressCity;
	}
	public String getBillAddressState() {
		return billAddressState;
	}
	public void setBillAddressState(String billAddressState) {
		this.billAddressState = billAddressState;
	}
	public String getBillAddressPostalCode() {
		return billAddressPostalCode;
	}
	public void setBillAddressPostalCode(String billAddressPostalCode) {
		this.billAddressPostalCode = billAddressPostalCode;
	}
	public String getBillAddressCountry() {
		return billAddressCountry;
	}
	public void setBillAddressCountry(String billAddressCountry) {
		this.billAddressCountry = billAddressCountry;
	}
	public String getBillAddressNote() {
		return billAddressNote;
	}
	public void setBillAddressNote(String billAddressNote) {
		this.billAddressNote = billAddressNote;
	}
	public String getShipAddress1() {
		return shipAddress1;
	}
	public void setShipAddress1(String shipAddress1) {
		this.shipAddress1 = shipAddress1;
	}
	public String getShipAddress2() {
		return shipAddress2;
	}
	public void setShipAddress2(String shipAddress2) {
		this.shipAddress2 = shipAddress2;
	}
	public String getShipAddress3() {
		return shipAddress3;
	}
	public void setShipAddress3(String shipAddress3) {
		this.shipAddress3 = shipAddress3;
	}
	public String getShipAddress4() {
		return shipAddress4;
	}
	public void setShipAddress4(String shipAddress4) {
		this.shipAddress4 = shipAddress4;
	}
	public String getShipAddress5() {
		return shipAddress5;
	}
	public void setShipAddress5(String shipAddress5) {
		this.shipAddress5 = shipAddress5;
	}
	public String getShipAddressCity() {
		return shipAddressCity;
	}
	public void setShipAddressCity(String shipAddressCity) {
		this.shipAddressCity = shipAddressCity;
	}
	public String getShipAddressState() {
		return shipAddressState;
	}
	public void setShipAddressState(String shipAddressState) {
		this.shipAddressState = shipAddressState;
	}
	public String getShipAddressPostalCode() {
		return shipAddressPostalCode;
	}
	public void setShipAddressPostalCode(String shipAddressPostalCode) {
		this.shipAddressPostalCode = shipAddressPostalCode;
	}
	public String getShipAddressCountry() {
		return shipAddressCountry;
	}
	public void setShipAddressCountry(String shipAddressCountry) {
		this.shipAddressCountry = shipAddressCountry;
	}
	public String getShipAddressNote() {
		return shipAddressNote;
	}
	public void setShipAddressNote(String shipAddressNote) {
		this.shipAddressNote = shipAddressNote;
	}
	public String getPending() {
		return pending;
	}
	public void setPending(String pending) {
		this.pending = pending;
	}
	public String getpONumber() {
		return pONumber;
	}
	public void setpONumber(String pONumber) {
		this.pONumber = pONumber;
	}
	public int getSendViaReffKey() {
		return sendViaReffKey;
	}
	public void setSendViaReffKey(int sendViaReffKey) {
		this.sendViaReffKey = sendViaReffKey;
	}
	public int getTermsRefKey() {
		return termsRefKey;
	}
	public void setTermsRefKey(int termsRefKey) {
		this.termsRefKey = termsRefKey;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date date) {
		this.dueDate = date;
	}
	public int getSalesRefKey() {
		return salesRefKey;
	}
	public void setSalesRefKey(int salesRefKey) {
		this.salesRefKey = salesRefKey;
	}
	public String getfOB() {
		return fOB;
	}
	public void setfOB(String fOB) {
		this.fOB = fOB;
	}
	public String getShipDate() {
		return shipDate;
	}
	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}
	public int getShipMethodRefKey() {
		return shipMethodRefKey;
	}
	public void setShipMethodRefKey(int shipMethodRefKey) {
		this.shipMethodRefKey = shipMethodRefKey;
	}
	public int getTtemSalesTaxRefKey() {
		return ttemSalesTaxRefKey;
	}
	public void setTtemSalesTaxRefKey(int ttemSalesTaxRefKey) {
		this.ttemSalesTaxRefKey = ttemSalesTaxRefKey;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMemoArabic() {
		return memoArabic;
	}
	public void setMemoArabic(String memoArabic) {
		this.memoArabic = memoArabic;
	}
	public int getCustomerMsgRefKey() {
		return customerMsgRefKey;
	}
	public void setCustomerMsgRefKey(int customerMsgRefKey) {
		this.customerMsgRefKey = customerMsgRefKey;
	}
	public String getIsToBePrinted() {
		return isToBePrinted;
	}
	public void setIsToBePrinted(String isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}
	public String getIsToEmailed() {
		return isToEmailed;
	}
	public void setIsToEmailed(String isToEmailed) {
		this.isToEmailed = isToEmailed;
	}
	public String getIsTaxIncluded() {
		return isTaxIncluded;
	}
	public void setIsTaxIncluded(String isTaxIncluded) {
		this.isTaxIncluded = isTaxIncluded;
	}
	public int getCustomerSalesTaxCodeRefKey() {
		return customerSalesTaxCodeRefKey;
	}
	public void setCustomerSalesTaxCodeRefKey(int customerSalesTaxCodeRefKey) {
		this.customerSalesTaxCodeRefKey = customerSalesTaxCodeRefKey;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getQuotationRecNo() {
		return quotationRecNo;
	}
	public void setQuotationRecNo(int quotationRecNo) {
		this.quotationRecNo = quotationRecNo;
	}
	public String getCustomField1() {
		return customField1;
	}
	public void setCustomField1(String customField1) {
		this.customField1 = customField1;
	}
	public String getCustomField2() {
		return customField2;
	}
	public void setCustomField2(String customField2) {
		this.customField2 = customField2;
	}
	public String getCustomField3() {
		return customField3;
	}
	public void setCustomField3(String customField3) {
		this.customField3 = customField3;
	}
	public String getCustomField4() {
		return customField4;
	}
	public void setCustomField4(String customField4) {
		this.customField4 = customField4;
	}
	public String getCustomField5() {
		return customField5;
	}
	public void setCustomField5(String customField5) {
		this.customField5 = customField5;
	}
	public int getRemindDays() {
		return remindDays;
	}
	public void setRemindDays(int remindDays) {
		this.remindDays = remindDays;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getUpdateREG() {
		return updateREG;
	}
	public void setUpdateREG(String updateREG) {
		this.updateREG = updateREG;
	}
	public String getDescriptionHIDE() {
		return descriptionHIDE;
	}
	public void setDescriptionHIDE(String descriptionHIDE) {
		this.descriptionHIDE = descriptionHIDE;
	}
	public String getQtyHIDE() {
		return qtyHIDE;
	}
	public void setQtyHIDE(String qtyHIDE) {
		this.qtyHIDE = qtyHIDE;
	}
	public String getClassHIDE() {
		return classHIDE;
	}
	public void setClassHIDE(String classHIDE) {
		this.classHIDE = classHIDE;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSalesRep() {
		return salesRep;
	}
	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getDepositeTo() {
		return depositeTo;
	}
	public void setDepositeTo(String depositeTo) {
		this.depositeTo = depositeTo;
	}
	public String getDeliveryDateStr() {
		return deliveryDateStr;
	}
	public void setDeliveryDateStr(String deliveryDateStr) {
		this.deliveryDateStr = deliveryDateStr;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getTotalSales() {
		return totalSales;
	}
	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}
	public String getDeliveryAmount() {
		return deliveryAmount;
	}
	public void setDeliveryAmount(String deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}
	public String getRemindFalg() {
		return remindFalg;
	}
	public void setRemindFalg(String remindFalg) {
		this.remindFalg = remindFalg;
	}
	public Date getRemindDate() {
		return remindDate;
	}
	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getiBANNo() {
		return iBANNo;
	}
	public void setiBANNo(String iBANNo) {
		this.iBANNo = iBANNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getPrintChequeAs() {
		return printChequeAs;
	}
	public void setPrintChequeAs(String printChequeAs) {
		this.printChequeAs = printChequeAs;
	}
	public String getTransformQ() {
		return transformQ;
	}
	public void setTransformQ(String transfomQ) {
		this.transformQ = transfomQ;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getItemrefkey() {
		return itemrefkey;
	}
	public void setItemrefkey(int itemrefkey) {
		this.itemrefkey = itemrefkey;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getQuantityInvoice() {
		return quantityInvoice;
	}
	public void setQuantityInvoice(double quantityInvoice) {
		this.quantityInvoice = quantityInvoice;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getTxnDateStr() {
		return txnDateStr;
	}
	public void setTxnDateStr(String txnDateStr) {
		this.txnDateStr = txnDateStr;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public int getInvertySiteKey() {
		return invertySiteKey;
	}
	public void setInvertySiteKey(int invertySiteKey) {
		this.invertySiteKey = invertySiteKey;
	}
	
	


}
