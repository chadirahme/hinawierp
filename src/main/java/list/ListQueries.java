package list;

public class ListQueries 
{
	StringBuffer query;
	public String getHRListFieldsQuery()
	{
		 query=new StringBuffer();
		 query.append("SELECT FIELD_ID, FIELD_NAME, FIELD_DESCRIPTION, ARABIC, NEW_FLAG, EDIT_FLAG, DELETE_FLAG, PARENT_LIST, REQUIRED,LastModified");
		 query.append(" FROM HRLISTFIELDS where NEW_FLAG ='Y'");
		 query.append(" order by FIELD_DESCRIPTION");
		return query.toString();		
	}
	
	public String getHRListValuesQuery(int fieldId ,int subId)
	{
		query=new StringBuffer();
		 query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID,notes from HRLISTVALUES");
		 if(subId==0)
		 query.append(" where FIELD_ID=" + fieldId);
		 else
		 query.append(" where SUB_ID=" + subId + " and FIELD_ID=" + fieldId);
		 
		 query.append(" order by PRIORITY_ID,DESCRIPTION");
		return query.toString();		
	}
	
	public String getMaxIDQuery(String tableName,String fieldName)
	{
		query=new StringBuffer();
		query.append(" select max("+ fieldName +") from "+ tableName);
		return query.toString();
	}
	public String addNewLocalItemValueQuery(int recNo,String name,String fullName,int itemTypeRef,String description,String arDescription, int userId)
	{
		String tempNotes=""; 
		if(tempNotes!=null)
		{
			tempNotes=description.replaceAll("'","`");
		}
		query=new StringBuffer();
		query.append(" insert into LocalItem (RecNo,ItemTypeRef,Name,FullName,Description,DescriptionAR,UserID,ModifiedDate,LastViewedBy,ViewDate)");
		query.append(" values ( "+recNo+",'" + itemTypeRef + "','" +name+"','"+fullName+"','"+description+"','"+ arDescription +"', '" +userId + "' ,getdate() , " +userId  +",getdate() )");
		return query.toString();
	}
	
	public String addNewHRListValueQuery(int ID,String DeptName,String DeptNameAr,String fieldName,int fieldID,int priorityId,int subId,String notes)
	{
		String tempNotes=""; 
		if(tempNotes!=null)
		{
			tempNotes=notes.replaceAll("'","`");
		}
		query=new StringBuffer();
		query.append(" insert into HRLISTVALUES (ID,DESCRIPTION,ARABIC,SUB_ID,FIELD_ID,FIELD_NAME,DEF_VALUE,REQUIRED,PRIORITY_ID,notes)");
		query.append(" values ( "+ID+",'"+DeptName+"','"+DeptNameAr+"',"+subId+","+ fieldID +", '" +fieldName + "','','N'," +priorityId  +",'"+tempNotes+"');");
		return query.toString();
	}
	public String updateHRListValueQuery(int ID,String DeptName,String DeptNameAr,int priorityId,String notes)
	{
		String tempNotes=""; 
		if(tempNotes!=null)
		{
			tempNotes=notes.replaceAll("'","`");
		}
		query=new StringBuffer();
		query.append(" update HRLISTVALUES set DESCRIPTION='" + DeptName + "' , ARABIC='" + DeptNameAr + "' , PRIORITY_ID="+priorityId+",notes='"+tempNotes+"' ");
		query.append(" where ID="+ID);
		return query.toString();
	}
	public String deleteHRListValueQuery(int listID)
	{
		query=new StringBuffer();
		query.append("  delete from HRLISTVALUES where ID=" + listID);
		return query.toString();		
	}
	
	public String checkValueUsedQuery(String tableName,String fieldName, int listID)
	{
		query=new StringBuffer();
		query.append("  select count(*) as cnt FROM " + tableName + " WHERE " + fieldName + " =" + listID);
		return query.toString();		
	}
	
	public String updateLastModifiedHRListFieldsQuery(int ID)
	{	
		query=new StringBuffer();
		query.append(" update HRLISTFIELDS set LastModified=getdate()");
		query.append(" where FIELD_ID="+ID);
		return query.toString();
	}
}
