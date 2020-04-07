package hba;

import home.QuotationAttachmentModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import model.CustomerContact;
import model.CustomerModel;
import model.TenantModel;

public class CustomerQuerries {
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	public String getCustomersQuery(String status) {
		query = new StringBuffer();
		// query.append("Select * from Customer");
		query.append("Select * from Customer ");
		if (!status.equals(""))
			query.append(" Where  IsActive='" + status + "' ");
		query.append(" Order by Replace(FullName,':',':'),PriorityID Desc");
		return query.toString();
	}

	public String UpdateCustomersQuery(int Cust_Key, String companyName,
			String name, String arName) {
		query = new StringBuffer();
		query.append("update Customer set name='" + name + "',companyName='"
				+ companyName + "',arName='" + arName + "'  Where  Cust_Key="
				+ Cust_Key);
		return query.toString();
	}

	public String getCustomersByKeyQuery(int custKey) {
		query = new StringBuffer();
		query.append("Select * from Customer  Where  Cust_key = " + custKey);
		return query.toString();
	}

	public String UpdateCustomerQuery(CustomerModel obj) {
		query = new StringBuffer();
		query.append("update Customer set name='" + obj.getName()
				+ "',companyName='" + obj.getCompanyName() + "',arName='"
				+ obj.getArName() + "',phone='" + obj.getPhone()
				+ "',Mobiletelphone2='" + obj.getAltphone() + "',fax='"
				+ obj.getFax() + "',email='" + obj.getEmail() + "',fullname='"
				+ obj.getFullName() + "',parent='" + obj.getParent()
				+ "',sublevel=" + obj.getSublevel() + ",contact='"
				+ obj.getContact() + "',IsActive='" + obj.getIsactive()
				+ "',OnlinePhotoPath='" + obj.getPhotoPath() + "',statusDesc='"
				+ obj.getStatusDescription() + "',BillAddress1='"
				+ obj.getZipCode() + "',BillAddress2='" + obj.getPobox()
				+ "',BillStateRefKey=" + obj.getStreet() + ",BillCityRefKey="
				+ obj.getCity() + ",TermsRef=" + obj.getPaymentMethod()
				+ ",CreditLimit=" + obj.getCreditLimit() + ",SalesRepKey=" //change SalesRepRef to SalesRepKey
				+ obj.getSalesRepKey() + ",BillCountryRefKey="
				+ obj.getCountry() + ",PriorityID=" + obj.getPriority()
				+ ",AltContact='" + obj.getAltcontact() + "',AltPhone='"
				+ obj.getMobile() + "',Cc='" + obj.getcC() + "',WebSite='"
				+ obj.getWebsite() + "',SkypeID='" + obj.getSkypeId()
				+ "',shipto='" + obj.getShipTo() +"' ,VAT_REGNO='" + obj.getVatRegNo()
				+ "',PrintChequeAs='"+obj.getPrintChequeAs()
				+ "',TimeModified=getdate()");
				
		if (null != obj.getNote()) {
			query.append(",note='" + obj.getNote().replaceAll("'", "`")+"'");
		}
		query.append(" Where  Cust_Key=" + obj.getCustkey());
		return query.toString();
	}

	public String UpdateTenantCustomerQuery(TenantModel obj, int custkey)
			throws Exception {
		query = new StringBuffer();
		query.append("update tenant set PlaceOfIssue='" + obj.getPlaceOfIssue()
				+ "',LicenceNo='" + obj.getTradeLicenseNo() + "',Nationality="
				+ obj.getNationality() + ",PassportNo='" + obj.getPassportNo()
				+ "',ResidenceVisaNo='" + obj.getResidenceVisaNo()
				+ "',SponsorName='" + obj.getSponserName()
				+ "',EmpDesignation='" + obj.getEmploymentDesignation()
				+ "',SalaryIncomeDetails='" + obj.getSalaryIncome() + "'");
		if (null != obj.getTradeLicenseExpiry()) {
			query.append(",ResidenceVisaExpDate='"
					+ df.format(obj.getTradeLicenseExpiry()) + "'");
		}
		if (null != obj.getPassportExpiry()) {
			query.append(",PassportExpDate='"
					+ df.format(obj.getPassportExpiry()) + "'");
		}
		if (null != obj.getResidenceExpiry()) {
			query.append(",LicenceExpDate='"
					+ df.format(obj.getResidenceExpiry()) + "'");
		}
		query.append(" Where Cust_Key=" + custkey);
		return query.toString();
	}

