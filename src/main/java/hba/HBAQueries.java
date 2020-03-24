package hba;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import model.ActivityStatusModel;
import model.BankTransferModel;
import model.BarcodeSettingsModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.CashModel;
import model.CheckFAItemsModel;
import model.CheckItemsModel;
import model.CustomerModel;
import model.CustomerStatusHistoryModel;
import model.DeliveryLineModel;
import model.DeliveryModel;
import model.DepreciationModel;
import model.ExpensesModel;
import model.ItemReceiptModel;
import model.JournalVoucherGridData;
import model.JournalVoucherModel;
import model.OtherNamesModel;
import model.PurchaseRequestGridData;
import model.PurchaseRequestModel;
import model.SerialFields;
import model.VendorModel;

public class HBAQueries {
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar now = Calendar.getInstance();

	WebusersModel dbUser = null;

	public HBAQueries() {
		Session sess = Sessions.getCurrent();
		dbUser = (WebusersModel) sess.getAttribute("Authentication");
	}

	// Add/Update barcode settings table
	public String UpdateBarcodeSettingsQuery(BarcodeSettingsModel obj) {
		query = new StringBuffer();

		// Insert if record does not exist. Update if record exists
		query.append("update BARCODESETTINGS set BARCODE_TYPE='"
				+ obj.getBarcodeType()
				+ "',BARCODE_COUNTER='"
				+ obj.getBarcodeCounter()
				+ "',BARCODE_FLAG='"
				+ obj.getBarcodeConvert()
				// + "',BARCODE_DefaultPrinter='"
				// + obj.getBarcodeDefaultPrinter()
				+ "',BARCODE_AfterScanGoTo='"
				+ obj.getBarcodeAfterScanGoTo()
				+ "' where rec_no=1;"
				+ " insert into BARCODESETTINGS"
				+ " (REC_NO,BARCODE_TYPE, BARCODE_COUNTER,BARCODE_FLAG,BARCODE_AfterScanGoTo)"
				+ " select 1,'"
				+ obj.getBarcodeType()
				+ "','"
				+ obj.getBarcodeCounter()
				+ "',"
				+ "'"
				+ obj.getBarcodeConvert()
				// + "','"
				// + obj.getBarcodeDefaultPrinter()
				+ "'"
				+ ",'"
				+ obj.getBarcodeAfterScanGoTo()
				+ "'"
				+ " where not exists (select 1 from BARCODESETTINGS where REC_NO=1)");

		return query.toString();
	}

	// Barcode Exists
	public String GetBarcode(String Barcode) {
		query = new StringBuffer();
		query.append(" select BARCODE from QBItems where BARCODE = '" + Barcode
				+ "'");
		return query.toString();
	}

	// Barcode Settings
	public String GetBarcodeSettingsQuery() {
		query = new StringBuffer();
		query.append(" select top 1 BARCODE_TYPE,BARCODE_COUNTER,BARCODE_FLAG ,BARCODE_AfterScanGoTo from BARCODESETTINGS ");
		return query.toString();
	}

	public String getDBCompany(int companyId) {
		String sql = "Select * from companiesdb where dbtype='HBA' and companyid="
				+ companyId;
		return sql;
	}

	public String getCustomersQuery(String status) {
		query = new StringBuffer();
		// query.append("Select * from Customer");
		query.append("Select * from Customer ");
		if(status.equals("P"))
		query.append(" Where PriorityID=1 and IsActive='Y'");
		else if (status.equals("Y"))
			query.append(" Where  IsActive='" + status + "' ");
		else if (status.equals("N"))
			query.append(" Where  IsActive='" + status + "' ");		
		query.append(" Order by Replace(FullName,':',':'),PriorityID Desc");
		return query.toString();
	}
	
	public String getCustomersContractExpiryQuery(String status) 
	{
		query = new StringBuffer();	
		query.append("Select * from Customer where CusContractExpiry is not null and Email <>'' and IsActive= 'Y' ");
		if (status.equals("Customer Contract Expiry Within 2 Months"))
			query.append(" and CusContractExpiry>getdate() and CusContractExpiry < DATEADD(day,60,GETDATE()) ");
		
		if (status.equals("Customer Contract Expired Before 2 Years"))
			query.append(" and CusContractExpiry<getdate() and CusContractExpiry >= DATEADD(month,-25,GETDATE()) ");
		
		if (status.equals("Customer Contract Expired More than 2 Years"))
			query.append(" and CusContractExpiry<getdate() and CusContractExpiry < DATEADD(month,-25,GETDATE()) ");
		
		
		query.append(" Order by Replace(FullName,':',':'),PriorityID Desc");
		
		if (status.equals("Customer Balance"))
		{
			query = new StringBuffer();	
			query.append("Select * from Customer where LocalBalance !=0 and IsActive= 'Y' and Email <>'' ");
			query.append(" Order by name");
		}
		return query.toString();
	}
	
	public String getSalesRepNamesForReminder(String status)
	{
		query=new StringBuffer();	
		
		query.append("  select empcontact.details as employeeEmail,vendor.email as vendorEmail,customer.email as customeremail ,qblists.fullname,qblists.recNo,qblists.hremp_key from salesRep ");
		query.append("  left join qblists on qblists.recNo=salesRep.qblistKey ");
		query.append("  LEFT JOIN empcontact on empcontact.emp_key=qblists.hremp_key "); 
		query.append("  LEFT JOIN vendor  on vendor.vend_key=qblists.recNo ");
		query.append("  LEFT JOIN customer  on customer.cust_key=qblists.recNo where empcontact.contact_id=622 and salesRep.isactive='"+status+"' ");
		
		return query.toString();
	}
	public String getCustomerForQuotationReminder(String custKeys)
	{
		query=new StringBuffer();	
		query.append("select customer.* from quotation ");
		query.append("left join customer on customer.cust_key=quotation.customerRefKey ");
		query.append("left join prospective on prospective.recNo=quotation.customerRefKey ");
		query.append(" where customer.Email <>'' ");
		//i want to get all customer has quotation with status=c
		if(!custKeys.equals(""))
			query.append(" and quotation.customerRefKey in (select cust_key from customer where salesrepKey in ("+custKeys+")) "); 
		
		query.append(" and quotation.status in ('C') order by quotation.txnDate desc ");
		return query.toString();
	}
	

	public String getCustomerContact() {
		query = new StringBuffer();
		query.append("select * from customercotact order by recNo ");
		return query.toString();
	}

	public String UpdateCustomersQuery(int Cust_Key, String companyName,
			String name, String arName) {
		query = new StringBuffer();
		query.append("update Customer set name='" + name + "',companyName='"
				+ companyName + "',arName='" + arName + "'  Where  Cust_Key="
				+ Cust_Key);
		return query.toString();
	}

	public String getVendorsQuery() {
		query = new StringBuffer();
		query.append("Select Name,ArName,CompanyName,TimeCreated,BillAddress1,Phone,Fax,Email,WebSite,CC,Contact,PrintChequeAs,Note,Vend_Key,Balance,IsActive ");
		query.append(" from Vendor Order by vend_key");
		return query.toString();
	}

	// Edit Vendor
	public String getVendorByKeyQuery(int vendKey) {
		query = new StringBuffer();
		query.append("Select * from Vendor  Where  Vend_Key = " + vendKey);
		return query.toString();
	}

	public String UpdateVendorQuery(VendorModel obj) {
		query = new StringBuffer();
		query.append("update Vendor set name='" + obj.getName()
				+ "',companyName='" + obj.getCompanyName() + "',arName='"
				+ obj.getArName() + "',phone='" + obj.getPhone()
				+ "',WebSite='" + obj.getWebSite() + "',fax='" + obj.getFax()
				+ "',email='" + obj.getEmail() + "',BillAddress1='"
				+ obj.getBillAddress1() + "'" + ",contact='" + obj.getContact()
				+ "' ,IsActive='" + obj.getIsActive() + "' ,PrintChequeAs='"
				+ obj.getPrintChequeAs() + "'  Where  vend_key="
				+ obj.getVend_Key());
		return query.toString();
	}

	public String addVendorQuery(VendorModel obj) {
		query = new StringBuffer();
		query.append("INSERT INTO Vendor(vend_key,TimeCreated,Name,ArName,IsActive,CompanyName,phone,WebSite,fax,email,contact,PrintChequeAs,BillAddress1)");
		query.append(" VALUES( " + obj.getVend_Key() + ",getdate(), '"
				+ obj.getName() + "' , '" + obj.getArName() + "' , ");
		query.append("'" + obj.getIsActive() + "'  , '" + obj.getCompanyName()
				+ "' , '" + obj.getPhone() + "' , '" + obj.getWebSite()
				+ "' , '" + obj.getFax() + "' ,");
		query.append(" '" + obj.getEmail() + "' , '" + obj.getContact()
				+ "' , '" + obj.getPrintChequeAs() + "' , '"
				+ obj.getBillAddress1() + "'");
		query.append(" )");
		return query.toString();
	}

	// Edit Customer
	public String getCustomersByKeyQuery(int custKey) {
		query = new StringBuffer();
		query.append("Select * from Customer  Where  Cust_key = " + custKey);
		return query.toString();
	}

	public String getCustomerStatusById(int custKey) {
		query = new StringBuffer();
		query.append("Select top 20 * from CustomerStatusHistory  Where  custKey = "
				+ custKey + " order by CustomerStatusHistory.actionDate desc ");
		return query.toString();
	}

	public String UpdateCustomerQuery(CustomerModel obj) {
		query = new StringBuffer();
		query.append("update Customer set name='" + obj.getName()
				+ "',companyName='" + obj.getCompanyName() + "',arName='"
				+ obj.getArName() + "',phone='" + obj.getPhone()
				+ "',altphone='" + obj.getAltphone() + "',fax='" + obj.getFax()
				+ "',email='" + obj.getEmail() + "'" + ",contact='"
				+ obj.getContact() + "' ,IsActive='" + obj.getIsactive()
				+ "'  Where  Cust_Key=" + obj.getCustkey());
		return query.toString();
	}

	public String getMaxIDQuery(String tableName, String fieldName) {
		query = new StringBuffer();
		query.append(" select max(" + fieldName + ") from " + tableName);
		return query.toString();
	}

	public String addCustomerQuery(CustomerModel obj) {
		query = new StringBuffer();
		query.append("INSERT INTO Customer(Cust_Key,TimeCreated,TimeModified,Name,ArName,FullName,Parent,IsActive,Sublevel,CompanyName,phone,altphone,fax,email,contact)");
		query.append(" VALUES( " + obj.getCustkey() + ",getdate(),getdate(), '"
				+ obj.getName() + "' , '" + obj.getArName() + "' , '"
				+ obj.getName() + "' , ");
		query.append(" '' , '" + obj.getIsactive() + "' , 0 , '"
				+ obj.getCompanyName() + "' , '" + obj.getPhone() + "' , '"
				+ obj.getAltphone() + "' , '" + obj.getFax() + "' ,");
		query.append(" '" + obj.getEmail() + "' , '" + obj.getContact() + "' ");
		query.append(" )");
		return query.toString();
	}

	// Cheque Payments
	public String GetDefaultSetupInfoQuery() {
		query = new StringBuffer();
		query.append(" Select RVSERIALNOS,PVSERIALNOS,Post2MainAccount,PostOnMainClass,ChangeClass_Account,postItem2Main,PostJVWithOutName,usepurchaseflow,ChangePrice_ConvertPO,");
		query.append(" BuyItemWithHighCost,UseMinPurchasePrice,MinPurchasePriceRatio,MaxPurchasePriceRatio,UseMaxPurchasePrice,address,companyName,AllowToAddInvSite,countrykey,citykey,telephone,ccemail,fax,usebillable,AllowToSkipPurchaseWorkFlow");
		if (dbUser.getMergedDatabse().equalsIgnoreCase("Yes")) {
			query.append(" From companySettings");
		} else {
			query.append(" From COMPSETUP");
		}

		return query.toString();
	}

	public String fillBankAccountsQuery(String accountType) {
		query = new StringBuffer();
		query.append("SELECT AccountType.SRL_No , AccountName ,AccountType    ,   SubLevel    ,   Rec_No , ListID ,ACTLEVELSwithNO,FullName ");
		query.append(" FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType");
		query.append(" where IsActive='Y' ");
		if (!accountType.equals("")) {
			query.append(" and AccountType in (" + accountType + ") ");
		}
		query.append(" order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO,Rec_No");
		return query.toString();
	}

	public String fillAccountsQueryNotIn(String accountType) {
		query = new StringBuffer();
		query.append("SELECT AccountType.SRL_No , AccountName ,AccountType    ,   SubLevel    ,   Rec_No , ListID ,ACTLEVELSwithNO,FullName ");
		query.append(" FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType");
		query.append(" where IsActive='Y' ");
		if (!accountType.equals("")) {
			query.append(" and AccountType not in (" + accountType + ") ");
		}
		query.append(" order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();
	}

	public String checkIfBankAccountsHasSubQuery(String accountName) {
		query = new StringBuffer();
		query.append("Select FullName from Accounts Where ");
		if (!accountName.equals("")) {
			query.append(" CharIndex('" + accountName + "' ,FullName)>0 ");
		}
		return query.toString();
	}

	public String checkIfClassHasSubQuery(String className) {
		query = new StringBuffer();
		query.append("Select FullName from CLASS Where ");
		if (!className.equals("")) {
			query.append(" CharIndex('" + className + "' ,FullName)>0 ");
		}
		return query.toString();
	}

	public String getBanksListQuery() {
		query = new StringBuffer();
		query.append("Select name,RecNo from Banks order by name ");
		return query.toString();
	}

	public String getQbListQuery(String ListType) {
		query = new StringBuffer();
		query.append("Select Name,RecNo,ListID,ListType,SubLevel,FullName,isactive from QbLists Where IsActive='Y' ");
		if (!ListType.equals("")) {
			query.append(" and ListType in (" + ListType + ") ");
		}
		query.append(" order by ListType,FullName");
		return query.toString();
	}

	public String getClassQuery(String classType) {
		query = new StringBuffer();
		query.append("SELECT NAME,CLASS_KEY,sublevel,fullname FROM CLASS where (Class.IsActive ='Y') ");
		if (!classType.equals("")) {
			query.append(" and CLASS_TYPE='" + classType + "' ");
		}
		query.append(" ORDER BY FullName");
		return query.toString();
	}

	public String getFlatQuery(String classType, String name) {
		query = new StringBuffer();
		query.append("SELECT CLASS.NAME,CLASS.CLASS_KEY FROM FLAT RIGHT JOIN CLASS ON FLAT.RECNO=CLASS.CLASS_KEY ");
		query.append(" where (Class.IsActive ='Y')");
		if (!classType.equals("")) {
			query.append(" and CLASS_TYPE='" + classType + "' ");
		}
		if (!name.equals("")) {
			query.append(" and Right(Class.FullName,Len('" + name
					+ ":' + Class.Name)) = '" + name + ":' + Class.Name");
		}
		query.append(" ORDER BY [NAME]");
		return query.toString();
	}

	public String getFixedAssetItemQuery() {
		query = new StringBuffer();
		query.append(" Select AssetID,ASsetCode,AssetName, CAse WHEN isnull(AssetCode,'') = '' THen AssetName Else AssetCode + '·' + AssetName END as ASSETNAMECode ");
		query.append(" FROM ASSETMASTER Where STatusID NOT IN (1,2) ");
		query.append(" order by ASsetCode,AssetName ");
		query.append(" ");
		return query.toString();
	}

	public String GetSerialNumberQuery(String serialField) {
		query = new StringBuffer();
		query.append("Select SerialField,LastNumber from SystemSerialNos");
		query.append(" where SerialField ='" + serialField + "'");
		return query.toString();
	}

	public String GetSerialNumberQueryForChequeNo(String serialField) {
		query = new StringBuffer();
		query.append(" Select LastNumber from SystemSerialNos");
		query.append("  where SerialField ='" + serialField + "'");
		return query.toString();
	}

	public String getPayToOrderInfoQuery(String ListType, int keyID) {
		query = new StringBuffer();
		if (ListType.equals("Customer")) {
			query.append(" SELECT * from Customer ");
			query.append(" where Cust_Key =" + keyID);
		} else if (ListType.equals("Class")) {
			query.append(" SELECT * from Class ");
			query.append(" where Class_Key =" + keyID);
		} else if (ListType.equals("Employee")) {
			query.append(" SELECT * from Employee ");
			query.append(" where Emp_Key =" + keyID);
		} else if (ListType.equals("Vendor")) {
			query.append(" SELECT * from Vendor ");
			query.append(" where Vend_Key =" + keyID);
		} else if (ListType.equals("OtherNames")) {
			query.append(" SELECT * from OtherNames ");
			query.append(" where OthNam_Key =" + keyID);
		} else if (ListType.equals("Terms")) {
			query.append(" SELECT * from Terms ");
			query.append(" where Term_Key =" + keyID);
		} else if (ListType.equals("Prospective")) {
			query.append(" SELECT * from Prospective ");
			query.append(" where recNo =" + keyID);
		}

		return query.toString();
	}

	// Save New Cheque Payment
	public String GetNewCheckMastRecNoQuery() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from CheckMast");
		return query.toString();
	}

