package hba;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.CashInvoiceModel;
import model.ReceiptVoucherDeatiledModel;
import model.ReceiptVoucherMastModel;

public class ReceiptVoucherQuerries {
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public String getRiceivedFrom()
	{
		query=new StringBuffer();
		query.append("Select Name,RecNo,ListID,ListType,SubLevel,FullName,isactive from QbLists Where IsActive='Y' order by ListType, Replace(FullName,':','          :')");
		return query.toString();	
	}
	
	public String getForPrintOnReceiptCustomer(int selectedKey)
	{
		query=new StringBuffer();
		query.append("Select PrintChequeAs,balance From Customer Where Cust_Key="+selectedKey+"");
		return query.toString();	
	}
	
	public String getForPrintOnReceiptVendor(int selectedKey)
	{
		query=new StringBuffer();
		query.append("Select PrintChequeAs,balance  From Vendor Where Vend_Key="+selectedKey+"");
		return query.toString();	
	}
	
	public String getForPrintOnReceiptEmployee(int SelectedKey)
	{
		query=new StringBuffer();
		query.append("Select PrintChequeAs From Employee Where Emp_Key="+SelectedKey+"");
		return query.toString();	
	}
	
	public String getForPrintOnReceiptOtherNames(int SelectedKey)
	{
		query=new StringBuffer();
		query.append("Select PrintChequeAs From OtherNames  Where OthNam_Key="+SelectedKey+"");
		return query.toString();	
	}
	
