package hba;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.CustomerStatusHistoryModel;
import model.DataFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;

public class CustomerStatusHistory {


	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();
	private DataFilter filter=new DataFilter();
	private List<CustomerStatusHistoryModel> customerHistroyStatusReport;
	private List<CustomerStatusHistoryModel> allCustomerStatusHistoryReport;


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


	@SuppressWarnings("unused")
	public CustomerStatusHistory()
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
			logger.error("ERROR in CustomerBlanceSummaryReport ----> init", ex);			
		}
	}


	private List<CustomerStatusHistoryModel> filterData()
	{
		customerHistroyStatusReport=allCustomerStatusHistoryReport;
		List<CustomerStatusHistoryModel>  lst=new ArrayList<CustomerStatusHistoryModel>();
		if(customerHistroyStatusReport!=null && customerHistroyStatusReport.size()>0)
		{
			for (Iterator<CustomerStatusHistoryModel> i = customerHistroyStatusReport.iterator(); i.hasNext();)
			{
				CustomerStatusHistoryModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
		}
		return lst;

	}





	@Command
	@NotifyChange({"customerHistroyStatusReport","totalNoOfCustomer","totalAmount","period","totalAmountStr"})
	public void changeFilter() 
	{
		try
		{
			customerHistroyStatusReport=filterData();
			totalNoOfCustomer=customerHistroyStatusReport.size();

		}
		catch (Exception ex) {
			logger.error("error in CustomerStatusHistory---changeFilter-->" , ex);
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
			logger.error("ERROR in CustomerStatusHistory ----> resetCustomerSummaryReport", ex);			
		}
	}



	@Command
	@NotifyChange({"customerHistroyStatusReport","allCustomerStatusHistoryReport","totalNoOfCustomer","totalAmount","period","totalAmountStr","customerDeatiledReport","allCustomerDeatiledReport"})
	public void searchCommand()
	{
		try
		{
			customerHistroyStatusReport= data.getCustomerStatusHistoryReport(fromDate,toDate);
			allCustomerStatusHistoryReport=customerHistroyStatusReport;
			totalNoOfCustomer=customerHistroyStatusReport.size();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CustomerStatusHistory ----> searchCommand", ex);			
		}
	}


	@Command
	public void exportCommand(@BindingParam("ref") Listbox grid) throws Exception 
	{			
		/*try
		{
			if(customerHistroyStatusReport==null)
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



		 exporter.export(headers.length, customerHistroyStatusReport, new RowRenderer<PdfPTable, CutomerSummaryReport>() {
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
			String amtStr=BigDecimal.valueOf(item.getAmount()).toPlainString();
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
			}*/
	}
	@Command
	public void exportToExcel(@BindingParam("ref") Listbox grid)
	{


		/* try
		 {

		 if(customerHistroyStatusReport==null)
		  {
			 Messagebox.show("There are no record !!","Customer Report", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }

		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 final String title="Customer Summary Report";
		 String[] tsHeaders;
		 String[] tsFooters;
		 tsHeaders = new String[]{"Name","Arabic Name", "Amount"};
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

		   	exporter.export(headers.length, customerHistroyStatusReport, new RowRenderer<Row, CutomerSummaryReport>() {
			@Override
			public void render(Row table, CutomerSummaryReport item, boolean isOddRow) 
				 {
				 	ExportContext context = exporter.getExportContext();
			        XSSFSheet sheet = context.getSheet();				        
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnityName());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnityNameAr());
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
		  logger.error("ERROR in CustomerBlanceSummaryReport ----> exportToExcel", ex);			
		 }*/
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


	/**
	 * @return the customerHistroyStatusReport
	 */
	public List<CustomerStatusHistoryModel> getCustomerHistroyStatusReport() {
		return customerHistroyStatusReport;
	}


	/**
	 * @param customerHistroyStatusReport the customerHistroyStatusReport to set
	 */
	public void setCustomerHistroyStatusReport(
			List<CustomerStatusHistoryModel> customerHistroyStatusReport) {
		this.customerHistroyStatusReport = customerHistroyStatusReport;
	}


	/**
	 * @return the allCustomerStatusHistoryReport
	 */
	public List<CustomerStatusHistoryModel> getAllCustomerStatusHistoryReport() {
		return allCustomerStatusHistoryReport;
	}


	/**
	 * @param allCustomerStatusHistoryReport the allCustomerStatusHistoryReport to set
	 */
	public void setAllCustomerStatusHistoryReport(
			List<CustomerStatusHistoryModel> allCustomerStatusHistoryReport) {
		this.allCustomerStatusHistoryReport = allCustomerStatusHistoryReport;
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
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


	public DecimalFormat getFormatter() {
		return formatter;
	}


	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
	}


	public boolean isHideZero() {
		return hideZero;
	}


	public void setHideZero(boolean hideZero) {
		this.hideZero = hideZero;
	}


	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}










}
