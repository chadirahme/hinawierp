package hba;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import layout.MenuModel;
import model.AccountsModel;
import model.CustomerModel;
import model.DataFilter;
import model.ImportExcelFiles;
import model.OtherNamesModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;

public class OtherNamesViewModel{
	private Logger logger = Logger.getLogger(this.getClass());
	
	HBAData data=new HBAData();
	OtherNameListData otherNameListData =new OtherNameListData();
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private String uploadedFilePath;
	private boolean canPreview;
	private boolean canSave;
	private boolean canPreviewMapping;
	private boolean canPreviewFinalList;
	private boolean canPreviewtemperoryList;
	private String message;
	private String attFile;
	
	private String importIsSelected;
	
	private boolean imoprtNotSelected;
	
	private String importComapnyStandard;
	
	private boolean importAnyExlec;
	
	private Integer rowHeadderNo=0;
	
	private Integer rowDataNo=0;
	
	
	private DataFilter filter=new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private Integer selectedPageSizeFroTempList;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	private List<OtherNamesModel> lstOtherNames;
	private List<OtherNamesModel> lstAllOtherNames;
	private OtherNamesModel selectedOtherNames;
	
	private boolean adminUser;
	private List<String> lstStatus;
	private String selectedStatus;
	
	private List<String> lstSortBy;
	private String selectedSortBy;
	
	private String alreadyselectedfileName="";
	
	private  String listType;
	
	Media media;

	private List<List<OtherNamesModel>> browsedGriddata = new ArrayList<List<OtherNamesModel>>();
	private List<String> headers = new ArrayList<String>();
	private List<String> auxHeaderValues ;
	private List<ImportExcelFiles> headers1 = new ArrayList<ImportExcelFiles>();
	private String selectedItem;
	ImportExcelFiles importExcelFiles;
	
	OtherNamesViewModel namesViewModel;
	
	DecimalFormat dcf=new DecimalFormat("0.00");
	Calendar calendr = Calendar.getInstance();
	
	private MenuModel companyRole;
  
