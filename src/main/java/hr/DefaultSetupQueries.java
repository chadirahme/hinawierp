package hr;

import java.text.SimpleDateFormat;

import model.AllowanceModel;

public class DefaultSetupQueries 
{

	/*
			SELECT * FROM HRCONDITIONS  Where Comp_Key =6
		select * from HRSETUPS  Where Comp_Key =6
		select * from COMPANYSALARY Where Comp_Key =6
		select * from SETUPCONDITIONS Where Comp_Key =6
		select * from COMPALLOWANCE Where Comp_Key =6
		
		
		delete FROM HRCONDITIONS  Where Comp_Key =6
		delete from HRSETUPS  Where Comp_Key =6
		delete from COMPANYSALARY Where Comp_Key =6
		delete from SETUPCONDITIONS Where Comp_Key =6
		delete from COMPALLOWANCE Where Comp_Key =6
	 */
	
	/*
	 * 
	 * select * from LEAVESETUP Where Comp_Key =6
		select * from LEAVECALCULATION Where Comp_Key =6
		select * from ABSENCESETUP Where Comp_Key =6
		select * from ABSENCECALCULATION Where Comp_Key =6
		select * from EOSSETUP Where Comp_Key =6
		select * from EOSCALCULATION Where Comp_Key =6
		select * from ADDITIONSETUP Where Comp_Key =6

		delete from LEAVESETUP Where Comp_Key =6
		delete from LEAVECALCULATION Where Comp_Key =6
		delete from ABSENCESETUP Where Comp_Key =6
		delete from ABSENCECALCULATION Where Comp_Key =6
		delete from EOSSETUP Where Comp_Key =6
		delete from EOSCALCULATION Where Comp_Key =6
		delete from ADDITIONSETUP Where Comp_Key =6

	 * 
	 * 
	 */
	
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String getHRAllowncesListValuesQuery()
	{
		query=new StringBuffer();
		 query.append("Select ID, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID from HRLISTVALUES");
		 query.append(" where FIELD_ID in (" + common.HREnum.HRList.HRListDEFAllowances.getValue() + " , " + common.HREnum.HRList.HRListOTHAllowances.getValue() +")" );
		 //query.append(" order by DESCRIPTION");
		return query.toString();		
	}
	
	
	public String getCompanyAllowanceQuery(int companyKey,int allowanceID)
	{
		
		query=new StringBuffer();
		query.append("Select * from COMPALLOWANCE  Where Comp_Key = "  + companyKey);
		query.append(" and Allowance_ID=" + allowanceID);
		query.append(" and EFF_DATE =(SELECT MAX(EFF_DATE) FROM COMPALLOWANCE COMPALLOWANCE1 WHERE");
		query.append(" COMPALLOWANCE1.REC_NO =COMPALLOWANCE.REC_NO AND COMPALLOWANCE1.COMP_KEY =" + companyKey);
		query.append(" AND COMPALLOWANCE1.Allowance_ID  =" + allowanceID + ")");
		return query.toString();
	}
	
	public String geSalaryFromHRConditionQuery(int companyKey,int allowanceID,boolean isSalary,String activity)
	{
		
		query=new StringBuffer();
		if(activity==null)
		{
		query.append("SELECT * FROM HRCONDITIONS  Where Comp_Key = "  + companyKey);
		query.append(" and Salary_Item=" + allowanceID);
		if(isSalary)
		query.append(" and ACTIVITY IN('BASICSAL','ALLOWANCE') ");
		}
		else
		{
			query.append("SELECT * FROM HRCONDITIONS  Where Comp_Key = "  + companyKey);
			query.append(" and ACTIVITY= '" + activity + "'");
		}
		return query.toString();
	}
	
	public String checkAllowncesAdvancedQuery(int companyKey,int salaryId)
	{
		
		query=new StringBuffer();
		if(salaryId==0)
		{
		query.append("Select Distinct EFF_DATE From COMPANYSALARY Where Comp_Key = "  + companyKey);			
		}
		else
		{
			query.append("Select Distinct EFF_DATE From COMPALLOWANCE Where Comp_Key = "  + companyKey);
			query.append(" and ALLOWANCE_ID= '" + salaryId + "'");
		}
		return query.toString();
	}
	
	public String getAllowncesAdvancedQuery(int companyKey,int salaryId)
	{
		
		query=new StringBuffer();
		if(salaryId==0)
		{
		query.append("Select * From COMPANYSALARY WHERE Comp_Key = "  + companyKey);			
		}
		else
		{
			query.append("Select * From COMPALLOWANCE Where Comp_Key = "  + companyKey);
			query.append(" and ALLOWANCE_ID= '" + salaryId + "'");
		}
		return query.toString();
	}
	
