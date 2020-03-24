package model;

public class FeedbackSendSources {

	int feedbackKey;

	private String sourceType;

	private int sourceId;

	private String empNAme;

	private String vendName;

	private String custName;

	private String prosName;
	
	
	
	//override hash set to dont allow duplicates when using objects 
	  @Override
	   public boolean equals(Object obj){  
	 
	        if(!(obj instanceof FeedbackSendSources))  
	            return false;  
	 
	        return (this.getSourceId() == ((FeedbackSendSources) obj).getSourceId());   
	    }  
	 
	 
	    @Override
	    public int hashCode(){  
	 
	        return  this.getSourceId();    
	    }

	/**
	 * @return the feedbackKey
	 */
	public int getFeedbackKey() {
		return feedbackKey;
	}

	/**
	 * @param feedbackKey the feedbackKey to set
	 */
	public void setFeedbackKey(int feedbackKey) {
		this.feedbackKey = feedbackKey;
	}

	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the sourceId
	 */
	public int getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @return the empNAme
	 */
	public String getEmpNAme() {
		return empNAme;
	}

	/**
	 * @param empNAme the empNAme to set
	 */
	public void setEmpNAme(String empNAme) {
		this.empNAme = empNAme;
	}

	/**
	 * @return the vendName
	 */
	public String getVendName() {
		return vendName;
	}

	/**
	 * @param vendName the vendName to set
	 */
	public void setVendName(String vendName) {
		this.vendName = vendName;
	}

	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * @return the prosName
	 */
	public String getProsName() {
		return prosName;
	}

	/**
	 * @param prosName the prosName to set
	 */
	public void setProsName(String prosName) {
		this.prosName = prosName;
	}





}
