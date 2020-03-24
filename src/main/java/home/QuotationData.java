package home;

import hba.HBAQueries;
import hba.ProspectiveData;

import java.io.File;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.*;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;










import db.DBHandler;
import db.SQLDBHandler;

public class QuotationData 
{
	private Logger logger = Logger.getLogger(QuotationData.class);
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public List<CountryModel> getCountries()
	{
		List<CountryModel> lst=new ArrayList<CountryModel>();
		DBHandler db = new DBHandler();
		QuotationQueries query=new QuotationQueries();
		String sqlQuery =query.getCountriesQuery();
		ResultSet rs = null;
		try {
			CountryModel obj=new CountryModel();
			obj.setCountryid(0);
			obj.setCountryname("Select");
			obj.setCountrycode("");
			lst.add(obj);
			
			
			rs=db.executeNonQuery(sqlQuery);
			while(rs.next())
			{
				obj=new CountryModel();
				obj.setCountryid(rs.getInt("countryid"));
				obj.setCountryname(rs.getString("countryname"));
				obj.setCountrycode(rs.getString("countrycode"));
				lst.add(obj);
			}
			
		} catch (Exception ex) {
			logger.error("error in QuotationData---getCountries-->" , ex);
		}
	   return lst;
	}
	
	public List<PositionModel> getPositions()
	{
		List<PositionModel> lst=new ArrayList<PositionModel>();
		DBHandler db = new DBHandler();
		QuotationQueries query=new QuotationQueries();
		String sqlQuery =query.getPositionsQuery();
		ResultSet rs = null;
		try {
			PositionModel obj=new PositionModel();
			obj.setPositionid(0);
			obj.setPositionname("Select");			
			lst.add(obj);
						
			rs=db.executeNonQuery(sqlQuery);
			while(rs.next())
			{
				obj=new PositionModel();
				obj.setPositionid(rs.getInt("positionid"));
				obj.setPositionname(rs.getString("positionname"));				
				lst.add(obj);
			}
			
		} catch (Exception ex) {
			logger.error("error in QuotationData---getPositions-->" , ex);
		}
	   return lst;
	}
	
	public int testINsert(QuotationModel objQuot)
	{
		int quotationid=0;
		DBHandler db=new DBHandler();
		Messagebox.show(objQuot.getCompanyName());
		String sql="INSERT INTO quotation (companyName) VALUES ('" +objQuot.getCompanyName() +  "')";
		try
		{
			//sql="spone";
			//quotationid =db.executeUpdateQuery(sql);	
			//String [] parameters={"aaaaaaaaaaa","bbbbbbbbbbb"};
			//db.callProcedure("spone",parameters);
			quotationid=db.executeUpdateQuery(sql);
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---insertQuotation-->" , ex);
		}
		return quotationid;
	}
	
