package common;

public class HbaEnum {
	public static enum HbaList {
		CashPayment("0"),
		ChequePayment("1"),
		CashReceipt("2"),
		ChequeReceipt("3"),
		CashChequeReceipt("4"),
		CashSale("5"),
		InventoryAdjIN("6"),
		InventoryAdjOUT("7"),
		PaymentVoucher("8"),
		SalesReceipt("9"),
		PostPDCCheques("10"),
		PostCUCCheques("11"),
		GeneralJournal("12"),
		BillPayments("13"),
		ItemReceipt("14"),
		Invoice("15"),
		StockTrans("16"),
		Delivery("17"),
		Inquiry("18"),
		Demo("19"),
		Quotation("20"),
		Visit("21"),
		CUCOpening("22"),
		PDCOpening("24"),
		BankTransfer("25"),
		BankTransferBills("26"),
		BillCreate("27"),
		BillCreditCreate("28"),
		FADepreciation("29"),
		FADepreciationCategory("30"),
		FADisposal("31"),
		FAJobCardInvoice("32"),
		FAAssetOpeningBalance("33"),
		OpeningPDC("34"),
		OpeningCUC("35"),
		FAAssetOpeningBalCategory("36"),
		ToDo("37"),
		DailyReport("38"),
		DocumentCenter("39"),
		OutgoingLetter("40"),
		InComingLetter("41"),
		Dashboard("42"),
		NameCenter("43"),
		CreditMemo("44"),
		GarageInvoice("45"),
		RealEstCommission("46"),
		RealEstContract("47"),
		PostDifferedIncome("48"),
		CashPayBill("49"),
		ChequePayBill("50"),
		BankTransferPayBill("51"),
		FAAddAsset("52"),
		Complaints("53"),
		JobCard("54"),
		JobCardCreditInvoice("55"),
		JobCardCashInvoice("56"),
		FAMaintenance("57"),
		FATransferAssets("58"),
		REProperty("59"),
		REFlat("60"),
		ConvertFA2Vehicle("61"),
		ConvertFA2Building("62"),
		REContract("63"),
		RENotice("64"),
		REGeneralMemo("65"),
		GarageVehicle("66"),
		GarageJobCard("67"),
		PurchaseOrder("68"),
		CustomerInfo("69"),
		VendorInfo("70"),
		ProspectiveInfo("71"),
		EmployeeInfo("72"),
		OtherNameInfo("73"),
		Templates("74"),
		Payroll("75"),
		REPreTerminate("76"),
		REreleaseContract("77"),
		RefundWriteCash("78"),
		RefundWriteCheque("79"),
		REPostDifferedIncome("80"),
		REMaintenance("81"),
		ChartOfAccounts("82"),
		ItemsInfo("83"),
		InventoryAdjustment("84"),
		InventorySiteTransfer("85"),
		REContractQuotation("86"),
		PurchaseRequest("87"),
		CreateNewUser("88"),
		HRPostLeaveSalary("89"),
		HRPostEOSBenefit("90"),
		HRPostLoanRefund("91"),
		HRPostLoan("92"),
		HRPostEOSProvision("93"),
		HRPostLeaveProvision("94"),
		HRPostSalarySheet("95"),
		HRPostTSSalarySheet("96"),
		HRPostSalaryPayment("97"),
		RealEstReleaseContract("98"),
		HRCreateEmployee("99"),
		HRLeave("100"),
		HRLoan("101"),
		HREOS("102"),
		RERenewContract("103"),
		HRSalarySheet("104"),
		CustomerList("105"),
		ProspectiveList("106"),
		VendorList("107"),
		EmployeeList("108"),
		OtherNameList("109"),
		SalesRep("110"),
		ClassInfo("111"),
		BankInfo("112");

		private final String value;

		private HbaList(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}


	}

	public static enum UserAction{
		Create(0),
		Edit(1),
		ChangeStatus(2),
		Delete(3),
		Void(4),
		Convert(5),
		Renew(6),
		Post(7),
		Active(8),
		InActive(9),
		Priority(10),
		Blocked(11),
		Print(12),
		Approve(13),
		Dispose(14),
		Transfer(15),
		Deliver(16),
		PrintForAPP(17);
		
		private final int value;

		private UserAction(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}



}
