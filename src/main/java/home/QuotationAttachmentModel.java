package home;

import org.zkoss.util.media.Media;

public class QuotationAttachmentModel 
{

	private int attachid;
	private int quotationid;
	private String filename;
	private String filepath;
	private String sessionid;
	private Media imageMedia;
	private int serialNumber=0;
	
	private int formId;
	private String nameType;
	private int userId;
	
	
	public int getAttachid() {
		return attachid;
	}
	public void setAttachid(int attachid) {
		this.attachid = attachid;
	}
	public int getQuotationid() {
		return quotationid;
	}
	public void setQuotationid(int quotationid) {
		this.quotationid = quotationid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	
	public Media getImageMedia() {
		return imageMedia;
	}
	public void setImageMedia(Media imageMedia) {
		this.imageMedia = imageMedia;
	}
	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public String getNameType() {
		return nameType;
	}
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	
	
	
}
