package hba;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportQueries
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar now = Calendar.getInstance();

	public String getTrialBalanceReoprtQuery() 
	{
		query = new StringBuffer();
		query.append("Select * From RPTGeneralLedger Where  Row_Type='T'  Order By Rec_No");
		return query.toString();
	}
	
	public String getRPTGeneralLedgerQuery() 
	{
		query = new StringBuffer();
		query.append("Select RPTGeneralLedger.*,Accounts.SubLevel,Accounts.AccountName As AccountNameORG,");
		query.append(" Accounts.AccountType,Accounts.Name,Accounts.DESCRIPTION,Accounts.AccountNumber,Accounts.ArAccountName");
		query.append(" From RPTGeneralLedger Left Join Accounts On RPTGeneralLedger.Act_Key=Accounts.Rec_No");
		query.append(" Where Row_Type='M' and Act_Type in ('AccountsReceivable') "); 
		query.append(" Order By Rec_No");
		query.append(" ");
		query.append(" ");
		query.append(" ");
		return query.toString();
	}
	
	public String CheckSubAccountsQuery(String accountName) 
	{
		query = new StringBuffer();
		query.append("Select FullName from Accounts Where CharIndex('%s',ACTLEVELSwithNO)>0");
		String sql=String.format(query.toString(), accountName);
		return sql;
	}
	
	public String getAccountsListQuery() 
	{
		query = new StringBuffer();
		query.append("Select Distinct Accounts.AccountName From RPTQBFinancialSTD ");
		query.append("  Inner Join Accounts On RPTQBFinancialSTD.Act_Key=Accounts.Rec_No");
		query.append(" where Act_Key <>  0  And  REPORT_NAME      ='PLSTD'");
		query.append(" Order By Accounts.AccountName");
		return query.toString();
	}
	
	public String getProfitLossReportQuery() 
	{
		query = new StringBuffer();		
		query.append(" Select RPTQBFinancialSTD.*,Accounts.SubLevel,Accounts.AccountName As AccountNameORG,");
		query.append(" Accounts.AccountType,Accounts.Name,Accounts.DESCRIPTION,Accounts.ArAccountName From RPTQBFinancialSTD ");
		query.append(" Left Join Accounts On RPTQBFinancialSTD.Act_Key=Accounts.Rec_No ");
		query.append(" where  Report_Name    ='PLSTD'");
		query.append(" Order By Rec_No");
		return query.toString();
	}
	
	public String getGeneralLedgerReportQuery() 
	{
		query = new StringBuffer();		
		query.append(" Select RPTGENERALLEDGERSTD.*,Accounts.AccountName,Accounts.ArAccountName,Accounts.AccountNumber,Accounts.AccountType,");
		query.append(" SplitAct.AccountName As SplitActName,SplitAct.ArAccountName As SplitActNameAR,QBLists.FullName As EntityName,");
		query.append(" QBLists.ListType As EntityListType,QBLists.ArFullName As EntityNameAr ");
		query.append(" From RPTGENERALLEDGERSTD ");
		query.append(" LEFT JOIN Accounts On RPTGENERALLEDGERSTD.Act_Key=Accounts.Rec_No");
		query.append(" LEFT JOIN Accounts As SplitAct On RPTGENERALLEDGERSTD.SplitAct_Key=SplitAct.Rec_No ");
		query.append(" LEFT JOIN QBlists On RPTGENERALLEDGERSTD.Entity_Key=QBlists.RecNo");
		query.append(" Order By RPTGENERALLEDGERSTD.Rec_No");
		
		return query.toString();
	}
}
