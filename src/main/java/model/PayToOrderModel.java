package model;

public class PayToOrderModel 
{

	private int key;
	private String BillAddress1;
	private String BillAddress2;
	private String BillAddress3;
	private String BillAddress4;
	
	private String fullName;
		
	private String Phone;
	private String fax;
	private String PrintChequeAs;
	private String Name;
	
	private String BankName;
	private String AccountName;
	private String AccountNo;
	private String BranchName;
	private String IBANNo;
	
	//by iqbal
	private String email;
	private double totalBalance;
			
	public String getBankName() {
		return BankName;
	}
	public void setBankName(String bankName) {
		BankName = bankName;
	}
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	public String getAccountNo() {
		return AccountNo;
	}
	public void setAccountNo(String accountNo) {
		AccountNo = accountNo;
	}
	public String getBranchName() {
		return BranchName;
	}
	public void setBranchName(String branchName) {
		BranchName = branchName;
	}
	public String getIBANNo() {
		return IBANNo;
	}
	public void setIBANNo(String iBANNo) {
		IBANNo = iBANNo;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getBillAddress1() {
		return BillAddress1;
	}
	public void setBillAddress1(String billAddress1) {
		BillAddress1 = billAddress1;
	}
	public String getBillAddress2() {
		return BillAddress2;
	}
	public void setBillAddress2(String billAddress2) {
		BillAddress2 = billAddress2;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getPrintChequeAs() {
		return PrintChequeAs;
	}
	public void setPrintChequeAs(String printChequeAs) {
		PrintChequeAs = printChequeAs;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getBillAddress3() {
		return BillAddress3;
	}
	public void setBillAddress3(String billAddress3) {
		BillAddress3 = billAddress3;
	}
	public String getBillAddress4() {
		return BillAddress4;
	}
	public void setBillAddress4(String billAddress4) {
		BillAddress4 = billAddress4;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	
}
