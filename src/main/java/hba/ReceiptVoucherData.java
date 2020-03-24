package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.AccountsModel;
import model.BanksModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.ClassModel;
import model.CompanyDBModel;
import model.CustomerModel;
import model.QbListsModel;
import model.ReceiptVoucherDeatiledModel;
import model.ReceiptVoucherMastModel;
import model.RecieptVoucherReportModel;
import model.SerialFields;
import model.VendorModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ReceiptVoucherData {
	
	
	private Logger logger = Logger.getLogger(ReceiptVoucherData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public ReceiptVoucherData()
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
			logger.error("error in ReceiptVoucherData---Init-->" , ex);
		}
	}
	
		
	public List<QbListsModel> getRiceivedFrom()
	{

		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getRiceivedFrom());
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ListType"));
				
				
				if(rs.getString("isactive").equalsIgnoreCase("Y"))
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
			logger.error("error in ReceiptVoucherData---getRiceivedFrom-->" , ex);
		}
		return lst;
	}
	
	public VendorModel getForPrintOnReceiptCustomer(int id)
	{
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		VendorModel str=new VendorModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getForPrintOnReceiptCustomer(id));
			while(rs.next())
			{
				
				str.setName(rs.getString("PrintChequeAs"));
				str.setBalance(rs.getDouble("balance"));
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getForPrintOnReceiptCustomer-->" , ex);
		}
		return str;
	}
	
	public VendorModel getForPrintOnReceiptVendor(int id)
	{

		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		VendorModel str=new VendorModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getForPrintOnReceiptVendor(id));
			while(rs.next())
			{
				str.setName(rs.getString("PrintChequeAs"));
				str.setBalance(rs.getDouble("balance"));
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getForPrintOnReceiptVendor-->" , ex);
		}
		return str;
	}
	
	public String getForPrintOnReceiptEmployee(int id)
	{

		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		String str=new String();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getForPrintOnReceiptEmployee(id));
			while(rs.next())
			{
				str=rs.getString("PrintChequeAs");
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getForPrintOnReceiptEmployee-->" , ex);
		}
		return str;
	}
	
	public String getForPrintOnReceiptOtherNames(int id)
	{
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		String str=new String();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getForPrintOnReceiptOtherNames(id));
			while(rs.next())
			{
				str=rs.getString("PrintChequeAs");
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getForPrintOnReceiptOtherNames-->" , ex);
		}
		return str;
	}
	
	
	public List<AccountsModel> getAccountCr()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getAccountCr());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
			//	obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("Name"));
				obj.setAccountType(rs.getString("AccountType"));
				//obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
			//	obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getAccountCr-->" , ex);
		}
		return lst;
	}
	
	public List<AccountsModel> getGridPaymentMethodCash()
	{
List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getGridPaymentMethodCash());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
			//	obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("name"));
				obj.setAccountType(rs.getString("AccountType"));
			//	obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getGridPaymentMethodCash-->" , ex);
		}
		return lst;
	}
	
	
	
	public List<BanksModel> getGridBankName()
	{
		List<BanksModel> lst=new ArrayList<BanksModel>();
		
		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getBanksListQuery());
			while(rs.next())
			{
				BanksModel obj=new BanksModel();
				obj.setRecno(rs.getInt("RecNo"));
				obj.setBankName(rs.getString("name"));				
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getGridBankName-->" , ex);
		}
		return lst;
	}
	
	
	public List<AccountsModel> getAcccountForCUG()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getAcccountForCUG());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
			//	obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("name"));
				obj.setAccountType(rs.getString("AccountType"));
				//obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				//obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getAcccountForCUG-->" , ex);
		}
		return lst;
	}
	
	
	public List<ClassModel> getGridClass()
	{
		List<ClassModel> lst=new ArrayList<ClassModel>();
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getGridClass());
			while(rs.next())
			{
				ClassModel obj=new ClassModel();
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("sublevel"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getGridClass-->" , ex);
		}
		return lst;
	}
	
	
	public ClassModel getGridClassPrePoupulateForSelectedAccount(int selectedId)
	{
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ClassModel obj=new ClassModel();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getGridClassPrePoupulateForSelectedAccount(selectedId));
			while(rs.next())
			{
				
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("class"));
			
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getGridClassPrePoupulateForSelectedAccount-->" , ex);
		}
		return obj;
	}
	
	
	public boolean checkIfSerialNumberIsDuplicateRVfalg1(String SerialNumber,int recNo)
	{
		boolean hasSerialNumber=false;		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfSerialNumberIsDuplicateRVfalg1(SerialNumber,recNo));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---checkIfSerialNumberIsDuplicateRVfalg1-->" , ex);
		}
		
		return hasSerialNumber;
	}
	
	public boolean checkIfSerialNumberIsDuplicateRVfalg2(String SerialNumber,int recNo)
	{
		boolean hasSerialNumber=false;		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfSerialNumberIsDuplicateRVfalg2(SerialNumber,recNo));
			while(rs.next())
			{
				hasSerialNumber=true;
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---checkIfSerialNumberIsDuplicateRVfalg2-->" , ex);
		}
		
		return hasSerialNumber;
	}
	
	public String GetRVNumberFlag1()
	{
		String LastNumber="1";
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetRVNumberFlag1());
			while(rs.next())
			{
				LastNumber=rs.getString("LastNumber");						
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---GetRVNumberFlag1-->" , ex);
		}
		return LastNumber;
	}
	
	public String GetRVNumberFlag2()
	{
		String LastNumber="1";
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetRVNumberFlag2());
			while(rs.next())
			{
				LastNumber=rs.getString("LastNumber");						
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---GetRVNumberFlag2-->" , ex);
		}
		return LastNumber;
	}
	
	
	public int addNewReceiptVoucherMast(ReceiptVoucherMastModel obj,int webUserID)
	{
		int result=0;
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.addNewReceiptVoucherMast(obj,webUserID));			
		}
		catch (Exception ex) 
		{
			logger.error("error in ReceiptVoucherData---addNewReceiptVoucherMast-->" , ex);
		}
		return result;		
	}
	
	public int updateRecieptVoucher(ReceiptVoucherMastModel obj,int webUserID)
	{
		int result=0;
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.updateReceiptVoucherMast(obj,webUserID));			
		}
		catch (Exception ex) 
		{
			logger.error("error in ReceiptVoucherData---updateRecieptVoucher-->" , ex);
		}
		return result;		
	}
	
	public int addNewReceiptVoucherDeatiled(ReceiptVoucherDeatiledModel obj)
	{
		int result=0;
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.addNewReceiptVoucherDeatiled(obj));			
		}
		catch (Exception ex) 
		{
			logger.error("error in ReceiptVoucherData---addNewReceiptVoucherDeatiled-->" , ex);
		}
		return result;		
	}
	
	public int deleteReceiptVoucherGridItems(int RecNo)
	{
		int result=0;
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();		
		try 
		{			
			result=db.executeUpdateQuery(query.deleteReceiptVoucherGridItems(RecNo));			
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---deleteCashInvoiceGridItems-->" , ex);
		}
		return result;
		
	}
	
	
	public int GetNewReceiptVoucherRecNo()
	{
		int MaxRecNo=1;
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetNewReceiptVoucherRecNo());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---GetNewReceiptVoucherRecNo-->" , ex);
		}
		return MaxRecNo;
	}
	
	public String GetJVRefNumber()
	{
		String LastNumber="1";
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetJVRefNumber());
			while(rs.next())
			{
				LastNumber=rs.getString("LastNumber");						
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---GetRVNumberFlag2-->" , ex);
		}
		return LastNumber;
	}
	
	//Querry for Receipt Voucher report.
	
	public List<RecieptVoucherReportModel> getReceiptVoucherReportValues(Date fromDate,Date toDate,int webUserID,boolean seeTrasction)
	{
		List<RecieptVoucherReportModel> lst=new ArrayList<RecieptVoucherReportModel>();
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getReceiptVoucherReportValues(fromDate,toDate,webUserID,seeTrasction));
			while(rs.next())
			{
				RecieptVoucherReportModel obj=new RecieptVoucherReportModel();
				obj.setRecNO(rs.getInt("rec_no"));
				obj.setReceiptName(rs.getString("receiptname").equalsIgnoreCase("null")?"":rs.getString("receiptname"));
				obj.setStatus(rs.getString("status")==null?"":rs.getString("status"));
				obj.setRvNumber(rs.getString("rvNo")==null?"":rs.getString("rvNo"));
				obj.setMode(rs.getString("mode")==null?"":rs.getString("mode"));
				obj.setRvDate(rs.getString("rvdate")==null?"":new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("rvdate")));
				obj.setCustomerName(rs.getString("customerName")==null?"":rs.getString("customerName"));
				obj.setArAccountName(rs.getString("arAccountName")==null?"":rs.getString("arAccountName"));
				obj.setArAcountNUmber(rs.getInt("arAccountNO"));
				obj.setBankName(rs.getString("bankname")==null?"":rs.getString("bankname"));
				obj.setCheckNUmber(rs.getString("chequeNo")==null?"":rs.getString("chequeNo"));
				obj.setPrintForApproval(rs.getString("printed4approval")==null?"":rs.getString("printed4approval"));
				obj.setCheckDate(rs.getString("chequeDate")==null?"":new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("chequeDate")));
				obj.setDepositeToAccountName(rs.getString("depositToaccountNO")==null?"":rs.getString("depositToaccountNO"));
				obj.setDepositeToAccountNo(rs.getString("depositToaccountName")==null?"":rs.getString("depositToaccountName"));
				obj.setAmmount(rs.getDouble("amount"));
				obj.setPaymenentMethod(rs.getString("paymentMethod")==null?"":rs.getString("paymentMethod"));
				obj.setRvMemo(rs.getString("rvmemo").equalsIgnoreCase("null")?"":rs.getString("rvmemo"));
				obj.setCucAccountName(rs.getString("cucacctName")==null?"":rs.getString("cucacctName"));
				obj.setCucAccountNumbner(rs.getString("cucAcctno")==null?"":rs.getString("cucAcctno"));
				obj.setFullname(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setClassName(rs.getString("className")==null?"":rs.getString("className"));
				obj.setClassTyep(rs.getString("class_type")==null?"":rs.getString("class_type"));
				obj.setWebUserId(rs.getInt("webUserID"));
				if(obj.getClassTyep().equalsIgnoreCase("F"))
				{
					String[] array=rs.getString("fullname").split(":");
					if(null!=array)
					{
						int i=0;
						for(i=0;i<array.length;i++)
						{
							if(i==array.length-2)
							{
								obj.setBulidingName(array[i]);
								break;
							}
						}
					}
					obj.setUnitNumber(obj.getClassName());
				}
				else if(obj.getClassTyep().equalsIgnoreCase("B"))
				{
					obj.setBulidingName(obj.getClassName());
					obj.setUnitNumber("");
				}
				if(obj.getStatus().equalsIgnoreCase("C"))
				{
					obj.setStatus("Created");
				}
				else if(obj.getStatus().equalsIgnoreCase("A"))
				{
					obj.setStatus("Approved");
				}
				else if(obj.getStatus().equalsIgnoreCase("P"))
				{
					obj.setStatus("Printed");
				}
				
				obj.setSubLevel(rs.getInt("sublevel"));
				obj.setrVStatus(rs.getString("rvstatus")==null?"":rs.getString("rvstatus"));
				obj.setPostQbBy(rs.getString("posttoqbby")==null?"":rs.getString("posttoqbby"));
				if(obj.getPostQbBy().equalsIgnoreCase("R"))
				{
					obj.setPostQbBy("Receipt Voucher");
				}
				else
				{
					obj.setPostQbBy("Journal Voucher");
				}
				obj.setCucAccountKey(rs.getInt("cucacctkey"));
				obj.setBankKey(rs.getInt("bank_key"));
				obj.setTotalAmount(rs.getDouble("totalAmount"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getReceiptVoucherReportValues-->" , ex);
		}
		return lst;
	}
	
	
	public ReceiptVoucherMastModel getReceiptVoucherById(int receiptVoucherId,int webUserID,boolean seeTrasction)
	{
		ReceiptVoucherMastModel obj=new ReceiptVoucherMastModel();
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getReceiptVoucherByID(receiptVoucherId,webUserID,seeTrasction));
			while(rs.next())
			{
				obj.setRecNo(rs.getInt("rec_no"));
				obj.setArAccountRefKey(rs.getInt("ArAccountRef_Key"));
				obj.setCustRefKey(rs.getInt("CustomerRef_key"));
				obj.setReceiptName(rs.getString("ReceiptName")==null?"":rs.getString("ReceiptName"));
				obj.setTxtDate(rs.getDate("TxnDate"));
				obj.setTxnId(rs.getString("TxnID")==null?"":rs.getString("TxnID"));
				obj.setTotalAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("Memo").equalsIgnoreCase("null")?"":rs.getString("Memo"));
				obj.setRefNumber(rs.getString("RefNumber")==null?"":rs.getString("RefNumber"));
				obj.setQbRefNo(rs.getString("QBRefNo"));
				obj.setQbRefDate(rs.getString("QBRefDate")==null?"":rs.getString("QBRefDate"));
				obj.setRvOrJv(rs.getString("RvOrJv")==null?"":rs.getString("RvOrJv"));
				obj.setMode(rs.getString("Mode")==null?"":rs.getString("Mode"));
				obj.setSepearateJournal(rs.getString("SeperateJournal")==null?"":rs.getString("SeperateJournal"));
				obj.setClassHide(rs.getString("ClassHide"));
				obj.setMemoHide(rs.getString("MemoHide")==null?"":rs.getString("MemoHide"));
				obj.setJvRefNumber(rs.getString("JVRefNumber")==null?"":rs.getString("JVRefNumber"));
				obj.setStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				obj.setUserId(rs.getInt("UserID"));
				obj.setDefferedIncome(rs.getString("postasdiffered")==null?"":rs.getString("postasdiffered"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getReceiptVoucherById-->" , ex);
		}
		return obj;
	}
	
	public List<ReceiptVoucherDeatiledModel> getReciptVoucherGridDataByID(int receiptVoucherId)
	{
		List<ReceiptVoucherDeatiledModel> lst=new ArrayList<ReceiptVoucherDeatiledModel>();
		
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getReceiptVoucherGridDataByID(receiptVoucherId));
			while(rs.next())
			{
				ReceiptVoucherDeatiledModel obj=new ReceiptVoucherDeatiledModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setLineNo(rs.getInt("line_No"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setDepositeToAccountRefKey(rs.getInt("DepositToAccountRef_Key"));
				obj.setAmmount(rs.getDouble("amount"));
				obj.setCheckDate(rs.getDate("chequeDate"));
				obj.setCheckNumber(rs.getString("chequeNO"));
				obj.setClassRefKey(rs.getInt("ClassRef_Key"));
				obj.setBankKey(rs.getInt("bank_Key"));
				obj.setMemo(rs.getString("Memo").equalsIgnoreCase("null")?"":rs.getString("memo"));
				obj.setPaymentMethod(rs.getString("paymentMethod"));
				obj.setQbStatus(rs.getString("qbStatus"));
				obj.setStatus(rs.getString("Status"));
				obj.setCucaccuntKey(rs.getInt("CUCAcctKey"));
				obj.setCucaccuntKey(rs.getInt("CUCAcctKey"));
				obj.setVisaAmount(rs.getDouble("VisaAmount"));
				obj.setVisaCharge(rs.getDouble("VisaCharge"));
				lst.add(obj);
				
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---getReciptVoucherGridDataByID-->" , ex);
		}
		return lst;
	}
	
	
	public ReceiptVoucherMastModel navigationReciptVoucher(int receiptVoucherId,int webUserID,boolean seeTrasction,String navigation,String actionTYpe)
	{
		ReceiptVoucherMastModel obj=new ReceiptVoucherMastModel();
		
		ReceiptVoucherQuerries query=new ReceiptVoucherQuerries();
		ResultSet rs = null;
		try 
		{
			if(navigation.equalsIgnoreCase("prev") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view")))
			{
				rs=db.executeNonQuery(query.getPreviousRecordReceiptVoucher(receiptVoucherId,webUserID,seeTrasction));
			}
			else if(navigation.equalsIgnoreCase("next")&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view")))
			{
				rs=db.executeNonQuery(query.getNextRecordReceiptVoucher(receiptVoucherId,webUserID,seeTrasction));
			}
			else if(navigation.equalsIgnoreCase("next")&& actionTYpe.equalsIgnoreCase("create"))
			{
				rs=db.executeNonQuery(query.getFirstRecordReceiptVoucher(webUserID,seeTrasction));
			}
			else if(navigation.equalsIgnoreCase("prev")&& actionTYpe.equalsIgnoreCase("create"))
			{
				rs=db.executeNonQuery(query.getLastRecordReceiptVoucher(webUserID,seeTrasction));
			}
			while(rs.next())
			{
				obj.setRecNo(rs.getInt("rec_no"));
				obj.setArAccountRefKey(rs.getInt("ArAccountRef_Key"));
				obj.setCustRefKey(rs.getInt("CustomerRef_key"));
				obj.setReceiptName(rs.getString("ReceiptName").equalsIgnoreCase("null")?"":rs.getString("ReceiptName"));
				obj.setTxtDate(rs.getDate("TxnDate"));
				obj.setTxnId(rs.getString("TxnID")==null?"":rs.getString("TxnID"));
				obj.setTotalAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("Memo").equalsIgnoreCase("null")?"":rs.getString("Memo"));
				obj.setRefNumber(rs.getString("RefNumber")==null?"":rs.getString("RefNumber"));
				obj.setQbRefNo(rs.getString("QBRefNo"));
				obj.setQbRefDate(rs.getString("QBRefDate")==null?"":rs.getString("QBRefDate"));
				obj.setRvOrJv(rs.getString("RvOrJv")==null?"":rs.getString("RvOrJv"));
				obj.setMode(rs.getString("Mode")==null?"":rs.getString("Mode"));
				obj.setSepearateJournal(rs.getString("SeperateJournal")==null?"":rs.getString("SeperateJournal"));
				obj.setClassHide(rs.getString("ClassHide"));
				obj.setMemoHide(rs.getString("MemoHide")==null?"":rs.getString("MemoHide"));
				obj.setJvRefNumber(rs.getString("JVRefNumber")==null?"":rs.getString("JVRefNumber"));
				obj.setStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				obj.setUserId(rs.getInt("UserID"));
				obj.setDefferedIncome(rs.getString("postasdiffered")==null?"":rs.getString("postasdiffered"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherData---navigationReciptVoucher-->" , ex);
		}
		return obj;
	}
	

	
	

}
