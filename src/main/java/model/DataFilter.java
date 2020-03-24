package model;

import java.util.Date;

public class DataFilter {
	private String name = "";
	private String arname = "";
	private String fullName = "";
	private String companyName = "";
	private String isactive = "";
	private String billCountry = "";
	private String phone = "";
	private String altphone = "";
	private String fax = "";
	private String email = "";
	private String contact = "";
	private String arName = "";
	private String irNo;
	private String flatName = "";
	private String buildingName = "";
	private String status = "";
	private String rent = "";
	private String deposit = "";

	private String description = "";
	private String type = "";
	private String accountName = "";

	private String accountNumber = "";
	private String billedQuantity= "";
	private Date createdDate;

	private String jobCardNO = "";
	private String txnDate = "";
	private String txnTime = "";
	private String recieveDate = "";
	private String workStartDate = "";
	private String workEndDate = "";
	private String odometer = "";
	private String routeNo = "";
	private String brand = "";
	private String jobType = "";
	private String note = "";

	private String propetyName = "";
	private String propetyType = "";

	private String plotNo = "";
	private String age = "";
	private String cost = "";
	private String noOfunits = "";
	private String unitcost = "";
	private String landLord = "";
	private String owner = "";
	private String watchman = "";
	private String watchmanPhone = "";
	private String street = "";
	private String city = "";
	private String country = "";
	private String conatactMaintanace = "";
	private String maintananceExpences = "";

	private String chassisNumber= "";
	private String vehicleType= "";
	private String regNumber= "";

	private String ownerName= "";
	private String mobile= "";
	private String assetCode= "";
	private String AssetName= "";
	private String color= "";
	private String power= "";
	private String odoMeter= "";
	private String year= "";
	private String engine= "";

	private String totalBalance;

	private String skypeID = "";
	private String bankName = "";
	private String branchName = "";
	private String ibanumber = "";
	private String printChequeAs = "";
	private String isActive = "";

	private String accountAssosiatedWith = "";
	private String commissionFlag = "";
	private double commissionPercent = 0.0;
	private String commissionUsed = "";
	private String commissionPaidBy = "";
	private String salesRepName = "";
	private String salesRepType = "";

	private String intials = "";

	private String sponsorName = "";
	private String sponsorNameArabic = "";
	private String bankCode = "";
	private String companyId = "";

	// customer summary report;
	private String enityName = "";

	private String enityNameAr = "";

	private String amount = "";

	private String period = "";

	private String formType = "";

	private String balance = "";

	private String credit = "";

	private String debit = "";

	private String acountName = "";

	private String refranceNumber = "";

	private String txnType = "";

	// cash invoice sales report

	private String serialNumber = "";

	private String invoiceNumber = "";

	private String invoiceDate = "";

	private String customerName = "";

	private String depositeTo = "";

	private String checkNo = "";

	private String invoiceAmount = "";

	private String totalSales = "";

	private String paymentType = "";

	private String salesRep = "";

	// For Receipt Voucher

	private String receiptName = "";

	private String paymenentMethod = "";

	private String bulidingName = "";

	private String unitNumber = "";

	private String rvNumber = "";

	private String mode = "";

	private String rvDate = "";

	private String arAccountName = "";

	private String checkNUmber = "";

	private String checkDate = "";

	private String depositeToAccountName = "";

	private String rvMemo = "";

	private String cucAccountNumbner = "";

	private String cucAccountName = "";

	private String fullname = "";

	private String className = "";

	private String classTyep = "";

	private String dueDate = "";

	private String paidAmount = "";

	private String unpaidPaidAmount = "";

	private String tremName = "";

	private String salesPrice = "";
	private String barcode = "";

	//
	private String actName = "";

	private String invoiceSaleNo = "";

	private String invoiceDateStr = "";

	private String sendVia = "";

	private String memo = "";

	private String itemName = "";

	private String rate = "";

	private String quantity = "";

	private String lineAmount = "";

	// item Receipt module

	private String irLocalNo = "";

	private String irDate = "";

	private String vendor = "";

	private String mainMemo = "";

	// purchase Request report

	private String vendorName = "";

	private String dropToName = "";

	private String txtnDate = "";

