package list;
import hr.HRQueries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.ClassModel;
import model.CompanyDBModel;
import model.HRListFieldsModel;
import model.HRListValuesModel;
import model.PropertyModel;
import model.VehicleModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ListData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	ListQueries lstQuery=new ListQueries();
	
	public ListData()
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
			logger.error("error in ListData---Init-->" , ex);
		}
	}

	public List<HRListFieldsModel> fillHRFields(String type)
	{
		List<HRListFieldsModel> lstHRFields=new ArrayList<HRListFieldsModel>();
		HRListFieldsModel obj=new HRListFieldsModel();
		
		if(!type.equals(""))
		{
			obj.setFieldId(0);
			obj.setDescreption(type);
			obj.setArabic(type);
			obj.setNewFlag("N");
			obj.setEditFlag("N");
			obj.setDeleteFlag("N");
			obj.setParentlistId(0);
			lstHRFields.add(obj);
		}
		try
		{
		ResultSet rs = null;
		rs=db.executeNonQuery(lstQuery.getHRListFieldsQuery());
		while(rs.next())
		{			
			obj=new HRListFieldsModel();
			obj.setFieldId(rs.getInt("FIELD_ID"));
			obj.setFieldName(rs.getString("FIELD_NAME"));
			obj.setDescreption(rs.getString("FIELD_DESCRIPTION"));
			obj.setArabic(rs.getString("ARABIC"));
			obj.setNewFlag(rs.getString("NEW_FLAG"));
			obj.setEditFlag(rs.getString("EDIT_FLAG"));
			obj.setDeleteFlag(rs.getString("DELETE_FLAG"));
			obj.setParentlistId(rs.getInt("PARENT_LIST"));
			obj.setLastModified(rs.getDate("LastModified"));
			lstHRFields.add(obj);
		}
		
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---fillHRFields-->" , ex);
		}
		
		return lstHRFields;
	}
	
	public List<HRListValuesModel> getHRListValues(int fieldId,String type,int subId)
	{
		 List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();		 		
		 HRListValuesModel obj=new HRListValuesModel();
			if(!type.equals(""))
			{
			obj.setListId(0);					
			obj.setEnDescription(type);		
			obj.setArDescription(type);
			lst.add(obj);
			}					   			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(lstQuery.getHRListValuesQuery(fieldId,subId));
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("ID"));					
					obj.setEnDescription(rs.getString("DESCRIPTION") == null ? "": rs.getString("DESCRIPTION"));
					obj.setArDescription(rs.getString("ARABIC") == null ? "": rs.getString("ARABIC"));
					obj.setSubId(rs.getInt("SUB_ID"));
					obj.setFieldId(rs.getInt("FIELD_ID"));
					obj.setFieldName(rs.getString("FIELD_NAME"));
					obj.setPriorityId(rs.getInt("PRIORITY_ID"));
					obj.setRequired(rs.getString("REQUIRED"));
					obj.setQbListId(rs.getString("QBListID"));
					obj.setNotes(rs.getString("notes") == null ? "": rs.getString("notes"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in ListData---getHRListValues-->" , ex);
			}
		 return lst;
	}
	private int getMaxID(String tableName,String fieldName)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(lstQuery.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---getMaxID-->" , ex);
		}	
		return result;
	}
	
	public int addNewLocalItemValue(String name,String fullName,int itemTypeRef,String description,String arDescription,int userId)
	{
		int result=0;			
		try 
		{	
			int newID=getMaxID("LocalItem", "RecNo");
			//db.executeUpdateQuery(lstQuery.updateLastModifiedHRListFieldsQuery(fieldID));
			result=db.executeUpdateQuery(lstQuery.addNewLocalItemValueQuery(newID, name, fullName, itemTypeRef, description,arDescription ,userId));	
			result=newID;
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---addNewHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int addNewHRListValue(String DeptName,String DeptNameAr,String fieldName,int fieldID,int priorityId,int subId,String notes)
	{
		int result=0;			
		try 
		{	
			int newID=getMaxID("HRLISTVALUES", "ID");
			db.executeUpdateQuery(lstQuery.updateLastModifiedHRListFieldsQuery(fieldID));
			result=db.executeUpdateQuery(lstQuery.addNewHRListValueQuery(newID, DeptName, DeptNameAr, fieldName, fieldID,priorityId,subId,notes));			
			result=newID;
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---addNewHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int updateHRListValue(int listId,String DeptName,String DeptNameAr,int priorityId,String notes,int fieldID)
	{
		int result=0;		
		try 
		{			
			db.executeUpdateQuery(lstQuery.updateLastModifiedHRListFieldsQuery(fieldID));
			result=db.executeUpdateQuery(lstQuery.updateHRListValueQuery(listId,DeptName, DeptNameAr,priorityId,notes));						
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---updateHRListValue-->" , ex);
		}	
		return result;
	}
	
	public int  deleteHRListValue(int listID)
	{
		int result=0;				
		try 
		{			
			result=db.executeUpdateQuery(lstQuery.deleteHRListValueQuery(listID));					
		}
		catch (Exception ex) {
			logger.error("error in ListData---deleteHRListValue-->" , ex);
		}
		return result;		
	}
	
	public int checkHRListValueUsed(String tableName,String fieldName, int listID)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(lstQuery.checkValueUsedQuery(tableName, fieldName, listID));
			while(rs.next())
			{
				result=rs.getInt("cnt");
			}			
		}
		catch (Exception ex) 
		{
			logger.error("error in ListData---checkHRListValueUsed-->" , ex);
		}	
		return result;
	}
	
	
	
}