	public String UpdateCustomerAdditinalQuery(CustomerModel obj) {
		query = new StringBuffer();
		query.append("update CustomerAdditional set CompanyTypeRefKey="
				+ obj.getCompanyTypeRefKey() + ",CompanySizeRefKey="
				+ obj.getCompanySizeRefKey() + ",SoftwareRefKey="
				+ obj.getSoftwareRefKey() + ",UserNos=" + obj.getUserNos()
				+ ",EmpNos=" + obj.getEmpNos());
		if (null != obj.getLastTrialBalance()) {
			query.append(",LastTrialBalance='"
					+ df.format(obj.getLastTrialBalance()) + "'");
		}
		query.append(",WorkingHrs=" + obj.getWorkingHours() + ",OwnerName='"
				+ obj.getOwnerName() + "',ManagerName='" 
				+ obj.getManageerName() + "',AuditorName='"
				+ obj.getAuditorName() + "',AccountantName='"
				+ obj.getAccountantName() 
				+ "',HowKnowRefKey='" + obj.getHowdidYouknowus()
				+ "' Where RecNo=" + obj.getCustkey());
		return query.toString();
	}

	public String getMaxIDQuery(String tableName, String fieldName) {
		query = new StringBuffer();
		query.append(" select max(" + fieldName + ") from " + tableName);
		return query.toString();
	}

	public String addTenantCustomerQuery(TenantModel obj, int custkey)
			throws Exception {

		query = new StringBuffer();
		query.append("INSERT INTO tenant(Cust_Key, PlaceOfIssue, LicenceNo, Nationality, PassportNo, ResidenceVisaNo,"
				+ "SponsorName, EmpDesignation, SalaryIncomeDetails");
		if (null != obj.getTradeLicenseExpiry()) {
			query.append(", LicenceExpDate");
		}
		if (null != obj.getPassportExpiry()) {
			query.append(", PassportExpDate");
		}
		if (null != obj.getResidenceExpiry()) {
			query.append(",ResidenceVisaExpDate");
		}
		query.append(") VALUES(" + custkey + ",'" + obj.getPlaceOfIssue()
				+ "','" + obj.getTradeLicenseNo() + "','"
				+ obj.getNationality() + "','" + obj.getPassportNo() + "','"
				+ obj.getResidenceVisaNo() + "','" + obj.getSponserName()
				+ "','" + obj.getEmploymentDesignation() + "','"
				+ obj.getSalaryIncome() + "'");
		if (null != obj.getTradeLicenseExpiry()) {
			query.append(",'" + df.format(obj.getTradeLicenseExpiry()) + "'");
		}
		if (null != obj.getPassportExpiry()) {
			query.append(",'" + df.format(obj.getPassportExpiry()) + "'");
		}
		if (null != obj.getResidenceExpiry()) {
			query.append(",'" + df.format(obj.getResidenceExpiry()) + "'");
		}
		query.append(")");
		return query.toString();
	}

