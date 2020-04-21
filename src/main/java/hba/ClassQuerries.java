package hba;

import java.text.SimpleDateFormat;

import model.ClassModel;

public class ClassQuerries {
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getClassQuery(String classType,String status)
	{
		query=new StringBuffer();
		query.append("SELECT NAME,NameArabic,CLASS_KEY,sublevel,FullName,isactive,sub_of_parent,ArFullName FROM CLASS");
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
		query.append("SELECT NAME,CLASS_KEY,sublevel,fullname,isactive,sub_of_parent,NameArabic,Parent,ListID" +
				"  FROM CLASS where class_key="+classId+"");
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
		query.append("insert into Class (ListID,Class_Key,Name,FullName,IsActive,SubLevel,sub_of_parent,NameArabic,ArFullName,Parent )" +
				"values('"+obj.getListID()+"',"+obj.getClass_Key()+",'"+obj.getName()+"','"+obj.getFullName()+"','"+obj.getIsActive()+"',"+obj.getSublevel()+","+obj.getSubofKey());
		query.append( ", '"+obj.getArabicName()+"','"+obj.getArFullName()+ "','"+obj.getParent()+"'");
		query.append(")");
		return query.toString();
		
	}
	
	public String updateClass(ClassModel obj)
	{
		String editedFromOnline="Y";
		query=new StringBuffer();
		query.append("update class set Name='"+obj.getName()+"', FullName='"+obj.getFullName()+"',IsActive='"+obj.getIsActive()
				+"',SubLevel="+obj.getSublevel()+",sub_of_parent="+obj.getSubofKey()+",editedFromOnline='"+editedFromOnline+"' ,NameArabic='"+obj.getArabicName()+"' ,ArFullName='"+obj.getArFullName()
				+"', Parent='"+obj.getParent()
				+"' where class_key="+obj.getClass_Key()+"");
		return query.toString();
		
	}

}
