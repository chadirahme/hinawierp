package hba;

import home.QuotationAttachmentModel;
import hr.HRData;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import model.AccountsModel;
import model.CheckFAItemsModel;
import model.CheckItemsModel;
import model.ClassModel;
import model.CompSetupModel;
import model.CreditBillModel;
import model.DepreciationModel;
import model.ExpensesModel;
import model.FixedAssetModel;
import model.HRListValuesModel;
import model.PayToOrderModel;
import model.QbListsModel;
import model.SerialFields;
import model.TermModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
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
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
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

public class CreditBillViewModel 
{
	private Logger logger = Logger.getLogger(CreditBillViewModel.class);
	private Date creationdate; 

	private List<AccountsModel> lstaccounts;
	private AccountsModel selectedAccount;

	private List<QbListsModel> lstPayToOrder;
	private QbListsModel selectedPaytoOrder;

	private List<AccountsModel> lstGridBankAccounts;
	private AccountsModel selectedGridBankAccount;

	private List <QbListsModel> lstGridCustomer;
	private  List<ClassModel> lstGridClass;
	private List<FixedAssetModel> lstGridFixedAssetItems;

	DecimalFormat formatter = new DecimalFormat("#,###.00");


	private CreditBillModel objCheque;

	private boolean showBankAccount=false;

	private boolean chkTobePrinted=true;

	private String billNo="";

	private Date billDueDate;


	private List<ExpensesModel> lstExpenses;
	private ExpensesModel selectedExpenses;


	//CheckItems
	private List<CheckItemsModel> lstCheckItems;
	private CheckItemsModel selectedCheckItems;
	private List <QbListsModel> lstGridQBItems;

	//Fixed Asset Items
	private List<CheckFAItemsModel> lstCheckFAItems;
	private CheckFAItemsModel selectedCheckFAItems;
	private List<FixedAssetModel> lstVendorFAItems;
	private List <QbListsModel> lstGridCustody;	
	private List <QbListsModel> lstInvcCustomerGridInvrtySite;

	int tmpUnitKey=0;
	private double totalAmount;
	private double totalExpenses;
	private String lblExpenses;
	private String lblCheckItems;
	private String lblCheckFAItems;

	private CompSetupModel compSetup;
	HBAData data=new HBAData();

	BillData billData=new BillData();

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");


	private List <TermModel> lstCreditBillTerms;
	private TermModel selectedCreditBillTerms;


	private String msgToBeDispalyedOnInvoice="";

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	private boolean adminUser;

	private int  editBillKey;

	private MenuModel companyRole;

	List<MenuModel> list;

	String actionTYpe;

	private boolean canView=false;

	private boolean canModify=false;

	private boolean canPrint=false;

	private boolean canCreate=false;

	private String labelStatus="";

	MenuData menuData=new MenuData();
	CompanyData companyData=new CompanyData();
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private HRData hrData = new HRData();
	private String webUserName="";

	NumberToWord numbToWord = new NumberToWord();
	private int webUserID=0;
	private boolean seeTrasction=false;

	@SuppressWarnings({ "rawtypes", "unused" })
	public CreditBillViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			editBillKey=0;
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
			
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");