	public String addCustomerAdditinalQuery(CustomerModel obj) {
		query = new StringBuffer();
		query.append("insert into CustomerAdditional(RecNo,CompanyTypeRefKey,CompanySizeRefKey,SoftwareRefKey,Ownername,ManagerName,AuditorName,AccountantName,EmpNos,UserNos,WorkingHrs,HowKnowRefKey");
		if (null != obj.getLastTrialBalance()) {
			query.append(",LastTrialBalance");
		}
		//+ "',HowKnowRefKey='" + obj.getHowdidYouknowus()
		query.append(") values (" + obj.getCustkey() + ","
				+ obj.getCompanyTypeRefKey() + "," + obj.getCompanySizeRefKey()
				+ "," + obj.getSoftwareRefKey() + ",'" + obj.getOwnerName()
				+ "','" + obj.getManageerName() + "','" + obj.getAuditorName()
				+ "','" + obj.getAccountantName() + "'," + obj.getEmpNos()
				+ "," + obj.getUserNos() + "," + obj.getWorkingHours()+ "," + obj.getHowdidYouknowus());
		if (null != obj.getLastTrialBalance()) {

			query.append(",'" + df.format(obj.getLastTrialBalance()) + "'");
		}
		query.append(")");
		return query.toString();
	}

	public String addCustomerContactQuery(CustomerContact contact, int custkey,
			int lineNo) {
		query = new StringBuffer();
		query.append("insert into customerCotact(RecNo,[lineno],[Name],Position,Telephone1,Mobile1,Telephone2,Fax,EMail,DefaultCont) values ("
				+ custkey
				+ ","
				+ lineNo
				+ ",'"
				+ contact.getName()
				+ "','"
				+ contact.getPosition()
				+ "','"
				+ contact.getPhone()
				+ "','"
				+ contact.getMobile()
				+ "','"
				+ contact.getExtension()
				+ "','"
				+ contact.getFax() + "','" + contact.getEmail() + "'");

		if (contact.getDefaultFlag()!=null && contact.getDefaultFlag().equalsIgnoreCase("Y")) {
			query.append(",'" + contact.getDefaultFlag() + "'");
		} else {
			query.append(",'N'");
		}

		query.append(")");
		return query.toString();
	}

	public String addCustomerQuery(CustomerModel obj) {
		query = new StringBuffer();
		query.append("INSERT INTO Customer(Cust_Key,TimeCreated,TimeModified,Name,ArName,FullName,Parent,IsActive,Sublevel,shipto,phone,Mobiletelphone2,fax,email,contact,OnlinePhotoPath,statusDesc,PrintChequeAs"
				+ ",BillAddress1, BillAddress2, BillStateRefKey, BillCityRefKey, BillCountryRefKey, PriorityID, AltContact, AltPhone, Cc, WebSite, SkypeID,VAT_REGNO");

		if (null != obj.getNote()) {
			query.append(",note");
		}

		query.append(",TermsRef,SalesRepRef,CreditLimit) VALUES( "
				+ obj.getCustkey() + ",getdate(),getdate(),'" + obj.getName()
				+ "','" + obj.getArName() + "','" + obj.getFullName() + "','"
				+ obj.getParent() + "','" + obj.getIsactive() + "',"
				+ obj.getSublevel() + ",'" + obj.getShipTo() + "','"
				+ obj.getPhone() + "','" + obj.getAltphone() + "','"
				+ obj.getFax() + "','" + obj.getEmail() + "','"
				+ obj.getContact() + "','" + obj.getPhotoPath() + "','"
				+ obj.getStatusDescription() + "','" + obj.getPrintChequeAs()
				+ "','" + obj.getZipCode() + "','" + obj.getPobox() + "',"
				+ obj.getStreet() + "," + obj.getCity() + ","
				+ obj.getCountry() + "," + obj.getPriority() + ",'"
				+ obj.getAltcontact() + "','" + obj.getMobile() + "','"
				+ obj.getcC() + "','" + obj.getWebsite() + "','"
				+ obj.getSkypeId() + "','" + obj.getVatRegNo() +"' ");
		if (null != obj.getNote()) {
			query.append(",'" + obj.getNote().replaceAll("'", "`") + "'");
		}
		query.append("," + obj.getPaymentMethod() + "," + obj.getSalesRepKey()
				+ "," + obj.getCreditLimit() + ")");
		return query.toString();
	}

