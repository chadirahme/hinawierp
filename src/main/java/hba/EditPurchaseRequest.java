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
import model.ClassModel;
import model.CompSetupModel;
import model.ExpensesModel;
import model.HRListValuesModel;
import model.PayToOrderModel;
import model.PrintModel;
import model.PurchaseRequestGridData;
import model.PurchaseRequestModel;
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


public class EditPurchaseRequest 
{
	private Logger logger = Logger.getLogger(EditPurchaseRequest.class);
	private Date creationdate; 
	private PurchaseRequestModel objCash;
	private List<ClassModel> lstClass;
	private ClassModel selectedLstClass;
	private List<QbListsModel> lstPayToOrder;
	private QbListsModel selectedPaytoOrder;

	private List<QbListsModel> lstDropShipTo;
	private QbListsModel selectedDropShipTo;

	int tmpUnitKey=0;
	private double totalAmount;
	private boolean chkTobePrinted=true;

	private String lblExpenses;
	private String lblCheckItems;

	DecimalFormat formatter = new DecimalFormat("0.00");

	private List <QbListsModel> lstGridCustomer;
	private  List<ClassModel> lstGridClass;

	//CheckItems
	private List<PurchaseRequestGridData> lstCheckItems;
	private PurchaseRequestGridData selectedCheckItems;
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

	private int  purchaseRequestKey;

	private MenuModel companyRole;

	List<MenuModel> list;

	String actionTYpe;

	private String 	memo="";

	private boolean canView=false;

	private boolean canModify=false;

	private boolean canPrint=false;

	private boolean canCreate=false;

	private String labelStatus="";

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	CompanyData companyData=new CompanyData();
	MenuData menuData=new MenuData();

	private String refNUmber="";

	private String address="";

	private String shipTo="";
	private String webUserName="";
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();
	NumberToWord numbToWord=new NumberToWord();
	private boolean seeTrasction=false;
	private PrintModel objPrint;

	@SuppressWarnings("rawtypes")
	public EditPurchaseRequest()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			Execution exec = Executions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Map map = exec.getArg();
			purchaseRequestKey=(Integer)map.get("purchaseRequestKey");
			String type=(String)map.get("type");
			objPrint=new PrintModel();
			if(map.get("objPrint")!=null)
			{
				objPrint=(PrintModel) map.get("objPrint");
			}
			actionTYpe=type;
			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			actionTYpe="Create";
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

			Window win = (Window)Path.getComponent("/purchaseRequestPopUp");
			if(type.equalsIgnoreCase("create"))
			{
				win.setTitle("Add Purchase Request Info");
			}
			else if(type.equalsIgnoreCase("edit"))
			{
				win.setTitle("Edit Purchase Request Info");
			}
			else
			{
				win.setTitle("View Purchase Request Info");
				canModify=false;
				canCreate=false;
			}
			Calendar c = Calendar.getInstance();			
			lstClass=data.fillClassList("");
			lstPayToOrder=data.fillQbList("'Vendor'");
			lstDropShipTo=data.getDropShipTo();
			objCash=new PurchaseRequestModel();
			compSetup=data.GetDefaultSetupInfo();
			lstGridCustomer=data.fillQbList("'Customer'");
			lstGridClass=data.fillClassList("");
			lstGridQBItems=data.getItemForPurchaseRequest();

