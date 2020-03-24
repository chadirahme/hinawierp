package home;

import hba.HBAQueries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import layout.MenuModel;
import list.ListData;
import model.CompanyDBModel;
import model.CustomerFeedbackModel;
import model.CustomerModel;
import model.HRListValuesModel;
import model.SerialFields;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.Clients;
//import org.zkoss.zkmax.zul.Chosenbox;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import common.HREnum;
import db.DBHandler;
import db.SQLDBHandler;
import setup.users.WebusersModel;
import timesheet.TimeSheetData;

public class NotesForEachModule {
	private Logger logger = Logger.getLogger(this.getClass());

	//CustomerFeedBackData feedBackData=null;

	ListData data=new ListData();

	private String attFile4;

	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	DecimalFormat dcf=new DecimalFormat("0.00");

	int notesID=0;

	private String memoEn="";

	private String memoAr="";

	private Date memoCreatedDate;

	private Date memoModifiedDate;

	private String createdDateStr="";

	private String modifeldDateStr="";

	private List<HRListValuesModel> lstService;

	private List<HRListValuesModel> lstlocalItem;
	
	private HRListValuesModel selectedService;

	private CustomerFeedbackModel selectedNotesForEachModule=new CustomerFeedbackModel();
	
	List<CustomerFeedbackModel> listNotesForEachModuleHistory=new ArrayList<CustomerFeedbackModel>();
	
	private CustomerFeedbackModel selectedNotesForEachModuleHistory=new CustomerFeedbackModel();

	private int webuserID=0;

	WebusersModel dbUser=null;

	private int supervisorID=0;

	private int employeeKey=0;

	private boolean adminUser;

	private MenuModel companyRole;

	private boolean canSave;

	private List<QuotationAttachmentModel> lstAtt=new ArrayList<QuotationAttachmentModel>();
	private QuotationAttachmentModel selectedAttchemnets=new QuotationAttachmentModel();

	private String emailTemplateName="";
	String viewType="";

	@Init
    public void init(@BindingParam("type") String type)
 	{ 		
 		try
		{
 			logger.info("type> Init>> "+ type);
 	 		viewType=type; 	
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			Session sess = Sessions.getCurrent();		
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			HBAQueries query = new HBAQueries();
			rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
					.getCompanyid()));
			while (rs.next()) {
				obj.setCompanyId(rs.getInt("companyid"));
				obj.setDbid(rs.getInt("dbid"));
				obj.setUserip(rs.getString("userip"));
				obj.setDbname(rs.getString("dbname"));
				obj.setDbuser(rs.getString("dbuser"));
				obj.setDbpwd(rs.getString("dbpwd"));
				obj.setDbtype(rs.getString("dbtype"));
			}
		//	feedBackData=new CustomerFeedBackData(obj);


			supervisorID=dbUser.getSupervisor();//logged in as supervisor
			employeeKey=dbUser.getEmployeeKey();
			if(employeeKey>0)
				supervisorID=employeeKey;//logged in as employee

			if(supervisorID>0)
				webuserID=supervisorID;

			if(viewType.equals("hritem"))
			{
			//lstService=feedBackData.getHRListValuesForFeedBack(147,"Select");
			if(lstService!=null && lstService.size()>0)
				selectedService=lstService.get(0);
			}
			else
			{
			//lstlocalItem=feedBackData.getLocalItemListValuesForFeedBack(38, "Select");
			if(lstlocalItem!=null && lstlocalItem.size()>0)
				selectedService=lstlocalItem.get(0);
			
			}
			memoAr="";
			memoEn="";

