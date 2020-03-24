package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.CashInvoiceGridData;
import model.CompSetupModel;
import model.DataFilter;
import model.DeliveryLineModel;
import model.PurchaseRequestGridData;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class ItemsPopupViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();
	private String viewType;
	private List<QbListsModel> lstItems;
	private List<QbListsModel> lstAllItems;
	private DataFilter filter=new DataFilter();
	private CashInvoiceGridData selectedGridItems;
	private PurchaseRequestGridData selectedPurchaseRequestGridDataRow;
	private  CashInvoiceGridData selectedQuotationRequestGridDataRow;
	private DeliveryLineModel selectedDeliveryGridDataRow;
	public ItemsPopupViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			
			//lstItems=data.fillQbItemsList();		
			//lstAllItems=lstItems;
			lstItems=(List<QbListsModel>) map.get("lstItems");
			lstAllItems=lstItems;
			selectedGridItems=(CashInvoiceGridData) map.get("selectedRow");
			selectedPurchaseRequestGridDataRow=(PurchaseRequestGridData) map.get("selectedPurchaseRequestGridDataRow");
			selectedQuotationRequestGridDataRow=(CashInvoiceGridData) map.get("selectedQuotationRequestGridDataRow");
			selectedDeliveryGridDataRow=(DeliveryLineModel) map.get("selectedDeliveryGridDataRow");
			
			//compSetup=(CompSetupModel) map.get("compSetup");
			//lstInvcCustomerGridClass=(List<QbListsModel>) map.get("lstInvcCustomerGridClass");
			//logger.info("at load >>>> "+selectedGridItems.getLineNo());
		}
		catch(Exception ex)
		{
			logger.error("ERROR in ItemsPopupViewModel ----> init", ex);
		}
	}
	
	@Command
	public void selectdItemCommand(@ContextParam(ContextType.VIEW) Window comp,@BindingParam("row") QbListsModel row)
	{
		Map args = new HashMap();		
		args.put("selectedItem", row);			
		args.put("selectedRow",selectedGridItems);
		args.put("selectedPurchaseRequestGridDataRow",selectedPurchaseRequestGridDataRow);
		args.put("selectedQuotationRequestGridDataRow",selectedQuotationRequestGridDataRow);
		args.put("selectedDeliveryGridDataRow",selectedDeliveryGridDataRow);
		args.put("refreshCommand", "selectedItemCommand");		
		
		BindUtils.postGlobalCommand(null, null, "refreshItemsParent", args);	
		comp.detach();	
	}
	
	@Command
	public void onOkCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		Map args = new HashMap();
		if(lstItems.size()==1)
		{
			args.put("selectedItem", lstItems.get(0));	
			args.put("selectedRow",selectedGridItems);
			args.put("selectedPurchaseRequestGridDataRow",selectedPurchaseRequestGridDataRow);
			args.put("selectedQuotationRequestGridDataRow",selectedQuotationRequestGridDataRow);
			args.put("selectedDeliveryGridDataRow",selectedDeliveryGridDataRow);
			args.put("refreshCommand", "selectedItemCommand");
			BindUtils.postGlobalCommand(null, null, "refreshItemsParent", args);	
			comp.detach();
		}					
	}
	
	@Command
    @NotifyChange({"lstItems","footer"})
    public void changeFilter() 
    {	      
	   lstItems=filterData();
	  
    }
	private List<QbListsModel> filterData()
	{
		lstItems=lstAllItems;
		List<QbListsModel>  lst=new ArrayList<QbListsModel>();
		for (Iterator<QbListsModel> i = lstItems.iterator(); i.hasNext();)
		{
			QbListsModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getSalesDesc().toLowerCase().contains(filter.getDescription().toLowerCase())
					//tmp.getItemType().toLowerCase().contains(filter.getType().toLowerCase())&&
					//tmp.getAccountName().toLowerCase().contains(filter.getAccountName().toLowerCase())
					)					
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	
	public List<QbListsModel> getLstItems() {
		return lstItems;
	}
	public void setLstItems(List<QbListsModel> lstItems) {
		this.lstItems = lstItems;
	}
	public DataFilter getFilter() {
		return filter;
	}
	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public CashInvoiceGridData getSelectedQuotationRequestGridDataRow() {
		return selectedQuotationRequestGridDataRow;
	}

	public void setSelectedQuotationRequestGridDataRow(
			CashInvoiceGridData selectedQuotationRequestGridDataRow) {
		this.selectedQuotationRequestGridDataRow = selectedQuotationRequestGridDataRow;
	}
	
}
