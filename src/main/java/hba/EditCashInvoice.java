/**
 * 
 */
package hba;

import common.FormatDateText;
import common.HbaEnum;
import home.QuotationAttachmentModel;
import hr.HRData;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuData;
import layout.MenuModel;
import model.*;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import common.NumberToWord;
import company.CompanyData;

/**
 * @author IQBALMUFASIL
 *
 */
public class EditCashInvoice {

	private Logger logger = Logger.getLogger(EditCashInvoice.class);
	private Date creationdate; 
	private CashInvoiceModel objCashInvoice;
	HBAData data=new HBAData();
	DecimalFormat formatter = new DecimalFormat("#,###.00");
	int customerKeyOtherForms = 0;
	boolean otherformFalg = false;
	MenuData menuData=new MenuData();

	//private static String FILE = ":/temp/hello.pdf";


	//for the grid
	private CashInvoiceGridData selectedGridItems;
	private List<CashInvoiceGridData> lstCashInvoiceCheckItems;
	private List <QbListsModel> lstCashInvoiceGridItem;
	private List <QbListsModel> lstInvcCustomerGridInvrtySite;
	private List <QbListsModel> lstInvcCustomerGridClass;

	//to the form
	private List<QbListsModel> lstInvcCustomerName;
	private QbListsModel selectedInvcCutomerName;

	private List<QbListsModel> lstInvcCustomerClass;
	private QbListsModel selectedInvcCutomerClass;

	private List<QbListsModel> lstInvcCustomerDepositTo;
	private QbListsModel selectedInvcCutomerDepositeTo;

	private List<QbListsModel> lstInvcCustomerSalsRep;
	private QbListsModel selectedInvcCutomerSalsRep;

	private List<QbListsModel> lstInvcCustomerPaymntMethd=new ArrayList<QbListsModel>();
	private QbListsModel selectedInvcCutomerPaymntMethd;

	private List<QbListsModel> lstInvcCustomerTemplate;
	private QbListsModel selectedInvcCutomerTemplate;

	private String invoiceNewSaleNo;
	private String invoiceNewBillToAddress;

	private double tempTotalAmount;

	private List<QbListsModel> lstInvcCustomerSendVia;
	private QbListsModel selectedInvcCutomerSendVia;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");

	private String lblTotalCost;

	private double toatlAmount;

	private CompSetupModel compSetup;

	private int webUserID=0;
	private String webUserName="";

	//for point of sale 
	private boolean showFieldsPOS=false;

	private boolean pointofSale;

	private double balance;

	private double amountPiad;
	private double exChnage;

	private List<String> barcodeValues;

	//private String selectedBarCode;

	private String msgToBeDispalyedOnInvoice="";

	private boolean adminUser;

	private int  cashInvoiceKey;

	private MenuModel companyRole;

	List<MenuModel> list;

	String actionTYpe;

	private boolean canView=false;

	private boolean canModify=false;

	private boolean canPrint=false;

	private boolean canCreate=false;

	private String labelStatus="";

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	CompanyData companyData=new CompanyData();


	private String focusItem = "false";
	private String focusDesc = "false";
	private String focusQty = "false";
	private String focusRate = "false";
	private String focusClass = "false";
	private String focusAmount = "false";
	private String focusNewLine = "false";
	private boolean skipFocus = false;


	private BarcodeSettingsModel barcodeSettings;
	private String focusColumnAfterScan="Item"; 
	ItemsData dataBarc = new ItemsData();

	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	NumberToWord numbToWord=new NumberToWord();
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();

	private boolean seeTrasction=false;
	//add this for load items only one time at init
	
	private String viewType;
	private List<QbListsModel> lstItems;
	private DataFilter filter=new DataFilter();
	
	private boolean showDelivery=false;
	private boolean selectD=false;
	private boolean transformD=false;
	private String printYear="";
	private PrintModel objPrint;
	private boolean changeToCashInvoice=false;
	boolean isSkip = false;

	private boolean posItems;

	private List<VATCodeModel> lstVatCodeList;
	VATCodeModel custVendVatCodeModel;

	/*@Init	
	public void init(@BindingParam("type") String type)
	{
		try
		{
			viewType=type ==null ? "" : type;
			if(viewType.equals("items"))
			{
				lstItems=data.fillQbItemsLists("");
			}
		}
		catch(Exception ex)
		{
			logger.error("ERROR in EditCashInvoice ----> init", ex);
		}
	}*/
	
