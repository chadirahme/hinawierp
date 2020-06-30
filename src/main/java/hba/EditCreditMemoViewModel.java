package hba;

import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.*;
import common.FormatDateText;
import common.HbaEnum;
import common.NumberToWord;
import company.CompanyData;
import home.QuotationAttachmentModel;
import hr.HRData;
import layout.MenuData;
import layout.MenuModel;
import model.*;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import setup.users.WebusersModel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class EditCreditMemoViewModel {

    private Logger logger = Logger.getLogger(this.getClass());
    HBAData data=new HBAData();
    private Date creationdate;
    private CashInvoiceModel objCashInvoice;
    DecimalFormat formatter = new DecimalFormat("#,###.00");

    MenuData menuData=new MenuData();

    //private static String FILE = ":/temp/hello.pdf";


    //for the grid
    private CashInvoiceGridData selectedGridItems;
    private List<CashInvoiceGridData> lstCashInvoiceCheckItems;
    private List <QbListsModel> lstCashInvoiceGridItem;
    private List<QbListsModel> lstItems;
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

    //private List <TermModel> lstCreditInvoiceTerms;
    private TermModel selectedCreditInvoiceTerms;

    private String invoiceNewSaleNo;
    private String invoiceNewBillToAddress;

    private Date dueDate;

    int customerKeyOtherForms = 0;
    boolean otherformFalg = false;
    private List<QbListsModel> lstInvcCustomerSendVia;
    private QbListsModel selectedInvcCutomerSendVia;

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat dcf=new DecimalFormat("0.00");

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
    private String webUserName="";
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

    private ListModelList<WebusersModel> lstUsers;
    private WebusersModel selectedUser;

    CompanyData companyData=new CompanyData();
    private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
    private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
    private boolean createPdfSendEmail = false;
    NumberToWord numbToWord=new NumberToWord();
    private List<HRListValuesModel> countries;
    private List<HRListValuesModel> cities;
    private HRData hrData = new HRData();

    private boolean seeTrasction=false;
    private boolean showDelivery=false;
    private boolean selectD=false;
    private boolean transformD=false;
    private String printYear="";
    private PrintModel objPrint;
    private boolean changeToCreditInvoice=false;
    boolean isSkip = false;
    private boolean posItems;

    private List<VATCodeModel> lstVatCodeList;
    VATCodeModel custVendVatCodeModel;

    @SuppressWarnings("rawtypes")
    public EditCreditMemoViewModel() {
        try{
            Execution exec = Executions.getCurrent();
            Map map = exec.getArg();
            creditInvoiceKey=(Integer)map.get("creditInvoiceKey");
            String type=(String)map.get("type");
            actionTYpe=type;
            canView=(Boolean) map.get("canView");
            canModify=(Boolean) map.get("canModify");
            canPrint=(Boolean) map.get("canPrint");
            canCreate=(Boolean) map.get("canCreate");
            objPrint=new PrintModel();
            if(map.get("objPrint")!=null)
            {
                objPrint=(PrintModel) map.get("objPrint");
            }
            if(map.get("posItems") !=null)
                posItems=(Boolean) map.get("posItems");
            if(map.get("changeToCreditInvoice")!=null)
                changeToCreditInvoice=(Boolean) map.get("changeToCreditInvoice");

            Session sess = Sessions.getCurrent();
            countries = hrData.getHRListValues(2, "");
            cities = hrData.getHRListValues(3, "");
            WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
            getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
            seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();

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

            Window win = (Window) Path.getComponent("/creditInvoicePopUp");
            if(type.equalsIgnoreCase("create"))
            {
                win.setTitle("Add Credit Memo");
            }
            else if(type.equalsIgnoreCase("edit"))
            {
                win.setTitle("Edit Credit Memo Info");
            }else if (type.equalsIgnoreCase("OtherForms")) {
                win.setTitle("Add Credit Invoice Info");
                customerKeyOtherForms = (Integer) map.get("customerKey");
                otherformFalg = true;

            }
            else
            {
                win.setTitle("View Credit Memo Info");
                canModify=false;
                canCreate=false;
            }

            showFieldsPOS=false;
            fillBarCode();
            objCashInvoice=new CashInvoiceModel();


            Calendar c = Calendar.getInstance();
            printYear=new SimpleDateFormat("yyyy").format(c.getTime());
            compSetup=data.getDefaultSetUpInfoForCashInvoice();
            creationdate=df.parse(sdf.format(c.getTime()));
            lstInvcCustomerName=data.fillQbList("'Customer'");
            lstCashInvoiceGridItem=data.fillQbItemsList();
            lstItems=lstCashInvoiceGridItem;//data.fillQbItemsList(); to use for POS
            lstInvcCustomerClass=data.GetMasterData("Class");
            lstInvcCustomerSalsRep=data.GetMasterData("SalesRep");
            lstInvcCustomerTemplate=data.GetMasterData("Template");
            lstInvcCustomerSendVia=data.GetMasterData("SendVia");
            lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
            lstInvcCustomerGridClass=data.GetMasterData("GridClass");
            ltnCreditinvcAccount=data.getAccountsForCreditInvoice();
            //lstCreditInvoiceTerms=data.getTermsForCreditInvoice();
            if(compSetup.getUseVAT().equals("Y")){
                lstVatCodeList=data.fillVatCodeList();
            }

            if(creditInvoiceKey>0)
            {
                if(type.equalsIgnoreCase("view"))
                    labelStatus="View";
                else
                    labelStatus="Edit";
                CashInvoiceModel obj1 = new CashInvoiceModel();

                objCashInvoice=data.getCreditMemoByID(creditInvoiceKey);
                List<CashInvoiceGridData> invoiceModelnew=data.getCreditMemoLineGridDataByID(creditInvoiceKey);
                if(objCashInvoice!=null)
                {
//                    if(objCashInvoice.getTransformD().equalsIgnoreCase("Y")){
//                        transformD=true;
//                    }else{
//                        transformD=false;
//                    }
                    for(QbListsModel cutomerNmae:lstInvcCustomerName)
                    {
                        if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
                        {
                            selectedInvcCutomerName=cutomerNmae;
                            obj1=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());
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
                    for(AccountsModel accountsModel:ltnCreditinvcAccount)
                    {
                        if(accountsModel.getRec_No()==objCashInvoice.getAccountRefKey())
                        {
                            selectedCreditinvAcount=accountsModel;
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
                    creationdate=df.parse(sdf.format(objCashInvoice.getTxnDate()));
                   // dueDate=df.parse(sdf.format(objCashInvoice.getDueDate()));
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
//                        for(String barcodeList:barcodeValues)
//                        {
//                            if(barcodeList.equalsIgnoreCase(editInvoiceGrid.getBarcode()))
//                            {
//                                obj.setBarcode(barcodeList);
//                                break;
//                            }
//
//                        }

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

                        obj.setInvoiceDescription(editInvoiceGrid.getInvoiceDescription());
                        obj.setAvgCost(editInvoiceGrid.getAvgCost());
                        //obj.setInvoicearabicDescription(editInvoiceGrid.getInvoicearabicDescription());
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
                objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.CreditMemo.toString()));
                invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
                objCashInvoice.setRecNo(0);
                objCashInvoice.setTransformD("N");
                lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
                CashInvoiceGridData objItems=new CashInvoiceGridData();
                objItems.setLineNo(1);
                objItems.setInvoiceQty(1);
                lstCashInvoiceCheckItems.add(objItems);
                lblTotalCost="Amount :0.00";
                toatlAmount=0.0;
                tempTotalAmount=0.0;

            }
        }
        catch(Exception ex)
        {
            logger.error("ERROR in EditCreditMemoViewModel ----> onLoad", ex);
        }
    }

    public void fillBarCode()
    {

        barcodeValues=new ArrayList<String>();//data.getLstBarcodes(null);
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
                    Messagebox.show("Selected Item have sub Sub Item(s). Do you want to continue?","Credit Memo", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
                                        BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
                                        BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "toatlAmount");
                                    }
                                }

                            });
                }
                else
                {
                    Messagebox.show("Selected Item have sub Class(s). You cannot continue!","Credit Memo", Messagebox.OK , Messagebox.INFORMATION);
                    type.setSelectedItems(null);
                    type.setInvoiceDescription("");
                    type.setSelectedInvcCutomerGridInvrtySiteNew(null);
                    type.setSelectedInvcCutomerGridInvrtyClassNew(null);
                    type.setAvgCost(0);
                    type.setInvoiceRate(0);
                    type.setInvoiceAmmount(0);
                    setLabelCheckTotalcost();
                    BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
                    BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "toatlAmount");
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
        if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))

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
            type.setInvoiceRate(objItems.getInvoiceRate());
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
            toalCheckItemsAmount+=item.getAmountAfterVAT();
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
                Messagebox.show("To add new record,First select Item from the existing record!","Credit Invoice",Messagebox.OK,Messagebox.INFORMATION);
                return;
            }

        }

    }

    //need to work on these below stuff

    @Command
    public void allowSkipCommand()
    {
        isSkip=true;
    }

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
                                lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
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


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean validateData(boolean Printflag)
    {
        boolean isValid=true;

        if(invoiceNewSaleNo==null)
        {
            Messagebox.show("Please Enter the Credit Memo Number","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            return false;
        }

        if((invoiceNewSaleNo!=null) && (data.checkIfSerialNumberIsDuplicateForCreditMemo(invoiceNewSaleNo,objCashInvoice.getRecNo())==true))
        {
            Messagebox.show("Duplicate Credit Memo Number!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            return false;
        }


        if(compSetup.getClosingDate()!=null){
            if(creationdate.compareTo(compSetup.getClosingDate())<=0){
                Messagebox.show("Invoice Date must be Higher than the QuickBooks Closing Date.!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                return false;
            }
        }

        if(compSetup.getDontSaveWithOutMemo().equals("Y")){
            if(FormatDateText.isEmpty(objCashInvoice.getInvoiceMemo())){
                Messagebox.show("You must fill the transaction Memo according to company settings!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                return false;
            }
        }


        if(selectedInvcCutomerName==null || selectedInvcCutomerName.getRecNo()==0)
        {
            Messagebox.show("Select an existing Name For This Transaction!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            return false;
        }

        if(selectedCreditinvAcount==null || selectedCreditinvAcount.getRec_No()==0)
        {
            Messagebox.show("Select an existing Account For This Transaction!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            return false;
        }

        if(tempTotalAmount!=toatlAmount)
        {
            Messagebox.show("The amount entered is not matching with actual grid total.!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            toatlAmount = Double.parseDouble(BigDecimal.valueOf(tempTotalAmount).toPlainString());
            return false;
        }

        if(toatlAmount<0)
        {
            Messagebox.show("The total amount should not be less than zero!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            return false;
        }

        if(compSetup.getUseSalesRepComition().equalsIgnoreCase("Y"))
        {
            if(selectedInvcCutomerSalsRep==null)
            {
                Messagebox.show("Please select the sales rep to continue!!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                return false;
            }
        }


        if(toatlAmount==0)
        {

            if(compSetup.getSaveInvQty().equalsIgnoreCase("Y"))
            {
                Messagebox.show("There is no value of Invoice,Do you want to Continue saving","Credit Memo", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
                        new org.zkoss.zk.ui.event.EventListener() {
                            @SuppressWarnings("unused")
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
        }


        if(lstCashInvoiceCheckItems.size()<=0)
        {
            Messagebox.show("You Connot Continue.Please Add atleast one grid Item !!!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
            return false;
        }

        boolean RateValidation=false;
        boolean siteValidation=false;
        if(lstCashInvoiceCheckItems!=null)
        {
            CashInvoiceGridData lastItem=lstCashInvoiceCheckItems.get(lstCashInvoiceCheckItems.size()-1);
            if(lastItem.getSelectedItems()!=null)
            {
                if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null || lastItem.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0))
                {
                    Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                    return false;
                }
            }
            else{
                Messagebox.show("You Can't Continue.! Please select the Item from the grid","Cash Invoice",Messagebox.OK,Messagebox.INFORMATION);
                return false;
            }

            for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
            {
                CashInvoiceGridData objExp=gridData;
                if(objExp.getInvoiceRate()>0)
                {
                    RateValidation=true;
                }
                else
                {
                    RateValidation=false;
                    break;
                }
            }
            for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
            {
                CashInvoiceGridData objExp=gridData;
                if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && ((objExp.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || objExp.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (objExp.getSelectedInvcCutomerGridInvrtySiteNew()==null || objExp.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0)))
                {
                    siteValidation=true;
                }
                else
                {
                    siteValidation=false;
                    break;
                }
            }
            for(CashInvoiceGridData gridData:lstCashInvoiceCheckItems)
            {
                CashInvoiceGridData objExp=gridData;
                if(objExp.getInvoiceQty()==0)
                {
                    Messagebox.show("Please Enter The Quantity,Empty Transaction is not allowed !!!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                    return false;
                }

            }

            if(!RateValidation)
            {
                Messagebox.show("Please Enter The Rate value  the Quoation amount cannot be zero,Empty Transaction is not allowed !!!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                return false;
            }

            if(siteValidation)
            {
                Messagebox.show("First select Site Name from the existing records in the Grid!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                return false;
            }
        }




//        if(Printflag)
//        {
//            if((invoiceNewSaleNo!=null) && (data.checkIfSerialNumberIsDuplicateForCreditInvoice(invoiceNewSaleNo,objCashInvoice.getRecNo())==true))
//            {
//                Messagebox.show("Duplicate Credit Memo Number!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
//                return false;
//            }
//            if(invoiceNewSaleNo==null)
//            {
//                Messagebox.show("Please Enter the Credit Memo Number","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
//                return false;
//            }
//        }


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
            boolean hasSubAccount=data.checkIfClassHasSub(type.getSelectedInvcCutomerGridInvrtyClassNew().getName()+":");
            if(hasSubAccount)
            {
                if(compSetup.getPostOnMainClass().equals("Y"))
                {
                    Messagebox.show("Selected Class have Sub Class(s). Do you want to continue?","Credit Invoice", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
                                        BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
                                    }
                                }

                            });
                }
                else
                {
                    Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Credit Invoice", Messagebox.OK , Messagebox.INFORMATION);
                    type.setSelectedInvcCutomerGridInvrtyClassNew(null);
                    BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
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
                                        BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "selectedInvcCutomerClass");
                                    }
                                }

                            });
                }
                else

                {
                    Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
                    selectedInvcCutomerClass=null;
                    BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "selectedInvcCutomerClass");
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
                        Messagebox.show("Quantity Available only is " +type.getInvoiceQtyOnHand() + "., Do you want to continue","Credit Invoice", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
                                            BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
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
                    Messagebox.show("The number is larger than the maximum value","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
                    return;
                }

                if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem"))
                {
                    if(compSetup.getAllowSavingAvgCost().equals("N"))
                    {
                        if(Math.round((type.getAvgCost()*100)/100) > Math.round((type.getInvoiceRate())*100/100))
                        {

                            Messagebox.show("The rate is lesser than the Avg. Cost" +type.getInvoiceAmmount() + "., Do you want to continue","Credit Invoice", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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
                                                BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
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
            Messagebox.show("You Connot Continue.!Please select the Item from the grid","Credit Invoice",Messagebox.OK,Messagebox.INFORMATION);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void saveData()
    {
        try
        {
            int tmpRecNo=0;
            CashInvoiceModel obj=new CashInvoiceModel();

            if(creditInvoiceKey==0)
            {
                tmpRecNo=data.GetNewCreditMemoRecNoQuery();
            }
            else
            {
                tmpRecNo=objCashInvoice.getRecNo();
            }
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

            obj.setAccountRefKey(selectedCreditinvAcount.getRec_No());
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
            obj.setTransformD(objCashInvoice.getTransformD());
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
            if(creditInvoiceKey==0)
            {
                result=data.addNewCreditMemo(obj,webUserID);
            }else
            {
                result=data.updateCreditMemo(obj,webUserID);
            }
            if(result>0)
            {
                if(creditInvoiceKey==0)
                {
                    data.ConfigSerialNumberCashInvoice(SerialFields.CreditMemo, invoiceNewSaleNo,0);
                }
                data.deleteCreditMemoGridItems(tmpRecNo);
                for (CashInvoiceGridData item : lstCashInvoiceCheckItems)
                {
                    if(item.getSelectedItems()!=null)
                    {
                        data.addNewCreditMemoLineGridItems(item, tmpRecNo);
                    }
                }

                //After Saving in to 4 Tables above Please save to QBVATTransaction
                if(compSetup.getUseVAT().equals("Y"))
                    data.CreateCashInvoice4QBVAT(tmpRecNo, HbaEnum.VatForms.CreditMemo.getValue() , selectedInvcCutomerName.getRecNo() , selectedInvcCutomerName.getListType(),toatlAmount);

                if(creditInvoiceKey==0)
                {
                    data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.CreditMemo.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getInvoiceSaleNo(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
                }else
                {
                    data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.CreditMemo.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getInvoiceSaleNo(), obj.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue());
                }
//                if (selectedInvcCutomerName.getRecNo() > 0) {
//                    Calendar c = Calendar.getInstance();
//                    CustomerStatusHistoryModel model = new CustomerStatusHistoryModel();
//                    model.setRecNo(data.getMaxID("CustomerStatusHistory","RecNo"));
//                    model.setCustKey(selectedInvcCutomerName.getRecNo());
//                    model.setActionDate(df.parse(sdf.format(c.getTime())));
//                    model.setCreatedFrom("Credit Invoice");
//                    model.setStatusDescription(objCashInvoice.getDescription());
//                    model.setType("C");
//                    model.setTxnRecNo(0);
//                    model.setTxnRefNumber(invoiceNewSaleNo);
//                    data.saveCustomerStatusHistroyfromFeedback(model,webUserID,webUserName);
//                }
            }
            if(creditInvoiceKey>0)
            {
                Clients.showNotification("The  Credit Memo  Has Been Updated Successfully.", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
                Map args = new HashMap();
                BindUtils.postGlobalCommand(null, null, "refreshParentCreditInvoice", args);
            }
            else
            {
                Clients.showNotification("The Credit Memo Has Been Created Successfully.", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
                Map args = new HashMap();
                BindUtils.postGlobalCommand(null, null, "refreshParentCreditInvoice", args);
            }

        }catch (Exception ex)
        {
            Messagebox.show(ex.getMessage());
        }
    }




    @Command
    @NotifyChange({"invoiceNewSaleNo","objCashInvoice","creationdate","customerCreditLimit","dueDate","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedCreditInvoiceTerms","selectedInvcCutomerSalsRep","selectedCreditinvAcount","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage","transformD"})
    public void navigationCreditInvoice(@BindingParam("cmp") String navigation)
    {
        try
        {
            objCashInvoice=data.navigationCreditMemo(creditInvoiceKey,webUserID,seeTrasction,navigation,actionTYpe);
            if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
            {
                actionTYpe="edit";
                labelStatus="Edit";
                CashInvoiceModel obj1 = new CashInvoiceModel();
                creditInvoiceKey=objCashInvoice.getRecNo();
                List<CashInvoiceGridData> invoiceModelnew=data.getCreditMemoLineGridDataByID(creditInvoiceKey);
                for(QbListsModel cutomerNmae:lstInvcCustomerName)
                {
                    if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
                    {
                        selectedInvcCutomerName=cutomerNmae;
                        obj1=data.getCashInvoiceCusomerInfo(selectedInvcCutomerName.getListType(), selectedInvcCutomerName.getRecNo());
                        objCashInvoice.setTotalBalance(obj1.getTotalBalance());
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
                for(AccountsModel accountsModel:ltnCreditinvcAccount)
                {
                    if(accountsModel.getRec_No()==objCashInvoice.getAccountRefKey())
                    {
                        selectedCreditinvAcount=accountsModel;
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
                creationdate=df.parse(sdf.format(objCashInvoice.getTxnDate()));
                invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"";
                lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
                for(CashInvoiceGridData editInvoiceGrid:invoiceModelnew)
                {
                    CashInvoiceGridData obj=new CashInvoiceGridData();
                    obj.setLineNo(lstCashInvoiceCheckItems.size()+1);
                    obj.setHideSite(false);

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
                objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.CreditMemo.toString()));
                invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
                objCashInvoice.setRecNo(0);
                objCashInvoice.setTransformD("N");
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
                selectedCreditinvAcount=null;
                selectedInvcCutomerSendVia=null;
                selectedCreditInvoiceTerms=null;
                invoiceNewBillToAddress="";

            }



        }
        catch (Exception ex)
        {
            logger.error("ERROR in EditCreditMemoViewModel ----> navigationCreditInvoice", ex);
        }
    }


    @Command
    @NotifyChange({"invoiceNewSaleNo","objCashInvoice","creationdate","customerCreditLimit","dueDate","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedCreditInvoiceTerms","selectedInvcCutomerSalsRep","selectedCreditinvAcount","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
    public void copyFunctinality()
    {
        try
        {
            if(creditInvoiceKey>0)
            {
                objCashInvoice=data.getCreditMemoByID(creditInvoiceKey);
                if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
                {
                    actionTYpe="edit";
                    labelStatus="Copied-Create";
                    creditInvoiceKey=objCashInvoice.getRecNo();
                    List<CashInvoiceGridData> invoiceModelnew=data.getCreditMemoLineGridDataByID(creditInvoiceKey);
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

                    for(AccountsModel accountsModel:ltnCreditinvcAccount)
                    {
                        if(accountsModel.getRec_No()==objCashInvoice.getAccountRefKey())
                        {
                            selectedCreditinvAcount=accountsModel;
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
                    Calendar c = Calendar.getInstance();
                    creationdate=df.parse(sdf.format(c.getTime()));
                    objCashInvoice.setInvoiceSaleNo(data.GetSaleNumber(SerialFields.CreditMemo.toString()));
                    invoiceNewSaleNo=objCashInvoice.getInvoiceSaleNo();
                    objCashInvoice.setRecNo(0);
                    invoiceNewBillToAddress=objCashInvoice.getBillAddress1()+"\n"+objCashInvoice.getBillAddress2()+"\n"+objCashInvoice.getBillAddress3()+"\n"+objCashInvoice.getBillAddress4()+"\n"+objCashInvoice.getBillAddress5()+"";
                    lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
                    for(CashInvoiceGridData editInvoiceGrid:invoiceModelnew)
                    {
                        CashInvoiceGridData obj=new CashInvoiceGridData();
                        obj.setLineNo(lstCashInvoiceCheckItems.size()+1);
                        obj.setHideSite(false);

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
                Messagebox.show("You can only copy a existing Credit Invoice","Credit Memo", Messagebox.OK , Messagebox.INFORMATION);
                return;
            }


        }
        catch (Exception ex)
        {
            logger.error("ERROR in EditCreditMemoViewModel ----> copyFunctinality", ex);
        }
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @GlobalCommand
    @NotifyChange({"lstCashInvoiceCheckItems","lblTotalCost","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
    public void refreshItemsParent(@BindingParam("refreshCommand") String refreshCommand,@BindingParam("selectedItem")QbListsModel selectedItem,@BindingParam("selectedRow")CashInvoiceGridData selectedRow)
    {
        try
        {
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
                                            BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
                                            BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "toatlAmount");
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
                        BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "lstCashInvoiceCheckItems");
                        BindUtils.postNotifyChange(null, null, EditCreditMemoViewModel.this, "toatlAmount");
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
        catch(Exception ex)
        {
            logger.error("ERROR in EditCreditMemoViewModel ----> refreshItemsParent", ex);
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
            customerCreditLimit=obj.getCreditLimit();


            //VAT
            if (compSetup.getUseVAT().equals("Y")) {
                if (obj.getVatKey() > 0) {
                    custVendVatCodeModel = lstVatCodeList.stream().filter(x -> x.getVatKey() == obj.getVatKey()).findFirst().orElse(null);
                    if (custVendVatCodeModel != null) {
                        if (lstCashInvoiceCheckItems.size() > 0) {
                            Messagebox.show("There is VAT Code assigned for this Customer" +
                                    ". System will recalculate the VAT Amount based on the Customer.", "Credit Memo", Messagebox.OK, Messagebox.INFORMATION);

                            //for Items
                            VATCodeOperation.recalculateCashInvoiceItemsVAT(lstCashInvoiceCheckItems, custVendVatCodeModel, compSetup);
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
                }
            }
            setLabelCheckTotalcost();
        }
        else
        {
            Messagebox.show("Invalid Customer Name !!","Credit Memo",Messagebox.OK,Messagebox.INFORMATION);
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
                                BindUtils.postNotifyChange(null,null,EditCreditMemoViewModel.this,"selectedInvcCutomerClass");
                            }
                        }

                    });
                } else

                {
                    Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK,Messagebox.INFORMATION);
                    setSelectedInvcCutomerClass(lstInvcCustomerClass.get(0));
                }
            }
        }

        else
        {
            Messagebox.show("Invalid Class Name !!","Credit Invoice",Messagebox.OK,Messagebox.INFORMATION);
            setSelectedInvcCutomerClass(lstInvcCustomerClass.get(0));
        }
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

                EditCreditMemoViewModel.HeaderFooter event = new EditCreditMemoViewModel.HeaderFooter();
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

                int y_line1 = 650;
                int y_line2 = y_line1 - 50;
                int y_line3 = y_line2 - 50;

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PdfWriter.getInstance(document, baos);

                document.open();
                document.newPage();

                Paragraph paragraph = new Paragraph();
                paragraph.setSpacingAfter(10);
                document.add(paragraph);
                paragraph.setAlignment(Element.ALIGN_LEFT);

                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);

                PdfPTable firsttbl = new PdfPTable(2);
                firsttbl.setWidthPercentage(100);
                firsttbl.getDefaultCell().setBorder(0);
                firsttbl.setWidths(new int[] { 200, 100 });
                Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
                        BaseColor.RED);
                Chunk c = new Chunk("Credit Invoice");
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

                cell1 = new PdfPCell(new Phrase(invoiceNewBillToAddress));
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.disableBorderSide(Rectangle.BOX);
                tbl1.addCell(cell1);
                document.add(tbl1);

				/*---------------------------------------------------------------*/



                BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
                PdfPTable table = new PdfPTable(2);
                objPrint.setAmountWidth(0);
                if(!objPrint.isHideAmount())
                    objPrint.setAmountWidth(60);
                table.setWidths(new int[] {400, objPrint.getAmountWidth() });
                table.setSpacingBefore(20);
                table.setWidthPercentage(100);
                //table.setWidths(new int[] {400, 60 });
                table.getDefaultCell().setPadding(5);

				/*PdfPCell HeadderProduct = new PdfPCell(new Phrase("Product/Service"));
				HeadderProduct.setPadding(1);
				HeadderProduct.setColspan(1);
				HeadderProduct.setBorder(Rectangle.NO_BORDER);
				HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);

				HeadderProduct.setBackgroundColor(myColor);
				HeadderProduct.setBorderWidth(120.0f);
				table.addCell(HeadderProduct);
				table.setHeaderRows(1);*/

                PdfPCell HeadderDate = new PdfPCell(new Phrase("Description"));
                HeadderDate.setPadding(1);
                HeadderDate.setColspan(1);
                HeadderDate.setBorder(Rectangle.NO_BORDER);
                HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
                HeadderDate.setBackgroundColor(myColor);
                table.addCell(HeadderDate);

				/*PdfPCell HeadderQty = new PdfPCell(new Phrase("QTY"));
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
				table.addCell(HeadderRate);*/

                PdfPCell HeadderAmount = new PdfPCell(new Phrase("Amount"));
                HeadderAmount.setPadding(1);
                HeadderAmount.setColspan(1);
                HeadderAmount.setBorder(Rectangle.NO_BORDER);
                HeadderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
                HeadderAmount.setBackgroundColor(myColor);
                HeadderAmount.setBorderWidth(40.0f);
                table.addCell(HeadderAmount);
                boolean desc=true;

                for (CashInvoiceGridData item : lstCashInvoiceCheckItems)
                {

					/*table.addCell(item.getSelectedItems().getName());*/
                    if(item.getInvoiceDescription()!=null && !item.getInvoiceDescription().equalsIgnoreCase(""))
                    {
                        table.addCell(item.getInvoiceDescription());
                    }else{
                        table.addCell("");
                    }
					/*table.addCell(""+item.getInvoiceQty());
					table.addCell(""+item.getInvoiceRate());*/

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
                        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
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
                logger.error("ERROR in EditCreditMemoViewModel ----> createPdfForPrinting", ex);
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
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(compSetup.getCompanyName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22)), rect.getLeft(), rect.getTop(), 0);
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("Phone: " + compSetup.getPhone1()+ "   Fax: " + compSetup.getFax()),rect.getLeft(), rect.getTop() - 15, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format(compSetup.getAddress())),(rect.getLeft()), rect.getTop() - 30, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(getCityName(compSetup.getCitykey()) + " - "+ getCountryName(compSetup.getCountrykey())),rect.getLeft(), rect.getTop() - 45, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase("______________________________________________________________________________"),rect.getLeft(), rect.getTop() - 50, 0);
                Calendar now = Calendar.getInstance();
                if (createPdfSendEmail){
                    ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("This Document Does Not Require Signature")),rect.getLeft(), rect.getBottom() - 15, 0);
                }
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Date :"+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(now.getTime()))),(rect.getRight()), rect.getBottom() - 30, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT,new Phrase(String.format("Printed by :"+ selectedUser.getFirstname())),(rect.getRight()), rect.getBottom() - 15, 0);
                ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT,new Phrase(String.format("Powered by www.hinawi.com")),	rect.getLeft(), rect.getBottom() - 30, 0);

            } catch (BadElementException e) {
                logger.error(
                        "ERROR in EditCreditMemoViewModel class HeaderFooter PDf ----> onEndPage",
                        e);
            } catch (MalformedURLException e) {
                logger.error(
                        "ERROR in EditCreditMemoViewModel class HeaderFooter PDf----> onEndPage",
                        e);
            } catch (IOException e) {
                logger.error(
                        "ERROR in EditCreditMemoViewModel class HeaderFooter PDf----> onEndPage",
                        e);
            } catch (DocumentException e) {
                logger.error(
                        "ERROR in EditCreditMemoViewModel class HeaderFooter PDf----> onEndPage",
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
            logger.error("ERROR in EditCreditMemoViewModel ----> previewPdfForprintingInvoice", ex);
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
            logger.error("ERROR in EditCreditMemoViewModel ----> cashInvoiceSetting", ex);
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @NotifyChange({ "selectedCreditinvAcount" })
    public void setSelectedCreditinvAcount(AccountsModel selectedCreditinvAcount) {
        this.selectedCreditinvAcount = selectedCreditinvAcount;
        if (selectedCreditinvAcount!=null) {
            if (selectedCreditinvAcount.getRec_No() == 0)
                return;

            boolean hasSubAccount = data.checkIfBankAccountsHasSub(selectedCreditinvAcount.getFullName() + ":");
            if (hasSubAccount) {
                if (compSetup.getPostOnMainClass().equals("Y")) {
                    Messagebox.show("Selected Account have Sub Account(s). Do you want to continue?","Credit Invoice", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
                        public void onEvent(Event evt) throws InterruptedException {
                            if (evt.getName().equals("onYes")) {
                            } else {
                                setSelectedCreditinvAcount(ltnCreditinvcAccount.get(0));
                                BindUtils.postNotifyChange(null,null,EditCreditMemoViewModel.this,"selectedCreditinvAcount");
                            }
                        }

                    });
                } else

                {
                    Messagebox.show("Selected Account have sub Account(s). You cannot continue!","Credit Invoice", Messagebox.OK,Messagebox.INFORMATION);
                    setSelectedCreditinvAcount(ltnCreditinvcAccount.get(0));
                }
            }
        }

        else
        {
            Messagebox.show("Invalid Account Name !!","Credit Invoice",Messagebox.OK,Messagebox.INFORMATION);
            setSelectedCreditinvAcount(ltnCreditinvcAccount.get(0));
        }

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


    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Command
    @NotifyChange({ "createPdfSendEmail" })
    public void CustomerSendEmail(@BindingParam("row") final CashInvoiceModel row) {
        if (validateData(false)) {
            lstAtt = new ArrayList<QuotationAttachmentModel>();
            selectedAttchemnets.setFilename(selectedInvcCutomerName.getFullName()+ " Credit Invoice.pdf");
            selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
            lstAtt.add(selectedAttchemnets);
            Messagebox.show("Do you want to Preview The Credit Invoice?",	"Credit Invoice", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
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
                            logger.error("ERROR in EditCreditMemoViewModel ----> CustomerSendEmail",ex);
                        }
                    }
                }
            });
        }
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


    private void getCompanyRolePermessions(int companyRoleId,int parentId)
    {
        companyRole=new MenuModel();

        List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
        for (MenuModel item : lstRoles)
        {
            if(item.getMenuid()==357)
            {
                companyRole=item;
                break;
            }
        }
    }

    @Command
    public void selectDelivery(){
        try {
            if(labelStatus.equalsIgnoreCase("Create")){
                Map<String, Object> arg = new HashMap<String, Object>();
                arg.put("customerKey",selectedInvcCutomerName.getRecNo());
                arg.put("invoiceType","credit");
                Executions.createComponents("/hba/payments/selectDelivery.zul", null, arg);
            }

        } catch (Exception ex) {
            logger.error("ERROR in EditDelivery ----> selectQuotation",ex);
        }
    }

    @GlobalCommand
    @NotifyChange({"lstCashInvoiceCheckItems","totalAmount","objCashInvoice","selectD","showDelivery"})
    public void getlstSelectedDeliveryCredit(@BindingParam("lstSelectedDelivery") List<DeliveryModel> selectedDelivery)
    {
        try
        {
            lstCashInvoiceCheckItems=new ArrayList<CashInvoiceGridData>();
            for(DeliveryModel deliveryLine:selectedDelivery)
            {
                if(deliveryLine.getLevel().equalsIgnoreCase("S")){
                    CashInvoiceGridData obj=new CashInvoiceGridData();
                    obj.setLineNo(lstCashInvoiceCheckItems.size()+1);
                    for(QbListsModel gridItem:lstCashInvoiceGridItem)
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
            logger.error("ERROR in EditCashInvoice ----> getlstSelectedDeliveryCredit", ex);
        }
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

    public MenuData getMenuData() {
        return menuData;
    }

    public void setMenuData(MenuData menuData) {
        this.menuData = menuData;
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

    public CompanyData getCompanyData() {
        return companyData;
    }

    public void setCompanyData(CompanyData companyData) {
        this.companyData = companyData;
    }

    public boolean isSeeTrasction() {
        return seeTrasction;
    }

    public void setSeeTrasction(boolean seeTrasction) {
        this.seeTrasction = seeTrasction;
    }

    public boolean isShowDelivery() {
        return showDelivery;
    }

    public void setShowDelivery(boolean showDelivery) {
        this.showDelivery = showDelivery;
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

    public boolean isPosItems() {
        return posItems;
    }

    public void setPosItems(boolean posItems) {
        this.posItems = posItems;
    }

    public List<QbListsModel> getLstItems() {
        return lstItems;
    }

    public void setLstItems(List<QbListsModel> lstItems) {
        this.lstItems = lstItems;
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