	public int insertQuotation(QuotationModel objQuot, List<QuotationProductModel> lstProduct,List<QuotationProductModel> lstService,List<QuotationAttachmentModel> lstAtt)
	{
		DBHandler db=new DBHandler();
		//QuotationQueries query=new QuotationQueries();
		//String sqlQuery =query.insertQuotation(objQuot);
		boolean saved = true;
		int quotationid=0;
		try
		{
			//db.executeUpdateQuery(sqlQuery);
			String [] parameters={objQuot.getCompanyName(),objQuot.getContactTitle(),objQuot.getContactName(),String.valueOf(objQuot.getPositionId()),String.valueOf(objQuot.getCountryId()),objQuot.getCity(),
					objQuot.getTelephone1(),objQuot.getTelephone2(),objQuot.getMobile1(),objQuot.getMobile2(),objQuot.getEmail(),objQuot.getWebsite(),objQuot.getHowyouknow(),objQuot.getAuditor(),
					objQuot.getPobox(),objQuot.getSkypeid(),objQuot.getBusinesstype(),objQuot.getCompanysize(),objQuot.getTotalEmployee(),objQuot.getExternalAuditorname(),
					objQuot.getCurrentAccount(),objQuot.getCurrentHr(),objQuot.getOtherSoftware(),objQuot.getSoftwareLanguage(),objQuot.getPartofgroup(),objQuot.getGroupName(),
					objQuot.getFromTime(),objQuot.getToTime(),objQuot.getFromDay(),objQuot.getToDay(),objQuot.getAddress(),objQuot.getCusttype(),objQuot.getCompanytype(),
					objQuot.getNotes(),objQuot.getOtherposition()};
			 		saved=db.callProcedure("addquotation", parameters);
						 			 
			 ResultSet rs= db.executeNonQuery("select Max(quotationid) as qouid from quotation");
			 while(rs.next())
			 {
				 quotationid=rs.getInt("qouid");
			 }
			 
			 for (QuotationProductModel item : lstProduct) 
			 {
				 String [] param={String.valueOf(quotationid),item.getType(),item.getDescription(),item.getNooflicense(),item.getMemo()};
				 saved=db.callProcedure("addproduct", param);
			 }
			 
			 for (QuotationProductModel item : lstService) 
			 {
				 String [] param={String.valueOf(quotationid),item.getType(),item.getDescription(),item.getNooflicense(),item.getMemo()};
				 saved=db.callProcedure("addproduct", param);
			 }
			 
			 for (QuotationAttachmentModel item : lstAtt) 
			 {
				 String [] param={String.valueOf(quotationid),item.getFilename(),item.getFilepath(),item.getSessionid()};
				 saved=db.callProcedure("addattachment", param);
			 }		
			 
			 
			 //Store it in prospective and CRM inquiry
			 
			 ProspectiveData data=new ProspectiveData();
			 
			 int recNo=0;
			 
			 recNo=data.getMaxOfProspective();
			 
			 ProspectiveModel prosp=new ProspectiveModel();
			 
			 prosp.setRecNo(recNo);
			 
			 Calendar c = Calendar.getInstance();		
				
			 prosp.setCreatedDate(df.parse(sdf.format(c.getTime())));
			 
			 prosp.setCompanyName(objQuot.getCompanyName());
			 
			 prosp.setFullname(objQuot.getContactName());
			 
			 prosp.setWebsite(objQuot.getWebsite());
			 
			 prosp.setPhone(objQuot.getTelephone1());
			 
			 prosp.setDirectPhone(objQuot.getTelephone2());
			 
			 prosp.setOther1(objQuot.getMobile1());//mobile 1
			 
			 prosp.setOther2(objQuot.getMobile2());//mobile 2
			 
			 prosp.setEmail(objQuot.getEmail());
			 
			 prosp.setOther3(objQuot.getGroupName());//group Name;
			 
			 prosp.setSkypeId(objQuot.getSkypeid());
			 
			 prosp.setAdress1(objQuot.getAddress());
			 
			 prosp.setCurrentAccountingSoftwaare(objQuot.getCurrentAccount());
			 
			// prosp.setCurrentHrsoftware(objQuot.getCurrentHr());
			 
			 prosp.setNotes(objQuot.getNotes());
			 
			 prosp.setSoftwareLanguages(objQuot.getSoftwareLanguage());
			 
			// prosp.setComapnySize(objQuot.getCompanysize());
			 
			 prosp.setBuisenessType(objQuot.getBusinesstype());
			 
			 prosp.setPobox(objQuot.getPobox());
			 
			// prosp.setTotalComapnyEmployee(objQuot.getTotalEmployee());
			 
			 String address=""; 
				if(prosp.getAdress1()!=null)
				{
					address=prosp.getAdress1().replaceAll("'","`");
				}
			 prosp.setAdress1(address);
				
			 data.insertProspectiveData(prosp);//insertion;
			 
			 
			 
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---insertQuotation-->" , ex);
		}
		return quotationid;
	}
	
