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
import model.BanksModel;
import model.ClassModel;
import model.CompSetupModel;
import model.DataFilter;
import model.HRListValuesModel;
import model.QbListsModel;
import model.ReceiptVoucherDeatiledModel;
import model.ReceiptVoucherGridData;
import model.ReceiptVoucherMastModel;
import model.RecieptVoucherReportModel;
import model.VendorModel;

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
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
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

public class ReceiptVoucherReportViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	ReceiptVoucherData data=new ReceiptVoucherData();
	ReceiptVoucherData dataRV=new ReceiptVoucherData();
	HBAData hbadata=new HBAData();
	private List<RecieptVoucherReportModel> invoiceSalesReport;
	private DataFilter filter=new DataFilter();
	private List<RecieptVoucherReportModel> allInvoiceSalesReport;

	CompanyData companyData=new CompanyData();

	private ReceiptVoucherViewModel objReceiptVoucher;
	private ReceiptVoucherMastModel receiptVoucherMastModel;
	private ReceiptVoucherDeatiledModel receiptVoucherDeatiledModel;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");

	DecimalFormat formatter = new DecimalFormat("#,###.00");

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

	private boolean isOpenGroup;

	private String lblTotalCost;

	private double toatlAmount;

	private CompSetupModel compSetup;

	private String receiptVoucherNo;

	private Date rvDate;

	private int  reciptVoiucherKey;

	private boolean makeAsDeferedIncome;

	private boolean makeAsDeferedIncomeVisible;

	String actionTYpe;

	private String labelStatus="";


	//private ReceiptVoucherReportGroupModel reportGroupModel;


	private int webUserID=0;

	private MenuModel companyRole;

	MenuData menuData=new MenuData();

	List<MenuModel> list;

	private boolean adminUser;

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

	private String msgToBeDispalyedOnInvoice="";

	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;

	NumberToWord numbToWord=new NumberToWord();//get amount in words 

	private WebusersModel selectedUserTemp;
	private List<QuotationAttachmentModel> lstAtt = new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets = new QuotationAttachmentModel();
	private boolean createPdfSendEmail = false;
	private List<HRListValuesModel> countries;
	private List<HRListValuesModel> cities;
	private HRData hrData = new HRData();
	
	private boolean seeTrasction=false;


	public ReceiptVoucherReportViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Calendar c = Calendar.getInstance();	
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
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
			lstUsers=new ListModelList<WebusersModel>(companyData.getUsersList(dbUser.getCompanyid()));
			lstReceivedFrom=dataRV.getRiceivedFrom();
			lstAccountCr=dataRV.getAccountCr();
			lstDepositeTo=dataRV.getGridPaymentMethodCash();
			lstBankModel=dataRV.getGridBankName();
			lstsClassModel=dataRV.getGridClass();
			lstcuc=dataRV.getAcccountForCUG();
			compSetup=hbadata.GetDefaultSetupInfo();

			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			seeTrasction=companyRole.isCanAllowToSeeAccountingTrasaction();//allow to see trasaction

			fillData();


		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> init", ex);			
		}
	}


	public void fillData()
	{
		postToQbBy=new ArrayList<String>();
		postToQbBy.add("Receipt Voucher");
		postToQbBy.add("Journal Voucher");
		selectedPostToQbBy=postToQbBy.get(0);

		lstPyamentMethod=new ArrayList<String>();
		lstPyamentMethod.add("Cash");
		lstPyamentMethod.add("Cheque");
	}

	private List<RecieptVoucherReportModel> filterData()
	{
		invoiceSalesReport=allInvoiceSalesReport;
		List<RecieptVoucherReportModel>  lst=new ArrayList<RecieptVoucherReportModel>();
		if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
		{
			for (Iterator<RecieptVoucherReportModel> i = invoiceSalesReport.iterator(); i.hasNext();)
			{
				RecieptVoucherReportModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						&&tmp.getRvNumber().toLowerCase().contains(filter.getRvNumber().toLowerCase())&&
						tmp.getRvDate().toLowerCase().contains(filter.getRvDate().toLowerCase())&&
						//tmp.getBulidingName().toLowerCase().contains(filter.getBulidingName().toLowerCase())&&
						//tmp.getUnitNumber().toLowerCase().contains(filter.getUnitNumber().toLowerCase())&&
						tmp.getReceiptName().toLowerCase().contains(filter.getReceiptName().toLowerCase())&&
						tmp.getPostQbBy().toLowerCase().contains(filter.getPostQbBy().toLowerCase())&&
						tmp.getUserName().toLowerCase().contains(filter.getUserName().toLowerCase())&&
						tmp.getArAccountName().toLowerCase().contains(filter.getArAccountName().toLowerCase())
						&&(tmp.getPaymenentMethod()).toLowerCase().contains(filter.getPaymenentMethod().toLowerCase())&&
						(tmp.getBankName()).toLowerCase().contains(filter.getBankName().toLowerCase())&&
						(tmp.getCheckNUmber()).toLowerCase().contains(filter.getCheckNUmber().toLowerCase())&&
						(tmp.getCheckDate()).toLowerCase().contains(filter.getCheckDate().toLowerCase())&&
						(tmp.getDepositeToAccountName()).toLowerCase().contains(filter.getDepositeToAccountName().toLowerCase())&&
						(tmp.getCucAccountName()).toLowerCase().contains(filter.getCucAccountName().toLowerCase())&&
						(tmp.getStatus()).toLowerCase().contains(filter.getStatus().toLowerCase())&&
						(tmp.getAmmount()+"").toLowerCase().contains(filter.getAmount().toLowerCase())
						)
				{
					lst.add(tmp);
				}
				/*reportGroupModel=new ReceiptVoucherReportGroupModel(lst, new ReceiptVoucherComparator(), true);
				for (int j = 0; j < reportGroupModel.getGroupCount(); j++)
				{
					reportGroupModel.removeOpenGroup(j);
				}*/


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
			calcAmonut();
			//totalNoOfInvoice=reportGroupModel.getGroupCount();

		}
		catch (Exception ex) {
			logger.error("error in ReceiptVoucherReportViewModel---changeFilter-->" , ex);
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
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> resetCashInvoiceReport", ex);			
		}
	}

	public RecieptVoucherReportModel getPrevious(RecieptVoucherReportModel uid) {
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
		for(RecieptVoucherReportModel report:invoiceSalesReport)
		{
			RecieptVoucherReportModel report1=getPrevious(report);
			if(report1!=null)
			{
				if(!report.getCustomerName().equalsIgnoreCase(report1.getCustomerName()))
				{
					incremnetor=incremnetor+1;
				}

			}
			if(report.getPaymenentMethod().replaceAll("\\s+","").equalsIgnoreCase("Cash"))
			{
				totalAmount=totalAmount+report.getAmmount();
			}
			else
			{
				totalSale=totalSale+report.getAmmount();
			}

			selectedUserTemp=new WebusersModel();
			for(WebusersModel model:lstUsers)
			{
				if(model.getUserid()==report.getWebUserId())
				{
					selectedUserTemp=model;
					break;
				}
			}

			if(report.getWebUserId()>0 && selectedUserTemp!=null)
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


	}

	@Command
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void searchCommand()
	{
		try
		{
			invoiceSalesReport= data.getReceiptVoucherReportValues(fromDate,toDate,webUserID,seeTrasction);
			allInvoiceSalesReport=invoiceSalesReport;
			calcAmonut();
			/*reportGroupModel=new ReceiptVoucherReportGroupModel(invoiceSalesReport, new ReceiptVoucherComparator(), true);
			for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			{
				reportGroupModel.removeOpenGroup(i);
			}
			totalNoOfInvoice=reportGroupModel.getGroupCount();*/
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> searchCommand", ex);			
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
		/*for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
		{
			if(isOpenGroup==false)
				reportGroupModel.removeOpenGroup(i);
			else
				reportGroupModel.addOpenGroup(i);
		} */
	}


	@GlobalCommand 
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void refreshRecipetVoucher(@BindingParam("type")String type)
	{		
		try
		{
			invoiceSalesReport= data.getReceiptVoucherReportValues(fromDate,toDate,webUserID,seeTrasction);
			allInvoiceSalesReport=invoiceSalesReport;
			calcAmonut();
			/*reportGroupModel=new ReceiptVoucherReportGroupModel(invoiceSalesReport, new ReceiptVoucherComparator(), true);
			for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			{
				reportGroupModel.removeOpenGroup(i);
			}
			totalNoOfInvoice=reportGroupModel.getGroupCount();*/

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> refreshRecipetVoucher", ex);			
		}
	}


	@Command
	public void createReceiptVoucher()
	{
		try
		{
			String[] str=new String[45];
			str[0]="0";
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("reciptVoiucherKey",0);
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			Executions.createComponents("/hba/payments/editReciptVoucher.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> createReceiptVoucher", ex);			
		}
	}


	//edit vendor list
	@Command
	public void editReciptVoucher(@BindingParam("row") RecieptVoucherReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("reciptVoiucherKey", row.getRecNO());
			arg.put("type", "edit");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			Executions.createComponents("/hba/payments/editReciptVoucher.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> editReciptVoucher", ex);			
		}
	}


	@Command
	public void viewReciptVoucher(@BindingParam("row") RecieptVoucherReportModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("reciptVoiucherKey",row.getRvNumber());
			arg.put("type", "view");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			Executions.createComponents("/hba/payments/editReciptVoucher.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> viewReciptVoucher", ex);			
		}
	}


	/* @Command
		public void exportCommand(@BindingParam("ref") Grid grid) throws Exception 
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
			final String title="Cash/ Cheque Receipt Voucher  Report From" +new SimpleDateFormat("dd-MM-yyyy").format(fromDate) + " To " + new SimpleDateFormat("dd-MM-yyyy").format(toDate)+"";
			String[] tsHeaders;
			tsHeaders = new String[]{"Rv No.","RV Date","Name","Print On Receipt","Ar. Account","Payment Method","Bank Name","Cheque Number","Cheque Date","Building Name","Unit NO","Deposite To","CUC Account","Amount","Status"};
			final String[] headers=tsHeaders;
			String[] tsFooters;
			tsFooters = new String[]{"Total No. Of RV's :"+totalNoOfInvoice+"","Total Customers :"+totalNoOfCustomer+"","Total Cash Amount :"+formatter.format(totalAmount)+"","Total Cheque Amount :"+formatter.format(totalSale)+""};
			final float[] columnWidths = new float[] {100f, 100f, 100f,100f, 100f, 100f, 100f,100f,100f, 100f,100f, 100f, 100f, 100f,100f};
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

					 for (int i = 0; i < footers.length; i++) {
							String footer = footers[i];
							Font font2 = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
							PdfPCell cell2 = exporter.getPdfPCellFactory().getFooterCell();
							cell2.setPhrase(new Phrase(footer, font2));
							table.addCell(cell2);
						}
						table.completeRow();
				}
			});

			 exporter.export(headers.length, invoiceSalesReport, new RowRenderer<PdfPTable, RecieptVoucherReportModel>() {
			 @Override
			 public void render(PdfPTable table, RecieptVoucherReportModel item, boolean isOddRow) 
			 {
				 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
				 PdfPCell cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getRvNumber(), font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getRvDate()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getCustomerName()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getReceiptName()+"", font));
				 table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getArAccountName()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getPaymenentMethod()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getBankName()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getCheckNUmber()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getCheckDate()+"", font));
				table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getBulidingName()+"", font));
				 table.addCell(cell);

				 cell = cellFactory.getCell(isOddRow);			
				 cell.setPhrase(new Phrase(item.getUnitNumber()+"", font));
				 table.addCell(cell);


				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getDepositeToAccountName()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getCustomerName()+"", font));
				table.addCell(cell);


				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getAmmount()+"", font));
				table.addCell(cell);

				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getStatus()+"", font));
				table.addCell(cell);

				try {
					table.setWidths(columnWidths);
				} catch (DocumentException e) {
					e.printStackTrace();
				}

				table.completeRow();
			 }


			 }, out);

			AMedia amedia = new AMedia("ReceiptVoucher.pdf", "pdf", "application/pdf", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			}

			 catch (Exception ex)
				{	
				logger.error("ERROR in ReceiptVoucherReportViewModel ----> exportCommand", ex);			
				}
		}*/
	@SuppressWarnings("unused")
	@Command
	public void exportToExcel(@BindingParam("ref") Grid grid)
	{
		try
		{
			if(invoiceSalesReport==null)
			{
				Messagebox.show("There is are record !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			final ExcelExporter exporter = new ExcelExporter();
			String[] tsHeaders;
			//tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","NO.of Days","Present Days","Off Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
			tsHeaders = new String[]{"Rv No.","RV Date","Name","Print On Receipt","Ar. Account","Payment Method","Bank Name","Cheque Number","Cheque Date","Building Name","Unit NO","Deposite To","CUC Account","Amount","Status"};
			final String[] headers=tsHeaders;
			String[] tsFooters;
			tsFooters = new String[]{"Total No. Of RV's :"+totalNoOfInvoice+"","Total Customers :"+totalNoOfCustomer+"","Total Cash Amount :"+formatter.format(totalAmount)+"","Total Cheque Amount :"+formatter.format(totalSale)+""};
			final float[] columnWidths = new float[] {100f, 100f, 100f,100f, 100f, 100f, 100f,100f,100f, 100f,100f, 100f, 100f, 100f,100f};
			final String[] footers=tsFooters;

			exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {

				@SuppressWarnings("static-access")
				@Override
				public void beforeRendering(XSSFWorkbook target) {
					ExportContext context = exporter.getExportContext();

					for (String header : headers) {
						Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
						cell.setCellValue(header);

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
					cs.setWrapText(true);   

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

			exporter.export(headers.length, invoiceSalesReport, new RowRenderer<Row, RecieptVoucherReportModel>() {
				@SuppressWarnings("static-access")
				@Override
				public void render(Row table, RecieptVoucherReportModel item, boolean isOddRow) 
				{
					ExportContext context = exporter.getExportContext();
					XSSFSheet sheet = context.getSheet();				        
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRvNumber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getRvDate());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCustomerName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getReceiptName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getArAccountName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPaymenentMethod());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBankName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCheckNUmber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCheckDate());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getBulidingName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getUnitNumber());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDepositeToAccountName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCucAccountName());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAmmount());
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getStatus());
				}

			}, out);

			AMedia amedia = new AMedia("ReceiptVoucher.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> exportToExcel", ex);			
		}
	}


	@Command
	@NotifyChange({"invoiceNewSaleNo","creationdate","labelStatus","invoiceNewBillToAddress","lstCashInvoiceCheckItems","objCashInvoice","selectedInvcCutomerName","lstInvcCustomerClass","selectedCreditInvoiceTerms","selectedInvcCutomerSalsRep","selectedCreditinvAcount","selectedInvcCutomerSendVia","toatlAmount","balance","amountPiad","toatlAmount","tempTotalAmount","exChnage"})
	public void printInvoice(@BindingParam("row") RecieptVoucherReportModel row)
	{
		try
		{
			
			reciptVoiucherKey=Integer.parseInt(row.getRvNumber());
			receiptVoucherMastModel=dataRV.getReceiptVoucherById(row.getRecNO(),webUserID,seeTrasction);
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
						break;
					}

				}
				receiptVoucherNo=receiptVoucherMastModel.getRefNumber();
				rvDate=receiptVoucherMastModel.getTxtDate();
				printOnReciptVoucher=receiptVoucherMastModel.getReceiptName();
				memo=receiptVoucherMastModel.getMemo();
				for(String postToQbBYNew:postToQbBy)
				{
					if(postToQbBYNew.equalsIgnoreCase(receiptVoucherMastModel.getRvOrJv()))
					{
						selectedPostToQbBy=postToQbBy.get(0);
						break;
					}
					else
					{
						selectedPostToQbBy=postToQbBy.get(1);
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

			amountInWords=numbToWord.GetFigToWord(toatlAmount);
			createPdfForPrinting();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> printInvoice", ex);			
		}
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

	@SuppressWarnings("unused")
	@Command
	public void createPdfForPrinting()
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


			cell1 = new PdfPCell(new Phrase("___________________\n\n  Received By \n  Date:    /    /   2016", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
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
				String path = hbadata.getImageData(dbUser.getCompanyName());
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
						"ERROR in ReceiptVoucherReportViewModel class HeaderFooter PDf ----> onEndPage",
						e);
			} catch (MalformedURLException e) {
				logger.error(
						"ERROR in ReceiptVoucherReportViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (IOException e) {
				logger.error(
						"ERROR in ReceiptVoucherReportViewModel class HeaderFooter PDf----> onEndPage",
						e);
			} catch (DocumentException e) {
				logger.error(
						"ERROR in ReceiptVoucherReportViewModel class HeaderFooter PDf----> onEndPage",
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
			logger.error("ERROR in ReceiptVoucherReportViewModel ----> previewPdfForprintingInvoice", ex);			
		}
	}


	public List<RecieptVoucherReportModel> getInvoiceSalesReport() {
		return invoiceSalesReport;
	}


	public void setInvoiceSalesReport(
			List<RecieptVoucherReportModel> invoiceSalesReport) {
		this.invoiceSalesReport = invoiceSalesReport;
	}


	public List<RecieptVoucherReportModel> getAllInvoiceSalesReport() {
		return allInvoiceSalesReport;
	}


	public void setAllInvoiceSalesReport(
			List<RecieptVoucherReportModel> allInvoiceSalesReport) {
		this.allInvoiceSalesReport = allInvoiceSalesReport;
	}

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= hbadata.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{			
			if(item.getMenuid()==290)
			{
				companyRole=item;
				break;
			}
		}
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


	/*public ReceiptVoucherReportGroupModel getReportGroupModel() {
		return reportGroupModel;
	}


	public void setReportGroupModel(ReceiptVoucherReportGroupModel reportGroupModel) {
		this.reportGroupModel = reportGroupModel;
	}*/


	public boolean isOpenGroup() {
		return isOpenGroup;
	}


	public void setOpenGroup(boolean isOpenGroup) {
		this.isOpenGroup = isOpenGroup;
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


	public boolean isAdminUser() {
		return adminUser;
	}


	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
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


	public void setSelectedReceivedFrom(QbListsModel selectedReceivedFrom) {
		this.selectedReceivedFrom = selectedReceivedFrom;
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


	public void setSelectedPostToQbBy(String selectedPostToQbBy) {
		this.selectedPostToQbBy = selectedPostToQbBy;
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


	public List<ReceiptVoucherGridData> getLstRecieptVoucherGrid() {
		return lstRecieptVoucherGrid;
	}


	public void setLstRecieptVoucherGrid(
			List<ReceiptVoucherGridData> lstRecieptVoucherGrid) {
		this.lstRecieptVoucherGrid = lstRecieptVoucherGrid;
	}


	public ReceiptVoucherGridData getSelectedReceiptValucherGrid() {
		return selectedReceiptValucherGrid;
	}


	public void setSelectedReceiptValucherGrid(
			ReceiptVoucherGridData selectedReceiptValucherGrid) {
		this.selectedReceiptValucherGrid = selectedReceiptValucherGrid;
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


	public List<AccountsModel> getLstcuc() {
		return lstcuc;
	}


	public void setLstcuc(List<AccountsModel> lstcuc) {
		this.lstcuc = lstcuc;
	}


	public String getMsgToBeDispalyedOnInvoice() {
		return msgToBeDispalyedOnInvoice;
	}


	public void setMsgToBeDispalyedOnInvoice(String msgToBeDispalyedOnInvoice) {
		this.msgToBeDispalyedOnInvoice = msgToBeDispalyedOnInvoice;
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


	public String getLblTotalCost() {
		return lblTotalCost;
	}


	public void setLblTotalCost(String lblTotalCost) {
		this.lblTotalCost = lblTotalCost;
	}


	public double getToatlAmount() {
		return toatlAmount;
	}


	public void setToatlAmount(double toatlAmount) {
		this.toatlAmount = toatlAmount;
	}


	public CompSetupModel getCompSetup() {
		return compSetup;
	}


	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
	}


	public String getReceiptVoucherNo() {
		return receiptVoucherNo;
	}


	public void setReceiptVoucherNo(String receiptVoucherNo) {
		this.receiptVoucherNo = receiptVoucherNo;
	}


	public Date getRvDate() {
		return rvDate;
	}


	public void setRvDate(Date rvDate) {
		this.rvDate = rvDate;
	}


	public int getReciptVoiucherKey() {
		return reciptVoiucherKey;
	}


	public void setReciptVoiucherKey(int reciptVoiucherKey) {
		this.reciptVoiucherKey = reciptVoiucherKey;
	}


	public boolean isMakeAsDeferedIncome() {
		return makeAsDeferedIncome;
	}


	public void setMakeAsDeferedIncome(boolean makeAsDeferedIncome) {
		this.makeAsDeferedIncome = makeAsDeferedIncome;
	}


	public boolean isMakeAsDeferedIncomeVisible() {
		return makeAsDeferedIncomeVisible;
	}


	public void setMakeAsDeferedIncomeVisible(boolean makeAsDeferedIncomeVisible) {
		this.makeAsDeferedIncomeVisible = makeAsDeferedIncomeVisible;
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


	public NumberToWord getNumbToWord() {
		return numbToWord;
	}


	public void setNumbToWord(NumberToWord numbToWord) {
		this.numbToWord = numbToWord;
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
	public void CustomerSendEmail() {

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
								"ERROR in ReceiptVoucherReportViewModel ----> CustomerSendEmail",
								ex);
					}
				}
			}
		});

	}

}



