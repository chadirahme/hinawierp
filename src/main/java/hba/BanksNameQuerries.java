package hba;

import java.text.SimpleDateFormat;

import model.BanksModel;
import model.OtherNamesModel;

public class BanksNameQuerries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String fillBankList()
	{
		query=new StringBuffer();		
		query.append("Select * from Banks order by Name");
		return query.toString();		
	}
	
	public String getBanksNameListByID(int bankKey)
	{
		  query=new StringBuffer();
		  query.append("Select * from Banks where recno="+bankKey+"");		
		  return query.toString();
	}
	
	public String getBanksNamebyaccountRefKey(int accrefKey)
	{
		  query=new StringBuffer();
		  query.append("Select accountName from accounts where rec_no="+accrefKey+"");		
		  return query.toString();
	}
	
	public String getNameFromBankListForValidation()
	{
		query=new StringBuffer();
		query.append("Select name,recno from Banks");
		return query.toString();
		
	}
	public String updateBankNmaeList(BanksModel obj)
	{
		query=new StringBuffer();		
		query.append("update Banks set name='"+obj.getBankName()+"',branch='"+obj.getBranch() +"',actname='"+obj.getActName()+"',actnumber='"+obj.getActNumber()+"',accountrefkey="+obj.getAccountRefKey()+",ibanno='"+obj.getIBANNo()+"' Where  recno='" + obj.getRecno()+"'");
		return query.toString();		
	}
	
	public String GetBankListRecNoQuery()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(recNo) as MaxRecNo from Banks");		
		  return query.toString();
	}
	
	public String inserBankNameQuerry(BanksModel obj,int recNo)
	{
		  query=new StringBuffer();
		  query.append("insert into Banks (recno,name,branch,actname,actnumber,accountrefkey,ibanno)values(");
		  query.append(recNo +",'" + obj.getBankName() +"' , '" + obj.getBranch() + "', '" + obj.getActName() + "', '" + obj.getActNumber() + "',"+obj.getAccountRefKey()+",'"+obj.getIBANNo()+"')");
		  return query.toString();
	}
	
	public String fillAccountsdropDownForBank()
	{
		query=new StringBuffer();
		 query.append("SELECT Accounts.AccountName As [Name],AccountType    ,   SubLevel    ,   Rec_No , ListID FROM Accounts Inner join AccountType on AccountType.TypeName = Accounts.AccountType where AccountType in ('Bank','Post Dated Cheque') and IsActive='Y' order by AccountType.SRL_No,Accounts.ACTLEVELSwithNO");
		 return query.toString();		
	}

}
