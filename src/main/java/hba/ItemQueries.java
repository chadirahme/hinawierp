package hba;

import java.text.SimpleDateFormat;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import model.BarcodeSettingsModel;
import model.CustomerModel;
import model.QbListsModel;

public class ItemQueries {

	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	WebusersModel dbUser=null;
	
	
	public ItemQueries()
	{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
	}

	// Item List
	public String GetQBItemsListQuery(String isActive) {
		query = new StringBuffer();
		query.append(" Select QBItems.Sublevel,Item_Key,QBItems.Name,ItemType,IncomeAccountRef,Accounts.AccountName,QBItems.IsActive,QBItems.fullname,SalesDesc,PurchaseDesc,PurchaseCost,SalesPrice ");
		query.append(" ,QuantityOnHand,AverageCost,QuantityOnOrder,QuantityOnSalesOrder,ReorderPoint ");
		query.append(" from QBItems inner join Accounts on QBItems.IncomeAccountRef=Accounts.Rec_No ");
		if (!isActive.equalsIgnoreCase(""))
			query.append(" Where QBItems.IsActive ='" + isActive
					+ "' and Accounts.isactive='" + isActive + "'");
		query.append(" order by QBItems.fullname");
		return query.toString();
	}
	
	// Item List having barcode
	public String GetQBItemsListBArcodeQuery(String isActive,boolean onlyBarcodes) {
		query = new StringBuffer();
		query.append(" Select Item_Key,QBItems.Name,ItemType,QBItems.IsActive,QBItems.fullname,SalesDesc,PurchaseDesc,SalesPrice ");
		query.append(" ,QuantityOnHand,barcode ");
		query.append(" from QBItems ");
		query.append(" Where 1=1");
		if (onlyBarcodes){
			query.append(" and barcode<>'' and barcode is not null");	
		}
		if (!isActive.equalsIgnoreCase(""))
			query.append(" and QBItems.IsActive ='" + isActive + "'");
		query.append(" order by QBItems.fullname");
		return query.toString();
	}

	public String updateItemsData(QbListsModel obj, String flag) {
		String editedFromOnline="Y";
		query = new StringBuffer();
		query.append("update QBItems set ListID='" + obj.getListID()
				+ "',EditSequence=" + obj.getEditSequence()
				+ ",subof_parent_key="
				+ obj.getSelectedsubItemOfinventroy().getItem_Key()
				+ ",ClassKey=" + obj.getSelectedPredefinedClass().getRec_No()
				+ ",Name='" + obj.getName() + "',FullName='"
				+ obj.getFullName() + "',Parent='" + obj.getParent()
				+ "',IsActive='" + obj.getIsActive() + "',ItemType='"
				+ obj.getItemType() + "',SalesDesc='" + obj.getSalesDesc()
				+ "',SalesPrice='" + obj.getSalesPrice() + "',Barcode='"
				+ obj.getBarcode() + "',SubLevel=" + obj.getSubLevel() + ",");
		if (!flag.equalsIgnoreCase("InventoryItem")) {
			query.append("COGSAccountRef="
					+ obj.getSelectedFillExpense().getRec_No() + "");
		} else {
			query.append("COGSAccountRef="
					+ obj.getSelectedCogsAccount().getRec_No() + "");
		}
		query.append(",PurchaseDesc='" + obj.getPurchaseDesc()
				+ "',PurchaseCost='" + obj.getPurchaseCost()
				+ "',QuantityOnHand=" + obj.getQtyOnHand() + ",ReorderPoint="
				+ obj.getReOrderPoint() + ",IncomeAccountRef="
				+ obj.getSelectedIncomeAccount().getRec_No()
				+ ",AssetAccountRef="
				+ obj.getSelectedAssetAcount().getRec_No() + ",UNIT_ID="
				+ obj.getSelectedItemUnitType().getItem_Key()
				+ ",DescriptionAR='" + obj.getDescArabic() + "'");
		if (!flag.equalsIgnoreCase("InventoryItem")) {
			query.append(",MIN_HRS=" + obj.getMinHours() + ",CHANGE_PRICE='"
					+ obj.getChangePrice() + "',AllowEditDesc='"
					+ obj.getAllowEditDescription() + "',editedFromOnline='"+editedFromOnline+"'");
		}
		query.append("Where  item_Key=" + obj.getItem_Key());

		return query.toString();
	}

	public String getItemsByKeyQuery(int itemKey) {
		query = new StringBuffer();
		query.append("Select * from QBItems  Where  item_key = " + itemKey);
		return query.toString();
	}

	public String getMaxIDQuery(String tableName, String fieldName) {
		query = new StringBuffer();
		query.append(" select max(" + fieldName + ") from " + tableName);
		return query.toString();
	}

