package reports;

public class AdditionModel implements Comparable<AdditionModel>{
	
	private int add_Id;
	
	private String description;
	
	private double adAmount;
	
	private String ahValueFlag;
	
	private double ahValue;
	
	private int empKey;

	/**
	 * @return the add_Id
	 */
	public int getAdd_Id() {
		return add_Id;
	}

	/**
	 * @param add_Id the add_Id to set
	 */
	public void setAdd_Id(int add_Id) {
		this.add_Id = add_Id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	public int compareTo(AdditionModel arg0) {
		// TODO Auto-generated method stub
		return this.compareTo(arg0);
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
