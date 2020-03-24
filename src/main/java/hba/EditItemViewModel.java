package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.AccountsModel;
import model.CashInvoiceGridData;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import common.BarcodeGeneration;

public class EditItemViewModel {
	
	//https://fadishei.wordpress.com/2012/03/22/zk-mvvm-design-pattern-and-server-side-paging/
	
	private Logger logger = Logger.getLogger(this.getClass());
	ItemsData data = new ItemsData();
	HBAData hbadata = new HBAData();
	private QbListsModel selectedItem;
	private boolean canSave;
	private boolean selectedCheckBox = false;
	private boolean disableSubOf = true;
	private boolean typeInventoryDisable = false;
	private boolean typeServiceDisable = false;
	private String selectedBarcode = "",selectedBarcodeType="";
	private boolean genBarcDisable = false;

	private List<String> fillItemType = new ArrayList<String>();
	private List<QbListsModel> fillsubItemOfinventroy;
	private List<AccountsModel> fillCogsAccount;
	private List<AccountsModel> fillIncomeAccount;
	private List<AccountsModel> fillAssetAcount;
	private List<AccountsModel> fillPredefinedClass;
	private List<QbListsModel> fillUnitType;
	private List<AccountsModel> fillExpense;
	private String selectedItemType;
	Date creationdate;
	private QbListsModel tempSelectedSubForEdit = new QbListsModel();
	BarcodeGeneration bGen=new BarcodeGeneration();

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();

	private MenuModel companyRole;

	private boolean adminUser;
	
	private boolean activeItems;
	private boolean posItems;
	
