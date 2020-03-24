package model;

import java.util.Date;

public class CashModel 
{
	private long RecNo;
	private String TxnID;
	private Date createdDate;
	private String createdDateStr;
	private Date modifiedDate;
	private String modifiedDateStr;
	private String editSequence;
	private String checkNo;
	private Date checkDate; 
	private String checkDateStr;
	private String PvNo;
	private Date PvDate;
	private String pvDateStr;
	private int BankKey;
	private int PayeeKey;
	private String PayeeType;
	private boolean chkApproved;
	private int registartionId;
	private String QBRefNo;
	private String QBRefDate;
	private String PrintName;
	private String Memo;
	private double Amount;
	private String Status;	
	private int bankRefKey;
	private String statusMemo;
	private String Cheque;
	private Date recosileDate;
	private String reconsileDateStr;
	private String QBStatus;
	private String ExpClassHide;
	private String ExpMemoHide;
	private String ExpBillNoHide;
	private String ExpBillDateHide;
	private String ItemClassHide;
	private String ItemDesHide;
	private String ItemBillNoHide;
	private String ItemBillDateHide;
	private String pvCheck_print;
	private int accForPdc;
	private int apAccountRefKey;
	private int UnitKey;
	private int contractRecNo;
	private String printLetter;
	private String swiftCode;
	private String removeFromPdc;
	private String newStatus;
	private String advancePayment;
	private int webUserID;
	private int UserID;
	private String address;
	private boolean chkBank;
	private String editedFromonline;
	private String PVCheck_Printed;
	private int AcctForPdc;
	
	
	public String getTxnID() {
		return TxnID;
	}

	public void setTxnID(String txnID) {
		TxnID = txnID;
	}

	public String getPvNo() {
		return PvNo;
	}

	public void setPvNo(String pvNo) {
		PvNo = pvNo;
	}

	public Date getPvDate() {
		return PvDate;
	}

	public void setPvDate(Date pvDate) {
		PvDate = pvDate;
	}

	public int getBankKey() {
		return BankKey;
	}

	public void setBankKey(int bankKey) {
		BankKey = bankKey;
	}

	public int getPayeeKey() {
		return PayeeKey;
	}

	public void setPayeeKey(int payeeKey) {
		PayeeKey = payeeKey;
	}

	public String getPayeeType() {
		return PayeeType;
	}