	private String refNUmber = "";

	private String decription = "";

	private String recivedQuantity = "";

	// bill

	private String billNo = "";

	private String refNumber = "";

	private String cRFlag = "";

	private String amountDue = "";

	private String accountNUmber = "";

	private String customer = "";

	// prospective List

	private String adress1 = "";
	private String other1 = "";
	private String other2 = "";
	private String arabic_name = "";
	private String created_date = "";
	private String has_quotation = "";
	private String category = "";
	private String website = "";
	private String telphone1 = "";
	private String telephone2 = "";

	private String contactPerson = "";

	private String postQbBy = "";
	private String userName = "";
	
	//general list 
	
	private String enDescription = "";
	
	private String priorityId="";
	
	private String arDescription="";
	
	private String notes="";

	private String rcvdQuantity= "";
	private String remainingQuantity= "";
	
	//delivery
	
	private String deliveryDate="";
	private String deliveryAmount="";
	private String statusDesc="";

	private double sellPrice= 0.0;
	private double costPrice= 0.0;
	
	
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber == null ? "" : accountNumber.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? "" : name.trim();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName == null ? "" : fullName.trim();
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName == null ? "" : companyName.trim();
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive == null ? "" : isactive.trim();
	}

	public String getBillCountry() {
		return billCountry;
	}

	public void setBillCountry(String billCountry) {
		this.billCountry = billCountry == null ? "" : billCountry.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? "" : phone.trim();
	}

	public String getAltphone() {
		return altphone;
	}

	public void setAltphone(String altphone) {
		this.altphone = altphone == null ? "" : altphone.trim();
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax == null ? "" : fax.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? "" : email.trim();
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact == null ? "" : contact.trim();
	}

	public String getFlatName() {
		return flatName;
	}

	public void setFlatName(String flatName) {
		this.flatName = flatName == null ? "" : flatName.trim();
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName == null ? "" : buildingName.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? "" : status.trim();
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent == null ? "" : rent.trim();
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit == null ? "" : deposit.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.description = description == null ? "" : description.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getArname() {
		return arname;
	}

	public void setArname(String arname) {
		this.arname = arname;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}

	public String getJobCardNO() {
		return jobCardNO;
	}

	public void setJobCardNO(String jobCardNO) {
		this.jobCardNO = jobCardNO;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getRecieveDate() {
		return recieveDate;
	}

	public void setRecieveDate(String recieveDate) {
		this.recieveDate = recieveDate;
	}

	public String getWorkStartDate() {
		return workStartDate;
	}

	public void setWorkStartDate(String workStartDate) {
		this.workStartDate = workStartDate;
	}

	public String getWorkEndDate() {
		return workEndDate;
	}

	public void setWorkEndDate(String workEndDate) {
		this.workEndDate = workEndDate;
	}

	public String getOdometer() {
		return odometer;
	}

	public void setOdometer(String odometer) {
		this.odometer = odometer;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the propetyName
	 */
	public String getPropetyName() {
		return propetyName;
	}

	/**
	 * @param propetyName
	 *            the propetyName to set
	 */
	public void setPropetyName(String propetyName) {
		this.propetyName = propetyName == null ? "" : propetyName.trim();
	}

	/**
	 * @return the propetyType
	 */
	public String getPropetyType() {
		return propetyType;
	}

	/**
	 * @param propetyType
	 *            the propetyType to set
	 */
	public void setPropetyType(String propetyType) {
		this.propetyType = propetyType == null ? "" : propetyType.trim();
	}

	/**
	 * @return the plotNo
	 */
	public String getPlotNo() {
		return plotNo;
	}

	/**
	 * @param plotNo
	 *            the plotNo to set
	 */
	public void setPlotNo(String plotNo) {
		this.plotNo = plotNo == null ? "" : plotNo.trim();
	}

	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(String age) {
		this.age = age == null ? "" : age.trim();
	}

	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost == null ? "" : cost.trim();
	}

	/**
	 * @return the noOfunits
	 */
	public String getNoOfunits() {
		return noOfunits;
	}

	/**
	 * @param noOfunits
	 *            the noOfunits to set
	 */
	public void setNoOfunits(String noOfunits) {
		this.noOfunits = noOfunits;
	}

	/**
	 * @return the unitcost
	 */
	public String getUnitcost() {
		return unitcost;
	}

	/**
	 * @param unitcost
	 *            the unitcost to set
	 */
	public void setUnitcost(String unitcost) {
		this.unitcost = unitcost;
	}

	/**
	 * @return the landLord
	 */
	public String getLandLord() {
		return landLord;
	}

	/**
	 * @param landLord
	 *            the landLord to set
	 */
	public void setLandLord(String landLord) {
		this.landLord = landLord == null ? "" : landLord.trim();
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner == null ? "" : owner.trim();
	}

	/**
	 * @return the watchman
	 */
	public String getWatchman() {
		return watchman;
	}

	/**
	 * @param watchman
	 *            the watchman to set
	 */
	public void setWatchman(String watchman) {
		this.watchman = watchman == null ? "" : watchman.trim();
	}

	/**
	 * @return the watchmanPhone
	 */
	public String getWatchmanPhone() {
		return watchmanPhone;
	}

	/**
	 * @param watchmanPhone
	 *            the watchmanPhone to set
	 */
	public void setWatchmanPhone(String watchmanPhone) {
		this.watchmanPhone = watchmanPhone;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the conatactMaintanace
	 */
	public String getConatactMaintanace() {
		return conatactMaintanace;
	}

	/**
	 * @param conatactMaintanace
	 *            the conatactMaintanace to set
	 */
	public void setConatactMaintanace(String conatactMaintanace) {
		this.conatactMaintanace = conatactMaintanace == null ? ""
				: conatactMaintanace.trim();
	}

	/**
	 * @return the maintananceExpences
	 */
	public String getMaintananceExpences() {
		return maintananceExpences;
	}

	/**
	 * @param maintananceExpences
	 *            the maintananceExpences to set
	 */
	public void setMaintananceExpences(String maintananceExpences) {
		this.maintananceExpences = maintananceExpences == null ? ""
				: maintananceExpences.trim();
	}

	/**
	 * @return the chassisNumber
	 */
	public String getChassisNumber() {
		return chassisNumber;
	}

	/**
	 * @param chassisNumber
	 *            the chassisNumber to set
	 */
	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber == null ? "" : chassisNumber.trim();
	}

	/**
	 * @return the vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType
	 *            the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType == null ? "" : vehicleType.trim();
	}

	/**
	 * @return the regNumber
	 */
	public String getRegNumber() {
		return regNumber;
	}

	/**
	 * @param regNumber
	 *            the regNumber to set
	 */
	public void setRegNumber(String regNumber) {
		this.regNumber = regNumber == null ? "" : regNumber.trim();
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName
	 *            the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName == null ? "" : ownerName.trim();
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the assetCode
	 */
	public String getAssetCode() {
		return assetCode;
	}

	/**
	 * @param assetCode
	 *            the assetCode to set
	 */
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode == null ? "" : assetCode.trim();
	}

	/**
	 * @return the assetName
	 */
	public String getAssetName() {
		return AssetName;
	}

	/**
	 * @param assetName
	 *            the assetName to set
	 */
	public void setAssetName(String assetName) {
		AssetName = assetName == null ? "" : assetName.trim();
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color == null ? "" : color.trim();
	}

	/**
	 * @return the power
	 */
	public String getPower() {
		return power;
	}

	/**
	 * @param power
	 *            the power to set
	 */
	public void setPower(String power) {
		this.power = power == null ? "" : power.trim();
	}

	/**
	 * @return the odoMeter
	 */
	public String getOdoMeter() {
		return odoMeter;
	}

	/**
	 * @param odoMeter
	 *            the odoMeter to set
	 */
	public void setOdoMeter(String odoMeter) {
		this.odoMeter = odoMeter;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year == null ? "" : year.trim();
	}

	/**
	 * @return the engine
	 */
	public String getEngine() {
		return engine;
	}

	/**
	 * @param engine
	 *            the engine to set
	 */
	public void setEngine(String engine) {
		this.engine = engine == null ? "" : engine.trim();
	}

	/**
	 * @return the totalBalance
	 */
	public String getTotalBalance() {
		return totalBalance;
	}

	/**
	 * @param totalBalance
	 *            the totalBalance to set
	 */
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}

	/**
	 * @return the skypeID
	 */
	public String getSkypeID() {
		return skypeID;
	}

	/**
	 * @param skypeID
	 *            the skypeID to set
	 */
	public void setSkypeID(String skypeID) {
		this.skypeID = skypeID;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName == null ? "" : bankName.trim();
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName
	 *            the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName == null ? "" : branchName.trim();
	}

	/**
	 * /**
	 * 
	 * @return the printChequeAs
	 */
	public String getPrintChequeAs() {
		return printChequeAs;
	}

	/**
	 * @return the ibanumber
	 */
	public String getIbanumber() {
		return ibanumber;
	}

	/**
	 * @param ibanumber
	 *            the ibanumber to set
	 */
	public void setIbanumber(String ibanumber) {
		this.ibanumber = ibanumber == null ? "" : ibanumber.trim();
	}

	/**
	 * @param printChequeAs
	 *            the printChequeAs to set
	 */
	public void setPrintChequeAs(String printChequeAs) {
		this.printChequeAs = printChequeAs == null ? "" : printChequeAs.trim();
	}

	/**
	 * @return the accountAssosiatedWith
	 */
	public String getAccountAssosiatedWith() {
		return accountAssosiatedWith;
	}

	/**
	 * @param accountAssosiatedWith
	 *            the accountAssosiatedWith to set
	 */
	public void setAccountAssosiatedWith(String accountAssosiatedWith) {
		this.accountAssosiatedWith = accountAssosiatedWith == null ? ""
				: accountAssosiatedWith.trim();

	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive == null ? "" : isActive.trim();
	}

	/**
	 * @return the commissionFlag
	 */
	public String getCommissionFlag() {
		return commissionFlag;
	}

	/**
	 * @param commissionFlag
	 *            the commissionFlag to set
	 */
	public void setCommissionFlag(String commissionFlag) {
		this.commissionFlag = commissionFlag == null ? "" : commissionFlag
				.trim();
	}

	/**
	 * @return the commissionPercent
	 */
	public double getCommissionPercent() {
		return commissionPercent;
	}

	/**
	 * @param commissionPercent
	 *            the commissionPercent to set
	 */
	public void setCommissionPercent(double commissionPercent) {
		this.commissionPercent = commissionPercent;

	}

	/**
	 * @return the commissionUsed
	 */
	public String getCommissionUsed() {
		return commissionUsed;
	}

	/**
	 * @param commissionUsed
	 *            the commissionUsed to set
	 */
	public void setCommissionUsed(String commissionUsed) {
		this.commissionUsed = commissionUsed == null ? "" : commissionUsed
				.trim();
	}

	/**
	 * @return the commissionPaidBy
	 */
	public String getCommissionPaidBy() {
		return commissionPaidBy;
	}

	/**
	 * @param commissionPaidBy
	 *            the commissionPaidBy to set
	 */
	public void setCommissionPaidBy(String commissionPaidBy) {
		this.commissionPaidBy = commissionPaidBy == null ? ""
				: commissionPaidBy.trim();
	}

	/**
	 * @return the salesRepName
	 */
	public String getSalesRepName() {
		return salesRepName;
	}

	/**
	 * @param salesRepName
	 *            the salesRepName to set
	 */
	public void setSalesRepName(String salesRepName) {
		this.salesRepName = salesRepName == null ? "" : salesRepName.trim();
	}

	/**
	 * @return the salesRepType
	 */
	public String getSalesRepType() {
		return salesRepType;
	}

	/**
	 * @param salesRepType
	 *            the salesRepType to set
	 */
	public void setSalesRepType(String salesRepType) {
		this.salesRepType = salesRepType == null ? "" : salesRepType.trim();
	}

	/**
	 * @return the intials
	 */
	public String getIntials() {
		return intials;
	}

	/**
	 * @param intials
	 *            the intials to set
	 */
	public void setIntials(String intials) {
		this.intials = intials == null ? "" : intials.trim();
	}

	/**
	 * @return the sponsorName
	 */
	public String getSponsorName() {
		return sponsorName;
	}

	/**
	 * @param sponsorName
	 *            the sponsorName to set
	 */
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName == null ? "" : sponsorName.trim();
	}

	/**
	 * @return the sponsorNameArabic
	 */
	public String getSponsorNameArabic() {
		return sponsorNameArabic;
	}

	/**
	 * @param sponsorNameArabic
	 *            the sponsorNameArabic to set
	 */
	public void setSponsorNameArabic(String sponsorNameArabic) {
		this.sponsorNameArabic = sponsorNameArabic == null ? ""
				: sponsorNameArabic.trim();
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode
	 *            the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode == null ? "" : bankCode.trim();
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId
	 *            the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId == null ? "" : companyId.trim();
	}

	public String getEnityName() {
		return enityName;
	}

	public void setEnityName(String enityName) {
		this.enityName = enityName == null ? "" : enityName.trim();
	}

	public String getEnityNameAr() {
		return enityNameAr;
	}

	public void setEnityNameAr(String enityNameAr) {
		this.enityNameAr = enityNameAr == null ? "" : enityNameAr.trim();
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount == null ? "" : amount.trim();
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period == null ? "" : period.trim();
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber == null ? "" : invoiceNumber.trim();
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate == null ? "" : invoiceDate.trim();
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName == null ? "" : customerName.trim();
	}

	public String getDepositeTo() {
		return depositeTo;
	}

	public void setDepositeTo(String depositeTo) {
		this.depositeTo = depositeTo == null ? "" : depositeTo.trim();
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo == null ? "" : checkNo.trim();
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount == null ? "" : invoiceAmount.trim();
	}

	public String getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales == null ? "" : totalSales.trim();
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType == null ? "" : paymentType.trim();
	}

	public String getSalesRep() {
		return salesRep;
	}

	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep == null ? "" : salesRep.trim();
	}

	public String getPaymenentMethod() {
		return paymenentMethod;
	}

	public void setPaymenentMethod(String paymenentMethod) {
		this.paymenentMethod = paymenentMethod == null ? "" : paymenentMethod
				.trim();
	}

	public String getBulidingName() {
		return bulidingName;
	}

	public void setBulidingName(String bulidingName) {
		this.bulidingName = bulidingName == null ? "" : bulidingName.trim();
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber == null ? "" : unitNumber.trim();
	}

	public String getRvNumber() {
		return rvNumber;
	}

	public void setRvNumber(String rvNumber) {
		this.rvNumber = rvNumber == null ? "" : rvNumber.trim();
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode == null ? "" : mode.trim();
	}

	public String getRvDate() {
		return rvDate;
	}

	public void setRvDate(String rvDate) {
		this.rvDate = rvDate == null ? "" : rvDate.trim();
	}

	public String getArAccountName() {
		return arAccountName;
	}

	public void setArAccountName(String arAccountName) {
		this.arAccountName = arAccountName == null ? "" : arAccountName.trim();
	}

	public String getCheckNUmber() {
		return checkNUmber;
	}

	public void setCheckNUmber(String checkNUmber) {
		this.checkNUmber = checkNUmber == null ? "" : checkNUmber.trim();
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate == null ? "" : checkDate.trim();
	}

	public String getDepositeToAccountName() {
		return depositeToAccountName;
	}

	public void setDepositeToAccountName(String depositeToAccountName) {
		this.depositeToAccountName = depositeToAccountName == null ? ""
				: depositeToAccountName.trim();
	}

	public String getRvMemo() {
		return rvMemo;
	}

	public void setRvMemo(String rvMemo) {
		this.rvMemo = salesRep == null ? "" : rvMemo.trim();
	}

	public String getCucAccountNumbner() {
		return cucAccountNumbner;
	}

	public void setCucAccountNumbner(String cucAccountNumbner) {
		this.cucAccountNumbner = cucAccountNumbner == null ? ""
				: cucAccountNumbner.trim();
	}

	public String getCucAccountName() {
		return cucAccountName;
	}

	public void setCucAccountName(String cucAccountName) {
		this.cucAccountName = cucAccountName == null ? "" : cucAccountName
				.trim();
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname == null ? "" : fullname.trim();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className == null ? "" : className.trim();
	}

	public String getClassTyep() {
		return classTyep;
	}

	public void setClassTyep(String classTyep) {
		this.classTyep = classTyep == null ? "" : classTyep.trim();
	}

	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName == null ? "" : receiptName.trim();
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate == null ? "" : dueDate.trim();
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount == null ? "" : paidAmount.trim();
	}

	public String getUnpaidPaidAmount() {
		return unpaidPaidAmount;
	}

	public String getArName() {
		return arName;
	}

	public void setArName(String arName) {
		this.arName = arName == null ? "" : arName.trim();
		;
	}

	public void setUnpaidPaidAmount(String unpaidPaidAmount) {
		this.unpaidPaidAmount = unpaidPaidAmount == null ? ""
				: unpaidPaidAmount.trim();
	}

	public String getTremName() {
		return tremName;
	}

	public void setTremName(String tremName) {
		this.tremName = tremName == null ? "" : tremName.trim();
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType == null ? "" : formType.trim();
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance == null ? "" : balance.trim();
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit == null ? "" : credit.trim();
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit == null ? "" : debit.trim();
	}

	public String getAcountName() {
		return acountName;

	}

	public void setAcountName(String acountName) {
		this.acountName = acountName == null ? "" : acountName.trim();
	}

	public String getRefranceNumber() {
		return refranceNumber;
	}

	public void setRefranceNumber(String refranceNumber) {
		this.refranceNumber = refranceNumber == null ? "" : refranceNumber
				.trim();
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType == null ? "" : txnType.trim();
	}

	public String getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice == null ? "" : salesPrice.trim();
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode == null ? "" : barcode.trim();
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName == null ? "" : actName.trim();
	}

	public String getInvoiceSaleNo() {
		return invoiceSaleNo;
	}

	public void setInvoiceSaleNo(String invoiceSaleNo) {
		this.invoiceSaleNo = invoiceSaleNo == null ? "" : invoiceSaleNo.trim();
	}

	public String getInvoiceDateStr() {
		return invoiceDateStr;
	}

	public void setInvoiceDateStr(String invoiceDateStr) {
		this.invoiceDateStr = invoiceDateStr == null ? "" : invoiceDateStr
				.trim();
	}

	public String getSendVia() {
		return sendVia;
	}

	public void setSendVia(String sendVia) {
		this.sendVia = sendVia == null ? "" : sendVia.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? "" : memo.trim();
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName == null ? "" : itemName.trim();
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate == null ? "" : rate.trim();
	}

	public String getQuantity() {
		return quantity;

	}

	public void setQuantity(String quantity) {
		this.quantity = quantity == null ? "" : quantity.trim();
	}

	public String getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(String lineAmount) {
		this.lineAmount = lineAmount == null ? "" : lineAmount.trim();
	}

	public String getIrLocalNo() {
		return irLocalNo;
	}

	public void setIrLocalNo(String irLocalNo) {
		this.irLocalNo = irLocalNo == null ? "" : irLocalNo.trim();
	}

	public String getIrDate() {
		return irDate;
	}

	public void setIrDate(String irDate) {
		this.irDate = irDate == null ? "" : irDate.trim();
	}

	public String getMainMemo() {
		return mainMemo;
	}

	public void setMainMemo(String mainMemo) {
		this.mainMemo = mainMemo == null ? "" : mainMemo.trim();
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor == null ? "" : vendor.trim();
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName == null ? "" : vendorName.trim();
	}

	public String getDropToName() {
		return dropToName;
	}

	public void setDropToName(String dropToName) {
		this.dropToName = dropToName == null ? "" : dropToName.trim();
	}

	public String getTxtnDate() {
		return txtnDate;
	}

	public void setTxtnDate(String txtnDate) {
		this.txtnDate = txtnDate == null ? "" : txtnDate.trim();
	}

	public String getRefNUmber() {
		return refNUmber;
	}

	public void setRefNUmber(String refNUmber) {
		this.refNUmber = refNUmber == null ? "" : refNUmber.trim();
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription == null ? "" : decription.trim();
	}

	public String getRecivedQuantity() {
		return recivedQuantity;
	}

	public void setRecivedQuantity(String recivedQuantity) {
		this.recivedQuantity = recivedQuantity == null ? "" : recivedQuantity
				.trim();
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? "" : billNo.trim();
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber == null ? "" : refNumber.trim();
	}

	public String getcRFlag() {
		return cRFlag;
	}

	public void setcRFlag(String cRFlag) {
		this.cRFlag = cRFlag == null ? "" : cRFlag.trim();
	}

	public String getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(String amountDue) {
		this.amountDue = amountDue == null ? "" : amountDue.trim();
	}

	public String getAccountNUmber() {
		return accountNUmber;
	}

	public void setAccountNUmber(String accountNUmber) {

		this.accountNUmber = accountNUmber == null ? "" : accountNUmber.trim();
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer == null ? "" : customer.trim();
	}

	public String getAdress1() {
		return adress1;
	}

	public void setAdress1(String adress1) {
		this.adress1 = adress1 == null ? "" : adress1.trim();
	}

	public String getOther1() {
		return other1;
	}

	public void setOther1(String other1) {
		this.other1 = other1 == null ? "" : other1.trim();
	}

	public String getOther2() {
		return other2;
	}

	public void setOther2(String other2) {
		this.other2 = other2;
		this.other1 = other1 == null ? "" : other1.trim();
	}

	public String getArabic_name() {
		return arabic_name;
	}

	public void setArabic_name(String arabic_name) {
		this.arabic_name = arabic_name == null ? "" : arabic_name.trim();
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date == null ? "" : created_date.trim();
	}

	public String getHas_quotation() {
		return has_quotation;
	}

	public void setHas_quotation(String has_quotation) {
		this.has_quotation = has_quotation == null ? "" : has_quotation.trim();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category == null ? "" : category.trim();
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website == null ? "" : website.trim();
	}

	public String getTelphone1() {
		return telphone1;
	}

	public void setTelphone1(String telphone1) {
		this.telphone1 = telphone1 == null ? "" : telphone1.trim();
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2 == null ? "" : telephone2.trim();
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson == null ? "" : contactPerson.trim();
	}

	/**
	 * @return the postQbBy
	 */
	public String getPostQbBy() {
		return postQbBy;
	}

	/**
	 * @param postQbBy
	 *            the postQbBy to set
	 */
	public void setPostQbBy(String postQbBy) {
		this.postQbBy = postQbBy == null ? "" : postQbBy.trim();
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName == null ? "" : userName.trim();
	}

	/**
	 * @return the enDescription
	 */
	public String getEnDescription() {
		return enDescription;
	}

	/**
	 * @param enDescription the enDescription to set
	 */
	public void setEnDescription(String enDescription) {
		this.enDescription = enDescription == null ? "" : enDescription.trim();
	}

	/**
	 * @return the priorityId
	 */
	public String getPriorityId() {
		return priorityId;
	}

	/**
	 * @param priorityId the priorityId to set
	 */
	public void setPriorityId(String priorityId) {
		this.priorityId = priorityId == null ? "" : priorityId.trim();
	}

	/**
	 * @return the arDescription
	 */
	public String getArDescription() {
		return arDescription;
	}

	/**
	 * @param arDescription the arDescription to set
	 */
	public void setArDescription(String arDescription) {
		this.arDescription = arDescription == null ? "" : arDescription.trim();
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes == null ? "" : notes.trim();
	}

	public String getRcvdQuantity() {
		return rcvdQuantity;
	}

	public void setRcvdQuantity(String rcvdQuantity) {
		this.rcvdQuantity = rcvdQuantity;
	}

	public String getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(String remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

	public String getIrNo() {
		return irNo;
	}

	public void setIrNo(String irNo) {
		this.irNo = irNo;
	}

	public String getBilledQuantity() {
		return billedQuantity;
	}

	public void setBilledQuantity(String billedQuantity) {
		this.billedQuantity = billedQuantity;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryAmount() {
		return deliveryAmount;
	}

	public void setDeliveryAmount(String deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	
	

}
