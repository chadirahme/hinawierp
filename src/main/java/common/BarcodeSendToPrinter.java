package common;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.Sides;

import org.apache.log4j.Logger;
import org.zkoss.zhtml.Messagebox;

public class BarcodeSendToPrinter {

	static Logger logger = Logger.getLogger("BarcodePrintingViewModel");
//	public void printOutImage(BufferedImage imageToPrint, int intCopies, String name, String printerName) {
//	     
//	     try {
//	    	  //create a blank printJob
//	          PrinterJob printJob = PrinterJob.getPrinterJob();
//	          //make some attributes
//	          AttributeSet attSet = new HashAttributeSet();
//	          //this is the name of the printer I need to print to
//	          attSet.add(new PrinterName(printerName, null));
//	          //now find that printer
//	          PrintService[] arrPrintServices = PrintServiceLookup.lookupPrintServices(null, attSet);
//	          printJob.setPrintService(arrPrintServices[0]);
//	          //okay, now set the correct number of copies
//	          printJob.setCopies(intCopies);
//	          //give it a name
//	          printJob.setJobName(name);
//	          //and finally, send the BufferedImage
//	          printJob.setPrintable(new BarcodePrintInterface(imageToPrint));
//	          printJob.print();
//	     }
//
//	     catch (Exception e) {
//	          e.printStackTrace();
//	     } 
//	}
	
	private PrintService getPrinterService(String printerName){
	
		PrintService printer = null;
		
		if(printerName!=null && !printerName.trim().equals("")){
			PrintService[] pservices = PrintServiceLookup.lookupPrintServices(null, null);

	        //Acquire Printer
	        for (PrintService serv: pservices) {
	            System.out.println(serv.toString());
	            if (serv.getName().equals(printerName)) {
	                printer = serv;
	                break;
	            }
	        }
		}
		
		return printer;
	}
	
//	public ArrayList<String> getAllPrinters(){
//		
//		ArrayList<String> lstPrinter = null;
//		
//		PrintService[] pservices = PrintServiceLookup.lookupPrintServices(  null , null);
//		
//		if(pservices!=null && pservices.length>0){
//			
//			lstPrinter = new ArrayList<String>();
//			
//			//Acquire Printer
//	        for (PrintService serv: pservices) {
//	            System.out.println(serv.toString());
//	            if(serv!=null && !serv.getName().trim().equals(""))
//	            {
//	            	lstPrinter.add(serv.getName());
//	            }
//	        }
//		}
//		
//		return lstPrinter;
//	}
	
	public ArrayList<String> getAllPrinters(){
		
//		PrinterJob printerJob= PrinterJob.getPrinterJob();
//		
		ArrayList<String> lstPrinter = null;
//		
//		PrintService[] pservices = printerJob.lookupPrintServices();
//		
//		if(pservices!=null && pservices.length>0){
//			
			lstPrinter = new ArrayList<String>();
//			
//			//Acquire Printer
//	        for (PrintService serv: pservices) {
//	            System.out.println(serv.toString());
//	            if(serv!=null && !serv.getName().trim().equals(""))
//	            {
//	            	lstPrinter.add(serv.getName());
//	            }
//	        }
//		}
		
		PrintRequestAttributeSet pras =new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
		PrintService printService[] =PrintServiceLookup.lookupPrintServices( flavor, pras);
		for (int i=0;i<printService.length;i++)
		{
			PrintService serv=printService[i];
			
			lstPrinter.add(serv.getName());
			System.out.println (serv.getName());
		}
		
		return lstPrinter;
	}
	
	public void printOutImage(BufferedImage imageToPrint, int intCopies, String jobName, String printerName, String fileName, String printType){
		
		boolean isValid = false;
		String PRINT_TYPE_A4 = "1";
		
		try {

			PrintService printer = getPrinterService(printerName);

	        if (printer != null) {
	        	
	        	System.out.println("Found!");
	        	
	        	isValid = createTempFile(imageToPrint,fileName);
	        	
//		        	BarcodeSendToPrinter sendToPrinter = new BarcodeSendToPrinter();
//					sendToPrinter.createTempFile(imageToPrint, "C:/RamyNew/RIBBON-Barcode.png");
				
	        	if(isValid){

		            //Open File
		            FileInputStream fis = new FileInputStream(fileName);

		            //Create Doc out of file, autosense filetype
		            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.PNG, null);

		            //Create job for printer
		            DocPrintJob printJob = printer.createPrintJob();

		            //Create AttributeSet
		            PrintRequestAttributeSet pset = new HashPrintRequestAttributeSet();

		            //Add MediaTray to AttributeSet
//			            pset.add(MediaTray.TOP);

		            //Add Duplex Option to AttributeSet
		            pset.add(Sides.ONE_SIDED);
		            if(printType!=null && printType.trim().equals(PRINT_TYPE_A4)){
		            	pset.add(MediaSizeName.ISO_A4);
		            	pset.add(OrientationRequested.PORTRAIT);
		            }else{
		            	int mm = 1000;
		            	
		            	// in mm
		            	float mmW = (float) (imageToPrint.getWidth() / 3.78);
		            	float mmY = (float) (imageToPrint.getHeight() / 3.78);
		            	
		            	MediaPrintableArea x = new MediaPrintableArea(0, 0, mmW, mmY, mm); 
		            	pset.add(x);
		            }

		            //Print using Doc and Attributes
		            printJob.print(pdfDoc, pset);

		            //Close File
		            fis.close();

		            deleteTempFile(fileName);
	        	}
	        }else{
	        	Messagebox.show("Invalid Printer Name", "Barcode Printing",Messagebox.OK, org.zkoss.zul.Messagebox.EXCLAMATION);
	        }
	        
	    }
	    catch (Throwable t) {
	        t.printStackTrace();
	    }
	}
	
	public void printPreviewImage(BufferedImage imageToPreview){
		
		//createTempFile(imageToPreview, "C:/testRamy");	
		
		System.out.println(">>>>>>>>>R.Z>>>>>>>>> printPreviewImage - 1");
		logger.debug(">>>>>>>>>R.Z>>>>>>>>> printPreviewImage - 1");
		
    	BarcodePreview loadImg = new BarcodePreview(imageToPreview);
    	
    	System.out.println(">>>>>>>>>R.Z>>>>>>>>> 6");
		logger.debug(">>>>>>>>>R.Z>>>>>>>>> 6");
		
    	loadImg.showPreviewImage(imageToPreview);
	    	
	}
	
	public boolean createTempFile(BufferedImage imageToPrint, String fileName){
		
		boolean created = true;
		try {
			ImageIO.write(imageToPrint, "png", new File(fileName));
		} catch (IOException e) {
			created = false;
			e.printStackTrace();
		}
		
		return created;
	}
	
	private boolean deleteTempFile(String fileName){
		
		boolean deleted = true;
		
		try{
 
    		File file = new File(fileName);
 
    		if(!file.delete()){
    			deleted = false;
    		}
 
    	}catch(Exception e){
 
    		deleted = false;
    		e.printStackTrace();
    	}
		
		return deleted;
	}
	
}

