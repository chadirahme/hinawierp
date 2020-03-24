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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuData;
import layout.MenuModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.CashInvoiceSalesReportModel;
import model.CompSetupModel;
import model.DataFilter;
import model.HRListValuesModel;
import model.PrintModel;
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
import org.zkoss.zk.ui.event.Event;
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

public class CashInvoiceSalesReport {

	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();

	private Date creationdate; 
	private CashInvoiceModel objCashInvoice;
	private List<CashInvoiceSalesReportModel> invoiceSalesReport;
	private DataFilter filter=new DataFilter();
	private List<CashInvoiceSalesReportModel> allInvoiceSalesReport;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");

	DecimalFormat formatter = new DecimalFormat("#,###.00");

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

	private boolean showFieldsPOS=false;

	private boolean pointofSale;

	private double balance;

	private double amountPiad;
	private double exChnage;

	private double toatlAmount;

	private List<String> barcodeValues;

	private String msgToBeDispalyedOnInvoice="";

	private int totalNoOfCustomer;

	private double totalAmount;

	private String period;

	private String totalAmountStr;

	private Date fromDate;

	private Date toDate;

	private double totalSale;

	private String totalSaleStr;

	private int totalNoOfInvoice;

	private double maxInvoiceAmount;

	private double minInvoiceAmount;

	private int webUserID=0;

	private CompSetupModel compSetup;

	private MenuModel companyRole;

	MenuData menuData=new MenuData();

	List<MenuModel> list;

	private boolean adminUser;

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	private WebusersModel selectedUserTemp;

	CompanyData companyData=new CompanyData();
	private boolean createPdfSendEmail = false;
	NumberToWord numbToWord=new NumberToWord();
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	
	private boolean seeTrasction=false;
	private String printYear="";
	private PrintModel objPrint;
	private boolean posItems;

	public CashInvoiceSalesReport()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			objPrint=new PrintModel();
			fillBarCode();
			lstInvcCustomerName=data.fillQbList("'Customer'");
			lstCashInvoiceGridItem=data.fillQbItemsList();
			lstInvcCustomerClass=data.GetMasterData("Class");
			lstInvcCustomerDepositTo=data.GetMasterData("DepositeTo");
			lstInvcCustomerSalsRep=data.GetMasterData("SalesRep");
			compSetup=data.getDefaultSetUpInfoForCashInvoice();
			
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

			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
			for(WebusersModel model:lstUsers)
			{
				if(model.getUserid()==dbUser.getUserid())
				{
					selectedUser=model;
					break;
				}
			}
			lstInvcCustomerTemplate=data.GetMasterData("Template");
			lstInvcCustomerSendVia= data.GetMasterData("SendVia");
			lstInvcCustomerGridInvrtySite=(data.GetMasterData("GridSite"));
			lstInvcCustomerGridClass=data.GetMasterData("GridClass");
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

