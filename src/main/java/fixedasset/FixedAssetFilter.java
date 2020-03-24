package fixedasset;

public class FixedAssetFilter 
{

	private String assetName="";
	private String masterDesc="";
	private String unit="";
	private String used="";
	private String category="";
	
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName==null?"":assetName.trim();
	}
	public String getMasterDesc() {
		return masterDesc;
	}
	public void setMasterDesc(String masterDesc) {
		this.masterDesc = masterDesc==null?"":masterDesc.trim();
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit==null?"":unit.trim();
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used==null?"":used.trim();
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category==null?"":category.trim();
	}
	
}
