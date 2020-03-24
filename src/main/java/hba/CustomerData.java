package hba;

import home.QuotationAttachmentModel;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.CompanyDBModel;
import model.CustomerActivitiesModel;
import model.CustomerContact;
import model.CustomerModel;
import model.TenantModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import common.CompanyProfile;
import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class CustomerData {

	private Logger logger = Logger.getLogger(CustomerData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	DecimalFormat dcf = new DecimalFormat("0.00");

	SQLDBHandler db = new SQLDBHandler("hinawi_hba");
	WebusersModel dbUser;

	public CustomerData() {
		try {
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb = new DBHandler();
			ResultSet rs = null;
			CompanyDBModel obj = new CompanyDBModel();
			dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			if (dbUser != null) {
				HBAQueries query = new HBAQueries();
				rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
						.getCompanyid()));
				while (rs.next()) {
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));
				}
				db = new SQLDBHandler(obj);
			}
		} catch (Exception ex) {
			logger.error("error in CustomerData---Init-->", ex);
		}
	}

	public List<CustomerModel> getCustomerList(String status) {
		List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomersQuery(status));

			while (rs.next()) {
				CustomerModel obj = new CustomerModel();
				obj.setCustkey(rs.getInt("Cust_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive") == null ? "" : rs
						.getString("isactive"));
				obj.setBillCountry(rs.getString("billCountry") == null ? ""
						: rs.getString("billCountry"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setAltphone(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setContact(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setArName(rs.getString("ArName") == null ? "" : rs
						.getString("ArName"));
				obj.setSublevel(rs.getInt("Sublevel"));
				if (obj.getIsactive().equalsIgnoreCase("Y")) {
					obj.setIsactive("Active");
				} else {
					obj.setIsactive("InActive");
				}
				obj.setPhotoPath(rs.getString("OnlinePhotoPath") == null ? ""
						: rs.getString("OnlinePhotoPath"));
				lstCustomers.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerList-->", ex);
		}
		return lstCustomers;
	}

	public CustomerModel getCustomerByKey(int custKey) {
		CustomerModel obj = new CustomerModel();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			rs = db.executeNonQuery(query.getCustomersByKeyQuery(custKey));
			rs1 = db.executeNonQuery(query.getCustomerAdditinal(custKey));
			while (rs.next()) {
				obj.setCustkey(rs.getInt("Cust_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive") == null ? "" : rs
						.getString("isactive"));
				obj.setBillCountry(rs.getString("billCountry") == null ? ""
						: rs.getString("billCountry"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setAltphone(rs.getString("Mobiletelphone2") == null ? ""
						: rs.getString("Mobiletelphone2"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setContact(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setArName(rs.getString("ArName") == null ? "" : rs
						.getString("ArName"));
				obj.setStatusDescription(rs.getString("statusDesc") == null ? ""
						: rs.getString("statusDesc"));
				obj.setSublevel(rs.getInt("sublevel"));
				obj.setPhotoPath(rs.getString("OnlinePhotoPath") == null ? ""
						: rs.getString("OnlinePhotoPath"));
				obj.setCountry(rs.getInt("billcountryrefkey"));
				obj.setCity(rs.getInt("billcityrefkey"));
				obj.setStreet(rs.getInt("billstaterefkey"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? ""
						: rs.getString("PrintChequeAs"));
				obj.setZipCode(rs.getString("billaddress1") == null ? "" : rs
						.getString("billaddress1"));
				obj.setPobox(rs.getString("billaddress2") == null ? "" : rs
						.getString("billaddress2"));
				obj.setAltcontact(rs.getString("AltContact") == null ? "" : rs
						.getString("AltContact"));
				obj.setcC(rs.getString("Cc") == null ? "" : rs.getString("Cc"));
				obj.setWebsite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));
				obj.setSkypeId(rs.getString("SkypeID") == null ? "" : rs
						.getString("SkypeID"));
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setNote(rs.getString("note") == null ? "" : rs
						.getString("note"));
				obj.setVatRegNo(rs.getString("VAT_REGNO") == null ? "" : rs
						.getString("VAT_REGNO"));
				
				obj.setCreditLimit(rs.getDouble("CreditLimit"));
				obj.setSalesRepKey(rs.getInt("SalesRepKey"));//i change from SalesRepRef to send status change email
				obj.setPaymentMethod(rs.getInt("TermsRef"));
				obj.setPriority(rs.getInt("PriorityID"));
				
				if (rs.getDate("cusContractExpiry") != null) {
					obj.setCustomerContactExpiryDateStr(new SimpleDateFormat(
							"dd-MM-yyyy").format(rs
							.getDate("cusContractExpiry")) == null ? ""
							: new SimpleDateFormat("dd-MM-yyyy").format(rs
									.getDate("cusContractExpiry")));
				} else {
					obj.setCustomerContactExpiryDateStr("");
				}
			}
			while (rs1.next()) {
				obj.setCompanyTypeRefKey((int) rs1
						.getFloat("CompanyTypeRefKey"));
				obj.setCompanySizeRefKey((int) rs1
						.getFloat("CompanySizeRefKey"));
				obj.setEmpNos((int) rs1.getFloat("EmpNos"));
				obj.setSoftwareRefKey((int) rs1.getFloat("SoftwareRefKey"));
				obj.setUserNos((int) rs1.getFloat("UserNos"));
				obj.setLastTrialBalance(rs1.getDate("LastTrialBalance"));
				obj.setWorkingHours((int) rs1.getFloat("WorkingHrs"));
				obj.setOwnerName(rs1.getString("OwnerName") == null ? "" : rs1
						.getString("OwnerName"));
				obj.setManageerName(rs1.getString("ManagerName") == null ? ""
						: rs1.getString("ManagerName"));
				obj.setAuditorName(rs1.getString("AuditorName") == null ? ""
						: rs1.getString("AuditorName"));
				obj.setAccountantName(rs1.getString("AccountantName") == null ? ""
						: rs1.getString("AccountantName"));
				obj.setHowdidYouknowus((int) rs1.getFloat("HowKnowRefKey"));							
			}
		}

		catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerByKey-->", ex);
		}
		return obj;
	}

	public int UpdateCustomers(int Cust_Key, String companyName, String name,
			String arName) {
		int result = 0;

		CustomerQuerries query = new CustomerQuerries();
		try {
			result = db.executeUpdateQuery(query.UpdateCustomersQuery(Cust_Key,
					companyName, name, arName));
		} catch (Exception ex) {
			logger.error("error in CustomerData---UpdateCustomers-->", ex);
		}
		return result;

	}

	public int UpdateCustomerData(CustomerModel obj, QuotationAttachmentModel objAtt , List<QuotationAttachmentModel> attachmentModels ) 
	{
		int result = 0;
		CustomerQuerries query = new CustomerQuerries();
		if (obj.getSelectedSubOf() != null
				&& obj.getSelectedSubOf().getCustkey() != 0) {
			CustomerModel subOf = new CustomerModel();
			subOf = obj.getSelectedSubOf();
			obj.setFullName(subOf.getFullName() + ":" + obj.getName());
			obj.setSublevel(subOf.getSublevel() + 1);
			obj.setParent(subOf.getListid());
		} else {
			obj.setFullName(obj.getName());
			obj.setSublevel(0);
			obj.setParent("");
		}
		try {
			String path = "";
			String creationPath = "";
			String repository = "";
			if (objAtt != null && objAtt.getFilename() != null
					&& !objAtt.getFilename().equalsIgnoreCase("")) {
				repository = System.getProperty("catalina.base")
						+ File.separator + "uploads" + File.separator + "";
				path = repository + "Customers" + File.separator
						+ "CutomerPhoto" + File.separator + obj.getCustkey()
						+ File.separator + objAtt.getFilename();
				creationPath = repository + "Customers" + File.separator
						+ "CutomerPhoto" + File.separator + obj.getCustkey()
						+ "";
				createFile(objAtt.getImageMedia().getStreamData(),
						creationPath, objAtt.getFilename());
			}
			obj.setPhotoPath(path);
			ResultSet r = db.executeNonQuery(query.getTenantCustomer(obj
					.getCustkey()));

			if (r.next()) {
				db.executeUpdateQuery(query.UpdateTenantCustomerQuery(
						obj.getTenant(), obj.getCustkey()));
			} else {
				db.executeUpdateQuery(query.addTenantCustomerQuery(
						obj.getTenant(), obj.getCustkey()));
			}

			result = db.executeUpdateQuery(query.UpdateCustomerQuery(obj));
			ResultSet s = db.executeNonQuery(query.getCustomerAdditinal(obj
					.getCustkey()));
			if (s.next()) {
				db.executeUpdateQuery(query.UpdateCustomerAdditinalQuery(obj));
			} else {
				db.executeUpdateQuery(query.addCustomerAdditinalQuery(obj));

			}

			db.executeUpdateQuery(query.updateQBListCustomer(obj));
			deleteCustomerContact(obj.getCustkey());
			
			for (CustomerContact contact : obj.getContacts()) {
				if(null !=contact.getName() && !contact.getName().equalsIgnoreCase(""))
				db.executeUpdateQuery(query.addCustomerContactQuery(contact,
						obj.getCustkey(), getMaxContactLineNo(obj.getCustkey())));
				
			}
			
			//Save Attachment			
			if(attachmentModels!=null && attachmentModels.size()>0)
			{
			db.executeUpdateQuery(query.deleteAllAdditionalAttachments(obj.getCustkey()));
						
			repository =CompanyProfile.CustomersAttachmentRepository(); 								
			creationPath=repository+obj.getCustkey()+"";
				for (QuotationAttachmentModel item : attachmentModels) 
				{
					if (item != null && item.getFilename() != null && !item.getFilename().equalsIgnoreCase("")) 
					{
						if (item.getImageMedia() != null)
							CompanyProfile.createFile(item.getImageMedia(),
									creationPath, item.getFilename());

						item.setFormId(10400);// info doc -- Select * From HRLISTVALUES Where Field_ID=125
						item.setNameType("C");
						item.setAttachid(obj.getCustkey());
						item.setUserId(dbUser.getDesktopUserid());
						db.executeUpdateQuery(query.addAdditionalAttachments(item));
					}
				}
		  }
			

		} catch (Exception ex) {
			logger.error("error in CustomerData---UpdateCustomerData-->", ex);
		}
		return result;

	}

	public int addCustomerData(CustomerModel obj,QuotationAttachmentModel objAtt ,List<QuotationAttachmentModel> attachmentModels) 
	{
		int result = 0;

		CustomerQuerries query = new CustomerQuerries();
		if (obj.getSelectedSubOf() != null
				&& obj.getSelectedSubOf().getCustkey() != 0) {
			CustomerModel subOf = new CustomerModel();
			subOf = obj.getSelectedSubOf();
			obj.setFullName(subOf.getFullName() + ":" + obj.getName());
			obj.setSublevel(subOf.getSublevel() + 1);
			obj.setParent(subOf.getListid());
		} else {
			obj.setFullName(obj.getName());
			obj.setSublevel(0);
			obj.setParent("");
		}
		try {
			int newID = getMaxID("QBLists", "recNo");
			obj.setCustkey(newID);
			String path = "";
			String creationPath = "";
			String repository = "";
			if (objAtt != null && objAtt.getFilename() != null
					&& !objAtt.getFilename().equalsIgnoreCase("")) {
				repository = System.getProperty("catalina.base")
						+ File.separator + "uploads" + File.separator + "";
				path = repository + "Customers" + File.separator
						+ "CutomerPhoto" + File.separator + obj.getCustkey()
						+ File.separator + objAtt.getFilename();
				creationPath = repository + "Customers" + File.separator
						+ "CutomerPhoto" + File.separator + obj.getCustkey()
						+ "";
				createFile(objAtt.getImageMedia().getStreamData(),
						creationPath, objAtt.getFilename());
			}
			obj.setPhotoPath(path);
			result = db.executeUpdateQuery(query.addCustomerQuery(obj));
			db.executeUpdateQuery(query.addTenantCustomerQuery(obj.getTenant(),
					obj.getCustkey()));
			db.executeUpdateQuery(query.addQBListCustomer(obj));
			
			for (CustomerContact contact : obj.getContacts()) {
				db.executeUpdateQuery(query.addCustomerContactQuery(contact,obj.getCustkey(), getMaxContactLineNo(obj.getCustkey())));
				
			}
			db.executeUpdateQuery(query.addCustomerAdditinalQuery(obj));

			//Save Attachment		
			repository =CompanyProfile.CustomersAttachmentRepository(); 			
			creationPath=repository+obj.getCustkey()+"";
			for(QuotationAttachmentModel item :attachmentModels)
			{
				if(item!=null && item.getFilename()!=null && !item.getFilename().equalsIgnoreCase(""))
				{															
					if(item.getImageMedia()!=null)
					{						
						int res =CompanyProfile.createFile(item.getImageMedia(),creationPath,item.getFilename());	
						if(res==0)
						{
							item.setFilepath(creationPath + File.separator+item.getFilename());							
							item.setFormId(10400);//info doc --  Select * From HRLISTVALUES Where  Field_ID=125
							item.setNameType("C");							
							item.setAttachid(obj.getCustkey());							
							item.setUserId(dbUser.getDesktopUserid());	
							db.executeUpdateQuery(query.addAdditionalAttachments(item));
						}
					}												
				}
			}					
			
			// result=1;
		} catch (Exception ex) {
			logger.error("error in CustomerData---addCustomerData-->", ex);
		}
		return result;

	}

	public int createFile(InputStream is, String filePath, String filename) {
		int res = 0;
		try {
			String path = "";
			File targetFile = new File(filePath);
			targetFile.setExecutable(true, false);
			targetFile.setReadable(true, false);
			targetFile.setWritable(true, false);
			// using PosixFilePermission to set file permissions 777
			targetFile.mkdirs();
			File newFile = new File(targetFile.getAbsolutePath(), "" + filename);
			newFile.setExecutable(true, false);
			newFile.setReadable(true, false);
			newFile.setWritable(true, false);

			newFile.canWrite();
			newFile.createNewFile();
			path = newFile.getAbsolutePath();
			File parent = newFile.getAbsoluteFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: "
						+ parent);
			}
			File file = new File(path);
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(file)));
			int c;
			while ((c = is.read()) != -1) {
				out.writeByte(c);
			}
			is.close();
			out.close();
		} catch (Exception ex) {
			res = 1;
			Messagebox.show(ex.getMessage());
		}
		return res;
	}

	public int getMaxID(String tableName, String fieldName) {
		int result = 0;
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while (rs.next()) {
				result = rs.getInt(1) + 1;
			}
			if (result == 0)
				result = 1;

		} catch (Exception ex) {
			logger.error("error in CustomerData---getMaxID-->", ex);
		}
		return result;
	}

	public List<CustomerModel> getCustomerOtherThanCurrentCutomer(int cust_key) {
		List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getCustomerOtherThanCurrentCutomer(cust_key));
			CustomerModel newobj = new CustomerModel();
			newobj.setCustkey(0);
			newobj.setName("None");
			newobj.setSublevel(0);
			lstCustomers.add(newobj);
			while (rs.next()) {
				CustomerModel obj = new CustomerModel();
				obj.setCustkey(rs.getInt("Cust_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive") == null ? "" : rs
						.getString("isactive"));
				obj.setBillCountry(rs.getString("billCountry") == null ? ""
						: rs.getString("billCountry"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setAltphone(rs.getString("Mobiletelphone2") == null ? ""
						: rs.getString("Mobiletelphone2"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setContact(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setArName(rs.getString("ArName") == null ? "" : rs
						.getString("ArName"));
				obj.setStatusDescription(rs.getString("statusDesc") == null ? ""
						: rs.getString("statusDesc"));
				obj.setSublevel(rs.getInt("sublevel"));
				obj.setPhotoPath(rs.getString("OnlinePhotoPath") == null ? ""
						: rs.getString("OnlinePhotoPath"));
				obj.setCountry(rs.getInt("billcountryrefkey"));
				obj.setCity(rs.getInt("billcityrefkey"));
				obj.setStreet(rs.getInt("billstaterefkey"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? ""
						: rs.getString("PrintChequeAs"));
				obj.setZipCode(rs.getString("billaddress1") == null ? "" : rs
						.getString("billaddress1"));
				obj.setPobox(rs.getString("billaddress2") == null ? "" : rs
						.getString("billaddress2"));
				obj.setAltcontact(rs.getString("AltContact") == null ? "" : rs
						.getString("AltContact"));
				obj.setcC(rs.getString("Cc") == null ? "" : rs.getString("Cc"));
				obj.setWebsite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));
				obj.setSkypeId(rs.getString("SkypeID") == null ? "" : rs
						.getString("SkypeID"));
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setNote(rs.getString("note") == null ? "" : rs
						.getString("note"));
				lstCustomers.add(obj);
			}

		} catch (Exception ex) {
			logger.error(
					"error in CustomerData---getCustomerOtherThanCurrentCutomer-->",
					ex);
		}
		return lstCustomers;
	}

	public List<CustomerContact> getCustomerContact(int cust_key) {
		List<CustomerContact> customerContact = new ArrayList<CustomerContact>();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomerContact(cust_key));
			while (rs.next()) {
				CustomerContact obj = new CustomerContact();
				obj.setCust_key(rs.getInt("recno"));
				obj.setLineNo(rs.getInt("lineno"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setPosition(rs.getString("Position") == null ? "" : rs
						.getString("Position"));
				obj.setPhone(rs.getString("telephone1") == null ? "" : rs
						.getString("telephone1"));
				obj.setExtension(rs.getString("telephone2") == null ? "" : rs
						.getString("telephone2"));
				obj.setMobile(rs.getString("mobile1") == null ? "" : rs
						.getString("mobile1"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setDefaultFlag(rs.getString("defaultcont") == null ? "" : rs
						.getString("defaultcont"));
				customerContact.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerContact-->", ex);
		}
		//to handle errro for contacts.get(0)
		if(customerContact.size()==0)
		{
			CustomerContact obj = new CustomerContact();
			customerContact.add(obj);
		}
		
		return customerContact;
	}

	public TenantModel getTenantCustomer(int cust_key) {
		TenantModel Tenant = new TenantModel();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getTenantCustomer(cust_key));
			while (rs.next()) {
				Tenant.setCustkey(rs.getInt("Cust_Key"));
				Tenant.setNationality(rs.getInt("Nationality"));
				Tenant.setTradeLicenseNo(rs.getString("LicenceNo") == null ? ""
						: rs.getString("LicenceNo"));
				Tenant.setTradeLicenseExpiry(rs.getDate("LicenceExpDate"));
				Tenant.setResidenceExpiry(rs.getDate("ResidenceVisaExpDate"));
				Tenant.setPassportExpiry(rs.getDate("PassportExpDate"));
				Tenant.setPassportNo(rs.getString("PassportNo") == null ? ""
						: rs.getString("PassportNo"));
				Tenant.setResidenceVisaNo(rs.getString("ResidenceVisaNo") == null ? ""
						: rs.getString("ResidenceVisaNo"));
				Tenant.setPlaceOfIssue(rs.getString("PlaceOfIssue") == null ? ""
						: rs.getString("PlaceOfIssue"));
				Tenant.setSponserName(rs.getString("SponsorName") == null ? ""
						: rs.getString("SponsorName"));
				Tenant.setEmploymentDesignation(rs.getString("EmpDesignation") == null ? ""
						: rs.getString("EmpDesignation"));
				Tenant.setSalaryIncome(rs.getString("SalaryIncomeDetails") == null ? ""
						: rs.getString("SalaryIncomeDetails"));
			}
		} catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerContact-->", ex);
		}
		return Tenant;
	}

	public void deleteCustomerContact(int Cust_Key) {
		try {
			db.executeUpdateQuery(" Delete from CustomerCotact Where RecNo="
					+ Cust_Key);
		} catch (Exception ex) {
			logger.error("error in CustomerData---deleteCustomerContact-->", ex);
		}
	}

	public List<CustomerActivitiesModel> getCustomerActivities(int custkey,
			int webUserID) {
		List<CustomerActivitiesModel> lstCustomers = new ArrayList<CustomerActivitiesModel>();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		int sno = 0;
		try {
			rs = db.executeNonQuery(query.getCustomerActivity(custkey,
					webUserID));
			while (rs.next()) {
				CustomerActivitiesModel obj = new CustomerActivitiesModel();
				sno++;
				obj.setSno(sno);
				obj.setRecno(rs.getInt("recno"));
				obj.setType(rs.getString("Type") == null ? "" : rs
						.getString("Type"));
				obj.setRefNumber(rs.getString("ref. Number") == null ? "" : rs
						.getString("ref. Number"));
				obj.setDate(rs.getString("Date") == null ? "" : rs
						.getString("Date"));
				obj.setAmount(rs.getString("Amount") == null ? "" : rs
						.getString("Amount"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs
						.getString("Status"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setUser(rs.getString("user") == null ? "" : rs
						.getString("user"));
				lstCustomers.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerActivities-->", ex);
		}
		return lstCustomers;
	}
	
	public List<CustomerActivitiesModel> getCustomerStatusHistory(int cust_key,String type)
	{
		List<CustomerActivitiesModel> lstCustomers = new ArrayList<CustomerActivitiesModel>();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;		
		try {
			rs = db.executeNonQuery(query.getCustomerStatusHistoryQuery(cust_key,type));
			while (rs.next()) {
				CustomerActivitiesModel obj = new CustomerActivitiesModel();				
				obj.setDate(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("ActionDate")));									
				obj.setStatus(rs.getString("CustomerStatusDesc") == null ? "" : rs.getString("CustomerStatusDesc"));						
				obj.setStatusDesc(rs.getString("StatusDescription") == null ? "" : rs.getString("StatusDescription"));
				
				obj.setCreatedFrom(rs.getString("Createdfrom") == "T" ? "TimeSheet" : rs.getString("Createdfrom"));
				obj.setUser(rs.getString("userName") == null ? "" : rs.getString("userName"));
				lstCustomers.add(obj);
				
			}

		} catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerStatusHistory-->", ex);
		}
		return lstCustomers;
	}


	public int getMaxContactLineNo(int recno) {
		int result = 0;
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getMaxContactLineNo(recno));
			while (rs.next()) {
				result = rs.getInt(1) + 1;
			}
			if (result == 0)
				result = 1;

		} catch (Exception ex) {
			logger.error("error in CustomerData---getMaxContactLineNo-->", ex);
		}
		return result;
	}

	public CustomerContact getTopCustomerContact(int recno) {
		CustomerContact obj = new CustomerContact();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getTopCustomerContact(recno));
			while (rs.next()) {
				
				obj.setCust_key(rs.getInt("recno"));
				obj.setLineNo(rs.getInt("lineno"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setPosition(rs.getString("Position") == null ? "" : rs
						.getString("Position"));
				obj.setPhone(rs.getString("telephone1") == null ? "" : rs
						.getString("telephone1"));
				obj.setExtension(rs.getString("telephone2") == null ? "" : rs
						.getString("telephone2"));
				obj.setMobile(rs.getString("mobile1") == null ? "" : rs
						.getString("mobile1"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setDefaultFlag(rs.getString("defaultcont") == null ? "" : rs
						.getString("defaultcont"));
			}
		} catch (Exception ex) {
			logger.error("error in CustomerData---getCustomerContact-->", ex);
		}
		return obj;
	}
	
	public CustomerContact getCOMPANYSETTINGS() 
	{
		CustomerContact obj = new CustomerContact();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCOMPANYSETTINGSQuery());
			while (rs.next()) {
				obj.setEmail(rs.getString("CcEmail")== null ? "" : rs.getString("CcEmail"));
				obj.setDefaultFlag(rs.getString("SENDEMAILFORSTATUS")== null ? "N" : rs.getString("SENDEMAILFORSTATUS"));
				obj.setUseVat(rs.getString("Use_VAT")== null ? "N" : rs.getString("Use_VAT"));
			}			

		} catch (Exception ex) {
			logger.error("error in CustomerData---getCOMPANYSETTINGS-->", ex);
		}
		return obj;
	}
	public String getSalesRepEmail(int SalesRepKey) 
	{
		String email="";
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getSalesRepEmailQuery(SalesRepKey));
			while (rs.next()) {
				email=rs.getString("EmpEmail")== null ? "" : rs.getString("EmpEmail");				
			}			

		} catch (Exception ex) {
			logger.error("error in CustomerData---getSalesRepEmail-->", ex);
		}
		return email;
	}
	
	public List<QuotationAttachmentModel> getAllAdditionalAttachments(int Name_Key)
	{
		List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
		CustomerQuerries query = new CustomerQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getAllAdditionalAttachments(Name_Key));
			while(rs.next())
			{			
				QuotationAttachmentModel obj=new QuotationAttachmentModel();		
				obj.setFormId(rs.getInt("Form_ID"));
				obj.setFilepath(rs.getString("FileName")==null?"":rs.getString("FileName"));
				obj.setFilename(rs.getString("Memo")==null?"":rs.getString("Memo"));
				lst.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in ProspectiveData---getAllAdditionalAttachments-->" , ex);
		}
		return lst;
	}
	
	
}
