package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import layout.MenuModel;
import model.CustomerModel;
import model.DataFilter;
import model.QbListsModel;

public class ItemsViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	ItemsData data=new ItemsData();
	HBAData hbadata = new HBAData();
	private List<QbListsModel> lstItems;
	private List<QbListsModel> lstAllItems;
	private QbListsModel selectedItems;
	private DataFilter filter=new DataFilter();
	private String footer;
	private String footer1;
	private String footer2;
	private String footer3;
	
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private Integer selectedPageSize;
	
	
	private List<String> lstStatus;
	private String selectedStatus;
	
	private MenuModel companyRole;
	private boolean adminUser;
	private boolean posItems;
	
	public ItemsViewModel()
	{
		FillStatusList();
		lstItems=data.fillQbItemsLists("");
		lstAllItems=lstItems;
		getItemType();
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		selectedPageSize=50;

		Session sess = Sessions.getCurrent();
		WebusersModel dbUser = (WebusersModel) sess
				.getAttribute("Authentication");
		if (dbUser != null) {
			adminUser = dbUser.getFirstname().equals("admin");
		}
		getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		//use as default for huge data
		posItems=true;
	}
	
	private void FillStatusList()
	{
	lstStatus=new ArrayList<String>();
	lstStatus.add("All");
	lstStatus.add("Active");
	lstStatus.add("InActive");
	selectedStatus=lstStatus.get(0);
	}

	private List<QbListsModel> filterData()
	{
		lstItems=lstAllItems;
		List<QbListsModel>  lst=new ArrayList<QbListsModel>();
		for (Iterator<QbListsModel> i = lstItems.iterator(); i.hasNext();)
		{
			QbListsModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getSalesDesc().toLowerCase().contains(filter.getDescription().toLowerCase())&&
					tmp.getItemType().toLowerCase().contains(filter.getType().toLowerCase())&&
					tmp.getIsActive().toLowerCase().startsWith(filter.getStatus().toLowerCase())&&
					tmp.getAccountName().toLowerCase().contains(filter.getAccountName().toLowerCase()))			
			{
				if(filter.getCostPrice()>0 || filter.getSellPrice()>0)
				{
					if(tmp.getPurchaseCost()>0 && tmp.getPurchaseCost() == filter.getCostPrice())
						lst.add(tmp);
					if(tmp.getSalesPrice()>0 && tmp.getSalesPrice() == filter.getSellPrice())
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
	   public void viewItem(@BindingParam("row") QbListsModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("itemKey", row.getItem_Key());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   arg.put("posItems", posItems);
			   Executions.createComponents("/hba/list/editItem.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ItemsViewModel ----> viewItem", ex);			
			}
	   }
	  @GlobalCommand 
	  @NotifyChange({"lstItems"})
	  public void refreshParentItems(@BindingParam("type")String type)
			  {		
				 try
				  {
					 String status="";
					 if(selectedStatus.equals("Active"))
					  status="Y";
					  else if(selectedStatus.equals("Inactive"))
					  status="N";
					  else
						  status="";
					 lstItems=data.fillQbItemsLists(status);
						lstAllItems=lstItems;
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in ItemsViewModel ----> refreshParent", ex);			
					}
			  }
	   
	   @Command
	   public void editItems(@BindingParam("row") QbListsModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("itemKey", row.getItem_Key());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   arg.put("posItems", posItems);
			   Executions.createComponents("/hba/list/editItem.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ItemsViewModel ----> editItems", ex);			
			}
	   }
	   
	   @Command
	   public void addItems()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("itemKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","add");
			   arg.put("posItems", posItems);
			   Executions.createComponents("/hba/list/editItem.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ItemsViewModel ----> addItems", ex);			
			}
	   }
	
	
	@Command
	   public void resetItems()
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
				logger.error("ERROR in ItemsViewModel ----> resetItems", ex);			
			}
	   }
	
	@Command
    @NotifyChange({"lstItems","footer"})
    public void changeFilter() 
    {	      
	   lstItems=filterData();
	   getItemType();
    }
	
	private void getItemType()
	{
		//String itemType="";
		int InventoryItem=0;
		int ItemDiscount=0;
		int OtherCharges=0;
		int Service=0;
		int NonInventoryPart=0;
		
		for (QbListsModel item : lstItems) 
		{
		if(item.getItemType().equals("InventoryItem"))
		{
			InventoryItem++;
		}
		if(item.getItemType().equals("ItemDiscount"))
		{
			ItemDiscount++;
		}
		if(item.getItemType().equals("OtherCharges"))
		{
			OtherCharges++;
		}
		if(item.getItemType().equals("Service"))
		{
			Service++;
		}
		if(item.getItemType().equals("NonInventoryPart"))
		{
			NonInventoryPart++;
		}
		
	   }
		
		footer="Total No. of Inventory Items " + String.valueOf(InventoryItem);	
		footer1="Total No. of Service Items " + String.valueOf(Service) ;
		footer2="Total No. of Other Charges " + String.valueOf(OtherCharges) ;
		footer3="Total No. of Discount " + String.valueOf(ItemDiscount) ;
		
	}
	
	

	public List<QbListsModel> getLstItems() {
		return lstItems;
	}

	public void setLstItems(List<QbListsModel> lstItems) {
		this.lstItems = lstItems;
	}

	public List<QbListsModel> getLstAllItems() {
		return lstAllItems;
	}

	public void setLstAllItems(List<QbListsModel> lstAllItems) {
		this.lstAllItems = lstAllItems;
	}

	public QbListsModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(QbListsModel selectedItems) {
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
	 * @return the footer1
	 */
	public String getFooter1() {
		return footer1;
	}

	/**
	 * @param footer1 the footer1 to set
	 */
	public void setFooter1(String footer1) {
		this.footer1 = footer1;
	}

	/**
	 * @return the footer2
	 */
	public String getFooter2() {
		return footer2;
	}

	/**
	 * @param footer2 the footer2 to set
	 */
	public void setFooter2(String footer2) {
		this.footer2 = footer2;
	}

	/**
	 * @return the footer3
	 */
	public String getFooter3() {
		return footer3;
	}

	/**
	 * @param footer3 the footer3 to set
	 */
	public void setFooter3(String footer3) {
		this.footer3 = footer3;
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
		else if(selectedStatus.equalsIgnoreCase("InActive"))
			status="N";
		 lstItems=data.fillQbItemsLists(status);
			lstAllItems=lstItems;
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

		List<MenuModel> lstRoles= hbadata.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==75)
			{
				companyRole=item;
				break;
			}
		}
	}

	public boolean isPosItems() {
		return posItems;
	}

	public void setPosItems(boolean posItems) {
		this.posItems = posItems;
	}

	
	
}
