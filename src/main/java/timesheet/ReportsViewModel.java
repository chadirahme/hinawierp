package timesheet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hr.HRData;
import hr.model.CompanyModel;
import layout.MenuModel;
import model.CustomerModel;
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
import org.zkoss.exporter.GroupRenderer;
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

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class ReportsViewModel 
{
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
	
	private int selectedPeriod;
	private int selectedFromMonth;
	private int selectedToMonth;

	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
	
	private List<EmployeeModel> lstUsers;
	private EmployeeModel selectedUsers;
	
	private List<TimeSheetDataModel> lstEmployeeHistory;
	private List<TimeSheetDataModel> lstAllEmployeeHistory;
	private int supervisorID;
	
	//Detailed report
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	private ReportsGroupAdapter reportGroupModel;
	private String footer;
	private String footerSummary;
	private List<TimeSheetDataModel> lstEmployeeHistoryDeatiled;
	private int userId;
	

	
	int menuID=211;
	private MenuModel companyRole;
	
	public ReportsViewModel()
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
			
			lstProject=data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID);
			if(lstProject.size()>0)
				selectedProject=lstProject.get(0);
			/*
			if(supervisorID>0)
			{
				lstProject=data.getProjectListBySupervisorID(supervisorID);
				if(lstProject.size()>0)
					selectedProject=lstProject.get(0);
			}
			else{
				lstProject=data.getProjectList(selectedCompany.getCompKey(),"All",false,supervisorID);
				if(lstProject.size()>0)
					selectedProject=lstProject.get(0);
			}
				*/	
			
			int month = c.get(Calendar.MONTH);
			selectedFromMonth=month;//lstMonths.get(0);
			selectedToMonth=month;//lstMonths.get(0);
			
			
			lstCompEmployees=hrdata.getEmployeeList(selectedCompany.getCompKey(),"All","A",supervisorID);
			selectedCompEmployee=lstCompEmployees.get(0);	
			
			lstUsers=data.GetUsersList();
			selectedUsers=lstUsers.get(0);
			
			selectedPeriod=1;
			fillPeriods();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReportsViewModel ----> init", ex);			
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
		//selectedFromMonth=0;
		//selectedToMonth=0;
	}
	
	@Command
	 @NotifyChange({"lstEmployeeHistory","reportGroupModel","lstEmployeeHistoryDeatiled","footer","footerSummary"})
	 public void searchCommand()
	 {
		try
		{
			// int month=selectedMonth.equals("All")?0:Integer.parseInt(selectedMonth);			 
			 lstEmployeeHistory= data.getEmployeeTimeSheetHistory(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
					 selectedToMonth+1,Integer.parseInt(selectedToYear),selectedProject.getProjectKey(),selectedCompEmployee.getEmployeeKey(),"",supervisorID,userId);
			 
			 lstAllEmployeeHistory=lstEmployeeHistory;
			 
			 lstEmployeeHistoryDeatiled= data.getTimeSheetGenerated(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
					 selectedToMonth+1,Integer.parseInt(selectedToYear),selectedCompEmployee.getEmployeeKey(),"",supervisorID,selectedProject.getProjectKey(),userId);
			 reportGroupModel=new ReportsGroupAdapter(lstEmployeeHistoryDeatiled, new ReportsComaprator(), true);
			 footer=" Total No Of Employees : " + reportGroupModel.getGroupCount();
			 footerSummary=" Total No Of Employees : " + lstEmployeeHistory.size();
			   for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			   {
				   reportGroupModel.removeOpenGroup(i);
			   }
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in ReportsViewModel ----> searchCommand", ex);			
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
	 @NotifyChange({"lstEmployeeHistory","reportGroupModel","lstEmployeeHistoryDeatiled","footer","footerSummary"})
	  public void filterWindowClose(@BindingParam("myData")String empKeys)
	  {		
		 try
		  {
			if(!empKeys.equals(""))
			{
				 lstEmployeeHistory= data.getEmployeeTimeSheetHistory(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
						 selectedToMonth+1,Integer.parseInt(selectedToYear),selectedProject.getProjectKey(),0,empKeys,supervisorID,userId);
				 
				 lstAllEmployeeHistory=lstEmployeeHistory;
				 footerSummary=" Total No Of Employees : " + lstEmployeeHistory.size();
				 
				 lstEmployeeHistoryDeatiled= data.getTimeSheetGenerated(selectedCompany.getCompKey(),selectedFromMonth+1,Integer.parseInt(selectedYear),
						 selectedToMonth+1,Integer.parseInt(selectedToYear),0,empKeys,supervisorID,selectedProject.getProjectKey(),userId);
				 reportGroupModel=new ReportsGroupAdapter(lstEmployeeHistoryDeatiled, new ReportsComaprator(), true);
				 footer=" Total No Of Employees : " + reportGroupModel.getGroupCount();
				   for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
				   {
					   reportGroupModel.removeOpenGroup(i);
				   }
			}
		  }
		 catch (Exception ex)
			{	
			logger.error("ERROR in ReportsViewModel ----> filterWindowClose", ex);			
			}
	  }
	 
	 
	 @Command
	  @NotifyChange({"lstEmployeeHistory","footerSummary"})
	  public void changeFilterSummery() 
	  {	      
		  if(lstEmployeeHistory==null)
		  {
 		Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
 		return;
		  }
		  
		 
		  lstEmployeeHistory=lstAllEmployeeHistory;
		  
		  List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();
		  for (Iterator<TimeSheetDataModel> i = lstEmployeeHistory.iterator(); i.hasNext();)
			{
			  TimeSheetDataModel tmp=i.next();	
			  if(tmp.getEmpNo().toLowerCase().contains(employeeFilter.getEmployeeNo()) &&
			   tmp.getEnFullName().toLowerCase().startsWith(employeeFilter.getFullName().toLowerCase())	
			    &&
			   tmp.getProjectName().toLowerCase().startsWith(employeeFilter.getProjectName().toLowerCase())
			  /*  &&
			   tmp.getTsStatus().toLowerCase().startsWith(employeeFilter.getTsStatus().toLowerCase())*/
			   )
			  {
				  lst.add(tmp);
			  }
				  
			}
		  lstEmployeeHistory=lst;
		  footerSummary=" Total No Of Employees : " + lstEmployeeHistory.size();
		  
	  }
	 
	 @Command
	  @NotifyChange({"reportGroupModel","footer"})
	  public void changeFilter() 
	  {	      
		  if(lstEmployeeHistoryDeatiled==null)
		  {
  		Messagebox.show("There is no record to search in !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
  		return;
		  }
		  
		  List<TimeSheetDataModel> lst=new ArrayList<TimeSheetDataModel>();
		  for (Iterator<TimeSheetDataModel> i = lstEmployeeHistoryDeatiled.iterator(); i.hasNext();)
			{
			  TimeSheetDataModel tmp=i.next();	
			  if(tmp.getEmpNo().toLowerCase().contains(employeeFilter.getEmployeeNo()) &&
			   tmp.getEnFullName().toLowerCase().startsWith(employeeFilter.getFullName().toLowerCase())	
			    &&
			   tmp.getEnPositionName().toLowerCase().startsWith(employeeFilter.getPosition().toLowerCase())
			    &&
			   tmp.getTsStatus().toLowerCase().startsWith(employeeFilter.getTsStatus().toLowerCase())&&
			   tmp.getProjectName().toLowerCase().startsWith(employeeFilter.getProjectName().toLowerCase())
			   )
			  {
				  lst.add(tmp);
			  }
				  
			}
		  
		  reportGroupModel=new ReportsGroupAdapter(lst, new ReportsComaprator(), true);
		//  int serailNo=0;
		    for (int i = 0; i < reportGroupModel.getGroupCount(); i++)
			  {			
		//    	serailNo=serailNo+1;
		    	reportGroupModel.removeOpenGroup(i);				
			  }
		    footer=" Total No Of Employees: " + reportGroupModel.getGroupCount();
	  }
	 
	 
	 
	 @Command
	@NotifyChange({"selectedProject","selectedCompEmployee","lstEmployeeHistory","reportGroupModel","lstEmployeeHistoryDeatiled"})
	public void clearCommand()
	{
		 selectedProject=lstProject.get(0);
		 selectedCompEmployee=lstCompEmployees.get(0);
		 lstEmployeeHistory=new ArrayList<TimeSheetDataModel>();
		 
		 lstEmployeeHistoryDeatiled=new ArrayList<TimeSheetDataModel>();
		 reportGroupModel=new ReportsGroupAdapter(lstEmployeeHistoryDeatiled, new ReportsComaprator(), true);
	}
	 
	 
	 //get Mobile Attendance Report
	@Command
	@NotifyChange({"lstEmployeeHistory"})
	public void getMobileAttendanceCommand()
	{
		lstEmployeeHistory=data.getMobileAttendance(selectedUsers.getEmployeeKey());
	}
	@Command
	public void viewMapCommand(@BindingParam("row") TimeSheetDataModel row) 
	{
		try 
		{
			 Map<String,Object> arg = new HashMap<String,Object>();
			 arg.put("compKey", selectedCompany.getCompKey());
			 arg.put("type", "T");
			 Executions.createComponents("/timesheet/map.zul", null,arg);			 		
			
		} catch (Exception ex) {
			logger.error(
					"ERROR in TimeSheetReportsViewModel ----> viewMapCommand", ex);
		}
	}
	 
	 
	 @Command
	public void exportCommand(@BindingParam("ref") Listbox grid) throws Exception 
	{			
		try
		{
			
			if(lstEmployeeHistory==null)
			  {
	  		Messagebox.show("There are no record !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
	  		return;
			  }
	    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		final PdfExporter exporter = new PdfExporter();
		final PdfPCellFactory cellFactory = exporter.getPdfPCellFactory();
		final FontFactory fontFactory = exporter.getFontFactory();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final String title="Report for period " + months.get(selectedFromMonth) + "/" + selectedYear + " To " + months.get(selectedToMonth) + "/" + selectedToYear;
		String[] tsHeaders;
		tsHeaders = new String[]{"Emp NO.","Name", "Project", "Month", "Year","Toatl NO.of Days","Present Days","Holidays","Absent","Sick","Leave","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Status"};
		final String[] headers=tsHeaders;
		exporter.setInterceptor(new Interceptor <PdfPTable> () {
			@Override
			public void beforeRendering(PdfPTable table) {
				
				PdfPCell cellTitle = exporter.getPdfPCellFactory().getHeaderCell();
				Font fontTitle = exporter.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
				cellTitle.setPhrase(new Phrase(title, fontTitle));
				cellTitle.setColspan(15);
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
			cell.setPhrase(new Phrase(item.getProjectName(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getTsMonthName(), font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getTsYear()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getDayNo()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getPresentDays()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getHolidays()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getAbsance()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getSick()+"", font));
			table.addCell(cell);
			
			cell = cellFactory.getCell(isOddRow);
			cell.setPhrase(new Phrase(item.getLeave()+"", font));
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
			cell.setPhrase(new Phrase(item.getTsStatus(), font));
			table.addCell(cell);
			
			table.completeRow();
		 }
			
		
		 }, out);
		
		AMedia amedia = new AMedia("TimesheetSummaryReport.pdf", "pdf", "application/pdf", out.toByteArray());
		Filedownload.save(amedia);
		out.close();
		}
		
		 catch (Exception ex)
			{	
			logger.error("ERROR in ReportsViewModel ----> exportCommand", ex);			
			}
	}
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
		  logger.error("ERROR in ReportsViewModel ----> exportToExcel", ex);			
		 }
	 }
	 
	 
	 
	 
	 
	 @Command
		public void exportCommandDeatiled(@BindingParam("ref") Grid grid) throws Exception 
		{			
			try
			{
				
				if(lstEmployeeHistoryDeatiled==null)
				  {
		  		Messagebox.show("There are no record !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
		  		return;
				  }
		    List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
			final PdfExporter exporterDeatiled = new PdfExporter();
			final PdfPCellFactory cellFactory = exporterDeatiled.getPdfPCellFactory();
			final FontFactory fontFactory = exporterDeatiled.getFontFactory();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			final String title="Report For " + months.get(selectedFromMonth) + "/" + selectedYear + " To " + months.get(selectedToMonth) + "/" + selectedToYear;
			String[] tsHeaders;
			tsHeaders = new String[]{"Emp NO.","Name", "Position","Project","Date", "Day","Status","Calculate","Unit","Total Hrs/Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total OT"};
			final String[] headers=tsHeaders;
			exporterDeatiled.setInterceptor(new Interceptor <PdfPTable> () {
				@Override
				public void beforeRendering(PdfPTable table) {
					
					PdfPCell cellTitle = exporterDeatiled.getPdfPCellFactory().getHeaderCell();
					Font fontTitle = exporterDeatiled.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
					cellTitle.setPhrase(new Phrase(title, fontTitle));
					cellTitle.setColspan(18);
					cellTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
					//cellTitle.setPadding(5.0f);
					table.addCell(cellTitle);
					/*float [] widths=new float[]{200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f,200f};
					try {
						table.setWidths(widths);
						table.setWidthPercentage(30);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
*/					table.completeRow();
					
					for (int i = 0; i < headers.length; i++) {
						String header = headers[i];
						Font font = exporterDeatiled.getFontFactory().getFont(FontFactory.FONT_TYPE_HEADER);
						
						PdfPCell cell = exporterDeatiled.getPdfPCellFactory().getHeaderCell();
						cell.setPhrase(new Phrase(header, font));
						if ("Units".equals(header) || "Total".equals(header)) {
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						}
						
						table.addCell(cell);
					}
					//table.setWidthPercentage(130);
					/*try {
						table.setWidths(widths);
						table.setWidthPercentage(100);
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					table.completeRow();
				}
				
				@Override
				public void afterRendering(PdfPTable table) {
				}
			});
			
			exporterDeatiled.export(headers.length, lstEmployeeHistoryDeatiled, new RowRenderer<PdfPTable, TimeSheetDataModel>() {
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
				cell.setPhrase(new Phrase(item.getProjectName(), font));
				table.addCell(cell);
				
				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getTimesheetDate()+"", font));
				table.addCell(cell);
				
				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getDayofWeek(), font));
				table.addCell(cell);
				
				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getTsStatus()+"", font));
				table.addCell(cell);
				
				cell = cellFactory.getCell(isOddRow);
				cell.setPhrase(new Phrase(item.getCalcFlag()+"", font));
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
			
			AMedia amedia = new AMedia("TimesheetDetailedReport.pdf", "pdf", "application/pdf", out.toByteArray());
			Filedownload.save(amedia);
			out.close();
			}
			
			 catch (Exception ex)
				{	
				logger.error("ERROR in ReportsViewModel ----> exportCommand", ex);			
				}
		}
		 @Command
		 public void exportToExcelDeatiled(@BindingParam("ref") Grid grid)
		 {
			 try
			 {
				 	//ByteArrayOutputStream out = new ByteArrayOutputStream();

					//ExcelExporter exporter = new ExcelExporter();
					//exporter.export(grid, out);

					//AMedia amedia = new AMedia("TimesheetReport.xls", "xls", "application/file", out.toByteArray());
					//Filedownload.save(amedia);
					//out.close();
				 
				 if(lstEmployeeHistoryDeatiled==null)
				  {
		  		Messagebox.show("There are no record !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
		  		return;
				  }
					
				 ByteArrayOutputStream out = new ByteArrayOutputStream();
				 List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
				 final ExcelExporter exporter = new ExcelExporter();
				 String[] tsHeaders;
				 tsHeaders = new String[]{"Emp NO.","Name","Position","Project", "Date", "Day","Status","Calculate","Unit","Total Hrs/Days","OT Unit 1.25","OT Unit 1.5","OT Unit 2","Total OT"};
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
				 
				   	exporter.export(headers.length, lstEmployeeHistoryDeatiled, new RowRenderer<Row, TimeSheetDataModel>() {
					@Override
					public void render(Row table, TimeSheetDataModel item, boolean isOddRow) 
						 {
						 	ExportContext context = exporter.getExportContext();
					        XSSFSheet sheet = context.getSheet();				        
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEmpNo());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnFullName());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getEnPositionName());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getProjectName());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTimesheetDate());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDayofWeek());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTsStatus());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getCalcFlag());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getDayOrHours());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getUnitNO());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt1());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt2());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getOt3());
					        exporter.getOrCreateCell(context.moveToNextCell(), sheet).setCellValue(item.getTotalOverTime());
						 }
						 
				    }, out);
				 
				   	AMedia amedia = new AMedia("TimesheetDetailedReport.xls", "xls", "application/file", out.toByteArray());
					Filedownload.save(amedia);
					out.close();
					
			 }
			 catch (Exception ex)
			 {	
			  logger.error("ERROR in ReportsViewModel ----> exportToExcel", ex);			
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
	 * @return the lstEmployeeHistoryDeatiled
	 */
	public List<TimeSheetDataModel> getLstEmployeeHistoryDeatiled() {
		return lstEmployeeHistoryDeatiled;
	}
	/**
	 * @param lstEmployeeHistoryDeatiled the lstEmployeeHistoryDeatiled to set
	 */
	public void setLstEmployeeHistoryDeatiled(
			List<TimeSheetDataModel> lstEmployeeHistoryDeatiled) {
		this.lstEmployeeHistoryDeatiled = lstEmployeeHistoryDeatiled;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
	 * @return the lstAllEmployeeHistory
	 */
	public List<TimeSheetDataModel> getLstAllEmployeeHistory() {
		return lstAllEmployeeHistory;
	}
	/**
	 * @param lstAllEmployeeHistory the lstAllEmployeeHistory to set
	 */
	public void setLstAllEmployeeHistory(
			List<TimeSheetDataModel> lstAllEmployeeHistory) {
		this.lstAllEmployeeHistory = lstAllEmployeeHistory;
	}
	/**
	 * @return the footerSummary
	 */
	public String getFooterSummary() {
		return footerSummary;
	}
	/**
	 * @param footerSummary the footerSummary to set
	 */
	public void setFooterSummary(String footerSummary) {
		this.footerSummary = footerSummary;
	}
	public List<EmployeeModel> getLstUsers() {
		return lstUsers;
	}
	public void setLstUsers(List<EmployeeModel> lstUsers) {
		this.lstUsers = lstUsers;
	}
	public EmployeeModel getSelectedUsers() {
		return selectedUsers;
	}
	public void setSelectedUsers(EmployeeModel selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	
	
	
}
