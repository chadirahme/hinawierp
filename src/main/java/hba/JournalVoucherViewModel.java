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
import model.BarcodeSettingsModel;
import model.ClassModel;
import model.CompSetupModel;
import model.CutomerSummaryReport;
import model.HRListValuesModel;
import model.JournalVoucherGridData;
import model.JournalVoucherModel;
import model.QbListsModel;
import model.SerialFields;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabs;

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

import setup.users.WebusersModel;
import common.NumberToWord;
import company.CompanyData;

public class JournalVoucherViewModel {
	private Logger logger = Logger.getLogger(JournalVoucherViewModel.class);
	private Date creationdate; 
	private HBAData data=new HBAData();
	private HRData hrData = new HRData();
	private DecimalFormat formatter = new DecimalFormat("#,###.00");
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private NumberToWord numbToWord=new NumberToWord();
	private CompSetupModel compSetup;
	private int webUserID=0;
	private String webUserName="";
	private int journalVoucherKey;
	private MenuModel companyRole;
	private boolean adminUser;
	private List<MenuModel> list;
	private boolean canView=false;
	private boolean canModify=false;
	private boolean canPrint=false;
	private boolean canCreate=false;
	private String labelStatus="";
	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;
	private CompanyData companyData=new CompanyData();
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private BarcodeSettingsModel barcodeSettings;
	private String focusColumnAfterScan="Item"; 
	private ItemsData dataBarc = new ItemsData();
	private MenuData menuData=new MenuData();
	private String actionTYpe="";
	private List <QbListsModel> lstInvcCustomerGridClass;
	private String lblTotalDebit="";
	private String lblTotalCredit="";
	private double totalDebit;
	private double totalCredit;
	private double tempTotalAmount;
	private JournalVoucherModel journalVoucher;
	private JournalVoucherGridData selectedGridItems;
	private List<JournalVoucherGridData>  lstJournalVoucherCheckItems;
	private List<QbListsModel> lstAccountType=new ArrayList<QbListsModel>();
	private QbListsModel selectedAccountType=new QbListsModel();
	private List<AccountsModel> lstAccounts=new ArrayList<AccountsModel>();
	private AccountsModel selectedAccount=new AccountsModel();
	private List<String> lstEntityType=new ArrayList<String>();
	//private QbListsModel selectedEntityType=new QbListsModel();
	private List<QbListsModel> lstName=new ArrayList<QbListsModel>();
	private QbListsModel selectedName=new QbListsModel();
	private List<ClassModel> lstGridClass=new ArrayList<ClassModel>();
	private ClassModel selectedClass=new ClassModel();
	private boolean matchFlag=false;
	Calendar c=Calendar.getInstance();
	
