package hba;

import java.text.SimpleDateFormat;

public class ItemReceiptQuerries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	
	public String getVendorList()
	{
		query = new StringBuffer();
		query.append(" Select Name,RecNo,ListType,SubLevel,ListID from QbLists Where IsActive =   'Y' And ListType =   'Vendor' order by ListType,FullName");
		return query.toString();
	}
	
	public String getApAccount()
	{
		query = new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name], ");
		query.append("AccountType,SubLevel,Rec_No,ListID FROM Accounts Inner join  AccountType on AccountType.TypeName = Accounts.AccountType where"); 
		query.append("AccountType in ('AccountsPayable') and IsActive='Y' order by AccountType.SRL_No,");
		query.append("Accounts.ACTLEVELSwithNO");
		return query.toString();
	}
	
	
	public String getAccountForExpanseTab()
	{
		query = new StringBuffer();
		query.append("SELECT AccountName As [Name],");
		query.append("AccountType    ,   SubLevel    ,   Rec_No , ListID FROM Accounts Inner join "); 
		query.append("AccountType on AccountType.TypeName = Accounts.AccountType Where IsActive='Y' and AccountType NOT IN ('AccountsPayable','AccountsRecievable') order by AccountType.SRL_No, ");
		query.append("Accounts.ACTLEVELSwithNO ");
		return query.toString();
		
	}
	
	
	public String getClassForExpanseTab()
	{
		query = new StringBuffer();
		query.append("Select [Name],Class_Key,ListID,SubLevel,FullName from [Class] Where IsActive='Y' order by FullName");
		return query.toString();
		
	}
	
	
	public String getCustomerForExpanseTab()
	{
		query = new StringBuffer();
		query.append("Select Name,RecNo,ListID,ListType,SubLevel,FullName from QbLists Where ListType in('Customer') and IsActive='Y' order by ListType,FullName");
		return query.toString();
		
	}
	
	public String getItemForItemTab()
	{
		query = new StringBuffer();
		query.append("SELECT name,item_key,ItemType,SubLevel,ListID FROM QBItems where (PricePercent=0 or PricePercent is Null) and IsActive='Y'  order by ItemType desc,FullName");
		return query.toString();
		
	}
	
	public String getInventroySiteForItemTab()
	{
		query = new StringBuffer();
		query.append("SELECT sitename,itemkey,'Inventory Site' as ItemType,0 as sublevel,ListID FROM InventorySiteList where IsActive='Y' and SiteName <> 'Unspecified Site' order by siteName");
		return query.toString();
		
	}
	
	public String getCuutomerItemTab()
	{
		query = new StringBuffer();
		query.append("Select Name,RecNo,ListType,SubLevel,ListID from QbLists Where ListType in('Customer') and IsActive='Y' order by ListType,FullName");
		return query.toString();
		
	}
	
	public String getClassForClassTab()
	{
		query = new StringBuffer();
		query.append("Select Name,Class_Key,SubLevel,FullName,ListID from [Class] Where IsActive='Y' order by FullName");
		return query.toString();
		
	}
	
	
	
	
	

}