			//list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			//seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction
			
						
			Calendar c = Calendar.getInstance();	
			printYear=new SimpleDateFormat("yyyy").format(c.getTime());
			creationdate=df.parse(sdf.format(c.getTime()));
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			//use as default for huge data
			posItems=true;

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> init", ex);			
		}
	}

	public void fillBarCode()
	{

		barcodeValues=data.getLstBarcodes(null);
	}

	private List<CashInvoiceSalesReportModel> filterData()
	{
		invoiceSalesReport=allInvoiceSalesReport;
		List<CashInvoiceSalesReportModel>  lst=new ArrayList<CashInvoiceSalesReportModel>();
		if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
		{
			for (Iterator<CashInvoiceSalesReportModel> i = invoiceSalesReport.iterator(); i.hasNext();)
			{
				CashInvoiceSalesReportModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						&&tmp.getInvoiceNumber().toLowerCase().contains(filter.getInvoiceNumber().toLowerCase())&&
						tmp.getSalesRep().toLowerCase().contains(filter.getSalesRep().toLowerCase())&&
						tmp.getUserName().toLowerCase().contains(filter.getUserName().toLowerCase())&&
						tmp.getPaymentType().toLowerCase().contains(filter.getPaymentType().toLowerCase())&&
						tmp.getDepositeTo().toLowerCase().contains(filter.getDepositeTo().toLowerCase())&&
						tmp.getInvoiceDateStr().toLowerCase().contains(filter.getInvoiceDate().toLowerCase())&&
						tmp.getCheckNo().toLowerCase().contains(filter.getCheckNo().toLowerCase())
						&&(tmp.getTotalSales()+"").toLowerCase().contains(filter.getTotalSales().toLowerCase())&&
						(tmp.getInvoiceAmount()+"").toLowerCase().contains(filter.getInvoiceAmount().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
		}
		return lst;

	}

	@Command
	@NotifyChange({"invoiceSalesReport","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void changeFilter() 
	{
		try
		{
			invoiceSalesReport=filterData();
			calcAmonut();

		}
		catch (Exception ex) {
			logger.error("error in CashInvoiceSalesReport---changeFilter-->" , ex);
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
			logger.error("ERROR in CashInvoiceSalesReport ----> resetCashInvoiceReport", ex);			
		}
	}

	public CashInvoiceSalesReportModel getPrevious(CashInvoiceSalesReportModel uid) {
		int idx = invoiceSalesReport.indexOf(uid);
		if (idx <= 0) return null;
		return invoiceSalesReport.get(idx - 1);
	}
	public void calcAmonut()
	{
		totalAmount=0;
		totalNoOfCustomer=0;
		totalSale=0;
		int incremnetor=0;
		for(CashInvoiceSalesReportModel report:invoiceSalesReport)
		{
			totalAmount=totalAmount+report.getInvoiceAmount();
			totalSale=totalSale+report.getTotalSales();
			CashInvoiceSalesReportModel report1=getPrevious(report);
			maxInvoiceAmount=report.getInvoiceAmount();
			if(report1!=null)
			{
				if(!report.getCustomerName().equalsIgnoreCase(report1.getCustomerName()))
				{
					incremnetor=incremnetor+1;
				}

			}
			selectedUserTemp=new WebusersModel();
			for(WebusersModel model:lstUsers)
			{
				if(model.getUserid()==report.getWebuserId())
				{
					selectedUserTemp=model;
					break;
				}
			}

			if(report.getWebuserId()>0 && selectedUserTemp!=null)
			{

				report.setUserName(selectedUserTemp.getFirstname());

			}
			else
			{
				report.setUserName("Admin");
			}

		}
		totalNoOfCustomer=incremnetor;
		totalAmountStr=formatter.format(totalAmount);
		totalSaleStr=formatter.format(totalSale);
		totalNoOfInvoice=invoiceSalesReport.size();
		if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
		{
			CashInvoiceSalesReportModel invoiceSalesReportModel=new CashInvoiceSalesReportModel();
			CashInvoiceSalesReportModel invoiceSalesReportModelMin=new CashInvoiceSalesReportModel();
			invoiceSalesReportModel = Collections.max(invoiceSalesReport);
			invoiceSalesReportModelMin = Collections.min(invoiceSalesReport);
			maxInvoiceAmount=invoiceSalesReportModel.getInvoiceAmount();
			minInvoiceAmount=invoiceSalesReportModelMin.getInvoiceAmount();
		}

	}

	@Command
	@NotifyChange({"invoiceSalesReport","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void searchCommand()
	{
		try
		{
			invoiceSalesReport= data.getCashInvoiceSalesReport(fromDate,toDate,webUserID,seeTrasction);
			calcAmonut();
			allInvoiceSalesReport=invoiceSalesReport;

			if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
			{
				CashInvoiceSalesReportModel invoiceSalesReportModel=new CashInvoiceSalesReportModel();
				CashInvoiceSalesReportModel invoiceSalesReportModelMin=new CashInvoiceSalesReportModel();
				invoiceSalesReportModel = Collections.max(invoiceSalesReport);
				maxInvoiceAmount=invoiceSalesReportModel.getInvoiceAmount();
				invoiceSalesReportModelMin = Collections.min(invoiceSalesReport);
				minInvoiceAmount=invoiceSalesReportModelMin.getInvoiceAmount();
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> searchCommand", ex);			
		}
	}


	/* @Command
		public void exportCommand(@BindingParam("ref") Listbox grid) throws Exception 
		{			
			try
			{

				if(invoiceSalesReport==null)
				  {
		  		Messagebox.show("There are no record !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
		  		return;
				  }
		    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			final PdfExporter exporter = new PdfExporter();
			final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
			final FontFactory fontFactory = exporter.getFontFactory();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final String title="Cash Invoice Sales Report From" +new SimpleDateFormat("dd-MM-yyyy").format(fromDate) + " To " + new SimpleDateFormat("dd-MM-yyyy").format(toDate)+"";
			String[] tsHeaders;
			tsHeaders = new String[]{"Invoice No.","Invoice Date", "Customer Name","Deposite To","Check No.","Invoice Amount","Total Sales","Payment Type","Sales Rep Name"};
			final String[] headers=tsHeaders;
			String[] tsFooters;
			tsFooters = new String[]{"Total No. Of Invoices :"+totalNoOfInvoice+"","Total Customers :"+totalNoOfCustomer+"","Total Invoice Amount :"+formatter.format(totalAmount)+"","Total Sales :"+formatter.format(totalSale)+""};
			final float[] columnWidths = new float[] {100f, 100f, 100f, 100f,100f, 100f, 100f, 100f,100f};
			final String[] footers=tsFooters;
			exporter.setInterceptor(new Interceptor <PdfPTable> () {
				@Override
				public void beforeRendering(PdfPTable table) {
					table.setWidthPercentage(100);
					PdfPCell cellTitle = exporter.getPdfPCellFactory().getHeaderCell();
					Font fontTitle = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
					cellTitle.setPhrase(new Phrase(title, fontTitle));
					cellTitle.setColspan(9);
					cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cellTitle);
					table.completeRow();

					for (int i = 0; i < headers.length; i++) {
						String header = headers[i];
						Font font = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);

						PdfPCell cell = exporter.getPdfPCellFactory().getHeaderCell();
						cell.setPhrase(new Phrase(header, font));
						if ("Units".equals(header) || "Total".equals(header)) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}

						table.addCell(cell);
					}
					try {
						table.setWidths(columnWidths);
					} catch (DocumentException e) {
						e.printStackTrace();
					}
					table.completeRow();
				}

				@Override
				public void afterRendering(PdfPTable table) {
					int headerwidths[] = {10,30, 30,30,20,20,24,24,24};
					try {
						table.setWidths(headerwidths);
					} catch (DocumentException e) {
						e.printStackTrace();
					}
					 for (int i = 0; i < footers.length; i++) {
							String footer = footers[i];
							Font font2 = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
							PdfPCell cell2 = exporter.getPdfPCellFactory().getFooterCell();
							cell2.setPhrase(new Phrase(footer, font2));
							cell2.setColspan(3);
							if(cell2.isContent())
							{
							table.addCell(cell2);
							}
						}
						table.completeRow();
				}
			});

			 exporter.export(headers.length, invoiceSalesReport, new RowRenderer<PdfPTable, CashInvoiceSalesReportModel>() {
			 @Override
			 public void render(PdfPTable table, CashInvoiceSalesReportModel item, boolean isOddRow) 
			 {
				 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
				 PdfPCell cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getInvoiceNumber(), font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getInvoiceDateStr()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getCustomerName()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getDepositeTo()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getCheckNo()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getInvoiceAmount()+"", font));
				 table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getTotalSales()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getPaymentType()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getSalesRep()+"", font));
				table.addCell(cell);

				try {
					table.setWidths(columnWidths);
				} catch (DocumentException e) {
					e.printStackTrace();
				}

				table.completeRow();
			 }


			 }, out);

			AMedia amedia = new AMedia("CashInvoiceSales.pdf", "pdf", "application/pdf", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			}

			 catch (Exception ex)
				{	
				logger.error("ERROR in CashInvoiceSalesReport ----> exportCommand", ex);			
				}
		}*/
	@SuppressWarnings("unused")
	@Command
	public void exportToExcel(@BindingParam("ref") Listbox grid)
	{
		try
		{
			// 	ByteArrayOutputStream out = new ByteArrayOutputStream();

			//	ExcelExporter exporter = new ExcelExporter();
			//	exporter.export(grid, out);

			/*AMedia amedia = new AMedia("TimesheetReport.xls", "xls", "application/file", out.toByteArray());
					Filedownload.save(amedia);
					out.close();*/

			if(invoiceSalesReport==null)
			{
				Messagebox.show("There is are record !!","Cash Invoice Sale Report", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			final ExcelExporter exporter = new ExcelExporter();
			String[] tsHeaders;
			//tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","NO.of Days","Present Days","Off Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
			tsHeaders = new String[]{"Invoice No.","Invoice Date", "Customer Name","Deposite To","Check No.","Invoice Amount","Total Sales","Payment Type","Sales Rep Name"};
			final String[] headers=tsHeaders;
			String[] tsFooters;

			tsFooters = new String[]{"Total No. Of Invoices :"+totalNoOfInvoice+"","Total Customers :"+totalNoOfCustomer+"","Total Invoice Amount :"+formatter.format(totalAmount)+""};
			final String[] footers=tsFooters;

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

				@SuppressWarnings("static-access")
				@Override
				public void afterRendering(XSSFWorkbook target) {
					ExportContext context = exporter.getExportContext();
					CellStyle cs = target.createCellStyle();
					cs.setWrapText(true);   //Wrapping text
					for (String foot : footers) {
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

					}
				}				    				   
			});

			exporter.export(headers.length, invoiceSalesReport, new RowRenderer<Row, CashInvoiceSalesReportModel>() {
				@SuppressWarnings("static-access")
				@Override
				public void render(Row table, CashInvoiceSalesReportModel item, boolean isOddRow) 
				{
					ExportContext context = exporter.getExportContext();
					XSSFSheet sheet = context.getSheet();	
					sheet.autoSizeColumn(5);
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceNumber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceDateStr());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomerName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDepositeTo());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCheckNo());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceAmount());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getInvoiceAmount());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPaymentType());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getSalesRep());
				}

			}, out);

			AMedia amedia = new AMedia("CashInvoiceSales.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> exportToExcel", ex);			
		}
	}



	@GlobalCommand 
	@NotifyChange({"invoiceSalesReport","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void refreshParentCashInvoice(@BindingParam("type")String type)
	{		
		try
		{
			invoiceSalesReport= data.getCashInvoiceSalesReport(fromDate,toDate,webUserID,seeTrasction);
			calcAmonut();
			allInvoiceSalesReport=invoiceSalesReport;

			if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
			{
				CashInvoiceSalesReportModel invoiceSalesReportModel=new CashInvoiceSalesReportModel();
				CashInvoiceSalesReportModel invoiceSalesReportModelMin=new CashInvoiceSalesReportModel();
				invoiceSalesReportModel = Collections.max(invoiceSalesReport);
				maxInvoiceAmount=invoiceSalesReportModel.getInvoiceAmount();
				invoiceSalesReportModelMin = Collections.min(invoiceSalesReport);
				minInvoiceAmount=invoiceSalesReportModelMin.getInvoiceAmount();
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> refreshParentCashInvoice", ex);			
		}
	}


	@Command
	public void createCashInvoice()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("cashInvoiceKey",0);
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			arg.put("posItems", posItems);
			Executions.createComponents("/hba/payments/editCashInvoice.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> editCashInvoice", ex);			
		}
	}




	//edit vendor list
	@Command
	public void editCashInvoice(@BindingParam("row") CashInvoiceSalesReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("cashInvoiceKey", row.getRecNO());
			arg.put("type", "edit");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editCashInvoice.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> editCashInvoice", ex);			
		}
	}


	@Command
	public void viewCashInvoice(@BindingParam("row") CashInvoiceSalesReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("cashInvoiceKey", row.getRecNO());
			arg.put("type", "view");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editCashInvoice.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> editCashInvoice", ex);			
		}
	}







	public List<CashInvoiceSalesReportModel> getInvoiceSalesReport() {
		return invoiceSalesReport;
	}


	public void setInvoiceSalesReport(
			List<CashInvoiceSalesReportModel> invoiceSalesReport) {
		this.invoiceSalesReport = invoiceSalesReport;
	}


	public List<CashInvoiceSalesReportModel> getAllInvoiceSalesReport() {
		return allInvoiceSalesReport;
	}


	public void setAllInvoiceSalesReport(
			List<CashInvoiceSalesReportModel> allInvoiceSalesReport) {
		this.allInvoiceSalesReport = allInvoiceSalesReport;
	}


	public HBAData getData() {
		return data;
	}


	public void setData(HBAData data) {
		this.data = data;
	}


	public DataFilter getFilter() {
		return filter;
	}


	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}


	public int getTotalNoOfCustomer() {
		return totalNoOfCustomer;
	}


	public void setTotalNoOfCustomer(int totalNoOfCustomer) {
		this.totalNoOfCustomer = totalNoOfCustomer;
	}


	public double getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}


	public String getPeriod() {
		return period;
	}


	public void setPeriod(String period) {
		this.period = period;
	}


	public String getTotalAmountStr() {
		return totalAmountStr;
	}


	public void setTotalAmountStr(String totalAmountStr) {
		this.totalAmountStr = totalAmountStr;
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


	public double getTotalSale() {
		return totalSale;
	}


	public void setTotalSale(double totalSale) {
		this.totalSale = totalSale;
	}


	public String getTotalSaleStr() {
		return totalSaleStr;
	}


	public void setTotalSaleStr(String totalSaleStr) {
		this.totalSaleStr = totalSaleStr;
	}


	public int getTotalNoOfInvoice() {
		return totalNoOfInvoice;
	}


	public void setTotalNoOfInvoice(int totalNoOfInvoice) {
		this.totalNoOfInvoice = totalNoOfInvoice;
	}


	public double getMaxInvoiceAmount() {
		return maxInvoiceAmount;
	}


	public void setMaxInvoiceAmount(double maxInvoiceAmount) {
		this.maxInvoiceAmount = maxInvoiceAmount;
	}


	public double getMinInvoiceAmount() {
		return minInvoiceAmount;
	}


	public void setMinInvoiceAmount(double minInvoiceAmount) {
		this.minInvoiceAmount = minInvoiceAmount;
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
			if(item.getMenuid()==132)
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


	public CashInvoiceGridData getSelectedGridItems() {
		return selectedGridItems;
	}


	public void setSelectedGridItems(CashInvoiceGridData selectedGridItems) {
		this.selectedGridItems = selectedGridItems;
	}


	public List<CashInvoiceGridData> getLstCashInvoiceCheckItems() {
		return lstCashInvoiceCheckItems;
	}


	public void setLstCashInvoiceCheckItems(
			List<CashInvoiceGridData> lstCashInvoiceCheckItems) {
		this.lstCashInvoiceCheckItems = lstCashInvoiceCheckItems;
	}


	public List<QbListsModel> getLstCashInvoiceGridItem() {
		return lstCashInvoiceGridItem;
	}


	public void setLstCashInvoiceGridItem(List<QbListsModel> lstCashInvoiceGridItem) {
		this.lstCashInvoiceGridItem = lstCashInvoiceGridItem;
	}


	public List<QbListsModel> getLstInvcCustomerGridInvrtySite() {
		return lstInvcCustomerGridInvrtySite;
	}


	public void setLstInvcCustomerGridInvrtySite(
			List<QbListsModel> lstInvcCustomerGridInvrtySite) {
		this.lstInvcCustomerGridInvrtySite = lstInvcCustomerGridInvrtySite;
	}


	public List<QbListsModel> getLstInvcCustomerGridClass() {
		return lstInvcCustomerGridClass;
	}


	public void setLstInvcCustomerGridClass(
			List<QbListsModel> lstInvcCustomerGridClass) {
		this.lstInvcCustomerGridClass = lstInvcCustomerGridClass;
	}


	public List<QbListsModel> getLstInvcCustomerName() {
		return lstInvcCustomerName;
	}


	public void setLstInvcCustomerName(List<QbListsModel> lstInvcCustomerName) {
		this.lstInvcCustomerName = lstInvcCustomerName;
	}


	public QbListsModel getSelectedInvcCutomerName() {
		return selectedInvcCutomerName;
	}


	public void setSelectedInvcCutomerName(QbListsModel selectedInvcCutomerName) {
		this.selectedInvcCutomerName = selectedInvcCutomerName;
	}


	public List<QbListsModel> getLstInvcCustomerClass() {
		return lstInvcCustomerClass;
	}


	public void setLstInvcCustomerClass(List<QbListsModel> lstInvcCustomerClass) {
		this.lstInvcCustomerClass = lstInvcCustomerClass;
	}


	public QbListsModel getSelectedInvcCutomerClass() {
		return selectedInvcCutomerClass;
	}


	public void setSelectedInvcCutomerClass(QbListsModel selectedInvcCutomerClass) {
		this.selectedInvcCutomerClass = selectedInvcCutomerClass;
	}


	public List<QbListsModel> getLstInvcCustomerDepositTo() {
		return lstInvcCustomerDepositTo;
	}


	public void setLstInvcCustomerDepositTo(
			List<QbListsModel> lstInvcCustomerDepositTo) {
		this.lstInvcCustomerDepositTo = lstInvcCustomerDepositTo;
	}


	public QbListsModel getSelectedInvcCutomerDepositeTo() {
		return selectedInvcCutomerDepositeTo;
	}


	public void setSelectedInvcCutomerDepositeTo(
			QbListsModel selectedInvcCutomerDepositeTo) {
		this.selectedInvcCutomerDepositeTo = selectedInvcCutomerDepositeTo;
	}


	public List<QbListsModel> getLstInvcCustomerSalsRep() {
		return lstInvcCustomerSalsRep;
	}


	public void setLstInvcCustomerSalsRep(List<QbListsModel> lstInvcCustomerSalsRep) {
		this.lstInvcCustomerSalsRep = lstInvcCustomerSalsRep;
	}


	public QbListsModel getSelectedInvcCutomerSalsRep() {
		return selectedInvcCutomerSalsRep;
	}


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


	public QbListsModel getSelectedInvcCutomerPaymntMethd() {
		return selectedInvcCutomerPaymntMethd;
	}


	public void setSelectedInvcCutomerPaymntMethd(
			QbListsModel selectedInvcCutomerPaymntMethd) {
		this.selectedInvcCutomerPaymntMethd = selectedInvcCutomerPaymntMethd;
	}


	public List<QbListsModel> getLstInvcCustomerTemplate() {
		return lstInvcCustomerTemplate;
	}


	public void setLstInvcCustomerTemplate(
			List<QbListsModel> lstInvcCustomerTemplate) {
		this.lstInvcCustomerTemplate = lstInvcCustomerTemplate;
	}


	public QbListsModel getSelectedInvcCutomerTemplate() {
		return selectedInvcCutomerTemplate;
	}


	public void setSelectedInvcCutomerTemplate(
			QbListsModel selectedInvcCutomerTemplate) {
		this.selectedInvcCutomerTemplate = selectedInvcCutomerTemplate;
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


	public List<QbListsModel> getLstInvcCustomerSendVia() {
		return lstInvcCustomerSendVia;
	}


	public void setLstInvcCustomerSendVia(List<QbListsModel> lstInvcCustomerSendVia) {
		this.lstInvcCustomerSendVia = lstInvcCustomerSendVia;
	}


	public QbListsModel getSelectedInvcCutomerSendVia() {
		return selectedInvcCutomerSendVia;
	}


	public void setSelectedInvcCutomerSendVia(
			QbListsModel selectedInvcCutomerSendVia) {
		this.selectedInvcCutomerSendVia = selectedInvcCutomerSendVia;
	}


	public boolean isShowFieldsPOS() {
		return showFieldsPOS;
	}


	public void setShowFieldsPOS(boolean showFieldsPOS) {
		this.showFieldsPOS = showFieldsPOS;
	}


	public boolean isPointofSale() {
		return pointofSale;
	}


	public void setPointofSale(boolean pointofSale) {
		this.pointofSale = pointofSale;
	}


	public double getBalance() {
		return balance;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}


	public double getAmountPiad() {
		return amountPiad;
	}


	public void setAmountPiad(double amountPiad) {
		this.amountPiad = amountPiad;
	}


	public double getExChnage() {
		return exChnage;
	}


	public void setExChnage(double exChnage) {
		this.exChnage = exChnage;
	}


	public List<String> getBarcodeValues() {
		return barcodeValues;
	}


	public void setBarcodeValues(List<String> barcodeValues) {
		this.barcodeValues = barcodeValues;
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
	@NotifyChange({"invoiceNewSaleNo","creationdate","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedCreditInvoiceTerms","selectedInvcCutomerSalsRep","selectedCreditinvAcount","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void printInvoice(@BindingParam("row") CashInvoiceSalesReportModel row)
	{
		try
		{
			objCashInvoice=data.getCashInvoiceByID(row.getRecNO(),webUserID,seeTrasction);
			if(objCashInvoice!=null && objCashInvoice.getRecNo()>0)
			{
				List<CashInvoiceGridData> invoiceModelnew=data.getCashInvoiceGridDataByID(row.getRecNO());
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

					objCashInvoice.setCheckNo(objCashInvoice.getCheckNo());
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
				createPdfForPrinting();
			}	
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashInvoiceSalesReport ----> printInvoice", ex);			
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
				totaltbl.setWidths(new int[]{objPrint.getWordAmountWidth(),100});
				//totaltbl.setWidths(new int[]{350,100});
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
				//totaltbl.setWidths(new int[]{350,100});
				totaltbl.setWidths(new int[]{objPrint.getWordAmountWidth(),100});
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
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in CashInvoiceSalesReport class HeaderFooter PDf----> onEndPage",
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
			logger.error("ERROR in CashInvoiceSalesReport ----> previewPdfForprintingInvoice", ex);			
		}
	}




	public CashInvoiceModel getObjCashInvoice() {
		return objCashInvoice;
	}


	public void setObjCashInvoice(CashInvoiceModel objCashInvoice) {
		this.objCashInvoice = objCashInvoice;
	}


	public Date getCreationdate() {
		return creationdate;
	}


	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}


	public double getToatlAmount() {
		return toatlAmount;
	}


	public void setToatlAmount(double toatlAmount) {
		this.toatlAmount = toatlAmount;
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
	@NotifyChange({ "createPdfSendEmail","objCashInvoice","selectedInvcCutomerName" })
	public void CustomerSendEmail(@BindingParam("row") final CashInvoiceSalesReportModel row) {
		objCashInvoice=data.getCashInvoiceByID(row.getRecNO(),webUserID,seeTrasction);
		for(QbListsModel cutomerNmae:lstInvcCustomerName)
		{
			if(cutomerNmae.getRecNo()==objCashInvoice.getCustomerRefKey())
			{
				selectedInvcCutomerName=cutomerNmae;
				break;
			}

		}
		lstAtt = new ArrayList<QuotationAttachmentModel>();
		selectedAttchemnets.setFilename(selectedInvcCutomerName.getFullName()+ " Cash Invoice.pdf");
		selectedAttchemnets.setFilepath("C:/temp/invoicePDFWebApplication.pdf");
		lstAtt.add(selectedAttchemnets);
		Messagebox.show("Do you want to Preview The Cash Invoice?",	"Cash Invoice", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
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
						arg.put("id", objCashInvoice.getCustomerRefKey());
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

	public ListModelList<WebusersModel> getLstUsers() {
		return lstUsers;
	}

	public void setLstUsers(ListModelList<WebusersModel> lstUsers) {
		this.lstUsers = lstUsers;
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

	public PrintModel getObjPrint() {
		return objPrint;
	}

	public void setObjPrint(PrintModel objPrint) {
		this.objPrint = objPrint;
	}

	public boolean isPosItems() {
		return posItems;
	}

	public void setPosItems(boolean posItems) {
		this.posItems = posItems;
	}


}
