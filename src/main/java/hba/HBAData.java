package hba;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import layout.MenuModel;
import model.AccountsModel;
import model.ActivityStatusModel;
import model.ApprovePurchaseOrderModel;
import model.ApprovedMaterialsModel;
import model.ApprovedQuotationModel;
import model.BalanceSheetReportModel;
import model.BankTransferModel;
import model.BanksModel;
import model.BarcodeSettingsModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.CashInvoiceSalesReportModel;
import model.CashModel;
import model.ChangeStatusQuotationModel;
import model.CheckFAItemsModel;
import model.CheckItemsModel;
import model.ClassModel;
import model.CompSetupModel;
import model.CompanyDBModel;
import model.CustomerModel;
import model.CustomerStatusHistoryModel;
import model.CutomerSummaryReport;
import model.DeliveryLineModel;
import model.DeliveryModel;
import model.DepreciationModel;
import model.ExpensesModel;
import model.FixedAssetModel;
import model.ItemReceiptModel;
import model.ItemReceiptReportModel;
import model.JobModel;
import model.JournalVoucherGridData;
import model.JournalVoucherModel;
import model.LocalItemModel;
import model.OtherNamesModel;
import model.PayToOrderModel;
import model.PaymentMethod;
import model.PropertyModel;
import model.PurchaseRequestGridData;
import model.PurchaseRequestModel;
import model.PurchaseRequestReportModel;
import model.QbListsModel;
import model.SelectItemReceiptModel;
import model.SerialFields;
import model.TermModel;
import model.VehicleModel;
import model.VendorModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import company.CompanyData;
import db.DBHandler;
import db.SQLDBHandler;

public class HBAData {
	private Logger logger = Logger.getLogger(HBAData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	// DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	// SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf = new DecimalFormat("0.00");

	SQLDBHandler db = new SQLDBHandler("hinawi_hba");

	public HBAData() {
		try {
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb = new DBHandler();
			ResultSet rs = null;
			CompanyDBModel obj = new CompanyDBModel();
			WebusersModel dbUser = (WebusersModel) sess
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
			logger.error("error in HBAData---Init-->", ex);
		}
	}

	public List<CustomerModel> getCustomerList(String status) {
		List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
		HBAQueries query = new HBAQueries();
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
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setAltphone(rs.getString("Mobiletelphone2") == null ? ""
						: rs.getString("Mobiletelphone2"));
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
				obj.setNote(rs.getString("note") == null ? "" : rs
						.getString("note"));
				
			obj.setCreatedDate(rs.getDate("TimeCreated"));
				lstCustomers.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getCustomerList-->", ex);
		}
		return lstCustomers;
	}
	
