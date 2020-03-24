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
import model.BanksModel;
import model.ClassModel;
import model.CompSetupModel;
import model.CutomerSummaryReport;
import model.HRListValuesModel;
import model.QbListsModel;
import model.ReceiptVoucherDeatiledModel;
import model.ReceiptVoucherGridData;
import model.ReceiptVoucherMastModel;
import model.SerialFields;
import model.VendorModel;

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

import common.NumberToWord;
import company.CompanyData;

/**
 * @author IQBALMUFASIL
 *
 */
public class ReceiptVoucherViewModel {

	private Logger logger = Logger.getLogger(ReceiptVoucherViewModel.class);
	private Date creationdate; 
	private ReceiptVoucherViewModel objReceiptVoucher;
	private ReceiptVoucherMastModel receiptVoucherMastModel;
	private ReceiptVoucherDeatiledModel receiptVoucherDeatiledModel;
	HBAData data=new HBAData();
	CompanyData companyData=new CompanyData();
	ReceiptVoucherData dataRV=new ReceiptVoucherData();
	DecimalFormat formatter = new DecimalFormat("#,###.00");

	MenuData menuData=new MenuData();
	private String webUserName="";
	private List<QbListsModel> lstReceivedFrom;
	private QbListsModel selectedReceivedFrom;

	private List<String> postToQbBy=new ArrayList<String>();	
	private String selectedPostToQbBy;


	private String printOnReciptVoucher;

	private String amountInWords;

	private String memo="";

	private List<AccountsModel> lstAccountCr;
	private AccountsModel selectedAccountCr;

	private boolean checkRvNO;

	private boolean checkRvDate;

	private double customerBalance;


	//Receipt Voucher Grid 
	private List<ReceiptVoucherGridData> lstRecieptVoucherGrid;

	private ReceiptVoucherGridData selectedReceiptValucherGrid;

	private List <String> lstPyamentMethod;

	private List<BanksModel> lstBankModel;

	private List<AccountsModel> lstDepositeTo;

	private List<ClassModel> lstsClassModel;

	private List<AccountsModel> lstcuc;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");

	private String lblTotalCost;

	private double toatlAmount;

	private CompSetupModel compSetup;

	private String receiptVoucherNo;

	private Date rvDate;

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

	private int  reciptVoiucherKey;

	private boolean makeAsDeferedIncome;

	private boolean makeAsDeferedIncomeVisible;

	private String msgToBeDispalyedOnInvoice="";

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	private Date txtDate;

	NumberToWord numbToWord=new NumberToWord();//get amount in words 

	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();

	private boolean seeTrasction=false;


	@SuppressWarnings({ "unused", "rawtypes" })
	public ReceiptVoucherViewModel() {
		try{
			fillBarCode();
			makeAsDeferedIncomeVisible=false;
			makeAsDeferedIncome=false;
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			reciptVoiucherKey=0;
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
			lstReceivedFrom=dataRV.getRiceivedFrom();
			lstAccountCr=dataRV.getAccountCr();
			lstDepositeTo=dataRV.getGridPaymentMethodCash();
			lstBankModel=dataRV.getGridBankName();
			lstsClassModel=dataRV.getGridClass();
			lstcuc=dataRV.getAcccountForCUG();
			compSetup=data.GetDefaultSetupInfo();
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			if(reciptVoiucherKey>0)
			{
				labelStatus="Edit";
				receiptVoucherMastModel=dataRV.getReceiptVoucherById(reciptVoiucherKey,webUserID,seeTrasction);
				List<ReceiptVoucherDeatiledModel> reciptVoucherGridData=dataRV.getReciptVoucherGridDataByID(reciptVoiucherKey);
				if(receiptVoucherMastModel!=null)
				{
					for(QbListsModel recivedFrom:lstReceivedFrom)
					{
						if(recivedFrom.getRecNo()==receiptVoucherMastModel.getCustRefKey())
						{
							selectedReceivedFrom=recivedFrom;
							break;
						}

					}

					for(AccountsModel accountCr:lstAccountCr)
					{
						if(accountCr.getRec_No()==receiptVoucherMastModel.getArAccountRefKey())
						{
							selectedAccountCr=accountCr;
							if((selectedAccountCr.getAccountType().equalsIgnoreCase("OtherCurrentliability")) || selectedPostToQbBy.equalsIgnoreCase("longtermliability"))
							{
								makeAsDeferedIncomeVisible=true;
							}
							else
							{
								makeAsDeferedIncomeVisible=false;
							}
							break;
						}

					}

					receiptVoucherNo=receiptVoucherMastModel.getRefNumber();
					rvDate=receiptVoucherMastModel.getTxtDate();
					printOnReciptVoucher=receiptVoucherMastModel.getReceiptName();
					memo=receiptVoucherMastModel.getMemo();
					if(receiptVoucherMastModel.getDefferedIncome().equalsIgnoreCase("Y"))
					{
						makeAsDeferedIncome=true;

					}
					else
					{
						makeAsDeferedIncome=false;
					}

					for(String postToQbBYNew:postToQbBy)
					{
						if(postToQbBYNew.equalsIgnoreCase(receiptVoucherMastModel.getRvOrJv()))
						{
							selectedPostToQbBy=postToQbBy.get(0);
							canCreate=false;
							break;
						}
						else
						{
							selectedPostToQbBy=postToQbBy.get(1);
							canCreate=true;
							break;
						}

					}
					lstRecieptVoucherGrid=new ArrayList<ReceiptVoucherGridData>();
					for(ReceiptVoucherDeatiledModel editReciptVoucherGrid:reciptVoucherGridData)
					{
						ReceiptVoucherGridData obj=new ReceiptVoucherGridData();
						obj.setLineNo(lstRecieptVoucherGrid.size()+1);

						for(AccountsModel depositeTo:lstDepositeTo)
						{
							if(depositeTo.getRec_No()==editReciptVoucherGrid.getDepositeToAccountRefKey())
							{
								obj.setSeletedDepositeTo(depositeTo);
								break;
							}

						}

						for(BanksModel banksModel:lstBankModel)
						{
							if(banksModel.getRecno()==editReciptVoucherGrid.getBankKey())
							{
								obj.setSelectedBank(banksModel);
								break;
							}

						}

						for(ClassModel classModel:lstsClassModel)
						{
							if(classModel.getClass_Key()==editReciptVoucherGrid.getClassRefKey())
							{
								obj.setSelectedClass(classModel);
								break;
							}

						}


						for(AccountsModel cucModel:lstcuc)
						{
							if(cucModel.getRec_No()==editReciptVoucherGrid.getCucaccuntKey())
							{
								obj.setSeletedCuc(cucModel);
								break;
							}

						}

						for(String payment:lstPyamentMethod)
						{
							if(payment.equalsIgnoreCase(editReciptVoucherGrid.getPaymentMethod()))
							{
								obj.setSelectedPaymentMethod(payment);
								break;
							}

						}
						obj.setAmount(editReciptVoucherGrid.getAmmount());
						obj.setMemo(editReciptVoucherGrid.getMemo());
						obj.setCheckDate(editReciptVoucherGrid.getCheckDate());
						obj.setChequeNO(editReciptVoucherGrid.getCheckNumber());
						lstRecieptVoucherGrid.add(obj);
					}
					setLabelCheckTotalcost();

				}

			}
			else
			{
				labelStatus="Create";
				checkRvNO=true;
				checkRvDate=true;
				canCreate=true;
				if(compSetup.getrVSerialSetup().equals("S"))
					receiptVoucherNo=dataRV.GetRVNumberFlag1();
				else
					receiptVoucherNo=dataRV.GetRVNumberFlag2();
				Calendar c = Calendar.getInstance();		
				creationdate=df.parse(sdf.format(c.getTime()));
				rvDate=creationdate;
				lstRecieptVoucherGrid=new ArrayList<ReceiptVoucherGridData>();
				ReceiptVoucherGridData objItems=new ReceiptVoucherGridData();
				objItems.setLineNo(1);
				lstRecieptVoucherGrid.add(objItems);
				lblTotalCost="Amount :0.00";
				toatlAmount=0.0;
			}
			amountInWords=numbToWord.GetFigToWord(toatlAmount);

		}
		catch(Exception ex)
		{
			logger.error("ERROR in ReceiptVoucherViewModel ----> onLoad", ex);
		}
	}

