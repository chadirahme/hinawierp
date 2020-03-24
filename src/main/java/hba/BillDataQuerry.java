package hba;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.CheckFAItemsModel;
import model.CheckItemsModel;
import model.CreditBillModel;
import model.ExpensesModel;

public class BillDataQuerry {

	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	//Fixed Asset Items
			public String getVendorFixedAssetItemQueryforbill(int vendorID)
			{
				query=new StringBuffer();
				query.append(" Select AssetID,ASsetCode,AssetName, CAse WHEN isnull(AssetCode,'') = '' THen AssetName Else AssetCode + '·' + AssetName END as ASSETNAMECode ");
				query.append(" FROM ASSETMASTER Where STatusID NOT IN (1,2) ");
				query.append(" and ASSETMASTER.VendorID = " + vendorID+" order by ASsetCode,AssetName SELECT sitename,itemkey,'Inventory Site' as ItemType,0 as sublevel,ListID FROM InventorySiteList where IsActive='Y' and SiteName <> 'Unspecified Site' order by siteName");
				return query.toString();	
			}
	
			//Save New Cheque Payment
			public String GetBillMastRecNoQuery()
			{
				  query=new StringBuffer();
				  query.append(" SELECT max(Rec_No) as MaxRecNo from Bill");		
				  return query.toString();
			}
			
			
			public String addNewBill(CreditBillModel obj,int webUserID)
			{
				  //DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		 
				  query=new StringBuffer();
				  query.append("Insert into Bill  (Rec_No,TxnID,CR_Flag,APAccountRef_Key,VendorRef_Key,TermsRef_Key,TxnDate,DueDate,Amount,RefNumber,BillNo,[Memo],IsPaid,");	
				  query.append(" Bill_Source,QBRefNo,Allocated,Allocation_Method,Allocation_Type,Allocation_Amount,AssetIns_RecNo,Status,Address,transformIR,webUserID)");
				  query.append(" Values(" + obj.getRecNo() + ",'" + obj.getTxtnId()+"','" + obj.getCr_flag() +"' ,"+obj.getApAccountRefKey()+","+ obj.getVendRefKey() + " , ");
				  query.append("" +obj.getTermsRefKey() + ", '" + sdf.format(obj.getTxnDate()) + "' ,'" + sdf.format(obj.getDueDate()) + "' , " + obj.getAmount() + " , '" +obj.getRefNumber() + "' , " );
				  query.append(" '" + obj.getBillNo() + "' , '" + obj.getMemo() + "','" + obj.getIsPaid() + "', '" + obj.getBillSource() + "' , '" + obj.getQbRefNUmber() + "' , ");
				  query.append(" '" + obj.getAllocated() + "' , '"+ obj.getAllocationMethod() + "' , '" + obj.getAllocatedType() + "' , " + obj.getAllocationAmount() + ", " + obj.getAssetInsRecNo() +" ,");
				  query.append(" '" + obj.getStatus() + "' , '" + obj.getAddress()+ "' , '" + obj.getTransformIR() + "',"+webUserID+"");
				  query.append(" )");
				  query.append(" ");
				  return query.toString();
			}
			
			
			public String updateBill(CreditBillModel obj,int webUserID)
			{
				  query=new StringBuffer();
				  String editedFromOnline="Y";
				  query.append("Update Bill set TxnDate='"+sdf.format(obj.getTxnDate())+"',DueDate='"+sdf.format(obj.getDueDate())+"',CR_Flag='"+obj.getCr_flag()+"',APAccountRef_Key="+obj.getApAccountRefKey()+",");
				  query.append("VendorRef_Key="+obj.getVendRefKey()+",TermsRef_Key="+obj.getTermsRefKey()+",Amount="+obj.getAmount()+",RefNumber='"+obj.getRefNumber()+"',[Memo]='"+obj.getMemo()+"',");	
				  query.append("BillNo='"+obj.getBillNo()+"',IsPaid='"+obj.getIsPaid()+"',Bill_Source='"+obj.getBillSource()+"',QBRefNo='"+obj.getQbRefNUmber()+"',Allocated='"+obj.getAllocated()+"',");
				  query.append("Allocation_Method='"+obj.getAllocationMethod()+"',Allocation_Type='"+obj.getAllocatedType()+"',AssetIns_RecNo="+obj.getAssetInsRecNo()+",Allocation_Amount="+obj.getAllocationAmount()+",Status='"+obj.getStatus()+"',");
				  query.append("webUserId="+webUserID+",editedFromOnline='"+editedFromOnline+"' where Rec_No="+obj.getRecNo()+"");
				  query.append(" ");
				  return query.toString();
			}
			
			
			public String deleteBill(int RecNo)
			{
				  query=new StringBuffer();
				  query.append(" Delete from Bill Where Rec_No=" + RecNo);		  
				  return query.toString();
			}
			
			
			
