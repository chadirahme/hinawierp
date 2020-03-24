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
import model.CheckItemsModel;
import model.ClassModel;
import model.CompSetupModel;
import model.ExpensesModel;
import model.HRListValuesModel;
import model.ItemReceiptModel;
import model.PayToOrderModel;
import model.QbListsModel;
import model.SerialFields;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
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


public class ItemReceiptViewModel 
{
	private Logger logger = Logger.getLogger(ItemReceiptViewModel.class);
	private Date creationdate; 
	private ItemReceiptModel objCash;
	private List<AccountsModel> lstaccounts;
	private AccountsModel selectedAccount;
	private List<QbListsModel> lstPayToOrder;
	private QbListsModel selectedPaytoOrder;

	int tmpUnitKey=0;
	private double totalAmount;
	private boolean chkTobePrinted=true;

	private String lblExpenses;
	private String lblCheckItems;

	DecimalFormat formatter = new DecimalFormat("#,###.00");

	private List<ExpensesModel> lstExpenses;
	private ExpensesModel selectedExpenses;
	private List<AccountsModel> lstGridBankAccounts;
	private List <QbListsModel> lstGridCustomer;
	private  List<ClassModel> lstGridClass;

	//CheckItems
	private List<CheckItemsModel> lstCheckItems;
	private CheckItemsModel selectedCheckItems;
	private List <QbListsModel> lstGridQBItems;



	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	HBAData data=new HBAData();
	private CompSetupModel compSetup;

	private List <QbListsModel> lstInvcCustomerGridInvrtySite;



	private int webUserID=0;


	private String msgToBeDispalyedOnInvoice="";

	private boolean adminUser;

	private int  itemReceiptKey;

	private MenuModel companyRole;

	List<MenuModel> list;

	String actionTYpe;

	private boolean canView=false;

	private boolean canModify=false;

	private boolean canPrint=false;

	private boolean canCreate=false;

	private String labelStatus="";

	private String memo;

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	CompanyData companyData=new CompanyData();
	MenuData menuData=new MenuData();
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private HRData hrData = new HRData();
	private String webUserName="";

	NumberToWord numbToWord = new NumberToWord();
	
	private boolean seeTrasction=false;


	public ItemReceiptViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			actionTYpe="Create";
			canView=companyRole.isCanView();
			canModify=companyRole.isCanModify();
			canPrint=companyRole.isCanPrint();
			canCreate=companyRole.isCanAdd();
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");

