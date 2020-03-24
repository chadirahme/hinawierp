package list;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import layout.MenuModel;
import model.CustomerModel;
import model.DataFilter;
import model.HRListFieldsModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;
import timesheet.TimeSheetData;

public class ListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	ListData data=new ListData();
	
	private ListModelList<HRListFieldsModel> lstHRFields;
	private HRListFieldsModel selectedHRFields;
	private ListModelList<HRListValuesModel> lstHRValues;
	private ListModelList<HRListValuesModel> lstAllHRValues=new ListModelList<HRListValuesModel>();
	
	private DataFilter filter = new DataFilter();
	
	private ListModelList<HRListValuesModel> lstParentHRFields;
	private HRListValuesModel selectedParentHRFields;
	
	private  String listType;
	private int passFieldId;
	private String passFieldName;
	private String title;
	
	private String language;
	private String selectName;
	private String msgAddNewone;
	private String msgAddMainField;
	private String msgValueExists;
	private String msgPriority;
	private String msgSaved;
	private String msgValueUsed;
	private String msgConfirmDelete;
	
	int menuID=207;
	private MenuModel companyRole;
	private Date lastModified;
	//Hi
	
	@Init
    public void init(@BindingParam("type") String type)
	{
		listType=type;	
		try
		{					 	        
			Session sess = Sessions.getCurrent();
			String sessLang= (String) sess.getAttribute("language");
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			
			if(sessLang==null)
			{
				sessLang="en";
				Sessions.getCurrent().setAttribute("language", sessLang);
			}
			language=sessLang;
			loadLabels();
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			String fieldID=Executions.getCurrent().getParameter("id") ;
			logger.info("url fieldID>> "  + fieldID); 
			fieldID=fieldID==null?"0":fieldID;
			passFieldId=Integer.parseInt(fieldID);
			 selectName=language.equals("en")? "Select" : "Ø§Ø®ØªØ§Ø±" ;
			
			if(passFieldId==0)
			{
				title=language.equals("en") ? "General List" : "Ø§Ù„Ù‚Ø§Ø¦Ù…Ø© Ø§Ù„Ø¹Ø§Ù…Ø©";
				
				if(listType.equals("general"))
				{
				lstHRFields=new ListModelList<HRListFieldsModel>( data.fillHRFields(selectName ));
				if(lstHRFields.size()>0)
					selectedHRFields=lstHRFields.get(0);
				}
			}
			else
			{
				//use to get last modifed date
				 List<HRListFieldsModel> tmpLst=data.fillHRFields("");
			  if(passFieldId==6)
			  {				  
				  title=language.equals("en") ? "Departments List" : "Ù‚Ø§Ø¦Ù…Ø© Ø§Ù„Ø¥Ø¯Ø§Ø±Ø§Øª" ;
				  passFieldName="DEPARTMENT";
				  for (HRListFieldsModel item : tmpLst) 
				  {
					if(item.getFieldId()==passFieldId)
					{
						lastModified=item.getLastModified();
					}
				  }
			  }
			  if(passFieldId==7)
			  {
				 title=language.equals("en") ?"Positions List":"Ù‚Ø§Ø¦Ù…Ø© Ø§Ù„ÙˆØ¸Ø§Ø¦Ù�";
				passFieldName="POSITION";
				for (HRListFieldsModel item : tmpLst) 
				  {
					if(item.getFieldId()==passFieldId)
					{
						lastModified=item.getLastModified();
					}
				  }
			  }
			  
			  lstHRValues=new ListModelList<HRListValuesModel>( data.getHRListValues(passFieldId,"",0));
			  lstAllHRValues.addAll(lstAllHRValues);
			}
						
		}
		catch (Exception ex) 
		{
			logger.error("error in ListViewModel---Init-->" , ex);
		}
	}
	
	private ListModelList<HRListValuesModel> filterData() {
		lstHRValues.clear();
		lstHRValues.addAll(lstAllHRValues);
		ListModelList<HRListValuesModel> lst = new ListModelList<HRListValuesModel>();
		for (Iterator<HRListValuesModel> i = lstHRValues.iterator(); i.hasNext();) {
			HRListValuesModel tmp = i.next();
			if (tmp.getEnDescription().toLowerCase().contains(filter.getEnDescription().toLowerCase())
					&&
					tmp.getArDescription().toLowerCase().contains(filter.getArDescription().toLowerCase())
					&& 
					tmp.getNotes().toLowerCase().contains(filter.getNotes().toLowerCase())
					&&(tmp.getPriorityId()+"").toLowerCase()
							.contains(filter.getPriorityId().toLowerCase())) {
				lst.add(tmp);
			}
		}
		return lst;

	}
	
	@Command
	@NotifyChange({ "lstHRValues"})
	public void changeFilter() {
		try {
			lstHRValues =  filterData();

		} catch (Exception ex) {
			logger.error("error in ListViewModel---changeFilter-->", ex);
		}

	}
	
	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		TimeSheetData data=new TimeSheetData();
		List<MenuModel> lstRoles= data.getTimeSheetRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	private void loadLabels()
	{		
		try
		{
		
			if(language.equals("ar"))
			 {
				Properties dbProps = new Properties(); 
				 URL resource = getClass().getResource("/");
				 String path = resource.getPath();
				 path = path.replace("WEB-INF/classes/", "WEB-INF/");
				 
				  FileInputStream fis = new FileInputStream(path + "labels/lists_ar.properties");
			      InputStreamReader isr = new InputStreamReader(fis, "UTF8");		       
				 //InputStream is =new FileInputStream(path + "labels/lists_ar.properties");  
		         dbProps.load(isr);
		         msgAddNewone=dbProps.getProperty("general.msgAddNewOne");
		         msgAddMainField=dbProps.getProperty("general.msgAddMainField");
		         msgValueExists=dbProps.getProperty("general.msgValueExists");
		         msgPriority=dbProps.getProperty("general.msgPriority");
		         msgSaved=dbProps.getProperty("general.msgSaved");
		         msgValueUsed=dbProps.getProperty("general.msgValueUsed");
		         msgConfirmDelete=dbProps.getProperty("general.msgConfirmDelete");		         
		         isr.close();
		         
			 }
			else
			{
				msgAddNewone="Please save the added value before add New one";
				msgAddMainField="Please select the Main field";
				msgValueExists="already exists";
				msgPriority="Priority cannot be Duplicate";
				msgSaved="Value is saved";
				msgValueUsed="You can't delete this value. Value is used";
				msgConfirmDelete="Are you sure to delete this value ?";
			}
			
		}
		catch (Exception ex) 
		{
			logger.error("error in ListViewModel---loadLabels-->" , ex);
		}
	}
	public ListViewModel()	
	{
		
	}
	
	@Command
	@NotifyChange({"lstHRValues"})
    public void changeEditableStatus(@BindingParam("row") HRListValuesModel item) 
	{
		item.setEditingStatus(!item.isEditingStatus());		
    }
	
	@Command
	@NotifyChange({"lstHRValues"})
	public void addNewCommand()
	{
		for (HRListValuesModel item : lstHRValues) 
		{
			if(item.getListId()==0)
			{
				Messagebox.show(msgAddNewone,title, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}	
		}
		
		if(passFieldId==0)
		{
		if(selectedHRFields.getParentlistId()>0)
		{
			if(selectedParentHRFields.getListId()==0)
			{
			Messagebox.show(msgAddMainField ,title, Messagebox.OK , Messagebox.EXCLAMATION);
			return;
			}
		}
	  }
		
		HRListValuesModel obj=new HRListValuesModel();
		obj.setListId(0);
		obj.setEnDescription("");
		obj.setArDescription("");		
		obj.setEditingStatus(true);
		obj.setQbListId("NOTPOSTED");
		if(passFieldId==0)
		{
		obj.setFieldId(selectedHRFields.getFieldId());
		obj.setFieldName(selectedHRFields.getFieldName());
		}
		else
		{
			obj.setFieldId(passFieldId);			
			obj.setFieldName(passFieldName);			
		}
		//get new priority
		int priorityId=0;
		for (HRListValuesModel item : lstHRValues)
		{
			if(item.getPriorityId()>priorityId)
				priorityId=item.getPriorityId();
		}
		obj.setPriorityId(priorityId+1);
		lstHRValues.add(0,obj);
	}
	
	@Command
	@NotifyChange({"lstHRValues"})
	public void confirm(@BindingParam("row") HRListValuesModel item) 
	{
		item.setEnDescription(item.getEnDescription().replace("'", "`"));
		for (HRListValuesModel obj : lstHRValues) 
		{
			if(obj.getListId()!=item.getListId())
			{
				if(item.getEnDescription().trim().toLowerCase().equals(obj.getEnDescription().trim().toLowerCase()))
				{
					Messagebox.show(item.getEnDescription() + " " + msgValueExists,title, Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(item.getPriorityId()==obj.getPriorityId())
				{
					Messagebox.show(msgPriority,title, Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		if(passFieldId==0)
		{
		if(selectedHRFields.getParentlistId()>0)
		{
			if(selectedParentHRFields.getListId()==0)
			{
			Messagebox.show(msgAddMainField,title, Messagebox.OK , Messagebox.EXCLAMATION);
			return;
			}
		}
		
		
		int subId=0;
		if(selectedHRFields.getParentlistId()>0)
			subId=selectedParentHRFields.getListId();
		
		if(item.getListId()==0)
		{				
		data.addNewHRListValue( item.getEnDescription(), item.getArDescription(),selectedHRFields.getFieldName(),selectedHRFields.getFieldId(),item.getPriorityId(),subId,item.getNotes());		
		}
		else
		{
		data.updateHRListValue(item.getListId(), item.getEnDescription(), item.getArDescription(),item.getPriorityId(),item.getNotes(),selectedHRFields.getFieldId());	
		}
		Clients.showNotification(msgSaved);
		
		lstHRValues=new ListModelList<HRListValuesModel>(data.getHRListValues(selectedHRFields.getFieldId(),"",subId));
		}
		
		else
		{
			if(item.getListId()==0)
			{				
			data.addNewHRListValue( item.getEnDescription(), item.getArDescription(),passFieldName,passFieldId,item.getPriorityId(),0,item.getNotes());		
			}
			else
			{
			data.updateHRListValue(item.getListId(), item.getEnDescription(), item.getArDescription(),item.getPriorityId(),item.getNotes(),passFieldId);	
			}
			Clients.showNotification(msgSaved);
			
			lstHRValues=new ListModelList<HRListValuesModel>(data.getHRListValues(passFieldId,"",0));
		}
		
		
	}
	
	@Command
	@NotifyChange({"lstHRValues"})
	public void deleteCommand(@BindingParam("row") final HRListValuesModel item) 
	{
		
		 if(!item.getQbListId().equals("NOTPOSTED"))
		  {
			 Messagebox.show(msgValueUsed,title, Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		  }
		 else if(CheckListUsed(item))
		 {
			 Messagebox.show(msgValueUsed,title, Messagebox.OK , Messagebox.EXCLAMATION);	 
			 return;
		 }
		 
		Messagebox.show(msgConfirmDelete,title,Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		{
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
              {								
				if(item.getListId()>0)
					data.deleteHRListValue(item.getListId());
				lstHRValues.remove(item);
								
              }
			 }
		 });	
	}
	
	private boolean CheckListUsed(HRListValuesModel item)
	{
		boolean isUsed=false;
		String tableName="";
		String fieldName="";
		if(item.getFieldId()==7)//POSITION
		{
			tableName="EMPMAST";
			fieldName="POS_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)			
				return true;
			
			tableName="COMPSTANDPOS";
			fieldName="ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)			
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==6)//DEPARTMENT
		{
			tableName="EMPMAST";
			fieldName="DEP_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)			
				return true;
			tableName="COMPSTANDPOS";
			fieldName="DEP_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)			
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==32)//BANK-NAME
		{
			tableName="EMPMAST";
			fieldName="BANK_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)			
				return true;
			
				return isUsed;
			
		}
		
		if(item.getFieldId()==33)//BANK-BARNCH
		{
			tableName="EMPMAST";
			fieldName="BRANCH_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
				
			return isUsed;				
		}
		if(item.getFieldId()==23)//BUSINESS
		{
			tableName="COMPSETUP";
			fieldName="BUSINESS_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;			
		}
		
		if(item.getFieldId()==3)//CITY
		{
			tableName="EMPMAST";
			fieldName="CURRENT_CITY";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="EMPMAST";
			fieldName="PERMANENT_CITY";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="COMPSETUP";
			fieldName="CITY_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==48)//CLASSIFICATION
		{
			tableName="EMPMAST";
			fieldName="CLASS_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="COMPSTANDPOS";
			fieldName="CLASS_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==12)//CONTACTYPE
		{
			tableName="EMPCONTACT";
			fieldName="CONTACT_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==2)//COUNTRY
		{
			tableName="EMPMAST";
			fieldName="CURRENT_COUNTRY";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="EMPMAST";
			fieldName="PERMANENT_COUNTRY";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="COMPSETUP";
			fieldName="COUNTRY_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==22)//DOCUMENTS
		{
			tableName="COMPDOCUMENTS";
			fieldName="DOCTYPE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==10)//EDULEVEL
		{
			tableName="EMPEDUCATION";
			fieldName="EDUCATION_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==41)//EMP_DOCUMENT_TYPE
		{
			tableName="EMPOTHERDOCMAST";
			fieldName="DOC_TYPE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==13)//LANGUAGE
		{
			tableName="EMPLANGUAGE";
			fieldName="LANGUAGE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==17)//LEAVETYPE
		{
			tableName="EMPLEAVE";
			fieldName="LEAVE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==27)//LEVEL
		{
			tableName="EMPMAST";
			fieldName="CLEVEL";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="COMPSTANDPOS";
			fieldName="GRADE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==1)//NATIONALITY
		{
			tableName="EMPMAST";
			fieldName="NAT_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==25)//RELIGION
		{
			tableName="EMPMAST";
			fieldName="RELIGION_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==37)//SALARY_ADDITIONS
		{
			tableName="EMPADTRX";
			fieldName="AD_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==38)//SALARY_DEDUCTIONS
		{
			tableName="EMPADTRX";
			fieldName="AD_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==49)//SECTION
		{
			tableName="EMPMAST";
			fieldName="SECTION_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="COMPSTANDPOS";
			fieldName="SECTION_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==14)//SKILLS
		{
			tableName="EMPSKILLS";
			fieldName="SKILL_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==28)//STATUS
		{
			tableName="EMPMAST";
			fieldName="STATUS";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==30)//DEF-ALLOWANCES
		{
			tableName="EMPSALARY";
			fieldName="ALLOWANCE_TYPE";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="SALARYALLOWANCE";
			fieldName="ALLOWANCE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==31)//OTH-ALLOWANCES
		{
			tableName="EMPSALARY";
			fieldName="ALLOWANCE_TYPE";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="SALARYALLOWANCE";
			fieldName="ALLOWANCE_ID";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="HRCONDITIONS";
			fieldName="Salary_Item";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==53)//FORMS
		{
			tableName="FORMMAST";
			fieldName="FORM_KEY";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			tableName="HRFORMS";
			fieldName="FORM_KEY";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			
			return isUsed;
		}
		
		if(item.getFieldId()==141)//Task Type
		{
			tableName="Tasks";
			fieldName="tasktype";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==142)//Task Priority
		{
			tableName="Tasks";
			fieldName="priorityrefKey";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		if(item.getFieldId()==143)//Task Status
		{
			tableName="Tasks";
			fieldName="status";
			if(data.checkHRListValueUsed(tableName, fieldName, item.getListId())>0)
				return true;
			
			return isUsed;
		}
		
		//if(tableName.equals(""))
		//	return true;				
		return isUsed;
	}

	public ListModelList<HRListFieldsModel> getLstHRFields() {
		return lstHRFields;
	}

	public void setLstHRFields(ListModelList<HRListFieldsModel> lstHRFields) {
		this.lstHRFields = lstHRFields;
	}

	public ListModelList<HRListValuesModel> getLstHRValues() {
		return lstHRValues;
	}

	public void setLstHRValues(ListModelList<HRListValuesModel> lstHRValues) {
		this.lstHRValues = lstHRValues;
	}

	public HRListFieldsModel getSelectedHRFields() {
		return selectedHRFields;
	}

	@NotifyChange({"lstHRValues","selectedHRFields","lstParentHRFields","selectedParentHRFields","lastModified"})
	public void setSelectedHRFields(HRListFieldsModel selectedHRFields) 
	{
		this.selectedHRFields = selectedHRFields;
		lstHRValues=new ListModelList<HRListValuesModel>();
		lstParentHRFields=new ListModelList<HRListValuesModel>();
		lastModified=selectedHRFields.getLastModified();
		if(selectedHRFields.getParentlistId()>0)
		{
			lstParentHRFields=new ListModelList<HRListValuesModel>(data.getHRListValues(selectedHRFields.getParentlistId(),selectName,0));
			if(lstParentHRFields.size()>0)
				selectedParentHRFields=lstParentHRFields.get(0);
		}
		else
		{
		lstHRValues=new ListModelList<HRListValuesModel>( data.getHRListValues(selectedHRFields.getFieldId(),"",0));
		lstAllHRValues.clear();
		lstAllHRValues.addAll(lstHRValues);
		}
	}

	public ListModelList<HRListValuesModel> getLstParentHRFields() {
		return lstParentHRFields;
	}

	public void setLstParentHRFields(ListModelList<HRListValuesModel> lstParentHRFields) {
		this.lstParentHRFields = lstParentHRFields;
	}

	public HRListValuesModel getSelectedParentHRFields() {
		return selectedParentHRFields;
	}

	@NotifyChange({"lstHRValues"})
	public void setSelectedParentHRFields(HRListValuesModel selectedParentHRFields) 
	{
		this.selectedParentHRFields = selectedParentHRFields;
		lstHRValues=new ListModelList<HRListValuesModel>();
		lstHRValues=new ListModelList<HRListValuesModel>( data.getHRListValues(selectedHRFields.getFieldId(),"",selectedParentHRFields.getListId()));
		lstAllHRValues.clear();
		lstAllHRValues.addAll(lstHRValues);
	}

	public int getPassFieldId() {
		return passFieldId;
	}

	public void setPassFieldId(int passFieldId) {
		this.passFieldId = passFieldId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the lstAllHRValues
	 */
	public ListModelList<HRListValuesModel> getLstAllHRValues() {
		return lstAllHRValues;
	}

	/**
	 * @param lstAllHRValues the lstAllHRValues to set
	 */
	public void setLstAllHRValues(ListModelList<HRListValuesModel> lstAllHRValues) {
		this.lstAllHRValues = lstAllHRValues;
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

	
}