	public String insertWebQuotation(QuotationModel objQuot,List<QuotationProductModel> lstProduct,List<QuotationProductModel> lstService,List<QuotationAttachmentModel> lstAtt)
	{
		String result="";
		int insRes = 0;
		SQLDBHandler db = new SQLDBHandler("hinawi_hba");
		QuotationQueries query=new QuotationQueries();
		try
		{
			// to store in original explorer
			CompanyDBModel obj = new CompanyDBModel();
			obj.setUserip("hinawi2.dyndns.org");
			obj.setDbname("ECActualERPData");
			obj.setDbuser("admin");
			obj.setDbpwd("explorer654321");
			db = new SQLDBHandler(obj);
			
		/*	obj.setUserip("CHADIRAHME-PC\\SQLEXPRESS");
			obj.setDbname("ECActualERPData");
			obj.setDbuser("sa");
			obj.setDbpwd("123456");		
			db = new SQLDBHandler(obj);*/
			
			insRes = db.executeUpdateQuery(query.insertWebQuotation(objQuot));
			logger.info("insert Web_Quotation>> " + insRes);
			int quotationid=0;
			ResultSet rs= db.executeNonQuery("select Max(quotationid) as qouid from Web_Quotation");
			 while(rs.next())
			 {
				 quotationid=rs.getInt("qouid");
			 }
			 
			 for (QuotationProductModel item : lstProduct) 
			 {			
				 db.executeUpdateQuery(query.insertWebQuotationProduct(item, quotationid));
			 }
			 
			 for (QuotationProductModel item : lstService) 
			 {
				 db.executeUpdateQuery(query.insertWebQuotationProduct(item, quotationid));
			 }
					
			
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---insertWebQuotation-->" , ex);
		}
		return result;
	}
	
