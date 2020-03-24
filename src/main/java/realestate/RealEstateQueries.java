package realestate;

public class RealEstateQueries 
{
	StringBuffer query;
	public String getRealEstateUnitListQuery()
	{
		query=new StringBuffer();
		 query.append("Select * from vw_RealEstateUnitList");
		return query.toString();		
	}
	
	public String getVacantListQuery()
	{
		query=new StringBuffer();
		query.append("Select * from vw_VacantList");
		return query.toString();		
	}
}
