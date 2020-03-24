package reports;

public class DiductionModel implements Comparable<DiductionModel> {
	
	private int add_id;
	
	private String Description;
	
	private double adAmount;
	
	private String ahValueFlag;
	
	private double ahValue;
	private int empKey;

	/**
	 * @return the add_id
	 */
	public int getAdd_id() {
		return add_id;
	}

	/**
	 * @param add_id the add_id to set
	 */
	public void setAdd_id(int add_id) {
		this.add_id = add_id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * @return the adAmount
	 */
	public double getAdAmount() {
		return adAmount;
	}

	/**
	 * @param adAmount the adAmount to set
	 */
	public void setAdAmount(double adAmount) {
		this.adAmount = adAmount;
	}

	/**
	 * @return the ahValueFlag
	 */
	public String getAhValueFlag() {
		return ahValueFlag;
	}

	/**
	 * @param ahValueFlag the ahValueFlag to set
	 */
	public void setAhValueFlag(String ahValueFlag) {
		this.ahValueFlag = ahValueFlag;
	}

	/**
	 * @return the ahValue
	 */
	public double getAhValue() {
		return ahValue;
	}

	/**
	 * @param ahValue the ahValue to set
	 */
	public void setAhValue(double ahValue) {
		this.ahValue = ahValue;
	}

	@Override
	public int compareTo(DiductionModel o) {
		// TODO Auto-generated method stub
		return this.compareTo(o);
	}

	/**
	 * @return the empKey
	 */
	public int getEmpKey() {
		return empKey;
	}

	/**
	 * @param empKey the empKey to set
	 */
	public void setEmpKey(int empKey) {
		this.empKey = empKey;
	}
	
	

}
