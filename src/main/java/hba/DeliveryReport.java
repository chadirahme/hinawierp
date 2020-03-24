package hba;


import home.QuotationAttachmentModel;
import hr.HRData;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuData;
import layout.MenuModel;
import model.CashInvoiceGridData;
import model.CompSetupModel;
import model.DataFilter;
import model.DeliveryLineModel;
import model.DeliveryModel;
import model.HRListValuesModel;
import model.PrintModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

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
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import setup.users.WebusersModel;
import common.NumberToWord;
import company.CompanyData;

public class DeliveryReport {
	private Logger logger = Logger.getLogger(this.getClass());
	private HBAData data=new HBAData();
	private Date creationdate; 
	private DeliveryModel objDelivery;
	private List<DeliveryModel> deliveryModelReport;
	private DataFilter filter=new DataFilter();
	private List<DeliveryModel> allDeliveryModelReport;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");
	DecimalFormat formatter = new DecimalFormat("#,###.00");
	private CashInvoiceGridData selectedGridItems;
	private List<CashInvoiceGridData> lstCashInvoiceCheckItems;
	private List <QbListsModel> lstCashInvoiceGridItem;
	private List <QbListsModel> lstInvcCustomerGridInvrtySite;
	private List <QbListsModel> lstInvcCustomerGridClass;
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
	private boolean posItems;
	private String printYear="";
	private PrintModel objPrint;
	
