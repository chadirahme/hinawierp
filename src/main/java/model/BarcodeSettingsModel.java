package model;

import java.util.Date;
import java.util.List;


public class BarcodeSettingsModel {
	private String barcodeType;
	private String barcodeCounter;
	private String barcodeConvert;
	private boolean barcodeConvertBool;
//	private String barcodeDefaultPrinter;
	private String barcodeAfterScanGoTo;
	
	public String getBarcodeType() {
		return barcodeType;
	}
	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}
	public String getBarcodeCounter() {
		return barcodeCounter;
	}
	public void setBarcodeCounter(String barcodeCounter) {
		this.barcodeCounter = barcodeCounter;
	}
//	public String getBarcodeDefaultPrinter() {
//		return barcodeDefaultPrinter;
//	}
//	public void setBarcodeDefaultPrinter(String barcodeDefaultPrinter) {
//		this.barcodeDefaultPrinter = barcodeDefaultPrinter;
//	}
	public String getBarcodeConvert() {
		return barcodeConvert;
	}
	public void setBarcodeConvert(String barcodeConvert) {
		this.barcodeConvert = barcodeConvert;
	}
	public boolean isBarcodeConvertBool() {
		return barcodeConvertBool;
	}
	public void setBarcodeConvertBool(boolean barcodeConvertBool) {
		this.barcodeConvertBool = barcodeConvertBool;
	}
	public String getBarcodeAfterScanGoTo() {
		return barcodeAfterScanGoTo;
	}
	public void setBarcodeAfterScanGoTo(String barcodeAfterScanGoTo) {
		this.barcodeAfterScanGoTo = barcodeAfterScanGoTo;
	}
	

}
