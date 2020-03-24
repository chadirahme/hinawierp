package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.AccountsModel;
import model.BarcodeSettingsModel;
import model.CashInvoiceModel;
import model.ChartOfAccountModel;
import model.CompanyDBModel;
import model.CustomerModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

public class ItemsData {
	
	private Logger logger = Logger.getLogger(ItemsData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public ItemsData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HBAQueries query=new HBAQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in CustomerData---Init-->" , ex);
		}
	}
	
	public boolean checkIfBcExist(String Barcode){
		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		boolean returnVal=false;
		
		try {
			rs=db.executeNonQuery(query.GetBarcode(Barcode));
			
			while(rs.next()){
				returnVal= true;
			}
			
		} catch (Exception ex) {
			logger.error("error in ItemsData---fillQbItemsLists-->" , ex);
		}
		
		return returnVal;
	}
	
	//Items List
	public List<QbListsModel> fillQbItemsLists(String isActive)
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetQBItemsListQuery(isActive));
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setItem_Key(rs.getInt("item_key"));
				obj.setName(rs.getString("Name"));	
				obj.setParent("");
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setIncomeAccountRef(rs.getInt("IncomeAccountRef"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setSalesDesc(rs.getString("SalesDesc")==null?"":rs.getString("SalesDesc"));
				obj.setPurchaseDesc(rs.getString("PurchaseDesc"));
				obj.setPurchaseCost(rs.getDouble("PurchaseCost"));
				obj.setSalesPrice(rs.getDouble("SalesPrice"));
				obj.setIsActive(rs.getString("isactive"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else
				{
					obj.setIsActive("INActive");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillQbItemsLists-->" , ex);
		}
		return lst;
	}
	
	//Items List having a barcode
	public List<QbListsModel> fillQbItemsListsBarcode(String isActive,boolean onlyBarcodes)
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetQBItemsListBArcodeQuery(isActive,onlyBarcodes));
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setItem_Key(rs.getInt("item_key"));
				obj.setName(rs.getString("Name"));	
				obj.setFullName(rs.getString("FullName"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setSalesDesc(rs.getString("SalesDesc")==null?"":rs.getString("SalesDesc"));
				obj.setPurchaseDesc(rs.getString("PurchaseDesc"));
				obj.setSalesPrice(rs.getDouble("SalesPrice"));
				obj.setIsActive(rs.getString("isactive"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else
				{
					obj.setIsActive("INActive");
				}
				obj.setBarcode(rs.getString("barcode"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillQbItemsListsBarcode-->" , ex);
		}
		return lst;
	}
			
	
	public QbListsModel getItemsByKeyQuery(int itemKey)
	{
		QbListsModel obj=new QbListsModel();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getItemsByKeyQuery(itemKey));
			while(rs.next())
			{				
				obj.setItem_Key(rs.getInt("item_key"));
				obj.setListID(rs.getString("ListID"));
				obj.setEditSequence(rs.getInt("EditSequence"));
				obj.setSubOfClasskey(rs.getInt("subof_parent_key"));
				obj.setName(rs.getString("Name"));			
				obj.setFullName(rs.getString("FullName"));
				obj.setParent(rs.getString("Parent"));
				obj.setIsActive(rs.getString("IsActive"));	
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setSalesDesc(rs.getString("SalesDesc")==null?"":rs.getString("SalesDesc"));
				obj.setPurchaseDesc(rs.getString("PurchaseDesc"));
				obj.setPurchaseCost(rs.getDouble("PurchaseCost"));
				obj.setSalesPrice(rs.getDouble("SalesPrice"));
				obj.setIncomeAccountRef(rs.getInt("IncomeAccountRef"));
				obj.setQtyOnHand(rs.getDouble("QuantityOnHand"));
				obj.setReOrderPoint(rs.getDouble("ReorderPoint"));
				obj.setCogsAccountRef(rs.getInt("COGSAccountRef"));
				obj.setAssetAccountRef(rs.getInt("AssetAccountRef"));
				obj.setUnit_id(rs.getInt("UNIT_ID"));
				obj.setPredefindedKey(rs.getInt("ClassKey"));
				obj.setMinHours(rs.getDouble("MIN_HRS"));
				obj.setChangePrice(rs.getString("CHANGE_PRICE"));
				obj.setAllowEditDescription(rs.getString("AllowEditDesc"));
				obj.setDescArabic(rs.getString("DescriptionAR"));
				obj.setBarcode(rs.getString("Barcode"));
			}
		}
		
		catch (Exception ex) {
			logger.error("error in ItemsData---getItemsByKeyQuery-->" , ex);
		}
		return obj;
	}
	
	public void updateBarcodeCounter(String barcCounter)
	{
		StringBuffer query;
		
		try {
			query=new StringBuffer();		
			query.append("update BARCODESETTINGS set BARCODE_COUNTER='" + barcCounter + "'");
			
			db.executeUpdateQuery(query.toString());
			
		} catch (Exception ex) {
			logger.error("error in ItemsData---updateBarcodeCounter-->" , ex);
		}
		
	}
	
	
	public int updateItemsData(QbListsModel model)
	{
		int result=0;
	
		ItemQueries query=new ItemQueries();	
		if(model.getSelectedsubItemOfinventroy()!=null && model.getSelectedsubItemOfinventroy().getItem_Key()!=0)
		{
			QbListsModel subOf=new QbListsModel();
			subOf=model.getSelectedsubItemOfinventroy();
			model.setFullName(subOf.getFullName()+":"+model.getName());
			model.setSubLevel(subOf.getSubLevel()+1);
			model.setParent(subOf.getListID());
		}
		else
		{
			model.setFullName(model.getName());
			model.setSubLevel(0);
			model.setParent("");
		}
		
		try 
		{			
			result=db.executeUpdateQuery(query.updateItemsData(model,model.getItemType()));			
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---updateItemsData-->" , ex);
		}
		return result;
		
	}
	
	public int addItemsData(QbListsModel obj,String flag)
	{
		int result=0;
	
		ItemQueries query=new ItemQueries();	
		if(obj.getSelectedsubItemOfinventroy()!=null && obj.getSelectedsubItemOfinventroy().getItem_Key()!=0)
		{
			QbListsModel subOf=new QbListsModel();
			subOf=obj.getSelectedsubItemOfinventroy();
			obj.setFullName(subOf.getFullName()+":"+obj.getName());
			obj.setSubLevel(subOf.getSubLevel()+1);
			obj.setParent(subOf.getListID());
		}
		else
		{
			obj.setFullName(obj.getName());
			obj.setSubLevel(0);
			obj.setParent("");
		}
		
		try 
		{			
			int newID=getMaxID("QBItems", "Item_Key");
			obj.setItem_Key(newID);
			result=db.executeUpdateQuery(query.addItemsData(obj,flag));		
			result=1;
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---addItemsData-->" , ex);
		}
		return result;
		
	}
	public int getMaxID(String tableName,String fieldName)
	{
		int result=0;		
		ItemQueries query=new ItemQueries();	
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in ItemsData---getMaxID-->" , ex);
		}	
		return result;
	}
	
	public List<QbListsModel> fillsubItemOfinventroy()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillsubItemOfinventroy());
			QbListsModel newobj=new QbListsModel();
			newobj.setItem_Key(0);
			newobj.setListID("");
			newobj.setName("None");					
			newobj.setSubLevel(0);
			newobj.setFullName("");
			newobj.setItemType("");
			newobj.setSalesDesc("None");
			lst.add(newobj);
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setItem_Key(rs.getInt("item_key"));
				obj.setListID(rs.getString("listId"));
				obj.setName(rs.getString("Name"));					
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setSalesDesc(rs.getString("FullName")  == null ? "" : rs.getString("FullName"));			
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillsubItemOfinventroy-->" , ex);
		}
		return lst;
		
	}
	public List<AccountsModel> fillCogsAccount()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillCogsAccount());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("listid"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillCogsAccount-->" , ex);
		}
		return lst;
		
	}
	public List<AccountsModel> fillIncomeAccount(String flag)
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillIncomeAccount(flag));
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("listid"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillIncomeAccount-->" , ex);
		}
		return lst;
		
	}
	public List<AccountsModel> fillAssetAcount()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillAssetAcount(""));
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("listid"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillAssetAcount-->" , ex);
		}
		return lst;
	}
	
	public List<AccountsModel> fillPredefinedClass()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillPredefinedClass());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Class_key"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("listid"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillPredefinedClass-->" , ex);
		}
		return lst;
	}
	
	public List<QbListsModel> fillUnitType()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillUnitType());
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setItem_Key(rs.getInt("unit_id"));
				obj.setSalesDesc(rs.getString("description"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillUnitType-->" , ex);
		}
		return lst;
	
	}
	
	public BarcodeSettingsModel fillBarcodeSettings()
	{
		BarcodeSettingsModel bSettings=null;
		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		try {
			
			rs=db.executeNonQuery(query.GetBarcodeSettingsQuery());

			while(rs.next()){
				bSettings=new BarcodeSettingsModel();
				bSettings.setBarcodeConvert(rs.getString("BARCODE_FLAG"));
				bSettings.setBarcodeCounter(rs.getString("BARCODE_COUNTER"));
				bSettings.setBarcodeType(rs.getString("BARCODE_TYPE"));
//				bSettings.setBarcodeDefaultPrinter(rs.getString("BARCODE_DefaultPrinter"));
				bSettings.setBarcodeAfterScanGoTo(rs.getString("BARCODE_AfterScanGoTo"));
			}
		} catch (Exception ex) {
			logger.error("error in ItemsData---GetBarcodeSettings-->" , ex);
		}
		
			return bSettings;
		
	}
	
	public List<AccountsModel> fillExpense()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillExpenseAccount());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_no"));
				obj.setName(rs.getString("Name"));
				obj.setAccountType(rs.getString("accountType"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("listid"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---fillPredefinedClass-->" , ex);
		}
		return lst;
	}
	
	
	public List<QbListsModel> getBarcodeItemForValidation()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		String name="";
		try 
		{
			rs=db.executeNonQuery(query.getBarcodeItemForValidation());
			while(rs.next())
			{
				QbListsModel barcodeObj=new QbListsModel();
				barcodeObj.setBarcode(rs.getString("Barcode"));
				barcodeObj.setItem_Key(rs.getInt("Item_Key"));
			  lst.add(barcodeObj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---getBarcodeItemForValidation-->" , ex);
		}
		return lst;
	}
	
	public List<QbListsModel> getFullNameaItemForValidation()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		ItemQueries query=new ItemQueries();
		ResultSet rs = null;
		String name="";
		try 
		{
			rs=db.executeNonQuery(query.getFullNameaItemForValidation());
			while(rs.next())
			{
				QbListsModel nameObj=new QbListsModel();
				nameObj.setName(rs.getString("name"));
				nameObj.setItem_Key(rs.getInt("Item_Key"));
			  lst.add(nameObj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ItemsData---getFullNameaItemForValidation-->" , ex);
		}
		return lst;
	}
	
	public String GenerateBarcode(){
		
		
		return "";
	}
}