				if(adminUser)
				{
					webUserID=0;
				}
				else
				{
					webUserID=dbUser.getUserid();
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
			Calendar c = Calendar.getInstance();			
			//2012-03-31			
			lstaccounts=data.fillBankAccounts("'AccountsPayable'");
			lstPayToOrder=data.fillQbList("'Vendor'");
			objCash=new ItemReceiptModel();
			compSetup=data.GetDefaultSetupInfo();
			lstGridBankAccounts=data.getAccountForExpances();
			lstGridCustomer=data.fillQbList("'Customer'");
			lstGridClass=data.fillClassList("");
			lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
			lstGridQBItems=data.fillQbItemsList();
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			if(itemReceiptKey>0)
			{
				labelStatus="Edit";
				objCash=data.getItemReceiptByID(itemReceiptKey,webUserID,seeTrasction);
				List<ExpensesModel> expenseGrid=data.getExpenseItemReceiptGridDataByID(Integer.toString(itemReceiptKey));
				List<CheckItemsModel> itemsGrid=data.getCheckItemsItemReceiptGridDataByID(itemReceiptKey);
				if(objCash!=null)
				{
					for(AccountsModel apAcounts:lstaccounts)
					{
						if(apAcounts.getRec_No()==objCash.getAccrefKey())
						{
							selectedAccount=apAcounts;
							break;
						}

					}

					for(QbListsModel vendorList:lstPayToOrder)
					{
						if(vendorList.getRecNo()==objCash.getVendorKey())
						{
							selectedPaytoOrder=vendorList;
							break;
						}

					}

					totalAmount=objCash.getAmount();
					creationdate=df.parse(sdf.format(objCash.getIrDate()));
					setSelectedPaytoOrder(selectedPaytoOrder);
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
						obj.setRecNo(editExpensesModel.getRecNo());
						obj.setMemo(editExpensesModel.getMemo());
						obj.setAmount(editExpensesModel.getAmount());
						lstExpenses.add(obj);
					}

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
						obj.setRecNo(editItemsGrid.getRecNo());
						obj.setMemo(editItemsGrid.getMemo());
						obj.setAmount(editItemsGrid.getAmount());
						obj.setBillNo(editItemsGrid.getBillNo());
						obj.setQuantity(editItemsGrid.getQuantity());
						if(editItemsGrid.getInvoiceDate()!=null)
							obj.setInvoiceDate(df.parse(sdf.format(editItemsGrid.getInvoiceDate())));
						lstCheckItems.add(obj);
					}

				}
				lblExpenses="Expenses 0.00";
				lblCheckItems="Items 0.00";


			}
			else
			{
				labelStatus="Create";
				creationdate=df.parse(sdf.format(c.getTime()));
				objCash.setIrDate(df.parse(sdf.format(c.getTime())));	
				objCash.setRecNo(0);
				memo="";
				ClearData();
				lstExpenses=new ArrayList<ExpensesModel>();
				ExpensesModel objExp=new ExpensesModel();
				objExp.setSrNO(1);
				lstExpenses.add(objExp);			
				lblExpenses="Expenses 0.00";
				lstCheckItems=new ArrayList<CheckItemsModel>();
				CheckItemsModel objItems=new CheckItemsModel();
				objItems.setLineNo(1);
				objItems.setInvoiceDate(creationdate);
				objItems.setQuantity(1);
				lstCheckItems.add(objItems);
				lblCheckItems="Items 0.00";
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptViewModel ----> init", ex);			
		}
	}

	private void ClearData()
	{
		if(compSetup.getPvSerialNos().equals("S"))
		{
			objCash.setIrNo(data.GetSaleNumber(SerialFields.ItemReceipt.toString()));
			objCash.setIrNoLocal(data.GetSaleNumber(SerialFields.ItemReceiptLocal.toString()));
		}
	}
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==310)
			{
				companyRole=item;
				break;
			}
		}
	}


	/*********Contorl Events********************/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"totalAmount","lblExpenses","lstExpenses"})
	public void selectExpenseAccount(@BindingParam("type")  final ExpensesModel type)
	{
		if(type.getSelectedAccount()!=null)
		{
			//check if account has sub account		
			boolean hasSubAccount=data.checkIfBankAccountsHasSub(type.getSelectedAccount().getName()+":");
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
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstExpenses");
							}
						}

					});


				}
				else
				{
					Messagebox.show("Selected account have sub accounts. You cannot continue !!","Account", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedAccount(null);	
					BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstExpenses");
				}				
			}					

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
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstExpenses");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedClass(null);	
					BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstExpenses");
				}	
			}
		}
	}

	//Check Items Grid
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCheckItems","lblCheckItems","totalAmount","hideSite"})
	public void selectCheckItems(@BindingParam("type") final CheckItemsModel type)
	{
		if(type.getSelectedItems()!=null)
		{
			if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && (type.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || type.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")))
			{
				type.setHideSite(true);
			}
			else
			{
				type.setHideSite(false);
			}

			boolean hasSubAccount=data.checkIfItemHasSubQuery(type.getSelectedItems().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Item have sub Sub Items(s). Do you want to continue?","Item", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	
								QbListsModel objItems=data.getQbItemsData(type.getSelectedItems().getRecNo());
								if(objItems!=null)
								{
									type.setCost(objItems.getPurchaseCost());
									type.setDescription(objItems.getPurchaseDesc());
									type.setAmount(type.getCost() * type.getQuantity());
									type.setSelectedClass(null);

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
							}
							else 
							{		 

								Map args = new HashMap();
								args.put("result", "3");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedItems(null);
								type.setCost(0);
								type.setSelectedClass(null);
								type.setAmount(0);
								type.setDescription("");
								type.setSelectedCustomer(null);
								setLabelCheckItems();
								getNewTotalAmount();
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstCheckItems");
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lblCheckItems");
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "totalAmount");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Items", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedItems(null);
					type.setCost(0);
					type.setSelectedClass(null);
					type.setAmount(0);
					type.setDescription("");
					type.setSelectedCustomer(null);
					setLabelCheckItems();
					getNewTotalAmount();
					BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstCheckItems");
					BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lblCheckItems");
					BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "totalAmount");

				}	
			}
			else
			{
				QbListsModel objItems=data.getQbItemsData(type.getSelectedItems().getRecNo());
				if(objItems!=null)
				{
					type.setCost(objItems.getPurchaseCost());
					type.setDescription(objItems.getPurchaseDesc());
					type.setAmount(type.getCost() * type.getQuantity());
					type.setSelectedClass(null);

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
					BindUtils.postGlobalCommand(null, null, "resetItemsGrid", null);
					return;
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
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstCheckItems");
							}
						}

					});


				}
			}

			catch (Exception e) {
				logger.info("error at ItemReceiptViewModel changeCheckItemsDate>>>" + e.getMessage());
			}

		}
	}
	@GlobalCommand("resetItemsGrid")
	@NotifyChange({"lstCheckItems"})
	public void resetItemsGrid(@BindingParam("result") String result)
	{		  
		//Messagebox.show(" @GlobalCommand>> resetItemsGrid " +result);
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
								BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstCheckItems");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedClass(null);
					BindUtils.postNotifyChange(null, null, ItemReceiptViewModel.this, "lstCheckItems");
				}	
			}
		}
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


		totalAmount=ExpAmount+toalCheckItemsAmount;		
	}

	/***********Grid Context Menu****************/
	@Command
	@NotifyChange({"lstExpenses"})
	public void insertExpense(@BindingParam("row") ExpensesModel row)
	{
		if(selectedExpenses!=null)
		{
			ExpensesModel lastItem=lstExpenses.get(lstExpenses.size()-1);
			if(lastItem.getSelectedAccount()!=null)
			{					

				ExpensesModel obj=new ExpensesModel();
				obj.setSrNO(lstExpenses.size()+1);
				lstExpenses.add(obj);

			}
			else
			{
				Messagebox.show("To add new record,First select Account from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			}
		}
		else
		{
			Messagebox.show("To add new record,First select values Account the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
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
			objItems.setInvoiceDate(creationdate);
			lstCheckItems.add(objItems);
		}
		setLabelCheckItems();
		getNewTotalAmount();
	}

	@Command
	@NotifyChange({"lstCheckItems"})
	public void insertCheckItems(@BindingParam("row") CheckItemsModel row)
	{
		/*		if(selectedCheckItems!=null)
		{


			CheckItemsModel lastItem=lstCheckItems.get(lstCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{					
					if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null))
					{
						Messagebox.show("To add new record,First select Site Name from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
					}
					else
					{
							CheckItemsModel obj=new CheckItemsModel();
							obj.setLineNo(lstCheckItems.size()+1);
							obj.setQuantity(1);
							lstCheckItems.add(obj);
					}
			}
			else
			{
				Messagebox.show("To add new record,First select Item from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			}

		}
		else
		{
			Messagebox.show("To add new record,First select Item from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
		}
		 */


		if(selectedCheckItems!=null)
		{

			CheckItemsModel lastItem=lstCheckItems.get(lstCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{					
				CheckItemsModel obj=new CheckItemsModel();
				obj.setLineNo(lstCheckItems.size()+1);
				obj.setQuantity(1);
				lstCheckItems.add(obj);
			}
			else
			{
				Messagebox.show("To add new record,First select Item from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return;
			}

		}

	}



	@Command  
	public void addItemReceipt()
	{
		if(validateData(true))
		{
			saveData();
		}
	}

	private boolean validateData(boolean Printflag)
	{
		boolean isValid=true;

		if(selectedAccount==null)
		{		
			Messagebox.show("You Must Assign an A/P Account For This Transaction!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
		if(selectedPaytoOrder==null)
		{		
			Messagebox.show("You Must Select A 'Vendor ' !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedPaytoOrder.getRecNo()==0)
		{			
			Messagebox.show("Select An Existing 'Vendor' !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
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
						Messagebox.show("Enter The Amount!!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}

				}
			}

		}

		/*if(lstCheckItems!=null)
		{	
			for(CheckItemsModel gridData:lstCheckItems)
			{
			CheckItemsModel lastItem=gridData;
			if(lastItem.getSelectedItems()!=null)
			{					
				if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null))
				{
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			}
			else{

				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			CheckItemsModel objExp=gridData;
			if(objExp.getSelectedItems()!=null)
			{
				if(objExp.getQuantity()==0)
				{	 		
					Messagebox.show("Please Enter The Qantity,Empty Transaction is not allowed !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
	 			return false;
				}

			}
			}

		}*/


		boolean RateValidation=false;
		boolean siteValidation=false;
		if(lstCheckItems!=null)
		{	
			CheckItemsModel lastItem=lstCheckItems.get(lstCheckItems.size()-1);
			if(lastItem.getSelectedItems()!=null)
			{		
				if((lastItem.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || lastItem.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (lastItem.getSelectedInvcCutomerGridInvrtySiteNew()==null || lastItem.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0))
				{
					Messagebox.show("Since Item Type is Inventory Please select Site Name for the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}
			}
			else{
				Messagebox.show("You Connot Continue.!Please select the Item from the grid","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			for(CheckItemsModel gridData:lstCheckItems)
			{
				CheckItemsModel objExp=gridData;
				if(objExp.getCost()>0)
				{
					RateValidation=true;
					break;
				}
				else
				{
					RateValidation=false;
				}

			}
			for(CheckItemsModel gridData:lstCheckItems)
			{
				CheckItemsModel objExp=gridData;
				if((compSetup.getAllowToAddInventorySite()!=null && compSetup.getAllowToAddInventorySite().equalsIgnoreCase("Y")) && ((objExp.getSelectedItems().getListType().equalsIgnoreCase("InventoryItem") || objExp.getSelectedItems().getListType().equalsIgnoreCase("Inventory Assembly")) && (objExp.getSelectedInvcCutomerGridInvrtySiteNew()==null || objExp.getSelectedInvcCutomerGridInvrtySiteNew().getRecNo()==0)))
				{
					siteValidation=true;
					break;

				}
				else
				{
					siteValidation=false;
				}
			}
			for(CheckItemsModel gridData:lstCheckItems)
			{
				CheckItemsModel objExp=gridData;
				if(objExp.getQuantity()==0)
				{	 		
					Messagebox.show("Please Enter The Quantity,Empty Transaction is not allowed !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}

			}

			if(!RateValidation)
			{
				Messagebox.show("Please Enter The Cost value ,the Item Receipt  amount cannot be zero,Empty Transaction is not allowed !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}

			if(siteValidation)
			{
				Messagebox.show("To Save this record,First select Site Name from the existing records in the Grid!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}



		if(Printflag)
		{
			if((objCash.getIrNo()!=null) && (data.checkIfSerialNumberIsDuplicateForItemReceiptIrNo(objCash.getIrNo(),objCash.getRecNo())==true))
			{
				Messagebox.show("Duplicate Item Receipt Number!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
			if((objCash.getIrNo()!=null) && (data.checkIfSerialNumberIsDuplicateForItemReceiptIrNumberLocal(objCash.getIrNoLocal(),objCash.getRecNo())==true))
			{
				Messagebox.show("Duplicate Item Receipt Local Number!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
			if(objCash.getIrNo()==null)
			{
				Messagebox.show("Please Enter Item Receipt Number","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}

		/*int tmpFindRecNo=0;
		if(compSetup.getPvSerialNos().equals("S"))
		{
			tmpFindRecNo=data.FindTxnNumber(SerialFields.CashPV,String.valueOf(objCash.getRecNo()),"");
		}
		if(tmpFindRecNo>0)
		{
			Messagebox.show("Already Entered PVNO!!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);		
			return false;
		}
		 */
		if(totalAmount==0 || totalAmount<0)
		{			 
			Messagebox.show("Empty or negative amount is not allowed!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		return isValid;

	}

	private void saveData() 
	{	      
		try
		{			
			int tmpRecNo=0;
			if(itemReceiptKey==0)
			{
				tmpRecNo=data.GetNewItemReceiptRecNo();	
			}
			else
			{
				tmpRecNo=objCash.getRecNo();
			}
			ItemReceiptModel obj=new ItemReceiptModel();
			obj.setRecNo(tmpRecNo);
			obj.setTxnID("0");
			obj.setIrNo(objCash.getIrNo());
			obj.setIrNoLocal(objCash.getIrNoLocal());
			obj.setIrDate(creationdate);//(objCheque.getPvDate());
			obj.setVendorKey(selectedPaytoOrder.getRecNo());
			obj.setPrintName(selectedPaytoOrder.getFullName());
			obj.setAmount(totalAmount);
			obj.setStatus("P");
			obj.setAccrefKey(selectedAccount.getRec_No());
			if(null!=memo)
			{
				obj.setMemo(memo);
			}
			else
			{
				obj.setMemo("");
			}
			obj.setAmount(totalAmount);
			obj.setStatus("C");
			obj.setItemClassHide("Y");
			obj.setItemDesHide("Y");
			obj.setBillNoHide("Y");
			obj.setBillDateHide("Y");
			obj.setQbStatus("N");
			obj.setIrsource("CMS");
			obj.setWebUserId(webUserID);
			int result=0;
			if(itemReceiptKey==0)
			{
				result=data.addNewItemReceipt(obj);
			}else
			{
				result=data.updateExistingItemReceipt(obj);
			}
			if(result>0)
			{
				if(itemReceiptKey==0)//Only on create
				{
					data.ConfigSerialNumberCashInvoice(SerialFields.ItemReceipt, obj.getIrNo(),0);
					data.ConfigSerialNumberCashInvoice(SerialFields.ItemReceiptLocal, obj.getIrNo(),0);
				}
				data.deleteCheckItemsItemReceipt(tmpRecNo);
				for (CheckItemsModel item : lstCheckItems) 
				{
					if(item.getSelectedItems()!=null)
						data.addCheckItemsItemReceipt(item, tmpRecNo);	
				}
				data.deleteExpenseItemReceipt(tmpRecNo);
				for (ExpensesModel item : lstExpenses) 
				{
					if(item.getSelectedAccount()!=null)
						data.addExpenseItemReceipt(item, tmpRecNo);	
				}
				if(itemReceiptKey==0)
				{
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.ItemReceipt.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getIrNo(), obj.getIrDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
				}else
				{
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.ItemReceipt.getValue(), (int)obj.getRecNo(), obj.getMemo(), obj.getIrNo(), obj.getIrDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue());
				}
			}



			if(itemReceiptKey>0)
			{
				Clients.showNotification("The Item  Receipt Has Been Updated Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
			}
			else
			{
				Clients.showNotification("The Item  Receipt Has Been Created Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
			}



		}catch (Exception ex) 
		{
			logger.error("ERROR in ItemReceiptViewModel ----> Save", ex);		
		}
	}


	/*******************Getter and Setter********************/
	public ItemReceiptModel getObjCash() {
		return objCash;
	}

	public void setObjCash(ItemReceiptModel objCash) {
		this.objCash = objCash;
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

	public void setSelectedAccount(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	public List <QbListsModel> getLstPayToOrder() {
		return lstPayToOrder;
	}

	public void setLstPayToOrder(List <QbListsModel> lstPayToOrder) {
		this.lstPayToOrder = lstPayToOrder;
	}

	public QbListsModel getSelectedPaytoOrder() {
		return selectedPaytoOrder;
	}

	@NotifyChange({"objCash","lstVendorFAItems"})
	public void setSelectedPaytoOrder(QbListsModel selectedPaytoOrder) 
	{
		this.selectedPaytoOrder = selectedPaytoOrder;
		if(selectedPaytoOrder!=null)
		{
			PayToOrderModel obj=data.getPayToOrderInfo(selectedPaytoOrder.getListType(), selectedPaytoOrder.getRecNo());		
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
			if(address.equalsIgnoreCase(""))
			{
				address=obj.getFullName();
			}
			objCash.setAddress(address);
		}
		else
		{
			Messagebox.show("Invlaid Name.");			
		}
	}





	@Command
	@NotifyChange({"lblExpenses","lblCheckItems","objCash","itemReceiptKey","memo","creationdate","labelStatus","selectedAccount","selectedPaytoOrder","lectedInvcCutomerSendVia","toatlAmount","lstCheckItems","lstExpenses","totalAmount","tempTotalAmount","actionTYpe"})
	public void navigationItemReceipt(@BindingParam("cmp") String navigation)
	{
		try
		{
			objCash=data.navigationItemReceipt(itemReceiptKey,webUserID,seeTrasction,navigation,actionTYpe);
			lblExpenses="Expenses 0.00";
			lblCheckItems="Items 0.00";
			if(objCash!=null && objCash.getRecNo()>0)
			{
				actionTYpe="edit";
				labelStatus="Edit";
				itemReceiptKey=objCash.getRecNo();
				List<ExpensesModel> expenseGrid=data.getExpenseItemReceiptGridDataByID(Integer.toString(itemReceiptKey));
				List<CheckItemsModel> itemsGrid=data.getCheckItemsItemReceiptGridDataByID(itemReceiptKey);
				for(AccountsModel apAcounts:lstaccounts)
				{
					if(apAcounts.getRec_No()==objCash.getAccrefKey())
					{
						selectedAccount=apAcounts;
						break;
					}

				}

				for(QbListsModel vendorList:lstPayToOrder)
				{
					if(vendorList.getRecNo()==objCash.getVendorKey())
					{
						selectedPaytoOrder=vendorList;
						break;
					}

				}

				totalAmount=objCash.getAmount();
				creationdate=df.parse(sdf.format(objCash.getIrDate()));
				setSelectedPaytoOrder(selectedPaytoOrder);
				memo=objCash.getMemo();
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
					obj.setRecNo(editItemsGrid.getRecNo());
					obj.setMemo(editItemsGrid.getMemo());
					obj.setDescription(editItemsGrid.getDescription());
					obj.setCost(editItemsGrid.getCost());
					obj.setAmount(editItemsGrid.getAmount());
					obj.setBillNo(editItemsGrid.getBillNo());
					obj.setQuantity(editItemsGrid.getQuantity());
					if(editItemsGrid.getInvoiceDate()!=null)
						obj.setInvoiceDate(df.parse(sdf.format(editItemsGrid.getInvoiceDate())));
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
				setLabelCheckItems();
				getNewTotalAmount();
				setLabelExpanseItems();

			}
			else
			{
				actionTYpe="create";
				itemReceiptKey=0;
				labelStatus="Create";
				totalAmount=0;
				Calendar c = Calendar.getInstance();			
				creationdate=df.parse(sdf.format(c.getTime()));
				objCash=new ItemReceiptModel();
				selectedAccount=null;
				selectedPaytoOrder=null;
				memo="";
				objCash.setIrDate(df.parse(sdf.format(c.getTime())));	
				objCash.setRecNo(0);
				ClearData();
				lstExpenses=new ArrayList<ExpensesModel>();
				ExpensesModel objExp=new ExpensesModel();
				objExp.setSrNO(1);
				lstExpenses.add(objExp);			
				lblExpenses="Expenses 0.00";
				lstCheckItems=new ArrayList<CheckItemsModel>();
				CheckItemsModel objItems=new CheckItemsModel();
				objItems.setLineNo(1);
				objItems.setInvoiceDate(creationdate);
				objItems.setQuantity(1);
				lstCheckItems.add(objItems);
				lblCheckItems="Items 0.00";
				setLabelCheckItems();
				getNewTotalAmount();
				setLabelExpanseItems();
			}



		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptViewModel ----> navigationItemReceipt", ex);		
		}
	}


	@Command
	@NotifyChange({"lblExpenses","lblCheckItems","objCash","itemReceiptKey","creationdate","memo","labelStatus","selectedAccount","selectedPaytoOrder","lectedInvcCutomerSendVia","toatlAmount","lstCheckItems","lstExpenses","totalAmount","tempTotalAmount","actionTYpe"})
	public void copyFunctinality()
	{
		try
		{
			if(itemReceiptKey>0)
			{
				objCash=data.getItemReceiptByID(itemReceiptKey,webUserID,seeTrasction);
				lblExpenses="Expenses 0.00";
				lblCheckItems="Items 0.00";
				if(objCash!=null && objCash.getRecNo()>0)
				{
					actionTYpe="edit";
					labelStatus="Copied-Create";
					itemReceiptKey=objCash.getRecNo();
					List<ExpensesModel> expenseGrid=data.getExpenseItemReceiptGridDataByID(Integer.toString(itemReceiptKey));
					List<CheckItemsModel> itemsGrid=data.getCheckItemsItemReceiptGridDataByID(itemReceiptKey);
					for(AccountsModel apAcounts:lstaccounts)
					{
						if(apAcounts.getRec_No()==objCash.getAccrefKey())
						{
							selectedAccount=apAcounts;
							break;
						}

					}

					for(QbListsModel vendorList:lstPayToOrder)
					{
						if(vendorList.getRecNo()==objCash.getVendorKey())
						{
							selectedPaytoOrder=vendorList;
							break;
						}

					}

					totalAmount=objCash.getAmount();
					itemReceiptKey=0;
					Calendar c = Calendar.getInstance();			
					creationdate=df.parse(sdf.format(c.getTime()));
					objCash.setRecNo(0);
					ClearData();
					memo=objCash.getMemo();
					setSelectedPaytoOrder(selectedPaytoOrder);
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
						obj.setRecNo(editExpensesModel.getRecNo());
						obj.setMemo(editExpensesModel.getMemo());
						obj.setAmount(editExpensesModel.getAmount());
						lstExpenses.add(obj);
					}
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
						obj.setRecNo(editItemsGrid.getRecNo());
						obj.setMemo(editItemsGrid.getMemo());
						obj.setDescription(editItemsGrid.getDescription());
						obj.setCost(editItemsGrid.getCost());
						obj.setAmount(editItemsGrid.getAmount());
						obj.setBillNo(editItemsGrid.getBillNo());
						obj.setQuantity(editItemsGrid.getQuantity());
						if(editItemsGrid.getInvoiceDate()!=null)
							obj.setInvoiceDate(df.parse(sdf.format(editItemsGrid.getInvoiceDate())));
						lstCheckItems.add(obj);
					}
					setLabelCheckItems();
					getNewTotalAmount();
					setLabelExpanseItems();

				}
			}
			else
			{
				Messagebox.show("You can only copy a existing Item Receipt.","Item Receipt", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptViewModel ----> copyFunctinality.", ex);		
		}
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public boolean isChkTobePrinted() {
		return chkTobePrinted;
	}

	public void setChkTobePrinted(boolean chkTobePrinted) {
		this.chkTobePrinted = chkTobePrinted;
	}



	public String getLblExpenses() {
		return lblExpenses;
	}

	public void setLblExpenses(String lblExpenses) {
		this.lblExpenses = lblExpenses;
	}

	public String getLblCheckItems() {
		return lblCheckItems;
	}

	public void setLblCheckItems(String lblCheckItems) {
		this.lblCheckItems = lblCheckItems;
	}


	public List<ExpensesModel> getLstExpenses() {
		return lstExpenses;
	}

	public void setLstExpenses(List<ExpensesModel> lstExpenses) {
		this.lstExpenses = lstExpenses;
	}

	public ExpensesModel getSelectedExpenses() {
		return selectedExpenses;
	}

	public void setSelectedExpenses(ExpensesModel selectedExpenses) {
		this.selectedExpenses = selectedExpenses;
	}

	public List<AccountsModel> getLstGridBankAccounts() {
		return lstGridBankAccounts;
	}

	public void setLstGridBankAccounts(List<AccountsModel> lstGridBankAccounts) {
		this.lstGridBankAccounts = lstGridBankAccounts;
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



	public List<CheckItemsModel> getLstCheckItems() {
		return lstCheckItems;
	}

	public void setLstCheckItems(List<CheckItemsModel> lstCheckItems) {
		this.lstCheckItems = lstCheckItems;
	}

	public CheckItemsModel getSelectedCheckItems() {
		return selectedCheckItems;
	}

	public void setSelectedCheckItems(CheckItemsModel selectedCheckItems) {
		this.selectedCheckItems = selectedCheckItems;
	}

	public List <QbListsModel> getLstGridQBItems() {
		return lstGridQBItems;
	}

	public void setLstGridQBItems(List <QbListsModel> lstGridQBItems) {
		this.lstGridQBItems = lstGridQBItems;
	}

	public CompSetupModel getCompSetup() {
		return compSetup;
	}

	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}

	public List<QbListsModel> getLstInvcCustomerGridInvrtySite() {
		return lstInvcCustomerGridInvrtySite;
	}

	public void setLstInvcCustomerGridInvrtySite(
			List<QbListsModel> lstInvcCustomerGridInvrtySite) {
		this.lstInvcCustomerGridInvrtySite = lstInvcCustomerGridInvrtySite;
	}

	public int getTmpUnitKey() {
		return tmpUnitKey;
	}

	public void setTmpUnitKey(int tmpUnitKey) {
		this.tmpUnitKey = tmpUnitKey;
	}

	public int getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}

	public String getMsgToBeDispalyedOnInvoice() {
		return msgToBeDispalyedOnInvoice;
	}

	public void setMsgToBeDispalyedOnInvoice(String msgToBeDispalyedOnInvoice) {
		this.msgToBeDispalyedOnInvoice = msgToBeDispalyedOnInvoice;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public int getItemReceiptKey() {
		return itemReceiptKey;
	}

	public void setItemReceiptKey(int itemReceiptKey) {
		this.itemReceiptKey = itemReceiptKey;
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




	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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
			logger.error("ERROR in ItemReceiptViewModel ----> goToRelatedReport", ex);			
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
				firsttbl.setWidths(new int[] { 180, 100 });
				Font f1 = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
						BaseColor.RED);
				Chunk c = new Chunk("Item Receipt (SRV)");
				c.setUnderline(0.1f, -2f);
				c.setFont(f1);
				Paragraph p = new Paragraph(c);

				firsttbl.addCell(p);

				PdfPCell cell1 = new PdfPCell(new Phrase("Date :"+sdf.format(objCash.getIrDate())+"\n\n"+"Item Receipt No. :"+objCash.getIrNo()));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setColspan(3);
				cell1.disableBorderSide(Rectangle.BOX);
				firsttbl.addCell(cell1);
				document.add(firsttbl);

				/*------------------------------------------------------------------------*/
				PdfPTable tbl1 = new PdfPTable(1);
				tbl1.setWidthPercentage(100);

				cell1 = new PdfPCell(new Phrase("To ,"));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("M/S : "+selectedPaytoOrder.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase(objCash.getAddress()));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				tbl1.addCell(cell1);
				document.add(tbl1);

				/*---------------------------------------------------------------*/
		
				paragraph=new Paragraph();
				Chunk chunk = new Chunk("\nMemo : "+objCash.getMemo());
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

				
				cell1 = new PdfPCell(new Phrase("___________________\n\n  Received By \n  Date:    /    /   2015", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
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
				logger.error("ERROR in ItemReceiptViewModel ----> createPdfForPrinting", ex);
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
			logger.error("ERROR in ItemReceiptViewModel ----> previewPdfForprintingInvoice", ex);			
		}
	}

		

	@Command
	public void closeItemReceipt()
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
						"ERROR in itemReceiptViewModel class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in itemReceiptViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in itemReceiptViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in itemReceiptViewModel class HeaderFooter PDf----> onEndPage",
						e);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail" })
	public void CustomerSendEmail(@BindingParam("row") final ItemReceiptModel row) {
		if (validateData(false)) {
			lstAtt = new ArrayList<QuotationAttachmentModel>();
			selectedAttchemnets.setFilename(selectedPaytoOrder.getFullName()
					+ " Item Receipt.pdf");
			selectedAttchemnets
			.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Item Receipt?",
					"Item Receipt", Messagebox.YES | Messagebox.NO,
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
							arg.put("id", selectedPaytoOrder.getRecNo());
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
									"ERROR in itemReceiptViewModel ----> CustomerSendEmail",
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

	public String getWebUserName() {
		return webUserName;
	}

	public void setWebUserName(String webUserName) {
		this.webUserName = webUserName;
	}

	




}