			public String addBillExpense(ExpensesModel objExpenses ,int RecNo)
			{
				  query=new StringBuffer();		 
				  String memo="";
				  if(objExpenses.getMemo()!=null)
				  {
					  memo=objExpenses.getMemo().replace( "'", "`");
				  }
				  query.append(" Insert into BillExpenses(Rec_No,AccountsRef_Key,Amount,[Memo],CustRef_Key,Class_Key,[Line_No],FaItemKey,AssetIns_RecNo,Billable) ");
				  query.append(" values(" + RecNo + ", " + objExpenses.getSelectedAccount().getRec_No() + ", " + objExpenses.getAmount() + ", '" + memo + "',");
				  if(objExpenses.getSelectedCustomer()!=null)
				  query.append(objExpenses.getSelectedCustomer().getRecNo());
				  else			 
				  query.append("0");
				  if(objExpenses.getSelectedClass()!=null)
				  query.append(", " + objExpenses.getSelectedClass().getClass_Key());
				  else
				  query.append(", 0");		  
				  query.append(", " + objExpenses.getSrNO());
				  if(objExpenses.getSelectedFixedAsset()!=null)
				  {
				  query.append(", "  + objExpenses.getSelectedFixedAsset().getAssetid());
				  }
				  else
				  {
				  query.append(", 0");
				  }
				  query.append(",0, '"+objExpenses.getBillable()+"'");
				  query.append(" )");
				  return query.toString();
			}
			
			//Expenses
			public String deleteBillExpense(int RecNo)
			{
				  query=new StringBuffer();
				  query.append(" Delete from BillExpenses  Where Rec_No=" + RecNo);		  
				  return query.toString();
			}
			
			public String addBillCheckItems(CheckItemsModel obj ,int RecNo)
			{
				 query=new StringBuffer();
				 String desc="";
				  if(obj.getDescription()!=null)
				  {
					  desc=obj.getDescription().replace( "'", "`");
				  }
				 query.append(" Insert into BillItems(Rec_No, ItemRef_Key,Description,Quantity,Cost,Amount,CustRef_Key,Class_Key,[Line_No],FAItemKey,InventorySiteKey,IsAllocated,Allocated_Amount,Allocated_UnitCost,NetTotal,Billable)");
				 query.append(" values(" + RecNo + ", " + obj.getSelectedItems().getRecNo() + ", '" + desc + "', " + obj.getQuantity() + ", ");
				 query.append(" " + obj.getCost() + ", " + obj.getAmount() + ", ");
				 if(obj.getSelectedCustomer()!=null)
					 query.append(obj.getSelectedCustomer().getRecNo());
				 else
					 query.append("0");
				 if(obj.getSelectedClass()!=null)
					 query.append(", " + obj.getSelectedClass().getClass_Key());
				 else
					 query.append(", 0");		 
				 query.append(", " + obj.getLineNo());
				 if(obj.getSelectedFixedAsset()!=null)
					 query.append(", "  + obj.getSelectedFixedAsset().getAssetid());
				 else
					 query.append(", 0");
				 
				 if(obj.getSelectedInvcCutomerGridInvrtySiteNew()!=null && obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()>0)
					 query.append(", "  + obj.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo());
				 else
					 query.append(", 0");
				 
				 query.append(",''");
				 query.append(",0");
				 query.append(",0");
				 query.append(",0");
				 query.append(", '" +obj.getBillable()+ "')" );			
				 
				 return query.toString();
			}
			
