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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
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


public class BarcodePrintingViewModel {

	static Logger logger = Logger.getLogger("BarcodePrintingViewModel");
	BarcodeGeneration barcodeGen = new BarcodeGeneration();

	private static final String ITM_ENG_DESC = "ENDESC", ITM_ARB_DESC = "ARDESC", ITM_PRICE = "PRICE";
	private static final int A4Width = 210;
	private static final int A4Height = 297;
	private static final String BARCODE_TYPE_BC128 = "BC128";
	private static final String BARCODE_TYPE_EAN13 = "EAN13";
	private static final String BARCODE_TYPE_EAN8 = "EAN8";
	private static final String BARCODE_TYPE_UPCA = "UPCA";
	private static final String BARCODE_TYPE_UPCE = "UPCE";
	private static final String BARCODE_PRINT_TYPE_A4 = "1";
	private static final String BARCODE_PRINT_TYPE_RB = "2";

	private List<QbListsModel> lstItems;
	private List<QbListsModel> lstAllItems;
	ItemsData data = new ItemsData();
	private DataFilter filter = new DataFilter();
	private String selectedPageType;
	private List<String> fillBarcodeSize = new ArrayList<String>();
	private List<String> fillBarcodeStartLoc = new ArrayList<String>();
	private List<String> fillInfPosition = new ArrayList<String>();
	private List<String> lstPrinters = new ArrayList<String>();
	private String printerName;
	private BarcodeSettingsModel barcodeSettings;
	private String selectedBarcodeSize;
	private String selectedStartLocation;
	private String selectedInfPosItmName;
	private String selectedInfPosItmDesc;
	private String selectedInfPosItmSellPrice;
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
	
	public BarcodePrintingViewModel() {
		lstItems = data.fillQbItemsListsBarcode("",true);
		lstAllItems = lstItems;

		
		//Fill Barcode Sizes
		fillBarcodeSize.add("210 * 148");
		fillBarcodeSize.add("105 * 148");
		fillBarcodeSize.add("105 * 74");
		fillBarcodeSize.add("105 * 42.3");
		fillBarcodeSize.add("70 * 37");
		fillBarcodeSize.add("52.5 * 29.7");
		fillBarcodeSize.add("52.5 * 21.2");
		fillBarcodeSize.add("48.3 * 16.9");
		fillBarcodeSize.add("38.1 * 12.7");
		fillBarcodeSize.add("38.1 * 21.2");
		selectedBarcodeSize="210 * 148";
		
		enableStartLoc("1");
		fillBarcodesStartLoc();
		fillInfPositions();
		fillPrinters();
		
		barcodeSettings = data.fillBarcodeSettings();

		printerName = "";

		// Set A4 by default
		selectedPageType = "1";

		// Set start location = 1 by default
		selectedStartLocation = "1";
		startLocDisable=false;

		// set information to print all as false
		printItemName = false;
		printDescription = false;
		printSellingPrice = false;
		
		//Set printing position = bottom
		selectedInfPosItmName="Bottom Left";
		selectedInfPosItmDesc="Bottom Left";
		selectedInfPosItmSellPrice="Bottom Left";

	}

	public void enableStartLoc(String pageType) {
		// A4
		if (pageType.trim().equals("1")) {
			startLocDisable=false;
			selectedStartLocation="1";
		}
		// Ribbon
		else {
			startLocDisable=true;
			selectedStartLocation="";
		}

	}
	
	private void fillPrinters() {
		
		BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
		
		lstPrinters = sendToPrinter.getAllPrinters();

	}
	
	public void fillBarcodesStartLoc() {
		
		int nbLabels=0;
		
		fillBarcodeStartLoc.clear();
		
		if (selectedBarcodeSize != null && selectedBarcodeSize.equals("48.3 * 16.9")) {
			nbLabels=64;
		}
		else if (selectedBarcodeSize != null && selectedBarcodeSize.equals("38.1 * 12.7")) {
			nbLabels=110;
		}
		else if (selectedBarcodeSize != null && selectedBarcodeSize.equals("38.1 * 21.2")) {
			nbLabels=65;
		}
		else if (selectedBarcodeSize != null && selectedBarcodeSize.equals("25 * 10")) {
			nbLabels=189;
		}
		else{
			String[] sizes = selectedBarcodeSize.split("\\*");
			if (sizes != null && sizes.length > 1) {
				double bcWidth = Double.valueOf( ((String) sizes[0]).trim());
				double bcHeight = Double.valueOf( ((String) sizes[1]).trim());
				
				nbLabels = (A4Width / (int)Math.floor(bcWidth)) * ( A4Height / (int)Math.floor(bcHeight));
			}
		}
		
		for (int i = 1; i <= nbLabels; i++) {
			fillBarcodeStartLoc.add(i + "");
		}
	}	

