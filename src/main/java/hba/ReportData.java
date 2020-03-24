package hba;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.BalanceSheetReportModel;
import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ReportData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db = new SQLDBHandler("hinawi_hba");
	ReportQueries query = new ReportQueries();
	public ReportData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb = new DBHandler();
			ResultSet rs = null;
			CompanyDBModel obj = new CompanyDBModel();
			WebusersModel dbUser = (WebusersModel) sess.getAttribute("Authentication");
			if (dbUser != null)
			{
				HBAQueries query = new HBAQueries();
				rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
						.getCompanyid()));
				while (rs.next()) 
				{
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));
				}
				db = new SQLDBHandler(obj);
			}
		}
		catch (Exception ex)
		{
			logger.error("error in ReportData---Init-->", ex);
		}
	}
	
	public List<BalanceSheetReportModel> getTrialBalanceReoprt() 
	{
		List<BalanceSheetReportModel> lst = new ArrayList<BalanceSheetReportModel>();		
		ResultSet rs = null;
		try 
		{
			rs = db.executeNonQuery(query.getTrialBalanceReoprtQuery());
			while (rs.next()) 
			{
				BalanceSheetReportModel obj = new BalanceSheetReportModel();
				obj.setRecNo(rs.getInt("rec_no"));
				obj.setActName(rs.getString("act_name") == null ? "" : rs
						.getString("act_name"));
				obj.setName(rs.getString("AccountName") == null ? "" : rs.getString("AccountName"));
				obj.setDebit(rs.getDouble("Debit"));
				obj.setCredit(rs.getDouble("Credit"));
				obj.setBalance(rs.getDouble("Balance"));
				
				
				lst.add(obj);
			}
		} 
		catch (Exception ex)
		{
			logger.error("error in ReportData---getTrialBalanceReoprt-->", ex);
		}

		return lst;

	}
	
	public List<BalanceSheetReportModel> getRPTGeneralLedger() 
	{
		List<BalanceSheetReportModel> lst = new ArrayList<BalanceSheetReportModel>();		
		ResultSet rs = null;
		try 
		{
			rs = db.executeNonQuery(query.getRPTGeneralLedgerQuery());
			while (rs.next()) 
			{
				BalanceSheetReportModel obj = new BalanceSheetReportModel();
				obj.setRecNo(rs.getInt("rec_no"));
				obj.setActName(rs.getString("act_name") == null ? "" : rs.getString("act_name"));
				obj.setAccountNameORG(rs.getString("AccountNameORG") == null ? "" : rs.getString("AccountNameORG"));
				obj.setAccountType(rs.getString("AccountType") == null ? "" : rs.getString("AccountType"));
				obj.setDescription(rs.getString("ArAccountName") == null ? "": rs.getString("ArAccountName"));
				
				obj.setAccountNumber(rs.getString("AccountNumber")== null ? "": rs.getString("AccountNumber"));			
				obj.setDebit(rs.getDouble("Debit"));
				obj.setCredit(rs.getDouble("Credit"));
				obj.setBalance(rs.getDouble("Balance"));
				
				String sublevelNew = rs.getString("subLevel") == null ? "0"
						: rs.getString("subLevel");
				if (sublevelNew.equalsIgnoreCase("0"))
					obj.setSubLevel(0);
				else
					obj.setSubLevel(Integer.parseInt(sublevelNew));
				lst.add(obj);
			}
		} 
		catch (Exception ex)
		{
			logger.error("error in ReportData---getRPTGeneralLedger-->", ex);
		}

		return lst;

	}
	
	public boolean CheckSubAccounts(String accountName) 
	{		
		ResultSet rs = null;
		try 
		{
	        accountName = accountName + ":";
			rs = db.executeNonQuery(query.CheckSubAccountsQuery(accountName));
			while (rs.next()) 
			{
				String FullName=rs.getString("FullName")==null?"":rs.getString("FullName");			
				logger.info("CheckSubAccounts= " + accountName);	
				return FullName.length()>0;
			}
		} 
		catch (Exception ex)
		{
			logger.error("error in ReportData---CheckSubAccounts-->", ex);
		}

		return false;
	}
	
	
	public List<String> getAccountsList() 
	{
		List<String> lst = new ArrayList<String>();		
		ResultSet rs = null;
		try 
		{
			rs = db.executeNonQuery(query.getAccountsListQuery());
			while (rs.next()) 
			{			
				lst.add(rs.getString("AccountName")==null?"":rs.getString("AccountName"));
			}
		} 
		catch (Exception ex)
		{
			logger.error("error in ReportData---getAccountsList-->", ex);
		}

		return lst;

	}
	
	public List<BalanceSheetReportModel> getProfitLossReport() 
	{
		List<BalanceSheetReportModel> lst = new ArrayList<BalanceSheetReportModel>();		
		ResultSet rs = null;
		try 
		{
			rs = db.executeNonQuery(query.getProfitLossReportQuery());
			while (rs.next()) 
			{
				BalanceSheetReportModel obj = new BalanceSheetReportModel();
				obj.setRecNo(rs.getInt("rec_no"));
				obj.setActKey(rs.getInt("Act_Key"));
				if(obj.getActKey()>0)
					obj.setAccountNameORG(rs.getString("AccountNameORG") == null ? "" : rs.getString("AccountNameORG"));
				else
					obj.setAccountNameORG(rs.getString("act_name") == null ? "" : rs.getString("act_name"));
				
				obj.setActName(rs.getString("act_name") == null ? "" : rs.getString("act_name"));
				
				obj.setAccountType(rs.getString("AccountType") == null ? "" : rs.getString("AccountType"));
				obj.setDescription(rs.getString("ArAccountName") == null ? "": rs.getString("ArAccountName"));
				obj.setBalance(rs.getDouble("Amount"));											
				
				String sublevelNew = rs.getString("subLevel") == null ? "0"
						: rs.getString("subLevel");
				if (sublevelNew.equalsIgnoreCase("0"))
					obj.setSubLevel(0);
				else
					obj.setSubLevel(Integer.parseInt(sublevelNew));
				
				if(obj.getAccountNameORG().contains("Total"))
					obj.setRowColor("blue");
				
				lst.add(obj);
			}
		} 
		catch (Exception ex)
		{
			logger.error("error in ReportData---getProfitLossReport-->", ex);
		}

		return lst;

	}
	
	public List<BalanceSheetReportModel> getGeneralLedgerReport() 
	{
		List<BalanceSheetReportModel> lst = new ArrayList<BalanceSheetReportModel>();		
		ResultSet rs = null;
		String tmpActName ="";
		try 
		{
			rs = db.executeNonQuery(query.getGeneralLedgerReportQuery());
			while (rs.next()) 
			{
				BalanceSheetReportModel obj = new BalanceSheetReportModel();
				obj.setRecNo(rs.getInt("rec_no"));
							
				obj.setAccountNameORG(rs.getString("AccountName") == null ? "" : rs.getString("AccountName"));
				obj.setDescription(rs.getString("ArAccountName") == null ? "": rs.getString("ArAccountName"));
				obj.setActName(rs.getString("act_name") == null ? "" : rs.getString("act_name"));
				obj.setRowType(rs.getString("Row_Type") == null ? "" : rs.getString("Row_Type"));
				obj.setAccountNumber(rs.getString("AccountNumber")== null ? "": rs.getString("AccountNumber"));		
				obj.setTxnDate(rs.getDate("Txn_Date"));	
				obj.setTxnType(rs.getString("Txn_Type")== null ? "": rs.getString("Txn_Type"));
				obj.setTxnNo(rs.getString("Txn_No")== null ? "": rs.getString("Txn_No"));
				obj.setEntityName(rs.getString("EntityName")== null ? "": rs.getString("EntityName"));
				obj.setEntityListType(rs.getString("EntityListType")== null ? "": rs.getString("EntityListType"));
				obj.setAccountType(rs.getString("AccountType")== null ? "": rs.getString("AccountType"));
				obj.setSplitActName(rs.getString("SplitActName")== null ? "": rs.getString("SplitActName"));
				obj.setMemo(rs.getString("Memo")== null ? "": rs.getString("Memo"));
			
				if(obj.getRowType().equals("M"))
				{
					if(!obj.getActName().equalsIgnoreCase("No accnt"))
						tmpActName =obj.getAccountNameORG();
					else
						tmpActName=obj.getActName();
				}
				
//				switch (obj.getRowType())
//				{
//				case "T":
//					obj.setAccountNameORG("Total : " +tmpActName);
//					break;
//				default:
//					obj.setAccountNameORG(tmpActName);
//					break;
//				}
				
				obj.setCredit(rs.getDouble("Credit"));
				obj.setDebit(rs.getDouble("Debit"));
				obj.setBalance(rs.getDouble("Balance"));				
				
				
								
				lst.add(obj);
			}
		} 
		catch (Exception ex)
		{
			logger.error("error in ReportData---getGeneralLedgerReport-->", ex);
		}

		return lst;

	}
	
}
