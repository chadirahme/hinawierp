package fixedasset;

import hba.HBAQueries;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.*;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

public class FixedAssetData 
{
	private Logger logger = Logger.getLogger(FixedAssetData.class);
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public FixedAssetData()
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
			logger.error("error in FixedAssetData---Init-->" , ex);
		}
	}
	
	public List<FixedAssetModel> getFixedAssetist()
	{
		List<FixedAssetModel> lst=new ArrayList<FixedAssetModel>();
		
		FixedAssetQueries query=new FixedAssetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getFixedAssetQuery());
			while(rs.next())
			{
				FixedAssetModel obj=new FixedAssetModel();
				obj.setAssetid(rs.getInt("AssetID"));
				obj.setAssetCode(rs.getString("AssetCode"));
				obj.setAssetName(rs.getString("AssetName"));
				obj.setAssetMasterDesc(rs.getString("AssetMaster_Description"));
				obj.setUsed(rs.getString("Used"));
				obj.setUnitDesc(rs.getString("Unit_DESCRIPTION"));
				obj.setCategoryDesc(rs.getString("Category_DESCRIPTION"));
				obj.setCountryDesc(rs.getString("Country_DESCRIPTION"));
				obj.setCityDesc(rs.getString("City_DESCRIPTION"));
				obj.setDepartmentDesc(rs.getString("Department_DESCRIPTION"));
				obj.setSectionDesc(rs.getString("Section_DESCRIPTION"));
				obj.setAccountsName(rs.getString("Accounts_Name"));
				obj.setClassName(rs.getString("Class_Name"));
				obj.setCustomerName(rs.getString("Customer_Name"));
				obj.setAssetOpenBalance(rs.getDouble("AssOpenBalValue"));
				obj.setAssetOpenBalDate(rs.getDate("AssOpenBalDate"));
				
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in FixedAssetData---getFixedAssetist-->" , ex);
		}
		return lst;
	}
	
	public FixedAssetModel getFixedAssetById(int assetID)
	{		
		FixedAssetModel obj=new FixedAssetModel();
		FixedAssetQueries query=new FixedAssetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getFixedAssetByIdQuery(assetID));
			while(rs.next())
			{
			
				obj.setAssetid(rs.getInt("AssetID"));
				obj.setAssetCode(rs.getString("AssetCode"));
				obj.setAssetName(rs.getString("AssetName"));
				obj.setAssetMasterDesc(rs.getString("Description"));
				obj.setUsed(rs.getString("Used"));
				obj.setUnitId(rs.getInt("Unit"));
				obj.setCategoryId(rs.getInt("Category"));
				obj.setCountryId(rs.getInt("CountryID"));
				obj.setCityId(rs.getInt("CityID"));
				obj.setDepartmentId(rs.getInt("DepartmentID"));
				obj.setSectionId(rs.getInt("SectionID"));
				obj.setAccountNumber(rs.getInt("AccountNumber"));
				
				
				/*obj.setUnitDesc(rs.getString("Unit_DESCRIPTION"));
				obj.setCategoryDesc(rs.getString("Category_DESCRIPTION"));
				obj.setCountryDesc(rs.getString("Country_DESCRIPTION"));
				obj.setCityDesc(rs.getString("City_DESCRIPTION"));
				obj.setDepartmentDesc(rs.getString("Department_DESCRIPTION"));
				obj.setSectionDesc(rs.getString("Section_DESCRIPTION"));
				obj.setAccountsName(rs.getString("Accounts_Name"));
				obj.setClassName(rs.getString("Class_Name"));
				obj.setCustomerName(rs.getString("Customer_Name"));*/
				
				obj.setAssetOpenBalance(rs.getDouble("AssOpenBalValue"));
				obj.setAssetOpenBalDate(rs.getDate("AssOpenBalDate"));
			
			}
			
		}
		catch (Exception ex) {
			logger.error("error in FixedAssetData---getFixedAssetById-->" , ex);
		}
		return obj;
	}
	
	public List<HRListValuesModel> getHRListValues(int fieldId,String type,int subId)
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();		 		 
			HRListValuesModel obj=new HRListValuesModel();
			if(!type.equals(""))
			{
			obj.setListId(0);					
			obj.setEnDescription(type);
			lst.add(obj);
			}
			
			FixedAssetQueries query=new FixedAssetQueries();
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getHRListValuesQuery(fieldId,subId));
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("ID"));					
					obj.setEnDescription(rs.getString("DESCRIPTION"));
					obj.setArDescription(rs.getString("ARABIC"));
					obj.setSubId(rs.getInt("SUB_ID"));
					obj.setFieldId(rs.getInt("FIELD_ID"));
					obj.setFieldName(rs.getString("FIELD_NAME"));
					obj.setPriorityId(rs.getInt("PRIORITY_ID"));
					obj.setRequired(rs.getString("REQUIRED"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in FixedAssetData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	
	public List<AccountsModel> fillAccounts()
	{
		List<AccountsModel> lst=new ArrayList<AccountsModel>();
		
		FixedAssetQueries query=new FixedAssetQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getAccountsQuery());
			while(rs.next())
			{
				AccountsModel obj=new AccountsModel();
				//obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("Name"));
				//obj.setAccountType(rs.getString("AccountType"));
				//obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in FixedAssetData---fillAccounts-->" , ex);
		}
		return lst;
	}
	
	public int editAsset(FixedAssetModel obj)
	{
		int result=0;
		FixedAssetQueries query=new FixedAssetQueries();
		try
		{
			if(obj.getAssetid()>0)
			db.executeUpdateQuery(query.updateFixedAssetQuery(obj));
			else
			db.executeUpdateQuery(query.addFixedAssetQuery(obj));	
			result=1;
		}
		catch (Exception ex) {
			logger.error("error in FixedAssetData---editAsset-->" , ex);
		}
		return result;
	}
}
