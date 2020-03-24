package hr;

import hr.model.CompanyModel;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.EmployeeModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Messagebox;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ImportEmployeeViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private String attFile;
	
	private String uploadedFilePath;
	private List<HRListValuesModel> lstDepartment;
	private List<HRListValuesModel> lstPosition;
	private List<HRListValuesModel> lstNationality;
	private List<HRListValuesModel> lstGender;
	private List<HRListValuesModel> lstMarital;
	private List<HRListValuesModel> lstReligion;
	
	private List<EmployeeModel> lstEmployeeData;
	
	private boolean canPreview;
	private boolean canSave;
	private String message;
	
	public ImportEmployeeViewModel()
	{
		try
		{
			int defaultCompanyId=0;
			defaultCompanyId=data.getDefaultCompanyID(1);
			lstComapnies=data.getCompanyList(1);
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>1 && defaultCompanyId==0)
			selectedCompany=lstComapnies.get(0);
			uploadedFilePath="";
			
			lstDepartment=data.getHRListValues(6,"");
			lstPosition=data.getHRListValues(7,"");
			lstNationality=data.getHRListValues(1, "");
			lstGender=data.getHRListValues(4, "");
			lstMarital=data.getHRListValues(9, "");
			lstReligion=data.getHRListValues(25, "");
			
			canSave=false;
			canPreview=false;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ImportEmployeeViewModel ----> init", ex);			
		}
	}

	@Command 
	@NotifyChange({"attFile","canPreview"})
	public void uploadFile(BindContext ctx)
	{
		try
		{
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();
			String fileFormat=event.getMedia().getFormat();
			logger.info("format >> "+fileFormat);
			logger.info(event.getMedia().getContentType());
			logger.info("size>> " + event.getMedia().getByteData().length);
			
			if(!fileFormat.equals("xls"))
			{
				Messagebox.show("Please select Excel in 2003 format (xls) !! ","Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}					
		
			String filePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator+"employees"+File.separator;
			
			//Session sess = Sessions.getCurrent();
			String sessID=(Executions.getCurrent()).getDesktop().getId();
			logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
			String dirPath=repository+sessID;//session.getId();
			File dir = new File(dirPath);

			if(!dir.exists())
				dir.mkdirs();
				
			filePath = dirPath +File.separator +selectedCompany.getCompKey() + "." +event.getMedia().getFormat();	
			createFile(event.getMedia().getStreamData(), filePath);
			logger.info("filePath>> " + filePath);
			
			logger.info("File Uploaded");
			uploadedFilePath=filePath;			
			attFile=event.getMedia().getName() + " is uploaded.";
			canPreview=true;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ImportEmployeeViewModel ----> uploadFile", ex);			
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
			Messagebox.show(ex.getMessage(),"Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		return res;
	}
	
	@Command 	
	public void saveFile()
	{
		try
		{			
			if(lstEmployeeData.size()==0)
			{
				Messagebox.show("There is no data to save !!","Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			List<EmployeeModel> lst=data.checkEmployeeNoExist(selectedCompany.getCompKey());
			for (EmployeeModel item : lstEmployeeData) 
		      {
		    	item.setCompKey(selectedCompany.getCompKey());
		    	
				int depId=getHRListID(item.getEnDepartmentName(),"6");
				if(depId==0)
				{
					//add new department
					int newID=data.addNewHRListValue( item.getEnDepartmentName(), item.getArabicDepartment(), "DEPARTMENT", "6");
					if(newID>0)
					{
						depId=newID;
						HRListValuesModel obj=new HRListValuesModel();
						obj.setListId(newID);
						obj.setEnDescription(item.getEnDepartmentName());
						lstDepartment.add(obj);
					}
				}
				item.setDepartmentID(depId);
				
				int posId=getHRListID(item.getEnPositionName(),"7");
				if(posId==0)
				{
					//add new Position
					int newID=data.addNewHRListValue( item.getEnPositionName(), item.getArabicPosition(), "POSITION", "7");
					if(newID>0)
					{
						posId=newID;
						HRListValuesModel obj=new HRListValuesModel();
						obj.setListId(newID);
						obj.setEnDescription(item.getEnPositionName());
						lstPosition.add(obj);
					}
				}							
				item.setPositionID(posId);
				
				if(item.getNationality().toLowerCase().equals("local"))
				{
					item.setNationalityID(0);
				}
				else
				{
				int NatId=getHRListID(item.getNationality(),"1");
				if(NatId==0)
				{
					//add new Position
					int newID=data.addNewHRListValue( item.getNationality(), "", "NATIONALITY", "1");
					if(newID>0)
					{
						NatId=newID;
						HRListValuesModel obj=new HRListValuesModel();
						obj.setListId(newID);
						obj.setEnDescription(item.getNationality());
						lstNationality.add(obj);
					}
				}							
				item.setNationalityID(NatId);
				}
				
				int sexId=getHRListID(item.getSex(), "4");
				if(sexId==0)
					sexId=1;
				item.setSexId(sexId);
				
				int MaritalId=getHRListID(item.getMaritalStatus(), "9");
				if(MaritalId==0)
				{
					//add new Position
					int newID=data.addNewHRListValue( item.getMaritalStatus(), "", "MARITAL", "9");
					if(newID>0)
					{
						MaritalId=newID;
						HRListValuesModel obj=new HRListValuesModel();
						obj.setListId(newID);
						obj.setEnDescription(item.getMaritalStatus());
						lstMarital.add(obj);
					}
				}							
				item.setMaritalID(MaritalId);
				
				int religionId=getHRListID(item.getEnReligion(), "25");
				if(religionId==0)
				{
					//add new Position
					int newID=data.addNewHRListValue( item.getEnReligion(), "", "RELIGION", "25");
					if(newID>0)
					{
						religionId=newID;
						HRListValuesModel obj=new HRListValuesModel();
						obj.setListId(newID);
						obj.setEnDescription(item.getEnReligion());
						lstReligion.add(obj);
					}
				}							
				item.setReligionId(religionId);
				
			
				item.setDateOfBirth(convertToDate(item.getDob()));
				item.setJoiningDate(convertToDate(item.getJoinDate()));				
				//we have to check if empNo exist before save
				
				
				if(lst.contains(item.getEmployeeNo()))
				{
					//Messagebox.show("Employee number exist for this company  !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
					//return;
				}
				else
				{
				data.insertNewEmployee(item);
				}
				
			  }				
			
			Messagebox.show("Data is saved..","Import Employee", Messagebox.OK , Messagebox.INFORMATION);
		}
		catch(Exception ex)
		{			
			Messagebox.show(ex.getMessage(),"Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
		}
	}
	
	@Command 	
	@NotifyChange({"attFile","canSave","lstEmployeeData","message"})
	public void previewDataFile()//saveFile()
	{
		try
		{
			message="";
			int colEmpNO=2;
			int colEnName=3;
			int colArName=4;
			int colJoinDate=5;
			int colSex=6;
			int colNat=7;
			int colDob=8;
			int colReligion=9;
			int colMarital=10;
			int colPos=11;
			int colDep=12;
			int colLoc=13;
			
			if(uploadedFilePath.equals(""))
			{
				Messagebox.show("Please select Excel File !!","Import Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			else
			{
				  FileInputStream fis = null;
			      fis = new FileInputStream(uploadedFilePath);
			      HSSFWorkbook workbook = new HSSFWorkbook(fis);
			      HSSFSheet sheet = workbook.getSheetAt(0);
			      Iterator rows = sheet.rowIterator();
			      List<EmployeeModel> EmpList = new ArrayList<EmployeeModel>();
			      
			      while (rows.hasNext()) 
			      {
		           HSSFRow row = (HSSFRow) rows.next();
		           Iterator cells = row.cellIterator();
		           EmployeeModel empModel = new EmployeeModel();
		           
		          
			           if (row.getRowNum() >= 3)
			           {
				          while (cells.hasNext())
				          {
				        	  
				             HSSFCell cell1 = (HSSFCell) cells.next();
				             //cell1.setCellType(Cell.CELL_TYPE_STRING);
				             
				           //  logger.info(">> " + cell1.getColumnIndex());
				            // logger.info(">> " + cell1.getStringCellValue());
				             
				             if(cell1.getColumnIndex() == colEmpNO)
				             {
				            	 if(cell1.getStringCellValue()==null || cell1.getStringCellValue().equals(""))
					        	   break;
				             } 
				             
				             if (cell1.getColumnIndex() == 0 && !cell1.getStringCellValue().equalsIgnoreCase(""))
				             {				            	  
				            	 empModel.setCompKey(253);
				             }
				             else if (cell1.getColumnIndex() == colDep)
				             {
				            empModel.setEnDepartmentName(cell1.getStringCellValue());	 
				             }
				            // else if (cell1.getColumnIndex() == 5)
				            // {
				            //empModel.setArabicDepartment(cell1.getStringCellValue());	 
				            // }
				             
				             else if (cell1.getColumnIndex() == colPos) 
				             {				            	  
				              empModel.setEnPositionName(cell1.getStringCellValue());
				             }
				             //else if (cell1.getColumnIndex() == 7) 
				            // {				            	  
				            //  empModel.setArabicPosition(cell1.getStringCellValue());
				            // }
				             
				             else if (cell1.getColumnIndex() == colLoc )
				             {				            	 
				            	   empModel.setEnLocationName(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == colEmpNO )
				             {				            
				            	   cell1.setCellType(Cell.CELL_TYPE_STRING);
				            	   empModel.setEmployeeNo(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == colEnName )
				             {				            	 
				            	   empModel.setFullName(cell1.getStringCellValue());
				             
				             if(empModel.getFullName()!=null)
				             {
				               List<String> empList = new ArrayList<String>(Arrays.asList(empModel.getFullName().split(" ")));				            	 				           
				            	if(empList.size()==1)
				            	{
				            	empModel.setEnFirstName(empList.get(0));
				            	empModel.setEnMiddleName("");
				            	empModel.setEnLastName("");
				            	}	
				            	if(empList.size()==2)
				            	{
				            	empModel.setEnFirstName(empList.get(0));
				            	empModel.setEnMiddleName("");
				            	empModel.setEnLastName(empList.get(1));
				            	}
				            	if(empList.size()>2)
				            	{
				            	empModel.setEnFirstName(empList.get(0));
				            	empModel.setEnMiddleName(empList.get(1));
				            	empModel.setEnLastName(empList.get(2));
				            	}		
				             }
				            }
				             /*
				             else if (cell1.getColumnIndex() == 12 )
				             {				            	 
				            	   empModel.setEnFirstName(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == 13 )
				             {				            	 
				            	   empModel.setEnMiddleName(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == 14 )
				             {				            	 
				            	   empModel.setEnLastName(cell1.getStringCellValue());
				             }
				             */
				             else if (cell1.getColumnIndex() == colArName )
				             {				            	 
				            	   empModel.setArabicName(cell1.getStringCellValue());
				            	   if(empModel.getArabicName()!=null)
						             {
						               List<String> empList = new ArrayList<String>(Arrays.asList(empModel.getArabicName().split(" ")));				            	 				           
						            	if(empList.size()==1)
						            	{
						            	empModel.setArFirstName(empList.get(0));
						            	empModel.setArMiddleName("");
						            	empModel.setArLastName("");
						            	}	
						            	if(empList.size()==2)
						            	{
						            	empModel.setArFirstName(empList.get(0));
						            	empModel.setArMiddleName("");
						            	empModel.setArLastName(empList.get(1));
						            	}
						            	if(empList.size()>2)
						            	{
						            	empModel.setArFirstName(empList.get(0));
						            	empModel.setArMiddleName(empList.get(1));
						            	empModel.setArLastName(empList.get(2));
						            	}		
						             }
				             }
				             /*
				             else if (cell1.getColumnIndex() == 16 )
				             {				            	 
				            	   empModel.setArFirstName(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == 17 )
				             {				            	 
				            	   empModel.setArMiddleName(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == 18 )
				             {				            	 
				            	   empModel.setArLastName(cell1.getStringCellValue());
				             }
				             */
				             else if (cell1.getColumnIndex() == colJoinDate )
				             {			
				            	 //empModel.setJoinDate(cell1.getStringCellValue());
				            	 
				            	 if (HSSFDateUtil.isCellDateFormatted(cell1)) 
				            	 {
				            		 //String dateFmt = cell1.getCellStyle().getDataFormatString();
				            		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				            		 empModel.setJoinDate(sdf.format(cell1.getDateCellValue()));
				            		// logger.info(">> " + cell1.getDateCellValue());
				            		// logger.info(">>>"+ empModel.getJoinDate());
				            	 }
				            	 else
				            	 {
				            	  empModel.setJoinDate(cell1.getStringCellValue());
				            	 }
				            	 
				             }
				             else if (cell1.getColumnIndex() == colSex )
				             {
				            	 // logger.info("sex>>" +cell1.getCellType() );
				            	   empModel.setSex(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == colNat )
				             {				            	 
				            	   empModel.setNationality(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == colDob )
				             {					            	   
				            	 if (HSSFDateUtil.isCellDateFormatted(cell1)) 
				            	 {
				            		 //String dateFmt = cell1.getCellStyle().getDataFormatString();
				            		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				            		 empModel.setDob(sdf.format(cell1.getDateCellValue()));
				            		// logger.info(">> " + cell1.getDateCellValue());
				            		// logger.info(">>>"+ empModel.getJoinDate());
				            	 }
				            	 else
				            	 {
				            	  empModel.setDob(cell1.getStringCellValue());
				            	 }				            	 				            
				             }
				             else if (cell1.getColumnIndex() == colReligion )
				             {				            	 
				            	   empModel.setEnReligion(cell1.getStringCellValue());
				             }
				             else if (cell1.getColumnIndex() == colMarital )
				             {				            	 
				            	   empModel.setMaritalStatus(cell1.getStringCellValue());
				             }
				             
				          }
				          
				          if(empModel.getEmployeeNo()!=null && !empModel.getEmployeeNo().equals(""))
				          EmpList.add(empModel);
				        
			         }
			      }
			      
			      //validate Data
			      if(true)//isDataValid(EmpList))
			      {
			      lstEmployeeData=EmpList;
			      logger.info("lst size >> " + EmpList.size());		
			      if(EmpList.size()>0)
			      canSave=true;
			      }
			      else
			      {
			    	  message="There is missing data in file. Please check that all red column are filled.";
			    	  canSave=false;
			      }
			      
		   }
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in ImportEmployeeViewModel ----> saveFile", ex);			
		}
	}
	
	private boolean isDataValid( List<EmployeeModel> EmpList )
	{
		boolean isvalid=true;
		
		
		for (EmployeeModel item : EmpList) 
		{
		if(item.getEmployeeNo().equals(""))
		{
			return false;
		}
		if(item.getFullName().equals(""))
		{
			return false;
		}
		if(item.getEnFirstName().equals(""))
		{
			return false;
		}
		if(item.getEnLastName().equals(""))
		{
			return false;
		}
		if(item.getJoinDate().equals(""))
		{
			return false;
		}
		if(item.getSex().equals(""))
		{
			return false;
		}
		if(item.getNationality().equals(""))
		{
			return false;
		}
		if(item.getDob().equals(""))
		{
			return false;
		}
		if(item.getEnReligion().equals(""))
		{
			return false;
		}
		if(item.getMaritalStatus().equals(""))
		{
			return false;
		}
		
		}
		
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
			logger.error("ERROR in ImportEmployeeViewModel ----> convertToDate", ex);			
		}
		return result;
	}
	private int getHRListID(String enDescription,String type)
	{
		int ID=0;
		enDescription=enDescription==null?"":enDescription.trim();
		if(type.equals("6"))
		{
		for (HRListValuesModel item : lstDepartment) 
		{
			if(item.getEnDescription().toLowerCase().equals(enDescription.toLowerCase()))
			{
				ID=item.getListId();
				return ID;
			}
		}
		}
		
		if(type.equals("7"))
		{
		for (HRListValuesModel item : lstPosition) 
		{
			if(item.getEnDescription().toLowerCase().trim().equals(enDescription.toLowerCase()))
			{
				ID=item.getListId();
				return ID;
			}
		}
		}
		
		if(type.equals("1"))
		{
		for (HRListValuesModel item : lstNationality) 
		{
			if(item.getEnDescription().toLowerCase().equals(enDescription.toLowerCase()))
			{
				ID=item.getListId();
				return ID;
			}
		}
		}
		
		if(type.equals("4"))
		{
		for (HRListValuesModel item : lstGender) 
		{
			if(item.getEnDescription().toLowerCase().equals(enDescription.toLowerCase()))
			{
				ID=item.getListId();
				return ID;
			}
		}
		}
		
		if(type.equals("9"))
		{
		for (HRListValuesModel item : lstMarital) 
		{
			if(item.getEnDescription().toLowerCase().equals(enDescription.toLowerCase()))
			{
				ID=item.getListId();
				return ID;
			}
		}
		}
		
		if(type.equals("25"))
		{
		for (HRListValuesModel item : lstReligion) 
		{
			if(item.getEnDescription().toLowerCase().equals(enDescription.toLowerCase()))
			{
				ID=item.getListId();
				return ID;
			}
		}
		}
		
		return ID;
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

	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public String getAttFile() {
		return attFile;
	}

	public void setAttFile(String attFile) {
		this.attFile = attFile;
	}

	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	public List<EmployeeModel> getLstEmployeeData() {
		return lstEmployeeData;
	}

	public void setLstEmployeeData(List<EmployeeModel> lstEmployeeData) {
		this.lstEmployeeData = lstEmployeeData;
	}

	public boolean isCanPreview() {
		return canPreview;
	}

	public void setCanPreview(boolean canPreview) {
		this.canPreview = canPreview;
	}

	public List<HRListValuesModel> getLstNationality() {
		return lstNationality;
	}

	public void setLstNationality(List<HRListValuesModel> lstNationality) {
		this.lstNationality = lstNationality;
	}

	public List<HRListValuesModel> getLstGender() {
		return lstGender;
	}

	public void setLstGender(List<HRListValuesModel> lstGender) {
		this.lstGender = lstGender;
	}

	public List<HRListValuesModel> getLstMarital() {
		return lstMarital;
	}

	public void setLstMarital(List<HRListValuesModel> lstMarital) {
		this.lstMarital = lstMarital;
	}

	public List<HRListValuesModel> getLstReligion() {
		return lstReligion;
	}

	public void setLstReligion(List<HRListValuesModel> lstReligion) {
		this.lstReligion = lstReligion;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
