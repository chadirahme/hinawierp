package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.DataFilter;
import model.DeliveryModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class VoidDelivery {
	private Logger logger = Logger.getLogger(this.getClass());
	private DataFilter filter=new DataFilter();
	private List<DeliveryModel> deliveryModel;
	private List<DeliveryModel> allDeliveryModel;
	private List<DeliveryModel> selectedDeliveryModel;
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	HBAData data=new HBAData();
	private int webUserID=0;
	private boolean adminUser;

	public VoidDelivery(){
		try{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Calendar c = Calendar.getInstance();	
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));

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
			
			//get list at load
			searchNotApproved();
			
		}catch (Exception ex){	
			logger.error("ERROR in VoidDelivery ----> init", ex);			
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
	@NotifyChange({"deliveryModel","allDeliveryModel"})
	public void searchNotApproved()
	{
		try
		{
			deliveryModel=new ArrayList<DeliveryModel>();
			allDeliveryModel = new ArrayList<DeliveryModel>();
			deliveryModel=data.getDeliveryReport(webUserID,true,fromDate, toDate);
			allDeliveryModel=deliveryModel;

		}
		catch (Exception ex)
		{	
			logger.error("ERROR in VoidDelivery ----> searchNotApproved", ex);			
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange({"deliveryModel","allDeliveryModel"})
	public void voidDelivery(@BindingParam("cmp") final Window x) {
		try {
			Messagebox.show("Are You Sure?","Delivery", Messagebox.YES | Messagebox.NO,	Messagebox.QUESTION,new org.zkoss.zk.ui.event.EventListener() {
				public void onEvent(Event evt)
						throws InterruptedException {
					if (evt.getName().equals("onYes")) {
						int result=0;	
						Iterator iter = null;
						DeliveryModel obj=new DeliveryModel();
						StringBuffer buffer = new StringBuffer();
						if(selectedDeliveryModel.size()>0){
							iter = selectedDeliveryModel.iterator();
						}else{
							Clients.showNotification("Please, Select At least one Delivery",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
							return;
						}
						if(iter!=null){
							while (iter.hasNext()) 
							{
								obj=(DeliveryModel) iter.next();
								if(obj.getTransformQ().equals("N"))
								{
								buffer.append(obj.getRecNo());
								if (iter.hasNext()) 
								{
									buffer.append(",");
								}
							  }
							}
						}
						if(!buffer.toString().equalsIgnoreCase(""))
						{
							result=data.updateDeliveryStatus(buffer.toString());
						}

						if(result>0){
							Clients.showNotification("The Delivery Has Been Voided Successfully.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
							x.detach();

						}else{
							Clients.showNotification("Error, The Delivery is Not Voided",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
						}
					}
				}
			});
		} catch (Exception ex) {
			logger.error("ERROR in VoidDelivery  ----> voidDelivery",ex);
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

	public List<DeliveryModel> getSelectedDeliveryModel() {
		return selectedDeliveryModel;
	}

	public void setSelectedDeliveryModel(List<DeliveryModel> selectedDeliveryModel) {
		this.selectedDeliveryModel = selectedDeliveryModel;
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
}
