package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

import model.ClassModel;
import model.CompanyDBModel;
import model.QbListsModel;

public class ClassData {
	private Logger logger = Logger.getLogger(ClassData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	ItemsData ItemsData=new ItemsData();
	
	public ClassData()
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
			logger.error("error in ClassData---Init-->" , ex);
		}
	}
	
	public List<ClassModel> fillClassList(String classType,String status)
	{
		List<ClassModel> lst=new ArrayList<ClassModel>();
		
		ClassQuerries query=new ClassQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getClassQuery(classType,status));
			while(rs.next())
			{
				ClassModel obj=new ClassModel();
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("sublevel"));
				obj.setIsActive(rs.getString("isactive"));
				obj.setSubofKey(rs.getInt("sub_of_parent"));
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else
				{
					obj.setIsActive("InActive");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ClassData---fillClassList-->" , ex);
		}
		return lst;
	}
	
	public ClassModel getClassById(int classId)
	{
	
		ClassModel obj=new ClassModel();
		ClassQuerries query=new ClassQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getClassById(classId));
			while(rs.next())
			{
				
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("sublevel"));
				obj.setIsActive(rs.getString("isactive"));
				obj.setSubofKey(rs.getInt("sub_of_parent"));
			}
		}
		catch (Exception ex) {
			logger.error("error in ClassData---getClassById-->" , ex);
		}
		return obj;
	}
	
	public List<ClassModel> getNameFromClassListForValidation()
	{
		List<ClassModel> lst=new ArrayList<ClassModel>();
		
		ClassQuerries query=new ClassQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getNameFromClassListForValidation());
			while(rs.next())
			{
				ClassModel obj=new ClassModel();
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("sublevel"));
				obj.setIsActive(rs.getString("isactive"));
				obj.setSubofKey(rs.getInt("sub_of_parent"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ClassData---getNameFromClassListForValidation-->" , ex);
		}
		return lst;
	}
	
	public List<ClassModel> getClassSubOFValues()
	{
		List<ClassModel> lst=new ArrayList<ClassModel>();
		
		ClassQuerries query=new ClassQuerries();
		ResultSet rs = null;
		try 
		{
			ClassModel objNew=new ClassModel();
			objNew.setClass_Key(0);
			objNew.setName("None");
			objNew.setFullName("None");
			objNew.setSublevel(0);
			objNew.setIsActive("");
			objNew.setSubofKey(0);
			lst.add(objNew);
			rs=db.executeNonQuery(query.getNameFromClassListForValidation());
			while(rs.next())
			{
				ClassModel obj=new ClassModel();
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("sublevel"));
				obj.setIsActive(rs.getString("isactive"));
				obj.setSubofKey(rs.getInt("sub_of_parent"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ClassData---getNameFromClassListForValidation-->" , ex);
		}
		return lst;
	}
	
	public int addClass(ClassModel obj)
	{
		int result=0;
		ClassQuerries query=new ClassQuerries();	
		if(obj.getSlectedSubOfClass()!=null && obj.getSlectedSubOfClass().getClass_Key()!=0)
		{
			ClassModel subOf=new ClassModel();
			subOf=obj.getSlectedSubOfClass();
			obj.setFullName(subOf.getFullName()+":"+obj.getName());
			obj.setSublevel(subOf.getSublevel()+1);
			obj.setParent(subOf.getListID());
			obj.setListID("");
		}
		else
		{
			obj.setFullName(obj.getName());
			obj.setSublevel(0);
			obj.setParent("");
			obj.setListID("");
		}
		
		try 
		{			
			int newID=ItemsData.getMaxID("class", "class_key");
			obj.setClass_Key(newID);
			result=db.executeUpdateQuery(query.addClass(obj));		
			result=1;
		}
		catch (Exception ex) {
			logger.error("error in ClassData---addClass-->" , ex);
		}
		return result;
	}
	
	public int updateClass(ClassModel obj)
	{
		int result=0;
		ClassQuerries query=new ClassQuerries();	
		if(obj.getSlectedSubOfClass()!=null && obj.getSlectedSubOfClass().getClass_Key()!=0)
		{
			ClassModel subOf=new ClassModel();
			subOf=obj.getSlectedSubOfClass();
			obj.setFullName(subOf.getFullName()+":"+obj.getName());
			obj.setSublevel(subOf.getSublevel()+1);
			obj.setParent(subOf.getListID());
		}
		else
		{
			obj.setFullName(obj.getName());
			obj.setSublevel(0);
			obj.setParent("");
		}
		
		try 
		{			
			result=db.executeUpdateQuery(query.updateClass(obj));		
			result=1;
		}
		catch (Exception ex) {
			logger.error("error in ClassData---addClass-->" , ex);
		}
		return result;
	}
	

}
