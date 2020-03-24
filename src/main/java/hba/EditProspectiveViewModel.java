package hba;

import home.MailClient;
import home.QuotationAttachmentModel;
import hr.HRData;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;

import layout.MenuModel;
import model.CustomerActivitiesModel;
import model.CustomerContact;
import model.HRListValuesModel;
import model.LocalItemModel;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import common.CompanyProfile;
import setup.users.WebusersModel;

public class EditProspectiveViewModel {
	private Logger logger = Logger.getLogger(this.getClass());
	private ProspectiveData data = new ProspectiveData();
	private HBAData hbadata = new HBAData();
	private HRData hrData = new HRData();
	private ProspectiveModel prospectiveModel;
	private Boolean prospectivePriority;
	private Boolean prospectiveActive;
	private boolean canSave;
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRListValuesModel selectedCountry;
	private HRListValuesModel selectedCity;
	private List<HRListValuesModel> howDid;
	private HRListValuesModel selectedHowDid;
	private List<HRListValuesModel> streets;
	private HRListValuesModel selectedStreet;
	private List<LocalItemModel> companySize;
	private LocalItemModel selectedCompanySize;
	private List<LocalItemModel> companyTypes;
	private LocalItemModel selectedCompanyType;
	private List<LocalItemModel> currentSoftwares;
	private LocalItemModel selectedCurrentSoftwares;
	private boolean selectedCheckBox = false;
	private boolean disableSubOf = true;
	//boolean activeProspective;
	private ProspectiveModel tempModel = new ProspectiveModel();
	private List<ProspectiveModel> prospectiveFilteredList;
	private String attFile4;
	private String editPhotoPath = "";
	private QuotationAttachmentModel lstAtt;
	private List<QuotationAttachmentModel> lstDocAtt;
	private QuotationAttachmentModel selectedAttchemnets;
	private List<CustomerActivitiesModel> statusHistory;
	
	@SuppressWarnings("unused")
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	@SuppressWarnings("unused")
	private SimpleDateFormat sdf = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss.SSS");
	@SuppressWarnings("unused")
	private Calendar c = Calendar.getInstance();
	private List<ProspectiveContactDetailsModel> lstProspectiveContact;
	private ProspectiveContactDetailsModel contactDetailsModel;
	private List<QbListsModel> salesRep;
	private QbListsModel selectedsalesRep;
	private int ifnote;
	private String contactName="";
	private String contactPhone="";
	private String contactMobile="";
	private String contactFax="";
	private String contactEmail="";
	
	private boolean adminUser;	
	WebusersModel dbUser;
	String oldStatus="";
	boolean oldPriorityID=false;
	
	private MenuModel companyRole;

	@SuppressWarnings({ "rawtypes", "unused" })
	public EditProspectiveViewModel() {
		try 
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int prospectKey = (Integer) map.get("prospectiveKey");
			String type = (String) map.get("type");
			Window win = (Window) Path.getComponent("/prospectiveModalDialog");
			setAttFile4("No file chosen");
			if (type.equalsIgnoreCase("add")) {
				setCanSave(true);
				win.setTitle("Add Prospective Info");
			} else if (type.equalsIgnoreCase("edit")) {
				setCanSave(true);
				win.setTitle("Edit Prospective Info");
			} else {
				setCanSave(false);
				win.setTitle("View Prospective Info");
			}

			prospectiveFilteredList = data
					.getProspectiveOtherThanCurrentProspective(prospectKey);

			countries = hrData.getHRListValues(2, "");// get country
			cities = hrData.getHRListValues(3, "");
			howDid = hrData.getHRListValues(140, "");
			streets = hrData.getHRListValues(51, "");
			companyTypes = hbadata.GetLocalItemByRef(3);
			companySize = hbadata.GetLocalItemByRef(4);
			currentSoftwares = hbadata.GetLocalItemByRef(10);
			salesRep = hbadata.GetMasterData("SalesRep");
			lstDocAtt=new ArrayList<QuotationAttachmentModel>();
			
			Session sess = Sessions.getCurrent();
			dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			int webUserID = 0;
			if (dbUser != null) 
			{
				//companyId=dbUser.getCompanyid();
				adminUser = dbUser.getFirstname().equals("admin");

				if (adminUser) {
					webUserID = 0;
				} else {
					webUserID = dbUser.getUserid();
				}
			}
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);

