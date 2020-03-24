package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.ActivityStatusModel;
import model.CashInvoiceModel;
import model.ChangeStatusQuotationModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class ChangeStatusQuotation {
	private Logger logger = Logger.getLogger(EditDelivery.class);
	private HBAData data=new HBAData();
	private int quotationKey=0;
	private ChangeStatusQuotationModel model=new ChangeStatusQuotationModel();
	private String newStatus="";
	private String newStatusDesc="";
	private String name = "";
	private String other = "";
	private Date changeDate;
	Calendar c=Calendar.getInstance();
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


	@SuppressWarnings("rawtypes")
	public ChangeStatusQuotation(){
		try {
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			quotationKey=(Integer) map.get("quotationKey");
			model=data.getQuotationByIDForChangeStatus(quotationKey);
			if(model.getCustomerName()!=null && !model.getCustomerName().equalsIgnoreCase("")){
				name=model.getCustomerName();
			}else{
				name=model.getProspectiveName();
			}
			changeDate=df.parse(sdf.format(c.getTime()));


		} catch (Exception ex) {
			logger.error("ERROR in ChangeStatusQuotation ----> Init",ex);
		}


	}


	@Command
	public void changeStatus(@BindingParam("cmp") Window x) {
		try {
			if(!newStatus.equals("")){
//				switch (newStatus) {
//				case "A":
//					newStatusDesc="Approve";
//					break;
//				case "J":
//					newStatusDesc="Reject";
//					break;
//				case "H":
//					newStatusDesc="Hold";
//					break;
//				case "T":
//					newStatusDesc="Close";
//					break;
//				case "O":
//					newStatusDesc="Other";
//					break;
//				default:
//					break;
//				}
				if(newStatus.equals("R"))
				{
					newStatusDesc="Revise";
					CashInvoiceModel objCashInvoice = data.getQuatationByID(quotationKey,0,true);
					objCashInvoice.setStatus(newStatus);
					String refNumber=objCashInvoice.getInvoiceSaleNo();
					if(refNumber.contains("-"))
					{
						String refNum=refNumber.substring(0, refNumber.indexOf("-"));
						String subNum=refNumber.substring(refNumber.indexOf("-")+1);
						objCashInvoice.setInvoiceSaleNo(refNum+"-"+ (Integer.parseInt(subNum)+1) );
					}
					else
					{
						objCashInvoice.setInvoiceSaleNo(refNumber+"-"+1);
					}
					logger.info("new ref number >> " + objCashInvoice.getInvoiceSaleNo());
					
					//TODO
					//make duplicate to this quotation 
					//data.updateQuotationStatus(quotationKey, newStatus, newStatusDesc);
					//data.updateQuotationStatus(quotationKey, newStatus, newStatusDesc);
					
					Map<String, Object> arg = new HashMap<String, Object>();
					arg.put("reviseQuotation", objCashInvoice);
					BindUtils.postGlobalCommand(null, null,"getReviseQuotation", arg);					
					x.detach();
				}
				else{
					data.updateQuotationStatus(quotationKey, newStatus, newStatusDesc);
					data.updateActivityStatus(quotationKey);
					ActivityStatusModel activityStatusModel=new ActivityStatusModel();
					activityStatusModel.setRecNo(data.getMaxID("activityStatus", "recno")+1);
					activityStatusModel.setActivity(25);
					activityStatusModel.setActivityRecNo(quotationKey);
					activityStatusModel.setStatus(newStatus);
					activityStatusModel.setDescription(newStatusDesc);
					activityStatusModel.setMemo(model.getNote());
					activityStatusModel.setStatusDate(changeDate);
					activityStatusModel.setActive("Y");
					data.addActivityStatus(activityStatusModel);
					Map<String, Object> arg = new HashMap<String, Object>();
					BindUtils.postGlobalCommand(null, null,"getStatusQuotation", arg);
					x.detach();
				}
				x.detach();
			}else{
				Clients.showNotification("Please Select Status",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",10000, true);
			}
		} catch (Exception ex) {
			logger.error("ERROR in ChangeStatusQuotation ----> save", ex);
		}
	}

	private void dublicateQoutation()
	{
		try
		{
			int tmpRecNo = 0;
			CashInvoiceModel obj = new CashInvoiceModel();
			tmpRecNo = data.GetNewQuotationRecNo();
			
			
		}
		catch (Exception ex) {
			logger.error("ERROR in ChangeStatusQuotation ----> dublicateQoutation", ex);
		}
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public HBAData getData() {
		return data;
	}


	public void setData(HBAData data) {
		this.data = data;
	}


	public int getQuotationKey() {
		return quotationKey;
	}


	public void setQuotationKey(int quotationKey) {
		this.quotationKey = quotationKey;
	}


	public ChangeStatusQuotationModel getModel() {
		return model;
	}


	public void setModel(ChangeStatusQuotationModel model) {
		this.model = model;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}




	public String getNewStatus() {
		return newStatus;
	}




	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}




	public String getOther() {
		return other;
	}




	public void setOther(String other) {
		this.other = other;
	}




	public Date getChangeDate() {
		return changeDate;
	}




	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}




	public String getNewStatusDesc() {
		return newStatusDesc;
	}




	public void setNewStatusDesc(String newStatusDesc) {
		this.newStatusDesc = newStatusDesc;
	}



}
