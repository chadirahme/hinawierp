package hba;

import home.QuotationAttachmentModel;
import hr.HRData;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.ApprovedMaterialsModel;
import model.ApprovedQuotationModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.CustomerStatusHistoryModel;
import model.DeliveryLineModel;
import model.DeliveryModel;
import model.CompSetupModel;
import model.DataFilter;
import model.HRListValuesModel;
import model.PrintModel;
import model.QbListsModel;
import model.SerialFields;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import setup.users.WebusersModel;
import common.NumberToWord;
import company.CompanyData;

public class EditDelivery {
	private Logger logger = Logger.getLogger(EditDelivery.class);
	private Date creationdate; 
	private DeliveryModel deliveryModel;
	private HBAData data=new HBAData();
	private DecimalFormat formatter = new DecimalFormat("#,###.00");
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private NumberToWord numbToWord=new NumberToWord();
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();
	private DeliveryLineModel selectedGridItems;
	private List<DeliveryLineModel> lstDeliveryCheckItems;
	private List <QbListsModel> lstDeliveryGridItem;
	private List <QbListsModel> lstInvcCustomerGridInvrtySite;
	private List <QbListsModel> lstInvcCustomerGridClass;
	private List<QbListsModel> lstInvcCustomerName;
	private QbListsModel selectedInvcCutomerName;
	private List<QbListsModel> lstInvcCustomerClass;
	private QbListsModel selectedInvcCutomerClass;
	private List<QbListsModel> lstInvcCustomerSalsRep;
	private QbListsModel selectedInvcCutomerSalsRep;
	private String deliveryNewSaleNo="";
	private String deliveryAddress="";
	private double tempTotalAmount;
	private List<QbListsModel> lstInvcCustomerSendVia;
	private QbListsModel selectedInvcCutomerSendVia;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	private String lblTotalCost="";
	private double totalAmount;
	private CompSetupModel compSetup;
	private int webUserID=0;
	private String webUserName="";
	private boolean seeTrasction=false;
	private String viewType="";
	private List<QbListsModel> lstItems;
	private List<QbListsModel> lstItemsForGrid;
	private DataFilter filter=new DataFilter();
	private String msgToBeDispalyedOnDelivery="";
	private boolean adminUser;
	private int  deliveryKey;
	private MenuModel companyRole;
	List<MenuModel> list;
	String actionTYpe="";
	private boolean canView=false;
	private boolean canModify=false;
	private boolean canPrint=false;
	private boolean canCreate=false;
	private String labelStatus="";
	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;
	CompanyData companyData=new CompanyData();
	private String selectedRadioButtons="";
	private int remindMeNoOfDays=0;
	private Date remindMeDate;
	private boolean disableRemindMeOn = false;
	private boolean disableRemindMe = false;
	private boolean showQuotation=false;
	private boolean showSelectMulti=false;
	private List<DeliveryLineModel> lstSelected;
	private int quotationNo=0;
	private boolean transformQ=false;
	private boolean posItems;
	private boolean statusFalg=false;
	private String status="";
	private boolean matchFlag=false;
	private String printYear="";
	private PrintModel objPrint;
	private boolean changeToDelivery=false;
	boolean isSkip = false;
		
