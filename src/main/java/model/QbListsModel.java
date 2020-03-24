package model;

import java.util.Date;

import org.zkoss.bind.annotation.NotifyChange;

public class QbListsModel {

	//test commit 123
	//test commit 1234
	private int recNo;
	private double qtyOnHand;
	private String name;
	private String listID;
	private String listType;
	private int subLevel;
	private String fullName;
	private int editSequence;
	
	private String intials;

	private String purchaseDesc;
	private double purchaseCost;
	private String parent;
	private String itemType;

	private Date estimatedTime;

	private int item_Key;
	private int incomeAccountRef;
	private String salesDesc;
	private double salesPrice;
	private String accountName;
	private String isActive;
	private String descArabic;
	private double reOrderPoint;
	private QbListsModel selectedsubItemOfinventroy;
	private AccountsModel selectedCogsAccount;
	private AccountsModel selectedIncomeAccount;
	private AccountsModel selectedAssetAcount;
	private AccountsModel selectedPredefinedClass;
	private QbListsModel selectedItemUnitType;
	private AccountsModel selectedFillExpense;

	private boolean editDescriptionInJB;
	private boolean allowInCashInvoice;

	private int subOfClasskey;
	private int cogsAccountRef;
	private int assetAccountRef;
	private int unit_id;
	private int expenceAccountKey;
	private int predefindedKey;
	private double minHours;

	private String changePrice;

	private String allowEditDescription;
	
	private String barcode;
	
	private int printQtyBarcode=1;
	private boolean checkedItem;
	
	private int hrEmpKey;

	private int subItemsCount;

	private String invoiceDescription;
	private String invoicearabicDescription;
	private int invoiceQtyOnHand;
	private double invoiceRate;
	private double AvgCost;
	private int selectedClass;

	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getListID() {
		return listID;
	}

