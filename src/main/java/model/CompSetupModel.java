package model;

import org.zkoss.bind.annotation.DependsOn;

public class CompSetupModel 
{

	private int companyKey;
	private String pvSerialNos;
	private String postOnMainAccount;
	private String postOnMainClass;
	private String buyItemWithHighCost;
	
	private String useMinPurchasePrice;
	private String useMaxPurchasePrice;
	private double minPurchasePriceRatio;
	private double maxPurchasePriceRatio;
	
	//Used for CashInvoice
	
	private String postItem2Main;
	private String sellStockWithZero;
	private String allowSavingAvgCost;
	private String useSellItemWithLowerSP;
	private String useMinSellingPrice;
	private double  minSellingPriceRatio;
	private String useMaxSellingPrice;
	private double  maxSellingPriceRatio;
	private String showAvgCost;
		
	//used in Leave Request
	private String allowMinusLeave;
	private double maxLeaveDays;
	private String calculateActualLeaveDays;
	private String addLeaveSalary2TS;
	private int leaveBasis;
	private String useEncash;
	private String leaveBeforeReturn;
	
	//used in TimeSheet
	private int payrollYear;
	private int payrollMonth;
	private String includeholidayUnit;
	
	private String useDefaultMaxCommission;
	private String useDefaultSalesRepCommission;
	
	//used in timesheet Shift
	private int weekStart;
	private int defHours;
	private String changeHoliday;
	
	private boolean canChangeHoliday;
	private boolean canIncludeholidayUnit;
	
	private String timesheetOTChange;
	private String timesheetTimeAuto;
	
	private boolean timesheetChangeTime;
	private boolean timesheetCalculateExtraHours;
	
	
	private String changePrice_ConvertPO;
	private String changePredefinedClass;
	
	private String rVSerialSetup;
	
	private String address;
	private String fax;
	private String companyName;
	private String postJVWithOutName;
	
	
	//credit Invoice
	
	private String useSalesRepComition;
	
	private String saveInvQty;
	
	private String canExceedOverLimit;
	
	
	//email setup HR;
	private String activateEmail;
	private String email1;
	private String email2;
	private String email3;
	private String allowToAddInventorySite;
	
	private int countrykey;
	private int citykey;
	private String phone1;
	private String ccemail;
	
	private String usebillable;
	private String usePurchaseFlow;
	private String useSalesFlow;
	private String allowToSkip;
	private String AllowToSkipPurchaseWorkFlow;
	
	
	public String getPvSerialNos() {
		return pvSerialNos;
	}

	public void setPvSerialNos(String pvSerialNos) {
		this.pvSerialNos = pvSerialNos;
	}

	public String getPostOnMainAccount() {
		return postOnMainAccount;
	}

	public void setPostOnMainAccount(String postOnMainAccount) {
		this.postOnMainAccount = postOnMainAccount;
	}

	public String getPostOnMainClass() {
		return postOnMainClass;
	}

	public void setPostOnMainClass(String postOnMainClass) {
		this.postOnMainClass = postOnMainClass;
	}

	public String getBuyItemWithHighCost() {
		return buyItemWithHighCost;
	}

	public void setBuyItemWithHighCost(String buyItemWithHighCost) {
		this.buyItemWithHighCost = buyItemWithHighCost;
	}

	public String getUseMinPurchasePrice() {
		return useMinPurchasePrice;
	}

	public void setUseMinPurchasePrice(String useMinPurchasePrice) {
		this.useMinPurchasePrice = useMinPurchasePrice;
	}

	public String getUseMaxPurchasePrice() {
		return useMaxPurchasePrice;
	}

	public void setUseMaxPurchasePrice(String useMaxPurchasePrice) {
		this.useMaxPurchasePrice = useMaxPurchasePrice;
	}

	public double getMinPurchasePriceRatio() {
		return minPurchasePriceRatio;
	}

	public void setMinPurchasePriceRatio(double minPurchasePriceRatio) {
		this.minPurchasePriceRatio = minPurchasePriceRatio;
	}