			/*if(selectedService!=null && selectedService.getListId()>0)
			{
				selectedNotesForEachModule=feedBackData.getNotesForModules(selectedService.getListId());
				memoAr=selectedNotesForEachModule.getMemoAr();
				memoEn=selectedNotesForEachModule.getMemoEn();

			}*/

		}
		catch(Exception e)
		{
			logger.error("ERROR in NotesForEachModule ----> init", e);			
		}
    }

	public NotesForEachModule()
	{
		

	}

	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		TimeSheetData data=new TimeSheetData();
		List<MenuModel> lstRoles= data.getRolesPermessions(companyRoleId,HREnum.MenuIds.CRM.getValue());
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==HREnum.MenuIds.CRMTemplates.getValue())
			{
				companyRole=item;
				break;
			}
		}
	}
	

	@Command
	public void saveCustomerFeedbackSend()
	{
		try {
			int result=0;
			CustomerFeedbackModel feedbackModel=new CustomerFeedbackModel();

			if(notesID>0)
				feedbackModel.setNoteID(notesID);
			else
				//feedbackModel.setNoteID(feedBackData.getMaxID("NotesForEachModule", "noteId"));


			if(selectedService!=null)
				feedbackModel.setServiceRefKey(selectedService.getListId());
			else
				feedbackModel.setServiceRefKey(0);

			Calendar c = Calendar.getInstance();		

			feedbackModel.setFeedBackModifiedDate(df.parse(sdf.format(c.getTime())));

			feedbackModel.setFeedbackCreateDate(df.parse(sdf.format(c.getTime())));

			feedbackModel.setMemoEn(memoEn);

			feedbackModel.setMemoAr(memoAr);

			feedbackModel.setWebuserID(webuserID);


			if(notesID>0)
			{
				//result=feedBackData.editNotesForEachModule(feedbackModel,lstAtt);
				Messagebox.show("Notes  Updated sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();			  
			}
			else
			{
				//result=feedBackData.saveNotesForEachModule(feedbackModel,lstAtt);
				Messagebox.show("Notes created sucessfully","Task",Messagebox.OK , Messagebox.INFORMATION);
				Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				Center center = bl.getCenter();
				Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				tabbox.getSelectedPanel().getLastChild().invalidate();		
			}

		} catch (Exception e) {
			logger.error("ERROR in NotesForEachModule ----> saveCustomerFeedbackSend", e);	
		}

	}



	@Command
	@NotifyChange({"lstService","emailTemplateName","selectedService"})	
	public void saveNewEmailTemplate(@BindingParam("row") String description) 
	{
		try {
			int resutlt=0;
			if(description!=null && !description.trim().equalsIgnoreCase(""))
			{

				for (HRListValuesModel obj : lstService) 
				{
					if(obj.getEnDescription().trim().toLowerCase().equals(description.trim().toLowerCase()))
					{
						Clients.showNotification("The Name Already Exists",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						return;
					}
				}
				HRListValuesModel item=new HRListValuesModel();
				item.setEnDescription(description.replace("'", "`"));
				item.setArDescription("");
				item.setPriorityId(0);
				item.setNotes("");
				resutlt=data.addNewHRListValue(item.getEnDescription(), item.getArDescription(),"EMAILTEMPLATE",147,item.getPriorityId(),0,item.getNotes());
				//resutlt=data.addNewLocalItemValue(item.getEnDescription(), item.getEnDescription(), 38,memoEn,memoAr,dbUser.getDesktopUserid());
				if(resutlt>0)
				{
					Clients.showNotification("Saved Succesfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				//	lstService=feedBackData.getLocalItemListValuesForFeedBack(38, "Select");
					if(lstService!=null && lstService.size()>0)
						selectedService=lstService.get(0);
					//lstService=feedBackData.getHRListValuesForFeedBack(147,"Select");
					emailTemplateName="";
				}
				else
				{
					Clients.showNotification("Coundn't save the value.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					return;
				}
			}
			else
			{
				Clients.showNotification("Please Enter The Value",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
		}
		catch (Exception e) {
			logger.error("ERROR in NotesForEachModule ----> saveNewEmailTemplate", e);	
		}
	}


	
	@Command
	@NotifyChange({"lstlocalItem","emailTemplateName","selectedService"})	
	public void saveLetterTemplateCommand(@BindingParam("row") String description) 
	{
		try 
		{
			int resutlt=0;
			if(selectedService.getListId()==0)
			{
			if(description!=null && !description.trim().equalsIgnoreCase(""))
			{

				for (HRListValuesModel obj : lstlocalItem) 
				{
					if(obj.getFieldName().trim().toLowerCase().equals(description.trim().toLowerCase()))
					{
						Clients.showNotification("The Name Already Exists",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						return;
					}
				}
				HRListValuesModel item=new HRListValuesModel();
				item.setEnDescription(description.replace("'", "`"));
				item.setArDescription("");
				item.setPriorityId(0);
				item.setNotes("");
				//resutlt=data.addNewHRListValue(item.getEnDescription(), item.getArDescription(),"EMAILTEMPLATE",147,item.getPriorityId(),0,item.getNotes());				
				resutlt=data.addNewLocalItemValue(item.getEnDescription(), item.getEnDescription(), 38,memoEn,memoAr,dbUser.getDesktopUserid());							
				
			}
			else
			{
				Clients.showNotification("Please Enter The Value",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
		  }
			else
			{
				//edit
				CustomerFeedbackModel feedbackModel=new CustomerFeedbackModel();					
				feedbackModel.setNoteID(selectedService.getListId());									
				Calendar c = Calendar.getInstance();		
				feedbackModel.setFeedBackModifiedDate(df.parse(sdf.format(c.getTime())));
				feedbackModel.setMemoEn(memoEn);
				feedbackModel.setMemoAr(memoAr);
				feedbackModel.setWebuserID(dbUser.getDesktopUserid());
				
				//resutlt=feedBackData.editLocalItem(feedbackModel);
			}
			
			if(resutlt>0)
			{
				Clients.showNotification("Saved Succesfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				//lstlocalItem=feedBackData.getLocalItemListValuesForFeedBack(38, "Select");
				if(lstlocalItem!=null && lstlocalItem.size()>0)
					selectedService=lstlocalItem.get(0);
				//lstService=feedBackData.getHRListValuesForFeedBack(147,"Select");
				emailTemplateName="";
			}
			else
			{
				Clients.showNotification("Coundn't save the value.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			
			
		}
		catch (Exception e) {
			logger.error("ERROR in NotesForEachModule ----> saveNewEmailTemplate", e);	
		}
	}


	/**
	 * @return the memoEn
	 */
	public String getMemoEn() {
		return memoEn;
	}


	/**
	 * @param memoEn the memoEn to set
	 */
	public void setMemoEn(String memoEn) {
		this.memoEn = memoEn;
	}


	/**
	 * @return the memoAr
	 */
	public String getMemoAr() {
		return memoAr;
	}


	/**
	 * @param memoAr the memoAr to set
	 */
	public void setMemoAr(String memoAr) {
		this.memoAr = memoAr;
	}


	/**
	 * @return the memoCreatedDate
	 */
	public Date getMemoCreatedDate() {
		return memoCreatedDate;
	}


	/**
	 * @param memoCreatedDate the memoCreatedDate to set
	 */
	public void setMemoCreatedDate(Date memoCreatedDate) {
		this.memoCreatedDate = memoCreatedDate;
	}


	/**
	 * @return the memoModifiedDate
	 */
	public Date getMemoModifiedDate() {
		return memoModifiedDate;
	}


	/**
	 * @param memoModifiedDate the memoModifiedDate to set
	 */
	public void setMemoModifiedDate(Date memoModifiedDate) {
		this.memoModifiedDate = memoModifiedDate;
	}


	/**
	 * @return the lstService
	 */
	public List<HRListValuesModel> getLstService() {
		return lstService;
	}


	/**
	 * @param lstService the lstService to set
	 */
	public void setLstService(List<HRListValuesModel> lstService) {
		this.lstService = lstService;
	}


	/**
	 * @return the selectedService
	 */
	public HRListValuesModel getSelectedService() {
		return selectedService;
	}


	/**
	 * @param selectedService the selectedService to set
	 */
	@NotifyChange({"memoAr","memoEn","modifeldDateStr","lstAtt","listNotesForEachModuleHistory"})
	public void setSelectedService(HRListValuesModel selectedService) 
	{
		this.selectedService = selectedService;
		if(viewType.equals("localitem"))
		{
		memoEn=selectedService.getEnDescription();
		memoAr=selectedService.getArDescription();
		modifeldDateStr=selectedService.getModifeldDateStr();
		}
		else
		{
			if(selectedService!=null && selectedService.getListId()>0)
			{
				lstAtt.clear();
				lstAtt=new ArrayList<QuotationAttachmentModel>();
				//selectedNotesForEachModule=feedBackData.getNotesForModules(selectedService.getListId());
				//listNotesForEachModuleHistory=feedBackData.getNotesForEachModuleHistory(selectedService.getListId());
				if(listNotesForEachModuleHistory.size()>0)
				selectedNotesForEachModuleHistory=listNotesForEachModuleHistory.get(0);
				if(selectedNotesForEachModule!=null)
				{
					notesID=selectedNotesForEachModule.getNoteID();
					memoAr=selectedNotesForEachModule.getMemoAr();
					memoEn=selectedNotesForEachModule.getMemoEn();
					createdDateStr=selectedNotesForEachModule.getCreatedDateStr();
					modifeldDateStr=selectedNotesForEachModule.getModifeldDateStr();
				//	lstAtt=feedBackData.getNotesForModulesAttchamnet(notesID);
				}
				else
				{
					notesID=0;
					memoAr="";
					memoEn="";
					lstAtt=new ArrayList<QuotationAttachmentModel>();
				}
			}
		}
	}


	/**
	 * @return the createdDateStr
	 */
	public String getCreatedDateStr() {
		return createdDateStr;
	}


	/**
	 * @param createdDateStr the createdDateStr to set
	 */
	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}


	/**
	 * @return the modifeldDateStr
	 */
	public String getModifeldDateStr() {
		return modifeldDateStr;
	}


	/**
	 * @param modifeldDateStr the modifeldDateStr to set
	 */
	public void setModifeldDateStr(String modifeldDateStr) {
		this.modifeldDateStr = modifeldDateStr;
	}



	@Command 
	@NotifyChange({"attFile4","lstAtt"})
	public void uploadFile(BindContext ctx,@BindingParam("attId") String attId )
	{
		try {
			UploadEvent event = (UploadEvent)ctx.getTriggerEvent();	
			if(lstAtt!=null && lstAtt.size()>=10)
			{
				Clients.showNotification("The you can upload maximum 10 images per task.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;

			}

			for(QuotationAttachmentModel attachmentModel:lstAtt)
			{
				if(attachmentModel.getFilename().equalsIgnoreCase(event.getMedia().getName()))
				{
					Clients.showNotification("The file already uploaded please select another file.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
					return;
				}

			}

			String filePath="";
			String repository = System.getProperty("catalina.base")+File.separator+"uploads"+File.separator;
			Session sess = Sessions.getCurrent();
			String sessID=(Executions.getCurrent()).getDesktop().getId();
			logger.info("sessionId >>>>>>" + (Executions.getCurrent()).getDesktop().getId());
			String dirPath=repository+sessID;//session.getId();
			/*File dir = new File(dirPath);
		if(!dir.exists())
			dir.mkdirs();*/
			filePath=repository+"NotesAndGerenralNotes"+File.separator+notesID+File.separator+event.getMedia().getName();	 
			if(attId.equals("4"))
			{
				attFile4=event.getMedia().getName();
				QuotationAttachmentModel objAtt=new QuotationAttachmentModel();
				objAtt.setFilename(attFile4);
				objAtt.setFilepath(filePath);
				objAtt.setSessionid(sessID);
				objAtt.setImageMedia(event.getMedia());
				lstAtt.add(objAtt);
				if(lstAtt!=null && lstAtt.size()>0)
					selectedAttchemnets=lstAtt.get(0);
			}
		}
		catch (Exception e) {
			logger.error("ERROR in NotesForEachModule ----> uploadFile", e);			
		}
	}


	@Command 
	@NotifyChange({"attFile4","lstAtt"})
	public void deleteFromAttchamentList(@BindingParam("row") QuotationAttachmentModel obj)
	{
		try {
			QuotationAttachmentModel tempModel=new QuotationAttachmentModel();
			for(QuotationAttachmentModel attachmentModel:lstAtt)
			{
				if(attachmentModel.getFilename().equalsIgnoreCase(obj.getFilename()))
				{
					tempModel=attachmentModel;
					break;
				}

			}
			lstAtt.remove(tempModel);
		}
		catch (Exception e) {
			logger.error("ERROR in NotesForEachModule ----> deleteFromAttchamentList", e);			
		}
	}




	@Command
	public void download(@BindingParam("row") QuotationAttachmentModel obj)
	{
		if(obj!=null && !obj.getFilepath().equalsIgnoreCase(""))
		{
			File file=new File(obj.getFilepath());
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String mimeType=mimeTypesMap.getContentType(file);

			try {
				Filedownload.save(org.apache.commons.io.FileUtils.readFileToByteArray(file), mimeType, obj.getFilename()); 

			}catch (FileNotFoundException e)
			{
				Clients.showNotification("There Is No Such File in server to download.(May be Deleted)",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			catch (Exception e) {
				logger.error("ERROR in NotesForEachModule ----> download", e);	
			}

		}
		else
		{
			Clients.showNotification("There Is No File to download.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
	} 


	/**
	 * @return the lstAtt
	 */
	public List<QuotationAttachmentModel> getLstAtt() {
		return lstAtt;
	}


	/**
	 * @param lstAtt the lstAtt to set
	 */
	public void setLstAtt(List<QuotationAttachmentModel> lstAtt) {
		this.lstAtt = lstAtt;
	}


	/**
	 * @return the selectedAttchemnets
	 */
	public QuotationAttachmentModel getSelectedAttchemnets() {
		return selectedAttchemnets;
	}


	/**
	 * @param selectedAttchemnets the selectedAttchemnets to set
	 */
	public void setSelectedAttchemnets(QuotationAttachmentModel selectedAttchemnets) {
		this.selectedAttchemnets = selectedAttchemnets;
	}


	/**
	 * @return the emailTemplateName
	 */
	public String getEmailTemplateName() {
		return emailTemplateName;
	}


	/**
	 * @param emailTemplateName the emailTemplateName to set
	 */
	public void setEmailTemplateName(String emailTemplateName) {
		this.emailTemplateName = emailTemplateName;
	}

	/**
	 * @return the listNotesForEachModuleHistory
	 */
	public List<CustomerFeedbackModel> getListNotesForEachModuleHistory() {
		return listNotesForEachModuleHistory;
	}


	/**
	 * @param listNotesForEachModuleHistory the listNotesForEachModuleHistory to set
	 */
	public void setListNotesForEachModuleHistory(
			List<CustomerFeedbackModel> listNotesForEachModuleHistory) {
		this.listNotesForEachModuleHistory = listNotesForEachModuleHistory;
	}


	/**
	 * @param selectedNotesForEachModuleHistory the selectedNotesForEachModuleHistory to set
	 */
	@NotifyChange({"memoAr","memoEn","modifeldDateStr","lstAtt"})
	public void setSelectedNotesForEachModuleHistory(
			CustomerFeedbackModel selectedNotesForEachModuleHistory) {
		this.selectedNotesForEachModuleHistory = selectedNotesForEachModuleHistory;
		if(selectedNotesForEachModuleHistory!=null)
		{
			notesID=selectedNotesForEachModuleHistory.getNoteID();
			memoAr=selectedNotesForEachModuleHistory.getMemoAr();
			memoEn=selectedNotesForEachModuleHistory.getMemoEn();
			//lstAtt=feedBackData.getNotesForModulesAttchamnet(notesID);
		}
	}


	/**
	 * @return the selectedNotesForEachModuleHistory
	 */
	public CustomerFeedbackModel getSelectedNotesForEachModuleHistory() {
		return selectedNotesForEachModuleHistory;
	}


	public MenuModel getCompanyRole() {
		return companyRole;
	}


	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public List<HRListValuesModel> getLstlocalItem() {
		return lstlocalItem;
	}

	public void setLstlocalItem(List<HRListValuesModel> lstlocalItem) {
		this.lstlocalItem = lstlocalItem;
	}









}