	public JournalVoucherViewModel() {
		try {
			barcodeSettings = dataBarc.fillBarcodeSettings();
			if (barcodeSettings == null) {
				focusColumnAfterScan = "";
			} else {
				focusColumnAfterScan = barcodeSettings.getBarcodeAfterScanGoTo();
			}
			journalVoucherKey=0;
			actionTYpe="Create";
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			canView=companyRole.isCanView();
			canModify=companyRole.isCanModify();
			canPrint=companyRole.isCanPrint();
			canCreate=companyRole.isCanAdd();
			compSetup = data.GetDefaultSetupInfo();
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			lstInvcCustomerGridClass=data.GetMasterData("GridClass");
			lstAccountType=data.getAccountTypeList();
			lstEntityType=data.getEntityType();
			lstGridClass = data.fillClassList("");
			lstAccounts = data.fillBankAccounts("");
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
			journalVoucher=new JournalVoucherModel();
			if(journalVoucherKey>0){

			}else{
				labelStatus="Create";
				Calendar c = Calendar.getInstance();		
				creationdate=df.parse(sdf.format(c.getTime()));
				lblTotalDebit="Total Debit :0.00";
				lblTotalCredit="Total Credit :0.00";
				totalDebit=0.0;
				totalCredit=0.0;
				tempTotalAmount=0.0;
				matchFlag=true;
				journalVoucher.setTxnDate(df.parse(sdf.format(c.getTime())));
				journalVoucher.setTxnNumber(data.GetSaleNumber(SerialFields.JvEntry.toString()));
				lstJournalVoucherCheckItems=new ArrayList<JournalVoucherGridData>();
				JournalVoucherGridData model=new JournalVoucherGridData();
				model.setLine_no(1);
				model.setRec_No(1);
				lstJournalVoucherCheckItems.add(model);
				JournalVoucherGridData model1=new JournalVoucherGridData();
				model1.setLine_no(2);
				model1.setRec_No(2);
				lstJournalVoucherCheckItems.add(model1);
			}



		} catch (Exception e) {
			logger.error("ERROR in JournalVoucherViewModel ----> onLoad", e);
		}

	}
	private void saveData() {
		try{
			int tmpRecNo = 0;
			int result=0;
			JournalVoucherModel model=new JournalVoucherModel();
			if(journalVoucherKey==0){
				tmpRecNo=data.GetNewJournalVoucherRecNo();
			}else{
				tmpRecNo=journalVoucher.getRecno();
			}
			model.setRecno(tmpRecNo);
			model.setTxnNumber(journalVoucher.getTxnNumber());
			model.setTxnDate(journalVoucher.getTxnDate());
			model.setRefNumber(journalVoucher.getTxnNumber());
			model.setTotalAmount(totalDebit);
			model.setNotes(journalVoucher.getNotes());
			if(journalVoucherKey==0){
				result=data.addNewJournalVoucher(model, webUserID);
			}else{
				result=data.updateJournalVoucher(model, webUserID);
			}
			if(result>0){
				if (compSetup.getPvSerialNos().equals("S") && journalVoucherKey == 0) {
					data.ConfigSerialNumberCashInvoice(SerialFields.JvEntry,journalVoucher.getTxnNumber(), 0);
				}
				data.deleteJournalLine(tmpRecNo);
				for (JournalVoucherGridData item : lstJournalVoucherCheckItems) {
					if (item.getSelectedAccount() != null && item.getSelectedAccount().getRec_No() > 0){
						data.addJournalLine(item, tmpRecNo);
						if(item.getEntityRef_Type().equalsIgnoreCase("customer")){
							CutomerSummaryReport cutomerSummaryReport =data.getCutomerTotalBalance(item.getSelectedName().getRecNo(), "Y", false);
							data.updateCustomerBalance(cutomerSummaryReport.getBalance(), item.getSelectedName().getRecNo()); 
						}
					}
				}
				if(journalVoucherKey==0){
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.GeneralJournal.getValue(), (int)model.getRecno(), model.getNotes(), model.getTxnNumber(), model.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
				}else{
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.GeneralJournal.getValue(), (int)model.getRecno(), model.getNotes(), model.getTxnNumber(), model.getTxnDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue());
				}
				if(journalVoucherKey>0)
				{
					Clients.showNotification("The  Journal Voucher  Has Been Updated Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
					Center center = bl.getCenter();
					Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
					tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
				}
				else
				{
					Clients.showNotification("The Journal Voucher Has Been Created Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
					Center center = bl.getCenter();
					Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
					tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
				}
				
			}

			

		}catch (Exception e) {
			logger.error("ERROR in JournalVoucherViewModel ----> saveData", e);
		}
	}

	@Command  
	public void addNewJournalVoucher()
	{
		if(validateData(false))
			saveData();
	}

	@Command
	@NotifyChange({"lstJournalVoucherCheckItems","lblTotalCost","totalAmount","tempTotalAmount"})
	public void insertCheckItems()
	{
		if(selectedGridItems!=null)
		{

			JournalVoucherGridData lastItem=lstJournalVoucherCheckItems.get(lstJournalVoucherCheckItems.size()-1);
			if(lastItem.getSelectedAccount()!=null)
			{					
				JournalVoucherGridData obj=new JournalVoucherGridData();
				obj.setLine_no(lstJournalVoucherCheckItems.size()+1);
				lstJournalVoucherCheckItems.add(obj);
			}
			else
			{
				Messagebox.show("To add new record,First select Account from the existing record!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
				return;
			}

		}

	}


	@Command   
	@NotifyChange({"lstJournalVoucherCheckItems","lblTotalCost","totalAmount","tempTotalAmount"})
	public void deleteCheckItems()
	{
		if(selectedGridItems!=null)
		{
			lstJournalVoucherCheckItems.remove(selectedGridItems);

			int srNo=0;
			for (JournalVoucherGridData item : lstJournalVoucherCheckItems)
			{
				srNo++;
				item.setLine_no(srNo);
			}

		}
		if(lstJournalVoucherCheckItems.size()==0)
		{
			JournalVoucherGridData objItems=new JournalVoucherGridData();
			objItems.setLine_no(lstJournalVoucherCheckItems.size()+1);
			lstJournalVoucherCheckItems.add(objItems);
		}
		getNewTotalAmount();
		setLabelDebit();
		setLabelCredit();
	}

	@NotifyChange({"lblTotalDebit","totalDebit"})
	private void setLabelDebit()
	{
		double toalCheckItemsAmount=0;
		for (JournalVoucherGridData item : lstJournalVoucherCheckItems) 
		{
			toalCheckItemsAmount+=item.getDebit();
		}
		lblTotalDebit="Total Debit : " + BigDecimal.valueOf(toalCheckItemsAmount).toPlainString();
		totalDebit=toalCheckItemsAmount;
		
	}

	@NotifyChange({"lblTotalCredit","totalCredit"})
	private void setLabelCredit()
	{
		double toalCheckItemsAmount=0;
		for (JournalVoucherGridData item : lstJournalVoucherCheckItems) 
		{
			toalCheckItemsAmount+=item.getCredit();
		}
		lblTotalCredit="Total Credit : " + BigDecimal.valueOf(toalCheckItemsAmount).toPlainString();
		totalCredit=toalCheckItemsAmount;
		
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

	@Command
	@NotifyChange({"journalVoucher","actionTYpe","labelStatus","journalVoucherKey","lstJournalVoucherCheckItems","lstAccounts","totalDebit","totalCredit","lblTotalDebit", "lblTotalCredit","matchFlag"})
	public void navigationJournalVoucher(@BindingParam("cmp") String navigation)
	{
		try {
			journalVoucher=data.navigationJournalVoucher(journalVoucherKey, webUserID, navigation, actionTYpe);
			if(journalVoucher!=null && journalVoucher.getRecno()>0){
				//String tmpAccountType="";
				actionTYpe="edit";
				labelStatus="Edit";
				journalVoucherKey=journalVoucher.getRecno();
				List<JournalVoucherGridData> lstjournalLine =data.getJournalVoucherGridDataByID(journalVoucherKey);
				lstJournalVoucherCheckItems=new ArrayList<JournalVoucherGridData>();
				for(JournalVoucherGridData item : lstjournalLine){
					JournalVoucherGridData obj=new JournalVoucherGridData();
					obj.setLine_no(lstJournalVoucherCheckItems.size()+1);
					/*for(QbListsModel gridItem:lstAccountType)
					{
						if(gridItem.getRecNo()==item.getAccountTypeRef_Key())
						{
							obj.setSelectedAccountType(gridItem);
							//tmpAccountType=gridItem.getName();
							break;
						}

					}*/

					//setLstAccounts(lstAccounts);
					//obj.setLstAccounts(lstAccounts);
					for (AccountsModel Account : lstAccounts) {
						if (Account.getRec_No() == item.getAccountRef_Key()) {
							obj.setSelectedAccount(Account);
							break;
						}
					}
					if(item.getdC_Flag().equalsIgnoreCase("D"))
						obj.setDebit(item.getAmount());
					else
						obj.setCredit(item.getAmount());
					obj.setMemo(item.getMemo());
					obj.setEntityRef_Type(item.getEntityRef_Type());
					//lstName = data.fillQbList("'"+item.getEntityRef_Type()+"'");
					//setLstName(lstName);
					obj.setLstName(data.fillQbList("'"+item.getEntityRef_Type()+"'"));
					for(QbListsModel gridItem:obj.getLstName())
					{
						if(gridItem.getRecNo()==item.getEntityRef_Key())
						{
							obj.setSelectedName(gridItem);
							break;
						}
					}
					if (item.getBillable().equalsIgnoreCase("Y")) {
						obj.setBillableChked(true);
						obj.setBillable("Y");
						obj.setShowBillable(true);
					} else {
						obj.setBillableChked(false);
						obj.setBillable("N");
						obj.setShowBillable(false);
					}

					for (ClassModel gridClass : lstGridClass) {
						if (gridClass.getClass_Key() == item.getClassRef_Key()) {
							obj.setSelectedClass(gridClass);
							break;
						}

					}
					lstJournalVoucherCheckItems.add(obj);
				}
				getNewTotalAmount();
				setLabelCredit();
				setLabelDebit();
				if(totalCredit!=totalDebit){
					matchFlag=false ;
				}else{
					matchFlag=true;
				}
			}else{
				labelStatus="Create";
				Calendar c = Calendar.getInstance();		
				creationdate=df.parse(sdf.format(c.getTime()));
				lblTotalDebit="Total Debit :0.00";
				lblTotalCredit="Total Credit :0.00";
				totalDebit=0.0;
				totalCredit=0.0;
				tempTotalAmount=0.0;
				matchFlag=true;
				journalVoucher.setTxnDate(df.parse(sdf.format(c.getTime())));
				journalVoucher.setTxnNumber(data.GetSaleNumber(SerialFields.JvEntry.toString()));
				lstJournalVoucherCheckItems=new ArrayList<JournalVoucherGridData>();
				JournalVoucherGridData model=new JournalVoucherGridData();
				model.setLine_no(1);
				model.setRec_No(1);
				lstJournalVoucherCheckItems.add(model);
				JournalVoucherGridData model1=new JournalVoucherGridData();
				model1.setLine_no(2);
				model1.setRec_No(2);
				lstJournalVoucherCheckItems.add(model1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Command
	@NotifyChange({"lstJournalVoucherCheckItems"})
	public void selectAccount(@BindingParam("type") final JournalVoucherGridData type) {
		if (type.getSelectedAccount() != null) 
		{
			if (type.getSelectedAccount().getAccountType().equals("Expense") || type.getSelectedAccount().getAccountType().equals("CostofGoodsSold") || type.getSelectedAccount().getAccountType().equals("Other Expense") || type.getSelectedAccount().getAccountType().equals("OtherCurrentAsset")) {
				if (type.getSelectedName() != null) {
					type.setBillableChked(true);
				} else {
					type.setBillableChked(false);
				}
			} else {
				if (type.getSelectedName() != null) {
					type.setBillableChked(true);
				} else {
					type.setBillableChked(false);
				}

			}

			boolean hasSubAccount = data.checkIfBankAccountsHasSub(type.getSelectedAccount().getFullName() + ":");
			if (hasSubAccount) 
			{
				if (compSetup.getPostOnMainAccount().equals("Y")) {
					Messagebox.show("Selected account have sub accounts. Do you want to continue?",	"Account", Messagebox.YES | Messagebox.NO,Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {

						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								Map args = new HashMap();
								args.put("result", "1");
								BindUtils.postGlobalCommand(null, null,	"resetGrid", args);
							} else {
								Map args = new HashMap();
								args.put("result", "2");
								BindUtils.postGlobalCommand(null, null,"resetGrid", args);
								type.setSelectedAccount(null);
								BindUtils.postNotifyChange(	null,null,JournalVoucherViewModel.this,"lstExpenses");
								BindUtils
								.postNotifyChange(null,null,JournalVoucherViewModel.this,"totalAmount");
								BindUtils.postNotifyChange(null,null,JournalVoucherViewModel.this,"lblExpenses");
								return;
							}
						}

					});

				} else {
					Messagebox.show("Selected account have sub accounts. You cannot continue !!","Account", Messagebox.OK,Messagebox.INFORMATION);
					type.setSelectedAccount(null);
					BindUtils.postNotifyChange(null, null,JournalVoucherViewModel.this, "lstJournalVoucherCheckItems");
					BindUtils.postNotifyChange(null, null,JournalVoucherViewModel.this, "totalCredit");
					BindUtils.postNotifyChange(null, null,JournalVoucherViewModel.this, "totalDebit");
					return;
				}
			}
			/*else{
				JournalVoucherGridData lst=lstJournalVoucherCheckItems.get(lstJournalVoucherCheckItems.size()-1);
				if(lst.getSelectedAccount()!=null)
				{
					JournalVoucherGridData obj=new JournalVoucherGridData();
					obj.setLine_no(lstJournalVoucherCheckItems.size()+1);
					lstJournalVoucherCheckItems.add(obj);

				}
			}*/

			if ((compSetup != null && compSetup.getUsebillable() != null && compSetup.getUsebillable().equalsIgnoreCase("Y")) && type.getSelectedName()!=null) 
			{
				type.setShowBillable(true);
			}

			if(type.getSelectedName()!=null && type.getEntityRef_Type()!=null)
			{
				if(type.getSelectedAccount().getAccountType().equalsIgnoreCase("AccountsReceivable") && !type.getEntityRef_Type().equalsIgnoreCase("Customer")){
					Messagebox.show("Entity Type Should Be Customer !!","Account", Messagebox.OK,Messagebox.INFORMATION);
					type.setSelectedAccount(null);
					return;
				}

				if(type.getSelectedAccount().getAccountType().equalsIgnoreCase("AccountsPayable") && !type.getEntityRef_Type().equalsIgnoreCase("Vendor")){
					Messagebox.show("Entity Type Should Be Vendor !!","Account", Messagebox.OK,Messagebox.INFORMATION);
					type.setSelectedAccount(null);
					return;
				}
			}			
		}
	}

	@Command
	@NotifyChange({"lstJournalVoucherCheckItems","lblTotalDebit","matchFlag", "lblTotalCredit"})
	public void selectDebit(@BindingParam("type") final JournalVoucherGridData type) {
		if(type.getDebit()!=0){
			type.setdC_Flag("D");
			type.setAmount(type.getDebit());
			type.setCredit(0);
		}
		getNewTotalAmount();
		setLabelDebit();
		setLabelCredit();
		if(totalCredit!=totalDebit){
			matchFlag=false ;
		}else{
			matchFlag=true;
		}
	}

	@Command
	@NotifyChange({"lstJournalVoucherCheckItems", "lblTotalCredit","matchFlag","lblTotalDebit"})
	public void selectCredit(@BindingParam("type") final JournalVoucherGridData type) {
		if(type.getCredit()!=0){
			type.setdC_Flag("C");
			type.setAmount(type.getCredit());
			type.setDebit(0);
		}	
		getNewTotalAmount();
		setLabelCredit();
		setLabelDebit();	
		if(totalCredit!=totalDebit){
			matchFlag=false ;
		}else{
			matchFlag=true;
		}
	}

	@Command
	@NotifyChange({"lstJournalVoucherCheckItems","lstName"})
	public void selectEntity(@BindingParam("type") final JournalVoucherGridData type) {
		if(type.getEntityRef_Type()!=null){
			if(type.getSelectedAccount()!=null && (type.getSelectedAccount().getAccountType().equalsIgnoreCase("AccountsPayable") && !type.getEntityRef_Type().equalsIgnoreCase("Vendor"))){
				Messagebox.show("Entity Type Should Be Vendor !!","Account", Messagebox.OK,Messagebox.INFORMATION);
				type.setEntityRef_Type(null);
				return;
			}

			if(type.getSelectedAccount()!=null && (type.getSelectedAccount().getAccountType().equalsIgnoreCase("AccountsReceivable") && !type.getEntityRef_Type().equalsIgnoreCase("Customer"))){
				Messagebox.show("Entity Type Should Be Customer !!","Account", Messagebox.OK,Messagebox.INFORMATION);
				type.setEntityRef_Type(null);
				return;
			}
			type.setLstName(data.fillQbList("'"+type.getEntityRef_Type()+"'"));
		}

	}

	@Command
	@NotifyChange({"lstJournalVoucherCheckItems"})
	public void selectEntityName(@BindingParam("type") final JournalVoucherGridData type) {
		if(type.getSelectedName()!=null){
			if ((compSetup != null && compSetup.getUsebillable() != null && compSetup.getUsebillable().equalsIgnoreCase("Y")) && getSelectedAccount()!=null) {
				if (type.getSelectedAccount().getAccountType().equals("Expense") || type.getSelectedAccount().getAccountType().equals("CostofGoodsSold") || type.getSelectedAccount().getAccountType().equals("Other Expense") || type.getSelectedAccount().getAccountType().equals("OtherCurrentAsset")) 	
					type.setShowBillable(true);
			}
		}
	}

	private boolean validateData(boolean flag) {
		boolean isValid = true;
		if(journalVoucher.getTxnNumber()!=null){
			if((data.checkJournalVoucherDuplicate(journalVoucher.getTxnNumber(), journalVoucherKey)) && (data.checkRVJournalVoucherDuplicate(journalVoucher.getTxnNumber()))){
				Messagebox.show("Duplicate JV Number!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
		}else{
			Messagebox.show("Entry Number Must Not Be Blank!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false; 
		}
		if(lstJournalVoucherCheckItems!=null){
			for(JournalVoucherGridData item : lstJournalVoucherCheckItems){
				if(item.getSelectedAccount()==null){
					Messagebox.show("Account must enter!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
				if(item.getdC_Flag()!=null){
					if(item.getdC_Flag().equalsIgnoreCase("D")){
						if(item.getDebit()<=0){
							Messagebox.show("Debit or Credit must  enter!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
							return false;
						}
					}
					if(item.getdC_Flag().equalsIgnoreCase("C")){
						if(item.getCredit()<=0){
							Messagebox.show("Debit or Credit must  enter!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
							return false;
						}
					}
				}else{
					Messagebox.show("Debit or Credit must  enter!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
				if(compSetup.getPostJVWithOutName().equalsIgnoreCase("N") && item.getSelectedName()==null){
					Messagebox.show("Please fill all the name fields to continue!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}


			}


		}else{
			Messagebox.show("Please, fill the Transaction","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false; 
		}
		getNewTotalAmount();
		if(totalCredit!=totalDebit){
			Messagebox.show("The Transaction is not in balance. Please make sure the total amount in the debit column equals the total amount in the credit column!","Journal Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false; 
		}

		return isValid;
	}
	@NotifyChange({"totalDebit","totalCredit","matchFlag"})
	private void getNewTotalAmount() {
		totalCredit=0;
		totalDebit=0;

		for (JournalVoucherGridData item : lstJournalVoucherCheckItems) {
			if(item.getdC_Flag().equalsIgnoreCase("D")){
				totalDebit += item.getDebit();
				
			}
			if(item.getdC_Flag().equalsIgnoreCase("C")){
				totalCredit += item.getCredit();
			}

		}
		if(totalCredit!=totalDebit){
			matchFlag=false ;
		}else{
			matchFlag=true;
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
				Chunk c = new Chunk("Journal Voucher");
				c.setUnderline(0.1f, -2f);
				c.setFont(f1);
				Paragraph p = new Paragraph(c);

				firsttbl.addCell(p);

				PdfPCell cell1 = new PdfPCell(new Phrase("Date :"+sdf.format(journalVoucher.getTxnDate())+"\n\n"+"Invoice Number :"+journalVoucher.getTxnNumber()));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setColspan(3);
				cell1.disableBorderSide(Rectangle.BOX);
				firsttbl.addCell(cell1);
				document.add(firsttbl);

				/*------------------------------------------------------------------------*/
				PdfPTable tbl1 = new PdfPTable(1);
				tbl1.setWidthPercentage(100);

				cell1 = new PdfPCell(new Phrase("Memo : "+journalVoucher.getNotes()));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				/*---------------------------------------------------------------*/ 





				paragraph = new Paragraph();
				paragraph.setSpacingAfter(10);
				document.add(paragraph);
				BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
				PdfPTable table = new PdfPTable(4);
				table.setSpacingBefore(10);
				table.setWidthPercentage(100);
				table.setWidths(new int[] {200,200,60,60});
				table.getDefaultCell().setPadding(4);

				PdfPCell HeadderDate = new PdfPCell(new Phrase("Account Name"));
				HeadderDate.setPadding(1);
				HeadderDate.setColspan(1);
				HeadderDate.setBorder(Rectangle.NO_BORDER);
				HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderDate.setBackgroundColor(myColor);
				table.addCell(HeadderDate);

				PdfPCell HeadderQty = new PdfPCell(new Phrase("Description"));
				HeadderQty.setPadding(1);
				HeadderQty.setColspan(1);
				HeadderQty.setBorder(Rectangle.NO_BORDER);
				HeadderQty.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderQty.setBackgroundColor(myColor);
				HeadderQty.setBorderWidth(20.0f);
				table.addCell(HeadderQty);

				PdfPCell HeadderRate = new PdfPCell(new Phrase("Debit"));
				HeadderRate.setPadding(1);
				HeadderRate.setColspan(1);
				HeadderRate.setBorder(Rectangle.NO_BORDER);
				HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderRate.setBackgroundColor(myColor);
				HeadderRate.setBorderWidth(40.0f);
				table.addCell(HeadderRate);

				PdfPCell HeadderAmount = new PdfPCell(new Phrase("Credit"));
				HeadderAmount.setPadding(1);
				HeadderAmount.setColspan(1);
				HeadderAmount.setBorder(Rectangle.NO_BORDER);
				HeadderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderAmount.setBackgroundColor(myColor);
				HeadderAmount.setBorderWidth(40.0f);
				table.addCell(HeadderAmount);


				for (JournalVoucherGridData item : lstJournalVoucherCheckItems) 
				{



					if(item.getSelectedAccount()!=null)
					{
						table.addCell(item.getSelectedAccount().getAccountName());
					}else{
						table.addCell("");
					}
					if(item.getMemo()!=null)
					{
						table.addCell(item.getMemo());
					}else{
						table.addCell("");
					}

					if(item.getDebit()>0){
						String amtStr1=BigDecimal.valueOf(item.getDebit()).toPlainString();
						double amtDbbl1=Double.parseDouble(amtStr1);
						HeadderAmount = new PdfPCell(new Phrase(""+formatter.format(BigDecimal.valueOf(amtDbbl1))));
						HeadderAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(HeadderAmount);
					}else{
						table.addCell("");
					}

					if(item.getCredit()>0){
						String amtStr1=BigDecimal.valueOf(item.getCredit()).toPlainString();
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

				PdfPTable totaltbl = new PdfPTable(2);
				totaltbl.setWidthPercentage(100);
				totaltbl.getDefaultCell().setBorder(0);
				totaltbl.setWidths(new int[]{350,100});
				cell1 = new PdfPCell(new Phrase(numbToWord.GetFigToWord(totalDebit), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setBackgroundColor(myColor);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				totaltbl.addCell(cell1);

				String amtStr1 = BigDecimal.valueOf(totalDebit)
						.toPlainString();
				double amtDbbl1 = Double.parseDouble(amtStr1);
				cell1 = new PdfPCell(new Phrase("Total :" + formatter.format(amtDbbl1), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				cell1.setBackgroundColor(myColor);
				totaltbl.addCell(cell1);
				document.add(totaltbl);




				document.add(new Chunk("\n\n"));

				PdfPTable endPage = new PdfPTable(3);
				endPage.setWidthPercentage(100);
				endPage.getDefaultCell().setBorder(0);
				endPage.setWidths(new int[]{150,150,150});
				cell1 = new PdfPCell(new Phrase("_______________________ \n\n   Accountant", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("_______________________ \n\n   Accountant Manager", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("_______________________ \n\n   General Manager", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				endPage.addCell(cell1);

				document.add(endPage);

				document.close();

				if (!createPdfSendEmail) {
					previewPdfForprintingInvoice();
				}

			} catch (Exception ex) {
				logger.error("ERROR in JournalVoucherViewModel----> createPdfForPrinting", ex);
			}
		}
	}

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
			logger.error("ERROR in JournalVoucherViewModel ----> previewPdfForprintingInvoice", ex);			
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
						"ERROR in CashInvoiceViewModel class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in CashInvoiceViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in CashInvoiceViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in CashInvoiceViewModel class HeaderFooter PDf----> onEndPage",
						e);
			}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail" })
	public void CustomerSendEmail(@BindingParam("row") final JournalVoucherModel row) {
		if (validateData(false)) {
			lstAtt = new ArrayList<QuotationAttachmentModel>();
			selectedAttchemnets.setFilename("journal Voucher.pdf");
			selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The journal Voucher?",	"journal Voucher", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
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
							arg.put("id", 0);
							arg.put("lstAtt", lstAtt);
							arg.put("feedBackKey", 0);
							arg.put("formType", "Customer");
							arg.put("type", "OtherForms");
							Executions.createComponents("/crm/editCustomerFeedbackSend.zul",null, arg);
						} catch (Exception ex) {
							logger.error("ERROR in JournalVoucherViewModel ----> CustomerSendEmail",ex);
						}
					}
				}
			});
		}
	}

	@Command
	public void closeJV()
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



	public HRData getHrData() {
		return hrData;
	}



	public void setHrData(HRData hrData) {
		this.hrData = hrData;
	}



	public DecimalFormat getFormatter() {
		return formatter;
	}



	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
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



	public NumberToWord getNumbToWord() {
		return numbToWord;
	}



	public void setNumbToWord(NumberToWord numbToWord) {
		this.numbToWord = numbToWord;
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



	public int getJournalVoucherKey() {
		return journalVoucherKey;
	}



	public void setJournalVoucherKey(int journalVoucherKey) {
		this.journalVoucherKey = journalVoucherKey;
	}



	public MenuModel getCompanyRole() {
		return companyRole;
	}



	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}



	public boolean isAdminUser() {
		return adminUser;
	}



	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}



	public List<MenuModel> getList() {
		return list;
	}



	public void setList(List<MenuModel> list) {
		this.list = list;
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



	public BarcodeSettingsModel getBarcodeSettings() {
		return barcodeSettings;
	}



	public void setBarcodeSettings(BarcodeSettingsModel barcodeSettings) {
		this.barcodeSettings = barcodeSettings;
	}



	public String getFocusColumnAfterScan() {
		return focusColumnAfterScan;
	}



	public void setFocusColumnAfterScan(String focusColumnAfterScan) {
		this.focusColumnAfterScan = focusColumnAfterScan;
	}



	public ItemsData getDataBarc() {
		return dataBarc;
	}



	public void setDataBarc(ItemsData dataBarc) {
		this.dataBarc = dataBarc;
	}



	public MenuData getMenuData() {
		return menuData;
	}



	public void setMenuData(MenuData menuData) {
		this.menuData = menuData;
	}



	public String getActionTYpe() {
		return actionTYpe;
	}



	public void setActionTYpe(String actionTYpe) {
		this.actionTYpe = actionTYpe;
	}



	public List<QbListsModel> getLstInvcCustomerGridClass() {
		return lstInvcCustomerGridClass;
	}



	public void setLstInvcCustomerGridClass(
			List<QbListsModel> lstInvcCustomerGridClass) {
		this.lstInvcCustomerGridClass = lstInvcCustomerGridClass;
	}



	public String getLblTotalCost() {
		return lblTotalDebit;
	}



	public void setLblTotalCost(String lblTotalCost) {
		this.lblTotalDebit = lblTotalCost;
	}



	public double getTotalAmount() {
		return totalDebit;
	}



	public void setTotalAmount(double totalAmount) {
		this.totalDebit = totalAmount;
	}



	public double getTempTotalAmount() {
		return tempTotalAmount;
	}



	public void setTempTotalAmount(double tempTotalAmount) {
		this.tempTotalAmount = tempTotalAmount;
	}



	public JournalVoucherModel getJournalVoucher() {
		return journalVoucher;
	}



	public void setJournalVoucher(JournalVoucherModel journalVoucher) {
		this.journalVoucher = journalVoucher;
	}

	public JournalVoucherGridData getSelectedGridItems() {
		return selectedGridItems;
	}

	public void setSelectedGridItems(JournalVoucherGridData selectedGridItems) {
		this.selectedGridItems = selectedGridItems;
	}

	public List<JournalVoucherGridData> getLstJournalVoucherCheckItems() {
		return lstJournalVoucherCheckItems;
	}

	public void setLstJournalVoucherCheckItems(
			List<JournalVoucherGridData> lstJournalVoucherCheckItems) {
		this.lstJournalVoucherCheckItems = lstJournalVoucherCheckItems;
	}

	public List<QbListsModel> getLstAccountType() {
		return lstAccountType;
	}

	public void setLstAccountType(List<QbListsModel> lstAccountType) {
		this.lstAccountType = lstAccountType;
	}

	public QbListsModel getSelectedAccountType() {
		return selectedAccountType;
	}


	public void setSelectedAccountType(QbListsModel selectedAccountType) {
		this.selectedAccountType = selectedAccountType;
	}

	public List<AccountsModel> getLstAccounts() {
		return lstAccounts;
	}

	public void setLstAccounts(List<AccountsModel> lstAccounts) {
		this.lstAccounts = lstAccounts;
	}

	public AccountsModel getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public List<String> getLstEntityType() {
		return lstEntityType;
	}

	public void setLstEntityType(List<String> lstEntityType) {
		this.lstEntityType = lstEntityType;
	}

	/*public QbListsModel getSelectedEntityType() {
		return selectedEntityType;
	}


	public void setSelectedEntityType(QbListsModel selectedEntityType) {
		this.selectedEntityType = selectedEntityType;
	}*/

	public List<QbListsModel> getLstName() {
		return lstName;
	}

	public void setLstName(List<QbListsModel> lstName) {
		this.lstName = lstName;
	}

	public QbListsModel getSelectedName() {
		return selectedName;
	}

	public void setSelectedName(QbListsModel selectedName) {
		this.selectedName = selectedName;
	}
	public List<ClassModel> getLstGridClass() {
		return lstGridClass;
	}
	public void setLstGridClass(List<ClassModel> lstGridClass) {
		this.lstGridClass = lstGridClass;
	}
	public ClassModel getSelectedClass() {
		return selectedClass;
	}
	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}
	public double getTotalDebit() {
		return totalDebit;
	}
	public void setTotalDebit(double totalDebit) {
		this.totalDebit = totalDebit;
	}
	public double getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(double totalCredit) {
		this.totalCredit = totalCredit;
	}
	public String getLblTotalDebit() {
		return lblTotalDebit;
	}
	public void setLblTotalDebit(String lblTotalDebit) {
		this.lblTotalDebit = lblTotalDebit;
	}
	public String getLblTotalCredit() {
		return lblTotalCredit;
	}
	public void setLblTotalCredit(String lblTotalCredit) {
		this.lblTotalCredit = lblTotalCredit;
	}
	public boolean isMatchFlag() {
		return matchFlag;
	}
	public void setMatchFlag(boolean matchFlag) {
		this.matchFlag = matchFlag;
	}

		

}
