package model;

import java.util.Date;

public class PurchaseRequestModel {
	
	
	private int recNo;
	
	private int vendorRefKEy;
	
	private Date txtnDate;
	
	private String refNUmber;
	
	private int entityRefKey;
	
	private int classRefkey;
	
	private int inventroySiteIteKey;
	
	private String shipTo;
	
	private String adress;
	
	private String adressBillTo;
	
	private double amount;
	
	private String memo;
	
	private String isManualyClosed;
	
	private String isFullyReceived="N";
	
	private String status;
	
	private String source;
	
	private String itemClassHIde;
	
	private String itemDecHide;
	
	private int webUserId;
	
	private String transformMR="N";
	
	private Date reqDate;
	
	
	public int getRecNo() {
		return recNo;
	}

	public void setRecNo(int recNo) {
		this.recNo = recNo;
	}

	public int getVendorRefKEy() {
		return vendorRefKEy;
	}

	public void setVendorRefKEy(int vendorRefKEy) {
		this.vendorRefKEy = vendorRefKEy;
	}

	public Date getTxtnDate() {
		return txtnDate;
	}

	public void setTxtnDate(Date txtnDate) {
		this.txtnDate = txtnDate;
	}

	public String getRefNUmber() {
		return refNUmber;
	}

	public void setRefNUmber(String refNUmber) {
		this.refNUmber = refNUmber;
	}

	public int getEntityRefKey() {
		return entityRefKey;
	}

	public void setEntityRefKey(int entityRefKey) {
		this.entityRefKey = entityRefKey;
	}

	public int getClassRefkey() {
		return classRefkey;
	}

	public void setClassRefkey(int classRefkey) {
		this.classRefkey = classRefkey;
	}

	public int getInventroySiteIteKey() {
		return inventroySiteIteKey;
	}

	public void setInventroySiteIteKey(int inventroySiteIteKey) {
		this.inventroySiteIteKey = inventroySiteIteKey;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIsManualyClosed() {
		return isManualyClosed;
	}

	public void setIsManualyClosed(String isManualyClosed) {
		this.isManualyClosed = isManualyClosed;
	}

	public String getIsFullyReceived() {
		return isFullyReceived;
	}

	public void setIsFullyReceived(String isFullyReceived) {
		this.isFullyReceived = isFullyReceived;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getItemClassHIde() {
		return itemClassHIde;
	}

	public void setItemClassHIde(String itemClassHIde) {
		this.itemClassHIde = itemClassHIde;
	}

	public String getItemDecHide() {
		return itemDecHide;
	}

	public void setItemDecHide(String itemDecHide) {
		this.itemDecHide = itemDecHide;
	}

	public String getAdressBillTo() {
		return adressBillTo;
	}

	public void setAdressBillTo(String adressBillTo) {
		this.adressBillTo = adressBillTo;
	}

	public int getWebUserId() {
		return webUserId;
	}

	public void setWebUserId(int webUserId) {
		this.webUserId = webUserId;
	}

	public String getTransformMR() {
		return transformMR;
	}

	public void setTransformMR(String transformMR) {
		this.transformMR = transformMR;
	}

	public Date getReqDate() {
		return reqDate;
	}

	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}
	
	
	

}