	public void setPayeeType(String payeeType) {
		PayeeType = payeeType;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMemo() {
		return Memo;
	}

	public void setMemo(String memo) {
		Memo = memo;
	}

	public String getCheque() {
		return Cheque;
	}

	public void setCheque(String cheque) {
		Cheque = cheque;
	}

	public String getQBStatus() {
		return QBStatus;
	}

	public void setQBStatus(String qBStatus) {
		QBStatus = qBStatus;
	}

	public String getQBRefNo() {
		return QBRefNo;
	}

	public void setQBRefNo(String qBRefNo) {
		QBRefNo = qBRefNo;
	}

	public String getQBRefDate() {
		return QBRefDate;
	}

	public void setQBRefDate(String qBRefDate) {
		QBRefDate = qBRefDate;
	}

	public String getExpClassHide() {
		return ExpClassHide;
	}

	public void setExpClassHide(String expClassHide) {
		ExpClassHide = expClassHide;
	}

	public String getExpMemoHide() {
		return ExpMemoHide;
	}

	public void setExpMemoHide(String expMemoHide) {
		ExpMemoHide = expMemoHide;
	}

	public String getExpBillNoHide() {
		return ExpBillNoHide;
	}

	public void setExpBillNoHide(String expBillNoHide) {
		ExpBillNoHide = expBillNoHide;
	}

	public String getExpBillDateHide() {
		return ExpBillDateHide;
	}

	public void setExpBillDateHide(String expBillDateHide) {
		ExpBillDateHide = expBillDateHide;
	}

	public String getItemClassHide() {
		return ItemClassHide;
	}

	public void setItemClassHide(String itemClassHide) {
		ItemClassHide = itemClassHide;
	}

	public String getItemDesHide() {
		return ItemDesHide;
	}

	public void setItemDesHide(String itemDesHide) {
		ItemDesHide = itemDesHide;
	}

	public String getItemBillNoHide() {
		return ItemBillNoHide;
	}

	public void setItemBillNoHide(String itemBillNoHide) {
		ItemBillNoHide = itemBillNoHide;
	}

	public String getItemBillDateHide() {
		return ItemBillDateHide;
	}

	public void setItemBillDateHide(String itemBillDateHide) {
		ItemBillDateHide = itemBillDateHide;
	}

	public String getPrintName() {
		return PrintName;
	}

	public void setPrintName(String printName) {
		PrintName = printName;
	}

	public int getUnitKey() {
		return UnitKey;
	}

	public void setUnitKey(int unitKey) {
		UnitKey = unitKey;
	}

	public long getRecNo() {
		return RecNo;
	}

	public void setRecNo(long recNo) {
		RecNo = recNo;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isChkApproved() {
		return chkApproved;
	}

	public void setChkApproved(boolean chkApproved) {
		this.chkApproved = chkApproved;
	}

	public boolean isChkBank() {
		return chkBank;
	}

	public void setChkBank(boolean chkBank) {
		this.chkBank = chkBank;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdDateStr
	 */
	public String getCreatedDateStr() {
		return createdDateStr;
	}

	/**
	 * @param createdDateStr the createdDateStr to set
	 */
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedDateStr
	 */
	public String getModifiedDateStr() {
		return modifiedDateStr;
	}

	/**
	 * @param modifiedDateStr the modifiedDateStr to set
	 */
	public void setModifiedDateStr(String modifiedDateStr) {
		this.modifiedDateStr = modifiedDateStr;
	}

	/**
	 * @return the editSequence
	 */
	public String getEditSequence() {
		return editSequence;
	}

	/**
	 * @param editSequence the editSequence to set
	 */
	public void setEditSequence(String editSequence) {
		this.editSequence = editSequence;
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
	 * @return the checkDate
	 */
	public Date getCheckDate() {
		return checkDate;
	}

	/**
	 * @param checkDate the checkDate to set
	 */
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	/**
	 * @return the registartionId
	 */
	public int getRegistartionId() {
		return registartionId;
	}

	/**
	 * @param registartionId the registartionId to set
	 */
	public void setRegistartionId(int registartionId) {
		this.registartionId = registartionId;
	}

	/**
	 * @return the statusMemo
	 */
	public String getStatusMemo() {
		return statusMemo;
	}

	/**
	 * @param statusMemo the statusMemo to set
	 */
	public void setStatusMemo(String statusMemo) {
		this.statusMemo = statusMemo;
	}

	/**
	 * @return the recosileDate
	 */
	public Date getRecosileDate() {
		return recosileDate;
	}

	/**
	 * @param recosileDate the recosileDate to set
	 */
	public void setRecosileDate(Date recosileDate) {
		this.recosileDate = recosileDate;
	}

	/**
	 * @return the reconsileDateStr
	 */
	public String getReconsileDateStr() {
		return reconsileDateStr;
	}

	/**
	 * @param reconsileDateStr the reconsileDateStr to set
	 */
	public void setReconsileDateStr(String reconsileDateStr) {
		this.reconsileDateStr = reconsileDateStr;
	}

	/**
	 * @return the pvCheck_print
	 */
	public String getPvCheck_print() {
		return pvCheck_print;
	}

	/**
	 * @param pvCheck_print the pvCheck_print to set
	 */
	public void setPvCheck_print(String pvCheck_print) {
		this.pvCheck_print = pvCheck_print;
	}

	/**
	 * @return the accForPdc
	 */
	public int getAccForPdc() {
		return accForPdc;
	}

	/**
	 * @param accForPdc the accForPdc to set
	 */
	public void setAccForPdc(int accForPdc) {
		this.accForPdc = accForPdc;
	}

	/**
	 * @return the apAccountRefKey
	 */
	public int getApAccountRefKey() {
		return apAccountRefKey;
	}

	/**
	 * @param apAccountRefKey the apAccountRefKey to set
	 */
	public void setApAccountRefKey(int apAccountRefKey) {
		this.apAccountRefKey = apAccountRefKey;
	}

	/**
	 * @return the contractRecNo
	 */
	public int getContractRecNo() {
		return contractRecNo;
	}

	/**
	 * @param contractRecNo the contractRecNo to set
	 */
	public void setContractRecNo(int contractRecNo) {
		this.contractRecNo = contractRecNo;
	}

	/**
	 * @return the printLetter
	 */
	public String getPrintLetter() {
		return printLetter;
	}

	/**
	 * @param printLetter the printLetter to set
	 */
	public void setPrintLetter(String printLetter) {
		this.printLetter = printLetter;
	}

	/**
	 * @return the swiftCode
	 */
	public String getSwiftCode() {
		return swiftCode;
	}

	/**
	 * @param swiftCode the swiftCode to set
	 */
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	/**
	 * @return the removeFromPdc
	 */
	public String getRemoveFromPdc() {
		return removeFromPdc;
	}

	/**
	 * @param removeFromPdc the removeFromPdc to set
	 */
	public void setRemoveFromPdc(String removeFromPdc) {
		this.removeFromPdc = removeFromPdc;
	}

	/**
	 * @return the newStatus
	 */
	public String getNewStatus() {
		return newStatus;
	}

	/**
	 * @param newStatus the newStatus to set
	 */
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	/**
	 * @return the advancePayment
	 */
	public String getAdvancePayment() {
		return advancePayment;
	}

	/**
	 * @param advancePayment the advancePayment to set
	 */
	public void setAdvancePayment(String advancePayment) {
		this.advancePayment = advancePayment;
	}

	/**
	 * @return the webUserID
	 */
	public int getWebUserID() {
		return webUserID;
	}

	/**
	 * @param webUserID the webUserID to set
	 */
	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}

	/**
	 * @return the editedFromonline
	 */
	public String getEditedFromonline() {
		return editedFromonline;
	}

	/**
	 * @param editedFromonline the editedFromonline to set
	 */
	public void setEditedFromonline(String editedFromonline) {
		this.editedFromonline = editedFromonline;
	}

	/**
	 * @return the checkDateStr
	 */
	public String getCheckDateStr() {
		return checkDateStr;
	}

	/**
	 * @param checkDateStr the checkDateStr to set
	 */
	public void setCheckDateStr(String checkDateStr) {
		this.checkDateStr = checkDateStr;
	}

	/**
	 * @return the pvDateStr
	 */
	public String getPvDateStr() {
		return pvDateStr;
	}

	/**
	 * @param pvDateStr the pvDateStr to set
	 */
	public void setPvDateStr(String pvDateStr) {
		this.pvDateStr = pvDateStr;
	}

	/**
	 * @return the bankRefKey
	 */
	public int getBankRefKey() {
		return bankRefKey;
	}

	/**
	 * @param bankRefKey the bankRefKey to set
	 */
	public void setBankRefKey(int bankRefKey) {
		this.bankRefKey = bankRefKey;
	}

	public String getPVCheck_Printed() {
		return PVCheck_Printed;
	}

	public void setPVCheck_Printed(String pVCheck_Printed) {
		PVCheck_Printed = pVCheck_Printed;
	}

	public int getAcctForPdc() {
		return AcctForPdc;
	}

	public void setAcctForPdc(int acctForPdc) {
		AcctForPdc = acctForPdc;
	}


	
	
	
}
