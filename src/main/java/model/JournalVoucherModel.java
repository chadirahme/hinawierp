package model;

import java.util.Date;

public class JournalVoucherModel {
	private int recno;
	private int editSequence;
	private double totalAmount;
	private int preTerRecNo;
	private int releaseRecNo;
	private int contractRecNo;
	private int revisedRecNo;
	private int userId;
	private String txnID="";
	private String timeCreated="";
	private String timeModified="";
	private String txnNumber="";
	private String refNumber="";
	private String isAdjustment="";
	private String qBStatus="";
	private String status="";
	private String notes="";
	private String newStatus="";
	private String rVDifferedIncomeFlag="";
	private String pVAdvancePaymentFlag="";
	private Date txnDate;
	private Date dATECREATED;
	private Date dATEMODIFIED;
	public int getRecno() {
		return recno;
	}
	public void setRecno(int recno) {
		this.recno = recno;
	}
	public int getEditSequence() {
		return editSequence;
	}
	public void setEditSequence(int editSequence) {
		this.editSequence = editSequence;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getPreTerRecNo() {
		return preTerRecNo;
	}
	public void setPreTerRecNo(int preTerRecNo) {
		this.preTerRecNo = preTerRecNo;
	}
	public int getReleaseRecNo() {
		return releaseRecNo;
	}
	public void setReleaseRecNo(int releaseRecNo) {
		this.releaseRecNo = releaseRecNo;
	}
	public int getContractRecNo() {
		return contractRecNo;
	}
	public void setContractRecNo(int contractRecNo) {
		this.contractRecNo = contractRecNo;
	}
	public int getRevisedRecNo() {
		return revisedRecNo;
	}
	public void setRevisedRecNo(int revisedRecNo) {
		this.revisedRecNo = revisedRecNo;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getTxnID() {
		return txnID;
	}
	public void setTxnID(String txnID) {
		this.txnID = txnID;
	}
	public String getTimeCreated() {
		return timeCreated;
	}
	public void setTimeCreated(String timeCreated) {
		this.timeCreated = timeCreated;
	}
	public String getTimeModified() {
		return timeModified;
	}
	public void setTimeModified(String timeModified) {
		this.timeModified = timeModified;
	}
	public String getTxnNumber() {
		return txnNumber;
	}
	public void setTxnNumber(String txnNumber) {
		this.txnNumber = txnNumber;
	}
	public String getRefNumber() {
		return refNumber;
	}
	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}
	public String getIsAdjustment() {
		return isAdjustment;
	}
	public void setIsAdjustment(String isAdjustment) {
		this.isAdjustment = isAdjustment;
	}
	public String getqBStatus() {
		return qBStatus;
	}
	public void setqBStatus(String qBStatus) {
		this.qBStatus = qBStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getNewStatus() {
		return newStatus;
	}
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	public String getrVDifferedIncomeFlag() {
		return rVDifferedIncomeFlag;
	}
	public void setrVDifferedIncomeFlag(String rVDifferedIncomeFlag) {
		this.rVDifferedIncomeFlag = rVDifferedIncomeFlag;
	}
	public String getpVAdvancePaymentFlag() {
		return pVAdvancePaymentFlag;
	}
	public void setpVAdvancePaymentFlag(String pVAdvancePaymentFlag) {
		this.pVAdvancePaymentFlag = pVAdvancePaymentFlag;
	}
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}
	public Date getDATECREATED() {
		return dATECREATED;
	}
	public void setDATECREATED(Date dATECREATED) {
		this.dATECREATED = dATECREATED;
	}
	public Date getDATEMODIFIED() {
		return dATEMODIFIED;
	}
	public void setDATEMODIFIED(Date dATEMODIFIED) {
		this.dATEMODIFIED = dATEMODIFIED;
	}
	
}
