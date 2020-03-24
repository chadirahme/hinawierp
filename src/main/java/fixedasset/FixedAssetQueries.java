package fixedasset;

import java.text.SimpleDateFormat;

import model.FixedAssetModel;

public class FixedAssetQueries 
{

	StringBuffer query;
	public String getFixedAssetQuery()
	{
		query=new StringBuffer();
		 query.append("Select * from [Assets List Query]");
		return query.toString();		
	}
	
	public String getFixedAssetByIdQuery(int AssetID)
	{
		query=new StringBuffer();
		 query.append("Select * from AssetMaster where AssetID=" + AssetID);
		return query.toString();		
	}
	
	public String getHRListValuesQuery(int fieldId,int subId)
	{
		query=new StringBuffer();
		 query.append("Select ID, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID from HRLISTVALUES");
		 if(subId==0)
			 query.append(" where FIELD_ID=" + fieldId);
			 else
			 query.append(" where SUB_ID=" + subId);
		 
		// query.append(" where FIELD_ID=" + fieldId);
		 query.append(" order by DESCRIPTION");
		return query.toString();		
	}
	
	public String getAccountsQuery()
	{
		query=new StringBuffer();
		 query.append("select * from Accounts order by AccountName" );
		return query.toString();		
	}
	
	public String updateFixedAssetQuery(FixedAssetModel obj)
	{
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		 query=new StringBuffer();
		 query.append("Update AssetMaster set  AssetName='%s' ,Description='%s' , Unit=%d , Category=%d , Used='%s' ,");
		 query.append(" CountryID=%d , CityID=%d , DepartmentID=%d , SectionID=%d , AssOpenBalValue='%s' , AssetCode='%s' ,AccountNumber=%d ,AssOpenBalDate='%s' ");
		 query.append(" ");
		 query.append(" ");
		 query.append(" ");
		 query.append(" ");
		 query.append(" ");
		 query.append(" ");
		 query.append(" where AssetID=" + obj.getAssetid());
		 String sql=query.toString();
		 sql=String.format(sql,obj.getAssetName(), obj.getAssetMasterDesc() , obj.getUnitId(),obj.getCategoryId(),obj.getUsed() ,
				 obj.getCountryId(),obj.getCityId(),obj.getDepartmentId(),obj.getSectionId(),obj.getAssetOpenBalance() , obj.getAssetCode(),obj.getAccountNumber(),
				 sdf.format(obj.getAssetOpenBalDate()));
		return sql;			
	}
	public String addFixedAssetQuery(FixedAssetModel obj)
	{
		 query=new StringBuffer();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		 query.append(" INSERT INTO AssetMaster(AssetCode,AssetName ,Description, Unit, Category, Used ,");
		 query.append(" CountryID, CityID , DepartmentID , SectionID , AssOpenBalValue , AssOpenBalDate,StatusID,AccountNumber )");
		 query.append(" values ('%s' , '%s', '%s' , %d ,%d , '%s' ,%d,%d,%d,%d ,'%s', '%s', %d ,%d)");		
		 String sql=query.toString();
		 sql=String.format(sql,obj.getAssetCode(), obj.getAssetName(), obj.getAssetMasterDesc() , obj.getUnitId(),obj.getCategoryId(),obj.getUsed() ,
				 obj.getCountryId(),obj.getCityId(),obj.getDepartmentId(),obj.getSectionId(),obj.getAssetOpenBalance(),sdf.format(obj.getAssetOpenBalDate()) , 0 ,
				 obj.getAccountNumber());
		return sql;			
	}
}