	public void fillBarCode()
	{
		postToQbBy=new ArrayList<String>();
		postToQbBy.add("Receipt Voucher");
		postToQbBy.add("Journal Voucher");
		selectedPostToQbBy=postToQbBy.get(0);

		lstPyamentMethod=new ArrayList<String>();
		lstPyamentMethod.add("Cash");
		lstPyamentMethod.add("Cheque");
	}


	private void setLabelCheckTotalcost()
	{
		double toalCheckItemsAmount=0;
		for (ReceiptVoucherGridData item : lstRecieptVoucherGrid) 
		{
			toalCheckItemsAmount+=item.getAmount();
		}
		lblTotalCost="Amount :" + BigDecimal.valueOf(toalCheckItemsAmount).toPlainString();
		toatlAmount=toalCheckItemsAmount;
		amountInWords=numbToWord.GetFigToWord(toatlAmount);
	}

	@Command   
	@NotifyChange({"lstRecieptVoucherGrid","lblTotalCost","toatlAmount","amountInWords"})
	public void deleteCheckItems(@BindingParam("row") ReceiptVoucherGridData row)
	{
		if(selectedReceiptValucherGrid!=null)
		{
			lstRecieptVoucherGrid.remove(selectedReceiptValucherGrid);

			int srNo=0;
			for (ReceiptVoucherGridData item : lstRecieptVoucherGrid)
			{
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if(lstRecieptVoucherGrid.size()==0)
		{
			ReceiptVoucherGridData objItems=new ReceiptVoucherGridData();
			objItems.setLineNo(lstRecieptVoucherGrid.size()+1);
			lstRecieptVoucherGrid.add(objItems);
		}
		setLabelCheckTotalcost();
	}

	@Command
	@NotifyChange({"lstRecieptVoucherGrid","toatlAmount","lblTotalCost","amountInWords"})
	public void insertCheckItems(@BindingParam("row") ReceiptVoucherGridData row)
	{
		if(lstRecieptVoucherGrid!=null)
		{

			ReceiptVoucherGridData lastItem=lstRecieptVoucherGrid.get(lstRecieptVoucherGrid.size()-1);
			if(lastItem!=null && lastItem.getSelectedPaymentMethod()!=null)
			{					
				if((lastItem.getSelectedPaymentMethod().equalsIgnoreCase("") && lastItem.getSelectedPaymentMethod()==null))
				{
					//Messagebox.show("To add new record,First select values to the existing record!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
				}
				else
				{
					ReceiptVoucherGridData obj=new ReceiptVoucherGridData();
					obj.setLineNo(lstRecieptVoucherGrid.size()+1);
					lstRecieptVoucherGrid.add(obj);
				}
			}
			else
			{
				Messagebox.show("To add new record,First select values to the existing record!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			}
		}
		setLabelCheckTotalcost();

	}

	//need to work on these below stuff


	@Command  
	@NotifyChange({"lstRecieptVoucherGrid"})
	public void addNewReceiptVoucher()
	{
		if(validateData(true))
		{
			saveData();
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void closeReceiptVoucher()
	{
		if(true)
		{

			Messagebox.show("Are you sure to Close Receipt Voucher ? Your Data will be lost.!", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onYes")) 
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
	public void clearReceiptVoucher()
	{
		if(true)
		{

			Messagebox.show("Are you sure to Clear Receipt Voucher ? Your Data will be lost.!", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
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
	public void addNewReceiptVoucherClose()
	{
		if(validateData(true))
		{

			Messagebox.show("Are you sure to add Receipt Voucher ?", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
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





	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@NotifyChange({"lstRecieptVoucherGrid"})
	private boolean validateData(boolean Printflag)
	{
		boolean isValid=true;

		if(selectedReceivedFrom==null)
		{		
			Messagebox.show("Select a value for Recived From!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedAccountCr==null)
		{		
			Messagebox.show("Select a value for Account(Cr.)  !","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedPostToQbBy==null || "".equalsIgnoreCase(selectedPostToQbBy))
		{		
			Messagebox.show("Select a value for Post To QB !","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(receiptVoucherNo==null || "".equalsIgnoreCase(receiptVoucherNo))
		{		
			Messagebox.show("Please enter the Reciept Voucher Number  !","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(rvDate==null)
		{		
			Messagebox.show("Please enter the Reciept Voucher Date!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
		if(lstRecieptVoucherGrid.size()<=0)
		{
			Messagebox.show("Please Add Atlest One Record for Reciept Voucher Grid!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
		if(lstRecieptVoucherGrid!=null)
		{	
			for(ReceiptVoucherGridData gridData:lstRecieptVoucherGrid)
			{
				final ReceiptVoucherGridData lastItem=gridData;
				lastItem.setShouldContinue(true);
				if(lastItem.getSelectedPaymentMethod()!=null && lastItem.getSelectedPaymentMethod().equalsIgnoreCase("cheque"))
				{	
					if( lastItem.getCheckDate()==null)
					{
						Messagebox.show("Please enter the cheque Date","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}
					if(lastItem.getChequeNO()==null || lastItem.getChequeNO().equalsIgnoreCase(""))
					{
						Messagebox.show("Please eneter the Cheque Number!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}
					if(lastItem.getSelectedBank()==null)
					{
						Messagebox.show("Please select the Bank Name!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}

				}

				if(lastItem.getSelectedPaymentMethod()==null || "".equalsIgnoreCase(lastItem.getSelectedPaymentMethod()))
				{
					Messagebox.show("Please select the Payment Method value!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}


				if(lastItem.getSeletedDepositeTo()==null)
				{
					Messagebox.show("Please select the DepositeTo value!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}


				if(lastItem.getSeletedDepositeTo()!=null && lastItem.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("Cheque Under Collection"))
				{

					if(lastItem.getSeletedCuc()==null)
					{
						Messagebox.show("Please Select the CUC value!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}

				}

				if(lastItem.getCheckDate()!=null && lastItem.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("Cheque Under Collection"))
				{
					final int i=0;
					if(lastItem.getCheckDate().compareTo(rvDate)<0) 
					{
						Messagebox.show("Cheque Date should be greater than Transaction Date for Cheques Under Collection.! Do you want to continue?","Reciept Voucher", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {			
							public void onEvent(Event evt) throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{	 				        	
									Map args = new HashMap();
									lastItem.setShouldContinue(true);
								}
								else 
								{	
									lastItem.setShouldContinue(false);
									return;
								}

							}

						});
					}

				}

				if(lastItem.isShouldContinue()==false)
				{
					lastItem.setCheckDate(null);
					return false;
				}
			}

		}



		if(selectedAccountCr!=null && selectedReceivedFrom!=null)
		{
			if(selectedReceivedFrom.getListType().equalsIgnoreCase("Customer"))
			{
				if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsPayable"))
				{
					Messagebox.show("Account type should not be AccountsPayable !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
					return false;
				}

			}
			else if(selectedReceivedFrom.getListType().equalsIgnoreCase("Vendor"))
			{
				if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable"))
				{
					Messagebox.show("Account type should not be AccountsReceivable !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
					return false;
				}
			}
			else if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable") || selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsPayable"))
			{
				Messagebox.show("You can't use AccountsReceivable or AccountsPayable Account!","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
				return false;
			}



			if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable"))
			{
				if(selectedReceivedFrom.getListType().equalsIgnoreCase("Vendor"))
				{
					Messagebox.show("AccountType should not  be AccountsReceivable !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
					return false;
				}
			}

			if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsPayable"))
			{
				if(selectedReceivedFrom.getListType().equalsIgnoreCase("Customer"))
				{
					Messagebox.show("AccountType should not  be AccountsPayable !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
					return false;
				}
			}

			if(!selectedReceivedFrom.getListType().equalsIgnoreCase("Customer") && !selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable") && selectedPostToQbBy.equalsIgnoreCase("Receipt Voucher"))
			{
				Messagebox.show("You cannot select Post to Qb as Receipt Voucher !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
				return false;
			}

		}

		if(toatlAmount<=0)
		{
			Messagebox.show("Amount cannot be zero or less ","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
			return false;
		}

		if(Printflag)
		{
			if(compSetup.getrVSerialSetup().equalsIgnoreCase("S"))
			{
				if((receiptVoucherNo!=null) && (dataRV.checkIfSerialNumberIsDuplicateRVfalg1(receiptVoucherNo,reciptVoiucherKey)==true))
				{
					Messagebox.show("Duplicate Rv Number!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
					return false; 
				}

			}
			else if((receiptVoucherNo!=null) && (dataRV.checkIfSerialNumberIsDuplicateRVfalg2(receiptVoucherNo,reciptVoiucherKey)==true))
			{
				Messagebox.show("Duplicate Rv Number!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
		}

		return isValid;

	}


	private void saveData() 
	{	      
		try
		{	
			int tmpRecNo=0;	
			ReceiptVoucherMastModel objMast=new ReceiptVoucherMastModel();
			if(reciptVoiucherKey>0)
			{
				tmpRecNo=reciptVoiucherKey;	
			}
			else
			{
				tmpRecNo=dataRV.GetNewReceiptVoucherRecNo();	
			}
			objMast.setRecNo(tmpRecNo);
			objMast.setArAccountRefKey(selectedAccountCr.getRec_No());
			objMast.setCustRefKey(selectedReceivedFrom.getRecNo());
			objMast.setReceiptName(printOnReciptVoucher);
			objMast.setTxtDate(rvDate);
			objMast.setRefNumber(receiptVoucherNo);
			objMast.setTxnId(null);
			objMast.setTotalAmount(toatlAmount);
			objMast.setMemo(memo);
			if(checkRvDate)
			{
				objMast.setQbRefDate("R");
			}
			else
			{
				objMast.setQbRefDate("C");
			}
			if(checkRvNO)
			{
				objMast.setQbRefNo("R");
			}
			else
			{
				objMast.setQbRefNo("C");
			}
			if(selectedPostToQbBy.equalsIgnoreCase("Receipt Voucher"))
			{
				objMast.setRvOrJv("R");
			}
			else
			{
				objMast.setRvOrJv("J");
			}
			objMast.setMode("Both");
			objMast.setSepearateJournal("N");
			objMast.setClassHide("Y");
			objMast.setMemoHide("N");
			if(objMast.getRvOrJv().equalsIgnoreCase("J"))
			{
				objMast.setJvRefNumber(dataRV.GetJVRefNumber());
			}
			else
			{
				objMast.setJvRefNumber(null);
			}
			objMast.setStatus("C");
			if(makeAsDeferedIncome)
			{
				objMast.setDefferedIncome("Y");
			}
			else
			{
				objMast.setDefferedIncome("N");
			}
			objMast.setUserId(0);
			int result=0;
			if(reciptVoiucherKey==0)
			{
				result=dataRV.addNewReceiptVoucherMast(objMast,webUserID);
			}else
			{
				result=dataRV.updateRecieptVoucher(objMast,webUserID);
			}
			if(result>0)
			{
				if(reciptVoiucherKey==0)
				{
					if(compSetup.getrVSerialSetup().equalsIgnoreCase("S"))
					{
						data.ConfigSerialNumberCashInvoice(SerialFields.CashChequeRV, receiptVoucherNo,0);
					}
					else
					{
						data.ConfigSerialNumberCashInvoice(SerialFields.ReceiptSerial, receiptVoucherNo,0);
					}
					if(objMast.getRvOrJv().equalsIgnoreCase("J"))
					{
						data.ConfigSerialNumberCashInvoice(SerialFields.JvEntry, receiptVoucherNo,0);
					}
				}

				dataRV.deleteReceiptVoucherGridItems(tmpRecNo);
				for (ReceiptVoucherGridData item : lstRecieptVoucherGrid) 
				{
					ReceiptVoucherDeatiledModel objDeatiled=new ReceiptVoucherDeatiledModel();
					if(item!=null)
					{
						objDeatiled.setRecNo(tmpRecNo);
						objDeatiled.setDepositeToAccountRefKey(item.getSeletedDepositeTo().getRec_No());
						objDeatiled.setAmmount(item.getAmount());
						objDeatiled.setMemo(item.getMemo());
						objDeatiled.setCheckDate(item.getCheckDate());
						objDeatiled.setCheckNumber(item.getChequeNO());
						if(item.getSelectedBank()!=null)
						{
							objDeatiled.setBankKey(item.getSelectedBank().getRecno());
						}
						else
						{
							objDeatiled.setBankKey(0);
						}
						objDeatiled.setLineNo(item.getLineNo());
						objDeatiled.setPaymentMethod(item.getSelectedPaymentMethod());
						if(item.getSelectedClass()!=null)
						{
							objDeatiled.setClassRefKey(item.getSelectedClass().getClass_Key());
						}
						else
						{
							objDeatiled.setClassRefKey(0);
						}
						if(item.getSeletedCuc()!=null)
						{
							objDeatiled.setCucaccuntKey(item.getSeletedCuc().getRec_No());
						}
						else
						{
							objDeatiled.setCucaccuntKey(0);
						}
						objDeatiled.setStatus("C");
						objDeatiled.setVisaCharge(0);

					}
					dataRV.addNewReceiptVoucherDeatiled(objDeatiled);	
				}
				if (reciptVoiucherKey == 0) {
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.CashChequeReceipt.getValue(), (int)objMast.getRecNo(), objMast.getMemo(), objMast.getRefNumber(), objMast.getTxtDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Create.getValue());
				} else {
					data.addUserActivity(data.GetNewUserActivityRecNo(), common.HbaEnum.HbaList.CashChequeReceipt.getValue(), (int)objMast.getRecNo(), objMast.getMemo(), objMast.getRefNumber(), objMast.getTxtDate(),  webUserName, webUserID,common.HbaEnum.UserAction.Edit.getValue()); 
				}

			}
			if(reciptVoiucherKey>0)
			{
				Clients.showNotification("The Receipt Voucher Has Been Updated Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
			}
			else
			{
				Clients.showNotification("The Receipt Voucher Has Been Created Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			        					        
			}

		}catch (Exception ex) 
		{
			logger.error("error in ReceiptVoucherViewModel---saveData-->" , ex);
		}
	}





	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@Command
	@NotifyChange({"lstRecieptVoucherGrid","lblTotalCost","toatlAmount","balance","amountInWords"})
	public void receiptVoucherGridAjax(@BindingParam("type") final ReceiptVoucherGridData type,@BindingParam("parm") String parm)
	{
		if(type!=null)
		{
			if(parm.equals("dpst"))
			{

				if(selectedPostToQbBy!=null && !"".equalsIgnoreCase(selectedPostToQbBy) && selectedPostToQbBy.equalsIgnoreCase("Receipt Voucher"))
				{		
					if(type.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("Cash") || type.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("Bank") || type.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("OtherCurrentAsset") || type.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("Cheque Under Collection")
							|| type.getSeletedDepositeTo().getAccountType().equalsIgnoreCase("Post Dated Cheque"))
					{
						Map args = new HashMap();

					}
					else
					{
						Messagebox.show("Since the R.V. Post to QuickBooks by Receipt Voucher you cannot select this account!","Reciept Voucher",Messagebox.OK,Messagebox.INFORMATION);
						type.setSeletedDepositeTo(null);
						BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");

					}

				}


				if(type.getSeletedDepositeTo()!=null)
				{
					//check if Account has sub class		
					boolean hasSubAccount=data.checkIfBankAccountsHasSub(type.getSeletedDepositeTo().getFullName()+":");

					if(type.getSeletedDepositeTo()!=null)
					{
						ClassModel classModel=dataRV.getGridClassPrePoupulateForSelectedAccount(type.getSeletedDepositeTo().getRec_No());

						for(ClassModel model:lstsClassModel)
						{
							if(model.getClass_Key()==classModel.getClass_Key())
							{
								type.setSelectedClass(model);
							}
						}
					}
					if(hasSubAccount)
					{
						if(compSetup.getPostOnMainAccount().equals("Y"))
						{

							Messagebox.show("Selected account have sub accounts. Do you want to continue?","Reciept Voucher", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener() {						
								public void onEvent(Event evt) throws InterruptedException {
									if (evt.getName().equals("onYes")) 
									{	 				        	
										Map args = new HashMap();
									}
									else 
									{	
										Map args = new HashMap();
										type.setSeletedDepositeTo(null);
										type.setSelectedClass(null);
										BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");
									}
								}

							});


						}
						else
						{
							Messagebox.show("Selected account have sub accounts. You cannot continue !!","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
							type.setSeletedDepositeTo(null);		
							type.setSelectedClass(null);
							BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");
						}


					}

				}
			}

			if(parm.equals("class"))
			{

				if(type.getSelectedClass()!=null)
				{
					//check if account has sub account		
					boolean hasSubAccount=data.checkIfClassHasSub(type.getSelectedClass().getName()+":");
					if(hasSubAccount)
					{
						if(compSetup.getPostOnMainClass().equals("Y"))
						{
							Messagebox.show("Selected Class have sub Sub Class(s). Do you want to continue?","Reciept Voucher", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
									new org.zkoss.zk.ui.event.EventListener() {						
								public void onEvent(Event evt) throws InterruptedException {
									if (evt.getName().equals("onYes")) 
									{	 		
										Map args = new HashMap();
									}
									else 
									{		 
										Map args = new HashMap();
										type.setSelectedClass(null);
										BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");
									}
								}

							});
						}
						else
						{
							Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
							type.setSelectedClass(null);	
							BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");
						}	
					}
				}
				ClassModel classModel=null;
				if(type.getSeletedDepositeTo()!=null)
				{
					classModel=dataRV.getGridClassPrePoupulateForSelectedAccount(type.getSeletedDepositeTo().getRec_No());
				}

				if(classModel!=null && classModel.getClass_Key()>0)
				{
					if(compSetup.getChangePredefinedClass().equals("N"))
					{
						Messagebox.show("Selected Accounts has Predefined class, you can't coninue!","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
						if(type.getSeletedDepositeTo()!=null)
						{
							type.setSelectedClass(null);		
							BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");
						}
					}
					else
					{
						Messagebox.show("Selected Accounts has Predefined Class,Do you want to Continue","Reciept Voucher", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {						
							public void onEvent(Event evt) throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{	
									Map args = new HashMap();
								}
								else 
								{		 
									if(type.getSeletedDepositeTo()!=null)
									{
										type.setSelectedClass(null);	
										BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "lstRecieptVoucherGrid");
									}
								}
							}

						});
					}

				}

			}
			if(parm.equals("amount"))
			{
				setLabelCheckTotalcost();
			}
		}
	}




	@Command
	@NotifyChange({"receiptVoucherMastModel","canCreate","customerBalance","ReceiptVoucherDeatiledModel","labelStatus","selectedAccountCr","ReceiptVoucherGridData","selectedReceivedFrom","selectedPostToQbBy","lstInvcCustomerClass","lstRecieptVoucherGrid","receiptVoucherNo","rvDate","memo","toatlAmount","printOnReciptVoucher","amountPiad","toatlAmount","tempTotalAmount","amountInWords","customerBalance","makeAsDeferedIncomeVisible","makeAsDeferedIncome"})
	public void navigationReceiptVoucher(@BindingParam("cmp") String navigation)
	{
		try
		{
			receiptVoucherMastModel=dataRV.navigationReciptVoucher(reciptVoiucherKey,webUserID,seeTrasction,navigation,actionTYpe);
			if(receiptVoucherMastModel!=null && receiptVoucherMastModel.getRecNo()>0)
			{
				actionTYpe="edit";
				labelStatus="Edit";
				reciptVoiucherKey=receiptVoucherMastModel.getRecNo();
				List<ReceiptVoucherDeatiledModel> reciptVoucherGridData=dataRV.getReciptVoucherGridDataByID(reciptVoiucherKey);

				for(QbListsModel recivedFrom:lstReceivedFrom)
				{
					if(recivedFrom.getRecNo()==receiptVoucherMastModel.getCustRefKey())
					{
						selectedReceivedFrom=recivedFrom;
						if(recivedFrom.getListType().equalsIgnoreCase("Customer"))
						{
							VendorModel model=dataRV.getForPrintOnReceiptCustomer(selectedReceivedFrom.getRecNo());
							printOnReciptVoucher=model.getName();
							customerBalance=model.getBalance();
						}
						else if(recivedFrom.getListType().equalsIgnoreCase("Vendor"))
						{
							VendorModel model=dataRV.getForPrintOnReceiptVendor(selectedReceivedFrom.getRecNo());
							printOnReciptVoucher=model.getName();
							customerBalance=model.getBalance();
						}
						else if(recivedFrom.getListType().equalsIgnoreCase("Employee"))
						{
							printOnReciptVoucher=dataRV.getForPrintOnReceiptEmployee(selectedReceivedFrom.getRecNo());
							customerBalance=0.0;
						}
						else if(recivedFrom.getListType().equalsIgnoreCase("OtherNames"))
						{
							printOnReciptVoucher=dataRV.getForPrintOnReceiptOtherNames(selectedReceivedFrom.getRecNo());
							customerBalance=0.0;
						}
						break;
					}

				}

				for(AccountsModel accountCr:lstAccountCr)
				{
					if(accountCr.getRec_No()==receiptVoucherMastModel.getArAccountRefKey())
					{
						selectedAccountCr=accountCr;
						if((selectedAccountCr.getAccountType().equalsIgnoreCase("OtherCurrentliability")) || selectedPostToQbBy.equalsIgnoreCase("longtermliability"))
						{
							makeAsDeferedIncomeVisible=true;
						}
						else
						{
							makeAsDeferedIncomeVisible=false;
						}
						break;
					}

				}
				receiptVoucherNo=receiptVoucherMastModel.getRefNumber();
				rvDate=receiptVoucherMastModel.getTxtDate();
				printOnReciptVoucher=receiptVoucherMastModel.getReceiptName();
				memo=receiptVoucherMastModel.getMemo();
				if(receiptVoucherMastModel.getDefferedIncome().equalsIgnoreCase("Y"))
				{
					makeAsDeferedIncome=true;
				}
				else
				{
					makeAsDeferedIncome=false;
				}

				if(receiptVoucherMastModel.getRvOrJv().equalsIgnoreCase("R"))
				{
					selectedPostToQbBy=postToQbBy.get(0);
					canCreate=false;
				}
				else
				{
					selectedPostToQbBy=postToQbBy.get(1);
					canCreate=true;
				}

				lstRecieptVoucherGrid=new ArrayList<ReceiptVoucherGridData>();
				for(ReceiptVoucherDeatiledModel editReciptVoucherGrid:reciptVoucherGridData)
				{
					ReceiptVoucherGridData obj=new ReceiptVoucherGridData();
					obj.setLineNo(lstRecieptVoucherGrid.size()+1);

					for(AccountsModel depositeTo:lstDepositeTo)
					{
						if(depositeTo.getRec_No()==editReciptVoucherGrid.getDepositeToAccountRefKey())
						{
							obj.setSeletedDepositeTo(depositeTo);
							break;
						}

					}

					for(BanksModel banksModel:lstBankModel)
					{
						if(banksModel.getRecno()==editReciptVoucherGrid.getBankKey())
						{
							obj.setSelectedBank(banksModel);
							break;
						}

					}

					for(ClassModel classModel:lstsClassModel)
					{
						if(classModel.getClass_Key()==editReciptVoucherGrid.getClassRefKey())
						{
							obj.setSelectedClass(classModel);
							break;
						}

					}


					for(AccountsModel cucModel:lstcuc)
					{
						if(cucModel.getRec_No()==editReciptVoucherGrid.getCucaccuntKey())
						{
							obj.setSeletedCuc(cucModel);
							break;
						}

					}

					for(String payment:lstPyamentMethod)
					{
						if(payment.equalsIgnoreCase(editReciptVoucherGrid.getPaymentMethod()))
						{
							obj.setSelectedPaymentMethod(payment);
							break;
						}

					}
					obj.setAmount(editReciptVoucherGrid.getAmmount());
					obj.setMemo(editReciptVoucherGrid.getMemo());
					obj.setCheckDate(editReciptVoucherGrid.getCheckDate());
					obj.setChequeNO(editReciptVoucherGrid.getCheckNumber());
					lstRecieptVoucherGrid.add(obj);
				}
				setLabelCheckTotalcost();

			}
			else
			{
				actionTYpe="create";
				labelStatus="Create";
				reciptVoiucherKey=0;
				checkRvNO=true;
				checkRvDate=true;
				if(compSetup.getrVSerialSetup().equals("S"))
					receiptVoucherNo=dataRV.GetRVNumberFlag1();
				else
					receiptVoucherNo=dataRV.GetRVNumberFlag2();
				Calendar c = Calendar.getInstance();		
				creationdate=df.parse(sdf.format(c.getTime()));
				rvDate=creationdate;
				lstRecieptVoucherGrid=new ArrayList<ReceiptVoucherGridData>();
				ReceiptVoucherGridData objItems=new ReceiptVoucherGridData();
				objItems.setLineNo(1);
				lstRecieptVoucherGrid.add(objItems);
				lblTotalCost="Amount :0.00";
				toatlAmount=0.0;
				selectedReceivedFrom=null;
				selectedAccountCr=null;
				selectedPostToQbBy=postToQbBy.get(0);
				printOnReciptVoucher="";
				customerBalance=0.0;
				memo="";
				canCreate=true;

			}

			amountInWords=numbToWord.GetFigToWord(toatlAmount);



		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherViewModel ----> navigationReceiptVoucher", ex);			
		}
	}


	@Command
	@NotifyChange({"receiptVoucherMastModel","customerBalance","ReceiptVoucherDeatiledModel","labelStatus","selectedAccountCr","ReceiptVoucherGridData","selectedReceivedFrom","selectedPostToQbBy","lstInvcCustomerClass","lstRecieptVoucherGrid","receiptVoucherNo","rvDate","memo","toatlAmount","printOnReciptVoucher","amountPiad","toatlAmount","tempTotalAmount","amountInWords","customerBalance","makeAsDeferedIncomeVisible","makeAsDeferedIncome"})
	public void copyFunctinality()
	{
		try
		{
			if(reciptVoiucherKey>0)
			{
				receiptVoucherMastModel=dataRV.getReceiptVoucherById(reciptVoiucherKey,webUserID,seeTrasction);
				if(receiptVoucherMastModel!=null && receiptVoucherMastModel.getRecNo()>0)
				{
					actionTYpe="edit";
					labelStatus="Copied-Create";
					reciptVoiucherKey=receiptVoucherMastModel.getRecNo();
					List<ReceiptVoucherDeatiledModel> reciptVoucherGridData=dataRV.getReciptVoucherGridDataByID(reciptVoiucherKey);

					for(QbListsModel recivedFrom:lstReceivedFrom)
					{
						if(recivedFrom.getRecNo()==receiptVoucherMastModel.getCustRefKey())
						{
							selectedReceivedFrom=recivedFrom;
							if(recivedFrom.getListType().equalsIgnoreCase("Customer"))
							{
								VendorModel model=dataRV.getForPrintOnReceiptCustomer(selectedReceivedFrom.getRecNo());
								printOnReciptVoucher=model.getName();
								customerBalance=model.getBalance();
							}
							else if(recivedFrom.getListType().equalsIgnoreCase("Vendor"))
							{
								VendorModel model=dataRV.getForPrintOnReceiptVendor(selectedReceivedFrom.getRecNo());
								printOnReciptVoucher=model.getName();
								customerBalance=model.getBalance();
							}
							else if(recivedFrom.getListType().equalsIgnoreCase("Employee"))
							{
								printOnReciptVoucher=dataRV.getForPrintOnReceiptEmployee(selectedReceivedFrom.getRecNo());
								customerBalance=0.0;
							}
							else if(recivedFrom.getListType().equalsIgnoreCase("OtherNames"))
							{
								printOnReciptVoucher=dataRV.getForPrintOnReceiptOtherNames(selectedReceivedFrom.getRecNo());
								customerBalance=0.0;
							}
							break;
						}

					}

					for(AccountsModel accountCr:lstAccountCr)
					{
						if(accountCr.getRec_No()==receiptVoucherMastModel.getArAccountRefKey())
						{
							selectedAccountCr=accountCr;
							if((selectedAccountCr.getAccountType().equalsIgnoreCase("OtherCurrentliability")) || selectedPostToQbBy.equalsIgnoreCase("longtermliability"))
							{
								makeAsDeferedIncomeVisible=true;
							}
							else
							{
								makeAsDeferedIncomeVisible=false;
							}
							break;
						}

					}

					reciptVoiucherKey=0;
					if(compSetup.getrVSerialSetup().equals("S"))
						receiptVoucherNo=dataRV.GetRVNumberFlag1();
					else
						receiptVoucherNo=dataRV.GetRVNumberFlag2();
					Calendar c = Calendar.getInstance();		
					creationdate=df.parse(sdf.format(c.getTime()));
					rvDate=creationdate;
					printOnReciptVoucher=receiptVoucherMastModel.getReceiptName();
					memo=receiptVoucherMastModel.getMemo();
					if(receiptVoucherMastModel.getDefferedIncome().equalsIgnoreCase("Y"))
					{
						makeAsDeferedIncome=true;
					}
					else
					{
						makeAsDeferedIncome=false;
					}

					if(receiptVoucherMastModel.getRvOrJv().equalsIgnoreCase("R"))
					{
						selectedPostToQbBy=postToQbBy.get(0);

					}
					else
					{
						selectedPostToQbBy=postToQbBy.get(1);
					}

					lstRecieptVoucherGrid=new ArrayList<ReceiptVoucherGridData>();
					for(ReceiptVoucherDeatiledModel editReciptVoucherGrid:reciptVoucherGridData)
					{
						ReceiptVoucherGridData obj=new ReceiptVoucherGridData();
						obj.setLineNo(lstRecieptVoucherGrid.size()+1);

						for(AccountsModel depositeTo:lstDepositeTo)
						{
							if(depositeTo.getRec_No()==editReciptVoucherGrid.getDepositeToAccountRefKey())
							{
								obj.setSeletedDepositeTo(depositeTo);
								break;
							}

						}

						for(BanksModel banksModel:lstBankModel)
						{
							if(banksModel.getRecno()==editReciptVoucherGrid.getBankKey())
							{
								obj.setSelectedBank(banksModel);
								break;
							}

						}

						for(ClassModel classModel:lstsClassModel)
						{
							if(classModel.getClass_Key()==editReciptVoucherGrid.getClassRefKey())
							{
								obj.setSelectedClass(classModel);
								break;
							}

						}


						for(AccountsModel cucModel:lstcuc)
						{
							if(cucModel.getRec_No()==editReciptVoucherGrid.getCucaccuntKey())
							{
								obj.setSeletedCuc(cucModel);
								break;
							}

						}

						for(String payment:lstPyamentMethod)
						{
							if(payment.equalsIgnoreCase(editReciptVoucherGrid.getPaymentMethod()))
							{
								obj.setSelectedPaymentMethod(payment);
								break;
							}

						}
						obj.setAmount(editReciptVoucherGrid.getAmmount());
						obj.setMemo(editReciptVoucherGrid.getMemo());
						obj.setCheckDate(editReciptVoucherGrid.getCheckDate());
						obj.setChequeNO(editReciptVoucherGrid.getCheckNumber());
						lstRecieptVoucherGrid.add(obj);
					}
					setLabelCheckTotalcost();

				}
				amountInWords=numbToWord.GetFigToWord(toatlAmount);



			}
			else
			{
				Messagebox.show("You can only copy a existing Receipt Voucher."," Receipt Voucher", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}
		}

		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherViewModel ----> copyFunctinality", ex);			
		}
	}



	public String getMsgToBeDispalyedOnInvoice() {
		return msgToBeDispalyedOnInvoice;
	}

	public void setMsgToBeDispalyedOnInvoice(String msgToBeDispalyedOnInvoice) {
		this.msgToBeDispalyedOnInvoice = msgToBeDispalyedOnInvoice;
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

	public double getToatlAmount() {
		return toatlAmount;
	}

	@NotifyChange({"amountInWords"})
	public void setToatlAmount(double toatlAmount) {
		this.toatlAmount = Double.parseDouble(BigDecimal.valueOf(toatlAmount).toPlainString());
		amountInWords=numbToWord.GetFigToWord(toatlAmount);
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
						new FileOutputStream("C:/temp/invoicePDFWebApplication.pdf"));
				writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
				HeaderFooter event = new HeaderFooter();
				writer.setPageEvent(event);

				// various fonts
				BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA,"Cp1252", false);
				BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN,"Cp1252", false);
				BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER,"Cp1252", false);
				BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL,"Cp1252", false);

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
				Font f1 = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD,
						BaseColor.RED);
				Chunk c = new Chunk("Receipt Voucher");
				c.setUnderline(0.1f, -2f);
				c.setFont(f1);
				Paragraph p = new Paragraph(c);

				firsttbl.addCell(p);

				PdfPCell cell1 = new PdfPCell(new Phrase("Date : " + sdf.format(rvDate) + "\n\n" + "P.V. No. : " + receiptVoucherNo));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setColspan(3);
				cell1.disableBorderSide(Rectangle.BOX);
				firsttbl.addCell(cell1);
				document.add(firsttbl);

				PdfPTable tbl1 = new PdfPTable(1);
				tbl1.setWidthPercentage(100);


				cell1 = new PdfPCell(new Phrase("\nRecived From : "	+ selectedReceivedFrom.getName(),FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);
				document.add(tbl1);

				paragraph = new Paragraph();
				paragraph.setSpacingAfter(10);
				document.add(paragraph);

				paragraph = new Paragraph();
				Chunk chunk = new Chunk("Being For : " + memo+"\n");
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
				boolean cashFlag=false;
				boolean chequeFlag=false;
				for (ReceiptVoucherGridData item : lstRecieptVoucherGrid) 
				{
					if(item.getSelectedPaymentMethod().equalsIgnoreCase("cash")){
						cashFlag=true;
						break;
					}
				}
				for (ReceiptVoucherGridData item : lstRecieptVoucherGrid) 
				{
					if(item.getSelectedPaymentMethod().equalsIgnoreCase("cheque")){
						chequeFlag=true;
						break;
					}
				}
				if(cashFlag){
					paragraph = new Paragraph();
					chunk = new Chunk("\nCash Details",FontFactory.getFont(FontFactory.HELVETICA_BOLD,16f));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);
					
					table = new PdfPTable(2);
					table.setSpacingBefore(5);
					table.setWidthPercentage(100);
					table.setWidths(new int[] {400, 60 });

					HeadderProduct = new PdfPCell(new Phrase("Pay Mode"));
					HeadderProduct.setHorizontalAlignment(Element.ALIGN_LEFT);
					myColor = WebColors.getRGBColor("#8ECDFA");
					HeadderProduct.setBackgroundColor(myColor);
					table.addCell(HeadderProduct);
					table.setHeaderRows(1);

					HeadderAmount1 = new PdfPCell(new Phrase("Amount"));
					HeadderAmount1.setBorder(Rectangle.NO_BORDER);
					HeadderAmount1.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderAmount1.setBackgroundColor(myColor);
					table.addCell(HeadderAmount1);


					for (ReceiptVoucherGridData item : lstRecieptVoucherGrid) 
					{
						if(item.getSelectedPaymentMethod().equalsIgnoreCase("cash")){
							table.addCell(item.getSelectedPaymentMethod());
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

					for(PdfPRow r: table.getRows()) {
						for(PdfPCell c1: r.getCells()) {
							c1.setBorder(Rectangle.NO_BORDER);
						}
					}

					document.add(table);
					document.add( Chunk.NEWLINE );

				}

				if(chequeFlag){
					paragraph = new Paragraph();
					chunk = new Chunk("Cheque Details",FontFactory.getFont(FontFactory.HELVETICA_BOLD,16f));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);
					
					table = new PdfPTable(4);
					table.setSpacingBefore(5);
					table.setWidthPercentage(100);
					table.setWidths(new int[] {60,180,180, 60 });
					table.getDefaultCell().setBorder(1);

					/*HeadderProduct = new PdfPCell(new Phrase("Pay Mode"));
					HeadderProduct.setBorder(Rectangle.NO_BORDER);
					HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
					myColor = WebColors.getRGBColor("#8ECDFA");
					HeadderProduct.setBackgroundColor(myColor);
					table.addCell(HeadderProduct);
					table.setHeaderRows(1);*/

					HeadderDate = new PdfPCell(new Phrase("Check NO"));
					HeadderDate.setBorder(Rectangle.NO_BORDER);
					HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderDate.setBackgroundColor(myColor);
					table.addCell(HeadderDate);

					HeadderQty = new PdfPCell(new Phrase("Check Date"));
					HeadderQty.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderQty.setBackgroundColor(myColor);
					table.addCell(HeadderQty);

					HeadderRate = new PdfPCell(new Phrase("Bank Name"));
					HeadderRate.setBorder(Rectangle.NO_BORDER);
					HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderRate.setBackgroundColor(myColor);
					table.addCell(HeadderRate);

					/*PdfPCell HeadderAmount = new PdfPCell(new Phrase("Deposite To"));
					// HeadderAmount.setPadding(1);
					// HeadderAmount.setColspan(1);
					HeadderAmount.setBorder(Rectangle.NO_BORDER);
					HeadderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderAmount.setBackgroundColor(myColor);
					// HeadderAmount.setBorderWidth(40.0f);
					table.addCell(HeadderAmount);*/

					HeadderAmount1 = new PdfPCell(new Phrase("Amount"));
					HeadderAmount1.setBorder(Rectangle.NO_BORDER);
					HeadderAmount1.setHorizontalAlignment(Element.ALIGN_CENTER);
					HeadderAmount1.setBackgroundColor(myColor);
					table.addCell(HeadderAmount1);
					boolean desc=true;

					for (ReceiptVoucherGridData item : lstRecieptVoucherGrid) 
					{
						if(item.getSelectedPaymentMethod().equalsIgnoreCase("cheque")){

							//table.addCell(item.getSelectedPaymentMethod());
							if(item.getChequeNO()!=null && !item.getChequeNO().equalsIgnoreCase(""))
							{
								cell1 = new PdfPCell(new Phrase(item.getChequeNO()));
								cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
								table.addCell(cell1);
							}
							else
								table.addCell("-");
							if(item.getCheckDate()!=null)
							{
								cell1 = new PdfPCell(new Phrase(sdf.format(item.getCheckDate())));
								cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
								table.addCell(cell1);
							}
							else
								table.addCell("-");
							if(item.getSelectedBank()!=null)
							{
								cell1 = new PdfPCell(new Phrase(""+item.getSelectedBank().getBankName()));
								cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
								table.addCell(cell1);
							}
							else
								table.addCell("-");
							/*if(item.getSeletedDepositeTo()!=null)
							table.addCell(""+item.getSeletedDepositeTo().getAccountName());
							else
							table.addCell("-");*/
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

					for(PdfPRow r: table.getRows()) {
						for(PdfPCell c1: r.getCells()) {
							c1.setBorder(Rectangle.NO_BORDER);
						}
					}

					document.add(table);
					document.add( Chunk.NEWLINE );
				}
				
				
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
				
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );

				paragraph=new Paragraph();
				chunk = new Chunk(msgToBeDispalyedOnInvoice);
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);
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
				logger.error("ERROR in ReceiptVoucherViewModel ----> createPdfForPrinting", ex);
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
			logger.error("ERROR in ReceiptVoucherViewModel ----> previewPdfForprintingInvoice", ex);			
		}
	}
	public ReceiptVoucherViewModel getObjReceiptVoucher() {
		return objReceiptVoucher;
	}

	public void setObjReceiptVoucher(ReceiptVoucherViewModel objReceiptVoucher) {
		this.objReceiptVoucher = objReceiptVoucher;
	}

	public ReceiptVoucherMastModel getReceiptVoucherMastModel() {
		return receiptVoucherMastModel;
	}

	public void setReceiptVoucherMastModel(
			ReceiptVoucherMastModel receiptVoucherMastModel) {
		this.receiptVoucherMastModel = receiptVoucherMastModel;
	}

	public ReceiptVoucherDeatiledModel getReceiptVoucherDeatiledModel() {
		return receiptVoucherDeatiledModel;
	}

	public void setReceiptVoucherDeatiledModel(
			ReceiptVoucherDeatiledModel receiptVoucherDeatiledModel) {
		this.receiptVoucherDeatiledModel = receiptVoucherDeatiledModel;
	}

	public List<QbListsModel> getLstReceivedFrom() {
		return lstReceivedFrom;
	}

	public void setLstReceivedFrom(List<QbListsModel> lstReceivedFrom) {
		this.lstReceivedFrom = lstReceivedFrom;
	}

	public QbListsModel getSelectedReceivedFrom() {
		return selectedReceivedFrom;
	}

	@NotifyChange({"printOnReciptVoucher","selectedPostToQbBy","customerBalance"})
	public void setSelectedReceivedFrom(QbListsModel selectedReceivedFrom) {
		this.selectedReceivedFrom = selectedReceivedFrom;

		if(null!=selectedReceivedFrom)
		{
			if(selectedReceivedFrom.getListType().equalsIgnoreCase("Customer"))
			{
				VendorModel model=dataRV.getForPrintOnReceiptCustomer(selectedReceivedFrom.getRecNo());
				printOnReciptVoucher=model.getName();
				List<CutomerSummaryReport> customerSummaryReport= data.getCutomerSummaryReport(creationdate,selectedReceivedFrom.getRecNo(),"Y",false,false);		
				if(customerSummaryReport!=null && customerSummaryReport.size()>0)
					customerBalance=customerSummaryReport.get(0).getBalance();
				//customerBalance=model.getBalance();
			}
			else if(selectedReceivedFrom.getListType().equalsIgnoreCase("Vendor"))
			{
				VendorModel model=dataRV.getForPrintOnReceiptVendor(selectedReceivedFrom.getRecNo());
				printOnReciptVoucher=model.getName();
				customerBalance=model.getBalance();
			}
			else if(selectedReceivedFrom.getListType().equalsIgnoreCase("Employee"))
			{
				printOnReciptVoucher=dataRV.getForPrintOnReceiptEmployee(selectedReceivedFrom.getRecNo());
				customerBalance=0.0;
			}
			else if(selectedReceivedFrom.getListType().equalsIgnoreCase("OtherNames"))
			{
				printOnReciptVoucher=dataRV.getForPrintOnReceiptOtherNames(selectedReceivedFrom.getRecNo());
				customerBalance=0.0;
			}
			if(selectedPostToQbBy!=null && selectedAccountCr!=null)
			{
				if((!selectedReceivedFrom.getListType().equalsIgnoreCase("Customer") || !selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable")) && selectedPostToQbBy.equalsIgnoreCase("Receipt Voucher"))
				{
					selectedPostToQbBy=postToQbBy.get(1);
				}
			}

		}

	}

	@NotifyChange({"customerBalance"})
	@Command
	public void refreshCustomerBalance() {

		if(null!=selectedReceivedFrom)
		{
			if(selectedReceivedFrom.getListType().equalsIgnoreCase("Customer"))
			{
				VendorModel model=dataRV.getForPrintOnReceiptCustomer(selectedReceivedFrom.getRecNo());
				printOnReciptVoucher=model.getName();

				List<CutomerSummaryReport> customerSummaryReport= data.getCutomerSummaryReport(creationdate,selectedReceivedFrom.getRecNo(),"Y",false,false);		
				if(customerSummaryReport!=null && customerSummaryReport.size()>0)
					customerBalance=customerSummaryReport.get(0).getBalance();

				//customerBalance=model.getBalance();
			}
			else if(selectedReceivedFrom.getListType().equalsIgnoreCase("Vendor"))
			{
				VendorModel model=dataRV.getForPrintOnReceiptVendor(selectedReceivedFrom.getRecNo());
				printOnReciptVoucher=model.getName();
				customerBalance=model.getBalance();
			}
		}
		else
		{
			Messagebox.show("Invlaid Customer Name !!");			
		}
	}

	public List<String> getPostToQbBy() {
		return postToQbBy;
	}

	public void setPostToQbBy(List<String> postToQbBy) {
		this.postToQbBy = postToQbBy;
	}

	public String getSelectedPostToQbBy() {
		return selectedPostToQbBy;
	}

	@NotifyChange({"selectedPostToQbBy"})
	public void setSelectedPostToQbBy(String selectedPostToQbBy) {
		this.selectedPostToQbBy = selectedPostToQbBy;
		if(selectedPostToQbBy!=null && selectedAccountCr!=null)
		{
			if((!selectedReceivedFrom.getListType().equalsIgnoreCase("Customer") || !selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable")) && selectedPostToQbBy.equalsIgnoreCase("Receipt Voucher"))
			{
				Messagebox.show("You cannot select Post to Qb as Receipt Voucher !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
				this.selectedPostToQbBy=postToQbBy.get(1);
			}
		}
	}

	public List<AccountsModel> getLstAccountCr() {
		return lstAccountCr;
	}

	public void setLstAccountCr(List<AccountsModel> lstAccountCr) {
		this.lstAccountCr = lstAccountCr;
	}

	public AccountsModel getSelectedAccountCr() {
		return selectedAccountCr;
	}


	public void setSelectedAccountCr(AccountsModel selectedAccountCr) {
		this.selectedAccountCr = selectedAccountCr;

	}

	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange({"selectedPostToQbBy","selectedAccountCr","lstAccountCr","makeAsDeferedIncomeVisible"})
	public void onselectOfAccountCr()
	{

		if(selectedAccountCr!=null && selectedReceivedFrom!=null)
		{
			if(selectedPostToQbBy!=null && selectedAccountCr!=null)
			{
				if((selectedAccountCr.getAccountType().equalsIgnoreCase("OtherCurrentliability")) || selectedPostToQbBy.equalsIgnoreCase("longtermliability"))
				{
					makeAsDeferedIncomeVisible=true;
				}
				else
				{
					makeAsDeferedIncomeVisible=false;
				}
				if((!selectedReceivedFrom.getListType().equalsIgnoreCase("Customer") || !selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable")) && selectedPostToQbBy.equalsIgnoreCase("Receipt Voucher"))
				{
					selectedPostToQbBy=postToQbBy.get(1);
				}
			}


			if(selectedReceivedFrom.getListType().equalsIgnoreCase("Customer"))
			{
				if(!selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable"))
				{
					if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsPayable"))
					{
						Messagebox.show("You can't use Accounts Payable account for Customer !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
						this.selectedAccountCr=null;
						BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "selectedAccountCr");
						return;
					}
					else
					{
						Messagebox.show("You have selected Non Accounts Receivable Type for a name with Customer Type ! Are you sure ?","Reciept Voucher", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {		
							@SuppressWarnings("unused")
							public void onEvent(Event evt) throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{	 				
									Map args = new HashMap();
								}
								else 
								{	
									selectedAccountCr=null;
									BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "selectedAccountCr");
								}
							}

						});

					}

				}
			}
			else if(selectedReceivedFrom.getListType().equalsIgnoreCase("Vendor"))
			{
				if(!selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsPayable"))
				{
					if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable"))
					{
						Messagebox.show("You can't use Accounts Payable account for Vendor !","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
						this.selectedAccountCr=null;
						return;
					}
					else
					{
						Messagebox.show("You have selected Non Accounts Receivable Type for a name with Vendor Type ! Are you sure ?","Reciept Voucher", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
								new org.zkoss.zk.ui.event.EventListener() {						
							@SuppressWarnings("unused")
							public void onEvent(Event evt) throws InterruptedException {
								if (evt.getName().equals("onYes")) 
								{	 				
									Map args = new HashMap();
								}
								else 
								{		 
									selectedAccountCr=null;
									BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "selectedAccountCr");
								}
							}

						});
					}

				}
			}
			else if(selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsReceivable") || selectedAccountCr.getAccountType().equalsIgnoreCase("AccountsPayable"))
			{
				Messagebox.show("You can't use AccountsReceivable or AccountsPayable Account!","Reciept Voucher", Messagebox.OK , Messagebox.INFORMATION);
				this.selectedAccountCr=null;
				BindUtils.postNotifyChange(null, null, ReceiptVoucherViewModel.this, "selectedAccountCr");
			}

		}
	}


	public ReceiptVoucherGridData getSelectedReceiptValucherGrid() {
		return selectedReceiptValucherGrid;
	}

	public void setSelectedReceiptValucherGrid(
			ReceiptVoucherGridData selectedReceiptValucherGrid) {
		this.selectedReceiptValucherGrid = selectedReceiptValucherGrid;
	}

	public List<ReceiptVoucherGridData> getLstRecieptVoucherGrid() {
		return lstRecieptVoucherGrid;
	}

	public void setLstRecieptVoucherGrid(
			List<ReceiptVoucherGridData> lstRecieptVoucherGrid) {
		this.lstRecieptVoucherGrid = lstRecieptVoucherGrid;
	}

	public List<String> getLstPyamentMethod() {
		return lstPyamentMethod;
	}

	public void setLstPyamentMethod(List<String> lstPyamentMethod) {
		this.lstPyamentMethod = lstPyamentMethod;
	}

	public List<BanksModel> getLstBankModel() {
		return lstBankModel;
	}

	public void setLstBankModel(List<BanksModel> lstBankModel) {
		this.lstBankModel = lstBankModel;
	}

	public List<AccountsModel> getLstDepositeTo() {
		return lstDepositeTo;
	}

	public void setLstDepositeTo(List<AccountsModel> lstDepositeTo) {
		this.lstDepositeTo = lstDepositeTo;
	}

	public List<ClassModel> getLstsClassModel() {
		return lstsClassModel;
	}

	public void setLstsClassModel(List<ClassModel> lstsClassModel) {
		this.lstsClassModel = lstsClassModel;
	}

	public String getReceiptVoucherNo() {
		return receiptVoucherNo;
	}

	public void setReceiptVoucherNo(String receiptVoucherNo) {
		this.receiptVoucherNo = receiptVoucherNo;
	}

	public String getPrintOnReciptVoucher() {
		return printOnReciptVoucher;
	}

	public void setPrintOnReciptVoucher(String printOnReciptVoucher) {
		this.printOnReciptVoucher = printOnReciptVoucher;
	}

	public String getAmountInWords() {
		return amountInWords;
	}

	public void setAmountInWords(String amountInWords) {
		this.amountInWords = amountInWords;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<AccountsModel> getLstcuc() {
		return lstcuc;
	}

	public void setLstcuc(List<AccountsModel> lstcuc) {
		this.lstcuc = lstcuc;
	}

	public boolean isCheckRvNO() {
		return checkRvNO;
	}

	public void setCheckRvNO(boolean checkRvNO) {
		this.checkRvNO = checkRvNO;
	}

	public boolean isCheckRvDate() {
		return checkRvDate;
	}

	public void setCheckRvDate(boolean checkRvDate) {
		this.checkRvDate = checkRvDate;
	}

	public double getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	public Date getRvDate() {
		return rvDate;
	}

	public void setRvDate(Date rvDate) {
		this.rvDate = rvDate;
	}




	/**
	 * @return the webUserID
	 */
	public int getWebUserID() {
		return webUserID;
	}

	/**
	 * @param webUserID the webUserID to set
	 */
	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
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

	/**
	 * @return the actionTYpe
	 */
	public String getActionTYpe() {
		return actionTYpe;
	}

	/**
	 * @param actionTYpe the actionTYpe to set
	 */
	public void setActionTYpe(String actionTYpe) {
		this.actionTYpe = actionTYpe;
	}

	/**
	 * @return the canView
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @param canView the canView to set
	 */
	public void setCanView(boolean canView) {
		this.canView = canView;
	}

	/**
	 * @return the canModify
	 */
	public boolean isCanModify() {
		return canModify;
	}

	/**
	 * @param canModify the canModify to set
	 */
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}

	/**
	 * @return the canPrint
	 */
	public boolean isCanPrint() {
		return canPrint;
	}

	/**
	 * @param canPrint the canPrint to set
	 */
	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}

	/**
	 * @return the canCreate
	 */
	public boolean isCanCreate() {
		return canCreate;
	}

	/**
	 * @param canCreate the canCreate to set
	 */
	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}

	/**
	 * @return the labelStatus
	 */
	public String getLabelStatus() {
		return labelStatus;
	}

	/**
	 * @param labelStatus the labelStatus to set
	 */
	public void setLabelStatus(String labelStatus) {
		this.labelStatus = labelStatus;
	}

	/**
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
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
			newTab.setLabel("Receipt Voucher Report");
			newTab.setClosable(true);
			Tabpanel newTabpanel = new Tabpanel();
			Include incContentPage = new Include();
			incContentPage.setSrc("/hba/report/receiptVoucherReport.zul");
			incContentPage.setParent(newTabpanel);
			newTabpanel.setParent(contentTabpanels);
			newTab.setParent(contentTabs);
			newTab.setSelected(true);
			newTab.setVflex("1");
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherViewModel ----> goToRelatedReport", ex);			
		}
	}



	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * @return the reciptVoiucherKey
	 */
	public int getReciptVoiucherKey() {
		return reciptVoiucherKey;
	}



	public boolean isMakeAsDeferedIncome() {
		return makeAsDeferedIncome;
	}

	public void setMakeAsDeferedIncome(boolean makeAsDeferedIncome) {
		this.makeAsDeferedIncome = makeAsDeferedIncome;
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

	/**
	 * @param reciptVoiucherKey the reciptVoiucherKey to set
	 */
	public void setReciptVoiucherKey(int reciptVoiucherKey) {
		this.reciptVoiucherKey = reciptVoiucherKey;
	}

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==290)
			{
				companyRole=item;
				break;
			}
		}
	}




	public boolean isMakeAsDeferedIncomeVisible() {
		return makeAsDeferedIncomeVisible;
	}

	public void setMakeAsDeferedIncomeVisible(boolean makeAsDeferedIncomeVisible) {
		this.makeAsDeferedIncomeVisible = makeAsDeferedIncomeVisible;
	}




	public Date getTxtDate() {
		return txtDate;
	}

	public void setTxtDate(Date txtDate) {
		this.txtDate = txtDate;
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
						"ERROR in ReceiptVoucherViewModel class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in ReceiptVoucherViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in ReceiptVoucherViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in ReceiptVoucherViewModel class HeaderFooter PDf----> onEndPage",
						e);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail" })
	public void CustomerSendEmail() {
		if (validateData(false)) {
			lstAtt = new ArrayList<QuotationAttachmentModel>();
			selectedAttchemnets.setFilename(selectedReceivedFrom.getFullName()
					+ " Receipt Voucher.pdf");
			selectedAttchemnets
			.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Receipt Voucher?",
					"Receipt Voucher", Messagebox.YES | Messagebox.NO,
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
							arg.put("id", selectedReceivedFrom.getRecNo());
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
									"ERROR in ReceiptVoucherViewModel ----> CustomerSendEmail",
									ex);
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

	public HRData getHrData() {
		return hrData;
	}

	public void setHrData(HRData hrData) {
		this.hrData = hrData;
	}

}
