package reports;

import model.ProjectReportModel;
import hr.HRData;
import hr.model.CompanyModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EmployeeModel;
import model.ProjectModel;

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

public class ProjectReportViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	HRData hrdata=new HRData();
	TimeSheetData data=new TimeSheetData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstMonths;
	private List<String> lstToMonths;
	private String selectedMonth;
	private List<String> lstYears;
	private List<String> lstToYears;
	private String selectedYear;
	private String selectedToYear;
	private List<ProjectModel> lstProject;
	private ProjectModel selectedProject;
	
	private int selectedPeriod;
	private int selectedFromMonth;
	private int selectedToMonth;

	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
	
	private List<ProjectReportModel> lstEmployeeHistory;
	private int supervisorID;
	
	public ProjectReportViewModel()
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
			
			lstProject=data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID);
			if(lstProject.size()>0)
				selectedProject=lstProject.get(0);
					
			
			lstCompEmployees=hrdata.getEmployeeList(selectedCompany.getCompKey(),"All","A",supervisorID);
			selectedCompEmployee=lstCompEmployees.get(0);	
			
			
			selectedPeriod=1;
			fillPeriods();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ProjectReportViewModel ----> init", ex);			
		}
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
	}
	
	@Command
	 @NotifyChange({"lstEmployeeHistory"})
	 public void searchCommand()
	 {
		try
		{
			// int month=selectedMonth.equals("All")?0:Integer.parseInt(selectedMonth);			 
			 lstEmployeeHistory= data.getProjectReport(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
					 selectedToMonth+1,Integer.parseInt(selectedToYear),selectedCompEmployee.getEmployeeKey(),"",selectedProject.getProjectKey());
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in ProjectReportViewModel ----> searchCommand", ex);			
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
	 @NotifyChange({"lstEmployeeHistory"})
	  public void filterWindowClose(@BindingParam("myData")String empKeys)
	  {		
		 try
		  {
			if(!empKeys.equals(""))
			{
				 lstEmployeeHistory= data.getProjectReport(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
						 selectedToMonth+1,Integer.parseInt(selectedToYear),0,empKeys,selectedProject.getProjectKey());
			}
		  }
		 catch (Exception ex)
			{	
			logger.error("ERROR in ProjectReportViewModel ----> filterWindowClose", ex);			
			}
	  }
	 
	 @Command
	@NotifyChange({"selectedProject","selectedCompEmployee","lstEmployeeHistory"})
	public void clearCommand()
	{
		 selectedProject=lstProject.get(0);
		 selectedCompEmployee=lstCompEmployees.get(0);
		 lstEmployeeHistory=new ArrayList<ProjectReportModel>();
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
		exporter.setInterceptor(new Interceptor <PdfPTable> () {
			@Override
			public void beforeRendering(PdfPTable table) {
				
				PdfPCell cellTitle = exporter.getPdfPCellFactory().getHeaderCell();
				Font fontTitle = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
				cellTitle.setPhrase(new Phrase(title, fontTitle));
				cellTitle.setColspan(12);
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
				table.completeRow();
			}
			
			@Override
			public void afterRendering(PdfPTable table) {
			}
		});
		
		 exporter.export(headers.length, lstEmployeeHistory, new RowRenderer<PdfPTable, ProjectReportModel>() {
		 @Override
		 public void render(PdfPTable table, ProjectReportModel item, boolean isOddRow) 
		 {
			 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
			 PdfPCell cell = cellFactory.getCell(isOddRow);			
			 cell.setPhrase(new Phrase(item.getProjectCode(), font));
			 table.addCell(cell);
			 
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getProjectName(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getTsMonthDesc(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(Integer.toString(item.getTsYear()), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getWorkWithPay()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getAbsenceWithPay()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getHolidaysWithPay()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getSickWithPay()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getLeave()+"", font));
			table.addCell(cell);
			table.completeRow();
		 }
			
		
		 }, out);
		
		AMedia amedia = new AMedia("ProjectReport.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
		}
		
		 catch (Exception ex)
			{	
			logger.error("ERROR in ProjectReportViewModel ----> exportCommand", ex);			
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

				AMedia amedia = new AMedia("TimesheetReport.xls", "xls", "application/file", out.toByteArray());
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
		  logger.error("ERROR in ProjectReportViewModel ----> exportToExcel", ex);			
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
		lstProject=null;		
		lstProject=new ListModelList<ProjectModel>(data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID));	
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


	public List<ProjectModel> getLstProject() {
		return lstProject;
	}


	public void setLstProject(List<ProjectModel> lstProject) {
		this.lstProject = lstProject;
	}


	public ProjectModel getSelectedProject() {
		return selectedProject;
	}


	public void setSelectedProject(ProjectModel selectedProject) {
		this.selectedProject = selectedProject;
	}

	public List<ProjectReportModel> getLstEmployeeHistory() {
		return lstEmployeeHistory;
	}

	public void setLstEmployeeHistory(List<ProjectReportModel> lstEmployeeHistory) {
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
}
