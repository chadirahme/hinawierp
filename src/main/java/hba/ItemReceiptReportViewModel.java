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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuData;
import layout.MenuModel;
import model.AccountsModel;
import model.CheckItemsModel;
import model.ClassModel;
import model.CompSetupModel;
import model.DataFilter;
import model.ExpensesModel;
import model.HRListValuesModel;
import model.ItemReceiptModel;
import model.ItemReceiptReportModel;
import model.PrintModel;
import model.PurchaseRequestReportModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.excel.ExcelExporter.ExportContext;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.xssf.usermodel.XSSFCellStyle;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

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

public class ItemReceiptReportViewModel {

	private Logger logger = Logger.getLogger(this.getClass());

	private DataFilter filter=new DataFilter();

	private Date creationdate; 
	private ItemReceiptModel objCash;


	private List<ItemReceiptReportModel> invoiceSalesReport;

	private List<ItemReceiptReportModel> allInvoiceSalesReport;


	private Date fromDate;

	private Date toDate;



	private List<AccountsModel> lstaccounts;
	private AccountsModel selectedAccount;
	private List<QbListsModel> lstPayToOrder;
	private QbListsModel selectedPaytoOrder;

	int tmpUnitKey=0;
	private double totalAmount;
	private boolean chkTobePrinted=true;

	private String lblExpenses;
	private String lblCheckItems;

	DecimalFormat formatter = new DecimalFormat("0.00");

	private List<ExpensesModel> lstExpenses;
	private ExpensesModel selectedExpenses;
	private List<AccountsModel> lstGridBankAccounts;
	private List <QbListsModel> lstGridCustomer;
	private  List<ClassModel> lstGridClass;

	//CheckItems
	private List<CheckItemsModel> lstCheckItems;
	private CheckItemsModel selectedCheckItems;
	private List <QbListsModel> lstGridQBItems;

	private ItemReceiptGroupModel reportGroupModel;

	private boolean isOpenGroup;
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private HRData hrData = new HRData();
	private String webUserName="";
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	NumberToWord numbToWord = new NumberToWord();



	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	HBAData data=new HBAData();

	private List <QbListsModel> lstInvcCustomerGridInvrtySite;
	private int webUserID=0;

	private CompSetupModel compSetup;

	private MenuModel companyRole;

	MenuData menuData=new MenuData();

	List<MenuModel> list;

	private boolean adminUser;

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	CompanyData companyData=new CompanyData();

	private String msgToBeDispalyedOnInvoice="";
	
	private boolean seeTrasction=false;
	private String printYear="";	
	private PrintModel objPrint;

