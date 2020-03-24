package hba;

import home.QuotationAttachmentModel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.CompanyDBModel;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import common.CompanyProfile;
import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ProspectiveData {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	SQLDBHandler db = new SQLDBHandler("hinawi_hba");
	Calendar c = Calendar.getInstance();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	ProspectiveQuerries query = new ProspectiveQuerries();
	int companyId=0;
	WebusersModel dbUser;

	public ProspectiveData() {
		try {
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb = new DBHandler();
			ResultSet rs = null;
			CompanyDBModel obj = new CompanyDBModel();
			dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			if (dbUser != null) {
				HBAQueries query = new HBAQueries();
				companyId=dbUser.getCompanyid();			
				rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
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
			logger.error("error in ProspectiveData---Init-->", ex);
		}
	}

	public int getMaxOfProspective() {
		int MaxRecNo = 1;
		// to store in original explorer
		CompanyDBModel obj = new CompanyDBModel();
		obj.setUserip("hinawi2.dyndns.org");
		obj.setDbname("ECActualERPData");
		obj.setDbuser("admin");
		obj.setDbpwd("explorer654321");
		db = new SQLDBHandler(obj);
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery("SELECT max(recno) as MaxRecNo from Prospective");
			while (rs.next()) {
				MaxRecNo = (int) (rs.getDouble("MaxRecNo") + 1.0);
			}
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getMaxOfProspective-->",
					ex);
		}
		return MaxRecNo;
	}

	public List<ProspectiveModel> getFullName() {
		List<ProspectiveModel> lst = new ArrayList<ProspectiveModel>();
		ResultSet rs = null;
		try {
			ProspectiveModel prospModel = new ProspectiveModel();
			// prospModel.setRecNo(0);
			// prospModel.setResult("Select");
			// lst.add(prospModel);

			rs = db.executeNonQuery(query.getFullName());
			while (rs.next()) {
				prospModel = new ProspectiveModel();
				prospModel.setRecNo(rs.getInt("Recno"));
				prospModel.setResult(rs.getString("FullName") == null ? "" : rs
						.getString("FullName"));
				lst.add(prospModel);
			}
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getFullName-->", ex);
		}
		return lst;
	}

	public List<ProspectiveModel> getCategory() {
		List<ProspectiveModel> lst = new ArrayList<ProspectiveModel>();
		ResultSet rs = null;
		try {
			ProspectiveModel prospModel = new ProspectiveModel();
			// prospModel.setRecNo(0);
			// prospModel.setResult("Select");
			// lst.add(prospModel);
			rs = db.executeNonQuery(query.getCategory());
			while (rs.next()) {
				prospModel = new ProspectiveModel();
				prospModel.setRecNo(rs.getInt("Recno"));
				prospModel.setResult(rs.getString("Name") == null ? "" : rs
						.getString("Name"));
				lst.add(prospModel);
			}
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getCategory-->", ex);
		}
		return lst;
	}

	public List<ProspectiveModel> getHowYouKnow() {
		List<ProspectiveModel> lst = new ArrayList<ProspectiveModel>();
		ResultSet rs = null;
		try {
			ProspectiveModel prospModel = new ProspectiveModel();
			// prospModel.setRecNo(0);
			// prospModel.setResult("Select");
			// lst.add(prospModel);
			rs = db.executeNonQuery(query.getHowYouKnow());
			while (rs.next()) {
				prospModel = new ProspectiveModel();
				prospModel.setRecNo(rs.getInt("SalesRep_Key"));
				prospModel.setResult(rs.getString("Initial") == null ? "" : rs
						.getString("Initial"));
				lst.add(prospModel);
			}
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getHowYouKnow-->", ex);
		}
		return lst;
	}

	public List<ProspectiveModel> getProspectiveSearchRes(String searchChar,
			int type) {
		List<ProspectiveModel> list = new ArrayList<ProspectiveModel>();
		List<ProspectiveContactDetailsModel> prospectiveContactsList = new ArrayList<ProspectiveContactDetailsModel>();
		List<ProspectiveContactDetailsModel> tempProspectiveContactsList;
		ResultSet rs = null;
		ResultSet rs2 = null;
		List<Integer> lstCustRefKey = new ArrayList<Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		prospectiveContactsList=getProspectiveContactLst(0);//get all prospective contacts

		try {
			String query = "SELECT  Prospective.*,HowUKnowList.Initial AS HowUKnow,CountryList.DESCRIPTION AS Country,"
					+ "LocalItem.Name As CompanyType,StreetList.DESCRIPTION AS Street,CityList.DESCRIPTION AS City FROM "
					+ "Prospective LEFT JOIN SalesRep AS HowUKnowList ON Prospective.HowKnowRefKey = HowUKnowList.SalesRep_Key "
					+ "LEFT JOIN HRLISTVALUES AS CityList ON Prospective.CityRefKey = CityList.ID "
					+ "LEFT JOIN HRLISTVALUES AS CountryList ON Prospective.CountryRefKey = CountryList.ID "
					+ "LEFT JOIN HRLISTVALUES AS StreetList ON Prospective.StreeRefKey = StreetList.ID "
					+ "LEFT JOIN LocalItem ON Prospective.CompanyTypeRefKey=LocalItem.RecNo ";
			if (type == 1) {
				query += "where Prospective.Status='A' and Prospective.recno="
						+ searchChar;
			}
			if (type == 2) {
				query += "where Prospective.Status='A' and Prospective.CompanyTypeRefKey="
						+ searchChar;
			}
			if (type == 3) {
				query += "where Prospective.Status='A' and Prospective.HowKnowRefKey="
						+ searchChar;
			}
			if (type == 4) {
				query += "where Prospective.Status='A' and (Prospective.Telephone1 like '%"
						+ searchChar
						+ "%' or Prospective.Telephone2 like '%"
						+ searchChar + "%')";
			}
			if (type == 5) {
				query += "where Prospective.Status='A' and Prospective.Fax like '%"
						+ searchChar + "%'";
			}
			if (type == 6 && searchChar != "") {
				if(searchChar.equals("P"))
					query+=" where Prospective.Status='A' and PriorityID=1 and Prospective.IsActive='Y' ";
				else
				query += " where Prospective.Status='A' and Prospective.isactive = '"
						+ searchChar + "'";
			}
			if (type == 7 && searchChar == "C") {
				query += "where Prospective.Status='" + searchChar + "'";
			}

			query += " Order by Replace(Prospective.FullName,':',':'),Prospective.PriorityID Desc";

			rs = db.executeNonQuery(query);
			rs2 = db.executeNonQuery("Select Distinct CustomerRefKey As QUOTPROSKEY "
					+ "From Quotation Where ClientType='P' AND Status in ('C','H','O')");

			while (rs2.next()) {
				lstCustRefKey.add(rs2.getInt("QUOTPROSKEY"));
			}
			while (rs.next()) {

				ProspectiveModel prospModel = new ProspectiveModel();

				if (lstCustRefKey.contains((int) rs.getDouble("RecNo"))) {

					prospModel.setHas_quotation("Yes");

				} else {

					prospModel.setHas_quotation("No");
				}

				if (rs.getInt("PriorityID") == 1) {

					prospModel.setRowColor("color: red;");
					prospModel.setPriority(true);
				} else {

					prospModel.setPriority(false);
				}

				prospModel.setRecNo(rs.getInt("RecNo"));
				prospModel.setName(rs.getString("Name"));
				prospModel.setArName(rs.getString("Arabic"));
				prospModel.setSubLevel(rs.getInt("Sublevel"));
				prospModel
				.setCreated_date(rs.getDate("TimeCreated") == null ? ""
						: sdf.format(rs.getDate("TimeCreated")));
				prospModel.setCategory(rs.getString("CompanyType") == null ? ""
						: rs.getString("CompanyType"));
				prospModel.setEmail(rs.getString("EMail") == null ? "" : rs
						.getString("EMail"));
				prospModel.setcC(rs.getString("cc") == null ? "" : rs
						.getString("cc"));
				prospModel.setWebsite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));
				prospModel 
				.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				prospModel.setAltPhone(rs.getString("Telephone1") == null ? ""
						: rs.getString("Telephone1"));

				prospModel.setFax(rs.getString("Fax") == null ? "" : rs
						.getString("Fax"));
				prospModel
				.setPrintChequeAs(rs.getString("PrintChequeAs") == null ? ""
						: rs.getString("PrintChequeAs"));
				prospModel.setAdress1(rs.getString("shipto") == null ? "" : rs
						.getString("shipto"));
				prospModel.setOther1(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				prospModel.setOther2(rs.getString("altcontact") == null ? ""
						: rs.getString("altcontact"));
				prospModel
				.setContactPerson(rs.getString("contact") == null ? ""
						: rs.getString("contact"));
				tempProspectiveContactsList= new ArrayList<ProspectiveContactDetailsModel>();
				for(ProspectiveContactDetailsModel contactDetailsModel:prospectiveContactsList)
				{
					if(prospModel.getRecNo()==contactDetailsModel.getRecNo())
					{
						tempProspectiveContactsList.add(contactDetailsModel);
					}
				}
				prospModel.setProspectiveContact(tempProspectiveContactsList);
				list.add(prospModel);
			}
		} catch (Exception ex) {
			logger.error(
					"error in ProspectiveData---getProspectiveSearchRes-->", ex);
		}
		return list;
	}

	public ProspectiveContactDetailsModel getProspectiveDetails(double recNo) {
		ResultSet rs = null;
		ProspectiveContactDetailsModel prospDetails = new ProspectiveContactDetailsModel();

		try {

			rs = db.executeNonQuery(query.getProspectiveDetails(recNo));
			while (rs.next()) {

				prospDetails.setName(rs.getString("Name"));
				prospDetails.setDob(rs.getString("DOB"));
				prospDetails.setEmail(rs.getString("Email"));
				prospDetails.setPosition(rs.getString("Position"));
				prospDetails.setTel(rs.getString("Telephone1"));
			}

		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getProspectiveDetails-->",
					ex);
		}

		return prospDetails;
	}

	public int updateProspectiveContactDetails(
			ProspectiveContactDetailsModel prospContactDetails, double recNo) {
		int updateRes = 0;
		try {
			updateRes = db
					.executeUpdateQuery(query.updateProspectiveContactDetails(
							prospContactDetails, recNo));
			logger.info("zzzzzzzzzzzzzzz " + updateRes);
		} catch (Exception ex) {

			logger.error(
					"error in ProspectiveData---updateProspectiveContactDetails-->",
					ex);
		}
		return updateRes;
	}

	public int insertProspective(ProspectiveModel prospModel,List<QuotationAttachmentModel> attachmentModels)
	{
		int result = 0;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				
		if (prospModel.getSelectedSubOf() != null
				&& prospModel.getSelectedSubOf().getRecNo() != 0) {
			ProspectiveModel subOf = new ProspectiveModel();
			subOf = prospModel.getSelectedSubOf();
			prospModel.setFullname(subOf.getFullname() + ":"
					+ prospModel.getName());
			prospModel.setSubLevel(subOf.getSubLevel() + 1);
			prospModel.setParent(subOf.getRecNo());
		} else {
			prospModel.setFullname(prospModel.getName());
			prospModel.setSubLevel(0);
			prospModel.setParent(0);
		}
		int priorityUpdate = 0;

		if (prospModel.isPriority() == true) {
			priorityUpdate = 1;
		} else {
			priorityUpdate = 0;
		}
		try 
		{
			int newID = getMaxID();
			//rename temp dir for picture
			if(!prospModel.getPhotoPath().equals(""))
			{				
				File file = new File(prospModel.getPhotoPath());
				File curentPath  = new File(file.getParent());
				String currentFolder= curentPath.getName().toString();
				String newPath=prospModel.getPhotoPath().replace(currentFolder, newID+"");
				
				String repository =CompanyProfile.CompanyProspectivesRepository(); //System.getProperty("catalina.base")+ File.separator + "uploads" + File.separator;
				String newPathDir =repository+newID;  //repository+"Prospectives"+File.separator+companyId +File.separator+newID;  
				
				File dir = new File(newPathDir);
				if (!dir.exists())
					dir.mkdirs();
				
				Path temp = Files.move
				        (Paths.get(prospModel.getPhotoPath()), 
				        Paths.get(newPath));
				 if(temp != null)
			        {
					 	prospModel.setPhotoPath(newPath);
			            System.out.println("File renamed and moved successfully");
			        }
			        else
			        {
			            System.out.println("Failed to move the file");
			        }
				//String fileName=f.getName();				
				//String path =prospModel.getPhotoPath();
				//File dir = new File(f.getParent());			
				
				//String dirPath= dir.getPath().substring(0, dir.getPath().lastIndexOf("\\")); 
				
				/*if (curentPath.exists())
				{
					String newPath=prospModel.getPhotoPath().replace(currentFolder, newID+""); //dirPath;//.substring(dirPath.lastIndexOf("\\")+1,dirPath.length()); 					
					File newDir = new File(newPath);
					file.renameTo(newDir);
					//file.delete();
					prospModel.setPhotoPath(newPath);
				}		*/				
			}
			
			
			prospModel.setRecNo(newID);
			String time = sdf1.format(df.parse(sdf.format(c.getTime())));
			result = db.executeUpdateQuery(query.insertProspective(prospModel,
					priorityUpdate, time));
			
			for (ProspectiveContactDetailsModel contactDetailsModel : prospModel
					.getProspectiveContact()) {
				db.executeUpdateQuery(query.insertProspectiveContact(
						contactDetailsModel, prospModel.getRecNo(),
						getMaxContactLineNo(prospModel.getRecNo())));
			}
			
			//Save Attachment				
			String repository =CompanyProfile.ProspectivesAttachmentRepository(); 								
			String creationPath=repository+prospModel.getRecNo()+"";
			for(QuotationAttachmentModel objAtt :attachmentModels)
			{
				if(objAtt!=null && objAtt.getFilename()!=null && !objAtt.getFilename().equalsIgnoreCase(""))
				{															
					if(objAtt.getImageMedia()!=null)
					{						
						int res =CompanyProfile.createFile(objAtt.getImageMedia(),creationPath,objAtt.getFilename());	
						if(res==0)
						{
							objAtt.setFilepath(creationPath + File.separator+objAtt.getFilename());							
							objAtt.setFormId(10400);//info doc --  Select * From HRLISTVALUES Where  Field_ID=125
							objAtt.setNameType("P");							
							objAtt.setAttachid(prospModel.getRecNo());							
							objAtt.setUserId(dbUser.getDesktopUserid());	
							db.executeUpdateQuery(query.addAdditionalAttachments(objAtt));
						}
					}												
				}
			}					
						
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---insertProspective-->", ex);
		}
		return result;
	}

	public List<ProspectiveModel> getProspectiveOtherThanCurrentProspective(
			int cust_key) {
		List<ProspectiveModel> lstProspectives = new ArrayList<ProspectiveModel>();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query
					.getProspectiveOtherThanCurrentProspective());
			ProspectiveModel newobj = new ProspectiveModel();
			newobj.setRecNo(0);
			newobj.setName("None");
			newobj.setSubLevel(0);
			lstProspectives.add(newobj);
			while (rs.next()) {
				ProspectiveModel obj = new ProspectiveModel();
				obj.setRecNo(rs.getInt("RecNo"));

				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));

				obj.setArName(rs.getString("Arabic") == null ? "" : rs
						.getString("Arabic"));

				obj.setFullname(rs.getString("fullName"));

				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));

				obj.setCountry(rs.getInt("CountryRefKey"));

				obj.setPhone(rs.getString("Telephone1") == null ? "" : rs
						.getString("Telephone1"));

				obj.setDirectPhone(rs.getString("Telephone2") == null ? "" : rs
						.getString("Telephone2"));

				obj.setAltPhone(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));

				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));

				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));

				obj.setContactPerson(rs.getString("contact") == null ? "" : rs
						.getString("contact"));

				obj.setAltContactPerson(rs.getString("AltContact") == null ? ""
						: rs.getString("AltContact"));

				obj.setLevelOfInterset(rs.getString("InterestLevel") == null ? ""
						: rs.getString("InterestLevel"));

				obj.setSubLevel(rs.getInt("Sublevel"));

				obj.setStreet(rs.getInt("StreeRefKey"));

				obj.setHowdidYouknowus((int) rs.getDouble("HowKnowRefKey"));

				obj.setPobox(rs.getString("Address1") == null ? "" : rs
						.getString("Address1"));

				obj.setZipCode(rs.getString("Address2") == null ? "" : rs
						.getString("Address2"));

				obj.setcC(rs.getString("CC") == null ? "" : rs.getString("CC"));

				obj.setWebsite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));

				obj.setSkypeId(rs.getString("SkypeID") == null ? "" : rs
						.getString("SkypeID"));

				obj.setSendTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));

				obj.setIsActive(rs.getString("IsActive") == null ? "" : rs
						.getString("IsActive"));

				obj.setPriority(rs.getBoolean("PriorityID"));

				lstProspectives.add(obj);
			}

		} catch (Exception ex) {
			logger.error(
					"error in ProspectiveData---getProspectiveOtherThanCurrentProspective-->",
					ex);
		}
		return lstProspectives;
	}

	public List<ProspectiveContactDetailsModel> getProspectiveContactLst(
			double recNo) {
		List<ProspectiveContactDetailsModel> lstProspectiveContact = new ArrayList<ProspectiveContactDetailsModel>();
		ResultSet rs = null;
		try {

			StringBuffer query = new StringBuffer();
			query.append(" Select * from ProspectiveCotact");
			if (recNo > 0)
				query.append(" Where RecNo=" + recNo);

			query.append(" order by defaultCont desc");
			rs = db.executeNonQuery(query.toString());

			while (rs.next()) {
				ProspectiveContactDetailsModel prospDetails = new ProspectiveContactDetailsModel();

				prospDetails.setRecNo(rs.getDouble("recNo"));
				prospDetails.setLineNo(rs.getInt("lineNo"));
				prospDetails.setName(rs.getString("Name") == null ? "" : rs
						.getString("Name"));
				prospDetails.setDob(rs.getString("DOB") == null ? "" : rs
						.getString("DOB"));
				prospDetails.setEmail(rs.getString("Email") == null ? "" : rs
						.getString("Email"));
				prospDetails.setPosition(rs.getString("Position") == null ? ""
						: rs.getString("Position"));
				prospDetails.setTel(rs.getString("Telephone1") == null ? ""
						: rs.getString("Telephone1"));
				prospDetails.setMobile(rs.getString("mobile1") == null ? "" : rs
						.getString("mobile1"));
				prospDetails.setExtension(rs.getString("Telephone2") == null ? ""
						: rs.getString("Telephone2"));
				prospDetails.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				prospDetails.setDefaultFlag(rs.getString("defaultcont") == null ? "" : rs
						.getString("defaultcont"));

				lstProspectiveContact.add(prospDetails);
			}

		} catch (Exception ex) {
			logger.error(
					"error in ProspectiveData---getProspectiveOtherThanCurrentProspective-->",
					ex);
		}
		return lstProspectiveContact;
	}

	public ProspectiveModel getProspectiveByKey(int RecNo) {
		ProspectiveModel obj = new ProspectiveModel();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getProspectiveByKey(RecNo));
			while (rs.next()) {
				obj.setRecNo(rs.getInt("RecNo"));
				obj.setName(rs.getString("name") == null ? "" : rs
						.getString("name"));
				obj.setArName(rs.getString("Arabic") == null ? "" : rs
						.getString("Arabic"));
				obj.setFullname(rs.getString("fullName"));
				obj.setCompanyName(rs.getString("companyName") == null ? ""
						: rs.getString("companyName"));
				obj.setCountry(rs.getInt("CountryRefKey"));
				obj.setCity(rs.getInt("CityRefKey"));
				obj.setPhone(rs.getString("Telephone1") == null ? "" : rs
						.getString("Telephone1"));
				obj.setDirectPhone(rs.getString("Telephone2") == null ? "" : rs
						.getString("Telephone2"));
				obj.setAltPhone(rs.getString("altphone") == null ? "" : rs
						.getString("altphone"));
				obj.setFax(rs.getString("fax") == null ? "" : rs
						.getString("fax"));
				obj.setEmail(rs.getString("email") == null ? "" : rs
						.getString("email"));
				obj.setContactPerson(rs.getString("contact") == null ? "" : rs
						.getString("contact"));
				obj.setAltContactPerson(rs.getString("AltContact") == null ? ""
						: rs.getString("AltContact"));
				obj.setLevelOfInterset(rs.getString("InterestLevel") == null ? ""
						: rs.getString("InterestLevel"));
				obj.setSubLevel(rs.getInt("Sublevel"));
				obj.setStreet(rs.getInt("StreeRefKey"));
				obj.setHowdidYouknowus((int) rs.getDouble("HowKnowRefKey"));
				obj.setPobox(rs.getString("Address1") == null ? "" : rs
						.getString("Address1"));
				obj.setZipCode(rs.getString("Address2") == null ? "" : rs
						.getString("Address2"));
				obj.setcC(rs.getString("CC") == null ? "" : rs.getString("CC"));
				obj.setWebsite(rs.getString("WebSite") == null ? "" : rs
						.getString("WebSite"));
				obj.setSkypeId(rs.getString("SkypeID") == null ? "" : rs
						.getString("SkypeID"));
				obj.setSendTo(rs.getString("ShipTo") == null ? "" : rs
						.getString("ShipTo"));
				obj.setPriority(rs.getBoolean("PriorityID"));
				obj.setIsActive(rs.getString("IsActive") == null ? "" : rs
						.getString("IsActive"));
				obj.setPhotoPath(rs.getString("PhotoPath") == null ? "" : rs
						.getString("PhotoPath"));
				obj.setNotes(rs.getString("note") == null ? "" : rs
						.getString("note"));
				obj.setCompanyTypeRefKey(rs.getInt("CompanyTypeRefKey"));
				obj.setCompanySizeRefKey(rs.getInt("CompanySizeRefKey"));
				obj.setSoftwareRefKey(rs.getInt("SoftwareRefKey"));
				obj.setNoUsers(rs.getInt("UserNos"));
				obj.setTotalComapnyEmployee(rs.getInt("EmpNos"));
				obj.setOwnerName(rs.getString("Ownername") == null ? "" : rs
						.getString("Ownername"));
				obj.setManageerName(rs.getString("ManagerName") == null ? ""
						: rs.getString("ManagerName"));
				obj.setAuditorName(rs.getString("AuditorName") == null ? ""
						: rs.getString("AuditorName"));
				obj.setAccountantName(rs.getString("AccountantName") == null ? ""
						: rs.getString("AccountantName"));
				obj.setLastTrialBalance(rs.getDate("LastTrialBalance"));
				obj.setWorkingHours((int) rs.getFloat("WorkingHrs"));
				obj.setSalesRepKey(rs.getInt("SalesRepKey"));

			}
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getProspectiveByKey-->",
					ex);
		}
		return obj;
	}

	public int updateProspectiveModel(ProspectiveModel obj,List<QuotationAttachmentModel> attachmentModels) 
	{
		int result = 0;
		if (obj.getSelectedSubOf() != null
				&& obj.getSelectedSubOf().getRecNo() != 0) {
			ProspectiveModel subOf = new ProspectiveModel();
			subOf = obj.getSelectedSubOf();
			obj.setFullname(subOf.getFullname() + ":" + obj.getName());
			obj.setSubLevel(subOf.getSubLevel() + 1);
			obj.setParent(subOf.getRecNo());
		} else {
			obj.setFullname(obj.getName());
			obj.setSubLevel(0);
			obj.setParent(0);
		}
		int priorityUpdate = 0;

		if (obj.isPriority() == true) {
			priorityUpdate = 1;
		} else {
			priorityUpdate = 0;
		}

		try 
		{
			//String path = "";
			//obj.setPhotoPath(path);
			result = db.executeUpdateQuery(query.UpdateProspective(obj,priorityUpdate));

			deleteProspectiveContact(obj.getRecNo());
			for (ProspectiveContactDetailsModel contactDetailsModel : obj.getProspectiveContact()) 
			{
				db.executeUpdateQuery(query.insertProspectiveContact(contactDetailsModel, obj.getRecNo(),getMaxContactLineNo(obj.getRecNo())));

			}
			
			//Save Attachment			
			if(attachmentModels!=null && attachmentModels.size()>0)
			{
			db.executeUpdateQuery(query.deleteAllAdditionalAttachments(obj.getRecNo()));
						
			String repository =CompanyProfile.ProspectivesAttachmentRepository(); 								
			String creationPath=repository+obj.getRecNo()+"";
				for (QuotationAttachmentModel objAtt : attachmentModels) 
				{
					if (objAtt != null && objAtt.getFilename() != null && !objAtt.getFilename().equalsIgnoreCase("")) 
					{
						if (objAtt.getImageMedia() != null)
							CompanyProfile.createFile(objAtt.getImageMedia(),
									creationPath, objAtt.getFilename());

						objAtt.setFormId(10400);// info doc -- Select * From
												// HRLISTVALUES Where
												// Field_ID=125
						objAtt.setNameType("P");
						objAtt.setAttachid(obj.getRecNo());
						objAtt.setUserId(dbUser.getDesktopUserid());
						db.executeUpdateQuery(query
								.addAdditionalAttachments(objAtt));
					}
				}
		  }
						
		} catch (Exception ex) {
			logger.error("error in ProspectiveData---UpdateProspectiveData-->",
					ex);
		}
		return result;
	}

	public int getMaxID() {
		int result = 0;
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery("select max(RecNo) from prospective ");
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

	public void deleteProspectiveContact(int RecNo) {
		try {
			db.executeUpdateQuery(" Delete from ProspectiveCotact Where RecNo="
					+ RecNo);
		} catch (Exception ex) {
			logger.error(
					"error in ProspectiveData---deleteProspectiveContact-->",
					ex);
		}
	}

	public List<ProspectiveModel> getNameFromProspectiveForValidation() {
		List<ProspectiveModel> lst = new ArrayList<ProspectiveModel>();

		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getNameFromProspectiveForValidation());
			while (rs.next()) {
				ProspectiveModel prospectiveModel = new ProspectiveModel();
				prospectiveModel.setName(rs.getString("name"));
				prospectiveModel.setRecNo(rs.getInt("recno"));
				lst.add(prospectiveModel);
			}

		} catch (Exception ex) {
			logger.error(
					"error in ProspectiveData---getNameFromProspectiveForValidation-->",
					ex);
		}
		return lst;
	}

	public int insertProspectiveData(ProspectiveModel prospModel) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		int insRes = 0;
		try {
			// to store in original explorer
			CompanyDBModel obj = new CompanyDBModel();
			obj.setUserip("hinawi2.dyndns.org");
			obj.setDbname("ECActualERPData");
			obj.setDbuser("admin");
			obj.setDbpwd("explorer654321");
			
			/*obj.setUserip("CHADIRAHME-PC\\SQLEXPRESS");
			obj.setDbname("ECActualERPData");
			obj.setDbuser("sa");
			obj.setDbpwd("123456");	*/
			
			db = new SQLDBHandler(obj);

			// db.executeUpdateQuery("Use ECACTUALERPDATA");
			insRes = db
					.executeUpdateQuery(" insert into Prospective (recNo,timecreated,name,fullname,contact,companyName,shipto,telephone1,telephone2,website,email,altphone,altcontact,other15,address1,skypeId) values ("
							+ prospModel.getRecNo()
							+ ",'"
							+ sdf1.format(prospModel.getCreatedDate())
							+ "','"
							+ prospModel.getCompanyName()
							+ "','"
							+ prospModel.getCompanyName()
							+ "','"
							+ prospModel.getFullname()
							+ "','"
							+ prospModel.getCompanyName()
							+ "','"
							+ prospModel.getAdress1()
							+ "','"
							+ prospModel.getPhone()
							+ "','"
							+ prospModel.getDirectPhone()
							+ "','"
							+ prospModel.getWebsite()
							+ "','"
							+ prospModel.getEmail()
							+ "','"
							+ prospModel.getOther1()
							+ "'"
							+ ",'"
							+ prospModel.getOther2()
							+ "','"
							+ prospModel.getOther3()
							+ "','"
							+ prospModel.getPobox()
							+ "','"
							+ prospModel.getSkypeId() + "')");
			logger.info("zzzzzzzzzzzzzzz " + insRes);
		} catch (Exception ex) {

			logger.error("error in ProspectiveData---insertProspectiveData-->",
					ex);
		}
		return insRes;

	}

	public int getMaxContactLineNo(int recno) {
		int result = 0;
		ProspectiveQuerries query = new ProspectiveQuerries();
		ResultSet rs = null;
		try {
			rs = db.executeNonQuery(query.getMaxContactLineNo(recno));
			while (rs.next()) {
				result = rs.getInt(1) + 1;
			}
			if (result == 0)
				result = 1;

		} catch (Exception ex) {
			logger.error("error in ProspectiveData---getMaxContactLineNo-->",
					ex);
		}
		return result;
	}
	
	public List<QuotationAttachmentModel> getAllAdditionalAttachments(int Name_Key)
	{
		List<QuotationAttachmentModel> lst=new ArrayList<QuotationAttachmentModel>();
		ProspectiveQuerries query = new ProspectiveQuerries();
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
