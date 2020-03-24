package company;

import hr.HRQueries;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CompanyDBModel;
import model.UserActivityModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class UserActivityData 
{
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	private Logger logger = Logger.getLogger(this.getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
	public UserActivityData()
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
			logger.error("error in UserActivityData---Init-->" , ex);
		}
	}
	
	public List<String> getUsersList(int companyId)
	{
		List<String> lst=new ArrayList<String>();
		try
		{
			
			//get the username from MYSQL users table
			List<String> lstLocalUsers=new ArrayList<String>();
			CompanyData data=new CompanyData();
			List<WebusersModel> lstUsers=data.getUsersList(companyId);
			for (WebusersModel webusersModel : lstUsers) 
			{
				if(!lstLocalUsers.contains(webusersModel.getUsername()))
				lstLocalUsers.add(webusersModel.getUsername());
			}
			
			lstLocalUsers.add(0,"All");
			if(lstLocalUsers.size()>0)
			return lstLocalUsers;
			
			ResultSet rs = null;
			String query=" SELECT distinct UserName FROM USERWEBCOMPANY where UserName is not null order by UserName";
			rs=db.executeNonQuery(query);
			while(rs.next())
			{		
				if(lstLocalUsers.contains(rs.getString("UserName")))
				lst.add(rs.getString("UserName"));
			}			
			lst.add(0,"All");
		}
		catch (Exception ex) 
		{
			logger.error("error in UserActivityData---getUsersList-->" , ex);
		}
		return lst;
	}
	
	public List<UserActivityModel> getUsersActivity(Date fromDate, Date toDate,String userName,int formId)
	{
		List<UserActivityModel> lst=new ArrayList<UserActivityModel>(); 
		try
		{
			SimpleDateFormat dbsdf = new SimpleDateFormat("yyyy-MM-dd");
			ResultSet rs = null;
			String query=" SELECT     USERACTIVITY.USER_ID, USERACTIVITY.FORM_ID, USERACTIVITY.ACTIVITY_ID, USERACTIVITY.CREATE_DATE, USERACTIVITY.CREATE_TIME, " +
					"  USERACTIVITY.REC_NO, USERACTIVITY.EMP_KEY, USERACTIVITY.COMP_KEY, USERACTIVITY.DESCRIPTION, USERACTIVITY.WebUserID, " +
					"USERWEBCOMPANY.UserID, COMPSETUP.COMP_NAME,  EMPMAST.ENGLISH_FULL,EMPMAST.EMP_NO ,USERWEBCOMPANY.UserName " +
					" FROM         USERACTIVITY INNER JOIN USERWEBCOMPANY ON USERACTIVITY.WebUserID = USERWEBCOMPANY.UserID AND USERACTIVITY.COMP_KEY = USERWEBCOMPANY.CompanyID INNER JOIN " +
					"  COMPSETUP ON USERACTIVITY.COMP_KEY = COMPSETUP.COMP_KEY left JOIN  EMPMAST ON USERACTIVITY.EMP_KEY = EMPMAST.EMP_KEY " +
					//" WHERE     (USERACTIVITY.FORM_ID = 68) AND (USERACTIVITY.USER_ID = 0)" +
					" WHERE (USERACTIVITY.USER_ID = 0)" +
					" and ( USERACTIVITY.CREATE_DATE  Between '"+ dbsdf.format(fromDate) + "' and '" + dbsdf.format(toDate) + "' )";
					if(!userName.equals("All"))
					{
						query+=" and USERWEBCOMPANY.UserName='" + userName +"'";
					}
					if(formId!=0)
					{
						query+=" and USERACTIVITY.FORM_ID='" + formId +"'";
					}
			
			rs=db.executeNonQuery(query);
			while(rs.next())
			{
				UserActivityModel obj=new UserActivityModel();
				obj.setFormName(getFormName(rs.getInt("FORM_ID")));
				obj.setActivity(getActivityName(rs.getInt("ACTIVITY_ID")));
				obj.setUserName(rs.getString("UserName"));
				obj.setLogDate(sdf.format(rs.getDate("CREATE_DATE")));
				obj.setLogTime(tdf.format(rs.getDate("CREATE_TIME")));
				obj.setEmployeeNumber(rs.getString("EMP_NO"));
				obj.setEmployeeName(rs.getString("ENGLISH_FULL"));
				obj.setCompanyName(rs.getString("COMP_NAME"));
				obj.setDescription(rs.getString("DESCRIPTION"));
				lst.add(obj);				
			}					
		}
		
		catch (Exception ex) 
		{
			logger.error("error in UserActivityData---getUsersActivity-->" , ex);
		}
		
		return lst;
	}
	
	private String getFormName(int formID)
	{
		String formName="";
		if(formID==68)
			formName="Timesheet Detailed";
		if(formID==49)
			formName="Leave Request";
		if(formID==47)
			formName="Passport Request";
		if(formID==55)
			formName="Loan Request";
		if(formID==75)
			formName="Timesheet Salary Sheet";
		
		return formName;
	}
	private String getActivityName(int activityID)
	{
		String activityName="";
		if(activityID==1)
			activityName="New";
		if(activityID==2)
			activityName="Edit";
		if(activityID==3)
			activityName="Approve";
		
		return activityName;
	}
	
}
