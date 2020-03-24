package hr;

import hr.model.CompanyModel;
import hr.model.LeaveParamsModel;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import layout.MenuModel;
import model.AllowanceModel;

import model.HRListValuesModel;


import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class DefaultSetupViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	DefaultSetupData data=new DefaultSetupData();
	HRData hrData=new HRData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<HRListValuesModel> lstHRAllowncesList;
	private  List<AllowanceModel> lstSalaries;
	
	private List<HRListValuesModel> lstHRLeaveList;
	private  List<AllowanceModel> lstLeaves;
	
	private  List<AllowanceModel> lstActivity;
	
	private Date salaryEffDate;
	private Date leaveEffDate;
	private Date eosEffDate;
	
	private LeaveParamsModel defLeavePARMS;
	private LeaveParamsModel defAllowancePARMS;
	private LeaveParamsModel defAbsencePARMS;
	private LeaveParamsModel defEOSPARMS;
	
	private MenuModel companyRole;	
	int menuID=300;
	
	public DefaultSetupViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			int defaultCompanyId=0;
			defaultCompanyId=hrData.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=hrData.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && defaultCompanyId==0)			
			selectedCompany=lstComapnies.get(0);
			
			lstHRAllowncesList=data.getHRAllowncesListValues();
			lstHRLeaveList=data.getHRLeaveListValues();
			
			defLeavePARMS=data.GetDefLeavePARAMS();
			defAllowancePARMS=data.GetDefAllowancePARAMS();
			defAbsencePARMS=data.GetDefAbsencePARAMS();
			defEOSPARMS=data.GetDefEOSPARAMS();
			
			getCompanyStartBussiness(selectedCompany.getCompKey());
			fillSalaryList();
			fillLeaveList();
			fillActivityList();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in DefaultSetupViewModel ----> Init", ex);			
		}
	}
	
	private void getCompanyRolePermessions(int companyRoleId)
	{
		setCompanyRole(new MenuModel());
		HRData data=new HRData();
		List<MenuModel> lstRoles= data.getHRRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				setCompanyRole(item);
				break;
			}
		}
	}
	
	private void getCompanyStartBussiness(int compKey)
	{
		Date startDate=hrData.getCompanyStartBussiness(compKey);
		if(startDate!=null)
		{
			salaryEffDate=startDate;
			leaveEffDate=startDate;
			eosEffDate=startDate;
		}				
	}
	
	
	private void fillSalaryList()
	{
		 //List<AllowanceModel> lst=new ListModelList<AllowanceModel>();
		 lstSalaries=new ListModelList<AllowanceModel>();
		 AllowanceModel objAllowance=new AllowanceModel();
		 AllowanceModel objNewAllowance;
		 int rowIndex=0;
		 
		 objAllowance.setSalaryItem("Basic Salary & Allowances(Monthly)");
		 objAllowance.setAllowanceType(0);
		 objAllowance.setHeadItem(true);
		 lstSalaries.add(objAllowance);
		 
		 objAllowance=new AllowanceModel();
		 objAllowance.setSalaryItem("     Basic Salary");
		 objAllowance.setSalaryId(0);
		 objAllowance.setAllowanceType(0);
		 objAllowance.setCompanyKey(selectedCompany.getCompKey());
		 lstSalaries.add(objAllowance);
		 
		 objAllowance=new AllowanceModel();
		 objAllowance.setSalaryItem("Benefit Payments");		 
		 objAllowance.setAllowanceType(1);
		 objAllowance.setHeadItem(true);
		 lstSalaries.add(objAllowance);
		 
		 objAllowance=new AllowanceModel();
		 objAllowance.setSalaryItem("Benefit Provided");		 
		 objAllowance.setAllowanceType(2);
		 objAllowance.setHeadItem(true);
		 lstSalaries.add(objAllowance);
		 
		 
	     int tmpDefAllowance;	
		 for (HRListValuesModel item : lstHRAllowncesList) 
		 {
			 tmpDefAllowance=item.getFieldId();
			 List<AllowanceModel> lst=data.getCompanyAllowance(selectedCompany.getCompKey(), item.getListId(), item.getEnDescription());
			 if(lst.size()>0)
			 {
				 AllowanceModel oldAllowance=lst.get(0);
				// if(tmpDefAllowance==30)
				//	 oldAllowance.setSalaryItem(oldAllowance.getSalaryItem() + " * ");
				 rowIndex=findLastRow(oldAllowance.getAllowanceType());
				 lstSalaries.add(rowIndex,oldAllowance);
			 }
			 else
			 {
				 objNewAllowance=new AllowanceModel();
				 objNewAllowance.setCompanyKey(selectedCompany.getCompKey());
				 switch (item.getListId()) {
				case 54: case 56:					
					objNewAllowance.setAllowanceType(1);
					objNewAllowance.setSalaryId(item.getListId());
					objNewAllowance.setSalaryItem(item.getEnDescription());
					 //if(tmpDefAllowance==30)
					//	 objNewAllowance.setSalaryItem(objNewAllowance.getSalaryItem() + " * ");
					 rowIndex=findLastRow(objNewAllowance.getAllowanceType());
					 lstSalaries.add(rowIndex,objNewAllowance);					 					
					break;

				case 55: case 57:					
					objNewAllowance.setAllowanceType(2);
					objNewAllowance.setSalaryId(item.getListId());
					objNewAllowance.setSalaryItem(item.getEnDescription());
					 //if(tmpDefAllowance==30)
					//	 objNewAllowance.setSalaryItem(objNewAllowance.getSalaryItem() + " * ");
					  rowIndex=findLastRow(objNewAllowance.getAllowanceType());
					 lstSalaries.add(rowIndex,objNewAllowance);					 					
					break;
					
				default:					
					objNewAllowance.setAllowanceType(0);
					objNewAllowance.setSalaryId(item.getListId());
					objNewAllowance.setSalaryItem(item.getEnDescription());
					// if(tmpDefAllowance==30)
					//	 objNewAllowance.setSalaryItem(objNewAllowance.getSalaryItem() + " * ");
					  rowIndex=findLastRow(objNewAllowance.getAllowanceType());
					 lstSalaries.add(rowIndex,objNewAllowance);	
					
					break;
				}
				 
			 }
		 }
		 
		 for (AllowanceModel item : lstSalaries) 
		 {
			 if(item.isHeadItem()==false)
			 {
				 item.setRowDisable(true);
				//if(item.getAllowanceType()==0)
				{
			 boolean hasSetup=data.geSalaryFromHRCondition(item.getCompanyKey(),item.getSalaryId(),true,null);
			 item.setSetupDone(hasSetup ? "Yes" : "No");
			 //item.setEditSalary(!hasSetup);
			 item.setHasSetup(hasSetup);
			
				}
				if(item.isHasSetup())
				GetSetupDataAllowances(item);
			 }
			 
		 }
	}
	
	private void GetSetupDataAllowances(AllowanceModel item)
	{
		int tmpRecordCount=0;
		tmpRecordCount =data.checkAllowncesAdvanced(item.getCompanyKey(),item.getSalaryId());
		if(tmpRecordCount==1)
		{
			AllowanceModel objAdvan=data.getAllowncesAdvanced(item.getCompanyKey(), item.getSalaryId());
			item.setChangeFromAdvancedSetup(objAdvan.isChangeFromAdvanced()?"YES":"");
			item.setMinimum(objAdvan.getMinimum());
			item.setMaximum(objAdvan.getMaximum());
			item.setPensionEmployee(objAdvan.getPensionEmployee());
			item.setPensionCompany(objAdvan.getPensionCompany());
			item.setEditPension(objAdvan.isEditPension());
		}
		else if(tmpRecordCount==0)
		{
			item.setChangeFromAdvancedSetup("");
		}
		else
		{
			item.setChangeFromAdvancedSetup("YES");
		}
				
	}
	private int findLastRow(int allowanceType)
	{
		int type1=0;
		int type2=0;
		int type3=0;
		
		int lastRow=0;
		int checkRow=0;
		
		for (AllowanceModel item : lstSalaries) 
		{			
			if(item.getAllowanceType()==0)
			{
				type1++;				
			}
		}
		
		for (AllowanceModel item : lstSalaries) 
		{			
			if(item.getAllowanceType()==1)
			{
				type2++;				
			}
		}
		
		for (AllowanceModel item : lstSalaries) 
		{			
			if(item.getAllowanceType()==2)
			{
				type3++;				
			}
		}
		
		if(allowanceType==0)
			return type1;
		
		if(allowanceType==1)
			return type1+type2;
		
		if(allowanceType==2)
			return type1+type2+type3;
		
		
		/*for (int i = 2; i < lstSalaries.size(); i++)
		{
		if(lstSalaries.get(i).getAllowanceType()==allowanceType)
		{
			checkRow=i;
			break;
		}
		}
		
		for (int i = checkRow; i < lstSalaries.size(); i++)
		{
			if(lstSalaries.get(i).getAllowanceType()!=0)
			{
				if(lstSalaries.get(i).getAllowanceType()!=allowanceType)
				{
					lastRow=i;
				}
			}
		}*/
		if(lastRow==0)
		lastRow=lstSalaries.size();
		return lastRow;
	}

	
	private void fillLeaveList()
	{
		
		 lstLeaves=new ListModelList<AllowanceModel>();
		 AllowanceModel objAllowance=new AllowanceModel();	
		 objAllowance.setCompanyKey(selectedCompany.getCompKey());
		 objAllowance.setLeaveId(100);
		 objAllowance.setLeaveType("Annual Leave");		
		 objAllowance.setNoOfDays(30);		 
		 lstLeaves.add(objAllowance);
		 
		 for (HRListValuesModel item : lstHRLeaveList) 
		 {
			 objAllowance=new AllowanceModel();		
			 objAllowance.setCompanyKey(selectedCompany.getCompKey());
			 objAllowance.setLeaveId(item.getListId());
			 objAllowance.setLeaveType(item.getEnDescription());
			 
			switch (item.getListId()) 
			{
			case 74:
				 objAllowance.setNoOfDays(15);	
				break;
			case 75:
				 objAllowance.setNoOfDays(45);	
				break;
			case 76:
				 objAllowance.setNoOfDays(21);	
				break;
			case 77:
				 objAllowance.setNoOfDays(60);	
				break;
			case 78:
				 objAllowance.setNoOfDays(3);	
				break;
			default:
				 objAllowance.setNoOfDays(10);	
				break;
			}
			
			lstLeaves.add(objAllowance);
			 
		 }
		 
		 for (AllowanceModel item : lstLeaves) 
		 {			
			 boolean hasSetup=data.geSalaryFromHRCondition(item.getCompanyKey(),item.getLeaveId(),false,null);
			 item.setSetupDone(hasSetup ? "Yes" : "No");
			 //item.setEditSalary(!hasSetup);
			 item.setHasSetup(hasSetup);	
			 GetSetupLeaves(item);
		 }
		
	}
	
	private void GetSetupLeaves(AllowanceModel item)
	{
		int tmpRecordCount=0;
		tmpRecordCount =data.checkLeavesAdvanced(item.getCompanyKey(),item.getLeaveId());
		if(tmpRecordCount==1)
		{
			AllowanceModel objAdvan=data.getLeavesAdvanced(item.getCompanyKey(), item.getLeaveId());
			item.setChangeFromAdvancedSetup(objAdvan.isChangeFromAdvanced()?"YES":"");
			item.setDaysAllowed(objAdvan.getDaysAllowed());
		}
		else if(tmpRecordCount==0)
		{
			item.setChangeFromAdvancedSetup("");
		}
		else
		{
			item.setChangeFromAdvancedSetup("YES");
		}
				
	}
	
	private void fillActivityList()
	{
		lstActivity=new ListModelList<AllowanceModel>();
		
		 AllowanceModel objAllowance=new AllowanceModel();	
		 objAllowance.setCompanyKey(selectedCompany.getCompKey());		
		 objAllowance.setLeaveType("Absence");
		 boolean hasSetup=data.geSalaryFromHRCondition(objAllowance.getCompanyKey(),0,false,"ABSENCE");
		 objAllowance.setSetupDone(hasSetup ? "Yes" : "No");
		 objAllowance.setHasSetup(hasSetup);	
		 lstActivity.add(objAllowance);
		 
		 objAllowance=new AllowanceModel();	
		 objAllowance.setCompanyKey(selectedCompany.getCompKey());		
		 objAllowance.setLeaveType("EOS(Labour Law)");	
		 hasSetup=data.geSalaryFromHRCondition(objAllowance.getCompanyKey(),0,false,"EOS");
		 objAllowance.setSetupDone(hasSetup ? "Yes" : "No");
		 objAllowance.setHasSetup(hasSetup);			 
		 lstActivity.add(objAllowance);
		 
		 objAllowance=new AllowanceModel();	
		 objAllowance.setCompanyKey(selectedCompany.getCompKey());		
		 objAllowance.setLeaveType("Additions & Deductions");	
		 hasSetup=data.getHRAdditionSetup(objAllowance.getCompanyKey());
		 objAllowance.setSetupDone(hasSetup ? "Yes" : "No");
		 objAllowance.setHasSetup(hasSetup);	
		 lstActivity.add(objAllowance);
		 
	}
	
	@Command	
	@NotifyChange({"lstSalaries","salaryEffDate","leaveEffDate","eosEffDate","lstLeaves","lstActivity"})
	public void saveCommand()
	{
		try 
		{
			boolean tmpSalSelected=false;
			boolean tmpLeaveSelected=false;
			boolean tmpActivitySelected=false;
			
			for (AllowanceModel item : lstSalaries) 
			{
				if(item.isEditSalary())
				{
					tmpSalSelected=true;
					break;
				}
			}
			
			for (AllowanceModel item : lstLeaves) 
			{
				if(item.isEditSalary())
				{
					tmpLeaveSelected=true;
					break;
				}
			}
			
			for (AllowanceModel item : lstActivity) 
			{
				if(item.isEditSalary())
				{
					tmpActivitySelected=true;
					break;
				}
			}
			
			if(tmpSalSelected==false && tmpLeaveSelected==false && tmpActivitySelected==false)
			{
				Messagebox.show("Nothing to Save !!","Default Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			//SaveDefault_SALARIES
			SaveDefault_SALARIES();
			Save_DefaultLeaves();
			
			logger.info("Updating Basic Salary............");
			logger.info("Updating Allowances............");
			SetupBasicSalaries();
			
			//SetupLeaves
			logger.info("Updating Leaves............");
			SetupLeaves();
			
			
			logger.info("Updating Activity............");
			setupActivity();
			
			
			//reload the lists
			getCompanyStartBussiness(selectedCompany.getCompKey());
			fillSalaryList();
			fillLeaveList();
			fillActivityList();
			Messagebox.show("Setup Completed Successfully.","Default Setup", Messagebox.OK , Messagebox.INFORMATION);
		}
		
		catch (Exception ex)
		{	
			logger.error("ERROR in DefaultSetupViewModel ----> saveCommand", ex);			
		}
	}
	
	private void SaveDefault_SALARIES()
	{
		
	
		List<AllowanceModel> lstBASICSALColumns=data.getHRColumns("BASICSAL");
		List<AllowanceModel> lstALLOWANCEColumns=data.getHRColumns("ALLOWANCE");
		
		for (AllowanceModel item : lstSalaries)
		{
			if(item.isEditSalary())
			{
				if(item.getSalaryId()==0)
				{
					//SaveDefault_BASICSAL(inti)
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=0;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstBASICSALColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 9:case 11:case 12:case 13:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{
								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setActivity("BASICSAL");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
				}
				else
				{
					//Save_Allowances
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=0;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstALLOWANCEColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 9:case 17:case 18:case 22:case 13:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{
								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setActivity("ALLOWANCE");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
				}
			}
		}
	}
	
	private void Save_DefaultLeaves()
	{
		List<AllowanceModel> lstLEAVEColumns=data.getHRColumns("LEAVE");
		
		for (AllowanceModel item : lstLeaves) 
		{
			if(item.isEditSalary())
			{
				if(item.getLeaveId()==76)
				{
					//Save_HajLeave
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=0;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstLEAVEColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 10:case 11:case 12:case 13:case 14:case 15:case 16:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{
								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setSalaryId(item.getLeaveId());
								item.setActivity("LEAVE");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
				}
				else if(item.getLeaveId()==75 || item.getLeaveId()==78)
				{
					//Save_MaternityLeave
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=0;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstLEAVEColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 7:case 8 :case 11:case 12:case 13:case 14:case 15:case 16:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{
								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setSalaryId(item.getLeaveId());
								item.setActivity("LEAVE");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
				}
				else
				{
					//Save_AnnualLeave
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=0;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstLEAVEColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 11:case 12:case 13:case 14:case 15:case 16:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{
								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setSalaryId(item.getLeaveId());
								item.setActivity("LEAVE");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
				}
			}
		}
	}
	
	private void SetupBasicSalaries()
	{
		int tmpLevelID,tmpSECID,tmpClassID,tmpSalaryItem,tmpEnCashItem,tmpFixed,tmpKidsNos,tmpKidsAgeFrom,tmpKidsAgeTo,tmpRoomNos,tmpSalaryNos,tmpBasicPer,tmpDistanse,
		tmpDepID,tmpPosID,tmpMaritalID,tmpSexID,tmpReligion;
		String tmpEMPType,tmpNationality,tmpTransfer2NextYear="",tmpSalaryRow="",tmpEncashRow="",tmpCondRow="",tmpMinWorkMode="",tmpCalculate="",tmpMode,
				tmpHouseType;
		double tmpMinWorkPeriod,tmpMonthFrom,tmpMonthTo,tmpDaysAllowed,tmpDuration,tmpSalaryPER;
		
				
		
		int tmpPensionEMP=0;
		int tmpPensionCOMP=0;
		for (AllowanceModel item : lstSalaries)
		{
			if(item.isEditSalary())
			{
				tmpLevelID=Integer.parseInt(defAllowancePARMS.getLevelID());
				tmpDepID=Integer.parseInt(defAllowancePARMS.getDepID());
				tmpPosID=Integer.parseInt(defAllowancePARMS.getPosID());
				tmpSECID=Integer.parseInt(defAllowancePARMS.getSecID());
				tmpClassID=Integer.parseInt(defAllowancePARMS.getClassID());
				tmpEMPType=defAllowancePARMS.getEmpType();
				tmpSexID=Integer.parseInt(defAllowancePARMS.getSexID());
				tmpMaritalID=Integer.parseInt(defAllowancePARMS.getMaritalID());
				tmpNationality=defAllowancePARMS.getNationality();
				tmpReligion=Integer.parseInt(defAllowancePARMS.getReligion());
				tmpFixed=Integer.parseInt(defAllowancePARMS.getFixed());					
				
				tmpKidsNos=Integer.parseInt(defAllowancePARMS.getKidsNo());
				tmpKidsAgeFrom=Integer.parseInt(defAllowancePARMS.getKidsAgeFrom());
				tmpKidsAgeTo=Integer.parseInt(defAllowancePARMS.getKidsAgeTo());
				tmpHouseType=defAllowancePARMS.getHouseType();
				tmpRoomNos=Integer.parseInt(defAllowancePARMS.getRoomsNo());
				tmpSalaryNos=Integer.parseInt(defAllowancePARMS.getSalaryNos());
				tmpBasicPer=Integer.parseInt(defAllowancePARMS.getBasicPer());
				tmpDistanse=Integer.parseInt(defAllowancePARMS.getDistance());	
				tmpPensionEMP=0;
				tmpPensionCOMP=0;
				
				if(item.getSalaryId()==0)
				{
					int CondRecNo=data.generateRecNo("HRCONDITIONS", "REC_NO", "BASICSAL",0);
					int  SetupRecNo = data.getMaxID("HRSETUPS", "REC_NO");
					data.deleteHRSetup(SetupRecNo);
					AllowanceModel obj=new AllowanceModel();
					obj.setCondNo(CondRecNo);
					obj.setRecNo(SetupRecNo);
					obj.setCompanyKey(item.getCompanyKey());
					obj.setEffectiveDate(salaryEffDate);
					obj.setActivity("BASICSAL");
					data.addHRSetup(obj);
					
					int tmpRangeTo=1;
					if(item.isEditPension())
					{
						tmpRangeTo=2;
					}
					for (int i = 1; i <= tmpRangeTo; i++) 
					{
						int tmpBasicRecNo = data.getMaxID("COMPANYSALARY", "REC_NO");
						switch (i) 
						{
						case 1:
							if(item.isEditPension())
							{
								 tmpNationality = "L";
								 tmpPensionEMP=item.getPensionEmployee();
								 tmpPensionCOMP=item.getPensionCompany();
							}
							else
							{
							    tmpNationality = "A";
		                        tmpPensionEMP = 0;
		                        tmpPensionCOMP = 0;
							}							
							break;
						case 2:
							tmpNationality = "N";
	                        tmpPensionEMP = 0;
	                        tmpPensionCOMP = 0;
							break;
							
						default:
							break;
						}
						obj=new AllowanceModel();
						obj.setEffectiveDate(salaryEffDate);
						obj.setRecNo(tmpBasicRecNo);
						obj.setLineNo(i);
						obj.setAllowanceId(0);
						obj.setCompanyKey(item.getCompanyKey());
						obj.setLeaveId(tmpLevelID);
						obj.setLeaveId(tmpLevelID);
						obj.setDepId(tmpDepID);
						obj.setPosId(tmpPosID);
						obj.setSecID(tmpSECID);
						obj.setClassId(tmpClassID);
						obj.setEmpType(tmpEMPType);
						obj.setSexId(tmpSexID);
						obj.setMaritalId(tmpMaritalID);
						obj.setNationality(tmpNationality);
						obj.setReligionId(tmpReligion);
						obj.setFixed(tmpFixed);						
						obj.setMinimum(item.getMinimum());
						obj.setMaximum(item.getMaximum());
						obj.setPensionEmployee(tmpPensionEMP);
						obj.setPensionCompany(tmpPensionCOMP);
						data.addCompanySalary(obj);
						
						//UpdateCondition
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "LEVEL_ID", tmpLevelID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "DEP_ID", tmpDepID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "POS_ID", tmpPosID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "SECTION_ID", tmpSECID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "CLASS_ID", tmpClassID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "EMP_TYPE", tmpEMPType);
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "SEX_ID", tmpSexID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "MARITAL_ID", tmpMaritalID+"");
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "NATIONALITY", tmpNationality);
						updateSetupConditions("BASICSAL", item.getCompanyKey(), tmpBasicRecNo, "RELIGION_ID", tmpReligion+"");
						
					}
				}
				else
				{
					//SetupOtherAllowances
					int tmpAllowanceType=0;
					int tmpPayPeriod=0;
					String tmpPayMode="M";
					if(item.getSalaryId()==54 || item.getSalaryId()==56)
					{
						tmpAllowanceType = 1;
			            tmpPayPeriod = 0;
			            tmpPayMode = "S";
					}
					else if(item.getSalaryId()==55 || item.getSalaryId()==57)
					{
						tmpAllowanceType = 2;
			            tmpPayPeriod = 0;
			            tmpPayMode = "S";
					}
					else
					{
						tmpAllowanceType = 0;
			            tmpPayPeriod = 1;
			            tmpPayMode = "M";
					}
					
					
					int CondRecNo=data.generateRecNo("HRCONDITIONS", "REC_NO", "ALLOWANCE",item.getSalaryId());
					int  SetupRecNo = data.getMaxID("HRSETUPS", "REC_NO");
					AllowanceModel obj=new AllowanceModel();
					obj.setCondNo(CondRecNo);
					obj.setRecNo(SetupRecNo);
					obj.setCompanyKey(item.getCompanyKey());
					obj.setEffectiveDate(salaryEffDate);
					obj.setActivity("ALLOWANCE"+item.getSalaryId());
					data.addHRSetup(obj);
					
					int tmpRangeTo=1;
					if(item.isEditPension())
					{
						tmpRangeTo=2;
					}
					for (int i = 1; i <= tmpRangeTo; i++) 
					{
						int tmpAllowanceRecNo = data.getMaxID("COMPALLOWANCE", "REC_NO");
						switch (i) 
						{
						case 1:
							if(item.isEditPension())
							{
								 tmpNationality = "L";
								 tmpPensionEMP=item.getPensionEmployee();
								 tmpPensionCOMP=item.getPensionCompany();
							}
							else
							{
							    tmpNationality = "A";
		                        tmpPensionEMP = 0;
		                        tmpPensionCOMP = 0;
							}							
							break;
						case 2:
							tmpNationality = "N";
	                        tmpPensionEMP = 0;
	                        tmpPensionCOMP = 0;
							break;
							
						default:
							break;
						}
						
						obj=new AllowanceModel();
						obj.setEffectiveDate(salaryEffDate);
						obj.setRecNo(tmpAllowanceRecNo);
						obj.setLineNo(i);
						obj.setAllowanceId(item.getSalaryId());
						obj.setCompanyKey(item.getCompanyKey());
						obj.setLeaveId(tmpLevelID);
						obj.setLeaveId(tmpLevelID);
						obj.setDepId(tmpDepID);
						obj.setPosId(tmpPosID);
						obj.setSecID(tmpSECID);
						obj.setClassId(tmpClassID);
						obj.setEmpType(tmpEMPType);
						obj.setSexId(tmpSexID);
						obj.setMaritalId(tmpMaritalID);
						obj.setNationality(tmpNationality);
						obj.setReligionId(tmpReligion);
						obj.setKidsNo(tmpKidsNos);
						obj.setKidsAgeFrom(tmpKidsAgeFrom);
						obj.setKidsAgeTo(tmpKidsAgeTo);
						obj.setAllowanceType(tmpAllowanceType);
						obj.setHouseType(tmpHouseType);
						obj.setRoomsNo(tmpRoomNos);
						obj.setSalaryNos(tmpSalaryNos);
						obj.setFixed(tmpFixed);											
						obj.setMinimum(item.getMinimum());
						obj.setMaximum(item.getMaximum());
						obj.setBasicPer(tmpBasicPer);
						obj.setPayPeriod(tmpPayPeriod);
						obj.setPayMode(tmpPayMode);
						obj.setPensionEmployee(tmpPensionEMP);
						obj.setPensionCompany(tmpPensionCOMP);
						data.addCompanyAllowances(obj);
						
						//UpdateCondition
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "LEVEL_ID", tmpLevelID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "DEP_ID", tmpDepID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "POS_ID", tmpPosID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "SECTION_ID", tmpSECID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "CLASS_ID", tmpClassID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "EMP_TYPE", tmpEMPType);
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "SEX_ID", tmpSexID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "MARITAL_ID", tmpMaritalID+"");
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "NATIONALITY", tmpNationality);
						updateSetupConditions("ALLOWANCE"+item.getSalaryId(), item.getCompanyKey(), tmpAllowanceRecNo, "RELIGION_ID", tmpReligion+"");
						
					}
					
				}
				
			}
		}
	}
	
	private void updateSetupConditions(String activity,int compKey,int recNo,String condition,String condValue)
	{
		AllowanceModel obj=new AllowanceModel();
		obj.setActivity(activity);
		obj.setCompanyKey(compKey);
		obj.setRecNo(recNo);
		obj.setCondition(condition);
		obj.setCondValue(condValue);
		data.deleteHRSetupConditionsQuery(obj);
		data.addHRSetupConditions(obj);
		
	}
	
	
	private void SetupLeaves()
	{
		int tmpLevelID,tmpSECID,tmpClassID,tmpSalaryItem,tmpEnCashItem,
		tmpDepID,tmpPosID,tmpMaritalID,tmpSexID,tmpReligion;
		String tmpEMPType,tmpNationality,tmpTransfer2NextYear="",tmpSalaryRow="",tmpEncashRow="",tmpCondRow="",tmpMinWorkMode="",tmpCalculate="",tmpMode;
		double tmpMinWorkPeriod,tmpMonthFrom,tmpMonthTo,tmpDaysAllowed,tmpDuration,tmpSalaryPER;
									
		for (AllowanceModel item : lstLeaves)
		{
			if(item.isEditSalary())
			{
				int CondRecNo=data.generateRecNo("HRCONDITIONS", "REC_NO", "LEAVE",item.getLeaveId());
				int  SetupRecNo = data.getMaxID("HRSETUPS", "REC_NO");
				AllowanceModel obj=new AllowanceModel();
				obj.setCondNo(CondRecNo);
				obj.setRecNo(SetupRecNo);
				obj.setCompanyKey(item.getCompanyKey());
				obj.setEffectiveDate(leaveEffDate);
				obj.setActivity("LEAVE"+item.getLeaveId());
				data.addHRSetup(obj);
				
				tmpLevelID=Integer.parseInt(defLeavePARMS.getLevelID());
				tmpDepID=Integer.parseInt(defLeavePARMS.getDepID());
				tmpPosID=Integer.parseInt(defLeavePARMS.getPosID());
				tmpSECID=Integer.parseInt(defLeavePARMS.getSecID());
				tmpClassID=Integer.parseInt(defLeavePARMS.getClassID());
				tmpEMPType=defLeavePARMS.getEmpType();
				tmpSexID=Integer.parseInt(defLeavePARMS.getSexID());
				tmpMaritalID=Integer.parseInt(defLeavePARMS.getMaritalID());
				tmpNationality=defLeavePARMS.getNationality();
				tmpReligion=Integer.parseInt(defLeavePARMS.getReligion());
				tmpMinWorkPeriod=defLeavePARMS.getMinWorkPeriod();
				tmpMinWorkMode=defLeavePARMS.getMinWorkMode();
				tmpTransfer2NextYear=defLeavePARMS.getTransfer2NextYear();
				tmpMonthFrom=defLeavePARMS.getMonthFrom();
				tmpMonthTo=defLeavePARMS.getMonthTo();
				tmpDaysAllowed=defLeavePARMS.getDaysAllowed();
				tmpDuration=defLeavePARMS.getDuration();
				tmpMode=defLeavePARMS.getMode();
				tmpCalculate=defLeavePARMS.getCalculate();
				tmpSalaryPER=defLeavePARMS.getSalaryPER();
				tmpSalaryItem=defLeavePARMS.getSalaryItem();
				tmpEnCashItem=defLeavePARMS.getEncashItem();
				
				int tmpLeaveRecNo = data.getMaxID("LEAVESETUP", "REC_NO");
				int tmpRangeTo=1;
				
				//tmpDaysAllowed=0;
				tmpSalaryPER=100;
				tmpMode = "Y";
				//tmpSalaryItem=-1;
				//tmpMinWorkPeriod=1;
				//tmpMinWorkMode="M";
				//tmpEnCashItem=-1;
				//tmpDepID=-1;
				//tmpPosID=-1;
				//tmpMaritalID=-1;
				//tmpSexID=-1;
				//tmpReligion=-1;
				//tmpCalculate="Y";
				
				//String tmpMode="Y";				
				if(item.getLeaveId()==100)
				{
					tmpRangeTo=3;
					tmpTransfer2NextYear="Y";
					//tmpDaysAllowed=0;
				}
				else
				{
					tmpRangeTo=1;
					tmpTransfer2NextYear="N";
					tmpDaysAllowed=item.getNoOfDays();
					tmpSalaryRow="Y";
					tmpEncashRow="Y";
					tmpCondRow="Y";
				}
				
				for (int i = 1; i <= tmpRangeTo; i++) 
				{
					switch (item.getLeaveId()) 
					{
					case 100:
						if(i==1)
						{
							tmpMonthFrom=0;
							tmpMonthTo=6;
							tmpSalaryRow="Y";
							tmpEncashRow="Y";
							tmpCondRow="Y";
							tmpDaysAllowed=0;
							
						}
						else if(i==2)
						{
							tmpMonthFrom=7;
							tmpMonthTo=12;
							tmpSalaryRow="";
							tmpEncashRow="";
							tmpCondRow="";
							tmpDaysAllowed=24;
							tmpSalaryItem=0;
							tmpEnCashItem=0;
							tmpTransfer2NextYear="";
							tmpMinWorkPeriod=0;
							tmpMinWorkMode="";
							tmpDepID=0;
							tmpPosID=0;
						}
						else
						{
							tmpMonthFrom=12;
							tmpMonthTo=0;
							tmpSalaryRow="";
							tmpEncashRow="";
							tmpCondRow="";
							tmpDaysAllowed=30;
							tmpSalaryItem=0;
							tmpEnCashItem=0;
							tmpTransfer2NextYear="";
							tmpMinWorkPeriod=0;
							tmpMinWorkMode="";
							tmpDepID=0;
							tmpPosID=0;
						}
						break;
					case 75:
						tmpMaritalID=39;
						tmpSexID=2;
						tmpMonthFrom=1;
						tmpMonthTo=0;
						break;
					case 76:
						tmpReligion=576;					
						tmpMonthFrom=1;
						tmpMonthTo=0;
						break;	
					case 77:										
						tmpMonthFrom=1;
						tmpMonthTo=0;
						tmpCalculate="N";
						break;	
					case 78:	
						tmpMaritalID=39;
						tmpSexID=1;
						tmpMonthFrom=1;
						tmpMonthTo=0;						
						break;							
					default:
						tmpMonthFrom=1;
						tmpMonthTo=0;	
						break;
					}
					
					//add
					obj=new AllowanceModel();
					obj.setAllowanceId(item.getLeaveId());
					obj.setEffectiveDate(leaveEffDate);
					obj.setRecNo(tmpLeaveRecNo);
					obj.setLineNo(i);				
					obj.setCompanyKey(item.getCompanyKey());
					obj.setLeaveId(tmpLevelID);
					obj.setDepId(tmpDepID);
					obj.setPosId(tmpPosID);
					obj.setSecID(tmpSECID);
					obj.setClassId(tmpClassID);
					obj.setEmpType(tmpEMPType);
					obj.setSexId(tmpSexID);
					obj.setMaritalId(tmpMaritalID);
					obj.setReligionId(tmpReligion);
					obj.setNationality(tmpNationality);
					obj.setMinWorkPeriod(tmpMinWorkPeriod);
					obj.setMinWorkFlag(tmpMinWorkMode);
					obj.setTransfer2NextYear(tmpTransfer2NextYear);
					obj.setSalaryPER(tmpSalaryPER);									
					obj.setSalaryId(tmpSalaryItem);
					obj.setSalaryRow(tmpSalaryRow);
					obj.setCondRow(tmpCondRow);
					obj.setEncashItem(tmpEnCashItem);
					obj.setEncashRow(tmpEncashRow);																				
					data.addLeaveSetup(obj);
					
					//UpdateCondition
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "LEVEL_ID", tmpLevelID+"");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "DEP_ID", tmpDepID+"");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "POS_ID", tmpPosID+"");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "SECTION_ID",tmpSECID+ "");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "CLASS_ID", tmpClassID+"");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "EMP_TYPE", tmpEMPType);
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "SEX_ID", tmpSexID+"");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "MARITAL_ID", tmpMaritalID+"");
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "NATIONALITY", tmpNationality);
					updateSetupConditions("LEAVE"+item.getLeaveId(), item.getCompanyKey(), tmpLeaveRecNo, "RELIGION_ID", tmpReligion+"");
					
					//LEAVECALCULATION
					tmpSalaryRow = "Y";
					obj=new AllowanceModel();
					obj.setEffectiveDate(leaveEffDate);
					obj.setRecNo(tmpLeaveRecNo);
					obj.setLineNo(i);				
					obj.setCompanyKey(item.getCompanyKey());
					obj.setSalaryRow(tmpSalaryRow);
					obj.setMonthFrom(tmpMonthFrom);
					obj.setMonthTo(tmpMonthTo);
					obj.setDaysAllowed(tmpDaysAllowed);
					obj.setDuration(tmpDuration);
					obj.setMode(tmpMode);
					obj.setCalculate(tmpCalculate);
					data.addLeaveCalculation(obj);
					
				}
			}
		}
	}
	
	private void setupActivity()
	{
		for (AllowanceModel item : lstActivity)
		{
			if(item.isEditSalary())
			{
				if(item.getLeaveType().equals("Absence"))
				{
					logger.info("Updating Absence............");
					List<AllowanceModel> lstABSENCEColumns=data.getHRColumns("ABSENCE");
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=-1;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstABSENCEColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 11:case 12:case 13:case 14:case 15:case 16:case 17:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setActivity("ABSENCE");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
					
					//SetupAbsenceDefaults
					SetupAbsenceDefaults(item);
				}
				else if(item.getLeaveType().contains("EOS"))
				{
					logger.info("Updating EOS............");
					List<AllowanceModel> lstEOSColumns=data.getHRColumns("EOS");
					int newID=data.getMaxID("HRCONDITIONS", "REC_NO");
					int tmpPRvColumnID=-1;
					int tmpSrlNo=0;
					for (AllowanceModel hrColumns : lstEOSColumns) 
					{
						switch (hrColumns.getColumnId()) 
						{
						case 2:case 3:case 9: case 11:case 12:case 13:case 14:
							if(tmpPRvColumnID!=hrColumns.getColumnId())
							{								
								tmpPRvColumnID=hrColumns.getColumnId();
								tmpSrlNo++;
								
								item.setLineNo(tmpSrlNo);
								item.setRecNo(newID);
								item.setActivity("EOS");
								item.setColumnId(tmpPRvColumnID);
								item.setAllowMultiple(hrColumns.getAllowMultiple());
								data.addHRConditions(item);
							}
							break;

						default:
							break;
						}
					}
					
					 SetupEOSDefaults(item);
					
				}
				else if(item.getLeaveType().contains("Additions"))
				{
					logger.info("Updating Additions............");
					int newID=data.getMaxID("ADDITIONSETUP", "REC_NO");
					int tmpLineNo=data.generateLineNo("ADDITIONSETUP", "LINE_NO", newID);
					AllowanceModel obj=new AllowanceModel();
					obj.setRecNo(newID);
					obj.setLineNo(tmpLineNo);
					obj.setCompanyKey(item.getCompanyKey());
					obj.setEffectiveDate(eosEffDate);
					obj.setCondRow("A");
					data.addAdditionSetup(obj);
					
					obj=new AllowanceModel();
					obj.setRecNo(newID);
					obj.setLineNo(tmpLineNo+1);
					obj.setCompanyKey(item.getCompanyKey());
					obj.setEffectiveDate(eosEffDate);
					obj.setCondRow("D");
					data.addAdditionSetup(obj);
					
				}
			}
		}
		
	}
	
	private void SetupAbsenceDefaults(AllowanceModel item)
	{
		
		int tmpLevelID,tmpSECID,tmpClassID,tmpSalaryItem,tmpEnCashItem,tmpDeductionID,
		tmpDepID,tmpPosID,tmpMaritalID,tmpSexID,tmpReligion;
		String tmpEMPType,tmpNationality,tmpTransfer2NextYear="",tmpSalaryRow="",tmpEncashRow="",tmpCondRow="",tmpMinWorkMode="",tmpMode,
				tmpExcuse,tmpAbsenceType,tmpMHFlag,tmpDeductService,tmpCalculate,tmpDeductFrom;
		double tmpMinWorkPeriod,tmpMonthFrom,tmpMonthTo,tmpDaysAllowed,tmpDuration,tmpSalaryPER,tmpMaxAllowed,tmpDeductionRate,tmpDeductionNos;
		
		tmpLevelID=Integer.parseInt(defAbsencePARMS.getLevelID());
		tmpDepID=Integer.parseInt(defAbsencePARMS.getDepID());
		tmpPosID=Integer.parseInt(defAbsencePARMS.getPosID());
		tmpSECID=Integer.parseInt(defAbsencePARMS.getSecID());
		tmpClassID=Integer.parseInt(defAbsencePARMS.getClassID());
		tmpEMPType=defAbsencePARMS.getEmpType();
		tmpSexID=Integer.parseInt(defAbsencePARMS.getSexID());
		tmpMaritalID=Integer.parseInt(defAbsencePARMS.getMaritalID());
		tmpNationality=defAbsencePARMS.getNationality();
		tmpReligion=Integer.parseInt(defAbsencePARMS.getReligion());
		tmpExcuse=defAbsencePARMS.getExecuse();
		tmpAbsenceType=defAbsencePARMS.getAbsenceType();
		tmpMaxAllowed=defAbsencePARMS.getMaxAllowed();
		tmpMHFlag=defAbsencePARMS.getCalculateIn();
		tmpDeductionRate=defAbsencePARMS.getDblRate();
		tmpDeductionNos=defAbsencePARMS.getNos();
		tmpDeductService=defAbsencePARMS.getDeductService();
		tmpCalculate=defAbsencePARMS.getCalculate();
		tmpDeductFrom=defAbsencePARMS.getDeductFrom();
		tmpDeductionID=defAbsencePARMS.getDeductionItem();
						
		
		int CondRecNo=data.generateRecNo("HRCONDITIONS", "REC_NO", "ABSENCE",0);
		int  SetupRecNo = data.getMaxID("HRSETUPS", "REC_NO");
		AllowanceModel obj=new AllowanceModel();
		obj.setCondNo(CondRecNo);
		obj.setRecNo(SetupRecNo);
		obj.setCompanyKey(item.getCompanyKey());
		obj.setEffectiveDate(eosEffDate);
		obj.setActivity("ABSENCE");
		data.addHRSetup(obj);
		
		int tmpRangeTo=2;				
		tmpMaxAllowed = 10;
		tmpAbsenceType = "D";
		
		for (int i = 1; i <= tmpRangeTo; i++) 
		{
			switch (i) 
			{
			case 1:
				tmpExcuse="Y";
				tmpDeductionRate=0;
				tmpDeductionNos=0;
				tmpCalculate="N";
				tmpDeductFrom="";
				tmpDeductionID=0;
				break;
				
			case 2:
				tmpExcuse="N";
				tmpDeductionRate=1;
				tmpDeductionNos=10;
				tmpCalculate="Y";
				tmpDeductFrom="S";
				tmpDeductionID=-1;
				break;
				
			default:
				break;
				
			}
			
			int tmpAbsenceRecNo = data.getMaxID("ABSENCESETUP", "REC_NO");
			obj=new AllowanceModel();
			obj.setEffectiveDate(eosEffDate);
			obj.setRecNo(tmpAbsenceRecNo);
			obj.setLineNo(i);
			obj.setCompanyKey(item.getCompanyKey());
			obj.setLeaveId(tmpLevelID);
			obj.setDepId(tmpDepID);
			obj.setPosId(tmpPosID);
			obj.setSecID(tmpSECID);
			obj.setClassId(tmpClassID);
			obj.setEmpType(tmpEMPType);
			obj.setSexId(tmpSexID);
			obj.setMaritalId(tmpMaritalID);
			obj.setReligionId(tmpReligion);
			obj.setNationality(tmpNationality);								
			obj.setCondRow(tmpExcuse);
			data.addAbsenceSetup(obj);
			
			//UpdateCondition
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "LEVEL_ID", tmpLevelID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "DEP_ID", tmpDepID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "POS_ID", tmpPosID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "SECTION_ID", tmpSECID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "CLASS_ID", tmpClassID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "EMP_TYPE", tmpEMPType);
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "SEX_ID", tmpSexID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "MARITAL_ID", tmpMaritalID+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "NATIONALITY", tmpNationality);
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "RELIGION_ID", tmpReligion+"");
			updateSetupConditions("ABSENCE", item.getCompanyKey(), tmpAbsenceRecNo, "EXCUSE", tmpExcuse);
			
			//ABSENCECALCULATION
			obj=new AllowanceModel();
			obj.setEffectiveDate(eosEffDate);
			obj.setRecNo(tmpAbsenceRecNo);
			obj.setLineNo(i);
			obj.setCompanyKey(item.getCompanyKey());
			obj.setMhFlag(tmpMHFlag);
			obj.setDeductionRate(tmpDeductionRate);
			obj.setDeductionNo(tmpDeductionNos);
			obj.setDeductionService(tmpDeductService);
			obj.setCalculate(tmpCalculate);
			obj.setDeductionFrom(tmpDeductFrom);
			obj.setDeductionId(tmpDeductionID);
			data.addAbsenceCalculation(obj);
		}
		
	}
	
	private void  SetupEOSDefaults(AllowanceModel item)
	{
		//GetDefEOSPARAMS		
		int tmpLevelID,tmpSECID,tmpClassID,tmpSalaryItem,tmpEnCashItem,tmpDeductionID,
		tmpDepID,tmpPosID,tmpMaritalID,tmpSexID,tmpReligion;
		String tmpEMPType,tmpNationality,tmpTransfer2NextYear="",tmpSalaryRow="",tmpEncashRow="",tmpCondRow="",tmpMinWorkMode="",tmpMode,
				tmpExcuse,tmpAbsenceType,tmpMHFlag,tmpDeductService,tmpCalculate,tmpDeductFrom;
		String tmpAdditions,tmpDeductions,tmpEOSReason,tmpContractType,tmpMaximumType,tmpSalaryEOS,tmpEOSIsDependable,
		tmpEOSCondRow="",tmpSalaryEOSRow="";
		
		double tmpMaximumValue,tmpPeriodFrom,tmpPeriodTo,tmpSalaryDays,tmpRate,tmpEOSBases;
		
		tmpLevelID=Integer.parseInt(defEOSPARMS.getLevelID());
		tmpDepID=Integer.parseInt(defEOSPARMS.getDepID());
		tmpPosID=Integer.parseInt(defEOSPARMS.getPosID());
		tmpSECID=Integer.parseInt(defEOSPARMS.getSecID());
		tmpClassID=Integer.parseInt(defEOSPARMS.getClassID());
		tmpEMPType=defEOSPARMS.getEmpType();
		tmpAdditions=defEOSPARMS.getAdditions();
		tmpDeductions=defEOSPARMS.getDeductions();
		tmpEOSReason=defEOSPARMS.getEosReason();
		tmpContractType=defEOSPARMS.getContractType();
		tmpMaximumValue=defEOSPARMS.getMaximumValue();
		tmpMaximumType=defEOSPARMS.getMaximumType();
		tmpSalaryItem=defEOSPARMS.getSalaryItem();
		tmpSalaryEOS=defEOSPARMS.getSalaryEOS();
		tmpPeriodFrom=defEOSPARMS.getPeriodFrom();
		tmpPeriodTo=defEOSPARMS.getPeriodTo();
		tmpSalaryDays=defEOSPARMS.getSalaryDays();
		tmpRate=defEOSPARMS.getDblRate();
		tmpEOSBases=defEOSPARMS.getEosBases();
		tmpEOSIsDependable=defEOSPARMS.getEosIsDependable();
		
		
		int CondRecNo=data.generateRecNo("HRCONDITIONS", "REC_NO", "EOS",0);
		int  SetupRecNo = data.getMaxID("HRSETUPS", "REC_NO");
		AllowanceModel obj=new AllowanceModel();
		obj.setCondNo(CondRecNo);
		obj.setRecNo(SetupRecNo);
		obj.setCompanyKey(item.getCompanyKey());
		obj.setEffectiveDate(eosEffDate);
		obj.setActivity("EOS");
		data.addHRSetup(obj);
		
		//String tmpEOSReason="",tmpEOSCondRow="",tmpSalaryRow="",tmpSalaryEOSRow="",tmpContractType="-1",tmpEOSIsDependable="";
		//int tmpDepID=-1,tmpPosID=-1,tmpSalaryItem=0,tmpPeriodFrom=0,tmpPeriodTo=0,tmpSalaryDays=0;
		 tmpMaximumValue = 2;
		 tmpMaximumType = "S";
		 tmpRate = 100;
		 tmpSalaryEOS="0"; 
		int tmpRangeTo=5;
		int tmpEOSRecNo = data.getMaxID("EOSSETUP", "REC_NO");
		for (int i = 1; i <= tmpRangeTo; i++) 
		{
			switch (i) 
			{
			case 1:
				tmpDepID=-1;
				tmpPosID = -1;
				tmpEOSReason = "T";
				tmpSalaryItem=-1;
				tmpEOSCondRow="Y";
				tmpSalaryRow="Y";
				tmpSalaryEOSRow="Y";
				tmpPeriodFrom=1;
				tmpPeriodTo=5;
				tmpSalaryDays=21;
				tmpEOSIsDependable = "Y";
				break;
				
			case 2:
				tmpDepID=0;
				tmpPosID = 0;
				tmpEOSReason = "T";
				tmpSalaryItem=0;
				tmpEOSCondRow="";
				tmpSalaryRow="N";
				tmpSalaryEOSRow="N";
				tmpPeriodFrom=5;
				tmpPeriodTo=99;
				tmpSalaryDays=30;
				tmpEOSIsDependable = "Y";
				break;
				
			case 3:
				tmpDepID=-1;
				tmpPosID = -1;
				tmpEOSReason = "R";
				tmpSalaryItem=-1;
				tmpEOSCondRow="Y";
				tmpSalaryRow="Y";
				tmpSalaryEOSRow="Y";
				tmpPeriodFrom=1;
				tmpPeriodTo=3;
				tmpSalaryDays=7;
				tmpEOSIsDependable = "Y";
				break;
				
			case 4:
				tmpDepID=0;
				tmpPosID = 0;
				tmpEOSReason = "R";
				tmpSalaryItem=0;
				tmpEOSCondRow="";
				tmpSalaryRow="N";
				tmpSalaryEOSRow="N";
				tmpPeriodFrom=3;
				tmpPeriodTo=5;
				tmpSalaryDays=14;
				tmpEOSIsDependable = "N";
				break;
				
			case 5:
				tmpDepID=0;
				tmpPosID = 0;
				tmpEOSReason = "R";
				tmpSalaryItem=0;
				tmpEOSCondRow="";
				tmpSalaryRow="N";
				tmpSalaryEOSRow="N";
				tmpPeriodFrom=5;
				tmpPeriodTo=99;
				tmpSalaryDays=30;
				tmpEOSIsDependable = "Y";
				break;
				
			default:
				break;
				
			}
			
			if(i==3)
			{
				tmpEOSRecNo = data.getMaxID("EOSSETUP", "REC_NO");
			}
			
			obj=new AllowanceModel();
			obj.setEffectiveDate(eosEffDate);
			obj.setRecNo(tmpEOSRecNo);
			obj.setLineNo(i);
			obj.setCompanyKey(item.getCompanyKey());
			obj.setLeaveId(tmpLevelID);			
			obj.setDepId(tmpDepID);
			obj.setPosId(tmpPosID);
			obj.setSecID(tmpSECID);
			obj.setClassId(tmpClassID);
			obj.setEmpType(tmpEMPType);
			obj.setAdditions(tmpAdditions);
			obj.setDeductions(tmpDeductions);
			obj.setEosReason(tmpEOSReason);
			obj.setContractType(tmpContractType);
			obj.setMaximumValue(tmpMaximumValue);
			obj.setMaximumType(tmpMaximumType);
			obj.setSalaryId(tmpSalaryItem);	
			obj.setSalaryEOS(tmpSalaryEOS);						
			obj.setCondRow(tmpEOSCondRow);
			obj.setSalaryRow(tmpSalaryRow);
			obj.setSalaryEosRow(tmpSalaryEOSRow);
			data.addEOSSetup(obj);
			
			
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "LEVEL_ID", tmpLevelID+"");
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "DEP_ID", ""+tmpDepID);
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "POS_ID", ""+tmpPosID);
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "SECTION_ID", tmpSECID+"");
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "CLASS_ID", tmpClassID+"");
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "EMP_TYPE", tmpEMPType);
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "EOS_REASON", tmpEOSReason);
			updateSetupConditions("EOS", item.getCompanyKey(), tmpEOSRecNo, "CONTRACT_TYPE", tmpContractType);
		
			obj=new AllowanceModel();
			obj.setEffectiveDate(eosEffDate);
			obj.setRecNo(tmpEOSRecNo);
			obj.setLineNo(i);
			obj.setCompanyKey(item.getCompanyKey());
			obj.setPeriodFrom(tmpPeriodFrom);
			obj.setPeriodTo(tmpPeriodTo);
			obj.setSalaryDays(tmpSalaryDays);
			obj.setEosBases(tmpEOSBases);
			obj.setDblRate(tmpRate);
			obj.setCondRow("Y");
			obj.setEosIsDependable(tmpEOSIsDependable);
			data.addEOSCalculation(obj);
			
		}
		
	}
	@Command
	@NotifyChange({"lstSalaries"})
	public void editSalaryCommand(@BindingParam("row") AllowanceModel row)
	{
		if(row.isEditSalary())
		{
		row.setRowDisable(false);
		}
		else
		{
			row.setRowDisable(true);
		}
	}
	
	
	@Command
	@NotifyChange({"lstSalaries"})
	public void changePensionCommand(@BindingParam("row") AllowanceModel row)
	{
		if(row.isEditPension())
		{
		row.setPensionEmployee(5);
		row.setPensionCompany(15);
		}
		else
		{
			row.setPensionEmployee(0);
			row.setPensionCompany(0);
		}
	}
	
	@Command
	@NotifyChange({"lstSalaries"})
	public void changeMinCommand(@BindingParam("row") AllowanceModel row)
	{
		if(row.getMaximum()>0)
		{
			if(row.getMinimum() > row.getMaximum())
			{
				Messagebox.show("Minimum Value must be less than the Maximum Value.","Default Setup", Messagebox.OK , Messagebox.INFORMATION);
				row.setMinimum(0);
			}
		}
	}
	@Command
	@NotifyChange({"lstSalaries"})
	public void changeMaxCommand(@BindingParam("row") AllowanceModel row)
	{
		if(row.getMaximum()>0)
		{
			if(row.getMinimum() > row.getMaximum())
			{
				Messagebox.show("Maximum Value must be grater than the Minimum Value.","Default Setup", Messagebox.OK , Messagebox.INFORMATION);
				row.setMaximum(0);
			}
		}
	}
	
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}

	@NotifyChange({"lstSalaries","salaryEffDate","leaveEffDate","eosEffDate","lstLeaves","lstActivity"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		getCompanyStartBussiness(selectedCompany.getCompKey());
		fillSalaryList();
		fillLeaveList();
		fillActivityList();
	}

	public List<AllowanceModel> getLstSalaries() {
		return lstSalaries;
	}

	public void setLstSalaries(List<AllowanceModel> lstSalaries) {
		this.lstSalaries = lstSalaries;
	}

	public Date getSalaryEffDate() {
		return salaryEffDate;
	}

	public void setSalaryEffDate(Date salaryEffDate) {
		this.salaryEffDate = salaryEffDate;
	}

	public Date getLeaveEffDate() {
		return leaveEffDate;
	}

	public void setLeaveEffDate(Date leaveEffDate) {
		this.leaveEffDate = leaveEffDate;
	}

	public Date getEosEffDate() {
		return eosEffDate;
	}

	public void setEosEffDate(Date eosEffDate) {
		this.eosEffDate = eosEffDate;
	}

	public List<AllowanceModel> getLstLeaves() {
		return lstLeaves;
	}

	public void setLstLeaves(List<AllowanceModel> lstLeaves) {
		this.lstLeaves = lstLeaves;
	}

	public List<AllowanceModel> getLstActivity() {
		return lstActivity;
	}

	public void setLstActivity(List<AllowanceModel> lstActivity) {
		this.lstActivity = lstActivity;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
}
