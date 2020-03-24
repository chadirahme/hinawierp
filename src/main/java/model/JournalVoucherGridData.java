package model;

import java.util.ArrayList;
import java.util.List;

public class JournalVoucherGridData {
	private int rec_No;
	private int line_no;
	private int entityRef_Key;
	private int accountRef_Key;
	private int classRef_Key;
	private double amount;
	private int proj_ID;
	private int AccountTypeRef_Key;
	private int rVdifferedRecNo;
	private String dC_Flag="";
	private String txnLineID="";
	private String entityRef_Type="";
	private String memo="";
	private String txnID="";
	private String qBStatus="";
	private String iSEdit="";
	private String billable="";
	private String rVdifferedEditKey="";
	private String pVAdvanceEditKey="";
	private boolean showBillable=false;
	private boolean billableChked=false;
	//private QbListsModel selectedAccountType;
	private AccountsModel selectedAccount;
	private QbListsModel selectedEntityType;
	private QbListsModel selectedName;
	private ClassModel selectedClass;
	private List<AccountsModel> lstAccounts=new ArrayList<AccountsModel>();
	private List<QbListsModel> lstName=new ArrayList<QbListsModel>();
	//private HBAData data=new HBAData();
	private double debit=0;
	private double credit=0;
	
	public int getRec_No() {
		return rec_No;
	}
	public void setRec_No(int rec_No) {
		this.rec_No = rec_No;
	}
	public int getLine_no() {
		return line_no;
	}
	public void setLine_no(int line_no) {
		this.line_no = line_no;
	}
	public int getEntityRef_Key() {
		return entityRef_Key;
	}
	public void setEntityRef_Key(int entityRef_Key) {
		this.entityRef_Key = entityRef_Key;
	}
	public int getAccountRef_Key() {
		return accountRef_Key;
	}
	public void setAccountRef_Key(int accountRef_Key) {
		this.accountRef_Key = accountRef_Key;
	}
	public int getClassRef_Key() {
		return classRef_Key;
	}
	public void setClassRef_Key(int classRef_Key) {
		this.classRef_Key = classRef_Key;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getProj_ID() {
		return proj_ID;
	}
	public void setProj_ID(int proj_ID) {
		this.proj_ID = proj_ID;
	}
	public int getrVdifferedRecNo() {
		return rVdifferedRecNo;
	}
	public void setrVdifferedRecNo(int rVdifferedRecNo) {
		this.rVdifferedRecNo = rVdifferedRecNo;
	}
	public String getdC_Flag() {
		return dC_Flag;
	}
	public void setdC_Flag(String dC_Flag) {
		this.dC_Flag = dC_Flag;
	}
	public String getTxnLineID() {
		return txnLineID;
	}
	public void setTxnLineID(String txnLineID) {
		this.txnLineID = txnLineID;
	}
	public String getEntityRef_Type() {
		return entityRef_Type;
	}
	
	
	public void setEntityRef_Type(String entityRef_Type) {
		this.entityRef_Type = entityRef_Type;
		
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTxnID() {
		return txnID;
	}
	public void setTxnID(String txnID) {
		this.txnID = txnID;
	}
	public String getqBStatus() {
		return qBStatus;
	}
	public void setqBStatus(String qBStatus) {
		this.qBStatus = qBStatus;
	}
	public String getiSEdit() {
		return iSEdit;
	}
	public void setiSEdit(String iSEdit) {
		this.iSEdit = iSEdit;
	}
	public String getBillable() {
		return billable;
	}
	public void setBillable(String billable) {
		this.billable = billable;
	}
	public String getrVdifferedEditKey() {
		return rVdifferedEditKey;
	}
	public void setrVdifferedEditKey(String rVdifferedEditKey) {
		this.rVdifferedEditKey = rVdifferedEditKey;
	}
	public String getpVAdvanceEditKey() {
		return pVAdvanceEditKey;
	}
	public void setpVAdvanceEditKey(String pVAdvanceEditKey) {
		this.pVAdvanceEditKey = pVAdvanceEditKey;
	}
	
	public int getAccountTypeRef_Key() {
		return AccountTypeRef_Key;
	}
	public void setAccountTypeRef_Key(int accountTypeRef_Key) {
		AccountTypeRef_Key = accountTypeRef_Key;
	}
	public boolean isShowBillable() {
		return showBillable;
	}
	public void setShowBillable(boolean showBillable) {
		this.showBillable = showBillable;
	}
	public boolean isBillableChked() {
		return billableChked;
	}
	public void setBillableChked(boolean billableChked) {
		this.billableChked = billableChked;
	}
	/*public QbListsModel getSelectedAccountType() {
		return selectedAccountType;
	}
	
	@NotifyChange({ "lstAccounts" })
	public void setSelectedAccountType(QbListsModel selectedAccountType) {
		this.selectedAccountType = selectedAccountType;
		lstAccounts = data.fillBankAccounts("'"+selectedAccountType.getName()+"'");
	}*/
	public AccountsModel getSelectedAccount() {
		return selectedAccount;
	}
	
	public void setSelectedAccount(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}
	
	public QbListsModel getSelectedEntityType() {
		return selectedEntityType;
	}
	
	public void setSelectedEntityType(QbListsModel selectedEntityType) {
		this.selectedEntityType = selectedEntityType;
		
	}
	public QbListsModel getSelectedName() {
		return selectedName;
	}
	public void setSelectedName(QbListsModel selectedName) {
		this.selectedName = selectedName;
	}
	public ClassModel getSelectedClass() {
		return selectedClass;
	}
	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}
	public List<AccountsModel> getLstAccounts() {
		return lstAccounts;
	}
	public void setLstAccounts(List<AccountsModel> lstAccounts) {
		this.lstAccounts = lstAccounts;
	}
	public List<QbListsModel> getLstName() {
		return lstName;
	}
	public void setLstName(List<QbListsModel> lstName) {
		this.lstName = lstName;
	}
	/*public HBAData getData() {
		return data;
	}
	public void setData(HBAData data) {
		this.data = data;
	}*/
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
	
}