package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import layout.MenuModel;
import list.ListData;
import model.HRListValuesModel;
import model.ProjectModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class ProjectListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	HRData hrdata=new HRData();
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private ListModelList<ProjectModel> lstProject;
	List<Integer> lstProjectUsed;
	
	private ListData listdata=new ListData();
	private List<HRListValuesModel> lstStatusHRValues;
	
	private String language;
	
	private String msgTitle;
	private String msgAddNew;
	private String msgCode;
	private String msgProjectName;
	private String msgSave;
	private String msgProjectUsed;
	private String msgProjectTransferUsed;
	private String msgConfirmDelete;
	
	int menuID=19;
	private MenuModel companyRole;
	private int supervisorID;
	private Date lastModified;
	
	public ProjectListViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			String sessLang= (String) sess.getAttribute("language");
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			supervisorID=dbUser.getSupervisor();
			if(sessLang==null)
			{
				sessLang="en";
				Sessions.getCurrent().setAttribute("language", sessLang);
			}
			language=sessLang;
			loadLabels();
			
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			int defaultCompanyId=0;	
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
				selectedCompany=lstComapnies.get(0);
													
			lstProject=new ListModelList<ProjectModel>(data.getProjectList(selectedCompany.getCompKey(),"",false,supervisorID));
			
			lstProjectUsed=data.getProjectUsedInTransfer();
			lstStatusHRValues=listdata.getHRListValues(43, "", 0);
			lastModified=data.getProjectLastModified();
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ProjectListViewModel ----> init", ex);			
		}
	}
	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		
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
		
		 FileInputStream fis = new FileInputStream(path + "labels/timesheet_ar.properties");
	     InputStreamReader isr = new InputStreamReader(fis, "UTF8");		       		
         dbProps.load(isr);
         
         msgTitle=dbProps.getProperty("projectlist.msgTitle");      
         msgAddNew=dbProps.getProperty("projectlist.msgAddNew");
         msgCode=dbProps.getProperty("projectlist.msgCode");
         msgProjectName=dbProps.getProperty("projectlist.msgProjectName");
         msgSave=dbProps.getProperty("projectlist.msgSave");
         msgProjectUsed=dbProps.getProperty("projectlist.msgProjectUsed");
         msgProjectTransferUsed=dbProps.getProperty("projectlist.msgProjectTransferUsed");
         msgConfirmDelete=dbProps.getProperty("projectlist.msgConfirmDelete");
         
         //logger.info("title >> " + msgTitle);
         isr.close();
		 }
		 else
		 {
			 msgTitle="Projects List";	
			 msgAddNew="Please save the added project before add New one";
			 msgCode="Project Code already exists";
			 msgProjectName="Project Name already exists";
			 msgSave="Project is saved";
			 msgProjectUsed="You can't delete this project. Already used";
			 msgProjectTransferUsed="You can't delete this project. Already used in transfer";
			 msgConfirmDelete="Are you sure to delete this project ?";
		 }
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in ProjectListViewModel ----> loadLabels", ex);			
		}		
	}
	
	@Command
	@NotifyChange({"lstProject"})
    public void changeEditableStatus(@BindingParam("row") ProjectModel item) 
	{
		if(item.getProjectKey()==0)
		{
			lstProject.remove(item);
		}
		else
		{
		item.setEditingStatus(!item.isEditingStatus());
		}
		
		//lstProject=new ListModelList<ProjectModel>(data.getProjectList(selectedCompany.getCompKey(),""));	
        //refreshRowTemplate(lcs);
    }

	@Command
	@NotifyChange({"lstProject","lastModified"})
	public void confirm(@BindingParam("row") ProjectModel item) 
	{
		int projectKey=0;
		projectKey=item.getProjectKey();
		for (ProjectModel obj : lstProject) 
		{
			if(obj.getProjectKey()!=item.getProjectKey())
		   {
			if(item.getProjectCode().trim().toLowerCase().equals(obj.getProjectCode().trim().toLowerCase()))
			{
				Messagebox.show(msgCode,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(item.getProjectName().trim().toLowerCase().equals(obj.getProjectName().trim().toLowerCase()))
			{
				Messagebox.show(msgProjectName,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		  }
			if(item.getStartDate()==null)
			{
				Messagebox.show("Start Date is mandetory",msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(item.getEndDate()==null)
			{
				Messagebox.show("End Date is mandetory",msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			if(item.getEndDate()!=null && item.getStartDate()!=null &&  ((int)( (item.getEndDate().getTime() - item.getStartDate().getTime()) / (1000 * 60 * 60 * 24))<0))
			{
				Messagebox.show("End Date should be greater than Start Date.",msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		
		if(item.getProjectKey()==0)
		{			
			item.setCompanyKey(selectedCompany.getCompKey());				
		}
		
		int statusID=0;
		for (HRListValuesModel hrValues : lstStatusHRValues) 
		{
			if(item.isActiveStatus())
			{
				if(hrValues.getEnDescription().equalsIgnoreCase("active"))
				{
					statusID=hrValues.getListId();
					item.setIsActive(hrValues.getEnDescription());
					item.setArActive(hrValues.getArDescription());
				}
			}
			else if(item.isActiveStatus()==false)
			{
				if(hrValues.getEnDescription().equalsIgnoreCase("Inactive"))
				{
					statusID=hrValues.getListId();
					item.setIsActive(hrValues.getEnDescription());
					item.setArActive(hrValues.getArDescription());
				}
			}
		}
		
			item.setStatusId(statusID);	
		
		data.insertNewProject(item);
		changeEditableStatus(item);
		lastModified=data.getProjectLastModified();
		if(projectKey>0)
		{
			Clients.showNotification("The Project Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		}
		else
		{
			Clients.showNotification("The Project Has Been Created Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			
		}
	}	
	
	@Command
	@NotifyChange({"lstProject"})
	public void addNewCommand()
	{
		for (ProjectModel item : lstProject) 
		{
			if(item.getProjectKey()==0)
			{
				Messagebox.show(msgAddNew,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}	
		}
		
		ProjectModel obj=new ProjectModel();
		obj.setProjectKey(0);
		obj.setProjectCode("");
		obj.setProjectName("");
		obj.setProjectArName("");
		obj.setQbListId("NOTPOSTED");
		obj.setEditingStatus(true);
		obj.setActiveStatus(true);
		lstProject.add(0,obj);
	}
	
	@Command
	@NotifyChange({"lstProject"})
	public void deleteCommand(@BindingParam("row") final ProjectModel item)
	{
		 if(!item.getQbListId().equals("NOTPOSTED"))
		 	{
			 Messagebox.show(msgProjectUsed,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);	
			 return;
		 	}
			else if(lstProjectUsed.contains(item.getProjectKey()))
			{
				Messagebox.show(msgProjectTransferUsed,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			}		
		 
		Messagebox.show(msgConfirmDelete,msgTitle,Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
               {					 
				
					if(item.getProjectKey()>0)
						data.deleteProject(item.getProjectKey());
			       lstProject.remove(item);
					
               }
			 }
		 });		
				
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

	@NotifyChange({"lstProject"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;				
		lstProject=null;		
		lstProject=new ListModelList<ProjectModel>(data.getProjectList(selectedCompany.getCompKey(),"",false,supervisorID));			
	}
	
	

	public ListModelList<ProjectModel> getLstProject() {
		return lstProject;
	}

	public void setLstProject(ListModelList<ProjectModel> lstProject) {
		this.lstProject = lstProject;
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

}
