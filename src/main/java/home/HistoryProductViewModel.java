package home;


import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;

public class HistoryProductViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	QuotationData data=new QuotationData();
	private int quotationid;
	private List<HistoryProductModel> lstHistProductModel;
	
	@Init
	public void init(@BindingParam("quotId")int quotId)
	{	
		try 
		{
		quotationid=quotId;	
		setLstHistProductModel(data.getQuotationProduct(quotationid));
		}
		catch (Exception ex){
			
			logger.info("Exception int HistoryProductViewModel ===> "+ ex);
		}
	}

	public List<HistoryProductModel> getLstHistProductModel() {
		return lstHistProductModel;
	}

	public void setLstHistProductModel(List<HistoryProductModel> lstHistProductModel) {
		this.lstHistProductModel = lstHistProductModel;
	}
}
