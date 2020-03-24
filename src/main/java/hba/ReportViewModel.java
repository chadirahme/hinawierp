package hba;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.BalanceSheetReportModel;
import model.DataFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

public class ReportViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	ReportData data=new ReportData();
	private boolean hideZero=false;
	private boolean hideArabic=true;
	private List<BalanceSheetReportModel> lstTBReport;
	List<BalanceSheetReportModel> lstAllTBReport;
	private DataFilter filter=new DataFilter();
	DecimalFormat formatter = new DecimalFormat("0.00");
	List<BalanceSheetReportModel> lstTrialBalance;
	private List<BalanceSheetReportModel> lstProfitLossReport;
	List<BalanceSheetReportModel> lstAllProfitLossReport;
	private List<BalanceSheetReportModel> lstGeneralLedgerReport;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private String balaneofDate;
	private String balaneoftoday;
	
	private double totalOpeningDb;
	private double totalOpeningCr;
	private double totalOpeningNet;
	private double totalMovementDb;
	private double totalMovementCr;
	private double totalMovementNet;
	private double totalBalanceDb;
	private double totalBalanceCr;
	
	private double totalBalance;
	
	public ReportViewModel()
	{
		try
		{
			
			Calendar c = Calendar.getInstance();
			Date toDate=df.parse(sdf.format(c.getTime()));
			balaneoftoday="Balance as of " +df.format(toDate) ;
			
			c.add(Calendar.MONTH, -1);		
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date fromDate=df.parse(sdf.format(c.getTime()));	
			logger.info(df.format(fromDate));
			balaneofDate="Balance as of " +df.format(fromDate) ;
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReportViewModel ----> init", ex);			
		}
	}
	
	@Command
	@NotifyChange({"lstTBReport","totalOpeningDb","totalOpeningCr","totalOpeningNet","totalMovementDb","totalMovementCr","totalMovementNet","totalBalanceDb","totalBalanceCr"})
	 public void searchTrialBalanceCommand()
	 {
		try
		{
			totalOpeningDb=0;
			 totalOpeningCr=0;
			 totalOpeningNet=0;
			 totalMovementDb=0;
			 totalMovementCr=0;
			 totalMovementNet=0;
			 totalBalanceDb=0;
			 totalBalanceCr=0;
			
			String tmpCurActType ="",tmpPRVActType="";
			double tmpOpeningDb =0,tmpOpeningCr =0,tmpOpeningNet=0,tmpMovementDb =0,tmpMovementCr =0,tmpMovementNet =0,tmpBalanceDb =0,tmpBalanceCr =0 ;
			lstTBReport=new ArrayList<BalanceSheetReportModel>();
			lstTrialBalance=data.getTrialBalanceReoprt();
			List<BalanceSheetReportModel> lstGL=data.getRPTGeneralLedger();
			
			for (BalanceSheetReportModel item : lstGL) 
			{
				BalanceSheetReportModel obj=new BalanceSheetReportModel();
//				switch (item.getAccountType())
//				{
//				case "Cash":
//				case "Cheque Under Collection":
//				case "Post Dated Cheque":
//					tmpCurActType="Bank";
//					break;
//				default:
//					tmpCurActType=item.getAccountType();
//					break;
//				}
				
                //If Not tmpPRVActType = tmpCurActType Then
                	if (!tmpPRVActType.equalsIgnoreCase(tmpCurActType))
                	{
                		if(!tmpPRVActType.equals(""))
                		{
                			if(tmpPRVActType.equals("Bank"))
                			obj.setAccountNameORG("Total By Bank,Cash,CUC");
                			else
                			obj.setAccountNameORG("Total By " +tmpPRVActType);	
                			obj.setRowType("ST");
                			//logger.info("addd >> " + obj.getAccountNameORG());
                			obj.setDescription("");
                			obj.setAccountType("");
                			obj.setAccountNumber("");
                			
                			obj.setOpeningDb(tmpOpeningDb);
                			obj.setOpeningCr(tmpOpeningCr);
                			obj.setOpeningNet(tmpOpeningNet);
                			obj.setMovementCr(tmpMovementCr);
                			obj.setMovementDb(tmpMovementDb);
                			obj.setMovementNet(tmpMovementNet);
                			obj.setBalanceCr(tmpBalanceCr);
                			obj.setBalanceDb(tmpBalanceDb);
                			                			
                			lstTBReport.add(obj);
                			                			
                			tmpOpeningDb=0;
            				tmpOpeningCr=0;
            				tmpOpeningNet=0;
            				tmpMovementCr=0;
            				tmpMovementDb=0;
            				tmpMovementNet=0;
            				tmpBalanceCr=0;
            				tmpBalanceDb=0;
                			
                			obj=new BalanceSheetReportModel();
                		}
                        tmpPRVActType = tmpCurActType;
                	}
				
				//obj.setAccountType(tmpCurActType);
				if(item.getActName().equalsIgnoreCase("No accnt"))
				{
					obj.setAccountNameORG(item.getActName());
				}
				else
				{
					obj.setAccountNameORG(item.getAccountNameORG());
				}
				
				/*if(data.CheckSubAccounts(obj.getAccountNameORG())==false)
					obj.setRowType("M");
				else
					obj.setRowType("");*/
				
				obj.setAccountNumber(item.getAccountNumber());
				obj.setDescription(item.getDescription());
				
//				switch (item.getAccountType())
//				{
//				case "Cheque Under Collection":
//					obj.setAccountType("CUC(Cheque Under Collection)");
//					break;
//
//				case "Post Dated Cheque":
//					obj.setAccountType("PDC(Post Dated Cheque)");
//					break;
//				default:
//					obj.setAccountType(item.getAccountType());
//					break;
//				}
				
				if(item.getBalance()>=0)
				{
					obj.setOpeningDb(item.getBalance());
				}
				else
				{
					obj.setOpeningCr(item.getBalance() * -1 );					
				}
				BalanceSheetReportModel findRow=getTrialAccountByName(item.getActName());
				if(findRow!=null)
				{
					obj.setMovementDb(findRow.getDebit());
					obj.setMovementCr(findRow.getCredit());
					obj.setMovementNet(obj.getMovementDb() - obj.getMovementCr());
					//check type for movment net
//					switch (item.getAccountType())
//					{
//					case "AccountsPayable":
//					case "OtherCurrentLiability":
//					case "Equity":
//					case "Income":
//					case "OtherIncome":
//					case "LongTermLiability":
//					case "CreditCard":
//						obj.setMovementNet((obj.getMovementCr() - obj.getMovementDb())*-1);
//						break;
//
//					default:
//						obj.setMovementNet(obj.getMovementDb() - obj.getMovementCr());
//						break;
//					}
					
					if(findRow.getBalance()>=0)
					{
						obj.setBalanceDb(findRow.getBalance());
					}
					else
					{
						obj.setBalanceCr(findRow.getBalance()*-1);
					}
				}
				
				obj.setOpeningNet(item.getBalance());
				/*if(obj.getOpeningDb()>=0)
				{
					obj.setOpeningNet(obj.getOpeningDb());
				}
				else
				{
					obj.setOpeningNet(obj.getOpeningCr()*-1);
				}*/
				
				double net=obj.getOpeningNet() + obj.getMovementNet();
				if(net>=0)
				obj.setBalanceDb(net);
				else
				obj.setBalanceCr(net*-1);

											
				obj.setBalance(item.getBalance());
				obj.setSubLevel(item.getSubLevel());
				obj.setRowType("M");
				if(obj.getRowType().equals("M")) //.getSubLevel()==0) 
				{
				tmpOpeningDb+=obj.getOpeningDb();
				tmpOpeningCr+=obj.getOpeningCr();
				tmpOpeningNet+=obj.getOpeningNet();
				tmpMovementCr+=obj.getMovementCr();
				tmpMovementDb+=obj.getMovementDb();
				tmpMovementNet+=obj.getMovementNet();
				tmpBalanceCr+=obj.getBalanceCr();
				tmpBalanceDb+=obj.getBalanceDb();
				
				logger.info("tmpOpeningDb= " +String.valueOf(tmpOpeningDb));				
				}
				lstTBReport.add(obj);
				
			}
			
			for (BalanceSheetReportModel item : lstTBReport) {
				totalOpeningDb+=item.getOpeningDb();
				totalOpeningCr+=item.getOpeningCr();
				totalOpeningNet+=item.getOpeningNet();
				totalMovementDb+=item.getMovementDb();
				totalMovementCr+=item.getMovementCr();
				totalMovementNet+=item.getMovementNet();
				totalBalanceDb+=item.getBalanceDb();
				totalBalanceCr+=item.getBalanceCr();
			}
			//logger.info("totalOpeningDb> " + totalOpeningDb);
			lstAllTBReport=lstTBReport;
			
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in ReportViewModel ----> searchTrialBalanceCommand", ex);			
		}
	 }
	
	private BalanceSheetReportModel getTrialAccountByName(String accountName)
	{
		try
		{
			for (BalanceSheetReportModel item : lstTrialBalance) 
			{
				if(item.getName().equalsIgnoreCase(accountName))
				{
					return item;
				}
			}
			return null;
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in ReportViewModel ----> getTrialAccountByName", ex);
		return null;
		}
	}
	
	@Command
    @NotifyChange({"lstTBReport","totalAmount","period","totalAmountStr"})
    public void changeFilter() 
    {
    	try
    	{
    		
    		lstTBReport=filterData();
    			    		
    		//calcAmonut();
    		//footer="Total no. of Customer "+lstCustomers.size();
	  
    	}
    	catch (Exception ex) {
			logger.info("error in CustomerBlanceSummaryReport---changeFilter-->" , ex);
		}
    	
    }
	
	@Command
    @NotifyChange({"lstProfitLossReport"})
    public void changeProfitFilter() 
    {
    	try
    	{
    	
    		lstProfitLossReport=filterData();	    		    		  
    	}
    	catch (Exception ex) {
			logger.info("error in CustomerBlanceSummaryReport---changeFilter-->" , ex);
		}
    	
    }
	
	private List<BalanceSheetReportModel> filterData()
	{
		lstTBReport=lstAllTBReport;
		lstProfitLossReport=lstAllProfitLossReport;
		
		List<BalanceSheetReportModel>  lst=new ArrayList<BalanceSheetReportModel>();
		
		if(lstTBReport!=null && lstTBReport.size()>0)
		{
		for (Iterator<BalanceSheetReportModel> i = lstTBReport.iterator(); i.hasNext();)
		{
			BalanceSheetReportModel tmp=i.next();				
			if(tmp.getAccountNameORG().toLowerCase().contains(filter.getActName().toLowerCase())&&
					tmp.getDescription().toLowerCase().contains(filter.getDescription().toLowerCase())&&
					tmp.getAccountNumber().toLowerCase().contains(filter.getAccountNumber().toLowerCase())&&
					tmp.getAccountType().toLowerCase().contains(filter.getType().toLowerCase())
					)
					
			{
				lst.add(tmp);
			}
		 }
		}
		
		if(lstProfitLossReport!=null && lstProfitLossReport.size()>0)
		{
		for (Iterator<BalanceSheetReportModel> i = lstProfitLossReport.iterator(); i.hasNext();)
		{
			BalanceSheetReportModel tmp=i.next();				
			if(tmp.getAccountNameORG().toLowerCase().contains(filter.getActName().toLowerCase())&&
					tmp.getDescription().toLowerCase().contains(filter.getDescription().toLowerCase())					
			  )
					
			{
				lst.add(tmp);
			}
		 }
		}
		
		return lst;
		
	}
	
	@Command
	public void exportProfitToExcel(@BindingParam("ref") Listbox grid)
	{
		try
		 {
			 	
		 if(lstProfitLossReport==null)
		  {
			 Messagebox.show("There are no record !!","Profit & Loss Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }
		 
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 final String title="Profit & Loss Sheet Report";
		 String[] tsHeaders;		
		 tsHeaders = new String[]{"Account Name","Account Name-Arabic","Amount"};		 
		 final String[] headers=tsHeaders;
				 
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
			   // @Override
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
			    
			    //@Override
			    public void afterRendering(XSSFWorkbook target) {
			    	
			    ExportContext context = exporter.getExportContext();
			         				       
			    }				    				   
			});
		 
		   	exporter.export(headers.length, lstProfitLossReport, new RowRenderer<Row, BalanceSheetReportModel>() {
			//@Override
			public void render(Row table, BalanceSheetReportModel item, boolean isOddRow) 
				 {
				 	ExportContext context = exporter.getExportContext();
			        XSSFSheet sheet = context.getSheet();				        
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAccountNameORG());			      
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDescription());
			       
			        String amtStr=BigDecimal.valueOf(item.getBalance()).toPlainString();
					double amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        			        
				 }
				 
		    }, out);
		 
		   	AMedia amedia = new AMedia("ProfitLossReport.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			
		 
		 }
		 catch (Exception ex)
		 {	
		  logger.error("ERROR in BalanceSheetReport ----> exportProfitToExcel", ex);			
		 }
	}
	
	@Command
	 public void exportToExcel(@BindingParam("ref") Listbox grid)
	 {
		 
		 
		 try
		 {
			 	
		 if(lstTBReport==null)
		  {
			 Messagebox.show("There are no record !!","Trial Balance Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }
			
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 final ExcelExporter exporter = new ExcelExporter();
		 final String title="Balance Sheet Report";
		 String[] tsHeaders;
		 String[] tsFooters;
		 tsHeaders = new String[]{"Account Name","Account No","Description", "Type","Opening Debit","Opening Credit","Opening Net" , "Movement Debit","Movement Credit","Movement Net","Debit","Credit"};
		 tsFooters = new String[]{"Total Customers :"+"100"+"","Total Amount :"+formatter.format(5000)+""};
		 final String[] headers=tsHeaders;
		 final String[] footers=tsFooters;
		
		 exporter.setInterceptor(new Interceptor<XSSFWorkbook>() {
		     
			   // @Override
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
			    
			   // @Override
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
		 
		   	exporter.export(headers.length, lstTBReport, new RowRenderer<Row, BalanceSheetReportModel>() {
			//@Override
			public void render(Row table, BalanceSheetReportModel item, boolean isOddRow) 
				 {
				 	ExportContext context = exporter.getExportContext();
			        XSSFSheet sheet = context.getSheet();				        
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAccountNameORG());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAccountNumber());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDescription());
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAccountType());
			        
			        String amtStr=BigDecimal.valueOf(item.getOpeningDb()).toPlainString();
					double amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getOpeningCr()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getOpeningNet()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getMovementDb()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getMovementCr()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getMovementNet()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getBalanceDb()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
			        
			        amtStr=BigDecimal.valueOf(item.getBalanceCr()).toPlainString();
					amtDbbl=Double.parseDouble(amtStr);
			        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(formatter.format(amtDbbl));
				 }
				 
		    }, out);
		 
		   	AMedia amedia = new AMedia("TrialBalanceReport.xls", "xls", "application/file", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			
	 }
		 catch (Exception ex)
		 {	
		  logger.error("ERROR in BalanceSheetReport ----> exportToExcel", ex);			
		 }
	 }

	
	@Command
	@NotifyChange({"lstProfitLossReport","totalBalance"})
	 public void searchProfitLossCommand()
	 {
		try
		{
			totalBalance=0;
			lstProfitLossReport=new ArrayList<BalanceSheetReportModel>();
			lstProfitLossReport=data.getProfitLossReport();
			lstAllProfitLossReport=lstProfitLossReport;
			
			for (BalanceSheetReportModel item : lstProfitLossReport) 
			{
				totalBalance+=item.getBalance();
			}
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in ReportViewModel ----> searchProfitLossCommand", ex);			
		}
		
	 }
	
	@Command
	@NotifyChange({"lstGeneralLedgerReport"})
	public void searchGeneralLedgerCommand()
	{
		try
		{
			lstGeneralLedgerReport=new ArrayList<BalanceSheetReportModel>();
			lstGeneralLedgerReport=data.getGeneralLedgerReport();
			//lstAllProfitLossReport=lstProfitLossReport;
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in ReportViewModel ----> searchGeneralLedgerCommand", ex);			
		}
	}

	
	public boolean isHideZero() {
		return hideZero;
	}

	public void setHideZero(boolean hideZero) {
		this.hideZero = hideZero;
	}

	public List<BalanceSheetReportModel> getLstTBReport() {
		return lstTBReport;
	}

	public void setLstTBReport(List<BalanceSheetReportModel> lstTBReport) {
		this.lstTBReport = lstTBReport;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public boolean isHideArabic() {
		return hideArabic;
	}

	public void setHideArabic(boolean hideArabic) {
		this.hideArabic = hideArabic;
	}

	public String getBalaneofDate() {
		return balaneofDate;
	}

	public void setBalaneofDate(String balaneofDate) {
		this.balaneofDate = balaneofDate;
	}

	public String getBalaneoftoday() {
		return balaneoftoday;
	}

	public void setBalaneoftoday(String balaneoftoday) {
		this.balaneoftoday = balaneoftoday;
	}

	public List<BalanceSheetReportModel> getLstProfitLossReport() {
		return lstProfitLossReport;
	}

	public void setLstProfitLossReport(List<BalanceSheetReportModel> lstProfitLossReport) {
		this.lstProfitLossReport = lstProfitLossReport;
	}

	public List<BalanceSheetReportModel> getLstGeneralLedgerReport() {
		return lstGeneralLedgerReport;
	}

	public void setLstGeneralLedgerReport(List<BalanceSheetReportModel> lstGeneralLedgerReport) {
		this.lstGeneralLedgerReport = lstGeneralLedgerReport;
	}

	public double getTotalOpeningDb() {
		return totalOpeningDb;
	}

	public void setTotalOpeningDb(double totalOpeningDb) {
		this.totalOpeningDb = totalOpeningDb;
	}

	public double getTotalOpeningCr() {
		return totalOpeningCr;
	}

	public void setTotalOpeningCr(double totalOpeningCr) {
		this.totalOpeningCr = totalOpeningCr;
	}

	public double getTotalOpeningNet() {
		return totalOpeningNet;
	}

	public void setTotalOpeningNet(double totalOpeningNet) {
		this.totalOpeningNet = totalOpeningNet;
	}

	public double getTotalMovementDb() {
		return totalMovementDb;
	}

	public void setTotalMovementDb(double totalMovementDb) {
		this.totalMovementDb = totalMovementDb;
	}

	public double getTotalMovementCr() {
		return totalMovementCr;
	}

	public void setTotalMovementCr(double totalMovementCr) {
		this.totalMovementCr = totalMovementCr;
	}

	public double getTotalMovementNet() {
		return totalMovementNet;
	}

	public void setTotalMovementNet(double totalMovementNet) {
		this.totalMovementNet = totalMovementNet;
	}

	public double getTotalBalanceDb() {
		return totalBalanceDb;
	}

	public void setTotalBalanceDb(double totalBalanceDb) {
		this.totalBalanceDb = totalBalanceDb;
	}

	public double getTotalBalanceCr() {
		return totalBalanceCr;
	}

	public void setTotalBalanceCr(double totalBalanceCr) {
		this.totalBalanceCr = totalBalanceCr;
	}

	public double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}
}
