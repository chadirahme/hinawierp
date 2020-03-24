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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import model.CustomerModel;
import model.CustomerStatusHistoryModel;
import model.HRListValuesModel;
import model.LocalItemModel;
import model.PaymentMethod;
import model.QbListsModel;
import model.TenantModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
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

public class EditCustomerViewModel {
	private Logger logger = Logger.getLogger(this.getClass());
	private CustomerData data = new CustomerData();
	private HBAData hbadata = new HBAData();
	private HRData hrData = new HRData();
	private CustomerModel selectedCustomer;
	private boolean canSave;
	private boolean activeCustomer;
	private Boolean priority;
	private boolean selectedCheckBox = false;
	private boolean disableSubOf = true;
	private CustomerModel tempModel = new CustomerModel();
	private List<CustomerModel> CustFilteredList;
	private String attFile4;
	private QuotationAttachmentModel lstAtt;
	private String editPhotoPath = "";
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRListValuesModel selectedCountry;
	private HRListValuesModel selectedCity;
	private List<HRListValuesModel> streets;
	private HRListValuesModel selectedStreet;
	private List<CustomerContact> contacts;
	private CustomerContact contact;
	private List<HRListValuesModel> companySize;
	private HRListValuesModel selectedCompanySize;
	//private List<LocalItemModel> companyTypes;
	private List<HRListValuesModel> companyTypes;
	
	private HRListValuesModel selectedCompanyType;
	private List<HRListValuesModel> currentSoftwares;
	private HRListValuesModel selectedCurrentSoftwares;
	private List<QbListsModel> salesRep;
	private QbListsModel selectedsalesRep;
	private List<PaymentMethod> paymentMethods;
	private PaymentMethod selectedPaymentMethod;
	private TenantModel tenant;
	private Date date;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Calendar c = Calendar.getInstance();
	private Date tradeLicenseExpiry;
	private Date passportExpiry;
	private Date residenceExpiry;
	private List<HRListValuesModel> nationality;
	private HRListValuesModel selectedNationality;
	private String tradeLicenseNo;
	private String passportNo;
	private String placeOfIssue;
	private String residenceVisaNo;
	private String sponserName;
	private String employmentDesignation;
	private String salaryIncome;
	private List<CustomerActivitiesModel> activities;
	private List<CustomerActivitiesModel> statusHistory;
	private boolean adminUser;
	private String contactName="";
	private String contactPhone="";
	private String contactMobile="";
	private String contactFax="";
	private String contactEmail="";
	private MenuModel companyRole;
	private String webuserName="";
	int webUserID = 0;
	String oldStatus="";
	int oldPriorityID=0;
	
	private List<QuotationAttachmentModel> lstDocAtt;
	private QuotationAttachmentModel selectedAttchemnets;
	private List<HRListValuesModel> howDid;
	private HRListValuesModel selectedHowDid;
	private CustomerContact compSetting;
	
