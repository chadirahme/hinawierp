package hba;

import home.QuotationAttachmentModel;
import hr.HRData;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
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

import layout.MenuData;
import layout.MenuModel;
import model.AccountsModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.CompSetupModel;
import model.HRListValuesModel;
import model.QbListsModel;
import model.SerialFields;
import model.TermModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

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
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import common.NumberToWord;
import company.CompanyData;

/**
 * @author IQBALMUFASIL
 *
 */
public class QuotationHbaViewModel {

	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	NumberToWord numbToWord=new NumberToWord();
	private Logger logger = Logger.getLogger(QuotationHbaViewModel.class);
	private Date creationdate; 
	private CashInvoiceModel objCashInvoice;
	HBAData data=new HBAData();
	DecimalFormat formatter = new DecimalFormat("#,###.00");
	MenuData menuData=new MenuData();
	//private static String FILE = ":/temp/hello.pdf";
	//for the grid
	private CashInvoiceGridData selectedGridItems;
	private List<CashInvoiceGridData> lstCashInvoiceCheckItems;
	private List <QbListsModel> lstCashInvoiceGridItem;
	private List <QbListsModel> lstInvcCustomerGridInvrtySite;
	private List <QbListsModel> lstInvcCustomerGridClass;
	//to the form
	private List <QbListsModel> lstInvcCustomerName;
	private QbListsModel selectedInvcCutomerName;
	private List <QbListsModel> lstInvcCustomerClass;
	private QbListsModel selectedInvcCutomerClass;
	private List <QbListsModel> lstInvcCustomerDepositTo;
	private QbListsModel selectedInvcCutomerDepositeTo;
	private List <QbListsModel> lstInvcCustomerSalsRep;
	private QbListsModel selectedInvcCutomerSalsRep;

	private List<QbListsModel> lstInvcCustomerPaymntMethd=new ArrayList<QbListsModel>();
	private QbListsModel selectedInvcCutomerPaymntMethd;

	private List <QbListsModel> lstInvcCustomerTemplate;
	private QbListsModel selectedInvcCutomerTemplate;

	private List <AccountsModel> ltnCreditinvcAccount;
	private AccountsModel selectedCreditinvAcount;

	private List <TermModel> lstCreditInvoiceTerms;
	private TermModel selectedCreditInvoiceTerms;

	private String invoiceNewSaleNo;
	private String invoiceNewBillToAddress;

	private Date dueDate;
	private List<QuotationAttachmentModel> lstAtt=new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets=new QuotationAttachmentModel();

	private boolean createPdfSendEmail=false;

	private List<QbListsModel> lstInvcCustomerSendVia;
	private QbListsModel selectedInvcCutomerSendVia;

	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	private String webUserName="";
	private String lblTotalCost;

	private double toatlAmount;

	private double tempTotalAmount;

	private CompSetupModel compSetup;

	//for point of sale 
	private boolean showFieldsPOS=false;

	private boolean pointofSale;

	private double balance;

	private double amountPiad;
	private double exChnage;

	private String customerCreditLimit;

	private List<String> barcodeValues;

	//private String selectedBarCode;

	private String msgToBeDispalyedOnInvoice="";

	private int webUserID=0;

	private MenuModel companyRole;

	List<MenuModel> list;

	String actionTYpe;

	private boolean canView=false;

	private boolean canModify=false;

	private boolean canPrint=false;

	private boolean canCreate=false;

	private String labelStatus="";

	private boolean adminUser;

	private int  creditInvoiceKey;


	private String selectedRadioButtons;

	private int remindMeNoOfDays;

	private Date remindMeDate;

	private List<String> listClientType;

	private String selectedClientType;

	private boolean disableRemindMeOn=false;
	private HRData hrData = new HRData();
	private boolean disableRemindMe=false;

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	CompanyData companyData=new CompanyData();

	private boolean seeTrasction=false;


