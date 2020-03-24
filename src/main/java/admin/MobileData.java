package admin;

import hr.HRQueries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CompanyDBModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;

import db.DBHandler;
import db.SQLDBHandler;

public class MobileData {

	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db=new SQLDBHandler("hinawi_school");
	public MobileData()
	{
		try
		{		
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();			
			if(true)
			{
				HRQueries query=new HRQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(4));
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
			logger.error("error in MobileData---Init-->" , ex);
		}	
	}
	
	public List<CompanyDBModel> getMobileCompanyList()
	{
		 List<CompanyDBModel> lst=new ArrayList<CompanyDBModel>();		
		 ResultSet rs=null;
		 CompanyDBModel obj=new CompanyDBModel();
		 obj.setCompanyId(0);
		 obj.setCompanyName("Select");
		 lst.add(obj);
		 try
			{
			 String sql="Select * from MobileCompanies ORDER BY CompanyName";			 
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 obj=new CompanyDBModel();
				 obj.setCompanyId(rs.getInt("CompanyId"));
				 obj.setCompanyName(rs.getString("CompanyName"));
				 obj.setServerName(rs.getString("ServerName"));
				 obj.setDbname(rs.getString("DbName"));
				 obj.setDbuser(rs.getString("DbUser"));
				 obj.setDbpwd(rs.getString("DbPassword"));
				 obj.setHr(rs.getInt("Hr")==1?true :false);
				 obj.setAccounting(rs.getInt("Accounting")==1?true :false);
				 obj.setRealEstate(rs.getInt("Realestate")==1?true :false);
				 obj.setAdmin(rs.getInt("Admin")==1?true :false);
				 obj.setActive(rs.getInt("Active")==1?true :false);
				 obj.setLastUpdated(rs.getDate("LastUpdated"));				 
				 lst.add(obj);
			 }
			 
			}
		  catch (Exception ex) 
			{
			  logger.error("error in MobileData---getMobileCompanyList-->" , ex);			
			}
		 return lst;
	}
	
	public int UpdateMobileCompany(CompanyDBModel obj)
	{
		int result=0;		
		try 
		{			
			result=db.executeUpdateQuery(updateMobileCompaniesQuery(obj));		
		}
		catch (Exception ex) {
			logger.error("error in MobileData---UpdateMobileCompany-->" , ex);
		}
		return result;		
	}
	
	private String updateMobileCompaniesQuery(CompanyDBModel obj)
	{

		StringBuffer query=new StringBuffer();
		query.append(" update MobileCompanies set ServerName ='%s' , DbName='%s' , DbUser ='%s' , "
		 		+ " DbPassword ='%s', Hr='%s', Accounting='%s', Realestate='%s' , Admin='%s' , Active='%s' , LastUpdated=getdate() ");		 	
		 query.append(" where CompanyId = "  + obj.getCompanyId());		
	
		return query.toString().format(query.toString() , obj.getServerName() ,obj.getDbname(),obj.getDbuser() , obj.getDbpwd() ,obj.getHr()?1:0,
				obj.getAccounting()?1:0,obj.getRealEstate()?1:0,obj.getAdmin()?1:0,obj.isActive()?1:0);
								
	}
	
	
		
}
