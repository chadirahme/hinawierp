package model;

public class LocalItemModel {
	private Integer RecNo;
	private String name;
	private Integer itemTypeRef;
	
	public Integer getRecNo() {
		return RecNo;
	}
	public void setRecNo(Integer recNo) {
		RecNo = recNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getItemTypeRef() {
		return itemTypeRef;
	}
	public void setItemTypeRef(Integer itemTypeRef) {
		this.itemTypeRef = itemTypeRef;
	}
	

}