	public String addItemsData(QbListsModel obj, String flag) {
		query = new StringBuffer();
		query.append("INSERT INTO QBItems(item_Key,ListID,EditSequence,ClassKey,Name,FullName,Parent,IsActive,SubLevel,ItemType,SalesDesc,SalesPrice,COGSAccountRef,PurchaseDesc,PurchaseCost,QuantityOnHand,ReorderPoint,IncomeAccountRef,AssetAccountRef,UNIT_ID,DescriptionAR,subof_parent_key,Barcode");
		if (!flag.equalsIgnoreCase("InventoryItem")) {
			query.append(",MIN_HRS,CHANGE_PRICE,AllowEditDesc)");
		} else {
			query.append(")");
		}
		query.append(" VALUES( " + obj.getItem_Key() + ",'" + obj.getListID()
				+ "' , '" + obj.getEditSequence() + "' , "
				+ obj.getSelectedPredefinedClass().getRec_No() + " , '"
				+ obj.getName() + "', '" + obj.getFullName() + "' , '"
				+ obj.getParent() + "' , '" + obj.getIsActive() + "' , "
				+ obj.getSubLevel() + " , '" + obj.getItemType() + "' , '"
				+ obj.getSalesDesc() + "', '" + obj.getSalesPrice() + "' , ");
		if (!flag.equalsIgnoreCase("InventoryItem")) {
			query.append("" + obj.getSelectedFillExpense().getRec_No() + ","
					+ "");
		} else {
			query.append("" + obj.getSelectedCogsAccount().getRec_No() + ","
					+ "");
		}
		query.append("'" + obj.getPurchaseDesc() + "','"
				+ obj.getPurchaseCost() + "' ,'" + obj.getQtyOnHand() + "' ,"
				+ obj.getReOrderPoint() + " ,"
				+ obj.getSelectedIncomeAccount().getRec_No() + " ,"
				+ obj.getSelectedAssetAcount().getRec_No() + ","
				+ obj.getSelectedItemUnitType().getItem_Key() + ",'"
				+ obj.getDescArabic() + "',"
				+ obj.getSelectedsubItemOfinventroy().getItem_Key() + ",'"
				+ obj.getBarcode() + "'");
		if (!flag.equalsIgnoreCase("InventoryItem")) {
			query.append("," + obj.getMinHours() + ",'" + obj.getChangePrice()
					+ "','" + obj.getAllowEditDescription() + "'");
		} else {
			query.append("");
		}
		query.append(" )");
		return query.toString();
	}

	public String fillsubItemOfinventroy() {
		query = new StringBuffer();
		query.append(" SELECT name,item_key,ListID,ItemType,SubLevel,FullName FROM QBItems where ItemType='InventoryItem' and sublevel<4 and IsActive='Y' order by ItemType,FullName");
		return query.toString();
	}

	public String fillCogsAccount() {
		query = new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],AccountType ,SubLevel,   Rec_No , ListID FROM Accounts Inner join ");
		query.append("AccountType on AccountType.TypeName = Accounts.AccountType where ");
		query.append("AccountType in ('Bank','AccountsReceivable', ");
		query.append("'OtherCurrentAsset','AccountsPayable','CreditCard','OtherCurrentLiability','Equity','Income','OtherIncome', ");
		query.append("'CostOfGoodsSold','Expense','FixedAsset') and IsActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();
	}

	public String fillIncomeAccount(String flag) {
		query = new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],AccountType,SubLevel,Rec_No , ListID FROM Accounts Inner join ");
		query.append("AccountType on AccountType.TypeName = Accounts.AccountType where ");
		query.append("AccountType in ('Bank','AccountsReceivable','OtherCurrentAsset','AccountsPayable','CreditCard','OtherCurrentLiability','Equity','Income','OtherIncome',");
		if (!flag.equalsIgnoreCase("InventoryItem")) {
			query.append("'OtherExpense',");
		}
		query.append("'CostOfGoodsSold','Expense','FixedAsset') and IsActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();
	}

	public String fillAssetAcount(String isActive) {
		query = new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],");
		query.append(" AccountType ,   SubLevel    ,   Rec_No , ListID FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType where  ");
		query.append("AccountType in ('Bank','AccountsReceivable',");
		query.append("'OtherCurrentAsset','AccountsPayable',");
		query.append("'CreditCard','OtherCurrentLiability','Equity','Income','OtherIncome', ");
		query.append("'CostOfGoodsSold','Expense','FixedAsset') and IsActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();
	}

	public String fillPredefinedClass() {
		query = new StringBuffer();
		query.append("Select Name,Class_Key,SubLevel,FullName,ListID from [Class] Where IsActive='Y' order by FullName");
		return query.toString();
	}

	public String fillUnitType() {
		query = new StringBuffer();
		query.append("SELECT DESCRIPTION,UNIT_ID ");
		 if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
		  {
			  query.append(" From ITEMUNITS");
		  }
		  else
		  {
			  query.append(" From UNITMAST");
		  }
		return query.toString();
	}

	public String fillExpenseAccount() {
		query = new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],AccountType,SubLevel,Rec_No, ListID FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType where");
		query.append(" AccountType in ('Bank','AccountsReceivable','OtherCurrentAsset','AccountsPayable','CreditCard','OtherCurrentLiability','Equity','Income','OtherIncome',");
		query.append("'CostOfGoodsSold','Expense','FixedAsset') and IsActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();
	}

	public String getFullNameaItemForValidation() {
		query = new StringBuffer();
		query.append("Select name,Item_Key from QBItems");
		return query.toString();
	}

	public String getBarcodeItemForValidation() {
		query = new StringBuffer();
		query.append("Select Barcode,Item_Key from QBItems");
		return query.toString();
	}

}
