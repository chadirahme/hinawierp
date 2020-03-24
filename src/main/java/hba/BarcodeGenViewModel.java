package hba;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import model.BarcodeSettingsModel;
import model.DataFilter;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Window;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import common.BarcodeGeneration;
import common.BarcodePrintInfo;
import common.BarcodeSendToPrinter;


public class BarcodeGenViewModel {

	static Logger logger = Logger.getLogger("BarcodeGenViewModel");
	BarcodeGeneration barcodeGen = new BarcodeGeneration();

	private List<QbListsModel> lstItems;
	private List<QbListsModel> lstAllItems;
	ItemsData data = new ItemsData();
	private DataFilter filter = new DataFilter();
	private String selectedPageType;
	private List<String> fillBarcodeSize = new ArrayList<String>();
	private List<String> fillBarcodeStartLoc = new ArrayList<String>();
	private List<String> fillInfPosition = new ArrayList<String>();
	private List<String> lstPrinters = new ArrayList<String>();
	private BarcodeSettingsModel barcodeSettings;
	private boolean chkAll=false;

	// Information to print
	private boolean printItemName;
	private boolean printDescription;
	private boolean printSellingPrice;
	
	//Order counter
	private int orderCounter=0;
	private String printItemNameOrder="",printDescriptionOrder="",printSellingPriceOrder="";

	//Disable start location in case of ribbon
	private boolean startLocDisable;
	
	private boolean overwrite=false;
	
	public BarcodeGenViewModel() {
		lstItems = data.fillQbItemsListsBarcode( "",false);
		lstAllItems = lstItems;

		barcodeSettings = data.fillBarcodeSettings();
	}

	@Command
	@NotifyChange({ "lstItems", "footer" })
	public void changeFilter() {
		lstItems = filterData();
	}