	@SuppressWarnings("rawtypes")
	public EditCustomerViewModel() {
		try {
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int custKey = (Integer) map.get("custKey");
			String type = (String) map.get("type");
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			
			if (dbUser != null) {
				adminUser = dbUser.getFirstname().equals("admin");

				if(adminUser)
				{
					webUserID=0;
					webuserName="Admin";
				}
				else
				{
					webUserID=dbUser.getUserid();
					webuserName=dbUser.getUsername();
				}
			}

			Window win = (Window) Path.getComponent("/customerModalDialog");
			attFile4 = "No file chosen";
			if (type.equalsIgnoreCase("add")) {
				canSave = true;
				win.setTitle("Add Customer Info");
			} else if (type.equalsIgnoreCase("edit")) {
				canSave = true;
				win.setTitle("Edit Customer Info");
			} else {
				canSave = false;
				win.setTitle("View Customer Info");
			}

			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			compSetting = data.getCOMPANYSETTINGS();
			
			CustFilteredList = data.getCustomerOtherThanCurrentCutomer(custKey);
			tenant = data.getTenantCustomer(custKey);
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			streets = hrData.getHRListValues(51, "");
			companyTypes =hrData.getHRListValues(23, ""); //hbadata.GetLocalItemByRef(3);
			companySize = hrData.getHRListValues(145, "");//hbadata.GetLocalItemByRef(4);
			currentSoftwares = hrData.getHRListValues(146, "");//hbadata.GetLocalItemByRef(10);
			salesRep = hbadata.GetMasterData("SalesRep");
			paymentMethods = hbadata.getPaymentMethod();
			nationality = hrData.getHRListValues(1, "");
			howDid=hrData.getHRListValues(140, "");
			lstDocAtt=new ArrayList<QuotationAttachmentModel>();


			if (custKey > 0) 
			{
				String Match = "";
				selectedCustomer = data.getCustomerByKey(custKey);
				oldStatus=selectedCustomer.getStatusDescription();
				oldPriorityID=selectedCustomer.getPriority();
				contactName=selectedCustomer.getContact();
				contactPhone=selectedCustomer.getPhone();
				contactMobile=selectedCustomer.getAltphone();
				contactFax=selectedCustomer.getFax();
				contactEmail=selectedCustomer.getEmail();
				contacts = data.getCustomerContact(custKey);
				lstDocAtt=data.getAllAdditionalAttachments(custKey);

				activities = data.getCustomerActivities(custKey, webUserID);
				statusHistory = data.getCustomerStatusHistory(custKey, "C");

				if (selectedCustomer.getIsactive().equals("Y")) {
					activeCustomer = false;
				} else {
					activeCustomer = true;
				}

				if (selectedCustomer.getPriority() == 1) {
					priority = true;
				} else {
					priority = false;
				}
				if (null != tenant.getTradeLicenseExpiry()) {
					tradeLicenseExpiry = tenant.getTradeLicenseExpiry();
				} else {
					tradeLicenseExpiry = null;
				}
				if (null != tenant.getPassportExpiry()) {
					passportExpiry = tenant.getPassportExpiry();
				} else {
					passportExpiry = null;
				}

				if (null != tenant.getResidenceExpiry()) {
					residenceExpiry = tenant.getResidenceExpiry();
				} else {
					residenceExpiry = null;
				}

				tradeLicenseNo = tenant.getTradeLicenseNo();
				passportNo = tenant.getPassportNo();
				placeOfIssue = tenant.getPlaceOfIssue();
				residenceVisaNo = tenant.getResidenceVisaNo();
				sponserName = tenant.getSponserName();
				employmentDesignation = tenant.getEmploymentDesignation();
				salaryIncome = tenant.getSalaryIncome();

				selectedCustomer.setSubOfdropDown(CustFilteredList);
				selectedCustomer.setSelectedSubOf(CustFilteredList.get(0));
				editPhotoPath = selectedCustomer.getPhotoPath();
				if (selectedCustomer.getSublevel() > 0) {
					disableSubOf = false;
					selectedCheckBox = true;
					Pattern patternnew = Pattern.compile("(.*?):");
					Matcher matchernew = patternnew.matcher(selectedCustomer
							.getFullName());
					if (matchernew.find()) {
						Match = matchernew.group(1);
					}
					for (CustomerModel model : CustFilteredList) {
						if (model != null) {
							String slectedCustomerName = model.getName();
							if (slectedCustomerName.equalsIgnoreCase(Match)) {
								selectedCustomer.setSelectedSubOf(model);
								tempModel = model;
								break;
							}
						}
					}
				}
				for (HRListValuesModel listValuesModel : countries) {
					if (selectedCustomer.getCountry() != null
							&& selectedCustomer.getCountry() == listValuesModel
							.getListId()) {
						selectedCountry = listValuesModel;
						break;
					}
				}

				for (HRListValuesModel listValuesModel : nationality) {
					if (tenant.getNationality() != null
							&& tenant.getNationality() == listValuesModel
							.getListId()) {
						selectedNationality = listValuesModel;
						break;
					}
				}

				for (HRListValuesModel model : cities) {
					if (selectedCustomer.getCity() != null
							&& selectedCustomer.getCity() == model.getListId()) {
						selectedCity = model;
						break;
					}
				}
				for (HRListValuesModel model : streets) {
					if (selectedCustomer.getStreet() != null
							&& selectedCustomer.getStreet() == model
							.getListId()) {
						selectedStreet = model;
						break;
					}
				}
				for (HRListValuesModel model : companyTypes) {
					if (selectedCustomer.getCompanyTypeRefKey().equals(
							model.getListId())) {
						selectedCompanyType = model;
						break;
					}
				}

				for (HRListValuesModel model : companySize) {
					if (selectedCustomer.getCompanySizeRefKey().equals(
							model.getListId())) {
						selectedCompanySize = model;
						break;
					}
				}

				for (HRListValuesModel model : currentSoftwares) {
					if (selectedCustomer.getSoftwareRefKey() != null
							&& selectedCustomer.getSoftwareRefKey().equals(
									model.getListId())) {
						selectedCurrentSoftwares = model;
						break;
					}
				}
				
				for (HRListValuesModel model : howDid) {
					if (selectedCustomer.getHowdidYouknowus() != null
							&& selectedCustomer.getHowdidYouknowus().equals(
									model.getListId())) {
						selectedHowDid = model;
						break;
					}
				}
				

				for (PaymentMethod model : paymentMethods) {
					if (selectedCustomer.getPaymentMethod() != null
							&& selectedCustomer.getPaymentMethod() == model
							.getRec_No()) {
						selectedPaymentMethod = model;
						break;
					}
				}

				for (QbListsModel model : salesRep) {
					if (selectedCustomer.getSalesRepKey() != 0
							&& selectedCustomer.getSalesRepKey() == model
							.getRecNo()) {
						selectedsalesRep = model;
						break;
					}
				}

				if (contacts.size() == 0) {
					CustomerContact model = new CustomerContact();
					model.setLineNo(1);
					model.setDefaultFlag("Y");
					contacts.add(model);
				}

			} else {
				selectedCustomer = new CustomerModel();
				selectedCustomer.setSubOfdropDown(CustFilteredList);
				selectedCustomer.setSelectedSubOf(CustFilteredList.get(0));
				selectedCustomer.setName("");
				selectedCustomer.setArName("");
				selectedCustomer.setCompanyName("");
				selectedCustomer.setContact("");
				selectedCustomer.setPhone("");
				selectedCustomer.setAltphone("");
				selectedCustomer.setFax("");
				selectedCustomer.setEmail("");
				selectedCustomer.setIsactive("Y");
				selectedCustomer.setStatusDescription("");
				CustomerContact model = new CustomerContact();
				model.setLineNo(1);
				model.setDefaultFlag("Y");
				contacts = new ArrayList<CustomerContact>();
				contacts.add(model);
				selectedCustomer.setLastTrialBalance(df.parse(sdf.format(c
						.getTime())));
				activeCustomer = false;
				priority = false;
			}
			lstAtt = new QuotationAttachmentModel();

		} catch (Exception ex) {
			logger.error("ERROR in EditCustomerViewModel ----> init", ex);
		}
	}

