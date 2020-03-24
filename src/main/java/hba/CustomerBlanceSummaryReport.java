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

import model.CustomerModel;
import model.CutomerSummaryReport;
import model.DataFilter;
import model.QbListsModel;
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
import org.zkoss.zk.ui.util.Clients;
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

public class CustomerBlanceSummaryReport {
	

	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();
	private DataFilter filter=new DataFilter();
	private List<CutomerSummaryReport> customerSummaryReport;
	private List<CutomerSummaryReport> allCustomerSummaryReport;
	private List<CutomerSummaryReport> customerDeatiledReport;
	private List<CutomerSummaryReport> allCustomerDeatiledReport;
	
	private List<QbListsModel> lstCustomers;
	
	private QbListsModel selectedCustomer;
	
	private boolean inactiveCustomer;
	
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	DecimalFormat formatter = new DecimalFormat("0.00");
	
	private int totalNoOfCustomer;
	
	private double totalAmount=0;
	
	private String period;
	
	private String totalAmountStr;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Date creationdate; 
	
	private boolean hideZero=true;
	
	private boolean inculdeOtherTrasctions=false;
	
	private String status="Y";
	
	
	
	public CustomerBlanceSummaryReport()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			
			lstCustomers=data.fillQbList("'Customer'");
			
			inactiveCustomer=false;
			
			if(lstCustomers.size()>0)
				selectedCustomer=lstCustomers.get(0);
			
