package hba;

import home.QuotationAttachmentModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import admin.TasksModel;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;

public class ProspectiveQuerries {
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

	public String getNameFromProspectiveForValidation() {
		query = new StringBuffer();
		query.append("Select name,recno from prospective");
		return query.toString();

	}

	public String getFullName() {
		query = new StringBuffer();
		query.append(" select Recno,FullName from Prospective where Status='A' order by FullName");
		return query.toString();

	}

	public String getCategory() {
		query = new StringBuffer();
		query.append(" select RecNo,Name from LocalItem where ItemTypeRef=3");
		return query.toString();

	}

	public String getHowYouKnow() {
		query = new StringBuffer();
		query.append(" select SalesRep_Key,Initial from SalesRep");
		return query.toString();

	}

	public String getProspectiveDetails(double recNo) {
		query = new StringBuffer();
		query.append("  Select * from ProspectiveCotact Where DefaultCont='Y' and RecNo="
				+ recNo);
		return query.toString();

	}

	public String updateProspectiveContactDetails(
			ProspectiveContactDetailsModel prospContactDetails, double recNo) {
		query = new StringBuffer();
		query.append(" update ProspectiveCotact set Name ='"
				+ prospContactDetails.getName() + "',Position='"
				+ prospContactDetails.getPosition() + "',Telephone1='"
				+ prospContactDetails.getTel() + "',Mobile1='"
				+ prospContactDetails.getMobile() + "',Telephone2='"
				+ prospContactDetails.getExtension() + "',Fax='"
				+ prospContactDetails.getFax() + "',EMail='"
				+ prospContactDetails.getEmail() + "' where RecNo=" + recNo);
		return query.toString();

	}

	public String insertProspective(ProspectiveModel prospModel,
			int priorityUpdate, String time) {
		query = new StringBuffer();
		query.append(" insert into Prospective (recNo,timecreated,Name,Arabic,fullname,SubLevel,PrintChequeAs , Address1, Address2, CityRefKey, StreeRefKey, CountryRefKey, HowKnowRefKey, InterestLevel, Contact, Telephone1, Telephone2, Fax, AltContact, AltPhone, Email, CC, WebSite, SkypeID, PriorityID ,ShipTo,ParentRefKey,CompanyTypeRefKey, CompanySizeRefKey, SoftwareRefKey,SalesRepKey, EmpNos, UserNos,isactive,status,"
				+ "PhotoPath,PhotoExist,AccountantName,"
				+ "Ownername, ManagerName, AuditorName");
				if(null!=prospModel.getNotes())
				{
				query.append( ",note ");
				}
				
				if (null != prospModel.getLastTrialBalance()) {
					query.append(", LastTrialBalance");
				}
				query.append(", WorkingHrs) values ("
				+ prospModel.getRecNo()
				+ ",'"
				+ time
				+ "','"
				+ prospModel.getName()
				+ "','"
				+ prospModel.getArName()
				+ "','"
				+ prospModel.getFullname()
				+ "',"
				+ prospModel.getSubLevel()
				+ ",'"
				+ prospModel.getPrintChequeAs()
				+ "','"
				+ prospModel.getPobox()
				+ "','"
				+ prospModel.getZipCode()
				+ "',"
				+ prospModel.getCity()
				+ ","
				+ prospModel.getStreet()
				+ ","
				+ prospModel.getCountry()
				+ ","
				+ prospModel.getHowdidYouknowus()
				+ ",'"
				+ prospModel.getLevelOfInterset()
				+ "','"
				+ prospModel.getContactPerson()
				+ "','"
				+ prospModel.getPhone()
				+ "','"
				+ prospModel.getDirectPhone()
				+ "','"
				+ prospModel.getFax()
				+ "','"
				+ prospModel.getAltContactPerson()
				+ "','"
				+ prospModel.getAltPhone()
				+ "','"
				+ prospModel.getEmail()
				+ "','"
				+ prospModel.getcC()
				+ "','"
				+ prospModel.getWebsite()
				+ "','"
				+ prospModel.getSkypeId()
				+ "',"
				+ priorityUpdate
				+ ",'"
				+ prospModel.getShipTo()
				+ "',"
				+ prospModel.getParent()
				+ ","
				+ prospModel.getCompanyTypeRefKey()
				+ ","
				+ prospModel.getCompanySizeRefKey()
				+ ","
				+ prospModel.getSoftwareRefKey()
				+ ","
				+ prospModel.getSalesRepKey()
				+ ","
				+ prospModel.getTotalComapnyEmployee()
				+ ","
				+ prospModel.getNoUsers()
				+ ",'Y','A','"
				+ prospModel.getPhotoPath()
				+ "','"
				+ prospModel.getPhotoExist()
				+ "','"				
				+ prospModel.getAccountantName()
				+ "','"
				+ prospModel.getOwnerName()
				+ "','"
				+ prospModel.getManageerName()
				+ "','"
				+ prospModel.getAuditorName()+"'");
				if(null!=prospModel.getNotes())
				{
				query.append( ",'"+ prospModel.getNotes().replaceAll("'", "`")+"'");
				}
				
		if (null != prospModel.getLastTrialBalance()) {

			query.append(",'" + df.format(prospModel.getLastTrialBalance())
					+ "'");
		}
		query.append("," + prospModel.getWorkingHours() + ")");
		return query.toString();
	}

