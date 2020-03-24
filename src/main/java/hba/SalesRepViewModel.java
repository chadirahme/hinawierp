package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.BanksModel;
import model.DataFilter;
import model.SalesRepModel;

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

public class SalesRepViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SalesRepData salesRepdata=new SalesRepData();
	HBAData data=new HBAData();
	private List<SalesRepModel> lstItems;
	private List<SalesRepModel> lstAllItems;
	private SalesRepModel selectedItems;
	private DataFilter filter=new DataFilter();
	private String footer;
	
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private Integer selectedPageSize;
	
	private List<String> lstStatus;
	private String selectedStatus;
	
	private MenuModel companyRole;
	
	public SalesRepViewModel()
	{
		lstItems=salesRepdata.fillSalesRepList("");
		lstAllItems=lstItems;
		
		lstStatus=new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		selectedStatus=lstStatus.get(0);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		selectedPageSize=50;
		
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		
	}

	private List<SalesRepModel> filterData()
	{
		lstItems=lstAllItems;
		List<SalesRepModel>  lst=new ArrayList<SalesRepModel>();
		for (Iterator<SalesRepModel> i = lstItems.iterator(); i.hasNext();)
		{
			SalesRepModel tmp=i.next();				
			if(tmp.getIsActive().toLowerCase().startsWith(filter.getIsActive().toLowerCase())&&
					tmp.getSalesRepName().toLowerCase().contains(filter.getSalesRepName().toLowerCase())&&
					tmp.getSalesRepType().toLowerCase().contains(filter.getSalesRepType().toLowerCase())&&
					//tmp.getCommissionPercent().toLowerCase().contains(filter.getCommissionPercent().toLowerCase())&&
					tmp.getIntials().toLowerCase().contains(filter.getIntials().toLowerCase()))
			{
				if(filter.getCommissionPercent()>0)
				{
					if(tmp.getCommissionPercent() == filter.getCommissionPercent())
						lst.add(tmp);
				}
				else
				{
				lst.add(tmp);
				}
			}
		}
				
		return lst;		
	}
	
	  @Command
	   public void resetSalesRep()
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
				logger.error("ERROR in SalesRepViewModel ----> resetSalesRep", ex);			
			}
	   }
  
  //edit vendor list
  @Command
  public void editSalesRepCommand(@BindingParam("row") SalesRepModel row)
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("salesRepKey", row.getSlaesRepKey());
		   arg.put("compKey",0);
		   arg.put("type","edit");
		   Executions.createComponents("/hba/list/editSalesRepList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in SlaesRepViewModel ----> editSalesRepCommand", ex);			
		}
  }
  
  @Command
  public void viewSalesRepCommand(@BindingParam("row") SalesRepModel row)
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("salesRepKey", row.getSlaesRepKey());
		   arg.put("compKey",0);
		   arg.put("type","view");
		   Executions.createComponents("/hba/list/editSalesRepList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in SalesRepViewModel ----> viewSalesRepCommand", ex);			
		}
  }
  @Command
  public void addSalesRepCommand()
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("salesRepKey",0);
		   arg.put("compKey",0);
		   arg.put("type","Add");
		   Executions.createComponents("/hba/list/editSalesRepList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in SalesRepViewModel ----> addSalesRepCommand", ex);			
		}
  }
  
  @GlobalCommand 
	  @NotifyChange({"lstItems"})
	    public void refreshParentSalesRep(@BindingParam("type")String type)
			  {		
				 try
				  {
					 lstItems=salesRepdata.fillSalesRepList("");
						lstAllItems=lstItems;
					 
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in SalesRepViewModel ----> refreshParentSalesRep", ex);			
					}
			  }


	
	@Command
    @NotifyChange({"lstItems","footer"})
    public void changeFilter() 
    {	      
	   lstItems=filterData();
    }
	
	public List<SalesRepModel> getLstItems() {
		return lstItems;
	}

	public void setLstItems(List<SalesRepModel> lstItems) {
		this.lstItems = lstItems;
	}

	public List<SalesRepModel> getLstAllItems() {
		return lstAllItems;
	}

	public void setLstAllItems(List<SalesRepModel> lstAllItems) {
		this.lstAllItems = lstAllItems;
	}

	public SalesRepModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(SalesRepModel selectedItems) {
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
	@NotifyChange({"lstItems"})
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		String status="";
		if(selectedStatus.equalsIgnoreCase("Active"))
			status="Y";
		else if(selectedStatus.equalsIgnoreCase("Inactive"))
			status="N";
		else if(selectedStatus.equalsIgnoreCase("All"))
			status="";
		
		lstItems=salesRepdata.fillSalesRepList(status);
		lstAllItems=lstItems;
		if(lstItems.size()>0)
		selectedItems=lstItems.get(0);
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

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==243)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	
}
