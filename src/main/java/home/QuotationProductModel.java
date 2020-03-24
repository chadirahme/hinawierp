package home;

public class QuotationProductModel 
{

	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNooflicense() {
		return nooflicense;
	}
	public void setNooflicense(String nooflicense) {
		this.nooflicense = nooflicense;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	private String description;
	private String nooflicense;
	private String memo;
	
}