	public Validator getTodoValidator() {
		return new AbstractValidator() {
			public void validate(ValidationContext ctx) {
				// get the form that will be applied to todo
				CustomerModel fx = (CustomerModel) ctx.getProperty().getValue();
				String name = fx.getName();

				if (Strings.isBlank(name)) {
					Clients.showNotification("Please fill all the required fields (*)  !!");
					// mark the validation is invalid, so the data will not
					// update to bean
					// and the further command will be skipped.
					ctx.setInvalid();
				}
			}
		};
	}

	@Command
	@NotifyChange({ "disableSubOf" })
	public void loadImage() {
		Window win1 = (Window) Path.getComponent("/customerModalDialog");
		Window win = (Window) win1.getFellow("uploadWindow");
		String filePath = "";
		Image image = (Image) win.getFellow("image");
		if (editPhotoPath != null && !editPhotoPath.equalsIgnoreCase("")) {
			File file = new File(editPhotoPath);
			filePath = file.getAbsolutePath();
			BufferedImage image1 = null;
			try {
				image1 = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				logger.error("ERROR in EditCustomerViewModel ----> loadImage",
						e);
			}
			image.setContent(image1);
			image.setWidth("100px");
			image.setHeight("80px");
		} else {
			Clients.showNotification("There is no photo to download.",
					Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
					10000, true);
		}
	}

