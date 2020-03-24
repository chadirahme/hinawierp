package model;

import java.util.Date;

public class BankTransferModel 
{
	private long RecNo;
	private String TxnID="";
	private String PvNo="";
	private Date PvDate;
	private Date createdDate;
	private int BankKey;
	private boolean chkBank;
	private int PayeeKey;
	private String PayeeType="";
	private double Amount;
	private String Status="";
	private String PVCheck_Printed="";
	private String Memo="";
	private int BankRefKey;
	private String Cheque="";
	private String QBStatus="";
	private String QBRefNo="";
	private String QBRefDate="";
	
	private String ExpClassHide="";
	private String ExpMemoHide="";
	private String ExpBillNoHide="";
	private String ExpBillDateHide="";
	private String ItemClassHide="";
	private String ItemDesHide="";
	private String ItemBillNoHide="";
	private String ItemBillDateHide="";
	private int AcctForPdc;
	private String PrintName="";
	private int UnitKey;
	private String SwiftCode="";
	private int UserID;
	
	private long BankTransferRecNo;
	private String attnName="";
	private String attnPosition="";
	private String fromActName="";
	private String fromBankName="";
	private String fromActNumber="";
	private String fromBranch="";
	private String fromIBANNo="";
	private String toActName="";
	private String toBankName="";
	private String toActNumber="";
	private String toBranch="";
	private String toIBANNo="";
	private String toTRANSCode="";
	
	public String getAttnName() {
		return attnName;
	}
	public void setAttnName(String attnName) {
		this.attnName = attnName;
	}
	public String getFromActName() {
		return fromActName;
	}
	public void setFromActName(String fromActName) {
		this.fromActName = fromActName;
	}
	public String getFromBankName() {
		return fromBankName;
	}
	public void setFromBankName(String fromBankName) {
		this.fromBankName = fromBankName;
	}
	public String getFromActNumber() {
		return fromActNumber;
	}
	public void setFromActNumber(String fromActNumber) {
		this.fromActNumber = fromActNumber;
	}
	public String getFromBranch() {
		return fromBranch;
	}
	public void setFromBranch(String fromBranch) {
		this.fromBranch = fromBranch;
	}
	public String getFromIBANNo() {
		return fromIBANNo;
	}
	public void setFromIBANNo(String fromIBANNo) {
		this.fromIBANNo = fromIBANNo;
	}
	public String getToActName() {
		return toActName;
	}
	public void setToActName(String toActName) {
		this.toActName = toActName;
	}
	public String getToBankName() {
		return toBankName;
	}
	public void setToBankName(String toBankName) {
		this.toBankName = toBankName;
	}
	public String getToActNumber() {
		return toActNumber;
	}
	public void setToActNumber(String toActNumber) {
		this.toActNumber = toActNumber;
	}
	public String getToBranch() {
		return toBranch;
	}
	public void setToBranch(String toBranch) {
		this.toBranch = toBranch;
	}
	public String getToIBANNo() {
		return toIBANNo;
	}
	public void setToIBANNo(String toIBANNo) {
		this.toIBANNo = toIBANNo;
	}
	public String getToTRANSCode() {
		return toTRANSCode;
	}
	public void setToTRANSCode(String toTRANSCode) {
		this.toTRANSCode = toTRANSCode;
	}
	public long getRecNo() {
		return RecNo;
	}
	public void setRecNo(long recNo) {
		RecNo = recNo;
	}
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
	public String getPVCheck_Printed() {
		return PVCheck_Printed;
	}
	public void setPVCheck_Printed(String pVCheck_Printed) {
		PVCheck_Printed = pVCheck_Printed;
	}
	public String getMemo() {
		return Memo;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public int getBankRefKey() {
		return BankRefKey;
	}
	public void setBankRefKey(int bankRefKey) {
		BankRefKey = bankRefKey;
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
	public int getAcctForPdc() {
		return AcctForPdc;
	}
	public void setAcctForPdc(int acctForPdc) {
		AcctForPdc = acctForPdc;
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
	public String getSwiftCode() {
		return SwiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		SwiftCode = swiftCode;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public long getBankTransferRecNo() {
		return BankTransferRecNo;
	}
	public void setBankTransferRecNo(long bankTransferRecNo) {
		BankTransferRecNo = bankTransferRecNo;
	}
	
	public boolean isChkBank() {
		return chkBank;
	}
	public void setChkBank(boolean chkBank) {
		this.chkBank = chkBank;
	}
	public String getAttnPosition() {
		return attnPosition;
	}
	public void setAttnPosition(String attnPosition) {
		this.attnPosition = attnPosition;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
