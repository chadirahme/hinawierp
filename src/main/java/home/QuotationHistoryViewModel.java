package home;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;

public class QuotationHistoryViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat dcf=new DecimalFormat("0.00");
	private Date fromdate;
	private Date todate ;
	private List<QuotationModel> lstQuotationHist;
	private List<QuotationModel> lstAllQuotationHist;
	QuotationData data=new QuotationData();
	private QuotationFilterModel filterModel=new QuotationFilterModel();
	private int totalNumber;
	
	public QuotationHistoryViewModel()
	{
		try {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		fromdate=df.parse(sdf.format(c.getTime()));
		todate = new Date();
		
		} 
		catch (Exception e) {
			logger.info("Exception in QuotationHistoryViewModel ===> init " + e);
		}
	
	}

	@NotifyChange({ "lstQuotationHist","totalNumber"})
	@Command
	public void getHistory() 
	{
		try 
		{			
			lstQuotationHist = data.getQuotationHistory(sdf.format(fromdate), sdf.format(todate));
			lstAllQuotationHist=lstQuotationHist;
			totalNumber=lstQuotationHist.size();

		} catch (Exception ex) {

			logger.info("Exception in QuotationHistoryViewModel ===> " + ex);
		}
	}
	
	@Command
	public void viewHistoryCommand(@BindingParam("id") int quotId) 
	{
		//Messagebox.show(quotId+"");
		Map<String,Object> arg = new HashMap<String,Object>();
		arg.put("quotId", quotId);
		Executions.createComponents("/admin/history_product.zul", null,arg);
	}
	
	
	  @Command
	   public void addTask()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("taskKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","add");
			   Executions.createComponents("/hba/list/editTask.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in QuotationHistoryViewModel ----> addTask", ex);			
			}
	   }
	  
	private  List<QuotationModel> filterData()
	{
	    List<QuotationModel> lst=new ListModelList<QuotationModel>();
		lstQuotationHist=lstAllQuotationHist;
		for (QuotationModel item : lstQuotationHist) 
		{
			if(item.getCreationDate().contains(filterModel.getDate())&&
			   item.getContactName().toLowerCase().contains(filterModel.getContactName().toLowerCase())&&
			   item.getCompanyName().toLowerCase().contains(filterModel.getCompanyName().toLowerCase())&&
			   item.getCity().toLowerCase().contains(filterModel.getCity().toLowerCase())&&
			   item.getCountryName().toLowerCase().contains(filterModel.getCountry().toLowerCase())&&
			   item.getTelephone1().toLowerCase().contains(filterModel.getTelephone().toLowerCase())&&
			   item.getMobile1().toLowerCase().contains(filterModel.getMobile().toLowerCase())&&
			   item.getEmail().toLowerCase().contains(filterModel.getEmail().toLowerCase())
					)
			{
				lst.add(item);
			}
		}
		return lst;
	}
	@Command
    @NotifyChange({"lstQuotationHist","totalNumber"})
    public void changeFilter() 
    {	      
		lstQuotationHist=filterData();
		totalNumber=lstQuotationHist.size();
    }
	
	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public List<QuotationModel> getLstQuotationHist() {
		return lstQuotationHist;
	}

	public void setLstQuotationHist(List<QuotationModel> lstQuotationHist) {
		this.lstQuotationHist = lstQuotationHist;
	}

	public QuotationFilterModel getFilterModel() {
		return filterModel;
	}

	public void setFilterModel(QuotationFilterModel filterModel) {
		this.filterModel = filterModel;
	}

	/**
	 * @return the totalNumber
	 */
	public int getTotalNumber() {
		return totalNumber;
	}

	/**
	 * @param totalNumber the totalNumber to set
	 */
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	
	
	
}