	@Command
	@NotifyChange({ "fillBarcodeStartLoc" })
	public void changeBarcodeStartLocation(@BindingParam("cmp") Window x) {
		fillBarcodesStartLoc();
	}
	
	public void fillInfPositions() {
		fillInfPosition.add("Bottom Left");
		fillInfPosition.add("Bottom Center");
		fillInfPosition.add("Bottom Right");
		fillInfPosition.add("Top Left");
		fillInfPosition.add("Top Center");
		fillInfPosition.add("Top Right");
	}
	
	@Command
	@NotifyChange({ "fillBarcodeSize","startLocDisable" ,"selectedStartLocation"})
	public void selectPageTypeCommand() {
		enableStartLoc(selectedPageType);

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
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange({ "printerName" })
	public void printBarcode(@BindingParam("cmp") Window x) {
		
		List<QbListsModel> lstSelectedItems = getSelectedItems();

		if (lstSelectedItems != null && lstSelectedItems.size() > 0) {

			if (validateData(false)) {

				BarcodePrintInfo barcodePrintInfo = new BarcodePrintInfo();

				// 1-Fill BarcodePrint Info
				if (selectedBarcodeSize != null) {
					String[] sizes = selectedBarcodeSize.split("\\*");
					if (sizes != null && sizes.length > 1) {
						double bcWidth = Double.valueOf( ((String) sizes[0]).trim());
						double bcHeight = Double.valueOf( ((String) sizes[1]).trim());
						
						barcodePrintInfo.setBarcodeWidth((int)Math.floor(bcWidth));
						barcodePrintInfo.setBarcodeHeight((int)Math.floor(bcHeight));
					}
				}
				
				// 2-Print Barcodes
				if (selectedPageType != null && selectedPageType.equals(BARCODE_PRINT_TYPE_A4)) {
					
					if(selectedBarcodeSize.equals("48.3 * 16.9")){
						
						barcodePrintInfo.setNbLabels(64);
						barcodePrintInfo.setxMargin(8.4);
						barcodePrintInfo.setyMargin(13.3);
						
					}else if(selectedBarcodeSize.equals("38.1 * 12.7")){
						
						barcodePrintInfo.setxMargin(9.75);
						barcodePrintInfo.setyMargin(8.8);
						barcodePrintInfo.setNbLabels(110);
						
					}else if(selectedBarcodeSize.equals("38.1 * 21.2")){
						
						barcodePrintInfo.setxMargin(9.75);
						barcodePrintInfo.setyMargin(10.7);
						barcodePrintInfo.setNbLabels(65);

					}

					barcodePrintInfo.setPrintLoc(Integer.valueOf(selectedStartLocation));
					printA4LstItems(lstSelectedItems, barcodePrintInfo, printerName, false);
					
				} else {
					
					printRibbonLstItems(lstSelectedItems, barcodePrintInfo,	printerName, false);
				}
			}
		} else {
			Messagebox.show("Select At Least One Item To Print!", "Barcode Printing", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}

	/*
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange({ "printerName" })
	public void printBarcode(@BindingParam("cmp") Window x) {
		
		List<QbListsModel> lstSelectedItems = getSelectedItems();

		if (lstSelectedItems != null && lstSelectedItems.size() > 0) {

			if (validateData(false)) {

				BarcodePrintInfo barcodePrintInfo = new BarcodePrintInfo();

				// 1-Fill BarcodePrint Info
				if (selectedBarcodeSize != null) {
					String[] sizes = selectedBarcodeSize.split("\\*");
					if (sizes != null && sizes.length > 1) {
						double bcWidth = Double.valueOf( ((String) sizes[0]).trim());
						double bcHeight = Double.valueOf( ((String) sizes[1]).trim());
						
						barcodePrintInfo.setBarcodeWidth((int)Math.floor(bcWidth));
						barcodePrintInfo.setBarcodeHeight((int)Math.floor(bcHeight));
					}
				}
				
				// 2-Print Barcodes
				if (selectedPageType != null && selectedPageType.equals(BARCODE_PRINT_TYPE_A4)) {
					
					if(selectedBarcodeSize.equals("48.3 * 16.9")){
						
						barcodePrintInfo.setNbLabels(64);
						barcodePrintInfo.setxMargin(8.4);
						barcodePrintInfo.setyMargin(13.3);
						
					}else if(selectedBarcodeSize.equals("38.1 * 12.7")){
						
						barcodePrintInfo.setxMargin(9.75);
						barcodePrintInfo.setyMargin(8.8);
						barcodePrintInfo.setNbLabels(110);
						
					}else if(selectedBarcodeSize.equals("38.1 * 21.2")){
						
						barcodePrintInfo.setxMargin(9.75);
						barcodePrintInfo.setyMargin(10.7);
						barcodePrintInfo.setNbLabels(65);

					}

					barcodePrintInfo.setPrintLoc(Integer.valueOf(selectedStartLocation));
					printA4LstItems(lstSelectedItems, barcodePrintInfo,	printerName, false);
				} else {
					
					printRibbonLstItems(lstSelectedItems, barcodePrintInfo,	printerName, false);
				}
			}
		} else {
			Messagebox.show("Select At Least One Item To Print!", "Barcode Printing", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}
	*/
	
	/*
	@Command
	public void previewBarcode(@BindingParam("cmp") Window x) {

		List<QbListsModel> lstSelectedItems = getSelectedItems();

		if (lstSelectedItems != null && lstSelectedItems.size() > 0) {

			if (validateData(true)) {

				BarcodePrintInfo barcodePrintInfo = new BarcodePrintInfo();

				// 1-Fill BarcodePrint Info
				if (selectedBarcodeSize != null) {
					String[] sizes = selectedBarcodeSize.split("\\*");
					if (sizes != null && sizes.length > 1) {
						double bcWidth = Double.valueOf( ((String) sizes[0]).trim());
						double bcHeight = Double.valueOf( ((String) sizes[1]).trim());
						
						barcodePrintInfo.setBarcodeWidth((int)Math.floor(bcWidth));
						barcodePrintInfo.setBarcodeHeight((int)Math.floor(bcHeight));
					}
				}
				
				// 2-Print Barcodes
				if (selectedPageType != null && selectedPageType.equals(BARCODE_PRINT_TYPE_A4)) {
					
					if(selectedBarcodeSize.equals("48.3 * 16.9")){
						
						barcodePrintInfo.setNbLabels(64);
						barcodePrintInfo.setxMargin(8.4);
						barcodePrintInfo.setyMargin(13.3);
						
					}else if(selectedBarcodeSize.equals("38.1 * 12.7")){
						
						barcodePrintInfo.setxMargin(9.75);
						barcodePrintInfo.setyMargin(8.8);
						barcodePrintInfo.setNbLabels(110);
						
					}else if(selectedBarcodeSize.equals("38.1 * 21.2")){
						
						barcodePrintInfo.setxMargin(9.75);
						barcodePrintInfo.setyMargin(10.7);
						barcodePrintInfo.setNbLabels(65);

					}

					barcodePrintInfo.setPrintLoc(Integer.valueOf(selectedStartLocation));
					printA4LstItems(lstSelectedItems, barcodePrintInfo,	printerName, true);
				} else {
					printRibbonLstItems(lstSelectedItems, barcodePrintInfo,	printerName, true);
				}
			}
		} else {
			Messagebox.show("Select At Least One Item To Print!", "Barcode Printing", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}
	*/
	
	private List<QbListsModel> getSelectedItems() {

		List<QbListsModel> lstSelectedItems = null;

		if (lstItems != null && lstItems.size() > 0) {

			lstSelectedItems = new ArrayList<QbListsModel>();

			for (int i = 0; i < lstItems.size(); i++) {

				QbListsModel item = lstItems.get(i);
				if (item.isCheckedItem()) {
					lstSelectedItems.add(item);
				}
			}
		}

		return lstSelectedItems;
	}

	private boolean validateData(boolean isPreview) {
		boolean isValid = true;

//		if(!isPreview){
//			// Printer name should be filled
//			if (printerName == null || printerName.trim().equals("")) {
//				Messagebox.show("Select a Printer Name For Printing!", "Barcode Printing", Messagebox.OK, Messagebox.INFORMATION);
//				printerName = originalPrinterName;
//				return false;
//			}
//		}

		if (selectedBarcodeSize == null || selectedBarcodeSize.trim().equals("")) {
			Messagebox.show("Select Barcode Size!", "Barcode Printing", Messagebox.OK, Messagebox.INFORMATION);
			return false;
		}

		return isValid;

	}

	@SuppressWarnings("unchecked")
	@Command
	public void CloseBarcodePrinting() {
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

	private void printRibbonLstItems(List<QbListsModel> zLstItems, BarcodePrintInfo bcPrintInfo, String printerName, boolean isPreview) {

		List<BufferedImage> lstRibImages = null;
		
		if (zLstItems != null && zLstItems.size() > 0) {

			QbListsModel qbListsModel = null;
			String barcodeType = null;
			lstRibImages = new ArrayList<BufferedImage>();
			
			// 1-Loop on items to print
			for (int i = 0; i < zLstItems.size(); i++) {

				qbListsModel = zLstItems.get(i);

				if (qbListsModel != null) {

					// 2-Add Info To Print to Top and Bottom Lists
					Object[] infoLists = getInfoLists(qbListsModel);
					
					// 3-Get Barcode Type for printing
					barcodeType = barcodeGen.getBarcodeType(qbListsModel.getBarcode());

					// 4-Prepare Barcode Image But add list of Info instead of 1
					// info
					BufferedImage currItemImg = barcodeGen.generatBarcode(qbListsModel.getBarcode(), (ArrayList)infoLists[0], (ArrayList)infoLists[1], barcodeType, bcPrintInfo.getBarcodeWidth(), bcPrintInfo.getBarcodeHeight());

					// 5-Print Barcode
					if(isPreview){
						
						for (int j=0; j<qbListsModel.getPrintQtyBarcode(); j++ ){
//							barcodeGen.printPreviewBarcode(currItemImg);
							lstRibImages.add(currItemImg);
						}
						
					}else{
						
						for (int j=0; j<qbListsModel.getPrintQtyBarcode(); j++ ){
							
//							barcodeGen.printBarcode(currItemImg, printerName, BARCODE_PRINT_TYPE_RB);
							lstRibImages.add(currItemImg);
						}
					}
				}
			}
			
			preparePdfPrint(lstRibImages, false);
		}
	}
	
	private Object[] getInfoLists(QbListsModel qbListsModel){

		//Maybe Add size of 3
		ArrayList<BarcodePrintInfo> lstInfoTop = new ArrayList<BarcodePrintInfo>();
		ArrayList<BarcodePrintInfo> lstInfoBot = new ArrayList<BarcodePrintInfo>();
		Object[] tmpTop = new Object[3];
		Object[] tmpBot = new Object[3];
		Object[] objLst = new Object[2];
		
		if (printItemName || printDescription || printSellingPrice) {

			BarcodePrintInfo bpi = null;
			if (printItemName && qbListsModel.getName() != null && !qbListsModel.getName().trim().equals("")) {
				bpi = new BarcodePrintInfo();
				bpi.setInfoDesc(qbListsModel.getName().trim());
				bpi.setInfoLoc( selectedInfPosItmName );
				
				if(selectedInfPosItmName!=null && selectedInfPosItmName.trim().contains("Top")){
					tmpTop[Integer.parseInt(printItemNameOrder)-1] = bpi;
//					lstInfoTop.add(Integer.parseInt(printItemNameOrder), qbListsModel.getName().trim());
				}else{
					tmpBot[Integer.parseInt(printItemNameOrder)-1] = bpi;
//					lstInfoBot.add(Integer.parseInt(printItemNameOrder)-1, qbListsModel.getName().trim());
				}
			}

			if (printDescription && qbListsModel.getPurchaseDesc() != null && !qbListsModel.getPurchaseDesc().trim().equals("")) {
				bpi = new BarcodePrintInfo();
				bpi.setInfoDesc(qbListsModel.getPurchaseDesc().trim());
				bpi.setInfoLoc( selectedInfPosItmDesc );
				
				if(selectedInfPosItmDesc!=null && selectedInfPosItmDesc.trim().contains("Top")){
					tmpTop[Integer.parseInt(printDescriptionOrder)-1] = bpi;
//					lstInfoTop.add(Integer.parseInt(printDescriptionOrder), qbListsModel.getPurchaseDesc().trim());
				}else{
					tmpBot[Integer.parseInt(printDescriptionOrder)-1] = bpi;
//					lstInfoBot.add(Integer.parseInt(printDescriptionOrder), qbListsModel.getPurchaseDesc().trim());
				}
			}

			if (printSellingPrice) {
				bpi = new BarcodePrintInfo();
				bpi.setInfoDesc(qbListsModel.getSalesPrice()+"");
				bpi.setInfoLoc( selectedInfPosItmSellPrice );
				
				if(selectedInfPosItmSellPrice!=null && selectedInfPosItmSellPrice.trim().contains("Top")){
					tmpTop[Integer.parseInt(printSellingPriceOrder)-1] = bpi;
//					lstInfoTop.add(Integer.parseInt(printSellingPriceOrder), qbListsModel.getSalesPrice());
				}else{
					tmpBot[Integer.parseInt(printSellingPriceOrder)-1] = bpi;
//					lstInfoBot.add(Integer.parseInt(printSellingPriceOrder), qbListsModel.getSalesPrice());
				}
			}
		}
		
		for(int i=0;i<tmpTop.length;i++){
			if(tmpTop[i]!=null){
				lstInfoTop.add(((BarcodePrintInfo)tmpTop[i]));
			}
		}
		
		for(int i=0;i<tmpBot.length;i++){
			if(tmpBot[i]!=null){
				lstInfoBot.add(((BarcodePrintInfo)tmpBot[i]));
			}
		}
		
		objLst[0] = lstInfoTop;
		objLst[1] = lstInfoBot;
		
		return objLst;
	}

	/*
	private void printA4LstItems(List<QbListsModel> zLstItems, BarcodePrintInfo bcPrintInfo, String printerName, boolean isPreview) {

		if (zLstItems != null && zLstItems.size() > 0) {

			BufferedImage A4Image = new BufferedImage((int) Math.floor(A4Width * 3.78) , (int) Math.floor(A4Height * 3.78),BufferedImage.TYPE_INT_ARGB);
			
			QbListsModel qbListsModel = null;
			// 1-Calculate Number of Barcodes and variables needed for printing
			// sizes and locations
//			int nbLabels = (A4Width /bcPrintInfo.getBarcodeWidth() ) * ( A4Height / bcPrintInfo.getBarcodeHeight());
			int nbPerRow = (int) (A4Width - (bcPrintInfo.getxMargin() * 2) ) / bcPrintInfo.getBarcodeWidth();
			int nbPerCol = (int) (A4Height - (bcPrintInfo.getyMargin() * 2) ) / bcPrintInfo.getBarcodeHeight();
			int currRow = (int) Math.floor(bcPrintInfo.getPrintLoc() / nbPerRow);
			currRow=currRow==0?1:currRow;
//			int currCol = nbPerRow - (bcPrintInfo.getPrintLoc() % nbPerRow);
			int currCol = bcPrintInfo.getPrintLoc() - (nbPerRow * (currRow-1));
			
			int sX = 0, sY = 0;
			boolean isPrinted = false;
			String barcodeType = "";

			// 2-Search For Location and in which A4 Image to print for each
			// item
			for (int i = 0; i < zLstItems.size(); i++) {

				qbListsModel = zLstItems.get(i);
				if (isPrinted) {
					isPrinted = false;
				}

				if (qbListsModel != null) {

					// 2-Add Info To Print to Top and Bottom Lists
					Object[] infoLists = getInfoLists(qbListsModel);

					// 3-Get Barcode Type for printing
					barcodeType = barcodeGen.getBarcodeType(qbListsModel.getBarcode());
					
					// 4-Prepare Barcode Image But add list of Info instead of 1
					// info
					BufferedImage currItemImg = barcodeGen.generatBarcode(qbListsModel.getBarcode(), (ArrayList)infoLists[0], (ArrayList)infoLists[1], barcodeType, bcPrintInfo.getBarcodeWidth(),bcPrintInfo.getBarcodeHeight());
//					BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
//					sendToPrinter.createTempFile(currItemImg, "C:/RamyNew/BarcodeInfo.png");
					
					for (int j = 0; j < qbListsModel.getPrintQtyBarcode(); j++) {

						// 5-Append Barcode to A4 @ position sX ; sY
						sX = (int) (((currCol - 1) * bcPrintInfo.getBarcodeWidth()) + bcPrintInfo.getxMargin() + (currCol * bcPrintInfo.getxSepa())) ;
						sY = (int) (((currRow - 1) * bcPrintInfo.getBarcodeHeight()) + bcPrintInfo.getyMargin() + (currRow * bcPrintInfo.getySepa()));
						
						A4Image = barcodeGen.appendA4Barcodes(A4Image,currItemImg, sX, sY);
//						sendToPrinter.createTempFile(A4Image, "C:/RamyNew/A4Image.png");
						
						// 6-Fix New Positions
						if (currCol < nbPerRow) {
							if (currCol==1 && currRow==1) {
								isPrinted = false;
							}
							currCol++;
						} else {
							isPrinted = false;
							currCol = 1;
							if (currRow < nbPerCol) {
								currRow++;
							} else {
								currRow = 1;
								if (i < zLstItems.size()) {
									// Print A4 page if this page is full and
									// create a new one;
									
									if(isPreview){
										
										barcodeGen.printPreviewBarcode(A4Image);
									}else{
										barcodeGen.printBarcode(A4Image,printerName, BARCODE_PRINT_TYPE_A4);
									}
									isPrinted = true;
//									A4Image = new BufferedImage(A4Width,A4Height,BufferedImage.TYPE_INT_ARGB);
									
									A4Image = new BufferedImage((int) Math.floor(A4Width * 3.78) , (int) Math.floor(A4Height * 3.78),BufferedImage.TYPE_INT_ARGB);
								}
							}
						}
					}
				}
			}
			
			// 7-Print A4 Barcodes if not printed above
			if (!isPrinted && zLstItems.size()>0) {
				// print A4Barcodes Page if barcodes were less than 1
				// paper
				if(isPreview){
					barcodeGen.printPreviewBarcode(A4Image);
				}else{
					barcodeGen.printBarcode(A4Image, printerName, BARCODE_PRINT_TYPE_A4);
				}
			}
		}
	}
	*/
	
	private void printA4LstItems(List<QbListsModel> zLstItems, BarcodePrintInfo bcPrintInfo, String printerName, boolean isPreview) {

		List<BufferedImage> lstA4Images = null;
		
		if (zLstItems != null && zLstItems.size() > 0) {
			
			lstA4Images = new ArrayList<BufferedImage>();

			BufferedImage A4Image = new BufferedImage((int) Math.floor(A4Width * 3.78) , (int) Math.floor(A4Height * 3.78),BufferedImage.TYPE_INT_ARGB);
			
			QbListsModel qbListsModel = null;
			// 1-Calculate Number of Barcodes and variables needed for printing
			// sizes and locations
//			int nbLabels = (A4Width /bcPrintInfo.getBarcodeWidth() ) * ( A4Height / bcPrintInfo.getBarcodeHeight());
			int nbPerRow = (int) (A4Width - (bcPrintInfo.getxMargin() * 2) ) / bcPrintInfo.getBarcodeWidth();
			int nbPerCol = (int) (A4Height - (bcPrintInfo.getyMargin() * 2) ) / bcPrintInfo.getBarcodeHeight();
			int currRow = (int) Math.floor(bcPrintInfo.getPrintLoc() / nbPerRow);
			currRow=currRow==0?1:currRow;
//			int currCol = nbPerRow - (bcPrintInfo.getPrintLoc() % nbPerRow);
			int currCol = bcPrintInfo.getPrintLoc() - (nbPerRow * (currRow-1));
			
			int sX = 0, sY = 0;
			boolean isPrinted = false;
			String barcodeType = "";

			// 2-Search For Location and in which A4 Image to print for each
			// item
			for (int i = 0; i < zLstItems.size(); i++) {

				qbListsModel = zLstItems.get(i);
				if (isPrinted) {
					isPrinted = false;
				}

				if (qbListsModel != null) {

					// 2-Add Info To Print to Top and Bottom Lists
					Object[] infoLists = getInfoLists(qbListsModel);

					// 3-Get Barcode Type for printing
					barcodeType = barcodeGen.getBarcodeType(qbListsModel.getBarcode());
					
					// 4-Prepare Barcode Image But add list of Info instead of 1
					// info
					BufferedImage currItemImg = barcodeGen.generatBarcode(qbListsModel.getBarcode(), (ArrayList)infoLists[0], (ArrayList)infoLists[1], barcodeType, bcPrintInfo.getBarcodeWidth(),bcPrintInfo.getBarcodeHeight());
//					BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
//					sendToPrinter.createTempFile(currItemImg, "C:/RamyNew/BarcodeInfo.png");
					
					for (int j = 0; j < qbListsModel.getPrintQtyBarcode(); j++) {

						// 5-Append Barcode to A4 @ position sX ; sY
						sX = (int) (((currCol - 1) * bcPrintInfo.getBarcodeWidth()) + bcPrintInfo.getxMargin() + (currCol * bcPrintInfo.getxSepa())) ;
						sY = (int) (((currRow - 1) * bcPrintInfo.getBarcodeHeight()) + bcPrintInfo.getyMargin() + (currRow * bcPrintInfo.getySepa()));
						
						A4Image = barcodeGen.appendA4Barcodes(A4Image,currItemImg, sX, sY);
//						sendToPrinter.createTempFile(A4Image, "C:/RamyNew/A4Image.png");
						
						// 6-Fix New Positions
						if (currCol < nbPerRow) {
							if (currCol==1 && currRow==1) {
								isPrinted = false;
							}
							currCol++;
						} else {
							isPrinted = false;
							currCol = 1;
							if (currRow < nbPerCol) {
								currRow++;
							} else {
								currRow = 1;
								if (i < zLstItems.size()) {
									// Print A4 page if this page is full and
									// create a new one;
									
									if(isPreview){
										
										lstA4Images.add(A4Image);
										
//										barcodeGen.printPreviewBarcode(A4Image);
										
									}else{
										
										lstA4Images.add(A4Image);
										
//										barcodeGen.printBarcode(A4Image,printerName, BARCODE_PRINT_TYPE_A4);
										
									}
									
									isPrinted = true;
//									A4Image = new BufferedImage(A4Width,A4Height,BufferedImage.TYPE_INT_ARGB);
									
									A4Image = new BufferedImage((int) Math.floor(A4Width * 3.78) , (int) Math.floor(A4Height * 3.78),BufferedImage.TYPE_INT_ARGB);
								}
							}
						}
					}
				}
			}
			
			// 7-Print A4 Barcodes if not printed above
			if (!isPrinted && zLstItems.size()>0) {
				// print A4Barcodes Page if barcodes were less than 1
				// paper
				if(isPreview){
					lstA4Images.add(A4Image);
					
//					barcodeGen.printPreviewBarcode(A4Image);
				}else{
					
					lstA4Images.add(A4Image);
					
//					barcodeGen.printBarcode(A4Image, printerName, BARCODE_PRINT_TYPE_A4);
				}
			}
			
			preparePdfPrint(lstA4Images, false);
			
		}
	}
	
	
	private void preparePdfPrint(List<BufferedImage> lstA4Images, boolean isRibbon){
		
		String pdfTitle = "";
		BufferedImage currBfImg = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		Image image = null;
		
		if(lstA4Images!=null && lstA4Images.size()>0){
			
			try 
			{
				
				Document doc = new Document();
				FileOutputStream pdfFileout = new FileOutputStream("C:/temp/R&RBarcodeImagePrint.pdf");
				PdfWriter.getInstance(doc, pdfFileout);
				
				if(isRibbon){
					pdfTitle = "A4 Barcodes";
				}else{
					pdfTitle = "Ribbon Barcodes";
				}
				
				currBfImg = lstA4Images.get(0);
				doc.setPageSize(new Rectangle(currBfImg.getWidth(), currBfImg.getHeight()));
				doc.addAuthor("Ramy & Rita");
				doc.addTitle(pdfTitle);
				doc.open();
				
				for(int i=0; i<lstA4Images.size(); i++){
					
					currBfImg = lstA4Images.get(i);
					baos = new ByteArrayOutputStream();
					ImageIO.write(currBfImg, "png", baos);

					baos.flush();
					byte[] retValue = baos.toByteArray();
					bytes = baos.toByteArray();
					baos.close();
					
					
					image = Image.getInstance(retValue);
					image.setAbsolutePosition(0, 0);
					
//					int size = image.getRowBytes() * image.getHeight();
					
					doc.newPage();
					doc.add(image);
				}
				
				doc.close();
				pdfFileout.close();
				
				previewPdfBarcodes();
				
				System.out.println("Success!");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Command
	 public void previewPdfBarcodes()
	 {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			//   arg.put("pdfContent", file);
			   Executions.createComponents("/hba/list/barcodePdfView.zul", null,arg);
		   }
		   catch (Exception ex)
		   {
			   logger.error("ERROR in CashInvoiceViewModel ----> previewPdfBarcodes", ex);
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
	
	@Command
	@NotifyChange({ "printItemNameOrder", "printDescriptionOrder","printSellingPriceOrder" })
	public void ChangeOrderItemName() {
		if (printItemName==true){
			printItemNameOrder=String.valueOf(orderCounter+1);
			orderCounter=orderCounter+1;
		}
		else{
			if (printItemNameOrder != null && printItemNameOrder.trim().equals(orderCounter+"")){
				printItemNameOrder="";
				orderCounter=orderCounter-1;
			}
			else{
				if (printDescriptionOrder !=null && !printDescriptionOrder.trim().equals("") && (Integer.parseInt(printDescriptionOrder.trim()) > Integer.parseInt(printItemNameOrder.trim()))) {
					printDescriptionOrder=String.valueOf(Integer.parseInt(printDescriptionOrder)-1);
				}
				if (printSellingPriceOrder !=null && !printSellingPriceOrder.trim().equals("") && (Integer.parseInt(printSellingPriceOrder.trim()) > Integer.parseInt(printItemNameOrder.trim()))) {
					printSellingPriceOrder=String.valueOf(Integer.parseInt(printSellingPriceOrder)-1);
				}
				printItemNameOrder="";
				orderCounter=orderCounter-1;
			}
		}
	}
	
	@Command
	@NotifyChange({ "printItemNameOrder", "printDescriptionOrder","printSellingPriceOrder" })
	public void ChangeOrderDescription() {
		if (printDescription==true){
			printDescriptionOrder=String.valueOf(orderCounter+1);
			orderCounter=orderCounter+1;
		}
		else{
			if (printDescriptionOrder != null && printDescriptionOrder.trim().equals(orderCounter+"")){
				printDescriptionOrder="";
				orderCounter=orderCounter-1;
			}
			else{
				if (printItemNameOrder !=null && !printItemNameOrder.trim().equals("") && (Integer.parseInt(printItemNameOrder.trim()) > Integer.parseInt(printDescriptionOrder.trim())) ) {
					printItemNameOrder=String.valueOf(Integer.parseInt(printItemNameOrder)-1);
				}
				if (printSellingPriceOrder !=null && !printSellingPriceOrder.trim().equals("") && (Integer.parseInt(printSellingPriceOrder.trim()) > Integer.parseInt(printDescriptionOrder.trim()))) {
					printSellingPriceOrder=String.valueOf(Integer.parseInt(printSellingPriceOrder)-1);
				}
				printDescriptionOrder="";
				orderCounter=orderCounter-1;
			}
		}
	}
	
	@Command
	@NotifyChange({ "printItemNameOrder", "printDescriptionOrder","printSellingPriceOrder" })
	public void ChangeOrderSellingPrice() {
		if (printSellingPrice==true){
			printSellingPriceOrder=String.valueOf(orderCounter+1);
			orderCounter=orderCounter+1;
		}
		else{
			if (printSellingPriceOrder != null && printSellingPriceOrder.trim().equals(orderCounter+"")){
				printSellingPriceOrder="";
				orderCounter=orderCounter-1;
			}
			else{
				if (printItemNameOrder !=null && !printItemNameOrder.trim().equals("") && (Integer.parseInt(printItemNameOrder.trim()) > Integer.parseInt(printSellingPriceOrder.trim())) ) {
					printItemNameOrder=String.valueOf(Integer.parseInt(printItemNameOrder)-1);
				}
				if (printDescriptionOrder !=null && !printDescriptionOrder.trim().equals("") && (Integer.parseInt(printDescriptionOrder.trim()) > Integer.parseInt(printSellingPriceOrder.trim()))) {
					printDescriptionOrder=String.valueOf(Integer.parseInt(printDescriptionOrder)-1);
				}
				printSellingPriceOrder="";
				orderCounter=orderCounter-1;
			}
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

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getSelectedBarcodeSize() {
		return selectedBarcodeSize;
	}

	public void setSelectedBarcodeSize(String selectedBarcodeSize) {
		this.selectedBarcodeSize = selectedBarcodeSize;
	}

	public String getSelectedStartLocation() {
		return selectedStartLocation;
	}

	public void setSelectedStartLocation(String selectedStartLocation) {
		this.selectedStartLocation = selectedStartLocation;
	}

	public String getSelectedInfPosItmName() {
		return selectedInfPosItmName;
	}

	public void setSelectedInfPosItmName(String selectedInfPosItmName) {
		this.selectedInfPosItmName = selectedInfPosItmName;
	}

	public String getSelectedInfPosItmDesc() {
		return selectedInfPosItmDesc;
	}

	public void setSelectedInfPosItmDesc(String selectedInfPosItmDesc) {
		this.selectedInfPosItmDesc = selectedInfPosItmDesc;
	}

	public String getSelectedInfPosItmSellPrice() {
		return selectedInfPosItmSellPrice;
	}

	public void setSelectedInfPosItmSellPrice(String selectedInfPosItmSellPrice) {
		this.selectedInfPosItmSellPrice = selectedInfPosItmSellPrice;
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

	
}