	public double getMaxPurchasePriceRatio() {
		return maxPurchasePriceRatio;
	}

	public void setMaxPurchasePriceRatio(double maxPurchasePriceRatio) {
		this.maxPurchasePriceRatio = maxPurchasePriceRatio;
	}

	public String getAllowMinusLeave() {
		return allowMinusLeave;
	}

	public void setAllowMinusLeave(String allowMinusLeave) {
		this.allowMinusLeave = allowMinusLeave;
	}

	public double getMaxLeaveDays() {
		return maxLeaveDays;
	}

	public void setMaxLeaveDays(double maxLeaveDays) {
		this.maxLeaveDays = maxLeaveDays;
	}

	public String getCalculateActualLeaveDays() {
		return calculateActualLeaveDays;
	}

	public void setCalculateActualLeaveDays(String calculateActualLeaveDays) {
		this.calculateActualLeaveDays = calculateActualLeaveDays;
	}

	public String getAddLeaveSalary2TS() {
		return addLeaveSalary2TS;
	}

	public void setAddLeaveSalary2TS(String addLeaveSalary2TS) {
		this.addLeaveSalary2TS = addLeaveSalary2TS;
	}

	public int getLeaveBasis() {
		return leaveBasis;
	}

	public void setLeaveBasis(int leaveBasis) {
		this.leaveBasis = leaveBasis;
	}

	public String getUseEncash() {
		return useEncash;
	}

	public void setUseEncash(String useEncash) {
		this.useEncash = useEncash;
	}

	public String getLeaveBeforeReturn() {
		return leaveBeforeReturn;
	}

	public void setLeaveBeforeReturn(String leaveBeforeReturn) {
		this.leaveBeforeReturn = leaveBeforeReturn;
	}

	public int getPayrollYear() {
		return payrollYear;
	}

	public void setPayrollYear(int payrollYear) {
		this.payrollYear = payrollYear;
	}

	public int getPayrollMonth() {
		return payrollMonth;
	}

	public void setPayrollMonth(int payrollMonth) {
		this.payrollMonth = payrollMonth;
	}

	/**
	 * @return the postItem2Main
	 */
	public String getPostItem2Main() {
		return postItem2Main;
	}

	/**
	 * @param postItem2Main the postItem2Main to set
	 */
	public void setPostItem2Main(String postItem2Main) {
		this.postItem2Main = postItem2Main;
	}

	/**
	 * @return the sellStockWithZero
	 */
	public String getSellStockWithZero() {
		return sellStockWithZero;
	}

	/**
	 * @param sellStockWithZero the sellStockWithZero to set
	 */
	public void setSellStockWithZero(String sellStockWithZero) {
		this.sellStockWithZero = sellStockWithZero;
	}

	/**
	 * @return the allowSavingAvgCost
	 */
	public String getAllowSavingAvgCost() {
		return allowSavingAvgCost;
	}

	/**
	 * @param allowSavingAvgCost the allowSavingAvgCost to set
	 */
	public void setAllowSavingAvgCost(String allowSavingAvgCost) {
		this.allowSavingAvgCost = allowSavingAvgCost;
	}

	/**
	 * @return the useSellItemWithLowerSP
	 */
	public String getUseSellItemWithLowerSP() {
		return useSellItemWithLowerSP;
	}

	/**
	 * @param useSellItemWithLowerSP the useSellItemWithLowerSP to set
	 */
	public void setUseSellItemWithLowerSP(String useSellItemWithLowerSP) {
		this.useSellItemWithLowerSP = useSellItemWithLowerSP;
	}

	/**
	 * @return the useMinSellingPrice
	 */
	public String getUseMinSellingPrice() {
		return useMinSellingPrice;
	}

	/**
	 * @param useMinSellingPrice the useMinSellingPrice to set
	 */
	public void setUseMinSellingPrice(String useMinSellingPrice) {
		this.useMinSellingPrice = useMinSellingPrice;
	}