			Calendar c = Calendar.getInstance();	
			creationdate=df.parse(sdf.format(c.getTime()));
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.YEAR, 1998);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerBlanceSummaryReport ----> init", ex);			
		}
	}
	
	
	private List<CutomerSummaryReport> filterData()
	{
		customerSummaryReport=allCustomerSummaryReport;
		List<CutomerSummaryReport>  lst=new ArrayList<CutomerSummaryReport>();
		if(customerSummaryReport!=null && customerSummaryReport.size()>0)
		{
		for (Iterator<CutomerSummaryReport> i = customerSummaryReport.iterator(); i.hasNext();)
		{
			CutomerSummaryReport tmp=i.next();				
			if(tmp.getEnityName().toLowerCase().contains(filter.getEnityName().toLowerCase())&&
				//	tmp.getEnityNameAr().toLowerCase().contains(filter.getEnityNameAr().toLowerCase())&&
				//	tmp.getPeriod().toLowerCase().contains(filter.getPeriod().toLowerCase())&&*/
					(tmp.getBalance()+"").toLowerCase().contains(filter.getAmount().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		}
		
		return lst;
		
	}
	
	
	private List<CutomerSummaryReport> filterDataDeatiled()
	{
		customerDeatiledReport=allCustomerDeatiledReport;
		List<CutomerSummaryReport>  lst=new ArrayList<CutomerSummaryReport>();
		if(customerDeatiledReport!=null && customerDeatiledReport.size()>0)
		{
		for (Iterator<CutomerSummaryReport> i = customerDeatiledReport.iterator(); i.hasNext();)
		{
			CutomerSummaryReport tmp=i.next();				
			if(tmp.getEnityName().toLowerCase().contains(filter.getEnityName().toLowerCase())/*&&
					tmp.getEnityNameAr().toLowerCase().contains(filter.getEnityNameAr().toLowerCase())*/
					/*tmp.getTxnType().toLowerCase().contains(filter.getTxnType().toLowerCase())&&
					tmp.getTxnDate().toLowerCase().contains(filter.getTxnDate().toLowerCase())&&
					tmp.getEnityNameAr().toLowerCase().contains(filter.getEnityNameAr().toLowerCase())&&
					tmp.getRefranceNumber().toLowerCase().contains(filter.getRefranceNumber().toLowerCase())&&
					tmp.getAcountName().toLowerCase().contains(filter.getAcountName().toLowerCase())&&
					tmp.getClassName().toLowerCase().contains(filter.getClassName().toLowerCase())&&
					(""+tmp.getDebit()).toLowerCase().contains(filter.getDebit().toLowerCase())&&
					(""+tmp.getCredit()).toLowerCase().contains(filter.getCredit().toLowerCase())&&
					(tmp.getBalance()+"").toLowerCase().contains(filter.getBalance().toLowerCase())&&
					(tmp.getAmount()+"").toLowerCase().contains(filter.getAmount().toLowerCase())*/
					)
			{
				lst.add(tmp);
			}
		}
		}
		return lst;
		
	}
	
	
	@Command
    @NotifyChange({"customerSummaryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr","customerDeatiledReport"})
    public void changeFilter() 
    {
    	try
    	{
    		customerSummaryReport=filterData();
    		customerDeatiledReport=filterDataDeatiled();
    		calcAmonut();
    		//footer="Total no. of Customer "+lstCustomers.size();
	  
    	}
    	catch (Exception ex) {
			logger.error("error in CustomerBlanceSummaryReport---changeFilter-->" , ex);
		}
    	
    }
	
	@Command
    @NotifyChange({"customerDeatiledReport","allCustomerDeatiledReport","totalNoOfCustomer","customerSummaryReport"})
    public void changeFilterDetailed() 
    {
    	try
    	{
    		customerDeatiledReport=filterDataDeatiled();
    		customerSummaryReport=filterData();
    		calcAmonut();
    		//totalNoOfCustomer=customerDeatiledReport.size();
    		//footer="Total no. of Customer "+lstCustomers.size();
	  
    	}
    	catch (Exception ex) {
			logger.error("error in CustomerBlanceSummaryReport---changeFilterDetailed-->" , ex);
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
				logger.error("ERROR in CustomerBlanceSummaryReport ----> resetCustomerSummaryReport", ex);			
			}
	   }
	 
	 public void calcAmonut()
	 {
		 
		 totalNoOfCustomer=0;
		 totalAmount=0;
		 for(CutomerSummaryReport report:customerSummaryReport)
		 {
			 totalAmount=totalAmount+report.getBalance();
		 }
		 totalNoOfCustomer=customerSummaryReport.size();
		 DecimalFormat formatter = new DecimalFormat("###,###,###.00");
		 totalAmountStr=formatter.format(totalAmount);
		 
	 }
	
	@Command
	 @NotifyChange({"customerSummaryReport","allCustomerSummaryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr","customerDeatiledReport","allCustomerDeatiledReport"})
	 public void searchCommand()
	 {
		try
		{
			
			customerSummaryReport= data.getNewCutomerSummaryReport(selectedCustomer.getRecNo(),status,hideZero);
			allCustomerSummaryReport=customerSummaryReport;
			calcAmonut();
			
			/*customerSummaryReport= data.getCutomerSummaryReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions,hideZero);
			allCustomerSummaryReport=customerSummaryReport;
			customerDeatiledReport= data.getCutomerDeatiledReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions);
			allCustomerDeatiledReport=customerDeatiledReport;
			calcAmonut();*/
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in CustomerBlanceSummaryReport ----> searchCommand", ex);			
		}
	 }
	
			
	 @Command
	public void exportCommand(@BindingParam("ref") Listbox grid) throws Exception 
	{			
		try
		{
			if(customerSummaryReport==null)
			  {
				 Messagebox.show("There are no record !!","Customer Report", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			  }
		final PdfExporter exporter = new PdfExporter();
		final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
		final FontFactory fontFactory = exporter.getFontFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final String title="Customer Summary Report";
		String[] tsHeaders;
		String[] tsFooters;
		tsHeaders = new String[]{"Name","Arabic Name", "Amount"};
		tsFooters = new String[]{"Total Customers :"+totalNoOfCustomer+"","Total Balance :"+formatter.format(totalAmount)+""};
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
		
		
		
		 exporter.export(headers.length, customerSummaryReport, new RowRenderer<PdfPTable, CutomerSummaryReport>() {
		 @Override
		 public void render(PdfPTable table, CutomerSummaryReport item, boolean isOddRow) 
		 {
			 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
			 PdfPCell cell = cellFactory.getCell(isOddRow);			
			 cell.setPhrase(new Phrase(item.getEnityName(), font));
			 table.addCell(cell);
			 
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getEnityNameAr(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			String amtStr=BigDecimal.valueOf(item.getBalance()).toPlainString();
			double amtDbbl=Double.parseDouble(amtStr);
			cell.setPhrase(new Phrase(formatter.format(amtDbbl)+"", font));
			table.addCell(cell);
			table.completeRow();
				
		 }
			
		
		 }, out);
		 
		AMedia amedia = new AMedia("customerSummaryReport.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
		}
		
		 catch (Exception ex)
			{	
			logger.error("ERROR in CustomerBlanceSummaryReport ----> exportCommand", ex);			
			}
	}
	 @Command
	 public void exportToExcel(@BindingParam("ref") Listbox grid)
	 {
		 
		 
		 try
		 {
			 	
		 if(customerSummaryReport==null)
		  {
			 Messagebox.show("There are no record !!","Customer Report", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }
			
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 final String title="Customer Summary Report";
		 String[] tsHeaders;
		 String[] tsFooters;
		 tsHeaders = new String[]{"Name","Arabic Name", "Balance"};
		 tsFooters = new String[]{"Total No. of Customers :"+totalNoOfCustomer+"","Total Balance :"+formatter.format(totalAmount)+""};
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
		 
		   	exporter.export(headers.length, customerSummaryReport, new RowRenderer<Row, CutomerSummaryReport>() {
			@Override
			public void render(Row table, CutomerSummaryReport item, boolean isOddRow) 
				 {
				 	ExportContext context = exporter.getExportContext();
			        XSSFSheet sheet = context.getSheet();				        
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnityName());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnityNameAr());
			        String amtStr=BigDecimal.valueOf(item.getBalance()).toPlainString();
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
		  logger.error("ERROR in CustomerBlanceSummaryReport ----> exportToExcel", ex);			
		 }
	 }
	 
	 
	 
	 @Command
	 public void exportToExcelDeatiled(@BindingParam("ref") Listbox grid)
	 {
		 
		 
		 try
		 {
			 	
		 if(customerDeatiledReport==null)
		  {
			 Messagebox.show("There are no record !!","Customer Report", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }
			
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 final String title="Customer Deatiled Report";
		 String[] tsHeaders;
		 String[] tsFooters;
		 tsHeaders = new String[]{"Name","Arabic Name", "Balance"};
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
			         
				       /* for (String footer : footers) {
				            Cell cell = exporter.getOrCreateCell(context.moveToNextCell(), context.getSheet());
				            cell.setCellValue(footer);
				             				           
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
		 
		   	exporter.export(headers.length, customerSummaryReport, new RowRenderer<Row, CutomerSummaryReport>() {
			@Override
			public void render(Row table, CutomerSummaryReport item, boolean isOddRow) 
				 {
				 	ExportContext context = exporter.getExportContext();
			        XSSFSheet sheet = context.getSheet();				        
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnityName());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnityNameAr());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTxnType());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTxnDate());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAcountName());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getClassName());
					String amtStr4=BigDecimal.valueOf(item.getDebit()).toPlainString();
					double amtDbb4=Double.parseDouble(amtStr4);
				       exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbb4));
			        String amtStr=BigDecimal.valueOf(item.getAmount()).toPlainString();
			        String amtStr3=BigDecimal.valueOf(item.getCredit()).toPlainString();
					double amtDbb3=Double.parseDouble(amtStr3);
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbb3));
					double amtDbbl=Double.parseDouble(amtStr);
					exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
					String amtStr2=BigDecimal.valueOf(item.getBalance()).toPlainString();
					double amtDbb2=Double.parseDouble(amtStr2);
				       exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbb2));
			       
				 }
				 
		    }, out);
		 
		   	AMedia amedia = new AMedia("customerDeatiledReport.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			
	 }
		 catch (Exception ex)
		 {	
		  logger.error("ERROR in CustomerBlanceSummaryReport ----> exportToExcelDeatiled", ex);			
		 }
	 }
	

	public List<CutomerSummaryReport> getCustomerSummaryReport() {
		return customerSummaryReport;
	}

	public void setCustomerSummaryReport(
			List<CutomerSummaryReport> customerSummaryReport) {
		this.customerSummaryReport = customerSummaryReport;
	}


	public List<CutomerSummaryReport> getAllCustomerSummaryReport() {
		return allCustomerSummaryReport;
	}


	public void setAllCustomerSummaryReport(
			List<CutomerSummaryReport> allCustomerSummaryReport) {
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

	@NotifyChange({"customerDeatiledReport","allCustomerDeatiledReport","totalNoOfCustomer","customerSummaryReport","allCustomerSummaryReport","totalAmount","period","totalAmountStr",})
	public void setHideZero(boolean hideZero) {
		this.hideZero = hideZero;
		try
    	{
			if(customerDeatiledReport==null)
				return;
			
			if(hideZero)
			{
    		List<CutomerSummaryReport> tempLIstDeatiled=new ArrayList<CutomerSummaryReport>();
    		List<CutomerSummaryReport> tempLIstSummary=new ArrayList<CutomerSummaryReport>();
    		
    		for(CutomerSummaryReport reportItr:customerDeatiledReport)
    		{
    			CutomerSummaryReport report=new CutomerSummaryReport();
    			if(reportItr.getBalance()!=0 )//|| reportItr.getAmount()!=0 || reportItr.getCredit()!=0 || reportItr.getCredit()!=0
    			{
    				report=reportItr;
    				tempLIstDeatiled.add(report);
    			}
    		}
    		
    		for(CutomerSummaryReport reportItr:customerSummaryReport)
    		{
    			CutomerSummaryReport report=new CutomerSummaryReport();
    			if(reportItr.getBalance()!=0)
    			{
    				report=reportItr;
    				tempLIstSummary.add(report);
    			}
    		}
    		customerDeatiledReport=tempLIstDeatiled;
    		allCustomerDeatiledReport=customerDeatiledReport;
    		customerSummaryReport=tempLIstSummary;
    		allCustomerSummaryReport=customerSummaryReport;
    		if(customerSummaryReport!=null)
    		totalNoOfCustomer=customerSummaryReport.size();
    		calcAmonut();
    	}
		else
		{
			searchCommand();
		}
    	}
    	catch (Exception ex) {
			logger.error("error in CustomerBlanceSummaryReport---setHideZero-->" , ex);
		}
	}


	/**
	 * @return the lstCustomers
	 */
	public List<QbListsModel> getLstCustomers() {
		return lstCustomers;
	}


	/**
	 * @param lstCustomers the lstCustomers to set
	 */
	public void setLstCustomers(List<QbListsModel> lstCustomers) {
		this.lstCustomers = lstCustomers;
	}


	/**
	 * @return the selectedCustomer
	 */
	public QbListsModel getSelectedCustomer() {
		return selectedCustomer;
	}


	/**
	 * @param selectedCustomer the selectedCustomer to set
	 */
	@NotifyChange({"customerSummaryReport","allCustomerSummaryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr","customerDeatiledReport","allCustomerDeatiledReport"})
	public void setSelectedCustomer(QbListsModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
		
		/*if(selectedCustomer!=null && selectedCustomer.getRecNo()>0)
		{
			customerSummaryReport= data.getCutomerSummaryReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions,hideZero);
			allCustomerSummaryReport=customerSummaryReport;
			customerDeatiledReport= data.getCutomerDeatiledReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions);
			allCustomerDeatiledReport=customerDeatiledReport;
			calcAmonut();
		}
		else
		{
			customerSummaryReport= data.getCutomerSummaryReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions,hideZero);
			allCustomerSummaryReport=customerSummaryReport;
			customerDeatiledReport= data.getCutomerDeatiledReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions);
			allCustomerDeatiledReport=customerDeatiledReport;
			calcAmonut();
		}*/
		
	}


	/**
	 * @return the inactiveCustomer
	 */
	public boolean isInactiveCustomer() {
		return inactiveCustomer;
	}


	/**
	 * @param inactiveCustomer the inactiveCustomer to set
	 */
	public void setInactiveCustomer(boolean inactiveCustomer) {
		this.inactiveCustomer = inactiveCustomer;
		if(inactiveCustomer)
		{
			status="";
		}
		else
		{
			status="Y";
		}
	}


	/**
	 * @return the inculdeOtherTrasctions
	 */
	public boolean isInculdeOtherTrasctions() {
		return inculdeOtherTrasctions;
	}


	/**
	 * @param inculdeOtherTrasctions the inculdeOtherTrasctions to set
	 */
	@NotifyChange({"inculdeOtherTrasctions","customerSummaryReport","allCustomerSummaryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr","customerDeatiledReport","allCustomerDeatiledReport"})
	public void setInculdeOtherTrasctions(boolean inculdeOtherTrasctions) {
		this.inculdeOtherTrasctions = inculdeOtherTrasctions;
		
		customerSummaryReport= data.getCutomerSummaryReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions,hideZero);
		allCustomerSummaryReport=customerSummaryReport;
		customerDeatiledReport= data.getCutomerDeatiledReport(toDate,selectedCustomer.getRecNo(),status,inculdeOtherTrasctions);
		allCustomerDeatiledReport=customerDeatiledReport;
		calcAmonut();
				
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	
	


}
