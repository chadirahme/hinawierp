package common;


import hba.HBAQueries;


import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CashInvoiceModel;
import model.CompanyDBModel;
import model.CustomerModel;
import model.CutomerSummaryReport;
import model.SalesRepModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import admin.TasksModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ReminderData {
	
	private Logger logger = Logger.getLogger(ReminderData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	  //CustomerData customerData;
	
	
	public ReminderData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=null;
			if(sess!=null && sess.getAttribute("Authentication")!=null)
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HBAQueries query=new HBAQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
				  
				 // customerData=new CustomerData();
				  
			}
			else
			{
				CompanyDBModel objNew = new CompanyDBModel();
			 	objNew.setUserip("hinawi2.dyndns.org");
			 	objNew.setDbname("ECActualERPData");
			 	objNew.setDbuser("admin");
			 	objNew.setDbpwd("admin123");
				db = new SQLDBHandler(objNew);
				 // customerData=new CustomerData();
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in ReminderData---Init-->" , ex);
		}
	}
	
	
	
	
	public List<TasksModel> getAllTask()
	{
	
		List<TasksModel> list=new ArrayList<TasksModel>();
		ReminderQuerries query=new ReminderQuerries();
		ResultSet rs = null;
		
		try 
		{
			rs=db.executeNonQuery(query.getAllTask());
			while(rs.next())
			{	
				TasksModel obj=new TasksModel();
				obj.setTaskid(rs.getInt("taskID"));
				obj.setCreationDate(rs.getDate("createDate"));
				obj.setCreationDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("createDate")));
				obj.setCreatedUserID(rs.getInt("createdUser"));
				obj.setTaskNumber(rs.getString("taskNo")==null?"":rs.getString("taskNo"));
				obj.setTaskTypeId(rs.getInt("tasktype"));
				obj.setTaskName(rs.getString("taskName")==null?"":rs.getString("taskName"));
				obj.setTaskStep(rs.getString("steps")==null?"":rs.getString("steps"));
				obj.setPrviousTaskLinkId(rs.getInt("linkid"));
				obj.setCustomerRefKey(rs.getInt("customerrefKey"));
				obj.setProjectKey(rs.getInt("projectrefKey"));
				obj.setSreviceId(rs.getInt("servicerefKey"));
				obj.setEmployeeid(rs.getInt("assignedUser"));
				obj.setCcEmployeeKey(rs.getInt("ccemployeeKey"));
				obj.setPriorityRefKey(rs.getInt("priorityrefKey"));
				obj.setEstimatatedNumber(rs.getDouble("estTime"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setActualNumber(rs.getDouble("actualTime"));
				obj.setStatusKey(rs.getInt("status"));
				obj.setPriorityNAme(rs.getString("TaskPriorityStr")==null?"":rs.getString("TaskPriorityStr"));
				obj.setTaskType(rs.getString("TaskTYpeStr")==null?"":rs.getString("TaskTYpeStr"));
				obj.setStatusName(rs.getString("TaskStatusStr")==null?"":rs.getString("TaskStatusStr"));
				obj.setProjectName(rs.getString("project_Name")==null?"":rs.getString("project_Name"));
				obj.setEmployeeName(rs.getString("employeename")==null?"":rs.getString("employeename"));
				obj.setHoursOrDays(rs.getString("hourOrDays")==null?"":rs.getString("hourOrDays"));
				obj.setServiceName(rs.getString("serviceNAme")==null?"":rs.getString("serviceNAme"));
				obj.setCcEmployeeName(rs.getString("employeeCcname")==null?"":rs.getString("employeeCcname"));
				obj.setExpectedDatetofinish(rs.getDate("expectedDateTofinsh"));
				obj.setClientType(rs.getString("customerType")==null?"":rs.getString("customerType"));
				obj.setCreatedAutommaticTask(rs.getString("createdAutomaticFeedback")==null?"":rs.getString("createdAutomaticFeedback"));
				obj.setCustomerNamefromFeedback(rs.getString("customerNameFeedback")==null?"":rs.getString("customerNameFeedback"));
				//obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				if(obj.getClientType().equalsIgnoreCase("P"))
				{
					obj.setCustomerNAme(rs.getString("prospectiveName")==null?"Custommer Not Mapped":rs.getString("prospectiveName"));
					obj.setClientTypeFullName("Prospective"); 
					obj.setCustomerAddress(rs.getString("propsAdress")==null?"No Address":rs.getString("propsAdress"));
					obj.setCustomerExpiryDate("No Date");
				}
				else
				{
					obj.setCustomerNAme(rs.getString("fullname")==null?"Custommer Not Mapped":rs.getString("fullname"));
					obj.setClientTypeFullName("Customer");
					if(rs.getDate("customerExpiry")!=null)
					obj.setCustomerExpiryDate(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("customerExpiry")));
					else
					obj.setCustomerExpiryDate("No Date");
					obj.setCustomerAddress(rs.getString("customerAddress")==null?"No Address":rs.getString("customerAddress"));
				}
				
				if(rs.getDate("expectedDateTofinsh")!=null)
				obj.setExpectedDatetofinishStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("expectedDateTofinsh")));
				else
					obj.setExpectedDatetofinishStr("");	
				
				if(rs.getDate("reminderDate")!=null)
					obj.setReminderDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("reminderDate")));
					else
					obj.setReminderDateStr("");	
				obj.setReminderDate(rs.getDate("reminderDate"));
				obj.setFeedbackKey(rs.getInt("feedBackKey"));
				
				if(obj.getFeedbackKey()>0)
				{
					obj.setFeedbackNo(rs.getString("enquiry_no")==null?"":rs.getString("enquiry_no"));
					obj.setHideFeedbackButton(true);
				}
				else
				{
					obj.setFeedbackNo("Not From FeedBack");
					obj.setHideFeedbackButton(false);
				}
				obj.setRemindIn(rs.getDouble("toBeReminderIn"));
				obj.setPreviossTaskName("");
				obj.setEmail(rs.getString("details"));
				list.add(obj);
			}
		}
		
		catch (Exception ex) {
			logger.error("error in ReminderData---getAllTask-->" , ex);
		}
		return list;
	}
	
	
	
	
	
	
	
	
	public List<CutomerSummaryReport> getCustomerBalanceForReminder(int salesRep)
	{
		List<CutomerSummaryReport> lst=new ArrayList<CutomerSummaryReport>();
		ReminderQuerries query=new ReminderQuerries();		
		ResultSet rs = null;
		double tempBalance=0; 
		double tempTotal=0; 
		double tempCreditTotal=0;
		double tempDebitTotal=0;
		int tempCustomerKey=0;
		String customerName="";
		CutomerSummaryReport objTotal=new CutomerSummaryReport();
		try 
		{		
			rs=db.executeNonQuery(query.getCustomerBalanceForReminder(salesRep));
			boolean hasNext = rs.next();
			while(hasNext)
			{
				CutomerSummaryReport obj=new CutomerSummaryReport();
				obj.setRec_no(rs.getInt("recno"));
				obj.setCustKey(rs.getInt("cust_key"));
				obj.setEnityName(rs.getString("fullName")==null?"":rs.getString("fullName"));
				if(rs.getDate("cuscontractExpiry")!=null)
				obj.setCustContractExpiry(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("cuscontractExpiry")));
				else
				obj.setCustContractExpiry("No Expiry Set");
				//customerName=obj.getEnityName();
				obj.setFromDate(rs.getDate("txnDate"));
				if(rs.getDouble("debit")<0)
				obj.setDebit(-(rs.getDouble("debit")));	
				else
				obj.setDebit(rs.getDouble("debit"));
				
				if(rs.getDouble("credit")<0)
				obj.setCredit(-(rs.getDouble("credit")));	
				else
				obj.setCredit(rs.getDouble("credit"));
				obj.setRefranceNumber(rs.getString("recNO")==null?"":rs.getString("recNO"));
				obj.setAcountName(rs.getString("itemOrAccountName")==null?"":rs.getString("itemOrAccountName"));
				obj.setTxnType(rs.getString("trasType")==null?"":rs.getString("trasType"));
				obj.setTxnDate(rs.getDate("txndate")==null?"":new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("txndate")));
				if(rs.getString("trasType").equalsIgnoreCase("J"))
				{
					obj.setTxnType("Jouneral Voucher");
				}
				else if(rs.getString("trasType").equalsIgnoreCase("R")) 
				{
					obj.setTxnType("Receipt Voucher");
				}
			
				
				//total amount calculation
				if(tempCustomerKey==0)
				{
					tempCustomerKey=rs.getInt("cust_key");
				}
				if(tempCustomerKey!=0 && tempCustomerKey==rs.getInt("cust_key"))
				{
					objTotal=new CutomerSummaryReport();
					objTotal.setEnityName(obj.getEnityName());
					if(obj.getCustContractExpiry()!=null)
						objTotal.setCustContractExpiry(obj.getCustContractExpiry());
					else
						objTotal.setCustContractExpiry("No Expiry Set");
					objTotal.setTxnType("Total");
					customerName=obj.getEnityName();
					tempTotal=tempTotal+obj.getAmount();
					tempCreditTotal=tempCreditTotal+obj.getCredit();
					tempDebitTotal=tempDebitTotal+obj.getDebit();
					if(rs.getString("debitFlag").equalsIgnoreCase("Y"))
					{
						obj.setAmount(rs.getDouble("debit"));
						tempBalance=tempBalance+obj.getAmount();
						obj.setBalance(tempBalance);
					}
					else
					{
						if(rs.getDouble("credit")>0)
						obj.setAmount(-(rs.getDouble("credit")));
						else
						obj.setAmount(+(rs.getDouble("credit")));	
						
						tempBalance=tempBalance+obj.getAmount();
						obj.setBalance(tempBalance);
					}
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);
					
					
				}
				else if(tempCustomerKey!=0 && tempCustomerKey!=rs.getInt("cust_key"))
				{
					objTotal=new CutomerSummaryReport();
					if(obj.getCustContractExpiry()!=null)
						objTotal.setCustContractExpiry(obj.getCustContractExpiry());
					else
						objTotal.setCustContractExpiry("No Expiry Set");
					objTotal.setBalance(tempBalance);
					objTotal.setDebit(tempDebitTotal);
					objTotal.setCredit(tempCreditTotal);
					objTotal.setCustKey(tempCustomerKey);
					objTotal.setEnityName(customerName);
					objTotal.setTxnType("Total");
					lst.add(objTotal);
					tempTotal=0;
					tempCreditTotal=0;
					tempDebitTotal=0;
					tempCreditTotal=rs.getDouble("credit");
					tempDebitTotal=rs.getDouble("debit");
					tempBalance=0;
					customerName="";
					customerName=rs.getString("fullName")==null?"":rs.getString("fullName");
					if(rs.getString("debitFlag").equalsIgnoreCase("Y"))
					{
						obj.setAmount(rs.getDouble("debit"));
						tempBalance=tempBalance+obj.getAmount();
						obj.setBalance(tempBalance);
					}
					else
					{
						if(rs.getDouble("credit")>0)
						obj.setAmount(-(rs.getDouble("credit")));
						else
						obj.setAmount(+(rs.getDouble("credit")));	
						
						tempBalance=tempBalance+obj.getAmount();
						obj.setBalance(tempBalance);
					}
					tempCustomerKey=rs.getInt("cust_key");
				}
				//lst.add(obj);
				hasNext = rs.next();
				if(!hasNext)
				{
					lst.add(objTotal);
				}
			
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in ReminderData---getCustomerBalanceForReminder-->" , ex);
		}
		
		return lst;
		
	}
	
	
	

	public List<SalesRepModel> getSalesRepNamesForReminder(String status)
	{
		List<SalesRepModel> lst=new ArrayList<SalesRepModel>();
		ReminderQuerries query=new ReminderQuerries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getSalesRepNamesForReminder(status));
			while(rs.next())
			{
				SalesRepModel obj=new SalesRepModel();
				obj.setSalesRepAsCustomerEmail(rs.getString("customeremail")==null?"":rs.getString("customeremail"));
				obj.setSalesRepAsEmployeeEmail(rs.getString("employeeEmail")==null?"":rs.getString("employeeEmail"));					
				obj.setSalesRepAsVendorEmail(rs.getString("vendorEmail")==null?"":rs.getString("vendorEmail"));
				obj.setSalesRepName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setQbListKey(rs.getInt("recNo"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ReminderData---getSalesRepNamesForReminder-->" , ex);
		}
		return lst;
	}
	
	
	public List<CashInvoiceModel> getQuotationForReminder(String custKeys)
	{
		
		
		ReminderQuerries query=new ReminderQuerries();
		ResultSet rs = null;
		List<CashInvoiceModel> list=new ArrayList<CashInvoiceModel>();
		try 
		{
			rs=db.executeNonQuery(query.getQuotationForReminder(custKeys));
			while(rs.next())
			{
				CashInvoiceModel obj=new CashInvoiceModel();
				obj.setRecNo(rs.getInt("recNo"));
				obj.setTxnId(rs.getString("txnId"));
				obj.setCustomerRefKey(rs.getInt("customerRefKey"));
				obj.setClassRefKey(rs.getInt("classRefKey"));
				obj.setAccountRefKey(rs.getInt("ARAccountRefKey"));
				obj.setTemplateRefKey(rs.getInt("templateRefKey"));
				obj.setInvoiceDateStr(new SimpleDateFormat("dd-MM-yyyy").format(rs.getDate("txnDate")));
				obj.setInvoiceSaleNo(rs.getString("refNUmber"));
				obj.setClientType(rs.getString("clientType")==null?"":rs.getString("clientType"));
				obj.setBillAddress1(rs.getString("billaddress1")==null?"":rs.getString("billaddress1"));
				obj.setBillAddress2(rs.getString("billaddress2")==null?"":rs.getString("billaddress2"));
				obj.setBillAddress3(rs.getString("billaddress3")==null?"":rs.getString("billaddress3"));
				obj.setBillAddress4(rs.getString("billaddress4")==null?"":rs.getString("billaddress4"));
				obj.setBillAddress5(rs.getString("billaddress5")==null?"":rs.getString("billaddress5"));
				obj.setBillAddressCity(rs.getString("billaddresscity")==null?"":rs.getString("billaddresscity"));
				obj.setBillAddressState(rs.getString("billaddressState")==null?"":rs.getString("billaddressState"));
				obj.setBillAddressPostalCode(rs.getString("billaddressPostalCode")==null?"":rs.getString("billaddressPostalCode"));	
				obj.setBillAddressCountry(rs.getString("billaddressCountry")==null?"":rs.getString("billaddressCountry"));
				obj.setBillAddressNote(rs.getString("billAddressNote")==null?"":rs.getString("billAddressNote"));
				obj.setShipAddress1(rs.getString("shipaddress1")==null?"":rs.getString("shipaddress1"));	
				obj.setShipAddress2(rs.getString("shipaddress2")==null?"":rs.getString("shipaddress2"));
				obj.setShipAddress3(rs.getString("shipaddress3")==null?"":rs.getString("shipaddress3"));
				obj.setShipAddress4(rs.getString("shipaddress4")==null?"":rs.getString("shipaddress4"));
				obj.setShipAddress5(rs.getString("shipaddress5")==null?"":rs.getString("shipaddress5"));
				obj.setShipAddressCity(rs.getString("shipaddressCity")==null?"":rs.getString("shipaddressCity"));
				obj.setShipAddressState(rs.getString("shipaddressState")==null?"":rs.getString("shipaddressState"));
				obj.setShipAddressPostalCode(rs.getString("shipaddressPostalCode")==null?"":rs.getString("shipaddressPostalCode"));
				obj.setShipAddressCountry(rs.getString("shipaddressCountry")==null?"":rs.getString("shipaddressCountry"));
				obj.setShipAddressNote(rs.getString("shipaddressNote")==null?"":rs.getString("shipaddressNote"));	
				obj.setSalesRefKey(rs.getInt("salesRefKey"));
				obj.setTermRefKey(rs.getInt("termsRefKey"));
				obj.setPoNumber(rs.getString("ponumber"));
				obj.setDueDate(rs.getDate("duedate"));
				obj.setfOB(rs.getString("fob"));
				obj.setShipDate(rs.getDate("shipDate"));
				obj.setShipMethodRefKey(rs.getInt("shipMethodRefKey"));
				obj.setItemSalesTaxRefKey(rs.getInt("itemSalesTaxRefKey"));
				obj.setMemo(rs.getString("memo")==null?"":rs.getString("memo"));
				obj.setCustomerMsgRefKey(rs.getInt("customermsgRefKey"));
				obj.setIsToBePrinted(rs.getString("istobePrinted")==null?"":rs.getString("istobePrinted"));
				obj.setIsToEmailed(rs.getString("istoEmailed")==null?"":rs.getString("istoEmailed"));
				obj.setIsTaxIncluded(rs.getString("isTaxIncluded")==null?"":rs.getString("isTaxIncluded"));
				obj.setOther(rs.getString("other")==null?"":rs.getString("other"));
				obj.setAmount(rs.getDouble("amount"));
				obj.setCustomField1(rs.getString("customField1")==null?"":rs.getString("customField1"));
				obj.setCustomField2(rs.getString("customField2")==null?"":rs.getString("customField2"));
				obj.setCustomField3(rs.getString("customField3")==null?"":rs.getString("customField3"));
				obj.setCustomField4(rs.getString("customField4")==null?"":rs.getString("customField4"));
				obj.setCustomField5(rs.getString("customField5")==null?"":rs.getString("customField5"));
				//obj.setInvoiceType(rs.getString("InvoiceType")==null?"":rs.getString("InvoiceType"));
				obj.setStatus(rs.getString("statusdesc")==null?"":rs.getString("statusdesc"));
				obj.setDescriptionHIDE(rs.getString("descriptionHIde")==null?"":rs.getString("descriptionHIde"));
				obj.setQtyHIDE(rs.getString("qtyHide")==null?"":rs.getString("qtyHide"));
				obj.setRateHIDE(rs.getString("rateHide")==null?"":rs.getString("rateHide"));
				obj.setSendVia("");
				obj.setSendViaReffKey(rs.getInt("sendviaReffKey"));
				obj.setRemindMeDate(rs.getDate("RemindDate"));
				obj.setRemindMedays(rs.getInt("RemindDays"));
				obj.setRemingMeFalg(rs.getString("RemindFlag")==null?"":rs.getString("RemindFlag"));
				obj.setAttachemnet(rs.getString("Attachment")==null?"":rs.getString("Attachment"));
				obj.setLetterTemplate(rs.getString("LetterTemplate")==null?"":rs.getString("LetterTemplate"));
				obj.setShipToAddress(rs.getString("ShipToAddress")==null?"":rs.getString("ShipToAddress"));
			/*	obj.setLineMemo(rs.getString("linememo")==null?"":rs.getString("linememo"));
				obj.setQuantity(  rs.getDouble("quantity"));
				obj.setLineMemo(rs.getString("linememo")==null?"":rs.getString("linememo"));
				obj.setRatePercent(rs.getDouble("ratePercent"));
				obj.setAvgCost(rs.getDouble("avgcost"));
				obj.setLineAmount(rs.getDouble("lineAmount"));
				obj.setRate(rs.getDouble("rate"));*/
				
				if(obj.getClientType().equalsIgnoreCase("P"))
				{
					obj.setCustomerName(rs.getString("prospectiveName")==null?"":rs.getString("prospectiveName"));
				}
				else
				{
					obj.setCustomerName(rs.getString("customerName")==null?"":rs.getString("customerName"));
				}
				
				/*obj.setItemName(rs.getString("itemName")==null?"":rs.getString("itemName"));
				obj.setItemName(rs.getString("description")==null?"":rs.getString("description"));*/
				list.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getQuotationReport-->" , ex);
		}
		return list;
	}
	
	

}