	/**
	 * @return the minSellingPriceRatio
	 */
	public double getMinSellingPriceRatio() {
		return minSellingPriceRatio;
	}

	/**
	 * @param minSellingPriceRatio the minSellingPriceRatio to set
	 */
	public void setMinSellingPriceRatio(double minSellingPriceRatio) {
		this.minSellingPriceRatio = minSellingPriceRatio;
	}

	/**
	 * @return the useMaxSellingPrice
	 */
	public String getUseMaxSellingPrice() {
		return useMaxSellingPrice;
	}

	/**
	 * @param useMaxSellingPrice the useMaxSellingPrice to set
	 */
	public void setUseMaxSellingPrice(String useMaxSellingPrice) {
		this.useMaxSellingPrice = useMaxSellingPrice;
	}

	/**
	 * @return the maxSellingPriceRatio
	 */
	public double getMaxSellingPriceRatio() {
		return maxSellingPriceRatio;
	}

	/**
	 * @param maxSellingPriceRatio the maxSellingPriceRatio to set
	 */
	public void setMaxSellingPriceRatio(double maxSellingPriceRatio) {
		this.maxSellingPriceRatio = maxSellingPriceRatio;
	}

	/**
	 * @return the showAvgCost
	 */
	public String getShowAvgCost() {
		return showAvgCost;
	}

	/**
	 * @param showAvgCost the showAvgCost to set
	 */
	public void setShowAvgCost(String showAvgCost) {
		this.showAvgCost = showAvgCost;
	}

	public String getUseDefaultMaxCommission() {
		return useDefaultMaxCommission;
	}

	public void setUseDefaultMaxCommission(String useDefaultMaxCommission) {
		this.useDefaultMaxCommission = useDefaultMaxCommission;
	}

	public String getUseDefaultSalesRepCommission() {
		return useDefaultSalesRepCommission;
	}

	public void setUseDefaultSalesRepCommission(
			String useDefaultSalesRepCommission) {
		this.useDefaultSalesRepCommission = useDefaultSalesRepCommission;
	}

	public String getIncludeholidayUnit() {
		return includeholidayUnit;
	}

	public void setIncludeholidayUnit(String includeholidayUnit) {
		this.includeholidayUnit = includeholidayUnit;
	}

	public int getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(int weekStart) {
		this.weekStart = weekStart;
	}

	public int getDefHours() {
		return defHours;
	}

	public void setDefHours(int defHours) {
		this.defHours = defHours;
	}

	public String getChangeHoliday() {
		return changeHoliday;
	}

	public void setChangeHoliday(String changeHoliday) {
		this.changeHoliday = changeHoliday;
	}

	public boolean isCanChangeHoliday() {
		return canChangeHoliday;
	}

	public void setCanChangeHoliday(boolean canChangeHoliday) {
		this.canChangeHoliday = canChangeHoliday;
	}

	public boolean isCanIncludeholidayUnit() {
		return canIncludeholidayUnit;
	}

	public void setCanIncludeholidayUnit(boolean canIncludeholidayUnit) {
		this.canIncludeholidayUnit = canIncludeholidayUnit;
	}

	public int getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(int companyKey) {
		this.companyKey = companyKey;
	}

	public String getTimesheetOTChange() {
		return timesheetOTChange;
	}

	public void setTimesheetOTChange(String timesheetOTChange) {
		this.timesheetOTChange = timesheetOTChange;
	}

	public String getTimesheetTimeAuto() {
		return timesheetTimeAuto;
	}

	public void setTimesheetTimeAuto(String timesheetTimeAuto) {
		this.timesheetTimeAuto = timesheetTimeAuto;
	}

	public String getChangePredefinedClass() {
		return changePredefinedClass;
	}

	public void setChangePredefinedClass(String changePredefinedClass) {
		this.changePredefinedClass = changePredefinedClass;
	}

	public String getrVSerialSetup() {
		return rVSerialSetup;
	}

