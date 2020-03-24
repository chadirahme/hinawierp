package model;

import java.util.Date;

public class ExpensesModel 
{

	private int srNO;
	private int accountid;
	private AccountsModel selectedAccount;
	private double amount;
	private String memo;
	private String billNo;
	private Date billDate;
	private int customerJobid;
	private QbListsModel selectedCustomer;
	private String className;
	private ClassModel selectedClass;
	private int fixedAssetItemid;
	private FixedAssetModel selectedFixedAsset;
	
	// item Receipt
	private int recNo;
	private String txnLineId;
	private int selectedAccountKey;
	private int selectedCutomerKey;
	private int selectedClassKey;
	
	private String billable="";
	
	private boolean billableChked=false;
	
	private boolean showBillable=false;
	
	
	
	public int getSrNO() {
		return srNO;
	}
	public void setSrNO(int srNO) {
		this.srNO = srNO;
	}
	public int getAccountid() {
		return accountid;
	}
	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public int getCustomerJobid() {
		return customerJobid;
	}
	public void setCustomerJobid(int customerJobid) {
		this.customerJobid = customerJobid;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getFixedAssetItemid() {
		return fixedAssetItemid;
	}
	public void setFixedAssetItemid(int fixedAssetItemid) {
		this.fixedAssetItemid = fixedAssetItemid;
	}
	public AccountsModel getSelectedAccount() {
		return selectedAccount;
	}
	public void setSelectedAccount(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}
	public QbListsModel getSelectedCustomer() {
		return selectedCustomer;
	}
	public void setSelectedCustomer(QbListsModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}
	public ClassModel getSelectedClass() {
		return selectedClass;
	}
	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}
	public FixedAssetModel getSelectedFixedAsset() {
		return selectedFixedAsset;
	}
	public void setSelectedFixedAsset(FixedAssetModel selectedFixedAsset) {
		this.selectedFixedAsset = selectedFixedAsset;
	}
	public int getRecNo() {
		return recNo;
	}
	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}
	public String getTxnLineId() {
		return txnLineId;
	}
	public void setTxnLineId(String txnLineId) {
		this.txnLineId = txnLineId;
	}
	public int getSelectedAccountKey() {
		return selectedAccountKey;
	}
	public void setSelectedAccountKey(int selectedAccountKey) {
		this.selectedAccountKey = selectedAccountKey;
	}
	public int getSelectedCutomerKey() {
		return selectedCutomerKey;
	}
	public void setSelectedCutomerKey(int selectedCutomerKey) {
		this.selectedCutomerKey = selectedCutomerKey;
	}
	public int getSelectedClassKey() {
		return selectedClassKey;
	}
	public void setSelectedClassKey(int selectedClassKey) {
		this.selectedClassKey = selectedClassKey;
	}
	public String getBillable() {
		return billable;
	}
	public void setBillable(String billable) {
		this.billable = billable;
	}
	public boolean isBillableChked() {
		return billableChked;
	}
	public void setBillableChked(boolean billableChked) {
		this.billableChked = billableChked;
	}
	public boolean isShowBillable() {
		return showBillable;
	}
	public void setShowBillable(boolean showBillable) {
		this.showBillable = showBillable;
	}
	
	
	
}