			if(purchaseRequestKey>0)
			{
				labelStatus="Edit";
				objCash=data.getPurchaseRequestByID(purchaseRequestKey,webUserID,seeTrasction);
				List<PurchaseRequestGridData> itemsGrid=data.getGridDataPurchaseRequestById(purchaseRequestKey);
				if(objCash!=null)
				{
					for(ClassModel apAcounts:lstClass)
					{
						if(apAcounts.getClass_Key()==objCash.getClassRefkey())
						{
							selectedLstClass=apAcounts;
							break;
						}
						else
						{
							selectedLstClass=null;
						}

					}

					for(QbListsModel vendorList:lstPayToOrder)
					{
						if(vendorList.getRecNo()==objCash.getVendorRefKEy())
						{
							selectedPaytoOrder=vendorList;
							break;
						}

					}

					for(QbListsModel dropShipToTemp:lstDropShipTo)
					{
						if(dropShipToTemp.getRecNo()==objCash.getEntityRefKey())
						{
							selectedDropShipTo=dropShipToTemp;
							break;
						}
						else
						{
							selectedDropShipTo=null;
						}


					}

					totalAmount=objCash.getAmount();
					creationdate=df.parse(sdf.format(objCash.getTxtnDate()));
					//setSelectedPaytoOrder(selectedPaytoOrder);
					//setSelectedDropShipTo(selectedDropShipTo);
					address=objCash.getAdress();
					shipTo=objCash.getAdressBillTo();
					memo=objCash.getMemo();
					refNUmber=objCash.getRefNUmber();
					lstCheckItems=new ArrayList<PurchaseRequestGridData>();
					for(PurchaseRequestGridData editItemsGrid:itemsGrid)
					{
						PurchaseRequestGridData obj=new PurchaseRequestGridData();
						obj.setLineNo(lstCheckItems.size()+1);

						for(QbListsModel items:lstGridQBItems)
						{
							if(items.getRecNo()==editItemsGrid.getItemrefKey())
							{
								obj.setSelectedItem(items);
								break;
							}

						}

						for(QbListsModel gridCutomer:lstGridCustomer)
						{
							if(gridCutomer.getRecNo()==editItemsGrid.getEntityRefKey())
							{
								obj.setSelctedCustomer(gridCutomer);
								break;
							}
							else
							{
								obj.setSelctedCustomer(null);
							}

						}
						obj.setDecription(editItemsGrid.getDecription());
						obj.setRecNo(editItemsGrid.getRecNo());
						obj.setAmount(editItemsGrid.getAmount());
						obj.setQuantity(editItemsGrid.getQuantity());
						obj.setRate(editItemsGrid.getRate());
						obj.setIsOrderd(editItemsGrid.getIsOrderd());
						obj.setRecivedQuantity(editItemsGrid.getRecivedQuantity());

						lstCheckItems.add(obj);
					}

				}

			}
			else
			{
				labelStatus="Create";
				creationdate=df.parse(sdf.format(c.getTime()));
				objCash.setTxtnDate(df.parse(sdf.format(c.getTime())));	
				objCash.setRecNo(0);
				ClearData();
				memo="";
				address="";
				shipTo="";
				lstCheckItems=new ArrayList<PurchaseRequestGridData>();
				PurchaseRequestGridData objItems=new PurchaseRequestGridData();
				objItems.setLineNo(1);
				objItems.setIsOrderd("C");
				objItems.setQuantity(1);
				objItems.setRecivedQuantity(0);
				lstCheckItems.add(objItems);
			}


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditPurchaseRequest ----> init", ex);			
		}
	}

	private void ClearData()
	{
		if(compSetup.getPvSerialNos().equals("S"))
		{
			refNUmber=data.GetSaleNumber(SerialFields.PurchaseRequest.toString());
		}
	}
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==311)
			{
				companyRole=item;
				break;
			}
		}
	}


	/*********Contorl Events********************/


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
		getNewTotalAmount();
	}


	//Check Items Grid
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"lstCheckItems","lblCheckItems","totalAmount","hideSite"})
	public void selectCheckItems(@BindingParam("type") final PurchaseRequestGridData type)
	{
		if(type.getSelectedItem()!=null)
		{

			boolean hasSubAccount=data.checkIfItemHasSubQuery(type.getSelectedItem().getName()+":");
			if(hasSubAccount)
			{
				if(compSetup.getPostOnMainClass().equals("Y"))
				{
					Messagebox.show("Selected Item have sub Sub Items(s). Do you want to continue?","Class", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
						public void onEvent(Event evt) throws InterruptedException {
							if (evt.getName().equals("onYes")) 
							{	
								QbListsModel objItems=data.getQbItemsData(type.getSelectedItem().getRecNo());
								if(objItems!=null)
								{
									type.setRate(objItems.getPurchaseCost());
									type.setDecription(objItems.getPurchaseDesc());
									type.setQuantity(type.getQuantity());
									type.setAmount(type.getRate() * type.getQuantity());
									type.setQuantityInHand((int)objItems.getQtyOnHand());
									getNewTotalAmount();
								}
							}
							else 
							{		 
								Map args = new HashMap();
								args.put("result", "3");
								BindUtils.postGlobalCommand(null, null, "resetGrid", args);
								type.setSelectedItem(null);	
								type.setRate(0);
								type.setQuantity(0);
								type.setAmount(0);
								getNewTotalAmount();
								type.setDecription("");
								BindUtils.postNotifyChange(null, null, EditPurchaseRequest.this, "lstCheckItems");
								BindUtils.postNotifyChange(null, null, EditPurchaseRequest.this, "totalAmount");
							}
						}

					});
				}
				else
				{
					Messagebox.show("Selected Item have sub Items(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					type.setSelectedItem(null);	
					type.setRate(0);
					type.setQuantity(0);
					type.setAmount(0);
					getNewTotalAmount();
					type.setDecription("");
					BindUtils.postNotifyChange(null, null, EditPurchaseRequest.this, "lstCheckItems");
					BindUtils.postNotifyChange(null, null, EditPurchaseRequest.this, "totalAmount");
				}	
			}
			else
			{
				QbListsModel objItems=data.getQbItemsData(type.getSelectedItem().getRecNo());
				if(objItems!=null)
				{
					type.setRate(objItems.getPurchaseCost());
					type.setDecription(objItems.getPurchaseDesc());
					type.setQuantity(type.getQuantity());
					type.setAmount(type.getRate() * type.getQuantity());
					getNewTotalAmount();
				}

			}

		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"selectedLstClass"})
	public void selectedClass()
	{
		if(selectedLstClass!=null)
		{
			//check if class has sub class		
			boolean hasSubAccount=data.checkIfClassHasSub(selectedLstClass.getName()+":");
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
								selectedLstClass=null;
								BindUtils.postNotifyChange(null, null, EditPurchaseRequest.this, "selectedLstClass");
							}
						}

					});
				}
				else

				{
					Messagebox.show("Selected Class have sub Class(s). You cannot continue!","Class", Messagebox.OK , Messagebox.INFORMATION);
					selectedLstClass=null;
					BindUtils.postNotifyChange(null, null, EditPurchaseRequest.this, "selectedLstClass");
				}	
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
	public void changeCheckItems(@BindingParam("type") PurchaseRequestGridData type,@BindingParam("parm") String parm)
	{
		if(parm.equals("qty"))
		{
			type.setAmount(type.getRate() * type.getQuantity());
		}

		if(parm.equals("rate"))
		{
			type.setAmount(type.getRate() * type.getQuantity());
		}

		if(parm.equals("amount"))
		{
			double cost=type.getAmount() / type.getQuantity();
			type.setRate(cost);
		}
		getNewTotalAmount();
	}



	private void getNewTotalAmount()
	{
		double toalCheckItemsAmount=0;
		for (PurchaseRequestGridData item : lstCheckItems) 
		{
			toalCheckItemsAmount+=item.getAmount();
		}


		totalAmount=toalCheckItemsAmount;		
	}

	/***********Grid Context Menu****************/

	@Command   
	@NotifyChange({"lstCheckItems","lblCheckItems","totalAmount"})
	public void deleteCheckItems(@BindingParam("row") PurchaseRequestGridData row)
	{
		if(selectedCheckItems!=null)
		{
			//Messagebox.show(String.valueOf(selectedExpenses.getSrNO()));
			lstCheckItems.remove(selectedCheckItems);

			int srNo=0;
			for (PurchaseRequestGridData item : lstCheckItems)
			{
				srNo++;
				item.setLineNo(srNo);
			}

		}
		if(lstCheckItems.size()==0)
		{
			PurchaseRequestGridData objItems=new PurchaseRequestGridData();
			objItems.setLineNo(lstCheckItems.size()+1);
			objItems.setQuantity(1);
			objItems.setIsOrderd("C");
			objItems.setRecivedQuantity(0);
			lstCheckItems.add(objItems);
		}
		getNewTotalAmount();
	}

	@Command
	@NotifyChange({"lstCheckItems"})
	public void insertCheckItems(@BindingParam("row") PurchaseRequestGridData row)
	{
		if(selectedCheckItems!=null)
		{
			PurchaseRequestGridData lastItem=lstCheckItems.get(lstCheckItems.size()-1);
			if(lastItem.getSelectedItem()==null)
			{					
				Messagebox.show("To add new record,First select Item from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			}
			else if(lastItem.getQuantity()==0)
			{
				Messagebox.show("To add new record,First Enter the Quatity to the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			}
			else
			{
				PurchaseRequestGridData objItems=new PurchaseRequestGridData();
				objItems.setLineNo(lstCheckItems.size()+1);
				objItems.setQuantity(1);
				objItems.setIsOrderd("C");
				objItems.setRecivedQuantity(0);
				lstCheckItems.add(objItems);
			}

		}
		else
		{
			Messagebox.show("To add new record,First select Item from the existing record!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
		}

	}



	@Command  
	public void addPurchaseRquest(@BindingParam("cmp") Window x)
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

		/*	if(selectedAccount==null)
		{		
			Messagebox.show("You Must Assign an A/P Account For This Transaction!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}*/
		/*	if(selectedPaytoOrder==null)
		{		
			Messagebox.show("You Must Select A 'Vendor ' !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}

		if(selectedPaytoOrder.getRecNo()==0)
		{			
			Messagebox.show("Select An Existing 'Vendor' !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}*/

		/*if(selectedDropShipTo==null)
		{		
			Messagebox.show("You have not selected the Drop Ship to Value. Do you want to continue?","Account", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
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

		if(selectedDropShipTo.getRecNo()==0)
		{			
			Messagebox.show("Select An Existing value for 'Drop Ship To '!!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}*/

		if(lstCheckItems!=null)
		{	
			for(PurchaseRequestGridData gridData:lstCheckItems)
			{
				PurchaseRequestGridData lastItem=gridData;
				if(lastItem.getSelectedItem()==null)
				{					
					Messagebox.show("You Connot Continue.!Please select the Item from the grid","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
					return false;
				}

				PurchaseRequestGridData objExp=gridData;
				if(objExp.getSelectedItem()!=null)
				{
					if(objExp.getQuantity()==0)
					{	 		
						Messagebox.show("Please Enter The Qantity,Empty Transaction is not allowed !!!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
						return false;
					}

				}
			}

		}


		if(Printflag)
		{
			if((refNUmber!=null) && (data.checkIfSerialNumberIsDuplicateForPurchaseRequest(refNUmber,objCash.getRecNo())==true))
			{
				Messagebox.show("Duplicate Purchase referance Number!","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
				return false; 
			}
			if(refNUmber==null)
			{
				Messagebox.show("Please Enter Purchase referance Number","Item Receipt",Messagebox.OK,Messagebox.INFORMATION);
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



	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"objCashInvoice","lstCashInvoiceCheckItems"})
	public void clearPurchaseRequest()
	{
		if(true)
		{

			Messagebox.show("Are you sure to Clear Purchase Request ? Your Data will be lost.!", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt) throws InterruptedException {
					if (evt.getName().equals("onYes")) 
					{	
						lstCheckItems=new ArrayList<PurchaseRequestGridData>()		;
						objCash=new PurchaseRequestModel();
					}
					else 
					{				        	
						return;
					}
				}

			});	

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveData() 
	{	      
		try
		{			
			int tmpRecNo=0;
			if(purchaseRequestKey==0)
			{
				tmpRecNo=data.GetNewPurchaseRequestRecNo();	
			}
			else
			{
				tmpRecNo=objCash.getRecNo();
			}
			PurchaseRequestModel obj=new PurchaseRequestModel();
			obj.setRecNo(tmpRecNo);
			obj.setRefNUmber(refNUmber);
			obj.setTxtnDate(creationdate);//(objCheque.getPvDate());
			if(selectedPaytoOrder!=null)
				obj.setVendorRefKEy(selectedPaytoOrder.getRecNo());
			else
				obj.setVendorRefKEy(0);	
			if(selectedDropShipTo!=null)
				obj.setEntityRefKey(selectedDropShipTo.getRecNo());
			else
				obj.setEntityRefKey(0);
			obj.setAmount(totalAmount);
			if(selectedLstClass!=null)
				obj.setClassRefkey(selectedLstClass.getClass_Key());
			else
				obj.setClassRefkey(0);	
			obj.setStatus("C");
			obj.setSource("ONL");
			obj.setWebUserId(webUserID);
			obj.setMemo(memo);
			obj.setAdress(address);
			if(shipTo!=null && !shipTo.equalsIgnoreCase(""))
				obj.setShipTo(shipTo);
			else
				obj.setShipTo("");	
			int result=0;
			if(purchaseRequestKey==0)
			{
				result=data.addNewPurchaseRqeuest(obj);
			}else
			{
				result=data.updateExistingPurchaseRequest(obj);
			}
			if(result>0)
			{
				if(purchaseRequestKey==0)//Only on create
				{
					data.ConfigSerialNumberCashInvoice(SerialFields.PurchaseRequest, obj.getRefNUmber(),0);
				}
				data.deleteGridDataPurchaseRequest(tmpRecNo);
				for (PurchaseRequestGridData item : lstCheckItems) 
				{
					if(item.getSelectedItem()!=null)
						data.addGridDataPurchaseRequest(item, tmpRecNo);	
				}
			}
			if(purchaseRequestKey>0)
			{
				Clients.showNotification("The Purchase Request Has Been Updated Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentPurchaseRequest", args);		   	        					        
			}
			else
			{
				Clients.showNotification("The Purchase Request Has Been Created Successfully.",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				Map args = new HashMap();
				BindUtils.postGlobalCommand(null, null, "refreshParentPurchaseRequest", args);		   		        					        
			}

		}catch (Exception ex) 
		{
			logger.error("ERROR in EditPurchaseRequest ----> Save", ex);			
		}
	}


	/*******************Getter and Setter********************/
	public PurchaseRequestModel getObjCash() {
		return objCash;
	}

	public void setObjCash(PurchaseRequestModel objCash) {
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


	public List<ClassModel> getLstClass() {
		return lstClass;
	}

	public void setLstClass(List<ClassModel> lstClass) {
		this.lstClass = lstClass;
	}

	public ClassModel getSelectedLstClass() {
		return selectedLstClass;
	}

	public void setSelectedLstClass(ClassModel selectedLstClass) {
		this.selectedLstClass = selectedLstClass;
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

	@NotifyChange({"objCash"})
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
				address=selectedPaytoOrder.getFullName();
			}
			objCash.setAdress(address);
		}
		else
		{
			Messagebox.show("Invlaid Name.");			
		}
	}





	@Command
	@NotifyChange({"shipTo","address","lblExpenses","lblCheckItems","refNUmber","objCash","memo","selectedDropShipTo","selectedLstClass","itemReceiptKey","creationdate","labelStatus","selectedAccount","selectedPaytoOrder","lectedInvcCutomerSendVia","toatlAmount","lstCheckItems","lstExpenses","totalAmount","tempTotalAmount","actionTYpe"})
	public void navigationPurchaseRequest(@BindingParam("cmp") String navigation)
	{
		try
		{
			objCash=data.navigationPurchaseRequest(purchaseRequestKey,webUserID,seeTrasction,navigation,actionTYpe);
			if(objCash!=null && objCash.getRecNo()>0)
			{
				actionTYpe="edit";
				labelStatus="Edit";
				purchaseRequestKey=objCash.getRecNo();
				List<PurchaseRequestGridData> itemsGrid=data.getGridDataPurchaseRequestById(purchaseRequestKey);
				for(ClassModel apAcounts:lstClass)
				{
					if(apAcounts.getClass_Key()==objCash.getClassRefkey())
					{
						selectedLstClass=apAcounts;
						break;
					}
					else
					{
						selectedLstClass=null;
					}

				}

				for(QbListsModel vendorList:lstPayToOrder)
				{
					if(vendorList.getRecNo()==objCash.getVendorRefKEy())
					{
						selectedPaytoOrder=vendorList;
						break;
					}

				}

				for(QbListsModel dropShipToTemp:lstDropShipTo)
				{
					if(dropShipToTemp.getRecNo()==objCash.getEntityRefKey())
					{
						selectedDropShipTo=dropShipToTemp;
						break;
					}
					else
					{
						selectedDropShipTo=null;
					}


				}

				totalAmount=objCash.getAmount();
				creationdate=df.parse(sdf.format(objCash.getTxtnDate()));
				//setSelectedPaytoOrder(selectedPaytoOrder);
				//setSelectedDropShipTo(selectedDropShipTo);
				address=objCash.getAdress();
				shipTo=objCash.getAdressBillTo();
				refNUmber=objCash.getRefNUmber();
				memo=objCash.getMemo();
				lstCheckItems=new ArrayList<PurchaseRequestGridData>();
				for(PurchaseRequestGridData editItemsGrid:itemsGrid)
				{
					PurchaseRequestGridData obj=new PurchaseRequestGridData();
					obj.setLineNo(lstCheckItems.size()+1);

					for(QbListsModel items:lstGridQBItems)
					{
						if(items.getRecNo()==editItemsGrid.getItemrefKey())
						{
							obj.setSelectedItem(items);
							break;
						}

					}

					for(QbListsModel gridCutomer:lstGridCustomer)
					{
						if(gridCutomer.getRecNo()==editItemsGrid.getEntityRefKey())
						{
							obj.setSelctedCustomer(gridCutomer);
							break;
						}
						else
						{
							obj.setSelctedCustomer(null);
						}

					}
					obj.setDecription(editItemsGrid.getDecription());
					obj.setRecNo(editItemsGrid.getRecNo());
					obj.setAmount(editItemsGrid.getAmount());
					obj.setQuantity(editItemsGrid.getQuantity());
					obj.setRate(editItemsGrid.getRate());
					obj.setIsOrderd(editItemsGrid.getIsOrderd());
					obj.setRecivedQuantity(editItemsGrid.getRecivedQuantity());

					lstCheckItems.add(obj);
				}
				getNewTotalAmount();

			}
			else
			{
				actionTYpe="create";
				purchaseRequestKey=0;
				labelStatus="Create";
				totalAmount=0;
				Calendar c = Calendar.getInstance();			
				creationdate=df.parse(sdf.format(c.getTime()));
				objCash=new PurchaseRequestModel();
				address="";
				shipTo="";
				//selectedAccount=null;
				selectedPaytoOrder=null;
				selectedDropShipTo=null;
				objCash.setTxtnDate(df.parse(sdf.format(c.getTime())));	
				objCash.setRecNo(0);
				ClearData();
				selectedLstClass=null;
				memo="";
				lstCheckItems=new ArrayList<PurchaseRequestGridData>();
				PurchaseRequestGridData objItems=new PurchaseRequestGridData();
				objItems.setLineNo(1);
				objItems.setQuantity(1);
				objItems.setIsOrderd("C");
				lstCheckItems.add(objItems);
				getNewTotalAmount();
			}



		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditPurchaseRequest ----> navigationPurchaseRequest", ex);		
		}
	}


	@Command
	@NotifyChange({"shipTo","address","lblExpenses","lblCheckItems","refNUmber","objCash","memo","selectedDropShipTo","selectedLstClass","itemReceiptKey","creationdate","labelStatus","selectedAccount","selectedPaytoOrder","lectedInvcCutomerSendVia","toatlAmount","lstCheckItems","lstExpenses","totalAmount","tempTotalAmount","actionTYpe"})
	public void copyFunctinality()
	{
		try
		{
			if(purchaseRequestKey>0)
			{
				objCash=data.getPurchaseRequestByID(purchaseRequestKey,webUserID,seeTrasction);
				if(objCash!=null && objCash.getRecNo()>0)
				{
					actionTYpe="edit";
					labelStatus="Copied-Create";
					purchaseRequestKey=objCash.getRecNo();
					List<PurchaseRequestGridData> itemsGrid=data.getGridDataPurchaseRequestById(purchaseRequestKey);
					for(ClassModel apAcounts:lstClass)
					{
						if(apAcounts.getClass_Key()==objCash.getClassRefkey())
						{
							selectedLstClass=apAcounts;
							break;
						}
						else
						{
							selectedLstClass=null;
						}

					}

					for(QbListsModel vendorList:lstPayToOrder)
					{
						if(vendorList.getRecNo()==objCash.getVendorRefKEy())
						{
							selectedPaytoOrder=vendorList;
							break;
						}

					}

					for(QbListsModel dropShipToTemp:lstDropShipTo)
					{
						if(dropShipToTemp.getRecNo()==objCash.getEntityRefKey())
						{
							selectedDropShipTo=dropShipToTemp;
							break;
						}
						else
						{
							selectedDropShipTo=null;
						}


					}

					totalAmount=objCash.getAmount();
					Calendar c = Calendar.getInstance();			
					creationdate=df.parse(sdf.format(c.getTime()));
					//setSelectedPaytoOrder(selectedPaytoOrder);
					//setSelectedDropShipTo(selectedDropShipTo);
					address=objCash.getAdress();
					shipTo=objCash.getAdressBillTo();
					objCash.setTxtnDate(df.parse(sdf.format(c.getTime())));	
					objCash.setRecNo(0);
					purchaseRequestKey=0;
					refNUmber=objCash.getRefNUmber();
					memo=objCash.getMemo();
					ClearData();
					lstCheckItems=new ArrayList<PurchaseRequestGridData>();
					for(PurchaseRequestGridData editItemsGrid:itemsGrid)
					{
						PurchaseRequestGridData obj=new PurchaseRequestGridData();
						obj.setLineNo(lstCheckItems.size()+1);

						for(QbListsModel items:lstGridQBItems)
						{
							if(items.getRecNo()==editItemsGrid.getItemrefKey())
							{
								obj.setSelectedItem(items);
								break;
							}

						}

						for(QbListsModel gridCutomer:lstGridCustomer)
						{
							if(gridCutomer.getRecNo()==editItemsGrid.getEntityRefKey())
							{
								obj.setSelctedCustomer(gridCutomer);
								break;
							}
							else
							{
								obj.setSelctedCustomer(null);
							}

						}
						obj.setDecription(editItemsGrid.getDecription());
						obj.setRecNo(editItemsGrid.getRecNo());
						obj.setAmount(editItemsGrid.getAmount());
						obj.setQuantity(editItemsGrid.getQuantity());
						obj.setRate(editItemsGrid.getRate());
						obj.setIsOrderd(editItemsGrid.getIsOrderd());
						obj.setRecivedQuantity(editItemsGrid.getRecivedQuantity());

						lstCheckItems.add(obj);
					}
					getNewTotalAmount();

				}


			}
			else
			{
				Messagebox.show("You can only copy a existing Purchase Request","Purchase Request", Messagebox.OK , Messagebox.INFORMATION);
				return;
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditPurchaseRequest ----> copyFunctinality", ex);		
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

	public List<PurchaseRequestGridData> getLstCheckItems() {
		return lstCheckItems;
	}

	public void setLstCheckItems(List<PurchaseRequestGridData> lstCheckItems) {
		this.lstCheckItems = lstCheckItems;
	}

	public PurchaseRequestGridData getSelectedCheckItems() {
		return selectedCheckItems;
	}

	public void setSelectedCheckItems(PurchaseRequestGridData selectedCheckItems) {
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



	public int getPurchaseRequestKey() {
		return purchaseRequestKey;
	}

	public void setPurchaseRequestKey(int purchaseRequestKey) {
		this.purchaseRequestKey = purchaseRequestKey;
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


	public List<QbListsModel> getLstDropShipTo() {
		return lstDropShipTo;
	}

	public void setLstDropShipTo(List<QbListsModel> lstDropShipTo) {
		this.lstDropShipTo = lstDropShipTo;
	}

	public QbListsModel getSelectedDropShipTo() {
		return selectedDropShipTo;
	}

	@NotifyChange({"objCash"})
	public void setSelectedDropShipTo(QbListsModel selectedDropShipTo) {
		this.selectedDropShipTo = selectedDropShipTo;
		if(selectedDropShipTo!=null)
		{
			PayToOrderModel obj=data.getPayToOrderInfo(selectedDropShipTo.getListType(), selectedDropShipTo.getRecNo());		
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
				address=selectedDropShipTo.getFullName();
			}
			objCash.setAdressBillTo(address);
		}
		else
		{
			Messagebox.show("Invlaid Name.");			
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
			newTab.setLabel("Purchase Request Report");
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
			logger.error("ERROR in EditPurchaseRequest ----> goToRelatedReport", ex);			
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
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/temp/invoicePDFWebApplication.pdf"));
				writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
				HeaderFooter event = new HeaderFooter();
				writer.setPageEvent(event);

				// various fonts
				BaseFont bf_helv = BaseFont.createFont(BaseFont.HELVETICA, "Cp1252", false);
				BaseFont bf_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp1252", false);
				BaseFont bf_courier = BaseFont.createFont(BaseFont.COURIER, "Cp1252", false);
				BaseFont bf_symbol = BaseFont.createFont(BaseFont.SYMBOL, "Cp1252", false);

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
				Chunk c = new Chunk("Meterial Request");
				c.setUnderline(0.1f, -2f);
				c.setFont(f1);
				Paragraph p = new Paragraph(c);

				firsttbl.addCell(p);

				PdfPCell cell1 = new PdfPCell(new Phrase("Date :"+sdf.format(objCash.getTxtnDate())+"\n\n"+"Meterial Request No."+refNUmber));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setColspan(3);
				cell1.disableBorderSide(Rectangle.BOX);
				firsttbl.addCell(cell1);
				document.add(firsttbl);

				String shipToName="";
				if(selectedDropShipTo!=null)
				{
					shipToName=selectedDropShipTo.getName();
				}
				else
				{
					shipToName="";
				}

				/*------------------------------------------------------------------------*/
				PdfPTable tbl1 = new PdfPTable(1);
				tbl1.setWidthPercentage(100);

				cell1 = new PdfPCell(new Phrase("Request To ,"));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("M/S : "+selectedPaytoOrder.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				tbl1.addCell(cell1);

				cell1 = new PdfPCell(new Phrase("\nShip To Address : "+shipToName+"\n"+objCash.getShipTo()));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.disableBorderSide(Rectangle.BOX);
				tbl1.addCell(cell1);
				document.add(tbl1);

				/*---------------------------------------------------------------*/ 

				paragraph=new Paragraph();
				Chunk chunk = new Chunk("\nMemo : "+memo);
				paragraph.add(chunk);
				paragraph.setAlignment(Element.ALIGN_LEFT);
				document.add(paragraph);

				document.add( Chunk.NEWLINE );

				PdfPTable table = new PdfPTable(5);
				objPrint.setSrNoWidth(0);
				objPrint.setQuantityWidth(0);
				objPrint.setRateWidth(0);
				objPrint.setAmountWidth(0);
				objPrint.setWordAmountWidth(0);
								
				if(!objPrint.isHideSrNo())
				objPrint.setSrNoWidth(100);
				if(!objPrint.isHideQuantity())
					objPrint.setQuantityWidth(40);
				if(!objPrint.isHideRate())
					objPrint.setRateWidth(60);
				if(!objPrint.isHideAmount())
					objPrint.setAmountWidth(60);		
				
				table.setWidths(new int[]{objPrint.getSrNoWidth(),210,objPrint.getQuantityWidth(),objPrint.getRateWidth(),objPrint.getAmountWidth()});
				
				
				table.setWidthPercentage(100);
				table.getDefaultCell().setPadding(5);
				//table.setWidths(new int[] {100,210,50,50,60});

				PdfPCell HeadderProduct = new PdfPCell(new Phrase("Item"));
				HeadderProduct.setBorder(Rectangle.NO_BORDER);
				HeadderProduct.setHorizontalAlignment(Element.ALIGN_CENTER);
				BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
				HeadderProduct.setBackgroundColor(myColor);
				table.addCell(HeadderProduct);
				table.setHeaderRows(1);

				PdfPCell HeadderDate = new PdfPCell(new Phrase("Description"));
				HeadderDate.setBorder(Rectangle.NO_BORDER);
				HeadderDate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderDate.setBackgroundColor(myColor);
				table.addCell(HeadderDate);

				PdfPCell HeadderQty = new PdfPCell(new Phrase("Quantity"));
				HeadderQty.setBorder(Rectangle.NO_BORDER);
				HeadderQty.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderQty.setBackgroundColor(myColor);
				table.addCell(HeadderQty);

				PdfPCell HeadderBill = new PdfPCell(new Phrase("Rate"));

				HeadderBill.setBorder(Rectangle.NO_BORDER);
				HeadderBill.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderBill.setBackgroundColor(myColor);
				table.addCell(HeadderBill);

				/*PdfPCell HeadderRate = new PdfPCell(new Phrase("Customer"));

				HeadderRate.setBorder(Rectangle.NO_BORDER);
				HeadderRate.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderRate.setBackgroundColor(myColor);
				table.addCell(HeadderRate);*/


				PdfPCell HeadderAmount1 = new PdfPCell(new Phrase("Amount"));
				HeadderAmount1.setBorder(Rectangle.NO_BORDER);
				HeadderAmount1.setHorizontalAlignment(Element.ALIGN_CENTER);
				HeadderAmount1.setBackgroundColor(myColor);
				table.addCell(HeadderAmount1);
				boolean desc=true;

				for (PurchaseRequestGridData item : lstCheckItems) 
				{
					
					if(item.getSelectedItem().getName()!=null && !item.getSelectedItem().getName().equalsIgnoreCase(""))
					{
						cell1 = new PdfPCell(new Phrase(item.getSelectedItem().getName()));
						cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
						table.addCell(cell1);
					}else
						table.addCell("-");
					
					if(item.getDecription()!=null && !item.getDecription().equalsIgnoreCase(""))
					{
						cell1 = new PdfPCell(new Phrase(item.getDecription()));
						cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
						table.addCell(cell1);
					}else
						table.addCell("-");
					
					if(item.getQuantity()>0)
					{
						cell1 = new PdfPCell(new Phrase(""+item.getQuantity()));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell1);
					}else
						table.addCell("-");
					
					if(item.getRate()>0)
					{
						cell1 = new PdfPCell(new Phrase(""+item.getRate()));
						cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell1);
					}else
						table.addCell("-");

					
					/*if(item.getSelctedCustomer()!=null)
						table.addCell(""+item.getSelctedCustomer().getName());
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

				for(PdfPRow r: table.getRows()) {
					for(PdfPCell c1: r.getCells()) {
						c1.setBorder(Rectangle.NO_BORDER);
					}
				}

				document.add(table);

				document.add( Chunk.NEWLINE );
				
				PdfPTable totaltbl = new PdfPTable(2);
				totaltbl.setWidthPercentage(100);
				totaltbl.getDefaultCell().setBorder(0);
				totaltbl.setWidths(new int[]{350,100});
				cell1 = new PdfPCell(new Phrase(numbToWord.GetFigToWord(totalAmount), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setBackgroundColor(myColor);
				cell1.disableBorderSide(Rectangle.BOX);
				cell1.setBorder(0);
				totaltbl.addCell(cell1);

				String amtStr1 = BigDecimal.valueOf(totalAmount)
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
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );
				document.add( Chunk.NEWLINE );

				if(msgToBeDispalyedOnInvoice==null || msgToBeDispalyedOnInvoice.equalsIgnoreCase(""))
				{
					paragraph=new Paragraph();
					chunk = new Chunk(" ", FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);

				}else{

					paragraph=new Paragraph();
					chunk = new Chunk(msgToBeDispalyedOnInvoice, FontFactory.getFont(FontFactory.HELVETICA_BOLD));
					paragraph.add(chunk);
					paragraph.setAlignment(Element.ALIGN_LEFT);
					document.add(paragraph);

				}
				document.close();

				if (!createPdfSendEmail) {
					previewPdfForprintingInvoice();
				}


			} catch (Exception ex) {
				logger.error("ERROR in EditPurchaseRequest ----> createPdfForPrinting", ex);
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
			logger.error("ERROR in EditPurchaseRequest ----> previewPdfForprintingInvoice", ex);			
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
						"ERROR in EditPurchaseRequest class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in EditPurchaseRequest class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in EditPurchaseRequest class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in EditPurchaseRequest class HeaderFooter PDf----> onEndPage",
						e);
			}
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getRefNUmber() {
		return refNUmber;
	}

	public void setRefNUmber(String refNUmber) {
		this.refNUmber = refNUmber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getWebUserName() {
		return webUserName;
	}

	public void setWebUserName(String webUserName) {
		this.webUserName = webUserName;
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
	public void CustomerSendEmail(@BindingParam("row") final PurchaseRequestModel row) {
		if (validateData(false)) {
			lstAtt = new ArrayList<QuotationAttachmentModel>();
			selectedAttchemnets.setFilename(selectedPaytoOrder.getFullName()+ " Meterial Request.pdf");
			selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
			lstAtt.add(selectedAttchemnets);
			Messagebox.show("Do you want to Preview The Meterial Request?",	"Meterial Request", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
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
							arg.put("id", row.getVendorRefKEy());
							arg.put("lstAtt", lstAtt);
							arg.put("feedBackKey", 0);
							arg.put("formType", "Customer");
							arg.put("type", "OtherForms");
							Executions.createComponents("/crm/editCustomerFeedbackSend.zul",null, arg);
						} catch (Exception ex) {
							logger.error("ERROR in EditPurchaseRequest ----> CustomerSendEmail",ex);
						}
					}
				}
			});
		}
	}




}
