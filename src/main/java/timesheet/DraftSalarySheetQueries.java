package timesheet;

public class DraftSalarySheetQueries 
{
	StringBuffer query;
	public String getDBCompany(int companyId)
	{
		 String sql="Select * from companiesdb where dbtype='HR' and companyid=" + companyId ;
		 return sql;
	}
	public String getCompanyListQuery(int UserId)
	{
		 query=new StringBuffer();
		 query.append("SELECT Create_Date,TOT_DEP,TOT_POS,COMPSETUP.COMP_KEY,COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR FROM ");
		 query.append(" COMPSETUP Inner Join Userwebcompany ON COMPSETUP.COMP_KEY =Userwebcompany.companyId");
		 query.append(" where USERID ="  + UserId);
		 query.append(" ORDER BY COMPSETUP.COMP_NAME");
		 return query.toString();		
	}
	
	public String getTotalCompanyEmployees()
	{
		query=new StringBuffer();
		query.append("Select Count(*) as 'TotalEmployees' ,COMP_KEY from EMPMAST group by COMP_KEY");
		return query.toString();			
	}
	
	public String getCompanySalaryStatusQuery(int companyKey,String type)
	{
		query=new StringBuffer();
		query.append("Select Max(Cast(Cast(SALARY_YEAR As VarChar) +  '-'+ Cast(SALARY_MONTH As VarChar) + '-1' as DateTime)) As LASTDATE ");
		query.append(" from COMPSALSTATUS Where COMP_KEY =" + companyKey);
		if(type.equals("C"))
		query.append(" And Not CREATE_NOS=0");
		else if(type.equals("A"))
			query.append(" And Not APPROVE_NOS=0");
		if(type.equals("P"))
			query.append(" And Not PAID_NOS=0");
		
		return query.toString();			
	}
}