				if (adminUser) {
					webUserID = 0;
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

			labelStatus="Create";
			Calendar c = Calendar.getInstance();

			creationdate=df.parse(sdf.format(c.getTime()));//2012-03-31

			lstaccounts=data.fillBankAccounts("'AccountsPayable'");
			lstPayToOrder=data.fillQbList("'Vendor'");
			lstCreditBillTerms=data.getTermsForCreditInvoice();
			compSetup=data.GetDefaultSetupInfo();

			lstGridBankAccounts=data.fillAccountsQueryNotIn("'AccountsPayable','AccountsRecievable'");
			lstGridCustomer=data.fillQbList("'Customer'");
			lstGridClass=data.fillClassList("");
			lstGridFixedAssetItems=data.getFixedAssetItems();
			lstGridCustody=data.fillQbList("'Employee'");
			lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
			setLstGridQBItems(billData.fillQbItemsList());
			objCheque=new CreditBillModel();
			objCheque.setTimeCreated(df.parse(sdf.format(c.getTime())));
			billDueDate=creationdate;
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			ClearData();

			lstExpenses=new ArrayList<ExpensesModel>();
			ExpensesModel objExp=new ExpensesModel();
			objExp.setSrNO(1);
			lstExpenses.add(objExp);			
			lblExpenses="Expenses 0.00";

			lstCheckItems=new ArrayList<CheckItemsModel>();
			CheckItemsModel objItems=new CheckItemsModel();
			objItems.setLineNo(1);
			objItems.setQuantity(1);
			lstCheckItems.add(objItems);
			lblCheckItems="Items 0.00";

			lstCheckFAItems=new ArrayList<CheckFAItemsModel>();
			CheckFAItemsModel objFAItems=new CheckFAItemsModel();
			objFAItems.setLineNo(1);
			objFAItems.setQuantity(1);
			lstCheckFAItems.add(objFAItems);
			lblCheckFAItems="Fixed Assets Items 0.00";


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CreditBillViewModel ----> init", ex);			
		}
	}

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==29)
			{
				companyRole=item;
				break;
			}
		}
	}

	public Validator getFormValidator()
	{
		return new AbstractValidator() 
		{
			public void validate(ValidationContext ctx) 
			{
				String enprogramname = (String)ctx.getProperties("enprogramname")[0].getValue();
				if (enprogramname == null || enprogramname.isEmpty()) 
				{
					// put error message into validationMessages map
					addInvalidMessage(ctx, "enprogramnameContentError", "English program name is required ");
				}
			}

		};
	}


	private void ClearData()
	{
		if(compSetup.getPvSerialNos().equals("S"))
		{
			objCheque.setRefNumber(data.GetSaleNumber(SerialFields.Bill.toString()));
		}
	}

	@Command    
	@NotifyChange({"totalAmount","lblExpenses","lstExpenses"})
	public void deleteExpense(@BindingParam("row") ExpensesModel row)
	{
		if(selectedExpenses!=null)
		{
			//Messagebox.show(String.valueOf(selectedExpenses.getSrNO()));
			lstExpenses.remove(selectedExpenses);

			int srNo=0;
			for (ExpensesModel item : lstExpenses)
			{
				srNo++;
				item.setSrNO(srNo);
			}

		}
		if(lstExpenses.size()==0)
		{
			ExpensesModel objExp=new ExpensesModel();
			objExp.setSrNO(lstExpenses.size()+1);
			lstExpenses.add(objExp);
		}
		double ExpAmount=0;
		for (ExpensesModel item : lstExpenses) 
		{
			ExpAmount+=item.getAmount();
		}

		lblExpenses="Expenses " + String.valueOf(ExpAmount);
		getNewTotalAmount();
	}

	@Command
	@NotifyChange({"lstExpenses"})
	public void insertExpense(@BindingParam("row") ExpensesModel row)
	{
		if(selectedExpenses!=null)
		{
			ExpensesModel lastItem=lstExpenses.get(lstExpenses.size()-1);
			if(lastItem.getSelectedAccount()==null || lastItem.getSelectedAccount().getRec_No()==0)
			{
				Messagebox.show("To add new record,First select Account from the existing record!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			}
			else
			{
				ExpensesModel objExp=new ExpensesModel();
				objExp.setSrNO(lstExpenses.size()+1);
				lstExpenses.add(objExp);
			}

		}

	}

	@Command
	@NotifyChange({"lstExpenses"})
	public void addNewExpenses() 
	{	      
		ExpensesModel objExp=new ExpensesModel();
		objExp.setSrNO(lstExpenses.size()+1);
		lstExpenses.add(objExp);
	}

	@Command   
	@NotifyChange({"lstCheckItems","lblCheckItems","totalAmount"})
	public void deleteCheckItems(@BindingParam("row") CheckItemsModel row)
	{
		if(selectedCheckItems!=null)
		{
			//Messagebox.show(String.valueOf(selectedExpenses.getSrNO()));
			lstCheckItems.remove(selectedCheckItems);

			int srNo=0;
			for (CheckItemsModel item : lstCheckItems)
			{
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if(lstCheckItems.size()==0)
		{
			CheckItemsModel objItems=new CheckItemsModel();
			objItems.setLineNo(lstCheckItems.size()+1);
			objItems.setQuantity(1);
			lstCheckItems.add(objItems);
		}
		setLabelCheckItems();
		getNewTotalAmount();
	}

	@Command
	@NotifyChange({"lstCheckItems"})
	public void insertCheckItems(@BindingParam("row") CheckItemsModel row)
	{
		if(lstCheckItems!=null)
		{
			CheckItemsModel lastItem=lstCheckItems.get(lstCheckItems.size()-1);
			if(lastItem.getSelectedItems()==null || lastItem.getSelectedItems().getRecNo()==0)
			{
				Messagebox.show("To add new record,First select Item from the existing record!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			}
			else
			{
				CheckItemsModel objExp=new CheckItemsModel();
				objExp.setLineNo(lstCheckItems.size()+1);
				lstCheckItems.add(objExp);
			}
		}

	}

	@Command
	@NotifyChange({"lstCheckItems"})
	public void addNewCheckItems() 
	{	      
		CheckItemsModel objItems=new CheckItemsModel();
		objItems.setLineNo(lstCheckItems.size()+1);
		objItems.setQuantity(1);
		lstCheckItems.add(objItems);
	}

	@Command
	@NotifyChange({"lstCheckFAItems"})
	public void addNewCheckFAItems()
	{
		CheckFAItemsModel obj=new CheckFAItemsModel();
		obj.setLineNo(lstCheckFAItems.size()+1);
		obj.setQuantity(1);
		lstCheckFAItems.add(obj);
	}

	@Command    
	@NotifyChange({"lstCheckFAItems","lblCheckFAItems","totalAmount"})
	public void deleteFAItems(@BindingParam("row") CheckFAItemsModel row)
	{
		if(selectedCheckFAItems!=null)
		{
			//Messagebox.show(String.valueOf(selectedExpenses.getSrNO()));
			lstCheckFAItems.remove(selectedCheckFAItems);

			int srNo=0;
			for (CheckFAItemsModel item : lstCheckFAItems)
			{
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if(lstCheckFAItems.size()==0)
		{
			CheckFAItemsModel obj=new CheckFAItemsModel();
			obj.setLineNo(lstCheckFAItems.size()+1);
			obj.setQuantity(1);
			lstCheckFAItems.add(obj);
		}

		double  toalCheckFAItemsAmount=0;
		for (CheckFAItemsModel item : lstCheckFAItems) 
		{
			toalCheckFAItemsAmount+=item.getAmount();
		}
		//totalAmount=ExpAmount;

		lblCheckFAItems="Fixed Assets Items " + String.valueOf(toalCheckFAItemsAmount);
		getNewTotalAmount();


	}
	@Command
	@NotifyChange({"lstCheckFAItems"})
	public void insertFAItems(@BindingParam("row") CheckFAItemsModel row)
	{
		if(selectedCheckFAItems!=null)
		{
			CheckFAItemsModel lastItem=lstCheckFAItems.get(lstCheckFAItems.size()-1);
			if(lastItem.getSelectedFixedAsset()==null || lastItem.getSelectedFixedAsset().getAssetid()==0)
			{
				Messagebox.show("To add new record,First select FA Item from the existing record!","Bill Fixed Asset Grid",Messagebox.OK,Messagebox.INFORMATION);
			}
			else
			{
				CheckFAItemsModel objExp=new CheckFAItemsModel();
				objExp.setLineNo(lstCheckFAItems.size()+1);
				lstCheckFAItems.add(objExp);
			}
		}

	}



	@Command  
	public void addNewChequePayment()
	{
		if(validateData(true))
		{
			saveData();
		}
	}


	private void saveData() 
	{	      
		try
		{			

			if(true)//validateData())
			{			   								
				int tmpRecNo=0;
				if(editBillKey==0)
				{
					tmpRecNo=billData.GetBillMastRecNoQuery();	
				}
				else
				{
					tmpRecNo=objCheque.getRecNo();
				}
				CreditBillModel obj=new CreditBillModel();
				obj.setRecNo(tmpRecNo);
				obj.setTxtnId("Local-"+tmpRecNo);
				obj.setCr_flag("C");
				obj.setAmount(totalAmount);
				obj.setMemo(objCheque.getMemo());
				obj.setBillSource("CMS");
				obj.setStatus("A");
				obj.setBillPaid("N");
				obj.setIsPaid("N");
				obj.setAddress(objCheque.getAddress());
				obj.setQbRefNUmber("R");
				obj.setTxnDate(creationdate);
				obj.setTimeCreated(creationdate);
				obj.setDueDate(billDueDate);
				obj.setAmount(totalAmount);
				obj.setAmountDue(0);
				obj.setBillNo(billNo);
				obj.setRefNumber(objCheque.getRefNumber());
				obj.setAllocated("N");
				obj.setAllocationMethod("");
				obj.setAllocatedType("");
				obj.setAllocationAmount(0.0);
				obj.setAssetInsRecNo(0);
				if(selectedAccount!=null)
				{
					obj.setApAccountRefKey(selectedAccount.getRec_No());
				}
				else
				{
					obj.setApAccountRefKey(0);
				}

				if (selectedCreditBillTerms!=null)
				{
					obj.setTermsRefKey(selectedCreditBillTerms.getTermKey());
				}
				else
				{
					obj.setTermsRefKey(0);
				}

				if (selectedPaytoOrder!=null)
				{
					obj.setVendRefKey(selectedPaytoOrder.getRecNo());
				}
				else
				{
					obj.setVendRefKey(0);
				}
				int result=0;

				if(editBillKey==0)
				{
					result=billData.addNewBill(obj,webUserID);
				}
				else
				{
					//billData.deleteBill(obj.getRecNo());
					result=billData.updateBill(obj,webUserID);
				}

				if(result>0)
				{
					//if(compSetup.getPvSerialNos().equals("S"))
					//{
					data.ConfigSerialNumberCashInvoice(SerialFields.Bill, objCheque.getRefNumber(),0);
					//}

					//add Expenses
					billData.deleteBillExpense(tmpRecNo);
					for (ExpensesModel item : lstExpenses) 
					{
						if(item.isBillableChked())
						{
							item.setBillable("Y");
						}
						else
						{
							item.setBillable("N");
						}
						if(item.getSelectedAccount()!=null)
							billData.addBillExpense(item, tmpRecNo);
					}

					//add CheckItems
					billData.deleteBillCheckItems(tmpRecNo);
					for (CheckItemsModel item : lstCheckItems) 
					{
						if(item.isBillableChked())
						{
							item.setBillable("Y");
						}
						else
						{
							item.setBillable("N");
						}
						if(item.getSelectedItems()!=null)
							billData.addBillCheckItems(item, tmpRecNo);	
					}

					//add CheckFAItems
					billData.deleteBillCheckFAItems(tmpRecNo);
					for (CheckFAItemsModel item : lstCheckFAItems) 
					{
						if(item.getSelectedFixedAsset()!=null)
						{
							billData.addBillCheckFAItems(item, tmpRecNo);
							billData.updateAssetMaster(item);
							//billData.DeleteDepreciation(item.getSelectedFixedAsset().getAssetid());
							//update Depreciation
							//updateFixedAssetItemDepreciation(item.getSelectedFixedAsset().getAssetid(), item.getSelectedCustomer().getRecNo());
						}
					}
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.BillCreditCreate.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getRefNumber(), billDueDate,  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());

					if(editBillKey==0)
					{
						Clients.showNotification("The  Bill  Has Been Created Successfully.",
								Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
						Center center = bl.getCenter();
						Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
						tabbox.getSelectedPanel().getLastChild().invalidate();
					}
					else
					{

						Clients.showNotification("The  Bill  Has Been Updated Successfully.",
								Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
						Center center = bl.getCenter();
						Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
						tabbox.getSelectedPanel().getLastChild().invalidate();

					}


				}

			}

		}
		catch (Exception ex) 
		{
			logger.error("ERROR in CreditBillViewModel ----> saveData", ex);
		}
	}

	@SuppressWarnings("unused")
	private void updateFixedAssetItemDepreciation(int assetID,int locationID) 
	{	
		FixedAssetModel obj=data.getFixedAssetItemData(assetID);
		int DiffMonths=data.Y_M_D_Diff(obj.getServiceDate(), obj.getLifeExpiryDate());

		logger.info("DiffMonths>>> " + DiffMonths);

		double CB=0;
		int LifeYears=0;
		int LifeMonths=0;
		CB=DiffMonths;
		Calendar date2 = Calendar.getInstance();
		try {
			date2.setTime(df.parse(sdf.format(obj.getLifeExpiryDate())));
			double dayinMonth = 0;
			dayinMonth=date2.get(Calendar.DAY_OF_MONTH);

			// logger.info("day in month>> " + dayinMonth);

			if(dayinMonth<30)
			{
				CB += dayinMonth / 30;
			}
			LifeMonths=DiffMonths;
			//long lprice=Long.parseLong(String.valueOf(obj.getPrice()));

			double NetBookValue=obj.getPrice();
			//logger.info("NetBookValue>> " + NetBookValue);
			double MonthDepn  = NetBookValue / CB;
			//logger.info("MonthDepn>> " + MonthDepn);
			MonthDepn=dcf.parse(dcf.format(MonthDepn)).doubleValue();
			logger.info("NetBookValue>> " + NetBookValue);
			logger.info("MonthDepn>> " + MonthDepn);

			List<DepreciationModel> lstDep=data.CalculateDepreciation(obj.getServiceDate(), obj.getPrice(), obj.getOpeningBalance(), 
					LifeYears, LifeMonths, obj.getLifeExpiryDate(), MonthDepn);

			billData.InsertDepreciation(assetID, locationID, lstDep);

			// Messagebox.show(String.valueOf(lstDep.size()));


		}
		catch (Exception ex) 
		{
			StringWriter sw = null;
			sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));	    				
			logger.info("error at CreditBillViewModel-> updateFixedAssetItemDepreciation>>> " + sw.toString());		
		}	

	}

	@Command
	@NotifyChange({"totalAmount","lblExpenses","lstExpenses"})
	public void changeAmount(@BindingParam("row") ExpensesModel row)
	{
		if(row.getAmount()>99999999.99)
		{
			Messagebox.show("Amount should be Less than 99999999.99","Amount", Messagebox.OK , Messagebox.INFORMATION);
			row.setAmount(0);
			return;			
		}

		double ExpAmount=0;
		for (ExpensesModel item : lstExpenses) 
		{
			ExpAmount+=item.getAmount();
		}

		lblExpenses="Expenses " + String.valueOf(ExpAmount);
		getNewTotalAmount();
	}


	@Command
	@NotifyChange({ "totalAmount", "lblExpenses", "lstExpenses", "billable" })
	public void selectExpenseCustomer(
			@BindingParam("type") final ExpensesModel type) {
		if (type.getSelectedAccount() != null) {

			if (type.getSelectedAccount().getAccountType().equals("Expense")
					|| type.getSelectedAccount().getAccountType()
					.equals("CostofGoodsSold")
					|| type.getSelectedAccount().getAccountType()
					.equals("OtherExpense")
					|| type.getSelectedAccount().getAccountType()
					.equals("OtherCurrentAsset")) {
				if (type.getSelectedCustomer() != null
						&& type.getSelectedCustomer().getRecNo() > 0) {
					type.setBillableChked(true);
				} else {
					type.setBillableChked(false);
				}
				if (compSetup != null && compSetup.getUsebillable() != null
						&& compSetup.getUsebillable().equalsIgnoreCase("Y")) {
					type.setShowBillable(true);
				}

			} else {
				type.setBillableChked(false);
				// make hide
			}
		}
	}
	
	@Command
	@NotifyChange({ "lstCheckItems", "billable" })
	public void selectItemCustomer(
			@BindingParam("type") final CheckItemsModel type) {
		if (type.getSelectedItems() != null) {
			if (type.getSelectedCustomer() != null
					&& type.getSelectedCustomer().getRecNo() > 0) {
				type.setBillableChked(true);
			} else {
				type.setBillableChked(false);
			}
			if (compSetup != null && compSetup.getUsebillable() != null
					&& compSetup.getUsebillable().equalsIgnoreCase("Y")) {
				type.setShowBillable(true);
			}
		} else {
			type.setBillableChked(false);
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"totalAmount","lblExpenses","lstExpenses"})
	public void selectExpenseAccount(@BindingParam("type")  final ExpensesModel type)
	{
		int count=0;
		if(type.getSelectedAccount()!=null)
		{
			if(type.getSelectedAccount().getAccountType().equals("AccountsReceivable") || type.getSelectedAccount().getAccountType().equals("AccountsPayable"))
			{
				for (ExpensesModel item : lstExpenses) 
				{
					if(item.getSelectedAccount()!=null)
					{
						if(item.getSelectedAccount().getAccountType().equals("AccountsReceivable") || item.getSelectedAccount().getAccountType().equals("AccountsPayable"))
						{
							count++;

						}

					}
				}
				
				

				if(count>1)
				{
					Messagebox.show("You can't use more than 1 a/r or a/p in one Cheque Payment","Account", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedAccount(null);
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstExpenses");
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "totalAmount");
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lblExpenses");
					return;
				}
			}
			if (type.getSelectedAccount().getAccountType().equals("Expense")
					|| type.getSelectedAccount().getAccountType()
					.equals("CostofGoodsSold")
					|| type.getSelectedAccount().getAccountType()
					.equals("Other Expense")
					|| type.getSelectedAccount().getAccountType()
					.equals("OtherCurrentAsset")) {
				if (type.getSelectedCustomer() != null) {
					type.setBillableChked(true);
				} else {
					type.setBillableChked(false);
				}
			} else {
				if (type.getSelectedCustomer() != null) {
					type.setBillableChked(true);
				} else {
					type.setBillableChked(false);
				}

			}




			//check if account has sub account		
			boolean hasSubAccount=data.checkIfBankAccountsHasSub(type.getSelectedAccount().getFullName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainAccount().equals("Y"))
				{

					Messagebox.show("Selected account have sub accounts. Do you want to continue?","Account", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 				        	
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
							}
							else 
							{	
								Map args = new HashMap();
								args.put("result", "2");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedAccount(null);
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstExpenses");
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "totalAmount");
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lblExpenses");
								return;
							}
						}

					});


				}
				else
				{
					Messagebox.show("Selected account have sub accounts. You cannot continue !!","Account", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedAccount(null);		
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstExpenses");
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "totalAmount");
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lblExpenses");
					return;
				}				
			}					

		}
	}

	@GlobalCommand("resetGrid")
	@NotifyChange({"lstExpenses","lstCheckItems"})
	public void resetGrid(@BindingParam("result") String result)
	{		   
		if(result.equals("1"))
		{
			ExpensesModel objExp=new ExpensesModel();
			objExp.setSrNO(lstExpenses.size()+1);
			lstExpenses.add(objExp);
		}
		else 
		{

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstExpenses"})
	public void selectExpenseClass(@BindingParam("type")  final ExpensesModel type)
	{
		if(type.getSelectedClass()!=null)
		{
			//check if account has sub account		
			boolean hasSubAccount=data.checkIfClassHasSub(type.getSelectedClass().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Class have sub Sub Class(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 				        		 				        	
							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "3");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedClass(null);
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstExpenses");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedClass(null);	
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstExpenses");
				}	
			}
		}
	}


	@Command
	@NotifyChange({"lstCheckItems"})
	public void selectItemsCustomer(@BindingParam("type")  final CheckItemsModel type)
	{
		if(type.getSelectedCustomer()!=null)
		{
			type.setBillableChked(true);
		}
		else
		{
			type.setBillableChked(false);
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCheckItems"})
	public void selectItemClass(@BindingParam("type")  final CheckItemsModel type)
	{
		if(type.getSelectedClass()!=null)
		{
			//check if account has sub account		
			boolean hasSubAccount=data.checkIfClassHasSub(type.getSelectedClass().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Class have sub Sub Class(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 				        		 				        	
							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "3");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedClass(null);
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstCheckItems");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedClass(null);	
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstCheckItems");
				}	
			}
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCheckItems","lblCheckItems","totalAmount"})
	public void selectCheckItems(@BindingParam("type") final CheckItemsModel type)
	{


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
								if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && ((type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))))
								{
									type.setHideSite(true);
								}
								else
								{
									type.setHideSite(false);
									QbListsModel tempSite=new QbListsModel();
									tempSite.setRecNo(0);
									type.setSelectedInvcCutomerGridInvrtySiteNew(tempSite);
								}
								QbListsModel objItems=data.getQbItemsData(type.getSelectedItems().getRecNo());
								if(objItems!=null)
								{
									type.setCost(objItems.getPurchaseCost());
									type.setDescription(objItems.getPurchaseDesc());
									type.setAmount(type.getCost() * type.getQuantity());

									for(ClassModel gridClass:lstGridClass)
									{
										if(gridClass.getClass_Key()==objItems.getSubOfClasskey())
										{
											type.setSelectedClass(gridClass);
											break;
										}

									}

									setLabelCheckItems();
									getNewTotalAmount();
								}

								if(type.getSelectedCustomer()!=null)
								{
									type.setBillableChked(true);
								}
								else
								{
									type.setBillableChked(false);
								}

							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedItems(null);
								type.setDescription("");
								type.setSelectedInvcCutomerGridInvrtySiteNew(null);
								type.setSelectedCustomer(null);
								type.setCost(0);
								type.setAmount(0);
								type.setNetTotal(0);
								type.setBillableChked(false);
								type.setSelectedFixedAsset(null);
								setLabelCheckItems();
								getNewTotalAmount();
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstCheckItems");
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "toatlAmount");
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lblCheckItems");

							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Item", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedItems(null);
					type.setDescription("");
					type.setSelectedInvcCutomerGridInvrtySiteNew(null);
					type.setSelectedCustomer(null);
					type.setCost(0);
					type.setAmount(0);
					type.setNetTotal(0);
					type.setBillableChked(false);
					type.setSelectedFixedAsset(null);
					setLabelCheckItems();
					getNewTotalAmount();
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstCheckItems");
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "toatlAmount");
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lblCheckItems");
				}	
			}
			else{
				if(type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))
				{
					type.setHideSite(true);
				}
				else
				{
					type.setHideSite(false);
					QbListsModel tempSite=new QbListsModel();
					tempSite.setRecNo(0);
					type.setSelectedInvcCutomerGridInvrtySiteNew(tempSite);
				}
				QbListsModel objItems=data.getQbItemsData(type.getSelectedItems().getRecNo());
				if(objItems!=null)
				{
					type.setCost(objItems.getPurchaseCost());
					type.setDescription(objItems.getPurchaseDesc());
					type.setAmount(type.getCost() * type.getQuantity());

					for(ClassModel gridClass:lstGridClass)
					{
						if(gridClass.getClass_Key()==objItems.getSubOfClasskey())
						{
							type.setSelectedClass(gridClass);
							break;
						}

					}

					setLabelCheckItems();
					getNewTotalAmount();
				}
				if(type.getSelectedCustomer()!=null)
				{
					type.setBillableChked(true);
				}
				else
				{
					type.setBillableChked(false);
				}
			}
		}

	}

	private void setLabelCheckItems()
	{
		double toalCheckItemsAmount=0;
		for (CheckItemsModel item : lstCheckItems) 
		{
			toalCheckItemsAmount+=item.getAmount();
		}
		//totalAmount=ExpAmount;
		lblCheckItems="Items " + String.valueOf(toalCheckItemsAmount);
	}

	private void setLabelExpanseItems()
	{
		double toalExpanseAmount=0;
		for (ExpensesModel item : lstExpenses) 
		{
			toalExpanseAmount+=item.getAmount();
		}
		lblExpenses="Expenses " + String.valueOf(toalExpanseAmount);
	}


	private void setLabelFaItems()
	{
		double toalExpanseAmount=0;
		for (CheckFAItemsModel item : lstCheckFAItems) 
		{
			toalExpanseAmount+=item.getAmount();
		}
		lblCheckFAItems="Fixed Assets Items " + String.valueOf(toalExpanseAmount);
	}



	private void getNewTotalAmount()
	{
		double ExpAmount=0;
		for (ExpensesModel item : lstExpenses) 
		{
			ExpAmount+=item.getAmount();
		}
		double toalCheckItemsAmount=0;
		for (CheckItemsModel item : lstCheckItems) 
		{
			toalCheckItemsAmount+=item.getAmount();
		}
		double toalCheckFAItemsAmount=0;
		for (CheckFAItemsModel item : lstCheckFAItems) 
		{
			toalCheckFAItemsAmount+=item.getAmount();
		}

		totalAmount=ExpAmount+toalCheckItemsAmount+toalCheckFAItemsAmount;		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCheckItems"})	
	public void changeCheckItemsDate(@BindingParam("type") final CheckItemsModel type)
	{
		if(type.getInvoiceDate()!=null)
		{
			Calendar date1 = Calendar.getInstance();
			Calendar date2 = Calendar.getInstance();
			try 
			{
				date1.setTime(df.parse(sdf.format(type.getInvoiceDate())));
				Date date = new Date();
				date2.setTime(df.parse(sdf.format(date)));
				int year=date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR);
				if(Math.abs(year)>60)
				{
					Messagebox.show("This date not allowed!","Bill Date", Messagebox.OK , Messagebox.INFORMATION);
					type.setInvoiceDate(date);
				}

				date2.setTime(df.parse(sdf.format(creationdate)));
				year=date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR);				
				int month=date1.get(Calendar.MONTH) - date2.get(Calendar.MONTH);				 
				month=year*12 + month;
				if(Math.abs(month)>6)
				{
					Messagebox.show("The date is more or less 6 months from the date of transaction. Do you want to continue?","Bill Date", Messagebox.YES |  Messagebox.NO, Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	 				        		 				        	
							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null, "resetItemsGrid", args);
								type.setInvoiceDate(null);
								BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstCheckItems");
							}
						}

					});


				}
			}

			catch (Exception e) {
				logger.info("error at changeCheckItemsDate>>>" + e.getMessage());
			}

		}
	}

	@GlobalCommand("resetItemsGrid")
	@NotifyChange({"lstCheckItems"})
	public void resetItemsGrid(@BindingParam("result") String result)
	{		   		
	}

	@Command
	@NotifyChange({"lstCheckItems","lblCheckItems","totalAmount"})
	public void changeCheckItems(@BindingParam("type") CheckItemsModel type,@BindingParam("parm") String parm)
	{
		if(parm.equals("qty"))
		{
			type.setAmount(type.getCost() * type.getQuantity());
		}

		if(parm.equals("cost"))
		{
			boolean MINPCFlag=false;
			boolean MAXPCFlag=false;
			//check if cost more than init value
			if(compSetup.getBuyItemWithHighCost().equals("N"))
			{
				QbListsModel objItems=data.getQbItemsData(type.getSelectedItems().getRecNo());
				if(compSetup.getUseMinPurchasePrice().equals("Y"))
				{
					if(compSetup.getMinPurchasePriceRatio()>0)
					{
						MINPCFlag=true;
						double MINPurchaseCost=objItems.getPurchaseCost() - ((objItems.getPurchaseCost() * compSetup.getMinPurchasePriceRatio())/100);
						if(type.getCost() < MINPurchaseCost)
						{
							Messagebox.show("The Price you entered is lower than the standard & discount allowed!","Bill",Messagebox.OK,Messagebox.INFORMATION);
						}
					}
				}

				if(compSetup.getUseMaxPurchasePrice().equals("Y"))
				{
					if(compSetup.getMaxPurchasePriceRatio()>0)
					{
						MAXPCFlag=true;
						double MAXPurchaseCost=objItems.getPurchaseCost() + ((objItems.getPurchaseCost() * compSetup.getMaxPurchasePriceRatio())/100);
						if(type.getCost() > MAXPurchaseCost)
						{
							Messagebox.show("The Price entered is higher than the standard & discount allowed!","Bill",Messagebox.OK,Messagebox.INFORMATION);
						}
					}
				}
				if(MINPCFlag==false && MAXPCFlag==false)
				{
					if(type.getCost()> objItems.getPurchaseCost())
					{
						Messagebox.show("The Price entered is higher than the standard cost price!","Bill",Messagebox.OK,Messagebox.INFORMATION);
						type.setCost(objItems.getPurchaseCost());
					}
				}
			}
			type.setAmount(type.getCost() * type.getQuantity());

		}

		if(parm.equals("amount"))
		{
			double cost=type.getAmount() / type.getQuantity();
			type.setCost(cost);
		}
		setLabelCheckItems();
		getNewTotalAmount();
	}

	@Command
	@NotifyChange({"lstCheckFAItems","lblCheckFAItems","totalAmount"})
	public void changeCheckFAItems(@BindingParam("type") CheckFAItemsModel type,@BindingParam("parm") String parm)
	{

		if(parm.equals("price") || parm.equals("qty") || parm.equals("charge"))
		{
			type.setAmount(( type.getUnitPrice() * type.getQuantity()) + type.getOtherCharges());
		}

		double  toalCheckFAItemsAmount=0;
		for (CheckFAItemsModel item : lstCheckFAItems) 
		{
			toalCheckFAItemsAmount+=item.getAmount();
		}
		//totalAmount=ExpAmount;

		lblCheckFAItems="Fixed Assets Items " + String.valueOf(toalCheckFAItemsAmount);
		getNewTotalAmount();
	}
	@Command
	@NotifyChange({"lstCheckFAItems","lstVendorFAItems","totalAmount","lblCheckFAItems"})
	public void selectCheckFAItems(@BindingParam("type") CheckFAItemsModel type)	
	{

		if(type.getSelectedFixedAsset()!=null)
		{
			for (CheckFAItemsModel item : lstCheckFAItems) 
			{
				if(item.getLineNo()!=type.getLineNo())
				{
					if(type.getSelectedFixedAsset()==item.getSelectedFixedAsset())
					{
						Messagebox.show("The selected Asset has already been previously selected.","Bill",Messagebox.OK,Messagebox.INFORMATION);
						type.setSelectedFixedAsset(null);
						BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "lstCheckFAItems");
						return;
					}

				}
			}

			type.setSelectedCustomer(null);
			type.setSelectedCustody(null);
			type.setDescription("");
			type.setUnitPrice(0);

			FixedAssetModel obj=data.getFixedAssetItemData(type.getSelectedFixedAsset().getAssetid());
			if(obj!=null)
			{
				type.setDescription(obj.getDescription());
				type.setUnitPrice(obj.getPrice());
				type.setAmount(( type.getUnitPrice() * type.getQuantity()) + type.getOtherCharges());

				for (QbListsModel item : lstGridCustomer) 
				{
					if(item.getRecNo()==obj.getLocationID())
						type.setSelectedCustomer(item);
				}
				for (QbListsModel vendor : lstGridCustody)
				{
					if(vendor.getRecNo()==obj.getEmployeeID())
					{
						type.setSelectedCustody(vendor);
					}
				}

			}
			long  toalCheckFAItemsAmount=0;
			for (CheckFAItemsModel item : lstCheckFAItems) 
			{
				toalCheckFAItemsAmount+=item.getAmount();
			}
			//totalAmount=ExpAmount;

			lblCheckFAItems="Fixed Assets Items " + toalCheckFAItemsAmount;
			getNewTotalAmount();

			//add New Row
			/*CheckFAItemsModel lastItem=lstCheckFAItems.get(lstCheckFAItems.size()-1);
		if(lastItem.getSelectedFixedAsset()!=null)
		{						
			CheckFAItemsModel objNew=new CheckFAItemsModel();
			objNew.setLineNo(lstCheckFAItems.size()+1);
			objNew.setQuantity(1);
			lstCheckFAItems.add(objNew);
		}*/

		}
	}

	private boolean validateData(boolean flag)
	{
		boolean isValid=true;

		if(selectedPaytoOrder==null)
		{
			Messagebox.show("You Must Select A 'Vendor' !!!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedPaytoOrder.getRecNo()==0)
		{
			Messagebox.show("You Must Select A 'Vendor' !!!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedAccount==null)
		{
			Messagebox.show("You Must Assign an Account For This Transaction!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(objCheque.getRefNumber()==null)
		{
			Messagebox.show("Enter the refrence Number!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false; 
		}

		if(lstExpenses==null && lstCheckItems==null && lstCheckFAItems==null)
		{
			Messagebox.show("Please enter the data in the at least one tab of data in the grid ","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false; 
		}

		if(lstExpenses!=null && lstCheckItems!=null && lstCheckFAItems!=null)
		{
			ExpensesModel lastItem=lstExpenses.get(0);
			CheckFAItemsModel fAItems=lstCheckFAItems.get(0);
			CheckItemsModel chekItems=lstCheckItems.get(0);
			if(lastItem.getSelectedAccount()==null && fAItems.getSelectedFixedAsset()==null && chekItems.getSelectedItems()==null)
			{
				Messagebox.show("Please enter the data in the at least one tab of data in the grid ","Bill",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}

		}

		//if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && ((type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly"))))
		boolean siteValidation=false;
		if(lstCheckItems!=null)
		{	
			for(CheckItemsModel gridData:lstCheckItems)
			{
				CheckItemsModel lastItem=gridData;
				if(lastItem.getSelectedItems()!=null)
				{		
					if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && ((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null || lastItem.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0)))
					{
						Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record in the Items Tab!","Bill",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}

					CheckItemsModel objExp=gridData;

					if((objExp.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || objExp.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (objExp.getSelectedInvcCutomerGridInvrtySiteNew()==null || objExp.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0))
					{
						siteValidation=true;
						break;

					}
					else
					{
						siteValidation=false;
					}

					if(objExp.getQuantity()==0)
					{	 		
						Messagebox.show("Please Enter The Quantity,Empty Transaction is not allowed !!!","Bill",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}
				}
			}	


			if(siteValidation)
			{
				Messagebox.show("To Save this record,First select Site Name from the existing records in the Items Tab!","Bill",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}


		}

		if(lstExpenses!=null)
		{	
			for(ExpensesModel gridData:lstExpenses)
			{
				ExpensesModel lastItem=gridData;
				if(lastItem.getSelectedAccount()!=null)
				{
					if(lastItem.getAmount()==0)
					{
						Messagebox.show("Enter NetAmount in the Expense Tab!!!","Bill",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}
				}

			}


		}




		if(billNo==null || billNo.equalsIgnoreCase("") || billNo.equalsIgnoreCase("0"))
		{
			Messagebox.show("Enter the bill Number !","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false; 
		}

		if(flag)
		{
			if((objCheque.getRefNumber()!=null) && (billData.checkIfReferanceNumberIsCreditBill(objCheque.getRefNumber(),objCheque.getRecNo())==true))
			{
				Messagebox.show("Duplicate Refreance Number!","Bill",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}

			if((billNo!=null) && (billData.checkIfBillNumberIsCreditBill(billNo,objCheque.getRecNo())==true))
			{
				Messagebox.show("Duplicate Bill Number!","Bill",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
		}

		// Messagebox.show(String.valueOf(tmpUnitKey));
		ExpensesModel objExp=lstExpenses.get(0);
		if(objExp.getSelectedAccount()!=null)
		{
			if(objExp.getAmount()==0)
			{
				Messagebox.show("Enter NetAmount in the Expense Tab !!!","Bill",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}


		if(totalAmount==0)
		{
			Messagebox.show("Empty amount transaction is not allowed!!","Bill",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}


		return isValid;

	}

	@SuppressWarnings("unused")
	private void settmpUnitKey(int key)
	{
		tmpUnitKey=key;
	}
	public Date getCreationdate() {
		return creationdate;
	}


	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}


	public List<AccountsModel> getLstaccounts() {
		return lstaccounts;
	}


	public void setLstaccounts(List<AccountsModel> lstaccounts) {
		this.lstaccounts = lstaccounts;
	}


	public AccountsModel getSelectedAccount() {
		return selectedAccount;
	}


	public void setSelectedAccount1(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	@NotifyChange({"objCheque","showBankAccount"})
	public void setSelectedAccount(AccountsModel selectedAccount) 
	{	
		this.selectedAccount = selectedAccount;
		//get Cheque Serial No
		if(selectedAccount!=null)
		{
			//String tmpSerailField=SerialFields.ChequeNo.toString()+"-"+String.valueOf(selectedAccount.getRec_No());
			//objCheque.setCheckNo(String.valueOf(data.GetSerialNumber(tmpSerailField)));
			//showBankAccount=selectedAccount.getAccountType().equals("Post Dated Cheque");
		}

	}


	public List<QbListsModel> getLstPayToOrder() {
		return lstPayToOrder;
	}


	public void setLstPayToOrder(List <QbListsModel> lstPayToOrder) {
		this.lstPayToOrder = lstPayToOrder;
	}







	public QbListsModel getSelectedPaytoOrder() {
		return selectedPaytoOrder;
	}

	@NotifyChange({"objCheque","lstVendorFAItems"})
	public void setSelectedPaytoOrder(QbListsModel selectedPaytoOrder) 
	{
		this.selectedPaytoOrder = selectedPaytoOrder;
		if(selectedPaytoOrder!=null)
		{
			PayToOrderModel obj=data.getPayToOrderInfo(selectedPaytoOrder.getListType(), selectedPaytoOrder.getRecNo());
			if(selectedPaytoOrder.getListType().equals("Employee") || selectedPaytoOrder.getListType().equals("Vendor") || selectedPaytoOrder.getListType().equals("Customer"))
			{
				/*if(obj.getPrintChequeAs().length()>0)
			//objCheque.setPrintName(obj.getPrintChequeAs());
		else
		//objCheque.setPrintName(obj.getName());
				 */		}
			String address="";	
			if(obj.getBillAddress1().length()>0)			
				address=obj.getBillAddress1();		
			if(obj.getBillAddress2().length()>0)
				address+="\n" + obj.getBillAddress2();
			if(obj.getBillAddress3().length()>0)
				address+="\n" + obj.getBillAddress3();
			if(obj.getBillAddress4().length()>0)
				address+="\n" + obj.getBillAddress4();
			if(obj.getPhone().length()>0)
				address+="\n" + obj.getPhone();
			if(obj.getFax().length()>0)
				address+="\n" + obj.getFax();
			objCheque.setAddress(address);

			//fill FixedItems ListBox
			lstVendorFAItems=billData.getVendorFixedAssetItemQueryforbill(selectedPaytoOrder.getRecNo());

		}
		else
		{
			Messagebox.show("Invlaid Name !!","Bill",Messagebox.OK,Messagebox.INFORMATION);		
		}
	}


	public boolean isShowBankAccount() {
		return showBankAccount;
	}


	public void setShowBankAccount(boolean showBankAccount) {
		this.showBankAccount = showBankAccount;
	}


	public List<AccountsModel> getLstGridBankAccounts() {
		return lstGridBankAccounts;
	}


	public void setLstGridBankAccounts(List<AccountsModel> lstGridBankAccounts) {
		this.lstGridBankAccounts = lstGridBankAccounts;
	}


	public List<ExpensesModel> getLstExpenses() {
		return lstExpenses;
	}


	public void setLstExpenses(List<ExpensesModel> lstExpenses) {
		this.lstExpenses = lstExpenses;
	}


	public AccountsModel getSelectedGridBankAccount() {
		return selectedGridBankAccount;
	}


	public void setSelectedGridBankAccount(AccountsModel selectedGridBankAccount) 
	{
		this.selectedGridBankAccount = selectedGridBankAccount;
		if(selectedGridBankAccount!=null)
		{

		}
	}
	public List <QbListsModel> getLstGridCustomer() {
		return lstGridCustomer;
	}
	public void setLstGridCustomer(List <QbListsModel> lstGridCustomer) {
		this.lstGridCustomer = lstGridCustomer;
	}
	public List<ClassModel> getLstGridClass() {
		return lstGridClass;
	}
	public void setLstGridClass(List<ClassModel> lstGridClass) {
		this.lstGridClass = lstGridClass;
	}
	public List<FixedAssetModel> getLstGridFixedAssetItems() {
		return lstGridFixedAssetItems;
	}
	public void setLstGridFixedAssetItems(List<FixedAssetModel> lstGridFixedAssetItems) {
		this.lstGridFixedAssetItems = lstGridFixedAssetItems;
	}

	public boolean isChkTobePrinted() {
		return chkTobePrinted;
	}
	public void setChkTobePrinted(boolean chkTobePrinted) {
		this.chkTobePrinted = chkTobePrinted;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getTotalExpenses() {
		return totalExpenses;
	}
	public void setTotalExpenses(double totalExpenses) {
		this.totalExpenses = totalExpenses;
	}
	public String getLblExpenses() {
		return lblExpenses;
	}
	public void setLblExpenses(String lblExpenses) {
		this.lblExpenses = lblExpenses;
	}


	public List<CheckItemsModel> getLstCheckItems() {
		return lstCheckItems;
	}


	public void setLstCheckItems(List<CheckItemsModel> lstCheckItems) {
		this.lstCheckItems = lstCheckItems;
	}


	public List <QbListsModel> getLstGridQBItems() {
		return lstGridQBItems;
	}


	public void setLstGridQBItems(List <QbListsModel> lstGridQBItems) {
		this.lstGridQBItems = lstGridQBItems;
	}


	public String getLblCheckItems() {
		return lblCheckItems;
	}


	public void setLblCheckItems(String lblCheckItems) {
		this.lblCheckItems = lblCheckItems;
	}


	public List<CheckFAItemsModel> getLstCheckFAItems() {
		return lstCheckFAItems;
	}


	public void setLstCheckFAItems(List<CheckFAItemsModel> lstCheckFAItems) {
		this.lstCheckFAItems = lstCheckFAItems;
	}


	public List<FixedAssetModel> getLstVendorFAItems() {
		return lstVendorFAItems;
	}


	public void setLstVendorFAItems(List<FixedAssetModel> lstVendorFAItems) {
		this.lstVendorFAItems = lstVendorFAItems;
	}


	public String getLblCheckFAItems() {
		return lblCheckFAItems;
	}


	public void setLblCheckFAItems(String lblCheckFAItems) {
		this.lblCheckFAItems = lblCheckFAItems;
	}


	public List <QbListsModel> getLstGridCustody() {
		return lstGridCustody;
	}


	public void setLstGridCustody(List <QbListsModel> lstGridCustody) {
		this.lstGridCustody = lstGridCustody;
	}


	public ExpensesModel getSelectedExpenses() {
		return selectedExpenses;
	}


	public void setSelectedExpenses(ExpensesModel selectedExpenses) {
		this.selectedExpenses = selectedExpenses;
	}


	public CheckItemsModel getSelectedCheckItems() {
		return selectedCheckItems;
	}


	public void setSelectedCheckItems(CheckItemsModel selectedCheckItems) {
		this.selectedCheckItems = selectedCheckItems;
	}


	public CheckFAItemsModel getSelectedCheckFAItems() {
		return selectedCheckFAItems;
	}


	public void setSelectedCheckFAItems(CheckFAItemsModel selectedCheckFAItems) {
		this.selectedCheckFAItems = selectedCheckFAItems;
	}


	public List<TermModel> getLstCreditBillTerms() {
		return lstCreditBillTerms;
	}


	public void setLstCreditBillTerms(List<TermModel> lstCreditBillTerms) {
		this.lstCreditBillTerms = lstCreditBillTerms;
	}


	public TermModel getSelectedCreditBillTerms() {
		return selectedCreditBillTerms;
	}


	public void setSelectedCreditBillTerms(TermModel selectedCreditBillTerms) {
		this.selectedCreditBillTerms = selectedCreditBillTerms;
	}


	public CreditBillModel getObjCheque() {
		return objCheque;
	}


	public void setObjCheque(CreditBillModel objCheque) {
		this.objCheque = objCheque;
	}


	public String getMsgToBeDispalyedOnInvoice() {
		return msgToBeDispalyedOnInvoice;
	}


	public void setMsgToBeDispalyedOnInvoice(String msgToBeDispalyedOnInvoice) {
		this.msgToBeDispalyedOnInvoice = msgToBeDispalyedOnInvoice;
	}


	public CompSetupModel getCompSetup() {
		return compSetup;
	}


	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}


	public BillData getBillData() {
		return billData;
	}


	public void setBillData(BillData billData) {
		this.billData = billData;
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


	public boolean isAdminUser() {
		return adminUser;
	}


	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}


	public int getEditBillKey() {
		return editBillKey;
	}


	public void setEditBillKey(int editBillKey) {
		this.editBillKey = editBillKey;
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


	public String getBillNo() {
		return billNo;
	}


	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}


	public Date getBillDueDate() {
		return billDueDate;
	}


	public void setBillDueDate(Date billDueDate) {
		this.billDueDate = billDueDate;
	}


	public List<QbListsModel> getLstInvcCustomerGridInvrtySite() {
		return lstInvcCustomerGridInvrtySite;
	}


	public void setLstInvcCustomerGridInvrtySite(
			List<QbListsModel> lstInvcCustomerGridInvrtySite) {
		this.lstInvcCustomerGridInvrtySite = lstInvcCustomerGridInvrtySite;
	}


	@Command
	@NotifyChange({"lblExpenses","lblCheckItems","lblCheckFAItems","objCheque","selectedCreditBillTerms","editBillKey","creationdate","labelStatus","selectedAccount","billNo","selectedPaytoOrder","creationdate","billDueDate","toatlAmount","lstCheckItems","lstExpenses","lstCheckFAItems","totalAmount","tempTotalAmount","actionTYpe"})
	public void navigationItemReceipt(@BindingParam("cmp") String navigation)
	{
		try
		{
			objCheque=billData.navigationBill(editBillKey,webUserID,seeTrasction,navigation,actionTYpe);
			lblExpenses="Expenses 0.00";
			lblCheckItems="Items 0.00";
			if(objCheque!=null && objCheque.getRecNo()>0)
			{
				actionTYpe="edit";
				labelStatus="Edit";
				editBillKey=objCheque.getRecNo();
				List<ExpensesModel> expenseGrid=billData.getBillGridDataExpenseById(editBillKey);
				List<CheckItemsModel> itemsGrid=billData.getBillGridDataItemById(editBillKey);
				List<CheckFAItemsModel> itemsFixedAssetGrid=billData.getBillGridDataFAById(editBillKey);
				for(AccountsModel apAcounts:lstaccounts)
				{
					if(apAcounts.getRec_No()==objCheque.getApAccountRefKey())
					{
						selectedAccount=apAcounts;
						break;
					}

				}

				for(TermModel termsList:lstCreditBillTerms)
				{
					if(termsList.getTermKey()==objCheque.getTermsRefKey())
					{
						selectedCreditBillTerms=termsList;
						break;
					}

				}

				for(QbListsModel vendorList:lstPayToOrder)
				{
					if(vendorList.getRecNo()==objCheque.getVendRefKey())
					{
						selectedPaytoOrder=vendorList;
						break;
					}

				}

				totalAmount=objCheque.getAmount();
				billDueDate=df.parse(sdf.format(objCheque.getDueDate()));
				creationdate=df.parse(sdf.format(objCheque.getTxnDate()));
				billNo=objCheque.getBillNo();
				setSelectedPaytoOrder(selectedPaytoOrder);
				objCheque.setRefNumber(objCheque.getRefNumber());

				//Expense Grid
				lstExpenses=new ArrayList<ExpensesModel>();
				for(ExpensesModel editExpensesModel:expenseGrid)
				{
					
					ExpensesModel obj=new ExpensesModel();
					
					obj.setSrNO(lstExpenses.size()+1);

					if (editExpensesModel.getBillable().equalsIgnoreCase("Y")) {
						obj.setBillableChked(true);
						obj.setBillable("Y");
						obj.setShowBillable(true);
					} else {
						obj.setBillableChked(false);
						obj.setBillable("N");
						obj.setShowBillable(false);
					}
					for(AccountsModel expanseAccount:lstGridBankAccounts)
					{
						if(expanseAccount.getRec_No()==editExpensesModel.getSelectedAccountKey())
						{
							obj.setSelectedAccount(expanseAccount);
							break;
						}

					}

					for(QbListsModel gridCutomer:lstGridCustomer)
					{
						if(gridCutomer.getRecNo()==editExpensesModel.getSelectedCutomerKey())
						{
							obj.setSelectedCustomer(gridCutomer);
							break;
						}

					}

					for(ClassModel gridClass:lstGridClass)
					{
						if(gridClass.getClass_Key()==editExpensesModel.getSelectedClassKey())
						{
							obj.setSelectedClass(gridClass);
							break;
						}

					}


					for(FixedAssetModel fixedList:lstGridFixedAssetItems)
					{
						if(fixedList.getAssetid()==editExpensesModel.getFixedAssetItemid())
						{
							obj.setSelectedFixedAsset(fixedList);
							break;
						}

					}
					obj.setRecNo(editExpensesModel.getRecNo());
					obj.setMemo(editExpensesModel.getMemo());
					obj.setAmount(editExpensesModel.getAmount());
					
					lstExpenses.add(obj);
				}


				if(expenseGrid.size()==0)
				{
					lstExpenses=new ArrayList<ExpensesModel>();
					ExpensesModel objExp=new ExpensesModel();
					objExp.setSrNO(lstExpenses.size()+1);
					lstExpenses.add(objExp);
					lblExpenses="Expenses 0.00";
				}

				//Items Grid
				lstCheckItems=new ArrayList<CheckItemsModel>();
				for(CheckItemsModel editItemsGrid:itemsGrid)
				{
					CheckItemsModel obj=new CheckItemsModel();
					obj.setLineNo(lstCheckItems.size()+1);
					if (editItemsGrid.getBillable().equalsIgnoreCase("Y")) {
						obj.setBillableChked(true);
						obj.setBillable("Y");
						obj.setShowBillable(true);
					} else {
						obj.setBillableChked(false);
						obj.setBillable("N");
						obj.setShowBillable(false);
					}
					for(QbListsModel items:lstGridQBItems)
					{
						if(items.getRecNo()==editItemsGrid.getItemKey())
						{
							obj.setSelectedItems(items);
							break;
						}

					}
					for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
					{
						if(gridSite.getRecNo()==editItemsGrid.getInventorySiteKey())
						{
							obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
							if(gridSite.getRecNo()>0)
								obj.setHideSite(true);
							break;
						}

					}

					for(QbListsModel gridCutomer:lstGridCustomer)
					{
						if(gridCutomer.getRecNo()==editItemsGrid.getSelectedCustKey())
						{
							obj.setSelectedCustomer(gridCutomer);
							break;
						}

					}
					for(ClassModel gridClass:lstGridClass)
					{
						if(gridClass.getClass_Key()==editItemsGrid.getSelectedClassKey())
						{
							obj.setSelectedClass(gridClass);
							break;
						}

					}


					for(FixedAssetModel fixedList:lstGridFixedAssetItems)
					{
						if(fixedList.getAssetid()==editItemsGrid.getFixedIteKey())
						{
							obj.setSelectedFixedAsset(fixedList);
							break;
						}

					}

					if(editItemsGrid.getBillable()!=null && editItemsGrid.getBillable().equalsIgnoreCase("Y"))
					{
						obj.setBillableChked(true);
					}
					else
					{
						obj.setBillableChked(false);
					}
					obj.setRecNo(editItemsGrid.getRecNo());
					obj.setMemo(editItemsGrid.getMemo());
					obj.setDescription(editItemsGrid.getDescription());
					obj.setCost(editItemsGrid.getCost());
					obj.setAmount(editItemsGrid.getAmount());
					obj.setQuantity(editItemsGrid.getQuantity());
					lstCheckItems.add(obj);
				}

				if(itemsGrid.size()==0)
				{
					lstCheckItems=new ArrayList<CheckItemsModel>();
					CheckItemsModel objItems=new CheckItemsModel();
					objItems.setLineNo(1);
					objItems.setQuantity(1);
					lstCheckItems.add(objItems);
					lblCheckItems="Items 0.00";
				}


				//Fixed Assert Grid

				lstCheckFAItems=new ArrayList<CheckFAItemsModel>();
				for(CheckFAItemsModel editFixedItemGrid:itemsFixedAssetGrid)
				{
					CheckFAItemsModel obj=new CheckFAItemsModel();
					obj.setLineNo(lstCheckFAItems.size()+1);

					for(FixedAssetModel items:lstVendorFAItems)
					{
						if(items.getAssetid()==editFixedItemGrid.getFaItemKey())
						{
							obj.setSelectedFixedAsset(items);
							break;
						}

					}
					for(QbListsModel gridCutomer:lstGridCustomer)
					{
						if(gridCutomer.getRecNo()==editFixedItemGrid.getCustomerKey())
						{
							obj.setSelectedCustomer(gridCutomer);
							break;
						}

					}
					for(QbListsModel gridCustody:lstGridCustody)
					{
						if(gridCustody.getRecNo()==editFixedItemGrid.getCustodyKey())
						{
							obj.setSelectedCustody(gridCustody);
							break;
						}

					}
					obj.setRecNo(editFixedItemGrid.getRecNo());
					obj.setDescription(editFixedItemGrid.getDescription());
					obj.setUnitPrice(editFixedItemGrid.getUnitPrice());
					obj.setOtherCharges(editFixedItemGrid.getOtherCharges());
					obj.setAmount(editFixedItemGrid.getAmount());
					obj.setQuantity(editFixedItemGrid.getQuantity());
					lstCheckFAItems.add(obj);
				}

				if(itemsFixedAssetGrid.size()==0)
				{
					lstCheckFAItems=new ArrayList<CheckFAItemsModel>();
					CheckFAItemsModel objFAItems=new CheckFAItemsModel();
					objFAItems.setLineNo(1);
					objFAItems.setQuantity(1);
					lstCheckFAItems.add(objFAItems);
				}

				setLabelCheckItems();
				getNewTotalAmount();
				setLabelExpanseItems();
				setLabelFaItems();

			}
			else
			{
				actionTYpe="create";
				editBillKey=0;
				labelStatus="Create";
				totalAmount=0;
				Calendar c = Calendar.getInstance();			
				creationdate=df.parse(sdf.format(c.getTime()));
				objCheque=new CreditBillModel();
				objCheque.setTimeCreated(df.parse(sdf.format(c.getTime())));
				billDueDate=creationdate;
				billNo="0";
				ClearData();
				lstExpenses=new ArrayList<ExpensesModel>();
				ExpensesModel objExp=new ExpensesModel();
				objExp.setSrNO(1);
				lstExpenses.add(objExp);			
				lblExpenses="Expenses 0.00";

				lstCheckItems=new ArrayList<CheckItemsModel>();
				CheckItemsModel objItems=new CheckItemsModel();
				objItems.setLineNo(1);
				objItems.setQuantity(1);
				lstCheckItems.add(objItems);
				lblCheckItems="Items 0.00";

				lstCheckFAItems=new ArrayList<CheckFAItemsModel>();
				CheckFAItemsModel objFAItems=new CheckFAItemsModel();
				objFAItems.setLineNo(1);
				objFAItems.setQuantity(1);
				lstCheckFAItems.add(objFAItems);
				lblCheckFAItems="Fixed Assets Items 0.00";
				selectedAccount=null;
				selectedCreditBillTerms=null;
				selectedPaytoOrder=null;
			}



		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CreditBillViewModel ----> navigationItemReceipt", ex);		
		}
	}


	@Command
	@NotifyChange({"lblExpenses","lblCheckItems","lblCheckFAItems","objCheque","selectedCreditBillTerms","editBillKey","creationdate","labelStatus","selectedAccount","billNo","selectedPaytoOrder","creationdate","billDueDate","toatlAmount","lstCheckItems","lstExpenses","lstCheckFAItems","totalAmount","tempTotalAmount","actionTYpe"})
	public void copyFunctinality(@BindingParam("cmp") String navigation)
	{
		try
		{
			if(editBillKey>0)
			{
				objCheque=billData.getBillById(editBillKey,webUserID,seeTrasction);
				lblExpenses="Expenses 0.00";
				lblCheckItems="Items 0.00";
				if(objCheque!=null && objCheque.getRecNo()>0)
				{
					actionTYpe="edit";
					labelStatus="Copied-Create";
					editBillKey=objCheque.getRecNo();
					List<ExpensesModel> expenseGrid=billData.getBillGridDataExpenseById(editBillKey);
					List<CheckItemsModel> itemsGrid=billData.getBillGridDataItemById(editBillKey);
					List<CheckFAItemsModel> itemsFixedAssetGrid=billData.getBillGridDataFAById(editBillKey);
					for(AccountsModel apAcounts:lstaccounts)
					{
						if(apAcounts.getRec_No()==objCheque.getApAccountRefKey())
						{
							selectedAccount=apAcounts;
							break;
						}

					}

					for(TermModel termsList:lstCreditBillTerms)
					{
						if(termsList.getTermKey()==objCheque.getTermsRefKey())
						{
							selectedCreditBillTerms=termsList;
							break;
						}

					}

					for(QbListsModel vendorList:lstPayToOrder)
					{
						if(vendorList.getRecNo()==objCheque.getVendRefKey())
						{
							selectedPaytoOrder=vendorList;
							break;
						}

					}

					totalAmount=objCheque.getAmount();
					billDueDate=df.parse(sdf.format(objCheque.getDueDate()));
					creationdate=df.parse(sdf.format(objCheque.getTxnDate()));
					billNo=objCheque.getBillNo();
					setSelectedPaytoOrder(selectedPaytoOrder);

					editBillKey=0;
					totalAmount=0;
					Calendar c = Calendar.getInstance();			
					creationdate=df.parse(sdf.format(c.getTime()));
					objCheque=new CreditBillModel();
					objCheque.setTimeCreated(df.parse(sdf.format(c.getTime())));
					billDueDate=creationdate;
					billNo="0";
					ClearData();

					//Expense Grid
					lstExpenses=new ArrayList<ExpensesModel>();
					for(ExpensesModel editExpensesModel:expenseGrid)
					{
						ExpensesModel obj=new ExpensesModel();
						obj.setSrNO(lstExpenses.size()+1);

						for(AccountsModel expanseAccount:lstGridBankAccounts)
						{
							if(expanseAccount.getRec_No()==editExpensesModel.getSelectedAccountKey())
							{
								obj.setSelectedAccount(expanseAccount);
								break;
							}

						}

						for(QbListsModel gridCutomer:lstGridCustomer)
						{
							if(gridCutomer.getRecNo()==editExpensesModel.getSelectedCutomerKey())
							{
								obj.setSelectedCustomer(gridCutomer);
								break;
							}

						}

						for(ClassModel gridClass:lstGridClass)
						{
							if(gridClass.getClass_Key()==editExpensesModel.getSelectedClassKey())
							{
								obj.setSelectedClass(gridClass);
								break;
							}

						}


						for(FixedAssetModel fixedList:lstGridFixedAssetItems)
						{
							if(fixedList.getAssetid()==editExpensesModel.getFixedAssetItemid())
							{
								obj.setSelectedFixedAsset(fixedList);
								break;
							}

						}
						obj.setRecNo(editExpensesModel.getRecNo());
						obj.setMemo(editExpensesModel.getMemo());
						obj.setAmount(editExpensesModel.getAmount());
						if(editExpensesModel.getBillable()!=null && editExpensesModel.getBillable().equalsIgnoreCase("Y"))
						{
							obj.setBillableChked(true);
						}
						else
						{
							obj.setBillableChked(false);
						}
						lstExpenses.add(obj);
					}

					if(expenseGrid.size()==0)
					{
						lstExpenses=new ArrayList<ExpensesModel>();
						ExpensesModel objExp=new ExpensesModel();
						objExp.setSrNO(lstExpenses.size()+1);
						lstExpenses.add(objExp);
						lblExpenses="Expenses 0.00";
					}

					//Items Grid
					lstCheckItems=new ArrayList<CheckItemsModel>();
					for(CheckItemsModel editItemsGrid:itemsGrid)
					{
						CheckItemsModel obj=new CheckItemsModel();
						obj.setLineNo(lstCheckItems.size()+1);

						for(QbListsModel items:lstGridQBItems)
						{
							if(items.getRecNo()==editItemsGrid.getItemKey())
							{
								obj.setSelectedItems(items);
								break;
							}

						}
						for(QbListsModel gridSite:lstInvcCustomerGridInvrtySite)
						{
							if(gridSite.getRecNo()==editItemsGrid.getInventorySiteKey())
							{
								obj.setSelectedInvcCutomerGridInvrtySiteNew(gridSite);
								if(gridSite.getRecNo()>0)
									obj.setHideSite(true);
								break;
							}

						}

						for(QbListsModel gridCutomer:lstGridCustomer)
						{
							if(gridCutomer.getRecNo()==editItemsGrid.getSelectedCustKey())
							{
								obj.setSelectedCustomer(gridCutomer);
								break;
							}

						}
						for(ClassModel gridClass:lstGridClass)
						{
							if(gridClass.getClass_Key()==editItemsGrid.getSelectedClassKey())
							{
								obj.setSelectedClass(gridClass);
								break;
							}

						}


						for(FixedAssetModel fixedList:lstGridFixedAssetItems)
						{
							if(fixedList.getAssetid()==editItemsGrid.getFixedIteKey())
							{
								obj.setSelectedFixedAsset(fixedList);
								break;
							}

						}

						if(editItemsGrid.getBillable()!=null && editItemsGrid.getBillable().equalsIgnoreCase("Y"))
						{
							obj.setBillableChked(true);
						}
						else
						{
							obj.setBillableChked(false);
						}
						obj.setRecNo(editItemsGrid.getRecNo());
						obj.setMemo(editItemsGrid.getMemo());
						obj.setDescription(editItemsGrid.getDescription());
						obj.setCost(editItemsGrid.getCost());
						obj.setAmount(editItemsGrid.getAmount());
						obj.setQuantity(editItemsGrid.getQuantity());
						lstCheckItems.add(obj);
					}

					if(itemsGrid.size()==0)
					{
						lstCheckItems=new ArrayList<CheckItemsModel>();
						CheckItemsModel objItems=new CheckItemsModel();
						objItems.setLineNo(1);
						objItems.setQuantity(1);
						lstCheckItems.add(objItems);
						lblCheckItems="Items 0.00";
					}


					//Fixed Assert Grid

					lstCheckFAItems=new ArrayList<CheckFAItemsModel>();
					for(CheckFAItemsModel editFixedItemGrid:itemsFixedAssetGrid)
					{
						CheckFAItemsModel obj=new CheckFAItemsModel();
						obj.setLineNo(lstCheckFAItems.size()+1);

						for(FixedAssetModel items:lstVendorFAItems)
						{
							if(items.getAssetid()==editFixedItemGrid.getFaItemKey())
							{
								obj.setSelectedFixedAsset(items);
								break;
							}

						}
						for(QbListsModel gridCutomer:lstGridCustomer)
						{
							if(gridCutomer.getRecNo()==editFixedItemGrid.getCustomerKey())
							{
								obj.setSelectedCustomer(gridCutomer);
								break;
							}

						}
						for(QbListsModel gridCustody:lstGridCustody)
						{
							if(gridCustody.getRecNo()==editFixedItemGrid.getCustodyKey())
							{
								obj.setSelectedCustody(gridCustody);
								break;
							}

						}
						obj.setRecNo(editFixedItemGrid.getRecNo());
						obj.setDescription(editFixedItemGrid.getDescription());
						obj.setUnitPrice(editFixedItemGrid.getUnitPrice());
						obj.setOtherCharges(editFixedItemGrid.getOtherCharges());
						obj.setAmount(editFixedItemGrid.getAmount());
						obj.setQuantity(editFixedItemGrid.getQuantity());
						lstCheckFAItems.add(obj);
					}

					if(itemsFixedAssetGrid.size()==0)
					{
						lstCheckFAItems=new ArrayList<CheckFAItemsModel>();
						CheckFAItemsModel objFAItems=new CheckFAItemsModel();
						objFAItems.setLineNo(1);
						objFAItems.setQuantity(1);
						lstCheckFAItems.add(objFAItems);
					}

					setLabelCheckItems();
					getNewTotalAmount();
					setLabelExpanseItems();
					setLabelFaItems();
					BindUtils.postNotifyChange(null, null, CreditBillViewModel.this, "objCheque");

				}
			}
			else
			{
				Messagebox.show("You can only copy a existing Bill","Bill", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CreditBillViewModel ----> copyFunctinality", ex);		
		}
	}

	public String getLabelStatus() {
		return labelStatus;
	}

	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
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
			newTab.setLabel("Item Receipt Report");
			newTab.setClosable(true);
			Tabpanel newTabpanel = new Tabpanel();
			Include incContentPage = new Include();
			incContentPage.setSrc("/hba/report/itemReceiptReport.zul");
			incContentPage.setParent(newTabpanel);
			newTabpanel.setParent(contentTabpanels);
			newTab.setParent(contentTabs);
			newTab.setSelected(true);
			newTab.setVflex("1");
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CreditBillViewModel ----> goToRelatedReport", ex);			
		}
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
				Chunk c = new Chunk("Bill (Credit Purchases)");
				c.setUnderline(0.1f, -2f);
				c.setFont(f1);
				Paragraph p = new Paragraph(c);

				firsttbl.addCell(p);

				PdfPCell cell1 = new PdfPCell(new Phrase("Date :"+sdf.format(creationdate)+"\n\n"+"Invoice Number :"+objCheque.getBillNo()));
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

				cell1 = new PdfPCell(new Phrase("M/S : "+selectedPaytoOrder.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase(objCheque.getAddress()));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				tbl1.addCell(cell1);
				document.add(tbl1);

				/*---------------------------------------------------------------*/ 

				paragraph = new Paragraph();
				paragraph.setSpacingAfter(5);
				document.add(paragraph);
				
				paragraph=new Paragraph();
				Chunk chunk = new Chunk("Memo : "+objCheque.getMemo());
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);

				paragraph = new Paragraph();
				paragraph.setSpacingAfter(5);
				document.add(paragraph);



			
				PdfPTable table;
				PdfPCell HeadderProduct;
				PdfPCell HeadderDate;
				PdfPCell HeadderRate;
				PdfPCell HeadderAmount1;
				PdfPCell HeadderQty;
				BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
				Font f;

				if (lstExpenses.size() > 0
						&& lstExpenses.get(0).getSelectedAccount() != null) {
					f = new Font(FontFamily.TIMES_ROMAN, 18.0f, Font.NORMAL,
							BaseColor.BLACK);
					c = new Chunk("Expenses",FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					c.setUnderline(0.1f, -2f);
					c.setFont(f);
					p = new Paragraph(c);
					document.add(p);

					table = new PdfPTable(3);
					table.setSpacingBefore(10);
					table.setWidthPercentage(100);
					table.setWidths(new int[] { 150, 240, 60 });
					table.getDefaultCell().setPadding(3);

					HeadderProduct = new PdfPCell(new Phrase(
							"Account No. & Name"));
					HeadderProduct.setBorder(Rectangle.NO_BORDER);
					HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);

					HeadderProduct.setBackgroundColor(myColor);
					table.addCell(HeadderProduct);
					table.setHeaderRows(1);

					HeadderDate = new PdfPCell(new Phrase("Description"));
					HeadderDate.setBorder(Rectangle.NO_BORDER);
					HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderDate.setBackgroundColor(myColor);
					table.addCell(HeadderDate);

					HeadderAmount1 = new PdfPCell(new Phrase("Amount"));
					HeadderAmount1.setBorder(Rectangle.NO_BORDER);
					HeadderAmount1.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderAmount1.setBackgroundColor(myColor);
					table.addCell(HeadderAmount1);

					for (ExpensesModel item : lstExpenses) {

						table.addCell(item.getSelectedAccount()
								.getAccountName());

						if (item.getMemo() != null
								&& !item.getMemo().equalsIgnoreCase(""))
							table.addCell(item.getMemo());
						else
							table.addCell("-");
						
						if(item.getAmount()>0){
							String amtStr1=BigDecimal.valueOf(item.getAmount()).toPlainString();
							double amtDbbl1=Double.parseDouble(amtStr1);
							HeadderAmount1 = new PdfPCell(new Phrase(""+formatter.format(BigDecimal.valueOf(amtDbbl1))));
							HeadderAmount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(HeadderAmount1);
						}else{
							table.addCell("");
						}
	
					}

					for (PdfPRow r : table.getRows()) {
						for (PdfPCell c1 : r.getCells()) {
							c1.setBorder(Rectangle.NO_BORDER);
						}
					}

					document.add(table);
				}
				if (lstCheckItems.size() > 0
						&& lstCheckItems.get(0).getSelectedItems() != null) {
					f = new Font(FontFamily.TIMES_ROMAN, 18.0f, Font.NORMAL,
							BaseColor.BLACK);
					c = new Chunk("Items",FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					c.setUnderline(0.1f, -2f);
					c.setFont(f);
					p = new Paragraph(c);
					document.add(p);

					table = new PdfPTable(5);
					table.setSpacingBefore(10);
					table.setWidthPercentage(100);
					table.setWidths(new int[] { 75, 195, 60, 60, 60 });
					table.getDefaultCell().setPadding(3);

					HeadderProduct = new PdfPCell(new Phrase("Item"));
					HeadderProduct.setBorder(Rectangle.NO_BORDER);
					HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderProduct.setBackgroundColor(myColor);
					table.addCell(HeadderProduct);
					table.setHeaderRows(1);

					HeadderDate = new PdfPCell(new Phrase("Description"));
					HeadderDate.setBorder(Rectangle.NO_BORDER);
					HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderDate.setBackgroundColor(myColor);
					table.addCell(HeadderDate);

					/*
					 * HeadderRate = new PdfPCell(new Phrase("Location"));
					 * HeadderRate.setBorder(Rectangle.NO_BORDER);
					 * HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
					 * HeadderRate.setBackgroundColor(myColor);
					 * table.addCell(HeadderRate);
					 */

					HeadderQty = new PdfPCell(new Phrase("Qty"));
					HeadderQty.setBorder(Rectangle.NO_BORDER);
					HeadderQty.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderQty.setBackgroundColor(myColor);
					table.addCell(HeadderQty);

					PdfPCell HeadderCost = new PdfPCell(new Phrase("Cost"));
					HeadderCost.setBorder(Rectangle.NO_BORDER);
					HeadderCost.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderCost.setBackgroundColor(myColor);
					table.addCell(HeadderCost);

					HeadderAmount1 = new PdfPCell(new Phrase("Total"));
					HeadderAmount1.setBorder(Rectangle.NO_BORDER);
					HeadderAmount1.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderAmount1.setBackgroundColor(myColor);
					table.addCell(HeadderAmount1);

					for (CheckItemsModel item : lstCheckItems) {
						if (item.getSelectedItems() != null) {

							table.addCell(item.getSelectedItems().getRecNo() + "");
							if (item.getDescription() != null
									&& !item.getDescription().equalsIgnoreCase(
											""))
								table.addCell(item.getDescription());
							else
								table.addCell("");

							/* table.addCell(""); */

							if (item.getQuantity() != 0) {
								String amtStr1 = BigDecimal.valueOf(
										item.getQuantity()).toPlainString();
								double amtDbbl1 = Double.parseDouble(amtStr1);

								table.addCell(""
										+ formatter.format(BigDecimal
												.valueOf(amtDbbl1)));
							} else
								table.addCell("");

							if (item.getCost() != 0) {
								String amtStr1 = BigDecimal.valueOf(
										item.getCost()).toPlainString();
								double amtDbbl1 = Double.parseDouble(amtStr1);
								table.addCell(""
										+ formatter.format(BigDecimal
												.valueOf(amtDbbl1)));
							} else
								table.addCell("");

							if(item.getAmount()>0){
								String amtStr1=BigDecimal.valueOf(item.getAmount()).toPlainString();
								double amtDbbl1=Double.parseDouble(amtStr1);
								HeadderAmount1 = new PdfPCell(new Phrase(""+formatter.format(BigDecimal.valueOf(amtDbbl1))));
								HeadderAmount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
								table.addCell(HeadderAmount1);
							}else{
								table.addCell("");
							}
						}
					}
					for (PdfPRow r : table.getRows()) {
						for (PdfPCell c1 : r.getCells()) {
							c1.setBorder(Rectangle.NO_BORDER);
						}
					}
					document.add(table);
				}

				if (lstCheckFAItems.size() > 0
						&& lstCheckFAItems.get(0).getSelectedFixedAsset() != null) {
					f = new Font(FontFamily.TIMES_ROMAN, 18.0f, Font.NORMAL,
							BaseColor.BLACK);
					c = new Chunk("Fixed Assets",FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					c.setUnderline(0.1f, -2f);
					c.setFont(f);
					p = new Paragraph(c);
					document.add(p);

					table = new PdfPTable(5);
					table.setSpacingBefore(10);
					table.setWidthPercentage(100);
					table.setWidths(new int[] { 75, 195, 60, 60, 60 });
					table.getDefaultCell().setPadding(3);

					HeadderProduct = new PdfPCell(new Phrase("Asset code"));
					HeadderProduct.setBorder(Rectangle.NO_BORDER);
					HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderProduct.setBackgroundColor(myColor);
					table.addCell(HeadderProduct);
					table.setHeaderRows(1);

					HeadderDate = new PdfPCell(new Phrase("Description"));
					HeadderDate.setBorder(Rectangle.NO_BORDER);
					HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderDate.setBackgroundColor(myColor);
					table.addCell(HeadderDate);

					/*
					 * HeadderRate = new PdfPCell(new Phrase(""));
					 * HeadderRate.setBorder(Rectangle.NO_BORDER);
					 * HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
					 * HeadderRate.setBackgroundColor(myColor);
					 * table.addCell(HeadderRate);
					 */

					HeadderQty = new PdfPCell(new Phrase("Qty"));
					HeadderQty.setBorder(Rectangle.NO_BORDER);
					HeadderQty.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderQty.setBackgroundColor(myColor);
					table.addCell(HeadderQty);

					PdfPCell HeadderCost = new PdfPCell(new Phrase("Cost"));
					HeadderCost.setBorder(Rectangle.NO_BORDER);
					HeadderCost.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderCost.setBackgroundColor(myColor);
					table.addCell(HeadderCost);

					HeadderAmount1 = new PdfPCell(new Phrase("Total"));
					HeadderAmount1.setBorder(Rectangle.NO_BORDER);
					HeadderAmount1.setHorizontalAlignment(Element.ALIGN_LEFT);
					HeadderAmount1.setBackgroundColor(myColor);
					table.addCell(HeadderAmount1);

					for (CheckFAItemsModel item : lstCheckFAItems) {
						if (item.getSelectedFixedAsset() != null) {

							table.addCell(item.getSelectedFixedAsset()
									.getAssetCode());

							if (item.getDescription() != null
									&& !item.getDescription().equalsIgnoreCase(
											""))
								table.addCell(item.getDescription());
							else
								table.addCell("");

							/* table.addCell(""); */

							if (item.getQuantity() != 0) {
								String amtStr1 = BigDecimal.valueOf(
										item.getQuantity()).toPlainString();
								double amtDbbl1 = Double.parseDouble(amtStr1);
								table.addCell(""
										+ formatter.format(BigDecimal
												.valueOf(amtDbbl1)));
							} else
								table.addCell("");

							if (item.getUnitPrice() != 0) {
								String amtStr1 = BigDecimal.valueOf(
										item.getUnitPrice()).toPlainString();
								double amtDbbl1 = Double.parseDouble(amtStr1);
								table.addCell(""
										+ formatter.format(BigDecimal
												.valueOf(amtDbbl1)));
							} else
								table.addCell("");

							if(item.getAmount()>0){
								String amtStr1=BigDecimal.valueOf(item.getAmount()).toPlainString();
								double amtDbbl1=Double.parseDouble(amtStr1);
								HeadderAmount1 = new PdfPCell(new Phrase(""+formatter.format(BigDecimal.valueOf(amtDbbl1))));
								HeadderAmount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
								table.addCell(HeadderAmount1);
							}else{
								table.addCell("");
							}
						}
					}
					for (PdfPRow r : table.getRows()) {
						for (PdfPCell c1 : r.getCells()) {
							c1.setBorder(Rectangle.NO_BORDER);
						}
					}
					document.add(table);
				}
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );

				PdfPTable totaltbl = new PdfPTable(2);
				totaltbl.setWidthPercentage(100);
				totaltbl.getDefaultCell().setBorder(0);
				totaltbl.setWidths(new int[] { 350, 100 });
				cell1 = new PdfPCell(new Phrase(numbToWord.GetFigToWord(totalAmount),
						FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				cell1.setBackgroundColor(myColor);
				totaltbl.addCell(cell1);
				String amtStr2 = BigDecimal.valueOf(totalAmount)
						.toPlainString();
				double amtDbbl2 = Double.parseDouble(amtStr2);
				cell1 = new PdfPCell(new Phrase("Total :"
						+ formatter.format(amtDbbl2),
						FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				cell1.setBackgroundColor(myColor);
				totaltbl.addCell(cell1);
				document.add(totaltbl);

				if (msgToBeDispalyedOnInvoice != null
						&& msgToBeDispalyedOnInvoice.equalsIgnoreCase("")) {
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);
					document.add(Chunk.NEWLINE);

					paragraph = new Paragraph();
					chunk = new Chunk(msgToBeDispalyedOnInvoice);
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);
				}
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				document.add(Chunk.NEWLINE);
				PdfPTable tb = new PdfPTable(1);
				tb.setWidthPercentage(100);

				cell1 = new PdfPCell();

				Phrase p1 = new Phrase(
						"\nPrepared By :_____________ Checked By :_____________ Approved By :_____________");
				
				cell1.addElement(p1);
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tb.addCell(cell1);
				document.add(tb);

				document.close();
				if (!createPdfSendEmail) {
					previewPdfForprintingInvoice();
				}


			} catch (Exception ex) {
				logger.error("ERROR in CreditBillViewModel ----> createPdfForPrinting", ex);
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
			logger.error("ERROR in CreditBillViewModel ----> previewPdfForprintingInvoice", ex);			
		}
	}


	@Command
	public void closeBill()
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void clearBill()
	{
		if(true)
		{

			Messagebox.show("Are you sure to Clear Bill ? Your Data will be lost.!", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
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
						"ERROR in CreditBillViewModel class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in CreditBillViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in CreditBillViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in CreditBillViewModel class HeaderFooter PDf----> onEndPage",
						e);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail" })
	public void CustomerSendEmail(@BindingParam("row") final CreditBillModel row) {
		if (validateData(false)) {
			lstAtt = new ArrayList<QuotationAttachmentModel>();
			selectedAttchemnets.setFilename(selectedPaytoOrder.getFullName()
					+ " Bill.pdf");
			selectedAttchemnets
			.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Bill?",
					"Credit Bill", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
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
							arg.put("id", row.getVendRefKey());
							arg.put("lstAtt", lstAtt);
							arg.put("feedBackKey", 0);
							arg.put("formType", "Customer");
							arg.put("type", "OtherForms");
							Executions
							.createComponents(
									"/crm/editCustomerFeedbackSend.zul",
									null, arg);
						} catch (Exception ex) {
							logger.error(
									"ERROR in CreditBillViewModel ----> CustomerSendEmail",
									ex);
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

	public HRData getHrData() {
		return hrData;
	}

	public void setHrData(HRData hrData) {
		this.hrData = hrData;
	}

	public int getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}



}