	public String insertProspectiveContact(
			ProspectiveContactDetailsModel contactDetailsModel, int RecNo,int lineno) {
		query = new StringBuffer();
		query.append("insert into ProspectiveCotact(RecNo,[lineno],Name,Position,Telephone1,Mobile1,Telephone2,Fax,EMail,DefaultCont) values ("
				+ RecNo
				+ ","
				+ lineno
				+ ",'"
				+ contactDetailsModel.getName()
				+ "','"
				+ contactDetailsModel.getPosition()
				+ "','"
				+ contactDetailsModel.getTel()
				+ "','"
				+ contactDetailsModel.getMobile()
				+ "','"
				+ contactDetailsModel.getExtension()
				+ "','"
				+ contactDetailsModel.getFax()
				+ "','"
				+ contactDetailsModel.getEmail()+ "'");

		if (contactDetailsModel.getDefaultFlag()!=null && contactDetailsModel.getDefaultFlag().equalsIgnoreCase("Y")) {
			query.append(",'" + contactDetailsModel.getDefaultFlag() + "'");
		} else {
			query.append(",'N'");
		}

		query.append( ")");
		return query.toString();
	}

	public String getProspectiveOtherThanCurrentProspective() {
		query = new StringBuffer();
		query.append("Select * from Prospective Order by Replace(FullName,':',':'),PriorityID Desc");
		return query.toString();

	}

	public String getProspectiveByKey(int RecNo) {
		query = new StringBuffer();
		query.append("Select * from Prospective where RecNo =" + RecNo);
		return query.toString();

	}

	public String getProspectiveList(String status) {
		query = new StringBuffer();
		query.append("Select * from Prospective status =" + status);
		return query.toString();

	}

	public String getMaxContactLineNo(int RecNo) {
		query = new StringBuffer();
		query.append(" select max([lineno]) from ProspectiveCotact where recno="
				+ RecNo);
		return query.toString();
	}

	public String UpdateProspective(ProspectiveModel obj,int priorityUpdate) {
		query = new StringBuffer();
		query.append("update Prospective set Name='"+ obj.getName() 
				+ "',PrintChequeAs='"+ obj.getPrintChequeAs() 
				+ "',Arabic='" + obj.getArName()
				+ "',CompanyName='" + obj.getCompanyName() 
				+ "',Address1='"+ obj.getPobox() 
				+ "',Fullname='" + obj.getFullname()
				+ "',Address2 ='" + obj.getZipCode() 
				+ "',CityRefKey='"+ obj.getCity() 
				+ "',StreeRefKey='" + obj.getStreet()
				+ "',CountryRefKey='" + obj.getCountry()
				+ "',HowKnowRefKey='" + obj.getHowdidYouknowus()
				+ "',InterestLevel ='" + obj.getLevelOfInterset()
				+ "',Telephone1='" + obj.getPhone() 
				+ "',Contact='"	+ obj.getContactPerson() 
				+ "',Telephone2='"+ obj.getDirectPhone() 
				+ "',Fax='" + obj.getFax()
				+ "',AltContact='" + obj.getAltContactPerson()
				+ "',AltPhone='" + obj.getAltPhone() 
				+ "',CC='"+ obj.getcC()
				+ "',SkypeID='" + obj.getSkypeId()
				+ "',ShipTo='" + obj.getShipTo()
				+ "',EMail='"+ obj.getEmail() 
				+ "',WebSite='" + obj.getWebsite()
				+ "',PriorityID='" + priorityUpdate 
				+ "',IsActive='" + obj.getIsActive() 
				+ "',PhotoPath='"+ obj.getPhotoPath()
				+ "',PhotoExist='"+ obj.getPhotoExist());
		
				if(null!=obj.getNotes())
				{
					query.append( "',note='" + obj.getNotes().replaceAll("'", "`"));
				}
				query.append( "',SubLevel=" + obj.getSubLevel()
				+ ",CompanyTypeRefKey=" + obj.getCompanyTypeRefKey()
				+ ",CompanySizeRefKey=" + obj.getCompanySizeRefKey()
				+ ",SoftwareRefKey=" + obj.getSoftwareRefKey()
				+ ",SalesRepKey=" + obj.getSalesRepKey()
				+ ",UserNos=" + obj.getNoUsers() 
				+ ",EmpNos="+ obj.getTotalComapnyEmployee() 
				+ ",Ownername='"+ obj.getOwnerName()
				+ "',ManagerName='"+ obj.getManageerName() 
				+ "',AuditorName='"+ obj.getAuditorName() 
				+ "',AccountantName='"+ obj.getAccountantName() 
				+ "',WorkingHrs="+ obj.getWorkingHours()
				+ ",TimeModified=getdate()");
				if (null != obj.getLastTrialBalance()) {
			query.append(",LastTrialBalance='"+ df.format(obj.getLastTrialBalance())+"'");
		}
		query.append( " where RecNo="	+ obj.getRecNo());
		
		return query.toString();

	}
	
	public String getAllAdditionalAttachments(int Name_Key)
	{
		  query=new StringBuffer();		
		  query.append("select * from AdditionalAttachments where Name_Type ='P' and Name_Key="+Name_Key);  		
		  return query.toString();
	}
	
	
	public String addAdditionalAttachments(QuotationAttachmentModel obj)
	{
		  query=new StringBuffer();		 		  
		  query.append(" Insert into AdditionalAttachments (Name_Key,Name_Type,Form_Id,FileName,User_ID,TxnDate,Memo)");
		  query.append(" values("+obj.getAttachid()+",'"+obj.getNameType()+"','"+obj.getFormId()+"','"+obj.getFilepath()+"','"+obj.getUserId()+"', getdate() ,'"+obj.getFilename()+"')");
		  return query.toString();		  
	}
	
	public String deleteAllAdditionalAttachments(int Name_Key)
	{
		  query=new StringBuffer();		
		  query.append("delete from AdditionalAttachments where Name_Type ='P' and Name_Key="+Name_Key);  		
		  return query.toString();
	}
}
