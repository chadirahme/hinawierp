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
import model.ClassModel;
import model.CompSetupModel;
import model.DataFilter;
import model.HRListValuesModel;
import model.PrintModel;
import model.PurchaseRequestGridData;
import model.PurchaseRequestModel;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
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

public class PurchaseRequestReportViewModel {

	private Logger logger = Logger.getLogger(this.getClass());

	private DataFilter filter=new DataFilter();

	private Date creationdate; 


	private List<PurchaseRequestReportModel> invoiceSalesReport;

	private List<PurchaseRequestReportModel> allInvoiceSalesReport;


	private List<ClassModel> lstClass;
	private ClassModel selectedLstClass;
	private List<QbListsModel> lstPayToOrder;
	private QbListsModel selectedPaytoOrder;
	private List<QbListsModel> lstDropShipTo;
	private QbListsModel selectedDropShipTo;
	private int  purchaseRequestKey;
	private PurchaseRequestModel objCash;

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

	private ItemReceiptGroupModel reportGroupModel;

	private boolean isOpenGroup;



	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	HBAData data=new HBAData();

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

	private Date fromDate;

	private Date toDate;

	private List<String> fromType=new ArrayList<String>();

	private String selelctedFromType=new String();

	private List<String> satusType=new ArrayList<String>();

	private String selelctedSatusType=new String();

	private String webOrOnline="";

	private String querrystatus="C";

	private String refNUmber="";

	private String memo="";
	private String webUserName="";
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();
	NumberToWord numbToWord=new NumberToWord();
	
	private boolean seeTrasction=false;
	
	private List<PurchaseRequestReportModel> lstPR;

	private boolean posItems;
	private String printYear="";
	private PrintModel objPrint;
	
	public PurchaseRequestReportViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			objPrint=new PrintModel();
			Calendar c = Calendar.getInstance();
			printYear=new SimpleDateFormat("yyyy").format(c.getTime());
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			lstClass=data.fillClassList("");
			lstPayToOrder=data.fillQbList("'Vendor'");
			lstDropShipTo=data.getDropShipTo();
			objCash=new PurchaseRequestModel();
			compSetup=data.GetDefaultSetupInfo();
			lstGridCustomer=data.fillQbList("'Customer'");
			lstGridClass=data.fillClassList("");
			lstGridQBItems=data.getItemForPurchaseRequest();
			countries = hrData.getHRListValues(2, "");
			cities = hrData.getHRListValues(3, "");
			fillData();
			//setSelelctedFromType(selelctedFromType);
			//setSelelctedSatusType(selelctedSatusType);
			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
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