			public String deleteBillCheckItems(int RecNo)
			{
				  query=new StringBuffer();
				  query.append(" Delete from BillItems  Where Rec_No=" + RecNo);		  
				  return query.toString();
			}
			
			
			public String addBillCheckFAItems(CheckFAItemsModel obj ,int RecNo)
			{
				 query=new StringBuffer();
				 String desc="";
				  if(obj.getDescription()!=null)
				  {
					  desc=obj.getDescription().replace( "'", "`");
				  }
				 query.append(" Insert into BillFAItems (RecNo,[LineNo], FaItemKey,Description,BillNo,CustKey,CustodyKey,InvoiceDate,Quantity,UnitPrice,OtherCharges,Amount)");
				 query.append(" values(" + RecNo + ", " +  obj.getLineNo() + ", " + obj.getSelectedFixedAsset().getAssetid() + ", '" + desc + "' ");
				 String billNo=obj.getBillNo()==null?"":obj.getBillNo();
				 query.append(", '" +billNo + "', " );
				 	
				 if(obj.getSelectedCustomer()!=null)
					 query.append(obj.getSelectedCustomer().getRecNo());
				 else
					 query.append("0");
				 if(obj.getSelectedCustody()!=null)
					 query.append(", " + obj.getSelectedCustody().getRecNo());
				 else
					 query.append(", 0");		 
				 if(obj.getInvoiceDate()!=null)
					 query.append(", '" + sdf.format(obj.getInvoiceDate()) + "'");
					  else
				      query.append(", NULL");		 
				 query.append(", " + obj.getQuantity() + ", " + obj.getUnitPrice() + ", " + obj.getOtherCharges() + ", " + obj.getAmount() + ")");
				 							
				 return query.toString();
			}
			
			
			public String deleteBillCheckFAItems(int RecNo)
			{
				  query=new StringBuffer();
				  query.append(" Delete from BillFAItems  Where RecNo=" + RecNo);		  
				  return query.toString();
			}
			
			
			public String checkIfReferanceNumberIsCreditBill(String serialNumber,int recNO)
			{
				query=new StringBuffer();
				 query.append(" Select * From  Bill Where RefNumber ='" + serialNumber +"' and rec_no !="+recNO);
				 return query.toString();		
			}
			
			public String checkIfBillNumberIsCreditBill(String serialNumber,int recNO)
			{
				query=new StringBuffer();
				 query.append("  Select * From  Bill Where BillNo ='" + serialNumber +"' and rec_no !="+recNO);
				 return query.toString();		
			}
			
			
			public String getBillById(int billKey,int webUserID,boolean seeTrasction)
			{
				  query=new StringBuffer();
				  query.append("select * from Bill  where rec_no="+billKey+"");
				  if (webUserID > 0 && !seeTrasction)
				  query.append(" and webUserId="+webUserID+"");
				  return query.toString();
			}
			
			public String getBillGridDataExpenseById(int billKey)
			{
				  query=new StringBuffer();
				  query.append("select * from BillExpenses where rec_No="+billKey+"");
				  return query.toString();
			}
			
			public String getBillGridDataItemById(int billKey)
			{
				  query=new StringBuffer();
				  query.append("select * from BillItems where rec_No="+billKey+"");
				  return query.toString();
			}
			
			public String getBillGridDataFAById(int billKey)
			{
				  query=new StringBuffer();
				  query.append("select * from BillFAItems where recNo="+billKey+"");
				  return query.toString();
			}
			
			public String getNextRecordbill(int recNo,int webUserID,boolean seeTrasction)
			{
				  query=new StringBuffer();
				  query.append("SELECT TOP 1 * FROM Bill   WHERE rec_no >"+recNo+"");
				  if (webUserID > 0 && !seeTrasction)
				  query.append(" and webuserid="+webUserID+" ");
				  query.append(" ORDER BY rec_no ");
				  return query.toString();
			}
			
			public String getPreviousRecordBill(int recNo,int webUserID,boolean seeTrasction)
			{
				  query=new StringBuffer();
				  query.append("SELECT TOP 1 * FROM Bill   WHERE rec_no <"+recNo+" ");
				  if (webUserID > 0 && !seeTrasction)
				  query.append(" and webuserid="+webUserID+" ");
				  query.append(" ORDER BY rec_no  desc");
				  return query.toString();
			}
			