	public String getMailBody(int quotationid)
	{
		
		StringBuffer result=null;
		DBHandler db=new DBHandler();
		ResultSet rs = null;
		try
		{
		String companyName="",contactTitle="",contactName="",positionname="",countryname="",countrycode="",city="",telephone1="",telephone2="",mobile1="",mobile2="",
				email="",website="",howyouknow="",pobox="",skypeid="",businesstype="",companysize="",totalEmployee="",externalAuditorname="",
						currentAccount="",currentHr="",otherSoftware="",softwareLanguage="",groupName="",fromTime="",toTime="",fromDay="",toDay="",address="",
						custtype="",companytype="",otherposition="",notes="",auditor="";
	    QuotationQueries query=new QuotationQueries();
	    rs=db.executeNonQuery(query.getQuotationData(quotationid));
	    while(rs.next())
	    {
	    	companyName=rs.getString("companyName");
	    	contactTitle=rs.getString("contactTitle");
	    	contactName=rs.getString("contactName");
	    	positionname=rs.getString("positionname");
	    	countryname=rs.getString("countryname");
	    	countrycode=rs.getString("countrycode");
	    	city=rs.getString("city");
	    	telephone1=rs.getString("telephone1");
	    	telephone2=rs.getString("telephone2");
	    	mobile1=rs.getString("mobile1");
	    	mobile2=rs.getString("mobile2");
	    	email=rs.getString("email");
	    	website=rs.getString("website")==null?"":rs.getString("website");
	    	howyouknow=rs.getString("howyouknow")==null?"":rs.getString("howyouknow");
	    	auditor=rs.getString("auditor");
	    	
	    	pobox=rs.getString("pobox");
	    	skypeid=rs.getString("skypeid");
	    	businesstype=rs.getString("businesstype");
	    	companysize=rs.getString("companysize");
	    	totalEmployee=rs.getString("totalEmployee");
	    	externalAuditorname=rs.getString("externalAuditorname");
	    	currentAccount=rs.getString("currentAccount");
	    	currentHr=rs.getString("currentHr");
	    	otherSoftware=rs.getString("otherSoftware");
	    	softwareLanguage=rs.getString("softwareLanguage");
	    	groupName=rs.getString("groupName");
	    	fromTime=rs.getString("fromTime");
	    	toTime=rs.getString("toTime");
	    	fromDay=rs.getString("fromDay");
	    	toDay=rs.getString("toDay");
	    	address=rs.getString("address");	    	
	    	custtype=rs.getString("custtype");
	    	companytype=rs.getString("companytype");
	    	otherposition=rs.getString("otherposition");
	    	notes=rs.getString("notes");
	    	
	    	if(positionname.equals("Other"))
	    		positionname=otherposition;
	    	
	    }
	    
	    
		result=new StringBuffer();
		result.append("<table border='0'>");
	  	//result.append("<tr>");
		//result.append("<td colspan='2' style='font-weight: bold; border:0;'>Dear "+ contactTitle + " " + contactName   + "</td>");
		//result.append("</tr>");
		 
		 	result.append("<tr><td colspan='2'>A new feedback inquiry has been received with following details : </td></tr>");
		 	
			result.append("<tr><td colspan='2' style='font-weight: bold;'>Company Information:<br/><br/></td></tr>");
			result.append("<tr><td colspan='2' style='font-weight: bold;color:red;font-size:16px;'>I am :"+custtype +"<br/><br/></td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Company Name :</td><td>" + companyName + "</td></tr>");
			result.append("<tr><td>Sender Name :</td><td>" + contactTitle + " " + contactName + "</td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Position :</td><td>" + positionname + "</td></tr>");
			result.append("<tr><td>City :</td><td>" + city + "</td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Country :</td><td>" + countryname + "</td></tr>");
			
			String tel= telephone1;
			if(!telephone2.contains("null"))
				tel+=", " + telephone2;
			result.append("<tr style='background:#f5f5f5'><td>Telephone No. :</td><td>" + tel + "</td></tr>");
			String mobile=mobile1;
			if(!mobile2.contains("null"))
				mobile+=", " + mobile2;
			result.append("<tr style='background:#f5f5f5'><td>Mobile No. :</td><td>" + mobile + "</td></tr>");
			result.append("<tr><td>Email Id :</td><td>" + email + "</td></tr>");
			result.append("<tr><td>Website URL :</td><td>" + website + "</td></tr>");
			result.append("<br/><br/>");
			result.append("<tr><td colspan='2' style='font-weight: bold;'>How Did You Know Our Company:<br/></td></tr>");
			result.append("<tr style='background:#f5f5f5'><td colspan='2'>" + howyouknow + "</td></tr>");
			if(auditor!=null)
			result.append("<tr style='background:#f5f5f5'><td colspan='2'>" + auditor + "</td></tr>");
			
			result.append("<tr><td colspan='2'><br/><br/></td></tr>");
			
			result.append("<tr><td colspan='2' style='font-weight: bold;'>Products:<br/></td></tr>");
			result.append("<tr><td colspan='2'>");
			result.append("<table><tr>");
			result.append("<td>Product Name</td>");
			result.append("<td>No. of License</td>");
			result.append("<td>Memo</td></tr>");
			
			 rs=db.executeNonQuery(query.getQuotationProduct(quotationid,"p"));
			while(rs.next())
			{
			 result.append("<tr style='background:#f5f5f5'><td>"+rs.getString("description") + "</td>");
			 result.append("<td>"+rs.getString("nooflicense") + "</td>");
			 result.append("<td>"+rs.getString("memo") + "</td></tr>");		
			 result.append("<tr><td colspan='3'><br/><br/></td></tr>");
			}
			result.append("</table>");
			result.append("</td></tr>");
			
			result.append("<tr><td colspan='2'><br/></td></tr>");
			
			result.append("<tr><td colspan='2' style='font-weight: bold;'>Services:<br/></td></tr>");
			result.append("<tr><td colspan='2'>");
			result.append("<table><tr>");
			result.append("<td>Type of service</td>");			
			result.append("<td>Memo</td></tr>");
			
			 rs=db.executeNonQuery(query.getQuotationProduct(quotationid,"s"));
			while(rs.next())
			{
			 result.append("<tr style='background:#f5f5f5'><td>"+rs.getString("description") + "</td>");			
			 result.append("<td>"+rs.getString("memo") + "</td></tr>");		
			 result.append("<tr><td colspan='2'><br/><br/></td></tr>");
			}
			result.append("</table>");
			result.append("</td></tr>");
			
			result.append("<tr><td colspan='2'><br/></td></tr>");
			result.append("<tr><td colspan='2' style='font-weight: bold;'>Additional Info:<br/><br/></td></tr>");
			
			result.append("<tr style='background:#f5f5f5'><td>Business Type :</td><td>" + businesstype + "</td></tr>");
			result.append("<tr><td>Company Size :</td><td>" + companysize + "</td></tr>");
			result.append("<tr><td>Company Type :</td><td>" + companytype + "</td></tr>");
			
			result.append("<tr style='background:#f5f5f5'><td>Total Employees & Labors. :</td><td>" + totalEmployee + "</td></tr>");
			result.append("<tr><td>External Auditor Name :</td><td>" + externalAuditorname + "</td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Current Accounting Software Name :</td><td>" + currentAccount + "</td></tr>");
			result.append("<tr><td>Current HR Software Name :</td><td>" + currentHr + "</td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Other Software Name :</td><td>" + otherSoftware + "</td></tr>");
			result.append("<tr><td>What Software Language you want to use :</td><td>" + softwareLanguage + "</td></tr>");
			String isGroup=groupName==null?  "No" : "Yes";
			result.append("<tr><td>Is company part of group? :</td><td>" +isGroup + "</td></tr>");
			result.append("<tr><td>Group Name :</td><td>" + groupName + "</td></tr>");
			
			if(pobox!=null)
			result.append("<tr style='background:#f5f5f5'><td>P.O Box :</td><td>" + pobox + "</td></tr>");
			result.append("<tr><td>Skype Id :</td><td>" + skypeid + "</td></tr>");
				
			result.append("<tr><td colspan='2'><br/><br/></td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Office Timing :</td><td>" + "From: "+fromTime + " To: " + toTime + "</td></tr>");
			result.append("<tr style='background:#f5f5f5'><td>Days :</td><td>" + "From: " + fromDay + " To: " + toDay + "</td></tr>");
			result.append("<tr><td>Address :</td><td>" + address + "</td></tr>");
		 
			result.append("<tr><td>Notes :</td><td>" + notes + "</td></tr>");
			
			result.append("<tr><td colspan='2'><br/><br/></td></tr>");
			result.append("<tr><td colspan='2'>Thank you !!</td></tr>");
			result.append("</table>");	
		 
		
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---getMailBody-->" , ex);
		}
		
		return result.toString();
	}
	