	public String addQBListCustomer(CustomerModel obj) {
		query = new StringBuffer();
		String listType = "Customer";
		query.append("INSERT INTO qblists(listType,recNo,Name,ArName,FullName,Parent,IsActive,Sublevel)");
		query.append(" VALUES( '" + listType + "'," + obj.getCustkey() + " , '"
				+ obj.getName() + "' , ");
		query.append(" '" + obj.getArName() + "' , '" + obj.getFullName()
				+ "' , '" + obj.getParent() + "', '" + obj.getIsactive()
				+ "' , " + obj.getSublevel() + "");
		query.append(" )");
		return query.toString();
	}

	public String updateQBListCustomer(CustomerModel obj) {
		String editedFromOnline = "Y";
		query = new StringBuffer();
		String listType = "Customer";
		query.append("update qblists set listType='" + listType + "',Name='"
				+ obj.getName() + "',ArName='" + obj.getArName()
				+ "',FullName='" + obj.getFullName() + "',Parent='"
				+ obj.getParent() + "',IsActive='" + obj.getIsactive()
				+ "',Sublevel=" + obj.getSublevel() + ",editedFromOnline='"
				+ editedFromOnline + "' where recNo=" + obj.getCustkey() + "");
		return query.toString();
	}

	public String getCustomerOtherThanCurrentCutomer(int cust_key) {
		query = new StringBuffer();
		query.append("Select * from Customer Order by Replace(FullName,':',':'),PriorityID Desc");
		return query.toString();
	}

	public String getCustomerContact(int cust_key) {
		query = new StringBuffer();
		query.append("Select * from customercotact where recno=" + cust_key
				+ " order by defaultCont desc");
		return query.toString();
	}

	public String getTenantCustomer(int cust_key) {
		query = new StringBuffer();
		query.append("Select * from Tenant where Cust_Key=" + cust_key);
		return query.toString();
	}

	public String getCustomerAdditinal(int cust_key) {
		query = new StringBuffer();
		query.append("Select * from CustomerAdditional where recno=" + cust_key);
		return query.toString();
	}

