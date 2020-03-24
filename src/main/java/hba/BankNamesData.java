package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

import model.AccountsModel;
import model.BanksModel;
import model.CompanyDBModel;
import model.OtherNamesModel;
import model.QbListsModel;

public class BankNamesData {

	private Logger logger = Logger.getLogger(HBAData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public BankNamesData()
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
			logger.error("error in BankNamesData---Init-->" , ex);
		}
	}
	
	
	public List<BanksModel> fillBankList()
	{
		List<BanksModel> lst=new ArrayList<BanksModel>();
		BanksNameQuerries query=new BanksNameQuerries();
		ResultSet rs = null;
		ResultSet acconutrs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillBankList());
			while(rs.next())
			{
				BanksModel obj=new BanksModel();
				obj.setRecno(rs.getInt("recno"));
				obj.setBankName(rs.getString("Name"));					
				obj.setBranch(rs.getString("branch"));
				obj.setActName(rs.getString("actname"));
				obj.setActNumber(rs.getString("actnumber"));
				obj.setAccountRefKey(rs.getInt("accountrefkey"));
				if(obj.getAccountRefKey()!=0)
				{
					acconutrs=db.executeNonQuery(query.getBanksNamebyaccountRefKey(obj.getAccountRefKey()));
					while(acconutrs.next())
					{
						obj.setAccountAssosiatedWith(acconutrs.getString("accountName"));
					}
					
				}
				else{
					obj.setAccountAssosiatedWith("");
				}
				obj.setIBANNo(rs.getString("ibanno"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in BankNamesData---fillBankList-->" , ex);
		}
		return lst;
	}
	
	public BanksModel getBanksNameNameListByID(int bankKey)
	{
		BanksModel obj=new BanksModel();
		BanksNameQuerries query=new BanksNameQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getBanksNameListByID(bankKey));
			while(rs.next())
			{
				obj.setRecno(rs.getInt("recno"));
				obj.setBankName(rs.getString("Name"));					
				obj.setBranch(rs.getString("branch"));
				obj.setActName(rs.getString("actname"));
				obj.setActNumber(rs.getString("actnumber"));
				obj.setAccountRefKey(rs.getInt("accountrefkey"));
				obj.setIBANNo(rs.getString("ibanno"));
			}
		}
		catch (Exception ex) {
			logger.error("error in BankNamesData---getBanksNameNameListByID-->" , ex);
		}
		return obj;
	}
	
	public List<BanksModel> getNameFromBankListForValidation()
	{
		List<BanksModel> lst=new ArrayList<BanksModel>();
		
		BanksNameQuerries query=new BanksNameQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNameFromBankListForValidation());
			while(rs.next())
			{
				BanksModel qbData=new BanksModel();
				qbData.setBankName(rs.getString("name"));
				qbData.setRecno(rs.getInt("recno"));
				lst.add(qbData);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in BankNamesData---getNameFromBankListForValidation-->" , ex);
		}
		return lst;
	}
	
	
	public int updateBankNmaeList(BanksModel obj)
	{
		int result=0;
		BanksNameQuerries query=new BanksNameQuerries();	
		try 
		{			
			result=db.executeUpdateQuery(query.updateBankNmaeList(obj));	
			
		}
		catch (Exception ex) {
			logger.error("error in BankNamesData---updateBankNmaeList-->" , ex);
		}
		return result;
		
	}
	
	public int GetBankListRecNoQuery()
	{
		int MaxRecNo=1;
		
		BanksNameQuerries query=new BanksNameQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetBankListRecNoQuery());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in BankNamesData---GetBankListRecNoQuery-->" , ex);
		}
		return MaxRecNo;
	}
	
	public int inserBankNameQuerry(BanksModel banksModel,int recNo)
	{
		int result=0;
		
		BanksNameQuerries query=new BanksNameQuerries();		
		try 
		{		
			String str=query.inserBankNameQuerry(banksModel,recNo);
			result=db.executeUpdateQuery(str);			
		}
		catch (Exception ex) 
		{
			logger.error("error in BankNamesData---inserBankNameQuerry-->" , ex);
		}
		return result;
	}
	
	public List<AccountsModel> fillAccountsdropDownForBank()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
		BanksNameQuerries query=new BanksNameQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillAccountsdropDownForBank());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setAccountName(rs.getString("name"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---fillBankAccounts-->" , ex);
		}
		return lst;
	}
	
	
}
