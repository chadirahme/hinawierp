package common;

import hba.ItemsData;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import model.BarcodeSettingsModel;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.EAN8Bean;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.zkoss.zul.Messagebox;

public class BarcodeGeneration {
	
	private static int printCounter = 1;
	private static final String BARCODE_TYPE_UPC = "UPC";
	private static final String BARCODE_TYPE_BC128 = "BC128";
	private static final String BARCODE_TYPE_EAN13 = "EAN13";
	private static final String BARCODE_TYPE_EAN8 = "EAN8";
	private static final String BARCODE_TYPE_UPCA = "UPCA";
	private static final String BARCODE_TYPE_UPCE = "UPCE";
	private static final String PRINT_TOP_LEFT = "Top Left";
	private static final String PRINT_TOP_CENTER = "Top Center";
	private static final String PRINT_TOP_RIGHT = "Top Right";
	private static final String PRINT_BOT_LEFT = "Bottom Left";
	private static final String PRINT_BOT_CENTER = "Bottom Center";
	private static final String PRINT_BOT_RIGHT = "Bottom Right";
	
	private static final int A4Width = 210, A4Height = 297, dpi = 600;
	private static final String fileName="C:/A4BCIMAGE_TMP.png";
	
	ItemsData data = new ItemsData();
	private BarcodeSettingsModel fillBarcodeSettings;
	
	public BarcodeGeneration() {
		fillBarcodeSettings = data.fillBarcodeSettings();	
	}
	
	//Function to generate a barcode
		public String generateBarcodeRecursive() {
			String returnVal = "";
			// If generation of barcode is successfully completed, then update
			// counter in database
			boolean UpdateCounter = false;
			if(fillBarcodeSettings==null){
				Messagebox.show(
						"Barcode Settings Should Be Defined!",
						"Barcode Generation", Messagebox.OK, Messagebox.INFORMATION);
				return "";
			}
			
			// if barcode type to generate is EAN13
			if (fillBarcodeSettings != null
					&& fillBarcodeSettings.getBarcodeType().equals("EAN13")) {
				if (String.valueOf(fillBarcodeSettings.getBarcodeCounter())
						.length() != 12) {
					Messagebox.show(
							"Invalid Barcode Counter From Barcode Settings",
							"Barcode", Messagebox.OK, Messagebox.EXCLAMATION);
					returnVal = "";
				} else {
					UpdateCounter = true;
					returnVal = chkGenBarcEANx(
							String.valueOf(fillBarcodeSettings.getBarcodeCounter())
									+ "0", 13, true);
				}
			}
			// if barcode type to generate is EAN8
			else if (fillBarcodeSettings != null
					&& fillBarcodeSettings.getBarcodeType().equals("EAN8")) {
				if (String.valueOf(fillBarcodeSettings.getBarcodeCounter())
						.length() != 7) {
					Messagebox.show(
							"Invalid Barcode Counter From Barcode Settings",
							"Barcode", Messagebox.OK, Messagebox.EXCLAMATION);
					returnVal = "";
				} else {
					UpdateCounter = true;
					returnVal = chkGenBarcEANx(
							String.valueOf(fillBarcodeSettings.getBarcodeCounter())
									+ "0", 8, true);
				}

			} else if (fillBarcodeSettings != null
					&& fillBarcodeSettings.getBarcodeType().equals("UPCA")) {
				if (String.valueOf(fillBarcodeSettings.getBarcodeCounter())
						.length() != 11) {
					Messagebox.show(
							"Invalid Barcode Counter From Barcode Settings",
							"Barcode", Messagebox.OK, Messagebox.EXCLAMATION);
					returnVal = "";
				} else {
					UpdateCounter = true;
					returnVal = chkGenBarcUPCA(
							String.valueOf(fillBarcodeSettings.getBarcodeCounter())
									+ "0", 13, true);
				}
			} else if (fillBarcodeSettings != null
					&& fillBarcodeSettings.getBarcodeType().equals("UPCE")) {
				if (String.valueOf(fillBarcodeSettings.getBarcodeCounter())
						.length() != 6) {
					Messagebox.show(
							"Invalid Barcode Counter From Barcode Settings",
							"Barcode", Messagebox.OK, Messagebox.EXCLAMATION);
					returnVal = "";
				} else {
					
					String barcCounter ;
					UpdateCounter = true;
					barcCounter="000000" +  String.valueOf(Integer.parseInt(fillBarcodeSettings.getBarcodeCounter())+1);
						
					returnVal = barcCounter.substring(barcCounter.length()-6,barcCounter.length());
					
					System.out.println(returnVal);
				}
			}

			if (UpdateCounter == true) {
				String barcCounterUpdate="";
				
				if (fillBarcodeSettings.getBarcodeType().equals("EAN13")){
					barcCounterUpdate="000000000000" + String.valueOf(Integer.parseInt(fillBarcodeSettings.getBarcodeCounter()) + 1);
					barcCounterUpdate=barcCounterUpdate.substring(barcCounterUpdate.length()-12,barcCounterUpdate.length());
				}
				else if (fillBarcodeSettings.getBarcodeType().equals("EAN8")){
					barcCounterUpdate="000000" + String.valueOf(Integer.parseInt(fillBarcodeSettings.getBarcodeCounter()) + 1);
					barcCounterUpdate=barcCounterUpdate.substring(barcCounterUpdate.length()-7,barcCounterUpdate.length());
				}
				else if (fillBarcodeSettings.getBarcodeType().equals("UPCE")){
					barcCounterUpdate="00000" + String.valueOf(Integer.parseInt(fillBarcodeSettings.getBarcodeCounter()) + 1);
					barcCounterUpdate=barcCounterUpdate.substring(barcCounterUpdate.length()-6,barcCounterUpdate.length());
				}
				else if (fillBarcodeSettings.getBarcodeType().equals("UPCA")){
					barcCounterUpdate="0000000000" + String.valueOf(Integer.parseInt(fillBarcodeSettings.getBarcodeCounter()) + 1);
					barcCounterUpdate=barcCounterUpdate.substring(barcCounterUpdate.length()-11,barcCounterUpdate.length());
				}
				
				data.updateBarcodeCounter(barcCounterUpdate);
				
				fillBarcodeSettings.setBarcodeCounter(barcCounterUpdate);

				// Check if barcode exist for another item. if yes, then recall the
				// function to generate another barcode
				if (data.checkIfBcExist(returnVal) == true) {
					generateBarcodeRecursive();
				}

			}

			return returnVal;
		}
		
