package hr;

import hr.model.SponsorModel;

import java.text.SimpleDateFormat;

import model.BanksModel;

public class SponsorQuerries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String fillSponserInfo()
	{
		query=new StringBuffer();		
		query.append("Select * FROM SPONSORINFO order by sponsor_Name");
		return query.toString();		
	}
	
	public String getSponsorById(int sponsorId)
	{
		  query=new StringBuffer();
		  query.append("Select * from SPONSORINFO where sponsor_key="+sponsorId+"");		
		  return query.toString();
	}
	
	public String getNameFromSponsorForValidation()
	{
		query=new StringBuffer();
		query.append("Select sponsor_Name,sponsor_namear,sponsor_key from SPONSORINFO");
		return query.toString();
		
	}
	public String updateSponsor(SponsorModel obj)
	{
		query=new StringBuffer();		
		query.append("update SPONSORINFO set LastModified=getdate(), sponsor_name='"+obj.getSponsorName()+"',sponsor_namear='"+obj.getSponsorNameArabic() +"',company_id='"+obj.getCompanyId()+"',bank_code='"+obj.getBankCode()+"' Where  sponsor_key='" + obj.getSponsorKey()+"'");
		return query.toString();		
	}
	
	public String getSponsorMaxRecQuerry()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(sponsor_key) as MaxRecNo from SPONSORINFO");		
		  return query.toString();
	}
	
	public String insertSponsor(SponsorModel obj,int recNo)
	{
		  query=new StringBuffer();
		  query.append("insert into SPONSORINFO (sponsor_key,sponsor_name,sponsor_namear,company_id,bank_code,LastModified)values(");
		  query.append(recNo +",'" + obj.getSponsorName() +"' , '" + obj.getSponsorNameArabic() + "', '" + obj.getCompanyId() + "', '" + obj.getBankCode() +"' , getdate() )");
		  return query.toString();
	}
	
	public String getSponsorLastModifiedQuery()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(LastModified) as LastModified from SPONSORINFO");		
		  return query.toString();
	}
}
