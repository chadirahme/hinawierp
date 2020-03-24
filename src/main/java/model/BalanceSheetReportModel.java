package model;

import java.util.Date;

public class BalanceSheetReportModel 
{
		
	private int recNo;
	
	private String actName;
	
	private double amount;
	
	private String rowType;
	
	private String actTYpe;
	
	private int actKey;
	
	private String reportName;
	
	private int subLevel;
	
	private String accountNameORG;
	
	private String accountType;
	
	private String name;
	
	private String description;
	
	private int istotal;
	
	private int istotalSummary;

	
	private double debit;
	private double credit;
	private double balance;
	private String accountNumber;
	
	private double OpeningDb;
	private double OpeningCr;
	private double movementDb;
	private double movementCr;
	private double movementNet;
	private double balanceDb;
	private double balanceCr;
	private double OpeningNet;
	
	private String rowColor;
	
	private String txnType;
	private String txnNo;
	private String entityName;
	private String entityListType;
	private Date txnDate;
	private String txnDateStr;
	private String splitActName;
	private String memo;
	
	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getRowType() {
		return rowType;
	}

	public void setRowType(String rowType) {
		this.rowType = rowType;
	}

	public String getActTYpe() {
		return actTYpe;
	}

	public void setActTYpe(String actTYpe) {
		this.actTYpe = actTYpe;
	}

	public int getActKey() {
		return actKey;
	}

	public void setActKey(int actKey) {
		this.actKey = actKey;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public int getSubLevel() {
		return subLevel;
	}

	public void setSubLevel(int subLevel) {
		this.subLevel = subLevel;
	}

	public String getAccountNameORG() {
		return accountNameORG;
	}

	public void setAccountNameORG(String accountNameORG) {
		this.accountNameORG = accountNameORG;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		
	}

	public int getIstotal() {
		return istotal;
	}

	public void setIstotal(int istotal) {
		this.istotal = istotal;
	}

	public int getIstotalSummary() {
		return istotalSummary;
	}

	public void setIstotalSummary(int istotalSummary) {
		this.istotalSummary = istotalSummary;
	}

	public double getDebit() {
		return debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getOpeningDb() {
		return OpeningDb;
	}

	public void setOpeningDb(double openingDb) {
		OpeningDb = openingDb;
	}

	public double getOpeningCr() {
		return OpeningCr;
	}

	public void setOpeningCr(double openingCr) {
		OpeningCr = openingCr;
	}

	public double getMovementDb() {
		return movementDb;
	}

	public void setMovementDb(double movementDb) {
		this.movementDb = movementDb;
	}

	public double getMovementCr() {
		return movementCr;
	}

	public void setMovementCr(double movementCr) {
		this.movementCr = movementCr;
	}

	public double getBalanceDb() {
		return balanceDb;
	}

	public void setBalanceDb(double balanceDb) {
		this.balanceDb = balanceDb;
	}

	public double getBalanceCr() {
		return balanceCr;
	}

	public void setBalanceCr(double balanceCr) {
		this.balanceCr = balanceCr;
	}

	public double getOpeningNet() {
		return OpeningNet;
	}

	public void setOpeningNet(double openingNet) {
		OpeningNet = openingNet;
	}

	public double getMovementNet() {
		return movementNet;
	}

	public void setMovementNet(double movementNet) {
		this.movementNet = movementNet;
	}

	public String getRowColor() {
		return rowColor;
	}

	public void setRowColor(String rowColor) {
		this.rowColor = rowColor;
	}

	

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnNo() {
		return txnNo;
	}

	public void setTxnNo(String txnNo) {
		this.txnNo = txnNo;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityListType() {
		return entityListType;
	}

	public void setEntityListType(String entityListType) {
		this.entityListType = entityListType;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getTxnDateStr() {
		return txnDateStr;
	}

	public void setTxnDateStr(String txnDateStr) {
		this.txnDateStr = txnDateStr;
	}

	public String getSplitActName() {
		return splitActName;
	}

	public void setSplitActName(String splitActName) {
		this.splitActName = splitActName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	

}
