package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AccountsModel;
import model.BanksModel;
import model.BarcodeSettingsModel;
import model.CashInvoiceGridData;
import model.CashInvoiceModel;
import model.CustomerModel;
import model.QbListsModel;
import model.SerialFields;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

import sun.security.util.Length;

import com.itextpdf.text.pdf.Barcode;
import common.BarcodeGeneration;

import db.SQLDBHandler;

public class EditBarcodeViewModel {
	private Logger logger = Logger.getLogger(this.getClass());
	private BarcodeSettingsModel selectedBrcdSetting;
	private List<String> fillBarcodeType = new ArrayList<String>();
	private List<String> fillAfterScanGoTo = new ArrayList<String>();
	private String selectedType;
	private String selectedAfterScanGoTo;
	private String selectedBarcodeCounter;
	HBAData data = new HBAData();
	ItemsData dataQuery = new ItemsData();
	BarcodeGeneration barcodeGen = new BarcodeGeneration();
	

	public EditBarcodeViewModel() {
		try {
			selectedBrcdSetting = dataQuery.fillBarcodeSettings();
			
			fillBarcodesType();
			fillAfterScanGoToData();

			if (selectedBrcdSetting!= null){
				for (String model : fillBarcodeType) {
					if (model.equalsIgnoreCase(selectedBrcdSetting.getBarcodeType()))
						selectedType = model;
				}
				
				selectedBarcodeCounter=selectedBrcdSetting.getBarcodeCounter();
				
				if (null != selectedBrcdSetting.getBarcodeConvert()
						&& selectedBrcdSetting.getBarcodeConvert().equalsIgnoreCase("Y")) {
					selectedBrcdSetting.setBarcodeConvertBool(true);
				} else {
					selectedBrcdSetting.setBarcodeConvertBool(false);
				}
				
				for (String model : fillAfterScanGoTo) {
					if (model.equalsIgnoreCase(selectedBrcdSetting.getBarcodeAfterScanGoTo()))
						selectedAfterScanGoTo = model;
				}
			}
			else {
				selectedBrcdSetting=new BarcodeSettingsModel();;
				selectedType = "EAN13";
				selectedBrcdSetting.setBarcodeConvertBool(false);
				selectedAfterScanGoTo = "Item";
			}
			
			
		} catch (Exception ex) {
			logger.error("ERROR in EditBarcodeViewModel ----> init", ex);
		}
	}
	
	public void fillBarcodesType() {
		fillBarcodeType.add("EAN13");
		fillBarcodeType.add("EAN8");
		fillBarcodeType.add("UPCA");
		fillBarcodeType.add("UPCE");
	}
	
	public void fillAfterScanGoToData(){
		fillAfterScanGoTo.add("Item");
		fillAfterScanGoTo.add("Description");
		fillAfterScanGoTo.add("Qty");
		fillAfterScanGoTo.add("Rate");
		fillAfterScanGoTo.add("Class");
		fillAfterScanGoTo.add("Amount");
		fillAfterScanGoTo.add("Create a New Line");
	}
	
