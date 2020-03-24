package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

import model.CompanyDBModel;
import model.VendorModel;

public class VendorsData {
	
	private Logger logger = Logger.getLogger(HBAData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	
	public VendorsData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
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
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HBAData---Init-->" , ex);
		}
	}
	
	
	public List<VendorModel> getVendorList(String status)
	{
		List<VendorModel> lst=new ArrayList<VendorModel>();
		
		VendorsQueries query=new VendorsQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getVendorsQuery(status));
			while(rs.next())
			{
				VendorModel obj=new VendorModel();
				obj.setVend_Key(rs.getInt("Vend_Key"));				
				obj.setName(rs.getString("name")==null?"":rs.getString("name"));
				obj.setArName(rs.getString("ArName")==null?"":rs.getString("ArName"));
				obj.setFullName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setAccountName(rs.getString("ActName")==null?"":rs.getString("ActName"));
				obj.setAccountNumber(rs.getString("actnumber")==null?"":rs.getString("actnumber"));
				obj.setAltPhone(rs.getString("AltPhone")==null?"":rs.getString("AltPhone"));
				obj.setAltContact(rs.getString("ALTCONTACT")==null?"":rs.getString("ALTCONTACT"));
				obj.setSkypeId(rs.getString("SkypeID")==null?"":rs.getString("SkypeID"));
				obj.setBankName(rs.getString("BankName")==null?"":rs.getString("BankName"));
				obj.setBranchName(rs.getString("Branch")==null?"":rs.getString("Branch"));
				obj.setIbanNo(rs.getString("IBANNo")==null?"":rs.getString("IBANNo"));
				obj.setSalutation(rs.getString("salutation")==null?"":rs.getString("salutation"));
				obj.setcC(rs.getString("cc")==null?"":rs.getString("cc"));
				obj.setWebSite(rs.getString("website")==null?"":rs.getString("website"));
				obj.setFirstName(rs.getString("firstname")==null?"":rs.getString("firstname"));
				obj.setMiddleName(rs.getString("middlename")==null?"":rs.getString("middlename"));
				obj.setLastName(rs.getString("lastname")==null?"":rs.getString("lastname"));
				//obj.setTimeCreated(df.parse(sdf.format(rs.getDate("TimeCreated"))));
				obj.setIsActive(rs.getString("IsActive")==null?"":rs.getString("IsActive"));
				obj.setBillAddress1(rs.getString("BillAddress1")==null?"":rs.getString("BillAddress1"));
				obj.setPhone(rs.getString("phone")==null?"":rs.getString("phone"));				
				obj.setFax(rs.getString("fax")==null?"":rs.getString("fax"));
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setContact(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setWebSite(rs.getString("WebSite")==null?"":rs.getString("WebSite"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs")==null?"":rs.getString("PrintChequeAs"));
				obj.setNote(rs.getString("Note")==null?"":rs.getString("Note"));
				double TotalBalance=rs.getDouble("TotalBalance");
				double balance=rs.getDouble("balance");
				obj.setBalance(dcf.parse(dcf.format(balance)).doubleValue());
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance)).doubleValue());
				if(obj.getIsActive().equalsIgnoreCase("Y"))
				{
					obj.setIsActive("Active");
				}
				else{
					obj.setIsActive("INActive");
				}
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getVendorList-->" , ex);
		}
		return lst;
	}
	public VendorModel getVendorByKey(int vendKey)
	{
		VendorModel obj=new VendorModel();
		VendorsQueries query=new VendorsQueries();
		ResultSet rs = null;
		try
		{
			rs=db.executeNonQuery(query.getVendorByKeyQuery(vendKey));
			while(rs.next())
			{				
				obj.setVend_Key(rs.getInt("Vend_Key"));			
				obj.setName(rs.getString("name")==null?"":rs.getString("name"));
				obj.setArName(rs.getString("ArName")==null?"":rs.getString("ArName"));
				obj.setFullName(rs.getString("fullname")==null?"":rs.getString("fullname"));
				obj.setAccountName(rs.getString("ActName")==null?"":rs.getString("ActName"));
				obj.setAccountNumber(rs.getString("actnumber")==null?"":rs.getString("actnumber"));
				obj.setAltPhone(rs.getString("AltPhone")==null?"":rs.getString("AltPhone"));
				obj.setAltContact(rs.getString("ALTCONTACT")==null?"":rs.getString("ALTCONTACT"));
				obj.setSkypeId(rs.getString("SkypeID")==null?"":rs.getString("SkypeID"));
				obj.setBankName(rs.getString("BankName")==null?"":rs.getString("BankName"));
				obj.setBranchName(rs.getString("Branch")==null?"":rs.getString("Branch"));
				obj.setIbanNo(rs.getString("IBANNo")==null?"":rs.getString("IBANNo"));
				obj.setSalutation(rs.getString("salutation")==null?"":rs.getString("salutation"));
				obj.setcC(rs.getString("cc")==null?"":rs.getString("cc"));
				obj.setWebSite(rs.getString("website")==null?"":rs.getString("website"));
				obj.setFirstName(rs.getString("firstname")==null?"":rs.getString("firstname"));
				obj.setMiddleName(rs.getString("middlename")==null?"":rs.getString("middlename"));
				obj.setLastName(rs.getString("lastname")==null?"":rs.getString("lastname"));
				obj.setCompanyName(rs.getString("companyName")==null?"":rs.getString("companyName"));
				obj.setIsActive(rs.getString("IsActive")==null?"":rs.getString("IsActive"));
				obj.setBillAddress1(rs.getString("BillAddress1")==null?"":rs.getString("BillAddress1"));
				obj.setPhone(rs.getString("phone")==null?"":rs.getString("phone"));				
				obj.setFax(rs.getString("fax")==null?"":rs.getString("fax"));
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setContact(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setWebSite(rs.getString("WebSite")==null?"":rs.getString("WebSite"));
				obj.setPrintChequeAs(rs.getString("PrintChequeAs")==null?"":rs.getString("PrintChequeAs"));
				obj.setNote(rs.getString("Note")==null?"":rs.getString("Note"));	
				double TotalBalance=rs.getDouble("TotalBalance");
				double balance=rs.getDouble("balance");
				obj.setBalance(dcf.parse(dcf.format(balance)).doubleValue());
				obj.setTotalBalance(dcf.parse(dcf.format(TotalBalance)).doubleValue());
				obj.setVatRegNo(rs.getString("VAT_REGNO") == null ? "" : rs
						.getString("VAT_REGNO"));
			}
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getVendorByKey-->" , ex);
		}
		return obj;
	}
	public int UpdateVendorData(VendorModel obj)
	{
		int result=0;
	
		VendorsQueries query=new VendorsQueries();		
		try 
		{			
			result=db.executeUpdateQuery(query.UpdateVendorQuery(obj));		
			db.executeUpdateQuery(query.vendorListUpdateQbListQuery(obj,obj.getVend_Key()));
			result=1;
		}
		catch (Exception ex) {
			logger.error("error in HBAData---UpdateVendorData-->" , ex);
		}
		return result;
		
	}
	public int addVendorData(VendorModel obj)
	{
		int result=0;
	
		VendorsQueries query=new VendorsQueries();		
		try 
		{			
			int newID=getMaxID("QBLists", "recNo");
			obj.setVend_Key(newID);
			result=db.executeUpdateQuery(query.addVendorQuery(obj));	
			vendorListInsertQbListQuery(obj,obj.getVend_Key());
			result=1;
		}
		catch (Exception ex) {
			logger.error("error in HBAData---addVendorData-->" , ex);
		}
		return result;
		
	}
	
	public int getMaxID(String tableName,String fieldName)
	{
		int result=0;		
		VendorsQueries query=new VendorsQueries();	
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in HBAData---getMaxID-->" , ex);
		}	
		return result;
	}
	
	public int vendorListInsertQuery(VendorModel customerModel,int recNo)
	{
		int result=0;
		
		VendorsQueries query=new VendorsQueries();		
		try 
		{		
			customerModel.setTimeCreated(new Date());
			customerModel.setIsActive("Y");
			String str=query.vendorListInsertQuery(customerModel,recNo);
			result=db.executeUpdateQuery(str);			
		}
		catch (Exception ex) 
		{
			logger.error("error in HBAData---vendorListInsertQuery-->" , ex);
		}
		return result;
	}
	
	public int vendorListInsertQbListQuery(VendorModel customerModel,int recNo)
	{
		int result=0;
		
		VendorsQueries query=new VendorsQueries();		
		try 
		{		
			customerModel.setTimeCreated(new Date());
			String str=query.vendorListInsertQbListQuery(customerModel,recNo);
			result=db.executeUpdateQuery(str);			
		}
		catch (Exception ex) 
		{
			logger.error("error in HBAData---vendorListInsertQbListQuery-->" , ex);
		}
		return result;
	}
	

}
