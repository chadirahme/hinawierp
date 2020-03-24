package common;

//import hba.CustomerData;
import hba.HBAQueries;
//import hba.TaskQueery;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ReminderQuerries {
	
	
	
	StringBuffer query;
	private Logger logger = Logger.getLogger(ReminderQuerries.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	WebusersModel dbUser=null;
	
//	CustomerData customerData;
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	
	public ReminderQuerries()
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
			logger.error("error in ReminderQuerries---Init-->" , ex);
		}
	}
	
		
	public String getAllTask()
	{
		  
		  query=new StringBuffer();		 
		  query.append(" SELECT  tasks.*,feedback.enquiry_no, ");
		  query.append(" HRLISTVALUES.description as TaskTYpeStr, ");
		  query.append(" abc.description as TaskPriorityStr, ");
		  query.append(" def.description as TaskStatusStr, ");
		  query.append(" customerList.fullname,customerList.CUSCONTRACTeXPIRY as customerExpiry ,customerList.shipto as customerAddress,prospectiveList.fullname as prospectiveName,prospectiveList.shipto as propsAdress,projectList.project_Name, ");
		  query.append(" serviceList.description as serviceNAme, ");
		  query.append(" ccemployee.english_first as employeeCcname, ");
		  query.append(" employeeLIst.english_first as employeename,empcontact.details ");
		  query.append(" from tasks LEFT JOIN HRLISTVALUES ON tasks.taskType = HRLISTVALUES.id ");
		  query.append(" LEFT JOIN HRLISTVALUES as abc ON tasks.priorityRefKey = abc.id ");
		  query.append(" LEFT JOIN HRLISTVALUES as def ON tasks.status = def.id ");
		  query.append(" LEFT JOIN Customer as customerList ON tasks.customerrefKey = customerList.cust_key ");
		  query.append(" LEFT JOIN Prospective as prospectiveList ON tasks.customerrefKey = prospectiveList.recNo ");
		  query.append(" LEFT JOIN projectMast as projectList ON tasks.projectrefKey = projectList.project_key ");
		  query.append(" LEFT JOIN HRLISTVALUES as serviceList ON tasks.servicerefKey = serviceList.id ");
		  query.append(" LEFT JOIN empmast as ccemployee ON tasks.ccemployeeKey = ccemployee.emp_key ");
		  query.append(" LEFT JOIN CustomerEnquiry as feedback ON tasks.feedBackKey = feedback.enquiry_Id  ");
		  query.append(" LEFT JOIN empmast as employeeLIst ON tasks.assignedUser = employeeLIst.emp_key "); 
		  query.append(" inner JOIN empcontact  ON tasks.assignedUser = empcontact.emp_key ");
		  query.append(" where  tasks.status not in (11,9818) and empcontact.contact_id=622 ");
		  query.append(" order by  employeeLIst.emp_key,createDate ");
		  return query.toString();
		  
	}
	
	public String getCustomerBalanceForReminder(int salesRepKey)
	{
		 query=new StringBuffer();	
		 
		 query.append("  select  * from ( Select customer.cust_key,customer.cuscontractExpiry,customer.salesrepKey,invoice.recNo,invoice.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,'Invoice' as trasType,COALESCE(invoice.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer  LEFT JOIN invoice ON customer.cust_key = invoice.customerRefkey  LEFT JOIN Accounts ON invoice.ArAccountRefKey=Accounts.rec_no  where  customer.isActive='Y' and  customer.salesrepKey="+salesRepKey+" ");

		 query.append(" union  ALL  select customer.cust_key,customer.cuscontractExpiry,customer.salesrepKey,SalesReceipt.recNo,SalesReceipt.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,'Sales Receipt' as trasType,COALESCE(SalesReceipt.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer  LEFT JOIN SalesReceipt ON customer.cust_key = SalesReceipt.customerRefkey  LEFT JOIN Accounts ON SalesReceipt.depositAccountRefKey=Accounts.rec_no  where   customer.isActive='Y' and  customer.salesrepKey="+salesRepKey+" ");

		 query.append(" union  ALL  select customer.cust_key,customer.cuscontractExpiry,customer.salesrepKey,RVMast.rec_No as recNo,RVMast.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,RVMast.rvorJv as trasType,0 as Debit,COALESCE(RVMast.totalAmount,0) as Credit ,'N' as debitFlag from customer  LEFT JOIN RVMast ON customer.cust_key = RVMast.customerRef_key  LEFT JOIN Accounts ON RVMast.ArAccountRef_Key=Accounts.rec_no  where   customer.isActive='Y' and  customer.salesrepKey="+salesRepKey+" "); 

		 query.append(" 	union  ALL  Select customer.cust_key,customer.cuscontractExpiry,customer.salesrepKey,CreditMemo.recNo,CreditMemo.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,'Credit Memo' as trasType,COALESCE(CreditMemo.amount,0) as Debit,0 as Credit,'Y' as debitFlag from customer LEFT JOIN CreditMemo ON customer.cust_key = CreditMemo.customerRefkey   LEFT JOIN Accounts ON CreditMemo.ArAccountRefKey=Accounts.rec_no  where   customer.isActive='Y' and  customer.salesrepKey="+salesRepKey+"  "); 

		 query.append(" 	union  ALL  Select customer.cust_key,customer.cuscontractExpiry,customer.salesrepKey,JournalEntry.rec_No as recNo,JournalEntry.TxnDate,Accounts.fullname as ItemOrAccountName,customer.fullname,customer.companyName,  'Journal General' as trasType,  (CASE  WHEN JournalLine.dc_flag ='D'  THEN JournalLine.amount  ELSE 0  END)  as Debit,  (CASE  WHEN JournalLine.dc_flag ='C'  THEN JournalLine.amount  ELSE 0  END)  as credit,  (CASE  WHEN JournalLine.dc_flag ='D'  THEN 'Y'  ELSE 'N'  END)  as debitFlag from customer LEFT JOIN JournalLine ON customer.cust_key = JournalLine.entityRef_key     LEFT JOIN JournalEntry ON JournalEntry.rec_no = JournalLine.rec_no    LEFT JOIN Accounts ON JournalLine.AccountRef_Key=Accounts.rec_no  Where EntityRef_Type In('Customer') and  Accounts.fullname in ('Accounts Receivable') and   customer.isActive='Y' and  customer.salesrepKey="+salesRepKey+" "); 

         query.append(" union  ALL  Select customer.cust_key,customer.cuscontractExpiry,customer.salesrepKey,0 as recNo,CustomerAdjustments.TxnDate,'' as ItemOrAccountName,customer.fullname,customer.companyName,'Local Adjustment' as trasType ,  (CASE  WHEN CustomerAdjustments.adjustment_amount > 0  THEN CustomerAdjustments.adjustment_amount  ELSE 0  END)  as Debit,  (CASE  WHEN CustomerAdjustments.adjustment_amount < 0  THEN CustomerAdjustments.adjustment_amount  ELSE 0  END)  as credit,  (CASE  WHEN CustomerAdjustments.adjustment_amount > 0  THEN 'Y'  ELSE 'N'  END)  as debitFlag from customer  LEFT JOIN CustomerAdjustments ON customer.cust_key = CustomerAdjustments.cust_key  where  customer.salesrepKey="+salesRepKey+"  )t3  where t3.debit!=0 or t3.credit!=0  order by cust_key,trasType  ");
         
         return query.toString();
	
	}
	
	
	public String getSalesRepNamesForReminder(String status)
	{
		query=new StringBuffer();	
		
		query.append("  select empcontact.details as employeeEmail,vendor.email as vendorEmail,customer.email as customeremail ,qblists.fullname,qblists.recNo,qblists.hremp_key from salesRep ");
		query.append("  left join qblists on qblists.recNo=salesRep.qblistKey ");
		query.append("  LEFT JOIN empcontact on empcontact.emp_key=qblists.hremp_key "); 
		query.append("  LEFT JOIN vendor  on vendor.vend_key=qblists.recNo ");
		query.append("  LEFT JOIN customer  on customer.cust_key=qblists.recNo where empcontact.contact_id=622 and salesRep.isactive='"+status+"' ");
		
		return query.toString();
	}
	
	
	public String getQuotationForReminder(String custKeys)
	{
		query=new StringBuffer();	
		query.append("select *,customer.fullname as customerName,prospective.fullname as prospectiveName from quotation ");
		query.append("left join customer on customer.cust_key=quotation.customerRefKey ");
		query.append("left join prospective on prospective.recNo=quotation.customerRefKey ");
		query.append("where quotation.customerRefKey in ("+custKeys+") and quotation.status in ('A','C') order by quotation.txnDate desc "); 
		
		return query.toString();
	}
	
}
