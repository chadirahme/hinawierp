package common;

public class BarcodePrintInfo {

	private String printType;
	private String printerName;
	private String printSize;
	private int printLoc;
	private String infoDesc;
	private String infoLoc;
	private int barcodeWidth;
	private int barcodeHeight;
	private double xMargin;
	private double yMargin;
	private double xSepa;
	private double ySepa;
	private int nbLabels;
	
//	private static int A4Width = 210;
//	private static int A4Height = 297;
	
	public String getPrintType() {
		return printType;
	}
	public void setPrintType(String printType) {
		this.printType = printType;
	}
	public String getPrinterName() {
		return printerName;
	}
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	public String getPrintSize() {
		return printSize;
	}
	public void setPrintSize(String printSize) {
		this.printSize = printSize;
	}
	public int getPrintLoc() {
		return printLoc;
	}
	public void setPrintLoc(int printLoc) {
		this.printLoc = printLoc;
	}
	public int getBarcodeWidth() {
		return barcodeWidth;
	}
	public void setBarcodeWidth(int barcodeWidth) {
		this.barcodeWidth = barcodeWidth;
	}
	public int getBarcodeHeight() {
		return barcodeHeight;
	}
	public void setBarcodeHeight(int barcodeHeight) {
		this.barcodeHeight = barcodeHeight;
	}
	public String getInfoDesc() {
		return infoDesc;
	}
	public void setInfoDesc(String infoDesc) {
		this.infoDesc = infoDesc;
	}
	public String getInfoLoc() {
		return infoLoc;
	}
	public void setInfoLoc(String infoLoc) {
		this.infoLoc = infoLoc;
	}
	public double getxMargin() {
		return xMargin;
	}
	public void setxMargin(double xMargin) {
		this.xMargin = xMargin;
	}
	public double getyMargin() {
		return yMargin;
	}
	public void setyMargin(double yMargin) {
		this.yMargin = yMargin;
	}
	public double getxSepa() {
		return xSepa;
	}
	public void setxSepa(double xSepa) {
		this.xSepa = xSepa;
	}
	public double getySepa() {
		return ySepa;
	}
	public void setySepa(double ySepa) {
		this.ySepa = ySepa;
	}
	public int getNbLabels() {
		return nbLabels;
	}
	public void setNbLabels(int nbLabels) {
		this.nbLabels = nbLabels;
	}
	
//	public static int getA4Width() {
//		return A4Width;
//	}
//	public static int getA4Height() {
//		return A4Height;
//	}
	
}
