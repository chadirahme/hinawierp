package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.BanksModel;
import model.DataFilter;
import model.VendorModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Tabbox;

public class BanksNameListViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	BankNamesData bankdata=new BankNamesData();
	private List<BanksModel> lstItems;
	private List<BanksModel> lstAllItems;
	private BanksModel selectedItems;
	private DataFilter filter=new DataFilter();
	private String footer;
	
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private Integer selectedPageSize;
	
	public BanksNameListViewModel()
	{
		lstItems=bankdata.fillBankList();
		lstAllItems=lstItems;
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		selectedPageSize=50;
	}

	private List<BanksModel> filterData()
	{
		lstItems=lstAllItems;
		List<BanksModel>  lst=new ArrayList<BanksModel>();
		for (Iterator<BanksModel> i = lstItems.iterator(); i.hasNext();)
		{
			BanksModel tmp=i.next();				
			if(tmp.getBankName().toLowerCase().contains(filter.getBankName().toLowerCase())&&
					tmp.getBranch().toLowerCase().contains(filter.getBranchName().toLowerCase())&&
					tmp.getActName().toLowerCase().contains(filter.getAccountName().toLowerCase())&&
					tmp.getActNumber().toLowerCase().contains(filter.getAccountNumber().toLowerCase())
					&&
					tmp.getAccountAssosiatedWith().toLowerCase().contains(filter.getAccountAssosiatedWith().toLowerCase()))	
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	  @Command
	   public void resetBank()
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
				logger.error("ERROR in BanksNameListViewModel ----> resetBank", ex);			
			}
	   }
  
  //edit vendor list
  @Command
  public void editBankCommand(@BindingParam("row") BanksModel row)
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("bankKey", row.getRecno());
		   arg.put("compKey",0);
		   arg.put("type","edit");
		   Executions.createComponents("/hba/list/editBanksNameList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in BanksNameListViewModel ----> editBankCommand", ex);			
		}
  }
  
  @Command
  public void viewBankCommand(@BindingParam("row") BanksModel row)
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("bankKey", row.getRecno());
		   arg.put("compKey",0);
		   arg.put("type","view");
		   Executions.createComponents("/hba/list/editBanksNameList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in BanksNameListViewModel ----> viewBankCommand", ex);			
		}
  }
  @Command
  public void addBankCommand()
  {
	   try
	   {
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("bankKey",0);
		   arg.put("compKey",0);
		   arg.put("type","Add");
		   Executions.createComponents("/hba/list/editBanksNameList.zul", null,arg);
	   }
	   catch (Exception ex)
		{	
			logger.error("ERROR in BanksNameListViewModel ----> addBankCommand", ex);			
		}
  }
  
  @GlobalCommand 
	  @NotifyChange({"lstItems"})
	    public void refreshParentBank(@BindingParam("type")String type)
			  {		
				 try
				  {
					 lstItems=bankdata.fillBankList();
						lstAllItems=lstItems;
					 
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in BanksNameListViewModel ----> refreshParentBank", ex);			
					}
			  }


	
	@Command
    @NotifyChange({"lstItems","footer"})
    public void changeFilter() 
    {	      
	   lstItems=filterData();
    }
	
	public List<BanksModel> getLstItems() {
		return lstItems;
	}

	public void setLstItems(List<BanksModel> lstItems) {
		this.lstItems = lstItems;
	}

	public List<BanksModel> getLstAllItems() {
		return lstAllItems;
	}

	public void setLstAllItems(List<BanksModel> lstAllItems) {
		this.lstAllItems = lstAllItems;
	}

	public BanksModel getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(BanksModel selectedItems) {
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
}