	public void setrVSerialSetup(String rVSerialSetup) {
		this.rVSerialSetup = rVSerialSetup;
	}

	public String getUseSalesRepComition() {
		return useSalesRepComition;
	}

	public void setUseSalesRepComition(String useSalesRepComition) {
		this.useSalesRepComition = useSalesRepComition;
	}

	public String getSaveInvQty() {
		return saveInvQty;
	}

	public void setSaveInvQty(String saveInvQty) {
		this.saveInvQty = saveInvQty;
	}

	public String getCanExceedOverLimit() {
		return canExceedOverLimit;
	}

	public void setCanExceedOverLimit(String canExceedOverLimit) {
		this.canExceedOverLimit = canExceedOverLimit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean getTimesheetChangeTime() {
		return timesheetChangeTime;
	}
	
	public void setTimesheetChangeTime(boolean timesheetChangeTime) {
		this.timesheetChangeTime = timesheetChangeTime;
		
	}
	@DependsOn({"timesheetChangeTime"})
	public boolean getTimesheetCalculateExtraHours() {
		if(timesheetChangeTime==false)
			timesheetCalculateExtraHours=false;
		return timesheetCalculateExtraHours;
	}

	public void setTimesheetCalculateExtraHours(
			boolean timesheetCalculateExtraHours) {
		this.timesheetCalculateExtraHours = timesheetCalculateExtraHours;
	}

	

	public String getActivateEmail() {
		return activateEmail;
	}

	public void setActivateEmail(String activateEmail) {
		this.activateEmail = activateEmail;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the allowToAddInventorySite
	 */
	public String getAllowToAddInventorySite() {
		return allowToAddInventorySite;
	}

	/**
	 * @param allowToAddInventorySite the allowToAddInventorySite to set
	 */
	public void setAllowToAddInventorySite(String allowToAddInventorySite) {
		this.allowToAddInventorySite = allowToAddInventorySite;
	}

	public int getCountrykey() {
		return countrykey;
	}

	public void setCountrykey(int countrykey) {
		this.countrykey = countrykey;
	}

	public int getCitykey() {
		return citykey;
	}

	public void setCitykey(int citykey) {
		this.citykey = citykey;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getCcemail() {
		return ccemail;
	}

	public void setCcemail(String ccemail) {
		this.ccemail = ccemail;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the usebillable
	 */
	public String getUsebillable() {
		return usebillable;
	}

	/**
	 * @param usebillable the usebillable to set
	 */
	public void setUsebillable(String usebillable) {
		this.usebillable = usebillable;
	}

	public String getPostJVWithOutName() {
		return postJVWithOutName;
	}

	public void setPostJVWithOutName(String postJVWithOutName) {
		this.postJVWithOutName = postJVWithOutName;
	}

	public String getUsePurchaseFlow() {
		return usePurchaseFlow;
	}

	public void setUsePurchaseFlow(String usePurchaseFlow) {
		this.usePurchaseFlow = usePurchaseFlow;
	}

	public String getChangePrice_ConvertPO() {
		return changePrice_ConvertPO;
	}

	public void setChangePrice_ConvertPO(String changePrice_ConvertPO) {
		this.changePrice_ConvertPO = changePrice_ConvertPO;
	}

	public String getUseSalesFlow() {
		return useSalesFlow;
	}

	public void setUseSalesFlow(String useSalesFlow) {
		this.useSalesFlow = useSalesFlow;
	}

	public String getAllowToSkip() {
		return allowToSkip;
	}

	public void setAllowToSkip(String allowToSkip) {
		this.allowToSkip = allowToSkip;
	}

	public String getAllowToSkipPurchaseWorkFlow() {
		return AllowToSkipPurchaseWorkFlow;
	}

	public void setAllowToSkipPurchaseWorkFlow(
			String allowToSkipPurchaseWorkFlow) {
		AllowToSkipPurchaseWorkFlow = allowToSkipPurchaseWorkFlow;
	}
	
	
	
	
}