	public void setListID(String listID) {
		this.listID = listID;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public int getSubLevel() {
		return subLevel;
	}

	public void setSubLevel(int subLevel) {
		this.subLevel = subLevel;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	/**
	 * @return the purchaseDesc
	 */
	public String getPurchaseDesc() {
		return purchaseDesc;
	}

	/**
	 * @param purchaseDesc the purchaseDesc to set
	 */
	public void setPurchaseDesc(String purchaseDesc) {
		this.purchaseDesc = purchaseDesc;
	}

	/**
	 * @return the purchaseCost
	 */
	public double getPurchaseCost() {
		return purchaseCost;
	}

	/**
	 * @param purchaseCost the purchaseCost to set
	 */
	public void setPurchaseCost(double purchaseCost) {
		this.purchaseCost = purchaseCost;
	}



	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public int getItem_Key() {
		return item_Key;
	}

	public void setItem_Key(int item_Key) {
		this.item_Key = item_Key;
	}

	public int getIncomeAccountRef() {
		return incomeAccountRef;
	}

	public void setIncomeAccountRef(int incomeAccountRef) {
		this.incomeAccountRef = incomeAccountRef;
	}

	public String getSalesDesc() {
		return salesDesc;
	}

	public void setSalesDesc(String salesDesc) {
		this.salesDesc = salesDesc;
	}



	/**
	 * @return the salesPrice
	 */
	public double getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice the salesPrice to set
	 */
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the descArabic
	 */
	public String getDescArabic() {
		return descArabic;
	}

	/**
	 * @param descArabic the descArabic to set
	 */
	public void setDescArabic(String descArabic) {
		this.descArabic = descArabic;
	}

	/**
	 * @return the reOrderPoint
	 */
	public double getReOrderPoint() {
		return reOrderPoint;
	}

	/**
	 * @param reOrderPoint the reOrderPoint to set
	 */
	public void setReOrderPoint(double reOrderPoint) {
		this.reOrderPoint = reOrderPoint;
	}

	/**
	 * @return the selectedsubItemOfinventroy
	 */
	public QbListsModel getSelectedsubItemOfinventroy() {
		return selectedsubItemOfinventroy;
	}

	/**
	 * @param selectedsubItemOfinventroy the selectedsubItemOfinventroy to set
	 */
	@NotifyChange({"selectedsubItemOfinventroy"})
	public void setSelectedsubItemOfinventroy(
			QbListsModel selectedsubItemOfinventroy) {
		this.selectedsubItemOfinventroy = selectedsubItemOfinventroy;
	}

	/**
	 * @return the selectedCogsAccount
	 */
	public AccountsModel getSelectedCogsAccount() {
		return selectedCogsAccount;
	}

	/**
	 * @param selectedCogsAccount the selectedCogsAccount to set
	 */
	public void setSelectedCogsAccount(AccountsModel selectedCogsAccount) {
		this.selectedCogsAccount = selectedCogsAccount;
	}

	/**
	 * @return the selectedIncomeAccount
	 */
	public AccountsModel getSelectedIncomeAccount() {
		return selectedIncomeAccount;
	}

	/**
	 * @param selectedIncomeAccount the selectedIncomeAccount to set
	 */
	public void setSelectedIncomeAccount(AccountsModel selectedIncomeAccount) {
		this.selectedIncomeAccount = selectedIncomeAccount;
	}

	/**
	 * @return the selectedAssetAcount
	 */
	public AccountsModel getSelectedAssetAcount() {
		return selectedAssetAcount;
	}

	/**
	 * @param selectedAssetAcount the selectedAssetAcount to set
	 */
	public void setSelectedAssetAcount(AccountsModel selectedAssetAcount) {
		this.selectedAssetAcount = selectedAssetAcount;
	}

	/**
	 * @return the selectedPredefinedClass
	 */
	public AccountsModel getSelectedPredefinedClass() {
		return selectedPredefinedClass;
	}

	/**
	 * @param selectedPredefinedClass the selectedPredefinedClass to set
	 */
	public void setSelectedPredefinedClass(AccountsModel selectedPredefinedClass) {
		this.selectedPredefinedClass = selectedPredefinedClass;
	}

	/**
	 * @return the selectedItemUnitType
	 */
	public QbListsModel getSelectedItemUnitType() {
		return selectedItemUnitType;
	}

	/**
	 * @param selectedItemUnitType the selectedItemUnitType to set
	 */
	public void setSelectedItemUnitType(QbListsModel selectedItemUnitType) {
		this.selectedItemUnitType = selectedItemUnitType;
	}

	/**
	 * @return the estimatedTime
	 */
	public Date getEstimatedTime() {
		return estimatedTime;
	}

	/**
	 * @param estimatedTime the estimatedTime to set
	 */
	public void setEstimatedTime(Date estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	/**
	 * @return the editDescriptionInJB
	 */
	public boolean isEditDescriptionInJB() {
		return editDescriptionInJB;
	}

	/**
	 * @param editDescriptionInJB the editDescriptionInJB to set
	 */
	public void setEditDescriptionInJB(boolean editDescriptionInJB) {
		this.editDescriptionInJB = editDescriptionInJB;
	}

	/**
	 * @return the allowInCashInvoice
	 */
	public boolean isAllowInCashInvoice() {
		return allowInCashInvoice;
	}

	/**
	 * @param allowInCashInvoice the allowInCashInvoice to set
	 */
	public void setAllowInCashInvoice(boolean allowInCashInvoice) {
		this.allowInCashInvoice = allowInCashInvoice;
	}

	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * @return the editSequence
	 */
	public int getEditSequence() {
		return editSequence;
	}

	/**
	 * @param editSequence the editSequence to set
	 */
	public void setEditSequence(int editSequence) {
		this.editSequence = editSequence;
	}

	/**
	 * @return the qtyOnHand
	 */
	public double getQtyOnHand() {
		return qtyOnHand;
	}

	/**
	 * @param qtyOnHand the qtyOnHand to set
	 */
	public void setQtyOnHand(double qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}

	/**
	 * @return the selectedFillExpense
	 */
	public AccountsModel getSelectedFillExpense() {
		return selectedFillExpense;
	}

	/**
	 * @param selectedFillExpense the selectedFillExpense to set
	 */
	public void setSelectedFillExpense(AccountsModel selectedFillExpense) {
		this.selectedFillExpense = selectedFillExpense;
	}

	/**
	 * @return the subOfClasskey
	 */
	public int getSubOfClasskey() {
		return subOfClasskey;
	}

	/**
	 * @param subOfClasskey the subOfClasskey to set
	 */
	public void setSubOfClasskey(int subOfClasskey) {
		this.subOfClasskey = subOfClasskey;
	}

	/**
	 * @return the cogsAccountRef
	 */
	public int getCogsAccountRef() {
		return cogsAccountRef;
	}

	/**
	 * @param cogsAccountRef the cogsAccountRef to set
	 */
	public void setCogsAccountRef(int cogsAccountRef) {
		this.cogsAccountRef = cogsAccountRef;
	}

	/**
	 * @return the assetAccountRef
	 */
	public int getAssetAccountRef() {
		return assetAccountRef;
	}

	/**
	 * @param assetAccountRef the assetAccountRef to set
	 */
	public void setAssetAccountRef(int assetAccountRef) {
		this.assetAccountRef = assetAccountRef;
	}

	/**
	 * @return the unit_id
	 */
	public int getUnit_id() {
		return unit_id;
	}

	/**
	 * @param unit_id the unit_id to set
	 */
	public void setUnit_id(int unit_id) {
		this.unit_id = unit_id;
	}

	/**
	 * @return the expenceAccountKey
	 */
	public int getExpenceAccountKey() {
		return expenceAccountKey;
	}

	/**
	 * @param expenceAccountKey the expenceAccountKey to set
	 */
	public void setExpenceAccountKey(int expenceAccountKey) {
		this.expenceAccountKey = expenceAccountKey;
	}

	/**
	 * @return the predefindedKey
	 */
	public int getPredefindedKey() {
		return predefindedKey;
	}

	/**
	 * @param predefindedKey the predefindedKey to set
	 */
	public void setPredefindedKey(int predefindedKey) {
		this.predefindedKey = predefindedKey;
	}

	/**
	 * @return the minHours
	 */
	public double getMinHours() {
		return minHours;
	}

	/**
	 * @param minHours the minHours to set
	 */
	public void setMinHours(double minHours) {
		this.minHours = minHours;
	}

	/**
	 * @return the changePrice
	 */
	public String getChangePrice() {
		return changePrice;
	}

	/**
	 * @param changePrice the changePrice to set
	 */
	public void setChangePrice(String changePrice) {
		this.changePrice = changePrice;
	}

	/**
	 * @return the allowEditDescription
	 */
	public String getAllowEditDescription() {
		return allowEditDescription;
	}

	/**
	 * @param allowEditDescription the allowEditDescription to set
	 */
	public void setAllowEditDescription(String allowEditDescription) {
		this.allowEditDescription = allowEditDescription;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getPrintQtyBarcode() {
		return printQtyBarcode;
	}

	public void setPrintQtyBarcode(int printQtyBarcode) {
		this.printQtyBarcode = printQtyBarcode;
	}

	public boolean isCheckedItem() {
		return checkedItem;
	}

	public void setCheckedItem(boolean checkedItem) {
		this.checkedItem = checkedItem;
	}

	public String getIntials() {
		return intials;
	}

	public void setIntials(String intials) {
		this.intials = intials;
	}

	/**
	 * @return the hrEmpKey
	 */
	public int getHrEmpKey() {
		return hrEmpKey;
	}

	/**
	 * @param hrEmpKey the hrEmpKey to set
	 */
	public void setHrEmpKey(int hrEmpKey) {
		this.hrEmpKey = hrEmpKey;
	}

	public int getSubItemsCount() {
		return subItemsCount;
	}

	public void setSubItemsCount(int subItemsCount) {
		this.subItemsCount = subItemsCount;
	}

	public String getInvoiceDescription() {
		return invoiceDescription;
	}

	public void setInvoiceDescription(String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
	}

	public String getInvoicearabicDescription() {
		return invoicearabicDescription;
	}

	public void setInvoicearabicDescription(String invoicearabicDescription) {
		this.invoicearabicDescription = invoicearabicDescription;
	}

	public int getInvoiceQtyOnHand() {
		return invoiceQtyOnHand;
	}

	public void setInvoiceQtyOnHand(int invoiceQtyOnHand) {
		this.invoiceQtyOnHand = invoiceQtyOnHand;
	}

	public double getInvoiceRate() {
		return invoiceRate;
	}

	public void setInvoiceRate(double invoiceRate) {
		this.invoiceRate = invoiceRate;
	}

	public double getAvgCost() {
		return AvgCost;
	}

	public void setAvgCost(double avgCost) {
		AvgCost = avgCost;
	}

	public int getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(int selectedClass) {
		this.selectedClass = selectedClass;
	}
	
	


}
