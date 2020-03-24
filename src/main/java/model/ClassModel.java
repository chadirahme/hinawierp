package model;

public class ClassModel 
{

	private int class_Key;
	private String name;
	private int sublevel;
	private String listID;
	private String fullName;
	private String isActive;
	private String parent;
	private int subofKey;
	
	
	private ClassModel slectedSubOfClass;
	
	
	
	public int getClass_Key() {
		return class_Key;
	}
	public void setClass_Key(int class_Key) {
		this.class_Key = class_Key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSublevel() {
		return sublevel;
	}
	public void setSublevel(int sublevel) {
		this.sublevel = sublevel;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the slectedSubOfClass
	 */
	public ClassModel getSlectedSubOfClass() {
		return slectedSubOfClass;
	}
	/**
	 * @param slectedSubOfClass the slectedSubOfClass to set
	 */
	public void setSlectedSubOfClass(ClassModel slectedSubOfClass) {
		this.slectedSubOfClass = slectedSubOfClass;
	}
	/**
	 * @return the listID
	 */
	public String getListID() {
		return listID;
	}
	/**
	 * @param listID the listID to set
	 */
	public void setListID(String listID) {
		this.listID = listID;
	}
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the subofKey
	 */
	public int getSubofKey() {
		return subofKey;
	}
	/**
	 * @param subofKey the subofKey to set
	 */
	public void setSubofKey(int subofKey) {
		this.subofKey = subofKey;
	}
	
	
	
	
}
