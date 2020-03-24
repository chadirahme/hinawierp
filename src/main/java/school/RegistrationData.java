package school;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.ApplicantModel;
import model.CompanyDBModel;
import model.CountryModel;
import model.HRListValuesModel;
import model.SchoolModel;
import model.SectionModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class RegistrationData 
{

	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db=new SQLDBHandler("hinawi_school");
	RegistrationQueries query=new RegistrationQueries();
	public RegistrationData()
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
			logger.error("error in RegistrationData---Init-->" , ex);
		}
	}
	
	public List<HRListValuesModel> getHRListValuesList(String type,int fieldID)
	{		
		 List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();				 
		 ResultSet rs = null;
			try 
			{
				if(!type.equals(""))
				{
					HRListValuesModel obj=new HRListValuesModel();
					obj.setFieldId(0);
					obj.setEnDescription(type);
					lst.add(obj);
				}				
				rs=db.executeNonQuery(query.getNationalityQuery(fieldID));
				while(rs.next())
				{					
					HRListValuesModel obj=new HRListValuesModel();
					obj.setFieldId(rs.getInt("ID"));
					obj.setEnDescription(rs.getString("DESCRIPTION"));									
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in RegistrationData---getHRListValuesList-->" , ex);
			}
		 return lst;
	}
	
	public int AddNewApplicant(ApplicantModel obj)
	{
		int appID=0;
		try		
		{
			
		   appID=db.executeUpdateQuery(query.AddNewApplicantQuery(obj));
			
		}
		catch (Exception ex) 
		{
			logger.error("error in RegistrationData---AddNewApplicant-->" , ex);
		}
		return appID;
	}
	
	
}
