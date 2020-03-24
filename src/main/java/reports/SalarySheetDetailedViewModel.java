package reports;

import hr.HRData;
import hr.model.CompanyModel;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import model.EmployeeModel;
import model.ProjectModel;
import model.ProjectReportModel;
import model.SalaryMasterModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.exporter.Interceptor;
import org.zkoss.exporter.RowRenderer;
import org.zkoss.exporter.excel.ExcelExporter;
import org.zkoss.exporter.pdf.FontFactory;
import org.zkoss.exporter.pdf.PdfExporter;
import org.zkoss.exporter.pdf.PdfPCellFactory;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import setup.users.WebusersModel;
import timesheet.TimeSheetData;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class SalarySheetDetailedViewModel {
	
	private Logger logger = Logger.getLogger(this.getClass());
	HRData hrdata=new HRData();
	TimeSheetData data=new TimeSheetData();
	
	SalarySheetDetailedData dataSalary=new SalarySheetDetailedData();
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstMonths;
	private List<String> lstToMonths;
	private String selectedMonth;
	private List<String> lstYears;
	private List<String> lstToYears;
	private String selectedYear;
	private String selectedToYear;
	private List<String> lstSattus;
	private String selectedStatus;
	
	private int selectedPeriod;
	private int selectedFromMonth;
	private int selectedToMonth;

	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
	
	private List<SalaryMasterModel> lstEmployeeHistory;
	
	
	private List<String> columns;
	
	private List<List<String>> gridDataAll;
	
	private int supervisorID;

	List<OverTimeModel> overTimeColumn=new ArrayList<OverTimeModel>();
	HashSet<String> allowanceHeadder = new HashSet<String>();
	HashSet<String> additionHeadder = new HashSet<String>();
	HashSet<String> diductionHeader = new HashSet<String>();
	HashSet<String> overTimeHeadder = new HashSet<String>();
	
	public SalarySheetDetailedViewModel()
	{
		try
		{
			int defaultCompanyId=0;
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			
			supervisorID=dbUser.getSupervisor();
			
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>0 && defaultCompanyId==0)		
			selectedCompany=lstComapnies.get(0);
			
			/*lstMonths=new ArrayList<String>();
			lstMonths.add("All");
			for (int i = 1; i < 13; i++) 
			{
			lstMonths.add(String.valueOf(i));	
			}
			selectedMonth=lstMonths.get(0);*/
			
			Calendar c = Calendar.getInstance();
			lstYears=new ArrayList<String>();
			lstToYears=new ArrayList<String>();
			for(int i=c.get(Calendar.YEAR);i>1975;i--)
			{
				lstYears.add(String.valueOf(i));
				lstToYears.add(String.valueOf(i));
			}
			selectedYear=lstYears.get(0);
			selectedToYear=lstToYears.get(0);
			
			/*lstProject=data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID);
			if(lstProject.size()>0)
				selectedProject=lstProject.get(0);*/
					
			
			lstCompEmployees=hrdata.getEmployeeList(selectedCompany.getCompKey(),"All","A",supervisorID);
			selectedCompEmployee=lstCompEmployees.get(0);	
			
			
			selectedPeriod=1;
			fillPeriods();
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in SalarySheetDetailedViewModel ----> init", ex);			
		}
	}
	
	
	void makeheadders(int comapnyId,int month)
	{
		columns=new ArrayList<String>();
		columns.add("EMP No.");
		columns.add("Name");
		columns.add("Department");
		columns.add("Position");
		columns.add("Bank Name");
		columns.add("Branch Name");
		columns.add("Account Number");
		columns.add("Basic Sal.");
		for(String allowanceModel:allowanceHeadder)
		{
			columns.add(""+allowanceModel);
		}
		columns.add("Total Allowance");
		columns.add("Days");
		columns.add("Total Work Units");
		for(OverTimeModel overTimeModel:overTimeColumn)
		{
			columns.add("OT Hrs "+overTimeModel.getOvertimeRate());
		}
		for(OverTimeModel overTimeModel:overTimeColumn)
		{
			columns.add("OT Amount "+overTimeModel.getOvertimeRate());
		}
		if(overTimeColumn.size()>0 && overTimeColumn!=null)
		{
		columns.add("OT Total.");
		columns.add("Sub Total");
		}
		columns.add("Loan");
		for(String additionModel:additionHeadder)
		{
			columns.add(additionModel+" Pay On");
			columns.add(additionModel+" Value");
			columns.add(additionModel+" Amount");
			
		}
		if(additionHeadder!=null && additionHeadder.size()>0)
		columns.add("Total  Additions ");
		
		for(String diductionModel:diductionHeader)
		{
			columns.add(diductionModel+" Pay On");
			columns.add(diductionModel+" Value");
			columns.add(diductionModel+" Amount");
			
		}
		if(diductionHeader!=null && diductionHeader.size()>0)
		columns.add("Total Deduction ");
		
		columns.add("Net To Pay");
		//columns.add("Currancy");
		//columns.add("Exchange Rate");
		//columns.add("Net To Pay -DHS");
		columns.add("Paid Amount");
		columns.add("Balance");
		columns.add("Salary Status");
		//columns.add("Balance-DHS");
		 
	}
	
	
	private void fillPeriods()
	{
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		lstMonths=months;
		lstToMonths=months;
		selectedFromMonth=0;
		selectedToMonth=0;
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
		selectedFromMonth=Integer.valueOf(month);
		selectedToMonth=Integer.valueOf(month);//lstMonths.get(0);
		
		lstSattus=new ArrayList<String>();
		lstSattus.add("All");
		lstSattus.add("Created");
		lstSattus.add("Approved");
		lstSattus.add("Paid");
		selectedStatus=lstSattus.get(0);
		
	}
	
	
	
	public void populateGridWithCalculation()
	{
		
		 gridDataAll=new ArrayList<List<String>>();
		 allowanceHeadder = new HashSet<String>();
		 additionHeadder = new HashSet<String>();
		 diductionHeader = new HashSet<String>();
		 overTimeHeadder = new HashSet<String>();
		 double totBasicSal=0.00,totAllowance=0.00,totalworkingHours=0.00,totalWordkingDays=0.0,totalAddition=0.00,totalDiductiuon=0.00,totalOvetTime=0.00,totsubTotal=0.00;
		 double totLoan=0.00,totNettoPay=0.00,totPaidAmmount=0.00,totBalance=0.00;
		 for(SalaryMasterModel masterModel1:lstEmployeeHistory)
		 {
				 for(AllowanceModel model:masterModel1.getAllowanceModels())
				 {
					 allowanceHeadder.add(model.getAllowanceName());
				 }
				  for(AdditionModel model:masterModel1.getAdditionModels())
				 {
					 additionHeadder.add(model.getDescription());
				 }
				 for(DiductionModel model:masterModel1.getDiductionModels())
				 {
					 diductionHeader.add(model.getDescription());
				 }
		 }
		 for(OverTimeModel model:overTimeColumn)
		 {
			 overTimeHeadder.add(""+model.getOvertimeRate());
		 }
		 for(SalaryMasterModel masterModel:lstEmployeeHistory)
		 {
			 List<String> gridData=new ArrayList<String>();
			 gridData.add(masterModel.getEmpNo());
			 gridData.add(masterModel.getEnglishName());
			 gridData.add(masterModel.getDepartment());
			 gridData.add(masterModel.getPostion());
			 gridData.add(""+masterModel.getBankName());
			 gridData.add(""+masterModel.getBranchName());
			 gridData.add(masterModel.getAccountNumber());
			 gridData.add(""+masterModel.getBasicSalary());
			 totBasicSal=totBasicSal+masterModel.getBasicSalary();
			 for(String modelCol:allowanceHeadder)
			 {
				 boolean flag=false;
				 if(masterModel.getAllowanceModels()!=null && masterModel.getAllowanceModels().size()>0)
				 {
					 for(AllowanceModel model:masterModel.getAllowanceModels())
					 {
						 if(model.getAllowanceName().equalsIgnoreCase(modelCol))
						 {
							 gridData.add(""+model.getAmount());
							 flag=true;
						 }
					 }
					 if(flag==false)
					 {
						 gridData.add("0.00");
					 }
				 }
				 else
				 {
					 gridData.add("0.00");
				 }
				 
			 }
			 gridData.add(""+masterModel.getTotalAllowance());
			 gridData.add(""+masterModel.getWorkingDays());
			 gridData.add(""+masterModel.getTotalworkHours());
			 totAllowance=totAllowance+masterModel.getTotalAllowance();
			 totalworkingHours=totalworkingHours+masterModel.getTotalworkHours();
			 totalWordkingDays=totalWordkingDays+masterModel.getWorkingDays();
			 //for rate
			 for(OverTimeModel modelCol:overTimeColumn)
			 {
				 boolean flag=false;
				 	if(masterModel.getOverTimeModels()!=null && masterModel.getOverTimeModels().size()>0)
				 	{
					 for(OverTimeModel model:masterModel.getOverTimeModels())
					 {
						String headder=""+model.getOvertimeRate();
					 	if(headder.equalsIgnoreCase(""+modelCol.getOvertimeRate()))
					 	{
					 		gridData.add(""+model.getOtHours());
					 		flag=true;
					 	}
				 	 }
					 if(flag==false)
					 {
						 gridData.add("");
					 }
					
				 }
				 else
				 {
					 gridData.add("");
				 }
				
			 }
			 for(OverTimeModel modelCol:overTimeColumn)
			 {
				 boolean flag=false;
				 	if(masterModel.getOverTimeModels()!=null && masterModel.getOverTimeModels().size()>0)
				 	{
				 		//for amount
						 for(OverTimeModel model:masterModel.getOverTimeModels())
					 	 {
							 String headder=""+model.getOvertimeRate();
							 if(headder.equalsIgnoreCase(""+modelCol.getOvertimeRate()))
							 	{
					 				gridData.add(""+model.getAmount());
					 				flag=true;
							 	}
							
						}
						 if(flag==false)
						 {
							 gridData.add("");
						 }
				 	}
				 	else
					{
						 gridData.add("");
					}
			 }
			 if(overTimeColumn!=null || overTimeColumn.size()>0)
			 {
				 gridData.add(""+masterModel.getTotalOT());
				 double tot=masterModel.getBasicSalary()+ masterModel.getTotalAllowance() + masterModel.getTotalOT();
				 totsubTotal=totsubTotal+tot;
				 gridData.add(""+Double.parseDouble(new DecimalFormat("#.##").format(tot)));
				 totalOvetTime=totalOvetTime+masterModel.getTotalOT();
				 
			 }
			 gridData.add(""+masterModel.getTotalLoan());
			 totLoan=totLoan+masterModel.getTotalLoan();
			 for(String modelCol:additionHeadder)
			 {
				 if(masterModel.getAdditionModels()!=null && masterModel.getAdditionModels().size()>0)
				 {
					 boolean flag=false;
					 	for(AdditionModel model:masterModel.getAdditionModels())
					 	{
					 		if(modelCol.equalsIgnoreCase(model.getDescription()))
					 		{
					 			flag=true;
					 			if(model.getAhValueFlag().equalsIgnoreCase("A"))
					 			{
					 				gridData.add("Amount");
					 			}
					 			else if(model.getAhValueFlag().equalsIgnoreCase("M"))
					 			{	
					 				gridData.add("Month");
					 			}
					 			else if(model.getAhValueFlag().equalsIgnoreCase("D"))
					 			{
					 				gridData.add("Days");
					 			}
					 			else
					 			{
					 				gridData.add(""+model.getAhValueFlag());
					 			}
					 			gridData.add(""+model.getAhValue());
					 			gridData.add(""+model.getAdAmount());
					 			
					 		}
					 	}
					 	if(flag==false)
						{
					 		 gridData.add(""); 
							 gridData.add("");
					 		 gridData.add("");
						}
					 	
				 }
				 else
				 {
					 gridData.add(""); 
					 gridData.add("");
			 		 gridData.add("");
				 }
			 }
			 if(additionHeadder!=null && additionHeadder.size()>0)
			 gridData.add(""+masterModel.getTotalAddition());
			 totalAddition=totalAddition+masterModel.getTotalAddition();
			 for(String modelCol:diductionHeader)
			 {
				 if(masterModel.getDiductionModels()!=null && masterModel.getDiductionModels().size()>0)
				 {
					boolean flag=false;
				 	for(DiductionModel model:masterModel.getDiductionModels())
				 	{
				 		
				 		if(modelCol.equalsIgnoreCase(model.getDescription()))
				 		{
				 			flag=true;
				 			if(model.getAhValueFlag().equalsIgnoreCase("A"))
				 			{
				 				gridData.add("Amount");
				 			}
				 			else if(model.getAhValueFlag().equalsIgnoreCase("M"))
				 			{
				 				gridData.add("Month");
				 			}
				 			else if(model.getAhValueFlag().equalsIgnoreCase("D"))
				 			{
				 				gridData.add("Days");
				 			}
				 			else
				 			{
				 				gridData.add(""+model.getAhValueFlag());
				 			}
				 			gridData.add(""+model.getAhValue());
				 			gridData.add(""+model.getAdAmount());
				 			
				 		}
				 	}
				 	if(flag==false)
					{
				 		 gridData.add(""); 
						 gridData.add("");
				 		 gridData.add("");
					}
				 }
				 else
				 {
					 gridData.add("");
					 gridData.add(""); 
					 gridData.add("");
				 }
			 }
			 if(diductionHeader!=null && diductionHeader.size()>0)
			 gridData.add(""+masterModel.getTotalDeduction());
			 totalDiductiuon=totalDiductiuon+masterModel.getTotalDeduction();
			 gridData.add(""+masterModel.getNetToPay());
			 totNettoPay=totNettoPay+masterModel.getNetToPay();
			// gridData.add(""+masterModel.getCurrancy());
			// gridData.add(""+masterModel.getExchnageRate());
			 //gridData.add(""+masterModel.getNetpayInDhrms());
			 gridData.add(""+masterModel.getPaidAmount());
			 totPaidAmmount=totPaidAmmount+masterModel.getPaidAmount();
			 gridData.add(""+masterModel.getBalance());
			 totBalance=totBalance+masterModel.getBalance();
			// gridData.add(""+masterModel.getBalanceInDhr());
			 gridData.add(""+masterModel.getSalaryStatus());
			 gridDataAll.add(gridData) ;
		 }
		if(lstEmployeeHistory.size()>0)
		{
		 makeheadders(selectedCompany.getCompKey(),selectedToMonth+1);
		 Double.parseDouble(new DecimalFormat("#.##").format(totBasicSal));
		 Double.parseDouble(new DecimalFormat("#.##").format(totAllowance));
		 Double.parseDouble(new DecimalFormat("#.##").format(totalworkingHours));
		 Double.parseDouble(new DecimalFormat("#.##").format(totalWordkingDays));
		 Double.parseDouble(new DecimalFormat("#.##").format(totalAddition));
		 Double.parseDouble(new DecimalFormat("#.##").format(totalDiductiuon));
		 Double.parseDouble(new DecimalFormat("#.##").format(totalOvetTime));
		 Double.parseDouble(new DecimalFormat("#.##").format(totLoan));
		 Double.parseDouble(new DecimalFormat("#.##").format(totNettoPay));
		 Double.parseDouble(new DecimalFormat("#.##").format(totPaidAmmount));
		 Double.parseDouble(new DecimalFormat("#.##").format(totBalance));
		 
		 List<String> gridTotalFooter=null;
		 int i=0;
		 for(i=0;i<2;i++)
		 {
			 gridTotalFooter=new ArrayList<String>();
			 gridTotalFooter.add("");
			 gridDataAll.add(gridTotalFooter) ;
		 }
		 gridTotalFooter=new ArrayList<String>();
		 gridTotalFooter.add("Over All Total : ");
		 gridTotalFooter.add("Basic Salary : "+new DecimalFormat("#.##").format(totBasicSal));
		 gridTotalFooter.add("Allowances : "+new DecimalFormat("#.##").format(totAllowance));
		 gridTotalFooter.add("Working Days/Hours : "+new DecimalFormat("#.##").format(totalWordkingDays));
		 gridTotalFooter.add("Working Hours : "+new DecimalFormat("#.##").format(totalworkingHours));
		 gridTotalFooter.add("Over Time : "+new DecimalFormat("#.##").format(totalOvetTime));
		 gridTotalFooter.add("Sub Total : "+Double.parseDouble(new DecimalFormat("#.##").format(totsubTotal)));
		 gridTotalFooter.add("Loan : "+new DecimalFormat("#.##").format(totLoan));
		 gridTotalFooter.add("Addition : "+new DecimalFormat("#.##").format(totalAddition));
		 gridTotalFooter.add("Diduction : "+new DecimalFormat("#.##").format(totalDiductiuon));
		 gridTotalFooter.add("Net To Pay : "+new DecimalFormat("#.##").format(totNettoPay));
		 gridTotalFooter.add("Paid Amount : "+new DecimalFormat("#.##").format(totPaidAmmount));
		 gridTotalFooter.add("Balance : "+new DecimalFormat("#.##").format(totBalance));
		 gridDataAll.add(gridTotalFooter) ;
		}
		
	}
	
	
	
	@Command
	 @NotifyChange({"lstEmployeeHistory","columns","gridDataAll"})
	 public void searchCommand()
	 {
		try
		{
			// overTimeColumn=new ArrayList<OverTimeModel>();
			 overTimeColumn=dataSalary.retriveOT(selectedCompany.getCompKey());
			 lstEmployeeHistory= dataSalary.retriveTheMainreportData(selectedCompany.getCompKey(),selectedToMonth+1,Integer.parseInt(selectedToYear),selectedStatus,selectedCompEmployee.getEmployeeKey(),"",supervisorID);
			 populateGridWithCalculation();
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in SalarySheetDetailedViewModel ----> searchCommand", ex);			
		}
	 }
	
	 @Command
	 public void filterCommand()
	 {
		 Map<String,Object> arg = new HashMap<String,Object>();
		 arg.put("compKey", selectedCompany.getCompKey());
		 arg.put("type", "T");
		 Executions.createComponents("/timesheet/employeefilter.zul", null,arg);
		 
	 }
	 
	 @GlobalCommand 
	 @NotifyChange({"lstEmployeeHistory","columns","gridDataAll"})
	  public void filterWindowClose(@BindingParam("myData")String empKeys)
	  {		
		 try
		  {
			if(!empKeys.equals(""))
			{
				 overTimeColumn=new ArrayList<OverTimeModel>();
				 overTimeColumn=dataSalary.retriveOT(selectedCompany.getCompKey());
				 lstEmployeeHistory= dataSalary.retriveTheMainreportData(selectedCompany.getCompKey(),selectedToMonth+1,Integer.parseInt(selectedToYear),selectedStatus,0,empKeys,supervisorID);
				 populateGridWithCalculation();
			}
		  }
		 catch (Exception ex)
			{	
			logger.error("ERROR in SalarySheetDetailedViewModel ----> filterWindowClose", ex);			
			}
	  }
	 
	 @Command
	@NotifyChange({"lstEmployeeHistory","columns","gridDataAll"})
	public void clearCommand()
	{
		 lstEmployeeHistory=new ArrayList<SalaryMasterModel>();
		 columns=new ArrayList<String>();
		 gridDataAll=new ArrayList<List<String>>();
	}
	 
	 @Command
	public void exportCommand(@BindingParam("ref") Listbox grid) throws Exception 
	{			
		try
		{
	    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		final PdfExporter exporter = new PdfExporter();
		final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
		final FontFactory fontFactory = exporter.getFontFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final String title="Project Report for period " + months.get(selectedFromMonth) + "/" + selectedYear + " To " + months.get(selectedToMonth) + "/" + selectedToYear;
		String[] tsHeaders;
		tsHeaders = new String[]{"Project Code.","Project Name", "TimeSheet Month", "TimeSheet Year", "Work With Pay","Absence With Pay","Holidays With Pay","Sick With Pay","Leave"};
		final String[] headers=tsHeaders;
//		exporter.setInterceptor(new Interceptor <PdfPTable> () {
//			@Override
//			public void beforeRendering(PdfPTable table) {
//
//				PdfPCell cellTitle = exporter.getPdfPCellFactory().getHeaderCell();
//				Font fontTitle = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
//				cellTitle.setPhrase(new Phrase(title, fontTitle));
//				cellTitle.setColspan(12);
//				cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(cellTitle);
//				table.completeRow();
//
//				for (int i = 0; i < headers.length; i++) {
//					String header = headers[i];
//					Font font = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
//
//					PdfPCell cell = exporter.getPdfPCellFactory().getHeaderCell();
//					cell.setPhrase(new Phrase(header, font));
//					if ("Units".equals(header) || "Total".equals(header)) {
//						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//					}
//
//					table.addCell(cell);
//				}
//				table.completeRow();
//			}
//
//			@Override
//			public void afterRendering(PdfPTable table) {
//			}
//		});
		
//		 exporter.export(headers.length, columns, new RowRenderer<PdfPTable, String>() {
//		 @Override
//		 public void render(PdfPTable table, String item, boolean isOddRow)
//		 {
//			 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
//			 PdfPCell cell = cellFactory.getCell(isOddRow);
//			 cell.setPhrase(new Phrase(item, font));
//			 table.addCell(cell);
//			table.completeRow();
//		 }
//
//
//		 }, out);
		
		AMedia amedia = new AMedia("SalarySheet.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
		}
		
		 catch (Exception ex)
			{	
			logger.error("ERROR in SalarySheetDetailedViewModel ----> exportCommand", ex);			
			}
	}
	 @Command
	 public void exportToExcel(@BindingParam("ref") Listbox grid)
	 {
		 try
		 {
			 	ByteArrayOutputStream out = new ByteArrayOutputStream();

				ExcelExporter exporter = new ExcelExporter();
				exporter.export(grid, out);

				AMedia amedia = new AMedia("SalarySheet.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
			/*	
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			 final ExcelExporter exporter = new ExcelExporter();
			 String[] tsHeaders;
			 tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","NO.of Days","Present Days","Off Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
			 final String[] headers=tsHeaders;
			
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
				    }				    				   
				});
			 
			   	exporter.export(headers.length, lstEmployeeHistory, new RowRenderer<Row, TimeSheetDataModel>() {
				@Override
				public void render(Row table, TimeSheetDataModel item, boolean isOddRow) 
					 {
					 	ExportContext context = exporter.getExportContext();
				        XSSFSheet sheet = context.getSheet();				        
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEmpNo());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnFullName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getProjectName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTsMonthName());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTsYear());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDayNo());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getPresentDays());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getHolidays());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt1());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt2());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt3());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTsStatus());
					 }
					 
			    }, out);
			 
			   	AMedia amedia = new AMedia("TimesheetReport.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
				*/
		 }
		 catch (Exception ex)
		 {	
		  logger.error("ERROR in SalarySheetDetailedViewModel ----> exportToExcel", ex);			
		 }
	 }
	
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}
	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}
	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}
	@NotifyChange({"lstProject","lstCompEmployees"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;				
	//	lstProject=null;		
		//lstProject=new ListModelList<ProjectModel>(data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID));	
		lstCompEmployees=hrdata.getEmployeeList(selectedCompany.getCompKey(),"All","A",supervisorID);
		if(lstCompEmployees.size()>0)
		selectedCompEmployee=lstCompEmployees.get(0);
	}
	
	
	public List<String> getLstMonths() {
		return lstMonths;
	}
	public void setLstMonths(List<String> lstMonths) {
		this.lstMonths = lstMonths;
	}
	public String getSelectedMonth() {
		return selectedMonth;
	}
	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
	}
	public List<String> getLstYears() {
		return lstYears;
	}
	public void setLstYears(List<String> lstYears) {
		this.lstYears = lstYears;
	}
	public String getSelectedYear() {
		return selectedYear;
	}
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}


	

	public List<SalaryMasterModel> getLstEmployeeHistory() {
		return lstEmployeeHistory;
	}

	public void setLstEmployeeHistory(List<SalaryMasterModel> lstEmployeeHistory) {
		this.lstEmployeeHistory = lstEmployeeHistory;
	}

	public int getSelectedPeriod() {
		return selectedPeriod;
	}

	public void setSelectedPeriod(int selectedPeriod) {
		this.selectedPeriod = selectedPeriod;
	}

	public int getSelectedFromMonth() {
		return selectedFromMonth;
	}

	public void setSelectedFromMonth(int selectedFromMonth) {
		this.selectedFromMonth = selectedFromMonth;
	}

	public int getSelectedToMonth() {
		return selectedToMonth;
	}

	public void setSelectedToMonth(int selectedToMonth) {
		this.selectedToMonth = selectedToMonth;
	}

	public List<String> getLstToMonths() {
		return lstToMonths;
	}

	public void setLstToMonths(List<String> lstToMonths) {
		this.lstToMonths = lstToMonths;
	}

	public List<String> getLstToYears() {
		return lstToYears;
	}

	public void setLstToYears(List<String> lstToYears) {
		this.lstToYears = lstToYears;
	}

	public String getSelectedToYear() {
		return selectedToYear;
	}

	public void setSelectedToYear(String selectedToYear) {
		this.selectedToYear = selectedToYear;
	}

	public List<EmployeeModel> getLstCompEmployees() {
		return lstCompEmployees;
	}

	public void setLstCompEmployees(List<EmployeeModel> lstCompEmployees) {
		this.lstCompEmployees = lstCompEmployees;
	}

	public EmployeeModel getSelectedCompEmployee() {
		return selectedCompEmployee;
	}

	public void setSelectedCompEmployee(EmployeeModel selectedCompEmployee) {
		this.selectedCompEmployee = selectedCompEmployee;
	}

	/**
	 * @return the columns
	 */
	public List<String> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}


	/**
	 * @return the gridDataAll
	 */
	public List<List<String>> getGridDataAll() {
		return gridDataAll;
	}


	/**
	 * @param gridDataAll the gridDataAll to set
	 */
	public void setGridDataAll(List<List<String>> gridDataAll) {
		this.gridDataAll = gridDataAll;
	}


	/**
	 * @return the lstSattus
	 */
	public List<String> getLstSattus() {
		return lstSattus;
	}


	/**
	 * @param lstSattus the lstSattus to set
	 */
	public void setLstSattus(List<String> lstSattus) {
		this.lstSattus = lstSattus;
	}


	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}


	/**
	 * @param selectedStatus the selectedStatus to set
	 */
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}


	
	

}