	public String getAccountCr()
	{
		query=new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],AccountType,SubLevel,Rec_No,ListID FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType where isActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();	
	}
	
	public String getGridPaymentMethodCheque()
	{
		query=new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],AccountType,SubLevel,Rec_No,ListID FROM Accounts Inner join ");
		query.append("AccountType on AccountType.TypeName = Accounts.AccountType where ");
		query.append("Not AccountType in ('AccountsPayable','AccountsReceivable') ");
		query.append("and isActive='Y' order by AccountType.SRL_No, Accounts.ACTLEVELSwithNO");
		return query.toString();	
	}
	
	public String getGridPaymentMethodCash()
	{
		query=new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],Accounts.fullname,AccountType,SubLevel,Rec_No,ListID FROM Accounts Inner join ");
		query.append("AccountType on AccountType.TypeName = Accounts.AccountType where Not AccountType in ('AccountsPayable','AccountsReceivable') ");
		query.append("and isActive='Y' order by AccountType.SRL_No,  Accounts.ACTLEVELSwithNO");
		return query.toString();	
	}
	
	public String getGridBankName()
	{
		query=new StringBuffer();
		query.append("Select Name,RecNo from Banks order by Name");
		return query.toString();
	}
	
	
	public String getAcccountForCUG()
	{
		query=new StringBuffer();
		query.append("SELECT Accounts.AccountName As [Name],AccountType    ,   SubLevel    ,   Rec_No, ListID FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType where AccountType in ('Bank') and isActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		return query.toString();	
	}
	
	public String getGridClass()
	{
		query=new StringBuffer();
		query.append("Select [Name],Class_Key,ListID,SubLevel,FullName from [Class] where isActive='Y' order by FullName");
		return query.toString();
	}
	
	
	public String getGridClassPrePoupulateForSelectedAccount(int selectedId)
	{
		query=new StringBuffer();
		query.append("SELECT Accounts.ClassKey,Class.Class_Key,Class.FullName as Class From Accounts  Inner Join Class On Accounts.ClassKey = Class.Class_Key Where Accounts.Rec_No = "+selectedId+"");
		return query.toString();
	}
	
	public String checkIfSerialNumberIsDuplicateRVfalg1(String serialNumber,int recNo)
	{
		query=new StringBuffer();
		 query.append("Select Rec_No from RVMast Where RefNumber   ='"+serialNumber+"' And Mode    in ('Both','Both-Contract') And Not Status in ('V') and rec_no !="+recNo+"");
		 return query.toString();		
	}
	
	public String checkIfSerialNumberIsDuplicateRVfalg2(String serialNumber,int recNo)
	{
		query=new StringBuffer();
		 query.append(" Select Rec_No from RVMast Where RefNumber   ='"+serialNumber+"' And Not Status in ('V') and rec_no !="+recNo+"");
		 return query.toString();		
	}
	
	public String GetRVNumberFlag1()
	{
		  query=new StringBuffer();
		  query.append(" Select * from SystemSerialNos Where SerialField ='CashChequeRV'");
		  return query.toString();
	}
	
	public String GetRVNumberFlag2()
	{
		  query=new StringBuffer();
		  query.append(" Select * from SystemSerialNos Where SerialField  ='ReceiptSerial'");
		  return query.toString();
	}
	
	public String addNewReceiptVoucherMast(ReceiptVoucherMastModel obj,int webUserID)
	{
		  query=new StringBuffer();
		  query.append("Insert into RvMast (Rec_No,ArAccountRef_Key,CustomerRef_key,ReceiptName,TxnDate,TotalAmount,[Memo],RefNumber,QBRefNo,QBRefDate,RvOrJv,Mode,SeperateJournal,ClassHide,MemoHide,JVRefNumber,Status,postasdiffered,UserID,webUserID) ");
		  query.append(" Values("+ obj.getRecNo()+","+obj.getArAccountRefKey()+","+obj.getCustRefKey()+",'"+obj.getReceiptName()+"','"+sdf.format(obj.getTxtDate())+"',");
		  query.append(""+obj.getTotalAmount()+",'"+obj.getMemo()+"','"+obj.getRefNumber()+"',"+obj.getQbNo()+",'"+obj.getQbRefDate()+"','"+obj.getRvOrJv()+"','"+obj.getMode()+"',");
		  query.append("'"+obj.getSepearateJournal()+"','"+obj.getClassHide()+"','"+obj.getMemoHide()+"','"+obj.getJvRefNumber()+"','"+obj.getStatus()+"','"+obj.getDefferedIncome()+"',"+obj.getUserId()+","+webUserID+"");
		  query.append(")");
		  query.append(" ");
		  return query.toString();
		  
		 
	}
	
	
	public String updateReceiptVoucherMast(ReceiptVoucherMastModel obj,int webUserID)
	{
		  String editedFromOnline="Y";
		  query=new StringBuffer();
		  query.append("Update RvMast set ArAccountRef_Key="+obj.getArAccountRefKey()+",CustomerRef_key="+obj.getCustRefKey()+",ReceiptName='"+obj.getReceiptName()+"',TxnDate='"+sdf.format(obj.getTxtDate())+"',");
		  query.append("TotalAmount="+obj.getTotalAmount()+",Memo='"+obj.getMemo()+"',RefNumber='"+obj.getRefNumber()+"',QBRefNo="+obj.getQbNo()+",QBRefDate='"+obj.getQbRefDate()+"',RvOrJv='"+obj.getRvOrJv()+"',Mode='"+obj.getMode()+"',");
		  query.append("SeperateJournal='"+obj.getSepearateJournal()+"',ClassHide='"+obj.getClassHide()+"',MemoHide='"+obj.getMemoHide()+"',JVRefNumber='"+obj.getJvRefNumber()+"',Status='"+obj.getStatus()+"',postasdiffered='"+obj.getDefferedIncome()+"',UserID="+obj.getUserId()+",webUserID="+webUserID+",editedFromOnline='"+editedFromOnline+"'");
		  query.append(" where rec_no="+obj.getRecNo()+"");
		  query.append(" ");
		  return query.toString();
		  
		 
	}
	
	
	public String addNewReceiptVoucherDeatiled(ReceiptVoucherDeatiledModel obj)
	{
		
		  query=new StringBuffer();
		  query.append("Insert into RvDetails(Rec_No,DepositToAccountRef_Key,Amount,[Memo],ChequeNo,Bank_Key,Line_No,PaymentMethod,ClassRef_Key,CUCAcctKey,Status,VisaCharge,VisaAmount");	
		  if(obj.getCheckDate()!=null)
		  {
			  query.append(",ChequeDate");
		  }
		  query.append(")");
		  query.append(" Values("+obj.getRecNo()+"," +obj.getDepositeToAccountRefKey()+","+obj.getAmmount()+",'"+obj.getMemo()+"','"+obj.getCheckNumber()+"',");
		  query.append(""+obj.getBankKey()+","+obj.getLineNo()+",'"+obj.getPaymentMethod()+"',"+obj.getClassRefKey()+","+obj.getCucaccuntKey()+",'"+obj.getStatus()+"',"+obj.getVisaCharge()+",");
		  query.append(""+obj.getVisaAmount()+"");
		  if(obj.getCheckDate()!=null)
		  {
			  query.append(",'"+sdf.format(obj.getCheckDate())+"'");
		  }
		  query.append(")");
		  query.append(" ");
		  return query.toString();
		  
		 
	}
	
	public String deleteReceiptVoucherGridItems(int RecNo)
	{
		  query=new StringBuffer();
		  query.append(" Delete from RvDetails Where Rec_No=" + RecNo);		  
		  return query.toString();
	}
	
	public String GetNewReceiptVoucherRecNo()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(Rec_No) as MaxRecNo from RVMAST");		
		  return query.toString();
	}
	
	public String GetJVRefNumber()
	{
		  query=new StringBuffer();
		  query.append(" Select * from SystemSerialNos Where SerialField ='JvEntry'");
		  return query.toString();
	}
	
	public String getReceiptVoucherReportValues(Date fromDate,Date toDate,int webUserID,boolean seeTrasction)
	{
		  query=new StringBuffer();
		  query.append(" SELECT RVMast.Rec_No ,RVMAST.ReceiptName,RVMAST.Status,RVMast.TimeCreated  , RVMast.RefNumber AS RvNo,  RVMAST.Mode ,RVMast.TxnDate AS RvDate,RVMast.webuserId, CustomerList.FullName AS CustomerName, ");
		  query.append(" RVArAccount.AccountNumber AS ArAccountNo,RVArAccount.Name AS ArAccountName , Banks.Name AS BankName , RVDetails.ChequeNo,Rvmast.Printed4Approval,RVDetails.ChequeDate  , RvDepositToAccount.AccountNumber AS DepositToAccountNo, RvDepositToAccount.Name AS DepositToAccountName, ");
		  query.append(" RVDetails.Amount , RVDetails.paymentmethod  , RVMast.Memo AS RvMemo,  CUCAccount.AccountNumber as CUCAcctNo      ,CUCAccount.Name as CUCAcctName , Class4BF.FullName     , Class4BF.Name as ClassName    ,Class4BF.Class_Type    ,Class4BF.SubLevel ,RVDetails.Status AS RvStatus, ");
		  query.append(" RVMast.RvOrJv AS PostToQBBy,    RVDetails.CUCAcctKey, RVDetails.Bank_Key , RVMast.TotalAmount ");
		  query.append(" FROM ((((((RVMast INNER   JOIN RVDetails ON RVMast.Rec_No = RVDetails.Rec_No)            LEFT    JOIN QBLists AS CustomerList  ON RVMast.CustomerRef_key = CustomerList.RecNo) ");  
		  query.append(" LEFT    JOIN Accounts AS RVArAccount  ON RVMast.ArAccountRef_Key  = RVArAccount.REC_NO) ");          
		  query.append(" LEFT    JOIN Accounts AS CUCAccount  ON  RVDetails.CUCAcctKey  = CUCAccount.REC_NO) ");           
		  query.append(" LEFT    JOIN Banks  ON RVDetails.Bank_Key   = Banks.RecNo)  ");          
		  query.append(" LEFT    JOIN Class As Class4BF ON RVDetails.ClassRef_Key  = Class4BF.Class_Key) ");           
		  query.append(" LEFT    JOIN Accounts AS RvDepositToAccount ON RVDetails.DepositToAccountRef_Key = RvDepositToAccount.REC_NO "); 
		  query.append(" Where RVMAST.MODE in ('Both','Both-Contract') and RVDETAILS.STATUS not  in ('V') and RVMast.TxnDate Between '"+sdf.format(fromDate)+"' And '"+sdf.format(toDate)+"'");
		  if (webUserID > 0 && !seeTrasction)
		  query.append(" and webuserID="+webUserID+"");
		  query.append(" Order by Rec_No,rvdetails.line_no");	
		  return query.toString();
	}
	
	
	public String getReceiptVoucherByID(int receiptVoucherId,int webUserID,boolean seeTrasction)
	{
		  query=new StringBuffer();
		  query.append("select * from RVMast where Rec_No="+receiptVoucherId+"");
		  if (webUserID > 0 && !seeTrasction)
		  query.append(" and webUserId="+webUserID+"");
		  return query.toString();
	}
	
	public String getReceiptVoucherGridDataByID(int receiptVoucherId)
	{
		  query=new StringBuffer();
		  query.append("select * from RvDetails where Rec_No="+receiptVoucherId+"");
		  return query.toString();
	}
	
	public String getNextRecordReceiptVoucher(int recNo,int webUserID,boolean seeTrasction)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM RVMast  WHERE Rec_No >"+recNo+"");
		  if (webUserID > 0 && !seeTrasction)
		  query.append(" and webuserid="+webUserID+" ");
		  query.append(" ORDER BY Rec_No ");
		  return query.toString();
	}
	
	public String getPreviousRecordReceiptVoucher(int recNo,int webUserID,boolean seeTrasction)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM RVMast  WHERE Rec_No <"+recNo+" ");
		  if (webUserID > 0 && !seeTrasction)
		  query.append(" and webuserid="+webUserID+" ");
		  query.append(" ORDER BY Rec_No  desc");
		  return query.toString();
	}
	
	public String getFirstRecordReceiptVoucher(int webUserID,boolean seeTrasction)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM RVMast ");
		  if (webUserID > 0 && !seeTrasction)
		  query.append(" where webuserid="+webUserID+" ");
		  query.append(" ORDER BY Rec_No");
		  return query.toString();
	}
	
	
	public String getLastRecordReceiptVoucher(int webUserID,boolean seeTrasction)
	{
		  query=new StringBuffer();
		  query.append("SELECT TOP 1 * FROM RVMast");
		  if (webUserID > 0 && !seeTrasction)
		  query.append(" where webuserid="+webUserID+" ");
		  query.append(" ORDER BY Rec_No desc");
		  return query.toString();
	}
	
	
	
	

}