	public String getCustomerActivity(int cust_key, int webuserid) {

		query = new StringBuffer();
		query.append("Select recno as 'recno','inquiry' as type,refnumber as 'ref. Number',txndate as 'Date','' as 'Amount',status as 'Status',memo as 'Memo',userid as 'User' From Inquiry where Inquiry.ClientType='C' And Inquiry.ProspectiveRefKey ="
				+ cust_key);
		query.append(" union all ");
		query.append("Select recno as 'recno','visits' as type,refnumber as 'ref. Number',txndate as 'Date','' as 'Amount',status as 'Status',memo as 'Memo',userid as 'User' From visits Where Visits.CustomerRefKey="
				+ cust_key);
		query.append(" union all ");
		query.append("Select recno as 'recno','Quotation' as type,refnumber as 'ref. Number',txndate as 'Date',amount as 'Amount',StatusDesc as 'Status',memo as 'Memo','' as 'User' From Quotation Where Quotation.ClientType='C' And Quotation.CustomerRefKey ="
				+ cust_key);
		if (webuserid > 0) {
			query.append(" and webUserID=" + webuserid);
		}
		query.append(" union all ");
		query.append("Select recno as 'recno','Delivery' as type,refnumber as 'ref. Number',txndate as 'Date',amount as 'Amount',Status as 'Status',memo as 'Memo','' as 'User' From Delivery Where Delivery.CustomerRefKey ="
				+ cust_key);
		query.append(" union all ");
		query.append("Select  CheckMast.recno as 'recno','Cash Payment' as 'Type',CheckMast.PVNo as 'ref. Number', CheckMast.PVDate as 'Date',CheckMast.Amount as 'Amount',CheckMast.Status As 'Status',CheckMast.Memo as 'Memo', CheckMast.UserID as 'User' From (CheckMast Left Join Accounts On CheckMast.BankKey=Accounts.REC_NO) Where CheckMast.Cheque in ('Cash') And CheckMast.PayeeKey="
				+ cust_key);
		query.append(" union all ");
		query.append("Select CheckMast.recno as 'recno','Cheque Payment' as 'Type',CheckMast.PVNo as 'Ref. Number',CheckMast.CheckDate as 'Date',CheckMast.Amount as 'Amount',CheckMast.Status As 'Status' ,CheckMast.Memo as 'Memo', CheckMast.UserID as 'User' From (CheckMast Left Join Accounts On CheckMast.BankKey=Accounts.REC_NO) Where CheckMast.Cheque in ('Cheque') And CheckMast.PayeeKey="
				+ cust_key);
		query.append(" union all ");
		query.append("Select CheckMast.recno as 'recno','BankTransfer' as 'Type',CheckMast.PVNo as 'ref. Number',CheckMast.CheckDate as 'Date',CheckMast.Amount as 'Amount' ,CheckMast.Status As 'Status',CheckMast.Memo as 'Memo',CheckMast.UserID as 'User' From (CheckMast Left Join Accounts On CheckMast.BankKey=Accounts.REC_NO) Where CheckMast.Cheque in ('BankTransfer') And CheckMast.PayeeKey="
				+ cust_key);
		query.append(" union all ");
		query.append("Select RVMast.Rec_No as 'recno','Cheque Receipt Voucher' as type,RVMast.RefNumber as 'ref. Number',RVMast.TxnDate as 'Date',RVMast.TotalAmount as 'Amount',RVMast.Status as 'Status', RVMast.Memo as 'Memo', RvMast.UserID as 'User' From (RVMAST Left Join Accounts AS ARAccountList ON RVMast.ArAccountRef_Key=ARAccountList.Rec_No) Where RVMAST.mode in ('Cheque') And RVMAST.CustomerRef_key ="
				+ cust_key);
		query.append(" union all ");
		query.append("Select RVMast.Rec_No as 'recno','Cash & Cheque Receipt Voucher' as type,RVMast.RefNumber as 'ref. Number',RVMast.TxnDate as 'Date',RVMast.TotalAmount as 'Amount',RVMast.Status As 'Status',RVMast.Memo as 'Memo',RvMast.UserID as 'User' From (RVMAST Left Join Accounts AS ARAccountList ON RVMast.ArAccountRef_Key=ARAccountList.Rec_No) Where RVMAST.mode in ('Both') And RVMAST.CustomerRef_key ="
				+ cust_key);
		if (webuserid > 0) {
			query.append(" and RVMast.webUserID=" + webuserid);
		}
		query.append(" union all ");
		query.append("Select recno as 'recno','Cash Invoice' as type,SalesReceipt.RefNumber as 'ref. Number',SalesReceipt.txndate as 'Date' ,SalesReceipt.amount as 'Amount',SalesReceipt.Status as 'Status',SalesReceipt.memo as 'Memo',SalesReceipt.recno as 'User' From (SalesReceipt LEFT JOIN Accounts AS AccountsList ON SalesReceipt.DepositAccountRefKey = AccountsList.REC_NO) Where SalesReceipt.CustomerRefKey="
				+ cust_key);
		if (webuserid > 0) {
			query.append(" and SalesReceipt.webUserID=" + webuserid);
		}
		query.append(" union all ");
		query.append("Select recno as 'recno','Credit Invoice' as type,Invoice.RefNumber as 'ref. Number',Invoice.txndate as 'Date' ,Invoice.amount as 'Amount',Invoice.Status as 'Status',Invoice.memo as 'Memo',Invoice.recno as 'User' From (Invoice LEFT JOIN Accounts AS ARAccountList ON Invoice.ARAccountRefKey = ARAccountList.REC_NO) Where Invoice.CustomerRefKey="
				+ cust_key);
		if (webuserid > 0) {
			query.append(" and Invoice.webUserID=" + webuserid);
		}
		query.append(" union all ");
		query.append("Select JournalEntry.rec_no as 'recno','Journal Voucher' as type,JournalEntry.RefNumber as 'ref. Number',JournalEntry.TxnDate as 'Date',JournalLine.Amount As 'Amount',JournalEntry.status As 'Status',JournalLine.memo as 'Memo','' as 'User' From ((JournalEntry Left Join JournalLine ON JournalEntry.Rec_no = JournalLine.Rec_No) LEFT JOIN Accounts ON JournalLine.AccountRef_key= Accounts.REC_NO)Where JournalLine.EntityRef_Key ="
				+ cust_key
				+ " And Accounts.AccountType in ('AccountsReceivable') AND JournalLine.EntityRef_Key<>0");
		query.append(" union all ");
		query.append("Select RVMast.Rec_No as 'recno','CUC' as 'Type',RvMast.RefNumber as 'Ref. Number',RvMast.TxnDate as 'Date',RVmast.TotalAmount as 'Amount',RVmast.Status as 'Status',RVMast.memo as 'Memo',RvMast.UserID as 'User' From (RvMast Left Join Accounts ArAccount on ArAccount.Rec_No = RvMast.ArAccountRef_Key) where  RvMast.Mode in ('Cheque-Opening') AND RvMast.CustomerRef_key="
				+ cust_key);
		return query.toString();
	}