	public String checkLeavesAdvancedQuery(int companyKey,int LeaveID)
	{		
		query=new StringBuffer();	
		query.append("Select Distinct EFF_DATE From LEAVESETUP Where COND_ROW='Y' and  Comp_Key = "  + companyKey + " and LEAVE_KEY=" +LeaveID );					
		return query.toString();
	}
	public String getLeavesAdvancedQuery(int companyKey,int LeaveID)
	{		
		query=new StringBuffer();	
		query.append("Select * From LEAVESETUP Where COND_ROW='Y' and  Comp_Key = "  + companyKey + " and LEAVE_KEY=" +LeaveID );					
		return query.toString();
	}
	public String getLeavesCalcualtionByRecNoQuery(int recNo)
	{		
		query=new StringBuffer();	
		query.append("Select * From LeaveCalculation Where Rec_No = "  + recNo + " Order By Line_No Desc" );					
		return query.toString();
	}
	
	public String getHRLeaveListValuesQuery()
	{
		query=new StringBuffer();
		 query.append("Select ID, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID from HRLISTVALUES");
		 query.append(" where FIELD_ID =" + common.HREnum.HRList.HRListLeaveType.getValue() );
		 query.append(" Order by Priority_ID,DESCRIPTION");
		return query.toString();		
	}
	
