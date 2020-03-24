package timesheet;

import hr.HRData;
import hr.model.SponsorModel;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import layout.MenuModel;
import model.DataFilter;
import model.HRListValuesModel;

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

public class ServiceListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	HRData hrdata=new HRData();
	private ListModelList<HRListValuesModel> lstPositions;
	List<HRListValuesModel> lstAllPositions;
	List<Integer> lstPositionUsed;
	private DataFilter filter=new DataFilter();
	private String language;
	
	private String msgTitle;
	private String msgAddNew;
	private String msgServiceName;
	private String msgSave;
	private String msgServiceUsed;	
	private String msgConfirmDelete;
	
	int menuID=133;
	private MenuModel companyRole;
	
	public ServiceListViewModel()
	{
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
			
			lstPositions=new ListModelList<HRListValuesModel>( data.getHRListValues(47,""));
			lstPositionUsed=data.getServiceUsedInTimesheet();
			lstAllPositions=lstPositions;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ServiceListViewModel ----> init", ex);			
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
         
         msgTitle=dbProps.getProperty("servicelist.msgTitle");      
         msgAddNew=dbProps.getProperty("servicelist.msgAddNew");        
         msgServiceName=dbProps.getProperty("servicelist.msgServiceName");         
         msgSave=dbProps.getProperty("servicelist.msgSave");
         msgServiceUsed=dbProps.getProperty("servicelist.msgServiceUsed");        
         msgConfirmDelete=dbProps.getProperty("servicelist.msgConfirmDelete");             
         isr.close();
		 }
		 else
		 {
			 msgTitle="Services List";	
			 msgAddNew="Please save the added service before add New one";		
			 msgServiceName="Service name already exists";			 
			 msgSave="Service is saved";			 
			 msgServiceUsed="You can't delete this service. Already used";			 
			 msgConfirmDelete="Are you sure to delete this service ?";
		 }
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in ServiceListViewModel ----> loadLabels", ex);			
		}		
	}
	
	@Command
	@NotifyChange({"lstPositions"})
    public void changeEditableStatus(@BindingParam("row") HRListValuesModel item) 
	{
		if(item.getListId()==0)
		{
			  lstPositions.remove(item);
		}
		else
		{
		item.setEditingStatus(!item.isEditingStatus());
		}
		//lstProject=new ListModelList<ProjectModel>(data.getProjectList(selectedCompany.getCompKey(),""));	
        //refreshRowTemplate(lcs);
    }
	
	@Command
	@NotifyChange({"lstPositions"})
	public void addNewCommand()
	{
		for (HRListValuesModel item : lstPositions) 
		{
			if(item.getListId()==0)
			{
				Messagebox.show(msgAddNew,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}	
		}
		
		HRListValuesModel obj=new HRListValuesModel();
		obj.setListId(0);
		obj.setEnDescription("");
		obj.setArDescription("");		
		obj.setEditingStatus(true);
		obj.setQbListId("NOTPOSTED");
		lstPositions.add(0,obj);
	}
	
	@Command
	@NotifyChange({"lstPositions"})
	public void confirm(@BindingParam("row") HRListValuesModel item) 
	{
	
		for (HRListValuesModel obj : lstPositions) 
		{
			if(obj.getListId()!=item.getListId())
			{
				if(item.getEnDescription().trim().toLowerCase().equals(obj.getEnDescription().trim().toLowerCase()))
				{
					Messagebox.show(msgServiceName,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
						
			}
		}
		
		if(item.getListId()==0)
		hrdata.addNewHRListValue( item.getEnDescription(), item.getArDescription(), "LABOUR_SERVICETYPE", "47");		
		else
		hrdata.updateHRListValue(item.getListId(), item.getEnDescription(), item.getArDescription());	
		Clients.showNotification(msgSave);
		lstPositions=new ListModelList<HRListValuesModel>(data.getHRListValues(47,""));
	}
	
	@Command
	@NotifyChange({"lstPositions"})
	public void deleteCommand(@BindingParam("row") final HRListValuesModel item) 
	{
		if(!item.getQbListId().equals("NOTPOSTED"))
	 	{
		 Messagebox.show(msgServiceUsed,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);	
		 return;
	 	}
		else if(item.getListId()>0 && lstPositionUsed.contains(item.getListId()))
		{
			Messagebox.show(msgServiceUsed,msgTitle, Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		}		
		
		Messagebox.show(msgConfirmDelete,msgTitle,Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		{
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
              {						 
							if(item.getListId()>0)
								data.deleteService(item.getListId());
					       lstPositions.remove(item);							
              }
			 }
		 });	
	}
	
	@Command
    @NotifyChange({"lstPositions"})
    public void changeFilter() 
    {	      
		lstPositions=(ListModelList<HRListValuesModel>) filterData();
    }
	private List<HRListValuesModel> filterData()
	{
		lstPositions=(ListModelList<HRListValuesModel>) lstAllPositions;
		List<HRListValuesModel>  lst=new ArrayList<HRListValuesModel>();
		for (Iterator<HRListValuesModel> i = lstPositions.iterator(); i.hasNext();)
		{
			HRListValuesModel tmp=i.next();				
			if(tmp.getEnDescription().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getArDescription().toLowerCase().contains(filter.getArname().toLowerCase()))
						
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}

	public ListModelList<HRListValuesModel> getLstPositions() {
		return lstPositions;
	}

	public void setLstPositions(ListModelList<HRListValuesModel> lstPositions) {
		this.lstPositions = lstPositions;
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
	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
}