		public String chkGenBarcEANx(String wBarcode, int n, boolean Generate) {
			int WVal;
			String Wchk;
			String[] Tun = new String[n];
			String[] Ttab = new String[n - 1];
			int WRem;
			int WRem2;
			int n1;
			int n2;
			String returnedBarcode = "";

			// calculate check digit of barcode
			Wchk = "";
			WVal = 0;

			if (n == 8) {
				n1 = 1;
				n2 = 3;
			} else {
				n1 = 3;
				n2 = 1;
			}

			for (int i = 1; i <= wBarcode.length() - 1; i++) {

				Tun[i] = wBarcode.substring(i - 1, i);

				if (i == (i / 2) * 2) {
					Ttab[i - 1] = String.valueOf(Integer.parseInt(Tun[i]) * n1);
				} else {
					Ttab[i - 1] = String.valueOf(Integer.parseInt(Tun[i]) * n2);
				}
				WVal = WVal + Integer.parseInt(Ttab[i - 1]);
			}

			// divide by 10,get reminder

			WRem = WVal - ((WVal / 10) * 10);

			if (WRem != 0) {
				WRem2 = 10 - WRem;
			} else {
				WRem2 = 0;
			}

			Wchk = String.valueOf(WRem2);

			// if generate=false --> if only check if the barcode is valid, then
			// return yes or no
			// else, return the generated barcode
			if (Generate == false) {
				if (Integer.parseInt(Wchk) == Integer.parseInt(wBarcode.substring(
						wBarcode.length() - 1, wBarcode.length()))) {
					return "Y";
				} else {
					return "N";
				}
			} else {
				returnedBarcode = wBarcode.substring(0, wBarcode.length() - 1)
						+ String.valueOf(Wchk);
				return returnedBarcode;
			}

		}
		
