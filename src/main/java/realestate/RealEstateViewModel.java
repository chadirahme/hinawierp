package realestate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import model.CustomerModel;
import model.DataFilter;
import model.RealEstateModel;

public class RealEstateViewModel 
{

	RealEstateData data=new RealEstateData();
	private List<RealEstateModel> lstRealEstate;
	private List<RealEstateModel> lstAllRealEstate;
	private RealEstateModel selectedRealEstate;
	
	private List<RealEstateModel> lstVacant;
	private List<RealEstateModel> lstAllVacant;
	private RealEstateModel selectedVacant;
	
	private DataFilter filter=new DataFilter();
	private Integer selectedPageSize;
	
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	private  String listType;	
	@Init
    public void init(@BindingParam("type") String type)
	{
		listType=type;
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		selectedPageSize=50;
		
		if(listType.equals("realstate"))
		{
			lstRealEstate=data.getRealEstateUnitList();
			lstAllRealEstate=lstRealEstate;
			if(lstRealEstate.size()>0)
			selectedRealEstate=lstRealEstate.get(0);
		}
		if(listType.equals("vacant"))
		{
			lstVacant=data.getVacantList();
			lstAllVacant=lstVacant;
			if(lstVacant.size()>0)
			selectedVacant=lstVacant.get(0);
		}
	}
	
	public RealEstateViewModel()
	{
		
	}
	
	private List<RealEstateModel> filterData()
	{
		lstRealEstate=lstAllRealEstate;
		List<RealEstateModel>  lst=new ArrayList<RealEstateModel>();
		for (Iterator<RealEstateModel> i = lstRealEstate.iterator(); i.hasNext();)
		{
			RealEstateModel tmp=i.next();				
			 if(tmp.getFlatName().toLowerCase().contains(filter.getFlatName().toLowerCase())&&
					tmp.getBuildingName().toLowerCase().contains(filter.getBuildingName().toLowerCase())&&
					tmp.getStatus().toLowerCase().contains(filter.getStatus().toLowerCase())&&
					tmp.getRent().toLowerCase().contains(filter.getRent().toLowerCase())&&
					tmp.getDeposit().toLowerCase().contains(filter.getDeposit().toLowerCase())			
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	    @Command
	    @NotifyChange({"lstRealEstate","footer"})
	    public void changeFilter() 
	    {	      
		   lstRealEstate=filterData();
	    }
	    
	    private List<RealEstateModel> filterVacantData()
		{
			lstVacant=lstAllVacant;
			List<RealEstateModel>  lst=new ArrayList<RealEstateModel>();
			for (Iterator<RealEstateModel> i = lstVacant.iterator(); i.hasNext();)
			{
				RealEstateModel tmp=i.next();				
				 if(tmp.getFlatName().toLowerCase().contains(filter.getFlatName().toLowerCase())&&
						tmp.getBuildingName().toLowerCase().contains(filter.getBuildingName().toLowerCase())&&
						tmp.getStatus().toLowerCase().contains(filter.getStatus().toLowerCase())&&
						tmp.getFlatDesc().toLowerCase().contains(filter.getDescription().toLowerCase())&&
						tmp.getFlatTypeDesc().toLowerCase().contains(filter.getType().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
			return lst;
			
		}
	    @Command
	    @NotifyChange({"lstVacant","footer"})
	    public void changeVacantFilter() 
	    {	      
		   lstVacant=filterVacantData();
	    }
	    
	   
	public List<RealEstateModel> getLstRealEstate() {
		return lstRealEstate;
	}
	public void setLstRealEstate(List<RealEstateModel> lstRealEstate) {
		this.lstRealEstate = lstRealEstate;
	}
	public RealEstateModel getSelectedRealEstate() {
		return selectedRealEstate;
	}
	public void setSelectedRealEstate(RealEstateModel selectedRealEstate) {
		this.selectedRealEstate = selectedRealEstate;
	}
	public List<RealEstateModel> getLstVacant() {
		return lstVacant;
	}
	public void setLstVacant(List<RealEstateModel> lstVacant) {
		this.lstVacant = lstVacant;
	}
	public RealEstateModel getSelectedVacant() {
		return selectedVacant;
	}
	public void setSelectedVacant(RealEstateModel selectedVacant) {
		this.selectedVacant = selectedVacant;
	}
	public DataFilter getFilter() {
		return filter;
	}
	public void setFilter(DataFilter filter) {
		this.filter = filter;
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
			if(listType.equals("realstate"))
			{
				selectedPageSize=lstRealEstate.size();
			}
			if(listType.equals("vacant"))
			{
				selectedPageSize=lstVacant.size();
			}
		}			
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}
}
