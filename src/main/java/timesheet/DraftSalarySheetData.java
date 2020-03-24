package timesheet;

import hr.model.CompanyModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.CompanyDBModel;
import model.DraftSalaryModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class DraftSalarySheetData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DraftSalarySheetQueries query=new DraftSalarySheetQueries();
	public DraftSalarySheetData()
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
			logger.error("error in DraftSalarySheetData---Init-->" , ex);
		}
	}
	
	public List<CompanyModel> getCompanyList(int userID)
	{
		  List<CompanyModel> lst=new ArrayList<CompanyModel>();		 			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getCompanyListQuery(userID));
				while(rs.next())
				{
					CompanyModel obj=new CompanyModel();
					obj.setCompKey(rs.getInt("COMP_KEY"));					
					obj.setEnCompanyName(rs.getString("COMP_NAME"));
					obj.setArCompanyName(rs.getString("COMP_NAME_AR"));	
					obj.setCreateDate(rs.getDate("Create_Date"));
					obj.setTotalDepartment(rs.getInt("TOT_DEP"));
					obj.setTotalPositions(rs.getInt("TOT_POS"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in DraftSalarySheetData---getCompanyList-->" , ex);
			}
		 return lst;
	}
	
	public List<DraftSalaryModel> getTotalCompanyEmployees()
	{
		  List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();		 			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getTotalCompanyEmployees());
				while(rs.next())
				{
					DraftSalaryModel obj=new DraftSalaryModel();
					obj.setCompKey(rs.getInt("COMP_KEY"));										
					obj.setTotalEmployees(rs.getInt("TotalEmployees"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in DraftSalarySheetData---getTotalCompanyEmployees-->" , ex);
			}
		 return lst;
	}
	
	public String getLastCompanySalaryDate(int companyKey,String type)
	{
		String result="";
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCompanySalaryStatusQuery(companyKey, type));
			while(rs.next())
			{
				Date lastDate=rs.getDate("LASTDATE");	
				if(lastDate!=null)
				{
					  Calendar c = Calendar.getInstance();	
					  c.setTime(lastDate);
					  String monthName =new SimpleDateFormat("MM").format(c.getTime());
					  int year= c.get(Calendar.YEAR);	
					  result=monthName + "/" + String.valueOf(year) ;
				}
			}
			
			if(result.equals("") && type.equals("C"))
			{
				result="Not yet created";
			}
			if(result.equals("") && type.equals("A"))
			{
				result="Not yet Approved";
			}
			if(result.equals("") && type.equals("P"))
			{
				result="Not yet Paid";
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getLastCompanySalaryDate-->" , ex);
		}
		
		return result;
	}
}
