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
import model.CompSetupModel;
import model.CompanyDBModel;
import model.OtherNamesModel;
import model.QbListsModel;
import model.SalesRepModel;

public class SalesRepData {

	private Logger logger = Logger.getLogger(HBAData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public SalesRepData()
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
	
	
	public List<SalesRepModel> fillSalesRepList(String status)
	{
		List<SalesRepModel> lst=new ArrayList<SalesRepModel>();
		SalesRepQuerries query=new SalesRepQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillSalesRepList(status));
			while(rs.next())
			{
				SalesRepModel obj=new SalesRepModel();
				obj.setSlaesRepKey(rs.getInt("salesrep_key"));
				obj.setIntials(rs.getString("initial"));					
				obj.setIsActive(rs.getString("isActive"));
				obj.setQbListKey(rs.getInt("qblistkey"));
				obj.setCommissionFlag(rs.getString("cmnflag"));
				obj.setCommissionPaidBy(rs.getString("cmnpaidby"));
				obj.setCommissionPercent(rs.getDouble("cmnpercentage"));
				obj.setCommissionUsed(rs.getString("cmnused"));
				obj.setSalesRepName(rs.getString("salesrep")==null?"":rs.getString("salesrep"));
				obj.setSalesRepType(rs.getString("salesreptype")==null?"":rs.getString("salesreptype"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else{
					obj.setIsActive("InActive");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in SalesRepData---fillSalesRepList-->" , ex);
		}
		return lst;
	}
	
	public SalesRepModel getsalesRepListByID(int salesrepKey)
	{
		SalesRepModel obj=new SalesRepModel();
		SalesRepQuerries query=new SalesRepQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getsalesRepListByID(salesrepKey));
			while(rs.next())
			{
				obj.setSlaesRepKey(rs.getInt("salesrep_key"));
				obj.setIntials(rs.getString("initial"));					
				obj.setIsActive(rs.getString("isActive"));
				obj.setQbListKey(rs.getInt("qblistkey"));
				obj.setCommissionFlag(rs.getString("cmnflag"));
				obj.setCommissionPaidBy(rs.getString("cmnpaidby"));
				obj.setCommissionPercent(rs.getDouble("cmnpercentage"));
				obj.setCommissionUsed(rs.getString("cmnused"));
				obj.setSalesRepName(rs.getString("salesrep"));
				obj.setSalesRepType(rs.getString("salesreptype"));
			}
		}
		catch (Exception ex) {
			logger.error("error in SalesRepData---getsalesRepListByID-->" , ex);
		}
		return obj;
	}
	
	public CompSetupModel getDefaultSetUpInfoForSalesRep()
	{
		CompSetupModel obj=new CompSetupModel();
		
		SalesRepQuerries query=new SalesRepQuerries();	
		ResultSet rs = null;
		try
		{
			rs=db.executeNonQuery(query.getDefaultSetUpInfoForSalesRep());
			while(rs.next())
			{
				obj.setUseDefaultMaxCommission(rs.getString("maxcommission"));
				obj.setUseDefaultSalesRepCommission(rs.getString("usesalesrepcommission"));
			}
		}
		catch (Exception ex) {
			logger.error("error in SalesRepData---getDefaultSetUpInfoForSalesRep-->" , ex);
		}
		return obj;
	}
	
	
	public int updateSalesRepList(SalesRepModel obj)
	{
		int result=0;
		SalesRepQuerries query=new SalesRepQuerries();	
		try 
		{			
			result=db.executeUpdateQuery(query.updateSalesRepList(obj));	
			
		}
		catch (Exception ex) {
			logger.error("error in SalesRepData---updateBankNmaeList-->" , ex);
		}
		return result;
		
	}
	
	public int getMaxSalesrepId()
	{
		int MaxRecNo=1;
		
		SalesRepQuerries query=new SalesRepQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxSalesrepId());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in SalesRepData---getMaxSalesrepId-->" , ex);
		}
		return MaxRecNo;
	}
	
	public int inserSalesRepQuerry(SalesRepModel salesRepModel,int recNo)
	{
		int result=0;
		
		SalesRepQuerries query=new SalesRepQuerries();		
		try 
		{		
			String str=query.inserSalesRepQuerry(salesRepModel,recNo);
			result=db.executeUpdateQuery(str);			
		}
		catch (Exception ex) 
		{
			logger.error("error in SalesRepData---inserSalesRepQuerry-->" , ex);
		}
		return result;
	}
	
	public List<QbListsModel> fillSalesNameDropDown()
	{
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		
		SalesRepQuerries query=new SalesRepQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillSalesNameDropDown());
			while(rs.next())
			{
				QbListsModel obj=new QbListsModel();
				obj.setName(rs.getString("name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setRecNo(rs.getInt("recno"));
				obj.setListType(rs.getString("listtype"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in SalesRepData---fillSalesNameDropDown-->" , ex);
		}
		return lst;
	}
	
	
}
