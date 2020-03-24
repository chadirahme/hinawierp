package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.ApprovedQuotationModel;
import model.DataFilter;

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

public class SelectApproveQuotation {
	private Logger logger = Logger.getLogger(SelectApprovePurchaseRequest.class);
	private DataFilter filter=new DataFilter();
	private List<ApprovedQuotationModel> lstAppQuotation;
	private List<ApprovedQuotationModel> allLstAppQuotation;
	private ApprovedQuotationModel quotation;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	HBAData data=new HBAData();
	private boolean adminUser;
	private int webUserID=0;
	private int customerKey;

	@SuppressWarnings("rawtypes")
	public SelectApproveQuotation(){
		try
		{
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			customerKey = (Integer) map.get("customerKey");

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
			lstAppQuotation=data.getApprovedQuotation(customerKey, webUserID,0);
		}
		catch (Exception ex) {
			logger.error("error in SelectApproveQuotation--- Init -->" , ex);
		}
	}

	@Command
	@NotifyChange({"lstAppQuotation","allLstAppQuotation"})
	public void changeFilter() 
	{
		try
		{
			lstAppQuotation=filterData();
			//calcAmonut();

		}
		catch (Exception ex) {
			logger.error("error in SelectApproveQuotation---changeFilter-->" , ex);
		}

	}

	private List<ApprovedQuotationModel> filterData()
	{
		lstAppQuotation=allLstAppQuotation;
		List<ApprovedQuotationModel>  lst=new ArrayList<ApprovedQuotationModel>();
		if(lstAppQuotation!=null && lstAppQuotation.size()>0)
		{
			for (Iterator<ApprovedQuotationModel> i = lstAppQuotation.iterator(); i.hasNext();)
			{
				ApprovedQuotationModel tmp=i.next();				
				if(tmp.getTxnDate().toString().toLowerCase().contains(filter.getTxnDate().toLowerCase())
						&&(tmp.getMemo()).toLowerCase().contains(filter.getMemo().toLowerCase())
						&&(tmp.getItemName()).toLowerCase().contains(filter.getItemName().toLowerCase())
						&&tmp.getRefNumber().toLowerCase().contains(filter.getRefNUmber().toLowerCase())
						&&(tmp.getAmount()+"").toLowerCase().contains(filter.getAmount().toLowerCase()))
				{
					lst.add(tmp);
				}

			}
		}
		return lst;

	}

	@Command
	public void selectRequests(@BindingParam("cmp") final Window x) {
		try {
			if(quotation!=null){
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("quotation", quotation);
				BindUtils.postNotifyChange(null, null,SelectApproveQuotation.this, "quotation");
				BindUtils.postGlobalCommand(null, null,"getLstQuotationDelivery", arg);
				x.detach();
			}else{
				Clients.showNotification("Please, Select Quotation",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}


		} catch (Exception ex) {
			logger.error("ERROR in SelectApproveQuotation ----> selectRequests",ex);
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

	public List<ApprovedQuotationModel> getLstAppQuotation() {
		return lstAppQuotation;
	}

	public void setLstAppQuotation(List<ApprovedQuotationModel> lstAppQuotation) {
		this.lstAppQuotation = lstAppQuotation;
	}

	public List<ApprovedQuotationModel> getAllLstAppQuotation() {
		return allLstAppQuotation;
	}

	public void setAllLstAppQuotation(
			List<ApprovedQuotationModel> allLstAppQuotation) {
		this.allLstAppQuotation = allLstAppQuotation;
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

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public int getWebUserID() {
		return webUserID;
	}

	public void setWebUserID(int webUserID) {
		this.webUserID = webUserID;
	}

	public int getCustomerKey() {
		return customerKey;
	}

	public void setCustomerKey(int customerKey) {
		this.customerKey = customerKey;
	}

	public ApprovedQuotationModel getQuotation() {
		return quotation;
	}

	public void setQuotation(ApprovedQuotationModel quotation) {
		this.quotation = quotation;
	}

}