	public String getCustomerStatusHistoryQuery(int cust_key,String type) 
	{
		query = new StringBuffer();
		query.append(" Select CustomerStatusHistory.*, StatusList.Description As CustomerStatusDesc,Users.UserName ");
		query.append("   from ((CustomerStatusHistory Left Join HRLISTVALUES As StatusList  ON CustomerStatusHistory.StatusID = StatusList.ID) ");
		query.append(" Left Join Users   ON CustomerStatusHistory.UserID  = Users.UserID) ");
		query.append("  Where CustomerstatusHistory.CustKey =" + cust_key);
		query.append(" And (CustomerStatusHistory.StatusID > 0 Or IsNull(CustomerStatusHistory.StatusDescription,'')<>'')");		
		query.append("  And CustomerStatusHistory.Type in ('" + type +"')");	
		query.append("  order by ActionDate desc");
		return query.toString();
	}
	
	
	public String getMaxContactLineNo(int RecNo) {
		query = new StringBuffer();
		query.append(" select max([lineno]) from customercotact where recno="
				+ RecNo);
		return query.toString();
	}

	public String getTopCustomerContact(int recno) {
		query = new StringBuffer();
		query.append("Select top 1 *  from customercotact where recno="+recno+" order by defaultCont desc");
		return query.toString();
	}
	
	public String getCOMPANYSETTINGSQuery() {
		query = new StringBuffer();
		query.append("Select CcEmail ,SENDEMAILFORSTATUS,USE_VAT from COMPANYSETTINGS");
		return query.toString();
	}
	public String getSalesRepEmailQuery(int SalesRepKey) {
		query = new StringBuffer();
		query.append("Select Qblists.Recno, Qblists.ListType,Customer.Email as CustEmail,OtherNames.Email as OthEmail,EMPCONTACT.Details as EmpEmail,Vendor.Email as vendEmail ");
		query.append(" from Qblists left join Customer   On Qblists.recno     = Customer.Cust_key ");
		query.append("  left join othernames On Qblists.recno     = othernames.OthNam_Key");
		query.append(" left join EMPCONTACT On Qblists.HREmp_Key = EMPCONTACT.Emp_Key And Contact_ID = 622");
		query.append("  left join Vendor     On Qblists.recno = Vendor.Vend_Key ");
		query.append("  Where Qblists.Recno =" + SalesRepKey);
		query.append(" ");
		return query.toString();
	}
	
	public String getAllAdditionalAttachments(int Name_Key)
	{
		  query=new StringBuffer();		
		  query.append("select * from AdditionalAttachments where Name_Type ='C' and Name_Key="+Name_Key);  		
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
		  query.append("delete from AdditionalAttachments where Name_Type ='C' and Name_Key="+Name_Key);  		
		  return query.toString();
	}
}
