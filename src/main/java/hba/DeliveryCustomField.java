package hba;

import java.util.HashMap;
import java.util.Map;

import model.DeliveryModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class DeliveryCustomField {
	private Logger logger = Logger.getLogger(EditDelivery.class);
	private DeliveryModel delivery;
	private String field1="";
	private String field2="";
	private String field3="";
	private String field4="";
	private String field5="";
	private int  deliveryKey;
	private HBAData data=new HBAData();
	private int webUserID=0;
	private boolean seeTrasction=false;

	@SuppressWarnings("rawtypes")
	public DeliveryCustomField(){
		try {
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			logger.info(map.keySet().toString());
			deliveryKey=(Integer) map.get("deliveryKey");
			seeTrasction=(Boolean) map.get("seeTrasction");
			webUserID=(Integer) map.get("webUserID");
			delivery=(DeliveryModel)map.get("deliveryModel");
			field1=delivery.getCustomField1();
			field2=delivery.getCustomField2();
			field3=delivery.getCustomField3();
			field4=delivery.getCustomField4();
			field5=delivery.getCustomField5();
			
			if(deliveryKey>0){
				delivery=data.getDeliveryByID(deliveryKey,webUserID,seeTrasction);
				field1=delivery.getCustomField1();
				field2=delivery.getCustomField2();
				field3=delivery.getCustomField3();
				field4=delivery.getCustomField4();
				field5=delivery.getCustomField5();
			}
		} catch (Exception ex) {
			logger.error("ERROR in DeliveryCustomField ----> Init",ex);
		}


	}

	@Command
	public void addCustomFields(@BindingParam("cmp") Window x)
	{
		try {
			
			
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("delivery",delivery);
			BindUtils.postNotifyChange(null, null,DeliveryCustomField.this, "delivery");
			BindUtils.postGlobalCommand(null, null,"getCustomFieldDelivery", arg);



		} catch (Exception ex) {
			logger.error("ERROR in DeliveryCustomField ----> addCustomFields",ex);
		}

		x.detach();


	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public DeliveryModel getDelivery() {
		return delivery;
	}

	public void setDelivery(DeliveryModel delivery) {
		this.delivery = delivery;
	}

	public String getField1() {
		return field1;
		
	}

	public void setField1(String field1) {
		this.field1 = field1;
		delivery.setCustomField1(field1);
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
		delivery.setCustomField2(field2);
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;

		delivery.setCustomField3(field3);
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;

		delivery.setCustomField4(field4);
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;

		delivery.setCustomField5(field5);
	}

	public int getDeliveryKey() {
		return deliveryKey;
	}

	public void setDeliveryKey(int deliveryKey) {
		this.deliveryKey = deliveryKey;
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

	public boolean isSeeTrasction() {
		return seeTrasction;
	}

	public void setSeeTrasction(boolean seeTrasction) {
		this.seeTrasction = seeTrasction;
	}



}
