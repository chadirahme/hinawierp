package hba;

import java.text.SimpleDateFormat;

import model.AccountsModel;

public class ChartOfAccountQueries {
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getFullNameFromChartOfAccountForValidation()
	{
		  query=new StringBuffer();
		  query.append("Select name,rec_no from Accounts");		
		  return query.toString();
	}
	
	public String getAccountNumberFromChartOfAccountForValidation()
	{
		  query=new StringBuffer();
		  query.append("Select AccountNumber,rec_no from Accounts ");		
		  return query.toString();
	}
	
	public String getAllAccountTypeFromChartOfAccount()
	{
		  query=new StringBuffer();
		  query.append("Select typename from AccountType");		
		  return query.toString();
	}
	
	public String getAccountTypeFromChartOfAccountForValidation(String accountType)
	{
		  query=new StringBuffer();
		  query.append("Select * from AccountType  where typename='"+accountType+"'");		
		  return query.toString();
	}
	
	public String getChartofAccountsByID(int accountid)
	{
		  query=new StringBuffer();
		  query.append("Select * from Accounts where rec_no="+accountid+"");		
		  return query.toString();
	}
	
	public String getChartofAccountsByNumber(String accountNumber)
	{
		  query=new StringBuffer();
		  query.append("Select * from Accounts where AccountNumber='"+accountNumber+"'");		
		  return query.toString();
	}
	
	
	public String getBankNamesForChartofAccounts()
	{
		  query=new StringBuffer();
		  query.append("Select name from banks");		
		  return query.toString();
	}
	
	public String getbanksForChartOFaccountById(int recno)
	{
		  query=new StringBuffer();
		  query.append("Select * from banks where accountrefkey="+recno+"");		
		  return query.toString();
	}
	
	public String getChartOfAccountRecNoQuery()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(Rec_No) as MaxRecNo from Accounts");		
		  return query.toString();
	}
	
	
	public String chartOfAccountInsertQuery(AccountsModel obj,int recNo)
	{
		  query=new StringBuffer();
		  query.append("insert into Accounts (Rec_No,name,Description,fullname,SubLevel,AccountType,AccountNumber,AccountName,ACTLEVELS,ACTLEVELSwithNO)values(");
		  query.append(recNo +",'"+ obj.getName() + "','" + obj.getDescription() +"' , '" + obj.getFullName() +"' , '" + obj.getSubLevel() + "', '" + obj.getAccountType() + "', '" + obj.getAccountNumber() + "', '" + obj.getAccountName() + "' , '" + obj.getActLevels() +"' , '" + obj.getaCTLEVELSwithNO() + "')");
		  return query.toString();
	}
	
	public String GetCharofAccountsListQuery(String isActive,boolean hasBalance)
	{
		  query=new StringBuffer();
		  query.append(" Select Accounts.*,Class.Class_Key,Class.FullName As Class");
		  query.append(" from Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType ");
		  query.append(" Left  Join Class On Accounts.ClassKey = Class.Class_Key ");
		  query.append(" Where 1=1 ");
		  
		  if(!isActive.equalsIgnoreCase(""))
		  query.append(" and Accounts.IsActive ='"+isActive+"'");	
		  if(hasBalance)
			  query.append(" and totalBalance<>0");
		  
		  query.append(" order by AccountType.SRL_No,Replace(Accounts.ACTLEVELSwithNO,':',':')");		  
		  return query.toString();
	}
	
	public String UpdateAccountQuery(AccountsModel obj)
	{
		String editedFromOnline="Y";
		query=new StringBuffer();		
		query.append("update Accounts set ACTLEVELSwithNO='"+obj.getaCTLEVELSwithNO()+"',sublevel='"+obj.getSubLevel()+"',ACTLEVELS='"+obj.getActLevels()+"',AccountName='"+obj.getAccountName()+"',fullname='"+obj.getFullName()+"',Description='"+obj.getDescription()+"',name='"+obj.getName()+"',accounttype='"+obj.getAccountType()+"',accountnumber='"+obj.getAccountNumber()+"',notes='"+obj.getNotes()+"',timemodified='"+sdf.format(obj.getModifiedDate())+"',balancedate='"+sdf.format(obj.getBalaceDate())+"',balance='"+obj.getBalance()+"',isactive='"+obj.getIsActive()+"',editedFromOnline='"+editedFromOnline+"' Where  Rec_No=" + obj.getRec_No());
		return query.toString();		
	}
	
	public String updateToatalBalance(double totalBalance,int rec_No)
	{
		query=new StringBuffer();		
		query.append("update Accounts set TotalBalance='"+totalBalance+"' Where  Rec_No=" + rec_No);
		return query.toString();		
	}
	
	public String addAccountQuery(AccountsModel obj,int recNo)
	{
		
		 query=new StringBuffer();
		  query.append("insert into Accounts (Rec_No,name,Description,fullname,SubLevel,AccountType,AccountNumber,AccountName,ACTLEVELS,ACTLEVELSwithNO,balancedate,timemodified,notes,balance,TimeCreated,isactive)values(");
		  query.append(recNo +",'"+ obj.getName() + "','" + obj.getDescription() +"' , '" + obj.getFullName() +"' , '" + obj.getSubLevel() + "', '" + obj.getAccountType() + "', '" + obj.getAccountNumber() + "', '" + obj.getAccountName() + "' , '" + obj.getActLevels() +"' , '" + obj.getaCTLEVELSwithNO() + "', '" + sdf.format(obj.getBalaceDate()) +"', '" + sdf.format(obj.getModifiedDate()) +"', '" + obj.getNotes() +"', '" + obj.getBalance() +"', '" + sdf.format(obj.getCreatedDate()) +"', '" + obj.getIsActive()+"')");
		  return query.toString();
	}
	
	public String UpdateBanksForChartOfAccountAccount(AccountsModel obj)
	{
		query=new StringBuffer();		
		query.append("update Banks set accountrefkey='"+obj.getRec_No()+"',actname='"+obj.getBankAccountName()+"',actnumber='"+obj.getBankAcountNumber()+"',branch='"+obj.getBranchName()+"',ibanno='"+obj.getiBanNumber()+"' Where  name='" + obj.getSelectedBankName()+"'");
		return query.toString();		
	}
	
	public String getsubOfOnEditChartOFAccount(String accountType)
	{
		  query=new StringBuffer();
		  query.append("Select Accounts.*,Class.Class_Key,Class.FullName As Class from Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType  Left  Join Class On Accounts.ClassKey = Class.Class_Key  Where Accounts.accounttype='"+accountType+"' order by AccountType.SRL_No,Replace(Accounts.ACTLEVELSwithNO,':',':')");		
		  return query.toString();
	}
	
	public String getSubOfCurrentSelection(String accountName,boolean asdnOrdescd)
	{
		  query=new StringBuffer();
		  if(asdnOrdescd==true)
		  query.append("Select * from Accounts Where CharIndex('"+accountName+"',actlevelswithno)>0 order by sublevel");
		  else
			  query.append("Select * from Accounts Where CharIndex('"+accountName+"',actlevelswithno)>0 order by sublevel desc");
		  return query.toString();
	}

}