	@Init
    public void init(@BindingParam("type") String type)
	{
		FillStatusList();
		importComapnyStandard="A";
		importIsSelected="B";
		rowHeadderNo=4;
		rowDataNo=5;
		selectedPageSizeFroTempList=1;
		namesViewModel=new OtherNamesViewModel();
		listType=type;
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(0);
		
		if(listType.equals("otherName"))
		{
			String sortBy="Name";
			String isactive="";
			lstOtherNames=otherNameListData.getOtherNameList(isactive,sortBy);
			lstAllOtherNames=lstOtherNames;
		if(lstOtherNames.size()>0)
			selectedOtherNames=lstOtherNames.get(0);
		}
		
		selectedPageSize=lstPageSize.get(0);
		
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
		}
		getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		
    }
	
	
	private void FillStatusList()
	{
		lstStatus=new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		selectedStatus=lstStatus.get(0);
		
		lstSortBy=new ArrayList<String>();
		lstSortBy.add("Name");
		lstSortBy.add("Created Date");
		lstSortBy.add("Company Name");
		lstSortBy.add("Account Number");
		selectedSortBy=lstSortBy.get(0);
		
	}
	
	private List<String> FillHeadderList()
	{
		auxHeaderValues=new ArrayList<String>();
		auxHeaderValues.add("Dont Map");
		auxHeaderValues.add("Select");
		auxHeaderValues.add("Name");
		auxHeaderValues.add("Arabic Name");
		//auxHeaderValues.add("Created Date");
		auxHeaderValues.add("Comapny Name");
		auxHeaderValues.add("Address");
		auxHeaderValues.add("Phone");
		auxHeaderValues.add("Fax");
		auxHeaderValues.add("Email");
		auxHeaderValues.add("Contact Person");
		auxHeaderValues.add("Alternate Phone");
		auxHeaderValues.add("Alternate Contact");
		auxHeaderValues.add("Skype ID");
		auxHeaderValues.add("Account Name");
		auxHeaderValues.add("Bank Account Number");
		auxHeaderValues.add("Bank Name");
		auxHeaderValues.add("Branch Name");
		auxHeaderValues.add("IBAN Number");
		return auxHeaderValues;
	}
	
	public OtherNamesViewModel()
	{
		try{
		canSave=false;
		canPreview=false;
		canPreviewMapping=false;
		canPreviewFinalList=true;
		canPreviewtemperoryList=false;
		uploadedFilePath="";
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in OtherNamesViewModel ----> init", ex);			
		}
	}
	
	@SuppressWarnings("unchecked")
	@Command 
	@NotifyChange({"attFile","canPreview","headers1","headers","browsedGriddata","disableHeadder"})
	public void uploadFile(BindContext ctx)
	{
		try
		{
			browsedGriddata.clear();
			headers1.clear();
			headers.clear();
			final String sortBy="Name";
			final String isactive="";
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();
			
			if(null!=importIsSelected && !importIsSelected.equalsIgnoreCase(""))
			{
				if(null!=importComapnyStandard && !importComapnyStandard.equalsIgnoreCase(""))
				{
					if(rowDataNo==0 || rowHeadderNo==0)
					{
					Messagebox.show("Please enter the Headder column No. and Data column No.","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
					}
				}
				else
				{
					Messagebox.show("Please select type of excel you want to import","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				
			}
			
			if(alreadyselectedfileName.equalsIgnoreCase(event.getMedia().getName()))
			{
				
				Messagebox.show("You have already Selected this file once.!!! Do you want to still you the Same file .?? !! ","Import Other Name List", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() 
						{						
							public void onEvent(Event evt) throws InterruptedException 
							{
								if (evt.getName().equals("onYes")) 
								{
									browsedGriddata=new ArrayList<List<OtherNamesModel>>();
									lstOtherNames=otherNameListData.getOtherNameList(isactive,sortBy);
									lstAllOtherNames=lstOtherNames;
									if(lstOtherNames.size()>0)
									selectedOtherNames=lstOtherNames.get(0);
									headers1= new ArrayList<ImportExcelFiles>(); 
									headers=new ArrayList<String>();
								}
								else 
								{		 
									
								}
							}	

						});
				
			}
			String fileFormat=event.getMedia().getFormat();
			alreadyselectedfileName=event.getMedia().getName();
			logger.info("format >> "+fileFormat);
			logger.info(event.getMedia().getContentType());
			logger.info(event.getMedia().getName());
			logger.info("size>> " + event.getMedia().getByteData().length);
			
			if(fileFormat.equals("xlsx") || fileFormat.equals("xls"))
			{
			String filePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"OtherNames"+File.separator;
			//Session sess = Sessions.getCurrent();
			String sessID=(Executions.getCurrent()).getDesktop().getId();
			logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
			String dirPath=repository+sessID;//session.getId();
			File dir = new File(dirPath);

			if(!dir.exists())
				dir.mkdirs();
				
			filePath = dirPath +File.separator +"." +event.getMedia().getFormat();	
			createFile(event.getMedia().getStreamData(), filePath);
			logger.info("filePath>> " + filePath);
			
			logger.info("File Uploaded");
			uploadedFilePath=filePath;			
			attFile=event.getMedia().getName() + " is uploaded.";
			canPreview=true;
		}
		else
		{
			Messagebox.show("Please select Excel in format (xls,xslx) !! ","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}		
		}
		catch (Exception ex)
		{	
			Messagebox.show("Error Loading the File Please Try agin with realoding page","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
			logger.error("ERROR in OtherNamesViewModel uploadFile ----> uploadFile", ex);			
		}
	}
	private int createFile( InputStream is, String filePath)
	{
		int res=0;
		try
	    {
		  File file = new File(filePath);  
		  DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		  int c;
		  while((c = is.read()) != -1)
		  {
			  out.writeByte(c);
		  }
		  is.close();
		  out.close();
	    }
		catch(Exception ex)
		{
			res=1;
			Messagebox.show(ex.getMessage(),"Import OtherNames", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		return res;
	}
	
	
	@Command
	@NotifyChange({"canPreviewtemperoryList","canPreviewFinalList","canPreview","canPreviewMapping","canSave","browsedGriddata"})
	public void updateGrid()
	{
		int index=0;
		lstOtherNames.clear();
		try
	    {
		for(ImportExcelFiles f1:headers1)
		{
			f1.setIndexSelected(index);
			index++;
		}
		OtherNamesModel mappingData;
		if(isValidHeadder(headers1))
		{
			  for(List<OtherNamesModel> csv : browsedGriddata)
		      	{
				  mappingData=new OtherNamesModel();
		      		if(!csv.isEmpty())
		      		{
		      				mappingData.setCreatedDate(df.parse(sdf.format(calendr.getTime())));
							mappingData.setArName("");
							mappingData.setCompanyName("");
							mappingData.setBillCountry("");
							mappingData.setPhone("");
							mappingData.setFax("");
							mappingData.setEmail("");
							mappingData.setContact("");
							mappingData.setAccountNumber("");
							mappingData.setContactPerson("");
							mappingData.setAltphone("");
							mappingData.setAlternateContact("");
							mappingData.setSkypeID("");
							mappingData.setAccountName("");
							mappingData.setBankName("");
							mappingData.setBranchName("");
							mappingData.setbBANNumber("");
							mappingData.setPrintChequeAs("");
							mappingData.setIsactive("Y");
		      			for(int i=0; i < csv.size(); i++)
		      			{
		      				for(ImportExcelFiles f2:headers1)
		      				{
		      					
		      					if(null!=f2.getSelectedStatus() &&i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Name")) 
	      						{
	      							List<QbListsModel> QbListNames=data.getNameFromQbListForValidation();
	      							for(QbListsModel ValidationName:QbListNames)
	      							{
	      								if(ValidationName.getName().replaceAll("\\s","").equalsIgnoreCase(csv.get(f2.getIndexSelected()).getName().replaceAll("\\s","")))
	      								{
	      									Messagebox.show("You cannot Continue.!!!Your Imported Excel Conatins a duplicated Name '"+csv.get(f2.getIndexSelected()).getName()+"' Please Change the Name in Previewed Data","Import OtherList", Messagebox.OK , Messagebox.EXCLAMATION);
	    		      						return;
	      								}
	      							}
	      						}
		      					if(f2.getSelectedStatus()==null || f2.getSelectedStatus().equals(""))
		      					{
		      						int indselcVal=f2.getIndexSelected()+1;
		      						Messagebox.show("Please select the Mapping Name in column "+indselcVal+"","Import OtherList", Messagebox.OK , Messagebox.EXCLAMATION);
		      						return;
		      					}
		      					else
		      					{	
		      							
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Name")) 
		      						{
		      						
		      							mappingData.setName(csv.get(i).getName());
		      							mappingData.setFullName(csv.get(i).getName());
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("ArabicName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setArName(csv.get(i).getName());
		      							else
		      							mappingData.setArName("");
		      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("ComapnyName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setCompanyName(csv.get(i).getName());
		      							else
			      							mappingData.setCompanyName("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Address"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setBillCountry(csv.get(i).getName());
		      							else
			      							mappingData.setBillCountry("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("phone"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setPhone(csv.get(i).getName());
		      							else
			      							mappingData.setPhone("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Fax"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setFax(csv.get(i).getName());
		      							else
			      							mappingData.setFax("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Email"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setEmail(csv.get(i).getName());
		      							else
			      							mappingData.setEmail("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("ContactPerson"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setContact(csv.get(i).getName());
		      							else
			      							mappingData.setContact("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("AlternatePhone"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setAltphone(csv.get(i).getName());
		      							else
			      							mappingData.setAltphone("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("AlternateContact"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setAlternateContact(csv.get(i).getName());
		      							else
			      							mappingData.setAlternateContact("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("SkypeID"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setSkypeID(csv.get(i).getName());
		      							else
			      							mappingData.setSkypeID("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("AccountName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setAccountName(csv.get(i).getName());
		      							else
			      							mappingData.setAccountName("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("BankAccountNumber"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setAccountNumber(csv.get(i).getName());
		      							else
			      							mappingData.setAccountNumber("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("BankName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setBankName(csv.get(i).getName());
		      							else
			      							mappingData.setBankName("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("BranchName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setBranchName(csv.get(i).getName());
		      							else
			      							mappingData.setBranchName("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("IBANNumber"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setbBANNumber(csv.get(i).getName());
		      							else
			      							mappingData.setbBANNumber("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("PrintChequeAs"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setPrintChequeAs(csv.get(i).getName());
		      							else
			      							mappingData.setPrintChequeAs("");
			      								
		      						}
		      						
		      					}
		      				}
		      				
		      			}
		      			
		      		}
		      		
		      	
			  lstOtherNames.add(mappingData);
			  
    	    }
		Messagebox.show("The Selected Headders are mapped to the following columns successfully","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
		canPreviewtemperoryList=false;
		canPreviewFinalList=true;
		canPreview=false;
		canSave=true;
		canPreviewMapping=true;
	    }
		else{
			
		}
	    }
		catch(Exception ex)
		{
			Messagebox.show("Error Mapping the File Please Try agin with realoding page","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
		}
	}
	
	
	@Command 	
	@NotifyChange({"attFile","selectedPageSizeFroTempList","canSave","message","browsedGriddata","headers","headers1","canPreviewMapping","canPreviewtemperoryList","canPreviewFinalList","canPreview"})
	public void previewDataFile()//saveFile()
	{
		try
		{
			message="";
			 message="Note:The columns which are selected as 'Dont Map' will be ignored ";
			 headers1=new ArrayList<ImportExcelFiles>();
			 browsedGriddata=new ArrayList<List<OtherNamesModel>>(); 
			 headers=new ArrayList<String>();
			if(uploadedFilePath.equals(""))
			{
				Messagebox.show("Please select Excel File !!","Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			else
			{
				  FileInputStream fis = null;
			      fis = new FileInputStream(uploadedFilePath);
			      Workbook workbook = WorkbookFactory.create(fis);
				  Sheet sheet = workbook.getSheetAt(0);
			      Iterator rows = sheet.rowIterator();
			      List<OtherNamesModel> OtherNmList = new ArrayList<OtherNamesModel>();
			      importExcelFiles=new ImportExcelFiles();
			      
			      while (rows.hasNext()) 
			      {
			    	  Row row =  (Row) rows.next();
			    	  boolean emptyRow=isEmptyRow(row);
			    	  Iterator cells;
			    	if(emptyRow==false)
			    	  {
		            cells = row.cellIterator();
		           ArrayList<OtherNamesModel> dataLine = new ArrayList<OtherNamesModel>();
		           
		           if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("A"))
		           {
			           if (row.getRowNum() == 4-1)
			           {
			        	   while (cells.hasNext())
					       {
			        		   Cell cell1 =  (Cell) cells.next();
			        		  if(!sheet.isColumnHidden(cell1.getColumnIndex()))
			        		  {
			        		   cell1.setCellType(Cell.CELL_TYPE_STRING);
			        		   headers.add(cell1.getStringCellValue());
			        		   importExcelFiles=new ImportExcelFiles();
			        		   importExcelFiles.setDropdowns(null);
			        		   importExcelFiles.setSelectedStatus(FillHeadderList().get(1));
			        		   importExcelFiles.setDropdowns(FillHeadderList());
			        		   headers1.add(importExcelFiles);
			        		  }
					       }
			        	   
			        	   
			           }
			           else if(row.getRowNum() >= 5-1){
			        	   
				          while (cells.hasNext())
				          {
				        	  
				        	  Cell cell1 =  (Cell) cells.next();
				        	  if(!sheet.isColumnHidden(cell1.getColumnIndex()))
			        		  {
				             cell1.setCellType(Cell.CELL_TYPE_STRING);
				             OtherNamesModel customerModel=new OtherNamesModel();
				             customerModel.setName(cell1.getStringCellValue());
				             dataLine.add(customerModel);
			        		  }
				          }
				          	 browsedGriddata.add(dataLine);
				        
			         }
		           }
		           else if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("B")){
		        	   if (row.getRowNum() == rowHeadderNo-1)
			           {
			        	   while (cells.hasNext())
					       {
			        		   Cell cell1 =  (Cell) cells.next();
			        		   if(!sheet.isColumnHidden(cell1.getColumnIndex()))
				        		  {
			        		   cell1.setCellType(Cell.CELL_TYPE_STRING);
			        		   headers.add(cell1.getStringCellValue());
			        		   importExcelFiles=new ImportExcelFiles();
			        		   importExcelFiles.setDropdowns(null);
			        		   importExcelFiles.setSelectedStatus(FillHeadderList().get(1));
			        		   importExcelFiles.setDropdowns(FillHeadderList());
			        		   headers1.add(importExcelFiles);
				        		  }
					       }
			        	   
			           }
			           else if(row.getRowNum() >= rowDataNo-1){
			        	   
				          while (cells.hasNext())
				          {
				        	  Cell cell1 =  (Cell) cells.next();
				        	  if(!sheet.isColumnHidden(cell1.getColumnIndex()))
			        		  {
				             cell1.setCellType(Cell.CELL_TYPE_STRING);
				             OtherNamesModel customerModel=new OtherNamesModel();
				             customerModel.setName(cell1.getStringCellValue());
				             dataLine.add(customerModel);
			        		  }
				          }
				          	 browsedGriddata.add(dataLine);
				        
			         }
		           }
			    	  }
		           
			      }
			      selectedPageSizeFroTempList= browsedGriddata.size();
			      //validate Data
			      if(isDataValid(browsedGriddata))
			      {
			      logger.info("lst size >> " + OtherNmList.size());		
			      if(browsedGriddata.size()>0)
			     // canSave=true;
			      canPreview=false;
			      canPreviewMapping=true;
			      canPreviewFinalList=false;
			      canPreviewtemperoryList=true;
			      }
			      else
			      {
			    	  message="There is missing data in file. Please make sure you first cloumns are headders and rest coulmns has data.";
			    	  canPreviewMapping=false;
			    	  canPreviewtemperoryList=false;
			    	  canPreviewFinalList=true;
			    	  
			      }
			      
		   }
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in OtherNamesViewModel ----> saveFile", ex);			
		}
	}
	
	static boolean isEmptyRow(Row row){
	    boolean isEmptyRow = true;
	        for(int cellNum = row.getFirstCellNum(); cellNum < row.getFirstCellNum()+2; cellNum++){
	           Cell cell = row.getCell(cellNum);
	           if(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && cell.getCellType()!= Cell.CELL_TYPE_FORMULA){
	           isEmptyRow = false;
	           }    
	        }
	    return isEmptyRow;
	  }
	
	private boolean isValidHeadder( List<ImportExcelFiles> headers1)
	{
		//boolean isvalid=true;
		boolean NameSelected=false;
		boolean sameName=false;
		int ind1=0,ind2=0;
		for(ImportExcelFiles f2:headers1)
		{
			if(null!=f2.getSelectedStatus() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Name"))
			{
				NameSelected=true;
				break;
			}
			else
			{
				NameSelected=false;
			}
			
		}
		for(ImportExcelFiles f2:headers1)
		{
			for(ImportExcelFiles f1:headers1)
			{
				if(null!=f2.getSelectedStatus() && null!=f1.getSelectedStatus() && (f2.getIndexSelected()!=f1.getIndexSelected()) && (f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase(f1.getSelectedStatus().replaceAll("\\s",""))) && (!f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("select"))&& (!f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("dontmap")) )
				{
					
					ind1=f1.getIndexSelected()+1;
					ind2=f2.getIndexSelected()+1;
					sameName=true;
					break;
				}
				
				if(null!=f2.getSelectedStatus() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Select"))
				{
					Messagebox.show("Please Select the Headder Values other than Select ","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
					return false;
				}
			}
		
		}
		if(sameName==true)
		{
			Messagebox.show("You have  selected the same column Name in columns "+ind1+"and"+ind2+" ","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
			return false;
		}
		if(NameSelected==false)
		{
			Messagebox.show("Please map your atleast one column to Name","Import Other Name List", Messagebox.OK , Messagebox.EXCLAMATION);
			return false;
			
		}
		else{
			return true;
		}
	}
	
	private boolean isDataValid( List<List<OtherNamesModel>> EmpList )
	{
		boolean isvalid=true;
		
		return isvalid;
	}
	private Date convertToDate(String value)
	{
		Date result = null;
		try
		{
			 value=value==null?"":value;
			 if(!value.equals(""))
			 {
		     value=value.replace("-", "/");
			 DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			 result=df.parse(value);
			 }
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in OtherNamesViewModel ----> convertToDate", ex);			
		}
		return result;
	}
	
	private List<OtherNamesModel> filterOtherNameData()
	{
		lstOtherNames=lstAllOtherNames;
		List<OtherNamesModel>  lst=new ArrayList<OtherNamesModel>();
		for (Iterator<OtherNamesModel> i = lstOtherNames.iterator(); i.hasNext();)
		{
			OtherNamesModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getArName().toLowerCase().contains(filter.getArname().toLowerCase())&&
					//tmp.getCreatedDate().() //(filter.getCreatedDate()) &&
					tmp.getCompanyName().toLowerCase().contains(filter.getCompanyName().toLowerCase())&&
					tmp.getBillCountry().toLowerCase().contains(filter.getBillCountry().toLowerCase())&&
					tmp.getPhone().toLowerCase().contains(filter.getPhone().toLowerCase())&&
					tmp.getFax().toLowerCase().contains(filter.getFax().toLowerCase())&&
					tmp.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase())&&
					tmp.getContact().toLowerCase().contains(filter.getContact().toLowerCase())&&
					tmp.getAccountNumber().toLowerCase().contains(filter.getAccountNumber().toLowerCase())
					&&
					tmp.getAccountName().toLowerCase().contains(filter.getAccountName().toLowerCase())
					&&
					tmp.getBankName().toLowerCase().contains(filter.getBankName().toLowerCase())&&
					tmp.getBranchName().toLowerCase().contains(filter.getBranchName().toLowerCase())&&
					tmp.getIsactive().toLowerCase().contains(filter.getIsactive().toLowerCase())
					&&
					tmp.getbBANNumber().toLowerCase().contains(filter.getIbanumber().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	 @Command
	    @NotifyChange({"footer","lstOtherNames"})
	    public void changeFilter() 
	    {	      
		   
		   if(listType.equalsIgnoreCase("otherName"))
		   lstOtherNames=filterOtherNameData();
		   
	    }
	 
	 @Command
	   @NotifyChange({"lstOtherNames","footer"})
	   public void updateotherNameList()
	   {
		 int result=0;
		 int resultNew=0;
		 for(OtherNamesModel customerModel:lstOtherNames)
		 {
			 int tmpRecNo=data.GetOtherNameListRecNoQuery();
			 
			 resultNew=data.OtherNameListInsertQbListQuery(customerModel,tmpRecNo);
			 
			 if(resultNew>0)
			 {
			 result=data.OtherNameListInsertQuery(customerModel,tmpRecNo);
			 }
		 }  
		 if(result>0)
			 Messagebox.show("Other Name List is Updated Sucessfully","Other Name List",Messagebox.OK , Messagebox.INFORMATION);
		 	 Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			 Center center = bl.getCenter();
			 Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			 tabbox.getSelectedPanel().getLastChild().invalidate();
	   }
	 
	 @Command
	   public void viewOtherNameCommand(@BindingParam("row") OtherNamesModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("otherNameKey", row.getCustkey());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/hba/list/editOtherNameList.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in OtherNamesViewModel ----> viewOtherNameCommand", ex);			
			}
	   }
	
	 @Command
	   public void editOtherNameCommand(@BindingParam("row") OtherNamesModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("otherNameKey", row.getCustkey());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editOtherNameList.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in OtherNamesViewModel ----> editOtherNameCommand", ex);			
			}
	   }
	 
	 @Command
	   public void addOtherNameCommand(@BindingParam("row") OtherNamesModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("otherNameKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","add");
			   Executions.createComponents("/hba/list/editOtherNameList.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in OtherNamesViewModel ----> addOtherNameCommand", ex);			
			}
	   }
	 
	 @Command
	   public void resetOtherName()
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
				logger.error("ERROR in OtherNamesViewModel ----> resetOtherName", ex);			
			}
	   }
	 
	 @GlobalCommand 
	  @NotifyChange({"lstOtherNames"})
	    public void refreshParent(@BindingParam("type")String type)
			  {		
				 try
				  {
						lstOtherNames=otherNameListData.getOtherNameList("Y","Name");
						lstAllOtherNames=lstOtherNames;
					 
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in OtherNamesViewModel ----> refreshParent", ex);			
					}
			  }
	
	


	/**
	 * @return the filter
	 */
	public DataFilter getFilter() {
		return filter;
	}


	/**
	 * @param filter the filter to set
	 */
	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}


	/**
	 * @return the lstPageSize
	 */
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}


	/**
	 * @param lstPageSize the lstPageSize to set
	 */
	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}


	/**
	 * @return the selectedPageSize
	 */
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}


	/**
	 * @param selectedPageSize the selectedPageSize to set
	 */
	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}


	/**
	 * @return the lstAllPageSize
	 */
	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}


	/**
	 * @param lstAllPageSize the lstAllPageSize to set
	 */
	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}


	/**
	 * @return the selectedAllPageSize
	 */
	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}


	/**
	 * @param selectedAllPageSize the selectedAllPageSize to set
	 */
	@NotifyChange({"selectedPageSize"})
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if(selectedAllPageSize.equals("All"))
		{
			if(listType.equalsIgnoreCase("otherName"))
			{
				selectedPageSize=lstOtherNames.size();
			}
				
		}
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}


	/**
	 * @return the lstOtherNames
	 */
	public List<OtherNamesModel> getLstOtherNames() {
		return lstOtherNames;
	}


	/**
	 * @param lstOtherNames the lstOtherNames to set
	 */
	public void setLstOtherNames(List<OtherNamesModel> lstOtherNames) {
		this.lstOtherNames = lstOtherNames;
	}


	/**
	 * @return the selectedOtherNames
	 */
	public OtherNamesModel getSelectedOtherNames() {
		return selectedOtherNames;
	}


	/**
	 * @param selectedOtherNames the selectedOtherNames to set
	 */
	public void setSelectedOtherNames(OtherNamesModel selectedOtherNames) {
		this.selectedOtherNames = selectedOtherNames;
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


	/**
	 * @return the lstStatus
	 */
	public List<String> getLstStatus() {
		return lstStatus;
	}


	/**
	 * @param lstStatus the lstStatus to set
	 */
	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
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
	@NotifyChange({"lstOtherNames"})
	public void setSelectedStatus(String selectedStatus) {
		
		this.selectedStatus = selectedStatus;
		String status="";
		if(selectedStatus.equalsIgnoreCase("Active"))
			status="Y";
		else if(selectedStatus.equalsIgnoreCase("Inactive"))
			status="N";
		else if(selectedStatus.equalsIgnoreCase("All"))
			status="";
		
		lstOtherNames=otherNameListData.getOtherNameList(status,"Name");
		lstAllOtherNames=lstOtherNames;
	if(lstOtherNames.size()>0)
		selectedOtherNames=lstOtherNames.get(0);
	}


	
	
	/**
	 * @return the lstSortBy
	 */
	public List<String> getLstSortBy() {
		return lstSortBy;
	}


	/**
	 * @param lstSortBy the lstSortBy to set
	 */
	public void setLstSortBy(List<String> lstSortBy) {
		this.lstSortBy = lstSortBy;
	}


	/**
	 * @return the selectedSortBy
	 */
	public String getSelectedSortBy() {
		return selectedSortBy;
	}


	/**
	 * @param selectedSortBy the selectedSortBy to set
	 */
	@NotifyChange({"lstOtherNames"})
	public void setSelectedSortBy(String selectedSortBy) {
		this.selectedSortBy = selectedSortBy;
		String sortBy="";
		if(selectedSortBy.replaceAll("\\s","").equalsIgnoreCase("Name"))
			sortBy="Name";
		else if(selectedSortBy.replaceAll("\\s","").equalsIgnoreCase("CompanyName"))
			sortBy="CompanyName";
		else if(selectedSortBy.replaceAll("\\s","").equalsIgnoreCase("AccountNumber"))
			sortBy="accountNo";
		else if(selectedSortBy.replaceAll("\\s","").equalsIgnoreCase("createdDate"))
			sortBy="timecreated";
		
		lstOtherNames=otherNameListData.getOtherNameList("Y",sortBy);
		lstAllOtherNames=lstOtherNames;
	if(lstOtherNames.size()>0)
		selectedOtherNames=lstOtherNames.get(0);
	}


	/**
	 * @return the canPreview
	 */
	public boolean isCanPreview() {
		return canPreview;
	}


	/**
	 * @param canPreview the canPreview to set
	 */
	public void setCanPreview(boolean canPreview) {
		this.canPreview = canPreview;
	}


	/**
	 * @return the canSave
	 */
	public boolean isCanSave() {
		return canSave;
	}


	/**
	 * @param canSave the canSave to set
	 */
	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @return the attFile
	 */
	public String getAttFile() {
		return attFile;
	}


	/**
	 * @param attFile the attFile to set
	 */
	public void setAttFile(String attFile) {
		this.attFile = attFile;
	}


	/**
	 * @return the browsedGriddata
	 */
	public List<List<OtherNamesModel>> getBrowsedGriddata() {
		return browsedGriddata;
	}


	/**
	 * @param browsedGriddata the browsedGriddata to set
	 */
	public void setBrowsedGriddata(List<List<OtherNamesModel>> browsedGriddata) {
		this.browsedGriddata = browsedGriddata;
	}


	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}


	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}


	/**
	 * @return the selectedItem
	 */
	public String getSelectedItem() {
		return selectedItem;
	}


	/**
	 * @param selectedItem the selectedItem to set
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}


	/**
	 * @return the headers1
	 */
	public List<ImportExcelFiles> getHeaders1() {
		return headers1;
	}


	/**
	 * @param headers1 the headers1 to set
	 */
	public void setHeaders1(List<ImportExcelFiles> headers1) {
		this.headers1 = headers1;
	}


	/**
	 * @return the importExcelFiles
	 */
	public ImportExcelFiles getImportExcelFiles() {
		return importExcelFiles;
	}


	/**
	 * @param importExcelFiles the importExcelFiles to set
	 */
	public void setImportExcelFiles(ImportExcelFiles importExcelFiles) {
		this.importExcelFiles = importExcelFiles;
	}


	/**
	 * @return the canPreviewMapping
	 */
	public boolean isCanPreviewMapping() {
		return canPreviewMapping;
	}


	/**
	 * @param canPreviewMapping the canPreviewMapping to set
	 */
	public void setCanPreviewMapping(boolean canPreviewMapping) {
		this.canPreviewMapping = canPreviewMapping;
	}


	/**
	 * @return the canPreviewFinalList
	 */
	public boolean isCanPreviewFinalList() {
		return canPreviewFinalList;
	}


	/**
	 * @param canPreviewFinalList the canPreviewFinalList to set
	 */
	public void setCanPreviewFinalList(boolean canPreviewFinalList) {
		this.canPreviewFinalList = canPreviewFinalList;
	}


	/**
	 * @return the canPreviewtemperoryList
	 */
	public boolean isCanPreviewtemperoryList() {
		return canPreviewtemperoryList;
	}


	/**
	 * @param canPreviewtemperoryList the canPreviewtemperoryList to set
	 */
	public void setCanPreviewtemperoryList(boolean canPreviewtemperoryList) {
		this.canPreviewtemperoryList = canPreviewtemperoryList;
	}


	/**
	 * @return the alreadyselectedfileName
	 */
	public String getAlreadyselectedfileName() {
		return alreadyselectedfileName;
	}


	/**
	 * @param alreadyselectedfileName the alreadyselectedfileName to set
	 */
	public void setAlreadyselectedfileName(String alreadyselectedfileName) {
		this.alreadyselectedfileName = alreadyselectedfileName;
	}


	/**
	 * @return the auxHeaderValues
	 */
	public List<String> getAuxHeaderValues() {
		return auxHeaderValues;
	}


	/**
	 * @param auxHeaderValues the auxHeaderValues to set
	 */
	public void setAuxHeaderValues(List<String> auxHeaderValues) {
		this.auxHeaderValues = auxHeaderValues;
	}





	/**
	 * @return the imoprtNotSelected
	 */
	public boolean isImoprtNotSelected() {
		return imoprtNotSelected;
	}


	/**
	 * @param imoprtNotSelected the imoprtNotSelected to set
	 */
	public void setImoprtNotSelected(boolean imoprtNotSelected) {
		this.imoprtNotSelected = imoprtNotSelected;
	}


	/**


	/**
	 * @return the importAnyExlec
	 */
	public boolean isImportAnyExlec() {
		return importAnyExlec;
	}


	/**
	 * @param importAnyExlec the importAnyExlec to set
	 */
	public void setImportAnyExlec(boolean importAnyExlec) {
		this.importAnyExlec = importAnyExlec;
	}


	/**
	 * @return the rowHeadderNo
	 */
	public Integer getRowHeadderNo() {
		return rowHeadderNo;
	}


	/**
	 * @param rowHeadderNo the rowHeadderNo to set
	 */
	public void setRowHeadderNo(Integer rowHeadderNo) {
		this.rowHeadderNo = rowHeadderNo;
	}


	/**
	 * @return the rowDataNo
	 */
	public Integer getRowDataNo() {
		return rowDataNo;
	}


	/**
	 * @param rowDataNo the rowDataNo to set
	 */
	public void setRowDataNo(Integer rowDataNo) {
		this.rowDataNo = rowDataNo;
	}


	/**
	 * @return the importIsSelected
	 */
	public String getImportIsSelected() {
		return importIsSelected;
	}


	/**
	 * @param importIsSelected the importIsSelected to set
	 */
	public void setImportIsSelected(String importIsSelected) {
		this.importIsSelected = importIsSelected;
	}


	/**
	 * @return the importComapnyStandard
	 */
	public String getImportComapnyStandard() {
		return importComapnyStandard;
	}


	/**
	 * @param importComapnyStandard the importComapnyStandard to set
	 */
	@NotifyChange({"rowHeadderNo","rowDataNo","OtherNamesViewModel"})
	public void setImportComapnyStandard(String importComapnyStandard) {
		
		if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("A"))
		{
			rowHeadderNo=4;
			rowDataNo=5;
		}
		if(null!=importComapnyStandard && importComapnyStandard.equalsIgnoreCase("B"))
		{
			rowHeadderNo=0;
			rowDataNo=0;
		}
		
		this.importComapnyStandard = importComapnyStandard;
	}


	/**
	 * @return the selectedPageSizeFroTempList
	 */
	public Integer getSelectedPageSizeFroTempList() {
		return selectedPageSizeFroTempList;
	}


	/**
	 * @param selectedPageSizeFroTempList the selectedPageSizeFroTempList to set
	 */
	public void setSelectedPageSizeFroTempList(Integer selectedPageSizeFroTempList) {
		this.selectedPageSizeFroTempList = selectedPageSizeFroTempList;
	}
	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==242)
			{
				companyRole=item;
				break;
			}
		}
	}


	/**
	 * @return the companyRole
	 */
	public MenuModel getCompanyRole() {
		return companyRole;
	}


	/**
	 * @param companyRole the companyRole to set
	 */
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	
	
	
	
	

}
