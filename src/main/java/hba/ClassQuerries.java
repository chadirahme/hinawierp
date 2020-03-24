package hba;

import java.text.SimpleDateFormat;

import model.ClassModel;

public class ClassQuerries {
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getClassQuery(String classType,String status)
	{
		query=new StringBuffer();
		query.append("SELECT NAME,CLASS_KEY,sublevel,fullname,isactive,sub_of_parent FROM CLASS"); 
		if(!status.equals(""))
		{
			query.append(" where IsActive='" + status +"'");
		}
		if(!classType.equals(""))
		{
		query.append(" and CLASS_TYPE='" + classType +"' ");	
		}
		query.append(" ORDER BY FullName");
		return query.toString();		
	}	
	
	public String getClassById(int classId)
	{
		query=new StringBuffer();
		query.append("SELECT NAME,CLASS_KEY,sublevel,fullname,isactive,sub_of_parent  FROM CLASS where class_key="+classId+"");
		return query.toString();		
	}	
	
	public String getNameFromClassListForValidation()
	{
		query=new StringBuffer();
		query.append("Select * from class ORDER BY FullName");
		return query.toString();
		
	}
	
	public String addClass(ClassModel obj)
	{
		query=new StringBuffer();
		query.append("insert into Class (ListID,Class_Key,Name,FullName,IsActive,SubLevel,sub_of_parent)values('"+obj.getListID()+"',"+obj.getClass_Key()+",'"+obj.getName()+"','"+obj.getFullName()+"','"+obj.getIsActive()+"',"+obj.getSublevel()+","+obj.getSlectedSubOfClass().getClass_Key()+")");
		return query.toString();
		
	}
	
	public String updateClass(ClassModel obj)
	{
		String editedFromOnline="Y";
		query=new StringBuffer();
		query.append("update class set ListID='"+obj.getListID()+"', Name='"+obj.getName()+"', FullName='"+obj.getFullName()+"',IsActive='"+obj.getIsActive()+"',SubLevel="+obj.getSublevel()+",sub_of_parent="+obj.getSlectedSubOfClass().getClass_Key()+",editedFromOnline='"+editedFromOnline+"' where class_key="+obj.getClass_Key()+"");
		return query.toString();
		
	}

}
