package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.EmployeeFilter;
import model.EmployeeModel;
import model.ProjectModel;
import model.TimeSheetDataModel;
import model.TimeSheetGridModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import reports.ReportsComaprator;
import reports.ReportsGroupAdapter;
import setup.users.WebusersModel;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class TimesheetGenaratedReport {
	
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
	private boolean adminUser;
	private ReportsGroupAdapter reportGroupModel;
	private String footer;
	
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	
	private int selectedPeriod;
	private int selectedFromMonth;
	private int selectedToMonth;

	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
	
	private List<TimeSheetDataModel> lstEmployeeHistory;
	private int supervisorID;
	
	private int userId;
	
	int menuID=211;
	private MenuModel companyRole;
	
	public TimesheetGenaratedReport()
	{
		try
		{
			int defaultCompanyId=0;
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			userId=dbUser.getUserid();
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>0 && defaultCompanyId==0)		
			selectedCompany=lstComapnies.get(0);
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
				supervisorID=dbUser.getSupervisor();
			}
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
			for(int i=c.get(Calendar.YEAR);i>1999;i--)
			{
				lstYears.add(String.valueOf(i));
				lstToYears.add(String.valueOf(i));
			}
			selectedYear=lstYears.get(0);
			selectedToYear=lstToYears.get(0);
			/*if(supervisorID>0)
			{*/
				lstProject=data.getProjectListBySupervisorID(supervisorID);
				if(lstProject.size()>0)
					selectedProject=lstProject.get(0);
			/*}
			else{
				lstProject=data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID);
				if(lstProject.size()>0)
					selectedProject=lstProject.get(0);
			}
				*/	
			lstCompEmployees=hrdata.getEmployeeList(selectedCompany.getCompKey(),"All","A",supervisorID);
			selectedCompEmployee=lstCompEmployees.get(0);	
			
			
			selectedPeriod=1;
			fillPeriods();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in TimesheetGenaratedReport ----> init", ex);			
		}
	}
	private void getCompanyRolePermessions(int companyRoleId)
	{
		setCompanyRole(new MenuModel());
		
		List<MenuModel> lstRoles= data.getTimeSheetRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				setCompanyRole(item);
				break;
			}
		}
	}
	
	private void fillPeriods()
	{
		List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		lstMonths=months;
		lstToMonths=months;
		selectedFromMonth=0;
		selectedToMonth=0;
	}
	
	@Command
	 @NotifyChange({"lstEmployeeHistory","reportGroupModel","footer"})
	 public void searchCommand()
	 {
		try
		{
			 lstEmployeeHistory= data.getTimeSheetGenerated(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
					 selectedToMonth+1,Integer.parseInt(selectedToYear),selectedCompEmployee.getEmployeeKey(),"",supervisorID,selectedProject.getProjectKey(),userId);
			 reportGroupModel=new ReportsGroupAdapter(lstEmployeeHistory, new ReportsComaprator(), true);
			 footer=" Total No Of Days: " + reportGroupModel.getGroupCount();
			   for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			   {
				   reportGroupModel.removeOpenGroup(i);
			   }
			   
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in TimesheetGenaratedReport ----> searchCommand", ex);			
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
	 @NotifyChange({"lstEmployeeHistory","footer","reportGroupModel"})
	  public void filterWindowClose(@BindingParam("myData")String empKeys)
	  {		
		 try
		  {
			if(!empKeys.equals(""))
			{
				 lstEmployeeHistory= data.getTimeSheetGenerated(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
						 selectedToMonth+1,Integer.parseInt(selectedToYear),0,empKeys,supervisorID,selectedProject.getProjectKey(),userId);
				 reportGroupModel=new ReportsGroupAdapter(lstEmployeeHistory, new ReportsComaprator(), true);
				 footer=" Total No Of Days: " + reportGroupModel.getGroupCount();
				   for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
				   {
					   reportGroupModel.removeOpenGroup(i);
				   }
				
			}
		  }
		 catch (Exception ex)
			{	
			logger.error("ERROR in TimesheetGenaratedReport ----> filterWindowClose", ex);			
			}
	  }
	 
	 
	 @Command
	  @NotifyChange({"reportGroupModel"})
	  public void changeFilter() 
	  {	      
		  if(lstEmployeeHistory==null)
		  {
   		Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
   		return;
		  }
		  
		  List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();
		  for (Iterator<TimeSheetDataModel> i = lstEmployeeHistory.iterator(); i.hasNext();)
			{
			  TimeSheetDataModel tmp=i.next();	
			  if(tmp.getEmpNo().toLowerCase().contains(employeeFilter.getEmployeeNo()) &&
			   tmp.getEnFullName().toLowerCase().startsWith(employeeFilter.getFullName().toLowerCase())	
			    &&
			   tmp.getEnPositionName().toLowerCase().startsWith(employeeFilter.getPosition().toLowerCase())
			    &&
			   tmp.getTsStatus().toLowerCase().startsWith(employeeFilter.getTsStatus().toLowerCase())
			   )
			  {
				  lst.add(tmp);
			  }
				  
			}
		  
		  reportGroupModel=new ReportsGroupAdapter(lst, new ReportsComaprator(), true);
		    for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			  {				
		    	reportGroupModel.removeOpenGroup(i);				
			  }
		  
	  }
	 
	 
	 
	 
	 
	 
	 @Command
	@NotifyChange({"selectedProject","selectedCompEmployee","lstEmployeeHistory","reportGroupModel"})
	public void clearCommand()
	{
		 selectedProject=lstProject.get(0);
		 selectedCompEmployee=lstCompEmployees.get(0);
		 lstEmployeeHistory=new ArrayList<TimeSheetDataModel>();
		 reportGroupModel=new ReportsGroupAdapter(lstEmployeeHistory, new ReportsComaprator(), true);
	}
	 
	 
	 @Command
	public void exportCommand(@BindingParam("ref") Grid grid) throws Exception 
	{			
		try
		{
	    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		final PdfExporter exporter = new PdfExporter();
		final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
		final FontFactory fontFactory = exporter.getFontFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final String title="Report For " + months.get(selectedFromMonth) + "/" + selectedYear + " To " + months.get(selectedToMonth) + "/" + selectedToYear;
		String[] tsHeaders;
		tsHeaders = new String[]{"Emp NO.","Name", "Position", "Date", "Day","Status","Calculate","Unit","Total Hrs/Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total OT"};
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
		
		 exporter.export(headers.length, lstEmployeeHistory, new RowRenderer<PdfPTable, TimeSheetDataModel>() {
		 @Override
		 public void render(PdfPTable table, TimeSheetDataModel item, boolean isOddRow) 
		 {
			 Font font = fontFactory.getFont(FontFactory.FONT_TYPE_CELL);
			 PdfPCell cell = cellFactory.getCell(isOddRow);			
			 cell.setPhrase(new Phrase(item.getEmpNo(), font));
			 table.addCell(cell);
			 
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getEnFullName(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getEnPositionName(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getDayofWeek(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getTsDate()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getDayOrHours()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getTsStatus()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getCalculation()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getDayOrHours()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getUnitNO()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getOt1()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getOt2()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getOt3()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getTotalOverTime()+"",font));
			table.addCell(cell);
			
			table.completeRow();
		 }
			
		
		 }, out);
		
		AMedia amedia = new AMedia("TimesheetReport.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
		}
		
		 catch (Exception ex)
			{	
			logger.error("ERROR in TimesheetGenaratedReport ----> exportCommand", ex);			
			}
	}
	 @Command
	 public void exportToExcel(@BindingParam("ref") Grid grid)
	 {
		 try
		 {
			 
			 if(lstEmployeeHistory==null)
			  {
	  		Messagebox.show("There is are record !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	  		return;
			  }
				
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			 final ExcelExporter exporter = new ExcelExporter();
			 String[] tsHeaders;
			 //tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","NO.of Days","Present Days","Off Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
			 tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","Toatl NO.of Days","Present Days","Holidays","Absent","Sick","Leave","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
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
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getAbsance());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getSick());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getLeave());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt1());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt2());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt3());
				        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTsStatus());
					 }
					 
			    }, out);
			 
			   	AMedia amedia = new AMedia("TimesheetSummaryReport.xls", "xls", "application/file", out.toByteArray());
				Filedownload.save(amedia);
				out.close();
		 }
		 catch (Exception ex)
		 {	
		  logger.error("ERROR in TimesheetGenaratedReport ----> exportToExcel", ex);			
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

	public List<TimeSheetDataModel> getLstEmployeeHistory() {
		return lstEmployeeHistory;
	}

	public void setLstEmployeeHistory(List<TimeSheetDataModel> lstEmployeeHistory) {
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
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	
	
	/**
	 * @return the reportGroupModel
	 */
	public ReportsGroupAdapter getReportGroupModel() {
		return reportGroupModel;
	}
	/**
	 * @param reportGroupModel the reportGroupModel to set
	 */
	public void setReportGroupModel(ReportsGroupAdapter reportGroupModel) {
		this.reportGroupModel = reportGroupModel;
	}
	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}
	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}
	/**
	 * @return the employeeFilter
	 */
	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}
	/**
	 * @param employeeFilter the employeeFilter to set
	 */
	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}
	
	


}
