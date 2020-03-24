package model;

import java.util.Date;


public class ChequeModel 
{
	private String TxnID;
	private int PvNo;
	private Date PvDate;
	private boolean chkPvDate;
	private boolean chkPvNo;
	private boolean chkApproved;
	private boolean chkBank;
	private String CheckNo;
	private Date CheckDate;
	private boolean chkChequeNo;
	private boolean chkChequeDate;
	private int BankKey;
	private int PayeeKey;
	private String PayeeType;
	private double Amount;
	private String Status;
	private String PVCheck_Printed;
	private String Memo;
	private int BankRefKey;
	private String Cheque;
	private String QBStatus;
	private String QBRefNo;
	private String QBRefDate;
	
	private String ExpClassHide;
	private String ExpMemoHide;
	private String ExpBillNoHide;
	private String ExpBillDateHide;
	private String ItemClassHide;
	private String ItemDesHide;
	private String ItemBillNoHide;
	private String ItemBillDateHide;
	private int AcctForPdc;
	private String PrintName;
	private int UnitKey;
	private long RecNo;
	private int UserID;
	
	
	private String address;
	
	public int getPvNo() {
		return PvNo;
	}
	public void setPvNo(int pvNo) {
		PvNo = pvNo;
	}
	public Date getPvDate() {
		return PvDate;
	}
	public void setPvDate(Date pvDate) {
		PvDate = pvDate;
		
	}
	public boolean isChkPvDate() {
		return chkPvDate;
	}
	
	public void setChkPvDate(boolean chkPvDate) {
		this.chkPvDate = chkPvDate;
	}
	public String getCheckNo() {
		return CheckNo;
	}
	public void setCheckNo(String checkNo) {
		CheckNo = checkNo;
	}
	public Date getCheckDate() {
		return CheckDate;
	}
	public void setCheckDate(Date checkDate) {
		CheckDate = checkDate;
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
	public int getBankRefKey() {
		return BankRefKey;
	}
	public void setBankRefKey(int bankRefKey) {
		BankRefKey = bankRefKey;
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
	public long getRecNo() {
		return RecNo;
	}
	public void setRecNo(long recNo) {
		RecNo = recNo;
	}
	public boolean isChkPvNo() {
		return chkPvNo;
	}
	public void setChkPvNo(boolean chkPvNo) {
		this.chkPvNo = chkPvNo;
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
	public boolean isChkChequeNo() {
		return chkChequeNo;
	}
	public void setChkChequeNo(boolean chkChequeNo) {
		this.chkChequeNo = chkChequeNo;
	}
	public boolean isChkChequeDate() {
		return chkChequeDate;
	}

	public void setChkChequeDate(boolean chkChequeDate) {
		this.chkChequeDate = chkChequeDate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTxnID() {
		return TxnID;
	}
	public void setTxnID(String txnID) {
		TxnID = txnID;
	}
	public String getPVCheck_Printed() {
		return PVCheck_Printed;
	}
	public void setPVCheck_Printed(String pVCheck_Printed) {
		PVCheck_Printed = pVCheck_Printed;
	}
	public String getCheque() {
		return Cheque;
	}
	public void setCheque(String cheque) {
		Cheque = cheque;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	
	
}
