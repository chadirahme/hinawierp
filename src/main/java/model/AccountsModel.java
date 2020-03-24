package model;

import java.util.Date;
import java.util.List;

public class AccountsModel 
{
private int sRL_No;
private String name;
private String accountName;
private String oldAccountName;
private String aCTLEVELSwithNO;
private String accountType;
private int subLevel;
private int rec_No;
private String listID;
private String fullName;

private String ActLevels;


private String description;
private String className;
private double totalBalance;

private String accountNumber;
private String oldAccountNumber;
private String mainAccountNo;
private String mainAccountName;
private String level1AccNo;
private String level1AccName;
private String level2AccNo;
private String level2AccName;
private String level3AccNo;
private String level3AccName;
private String level4AccNo;
private String level4AccName;
private String notes;
private String isActive;
private double balance;
private Date balaceDate;
private Date createdDate;
private Date modifiedDate;

//chart of account edit page fields
private String bankAccountName;
private String bankAcountNumber;
private String branchName;
private String iBanNumber;
private List<String> bankNameList;
private String bankName;
private String selectedBankName;
private List<String> accountTypedropDown;
private String selectedAccountType;
private List<AccountsModel> subOfdropDown;
private AccountsModel selectedSubOf;






/**
 * @return the accountTypedropDown
 */
public List<String> getAccountTypedropDown() {
	return accountTypedropDown;
}
/**
 * @param accountTypedropDown the accountTypedropDown to set
 */
public void setAccountTypedropDown(List<String> accountTypedropDown) {
	this.accountTypedropDown = accountTypedropDown;
}
/**
 * @return the selectedAccountType
 */
public String getSelectedAccountType() {
	return selectedAccountType;
}
/**
 * @param selectedAccountType the selectedAccountType to set
 */
public void setSelectedAccountType(String selectedAccountType) {
	this.selectedAccountType = selectedAccountType;
}
/**
 * @return the subOfdropDown
 */
public List<AccountsModel> getSubOfdropDown() {
	return subOfdropDown;
}
/**
 * @param subOfdropDown the subOfdropDown to set
 */
public void setSubOfdropDown(List<AccountsModel> subOfdropDown) {
	this.subOfdropDown = subOfdropDown;
}
/**
 * @return the selectedSubOf
 */
public AccountsModel getSelectedSubOf() {
	return selectedSubOf;
}
/**
 * @param selectedSubOf the selectedSubOf to set
 */
public void setSelectedSubOf(AccountsModel selectedSubOf) {
	this.selectedSubOf = selectedSubOf;
}
/**
 * @return the mainAccountNo
 */
public String getMainAccountNo() {
	return mainAccountNo;
}
/**
 * @param mainAccountNo the mainAccountNo to set
 */
public void setMainAccountNo(String mainAccountNo) {
	this.mainAccountNo = mainAccountNo;
}
/**
 * @return the mainAccountName
 */
public String getMainAccountName() {
	return mainAccountName;
}
/**
 * @param mainAccountName the mainAccountName to set
 */
public void setMainAccountName(String mainAccountName) {
	this.mainAccountName = mainAccountName;
}
/**
 * @return the level1AccNo
 */
public String getLevel1AccNo() {
	return level1AccNo;
}
/**
 * @param level1AccNo the level1AccNo to set
 */
public void setLevel1AccNo(String level1AccNo) {
	this.level1AccNo = level1AccNo;
}
/**
 * @return the level1AccName
 */
public String getLevel1AccName() {
	return level1AccName;
}
/**
 * @param level1AccName the level1AccName to set
 */
public void setLevel1AccName(String level1AccName) {
	this.level1AccName = level1AccName;
}
/**
 * @return the level2AccNo
 */
public String getLevel2AccNo() {
	return level2AccNo;
}
/**
 * @param level2AccNo the level2AccNo to set
 */
public void setLevel2AccNo(String level2AccNo) {
	this.level2AccNo = level2AccNo;
}
/**
 * @return the level2AccName
 */
public String getLevel2AccName() {
	return level2AccName;
}
/**
 * @param level2AccName the level2AccName to set
 */
public void setLevel2AccName(String level2AccName) {
	this.level2AccName = level2AccName;
}
/**
 * @return the level3AccNo
 */
public String getLevel3AccNo() {
	return level3AccNo;
}
/**
 * @param level3AccNo the level3AccNo to set
 */
public void setLevel3AccNo(String level3AccNo) {
	this.level3AccNo = level3AccNo;
}
/**
 * @return the level3AccName
 */
public String getLevel3AccName() {
	return level3AccName;
}
/**
 * @param level3AccName the level3AccName to set
 */
public void setLevel3AccName(String level3AccName) {
	this.level3AccName = level3AccName;
}
/**
 * @return the level4AccNo
 */
public String getLevel4AccNo() {
	return level4AccNo;
}
/**
 * @param level4AccNo the level4AccNo to set
 */
public void setLevel4AccNo(String level4AccNo) {
	this.level4AccNo = level4AccNo;
}
/**
 * @return the level4AccName
 */
public String getLevel4AccName() {
	return level4AccName;
}
/**
 * @param level4AccName the level4AccName to set
 */
public void setLevel4AccName(String level4AccName) {
	this.level4AccName = level4AccName;
}
/**
 * @return the notes
 */
public String getNotes() {
	return notes;
}
/**
 * @param notes the notes to set
 */
public void setNotes(String notes) {
	this.notes = notes;
}
public int getsRL_No() {
	return sRL_No;
}
public void setsRL_No(int sRL_No) {
	this.sRL_No = sRL_No;
}
public String getAccountName() {
	return accountName;
}
public void setAccountName(String accountName) {
	this.accountName = accountName;
}
public String getaCTLEVELSwithNO() {
	return aCTLEVELSwithNO;
}
public void setaCTLEVELSwithNO(String aCTLEVELSwithNO) {
	this.aCTLEVELSwithNO = aCTLEVELSwithNO;
}
public String getAccountType() {
	return accountType;
}
public void setAccountType(String accountType) {
	this.accountType = accountType;
}
public int getSubLevel() {
	return subLevel;
}
public void setSubLevel(int subLevel) {
	this.subLevel = subLevel;
}
public int getRec_No() {
	return rec_No;
}
public void setRec_No(int rec_No) {
	this.rec_No = rec_No;
}
public String getListID() {
	return listID;
}
public void setListID(String listID) {
	this.listID = listID;
}
public String getFullName() {
	return fullName;
}
public void setFullName(String fullName) {
	this.fullName = fullName;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getClassName() {
	return className;
}
public void setClassName(String className) {
	this.className = className;
}
public double getTotalBalance() {
	return totalBalance;
}
public void setTotalBalance(double totalBalance) {
	this.totalBalance = totalBalance;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
/**
 * @return the actLevels
 */
public String getActLevels() {
	return ActLevels;
}
/**
 * @param actLevels the actLevels to set
 */
public void setActLevels(String actLevels) {
	ActLevels = actLevels;
}
/**
 * @return the accountNumber
 */
public String getAccountNumber() {
	return accountNumber;
}
/**
 * @param accountNumber the accountNumber to set
 */
public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
}
/**
 * @return the isActive
 */
public String getIsActive() {
	return isActive;
}
/**
 * @param isActive the isActive to set
 */
public void setIsActive(String isActive) {
	this.isActive = isActive;
}
/**
 * @return the balance
 */
public double getBalance() {
	return balance;
}
/**
 * @param balance the balance to set
 */
public void setBalance(double balance) {
	this.balance = balance;
}
/**
 * @return the balaceDate
 */
public Date getBalaceDate() {
	return balaceDate;
}
/**
 * @param balaceDate the balaceDate to set
 */
public void setBalaceDate(Date balaceDate) {
	this.balaceDate = balaceDate;
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
 * @return the bankAccountName
 */
public String getBankAccountName() {
	return bankAccountName;
}
/**
 * @param bankAccountName the bankAccountName to set
 */
public void setBankAccountName(String bankAccountName) {
	this.bankAccountName = bankAccountName;
}
/**
 * @return the bankAcountNumber
 */
public String getBankAcountNumber() {
	return bankAcountNumber;
}
/**
 * @param bankAcountNumber the bankAcountNumber to set
 */
public void setBankAcountNumber(String bankAcountNumber) {
	this.bankAcountNumber = bankAcountNumber;
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
 * @return the iBanNumber
 */
public String getiBanNumber() {
	return iBanNumber;
}
/**
 * @param iBanNumber the iBanNumber to set
 */
public void setiBanNumber(String iBanNumber) {
	this.iBanNumber = iBanNumber;
}
/**
 * @return the bankNameList
 */
public List<String> getBankNameList() {
	return bankNameList;
}
/**
 * @param bankNameList the bankNameList to set
 */
public void setBankNameList(List<String> bankNameList) {
	this.bankNameList = bankNameList;
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
 * @return the selectedBankName
 */
public String getSelectedBankName() {
	return selectedBankName;
}
/**
 * @param selectedBankName the selectedBankName to set
 */
public void setSelectedBankName(String selectedBankName) {
	this.selectedBankName = selectedBankName;
}
/**
 * @return the oldAccountName
 */
public String getOldAccountName() {
	return oldAccountName;
}
/**
 * @param oldAccountName the oldAccountName to set
 */
public void setOldAccountName(String oldAccountName) {
	this.oldAccountName = oldAccountName;
}
/**
 * @return the oldAccountNumber
 */
public String getOldAccountNumber() {
	return oldAccountNumber;
}
/**
 * @param oldAccountNumber the oldAccountNumber to set
 */
public void setOldAccountNumber(String oldAccountNumber) {
	this.oldAccountNumber = oldAccountNumber;
}



}