			if (prospectKey > 0) 
			{// get into edit
				String Match = "";
				prospectiveModel = data.getProspectiveByKey(prospectKey);
				oldStatus=prospectiveModel.getNotes();
				oldPriorityID=prospectiveModel.isPriority();
				
				contactName=prospectiveModel.getContactPerson();
				contactPhone=prospectiveModel.getPhone();
				contactMobile=prospectiveModel.getAltPhone();
				contactFax=prospectiveModel.getFax();
				contactEmail=prospectiveModel.getEmail();
				lstProspectiveContact = data.getProspectiveContactLst(prospectKey);
				lstDocAtt=data.getAllAdditionalAttachments(prospectKey);
				CustomerData cdata = new CustomerData();
				statusHistory = cdata.getCustomerStatusHistory(prospectKey, "P");
				
				if (prospectiveModel.getIsActive().equals("Y")) {
					//activeProspective = false;
					prospectiveActive=true;
				} 
				else 				
				{
					//activeProspective = true;
					prospectiveActive=false;
				}
				
				prospectivePriority=prospectiveModel.isPriority();
				
				
				if(""!=prospectiveModel.getNotes())
				{
					ifnote=1;
				}else{
					ifnote=0;
				}
				prospectiveModel.setSubOfdropDown(prospectiveFilteredList);
				prospectiveModel.setSelectedSubOf(prospectiveFilteredList.get(0));
				editPhotoPath = prospectiveModel.getPhotoPath();
			
				lstAtt = new QuotationAttachmentModel();
				lstAtt.setFilepath(editPhotoPath);				
				
				if (prospectiveModel.getSubLevel() > 0) {
					disableSubOf = false;
					selectedCheckBox = true;

					Pattern patternnew = Pattern.compile("(.*?):");
					Matcher matchernew = patternnew.matcher(prospectiveModel.getFullname());
					if (matchernew.find()) {
						Match = matchernew.group(1);

					}

					for (ProspectiveModel model : prospectiveFilteredList) {
						if (model != null) {
							String slectedCustomerName = model.getName();
							if (slectedCustomerName.equalsIgnoreCase(Match)) {
								prospectiveModel.setSelectedSubOf(model);
								tempModel = model;
								break;
							}
						}
					}

				}

				for (HRListValuesModel listValuesModel : countries) {
					if (prospectiveModel.getCountry() != null
							&& prospectiveModel.getCountry() == listValuesModel
									.getListId()) {
						selectedCountry = listValuesModel;
						break;
					}
				}

				for (HRListValuesModel model : cities) {
					if (prospectiveModel.getCity() != null
							&& prospectiveModel.getCity() == model.getListId()) {
						selectedCity = model;
						break;
					}
				}

				for (QbListsModel model : salesRep) {
					if (prospectiveModel.getSalesRepKey() != null
							&& prospectiveModel.getSalesRepKey() == model
									.getRecNo()) {
						selectedsalesRep = model;
						break;
					}
				}

				for (HRListValuesModel model : howDid) {
					if (prospectiveModel.getHowdidYouknowus() != null
							&& prospectiveModel.getHowdidYouknowus() == model
									.getListId()) {
						selectedHowDid = model;
						break;
					}
				}

				for (HRListValuesModel model : streets) {
					if (prospectiveModel.getStreet() != null
							&& prospectiveModel.getStreet() == model
									.getListId()) {
						selectedStreet = model;
						break;
					}
				}
				for (LocalItemModel model : companyTypes) {
					if (prospectiveModel.getCompanyTypeRefKey() != 0
							&& prospectiveModel.getCompanyTypeRefKey() == model
									.getRecNo()) {
						selectedCompanyType = model;
						break;
					}
				}

				for (LocalItemModel model : companySize) {
					if (prospectiveModel.getCompanySizeRefKey() != 0
							&& prospectiveModel.getCompanySizeRefKey() == model
									.getRecNo()) {
						selectedCompanySize = model;
						break;
					}
				}

				for (LocalItemModel model : currentSoftwares) {
					if (prospectiveModel.getSoftwareRefKey() != 0
							&& prospectiveModel.getSoftwareRefKey() == model
									.getRecNo()) {
						selectedCurrentSoftwares = model;
						break;
					}
				}

				if (lstProspectiveContact.size() == 0) {
					ProspectiveContactDetailsModel model = new ProspectiveContactDetailsModel();
					model.setLineNo(1);
					model.setDefaultFlag("Y");
					lstProspectiveContact.add(model);
				}

			}
			else 
			{
				prospectiveModel = new ProspectiveModel();
				prospectiveModel.setSubOfdropDown(prospectiveFilteredList);
				prospectiveModel.setSelectedSubOf(prospectiveFilteredList
						.get(0));
				prospectiveModel.setName("");
				prospectiveModel.setArName("");
				prospectiveModel.setCompanyName("");
				prospectiveModel.setContactPerson("");
				prospectiveModel.setPhone("");
				prospectiveModel.setAltPhone("");
				prospectiveModel.setFax("");
				prospectiveModel.setEmail("");
				prospectiveModel.setIsActive("Y");
				prospectiveModel.setNotes("");
				ProspectiveContactDetailsModel model = new ProspectiveContactDetailsModel();
				model.setLineNo(1);
				model.setDefaultFlag("Y");
				lstProspectiveContact = new ArrayList<ProspectiveContactDetailsModel>();
				lstProspectiveContact.add(model);
				prospectiveModel.setProspectiveContact(lstProspectiveContact);
				//activeProspective = false;
				prospectiveActive=true;
				prospectivePriority=true;

				lstAtt = new QuotationAttachmentModel();
				lstAtt.setFilepath(editPhotoPath);	
			}
			

		} catch (Exception ex) {
			logger.error("ERROR in EditProspectiveViewModel ----> init", ex);
		}
	}

	public List<ProspectiveContactDetailsModel> getLstProspectiveContact() {
		return lstProspectiveContact;
	}

	public void setLstProspectiveContact(
			List<ProspectiveContactDetailsModel> lstProspectiveContact) {
		this.lstProspectiveContact = lstProspectiveContact;
	}

	public void setDisableSubOf(boolean disableSubOf) {
		this.disableSubOf = disableSubOf;
	}

	public boolean isSelectedCheckBox() {
		return selectedCheckBox;
	}

	public void setSelectedCheckBox(boolean selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
	}

	public Boolean getProspectivePriority() {
		return prospectivePriority;
	}

	public void setProspectivePriority(Boolean prospectivePriority) {
		this.prospectivePriority = prospectivePriority;
	}

	public ProspectiveModel getProspectiveModel() {
		return prospectiveModel;
	}

	public void setProspectiveModel(ProspectiveModel prospectiveModel) {
		this.prospectiveModel = prospectiveModel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	public void updateProspective(@BindingParam("cmp") Window x) {
		int result = 0;
		String shipTo="";
		if (prospectiveModel.getName().equalsIgnoreCase("")) {
			Messagebox.show("Please enter the Prospective Name",
					"Prospective List", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (selectedCheckBox == false) {
			prospectiveModel.setSelectedSubOf(prospectiveFilteredList.get(0));
		}
		
		if (prospectiveActive) {
			prospectiveModel.setIsActive("Y");
		} else {
			prospectiveModel.setIsActive("N");
		}
		
		prospectiveModel.setPriority(prospectivePriority);

		if (prospectiveModel.getSelectedSubOf() != null
				&& prospectiveModel.getSelectedSubOf().getName()
						.equalsIgnoreCase(prospectiveModel.getName())
				&& prospectiveModel.getSelectedSubOf().getRecNo() == prospectiveModel
						.getRecNo()) {
			Messagebox
					.show("You cannot make an Prospective a subProspective of itself.",
							"Prospective List", Messagebox.OK,
							Messagebox.INFORMATION);
			return;
		}

		if (null != prospectiveModel.getSelectedSubOf()
				&& prospectiveModel.getSelectedSubOf().getName()
						.equalsIgnoreCase("none")) {
			ProspectiveModel subIytem = new ProspectiveModel();
			prospectiveModel.setSelectedSubOf(subIytem);
		}

		if (selectedsalesRep != null && selectedsalesRep.getRecNo() > 0) {
			prospectiveModel.setSalesRepKey(selectedsalesRep.getRecNo());
		} else {
			prospectiveModel.setSalesRepKey(0);
		}
		if(prospectiveModel.getContactPerson()!=null && !prospectiveModel.getContactPerson().equalsIgnoreCase(""))
			shipTo = shipTo + prospectiveModel.getContactPerson() + "\r\n";
		if(prospectiveModel.getPhone()!=null && !prospectiveModel.getPhone().equalsIgnoreCase(""))
			shipTo = shipTo		+ prospectiveModel.getPhone() + "\r\n";
		if(prospectiveModel.getAltPhone()!=null && !prospectiveModel.getAltPhone().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getAltPhone() + "\r\n";
		if(prospectiveModel.getFax()!=null && !prospectiveModel.getFax().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getFax() + "\r\n";
		if(prospectiveModel.getAltContactPerson()!=null && !prospectiveModel.getAltContactPerson().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getAltContactPerson() + "\r\n";
		if(prospectiveModel.getEmail()!=null && !prospectiveModel.getEmail().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getEmail() + "\r\n";
		if(prospectiveModel.getcC()!=null && !prospectiveModel.getcC().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getcC()+ "\r\n";
		if(prospectiveModel.getWebsite()!=null && !prospectiveModel.getWebsite().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getWebsite()+ "\r\n";
		if(prospectiveModel.getPobox()!=null && !prospectiveModel.getPobox().equalsIgnoreCase(""))
			shipTo = shipTo	+ prospectiveModel.getPobox()+ "\r\n";
		
		if (selectedStreet != null && selectedStreet.getListId() > 0) {
			prospectiveModel.setStreet(selectedStreet.getListId());
			shipTo=shipTo+selectedStreet.getEnDescription()+ "\r\n";
			
		} else {
			prospectiveModel.setStreet(0);
		}
		if (selectedCity != null && selectedCity.getListId() > 0) {
			prospectiveModel.setCity(selectedCity.getListId());
			shipTo=shipTo+selectedCity.getEnDescription()+ "\r\n";
		} else {
			prospectiveModel.setCity(0);
		}
		

		if (selectedCountry != null && selectedCountry.getListId() > 0) {
			prospectiveModel.setCountry(selectedCountry.getListId());
			shipTo=shipTo+selectedCountry.getEnDescription()+ "\r\n";
		} else {
			prospectiveModel.setCountry(0);
		}
		
		if (selectedHowDid != null && selectedHowDid.getListId() > 0) {
			prospectiveModel.setHowdidYouknowus(selectedHowDid.getListId());
		} else {
			prospectiveModel.setHowdidYouknowus(0);
		}

		

		if (selectedCompanyType != null && selectedCompanyType.getRecNo() > 0) {
			prospectiveModel.setCompanyTypeRefKey(selectedCompanyType
					.getRecNo());
		} else {
			prospectiveModel.setCompanyTypeRefKey(0);
		}

		if (selectedCompanySize != null && selectedCompanySize.getRecNo() > 0) {
			prospectiveModel.setCompanySizeRefKey(selectedCompanySize
					.getRecNo());
		} else {
			prospectiveModel.setCompanySizeRefKey(0);
		}

		if (selectedCurrentSoftwares != null
				&& selectedCurrentSoftwares.getRecNo() > 0) {
			prospectiveModel.setSoftwareRefKey(selectedCurrentSoftwares
					.getRecNo());
		} else {
			prospectiveModel.setSoftwareRefKey(0);
		}

		List<QbListsModel> QbListNames = hbadata
				.getNameFromQbListForValidation();
		List<ProspectiveModel> prospectiveModels = data
				.getNameFromProspectiveForValidation();
		
		prospectiveModel.setShipTo(shipTo);
		prospectiveModel.setContactPerson(contactName);
		prospectiveModel.setPhone(contactPhone);
		prospectiveModel.setAltPhone(contactMobile);
		prospectiveModel.setFax(contactFax);
		prospectiveModel.setEmail(contactEmail);
		if (prospectiveModel.getRecNo() > 0) 
		{
			for (QbListsModel ValidationName : QbListNames) {
				if (prospectiveModel.getName().equalsIgnoreCase(
						ValidationName.getName())
						&& prospectiveModel.getRecNo() != ValidationName
								.getRecNo()) {
					Messagebox.show("The Prospective Name already exist.",
							"Prospective List", Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
			}
			for (ProspectiveModel ValidationName : prospectiveModels) {
				if (prospectiveModel.getName().equalsIgnoreCase(
						ValidationName.getName())
						&& prospectiveModel.getRecNo() != ValidationName
								.getRecNo()) {
					Messagebox.show("The Prospective Name already exist.",
							"Prospective List", Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
			}
			
			prospectiveModel.setProspectiveContact(lstProspectiveContact);
			prospectiveModel.setPhotoPath(lstAtt.getFilepath());
			if(!prospectiveModel.getPhotoPath().equals(""))
				prospectiveModel.setPhotoExist("Y");	
			else
				prospectiveModel.setPhotoExist("N");
				
			result = data.updateProspectiveModel(prospectiveModel,lstDocAtt);
			if (result == 1) 
			{
				if (prospectiveModel.getRecNo() > 0)
					Clients.showNotification(
							"The Prospective Has Been Updated Successfully.",
							Clients.NOTIFICATION_TYPE_INFO, null,
							"middle_center", 10000, true);
			
				sendStatusChangeEmail("Old Prospective updated: " + prospectiveModel.getNotes(), false);
				
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshProspectiveList", args);
			} else
				Clients.showNotification("Erro at save Prospective !!");
		} 
		else 
		{
			for (QbListsModel ValidationName : QbListNames) {
				if (prospectiveModel.getName().equalsIgnoreCase(
						ValidationName.getName())) {
					Messagebox.show("The Prospective Name already exist.",
							"Prospective List", Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
			}
			if (null != prospectiveModel.getContactPerson()) 
			{
				lstProspectiveContact.remove(0);
				ProspectiveContactDetailsModel Contact = new ProspectiveContactDetailsModel();
				Contact.setName(prospectiveModel.getContactPerson());
				Contact.setTel(prospectiveModel.getPhone());
				Contact.setEmail(prospectiveModel.getEmail());
				Contact.setMobile(prospectiveModel.getAltPhone());
				Contact.setFax(prospectiveModel.getFax());
				lstProspectiveContact.add(Contact);
				if (null != prospectiveModel.getAltContactPerson()) 
				{
					ProspectiveContactDetailsModel Contact1 = new ProspectiveContactDetailsModel();
					Contact1.setName(prospectiveModel.getAltContactPerson());
					Contact1.setEmail(prospectiveModel.getcC());
					lstProspectiveContact.add(Contact1);
				}
			}
			
			prospectiveModel.setPhotoPath(lstAtt.getFilepath());
			if(!prospectiveModel.getPhotoPath().equals(""))
				prospectiveModel.setPhotoExist("Y");	
			else
				prospectiveModel.setPhotoExist("N");
			result = data.insertProspective(prospectiveModel,lstDocAtt);
			if (result == 1) {
				Clients.showNotification(
						"The Prospective Has Been Saved Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				
				sendStatusChangeEmail("New Prospective added: " + prospectiveModel.getNotes(), false);
				
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshProspectiveList", args);

			} else
				Clients.showNotification("Error at save Prospective !!");
		}
		x.detach();

	}
	
	private void sendStatusChangeEmail(String newStatus,boolean forPriority)
	{
		try
		{
			String[] cc =null;
			String[] to =null;
			String[] bcc =null;							
			String toMail="eng.chadi@gmail.com";//"hinawi@eim.ae,aya@hinawi.ae,chadi@hinawi.ae";//"eng.chadi@gmail.com";				
			to= toMail.split(",");	
					
			MailClient mc = new MailClient();
			String subject="";
			if(forPriority==false)
			subject="Status Changed Online for Prospective : " + prospectiveModel.getName();
			else
			subject="Priority Changed Online for Prospective : " + prospectiveModel.getName();
			logger.info("begin send email to" + to.toString());
			
			StringBuffer result=null;			
			result=new StringBuffer();
			//add style
			result.append("<html> <head> <style> ");
			//result.append(" table {font-family: arial, sans-serif;border-collapse: collapse;width: 100%;}");
			//result.append(" td, th {border: 1px solid #dddddd;text-align: left;padding: 0px;}");
			//result.append(" tr:nth-child(even) {background-color: #dddddd;}");
			result.append(" </style> </head>");
			//end style
			result.append("<body>");
			result.append(subject);
			result.append("<br/>");
			if(forPriority==false)
			result.append("<b><u> Updated Status: </u> </b> <br/>");
			else
			result.append("<b><u> Updated Priority: </u> </b> <br/>");	
			
			result.append("<table border='0'>");
			result.append("<tr>");
			result.append("<td width='200px'>Name </td>");
			result.append("<td>" + prospectiveModel.getName() +" <span style='color:red'>[Prospective]<span/> </td>");
			result.append("</tr>");
			
			if(forPriority==false)
			{
			result.append("<tr>");
			result.append("<td>New Status </td>");
			result.append("<td>" + newStatus +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Status Description </td>");
			result.append("<td>" + oldStatus +"</td>");
			result.append("</tr>");
			}
			else
			{
				result.append("<tr>");
				result.append("<td>New Priority </td>");
				if(prospectivePriority)
				result.append("<td style='color:green'><b>" + " Added as Priority"+"</b></td>");
				else
				result.append("<td style='color:red'><b>" + " Removed from Priority"+"</b></td>");
					
				result.append("</tr>");
				
				result.append("<tr>");
				result.append("<td>Status Description </td>");
				result.append("<td>" + newStatus +"</td>");
				result.append("</tr>");
			}
			
			result.append("<tr>");
			result.append("<td><hr/> </td>");
			result.append("<td>&nbsp;</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Contact Person: </td>");
			result.append("<td>" + prospectiveModel.getContactPerson() +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Phone: </td>");
			result.append("<td>" + contactPhone +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Alt. Phone: </td>");
			result.append("<td>" + contactMobile +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Alt. Contact: </td>");
			result.append("<td>" + prospectiveModel.getAltContactPerson()==""?"&nbsp;":prospectiveModel.getAltContactPerson() +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Email: </td>");
			result.append("<td>" + contactEmail==""?"&nbsp;":contactEmail +"</td>");
			result.append("</tr>");
								
			result.append("<tr>");
			result.append("<td>Country: </td>");
			logger.info("sss>> "+selectedCountry);		
			if(selectedCountry!=null)
			result.append("<td>" + selectedCountry.getEnDescription() +"</td>");
			else
			result.append("<td>" + "&nbsp;" +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>City: </td>");
			if(selectedCity!=null)
			result.append("<td>" +selectedCity.getEnDescription() +"</td>");
			else
			result.append("<td>" + "&nbsp;" +"</td>");	
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Send By: </td>");
			result.append("<td>" + dbUser.getUsername() +"</td>");
			result.append("</tr>");
			
			result.append("</table>");
			result.append("</body>");
			result.append("</html>");
			
			String messageBody=result.toString();	
			logger.info("body >> " + messageBody);
			mc.sendMochaMail(to,cc,bcc, subject, messageBody,true,null,true,"Customer Status","");
		}
		catch (Exception e) {
			logger.error("ERROR in sendStatusChangeEmail ----> EditCustomerViewModel", e);
		}
	}

	@Command
	@NotifyChange({ "disableSubOf" })
	public void doChecked() {
		if (selectedCheckBox == true) {
			disableSubOf = false;
			prospectiveModel.setSelectedSubOf(tempModel);
		} else {
			disableSubOf = true;
			prospectiveModel.setSelectedSubOf(prospectiveFilteredList.get(0));
		}

	}

	@Command
	@NotifyChange("lstProspectiveContact")
	public void deleteCheckItems(
			@BindingParam("row") ProspectiveContactDetailsModel row) {
		if (contactDetailsModel != null) {
			lstProspectiveContact.remove(contactDetailsModel);

			int srNo = 0;
			for (ProspectiveContactDetailsModel item : lstProspectiveContact) {
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if (lstProspectiveContact.size() == 0) {
			ProspectiveContactDetailsModel objItems = new ProspectiveContactDetailsModel();
			objItems.setLineNo(lstProspectiveContact.size() + 1);
			lstProspectiveContact.add(objItems);
		}
	}

	@SuppressWarnings("unused")
	@Command
	@NotifyChange("lstProspectiveContact")
	public void insertCheckItems(
			@BindingParam("row") ProspectiveContactDetailsModel row) {
		if (contactDetailsModel != null) {

			ProspectiveContactDetailsModel lastItem = lstProspectiveContact
					.get(lstProspectiveContact.size() - 1);

			ProspectiveContactDetailsModel obj = new ProspectiveContactDetailsModel();
			obj.setLineNo(lstProspectiveContact.size() + 1);
			obj.setDefaultFlag("N");
			lstProspectiveContact.add(obj);

		}

	}

	public ProspectiveContactDetailsModel getContactDetailsModel() {
		return contactDetailsModel;
	}

	public void setContactDetailsModel(
			ProspectiveContactDetailsModel contactDetailsModel) {
		this.contactDetailsModel = contactDetailsModel;
	}

	public List<HRListValuesModel> getCountries() {
		return countries;
	}

	public void setCountries(List<HRListValuesModel> countries) {
		this.countries = countries;
	}

	public List<HRListValuesModel> getCities() {
		return cities;
	}

	public void setCities(List<HRListValuesModel> cities) {
		this.cities = cities;
	}

	public HRListValuesModel getSelectedCountry() {
		return selectedCountry;
	}

	@NotifyChange({"cities"})
	public void setSelectedCountry(HRListValuesModel selectedCountry) {
		this.selectedCountry = selectedCountry;
		cities = hrData.getHRListValuesWithSub(3, selectedCountry.getListId(),
				"");
	}

	public HRListValuesModel getSelectedCity() {
		return selectedCity;
	}

	@NotifyChange({ "streets" })
	public void setSelectedCity(HRListValuesModel selectedCity) {
		this.selectedCity = selectedCity;
		streets = hrData.getHRListValuesWithSub(51, selectedCity.getListId(),
				"");
	}

	public List<HRListValuesModel> getHowDid() {
		return howDid;
	}

	public void setHowDid(List<HRListValuesModel> howDid) {
		this.howDid = howDid;
	}

	public HRListValuesModel getSelectedHowDid() {
		return selectedHowDid;
	}

	public void setSelectedHowDid(HRListValuesModel selectedHowDid) {
		this.selectedHowDid = selectedHowDid;
	}

	public List<HRListValuesModel> getStreets() {
		return streets;
	}

	public void setStreets(List<HRListValuesModel> streets) {
		this.streets = streets;
	}

	public HRListValuesModel getSelectedStreet() {
		return selectedStreet;
	}

	public void setSelectedStreet(HRListValuesModel selectedStreet) {
		this.selectedStreet = selectedStreet;
	}

	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	public boolean isDisableSubOf() {
		return disableSubOf;
	}

	public String getAttFile4() {
		return attFile4;
	}

	public void setAttFile4(String attFile4) {
		this.attFile4 = attFile4;
	}

	@SuppressWarnings("unused")
	@Command
	@NotifyChange({ "attFile4" })
	public void uploadFile(BindContext ctx, @BindingParam("attId") String attId) 
	{
		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		String filePath = "";
		String repository = System.getProperty("catalina.base")+ File.separator + "uploads" + File.separator;//CompanyProfile.CompanyProspectivesRepository();
		//Session sess = Sessions.getCurrent();
		String sessID = (Executions.getCurrent()).getDesktop().getId();
		//logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
		String dirPath =repository+sessID;     //repository+"Prospectives"+File.separator+companyId +File.separator+sessID ;  //repository + sessID;// session.getId();
		//logger.info("prospective >>>>>>" + dirPath);
		if(prospectiveModel.getRecNo()>0) //edit
		{
			//dirPath =repository+"Prospectives"+File.separator+companyId +File.separator+prospectiveModel.getRecNo();  
			dirPath=repository +prospectiveModel.getRecNo();  
		}
		
		File dir = new File(dirPath);
		if (!dir.exists())
			dir.mkdirs();
		
		//filePath=repository+"Prospectives"+File.separator+sessID+File.separator+event.getMedia().getName();	
		//logger.info("prospective 1 >>>>>>" + filePath);
		
		filePath = dirPath + File.separator+ event.getMedia().getName();   //+ attId + "." + event.getMedia().getFormat();
		//logger.info("prospective 2 >>>>>>" + filePath);
		
		createFile(event.getMedia().getStreamData(), filePath);
		editPhotoPath= filePath; //use when click in button show image
		
		if (attId.equals("4")) 
		{
			attFile4 = event.getMedia().getName();
			QuotationAttachmentModel objAtt = new QuotationAttachmentModel();
			objAtt.setFilename(attFile4);
			objAtt.setFilepath(filePath); //attId + "." + event.getMedia().getFormat());
			objAtt.setSessionid(sessID);
			objAtt.setImageMedia(event.getMedia());
			lstAtt = objAtt;
			Window win1 = (Window) Path.getComponent("/prospectiveModalDialog");
			Window win = (Window) win1.getFellow("uploadWindow");
			Image image = (Image) win.getFellow("image");
			BufferedImage image1 = null;
			try {
				image1 = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				logger.error(
						"ERROR in EditProspectiveViewModel ----> uploadFile", e);
			}
			image.setContent(image1);
			image.setWidth("100px");
			image.setHeight("80px");
		}

	}

	private int createFile(InputStream is, String filePath) {
		int res = 0;
		try {
			File file = new File(filePath);
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

	@Command
	@NotifyChange({ "disableSubOf" })
	public void loadImage() {
		Window win1 = (Window) Path.getComponent("/prospectiveModalDialog");
		Window win = (Window) win1.getFellow("uploadWindow");
		String filePath = "";
		Image image = (Image) win.getFellow("image");
		if (editPhotoPath != null && !editPhotoPath.equalsIgnoreCase("")) 
		{
			File file = new File(editPhotoPath);
			filePath = file.getAbsolutePath();
			BufferedImage image1 = null;
			try {
				image1 = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				logger.error(
						"ERROR in EditProspectiveViewModel ----> loadImage", e);
			}
			image.setContent(image1);
			image.setWidth("100px");
			image.setHeight("80px");
		}
		else {
			Clients.showNotification("There is no photo to download.",
					Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
					10000, true);
		}
	}
	
	@SuppressWarnings("unused")
	@Command 
	@NotifyChange({"attFile4","lstDocAtt"})
	public void uploadAttachFile(BindContext ctx,@BindingParam("attId") String attId )
	{
		try 
		{
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
			if(lstDocAtt!=null && lstDocAtt.size()>=10)
			{
				Clients.showNotification("You can upload maximum 10 files !!",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}

			for(QuotationAttachmentModel attachmentModel:lstDocAtt)
			{
				if(attachmentModel.getFilename().equalsIgnoreCase(event.getMedia().getName()))
				{
					Clients.showNotification("The file already uploaded please select another file.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					return;
				}
			}

			String filePath="";
			String repository =CompanyProfile.ProspectivesAttachmentRepository(); 					
			String sessID=(Executions.getCurrent()).getDesktop().getId();			
			String dirPath=repository+sessID;
			if(prospectiveModel.getRecNo()>0) //edit
			{				
				dirPath=repository +prospectiveModel.getRecNo();  
			}
			/*File dir = new File(dirPath);
			if (!dir.exists())
				dir.mkdirs();*/
			
			filePath = dirPath + File.separator+ event.getMedia().getName();  			
			if(attId.equals("1"))
			{				
				QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
				objAtt.setFilename(event.getMedia().getName());
				objAtt.setFilepath(filePath);
				objAtt.setSessionid(sessID);
				objAtt.setImageMedia(event.getMedia());
				lstDocAtt.add(objAtt);
				if(lstDocAtt!=null && lstDocAtt.size()>0)
					selectedAttchemnets=lstDocAtt.get(0);
			}
		}
		catch (Exception e) {
			logger.error("ERROR in EditProspectiveViewModel ----> uploadFile", e);			
		}
	}
	
	@Command 
	@NotifyChange({"attFile4","lstDocAtt"})
	public void deleteFromAttchamentList(@BindingParam("row") QuotationAttachmentModel obj)
	{
		try 
		{
			QuotationAttachmentModel tempModel=new QuotationAttachmentModel();
			for(QuotationAttachmentModel attachmentModel:lstDocAtt)
			{
				if(attachmentModel.getFilename().equalsIgnoreCase(obj.getFilename()))
				{
					tempModel=attachmentModel;
					break;
				}
			}
			lstDocAtt.remove(tempModel);
		}
		catch (Exception e) {
			logger.error("ERROR in EditProspectiveViewModel ----> deleteFromAttchamentList", e);			
		}
	}
	
	@Command
	public void downloadAttachment(@BindingParam("row") QuotationAttachmentModel obj)
	{
		if(obj.getFilepath()!=null && !obj.getFilepath().equalsIgnoreCase(""))
		{
			File file=new File(obj.getFilepath());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String mimeType=mimeTypesMap.getContentType(file);

			try {
				Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, obj.getFilename()); 

			}catch (FileNotFoundException e)
			{
				Clients.showNotification("There Is No Such File in server to download.(May be Deleted)",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			catch (Exception e) {
				logger.error("ERROR in EditTaskListViewModel ----> download", e);	
			}

		}
		else
		{
			Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	}

	public String getEditPhotoPath() {
		return editPhotoPath;
	}

	public void setEditPhotoPath(String editPhotoPath) {
		this.editPhotoPath = editPhotoPath;
	}

	public QuotationAttachmentModel getLstAtt() {
		return lstAtt;
	}

	public void setLstAtt(QuotationAttachmentModel lstAtt) {
		this.lstAtt = lstAtt;
	}

	public List<LocalItemModel> getCompanyTypes() {
		return companyTypes;
	}

	public void setCompanyTypes(List<LocalItemModel> companyTypes) {
		this.companyTypes = companyTypes;
	}

	public LocalItemModel getSelectedCompanyType() {
		return selectedCompanyType;
	}

	public void setSelectedCompanyType(LocalItemModel selectedCompanyType) {
		this.selectedCompanyType = selectedCompanyType;
	}

	public List<LocalItemModel> getCurrentSoftwares() {
		return currentSoftwares;
	}

	public void setCurrentSoftwares(List<LocalItemModel> currentSoftwares) {
		this.currentSoftwares = currentSoftwares;
	}

	public LocalItemModel getSelectedCurrentSoftwares() {
		return selectedCurrentSoftwares;
	}

	public void setSelectedCurrentSoftwares(
			LocalItemModel selectedCurrentSoftwares) {
		this.selectedCurrentSoftwares = selectedCurrentSoftwares;
	}

	public List<LocalItemModel> getCompanySize() {
		return companySize;
	}

	public void setCompanySize(List<LocalItemModel> companySize) {
		this.companySize = companySize;
	}

	public LocalItemModel getSelectedCompanySize() {
		return selectedCompanySize;
	}

	public void setSelectedCompanySize(LocalItemModel selectedCompanySize) {
		this.selectedCompanySize = selectedCompanySize;
	}

	public List<QbListsModel> getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(List<QbListsModel> salesRep) {
		this.salesRep = salesRep;
	}

	public QbListsModel getSelectedsalesRep() {
		return selectedsalesRep;
	}

	public void setSelectedsalesRep(QbListsModel selectedsalesRep) {
		this.selectedsalesRep = selectedsalesRep;
	}

	public int getIfnote() {
		return ifnote;
	}

	public void setIfnote(int ifnote) {
		this.ifnote = ifnote;
	}

	@Command
	@NotifyChange("lstProspectiveContact")
	public void contactChangeName(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			lstProspectiveContact.get(0).setName(contactInfo);
		}
		else
		{
			lstProspectiveContact.get(0).setName("");
		}
		BindUtils.postNotifyChange(null, null,EditProspectiveViewModel.this, "lstProspectiveContact");	
	}

	@Command
	@NotifyChange("lstProspectiveContact")
	public void contactChangePhone(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			lstProspectiveContact.get(0).setTel(contactInfo);
		}
		else
		{
			lstProspectiveContact.get(0).setTel("");
		}
		BindUtils.postNotifyChange(null, null,EditProspectiveViewModel.this, "lstProspectiveContact");	

	}
	
	@Command
	@NotifyChange("lstProspectiveContact")
	public void contactChangeAPhone(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			lstProspectiveContact.get(0).setMobile(contactInfo);
		}
		else
		{
			lstProspectiveContact.get(0).setMobile("");
		}
		BindUtils.postNotifyChange(null, null,EditProspectiveViewModel.this, "lstProspectiveContact");	

	}

	@Command
	@NotifyChange("lstProspectiveContact")
	public void contactChangeFax(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			lstProspectiveContact.get(0).setFax(contactInfo);
		}
		else
		{
			lstProspectiveContact.get(0).setFax("");
		}
		BindUtils.postNotifyChange(null, null,EditProspectiveViewModel.this, "lstProspectiveContact");	

	}

	
	@Command
	@NotifyChange("lstProspectiveContact")
	public void contactChangeemail(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			lstProspectiveContact.get(0).setEmail(contactInfo);
		}
		else
		{
			lstProspectiveContact.get(0).setEmail("");
		}
		BindUtils.postNotifyChange(null, null,EditProspectiveViewModel.this, "lstProspectiveContact");	

	}

	@Command
	@NotifyChange("contactName")
	public void customerChangeName(
			@BindingParam("cmp") ProspectiveContactDetailsModel contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactName=contactInfo.getName();
			BindUtils.postNotifyChange(null, null, EditProspectiveViewModel.this,"contactName");
		}
	}

	@Command
	@NotifyChange("contactPhone")
	public void customerChangePhone(
			@BindingParam("cmp") ProspectiveContactDetailsModel contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactPhone=contactInfo.getTel();
			BindUtils.postNotifyChange(null, null, EditProspectiveViewModel.this,"contactPhone");
		}
	}

	@Command
	@NotifyChange("contactMobile")
	public void customerChangeMobile(
			@BindingParam("cmp") ProspectiveContactDetailsModel contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactMobile=contactInfo.getMobile();
			BindUtils.postNotifyChange(null, null, EditProspectiveViewModel.this,"contactMobile");
		}
	}

	@Command
	@NotifyChange("contactFax")
	public void customerChangeFax(
			@BindingParam("cmp") ProspectiveContactDetailsModel contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactFax=contactInfo.getFax();
			BindUtils.postNotifyChange(null, null, EditProspectiveViewModel.this,"contactFax");
		}
	}

	@Command
	@NotifyChange("contactEmail")
	public void customerChangeEmail(
			@BindingParam("cmp") ProspectiveContactDetailsModel contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactEmail=contactInfo.getEmail();
			BindUtils.postNotifyChange(null, null, EditProspectiveViewModel.this,"contactEmail");
		}
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= hbadata.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==85)
			{
				companyRole=item;
				break;
			}
		}
	}

	/**
	 * @return the companyRole
	 */
	public MenuModel getCompanyRole() {
		return companyRole;
	}

	/**
	 * @param companyRole the companyRole to set
	 */
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public Boolean getProspectiveActive() {
		return prospectiveActive;
	}

	public void setProspectiveActive(Boolean prospectiveActive) {
		this.prospectiveActive = prospectiveActive;
	}

	public List<QuotationAttachmentModel> getLstDocAtt() {
		return lstDocAtt;
	}

	public void setLstDocAtt(List<QuotationAttachmentModel> lstDocAtt) {
		this.lstDocAtt = lstDocAtt;
	}

	public QuotationAttachmentModel getSelectedAttchemnets() {
		return selectedAttchemnets;
	}

	public void setSelectedAttchemnets(QuotationAttachmentModel selectedAttchemnets) {
		this.selectedAttchemnets = selectedAttchemnets;
	}

	public List<CustomerActivitiesModel> getStatusHistory() {
		return statusHistory;
	}

	public void setStatusHistory(List<CustomerActivitiesModel> statusHistory) {
		this.statusHistory = statusHistory;
	}
	
	
}