		public String chkGenBarcUPCA(String wBarcode, int n, boolean Generate) {
			int WVal;
			String Wchk;
			String[] Tun = new String[n];
			String[] Ttab = new String[n - 1];
			int WRem;
			int WRem2;
			String returnedBarcode = "";

			// calculate check digit of barcode
			Wchk = "";
			WVal = 0;

			for (int i = 1; i <= wBarcode.length() - 1; i++) {

				Tun[i] = wBarcode.substring(i - 1, i);
				if (i - 1 == (i / 2) * 2) {
					Ttab[i - 1] = String.valueOf(Integer.parseInt(Tun[i]) * 3);
				} else {
					Ttab[i - 1] = String.valueOf(Integer.parseInt(Tun[i]) * 1);
				}
				WVal = WVal + Integer.parseInt(Ttab[i - 1]);
			}

			// divide by 10,get reminder

			WRem = WVal - ((WVal / 10) * 10);

			if (WRem != 0) {
				WRem2 = 10 - WRem;
			} else {
				WRem2 = 0;
			}

			Wchk = String.valueOf(WRem2);

			// if generate=false --> if only check if the barcode is valid, then
			// return yes or no
			// else, return the generated barcode
			if (Generate == false) {
				if (Integer.parseInt(Wchk) == Integer.parseInt(wBarcode.substring(
						wBarcode.length() - 1, wBarcode.length()))) {
					return "Y";
				} else {
					return "N";
				}
			} else {
				returnedBarcode = wBarcode.substring(0, wBarcode.length() - 1)
						+ String.valueOf(Wchk);
				return returnedBarcode;
			}

		}
		
		public String ValidateBarcode(String barcode) {
			String Result="";
			
			// If the length of the entered barcode is 13, then check if it is EAN13
			
			if (barcode != null && !barcode.trim().equals("")) {
				if ((barcode.length() == 13 || barcode.length() == 8) && isNumeric(barcode)==true) {
					if (chkGenBarcEANx(barcode, barcode.length(),
							false).equals("N")) {
						Messagebox.show("Invalid Barcode", "Barcode",
								Messagebox.OK, Messagebox.EXCLAMATION);
						Result = "";
//						selectedItem.setBarcode("");
//						return;
					}
					else{
						Result=barcode;
					}
				}
				// If the length of the entered barcode is 12, then check if it is UPCA
				// UPCA barcode should be 12 digit
				// UPCE barcodes can be any numeric value with 6 digits. The calculation of the checksum is done while printing
				// If convert barcode is "Yes", then the barcode will be expanded to
				// EAN13 by adding a checkdigit
				else if (barcode.length() == 12 && isNumeric(barcode)==true
						&& chkGenBarcUPCA(barcode,
								barcode.length(), false).equals("N")) {
					if (fillBarcodeSettings.getBarcodeConvert()==null || fillBarcodeSettings.getBarcodeConvert().equals("N")) {
						Messagebox.show("Invalid Barcode", "Barcode",
								Messagebox.OK, Messagebox.EXCLAMATION);
						Result = "";
//						selectedItem.setBarcode("");
//						return;
					} else {
						Result = chkGenBarcEANx(
								String.valueOf(barcode) + "0", 13, true);
//						selectedItem.setBarcode(barcode);
					}

				}
				// If convert barcode is "Yes", and the length is less than 8, then
				// the barcode will be expanded to EAN8 by adding a zeros in the
				// beginning and a checkdigit at the end
				else if (barcode.length() < 8 && isNumeric(barcode)==true
						&& fillBarcodeSettings.getBarcodeConvert() != null && fillBarcodeSettings.getBarcodeConvert().equals("Y")) {
					String Barcode = "0000000" + String.valueOf(barcode);

					Result = chkGenBarcEANx(
							Barcode.substring(Barcode.length() - 7,
									Barcode.length())
									+ "0", 8, true);
//					selectedItem.setBarcode(barcode);
				}
				// If convert barcode is "Yes", and the length is less than 12, then
				// the barcode will be expanded to UPCA by adding a zeros in the
				// beginning and a checkdigit at the end
				else if (barcode.length() < 12 && isNumeric(barcode)==true
						&& fillBarcodeSettings.getBarcodeConvert() != null && fillBarcodeSettings.getBarcodeConvert().equals("Y")) {
					String Barcode = "00000000000"
							+ String.valueOf(barcode);

					Result = chkGenBarcUPCA(
							Barcode.substring(Barcode.length() - 11,
									Barcode.length())
									+ "0", 12, true);
//					selectedItem.setBarcode(barcode);
				}
				else{
					Result=barcode;
				}

			}
			
			return Result;
		}
	
//	private void testBarcode(){
//		
//		BufferedImage A4Image = new BufferedImage(A4Width, A4Height, BufferedImage.TYPE_INT_ARGB);
//		
//		//RUN BARCODE 128
//		String barcode = "ABC123";
//		List<String> lstInfo = new ArrayList<String>();
//		lstInfo.add("Description");
////		BufferedImage bcImg = generatBarcode(barcode, info, BARCODE_TYPE_BC128);
//		
//		barcode = "1234567890128";
//		BufferedImage bcImg = generatBarcode(barcode, lstInfo, BARCODE_TYPE_EAN13,105,43);
//		//generateBC128(barcode1);
//		
//		A4Image = appendA4Barcodes(bcImg,bcImg, 0,0);
//	}