	@SuppressWarnings("rawtypes")
	public EditCashInvoice() 
	{
		try{
			
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			objPrint=new PrintModel();
			if(map.get("objPrint")!=null)
			{
				objPrint=(PrintModel) map.get("objPrint");
			}
			if(map.get("changeToCashInvoice")!=null)
				changeToCashInvoice=(Boolean) map.get("changeToCashInvoice");
			
			if(map.get("posItems") !=null)
				posItems=(Boolean) map.get("posItems");
			
			String type=(String)map.get("type");
			viewType=type ==null ? "" : type;
			/*if(viewType.equals("items"))
			{
				lstItems=data.fillQbItemsList();				
				selectedGridItems=(CashInvoiceGridData) map.get("selectedRow");
				compSetup=(CompSetupModel) map.get("compSetup");
				lstInvcCustomerGridClass=(List<QbListsModel>) map.get("lstInvcCustomerGridClass");
				logger.info("at load >>>> "+selectedGridItems.getLineNo());
				return;
			}*/
			Calendar c = Calendar.getInstance();
			printYear=new SimpleDateFormat("yyyy").format(c.getTime());
			lstItems=data.fillQbItemsList();
			barcodeSettings = dataBarc.fillBarcodeSettings();
			if (barcodeSettings == null) {
				focusColumnAfterScan = "";
			} else {
				focusColumnAfterScan = barcodeSettings.getBarcodeAfterScanGoTo();
			}
			cashInvoiceKey=(Integer)map.get("cashInvoiceKey");
			
			actionTYpe=type;
			canView=(Boolean) map.get("canView");
			canModify=(Boolean) map.get("canModify");
			canPrint=(Boolean) map.get("canPrint");
			canCreate=(Boolean) map.get("canCreate");
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction
			//list=menuData.getMenuList(dbUser.getUserid(),false);
			//getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");


				if(adminUser)
				{
					webUserID=0;
					webUserName="Admin";
				} else {
					webUserID = dbUser.getUserid();
					webUserName=dbUser.getUsername();
				}
			}

			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
			for(WebusersModel model:lstUsers)
			{
				if(model.getUserid()==dbUser.getUserid())
				{
					selectedUser=model;
					break;
				}
			}
			Window win = (Window)Path.getComponent("/cashInvoicePopUp");
			if(type.equalsIgnoreCase("create"))
			{
				win.setTitle("Add Cash Invoice Info");
			}
			else if(type.equalsIgnoreCase("edit"))
			{
				win.setTitle("Edit Cash Invoice Info");
			}else if (type.equalsIgnoreCase("OtherForms")) {
				win.setTitle("Add Cash Invoice Info");
				customerKeyOtherForms = (Integer) map.get("customerKey");
				otherformFalg = true;

			}
			else
			{
				win.setTitle("View Cash Invoice Info");
				canModify=false;
				canCreate=false;
			}
			showFieldsPOS=false;
			fillBarCode();
			objCashInvoice=new CashInvoiceModel();
			lstInvcCustomerName=data.fillQbList("'Customer'");
			//stop this to check performance
			//lstCashInvoiceGridItem=data.fillQbItemsList();
			lstCashInvoiceGridItem=new ArrayList<QbListsModel>(); 
			
			lstInvcCustomerClass=data.GetMasterData("Class");
			lstInvcCustomerDepositTo=data.GetMasterData("DepositeTo");
			lstInvcCustomerSalsRep=data.GetMasterData("SalesRep");
			compSetup=data.getDefaultSetUpInfoForCashInvoice();
			if(compSetup.getUseVAT().equals("Y")){
				lstVatCodeList=data.fillVatCodeList();
			}

			List<QbListsModel> lstInvcCustomerPaymntMethdTemp=new ArrayList<QbListsModel>();

			lstInvcCustomerPaymntMethdTemp=data.GetMasterData("Payment");
			for(QbListsModel listsModel:lstInvcCustomerPaymntMethdTemp)
			{
				QbListsModel model=new QbListsModel();
				if(!listsModel.getName().equalsIgnoreCase("check"))
				{
					model=listsModel;
					lstInvcCustomerPaymntMethd.add(model);
				}

			}
			lstInvcCustomerTemplate=data.GetMasterData("Template");
			lstInvcCustomerSendVia= data.GetMasterData("SendVia");
			lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
			lstInvcCustomerGridClass=data.GetMasterData("GridClass");

			if(cashInvoiceKey>0)
			{
				isSkip=true;
				labelStatus="Edit";
				CashInvoiceModel obj1 = new CashInvoiceModel();
				objCashInvoice=data.getCashInvoiceByID(cashInvoiceKey,webUserID,seeTrasction);
				List<CashInvoiceGridData> invoiceModelnew=data.getCashInvoiceGridDataByID(cashInvoiceKey);
				if(objCashInvoice!=null)
				{
					if(objCashInvoice.getTransformD().equalsIgnoreCase("Y")){
						transformD=true;
					}else{
						transformD=false;
					}
					for(QbListsModel cutomerNmae:lstInvcCustomerName)
					{
						if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
						{
							selectedInvcCutomerName=cutomerNmae;
							 obj1=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());
							objCashInvoice.setInvoiceProfileNumber(obj1.getTotalBalance());
							break;
						}

					}

					for(QbListsModel className:lstInvcCustomerClass)
					{
						if(className.getRecNo()==objCashInvoice.getClassRefKey())
						{
							selectedInvcCutomerClass=className;
							break;
						}

					}

					for(QbListsModel depositTo:lstInvcCustomerDepositTo)
					{
						if(depositTo.getRecNo()==objCashInvoice.getDepositAccountRefKey())
						{
							selectedInvcCutomerDepositeTo=depositTo;
							break;
						}

					}

					for(QbListsModel salesRep:lstInvcCustomerSalsRep)
					{
						if(salesRep.getRecNo()==objCashInvoice.getSalesRefKey())
						{
							selectedInvcCutomerSalsRep=salesRep;
							break;
						}

					}

					for(QbListsModel paymentMethod:lstInvcCustomerPaymntMethd)
					{
						if(paymentMethod.getRecNo()==objCashInvoice.getPaymentMethodRefKey())
						{
							selectedInvcCutomerPaymntMethd=paymentMethod;
							break;
						}

					}

					for(QbListsModel template:lstInvcCustomerTemplate)
					{
						if(template.getRecNo()==objCashInvoice.getTemplateRefKey())
						{
							selectedInvcCutomerTemplate=template;
							break;
						}

					}

					for(QbListsModel sendVia:lstInvcCustomerSendVia)
					{
						if(sendVia.getRecNo()==objCashInvoice.getSendViaReffKey())
						{
							selectedInvcCutomerSendVia=sendVia;
							break;
						}

					}

					//setSelectedInvcCutomerName(selectedInvcCutomerName);

					objCashInvoice.setInvoiceCheckNo(objCashInvoice.getCheckNo());
					objCashInvoice.setInvoiceMemo(objCashInvoice.getMemo());
					toatlAmount=objCashInvoice.getAmount();
					tempTotalAmount=objCashInvoice.getAmount();
					invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
					creationdate=df.parse(sdf.format(objCashInvoice.getTxnDate()));
					invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"";
					lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
					for(CashInvoiceGridData editInvoiceGrid:invoiceModelnew)
					{
						CashInvoiceGridData obj=new CashInvoiceGridData();
						obj.setLineNo(lstCashInvoiceCheckItems.size()+1);

						//change here to read from lstItems instead of lstCashInvoiceGridItem					
						for(QbListsModel gridItem:lstItems)
						{
							if(gridItem.getRecNo()==editInvoiceGrid.getItemRefKey())
							{
								obj.setSelectedItems(gridItem);
								break;
							}

						}

						for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
						{
							if(gridSite.getRecNo()==editInvoiceGrid.getInventorySiteKey())
							{
								obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
								if(gridSite.getRecNo()>0)
									obj.setHideSite(false);
								break;
							}

						}

						for(QbListsModel gridClass:lstInvcCustomerGridClass)
						{
							if(gridClass.getRecNo()==editInvoiceGrid.getSelectedClass())
							{
								obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
								break;
							}

						}
						for(String barcodeList:barcodeValues)
						{
							if(barcodeList.equalsIgnoreCase(editInvoiceGrid.getBarcode()))
							{
								obj.setBarcode(barcodeList);
								break;
							}

						}

						obj.setInvoiceQty(editInvoiceGrid.getInvoiceQty());
						obj.setServiceDate(editInvoiceGrid.getServiceDate());
						obj.setInvoiceRate(editInvoiceGrid.getInvoiceRate());
						obj.setInvoiceQtyOnHand(editInvoiceGrid.getInvoiceQtyOnHand());
						obj.setInvoiceAmmount(editInvoiceGrid.getInvoiceAmmount());
						obj.setVatAmount(editInvoiceGrid.getVatAmount());
						obj.setAmountAfterVAT(obj.getInvoiceAmmount() + obj.getVatAmount());
						obj.setVatKey(editInvoiceGrid.getVatKey());
						obj.setUnitPriceWithVAT(editInvoiceGrid.getUnitPriceWithVAT());
						if(compSetup.getUseVAT().equals("Y")){
							if(obj.getVatKey()>0) {
								VATCodeModel vatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == obj.getVatKey()).findFirst().orElse(null);
								if (vatCodeModel != null) {
									obj.setSelectedVatCode(vatCodeModel);
								} else {
									obj.setSelectedVatCode(lstVatCodeList.get(0));
								}

								if (obj.getVatKey() == obj1.getVatKey())
									obj.setNotAllowEditVAT(true);
							}
						}

						obj.setBarcode(editInvoiceGrid.getBarcode());
						obj.setInvoiceDescription(editInvoiceGrid.getInvoiceDescription());
						obj.setAvgCost(editInvoiceGrid.getAvgCost());
						obj.setInvoicearabicDescription(editInvoiceGrid.getInvoicearabicDescription());
						obj.setOverrideItemAccountRefKey(0);
						obj.setIsTaxable("Y");
						obj.setOther1("0");
						obj.setOther2("0");
						obj.setSalesTaxCodeRefKey(0);
						lstCashInvoiceCheckItems.add(obj);
					}

				}

			}
			else
			{
				labelStatus="Create";
				objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.SalesReceipt.toString()));
				objCashInvoice.setRecNo(0);
				objCashInvoice.setTransformD("N");
				if(customerKeyOtherForms>0){

					for (QbListsModel cutomerNmae : lstInvcCustomerName) {
						if (cutomerNmae.getRecNo() == customerKeyOtherForms) {
							selectedInvcCutomerName = cutomerNmae;
							setSelectedInvcCutomerName(cutomerNmae);
							break;
						}
					}
				}
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				//Calendar c = Calendar.getInstance();		
				creationdate=df.parse(sdf.format(c.getTime()));
				lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
				CashInvoiceGridData objItems=new CashInvoiceGridData();
				objItems.setLineNo(1);
				objItems.setInvoiceQty(1);
				lstCashInvoiceCheckItems.add(objItems);
				lblTotalCost="Amount :0.00";
				toatlAmount=0.0;
				tempTotalAmount=0.0;
				
				if(changeToCashInvoice)
				{
					//get the record 
					int RecNo=(Integer) map.get("RecNo");
					int customerKey=(Integer) map.get("customerKey");
					
					List<DeliveryModel> lstdeliveryModel=data.getDeliveryForInvoice(customerKey, webUserID, "S",RecNo);
													
					getlstSelectedDelivery(lstdeliveryModel);
					
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
			if(actionTYpe.equalsIgnoreCase("View"))
			{
				labelStatus="View";
			}
		}
		catch(Exception ex)
		{
			logger.error("ERROR in EditCashInvoice ----> onLoad", ex);
		}
	}

	public void fillBarCode()
	{

		barcodeValues=data.getLstBarcodes(null);
	}

	@Command
	public void openItemsCommands(@BindingParam("type") CashInvoiceGridData type)
	{
		//open items popup
		try
		{
		selectedGridItems=type;
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("selectedRow",type);
		arg.put("lstItems", lstItems);
		//arg.put("type","items");
		//arg.put("compSetup", compSetup);
		//arg.put("lstInvcCustomerGridClass", lstInvcCustomerGridClass);
		Executions.createComponents("/hba/payments/itemspopup.zul", null,arg);		
		}
		catch(Exception ex)
		{
			logger.error("ERROR in openItemsCommands ----> onLoad", ex);
		}
	}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Command
		public void selectdItemCommand(@ContextParam(ContextType.VIEW) Window comp,@BindingParam("row") QbListsModel row)
		{
			Map args = new HashMap();
			args.put("selectedItem", row);			
			args.put("selectedRow",selectedGridItems);
			args.put("refreshCommand", "selectdItemCommand");
			BindUtils.postGlobalCommand(null, null, "refreshItemsParent", args);	
			comp.detach();	
		}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand 
	//@NotifyChange({"lstCashInvoiceCheckItems","selectedGridItems"})
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void refreshItemsParent(@BindingParam("refreshCommand") String refreshCommand,@BindingParam("selectedItem")QbListsModel selectedItem,@BindingParam("selectedRow")CashInvoiceGridData selectedRow)
	{
		try
		{
		//logger.info("" + refreshCommand);			
		//logger.info(selectedItem.getFullName());
		//logger.info(selectedGridItems.getLineNo());
		
		selectedGridItems=selectedRow;		
		selectedGridItems.setSelectedItems(selectedItem);
		
		final CashInvoiceGridData type=selectedGridItems;
			
		if(selectedGridItems.getSelectedItems()!=null)
		{
			boolean hasSubAccount=data.checkIfItemHasSubQuery(selectedGridItems.getSelectedItems().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostItem2Main().equals("Y"))
				{
					Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							
							if (evt.getName().equals("onYes")) 
							{	 	
								selectInvoiceItemOnfuction(type);

							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedItems(null);
								type.setInvoiceDescription("");
								type.setSelectedInvcCutomerGridInvrtySiteNew(null);
								type.setSelectedInvcCutomerGridInvrtyClassNew(null);
								type.setAvgCost(0);
								type.setInvoiceRate(0);
								setLabelCheckTotalcost();
								BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
								BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "toatlAmount");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Item have sub Class(s). You cannot continue!","Item", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedItems(null);	
					type.setInvoiceDescription("");
					type.setSelectedInvcCutomerGridInvrtySiteNew(null);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);
					type.setAvgCost(0);
					type.setInvoiceRate(0);
					setLabelCheckTotalcost();
					BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
					BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "toatlAmount");
				}	
			}
			else{
				selectInvoiceItemOnfuction(type);
			}

		}

		if(amountPiad>=toatlAmount)
		{
			//balance=0.0;
			balance=amountPiad-toatlAmount;
		}
		else
		{
			//Messagebox.show("You cannot enter amount recived less than total cash invoice amount","Item", Messagebox.OK , Messagebox.INFORMATION);
			amountPiad=0.0;
			balance=toatlAmount;
		}
		
		//selectInvoiceGridItems(selectedGridItems);
		//Messagebox.show(selectedItem.getFullName());
		}
		catch(Exception ex)
		{
			logger.error("ERROR in EditCashInvoice ----> refreshItemsParent", ex);
		}
	}
	
	/*private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==140)
			{
				companyRole=item;
				break;
			}
		}
	}*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void selectInvoiceGridItems(@BindingParam("type") final  CashInvoiceGridData type)
	{
		//type=selectedGridItems;
		if(selectedGridItems.getSelectedItems()!=null)
		{

			boolean hasSubAccount=data.checkIfItemHasSubQuery(selectedGridItems.getSelectedItems().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostItem2Main().equals("Y"))
				{
					Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 	
								selectInvoiceItemOnfuction(type);

							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedItems(null);
								type.setInvoiceDescription("");
								type.setSelectedInvcCutomerGridInvrtySiteNew(null);
								type.setSelectedInvcCutomerGridInvrtyClassNew(null);
								type.setAvgCost(0);
								type.setInvoiceRate(0);
								setLabelCheckTotalcost();
								BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
								BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "toatlAmount");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Item have sub Class(s). You cannot continue!","Item", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedItems(null);	
					type.setInvoiceDescription("");
					type.setSelectedInvcCutomerGridInvrtySiteNew(null);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);
					type.setAvgCost(0);
					type.setInvoiceRate(0);
					setLabelCheckTotalcost();
					BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
					BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "toatlAmount");
				}	
			}
			else{
				selectInvoiceItemOnfuction(type);
			}

		}

		if(amountPiad>=toatlAmount)
		{
			//balance=0.0;
			balance=amountPiad-toatlAmount;
		}
		else
		{
			//Messagebox.show("You cannot enter amount recived less than total cash invoice amount","Item", Messagebox.OK , Messagebox.INFORMATION);
			amountPiad=0.0;
			balance=toatlAmount;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage","focusItem","focusDesc","focusQty","focusRate","focusClass","focusAmount","focusNewLine", "skipFocus"})
	public void selectInvoiceBCGridItems(@BindingParam("type") final  CashInvoiceGridData type)
	{
		CashInvoiceGridData cashInvoiceGridData = null;

		if(type.getBarcode()!=null && !type.getBarcode().trim().equals(""))
		{

			//Get Item Info and Fill Data 
			cashInvoiceGridData = data.getSelectedBCItem(type.getBarcode());

			if(cashInvoiceGridData!=null){

				fillCashInvInfo(type,cashInvoiceGridData);

				if(type.getSelectedItems()!=null)
				{
					boolean hasSubAccount=data.checkIfItemHasSubQuery(type.getSelectedItems().getName()+":");
					if(hasSubAccount)
					{
						if(compSetup.getPostItem2Main().equals("Y"))
						{
							Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener() {						
								public void onEvent(Event evt) throws InterruptedException {
									if (evt.getName().equals("onYes")) 
									{	 	
										selectInvoiceItemOnfuction(type);

									}
									else 
									{		 
										Map args = new HashMap();
										args.put("result", "1");
										BindUtils.postGlobalCommand(null, null, "resetGrid", args);
										type.setSelectedItems(null);
										type.setInvoiceDescription("");
										type.setSelectedInvcCutomerGridInvrtySiteNew(null);
										type.setSelectedInvcCutomerGridInvrtyClassNew(null);
										type.setAvgCost(0);
										type.setInvoiceRate(0);
										type.setInvoiceAmmount(0);
										setLabelCheckTotalcost();
										BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
										BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "toatlAmount");
									}
								}

							});
						}
						else
						{
							Messagebox.show("Selected Item have sub Class(s). You cannot continue!","Item", Messagebox.OK , Messagebox.INFORMATION);
							type.setSelectedItems(null);
							type.setInvoiceDescription("");
							type.setSelectedInvcCutomerGridInvrtySiteNew(null);
							type.setSelectedInvcCutomerGridInvrtyClassNew(null);
							type.setAvgCost(0);
							type.setInvoiceRate(0);
							type.setInvoiceAmmount(0);
							setLabelCheckTotalcost();
							BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
							BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "toatlAmount");
						}	
					}
					else{
						selectInvoiceItemOnfuction(type);
					}

				}

				if(amountPiad>=toatlAmount)
				{
					//balance=0.0;
					balance=amountPiad-toatlAmount;
				}
				else
				{
					//Messagebox.show("You cannot enter amount recived less than total cash invoice amount","Item", Messagebox.OK , Messagebox.INFORMATION);
					amountPiad=0.0;
					balance=toatlAmount;
				}
			}else{

				Messagebox.show("Invalid Barcode!","Item", Messagebox.OK , Messagebox.EXCLAMATION);
			}
		}else if(type.getSelectedItems()!=null){

			for(QbListsModel model:lstCashInvoiceGridItem)
			{
				if(model.getRecNo()==type.getRecNo()){

					type.setBarcode(model.getBarcode());
				}
			}
		}

		if(skipFocus==false){

			setFocusVariables(type);
			focusNewLine = "true";

		}

	}

	private CashInvoiceGridData fillCashInvInfo(CashInvoiceGridData type,CashInvoiceGridData cashInvoiceGridData){

		if(cashInvoiceGridData!=null){

			type.setRecNo(cashInvoiceGridData.getRecNo());
			//type.setItemType(cashInvoiceGridData.getItemType());

			for(QbListsModel model:lstCashInvoiceGridItem)
			{
				if(model.getRecNo()==cashInvoiceGridData.getRecNo())
					type.setSelectedItems(model);
			}

			type.setAvgCost(cashInvoiceGridData.getAvgCost());
			type.setInvoiceDescription(cashInvoiceGridData.getInvoiceDescription());
			type.setInvoicearabicDescription(cashInvoiceGridData.getInvoicearabicDescription());
			type.setInvoiceQtyOnHand(cashInvoiceGridData.getInvoiceQtyOnHand());
			type.setInvoiceRate(cashInvoiceGridData.getInvoiceRate());
			type.setInvoiceAmmount(cashInvoiceGridData.getInvoiceRate() * cashInvoiceGridData.getInvoiceQty());
			type.setServiceDate(cashInvoiceGridData.getServiceDate());//dummy
			type.setOverrideItemAccountRefKey(cashInvoiceGridData.getOverrideItemAccountRefKey());
			type.setIsTaxable(cashInvoiceGridData.getIsTaxable());
			type.setOther1(cashInvoiceGridData.getOther1());
			type.setOther2(cashInvoiceGridData.getOther2());
			type.setSalesTaxCodeRefKey(cashInvoiceGridData.getSalesTaxCodeRefKey());
			type.setBarcode(cashInvoiceGridData.getBarcode());
		}

		return cashInvoiceGridData;
	}


	public void selectInvoiceItemOnfuction(CashInvoiceGridData type)
	{
		type=selectedGridItems;
		logger.info("type>>> "+ type.getSelectedItems().getFullName());
		logger.info("type>>> "+ type.getSelectedItems().getListType());
		
		if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))
		{
			type.setHideSite(true);
			innerFuction(type);
			/*if(compSetup.getSellStockWithZero().equals("N"))
    	{
    		Messagebox.show("Quantity on hand is Zero or Minus, You can't continue!","Item", Messagebox.OK , Messagebox.INFORMATION);
    		Map args = new HashMap();
	        args.put("result", "1");
	        BindUtils.postGlobalCommand(null, null, "resetGrid", args);
        	type.setSelectedItems(null);
        	type.setBarcode(null);
        	type.setHideSite(false);
    	}
    	else
    	{

    		Messagebox.show("No Quantity Available, Do you want to continue?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				    	if (evt.getName().equals("onYes")) 
				        {
				    		innerFuction(type);
				        }
				    	else
				    	{
				    		Map args = new HashMap();
					        args.put("result", "2");
					        BindUtils.postGlobalCommand(null, null, "resetGrid", args);
				        	type.setSelectedItems(null);
				        	type.setHideSite(false);
				    	}
				    }
    		});


    	}*/
		}
		else
		{
			innerFuction(type);
			type.setHideSite(false);
		}

	}

	public void  innerFuction(CashInvoiceGridData type)
	{
		CashInvoiceGridData objItems=data.getInvoiceGridData(type.getSelectedItems().getRecNo());
		if(objItems!=null)
		{
			type.setRecNo(objItems.getRecNo());
			type.setItemType(objItems.getItemType());
			type.setAvgCost(objItems.getAvgCost());
			type.setInvoiceDescription(objItems.getInvoiceDescription());
			type.setInvoicearabicDescription(objItems.getInvoicearabicDescription());
			type.setInvoiceQtyOnHand(objItems.getInvoiceQtyOnHand());
			type.setInvoiceRate(objItems.getInvoiceRate());
			type.setInvoiceQty(1);
			type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
			//check VaTCode
			VATCodeOperation.selectCashInvoiceItemsVAT(type,custVendVatCodeModel,compSetup,lstVatCodeList);

			type.setServiceDate(creationdate);//dummy
			for(QbListsModel gridClass:lstInvcCustomerGridClass)
			{
				if(gridClass.getRecNo()==objItems.getSelectedClass())
				{
					type.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
					break;
				}

			}
			type.setOverrideItemAccountRefKey(0);
			type.setIsTaxable("Y");
			type.setOther1("0");
			type.setOther2("0");
			type.setSalesTaxCodeRefKey(0);
			type.setBarcode(objItems.getBarcode());
			setLabelCheckTotalcost();
		}

	}


	private void setLabelCheckTotalcost()
	{
		double toalCheckItemsAmount=0;
		for (CashInvoiceGridData item : lstCashInvoiceCheckItems) 
		{
			toalCheckItemsAmount+=item.getAmountAfterVAT();// getInvoiceAmmount();
		}
		lblTotalCost="Amount :" + BigDecimal.valueOf(toalCheckItemsAmount).toPlainString();
		toatlAmount=toalCheckItemsAmount;
		tempTotalAmount=toalCheckItemsAmount;
	}

	@Command   
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","tempTotalAmount"})
	public void deleteCheckItems(@BindingParam("row") CashInvoiceGridData row)
	{
		if(selectedGridItems!=null)
		{
			lstCashInvoiceCheckItems.remove(selectedGridItems);

			int srNo=0;
			for (CashInvoiceGridData item : lstCashInvoiceCheckItems)
			{
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if(lstCashInvoiceCheckItems.size()==0)
		{
			CashInvoiceGridData objItems=new CashInvoiceGridData();
			objItems.setLineNo(lstCashInvoiceCheckItems.size()+1);
			objItems.setInvoiceQty(1);
			lstCashInvoiceCheckItems.add(objItems);
		}
		setLabelCheckTotalcost();
	}

	@Command
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","tempTotalAmount"})
	public void insertCheckItems(@BindingParam("row") CashInvoiceGridData row)
	{
		if(selectedGridItems!=null)
		{
			CashInvoiceGridData lastItem=lstCashInvoiceCheckItems.get(lstCashInvoiceCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{					
				CashInvoiceGridData obj=new CashInvoiceGridData();
				obj.setLineNo(lstCashInvoiceCheckItems.size()+1);
				obj.setInvoiceQty(1);
				lstCashInvoiceCheckItems.add(obj);
			}
			else
			{
				Messagebox.show("To add new record,First select Item from the existing record!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return;
			}
		}
	}


	private void insertCheckItemsData(CashInvoiceGridData gridData){

		if(gridData!=null)
		{

			CashInvoiceGridData lastItem=lstCashInvoiceCheckItems.get(lstCashInvoiceCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{					
				CashInvoiceGridData obj=new CashInvoiceGridData();
				obj.setLineNo(lstCashInvoiceCheckItems.size()+1);
				obj.setInvoiceQty(1);
				lstCashInvoiceCheckItems.add(obj);
			}
			else
			{
				Messagebox.show("To add new record,First select Item from the existing record!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			}
		}
	}

	//need to work on these below stuff


	@Command  
	@NotifyChange({"toatlAmount"})
	public void addNewCashInvoice(@BindingParam("cmp") Window x)
	{
		if(validateData(true))
		{
			saveData();
			x.detach();

		}
	}


	@Command
	public void closeCashInvoice(@BindingParam("cmp") Window x)
	{
		x.detach();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"objCashInvoice","lstCashInvoiceCheckItems"})
	public void clearCashInvoice()
	{
		if(true)
		{

			Messagebox.show("Are you sure to Clear Cash Invoice ? Your Data will be lost.!", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onYes")) 
					{	
						lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>()		;
						objCashInvoice=new CashInvoiceModel();
					}
					else 
					{				        	
						return;
					}
				}

			});	

		}
	}

	@Command
	@NotifyChange({"toatlAmount"})
	public void addNewCashInvoiceClose(@BindingParam("cmp") Window x)
	{
		if(validateData(true))
		{
			saveData();
			x.detach();
		}
	}


	private boolean validateData(boolean Printflag)
	{
		boolean isValid=true;

		if(compSetup.getClosingDate()!=null){
			if(creationdate.compareTo(compSetup.getClosingDate())<=0){
				Messagebox.show("Invoice Date must be Higher than the QuickBooks Closing Date.!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}

		if(compSetup.getDontSaveWithOutMemo().equals("Y")){
			if(FormatDateText.isEmpty(objCashInvoice.getInvoiceMemo())){
				Messagebox.show("You must fill the transaction Memo according to company settings!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}

		if(compSetup.getUseSalesFlow().equalsIgnoreCase("Y") && objCashInvoice.getTransformD().equalsIgnoreCase("N") && isSkip==false)
		{
			Messagebox.show("Work Flow Is Activate, Please Select Delivery To Create Cash Invoice","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}


		if (selectedInvcCutomerName == null || selectedInvcCutomerName.getRecNo()==0)
		{		
			Messagebox.show("Select an existing Name For This Transaction!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedInvcCutomerPaymntMethd!=null && selectedInvcCutomerPaymntMethd.getName().equalsIgnoreCase("check"))
		{
			Messagebox.show("Please eneter the check number!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedInvcCutomerDepositeTo==null || selectedInvcCutomerDepositeTo.getRecNo()==0)
		{		
			Messagebox.show("Select an existing Deposite To for This Transaction!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(tempTotalAmount!=toatlAmount)
		{		
			Messagebox.show("The amount entered is not matching with actual grid total.!","Credit Invoice",Messagebox.OK,Messagebox.INFORMATION);
			toatlAmount = Double.parseDouble(BigDecimal.valueOf(tempTotalAmount).toPlainString());
			return false;
		}
		if(lstCashInvoiceCheckItems.size()<=0)
		{
			Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
		/*if(lstCashInvoiceCheckItems!=null)
		{	
			for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
			{
			CashInvoiceGridData lastItem=gridData;
			if(lastItem.getSelectedItems()!=null)
			{					
				if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null))
				{
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			}
			else{
				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			CashInvoiceGridData objExp=gridData;
			if(objExp.getSelectedItems()!=null)
			{
				if(objExp.getInvoiceRate()==0)
				{	 		
	 			Messagebox.show("Please Enter The Rate value ,Empty Transaction is not allowed !!!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
	 			return false;
				}

				if(objExp.getInvoiceQty()==0)
				{	 		
	 			Messagebox.show("Please Enter The Qantity,Empty Transaction is not allowed !!!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
	 			return false;
				}


			}
			}

		}*/

		boolean RateValidation=false;
		boolean siteValidation=false;
		if(lstCashInvoiceCheckItems!=null)
		{	
			CashInvoiceGridData lastItem=lstCashInvoiceCheckItems.get(lstCashInvoiceCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{			
				if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null || lastItem.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0))
				{
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			}
			else{
				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
			{
				CashInvoiceGridData objExp=gridData;
				if(objExp.getInvoiceRate()>0)
				{
					RateValidation=true;
					break;
				}
				else
				{
					RateValidation=false;
				}

			}
			for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
			{
				CashInvoiceGridData objExp=gridData;
				if((objExp.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || objExp.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (objExp.getSelectedInvcCutomerGridInvrtySiteNew()==null || objExp.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0))
				{
					siteValidation=true;
					break;

				}
				else
				{
					siteValidation=false;
				}
			}
			for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
			{
				CashInvoiceGridData objExp=gridData;
				if(objExp.getInvoiceQty()==0)
				{	 		
					Messagebox.show("Please Enter The Quantity,Empty Transaction is not allowed !!!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}

			}

			if(siteValidation)
			{
				Messagebox.show("To Save this record,First select Site Name from the existing records in the Grid!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			if(!RateValidation)
			{
				Messagebox.show("Please Enter The Rate value ,the amount cannot be zero,Empty Transaction is not allowed !!!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
			CashInvoiceGridData objExp=lstCashInvoiceCheckItems.get(0);
			if(objExp.getSelectedItems()==null)
			{
				Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

		}
		if(Printflag)
		{
			if((invoiceNewSaleNo!=null) && (data.checkIfSerialNumberIsDuplicate(invoiceNewSaleNo,objCashInvoice.getRecNo())==true))
			{
				Messagebox.show("Duplicate Sale Number!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
			if(invoiceNewSaleNo==null)
			{
				Messagebox.show("Please Enter the Sale Number","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}


		return isValid;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCashInvoiceCheckItems"})
	public void selectCashInvoiceGridClass(@BindingParam("type")  final CashInvoiceGridData type)
	{
		if(type.getSelectedInvcCutomerGridInvrtyClassNew()!=null)
		{
			//check if class has sub class		
			boolean hasSubAccount=type.getSelectedInvcCutomerGridInvrtyClassNew().getSubItemsCount()>0;//data.checkIfClassHasSub(type.getSelectedInvcCutomerGridInvrtyClassNew().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 				        		 				        	
							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedInvcCutomerGridInvrtyClassNew(null);
								BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);	
					BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
				}	
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"selectedInvcCutomerClass","lstInvcCustomerClass"})
	public void selectCashInvoiceClass()
	{
		if(selectedInvcCutomerClass!=null)
		{
			//check if class has sub class		
			boolean hasSubAccount=selectedInvcCutomerClass.getSubItemsCount()>0;//data.checkIfClassHasSub(selectedInvcCutomerClass.getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 				        		 				        	
							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								selectedInvcCutomerClass=null;
								BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "selectedInvcCutomerClass");
							}
						}

					});
				}
				else

				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					selectedInvcCutomerClass=null;
					BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "selectedInvcCutomerClass");
				}	
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","balance","amountPiad","toatlAmount","exChnage"})
	public void changeCashInvoiceCheckItems(@BindingParam("type") final CashInvoiceGridData type,@BindingParam("parm") String parm)
	{
		if(type!=null && type.getSelectedItems()!=null)
		{
			if(parm.equals("qty"))
			{
				if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))
				{
					if(type.getInvoiceQty()>type.getInvoiceQtyOnHand())
					{
						Messagebox.show("Quantity Available only is " +type.getInvoiceQtyOnHand() + "., Do you want to continue","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {						
							public void onEvent(Event evt) throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{
									type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
								}
								else 
								{		 
									Map args = new HashMap();
									args.put("result", "4");
									BindUtils.postGlobalCommand(null, null, "resetGrid", args);
									type.setInvoiceQty(0);
									BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
								}
							}

						});
					}
					else{
						type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
					}

				}
				else
				{
					type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
				}


			}



			if(parm.equals("rate"))
			{
				if(type.getInvoiceRate()>99999999999.99 )
				{
					Messagebox.show("The number is larger than the maximum value","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
				}

				logger.info(type.getSelectedItems().getListType());
				if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem"))
				{
					if(compSetup.getAllowSavingAvgCost().equals("N"))
					{
						if(Math.round((type.getAvgCost()*100)/100) > Math.round((type.getInvoiceRate())*100/100))
						{

							Messagebox.show("The rate is lesser than the Avg. Cost" +type.getInvoiceAmmount() + "., Do you want to continue","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener() 
							{						
								public void onEvent(Event evt) throws InterruptedException 
								{
									if (evt.getName().equals("onYes")) 
									{
										type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
									}
									else 
									{		 
										Map args = new HashMap();
										args.put("result", "1");
										BindUtils.postGlobalCommand(null, null, "resetGrid", args);
										type.setInvoiceRate(0);
										BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
									}
								}	

							});
						}
					}
					else{
						type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
					}
				}
				else{
					type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
				}

				if(compSetup.getUseSellItemWithLowerSP().equals("N"))
				{
					if((checkSellPrice(type.getInvoiceRate(),0.0,type)==false))
					{
						Map args = new HashMap();
						args.put("result", "1");
						BindUtils.postGlobalCommand(null, null, "resetGrid", args);
						type.setInvoiceRate(0);
						BindUtils.postNotifyChange(null, null, EditCashInvoice.this, "lstCashInvoiceCheckItems");
					}
				}
				else{
					type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
				}

			}
			if(parm.equals("amount"))
			{
				type.setInvoiceRate(type.getInvoiceAmmount()/type.getInvoiceQty());
			}
			VATCodeOperation.getCashInvoiceItemVatAmount(compSetup,type);
			setLabelCheckTotalcost();

			if(amountPiad>=toatlAmount)
			{
				//balance=0.0;
				balance=amountPiad-toatlAmount;
			}
			else
			{
				//Messagebox.show("You cannot enter amount recived less than total cash invoice amount","Item", Messagebox.OK , Messagebox.INFORMATION);
				amountPiad=0.0;
				balance=toatlAmount;
			}
		}
		else
		{
			Messagebox.show("You can't Continue!Please select the Item from the grid","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
		}
	}


	public boolean checkSellPrice(Double UserSellingPrice,Double _ActSellingPrice,final CashInvoiceGridData type)
	{

		boolean MINSPFlag  = false;
		boolean MAXSPFlag = false;
		Double MINSellingPrice;
		Double MAXSellingPrice; 
		Double ACTSellingPrice;

		CashInvoiceGridData objItems=data.getInvoiceGridData(type.getSelectedItems().getRecNo());

		ACTSellingPrice=objItems.getInvoiceRate();

		if(compSetup.getUseSellItemWithLowerSP().equals("N"))
		{
			if(compSetup.getUseMinSellingPrice().equals("Y"))
			{
				if(compSetup.getMinSellingPriceRatio()>0)
				{
					MINSellingPrice = ACTSellingPrice - ((ACTSellingPrice * compSetup.getMinSellingPriceRatio()) / 100);
					MINSPFlag = true;
					if(UserSellingPrice < MINSellingPrice )
					{
						Messagebox.show("The Price you entered is lower than the standard & discount allowed!","Cash Invoice", Messagebox.OK , Messagebox.INFORMATION);
						_ActSellingPrice = ACTSellingPrice;
						return false;
					}
				}

			}
			if(compSetup.getUseMaxSellingPrice().equals("Y"))
			{
				if(compSetup.getMaxSellingPriceRatio()>0)
				{
					MAXSellingPrice = ACTSellingPrice + ((ACTSellingPrice * compSetup.getMaxSellingPriceRatio()) / 100);
					MAXSPFlag = true;
					if(UserSellingPrice > MAXSellingPrice)
					{
						Messagebox.show("The Price entered is higher than the standard & discount allowed!","Cash Invoice", Messagebox.OK , Messagebox.INFORMATION);
						_ActSellingPrice = ACTSellingPrice;
						return false;
					}

				}
			}
			if(MINSPFlag == false && MAXSPFlag == false)
			{
				if(UserSellingPrice < ACTSellingPrice)
				{
					Messagebox.show("The Price entered is lower than the standard selling price!","Cash Invoice", Messagebox.OK , Messagebox.INFORMATION);
					_ActSellingPrice = ACTSellingPrice;
					return false;
				}
			}

		}
		return true;
	}

	@Command
	public void allowSkipCommand()
	{
		isSkip=true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveData() 
	{	      
		try
		{	
			int tmpRecNo=0;
			if(cashInvoiceKey==0)
			{
				tmpRecNo=data.GetNewCashInvoiceRecNo();	
			}
			else
			{
				tmpRecNo=objCashInvoice.getRecNo();
			}
			CashInvoiceModel obj=new CashInvoiceModel();
			obj.setRecNo(tmpRecNo);
			obj.setTxnId("0");
			obj.setCustomerRefKey(selectedInvcCutomerName.getRecNo());
			if(null!=selectedInvcCutomerClass)
			{
				obj.setClassRefKey(selectedInvcCutomerClass.getRecNo());
			}
			else
			{
				obj.setClassRefKey(0);
			}

			obj.setDepositAccountRefKey(selectedInvcCutomerDepositeTo.getRecNo());
			if(null!=selectedInvcCutomerSalsRep)
			{
				obj.setSalesRefKey(selectedInvcCutomerSalsRep.getRecNo());
			}
			else
			{
				obj.setSalesRefKey(0);
			}
			if(null!=selectedInvcCutomerTemplate)
			{
				obj.setTemplateRefKey(selectedInvcCutomerTemplate.getRecNo());
			}
			else
			{
				obj.setTemplateRefKey(0);
			}
			obj.setTxnDate(creationdate);
			obj.setRefNumber(invoiceNewSaleNo);
			obj.setShipAddressNote(invoiceNewBillToAddress);
			if(invoiceNewBillToAddress!=null)
			{
				String names[]=invoiceNewBillToAddress.split("\n");
				if(names!=null)
				{
					for(int i=0;i<names.length;i++)
					{
						if(i==0)
						{
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
						}
						else if(i==1)
						{
							obj.setBillAddress2(names[i]);
							obj.setShipAddress2(names[i]);
						}
						else if(i==2)
						{
							obj.setBillAddress3(names[i]);
							obj.setShipAddress3(names[i]);
						}
						else if(i==3)
						{
							obj.setBillAddress4(names[i]);
							obj.setShipAddress4(names[i]);
						}
						else if(i>=4)
						{
							obj.setBillAddress5(names[i]);
							obj.setShipAddress5(names[i]);
						}
					}

				}

			}
			else
			{
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
			obj.setBillAddressCity(objCashInvoice.getBillAddressCity());
			obj.setBillAddressState(objCashInvoice.getBillAddressState());
			obj.setBillAddressPostalCode(objCashInvoice.getBillAddressPostalCode());
			obj.setBillAddressCountry(objCashInvoice.getBillAddressCountry());
			obj.setBillAddressNote(objCashInvoice.getBillAddressNote());
			obj.setShipAddressCity(objCashInvoice.getShipAddressCity());
			obj.setShipAddressState(objCashInvoice.getShipAddressState());
			obj.setShipAddressPostalCode(objCashInvoice.getShipAddressPostalCode());
			obj.setShipAddressCountry(objCashInvoice.getShipAddressCountry());
			obj.setShipAddressNote(objCashInvoice.getShipAddressNote());
			obj.setIsPending("N");
			if(null!=objCashInvoice.getInvoiceCheckNo())
			{
				obj.setCheckNo(objCashInvoice.getInvoiceCheckNo());
			}
			else
			{
				obj.setCheckNo("");
			}
			if(null!=selectedInvcCutomerPaymntMethd)
			{
				obj.setPaymentMethodRefKey(selectedInvcCutomerPaymntMethd.getRecNo());
			}
			else
			{
				obj.setPaymentMethodRefKey(0);	
			}

			obj.setfOB("0");
			obj.setShipDate(creationdate);
			if(null!=selectedInvcCutomerPaymntMethd)
			{
				obj.setShipMethodRefKey(selectedInvcCutomerPaymntMethd.getRecNo());
				if(!selectedInvcCutomerPaymntMethd.getName().equalsIgnoreCase("cheque"))
				{
					obj.setCheckNo("");
				}
			}
			else
			{
				obj.setShipMethodRefKey(0);
			}

			obj.setItemSalesTaxRefKey(0);
			if(null!=objCashInvoice.getInvoiceMemo())
			{
				obj.setMemo(objCashInvoice.getInvoiceMemo());
			}
			else
			{
				obj.setMemo("");
			}
			obj.setTransformD(objCashInvoice.getTransformD());
			obj.setCustomerMsgRefKey(0);
			obj.setIsToBePrinted("Y");
			obj.setIsToEmailed("N");
			obj.setIsTaxIncluded("N");
			obj.setCustomerSalesTaxCodeRefKey(0);
			obj.setOther("");
			obj.setAmount(toatlAmount);
			if(compSetup.getUseVAT().equals("Y"))
			{
				double vatAmount = 0;
				for (CashInvoiceGridData item : lstCashInvoiceCheckItems) {
					vatAmount += item.getVatAmount();
				}
				obj.setVatAmount(vatAmount);
			}
			obj.setQuotationRecNo(0);
			if(null!=selectedInvcCutomerSendVia)
			{
				obj.setSendViaReffKey(selectedInvcCutomerSendVia.getRecNo());
			}
			else
			{
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
			obj.setRateHIDE("N");
			int result=0;
			if(cashInvoiceKey==0)
			{
				result=data.addNewCashInvoice(obj,webUserID);
			}else
			{
				result=data.updateCashInvoice(obj,webUserID);
			}
			if(result>0)
			{
				CutomerSummaryReport cutomerSummaryReport =data.getCutomerTotalBalance(selectedInvcCutomerName.getRecNo(), "Y", false);
				data.updateCustomerBalance(cutomerSummaryReport.getBalance(), selectedInvcCutomerName.getRecNo()); 
				if(cashInvoiceKey==0)//only on create
				{
					data.ConfigSerialNumberCashInvoice(SerialFields.SalesReceipt, invoiceNewSaleNo,0);
				}
				data.deleteCashInvoiceGridItems(tmpRecNo);
				data.deleteCashInvoiceDeliveryLine(tmpRecNo,"S");
				for (CashInvoiceGridData item : lstCashInvoiceCheckItems) 
				{
					if(item.getSelectedItems()!=null)
					{
						if(item.getDeliverRecNo()>0){
							data.addDeliveryInvoice(item, tmpRecNo,"S");
						}
						item.setServiceDate(creationdate);
						data.addCashInvoiceGridItems(item, tmpRecNo);
					}
				}
				//After Saving in to 4 Tables above Please save to QBVATTransaction
				if(compSetup.getUseVAT().equals("Y"))
				data.CreateCashInvoice4QBVAT(tmpRecNo, HbaEnum.VatForms.CashInvoice.getValue() , selectedInvcCutomerName.getRecNo() , selectedInvcCutomerName.getListType(),toatlAmount);

				if(cashInvoiceKey == 0)
				{
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.SalesReceipt.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getInvoiceSaleNo(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
				}else
				{
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.SalesReceipt.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getInvoiceSaleNo(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue());
				}
				if (selectedInvcCutomerName.getRecNo() > 0) {
					Calendar c = Calendar.getInstance();
					CustomerStatusHistoryModel model = new CustomerStatusHistoryModel();
					model.setRecNo(data.getMaxID("CustomerStatusHistory","RecNo"));
					model.setCustKey(selectedInvcCutomerName.getRecNo());
					model.setActionDate(df.parse(sdf.format(c.getTime())));
					model.setCreatedFrom("Cash Invoice");
					model.setStatusDescription(objCashInvoice.getDescription());
					model.setType("C");
					model.setTxnRecNo(0);
					model.setTxnRefNumber(invoiceNewSaleNo);
					data.saveCustomerStatusHistroyfromFeedback(model,webUserID,webUserName);
				}
				
			}

			if(cashInvoiceKey>0)
			{
				Clients.showNotification("The  Cash Invoice  Has Been Updated Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentCashInvoice", args);
			}
			else
			{
				Clients.showNotification("The Cash Invoice Has Been Created Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentCashInvoice", args);
			}
		}catch (Exception ex) 
		{
			Messagebox.show(ex.getMessage());
		}
	}




	/**
	 * @return the creationdate
	 */
	public Date getCreationdate() {
		return creationdate;
	}


	/**
	 * @param creationdate the creationdate to set
	 */
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}


	/**
	 * @return the objCashInvoice
	 */
	public CashInvoiceModel getObjCashInvoice() {
		return objCashInvoice;
	}


	/**
	 * @param objCashInvoice the objCashInvoice to set
	 */
	public void setObjCashInvoice(CashInvoiceModel objCashInvoice) {
		this.objCashInvoice = objCashInvoice;
	}

	/**
	/**
	 * @return the lstCashInvoiceGridItem
	 */
	public List<QbListsModel> getLstCashInvoiceGridItem() {
		return lstCashInvoiceGridItem;
	}


	/**
	 * @param lstCashInvoiceGridItem the lstCashInvoiceGridItem to set
	 */
	public void setLstCashInvoiceGridItem(List<QbListsModel> lstCashInvoiceGridItem) {
		this.lstCashInvoiceGridItem = lstCashInvoiceGridItem;
	}


	/**
	 * @return the selectedGridItems
	 */
	public CashInvoiceGridData getSelectedGridItems() {
		return selectedGridItems;
	}


	/**
	 * @param selectedGridItems the selectedGridItems to set
	 */
	public void setSelectedGridItems(CashInvoiceGridData selectedGridItems) {
		this.selectedGridItems = selectedGridItems;
	}



	/**
	 * @return the lstCashInvoiceCheckItems
	 */
	public List<CashInvoiceGridData> getLstCashInvoiceCheckItems() {
		return lstCashInvoiceCheckItems;
	}



	/**
	 * @param lstCashInvoiceCheckItems the lstCashInvoiceCheckItems to set
	 */
	public void setLstCashInvoiceCheckItems(
			List<CashInvoiceGridData> lstCashInvoiceCheckItems) {
		this.lstCashInvoiceCheckItems = lstCashInvoiceCheckItems;
	}







	/**
	 * @return the selectedInvcCutomerName
	 */
	public QbListsModel getSelectedInvcCutomerName() {
		return selectedInvcCutomerName;
	}


	/**
	 * @param selectedInvcCutomerName the selectedInvcCutomerName to set
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@NotifyChange({"objCashInvoice","invoiceNewBillToAddress","showDelivery","selectedInvcCutomerName","lstCashInvoiceCheckItems","lblTotalCost","toatlAmount"})
	public void setSelectedInvcCutomerName(QbListsModel selectedInvcCutomerName) {
		this.selectedInvcCutomerName = selectedInvcCutomerName;
		isSkip=false;
		invoiceNewBillToAddress="";
		custVendVatCodeModel=null;
		if(selectedInvcCutomerName!=null)
		{
			if (selectedInvcCutomerName.getRecNo() == 0)
				return;

			CashInvoiceModel obj=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());		
			StringBuffer address =new StringBuffer();	
			if(obj.getBillAddress5()!=null && !obj.getBillAddress5().equalsIgnoreCase(""))
				invoiceNewBillToAddress=obj.getBillAddress5();
			else
				invoiceNewBillToAddress=obj.getFullname();
			objCashInvoice.setBillAddress1(obj.getBillAddress1());
			objCashInvoice.setBillAddress2(obj.getBillAddress2());
			objCashInvoice.setBillAddress3(obj.getBillAddress3());
			objCashInvoice.setBillAddress4(obj.getBillAddress4());
			objCashInvoice.setBillAddress5("");
			objCashInvoice.setBillAddressCity(obj.getBillAddressCity());
			objCashInvoice.setBillAddressState(obj.getBillAddressState());
			objCashInvoice.setBillAddressCountry(obj.getBillAddressCountry());
			objCashInvoice.setBillAddressPostalCode(obj.getBillAddressPostalCode());
			objCashInvoice.setBillAddressNote("");
			objCashInvoice.setShipAddress1(obj.getBillAddress1());
			objCashInvoice.setShipAddress2(obj.getBillAddress2());
			objCashInvoice.setShipAddress3(obj.getBillAddress3());
			objCashInvoice.setShipAddress4(obj.getBillAddress4());
			objCashInvoice.setShipAddress5("");
			objCashInvoice.setShipAddressCity(obj.getBillAddressCity());
			objCashInvoice.setShipAddressState(obj.getBillAddressState());
			objCashInvoice.setShipAddressCountry(obj.getBillAddressCountry());
			objCashInvoice.setShipAddressPostalCode(obj.getBillAddressPostalCode());
			objCashInvoice.setShipAddressNote("");
			objCashInvoice.setInvoiceAddress("");
			objCashInvoice.setName(obj.getName());
			objCashInvoice.setInvoiceProfileNumber(obj.getTotalBalance());
			objCashInvoice.setAccountName(obj.getAccountName());
			objCashInvoice.setAccountNo(obj.getAccountNo());
			objCashInvoice.setBankName(obj.getBankName());
			objCashInvoice.setBranchName(obj.getBranchName());
			objCashInvoice.setiBANNo(obj.getiBANNo());
			objCashInvoice.setEmail(obj.getEmail());
			objCashInvoice.setTotalBalance(obj.getTotalBalance());
			objCashInvoice.setPhone(obj.getPhone());
			objCashInvoice.setFax(obj.getFax());
			objCashInvoice.setPrintChequeAs(obj.getPrintChequeAs());

			//VAT
			if (compSetup.getUseVAT().equals("Y")) {
				if (obj.getVatKey() > 0) {
					custVendVatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == obj.getVatKey()).findFirst().orElse(null);
					if (custVendVatCodeModel != null) {
						if (lstCashInvoiceCheckItems.size() > 0) {
							Messagebox.show("There is VAT Code assigned for this Customer" +
									". System will recalculate the VAT Amount based on the Customer.", "Cash Invoice", Messagebox.OK, Messagebox.INFORMATION);

							//for Items
							VATCodeOperation.recalculateCashInvoiceItemsVAT(lstCashInvoiceCheckItems, custVendVatCodeModel, compSetup);
							//setLabelCheckItems();
							//getNewTotalAmount();
						}
					}
				}
				else //not vat assigned to this customer Sony asked to skip this
				{
					for (CashInvoiceGridData item : lstCashInvoiceCheckItems) {
						if (item.getSelectedItems() != null && item.getSelectedVatCode()!=null) {
							item.setNotAllowEditVAT(false);
							//VATCodeOperation.selectCashInvoiceItemsVAT(item, custVendVatCodeModel, compSetup, lstVatCodeList);
						}
					}
					///getNewTotalAmount();
				}
			}
			setLabelCheckTotalcost();
			//stop now when add VAT
			if(!data.getCustomerDelivery(selectedInvcCutomerName.getRecNo(),"S"))
			{
				showDelivery=false;
				isSkip=true;
			}
			else
			{
				showDelivery=true;
				if(compSetup.getAllowToSkip().equals("Y"))
				{
					Messagebox.show("Customer has Delivery. Do you want to skip?","Delivery", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
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
			Messagebox.show("Invalid Customer Name !!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			setSelectedInvcCutomerName(lstInvcCustomerName.get(0));
		}
	}

	/**
	 * @return the lstInvcCustomerDepositTo
	 */




	/**
	 * @return the selectedInvcCutomerDepositeTo
	 */
	public QbListsModel getSelectedInvcCutomerDepositeTo() {
		return selectedInvcCutomerDepositeTo;
	}



	/**
	 * @param selectedInvcCutomerDepositeTo the selectedInvcCutomerDepositeTo to set
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange({ "selectedInvcCutomerDepositeTo" })
	public void setSelectedInvcCutomerDepositeTo(
			QbListsModel selectedInvcCutomerDepositeTo) {
		this.selectedInvcCutomerDepositeTo = selectedInvcCutomerDepositeTo;
		if (selectedInvcCutomerDepositeTo!=null) {
			if (selectedInvcCutomerDepositeTo.getRecNo() == 0)
				return;

			boolean hasSubAccount = data.checkIfBankAccountsHasSub(selectedInvcCutomerDepositeTo.getFullName() + ":");
			if (hasSubAccount) {
				if (compSetup.getPostOnMainClass().equals("Y")) {
					Messagebox.show("Selected Deposit Account have Sub Account(s). Do you want to continue?","Cash Invoice", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) {
							} else {
								setSelectedInvcCutomerDepositeTo(lstInvcCustomerDepositTo.get(0));
								BindUtils.postNotifyChange(null,null,EditCashInvoice.this,"selectedInvcCutomerDepositeTo");
							}
						}

					});
				} else

				{
					Messagebox.show("Selected Deposit Account have sub Account(s). You cannot continue!","Cash Invoice", Messagebox.OK,Messagebox.INFORMATION);
					setSelectedInvcCutomerDepositeTo(lstInvcCustomerDepositTo.get(0));
				}
			}
		}

		else
		{
			Messagebox.show("Invalid Deposit Account Name !!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			setSelectedInvcCutomerDepositeTo(lstInvcCustomerDepositTo.get(0));
		}

	}






	/**
	 * @return the selectedInvcCutomerSalsRep
	 */
	public QbListsModel getSelectedInvcCutomerSalsRep() {
		return selectedInvcCutomerSalsRep;
	}



	/**
	 * @param selectedInvcCutomerSalsRep the selectedInvcCutomerSalsRep to set
	 */
	public void setSelectedInvcCutomerSalsRep(
			QbListsModel selectedInvcCutomerSalsRep) {
		this.selectedInvcCutomerSalsRep = selectedInvcCutomerSalsRep;
	}







	public List<QbListsModel> getLstInvcCustomerPaymntMethd() {
		return lstInvcCustomerPaymntMethd;
	}

	public void setLstInvcCustomerPaymntMethd(
			List<QbListsModel> lstInvcCustomerPaymntMethd) {
		this.lstInvcCustomerPaymntMethd = lstInvcCustomerPaymntMethd;
	}

	/**
	 * @return the selectedInvcCutomerPaymntMethd
	 */
	public QbListsModel getSelectedInvcCutomerPaymntMethd() {
		return selectedInvcCutomerPaymntMethd;
	}



	/**
	 * @param selectedInvcCutomerPaymntMethd the selectedInvcCutomerPaymntMethd to set
	 */
	public void setSelectedInvcCutomerPaymntMethd(
			QbListsModel selectedInvcCutomerPaymntMethd) {
		this.selectedInvcCutomerPaymntMethd = selectedInvcCutomerPaymntMethd;
	}





	/**
	 * @return the selectedInvcCutomerTemplate
	 */
	public QbListsModel getSelectedInvcCutomerTemplate() {
		return selectedInvcCutomerTemplate;
	}



	/**
	 * @param selectedInvcCutomerTemplate the selectedInvcCutomerTemplate to set
	 */
	public void setSelectedInvcCutomerTemplate(
			QbListsModel selectedInvcCutomerTemplate) {
		this.selectedInvcCutomerTemplate = selectedInvcCutomerTemplate;
	}






	/**
	 * @return the lstInvcCustomerGridInvrtySite
	 */
	public List<QbListsModel> getLstInvcCustomerGridInvrtySite() {
		return lstInvcCustomerGridInvrtySite;
	}

	/**
	 * @param lstInvcCustomerGridInvrtySite the lstInvcCustomerGridInvrtySite to set
	 */
	public void setLstInvcCustomerGridInvrtySite(
			List<QbListsModel> lstInvcCustomerGridInvrtySite) {
		this.lstInvcCustomerGridInvrtySite = lstInvcCustomerGridInvrtySite;
	}

	/**
	 * @return the lstInvcCustomerGridClass
	 */
	public List<QbListsModel> getLstInvcCustomerGridClass() {
		return lstInvcCustomerGridClass;
	}

	/**
	 * @param lstInvcCustomerGridClass the lstInvcCustomerGridClass to set
	 */
	public void setLstInvcCustomerGridClass(
			List<QbListsModel> lstInvcCustomerGridClass) {
		this.lstInvcCustomerGridClass = lstInvcCustomerGridClass;
	}




	/**
	 * @return the selectedInvcCutomerClass
	 */
	public QbListsModel getSelectedInvcCutomerClass() {
		return selectedInvcCutomerClass;
	}



	/**
	 * @param selectedInvcCutomerClass the selectedInvcCutomerClass to set
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange({ "selectedInvcCutomerClass" })
	public void setSelectedInvcCutomerClass(QbListsModel selectedInvcCutomerClass) {
		this.selectedInvcCutomerClass = selectedInvcCutomerClass;
		if (selectedInvcCutomerClass!=null) {
			if (selectedInvcCutomerClass.getRecNo() == 0)
				return;

			boolean hasSubAccount = data.checkIfClassHasSub(selectedInvcCutomerClass.getName() + ":");
			if (hasSubAccount) {
				if (compSetup.getPostOnMainClass().equals("Y")) {
					Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) {
							} else {
								setSelectedInvcCutomerClass(lstInvcCustomerClass.get(0));
								BindUtils.postNotifyChange(null,null,EditCashInvoice.this,"selectedInvcCutomerClass");
							}
						}

					});
				} else

				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK,Messagebox.INFORMATION);
					setSelectedInvcCutomerClass(lstInvcCustomerClass.get(0));
					//BindUtils.postNotifyChange(null, null,EditCashInvoice.this,"selectedInvcCutomerClass");
				}
			}
		}

		else
		{
			Messagebox.show("Invalid Class Name !!","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
			setSelectedInvcCutomerClass(lstInvcCustomerClass.get(0));
		}
	}






	public List<QbListsModel> getLstInvcCustomerName() {
		return lstInvcCustomerName;
	}

	public void setLstInvcCustomerName(List<QbListsModel> lstInvcCustomerName) {
		this.lstInvcCustomerName = lstInvcCustomerName;
	}

	public List<QbListsModel> getLstInvcCustomerClass() {
		return lstInvcCustomerClass;
	}

	public void setLstInvcCustomerClass(List<QbListsModel> lstInvcCustomerClass) {
		this.lstInvcCustomerClass = lstInvcCustomerClass;
	}

	public List<QbListsModel> getLstInvcCustomerDepositTo() {
		return lstInvcCustomerDepositTo;
	}

	public void setLstInvcCustomerDepositTo(
			List<QbListsModel> lstInvcCustomerDepositTo) {
		this.lstInvcCustomerDepositTo = lstInvcCustomerDepositTo;
	}

	public List<QbListsModel> getLstInvcCustomerSalsRep() {
		return lstInvcCustomerSalsRep;
	}

	public void setLstInvcCustomerSalsRep(List<QbListsModel> lstInvcCustomerSalsRep) {
		this.lstInvcCustomerSalsRep = lstInvcCustomerSalsRep;
	}

	public List<QbListsModel> getLstInvcCustomerTemplate() {
		return lstInvcCustomerTemplate;
	}

	public void setLstInvcCustomerTemplate(
			List<QbListsModel> lstInvcCustomerTemplate) {
		this.lstInvcCustomerTemplate = lstInvcCustomerTemplate;
	}

	public List<QbListsModel> getLstInvcCustomerSendVia() {
		return lstInvcCustomerSendVia;
	}

	public void setLstInvcCustomerSendVia(List<QbListsModel> lstInvcCustomerSendVia) {
		this.lstInvcCustomerSendVia = lstInvcCustomerSendVia;
	}

	/**
	 * @return the selectedInvcCutomerSendVia
	 */
	public QbListsModel getSelectedInvcCutomerSendVia() {
		return selectedInvcCutomerSendVia;
	}



	/**
	 * @param selectedInvcCutomerSendVia the selectedInvcCutomerSendVia to set
	 */
	public void setSelectedInvcCutomerSendVia(
			QbListsModel selectedInvcCutomerSendVia) {
		this.selectedInvcCutomerSendVia = selectedInvcCutomerSendVia;
	}

	/**
	 * @return the lblTotalCost
	 */
	public String getLblTotalCost() {
		return lblTotalCost;
	}

	/**
	 * @param lblTotalCost the lblTotalCost to set
	 */
	public void setLblTotalCost(String lblTotalCost) {
		this.lblTotalCost = lblTotalCost;
	}

	/**
	 * @return the compSetup
	 */
	public CompSetupModel getCompSetup() {
		return compSetup;
	}

	/**
	 * @param compSetup the compSetup to set
	 */
	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = Double.parseDouble(BigDecimal.valueOf(balance).toPlainString());
	}

	public double getAmountPiad() {
		return amountPiad;
	}

	@Command
	@NotifyChange({"balance","toatlAmount","exChnage","amountPiad"})
	public void amountPiadChange() {
		if(amountPiad>=toatlAmount)
		{
			//balance=0.0;
			balance=amountPiad-toatlAmount;
		}
		else
		{
			amountPiad=0.0;
			balance=toatlAmount;
			Messagebox.show("You cannot enter amount received less than total cash invoice amount","Item", Messagebox.OK , Messagebox.INFORMATION);

		}
	}



	@NotifyChange({"balance","toatlAmount","exChnage","amountPiad"})
	public void setAmountPiad(double amountPiad) {
		this.amountPiad = amountPiad;
		/*if(amountPiad>=toatlAmount)
		{
		//balance=0.0;
		balance=amountPiad-toatlAmount;
		}
		else
		{
			amountPiad=0.0;
			//Messagebox.show("You cannot enter amount recived less than total cash invoice amount","Item", Messagebox.OK , Messagebox.INFORMATION);

		}*/
	}

	public List<String> getBarcodeValues() {
		return barcodeValues;
	}

	public void setBarcodeValues(List<String> barcodeValues) {
		this.barcodeValues = barcodeValues;
	}

	/*public String getSelectedBarCode() {
		return selectedBarCode;
	}

	public void setSelectedBarCode(String selectedBarCode) {
		this.selectedBarCode = selectedBarCode;
	}*/

	public boolean isPointofSale() {
		return pointofSale;
	}

	@NotifyChange({"showFieldsPOS","lstCashInvoiceCheckItems","lblTotalCost"})
	public void setPointofSale(boolean pointofSale) {
		this.pointofSale = pointofSale;
		if(pointofSale==true)
		{
			lblTotalCost="Amount :0.00";
			lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
			CashInvoiceGridData objItems=new CashInvoiceGridData();
			objItems.setLineNo(1);
			objItems.setInvoiceQty(1);
			lstCashInvoiceCheckItems.add(objItems);
			showFieldsPOS=true;
		}
		else
		{
			showFieldsPOS=false;
		}
	}

	public boolean isShowFieldsPOS() {
		return showFieldsPOS;
	}

	public void setShowFieldsPOS(boolean showFieldsPOS) {
		this.showFieldsPOS = showFieldsPOS;
	}

	public double getToatlAmount() {
		return toatlAmount;
	}

	public void setToatlAmount(double toatlAmount) {
		this.toatlAmount = Double.parseDouble(BigDecimal.valueOf(toatlAmount).toPlainString());
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
				PdfWriter writer = PdfWriter.getInstance(document,
						new FileOutputStream(
								"C:/temp/invoicePDFWebApplication.pdf"));
				writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

				HeaderFooter event = new HeaderFooter();
				writer.setPageEvent(event);

				// various fonts
				BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA,
						"Cp1252", false);
				BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN,
						"Cp1252", false);
				BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER,
						"Cp1252", false);
				BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL,
						"Cp1252", false);

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
				firsttbl.setWidths(new int[] { 200, 100 });
				Font f1 = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
						BaseColor.RED);
				Chunk c = new Chunk("Cash Invoice");
				c.setUnderline(0.1f, -2f);
				c.setFont(f1);
				Paragraph p = new Paragraph(c);

				firsttbl.addCell(p);

				PdfPCell cell1 = new PdfPCell(new Phrase("Date :"+sdf.format(creationdate)+"\n\n"+"Invoice Number :"+invoiceNewSaleNo));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setColspan(3);
				cell1.disableBorderSide(Rectangle.BOX);
				firsttbl.addCell(cell1);
				document.add(firsttbl);

				/*------------------------------------------------------------------------*/
				PdfPTable tbl1 = new PdfPTable(1);
				tbl1.setWidthPercentage(100);

				cell1 = new PdfPCell(new Phrase("Bill To ,"));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("M/S : "+selectedInvcCutomerName.getFullName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase(invoiceNewBillToAddress,arfont));
				cell1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.disableBorderSide(Rectangle.BOX);
				tbl1.addCell(cell1);
				document.add(tbl1);

				/*---------------------------------------------------------------*/ 





				paragraph = new Paragraph();
				paragraph.setSpacingAfter(10);
				document.add(paragraph);
				BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
				PdfPTable table = new PdfPTable(5);
				objPrint.setSrNoWidth(0);
				objPrint.setQuantityWidth(0);
				objPrint.setRateWidth(0);
				objPrint.setAmountWidth(0);
				objPrint.setWordAmountWidth(0);
								
				if(!objPrint.isHideSrNo())
				objPrint.setSrNoWidth(40);
				if(!objPrint.isHideQuantity())
					objPrint.setQuantityWidth(40);
				if(!objPrint.isHideRate())
					objPrint.setRateWidth(60);
				if(!objPrint.isHideAmount())
					objPrint.setAmountWidth(60);
				if(!objPrint.isHideWordAmount())
					objPrint.setWordAmountWidth(350);
				
				table.setWidths(new int[]{objPrint.getSrNoWidth(),210,objPrint.getQuantityWidth(),objPrint.getRateWidth(),objPrint.getAmountWidth()});
				//table.setWidths(new int[]{60,210,60,60,60});
				table.setSpacingBefore(20);
				table.setWidthPercentage(100);
				table.getDefaultCell().setPadding(5);

				PdfPCell HeadderProduct = new PdfPCell(new Phrase("No."));
				HeadderProduct.setPadding(1);
				HeadderProduct.setColspan(1);
				HeadderProduct.setBorder(Rectangle.NO_BORDER);
				HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderProduct.setBackgroundColor(myColor);
				table.addCell(HeadderProduct);

				PdfPCell HeadderDate = new PdfPCell(new Phrase("Description"));
				HeadderDate.setPadding(1);
				HeadderDate.setColspan(1);
				HeadderDate.setBorder(Rectangle.NO_BORDER);
				HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderDate.setBackgroundColor(myColor);
				table.addCell(HeadderDate);

				PdfPCell HeadderQty = new PdfPCell(new Phrase("QTY"));
				HeadderQty.setPadding(1);
				HeadderQty.setColspan(1);
				HeadderQty.setBorder(Rectangle.NO_BORDER);
				HeadderQty.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderQty.setBackgroundColor(myColor);
				HeadderQty.setBorderWidth(20.0f);
				table.addCell(HeadderQty);

				PdfPCell HeadderRate = new PdfPCell(new Phrase("Rate"));
				HeadderRate.setPadding(1);
				HeadderRate.setColspan(1);
				HeadderRate.setBorder(Rectangle.NO_BORDER);
				HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderRate.setBackgroundColor(myColor);
				HeadderRate.setBorderWidth(40.0f);
				table.addCell(HeadderRate);

				PdfPCell HeadderAmount = new PdfPCell(new Phrase("Amount"));
				HeadderAmount.setPadding(1);
				HeadderAmount.setColspan(1);
				HeadderAmount.setBorder(Rectangle.NO_BORDER);
				HeadderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderAmount.setBackgroundColor(myColor);
				HeadderAmount.setBorderWidth(40.0f);
				table.addCell(HeadderAmount);


				for (CashInvoiceGridData item : lstCashInvoiceCheckItems) 
				{

					table.addCell(new Phrase(""+item.getSelectedItems().getRecNo(),FontFactory.getFont(FontFactory.HELVETICA, 11)));

					if(item.getInvoiceDescription()!=null && !item.getInvoiceDescription().equalsIgnoreCase(""))
					{
						PdfPCell cell=new PdfPCell(new Phrase(item.getInvoiceDescription(),arfont));
						//cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
						//cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(cell);
					}else{
						table.addCell("");
					}
					if(item.getInvoiceQty()!=0){
						table.addCell(new Phrase(""+item.getInvoiceQty(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}else{
						table.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}
					if(item.getInvoiceRate()!=0.0){
						table.addCell(new Phrase(""+formatter.format(item.getInvoiceRate()), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}else{
						table.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}

					if(item.getInvoiceAmmount()>0){
						String amtStr1=BigDecimal.valueOf(item.getInvoiceAmmount()).toPlainString();
						double amtDbbl1=Double.parseDouble(amtStr1);
						HeadderAmount = new PdfPCell(new Phrase(""+formatter.format(BigDecimal.valueOf(amtDbbl1))));
						HeadderAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(HeadderAmount);
					}else{
						table.addCell("");
					}
				}

				for(PdfPRow r: table.getRows()) {
					for(PdfPCell c1: r.getCells()) {
						c1.setBorder(Rectangle.NO_BORDER);
					}
				}

				document.add(table);
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );
				paragraph.setSpacingAfter(10);
				document.add(paragraph);
				if(pointofSale)
				{
					paragraph=new Paragraph();
					String amtStr1=BigDecimal.valueOf(amountPiad).toPlainString();
					double amtDbbl1=Double.parseDouble(amtStr1);
					Chunk chunk = new Chunk("Amount Recived :"+formatter.format(amtDbbl1), FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_RIGHT);
					document.add(paragraph);
					document.add( Chunk.NEWLINE );



					PdfPTable totaltbl = new PdfPTable(2);
					totaltbl.setWidthPercentage(100);
					totaltbl.getDefaultCell().setBorder(0);
					totaltbl.setWidths(new int[]{350,100});
					cell1 = new PdfPCell(new Phrase("Amount in word: "+ numbToWord.GetFigToWord(toatlAmount), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
					cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell1.setBackgroundColor(myColor);
					cell1.disableBorderSide(Rectangle.BOX);
					cell1.setBorder(0);
					totaltbl.addCell(cell1);

					String amtStr2 = BigDecimal.valueOf(toatlAmount).toPlainString();
					double amtDbbl2 = Double.parseDouble(amtStr2);
					cell1 = new PdfPCell(new Phrase("Total :"+ formatter.format(amtDbbl2), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
					cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell1.disableBorderSide(Rectangle.BOX);
					cell1.setBackgroundColor(myColor);
					cell1.setBorder(0);
					totaltbl.addCell(cell1);
					if(!objPrint.isHideWordAmount())
					document.add(totaltbl);

					paragraph=new Paragraph();
					String amtStr3=BigDecimal.valueOf(balance).toPlainString();
					double amtDbbl3=Double.parseDouble(amtStr3);
					chunk = new Chunk("Balance/Ex-Change :"+formatter.format(amtDbbl3));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_RIGHT);
					document.add(paragraph);

					/*paragraph=new Paragraph();
		   chunk = new Chunk("Ex-Change :"+exChnage);
		   paragraph.add(chunk);
		   paragraph.setAlignment(Element.ALIGN_RIGHT);
		   document.add(paragraph);*/

				}
				else
				{
					PdfPTable totaltbl = new PdfPTable(2);
					totaltbl.setWidthPercentage(100);
					totaltbl.getDefaultCell().setBorder(0);
					totaltbl.setWidths(new int[]{350,100});
					cell1 = new PdfPCell(new Phrase("Amount in word: "
							+ numbToWord.GetFigToWord(toatlAmount), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
					cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell1.setBackgroundColor(myColor);
					cell1.disableBorderSide(Rectangle.BOX);
					cell1.setBorder(0);
					totaltbl.addCell(cell1);

					String amtStr1 = BigDecimal.valueOf(toatlAmount)
							.toPlainString();
					double amtDbbl1 = Double.parseDouble(amtStr1);
					cell1 = new PdfPCell(new Phrase("Total :"
							+ formatter.format(amtDbbl1), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
					cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell1.disableBorderSide(Rectangle.BOX);
					cell1.setBorder(0);
					cell1.setBackgroundColor(myColor);
					totaltbl.addCell(cell1);
					if(!objPrint.isHideWordAmount())
					document.add(totaltbl);

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
				}

				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );

				if(msgToBeDispalyedOnInvoice==null || msgToBeDispalyedOnInvoice.equalsIgnoreCase(""))
				{
					paragraph=new Paragraph();
					Chunk chunk = new Chunk("Product or Service once Sold/Provided can not be Refunded", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);

				}else{

					paragraph=new Paragraph();
					Chunk chunk = new Chunk(msgToBeDispalyedOnInvoice, FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);

				}

				document.add(new Chunk("\n\n"));

				PdfPTable endPage = new PdfPTable(2);
				endPage.setWidthPercentage(100);
				endPage.getDefaultCell().setBorder(0);
				endPage.setWidths(new int[]{330,120});
				if (!createPdfSendEmail){
					cell1 = new PdfPCell(new Phrase("____________________\n\n "+compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				}else{
					cell1 = new PdfPCell(new Phrase(""));
				}
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);

				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);

				String amtStr1 = BigDecimal.valueOf(toatlAmount)
						.toPlainString();
				double amtDbbl1 = Double.parseDouble(amtStr1);
				cell1 = new PdfPCell(new Phrase("___________________\n\n  Customer Approval \n  Date:    /    /   "+printYear, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);
				document.add(endPage);

				document.close();

				if (!createPdfSendEmail) {
					previewPdfForprintingInvoice();
				}

			} catch (Exception ex) {
				logger.error("ERROR in CashInvoiceViewModel----> createPdfForPrinting", ex);
			}
		}
	}


	/** Inner class to add a header and a footer. */
	class HeaderFooter extends PdfPageEventHelper {

		@SuppressWarnings("hiding")
		public void onEndPage(PdfWriter writer, Document document) {
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = null;
			dbUser = (WebusersModel) sess.getAttribute("Authentication");
			Rectangle rect = writer.getBoxSize("art");
			Image logo = null;
			try {
				String path = data.getImageData(dbUser.getCompanyName());
				logo = Image.getInstance(path);
				logo.scaleAbsolute(250, 100);
				Chunk chunk = new Chunk(logo, 0, -45);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(chunk),rect.getRight(), rect.getTop(), 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)), rect.getLeft(), rect.getTop(), 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("Phone: " + compSetup.getPhone1()+ "   Fax: " + compSetup.getFax()),rect.getLeft(), rect.getTop() - 15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format(compSetup.getAddress())),(rect.getLeft()), rect.getTop() - 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(getCityName(compSetup.getCitykey()) + " - "+ getCountryName(compSetup.getCountrykey())),rect.getLeft(), rect.getTop() - 45, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("______________________________________________________________________________"),rect.getLeft(), rect.getTop() - 50, 0);
				Calendar now = Calendar.getInstance();
				/*if (createPdfSendEmail){
					ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("This Document Does Not Require Signature")),rect.getLeft(), rect.getBottom() - 15, 0);
				}*/
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Date :"+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(now.getTime()))),(rect.getRight()), rect.getBottom() - 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Printed by :"+ selectedUser.getFirstname())),(rect.getRight()), rect.getBottom() - 15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("Powered by www.hinawi.com")),	rect.getLeft(), rect.getBottom() - 30, 0);

			} catch (BadElementException e) {
				logger.error(
						"ERROR in EditCashInvoice class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in EditCashInvoice class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in EditCashInvoice class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in EditCashInvoice class HeaderFooter PDf----> onEndPage",
						e);
			}
		}
	}

	//edit vendor list
	@Command
	public void previewPdfForprintingInvoice()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			//   arg.put("pdfContent", file);
			Executions.createComponents("/hba/payments/invoicePdfView.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCashInvoice ----> previewPdfForprintingInvoice", ex);			
		}
	}




	@Command
	@NotifyChange({"invoiceNewSaleNo","objCashInvoice","creationdate","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedInvcCutomerDepositeTo","selectedInvcCutomerSalsRep","selectedInvcCutomerPaymntMethd","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage","transformD"})
	public void navigationCashInvoice(@BindingParam("cmp") String navigation)
	{
		try
		{
			Window win = (Window)Path.getComponent("/cashInvoicePopUp");
			objCashInvoice=data.navigationCashInvoice(cashInvoiceKey,webUserID,seeTrasction,navigation,actionTYpe);
			if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
			{
				win.setTitle("Edit Cash Invoice Info");
				actionTYpe="edit";
				labelStatus="Edit";
				CashInvoiceModel obj1 = new CashInvoiceModel();
				cashInvoiceKey=objCashInvoice.getRecNo();
				if(objCashInvoice.getTransformD().equalsIgnoreCase("Y")){
					transformD=true;
				}else{
					transformD=false;
				}
				List<CashInvoiceGridData> invoiceModelnew=data.getCashInvoiceGridDataByID(cashInvoiceKey);
				for(QbListsModel cutomerNmae:lstInvcCustomerName)
				{
					if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
					{
						selectedInvcCutomerName=cutomerNmae;
						obj1=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());
						objCashInvoice.setInvoiceProfileNumber(obj1.getTotalBalance());
						break;
					}

				}

				for(QbListsModel className:lstInvcCustomerClass)
				{
					if(className.getRecNo()==objCashInvoice.getClassRefKey())
					{
						selectedInvcCutomerClass=className;
						break;
					}

				}

				for(QbListsModel depositTo:lstInvcCustomerDepositTo)
				{
					if(depositTo.getRecNo()==objCashInvoice.getDepositAccountRefKey())
					{
						selectedInvcCutomerDepositeTo=depositTo;
						break;
					}

				}

				for(QbListsModel salesRep:lstInvcCustomerSalsRep)
				{
					if(salesRep.getRecNo()==objCashInvoice.getSalesRefKey())
					{
						selectedInvcCutomerSalsRep=salesRep;
						break;
					}

				}

				for(QbListsModel paymentMethod:lstInvcCustomerPaymntMethd)
				{
					if(paymentMethod.getRecNo()==objCashInvoice.getPaymentMethodRefKey())
					{
						selectedInvcCutomerPaymntMethd=paymentMethod;
						break;
					}

				}

				for(QbListsModel template:lstInvcCustomerTemplate)
				{
					if(template.getRecNo()==objCashInvoice.getTemplateRefKey())
					{
						selectedInvcCutomerTemplate=template;
						break;
					}

				}

				for(QbListsModel sendVia:lstInvcCustomerSendVia)
				{
					if(sendVia.getRecNo()==objCashInvoice.getSendViaReffKey())
					{
						selectedInvcCutomerSendVia=sendVia;
						break;
					}

				}

				//setSelectedInvcCutomerName(selectedInvcCutomerName);

				objCashInvoice.setInvoiceCheckNo(objCashInvoice.getCheckNo());
				objCashInvoice.setInvoiceMemo(objCashInvoice.getMemo());
				toatlAmount=objCashInvoice.getAmount();
				tempTotalAmount=objCashInvoice.getAmount();
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				creationdate=df.parse(sdf.format(objCashInvoice.getTxnDate()));
				invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"";
				lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
				for(CashInvoiceGridData editInvoiceGrid:invoiceModelnew)
				{
					CashInvoiceGridData obj=new CashInvoiceGridData();
					obj.setLineNo(lstCashInvoiceCheckItems.size()+1);
					//change here to read from lstItems instead of lstCashInvoiceGridItem	
					
					for(QbListsModel gridItem:lstItems)					
					{
						if(gridItem.getRecNo()==editInvoiceGrid.getItemRefKey())
						{
							obj.setSelectedItems(gridItem);
							break;
						}

					}

					for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
					{
						if(gridSite.getRecNo()==editInvoiceGrid.getInventorySiteKey())
						{
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if(gridSite.getRecNo()>0)
								obj.setHideSite(true);
							break;
						}

					}

					for(QbListsModel gridClass:lstInvcCustomerGridClass)
					{
						if(gridClass.getRecNo()==editInvoiceGrid.getSelectedClass())
						{
							obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
							break;
						}

					}
					for(String barcodeList:barcodeValues)
					{
						if(barcodeList.equalsIgnoreCase(editInvoiceGrid.getBarcode()))
						{
							obj.setBarcode(barcodeList);
							break;
						}

					}
					obj.setDeliverRecNo(editInvoiceGrid.getDeliverRecNo());
					obj.setDeliveryLineNo(editInvoiceGrid.getDeliveryLineNo());
					obj.setInvoiceQty(editInvoiceGrid.getInvoiceQty());
					obj.setServiceDate(editInvoiceGrid.getServiceDate());
					obj.setInvoiceRate(editInvoiceGrid.getInvoiceRate());
					obj.setInvoiceQtyOnHand(editInvoiceGrid.getInvoiceQtyOnHand());
					obj.setInvoiceAmmount(editInvoiceGrid.getInvoiceAmmount());
					obj.setVatAmount(editInvoiceGrid.getVatAmount());
					obj.setAmountAfterVAT(obj.getInvoiceAmmount() + obj.getVatAmount());
					obj.setVatKey(editInvoiceGrid.getVatKey());
					obj.setUnitPriceWithVAT(editInvoiceGrid.getUnitPriceWithVAT());
					if(compSetup.getUseVAT().equals("Y")){
						if(obj.getVatKey()>0) {
							VATCodeModel vatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == obj.getVatKey()).findFirst().orElse(null);
							if (vatCodeModel != null) {
								obj.setSelectedVatCode(vatCodeModel);
							} else {
								obj.setSelectedVatCode(lstVatCodeList.get(0));
							}

							if (obj.getVatKey() == obj1.getVatKey())
								obj.setNotAllowEditVAT(true);
						}
					}

					obj.setBarcode(editInvoiceGrid.getBarcode());
					obj.setInvoiceDescription(editInvoiceGrid.getInvoiceDescription());
					obj.setAvgCost(editInvoiceGrid.getAvgCost());
					obj.setInvoicearabicDescription(editInvoiceGrid.getInvoicearabicDescription());
					obj.setOverrideItemAccountRefKey(0);
					obj.setIsTaxable("Y");
					obj.setOther1("0");
					obj.setOther2("0");
					obj.setSalesTaxCodeRefKey(0);
					lstCashInvoiceCheckItems.add(obj);
				}

			}
			else
			{
				win.setTitle("Create Cash Invoice Info");
				actionTYpe="create";
				labelStatus="Create";
				cashInvoiceKey=0;
				objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.SalesReceipt.toString()));
				objCashInvoice.setRecNo(0);
				objCashInvoice.setTransformD("N");
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				Calendar c = Calendar.getInstance();		
				compSetup=data.getDefaultSetUpInfoForCashInvoice();
				creationdate=df.parse(sdf.format(c.getTime()));
				lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
				CashInvoiceGridData objItems=new CashInvoiceGridData();
				objItems.setLineNo(1);
				objItems.setInvoiceQty(1);
				lstCashInvoiceCheckItems.add(objItems);
				lblTotalCost="Amount :0.00";
				toatlAmount=0.0;
				tempTotalAmount=0.0;
				selectedInvcCutomerName=null;
				selectedInvcCutomerClass=null;
				selectedInvcCutomerDepositeTo=null;
				selectedInvcCutomerSalsRep=null;
				selectedInvcCutomerTemplate=null;
				selectedInvcCutomerSendVia=null;
				selectedInvcCutomerPaymntMethd=null;
				invoiceNewBillToAddress="";
			}

			if(actionTYpe.equalsIgnoreCase("View"))
			{
				labelStatus="View";
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCashInvoice ----> navigationCashInvoice", ex);			
		}
	}


	@Command
	@NotifyChange({"invoiceNewSaleNo","objCashInvoice","creationdate","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedInvcCutomerDepositeTo","selectedInvcCutomerSalsRep","selectedInvcCutomerPaymntMethd","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void copyFunctinality()
	{
		try
		{	if(cashInvoiceKey>0)
		{
			objCashInvoice=data.getCashInvoiceByID(cashInvoiceKey,webUserID,seeTrasction);
			if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
			{
				actionTYpe="create";
				labelStatus="Copied-Create";
				List<CashInvoiceGridData> invoiceModelnew=data.getCashInvoiceGridDataByID(cashInvoiceKey);
				for(QbListsModel cutomerNmae:lstInvcCustomerName)
				{
					if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
					{
						selectedInvcCutomerName=cutomerNmae;
						CashInvoiceModel obj1=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());
						objCashInvoice.setInvoiceProfileNumber(obj1.getTotalBalance());
						break;
					}

				}

				for(QbListsModel className:lstInvcCustomerClass)
				{
					if(className.getRecNo()==objCashInvoice.getClassRefKey())
					{
						selectedInvcCutomerClass=className;
						break;
					}

				}

				for(QbListsModel depositTo:lstInvcCustomerDepositTo)
				{
					if(depositTo.getRecNo()==objCashInvoice.getDepositAccountRefKey())
					{
						selectedInvcCutomerDepositeTo=depositTo;
						break;
					}

				}

				for(QbListsModel salesRep:lstInvcCustomerSalsRep)
				{
					if(salesRep.getRecNo()==objCashInvoice.getSalesRefKey())
					{
						selectedInvcCutomerSalsRep=salesRep;
						break;
					}

				}

				for(QbListsModel paymentMethod:lstInvcCustomerPaymntMethd)
				{
					if(paymentMethod.getRecNo()==objCashInvoice.getPaymentMethodRefKey())
					{
						selectedInvcCutomerPaymntMethd=paymentMethod;
						break;
					}

				}

				for(QbListsModel template:lstInvcCustomerTemplate)
				{
					if(template.getRecNo()==objCashInvoice.getTemplateRefKey())
					{
						selectedInvcCutomerTemplate=template;
						break;
					}

				}

				for(QbListsModel sendVia:lstInvcCustomerSendVia)
				{
					if(sendVia.getRecNo()==objCashInvoice.getSendViaReffKey())
					{
						selectedInvcCutomerSendVia=sendVia;
						break;
					}

				}

				//setSelectedInvcCutomerName(selectedInvcCutomerName);
				cashInvoiceKey=0;
				objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.SalesReceipt.toString()));
				objCashInvoice.setRecNo(0);
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				Calendar c = Calendar.getInstance();		
				compSetup=data.getDefaultSetUpInfoForCashInvoice();
				creationdate=df.parse(sdf.format(c.getTime()));
				objCashInvoice.setInvoiceCheckNo(objCashInvoice.getCheckNo());
				objCashInvoice.setInvoiceMemo(objCashInvoice.getMemo());
				toatlAmount=objCashInvoice.getAmount();
				tempTotalAmount=objCashInvoice.getAmount();
				invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"";
				lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
				for(CashInvoiceGridData editInvoiceGrid:invoiceModelnew)
				{
					CashInvoiceGridData obj=new CashInvoiceGridData();
					obj.setLineNo(lstCashInvoiceCheckItems.size()+1);

					for(QbListsModel gridItem:lstCashInvoiceGridItem)
					{
						if(gridItem.getRecNo()==editInvoiceGrid.getItemRefKey())
						{
							obj.setSelectedItems(gridItem);
							break;
						}

					}

					for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
					{
						if(gridSite.getRecNo()==editInvoiceGrid.getInventorySiteKey())
						{
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if(gridSite.getRecNo()>0)
								obj.setHideSite(true);
							break;
						}

					}

					for(QbListsModel gridClass:lstInvcCustomerGridClass)
					{
						if(gridClass.getRecNo()==editInvoiceGrid.getSelectedClass())
						{
							obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
							break;
						}

					}
					for(String barcodeList:barcodeValues)
					{
						if(barcodeList.equalsIgnoreCase(editInvoiceGrid.getBarcode()))
						{
							obj.setBarcode(barcodeList);
							break;
						}

					}

					obj.setInvoiceQty(editInvoiceGrid.getInvoiceQty());
					obj.setServiceDate(editInvoiceGrid.getServiceDate());
					obj.setInvoiceRate(editInvoiceGrid.getInvoiceRate());
					obj.setInvoiceQtyOnHand(editInvoiceGrid.getInvoiceQtyOnHand());
					obj.setInvoiceAmmount(editInvoiceGrid.getInvoiceAmmount());
					obj.setBarcode(editInvoiceGrid.getBarcode());
					obj.setInvoiceDescription(editInvoiceGrid.getInvoiceDescription());
					obj.setAvgCost(editInvoiceGrid.getAvgCost());
					obj.setInvoicearabicDescription(editInvoiceGrid.getInvoicearabicDescription());
					obj.setOverrideItemAccountRefKey(0);
					obj.setIsTaxable("Y");
					obj.setOther1("0");
					obj.setOther2("0");
					obj.setSalesTaxCodeRefKey(0);
					lstCashInvoiceCheckItems.add(obj);
				}

			}
		}
		else
		{
			Messagebox.show("You can only copy a existing cash Invoice","Cash Invoice", Messagebox.OK , Messagebox.INFORMATION);
			return;
		}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCashInvoice ----> copyFunctinality", ex);			
		}
	}




	//edit vendor list
	@Command
	public void cashInvoiceSetting()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			//arg.put("pdfContent", file);
			Executions.createComponents("/hba/payments/cashInvoicePrintSetting.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCashInvoice ----> cashInvoiceSetting", ex);			
		}
	}

	public String getMsgToBeDispalyedOnInvoice() {
		return msgToBeDispalyedOnInvoice;
	}

	public void setMsgToBeDispalyedOnInvoice(String msgToBeDispalyedOnInvoice) {
		this.msgToBeDispalyedOnInvoice = msgToBeDispalyedOnInvoice;
	}

	public double getExChnage() {
		return exChnage;
	}

	public void setExChnage(double exChnage) {
		this.exChnage = exChnage;
	}

	public String getInvoiceNewSaleNo() {
		return invoiceNewSaleNo;
	}

	public void setInvoiceNewSaleNo(String invoiceNewSaleNo) {
		this.invoiceNewSaleNo = invoiceNewSaleNo;
	}

	public String getInvoiceNewBillToAddress() {
		return invoiceNewBillToAddress;
	}

	public void setInvoiceNewBillToAddress(String invoiceNewBillToAddress) {
		this.invoiceNewBillToAddress = invoiceNewBillToAddress;
	}

	public double getTempTotalAmount() {
		return tempTotalAmount;
	}

	public void setTempTotalAmount(double tempTotalAmount) {
		this.tempTotalAmount = tempTotalAmount;
	}

	public int getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public int getCashInvoiceKey() {
		return cashInvoiceKey;
	}

	public void setCashInvoiceKey(int cashInvoiceKey) {
		this.cashInvoiceKey = cashInvoiceKey;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
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



	@NotifyChange({"objCashInvoice"})
	@Command
	public void refreshCustomerBalance() {
		if(selectedInvcCutomerName!=null)
		{
			CashInvoiceModel obj=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());		
			objCashInvoice.setInvoiceProfileNumber(obj.getTotalBalance());
		}
		else
		{
			Messagebox.show("Invlaid Customer Name !!");			
		}
	}

	public String getFocusItem() {
		return focusItem;
	}

	public void setFocusItem(String focusItem) {
		this.focusItem = focusItem;
	}

	public String getFocusDesc() {
		return focusDesc;
	}

	public void setFocusDesc(String focusDesc) {
		this.focusDesc = focusDesc;
	}

	public String getFocusQty() {
		return focusQty;
	}

	public void setFocusQty(String focusQty) {
		this.focusQty = focusQty;
	}

	public String getFocusRate() {
		return focusRate;
	}

	public void setFocusRate(String focusRate) {
		this.focusRate = focusRate;
	}

	public String getFocusClass() {
		return focusClass;
	}

	public void setFocusClass(String focusClass) {
		this.focusClass = focusClass;
	}

	public String getFocusAmount() {
		return focusAmount;
	}

	public void setFocusAmount(String focusAmount) {
		this.focusAmount = focusAmount;
	}

	public String getFocusNewLine() {
		return focusNewLine;
	}

	public void setFocusNewLine(String focusNewLine) {
		this.focusNewLine = focusNewLine;
	}

	public boolean isSkipFocus() {
		return skipFocus;
	}

	public void setSkipFocus(boolean skipFocus) {
		this.skipFocus = skipFocus;
	}

	public String getFocusColumnAfterScan() {
		return focusColumnAfterScan;
	}

	public void setFocusColumnAfterScan(String focusColumnAfterScan) {
		this.focusColumnAfterScan = focusColumnAfterScan;
	}

	@Command
	@NotifyChange({"focusItem","focusDesc","focusQty","focusRate","focusClass","focusAmount","focusNewLine","lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","tempTotalAmount"})
	public void setGridFocus(@BindingParam("type") final CashInvoiceGridData type){

		if(skipFocus==false){
			if(type!=null && type.getRecNo()>0){
				setFocusVariables(type);
			}
			else
			{
			}
		}
		else{

			focusNewLine="false";
			skipFocus = false;
		}

	}

	private void setFocusVariables(CashInvoiceGridData gridData){

		//	focusItem = "false";
		//	focusDesc = "false";
		//	focusQty = "false";
		//	focusRate = "false";
		//	focusClass = "false";
		//	focusAmount = "false";
		//	focusNewLine = "false";

		if(focusColumnAfterScan==null || focusColumnAfterScan.trim().equals("") || focusColumnAfterScan.trim().equalsIgnoreCase("Item")){

			focusDesc = "false";
			focusQty = "false";
			focusRate = "false";
			focusClass = "false";
			focusAmount = "false";
			focusNewLine = "false";
			focusItem = "true";

		}else if(focusColumnAfterScan.trim().equalsIgnoreCase("Description")){

			focusItem = "false";
			focusQty = "false";
			focusRate = "false";
			focusClass = "false";
			focusAmount = "false";
			focusNewLine = "false";
			focusDesc = "true";

		}else if(focusColumnAfterScan.trim().equalsIgnoreCase("Qty")){

			focusItem = "false";
			focusDesc = "false";
			focusRate = "false";
			focusClass = "false";
			focusAmount = "false";
			focusNewLine = "false";
			focusQty = "true";

		}else if(focusColumnAfterScan.trim().equalsIgnoreCase("Rate")){

			focusItem = "false";
			focusDesc = "false";
			focusQty = "false";
			focusClass = "false";
			focusAmount = "false";
			focusNewLine = "false";
			focusRate = "true";

		}else if(focusColumnAfterScan.trim().equalsIgnoreCase("Class")){

			focusItem = "false";
			focusDesc = "false";
			focusQty = "false";
			focusRate = "false";
			focusAmount = "false";
			focusNewLine = "false";
			focusClass = "true";

		}else if(focusColumnAfterScan.trim().equalsIgnoreCase("Amount")){

			focusItem = "false";
			focusDesc = "false";
			focusQty = "false";
			focusRate = "false";
			focusClass = "false";
			focusNewLine = "false";
			focusAmount = "true";

		}else { //Create a New Line

			focusItem = "false";
			focusDesc = "false";
			focusQty = "false";
			focusRate = "false";
			focusClass = "false";
			focusAmount = "false";
			focusNewLine = "true";

			insertCheckItemsData(gridData);

			//		focusNewLine = "true";
		}

		if(skipFocus==false){
			skipFocus = true;
		}
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
	public String getCountryName(int countryKey) {
		String country = "";
		for (HRListValuesModel listValuesModel : countries) {
			if (countryKey != 0 && countryKey == listValuesModel.getListId()) {
				country = listValuesModel.getEnDescription();
				break;
			}
		}
		return country;
	}

	public String getCityName(int CityKey) {
		String City = "";
		for (HRListValuesModel model : cities) {
			if (CityKey != 0 && CityKey == model.getListId()) {
				City = model.getEnDescription();
				break;
			}
		}
		return City;

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail" })
	public void CustomerSendEmail(@BindingParam("row") final CashInvoiceModel row) {
		if (validateData(false)) {
			lstAtt = new ArrayList<QuotationAttachmentModel>();
			selectedAttchemnets.setFilename(selectedInvcCutomerName.getFullName()+ " Cash Invoice.pdf");
			selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Cash Invoice?",	"Cash Invoice", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt)
						throws InterruptedException {
					if (evt.getName().equals("onYes")) {
						createPdfSendEmail = false;
						createPdfForPrinting();
					}
					if (evt.getName().equals("onNo")) {
						try {
							createPdfSendEmail = true;
							createPdfForPrinting();
							createPdfSendEmail = false;
							Map<String, Object> arg = new HashMap<String, Object>();
							arg.put("id", row.getCustomerRefKey());
							arg.put("lstAtt", lstAtt);
							arg.put("feedBackKey", 0);
							arg.put("formType", "Customer");
							arg.put("type", "OtherForms");
							Executions.createComponents("/crm/editCustomerFeedbackSend.zul",null, arg);
						} catch (Exception ex) {
							logger.error("ERROR in EditCashInvoice ----> CustomerSendEmail",ex);
						}
					}
				}
			});
		}
	}

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==132)
			{
				companyRole=item;
				break;
			}
		}
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

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
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

	public int getCustomerKeyOtherForms() {
		return customerKeyOtherForms;
	}

	public void setCustomerKeyOtherForms(int customerKeyOtherForms) {
		this.customerKeyOtherForms = customerKeyOtherForms;
	}

	public boolean isOtherformFalg() {
		return otherformFalg;
	}

	public void setOtherformFalg(boolean otherformFalg) {
		this.otherformFalg = otherformFalg;
	}

	public MenuData getMenuData() {
		return menuData;
	}

	public void setMenuData(MenuData menuData) {
		this.menuData = menuData;
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

	public String getWebUserName() {
		return webUserName;
	}

	public void setWebUserName(String webUserName) {
		this.webUserName = webUserName;
	}

	public List<MenuModel> getList() {
		return list;
	}

	public void setList(List<MenuModel> list) {
		this.list = list;
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

	public BarcodeSettingsModel getBarcodeSettings() {
		return barcodeSettings;
	}

	public void setBarcodeSettings(BarcodeSettingsModel barcodeSettings) {
		this.barcodeSettings = barcodeSettings;
	}

	public ItemsData getDataBarc() {
		return dataBarc;
	}

	public void setDataBarc(ItemsData dataBarc) {
		this.dataBarc = dataBarc;
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

	public boolean isShowDelivery() {
		return showDelivery;
	}

	public void setShowDelivery(boolean showDelivery) {
		this.showDelivery = showDelivery;
	}
	
	@Command
	public void selectDelivery(){
		try {
			if(labelStatus.equalsIgnoreCase("Create")){
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("customerKey",selectedInvcCutomerName.getRecNo());
				arg.put("invoiceType","cash");
				Executions.createComponents("/hba/payments/selectDelivery.zul", null, arg);
			}

		} catch (Exception ex) {
			logger.error("ERROR in EditDelivery ----> selectQuotation",ex);
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"lstCashInvoiceCheckItems","totalAmount","objCashInvoice","selectD","showDelivery"})
	public void getlstSelectedDelivery(@BindingParam("lstSelectedDelivery") List<DeliveryModel> selectedDelivery)
	{		
		try
		{
			lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
			for(DeliveryModel deliveryLine:selectedDelivery)
			{ 
				if(deliveryLine.getLevel().equalsIgnoreCase("S")){
					CashInvoiceGridData obj=new CashInvoiceGridData();
					obj.setLineNo(lstCashInvoiceCheckItems.size()+1);					
					for(QbListsModel gridItem:lstItems)					
					{
						if(gridItem.getRecNo()==deliveryLine.getItemrefkey())
						{
							obj.setSelectedItems(gridItem);
							break;
						}

					}
					for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
					{
						if(gridSite.getRecNo()==deliveryLine.getInvertySiteKey())
						{
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if(gridSite.getRecNo()>0)
								obj.setHideSite(true);
							break;
						}

					}
					for(QbListsModel gridClass:lstInvcCustomerGridClass)
					{
						if(gridClass.getRecNo()==deliveryLine.getClassRefKey())
						{
							obj.setSelectedInvcCutomerGridInvrtyClassNew(gridClass);
							break;
						}

					}
					obj.setDeliveryLineNo(deliveryLine.getLineNo());
					obj.setDeliverRecNo(deliveryLine.getRecNo());
					obj.setInvoiceQty((int)deliveryLine.getQuantity());
					obj.setServiceDate(deliveryLine.getTxnDate());
					obj.setInvoiceQtyOnHand((int) deliveryLine.getQuantityInvoice());
					obj.setInvoiceAmmount(deliveryLine.getAmount());
					obj.setInvoiceDescription(deliveryLine.getMemo());
					obj.setOverrideItemAccountRefKey(0);
					obj.setIsTaxable("Y");
					obj.setOther1("0");
					obj.setOther2("0");
					obj.setSalesTaxCodeRefKey(0);
					lstCashInvoiceCheckItems.add(obj);
				}
			}
			setLabelCheckTotalcost();
			objCashInvoice.setTransformD("Y");
			selectD=true;
			showDelivery=false;

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCashInvoice ----> getlstSelectedDelivery", ex);			
		}
	}

	public boolean isSelectD() {
		return selectD;
	}

	public void setSelectD(boolean selectD) {
		this.selectD = selectD;
	}

	public boolean isTransformD() {
		return transformD;
	}

	public void setTransformD(boolean transformD) {
		this.transformD = transformD;
	}

	public List<VATCodeModel> getLstVatCodeList() {
		return lstVatCodeList;
	}

	public void setLstVatCodeList(List<VATCodeModel> lstVatCodeList) {
		this.lstVatCodeList = lstVatCodeList;
	}


	@Command
	@NotifyChange({ "toatlAmount", "lstCashInvoiceCheckItems","lblTotalCost","balance","amountPiad","exChnage" })
	public void selectVatCode(@BindingParam("type") final CashInvoiceGridData type) {
		VATCodeOperation.getCashInvoiceItemVatAmount(compSetup,type);
		setLabelCheckTotalcost();
	}
}
