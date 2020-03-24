package fixedasset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

import model.FixedAssetModel;

public class FixedAssetViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	FixedAssetData data=new FixedAssetData();
	private List<FixedAssetModel> lstFixedAsset;
	private FixedAssetModel selectedFixedAsset;
	private FixedAssetFilter filter=new FixedAssetFilter();
	private static final String footerMessage = "A Total of %d Items";
	private List<FixedAssetModel> lstAllFixedAsset;
	
	private Integer selectedPageSize;
	
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	public FixedAssetViewModel()
	{
		try
		{
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		selectedPageSize=50;
		
		lstFixedAsset=data.getFixedAssetist();
		lstAllFixedAsset=lstFixedAsset;
		if(lstFixedAsset.size()>0)
		selectedFixedAsset=lstFixedAsset.get(0);
		}
		catch (Exception ex) {
			logger.error("error in FixedAssetViewModel---Init-->" , ex);
		}
		
	}
	
	 @Command
	 public void addCommand()
	 {
		 try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("assetId", 0);			
			   arg.put("type","edit");
			   Executions.createComponents("/fixedasset/list/editasset.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in FixedAssetViewModel ----> addCommand", ex);			
			}
	 }
	 
	 @Command
	 public void viewCommand(@BindingParam("row") FixedAssetModel row)
	 {
		 try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("assetId", row.getAssetid());			
			   arg.put("type","view");
			   Executions.createComponents("/fixedasset/list/editasset.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in FixedAssetViewModel ----> viewCommand", ex);			
			}
	 }
	 
	 @Command
	 public void editCommand(@BindingParam("row") FixedAssetModel row)
	 {
		 try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("assetId", row.getAssetid());			
			   arg.put("type","edit");
			   Executions.createComponents("/fixedasset/list/editasset.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in FixedAssetViewModel ----> editCommand", ex);			
			}
	 }
	 
	  	@GlobalCommand 
	  	@NotifyChange({"lstFixedAsset"})
		public void refreshParent()
	  	{		
	  		lstFixedAsset=data.getFixedAssetist();
			lstAllFixedAsset=lstFixedAsset;
	  	}
	 
	public FixedAssetFilter getFilter() {
		return filter;
	}
	
	public List<FixedAssetModel> getLstFixedAsset() {
		return lstFixedAsset;
	}
	public void setLstFixedAsset(List<FixedAssetModel> lstFixedAsset) {
		this.lstFixedAsset = lstFixedAsset;
	}
	public FixedAssetModel getSelectedFixedAsset() {
		return selectedFixedAsset;
	}
	public void setSelectedFixedAsset(FixedAssetModel selectedFixedAsset) {
		this.selectedFixedAsset = selectedFixedAsset;
	}
	
	
	
		public String getFooter() {
	        return String.format(footerMessage, lstFixedAsset.size());
	    }
	
		private List<FixedAssetModel> filterData()
		{
			lstFixedAsset=lstAllFixedAsset;
			
			String assetName=filter.getAssetName().toLowerCase();
			String masterDesc=filter.getMasterDesc().toLowerCase();
			String unit=filter.getUnit().toLowerCase();
			String used=filter.getUsed().toLowerCase();
			String category=filter.getCategory().toLowerCase();
			
			List<FixedAssetModel> lst=new ArrayList<FixedAssetModel>();
			for (Iterator<FixedAssetModel> i = lstFixedAsset.iterator(); i.hasNext();)
			{
				FixedAssetModel tmp=i.next();				
				if(tmp.getAssetName().toLowerCase().contains(assetName)&&
						tmp.getAssetMasterDesc().toLowerCase().contains(masterDesc)&&
						tmp.getUnitDesc().toLowerCase().contains(unit)&&
						tmp.getUsed().toLowerCase().contains(used)&&
						tmp.getCategoryDesc().toLowerCase().contains(category))
				{
					lst.add(tmp);
				}
			}
			return lst;
			
		}
	    @Command
	    @NotifyChange({"lstFixedAsset","footer"})
	    public void changeFilter() 
	    {	      
	    	lstFixedAsset=filterData();
	    }
		public Integer getSelectedPageSize() {
			return selectedPageSize;
		}
		public void setSelectedPageSize(Integer selectedPageSize) {
			this.selectedPageSize = selectedPageSize;
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
				
			selectedPageSize=lstFixedAsset.size();
			
			}			
			else
				selectedPageSize=Integer.parseInt(selectedAllPageSize);
		}
}