	public ItemReceiptReportViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Calendar c = Calendar.getInstance();	
			objPrint=new PrintModel();
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
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
			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			for(WebusersModel model:lstUsers)
			{
				if(model.getUserid()==dbUser.getUserid())
				{
					selectedUser=model;
					break;
				}
			}
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

			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction
			//Calendar c = Calendar.getInstance();	
			/*creationdate=df.parse(sdf.format(c.getTime()));
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));*/
			printYear=new SimpleDateFormat("yyyy").format(c.getTime());

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> init", ex);			
		}
	}



	@Command
	@NotifyChange({"reportGroupModel","isOpenGroup"})
	public void colseGroup()
	{
		if(invoiceSalesReport==null)
		{   		   
			return;
		}

		isOpenGroup=!isOpenGroup;
		for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
		{
			if(isOpenGroup==false)
				reportGroupModel.removeOpenGroup(i);
			else
				reportGroupModel.addOpenGroup(i);
		} 
	}

	private List<ItemReceiptReportModel> filterData()
	{
		invoiceSalesReport=allInvoiceSalesReport;
		List<ItemReceiptReportModel>  lst=new ArrayList<ItemReceiptReportModel>();
		if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
		{
			for (Iterator<ItemReceiptReportModel> i = invoiceSalesReport.iterator(); i.hasNext();)
			{
				ItemReceiptReportModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						&&tmp.getIrDate().toLowerCase().contains(filter.getIrDate().toLowerCase())&&
						tmp.getIrLocalNo().toLowerCase().contains(filter.getIrLocalNo().toLowerCase())&&
						tmp.getVendor().toLowerCase().contains(filter.getVendor().toLowerCase())
						&&(tmp.getStatus()).toLowerCase().contains(filter.getStatus().toLowerCase())&&
						(tmp.getMainMemo()).toLowerCase().contains(filter.getMainMemo().toLowerCase())&&
						(tmp.getItemName()).toLowerCase().contains(filter.getItemName().toLowerCase())&&
						(tmp.getDescription()).toLowerCase().contains(filter.getDescription().toLowerCase())&&
						(tmp.getQuantity()+"").toLowerCase().contains(filter.getQuantity().toLowerCase())&&
						tmp.getClassName().toLowerCase().contains(filter.getClassName().toLowerCase())&&
						(tmp.getAmount()+"").toLowerCase().contains(filter.getAmount().toLowerCase())
						)
				{
					lst.add(tmp);
				}
				reportGroupModel=new ItemReceiptGroupModel(lst, new ItemReceiptComparator(), true);
				for (int j = 0; j < reportGroupModel.getGroupCount(); j++)
				{
					reportGroupModel.removeOpenGroup(j);
				}

			}
		}
		return lst;

	}

	@Command
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void changeFilter() 
	{
		try
		{
			invoiceSalesReport=filterData();
			//calcAmonut();

		}
		catch (Exception ex) {
			logger.error("error in ItemReceiptReportViewModel---changeFilter-->" , ex);
		}

	}

	@Command
	public void resetCashInvoiceReport()
	{
		try
		{
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> resetCashInvoiceReport", ex);			
		}
	}

	public ItemReceiptReportModel getPrevious(ItemReceiptReportModel uid) {
		int idx = invoiceSalesReport.indexOf(uid);
		if (idx <= 0) return null;
		return invoiceSalesReport.get(idx - 1);
	}
	/* public void calcAmonut()
	 {
		 totalAmount=0;
		 totalNoOfCustomer=0;
		 totalSale=0;
		 int incremnetor=0;
		 for(CashInvoiceModel report:invoiceSalesReport)
		 {
			 CashInvoiceModel report1=getPrevious(report);
			 if(report1!=null)
			 {
				 if(!report.getInvoiceSaleNo().equalsIgnoreCase(report1.getInvoiceSaleNo()))
				 {
					 incremnetor=incremnetor+1;

				 }
			 }
			 totalAmount=totalAmount+report.getLineAmount();
		 }
		 totalNoOfCustomer=incremnetor;
		 totalAmountStr=formatter.format(totalAmount);
		 totalNoOfInvoice=reportGroupModel.getGroupCount();
	 }
	 */
	@Command
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void searchCommand()
	{
		try
		{
			invoiceSalesReport=data.getItemReceiptReport(webUserID,seeTrasction,fromDate,toDate);
			allInvoiceSalesReport=invoiceSalesReport;
			reportGroupModel=new ItemReceiptGroupModel(invoiceSalesReport, new ItemReceiptComparator(), true);
			for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			{
				reportGroupModel.removeOpenGroup(i);
			}
			//calcAmonut();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> searchCommand", ex);			
		}
	}

	@SuppressWarnings("unused")
	@Command
	public void exportToExcel(@BindingParam("ref") Listbox grid)
	{
		try
		{
			if(invoiceSalesReport==null)
			{
				Messagebox.show("There is are record !!","Item Receipt Report", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			final ExcelExporter exporter = new ExcelExporter();
			String[] tsHeaders;
			//tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","NO.of Days","Present Days","Off Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
			tsHeaders = new String[]{"No.","Date", "Account No.","Vendor","Memo","ItemName","Description","Quantity","Description","Qty","Status","Cutomer","Class","Amount"};
			final String[] headers=tsHeaders;
			String[] tsFooters;

			// tsFooters = new String[]{"Total No. Of Quotation :"+totalNoOfInvoice+"","Total Customers :"+totalNoOfCustomer+"","Total Amount :"+formatter.format(totalAmount)+""};
			// final String[] footers=tsFooters;

			exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {

				@SuppressWarnings("static-access")
				@Override
				public void beforeRendering(XSSFWorkbook target) {
					ExportContext context = exporter.getExportContext();
					CellStyle cs = target.createCellStyle();
					cs.setWrapText(true);   //Wrapping text

					for (String header : headers) {
						Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
						cell.setCellValue(header);
						cell.setCellStyle(cs);

						CellStyle srcStyle = cell.getCellStyle();
						if (srcStyle.getAlignment() != CellStyle.ALIGN_CENTER) {				                   
							XSSFCellStyle newCellStyle = target.createCellStyle();
							newCellStyle.cloneStyleFrom(srcStyle);
							newCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
							cell.setCellStyle(newCellStyle);
						}

					}
				}

				@Override
				public void afterRendering(XSSFWorkbook target) {
					ExportContext context = exporter.getExportContext();
					CellStyle cs = target.createCellStyle();
					cs.setWrapText(true);   //Wrapping text
					/* for (String foot : footers) {
						            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
						            cell.setCellValue(foot);
						            cell.setCellStyle(cs);

						                CellStyle srcStyle = cell.getCellStyle();
						                if (srcStyle.getAlignment() != CellStyle.ALIGN_CENTER) {				                   
											XSSFCellStyle newCellStyle = target.createCellStyle();
						                    newCellStyle.cloneStyleFrom(srcStyle);
						                    newCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
						                    cell.setCellStyle(newCellStyle);
						                }

						        }*/
				}				    		  
			});

			exporter.export(headers.length, invoiceSalesReport, new RowRenderer<Row, ItemReceiptReportModel>() {
				@SuppressWarnings("static-access")
				@Override
				public void render(Row table, ItemReceiptReportModel item, boolean isOddRow) 
				{
					ExportContext context = exporter.getExportContext();
					XSSFSheet sheet = context.getSheet();	
					sheet.autoSizeColumn(5);
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getIrLocalNo());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getIrDate());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAccountNumber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getVendor());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getMainMemo());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getItemName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDescription());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getStatus());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomerName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getClassName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAmount());
				}

			}, out);

			AMedia amedia = new AMedia("ItemReceipt.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> exportToExcel", ex);			
		}
	}



	@GlobalCommand 
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void refreshParentItemReceipt(@BindingParam("type")String type)
	{		
		try
		{
			invoiceSalesReport=data.getItemReceiptReport(webUserID,seeTrasction,fromDate,toDate);
			allInvoiceSalesReport=invoiceSalesReport;
			reportGroupModel=new ItemReceiptGroupModel(invoiceSalesReport, new ItemReceiptComparator(), true);
			for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			{
				reportGroupModel.removeOpenGroup(i);
			}
			//calcAmonut();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> refreshParentCashInvoice", ex);			
		}
	}


	@Command
	public void createItemReceipt()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("itemReceiptKey",0);
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editItemReceipt.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> createItemReceipt", ex);			
		}
	}




	//edit vendor list
	@Command
	public void editItemReceipt(@BindingParam("row") ItemReceiptReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("itemReceiptKey", row.getMastRecoNO());
			arg.put("type", "edit");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editItemReceipt.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> editItemReceipt", ex);			
		}
	}


	@Command
	public void viewItemReceipt(@BindingParam("row") ItemReceiptReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("itemReceiptKey", row.getMastRecoNO());
			arg.put("type", "view");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editItemReceipt.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ItemReceiptReportViewModel ----> viewItemReceipt", ex);			
		}
	}
	
	@Command
	@NotifyChange({"invoiceSalesReport"})
	public void approveIRCommand(@BindingParam("row") ItemReceiptReportModel row)
	{
		try 
		{
			int result=0;				
			result=data.updateItemReceiptStatus(row.getMastRecoNO()+"");
			if(result>0)
			{
				Clients.showNotification("The Item Receipt Has Been Approved Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);				
			}
			else
			{
				Clients.showNotification("Error, The Item Receipt is Not Approved",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			
			searchCommand();	
	
		} 
		catch (Exception ex) {
			logger.error("ERROR in ItemReceiptReportViewModel ----> approveIRCommand",ex);
		}
	}
	
	@Command	
	public void ChangeToBillCommand(@BindingParam("row") ItemReceiptReportModel row)
	{
		try 
		{			
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("editBillKey",0);
			arg.put("itemReceiptKey", row.getMastRecoNO());
			arg.put("type", "create");
			arg.put("vendorKey", row.getVendorKey());
			arg.put("changeToBill",true);
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			Executions.createComponents("/hba/payments/editCreditBill.zul", null,arg);
									
		} 
		catch (Exception ex) {
			logger.error("ERROR in ItemReceiptReportViewModel ----> ChangeToBillCommand",ex);
		}
	}


	public Logger getLogger() {
		return logger;
	}



	public void setLogger(Logger logger) {
		this.logger = logger;
	}



	public ItemReceiptModel getObjCash() {
		return objCash;
	}



	public void setObjCash(ItemReceiptModel objCash) {
		this.objCash = objCash;
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



	public List<QbListsModel> getLstPayToOrder() {
		return lstPayToOrder;
	}



	public void setLstPayToOrder(List<QbListsModel> lstPayToOrder) {
		this.lstPayToOrder = lstPayToOrder;
	}



	public QbListsModel getSelectedPaytoOrder() {
		return selectedPaytoOrder;
	}



	public void setSelectedPaytoOrder(QbListsModel selectedPaytoOrder) {
		this.selectedPaytoOrder = selectedPaytoOrder;
	}



	public int getTmpUnitKey() {
		return tmpUnitKey;
	}



	public void setTmpUnitKey(int tmpUnitKey) {
		this.tmpUnitKey = tmpUnitKey;
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



	public List<QbListsModel> getLstGridCustomer() {
		return lstGridCustomer;
	}



	public void setLstGridCustomer(List<QbListsModel> lstGridCustomer) {
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



	public List<QbListsModel> getLstGridQBItems() {
		return lstGridQBItems;
	}



	public void setLstGridQBItems(List<QbListsModel> lstGridQBItems) {
		this.lstGridQBItems = lstGridQBItems;
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



	public HBAData getData() {
		return data;
	}



	public void setData(HBAData data) {
		this.data = data;
	}



	public CompSetupModel getCompSetup() {
		return compSetup;
	}



	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}



	public CompanyData getCompanyData() {
		return companyData;
	}



	public void setCompanyData(CompanyData companyData) {
		this.companyData = companyData;
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


	public boolean isAdminUser() {
		return adminUser;
	}


	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}


	public DecimalFormat getFormatter() {
		return formatter;
	}


	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
	}




	public List<QbListsModel> getLstInvcCustomerGridInvrtySite() {
		return lstInvcCustomerGridInvrtySite;
	}


	public void setLstInvcCustomerGridInvrtySite(
			List<QbListsModel> lstInvcCustomerGridInvrtySite) {
		this.lstInvcCustomerGridInvrtySite = lstInvcCustomerGridInvrtySite;
	}




	public String getMsgToBeDispalyedOnInvoice() {
		return msgToBeDispalyedOnInvoice;
	}


	public void setMsgToBeDispalyedOnInvoice(String msgToBeDispalyedOnInvoice) {
		this.msgToBeDispalyedOnInvoice = msgToBeDispalyedOnInvoice;
	}


	public MenuData getMenuData() {
		return menuData;
	}


	public void setMenuData(MenuData menuData) {
		this.menuData = menuData;
	}

	@Command
	@NotifyChange({"lblExpenses","lblCheckItems","objCash","itemReceiptKey","creationdate","labelStatus","selectedAccount","selectedPaytoOrder","lectedInvcCutomerSendVia","toatlAmount","lstCheckItems","lstExpenses","totalAmount","tempTotalAmount","actionTYpe"})
	public void printInvoice(@BindingParam("row") ItemReceiptReportModel row)
	{
		try
		{
			int itemReceiptKey=row.getMastRecoNO();
			objCash=data.getItemReceiptByID(itemReceiptKey,webUserID,seeTrasction);
			if(objCash!=null && objCash.getRecNo()>0)
			{
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

			}
			createPdfForPrinting();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in QuotationHbaReport ----> printInvoice", ex);			
		}
	}

	@SuppressWarnings("unused")
	@Command
	public void createPdfForPrinting()
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
				
				table.setSpacingBefore(10);
				table.setWidthPercentage(100);
				//table.setWidths(new int[] { 75, 195, 60, 60, 60 });
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
			if(!objPrint.isHideWordAmount())
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


			cell1 = new PdfPCell(new Phrase("___________________\n\n  Received By \n  Date:    /    /   "+printYear, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
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


	public Date getCreationdate() {
		return creationdate;
	}


	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}


	public ItemReceiptGroupModel getReportGroupModel() {
		return reportGroupModel;
	}

	public void setReportGroupModel(ItemReceiptGroupModel reportGroupModel) {
		this.reportGroupModel = reportGroupModel;
	}

	public boolean isOpenGroup() {
		return isOpenGroup;
	}

	public void setOpenGroup(boolean isOpenGroup) {
		this.isOpenGroup = isOpenGroup;
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



	public DataFilter getFilter() {
		return filter;
	}



	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}



	public List<ItemReceiptReportModel> getInvoiceSalesReport() {
		return invoiceSalesReport;
	}



	public void setInvoiceSalesReport(
			List<ItemReceiptReportModel> invoiceSalesReport) {
		this.invoiceSalesReport = invoiceSalesReport;
	}



	public List<ItemReceiptReportModel> getAllInvoiceSalesReport() {
		return allInvoiceSalesReport;
	}



	public void setAllInvoiceSalesReport(
			List<ItemReceiptReportModel> allInvoiceSalesReport) {
		this.allInvoiceSalesReport = allInvoiceSalesReport;
	}



	public Date getFromDate() {
		return fromDate;
	}



	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}



	public Date getToDate() {
		return toDate;
	}



	public void setToDate(Date toDate) {
		this.toDate = toDate;
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



	public PrintModel getObjPrint() {
		return objPrint;
	}



	public void setObjPrint(PrintModel objPrint) {
		this.objPrint = objPrint;
	}


}