	public String getHRAdditionSetupQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append("SELECT * FROM ADDITIONSETUP Where Comp_Key = "  + companyKey);		
		return query.toString();		
	}
	
	public String getMaxIDQuery(String tableName,String fieldName)
	{
		query=new StringBuffer();
		query.append(" select max("+ fieldName +") from "+ tableName);
		return query.toString();
	}
	public String generateRecNoQuery(String tableName,String fieldName,String activity,int salaryItem)
	{
		query=new StringBuffer();
		if(salaryItem==0)
		query.append(" select max("+ fieldName +") from "+ tableName + " where ACTIVITY='"  + activity + "'");
		else
		query.append(" select max("+ fieldName +") from "+ tableName + " where ACTIVITY='"  + activity + "' and SALARY_ITEM="+salaryItem);	
		return query.toString();
	}
	public String generateLineNoQuery(String tableName,String fieldName,int recNo)
	{
		query=new StringBuffer();		
		query.append(" select max("+ fieldName +") from "+ tableName + " where REC_NO='"  + recNo + "'");		
		return query.toString();
	}
	
	public String getHRColumnsQuery(String activity)
	{
		query=new StringBuffer();
		query.append(" SELECT * FROM HRCOLUMNS WHERE ACTIVITY='" + activity+"'");
		query.append(" Order by Column_ID");
		return query.toString();
	}
	
	public String addHRConditionsQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into HRCONDITIONS(REC_NO,ACTIVITY,COMP_KEY,SALARY_ITEM,SRL_NO,COLUMN_ID,ALLOW_MULTIPLE) values('%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),obj.getRecNo(),obj.getActivity(),obj.getCompanyKey(),obj.getSalaryId(),obj.getLineNo(),obj.getColumnId(),obj.getAllowMultiple() );		
	}
	
	public String deleteHRSetupQuery(int recNo)
	{
		query=new StringBuffer();
		query.append("Delete from HRSETUPS Where REC_NO="  + recNo);		
		return query.toString();		
	}
	public String addHRSetupQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into HRSETUPS(REC_NO,ACTIVITY,COMP_KEY,EFF_DATE,COND_NO) values('%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),obj.getRecNo(),obj.getActivity(),obj.getCompanyKey(),sdf.format(obj.getEffectiveDate()),obj.getCondNo() );		
	}
	
	public String addCompanySalaryQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into COMPANYSALARY(EFF_DATE,REC_NO,LINE_NO,ALLOWANCE_ID,COMP_KEY,LEVEL_ID,DEP_ID,POS_ID,SECTION_ID,CLASS_ID, EMP_TYPE, SEX_ID,MARITAL_ID ," +
				" NATIONALITY , RELIGION_ID," +
				"FIXED, MINIMUM , MAXIMUM,PENSION_EMP, PENSION_COMP, Annual_Increase,Upgrade_Years, COND_ROW )" +
				" values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',0,0,'Y')");				    
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getRecNo(),obj.getLineNo(),obj.getAllowanceId(),obj.getCompanyKey(),
				obj.getLeaveId(),obj.getDepId(),obj.getPosId(),obj.getSecID(),obj.getClassId(),obj.getEmpType(),obj.getSexId(),obj.getMaritalId(),
				obj.getNationality(),obj.getReligionId(),obj.getFixed(),
				obj.getMinimum(),obj.getMaximum(),obj.getPensionEmployee(),obj.getPensionCompany());	
	}
	
	public String deleteHRSetupConditionsQuery(AllowanceModel obj)
	{
		query=new StringBuffer();
		query.append("Delete from SETUPCONDITIONS Where Activity='%s' and Comp_Key='%s' and Rec_No='%s' and [Value] ='%s' and CONDITION='%s' ");		
		return query.toString().format(query.toString(),obj.getActivity(),obj.getCompanyKey(),obj.getRecNo(),obj.getCondValue(),obj.getCondition());		
	}
	
	public String addHRSetupConditionsQuery(AllowanceModel obj)
	{
		query=new StringBuffer();
		query.append("Insert into SETUPCONDITIONS(ACTIVITY,COMP_KEY,REC_NO,CONDITION,[VALUE]) Values('%s','%s','%s','%s','%s') ");		
		return query.toString().format(query.toString(),obj.getActivity(),obj.getCompanyKey(),obj.getRecNo(),obj.getCondition(),obj.getCondValue());		
	}
	
	public String addCompanyAllowancesQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into COMPALLOWANCE(EFF_DATE,REC_NO,LINE_NO,ALLOWANCE_ID,COMP_KEY,LEVEL_ID,DEP_ID,POS_ID,SECTION_ID,CLASS_ID, EMP_TYPE, SEX_ID,MARITAL_ID , NATIONALITY , RELIGION_ID," +
				"KIDS_NOS,KIDS_AGE_FROM,KIDS_AGE_TO,ALLOWANCE_TYPE,HOUSE_TYPE,ROOM_NOS,SALARY_NOS,FIXED,MINIMUM,MAXIMUM,BASIC_PER,PAY_PERIOD,PAY_MODE,DISTANSE,PENSION_EMP,PENSION_COMP,COND_ROW )" +
				" values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'," +
						"'%s','%s','%s','%s','%s','%s','%s','%s',  '%s','%s','%s',  '%s','%s','%s','%s','%s','Y')");				    
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getRecNo(),obj.getLineNo(),obj.getAllowanceId(),obj.getCompanyKey(),
				obj.getLeaveId(),obj.getDepId(),obj.getPosId(),obj.getSecID(),obj.getClassId(),obj.getEmpType(),obj.getSexId(),obj.getMaritalId(),
				obj.getNationality(),obj.getReligionId(),
				obj.getKidsNo(),obj.getKidsAgeFrom(),obj.getKidsAgeTo(),
				obj.getAllowanceType(),obj.getHouseType(),obj.getRoomsNo(),obj.getSalaryNos(),obj.getFixed(),   
				obj.getMinimum(),obj.getMaximum(),obj.getBasicPer(),  obj.getPayPeriod() ,obj.getPayMode()  ,obj.getDistance(),  obj.getPensionEmployee(),obj.getPensionCompany());	
	}
	
	public String addLeaveSetupQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into LEAVESETUP(LEAVE_KEY,EFF_DATE,REC_NO,LINE_NO,COMP_KEY,LEVEL_ID,DEP_ID,POS_ID,SECTION_ID,CLASS_ID, EMP_TYPE, SEX_ID,MARITAL_ID , RELIGION_ID, NATIONALITY ," +
				"MIN_WORK_PERIOD,MIN_WORK_FLAG,TRANS_NEXT_YEAR,SALARY_PER,SALARY_ITEM,SALARY_ROW,COND_ROW,ENCASHITEM,ENCASHROW )" +
				" values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),obj.getAllowanceId() ,sdf.format(obj.getEffectiveDate()),obj.getRecNo(),obj.getLineNo(),obj.getCompanyKey(),
			obj.getLeaveId(),obj.getDepId(),obj.getPosId(),obj.getSecID(),obj.getClassId(),obj.getEmpType(), obj.getSexId(),obj.getMaritalId(),obj.getReligionId(),obj.getNationality(),
				obj.getMinWorkPeriod(),obj.getMinWorkFlag(),obj.getTransfer2NextYear(),obj.getSalaryPER(), obj.getSalaryId(),obj.getSalaryRow(),obj.getCondRow(),obj.getEncashItem(),
				obj.getEncashRow());	
	}
	
	public String addLeaveCalculationQuery(AllowanceModel obj)
	{
		query=new StringBuffer();
		query.append("Insert into LEAVECALCULATION(EFF_DATE,COMP_KEY,CALC_ROW,REC_NO,LINE_NO,MONTH_FROM,MONTH_TO,DAYS_ALLOWED,DURATION_PERIOD,DURATION_MODE,CALCULATE) " +
				"Values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') ");		
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getCompanyKey(),obj.getSalaryRow(),obj.getRecNo(),obj.getLineNo(),
				obj.getMonthFrom(),obj.getMonthTo(),obj.getDaysAllowed(),obj.getDuration(),obj.getMode(),obj.getCalculate());		
	}
	
	public String addAdditionSetupQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into ADDITIONSETUP(REC_NO,LINE_NO,COMP_KEY,EFF_DATE,AD_FLAG,SALARY_ID,ACTIVITY_ROW) values('%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),obj.getRecNo(),obj.getLineNo(),obj.getCompanyKey(),sdf.format(obj.getEffectiveDate()),obj.getCondRow(),-1,"P" );		
	}
	
	public String addAbsenceSetupQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into ABSENCESETUP(EFF_DATE,REC_NO,LINE_NO,COMP_KEY,LEVEL_ID,DEP_ID,POS_ID,SECTION_ID,CLASS_ID,EMP_TYPE,SEX_ID,MARITAL_ID,RELIGION_ID,NATIONALITY,EXCUSE)" +
				" values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getRecNo(),obj.getLineNo(),obj.getCompanyKey(),
				obj.getLeaveId(),obj.getDepId(),obj.getPosId(),obj.getSecID(),obj.getClassId(),obj.getEmpType(),
				obj.getSexId(),obj.getMaritalId(),obj.getReligionId(),obj.getNationality(),
				obj.getCondRow());		
	}
	
	public String addAbsenceCalculationQuery(AllowanceModel obj)
	{
		query=new StringBuffer();
		query.append("Insert into ABSENCECALCULATION(EFF_DATE,COMP_KEY,TYPE_ROW,RATE_ROW,REC_NO,LINE_NO,ABSENCE_TYPE,MAX_ALLOWED,MH_FLAG,DEDUCTION_RATE,DEDUCTION_NOS,DEDUCT_SERVICE,CALCULATE,DEDUCT_FROM,DEDUCT_ID) " +
				"Values('%s','%s','Y','Y','%s','%s','D','10','%s','%s','%s','%s','%s','%s','%s') ");		
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getCompanyKey(),obj.getRecNo(),obj.getLineNo(),
				obj.getMhFlag(),obj.getDeductionRate() , obj.getDeductionNo(),obj.getDeductionService(),obj.getCalculate(),obj.getDeductionFrom(),obj.getDeductionId());		
	}
	
	public String addEOSSetupQuery(AllowanceModel obj)
	{
		query=new StringBuffer();			
		query.append("insert into EOSSETUP(EFF_DATE,REC_NO,LINE_NO,COMP_KEY,LEVEL_ID,DEP_ID,POS_ID,SECTION_ID,CLASS_ID,EMP_TYPE,ADDITIONS,DEDUCTONS,EOS_REASON," +
				"CONTRACT_TYPE,MAXIMUM_VALUE,MAXIMUM_TYPE,SALARY_ITEM,SALARY_EOS,COND_ROW,SALARY_ROW,SALARY_EOS_ROW)" +
				" values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'," +
				"'%s','%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getRecNo(),obj.getLineNo(),obj.getCompanyKey(),
				 obj.getLeaveId(),obj.getDepId(),obj.getPosId(),obj.getSecID(),obj.getClassId(),obj.getEmpType(),obj.getAdditions(),obj.getDeductions(),
				obj.getEosReason(),obj.getContractType(),obj.getMaximumValue(),obj.getMaximumType(),  obj.getSalaryId(),obj.getSalaryEOS(),  obj.getCondRow(),obj.getSalaryRow(),obj.getSalaryEosRow());		
	}
	
	public String addEOSCalculationQuery(AllowanceModel obj)
	{
		query=new StringBuffer();
		query.append("Insert into EOSCALCULATION(EFF_DATE,COMP_KEY,REC_NO,LINE_NO,PERIOD_FROM,PERIOD_TO,CALC_DAYS,BASE_DAYS,RATE,CALC_ROW,ISDEPENDABLE) " +
				"Values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') ");		
		return query.toString().format(query.toString(),sdf.format(obj.getEffectiveDate()),obj.getCompanyKey(),obj.getRecNo(),obj.getLineNo(),
				obj.getPeriodFrom(),obj.getPeriodTo(),obj.getSalaryDays(),obj.getEosBases(),obj.getDblRate(), obj.getCondRow(),obj.getEosIsDependable());		
	}
}
