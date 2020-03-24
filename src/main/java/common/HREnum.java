package common;

public class HREnum 
{
	
	public static enum MenuIds 
	{
		CRM(80),
		CRMTasks(316),
		CRMSendEmail(318),
		CRMTemplates(319),
		zzz(1);

		private final int value;

		private MenuIds(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
	
	
	public static enum HRFormNames {
		HRTimesheetDetailed(68), HRLeaves(49), HRPassportRequest(47), HRLoanForm(
				55), HRTSSalarySheet(75);

		private final int value;

		private HRFormNames(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static enum HRStatus {
		HRNew(1), HREdit(2), HRApprove(3), HRDelete(4), HRVoid(5), HRHold(6), HRReturn(
				7), HRRefund(8), HRChangeAdjust(9), HRSetupDone(10), HRSetupRemove(
				11), HRPrint(12), HRPay(13), HRGenerateSIF(14), HRRemove(15), HRUse(
				16);

		private final int value;

		private HRStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static enum HRList {
		HRListNationality(1), HRListCountry(2), HRListCity(3), HRListSex(4), HRListBLOOD(
				5), HRListDEPARTMENT(6), HRListPOSITION(7), HRListCOMPTYPE(8), HRListMARITAl(
				9), HRListEDULEVEL(10), HRListEDUSPEC(11), HRListContact(12), HRListLANGUAGE(
				13), HRListSKILLS(14), HRListRate(15), HRListLEAVEREASON(16), HRListLeaveType(
				17), HRListMEDICAL(18), HRListTERMINATETYPE(19), HRListTERMINATEREASON(
				20), HRListRELATION(21), HRListDOCUMENTS(22), HRListBUSINESS(23), HRListDateFormat(
				24), HRListReligion(25), HRListCharacter(26), HRListLevel(27), HRListStatus(
				28), HRListReason(29), HRListDEFAllowances(30), HRListOTHAllowances(
				31), HRListBankName(32), HRListBankBranch(33), HRListBankAccount(
				34), HRListExperienceLeaveReason(35), HRListAbsenceReason(36), HRListAdditions(
				37), HRListDeductions(38), HRListAnnualLeave(39), HRListActivityStatus(
				40), HRListEmployeeOtherDocuments(41), HRListEmployeeOtherCustomFields(
				42), HRListProjectStatus(43), HRListLabourCategory(44), HRListLabourUnits(
				45), HRListLabourOccupation(46), HRListLabourServiceType(47), HRListClassification(
				48), HRListSection(49), HRListHoliday(50), HRListStreet(51), HRListInsuranceCompany(
				52), HRListForms(53), HRListFloor(54), HRListFlatType(55), HRListFlatSizeType(
				56), HRListFlatStatus(57), HRListEmirate(58), HRListFlatPurpose(
				60), HRListPropertyType(61), HRListOtherCharges(62), HRListParking(
				63), HRListComplaint(64), HRListMaintenance(65), HRListNotice(
				66), HRListGeneralMemo(67), HRListSendVia(68), HRListBillTerms(
				69), HRListCategory(70), HRListCustomerJob(71), HRListLocation(
				72), HRListSubCategory1(73), HRListSubCategory2(74), HRListSubCategory3(
				75), HRListSubLocationArea(76), HRListUnit(77), HRListEmployeeStatus(
				78), HRListNameTitle(79), HRListItemList(80), HRListEmployeeExpense(
				81), HRListTrainingCourse(82), HRListRecruitmentCompany(83), HRListTeachingPhase(
				84), HRListTeachingCycle(85), HRListTeachingLevel(86), HRListTeachingClass(
				87), HRlistStaffType(88), HRListRegion(89), HRListCurrency(90), HRListFamilyStatus(
				91), HRListTravelClass(92), HRListCompanyActivity(93), HRListSubject(
				94), HRListCompany(95), HRListProject(96), HRListSubProject(97), HRListAttachementName(
				125), HRListToDoStatus(126), HRListInsuranceType(127), HRListInsuranceCover(
				128), HRListPaymentType(129), HRListLicenceType(130), HRListAssetCondition(
				131), HRListWarrentyType(132), HRListVehicleType(133), HRListVehicleBrand(
				134), HRListVehicleSeries(135), HRListColor(136), HRListInventoryAdjNotes(
				137), HRListREDocumentCheckList(138), HRListCustomerStatus(139), HRListHowDidYouKnow(
				140), HRListTaskType(141), HRListTaskPriority(142), HRListTaskStatus(
				143), HRListFeedBackType(144), HRListCompanySize(145), HRListCurrentSoftware(
				146);

		private final int value;

		private HRList(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