	@SuppressWarnings({ "unused", "rawtypes" })
	public QuotationHbaViewModel() {
		try{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			creditInvoiceKey=0;
			actionTYpe="Create";
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			canView=companyRole.isCanView();
			canModify=companyRole.isCanModify();
			canPrint=companyRole.isCanPrint();
			canCreate=companyRole.isCanAdd();
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction
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

			showFieldsPOS=false;
			fillBarCode();
			objCashInvoice=new CashInvoiceModel();
			Calendar c = Calendar.getInstance();		
			compSetup=data.getDefaultSetUpInfoForCashInvoice();
			creationdate=df.parse(sdf.format(c.getTime()));
			remindMeDate=df.parse(sdf.format(c.getTime()));
			if(selectedClientType!=null)
			{
				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
				{
					lstInvcCustomerName=data.quotationPrecpectiveList();
				}
				else
				{
					lstInvcCustomerName=data.quotationCustomerList();
				}
			}
			lstCashInvoiceGridItem=data.fillQbItemsList();
			lstInvcCustomerClass=data.GetMasterData("Class");
			lstInvcCustomerSalsRep=data.GetMasterData("SalesRep");
			lstInvcCustomerTemplate=data.GetMasterData("Template");
			lstInvcCustomerSendVia=data.GetMasterData("SendVia");
			lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
			lstInvcCustomerGridClass=data.GetMasterData("GridClass");
			//ltnCreditinvcAccount=data.getAccountsForCreditInvoice();
			//lstCreditInvoiceTerms=data.getTermsForCreditInvoice();
			if(creditInvoiceKey>0)
			{
				labelStatus="Edit";

				objCashInvoice=data.getQuatationByID(creditInvoiceKey,webUserID,seeTrasction);
				List<CashInvoiceGridData> invoiceModelnew=data.getQuotationridDataByID(creditInvoiceKey);
				if(objCashInvoice!=null)
				{
					for(QbListsModel cutomerNmae:lstInvcCustomerName)
					{
						if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
						{
							selectedInvcCutomerName=cutomerNmae;
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

					for(QbListsModel salesRep:lstInvcCustomerSalsRep)
					{
						if(salesRep.getRecNo()==objCashInvoice.getSalesRefKey())
						{
							selectedInvcCutomerSalsRep=salesRep;
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

					//objCashInvoice.setPoNumber(objCashInvoice.getPoNumber());
					objCashInvoice.setInvoiceMemo(objCashInvoice.getMemo());
					toatlAmount=objCashInvoice.getAmount();
					tempTotalAmount=objCashInvoice.getAmount();
					invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
					if(objCashInvoice.getTxnDate()!=null)
						creationdate=df.parse(sdf.format(objCashInvoice.getTxnDate()));
					if(objCashInvoice.getDueDate()!=null)
						dueDate=df.parse(sdf.format(objCashInvoice.getDueDate()));
					if(objCashInvoice.getRemindMeDate()!=null)
						remindMeDate=df.parse(sdf.format(objCashInvoice.getRemindMeDate()));
					String cleintType=objCashInvoice.getClientType();
					if(cleintType!=null)
					{
						if(cleintType.equalsIgnoreCase("C"))
						{
							selectedClientType=listClientType.get(1);
						}
						else if(cleintType.equalsIgnoreCase("P"))
						{
							selectedClientType=listClientType.get(0);
						}
					}

					String remindFlag=objCashInvoice.getRemingMeFalg();
					if(remindFlag!=null)
					{
						if(remindFlag.equalsIgnoreCase("D"))
						{
							selectedRadioButtons="A";
							disableRemindMe=true;
							remindMeNoOfDays=0;
							disableRemindMeOn=false;
						}
						else if(remindFlag.equalsIgnoreCase("S"))
						{
							selectedRadioButtons="B";
							disableRemindMeOn=true;
							disableRemindMe=false;
						}
						else if(remindFlag.equalsIgnoreCase("N"))
						{
							selectedRadioButtons="C";
							disableRemindMe=true;
							disableRemindMeOn=true;
							remindMeNoOfDays=0;
						}
					}
					remindMeNoOfDays=objCashInvoice.getRemindMedays();
					invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"\n"+objCashInvoice.getBillAddressCity()+"\n"+objCashInvoice.getBillAddressState()+"";
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
				dueDate=creationdate;
				objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.Quotation.toString()));
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				objCashInvoice.setRecNo(0);
				lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
				CashInvoiceGridData objItems=new CashInvoiceGridData();
				objItems.setLineNo(1);
				objItems.setInvoiceQty(1);
				lstCashInvoiceCheckItems.add(objItems);
				lblTotalCost="Amount :0.00";
				toatlAmount=0.0;
				tempTotalAmount=0.0;
				selectedRadioButtons="C";

			}
		}
		catch(Exception ex)
		{
			logger.error("ERROR in QutationHbaViewModel ----> onLoad", ex);
		}
	}

	public void fillBarCode()
	{
		barcodeValues=data.getLstBarcodes(null);
		listClientType=new ArrayList<String>();
		listClientType.add("Prospective Client");
		listClientType.add("Customer");
		selectedClientType=listClientType.get(1);
	}




	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","tempTotalAmount","balance","amountPiad","toatlAmount","exChnage"})
	public void selectInvoiceGridItems(@BindingParam("type") final  CashInvoiceGridData type)
	{

		if(type.getSelectedItems()!=null)
		{

			boolean hasSubAccount=data.checkIfItemHasSubQuery(type.getSelectedItems().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostItem2Main().equals("Y"))
				{
					Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
								type.setInvoiceQty(0);
								type.setInvoiceDescription("");
								type.setSelectedInvcCutomerGridInvrtySiteNew(null);
								type.setSelectedInvcCutomerGridInvrtyClassNew(null);
								type.setAvgCost(0);
								type.setInvoiceRate(0);
								type.setInvoiceAmmount(0);
								setLabelCheckTotalcost();
								BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
								BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "toatlAmount");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Quotation", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedItems(null);	
					type.setInvoiceQty(0);
					type.setInvoiceDescription("");
					type.setSelectedInvcCutomerGridInvrtySiteNew(null);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);
					type.setAvgCost(0);
					type.setInvoiceRate(0);
					type.setInvoiceAmmount(0);
					setLabelCheckTotalcost();
					BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
					BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "toatlAmount");
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
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","balance","amountPiad","toatlAmount","exChnage"})
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
							Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {						
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
										type.setInvoiceQty(0);
										type.setInvoiceDescription("");
										type.setSelectedInvcCutomerGridInvrtySiteNew(null);
										type.setSelectedInvcCutomerGridInvrtyClassNew(null);
										type.setAvgCost(0);
										type.setInvoiceRate(0);
										type.setInvoiceAmmount(0);
										setLabelCheckTotalcost();
										BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
										BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "toatlAmount");

									}
								}

							});
						}
						else
						{
							Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Quotation", Messagebox.OK , Messagebox.INFORMATION);
							type.setSelectedItems(null);
							type.setInvoiceQty(0);
							type.setInvoiceDescription("");
							type.setSelectedInvcCutomerGridInvrtySiteNew(null);
							type.setSelectedInvcCutomerGridInvrtyClassNew(null);
							type.setAvgCost(0);
							type.setInvoiceRate(0);
							type.setInvoiceAmmount(0);
							setLabelCheckTotalcost();
							BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
							BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "toatlAmount");
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

				Messagebox.show("Invalid Barcode!","Credit Invoice", Messagebox.OK , Messagebox.EXCLAMATION);
			}
		}else if(type.getSelectedItems()!=null){

			for(QbListsModel model:lstCashInvoiceGridItem)
			{
				if(model.getRecNo()==type.getRecNo()){

					type.setBarcode(model.getBarcode());
				}
			}
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

	public void selectInvoiceItemOnfuction(final CashInvoiceGridData type)
	{
		if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && (type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")))

		{
			type.setHideSite(true);
			innerFuction(type);
			/*	if(compSetup.getSellStockWithZero().equals("N"))
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
			type.setInvoiceQty(1);
			type.setInvoiceRate(objItems.getInvoiceRate());
			type.setInvoiceAmmount(type.getInvoiceRate() * type.getInvoiceQty());
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
			toalCheckItemsAmount+=item.getInvoiceAmmount();
		}
		lblTotalCost="Amount :" + BigDecimal.valueOf(toalCheckItemsAmount).toPlainString();
		toatlAmount=toalCheckItemsAmount;
		tempTotalAmount=toalCheckItemsAmount;
	}

	@Command   
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount"})
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
	@NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount"})
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
				Messagebox.show("To add new record,First select Item from the existing record!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return;
			}
		}

	}

	//need to work on these below stuff


	@Command  
	@NotifyChange({"toatlAmount"})
	public void addNewCashInvoice()
	{
		if(validateData(true))
		{
			saveData();
		}
	}

	/*@SuppressWarnings("unchecked")
	@Command
	public void addNewCashInvoiceClear()
	{
		if(true)
		{

			Messagebox.show("Are you sure to add Cash Invoice ?", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {
				    public void onEvent(Event evt) throws InterruptedException {
				        if (evt.getName().equals("onYes")) 
				        {	
				        	saveData();
				        	 Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
							 Center center = bl.getCenter();
							 Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
							 tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
				        }
				        else 
				        {				        	
				        	return;
				        }
				    }

				});	

		}
	}*/

	@Command
	public void closeCashInvoice()
	{
		if(true)
		{

			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().detach();
			tabbox.getSelectedTab().detach();	
			Tabs contentTabs=(Tabs)tabbox.getFellow("contentTabs");
			for (Component oldTab : contentTabs.getChildren()) 
			{
				if(oldTab instanceof Tab)
				{
					((Tab) oldTab).setSelected(true);
				}
			}


		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void clearCashInvoice()
	{
		if(true)
		{

			Messagebox.show("Are you sure to Clear Credit Invoice ? Your Data will be lost.!", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onYes")) 
					{	
						Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
						Center center = bl.getCenter();
						Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
						tabbox.getSelectedPanel().getLastChild().invalidate();					        					        
					}
					else 
					{				        	
						return;
					}
				}

			});	

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"toatlAmount"})
	public void addNewCashInvoiceClose()
	{
		if(validateData(true))
		{

			Messagebox.show("Are you sure to add Credit Invoice ?", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onYes")) 
					{	
						saveData();
						Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
						Center center = bl.getCenter();
						Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
						tabbox.getSelectedPanel().detach();
						tabbox.getSelectedTab().detach();	
						Tabs contentTabs=(Tabs)tabbox.getFellow("contentTabs");
						for (Component oldTab : contentTabs.getChildren()) 
						{
							if(oldTab instanceof Tab)
							{
								((Tab) oldTab).setSelected(true);
							}
						}


					}
					else 
					{				        	
						return;
					}
				}

			});	

		}
	}


	private boolean validateData(boolean Printflag)
	{
		boolean isValid=true;


		if(selectedClientType==null)
		{		
			Messagebox.show("Select a client Type!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedInvcCutomerName==null)
		{		
			Messagebox.show("Select an existing Customer Name For This Transaction!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		/*if(selectedCreditinvAcount==null)
		{		
			Messagebox.show("Select an existing Account For This Transaction!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}*/

		if(tempTotalAmount!=toatlAmount)
		{		
			Messagebox.show("The amount entered is not matching with actual grid total.!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
			toatlAmount = Double.parseDouble(BigDecimal.valueOf(tempTotalAmount).toPlainString());
			return false;
		}

		if(toatlAmount<=0)
		{		
			Messagebox.show("The total amount should not be less than zero or zero!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(compSetup.getUseSalesRepComition().equalsIgnoreCase("Y"))
		{	
			if(selectedInvcCutomerSalsRep==null)
			{
				Messagebox.show("Please select the sales rep to continue!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}


		/*if(toatlAmount==0)
		{		

			if(compSetup.getSaveInvQty().equalsIgnoreCase("Y"))
			{	
				Messagebox.show("There is no value of Invoice,Do you want to Continue saving","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
 				    public void onEvent(Event evt) throws InterruptedException {
 				        if (evt.getName().equals("onYes")) 
 				        {	 			
 				        	Map args = new HashMap();
 				        }
 				        else 
 				        {		 
 				        	return;
 				        }
 				    }

				});
			}
		}*/

		if(creditLimit()==false)
		{
			return false;
		}
		if(lstCashInvoiceCheckItems.size()<=0)
		{
			Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		/*	if(lstCashInvoiceCheckItems!=null)
		{	
			for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
			{
			CashInvoiceGridData lastItem=gridData;
			if(lastItem.getSelectedItems()!=null)
			{					
				if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null))
				{
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			}
			else{
				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			CashInvoiceGridData objExp=gridData;
			if(objExp.getSelectedItems()!=null)
			{
				if(objExp.getInvoiceRate()==0)
				{	 		
	 			Messagebox.show("Please Enter The Rate value ,Empty Transaction is not allowed !!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
	 			return false;
				}

				if(objExp.getInvoiceQty()==0)
				{	 		
	 			Messagebox.show("Please Enter The Qantity,Empty Transaction is not allowed !!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
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
				if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && (lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null || lastItem.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0))
				{
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			}
			else{
				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Quotation",Messagebox.OK,Messagebox.INFORMATION);
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
					Messagebox.show("Please Enter The Quantity,Empty Transaction is not allowed !!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}

			}

			if(!RateValidation)
			{
				Messagebox.show("Please Enter The Rate value  the Quoation amount cannot be zero,Empty Transaction is not allowed !!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			if(siteValidation && (compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")))
			{
				Messagebox.show("To Save this record,First select Site Name from the existing records in the Grid!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
			CashInvoiceGridData objExp=lstCashInvoiceCheckItems.get(0);
			if(objExp.getSelectedItems()==null)
			{
				Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

		}


		if(Printflag)
		{
			if((invoiceNewSaleNo!=null) && (data.checkIfSerialNumberIsDuplicateForQuotation(invoiceNewSaleNo,objCashInvoice.getRecNo())==true))
			{
				Messagebox.show("Duplicate Qutation Number!","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
			if(invoiceNewSaleNo==null)
			{
				Messagebox.show("Please Enter the Qutation Number","Quotation",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}


		return isValid;

	}


	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public boolean creditLimit()
	{
		boolean abcGetCreditLimitFlag = true;
		double _CreditLimit=0;
		double _LocalBalance=0;
		int _CUSTKey=0;
		if(customerCreditLimit!=null && !"".equalsIgnoreCase(customerCreditLimit))
		{

			if(Double.parseDouble(customerCreditLimit)>0)
			{
				_CreditLimit=Double.parseDouble(customerCreditLimit);
				CashInvoiceModel cashInvoiceModel=data.getLocalBalnaceForCreditInvoice(selectedInvcCutomerName.getRecNo());
				if(null!=cashInvoiceModel)
				{
					_LocalBalance=cashInvoiceModel.getInvoiceAmount()+cashInvoiceModel.getPaidAmount();
				}
				_LocalBalance = _LocalBalance + toatlAmount;

				if(_CreditLimit<_LocalBalance)
				{
					if(compSetup.getCanExceedOverLimit().equalsIgnoreCase("N"))
					{
						Messagebox.show("You cannot save the Invoice. The Credit Limit is less than the Local balance","Quotation",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}        				
					else
					{
						final boolean getCreditLimitFlag = true;
						Messagebox.show("The Credit Limit is less than the Local balance,Do you want to Continue ?","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {		

							public void onEvent(Event evt) throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{	 			
									//	getCreditLimitFlag =true;
								}
								else 
								{		 
									//getCreditLimitFlag = false;
								}
							}

						});
						/*if(getCreditLimitFlag==true)
                			{
                				abcGetCreditLimitFlag=true;
                			}
                			else
                			{
                				abcGetCreditLimitFlag=false;
                			}*/


					}

				}

			}

		}

		return abcGetCreditLimitFlag;



	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCashInvoiceCheckItems"})
	public void selectCashInvoiceGridClass(@BindingParam("type")  final CashInvoiceGridData type)
	{
		if(type.getSelectedInvcCutomerGridInvrtyClassNew()!=null)
		{
			//check if class has sub class		
			boolean hasSubAccount=data.checkIfClassHasSub(type.getSelectedInvcCutomerGridInvrtyClassNew().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
								BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Quotation", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedInvcCutomerGridInvrtyClassNew(null);	
					BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
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
			boolean hasSubAccount=data.checkIfClassHasSub(selectedInvcCutomerClass.getName()+":");
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
								BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "selectedInvcCutomerClass");
							}
						}

					});
				}
				else

				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					selectedInvcCutomerClass=null;
					BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "selectedInvcCutomerClass");
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
						Messagebox.show("Quantity Available only is " +type.getInvoiceQtyOnHand() + "., Do you want to continue","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
									BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
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
					return;
				}

				if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem"))
				{
					if(compSetup.getAllowSavingAvgCost().equals("N"))
					{
						if(Math.round((type.getAvgCost()*100)/100) > Math.round((type.getInvoiceRate())*100/100))
						{

							Messagebox.show("The rate is lesser than the Avg. Cost" +type.getInvoiceAmmount() + "., Do you want to continue","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
										BindUtils.postNotifyChange(null, null, QuotationHbaViewModel.this, "lstCashInvoiceCheckItems");
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
			Messagebox.show("You Connot Continue.!Please select the Item from the grid","Quotation",Messagebox.OK,Messagebox.INFORMATION);
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
						Messagebox.show("The Price you entered is lower than the standard & discount allowed!","Credit Invoice", Messagebox.OK , Messagebox.INFORMATION);
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
						Messagebox.show("The Price entered is higher than the standard & discount allowed!","Credit Invoice", Messagebox.OK , Messagebox.INFORMATION);
						_ActSellingPrice = ACTSellingPrice;
						return false;
					}

				}
			}
			if(MINSPFlag == false && MAXSPFlag == false)
			{
				if(UserSellingPrice < ACTSellingPrice)
				{
					Messagebox.show("The Price entered is lower than the standard selling price!","Credit Invoice", Messagebox.OK , Messagebox.INFORMATION);
					_ActSellingPrice = ACTSellingPrice;
					return false;
				}
			}

		}
		return true;
	}

	private void saveData() 
	{	      
		try
		{	
			int tmpRecNo=0;
			CashInvoiceModel obj=new CashInvoiceModel();

			if(creditInvoiceKey==0)
			{
				tmpRecNo=data.GetNewQuotationRecNo();	
			}
			else
			{
				tmpRecNo=objCashInvoice.getRecNo();
			}

			if(selectedClientType.equalsIgnoreCase("Prospective Client"))
			{
				obj.setClientType("P");
			}
			else
			{
				obj.setClientType("C");
			}

			if(selectedRadioButtons.equalsIgnoreCase("A"))
			{
				obj.setRemingMeFalg("D");
				obj.setRemindMeDate(remindMeDate);
			}
			else if(selectedRadioButtons.equalsIgnoreCase("B"))
			{
				obj.setRemingMeFalg("S");
				obj.setRemindMedays(remindMeNoOfDays);
			}
			else
			{
				obj.setRemingMeFalg("N");
			}

			obj.setRecNo(tmpRecNo);
			if(objCashInvoice.getTxnId()!=null && !objCashInvoice.getTxnId().equalsIgnoreCase(""))
				obj.setTxnId(objCashInvoice.getTxnId());
			else
				obj.setTxnId(" ");	
			obj.setCustomerRefKey(selectedInvcCutomerName.getRecNo());
			if(null!=selectedInvcCutomerClass)
			{
				obj.setClassRefKey(selectedInvcCutomerClass.getRecNo());
			}
			else
			{
				obj.setClassRefKey(0);
			}

			//	obj.setAccountRefKey(selectedCreditinvAcount.getRec_No());
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
			obj.setShipToAddress(invoiceNewBillToAddress);
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
			if(null!=objCashInvoice.getPoNumber())
			{
				obj.setPoNumber(objCashInvoice.getPoNumber());
			}
			else
			{
				obj.setPoNumber("");
			}
			if(null!=selectedCreditInvoiceTerms)
			{
				obj.setTermRefKey(selectedCreditInvoiceTerms.getTermKey());
			}
			else
			{
				obj.setTermRefKey(0);	
			}

			obj.setfOB("0");
			obj.setDueDate(dueDate);
			obj.setShipDate(creationdate);
			if(null!=selectedInvcCutomerPaymntMethd)
			{
				obj.setShipMethodRefKey(selectedInvcCutomerPaymntMethd.getRecNo());
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
			obj.setCustomerMsgRefKey(0);
			obj.setIsToBePrinted("Y");
			obj.setIsToEmailed("N");
			obj.setIsTaxIncluded("N");
			obj.setCustomerSalesTaxCodeRefKey(0);
			obj.setOther("");
			obj.setAmount(toatlAmount);
			obj.setQuotationRecNo(0);
			obj.setAttachemnet("");
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
			obj.setLetterTemplate(null);
			int result=0;
			if(creditInvoiceKey==0)
			{
				result=data.saveQuotation(obj,webUserID);
			}else
			{
				result=data.updateQuotation(obj,webUserID);
			}
			if(result>0)
			{
				if(creditInvoiceKey==0)
				{
					data.ConfigSerialNumberCashInvoice(SerialFields.Quotation, invoiceNewSaleNo,0);
				}
				data.deleteQuotationGridItems(tmpRecNo);
				for (CashInvoiceGridData item : lstCashInvoiceCheckItems) 
				{
					if(item.getSelectedItems()!=null)
					{
						item.setServiceDate(creationdate);
						data.addNewQuotationGridItemsQuery(item, tmpRecNo);
					}
				}
				if (creditInvoiceKey == 0) {
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.Quotation.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getRefNumber(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
				} else {
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.Quotation.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getRefNumber(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue());
				}
			}
			if(creditInvoiceKey>0)
			{
				Clients.showNotification("The  Quotation  Has Been Updated Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
			}
			else
			{
				Clients.showNotification("The Quotation Has Been Created Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
			}

		}catch (Exception ex) 
		{
			logger.error("ERROR in QutationHbaViewModel ----> save", ex);			
		}
	}




	@Command
	@NotifyChange({"selectedInvcCutomerName","invoiceNewSaleNo","creationdate","lstInvcCustomerName","remindMeNoOfDays","remindMeDate","disableRemindMe","disableRemindMeOn","selectedClientType","selectedRadioButtons","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedCreditInvoiceTerms","selectedInvcCutomerSalsRep","selectedCreditinvAcount","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void navigationCreditInvoice(@BindingParam("cmp") String navigation)
	{
		try
		{
			objCashInvoice=data.navigationQuotation(creditInvoiceKey,webUserID,seeTrasction,navigation,actionTYpe);
			if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
			{
				actionTYpe="edit";
				labelStatus="Edit";
				creditInvoiceKey=objCashInvoice.getRecNo();
				List<CashInvoiceGridData> invoiceModelnew=data.getQuotationridDataByID(creditInvoiceKey);
				String cleintType=objCashInvoice.getClientType();
				if(cleintType!=null)
				{
					if(cleintType.equalsIgnoreCase("C"))
					{
						selectedClientType=listClientType.get(1);
					}
					else if(cleintType.equalsIgnoreCase("P"))
					{
						selectedClientType=listClientType.get(0);
					}
				}

				if(selectedClientType!=null)
				{
					if(selectedClientType.equalsIgnoreCase("Prospective Client"))
					{
						lstInvcCustomerName=data.quotationPrecpectiveList();
					}
					else
					{
						lstInvcCustomerName=data.quotationCustomerList();
					}
				}
				for(QbListsModel cutomerNmae:lstInvcCustomerName)
				{
					if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
					{
						selectedInvcCutomerName=cutomerNmae;
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

				for(QbListsModel salesRep:lstInvcCustomerSalsRep)
				{
					if(salesRep.getRecNo()==objCashInvoice.getSalesRefKey())
					{
						selectedInvcCutomerSalsRep=salesRep;
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

				objCashInvoice.setPoNumber(objCashInvoice.getPoNumber());
				objCashInvoice.setInvoiceMemo(objCashInvoice.getMemo());
				toatlAmount=objCashInvoice.getAmount();
				tempTotalAmount=objCashInvoice.getAmount();
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				if(objCashInvoice.getTxnDate()!=null)
					creationdate=df.parse(sdf.format(objCashInvoice.getTxnDate()));
				if(objCashInvoice.getDueDate()!=null)
					dueDate=df.parse(sdf.format(objCashInvoice.getDueDate()));
				if(objCashInvoice.getRemindMeDate()!=null)
				{
					remindMeDate=df.parse(sdf.format(objCashInvoice.getRemindMeDate()));
				}

				String remindFlag=objCashInvoice.getRemingMeFalg();
				if(remindFlag!=null)
				{
					if(remindFlag.equalsIgnoreCase("D"))
					{
						selectedRadioButtons="A";
						disableRemindMe=true;
						remindMeNoOfDays=0;
						disableRemindMeOn=false;
					}
					else if(remindFlag.equalsIgnoreCase("S"))
					{
						selectedRadioButtons="B";
						disableRemindMeOn=true;
						disableRemindMe=false;
					}
					else if(remindFlag.equalsIgnoreCase("N"))
					{
						selectedRadioButtons="C";
						disableRemindMe=true;
						disableRemindMeOn=true;
						remindMeNoOfDays=0;
					}
				}
				remindMeNoOfDays=objCashInvoice.getRemindMedays();
				invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"\n"+objCashInvoice.getBillAddressCity()+"\n"+objCashInvoice.getBillAddressState()+"";
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

				labelStatus="Create";
				actionTYpe="create";
				dueDate=creationdate;
				creditInvoiceKey=0;
				fillBarCode();
				if(selectedClientType!=null)
				{
					if(selectedClientType.equalsIgnoreCase("Prospective Client"))
					{
						lstInvcCustomerName=data.quotationPrecpectiveList();
					}
					else
					{
						lstInvcCustomerName=data.quotationCustomerList();
					}
				}
				objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.Quotation.toString()));
				invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
				objCashInvoice.setRecNo(0);
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
				//selectedInvcCutomerDepositeTo=null;
				selectedInvcCutomerSalsRep=null;
				//selectedCreditinvAcount=null;
				selectedInvcCutomerSendVia=null;
				//selectedCreditInvoiceTerms=null;
				invoiceNewBillToAddress="";
				selectedRadioButtons="C";
				if(selectedClientType!=null)
				{
					if(lstInvcCustomerName.size()>0)
					{
						selectedInvcCutomerName=lstInvcCustomerName.get(0);
					}
				}

			}



		}
		catch (Exception ex)
		{	
			logger.error("ERROR in QutationHbaViewModel ----> navigationCashInvoice", ex);			
		}
	}


	@Command
	@NotifyChange({"selectedInvcCutomerName","invoiceNewSaleNo","creationdate","lstInvcCustomerName","remindMeNoOfDays","remindMeDate","disableRemindMe","disableRemindMeOn","selectedClientType","selectedRadioButtons","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedCreditInvoiceTerms","selectedInvcCutomerSalsRep","selectedCreditinvAcount","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void copyFunctinality()
	{
		try
		{
			if(creditInvoiceKey>0)
			{
				objCashInvoice=data.getQuatationByID(creditInvoiceKey, webUserID,seeTrasction);
				if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
				{
					actionTYpe="edit";
					labelStatus="Copied-Create";
					creditInvoiceKey=objCashInvoice.getRecNo();
					List<CashInvoiceGridData> invoiceModelnew=data.getQuotationridDataByID(creditInvoiceKey);
					String cleintType=objCashInvoice.getClientType();
					if(cleintType!=null)
					{
						if(cleintType.equalsIgnoreCase("C"))
						{
							selectedClientType=listClientType.get(1);
						}
						else if(cleintType.equalsIgnoreCase("P"))
						{
							selectedClientType=listClientType.get(0);
						}
					}

					if(selectedClientType!=null)
					{
						if(selectedClientType.equalsIgnoreCase("Prospective Client"))
						{
							lstInvcCustomerName=data.quotationPrecpectiveList();
						}
						else
						{
							lstInvcCustomerName=data.quotationCustomerList();
						}
					}
					for(QbListsModel cutomerNmae:lstInvcCustomerName)
					{
						if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
						{
							selectedInvcCutomerName=cutomerNmae;
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

					for(QbListsModel salesRep:lstInvcCustomerSalsRep)
					{
						if(salesRep.getRecNo()==objCashInvoice.getSalesRefKey())
						{
							selectedInvcCutomerSalsRep=salesRep;
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

					objCashInvoice.setPoNumber(objCashInvoice.getPoNumber());
					objCashInvoice.setInvoiceMemo(objCashInvoice.getMemo());
					toatlAmount=objCashInvoice.getAmount();
					tempTotalAmount=objCashInvoice.getAmount();
					creditInvoiceKey=0;
					objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.Quotation.toString()));
					invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
					objCashInvoice.setRecNo(0);
					Calendar c = Calendar.getInstance();		
					creationdate=df.parse(sdf.format(c.getTime()));
					if(objCashInvoice.getDueDate()!=null)
						dueDate=df.parse(sdf.format(objCashInvoice.getDueDate()));
					if(objCashInvoice.getRemindMeDate()!=null)
					{
						remindMeDate=df.parse(sdf.format(objCashInvoice.getRemindMeDate()));
					}

					String remindFlag=objCashInvoice.getRemingMeFalg();
					if(remindFlag!=null)
					{
						if(remindFlag.equalsIgnoreCase("D"))
						{
							selectedRadioButtons="A";
							disableRemindMe=true;
							remindMeNoOfDays=0;
							disableRemindMeOn=false;
						}
						else if(remindFlag.equalsIgnoreCase("S"))
						{
							selectedRadioButtons="B";
							disableRemindMeOn=true;
							disableRemindMe=false;
						}
						else if(remindFlag.equalsIgnoreCase("N"))
						{
							selectedRadioButtons="C";
							disableRemindMe=true;
							disableRemindMeOn=true;
							remindMeNoOfDays=0;
						}
					}
					remindMeNoOfDays=objCashInvoice.getRemindMedays();
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
				Messagebox.show("You can only copy a existing Quotation","Quotation", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}




		}
		catch (Exception ex)
		{	
			logger.error("ERROR in QutationHbaViewModel ----> copyFunctinality", ex);			
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
	@SuppressWarnings("unused")
	@NotifyChange({"objCashInvoice","invoiceNewBillToAddress"})
	public void setSelectedInvcCutomerName(QbListsModel selectedInvcCutomerName) {
		this.selectedInvcCutomerName = selectedInvcCutomerName;
		if(selectedInvcCutomerName!=null)
		{

			if(selectedClientType!=null)
			{
				if(selectedClientType.equalsIgnoreCase("Prospective Client"))
				{
					CashInvoiceModel obj=data.getCashInvoiceCusomerInfo("Prospective", selectedInvcCutomerName.getRecNo());
					if(obj.getShipToAddress()!=null && !obj.getShipToAddress().equalsIgnoreCase(""))
						invoiceNewBillToAddress=obj.getShipToAddress();
					else
						invoiceNewBillToAddress=selectedInvcCutomerName.getFullName();
				}
				else
				{
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
					customerCreditLimit=obj.getCreditLimit();

				}
			}


		}
		else
		{
			Messagebox.show("Invlaid Customer Name !!");			
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
	public void setSelectedInvcCutomerDepositeTo(
			QbListsModel selectedInvcCutomerDepositeTo) {
		this.selectedInvcCutomerDepositeTo = selectedInvcCutomerDepositeTo;
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
	public void setSelectedInvcCutomerClass(QbListsModel selectedInvcCutomerClass) {
		this.selectedInvcCutomerClass = selectedInvcCutomerClass;
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
			Messagebox.show("You cannot enter amount received less than total cash invoice amount","Quotation", Messagebox.OK , Messagebox.INFORMATION);

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
				BaseFont bfarabic = BaseFont.createFont(
					    "c://temp//trado.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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
				Chunk c = new Chunk("Quotation");
				c.setUnderline(0.1f, -2f);
				c.setFont(f);
				Paragraph p = new Paragraph(c);



				firsttbl.addCell(p);


				PdfPCell cell1 = new PdfPCell(new Phrase("Date : "+sdf.format(creationdate)+"\n\n"+"Quotation No. : "+invoiceNewSaleNo));
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
				cell1 = new PdfPCell(new Phrase(invoiceNewBillToAddress ,arfont));
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

				PdfPTable table = new PdfPTable(5);
				table.setWidths(new int[]{60,210,60,60,60});
				table.setSpacingBefore(20);
				table.setWidthPercentage(100);
				table.getDefaultCell().setPadding(5);
				BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
				
				PdfPCell HeadderProduct = new PdfPCell(new Phrase("No."));
				HeadderProduct.setPadding(1);
				HeadderProduct.setColspan(1);
				HeadderProduct.setBorder(Rectangle.NO_BORDER);
				HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderProduct.setBackgroundColor(myColor);
				table.addCell(HeadderProduct);
				
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

				PdfPCell HeadderRate = new PdfPCell(new Phrase("Rate"));
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
				boolean desc=true;

				for (CashInvoiceGridData item : lstCashInvoiceCheckItems) 
				{

					table.addCell(new Phrase(""+item.getSelectedItems().getRecNo(),FontFactory.getFont(FontFactory.HELVETICA, 11)));
					if(desc)
					{
						table.addCell(new Phrase(item.getInvoiceDescription(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}
					if(item.getInvoiceQty()!=0){
						table.addCell(new Phrase(""+item.getInvoiceQty(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}else{
						table.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
					}
					if(item.getInvoiceRate()!=0.0){
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
					}

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
					Chunk chunk = new Chunk("Hope this offer meets your requirments. Please do not hesitate to contact us for further clarification.");
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);

				}else{

					paragraph=new Paragraph();
					Chunk chunk = new Chunk(msgToBeDispalyedOnInvoice);
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);

				}


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

				String amtStr1 = BigDecimal.valueOf(toatlAmount)
						.toPlainString();
				double amtDbbl1 = Double.parseDouble(amtStr1);
				cell1 = new PdfPCell(new Phrase("___________________\n\n  Customer Approval \n  Date:    /    /   2015", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
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
				logger.error("ERROR in createPdfForPrinting QutationHbaViewModel----> onLoad", ex);
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



				/*  PdfPTable table = new PdfPTable(2);
	         table.setTotalWidth(527);
	         //table.setLockedWidth(true);
	         table.getDefaultCell().setBorder(Rectangle.BOTTOM);
	         table.addCell(dbUser.getCompanyName()+"\n"+compSetup.getAddress());
	         table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);*/
				//  table.addCell(String.format("Page %d of %d", x, y));


				/* ColumnText.showTextAligned(writer.getDirectContent(),
		              Element.ALIGN_RIGHT, new Phrase("Shark Insurances"),
		              rect.getRight(), rect.getTop(), 0);

		      ColumnText.showTextAligned(writer.getDirectContent(),
		              Element.ALIGN_RIGHT, new Phrase("4543 1st Street"),
		              rect.getRight(), rect.getTop() - 15, 0);

		      ColumnText.showTextAligned(writer.getDirectContent(),
		              Element.ALIGN_RIGHT, new Phrase("Bay city, 38989 "),
		              rect.getRight(), rect.getTop() - 30, 0);

		      ColumnText.showTextAligned(writer.getDirectContent(),
		              Element.ALIGN_RIGHT, new Phrase("E-mail: info@sharks.com"),
		              rect.getRight(), rect.getTop() - 60, 0);*/

			} catch (BadElementException e) {
				logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf ----> onEndPage", e);
			} catch (MalformedURLException e) {
				logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf----> onEndPage", e);
			} catch (IOException e) {
				logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf----> onEndPage", e);
			} catch (DocumentException e) {
				logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf----> onEndPage", e);
			}
		}
	}	



	/**
	 * Create a header table with page X of Y
	 * @param x the page number
	 * @param y the total number of pages
	 * @return a table that can be used as header
	 */
	/*public static PdfPTable getHeaderTable(int x, int y,CompSetupModel compSetup,HBAData data,,) {
Session sess = Sessions.getCurrent();
WebusersModel dbUser=null;
Image logo=null;
try {
dbUser=(WebusersModel)sess.getAttribute("Authentication");
String path=data.getImageData(dbUser.getCompanyName());
logo = Image.getInstance(path);
Chunk chunk = new Chunk(logo, 0, -45);
PdfPTable table = new PdfPTable(2);
table.setTotalWidth(527);
//table.setLockedWidth(true);
table.getDefaultCell().setBorder(Rectangle.BOTTOM);
table.addCell(dbUser.getCompanyName()+"\n"+compSetup.getAddress());
table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
table.addCell(new Phrase(chunk));



} catch (BadElementException e) {
	logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf ----> onEndPage", e);
} catch (MalformedURLException e) {
	logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf----> onEndPage", e);
} catch (IOException e) {
	logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf----> onEndPage", e);
} catch (DocumentException e) {
	logger.error("ERROR in QutationHbaViewModel class HeaderFooter PDf----> onEndPage", e);
}
return table;
}*/


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
			logger.error("ERROR in QutationHbaViewModel ----> previewPdfForprintingInvoice", ex);			
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
			logger.error("ERROR in QutationHbaViewModel ----> cashInvoiceSetting", ex);			
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


	public AccountsModel getSelectedCreditinvAcount() {
		return selectedCreditinvAcount;
	}

	public void setSelectedCreditinvAcount(AccountsModel selectedCreditinvAcount) {
		this.selectedCreditinvAcount = selectedCreditinvAcount;
	}



	public TermModel getSelectedCreditInvoiceTerms() {
		return selectedCreditInvoiceTerms;
	}


	@NotifyChange({"dueDate"})
	public void setSelectedCreditInvoiceTerms(TermModel selectedCreditInvoiceTerms) {
		this.selectedCreditInvoiceTerms = selectedCreditInvoiceTerms;
		if(selectedCreditInvoiceTerms!=null)
		{
			if(selectedCreditInvoiceTerms.getNumberOfDays()!=0)
			{
				Calendar cal = Calendar.getInstance();
				cal.setTime(creationdate);
				cal.add(Calendar.DATE, selectedCreditInvoiceTerms.getNumberOfDays());
				dueDate=cal.getTime();
			}
			else
			{
				dueDate=creationdate;
			}
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

	public List<AccountsModel> getLtnCreditinvcAccount() {
		return ltnCreditinvcAccount;
	}

	public void setLtnCreditinvcAccount(List<AccountsModel> ltnCreditinvcAccount) {
		this.ltnCreditinvcAccount = ltnCreditinvcAccount;
	}

	public List<TermModel> getLstCreditInvoiceTerms() {
		return lstCreditInvoiceTerms;
	}

	public void setLstCreditInvoiceTerms(List<TermModel> lstCreditInvoiceTerms) {
		this.lstCreditInvoiceTerms = lstCreditInvoiceTerms;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCustomerCreditLimit() {
		return customerCreditLimit;
	}

	public void setCustomerCreditLimit(String customerCreditLimit) {
		this.customerCreditLimit = customerCreditLimit;
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

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public int getCreditInvoiceKey() {
		return creditInvoiceKey;
	}

	public void setCreditInvoiceKey(int creditInvoiceKey) {
		this.creditInvoiceKey = creditInvoiceKey;
	}

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==301)
			{
				companyRole=item;
				break;
			}
		}
	}

	public String getSelectedRadioButtons() {
		return selectedRadioButtons;
	}

	@NotifyChange({"disableRemindMe","disableRemindMeOn","remindMeNoOfDays","remindMeDate"})
	public void setSelectedRadioButtons(String selectedRadioButtons) {
		this.selectedRadioButtons = selectedRadioButtons;
		if(selectedRadioButtons!=null)
		{
			if(selectedRadioButtons.equalsIgnoreCase("A"))
			{
				disableRemindMe=true;
				remindMeNoOfDays=0;
				disableRemindMeOn=false;
			}
			else if(selectedRadioButtons.equalsIgnoreCase("B"))
			{
				disableRemindMeOn=true;
				disableRemindMe=false;
				Calendar c = Calendar.getInstance();		
				try {
					remindMeDate=df.parse(sdf.format(c.getTime()));
				} catch (ParseException e) {

					logger.error("ERROR in QutationHbaViewModel ----> setSelectedRadioButtons", e);			
				}
			}
			else
			{
				disableRemindMe=true;
				disableRemindMeOn=true;
				remindMeNoOfDays=0;
				Calendar c = Calendar.getInstance();		
				try {
					remindMeDate=df.parse(sdf.format(c.getTime()));
				} catch (ParseException e) {

					logger.error("ERROR in QutationHbaViewModel ----> setSelectedRadioButtons", e);			
				}
			}
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

	public List<String> getListClientType() {
		return listClientType;
	}

	public void setListClientType(List<String> listClientType) {
		this.listClientType = listClientType;
	}

	public String getSelectedClientType() {
		return selectedClientType;
	}

	@NotifyChange({"lstInvcCustomerName"})
	public void setSelectedClientType(String selectedClientType) {
		this.selectedClientType = selectedClientType;
		if(selectedClientType!=null)
		{
			if(selectedClientType.equalsIgnoreCase("Prospective Client"))
			{
				lstInvcCustomerName=data.quotationPrecpectiveList();
			}
			else
			{
				lstInvcCustomerName=data.quotationCustomerList();
			}
		}
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

	//goToRelatedReport
	@Command
	public void goToRelatedReport()
	{
		try
		{
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			Tabs contentTabs=(Tabs)tabbox.getFellow("contentTabs");
			Tabpanels contentTabpanels=(Tabpanels)tabbox.getFellow("contentTabpanels");
			Tab newTab = new Tab();
			newTab.setLabel("Quotation Report");
			newTab.setClosable(true);
			Tabpanel newTabpanel = new Tabpanel();
			Include incContentPage = new Include();
			incContentPage.setSrc("/hba/report/QuotationHbaReport.zul");
			incContentPage.setParent(newTabpanel);
			newTabpanel.setParent(contentTabpanels);
			newTab.setParent(contentTabs);
			newTab.setSelected(true);
			newTab.setVflex("1");
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceViewModel ----> goToRelatedReport", ex);			
		}
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
	public void CustomerSendEmail(@BindingParam("row") final CashInvoiceModel row) {
		if(validateData(false))
		{
			lstAtt=new ArrayList<QuotationAttachmentModel>(); 
			selectedAttchemnets.setFilename(selectedInvcCutomerName.getFullName()+" Quotation.pdf");
			selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Quotation?","Quotation", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {						
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
							if(selectedClientType.equalsIgnoreCase("Customer")){
								arg.put("formType", "Customer");
							}
							if(selectedClientType.equalsIgnoreCase("Prospective Client")){
								arg.put("formType", "Prospective");
							}
							arg.put("type", "OtherForms");
							Executions.createComponents("/crm/editCustomerFeedbackSend.zul",
									null, arg);
						} catch (Exception ex) {
							logger.error("ERROR in QuotationHbaViewModel ----> CustomerSendEmail",ex);
						}
					}
				}
			});
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



}