			public String getFirstRecordBill(int webUserID,boolean seeTrasction)
			{
				  query=new StringBuffer();
				  query.append("SELECT TOP 1 * FROM Bill  ");
				  if (webUserID > 0 && !seeTrasction)
				  query.append(" where webuserid="+webUserID+" ");
				  query.append(" ORDER BY rec_no");
				  return query.toString();
			}
			
			
			public String getLastRecordBill(int webUserID,boolean seeTrasction)
			{
				  query=new StringBuffer();
				  query.append("SELECT TOP 1 * FROM Bill ");
				  if (webUserID > 0 && !seeTrasction)
				  query.append(" where webuserid="+webUserID+" ");
				  query.append(" ORDER BY rec_no desc");
				  return query.toString();
			}
			
			
			/*public String getBillreport(int webUserId,boolean seeTrasction,Date fromDate,Date toDate)
			{
				  query=new StringBuffer();

				  query.append("SELECT Bill.TxnDate,Bill.webuserID,Bill.Rec_No as MastRecNo, Bill.BillNo ,Bill.RefNumber,Bill.CR_Flag,Bill.status,Bill.DueDate,Bill.Amount,Bill.AmountDue,Accounts.AccountNumber, Accounts.Name AS AccountName,");
				  query.append("QBListsVendor.Name AS Vendor, QBItems.Name AS ItemName,BillItems.Description, BillItems.Quantity, Bill.Memo as MainMemo,QBListsCustomer.Name AS Customer, Class.Name AS ClassName FROM (((((Bill  LEFT JOIN BillItems ON Bill.Rec_No = BillItems.Rec_No)");
				  query.append("LEFT JOIN Accounts ON Bill.APAccountRef_Key = Accounts.REC_NO) LEFT JOIN QBLists AS QBListsVendor ON Bill.VendorRef_Key = QBListsVendor.RecNo) LEFT JOIN QBItems ON BillItems.ItemRef_Key = QBItems.Item_Key) LEFT JOIN QBLists AS QBListsCustomer ON BillItems.CustRef_Key = QBListsCustomer.RecNo)");
				  query.append("LEFT JOIN Class ON BillItems.Class_Key = Class.Class_Key where Bill.Rec_No <>0 And Bill.CR_Flag='C'"); 
				  query.append(" and Bill.TxnDate Between '"+sdf.format(fromDate)+"' And '"+sdf.format(toDate)+"' ");
				  if (webUserId > 0 && !seeTrasction)
					    	 query.append(" and Bill.webUserId="+webUserId+"");
			      query.append("ORDER BY Bill.BillNo ");
				  return query.toString();
			}*/
			
			public String getBillreport(int webUserId,boolean seeTrasction,Date fromDate,Date toDate)
			{
				  query=new StringBuffer();

				  query.append("SELECT Bill.*,Accounts.AccountNumber, Accounts.Name AS AccountName,QBListsVendor.Name AS Vendor ");
				  query.append("FROM (Bill  LEFT JOIN Accounts ON Bill.APAccountRef_Key = Accounts.REC_NO) ");
				  query.append("LEFT JOIN QBLists AS QBListsVendor ON Bill.VendorRef_Key = QBListsVendor.RecNo ");
				  query.append(" where Bill.Rec_No <>0 And Bill.CR_Flag='C'"); 
				  query.append(" and Bill.TxnDate Between '"+sdf.format(fromDate)+"' And '"+sdf.format(toDate)+"' ");
				  if (webUserId > 0 && !seeTrasction)
					    	 query.append(" and Bill.webUserId="+webUserId+"");
			      query.append("ORDER BY Bill.BillNo ");
				  return query.toString();
			}
			
			
			public String GetQBItemsQuery()
			{
				  query=new StringBuffer();
				  query.append(" SELECT name,item_key,ListID,ItemType,SubLevel,FullName,BARCODE,salesdesc FROM QBItems ");
				  query.append(" where isnull(PricePercent,0)=0 and IsActive='Y' and ItemType!='itemDiscount' order by ItemType desc,FullName ");
				  return query.toString();
			}
			
	
}
