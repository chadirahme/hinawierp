package hba;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import model.EstimateModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;

public class EstimateItemViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	EstimateData data=new EstimateData();
	private List<EstimateModel> lstEstimate = new ArrayList<EstimateModel>();
	private int totalEstimate;
	private int totalItemSelected;
	private double totalRequestedQuantity;
	private Set<EstimateModel> selectedItem;
	
	public EstimateItemViewModel()
	{
		try
		{
			lstEstimate=data.GetBOQEstimateMaterialsList();		
			totalEstimate=lstEstimate.size();
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in EstimateItemViewModel ----> init", ex);			
		}
	}
	
	@Command
	 @NotifyChange({"lstEstimate","totalRequestedQuantity"})
	 public void checkQty(@BindingParam("row") EstimateModel row)
	 {
		totalRequestedQuantity=0;
		 if(row.getRequestQuantity() > row.getQuantity())
		 {
			 Messagebox.show("Request Quantity must not be greater than the available Quantity !!","Estimate", Messagebox.OK , Messagebox.EXCLAMATION);
			 row.setRequestQuantity(row.getQuantity());			 
		 }
		 
		 for(EstimateModel item : lstEstimate)
		 {
			 totalRequestedQuantity+=item.getRequestQuantity();
		 }
	 }
	
	
	public List<EstimateModel> getLstEstimate() {
		return lstEstimate;
	}
	public void setLstEstimate(List<EstimateModel> lstEstimate) {
		this.lstEstimate = lstEstimate;
	}
	public int getTotalEstimate() {
		return totalEstimate;
	}
	public void setTotalEstimate(int totalEstimate) {
		this.totalEstimate = totalEstimate;
	}
	public int getTotalItemSelected() {
		return totalItemSelected;
	}
	public void setTotalItemSelected(int totalItemSelected) {
		this.totalItemSelected = totalItemSelected;
	}
	public double getTotalRequestedQuantity() {
		return totalRequestedQuantity;
	}
	public void setTotalRequestedQuantity(double totalRequestedQuantity) {
		this.totalRequestedQuantity = totalRequestedQuantity;
	}
	
	public Set<EstimateModel> getSelectedItem() {
		
		
		return selectedItem;
	}
	@NotifyChange("totalItemSelected")
	public void setSelectedItem(Set<EstimateModel> selectedItem) {
		if(selectedItem!=null)
			totalItemSelected=selectedItem.size();
		this.selectedItem = selectedItem;
	}
}