	public DeliveryReport(){
		try{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
			objPrint=new PrintModel();
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
			compSetup=data.getDefaultSetUpInfoForCashInvoice();
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction
			Calendar c = Calendar.getInstance();	
			creationdate=df.parse(sdf.format(c.getTime()));
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			//use as default for huge data
			posItems=true;
			printYear=new SimpleDateFormat("yyyy").format(c.getTime());
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> init", ex);			
		}
	}


	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==315)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	@Command
	@NotifyChange({"deliveryModelReport"})
	public void searchCommand()
	{
		try
		{
			deliveryModelReport=data.getDeliveryReport(webUserID,seeTrasction,fromDate,toDate);
			allDeliveryModelReport=deliveryModelReport;
						
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> searchCommand", ex);			
		}
	}
	
	
	@Command
	@NotifyChange({"deliveryModelReport"})
	public void changeFilter() 
	{
		try
		{
			deliveryModelReport=filterData();
		}
		catch (Exception ex) 
		{
			logger.error("error in DeliveryReport---changeFilter-->" , ex);
		}

	}
	
	private List<DeliveryModel> filterData()
	{
		deliveryModelReport=allDeliveryModelReport;
		List<DeliveryModel>  lst=new ArrayList<DeliveryModel>();
		if(deliveryModelReport!=null && deliveryModelReport.size()>0)
		{
			for (Iterator<DeliveryModel> i = deliveryModelReport.iterator(); i.hasNext();)
			{
				DeliveryModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
					
						&&tmp.getUserName().toLowerCase().contains(filter.getUserName().toLowerCase())
					
						&&tmp.getDeliveryDateStr().toLowerCase().contains(filter.getDeliveryDate().toLowerCase())
					  )
				{
					lst.add(tmp);
				}
			}
		}
		return lst;

	}
	
	@Command
	public void resetDeliveryReport()
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
			logger.error("ERROR in DeliveryReport ----> resetDeliveryReport", ex);			
		}
	}
	@Command
	public void exportToExcel()
	{
		try
		{
			if(deliveryModelReport==null)
			{
				Messagebox.show("There is no record !!","Delivery Report", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final ExcelExporter exporter = new ExcelExporter();
			String[] tsHeaders;			
			tsHeaders = new String[]{"Delivery No.","Delivery Date", "Customer Name"};
			final String[] headers=tsHeaders;
			
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
				public void afterRendering(XSSFWorkbook target) {
					
				}
			});

			exporter.export(headers.length, deliveryModelReport, new RowRenderer<Row, DeliveryModel>() {
				@SuppressWarnings("static-access")
				@Override
				public void render(Row table, DeliveryModel item, boolean isOddRow) 
				{
					ExportContext context = exporter.getExportContext();
					XSSFSheet sheet = context.getSheet();	
					sheet.autoSizeColumn(5);
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRefNumber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDeliveryDateStr());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomerName());					
				}

			}, out);

			AMedia amedia = new AMedia("DeliveryReport.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> exportToExcel", ex);			
		}
	}
	
	@Command
	public void createDelivery()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("deliveryKey",0);
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("posItems", posItems);
			Executions.createComponents("/hba/payments/editDelivery.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> createDelivery", ex);			
		}
	}
	
	@GlobalCommand 
	@NotifyChange({"deliveryModelReport"})
	public void refreshDeliveryListParent()
	{
		logger.info("refreshDeliveryListParent >> ");
		searchCommand();
	}
	
	
	@Command
	public void voidDelivery()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			Executions.createComponents("/hba/payments/voidDelivery.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> createDelivery", ex);			
		}
	}
	
	@Command
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NotifyChange({"deliveryModelReport"})
	public void voidDeliveryCommand(@BindingParam("row") final DeliveryModel row)
	{
		try
		{
			if(row.getTransformQ().equals("Y"))
			{
				Messagebox.show("You can't void this item. Its link to Quotation !!","Void Delivery",Messagebox.OK , Messagebox.INFORMATION);				
				return;
			}
			
			Messagebox.show("Are You Sure to void this item ?","Delivery", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt)
						throws InterruptedException {
					if (evt.getName().equals("onYes")) 
					{
						int result=0;							
						result=data.updateDeliveryStatus(row.getRecNo()+"");
						
						if(result>0){
							Clients.showNotification("The Delivery Has Been Voided Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);							

						}
						else
						{
							Clients.showNotification("Error, The Delivery is Not Voided",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						}
						searchCommand();
						BindUtils.postNotifyChange(null,null,DeliveryReport.this,"deliveryModelReport");
					}
				}
			});
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> voidDeliveryCommand", ex);			
		}
	}
	
	
	@Command
	public void editDelivery(@BindingParam("row") DeliveryModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("deliveryKey",row.getRecNo());
			arg.put("type", "edit");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("posItems", posItems);
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editDelivery.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> editDelivery", ex);			
		}
	}
	
	@Command
	public void viewDelivery(@BindingParam("row") DeliveryModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("deliveryKey",row.getRecNo());
			arg.put("type", "view");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("posItems", posItems);
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editDelivery.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> editDelivery", ex);			
		}
	}
	
	@Command
	public void CashInvoiceCommand(@BindingParam("row") DeliveryModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("cashInvoiceKey",0);
			arg.put("RecNo",row.getRecNo());
			arg.put("changeToCashInvoice",true);
			arg.put("customerKey",row.getCustomerRefKey());
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editCashInvoice.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> CashInvoiceCommand", ex);			
		}
	}
	@Command
	public void CreditInvoiceCommand(@BindingParam("row") DeliveryModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("creditInvoiceKey",0);
			arg.put("RecNo",row.getRecNo());
			arg.put("changeToCreditInvoice",true);
			arg.put("customerKey",row.getCustomerRefKey());
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			arg.put("objPrint", objPrint);
			Executions.createComponents("/hba/payments/editCreditInvoice.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> CashInvoiceCommand", ex);			
		}
	}
	
	
	@SuppressWarnings("unused")
	@Command
	public void printDelivery(@BindingParam("row") DeliveryModel row)
	{
		try
		{
			List<DeliveryLineModel> deliveryModels =data.getDDeliveryGridDataByID(row.getRecNo());
			List<DeliveryLineModel> lstDeliveryCheckItems=new ArrayList<DeliveryLineModel>();
			for(DeliveryLineModel editDeliveryGrid: deliveryModels)
			{
				DeliveryLineModel obj=new DeliveryLineModel();
				obj.setLineNo(lstDeliveryCheckItems.size()+1);			
				obj.setQuotationLineNo(editDeliveryGrid.getQuotationLineNo());
				obj.setQuantity(editDeliveryGrid.getQuantity());
				obj.setServiceDate(editDeliveryGrid.getServiceDate());
				obj.setRate(editDeliveryGrid.getRate());
				obj.setQuantityInvoice(editDeliveryGrid.getQuantityInvoice());
				obj.setAmount(editDeliveryGrid.getAmount());
				obj.setDescription(editDeliveryGrid.getDescription());
				obj.setAvgCost(editDeliveryGrid.getAvgCost());
				obj.setOverrideItemAccountRefKey(0);
				obj.setIsTaxable("Y");
				obj.setOther1("0");
				obj.setOther2("0");
				obj.setSalesTaxCodeRefKey(0);
				lstDeliveryCheckItems.add(obj);
			}
			
			Document document = new Document(PageSize.A4, 40, 40, 108, 40);
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
			firsttbl.setWidths(new int[]{200,100});
			Font f = new Font(FontFamily.TIMES_ROMAN, 22.0f, Font.BOLD, BaseColor.RED);
			Chunk c = new Chunk("Delivery");
			c.setUnderline(0.1f, -2f);
			c.setFont(f);
			Paragraph p = new Paragraph(c);



			firsttbl.addCell(p);


			PdfPCell cell1 = new PdfPCell(new Phrase("Date : "+sdf.format(creationdate)+"\n\n"+"Delivery No. : "+row.getRefNumber()));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.setColspan(3);
			cell1.disableBorderSide(Rectangle.BOX);


			firsttbl.addCell(cell1);

			document.add(firsttbl);
			
			
			PdfPTable tbl1 = new PdfPTable(1);
			tbl1.setWidthPercentage(100);

			cell1 = new PdfPCell(new Phrase("To ,"));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);

			cell1 = new PdfPCell(new Phrase("M/S : "+row.getCustomerName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			tbl1.addCell(cell1);

			//add arfont here to use for arabic and RUN_DIRECTION_RTL and change the align from left to right 	
			String shipAddress="";
			shipAddress=row.getShipAddress1()+"\n"+row.getShipAddress2()+"\n"+row.getShipAddress3()+"\n"+row.getShipAddress4()+"\n"+row.getShipAddress5();
			cell1 = new PdfPCell(new Phrase(shipAddress ,arfont));
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

			PdfPTable table = new PdfPTable(2);
			objPrint.setQuantityWidth(0);
			if(!objPrint.isHideQuantity())
				objPrint.setQuantityWidth(40);
			
			table.setWidths(new int[]{390,objPrint.getQuantityWidth()});
			table.setSpacingBefore(20);
			table.setWidthPercentage(100);
			table.getDefaultCell().setPadding(2);
			BaseColor myColor = WebColors.getRGBColor("#8ECDFA");
			
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
			
			for (DeliveryLineModel item : lstDeliveryCheckItems) 
			{

				//table.addCell(new Phrase(""+item.getSelectedItems().getRecNo(),FontFactory.getFont(FontFactory.HELVETICA, 11)));
				PdfPCell cell=new PdfPCell(new Phrase(item.getDescription(),arfont));
				cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cell);

				if(item.getQuantity()!=0)
				{
					cell=new PdfPCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					//table.addCell(new Phrase(""+item.getQuantity(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
					table.addCell(cell);
				}
				else{
					table.addCell(new Phrase(" ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
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
			
			document.add( Chunk.NEWLINE );
			document.add( Chunk.NEWLINE );
			paragraph=new Paragraph();
			Chunk chunk = new Chunk("");
			paragraph.add(chunk);
			paragraph.setAlignment(Element.ALIGN_LEFT);
			document.add(paragraph);
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
			/*amtStr1 = BigDecimal.valueOf(totalAmount).toPlainString();
			amtDbbl1 = Double.parseDouble(amtStr1);*/
			cell1 = new PdfPCell(new Phrase("___________________\n\n  Customer Approval \n  Date:    /    /   "+printYear, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
			cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell1.disableBorderSide(Rectangle.BOX);
			cell1.setBorder(0);
			endPage.addCell(cell1);
			document.add(endPage);
			document.close();
			
			if(!createPdfSendEmail){
				previewPdfForprintingInvoice();
			}
			
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DeliveryReport ----> printDelivery", ex);			
		}
	}
	
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
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(getCityName(compSetup.getCitykey())+" - "+getCountryName(compSetup.getCountrykey())),rect.getLeft(), rect.getTop()-45, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("______________________________________________________________________________"),rect.getLeft(), rect.getTop()-50, 0);
				Calendar now = Calendar.getInstance();
				//ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(String.format("This Document Does Not Require Signature")),rect.getLeft(), rect.getBottom()-15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(String.format("Date :"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(now.getTime()))),(rect.getRight()), rect.getBottom() - 30, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_RIGHT, new Phrase(String.format("Printed by :"+selectedUser.getFirstname())),(rect.getRight()), rect.getBottom() - 15, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(String.format("Powered by www.hinawi.com")),rect.getLeft(), rect.getBottom()-30, 0);


			} catch (BadElementException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf ----> onEndPage", e);
			} catch (MalformedURLException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf----> onEndPage", e);
			} catch (IOException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf----> onEndPage", e);
			} catch (DocumentException e) {
				logger.error("ERROR in EditDelivery class HeaderFooter PDf----> onEndPage", e);
			}
		}
	}	
	private void previewPdfForprintingInvoice() {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			// arg.put("pdfContent", file);
			Executions.createComponents("/hba/payments/invoicePdfView.zul",
					null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in EditDelivery ----> previewPdfForprintingInvoice",
					ex);
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
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public DeliveryModel getObjCashInvoice() {
		return objDelivery;
	}
	public void setObjCashInvoice(DeliveryModel objCashInvoice) {
		this.objDelivery = objCashInvoice;
	}
	public List<DeliveryModel> getDeliveryModelReport() {
		return deliveryModelReport;
	}
	public void setDeliveryModelReport(List<DeliveryModel> deliveryModelReport) {
		this.deliveryModelReport = deliveryModelReport;
	}
	public DataFilter getFilter() {
		return filter;
	}
	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
	public List<DeliveryModel> getAllInvoiceSalesReport() {
		return allDeliveryModelReport;
	}
	public void setAllInvoiceSalesReport(List<DeliveryModel> allInvoiceSalesReport) {
		this.allDeliveryModelReport = allInvoiceSalesReport;
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
	public double getToatlAmount() {
		return toatlAmount;
	}
	public void setToatlAmount(double toatlAmount) {
		this.toatlAmount = toatlAmount;
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
	public CompSetupModel getCompSetup() {
		return compSetup;
	}
	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	public MenuData getMenuData() {
		return menuData;
	}
	public void setMenuData(MenuData menuData) {
		this.menuData = menuData;
	}
	public List<MenuModel> getList() {
		return list;
	}
	public void setList(List<MenuModel> list) {
		this.list = list;
	}
	public boolean isAdminUser() {
		return adminUser;
	}
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
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
	public WebusersModel getSelectedUserTemp() {
		return selectedUserTemp;
	}
	public void setSelectedUserTemp(WebusersModel selectedUserTemp) {
		this.selectedUserTemp = selectedUserTemp;
	}
	public CompanyData getCompanyData() {
		return companyData;
	}
	public void setCompanyData(CompanyData companyData) {
		this.companyData = companyData;
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


	public DeliveryModel getObjDelivery() {
		return objDelivery;
	}


	public void setObjDelivery(DeliveryModel objDelivery) {
		this.objDelivery = objDelivery;
	}


	public List<DeliveryModel> getAllDeliveryModelReport() {
		return allDeliveryModelReport;
	}


	public void setAllDeliveryModelReport(List<DeliveryModel> allDeliveryModelReport) {
		this.allDeliveryModelReport = allDeliveryModelReport;
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
