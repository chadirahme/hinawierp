package model;

public class EstimateModel {

	private int estimateNo;
	private String estimateName="";
	private String customerName;
	private String industryTypeEN;
	private String itemName;
	private double quantity;
	private double requestQuantity;
	
	public int getEstimateNo() {
		return estimateNo;
	}
	public void setEstimateNo(int estimateNo) {
		this.estimateNo = estimateNo;
	}
	public String getEstimateName() {
		return estimateName;
	}
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getIndustryTypeEN() {
		return industryTypeEN;
	}
	public void setIndustryTypeEN(String industryTypeEN) {
		this.industryTypeEN = industryTypeEN;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getRequestQuantity() {
		return requestQuantity;
	}
	public void setRequestQuantity(double requestQuantity) {
		this.requestQuantity = requestQuantity;
	}
	
	
	
}