	public ArrayList getMailAttachment(int quotationid)
	{
		
		StringBuffer result=null;
		DBHandler db=new DBHandler();
		ResultSet rs = null;
		ArrayList fileArray = new ArrayList();
		try
		{
			//String repository = System.getProperty("catalina.home")+File.separator+"uploads"+File.separator;
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator;
			 QuotationQueries query=new QuotationQueries();
			 rs=db.executeNonQuery(query.getQuotationAttachment(quotationid));
			 while(rs.next())
			 {
				String dirPath=repository+rs.getString("sessionid");
				String filePath = dirPath +File.separator +rs.getString("filepath");
				File dir = new File(filePath);
				if(dir.exists())
				fileArray.add(filePath);
			 }
			 
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---getMailAttachment-->" , ex);
		}
		
		return fileArray;
	}
	
	public ArrayList getInstructionsAttachment()
	{
		
		StringBuffer result=null;		
		ArrayList fileArray = new ArrayList();
		try
		{
			//String repository1 = System.getProperty("catalina.home")+File.separator+"uploads"+File.separator;
			//logger.info("local>>> "+repository1);
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"instructions"+File.separator;
			//logger.info("live>>> "+repository);
			String filePath=repository+"TeamViewer.pdf";	
			File dir = new File(filePath);
			if(dir.exists())
			fileArray.add(filePath);						
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---getInstructionsAttachment-->" , ex);
		}
		
		return fileArray;
	}
	//History
	public List<QuotationModel> getQuotationHistory(String fromDate,String toDate)
	{
		DBHandler db=new DBHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		QuotationQueries query=new QuotationQueries();		
		List<QuotationModel> qtModelList = new ArrayList<QuotationModel>();
		ResultSet quotHistRS = null;
		try
		{
		quotHistRS = db.executeNonQueryRS(query.getQuotationHistory(fromDate,toDate));
		while (quotHistRS.next()){
			
			QuotationModel quotModel = new QuotationModel();
			
			quotModel.setAddress(quotHistRS.getString("address"));
			quotModel.setAuditor(quotHistRS.getString("auditor"));
			quotModel.setBusinesstype(quotHistRS.getString("businesstype"));
			quotModel.setCity(quotHistRS.getString("city"));
			quotModel.setCountryName(quotHistRS.getString("countryname"));
			quotModel.setCompanyName(quotHistRS.getString("companyName"));
			quotModel.setCompanysize(quotHistRS.getString("companysize"));
			quotModel.setCompanytype(quotHistRS.getString("companytype"));
			quotModel.setContactName(quotHistRS.getString("contactName"));
			quotModel.setContactTitle(quotHistRS.getString("contactTitle"));
			quotModel.setCurrentAccount(quotHistRS.getString("currentAccount"));
			quotModel.setCurrentHr(quotHistRS.getString("currentHr"));
			quotModel.setCusttype(quotHistRS.getString("custtype"));
			quotModel.setEmail(quotHistRS.getString("email"));
			quotModel.setMailTo("mailto:"+quotHistRS.getString("email"));
			quotModel.setExternalAuditorname(quotHistRS.getString("externalAuditorname"));
			quotModel.setFromDay(quotHistRS.getString("fromDay"));
			quotModel.setFromTime(quotHistRS.getString("fromTime"));
			quotModel.setGroupName(quotHistRS.getString("groupName"));
			quotModel.setHowyouknow(quotHistRS.getString("howyouknow"));
			quotModel.setMobile1(quotHistRS.getString("mobile1"));
			quotModel.setMobile2(quotHistRS.getString("mobile2"));
			quotModel.setNotes(quotHistRS.getString("notes"));
			quotModel.setOtherposition(quotHistRS.getString("otherposition"));
			quotModel.setOtherSoftware(quotHistRS.getString("otherSoftware"));
			quotModel.setPartofgroup(quotHistRS.getString("partofgroup"));
			quotModel.setPobox(quotHistRS.getString("pobox"));
			quotModel.setSkypeid(quotHistRS.getString("skypeid"));
			quotModel.setSoftwareLanguage(quotHistRS.getString("softwareLanguage"));
			quotModel.setTelephone1(quotHistRS.getString("telephone1"));
			quotModel.setTelephone2(quotHistRS.getString("telephone2"));
			quotModel.setToDay(quotHistRS.getString("toDay"));
			quotModel.setTotalEmployee(quotHistRS.getString("totalEmployee"));
			quotModel.setToTime(quotHistRS.getString("toTime"));
			quotModel.setWebsite(quotHistRS.getString("website"));
			quotModel.setQuotationid(Integer.parseInt(quotHistRS.getString("quotationid")));
			quotModel.setCreationDate(sdf.format(quotHistRS.getDate("creationdate")));
			qtModelList.add(quotModel);
		}
		
		}
		catch (Exception ex) 
		{
			logger.error("error in QuotationData---getQuotationHistory-->" , ex);
		}
		return qtModelList;
	}
	
    public List<HistoryProductModel> getQuotationProduct(int quotationid){
		
		List<HistoryProductModel> lstHistProduct = new ArrayList<HistoryProductModel>();
		
		try {
		
		DBHandler db=new DBHandler();
		QuotationQueries query=new QuotationQueries();			
		ResultSet quotHistRS = null;
		quotHistRS = db.executeNonQueryRS(query.getProductsQuery(quotationid));
		
		while (quotHistRS.next()){
			
			HistoryProductModel histModel = new HistoryProductModel();
			
			histModel.setDescription(quotHistRS.getString("description"));
			histModel.setNoofliscence(quotHistRS.getString("nooflicense"));
			histModel.setMemo(quotHistRS.getString("memo"));
			histModel.setProducttype(quotHistRS.getString("producttype").equals("p")?"Product":"Service");		
			lstHistProduct.add(histModel);
		}
		
		}
		catch (Exception ex){
			
			logger.info("Exception in QuotationData ===> "+ ex );
		}
		
		return lstHistProduct;
	}
}