	@Command
	@NotifyChange({ "disableSubOf" })
	public void doChecked() {
		if (selectedCheckBox == true) {
			disableSubOf = false;
			selectedCustomer.setSelectedSubOf(tempModel);
		} else {
			disableSubOf = true;
			selectedCustomer.setSelectedSubOf(CustFilteredList.get(0));
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	@NotifyChange({ "lstCustomers", "footer" })
	public void updateCustomer(@BindingParam("cmp") Window x,@BindingParam("type") int type) {
		if(type==2)
		{
			SaveStatusCommand(x);
			return;
		}
		int result = 0;
		String shipTo = "";
		if (selectedCustomer.getName().equalsIgnoreCase("")) {
			Messagebox.show("Please enter the Customer Name", "Customer List",
					Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (selectedCheckBox == false) {
			selectedCustomer.setSelectedSubOf(CustFilteredList.get(0));
		}
		if (activeCustomer) {
			selectedCustomer.setIsactive("N");
		} else {
			selectedCustomer.setIsactive("Y");
		}

		if (priority) {
			selectedCustomer.setPriority(1);
		} else {
			selectedCustomer.setPriority(0);
		}

		if (selectedCustomer.getSelectedSubOf() != null
				&& selectedCustomer.getSelectedSubOf().getName()
				.equalsIgnoreCase(selectedCustomer.getName())
				&& selectedCustomer.getSelectedSubOf().getCustkey() == selectedCustomer
				.getCustkey()) {
			Messagebox.show(
					"You cannot make an Customer a subCustomer of itself.",
					"Customer List", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}

		if (null != selectedCustomer.getSelectedSubOf()
				&& selectedCustomer.getSelectedSubOf().getName()
				.equalsIgnoreCase("none")) {
			CustomerModel subIytem = new CustomerModel();
			selectedCustomer.setSelectedSubOf(subIytem);
		}
		if(selectedCustomer.getContact()!=null && !selectedCustomer.getContact().equalsIgnoreCase(""))
			shipTo = shipTo + selectedCustomer.getContact() + "\r\n";
		if(selectedCustomer.getPhone()!=null && !selectedCustomer.getPhone().equalsIgnoreCase(""))
			shipTo = shipTo		+ selectedCustomer.getPhone() + "\r\n";
		if(selectedCustomer.getAltphone()!=null && !selectedCustomer.getAltphone().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getAltphone() + "\r\n";
		if(selectedCustomer.getFax()!=null && !selectedCustomer.getFax().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getFax() + "\r\n";
		if(selectedCustomer.getMobile()!=null && !selectedCustomer.getMobile().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getMobile() + "\r\n";
		if(selectedCustomer.getAltcontact()!=null && !selectedCustomer.getAltcontact().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getAltcontact() + "\r\n";
		if(selectedCustomer.getEmail()!=null && !selectedCustomer.getEmail().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getEmail() + "\r\n";
		if(selectedCustomer.getcC()!=null && !selectedCustomer.getcC().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getcC()+ "\r\n";
		if(selectedCustomer.getWebsite()!=null && !selectedCustomer.getWebsite().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getWebsite()+ "\r\n";
		if(selectedCustomer.getPobox()!=null && !selectedCustomer.getPobox().equalsIgnoreCase(""))
			shipTo = shipTo	+ selectedCustomer.getPobox()+ "\r\n";
		if (selectedStreet != null && selectedStreet.getListId() > 0) {
			selectedCustomer.setStreet(selectedStreet.getListId());
			shipTo = shipTo + selectedStreet.getEnDescription() + "\r\n";
		} else {
			selectedCustomer.setStreet(0);
		}

		
		if (selectedCity != null && selectedCity.getListId() > 0) {
			selectedCustomer.setCity(selectedCity.getListId());
			shipTo = shipTo + selectedCity.getEnDescription() + "\r\n";
		} else {
			selectedCustomer.setCity(0);
		}
		if (selectedCountry != null && selectedCountry.getListId() > 0) {
			selectedCustomer.setCountry(selectedCountry.getListId());
			shipTo = shipTo + selectedCountry.getEnDescription() + "\r\n";
		} else {
			selectedCustomer.setCountry(0);
		}
		
		if (selectedCompanyType != null && selectedCompanyType.getListId() > 0) {
			selectedCustomer.setCompanyTypeRefKey(selectedCompanyType
					.getListId());
		} else {
			selectedCustomer.setCompanyTypeRefKey(0);
		}

		if (selectedCompanySize != null && selectedCompanySize.getListId() > 0) {
			selectedCustomer.setCompanySizeRefKey(selectedCompanySize
					.getListId());
		} else {
			selectedCustomer.setCompanySizeRefKey(0);
		}

		if (selectedCurrentSoftwares != null
				&& selectedCurrentSoftwares.getListId() > 0) {
			selectedCustomer.setSoftwareRefKey(selectedCurrentSoftwares
					.getListId());
		} else {
			selectedCustomer.setSoftwareRefKey(0);
		}

		if (selectedsalesRep != null && selectedsalesRep.getRecNo() > 0) {
			selectedCustomer.setSalesRepKey(selectedsalesRep.getRecNo());
		} else {
			selectedCustomer.setSalesRepKey(0);
		}
		if (selectedPaymentMethod != null
				&& selectedPaymentMethod.getRec_No() > 0) {
			selectedCustomer
			.setPaymentMethod(selectedPaymentMethod.getRec_No());
		} else {
			selectedCustomer.setPaymentMethod(0);
		}
		
		if (selectedHowDid != null
				&& selectedHowDid.getListId() > 0) {
			selectedCustomer
			.setHowdidYouknowus(selectedHowDid.getListId());
		} else {
			selectedCustomer.setHowdidYouknowus(0);
		}

		if (selectedNationality != null && selectedNationality.getListId() > 0) {
			tenant.setNationality(selectedNationality.getListId());
		} else {
			tenant.setNationality(0);
		}

		tenant.setCustkey(selectedCustomer.getCustkey());
		tenant.setEmploymentDesignation(employmentDesignation);
		tenant.setPassportExpiry(passportExpiry);
		tenant.setPassportNo(passportNo);
		tenant.setPlaceOfIssue(placeOfIssue);
		tenant.setResidenceExpiry(residenceExpiry);
		tenant.setResidenceVisaNo(residenceVisaNo);
		tenant.setSalaryIncome(salaryIncome);
		tenant.setSponserName(sponserName);
		tenant.setTradeLicenseExpiry(tradeLicenseExpiry);
		tenant.setTradeLicenseNo(tradeLicenseNo);

		selectedCustomer.setTenant(tenant);

		/* List<CustomerModel> QbListNames=data.getCustomerList(""); */
		List<QbListsModel> QbListNames = hbadata
				.getNameFromQbListForValidation();

		selectedCustomer.setShipTo(shipTo);
		selectedCustomer.setContact(contactName);
		selectedCustomer.setPhone(contactPhone);
		selectedCustomer.setAltphone(contactMobile);
		selectedCustomer.setFax(contactFax);
		selectedCustomer.setEmail(contactEmail);
		if (selectedCustomer.getCustkey() > 0) 
		{
			for (QbListsModel ValidationName : QbListNames) {
				if (selectedCustomer.getName().equalsIgnoreCase(
						ValidationName.getName())
						&& (selectedCustomer.getCustkey() != ValidationName
						.getRecNo())) {
					Messagebox.show("The Customer Name already exist.",
							"Customer List", Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
			}

			selectedCustomer.setContacts(contacts);
			result = data.UpdateCustomerData(selectedCustomer, lstAtt ,lstDocAtt);
			if (result == 1) {
				if (selectedCustomer.getCustkey() > 0)
					Clients.showNotification(
							"The Customer Has Been Updated Successfully.",
							Clients.NOTIFICATION_TYPE_INFO, null,
							"middle_center", 10000, true);
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParent", args);
			} else
				Clients.showNotification("Erro at save Customer !!");
		}
		else
		{
			for (QbListsModel ValidationName : QbListNames) {
				if (selectedCustomer.getName().equalsIgnoreCase(
						ValidationName.getName())) {
					Messagebox.show("The Customer Name already exist.",
							"Customer List", Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
			}

			selectedCustomer.setTenant(tenant);
			selectedCustomer.setContacts(contacts);
			result = data.addCustomerData(selectedCustomer, lstAtt ,lstDocAtt);
			
			if (result == 1) {
				Clients.showNotification(
						"The Customer Has Been Saved Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				Map args = new HashMap();
				// args.put("compKey", selectedCompany.getCompKey());
				BindUtils.postGlobalCommand(null, null, "refreshParent", args);

			} else
				Clients.showNotification("Erro at save Customer !!");
		}

		if (selectedCustomer.getCustkey() > 0) {
			try {
				CustomerStatusHistoryModel model = new CustomerStatusHistoryModel();
				model.setRecNo(hbadata.getMaxID("CustomerStatusHistory",
						"RecNo"));
				model.setCustKey(selectedCustomer.getCustkey());

				model.setActionDate(df.parse(sdf.format(c.getTime())));

				if (selectedCustomer.getCustkey() == 0)
					model.setCreatedFrom("Create Customer Form Online");
				else
					model.setCreatedFrom("Edit Customer Form Online");
				model.setStatusDescription(selectedCustomer
						.getStatusDescription());
				//send email if status changed
				if(!oldStatus.equalsIgnoreCase(model.getStatusDescription()))
				{
					sendStatusChangeEmail(model.getStatusDescription(),false);
				}
				if(oldPriorityID!=selectedCustomer.getPriority())
				{
					sendStatusChangeEmail(model.getStatusDescription(),true);
				}
				model.setType("C");
				model.setTxnRecNo(0);
				model.setTxnRefNumber("");
				hbadata.saveCustomerStatusHistroyfromFeedback(model,webUserID,webuserName);
				// hbadata.updateCustomerStatusDescription(model);
			} catch (ParseException e) {
				logger.error("ERROR in updateCustomer ----> init", e);
			}
		}

		// Messagebox.show("Customer is Updated..","Update Customer",Messagebox.OK
		// , Messagebox.INFORMATION);
		x.detach();

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command	
	public void SaveStatusCommand(Window x)	
	{
		if (selectedCustomer.getCustkey() > 0) 
		{
			try {
				CustomerStatusHistoryModel model = new CustomerStatusHistoryModel();
				model.setRecNo(hbadata.getMaxID("CustomerStatusHistory",
						"RecNo"));
				model.setCustKey(selectedCustomer.getCustkey());
				model.setActionDate(df.parse(sdf.format(c.getTime())));

				if (selectedCustomer.getCustkey() == 0)
					model.setCreatedFrom("Create Customer Form Online");
				else
					model.setCreatedFrom("Edit Customer Form Online");
				model.setStatusDescription(selectedCustomer
						.getStatusDescription());
				//send email if status changed
				if(!oldStatus.equalsIgnoreCase(model.getStatusDescription()))
				{
				sendStatusChangeEmail(model.getStatusDescription() , false);							
				model.setType("C");
				model.setTxnRecNo(0);
				model.setTxnRefNumber("");
				hbadata.saveCustomerStatusHistroyfromFeedback(model,webUserID,webuserName);
				hbadata.updateCustomerStatusDescription(model);
				}
			} catch (ParseException e) {
				logger.error("ERROR in updateCustomer ----> init", e);
			}
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
			
			CustomerContact obj = compSetting;
			if(obj.getDefaultFlag().equalsIgnoreCase("N"))
				return;
			if(obj.getEmail().equalsIgnoreCase(""))
				obj.setEmail("eng.chadi@gmail.com");			
			
			if(!obj.getEmail().equalsIgnoreCase(""))
				cc=obj.getEmail().split(",");			
			
			String toMail="eng.chadi@gmail.com";
			if(forPriority)
			{
				toMail=obj.getEmail();
				cc=null;
			}
			else
			{
			toMail=getEmployeeSalesEmail(selectedCustomer.getSalesRepKey());
			}
			if(toMail.equalsIgnoreCase(""))
				toMail=obj.getEmail();
			to= toMail.split(",");	
					
			MailClient mc = new MailClient();
			String subject="";
			if(forPriority==false)
			subject="Status Changed Online for Job In Progress : " + selectedCustomer.getName();
			else
			subject="Priority Changed Online for : " + selectedCustomer.getName();
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
			result.append("<td>" + selectedCustomer.getName() +" <span style='color:red'>[Customer]<span/> </td>");
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
				if(priority)
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
			result.append("<td>" + selectedCustomer.getContact() +"</td>");
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
			result.append("<td>" + selectedCustomer.getAltcontact()==""?"&nbsp;":selectedCustomer.getAltcontact() +"</td>");
			result.append("</tr>");
			
			
			result.append("<tr>");
			result.append("<td>Mobile: </td>");
			result.append("<td>" + selectedCustomer.getMobile()==""?"&nbsp;":selectedCustomer.getMobile() +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Email: </td>");
			result.append("<td>" + contactEmail==""?"&nbsp;":contactEmail +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Cc: </td>");
			result.append("<td>" + selectedCustomer.getcC()==""?"&nbsp;":selectedCustomer.getcC() +"</td>");
			result.append("</tr>");
			
			result.append("<tr>");
			result.append("<td>Website: </td>");
			result.append("<td>" + selectedCustomer.getWebsite()==""?"&nbsp;":selectedCustomer.getWebsite() +"</td>");
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
			result.append("<td>" + webuserName +"</td>");
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

	private String getEmployeeSalesEmail(int SalesRepKey)
	{
		String email="";
		try
		{
		email=data.getSalesRepEmail(SalesRepKey);	
		}
		catch (Exception e) {
			logger.error("ERROR in getEmployeeSalesEmail ----> EditCustomerViewModel", e);
		}
		return email;
	}
	
	public CustomerModel getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CustomerModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	public boolean isActiveCustomer() {
		return activeCustomer;
	}

	public void setActiveCustomer(boolean activeCustomer) {
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the customer status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && activeCustomer==false)
		{
			Clients.showNotification("You are not allowed to Active the customer status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && activeCustomer==true)
		{
			Clients.showNotification("You are not allowed to In-Active the customer status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.activeCustomer = activeCustomer;
		}

	}

	/**
	 * @return the disableSubOf
	 */

	public boolean isDisableSubOf() {
		return disableSubOf;
	}

	/**
	 * @param disableSubOf
	 *            the disableSubOf to set
	 */
	public void setDisableSubOf(boolean disableSubOf) {
		this.disableSubOf = disableSubOf;
	}

	/**
	 * @return the selectedCheckBox
	 */
	public boolean isSelectedCheckBox() {
		return selectedCheckBox;
	}

	/**
	 * @param selectedCheckBox
	 *            the selectedCheckBox to set
	 */

	public void setSelectedCheckBox(boolean selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
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
	public void uploadFile(BindContext ctx, @BindingParam("attId") String attId) {
		UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
		String filePath = "";
		String repository = System.getProperty("catalina.base")
				+ File.separator + "uploads" + File.separator;
		Session sess = Sessions.getCurrent();
		String sessID = (Executions.getCurrent()).getDesktop().getId();
		logger.info("sessionId >>>>>>"
				+ (Executions.getCurrent()).getDesktop().getId());
		String dirPath = repository + sessID;// session.getId();
		File dir = new File(dirPath);

		if (!dir.exists())
			dir.mkdirs();
		filePath = dirPath + File.separator + attId + "."
				+ event.getMedia().getFormat();
		createFile(event.getMedia().getStreamData(), filePath);
		if (attId.equals("4")) {
			attFile4 = event.getMedia().getName();
			QuotationAttachmentModel objAtt = new QuotationAttachmentModel();
			objAtt.setFilename(attFile4);
			objAtt.setFilepath(attId + "." + event.getMedia().getFormat());
			objAtt.setSessionid(sessID);
			objAtt.setImageMedia(event.getMedia());
			lstAtt = objAtt;
			Window win1 = (Window) Path.getComponent("/customerModalDialog");
			Window win = (Window) win1.getFellow("uploadWindow");
			Image image = (Image) win.getFellow("image");
			BufferedImage image1 = null;
			try {
				image1 = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				logger.error("ERROR in EditCustomerViewModel ----> uploadFile",
						e);
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
			String repository =CompanyProfile.CustomersAttachmentRepository(); 					
			String sessID=(Executions.getCurrent()).getDesktop().getId();			
			String dirPath=repository+sessID;
			if(selectedCustomer.getCustkey()>0) //edit
			{				
				dirPath=repository +selectedCustomer.getCustkey();  
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
			logger.error("ERROR in EditCustomerViewModel ----> uploadAttachFile", e);			
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
			logger.error("ERROR in EditCustomerViewModel ----> deleteFromAttchamentList", e);			
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
				logger.error("ERROR in EditCustomerViewModel ----> download", e);	
			}

		}
		else
		{
			Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	}

	public QuotationAttachmentModel getLstAtt() {
		return lstAtt;
	}

	public void setLstAtt(QuotationAttachmentModel lstAtt) {
		this.lstAtt = lstAtt;
	}

	public String getEditPhotoPath() {
		return editPhotoPath;
	}

	public void setEditPhotoPath(String editPhotoPath) {
		this.editPhotoPath = editPhotoPath;
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

	@NotifyChange({ "cities" })
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

	public List<CustomerContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<CustomerContact> contacts) {
		this.contacts = contacts;
	}

	public CustomerContact getContact() {
		return contact;
	}

	public void setContact(CustomerContact contact) {
		this.contact = contact;
	}

	@Command
	@NotifyChange("contacts")
	public void deleteCheckItems(@BindingParam("row") CustomerContact row) {
		if (contact != null) {
			contacts.remove(contact);

			int srNo = 0;
			for (CustomerContact item : contacts) {
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if (contacts.size() == 0) {
			CustomerContact objItems = new CustomerContact();
			objItems.setLineNo(contacts.size() + 1);
			contacts.add(objItems);
		}
	}

	@SuppressWarnings("unused")
	@Command
	@NotifyChange("contacts")
	public void insertCheckItems(@BindingParam("row") CustomerContact row) {
		if (contact != null) {
			CustomerContact lastItem = contacts.get(contacts.size() - 1);
			CustomerContact obj = new CustomerContact();
			obj.setLineNo(contacts.size() + 1);
			obj.setDefaultFlag("N");
			contacts.add(obj);
		}

	}

	public Boolean getPriority() {
		return priority;
	}

	public void setPriority(Boolean priority) {
		this.priority = priority;
	}

	public List<HRListValuesModel> getCompanySize() {
		return companySize;
	}

	public void setCompanySize(List<HRListValuesModel> companySize) {
		this.companySize = companySize;
	}

	public HRListValuesModel getSelectedCompanySize() {
		return selectedCompanySize;
	}

	public void setSelectedCompanySize(HRListValuesModel selectedCompanySize) {
		this.selectedCompanySize = selectedCompanySize;
	}

	public List<HRListValuesModel> getCompanyTypes() {
		return companyTypes;
	}

	public void setCompanyTypes(List<HRListValuesModel> companyTypes) {
		this.companyTypes = companyTypes;
	}

	public HRListValuesModel getSelectedCompanyType() {
		return selectedCompanyType;
	}

	public void setSelectedCompanyType(HRListValuesModel selectedCompanyType) {
		this.selectedCompanyType = selectedCompanyType;
	}

	public List<HRListValuesModel> getCurrentSoftwares() {
		return currentSoftwares;
	}

	public void setCurrentSoftwares(List<HRListValuesModel> currentSoftwares) {
		this.currentSoftwares = currentSoftwares;
	}

	public HRListValuesModel getSelectedCurrentSoftwares() {
		return selectedCurrentSoftwares;
	}

	public void setSelectedCurrentSoftwares(
			HRListValuesModel selectedCurrentSoftwares) {
		this.selectedCurrentSoftwares = selectedCurrentSoftwares;
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

	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public PaymentMethod getSelectedPaymentMethod() {
		return selectedPaymentMethod;
	}

	public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
		this.selectedPaymentMethod = selectedPaymentMethod;
	}

	public TenantModel getTenant() {
		return tenant;
	}

	public void setTenant(TenantModel tenant) {
		this.tenant = tenant;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date creationdate) {
		this.date = creationdate;
	}

	public Date getTradeLicenseExpiry() {
		return tradeLicenseExpiry;
	}

	public void setTradeLicenseExpiry(Date tradeLicenseExpiry) {
		this.tradeLicenseExpiry = tradeLicenseExpiry;
	}

	public Date getPassportExpiry() {
		return passportExpiry;
	}

	public void setPassportExpiry(Date passportExpiry) {
		this.passportExpiry = passportExpiry;
	}

	public Date getResidenceExpiry() {
		return residenceExpiry;
	}

	public void setResidenceExpiry(Date residenceExpiry) {
		this.residenceExpiry = residenceExpiry;
	}

	public List<HRListValuesModel> getNationality() {
		return nationality;
	}

	public void setNationality(List<HRListValuesModel> nationality) {
		this.nationality = nationality;
	}

	public HRListValuesModel getSelectedNationality() {
		return selectedNationality;
	}

	public void setSelectedNationality(HRListValuesModel selectedNationality) {
		this.selectedNationality = selectedNationality;
	}

	public String getTradeLicenseNo() {
		return tradeLicenseNo;
	}

	public void setTradeLicenseNo(String tradeLicenseNo) {
		this.tradeLicenseNo = tradeLicenseNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getPlaceOfIssue() {
		return placeOfIssue;
	}

	public void setPlaceOfIssue(String placeOfIssue) {
		this.placeOfIssue = placeOfIssue;
	}

	public String getResidenceVisaNo() {
		return residenceVisaNo;
	}

	public void setResidenceVisaNo(String residenceVisaNo) {
		this.residenceVisaNo = residenceVisaNo;
	}

	public String getSponserName() {
		return sponserName;
	}

	public void setSponserName(String sponserName) {
		this.sponserName = sponserName;
	}

	public String getEmploymentDesignation() {
		return employmentDesignation;
	}

	public void setEmploymentDesignation(String employmentDesignation) {
		this.employmentDesignation = employmentDesignation;
	}

	public String getSalaryIncome() {
		return salaryIncome;
	}

	public void setSalaryIncome(String salaryIncome) {
		this.salaryIncome = salaryIncome;
	}

	public List<CustomerActivitiesModel> getActivities() {
		return activities;
	}

	public void setActivities(List<CustomerActivitiesModel> activities) {
		this.activities = activities;
	}

	@Command
	public void viewCustomerActivity(
			@BindingParam("row") CustomerActivitiesModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
//			switch (row.getType()) {
//			case "inquiry":
//
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "visits":
//
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "Quotation":
//				arg.put("cashInvoiceKey", row.getRecno() + "");
//				arg.put("type", "view");
//				Executions.createComponents("/hba/payments/editQuotation.zul",
//						null, arg);
//				break;
//
//			case "Delivery":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "Cash Payment":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "Cheque Payment":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "BankTransfer":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "Cheque Receipt Voucher":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "Cash & Cheque Receipt Voucher":
//				arg.put("reciptVoiucherKey", row.getRecno() + "");
//				arg.put("type", "view");
//				arg.put("canModify", false);
//				arg.put("canView", true);
//				arg.put("canPrint", true);
//				arg.put("canCreate", false);
//				Executions.createComponents(
//						"/hba/payments/editReciptVoucher.zul", null, arg);
//				break;
//
//			case "Cash Invoice":
//				arg.put("cashInvoiceKey", row.getRecno());
//				arg.put("type", "view");
//				arg.put("canModify", false);
//				arg.put("canView", true);
//				arg.put("canPrint", true);
//				arg.put("canCreate", false);
//				Executions.createComponents(
//						"/hba/payments/editCashInvoice.zul", null, arg);
//				break;
//
//			case "Credit Invoice":
//				arg.put("creditInvoiceKey", row.getRecno());
//				arg.put("type", "view");
//				arg.put("canModify", false);
//				arg.put("canView", true);
//				arg.put("canPrint", true);
//				arg.put("canCreate", false);
//				Executions.createComponents(
//						"/hba/payments/editCreditInvoice.zul", null, arg);
//				break;
//
//			case "Journal Voucher":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			case "CUC":
//				Clients.showNotification("Under Implimentation.",
//						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
//						10000, true);
//				return;
//
//			}

		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerFeebackSend ----> viewCustomerFeedback",
					ex);
		}
	}

	/*@GlobalCommand
	@NotifyChange({ "contacts" })
	public void refreshCustomerContact(int recno) {
		try {

			contacts.clear();
			contacts = data.getCustomerContact(recno);

		} catch (Exception ex) {
			logger.error("ERROR in EditCustomerViewModel ----> refreshCustomerContact", ex);
		}
	}*/

	@Command
	@NotifyChange("contacts")
	public void contactChangeName(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			contacts.get(0).setName(contactInfo);
		}
		else
		{
			contacts.get(0).setName("");
		}
		BindUtils.postNotifyChange(null, null,EditCustomerViewModel.this, "contacts");	
	}

	@Command
	@NotifyChange("contacts")
	public void contactChangePhone(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			contacts.get(0).setPhone(contactInfo);
		}
		else
		{
			contacts.get(0).setPhone("");
		}
		BindUtils.postNotifyChange(null, null,EditCustomerViewModel.this, "contacts");	

	}

	@Command
	@NotifyChange("contacts")
	public void contactChangeAPhone(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			contacts.get(0).setMobile(contactInfo);
		}
		else
		{
			contacts.get(0).setMobile("");
		}
		BindUtils.postNotifyChange(null, null,EditCustomerViewModel.this, "contacts");	

	}

	@Command
	@NotifyChange("contacts")
	public void contactChangeFax(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			contacts.get(0).setFax(contactInfo);
		}
		else
		{
			contacts.get(0).setFax("");
		}
		BindUtils.postNotifyChange(null, null,EditCustomerViewModel.this, "contacts");	

	}


	@Command
	@NotifyChange("contacts")
	public void contactChangeemail(@BindingParam("cmp") String contactInfo) {
		if (contactInfo!=null && contactInfo != "") {
			contacts.get(0).setEmail(contactInfo);
		}
		else
		{
			contacts.get(0).setEmail("");
		}
		BindUtils.postNotifyChange(null, null,EditCustomerViewModel.this, "contacts");	

	}

	@Command
	@NotifyChange("contactName")
	public void customerChangeName(
			@BindingParam("cmp") CustomerContact contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactName=contactInfo.getName();
			BindUtils.postNotifyChange(null, null, EditCustomerViewModel.this,"contactName");
		}
	}

	@Command
	@NotifyChange("contactPhone")
	public void customerChangePhone(
			@BindingParam("cmp") CustomerContact contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactPhone=contactInfo.getPhone();
			BindUtils.postNotifyChange(null, null, EditCustomerViewModel.this,"contactPhone");
		}
	}

	@Command
	@NotifyChange("contactMobile")
	public void customerChangeMobile(
			@BindingParam("cmp") CustomerContact contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactMobile=contactInfo.getMobile();
			BindUtils.postNotifyChange(null, null, EditCustomerViewModel.this,"contactMobile");
		}
	}

	@Command
	@NotifyChange("contactFax")
	public void customerChangeFax(
			@BindingParam("cmp") CustomerContact contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactFax=contactInfo.getFax();
			BindUtils.postNotifyChange(null, null, EditCustomerViewModel.this,"contactFax");
		}
	}

	@Command
	@NotifyChange("contactEmail")
	public void customerChangeEmail(
			@BindingParam("cmp") CustomerContact contactInfo) {

		if (contactInfo.getDefaultFlag().equalsIgnoreCase("Y")) {
			contactEmail=contactInfo.getEmail();
			BindUtils.postNotifyChange(null, null, EditCustomerViewModel.this,"contactEmail");
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
			if(item.getMenuid()==73)
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

	public List<CustomerActivitiesModel> getStatusHistory() {
		return statusHistory;
	}

	public void setStatusHistory(List<CustomerActivitiesModel> statusHistory) {
		this.statusHistory = statusHistory;
	}

	public CustomerContact getCompSetting() {
		return compSetting;
	}

	public void setCompSetting(CustomerContact compSetting) {
		this.compSetting = compSetting;
	}


}
