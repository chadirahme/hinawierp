package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.DataFilter;
import model.DeliveryModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;


public class SelectDelivery {
	private Logger logger = Logger.getLogger(this.getClass());
	private DataFilter filter=new DataFilter();
	private List<DeliveryModel> deliveryModel;
	private List<DeliveryModel> allDeliveryModel;
	private List<DeliveryModel> selectedDelivery;
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	HBAData data=new HBAData();
	private int webUserID=0;
	private boolean adminUser;
	private int customerKey=0;
	private String invoiceType="";

	@SuppressWarnings("rawtypes")
	public SelectDelivery() {
		try {
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			customerKey = (Integer) map.get("customerKey");
			invoiceType = (String) map.get("invoiceType");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");

				if(adminUser)
				{
					webUserID=0;
				}
				else
				{
					webUserID=dbUser.getUserid();
				}
			}
			if(invoiceType.equalsIgnoreCase("cash")){
				deliveryModel=data.getDeliveryForInvoice(customerKey, webUserID, "S",0);
			}
			if(invoiceType.equalsIgnoreCase("credit")){
				deliveryModel=data.getDeliveryForInvoice(customerKey, webUserID, "I",0);
			}
			
		} catch (Exception ex){	
			logger.error("ERROR in SelectDelivery ----> init", ex);			
		}
	}

	@Command
	@NotifyChange({"deliveryModel"})
	public void changeFilter() 
	{
		try
		{
			deliveryModel=filterData();
			//calcAmonut();

		}
		catch (Exception ex) {
			logger.error("error in VoidDelivery --- changeFilter -->" , ex);
		}

	}

	private List<DeliveryModel> filterData()
	{
		deliveryModel=allDeliveryModel;
		List<DeliveryModel> lst=new ArrayList<DeliveryModel>();
		if(deliveryModel!=null && deliveryModel.size()>0)
		{
			for (Iterator<DeliveryModel> i = deliveryModel.iterator(); i.hasNext();)
			{
				DeliveryModel tmp=i.next();				
				if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())	&&(tmp.getStatusDesc()).toLowerCase().contains(filter.getStatusDesc().toLowerCase())
						&&(tmp.getMemo()).toLowerCase().contains(filter.getMemo().toLowerCase())&&tmp.getRefNumber().toLowerCase().contains(filter.getRefNUmber().toLowerCase()))
				{
					lst.add(tmp);
				}
			}
		}
		return lst;
	}

	@Command
	public void selectDelivery(@BindingParam("cmp") final Window x) {
		try {
			if(selectedDelivery.size()>0){
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("lstSelectedDelivery", selectedDelivery);
				BindUtils.postNotifyChange(null, null,SelectDelivery.this, "selectedDelivery");
				if(invoiceType.equalsIgnoreCase("cash")){
					BindUtils.postGlobalCommand(null, null,"getlstSelectedDelivery", arg);
				}
				if(invoiceType.equalsIgnoreCase("credit")){
					BindUtils.postGlobalCommand(null, null,"getlstSelectedDeliveryCredit", arg);
				}				
				
				x.detach();
			}else{
				Clients.showNotification("Please, Select Delivery",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}


		} catch (Exception ex) {
			logger.error("ERROR in SelectDelivery ----> selectDelivery",ex);
		}

	}



	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public List<DeliveryModel> getDeliveryModel() {
		return deliveryModel;
	}

	public void setDeliveryModel(List<DeliveryModel> deliveryModel) {
		this.deliveryModel = deliveryModel;
	}

	public List<DeliveryModel> getAllDeliveryModel() {
		return allDeliveryModel;
	}

	public void setAllDeliveryModel(List<DeliveryModel> allDeliveryModel) {
		this.allDeliveryModel = allDeliveryModel;
	}

	public List<DeliveryModel> getSelectedDelivery() {
		return selectedDelivery;
	}

	public void setSelectedDelivery(List<DeliveryModel> selectedDelivery) {
		this.selectedDelivery = selectedDelivery;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public HBAData getData() {
		return data;
	}

	public void setData(HBAData data) {
		this.data = data;
	}

	public int getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public int getCustomerKey() {
		return customerKey;
	}

	public void setCustomerKey(int customerKey) {
		this.customerKey = customerKey;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

}

