package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.BillReportModel;
import model.CheckFAItemsModel;
import model.CheckItemsModel;
import model.CompanyDBModel;
import model.CreditBillModel;
import model.DepreciationModel;
import model.ExpensesModel;
import model.FixedAssetModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class BillData {
	private Logger logger = Logger.getLogger(BillData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public BillData()
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
			logger.error("error in BillData---Init-->" , ex);
		}
	}
	
	
	public int GetBillMastRecNoQuery()
	{
		int MaxRecNo=1;
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetBillMastRecNoQuery());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---GetBillMastRecNoQuery-->" , ex);
		}
		return MaxRecNo;
	}
	
	//Fixed Asset Items
	public List<FixedAssetModel> getVendorFixedAssetItemQueryforbill(int vendorID)
	{
		List<FixedAssetModel> lst=new ArrayList<FixedAssetModel>();
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			FixedAssetModel obj=new FixedAssetModel();
			obj.setAssetid(0);
			lst.add(obj);
			
			rs=db.executeNonQuery(query.getVendorFixedAssetItemQueryforbill(vendorID));
			while(rs.next())
			{
				obj=new FixedAssetModel();
				obj.setAssetid(rs.getInt("AssetID"));
				obj.setAssetCode(rs.getString("ASsetCode"));
				obj.setAssetName(rs.getString("AssetName"));
				obj.setAssetMasterDesc(rs.getString("ASSETNAMECode"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---getVendorFixedAssetItemQueryforbill-->" , ex);
		}
		
		return lst;
	}
	
	public int addNewBill(CreditBillModel obj,int webUserID)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.addNewBill(obj,webUserID));			
		}
		catch (Exception ex) 
		{
			logger.error("error in BillData---addNewBill-->" , ex);
		}
		return result;
		
	}
	
	public int updateBill(CreditBillModel obj,int webUserID)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.updateBill(obj,webUserID));			
		}
		catch (Exception ex) 
		{
			logger.error("error in BillData---updateBill-->" , ex);
		}
		return result;
		
	}
	
	public int deleteBill(int RecNo)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.deleteBill(RecNo));			
		}
		catch (Exception ex) 
		{
			logger.error("error in BillData---deleteBill-->" , ex);
		}
		return result;
		
	}
	
	
	public int addBillExpense(ExpensesModel objExpenses ,int RecNo)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.addBillExpense(objExpenses, RecNo));			
		}
		catch (Exception ex) {
			logger.error("error in BillData---addBillExpense-->" , ex);
		}
		return result;
		
	}
	
	public int deleteBillExpense(int RecNo)
	{
		int result=0;
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.deleteBillExpense(RecNo));			
		}
		catch (Exception ex) {
			logger.error("error in BillData---deleteBillExpense-->" , ex);
		}
		return result;
		
	}
	
	public int addBillCheckItems(CheckItemsModel obj ,int RecNo)
	{
		int result=0;
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.addBillCheckItems(obj, RecNo));	
		}
		catch (Exception ex) {
			logger.error("error in BillData---addBillCheckItems-->" , ex);
		}
		return result;
		
	}
	
	public int deleteBillCheckItems(int RecNo)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.deleteBillCheckItems(RecNo));			
		}
		catch (Exception ex) {
			logger.error("error in BillData---deleteBillCheckItems-->" , ex);
		}
		return result;
		
	}
	
	public int addBillCheckFAItems(CheckFAItemsModel obj ,int RecNo)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.addBillCheckFAItems(obj, RecNo));	
		}
		catch (Exception ex) {
			logger.error("error in BillData---addBillCheckFAItems-->" , ex);
		}
		return result;
		
	}
	
	public int deleteBillCheckFAItems(int RecNo)
	{
		int result=0;
		
		BillDataQuerry query=new BillDataQuerry();		
		try 
		{			
			result=db.executeUpdateQuery(query.deleteBillCheckFAItems(RecNo));			
		}
		catch (Exception ex) {
			logger.error("error in BillData---deleteBillCheckFAItems-->" , ex);
		}
		return result;		
	}
	
	
	public int updateAssetMaster(CheckFAItemsModel obj)
	{
		int result=0;
		
		HBAQueries query=new HBAQueries();		
		try 
		{			
			result=db.executeUpdateQuery(query.updateAssetMasterQuery(obj));	
		}
		catch (Exception ex) {
			logger.error("error in BillData---updateAssetMaster-->" , ex);
		}
		return result;		
	}
	
	public int DeleteDepreciation(int AssetID)
	{
		int result=0;
		
		HBAQueries query=new HBAQueries();		
		try 
		{			
			result=db.executeUpdateQuery(query.DeleteDepreciationQuery(AssetID));			
		}
		catch (Exception ex) {
			logger.error("error in BillData---DeleteDepreciation-->" , ex);
		}
		return result;		
	}
	
	public int InsertDepreciation(int assetID,int locationId, List<DepreciationModel> lstDep)
	{
		int result=0;
		
		HBAQueries query=new HBAQueries();		
		try 
		{			
			for (DepreciationModel obj : lstDep) 
			{
				result=db.executeUpdateQuery(query.InsertDepreciationQuery(assetID, locationId, obj));
			}
				
		}
		catch (Exception ex) {
			logger.error("error in BillData---InsertDepreciation-->" , ex);
		}
		return result;		
	}
	
	
	public boolean checkIfReferanceNumberIsCreditBill(String SerialNumber,int recNo)
	{
		boolean hasSerialNumber=false;		
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfReferanceNumberIsCreditBill(SerialNumber,recNo));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---checkIfReferanceNumberIsCreditBill-->" , ex);
		}
		
		return hasSerialNumber;
	}
	
	
	public boolean checkIfBillNumberIsCreditBill(String SerialNumber,int recNo)
	{
		boolean hasSerialNumber=false;		
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfBillNumberIsCreditBill(SerialNumber,recNo));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---checkIfBillNumberIsCreditBill-->" , ex);
		}
		
		return hasSerialNumber;
	}
	
	
	public CreditBillModel getBillById(int billKey,int webUserID,boolean seeTrasction)
	{
		CreditBillModel obj=new CreditBillModel();
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getBillById(billKey,webUserID,seeTrasction));
			while(rs.next())
			{
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxnDate(rs.getDate("TxnDate"));
				obj.setTxnNumber(rs.getString("txnNumber")==null?"":rs.getString("txnNumber"));
				obj.setCr_flag(rs.getString("Cr_flag")==null?"":rs.getString("Cr_flag"));
				obj.setAddress(rs.getString("address")==null?"":rs.getString("address"));
				obj.setVendRefKey(rs.getInt("VendorRef_Key"));
				obj.setApAccountRefKey(rs.getInt("APAccountRef_Key"));
				obj.setDueDate(rs.getDate("dueDate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setAmountDue(rs.getDouble("amountDue"));
				obj.setRefNumber(rs.getString("RefNumber")==null?"":rs.getString("RefNumber"));
				obj.setTermsRefKey(rs.getInt("TermsRef_Key"));
				obj.setMemo(rs.getString("Memo"));
				obj.setIsPaid(rs.getString("ispaid")==null?"":rs.getString("ispaid"));
				obj.setBillNo(rs.getString("billNo")==null?"":rs.getString("billNo"));
				obj.setBillSource(rs.getString("Bill_Source")==null?"":rs.getString("Bill_Source"));
				obj.setStatus(rs.getString("status")==null?"":rs.getString("status"));
				obj.setUseFixedAsset(rs.getString("useFixedAsset")==null?"":rs.getString("useFixedAsset"));
				obj.setQbRefNUmber(rs.getString("QBRefNo")==null?"":rs.getString("QBRefNo"));
				obj.setAllocated(rs.getString("Allocated")==null?"":rs.getString("Allocated"));
				obj.setAllocatedType(rs.getString("Allocation_Type")==null?"":rs.getString("Allocation_Type"));
				obj.setAllocationAmount(rs.getDouble("Allocation_Amount"));
				obj.setAllocationMethod(rs.getString("Allocation_Method")==null?"":rs.getString("Allocation_Method"));
				obj.setAssetInsRecNo(rs.getInt("AssetIns_RecNo"));
				obj.setWebuserId(rs.getInt("webUserId"));
				obj.setTransformIR(rs.getString("TransformIR")==null?"":rs.getString("TransformIR"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---getBillById-->" , ex);
		}
		return obj;
	}
	/*ccccccccccccccccccccccccccccccccccc*/
	
	public List<ExpensesModel> getBillGridDataExpenseById(int billKey)
	{
		List<ExpensesModel> lst=new ArrayList<ExpensesModel>();
		
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getBillGridDataExpenseById(billKey));
			while(rs.next())
			{
				ExpensesModel obj=new ExpensesModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setSrNO(rs.getInt("line_No"));
				obj.setTxnLineId(rs.getString("txnlineId"));
				obj.setSelectedAccountKey(rs.getInt("accountsRef_Key"));
				obj.setSelectedCutomerKey(rs.getInt("custRef_key"));
				obj.setSelectedClassKey(rs.getInt("class_key"));
				obj.setFixedAssetItemid(rs.getInt("FaItemKey"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setBillable(rs.getString("billable")==null?"":rs.getString("billable"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---getBillGridDataExpenseById-->" , ex);
		}
		return lst;
	}
	
	
	public List<CheckItemsModel> getBillGridDataItemById(int billKey)
	{
		List<CheckItemsModel> lst=new ArrayList<CheckItemsModel>();
		
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
rs=db.executeNonQuery(query.getBillGridDataItemById(billKey));
			while(rs.next())
			{
				CheckItemsModel obj=new CheckItemsModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setLineNo(rs.getInt("Line_No"));
				obj.setItemKey(rs.getInt("ItemRef_Key"));
				obj.setDescription(rs.getString("Description"));
				obj.setQuantity((int)rs.getDouble("Quantity"));
				obj.setCost(rs.getDouble("Cost"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setSelectedCustKey(rs.getInt("CustRef_Key"));
				obj.setSelectedClassKey(rs.getInt("Class_Key"));
				obj.setInventorySiteKey(rs.getInt("InventorySiteKey"));
				obj.setFixedIteKey(rs.getInt("FAItemKey"));
				obj.setBillable(rs.getString("billable")==null?"":rs.getString("billable"));
				lst.add(obj);
				
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---getBillGridDataItemById-->" , ex);
		}
		return lst;
	}
	
	
	
	public List<CheckFAItemsModel> getBillGridDataFAById(int billKey)
	{
		List<CheckFAItemsModel> lst=new ArrayList<CheckFAItemsModel>();
		
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getBillGridDataFAById(billKey));
			while(rs.next())
			{
				CheckFAItemsModel obj=new CheckFAItemsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setFaItemKey(rs.getInt("FaItemKey"));
				obj.setDescription(rs.getString("Description"));
				obj.setBillNo(rs.getString("billNo"));
				obj.setInvoiceDate(rs.getDate("invoiceDate"));
				obj.setQuantity((int)rs.getDouble("quantity"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setUnitPrice(rs.getDouble("unitPrice"));
				obj.setOtherCharges(rs.getDouble("otherCharges"));
				obj.setCustomerKey(rs.getInt("custKey"));
				obj.setCustodyKey(rs.getInt("custodyKey"));
				lst.add(obj);
				
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---getBillGridDataFAById-->" , ex);
		}
		return lst;
	}
	
	
	public CreditBillModel navigationBill(int billKey,int webUserID,boolean seeTrasction,String navigation,String actionTYpe)
	{
		CreditBillModel obj=new CreditBillModel();
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			if(navigation.equalsIgnoreCase("prev") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view")))
			{
				rs=db.executeNonQuery(query.getPreviousRecordBill(billKey,webUserID,seeTrasction));
			}
			else if(navigation.equalsIgnoreCase("next")&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view")))
			{
				rs=db.executeNonQuery(query.getNextRecordbill(billKey,webUserID,seeTrasction));
			}
			else if(navigation.equalsIgnoreCase("next")&& actionTYpe.equalsIgnoreCase("create"))
			{
				rs=db.executeNonQuery(query.getFirstRecordBill(webUserID,seeTrasction));
			}
			else if(navigation.equalsIgnoreCase("prev")&& actionTYpe.equalsIgnoreCase("create"))
			{
				rs=db.executeNonQuery(query.getLastRecordBill(webUserID,seeTrasction));
			}
			while(rs.next())
			{
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxnDate(rs.getDate("TxnDate"));
				obj.setTxnNumber(rs.getString("txnNumber")==null?"":rs.getString("txnNumber"));
				obj.setCr_flag(rs.getString("Cr_flag")==null?"":rs.getString("Cr_flag"));
				obj.setAddress(rs.getString("address")==null?"":rs.getString("address"));
				obj.setVendRefKey(rs.getInt("VendorRef_Key"));
				obj.setApAccountRefKey(rs.getInt("APAccountRef_Key"));
				obj.setDueDate(rs.getDate("dueDate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setAmountDue(rs.getDouble("amountDue"));
				obj.setRefNumber(rs.getString("RefNumber")==null?"":rs.getString("RefNumber"));
				obj.setTermsRefKey(rs.getInt("TermsRef_Key"));
				obj.setMemo(rs.getString("Memo"));
				obj.setIsPaid(rs.getString("ispaid")==null?"":rs.getString("ispaid"));
				obj.setBillNo(rs.getString("billNo")==null?"":rs.getString("billNo"));
				obj.setBillSource(rs.getString("Bill_Source")==null?"":rs.getString("Bill_Source"));
				obj.setStatus(rs.getString("status")==null?"":rs.getString("status"));
				obj.setUseFixedAsset(rs.getString("useFixedAsset")==null?"":rs.getString("useFixedAsset"));
				obj.setQbRefNUmber(rs.getString("QBRefNo")==null?"":rs.getString("QBRefNo"));
				obj.setAllocated(rs.getString("Allocated")==null?"":rs.getString("Allocated"));
				obj.setAllocatedType(rs.getString("Allocation_Type")==null?"":rs.getString("Allocation_Type"));
				obj.setAllocationAmount(rs.getDouble("Allocation_Amount"));
				obj.setAllocationMethod(rs.getString("Allocation_Method")==null?"":rs.getString("Allocation_Method"));
				obj.setAssetInsRecNo(rs.getInt("AssetIns_RecNo"));
				obj.setWebuserId(rs.getInt("webUserId"));
				obj.setTransformIR(rs.getString("TransformIR")==null?"":rs.getString("TransformIR"));
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---navigationBill-->" , ex);
		}
		return obj;
	}
	
	
	public List<BillReportModel> getBillReport(int webUserId,boolean seeTrasction,Date fromDate,Date toDate)
	{
		List<BillReportModel> lst=new ArrayList<BillReportModel>();
		
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getBillreport(webUserId,seeTrasction,fromDate,toDate));
			while(rs.next())
			{
				BillReportModel obj=new BillReportModel();
				obj.setTxnDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("txnDate")));
				obj.setDueDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("dueDate")));
				obj.setWebUserId(rs.getInt("webuserID"));
				String stat=rs.getString("status")==null?"":rs.getString("status");
				if(stat.equalsIgnoreCase("C"))
				{
					obj.setStatus("Created");
				}
				else if(stat.equalsIgnoreCase("A"))
				{
					obj.setStatus("Approved");
				}
				obj.setAccountName(rs.getString("accountName")==null?"":rs.getString("accountName"));
				obj.setAccountNUmber(rs.getString("accountNumber")==null?"":rs.getString("accountNumber"));
				obj.setVendor(rs.getString("vendor")==null?"":rs.getString("vendor"));
				//obj.setItemName(rs.getString("itemName")==null?"":rs.getString("itemName"));
				//obj.setDescription(rs.getString("description")==null?"":rs.getString("description"));
				//obj.setQuantity((int)rs.getDouble("quantity"));
				obj.setMainMemo(rs.getString("Memo")==null?"":rs.getString("Memo"));
				//obj.setCustomer(rs.getString("customer")==null?"":rs.getString("customer"));
				//obj.setClassName(rs.getString("className")==null?"":rs.getString("className"));
				obj.setAmount(String.valueOf(rs.getDouble("amount")));
				obj.setAmountDue(String.valueOf(rs.getDouble("amountDue")));
				obj.setcRFlag(rs.getString("cr_Flag")==null?"":rs.getString("cr_Flag"));
				obj.setRefNumber(rs.getString("refNumber")==null?"":rs.getString("refNumber"));
				obj.setBillNo(rs.getString("billNo")==null?"":rs.getString("billNo"));
				obj.setMastRecNo(rs.getInt("Rec_No"));
				lst.add(obj);
				
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---getBillReport-->" , ex);
		}
		return lst;
	}
	
	
	public List<QbListsModel> fillQbItemsList()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		BillDataQuerry query=new BillDataQuerry();
		ResultSet rs = null;
		try 
		{
			QbListsModel obj=new QbListsModel();
			rs=db.executeNonQuery(query.GetQBItemsQuery());
			while(rs.next())
			{
				obj=new QbListsModel();
				obj.setRecNo(rs.getInt("item_key"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ItemType"));
				obj.setBarcode(rs.getString("BARCODE"));
				obj.setSalesDesc(rs.getString("salesdesc")==null?"":rs.getString("salesdesc"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in BillData---fillQbItemsList-->" , ex);
		}
		return lst;
	}
	

}
