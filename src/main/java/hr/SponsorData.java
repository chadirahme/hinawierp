package hr;
import hr.model.SponsorModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class SponsorData {
	
	private Logger logger = Logger.getLogger(SponsorData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public SponsorData()
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
				HRQueries query=new HRQueries();
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
			logger.error("error in SponsorData---Init-->" , ex);
		}
	}
	
	
	public List<SponsorModel> fillSponserInfo()
	{
		List<SponsorModel> lst=new ArrayList<SponsorModel>();
		SponsorQuerries query=new SponsorQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.fillSponserInfo());
			while(rs.next())
			{
				SponsorModel obj=new SponsorModel();
				obj.setSponsorKey(rs.getInt("sponsor_key"));
				obj.setSponsorName(rs.getString("sponsor_name"));					
				obj.setSponsorNameArabic(rs.getString("sponsor_namear"));
				obj.setTelePhone(rs.getString("telephone"));
				obj.setFax(rs.getString("fax"));
				obj.setNotes(rs.getString("notes"));
				obj.setCompanyId(rs.getString("company_id"));
				obj.setBankCode(rs.getString("bank_code"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in SponsorData---fillSponserInfo-->" , ex);
		}
		return lst;
	}
	
	public SponsorModel getSponsorById(int sponsorId)
	{
		SponsorModel obj=new SponsorModel();
		SponsorQuerries query=new SponsorQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSponsorById(sponsorId));
			while(rs.next())
			{
				obj.setSponsorKey(rs.getInt("sponsor_key"));
				obj.setSponsorName(rs.getString("sponsor_name"));					
				obj.setSponsorNameArabic(rs.getString("sponsor_namear"));
				obj.setTelePhone(rs.getString("telephone"));
				obj.setFax(rs.getString("fax"));
				obj.setNotes(rs.getString("notes"));
				obj.setCompanyId(rs.getString("company_id"));
				obj.setBankCode(rs.getString("bank_code"));
			}
		}
		catch (Exception ex) {
			logger.error("error in SponsorData---getSponsorById-->" , ex);
		}
		return obj;
	}
	
	public List<SponsorModel> getNameFromSponsorForValidation()
	{
		List<SponsorModel> lst=new ArrayList<SponsorModel>();
		
		SponsorQuerries query=new SponsorQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNameFromSponsorForValidation());
			while(rs.next())
			{
				SponsorModel obj=new SponsorModel();
				obj.setSponsorKey(rs.getInt("sponsor_key"));
				obj.setSponsorName(rs.getString("sponsor_name"));					
				obj.setSponsorNameArabic(rs.getString("sponsor_namear"));
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in SponsorData---getNameFromSponsorForValidation-->" , ex);
		}
		return lst;
	}
	
	
	public int updateSponsor(SponsorModel obj)
	{
		int result=0;
		SponsorQuerries query=new SponsorQuerries();	
		try 
		{			
			result=db.executeUpdateQuery(query.updateSponsor(obj));	
			
		}
		catch (Exception ex) {
			logger.error("error in SponsorData---updateSponsor-->" , ex);
		}
		return result;
		
	}
	
	public int getSponsorMaxRecQuerry()
	{
		int MaxRecNo=1;
		
		SponsorQuerries query=new SponsorQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSponsorMaxRecQuerry());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo")+1;						
			}
		}
		catch (Exception ex) {
			logger.error("error in SponsorData---getSponsorMaxRecQuerry-->" , ex);
		}
		return MaxRecNo;
	}
	
	public int inserBankNameQuerry(SponsorModel sponsorModel,int recNo)
	{
		int result=0;
		
		SponsorQuerries query=new SponsorQuerries();		
		try 
		{		
			String str=query.insertSponsor(sponsorModel,recNo);
			result=db.executeUpdateQuery(str);			
		}
		catch (Exception ex) 
		{
			logger.error("error in SponsorData---inserBankNameQuerry-->" , ex);
		}
		return result;
	}
	
	public Date getSponsorLastModified()
	{
		Date lastModified=null;
		
		SponsorQuerries query=new SponsorQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSponsorLastModifiedQuery());
			while(rs.next())
			{
				lastModified=rs.getDate("LastModified");						
			}
		}
		catch (Exception ex) {
			logger.error("error in SponsorData---getSponsorLastModified-->" , ex);
		}
		return lastModified;
	}
	

}
