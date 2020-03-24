package hba;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.BalanceSheetReportModel;
import model.CutomerSummaryReport;
import model.DataFilter;
import model.TimeSheetDataModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.excel.ExcelExporter.ExportContext;
import org.zkoss.exporter.pdf.FontFactory;
import org.zkoss.exporter.pdf.PdfExporter;
import org.zkoss.exporter.pdf.PdfPCellFactory;
import org.zkoss.poi.ss.usermodel.Cell;
import org.zkoss.poi.ss.usermodel.CellStyle;
import org.zkoss.poi.ss.usermodel.Row;
import org.zkoss.poi.xssf.usermodel.XSSFCellStyle;
import org.zkoss.poi.xssf.usermodel.XSSFSheet;
import org.zkoss.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class BalanceSheetReport {
	

	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();
	private DataFilter filter=new DataFilter();
	private List<BalanceSheetReportModel> customerSummaryReport;
	private List<BalanceSheetReportModel> allCustomerSummaryReport;
	private List<CutomerSummaryReport> customerDeatiledReport;
	private List<CutomerSummaryReport> allCustomerDeatiledReport;
	
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	DecimalFormat formatter = new DecimalFormat("0.00");
	
	private int totalNoOfCustomer;
	
	private double totalAmount;
	
	private String period;
	
	private String totalAmountStr;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Date creationdate; 
	
	private boolean hideZero=false;
	private boolean hideArabic=true;
	
	
	public BalanceSheetReport()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Calendar c = Calendar.getInstance();	
			creationdate=df.parse(sdf.format(c.getTime()));
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in BalanceSheetReport ----> init", ex);			
		}
	}
	
	
	private List<BalanceSheetReportModel> filterData()
	{
		customerSummaryReport=allCustomerSummaryReport;
		List<BalanceSheetReportModel>  lst=new ArrayList<BalanceSheetReportModel>();
		if(customerSummaryReport!=null && customerSummaryReport.size()>0)
		{
		for (Iterator<BalanceSheetReportModel> i = customerSummaryReport.iterator(); i.hasNext();)
		{
			BalanceSheetReportModel tmp=i.next();				
			if(tmp.getActName().toLowerCase().contains(filter.getActName().toLowerCase())&&
					tmp.getDescription().toLowerCase().contains(filter.getDescription().toLowerCase())&&
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
    @NotifyChange({"customerSummaryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr"})
    public void changeFilter() 
    {
    	try
    	{
    		customerSummaryReport=filterData();
    		calcAmonut();
    		//footer="Total no. of Customer "+lstCustomers.size();
	  
    	}
    	catch (Exception ex) {
			logger.error("error in CustomerBlanceSummaryReport---changeFilter-->" , ex);
		}
    	
    }
	
	 @Command
	   public void resetCustomerSummaryReport()
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
				logger.error("ERROR in BalanceSheetReport ----> resetCustomerSummaryReport", ex);			
			}
	   }
	 
	 public void calcAmonut()
	 {
		 totalAmount=0;
		 totalNoOfCustomer=0;
		 for(BalanceSheetReportModel report:customerSummaryReport)
		 {
			 totalAmount=totalAmount+report.getAmount();
			 //period=report.getPeriod();
		 }
		 totalNoOfCustomer=customerSummaryReport.size();
		 totalAmountStr=formatter.format(totalAmount);
		 
	 }
	
	@Command
	 @NotifyChange({"customerSummaryReport","allCustomerSummaryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr","customerDeatiledReport","allCustomerDeatiledReport"})
	 public void searchCommand()
	 {
		try
		{
			customerSummaryReport= data.getBalanceSheetReoprt();
			allCustomerSummaryReport=customerSummaryReport;
			calcAmonut();
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in BalanceSheetReport ----> searchCommand", ex);			
		}
	 }
	
			
	 @Command
	public void exportCommand(@BindingParam("ref") Listbox grid) throws Exception 
	{			
		try
		{
			if(customerSummaryReport==null)
			  {
				 Messagebox.show("There are no record !!","Balance Sheet Report", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			  }
		final PdfExporter exporter = new PdfExporter();
		final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
		final FontFactory fontFactory = exporter.getFontFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final String title="Balance Sheet Report";
		String[] tsHeaders;
		String[] tsFooters;
		tsHeaders = new String[]{"Account Name","Account Name Arabic", "Amount"};
		tsFooters = new String[]{"Total Customers :"+totalNoOfCustomer+"","Total Amount :"+formatter.format(totalAmount)+""};
		final String[] headers=tsHeaders;
		final String[] footers=tsFooters;
		exporter.setInterceptor(new Interceptor <PdfPTable> () {
			@Override
			public void beforeRendering(PdfPTable table) {
				
				PdfPCell cellTitle = exporter.getPdfPCellFactory().getHeaderCell();
				Font fontTitle = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
				cellTitle.setPhrase(new Phrase(title, fontTitle));
				cellTitle.setColspan(3);
				cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cellTitle);
				table.completeRow();
				
				for (int i = 0; i < headers.length; i++) {
					String header = headers[i];
					Font font = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
					PdfPCell cell = exporter.getPdfPCellFactory().getHeaderCell();
					cell.setPhrase(new Phrase(header, font));
					table.addCell(cell);
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
		
		
		
		 exporter.export(headers.length, customerSummaryReport, new RowRenderer<PdfPTable, BalanceSheetReportModel>() {
		 @Override
		 public void render(PdfPTable table, BalanceSheetReportModel item, boolean isOddRow) 
		 {
			 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
			 PdfPCell cell = cellFactory.getCell(isOddRow);			
			 cell.setPhrase(new Phrase(item.getName(), font));
			 table.addCell(cell);
			 
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getDescription(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			String amtStr=BigDecimal.valueOf(item.getAmount()).toPlainString();
			double amtDbbl=Double.parseDouble(amtStr);
			cell.setPhrase(new Phrase(formatter.format(amtDbbl)+"", font));
			table.addCell(cell);
			table.completeRow();
				
		 }
			
		
		 }, out);
		 
		AMedia amedia = new AMedia("BalanceSheetReport.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
		}
		
		 catch (Exception ex)
			{	
			logger.error("ERROR in BalanceSheetReport ----> exportCommand", ex);			
			}
	}
	 @Command
	 public void exportToExcel(@BindingParam("ref") Listbox grid)
	 {
		 
		 
		 try
		 {
			 	
		 if(customerSummaryReport==null)
		  {
			 Messagebox.show("There are no record !!","Balance Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }
			
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 final String title="Balance Sheet Report";
		 String[] tsHeaders;
		 String[] tsFooters;
		 tsHeaders = new String[]{"Account Name","Account Name Arabic", "Amount"};
		 tsFooters = new String[]{"Total Customers :"+totalNoOfCustomer+"","Total Amount :"+formatter.format(totalAmount)+""};
		 final String[] headers=tsHeaders;
		 final String[] footers=tsFooters;
		
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
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
			    
			    @Override
			    public void afterRendering(XSSFWorkbook target) {
			    	
			    	 ExportContext context = exporter.getExportContext();
			         
				        for (String footer : footers) {
				            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
				            cell.setCellValue(footer);
				             				           
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
		 
		   	exporter.export(headers.length, customerSummaryReport, new RowRenderer<Row, BalanceSheetReportModel>() {
			@Override
			public void render(Row table, BalanceSheetReportModel item, boolean isOddRow) 
				 {
				 	ExportContext context = exporter.getExportContext();
			        XSSFSheet sheet = context.getSheet();				        
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getActName());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDescription());
			        String amtStr=BigDecimal.valueOf(item.getAmount()).toPlainString();
					double amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
				 }
				 
		    }, out);
		 
		   	AMedia amedia = new AMedia("customerSummaryReport.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			
	 }
		 catch (Exception ex)
		 {	
		  logger.error("ERROR in BalanceSheetReport ----> exportToExcel", ex);			
		 }
	 }
	 
	public List<BalanceSheetReportModel> getCustomerSummaryReport() {
		return customerSummaryReport;
	}

	public void setCustomerSummaryReport(
			List<BalanceSheetReportModel> customerSummaryReport) {
		this.customerSummaryReport = customerSummaryReport;
	}


	public List<BalanceSheetReportModel> getAllCustomerSummaryReport() {
		return allCustomerSummaryReport;
	}


	public void setAllCustomerSummaryReport(
			List<BalanceSheetReportModel> allCustomerSummaryReport) {
		this.allCustomerSummaryReport = allCustomerSummaryReport;
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


	public Date getCreationdate() {
		return creationdate;
	}


	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}


	public List<CutomerSummaryReport> getCustomerDeatiledReport() {
		return customerDeatiledReport;
	}


	public void setCustomerDeatiledReport(
			List<CutomerSummaryReport> customerDeatiledReport) {
		this.customerDeatiledReport = customerDeatiledReport;
	}


	public List<CutomerSummaryReport> getAllCustomerDeatiledReport() {
		return allCustomerDeatiledReport;
	}


	public void setAllCustomerDeatiledReport(
			List<CutomerSummaryReport> allCustomerDeatiledReport) {
		this.allCustomerDeatiledReport = allCustomerDeatiledReport;
	}


	public boolean isHideZero() {
		return hideZero;
	}

	@NotifyChange({"customerDeatiledReport","allCustomerDeatiledReport","totalNoOfCustomer","customerSummaryReport","allCustomerSummaryReport","totalAmount","totalAmountStr"})
	public void setHideZero(boolean hideZero) {
		this.hideZero = hideZero;
		try
    	{
		if(hideZero)
		 {
    		List<BalanceSheetReportModel> tempLIstSummary=new ArrayList<BalanceSheetReportModel>();
    		if(customerSummaryReport!=null)
    		{
    		for(BalanceSheetReportModel reportItr:customerSummaryReport)
    		{
    			BalanceSheetReportModel report=new BalanceSheetReportModel();
    			if(reportItr.getAmount()!=0)
    			{
    				report=reportItr;
    				tempLIstSummary.add(report);
    			}
    		}
    		
    		customerSummaryReport=tempLIstSummary;
    		allCustomerSummaryReport=customerSummaryReport;
    		totalNoOfCustomer=customerSummaryReport.size();
    		calcAmonut();
    		}
	  
		 }
		 else
		 {
			searchCommand();
		 }
    	}
    	catch (Exception ex) {
			logger.error("error in BalanceSheetReport---setHideZero-->" , ex);
		}
	}


	public boolean isHideArabic() {
		return hideArabic;
	}


	public void setHideArabic(boolean hideArabic) {
		this.hideArabic = hideArabic;
	}

	
	


}