	public String GetNewUserActivityRecNo() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from useraction");
		return query.toString();
	}

	public String GetNewTxnNumberQuery(SerialFields field, String txnNumber, String Condition,long recNo) {
		String CheckPV = "'Cheque','Cheque-Preterminate','Cheque-Refund','Bill','Cheque-Opening'";
		String CashPV = "'Cash','Cash-Preterminate','Cash-Refund','Bill-Cash'";

		query = new StringBuffer();
		switch (field) {
		case ChequePV:
			query.append("Select RecNo from CheckMast where");
			query.append(" PVNo='" + txnNumber + "' and ");
			query.append(" Cheque in (" + CheckPV + ") and ");
			query.append(" Not Status in ('V') and recno !=" + recNo);
			break;

		case ChequeNo:
			query.append("Select RecNo from CheckMast Where ");
			query.append(" CheckNo='" + txnNumber + "' and");
			query.append(" Cheque in (" + CheckPV + ") and ");
			query.append(" Not Status in ('V') And");
			query.append(" Not CheckNo is Null");
			query.append(" and " + Condition+"and recno !=" + recNo);

		case CashPV:
			query.append("Select RecNo from CheckMast where");
			query.append(" PVNo='" + txnNumber + "' and ");
			query.append(" Cheque in (" + CashPV + ") and ");
			query.append(" Not Status in ('V') and recno !=" + recNo);
			break;

		default:
			break;
		}

		return query.toString();
	}

	public String insertSystemSerialNosQuery(String tmpConfigSerailNum,
			String SerialField) {
		query = new StringBuffer();
		query.append(" Insert into SystemSerialNos(SerialField,LastNumber)");
		query.append(" Values('" + SerialField + "','" + tmpConfigSerailNum
				+ "')");
		return query.toString();
	}

	public String updateSystemSerialNosQuery(String tmpConfigSerailNum,String SerialField) {
		query = new StringBuffer();
		query.append(" Update SystemSerialNos set LastNumber='"	+ tmpConfigSerailNum + "'");
		query.append(" where SerialField='" + SerialField + "'");
		return query.toString();
	}


	public String addNewChequePaymentQuery(CashModel obj) {
		query = new StringBuffer();
		query.append("Insert into Checkmast (RecNo,CheckNo,CheckDate,PvNo,PvDate,BankKey,PayeeKey,PayeeType,Amount,Status,PVCheck_Printed,[Memo],");
		query.append(" BankRefKey,Cheque,QBRefNo,QBRefDate,QBStatus,ExpClassHide,ExpMemoHide,ExpBillNoHide,ExpBillDateHide,ItemClassHide,ItemDesHide,");
		query.append(" ItemBillNoHide,ItemBillDateHide,AcctForPdc,PrintName,UnitKey,UserID,webUserID)");
		query.append(" Values(" 
				+ obj.getRecNo() 
				+ ",'" 
				+ obj.getCheckNo()
				+ "','" 
				+ sdf.format(obj.getCheckDate()) 
				+ "','"
				+ obj.getPvNo() 
				+ "','" 
				+ sdf.format(obj.getPvDate()) 
				+ "',"
				+ obj.getBankKey() 
				+ "," 
				+ obj.getPayeeKey() 
				+ ",'"
				+ obj.getPayeeType() 
				+ "'," 
				+ obj.getAmount() 
				+ ",'" 
				+ obj.getStatus() 
				+ "','"
				+ obj.getPVCheck_Printed() 
				+ "','" 
				+ obj.getMemo() 
				+ "',"
				+ obj.getBankRefKey() 
				+ ",'" 
				+ obj.getCheque() 
				+ "','" 
				+ obj.getQBRefNo()
				+ "','" 
				+ obj.getQBRefDate() 
				+ "','" 
				+ obj.getQBStatus()
				+ "','" 
				+ obj.getExpClassHide() 
				+ "' ,'" 
				+ obj.getExpMemoHide() 
				+ "','"
				+ obj.getExpBillNoHide() 
				+ "','" 
				+ obj.getExpBillDateHide()
				+ "','" 
				+ obj.getItemClassHide() 
				+ "','"
				+ obj.getItemDesHide() 
				+ "','" 
				+ obj.getItemBillNoHide()
				+ "','" 
				+ obj.getItemBillDateHide() 
				+ "','" 
				+ obj.getAcctForPdc() 
				+ "','" 
				+ obj.getPrintName()
				+ "'," 
				+ obj.getUnitKey() 
				+ "," 
				+ obj.getUserID()
				+ "," 
				+ obj.getWebUserID()
				+")");
		return query.toString();
	}

	public String addNewCashPaymentQuery(CashModel obj) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Insert into Checkmast (RecNo,PvNo,PvDate,BankKey,PayeeKey,PayeeType,Amount,Status,[Memo],");
		query.append(" Cheque,QBRefNo,QBRefDate,QBStatus,ExpClassHide,ExpMemoHide,ExpBillNoHide,ExpBillDateHide,ItemClassHide,ItemDesHide,");
		query.append(" ItemBillNoHide,ItemBillDateHide,PrintName,UnitKey,UserID)");
		query.append(" Values(" + obj.getRecNo() + ", '" + obj.getPvNo()
				+ "' , ");
		query.append(" '" + sdf.format(obj.getPvDate()) + "' , "
				+ obj.getBankKey() + " , " + obj.getPayeeKey() + " , '"
				+ obj.getPayeeType() + "' , " + obj.getAmount() + " , ");
		query.append(" '" + obj.getStatus() + "' , '" + obj.getMemo() + "' , ");
		query.append(" '" + obj.getCheque() + "' , '" + obj.getQBRefNo()
				+ "' , '" + obj.getQBRefDate() + "' , '" + obj.getQBStatus()
				+ "' , '" + obj.getExpClassHide() + "' ,");
		query.append(" '" + obj.getExpMemoHide() + "' , '"
				+ obj.getExpBillNoHide() + "' , '" + obj.getExpBillDateHide()
				+ "' , ");
		query.append(" '" + obj.getItemClassHide() + "' , '"
				+ obj.getItemDesHide() + "' , '" + obj.getItemBillNoHide()
				+ "' , '" + obj.getItemBillDateHide() + "' ,");
		query.append(" '" + obj.getPrintName() + "' , " + obj.getUnitKey()
				+ " , " + obj.getUserID());
		query.append(" )");
		query.append(" ");
		return query.toString();
	}

	public String updateNewCashPayment(CashModel obj) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Update Checkmast set PvNo='" + obj.getPvNo()
				+ "',BankKey=" + obj.getBankKey() + ",PayeeKey="
				+ obj.getPayeeKey() + ",PayeeType='" + obj.getPayeeType()
				+ "',Amount=" + obj.getAmount() + ",PvDate='"
				+ sdf.format(obj.getPvDate()) + "',Status='" + obj.getStatus()
				+ "',Memo='" + obj.getMemo() + "',");
		query.append("Cheque='" + obj.getCheque() + "',QBRefNo='"
				+ obj.getQBRefNo() + "',QBRefDate='" + obj.getQBRefDate()
				+ "',QBStatus='" + obj.getQBStatus() + "',ExpClassHide='"
				+ obj.getExpClassHide() + "',ExpMemoHide='"
				+ obj.getExpMemoHide() + "',ExpBillNoHide='"
				+ obj.getExpBillNoHide() + "',ExpBillDateHide='"
				+ obj.getExpBillDateHide() + "',");
		query.append("ItemClassHide='" + obj.getItemClassHide()
				+ "',ItemDesHide='" + obj.getItemDesHide()
				+ "',ItemBillNoHide='" + obj.getItemBillNoHide()
				+ "',ItemBillDateHide='" + obj.getItemBillDateHide()
				+ "',PrintName='" + obj.getPrintName() + "',UnitKey='"
				+ obj.getUnitKey() + "',userID=" + obj.getUserID()
				+ " where recNo=" + obj.getRecNo());
		return query.toString();

	}

	public String updateNewChequePayment(CashModel obj) {
		query = new StringBuffer();
		query.append("Update Checkmast set "
				+ "PvNo='" + obj.getPvNo()
				+ "',BankKey=" + obj.getBankKey()
				+ ",CheckNo=" + obj.getCheckNo()
				+ ",UnitKey=" + obj.getUnitKey()
				+ ",PayeeKey=" + obj.getPayeeKey()
				+ ",BankRefKey=" + obj.getBankRefKey() 
				+ ",PayeeType='" + obj.getPayeeType()
				+ "',Amount=" + obj.getAmount()
				+ ",CheckDate='" + sdf.format(obj.getCheckDate()) 
				+ "',PvDate='" + sdf.format(obj.getPvDate()) 
				+ "',Status='" + obj.getStatus()
				+ "',Memo='" + obj.getMemo()
				+ "',PVCheck_Printed='" + obj.getPVCheck_Printed()
				+ "',AcctForPdc='" + obj.getAcctForPdc()
				+ "',Cheque='" + obj.getCheque() 
				+ "',QBRefNo='"	+ obj.getQBRefNo() 
				+ "',QBRefDate='" + obj.getQBRefDate()
				+ "',QBStatus='" + obj.getQBStatus() 
				+ "',ExpClassHide='"+ obj.getExpClassHide() 
				+ "',ExpMemoHide='"	+ obj.getExpMemoHide() 
				+ "',ExpBillNoHide='"+ obj.getExpBillNoHide() 
				+ "',ExpBillDateHide='"	+ obj.getExpBillDateHide() 
				+ "',ItemClassHide='" + obj.getItemClassHide()
				+ "',ItemDesHide='" + obj.getItemDesHide()
				+ "',ItemBillNoHide='" + obj.getItemBillNoHide()
				+ "',ItemBillDateHide='" + obj.getItemBillDateHide()
				+ "',webUserID='" + obj.getWebUserID()
				+ "',PrintName='" + obj.getPrintName()
				+ "',userID=" + obj.getUserID()
				+ " where recNo=" + obj.getRecNo());
		return query.toString();

	}

	public String addNewBankTransferQuery(BankTransferModel obj) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Insert into Checkmast (RecNo,TxnID,PvNo,PvDate,BankKey,PayeeKey,PayeeType,Amount,Status,[Memo],BankRefKey,");
		query.append(" Cheque,QBRefNo,QBRefDate,QBStatus,ExpClassHide,ExpMemoHide,ExpBillNoHide,ExpBillDateHide,ItemClassHide,ItemDesHide,");
		query.append(" ItemBillNoHide,ItemBillDateHide,PrintName,UnitKey,UserID,SwiftCode)");
		query.append(" Values(" + obj.getRecNo() + ",'" + obj.getTxnID()
				+ "' , '" + obj.getPvNo() + "' , ");
		query.append(" '" + sdf.format(obj.getPvDate()) + "' , "
				+ obj.getBankKey() + " , " + obj.getPayeeKey() + " , '"
				+ obj.getPayeeType() + "' , " + obj.getAmount() + " , ");
		query.append(" '" + obj.getStatus() + "' , '" + obj.getMemo() + "' , "
				+ obj.getBankRefKey() + " , ");
		query.append(" '" + obj.getCheque() + "' , '" + obj.getQBRefNo()
				+ "' , '" + obj.getQBRefDate() + "' , '" + obj.getQBStatus()
				+ "' , '" + obj.getExpClassHide() + "' ,");
		query.append(" '" + obj.getExpMemoHide() + "' , '"
				+ obj.getExpBillNoHide() + "' , '" + obj.getExpBillDateHide()
				+ "' , ");
		query.append(" '" + obj.getItemClassHide() + "' , '"
				+ obj.getItemDesHide() + "' , '" + obj.getItemBillNoHide()
				+ "' , '" + obj.getItemBillDateHide() + "' ,");
		query.append(" '" + obj.getPrintName() + "' , " + obj.getUnitKey()
				+ " , " + obj.getUserID() + " , '" + obj.getSwiftCode() + "'");
		query.append(" )");
		query.append(" ");
		return query.toString();
	}

	

	// Expenses
	public String deleteExpenseQuery(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from CheckExpense Where RecNo=" + RecNo);
		return query.toString();
	}

	public String addNewExpenseQuery(ExpensesModel objExpenses, int RecNo) {
		query = new StringBuffer();
		String memo = "";
		if (objExpenses.getMemo() != null) {
			memo = objExpenses.getMemo().replace("'", "`");
		}
		query.append(" Insert into CheckExpense (RecNo,AccountKey,Amount,[Memo],CustKey,ClassKey,[LineNo],BillNO,BillDate,FaItemKey,AccountType,billable) ");
		query.append(" values(" + RecNo + ","+ objExpenses.getSelectedAccount().getRec_No() + ", " + objExpenses.getAmount() + ", '" + memo + "',");
		if (objExpenses.getSelectedCustomer() != null)
			query.append(objExpenses.getSelectedCustomer().getRecNo());
		else
			query.append("0");
		if (objExpenses.getSelectedClass() != null)
			query.append(", " + objExpenses.getSelectedClass().getClass_Key());
		else
			query.append(", 0");
		query.append(", " + objExpenses.getSrNO());
		String billNo = objExpenses.getBillNo() == null ? "" : objExpenses
				.getBillNo();
		query.append(", '" + billNo + "'");
		if (objExpenses.getBillDate() != null)
			query.append(", '" + sdf.format(objExpenses.getBillDate()) + "'");
		else
			query.append(", NULL");
		if (objExpenses.getSelectedFixedAsset() != null)
			query.append(", "
					+ objExpenses.getSelectedFixedAsset().getAssetid());
		else
			query.append(", 0");
		if (objExpenses.getSelectedAccount() != null)
			query.append(", '"
					+ objExpenses.getSelectedAccount().getAccountType() + "'");
		else
			query.append(", '' ");
		if (objExpenses.isBillableChked())
			query.append(",'Y'");
		else
			query.append(", 'N' ");
		query.append(" )");
		return query.toString();
	}

	public String updateAccountsTotalBalanceQuery(int RecNo, double amount) {
		query = new StringBuffer();
		query.append(" Update Accounts Set TotalBalance=TotalBalance + "
				+ amount);
		query.append(" Where Rec_No=" + RecNo);
		return query.toString();
	}

	// Check Items
	public String GetQBItemsQuery() 
	{
		query = new StringBuffer();
		query.append(" SELECT name,item_key,ListID,ItemType,SubLevel,FullName,BARCODE,salesdesc, ");
		//add this to not go again to database when select item
		query.append(" DescriptionAR, QuantityOnHand,SalesPrice,averagecost,ClassKey");
		query.append(" FROM QBItems ");
		query.append(" where isnull(PricePercent,0)=0 and IsActive='Y' order by ItemType desc,FullName ");
		return query.toString();
	}

	public String GetQBItemsDataQuery(int Item_Key) {
		query = new StringBuffer();
		query.append(" SELECT * from QBItems Where Item_Key=" + Item_Key);
		return query.toString();
	}

	public String deleteCheckItemsQuery(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from CheckItems Where RecNo=" + RecNo);
		return query.toString();
	}

	public String addNewCheckItemsQuery(CheckItemsModel obj, int RecNo) {
		query = new StringBuffer();
		String desc = "";
		if (obj.getDescription() != null) {
			desc = obj.getDescription().replace("'", "`");
		}
		query.append(" Insert into CheckItems (RecNo, ItemKey,Description,Quantity,Cost,Amount,CustKey,ClassKey,[LineNo],FaItemKey,InvoiceDate,InventorySiteKey,BillNo,billable)");
		query.append(" values(" + RecNo + ", "
				+ obj.getSelectedItems().getRecNo() + ", '" + desc + "', "
				+ obj.getQuantity() + ", ");
		query.append(" " + obj.getCost() + ", " + obj.getAmount() + ", ");
		if (obj.getSelectedCustomer() != null)
			query.append(obj.getSelectedCustomer().getRecNo());
		else
			query.append("0");
		if (obj.getSelectedClass() != null)
			query.append(", " + obj.getSelectedClass().getClass_Key());
		else
			query.append(", 0");
		query.append(", " + obj.getLineNo());
		if (obj.getSelectedFixedAsset() != null)
			query.append(", " + obj.getSelectedFixedAsset().getAssetid());
		else
			query.append(", 0");

		if (obj.getInvoiceDate() != null)
			query.append(", '" + sdf.format(obj.getInvoiceDate()) + "'");
		else
			query.append(", NULL");
		query.append(" , 0");
		String billNo = obj.getBillNo() == null ? "" : obj.getBillNo();
		query.append(", '" + billNo + "'");
		if (obj.isBillableChked())
			query.append(",'Y'");
		else
			query.append(", 'N' ");




		query.append( ")");

		return query.toString();
	}

	// Fixed Asset Items
	public String getVendorFixedAssetItemQuery(int vendorID) {
		query = new StringBuffer();
		query.append(" Select AssetID,ASsetCode,AssetName, CAse WHEN isnull(AssetCode,'') = '' THen AssetName Else AssetCode + '·' + AssetName END as ASSETNAMECode ");
		query.append(" FROM ASSETMASTER Where STatusID NOT IN (1,2) and USED = 'NEW' AND BILLED = 0");
		query.append(" AND ASSETMASTER.VendorID = " + vendorID);
		query.append(" order by ASsetCode,AssetName ");
		return query.toString();
	}

	public String getFixedAssetItemDataQuery(int AssetID) {
		query = new StringBuffer();
		query.append(" Select AssetMaster.*,Customer.Name as CustName,Employee.Name as EMPName ");
		query.append(" from AssetMaster LEFT JOIN CUSTOMER ON ASSETMASTER.LOCATIONID = CUSTOMER.CUST_KEY  ");
		query.append(" LEFT JOIN EMPLOYEE ON ASSETMASTER.EMPLOYEEID = EMPLOYEE.EMP_KEY ");
		query.append(" Where AssetID=" + AssetID);
		return query.toString();
	}

	public String deleteCheckFAItemsQuery(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from CheckFAItems Where RecNo=" + RecNo);
		return query.toString();
	}

	public String addNewCheckFAItemsQuery(CheckFAItemsModel obj, int RecNo) {
		query = new StringBuffer();
		String desc = "";
		if (obj.getDescription() != null) {
			desc = obj.getDescription().replace("'", "`");
		}
		query.append(" Insert into CheckFAItems (RecNo,[LineNo], FaItemKey,Description,BillNo,CustKey,CustodyKey,InvoiceDate,Quantity,UnitPrice,OtherCharges,Amount)");
		query.append(" values(" + RecNo + ", " + obj.getLineNo() + ", "
				+ obj.getSelectedFixedAsset().getAssetid() + ", '" + desc
				+ "' ");
		String billNo = obj.getBillNo() == null ? "" : obj.getBillNo();
		query.append(", '" + billNo + "', ");

		if (obj.getSelectedCustomer() != null)
			query.append(obj.getSelectedCustomer().getRecNo());
		else
			query.append("0");
		if (obj.getSelectedCustody() != null)
			query.append(", " + obj.getSelectedCustody().getRecNo());
		else
			query.append(", 0");
		if (obj.getInvoiceDate() != null)
			query.append(", '" + sdf.format(obj.getInvoiceDate()) + "'");
		else
			query.append(", NULL");
		query.append(", " + obj.getQuantity() + ", " + obj.getUnitPrice()
				+ ", " + obj.getOtherCharges() + ", " + obj.getAmount() + ")");

		return query.toString();
	}

	public String updateAssetMasterQuery(CheckFAItemsModel obj) {
		query = new StringBuffer();
		query.append(" UPDATE ASSETMASTER SET");
		query.append(" UnitPrice=" + obj.getUnitPrice());
		query.append(", Quantity=" + obj.getQuantity());
		query.append(", OtherCharges=" + obj.getOtherCharges());
		query.append(", Price=" + obj.getAmount());
		if (obj.getSelectedCustomer() != null)
			query.append(", LocationID=" + obj.getSelectedCustomer().getRecNo());
		else
			query.append(", LocationID=0");
		if (obj.getSelectedCustody() != null)
			query.append(", EmployeeID=" + obj.getSelectedCustody().getRecNo());
		else
			query.append(", EmployeeID=0");
		query.append(", Billed = 'True' ");
		if (obj.getInvoiceDate() != null)
			query.append(", InvoiceDate='" + sdf.format(obj.getInvoiceDate())
					+ "'");
		String billNo = obj.getBillNo() == null ? "" : obj.getBillNo();
		query.append(", InvoiceNumber='" + billNo + "'");

		query.append(" Where ASSETID="
				+ obj.getSelectedFixedAsset().getAssetid());

		return query.toString();
	}

	public String DeleteDepreciationQuery(int AssetID) {
		query = new StringBuffer();
		query.append(" Delete From Depreciation Where AssetID=" + AssetID);
		return query.toString();
	}

	public String getSystemSettingQuery(String SettingName) {
		query = new StringBuffer();
		query.append(" Select SettingValue from SystemSettings Where SettingName='"
				+ SettingName + "'");
		return query.toString();
	}

	public String InsertDepreciationQuery(int assetID, int locationId,
			DepreciationModel obj) {
		query = new StringBuffer();
		query.append(" Insert Into Depreciation(AssetID,PeriodNo,periodstart,PeriodDate,NoDays,DepreciationAmount,AccumAmount,NetBookValue,Notes,LocationID) ");
		query.append(" values (" + assetID + ", " + obj.getSrNo() + ", '"
				+ sdf.format(obj.getPeriodStart()) + "', '"
				+ sdf.format(obj.getPeriodEnd()) + "', " + obj.getDays() + ", ");
		query.append(" " + obj.getDepreciationAmount() + ", "
				+ obj.getAccDeprec() + ", " + obj.getNetBookValue() + ", ");
		query.append("'" + obj.getNotes() + "', " + locationId);
		query.append(" )");
		return query.toString();
	}

	// Item List
	public String GetQBItemsListQuery(String isActive) {
		query = new StringBuffer();
		query.append(" Select QBItems.Sublevel,Item_Key,QBItems.Name,ItemType,IncomeAccountRef,Accounts.AccountName,Accounts.isactive,QBItems.fullname,SalesDesc,PurchaseDesc,PurchaseCost,SalesPrice ");
		query.append(" ,QuantityOnHand,AverageCost,QuantityOnOrder,QuantityOnSalesOrder,ReorderPoint ");
		query.append(" from QBItems inner join Accounts on QBItems.IncomeAccountRef=Accounts.Rec_No ");
		if (!isActive.equalsIgnoreCase(""))
			query.append(" Where QBItems.IsActive ='" + isActive
					+ "' and Accounts.isactive='" + isActive + "'");
		query.append(" order by ItemType Desc, QBItems.FullName");
		return query.toString();
	}

	// Chart of Accounts
	public String GetCharofAccountsListQuery(String isActive) {
		query = new StringBuffer();
		query.append(" Select Accounts.*,Class.Class_Key,Class.FullName As Class");
		query.append(" from Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType ");
		query.append(" Left  Join Class On Accounts.ClassKey = Class.Class_Key ");
		query.append(" Where Accounts.IsActive ='" + isActive + "'");
		query.append(" order by AccountType.SRL_No,Replace(Accounts.ACTLEVELSwithNO,':',':')");
		return query.toString();
	}

	public String UpdateAccountQuery(int Rec_No, String name, String description) {
		query = new StringBuffer();
		// query.append("update Accounts set name='" + name + "',Description='"
		// + description +"'  Where  Rec_No=" + Rec_No);
		query.append("update Accounts set Description='" + description
				+ "'  Where  Rec_No=" + Rec_No);
		return query.toString();
	}

	// Bank Transfer
	public String getBanksDetailQuery(int RecNo) {
		query = new StringBuffer();
		query.append("Select RecNo, Name, AccountRefKey, Attn_Name, Attn_Position, ActName, ActNumber, Branch, IBANNo from Banks ");
		query.append(" where RecNo=" + RecNo);
		return query.toString();
	}

	public String UpdateAccountDetailsQuery(BankTransferModel obj,
			String listType, int payKey) {
		query = new StringBuffer();
		if (listType.equals("Customer")) {
			query.append(" update Customer set ActName='" + obj.getToActName()
					+ "' ,BankName='" + obj.getToBankName() + "' , AccountNo='"
					+ obj.getToActNumber() + "' ,");
			query.append(" BranchName='" + obj.getToBranch()
					+ "' where Cust_Key=" + payKey);
		}
		if (listType.equals("Vendor")) {
			query.append(" update Vendor set ActName='" + obj.getToActName()
					+ "' ,BankName='" + obj.getToBankName() + "' , ActNumber='"
					+ obj.getToActNumber() + "' ,");
			query.append(" Branch='" + obj.getToBranch() + "' where Vend_Key="
					+ payKey);
		}
		if (listType.equals("OtherNames")) {
			query.append(" update OtherNames set ActName='"
					+ obj.getToActName() + "' ,BankName='"
					+ obj.getToBankName() + "' , AccountNo='"
					+ obj.getToActNumber() + "' ,");
			query.append(" BranchName='" + obj.getToBranch()
					+ "' where OthNam_Key=" + payKey);
		}

		return query.toString();
	}

	// Garage
	public String getJobGarageListQuery() {
		query = new StringBuffer();
		query.append(" SELECT JOBMAST.STATUS, JOBMAST.REC_NO, VEHICLEMAST.CHASIS_NO, Customer.FULLNAME,JOBMAST.TXN_DATE, JOBMAST.TXN_TIME, JOBMAST.SA, Employee.FULLNAME AS SAName,");
		query.append(" JOBMAST.WORK_START,JOBMAST.WORK_END, JOBMAST.ODOMETER, JOBMAST.NOTES, JOBMAST.REF_NO, VEHICLEMAST.ENGINE_NO, VEHICLEMAST.REG_NO,");
		query.append(" JOBMAST.ROUT_NO, HRLISTVALUES.DESCRIPTION as Brand  , HRLISTVALUES_1.DESCRIPTION As Series");
		query.append(" FROM ((((JOBMAST LEFT JOIN VEHICLEMAST ON JOBMAST.Vehicle_ID = VEHICLEMAST.VEHICLE_ID)");
		query.append(" LEFT JOIN Customer ON JOBMAST.CUST_KEY = Customer.CUST_KEY) LEFT JOIN Employee ON JOBMAST.SA = Employee.EMP_KEY)");
		query.append(" LEFT JOIN HRLISTVALUES ON VEHICLEMAST.BRAND = HRLISTVALUES.ID) LEFT JOIN HRLISTVALUES AS HRLISTVALUES_1 ON VEHICLEMAST.SERIES = HRLISTVALUES_1.ID");
		query.append(" ORDER BY Cast(REF_NO As Int)");

		return query.toString();
	}

	// For-- Cash Invoice By IqbaL
	// ---------------------------------------------------------------------------------------------------------

	public String getCustomerProfile() {
		query = new StringBuffer();
		query.append("select Name,RecNo,ListType,SubLevel,ListID,FullName from QbLists where ListType in('Customer') and IsActive='Y' order by ListType,FullName");
		return query.toString();
	}

	public String getCustomerClass() {
		query = new StringBuffer();

		//query.append("select Name,Class_Key,SubLevel,FullName,ListID from Class where ISActive='Y' order by FullName");
		query.append("select [name],class_key,sublevel,fullname,ListId, ");
		query.append(" (select count(*) from Class as c Where ISActive='Y' and CharIndex( Class.Name +':' ,FullName)>0 ) as 'subItemsCount'");
		query.append(" from [class] where isactive='y' order by fullname");
		return query.toString();
	}

	public String getCustomerDepositeTo() {
		query = new StringBuffer();
		query.append("select Accounts.AccountName As[Name],AccountType,SubLevel,Rec_No,ListID from Accounts inner join accountType on accountType.Typename = Accounts.AccountType where accounttype in('Cash','Bank','OtherCurrentasset') and isactive='Y' order by AccountType.SRL_NO,Accounts.ACTLEVELSwithNo");
		return query.toString();
	}

	public String getDefaultSetUpInfoForCashInvoice() {
		query = new StringBuffer();
		query.append("Select PostOnMainClass,PostItem2Main,SellStockWithZero,postOnMainClass,AllowSavingAvgCost,UseSellItemWithLowerSP,UseMinSellingPrice,");
		query.append("MinSellingPriceRatio,UseMaxSellingPrice,MaxSellingPriceRatio,ShowAvgCost,SaveINVQty,UseSalesRepCommission,canExceedCreditLimit,address,companyName,AllowToAddInvSite,countrykey,citykey,telephone,ccemail,fax,PVSERIALNOS,"
				+ "UseSalesFlow,usePurchaseFlow,AllowToSkipSalesWorkFlow,AllowToSkipPurchaseWorkFlow");
		if (dbUser.getMergedDatabse().equalsIgnoreCase("Yes")) {
			query.append(" From companySettings");
		} else {
			query.append(" From COMPSETUP");
		}
		return query.toString();
	}

	public String getCustomerRep() {
		query = new StringBuffer();
		query.append("select QBLists.FullName,SalesRep.salesRep_Key,SalesRep.Initial,QBLists.ListType,SalesRep.ListId,0 as sublevel from salesrep inner join qblists on salesrep.qblistkey=qblists.recNo");
		return query.toString();
	}

	public String getCustomerPaymentMethod() {
		query = new StringBuffer();
		query.append("select * from paymentmethod order by name");
		return query.toString();
	}

	public String getCustomerTemplates() {
		query = new StringBuffer();
		query.append("select * from Activitytemplate where templatetype ='salesreceipt' and active='true' order by recno");
		return query.toString();
	}

	public String getCustomerSendVia() {
		query = new StringBuffer();
		query.append("select description,id from hrlistvalues where field_id=68 and hrlistvalues.id<>149 and hrlistvalues.id<>172 order by description");
		return query.toString();
	}

	public String getGridVlaueforClass() {
		query = new StringBuffer();
		//change this to get if class has subitems
		//query.append("select [name],class_key,sublevel,fullname,ListId from [class] where isactive='y' order by fullname");
		query.append("select [name],class_key,sublevel,fullname,ListId, ");
		query.append(" (select count(*) from Class as c Where ISActive='Y' and CharIndex( Class.Name +':' ,FullName)>0 ) as 'subItemsCount'");
		query.append(" from [class] where isactive='y' order by fullname");
		return query.toString();
	}

	public String getGridInventorySite() {
		query = new StringBuffer();
		query.append("select sitename,itemKey,'inventory site' as itemtpe,0 as sublevel,listId from inventorysitelist where isactive='y' and sitename <> 'unspecified site' order by sitename");
		return query.toString();
	}

	public String getGridVlaueforItemType(int itemKey) {
		query = new StringBuffer();
		query.append("select itemType,listId from [QBItems] where item_key="
				+ itemKey + '"');
		return query.toString();
	}

	public String getGridVlaueforDescandArabicDesc(int itemKey) {
		query = new StringBuffer();
		query.append("select * from QBItems where item_key=" + itemKey);
		return query.toString();
	}

	public String addNewCashInvoiceQuery(CashInvoiceModel obj, int webUserID) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Insert into SalesReceipt(RecNo,CustomerRefKey,ClassRefKey,DepositAccountRefKey,TemplateRefKey,TxnDate,RefNumber,BillAddress1,");
		query.append("BillAddress2,BillAddress3,BillAddress4,BillAddress5,BillAddressCity,BillAddressState,BillAddressPostalCode,BillAddressCountry,BillAddressNote,ShipAddress1,");
		query.append("ShipAddress2,ShipAddress3,ShipAddress4,ShipAddress5,ShipAddressCity,ShipAddressState,ShipAddressPostalCode,ShipAddressCountry,ShipAddressNote,");
		query.append("IsPending,CheckNo,PaymentMethodRefKey,SalesRefKey,FOB,ShipDate,ShipMethodRefKey,ItemSalesTaxRefKey,Memo,CustomerMsgRefKey,IsToBePrinted,IsToEmailed,IsTaxIncluded,");
		query.append("CustomerSalesTaxCodeRefKey,Other,Amount,QuotationRecNo,SendViaReffKey,CustomField1,CustomField2,CustomField3,CustomField4,CustomField5,status,DescriptionHIDE,QtyHIDE,ClassHIDE,RateHIDE,transformD,webUserID)");
		query.append(" Values(" + obj.getRecNo() + ", "
				+ obj.getCustomerRefKey() + " , " + obj.getClassRefKey()
				+ " , " + obj.getDepositAccountRefKey() + " , "
				+ obj.getTemplateRefKey() + " , ");
		query.append(" '" + sdf.format(obj.getTxnDate()) + "' , '"
				+ obj.getRefNumber() + "' ,' " + obj.getBillAddress1()
				+ "' , '" + obj.getBillAddress2() + "' , '"
				+ obj.getBillAddress3() + "' , '" + obj.getBillAddress4()
				+ "' , '" + obj.getBillAddress5() + "' , ");
		query.append(" '" + obj.getBillAddressCity() + "' , '"
				+ obj.getBillAddressState() + "' , '"
				+ obj.getBillAddressPostalCode() + "' , '"
				+ obj.getBillAddressCountry() + "' , '"
				+ obj.getBillAddressNote() + "' , '" + obj.getShipAddress1()
				+ "' ,");
		query.append(" '" + obj.getShipAddress2() + "' , '"
				+ obj.getShipAddress3() + "' , '" + obj.getShipAddress4()
				+ "' , '" + obj.getShipAddress5() + "' , '"
				+ obj.getShipAddressCity() + "' , '"
				+ obj.getShipAddressState() + "' , '"
				+ obj.getShipAddressPostalCode() + "' , '"
				+ obj.getShipAddressCountry() + "' , '"
				+ obj.getShipAddressNote() + "' , ");
		query.append(" '" + obj.getIsPending() + "' , '" + obj.getCheckNo()
				+ "' , '" + obj.getPaymentMethodRefKey() + "' , '"
				+ obj.getSalesRefKey() + "' , '" + obj.getfOB() + "' , '"
				+ sdf.format(obj.getShipDate()) + "' , '"
				+ obj.getShipMethodRefKey() + "' , '"
				+ obj.getItemSalesTaxRefKey() + "' , '" + obj.getMemo()
				+ "' , '" + obj.getCustomerMsgRefKey() + "' ,");
		query.append(" '" + obj.getIsToBePrinted() + "' , '"
				+ obj.getIsToEmailed() + "' , '" + obj.getIsTaxIncluded()
				+ "' , '" + obj.getCustomerSalesTaxCodeRefKey() + "' , '"
				+ obj.getOther() + "' , '" + obj.getAmount() + "' , '"
				+ obj.getQuotationRecNo() + "' , '" + obj.getSendViaReffKey()
				+ "' , '" + obj.getCustomField1() + "' ,");
		query.append(" '" + obj.getCustomField2() + "' , '"
				+ obj.getCustomField3() + "' , '" + obj.getCustomField4()
				+ "' , '" + obj.getCustomField5() + "' , '" + obj.getStatus()
				+ "' , '" + obj.getDescriptionHIDE() + "' , '"
				+ obj.getQtyHIDE() + "' , '" + obj.getClassHIDE() + "' , '"
				+ obj.getRateHIDE() + "' , '"
						+ obj.getTransformD()+ "'," + webUserID);
		query.append(" )");
		query.append(" ");
		return query.toString();

	}

	public String updateCashInvoiceQuery(CashInvoiceModel obj, int webUserID) {
		String editedFromOnline = "Y";
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Update SalesReceipt set CustomerRefKey="
				+ obj.getCustomerRefKey() + ",ClassRefKey="
				+ obj.getClassRefKey() + ",DepositAccountRefKey="
				+ obj.getDepositAccountRefKey() + ",TemplateRefKey="
				+ obj.getTemplateRefKey() + ",TxnDate='"
				+ sdf.format(obj.getTxnDate()) + "',RefNumber='"
				+ obj.getRefNumber() + "',BillAddress1='"
				+ obj.getBillAddress1() + "',");
		query.append("BillAddress2='" + obj.getBillAddress2()
				+ "',BillAddress3='" + obj.getBillAddress3()
				+ "',BillAddress4='" + obj.getBillAddress4()
				+ "',BillAddress5='" + obj.getBillAddress5()
				+ "',BillAddressCity='" + obj.getBillAddressCity()
				+ "',BillAddressState='" + obj.getBillAddressState()
				+ "',BillAddressPostalCode='" + obj.getBillAddressPostalCode()
				+ "',BillAddressCountry='" + obj.getBillAddressCountry() + "',");
		query.append("BillAddressNote='" + obj.getBillAddressNote()
				+ "',ShipAddress1='" + obj.getShipAddress1()
				+ "',ShipAddress2='" + obj.getShipAddress2()
				+ "',ShipAddress3='" + obj.getShipAddress3()
				+ "',ShipAddress4='" + obj.getShipAddress4()
				+ "',ShipAddress5='" + obj.getShipAddress5()
				+ "',ShipAddressCity='" + obj.getShipAddressCity()
				+ "',ShipAddressState='" + obj.getShipAddressState() + "',");
		query.append("ShipAddressPostalCode='" + obj.getShipAddressPostalCode()
				+ "',ShipAddressCountry='" + obj.getShipAddressCountry()
				+ "',ShipAddressNote='" + obj.getShipAddressNote()
				+ "',IsPending='" + obj.getIsPending() + "',CheckNo='"
				+ obj.getCheckNo() + "',PaymentMethodRefKey="
				+ obj.getPaymentMethodRefKey() + ",SalesRefKey="
				+ obj.getSalesRefKey() + ",FOB='" + obj.getfOB() + "',");
		query.append("ShipDate='" + sdf.format(obj.getShipDate())
				+ "',ShipMethodRefKey=" + obj.getShipMethodRefKey()
				+ ",ItemSalesTaxRefKey=" + obj.getItemSalesTaxRefKey()
				+ ",Memo='" + obj.getMemo() + "',CustomerMsgRefKey="
				+ obj.getCustomerMsgRefKey() + ",IsToBePrinted='"
				+ obj.getIsToBePrinted() + "',IsToEmailed='"
				+ obj.getIsToEmailed() + "',IsTaxIncluded='"
				+ obj.getIsTaxIncluded() + "',");
		query.append("CustomerSalesTaxCodeRefKey="
				+ obj.getCustomerSalesTaxCodeRefKey() + ",Other='"
				+ obj.getOther() + "',Amount=" + obj.getAmount()
				+ ",QuotationRecNo='" + obj.getQuotationRecNo()
				+ "',SendViaReffKey='" + obj.getSendViaReffKey()
				+ "',CustomField1='" + obj.getCustomField1()
				+ "',CustomField2='" + obj.getCustomField2()
				+ "',CustomField3='" + obj.getCustomField3() + "',");
		query.append("CustomField4='" + obj.getCustomField4()
				+ "',CustomField5='" + obj.getCustomField5() + "',status='"
				+ obj.getStatus() + "',DescriptionHIDE='"
				+ obj.getDescriptionHIDE() + "',QtyHIDE='" + obj.getQtyHIDE()
				+ "',ClassHIDE='" + obj.getClassHIDE() + "',RateHIDE='"
				+ obj.getRateHIDE() + "',webUserID=" + webUserID
				+ ",editedFromOnline='" + editedFromOnline + "'  where recNO="
				+ obj.getRecNo());
		return query.toString();

	}

	public String addNewCreditInvoiceQuery(CashInvoiceModel obj, int webUserID) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String invoiceSource = "CMS";
		query = new StringBuffer();
		query.append("Insert into Invoice (RecNo,CustomerRefKey,ClassRefKey,ARAccountRefKey,TemplateRefKey,TxnDate,RefNumber,BillAddress1,");
		query.append("BillAddress2,BillAddress3,BillAddress4,BillAddress5,BillAddressCity,BillAddressState,BillAddressPostalCode,BillAddressCountry,BillAddressNote,ShipAddress1,");
		query.append("ShipAddress2,ShipAddress3,ShipAddress4,ShipAddress5,ShipAddressCity,ShipAddressState,ShipAddressPostalCode,ShipAddressCountry,ShipAddressNote,");
		query.append("IsPending,PONumber,TermsRefKey,DueDate,SalesRefKey,FOB,ShipDate,ShipMethodRefKey,ItemSalesTaxRefKey,Memo,CustomerMsgRefKey,IsToBePrinted,IsToEmailed,IsTaxIncluded,");
		query.append("CustomerSalesTaxCodeRefKey,Other,Amount,QuotationRecNo,SendViaReffKey,CustomField1,CustomField2,CustomField3,CustomField4,CustomField5,status,invoice_source,DescriptionHIDE,QtyHIDE,ClassHIDE,RateHIDE,transformD,webUserID)");
		query.append(" Values(" + obj.getRecNo() + ","
				+ obj.getCustomerRefKey() + "," + obj.getClassRefKey() + ","
				+ obj.getAccountRefKey() + "," + obj.getTemplateRefKey() + ",");
		query.append(" '" + sdf.format(obj.getTxnDate()) + "','"
				+ obj.getRefNumber() + "' ,' " + obj.getBillAddress1()
				+ "' , '" + obj.getBillAddress2() + "' , '"
				+ obj.getBillAddress3() + "' , '" + obj.getBillAddress4()
				+ "' , '" + obj.getBillAddress5() + "' , ");
		query.append(" '" + obj.getBillAddressCity() + "','"
				+ obj.getBillAddressState() + "' , '"
				+ obj.getBillAddressPostalCode() + "' , '"
				+ obj.getBillAddressCountry() + "' , '"
				+ obj.getBillAddressNote() + "' , '" + obj.getShipAddress1()
				+ "' ,");
		query.append(" '" + obj.getShipAddress2() + "' , '"
				+ obj.getShipAddress3() + "' , '" + obj.getShipAddress4()
				+ "' , '" + obj.getShipAddress5() + "' , '"
				+ obj.getShipAddressCity() + "' , '"
				+ obj.getShipAddressState() + "' , '"
				+ obj.getShipAddressPostalCode() + "' , '"
				+ obj.getShipAddressCountry() + "' , '"
				+ obj.getShipAddressNote() + "' , ");
		query.append(" '" + obj.getIsPending() + "' , '" + obj.getPoNumber()
				+ "' ," + obj.getTermRefKey() + ", '"
				+ sdf.format(obj.getDueDate()) + "' , '" + obj.getSalesRefKey()
				+ "' , '" + obj.getfOB() + "' , '"
				+ sdf.format(obj.getShipDate()) + "' , '"
				+ obj.getShipMethodRefKey() + "' , '"
				+ obj.getItemSalesTaxRefKey() + "' , '" + obj.getMemo()
				+ "' , '" + obj.getCustomerMsgRefKey() + "' ,");
		query.append(" '" + obj.getIsToBePrinted() + "' , '"
				+ obj.getIsToEmailed() + "' , '" + obj.getIsTaxIncluded()
				+ "' , '" + obj.getCustomerSalesTaxCodeRefKey() + "' , '"
				+ obj.getOther() + "' , '" + obj.getAmount() + "' , '"
				+ obj.getQuotationRecNo() + "' , '" + obj.getSendViaReffKey()
				+ "' , '" + obj.getCustomField1() + "' ,");
		query.append(" '" + obj.getCustomField2() + "' , '"
				+ obj.getCustomField3() + "' , '" + obj.getCustomField4()
				+ "' , '" + obj.getCustomField5() + "' , '" + obj.getStatus()
				+ "' , '" + invoiceSource + "' ,'" + obj.getDescriptionHIDE()
				+ "' , '" + obj.getQtyHIDE() + "' , '" + obj.getClassHIDE()
				+ "' , '" + obj.getRateHIDE() + "','"+ obj.getTransformD() + "'," + webUserID);
		query.append(")");
		query.append(" ");
		return query.toString();

	}

	public String updateCreditInvoiceQuery(CashInvoiceModel obj, int webUserID) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String editedFromOnline = "Y";
		String invoiceSource = "CMS";
		query = new StringBuffer();
		query.append("Update Invoice set CustomerRefKey="
				+ obj.getCustomerRefKey() + ",ClassRefKey="
				+ obj.getClassRefKey() + ",ARAccountRefKey="
				+ obj.getAccountRefKey() + ",TemplateRefKey="
				+ obj.getTemplateRefKey() + ",TxnDate='"
				+ sdf.format(obj.getTxnDate()) + "',RefNumber='"
				+ obj.getRefNumber() + "',BillAddress1='"
				+ obj.getBillAddress1() + "',");
		query.append("BillAddress2='" + obj.getBillAddress2()
				+ "',BillAddress3='" + obj.getBillAddress3()
				+ "',BillAddress4='" + obj.getBillAddress4()
				+ "',BillAddress5='" + obj.getBillAddress5()
				+ "',BillAddressCity='" + obj.getBillAddressCity()
				+ "',BillAddressState='" + obj.getBillAddressState()
				+ "',BillAddressPostalCode='" + obj.getBillAddressPostalCode()
				+ "',BillAddressCountry='" + obj.getBillAddressCountry() + "',");
		query.append("BillAddressNote='" + obj.getBillAddressNote()
				+ "',ShipAddress1='" + obj.getShipAddress1()
				+ "',ShipAddress2='" + obj.getShipAddress2()
				+ "',ShipAddress3='" + obj.getShipAddress3()
				+ "',ShipAddress4='" + obj.getShipAddress4()
				+ "',ShipAddress5='" + obj.getShipAddress5()
				+ "',ShipAddressCity='" + obj.getShipAddressCity()
				+ "',ShipAddressState='" + obj.getShipAddressState() + "',");
		query.append("ShipAddressPostalCode='" + obj.getShipAddressPostalCode()
				+ "',ShipAddressCountry='" + obj.getShipAddressCountry()
				+ "',ShipAddressNote='" + obj.getShipAddressNote()
				+ "',IsPending='" + obj.getIsPending() + "',PONumber='"
				+ obj.getPoNumber() + "',TermsRefKey=" + obj.getTermRefKey()
				+ ",DueDate='" + sdf.format(obj.getDueDate())
				+ "',SalesRefKey=" + obj.getSalesRefKey() + ",FOB='"
				+ obj.getfOB() + "',");
		query.append("ShipDate='" + sdf.format(obj.getShipDate())
				+ "',ShipMethodRefKey=" + obj.getShipMethodRefKey()
				+ ",ItemSalesTaxRefKey=" + obj.getItemSalesTaxRefKey()
				+ ",Memo='" + obj.getMemo() + "',CustomerMsgRefKey="
				+ obj.getCustomerMsgRefKey() + ",IsToBePrinted='"
				+ obj.getIsToBePrinted() + "',IsToEmailed='"
				+ obj.getIsToEmailed() + "',IsTaxIncluded='"
				+ obj.getIsTaxIncluded() + "',");
		query.append("CustomerSalesTaxCodeRefKey="
				+ obj.getCustomerSalesTaxCodeRefKey() + ",Other='"
				+ obj.getOther() + "',Amount=" + obj.getAmount()
				+ ",QuotationRecNo='" + obj.getQuotationRecNo()
				+ "',SendViaReffKey='" + obj.getSendViaReffKey()
				+ "',CustomField1='" + obj.getCustomField1()
				+ "',CustomField2='" + obj.getCustomField2()
				+ "',CustomField3='" + obj.getCustomField3() + "',");
		query.append("CustomField4='" + obj.getCustomField4()
				+ "',CustomField5='" + obj.getCustomField5() + "',status='"
				+ obj.getStatus() + "',invoice_source='" + invoiceSource
				+ "',DescriptionHIDE='" + obj.getDescriptionHIDE()
				+ "',QtyHIDE='" + obj.getQtyHIDE() + "',ClassHIDE='"
				+ obj.getClassHIDE() + "',RateHIDE='" + obj.getRateHIDE()
				+ "',webUserID=" + webUserID + ",editedFromOnline='"
				+ editedFromOnline + "'  where recNO=" + obj.getRecNo());
		return query.toString();

	}

	// cash Invoice By Iqbal

	// Save New Cheque Payment
	public String GetNewCashInvoiceRecNoQuery() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from SalesReceipt");
		return query.toString();
	}

	public String GetNewCreditInvoiceRecNoQuery() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from INVOICE");
		return query.toString();
	}

	// changes by iqbal for chash invoice

	public String checkIfItemHasSubQuery(String itemName) {
		query = new StringBuffer();
		query.append("Select FullName from QBItems  Where ");
		if (!itemName.equals("")) {
			query.append(" CharIndex('" + itemName + "' ,FullName)>0 ");
		}
		return query.toString();
	}

	public String deleteCashInvoiceGridItemsQuery(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from SalesReceiptLine Where RecNo=" + RecNo);
		return query.toString();
	}

	public String deleteCreditInvoiceGridItemsQuery(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from INVOICELINE  Where RecNo=" + RecNo);
		return query.toString();
	}

	public String addNewCashInvoiceGridItemsQuery(CashInvoiceGridData obj,
			int RecNo) {
		query = new StringBuffer();
		String desc = "";
		if (obj.getInvoiceDescription() != null) {
			desc = obj.getInvoiceDescription().replace("'", "`");
		}
		query.append(" Insert into SalesReceiptLine (RecNo,[LineNo],ItemRefKey,Description,DescriptionAR,Quantity,Rate,AvgCost,RatePercent,PriceLevelRefKey,ClassRefKey,Amount,ServiceDate,SalesTaxCodeRefKey,IsTaxable,OverrideItemAccountRefKey,Other1,Other2,QuotationLineNo,InventorySiteKey,BarCode)");
		query.append(" values(" + RecNo + ", '" + obj.getLineNo() + "' , '"
				+ obj.getSelectedItems().getRecNo() + "', '" + desc + "', '"
				+ obj.getInvoicearabicDescription() + "' , '"
				+ obj.getInvoiceQty() + "' , '" + obj.getInvoiceRate()
				+ "' , '" + obj.getAvgCost() + "' , '" + obj.getRatePercent()
				+ "' , '" + obj.getPriceLevelRefKey() + "', ");
		if (obj.getSelectedInvcCutomerGridInvrtyClassNew() != null)
			query.append(obj.getSelectedInvcCutomerGridInvrtyClassNew()
					.getRecNo());
		else
			query.append("0");
		query.append(" ," + obj.getInvoiceAmmount() + ", '"
				+ sdf.format(obj.getServiceDate()) + "' , '"
				+ obj.getSalesTaxCodeRefKey() + "' , '" + obj.getIsTaxable()
				+ "' , '" + obj.getOverrideItemAccountRefKey() + "' , '"
				+ obj.getOther1() + "' , '" + obj.getOther2() + "' , '"
				+ obj.getQuotationLineNo());
		if (obj.getSelectedInvcCutomerGridInvrtySiteNew() != null) {
			query.append("', "
					+ obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo());
			query.append(",'" + obj.getBarcode() + "'");
			query.append(" )");
		} else
			query.append(" ',0,'" + obj.getBarcode() + "')");
		query.append(" ");

		return query.toString();
	}

	public String addNewCreditInvoiceGridItemsQuery(CashInvoiceGridData obj,
			int RecNo) {
		query = new StringBuffer();
		String desc = "";
		if (obj.getInvoiceDescription() != null) {
			desc = obj.getInvoiceDescription().replace("'", "`");
		}
		query.append(" Insert into InvoiceLine (RecNo,[LineNo],ItemRefKey,Description,DescriptionAR,Quantity,Rate,AvgCost,RatePercent,PriceLevelRefKey,ClassRefKey,Amount,ServiceDate,SalesTaxCodeRefKey,IsTaxable,OverrideItemAccountRefKey,Other1,Other2,QuotationLineNo,InventorySiteKey,DeliveryRecNo,DeliveryLineNo,DeliveryTxnID,DeliveryTxnLineID)");
		query.append(" values(" + RecNo + ", '" + obj.getLineNo() + "' , '"
				+ obj.getSelectedItems().getRecNo() + "', '" + desc + "', '"
				+ obj.getInvoicearabicDescription() + "' , '"
				+ obj.getInvoiceQty() + "' , '" + obj.getInvoiceRate()
				+ "' , '" + obj.getAvgCost() + "' , '" + obj.getRatePercent()
				+ "' , '" + obj.getPriceLevelRefKey() + "', ");
		if (obj.getSelectedInvcCutomerGridInvrtyClassNew() != null)
			query.append(obj.getSelectedInvcCutomerGridInvrtyClassNew()
					.getRecNo());
		else
			query.append("0");
		query.append(" ," + obj.getInvoiceAmmount() + ", '"
				+ sdf.format(obj.getServiceDate()) + "' , '"
				+ obj.getSalesTaxCodeRefKey() + "' , '" + obj.getIsTaxable()
				+ "' , '" + obj.getOverrideItemAccountRefKey() + "' , '"
				+ obj.getOther1() + "' , '" + obj.getOther2() + "' , '"
				+ obj.getQuotationLineNo());
		if (obj.getSelectedInvcCutomerGridInvrtySiteNew() != null) {
			query.append("', "
					+ obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo());
			query.append(",0,0,'',''");
			query.append(" )");
		} else
			query.append(" ',0,0,0,'','')");
		query.append(" ");

		return query.toString();
	}

	/**
	 * Gets List of Barcodes or the barcode of a selected itemKey
	 * 
	 * @param itemKey
	 * @return
	 */
	public String getLstBarcode(String itemKey) {
		query = new StringBuffer();
		query.append("Select Barcode from QBItems Where Barcode is not null");
		if (itemKey != null && !itemKey.trim().equals("")) {
			query.append(" And Barcode = '" + itemKey + "' ) ");
		}
		return query.toString();
	}

	public String getSelectedBCItem(String barcode) {
		query = new StringBuffer();
		query.append("select * from QBItems where BARCODE = '" + barcode + "'");
		return query.toString();
	}

	public String getOtherListQuery(String status, String sortBy) {
		query = new StringBuffer();
		// query.append("Select * from Customer");
		if (status.equalsIgnoreCase("")) {
			query.append("Select * from OtherNames Order by " + sortBy);
		} else {
			query.append("Select * from OtherNames  Where  IsActive='" + status
					+ "' Order by " + sortBy);
		}
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicate(String serialNumber, int recNo) 
	{
		query = new StringBuffer();
		query.append(" Select * from SalesReceipt Where RefNumber='"+ serialNumber + "' and recno !=" + recNo);
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicateForCreditInvoice(String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from Invoice Where RefNumber='" + serialNumber + "' and recno !=" + recNO);
		return query.toString();
	}

	public String getOtherListQuery() {
		query = new StringBuffer();
		// query.append("Select * from Customer");
		query.append("Select * from OtherNames  Where  IsActive='Y' Order by Replace(FullName,':',':'),PriorityID Desc");
		return query.toString();
	}

	public String getPropetyListQuery() {
		query = new StringBuffer();
		query.append("Select Building.*,Class.Name as ClassName,Class.Class_Type,Class.Class_Key,");
		query.append("PropertyList.Description as PropertyType,");
		query.append("StreetList.Description as Street,");
		query.append("CityList.Description as City,");
		query.append("CountryList.Description as Country from ");
		query.append("(((((Building Left Join HRLISTVALUES As StreetList ON Building.Street_ID=StreetList.ID) ");
		query.append("Left Join HRLISTVALUES As CityList ON Building.City_ID=CityList.ID)");
		query.append("Left Join HRLISTVALUES As CountryList On Building.Country_ID=CountryList.ID)");
		query.append("Left Join HRLISTVALUES As PropertyList On Building.Property_Type=PropertyList.ID)");
		query.append("Right Join Class  On Building.RecNo=Class.Class_Key)");
		query.append("where Class.Class_Type='B'");
		query.append("Order by Class.Name");
		return query.toString();
	}

	public String getVehicleListQuery() {
		query = new StringBuffer();
		query.append("SELECT VEHICLEMAST.*,Customer.FullName,Customer.PHONE,AssetCode,AssetName,");
		query.append("Customer.ALTPHONE,TypeList.Description As VTypeEn,TypeList.Arabic As VTypeAR,");
		query.append("BrandList.Description As BrandEn,BrandList.Arabic As BrandAR,");
		query.append("SeriesList.Description As SeriesEn,SeriesList.Arabic As SeriesAR,");
		query.append("ColorList.Description As ColorEn,ColorList.Arabic As ColorAR FROM ");
		query.append("VEHICLEMAST LEFT JOIN Customer ON ");
		query.append("VEHICLEMAST.CUSTOMER_ID=Customer.CUST_KEY ");
		query.append("LEFT JOIN HRLISTVALUES As TypeList ON ");
		query.append("VEHICLEMAST.Vehicle_Type=TypeList.ID ");
		query.append("LEFT JOIN HRLISTVALUES As BrandList ON ");
		query.append("VEHICLEMAST.Brand=BrandList.ID ");
		query.append("LEFT JOIN HRLISTVALUES As SeriesList ON ");
		query.append("VEHICLEMAST.Series=SeriesList.ID ");
		query.append("LEFT JOIN HRLISTVALUES As ColorList ON ");
		query.append("VEHICLEMAST.Color=ColorList.ID ");
		query.append("LEFT JOIN ASSETMASTER ON ");
		query.append("VEHICLEMAST.Asset_ID=AssetMaster.AssetID");
		return query.toString();
	}

	public String getNameFromQbListForValidation() {
		query = new StringBuffer();
		query.append("Select name,recno from QBLists");
		return query.toString();

	}

	public String GetOtherNameListRecNoQuery() {
		query = new StringBuffer();
		query.append("SELECT max(recNo) as MaxRecNo from QBLists");
		return query.toString();
	}

	public String OtherNameListInsertQuery(OtherNamesModel obj, int recNo) {
		query = new StringBuffer();
		query.append("insert into OtherNames (othnam_key,name,fullname,arname,timecreated,companyname,billAddress1,phone,fax,email,contact,ActName,accountno,AltPhone,ALTCONTACT,SkypeID,BankName,BranchName,IBANNo,PrintChequeAs)values(");
		query.append(recNo + ",'" + obj.getName() + "','" + obj.getFullName()
				+ "' , '" + obj.getArName() + "' , '"
				+ sdf.format(obj.getCreatedDate()) + "', '"
				+ obj.getCompanyName() + "', '" + obj.getBillCountry() + "', '"
				+ obj.getPhone() + "' , '" + obj.getFax() + "' , '"
				+ obj.getEmail() + "', '" + obj.getContact() + "','"
				+ obj.getAccountName() + "','" + obj.getAccountNumber() + "'");
		query.append(",'" + obj.getAltphone() + "', '"
				+ obj.getAlternateContact() + "' ,'" + obj.getSkypeID()
				+ "', '" + obj.getBankName() + "' ,'" + obj.getBranchName()
				+ "', '" + obj.getbBANNumber() + "', '" + obj.getName() + "')");
		return query.toString();
	}

	public String OtherNameListInsertQbListQuery(OtherNamesModel obj, int recNo) {
		String listType = "OtherNames";
		String isactive = "Y";
		query = new StringBuffer();
		query.append("insert into QBLists (recno,listType,name,fullname,isactive,sublevel)values(");
		query.append(recNo + ",'" + listType + "' , '" + obj.getName() + "', '"
				+ obj.getName() + "', '" + isactive + "'," + 0 + ")");
		return query.toString();
	}

	public String getCutomerSummaryReport() {
		query = new StringBuffer();
		query.append("Select * From RPTQBBALANCESUMMARY Where Form_Type='C' Order by Rec_No");
		return query.toString();
	}

	public String getCustomerStatusHistoryReport(Date fromDate, Date toDate) {
		query = new StringBuffer();
		query.append("select CustomerStatusHistory.*,customerlist.fullname,prospectiveList.fullname as fullNameProspecive from dbo.CustomerStatusHistory left join QBLists as customerlist ON CustomerStatusHistory.custKey = customerlist.recNo left join Prospective as prospectiveList ON CustomerStatusHistory.custKey = prospectiveList.recNo where actiondate Between '" + sdf.format(fromDate)+ "' And '" + sdf.format(toDate) + "'order by CustomerStatusHistory.actionDate desc");
		return query.toString();
	}
	
	public String getNewCustomerBalanceReport(int customerID,String isActive)
	{
		query = new StringBuffer();
		query.append(" SELECT recno,FullName,IsActive,");
		query.append(" tmpDebit ,tmpCredit , tmpDebit-abs(tmpCredit) as 'Balance'");
		query.append(" from");
		query.append(" (select recno,FullName,IsActive,");
		query.append(" isnull(TotalDebitExpenses,0)+ isnull(TotalDebitAdjustment,0) +isnull(TotalInvoices,0)+isnull(TotalDebitJouranl,0)+isnull(TotalDebitContract,0) as 'tmpDebit',");
		query.append(" isnull(TotalCreditMemo,0)+isnull(TotalRVMast,0)+isnull(abs(TotalCreditAdjustment),0)+isnull(TotalCreditJouranl,0) as 'tmpCredit'");
		query.append(" FROM [dbo].[WVU_CustomerBalanceReport]");
		query.append(" )t");
		
		query.append(" where 1=1 ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append(" and IsActive='" + isActive + "'");
		if (customerID > 0)
			query.append(" and recno=" + customerID);
		query.append(" order by FullName");
		query.append(" ");
		query.append(" ");
		query.append(" ");
		
		return query.toString();
	}
	public String getNewCustomerBalanceReportOLD(int customerID,String isActive)
	{
		query = new StringBuffer();
		query.append("Select Name,RecNo,FullName, ");
		query.append(" (Select sum(Amount) from Invoice where CustomerRefKey =QbLists.RecNo) as 'TotalInvoices',");
		query.append(" (Select sum(TotalAmount) From RVMast where CustomerRef_Key =QbLists.RecNo ) as 'TotalRVMast' ,");
		query.append(" (Select sum(case when Adjustment_Amount>0 then Adjustment_Amount else 0 end ) from CustomerAdjustments Where Cust_Key = QbLists.RecNo) as 'TotalDebitAdjustment',");
		query.append(" (Select sum(case when Adjustment_Amount<0 then Adjustment_Amount else 0 end ) from CustomerAdjustments Where Cust_Key = QbLists.RecNo) as 'TotalCreditAdjustment',");
		query.append(" (Select sum(Amount) from CheckExpense Where Custkey = QbLists.RecNo) as 'TotalDebitExpenses',");
		query.append(" (Select sum(Amount) from CreditMemo Where CustomerRefKey = QbLists.RecNo) as 'TotalCreditMemo',");
		query.append(" (Select sum(Amount) from journalLine left join Accounts On Accounts.Rec_No =  journalLine.AccountRef_Key  Where EntityRef_Type In('Customer')");
		query.append(" And AccountType in ('AccountsReceivable') and DC_Flag='C'  and EntityRef_Key  = QbLists.RecNo) as 'TotalCreditJouranl',");
		query.append(" (Select sum(Amount) from journalLine left join Accounts On Accounts.Rec_No =  journalLine.AccountRef_Key  Where EntityRef_Type In('Customer')");
		query.append(" And AccountType in ('AccountsReceivable') and DC_Flag='D'  and EntityRef_Key  = QbLists.RecNo) as 'TotalDebitJouranl',");
		query.append(" (Select Sum(Contractposting.Amount) + sum(Othercharges.Amount)  from Contractposting");
		query.append(" inner join contractmast on Contractposting.rec_no = contractmast.recno ");
		query.append(" left join Othercharges on Othercharges.contractrecno=contractmast.recno");
		query.append(" Where   Contractmast.Status not in ('V')  and QbLists.RecNo=tenantKey) as 'TotalDebitContract'");
		query.append(" from QbLists where ListType in ('Customer')  ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append(" and QbLists.isActive='" + isActive + "'");
		if (customerID > 0)
			query.append(" and QbLists.recno=" + customerID);
		query.append(" order by FullName");
		query.append(" ");
		query.append(" ");
		query.append(" ");
		
		return query.toString();
	}

	public String getCutomerDetailedReport(Date toDate, int customerID,String isActive, boolean inculdeOtherTransactions) {
		query = new StringBuffer();
		// query.append("Select * From RPTQBBALANCEDETAILED Where Form_Type='C'  Order by Rec_No");
		// need to add later /*AND FROM_DATE >= '"+sdf.format(fromDate)+"' AND
		// TO_DATE <= '"+sdf.format(toDate)+"'*/

		// For Credit Invoice
		query.append(" select distinct * from ( Select customer.cust_key,invoice.recNo,invoice.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,'Invoice' as trasType,COALESCE(invoice.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer ");
		query.append(" LEFT JOIN invoice ON customer.cust_key = invoice.customerRefkey ");
		query.append(" LEFT JOIN Accounts ON invoice.ArAccountRefKey=Accounts.rec_no ");
		if (customerID > 0 || (isActive != null && !isActive.equalsIgnoreCase("")) || toDate != null)
			query.append(" where Accounts.accountType='AccountsReceivable' and ");
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append(" customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" invoice.TxnDate <=  '" + sdf.format(toDate) + "' ");

		if (inculdeOtherTransactions) {

			query.append("  union  ALL ");

			query.append(" select customer.cust_key,invoice.recNo,invoice.TxnDate,qbitems.fullname as ItemOrAccountName, customer.fullname,customer.companyName,'Invoice' as trasType,0 as Debit,COALESCE(invoiceline.amount,0) as Credit,'N' as debitFlag from customer ");
			query.append(" LEFT JOIN invoice ON customer.cust_key = invoice.customerRefkey ");
			query.append("left join invoiceline on invoiceline.recNo = invoice.recNO ");
			query.append(" left join qbitems on invoiceline.itemRefKey=qbitems.item_key ");
			if (customerID > 0 || (isActive != null && !isActive.equalsIgnoreCase("")) || toDate != null)
				query.append(" where ");
			if (customerID > 0)
				query.append(" customer.cust_key=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append(" customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" invoice.TxnDate <=  '" + sdf.format(toDate) + "' ");
		}

		query.append("  union  ALL ");

		// For Cash Invoice
		query.append(" select customer.cust_key,SalesReceipt.recNo,SalesReceipt.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,'Sales Receipt' as trasType,COALESCE(SalesReceipt.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer ");
		query.append(" LEFT JOIN SalesReceipt ON customer.cust_key = SalesReceipt.customerRefkey ");
		query.append(" LEFT JOIN Accounts ON SalesReceipt.depositAccountRefKey=Accounts.rec_no ");

		if (customerID > 0
				|| (isActive != null && !isActive.equalsIgnoreCase(""))
				|| toDate != null)
			query.append(" where Accounts.accountType='AccountsReceivable' and ");
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append("  customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" SalesReceipt.TxnDate <= '" + sdf.format(toDate)
					+ "' ");

		if (inculdeOtherTransactions) {
			query.append("  union  ALL ");

			query.append(" select customer.cust_key,SalesReceipt.recNo,SalesReceipt.TxnDate,qbitems.fullname as ItemOrAccountName, customer.fullname,customer.companyName,'Sales Receipt' as trasType,0 as Debit,COALESCE(SalesReceiptLine.amount,0) as Credit,'N' as debitFlag from customer ");
			query.append(" LEFT JOIN SalesReceipt ON customer.cust_key = SalesReceipt.customerRefkey ");
			query.append(" left join SalesReceiptLine on SalesReceiptLine.recNo = SalesReceipt.recNO  ");
			query.append(" left join qbitems on SalesReceiptLine.itemRefKey=qbitems.item_key ");
			if (customerID > 0
					|| (isActive != null && !isActive.equalsIgnoreCase(""))
					|| toDate != null)
				query.append(" where ");
			if (customerID > 0)
				query.append(" customer.cust_key=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append("  customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" SalesReceipt.TxnDate <= '" + sdf.format(toDate)
						+ "' ");
		}

		query.append("  union  ALL ");

		// For Reciept Voucher
		query.append(" select customer.cust_key,RVMast.rec_No as recNo,RVMast.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,RVMast.rvorJv as trasType,0 as Debit,COALESCE(RVMast.totalAmount,0) as Credit ,'N' as debitFlag from customer ");
		query.append(" LEFT JOIN RVMast ON customer.cust_key = RVMast.customerRef_key ");
		query.append(" LEFT JOIN Accounts ON RVMast.ArAccountRef_Key=Accounts.rec_no ");
		if (customerID > 0
				|| (isActive != null && !isActive.equalsIgnoreCase(""))
				|| toDate != null)
			query.append(" where  Accounts.accountType='AccountsReceivable' and ");
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append("  customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" RVMast.TxnDate <= '" + sdf.format(toDate) + "' ");

		if (inculdeOtherTransactions) {
			query.append("  union  ALL ");

			query.append(" select customer.cust_key,RVMast.rec_No as recNo,RVMast.TxnDate,Accounts.fullname as ItemOrAccountName, customer.fullname,customer.companyName,RVMast.rvorJv as trasType,COALESCE(RVDetails.amount,0) as Debit,0 as Credit ,'Y' as debitFlag from customer ");
			query.append(" LEFT JOIN RVMast ON customer.cust_key = RVMast.customerRef_key ");
			query.append(" left join RVDetails on RVDetails.rec_No = RVMast.rec_NO ");
			query.append(" left join Accounts on RVDetails.depositToAccountRef_Key=Accounts.rec_no ");
			if (customerID > 0
					|| (isActive != null && !isActive.equalsIgnoreCase(""))
					|| toDate != null)
				query.append(" where ");
			if (customerID > 0)
				query.append(" customer.cust_key=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append("  customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" RVMast.TxnDate <= '" + sdf.format(toDate) + "' ");
		}

		query.append("  union  ALL ");

		query.append(" Select customer.cust_key,CreditMemo.recNo,CreditMemo.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,'Credit Memo' as trasType,COALESCE(CreditMemo.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer ");
		query.append("LEFT JOIN CreditMemo ON customer.cust_key = CreditMemo.customerRefkey ");
		query.append("  LEFT JOIN Accounts ON CreditMemo.ArAccountRefKey=Accounts.rec_no ");
		if (customerID > 0
				|| (isActive != null && !isActive.equalsIgnoreCase(""))
				|| toDate != null)
			query.append(" where  Accounts.accountType='AccountsReceivable' and ");
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append("  customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" CreditMemo.TxnDate <= '" + sdf.format(toDate) + "' ");

		if (inculdeOtherTransactions) {
			query.append("  union  ALL ");

			query.append(" select customer.cust_key,CreditMemo.recNo,CreditMemo.TxnDate,qbitems.fullname as ItemOrAccountName, customer.fullname,customer.companyName,'Credit Memo' as trasType,0 as Debit,COALESCE(CreditMemo.amount,0) as Credit,'N' as debitFlag from customer ");
			query.append(" LEFT JOIN CreditMemo ON customer.cust_key = CreditMemo.customerRefkey ");
			query.append(" left join CreditMemoLine on CreditMemoLine.recNo = CreditMemo.recNO ");
			query.append(" left join qbitems on CreditMemoLine.itemRefKey=qbitems.item_key ");
			if (customerID > 0
					|| (isActive != null && !isActive.equalsIgnoreCase(""))
					|| toDate != null)
				query.append(" where ");
			if (customerID > 0)
				query.append(" customer.cust_key=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append("  customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" CreditMemo.TxnDate <= '" + sdf.format(toDate)
						+ "' ");

		}

		query.append("  union  ALL ");

		query.append(" Select customer.cust_key,JournalEntry.rec_No as recNo,JournalEntry.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName, ");
		query.append(" 'Journal General' as trasType, ");
		query.append(" (CASE  WHEN JournalLine.dc_flag ='D'  THEN JournalLine.amount  ELSE 0  END)  as Debit,  ");
		query.append("(CASE  WHEN JournalLine.dc_flag ='C'  THEN JournalLine.amount  ELSE 0  END)  as credit,  ");
		query.append("(CASE  WHEN JournalLine.dc_flag ='D'  THEN 'Y'  ELSE 'N'  END)  as debitFlag from customer ");
		query.append("LEFT JOIN JournalLine ON customer.cust_key = JournalLine.entityRef_key   ");
		query.append("  LEFT JOIN JournalEntry ON JournalEntry.rec_no = JournalLine.rec_no   ");
		query.append(" LEFT JOIN Accounts ON JournalLine.AccountRef_Key=Accounts.rec_no  Where EntityRef_Type In('Customer') and ");
		if (!inculdeOtherTransactions)
			query.append(" Accounts.accountType in ('AccountsReceivable') and ");
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append("  customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" JournalEntry.TxnDate <= '" + sdf.format(toDate)
					+ "' ");

		if (inculdeOtherTransactions) {
			query.append("  union  ALL ");
			// Check Mast

			query.append(" Select customer.cust_key,CheckMast.recNo,CheckMast.pvdate as TxnDate  ,Accounts.fullname  as ItemOrAccountName,customer.fullname,customer.companyName,'Payment' as trasType,0 as Debit,COALESCE(CheckMast.amount,0) as Credit,'N' as debitFlag from customer ");
			query.append(" LEFT JOIN CheckMast ON customer.cust_key = CheckMast.payeeKey ");
			query.append(" LEFT JOIN Accounts ON CheckMast.bankKey=Accounts.rec_no  ");
			query.append(" where PayeeType ='Customer' and ");
			if (customerID > 0)
				query.append(" customer.cust_key=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append("  customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" CheckMast.pvdate <= '" + sdf.format(toDate)
						+ "' ");
		}

		// check CheckExpense

		query.append("  union  ALL ");

		query.append(" Select customer.cust_key,CheckMast.recNo,CheckMast.pvdate as TxnDate ,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName, ");
		query.append("'Payment' as trasType,COALESCE(CheckExpense.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer  ");
		query.append(" LEFT JOIN CheckMast ON customer.cust_key = CheckMast.payeeKey  ");
		query.append("  left join CheckExpense on CheckExpense.recNo = CheckMast.recNO  ");
		query.append(" LEFT JOIN Accounts ON CheckExpense.AccountKey=Accounts.rec_no  ");

		if (customerID > 0
				|| (isActive != null && !isActive.equalsIgnoreCase(""))
				|| toDate != null)
			query.append(" where ");
		if (!inculdeOtherTransactions) {
			query.append(" Accounts.accountType='AccountsReceivable' and ");
		}
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append("  customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" CheckMast.pvdate <= '" + sdf.format(toDate) + "' ");

		query.append(" union  ALL ");

		query.append(" Select customer.cust_key, CheckMast.recNo as recNo ,CheckMast.pvdate as TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName, ");
		query.append(" 'Payment' as trasType,COALESCE(CheckExpense.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer  ");
		query.append(" LEFT JOIN CheckExpense ON customer.cust_key = CheckExpense.custKey ");
		query.append("LEFT JOIN Accounts ON CheckExpense.AccountKey=Accounts.rec_no  ");
		query.append(" LEFT JOIN CheckMast ON CheckMast.recNO  = CheckExpense.recNo ");
		if (customerID > 0
				|| (isActive != null && !isActive.equalsIgnoreCase(""))
				|| toDate != null)
			query.append(" where ");
		if (!inculdeOtherTransactions) {
			query.append(" Accounts.accountType='AccountsReceivable' and ");
		}
		if (customerID > 0)
			query.append(" CheckExpense.custKey=" + customerID + " and ");
		if (isActive != null && !isActive.equalsIgnoreCase(""))
			query.append("  customer.isActive='" + isActive + "' and ");
		if (toDate != null)
			query.append(" CheckMast.pvdate <= '" + sdf.format(toDate) + "' ");

		// CheckItems

		if (inculdeOtherTransactions) {

			query.append("  union  ALL ");

			query.append(" select customer.cust_key,CheckMast.recNo,CheckMast.pvdate as TxnDate,qbitems.fullname as ItemOrAccountName, customer.fullname,customer.companyName,'Payment' as trasType, ");
			query.append("	  COALESCE(CheckItems.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer  ");
			query.append("		  LEFT JOIN CheckMast ON customer.cust_key = CheckMast.payeeKey   ");
			query.append("	  left join CheckItems on CheckItems.recNo = CheckMast.recNO ");
			query.append("	  left join qbitems on CheckItems.itemKey=qbitems.item_key  ");
			if (customerID > 0
					|| (isActive != null && !isActive.equalsIgnoreCase(""))
					|| toDate != null)
				query.append(" where ");
			if (customerID > 0)
				query.append(" customer.cust_key=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append("  customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" CheckMast.pvdate <= '" + sdf.format(toDate)
						+ "' ");

			query.append("   union  ALL  ");

			query.append("	  select customer.cust_key,CheckMast.recNo,CheckMast.pvdate as TxnDate,qbitems.fullname as ItemOrAccountName, customer.fullname,customer.companyName,'Payment' as trasType, ");
			query.append("	  COALESCE(CheckItems.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer ");
			query.append("	  left join CheckItems on customer.cust_key = CheckItems.custKey ");
			query.append("	  left join qbitems on CheckItems.itemKey=qbitems.item_key  ");
			query.append("  LEFT JOIN CheckMast ON CheckItems.recNo = CheckMast.recNo  ");
			if (customerID > 0
					|| (isActive != null && !isActive.equalsIgnoreCase(""))
					|| toDate != null)
				query.append(" where ");
			if (customerID > 0)
				query.append(" CheckItems.custKey=" + customerID + " and ");
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				query.append("  customer.isActive='" + isActive + "' and ");
			if (toDate != null)
				query.append(" CheckMast.pvdate <= '" + sdf.format(toDate)
						+ "' ");

		}

		query.append("  union  ALL ");

		query.append(" Select customer.cust_key,0 as recNo,CustomerAdjustments.TxnDate,'' as ItemOrAccountName,customer.fullname,customer.companyName,'Local Adjustment' as trasType , ");

		query.append(" (CASE ");
		query.append(" WHEN CustomerAdjustments.adjustment_amount > 0 ");
		query.append(" THEN CustomerAdjustments.adjustment_amount ");
		query.append(" ELSE 0 ");
		query.append(" END)  as Debit, ");

		query.append(" (CASE ");
		query.append(" WHEN CustomerAdjustments.adjustment_amount < 0 ");
		query.append(" THEN CustomerAdjustments.adjustment_amount ");
		query.append(" ELSE 0 ");
		query.append(" END)  as credit, ");

		query.append(" (CASE ");
		query.append(" WHEN CustomerAdjustments.adjustment_amount > 0 ");
		query.append(" THEN 'Y' ");
		query.append(" ELSE 'N' ");
		query.append(" END)  as debitFlag from customer ");
		query.append(" LEFT JOIN CustomerAdjustments ON customer.cust_key = CustomerAdjustments.cust_key ");
		if (customerID > 0 || toDate != null)
			query.append(" where ");
		if (customerID > 0)
			query.append(" customer.cust_key=" + customerID + " and ");
		if (toDate != null)
			query.append(" CustomerAdjustments.TxnDate <= '"
					+ sdf.format(toDate) + "' ");
		query.append(" )t3  where t3.debit!=0 or t3.credit!=0  order by cust_key,TxnDate ");
		return query.toString();
	}

	public String getCashInvoiceSalesReport(Date fromDate, Date toDate,
			int webUserID,boolean seeTrasction) {

		query = new StringBuffer();
		query.append("SELECT QBLists.FullName AS CustomerName,Accounts.Name AS DepositAccount, ");
		query.append("SalesRep.Initial AS SalesRepName,SalesReceipt.RefNumber As InvoiceNo,SalesReceipt.TxnDate As InvoiceDate, ");
		query.append("SalesReceipt.Amount As InvAmount,SalesReceipt.CheckNo,SalesReceipt.Memo AS Notes, ");
		query.append("PaymentMethod.Name As PaymentType,SalesReceipt.RecNo,SalesReceipt.webuserId,SalesReceipt.Status As statusFlag ");
		query.append("FROM (((SalesReceipt LEFT JOIN QBLists         ON SalesReceipt.CustomerRefKey           = QBLists.RecNo) ");
		query.append("LEFT JOIN Accounts        ON SalesReceipt.DepositAccountRefKey     = Accounts.REC_NO)     ");
		query.append("LEFT JOIN PaymentMethod   ON SalesReceipt.PaymentMethodRefKey      = PaymentMethod.REC_NO) ");
		query.append("LEFT JOIN SalesRep        ON SalesReceipt.SalesRefKey              = SalesRep.SalesRep_Key where SalesReceipt.TxnDate Between '"
				+ sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "'");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and SalesReceipt.webuserID=" + webUserID + "");
		query.append(" Order by SalesReceipt.RecNo Desc, SalesReceipt.TxnDate,SalesReceipt.RefNumber");
		return query.toString();
	}

	public String getCreditInvoiceReport(Date fromDate, Date toDate,
			int webUserID) {

		query = new StringBuffer();
		query.append("SELECT QBLists.FullName AS CustomerName,Accounts.Name AS DepositAccount,SalesRep.Initial AS SalesRepName,Invoice.RefNumber As InvoiceNo, ");
		query.append(" Invoice.TxnDate As InvoiceDate,Invoice.DueDate As InvoiceDue,Invoice.status,Terms.Name AS TermName,Invoice.Amount As InvAmount,Invoice.AmountPaid As PaidAmount,Invoice.Invoice_Source, ");
		query.append(" Invoice.Memo AS Notes,Invoice.RecNo,Invoice.webuserId  FROM (((Invoice LEFT JOIN QBLists  ON Invoice.CustomerRefKey    = QBLists.RecNo)  LEFT JOIN Accounts ON Invoice.ARAccountRefKey  = Accounts.REC_NO) ");
		query.append(" LEFT JOIN SalesRep ON Invoice.SalesRefKey      = SalesRep.SalesRep_Key)   LEFT JOIN Terms    ON Invoice.TermsRefKey         = Terms.Term_Key where Invoice.TxnDate Between '"
				+ sdf.format(fromDate)
				+ "' And '"
				+ sdf.format(toDate)
				+ "' and Invoice.invoice_source='CMS'");
		if (webUserID > 0)
			query.append(" and Invoice.webuserID=" + webUserID + "");
		query.append(" Order by Invoice.RecNo Desc, Invoice.TxnDate");
		return query.toString();
	}

	public String getAccountsForCreditInvoice() {
		query = new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name], AccountType ,   SubLevel  ,   Rec_No , ListID FROM Accounts Inner join   AccountType on AccountType.TypeName = Accounts.AccountType where accounttype = 'AccountsReceivable' and  isActive='Y' order by AccountType.SRL_No, Accounts.ACTLEVELSwithNO");
		return query.toString();
	}

	public String getTermsForCreditInvoice() {
		query = new StringBuffer();
		query.append("Select * from Terms Order by [Name]");
		return query.toString();
	}

	public String getLocalBalnaceForCreditInvoice(int custKey) {
		query = new StringBuffer();
		query.append("Select Sum(Amount) as InvAmount,Sum(AmountPaid) as PaidAmount From Invoice  WHERE CustomerRefKey="
				+ custKey + "");
		return query.toString();
	}

	public String getCashInvoiceByID(int cashInvoiceKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from SalesReceipt where recNo=" + cashInvoiceKey
				+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String getCashInvoiceGridDataByID(int cashInvoiceKey) {
		query = new StringBuffer();
		query.append("SELECT SalesReceiptLine.*,QBItems.FullName  AS ItemName,QBItems.ArFullName AS ItemNameAR,QBItems.ListID,QBItems.ItemType,QBItems.IncomeAccountRef ,Class.FullName AS ClassName, ");
		query.append("Class.ArFullName AS ClassNameAR,DeliveryInvoiced.DeliveryRecNo,DeliveryInvoiced.DeliveryLineNo As DeliveryLineNo,InventorySiteList.SiteName as InventorySite,InventorySiteList.ArName as InventorySiteAR   ");
		query.append("FROM ((((SalesReceiptLine LEFT JOIN Class ON SalesReceiptLine.ClassRefKey = Class.Class_Key)   ");
		query.append("LEFT JOIN QBItems ON SalesReceiptLine.ItemRefKey = QBItems.Item_Key)   ");
		query.append("LEFT JOIN InventorySiteList ON SalesReceiptLine.InventorySiteKey = InventorySiteList.ItemKey)  ");
		query.append("LEFT JOIN DeliveryInvoiced ON CAST(SalesReceiptLine.RecNo AS VARCHAR) + 'S'  = CAST(DeliveryInvoiced.RecNo AS VARCHAR) + DeliveryInvoiced.TxnType   ");
		query.append("And SalesReceiptLine.[LineNo]  = DeliveryInvoiced.[LineNo])  ");
		query.append("Where SalesReceiptLine.RecNo =" + cashInvoiceKey + " Order By SalesReceiptLine.[LineNo]  ");
		return query.toString();
	}

	public String getNextRecordCashInvoice(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM salesreceipt  WHERE recNO >" + recNo
				+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno ");
		return query.toString();
	}

	public String getPreviousRecordCashInvoice(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM salesreceipt  WHERE recNO <" + recNo
				+ " ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno  desc");
		return query.toString();
	}

	public String getFirstRecordCashInvoice(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM salesreceipt ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno");
		return query.toString();
	}

	public String getLastRecordCashInvoice(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM salesreceipt");
		if (webUserID > 0 && !seeTrasction)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno desc");
		return query.toString();
	}

	public String getCreditInvoiceByID(int cashInvoiceKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from Invoice where recNo=" + cashInvoiceKey + "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String getCreditInvoiceGridDataByID(int cashInvoiceKey) {
		query = new StringBuffer();
		query.append("SELECT InvoiceLine.*,QBItems.FullName AS ItemName,QBItems.ArFullName as ItemNameAR,QBItems.ItemType,QBItems.ListID,QBItems.IncomeAccountRef, ");
		query.append("Class.FullName AS ClassName,Class.ArFullName As ClassNameAR,DeliveryInvoiced.DeliveryRecNo,DeliveryInvoiced.DeliveryLineNo as DeliveryLineNo, ");
		query.append("InventorySiteList.SiteName as InventorySite,InventorySiteList.ArName as InventorySiteAR FROM ((((InvoiceLine  ");
		query.append("LEFT JOIN Class ON InvoiceLine.ClassRefKey = Class.Class_Key) ");
		query.append("LEFT JOIN QBItems ON InvoiceLine.ItemRefKey = QBItems.Item_Key) ");
		query.append("LEFT JOIN InventorySiteList ON InventorySiteList.ItemKey = InvoiceLine.InventorySiteKey) ");
		query.append("LEFT JOIN DeliveryInvoiced  ON CAST(InvoiceLine.RecNo AS VARCHAR) + 'I'  = CAST(DeliveryInvoiced.RecNo AS VARCHAR) + DeliveryInvoiced.TxnType ");
		query.append("And InvoiceLine.[LineNo] = DeliveryInvoiced.[LineNo]) ");
		query.append("where InvoiceLine.RecNo=  "+ cashInvoiceKey);
		query.append(" Order By [LineNo] ");
		return query.toString();
	}

	public String getNextRecordCreditInvoice(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Invoice  WHERE recNO >" + recNo
				+ " and invoice_source='CMS' ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno ");
		return query.toString();
	}

	public String getPreviousRecordCreditInvoice(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Invoice  WHERE recNO <" + recNo
				+ " and invoice_source='CMS'");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno  desc ");
		return query.toString();
	}

	public String getFirstRecordCreditInvoice(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Invoice where invoice_source='CMS' ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno");
		return query.toString();
	}

	public String getLastRecordCreditInvoice(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Invoice  where invoice_source='CMS' ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and  webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno desc");
		return query.toString();
	}

	public String getLogoImage() {
		query = new StringBuffer();
		if (dbUser.getMergedDatabse().equalsIgnoreCase("Yes")) {
			query.append("Select logo1 from companySettings");
		} else {
			query.append("Select logo1 from COMPSETUP");
		}
		// query.append("Select logo1 from compsetup");
		return query.toString();
	}

	public String getBalanceSheetReoprt() {
		query = new StringBuffer();
		query.append("Select RPTQBFinancialSTD.*,Accounts.SubLevel,Accounts.AccountName As AccountNameORG,Accounts.AccountType,Accounts.Name,Accounts.DESCRIPTION From RPTQBFinancialSTD Left Join Accounts On RPTQBFinancialSTD.Act_Key=Accounts.Rec_No  Where Report_Name ='BALANCESHEETSTD' Order By Rec_No");
		return query.toString();
	}

	// HBA quotation querries

	// -------------------------------------------------------------------------------------------Quotation
	// Payment--------------------------------------------------------------------------------------------------

	public String quotationPrecpectiveList() {
		query = new StringBuffer();
		query.append("Select [Name],RecNo,SubLevel,FullName from [Prospective] Where IsActive='Y' And Status='A' order by FullName");
		return query.toString();
	}

	public String quotationCustomerList() {
		query = new StringBuffer();
		query.append("Select Name,RecNo,ListType,SubLevel,ListID,FullName from QbLists Where ListType in('Customer') and IsActive='Y' order by ListType,FullName");
		return query.toString();
	}

	public String saveQuotation(CashInvoiceModel obj, int webUserID) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Insert into QUOTATION (RecNo,TxnID,ClientType,CustomerRefKey,ClassRefKey,ARAccountRefKey,TemplateRefKey,TxnDate,RefNumber,BillAddress1,");
		query.append("BillAddress2,BillAddress3,BillAddress4,BillAddress5,BillAddressCity,BillAddressState,BillAddressPostalCode,BillAddressCountry,BillAddressNote,ShipAddress1,");
		query.append("ShipAddress2,ShipAddress3,ShipAddress4,ShipAddress5,ShipAddressCity,ShipAddressState,ShipAddressPostalCode,ShipAddressCountry,ShipAddressNote,");
		query.append("IsPending,PONumber,TermsRefKey,DueDate,SalesRefKey,FOB,ShipDate,ShipMethodRefKey,ItemSalesTaxRefKey,Memo,CustomerMsgRefKey,IsToBePrinted,IsToEmailed,IsTaxIncluded,");
		query.append("CustomerSalesTaxCodeRefKey,Other,Amount,SendViaReffKey,CustomField1,CustomField2,CustomField3,CustomField4,CustomField5,status,remindflag,reminddate,reminddays,attachment,DescriptionHIDE,QtyHIDE,RateHIDE,LetterTemplate,ShipToAddress,webUserID)");
		query.append(" Values(" + obj.getRecNo() + ",'" + obj.getTxnId()
				+ "','" + obj.getClientType() + "'," + obj.getCustomerRefKey()
				+ "," + obj.getClassRefKey() + "," + obj.getAccountRefKey()
				+ "," + obj.getTemplateRefKey() + ",");
		query.append(" '" + sdf.format(obj.getTxnDate()) + "','"
				+ obj.getRefNumber() + "' ,' " + obj.getBillAddress1()
				+ "' , '" + obj.getBillAddress2() + "' , '"
				+ obj.getBillAddress3() + "' , '" + obj.getBillAddress4()
				+ "' , '" + obj.getBillAddress5() + "' , ");
		query.append(" '" + obj.getBillAddressCity() + "','"
				+ obj.getBillAddressState() + "' , '"
				+ obj.getBillAddressPostalCode() + "' , '"
				+ obj.getBillAddressCountry() + "' , '"
				+ obj.getBillAddressNote() + "' , '" + obj.getShipAddress1()
				+ "' ,");
		query.append(" '" + obj.getShipAddress2() + "' , '"
				+ obj.getShipAddress3() + "' , '" + obj.getShipAddress4()
				+ "' , '" + obj.getShipAddress5() + "' , '"
				+ obj.getShipAddressCity() + "' , '"
				+ obj.getShipAddressState() + "' , '"
				+ obj.getShipAddressPostalCode() + "' , '"
				+ obj.getShipAddressCountry() + "' , '"
				+ obj.getShipAddressNote() + "' , ");
		query.append(" '" + obj.getIsPending() + "' , '" + obj.getPoNumber()
				+ "' ," + obj.getTermRefKey() + ", '"
				+ sdf.format(obj.getDueDate()) + "' , '" + obj.getSalesRefKey()
				+ "' , '" + obj.getfOB() + "' , '"
				+ sdf.format(obj.getShipDate()) + "' , '"
				+ obj.getShipMethodRefKey() + "' , '"
				+ obj.getItemSalesTaxRefKey() + "' , '" + obj.getMemo()
				+ "' , '" + obj.getCustomerMsgRefKey() + "' ,");
		query.append(" '" + obj.getIsToBePrinted() + "' , '"
				+ obj.getIsToEmailed() + "' , '" + obj.getIsTaxIncluded()
				+ "' , " + obj.getCustomerSalesTaxCodeRefKey() + ", '"
				+ obj.getOther() + "'," + obj.getAmount() + ","
				+ obj.getSendViaReffKey() + ", '" + obj.getCustomField1()
				+ "' ,");
		query.append(" '" + obj.getCustomField2() + "' , '"
				+ obj.getCustomField3() + "' , '" + obj.getCustomField4()
				+ "' , '" + obj.getCustomField5() + "','" + obj.getStatus()
				+ "','" + obj.getRemingMeFalg() + "'");
		if (obj.getRemindMeDate() != null)
			query.append(",'" + sdf.format(obj.getRemindMeDate()) + "' ");
		else
			query.append("," + obj.getRemindMeDate() + "");
		query.append(", " + obj.getRemindMedays() + ",'" + obj.getAttachemnet()
				+ "','" + obj.getDescriptionHIDE() + "' , '" + obj.getQtyHIDE()
				+ "' ,'" + obj.getRateHIDE() + "','" + obj.getLetterTemplate()
				+ "','" + obj.getShipToAddress() + "'," + webUserID);
		query.append(")");
		query.append(" ");
		return query.toString();

	}

	public String updateQuotation(CashInvoiceModel obj, int webUserID) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Update QUOTATION set TxnID='" + obj.getTxnId()
				+ "',ClientType='" + obj.getClientType() + "',CustomerRefKey="
				+ obj.getCustomerRefKey() + ",ClassRefKey="
				+ obj.getClassRefKey() + ",ARAccountRefKey="
				+ obj.getAccountRefKey() + ",TemplateRefKey="
				+ obj.getTemplateRefKey() + ",TxnDate='"
				+ sdf.format(obj.getTxnDate()) + "',RefNumber='"
				+ obj.getRefNumber() + "',BillAddress1='"
				+ obj.getBillAddress1() + "',");
		query.append("BillAddress2='" + obj.getBillAddress2()
				+ "',BillAddress3='" + obj.getBillAddress3()
				+ "',BillAddress4='" + obj.getBillAddress4()
				+ "',BillAddress5='" + obj.getBillAddress5()
				+ "',BillAddressCity='" + obj.getBillAddressCity()
				+ "',BillAddressState='" + obj.getBillAddressState()
				+ "',BillAddressPostalCode='" + obj.getBillAddressPostalCode()
				+ "',BillAddressCountry='" + obj.getBillAddressCountry() + "',");
		query.append("BillAddressNote='" + obj.getBillAddressNote()
				+ "',ShipAddress1='" + obj.getShipAddress1()
				+ "',ShipAddress2='" + obj.getShipAddress2()
				+ "',ShipAddress3='" + obj.getShipAddress3()
				+ "',ShipAddress4='" + obj.getShipAddress4()
				+ "',ShipAddress5='" + obj.getShipAddress5()
				+ "',ShipAddressCity='" + obj.getShipAddressCity()
				+ "',ShipAddressState='" + obj.getShipAddressState() + "',");
		query.append("ShipAddressPostalCode='" + obj.getShipAddressPostalCode()
				+ "',ShipAddressCountry='" + obj.getShipAddressCountry()
				+ "',ShipAddressNote='" + obj.getShipAddressNote()
				+ "',IsPending='" + obj.getIsPending() + "',PONumber='"
				+ obj.getPoNumber() + "',TermsRefKey=" + obj.getTermRefKey()
				//+ ",DueDate='" + sdf.format(obj.getDueDate())
				+ ",SalesRefKey=" + obj.getSalesRefKey() + ",FOB='"
				+ obj.getfOB() + "',");
		query.append("ShipDate='" + sdf.format(obj.getShipDate())
				+ "',ShipMethodRefKey=" + obj.getShipMethodRefKey()
				+ ",ItemSalesTaxRefKey=" + obj.getItemSalesTaxRefKey()
				+ ",Memo='" + obj.getMemo() + "',CustomerMsgRefKey="
				+ obj.getCustomerMsgRefKey() + ",IsToBePrinted='"
				+ obj.getIsToBePrinted() + "',IsToEmailed='"
				+ obj.getIsToEmailed() + "',IsTaxIncluded='"
				+ obj.getIsTaxIncluded() + "',");
		query.append("CustomerSalesTaxCodeRefKey="
				+ obj.getCustomerSalesTaxCodeRefKey() + ",Other='"
				+ obj.getOther() + "',Amount=" + obj.getAmount()
				+ ",SendViaReffKey='" + obj.getSendViaReffKey()
				+ "',CustomField1='" + obj.getCustomField1()
				+ "',CustomField2='" + obj.getCustomField2()
				+ "',CustomField3='" + obj.getCustomField3() + "',");
		query.append("CustomField4='" + obj.getCustomField4()
				+ "',CustomField5='" + obj.getCustomField5() + "',status='"
				+ obj.getStatus() + "',remindflag='" + obj.getRemingMeFalg()
				+ "'");
		if (obj.getRemindMeDate() != null)
			query.append(",reminddate='" + sdf.format(obj.getRemindMeDate())
					+ "',");
		else
			query.append(",reminddate=" + obj.getRemindMeDate() + ",");
		
		if (obj.getDueDate() != null)
			query.append("DueDate='" + sdf.format(obj.getDueDate())+ "',");
		else
			query.append("DueDate=" + obj.getDueDate() + ",");
		
		
		query.append("reminddays=" + obj.getRemindMedays() + ",attachment='"
				+ obj.getAttachemnet() + "',DescriptionHIDE='"
				+ obj.getDescriptionHIDE() + "',QtyHIDE='" + obj.getQtyHIDE()
				+ "',RateHIDE='" + obj.getRateHIDE() + "',LetterTemplate='"
				+ obj.getLetterTemplate() + "',ShipToAddress='"
				+ obj.getShipToAddress() + "',webUserID=" + webUserID
				+ "  where recNO=" + obj.getRecNo());
		return query.toString();

	}

	public String addNewQuotationGridItemsQuery(CashInvoiceGridData obj,
			int RecNo) {
		query = new StringBuffer();
		String desc = "";
		if (obj.getInvoiceDescription() != null) {
			desc = obj.getInvoiceDescription().replace("'", "`");
		}
		query.append(" Insert into QuotationLine (RecNo,[LineNo],ItemRefKey,Description,Quantity,Rate,AvgCost,RatePercent,PriceLevelRefKey,ClassRefKey,Amount,ServiceDate,SalesTaxCodeRefKey,IsTaxable,OverrideItemAccountRefKey,Other1,Other2,InventorySiteKey)");
		query.append(" values(" + RecNo + ", '" + obj.getLineNo() + "' , '"
				+ obj.getSelectedItems().getRecNo() + "', '" + desc + "',"
				+ obj.getInvoiceQty() + " , '" + obj.getInvoiceRate() + "' , '"
				+ obj.getAvgCost() + "' , '" + obj.getRatePercent() + "' , '"
				+ obj.getPriceLevelRefKey() + "', ");
		if (obj.getSelectedInvcCutomerGridInvrtyClassNew() != null)
			query.append(obj.getSelectedInvcCutomerGridInvrtyClassNew()
					.getRecNo());
		else
			query.append("0");
		query.append("," + obj.getInvoiceAmmount() + ", '"
				+ sdf.format(obj.getServiceDate()) + "' , '"
				+ obj.getSalesTaxCodeRefKey() + "' , '" + obj.getIsTaxable()
				+ "' , '" + obj.getOverrideItemAccountRefKey() + "' , '"
				+ obj.getOther1() + "' , '" + obj.getOther2() + "',");
		if (obj.getSelectedInvcCutomerGridInvrtySiteNew() != null) {
			query.append(""
					+ obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo());
			query.append("");
			query.append(" )");
		} else
			query.append("0)");
		query.append(" ");

		return query.toString();
	}

	public String deleteQuotationGridItems(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from QuotationLine  Where RecNo=" + RecNo);
		return query.toString();
	}

	public String getQuatationByID(int cashInvoiceKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from QUOTATION where recNo=" + cashInvoiceKey
				+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String getQuatationGridDataByID(int quotationKey) {
		query = new StringBuffer();
		query.append("SELECT QuotationLine.*, QBItems.FullName  AS ItemName,QBItems.[ArFullName] as ItemNameAR,QBItems.ItemType , QBItems.IncomeAccountRef , ");
		query.append("Class.FullName AS ClassName, Class.[ArFullName] as ItemClassAR,InventorySiteList.SiteName as InventorySite,InventorySiteList.ArName as InventorySiteAR, ");
		query.append("QuotationDelivery.TxnType As DeliverdAs,QuotationDelivery.RecNo As DeliverRecNo ");
		query.append("FROM QuotationLine LEFT JOIN Class ON QuotationLine.ClassRefKey  = Class.Class_Key LEFT JOIN QBItems ON QuotationLine.ItemRefKey = QBItems.Item_Key  ");
		query.append("LEFT JOIN QuotationDelivery ON QuotationDelivery.QuotationRecNo = QuotationLine.RecNo And QuotationDelivery.QuotationLineNo = QuotationLine.[LineNo] ");
		query.append("LEFT JOIN InventorySiteList ON InventorySiteList.ItemKey=QuotationLine.InventorySiteKey  ");
		query.append("Where QuotationLine.RecNo ="+quotationKey+" Order By [LineNo] ");
		return query.toString();
	}

	public String getNextRecordQuatation(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM QUOTATION  WHERE recNO >" + recNo
				+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno ");
		return query.toString();
	}

	public String getPreviousRecordQuatation(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM QUOTATION  WHERE recNO <" + recNo
				+ " ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno  desc ");
		return query.toString();
	}

	public String getFirstRecordQuatation(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM QUOTATION ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno");
		return query.toString();
	}

	public String getLastRecordQuatation(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM QUOTATION");
		if (webUserID > 0 && !seeTrasction)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno desc");
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicateForQuotation(
			String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from QUOTATION Where RefNumber='"
				+ serialNumber + "' and recno !=" + recNO);
		return query.toString();
	}

	public String GetNewQuotationRecNo() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from QUOTATION");
		return query.toString();
	}

	public String getQuotationReport(int webUserID,boolean seeTrasction, Date fromDate, Date toDate) {
		query = new StringBuffer();
		query.append("SELECT Quotation.*,Prospective.FullName As ProspectiveName,EntityList.FullName AS Entity,SendViaList.DESCRIPTION AS SendVia "
				+ "FROM ((((Quotation LEFT JOIN QBLists AS EntityList ON Quotation.CustomerRefKey = EntityList.RecNo) "
				+ "LEFT JOIN Prospective ON Quotation.CustomerRefKey = Prospective.RecNo) "
				+ "LEFT JOIN HRListValues AS SendViaList ON Quotation.SendViaReffKey = SendViaList.ID)) "
				+ " Where  Quotation.Status not in ('V')");
		query.append("and Quotation.TxnDate Between '" + sdf.format(fromDate)+ "' And '" + sdf.format(toDate) + "'");
		if (webUserID > 0 && !seeTrasction) {
			query.append("and Quotation.webUserID=" + webUserID);
		}
		query.append(" Order by Quotation.TxnDate Desc,RefNumber Desc, Quotation.RecNo");
		return query.toString();
	}

	// Methods for item Receipt

	// -------------------------------------------------------------------------------------------Item
	// Receipt--------------------------------------------------------------------------------------------------
	public String getAccountForExpances() {
		query = new StringBuffer();
		query.append("select Accounts.AccountName As[AccountName],Accounts.name,AccountType,SubLevel,Rec_No,ListID from Accounts inner join accountType on accountType.Typename = Accounts.AccountType where accounttype NOT IN ('AccountsPayable','AccountsRecievable')  and isactive='Y' order by AccountType.SRL_NO,Accounts.ACTLEVELSwithNo");
		return query.toString();
	}

	public String addNewItemReceipt(ItemReceiptModel obj) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		query = new StringBuffer();
		query.append("Insert into IRMAST (RecNo,TxnID,IRNo,IRNoLocal,IRDate,APAccountKey,VendorKey,Amount,Status,[Memo],");
		query.append("QBStatus,ItemClassHide,ItemDesHide,ItemBillNoHide,ItemBillDateHide,PrintName,IRSource,webUserId,transformPO)");
		query.append(" Values(" + obj.getRecNo() + ",'" + obj.getTxnID()
				+ "','" + obj.getIrNo() + "','" + obj.getIrNoLocal() + "',");
		query.append(" '" + sdf.format(obj.getIrDate()) + "',"
				+ obj.getAccrefKey() + "," + obj.getVendorKey() + ","
				+ obj.getAmount() + ",");
		query.append(" '" + obj.getStatus() + "','" + obj.getMemo() + "',");
		query.append(" '" + obj.getQbStatus() + "','" + obj.getItemClassHide()
				+ "','" + obj.getItemDesHide() + "','" + obj.getBillNoHide()
				+ "','" + obj.getBillDateHide() + "',");
		query.append(" '" + obj.getPrintName() + "','" + obj.getIrsource()
				+ "'," + obj.getWebUserId()+ ",'" + obj.getTransformPO()+"'");
		query.append(" )");
		query.append(" ");
		return query.toString();
	}

	public String updateExistingItemReceipt(ItemReceiptModel obj) {
		// DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String editedFromOnline = "Y";
		query = new StringBuffer();
		query.append("Update IRMAST set TxnID='" + obj.getTxnID() + "',IRNo='"
				+ obj.getIrNo() + "',IRNoLocal='" + obj.getIrNoLocal()
				+ "',IRDate='" + sdf.format(obj.getIrDate())
				+ "',APAccountKey=" + obj.getAccrefKey() + ",VendorKey="
				+ obj.getVendorKey() + ",Amount=" + obj.getAmount()
				+ ",Status='" + obj.getStatus() + "',[Memo]='" + obj.getMemo()
				+ "',");
		query.append("QBStatus='" + obj.getQbStatus() + "',ItemClassHide='"
				+ obj.getItemClassHide() + "',ItemDesHide='"
				+ obj.getItemDesHide() + "',ItemBillNoHide='"
				+ obj.getBillNoHide() + "',ItemBillDateHide='"
				+ obj.getBillDateHide() + "',PrintName='" + obj.getPrintName()
				+ "',IRSource='" + obj.getIrsource() + "',webUserId="
				+ obj.getWebUserId() + ",editedFromOnline='" + editedFromOnline
				+ "' where recNo=" + obj.getRecNo() + "");
		query.append(" ");
		return query.toString();
	}

	public String addExpenseItemReceipt(ExpensesModel objExpenses, int RecNo) {
		query = new StringBuffer();
		String memo = "";
		if (objExpenses.getMemo() != null) {
			memo = objExpenses.getMemo().replace("'", "`");
		}
		query.append(" Insert into IREXPENSES (Rec_No,Accountsref_Key,Amount,[Memo],Custref_Key,Class_Key,[Line_No]) ");
		query.append(" values(" + RecNo + ", "
				+ objExpenses.getSelectedAccount().getRec_No() + ", "
				+ objExpenses.getAmount() + ", '" + memo + "',");
		if (objExpenses.getSelectedCustomer() != null)
			query.append(objExpenses.getSelectedCustomer().getRecNo());
		else
			query.append("0");
		if (objExpenses.getSelectedClass() != null)
			query.append(", " + objExpenses.getSelectedClass().getClass_Key());
		else
			query.append(", 0");
		query.append(", " + objExpenses.getSrNO());
		query.append(" )");
		return query.toString();
	}

	public String deleteExpenseItemReceipt(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from IREXPENSES Where Rec_No=" + RecNo);
		return query.toString();
	}

	public String deleteCheckItemsItemReceipt(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from IRITEMS Where RecNo=" + RecNo);
		return query.toString();
	}

	public String addCheckItemsItemReceipt(CheckItemsModel obj, int RecNo) {

		query = new StringBuffer();
		String desc = "";
		if (obj.getDescription() != null) {
			desc = obj.getDescription().replace("'", "`");
		}
		query.append(" Insert into IRITEMS (RecNo, ItemKey,Description,Quantity,Cost,Amount,CustKey,ClassKey,[LineNo],InvoiceDate,InventorySiteKey,BillNo");
		if(obj.getpORecNo()>0){
			query.append(",PORecNo ,POLineNo ");
		}
		query.append(" )values(" + RecNo + ", "
				+ obj.getSelectedItems().getRecNo() + ", '" + desc + "', "
				+ obj.getQuantity() + ", ");
		query.append(" " + obj.getCost() + ", " + obj.getAmount() + ", ");
		if (obj.getSelectedCustomer() != null)
			query.append(obj.getSelectedCustomer().getRecNo());
		else
			query.append("0");
		if (obj.getSelectedClass() != null)
			query.append(", " + obj.getSelectedClass().getClass_Key());
		else
			query.append(", 0");
		query.append(", " + obj.getLineNo());
		if (obj.getInvoiceDate() != null)
			query.append(", '" + sdf.format(obj.getInvoiceDate()) + "'");
		else
			query.append(", NULL");
		query.append(" ,");
		if (null != obj.getSelectedInvcCutomerGridInvrtySiteNew()
				&& obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo() > 0)
			query.append(""
					+ obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()
					+ "");
		else
			query.append("0");
		String billNo = obj.getBillNo() == null ? "" : obj.getBillNo();
		query.append(", '" + billNo + "'");
		if(obj.getpORecNo()>0){
			query.append(","+obj.getpORecNo()+","+obj.getpOLineNo());
		}
		query.append(")");
		return query.toString();
	}

	public String getItemReceiptByID(int itemReceiptKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from IRMAST where recNo=" + itemReceiptKey + "");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String getExpenseItemReceiptGridDataByID(String itemReceiptKey) {
		query = new StringBuffer();
		query.append("select * from IREXPENSES where rec_No in(" + itemReceiptKey	+ ")  order by 'Line_NO'");
		return query.toString();
	}

	public String getCheckItemsItemReceiptGridDataByID(int itemReceiptKey) {
		query = new StringBuffer();
		query.append("select * from IRITEMS where recNo=" + itemReceiptKey
				+ "  order by 'LineNO'");
		return query.toString();
	}

	public String getNextRecordItemReceipt(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM IRMAST  WHERE recNO >" + recNo + "");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno ");
		return query.toString();
	}

	public String getPreviousRecordItemReceipt(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM IRMAST  WHERE recNO <" + recNo + " ");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno  desc ");
		return query.toString();
	}

	public String getFirstRecordItemReceipt(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM IRMAST ");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno");
		return query.toString();
	}

	public String getLastRecordItemReceipt(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM IRMAST");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno desc");
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicateForItemReceiptIrNo(
			String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from IRMAST Where IRNo='" + serialNumber + "' and recno !=" + recNO);
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicateForItemReceiptIrNumberLocal(
			String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from IRMAST Where IRNoLocal='" + serialNumber+ "' and recno !=" + recNO);
		return query.toString();
	}

	public String GetNewItemReceiptRecNo() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from IRMAST");
		return query.toString();
	}

	/*public String getItemReceiptReport(int webUserId,boolean seeTrasction, Date fromDate, Date toDate) {
		query = new StringBuffer();
		query.append(" SELECT IRMast.amount,IRMast.IRNoLocal, IRMast.IRDate,IRMast.RecNo as MastRecNo,IRMast.status,Accounts.AccountNumber, Accounts.Name AS AccountName, ");
		query.append("QBListsVendor.Name AS Vendor, QBItems.Name AS ItemName,IRItems.Description, IRItems.Quantity,IRItems.[LineNo] As IRLineNo, ");
		query.append("IRMast.Memo as MainMemo, QBListsCustomer.Name AS Customer, Class.Name AS ClassName ");
		query.append("FROM (((((IRMast  LEFT JOIN IRItems  ON IRMast.RecNo  = IRItems.RecNo) ");
		query.append("LEFT JOIN Accounts  ON IRMast.APAccountKey    = Accounts.REC_NO) LEFT JOIN QBLists AS QBListsVendor    ON IRMast.VendorKey  = QBListsVendor.RecNo) ");
		query.append("LEFT JOIN QBItems  ON IRItems.ItemKey = QBItems.Item_Key) LEFT JOIN QBLists AS QBListsCustomer ON IRItems.CustKey             = QBListsCustomer.RecNo) ");
		query.append("LEFT JOIN Class ON IRItems.ClassKey              = Class.Class_Key  WHERE IRMast.RecNo <>0 ");
		query.append("and IRMast.IRDate Between '" + sdf.format(fromDate)
				+ "' And '" + sdf.format(toDate) + "' ");
		if (webUserId > 0 && !seeTrasction)
			query.append(" and IRMast.webUserId=" + webUserId + "");
		query.append("ORDER BY IRMast.RecNo Desc,IRMast.IRNoLocal , IRMast.IRDate ,IRItems.[LineNo] ");
		return query.toString();
	}*/
	
	public String getItemReceiptReport(int webUserId,boolean seeTrasction, Date fromDate, Date toDate) {
		query = new StringBuffer();
		query.append(" SELECT IRMast.*,Accounts.Name AS AccountName,QBListsVendor.Name AS Vendor ");
		query.append("from (IRMast LEFT JOIN Accounts ON IRMast.APAccountKey = Accounts.REC_NO) ");
		query.append("LEFT JOIN QBLists AS QBListsVendor ON IRMast.VendorKey  = QBListsVendor.RecNo ");
		query.append(" WHERE IRMast.RecNo <>0 ");
		query.append("and IRMast.IRDate Between '" + sdf.format(fromDate)
				+ "' And '" + sdf.format(toDate) + "' ");
		if (webUserId > 0 && !seeTrasction)
			query.append(" and IRMast.webUserId=" + webUserId + "");
		query.append("ORDER BY IRMast.RecNo Desc,IRMast.IRNoLocal , IRMast.IRDate  ");
		return query.toString();
	}

	// PURCHASE REQUEST CHANGES FROM IQBAL

	// -------------------------------------------------------------------------------------------Purchase
	// Request--------------------------------------------------------------------------------------------------

	public String getDropShipTo() {
		query = new StringBuffer();
		query.append("Select Name,RecNo,ListID,ListType,SubLevel,FullName from QbLists  Where IsActive='Y' order by FullName,Name, ListType, Sublevel, Recno, ListID");
		return query.toString();
	}

	public String getItemForPurchaseRequest() {
		query = new StringBuffer();
		query.append("SELECT name,item_key,FullName,ItemType,SubLevel,ListID,salesdesc, PurchaseDesc,averageCost,ClassKey,0 as subItemsCount "
				+ "FROM QBItems   where (PricePercent=0 or PricePercent is Null) and IsActive='Y'  order by ItemType desc,FullName, Name,SubLevel,Item_Key,ListID ");

		//use this instead to get if item has subitem to not go to database when select item
		/*query.append("SELECT name,item_key,FullName,ItemType,SubLevel,ListID,salesdesc ,   PurchaseDesc,averageCost,ClassKey, ");
		query.append(" (select count(*) from QBItems as c Where (PricePercent=0 or PricePercent is Null) and IsActive='Y' and CharIndex( QBItems.Name +':' ,FullName)>0 ) as 'subItemsCount'");
		query.append(" FROM QBItems ");
		query.append("  where (PricePercent=0 or PricePercent is Null) and IsActive='Y' ");
		query.append("  order by ItemType desc,FullName, Name,SubLevel,Item_Key,ListID ");*/

		return query.toString();
	}

	public String addNewPurchaseRqeuest(PurchaseRequestModel obj) {
		query = new StringBuffer();
		query.append("Insert into PurchaseRequest (Rec_No,RefNumber,TxnDate, VendorRefKey, Address, EntityRefKey, ShipTo, ClassRefKey, TotalAmount, [Memo], Status,Source,reqdate,IsFullyReceived,webUserId) Values(" 
				+ obj.getRecNo() + ",'" 
				+ obj.getRefNUmber()+ "','" 
				+ sdf.format(obj.getTxtnDate()) + "',"
				+ obj.getVendorRefKEy() + ",'" 
				+ obj.getAdress() + "'," 
				+ obj.getEntityRefKey()	+ ",'" 
				+ obj.getShipTo() + "'," 
				+ obj.getClassRefkey() + "," 
				+ obj.getAmount() + ",'" 
				+ obj.getMemo() + "','" 
				+ obj.getStatus() + "','" 
				+ obj.getSource() + "','" 
				+sdf.format(obj.getReqDate())+ "','N',"
				+ obj.getWebUserId()
				);
		query.append(")");
		return query.toString();
	}

	public String updateExistingPurchaseRequest(PurchaseRequestModel obj) {
		query = new StringBuffer();
		query.append("Update PurchaseRequest set RefNumber='"+ obj.getRefNUmber() 
				+ "',TxnDate='"+ sdf.format(obj.getTxtnDate()) 
				+ "',reqDate='"+ sdf.format(obj.getReqDate()) 
				+ "',VendorRefKey="+ obj.getVendorRefKEy() 
				+ ",Address='" + obj.getAdress()
				+ "',IsFullyReceived='" + obj.getIsFullyReceived()
				+ "',EntityRefKey=" + obj.getEntityRefKey() 
				+ ",ShipTo='"+ obj.getShipTo() 
				+ "',TotalAmount=" + obj.getAmount()
				+  ",[Memo]='" + obj.getMemo()
				+ "',");
		query.append("ClassRefKey=" + obj.getClassRefkey() + ",Source='"
				+ obj.getSource() + "',webUserId=" + obj.getWebUserId()
				+ " where Rec_No=" + obj.getRecNo() + "");
		query.append(" ");
		return query.toString();
	}

	public String addGridDataPurchaseRequest(PurchaseRequestGridData obj,int RecNo) {
		String desc="";
		if (obj.getSelctedCustomer() == null) {
			obj.setEntityRefKey(0);
		} else {
			obj.setEntityRefKey(obj.getSelctedCustomer().getRecNo());
		}
		if(obj.getDecription()!=null){
			desc=obj.getDecription().replaceAll("'", "`");
		}
		query = new StringBuffer();
		query.append(" Insert into purchaseREquestLIne (Rec_No,ItemRefKey,Description,Quantity,Rate,Amount,EntityRefKey,Line_No,IsOrdered,RcvdQuantity)");
		query.append(" values(" + RecNo + ","
				+ obj.getSelectedItem().getRecNo() + ",'" + desc
				+ "'," + obj.getQuantity() + "," + obj.getRate() + ","
				+ obj.getAmount() + "," + obj.getEntityRefKey() + ","
				+ obj.getLineNo() + ",'" + obj.getIsOrderd() + "',"
				+ obj.getRecivedQuantity() + "");
		query.append(" )");
		return query.toString();
	}

	public String deleteGridDataPurchaseRequest(int RecNo) {
		query = new StringBuffer();
		query.append("Delete from purchaseREquestLIne Where Rec_No=" + RecNo);
		return query.toString();
	}

	public String getPurchaseRequestByID(int purchaseRequestKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from PurchaseRequest where rec_no="
				+ purchaseRequestKey + "");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String getGridDataPurchaseRequestById(int purchaseRequestKey) {
		query = new StringBuffer();
		query.append("select * from purchaseREquestLIne where rec_No="
				+ purchaseRequestKey + "  order by 'Line_NO'");
		return query.toString();
	}

	public String getNextRecordPurchaseRequest(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseRequest  WHERE rec_no >"
				+ recNo + "");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no ");
		return query.toString();
	}

	public String getPreviousRecordPurchaseRequest(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseRequest  WHERE rec_no <"
				+ recNo + " ");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no  desc ");
		return query.toString();
	}

	public String getFirstRecordPurchaseRequest(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseRequest ");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no");
		return query.toString();
	}

	public String getLastRecordPurchaseRequest(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseRequest");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no desc");
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicateForPurchaseRequest(
			String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from PurchaseRequest Where RefNumber="
				+ serialNumber + " and rec_no !=" + recNO);
		return query.toString();
	}

	public String GetNewPurchaseRequestRecNo() {
		query = new StringBuffer();
		query.append(" SELECT max(rec_no) as MaxRecNo from PurchaseRequest");
		return query.toString();
	}

	public String getPurchaseRequestReport(int webUserId,boolean seeTrasction, Date fromDate,
			Date toDate, String reportFrom, String status) {
		query = new StringBuffer();
		query.append("SELECT distinct PurchaseRequest.*, VENDOR.FULLNAME as VendorName,QBLists.FullName As DropShipto,Class.Name As ClassName, ");
		query.append("QBitems.Name as ItemName,Customerlists.FullName As CustomerName, PurchaseRequestLine.Amount,PurchaseRequestLine.rate, ");
		query.append("PurchaseRequestLine.Quantity,PurchaseRequestLine.RcvdQuantity,PurchaseRequestLine.Description AS itemDesc ");
		query.append(" FROM PurchaseRequest  INNER JOIN PurchaseRequestLine ON PurchaseRequest.Rec_No  = PurchaseRequestLine.Rec_No ");
		query.append(" LEFT  JOIN Vendor ON PurchaseRequest.VendorRefKey         = Vendor.Vend_Key ");
		query.append("LEFT  JOIN QBLists ON PurchaseRequest.EntityRefKey           = QBLists.RecNo ");
		query.append("LEFT  JOIN Class ON PurchaseRequest.ClassRefKey             = Class.Class_Key ");
		query.append("LEFT  JOIN QBLists As Customerlists   ON PurchaseRequestLine.EntityRefKey    = Customerlists.RecNo ");
		query.append("LEFT  JOIN QBItems ON PurchaseRequestLine.ItemRefKey      = QBItems.Item_Key where");
		if (!reportFrom.equalsIgnoreCase(""))
			query.append("  Source = '" + reportFrom + "' and ");
		if (!status.equalsIgnoreCase(""))
			query.append("  PurchaseRequest.status = '" + status + "' and ");
		query.append(" PurchaseRequest.TXNDate between '"
				+ sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "'");
		if (webUserId > 0 && !seeTrasction)
			query.append(" and PurchaseRequest.webUserId=" + webUserId + "");
		query.append(" order by PurchaseRequest.Rec_No");
		return query.toString();
	}

	/*-------------------------------------------------------------------------------------------Purchase Order--------------------------------------------------------------------------------------------------*/
	public String addNewPurchaseOrder(PurchaseRequestModel obj) {
		query = new StringBuffer();
		query.append("Insert into PurchaseOrder (Rec_No,RefNumber,TxnDate, VendorRefKey, Address, EntityRefKey, ShipTo, ClassRefKey, TotalAmount, [Memo], Status,Source,webUserId,TransformMR,isfullyreceived)");
		query.append(" Values(" + obj.getRecNo() 
				+ ",'" + obj.getRefNUmber()
				+ "','" + sdf.format(obj.getTxtnDate()) 
				+ "',"+ obj.getVendorRefKEy() 
				+ ",'" + obj.getAdress() 
				+ "'," + obj.getEntityRefKey()
				+ ",'" + obj.getShipTo() 
				+ "'," + obj.getClassRefkey() 
				+ "," + obj.getAmount() 
				+ ",'" + obj.getMemo() 
				+ "','" + obj.getStatus() 
				+ "','" + obj.getSource() 
				+ "'," + obj.getWebUserId()
				+",'"+obj.getTransformMR()+
				"','"+obj.getIsFullyReceived()+"'");		
		query.append(" )");
		return query.toString();
	}

	public String updateExistingPurchaseOrder(PurchaseRequestModel obj) {
		query = new StringBuffer();
		query.append("Update PurchaseOrder set RefNumber='"	+ obj.getRefNUmber() 
				+ "',TxnDate='"	+ sdf.format(obj.getTxtnDate()) 
				+ "',VendorRefKey="	+ obj.getVendorRefKEy() 
				+ ",Address='" + obj.getAdress()
				+ "',EntityRefKey=" + obj.getEntityRefKey() 
				+ ",ShipTo='" + obj.getShipTo() 
				+ "',TotalAmount=" + obj.getAmount()
				+ ",Status='" + obj.getStatus() 
				+ "',[Memo]='" + obj.getMemo()
				+ "',ClassRefKey=" + obj.getClassRefkey() 
				+ ",Source='" + obj.getSource() 
				+ "',webUserId=" + obj.getWebUserId()
				+ "',isfullyreceived=" + obj.getIsFullyReceived()
				+ " where Rec_No=" + obj.getRecNo() );
		return query.toString();
	}

	public String addGridDataPurchaseOrder(PurchaseRequestGridData obj,
			int RecNo) {
		if (obj.getSelctedCustomer() == null) {
			obj.setEntityRefKey(0);
		} else {
			obj.setEntityRefKey(obj.getSelctedCustomer().getRecNo());
		}
		query = new StringBuffer();
		query.append(" Insert into PurchaseOrderLine (Rec_No,ItemRefKey,Description,Quantity,Rate,Amount,EntityRefKey,Line_No,RcvdQuantity)");
		query.append(" values(" + RecNo + ","
				+ obj.getSelectedItem().getRecNo() + ",'" + obj.getDecription()
				+ "'," + obj.getQuantity() + "," + obj.getRate() + ","
				+ obj.getAmount() + "," + obj.getEntityRefKey() + ","
				+ obj.getLineNo() + "," + obj.getRecivedQuantity() + "");
		query.append(" )");
		return query.toString();
	}

	public String deleteGridDataPurchaseOrder(int RecNo) {
		query = new StringBuffer();
		query.append("Delete from PurchaseOrderLine Where Rec_No=" + RecNo);
		return query.toString();
	}

	public String getPurchaseOrderByID(int purchaseRequestKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from PurchaseOrder where rec_no="
				+ purchaseRequestKey + "");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String getGridDataPurchaseOrderById(int purchaseRequestKey) {
		query = new StringBuffer();
		query.append("select * from PurchaseOrderLine where rec_No="
				+ purchaseRequestKey + "  order by 'Line_NO'");
		return query.toString();
	}

	public String getNextRecordPurchaseOrder(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseOrder  WHERE rec_no >"
				+ recNo + "");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no ");
		return query.toString();
	}

	public String getPreviousRecordPurchaseOrder(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseOrder  WHERE rec_no <"
				+ recNo + " ");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no  desc");
		return query.toString();
	}

	public String getFirstRecordPurchaseOrder(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseOrder ");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no");
		return query.toString();
	}

	public String getLastRecordPurchaseOrder(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM PurchaseOrder");
		if (webUserID > 0 && !seeTrasction) 
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no desc");
		return query.toString();
	}

	public String checkIfSerialNumberIsDuplicateForPurchaseOrder(
			String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from PurchaseOrder Where RefNumber="
				+ serialNumber + " and rec_no !=" + recNO);
		return query.toString();
	}

	public String GetNewPurchaseOrderRecNo() {
		query = new StringBuffer();
		query.append(" SELECT max(rec_no) as MaxRecNo from PurchaseOrder");
		return query.toString();
	}

	public String getPurchaseOrderReport(int webUserId,boolean seeTrasction, Date fromDate,
			Date toDate, String reportFrom, String status) {
		query = new StringBuffer();
		query.append("SELECT distinct PurchaseOrder.*, VENDOR.FULLNAME as VendorName,QBLists.FullName As DropShipto,Class.Name As ClassName, ");
		query.append("QBitems.Name as ItemName,Customerlists.FullName As CustomerName, PurchaseOrderLine.Amount,PurchaseOrderLine.rate, ");
		query.append("PurchaseOrderLine.Quantity,PurchaseOrderLine.RcvdQuantity,PurchaseOrderLine.Description AS itemDesc ");
		query.append(" FROM PurchaseOrder  INNER JOIN PurchaseOrderLine ON PurchaseOrder.Rec_No  = PurchaseOrderLine.Rec_No ");
		query.append(" LEFT  JOIN Vendor ON PurchaseOrder.VendorRefKey         = Vendor.Vend_Key ");
		query.append("LEFT  JOIN QBLists ON PurchaseOrder.EntityRefKey           = QBLists.RecNo ");
		query.append("LEFT  JOIN Class ON PurchaseOrder.ClassRefKey             = Class.Class_Key ");
		query.append("LEFT  JOIN QBLists As Customerlists   ON PurchaseOrderLine.EntityRefKey    = Customerlists.RecNo ");
		query.append("LEFT  JOIN QBItems ON PurchaseOrderLine.ItemRefKey      = QBItems.Item_Key where");
		if (!reportFrom.equalsIgnoreCase(""))
			query.append("  Source = '" + reportFrom + "' and ");
		if (!status.equalsIgnoreCase(""))
			query.append("  PurchaseOrder.status = '" + status + "' and ");
		query.append(" PurchaseOrder.TXNDate between '" + sdf.format(fromDate)
				+ "' And '" + sdf.format(toDate) + "'");
		if (webUserId > 0 && !seeTrasction)
			query.append(" and PurchaseOrder.webUserId=" + webUserId + "");
		query.append(" order by PurchaseOrder.Rec_No");
		return query.toString();
	}

	// -------------------------------------------------------------------------------------------Cash
	// Payment--------------------------------------------------------------------------------------------------
	public String getCashPaymentReportQuery(Date fromDate, Date toDate,int webUserID) 
	{

		query = new StringBuffer();
		query.append("SELECT * from Checkmast");				
		query.append(" where PVDate Between '"
				+ sdf.format(fromDate)
				+ "' And '"
				+ sdf.format(toDate)
				+ "' and Cheque='Cash'");
		if (webUserID > 0)
			query.append(" and webUserId=" + webUserID + "");
		query.append(" Order by RecNo Desc, PVDate");
		return query.toString();
	}
	

	public String getCashPaymentById(int cashPaymenId, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from Checkmast where recno=" + cashPaymenId + "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}
	/*-----------------------------------------------------*/
	public String getNextRecordCashPayment(int recNo, int webUserID,boolean seeTrasction,String type) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Checkmast where cheque='"+ type+"' and RecNo >" + recNo
				+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno ");
		return query.toString();
	}

	public String getPreviousRecordPayment(int recNo, int webUserID,boolean seeTrasction,String type) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Checkmast where cheque='"+ type+"' and recNO <" + recNo );
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno  desc");
		return query.toString();
	}

	public String getFirstRecordCashPayment(int webUserID,boolean seeTrasction,String type) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Checkmast where cheque='"+ type+"'");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno");
		return query.toString();
	}

	public String getLastRecordCashPayment(int webUserID,boolean seeTrasction,String type) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Checkmast where cheque='"+ type+"'");

		if (webUserID > 0 && !seeTrasction)
			query.append(" and  webuserid=" + webUserID );
		query.append(" ORDER BY recno desc");

		return query.toString();
	}

	public String getCashPaymentGridDataExpenseById(int cashPaymentKey) {
		query = new StringBuffer();
		query.append("select * from CheckExpense where recNo=" + cashPaymentKey
				+ "");
		return query.toString();
	}

	public String getCashPaymentGridDataItemById(int cashPaymentKey) {
		query = new StringBuffer();
		query.append("select * from CheckItems where recNo=" + cashPaymentKey
				+ "");
		return query.toString();
	}

	public String getCashPaymentGridDataFAById(int cashPaymentKey) {
		query = new StringBuffer();
		query.append("select * from CheckFAItems where recNo=" + cashPaymentKey
				+ "");
		return query.toString();
	}

	public String saveCustomerStatusHistroyfromFeedback(CustomerStatusHistoryModel obj,int webUserId,String webUserName) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		String desc = "";
		if (obj.getStatusDescription() != null) {
			desc = obj.getStatusDescription().replace("'", "`");
		}
		obj.setStatusDescription(desc);
		int userId = 0;
		int statusId = 0;

		query = new StringBuffer();
		query.append(" Insert into CustomerStatusHistory(recNo,CustKey,StatusDescription,ActionDate,type,CreatedFrom,TxnRefNo,TxnRecNo,UserID,StatusID,WebUserID,WebUserName)");
		query.append(" values(" + obj.getRecNo() + ",'" + obj.getCustKey()
				+ "','" + obj.getStatusDescription() + "','"
				+ sdf1.format(obj.getActionDate()) + "','" + obj.getType()
				+ "','" + obj.getCreatedFrom() + "','" + obj.getTxnRefNumber()
				+ "'," + obj.getTxnRecNo() + "," + userId + "," + statusId
				+ ","+webUserId+",'"+webUserName+"')");
		return query.toString();
	}

	public String updateCustomerStatusDescription(CustomerStatusHistoryModel obj) {
		String desc = "";
		if (obj.getStatusDescription() != null) {
			desc = obj.getStatusDescription().replace("'", "`");
		}
		obj.setStatusDescription(desc);
		query = new StringBuffer();
		query.append(" update customer set statusDesc='"
				+ obj.getStatusDescription() + "' where cust_key="
				+ obj.getCustKey());
		return query.toString();
	}

	public String updateBarcode(int itemId, String barcode) {
		query = new StringBuffer();
		query.append("Update QBItems set BARCODE = '" + barcode
				+ "' Where Item_Key = " + itemId + "");
		return query.toString();
	}

	public String GetLocalItemByRef(int itemTypeRef) {
		query = new StringBuffer();
		query.append("Select * from LocalItem where ItemTypeRef="+itemTypeRef);
		return query.toString();
	}

	public String getPaymentMethod() {
		query = new StringBuffer();
		query.append("select Name,Rec_No from paymentmethod");
		return query.toString();
	}

	@SuppressWarnings("static-access")
	public String addUserActivityQuery(int Recno,String ACTIVITY,int activityRecNo,String memo,String refNumber,Date txDate,String WebUserName,int WebUserID,int action)
	{
		query=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat tdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();	
		String today=sdf.format(c.getTime());
		String txD=sdf.format(txDate);
		String totime=tdf.format(c.getTime());
		query.append("insert into useraction(Recno,ACTIVITY,activityRecNo,actionDate,DESCRIPTION,refNumber,txnDate,actualDate,WebUserID,WebUserName,action) values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(), Recno,ACTIVITY,activityRecNo,today,memo,refNumber,txD,totime ,WebUserID ,WebUserName,action);		
	}

	/*********************************JournalVoucher***********************************************/

	public String addNewJournalVoucherQuery(JournalVoucherModel obj, int webUserID) {
		query = new StringBuffer();
		query.append("Insert into JournalEntry(Rec_no,TxnNumber,TxnDate,RefNumber,QBStatus,Status,TotalAmount,PreTerRecNo,ReleaseRecNo,ContractRecNo,Notes,RevisedRecNo,DATECREATED,DATEMODIFIED,USERID,RVDifferedIncomeFlag,PVAdvancePaymentFlag,ModifiedUserID,webuserid) values(");
		query.append(obj.getRecno());
		if(obj.getTxnNumber()!=null){
			query.append(",'"+obj.getTxnNumber()+"'");
		}else{
			query.append(",' '");
		}
		if(obj.getTxnDate()!=null){
			query.append(",'"+sdf.format(obj.getTxnDate())+"'");
		}else{
			query.append(",' '");
		}
		if(obj.getTxnNumber()!=null){
			query.append(",'"+obj.getTxnNumber()+"'");
		}else{
			query.append(",' '");
		}
		query.append(",'N','C'"); 
		if(obj.getTotalAmount()!=0){
			query.append(","+obj.getTxnNumber());
		}else{
			query.append(",0");
		}
		query.append(",0,0,0");
		if(obj.getNotes()!=null){
			query.append(",'"+obj.getNotes()+"'");
		}else{
			query.append(",' '");
		}
		query.append(",0");
		query.append(",'"+sdf.format(now.getTime())+"','"+sdf.format(now.getTime())+"',0,'N','N',0,"+webUserID+")");
		return query.toString();
	}

	public String updateJournalVoucherQuery(JournalVoucherModel obj, int webUserID) {
		query = new StringBuffer();
		query.append("Update JournalEntry set TxnNumber='"+obj.getTxnNumber()+"'");
		if(obj.getTxnDate()!=null)
			query.append(",TxnDate='"+sdf.format(obj.getTxnDate())+"'");
		query.append(",RefNumber='"+obj.getTxnNumber()
				+"',TotalAmount="+obj.getTotalAmount()
				+",Notes='"+obj.getNotes()
				+"',DATEMODIFIED='"+sdf.format(now.getTime())
				+"',ModifiedUserID="+webUserID
				+ " where Rec_NO="+ obj.getRecno());
		return query.toString();

	}

	public String getAccountTypeList() {
		query = new StringBuffer();
		query.append("select * from AccountType");
		return query.toString();
	}

	public String getEntityType() {
		query = new StringBuffer();
		query.append("select distinct listtype from qblists");
		return query.toString();
	}

	public String GetNewJournalVoucherRecNoQuery() {
		query = new StringBuffer();
		query.append(" SELECT max(Rec_No) as MaxRecNo from JournalEntry");
		return query.toString();
	}

	public String deleteJournalLineQuery(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from JournalLine Where Rec_No=" + RecNo);
		return query.toString();
	}

	public String addJournalLineQuery(JournalVoucherGridData obj, int RecNo) {
		query = new StringBuffer();
		String memo = "";
		if (obj.getMemo() != null) {
			memo = obj.getMemo().replace("'", "`");
		}
		query.append(" Insert into JournalLine (Rec_No,Line_no ,Amount, Memo,DC_Flag, AccountRef_Key, EntityRef_Key, EntityRef_Type, ClassRef_Key, QBStatus, ISEdit, Proj_ID, Billable, RVdifferedEditKey, RVdifferedRecNo, PVAdvanceEditKey) ");
		query.append(" values(" + RecNo + ","+ obj.getLine_no() + ", "+ obj.getAmount() + ", '" + memo + "','"+obj.getdC_Flag()+"'");
		/*if (obj.getSelectedAccountType() != null)
			query.append(obj.getSelectedAccountType().getRecNo());AccountTypeRef_Key,
		else
			query.append("0");*/
		if (obj.getSelectedAccount() != null)
			query.append(", " + obj.getSelectedAccount().getRec_No());
		else
			query.append(", 0");
		if (obj.getSelectedName() != null)
			query.append(", " + obj.getSelectedName().getRecNo());
		else
			query.append(", 0");
		query.append(",'" + obj.getEntityRef_Type()+"'");
		if (obj.getSelectedClass() != null)
			query.append("," + obj.getSelectedClass().getClass_Key());
		else
			query.append(", NULL");
		query.append(",'N','Y',0");
		if (obj.isBillableChked())
			query.append(",'Y'");
		else
			query.append(",'N' ");
		query.append(",'N',0,'N')");
		return query.toString();
	}

	public String getNextRecordJournalVoucher(int recNo, int webUserID) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM JournalEntry  WHERE rec_NO >" + recNo
				+ "");
		if (webUserID > 0)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no ");
		return query.toString();
	}

	public String getPreviousRecordJournalVoucher(int recNo, int webUserID) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM JournalEntry  WHERE rec_NO <" + recNo
				+ " ");
		if (webUserID > 0)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no  desc");
		return query.toString();
	}

	public String getFirstRecordJournalVoucher(int webUserID) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM JournalEntry ");
		if (webUserID > 0)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no");
		return query.toString();
	}

	public String getLastRecordJournalVoucher(int webUserID) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM JournalEntry");
		if (webUserID > 0)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY rec_no desc");
		return query.toString();
	}

	public String getJournalVoucherGridDataByID(int journalVoucherKey) {
		query = new StringBuffer();
		query.append("select * from JournalLine where Rec_No=" + journalVoucherKey + "  order by 'Line_no' ");
		return query.toString();
	}

	public String checkJournalVoucherDuplicate(String txnNumber, int rec_No) {
		query = new StringBuffer();
		query.append(" Select * from JournalEntry Where txnNumber='"
				+ txnNumber + "' and rec_no !=" + rec_No);
		return query.toString();
	}


	public String checkRVJournalVoucherDuplicate(String txnNumber) {
		query = new StringBuffer();
		query.append(" Select * from RVMAST where JVRefNumber="	+ txnNumber + " And RvOrJv ='J'" );
		return query.toString();
	}

	/***************************************************Work Flow Purchase Request***************************************************/

	public String updatePurchaseRequestStatus(String records) {
		query = new StringBuffer();
		query.append("Update PurchaseRequest set Status='A' where rec_no in("+records+")");
		return query.toString();

	}

	public String getNotApprovedPurchaseRequestReport(int webUserId, Date fromDate,	Date toDate, String reportFrom) {
		query = new StringBuffer();
		query.append("SELECT distinct PurchaseRequest.*, VENDOR.FULLNAME as VendorName,QBLists.FullName As DropShipto,Class.Name As ClassName, ");
		query.append("QBitems.Name as ItemName,Customerlists.FullName As CustomerName, PurchaseRequestLine.Amount,PurchaseRequestLine.rate, ");
		query.append("PurchaseRequestLine.Quantity,PurchaseRequestLine.RcvdQuantity,PurchaseRequestLine.Description AS itemDesc ");
		query.append(" FROM PurchaseRequest  INNER JOIN PurchaseRequestLine ON PurchaseRequest.Rec_No  = PurchaseRequestLine.Rec_No ");
		query.append(" LEFT  JOIN Vendor ON PurchaseRequest.VendorRefKey = Vendor.Vend_Key ");
		query.append("LEFT  JOIN QBLists ON PurchaseRequest.EntityRefKey = QBLists.RecNo ");
		query.append("LEFT  JOIN Class ON PurchaseRequest.ClassRefKey = Class.Class_Key ");
		query.append("LEFT  JOIN QBLists As Customerlists   ON PurchaseRequestLine.EntityRefKey    = Customerlists.RecNo ");
		query.append("LEFT  JOIN QBItems ON PurchaseRequestLine.ItemRefKey      = QBItems.Item_Key where PurchaseRequest.Status='C' and ");
		if (!reportFrom.equalsIgnoreCase(""))
			query.append("  Source = '" + reportFrom + "' and ");
		query.append(" PurchaseRequest.TXNDate between '"
				+ sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "'");
		if (webUserId > 0)
			query.append(" and PurchaseRequest.webUserId=" + webUserId + "");
		query.append(" order by PurchaseRequest.Rec_No");
		return query.toString();
	}



	public String getApprovedPurchaseRequest(int webUserId, Date fromDate,	Date toDate, String reportFrom,int vendorKey,int RecNo) 
	{
		query = new StringBuffer();
		query.append("SELECT PurchaseRequestLine.Rec_No as PORecNo,PurchaseRequestLine.Line_No, PurchaseRequestLine.Description,PurchaseRequestLine.Quantity,PurchaseRequestLine.RcvdQuantity,PurchaseRequestLine.ItemRefKey,PurchaseRequestLine.EntityRefKey,");
		query.append("(PurchaseRequestLine.Quantity - Sum(isnull(PurchaseRequestLine.[RcvdQuantity],0))) as RemainingQuantity, PurchaseRequestLine.Rate,");
		query.append("((PurchaseRequestLine.Quantity - Sum(isnull(PurchaseRequestLine.[RcvdQuantity],0))) * PurchaseRequestLine.Rate) as Amount,");
		query.append("QBItems.FullName As ItemName, Vendor.FullName as VendorName,PurchaseRequest.RefNumber,PurchaseRequest.TXNDate,PurchaseRequest.Memo ");
		query.append(" FROM  PurchaseRequestLine ");
		query.append("INNER JOIN PurchaseRequest On PurchaseRequestLine.Rec_No = PurchaseRequest.Rec_No ");
		query.append("LEFT JOIN Vendor ON PurchaseRequest.VendorRefKey= Vendor.Vend_Key ");
		query.append("LEFT JOIN QBItems ON PurchaseRequestLine.ItemRefKey = QBItems.Item_Key ");
		query.append("WHERE PurchaseRequest.Status in ('A') ");
		if(RecNo>0)
		{
			query.append(" and PurchaseRequestLine.Rec_No=" + RecNo + "");
		}
		else
		{
		if(vendorKey>0)
		{
			query.append(" and PurchaseRequest.VendorRefKey="+vendorKey);
		}		
		if (!reportFrom.equalsIgnoreCase(""))
			query.append(" and Source = '" + reportFrom + "'");
		query.append(" and PurchaseRequest.TXNDate between '" + sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "'");
		if (webUserId > 0)
			query.append(" and PurchaseRequest.webUserId=" + webUserId + "");
		}
		query.append(" group by PurchaseRequestLine.Rec_No,[Line_No],PurchaseRequestLine.Quantity,PurchaseRequestLine.RcvdQuantity, ");
		query.append(" PurchaseRequestLine.ItemRefKey,PurchaseRequestLine.EntityRefKey,PurchaseRequestLine.Description,PurchaseRequestLine.Rate,PurchaseRequest.RefNumber,PurchaseRequest.TXNDate, PurchaseRequest.Memo, QBItems.FullName , Vendor.FullName");
		query.append(" having (PurchaseRequestLine.Quantity - (Sum(isnull(PurchaseRequestLine.RcvdQuantity,0)))) > 0");
		query.append(" order by PurchaseRequestLine.Rec_No,PurchaseRequestLine.[Line_No]");
		return query.toString();
	}

	public String checkMR(int vendorKey) {
		query = new StringBuffer();
		query.append("SELECT PurchaseRequestLine.Rec_No as PORecNo,PurchaseRequestLine.Line_No, PurchaseRequestLine.Description,PurchaseRequestLine.Quantity,PurchaseRequestLine.RcvdQuantity,PurchaseRequestLine.ItemRefKey,PurchaseRequestLine.EntityRefKey,");
		query.append("(PurchaseRequestLine.Quantity - Sum(isnull(PurchaseRequestLine.[RcvdQuantity],0))) as RemainingQuantity, PurchaseRequestLine.Rate,");
		query.append("((PurchaseRequestLine.Quantity - Sum(isnull(PurchaseRequestLine.[RcvdQuantity],0))) * PurchaseRequestLine.Rate) as Amount,");
		query.append("QBItems.FullName As ItemName, Vendor.FullName as VendorName,PurchaseRequest.RefNumber,PurchaseRequest.TXNDate,PurchaseRequest.Memo ");
		query.append(" FROM  PurchaseRequestLine ");
		query.append("INNER JOIN PurchaseRequest On PurchaseRequestLine.Rec_No = PurchaseRequest.Rec_No ");
		query.append("LEFT JOIN Vendor ON PurchaseRequest.VendorRefKey= Vendor.Vend_Key ");
		query.append("LEFT JOIN QBItems ON PurchaseRequestLine.ItemRefKey = QBItems.Item_Key ");
		query.append("WHERE PurchaseRequest.Status in ('A') ");
		if(vendorKey>0){
			query.append(" and PurchaseRequest.VendorRefKey="+vendorKey);
		}		
		query.append(" group by PurchaseRequestLine.Rec_No,[Line_No],PurchaseRequestLine.Quantity,PurchaseRequestLine.RcvdQuantity, ");
		query.append(" PurchaseRequestLine.ItemRefKey,PurchaseRequestLine.EntityRefKey,PurchaseRequestLine.Description,PurchaseRequestLine.Rate,PurchaseRequest.RefNumber,PurchaseRequest.TXNDate, PurchaseRequest.Memo, QBItems.FullName , Vendor.FullName");
		query.append(" having (PurchaseRequestLine.Quantity - (Sum(isnull(PurchaseRequestLine.RcvdQuantity,0)))) > 0");
		query.append(" order by PurchaseRequestLine.Rec_No,PurchaseRequestLine.[Line_No]");

		return query.toString();

	}


	public String updatePurchaseRequestLine(PurchaseRequestGridData obj) {
		query = new StringBuffer();
		query.append("update PurchaseRequestLine set RcvdQuantity=" + (obj.getQuantity()+obj.getRecivedQuantity())
				+ ",PORec_No  =" + obj.getRecNo()
				+ ",POLine_No ="+ obj.getLineNo()
				+ ",PO_Source='ONL'  Where  Rec_No="+ obj.getpORecNo()+" and Line_No ="+obj.getpOLineNo());
		return query.toString();
	}

	public String checkMRIsFULLYRECEIVED(int rec_no) {
		query = new StringBuffer();
		query.append("SELECT Sum(PurchaserequestLine.Quantity) as POQty,(Sum(isnull(PurchaserequestLine.RcvdQuantity,0))) as RcvdQty FROM PurchaserequestLine where rec_no = "+ rec_no );
		return query.toString();
	}

	/***************************************************Work Flow Purchase Order***************************************************/

	public String getNotApprovedPurchaseOrderReport(int webUserId,boolean seeTrasction, Date fromDate,	Date toDate, String reportFrom) {
		query = new StringBuffer();
		query.append("SELECT PURCHASEORDER.*,VENDOR.FULLNAME As VendorName,QBLists.FullName As DropShipto ");
		query.append("FROM PURCHASEORDER ");
		query.append(" LEFT JOIN Vendor ON PURCHASEORDER.VendorRefKey = Vendor.Vend_Key  ");
		query.append(" LEFT JOIN QBLists ON PURCHASEORDER.EntityRefKey = QBLists.RecNo  ");
		query.append(" where PURCHASEORDER.Status in ('C') and ");
		if (!reportFrom.equalsIgnoreCase(""))
			query.append("Source in('" + reportFrom + "') and ");
		query.append(" PURCHASEORDER.TXNDate between '" + sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "' ");
		if (webUserId > 0 && !seeTrasction )
			query.append(" and PURCHASEORDER.webUserId=" + webUserId + " ");
		query.append(" order by PURCHASEORDER.Rec_No");
		return query.toString();
	}

	public String updatePurchaseOrderStatus(String records) {
		query = new StringBuffer();
		query.append("Update PurchaseOrder set Status='A' where rec_no in("+records+")");

		return query.toString();

	}
	public String updateItemReceiptStatus(String records) {
		query = new StringBuffer();
		query.append("Update IRMast set Status='A' where recno in("+records+")");

		return query.toString();

	}


	public String getApprovedPurchaseOrder(int webUserId, Date fromDate, Date toDate, String reportFrom,int vendorKey,int RecNo)
	{
		query = new StringBuffer();
		query.append("SELECT PurchaseOrderLine.Rec_No as PORecNo,PurchaseOrderLine.Line_No,purchaseorderline.entityRefKey,PurchaseOrderLine.ItemRefKey, ");
		query.append(" PurchaseOrderLine.Description,PurchaseOrderLine.Quantity,PurchaseOrderLine.RcvdQuantity, ");
		query.append("(PurchaseOrderLine.Quantity - Sum(isnull(PurchaseOrderLine.[RcvdQuantity],0))) as RemainingQuantity,PurchaseOrderLine.Rate, ");
		query.append("((PurchaseOrderLine.Quantity - Sum(isnull(PurchaseOrderLine.[RcvdQuantity],0))) * PurchaseOrderLine.Rate) as Amount, ");
		query.append("  QBItems.FullName As ItemName, Vendor.FullName as VendorName, ");
		query.append("PurchaseOrder.RefNumber,PurchaseOrder.TXNDate,PurchaseOrder.Memo  ");
		query.append("FROM PurchaseOrderLine  ");
		query.append("INNER JOIN PurchaseOrder       On PurchaseOrderLine.Rec_No = PurchaseOrder.Rec_No  ");
		query.append("LEFT JOIN Vendor   ON PurchaseOrder.VendorRefKey= Vendor.Vend_Key  ");
		query.append(" LEFT JOIN QBItems  ON PurchaseOrderLine.ItemRefKey  = QBItems.Item_Key  ");
		query.append(" WHERE PurchaseOrder.Status in ('A','P')  ");
		if(RecNo>0)
		{
			query.append(" And PurchaseOrderLine.Rec_No ="+RecNo);
		}
		else
		{
		if(vendorKey>0){
			query.append(" And PurchaseOrder.VendorRefKey ="+vendorKey);
		}		
		if (!reportFrom.equalsIgnoreCase(""))
			query.append(" and Source = '" + reportFrom + "'");
		query.append(" and PurchaseOrder.TXNDate between '" + sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "' ");
		if (webUserId > 0)
			query.append(" and PurchaseOrder.webUserId=" + webUserId + "");
		}
		query.append(" group by PurchaseOrderLine.Rec_No,[Line_No],PurchaseOrderLine.Quantity,PurchaseOrderLine.RcvdQuantity, ");
		query.append(" PurchaseOrderLine.ItemRefKey,PurchaseOrderLine.Description, ");
		query.append(" PurchaseOrderLine.Rate,purchaseorderline.entityRefKey,PurchaseOrder.RefNumber , ");
		query.append(" PurchaseOrder.TXNDate, PurchaseOrder.Memo, QBItems.FullName , Vendor.FullName  ");
		query.append(" having (PurchaseOrderLine.Quantity - (Sum(isnull(PurchaseOrderLine.RcvdQuantity,0)))) > 0   ");
		query.append(" order by PurchaseOrderLine.Rec_No,PurchaseOrderLine.[Line_No]  ");
		
		return query.toString();
	}

	public String checkPOIsFULLYRECEIVED(int rec_no) {
		query = new StringBuffer();
		query.append("SELECT Sum(PurchaseOrderLine.Quantity) as POQty, (Sum(isnull(PurchaseOrderLine.RcvdQuantity,0))) as RcvdQty  FROM PurchaseOrderLine Where  PurchaseOrderLine.Rec_No =  "+ rec_no );
		return query.toString();
	}

	public String checkPO(int vendorKey) {
		query = new StringBuffer();
		query.append(" Select PurchaseOrderLine.Rec_No,[Line_No], Sum(isnull(PurchaseOrderLine.RcvdQuantity,0)) as RecvdQty ,PurchaseOrder.VendorRefKey ");
		query.append(" From PurchaseOrderLine ");
		query.append(" Inner Join PurchaseOrder on PurchaseOrder.Rec_No = PurchaseOrderLine.Rec_No ");
		query.append(" Where PurchaseOrder.VendorRefKey= "+vendorKey);
		query.append(" and PurchaseOrder.Status in('A','P') And IsNull(PurchaseOrder.IsFullyReceived,'') <> 'Y'  ");
		query.append(" group by  PurchaseOrderLine.Rec_No,[Line_No], PurchaseOrderLine.Quantity, PurchaseOrder.VendorRefKey ");
		query.append(" Having (PurchaseOrderLine.Quantity - (Sum(isnull(PurchaseOrderLine.RcvdQuantity,0)))) <> 0  ");
		return query.toString();

	}

	public String updatePurchaseOrderLine(CheckItemsModel item) {
		query = new StringBuffer();
		query.append("update PurchaseOrderLine set RcvdQuantity=" + (item.getQuantity()+item.getRecivedQuantity())
				+ ",BilledRec_No =" + item.getRecNo()
				+ ",BilledLine_No ="+ item.getLineNo()
				+ ",Billed_Source ='IR' Where  Rec_No="+ item.getpORecNo()+" and Line_No ="+item.getpOLineNo());
		return query.toString();
	}
	//--------------------------------item receipt work flow----------------------------------------------

	public String getItemReceipt(int webUserId, Date fromDate, Date toDate, String reportFrom,int vendorKey,int RecNo) 
	{
		query = new StringBuffer();
		query.append(" SELECT IRITems.RecNo as IRMastRecNo,IRITems.[LineNo],IRITems.ItemKey,IRITems.Description,  IRITems.Quantity,Sum(isnull(BilledItemReceipts.[BilledQuantity],0)) as BilledQuantity,");
		query.append(" (IRITems.Quantity - Sum(isnull(BilledItemReceipts.[BilledQuantity],0))) as RemainingQuantity,  ");
		query.append(" IRITems.Cost,((IRITems.Quantity - Sum(isnull(BilledItemReceipts.[BilledQuantity],0))) * IRITems.Cost) as Amount , ");
		query.append(" IRMast.IRNo ,IRMast.IRNoLocal,  IRMast.IRDate, IRMast.Memo, QBItems.FullName As ItemName, Vendor.FullName as VendorName,  ");
		query.append(" Accounts.Name as AccName, Accounts.AccountType,Accounts.AccountNumber, Accounts.Name as AccountName   FROM IRItems ");
		query.append(" INNER JOIN (IRMast LEFT JOIN Vendor ON IRMast.VendorKey = Vendor.Vend_Key   ");
		query.append(" LEFT  JOIN Accounts ON IRMast.APAccountKey = Accounts.REC_NO)  ON IRItems.RECNo = IRMast.RecNo   ");
		query.append(" Inner JOIN QBItems ON IRItems.ItemKey = QBItems.Item_Key  ");
		query.append(" LEFT  JOIN BilledItemReceipts ON IRItems.RECNo = BilledItemReceipts.ItemReceiptRecNo   and IRItems.[LineNo] = BilledItemReceipts.ItemReceiptLineNo   ");
		query.append(" Where IRMast.Status in ('A','C','P') ");
		if(RecNo>0)
		{
			query.append(" and IRITems.RecNo = "+RecNo);
		}
		else
		{
		if(vendorKey>0){
			query.append(" and  IRMast.VendorKey = "+vendorKey);
		}		
		if (!reportFrom.equalsIgnoreCase(""))
			query.append(" and Source = '" + reportFrom + "'");
		query.append(" and IRMast.IRDate between '" + sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "' ");
		if (webUserId > 0)
			query.append(" and IRMast.webUserId=" + webUserId + "");
		}
		query.append(" group by IRITems.RecNo,[LineNo],IrItems.Quantity,    IRITems.ItemKey,IRITems.Description,  IRITems.Cost,IRMast.IRNo ,IRMast.IRNoLocal,  IRMast.IRDate, IRMast.Memo, QBItems.FullName , Vendor.FullName ,  Accounts.Name, Accounts.AccountType, Accounts.AccountNumber, Accounts.Name  having (IrItems.Quantity - (Sum(isnull(BilledItemReceipts.BilledQuantity,0)))) > 0  ");
		query.append(" order by IRITems.RecNo,IRItems.[LineNo] ");
		return query.toString();
	}

	public String checkIR(int vendorKey) {
		query = new StringBuffer();
		query.append("Select IRITems.RecNo,[LineNo],APAccountList.AccountName,Sum(isnull(BilledItemReceipts.BilledQuantity,0)) as BilledQty From IRItems ");
		query.append(" Inner Join IrMast ON IRMast.RecNo = IRItems.RecNo ");
		query.append(" LEFT JOIN BilledItemReceipts ON  IRItems.RECNo = BilledItemReceipts.ItemReceiptRecNo ");
		query.append(" And  IRItems.[LineNo] = BilledItemReceipts.ItemReceiptLineNo   ");
		query.append(" Left JOIN Accounts AS APAccountList ON IrMast.APAccountKey = APAccountList.REC_NO ");
		query.append(" Where  IRMAST.Status<>'V' ");
		if(vendorKey>0){
			query.append(" and VendorKey ="+vendorKey);
		}		
		query.append(" group by IRITems.RecNo,[LineNo],APAccountList.AccountName,IrItems.Quantity having (IrItems.Quantity - (Sum(isnull(BilledItemReceipts.BilledQuantity,0)))) > 0 ");
		return query.toString();

	}

	public String insertItemReceiptLine(CheckItemsModel item,int recNo) {
		query = new StringBuffer();
		query.append("Insert Into BilledItemReceipts (RecNo,BillRecNo,BillLineNo,BilledItemKey,ItemReceiptRecNo,ItemReceiptLineNo,BilledQuantity,Source) Values("
				+recNo
				+ ","+ item.getRecNo()
				+ ","+ item.getLineNo()
				+ ","+ item.getSelectedItems().getRecNo() 
				+ ","+ item.getpORecNo()
				+","+item.getpOLineNo()
				+","+item.getQuantity()
				+",'BILL')");
		return query.toString();
	}

	public String GetMaxBilledItemReceipts()
	{
		query=new StringBuffer();
		query.append("SELECT max(RecNo)+1 as MaxRecNo from BilledItemReceipts");		
		return query.toString();
	}
	//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=Delivery

	public String getDeliveryReportQuery(int webUserID,boolean seeTrasction, Date fromDate, Date toDate) 
	{
		query = new StringBuffer();
		query.append("SELECT Delivery.*,Prospective.FullName As ProspectiveName,EntityList.FullName AS Entity,SendViaList.DESCRIPTION AS SendVia");
		query.append(" from Delivery LEFT JOIN QBLists AS EntityList   ON Delivery.CustomerRefKey = EntityList.RecNo");
		query.append(" LEFT JOIN Prospective         ON Delivery.CustomerRefKey   = Prospective.RecNo");
		query.append(" LEFT JOIN HRListValues AS SendViaList   ON Delivery.SendViaReffKey = SendViaList.ID ");
		query.append(" ");
		query.append(" where Delivery.Status not in ('V') ");
		query.append(" and Delivery.TxnDate Between '" + sdf.format(fromDate) + "' And '" + sdf.format(toDate) + "'");
		if (webUserID > 0 && !seeTrasction) {
			query.append(" and Delivery.webUserID=" + webUserID);
		}
		query.append(" Order by Delivery.TxnDate Desc,Delivery.RefNumber Desc, Delivery.RecNo");
		return query.toString();
	}

	public String getNextRecordDelivery(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Delivery  WHERE recNO >" + recNo+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno ");
		return query.toString();
	}

	public String getPreviousRecordDelivery(int recNo, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Delivery  WHERE recNO <" + recNo
				+ " ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno  desc");
		return query.toString();
	}

	public String getFirstRecordDelivery(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Delivery ");
		if (webUserID > 0 && !seeTrasction)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno");
		return query.toString();
	}

	public String getLastRecordDelivery(int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("SELECT TOP 1 * FROM Delivery");
		if (webUserID > 0 && !seeTrasction)
			query.append(" where webuserid=" + webUserID + " ");
		query.append(" ORDER BY recno desc");
		return query.toString();
	}

	public String getDDeliveryGridDataByID(int deliveryId) {
		query = new StringBuffer();
		query.append("SELECT DeliveryLine.*,QBItems.FullName  AS ItemName,QBItems.[ArFullName] as ItemNameAR, ");
		query.append(" QBItems.ItemType as itemType,QBItems.IncomeAccountRef as IncomeAccountRef  , Class.FullName AS ClassName,Class.[ArFullName] as ItemClassAR, ");
		query.append(" DeliveryInvoiced.RecNo As InvoiceRecNo,DeliveryInvoiced.TxnType as TxnType,DeliveryInvoiced.[LineNo] As InvoiceLineNo, ");
		query.append(" InventorySiteList.SiteName as InventorySite,InventorySiteList.ArName as InventorySiteAR FROM ");
		query.append(" (((DeliveryLine  LEFT JOIN Class ON DeliveryLine.ClassRefKey  = Class.Class_Key) ");
		query.append(" LEFT JOIN QBItems ON DeliveryLine.ItemRefKey = QBItems.Item_Key) ");
		query.append(" LEFT JOIN InventorySiteList ON DeliveryLine.InventorySiteKey = InventorySiteList.ItemKey) ");
		query.append(" LEFT JOIN DeliveryInvoiced ON DeliveryInvoiced.DeliveryRecNo = DeliveryLine.RecNo And DeliveryInvoiced.DeliveryLineNo = DeliveryLine.[LineNo] ");
		query.append(" Where DeliveryLine.RecNo =" + deliveryId + " Order By DeliveryLine.[LineNo] ");
		return query.toString();
	}

	public String getDeliveryByID(int deliveryKey, int webUserID,boolean seeTrasction) {
		query = new StringBuffer();
		query.append("select * from Delivery where recNo=" + deliveryKey
				+ "");
		if (webUserID > 0 && !seeTrasction)
			query.append(" and webUserId=" + webUserID + "");
		return query.toString();
	}

	public String addNewDelivery(DeliveryModel obj, int webUserID,String webUserName) 
	{
		Calendar c = Calendar.getInstance();
		query = new StringBuffer();
		query.append("Insert into Delivery(RecNo,CustomerRefKey,ClassRefKey,TxnDate,RefNumber,BillAddress1,BillAddress2, ");
		query.append("BillAddress3,BillAddress4,BillAddress5,BillAddressCity,BillAddressState,BillAddressPostalCode,BillAddressCountry,BillAddressNote, ");
		query.append("ShipAddress1,ShipAddress2,ShipAddress3,ShipAddress4,ShipAddress5,ShipAddressCity,ShipAddressState,ShipAddressPostalCode,ShipAddressCountry, ");
		query.append("ShipAddressNote,IsPending,PONumber,SendViaReffKey,SalesRefKey,FOB,[Memo],IsToBePrinted,IsToEmailed,IsTaxIncluded,Amount,QuotationRecNo,CustomField1,CustomField2, ");
		query.append("CustomField3,CustomField4,CustomField5,RemindFlag,");
		if(obj.getRemindDate()!=null){
			query.append("RemindDate,");
		}
		query.append("RemindDays,status,UpdateREG,DescriptionHIDE,QtyHIDE,ClassHIDE,webUserID,DATECREATED,transformQ,webUserName) Values( " );
		query.append(obj.getRecNo());
		query.append(","+obj.getCustomerRefKey());
		query.append(","+obj.getClassRefKey());
		query.append(",'"+sdf.format(obj.getTxnDate())+"'");
		query.append(",'"+obj.getRefNumber()+"'");
		query.append(",'"+obj.getBillAddress1()+"'");
		query.append(",'"+obj.getBillAddress2()+"'");
		query.append(",'"+obj.getBillAddress3()+"'");
		query.append(",'"+obj.getBillAddress4()+"'");
		query.append(",'"+obj.getBillAddress5()+"'");
		query.append(",'"+obj.getBillAddressCity()+"'");
		query.append(",'"+obj.getBillAddressState()+"'");
		query.append(",'"+obj.getBillAddressPostalCode()+"'");
		query.append(",'"+obj.getBillAddressCountry()+"'");
		query.append(",'"+obj.getBillAddressNote()+"'");
		query.append(",'"+obj.getBillAddress1()+"'");
		query.append(",'"+obj.getBillAddress2()+"'");
		query.append(",'"+obj.getBillAddress3()+"'");
		query.append(",'"+obj.getBillAddress4()+"'");
		query.append(",'"+obj.getBillAddress5()+"'");
		query.append(",'"+obj.getBillAddressCity()+"'");
		query.append(",'"+obj.getBillAddressState()+"'");
		query.append(",'"+obj.getBillAddressPostalCode()+"'");
		query.append(",'"+obj.getBillAddressCountry()+"'");
		query.append(",'"+obj.getBillAddressNote()+"'");
		query.append(",'N'");//ispinding
		query.append(",'"+obj.getpONumber()+"'");
		query.append(","+obj.getSendViaReffKey()+"");
		query.append(","+obj.getSalesRefKey()+"");
		query.append(",''");
		query.append(",'"+obj.getMemo()+"'");
		query.append(",'Y'");
		query.append(",'N'");
		query.append(",'N'");
		query.append(",0");	
		if(obj.getQuotationRecNo()>0){
			query.append(","+obj.getQuotationRecNo()+"");
		}else{
			query.append(",0");	
		}
		query.append(",'"+obj.getCustomField1()+"'");
		query.append(",'"+obj.getCustomField2()+"'");
		query.append(",'"+obj.getCustomField3()+"'");
		query.append(",'"+obj.getCustomField4()+"'");
		query.append(",'"+obj.getCustomField5()+"'");
		query.append(",'"+obj.getRemindFalg()+"'");
		if(obj.getRemindDate()!=null){
			query.append(",'"+sdf.format(obj.getRemindDate())+"'");
		}

		query.append(","+obj.getRemindDays()+"");
		query.append(",'C'");
		query.append(",'N'");
		query.append(",'N'");
		query.append(",'N'");
		query.append(",'N'");
		query.append(","+webUserID+"");
		query.append(",'"+sdf.format(c.getTime())+"'");
		query.append(",'"+obj.getTransformQ()+"'");
		query.append(",'"+webUserName+"'");
		query.append(")");
		return query.toString();

	}

	public String addDeliveryGridItems(DeliveryLineModel obj, int recNo) {
		query = new StringBuffer();
		query.append("Insert into DeliveryLine(RecNo,[LineNo],ItemRefKey,Description,Quantity,Rate,AvgCost,RatePercent,PriceLevelRefKey, ");
		query.append("ClassRefKey,Amount,SalesTaxCodeRefKey,IsTaxable,OverrideItemAccountRefKey,Other1,Other2,QuotationLineNo,InventorySiteKey) Values(" );
		query.append(recNo);
		query.append(","+obj.getLineNo());
		query.append(","+obj.getSelectedItems().getRecNo());
		query.append(",'"+obj.getDescription()+"'");
		query.append(","+obj.getQuantity()+"");
		query.append(","+obj.getRate()+"");
		query.append(","+obj.getAvgCost()+"");
		query.append(",0");
		query.append(",0");
		query.append(","+obj.getClassRefKey()+"");
		query.append(",0");
		query.append(",0");
		query.append(",'N'");
		query.append(",'0'");
		query.append(",''");
		query.append(",''");	
		if(obj.getQuotationLineNo()>0){
			query.append(","+obj.getQuotationLineNo()+"");
		}else{
			query.append(",0");	
		}
		if(obj.getSelectedInvcCutomerGridInvrtySiteNew()!=null)
		query.append(","+obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()+"");
		else
		query.append(",0");	
		
		query.append(")");
		return query.toString();

	}

	public String deleteDeliveryGridItems(int RecNo) {
		query = new StringBuffer();
		query.append(" Delete from DeliveryLine Where RecNo=" + RecNo);
		return query.toString();
	}

	public String updateDelivery(DeliveryModel obj, int webUserID,String webUserName) {
		query = new StringBuffer();
		query.append("Update Delivery set " );
		query.append("CustomerRefKey ="+obj.getCustomerRefKey() );
		query.append(",ClassRefKey="+obj.getClassRefKey());
		query.append(",TxnDate='"+ sdf.format(obj.getTxnDate())+"'");
		query.append(",RefNumber='"+obj.getRefNumber()+"'");
		query.append(",BillAddress1  ='"+obj.getBillAddress1()+"'");
		query.append(",BillAddress2  ='"+obj.getBillAddress2()+"'");
		query.append(",BillAddress3  ='"+obj.getBillAddress3()+"'");
		query.append(",BillAddress4  ='"+obj.getBillAddress4()+"'");
		query.append(",BillAddress5  ='"+obj.getBillAddress5()+"'");
		query.append(",BillAddressCity ='"+obj.getBillAddressCity()+"'");
		query.append(",BillAddressState ='"+obj.getBillAddressState()+"'");
		query.append(",BillAddressPostalCode='"+obj.getBillAddressPostalCode()+"'");
		query.append(",BillAddressCountry='"+obj.getBillAddressCountry()+"'");
		query.append(",BillAddressNote='"+obj.getBillAddressNote()+"'");
		query.append(",ShipAddress1='"+obj.getBillAddress1()+"'");
		query.append(",ShipAddress2='"+obj.getBillAddress2()+"'");
		query.append(",ShipAddress3='"+obj.getBillAddress3()+"'");
		query.append(",ShipAddress4='"+obj.getBillAddress4()+"'");
		query.append(",ShipAddress5='"+obj.getBillAddress5()+"'");
		query.append(",ShipAddressCity ='"+obj.getBillAddressCity()+"'");
		query.append(",ShipAddressState ='"+obj.getBillAddressState()+"'");
		query.append(",ShipAddressPostalCode='"+obj.getBillAddressPostalCode()+"'");
		query.append(",ShipAddressCountry ='"+obj.getBillAddressCountry()+"'");
		query.append(",ShipAddressNote='"+obj.getBillAddressNote()+"' ");
		query.append(",PONumber='"+obj.getpONumber()+"'");
		query.append(",SendViaReffKey="+obj.getSendViaReffKey()+"");
		query.append(",SalesRefKey ="+obj.getSalesRefKey()+"");
		query.append(",[Memo]='"+obj.getMemo()+"'");
		query.append(",CustomField1='"+obj.getCustomField1()+"'");
		query.append(",CustomField2='"+obj.getCustomField2()+"'");
		query.append(",CustomField3='"+obj.getCustomField3()+"'");
		query.append(",CustomField4='"+obj.getCustomField4()+"'");
		query.append(",CustomField5='"+obj.getCustomField5()+"'");
		query.append(",RemindFlag='"+obj.getRemindFalg()+"'");
		if(obj.getRemindDate()!=null){
			query.append(",RemindDate ='"+sdf.format(obj.getRemindDate())+"'");	
		}
		query.append(",RemindDays="+obj.getRemindDays() + ", webUserID=" + webUserID + " , webUserName='"+webUserName+"'");
		query.append(" Where RecNo="+obj.getRecNo());
		return query.toString();

	}

	public String updateQBItemsDesc(DeliveryLineModel obj) {
		query = new StringBuffer();
		query.append("Update QBItems SET SALESDESC =' "+obj.getDescription()+"' WHERE Item_Key ="+obj.getSelectedItems().getRecNo() );

		return query.toString();

	}

	public String checkIfSerialNumberIsDuplicateForDelivery(
			String serialNumber, int recNO) {
		query = new StringBuffer();
		query.append(" Select * from Delivery Where RefNumber='" + serialNumber + "' and recno !=" + recNO);
		return query.toString();
	}

	public String getQuotationByIDForChangeStatus(int quotationKey) {
		query = new StringBuffer();
		query.append(" select Quotation.*,ActivityStatus.StatusDate AS LastStatusDate,ActivityStatus.RecNo As StatusRecNo,Prospective.FullName As ProspectiveName,EntityList.FullName As Entity,SendViaList.Description As SendVia,Class.FullName As ClassName ");
		query.append(" from ((((Quotation Left Join QBLists As EntityList ON EntityList.RecNo = Quotation.CustomerRefKey) ");
		query.append(" LEFT JOIN Prospective ON Quotation.CustomerRefKey   = Prospective.RecNo)  ");
		query.append(" Left Join Class ON Class.Class_Key = Quotation.ClassRefKey) ");
		query.append(" Left Join ActivityStatus ON CAST(ActivityStatus.ActivityRecNo AS VARCHAR) + CAST(ActivityStatus.Active AS VARCHAR) = CAST(Quotation.RecNo AS VARCHAR) + 'Y') ");
		query.append(" Left Join HRListValues As SendViaList ON SendViaList.ID = Quotation.SendViaReffKey Where ");
		query.append(" Quotation.Status in ('C','H','O') AND Quotation.RecNo  = "+quotationKey);
		return query.toString();
	}

	public String updateQuotationStatus(int quotationKey,String status,String statusDesc) {
		query = new StringBuffer();
		query.append("Update Quotation set " );
		query.append("Status ='"+status+"'");
		query.append(" ,StatusDesc ='"+statusDesc+"'");
		query.append("Where RecNo="+quotationKey);
		return query.toString();

	}

	public String updateActivityStatus(int quotationKey) {
		query = new StringBuffer();
		query.append("Update ActivityStatus set " );
		query.append("Active ='N'");
		query.append("Where ActivityRecNo="+quotationKey+" and Activity=25");
		return query.toString();
	}

	public String AddActivityStatus(ActivityStatusModel model) {
		query = new StringBuffer();
		query.append("Insert into ActivityStatus(RecNo,Activity,ActivityRecNo,Status,Description");
		if(model.getStatusDate()!=null){
			query.append(",StatusDate");
		}
		query.append(",[Memo],Active) Values(" );
		query.append(model.getRecNo());
		query.append(","+model.getActivity());
		query.append(","+model.getActivityRecNo());
		query.append(",'"+model.getStatus()+"'");
		query.append(",'"+model.getDescription()+"'");
		if(model.getStatusDate()!=null){
			query.append(",'"+sdf.format(model.getStatusDate())+"'");
		}
		query.append(",'"+model.getMemo()+"'");
		query.append(",'"+model.getActive()+"'");
		query.append(")");
		return query.toString();
	}
	
	public String getApprovedQuotation(int customerKey,int webUserId,int RecNo)
	{
		query = new StringBuffer();
		query.append("select Quotation.*, Prospective.FullName As ProspectiveName,EntityList.FullName As Entity,SendViaList.Description As SendVia,QuotationLine.[LineNo],QuotationLine.Amount As LineAmount ");
		query.append("from ((((Quotation LEFT JOIN QBLists As EntityList ON EntityList.RecNo = Quotation.CustomerRefKey) ");
		query.append("LEFT JOIN Prospective ON Quotation.CustomerRefKey = Prospective.RecNo) ");
		query.append("LEFT JOIN HRListValues As SendViaList ON SendViaList.ID    = Quotation.SendViaReffKey) ");
		query.append("LEFT JOIN QuotationLine ON QuotationLine.RecNo = Quotation.RecNo)  ");
		query.append("LEFT JOIN QuotationDelivery ON QuotationDelivery.QuotationRecNo = QuotationLine.RECNO And " );
		query.append("QuotationDelivery.QuotationLineNo = QuotationLine.[LineNo] " );
		query.append("Where Quotation.Status in ('A','I','S','D') And QuotationDelivery.TxnType is Null And Quotation.ClientType ='C' " );		
		if (RecNo > 0)
			query.append(" and Quotation.RecNo=" + RecNo + "");
		else
		{
			query.append(" and Quotation.CustomerRefKey   ="+customerKey );
			
		if (webUserId > 0)
			query.append(" and Quotation.webUserId=" + webUserId + "");
		
		}
		return query.toString();
	}
	
	public String updateQuotationDelivery(int quotationKey,int deliveryRecNo) {
		query = new StringBuffer();
		query.append("Update Quotation set " );
		query.append("DeliveryRecNo  ="+deliveryRecNo);
		query.append(" ,Status='D'");
		query.append(" ,StatusDesc ='Deliver'");
		query.append("Where RecNo="+quotationKey);
		return query.toString();
	}
	
	public String addQuotationDelivery(DeliveryLineModel item,int recNo) {
		query = new StringBuffer();
		query.append("Insert into QuotationDelivery(RecNo,QuotationRecNo,QuotationLineNo,TxnType) Values("
				+recNo
				+ ","+ item.getQuotationNo()
				+ ","+ item.getQuotationLineNo()
				+",'D')");
		return query.toString();
	}
	
	public String GetNewDeliveryRecNo() {
		query = new StringBuffer();
		query.append(" SELECT max(RecNo) as MaxRecNo from Delivery");
		return query.toString();
	}
	
	public String updateDeliveryStatus(String records) {
		query = new StringBuffer();
		query.append("Update Delivery set Status='V' where RecNo in("+records+")");
		return query.toString();

	}
	
	public String getCustomerDelivery(int customerKey,int webUserId,String type) {
		query = new StringBuffer();
		query.append("SELECT Delivery.RecNo, Delivery.RefNumber, Delivery.TxnDate,Delivery.CustomerRefKey,Delivery.status,DeliveryLine.[LineNo],DeliveryLine.ItemRefKey, ");
		query.append("DeliveryLine.Description,DeliveryLine.Quantity,DeliveryLine.QuantityInvoice,  QBItems.FullName AS ItemName,QBLists.FullName AS CustName ");
		query.append("FROM (((Delivery INNER JOIN DeliveryLine ON Delivery.RecNo = DeliveryLine.RecNo) ");
		query.append("INNER JOIN QBLists ON Delivery.CustomerRefKey = QBLists.RecNo) ");
		query.append("INNER JOIN QBItems ON DeliveryLine.ItemRefKey  = QBItems.Item_Key) ");
		query.append("LEFT JOIN DeliveryInvoiced ON DeliveryInvoiced.DeliveryRecNo = DeliveryLine.RecNo And DeliveryInvoiced.DeliveryLineNo = DeliveryLine.[LineNo]  " );
		query.append("Where Delivery.CustomerRefKey = " +customerKey );
		query.append("And (DeliveryInvoiced.RecNo is Null) " );
		query.append("And (DeliveryInvoiced.TxnType is Null Or DeliveryInvoiced.TxnType = '"+type +"')" );
		query.append("And Delivery.status not in ('V') " );
		if (webUserId > 0)
			query.append(" and Delivery.webUserId=" + webUserId + " ");
		query.append("  Order by Delivery.RecNo" );
		return query.toString();
	}
	
	public String getDeliveryForInvoice(int customerKey,int webUserId,String txnType,int RecNo)
	{
		query = new StringBuffer();
		query.append("SELECT Delivery.RecNo,Delivery.TxnID,Delivery.RefNumber,Delivery.TxnDate as TxnDate,Delivery.CustomerRefKey,Delivery.status,DeliveryLine.[LineNo],DeliveryLine.inventorysitekey,DeliveryLine.ItemRefKey as ItemRefKey,Delivery.memo, ");
		query.append("DeliveryLine.Description,DeliveryLine.Quantity,DeliveryLine.QuotationLineNo,DeliveryLine.QuantityInvoice,QBItems.FullName AS ItemName,QBLists.FullName AS CustName,DeliveryLine.TxnLineID ");
		query.append("FROM (((Delivery INNER JOIN DeliveryLine  ON Delivery.RecNo = DeliveryLine.RecNo) ");
		query.append("INNER JOIN QBLists ON Delivery.CustomerRefKey  = QBLists.RecNo) ");
		query.append("INNER JOIN QBItems ON DeliveryLine.ItemRefKey = QBItems.Item_Key) ");
		query.append("LEFT JOIN DeliveryInvoiced ON DeliveryInvoiced.DeliveryRecNo = DeliveryLine.RecNo And DeliveryInvoiced.DeliveryLineNo = DeliveryLine.[LineNo]  " );
		query.append("Where Delivery.CustomerRefKey = " + customerKey   );
		query.append(" And (DeliveryInvoiced.RecNo is Null Or DeliveryInvoiced.RecNo = 0) " );
		query.append("And (DeliveryInvoiced.TxnType is Null Or DeliveryInvoiced.TxnType = '" + txnType + "')" );
		query.append("And Delivery.status not in ('V')"  );
		if (RecNo > 0)
			query.append(" and Delivery.RecNo=" + RecNo + " ");
		else
		{
		if (webUserId > 0)
			query.append(" and Delivery.webUserId=" + webUserId + " ");
		}
		query.append("  Order by Delivery.RecNo,DeliveryLine.[LineNo]" );
		return query.toString();
	}
	
	public String deleteCashInvoiceDeliveryLine(int RecNo,String type) {
		query = new StringBuffer();
		query.append(" Delete from DeliveryInvoiced Where TxnType ='"+type+"' and RecNo=" + RecNo);
		return query.toString();
	}
	
	public String addDeliveryInvoice(CashInvoiceGridData item,int recNo, String type ) {
		query = new StringBuffer();
		query.append("Insert into DeliveryInvoiced(TxnType,RecNo,[LineNo],DeliveryRecNo,DeliveryLineNo) Values('"+type+"',"
				+recNo
				+ ","+ item.getLineNo()
				+ ","+ item.getDeliverRecNo()
				+","+item.getDeliveryLineNo()
				+ ")");
		return query.toString();
	}
	
	public String checkInvoicedDelivery(int deliveryKey) {
		query = new StringBuffer();
		query.append("select * from DeliveryInvoiced where deliveryrecno = " +deliveryKey );
		
		return query.toString();
	}
	
	public String updateCustomerBalance(double balance,int customerKey) {
		query = new StringBuffer();
		query.append("Update Customer Set LocalBalance =" + balance + " Where Cust_Key =" + customerKey);
		return query.toString();

	}

	public String getBankInfoForBankTransfer(long recno) {
		query = new StringBuffer();
		query.append("SELECT * from BankTransfer Where RecNo = " + recno);
		return query.toString();
	}
	
	public String addNewBankTransferInfoQuery(BankTransferModel obj) {
		query = new StringBuffer();
		query.append("Insert into BankTransfer (RecNo,Attn_Name,Attn_Position,From_ActName,From_BankName,From_ActNumber,From_Branch,From_IBANNo,");
		query.append("To_ActName,To_BankName,To_ActNumber,To_Branch,To_IBANNo,To_TRANSCode)");
		query.append(" Values(" + obj.getBankTransferRecNo() + ",'"	+ obj.getAttnName() + "' , '" + obj.getAttnPosition() + "' , '"	+ obj.getFromActName());
		query.append("" + "' , '" + obj.getFromBankName() + "' , '"	+ obj.getFromActNumber() + "' , '" + obj.getFromBranch() + "' , '" + obj.getFromIBANNo());
		query.append("" + "' , '" + obj.getToActName() + "' , '" + obj.getToBankName() + "' , '" + obj.getToActNumber()	+ "' , '" + obj.getToBranch());
		query.append("" + "' , '" + obj.getToIBANNo() + "' , '"	+ obj.getToTRANSCode() + "')");
		return query.toString();
	}
	
	public String updateBankTransfer(BankTransferModel obj, int webUserID) {
		Calendar c = Calendar.getInstance();
		query = new StringBuffer();
		query.append("Update Checkmast set " );
		query.append("PvNo ='"+obj.getPvNo()+"'");
		query.append(",PvDate = '"+sdf.format(obj.getPvDate())+"'");
		query.append(",BankKey ="+ obj.getBankKey());
		query.append(",PayeeKey ="+obj.getPayeeKey());
		query.append(",PayeeType  ='"+obj.getPayeeType()+"'");
		query.append(",Amount  ="+obj.getAmount());
		query.append(",Status  ='"+obj.getStatus()+"'");
		query.append(",[memo]  ='"+obj.getMemo()+"'");
		query.append(",BankRefKey  ="+obj.getBankRefKey());
		query.append(",QBStatus ='N'");
		query.append(",QBRefNo ='P'");
		query.append(",QBRefDate ='P'");
		query.append(",ExpClassHide ='N'");
		query.append(",ExpMemoHide ='N'");
		query.append(",ExpBillNoHide ='N'");
		query.append(",ExpBillDateHide ='N'");
		query.append(",ItemClassHide ='N'");
		query.append(",ItemDesHide ='N'");
		query.append(",ItemBillNoHide ='N'");
		query.append(",ItemBillDateHide ='N'");
		query.append(",PrintName ='"+obj.getPrintName()+"'");
		query.append(",SwiftCode ='"+obj.getSwiftCode()+"'");
		query.append(",DATEMODIFIED ='"+sdf.format(c.getTime())+"'");
		query.append(",ModifiedUserID ="+webUserID);
		query.append(" Where RecNo="+obj.getRecNo());
		return query.toString();

	}
	
	public String updateBankTransferInfo(BankTransferModel obj) {
		query = new StringBuffer();
		query.append("Update BankTransfer set " );
		query.append("Attn_Name ='"+obj.getAttnName()+"'");
		query.append(",Attn_Position = '"+obj.getAttnPosition()+"'");
		query.append(",From_ActName ='"+ obj.getFromActName()+"'");
		query.append(",From_BankName ='"+obj.getFromBankName()+"'");
		query.append(",From_ActNumber  ='"+obj.getFromActNumber()+"'");
		query.append(",From_Branch  ='"+obj.getFromBranch()+"'");
		query.append(",To_ActName  ='"+obj.getToActName()+"'");
		query.append(",To_BankName	  ='"+obj.getToBankName()+"'");
		query.append(",To_ActNumber  ='"+obj.getToActNumber()+"'");
		query.append(",To_Branch  ='"+obj.getToBranch()+"'");
		query.append(",From_IBANNo ='"+obj.getFromIBANNo()+"'");
		query.append(",To_IBANNo  ='"+obj.getToIBANNo()+"'");
		query.append(",To_TRANSCode ='"+obj.getToTRANSCode()+"'");
		query.append(" Where RecNo="+obj.getRecNo());
		return query.toString();

	}
}
