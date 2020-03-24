package hr;

import hr.model.SponsorModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.DataFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;

public class SponsorListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SponsorData sponsorsdata=new SponsorData();
	private List<SponsorModel> lstItems;
	private List<SponsorModel> lstAllItems;
	private SponsorModel selectedItems;
	private DataFilter filter=new DataFilter();
	private String footer;
	
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private Integer selectedPageSize;
	
	int menuID=249;
	private MenuModel companyRole;
	private Date lastModified;
	
	public SponsorListViewModel()
	{
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		getCompanyRolePermessions(dbUser.getCompanyroleid());
		
		lstItems=sponsorsdata.fillSponserInfo();
		lstAllItems=lstItems;
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		selectedPageSize=50;
		
		lastModified=sponsorsdata.getSponsorLastModified();
	}

	private void getCompanyRolePermessions(int companyRoleId)
	{
		companyRole=new MenuModel();
		HRData data=new HRData();
		List<MenuModel> lstRoles= data.getHRRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	private List<SponsorModel> filterData()
	{
		lstItems=lstAllItems;
		List<SponsorModel>  lst=new ArrayList<SponsorModel>();
		for (Iterator<SponsorModel> i = lstItems.iterator(); i.hasNext();)
		{
			SponsorModel tmp=i.next();				
			if(tmp.getSponsorName().toLowerCase().contains(filter.getSponsorName().toLowerCase())&&
					tmp.getSponsorNameArabic().toLowerCase().contains(filter.getSponsorNameArabic().toLowerCase())&&
					tmp.getBankCode().toLowerCase().contains(filter.getBankCode().toLowerCase())&&
					tmp.getCompanyId().toLowerCase().contains(filter.getCompanyId().toLowerCase())
					)	
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	  @Command
	   public void resetSponsor()
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
				logger.error("ERROR in SponsorListViewModel ----> resetSponsor", ex);			
			}
	   }
  
  //edit vendor list
  @Command
  public void editSponsorCommand(@BindingParam("row") SponsorModel row)
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("sponsorKey", row.getSponsorKey());
		   arg.put("compKey",0);
		   arg.put("type","edit");
		   Executions.createComponents("/hr/list/editSponsorList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in SponsorListViewModel ----> editSponsorCommand", ex);			
		}
  }
  
  @Command
  public void viewSponsorCommand(@BindingParam("row") SponsorModel row)
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("sponsorKey", row.getSponsorKey());
		   arg.put("compKey",0);
		   arg.put("type","view");
		   Executions.createComponents("/hr/list/editSponsorList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in SponsorListViewModel ----> viewSponsorCommand", ex);			
		}
  }
  @Command
  public void addSponsorCommand()
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("sponsorKey",0);
		   arg.put("compKey",0);
		   arg.put("type","Add");
		   Executions.createComponents("/hr/list/editSponsorList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in SponsorListViewModel ----> addSponsorCommand", ex);			
		}
  }
  
  @GlobalCommand 
	  @NotifyChange({"lstItems","lastModified"})
	    public void refreshParentSponsor(@BindingParam("type")String type)
			  {		
				 try
				  {
					 lstItems=sponsorsdata.fillSponserInfo();
				     lstAllItems=lstItems;
				     lastModified=sponsorsdata.getSponsorLastModified();
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in SponsorListViewModel ----> refreshParentSponsor", ex);			
					}
			  }
  
	@Command
    @NotifyChange({"lstItems","footer"})
    public void changeFilter() 
    {	      
	   lstItems=filterData();
    }
	
	public List<SponsorModel> getLstItems() {
		return lstItems;
	}

	public void setLstItems(List<SponsorModel> lstItems) {
		this.lstItems = lstItems;
	}

	public List<SponsorModel> getLstAllItems() {
		return lstAllItems;
	}

	public void setLstAllItems(List<SponsorModel> lstAllItems) {
		this.lstAllItems = lstAllItems;
	}

	public SponsorModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(SponsorModel selectedItems) {
		this.selectedItems = selectedItems;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
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
			selectedPageSize=lstItems.size();
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}
	
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
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
