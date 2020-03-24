package hba;

import java.text.SimpleDateFormat;

import model.VendorModel;

public class VendorsQueries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getMaxIDQuery(String tableName,String fieldName)
	{
		query=new StringBuffer();
		query.append(" select max("+ fieldName +") from "+ tableName);
		return query.toString();
	}
	
	public String vendorListInsertQuery(VendorModel obj,int recNo)
	{
		  query=new StringBuffer();
		  query.append("insert into vendor (vend_key,name,fullname,arname,timecreated,companyname,billAddress1,phone,fax,email,contact,ActName,actnumber,AltPhone,ALTCONTACT,SkypeID,BankName,Branch,IBANNo,PrintChequeAs,salutation,cc,website,note,firstname,middlename,lastname)values(");
		  query.append(recNo +",'"+ obj.getName() + "','" + obj.getFullName() +"' , '" + obj.getArName() +"' , '" + sdf.format(obj.getTimeCreated()) + "', '" + obj.getCompanyName() + "', '" + obj.getBillAddress1() + "', '" + obj.getPhone() + "' , '" + obj.getFax() +"' , '" + obj.getEmail() + "', '" + obj.getContact() + "','" + obj.getAccountName() + "','"+obj.getAccountNumber() + "'");
		  query.append(",'"+ obj.getAltPhone() + "', '" + obj.getAltContact() +"' ,'"+ obj.getSkypeId() + "', '" + obj.getBankName() +"' ,'"+ obj.getBranchName() + "', '" + obj.getIbanNo() +"', '" + obj.getName() +"', '" + obj.getSalutation() +"', '" + obj.getcC() +"', '" + obj.getWebSite() +"', '" + obj.getNote() +"', '" + obj.getFirstName() +"', '" + obj.getMiddleName() +"', '" + obj.getLastName() +"')");
		  return query.toString();
	}
	
	public String vendorListInsertQbListQuery(VendorModel obj,int recNo)
	{
		String listType="Vendor";
		String isactive="Y";
		if(obj.getSubLevel()==null)
		{
			obj.setSubLevel("0");
		}
		  query=new StringBuffer();
		  query.append("insert into QBLists (recno,listType,name,fullname,isactive,sublevel)values(");
		  query.append(recNo +",'" + listType +"' , '" + obj.getName() + "', '" + obj.getName() + "', '" + isactive + "',"+obj.getSubLevel()+")");
		  return query.toString();
	}
	
	public String getVendorsQuery(String status)
	{
		query=new StringBuffer();		
		query.append("Select * ");
		if(status.equalsIgnoreCase(""))
		{
		query.append(" from Vendor Order by vend_key");
		}
		else{
			query.append(" from Vendor Where  IsActive='"+status+"' Order by vend_key");
		}
		return query.toString();		
	}
	//Edit Vendor
	public String getVendorByKeyQuery(int vendKey)
	{
		query=new StringBuffer();		
		query.append("Select * from Vendor  Where  Vend_Key = " + vendKey);
		return query.toString();		
	}
	public String UpdateVendorQuery(VendorModel obj)
	{
		query=new StringBuffer();		
		query.append("update Vendor set name='" + obj.getName() + "',fullname='" + obj.getName() + "',timemodified='" + sdf.format(obj.getTimeModified()) + "',ActName='" + obj.getAccountName() + "',actnumber='" + obj.getAccountNumber() + "',SkypeID='" + obj.getSkypeId() + "',Branch='" + obj.getBranchName() + "',BankName='" + obj.getBankName() + "',AltPhone='" + obj.getAltPhone() + "',ALTCONTACT='" + obj.getAltContact() + "',note='" + obj.getNote() + "',salutation='" + obj.getSalutation() + "',IBANNo='" + obj.getIbanNo() + "',companyName='" + obj.getCompanyName() +"',arName='" + obj.getArName() 
	    + "',phone='" + obj.getPhone() +"',firstname='" + obj.getFirstName() +"',middlename='" + obj.getMiddleName() +"',lastname='" + obj.getLastName() +"',cc='" + obj.getcC() +"',WebSite='" +obj.getWebSite() +"',fax='"+obj.getFax()+"',email='"+obj.getEmail()+"',BillAddress1='"+obj.getBillAddress1()+"'" +
	    		",contact='"+obj.getContact()+"' ,IsActive='"+obj.getIsActive()+"' ,PrintChequeAs='"+obj.getPrintChequeAs()+"' ,VAT_REGNO='" + obj.getVatRegNo()  +"'  Where  vend_key=" + obj.getVend_Key());
		return query.toString();		
	}
	public String addVendorQuery(VendorModel obj)
	{
		obj.setFullName(obj.getName());
		query=new StringBuffer();		
		query.append("INSERT INTO Vendor(vend_key,TimeCreated,Name,ArName,IsActive,CompanyName,phone,WebSite,fax,email,contact,PrintChequeAs,BillAddress1,firstname,middlename,lastname,cc,ActName,actnumber,SkypeID,Branch,BankName,AltPhone,ALTCONTACT,note,salutation,IBANNo,VAT_REGNO)");
		query.append(" VALUES( " + obj.getVend_Key() + ",getdate(), '" + obj.getName()+"' , '" + obj.getArName()+"' , ");
		query.append("'" + obj.getIsActive()+"'  , '" + obj.getCompanyName()+"' , '" +obj.getPhone() +"' , '" + obj.getWebSite() +"' , '"+obj.getFax() + "' ,"  );
		query.append(" '" + obj.getEmail() + "' , '" + obj.getContact() + "' , '" + obj.getPrintChequeAs() + "' , '" + obj.getBillAddress1() + "' , '" + obj.getFirstName() +"' , '" + obj.getMiddleName() +"' , '" + obj.getLastName() +"' , '" + obj.getcC() +"' , '" + obj.getAccountName() +"' , '" + obj.getAccountNumber() +"' , '" + obj.getSkypeId() +"' , '" + obj.getBranchName() +"' , '" + obj.getBankName() +"' , '" + obj.getAltPhone() +"' , '" + obj.getAltContact() +"' , '" + obj.getNote() +"' , '" + obj.getSalutation() +"' , '" + obj.getIbanNo() +"' , '" + obj.getVatRegNo() + "'" );
		query.append(" )");
		return query.toString();		
	}
	
	public String vendorListUpdateQbListQuery(VendorModel obj,int recNo)
	{
		String editedFromOnline="Y";
		String listType="Vendor";
		String isactive="Y";
		if(obj.getSubLevel()==null)
		{
			obj.setSubLevel("0");
		}
		  query=new StringBuffer();
		  query.append("update QBLists set listType='"+listType+"',name='"+obj.getName()+"',fullname='"+ obj.getName()+"',isactive='"+isactive+"',sublevel="+obj.getSubLevel()+",editedFromOnline='"+editedFromOnline+"' where recno="+recNo+"");
		  return query.toString();
	}

}
