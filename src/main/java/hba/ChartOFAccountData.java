package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;   
import java.util.regex.Pattern;

import model.AccountsModel;
import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

public class ChartOFAccountData {
	
	private Logger logger = Logger.getLogger(ChartOFAccountData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("");
	Date creationdate;
	Calendar c = Calendar.getInstance();
	
	
	public ChartOFAccountData()
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
			logger.error("error in ChartOFAccountData---Init-->" , ex);
		}
	}
	
	public List<AccountsModel> getFullNameFromChartOfAccountForValidation()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		String name="";
		try 
		{
			rs=db.executeNonQuery(query.getFullNameFromChartOfAccountForValidation());
			while(rs.next())
			{
				AccountsModel nameObj=new AccountsModel();
				nameObj.setName(rs.getString("name"));
				nameObj.setRec_No(rs.getInt("Rec_No"));
			  lst.add(nameObj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---getFullNameFromChartOfAccountForValidation-->" , ex);
		}
		return lst;
	}
	
	public List<AccountsModel> getAccountNumberFromChartOfAccountForValidation()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		String name="";
		try 
		{
			rs=db.executeNonQuery(query.getAccountNumberFromChartOfAccountForValidation());
			while(rs.next())
			{
				AccountsModel objNum=new AccountsModel();
			   objNum.setAccountNumber(rs.getString("AccountNumber"));
			   objNum.setRec_No(rs.getInt("Rec_No"));
			  lst.add(objNum);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---getAccountNumberFromChartOfAccountForValidation-->" , ex);
		}
		return lst;
	}
	public String getAccountTypeFromChartOfAccountForValidation(String accountType)
	{
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		String name="";
		try 
		{
			rs=db.executeNonQuery(query.getAccountTypeFromChartOfAccountForValidation(accountType));
			while(rs.next())
			{
			   name = rs.getString("TypeName");
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---getAccountNumberFromChartOfAccountForValidation-->" , ex);
		}
		return name;
	}
	
	public int getChartOfAccountRecNoQuery()
	{
		int MaxRecNo=1;
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getChartOfAccountRecNoQuery());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---getChartOfAccountRecNoQuery-->" , ex);
		}
		return MaxRecNo;
	}
	
	
	public int chartOfAccountInsertQuery(AccountsModel customerModel,int recNo)
	{
		int result=0;
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();		
		try 
		{		
			//.setCreatedDate(new Date());
			String str=query.chartOfAccountInsertQuery(customerModel,recNo);
			result=db.executeUpdateQuery(str);			
		}
		catch (Exception ex) 
		{
			logger.error("error in ChartOFAccountData---chartOfAccountInsertQuery-->" , ex);
		}
		return result;
	}
	
	public AccountsModel getChartofAccountsByID(int accountId)
	{
		AccountsModel obj=new AccountsModel();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		ResultSet rsBank = null;
		Pattern pattern = Pattern.compile("(\\·)(.*?)(\\:)");
		List<String> listMatches = new ArrayList<String>();
		 
		DecimalFormat dcf = new DecimalFormat( "#,###,###,##0.00" );
		try 
		{
			rs=db.executeNonQuery(query.getChartofAccountsByID(accountId));
			while(rs.next())
			{
				
				obj.setRec_No(rs.getInt("Rec_No"));
				if(obj.getRec_No()!=0)
				{
					rsBank=db.executeNonQuery(query.getbanksForChartOFaccountById(accountId));
					while(rsBank.next())
					{
						obj.setBankAccountName(rsBank.getString("actname")==null?"":rsBank.getString("actname"));
						obj.setBankAcountNumber(rsBank.getString("actnumber")==null?"":rsBank.getString("actnumber"));
						obj.setBranchName(rsBank.getString("branch")==null?"":rsBank.getString("branch"));
						obj.setiBanNumber(rsBank.getString("ibanno")==null?"":rsBank.getString("ibanno"));
						obj.setBankName(rsBank.getString("name")==null?"":rsBank.getString("name"));
					}
					
				}
				obj.setName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setFullName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setActLevels(rs.getString("actlevels")==null?"":rs.getString("actlevels"));
				obj.setAccountName(rs.getString("AccountName")==null?"":rs.getString("AccountName"));
				obj.setOldAccountName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setOldAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setAccountType(rs.getString("AccountType")==null?"":rs.getString("AccountType"));
				obj.setIsActive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				obj.setAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setaCTLEVELSwithNO(rs.getString("actlevelswithno")==null?"":rs.getString("actlevelswithno"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				/*if(obj.getSubLevel()>0)
				{
					Matcher matcher = pattern.matcher(obj.getaCTLEVELSwithNO());
					while(matcher.find())
			        {
			            listMatches.add(matcher.group(2));
			        }
					obj.setSubOfdropDown(listMatches);
				}*/
				obj.setDescription(rs.getString("Description")==null?"":rs.getString("Description"));
				double TotalBalance=rs.getDouble("TotalBalance");
				double balance=rs.getDouble("balance");
				if(rs.getDate("balanceDate")!=null)
				obj.setBalaceDate(df.parse(sdf.format(rs.getDate("balanceDate"))));
				if(rs.getDate("TimeCreated")!=null)
				obj.setCreatedDate(df.parse(sdf.format(rs.getDate("TimeCreated"))));
				obj.setBalance(dcf.parse(dcf.format(balance)).doubleValue());
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance)).doubleValue());
			//	obj.setClassName(rs.getString("Class"));
				obj.setNotes(rs.getString("notes")==null?"":rs.getString("notes"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---getChartofAccountsByID-->" , ex);
		}
		return obj;
	}
	
	public List<AccountsModel> fillChartofAccounts(String isActive,boolean hasBalance)
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		DecimalFormat dcf=new DecimalFormat("#,##0.00");
		try 
		{
			rs=db.executeNonQuery(query.GetCharofAccountsListQuery(isActive,hasBalance));
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setaCTLEVELSwithNO(rs.getString("actlevelswithno"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setOldAccountName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setOldAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setDescription(rs.getString("Description")==null?"":rs.getString("Description"));
				obj.setIsActive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				double TotalBalance=rs.getDouble("TotalBalance");
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance)).doubleValue());
				obj.setClassName(rs.getString("Class"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else{
					obj.setIsActive("INActive");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---fillChartofAccounts-->" , ex);
		}
		return lst;
	}
	
	public int addAccount(AccountsModel obj) throws ParseException
	{
		int result=0;
		AccountsModel topLevelRec=null;
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		List<AccountsModel> getSubOfCurentSelection=new ArrayList<AccountsModel>();
		if(obj.getSelectedSubOf()!=null)
				{
					AccountsModel getbyIdOfSubOfSelected=getChartofAccountsByID(obj.getSelectedSubOf().getRec_No());
					obj.setActLevels(getbyIdOfSubOfSelected.getActLevels()+":"+obj.getName());
					obj.setFullName(getbyIdOfSubOfSelected.getFullName()+":"+obj.getName());
					obj.setAccountName(obj.getAccountNumber()+"·"+obj.getName());
					obj.setaCTLEVELSwithNO(getbyIdOfSubOfSelected.getaCTLEVELSwithNO()+":"+obj.getAccountName());
					obj.setAccountType(getbyIdOfSubOfSelected.getAccountType());
					obj.setSubLevel(getbyIdOfSubOfSelected.getSubLevel()+1);
					
					 Pattern pattern = Pattern.compile("(.*?)·");
			    	Matcher matcher = pattern.matcher(getbyIdOfSubOfSelected.getaCTLEVELSwithNO());
			    	String actlevelMatching="";
			    	if (matcher.find())
			    	{
			    	    actlevelMatching=matcher.group(1);
			    	}
			    	topLevelRec=getChartofAccountsByNumber(actlevelMatching);
			    	
				}
				else
				{
					obj.setActLevels(obj.getName());
					obj.setFullName(obj.getName());
					obj.setAccountName(obj.getAccountNumber()+"·"+obj.getName());
					obj.setaCTLEVELSwithNO(obj.getAccountName());
					obj.setAccountType(obj.getSelectedAccountType());
					obj.setSubLevel(0);
					
					
				}
					
					
					
		try 
		{	
			int tmpRecNo=getChartOfAccountRecNoQuery();
			result=db.executeUpdateQuery(query.addAccountQuery(obj,tmpRecNo));	
			if(result>0)
			{
				result=db.executeUpdateQuery(query.updateToatalBalance(obj.getBalance(),tmpRecNo));
			}
			if(obj.getSelectedSubOf()!=null)
			{
			getSubOfCurentSelection=getSubOfCurrentSelection(topLevelRec.getAccountName(),false);
	    	for(AccountsModel oldObj:getSubOfCurentSelection)
			{
	    		   	AccountsModel topLevelRecNew=getChartofAccountsByNumber(oldObj.getAccountNumber());
			    	List<AccountsModel> getSubOfCurentSelectionNew =getSubOfCurrentSelection(topLevelRecNew.getAccountName(),true);
			    	double totalBalance=0;
			    	for(AccountsModel deeperObj:getSubOfCurentSelectionNew)
			    	{
			    		totalBalance=totalBalance+deeperObj.getBalance();
			    	}
			    	result=db.executeUpdateQuery(query.updateToatalBalance(totalBalance,oldObj.getRec_No()));
	    		
			}
	    	
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---UpdateAccount-->" , ex);
		}
		
		return result;
		
	}
	
	public AccountsModel getChartofAccountsByNumber(String acountNumber)
	{
		AccountsModel obj=new AccountsModel();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		ResultSet rsBank = null;
		Pattern pattern = Pattern.compile("(\\·)(.*?)(\\:)");
		List<String> listMatches = new ArrayList<String>();
		 
		DecimalFormat dcf = new DecimalFormat( "#,###,###,##0.00" );
		try 
		{
			rs=db.executeNonQuery(query.getChartofAccountsByNumber(acountNumber));
			while(rs.next())
			{
				
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setFullName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setActLevels(rs.getString("actlevels")==null?"":rs.getString("actlevels"));
				obj.setAccountName(rs.getString("AccountName")==null?"":rs.getString("AccountName"));
				obj.setOldAccountName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setOldAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setAccountType(rs.getString("AccountType")==null?"":rs.getString("AccountType"));
				obj.setIsActive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				obj.setAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setaCTLEVELSwithNO(rs.getString("actlevelswithno")==null?"":rs.getString("actlevelswithno"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setDescription(rs.getString("Description")==null?"":rs.getString("Description"));
				double TotalBalance=rs.getDouble("TotalBalance");
				double balance=rs.getDouble("balance");
				if(rs.getDate("balanceDate")!=null)
				obj.setBalaceDate(df.parse(sdf.format(rs.getDate("balanceDate"))));
				if(rs.getDate("TimeCreated")!=null)
				obj.setCreatedDate(df.parse(sdf.format(rs.getDate("TimeCreated"))));
				obj.setBalance(dcf.parse(dcf.format(balance)).doubleValue());
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance)).doubleValue());
			//	obj.setClassName(rs.getString("Class"));
				obj.setNotes(rs.getString("notes")==null?"":rs.getString("notes"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---getChartofAccountsByNumber-->" , ex);
		}
		return obj;
	}
	
	
	public int UpdateAccount(AccountsModel obj) throws ParseException
	{
		
		List<AccountsModel> getSubOfCurentSelection=new ArrayList<AccountsModel>();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		AccountsModel selectedTempObject=new AccountsModel();
		AccountsModel getbyIdOfSubOfSelected=null;
		getSubOfCurentSelection=getSubOfCurrentSelection(obj.getAccountName(),true);
		int result=0;
		
		try 
		{	
		if(obj.getSelectedSubOf()!=null)
			getbyIdOfSubOfSelected=getChartofAccountsByID(obj.getSelectedSubOf().getRec_No());
		int sublevelTrack=0;
		String ActLevels="";
		String ACTLEVELSwithNO ="";
		String oldAccountName="";
		String oldAccountNumber ="";
		selectedTempObject=obj;
		String AccountType="";
		double totalBalanceForWithSub=0;
		
		if(getbyIdOfSubOfSelected!=null)
		{
			//totalBalanceForWithSub=getbyIdOfSubOfSelected.getBalance();
			AccountType=getbyIdOfSubOfSelected.getAccountType();
		}
		else
		{
			AccountType=obj.getSelectedAccountType();
		}
		
		for(AccountsModel oldObj:getSubOfCurentSelection)
		{
			
			creationdate=df.parse(sdf.format(c.getTime()));
			
			if(getbyIdOfSubOfSelected!=null && oldObj.getAccountName().equalsIgnoreCase(obj.getAccountName()) )
			{
				oldObj=obj;
				obj.setActLevels(getbyIdOfSubOfSelected.getActLevels()+":"+oldObj.getOldAccountName());
				obj.setFullName(getbyIdOfSubOfSelected.getFullName()+":"+oldObj.getOldAccountName());
				obj.setAccountName(obj.getOldAccountNumber()+"·"+oldObj.getOldAccountName());
				obj.setaCTLEVELSwithNO(getbyIdOfSubOfSelected.getaCTLEVELSwithNO()+":"+oldObj.getAccountName());
				ActLevels=obj.getActLevels();
				oldAccountName=obj.getOldAccountName();
				oldAccountNumber=obj.getOldAccountNumber();
				ACTLEVELSwithNO=obj.getaCTLEVELSwithNO();
				obj.setSubLevel(getbyIdOfSubOfSelected.getSubLevel()+1);
				obj.setAccountType(AccountType);
				obj.setName(selectedTempObject.getName());
				obj.setAccountNumber(selectedTempObject.getAccountNumber());
				sublevelTrack=obj.getSubLevel();
				
			}
			else if(getbyIdOfSubOfSelected==null)
			{
				
				//	double totalBalanceForWithOutSub=0;
					//int recNoForTotalBalance=0;
					String AccName="";
					for(AccountsModel irtnObj:getSubOfCurentSelection)
					{
						
							irtnObj.setModifiedDate(creationdate);
							irtnObj.setBalaceDate(creationdate);
						if(irtnObj.getAccountName().equalsIgnoreCase(obj.getAccountName()))
						{
							//recNoForTotalBalance=irtnObj.getRec_No();
							irtnObj.setActLevels(irtnObj.getOldAccountName());
							irtnObj.setFullName(irtnObj.getOldAccountName());
							irtnObj.setAccountName(irtnObj.getOldAccountNumber()+"·"+irtnObj.getOldAccountName());
							irtnObj.setaCTLEVELSwithNO(irtnObj.getAccountName());
							irtnObj.setAccountType(irtnObj.getSelectedAccountType());
							irtnObj.setSubLevel(0);
							ActLevels=obj.getActLevels();
							oldAccountName=obj.getOldAccountName();
							oldAccountNumber=obj.getOldAccountNumber();
							ACTLEVELSwithNO=obj.getaCTLEVELSwithNO();
							irtnObj.setName(selectedTempObject.getName());
							irtnObj.setAccountNumber(selectedTempObject.getAccountNumber());
							irtnObj.setaCTLEVELSwithNO(irtnObj.getaCTLEVELSwithNO().replace(oldAccountName, selectedTempObject.getName()));
							irtnObj.setAccountName(irtnObj.getAccountName().replace(oldAccountName, selectedTempObject.getName()));
							irtnObj.setActLevels(irtnObj.getActLevels().replace(oldAccountName, selectedTempObject.getName()));
							irtnObj.setFullName(irtnObj.getFullName().replace(oldAccountName, selectedTempObject.getName()));
							irtnObj.setAccountName(irtnObj.getAccountName().replace(oldAccountNumber, selectedTempObject.getAccountNumber()));
							irtnObj.setaCTLEVELSwithNO(irtnObj.getaCTLEVELSwithNO().replace(oldAccountNumber, selectedTempObject.getAccountNumber()));
							irtnObj.setNotes(selectedTempObject.getNotes());
							irtnObj.setBankName(selectedTempObject.getBankName());
							irtnObj.setBranchName(selectedTempObject.getBranchName());
							irtnObj.setiBanNumber(selectedTempObject.getiBanNumber());
							irtnObj.setBankAccountName(selectedTempObject.getBankAccountName());
							irtnObj.setBankAcountNumber(selectedTempObject.getBankAcountNumber());
							irtnObj.setIsActive(selectedTempObject.getIsActive());
							irtnObj.setBalance(selectedTempObject.getBalance());
							irtnObj.setAccountType(AccountType);
							AccName=irtnObj.getAccountName();
							sublevelTrack=irtnObj.getSubLevel();
						}
							else
						{
								
								irtnObj.setActLevels(ActLevels+":"+irtnObj.getName());
								irtnObj.setaCTLEVELSwithNO(ACTLEVELSwithNO+":"+irtnObj.getAccountName());
								irtnObj.setFullName(irtnObj.getActLevels());
								irtnObj.setaCTLEVELSwithNO(irtnObj.getaCTLEVELSwithNO().replace(oldAccountName, selectedTempObject.getName()));
								irtnObj.setAccountName(irtnObj.getAccountName().replace(oldAccountName, selectedTempObject.getName()));
								irtnObj.setActLevels(irtnObj.getActLevels().replace(oldAccountName, selectedTempObject.getName()));
								irtnObj.setFullName(irtnObj.getFullName().replace(oldAccountName, selectedTempObject.getName()));
								irtnObj.setAccountName(irtnObj.getAccountName().replace(oldAccountNumber, selectedTempObject.getAccountNumber()));
								irtnObj.setaCTLEVELSwithNO(irtnObj.getaCTLEVELSwithNO().replace(oldAccountNumber, selectedTempObject.getAccountNumber()));
								irtnObj.setAccountType(AccountType);
							if(irtnObj.getSubLevel()==1)
							{
								if(sublevelTrack==irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel());
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack>irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel()+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack<irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(sublevelTrack+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								
								
							}
							else if(irtnObj.getSubLevel()==2)
							{	
								if(sublevelTrack==irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel());
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack>irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel()+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack<irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(sublevelTrack+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								
							
							}
							else if(irtnObj.getSubLevel()==3)
							{
								if(sublevelTrack==irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel());
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack>irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel()+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack<irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(sublevelTrack+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								
							
							}
							else
							{
								if(sublevelTrack==irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel());
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack>irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(irtnObj.getSubLevel()+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
								if(sublevelTrack<irtnObj.getSubLevel())
								{
									irtnObj.setSubLevel(sublevelTrack+1);
									sublevelTrack=irtnObj.getSubLevel();
								}
							
							}
						}
						
						if(irtnObj.getModifiedDate()==null)
							oldObj.setModifiedDate(creationdate);
						if(irtnObj.getBalaceDate()==null)
							obj.setBalaceDate(creationdate);
						result=db.executeUpdateQuery(query.UpdateAccountQuery(irtnObj));	
						if(result>0)
						{
							db.executeUpdateQuery(query.UpdateBanksForChartOfAccountAccount(irtnObj));
						}
					}
					List<AccountsModel> updateTotalbalanceWithOutSub=getSubOfCurrentSelection(AccName,false);
					for(AccountsModel newirtnObjwithOutSub:updateTotalbalanceWithOutSub)
					{
						
						List<AccountsModel> getSubOfCurentSelectionNew =getSubOfCurrentSelection(newirtnObjwithOutSub.getAccountName(),true);
				    	double totalBalance=0;
				    	for(AccountsModel deeperObj:getSubOfCurentSelectionNew)
				    	{
				    		totalBalance=totalBalance+deeperObj.getBalance();
				    	}
				    	result=db.executeUpdateQuery(query.updateToatalBalance(totalBalance,newirtnObjwithOutSub.getRec_No()));
						//totalBalanceForWithOutSub=totalBalanceForWithOutSub+newirtnObjwithOutSub.getBalance();
						//result=db.executeUpdateQuery(query.updateToatalBalance(totalBalanceForWithOutSub,recNoForTotalBalance));
					}
					
					break;
				
				
			}
			else{
				obj=oldObj;
		    	obj.setActLevels(ActLevels+":"+oldObj.getName());
		    	obj.setaCTLEVELSwithNO(ACTLEVELSwithNO+":"+oldObj.getAccountName());
				obj.setFullName(obj.getActLevels());
				
				if(oldObj.getSubLevel()==1)
				{
					if(sublevelTrack==oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack>oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack<oldObj.getSubLevel())
					{
						obj.setSubLevel(sublevelTrack+1);
						sublevelTrack=obj.getSubLevel();
					}
					
					
				}
				else if(oldObj.getSubLevel()==2)
				{	
					if(sublevelTrack==oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack>oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack<oldObj.getSubLevel())
					{
						obj.setSubLevel(sublevelTrack+1);
						sublevelTrack=obj.getSubLevel();
					}
					
				
				}
				else if(oldObj.getSubLevel()==3)
				{
					if(sublevelTrack==oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack>oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+2);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack<oldObj.getSubLevel())
					{
						obj.setSubLevel(sublevelTrack+1);
						sublevelTrack=obj.getSubLevel();
					}
					
				
				}
				else
				{
					if(sublevelTrack==oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack>oldObj.getSubLevel())
					{
						obj.setSubLevel(oldObj.getSubLevel()+1);
						sublevelTrack=obj.getSubLevel();
					}
					if(sublevelTrack<oldObj.getSubLevel())
					{
						obj.setSubLevel(sublevelTrack+1);
						sublevelTrack=obj.getSubLevel();
					}
					
				
				}
			obj.setAccountType(AccountType);
			}
			obj.setaCTLEVELSwithNO(obj.getaCTLEVELSwithNO().replace(oldAccountName, selectedTempObject.getName()));
			obj.setAccountName(obj.getAccountName().replace(oldAccountName, selectedTempObject.getName()));
			obj.setActLevels(obj.getActLevels().replace(oldAccountName, selectedTempObject.getName()));
			obj.setFullName(obj.getFullName().replace(oldAccountName, selectedTempObject.getName()));
			obj.setAccountName(obj.getAccountName().replace(oldAccountNumber, selectedTempObject.getAccountNumber()));
			obj.setaCTLEVELSwithNO(obj.getaCTLEVELSwithNO().replace(oldAccountNumber, selectedTempObject.getAccountNumber()));
			
			if(obj.getModifiedDate()==null)
				oldObj.setModifiedDate(creationdate);
			if(obj.getBalaceDate()==null)
				obj.setBalaceDate(creationdate);
			
				
			result=db.executeUpdateQuery(query.UpdateAccountQuery(obj));	
			if(result>0)
			{
				db.executeUpdateQuery(query.UpdateBanksForChartOfAccountAccount(obj));
			}
			
			//db.executeUpdateQuery(query.updateToatalBalance(oldObj.getBalance(),obj.getRec_No()));
		
		}
			if(getbyIdOfSubOfSelected!=null)
			{
				List<AccountsModel> updateTotalbalance=getSubOfCurrentSelection(getbyIdOfSubOfSelected.getAccountName(),false);
				for(AccountsModel newirtnObj:updateTotalbalance)
				{
					List<AccountsModel> getSubOfCurentSelectionNew =getSubOfCurrentSelection(newirtnObj.getAccountName(),true);
			    	double totalBalance=0;
			    	for(AccountsModel deeperObj:getSubOfCurentSelectionNew)
			    	{
			    		totalBalance=totalBalance+deeperObj.getBalance();
			    	}
			    	result=db.executeUpdateQuery(query.updateToatalBalance(totalBalance,newirtnObj.getRec_No()));
					//totalBalanceForWithSub=totalBalanceForWithSub+newirtnObj.getBalance();
				}
				//result=db.executeUpdateQuery(query.updateToatalBalance(totalBalanceForWithSub,getbyIdOfSubOfSelected.getRec_No()));
			}
		}
		catch (Exception ex) {
			logger.error("error in ChartOFAccountData---UpdateAccount-->" , ex);
		}
		return result;
		
	}
	public List<String> getBankNamesForChartofAccounts()
	{
		List<String> lst=new ArrayList<String>();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		try 
		{
			lst.add("Select");
			rs=db.executeNonQuery(query.getBankNamesForChartofAccounts());
			while(rs.next())
			{
				lst.add(rs.getString("Name"));
			}
		}
		catch (Exception ex) {
			logger.error("error in getBankNamesForChartofAccounts---fillChartofAccounts-->" , ex);
		}
		return lst;
	}
	
	public List<String> getAllAccountTypeFromChartOfAccount()
	{
		List<String> lst=new ArrayList<String>();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getAllAccountTypeFromChartOfAccount());
			while(rs.next())
			{
				lst.add(rs.getString("typename"));
			}
		}
		catch (Exception ex) {
			logger.error("error in getBankNamesForChartofAccounts---getAllAccountTypeFromChartOfAccount-->" , ex);
		}
		return lst;
	}
	
	public List<AccountsModel> getsubOfOnEditChartOFAccount(String accountType)
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getsubOfOnEditChartOFAccount(accountType));
			AccountsModel newobj=new AccountsModel();
			newobj.setRec_No(0);
			newobj.setName("None");
			newobj.setAccountName("None");
			newobj.setSubLevel(0);
			newobj.setAccountType("");
			lst.add(newobj);
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setAccountType(rs.getString("AccountType"));
				//if(obj.getSubLevel()==0)
				obj.setAccountName(obj.getAccountName());
				/*else if(obj.getSubLevel()==1)
					obj.setAccountName("              "+obj.getAccountName());
				else if(obj.getSubLevel()==2)
				obj.setAccountName("                      "+obj.getAccountName());
				else if(obj.getSubLevel()==3)
					obj.setAccountName("                         "+obj.getAccountName());
				else
					obj.setAccountName("                             "+obj.getAccountName());
					*/
				
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in getsubOfOnEditChartOFAccount---getAllAccountTypeFromChartOfAccount-->" , ex);
		}
		return lst;
	}
	
	/*public List<AccountsModel> getsubOfOnAddChartOFAccount(String accountType)
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetCharofAccountsListQuery(accountType));
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setAccountType(rs.getString("AccountType"));
				if(obj.getSubLevel()==0)
				obj.setAccountName(obj.getAccountName());
				else if(obj.getSubLevel()==1)
					obj.setAccountName("              "+obj.getAccountName());
				else if(obj.getSubLevel()==2)
				obj.setAccountName("                      "+obj.getAccountName());
				else if(obj.getSubLevel()==3)
					obj.setAccountName("                         "+obj.getAccountName());
				else
					obj.setAccountName("                             "+obj.getAccountName());
					
				
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in getsubOfOnAddChartOFAccount---getAllAccountTypeFromChartOfAccount-->" , ex);
		}
		return lst;
	}*/
	
	
	public List<AccountsModel> getSubOfCurrentSelection(String accountName,boolean asdnOrdescd)
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
		ChartOfAccountQueries query=new ChartOfAccountQueries();
		ResultSet rs = null;
		DecimalFormat dcf=new DecimalFormat("#,##0.00");
		try 
		{
			rs=db.executeNonQuery(query.getSubOfCurrentSelection(accountName,asdnOrdescd));
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setOldAccountName(rs.getString("Name")==null?"":rs.getString("Name"));
				obj.setFullName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setActLevels(rs.getString("actlevels")==null?"":rs.getString("actlevels"));
				obj.setAccountName(rs.getString("AccountName")==null?"":rs.getString("AccountName"));
				obj.setAccountType(rs.getString("AccountType")==null?"":rs.getString("AccountType"));
				obj.setIsActive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				obj.setAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setOldAccountNumber(rs.getString("accountnumber")==null?"":rs.getString("accountnumber"));
				obj.setaCTLEVELSwithNO(rs.getString("actlevelswithno")==null?"":rs.getString("actlevelswithno"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setDescription(rs.getString("Description")==null?"":rs.getString("Description"));
				double TotalBalance=rs.getDouble("TotalBalance");
				double balance=rs.getDouble("balance");
				if(rs.getDate("balanceDate")!=null)
				obj.setBalaceDate(df.parse(sdf.format(rs.getDate("balanceDate"))));
				if(rs.getDate("TimeCreated")!=null)
				obj.setCreatedDate(df.parse(sdf.format(rs.getDate("TimeCreated"))));
				obj.setBalance(dcf.parse(dcf.format(balance)).doubleValue());
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance)).doubleValue());
				obj.setNotes(rs.getString("notes")==null?"":rs.getString("notes"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in getSubOfCurrentSelection---fillChartofAccounts-->" , ex);
		}
		return lst;
	}
	
	

}