			//use as default for huge data
			posItems=true;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> init", ex);			
		}
	}

	public void fillData()
	{
		fromType=new ArrayList<String>();
		fromType.add("From Online");
		fromType.add("From Desktop");
		fromType.add("Both");
		selelctedFromType=fromType.get(0);

		satusType=new ArrayList<String>();
		satusType.add("Created");
		satusType.add("Approved");
		satusType.add("Printed");
		satusType.add("All");
		/*satusType.add("Void");*/
		selelctedSatusType=satusType.get(0);
	}


	@Command
	@NotifyChange({"reportGroupModel","isOpenGroup"})
	public void colseGroup()
	{
		if(invoiceSalesReport==null)
		{   		   
			return;
		}

	}

	private List<PurchaseRequestReportModel> filterData()
	{
		invoiceSalesReport=allInvoiceSalesReport;
		List<PurchaseRequestReportModel>  lst=new ArrayList<PurchaseRequestReportModel>();
		if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
		{
			for (Iterator<PurchaseRequestReportModel> i = invoiceSalesReport.iterator(); i.hasNext();)
			{
				PurchaseRequestReportModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						&&tmp.getVendorName().toLowerCase().contains(filter.getVendorName().toLowerCase())&&
						tmp.getDropToName().toLowerCase().contains(filter.getDropToName().toLowerCase())&&
						tmp.getTxtnDate().toLowerCase().contains(filter.getTxtnDate().toLowerCase())&&
						tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						&&(tmp.getStatus()).toLowerCase().contains(filter.getStatus().toLowerCase())&&
						(tmp.getMemo()).toLowerCase().contains(filter.getMemo().toLowerCase())&&
						(tmp.getItemName()).toLowerCase().contains(filter.getItemName().toLowerCase())&&
						(tmp.getDecription()).toLowerCase().contains(filter.getDecription().toLowerCase())&&
						(tmp.getQuantity()+"").toLowerCase().contains(filter.getQuantity().toLowerCase())&&
						(tmp.getRecivedQuantity()+"").toLowerCase().contains(filter.getRecivedQuantity().toLowerCase())&&
						(tmp.getRate()+"").toLowerCase().contains(filter.getRate().toLowerCase())&&
						tmp.getRefNUmber().toLowerCase().contains(filter.getRefNUmber().toLowerCase())&&
						(tmp.getAmount()+"").toLowerCase().contains(filter.getAmount().toLowerCase())
						)
				{
					lst.add(tmp);
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
			logger.error("error in PurchaseRequestReportViewModel---changeFilter-->" , ex);
		}

	}

	@Command
	public void resetPurchaseRequestReport()
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
			logger.error("ERROR in PurchaseRequestReportViewModel ----> resetPurchaseRequestReport", ex);			
		}
	}

	public PurchaseRequestReportModel getPrevious(PurchaseRequestReportModel uid) {
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
			invoiceSalesReport=data.getPurchaseRequestReport(webUserID,seeTrasction,fromDate, toDate, webOrOnline,querrystatus);
			allInvoiceSalesReport=invoiceSalesReport;

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> searchCommand", ex);			
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
				Messagebox.show("There is are record !!","Purchase Request Report", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			final ExcelExporter exporter = new ExcelExporter();
			String[] tsHeaders;
			//tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","NO.of Days","Present Days","Off Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
			tsHeaders = new String[]{"Vendor Name","Drop Ship To", "Ref. No","Date","Class Name","Customer Name","Memo","Item Name","Description","Qty","Recived Qty","Rate","Amount","Status"};
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

			exporter.export(headers.length, invoiceSalesReport, new RowRenderer<Row, PurchaseRequestReportModel>() {
				@SuppressWarnings("static-access")
				@Override
				public void render(Row table, PurchaseRequestReportModel item, boolean isOddRow) 
				{
					ExportContext context = exporter.getExportContext();
					XSSFSheet sheet = context.getSheet();	
					sheet.autoSizeColumn(5);
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getVendorName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDropToName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRefNUmber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTxtnDate());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getClassName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomerName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getMemo());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getItemName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDecription());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getQuantity());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRecivedQuantity());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRate());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAmount());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getStatus());
				}

			}, out);

			AMedia amedia = new AMedia("purchaseRequest.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> exportToExcel", ex);			
		}
	}



	@GlobalCommand 
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void refreshParentPurchaseRequest(@BindingParam("type")String type)
	{		
		try
		{
			invoiceSalesReport=data.getPurchaseRequestReport(webUserID,seeTrasction,fromDate, toDate, webOrOnline,querrystatus);
			allInvoiceSalesReport=invoiceSalesReport;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> refreshParentPurchaseRequest", ex);			
		}
	}


	@Command
	public void createPurchaseRequest()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("purchaseRequestKey",0);
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("posItems", posItems);
			Executions.createComponents("/hba/payments/editPurchaseRequest.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> createPurchaseRequest", ex);			
		}
	}




	//edit vendor list
	@Command
	public void editPurchaseRequest(@BindingParam("row") PurchaseRequestReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("purchaseRequestKey", row.getRecNo());
			arg.put("type", "edit");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("posItems", posItems);
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editPurchaseRequest.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> editPurchaseRequest", ex);			
		}
	}


	@Command
	public void viewPurchaseRequest(@BindingParam("row") PurchaseRequestReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("purchaseRequestKey", row.getRecNo());
			arg.put("type", "view");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("posItems", posItems);
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editPurchaseRequest.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> viewPurchaseRequest", ex);			
		}
	}


	public Logger getLogger() {
		return logger;
	}



	public void setLogger(Logger logger) {
		this.logger = logger;
	}



	public PurchaseRequestModel getObjCash() {
		return objCash;
	}



	public void setObjCash(PurchaseRequestModel objCash) {
		this.objCash = objCash;
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
			if(item.getMenuid()==311)
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
	@NotifyChange({"lblExpenses","lblCheckItems","refNUmber","objCash","memo","selectedDropShipTo","selectedLstClass","itemReceiptKey","creationdate","labelStatus","selectedAccount","selectedPaytoOrder","lectedInvcCutomerSendVia","toatlAmount","lstCheckItems","lstExpenses","totalAmount","tempTotalAmount","actionTYpe"})
	public void printInvoice(@BindingParam("row") PurchaseRequestReportModel row)
	{
		try
		{
			int purchaseRequestKey=row.getRecNo();
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
			createPdfForPrinting();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> printInvoice", ex);			
		}
	}

	@SuppressWarnings("unused")
	@Command
	public void createPdfForPrinting()
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
			logger.error("ERROR in PurchaseRequestViewModel ----> createPdfForPrinting", ex);
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
			logger.error("ERROR in PurchaseRequestReportViewModel ----> previewPdfForprintingInvoice", ex);			
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
						"ERROR in PurchaseRequestViewModel class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in PurchaseRequestViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in PurchaseRequestViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in PurchaseRequestViewModel class HeaderFooter PDf----> onEndPage",
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



	public List<PurchaseRequestReportModel> getInvoiceSalesReport() {
		return invoiceSalesReport;
	}



	public void setInvoiceSalesReport(
			List<PurchaseRequestReportModel> invoiceSalesReport) {
		this.invoiceSalesReport = invoiceSalesReport;
	}



	public List<PurchaseRequestReportModel> getAllInvoiceSalesReport() {
		return allInvoiceSalesReport;
	}



	public void setAllInvoiceSalesReport(
			List<PurchaseRequestReportModel> allInvoiceSalesReport) {
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

	public List<String> getFromType() {
		return fromType;
	}

	public void setFromType(List<String> fromType) {
		this.fromType = fromType;
	}

	public List<String> getSatusType() {
		return satusType;
	}

	public void setSatusType(List<String> satusType) {
		this.satusType = satusType;
	}

	public String getSelelctedFromType() {
		return selelctedFromType;
	}

	@NotifyChange({"invoiceSalesReport","allInvoiceSalesReport"})
	public void setSelelctedFromType(String selelctedFromType) {
		this.selelctedFromType = selelctedFromType;
		if(selelctedFromType!=null)
		{
			if(selelctedFromType.equalsIgnoreCase("From Desktop"))
			{
				webOrOnline="CMS";
			}
			else if(selelctedFromType.equalsIgnoreCase("From Online"))
			{
				webOrOnline="ONL";
			}
			else if(selelctedFromType.equalsIgnoreCase("Both"))
			{
				webOrOnline="";
			}
			invoiceSalesReport=data.getPurchaseRequestReport(webUserID,seeTrasction,fromDate, toDate, webOrOnline,querrystatus);
			allInvoiceSalesReport=invoiceSalesReport;
		}

	}

	public String getSelelctedSatusType() {
		return selelctedSatusType;
	}

	@NotifyChange({"invoiceSalesReport","allInvoiceSalesReport"})
	public void setSelelctedSatusType(String selelctedSatusType) {
		this.selelctedSatusType = selelctedSatusType;
		if(selelctedSatusType!=null)
		{
			if(selelctedSatusType.equalsIgnoreCase("Created"))
			{
				querrystatus="C";
			}
			else if(selelctedSatusType.equalsIgnoreCase("Approved"))
			{
				querrystatus="A";
			}
			else if(selelctedSatusType.equalsIgnoreCase("Printed"))
			{
				querrystatus="P";
			}
			else if(selelctedSatusType.equalsIgnoreCase("All"))
			{
				querrystatus="";
			}
			invoiceSalesReport=data.getPurchaseRequestReport(webUserID,seeTrasction,fromDate, toDate, webOrOnline,querrystatus);
			allInvoiceSalesReport=invoiceSalesReport;
		}
	}

	public String getWebOrOnline() {
		return webOrOnline;
	}

	public void setWebOrOnline(String webOrOnline) {
		this.webOrOnline = webOrOnline;
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

	public List<QbListsModel> getLstDropShipTo() {
		return lstDropShipTo;
	}

	public void setLstDropShipTo(List<QbListsModel> lstDropShipTo) {
		this.lstDropShipTo = lstDropShipTo;
	}

	public QbListsModel getSelectedDropShipTo() {
		return selectedDropShipTo;
	}

	public void setSelectedDropShipTo(QbListsModel selectedDropShipTo) {
		this.selectedDropShipTo = selectedDropShipTo;
	}

	public int getPurchaseRequestKey() {
		return purchaseRequestKey;
	}

	public void setPurchaseRequestKey(int purchaseRequestKey) {
		this.purchaseRequestKey = purchaseRequestKey;
	}

	public String getQuerrystatus() {
		return querrystatus;
	}

	public void setQuerrystatus(String querrystatus) {
		this.querrystatus = querrystatus;
	}

	public String getRefNUmber() {
		return refNUmber;
	}

	public void setRefNUmber(String refNUmber) {
		this.refNUmber = refNUmber;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({ "createPdfSendEmail","objCash","selectedPaytoOrder" })
	public void CustomerSendEmail(@BindingParam("row") final PurchaseRequestReportModel row) {
		objCash=data.getPurchaseRequestByID(purchaseRequestKey,webUserID,seeTrasction);
		for(QbListsModel vendorList:lstPayToOrder)
		{
			if(vendorList.getRecNo()==objCash.getVendorRefKEy())
			{
				selectedPaytoOrder=vendorList;
				break;
			}

		}
		lstAtt = new ArrayList<QuotationAttachmentModel>();
		selectedAttchemnets.setFilename(selectedPaytoOrder.getName()+ " Purchase Request.pdf");
		selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
		lstAtt.add(selectedAttchemnets);
		Messagebox.show("Do you want to Preview The Purchase Request?",	"Purchase Request", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
			public void onEvent(Event evt)
					throws InterruptedException {
				if (evt.getName().equals("onYes")) {
					createPdfSendEmail = false;
					printInvoice(row);
				}
				if (evt.getName().equals("onNo")) {
					try {
						createPdfSendEmail = true;
						printInvoice(row);
						createPdfSendEmail = false;
						Map<String, Object> arg = new HashMap<String, Object>();
						arg.put("id", objCash.getVendorRefKEy());
						arg.put("lstAtt", lstAtt);
						arg.put("feedBackKey", 0);
						arg.put("formType", "Customer");
						arg.put("type", "OtherForms");
						Executions.createComponents("/crm/editCustomerFeedbackSend.zul",null, arg);
					} catch (Exception ex) {
						logger.error("ERROR in CashInvoiceSalesReport ----> CustomerSendEmail",ex);
					}
				}
			}
		});

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


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"allInvoiceSalesReport","invoiceSalesReport"})
	public void approveRequests(@BindingParam("cmp") final Window x) {
		try {
			Messagebox.show("Are You Sure?","Purchase Request", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt)
						throws InterruptedException {
					if (evt.getName().equals("onYes")) {
						int result=0;	
						Iterator iter = null;
						PurchaseRequestReportModel obj=new PurchaseRequestReportModel();
						StringBuffer buffer = new StringBuffer();
						if(lstPR.size()>0){
							iter = lstPR.iterator();
						}else{
							Clients.showNotification("Please, Select Purchase Request To Approve",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
							return;
						}
						if(iter!=null){
							while (iter.hasNext()) {
								obj=(PurchaseRequestReportModel) iter.next();
								buffer.append(obj.getRecNo());
								if (iter.hasNext()) {
									buffer.append(",");
								}
							}
						}
						if(!buffer.toString().equalsIgnoreCase(""))
						{
							result=data.updatePurchaseRequestStatus(buffer.toString());
						}

						if(result>0){
							Clients.showNotification("The Purchase Request Has Been Approved Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
							x.detach();

						}else{
							Clients.showNotification("Error, The Purchase Request is Not Approved",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						}
					}
				}
			});
		} 
		catch (Exception ex) {
			logger.error("ERROR in PurchaseRequestReportViewModel ----> approveRequests",ex);
		}

	}
	
	@Command
	@NotifyChange({"invoiceSalesReport"})
	public void approveMaterialCommand(@BindingParam("row") PurchaseRequestReportModel row)
	{
		try
		{
			int result=0;	
			result=data.updatePurchaseRequestStatus(row.getRecNo()+"");
			if(result>0)
			{
				Clients.showNotification("The Purchase Request Has Been Approved Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);				
			}
			else
			{
				Clients.showNotification("Error, The Purchase Request is Not Approved",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			
			searchCommand();
			
		}
		catch (Exception ex) 
		{
			logger.error("ERROR in PurchaseRequestReportViewModel ----> approveMaterialCommand",ex);
		}
	}
	
	@Command
	public void ChangeToPOCommand(@BindingParam("row") PurchaseRequestReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("purchaseRequestKey",0);
			arg.put("changeToPO",true);
			arg.put("RecNo",row.getRecNo());
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			Executions.createComponents("/hba/payments/editPurchaseOrder.zul", null,arg);
		}
		catch (Exception ex) 
		{
			logger.error("ERROR in PurchaseRequestReportViewModel ---->ChangeToPOCommand",ex);
		}
	}

	public NumberToWord getNumbToWord() {
		return numbToWord;
	}

	public void setNumbToWord(NumberToWord numbToWord) {
		this.numbToWord = numbToWord;
	}

	public List<PurchaseRequestReportModel> getLstPR() {
		return lstPR;
	}

	public void setLstPR(List<PurchaseRequestReportModel> lstPR) {
		this.lstPR = lstPR;
	}
	
	@Command
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void searchNotApproved()
	{
		try
		{
			invoiceSalesReport=new ArrayList<PurchaseRequestReportModel>();
			allInvoiceSalesReport = new ArrayList<PurchaseRequestReportModel>();
			invoiceSalesReport=data.getNotApprovedPurchaseRequestReport(webUserID,fromDate, toDate, webOrOnline);
			allInvoiceSalesReport=invoiceSalesReport;

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in PurchaseRequestReportViewModel ----> searchNotApproved", ex);			
		}
	}


/**********************************************************Work Flow************************************************************************/
	
	@Command
	public void approveMaterialsRequest() {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			Executions.createComponents("/hba/payments/approveMaterialsRequest.zul", null, arg);
		} catch (Exception ex) {
			logger.error("ERROR in PurchaseRequestViewModel ----> approveMaterialsRequest",
					ex);
		}
	}

public boolean isPosItems() {
	return posItems;
}

public void setPosItems(boolean posItems) {
	this.posItems = posItems;
}

public PrintModel getObjPrint() {
	return objPrint;
}

public void setObjPrint(PrintModel objPrint) {
	this.objPrint = objPrint;
}




}
