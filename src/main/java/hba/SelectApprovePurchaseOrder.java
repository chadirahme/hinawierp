package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.ApprovePurchaseOrderModel;
import model.DataFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class SelectApprovePurchaseOrder {
	private Logger logger = Logger.getLogger(SelectApprovePurchaseOrder.class);
	private List<String> fromType=new ArrayList<String>();
	private String selelctedFromType=new String();
	private String webOrOnline="";
	private Date fromDate;
	private Date toDate;
	private DataFilter filter=new DataFilter();
	private List<ApprovePurchaseOrderModel> invoiceSalesReport;
	private List<ApprovePurchaseOrderModel> allInvoiceSalesReport;
	private List<ApprovePurchaseOrderModel> lstPO;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	HBAData data=new HBAData();
	private boolean adminUser;
	private int webUserID=0;
	private int vendorKey;
	


	@SuppressWarnings("rawtypes")
	public SelectApprovePurchaseOrder(){
		try{
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Calendar c = Calendar.getInstance();
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			vendorKey = (Integer) map.get("vendorKey");
			fillData();
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


		}catch (Exception ex)
		{	
			logger.error("ERROR in SelectApprovePurchaseOrder ----> init", ex);			
		}

	}


	public void fillData()
	{
		fromType=new ArrayList<String>();
		fromType.add("From Online");
		fromType.add("From Desktop");
		fromType.add("Both");
		selelctedFromType=fromType.get(0);
	}



	public String getSelelctedFromType() {
		return selelctedFromType;
	}

	@NotifyChange({"invoiceSalesReport","allInvoiceSalesReport","webOrOnline"})
	public void setSelelctedFromType(String selelctedFromType) {
		this.selelctedFromType = selelctedFromType;
		if(selelctedFromType!=null)
		{
			if(selelctedFromType.equalsIgnoreCase("From Desktop"))
			{
				webOrOnline="CMS";
			}
			else if(selelctedFromType.equalsIgnoreCase("From Online"))
			{
				webOrOnline="ONL";
			}
			else if(selelctedFromType.equalsIgnoreCase("Both"))
			{
				webOrOnline="";
			}
		}

	}

	@Command
	@NotifyChange({"invoiceSalesReport"})
	public void searchApproved()
	{
		try
		{
			invoiceSalesReport=new ArrayList<ApprovePurchaseOrderModel>();
			allInvoiceSalesReport = new ArrayList<ApprovePurchaseOrderModel>();
			invoiceSalesReport=data.getApprovedPurchaseOrder(webUserID,fromDate, toDate, webOrOnline,vendorKey,0);
			allInvoiceSalesReport=invoiceSalesReport;



		}
		catch (Exception ex)
		{	
			logger.error("ERROR in SelectApprovePurchaseOrder ----> searchApproved", ex);			
		}
	}


	@Command
	public void resetPurchaseOrderReport()
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
			logger.error("ERROR in SelectApprovePurchaseOrder ----> resetPurchaseOrderReport", ex);			
		}
	}

	@Command
	@NotifyChange({"invoiceSalesReport","reportGroupModel","allInvoiceSalesReport","totalNoOfCustomer","totalAmount","period","totalSale","totalAmountStr","totalSaleStr","totalNoOfInvoice","maxInvoiceAmount","minInvoiceAmount"})
	public void changeFilter() 
	{
		try
		{
			invoiceSalesReport=filterData();
			//calcAmonut();

		}
		catch (Exception ex) {
			logger.error("error in SelectApprovePurchaseOrder---changeFilter-->" , ex);
		}

	}

	private List<ApprovePurchaseOrderModel> filterData()
	{
		invoiceSalesReport=allInvoiceSalesReport;
		List<ApprovePurchaseOrderModel>  lst=new ArrayList<ApprovePurchaseOrderModel>();
		if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
		{
			for (Iterator<ApprovePurchaseOrderModel> i = invoiceSalesReport.iterator(); i.hasNext();)
			{
				ApprovePurchaseOrderModel tmp=i.next();				
				if(tmp.getVendorName().toLowerCase().contains(filter.getVendorName().toLowerCase())
						&&tmp.getTxnDate().toString().toLowerCase().contains(filter.getTxnDate().toLowerCase())
						&&(tmp.getMemo()).toLowerCase().contains(filter.getMemo().toLowerCase())
						&&(tmp.getItemName()).toLowerCase().contains(filter.getItemName().toLowerCase())
						&&(tmp.getDescription()).toLowerCase().contains(filter.getDecription().toLowerCase())
						&&(tmp.getRemainingQuantity()+"").toLowerCase().contains(filter.getRemainingQuantity().toLowerCase())
						&&(tmp.getRcvdQuantity()+"").toLowerCase().contains(filter.getRcvdQuantity().toLowerCase())
						&&(tmp.getRate()+"").toLowerCase().contains(filter.getRate().toLowerCase())
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
	public void selectOrders(@BindingParam("cmp") final Window x) {
		try {
			if(lstPO.size()>0){
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("lstSelectApprovePurchaseOrder", lstPO);
				BindUtils.postNotifyChange(null, null,SelectApprovePurchaseOrder.this, "lstPO");
				BindUtils.postGlobalCommand(null, null,"getlstSelectApprovePurchaseOrder", arg);
				x.detach();
			}else{
				Clients.showNotification("Please, Select Purchase Request",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}


		} catch (Exception ex) {
			logger.error("ERROR in SelectApprovePurchaseOrder ----> selectRequests",ex);
		}

	}

	public List<String> getFromType() {
		return fromType;
	}

	public void setFromType(List<String> fromType) {
		this.fromType = fromType;
	}

	public String getWebOrOnline() {
		return webOrOnline;
	}

	public void setWebOrOnline(String webOrOnline) {
		this.webOrOnline = webOrOnline;
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

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public List<ApprovePurchaseOrderModel> getInvoiceSalesReport() {
		return invoiceSalesReport;
	}

	public void setInvoiceSalesReport(
			List<ApprovePurchaseOrderModel> invoiceSalesReport) {
		this.invoiceSalesReport = invoiceSalesReport;
	}

	public List<ApprovePurchaseOrderModel> getAllInvoiceSalesReport() {
		return allInvoiceSalesReport;
	}

	public void setAllInvoiceSalesReport(
			List<ApprovePurchaseOrderModel> allInvoiceSalesReport) {
		this.allInvoiceSalesReport = allInvoiceSalesReport;
	}

	public List<ApprovePurchaseOrderModel> getLstPO() {
		return lstPO;
	}

	public void setLstPO(List<ApprovePurchaseOrderModel> lstPO) {
		this.lstPO = lstPO;
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
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


	public int getVendorKey() {
		return vendorKey;
	}


	public void setVendorKey(int vendorKey) {
		this.vendorKey = vendorKey;
	}


}
