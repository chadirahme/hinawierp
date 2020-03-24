package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.AccountsModel;
import model.CompanyDBModel;
import model.OtherNamesModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

public class OtherNameListData {
	
	private Logger logger = Logger.getLogger(OtherNameListData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("");
	
	public OtherNameListData()
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
			logger.error("error in OtherNameListData---Init-->" , ex);
		}
	}
	
	public String getNameFromQbListForNameValidation(String valididationName)
	{
		
		OtherNameListQueries query=new OtherNameListQueries();
		ResultSet rs = null;
		String name="";
		try 
		{
			rs=db.executeNonQuery(query.getNameFromQbListForNameValidation(valididationName));
			while(rs.next())
			{
			   name = rs.getString("TypeName");
			}
			
		}
		catch (Exception ex) {
			logger.error("error in OtherNameListData---getNameFromQbListForNameValidation-->" , ex);
		}
		return name;
	}

	
	public OtherNamesModel getOtherNameListByID(int OtherNamekey)
	{
		OtherNamesModel obj=new OtherNamesModel();
		OtherNameListQueries query=new OtherNameListQueries();
		ResultSet rs = null;
		ResultSet rsBank = null;
		String S1,S2;
		Pattern pattern = Pattern.compile("(\\·)(.*?)(\\:)");
		List<String> listMatches = new ArrayList<String>();
		 
		DecimalFormat dcf = new DecimalFormat( "#,###,###,##0.00" );
		try 
		{
			rs=db.executeNonQuery(query.getOtherNameListByID(OtherNamekey));
			while(rs.next())
			{
				
				obj.setCustkey(rs.getInt("othNam_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name")==null?"":rs.getString("name"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName")==null?"":rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				S1=rs.getString("BillAddress1")==null?"":rs.getString("BillAddress1");
				S2=rs.getString("BillAddress2")==null?"":rs.getString("BillAddress2");
				obj.setBillCountry(S1+S2);
				obj.setPhone(rs.getString("phone")==null?"":rs.getString("phone"));
				obj.setAltphone(rs.getString("altphone")==null?"":rs.getString("altphone"));
				obj.setFax(rs.getString("fax")==null?"":rs.getString("fax"));
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setContact(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setArName(rs.getString("ArName")==null?"":rs.getString("ArName"));
				obj.setAccountNumber(rs.getString("accountno")==null?"":rs.getString("accountno"));
				obj.setContactPerson(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setAlternateContact(rs.getString("ALTCONTACT")==null?"":rs.getString("ALTCONTACT"));
				obj.setSkypeID(rs.getString("SkypeID")==null?"":rs.getString("SkypeID"));
				obj.setAccountName(rs.getString("ActName")==null?"":rs.getString("ActName"));
				obj.setBankName(rs.getString("BankName")==null?"":rs.getString("BankName"));
				obj.setBranchName(rs.getString("BranchName")==null?"":rs.getString("BranchName"));
				obj.setbBANNumber(rs.getString("IBANNo")==null?"":rs.getString("IBANNo"));
				obj.setPrintChequeAs(rs.getString("fullName")==null?"":rs.getString("fullName"));
				
			}
		}
		catch (Exception ex) {
			logger.error("error in OtherNameListData---getOtherNameListByID-->" , ex);
		}
		return obj;
	}
	
	
	
	public int updateOtherNameList(OtherNamesModel obj)
	{
		int result=0;
		OtherNameListQueries query=new OtherNameListQueries();	
		try 
		{			
			result=db.executeUpdateQuery(query.updateOtherNameList(obj));	
			db.executeUpdateQuery(query.otherNamesListUpdateQbListQuery(obj, obj.getCustkey()));
			
		}
		catch (Exception ex) {
			logger.error("error in OtherNameListData---updateOtherNameList-->" , ex);
		}
		return result;
		
	}
	
	
	public List<OtherNamesModel> getOtherNameList(String status,String sortBy)
	{
		List<OtherNamesModel> lstOtherNames=new ArrayList<OtherNamesModel>();		
		OtherNameListQueries query=new OtherNameListQueries();	
		ResultSet rs = null;
		String S1,S2;
		try 
		{
			rs=db.executeNonQuery(query.getOtherListQuery(status,sortBy));
			while(rs.next())
			{
				OtherNamesModel obj=new OtherNamesModel();
				obj.setCustkey(rs.getInt("othNam_key"));
				obj.setListid(rs.getString("listid"));
				obj.setName(rs.getString("name")==null?"":rs.getString("name"));
				obj.setCreatedDate(rs.getDate("TimeCreated"));
				obj.setFullName(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName")==null?"":rs.getString("companyName"));
				obj.setIsactive(rs.getString("isactive")==null?"":rs.getString("isactive"));
				S1=rs.getString("BillAddress1")==null?"":rs.getString("BillAddress1");
				S2=rs.getString("BillAddress2")==null?"":rs.getString("BillAddress2");
				obj.setBillCountry(S1+S2);
				obj.setPhone(rs.getString("phone")==null?"":rs.getString("phone"));
				obj.setAltphone(rs.getString("altphone")==null?"":rs.getString("altphone"));
				obj.setFax(rs.getString("fax")==null?"":rs.getString("fax"));
				obj.setEmail(rs.getString("email")==null?"":rs.getString("email"));
				obj.setContact(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setArName(rs.getString("ArName")==null?"":rs.getString("ArName"));
				obj.setAccountNumber(rs.getString("accountno")==null?"":rs.getString("accountno"));
				obj.setContactPerson(rs.getString("contact")==null?"":rs.getString("contact"));
				obj.setAlternateContact(rs.getString("ALTCONTACT")==null?"":rs.getString("ALTCONTACT"));
				obj.setSkypeID(rs.getString("SkypeID")==null?"":rs.getString("SkypeID"));
				obj.setAccountName(rs.getString("ActName")==null?"":rs.getString("ActName"));
				obj.setBankName(rs.getString("BankName")==null?"":rs.getString("BankName"));
				obj.setBranchName(rs.getString("BranchName")==null?"":rs.getString("BranchName"));
				obj.setbBANNumber(rs.getString("IBANNo")==null?"":rs.getString("IBANNo"));
				obj.setPrintChequeAs(rs.getString("fullName")==null?"":rs.getString("fullName"));
				if(obj.getIsactive().equalsIgnoreCase("Y"))
				{
					obj.setIsactive("Active");
				}
				else{
					obj.setIsactive("INActive");
				}
				lstOtherNames.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in HBAData---getCustomerList-->" , ex);
		}
		return lstOtherNames;
	}
	
	
	

}