	/**
	 * Generates Barcode 128
	 * @param barcode
	 * @param info
	 * @return
	 */
	public BufferedImage generatBarcode(String barcode, List<BarcodePrintInfo> lstInfoTop, List<BarcodePrintInfo> lstInfoBottom , String bcType, int widthInMm, int heightInMm){
		
		//TODO Add Size , APPEND RIGHT OR TOP (Location)
		BufferedImage barcodeImage = null;
		
		if (bcType!=null){
			
			if(bcType.trim().equalsIgnoreCase(BARCODE_TYPE_BC128)){
				
				barcodeImage = generateBarcode128(barcode, 0);
				
			}
			else if(bcType.trim().equalsIgnoreCase(BARCODE_TYPE_EAN13)){
				
				barcodeImage = generateBarcodeEan13(barcode, 0);
				
			}
			else if(bcType.trim().equalsIgnoreCase(BARCODE_TYPE_EAN8)){
				
				barcodeImage = generateBarcodeEan8(barcode, 0);
				
			}
			else if(bcType.trim().equalsIgnoreCase(BARCODE_TYPE_UPCE)){
				
				barcodeImage = generateBarcodeUPCE(barcode, 0);
				
			}
			else if(bcType.trim().equalsIgnoreCase(BARCODE_TYPE_UPCA)){
				
				barcodeImage = generateBarcodeUPC(barcode, 0);
				
			}
			
//			BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
//			sendToPrinter.createTempFile(barcodeImage, "C:/RamyNew/1-BarcodeGen.png");
			
			barcodeImage = fillBarcodeInfoImage(barcodeImage, lstInfoTop, lstInfoBottom);
			
//			sendToPrinter.createTempFile(barcodeImage, "C:/RamyNew/2-BarcodeFillinfo.png");
			
			barcodeImage = resizeImg(barcodeImage, widthInMm, heightInMm);
			
//			sendToPrinter.createTempFile(barcodeImage, "C:/RamyNew/3-Barcoderesize.png");
		}
		
		
		return barcodeImage;
	}
	
	private static BufferedImage generateBarcode128(String barcode, int orientation){
		
		BufferedImage image = null;
		try
		{
			//Preparation of barcode
			Code128Bean bean = new Code128Bean();
//			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
//			bean.doQuietZone(false);
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_INT_BGR, false);
			bean.generateBarcode(canvas, new String(barcode.getBytes(), "UTF-8") );
			canvas.finish();
			
			image = canvas.getBufferedImage();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION:"+ex.toString());
		}
		