	private List<QbListsModel> filterData() {
		lstItems = lstAllItems;
		List<QbListsModel> lst = new ArrayList<QbListsModel>();
		for (Iterator<QbListsModel> i = lstItems.iterator(); i.hasNext();) {
			QbListsModel tmp = i.next();
			if (tmp.getName().toLowerCase()
					.contains(filter.getName().toLowerCase())
					&& tmp.getSalesDesc().toLowerCase()
							.contains(filter.getDescription().toLowerCase())
					&& tmp.getItemType().toLowerCase()
							.contains(filter.getType().toLowerCase())
					&& tmp.getIsActive().toLowerCase()
							.contains(filter.getIsactive().toLowerCase())
					&& tmp.getBarcode().toLowerCase()
							.contains(filter.getBarcode().toLowerCase())
					&& String.valueOf(tmp.getSalesPrice()).contains(
							filter.getSalesPrice())) {
				lst.add(tmp);
			}
		}
		return lst;
	}
	
	
	@Command
	@NotifyChange({ "lstItems" })
	public void generateBarcode(@BindingParam("cmp") Window x) {
		
		boolean oneChecked = false;
		String barcode="";
		BarcodeGeneration bGen=new BarcodeGeneration();
		
		for (int i = 0; i < lstItems.size(); i++) {
			
			QbListsModel item = lstItems.get(i);
			//Generate barcode
			
			if (item.isCheckedItem()) {
				oneChecked=true;
				if(overwrite==true || item.getBarcode()==null || item.getBarcode().equals("")){
					barcode = bGen.generateBarcodeRecursive();
					item.setBarcode(barcode);
					
					updateBarcode(item);
				}
			}
		}
		
		if (oneChecked==false){
			Messagebox.show("Select At Least One Item To Generate Barcode For!", "Barcode Generation", Messagebox.OK, Messagebox.INFORMATION);
		}
		else{
			Messagebox.show("Barcode(s) successfully generated", "Barcode Generation", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}
	
	private void updateBarcode(QbListsModel item){
		
		if(item!=null && item.getBarcode()!=null && !item.getBarcode().trim().equals("")){
			
			HBAData data = new HBAData();
			
			data.updateBarcode(item);
		}
	}
	
	@Command
	public void CloseBarcodeGeneration() {
		if (true) {
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox = (Tabbox) center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().detach();
			tabbox.getSelectedTab().detach();
			Tabs contentTabs = (Tabs) tabbox.getFellow("contentTabs");
			for (Component oldTab : contentTabs.getChildren()) {
				if (oldTab instanceof Tab) {
					((Tab) oldTab).setSelected(true);
				}
			}
		}
	}

	
	
	@Command
	@NotifyChange({ "lstItems"})
	public void chkAll() {
		
		if (chkAll){
			chkAll = false;
		}
		else{
			chkAll=true;
		}
		for (int i=0 ; i< lstItems.size() ; i++){
			((QbListsModel)lstItems.get(i)).setCheckedItem(chkAll);
		}
	}
	

	public List<QbListsModel> getLstItems() {
		return lstItems;
	}

	public void setLstItems(List<QbListsModel> lstItems) {
		this.lstItems = lstItems;
	}

	public List<QbListsModel> getLstAllItems() {
		return lstAllItems;
	}

	public void setLstAllItems(List<QbListsModel> lstAllItems) {
		this.lstAllItems = lstAllItems;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public String getSelectedPageType() {
		return selectedPageType;
	}

	public void setSelectedPageType(String selectedPageType) {
		this.selectedPageType = selectedPageType;
	}

	public List<String> getFillBarcodeSize() {
		return fillBarcodeSize;
	}

	public void setFillBarcodeSize(List<String> fillBarcodeSize) {
		this.fillBarcodeSize = fillBarcodeSize;
	}

	public List<String> getFillBarcodeStartLoc() {
		return fillBarcodeStartLoc;
	}

	public void setFillBarcodeStartLoc(List<String> fillBarcodeStartLoc) {
		this.fillBarcodeStartLoc = fillBarcodeStartLoc;
	}

	public boolean isPrintItemName() {
		return printItemName;
	}

	public void setPrintItemName(boolean printItemName) {
		this.printItemName = printItemName;
	}

	public boolean isPrintDescription() {
		return printDescription;
	}

	public void setPrintDescription(boolean printDescription) {
		this.printDescription = printDescription;
	}

	public boolean isPrintSellingPrice() {
		return printSellingPrice;
	}

	public void setPrintSellingPrice(boolean printSellingPrice) {
		this.printSellingPrice = printSellingPrice;
	}

	public List<String> getFillInfPosition() {
		return fillInfPosition;
	}

	public void setFillInfPosition(List<String> fillInfPosition) {
		this.fillInfPosition = fillInfPosition;
	}

	public int getOrderCounter() {
		return orderCounter;
	}

	public void setOrderCounter(int orderCounter) {
		this.orderCounter = orderCounter;
	}

	public String getPrintItemNameOrder() {
		return printItemNameOrder;
	}

	public void setPrintItemNameOrder(String printItemNameOrder) {
		this.printItemNameOrder = printItemNameOrder;
	}

	public String getPrintDescriptionOrder() {
		return printDescriptionOrder;
	}

	public void setPrintDescriptionOrder(String printDescriptionOrder) {
		this.printDescriptionOrder = printDescriptionOrder;
	}

	public String getPrintSellingPriceOrder() {
		return printSellingPriceOrder;
	}

	public void setPrintSellingPriceOrder(String printSellingPriceOrder) {
		this.printSellingPriceOrder = printSellingPriceOrder;
	}

	public boolean isStartLocDisable() {
		return startLocDisable;
	}

	public void setStartLocDisable(boolean startLocDisable) {
		this.startLocDisable = startLocDisable;
	}

	public List<String> getLstPrinters() {
		return lstPrinters;
	}

	public void setLstPrinters(List<String> lstPrinters) {
		this.lstPrinters = lstPrinters;
	}

	public boolean isChkAll() {
		return chkAll;
	}

	public void setChkAll(boolean chkAll) {
		this.chkAll = chkAll;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	

	
}
