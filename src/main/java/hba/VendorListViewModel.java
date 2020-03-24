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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import layout.MenuModel;
import model.CustomerModel;
import model.DataFilter;
import model.ImportExcelFiles;
import model.OtherNamesModel;
import model.QbListsModel;
import model.VendorModel;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
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
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class VendorListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	VendorsData data=new VendorsData();
	
	HBAData dataHba=new HBAData();
	
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
	
	private String importComapnyStandard;
	
	private Integer rowHeadderNo=0;
	
	private Integer rowDataNo=0;
	
	private String alreadyselectedfileName="";
	
	Media media;
	
	
		
	private DataFilter filter=new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	private boolean adminUser;

	private List<VendorModel> lstVendors=new ArrayList<VendorModel>();
	private List<VendorModel> lstAllVendors=new ArrayList<VendorModel>();
	private VendorModel selectedVendor;
	
	private  String listType;
	
	private List<String> lstStatus;
	private String selectedStatus;
	
	
	
	
	private List<List<VendorModel>> browsedGriddata = new ArrayList<List<VendorModel>>();
	private List<String> headers = new ArrayList<String>();
	private List<String> auxHeaderValues ;
	private List<ImportExcelFiles> headers1 = new ArrayList<ImportExcelFiles>();
	private String selectedItem;
	ImportExcelFiles importExcelFiles;
	
	DecimalFormat dcf=new DecimalFormat("0.00");
	Calendar calendr = Calendar.getInstance();
	
	
	private Set<VendorModel> selectedVendorsEntities;
	
	private boolean hideEmail=false;
	
	
	private MenuModel companyRole;
	
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		
		importComapnyStandard="A";
		importIsSelected="B";
		rowHeadderNo=4;
		rowDataNo=5;
		
		listType=type;
		
		lstStatus=new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		selectedStatus=lstStatus.get(0);
		
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		
		if(listType.equals("vendor"))
		{
		lstVendors=data.getVendorList("");
		lstAllVendors.addAll(lstVendors);
		if(lstVendors.size()>0)
		selectedVendor=lstVendors.get(0);
		//lstPageSize.add(lstVendors.size());
		}				
		selectedPageSize=lstPageSize.get(2);
		
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
		}
		getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		//Messagebox.show(type);
    }
	
	
	private List<String> FillHeadderList()
	{
		auxHeaderValues=new ArrayList<String>();
		auxHeaderValues.add("Dont Map");
		auxHeaderValues.add("Select");
		auxHeaderValues.add("Name");
		auxHeaderValues.add("Arabic Name");
		auxHeaderValues.add("Salutation");
		auxHeaderValues.add("First Name");
		auxHeaderValues.add("Middle Name");
		auxHeaderValues.add("Last Name");
		auxHeaderValues.add("Comapny Name");
		auxHeaderValues.add("Address");
		auxHeaderValues.add("Phone");
		auxHeaderValues.add("Alternate Phone");
		auxHeaderValues.add("Fax");
		auxHeaderValues.add("Email");
		auxHeaderValues.add("WebSite");
		auxHeaderValues.add("Cc");
		auxHeaderValues.add("Contact Person");
		auxHeaderValues.add("Alternate Contact");
		auxHeaderValues.add("Skype ID");
		auxHeaderValues.add("Bank Account Name");
		auxHeaderValues.add("Bank Account Number");
		auxHeaderValues.add("Bank Name");
		auxHeaderValues.add("Branch Name");
		auxHeaderValues.add("IBAN Number");
		return auxHeaderValues;
	}
	
	public VendorListViewModel()
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
			logger.error("ERROR in VendorListViewModel ----> init", ex);			
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
									browsedGriddata=new ArrayList<List<VendorModel>>();
									lstVendors.clear();
									lstVendors=data.getVendorList("");
									lstAllVendors.clear();
									lstAllVendors.addAll(lstVendors);
									if(lstVendors.size()>0)
									selectedVendor=lstVendors.get(0);
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
			Messagebox.show("Please select Excel in format (xls,xslx) !! ","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}		
		}
		catch (Exception ex)
		{	
			Messagebox.show("Error Loading the File Please Try agin with realoding page","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
			logger.error("ERROR in uploadFile ----> uploadFile", ex);			
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
		lstVendors.clear();
		try
	    {
		for(ImportExcelFiles f1:headers1)
		{
			f1.setIndexSelected(index);
			index++;
		}
		VendorModel mappingData;
		if(isValidHeadder(headers1))
		{
			  for(List<VendorModel> csv : browsedGriddata)
		      	{
				  mappingData=new VendorModel();
		      		if(!csv.isEmpty())
		      		{
		      				mappingData.setTimeCreated(df.parse(sdf.format(calendr.getTime())));
							mappingData.setArName("");
							mappingData.setCompanyName("");
							mappingData.setFirstName("");
							mappingData.setMiddleName("");
							mappingData.setLastName("");
							mappingData.setSalutation("");
							mappingData.setBillAddress1("");
							mappingData.setBalance(0.0);
							mappingData.setTotalBalance(0.0);
							mappingData.setPhone("");
							mappingData.setFax("");
							mappingData.setcC("");
							mappingData.setWebSite("");
							mappingData.setFax("");
							mappingData.setEmail("");
							mappingData.setContact("");
							mappingData.setAccountNumber("");
							mappingData.setContact("");
							mappingData.setAltContact("");
							mappingData.setAltPhone("");
							mappingData.setSkypeId("");
							mappingData.setAccountName("");
							mappingData.setBankName("");
							mappingData.setBranchName("");
							mappingData.setIbanNo("");
							mappingData.setPrintChequeAs("");
							mappingData.setNote("");
							
		      			for(int i=0; i < csv.size(); i++)
		      			{
		      				for(ImportExcelFiles f2:headers1)
		      				{
		      					
		      					if(null!=f2.getSelectedStatus() &&i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Name")) 
	      						{
	      							List<QbListsModel> QbListNames=dataHba.getNameFromQbListForValidation();
	      							for(QbListsModel ValidationName:QbListNames)
	      							{
	      								if(ValidationName.getName().replaceAll("\\s","").equalsIgnoreCase(csv.get(f2.getIndexSelected()).getName().replaceAll("\\s","")))
	      								{
	      									Messagebox.show("You cannot Continue.!!!Your Imported Excel Conatins a duplicated Name '"+csv.get(f2.getIndexSelected()).getName()+"' Please Change the Name in Previewed Data","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
	    		      						return;
	      								}
	      							}
	      						}
		      					if(f2.getSelectedStatus()==null || f2.getSelectedStatus().equals(""))
		      					{
		      						int indselcVal=f2.getIndexSelected()+1;
		      						Messagebox.show("Please select the Mapping Name in column "+indselcVal+"","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
		      						return;
		      					}
		      					else
		      					{	
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Name")) 
		      						{
		      							if(csv.get(i).getName()!=null)
		      							{
		      							mappingData.setName(csv.get(i).getName());
		      							mappingData.setFullName(csv.get(i).getName());
		      							}
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Salutation")) 
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setSalutation(csv.get(i).getName());
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Balance")) 
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setBalance(Double.parseDouble(csv.get(i).getName()));
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("TotalBalance")) 
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setTotalBalance(Double.parseDouble(csv.get(i).getName()));
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("FirstName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setFirstName(csv.get(i).getName());
		      							else
		      							mappingData.setFirstName("");
		      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("MiddleName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setMiddleName(csv.get(i).getName());
		      							else
		      							mappingData.setMiddleName("");
		      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("LastName"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setLastName(csv.get(i).getName());
		      							else
		      							mappingData.setLastName("");
		      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("WebSite"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setWebSite(csv.get(i).getName());
		      							else
		      							mappingData.setWebSite("");
		      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("Cc"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setcC(csv.get(i).getName());
		      							else
		      							mappingData.setcC("");
		      								
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
		      							mappingData.setBillAddress1(csv.get(i).getName());
		      							else
			      							mappingData.setBillAddress1("");
			      								
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
		      							mappingData.setAltPhone(csv.get(i).getName());
		      							else
			      							mappingData.setAltPhone("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("AlternateContactPerson"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setAltContact(csv.get(i).getName());
		      							else
			      							mappingData.setAltContact("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("SkypeID"))
		      						{
		      							if(csv.get(i).getName()!=null)
		      							mappingData.setSkypeId(csv.get(i).getName());
		      							else
			      							mappingData.setSkypeId("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("BankAccountName"))
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
		      							mappingData.setIbanNo(csv.get(i).getName());
		      							else
			      							mappingData.setIbanNo("");
			      								
		      						}
		      						if(i==f2.getIndexSelected() && f2.getSelectedStatus().replaceAll("\\s","").equalsIgnoreCase("name"))
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
		      		
		      	
		      		lstVendors.add(mappingData);
			  
    	    }
		Messagebox.show("The Selected Headders are mapped to the following columns successfully","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
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
			Messagebox.show("Error Mapping the File Please Try agin with realoding page","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
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
			 browsedGriddata=new ArrayList<List<VendorModel>>(); 
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
			      List<VendorModel> vendrList = new ArrayList<VendorModel>();
			      importExcelFiles=new ImportExcelFiles();
			      
			      while (rows.hasNext()) 
			      {
			    	  Row row =  (Row) rows.next();
			    	  boolean emptyRow=isEmptyRow(row);
			    	  Iterator cells;
			    	if(emptyRow==false)
			    	  {
		            cells = row.cellIterator();
		           ArrayList<VendorModel> dataLine = new ArrayList<VendorModel>();
		           
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
				             VendorModel customerModel=new VendorModel();
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
				             VendorModel customerModel=new VendorModel();
				             customerModel.setName(cell1.getStringCellValue());
				             dataLine.add(customerModel);
			        		  }
				          }
				          	 browsedGriddata.add(dataLine);
				        
			         }
		           }
			    	  }
		           
			      }
			      //validate Data
			      if(isDataValid(browsedGriddata))
			      {
			      logger.info("lst size >> " + vendrList.size());		
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
			logger.error("ERROR in VendorListViewModel ----> saveFile", ex);			
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
					Messagebox.show("Please Select the Headder Values other than Select ","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
					return false;
				}
			}
		
		}
		if(sameName==true)
		{
			Messagebox.show("You have  selected the same column Name in columns "+ind1+"and"+ind2+" ","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
			return false;
		}
		if(NameSelected==false)
		{
			Messagebox.show("Please map your atleast one column to Name","Import Vendor List", Messagebox.OK , Messagebox.EXCLAMATION);
			return false;
			
		}
		else{
			return true;
		}
	}
	
	private boolean isDataValid( List<List<VendorModel>> EmpList )
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
			logger.error("ERROR in VendorListViewModel ----> convertToDate", ex);			
		}
		return result;
	}
	
	 @Command
	   @NotifyChange({"lstVendors","footer"})
	   public void updateotherNameList()
	   {
		 int result=0;
		 int resultNew=0;
		 for(VendorModel customerModel:lstVendors)
		 {
			 int tmpRecNo=dataHba.GetOtherNameListRecNoQuery();
			 
			 resultNew=data.vendorListInsertQbListQuery(customerModel,tmpRecNo);
			 
			 if(resultNew>0)
			 {
			 result=data.vendorListInsertQuery(customerModel,tmpRecNo);
			 }
		 }  
		 if(result>0)
			 Messagebox.show("Vendor List is Updated..","Vendor List",Messagebox.OK , Messagebox.INFORMATION);
		 	Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();
	   }
	 
	
	
	
	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
	
	
	
	private List<VendorModel> filterVendorData()
	{
		lstVendors=lstAllVendors;
		List<VendorModel>  lst=new ArrayList<VendorModel>();
		for (Iterator<VendorModel> i = lstVendors.iterator(); i.hasNext();)
		{
			VendorModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getArName().toLowerCase().contains(filter.getArname().toLowerCase())&&
					tmp.getPhone().toLowerCase().contains(filter.getPhone().toLowerCase())&&
					tmp.getFax().toLowerCase().contains(filter.getFax().toLowerCase())&&
					tmp.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase())&&
					tmp.getContact().toLowerCase().contains(filter.getContact().toLowerCase())
					&&
					tmp.getAccountNumber().toLowerCase().contains(filter.getAccountNumber().toLowerCase())
					&&
					tmp.getAccountName().toLowerCase().contains(filter.getAccountName().toLowerCase())
					&&
					tmp.getBankName().toLowerCase().contains(filter.getBankName().toLowerCase())&&
					tmp.getBranchName().toLowerCase().contains(filter.getBranchName().toLowerCase())&&
					tmp.getIsActive().toLowerCase().contains(filter.getIsactive().toLowerCase())
					&&
					tmp.getIbanNo().toLowerCase().contains(filter.getIbanumber().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	    @Command
	    @NotifyChange({"footer","lstVendors"})
	    public void changeFilter() 
	    {
	    	try
	    	{
		   lstVendors=filterVendorData();
	    	}
	    	catch (Exception ex) {
				logger.error("error in VendorListViewModel---fillPropertyList-->" , ex);
			}
	    	
	    }
	   
	    @Command
		   public void resetVendor()
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
					logger.error("ERROR in ImportEmployeeViewModel ----> resetOtherName", ex);			
				}
		   }
	   
	   //edit vendor list
	   @Command
	   public void editVendorCommand(@BindingParam("row") VendorModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("vendKey", row.getVend_Key());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editvendor.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in VendorListViewModel ----> editVendorCommand", ex);			
			}
	   }
	   
	   @Command
	   public void viewVendorCommand(@BindingParam("row") VendorModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("vendKey", row.getVend_Key());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/hba/list/editvendor.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in VendorListViewModel ----> editVendorCommand", ex);			
			}
	   }
	   @Command
	   public void addVendorCommand()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("vendKey",0);
			   arg.put("compKey",0);
			   arg.put("type","Add");
			   Executions.createComponents("/hba/list/editvendor.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in VendorListViewModel ----> editVendorCommand", ex);			
			}
	   }
	   
	   @GlobalCommand 
		  @NotifyChange({"lstVendors"})
		    public void refreshParentVendor(@BindingParam("type")String type)
				  {		
					 try
					  {
						 lstVendors.clear();
						 lstVendors=data.getVendorList("");
						 lstAllVendors.clear();
						lstAllVendors=lstVendors;
						 
					  }
					 catch (Exception ex)
						{	
						logger.error("ERROR in VendorListViewModel ----> refreshParent", ex);			
						}
				  }

	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public List<VendorModel> getLstVendors() {
		return lstVendors;
	}

	public void setLstVendors(List<VendorModel> lstVendors) {
		this.lstVendors = lstVendors;
	}

	public VendorModel getSelectedVendor() {
		return selectedVendor;
	}

	public void setSelectedVendor(VendorModel selectedVendor) {
		this.selectedVendor = selectedVendor;
	}

	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	@NotifyChange({"selectedPageSize"})	
	public void setSelectedAllPageSize(String selectedAllPageSize) 
	{
		this.selectedAllPageSize = selectedAllPageSize;
		if(selectedAllPageSize.equals("All"))
		{
			if(listType.equals("vendor"))
			{
				selectedPageSize=lstVendors.size();
			}
			
			
		}
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}

	/**
	 * @return the uploadedFilePath
	 */
	public String getUploadedFilePath() {
		return uploadedFilePath;
	}

	/**
	 * @param uploadedFilePath the uploadedFilePath to set
	 */
	public void setUploadedFilePath(String uploadedFilePath) {
		this.uploadedFilePath = uploadedFilePath;
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
	@NotifyChange({"rowHeadderNo","rowDataNo","VendorListViewModel"})
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
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * @return the browsedGriddata
	 */
	public List<List<VendorModel>> getBrowsedGriddata() {
		return browsedGriddata;
	}

	/**
	 * @param browsedGriddata the browsedGriddata to set
	 */
	public void setBrowsedGriddata(List<List<VendorModel>> browsedGriddata) {
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
	@NotifyChange({"lstVendors"})
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		this.selectedStatus = selectedStatus;
		String status="";
		if(selectedStatus.equalsIgnoreCase("Active"))
			status="Y";
		else if(selectedStatus.equalsIgnoreCase("Inactive"))
			status="N";
		else if(selectedStatus.equalsIgnoreCase("All"))
			status="";
		
		lstVendors.clear();
		lstVendors=data.getVendorList(status);
		lstAllVendors.clear();
		lstAllVendors.addAll(lstVendors);
		if(lstVendors.size()>0)
		selectedVendor=lstVendors.get(0);
		
	}
	
	
	
	
	  @Command
			public void selectVendorsForSendEmails(@BindingParam("cmp") Window comp) 
			{
				List<Integer> lstVandorKey=new ArrayList<Integer>();
				String vendKeys="";
				if(selectedVendorsEntities!=null)
				{
					for (VendorModel item : selectedVendorsEntities) 
					{
						lstVandorKey.add(item.getVend_Key());
					}
					
				for (Integer custKey : lstVandorKey) 
				{
					if(vendKeys.equals(""))
						vendKeys+=String.valueOf(custKey);
					else
						vendKeys+=","+String.valueOf(custKey);
				}					
				
				}
				
				else if(lstVandorKey.size()==1)
				{
					vendKeys=String.valueOf(lstVendors.get(0).getVend_Key());
				}
				
				if(vendKeys.equals(""))
				{
					Messagebox.show("Please select Vendors!!","Vendors", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				Map args = new HashMap();
				args.put("myData", vendKeys);	
				args.put("slectedVendorObject", selectedVendorsEntities);	
				BindUtils.postGlobalCommand(null, null, "getVendorIDsForSendEmail", args);
				comp.detach();
			}
		   
		   
		   
		   
		   @Command
		   @NotifyChange({"lstVendors"})
		   public void hideVendorsWithNoEmailsForSendEmails() 
		 	{
		 	   try
			   {
		 		   	 			   
		 		   List<VendorModel> tempVendList=new ArrayList<VendorModel>();
		 		  tempVendList.addAll(lstAllVendors);
		 		  if(hideEmail)
		 		  {
		 			 lstVendors.clear();
		 		      for(VendorModel vendorModel:tempVendList)
		 		      {
		 			     if(vendorModel.getEmail()!=null && !vendorModel.getEmail().equalsIgnoreCase(""))
						 {						 
		 			    	lstVendors.add(vendorModel);
						 }
		 		      }
		 		     lstAllVendors.clear();
		 		   lstAllVendors.addAll(lstVendors);
		 		  }
		 		  else
		 		  {
		 			 String status="";
		 			if(selectedStatus.equalsIgnoreCase("Active"))
		 				status="Y";
		 			else if(selectedStatus.equalsIgnoreCase("InActive"))
		 			status="N";
		 			lstVendors.clear();
		 			lstVendors=data.getVendorList(status);
		 			lstAllVendors.clear();
		 			lstAllVendors.addAll(lstVendors);
		 		  }
				  
			   }
			   catch (Exception ex)
				{	
					logger.error("ERROR in CustomerViewModel ----> hideCutomersWithNoEmails", ex);			
				}
		 		}


		/**
		 * @return the selectedVendorsEntities
		 */
		public Set<VendorModel> getSelectedVendorsEntities() {
			return selectedVendorsEntities;
		}


		/**
		 * @param selectedVendorsEntities the selectedVendorsEntities to set
		 */
		public void setSelectedVendorsEntities(Set<VendorModel> selectedVendorsEntities) {
			this.selectedVendorsEntities = selectedVendorsEntities;
		}


		/**
		 * @return the hideEmail
		 */
		public boolean isHideEmail() {
			return hideEmail;
		}


		/**
		 * @param hideEmail the hideEmail to set
		 */
		public void setHideEmail(boolean hideEmail) {
			this.hideEmail = hideEmail;
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
		   
		private void getCompanyRolePermessions(int companyRoleId,int parentId)
		{
			companyRole=new MenuModel();

			List<MenuModel> lstRoles= dataHba.getMenuRoles(companyRoleId,parentId);
			for (MenuModel item : lstRoles) 
			{
				if(item.getMenuid()==74)
				{
					companyRole=item;
					break;
				}
			}
		}
	
	
}