		return image;
	}
	
	
	
	private static BufferedImage generateBarcodeEan13(String barcode, int orientation){
		
		BufferedImage image = null;
		try
		{
			//Preparation of barcode
			EAN13Bean bean = new EAN13Bean();
//			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
//			bean.doQuietZone(false);
			BitmapCanvasProvider canvas2 = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_INT_RGB, false); //or (dpi, orientation, false); //new BitmapCanvasProvider(dpi, BufferedImage.TYPE_INT_RGB, false, orientation);
			bean.generateBarcode(canvas2, new String(barcode.getBytes(), "UTF-8") );
			canvas2.finish();
			
			image = canvas2.getBufferedImage();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION:"+ex.toString());
		}
		
		return image;
	}
	
	private static BufferedImage generateBarcodeEan8(String barcode, int orientation){
		
		BufferedImage image = null;
		try
		{
			//Preparation of barcode
			EAN8Bean bean = new EAN8Bean();
//			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
//			bean.doQuietZone(false);
			BitmapCanvasProvider canvas2 = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_INT_RGB, false);
			bean.generateBarcode(canvas2, new String(barcode.getBytes(), "UTF-8") );
			canvas2.finish();
			
			image = canvas2.getBufferedImage();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION:"+ex.toString());
		}
		
		return image;
	}
	
	private static BufferedImage generateBarcodeUPC(String barcode, int orientation){
		
		BufferedImage image = null;
		try
		{
			//Preparation of barcode
			UPCABean bean = new UPCABean();
//			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
//			bean.doQuietZone(false);
			BitmapCanvasProvider canvas2 = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_INT_RGB, false);
			bean.generateBarcode(canvas2, new String(barcode.getBytes(), "UTF-8") );
			canvas2.finish();
			
			image = canvas2.getBufferedImage();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION:"+ex.toString());
		}
		
		return image;
	}
	
	private static BufferedImage generateBarcodeUPCE(String barcode, int orientation){
		
		BufferedImage image = null;
		try
		{
			//Preparation of barcode
			UPCEBean bean = new UPCEBean();
//			bean.setModuleWidth(UnitConv.in2mm(2.8f / dpi));
//			bean.doQuietZone(false);
			BitmapCanvasProvider canvas2 = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_INT_RGB, false);
			bean.generateBarcode(canvas2, new String(barcode!=null && barcode.length()==6 ? ("0"+barcode).getBytes() : barcode.getBytes(), "UTF-8") );
			canvas2.finish();
			
			image = canvas2.getBufferedImage();
			
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("EXCEPTION:"+ex.toString());
		}
		
		return image;
	}
	
	private static BufferedImage fillBarcodeInfoImage(BufferedImage bcImage, List<BarcodePrintInfo> lstInfoTop, List<BarcodePrintInfo> lstInfoBottom){
		
		BufferedImage newImage = null;
		int infoSizeTop = 0,infoSizeBottom = 0,fontSize=75;
		int heightAdd = 100;
		int startX;
		int marginX = 15, marginY = 30, marginYTot=0, startY=0, topSectionY=0;
		BarcodePrintInfo bpi = null;
		
		if(bcImage!=null && (lstInfoTop!=null || lstInfoBottom!=null)){
			
			if(lstInfoTop!=null && lstInfoTop.size()>0){
				infoSizeTop = lstInfoTop.size();
			}
			
			if(lstInfoBottom!=null && lstInfoBottom.size()>0){
				infoSizeBottom = lstInfoBottom.size();
			}
			
			marginX = (infoSizeBottom + infoSizeTop) * marginX;
			
			//=  Y space 4 Top & Bot + Empty Y spac8e 4 each Top BC + Empty y space 4 each Bot BC + Top Sizes BC * nbr of BC + Bottom Sizes BC * nbr of BC
			marginYTot = ( marginY * 3 ) + (infoSizeTop * marginY) + (infoSizeBottom * marginY) +  (heightAdd * infoSizeTop) + (heightAdd * infoSizeBottom) ;
			//Y Space 4 Top + Empty Space 4 each top BC + Space for Size of BC * nbr of BC 
			startY = (marginY * 2) + (infoSizeTop * marginY) + (heightAdd * infoSizeTop);
			
			//Adding Text To Generated Barcode Image
//	        newImage = new BufferedImage(bcImage.getWidth()+ (marginX * 2), bcImage.getHeight() + (marginY * 2) + (infoSizeTop * (heightAdd+marginY)) + (infoSizeBottom * heightAdd) , BufferedImage.TYPE_INT_ARGB);
	        newImage = new BufferedImage(bcImage.getWidth()+ (marginX * 2), bcImage.getHeight() + marginYTot , BufferedImage.TYPE_INT_ARGB);
	        
	        Font fnt=new Font("Dante",1,fontSize);
	        Color fntC = new Color(10, 10, 10);
            Graphics g = newImage.getGraphics();
            
            g.fillRect(0, 0, newImage.getWidth() + (marginX * 2), bcImage.getHeight() + marginYTot );
            g.drawImage(bcImage, marginX , startY, bcImage.getWidth() + marginX - (marginX/2)  , bcImage.getHeight(),Color.white, null);
            g.setFont(fnt);
            g.setColor(fntC);
            
            //info is the String to print top
        	for(int i=1;i<=lstInfoTop.size();i++){
        		bpi = lstInfoTop.get(i-1);
        		if(bpi!=null && bpi.getInfoDesc()!=null && !bpi.getInfoDesc().trim().equals("")){
        			if(bpi.getInfoLoc().equals(PRINT_TOP_CENTER)){
        				startX = (int)(newImage.getWidth() - g.getFontMetrics().getStringBounds(bpi.getInfoDesc().trim(), g).getWidth())/2;
        			}else if(bpi.getInfoLoc().equals(PRINT_TOP_LEFT)){
        				startX = marginX; // * lstInfoTop.size();
        			}else{
        				startX = (int)(newImage.getWidth() - g.getFontMetrics().getStringBounds(bpi.getInfoDesc().trim(), g).getWidth()) - ( (marginX/2) * lstInfoTop.size());
        			}
        			
        			g.drawString(bpi.getInfoDesc().trim(), startX , (marginY - 15) + ( i * marginY) + ( i * heightAdd) ) ;
        		}
        	}
        	
        	topSectionY = marginY + ( lstInfoTop.size() * marginY) + ( lstInfoTop.size() * heightAdd);
            //info is the String to print below
        	for(int i=1;i<=lstInfoBottom.size();i++){
        		
        		bpi = lstInfoBottom.get(i-1);
        		if(bpi!=null && bpi.getInfoDesc()!=null && !bpi.getInfoDesc().trim().equals("")){
        			if(bpi.getInfoLoc().equals(PRINT_BOT_CENTER)){
        				startX = (int)(newImage.getWidth() - g.getFontMetrics().getStringBounds(bpi.getInfoDesc().trim(), g).getWidth())/2;
        			}else if(bpi.getInfoLoc().equals(PRINT_BOT_LEFT)){
        				startX = marginX;// * lstInfoBottom.size();
        			}else{
        				startX = (int)(newImage.getWidth() - g.getFontMetrics().getStringBounds(bpi.getInfoDesc().trim(), g).getWidth()) - ( (marginX/2) * lstInfoBottom.size());
        			}
        			g.drawString(bpi.getInfoDesc().trim(), startX , topSectionY + bcImage.getHeight() + ( i * marginY) + ( i * heightAdd) ) ;
        		}
        	}
            
            g.dispose();
            
			System.out.println("Bar Code is generated successfully...");
		}
		
		return newImage;
	}
	
	
	private static BufferedImage resizeImg(BufferedImage barcodeImage, int mmWidth, int mmHeight){
		
		int pixelX , pixelY;
		pixelX = (int) Math.floor(mmWidth * 3.78); //264
		pixelY = (int) Math.floor(mmHeight * 3.78);
		
		BufferedImage zNewImage = new BufferedImage(pixelX, pixelY, BufferedImage.TYPE_INT_RGB );
		Graphics g = zNewImage.createGraphics();
		g.drawImage(barcodeImage , 0, 0, pixelX, pixelY,null);
		g.dispose();
		
		return zNewImage;
	}
	
	public void printBarcode(BufferedImage bcImg, String printerName, String printType){
		
		BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
		
		sendToPrinter.printOutImage(bcImg, 1, "Barcode Printing", printerName, fileName, printType);
				
	}
	
	public void printPreviewBarcode(BufferedImage bcImg){
		
		BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
		
		sendToPrinter.printPreviewImage(bcImg);
	}
	
	
	public static BufferedImage appendA4Barcodes(BufferedImage A4Image, BufferedImage bcImage, int sX, int sY){
		
		if(bcImage!=null){
			int pixelX = (int) Math.floor(sX * 3.78); //264
			int pixelY = (int) Math.floor(sY * 3.78);
			Graphics g = A4Image.getGraphics();
//			g.drawImage(bcImage, sX, sY, bcImage.getWidth(), bcImage.getHeight(),Color.white, null);
            g.drawImage(bcImage, pixelX, pixelY, bcImage.getWidth(), bcImage.getHeight(),Color.white, null);
            g.dispose();
		}
			
		return A4Image;
	}
	
	public String getBarcodeType(String barcode) {

		String bcType = null;
		String isValid = null;

		if (barcode != null && !barcode.trim().equals("")) {

			int bcLength = barcode.trim().length();

			if ((bcLength == 13 || bcLength == 8) && isNumeric(barcode)==true) {

				isValid = chkGenBarcEANx(barcode, bcLength, false);

				if (isValid != null && isValid.trim().equals("Y")) {

					bcType = bcLength == 13 ? BARCODE_TYPE_EAN13 : BARCODE_TYPE_EAN8;
				}
				else{
					bcType = BARCODE_TYPE_BC128;
				}

			} else if (bcLength == 12 && isNumeric(barcode)==true) {

				isValid = chkGenBarcUPCA(barcode, bcLength, false);

				if (isValid != null && isValid.trim().equals("Y")) {

					bcType = BARCODE_TYPE_UPCA;
				}
				else{
					bcType = BARCODE_TYPE_BC128;
				}
			} else if (bcLength == 6 && isNumeric(barcode)==true) {

				bcType = BARCODE_TYPE_UPCE;

			} else {

				bcType = BARCODE_TYPE_BC128;
			}
		}

		return bcType;
	}
	
	public boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