	public List<CustomerModel> getCustomersContractExpiryList(String status) {
		List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomersContractExpiryQuery(status));
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
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
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
				if (rs.getDate("cusContractExpiry") != null) {
					obj.setCustomerContactExpiryDateStr(new SimpleDateFormat(
							"dd-MM-yyyy").format(rs
									.getDate("cusContractExpiry")) == null ? ""
											: new SimpleDateFormat("dd-MM-yyyy").format(rs
													.getDate("cusContractExpiry")));
				} else {
					obj.setCustomerContactExpiryDateStr("");
				}
				obj.setLocalBalance(rs.getDouble("LocalBalance"));				
				lstCustomers.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getCustomersContractExpiryList-->", ex);
		}
		return lstCustomers;
	}

	public List<Integer> getSalesRepNamesForReminder(String status)
	{
		List<Integer> lst=new ArrayList<Integer>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSalesRepNamesForReminder(status));
			while(rs.next())
			{						
				lst.add(rs.getInt("recNo"));
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getSalesRepNamesForReminder-->" , ex);
		}
		return lst;
	}
	public List<CustomerModel> getCustomersQuoataionList(List<Integer> lstCustKey) 
	{
		List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try
		{
			String custKeys="";
			for (Integer custId : lstCustKey)
			{
				if(custKeys.length()>0)
					custKeys+=",";
				custKeys+=custId;		
			}
			//i want to get all customer has quotation with status=c
			custKeys="";
			
			rs=db.executeNonQuery(query.getCustomerForQuotationReminder(custKeys));
			while (rs.next()) 
			{
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
				obj.setMobile(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
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
				if (rs.getDate("cusContractExpiry") != null) {
					obj.setCustomerContactExpiryDateStr(new SimpleDateFormat(
							"dd-MM-yyyy").format(rs
									.getDate("cusContractExpiry")) == null ? ""
											: new SimpleDateFormat("dd-MM-yyyy").format(rs
													.getDate("cusContractExpiry")));
				} else {
					obj.setCustomerContactExpiryDateStr("");
				}
				obj.setLocalBalance(rs.getDouble("LocalBalance"));				
				lstCustomers.add(obj);
			}
			
			
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getCustomersQuoataionList-->" , ex);
		}
		
		return lstCustomers;
		
	}
	
	public int updateBarcodeSettings(BarcodeSettingsModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.UpdateBarcodeSettingsQuery(obj));
			result = 1;
		} catch (Exception ex) {
			logger.error("error in HBAData---updateBarcodeSettings-->", ex);
		}
		return result;

	}

	public CustomerModel getCustomerByKey(int custKey) {
		CustomerModel obj = new CustomerModel();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomersByKeyQuery(custKey));
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
		}

		catch (Exception ex) {
			logger.error("error in HBAData---getCustomerByKey-->", ex);
		}
		return obj;
	}

	public List<CustomerStatusHistoryModel> getCustomerStatusById(int custKey) {
		List<CustomerStatusHistoryModel> lst = new ArrayList<CustomerStatusHistoryModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomerStatusById(custKey));
			while (rs.next()) {
				CustomerStatusHistoryModel obj = new CustomerStatusHistoryModel();
				obj.setRecNo(rs.getInt("recno"));
				obj.setCustKey(rs.getInt("custKey"));

				obj.setActionDate(rs.getDate("actionDate"));
				obj.setActionDatstr(new SimpleDateFormat("dd-MM-yyyy h:mm a")
				.format(rs.getDate("actionDate")));
				obj.setStatusDescription(rs.getString("statusdescription") == null ? ""
						: rs.getString("statusdescription"));
				obj.setType(rs.getString("type") == null ? "" : rs
						.getString("type"));
				if (obj.getType() != null
						&& obj.getType().equalsIgnoreCase("C")) {
					obj.setType("Customer");
					// obj.setCustomerName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				} else if (obj.getType() != null
						&& obj.getType().equalsIgnoreCase("P")) {
					obj.setType("Prospective");
					// obj.setCustomerName(rs.getString("fullNameProspecive")==null?"":rs.getString("fullNameProspecive"));
				}
				obj.setCreatedFrom(rs.getString("createdFrom") == null ? ""
						: rs.getString("createdFrom"));
				obj.setTxnRefNumber(rs.getString("txnRefNo") == null ? "" : rs
						.getString("txnRefNo"));
				lst.add(obj);
			}
		}

		catch (Exception ex) {
			logger.error("error in HBAData---getCustomerStatusById-->", ex);
		}
		return lst;
	}

	public int UpdateCustomers(int Cust_Key, String companyName, String name,
			String arName) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.UpdateCustomersQuery(Cust_Key,
					companyName, name, arName));
		} catch (Exception ex) {
			logger.error("error in HBAData---UpdateCustomers-->", ex);
		}
		return result;

	}

	public int UpdateCustomerData(CustomerModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.UpdateCustomerQuery(obj));
			result = 1;
		} catch (Exception ex) {
			logger.error("error in HBAData---UpdateCustomerData-->", ex);
		}
		return result;

	}

	public int addCustomerData(CustomerModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			int newID = getMaxID("Customer", "cust_key");
			obj.setCustkey(newID);
			result = db.executeUpdateQuery(query.addCustomerQuery(obj));
			result = 1;
		} catch (Exception ex) {
			logger.error("error in HBAData---addCustomerData-->", ex);
		}
		return result;

	}

	public int getMaxID(String tableName, String fieldName) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while (rs.next()) {
				result = rs.getInt(1) + 1;
			}
			if (result == 0)
				result = 1;

		} catch (Exception ex) {
			logger.error("error in HBAData---getMaxID-->", ex);
		}
		return result;
	}

	public List<VendorModel> getVendorList() {
		List<VendorModel> lst = new ArrayList<VendorModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getVendorsQuery());
			while (rs.next()) {
				VendorModel obj = new VendorModel();
				obj.setVend_Key(rs.getInt("Vend_Key"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setArName(rs.getString("ArName") == null ? "" : rs
						.getString("ArName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				// obj.setTimeCreated(df.parse(sdf.format(rs.getDate("TimeCreated"))));
				obj.setIsActive(rs.getString("IsActive") == null ? "" : rs
						.getString("IsActive"));
				obj.setBillAddress1(rs.getString("BillAddress1") == null ? ""
						: rs.getString("BillAddress1"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setContact(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setWebSite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? ""
						: rs.getString("PrintChequeAs"));
				obj.setNote(rs.getString("Note") == null ? "" : rs
						.getString("Note"));
				lst.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getVendorList-->", ex);
		}
		return lst;
	}

	public VendorModel getVendorByKey(int vendKey) {
		VendorModel obj = new VendorModel();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getVendorByKeyQuery(vendKey));
			while (rs.next()) {
				obj.setVend_Key(rs.getInt("Vend_Key"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setArName(rs.getString("ArName") == null ? "" : rs
						.getString("ArName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				// obj.setTimeCreated(df.parse(sdf.format(rs.getDate("TimeCreated"))));
				obj.setIsActive(rs.getString("IsActive") == null ? "" : rs
						.getString("IsActive"));
				obj.setBillAddress1(rs.getString("BillAddress1") == null ? ""
						: rs.getString("BillAddress1"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setContact(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setWebSite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? ""
						: rs.getString("PrintChequeAs"));
				obj.setNote(rs.getString("Note") == null ? "" : rs
						.getString("Note"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getVendorByKey-->", ex);
		}
		return obj;
	}

	public int UpdateVendorData(VendorModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.UpdateVendorQuery(obj));
			result = 1;
		} catch (Exception ex) {
			logger.error("error in HBAData---UpdateVendorData-->", ex);
		}
		return result;

	}

	public int addVendorData(VendorModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			int newID = getMaxID("Vendor", "vend_key");
			obj.setVend_Key(newID);
			result = db.executeUpdateQuery(query.addVendorQuery(obj));
			result = 1;
		} catch (Exception ex) {
			logger.error("error in HBAData---addVendorData-->", ex);
		}
		return result;

	}

	// Cheque Payment
	public CompSetupModel GetDefaultSetupInfo() {
		CompSetupModel obj = new CompSetupModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetDefaultSetupInfoQuery());
			while (rs.next()) {
				if (rs.getString("PVSERIALNOS") == null
						|| rs.getString("PVSERIALNOS").equals(""))
					obj.setPvSerialNos("S");
				else
					obj.setPvSerialNos(rs.getString("PVSERIALNOS"));
				if (rs.getString("Post2MainAccount") == null
						|| rs.getString("Post2MainAccount").equals(""))
					obj.setPostOnMainAccount("Y");
				else
					obj.setPostOnMainAccount(rs.getString("Post2MainAccount"));

				if (rs.getString("PostOnMainClass") == null
						|| rs.getString("PostOnMainClass").equals(""))
					obj.setPostOnMainClass("Y");
				else
					obj.setPostOnMainClass(rs.getString("PostOnMainClass"));

				if (rs.getString("BuyItemWithHighCost") == null
						|| rs.getString("BuyItemWithHighCost").equals(""))
					obj.setBuyItemWithHighCost("Y");
				else
					obj.setBuyItemWithHighCost(rs
							.getString("BuyItemWithHighCost"));
				obj.setPostItem2Main(rs.getString("postItem2Main") == null ? ""
						: rs.getString("postItem2Main"));

				obj.setCompanyName(rs.getString("companyName"));
				obj.setAllowToAddInventorySite(rs.getString("AllowToAddInvSite"));
				obj.setCountrykey(rs.getInt("countrykey"));
				obj.setCitykey(rs.getInt("citykey"));
				obj.setPhone1(rs.getString("telephone"));
				obj.setFax(rs.getString("Fax"));
				obj.setCcemail(rs.getString("ccemail"));

				obj.setUseMinPurchasePrice(rs.getString("UseMinPurchasePrice"));
				obj.setUseMaxPurchasePrice(rs.getString("UseMaxPurchasePrice"));
				obj.setMinPurchasePriceRatio(rs
						.getDouble("MinPurchasePriceRatio"));
				obj.setMaxPurchasePriceRatio(rs
						.getDouble("MaxPurchasePriceRatio"));
				obj.setChangePredefinedClass(rs
						.getString("ChangeClass_Account"));
				obj.setrVSerialSetup(rs.getString("RVSERIALNOS"));
				obj.setAddress(rs.getString("address"));
				obj.setUsebillable(rs.getString("usebillable") == null ? ""
						: rs.getString("usebillable"));
				obj.setPostJVWithOutName(rs.getString("PostJVWithOutName") == null ? ""
						: rs.getString("PostJVWithOutName"));
				obj.setUsePurchaseFlow(rs.getString("usePurchaseFlow") == null ? ""
						: rs.getString("usePurchaseFlow"));
				obj.setChangePrice_ConvertPO(rs.getString("ChangePrice_ConvertPO") == null ? "": rs.getString("ChangePrice_ConvertPO"));				
				obj.setAllowToSkipPurchaseWorkFlow(rs.getString("AllowToSkipPurchaseWorkFlow") == null ? "": rs.getString("AllowToSkipPurchaseWorkFlow"));
				

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetDefaultSetupInfo-->", ex);
		}
		return obj;
	}

	public List<AccountsModel> fillBankAccounts(String accountType) {
		List<AccountsModel> lst = new ArrayList<AccountsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.fillBankAccountsQuery(accountType));
			while (rs.next()) {
				AccountsModel obj = new AccountsModel();
				obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillBankAccounts-->", ex);
		}
		return lst;
	}

	public List<AccountsModel> fillAccountsQueryNotIn(String accountType) {
		List<AccountsModel> lst = new ArrayList<AccountsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.fillAccountsQueryNotIn(accountType));
			while (rs.next()) {
				AccountsModel obj = new AccountsModel();
				obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillBankAccounts-->", ex);
		}
		return lst;
	}

	public boolean checkIfBankAccountsHasSub(String accountName) {
		boolean hasSubAccount = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.checkIfBankAccountsHasSubQuery(accountName));
			while (rs.next()) {
				hasSubAccount = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkIfBankAccountsHasSub-->", ex);
		}

		return hasSubAccount;
	}

	public boolean checkIfClassHasSub(String className) {
		boolean hasSubAccount = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIfClassHasSubQuery(className));
			while (rs.next()) {
				hasSubAccount = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkIfClassHasSub-->", ex);
		}

		return hasSubAccount;
	}

	public List<BanksModel> fillBanksList() {
		List<BanksModel> lst = new ArrayList<BanksModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getBanksListQuery());
			while (rs.next()) {
				BanksModel obj = new BanksModel();
				obj.setRecno(rs.getInt("RecNo"));
				obj.setBankName(rs.getString("name"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillBanksList-->", ex);
		}
		return lst;
	}

	public List<QbListsModel> fillQbList(String ListType) {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		QbListsModel obj1 = new QbListsModel();
		obj1.setRecNo(0);
		obj1.setName("Select");
		obj1.setFullName("Select");
		obj1.setSubLevel(0);
		obj1.setIsActive("None");
		obj1.setListType("None");
		lst.add(obj1);

		try {
			rs = db.executeNonQuery(query.getQbListQuery(ListType));
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ListType"));
				if (rs.getString("isactive").equalsIgnoreCase("Y")) {
					obj.setIsActive("Active");
				} else {
					obj.setIsActive("INActive");
				}

				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillQbList-->", ex);
		}
		return lst;
	}

	public List<ClassModel> fillClassList(String classType) {
		List<ClassModel> lst = new ArrayList<ClassModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getClassQuery(classType));
			while (rs.next()) {
				ClassModel obj = new ClassModel();
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("sublevel"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillClassList-->", ex);
		}
		return lst;
	}

	public List<ClassModel> fillFlatList(String classType, String name) {
		List<ClassModel> lst = new ArrayList<ClassModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getFlatQuery(classType, name));
			while (rs.next()) {
				ClassModel obj = new ClassModel();
				obj.setClass_Key(rs.getInt("CLASS_KEY"));
				obj.setName(rs.getString("Name"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillFlatList-->", ex);
		}
		return lst;
	}

	public List<FixedAssetModel> getFixedAssetItems() {
		List<FixedAssetModel> lst = new ArrayList<FixedAssetModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getFixedAssetItemQuery());
			while (rs.next()) {
				FixedAssetModel obj = new FixedAssetModel();
				obj.setAssetid(rs.getInt("AssetID"));
				obj.setAssetCode(rs.getString("ASsetCode"));
				obj.setAssetName(rs.getString("ASSETNAMECode"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getFixedAssetItems-->", ex);
		}

		return lst;
	}

	public String GetSerialNumber(String serialField) {
		String LastNumber = "1";

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetSerialNumberQuery(serialField));
			while (rs.next()) {
				LastNumber = rs.getString("LastNumber") == null ? ""
						: rs.getString("LastNumber");
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetSerialNumber-->", ex);
		}
		return LastNumber;
	}

	public PayToOrderModel getPayToOrderInfo(String ListType, int keyID) {
		PayToOrderModel obj = new PayToOrderModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPayToOrderInfoQuery(ListType,
					keyID));
			while (rs.next()) {
				obj.setName(rs.getString("Name"));
				obj.setBillAddress1(rs.getString("BillAddress1") == null ? ""
						: rs.getString("BillAddress1"));
				obj.setBillAddress2(rs.getString("BillAddress2") == null ? ""
						: rs.getString("BillAddress2"));
				if (ListType.equals("Customer")) {
					obj.setBillAddress3(rs.getString("BillAddress3") == null ? ""
							: rs.getString("BillAddress3"));
					obj.setBillAddress4(rs.getString("BillAddress4") == null ? ""
							: rs.getString("BillAddress4"));
				} else {
					obj.setBillAddress3("");
					obj.setBillAddress4("");
				}
				obj.setPhone(rs.getString("Phone") == null ? "" : rs
						.getString("Phone"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? ""
						: rs.getString("PrintChequeAs"));

				if (ListType.equals("Customer")) {
					obj.setAccountName(rs.getString("ActName") == null ? ""
							: rs.getString("ActName"));
					obj.setAccountNo(rs.getString("AccountNo") == null ? ""
							: rs.getString("AccountNo"));
					obj.setBankName(rs.getString("BankName") == null ? "" : rs
							.getString("BankName"));
					obj.setBranchName(rs.getString("BranchName") == null ? ""
							: rs.getString("BranchName"));
					obj.setIBANNo(rs.getString("IBANNo") == null ? "" : rs
							.getString("IBANNo"));
				}
				if (ListType.equals("Employee")) {
					obj.setAccountName(rs.getString("ActName") == null ? ""
							: rs.getString("ActName"));
					obj.setAccountNo(rs.getString("Account_No") == null ? ""
							: rs.getString("Account_No"));
					obj.setBankName(rs.getString("BANK_ID") == null ? "" : rs
							.getString("BANK_ID"));
					obj.setBranchName(rs.getString("BRANCH_ID") == null ? ""
							: rs.getString("BRANCH_ID"));
					obj.setIBANNo(rs.getString("IBANNo") == null ? "" : rs
							.getString("IBANNo"));
				}
				if (ListType.equals("Vendor")) {
					obj.setFullName(rs.getString("fullName") == null ? "" : rs
							.getString("fullName"));
					obj.setAccountName(rs.getString("ActName") == null ? ""
							: rs.getString("ActName"));
					obj.setAccountNo(rs.getString("ActNumber") == null ? ""
							: rs.getString("ActNumber"));
					obj.setBankName(rs.getString("BankName") == null ? "" : rs
							.getString("BankName"));
					obj.setBranchName(rs.getString("Branch") == null ? "" : rs
							.getString("Branch"));
					obj.setIBANNo(rs.getString("IBANNo") == null ? "" : rs
							.getString("IBANNo"));
				}
				if (ListType.equals("OtherNames")) {
					obj.setAccountName(rs.getString("ActName") == null ? ""
							: rs.getString("ActName"));
					obj.setAccountNo(rs.getString("AccountNo") == null ? ""
							: rs.getString("AccountNo"));
					obj.setBankName(rs.getString("BankName") == null ? "" : rs
							.getString("BankName"));
					obj.setBranchName(rs.getString("BranchName") == null ? ""
							: rs.getString("BranchName"));
					obj.setIBANNo(rs.getString("IBANNo") == null ? "" : rs
							.getString("IBANNo"));
				}
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getPayToOrderInfo-->", ex);
		}

		return obj;
	}

	// Save New Cheque payment
	public int GetNewCheckMastRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewCheckMastRecNoQuery());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewCheckMastRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public boolean FindTxnNumber(SerialFields field, String TxnNumber,String condition,long recNo) {
		boolean falg=false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewTxnNumberQuery(field,
					TxnNumber, condition,recNo));
			while (rs.next()) {
				falg=true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---FindTxnNumber-->", ex);
		}
		return falg;
	}

	/*public String ConfigSerialNumber(SerialFields field, String SerialNumber, int keyID) {
		String tmpConfigSerailNum = "";
		SerialNumber = SerialNumber + 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			String tmpField = "";
			if (keyID == 0)
				tmpField = field.toString();
			else
				tmpField = field.toString() + "-" + String.valueOf(keyID);

			// check if exist serial number
			boolean isSerailFound = false;
			rs = db.executeNonQuery(query.GetSerialNumberQuery(tmpField));
			while (rs.next()) {
				isSerailFound = true;
			}
			if (isSerailFound)
				db.executeUpdateQuery(query.updateSystemSerialNosQuery(
						String.valueOf(SerialNumber), tmpField));
			else
				db.executeUpdateQuery(query.insertSystemSerialNosQuery(
						String.valueOf(SerialNumber), tmpField));

			tmpConfigSerailNum = String.valueOf(SerialNumber);
		} catch (Exception ex) {
			logger.error("error in HBAData---ConfigSerialNumber-->", ex);
		}

		return tmpConfigSerailNum;
	}*/

	public int addNewChequePayment(CashModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewChequePaymentQuery(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewChequePayment-->", ex);
		}
		return result;

	}

	// ------------------------------------------------------------------------------------Cash
	// payment-------------------------------------------------------------------------------------------------
	public int addNewCashPayment(CashModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewCashPaymentQuery(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewCashPayment-->", ex);
		}
		return result;
	}

	public int updateNewCashPayment(CashModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateNewCashPayment(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateNewCashPayment-->", ex);
		}
		return result;
	}

	public int updateNewChequePayment(CashModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateNewChequePayment(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateNewCashPayment-->", ex);
		}
		return result;
	}

	public List<CashModel> getCashPaymentReport(Date fromDate, Date toDate,int webUserID)
	{
		List<CashModel> lst=new ArrayList<CashModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try
		{
			rs = db.executeNonQuery(query.getCashPaymentReportQuery(fromDate, toDate, webUserID));
			while (rs.next()) 
			{
				CashModel obj = new CashModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setTxnID(rs.getString("TxnID") == null ? "" : rs
						.getString("TxnID"));
				obj.setPvDate(rs.getDate("PVDate"));
				obj.setPvDateStr(rs.getString("PVDate") == null ? "" : rs
						.getString("PVDate"));
				obj.setPvNo(rs.getString("PVNo") == null ? ""
						: rs.getString("PVNo"));
				obj.setPayeeType(rs.getString("PayeeType") == null ? "" : rs
						.getString("PayeeType"));
				obj.setPrintName(rs.getString("PrintName") == null ? "" : rs
						.getString("PrintName"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs
						.getString("Status"));
				obj.setAmount(rs.getDouble("Amount"));
				
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getCashPaymentReport-->", ex);
		}
		return lst;
	}
	public CashModel getCashPaymentById(int cashPaymentKey, int webUserID,boolean seeTrasction) {
		CashModel obj = new CashModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCashPaymentById(cashPaymentKey,
					webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setTxnID(rs.getString("TxnID") == null ? "" : rs
						.getString("TxnID"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setCreatedDateStr(rs.getString("TimeCreated") == null ? ""
						: rs.getString("TimeCreated"));
				obj.setModifiedDate(rs.getDate("TimeModified"));
				obj.setModifiedDateStr(rs.getString("TimeModified") == null ? ""
						: rs.getString("TimeModified"));
				obj.setEditSequence(rs.getString("EditSequence") == null ? ""
						: rs.getString("EditSequence"));
				obj.setCheckNo(rs.getString("CheckNo") == null ? "" : rs
						.getString("CheckNo"));
				obj.setCheckDate(rs.getDate("CheckDate"));
				obj.setCheckDateStr(rs.getString("CheckDate") == null ? "" : rs
						.getString("CheckDate"));
				obj.setPvNo(rs.getString("PVNo") == null ? ""
						: rs.getString("PVNo"));
				obj.setPvDate(rs.getDate("PVDate"));
				obj.setPvDateStr(rs.getString("PVDate") == null ? "" : rs
						.getString("PVDate"));
				obj.setBankKey(rs.getInt("BankKey"));
				obj.setPayeeKey(rs.getInt("PayeeKey"));
				obj.setPayeeType(rs.getString("PayeeType") == null ? "" : rs
						.getString("PayeeType"));
				obj.setRegistartionId(rs.getInt("RegistrationID"));
				obj.setPrintName(rs.getString("PrintName") == null ? "" : rs
						.getString("PrintName"));
				obj.setQBRefDate(rs.getString("QBRefDate") == null ? "" : rs
						.getString("QBRefDate"));
				obj.setQBRefNo(rs.getString("QBRefNo") == null ? "" : rs
						.getString("QBRefNo"));
				obj.setMemo(rs.getString("Memo") == null ? "" : rs
						.getString("Memo"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs
						.getString("Status"));
				obj.setBankRefKey(rs.getInt("BankRefKey"));
				obj.setCheque(rs.getString("Cheque") == null ? "" : rs
						.getString("Cheque"));
				obj.setStatusMemo(rs.getString("StatusMemo") == null ? "" : rs
						.getString("StatusMemo"));
				obj.setRecosileDate(rs.getDate("ReconcileDate"));
				obj.setReconsileDateStr(rs.getString("ReconcileDate") == null ? ""
						: rs.getString("ReconcileDate"));
				obj.setQBStatus(rs.getString("QBStatus") == null ? "" : rs
						.getString("QBStatus"));
				obj.setExpClassHide(rs.getString("ExpClassHide") == null ? ""
						: rs.getString("ExpClassHide"));
				obj.setExpMemoHide(rs.getString("ExpMemoHide") == null ? ""
						: rs.getString("ExpMemoHide"));
				obj.setExpBillNoHide(rs.getString("ExpBillNoHide") == null ? ""
						: rs.getString("ExpBillNoHide"));
				obj.setExpBillDateHide(rs.getString("ExpBillDateHide") == null ? ""
						: rs.getString("ExpBillDateHide"));
				obj.setItemClassHide(rs.getString("ItemClassHide") == null ? ""
						: rs.getString("ItemClassHide"));
				obj.setItemDesHide(rs.getString("ItemDesHide") == null ? ""
						: rs.getString("ItemDesHide"));
				obj.setItemBillDateHide(rs.getString("ItemBillDateHide") == null ? ""
						: rs.getString("ItemBillDateHide"));
				obj.setItemBillNoHide(rs.getString("ItemBillNoHide") == null ? ""
						: rs.getString("ItemBillNoHide"));
				obj.setPvCheck_print(rs.getString("PVCheck_Printed") == null ? ""
						: rs.getString("PVCheck_Printed"));
				obj.setAccForPdc(rs.getInt("AcctForPdc"));
				obj.setApAccountRefKey(rs.getInt("APAccountRefKey"));
				obj.setUnitKey(rs.getInt("UnitKey"));
				obj.setContractRecNo(rs.getInt("ContractRecNo"));
				obj.setPrintLetter(rs.getString("PrintLetter") == null ? ""
						: rs.getString("PrintLetter"));
				obj.setSwiftCode(rs.getString("SwiftCode") == null ? "" : rs
						.getString("SwiftCode"));
				obj.setUserID(rs.getInt("UserID"));
				obj.setRemoveFromPdc(rs.getString("RemoveFromPDC") == null ? ""
						: rs.getString("RemoveFromPDC"));
				obj.setNewStatus(rs.getString("NewStatus") == null ? "" : rs
						.getString("NewStatus"));
				obj.setAdvancePayment(rs.getString("AdvancePayment") == null ? ""
						: rs.getString("AdvancePayment"));
				obj.setWebUserID(rs.getInt("webUserID"));
				obj.setEditedFromonline(rs.getString("editedFromOnline") == null ? ""
						: rs.getString("editedFromOnline"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCashPaymentById-->", ex);
		}
		return obj;
	}

	public CashModel navigationCashPayment(int cashPaymentId, int webUserID,boolean seeTrasction,
			String navigation, String actionTYpe,String type) {
		CashModel obj = new CashModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")	&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) 
			{
				rs = db.executeNonQuery(query.getPreviousRecordPayment(cashPaymentId, webUserID,seeTrasction,type));
			} 
			else if (navigation.equalsIgnoreCase("next") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) 
			{
				rs = db.executeNonQuery(query.getNextRecordCashPayment(cashPaymentId, webUserID,seeTrasction,type));
			} 
			else if (navigation.equalsIgnoreCase("next")&& actionTYpe.equalsIgnoreCase("create")) 
			{
				rs = db.executeNonQuery(query.getFirstRecordCashPayment(webUserID,seeTrasction,type));
			} 
			else if (navigation.equalsIgnoreCase("prev") && actionTYpe.equalsIgnoreCase("create")) 
			{
				rs = db.executeNonQuery(query.getLastRecordCashPayment(webUserID,seeTrasction,type));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setTxnID(rs.getString("TxnID") == null ? "" : rs.getString("TxnID"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setCreatedDateStr(rs.getString("TimeCreated") == null ? "": rs.getString("TimeCreated"));
				obj.setModifiedDate(rs.getDate("TimeModified"));
				obj.setModifiedDateStr(rs.getString("TimeModified") == null ? "": rs.getString("TimeModified"));
				obj.setEditSequence(rs.getString("EditSequence") == null ? "": rs.getString("EditSequence"));
				obj.setCheckNo(rs.getString("CheckNo") == null ? "" : rs.getString("CheckNo"));
				obj.setCheckDate(rs.getDate("CheckDate"));
				obj.setCheckDateStr(rs.getString("CheckDate") == null ? "" : rs.getString("CheckDate"));
				obj.setPvNo(rs.getString("PVNo") == null ? ""
						: rs.getString("PVNo"));
				obj.setPvDate(rs.getDate("PVDate"));
				obj.setPvDateStr(rs.getString("PVDate") == null ? "" : rs.getString("PVDate"));
				obj.setBankKey(rs.getInt("BankKey"));
				obj.setPayeeKey(rs.getInt("PayeeKey"));
				obj.setPayeeType(rs.getString("PayeeType") == null ? "" : rs.getString("PayeeType"));
				obj.setRegistartionId(rs.getInt("RegistrationID"));
				obj.setPrintName(rs.getString("PrintName") == null ? "" : rs.getString("PrintName"));
				obj.setQBRefDate(rs.getString("QBRefDate") == null ? "" : rs.getString("QBRefDate"));
				obj.setQBRefNo(rs.getString("QBRefNo") == null ? "" : rs.getString("QBRefNo"));
				obj.setMemo(rs.getString("Memo") == null ? "" : rs.getString("Memo"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs.getString("Status"));
				obj.setBankRefKey(rs.getInt("BankRefKey"));
				obj.setCheque(rs.getString("Cheque") == null ? "" : rs.getString("Cheque"));
				obj.setStatusMemo(rs.getString("StatusMemo") == null ? "" : rs.getString("StatusMemo"));
				obj.setRecosileDate(rs.getDate("ReconcileDate"));
				obj.setReconsileDateStr(rs.getString("ReconcileDate") == null ? "": rs.getString("ReconcileDate"));
				obj.setQBStatus(rs.getString("QBStatus") == null ? "" : rs.getString("QBStatus"));
				obj.setExpClassHide(rs.getString("ExpClassHide") == null ? "": rs.getString("ExpClassHide"));
				obj.setExpMemoHide(rs.getString("ExpMemoHide") == null ? "": rs.getString("ExpMemoHide"));
				obj.setExpBillNoHide(rs.getString("ExpBillNoHide") == null ? "": rs.getString("ExpBillNoHide"));
				obj.setExpBillDateHide(rs.getString("ExpBillDateHide") == null ? "": rs.getString("ExpBillDateHide"));
				obj.setItemClassHide(rs.getString("ItemClassHide") == null ? "": rs.getString("ItemClassHide"));
				obj.setItemDesHide(rs.getString("ItemDesHide") == null ? "": rs.getString("ItemDesHide"));
				obj.setItemBillDateHide(rs.getString("ItemBillDateHide") == null ? "": rs.getString("ItemBillDateHide"));
				obj.setItemBillNoHide(rs.getString("ItemBillNoHide") == null ? "": rs.getString("ItemBillNoHide"));
				obj.setPVCheck_Printed(rs.getString("PVCheck_Printed") == null ? "": rs.getString("PVCheck_Printed"));
				obj.setAccForPdc(rs.getInt("AcctForPdc"));
				obj.setApAccountRefKey(rs.getInt("APAccountRefKey"));
				obj.setUnitKey(rs.getInt("UnitKey"));
				obj.setContractRecNo(rs.getInt("ContractRecNo"));
				obj.setPrintLetter(rs.getString("PrintLetter") == null ? "": rs.getString("PrintLetter"));
				obj.setSwiftCode(rs.getString("SwiftCode") == null ? "" : rs.getString("SwiftCode"));
				obj.setUserID(rs.getInt("UserID"));
				obj.setRemoveFromPdc(rs.getString("RemoveFromPDC") == null ? "": rs.getString("RemoveFromPDC"));
				obj.setNewStatus(rs.getString("NewStatus") == null ? "" : rs.getString("NewStatus"));
				obj.setAdvancePayment(rs.getString("AdvancePayment") == null ? "": rs.getString("AdvancePayment"));
				obj.setWebUserID(rs.getInt("webUserID"));
				obj.setEditedFromonline(rs.getString("editedFromOnline") == null ? "": rs.getString("editedFromOnline"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationCashPayment-->", ex);
		}
		return obj;
	}

	public List<ExpensesModel> getCashPaymentGridDataExpenseById(int billKey) {
		List<ExpensesModel> lst = new ArrayList<ExpensesModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getCashPaymentGridDataExpenseById(billKey));
			while (rs.next()) {
				ExpensesModel obj = new ExpensesModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setSrNO(rs.getInt("lineNo"));
				obj.setTxnLineId(rs.getString("txnlineId"));
				obj.setSelectedAccountKey(rs.getInt("accountKey"));
				obj.setSelectedCutomerKey(rs.getInt("custkey"));
				obj.setSelectedClassKey(rs.getInt("classkey"));
				obj.setFixedAssetItemid(rs.getInt("FaItemKey"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setBillable(rs.getString("billable") == null ? "" : rs
						.getString("billable"));
				obj.setBillDate(rs.getDate("BillDate"));
				obj.setBillNo((rs.getString("BillNO") == null ? "" : rs
						.getString("BillNO")));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---getCashPaymentGridDataExpenseById-->",
					ex);
		}
		return lst;
	}

	public List<CheckItemsModel> getCashPaymentGridDataItemById(int billKey) {
		List<CheckItemsModel> lst = new ArrayList<CheckItemsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getCashPaymentGridDataItemById(billKey));
			while (rs.next()) {
				CheckItemsModel obj = new CheckItemsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("LineNo"));
				obj.setItemKey(rs.getInt("ItemKey"));
				obj.setDescription(rs.getString("Description"));
				obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setCost(rs.getDouble("Cost"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setSelectedCustKey(rs.getInt("CustKey"));
				obj.setSelectedClassKey(rs.getInt("ClassKey"));
				obj.setInventorySiteKey(rs.getInt("InventorySiteKey"));
				obj.setFixedIteKey(rs.getInt("FAItemKey"));
				obj.setBillable(rs.getString("billable") == null ? "" : rs.getString("billable"));
				obj.setInvoiceDate(rs.getDate("InvoiceDate"));
				obj.setBillNo((rs.getString("BillNO") == null ? "" : rs
						.getString("BillNO")));
				lst.add(obj);


			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---getCashPaymentGridDataItemById-->", ex);
		}
		return lst;
	}

	public List<CheckFAItemsModel> getCashPaymentGridDataFAById(int billKey) {
		List<CheckFAItemsModel> lst = new ArrayList<CheckFAItemsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCashPaymentGridDataFAById(billKey));
			while (rs.next()) {
				CheckFAItemsModel obj = new CheckFAItemsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setFaItemKey(rs.getInt("FaItemKey"));
				obj.setDescription(rs.getString("Description"));
				obj.setBillNo(rs.getString("billNo"));
				obj.setInvoiceDate(rs.getDate("invoiceDate"));
				obj.setQuantity((int) rs.getDouble("quantity"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setUnitPrice(rs.getDouble("unitPrice"));
				obj.setOtherCharges(rs.getDouble("otherCharges"));
				obj.setCustomerKey(rs.getInt("custKey"));
				obj.setCustodyKey(rs.getInt("custodyKey"));
				obj.setInvoiceDate(rs.getDate("InvoiceDate"));
				obj.setBillNo((rs.getString("BillNO") == null ? "" : rs
						.getString("BillNO")));

				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCashPaymentGridDataFAById-->",
					ex);
		}
		return lst;
	}

	// ------------------------------------------------------------------------------------Bank
	// Transfer-------------------------------------------------------------------------------------------------

	public int addNewBankTransfer(BankTransferModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewBankTransferQuery(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewBankTransfer-->", ex);
		}
		return result;
	}

	public int addNewBankTransferInfo(BankTransferModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.addNewBankTransferInfoQuery(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewBankTransferInfo-->", ex);
		}
		return result;
	}

	// Expenses
	public int deleteExpense(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.deleteExpenseQuery(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteExpense-->", ex);
		}
		return result;

	}

	public int addExpense(ExpensesModel objExpenses, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewExpenseQuery(
					objExpenses, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addExpense-->", ex);
		}
		return result;

	}

	public int updateAccountsTotalBalance(int RecNo, double amount) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateAccountsTotalBalanceQuery(RecNo, amount));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateAccountsTotalBalance-->", ex);
		}
		return result;

	}

	// Check Items
	public List<QbListsModel> fillQbItemsList() {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			QbListsModel obj = new QbListsModel();
			rs = db.executeNonQuery(query.GetQBItemsQuery());
			while (rs.next()) {
				obj = new QbListsModel();
				obj.setRecNo(rs.getInt("item_key"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ItemType"));
				obj.setBarcode(rs.getString("BARCODE"));
				obj.setSalesDesc(rs.getString("salesdesc") == null ? "" : rs.getString("salesdesc"));
				
				obj.setInvoiceDescription(rs.getString("SalesDesc")== null ? "" : rs.getString("salesdesc"));
				obj.setInvoicearabicDescription(rs.getString("DescriptionAR"));
				Double qtyOnhand = rs.getDouble("QuantityOnHand");
				obj.setInvoiceQtyOnHand(qtyOnhand.intValue());
				obj.setInvoiceRate(rs.getDouble("SalesPrice"));
				obj.setAvgCost(rs.getDouble("averagecost"));
				obj.setSelectedClass(rs.getInt("ClassKey"));
				
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillQbItemsList-->", ex);
		}
		return lst;
	}

	public QbListsModel getQbItemsData(int Item_Key) {
		QbListsModel obj = new QbListsModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetQBItemsDataQuery(Item_Key));
			while (rs.next()) {
				obj.setRecNo(Item_Key);
				obj.setPurchaseDesc(rs.getString("PurchaseDesc"));
				obj.setPurchaseCost(rs.getDouble("averageCost"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setRecNo(rs.getInt("Item_Key"));
				obj.setSubOfClasskey(rs.getInt("ClassKey"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQbItemsData-->", ex);
		}
		return obj;
	}

	public int deleteCheckItems(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.deleteCheckItemsQuery(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteCheckItems-->", ex);
		}
		return result;

	}

	public int addCheckItems(CheckItemsModel obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewCheckItemsQuery(obj,
					RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCheckItems-->", ex);
		}
		return result;

	}

	// Fixed Asset Items
	public List<FixedAssetModel> getVendorFixedAssetItem(int vendorID) {
		List<FixedAssetModel> lst = new ArrayList<FixedAssetModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			FixedAssetModel obj = new FixedAssetModel();
			obj.setAssetid(0);
			lst.add(obj);

			rs = db.executeNonQuery(query
					.getVendorFixedAssetItemQuery(vendorID));
			while (rs.next()) {
				obj = new FixedAssetModel();
				obj.setAssetid(rs.getInt("AssetID"));
				obj.setAssetCode(rs.getString("ASsetCode"));
				obj.setAssetName(rs.getString("AssetName"));
				obj.setAssetMasterDesc(rs.getString("ASSETNAMECode"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getVendorFixedAssetItem-->", ex);
		}

		return lst;
	}

	public FixedAssetModel getFixedAssetItemData(int AssetID) {
		FixedAssetModel obj = new FixedAssetModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getFixedAssetItemDataQuery(AssetID));
			while (rs.next()) {
				obj.setDescription(rs.getString("Description"));
				obj.setCustomerName(rs.getString("CustName"));
				obj.setLocationID(rs.getInt("LocationID"));
				obj.setEmployeeName(rs.getString("EMPName"));
				obj.setEmployeeID(rs.getInt("EMployeeID"));
				obj.setServiceDate(rs.getDate("ServiceDate"));
				obj.setLifeExpiryDate(rs.getDate("LifeExpiryDate"));
				obj.setPrice(rs.getDouble("Price"));
				obj.setOpeningBalance(rs.getDouble("OpeningBalance"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getFixedAssetItemData-->", ex);
		}
		return obj;
	}

	public int deleteCheckFAItems(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db
					.executeUpdateQuery(query.deleteCheckFAItemsQuery(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteCheckFAItems-->", ex);
		}
		return result;
	}

	public int addCheckFAItems(CheckFAItemsModel obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewCheckFAItemsQuery(obj,
					RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCheckFAItems-->", ex);
		}
		return result;

	}

	public int updateAssetMaster(CheckFAItemsModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateAssetMasterQuery(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateAssetMaster-->", ex);
		}
		return result;
	}

	public int DeleteDepreciation(int AssetID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.DeleteDepreciationQuery(AssetID));
		} catch (Exception ex) {
			logger.error("error in HBAData---DeleteDepreciation-->", ex);
		}
		return result;
	}

	public int Y_M_D_Diff(Date DateOne, Date DateTwo) {
		int totalmonth = 0;
		try {

			// ' Make sure that first date is the smaller of the two
			if (DateTwo.before(DateOne)) {
				Date Temp;
				Temp = DateOne; // ' Swap them if not
				DateOne = DateTwo;
				DateTwo = Temp;
			}

			int Year = 0, Month = 0, Day = 0;
			Calendar date1 = Calendar.getInstance();
			date1.setTime(df.parse(sdf.format(DateOne)));
			Calendar date2 = Calendar.getInstance();
			date2.setTime(df.parse(sdf.format(DateTwo)));

			date2.add(Calendar.DAY_OF_MONTH, 1);

			logger.info("ServiceDate>>> " + date1.getTime());
			logger.info("Expiry Date>>> " + date2.getTime());

			if (!DateOne.equals(DateTwo)) {
				if (date2.get(Calendar.YEAR) > date1.get(Calendar.YEAR)) {
					logger.info("year2>>" + date2.get(Calendar.YEAR)
							+ ">>>year1>>> " + date1.get(Calendar.YEAR));
					// date2.add(Calendar.YEAR,-date1.get(Calendar.YEAR));
					Year = date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR);// date2.get(Calendar.YEAR);
				}
				logger.info("year>>" + Year);
				logger.info(date2.get(Calendar.DAY_OF_MONTH) + " , "
						+ date2.get(Calendar.YEAR));

				// ' Months

				// date1.setTime(df.parse(sdf.format(DateOne)));
				// date2.setTime(df.parse(sdf.format(DateTwo)));
				// date2.add(Calendar.DAY_OF_MONTH, 1);

				date2.add(Calendar.MONTH, -(date1.get(Calendar.MONTH)));

				Month = date2.get(Calendar.MONTH);// -
				// date1.get(Calendar.MONTH);
				logger.info("Month>>" + Month);
				logger.info("ServiceDate>>> " + date1.getTime());
				logger.info("Expiry Date>>> " + date2.getTime());

				// Reset Dates
				date1.setTime(df.parse(sdf.format(DateOne)));
				date2.setTime(df.parse(sdf.format(DateTwo)));
				date2.add(Calendar.DAY_OF_MONTH, 1);

				if (date2.get(Calendar.MONTH) <= date1.get(Calendar.MONTH)) {
					if (Year > 0)
						Year -= 1;
				}
				logger.info("year>>" + Year);
				logger.info("Month>>" + Month);

				// ' Days
				logger.info(date2.get(Calendar.DAY_OF_MONTH) + " , "
						+ date1.get(Calendar.DAY_OF_MONTH));
				date2.add(Calendar.DAY_OF_MONTH,
						-date1.get(Calendar.DAY_OF_MONTH));
				Day = date2.get(Calendar.DAY_OF_MONTH);// -
				// date1.get(Calendar.DAY_OF_MONTH)
				// + 1;
				logger.info("Day>>" + Day);

				// Reset Dates
				date1.setTime(df.parse(sdf.format(DateOne)));
				date2.setTime(df.parse(sdf.format(DateTwo)));
				date2.add(Calendar.DAY_OF_MONTH, 1);

				if (date2.get(Calendar.DAY_OF_MONTH) < date1
						.get(Calendar.DAY_OF_MONTH)) {
					if (Month > 0)
						Month -= 1;
				}
				logger.info("Month>>" + Month);
				if (date2.get(Calendar.DAY_OF_MONTH) > date1
						.get(Calendar.DAY_OF_MONTH)) {
					Day = 0;
					Month += 1;
				}
				logger.info("Month>2>" + Month);
				// Corrections
				if (Month == 0) {
					Month = 0;
					Year += 1;
				}

				if (date2.get(Calendar.YEAR) == date1.get(Calendar.YEAR)
						&& date2.get(Calendar.MONTH) == date1
						.get(Calendar.MONTH)) {
					Year = 0;
				}

				totalmonth = Year * 12 + Month;
			} else {
				totalmonth = 0;
			}

		} catch (Exception e) {
			logger.info("error at Y_M_D_Diff>>>" + e.getMessage());
		}
		return totalmonth;
	}

	public String GetSystemSetting(String SettingName) {
		String SettingValue = "";

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getSystemSettingQuery(SettingName));
			while (rs.next()) {
				SettingValue = rs.getString("SettingValue");
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetSystemSetting-->", ex);
		}
		return SettingValue;
	}

	@SuppressWarnings("finally")
	public List<DepreciationModel> CalculateDepreciation(Date ServiceDate,
			double Price, double OpeningBalance, int LifeYears, int LifeMonths,
			Date EndDate, double MonthlyDepn) {
		List<DepreciationModel> lstDepr = new ArrayList<DepreciationModel>();
		String DepreciationPeriod = "";
		int i = 0;
		int XStart = 1;
		Date VarDate = ServiceDate;
		Date PeriodStartDate = ServiceDate;
		int DaysInMonth = 0;
		int DaysNextMonth = 0;
		int DaysRemain = 0;
		double Fraction = 0;
		double DepreciationAmount = 0;
		double NetBookValue = Price - OpeningBalance;
		double AccDeprec = OpeningBalance;

		try {
			DepreciationPeriod = GetSystemSetting("Depreciation Period");
			if (DepreciationPeriod.toLowerCase().equals("monthly")) {
				int C = 0;// 'C : Number of periods
				C = LifeYears * 12 + LifeMonths;
				if (C == 0 || Price <= 0) {
					return lstDepr;
				}
				for (i = XStart; i < C; i++) {
					Calendar date1 = Calendar.getInstance();
					date1.setTime(df.parse(sdf.format(VarDate)));
					DaysInMonth = date1.get(Calendar.DAY_OF_MONTH);
					Calendar date2 = Calendar.getInstance();
					date2.setTime(df.parse(sdf.format(VarDate)));
					date2.add(Calendar.MONTH, 1);
					DaysNextMonth = date2
							.getActualMaximum(Calendar.DAY_OF_MONTH);// date2.get(Calendar.DAY_OF_MONTH);
					// logger.info("date2>>>" + date2.getTime());
					// logger.info("DaysNextMonth>>>" + DaysNextMonth);
					DaysRemain = DaysInMonth - date1.get(Calendar.DAY_OF_MONTH);
					if (DaysRemain == 0) {
						Fraction = 1;
						date1.add(Calendar.DAY_OF_MONTH, 1);
						PeriodStartDate = date1.getTime();
						date1.setTime(df.parse(sdf.format(VarDate)));
						date1.add(Calendar.DAY_OF_MONTH, DaysNextMonth);
						VarDate = date1.getTime();
						// logger.info("1>>>" + VarDate);
					} else {
						Fraction = (DaysRemain + 1) / DaysInMonth;
						PeriodStartDate = VarDate;
						date1.add(Calendar.DAY_OF_MONTH, DaysRemain);
						VarDate = date1.getTime();
						logger.info("2>>>" + VarDate);
					}

					DepreciationModel obj = new DepreciationModel();
					obj.setSrNo(i);
					obj.setPeriodStart(PeriodStartDate);
					obj.setPeriodEnd(VarDate);
					int days = DateDiff(PeriodStartDate, VarDate);
					obj.setDays(days);
					DepreciationAmount = (NetBookValue / C) * Fraction;
					String formate = dcf.format(DepreciationAmount);
					DepreciationAmount = dcf.parse(formate).doubleValue();// Double.parseDouble(String.valueOf(DepreciationAmount));

					obj.setDepreciationAmount(DepreciationAmount);
					AccDeprec += DepreciationAmount;
					obj.setAccDeprec(dcf.parse(dcf.format(AccDeprec))
							.doubleValue());
					double NBValue = Price - AccDeprec;
					NBValue = dcf.parse(dcf.format(NBValue)).doubleValue();
					obj.setNetBookValue(NBValue);
					if (NBValue == 0) {
						obj.setAccDeprec(AccDeprec - 1);
						obj.setDepreciationAmount(DepreciationAmount - 1);
						obj.setNetBookValue(1);
					}
					obj.setNotes("");
					lstDepr.add(obj);
				}

				if (Price - AccDeprec > 0) {
					DepreciationModel obj = new DepreciationModel();
					if (OpeningBalance > 0) {
						obj.setSrNo(i + 1);
					} else {
						obj.setSrNo(i);

					}
					Calendar date2 = Calendar.getInstance();
					date2.setTime(df.parse(sdf.format(VarDate)));
					date2.add(Calendar.DAY_OF_MONTH, 1);
					PeriodStartDate = date2.getTime();
					obj.setPeriodStart(PeriodStartDate);
					if (EndDate.before(PeriodStartDate)) {
						date2.setTime(df.parse(sdf.format(PeriodStartDate)));
						date2.add(Calendar.DAY_OF_MONTH, 1);
						EndDate = date2.getTime();
						int days = DateDiff(PeriodStartDate, EndDate);
						obj.setDays(days);
					} else {
						int days = DateDiff(PeriodStartDate, EndDate);// + 1;
						obj.setDays(days);
					}
					obj.setPeriodEnd(EndDate);
					DepreciationAmount = Price - AccDeprec;
					DepreciationAmount = dcf.parse(
							dcf.format(DepreciationAmount)).doubleValue();
					obj.setDepreciationAmount(DepreciationAmount);
					AccDeprec += DepreciationAmount;
					obj.setAccDeprec(dcf.parse(dcf.format(AccDeprec))
							.doubleValue());
					double NBValue = Price - AccDeprec;
					NBValue = dcf.parse(dcf.format(NBValue)).doubleValue();
					obj.setNetBookValue(NBValue);
					if (NBValue == 0) {
						obj.setAccDeprec(AccDeprec - 1);
						obj.setDepreciationAmount(DepreciationAmount - 1);
						obj.setNetBookValue(1);
					}
					obj.setNotes("");
					lstDepr.add(obj);
				}

			}
		}

		catch (Exception ex) {
			StringWriter sw = null;
			sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.info("error at CalculateDepreciation>>> " + sw.toString());
		} finally {
			return lstDepr;
		}

	}

	private int DateDiff(Date date1, Date date2) {
		int days = 0;
		try {

			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(df.parse(sdf.format(date1)));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(df.parse(sdf.format(date2)));

			cal2.add(Calendar.DAY_OF_MONTH, -cal1.get(Calendar.DAY_OF_MONTH));
			days = cal2.get(Calendar.DAY_OF_MONTH);
			days += 1;
		} catch (Exception e) {
			logger.info("error at DateDiff>>> " + e.getMessage());
		}

		return days;
	}

	public int InsertDepreciation(int assetID, int locationId,
			List<DepreciationModel> lstDep) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			for (DepreciationModel obj : lstDep) {
				result = db.executeUpdateQuery(query.InsertDepreciationQuery(
						assetID, locationId, obj));
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---InsertDepreciation-->", ex);
		}
		return result;
	}

	// Items List
	public List<QbListsModel> fillQbItemsLists(String isActive) {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetQBItemsListQuery(isActive));
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setItem_Key(rs.getInt("item_key"));
				obj.setName(rs.getString("Name"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setIncomeAccountRef(rs.getInt("IncomeAccountRef"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setSalesDesc(rs.getString("SalesDesc") == null ? "" : rs
						.getString("SalesDesc"));
				obj.setPurchaseDesc(rs.getString("PurchaseDesc"));
				obj.setPurchaseCost(rs.getDouble("PurchaseCost"));
				obj.setSalesPrice(rs.getDouble("SalesPrice"));
				obj.setIsActive(rs.getString("isactive"));
				if (obj.getIsActive().equalsIgnoreCase("Y")) {
					obj.setIsActive("Active");
				} else {
					obj.setIsActive("INActive");
				}
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillQbItemsLists-->", ex);
		}
		return lst;
	}

	// Chart of Accounts
	public List<AccountsModel> fillChartofAccounts(String isActive) {
		List<AccountsModel> lst = new ArrayList<AccountsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		DecimalFormat dcf = new DecimalFormat("#,##0.00");
		try {
			rs = db.executeNonQuery(query.GetCharofAccountsListQuery(isActive));
			while (rs.next()) {
				AccountsModel obj = new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setAccountType(rs.getString("AccountType"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setDescription(rs.getString("Description") == null ? ""
						: rs.getString("Description"));
				double TotalBalance = rs.getDouble("TotalBalance");
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance))
						.doubleValue());
				obj.setClassName(rs.getString("Class"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillChartofAccounts-->", ex);
		}
		return lst;
	}

	public int UpdateAccount(int Rec_No, String name, String description) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.UpdateAccountQuery(Rec_No,
					name, description));
		} catch (Exception ex) {
			logger.error("error in HBAData---UpdateAccount-->", ex);
		}
		return result;

	}

	// Bank Transfer
	public BanksModel getBanksDetail(int RecNo) {
		BanksModel objBank = new BanksModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getBanksDetailQuery(RecNo));
			while (rs.next()) {
				objBank = new BanksModel();
				objBank.setRecno(rs.getInt("RecNo"));
				objBank.setBankName(rs.getString("name"));
				objBank.setAccountRefKey(rs.getInt("AccountRefKey"));
				objBank.setAttn_Name(rs.getString("Attn_Name"));
				objBank.setAttn_Position(rs.getString("Attn_Position"));
				objBank.setActName(rs.getString("ActName"));
				objBank.setActNumber(rs.getString("ActNumber"));
				objBank.setBranch(rs.getString("Branch"));
				objBank.setIBANNo(rs.getString("IBANNo"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getBanksDetail-->", ex);
		}
		return objBank;
	}

	public int UpdateAccountDetails(BankTransferModel obj, String listType,
			int payKey) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.UpdateAccountDetailsQuery(obj,
					listType, payKey));
		} catch (Exception ex) {
			logger.error("error in HBAData---UpdateAccountDetails-->", ex);
		}
		return result;
	}

	// Garage
	public List<JobModel> getJobGarageList() {
		List<JobModel> lst = new ArrayList<JobModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;

		HashMap<String, String> hmStatus = new HashMap<String, String>();
		hmStatus.put("F", "Finished");
		hmStatus.put("C", "Created");
		hmStatus.put("A", "Created");
		hmStatus.put("S", "Started");

		try {
			rs = db.executeNonQuery(query.getJobGarageListQuery());
			while (rs.next()) {
				JobModel obj = new JobModel();
				obj.setStatus(hmStatus.get(rs.getString("STATUS")));
				obj.setRecno(rs.getInt("REC_NO"));
				obj.setChasisNo(rs.getString("CHASIS_NO") == null ? "" : rs
						.getString("CHASIS_NO"));
				obj.setFullName(rs.getString("FULLNAME") == null ? "" : rs
						.getString("FULLNAME"));
				obj.setTxnDate(df.parse(sdf.format(rs.getDate("TXN_DATE"))));
				obj.setTxnTime(df.parse(sdf.format(rs.getDate("TXN_TIME"))));
				obj.setSa(rs.getInt("SA"));
				obj.setSaName(rs.getString("SAName") == null ? "" : rs
						.getString("SAName"));
				obj.setWorkStart(df.parse(sdf.format(rs.getDate("WORK_START"))));
				obj.setWorkEnd(df.parse(sdf.format(rs.getDate("WORK_END"))));
				obj.setOdometer(rs.getInt("ODOMETER"));
				obj.setNotes(rs.getString("NOTES") == null ? "" : rs
						.getString("NOTES"));
				obj.setRefNo(rs.getInt("REF_NO"));
				obj.setEngineNo(rs.getString("ENGINE_NO") == null ? "" : rs
						.getString("ENGINE_NO"));
				obj.setRegNo(rs.getString("REG_NO") == null ? "" : rs
						.getString("REG_NO"));
				obj.setRoutNo(rs.getString("ROUT_NO") == null ? "" : rs
						.getString("ROUT_NO"));
				obj.setBrand(rs.getString("Brand") == null ? "" : rs
						.getString("Brand"));
				obj.setSeries(rs.getString("Series") == null ? "" : rs
						.getString("Series"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getJobGarageList-->", ex);
		}
		return lst;
	}

	// By Iqbal for cash Invoice Queries
	// ----------------------------------------------------------------------------------------------------------

	// Chnages by iqbal for cash invoice
	public CashInvoiceModel getCashInvoiceCusomerInfo(String ListType, int keyID) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPayToOrderInfoQuery(ListType,keyID));
			while (rs.next()) {
				if (ListType.equals("Prospective")) {
					obj.setShipToAddress(rs.getString("shipTo") == null ? "": rs.getString("shipTo"));
					break;
				}
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("email"));
				obj.setBillAddressCity(rs.getString("BillCity") == null ? "": rs.getString("BillCity"));
				obj.setBillAddressState(rs.getString("BillState") == null ? "": rs.getString("BillState"));
				obj.setBillAddressCountry(rs.getString("BillCountry") == null ? "": rs.getString("BillCountry"));
				obj.setBillAddressPostalCode(rs.getString("BillPostalCode") == null ? "": rs.getString("BillPostalCode"));
				obj.setBillAddress5(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setTotalBalance(rs.getDouble("LocalBalance"));
				obj.setBillAddress1(rs.getString("BillAddress1") == null ? "": rs.getString("BillAddress1"));
				obj.setBillAddress2(rs.getString("BillAddress2") == null ? "": rs.getString("BillAddress2"));
				if (ListType.equals("Customer")) {
					obj.setFullname(rs.getString("fullname") == null ? "" : rs.getString("fullname"));
					obj.setBillAddress3(rs.getString("BillAddress3") == null ? "": rs.getString("BillAddress3"));
					obj.setBillAddress4(rs.getString("BillAddress4") == null ? "": rs.getString("BillAddress4"));
					obj.setStatus(rs.getString("isactive") == null ? "" : rs.getString("isactive"));
					if (rs.getDate("cusContractExpiry") != null) {
						obj.setCustomerContactExpiryDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("cusContractExpiry")) == null ? "": new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("cusContractExpiry")));
					} else {
						obj.setCustomerContactExpiryDateStr("");
					}
					if (rs.getDate("timeCreated") != null) {
						obj.setCustomerCreatedDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("timeCreated")) == null ? "": new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("timeCreated")));
					} else {
						obj.setCustomerCreatedDate("");
					}

					obj.setBalckListed(rs.getString("blackListed") == null ? "Not Set": rs.getString("blackListed"));
					if (obj.getBalckListed().equalsIgnoreCase("Y")) {
						obj.setBalckListed("Yes");
					} else if (obj.getBalckListed().equalsIgnoreCase("N")) {
						obj.setBalckListed("No");
					}

					if (obj.getStatus().equalsIgnoreCase("Y")) {
						obj.setStatus("Active");
					} else {
						obj.setStatus("InActive");
					}

				} else {
					obj.setBillAddress3("");
					obj.setBillAddress4("");
				}
				obj.setPhone(rs.getString("Phone") == null ? "" : rs.getString("Phone"));
				obj.setFax(rs.getString("fax") == null ? "" : rs.getString("fax"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? "": rs.getString("PrintChequeAs"));

				if (ListType.equals("Customer")) {
					obj.setAccountName(rs.getString("ActName") == null ? "": rs.getString("ActName"));
					obj.setAccountNo(rs.getString("AccountNo") == null ? "": rs.getString("AccountNo"));
					obj.setBankName(rs.getString("BankName") == null ? "" : rs.getString("BankName"));
					obj.setBranchName(rs.getString("BranchName") == null ? "": rs.getString("BranchName"));
					obj.setiBANNo(rs.getString("IBANNo") == null ? "" : rs.getString("IBANNo"));
					obj.setCreditLimit(rs.getString("CreditLimit") == null ? "": rs.getString("CreditLimit"));

				}
				if (ListType.equals("Employee")) {
					obj.setFullname(rs.getString("fullname") == null ? "" : rs.getString("fullname"));
					obj.setAccountName(rs.getString("ActName") == null ? "": rs.getString("ActName"));
					obj.setAccountNo(rs.getString("Account_No") == null ? "": rs.getString("Account_No"));
					obj.setBankName(rs.getString("BANK_ID") == null ? "" : rs.getString("BANK_ID"));
					obj.setBranchName(rs.getString("BRANCH_ID") == null ? "": rs.getString("BRANCH_ID"));
					obj.setiBANNo(rs.getString("IBANNo") == null ? "" : rs.getString("IBANNo"));
				}
				if (ListType.equals("Vendor")) {
					obj.setFullname(rs.getString("fullname") == null ? "" : rs.getString("fullname"));
					obj.setAccountName(rs.getString("ActName") == null ? "": rs.getString("ActName"));
					obj.setAccountNo(rs.getString("ActNumber") == null ? "": rs.getString("ActNumber"));
					obj.setBankName(rs.getString("BankName") == null ? "" : rs.getString("BankName"));
					obj.setBranchName(rs.getString("Branch") == null ? "" : rs.getString("Branch"));
					obj.setiBANNo(rs.getString("IBANNo") == null ? "" : rs.getString("IBANNo"));
				}
				if (ListType.equals("OtherNames")) {
					obj.setFullname(rs.getString("fullname") == null ? "" : rs.getString("fullname"));
					obj.setAccountName(rs.getString("ActName") == null ? "": rs.getString("ActName"));
					obj.setAccountNo(rs.getString("AccountNo") == null ? "": rs.getString("AccountNo"));
					obj.setBankName(rs.getString("BankName") == null ? "" : rs.getString("BankName"));
					obj.setBranchName(rs.getString("BranchName") == null ? "": rs.getString("BranchName"));
					obj.setiBANNo(rs.getString("IBANNo") == null ? "" : rs.getString("IBANNo"));
				}
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCashInvoiceCustomerInfo-->", ex);
		}
		return obj;
	}

	public boolean checkIfItemHasSubQuery(String itemName) {
		boolean hasSubAccount = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIfItemHasSubQuery(itemName));
			while (rs.next()) {
				hasSubAccount = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkIfClassHasSub-->", ex);
		}

		return hasSubAccount;
	}

	// by iqbal--- for sale number-- cash invoice module--return type string
	public String GetSaleNumber(String serialField) {
		String LastNumber = "1";

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetSerialNumberQuery(serialField));
			while (rs.next()) {
				LastNumber = rs.getString("LastNumber");
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetSerialNumber-->", ex);
		}
		return LastNumber;
	}

	// cash Invoice By Iqbal
	public int GetNewCashInvoiceRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewCashInvoiceRecNoQuery());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewCheckMastRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public int GetNewCreditInvoiceRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewCreditInvoiceRecNoQuery());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewCreditInvoiceRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public CompSetupModel getDefaultSetUpInfoForCashInvoice() {
		CompSetupModel obj = new CompSetupModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getDefaultSetUpInfoForCashInvoice());
			while (rs.next()) {
				obj.setPostOnMainClass(rs.getString("PostOnMainClass"));
				obj.setPostItem2Main(rs.getString("postItem2Main"));
				obj.setSellStockWithZero(rs.getString("sellStockWithZero"));
				obj.setAllowSavingAvgCost(rs.getString("allowSavingAvgCost"));
				obj.setUseSellItemWithLowerSP(rs.getString("useSellItemWithLowerSP"));
				obj.setUseMinSellingPrice(rs.getString("useMinSellingPrice"));
				obj.setMinSellingPriceRatio(rs.getDouble("minSellingPriceRatio"));
				obj.setMaxSellingPriceRatio(rs.getDouble("maxSellingPriceRatio"));
				obj.setUseMaxSellingPrice(rs.getString("useMaxSellingPrice"));
				obj.setShowAvgCost(rs.getString("ShowAvgCost"));
				obj.setSaveInvQty(rs.getString("SaveINVQty"));
				obj.setUseSalesRepComition(rs.getString("UseSalesRepCommission"));
				obj.setCanExceedOverLimit(rs.getString("canExceedCreditLimit"));
				obj.setAddress(rs.getString("address"));
				obj.setCompanyName(rs.getString("companyName"));
				obj.setAllowToAddInventorySite(rs.getString("AllowToAddInvSite"));
				obj.setCountrykey(rs.getInt("countrykey"));
				obj.setCitykey(rs.getInt("citykey"));
				obj.setPhone1(rs.getString("telephone"));
				obj.setFax(rs.getString("Fax"));
				obj.setCcemail(rs.getString("ccemail"));
				obj.setUseSalesFlow(rs.getString("UseSalesFlow")== null ? "N": rs.getString("UseSalesFlow"));
				obj.setUsePurchaseFlow(rs.getString("usePurchaseFlow") == null ? "N": rs.getString("usePurchaseFlow"));	
				obj.setAllowToSkip(rs.getString("AllowToSkipSalesWorkFlow") == null ? "": rs.getString("AllowToSkipSalesWorkFlow"));
				obj.setAllowToSkipPurchaseWorkFlow(rs.getString("AllowToSkipPurchaseWorkFlow") == null ? "": rs.getString("AllowToSkipPurchaseWorkFlow"));
				
								
				if (rs.getString("PVSERIALNOS") == null
						|| rs.getString("PVSERIALNOS").equals(""))
					obj.setPvSerialNos("S");
				else
					obj.setPvSerialNos(rs.getString("PVSERIALNOS"));
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---GetDefaultSetupInfoForCashInvoice-->",
					ex);
		}
		return obj;
	}

	public int addNewCashInvoice(CashInvoiceModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewCashInvoiceQuery(obj,
					webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewCashInvoice-->", ex);
		}
		return result;
	}

	public int updateCashInvoice(CashInvoiceModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateCashInvoiceQuery(obj,
					webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateCashInvoice-->", ex);
		}
		return result;
	}





	public int updateCreditInvoice(CashInvoiceModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateCreditInvoiceQuery(obj,
					webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateCreditInvoice-->", ex);
		}
		return result;
	}

	public int addCreditCashInvoice(CashInvoiceModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewCreditInvoiceQuery(obj,webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCreditCashInvoice-->", ex);
		}
		return result;
	}

	public List<ClassModel> GetClassModel() {
		List<ClassModel> lst=new ArrayList<ClassModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		ClassModel obj=new ClassModel();
		rs = db.executeNonQuery(query.getGridVlaueforClass());
		try {
			while (rs.next()) {
				obj.setClass_Key(rs.getInt("class_key"));
				obj.setName(rs.getString("name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSublevel(rs.getInt("SubLevel"));
				lst.add(obj);
			}
		} catch (SQLException e) {
			logger.error("error in HBAData---GetClassModel-->", e);
		}
		return lst;
	}

	public List<QbListsModel> GetMasterData(String text) {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		QbListsModel obj = new QbListsModel();
		obj.setRecNo(0);
		obj.setFullName("Select");
		obj.setName("Select");
		lst.add(obj);
		try {
			if (text.toLowerCase().equalsIgnoreCase("Name")) 
			{
				rs = db.executeNonQuery(query.getCustomerProfile());
			} 
			else if (text.toLowerCase().equalsIgnoreCase("Class"))
			{
				rs = db.executeNonQuery(query.getCustomerClass());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("Class_Key"));
					obj.setName(rs.getString("Name"));
					obj.setListID(rs.getString("ListID"));
					obj.setSubLevel(rs.getInt("SubLevel"));
					obj.setFullName(rs.getString("FullName"));
					obj.setSubItemsCount(rs.getInt("subItemsCount"));	
					lst.add(obj);
				}
			} 
			else if (text.toLowerCase().equalsIgnoreCase("DepositeTo")) {
				rs = db.executeNonQuery(query.getCustomerDepositeTo());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("Rec_No"));
					obj.setName(rs.getString("Name"));
					obj.setSubLevel(rs.getInt("SubLevel"));
					obj.setListID(rs.getString("ListID"));
					obj.setItemType("AccountType");
					lst.add(obj);
				}
			} else if (text.toLowerCase().equalsIgnoreCase("SalesRep")) {
				rs = db.executeNonQuery(query.getCustomerRep());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("SalesRep_Key"));
					obj.setFullName(rs.getString("fullname"));
					obj.setListType(rs.getString("ListType"));
					obj.setListID(rs.getString("ListID"));
					obj.setListType(rs.getString("ListType"));
					obj.setIntials(rs.getString("Initial") == null ? "" : rs
							.getString("Initial"));
					lst.add(obj);
				}
			} else if (text.toLowerCase().equalsIgnoreCase("Payment")) {
				rs = db.executeNonQuery(query.getCustomerPaymentMethod());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("Rec_No"));
					obj.setName(rs.getString("Name"));
					obj.setListID(rs.getString("ListID"));
					lst.add(obj);
				}
			} else if (text.toLowerCase().equalsIgnoreCase("Template")) {
				rs = db.executeNonQuery(query.getCustomerTemplates());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("RecNo"));
					obj.setName(rs.getString("TemplateName"));
					obj.setListType(rs.getString("TemplateType"));
					lst.add(obj);
				}
			} else if (text.toLowerCase().equalsIgnoreCase("SendVia")) {
				rs = db.executeNonQuery(query.getCustomerSendVia());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("id"));
					obj.setName(rs.getString("description"));
					lst.add(obj);
				}
			} else if (text.toLowerCase().equalsIgnoreCase("GridSite")) {
				rs = db.executeNonQuery(query.getGridInventorySite());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("itemKey"));
					obj.setName(rs.getString("sitename"));
					obj.setListType(rs.getString("itemtpe"));
					lst.add(obj);
				}
			} else if (text.toLowerCase().equalsIgnoreCase("GridClass")) {
				rs = db.executeNonQuery(query.getGridVlaueforClass());
				while (rs.next()) {
					obj = new QbListsModel();
					obj.setRecNo(rs.getInt("class_key"));
					obj.setName(rs.getString("name"));
					obj.setFullName(rs.getString("fullname"));
					obj.setSubLevel(rs.getInt("SubLevel"));
					obj.setSubItemsCount(rs.getInt("subItemsCount"));					
					lst.add(obj);
				}
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---fillQbItemsList-->", ex);
		}
		return lst;
	}

	public CashInvoiceGridData getInvoiceGridData(int Item_Key) {
		CashInvoiceGridData obj = new CashInvoiceGridData();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getGridVlaueforDescandArabicDesc(Item_Key));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("Item_Key"));
				obj.setInvoiceDescription(rs.getString("SalesDesc"));
				obj.setInvoicearabicDescription(rs.getString("DescriptionAR"));
				Double qtyOnhand = rs.getDouble("QuantityOnHand");
				obj.setInvoiceQtyOnHand(qtyOnhand.intValue());
				obj.setInvoiceRate(rs.getDouble("SalesPrice"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setAvgCost(rs.getDouble("averagecost"));
				obj.setBarcode(rs.getString("BARCODE"));
				obj.setSelectedClass(rs.getInt("ClassKey"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQbItemsData-->", ex);
		}
		return obj;
	}

	public CheckItemsModel getItemData(int Item_Key) {
		CheckItemsModel obj = new CheckItemsModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getGridVlaueforDescandArabicDesc(Item_Key));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("Item_Key"));
				obj.setDescription(rs.getString("SalesDesc"));
				Double qtyOnhand = rs.getDouble("QuantityOnHand");
				obj.setQuantity(qtyOnhand.intValue());
				obj.setCost(rs.getDouble("SalesPrice"));
				obj.setItemType(rs.getString("ItemType"));
				obj.setSelectedClassKey(rs.getInt("ClassKey"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQbItemsData-->", ex);
		}
		return obj;
	}

	public int addCashInvoiceGridItems(CashInvoiceGridData obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.addNewCashInvoiceGridItemsQuery(obj, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCashInvoiceGridItems-->", ex);
		}
		return result;

	}

	public int addCreditInvoiceGridItems(CashInvoiceGridData obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.addNewCreditInvoiceGridItemsQuery(obj, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCreditInvoiceGridItems-->", ex);
		}
		return result;

	}

	public int deleteCashInvoiceGridItems(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteCashInvoiceGridItemsQuery(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteCashInvoiceGridItems-->", ex);
		}
		return result;

	}

	public int deleteCreditInvoiceGridItems(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteCreditInvoiceGridItemsQuery(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteCreditInvoiceGridItems-->",
					ex);
		}
		return result;

	}

	// by iqbal for calculating next serial number
	public String ConfigSerialNumberCashInvoice(SerialFields field,	String SerialNumber, int keyID) {
		String tmpSerialNumber = SerialNumber;
		String tmpFindLastIdx;
		Boolean tmpStartInt = false;
		String tmpFindNos = "";
		double tmpRightPos = 0.0;
		double tmpLeftPos = 0.0;
		String tmpField;
		String tmpConfigSerailNum = "";
		Integer tmpX;
		try {

			if (keyID == 0)
				tmpField = field.toString();
			else
				tmpField = field.toString() + "-" + String.valueOf(keyID);

			for (tmpX = SerialNumber.length(); tmpX >= 1; tmpX--) {
				tmpFindLastIdx = SerialNumber.substring(tmpX - 1, tmpX);
				if ("1234567890".indexOf(tmpFindLastIdx) != -1) {
					if (tmpStartInt == false) {
						tmpStartInt = true;
						tmpRightPos = tmpX;
					}
					tmpFindNos = tmpFindLastIdx + tmpFindNos;

				} else if (tmpStartInt == true) {
					tmpLeftPos = tmpX + 1;
					break;

				}
			}

			if (tmpStartInt == true) {
				String tmpSuffix = "";
				String tmpPrefix = "";

				if (tmpLeftPos > 0)
					if (((int) (long) Math.round(tmpLeftPos) - 1) <= SerialNumber
					.length())
						tmpSuffix = SerialNumber.substring(0,
								(int) (long) Math.round(tmpLeftPos) - 1);
				if (tmpRightPos > 0)
					if (((int) (long) Math.round(tmpRightPos) + 1) <= SerialNumber
					.length())
						tmpPrefix = SerialNumber.substring((int) (long) Math
								.round(tmpRightPos) + 1);

				double tmpLastNumber = Integer.parseInt(tmpFindNos) + 1;

				tmpConfigSerailNum = tmpSuffix
						+ (int) (long) Math.round(tmpLastNumber) + tmpPrefix;
			} else {
				tmpConfigSerailNum = tmpSerialNumber;
			}

			HBAQueries query = new HBAQueries();
			ResultSet rs = null;

			// check if dulicate exist serial number
			boolean isSerailFound = false;
			rs = db.executeNonQuery(query.GetSerialNumberQuery(tmpField));
			while (rs.next()) {
				isSerailFound = true;
			}
			if (isSerailFound)
				db.executeUpdateQuery(query.updateSystemSerialNosQuery(
						tmpConfigSerailNum, tmpField));
			else
				db.executeUpdateQuery(query.insertSystemSerialNosQuery(
						tmpConfigSerailNum, tmpField));

		} catch (Exception ex) {
			logger.error("error in HBAData---ConfigSerialNumberCashInvoice-->",
					ex);
		}
		return tmpConfigSerailNum;
		// return SerialNumber;
	}

	public List<OtherNamesModel> getOtherNameList(String status, String sortBy) {
		List<OtherNamesModel> lstOtherNames = new ArrayList<OtherNamesModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		String S1, S2;
		try {
			rs = db.executeNonQuery(query.getOtherListQuery(status, sortBy));
			while (rs.next()) {
				OtherNamesModel obj = new OtherNamesModel();
				obj.setCustkey(rs.getInt("othNam_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive") == null ? "" : rs
						.getString("isactive"));
				S1 = rs.getString("BillAddress1") == null ? "" : rs
						.getString("BillAddress1");
				S2 = rs.getString("BillAddress2") == null ? "" : rs
						.getString("BillAddress2");
				obj.setBillCountry(S1 + S2);
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
				obj.setAccountNumber(rs.getString("accountno") == null ? ""
						: rs.getString("accountno"));
				obj.setContactPerson(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setAlternateContact(rs.getString("ALTCONTACT") == null ? ""
						: rs.getString("ALTCONTACT"));
				obj.setSkypeID(rs.getString("SkypeID") == null ? "" : rs
						.getString("SkypeID"));
				obj.setAccountName(rs.getString("ActName") == null ? "" : rs
						.getString("ActName"));
				obj.setBankName(rs.getString("BankName") == null ? "" : rs
						.getString("BankName"));
				obj.setBranchName(rs.getString("BranchName") == null ? "" : rs
						.getString("BranchName"));
				obj.setbBANNumber(rs.getString("IBANNo") == null ? "" : rs
						.getString("IBANNo"));
				obj.setPrintChequeAs(rs.getString("fullName") == null ? "" : rs
						.getString("fullName"));
				lstOtherNames.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getCustomerList-->", ex);
		}
		return lstOtherNames;
	}

	public boolean checkIfSerialNumberIsDuplicate(String SerialNumber,int recNo) {
		boolean hasSerialNumber = false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIfSerialNumberIsDuplicate(
					SerialNumber, recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---checkIfSerialNumberIsDuplicate-->", ex);
		}

		return hasSerialNumber;
	}

	public int GetSerialNumberQueryForChequeNo(int bankNo) {
		int num=0;
		String SerialNumber="ChequeNo-"+bankNo;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetSerialNumberQueryForChequeNo(SerialNumber));
			while (rs.next()) {
				num=rs.getInt("lastNumber");
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---GetSerialNumberQueryForChequeNo-->", ex);
		}

		return num;
	}

	public boolean checkIfSerialNumberIsDuplicateForCreditInvoice(
			String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIfSerialNumberIsDuplicateForCreditInvoice(SerialNumber, recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkIfSerialNumberIsDuplicateForCreditInvoice-->",ex);
		}

		return hasSerialNumber;
	}

	public List<CustomerModel> getOtherNameList() {
		List<CustomerModel> lstOtherNames = new ArrayList<CustomerModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		String S1, S2;
		try {
			rs = db.executeNonQuery(query.getOtherListQuery());
			while (rs.next()) {
				CustomerModel obj = new CustomerModel();
				obj.setCustkey(rs.getInt("othNam_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive") == null ? "" : rs
						.getString("isactive"));
				S1 = rs.getString("BillAddress1") == null ? "" : rs
						.getString("BillAddress1");
				S2 = rs.getString("BillAddress2") == null ? "" : rs
						.getString("BillAddress2");
				obj.setBillCountry(S1 + S2);
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
				lstOtherNames.add(obj);
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getCustomerList-->", ex);
		}
		return lstOtherNames;
	}

	// Iqbal

	public List<PropertyModel> fillPropertyList() {
		List<PropertyModel> lst = new ArrayList<PropertyModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPropetyListQuery());
			while (rs.next()) {
				PropertyModel obj = new PropertyModel();
				obj.setPropetyName(rs.getString("bldg_name") == null ? "" : rs
						.getString("bldg_name"));
				obj.setPropety_Type(rs.getInt("property_type"));
				if (obj.getPropety_Type() == -1) {
					obj.setPropetyType("ALL");
				} else {
					obj.setPropetyType(rs.getString("propertytype") == null ? ""
							: rs.getString("propertytype"));
				}
				obj.setPlotNo(rs.getString("plotno") == null ? "" : rs
						.getString("plotno"));
				obj.setAge(rs.getString("ageofbldg") == null ? "" : rs
						.getString("ageofbldg"));
				obj.setCost(rs.getDouble("cost"));
				obj.setNoOfunits(rs.getString("noofunits") == null ? "" : rs
						.getString("noofunits"));
				obj.setUnitcost(rs.getDouble("estcost"));
				obj.setLandLord(rs.getString("landlord") == null ? "" : rs
						.getString("landlord"));
				obj.setOwner(rs.getString("owner") == null ? "" : rs
						.getString("bldg_name"));
				obj.setWatchman(rs.getString("watchman") == null ? "" : rs
						.getString("watchman"));
				obj.setWatchmanPhone(rs.getString("watchman_phone") == null ? ""
						: rs.getString("watchman_phone"));
				obj.setCity(rs.getString("city") == null ? "" : rs
						.getString("city"));
				obj.setStreet(rs.getString("street") == null ? "" : rs
						.getString("street"));
				obj.setCountry(rs.getString("country") == null ? "" : rs
						.getString("country"));
				obj.setConatactMaintanace(rs.getString("contractmaint") == null ? ""
						: rs.getString("contractmaint"));
				String maintexpenses = rs.getString("maintexpenses") == null ? ""
						: rs.getString("maintexpenses");
				if (maintexpenses != null && !maintexpenses.equals(""))
					obj.setMaintananceExpences(rs.getDouble("maintexpenses"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillPropertyList-->", ex);
		}
		return lst;
	}

	public List<VehicleModel> fillVehicleList() {
		List<VehicleModel> lst = new ArrayList<VehicleModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getVehicleListQuery());
			while (rs.next()) {
				VehicleModel obj = new VehicleModel();
				obj.setChassisNumber(rs.getString("chasis_no") == null ? ""
						: rs.getString("chasis_no"));
				obj.setVehicleType(rs.getString("VTypeEn") == null ? "" : rs
						.getString("VTypeEn"));
				obj.setRegNumber(rs.getString("reg_no") == null ? "" : rs
						.getString("reg_no"));
				obj.setBrand(rs.getString("brandEn") == null ? "" : rs
						.getString("brandEn"));
				obj.setType(rs.getString("seriesEn") == null ? "" : rs
						.getString("seriesEn"));
				obj.setOwnerName(rs.getString("fullname") == null ? "" : rs
						.getString("fullname"));
				obj.setPhone(rs.getString("phone") == null ? "" : rs
						.getString("phone"));
				obj.setMobile(rs.getString("altPhone") == null ? "" : rs
						.getString("altPhone"));
				obj.setAssetCode(rs.getString("assetcode") == null ? "" : rs
						.getString("assetcode"));
				obj.setAssetName(rs.getString("assetname") == null ? "" : rs
						.getString("assetname"));
				obj.setColor(rs.getString("coloren") == null ? "" : rs
						.getString("coloren"));
				obj.setPower(rs.getString("power") == null ? "" : rs
						.getString("power"));
				obj.setOdoMeter(rs.getString("odometer") == null ? "" : rs
						.getString("odometer"));
				obj.setYear(rs.getString("myear") == null ? "" : rs
						.getString("myear"));
				obj.setEngine(rs.getString("engine_no") == null ? "" : rs
						.getString("engine_no"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---fillVehicleList-->", ex);
		}
		return lst;
	}

	public List<QbListsModel> getNameFromQbListForValidation() {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getNameFromQbListForValidation());
			while (rs.next()) {
				QbListsModel qbData = new QbListsModel();
				qbData.setName(rs.getString("name"));
				qbData.setRecNo(rs.getInt("recno"));
				lst.add(qbData);
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getOtherNamesListHeadders-->", ex);
		}
		return lst;
	}

	public int GetOtherNameListRecNoQuery() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetOtherNameListRecNoQuery());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetOtherNameListRecNoQuery-->", ex);
		}
		return MaxRecNo;
	}

	public int OtherNameListInsertQuery(OtherNamesModel customerModel, int recNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			customerModel.setCreatedDate(new Date());
			String str = query.OtherNameListInsertQuery(customerModel, recNo);
			result = db.executeUpdateQuery(str);
		} catch (Exception ex) {
			logger.error("error in HBAData---OtherNameListInsertQuery-->", ex);
		}
		return result;
	}

	public int OtherNameListInsertQbListQuery(OtherNamesModel customerModel,
			int recNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			customerModel.setCreatedDate(new Date());
			String str = query.OtherNameListInsertQbListQuery(customerModel,
					recNo);
			result = db.executeUpdateQuery(str);
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---OtherNameListInsertQbListQuery-->", ex);
		}
		return result;
	}

	public List<CustomerStatusHistoryModel> getCustomerStatusHistoryReport(Date fromDate, Date toDate) {
		List<CustomerStatusHistoryModel> lst = new ArrayList<CustomerStatusHistoryModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomerStatusHistoryReport(fromDate,toDate));
			while (rs.next()) {
				CustomerStatusHistoryModel obj = new CustomerStatusHistoryModel();
				obj.setRecNo(rs.getInt("recno"));
				obj.setCustKey(rs.getInt("custKey"));
				obj.setActionDate(rs.getDate("actionDate"));
				obj.setActionDatstr(new SimpleDateFormat("dd-MM-yyyy h:mm a").format(rs.getDate("actionDate")));
				obj.setStatusDescription(rs.getString("statusdescription") == null ? "": rs.getString("statusdescription"));
				obj.setType(rs.getString("type") == null ? "" : rs.getString("type"));
				if (obj.getType() != null && obj.getType().equalsIgnoreCase("C")) {
					obj.setType("Customer");
					obj.setCustomerName(rs.getString("fullname") == null ? "": rs.getString("fullname"));
				} else if (obj.getType() != null && obj.getType().equalsIgnoreCase("P")) {
					obj.setType("Prospective");
					obj.setCustomerName(rs.getString("fullNameProspecive") == null ? "": rs.getString("fullNameProspecive"));
				} else {
					obj.setCustomerName("");
				}
				obj.setCreatedFrom(rs.getString("createdFrom") == null ? "": rs.getString("createdFrom"));
				obj.setTxnRefNumber(rs.getString("txnRefNo") == null ? "" : rs.getString("txnRefNo"));
				obj.setCreatedBy(rs.getString("WebUserName") == null ? "": rs.getString("WebUserName"));
				obj.setWebUserID(rs.getInt("WebUserID"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCustomerStatusHistoryReport-->", ex);
		}

		return lst;

	}

	public List<CutomerSummaryReport> getNewCutomerSummaryReport(int custID,String isActive,boolean hideZero) 
	  {
		List<CutomerSummaryReport> lst = new ArrayList<CutomerSummaryReport>();
		HBAQueries query = new HBAQueries();
		CutomerSummaryReport obj = new CutomerSummaryReport();
		try 
		{
			double tmpBalance =0 , tmpCredit =0,tmpDebit =0;
			String sql="";
			ResultSet rs = null ,rsTrxn=null;	
			rs=db.executeNonQuery(query.getNewCustomerBalanceReport(custID, isActive));
			while (rs.next()) 
			{
				tmpBalance =0; tmpCredit =0;tmpDebit =0;
				obj = new CutomerSummaryReport();
				int customerID=rs.getInt("RecNo");
				obj.setEnityName(rs.getString("FullName"));
				tmpDebit=rs.getDouble("tmpDebit"); //rs.getDouble("TotalDebitExpenses") + rs.getDouble("TotalDebitAdjustment")+rs.getDouble("TotalInvoices")+rs.getDouble("TotalDebitJouranl")+rs.getDouble("TotalDebitContract");
				tmpCredit=rs.getDouble("tmpCredit");//rs.getDouble("TotalCreditMemo") +rs.getDouble("TotalRVMast")+rs.getDouble("TotalCreditAdjustment")+rs.getDouble("TotalCreditJouranl");
				tmpBalance=rs.getDouble("Balance");//tmpDebit -tmpCredit;	
				
				obj.setCredit(tmpCredit);
				obj.setDebit(tmpDebit);
				obj.setBalance(tmpBalance);
				if(hideZero)
				{
					if(obj.getBalance()!=0)
						lst.add(obj);
				}
				else
				{
					lst.add(obj);
				}
								
			}
			if(true)
			return lst;
			
			
			sql="Select Name,RecNo,FullName,isactive from QbLists Where ListType in ('Customer') ";
			if (isActive != null && !isActive.equalsIgnoreCase(""))
				sql+=" and IsActive='" + isActive + "'";
			if(custID>0)
				sql+=" and RecNo=" + custID;
			sql+=" order by ListType,FullName";
			rs = db.executeNonQuery(sql);
			while (rs.next()) 
			{
				obj = new CutomerSummaryReport();
				int customerID=rs.getInt("RecNo");
				obj.setEnityName(rs.getString("FullName"));
													
			
			sql="Select * From RVMast where CustomerRef_Key =" + customerID;
			rsTrxn = db.executeNonQuery(sql);
			while (rsTrxn.next()) 
			{
				if(rsTrxn.getInt("ARAccountRef_Key")==20)//AccountsReceivable
				{
					tmpBalance +=rsTrxn.getDouble("TotalAmount")* -1;
					tmpCredit +=rsTrxn.getDouble("TotalAmount");
				}
			}
			
			sql=" Select * From Invoice where CustomerRefKey =" + customerID;
			rsTrxn = db.executeNonQuery(sql);
			while (rsTrxn.next()) 
			{				
					tmpBalance +=rsTrxn.getDouble("Amount");
					tmpDebit  +=rsTrxn.getDouble("Amount");				
			}
			
			
			obj.setCredit(tmpCredit);
			obj.setDebit(tmpDebit);
			obj.setBalance(tmpBalance);
			lst.add(obj);
			}
			
			
		}
		catch (Exception ex) 
		{
			logger.error("error in HBAData---getNewCutomerSummaryReport-->", ex);
		}
		return lst;
		
	  }
	
	public List<CutomerSummaryReport> getCutomerSummaryReport(Date toDate,int customerID, String isActive, boolean inculdeOtherTransactions,boolean hideZero) 
	  {
		List<CutomerSummaryReport> lst = new ArrayList<CutomerSummaryReport>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		double tempBalance = 0;
		double tempTotal = 0;
		double tempCreditTotal = 0;
		double tempDebitTotal = 0;
		int tempCustomerKey = 0;
		String customerName = "";
		CutomerSummaryReport objTotal = new CutomerSummaryReport();
		try {
			rs = db.executeNonQuery(query.getCutomerDetailedReport(toDate,
					customerID, isActive, inculdeOtherTransactions));
			boolean hasNext = rs.next();
			while (hasNext) 
			{
				CutomerSummaryReport obj = new CutomerSummaryReport();
				obj.setRec_no(rs.getInt("recno"));
				obj.setCustKey(rs.getInt("cust_key"));
				obj.setEnityName(rs.getString("fullName") == null ? "" : rs
						.getString("fullName"));
				// customerName=obj.getEnityName();
				obj.setFromDate(rs.getDate("txnDate"));
				if (rs.getDouble("debit") < 0)
					obj.setDebit(-(rs.getDouble("debit")));
				else
					obj.setDebit(rs.getDouble("debit"));

				if (rs.getDouble("credit") < 0)
					obj.setCredit(-(rs.getDouble("credit")));
				else
					obj.setCredit(rs.getDouble("credit"));
				obj.setRefranceNumber(rs.getString("recNO") == null ? "" : rs
						.getString("recNO"));
				obj.setAcountName(rs.getString("itemOrAccountName") == null ? ""
						: rs.getString("itemOrAccountName"));
				obj.setTxnType(rs.getString("trasType") == null ? "" : rs
						.getString("trasType"));
				obj.setTxnDate(rs.getDate("txndate") == null ? ""
						: new SimpleDateFormat("dd-MM-yyyy").format(rs
								.getDate("txndate")));
				if (rs.getString("trasType").equalsIgnoreCase("J")) {
					obj.setTxnType("Jouneral Voucher");
				} else if (rs.getString("trasType").equalsIgnoreCase("R")) {
					obj.setTxnType("Receipt Voucher");
				}

				// total amount calculation
				if (tempCustomerKey == 0) {
					tempCustomerKey = rs.getInt("cust_key");
				}
				if (tempCustomerKey != 0
						&& tempCustomerKey == rs.getInt("cust_key")) {
					objTotal = new CutomerSummaryReport();
					objTotal.setEnityName(obj.getEnityName());
					objTotal.setTxnType("Total");
					customerName = obj.getEnityName();
					tempTotal = tempTotal + obj.getAmount();
					tempCreditTotal = tempCreditTotal + obj.getCredit();
					tempDebitTotal = tempDebitTotal + obj.getDebit();
					if (rs.getString("debitFlag").equalsIgnoreCase("Y")) {
						obj.setAmount(rs.getDouble("debit"));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					} else {
						if (rs.getDouble("credit") > 0)
							obj.setAmount(-(rs.getDouble("credit")));
						else
							obj.setAmount(+(rs.getDouble("credit")));

						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					}
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);

				} else if (tempCustomerKey != 0
						&& tempCustomerKey != rs.getInt("cust_key")) {
					objTotal = new CutomerSummaryReport();
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);
					objTotal.setEnityName(customerName);
					objTotal.setTxnType("Total");
					if(hideZero)
					{
						if(objTotal.getBalance()!=0)
							lst.add(objTotal);
					}
					else
					{
						lst.add(objTotal);
					}
					//lst.add(objTotal);
					tempTotal = 0;
					tempCreditTotal = 0;
					tempDebitTotal = 0;
					tempCreditTotal = rs.getDouble("credit");
					tempDebitTotal = rs.getDouble("debit");
					tempBalance = 0;
					customerName = "";
					customerName = rs.getString("fullName") == null ? "" : rs
							.getString("fullName");
					if (rs.getString("debitFlag").equalsIgnoreCase("Y")) {
						obj.setAmount(rs.getDouble("debit"));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					} else {
						if (rs.getDouble("credit") > 0)
							obj.setAmount(-(rs.getDouble("credit")));
						else
							obj.setAmount(+(rs.getDouble("credit")));

						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					}
					tempCustomerKey = rs.getInt("cust_key");
				}
				// lst.add(obj);
				hasNext = rs.next();
				if (!hasNext)
				{
					if(hideZero)
					{
						if(objTotal.getBalance()!=0)
							lst.add(objTotal);
					}
					else
					{
						lst.add(objTotal);
					}
					
				}

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCutomerSummaryReport-->", ex);
		}

		return lst;

	}

	public List<BalanceSheetReportModel> getBalanceSheetReoprt() {
		List<BalanceSheetReportModel> lst = new ArrayList<BalanceSheetReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getBalanceSheetReoprt());
			while (rs.next()) {
				BalanceSheetReportModel obj = new BalanceSheetReportModel();
				obj.setRecNo(rs.getInt("rec_no"));
				obj.setActName(rs.getString("act_name") == null ? "" : rs
						.getString("act_name"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setRowType(rs.getString("row_type") == null ? "" : rs
						.getString("row_type"));
				obj.setActTYpe(rs.getString("act_type") == null ? "" : rs
						.getString("act_type"));
				obj.setActKey(rs.getInt("act_key"));
				obj.setReportName(rs.getString("report_Name") == null ? "" : rs
						.getString("report_Name"));
				String sublevelNew = rs.getString("subLevel") == null ? "0"
						: rs.getString("subLevel");
				if (sublevelNew.equalsIgnoreCase("0"))
					obj.setSubLevel(0);
				else
					obj.setSubLevel(Integer.parseInt(sublevelNew));
				obj.setAccountNameORG(rs.getString("accountNameorg") == null ? ""
						: rs.getString("accountNameorg"));
				obj.setAccountType(rs.getString("accountType") == null ? ""
						: rs.getString("accountType"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setDescription(rs.getString("description") == null ? ""
						: rs.getString("description"));
				if (obj.getActName() != null
						&& !obj.getActName().equalsIgnoreCase("")
						&& obj.getActName().startsWith("Total")) {
					obj.setIstotal(1);
				}
				if (obj.getActName() != null
						&& !obj.getActName().equalsIgnoreCase("")
						&& obj.getActName().startsWith("TOTAL")) {
					obj.setIstotalSummary(1);
				}
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getBalanceSheetReoprt-->", ex);
		}

		return lst;

	}

	public List<CutomerSummaryReport> getCutomerDeatiledReport(Date toDate,
			int customerID, String isActive, boolean inculdeOtherTransactions) {
		List<CutomerSummaryReport> lst = new ArrayList<CutomerSummaryReport>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		double tempBalance = 0;
		double tempTotal = 0;
		double tempCreditTotal = 0;
		double tempDebitTotal = 0;
		int tempCustomerKey = 0;
		String customerName = "";
		CutomerSummaryReport objTotal = new CutomerSummaryReport();
		try {
			rs = db.executeNonQuery(query.getCutomerDetailedReport(toDate,
					customerID, isActive, inculdeOtherTransactions));
			boolean hasNext = rs.next();
			while (hasNext) {
				CutomerSummaryReport obj = new CutomerSummaryReport();
				obj.setRec_no(rs.getInt("recno"));
				obj.setCustKey(rs.getInt("cust_key"));
				obj.setEnityName(rs.getString("fullName") == null ? "" : rs
						.getString("fullName"));
				// customerName=obj.getEnityName();
				obj.setFromDate(rs.getDate("txnDate"));
				if (rs.getDouble("debit") < 0)
					obj.setDebit(-(rs.getDouble("debit")));
				else
					obj.setDebit(rs.getDouble("debit"));

				if (rs.getDouble("credit") < 0)
					obj.setCredit(-(rs.getDouble("credit")));
				else
					obj.setCredit(rs.getDouble("credit"));
				obj.setRefranceNumber(rs.getString("recNO") == null ? "" : rs
						.getString("recNO"));
				obj.setAcountName(rs.getString("itemOrAccountName") == null ? ""
						: rs.getString("itemOrAccountName"));
				obj.setTxnType(rs.getString("trasType") == null ? "" : rs
						.getString("trasType"));
				obj.setTxnDate(rs.getDate("txndate") == null ? ""
						: new SimpleDateFormat("dd-MM-yyyy").format(rs
								.getDate("txndate")));
				if (rs.getString("trasType").equalsIgnoreCase("J")) {
					obj.setTxnType("Jouneral Voucher");
				} else if (rs.getString("trasType").equalsIgnoreCase("R")) {
					obj.setTxnType("Receipt Voucher");
				}

				// total amount calculation
				if (tempCustomerKey == 0) {
					tempCustomerKey = rs.getInt("cust_key");
				}
				if (tempCustomerKey != 0
						&& tempCustomerKey == rs.getInt("cust_key")) {
					objTotal = new CutomerSummaryReport();
					objTotal.setEnityName(obj.getEnityName());
					objTotal.setTxnType("Total");
					customerName = obj.getEnityName();
					tempTotal = tempTotal + obj.getAmount();
					tempCreditTotal = tempCreditTotal + obj.getCredit();
					tempDebitTotal = tempDebitTotal + obj.getDebit();
					if (rs.getString("debitFlag").equalsIgnoreCase("Y")) {
						obj.setAmount(rs.getDouble("debit"));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					} else {
						if (rs.getDouble("credit") > 0)
							obj.setAmount(-(rs.getDouble("credit")));
						else
							obj.setAmount(+(rs.getDouble("credit")));

						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					}
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);

				} else if (tempCustomerKey != 0
						&& tempCustomerKey != rs.getInt("cust_key")) {

					objTotal = new CutomerSummaryReport();
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);
					objTotal.setEnityName(customerName);
					objTotal.setTxnType("Total");
					lst.add(objTotal);
					tempTotal = 0;
					tempCreditTotal = 0;
					tempDebitTotal = 0;
					tempCreditTotal = rs.getDouble("credit");
					tempDebitTotal = rs.getDouble("debit");
					tempBalance = 0;
					customerName = "";
					customerName = rs.getString("fullName") == null ? "" : rs
							.getString("fullName");
					if (rs.getString("debitFlag").equalsIgnoreCase("Y")) {
						obj.setAmount(rs.getDouble("debit"));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					} else {
						if (rs.getDouble("credit") > 0)
							obj.setAmount(-(rs.getDouble("credit")));
						else
							obj.setAmount(+(rs.getDouble("credit")));

						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					}
					tempCustomerKey = rs.getInt("cust_key");
				}
				lst.add(obj);
				hasNext = rs.next();
				if (!hasNext) {
					lst.add(objTotal);
				}

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCutomerSummaryReport-->", ex);
		}

		return lst;

	}

	public List<CashInvoiceSalesReportModel> getCashInvoiceSalesReport(
			Date fromDate, Date toDate, int webUserID,boolean seeTrasction) {
		List<CashInvoiceSalesReportModel> lst = new ArrayList<CashInvoiceSalesReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		double totalSale = 0;
		try {
			rs = db.executeNonQuery(query.getCashInvoiceSalesReport(fromDate,
					toDate, webUserID,seeTrasction));
			while (rs.next()) {
				CashInvoiceSalesReportModel obj = new CashInvoiceSalesReportModel();
				obj.setInvoiceNumber(rs.getString("invoiceno") == null ? ""
						: rs.getString("invoiceno"));
				obj.setInvoiceDateStr(new SimpleDateFormat("dd-MM-yyyy")
				.format(rs.getDate("invoiceDate")));
				obj.setCustomerName(rs.getString("customerName") == null ? ""
						: rs.getString("customerName"));
				obj.setDepositeTo(rs.getString("depositaccount") == null ? ""
						: rs.getString("depositaccount"));
				obj.setCheckNo(rs.getString("checkNo") == null ? "" : rs
						.getString("checkNo"));
				obj.setInvoiceAmount(rs.getDouble("invamount"));
				obj.setPaymentType(rs.getString("paymentType") == null ? ""
						: rs.getString("paymentType"));
				obj.setSalesRep(rs.getString("salesrepname") == null ? "" : rs
						.getString("salesrepname"));
				obj.setRecNO(rs.getInt("recNo"));
				obj.setWebuserId(rs.getInt("webUserID"));
				totalSale = totalSale + obj.getInvoiceAmount();
				obj.setTotalSales(totalSale);
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCashInvoiceSalesReport-->", ex);
		}

		return lst;

	}

	@SuppressWarnings("unused")
	public List<CashInvoiceSalesReportModel> getCreditInvoiceReport(
			Date fromDate, Date toDate, int webUserID) {
		List<CashInvoiceSalesReportModel> lst = new ArrayList<CashInvoiceSalesReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		double totalSale = 0;
		double paidAmount = 0;
		try {
			rs = db.executeNonQuery(query.getCreditInvoiceReport(fromDate,
					toDate, webUserID));
			while (rs.next()) {
				CashInvoiceSalesReportModel obj = new CashInvoiceSalesReportModel();
				obj.setCustomerName(rs.getString("customerName") == null ? ""
						: rs.getString("customerName"));
				obj.setDepositeTo(rs.getString("depositaccount") == null ? ""
						: rs.getString("depositaccount"));
				obj.setSalesRep(rs.getString("salesrepname") == null ? "" : rs
						.getString("salesrepname"));
				obj.setInvoiceNumber(rs.getString("invoiceno") == null ? ""
						: rs.getString("invoiceno"));
				obj.setInvoiceDateStr(new SimpleDateFormat("dd-MM-yyyy")
				.format(rs.getDate("invoiceDate")));
				obj.setDueDate(new SimpleDateFormat("dd-MM-yyyy").format(rs
						.getDate("invoicedue")));
				obj.setTremName(rs.getString("termName") == null ? "" : rs
						.getString("termName"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setInvoiceAmount(rs.getDouble("invamount"));
				obj.setPaidAmount(rs.getDouble("paidAmount"));
				obj.setInvoiceSource(rs.getString("invoice_Source") == null ? ""
						: rs.getString("invoice_Source"));
				obj.setNotes(rs.getString("notes") == null ? "" : rs
						.getString("notes"));
				obj.setRecNO(rs.getInt("recNo"));
				obj.setWebuserId(rs.getInt("webUserID"));
				if (obj.getStatus().equalsIgnoreCase("C")) {
					obj.setStatus("Created");
				} else if (obj.getStatus().equalsIgnoreCase("P")) {
					obj.setStatus("Paid");
				}
				obj.setUnpaidPaidAmount(obj.getInvoiceAmount()
						- obj.getPaidAmount());
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCreditInvoiceReport-->", ex);
		}

		return lst;

	}

	public List<String> getLstBarcodes(String itemKey) {
		List<String> lstBarcode = new ArrayList<String>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getLstBarcode(itemKey));

			while (rs.next()) {
				lstBarcode.add(rs.getString("Barcode"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getLstBarcodes-->", ex);
		}

		return lstBarcode;
	}

	public CashInvoiceGridData getSelectedBCItem(String barcode) {
		CashInvoiceGridData cashInvoiceGridData = null;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;

		try {
			rs = db.executeNonQuery(query.getSelectedBCItem(barcode));

			while (rs.next()) {
				cashInvoiceGridData = new CashInvoiceGridData();

				cashInvoiceGridData.setRecNo(rs.getInt("Item_Key"));
				cashInvoiceGridData.setInvoiceDescription(rs
						.getString("SalesDesc"));
				cashInvoiceGridData.setInvoicearabicDescription(rs
						.getString("DescriptionAR"));
				Double qtyOnhand = rs.getDouble("QuantityOnHand");
				cashInvoiceGridData.setInvoiceQtyOnHand(qtyOnhand.intValue());
				cashInvoiceGridData.setInvoiceRate(rs.getDouble("SalesPrice"));
				cashInvoiceGridData.setItemType(rs.getString("ItemType"));
				cashInvoiceGridData.setAvgCost(rs.getDouble("averagecost"));
				cashInvoiceGridData.setBarcode(rs.getString("BARCODE"));
			}

		} catch (Exception ex) {
			logger.error("error in HBAData---getLstBarcodes-->", ex);
		}

		return cashInvoiceGridData;
	}

	public List<AccountsModel> getAccountsForCreditInvoice() {
		List<AccountsModel> lst = new ArrayList<AccountsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getAccountsForCreditInvoice());
			while (rs.next()) {
				AccountsModel obj = new AccountsModel();
				// obj.setsRL_No(rs.getInt("SRL_No"));
				obj.setAccountName(rs.getString("name"));
				obj.setAccountType(rs.getString("AccountType"));
				// obj.setaCTLEVELSwithNO(rs.getString("ACTLEVELSwithNO"));
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				// obj.setFullName(rs.getString("FullName"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getAccountsForCreditInvoice-->",
					ex);
		}
		return lst;
	}

	public List<TermModel> getTermsForCreditInvoice() {
		List<TermModel> lst = new ArrayList<TermModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getTermsForCreditInvoice());
			while (rs.next()) {
				TermModel obj = new TermModel();
				obj.setTermKey(rs.getInt("term_key"));
				obj.setName(rs.getString("name"));
				obj.setNumberOfDays(rs.getInt("noofdays"));
				obj.setStatus(rs.getString("isActive"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getTermsForCreditInvoice-->", ex);
		}
		return lst;
	}

	public CashInvoiceModel getLocalBalnaceForCreditInvoice(int custKey) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getLocalBalnaceForCreditInvoice(custKey));
			while (rs.next()) {
				obj.setInvoiceAmount(rs.getDouble("InvAmount"));
				obj.setPaidAmount(rs.getDouble("PaidAmount "));
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---getLocalBalnaceForCreditInvoice-->", ex);
		}
		return obj;
	}

	public CashInvoiceModel getCashInvoiceByID(int cashInvoiceId, int webUserID,boolean seeTrasction) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCashInvoiceByID(cashInvoiceId,
					webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setDepositAccountRefKey(rs.getInt("depositAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? ""
						: rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? ""
						: rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? ""
						: rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? ""
						: rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? ""
						: rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? ""
						: rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? ""
						: rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs
						.getString("billaddressPostalCode") == null ? "" : rs
								.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? ""
						: rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? ""
						: rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? ""
						: rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? ""
						: rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? ""
						: rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? ""
						: rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? ""
						: rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? ""
						: rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? ""
						: rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs
						.getString("shipaddressPostalCode") == null ? "" : rs
								.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? ""
						: rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? ""
						: rs.getString("shipaddressNote"));
				obj.setCheckDate(rs.getDate("checkDate"));
				obj.setCheckNo(rs.getString("checkNO") == null ? "" : rs
						.getString("checkNO"));
				obj.setPaymentMethodRefKey(rs.getInt("paymentMethodRefKey"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setBankKey(rs.getInt("bankKey"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? ""
						: rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? ""
						: rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? ""
						: rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs
						.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? ""
						: rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? ""
						: rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? ""
						: rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? ""
						: rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? ""
						: rs.getString("customField5"));
				obj.setQuotationRecNo(rs.getInt("quotationRecNO"));
				obj.setGradeId(rs.getInt("gradeID"));
				obj.setInvoiceType(rs.getString("InvoiceType") == null ? ""
						: rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? ""
						: rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs
						.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs
						.getString("rateHide"));
				obj.setClassHIDE(rs.getString("classHide") == null ? "" : rs
						.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setCommCreated(rs.getString("comm_Created") == null ? ""
						: rs.getString("comm_Created"));
				obj.setTransformD(rs.getString("transformD") == null ? ""
						: rs.getString("transformD"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCashInvoiceByID-->", ex);
		}
		return obj;
	}

	public List<CashInvoiceGridData> getCashInvoiceGridDataByID(
			int cashInvoiceId) {
		List<CashInvoiceGridData> lst = new ArrayList<CashInvoiceGridData>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCashInvoiceGridDataByID(cashInvoiceId));
			while (rs.next()) {
				CashInvoiceGridData obj = new CashInvoiceGridData();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setItemRefKey(rs.getInt("itemrefKey"));
				obj.setInvoiceDescription(rs.getString("description"));
				obj.setInvoiceQty((int) rs.getDouble("quantity"));
				obj.setInvoiceRate(rs.getDouble("rate"));
				obj.setAvgCost(rs.getDouble("avgCost"));
				obj.setRatePercent(rs.getDouble("ratePercent"));
				obj.setPriceLevelRefKey(rs.getInt("priceLevelRefKey"));
				obj.setSelectedClass(rs.getInt("classrefKey"));
				obj.setInvoiceAmmount(rs.getDouble("amount"));
				obj.setServiceDate(rs.getDate("serviceDate"));
				obj.setSalesTaxCodeRefKey(rs.getInt("salestaxcoderefkey"));
				obj.setIsTaxable(rs.getString("isTaxable"));
				obj.setOverrideItemAccountRefKey(rs.getInt("overrideItemAccountRefKey"));
				obj.setOther1(rs.getString("other1") == null ? "" : rs.getString("other1"));
				obj.setOther2(rs.getString("other2") == null ? "" : rs.getString("other2"));
				obj.setQuotationLineNo(rs.getInt("quotationlineNo"));
				obj.setInventorySiteKey(rs.getInt("inventorySiteKey"));
				obj.setInvoicearabicDescription(rs.getString("descriptionAr") == null ? "": rs.getString("descriptionAr"));
				obj.setBarcode(rs.getString("barcode") == null ? "" : rs.getString("barcode"));
				obj.setDeliverRecNo(rs.getInt("DeliveryRecNo"));
				obj.setDeliveryLineNo(rs.getInt("DeliveryLineNo"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCashInvoiceGridDataByID-->", ex);
		}
		return lst;
	}

	public List<MenuModel> getMenuRoles(int companyRoleId, int parentID) {
		List<MenuModel> lstRoles = new ArrayList<MenuModel>();
		try {
			CompanyData compData = new CompanyData();
			lstRoles = compData.getRoleCredentials(companyRoleId, parentID);

		} catch (Exception ex) {
			logger.error("error in HBAData---getTimeSheetRoles-->", ex);
		}
		return lstRoles;
	}

	public CashInvoiceModel navigationCashInvoice(int cashInvoiceId,
			int webUserID,boolean seeTrasction, String navigation, String actionTYpe) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordCashInvoice(
						cashInvoiceId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordCashInvoice(
						cashInvoiceId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getFirstRecordCashInvoice(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getLastRecordCashInvoice(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setDepositAccountRefKey(rs.getInt("depositAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? ""
						: rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? ""
						: rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? ""
						: rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? ""
						: rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? ""
						: rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? ""
						: rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? ""
						: rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs
						.getString("billaddressPostalCode") == null ? "" : rs
								.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? ""
						: rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? ""
						: rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? ""
						: rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? ""
						: rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? ""
						: rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? ""
						: rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? ""
						: rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? ""
						: rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? ""
						: rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs
						.getString("shipaddressPostalCode") == null ? "" : rs
								.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? ""
						: rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? ""
						: rs.getString("shipaddressNote"));
				obj.setCheckDate(rs.getDate("checkDate"));
				obj.setCheckNo(rs.getString("checkNO") == null ? "" : rs
						.getString("checkNO"));
				obj.setInvoiceCheckNo(rs.getString("checkNO") == null ? "" : rs
						.getString("checkNO"));
				obj.setPaymentMethodRefKey(rs.getInt("paymentMethodRefKey"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setBankKey(rs.getInt("bankKey"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? ""
						: rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? ""
						: rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? ""
						: rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs
						.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? ""
						: rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? ""
						: rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? ""
						: rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? ""
						: rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? ""
						: rs.getString("customField5"));
				obj.setQuotationRecNo(rs.getInt("quotationRecNO"));
				obj.setGradeId(rs.getInt("gradeID"));
				obj.setInvoiceType(rs.getString("InvoiceType") == null ? ""
						: rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? ""
						: rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs
						.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs
						.getString("rateHide"));
				obj.setClassHIDE(rs.getString("classHide") == null ? "" : rs
						.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setCommCreated(rs.getString("comm_Created") == null ? ""
						: rs.getString("comm_Created"));
				obj.setTransformD(rs.getString("transformD") == null ? ""
						: rs.getString("transformD"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationCashInvoice-->", ex);
		}
		return obj;
	}

	public CashInvoiceModel getCreditInvoiceById(int cashInvoiceId,
			int webUserID,boolean seeTrasction) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCreditInvoiceByID(cashInvoiceId,
					webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				// obj.setDepositAccountRefKey(rs.getInt("depositAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? ""
						: rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? ""
						: rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? ""
						: rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? ""
						: rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? ""
						: rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? ""
						: rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? ""
						: rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs
						.getString("billaddressPostalCode") == null ? "" : rs
								.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? ""
						: rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? ""
						: rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? ""
						: rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? ""
						: rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? ""
						: rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? ""
						: rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? ""
						: rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? ""
						: rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? ""
						: rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs
						.getString("shipaddressPostalCode") == null ? "" : rs
								.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? ""
						: rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? ""
						: rs.getString("shipaddressNote"));
				// obj.setCheckDate(rs.getDate("checkDate"));
				// obj.setCheckNo(rs.getString("checkNO")==null?"":rs.getString("checkNO"));
				// obj.setPaymentMethodRefKey(rs.getInt("paymentMethodRefKey"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				// obj.setBankKey(rs.getInt("bankKey"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? ""
						: rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? ""
						: rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? ""
						: rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs
						.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? ""
						: rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? ""
						: rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? ""
						: rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? ""
						: rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? ""
						: rs.getString("customField5"));
				obj.setQuotationRecNo(rs.getInt("quotationRecNO"));
				obj.setGradeId(rs.getInt("gradeID"));
				obj.setInvoiceType(rs.getString("InvoiceType") == null ? ""
						: rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? ""
						: rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs
						.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs
						.getString("rateHide"));
				obj.setClassHIDE(rs.getString("classHide") == null ? "" : rs
						.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setCommCreated(rs.getString("comm_Created") == null ? ""
						: rs.getString("comm_Created"));
				obj.setAccountRefKey(rs.getInt("ARAccountRefKey"));
				obj.setTermRefKey(rs.getInt("termsRefKey"));
				obj.setPoNumber(rs.getString("ponumber"));
				obj.setTransformD(rs.getString("transformD") == null ? ""
						: rs.getString("transformD"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCreditInvoiceById-->", ex);
		}
		return obj;
	}

	public List<CashInvoiceGridData> getCreditInvoiceGridDataByID(
			int cashInvoiceId) {
		List<CashInvoiceGridData> lst = new ArrayList<CashInvoiceGridData>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCreditInvoiceGridDataByID(cashInvoiceId));
			while (rs.next()) {
				CashInvoiceGridData obj = new CashInvoiceGridData();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setItemRefKey(rs.getInt("itemrefKey"));
				obj.setInvoiceDescription(rs.getString("description"));
				obj.setInvoiceQty((int) rs.getDouble("quantity"));
				obj.setInvoiceRate(rs.getDouble("rate"));
				obj.setAvgCost(rs.getDouble("avgCost"));
				obj.setRatePercent(rs.getDouble("ratePercent"));
				obj.setPriceLevelRefKey(rs.getInt("priceLevelRefKey"));
				obj.setSelectedClass(rs.getInt("classrefKey"));
				obj.setInvoiceAmmount(rs.getDouble("amount"));
				obj.setServiceDate(rs.getDate("serviceDate"));
				obj.setSalesTaxCodeRefKey(rs.getInt("salestaxcoderefkey"));
				obj.setIsTaxable(rs.getString("isTaxable"));
				obj.setOverrideItemAccountRefKey(rs.getInt("overrideItemAccountRefKey"));
				obj.setOther1(rs.getString("other1") == null ? "" : rs.getString("other1"));
				obj.setOther2(rs.getString("other2") == null ? "" : rs.getString("other2"));
				obj.setQuotationLineNo(rs.getInt("quotationlineNo"));
				obj.setInventorySiteKey(rs.getInt("inventorySiteKey"));
				obj.setInvoicearabicDescription(rs.getString("descriptionAr") == null ? "" : rs.getString("descriptionAr"));
				obj.setDeliverRecNo(rs.getInt("DeliveryRecNo"));
				obj.setDeliveryLineNo(rs.getInt("DeliveryLineNo"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCreditInvoiceGridDataByID-->",
					ex);
		}
		return lst;
	}

	public CashInvoiceModel navigationCreditInvoiceInvoice(int cashInvoiceId,
			int webUserID,boolean seeTrasction, String navigation, String actionTYpe) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordCreditInvoice(
						cashInvoiceId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordCreditInvoice(
						cashInvoiceId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getFirstRecordCreditInvoice(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getLastRecordCreditInvoice(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setAccountRefKey(rs.getInt("ARAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? ""
						: rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? ""
						: rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? ""
						: rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? ""
						: rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? ""
						: rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? ""
						: rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? ""
						: rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs
						.getString("billaddressPostalCode") == null ? "" : rs
								.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? ""
						: rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? ""
						: rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? ""
						: rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? ""
						: rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? ""
						: rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? ""
						: rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? ""
						: rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? ""
						: rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? ""
						: rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs
						.getString("shipaddressPostalCode") == null ? "" : rs
								.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? ""
						: rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? ""
						: rs.getString("shipaddressNote"));
				// obj.setCheckDate(rs.getDate("checkDate"));
				// /
				// obj.setCheckNo(rs.getString("checkNO")==null?"":rs.getString("checkNO"));
				// obj.setPaymentMethodRefKey(rs.getInt("paymentMethodRefKey"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setTermRefKey(rs.getInt("termsRefKey"));
				// obj.setBankKey(rs.getInt("bankKey"));
				obj.setPoNumber(rs.getString("ponumber"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? ""
						: rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? ""
						: rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? ""
						: rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs
						.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? ""
						: rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? ""
						: rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? ""
						: rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? ""
						: rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? ""
						: rs.getString("customField5"));
				obj.setQuotationRecNo(rs.getInt("quotationRecNO"));
				obj.setGradeId(rs.getInt("gradeID"));
				obj.setInvoiceType(rs.getString("InvoiceType") == null ? ""
						: rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? ""
						: rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs
						.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs
						.getString("rateHide"));
				obj.setClassHIDE(rs.getString("classHide") == null ? "" : rs
						.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setCommCreated(rs.getString("comm_Created") == null ? ""
						: rs.getString("comm_Created"));
				obj.setTransformD(rs.getString("transformD") == null ? ""
						: rs.getString("transformD"));

			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---navigationCreditInvoiceInvoice-->", ex);
		}
		return obj;
	}

	public String getImageData(String ComapnName) {
		HBAQueries query = new HBAQueries();
		FileOutputStream fos = null;
		String path = "";
		try {
			ResultSet rs = db.executeNonQuery(query.getLogoImage());
			String repository = System.getProperty("catalina.base")
					+ File.separator + "uploads" + File.separator + "";
			File targetFile = new File(repository + "CompanyLogo"
					+ File.separator + ComapnName.trim() + "");

			targetFile.setExecutable(true, false);
			targetFile.setReadable(true, false);
			targetFile.setWritable(true, false);
			// using PosixFilePermission to set file permissions 777
			targetFile.mkdirs();
			File newFile = new File(targetFile.getAbsolutePath(), "logo.jpg");
			newFile.setExecutable(true, false);
			newFile.setReadable(true, false);
			newFile.setWritable(true, false);

			newFile.canWrite();
			newFile.createNewFile();
			path = newFile.getAbsolutePath();
			
			//i add this for local i didnt have the logo binary saved in sql table
			if(newFile.exists())
				return path;
			
			File parent = newFile.getAbsoluteFile();
			if (!parent.exists() && !parent.mkdirs()) {
				throw new IllegalStateException("Couldn't create dir: "
						+ parent);
			}
			while (rs.next()) {
				byte[] b = rs.getBytes("logo1");
				fos = new FileOutputStream(new File(path));
				fos.write(b);
				fos.flush();
				fos.close();
			}
			
			logger.info("logoPath >>> " + path);			

		} catch (Exception e) {
			logger.error("error in HBAData---getImageData-->", e);
		}
		return path;
	}

	// /Quotation HBA module
	public List<QbListsModel> quotationPrecpectiveList() {

		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		QbListsModel obj1 = new QbListsModel();
		obj1.setRecNo(0);
		obj1.setName("Select");
		obj1.setFullName("Select");
		obj1.setSubLevel(0);
		obj1.setIsActive("None");
		obj1.setListType("None");
		lst.add(obj1);

		try {
			rs = db.executeNonQuery(query.quotationPrecpectiveList());
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setName(rs.getString("name"));
				obj.setFullName(rs.getString("fullname"));
				obj.setSubLevel(rs.getInt("sublevel"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---quotationPrecpectiveList-->", ex);
		}
		return lst;

	}

	public List<QbListsModel> quotationCustomerList() {

		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.quotationCustomerList());
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setName(rs.getString("name"));
				obj.setListType(rs.getString("listType"));
				obj.setListID(rs.getString("listId"));
				obj.setSubLevel(rs.getInt("sublevel"));
				obj.setFullName(rs.getString("fullName"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---quotationCustomerList-->", ex);
		}
		return lst;

	}

	public int saveQuotation(CashInvoiceModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.saveQuotation(obj, webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---saveQuotation-->", ex);
		}
		return result;
	}

	public int updateQuotation(CashInvoiceModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.updateQuotation(obj, webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateQuotation-->", ex);
		}
		return result;
	}

	public int addNewQuotationGridItemsQuery(CashInvoiceGridData obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewQuotationGridItemsQuery(
					obj, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewQuotationGridItemsQuery-->",
					ex);
		}
		return result;

	}

	public int deleteQuotationGridItems(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteQuotationGridItems(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteQuotationGridItems-->", ex);
		}
		return result;

	}

	public CashInvoiceModel navigationQuotation(int cashInvoiceId,
			int webUserID,boolean seeTrasction, String navigation, String actionTYpe) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordQuatation(
						cashInvoiceId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordQuatation(
						cashInvoiceId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getFirstRecordQuatation(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getLastRecordQuatation(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setAccountRefKey(rs.getInt("ARAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setClientType(rs.getString("clientType") == null ? "" : rs
						.getString("clientType"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? ""
						: rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? ""
						: rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? ""
						: rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? ""
						: rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? ""
						: rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? ""
						: rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? ""
						: rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs
						.getString("billaddressPostalCode") == null ? "" : rs
								.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? ""
						: rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? ""
						: rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? ""
						: rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? ""
						: rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? ""
						: rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? ""
						: rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? ""
						: rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? ""
						: rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? ""
						: rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs
						.getString("shipaddressPostalCode") == null ? "" : rs
								.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? ""
						: rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? ""
						: rs.getString("shipaddressNote"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setTermRefKey(rs.getInt("termsRefKey"));
				obj.setPoNumber(rs.getString("ponumber"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? ""
						: rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? ""
						: rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? ""
						: rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs
						.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? ""
						: rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? ""
						: rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? ""
						: rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? ""
						: rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? ""
						: rs.getString("customField5"));
				// obj.setInvoiceType(rs.getString("InvoiceType")==null?"":rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setStatusDesc(rs.getString("statusDesc") == null ? "" : rs
						.getString("statusDesc"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? ""
						: rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs
						.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs
						.getString("rateHide"));
				// obj.setClassHIDE(rs.getString("classHide")==null?"":rs.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setRemindMeDate(rs.getDate("RemindDate"));
				obj.setRemindMedays(rs.getInt("RemindDays"));
				obj.setRemingMeFalg(rs.getString("RemindFlag") == null ? ""
						: rs.getString("RemindFlag"));
				obj.setAttachemnet(rs.getString("Attachment") == null ? "" : rs
						.getString("Attachment"));
				obj.setLetterTemplate(rs.getString("LetterTemplate") == null ? ""
						: rs.getString("LetterTemplate"));
				obj.setShipToAddress(rs.getString("ShipToAddress") == null ? ""
						: rs.getString("ShipToAddress"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationQuotation-->", ex);
		}
		return obj;
	}

	public CashInvoiceModel getQuatationByID(int cashInvoiceId, int webUserID,boolean seeTrasction) {
		CashInvoiceModel obj = new CashInvoiceModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getQuatationByID(cashInvoiceId,
					webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setAccountRefKey(rs.getInt("ARAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setClientType(rs.getString("clientType") == null ? "" : rs.getString("clientType"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? "" : rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? "" : rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? "" : rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? "" : rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? "" : rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? "" : rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? "" : rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs.getString("billaddressPostalCode") == null ? "" : rs.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? "" : rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? "" : rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? "" : rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? "" : rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? "" : rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? "" : rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? "" : rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? "" : rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? "" : rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs.getString("shipaddressPostalCode") == null ? "" : rs.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? "": rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? "" : rs.getString("shipaddressNote"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setTermRefKey(rs.getInt("termsRefKey"));
				obj.setPoNumber(rs.getString("ponumber"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? "": rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? "": rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? "": rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? "": rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? "": rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? "": rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? "": rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? "": rs.getString("customField5"));
				// obj.setInvoiceType(rs.getString("InvoiceType")==null?"":rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				obj.setStatusDesc(rs.getString("statusDesc") == null ? "" : rs.getString("statusDesc"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? "": rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs.getString("rateHide"));
				// obj.setClassHIDE(rs.getString("classHide")==null?"":rs.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setRemindMeDate(rs.getDate("RemindDate"));
				obj.setRemindMedays(rs.getInt("RemindDays"));
				obj.setRemingMeFalg(rs.getString("RemindFlag") == null ? "": rs.getString("RemindFlag"));
				obj.setAttachemnet(rs.getString("Attachment") == null ? "" : rs.getString("Attachment"));
				obj.setLetterTemplate(rs.getString("LetterTemplate") == null ? "": rs.getString("LetterTemplate"));
				obj.setShipToAddress(rs.getString("ShipToAddress") == null ? "": rs.getString("ShipToAddress"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQuatationByID-->", ex);
		}
		return obj;
	}

	public List<CashInvoiceGridData> getQuotationridDataByID(int cashInvoiceId) {
		List<CashInvoiceGridData> lst = new ArrayList<CashInvoiceGridData>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getQuatationGridDataByID(cashInvoiceId));
			while (rs.next()) {
				CashInvoiceGridData obj = new CashInvoiceGridData();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setItemRefKey(rs.getInt("itemrefKey"));
				obj.setInvoiceDescription(rs.getString("description"));
				obj.setInvoiceQty((int) rs.getDouble("quantity"));
				obj.setInvoiceRate(rs.getDouble("rate"));
				obj.setAvgCost(rs.getDouble("avgCost"));
				obj.setRatePercent(rs.getDouble("ratePercent"));
				obj.setPriceLevelRefKey(rs.getInt("priceLevelRefKey"));
				obj.setSelectedClass(rs.getInt("classrefKey"));
				obj.setInvoiceAmmount(rs.getDouble("amount"));
				obj.setServiceDate(rs.getDate("serviceDate"));
				obj.setSalesTaxCodeRefKey(rs.getInt("salestaxcoderefkey"));
				obj.setIsTaxable(rs.getString("isTaxable"));
				obj.setOverrideItemAccountRefKey(rs.getInt("overrideItemAccountRefKey"));
				obj.setOther1(rs.getString("other1") == null ? "" : rs.getString("other1"));
				obj.setOther2(rs.getString("other2") == null ? "" : rs.getString("other2"));
				obj.setInventorySiteKey(rs.getInt("InventorySiteKey"));
				obj.setInvertySiteKey(rs.getInt("InventorySiteKey"));
				obj.setDeliverdAs(rs.getString("DeliverdAs") == null ? "" : rs.getString("DeliverdAs"));
				obj.setDeliverRecNo(rs.getInt("DeliverRecNo"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQuotationridDataByID-->", ex);
		}
		return lst;
	}

	public boolean checkIfSerialNumberIsDuplicateForQuotation(
			String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.checkIfSerialNumberIsDuplicateForQuotation(SerialNumber,
							recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---checkIfSerialNumberIsDuplicateForQuotation-->",
					ex);
		}

		return hasSerialNumber;
	}

	public int GetNewQuotationRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewQuotationRecNo());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewQuotationRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public List<CashInvoiceModel> getQuotationReport(int webUserID,boolean seeTrasction,Date fromDate, Date toDate) {
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		List<CashInvoiceModel> list = new ArrayList<CashInvoiceModel>();
		try {
			rs = db.executeNonQuery(query.getQuotationReport(webUserID,seeTrasction, fromDate, toDate));
			while (rs.next()) {
				CashInvoiceModel obj = new CashInvoiceModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setItemName("");// add this for filter				
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setAccountRefKey(rs.getInt("ARAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setInvoiceDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("txnDate")));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setClientType(rs.getString("clientType") == null ? "" : rs.getString("clientType"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? "": rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? "": rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? "": rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? "": rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? "": rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? "": rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? "": rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs.getString("billaddressPostalCode") == null ? "" : rs.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? "": rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? "": rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? "": rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? "": rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? "": rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? "": rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? "": rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? "": rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? "": rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs.getString("shipaddressPostalCode") == null ? "" : rs.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? "": rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? "": rs.getString("shipaddressNote"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setTermRefKey(rs.getInt("termsRefKey"));
				obj.setPoNumber(rs.getString("ponumber"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? "": rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? "": rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? "": rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? "": rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? "": rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? "": rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? "": rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? "": rs.getString("customField5"));
				obj.setStatus(rs.getString("statusdesc") == null ? "" : rs.getString("statusdesc"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? "": rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide") == null ? "" : rs.getString("rateHide"));
				obj.setSendVia(rs.getString("SendVia") == null ? "" : rs.getString("SendVia"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setRemindMeDate(rs.getDate("RemindDate"));
				obj.setRemindMedays(rs.getInt("RemindDays"));
				obj.setRemingMeFalg(rs.getString("RemindFlag") == null ? "": rs.getString("RemindFlag"));
				obj.setAttachemnet(rs.getString("Attachment") == null ? "" : rs.getString("Attachment"));
				obj.setLetterTemplate(rs.getString("LetterTemplate") == null ? "": rs.getString("LetterTemplate"));
				obj.setShipToAddress(rs.getString("ShipToAddress") == null ? "": rs.getString("ShipToAddress"));
				/*obj.setLineMemo(rs.getString("linememo") == null ? "" : rs.getString("linememo"));
				obj.setQuantity((int) rs.getDouble("quantity"));
				obj.setLineMemo(rs.getString("linememo") == null ? "" : rs.getString("linememo"));
				obj.setRatePercent(rs.getDouble("ratePercent"));
				obj.setAvgCost(rs.getDouble("avgcost"));
				obj.setLineAmount(rs.getDouble("lineAmount"));*/
				obj.setAmount(rs.getDouble("Amount"));
				obj.setLineAmount(obj.getAmount()); //use this for filter
				/*obj.setRate(rs.getDouble("rate"));*/
				if (obj.getClientType().equalsIgnoreCase("P")) {
					obj.setCustomerName(rs.getString("prospectiveName") == null ? "": rs.getString("prospectiveName"));
				} else {
					obj.setCustomerName(rs.getString("entity") == null ? "": rs.getString("entity"));
				}
				/*obj.setItemName(rs.getString("itemName") == null ? "" : rs.getString("itemName"));
				obj.setItemName(rs.getString("description") == null ? "" : rs.getString("description"));*/
				//use this to hide change status in list
				obj.setStatusFlag(rs.getString("Status"));
				if(obj.getStatusFlag().contains("C") || obj.getStatusFlag().contains("H") || obj.getStatusFlag().contains("O"))
				{
					obj.setCanChangeStatus(true);
				}
							
				list.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQuotationReport-->", ex);
		}
		return list;
	}

	// Methods for item Receipt moudle

	public List<AccountsModel> getAccountForExpances() {
		List<AccountsModel> lst = new ArrayList<AccountsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getAccountForExpances());
			while (rs.next()) {
				AccountsModel obj = new AccountsModel();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("Name"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setListID(rs.getString("ListID"));
				obj.setAccountName(rs.getString("AccountName"));
				obj.setAccountType(rs.getString("AccountType"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getAccountForExpances-->", ex);
		}
		return lst;
	}

	public int updateExistingItemReceipt(ItemReceiptModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db
					.executeUpdateQuery(query.updateExistingItemReceipt(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateExistingItemReceipt-->", ex);
		}
		return result;
	}

	public int addNewItemReceipt(ItemReceiptModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewItemReceipt(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewCashPayment-->", ex);
		}
		return result;
	}

	public int addExpenseItemReceipt(ExpensesModel objExpenses, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addExpenseItemReceipt(
					objExpenses, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addExpense-->", ex);
		}
		return result;

	}

	// Expenses
	public int deleteExpenseItemReceipt(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteExpenseItemReceipt(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addExpenseItemReceipt-->", ex);
		}
		return result;

	}

	public int deleteCheckItemsItemReceipt(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteCheckItemsItemReceipt(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteCheckItemsItemReceipt-->",
					ex);
		}
		return result;

	}

	public int addCheckItemsItemReceipt(CheckItemsModel obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addCheckItemsItemReceipt(obj,
					RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCheckItemsItemReceipt-->", ex);
		}
		return result;

	}

	public int GetNewItemReceiptRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewItemReceiptRecNo());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewCheckMastRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public boolean checkIfSerialNumberIsDuplicateForItemReceiptIrNo(
			String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIfSerialNumberIsDuplicateForItemReceiptIrNo(
					SerialNumber, recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---checkIfSerialNumberIsDuplicateForItemReceiptIrNo-->",
					ex);
		}

		return hasSerialNumber;
	}

	public boolean checkIfSerialNumberIsDuplicateForItemReceiptIrNumberLocal(
			String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.checkIfSerialNumberIsDuplicateForItemReceiptIrNumberLocal(
							SerialNumber, recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---checkIfSerialNumberIsDuplicateForItemReceiptIrNumberLocal-->",
					ex);
		}

		return hasSerialNumber;
	}

	public ItemReceiptModel getItemReceiptByID(int itemReceiptKey, int webUserID,boolean seeTrasction) {
		ItemReceiptModel obj = new ItemReceiptModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getItemReceiptByID(itemReceiptKey,
					webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnID(rs.getString("txnId") == null ? "" : rs
						.getString("txnId"));
				obj.setIrNo(rs.getString("irno") == null ? "" : rs
						.getString("irno"));
				obj.setIrNoLocal(rs.getString("irNoLocal") == null ? "" : rs
						.getString("irNoLocal"));
				obj.setIrDate(rs.getDate("IrDate"));
				obj.setAccrefKey(rs.getInt("apaccountKey"));
				obj.setVendorKey(rs.getInt("vendorKey"));
				obj.setPrintName(rs.getString("printName") == null ? "" : rs
						.getString("printName"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setStatusMemo(rs.getString("statusMemo") == null ? "" : rs
						.getString("statusMemo"));
				obj.setQbStatus(rs.getString("qbStatus") == null ? "" : rs
						.getString("qbStatus"));
				obj.setIrsource(rs.getString("irSource") == null ? "" : rs
						.getString("irSource"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setItemClassHide(rs.getString("itemClassHide") == null ? ""
						: rs.getString("itemClassHide"));
				obj.setItemDesHide(rs.getString("itemDesHide") == null ? ""
						: rs.getString("itemDesHide"));
				obj.setBillDateHide(rs.getString("itemBillDateHide") == null ? ""
						: rs.getString("itemBillDateHide"));
				obj.setBillNoHide(rs.getString("itemBillNoHide") == null ? ""
						: rs.getString("itemBillNoHide"));
				obj.setTransformPO(rs.getString("TransformPO") == null ? ""
						: rs.getString("TransformPO"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getItemReceiptByID-->", ex);
		}
		return obj;
	}

	public List<ExpensesModel> getExpenseItemReceiptGridDataByID(
			String itemReceiptKey) {
		List<ExpensesModel> lst = new ArrayList<ExpensesModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getExpenseItemReceiptGridDataByID(itemReceiptKey));
			while (rs.next()) {
				ExpensesModel obj = new ExpensesModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setSrNO(rs.getInt("line_No"));
				obj.setTxnLineId(rs.getString("txnlineId"));
				obj.setSelectedAccountKey(rs.getInt("accountsRef_Key"));
				obj.setSelectedCutomerKey(rs.getInt("custRef_key"));
				obj.setSelectedClassKey(rs.getInt("class_key"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---getExpenseItemReceiptGridDataByID-->",
					ex);
		}
		return lst;
	}

	public List<CheckItemsModel> getCheckItemsItemReceiptGridDataByID(
			int itemReceiptKey) {
		List<CheckItemsModel> lst = new ArrayList<CheckItemsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getCheckItemsItemReceiptGridDataByID(itemReceiptKey));
			while (rs.next()) {
				CheckItemsModel obj = new CheckItemsModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setItemKey(rs.getInt("itemKey"));
				obj.setDescription(rs.getString("description"));
				obj.setBillNo(rs.getString("billNo"));
				obj.setInvoiceDate(rs.getDate("invoiceDate"));
				obj.setQuantity((int) rs.getDouble("quantity"));
				obj.setCost(rs.getDouble("cost"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setSelectedCustKey(rs.getInt("custKey"));
				obj.setSelectedClassKey(rs.getInt("classKey"));
				obj.setInventorySiteKey(rs.getInt("inventorySiteKey"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---getCheckItemsItemReceiptGridDataByID-->",
					ex);
		}
		return lst;
	}

	public ItemReceiptModel navigationItemReceipt(int itemReceiptKey,
			int webUserID,boolean seeTrasction, String navigation, String actionTYpe) {
		ItemReceiptModel obj = new ItemReceiptModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordItemReceipt(
						itemReceiptKey, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe
							.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordItemReceipt(
						itemReceiptKey, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getFirstRecordItemReceipt(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev")
					&& actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query
						.getLastRecordItemReceipt(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnID(rs.getString("txnId") == null ? "" : rs
						.getString("txnId"));
				obj.setIrNo(rs.getString("irno") == null ? "" : rs
						.getString("irno"));
				obj.setIrNoLocal(rs.getString("irNoLocal") == null ? "" : rs
						.getString("irNoLocal"));
				obj.setIrDate(rs.getDate("IrDate"));
				obj.setAccrefKey(rs.getInt("apaccountKey"));
				obj.setVendorKey(rs.getInt("vendorKey"));
				obj.setPrintName(rs.getString("printName") == null ? "" : rs
						.getString("printName"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setStatusMemo(rs.getString("statusMemo") == null ? "" : rs
						.getString("statusMemo"));
				obj.setQbStatus(rs.getString("qbStatus") == null ? "" : rs
						.getString("qbStatus"));
				obj.setIrsource(rs.getString("irSource") == null ? "" : rs
						.getString("irSource"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setItemClassHide(rs.getString("itemClassHide") == null ? ""
						: rs.getString("itemClassHide"));
				obj.setItemDesHide(rs.getString("itemDesHide") == null ? ""
						: rs.getString("itemDesHide"));
				obj.setBillDateHide(rs.getString("itemBillDateHide") == null ? ""
						: rs.getString("itemBillDateHide"));
				obj.setBillNoHide(rs.getString("itemBillNoHide") == null ? ""
						: rs.getString("itemBillNoHide"));
				obj.setTransformPO(rs.getString("TransformPO") == null ? ""
						: rs.getString("TransformPO"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationItemReceipt-->", ex);
		}
		return obj;
	}

	public List<ItemReceiptReportModel> getItemReceiptReport(int webUserId,boolean seeTrasction,Date fromDate, Date toDate) {
		List<ItemReceiptReportModel> lst = new ArrayList<ItemReceiptReportModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getItemReceiptReport(webUserId,seeTrasction,fromDate, toDate));
			while (rs.next()) {
				ItemReceiptReportModel obj = new ItemReceiptReportModel();
				obj.setIrLocalNo(rs.getString("irnoLOcal") == null ? "" : rs.getString("irnoLOcal"));
				obj.setIrDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("irDate")));
				obj.setMastRecoNO(rs.getInt("RecNo"));
				String stat = rs.getString("status") == null ? "" : rs.getString("status");
				if (stat.equalsIgnoreCase("C")) {
					obj.setStatus("Created");
				} else if (stat.equalsIgnoreCase("A")) {
					obj.setStatus("Approved");
				}
				obj.setAccountName(rs.getString("accountName") == null ? "": rs.getString("accountName"));
				//obj.setAccountNumber(rs.getString("accountNumber") == null ? "": rs.getString("accountNumber"));
				obj.setVendor(rs.getString("vendor") == null ? "" : rs.getString("vendor"));
				obj.setVendorKey(rs.getInt("VendorKey"));
			
				//obj.setItemName(rs.getString("itemName") == null ? "" : rs.getString("itemName"));
				//obj.setDescription(rs.getString("description") == null ? "": rs.getString("description"));
				//obj.setQuantity((int) rs.getDouble("quantity"));
				//obj.setLineNUmber(rs.getString("irlineno") == null ? "" : rs.getString("irlineno"));
				//obj.setMainMemo(rs.getString("mainMemo") == null ? "" : rs.getString("mainMemo"));
				//obj.setCustomerName(rs.getString("customer") == null ? "" : rs.getString("customer"));
				//obj.setClassName(rs.getString("className") == null ? "" : rs.getString("className"));
				obj.setAmount(rs.getDouble("amount"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getItemReceiptReport-->", ex);
		}
		return lst;
	}

	// PURCHASE REQUEST CHANGES FROM IQBAL

	public List<QbListsModel> getDropShipTo() {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getDropShipTo());
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ListType"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getDropShipTo-->", ex);
		}
		return lst;
	}

	public List<QbListsModel> getItemForPurchaseRequest() {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getItemForPurchaseRequest());
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setRecNo(rs.getInt("item_key"));
				obj.setName(rs.getString("Name"));
				obj.setListID(rs.getString("ListID"));
				obj.setSubLevel(rs.getInt("SubLevel"));
				obj.setFullName(rs.getString("FullName"));
				obj.setListType(rs.getString("ItemType"));
				obj.setSalesDesc(rs.getString("salesdesc") == null ? "" : rs.getString("salesdesc"));
				obj.setSubItemsCount(rs.getInt("subItemsCount"));
				obj.setPurchaseDesc(rs.getString("PurchaseDesc"));
				obj.setPurchaseCost(rs.getDouble("averageCost"));				
				obj.setSubOfClasskey(rs.getInt("ClassKey"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getItemForPurchaseRequest-->", ex);
		}
		return lst;
	}

	public int updateExistingPurchaseRequest(PurchaseRequestModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.updateExistingPurchaseRequest(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateExistingPurchaseRequest-->",
					ex);
		}
		return result;
	}

	public int addNewPurchaseRqeuest(PurchaseRequestModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewPurchaseRqeuest(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewPurchaseRqeuest-->", ex);
		}
		return result;
	}

	public int addGridDataPurchaseRequest(PurchaseRequestGridData obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addGridDataPurchaseRequest(obj, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addGridDataPurchaseRequest-->", ex);
		}
		return result;

	}

	public int deleteGridDataPurchaseRequest(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteGridDataPurchaseRequest(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteGridDataPurchaseRequest-->",
					ex);
		}
		return result;

	}

	public int GetNewPurchaseRequestRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewPurchaseRequestRecNo());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewPurchaseRequestRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public boolean checkIfSerialNumberIsDuplicateForPurchaseRequest(
			String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.checkIfSerialNumberIsDuplicateForPurchaseRequest(
							SerialNumber, recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---checkIfSerialNumberIsDuplicateForPurchaseRequest-->",
					ex);
		}

		return hasSerialNumber;
	}

	public PurchaseRequestModel getPurchaseRequestByID(int purchaseRequestKey,
			int webUserID,boolean seeTrasction) {
		PurchaseRequestModel obj = new PurchaseRequestModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPurchaseRequestByID(
					purchaseRequestKey, webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(rs.getDate("TxnDate"));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs
						.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs
						.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setSource(rs.getString("Source") == null ? "" : rs
						.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getPurchaseRequestByID-->", ex);
		}
		return obj;
	}

	public List<PurchaseRequestGridData> getGridDataPurchaseRequestById(
			int purchaseRequestKey) {
		List<PurchaseRequestGridData> lst = new ArrayList<PurchaseRequestGridData>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getGridDataPurchaseRequestById(purchaseRequestKey));
			while (rs.next()) {
				PurchaseRequestGridData obj = new PurchaseRequestGridData();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setItemrefKey(rs.getInt("ItemRefKey"));
				obj.setDecription(rs.getString("Description") == null ? "" : rs
						.getString("Description"));
				obj.setIsOrderd(rs.getString("IsOrdered") == null ? "" : rs
						.getString("IsOrdered"));
				obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setLineNo(rs.getInt("Line_No"));
				obj.setRecivedQuantity((int) rs.getDouble("RcvdQuantity"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---getGridDataPurchaseRequestById-->", ex);
		}
		return lst;
	}

	public PurchaseRequestModel navigationPurchaseRequest(
			int purchaseRequestKey, int webUserID,boolean seeTrasction, String navigation,
			String actionTYpe) {
		PurchaseRequestModel obj = new PurchaseRequestModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordPurchaseRequest(purchaseRequestKey, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordPurchaseRequest(purchaseRequestKey, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getFirstRecordPurchaseRequest(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getLastRecordPurchaseRequest(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(rs.getDate("TxnDate"));
				obj.setReqDate(rs.getDate("ReqDate"));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				obj.setSource(rs.getString("Source") == null ? "" : rs.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setIsFullyReceived(rs.getString("IsFullyReceived") == null ? "" : rs.getString("IsFullyReceived"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationPurchaseRequest-->", ex);
		}
		return obj;
	}


	public List<PurchaseRequestReportModel> getPurchaseRequestReport(
			int webUserId,boolean seeTrasction, Date fromDate, Date toDate, String reportFrom,
			String status) {
		List<PurchaseRequestReportModel> lst = new ArrayList<PurchaseRequestReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPurchaseRequestReport(webUserId,seeTrasction,
					fromDate, toDate, reportFrom, status));
			while (rs.next()) {
				PurchaseRequestReportModel obj = new PurchaseRequestReportModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(new SimpleDateFormat("dd-MM-yyyy").format(rs
						.getDate("TxnDate")));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs
						.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs
						.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				if (obj.getStatus().equalsIgnoreCase("C")) {
					obj.setStatus("Created");
				} else if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatus("Approved");
				} else if (obj.getStatus().equalsIgnoreCase("P")) {
					obj.setStatus("Printed");
				} else if (obj.getStatus().equalsIgnoreCase("V")) {
					obj.setStatus("void");
				}
				obj.setSource(rs.getString("Source") == null ? "" : rs
						.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setDecription(rs.getString("itemdesc") == null ? "" : rs
						.getString("itemdesc"));
				obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setRecivedQuantity((int) rs.getDouble("RcvdQuantity"));
				obj.setVendorName(rs.getString("vendorName") == null ? "" : rs
						.getString("vendorName"));
				obj.setDropToName(rs.getString("dropShipTo") == null ? "" : rs
						.getString("dropShipTo"));
				obj.setClassName(rs.getString("className") == null ? "" : rs
						.getString("className"));
				obj.setCustomerName(rs.getString("customerName") == null ? ""
						: rs.getString("customerName"));
				obj.setItemName(rs.getString("itemName") == null ? "" : rs
						.getString("itemName"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getPurchaseRequestReport-->", ex);
		}
		return lst;
	}

	// Purchase Order
	public int updateExistingPurchaseOrder(PurchaseRequestModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.updateExistingPurchaseOrder(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateExistingPurchaseOrder-->",
					ex);
		}
		return result;
	}

	public int addNewPurchaseOrder(PurchaseRequestModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewPurchaseOrder(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewPurchaseOrder-->", ex);
		}
		return result;
	}

	public int addGridDataPurchaseOrder(PurchaseRequestGridData obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addGridDataPurchaseOrder(obj,
					RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addGridDataPurchaseOrder-->", ex);
		}
		return result;

	}

	public int deleteGridDataPurchaseOrder(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.deleteGridDataPurchaseOrder(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteGridDataPurchaseOrder-->",
					ex);
		}
		return result;

	}

	public int GetNewPurchaseOrderRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewPurchaseOrderRecNo());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewPurchaseOrderRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public boolean checkIfSerialNumberIsDuplicateForPurchaseOrder(
			String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.checkIfSerialNumberIsDuplicateForPurchaseOrder(
							SerialNumber, recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---checkIfSerialNumberIsDuplicateForPurchaseOrder-->",
					ex);
		}

		return hasSerialNumber;
	}

	public PurchaseRequestModel getPurchaseOrderByID(int purchaseRequestKey,
			int webUserID,boolean seeTrasction) {
		PurchaseRequestModel obj = new PurchaseRequestModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPurchaseOrderByID(
					purchaseRequestKey, webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(rs.getDate("TxnDate"));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs
						.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs
						.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				obj.setSource(rs.getString("Source") == null ? "" : rs
						.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setTransformMR(rs.getString("TransformMR") == null ? "" : rs
						.getString("TransformMR"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getPurchaseOrderByID-->", ex);
		}
		return obj;
	}

	public List<PurchaseRequestGridData> getGridDataPurchaseOrderById(
			int purchaseRequestKey) {
		List<PurchaseRequestGridData> lst = new ArrayList<PurchaseRequestGridData>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getGridDataPurchaseOrderById(purchaseRequestKey));
			while (rs.next()) {
				PurchaseRequestGridData obj = new PurchaseRequestGridData();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setItemrefKey(rs.getInt("ItemRefKey"));
				obj.setDecription(rs.getString("Description") == null ? "" : rs
						.getString("Description"));
				// obj.setIsOrderd(rs.getString("IsOrdered")==null?"":rs.getString("IsOrdered"));
				obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setLineNo(rs.getInt("Line_No"));
				obj.setRecivedQuantity((int) rs.getDouble("RcvdQuantity"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getGridDataPurchaseOrderById-->",
					ex);
		}
		return lst;
	}

	public PurchaseRequestModel navigationPurchaseOrder(int purchaseRequestKey,int webUserID,boolean seeTrasction, String navigation, String actionTYpe) {
		PurchaseRequestModel obj = new PurchaseRequestModel();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordPurchaseOrder(purchaseRequestKey, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordPurchaseOrder(purchaseRequestKey, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getFirstRecordPurchaseOrder(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getLastRecordPurchaseOrder(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(rs.getDate("TxnDate"));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				obj.setSource(rs.getString("Source") == null ? "" : rs.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setTransformMR(rs.getString("TransformMR") == null ? "" : rs.getString("TransformMR"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationPurchaseOrder-->", ex);
		}
		return obj;
	}

	public List<PurchaseRequestReportModel> getPurchaseRequestOrder(
			int webUserId,boolean seeTrasction, Date fromDate, Date toDate, String reportFrom,
			String status) {
		List<PurchaseRequestReportModel> lst = new ArrayList<PurchaseRequestReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getPurchaseOrderReport(webUserId,seeTrasction,
					fromDate, toDate, reportFrom, status));
			while (rs.next()) {
				PurchaseRequestReportModel obj = new PurchaseRequestReportModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(new SimpleDateFormat("dd-MM-yyyy").format(rs
						.getDate("TxnDate")));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs
						.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs
						.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs
						.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs
						.getString("status"));
				if (obj.getStatus().equalsIgnoreCase("C")) {
					obj.setStatus("Created");
				} else if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatus("Approved");
				} else if (obj.getStatus().equalsIgnoreCase("P")) {
					obj.setStatus("Printed");
				} else if (obj.getStatus().equalsIgnoreCase("V")) {
					obj.setStatus("void");
				}
				obj.setSource(rs.getString("Source") == null ? "" : rs
						.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setDecription(rs.getString("itemdesc") == null ? "" : rs
						.getString("itemdesc"));
				obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setRecivedQuantity((int) rs.getDouble("RcvdQuantity"));
				obj.setVendorName(rs.getString("vendorName") == null ? "" : rs
						.getString("vendorName"));
				obj.setDropToName(rs.getString("dropShipTo") == null ? "" : rs
						.getString("dropShipTo"));
				obj.setClassName(rs.getString("className") == null ? "" : rs
						.getString("className"));
				obj.setCustomerName(rs.getString("customerName") == null ? ""
						: rs.getString("customerName"));
				obj.setItemName(rs.getString("itemName") == null ? "" : rs
						.getString("itemName"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getPurchaseRequestOrder-->", ex);
		}
		return lst;
	}

	public int saveCustomerStatusHistroyfromFeedback(CustomerStatusHistoryModel obj,int webUserId,String webUserName) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.saveCustomerStatusHistroyfromFeedback(obj,webUserId,webUserName));
		} catch (Exception ex) {
			logger.error("error in HBAData---saveCustomerStatusHistroyfromFeedback-->",	ex);
		}
		return result;

	}

	public int updateCustomerStatusDescription(CustomerStatusHistoryModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query
					.updateCustomerStatusDescription(obj));
		} catch (Exception ex) {
			logger.error(
					"error in HBAData---updateCustomerStatusDescription-->", ex);
		}
		return result;

	}

	public void updateBarcode(QbListsModel item) {
		HBAQueries query = new HBAQueries();
		try {
			db.executeUpdateQuery(query.updateBarcode(item.getItem_Key(),
					item.getBarcode()));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateBarcodeSettings-->", ex);
		}

	}

	public List<LocalItemModel> GetLocalItemByRef(int itemTypeRef) {
		List<LocalItemModel> lst = new ArrayList<LocalItemModel>();
		HBAQueries query = new HBAQueries();
		LocalItemModel obj = new LocalItemModel();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetLocalItemByRef(itemTypeRef));
			while (rs.next()) {
				obj = new LocalItemModel();
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setName(rs.getString("name"));
				obj.setItemTypeRef(rs.getInt("ItemTypeRef"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HRData---getHRListValues-->", ex);
		}
		return lst;

	}

	public List<PaymentMethod> getPaymentMethod() {
		List<PaymentMethod> lst = new ArrayList<PaymentMethod>();
		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		PaymentMethod obj;
		try {
			rs = db.executeNonQuery(query.getPaymentMethod());
			while (rs.next()) {
				obj = new PaymentMethod();
				obj.setRec_No(rs.getInt("Rec_No"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HRData---getHRListValues-->", ex);
		}
		return lst;
	}

	public int addUserActivity(int Recno,String ACTIVITY,int activityRecNo,String memo,String refNumber,Date txDate,String WebUserName,int WebUserID,int action)
	{
		int result=0;		
		HBAQueries query=new HBAQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.addUserActivityQuery( Recno, ACTIVITY, activityRecNo, memo, refNumber, txDate,  WebUserName, WebUserID,action));

		}
		catch (Exception ex) {
			logger.error("error in HRData---addUserActivity-->" , ex);
		}
		return result;		
	}

	public int GetNewUserActivityRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewUserActivityRecNo());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewCheckMastRecNo-->", ex);
		}
		return MaxRecNo;
	}
	/*********************************JournalVoucher***********************************************/

	public int addNewJournalVoucher(JournalVoucherModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewJournalVoucherQuery(obj,webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewJournalVoucher-->", ex);
		}
		return result;
	}

	public int updateJournalVoucher(JournalVoucherModel obj, int webUserID) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateJournalVoucherQuery(obj,webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateJournalVoucher-->", ex);
		}
		return result;
	}

	public List<QbListsModel> getAccountTypeList() {
		List<QbListsModel> lst = new ArrayList<QbListsModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getAccountTypeList());
			while (rs.next()) {
				QbListsModel obj = new QbListsModel();
				obj.setRecNo(rs.getInt("SRL_No"));
				obj.setName(rs.getString("TypeName"));
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getAccountTypeList-->", ex);
		}
		return lst;
	}


	public List<String> getEntityType() {
		List<String> lst = new ArrayList<String>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getEntityType());
			while (rs.next()) {
				String obj = new String();
				obj = rs.getString("listtype");
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getAccountTypeList-->", ex);
		}
		return lst;
	}

	public int GetNewJournalVoucherRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewJournalVoucherRecNoQuery());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewJournalVoucherRecNo-->", ex);
		}
		return MaxRecNo;
	}

	public int deleteJournalLine(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.deleteJournalLineQuery(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteJournalLine-->", ex);
		}
		return result;

	}

	public int addJournalLine(JournalVoucherGridData obj, int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addJournalLineQuery(obj, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addExpense-->", ex);
		}
		return result;

	}

	public JournalVoucherModel navigationJournalVoucher(int journalVoucherKey,int webUserID, String navigation, String actionTYpe) {
		JournalVoucherModel obj = new JournalVoucherModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")	&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordJournalVoucher(journalVoucherKey, webUserID));
			} else if (navigation.equalsIgnoreCase("next") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordJournalVoucher(journalVoucherKey, webUserID));
			} else if (navigation.equalsIgnoreCase("next") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getFirstRecordJournalVoucher(webUserID));
			} else if (navigation.equalsIgnoreCase("prev") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getLastRecordJournalVoucher(webUserID));
			}
			while (rs.next()) {
				obj.setRecno(rs.getInt("rec_No"));
				obj.setTxnNumber(rs.getString("TxnNumber"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setStatus(rs.getString("Status"));
				obj.setTotalAmount(rs.getFloat("TotalAmount"));
				obj.setNotes(rs.getString("Notes") == null ? "" : rs.getString("Notes"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationJournalVoucher-->", ex);
		}
		return obj;
	}

	public List<JournalVoucherGridData> getJournalVoucherGridDataByID(int journalVoucherKey) {
		List<JournalVoucherGridData> lst = new ArrayList<JournalVoucherGridData>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getJournalVoucherGridDataByID(journalVoucherKey));
			while (rs.next()) {
				JournalVoucherGridData obj = new JournalVoucherGridData();
				obj.setRec_No(rs.getInt("rec_No"));
				obj.setLine_no(rs.getInt("line_No"));
				obj.setdC_Flag(rs.getString("DC_Flag"));
				obj.setAccountRef_Key(rs.getInt("AccountRef_Key"));
				obj.setEntityRef_Key(rs.getInt("EntityRef_Key"));
				obj.setEntityRef_Type(rs.getString("EntityRef_Type"));
				obj.setClassRef_Key(rs.getInt("ClassRef_Key"));
				obj.setAmount(rs.getFloat("Amount"));
				obj.setMemo(rs.getString("memo"));
				obj.setBillable(rs.getString("Billable"));
				//obj.setAccountTypeRef_Key(rs.getInt("AccountTypeRef_Key"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getJournalVoucherGridDataByID-->", ex);
		}
		return lst;
	}

	public boolean checkJournalVoucherDuplicate(String txnNumber,int rec_No) {
		boolean hasSerialNumber = false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkJournalVoucherDuplicate(txnNumber, rec_No));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkJournalVoucherDuplicate-->", ex);
		}

		return hasSerialNumber;
	}

	public boolean checkRVJournalVoucherDuplicate(String txnNumber) {
		boolean hasSerialNumber = false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkRVJournalVoucherDuplicate(txnNumber));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkRVJournalVoucherDuplicate-->", ex);
		}

		return hasSerialNumber;
	}

	/***************************************************Work Flow Purchase Request***************************************************/

	public int updatePurchaseRequestStatus(String records) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updatePurchaseRequestStatus(records));
		} catch (Exception ex) {
			logger.error("error in HBAData---updatePurchaseRequestStatus-->", ex);
		}
		return result;
	}


	public List<PurchaseRequestReportModel> getNotApprovedPurchaseRequestReport(
			int webUserId, Date fromDate, Date toDate, String reportFrom) {
		List<PurchaseRequestReportModel> lst = new ArrayList<PurchaseRequestReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getNotApprovedPurchaseRequestReport(webUserId,fromDate, toDate, reportFrom));
			while (rs.next()) {
				PurchaseRequestReportModel obj = new PurchaseRequestReportModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("TxnDate")));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				if (obj.getStatus().equalsIgnoreCase("C")) {
					obj.setStatus("Created");
				} else if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatus("Approved");
				} else if (obj.getStatus().equalsIgnoreCase("P")) {
					obj.setStatus("Printed");
				} else if (obj.getStatus().equalsIgnoreCase("V")) {
					obj.setStatus("void");
				}
				obj.setSource(rs.getString("Source") == null ? "" : rs.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				obj.setDecription(rs.getString("itemdesc") == null ? "" : rs.getString("itemdesc"));
				obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setRecivedQuantity((int) rs.getDouble("RcvdQuantity"));
				obj.setVendorName(rs.getString("vendorName") == null ? "" : rs.getString("vendorName"));
				obj.setDropToName(rs.getString("dropShipTo") == null ? "" : rs.getString("dropShipTo"));
				obj.setClassName(rs.getString("className") == null ? "" : rs.getString("className"));
				obj.setCustomerName(rs.getString("customerName") == null ? "": rs.getString("customerName"));
				obj.setItemName(rs.getString("itemName") == null ? "" : rs.getString("itemName"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getNotApprovedPurchaseRequestReport-->", ex);
		}
		return lst;
	}

	public List<ApprovedMaterialsModel> getApprovedPurchaseRequest(int webUserId, Date fromDate, Date toDate, String reportFrom,int vendorKey,int RecNo)
	{
		int tmpRec=0;
		List<ApprovedMaterialsModel> lst = new ArrayList<ApprovedMaterialsModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getApprovedPurchaseRequest(webUserId,fromDate, toDate, reportFrom,vendorKey,RecNo));
			while (rs.next()) {

				if(rs.getInt("PORecNo")!=tmpRec){
					ApprovedMaterialsModel obj1=new ApprovedMaterialsModel();
					tmpRec=rs.getInt("PORecNo");
					obj1.setpORecNo(rs.getInt("PORecNo"));
					obj1.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
					obj1.setTxnDate(df.format(rs.getDate("TXNDate")));
					obj1.setLevel("P");
					obj1.setShow(false);
					lst.add(obj1);

				}
				ApprovedMaterialsModel obj = new ApprovedMaterialsModel();		
				obj.setpORecNo(rs.getInt("PORecNo"));
				obj.setLineNo(rs.getInt("Line_No"));
				obj.setItemRefKey(rs.getInt("ItemRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setDescription(rs.getString("Description") == null ? "" : rs.getString("Description"));
				obj.setQuantity(rs.getDouble("Quantity"));
				obj.setRcvdQuantity(rs.getDouble("RcvdQuantity"));
				obj.setRemainingQuantity(rs.getDouble("RemainingQuantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setItemName(rs.getString("ItemName") == null ? "" : rs.getString("ItemName"));
				obj.setVendorName(rs.getString("VendorName") == null ? "" : rs.getString("VendorName"));
				obj.setRefNumber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setLevel("S");
				obj.setShow(true);
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getApprovedPurchaseRequest-->", ex);
		}
		return lst;
	}

	public boolean checkMR(int vendorKey) {
		boolean hasMR = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkMR(vendorKey));
			while (rs.next()) {
				hasMR = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkMR-->", ex);
		}

		return hasMR;
	}


	public int updatePurchaseRequestLine(PurchaseRequestGridData obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updatePurchaseRequestLine(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updatePurchaseRequestLine-->", ex);
		}
		return result;
	}

	public boolean checkMRIsFULLYRECEIVED(int rec_no) {
		boolean hasMR = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkMRIsFULLYRECEIVED(rec_no));
			if (rs.next()) {
				if(rs.getDouble("POQty")==rs.getDouble("RcvdQty")){				
					hasMR = true;
				}
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkMRIsFULLYRECEIVED-->", ex);
		}

		return hasMR;
	}


	/***************************************************Work Flow Purchase Order***************************************************/

	public List<PurchaseRequestReportModel> getNotApprovedPurchaseOrderReport(int webUserId,boolean seeTrasction, Date fromDate, Date toDate, String reportFrom) {
		List<PurchaseRequestReportModel> lst = new ArrayList<PurchaseRequestReportModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getNotApprovedPurchaseOrderReport(webUserId,seeTrasction,fromDate, toDate, reportFrom));
			while (rs.next()) {
				PurchaseRequestReportModel obj = new PurchaseRequestReportModel();
				obj.setRecNo(rs.getInt("rec_No"));
				obj.setTxtnDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("TxnDate")));
				obj.setRefNUmber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setVendorRefKEy(rs.getInt("VendorRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setClassRefkey(rs.getInt("ClassRefKey"));
				obj.setAdress(rs.getString("Address") == null ? "" : rs.getString("Address"));
				obj.setShipTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAdressBillTo(rs.getString("ShipTo") == null ? "" : rs.getString("ShipTo"));
				obj.setAmount(rs.getDouble("TotalAmount"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				if (obj.getStatus().equalsIgnoreCase("C")) {
					obj.setStatus("Created");
				} else if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatus("Approved");
				} else if (obj.getStatus().equalsIgnoreCase("P")) {
					obj.setStatus("Printed");
				} else if (obj.getStatus().equalsIgnoreCase("V")) {
					obj.setStatus("void");
				}

				obj.setSource(rs.getString("Source") == null ? "" : rs.getString("Source"));
				obj.setWebUserId(rs.getInt("webUserId"));
				/*obj.setQuantity((int) rs.getDouble("Quantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setRecivedQuantity((int) rs.getDouble("RcvdQuantity"));*/
				obj.setVendorName(rs.getString("vendorName") == null ? "" : rs.getString("vendorName"));
				obj.setDropToName(rs.getString("dropShipTo") == null ? "" : rs.getString("dropShipTo"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getNotApprovedPurchaseOrderReport-->", ex);
		}
		return lst;
	}

	public int updatePurchaseOrderStatus(String records) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updatePurchaseOrderStatus(records));
		} catch (Exception ex) {
			logger.error("error in HBAData---updatePurchaseOrderStatus-->", ex);
		}
		return result;
	}
	
	public int updateItemReceiptStatus(String records) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateItemReceiptStatus(records));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateItemReceiptStatus-->", ex);
		}
		return result;
	}

	public List<ApprovePurchaseOrderModel> getApprovedPurchaseOrder(int webUserId, Date fromDate, Date toDate, String reportFrom,int vendorKey,int RecNo)
	{
		int tmpRec=0;
		List<ApprovePurchaseOrderModel> lst = new ArrayList<ApprovePurchaseOrderModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getApprovedPurchaseOrder(webUserId,fromDate, toDate, reportFrom,vendorKey,RecNo));
			while (rs.next()) {

				if(rs.getInt("PORecNo")!=tmpRec){
					ApprovePurchaseOrderModel obj1=new ApprovePurchaseOrderModel();
					tmpRec=rs.getInt("PORecNo");
					obj1.setpORecNo(rs.getInt("PORecNo"));
					obj1.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
					obj1.setTxnDate(df.format(rs.getDate("TXNDate")));
					obj1.setLevel("P");
					obj1.setShow(false);
					lst.add(obj1);

				}
				ApprovePurchaseOrderModel obj = new ApprovePurchaseOrderModel();		
				obj.setpORecNo(rs.getInt("PORecNo"));
				obj.setLineNo(rs.getInt("Line_No"));
				obj.setItemRefKey(rs.getInt("ItemRefKey"));
				obj.setEntityRefKey(rs.getInt("EntityRefKey"));
				obj.setDescription(rs.getString("Description") == null ? "" : rs.getString("Description"));
				obj.setQuantity(rs.getDouble("Quantity"));
				obj.setRcvdQuantity(rs.getDouble("RcvdQuantity"));
				obj.setRemainingQuantity(rs.getDouble("RemainingQuantity"));
				obj.setRate(rs.getDouble("Rate"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setItemName(rs.getString("ItemName") == null ? "" : rs.getString("ItemName"));
				obj.setVendorName(rs.getString("VendorName") == null ? "" : rs.getString("VendorName"));
				obj.setRefNumber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setLevel("S");
				obj.setShow(true);
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getApprovedPurchaseOrder-->", ex);
		}
		return lst;
	}

	public boolean checkPOIsFULLYRECEIVED(int rec_no) {
		boolean hasMR = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkPOIsFULLYRECEIVED(rec_no));
			if (rs.next()) {
				if(rs.getDouble("POQty")==rs.getDouble("RcvdQty")){				
					hasMR = true;
				}
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkPOIsFULLYRECEIVED-->", ex);
		}

		return hasMR;
	}

	public boolean checkPO(int vendorKey) {
		boolean hasMR = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkPO(vendorKey));
			while (rs.next()) {
				hasMR = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkPO-->", ex);
		}

		return hasMR;
	}

	public int updatePurchaseOrderLine(CheckItemsModel item) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updatePurchaseOrderLine(item));
		} catch (Exception ex) {
			logger.error("error in HBAData---updatePurchaseRequestLine-->", ex);
		}
		return result;
	}


	public List<SelectItemReceiptModel> getItemReceipt(int webUserId, Date fromDate, Date toDate, String reportFrom,int vendorKey,int RecNo) 
	{
		int tmpRec=0;
		List<SelectItemReceiptModel> lst = new ArrayList<SelectItemReceiptModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getItemReceipt(webUserId,fromDate, toDate, reportFrom,vendorKey,RecNo));
			while (rs.next()) {

				if(rs.getInt("IRMastRecNo")!=tmpRec){
					SelectItemReceiptModel obj1=new SelectItemReceiptModel();
					tmpRec=rs.getInt("IRMastRecNo");
					obj1.setpORecNo(rs.getInt("IRMastRecNo"));
					obj1.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
					obj1.setTxnDate(df.format(rs.getDate("IRDate")));
					obj1.setLevel("P");
					obj1.setShow(false);
					lst.add(obj1);

				}
				SelectItemReceiptModel obj = new SelectItemReceiptModel();		
				obj.setpORecNo(rs.getInt("IRMastRecNo"));
				obj.setLineNo(rs.getInt("LineNo"));
				obj.setItemRefKey(rs.getInt("ItemKey"));
				//obj.setCustkey(rs.getInt("custkey"));
				obj.setDescription(rs.getString("Description") == null ? "" : rs.getString("Description"));
				obj.setQuantity(rs.getDouble("Quantity"));
				obj.setBilledQuantity(rs.getDouble("BilledQuantity"));
				obj.setRemainingQuantity(rs.getDouble("RemainingQuantity"));
				obj.setCost(rs.getDouble("cost"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setItemName(rs.getString("ItemName") == null ? "" : rs.getString("ItemName"));
				obj.setVendorName(rs.getString("VendorName") == null ? "" : rs.getString("VendorName"));
				obj.setIrNo(rs.getString("IrNo") == null ? "" : rs.getString("IrNo"));
				obj.setAccountNumber(rs.getInt("AccountNumber"));
				obj.setAccName(rs.getString("AccName") == null ? "" : rs.getString("AccName"));
				obj.setAccType(rs.getString("AccountType") == null ? "" : rs.getString("AccountType"));
				obj.setLevel("S");
				obj.setShow(true);
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getItemReceipt-->", ex);
		}
		return lst;
	}

	public boolean checkIR(int vendorKey) {
		boolean hasMR = false;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIR(vendorKey));
			while (rs.next()) {
				hasMR = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkIR-->", ex);
		}

		return hasMR;
	}

	public int insertItemReceiptLine(CheckItemsModel obj) {


		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.insertItemReceiptLine(obj,GetMaxBilledItemReceipts()));
		} catch (Exception ex) {
			logger.error("error in HBAData---addCheckItemsItemReceipt-->", ex);
		}
		return result;

	}

	public int GetMaxBilledItemReceipts()
	{
		int MaxRecNo=1;

		HBAQueries query=new HBAQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.GetMaxBilledItemReceipts());
			while(rs.next())
			{
				MaxRecNo=rs.getInt("MaxRecNo");						
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---GetMaxBilledItemReceipts-->" , ex);
		}
		return MaxRecNo;
	}

	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-Delivery

	public List<DeliveryModel> getDeliveryReport(int webUserID,boolean seeTrasction,Date fromDate, Date toDate)
	{
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		List<DeliveryModel> lst=new ArrayList<DeliveryModel>();
		try
		{
			rs = db.executeNonQuery(query.getDeliveryReportQuery(webUserID,seeTrasction,fromDate, toDate));
			while (rs.next()) 
			{
				DeliveryModel obj=new DeliveryModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setCustomerRefKey(rs.getInt("CustomerRefKey"));
				obj.setRefNumber(rs.getString("RefNumber"));
				obj.setDeliveryDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("txnDate")));
				obj.setCustomerName(rs.getString("entity") == null ? "": rs.getString("entity"));
				obj.setUserName(rs.getString("WebUserName")== null ? "": rs.getString("WebUserName"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? "" : rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? "" : rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? "" : rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? "" : rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? "" : rs.getString("shipaddress5"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs.getString("Status"));
				obj.setTransformQ(rs.getString("TransformQ")==null?"":rs.getString("TransformQ"));				
				if(obj.getStatus().equalsIgnoreCase("C"))
				{
					obj.setStatusDesc("Created");
				}
				else if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatusDesc("Approved");
				} else if (obj.getStatus().equalsIgnoreCase("P")) {
					obj.setStatusDesc("Printed");
				} else if (obj.getStatus().equalsIgnoreCase("V")) {
					obj.setStatusDesc("void");
				}
				else
				{
					obj.setStatusDesc("");
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getDeliveryReport-->" , ex);
		}
		return lst;
	}

	public DeliveryModel navigationDelivery(int deliveryId,int webUserID,boolean seeTrasction, String navigation, String actionTYpe) {
		DeliveryModel obj = new DeliveryModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getPreviousRecordDelivery(deliveryId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) {
				rs = db.executeNonQuery(query.getNextRecordDelivery(deliveryId, webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("next") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getFirstRecordDelivery(webUserID,seeTrasction));
			} else if (navigation.equalsIgnoreCase("prev") && actionTYpe.equalsIgnoreCase("create")) {
				rs = db.executeNonQuery(query.getLastRecordDelivery(webUserID,seeTrasction));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnID(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setRefNumber(rs.getString("refNUmber"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? "" : rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? "" : rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? "" : rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? "" : rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? "" : rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? "" : rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? "" : rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs .getString("billaddressPostalCode") == null ? "" : rs.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? "" : rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? "" : rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? "" : rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? "" : rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? "" : rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? "" : rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? "" : rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? "" : rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? "" : rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs.getString("shipaddressPostalCode") == null ? "" : rs.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? "": rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? "": rs.getString("shipaddressNote"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setpONumber(rs.getString("pONumber") == null ? "": rs.getString("pONumber"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? "": rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? "": rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? "": rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? "": rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? "": rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? "" : rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? "" : rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? "" : rs.getString("customField5"));
				obj.setQuotationRecNo(rs.getInt("quotationRecNO"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? "": rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs.getString("qtyHide"));
				obj.setClassHIDE(rs.getString("classHide") == null ? "" : rs.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setTransformQ(rs.getString("TransformQ") == null ? "" : rs.getString("TransformQ"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationDelivery-->", ex);
		}
		return obj;
	}

	public List<DeliveryLineModel> getDDeliveryGridDataByID(int deliveryId) {
		List<DeliveryLineModel> lst = new ArrayList<DeliveryLineModel>();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getDDeliveryGridDataByID(deliveryId));
			while (rs.next()) {
				DeliveryLineModel obj = new DeliveryLineModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setLineNo(rs.getInt("lineNo"));
				obj.setItemRefKey(rs.getInt("itemrefKey"));
				obj.setDescription(rs.getString("description"));
				obj.setQuantity((int) rs.getDouble("quantity"));
				obj.setQuantityInvoice((int) rs.getDouble("QuantityInvoice"));
				obj.setQuantityPost((int) rs.getDouble("QuantityPost"));
				obj.setRate(rs.getDouble("rate"));
				obj.setAvgCost(rs.getDouble("avgCost"));
				obj.setRatePercent(rs.getDouble("ratePercent"));
				obj.setPriceLevelRefKey(rs.getInt("priceLevelRefKey"));
				obj.setClassRefKey(rs.getInt("classrefKey"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setServiceDate(rs.getDate("serviceDate"));
				obj.setSalesTaxCodeRefKey(rs.getInt("salestaxcoderefkey"));
				obj.setIsTaxable(rs.getString("isTaxable"));
				obj.setOverrideItemAccountRefKey(rs.getInt("overrideItemAccountRefKey"));
				obj.setOther1(rs.getString("other1") == null ? "" : rs.getString("other1"));
				obj.setOther2(rs.getString("other2") == null ? "" : rs.getString("other2"));
				obj.setQuotationLineNo(rs.getInt("QuotationLineNo"));
				obj.setInvertySiteKey(rs.getInt("inventorysitekey"));
				obj.setItemName(rs.getString("ItemName") == null ? "" : rs.getString("ItemName"));
				obj.setItemNameAR(rs.getString("ItemNameAr") == null ? "" : rs.getString("ItemNameAr"));
				obj.setItemType(rs.getString("ItemType") == null ? "" : rs.getString("ItemType"));
				obj.setIncomeAccountRef(rs.getInt("IncomeAccountRef"));
				obj.setClassName(rs.getString("ClassName") == null ? "" : rs.getString("ClassName"));
				obj.setItemClassAR(rs.getString("ItemClassAR") == null ? "" : rs.getString("ItemClassAR"));
				obj.setInvoiceRecNo(rs.getInt("InvoiceRecNo"));
				obj.setTxnType(rs.getString("TxnType") == null ? "" : rs.getString("TxnType"));
				obj.setInvoiceLineNo(rs.getInt("InvoiceLineNo"));
				obj.setInventorySite(rs.getString("InventorySite") == null ? "" : rs.getString("InventorySite"));
				obj.setInventorySiteAR(rs.getString("InventorySiteAR") == null ? "" : rs.getString("InventorySiteAR"));
				lst.add(obj);

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getDDeliveryGridDataByID-->", ex);
		}
		return lst;
	}

	public DeliveryModel getDeliveryByID(int deliveryKey, int webUserID,boolean seeTrasction) {
		DeliveryModel obj = new DeliveryModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getDeliveryByID(deliveryKey,	webUserID,seeTrasction));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnID(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setTxnDate(rs.getDate("txnDate"));
				obj.setRefNumber(rs.getString("refNUmber"));
				obj.setBillAddress1(rs.getString("billaddress1") == null ? "" : rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2") == null ? "" : rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3") == null ? "" : rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4") == null ? "" : rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5") == null ? "" : rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity") == null ? "" : rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState") == null ? "" : rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs .getString("billaddressPostalCode") == null ? "" : rs.getString("billaddressPostalCode"));
				obj.setBillAddressCountry(rs.getString("billaddressCountry") == null ? "" : rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote") == null ? "" : rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1") == null ? "" : rs.getString("shipaddress1"));
				obj.setShipAddress2(rs.getString("shipaddress2") == null ? "" : rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3") == null ? "" : rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4") == null ? "" : rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5") == null ? "" : rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity") == null ? "" : rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState") == null ? "" : rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs.getString("shipaddressPostalCode") == null ? "" : rs.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry") == null ? "": rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote") == null ? "": rs.getString("shipaddressNote"));
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setfOB(rs.getString("fob"));
				obj.setpONumber(rs.getString("pONumber") == null ? "": rs.getString("pONumber"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted") == null ? "": rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed") == null ? "": rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded") == null ? "": rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other") == null ? "" : rs.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1") == null ? "": rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2") == null ? "": rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3") == null ? "" : rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4") == null ? "" : rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5") == null ? "" : rs.getString("customField5"));
				obj.setQuotationRecNo(rs.getInt("quotationRecNO"));
				obj.setStatus(rs.getString("status") == null ? "" : rs.getString("status"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde") == null ? "": rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide") == null ? "" : rs.getString("qtyHide"));
				obj.setClassHIDE(rs.getString("classHide") == null ? "" : rs.getString("classHide"));
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getDeliveryByID-->", ex);
		}
		return obj;
	}

	public int addNewDelivery(DeliveryModel obj, int webUserID,String webUserName) 
	{
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addNewDelivery(obj, webUserID,webUserName));
		} catch (Exception ex) {
			logger.error("error in HBAData---addNewDelivery-->", ex);
		}
		return result;
	}

	public int addDeliveryGridItems(DeliveryLineModel obj, int RecNo) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addDeliveryGridItems(obj, RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---addDeliveryGridItems-->",ex);
		}
		return result;
	}
	
	public int deleteDeliveryGridItems(int RecNo) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.deleteDeliveryGridItems(RecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData---deleteDeliveryGridItems-->", ex);
		}
		return result;

	}
	
	public int updateDelivery(DeliveryModel obj, int webUserID,String webUserName) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateDelivery(obj, webUserID,webUserName));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateDelivery-->", ex);
		}
		return result;
	}
	
	public int updateQBItemsDesc(DeliveryLineModel obj) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateQBItemsDesc(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData --- updateQBItemsDesc -->", ex);
		}
		return result;
	}
	
	public boolean checkIfSerialNumberIsDuplicateForDelivery(String SerialNumber, int recNo) {
		boolean hasSerialNumber = false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkIfSerialNumberIsDuplicateForDelivery(SerialNumber,recNo));
			while (rs.next()) {
				hasSerialNumber = true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---checkIfSerialNumberIsDuplicateForDelivery-->",ex);
		}
		return hasSerialNumber;
	}

	
	public ChangeStatusQuotationModel getQuotationByIDForChangeStatus(int quotationKey) {
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		ChangeStatusQuotationModel obj = new ChangeStatusQuotationModel();
		try {
			
			rs = db.executeNonQuery(query.getQuotationByIDForChangeStatus(quotationKey));
			while (rs.next()) {
				obj.setProspectiveName(rs.getString("ProspectiveName") == null ? "" : rs.getString("ProspectiveName"));
				obj.setCustomerName(rs.getString("Entity") == null ? "" : rs.getString("Entity"));
				obj.setQuotationDate(rs.getDate("TxnDate"));
				obj.setLastChangeDate(rs.getDate("LastStatusDate"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs.getString("Status"));
				obj.setAmount(rs.getDouble("amount"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getQuotationByIDForChangeStatus-->", ex);
		}
		return obj;
	}
	
	public int updateQuotationStatus(int webUserID,String status,String statusDesc) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateQuotationStatus(webUserID,status,statusDesc));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateQuotationStatus-->", ex);
		}
		return result;
	}
	
	public int updateActivityStatus(int quotationKey) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateActivityStatus(quotationKey));
		} catch (Exception ex) {
			logger.error("error in HBAData--- updateActivityStatus -->", ex);
		}
		return result;
	}
	
	public int addActivityStatus(ActivityStatusModel model) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.AddActivityStatus(model));
		} catch (Exception ex) {
			logger.error("error in HBAData--- addActivityStatus -->", ex);
		}
		return result;
	}
	
	public List<ApprovedQuotationModel> getApprovedQuotation(int customerKey,int webUserId,int RecNo) {
		int tmpRec=0;
		List<ApprovedQuotationModel> lst = new ArrayList<ApprovedQuotationModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getApprovedQuotation(customerKey,webUserId,RecNo));
			while (rs.next()) {
				if(rs.getInt("RecNo")!=tmpRec){
					ApprovedQuotationModel obj = new ApprovedQuotationModel();	
					tmpRec=rs.getInt("RecNo");						
					obj.setRecNo(rs.getInt("RecNo"));
					obj.setAmount(rs.getDouble("Amount"));
					obj.setSendVia(rs.getString("sendVia") == null ? "" : rs.getString("sendVia"));
					obj.setTxnDate(rs.getDate("TxnDate"));
					obj.setEntity(rs.getString("Entity") == null ? "" : rs.getString("Entity"));
					obj.setRefNumber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
					obj.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
					obj.setShow(true);
					lst.add(obj);

				}
				
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getApprovedQuotation-->", ex);
		}
		return lst;
	}
	
	
	public boolean checkCustomerQuotation(int vendorKey) {
		boolean flag=false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getApprovedQuotation(vendorKey,0,0));
			while (rs.next()) {
				flag=true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData--- checkCustomerQuotation -->", ex);
		}
		return flag;
	}
	
	
	public int updateQuotationDelivery(int quotationKey,int deliveryRecNo) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateQuotationDelivery(quotationKey,deliveryRecNo));
		} catch (Exception ex) {
			logger.error("error in HBAData--- updateQuotationDelivery -->", ex);
		}
		return result;
	}
	
	public int addQuotationDelivery(DeliveryLineModel item,int recNo) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addQuotationDelivery(item,recNo));
		} catch (Exception ex) {
			logger.error("error in HBAData--- addQuotationDelivery -->", ex);
		}
		return result;
	}
	
	public int GetNewDeliveryRecNo() {
		int MaxRecNo = 1;

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.GetNewDeliveryRecNo());
			while (rs.next()) {
				MaxRecNo = rs.getInt("MaxRecNo") + 1;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---GetNewDeliveryRecNo-->", ex);
		}
		return MaxRecNo;
	}
	
	public int updateDeliveryStatus(String records) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateDeliveryStatus(records));
		} catch (Exception ex) {
			logger.error("error in HBAData---updatePurchaseRequestStatus-->", ex);
		}
		return result;
	}
	
	public boolean getCustomerDelivery(int customer,String type) {
		boolean flag=false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getCustomerDelivery(customer,0,type));
			while (rs.next()) {
				flag=true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData--- checkCustomerQuotation -->", ex);
		}
		return flag;
	}

	public List<DeliveryModel> getDeliveryForInvoice(int customerKey,int webUserId,String txnType,int RecNo) {
		int tmpRec=0;
		List<DeliveryModel> lst = new ArrayList<DeliveryModel>();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getDeliveryForInvoice(customerKey,webUserId,txnType,RecNo));
			while (rs.next()) {
				if(rs.getInt("RecNo")!=tmpRec){
					DeliveryModel obj1=new DeliveryModel();
					tmpRec=rs.getInt("RecNo");
					obj1.setRecNo(rs.getInt("RecNo"));
					obj1.setRefNumber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
					obj1.setMemo(rs.getString("memo") == null ? "" : rs.getString("memo"));
					obj1.setTxnDateStr(df.format(rs.getDate("TXNDate")));
					obj1.setLevel("P");
					obj1.setShow(false);
					lst.add(obj1);
				}
				DeliveryModel obj = new DeliveryModel();		
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setLineNo(rs.getInt("LineNo"));
				obj.setItemrefkey(rs.getInt("ItemRefKey"));
				obj.setMemo(rs.getString("Description") == null ? "" : rs.getString("Description"));
				obj.setQuantity(rs.getDouble("Quantity"));
				obj.setQuantityInvoice(rs.getDouble("QuantityInvoice"));
				obj.setItemName(rs.getString("ItemName") == null ? "" : rs.getString("ItemName"));
				obj.setRefNumber(rs.getString("RefNumber") == null ? "" : rs.getString("RefNumber"));
				obj.setInvertySiteKey(rs.getInt("inventorysitekey"));
				obj.setLevel("S");
				obj.setShow(true);
				lst.add(obj);
			}
		} catch (Exception ex) {
			logger.error("error in HBAData--- getApprovedPurchaseOrder -->", ex);
		}
		return lst;
	}
	
	public int deleteCashInvoiceDeliveryLine(int RecNo,String type) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.deleteCashInvoiceDeliveryLine(RecNo,type));
		} catch (Exception ex) {
			logger.error("error in HBAData--- deleteCashInvoiceDeliveryLine -->", ex);
		}
		return result;

	}
	
	public int addDeliveryInvoice(CashInvoiceGridData obj, int RecNo, String type) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.addDeliveryInvoice(obj, RecNo,type));
		} catch (Exception ex) {
			logger.error("error in HBAData---addDeliveryInvoice-->", ex);
		}
		return result;

	}
	
	public boolean checkInvoicedDelivery(int deliveryKey) {
		boolean flag=false;
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.checkInvoicedDelivery(deliveryKey));
			while (rs.next()) {
				flag=true;
			}
		} catch (Exception ex) {
			logger.error("error in HBAData--- checkInvoicedDelivery -->", ex);
		}
		return flag;
	}
	
	public CutomerSummaryReport getCutomerTotalBalance(int customerID, String isActive, boolean inculdeOtherTransactions) {
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		double tempBalance = 0;
		double tempTotal = 0;
		double tempCreditTotal = 0;
		double tempDebitTotal = 0;
		int tempCustomerKey = 0;
		String customerName = "";
		CutomerSummaryReport objTotal = new CutomerSummaryReport();
		CutomerSummaryReport obj = new CutomerSummaryReport();
		
		try {
			Calendar c = Calendar.getInstance();		
			Date toDate=df.parse(sdf.format(c.getTime()));
			rs = db.executeNonQuery(query.getCutomerDetailedReport(toDate,customerID, isActive, inculdeOtherTransactions));
			boolean hasNext = rs.next();
			while (hasNext) {
				
				obj.setRec_no(rs.getInt("recno"));
				obj.setCustKey(rs.getInt("cust_key"));
				obj.setEnityName(rs.getString("fullName") == null ? "" : rs.getString("fullName"));
				obj.setFromDate(rs.getDate("txnDate"));
				if (rs.getDouble("debit") < 0)
					obj.setDebit(-(rs.getDouble("debit")));
				else
					obj.setDebit(rs.getDouble("debit"));
				if (rs.getDouble("credit") < 0)
					obj.setCredit(-(rs.getDouble("credit")));
				else
					obj.setCredit(rs.getDouble("credit"));
				obj.setRefranceNumber(rs.getString("recNO") == null ? "" : rs.getString("recNO"));
				obj.setAcountName(rs.getString("itemOrAccountName") == null ? "": rs.getString("itemOrAccountName"));
				obj.setTxnType(rs.getString("trasType") == null ? "" : rs.getString("trasType"));
				obj.setTxnDate(rs.getDate("txndate") == null ? "": new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("txndate")));
				if (rs.getString("trasType").equalsIgnoreCase("J")) {
					obj.setTxnType("Jouneral Voucher");
				} else if (rs.getString("trasType").equalsIgnoreCase("R")) {
					obj.setTxnType("Receipt Voucher");
				}
				if (tempCustomerKey == 0) {
					tempCustomerKey = rs.getInt("cust_key");
				}
				if (tempCustomerKey != 0 && tempCustomerKey == rs.getInt("cust_key")) {
					objTotal = new CutomerSummaryReport();
					objTotal.setEnityName(obj.getEnityName());
					objTotal.setTxnType("Total");
					customerName = obj.getEnityName();
					tempTotal = tempTotal + obj.getAmount();
					tempCreditTotal = tempCreditTotal + obj.getCredit();
					tempDebitTotal = tempDebitTotal + obj.getDebit();
					if (rs.getString("debitFlag").equalsIgnoreCase("Y")) {
						obj.setAmount(rs.getDouble("debit"));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					} else {
						if (rs.getDouble("credit") > 0)
							obj.setAmount(-(rs.getDouble("credit")));
						else
							obj.setAmount(+(rs.getDouble("credit")));

						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					}
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);
				} else if (tempCustomerKey != 0	&& tempCustomerKey != rs.getInt("cust_key")) {
					objTotal = new CutomerSummaryReport();
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);
					objTotal.setEnityName(customerName);
					objTotal.setTxnType("Total");
					//lst.add(objTotal);
					tempTotal = 0;
					tempCreditTotal = 0;
					tempDebitTotal = 0;
					tempCreditTotal = rs.getDouble("credit");
					tempDebitTotal = rs.getDouble("debit");
					tempBalance = 0;
					customerName = "";
					customerName = rs.getString("fullName") == null ? "" : rs.getString("fullName");
					if (rs.getString("debitFlag").equalsIgnoreCase("Y")) {
						obj.setAmount(rs.getDouble("debit"));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					} else {
						if (rs.getDouble("credit") > 0)
							obj.setAmount(-(rs.getDouble("credit")));
						else
							obj.setAmount(+(rs.getDouble("credit")));
						tempBalance = tempBalance + obj.getAmount();
						obj.setBalance(tempBalance);
					}
					tempCustomerKey = rs.getInt("cust_key");
				}
				// lst.add(obj);
				hasNext = rs.next();
				/*if (!hasNext) {
					lst.add(objTotal);
				}*/

			}
		} catch (Exception ex) {
			logger.error("error in HBAData---getCutomerTotalBalance-->", ex);
		}

		return obj;

	}
	
	public int updateCustomerBalance(double balance,int customerKey) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateCustomerBalance(balance,customerKey));
			result = 1;
		} catch (Exception ex) {
			logger.error("error in HBAData---updateCustomerBalance-->", ex);
		}
		return result;

	}
	
	public BankTransferModel navigationBankTransfer(int bankTransferkey, int webUserID,boolean seeTrasction,String navigation, String actionTYpe,String type) {
		BankTransferModel obj = new BankTransferModel();

		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			if (navigation.equalsIgnoreCase("prev")	&& (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) 
			{
				rs = db.executeNonQuery(query.getPreviousRecordPayment(bankTransferkey, webUserID,seeTrasction,type));
			} 
			else if (navigation.equalsIgnoreCase("next") && (actionTYpe.equalsIgnoreCase("edit") || actionTYpe.equalsIgnoreCase("view"))) 
			{
				rs = db.executeNonQuery(query.getNextRecordCashPayment(bankTransferkey, webUserID,seeTrasction,type));
			} 
			else if (navigation.equalsIgnoreCase("next")&& actionTYpe.equalsIgnoreCase("create")) 
			{
				rs = db.executeNonQuery(query.getFirstRecordCashPayment(webUserID,seeTrasction,type));
			} 
			else if (navigation.equalsIgnoreCase("prev") && actionTYpe.equalsIgnoreCase("create")) 
			{
				rs = db.executeNonQuery(query.getLastRecordCashPayment(webUserID,seeTrasction,type));
			}
			while (rs.next()) {
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setTxnID(rs.getString("TxnID") == null ? "" : rs.getString("TxnID"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setPvNo(rs.getString("PVNo") == null ? "" : rs.getString("PVNo"));
				obj.setPvDate(rs.getDate("PVDate"));
				obj.setBankKey(rs.getInt("BankKey"));
				obj.setPayeeKey(rs.getInt("PayeeKey"));
				obj.setPayeeType(rs.getString("PayeeType") == null ? "" : rs.getString("PayeeType"));
				obj.setPrintName(rs.getString("PrintName") == null ? "" : rs.getString("PrintName"));
				obj.setQBRefDate(rs.getString("QBRefDate") == null ? "" : rs.getString("QBRefDate"));
				obj.setQBRefNo(rs.getString("QBRefNo") == null ? "" : rs.getString("QBRefNo"));
				obj.setMemo(rs.getString("Memo") == null ? "" : rs.getString("Memo"));
				obj.setAmount(rs.getDouble("Amount"));
				obj.setStatus(rs.getString("Status") == null ? "" : rs.getString("Status"));
				obj.setBankRefKey(rs.getInt("BankRefKey"));
				obj.setCheque(rs.getString("Cheque") == null ? "" : rs.getString("Cheque"));
				obj.setQBStatus(rs.getString("QBStatus") == null ? "" : rs.getString("QBStatus"));
				obj.setExpClassHide(rs.getString("ExpClassHide") == null ? "": rs.getString("ExpClassHide"));
				obj.setExpMemoHide(rs.getString("ExpMemoHide") == null ? "": rs.getString("ExpMemoHide"));
				obj.setExpBillNoHide(rs.getString("ExpBillNoHide") == null ? "": rs.getString("ExpBillNoHide"));
				obj.setExpBillDateHide(rs.getString("ExpBillDateHide") == null ? "": rs.getString("ExpBillDateHide"));
				obj.setItemClassHide(rs.getString("ItemClassHide") == null ? "": rs.getString("ItemClassHide"));
				obj.setItemDesHide(rs.getString("ItemDesHide") == null ? "": rs.getString("ItemDesHide"));
				obj.setItemBillDateHide(rs.getString("ItemBillDateHide") == null ? "": rs.getString("ItemBillDateHide"));
				obj.setItemBillNoHide(rs.getString("ItemBillNoHide") == null ? "": rs.getString("ItemBillNoHide"));
				obj.setPVCheck_Printed(rs.getString("PVCheck_Printed") == null ? "": rs.getString("PVCheck_Printed"));
				obj.setUnitKey(rs.getInt("UnitKey"));
				obj.setSwiftCode(rs.getString("SwiftCode") == null ? "" : rs.getString("SwiftCode"));
				obj.setUserID(rs.getInt("UserID"));
			}
		} catch (Exception ex) {
			logger.error("error in HBAData---navigationBankTransfer-->", ex);
		}
		return obj;
	}
	
	public BankTransferModel getBankInfoForBankTransfer(long recno) {
		BankTransferModel obj = new BankTransferModel();
		HBAQueries query = new HBAQueries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getBankInfoForBankTransfer(recno));
			while (rs.next()) {
				obj.setFromActName(rs.getString("From_ActName") == null ? "" : rs.getString("From_ActName"));
				obj.setFromBankName(rs.getString("From_BankName") == null ? "": rs.getString("From_BankName"));
				obj.setFromActNumber(rs.getString("From_ActNumber") == null ? "" : rs.getString("From_ActNumber"));
				obj.setFromBranch(rs.getString("From_Branch") == null ? "": rs.getString("From_Branch"));
				obj.setToActName(rs.getString("To_ActName") == null ? "" : rs.getString("To_ActName"));
				obj.setToBankName(rs.getString("To_BankName") == null ? "" : rs.getString("To_BankName"));
				obj.setToActNumber(rs.getString("To_ActNumber") == null ? "" : rs.getString("To_ActNumber"));
				obj.setToBranch(rs.getString("To_Branch") == null ? "" : rs.getString("To_Branch"));
				obj.setFromIBANNo(rs.getString("From_IBANNo") == null ? "" : rs.getString("From_IBANNo"));
				obj.setToIBANNo(rs.getString("To_IBANNo") == null ? "" : rs.getString("To_IBANNo"));
				obj.setToTRANSCode(rs.getString("To_TRANSCode") == null ? "" : rs.getString("To_TRANSCode"));
				obj.setAttnName(rs.getString("Attn_Name") == null ? "" : rs.getString("Attn_Name"));
				obj.setAttnPosition(rs.getString("Attn_Position") == null ? "" : rs.getString("Attn_Position"));
			}
		}

		catch (Exception ex) {
			logger.error("error in HBAData---getBankInfoForBankTransfer-->", ex);
		}
		return obj;
	}
	
	public int updateBankTransfer(BankTransferModel obj, int webUserID) {
		int result = 0;
		HBAQueries query = new HBAQueries();
		try {
			result = db.executeUpdateQuery(query.updateBankTransfer(obj, webUserID));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateBankTransfer-->", ex);
		}
		return result;
	}
	
	public int updateBankTransferInfo(BankTransferModel obj) {
		int result = 0;

		HBAQueries query = new HBAQueries();
		try {
			 
			result = db.executeUpdateQuery(query.updateBankTransferInfo(obj));
		} catch (Exception ex) {
			logger.error("error in HBAData---updateBankTransferInfo-->", ex);
		}
		return result;
	}
	
	public void upadteLogo(InputStream  logo)
	{
		try
		{
			PreparedStatement pstmt;
			String  query = ("update companySettings set logo1=?");
			
			pstmt =db.getConnection().prepareStatement(query);
			 pstmt.setBinaryStream(1, logo); 
			 pstmt.executeUpdate();
            
           
			//String sql="update companySettings set logo1='" + logo+"'";
			//db.executeUpdateQuery(sql);
		}
		catch (Exception ex) {
			logger.error("error in HBAData---upadteLogo-->", ex);
		}
	}
}