	@SuppressWarnings("rawtypes")
	public EditDelivery(){
		try{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			String type=(String)map.get("type");
			viewType=type ==null ? "" : type;
			if(map.get("posItems") !=null)
				posItems=(Boolean) map.get("posItems");
			lstItems=data.fillQbItemsList();
			objPrint=new PrintModel();
			if(map.get("objPrint")!=null)
			{
				objPrint=(PrintModel) map.get("objPrint");
			}
			if(map.get("changeToDelivery")!=null)
				changeToDelivery=(Boolean) map.get("changeToDelivery");
			
			if(posItems)
			{
				lstItemsForGrid=null;
			}
			else
			{
				lstItemsForGrid=lstItems;
			}
			Calendar c = Calendar.getInstance();
			printYear=new SimpleDateFormat("yyyy").format(c.getTime());
			deliveryKey=(Integer)map.get("deliveryKey");
			actionTYpe=type;
			canView=(Boolean) map.get("canView");
			canModify=(Boolean) map.get("canModify");
			canPrint=(Boolean) map.get("canPrint");
			canCreate=(Boolean) map.get("canCreate");
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
				if(adminUser){
					webUserID=0;
					webUserName="Admin";
				}else{
					webUserID = dbUser.getUserid();
					webUserName=dbUser.getUsername();
				}
			}
			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
			for(WebusersModel model:lstUsers)
			{
				if(model.getUserid()==dbUser.getUserid()){
					selectedUser=model;
					break;
				}
			}
			Window win = (Window)Path.getComponent("/deliveryPopUp");
			if(type.equalsIgnoreCase("create")){
				win.setTitle("Add Delivery Info");
			}
			else if(type.equalsIgnoreCase("edit")){
				win.setTitle("Edit Delivery Info");
			}else{
				win.setTitle("View Delivery Info");
				canModify=false;
				canCreate=false;
			}
			deliveryModel=new DeliveryModel();
			lstInvcCustomerName=data.fillQbList("'Customer'");
			lstDeliveryGridItem=new ArrayList<QbListsModel>(); 
			lstInvcCustomerClass=data.GetMasterData("Class");
			lstInvcCustomerSalsRep=data.GetMasterData("SalesRep");
			compSetup=data.getDefaultSetUpInfoForCashInvoice();

			lstInvcCustomerSendVia= data.GetMasterData("SendVia");
			lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
			lstInvcCustomerGridClass=data.GetMasterData("GridClass");
			if(deliveryKey>0)
			{
				labelStatus="Edit";
				deliveryModel=data.getDeliveryByID(deliveryKey,webUserID,seeTrasction);
				List<DeliveryLineModel> deliveryModels =data.getDDeliveryGridDataByID(deliveryKey);

				for(QbListsModel cutomerNmae:lstInvcCustomerName)
				{
					if(cutomerNmae.getRecNo()==deliveryModel.getCustomerRefKey())
					{
						selectedInvcCutomerName=cutomerNmae;
						break;
					}
				}

				for(QbListsModel className:lstInvcCustomerClass)
				{
					if(className.getRecNo()==deliveryModel.getClassRefKey())
					{
						selectedInvcCutomerClass=className;
						break;
					}
				}



				for(QbListsModel salesRep:lstInvcCustomerSalsRep)
				{
					if(salesRep.getRecNo()==deliveryModel.getSalesRefKey())
					{
						selectedInvcCutomerSalsRep=salesRep;
						break;
					}

				}
				for(QbListsModel sendVia:lstInvcCustomerSendVia)
				{
					if(sendVia.getRecNo()==deliveryModel.getSendViaReffKey())
					{
						selectedInvcCutomerSendVia=sendVia;
						break;
					}

				}

				if (deliveryModel.getRemindDate() != null) {
					remindMeDate = df.parse(sdf.format(deliveryModel
							.getRemindDate()));
				}

				String remindFlag = deliveryModel.getRemindFalg();
				if (remindFlag != null) {
					if (remindFlag.equalsIgnoreCase("D")) {
						selectedRadioButtons = "A";
						disableRemindMe = true;
						remindMeNoOfDays = 0;
						disableRemindMeOn = false;
					} else if (remindFlag.equalsIgnoreCase("S")) {
						selectedRadioButtons = "B";
						disableRemindMeOn = true;
						disableRemindMe = false;
					} else if (remindFlag.equalsIgnoreCase("N")) {
						selectedRadioButtons = "C";
						disableRemindMe = true;
						disableRemindMeOn = true;
						remindMeNoOfDays = 0;
					}
				}
				remindMeNoOfDays = deliveryModel.getRemindDays();
				deliveryModel.setCheckNo(deliveryModel.getCheckNo());
				deliveryModel.setMemo(deliveryModel.getMemo());
				totalAmount=deliveryModel.getAmount();
				tempTotalAmount=deliveryModel.getAmount();
				deliveryNewSaleNo=deliveryModel.getRefNumber();
				creationdate=df.parse(sdf.format(deliveryModel.getTxnDate()));
				deliveryAddress=deliveryModel.getBillAddress1()+"\n"+deliveryModel.getBillAddress2()+"\n"+deliveryModel.getBillAddress3()+"\n"+deliveryModel.getBillAddress4()+"\n"+deliveryModel.getBillAddress5()+"";
				lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
				for(DeliveryLineModel editDeliveryGrid: deliveryModels)
				{
					DeliveryLineModel obj=new DeliveryLineModel();
					obj.setLineNo(lstDeliveryCheckItems.size()+1);
					//change here to read from lstItems instead of lstDeliveryGridItem					
					for(QbListsModel gridItem:lstItems)					
					{
						if(gridItem.getRecNo()==editDeliveryGrid.getItemRefKey())
						{
							obj.setSelectedItems(gridItem);
							break;
						}
					}
					for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
					{
						if(gridSite.getRecNo()==editDeliveryGrid.getInvertySiteKey())
						{
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if(gridSite.getRecNo()>0)
								obj.setHideSite(true);
							break;
						}
					}

					for(QbListsModel gridClass:lstInvcCustomerGridClass)
					{
						if(gridClass.getRecNo()==editDeliveryGrid.getClassRefKey())
						{
							obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
							break;
						}
					}
					obj.setQuotationLineNo(editDeliveryGrid.getQuotationLineNo());
					obj.setQuantity(editDeliveryGrid.getQuantity());
					obj.setServiceDate(editDeliveryGrid.getServiceDate());
					obj.setRate(editDeliveryGrid.getRate());
					obj.setQuantityInvoice(editDeliveryGrid.getQuantityInvoice());
					obj.setAmount(editDeliveryGrid.getAmount());
					obj.setDescription(editDeliveryGrid.getDescription());
					obj.setAvgCost(editDeliveryGrid.getAvgCost());
					obj.setOverrideItemAccountRefKey(0);
					obj.setIsTaxable("Y");
					obj.setOther1("0");
					obj.setOther2("0");
					obj.setSalesTaxCodeRefKey(0);
					lstDeliveryCheckItems.add(obj);
				}

			}
			else
			{
				labelStatus="Create";
				deliveryNewSaleNo=data.GetSaleNumber(SerialFields.delivery.toString());
				deliveryModel.setRefNumber(deliveryNewSaleNo);
				deliveryModel.setTransformQ("N");
				//Calendar c = Calendar.getInstance();		
				creationdate=df.parse(sdf.format(c.getTime()));
				remindMeDate=df.parse(sdf.format(c.getTime()));
				lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
				DeliveryLineModel objItems=new DeliveryLineModel();
				objItems.setLineNo(1);
				objItems.setQuantity(1);
				objItems.setShowCheck(true);
				objItems.setQuotationLineNo(0);
				lstDeliveryCheckItems.add(objItems);
				lblTotalCost="Amount :0.00";
				totalAmount=0.0;
				tempTotalAmount=0.0;
				selectedRadioButtons = "C";
				disableRemindMe = true;
				disableRemindMeOn = true;
				remindMeNoOfDays = 0;
				
				if(changeToDelivery)
				{
					//get the record 
					int RecNo=(Integer) map.get("RecNo");
					int customerKey=(Integer) map.get("customerKey");
					
					List<ApprovedQuotationModel> lst=data.getApprovedQuotation(0, webUserID,RecNo);
					getLstQuotationDelivery(lst.get(0));
					
					for(QbListsModel customerList:lstInvcCustomerName)
					{
						if(customerList.getRecNo()==customerKey)
						{
							selectedInvcCutomerName=customerList;
							break;
						}

					}
					
				}				
			}
		}
		catch(Exception ex)
		{
			logger.error("ERROR in EditDelivery ----> onLoad", ex);
		}
	}
	
	private void saveData(Window x) {
		try {
			if(validateData(true))
			{
				int tmpRecNo = 0;
				DeliveryModel obj = new DeliveryModel();

				if (deliveryKey == 0) {
					tmpRecNo = data.GetNewDeliveryRecNo();
				} else {
					tmpRecNo = deliveryModel.getRecNo();
				}
				if (selectedRadioButtons.equalsIgnoreCase("A")) {
					obj.setRemindFalg("D");
					obj.setRemindDate(remindMeDate);
				} else if (selectedRadioButtons.equalsIgnoreCase("B")) {
					obj.setRemindFalg("S");
					obj.setRemindDays(remindMeNoOfDays);
				} else {
					obj.setRemindFalg("N");
				}

				obj.setRecNo(tmpRecNo);
				if (deliveryModel.getTxnID() != null
						&& !deliveryModel.getTxnID().equalsIgnoreCase(""))
					obj.setTxnID(deliveryModel.getTxnID());
				else
					obj.setTxnID(" ");
				obj.setCustomerRefKey(selectedInvcCutomerName.getRecNo());
				if (null != selectedInvcCutomerClass) {
					obj.setClassRefKey(selectedInvcCutomerClass.getRecNo());
				} else {
					obj.setClassRefKey(0);
				}

				if (null != selectedInvcCutomerSalsRep) {
					obj.setSalesRefKey(selectedInvcCutomerSalsRep.getRecNo());
				} else {
					obj.setSalesRefKey(0);
				}
				obj.setpONumber(deliveryModel.getpONumber());
				obj.setTransformQ(deliveryModel.getTransformQ());
				obj.setTxnDate(creationdate);
				obj.setRefNumber(deliveryNewSaleNo);
				obj.setShipAddressNote(deliveryAddress);
				if (deliveryAddress != null) {
					String names[] = deliveryAddress.split("\n");
					if (names != null) {
						for (int i = 0; i < names.length; i++) {
							if (i == 0) {
								obj.setBillAddress1(names[i]);
								obj.setShipAddress1(names[i]);
								obj.setBillAddress2("");
								obj.setShipAddress2("");
								obj.setBillAddress3("");
								obj.setShipAddress3("");
								obj.setBillAddress4("");
								obj.setShipAddress4("");
								obj.setBillAddress5("");
								obj.setShipAddress5("");
							} else if (i == 1) {
								obj.setBillAddress2(names[i]);
								obj.setShipAddress2(names[i]);
							} else if (i == 2) {
								obj.setBillAddress3(names[i]);
								obj.setShipAddress3(names[i]);
							} else if (i == 3) {
								obj.setBillAddress4(names[i]);
								obj.setShipAddress4(names[i]);
							} else if (i >= 4) {
								obj.setBillAddress5(names[i]);
								obj.setShipAddress5(names[i]);
							}
						}

					}

				} else {
					obj.setBillAddress1("");
					obj.setShipAddress1("");
					obj.setBillAddress2("");
					obj.setShipAddress2("");
					obj.setBillAddress3("");
					obj.setShipAddress3("");
					obj.setBillAddress4("");
					obj.setShipAddress4("");
					obj.setBillAddress5("");
					obj.setShipAddress5("");
				}
				obj.setBillAddressCity(deliveryModel.getBillAddressCity());
				obj.setBillAddressState(deliveryModel.getBillAddressState());
				obj.setBillAddressPostalCode(deliveryModel.getBillAddressPostalCode());
				obj.setBillAddressCountry(deliveryModel.getBillAddressCountry());
				obj.setBillAddressNote(deliveryModel.getBillAddressNote());
				obj.setShipAddressCity(deliveryModel.getShipAddressCity());
				obj.setShipAddressState(deliveryModel.getShipAddressState());
				obj.setShipAddressPostalCode(deliveryModel.getShipAddressPostalCode());
				obj.setShipAddressCountry(deliveryModel.getShipAddressCountry());
				obj.setShipAddressNote(deliveryModel.getShipAddressNote());
				obj.setPending("N");
				obj.setfOB("0");
				obj.setDateCreated(creationdate);
				obj.setMemo(deliveryModel.getMemo());
				obj.setCustomerMsgRefKey(0);
				obj.setIsToBePrinted("Y");
				obj.setIsToEmailed("N");
				obj.setIsTaxIncluded("N");
				obj.setCustomerSalesTaxCodeRefKey(0);
				obj.setOther("");
				obj.setAmount(totalAmount);
				obj.setQuotationRecNo(0);
				if (null != selectedInvcCutomerSendVia) {
					obj.setSendViaReffKey(selectedInvcCutomerSendVia.getRecNo());
				} else {
					obj.setSendViaReffKey(0);
				}
				obj.setCustomField1("");
				obj.setCustomField2("");
				obj.setCustomField3("");
				obj.setCustomField4("");
				obj.setCustomField5("");
				obj.setStatus("C");
				obj.setDescriptionHIDE("N");
				obj.setQtyHIDE("N");
				obj.setClassHIDE("N");

				obj.setCustomField1(deliveryModel.getCustomField1());
				obj.setCustomField2(deliveryModel.getCustomField2());
				obj.setCustomField3(deliveryModel.getCustomField3());
				obj.setCustomField4(deliveryModel.getCustomField4());
				obj.setCustomField5(deliveryModel.getCustomField5());
				obj.setMemo(deliveryModel.getMemo());
				int result = 0;
				if (deliveryKey == 0) {
					result = data.addNewDelivery(obj, webUserID,webUserName);
				} else {
					result = data.updateDelivery(obj, webUserID,webUserName);
				}
				if (result > 0) {
					if (deliveryKey == 0) {
						data.ConfigSerialNumberCashInvoice(SerialFields.delivery,deliveryNewSaleNo, 0);
					}
					data.deleteDeliveryGridItems(tmpRecNo);
					for (DeliveryLineModel item : lstDeliveryCheckItems) {
						if (item.getSelectedItems() != null) {
							item.setServiceDate(creationdate);
							data.addDeliveryGridItems(item, tmpRecNo);
						}
					}
					if (deliveryKey == 0) {
						data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.Delivery.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getRefNumber(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
					} else {
						data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.Delivery.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getRefNumber(), obj.getTxnDate(),webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue());
					}
					if (deliveryKey > 0) {
						Clients.showNotification("The  Delivery  Has Been Updated Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",10000, true);
					} else {
						Clients.showNotification("The Delivery Has Been Created Successfully.",	Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000, true);
					}
					if(lstSelected!=null && lstSelected.size()>0){
						for (DeliveryLineModel item : lstSelected) {
							data.addQuotationDelivery(item,tmpRecNo);
						}
					}
					if(quotationNo>0){

						data.updateQuotationDelivery(quotationNo, tmpRecNo);

					}
					if (selectedInvcCutomerName.getRecNo() > 0) {
						Calendar c = Calendar.getInstance();
						CustomerStatusHistoryModel model = new CustomerStatusHistoryModel();
						model.setRecNo(data.getMaxID("CustomerStatusHistory","RecNo"));
						model.setCustKey(selectedInvcCutomerName.getRecNo());
						model.setActionDate(df.parse(sdf.format(c.getTime())));
						model.setCreatedFrom("Delivery");
						model.setStatusDescription(deliveryModel.getMemo());
						model.setType("C");
						model.setTxnRecNo(0);
						model.setTxnRefNumber(deliveryNewSaleNo);
						data.saveCustomerStatusHistroyfromFeedback(model,webUserID,webUserName);
					}
					//fill the qoutation list
					BindUtils.postGlobalCommand(null, null, "refreshDeliveryListParent", null);
					
					x.detach();
				}

			}
		} catch (Exception ex) {
			logger.error("ERROR in EditDelivery ----> save", ex);
		}
	}


	@Command
	@NotifyChange({"deliveryNewSaleNo","deliveryModel","creationdate","labelStatus","deliveryAddress","lstDeliveryCheckItems","deliveryModel","selectedInvcCutomerName","lstInvcCustomerClass","selectedInvcCutomerDepositeTo","selectedInvcCutomerSalsRep","selectedInvcCutomerPaymntMethd","selectedInvcCutomerSendVia","totalAmount","balance","amountPiad","totalAmount","tempTotalAmount","exChnage","transformQ","status=","matchFlag","statusFalg"})
	public void navigationDelivery(@BindingParam("cmp") String navigation)
	{
		try
		{
			Window win = (Window)Path.getComponent("/deliveryPopUp");
			deliveryModel=data.navigationDelivery(deliveryKey,webUserID,seeTrasction,navigation,actionTYpe);
			if(deliveryModel!=null && deliveryModel.getRecNo()>0)
			{
				if(deliveryModel.getTransformQ().equalsIgnoreCase("Y")){
					transformQ=true;
				}else{
					transformQ=false;
				}
				if(data.checkInvoicedDelivery(deliveryModel.getRecNo())){
					status="Invoiced";
					matchFlag=false;
					statusFalg=false;
				}else{
					if(deliveryModel.getStatus().equalsIgnoreCase("V")){
						status="Voided";
						matchFlag=false;
						statusFalg=false;					
					}else{
						status="Created";
						matchFlag=true;
						statusFalg=true;
					}
				}
				win.setTitle("Edit Delivery Info");
				actionTYpe="edit";
				labelStatus="Edit";
				deliveryKey=deliveryModel.getRecNo();
				List<DeliveryLineModel> deliveryModels =data.getDDeliveryGridDataByID(deliveryKey);
				for(QbListsModel cutomerNmae:lstInvcCustomerName)
				{
					if(cutomerNmae.getRecNo()==deliveryModel.getCustomerRefKey())
					{
						selectedInvcCutomerName=cutomerNmae;
						break;
					}
				}
				for(QbListsModel className:lstInvcCustomerClass)
				{
					if(className.getRecNo()==deliveryModel.getClassRefKey())
					{
						selectedInvcCutomerClass=className;
						break;
					}
				}
				for(QbListsModel salesRep:lstInvcCustomerSalsRep)
				{
					if(salesRep.getRecNo()==deliveryModel.getSalesRefKey())
					{
						selectedInvcCutomerSalsRep=salesRep;
						break;
					}

				}
				for(QbListsModel sendVia:lstInvcCustomerSendVia)
				{
					if(sendVia.getRecNo()==deliveryModel.getSendViaReffKey())
					{
						selectedInvcCutomerSendVia=sendVia;
						break;
					}

				}
				if (deliveryModel.getRemindDate() != null) {
					remindMeDate = df.parse(sdf.format(deliveryModel
							.getRemindDate()));
				}
				String remindFlag = deliveryModel.getRemindFalg();
				if (remindFlag != null) {
					if (remindFlag.equalsIgnoreCase("D")) {
						selectedRadioButtons = "A";
						disableRemindMe = true;
						remindMeNoOfDays = 0;
						disableRemindMeOn = false;
					} else if (remindFlag.equalsIgnoreCase("S")) {
						selectedRadioButtons = "B";
						disableRemindMeOn = true;
						disableRemindMe = false;
					} else if (remindFlag.equalsIgnoreCase("N")) {
						selectedRadioButtons = "C";
						disableRemindMe = true;
						disableRemindMeOn = true;
						remindMeNoOfDays = 0;
					}
				}
				remindMeNoOfDays = deliveryModel.getRemindDays();
				deliveryModel.setCheckNo(deliveryModel.getCheckNo());
				deliveryModel.setMemo(deliveryModel.getMemo());
				totalAmount=deliveryModel.getAmount();
				tempTotalAmount=deliveryModel.getAmount();
				deliveryNewSaleNo=deliveryModel.getRefNumber();
				creationdate=df.parse(sdf.format(deliveryModel.getTxnDate()));
				deliveryModel.setpONumber(deliveryModel.getpONumber());
				deliveryAddress=deliveryModel.getBillAddress1()+"\n"+deliveryModel.getBillAddress2()+"\n"+deliveryModel.getBillAddress3()+"\n"+deliveryModel.getBillAddress4()+"\n"+deliveryModel.getBillAddress5()+"";
				lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
				for(DeliveryLineModel editDeliveryGrid: deliveryModels)
				{
					DeliveryLineModel obj=new DeliveryLineModel();
					obj.setLineNo(lstDeliveryCheckItems.size()+1);
					//change here to read from lstItems instead of lstDeliveryGridItem					
					for(QbListsModel gridItem:lstItems)					
					{
						if(gridItem.getRecNo()==editDeliveryGrid.getItemRefKey())
						{
							obj.setSelectedItems(gridItem);
							break;
						}

					}
					for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
					{
						if(gridSite.getRecNo()==editDeliveryGrid.getInvertySiteKey())
						{
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if(gridSite.getRecNo()>0)
								obj.setHideSite(true);
							break;
						}
					}
					for(QbListsModel gridClass:lstInvcCustomerGridClass)
					{
						if(gridClass.getRecNo()==editDeliveryGrid.getClassRefKey())
						{
							obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
							break;
						}
					}
					obj.setInvoiceRecNo(editDeliveryGrid.getInvoiceRecNo());
					obj.setQuotationLineNo(editDeliveryGrid.getQuotationLineNo());
					obj.setQuantity(editDeliveryGrid.getQuantity());
					obj.setServiceDate(editDeliveryGrid.getServiceDate());
					obj.setRate(editDeliveryGrid.getRate());
					obj.setQuantityInvoice(editDeliveryGrid.getQuantityInvoice());
					obj.setAmount(editDeliveryGrid.getAmount());
					obj.setDescription(editDeliveryGrid.getDescription());
					obj.setAvgCost(editDeliveryGrid.getAvgCost());
					obj.setOverrideItemAccountRefKey(0);
					obj.setIsTaxable("Y");
					obj.setOther1("0");
					obj.setOther2("0");
					obj.setSalesTaxCodeRefKey(0);
					lstDeliveryCheckItems.add(obj);
				}
			}
			else
			{
				win.setTitle("Create Delivery Info");
				actionTYpe="create";
				labelStatus="Create";
				deliveryKey=0;
				deliveryModel=new DeliveryModel();
				deliveryModel.setRefNumber(data.GetSaleNumber(SerialFields.delivery.toString()));
				deliveryModel.setRecNo(0);
				deliveryModel.setTransformQ("N");
				deliveryNewSaleNo=deliveryModel.getRefNumber();
				Calendar c = Calendar.getInstance();		
				compSetup=data.getDefaultSetUpInfoForCashInvoice();
				creationdate=df.parse(sdf.format(c.getTime()));
				lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
				DeliveryLineModel objItems=new DeliveryLineModel();
				objItems.setLineNo(1);
				objItems.setQuantity(1);
				objItems.setShowCheck(true);
				objItems.setQuotationLineNo(0);
				lstDeliveryCheckItems.add(objItems);
				lblTotalCost="Amount :0.00";
				totalAmount=0.0;
				tempTotalAmount=0.0;
				selectedInvcCutomerName=null;
				selectedInvcCutomerClass=null;
				selectedInvcCutomerSalsRep=null;
				selectedInvcCutomerSendVia=null;
				deliveryAddress="";
				selectedRadioButtons = "C";
				disableRemindMe = true;
				disableRemindMeOn = true;
				remindMeNoOfDays = 0;
			}

			if(actionTYpe.equalsIgnoreCase("View"))
			{
				labelStatus="View";
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditDelivery ----> navigationDelivery", ex);			
		}
	}

	@Command
	@NotifyChange({"lstDeliveryCheckItems","lblTotalCost","totalAmount","tempTotalAmount"})
	public void insertCheckItems(@BindingParam("row") DeliveryLineModel row)
	{
		if(selectedGridItems!=null)
		{
			DeliveryLineModel lastItem=lstDeliveryCheckItems.get(lstDeliveryCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{					
				DeliveryLineModel obj=new DeliveryLineModel();
				obj.setLineNo(lstDeliveryCheckItems.size()+1);
				obj.setQuantity(1);
				obj.setShowCheck(true);
				obj.setQuotationLineNo(0);
				lstDeliveryCheckItems.add(obj);
			}
			else
			{
				Messagebox.show("To add new record,First select Item from the existing record!","Delivery",Messagebox.OK,Messagebox.INFORMATION);
				return;
			}
		}
	}


	@Command
	public void addNewDelivery(@BindingParam("cmp") Window x)
	{
		if((true))
		{
			saveData(x);
			//refresh the list parent

		}
	}



	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==315)
			{
				companyRole=item;
				break;
			}
		}
	}

	@Command
	public void openItemsCommands(@BindingParam("type") DeliveryLineModel type)
	{
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("selectedDeliveryGridDataRow",type );
			arg.put("lstItems", lstItems);

			//arg.put("delivery",type );
			//Executions.createComponents("/hba/payments/selectitems.zul", null, arg);
			Executions.createComponents("/hba/payments/itemspopup.zul", null,arg);	

		} catch (Exception ex) {
			logger.error("ERROR in EditDelivery ----> openItemsCommands",
					ex);
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand 
	@NotifyChange({ "lstDeliveryCheckItems", "lblTotalCost", "totalAmount"})
	public void refreshItemsParent(@BindingParam("selectedItem")QbListsModel selectedItem,@BindingParam("selectedDeliveryGridDataRow") DeliveryLineModel selectedRow)
	{
		try
		{
			final DeliveryLineModel type=selectedRow;
			selectedRow.setSelectedItems(selectedItem);
			if(selectedRow.getSelectedItems()!=null)
			{
				boolean hasSubAccount =type.getSelectedItems().getSubItemsCount()>0; // data.checkIfItemHasSubQuery(type.getSelectedItems().getName() + ":");
				if (hasSubAccount)
				{
					if (compSetup.getPostItem2Main().equals("Y")) 
					{
						Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Delivery",Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
							public void onEvent(Event evt)
									throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{
									selectInvoiceItemOnfuction(type);
									BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
									BindUtils.postNotifyChange(null,null,EditDelivery.this,"totalAmount");

								} else {
									Map args = new HashMap();
									args.put("result", "1");
									BindUtils.postGlobalCommand(null, null,"resetGrid", args);
									type.setSelectedItems(null);
									type.setQuantity(0);
									type.setDescription("");
									type.setSelectedInvcCutomerGridInvrtySiteNew(null);
									type.setSelectedInvcCutomerGridInvrtyClassNew(null);
									type.setAvgCost(0);
									type.setRate(0);
									type.setAmount(0);
									setLabelCheckTotalcost();
									BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
									BindUtils.postNotifyChange(null,null,EditDelivery.this,"totalAmount");
								}
							}

						});
					}
					else 
					{
						Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Delivery", Messagebox.OK,Messagebox.INFORMATION);
						type.setSelectedItems(null);
						type.setQuantity(0);
						type.setDescription("");
						type.setSelectedInvcCutomerGridInvrtySiteNew(null);
						type.setSelectedInvcCutomerGridInvrtyClassNew(null);
						type.setAvgCost(0);
						type.setRate(0);
						type.setAmount(0);
						setLabelCheckTotalcost();
						BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
						BindUtils.postNotifyChange(null,null,EditDelivery.this,"totalAmount");
					}
				}

				else 
				{
					selectInvoiceItemOnfuction(type);
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("ERROR in EditDelivery ----> refreshItemsParent",ex);
		}
	}

	@SuppressWarnings("rawtypes")
	@GlobalCommand 
	@NotifyChange({"lstDeliveryCheckItems"})
	public void getlstSelectItemDelivery(@BindingParam("lstCheckItemDelivery") List<QbListsModel> lstCheckItems)
	{		
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			DeliveryLineModel delivery=(DeliveryLineModel) map.get("delivery");
			if(delivery.isShowCheck()){
				if(lstDeliveryCheckItems.size()<=1){
					lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
				}
				for(QbListsModel items:lstCheckItems)
				{
					if(data.checkIfItemHasSubQuery(items.getName()+":"))
					{

						DeliveryLineModel obj=new DeliveryLineModel();
						obj.setLineNo(lstCheckItems.size()+1);
						for(QbListsModel gridItem:lstItems)					
						{
							if(gridItem.getRecNo()==items.getRecNo())
							{
								obj.setSelectedItems(gridItem);
								break;
							}

						}
						obj.setShowCheck(false);
						lstDeliveryCheckItems.add(obj);
					}
				}
			}else{

			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditDelivery ----> getlstSelectItemDelivery", ex);			
		}
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@GlobalCommand 
	@NotifyChange({"deliveryModel"})
	public void getCustomFieldDelivery(@BindingParam("delivery") DeliveryModel delivery)
	{		
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			deliveryModel.setCustomField1(delivery.getCustomField1());
			deliveryModel.setCustomField2(delivery.getCustomField2());
			deliveryModel.setCustomField3(delivery.getCustomField3());
			deliveryModel.setCustomField4(delivery.getCustomField4());
			deliveryModel.setCustomField5(delivery.getCustomField5());

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditDelivery ----> getCustomFieldDelivery", ex);			
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "lstDeliveryCheckItems", "lblTotalCost", "totalAmount"})
	public void selectDeliveryGridItems(@BindingParam("type") final DeliveryLineModel type) {

		if (type.getSelectedItems() != null) {

			boolean hasSubAccount = data.checkIfItemHasSubQuery(type.getSelectedItems().getName() + ":");
			if (hasSubAccount) {
				if (compSetup.getPostItem2Main().equals("Y")) {
					Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Delivery",Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								selectInvoiceItemOnfuction(type);

							} else {
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null,"resetGrid", args);
								type.setSelectedItems(null);
								type.setQuantity(0);
								type.setDescription("");
								type.setSelectedInvcCutomerGridInvrtySiteNew(null);
								type.setSelectedInvcCutomerGridInvrtyClassNew(null);
								type.setAvgCost(0);
								type.setRate(0);
								type.setAmount(0);
								setLabelCheckTotalcost();
								BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
								BindUtils.postNotifyChange(null,null,EditDelivery.this,"totalAmount");
							}
						}

					});
				} else {
					Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Delivery", Messagebox.OK,Messagebox.INFORMATION);
					type.setSelectedItems(null);
					type.setQuantity(0);
					type.setDescription("");
					type.setSelectedInvcCutomerGridInvrtySiteNew(null);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);
					type.setAvgCost(0);
					type.setRate(0);
					type.setAmount(0);
					setLabelCheckTotalcost();
					BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
					BindUtils.postNotifyChange(null,null,EditDelivery.this,"totalAmount");
				}
			} else {
				selectInvoiceItemOnfuction(type);
			}

		}
	}

	@NotifyChange({ "lstDeliveryCheckItems"})
	public void selectInvoiceItemOnfuction(final DeliveryLineModel type) {
		if (type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))
		{
			type.setHideSite(true);
			innerFuction(type);
		} else {
			innerFuction(type);
			logger.info("not hide false");
			type.setHideSite(false);
		}
	}

	@NotifyChange({ "lstDeliveryCheckItems"})
	public void innerFuction(DeliveryLineModel type) {
		CashInvoiceGridData objItems = data.getInvoiceGridData(type.getSelectedItems().getRecNo());
		if (objItems != null) {
			type.setRecNo(objItems.getRecNo());
			type.setItemType(objItems.getItemType());
			type.setAvgCost(objItems.getAvgCost());
			type.setDescription(objItems.getInvoicearabicDescription());
			type.setQuantityInvoice(objItems.getInvoiceQtyOnHand());
			type.setRate(objItems.getInvoiceRate());
			type.setQuantity(1);
			type.setAmount(type.getRate() * type.getQuantity());
			type.setServiceDate(creationdate);// dummy
			for (QbListsModel gridClass : lstInvcCustomerGridClass) {
				if (gridClass.getRecNo() == objItems.getSelectedClass()) {
					type.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
					break;
				}

			}
			type.setOverrideItemAccountRefKey(0);
			type.setIsTaxable("Y");
			type.setOther1("0");
			type.setOther2("0");
			type.setSalesTaxCodeRefKey(0);
			setLabelCheckTotalcost();
		}

	}

	private void setLabelCheckTotalcost() {
		double toalCheckItemsAmount = 0;
		for (DeliveryLineModel item : lstDeliveryCheckItems) {
			toalCheckItemsAmount += item.getAmount();
		}
		lblTotalCost = "Amount :"
				+ BigDecimal.valueOf(toalCheckItemsAmount).toPlainString();
		totalAmount = toalCheckItemsAmount;
		tempTotalAmount = toalCheckItemsAmount;
	}

	@Command
	@NotifyChange({ "lstDeliveryCheckItems", "lblTotalCost", "totalAmount" })
	public void deleteCheckItems(@BindingParam("row") DeliveryLineModel row) {
		if (selectedGridItems != null) {
			lstDeliveryCheckItems.remove(selectedGridItems);

			int srNo = 0;
			for (DeliveryLineModel item : lstDeliveryCheckItems) {
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if (lstDeliveryCheckItems.size() == 0) {
			DeliveryLineModel objItems = new DeliveryLineModel();
			objItems.setLineNo(lstDeliveryCheckItems.size() + 1);
			objItems.setQuantity(1);
			objItems.setShowCheck(true);
			lstDeliveryCheckItems.add(objItems);
		}
		setLabelCheckTotalcost();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "selectedInvcCutomerClass", "lstInvcCustomerClass" })
	public void selectDeliveryClass() {
		if (selectedInvcCutomerClass != null) {
			// check if class has sub class
			boolean hasSubAccount = data.checkIfClassHasSub(selectedInvcCutomerClass.getName() + ":");
			if (hasSubAccount) {
				if (compSetup.getPostOnMainClass().equals("Y")) {
					Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) {
							} else {
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null,"resetGrid", args);
								selectedInvcCutomerClass = null;
								BindUtils.postNotifyChange(null,null,EditDelivery.this,"selectedInvcCutomerClass");
							}
						}

					});
				} else

				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK,Messagebox.INFORMATION);
					selectedInvcCutomerClass = null;
					BindUtils.postNotifyChange(null, null,EditDelivery.this,"selectedInvcCutomerClass");
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "lstDeliveryCheckItems", "lblTotalCost", "totalAmount" })
	public void changeDeliveryCheckItems(@BindingParam("type") final DeliveryLineModel type,@BindingParam("parm") String parm) {
		if (type != null && type.getSelectedItems() != null) {
			if (parm.equals("qty")) {
				if(compSetup.getSellStockWithZero().equals("N")) {
					if(type.getQuantity()>type.getQuantityInvoice()){
						Messagebox.show("This Quantity is Not Available, You can't continue!","Item", Messagebox.OK , Messagebox.INFORMATION); 
						type.setSelectedItems(null);
						type.setHideSite(false); 
					}else{
						type.setAmount(type.getRate()* type.getQuantity());
					}
				}else{			 
					Messagebox.show("No Quantity Available, Do you want to continue?", "Item", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener(){
						public void onEvent(Event evt) throws InterruptedException { 
							if(evt.getName().equals("onYes")) 
							{ 
								type.setAmount(type.getRate()* type.getQuantity());
							}else{
								type.setSelectedItems(null);
								type.setHideSite(false); 
							} 
						} 
					});
				}


			}

			if (parm.equals("rate")) {
				if (type.getRate() > 99999999999.99) {
					Messagebox.show("The number is larger than the maximum value","Dlivery", Messagebox.OK,Messagebox.INFORMATION);
					return;
				}

				if (type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem")) {
					if (compSetup.getAllowSavingAvgCost().equals("N")) {
						if (Math.round((type.getAvgCost() * 100) / 100) > Math.round((type.getRate()) * 100 / 100)) {
							Messagebox.show("The rate is lesser than the Avg. Cost"+ type.getAmount()+ "., Do you want to continue","Delivery",Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
								public void onEvent(Event evt) throws InterruptedException {
									if (evt.getName().equals("onYes")) {
										type.setAmount(type.getRate() * type.getQuantity());
									} else {
										Map args = new HashMap();
										args.put("result", "1");
										BindUtils.postGlobalCommand(null, null,"resetGrid", args);
										type.setRate(0);
										BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
									}
								}

							});
						}
					} else {
						type.setAmount(type.getRate() * type.getQuantity());
					}
				} else {
					type.setAmount(type.getRate() * type.getQuantity());
				}
			}
			if (parm.equals("amount")) {
				type.setRate(type.getAmount() / type.getQuantity());
			}
			setLabelCheckTotalcost();
		} else {
			Messagebox.show("You Connot Continue.!Please select the Item from the grid","Delivery", Messagebox.OK, Messagebox.INFORMATION);
		}
	}


	@Command
	@NotifyChange({ "lstDeliveryCheckItems" })
	public void changeDescription(@BindingParam("type") final DeliveryLineModel type) {
		if(type.getDescription()!=null){
			data.updateQBItemsDesc(type);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "lstDeliveryCheckItems" })
	public void selectDeliveryGridClass(@BindingParam("type") final DeliveryLineModel type) {
		if (type.getSelectedInvcCutomerGridInvrtyClassNew() != null) {
			// check if class has sub class
			boolean hasSubAccount = data.checkIfClassHasSub(type.getSelectedInvcCutomerGridInvrtyClassNew().getName()+ ":");
			if (hasSubAccount) {
				if (compSetup.getPostOnMainClass().equals("Y")) {
					Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Delivery",Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
							} else {
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null,"resetGrid", args);
								type.setSelectedInvcCutomerGridInvrtyClassNew(null);
								BindUtils.postNotifyChange(null,null,EditDelivery.this,"lstDeliveryCheckItems");
							}
						}

					});
				} else {
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Delivery", Messagebox.OK,Messagebox.INFORMATION);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);
					BindUtils.postNotifyChange(null, null,EditDelivery.this,"lstDeliveryCheckItems");
				}
			}
		}
	}
	
	@Command
	public void allowSkipCommand()
	{
		isSkip=true;
	}
	
	@SuppressWarnings("unused")
	private boolean validateData(boolean Printflag) {
		final boolean isValid = true;
		if (selectedInvcCutomerName == null) {
			Messagebox.show("Select an existing Customer Name For This Transaction!", "Delivery", Messagebox.OK, Messagebox.INFORMATION);
			return false;
		}
		if (lstDeliveryCheckItems.size() <= 0) {
			Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Delivery", Messagebox.OK, Messagebox.INFORMATION);
			return false;
		}

		if(compSetup.getUseSalesFlow().equalsIgnoreCase("Y") && deliveryModel.getTransformQ().equalsIgnoreCase("N") && isSkip==false)
		{
			Messagebox.show("Work Flow Is Activate, Please Select Quotation To Create Delivery","Delivery",Messagebox.OK,Messagebox.INFORMATION);
			return false;							
		}
		
		/*if (tempTotalAmount != totalAmount) {
			Messagebox.show("The amount entered is not matching with actual grid total.!","Delivery", Messagebox.OK, Messagebox.INFORMATION);
			totalAmount = Double.parseDouble(BigDecimal.valueOf(tempTotalAmount).toPlainString());
			return false;
		}

		if (totalAmount <= 0) {
			Messagebox.show("The total amount should not be less than zero or zero!","Delivery", Messagebox.OK, Messagebox.INFORMATION);
			return false;
		}

		if (compSetup.getUseSalesRepComition().equalsIgnoreCase("Y")) {
			if (selectedInvcCutomerSalsRep == null) {
				Messagebox.show("Please select the sales rep to continue!!", "Delivery", Messagebox.OK, Messagebox.INFORMATION);
				return false;
			}
		}*/







		boolean RateValidation = false;
		boolean siteValidation = false;
		if (lstDeliveryCheckItems != null) {
			DeliveryLineModel lastItem = lstDeliveryCheckItems.get(lstDeliveryCheckItems.size() - 1);
			if (lastItem.getSelectedItems() != null) {
				if (((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && lastItem.getSelectedItems().getListType()
						.equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew() == null || lastItem
						.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo() == 0)) {
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Delivery", Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			} else {
				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Delivery", Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			for (DeliveryLineModel gridData : lstDeliveryCheckItems) {
				DeliveryLineModel objExp = gridData;
				if (objExp.getRate() > 0) {
					RateValidation = true;
					break;
				} else {
					RateValidation = false;
				}

			}
			for (DeliveryLineModel gridData : lstDeliveryCheckItems) {
				DeliveryLineModel objExp = gridData;
				if ((objExp.getSelectedItems().getListType()
						.equalsIgnoreCase("InventoryItem") || objExp
						.getSelectedItems().getListType()
						.equalsIgnoreCase("Inventory Assembly"))
						&& (objExp.getSelectedInvcCutomerGridInvrtySiteNew() == null || objExp
						.getSelectedInvcCutomerGridInvrtySiteNew()
						.getRecNo() == 0)) {
					siteValidation = true;
					break;

				} else {
					siteValidation = false;
				}
			}
			for (DeliveryLineModel gridData : lstDeliveryCheckItems) {
				DeliveryLineModel objExp = gridData;
				if (objExp.getQuantity() == 0) {
					Messagebox.show("Please Enter The Quantity,Empty Transaction is not allowed !!!","Delivery", Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}

			}


			if (siteValidation && (compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y"))) {
				Messagebox
				.show("To Save this record,First select Site Name from the existing records in the Grid!", "Delivery", Messagebox.OK, Messagebox.INFORMATION);
				return false;
			}
			DeliveryLineModel objExp = lstDeliveryCheckItems.get(0);
			if (objExp.getSelectedItems() == null) {
				Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Delivery", Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

		}

		if (Printflag) {
			if ((deliveryNewSaleNo != null)	&& (data.checkIfSerialNumberIsDuplicateForDelivery(deliveryNewSaleNo, deliveryModel.getRecNo()) == true)) {
				Messagebox.show("Duplicate Delivery Number!", "Delivery",Messagebox.OK, Messagebox.INFORMATION);
				return false;
			}
			if (deliveryNewSaleNo == null) {
				Messagebox.show("Please Enter the Delivery Number", "Delivery", Messagebox.OK, Messagebox.INFORMATION);
				return false;
			}
		}

		return isValid;

	}

	@Command
	public void openCustomField(){
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("deliveryModel",deliveryModel);
			arg.put("deliveryKey",deliveryKey);
			arg.put("seeTrasction",seeTrasction);
			arg.put("webUserID",webUserID);
			Executions.createComponents("/hba/payments/customFields.zul", null, arg);

		} catch (Exception ex) {
			logger.error("ERROR in EditDelivery ----> openCustomField",ex);
		}
	}

	@Command
	public void selectQuotation(){
		try {
			if(labelStatus.equalsIgnoreCase("Create")){
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("customerKey",selectedInvcCutomerName.getRecNo());
				Executions.createComponents("/hba/payments/selectApproveQuotation.zul", null, arg);
			}

		} catch (Exception ex) {
			logger.error("ERROR in EditDelivery ----> selectQuotation",ex);
		}
	}


	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public HBAData getData() {
		return data;
	}
	public void setData(HBAData data) {
		this.data = data;
	}
	public DecimalFormat getFormatter() {
		return formatter;
	}
	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
	}
	public List<QuotationAttachmentModel> getLstAtt() {
		return lstAtt;
	}
	public void setLstAtt(List<QuotationAttachmentModel> lstAtt) {
		this.lstAtt = lstAtt;
	}
	public QuotationAttachmentModel getSelectedAttchemnets() {
		return selectedAttchemnets;
	}
	public void setSelectedAttchemnets(QuotationAttachmentModel selectedAttchemnets) {
		this.selectedAttchemnets = selectedAttchemnets;
	}
	public boolean isCreatePdfSendEmail() {
		return createPdfSendEmail;
	}
	public void setCreatePdfSendEmail(boolean createPdfSendEmail) {
		this.createPdfSendEmail = createPdfSendEmail;
	}
	public NumberToWord getNumbToWord() {
		return numbToWord;
	}
	public void setNumbToWord(NumberToWord numbToWord) {
		this.numbToWord = numbToWord;
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
	public HRData getHrData() {
		return hrData;
	}
	public void setHrData(HRData hrData) {
		this.hrData = hrData;
	}

	public List<QbListsModel> getLstDeliveryGridItem() {
		return lstDeliveryGridItem;
	}
	public void setLstDeliveryGridItem(List<QbListsModel> lstDeliveryGridItem) {
		this.lstDeliveryGridItem = lstDeliveryGridItem;
	}
	public List<QbListsModel> getLstInvcCustomerGridInvrtySite() {
		return lstInvcCustomerGridInvrtySite;
	}
	public void setLstInvcCustomerGridInvrtySite(
			List<QbListsModel> lstInvcCustomerGridInvrtySite) {
		this.lstInvcCustomerGridInvrtySite = lstInvcCustomerGridInvrtySite;
	}
	public List<QbListsModel> getLstInvcCustomerGridClass() {
		return lstInvcCustomerGridClass;
	}
	public void setLstInvcCustomerGridClass(
			List<QbListsModel> lstInvcCustomerGridClass) {
		this.lstInvcCustomerGridClass = lstInvcCustomerGridClass;
	}
	public List<QbListsModel> getLstInvcCustomerName() {
		return lstInvcCustomerName;
	}
	public void setLstInvcCustomerName(List<QbListsModel> lstInvcCustomerName) {
		this.lstInvcCustomerName = lstInvcCustomerName;
	}
	public QbListsModel getSelectedInvcCutomerName() {
		return selectedInvcCutomerName;
	}


	@SuppressWarnings("unchecked")
	@NotifyChange({"deliveryModel","deliveryAddress","showQuotation"})
	public void setSelectedInvcCutomerName(QbListsModel selectedInvcCutomerName) {
		this.selectedInvcCutomerName = selectedInvcCutomerName;
		isSkip=false;
		if(selectedInvcCutomerName!=null)
		{
			CashInvoiceModel obj=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());		
			if(obj.getBillAddress5()!=null && !obj.getBillAddress5().equalsIgnoreCase(""))
				deliveryAddress=obj.getBillAddress5();
			else
				deliveryAddress=obj.getFullname();
			deliveryModel.setBillAddress1(obj.getBillAddress1());
			deliveryModel.setBillAddress2(obj.getBillAddress2());
			deliveryModel.setBillAddress3(obj.getBillAddress3());
			deliveryModel.setBillAddress4(obj.getBillAddress4());
			deliveryModel.setBillAddress5("");
			deliveryModel.setBillAddressCity(obj.getBillAddressCity());
			deliveryModel.setBillAddressState(obj.getBillAddressState());
			deliveryModel.setBillAddressCountry(obj.getBillAddressCountry());
			deliveryModel.setBillAddressPostalCode(obj.getBillAddressPostalCode());
			deliveryModel.setBillAddressNote("");
			deliveryModel.setShipAddress1(obj.getBillAddress1());
			deliveryModel.setShipAddress2(obj.getBillAddress2());
			deliveryModel.setShipAddress3(obj.getBillAddress3());
			deliveryModel.setShipAddress4(obj.getBillAddress4());
			deliveryModel.setShipAddress5("");
			deliveryModel.setShipAddressCity(obj.getBillAddressCity());
			deliveryModel.setShipAddressState(obj.getBillAddressState());
			deliveryModel.setShipAddressCountry(obj.getBillAddressCountry());
			deliveryModel.setShipAddressPostalCode(obj.getBillAddressPostalCode());
			deliveryModel.setShipAddressNote("");
			deliveryModel.setCustomerName(obj.getName());
			deliveryModel.setAccountName(obj.getAccountName());
			deliveryModel.setAccountNo(obj.getAccountNo());
			deliveryModel.setBankName(obj.getBankName());
			deliveryModel.setBranchName(obj.getBranchName());
			deliveryModel.setiBANNo(obj.getiBANNo());
			deliveryModel.setEmail(obj.getEmail());
			deliveryModel.setTotalBalance(obj.getTotalBalance());
			deliveryModel.setPhone(obj.getPhone());
			deliveryModel.setFax(obj.getFax());
			deliveryModel.setPrintChequeAs(obj.getPrintChequeAs());

			if(!data.checkCustomerQuotation(selectedInvcCutomerName.getRecNo())){
				showQuotation=false;
			}
			else
			{
				showQuotation=true;
				if(compSetup.getAllowToSkip().equals("Y"))
				{
					Messagebox.show("Customer has Qoutation. Do you want to load ?","Delivery", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{
								isSkip=true;
							}
							if (evt.getName().equals("onNo"))
							{
								isSkip=false;
							}
						}
					});
				}
			}

		}
		else
		{
			Messagebox.show("Invlaid Customer Name !!");			
		}
	}
	public List<QbListsModel> getLstInvcCustomerClass() {
		return lstInvcCustomerClass;
	}
	public void setLstInvcCustomerClass(List<QbListsModel> lstInvcCustomerClass) {
		this.lstInvcCustomerClass = lstInvcCustomerClass;
	}
	public QbListsModel getSelectedInvcCutomerClass() {
		return selectedInvcCutomerClass;
	}
	public void setSelectedInvcCutomerClass(QbListsModel selectedInvcCutomerClass) {
		this.selectedInvcCutomerClass = selectedInvcCutomerClass;
	}

	public List<QbListsModel> getLstInvcCustomerSalsRep() {
		return lstInvcCustomerSalsRep;
	}
	public void setLstInvcCustomerSalsRep(List<QbListsModel> lstInvcCustomerSalsRep) {
		this.lstInvcCustomerSalsRep = lstInvcCustomerSalsRep;
	}
	public QbListsModel getSelectedInvcCutomerSalsRep() {
		return selectedInvcCutomerSalsRep;
	}
	public void setSelectedInvcCutomerSalsRep(
			QbListsModel selectedInvcCutomerSalsRep) {
		this.selectedInvcCutomerSalsRep = selectedInvcCutomerSalsRep;
	}
	public String getDeliveryNewSaleNo() {
		return deliveryNewSaleNo;
	}
	public void setDeliveryNewSaleNo(String deliveryNewSaleNo) {
		this.deliveryNewSaleNo = deliveryNewSaleNo;
	}
	public String getdeliveryAddress() {
		return deliveryAddress;
	}
	public void setdeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public double getTempTotalAmount() {
		return tempTotalAmount;
	}
	public void setTempTotalAmount(double tempTotalAmount) {
		this.tempTotalAmount = tempTotalAmount;
	}
	public List<QbListsModel> getLstInvcCustomerSendVia() {
		return lstInvcCustomerSendVia;
	}
	public void setLstInvcCustomerSendVia(List<QbListsModel> lstInvcCustomerSendVia) {
		this.lstInvcCustomerSendVia = lstInvcCustomerSendVia;
	}
	public QbListsModel getSelectedInvcCutomerSendVia() {
		return selectedInvcCutomerSendVia;
	}
	public void setSelectedInvcCutomerSendVia(
			QbListsModel selectedInvcCutomerSendVia) {
		this.selectedInvcCutomerSendVia = selectedInvcCutomerSendVia;
	}
	public DateFormat getDf() {
		return df;
	}
	public void setDf(DateFormat df) {
		this.df = df;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public DecimalFormat getDcf() {
		return dcf;
	}
	public void setDcf(DecimalFormat dcf) {
		this.dcf = dcf;
	}
	public String getLblTotalCost() {
		return lblTotalCost;
	}
	public void setLblTotalCost(String lblTotalCost) {
		this.lblTotalCost = lblTotalCost;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public CompSetupModel getCompSetup() {
		return compSetup;
	}
	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}
	public int getWebUserID() {
		return webUserID;
	}
	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}
	public String getWebUserName() {
		return webUserName;
	}
	public void setWebUserName(String webUserName) {
		this.webUserName = webUserName;
	}
	public boolean isSeeTrasction() {
		return seeTrasction;
	}
	public void setSeeTrasction(boolean seeTrasction) {
		this.seeTrasction = seeTrasction;
	}
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public List<QbListsModel> getLstItems() {
		return lstItems;
	}
	public void setLstItems(List<QbListsModel> lstItems) {
		this.lstItems = lstItems;
	}
	public DataFilter getFilter() {
		return filter;
	}
	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
	public String getMsgToBeDispalyedOnDelivery() {
		return msgToBeDispalyedOnDelivery;
	}
	public void setMsgToBeDispalyedOnDelivery(String msgToBeDispalyedOnDelivery) {
		this.msgToBeDispalyedOnDelivery = msgToBeDispalyedOnDelivery;
	}
	public boolean isAdminUser() {
		return adminUser;
	}
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}
	public int getDeliveryKey() {
		return deliveryKey;
	}
	public void setDeliveryKey(int deliveryKey) {
		this.deliveryKey = deliveryKey;
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	public List<MenuModel> getList() {
		return list;
	}
	public void setList(List<MenuModel> list) {
		this.list = list;
	}
	public String getActionTYpe() {
		return actionTYpe;
	}
	public void setActionTYpe(String actionTYpe) {
		this.actionTYpe = actionTYpe;
	}
	public boolean isCanView() {
		return canView;
	}
	public void setCanView(boolean canView) {
		this.canView = canView;
	}
	public boolean isCanModify() {
		return canModify;
	}
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}
	public boolean isCanPrint() {
		return canPrint;
	}
	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}
	public boolean isCanCreate() {
		return canCreate;
	}
	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}
	public String getLabelStatus() {
		return labelStatus;
	}
	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}
	public ListModelList<WebusersModel> getLstUsers() {
		return lstUsers;
	}
	public void setLstUsers(ListModelList<WebusersModel> lstUsers) {
		this.lstUsers = lstUsers;
	}
	public WebusersModel getSelectedUser() {
		return selectedUser;
	}
	public void setSelectedUser(WebusersModel selectedUser) {
		this.selectedUser = selectedUser;
	}
	public CompanyData getCompanyData() {
		return companyData;
	}
	public void setCompanyData(CompanyData companyData) {
		this.companyData = companyData;
	}

	public DeliveryModel getDeliveryModel() {
		return deliveryModel;
	}

	public void setDeliveryModel(DeliveryModel deliveryModel) {
		this.deliveryModel = deliveryModel;
	}


	public DeliveryLineModel getSelectedGridItems() {
		return selectedGridItems;
	}


	public void setSelectedGridItems(DeliveryLineModel selectedGridItems) {
		this.selectedGridItems = selectedGridItems;
	}


	public List<DeliveryLineModel> getLstDeliveryCheckItems() {
		return lstDeliveryCheckItems;
	}


	public void setLstDeliveryCheckItems(
			List<DeliveryLineModel> lstDeliveryCheckItems) {
		this.lstDeliveryCheckItems = lstDeliveryCheckItems;
	}


	public String getDeliveryAddress() {
		return deliveryAddress;
	}


	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}


	public String getSelectedRadioButtons() {
		return selectedRadioButtons;
	}


	@NotifyChange({ "disableRemindMe", "disableRemindMeOn", "remindMeNoOfDays","remindMeDate" })
	public void setSelectedRadioButtons(String selectedRadioButtons) {
		this.selectedRadioButtons = selectedRadioButtons;
		if (selectedRadioButtons != null) {
			if (selectedRadioButtons.equalsIgnoreCase("A")) {
				disableRemindMe = true;
				remindMeNoOfDays = 0;
				disableRemindMeOn = false;
			} else if (selectedRadioButtons.equalsIgnoreCase("B")) {
				disableRemindMeOn = true;
				disableRemindMe = false;
				Calendar c = Calendar.getInstance();
				try {
					remindMeDate = df.parse(sdf.format(c.getTime()));
				} catch (ParseException e) {

					logger.error("ERROR in EditDelivery ----> setSelectedRadioButtons",e);
				}
			} else {
				disableRemindMe = true;
				disableRemindMeOn = true;
				remindMeNoOfDays = 0;
				Calendar c = Calendar.getInstance();
				try {
					remindMeDate = df.parse(sdf.format(c.getTime()));
				} catch (ParseException e) {

					logger.error("ERROR in EditDelivery ----> setSelectedRadioButtons",e);
				}
			}
		}

	}

	@GlobalCommand 
	@NotifyChange({"deliveryModel","lstDeliveryCheckItems","status", "matchFlag", "statusFalg","showSelectMulti","lstInvcCustomerGridInvrtySite","lstInvcCustomerGridClass"})
	public void getLstQuotationDelivery(@BindingParam("quotation") ApprovedQuotationModel quotation)
	{
		try
		{
			quotationNo=quotation.getRecNo();
			showSelectMulti=true;
			String memo="";
			lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
			memo=memo+"Quotation Ref No. "+quotation.getRefNumber()+" Date "+quotation.getTxnDate()+"\n";
			List<CashInvoiceGridData> invoiceModelnew = data.getQuotationridDataByID(quotation.getRecNo());
			for(CashInvoiceGridData editDeliveryGrid: invoiceModelnew)
			{
				if(!editDeliveryGrid.getDeliverdAs().equalsIgnoreCase("D")){
					DeliveryLineModel obj=new DeliveryLineModel();
					obj.setLineNo(lstDeliveryCheckItems.size()+1);				
					for(QbListsModel gridItem:lstItems)					
					{
						if(gridItem.getRecNo()==editDeliveryGrid.getItemRefKey())
						{
							obj.setSelectedItems(gridItem);
							break;
						}

					}
					for (QbListsModel gridSite : lstInvcCustomerGridInvrtySite) {
						if (gridSite.getRecNo() == editDeliveryGrid.getInventorySiteKey()) {
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if (gridSite.getRecNo() > 0)
								obj.setHideSite(true);
							break;
						}

					}
					for(QbListsModel gridClass:lstInvcCustomerGridClass)
					{
						if(gridClass.getRecNo()==editDeliveryGrid.getSelectedClass())
						{
							obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
							break;
						}
					}
					obj.setQuotationNo(quotation.getRecNo());
					obj.setQuotationLineNo(editDeliveryGrid.getLineNo());
					obj.setDescription(editDeliveryGrid.getInvoiceDescription());
					obj.setQuantity(editDeliveryGrid.getInvoiceQty());
					obj.setServiceDate(editDeliveryGrid.getServiceDate());
					obj.setAvgCost(editDeliveryGrid.getAvgCost());
					obj.setOverrideItemAccountRefKey(0);
					obj.setIsTaxable("Y");
					obj.setOther1("0");
					obj.setOther2("0");
					obj.setSalesTaxCodeRefKey(0);
					lstDeliveryCheckItems.add(obj);
				}
			}
			deliveryModel.setMemo(memo);
			deliveryModel.setTransformQ("Y");
		}
		catch(Exception ex)
		{
			logger.error("ERROR in EditDelivery  ----> getLstQuotationDelivery", ex);
		}
	}



	public int getRemindMeNoOfDays() {
		return remindMeNoOfDays;
	}


	public void setRemindMeNoOfDays(int remindMeNoOfDays) {
		this.remindMeNoOfDays = remindMeNoOfDays;
	}


	public Date getRemindMeDate() {
		return remindMeDate;
	}


	public void setRemindMeDate(Date remindMeDate) {
		this.remindMeDate = remindMeDate;
	}


	public boolean isDisableRemindMeOn() {
		return disableRemindMeOn;
	}


	public void setDisableRemindMeOn(boolean disableRemindMeOn) {
		this.disableRemindMeOn = disableRemindMeOn;
	}


	public boolean isDisableRemindMe() {
		return disableRemindMe;
	}


	public void setDisableRemindMe(boolean disableRemindMe) {
		this.disableRemindMe = disableRemindMe;
	}


	public boolean isShowQuotation() {
		return showQuotation;
	}


	public void setShowQuotation(boolean showQuotation) {
		this.showQuotation = showQuotation;
	}





	public boolean isShowSelectMulti() {
		return showSelectMulti;
	}


	public void setShowSelectMulti(boolean showSelectMulti) {
		this.showSelectMulti = showSelectMulti;
	}


	public List<DeliveryLineModel> getLstSelected() {
		return lstSelected;
	}


	public void setLstSelected(List<DeliveryLineModel> lstSelected) {
		this.lstSelected = lstSelected;
	}


	public boolean isTransformQ() {
		return transformQ;
	}


	public void setTransformQ(boolean transformQ) {
		this.transformQ = transformQ;
	}


	public int getQuotationNo() {
		return quotationNo;
	}


	public void setQuotationNo(int quotationNo) {
		this.quotationNo = quotationNo;
	}

	@SuppressWarnings("unused")
	@Command
	public void createPdfForPrinting()
	{
		if(validateData(false))
		{

			Document document = new Document(PageSize.A4, 40, 40, 108, 40);
			try {
				Execution exec = Executions.getCurrent();
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/temp/invoicePDFWebApplication.pdf"));
				writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
				HeaderFooter event = new HeaderFooter();
				writer.setPageEvent(event);
				// various fonts
				BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
				BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
				BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252", false);
				BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);
				//add this font for arabic text
				BaseFont bfarabic = BaseFont.createFont("c://temp//trado.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				Font arfont = new Font(bfarabic, 12);

				int y_line1 = 650;
				int y_line2 = y_line1 - 50;
				int y_line3 = y_line2 - 50;

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter.getInstance(document, baos);

				document.open();
				document.newPage();

				Paragraph paragraph = new Paragraph();

				PdfPTable firsttbl = new PdfPTable(2);
				firsttbl.setWidthPercentage(100);
				firsttbl.getDefaultCell().setBorder(0);
				firsttbl.setWidths(new int[]{200,100});
				Font f = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD, BaseColor.RED);
				Chunk c = new Chunk("Delivery");
				c.setUnderline(0.1f, -2f);
				c.setFont(f);
				Paragraph p = new Paragraph(c);



				firsttbl.addCell(p);


				PdfPCell cell1 = new PdfPCell(new Phrase("Date : "+sdf.format(creationdate)+"\n\n"+"Delivery No. : "+deliveryNewSaleNo));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setColspan(3);
				cell1.disableBorderSide(Rectangle.BOX);


				firsttbl.addCell(cell1);

				document.add(firsttbl);

				/*	if(invoiceNewBillToAddress==null && invoiceNewBillToAddress.equalsIgnoreCase(""))
				{
					invoiceNewBillToAddress=selectedInvcCutomerName.getFullName();
				}
				 */





				PdfPTable tbl1 = new PdfPTable(1);
				tbl1.setWidthPercentage(100);

				cell1 = new PdfPCell(new Phrase("To ,"));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("M/S : "+selectedInvcCutomerName.getFullName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				//add arfont here to use for arabic and RUN_DIRECTION_RTL and change the align from left to right 				
				cell1 = new PdfPCell(new Phrase(deliveryAddress ,arfont));
				cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);

				//cell1 = new PdfPCell(new Phrase(invoiceNewBillToAddress));
				//cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				tbl1.addCell(cell1);


				/*---------------------------------------------------------------*/ 



				document.add(tbl1);

				paragraph = new Paragraph();
				paragraph.setSpacingAfter(10);
				document.add(paragraph);

				PdfPTable table = new PdfPTable(2);
				objPrint.setQuantityWidth(0);
				if(!objPrint.isHideQuantity())
					objPrint.setQuantityWidth(40);
				table.setWidths(new int[]{390,objPrint.getQuantityWidth()});				
				table.setSpacingBefore(20);
				table.setWidthPercentage(100);
				table.getDefaultCell().setPadding(2);
				BaseColor myColor = WebColors.getRGBColor("#8ECDFA");

				/*PdfPCell HeadderProduct = new PdfPCell(new Phrase("No."));
				HeadderProduct.setPadding(1);
				HeadderProduct.setColspan(1);
				HeadderProduct.setBorder(Rectangle.NO_BORDER);
				HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderProduct.setBackgroundColor(myColor);
				table.addCell(HeadderProduct);*/

				Paragraph Headder = new Paragraph("Description");

				PdfPCell HeadderDate = new PdfPCell(Headder);
				HeadderDate.setPadding(1);
				HeadderDate.setColspan(1);
				HeadderDate.setBorder(Rectangle.NO_BORDER);
				HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
				//HeadderDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
				HeadderDate.setBackgroundColor(myColor);

				//   HeadderDate.setBorderWidth(120.0f);
				table.addCell(HeadderDate);
				//  table.setHeaderRows(1);
				PdfPCell HeadderQty = new PdfPCell(new Phrase("QTY"));
				HeadderQty.setPadding(1);
				HeadderQty.setColspan(1);
				HeadderQty.setBorder(Rectangle.NO_BORDER);
				HeadderQty.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderQty.setBackgroundColor(myColor);
				HeadderQty.setBorderWidth(20.0f);
				table.addCell(HeadderQty);

				/*PdfPCell HeadderRate = new PdfPCell(new Phrase("Rate"));
				HeadderRate.setPadding(1);
				HeadderRate.setColspan(1);
				HeadderRate.setBorder(Rectangle.NO_BORDER);
				HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderRate.setBackgroundColor(myColor);
				HeadderRate.setBorderWidth(40.0f);
				table.addCell(HeadderRate);

				PdfPCell HeadderAmount = new PdfPCell(new Phrase("Amount"));
				//  HeadderAmount.setPadding(1);
				//  HeadderAmount.setColspan(1);
				HeadderAmount.setBorder(Rectangle.NO_BORDER);
				//HeadderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
				// HeadderAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
				HeadderAmount.setBackgroundColor(myColor);
				//  HeadderAmount.setBorderWidth(40.0f);
				table.addCell(HeadderAmount);
				boolean desc=true;*/

				for (DeliveryLineModel item : lstDeliveryCheckItems) 
				{

					//table.addCell(new Phrase(""+item.getSelectedItems().getRecNo(),FontFactory.getFont(FontFactory.HELVETICA, 11)));
					PdfPCell cell=new PdfPCell(new Phrase(item.getDescription(),arfont));
					cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(cell);

					if(item.getQuantity()!=0)
					{
						cell=new PdfPCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						//table.addCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
						table.addCell(cell);						
					}else{
						table.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}
					/*if(item.getInvoiceRate()!=0.0){
						table.addCell(new Phrase(""+item.getInvoiceRate(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}else{
						table.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}

					String amtStr1=BigDecimal.valueOf(item.getInvoiceAmmount()).toPlainString();
					double amtDbbl1=Double.parseDouble(amtStr1);
					if(item.getInvoiceAmmount()!=0){
						table.addCell(new Phrase(""+formatter.format(BigDecimal.valueOf(amtDbbl1)), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}
					else{
						table.addCell(new Phrase("", FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}*/

				}

				for(PdfPRow r: table.getRows()) {
					for(PdfPCell c1: r.getCells()) {
						c1.setBorder(Rectangle.NO_BORDER);
						//c1.setHorizontalAlignment(Element.ALIGN_LEFT);
					}
				}
				document.add(table);
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );
				paragraph.setSpacingAfter(10);
				document.add(paragraph);
				DottedLineSeparator sep = new DottedLineSeparator(); 
				sep.setLineColor(new BaseColor(44, 67, 144));
				//document.add(new Chunk(ls1));
				/*PdfPTable totaltbl = new PdfPTable(2);
				totaltbl.setWidthPercentage(100);
				totaltbl.getDefaultCell().setBorder(0);
				totaltbl.setWidths(new int[]{350,100});
				cell1 = new PdfPCell(new Phrase("Amount in word: "+ numbToWord.GetFigToWord(totalAmount), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setBackgroundColor(myColor);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				totaltbl.addCell(cell1);

				String amtStr1 = BigDecimal.valueOf(totalAmount).toPlainString();
				double amtDbbl1 = Double.parseDouble(amtStr1);
				cell1 = new PdfPCell(new Phrase("Total :"+ formatter.format(amtDbbl1), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				cell1.setBackgroundColor(myColor);
				totaltbl.addCell(cell1);
				document.add(totaltbl);*/

				//paragraph=new Paragraph();
				//String amtStr1=BigDecimal.valueOf(toatlAmount).toPlainString();
				//double amtDbbl1=Double.parseDouble(amtStr1);
				// Chunk chunk = new Chunk("Total :"+formatter.format(amtDbbl1));


				// paragraph.add(chunk);
				//paragraph.setAlignment(Element.ALIGN_RIGHT);
				// document.add(paragraph);

				// paragraph=new Paragraph();
				//   chunk = new Chunk("Amount in word: "+numbToWord.GetFigToWord(toatlAmount));
				//  paragraph.add(chunk);
				//  paragraph.setAlignment(Element.ALIGN_LEFT);
				//  document.add(paragraph);


				/*document.add( Chunk.NEWLINE );
	 	   		paragraph=new Paragraph();
		   		chunk = new Chunk("Total Due :"+formatter.format(amtDbbl1));
		   		paragraph.add(chunk);
		   		paragraph.setAlignment(Element.ALIGN_LEFT);
		   		document.add(paragraph);*/


				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );
				paragraph=new Paragraph();
				Chunk chunk = new Chunk(msgToBeDispalyedOnDelivery);
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);
				document.add(new Chunk("\n\n"));
				PdfPTable endPage = new PdfPTable(2);
				endPage.setWidthPercentage(100);
				endPage.getDefaultCell().setBorder(0);
				endPage.setWidths(new int[]{330,120});
				cell1 = new PdfPCell(new Phrase("____________________\n\n "+compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);
				/*amtStr1 = BigDecimal.valueOf(totalAmount).toPlainString();
				amtDbbl1 = Double.parseDouble(amtStr1);*/
				cell1 = new PdfPCell(new Phrase("___________________\n\n  Customer Approval \n  Date:    /    /   "+printYear, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);
				document.add(endPage);
				document.close();
				/*// Create a reader
       PdfReader reader = new PdfReader(baos.toByteArray());
       // Create a stamper
       PdfStamper stamper
           = new PdfStamper(reader, new FileOutputStream("C:/temp/invoicePDFWebApplication.pdf"));
       // Loop over the pages and add a header to each page
       int n = reader.getNumberOfPages();
       for (int i = 1; i <= n; i++) {
           getHeaderTable(i, n,compSetup,data).writeSelectedRows(0, -1, 34, 803,stamper.getOverContent(i));
       }
       // Close the stamper
       stamper.close();
       reader.close();*/
				if(!createPdfSendEmail){
					previewPdfForprintingInvoice();
				}


			} catch (Exception ex) {
				logger.error("ERROR in  EditDelivery -- createPdfForPrinting --> onLoad", ex);
			}
		}
	}

	/** Inner class to add a header and a footer. */
	class HeaderFooter extends PdfPageEventHelper {

		@SuppressWarnings("hiding")
		public void onEndPage (PdfWriter writer, Document document) {
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=null;
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Rectangle rect = writer.getBoxSize("art");
			Image logo=null;
			try {
				String path=data.getImageData(dbUser.getCompanyName());
				logo = Image.getInstance(path);
				logo.scaleAbsolute(250, 100);
				Chunk chunk = new Chunk(logo, 0, -45);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(chunk),rect.getRight(), rect.getTop(), 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(compSetup.getCompanyName(),FontFactory.getFont(FontFactory.HELVETICA_BOLD,18)),rect.getLeft(), rect.getTop(), 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("Phone: "+compSetup.getPhone1()+"   Fax: "+compSetup.getFax()),rect.getLeft(), rect.getTop()-15, 0);
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(compSetup.getCcemail()),rect.getRight(180), rect.getTop()-15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(String.format(compSetup.getAddress())),(rect.getLeft()), rect.getTop() - 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(getCityName(compSetup.getCitykey())+" - "+getCountryName(compSetup.getCountrykey())),rect.getLeft(), rect.getTop()-45, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("______________________________________________________________________________"),rect.getLeft(), rect.getTop()-50, 0);
				Calendar now = Calendar.getInstance();
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(String.format("This Document Does Not Require Signature")),rect.getLeft(), rect.getBottom()-15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(String.format("Date :"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(now.getTime()))),(rect.getRight()), rect.getBottom() - 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(String.format("Printed by :"+selectedUser.getFirstname())),(rect.getRight()), rect.getBottom() - 15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(String.format("Powered by www.hinawi.com")),rect.getLeft(), rect.getBottom()-30, 0);


			} catch (BadElementException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf ----> onEndPage", e);
			} catch (MalformedURLException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf----> onEndPage", e);
			} catch (IOException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf----> onEndPage", e);
			} catch (DocumentException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf----> onEndPage", e);
			}
		}
	}	


	@Command
	public void previewPdfForprintingInvoice() {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			// arg.put("pdfContent", file);
			Executions.createComponents("/hba/payments/invoicePdfView.zul",
					null, arg);
		} catch (Exception ex) {
			logger.error("ERROR in EditDelivery ----> previewPdfForprintingInvoice",ex);
		}
	}


	public String getCountryName(int countryKey) {
		String country="";
		for (HRListValuesModel listValuesModel : countries) {
			if (countryKey != 0	&& countryKey == listValuesModel.getListId()) {
				country = listValuesModel.getEnDescription();
				break;
			}
		}
		return country;
	}

	public String getCityName(int CityKey) {
		String City="";
		for (HRListValuesModel model : cities) {
			if (CityKey != 0	&& CityKey == model.getListId()) {
				City = model.getEnDescription();
				break;
			}
		}
		return City;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail"})
	public void CustomerSendEmail(@BindingParam("row") final DeliveryModel row) {
		if(validateData(false))
		{
			lstAtt=new ArrayList<QuotationAttachmentModel>(); 
			selectedAttchemnets.setFilename(selectedInvcCutomerName.getFullName()+" Delivery.pdf");
			selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Delivery Note?","Delivery", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {						
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onYes")) 
					{
						createPdfSendEmail=false;
						createPdfForPrinting();
					}
					if (evt.getName().equals("onNo")){
						try {
							Map<String, Object> arg = new HashMap<String, Object>();
							createPdfSendEmail=true;
							createPdfForPrinting();
							arg.put("id", row.getCustomerRefKey());
							arg.put("lstAtt", lstAtt);
							arg.put("feedBackKey", 0);
							arg.put("formType", "Customer");
							arg.put("type", "OtherForms");
							Executions.createComponents("/crm/editCustomerFeedbackSend.zul",null, arg);
						} catch (Exception ex) {
							logger.error("ERROR in EditDelivery ----> CustomerSendEmail",ex);
						}
					}
				}
			});
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void clearDelivery() {
		if (true) {
			Messagebox.show("Your Data will be lost, Are you sure ?","Confirm Save", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt)
						throws InterruptedException {
					if (evt.getName().equals("onYes")) {
						Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
						Center center = bl.getCenter();
						Tabbox tabbox = (Tabbox) center.getFellow("mainContentTabbox");
						tabbox.getSelectedPanel().getLastChild().invalidate();
					} else {
						return;
					}
				}
			});
		}
	}


	public boolean isPosItems() {
		return posItems;
	}


	public void setPosItems(boolean posItems) {
		this.posItems = posItems;
	}


	public List<QbListsModel> getLstItemsForGrid() {
		return lstItemsForGrid;
	}


	public void setLstItemsForGrid(List<QbListsModel> lstItemsForGrid) {
		this.lstItemsForGrid = lstItemsForGrid;
	}


	public boolean isStatusFalg() {
		return statusFalg;
	}


	public void setStatusFalg(boolean statusFalg) {
		this.statusFalg = statusFalg;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public boolean isMatchFlag() {
		return matchFlag;
	}


	public void setMatchFlag(boolean matchFlag) {
		this.matchFlag = matchFlag;
	}






}