	private boolean validateData()
	{
		boolean isValid=true;
		
		if (selectedType== null || selectedType.trim().equals("")){
			Messagebox.show("Barcode Type Should Be Selected!","Barcode Settings",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
		
		if (selectedAfterScanGoTo== null || selectedAfterScanGoTo.trim().equals("")){
			Messagebox.show("After Scanning Go To Should Be Selected!","Barcode Settings",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
		
		if (selectedType.trim().equals("EAN13")){
			if (String.valueOf(selectedBarcodeCounter).length() != 12){
				Messagebox.show("Barcode Counter Length Must Be 12!","Barcode Settings",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}
		else if (selectedType.trim().equals("EAN8")){
			if (String.valueOf(selectedBarcodeCounter).length() != 7){
				Messagebox.show("Barcode Counter Length Must Be 7!","Barcode Settings",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}
		else if (selectedType.trim().equals("UPCA")){
			if (String.valueOf(selectedBarcodeCounter).length() != 11){
				Messagebox.show("Barcode Counter Length Must Be 11!","Barcode Settings",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}
		else if (selectedType.trim().equals("UPCE")){
			if (String.valueOf(selectedBarcodeCounter).length() != 6){
				Messagebox.show("Barcode Counter Length Must Be 11!","Barcode Settings",Messagebox.OK,Messagebox.INFORMATION);
				return false;
			}
		}
		 
		return isValid;
		
	}
	
	@Command
	@NotifyChange({ "selectedBarcodeCounter" })
	public void ValidateBarcodeCounter(@BindingParam("cmp") Window x) {
		String brcdCounter;
		
		if (selectedBarcodeCounter != null && !selectedBarcodeCounter.equals("") && barcodeGen.isNumeric(selectedBarcodeCounter)==false){
			//If barcode counter is not numeric, then stop
			Messagebox.show("Barcode Counter Should Be Numeric!","Barcode Settings",Messagebox.OK,Messagebox.EXCLAMATION);
			selectedBarcodeCounter="";
		}
		else{
			if (selectedType.equals("EAN13")){
				if (selectedBarcodeCounter.length()>0 && selectedBarcodeCounter.length()<12){
					//If length of the counter is greater than 0 and less than 13, then expand it to 12
					brcdCounter="000000000000" + selectedBarcodeCounter;
					selectedBarcodeCounter=brcdCounter.substring(brcdCounter.length()-12,brcdCounter.length());
				}
				else{
					if (selectedBarcodeCounter.length()>12){
						Messagebox.show("Barcode Counter Length Should Be 12!","Barcode Settings",Messagebox.OK,Messagebox.EXCLAMATION);
						selectedBarcodeCounter="";
					}
				}
			}
			else if (selectedType.equals("EAN8")){
				if (selectedBarcodeCounter.length()>0 && selectedBarcodeCounter.length()<7){
					//If length of the counter is greater than 0 and less than 7, then expand it to 7
					brcdCounter="000000" + selectedBarcodeCounter;
					selectedBarcodeCounter=brcdCounter.substring(brcdCounter.length()-7,brcdCounter.length());
				}
				else{
					if (selectedBarcodeCounter.length()>7){
						Messagebox.show("Barcode Counter Length Should Be 7!","Barcode Settings",Messagebox.OK,Messagebox.EXCLAMATION);
						selectedBarcodeCounter="";
					}
				}
			}
			else if (selectedType.equals("UPCE")){
				if (selectedBarcodeCounter.length()>0 && selectedBarcodeCounter.length()<6){
					//If length of the counter is greater than 0 and less than 6, then expand it to 6
					brcdCounter="00000" + selectedBarcodeCounter;
					selectedBarcodeCounter=brcdCounter.substring(brcdCounter.length()-6,brcdCounter.length());
				}
				else{
					if (selectedBarcodeCounter.length()>6){
						Messagebox.show("Barcode Counter Length Should Be 6!","Barcode Settings",Messagebox.OK,Messagebox.EXCLAMATION);
						selectedBarcodeCounter="";
					}
				}
			}
			else if (selectedType.equals("UPCA")){
				if (selectedBarcodeCounter.length()>0 && selectedBarcodeCounter.length()<11){
					//If length of the counter is greater than 0 and less than 11, then expand it to 11
					brcdCounter="0000000000" + selectedBarcodeCounter;
					selectedBarcodeCounter=brcdCounter.substring(brcdCounter.length()-11,brcdCounter.length());
				}
				else{
					if (selectedBarcodeCounter.length()>11){
						Messagebox.show("Barcode Counter Length Should Be 11!","Barcode Settings",Messagebox.OK,Messagebox.EXCLAMATION);
						selectedBarcodeCounter="";
					}
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Command  
	public void saveBarcodeSettings(@BindingParam("cmp") Window x){
		if(validateData()){
			
			saveData(); 
			
        	Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();		
		}
	}
	
	 private void saveData() {	      
		int result = 0;
		try
		{	
			ItemQueries query=new ItemQueries();
			
			if (selectedBrcdSetting.isBarcodeConvertBool() == true) {
				selectedBrcdSetting.setBarcodeConvert("Y");
			} else {
				selectedBrcdSetting.setBarcodeConvert("N");
			}
			
//			if (selectedBrcdSetting.getBarcodeDefaultPrinter()==null){
//				selectedBrcdSetting.setBarcodeDefaultPrinter("");
//			}
			
			selectedBrcdSetting.setBarcodeType(selectedType);
			
			selectedBrcdSetting.setBarcodeAfterScanGoTo(selectedAfterScanGoTo);
			
			selectedBrcdSetting.setBarcodeCounter(selectedBarcodeCounter);
			
			result = data.updateBarcodeSettings(selectedBrcdSetting);
			
			if (result == 1) {
					Clients.showNotification(
							"Barcode Settings Has Been Saved Successfully.",
							Clients.NOTIFICATION_TYPE_INFO, null,
							"middle_center", 10000, true);
			} else {
				Clients.showNotification("Error at save Barcode Settings !!");

			}
			
		}catch (Exception ex) 
		{
			Messagebox.show(ex.getMessage());
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@Command
	public void CloseBarcodeSettings(){
		if(true)
		{
        	 Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			 Center center = bl.getCenter();
			 Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
			 tabbox.getSelectedPanel().detach();
			 tabbox.getSelectedTab().detach();	
			 Tabs contentTabs=(Tabs)tabbox.getFellow("contentTabs");
			 for (Component oldTab : contentTabs.getChildren()) {
					if(oldTab instanceof Tab){
							((Tab) oldTab).setSelected(true);
					}
				}
				
		}
	}
	
	
	public BarcodeSettingsModel getSelectedBrcdSetting() {
		return selectedBrcdSetting;
	}
	public void setSelectedBrcdSetting(BarcodeSettingsModel selectedBrcdSetting) {
		this.selectedBrcdSetting = selectedBrcdSetting;
	}

	public List<String> getFillBarcodeType() {
		return fillBarcodeType;
	}

	public void setFillBarcodeType(List<String> fillBarcodeType) {
		this.fillBarcodeType = fillBarcodeType;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public String getSelectedBarcodeCounter() {
		return selectedBarcodeCounter;
	}

	public void setSelectedBarcodeCounter(String selectedBarcodeCounter) {
		this.selectedBarcodeCounter = selectedBarcodeCounter;
	}

	public List<String> getFillAfterScanGoTo() {
		return fillAfterScanGoTo;
	}

	public void setFillAfterScanGoTo(List<String> fillAfterScanGoTo) {
		this.fillAfterScanGoTo = fillAfterScanGoTo;
	}

	public String getSelectedAfterScanGoTo() {
		return selectedAfterScanGoTo;
	}

	public void setSelectedAfterScanGoTo(String selectedAfterScanGoTo) {
		this.selectedAfterScanGoTo = selectedAfterScanGoTo;
	}

	
}
