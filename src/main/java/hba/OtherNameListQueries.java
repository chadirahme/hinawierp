package hba;

import java.text.SimpleDateFormat;

import model.AccountsModel;
import model.OtherNamesModel;
import model.VendorModel;

public class OtherNameListQueries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getNameFromQbListForNameValidation(String name)
	{
		  query=new StringBuffer();
		  query.append("Select * from QBLists  where name='"+name+"'");		
		  return query.toString();
	}
	
	public String getOtherNameListByID(int OtherNamekey)
	{
		  query=new StringBuffer();
		  query.append("Select * from OtherNames where othNam_key="+OtherNamekey+"");		
		  return query.toString();
	}
	
	public String updateOtherNameList(OtherNamesModel obj)
	{
		query=new StringBuffer();		
		query.append("update OtherNames set timemodified='"+sdf.format(obj.getModifiedDate())+"',name='"+obj.getName()+"',arName='"+obj.getArName() +"',isactive='"+obj.getIsactive()+"',companyname='"+obj.getCompanyName()+"',fullname='"+obj.getName()+"',billaddress1='"+obj.getBillCountry()+"',phone='"+obj.getPhone()+"',altphone='"+obj.getAltphone()+"',fax='"+obj.getFax()+"',email='"+obj.getEmail()+"',contact='"+obj.getContact()+"',altcontact='"+obj.getAlternateContact()+"',accountno='"+obj.getAccountNumber()+"',skypeid='"+obj.getSkypeID()+"',bankname='"+obj.getBankName()+"',branchname='"+obj.getBranchName()+"',actname='"+obj.getAccountName()+"',ibanno='"+obj.getbBANNumber()+"' Where  othNam_key='" + obj.getCustkey()+"'");
		return query.toString();		
	}
	
	public String getOtherListQuery(String status,String sortBy)
	{
		query=new StringBuffer();
		//query.append("Select * from Customer");
		if(status.equalsIgnoreCase(""))
		{
		query.append("Select * from OtherNames Order by "+sortBy);
		}
		else
		{
		query.append("Select * from OtherNames  Where  IsActive='"+status+"' Order by "+sortBy);
		}
		return query.toString();		
	}
	
	public String otherNamesListUpdateQbListQuery(OtherNamesModel obj,int recNo)
	{
		String editedFromOnline="Y";
		String listType="OtherNames";
		String isactive="Y";
		  query=new StringBuffer();
		  query.append("update QBLists set listType='"+listType+"',name='"+obj.getName()+"',fullname='"+ obj.getName()+"',isactive='"+isactive+"',sublevel="+0+",editedFromOnline='"+editedFromOnline+"' where recno="+recNo+"");
		  return query.toString();
	}
	


}