	public EditItemViewModel() {
		try {
			QbListsModel unitType = new QbListsModel();
			QbListsModel subIytem = new QbListsModel();
			AccountsModel ExpenseAcc = new AccountsModel();
			AccountsModel cogsAcc = new AccountsModel();
			AccountsModel incomeAcc = new AccountsModel();
			AccountsModel assetAcc = new AccountsModel();
			AccountsModel predefClass = new AccountsModel();

			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int itemKey = (Integer) map.get("itemKey");
			String type = (String) map.get("type");
			if(map.get("posItems") !=null)
			posItems=(Boolean) map.get("posItems");
			Window win = (Window) Path.getComponent("/ItemModalDialog");
			creationdate = df.parse(sdf.format(c.getTime()));
			if (type.equalsIgnoreCase("add")) {
				canSave = true;
				win.setTitle("Add Item Info");
			} else if (type.equalsIgnoreCase("edit")) {
				canSave = true;
				win.setTitle("Edit Item Info");
			} else {
				canSave = false;
				win.setTitle("View Item Info");
			}
			fillItemsType();
			fillsubItemOfinventroy = data.fillsubItemOfinventroy();
			fillCogsAccount = data.fillCogsAccount();
			fillAssetAcount = data.fillAssetAcount();
			fillPredefinedClass = data.fillPredefinedClass();
			fillUnitType = data.fillUnitType();
			fillExpense = data.fillExpense();

			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			if (dbUser != null) {
				adminUser = dbUser.getFirstname().equals("admin");
			}
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);

			if (itemKey > 0) {
				selectedItem = data.getItemsByKeyQuery(itemKey);
				selectedItem.setSelectedItemUnitType(unitType);
				selectedItem.setSelectedFillExpense(ExpenseAcc);
				selectedItem.setSelectedsubItemOfinventroy(subIytem);
				selectedItem.setSelectedPredefinedClass(predefClass);
				selectedItem.setSelectedIncomeAccount(incomeAcc);
				selectedItem.setSelectedCogsAccount(cogsAcc);
				selectedItem.setSelectedAssetAcount(assetAcc);

				if (selectedItem.getIsActive().equals("Y")) {
					activeItems=false;
				} else {
					activeItems=true;
				}
				for (String model : fillItemType) {
					if (model.equalsIgnoreCase(selectedItem.getItemType()))
						selectedItemType = model;

				}

				for (QbListsModel model : fillsubItemOfinventroy) {
					if (selectedItem.getSubOfClasskey() == model.getItem_Key()) {
						selectedItem.setSelectedsubItemOfinventroy(model);
						tempSelectedSubForEdit = model;
						if (tempSelectedSubForEdit.getName().equalsIgnoreCase(
								"none")) {
							selectedCheckBox = false;
						} else {
							selectedCheckBox = true;
						}

						doChecked();

					}

				}
				for (AccountsModel model : fillCogsAccount) {
					if (selectedItem.getCogsAccountRef() == model.getRec_No())
						selectedItem.setSelectedCogsAccount(model);
				}
				if (selectedItemType != null) {
					if (selectedItemType.equalsIgnoreCase("InventoryItem")) {
						typeInventoryDisable = false;
						typeServiceDisable = false;
						fillIncomeAccount = data
								.fillIncomeAccount("InventoryItem");
					} else if (selectedItemType.equalsIgnoreCase("Service")) {
						typeInventoryDisable = true;
						typeServiceDisable = true;
						fillIncomeAccount = data.fillIncomeAccount("Service");

					} else {
						typeInventoryDisable = true;
						typeServiceDisable = true;
						fillIncomeAccount = data.fillIncomeAccount("");
					}
					for (AccountsModel model : fillIncomeAccount) {
						if (selectedItem.getIncomeAccountRef() == model
								.getRec_No())
							selectedItem.setSelectedIncomeAccount(model);
					}
				}
				for (AccountsModel model : fillAssetAcount) {
					if (selectedItem.getAssetAccountRef() == model.getRec_No())
						selectedItem.setSelectedAssetAcount(model);
				}
				for (AccountsModel model : fillPredefinedClass) {
					if (selectedItem.getPredefindedKey() == model.getRec_No())
						selectedItem.setSelectedPredefinedClass(model);
				}
				for (QbListsModel model : fillUnitType) {
					if (selectedItem.getUnit_id() == model.getItem_Key())
						selectedItem.setSelectedItemUnitType(model);
				}
				for (AccountsModel model : fillExpense) {
					if (selectedItem.getCogsAccountRef() == model.getRec_No())
						selectedItem.setSelectedFillExpense(model);
				}

				if (null != selectedItem.getChangePrice()
						&& selectedItem.getChangePrice().equalsIgnoreCase("Y")) {
					selectedItem.setAllowInCashInvoice(true);
				} else {
					selectedItem.setAllowInCashInvoice(false);
				}

				if (null != selectedItem.getAllowEditDescription()
						&& selectedItem.getAllowEditDescription()
								.equalsIgnoreCase("Y")) {
					selectedItem.setEditDescriptionInJB(true);
				} else {
					selectedItem.setEditDescriptionInJB(false);
				}

				selectedBarcode = selectedItem.getBarcode();

			} else {
				selectedItem = new QbListsModel();
				selectedItem.setName("");
				selectedItem.setFullName("");
				selectedItem.setItemType("");
				selectedItem.setListType("");
				selectedItem.setPurchaseDesc("");
				selectedItem.setRecNo(0);
				selectedItem.setSalesPrice(0.0);
				selectedItem.setSubLevel(0);
				selectedItem.setDescArabic("");
				selectedItem.setSalesDesc("");
				selectedItem.setBarcode("");
				selectedItem.setAllowInCashInvoice(false);
				selectedItem.setEditDescriptionInJB(false);
				selectedItem.setEstimatedTime(creationdate);
				selectedItem.setPurchaseCost(0.0);
			//	selectedItem.setIsActive("false");
				activeItems=false;
				selectedItem.setListID("");
				selectedItem.setParent("");
				selectedItem.setQtyOnHand(0);
				selectedItem.setEditSequence(0);
				selectedItem.setSelectedItemUnitType(unitType);
				selectedItem.setSelectedFillExpense(ExpenseAcc);
				selectedItem.setSelectedsubItemOfinventroy(subIytem);
				selectedItem.setSelectedPredefinedClass(predefClass);
				selectedItem.setSelectedIncomeAccount(incomeAcc);
				selectedItem.setSelectedCogsAccount(cogsAcc);
				selectedItem.setSelectedAssetAcount(assetAcc);

			}
			
			selectedBarcodeType=bGen.getBarcodeType(selectedBarcode);
			
		} catch (Exception ex) {
			logger.error("ERROR in EditCustomerViewModel ----> init", ex);
		}
	}

	public void fillItemsType() {
		fillItemType.add("InventoryItem");
		fillItemType.add("Service");
		fillItemType.add("NonInventoryPart");

	}

	public Validator getTodoValidator() {
		return new AbstractValidator() {
			public void validate(ValidationContext ctx) {
				// get the form that will be applied to todo
				QbListsModel fx = (QbListsModel) ctx.getProperty().getValue();
				String name = fx.getName();

				if (Strings.isBlank(name)) {
					Clients.showNotification("Please fill all the required fields (*)  !!");
					// mark the validation is invalid, so the data will not
					// update to bean
					// and the further command will be skipped.
					ctx.setInvalid();
				}
			}
		};
	}

	@Command
	@NotifyChange({ "disableSubOf", "selectedsubItemOfinventroy",
			"fillsubItemOfinventroy" })
	public void doChecked() {
		if (selectedCheckBox == true) {
			disableSubOf = false;
			selectedItem.setSelectedsubItemOfinventroy(tempSelectedSubForEdit);

		} else {
			disableSubOf = true;
			selectedItem.setSelectedsubItemOfinventroy(fillsubItemOfinventroy
					.get(0));
		}

	}

	@Command
	@NotifyChange({ "lstItems", "footer" })
	public void updateItem(@BindingParam("cmp") Window x) {
		int result = 0;

		if (selectedCheckBox == false) {
			selectedItem.setSelectedsubItemOfinventroy(fillsubItemOfinventroy.get(0));
		}
		if (selectedItemType == null) {
			Messagebox.show("Please select the Item Type", "Item List",
					Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (selectedItem.getName().equalsIgnoreCase("")) {
			Messagebox.show("Please enter the Item Name/Number ", "Item List",
					Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (selectedItem.getSelectedIncomeAccount().getRec_No() == 0) {
			Messagebox.show("Please select the Income Account", "Item List",
					Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (selectedItem.getSelectedsubItemOfinventroy() != null
				&& selectedItem.getSelectedsubItemOfinventroy().getName()
						.equalsIgnoreCase(selectedItem.getName())
				&& selectedItem.getSelectedsubItemOfinventroy().getItem_Key() == selectedItem
						.getItem_Key()) {
			Messagebox.show("You cannot make an item a subitem of itself.",
					"Item List", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (selectedItemType != null) {
			selectedItem.setItemType(selectedItemType);
			if (selectedItemType.equalsIgnoreCase("InventoryItem")) {
				if (selectedItem.getSelectedCogsAccount().getRec_No() == 0) {
					Messagebox.show("Please select the COGS Account",
							"Item List", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
				if (selectedItem.getSelectedAssetAcount().getRec_No() == 0) {
					Messagebox.show("Please select the Asset Account",
							"Item List", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
			} else {

				if (selectedItem.getSelectedItemUnitType().getItem_Key() == 0) {
					Messagebox.show("Please select the Unit Type", "Item List",
							Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
				if (selectedItem.getSelectedFillExpense().getRec_No() == 0) {
					Messagebox.show("Please select the Expense Account",
							"Item List", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}

			}
		}
		
		if (selectedBarcode == null){
			selectedItem.setBarcode("");
		}

		List<QbListsModel> QbListNames = data.getFullNameaItemForValidation();
		List<QbListsModel> QbListBarcodes = data.getBarcodeItemForValidation();

		if (selectedItem.isAllowInCashInvoice() == true) {
			selectedItem.setChangePrice("Y");
		} else {
			selectedItem.setChangePrice("N");
		}

		if (selectedItem.isEditDescriptionInJB() == true) {
			selectedItem.setAllowEditDescription("Y");
		} else {
			selectedItem.setAllowEditDescription("N");
		}
		if (!activeItems) {
			selectedItem.setIsActive("Y");
		} else {
			selectedItem.setIsActive("N");
		}

		if (null != selectedItem.getSelectedsubItemOfinventroy()
				&& selectedItem.getSelectedsubItemOfinventroy().getName()
						.equalsIgnoreCase("none")) {
			QbListsModel subIytem = new QbListsModel();
			selectedItem.setSelectedsubItemOfinventroy(subIytem);
		}
		if (selectedItem.getItem_Key() > 0) {
			for (QbListsModel ValidationName : QbListNames) {
				if (selectedItem.getName().equalsIgnoreCase(
						ValidationName.getName())
						&& (selectedItem.getItem_Key() != ValidationName
								.getItem_Key())) {
					Messagebox.show("The Item Name already exist.",
							"Item List", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
			}

			// Test if the barcode already exists for another item
			for (QbListsModel ValidationBarcode : QbListBarcodes) {
				if (null != selectedItem.getBarcode() && selectedItem.getBarcode().equalsIgnoreCase(
						ValidationBarcode.getBarcode())
						&& (selectedItem.getItem_Key() != ValidationBarcode
								.getItem_Key())) {
					Messagebox.show("The Item Barcode already exist.",
							"Item List", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
			}

			result = data.updateItemsData(selectedItem);
			if (result == 1) {
				if (selectedItem.getItem_Key() > 0) {
					Clients.showNotification(
							"The Item Has Been Updated Successfully.",
							Clients.NOTIFICATION_TYPE_INFO, null,
							"middle_center", 10000, true);
					Map args = new HashMap();
					BindUtils.postGlobalCommand(null, null,
							"refreshParentItems", args);
				}
			} else {
				Clients.showNotification("Error at save Items !!");

			}
			x.detach();
		} else {
			for (QbListsModel ValidationName : QbListNames) {
				if (selectedItem.getName().equalsIgnoreCase(
						ValidationName.getName())) {
					Messagebox.show("The Item Name already exist.",
							"Item List", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
			}
			result = data
					.addItemsData(selectedItem, selectedItem.getItemType());
			if (result == 1) {
				if (selectedItem.getItem_Key() > 0) {
					Clients.showNotification(
							"The Item Has Been Saved Successfully.",
							Clients.NOTIFICATION_TYPE_INFO, null,
							"middle_center", 10000, true);
					Map args = new HashMap();
					BindUtils.postGlobalCommand(null, null,
							"refreshParentItems", args);
				}
			} else {
				Clients.showNotification("Error at save Items !!");

			}
			x.detach();
		}

	}

	// Validate the entered barcode.
	// Types of barcodes: EAN8, EAN13, UPCA, UPCE, Barcode128
	@Command
	@NotifyChange({ "selectedBarcode","selectedBarcodeType" })
	public void ValidateBarcode(@BindingParam("cmp") Window x) {
			String barcode;
			
			barcode=bGen.ValidateBarcode(selectedBarcode);
			
			if (barcode==""){
				selectedBarcode ="";
				selectedItem.setBarcode("");
				selectedBarcodeType="";
				return;
			}
			else{
				selectedBarcode =barcode;
				selectedItem.setBarcode(barcode);
			}
			
			selectedBarcodeType=bGen.getBarcodeType(barcode);
	}


	@Command
	@NotifyChange({ "selectedBarcode", "genBarcDisable" ,"selectedBarcodeType"})
	public void GenerateBarcode(@BindingParam("cmp") Window x) {
		String returnVal;

		returnVal = bGen.generateBarcodeRecursive();

		if (returnVal.equals("")) {
			selectedBarcodeType="";
			return;
		} else {
			selectedBarcode = returnVal;
			selectedItem.setBarcode(selectedBarcode);
			// Disable button generate barcode after generation.Enable it if the
			// user modifies the barcode manually
			genBarcDisable = true;
		}
		
		selectedBarcodeType=bGen.getBarcodeType(selectedBarcode);

	}

	@Command
	@NotifyChange({ "genBarcDisable" })
	public void changeBarcode(@BindingParam("cmp") Window x) {
		// Allow the user to regenerate a barcode if he modifies it
		genBarcDisable = false;

	}
	
	@Command
	@NotifyChange({ "selectedBarcodeType" })
	public void changingBarcode(@BindingParam("cmp") Window x) {
		selectedBarcodeType = "";
		
	}
	
	@Command
	public void openItemsCommands(@BindingParam("type") CashInvoiceGridData type)
	{
		//open items popup
		try
		{
		//selectedGridItems=type;
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("selectedRow",type);
		arg.put("lstItems", fillsubItemOfinventroy);
		//arg.put("type","items");
		//arg.put("compSetup", compSetup);
		//arg.put("lstInvcCustomerGridClass", lstInvcCustomerGridClass);
		Executions.createComponents("/hba/payments/itemspopup.zul", null,arg);		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in openItemsCommands ----> onLoad", ex);
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"selectedItem"})
	public void refreshItemsParent(@BindingParam("refreshCommand") String refreshCommand,@BindingParam("selectedItem")QbListsModel selectedPopupItem)
	{
		//logger.info(refreshCommand + " " + selectedPopupItem.getSalesDesc());
		selectedItem.setSelectedsubItemOfinventroy(selectedPopupItem);		
	}
	
	/**
	 * @return the selectedItem
	 */
	public QbListsModel getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem
	 *            the selectedItem to set
	 */
	public void setSelectedItem(QbListsModel selectedItem) {
		this.selectedItem = selectedItem;
	}

	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	/**
	 * @return the disableSubOf
	 */
	public boolean isDisableSubOf() {
		return disableSubOf;
	}

	/**
	 * @param disableSubOf
	 *            the disableSubOf to set
	 */
	public void setDisableSubOf(boolean disableSubOf) {
		this.disableSubOf = disableSubOf;
	}

	/**
	 * @return the selectedCheckBox
	 */
	public boolean isSelectedCheckBox() {
		return selectedCheckBox;
	}

	/**
	 * @param selectedCheckBox
	 *            the selectedCheckBox to set
	 */
	public void setSelectedCheckBox(boolean selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
	}

	/**
	 * @return the fillsubItemOfinventroy
	 */
	public List<QbListsModel> getFillsubItemOfinventroy() {
		return fillsubItemOfinventroy;
	}

	/**
	 * @param fillsubItemOfinventroy
	 *            the fillsubItemOfinventroy to set
	 */
	public void setFillsubItemOfinventroy(
			List<QbListsModel> fillsubItemOfinventroy) {
		this.fillsubItemOfinventroy = fillsubItemOfinventroy;
	}

	/**
	 * @return the fillCogsAccount
	 */
	public List<AccountsModel> getFillCogsAccount() {
		return fillCogsAccount;
	}

	/**
	 * @param fillCogsAccount
	 *            the fillCogsAccount to set
	 */
	public void setFillCogsAccount(List<AccountsModel> fillCogsAccount) {
		this.fillCogsAccount = fillCogsAccount;
	}

	/**
	 * @return the fillIncomeAccount
	 */
	public List<AccountsModel> getFillIncomeAccount() {
		return fillIncomeAccount;
	}

	/**
	 * @param fillIncomeAccount
	 *            the fillIncomeAccount to set
	 */
	public void setFillIncomeAccount(List<AccountsModel> fillIncomeAccount) {
		this.fillIncomeAccount = fillIncomeAccount;
	}

	/**
	 * @return the fillAssetAcount
	 */
	public List<AccountsModel> getFillAssetAcount() {
		return fillAssetAcount;
	}

	/**
	 * @param fillAssetAcount
	 *            the fillAssetAcount to set
	 */
	public void setFillAssetAcount(List<AccountsModel> fillAssetAcount) {
		this.fillAssetAcount = fillAssetAcount;
	}

	/**
	 * @return the fillPredefinedClass
	 */
	public List<AccountsModel> getFillPredefinedClass() {
		return fillPredefinedClass;
	}

	/**
	 * @param fillPredefinedClass
	 *            the fillPredefinedClass to set
	 */
	public void setFillPredefinedClass(List<AccountsModel> fillPredefinedClass) {
		this.fillPredefinedClass = fillPredefinedClass;
	}

	/**
	 * @return the fillUnitType
	 */
	public List<QbListsModel> getFillUnitType() {
		return fillUnitType;
	}

	/**
	 * @param fillUnitType
	 *            the fillUnitType to set
	 */
	public void setFillUnitType(List<QbListsModel> fillUnitType) {
		this.fillUnitType = fillUnitType;
	}

	/**
	 * @return the fillItemType
	 */
	public List<String> getFillItemType() {
		return fillItemType;
	}

	/**
	 * @param fillItemType
	 *            the fillItemType to set
	 */
	public void setFillItemType(List<String> fillItemType) {
		this.fillItemType = fillItemType;
	}

	/**
	 * @return the selectedItemType
	 */
	public String getSelectedItemType() {
		return selectedItemType;
	}

	/**
	 * @param selectedItemType
	 *            the selectedItemType to set
	 */
	@NotifyChange({ "typeInventoryDisable", "typeServiceDisable",
			"fillIncomeAccount", "fillExpense" })
	public void setSelectedItemType(String selectedItemType) {
		this.selectedItemType = selectedItemType;
		if (selectedItemType.equalsIgnoreCase("InventoryItem")) {
			typeInventoryDisable = false;
			typeServiceDisable = false;
			fillIncomeAccount = data.fillIncomeAccount("InventoryItem");
		} else if (selectedItemType.equalsIgnoreCase("Service")) {
			typeInventoryDisable = true;
			typeServiceDisable = true;
			fillIncomeAccount = data.fillIncomeAccount("Service");

		} else {
			typeInventoryDisable = true;
			typeServiceDisable = true;
			fillIncomeAccount = data.fillIncomeAccount("");
		}

	}

	/**
	 * @return the typeInventoryDisable
	 */
	public boolean isTypeInventoryDisable() {
		return typeInventoryDisable;
	}

	/**
	 * @param typeInventoryDisable
	 *            the typeInventoryDisable to set
	 */
	public void setTypeInventoryDisable(boolean typeInventoryDisable) {
		this.typeInventoryDisable = typeInventoryDisable;
	}

	/**
	 * @return the typeServiceDisable
	 */
	public boolean isTypeServiceDisable() {
		return typeServiceDisable;
	}

	/**
	 * @param typeServiceDisable
	 *            the typeServiceDisable to set
	 */
	public void setTypeServiceDisable(boolean typeServiceDisable) {
		this.typeServiceDisable = typeServiceDisable;
	}

	/**
	 * @return the fillExpense
	 */
	public List<AccountsModel> getFillExpense() {
		return fillExpense;
	}

	/**
	 * @param fillExpense
	 *            the fillExpense to set
	 */
	public void setFillExpense(List<AccountsModel> fillExpense) {
		this.fillExpense = fillExpense;
	}

	public String getSelectedBarcode() {
		return selectedBarcode;
	}

	public void setSelectedBarcode(String selectedBarcode) {
		this.selectedBarcode = selectedBarcode;
	}

	public boolean isGenBarcDisable() {
		return genBarcDisable;
	}

	public void setGenBarcDisable(boolean genBarcDisable) {
		genBarcDisable = genBarcDisable;
	}

	public String getSelectedBarcodeType() {
		return selectedBarcodeType;
	}

	public void setSelectedBarcodeType(String selectedBarcodeType) {
		this.selectedBarcodeType = selectedBarcodeType;
	}


	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= hbadata.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==75)
			{
				companyRole=item;
				break;
			}
		}
	}

	/**
	 * @return the activeItems
	 */
	public boolean isActiveItems() {
		return activeItems;
	}

	/**
	 * @param activeItems the activeItems to set
	 */
	public void setActiveItems(boolean activeItems) {
		
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && activeItems==false)
		{
			Clients.showNotification("You are not allowed to Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && activeItems==true)
		{
			Clients.showNotification("You are not allowed to In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.activeItems = activeItems;
		}
	}

	public boolean isPosItems() {
		return posItems;
	}

	public void setPosItems(boolean posItems) {
		this.posItems = posItems;
	}

	
	
}
