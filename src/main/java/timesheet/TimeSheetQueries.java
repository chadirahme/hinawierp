package timesheet;

import home.QuotationAttachmentModel;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.EmployeeShiftModel;
import model.ProjectModel;
import model.TimeSheetDataModel;
import model.TimeSheetGridModel;
import model.TransferModel;

public class TimeSheetQueries 
{

	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public String GetCompanySetupQuery(int compkey)
	{
		query=new StringBuffer();		
		 query.append(" Select YEAR_PAYROLL,MONTH_PAYROLL,INCLUDEHOLIDAY_UNIT,TIMESHEET_OTCHANGE,TIMESHEET_TIMEAUTO,TIMESHEET_CHANGETIME,TIMESHEET_CALCEXTRAHOURS ");						
		 query.append(" FROM COMPSETUP Where ");		
		 query.append(" COMP_KEY=" + compkey);		 
		return query.toString();
	}
	
	public String getProjectLastModifiedQuery()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(LastModified) as LastModified from ProjectMast");		
		  return query.toString();
	}
	
	public String GetProjectQuery(int compkey,boolean isActive,int supervisorID)
	{
		query=new StringBuffer();
		 query.append(" Select Project_Key,Project_Name,Project_Code,Project_NameAR,start_date,end_date,ProjectMast.QBLISTID,DESCRIPTION as 'IsActive',Status_id,HRLISTVALUES.ARABIC as 'ArActive' ");						
		 query.append(" FROM ProjectMast  ");
		 query.append(" inner join HRLISTVALUES on ProjectMast.Status_id=HRLISTVALUES.ID");
		 if(compkey>0)
		 query.append(" where COMP_KEY=" + compkey);
		 else
		 query.append(" where COMP_KEY !=" + compkey);	 //for Hba getting projects
		 if(isActive)
		 {
			 query.append(" and DESCRIPTION='active' ");
		 }
		 if(supervisorID>0)
		 {
			 query.append(" and Project_Key in (  select distinct locationid from  EmployeeDetails where Active ='A' and ");
			 if(compkey>0) 
			 query.append(" CompKey=" + compkey);
			 else
			 query.append(" CompKey!=" + compkey);
			 query.append(" and Salary_Create='T' and SupervisorID=" + supervisorID  + " ) ");
			 query.append(" ");
		 }
		 
		 query.append(" order by Project_Name");
		return query.toString();
	}
	
	public String getProjectListBySupervisorID(int supervisorId)
	{
		query=new StringBuffer();
		query.append("select distinct ProjectMast.* from DAILYTS inner JOIN ProjectMast ON DAILYTS.project_key=ProjectMast.project_key where DAILYTS.supervisorid="+supervisorId+"");
		return query.toString();
	}
	
	public String getCustomerJobQuery()
	{
		query=new StringBuffer();
		query.append("Select Name,RecNo,ListID,ListType,SubLevel,FullName from QbLists Where ListType in('Customer') and IsActive='Y' order by ListType,FullName");
		return query.toString();
	}
	
	public String GetEmployeeInDailyTSQuery(Date fromDate,Date toDate,int compKey)
	{
		query=new StringBuffer();
		 query.append(" Select Distinct DAILYTS.LABOUR_KEY ");						
		 query.append(" FROM DAILYTS Where ");	
		 query.append(" COMP_KEY=" + compKey);		
		 query.append(" and DAILYTS.TS_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		 
		return query.toString();
	}
	
	public String GetEmployeeListInDailyTSQuery(String empkey)
	{
		query=new StringBuffer();
		/*
		 query.append(" Select * from EMPMAST ");						
		 query.append(" Where ");	
		 query.append(" EMP_KEY in (" + empkey + ")");		
		 query.append(" ORDER BY EMP_NO");		 
		 */
		 query.append(" Select EmployeeKey,EmployeeNo,LocationID, PositionId,Position,compkey,fullName,status,EmployeementDate from EmployeeDetails  ");						
		 query.append(" Where ");	
		 query.append(" employeekey in (" + empkey + ")");		
		 query.append(" ORDER BY fullname");	
		 return query.toString();
	}
	
	public String GetSalarySheetQuery(int salaryMonth,int salaryYear)
	{
		query=new StringBuffer();
		 query.append(" Select * from SALARYMASTTS  ");						
		 query.append(" Where ");	
		 query.append(" SALARY_MONTH =" + salaryMonth);
		 query.append(" and SALARY_YEAR =" + salaryYear);
		 query.append(" ORDER BY EMP_KEY");		 
		return query.toString();
	}
	public String deleteTmpTsDatesQuery()
	{
		query=new StringBuffer();		
		query.append(" Delete from tmpSELECTEDTSDAYS ");	
		return query.toString();
	}
	public String addtmpTSDatesQuery(Date tsDate,int dayNo)
	{
		query=new StringBuffer();		
		query.append("insert into tmpSELECTEDTSDAYS(TS_DATE,DAY_NO) values('%s','%s');");				    
		return query.toString().format(query.toString(),sdf.format(tsDate) ,dayNo);		
	}
	
	public String GetDataFromSetupQuery(int compkey,int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		 query.append(" SELECT SHIFTMAST.no_of_shifts, SQLSelectTSDays.TS_DATE, SQLSelectTSDays.EMP_KEY, SQLSelectTSDays.EMP_NO,");						
		 query.append(" SQLSelectTSDays.DAY_NO, SQLSelectTSDays.ENGLISH_FULL, SQLSelectTSDays.ARABIC_FULL,");
		 query.append(" '' AS REC_NO,'' AS LINE_NO, '' AS TOTAL_UNIT_NO, '' AS OT_NOS, '' AS TOTAL_NORMAL_HRS,");
		 query.append(" '' AS NORMAL_OTHRS, COMPSHIFT.OFFDAY, 'P' AS STATUS, SHIFTMAST.UNIT_KEY AS UNIT_ID,");
		 query.append(" COMPSHIFT.SHIFT_KEY, COMPSHIFT.REC_NO AS SHIFT_REC_NO, COMPSHIFT.FROM_TIME, COMPSHIFT.TO_TIME,");
		 query.append(" COMPSHIFT.HOURS AS UNIT_NOS, COMPSHIFT.HOURS AS NORMAL_UNITS, '' AS PROJECT_KEY, '' AS SERVICE_ID,");
		 query.append(" 'Y' AS CALC_FLAG, '' AS NOTES, COMPSHIFT.PAIDNORMAL, COMPSHIFT.PAIDIFWORK, SHIFTMAST.TIMING_FLAG,");
		 query.append(" SQLSelectTSDays.DEP_ID, SQLSelectTSDays.POS_ID, SQLSelectTSDays.SECTION_ID, SQLSelectTSDays.CLASS_ID,");
		 query.append(" SQLSelectTSDays.STATUS AS EMP_STATUS, SQLCompHolidays.DESCRIPTION AS HolidayDesc ");
		 query.append(" FROM");
		 //query.append(" (((SQLSelectTSDays LEFT OUTER JOIN SQLDailyShift    ON (SQLSelectTSDays.TS_DATE = SQLDailyShift.TS_DATE) AND");
		 query.append(" (((SQLSelectTSDays inner JOIN SQLDailyShift    ON (SQLSelectTSDays.TS_DATE = SQLDailyShift.TS_DATE) AND");
		 query.append(" (SQLSelectTSDays.EMP_KEY = SQLDailyShift.EMP_KEY))");
		 query.append(" LEFT OUTER JOIN COMPSHIFT        ON (SQLDailyShift.SHIFT_KEY = COMPSHIFT.SHIFT_KEY) AND");
		 query.append(" (SQLDailyShift.DAY_NO    = COMPSHIFT.DAY_NO) AND");
		 query.append(" (SQLDailyShift.COMP_KEY  = COMPSHIFT.COMP_KEY))");
		 query.append(" LEFT OUTER JOIN SHIFTMAST        ON (COMPSHIFT.SHIFT_KEY     = SHIFTMAST.SHIFT_KEY) AND");
		 query.append(" (COMPSHIFT.COMP_KEY      = SHIFTMAST.COMP_KEY))");
		 query.append(" LEFT OUTER JOIN SQLCompHolidays  ON (SQLDailyShift.SHIFT_KEY = SQLCompHolidays.SHIFT_KEY) AND");
		 query.append(" (SQLDailyShift.COMP_KEY  = SQLCompHolidays.COMP_KEY) AND");
		 query.append(" (SQLDailyShift.TS_DATE   = SQLCompHolidays.TS_DATE) Where");
		 query.append(" SQLSelectTSDays.COMP_KEY=" + compkey);
		 query.append(" AND SQLSelectTSDays.EMP_KEY  = " + empKey);
		 query.append(" AND SQLSelectTSDays.TS_DATE between '" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "' ");
		 query.append(" Order by SQLSelectTSDays.EMP_NO,SQLSelectTSDays.EMP_KEY,SQLSelectTSDays.TS_DATE,COMPSHIFT.SHIFT_KEY,COMPSHIFT.REC_NO");
		 query.append(" ");		 		
		return query.toString();
	}
	
	public String GetDataFromTimeSheetQuery(int compkey,int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		query.append(" SELECT SHIFTMAST.no_of_shifts, DAILYTS.LABOUR_KEY AS EMP_KEY,DAILYTS.TS_DATE,DAILYTS.REC_NO , DAILYTIMING.LINE_NO,");		 		
		query.append(" DAILYTS.UNIT_NOS   As TOTAL_UNIT_NO, DAILYTS.OT_NOS,");
		 query.append(" DAILYTS.NORMAL_HRS As TOTAL_NORMAL_HRS, DAILYTS.NORMAL_OTHRS,");	
		 query.append(" DAILYTS.HOLIDAY AS OFFDAY   , DAILYTIMING.STATUS, DAILYTIMING.UNIT_ID, DAILYTIMING.SHIFT_KEY,");	
		 query.append(" DAILYTIMING.SHIFT_REC_NO, DAILYTIMING.FROM_TIME, DAILYTIMING.TO_TIME, DAILYTIMING.UNIT_NOS,");	
		 query.append(" DAILYTIMING.NORMAL_UNITS, DAILYTIMING.PROJECT_KEY, DAILYTIMING.SERVICE_ID, DAILYTIMING.CALC_FLAG,");	
		 query.append(" DAILYTIMING.NOTES, COMPSHIFT.PAIDNORMAL, COMPSHIFT.PAIDIFWORK, SHIFTMAST.TIMING_FLAG,DAILYTIMING.LEAVE_FLAG,");	
		 query.append(" SQLCompHolidays.DESCRIPTION AS HolidayDesc,UNITMAST.UNIT_NAME,TIMING, isnull(TS_STATUS,0) as TS_STATUS,CustomerRefKey ,DAILYTS.TomorrowsPlan,DAILYTS.AttachmentPath ");	
		 query.append("  FROM DAILYTS  LEFT OUTER JOIN DAILYTIMING ON DAILYTS.REC_NO = DAILYTIMING.REC_NO ");	
		 query.append(" LEFT OUTER JOIN COMPSHIFT ON DAILYTIMING.SHIFT_REC_NO = COMPSHIFT.REC_NO ");	
		 query.append(" LEFT OUTER JOIN SHIFTMAST ON COMPSHIFT.SHIFT_KEY      = SHIFTMAST.SHIFT_KEY");	
		 query.append(" LEFT OUTER JOIN SQLCompHolidays ON (DAILYTS.SHIFT_KEY = SQLCompHolidays.SHIFT_KEY) AND ");	
		 query.append(" (DAILYTS.TS_DATE   = SQLCompHolidays.TS_DATE)");	
		 query.append(" LEFT OUTER join UNITMAST on UNITMAST.UNIT_KEY=DAILYTIMING.UNIT_ID");
		 query.append(" where");	
		 query.append(" (SQLCompHolidays.DESCRIPTION is null OR  SQLCompHolidays.COMP_KEY = " + compkey + ")");	
		 query.append(" AND DAILYTS.LABOUR_KEY=" + empKey);	
		 query.append(" AND DAILYTS.TS_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");			
		 query.append(" AND DAILYTIMING.LABOUR_KEY  =" + empKey);		
		 query.append(" Order by DAILYTS.TS_DATE,DAILYTIMING.LINE_NO");		
		 query.append(" ");		
		 query.append(" ");		
		 return query.toString();
	}
	
	public String GetOverTimeSheetQuery(int empKey,Date fromDate,Date toDate)
	{
		 query=new StringBuffer();
		 query.append(" Select DailyOT.REC_NO,DailyOT.LINE_NO,DailyOT.LABOUR_KEY AS EMP_KEY,DailyOT.TS_DATE,DailyOT.CALCULATION,");		
		 query.append(" DailyOT.UNITS,DailyOT.AMOUNT,DailyOT.MAX_UNITS,DailyOT.WITHOUT_HRS,DailyOT.SHIFT_KEY,");	
		 query.append(" DailyOT.SHIFT_REC_NO,DailyOT.CALC_FLAG,DailyOT.PROJECT_KEY");		
		 query.append(" FROM DailyOT ");	
		 query.append(" Where DailyOT.LABOUR_KEY  =" + empKey);		
		 query.append(" AND DailyOT.TS_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		 query.append(" Order by DAILYOT.TS_DATE,DAILYOT.LINE_NO,DAILYOT.CALCULATION");		
		 query.append(" ");	
		 return query.toString();
	}
	
	public String getLastTimeSheetCreatedQuery1(int compKey)
	{
		query=new StringBuffer();
		query.append(" select * from  DAILYTS");	
		query.append(" where COMP_KEY=" + compKey);	
		query.append(" and TS_DATE in (select max(TS_DATE) from DAILYTS where COMP_KEY=" +compKey +")");	
		return query.toString();		
	}
	
	public String getLastTimeSheetCreatedQuery(int compKey)
	{
		query=new StringBuffer();
		query.append(" select distinct LABOUR_KEY,Convert(datetime,convert(varchar(10),year(max(TS_DATE)))+'-'+convert(varchar(10),month(max(TS_DATE)))+'-'+convert(varchar(10),1))as TS_DATE");	
		query.append(" ,isnull(TS_STATUS,0) as TS_STATUS  ");
		query.append(" from  DAILYTS  ");
		query.append(" where COMP_KEY=" + compKey);	
		query.append(" and month(TS_DATE)=(select month(max(TS_DATE)) from DAILYTS where COMP_KEY=" +compKey +")");	
		query.append(" and year(ts_date)=(select year(max(TS_DATE)) from DAILYTS where COMP_KEY="+ compKey+")");
		query.append(" group by TS_DATE,LABOUR_KEY,TS_STATUS");
		return query.toString();		
	}
	
	public String getLastTimeSheetCreatedByUserQuery(int compKey,int supervisorID)
	{
		query=new StringBuffer();
		query.append(" select distinct LABOUR_KEY,Convert(datetime,convert(varchar(10),year(max(TS_DATE)))+'-'+convert(varchar(10),month(max(TS_DATE)))+'-'+convert(varchar(10),1))as TS_DATE");	
		query.append(" ,isnull(TS_STATUS,0) as TS_STATUS,SUPERVISORID,english_full  ");
		query.append(" from  DAILYTS left join empmast on DAILYTS.SUPERVISORID=empmast.emp_key ");
		query.append(" where DAILYTS.COMP_KEY=" + compKey);	
		query.append(" and DAILYTS.SUPERVISORID=" + supervisorID);	
		query.append(" and month(TS_DATE)=(select month(max(TS_DATE)) from DAILYTS where COMP_KEY=" +compKey +" and SUPERVISORID=" + supervisorID+ ")");	
		query.append(" and year(ts_date)=(select year(max(TS_DATE)) from DAILYTS where COMP_KEY="+ compKey +  " and SUPERVISORID=" + supervisorID +")");
		query.append(" group by TS_DATE,LABOUR_KEY,TS_STATUS,SUPERVISORID,english_full");
		return query.toString();		
	}
	
	//Filter Employee
	public String searchEmployeeQuery(int compKey,String type,int supervisorId)
	{
		query=new StringBuffer();
		query.append(" select * from  EmployeeDetails");	
		query.append(" where Active ='A' and CompKey=" + compKey);
		if(type.equals("T"))
		query.append(" and Salary_Create='T'");	
		if(supervisorId>0)
			query.append(" and (SupervisorID=" + supervisorId+" or ( SuperSupervisorId ="+supervisorId+" or(employeeKey="+supervisorId+" and CompKey="+compKey+")))");	
		query.append(" order by REPLICATE('0',2-LEN(employeeNo)) + employeeNo");	
		return query.toString();		
	}
	
	public String getEmployeeShiftQuery(int empKey,Date fromDate,Date toDate,int compKey)
	{
		query=new StringBuffer();
		query.append(" SELECT POS_ID, FROM_DATE, TO_DATE, EMPSHIFT.SHIFT_KEY ,TIMING_FLAG,Unit_Key ");	
		query.append(" from EMPSHIFT INNER JOIN  SHIFTMAST ON SHIFTMAST.SHIFT_KEY = EMPSHIFT.SHIFT_KEY AND SHIFTMAST.COMP_KEY = EMPSHIFT.COMP_KEY");
		query.append(" where EMP_KEY=" + empKey + " and EMPSHIFT.COMP_KEY=" + compKey);
		//query.append(" and (TO_DATE >= '" +sdf.format(toDate) + "')");
		//query.append(" and ((FROM_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"') OR (TO_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"'))");
		//query.append(" AND (FROM_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		query.append(" and '" +sdf.format(fromDate) + "' between FROM_DATE and TO_DATE ");
		return query.toString();		
	}
	
	public String checkEmployeeSalarySheetQuery(int empKey,int month,int year)
	{
		query=new StringBuffer();
		query.append(" SELECT * from SALARYMASTTS where EMP_KEY= "+ empKey);
		query.append(" and SALARY_MONTH=" + month + " and SALARY_YEAR=" + year);
		return query.toString();	
	}
	
	public String checkEmployeeSalarySheetApproved(int empKey,int fromMonth,int year)
	{
		query=new StringBuffer();
		query.append(" SELECT * from SALARYMASTTS where salary_status='A' and EMP_KEY= "+ empKey);
		query.append(" and SALARY_MONTH=" + fromMonth + " and SALARY_YEAR=" + year);
		
		return query.toString();	
	}
	
	public String checkForLoanEmployeeSalarySheetApprovedQuery(int empKey,int fromMonth,int toMonth,int fromYear,int toYear)
	{
		query=new StringBuffer();
		query.append(" SELECT * from SALARYMASTTS where salary_status='A' and EMP_KEY= "+ empKey);
		if(fromYear==toYear)
		{
		query.append(" and SALARY_MONTH between " + fromMonth + " and " + toMonth + " and SALARY_YEAR between " + fromYear + " and " + toYear);
		}
		else
		{
		 query.append(" and (  (SALARY_MONTH between " + fromMonth + " and 12 ) or (SALARY_MONTH between 1 and " +toMonth  +") )"  + " and SALARY_YEAR between " + fromYear + " and " + toYear);	
		}
		return query.toString();	
	}
	
	public String checkIfTimeSheetApprovedQuery(int empKey,int month,int year)
	{
		query=new StringBuffer();
		query.append(" select count(*) as cnt from DAILYTS where labour_key= "+ empKey);
		query.append(" and ts_Status=1 and datepart(month,TS_DATE)=" + month + " and datepart(year,TS_DATE)=" + year);
		return query.toString();	
	}
	
	
	public String checkIfOnlineLeaveTakenQuery(int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		query.append(" SELECT * ");	
		query.append(" from LEAVEREQUEST LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.LEAVE_Type_ID = HRLISTVALUES.ID ");
		query.append(" where (STATUS='A' or STATUS='C')  and EMP_KEY=" + empKey);
		//query.append(" and (TO_DATE >= '" +sdf.format(toDate) + "')");
		
		query.append(" and ( ( '" +sdf.format(fromDate) + "' between LEAVE_START_DATE and LEAVE_END_DATE" );
		query.append("  or '" +sdf.format(toDate) +  "' between LEAVE_START_DATE and LEAVE_END_DATE )");
		query.append("  or ( LEAVE_START_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"')");
		query.append(" )");
		//query.append(" and ((LEAVE_START_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"') OR (LEAVE_END_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"'))");
		//query.append(" AND (FROM_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		return query.toString();		
	} 
	
	public String checkIfDesktopLeaveTakenQuery(int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		query.append(" SELECT * ");	
		query.append(" from EMPLEAVE LEFT JOIN HRLISTVALUES ON EMPLEAVE.LEAVE_ID=HRLISTVALUES.ID ");
		query.append(" where (STATUS='A' or STATUS='C')  and EMP_KEY=" + empKey);
		//query.append(" and (TO_DATE >= '" +sdf.format(toDate) + "')");
		query.append(" and ( ( '" +sdf.format(fromDate) + "' between From_DATE and To_date" );
		query.append("  or '" +sdf.format(toDate) +  "' between From_DATE and To_date )");
		query.append("  or ( From_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"')");
		query.append("    OR (RETURN_STATUS='False' AND TO_DATE<='" + sdf.format(toDate)  +"') )");
		query.append("  and payment in ('P','N','W') and EnCash_Status <> 'Y' ");
		//query.append(" and ((From_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"') OR (To_DATE Between '" +sdf.format(fromDate)+"' And '" + sdf.format(toDate)  +"'))");
		//query.append(" AND (FROM_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		return query.toString();		
	} 
	
	public String getMaxOTQuery(int compKey)
	{
		query=new StringBuffer();
		query.append(" SELECT DISTINCT TSSETUP.REC_NO , OTCALCULATION.OT_NO,     OTCALCULATION.DAY_TYPE, ");	
		query.append(" OTCALCULATION.OT_RATE     , OTCALCULATION.HOURS,     TSSETUP.UNIT_ID,");
		query.append(" OTCALCULATION.AUTO_FILL   , OTCALCULATION.CALCULATE, POS_ID");
		query.append(" ");
		query.append(" FROM TSSETUP INNER JOIN OTCALCULATION ON TSSETUP.REC_NO = OTCALCULATION.REC_NO ");
		query.append(" where TSSETUP.comp_key=" + compKey);	
		// query.append(" AND TSSETUP.POS_ID=" + Shift_Key);		
		return query.toString();		
	}
	
	public String getCompanyShiftQuery(int compKey,int Shift_Key)
	{
		query=new StringBuffer();
		query.append(" Select * from COMPSHIFT ");	
		query.append(" where COMP_KEY=" + compKey);	
		 query.append(" AND Shift_Key=" + Shift_Key);		
		return query.toString();		
	}
	
	public String getMinMaxTimeShiftQuery(int Shift_Key)
	{
		query=new StringBuffer();
		query.append(" select min(DEF_FROMTIME) as 'MinDate' ,max(DEF_TOTIME) as 'MaxDate' from SHIFTDET ");			
		query.append(" where Shift_Key=" + Shift_Key);		
		return query.toString();		
	}
	
	
	//Begin Save Time Sheet
	public String deleteDAILYTSByRecNoQuery(int recNo)
	{
		query=new StringBuffer();
		query.append(" Delete from DAILYTS ");	
		query.append(" where REC_NO=" + recNo);				
		return query.toString();		
	}
	public String deleteDAILYTIMINGByRecNoQuery(int recNo)
	{
		query=new StringBuffer();
		query.append(" Delete from DAILYTIMING ");	
		query.append(" where REC_NO=" + recNo);				
		return query.toString();		
	}
	public String deleteDAILYOTByRecNoQuery(int recNo)
	{
		query=new StringBuffer();
		query.append(" Delete from DAILYOT ");	
		query.append(" where REC_NO=" + recNo);				
		return query.toString();		
	}
	
	
	public String deleteDAILYTSQuery(int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		query.append(" Delete from DAILYTS ");	
		query.append(" where LABOUR_KEY=" + empKey);	
		query.append(" AND TS_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		return query.toString();		
	}
	
	public String deleteDAILYTIMINGQuery(int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		query.append(" Delete from DAILYTIMING ");	
		query.append(" where LABOUR_KEY=" + empKey);	
		query.append(" AND TS_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		return query.toString();		
	}
	
	public String deleteDAILYOTQuery(int empKey,Date fromDate,Date toDate)
	{
		query=new StringBuffer();
		query.append(" Delete from DAILYOT ");	
		query.append(" where LABOUR_KEY=" + empKey);	
		query.append(" AND TS_DATE Between'" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'");		
		return query.toString();		
	}
	
	public String getNextSequenceNoQuery()
	{
		query=new StringBuffer();
		query.append(" GetNextSequenceNo 'DAILYTS' ");
		return query.toString();		
	}
	public String getMaxIDQuery(String tableName,String fieldName)
	{
		query=new StringBuffer();
		query.append(" select max("+ fieldName +") from "+ tableName);
		return query.toString();
	}
	
	public String InsertDAILYTSQuery(TimeSheetGridModel obj)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String tomorrowplan=obj.getTomorrowPlan()==null?"":obj.getTomorrowPlan().replace("'", "`");
		query=new StringBuffer();		
		//obj.setUnits((int)obj.getNormalHours()); // used before to save OT >> obj.getOtNos()
		/*query.append(" InsertDAILYTS " + obj.getRecNo() + " , " +obj.getEmpKey()+ " ,'" + sdf.format(obj.getTsDate()) + "', '" + obj.getStatus() + "' , ");
		query.append(" " + obj.getUnitKey() + " , " + obj.getTotals() + " , " + obj.getShiftkey() + " , " + obj.getProjectId() + " , " + obj.getServiceId());
		query.append(" , " + obj.getTotalNormalHours() + " , " + obj.getNormalOTHours() +  " , " + obj.getTotalOTUnits() + " , '" +obj.getHolidayDesc() + "' , '" + obj.getCalculate() + "' , ");
		query.append(" " + obj.getCompKey() + " , " + obj.getSupervisorId() + ", '" + tomorrowplan + "'" );
		query.append(" ");
		return query.toString();*/	
		String status="",holiday="",calculate="";
		if(obj.getStatus().length()>0)
		{
			status=obj.getStatus().substring(0,1);
		}
		if(obj.getHolidayDesc().length()>0)
		{
			holiday=obj.getHolidayDesc().substring(0,1);
		}
		if(obj.getCalculate().length()>0)
		{
			calculate=obj.getCalculate().substring(0,1);
		}
		
		query.append("insert into DAILYTS(REC_NO,LABOUR_KEY,TS_DATE,STATUS,UNIT_ID,UNIT_NOS,SHIFT_KEY,PROJECT_KEY,SERVICE_ID,NORMAL_HRS,NORMAL_OTHRS,OT_NOS,HOLIDAY,CALC_FLAG,COMP_KEY,SUPERVISORID,TomorrowsPlan,AttachmentPath) " +
				"values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s' )");				    
		return query.toString().format(query.toString(),obj.getRecNo() ,obj.getEmpKey(),sdf.format(obj.getTsDate()),status,obj.getUnitKey(),
				 obj.getTotals(),obj.getShiftkey(),obj.getProjectId() ,obj.getServiceId(),obj.getTotalNormalHours(),obj.getNormalOTHours() ,obj.getTotalOTUnits(),holiday,
				 calculate,obj.getCompKey(), obj.getSupervisorId(),tomorrowplan,obj.getAttachPath());	
		
	}
	
	
	
	public String InsertDailyTimingQuery(TimeSheetGridModel obj)
	{
		String tsFromTime="NULL";
		String tsToTime="NULL";
		String notes=""; 
		if(obj.getNotes()!=null)
		{
			notes=obj.getNotes().replaceAll("'","`");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		if(obj.getTsFromTime()!=null)
			tsFromTime="'"+tdf.format(obj.getTsFromTime())+"'";
		if(obj.getTstoTime()!=null)
			tsToTime="'" + tdf.format(obj.getTstoTime()) + "'";
		
		query=new StringBuffer();		
		/*query.append(" InsertDailyTiming " + obj.getLineNo() + " , " +obj.getEmpKey()+ " ,'" + sdf.format(obj.getTsDate()) + "', '" + obj.getStatus() + "' , ");
		query.append(" " + obj.getUnitKey() + " , " + obj.getRecNo() + " , " + obj.getShiftkey() + " , " + obj.getShiftRecNo() + " , " + tsFromTime+" ");
		query.append(" , " +tsToTime + " , " + obj.getUnits() + " , "  + obj.getNormalHours() + " , " + obj.getProjectId()+" , "  + obj.getServiceId());
		query.append(" , '" + obj.getCalculate() + "' , '" + notes+ "' , '" + obj.getLeaveFlag()+"' ,'" +obj.getCustomerJobRefKey() +"'" );		
		query.append(" ");
		return query.toString();*/	
		
		String status="",leaveflag="",calculate="";
		if(obj.getStatus().length()>0)
		{
			status=obj.getStatus().substring(0,1);
		}
		if(obj.getLeaveFlag()!=null && obj.getLeaveFlag().length()>0)
		{
			leaveflag=obj.getLeaveFlag().substring(0,1);
		}
		if(obj.getCalculate().length()>0)
		{
			calculate=obj.getCalculate().substring(0,1);
		}
		
		query.append("insert into DAILYTIMING(LINE_NO,LABOUR_KEY,TS_DATE,STATUS,UNIT_ID,REC_NO,SHIFT_KEY,SHIFT_REC_NO,FROM_TIME,TO_TIME,UNIT_NOS,NORMAL_UNITS,PROJECT_KEY,SERVICE_ID,CALC_FLAG,NOTES,LEAVE_FLAG,CustomerRefKey) " +
				"values('%s','%s','%s','%s','%s','%s','%s','%s',%s,%s,'%s','%s','%s','%s','%s','%s','%s','%s' )");				    
		return query.toString().format(query.toString(),obj.getLineNo() ,obj.getEmpKey(),sdf.format(obj.getTsDate()),status,obj.getUnitKey(),obj.getRecNo() ,
				 obj.getShiftkey(),obj.getShiftRecNo(),tsFromTime,tsToTime,obj.getUnits(),obj.getNormalHours(),obj.getProjectId(),obj.getServiceId(),calculate,
				 notes, leaveflag,obj.getCustomerJobRefKey() );	
		
	}
	
	public String InsertDAILYOTQuery(TimeSheetGridModel obj)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		query=new StringBuffer();		
		/*query.append(" InsertDAILYOT " + obj.getLineNo() + " , " + obj.getRecNo() + " , " +obj.getEmpKey()+ " ,'" + sdf.format(obj.getTsDate()) + "' ,");
		query.append(" " + obj.getOtCalculation() + " , " + obj.getOtUnits() + " , " + obj.getOtAmount() + " , " + obj.getShiftkey() + " , " + obj.getShiftRecNo());
		query.append(" , " + obj.getMaxOTAmount() + " , " + "0" + " , '" + obj.getCalculate() + "' , " + obj.getProjectId() + " , " + obj.getCustomerJobRefKey() );
		return query.toString();	*/	
		String calculate="";
		if(obj.getCalculate().length()>0)
		{
			calculate=obj.getCalculate().substring(0,1);
		}
		query.append("insert into DAILYOT(LINE_NO,REC_NO,LABOUR_KEY,TS_DATE,CALCULATION,UNITS,AMOUNT,SHIFT_KEY,SHIFT_REC_NO,MAX_UNITS,WITHOUT_HRS,CALC_FLAG,PROJECT_KEY,CustomerRefKey) " +
				"values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s' )");				    
		return query.toString().format(query.toString(),obj.getLineNo(), obj.getRecNo() ,obj.getEmpKey(),sdf.format(obj.getTsDate()),obj.getOtCalculation(),
				obj.getOtUnits() , obj.getOtAmount(),obj.getShiftkey(),obj.getShiftRecNo(),obj.getMaxOTAmount(),"0" ,calculate,obj.getProjectId(),obj.getCustomerJobRefKey() );	
	
	}
	public String addUserActivityQuery(int FORM_ID,int ACTIVITY_ID,int EMP_KEY,int COMP_KEY,String DESCRIPTION,int WebUserID)
	{
		query=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		Calendar c = Calendar.getInstance();	
		String today=sdf.format(c.getTime());	
		String totime=tdf.format(c.getTime());
		query.append("insert into USERACTIVITY(FORM_ID,ACTIVITY_ID,CREATE_DATE,CREATE_TIME,EMP_KEY,COMP_KEY,DESCRIPTION,WebUserID) values('%s','%s','%s','%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(), FORM_ID,ACTIVITY_ID,today,totime ,EMP_KEY,COMP_KEY,DESCRIPTION,WebUserID );		
	}
	
	//Over Time
	public String getOTCALCULATIONQuery(int compKey)
	{
		query=new StringBuffer();
		query.append("  Select Distinct OTCALCULATION.REC_NO , OTCALCULATION.OT_NO, OTCALCULATION.DAY_TYPE ,OTCALCULATION.OT_RATE , OTCALCULATION.HOURS, ");
		query.append("  OTCALCULATION.AUTO_FILL , OTCALCULATION.AUTO_FILL, OTCALCULATION.CALCULATE AS OTCALC ,TSSETUP.CALCULATE AS OTFLAG ,POS_ID,Salary_Item ,CALC_HRS ");
		query.append("  from OTCALCULATION Inner join TSSETUP on TSSETUP.Rec_No = OTCALCULATION.Rec_No Where ");
		query.append("  OTCALCULATION.COMP_KEY =" + compKey);
		//query.append("  and TSSETUP.POS_ID in (2 ,-1) And ");		
		query.append("  Order by OTCALCULATION.Calculate");
		query.append("  ");
		query.append("  ");
		return query.toString();		
	}
	
	public String getOTEmployeeSalaryQuery(int empKey,Date tsDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		query=new StringBuffer();
		query.append("  Select Amount from EMPSALARY EMPSALARY1 Where ");		
		query.append("  EMPSALARY1.PAY_MODE = 'M' And ");
		query.append("  EMPSALARY1.PAY_PERIOD    = 1   And EMPSALARY1.ALLOWANCE_TYPE =0   And");
		query.append("  EMPSALARY1.EFF_DATE     <= '" + sdf.format(tsDate)+ "'");
		query.append(" and EMPSALARY1.EFF_DATE     >= (Select Max(EFF_DATE) from EMPSALARY Where");
		query.append("  EMPSALARY.PAY_MODE      = 'M' And  EMPSALARY.PAY_PERIOD    = 1   And EMPSALARY.ALLOWANCE_TYPE =0   And");
		query.append("  EMPSALARY.EMP_KEY       = " + empKey);
		query.append("  and EMPSALARY.SALARY_ID     = EMPSALARY1.SALARY_ID And ");
		query.append("  EMPSALARY.EFF_DATE  <= ' " +sdf.format(tsDate) + "' And EMPSALARY1.EMP_KEY=" +empKey + ")");
		return query.toString();		
	}
	
	
	
	
	//Transfer
	public String getNextTransferSequenceNoQuery()
	{
		query=new StringBuffer();
		query.append(" SELECT MAX(FORM_NO) as 'FormNo' from LOCATIONTRANSFER ");
		return query.toString();		
	}
	
	public String getSiteInChargeQuery(int projectKey)
	{
		query=new StringBuffer();
		query.append(" SELECT EMPMAST.ENGLISH_FULL, EMPMAST.ARABIC_FULL,PROJECTMAST.CONTACT_PERSON ");
		query.append(" FROM (PROJECTMAST INNER JOIN EMPMAST ON PROJECTMAST.CONTACT_PERSON = EMPMAST.EMP_KEY)");
		query.append(" where PROJECT_KEY=" + projectKey);
		return query.toString();		
	}
	
	public String getSiteContactQuery(int empKey)
	{
		query=new StringBuffer();
		query.append(" SELECT DETAILS FROM EMPCONTACT  ");		
		query.append(" where CONTACT_ID=621 and EMP_KEY=" + empKey);
		return query.toString();		
	}
	
	public String GetEmployeeTransferQuery(String empkey,int compKey)
	{
		query=new StringBuffer();
		 query.append(" Select * from EMPMAST ");						
		 query.append(" Where EMPMAST.COMP_KEY=" + compKey);	
		 query.append(" and EMP_KEY in (" + empkey + ")");		
		 query.append(" Order by EMPMAST.ENGLISH_FULL");		 
		return query.toString();
	}
	
	public String GetEmployeeCurrentLocationQuery(String empkey)
	{
		 query=new StringBuffer();
		 query.append(" SELECT LOCATIONTRANSFER.PROJECT_KEY,EFF_DATE ,Project_Name,EMP_KEY  ");	
		 query.append(" FROM LOCATIONTRANSFER");
		 query.append(" inner join ProjectMast on  LOCATIONTRANSFER.Project_Key=ProjectMast.Project_Key");		 
		 query.append(" Where EMP_KEY in (" + empkey + ")");
		 query.append(" and EFF_DATE =(SELECT MAX(EFF_DATE) FROM LOCATIONTRANSFER LOCATIONTRANSFER1 WHERE");		
		 query.append(" LOCATIONTRANSFER.EMP_KEY =LOCATIONTRANSFER1.EMP_KEY )");		 
		return query.toString();
	}
	
	public String checkIfTransferExistsQuery(Date transferDate)
	{
		 query=new StringBuffer();
		 query.append(" SELECT * FROM LOCATIONTRANSFER ");				
		// query.append(" Where EMP_KEY =" + empKey);
		 query.append(" where 1=1 ");
		 query.append(" AND ((EFF_DATE Between'" + sdf.format(transferDate) + "' and '" + sdf.format(transferDate) + "') ");		
		 query.append(" OR (TO_DATE    Between '" + sdf.format(transferDate) + "' and '" + sdf.format(transferDate) + "') ");		
		 query.append(" OR ('" + sdf.format(transferDate) + "' BETWEEN EFF_DATE AND TO_DATE )");
		 query.append(" OR ('" + sdf.format(transferDate) + "' BETWEEN EFF_DATE AND TO_DATE ) )");	
		 return query.toString();
	}
	
	public String getNextTransferRecNoQuery()
	{
		query=new StringBuffer();
		query.append(" SELECT MAX(REC_NO) as 'REC_NO' from LOCATIONTRANSFER ");
		return query.toString();		
	}
	
	public String insertNewTransferQuery(TransferModel obj)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		query=new StringBuffer();
		query.append(" INSERT INTO LOCATIONTRANSFER(");
		query.append(" REC_NO,CREATE_DATE,FORM_NO,COMP_KEY,EMP_KEY,PROJECT_KEY,EFF_DATE,FORM_NAME,NOTES,STATUS,CUR_PROJECT )");
		query.append(" VALUES(" + obj.getRecNo() + " , '" + sdf.format(obj.getCreateDate()) + "' , " + obj.getFormNo() + " , " + obj.getCompKey() + " , " + obj.getEmpKey() + " , ");
		query.append(" " + obj.getProjectKey() + " , '" + sdf.format(obj.getEffDate()) + "' , '" +obj.getFormName() + "' , '" + obj.getNotes() + "' , '"+obj.getStatus() +"' , " + obj.getCurrProjectId() );
		query.append(" )");
		return query.toString();	
	}
	//Employee transfer between sites, update empmast
	public String updateEmployeeTableForTranfer(TransferModel obj)
	{
		query=new StringBuffer();
		query.append(" UPDATE empmast set loc_id=" + obj.getProjectKey() + " where emp_key="  + obj.getEmpKey());
		return query.toString();
	}
	//Project List
	public String getMaxProjectKeyQuery()
	{
		query=new StringBuffer();
		query.append(" SELECT MAX(Project_Key) as 'REC_NO' from ProjectMast ");
		return query.toString();		
	}
	
	public String insertNewProjectQuery(ProjectModel obj)
	{
		query=new StringBuffer();
		query.append(" INSERT INTO ProjectMast(Project_Key,Project_Code,Project_Name,Project_NameAR,COMP_KEY,Status_id,start_date,end_date,LastModified) ");
		query.append(" VALUES(" +obj.getProjectKey() + " , '" + obj.getProjectCode() + "' , '" + obj.getProjectName() + "' , '" + obj.getProjectArName() + "' , " + obj.getCompanyKey() + " , " + obj.getStatusId()+",'" +sdf.format(obj.getStartDate())+"','" +sdf.format(obj.getEndDate())+"' , getdate()");
		query.append(" )");
		return query.toString();
	}
	
	public String updateProjectQuery(ProjectModel obj)
	{
		query=new StringBuffer();
		query.append(" UPDATE ProjectMast set LastModified=getdate(), Project_Code='" + obj.getProjectCode() + "' , Project_Name='" +obj.getProjectName() + "' ");
		query.append(" , Project_NameAR='" + obj.getProjectArName() + "' , Status_id= " + obj.getStatusId()+",start_date='"+sdf.format(obj.getStartDate())+"',end_date='"+sdf.format(obj.getStartDate())+"'");
		query.append(" where Project_Key="  + obj.getProjectKey());
		return query.toString();
	}
	
	public String getProjectUsedInTransferQuery()
	{
		query=new StringBuffer();
		query.append("  SELECT distinct Project_Key from LOCATIONTRANSFER  ");
		return query.toString();		
	}
	
	public String deleteProjectQuery(int Project_Key)
	{
		query=new StringBuffer();
		query.append("  delete from ProjectMast where Project_Key=" + Project_Key);
		return query.toString();		
	}
	
	//Service List
	public String getServiceUsedInTimesheetQuery()
	{
		query=new StringBuffer();
		query.append("  SELECT distinct SERVICE_ID from DAILYTS  ");
		return query.toString();		
	}
	public String deleteServiceQuery(int listID)
	{
		query=new StringBuffer();
		query.append("  delete from HRLISTVALUES where ID=" + listID);
		return query.toString();		
	}
	
	//Time sheet Approve
	public String getTimeSheetHistoryQuery(int compKey,int month,int year)
	{
		query=new StringBuffer();
		query.append("  select  month(TS_DATE) as tsMonth,year(TS_DATE) as tsYear");
		query.append("  from DAILYTS ");
		query.append(" where COMP_KEY=" + compKey);
		query.append(" and year(TS_DATE)="+year);
		if(month!=0)
		query.append(" and month(TS_DATE)=" + month);
		
		query.append(" group by month(TS_DATE),year(TS_DATE)");
		query.append(" order by 1 ");
		return query.toString();		
	}
	public String getTimeSheetHistoryEmployeeQuery(int compKey,int month,int year,int projectKey,int status)
	{
		query=new StringBuffer();
		query.append("  select  english_full,count(*) as cnt,emp_no,emp_key,month(TS_DATE) as tsMonth,year(TS_DATE) as tsYear,isnull(TS_STATUS,0) as TS_STATUS");
		query.append(" ,sum(case when holiday='N' then 1 else 0 end) as 'Present' , sum(case when holiday='Y' then 1 else 0 end) as 'Holidays' ");
		query.append("  from DAILYTS inner join empmast on DAILYTS.LABOUR_KEY=empmast.emp_key");
		query.append(" where DAILYTS.COMP_KEY=" + compKey);
		query.append(" and year(TS_DATE)="+year);
		if(month!=0)
		query.append(" and month(TS_DATE)=" + month);
		if(projectKey!=0)
		query.append(" and project_key=" + projectKey);	
		if(status!=2)
			query.append(" and isnull(TS_STATUS,0)=" + status);	
		query.append(" group by english_full,emp_no,emp_key,month(TS_DATE),year(TS_DATE),TS_STATUS");
		query.append(" order by REPLICATE('0',2-LEN(emp_no)) + emp_no");
		return query.toString();		
	}
	
	public String approveTimeSheetQuery(TimeSheetDataModel obj)
	{
		query=new StringBuffer();
		query.append("  update DAILYTS set TS_STATUS=1 where LABOUR_KEY=" + obj.getEmpKey());
		query.append(" and month(TS_DATE)=" + obj.getTsMonth());
		query.append(" and year(TS_DATE)=" + obj.getTsYear());
		return query.toString();	
	}
	public String approveMonthlyTimeSheetQuery(TimeSheetDataModel obj)
	{
		query=new StringBuffer();
		query.append("  update DAILYTS set TS_STATUS=1 where ");
		query.append("  month(TS_DATE)=" + obj.getTsMonth());
		query.append(" and year(TS_DATE)=" + obj.getTsYear());
		return query.toString();	
	}
	
	public String autoApproveTimeSheetQuery()
	{
		query=new StringBuffer();
		query.append("  update DAILYTS set TS_STATUS=1 where ");
		query.append("  DAILYTS.TS_DATE Between DATEADD(day,-7,getdate()) and DATEADD(day,-1,getdate())");		
		return query.toString();	
	}
	
	  //Time sheet History
		public String getEmployeeTimeSheetHistoryQuery(int compKey,int fromMonth,int fromYear,int toMonth,int toYear,int empKey,String filterEmpKeys,int supervisorId)
		{
			query=new StringBuffer();
			query.append("  select  month(TS_DATE) as tsMonth,year(TS_DATE) as tsYear");
			query.append("  from DAILYTS ");
			query.append(" where COMP_KEY=" + compKey);
			if(empKey!=0)
			query.append(" and LABOUR_KEY=" + empKey);	
			if(supervisorId!=0)
			query.append(" and supervisorid=" + supervisorId);	
			if(!filterEmpKeys.equals(""))
			query.append(" and LABOUR_KEY in (" + filterEmpKeys + ")");	
			
			query.append(" and year(TS_DATE) between "+fromYear + " and " + toYear);			
			query.append(" and month(TS_DATE) between " + fromMonth + " and " + toMonth);
			query.append(" group by month(TS_DATE),year(TS_DATE)");
			query.append(" order by 2 ");
			return query.toString();		
		}
		public String getDetailTimeSheetHistoryQuery(int compKey,String DateFrom,String DateTo,int projectKey,int empKey,String filterEmpKeys,int supervisorId)
		{
			query=new StringBuffer();
			query.append("  select english_full,count(*) as cnt,emp_no,emp_key,month(TS_DATE) as tsMonth,year(TS_DATE) as tsYear,isnull(TS_STATUS,0) as TS_STATUS");
			query.append(" ,sum(case When DAILYTS.Status ='P' Then UNIT_NOS Else 0 End) As 'Present',");
			query.append(" sum(case When DAILYTS.Status ='A' Then NORMAL_HRS Else 0 End) As 'Absence',");
			query.append(" sum(case When DAILYTS.Status ='H' Then NORMAL_HRS Else 0 End) As 'Holidays',");
			query.append(" sum(case When DAILYTS.Status ='S' Then NORMAL_HRS Else 0 End) As 'Sick',");
			query.append(" sum(case When DAILYTS.Status ='L' Then NORMAL_HRS Else 0 End) As 'Leave' ");
			//query.append(" ,Project_Name , ProjectMast.project_key");
			query.append("  from DAILYTS inner join empmast on DAILYTS.LABOUR_KEY=empmast.emp_key");
			query.append(" left join ProjectMast on ProjectMast.Project_Key=DAILYTS.Project_Key");
			query.append(" where DAILYTS.COMP_KEY=" + compKey);
			if(empKey!=0)
			query.append(" and LABOUR_KEY=" + empKey);
			if(supervisorId!=0)
			query.append(" and supervisorid=" + supervisorId);	
			if(!filterEmpKeys.equals(""))
			query.append(" and LABOUR_KEY in (" + filterEmpKeys + ")");
			
			query.append(" and TS_DATE Between '"+DateFrom+"' And '"+DateTo+"'");
			/*if(month!=0)
			query.append(" and month(TS_DATE)=" + month);*/
			if(projectKey!=0)
			query.append(" and ProjectMast.project_key=" + projectKey);	
			query.append(" group by english_full,emp_no,emp_key,month(TS_DATE),year(TS_DATE),TS_STATUS ");
			query.append(" order by REPLICATE('0',2-LEN(emp_no)) + emp_no");
			return query.toString();		
		}
		
		public String getProjectReport(int compKey,String DateFrom,String DateTo,int empKey,String filterEmpKeys,int projectKey)
		{
			query=new StringBuffer();
			
			
			query.append("Select DAILYTIMING.Project_Key,projectmast.project_code,projectmast.Project_Name,ProjectMast.Project_NameAR,Month(DAILYTIMING.TS_DATE) As TSMonth,");
			query.append("Year(DAILYTIMING.TS_DATE) As TSYear,DAILYTS.NORMAL_HRS,");
			query.append("Sum(Case When DAILYTIMING.Status ='P' Then DAILYTIMING.UNIT_NOS Else 0 End) As WORKWithPay,");
			query.append("Sum(Case When DAILYTIMING.Status ='A' Then DAILYTIMING.NORMAL_UNITS Else 0 End) As ABSENCEWithPay,");
			query.append("Sum(Case When DAILYTIMING.Status ='H' Then DAILYTIMING.UNIT_NOS Else 0 End) As HolidaySWithPay,");
			query.append("Sum(Case When DAILYTIMING.Status ='S' Then DAILYTIMING.NORMAL_UNITS Else 0 End) As SICKWithPay,");
			query.append("Sum(Case When DAILYTIMING.Status ='L' Then DAILYTIMING.NORMAL_UNITS Else 0 End) As Leave From  ");
			query.append("DAILYTIMING INNER JOIN DAILYTS ON DAILYTIMING.REC_NO = DAILYTS.REC_NO  ");
			query.append("INNER JOIN EMPMAST ON DAILYTS.LABOUR_KEY = EMPMAST.EMP_KEY ");
			query.append("LEFT JOIN PROJECTMAST ON DAILYTIMING.PROJECT_KEY=PROJECTMAST.PROJECT_KEY WHERE ");
			query.append("DAILYTS.TS_DATE Between '"+DateFrom+"' And '"+DateTo+"' And   ");
			query.append("DAILYTS.COMP_KEY =" +compKey+" ");
			if(projectKey>0)
			{
			query.append("And DAILYTIMING.Project_Key="+projectKey+"");
			}
			query.append("And DAILYTS.Labour_Key  in ");
			if(filterEmpKeys!=null && !"".equalsIgnoreCase(filterEmpKeys))
			{
				query.append("("+filterEmpKeys+")");
			}
			else if(empKey>0)
			{
				query.append("("+empKey +")");
			}
			else
			{	
			query.append(" (Select Distinct LABOUR_KEY from DAILYTS Inner Join EMPMAST on EMPMAST.EMP_KEY   = DAILYTS.Labour_Key ");
			query.append(" Where TS_DATE  Between '"+DateFrom+"' And '"+DateTo+"' And DAILYTS.COMP_KEY    ="+compKey+") ");
			}
			query.append("Group by DAILYTIMING.Project_Key,projectmast.project_code,projectmast.Project_Name,ProjectMast.Project_NameAR, Month(DAILYTIMING.TS_DATE), Year(DAILYTIMING.TS_DATE)");
			query.append(",DAILYTS.NORMAL_HRS Order by DAILYTIMING.Project_Key,");
			query.append("Year(DAILYTIMING.TS_DATE),Month(DAILYTIMING.TS_DATE),DAILYTS.NORMAL_HRS");
			return query.toString();
		}
		
		public String getOverTimeLabesByComapny(int comapnyId)
		{
			query=new StringBuffer();
			query.append("Select Distinct OT_RATE from OTCALCULATION WHERE COMP_KEY="+comapnyId+" Order by OT_RATE");
			return query.toString();
		}
		
		public String getTimesheetGenartedReport(int compKey,String DateFrom,String DateTo,int empKey,String filterEmpKeys,int supervisorId,int projectId,int userId)
		{
			query=new StringBuffer();
			query.append("SELECT Unitmast.*,ProjectMast.Project_name,EmployeeDetails.position,EmployeeDetails.department,DAILYTIMING.status as leavestatus,DAILYTIMING.*, EMPMAST.DEP_ID, EMPMAST.POS_ID,EMPMAST.SUPERVISOR, EMPMAST.ENGLISH_FULL,EMPMAST.ARABIC_FULL,EMPMAST.EMPLOYEEMENT_DATE,EMPMAST.EMP_NO,EMPMAST.EMP_KEY,EMPMAST.COMP_KEY,EMPMAST.ACTIVE,EMPMAST.TERMINATED,HRLISTVALUES.DESCRIPTION as DEPDESC,HRLISTVALUES.ARABIC as DEPDESCAR,COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR FROM "); 
			query.append("(((DAILYTIMING LEFT JOIN EMPMAST ON DAILYTIMING.LABOUR_KEY = EMPMAST.EMP_KEY) LEFT JOIN EmployeeDetails ON DAILYTIMING.LABOUR_KEY = EmployeeDetails.employeeKey LEFT JOIN UNITMAST ON DAILYTIMING.UNIT_ID = UNITMAST.UNIT_KEY LEFT JOIN ProjectMast on ProjectMast.Project_Key=DAILYTIMING.Project_Key LEFT JOIN COMPSETUP ON EMPMAST.COMP_KEY = COMPSETUP.COMP_KEY)");
			query.append(" LEFT JOIN HRLISTVALUES ON EMPMAST.DEP_ID = HRLISTVALUES.ID) INNER JOIN USERWEBCOMPANY ON EMPMAST.COMP_KEY=USERWEBCOMPANY.companyid ");
			query.append("WHERE USERWEBCOMPANY.USERID ="+userId+" AND  EMPMAST.COMP_KEY ="+compKey+ " ");
			if(projectId>0)
			{
			query.append(" and DAILYTIMING.project_key="+projectId+"  "); 
			}
			if(filterEmpKeys!=null && !"".equalsIgnoreCase(filterEmpKeys))
			{
				query.append("And DAILYTIMING.Labour_Key  in ("+filterEmpKeys+")");
			}
			if(supervisorId>0)
			{
			query.append(" and EMPMAST.SUPERVISOR="+supervisorId+"  "); 
			}
			else if(empKey>0)
			{
				query.append("And DAILYTIMING.Labour_Key in ("+empKey +")");
			}
			query.append(" AND DAILYTIMING.TS_DATE BETWEEN '"+DateFrom+"' AND '"+DateTo+"' ORDER BY EMPMAST.COMP_KEY,REPLICATE('0',2-LEN(emp_no)) + emp_no,TS_DATE");
			return query.toString();
		}
		
		
		public String getTimesheetGenartedReportOvertIme(String DateFrom,String DateTo)
		{
			query=new StringBuffer();
			//and rec_no="+recNo+" and line_no="+LineNo+"
			query.append("SELECT * FROM DAILYOT WHERE TS_DATE   between '"+DateFrom+"' And '"+DateTo+"'  Order By Rec_No,Calculation"); 
			return query.toString();
		}
		
		
		
		public String getOTHistoryQuery(String DateFrom,String DateTo)
		{
			query=new StringBuffer();
			query.append("  SELECT CALCULATION,project_key,labour_key, SUM(UNITS) as 'TotalUnit', sum(AMOUNT) as 'TotalAmount' ");
			query.append(" FROM  DAILYOT");					
		//	query.append(" WHERE LABOUR_KEY=" + empKey);
			//query.append(" and PROJECT_KEY=" + projectKey);
			query.append(" Where TS_DATE Between '"+DateFrom+"' And '"+DateTo+"'");			
			//query.append(" and month(TS_DATE)=" + month);
			query.append(" group by CALCULATION,project_key,labour_key");
			return query.toString();	
		}
		
		public String searchAdvancedEmployeeQuery(int compKey,String type,String employeeType,String empNo,String empName,int depId,int posId)
		{
			query=new StringBuffer();
			query.append(" select * from  EmployeeDetails");	
			query.append(" where Active ='A' and CompKey=" + compKey);
			if(type.equals("T"))
			query.append(" and Salary_Create='T'");	
			if(!employeeType.equals(""))
			query.append(" and EmployeeType='" + employeeType + "'");	
			if(!empNo.equals(""))
				query.append(" and EmployeeNo='" + empNo + "'");
			if(!empName.equals(""))
				query.append(" and fullname like'" + empName + "%'");
			if(depId>0)
			query.append(" and DepartmentID=" + depId + "");
			if(posId>0)
				query.append(" and PositionID=" + posId + "");
			query.append(" order by REPLICATE('0',2-LEN(employeeNo)) + employeeNo");	
			return query.toString();		
		}
		
		//Assign Employee To TimeSheet	
		public String getAssignSalaryEmployeeQuery(int compKey,String type)
		{
			query=new StringBuffer();
			query.append(" select * from  EmployeeDetails");	
			query.append(" where Active ='A' and CompKey=" + compKey);
			if(type.equals("T"))
			query.append(" and Salary_Create='T'");	
			query.append(" order by REPLICATE('0',2-LEN(employeeNo)) + employeeNo");	
			return query.toString();		
		}
		
		public String getAssignEmployeesToProjectByKeys(int compKey,String filterKeys,String filter)
		{
			query=new StringBuffer();
			query.append("SELECT * FROM EmployeeDetails Where compkey= "+compKey+" AND (TERMINATED='N' or TERMINATED IS NULL)");
			if(filter.equalsIgnoreCase("N"))
			{
				query.append("And locationId=0");
			}
			else if(filter.equalsIgnoreCase("A"))
			{
				query.append("And locationId>0");
			}
			if(!filterKeys.equals(""))
			query.append(" and employeekey in("+filterKeys+")");
			query.append(" ORDER BY REPLICATE('0',2-LEN(employeeNo)) + employeeNo");
			return query.toString();		
		}
		
		public String updateAssignedEmployeesToProject(int locationId,int empId)
		{
			query=new StringBuffer();
			query.append(" update empmast");	
			query.append(" set LOC_ID ="+locationId+" where emp_key ="+empId +"");
			return query.toString();		
		}
		
		
		public String updateSalaryCreateQuery(int empKey,String salaryCreate)
		{			
			query=new StringBuffer();
			query.append("  update EMPMAST set SALARY_CREATE='" + salaryCreate +  "'");
			query.append("  where EMP_KEY=" + empKey);
			return query.toString();	
		}
		
		public String getEmployeesForReports(int compKey,String DateFrom,String DateTo,int empKey,String filterEmpKeys,int supervisorId,int projectId,int userId)
		{
			query=new StringBuffer();
			query.append(" select  english_full,count(*) as cnt,emp_no,emp_key,month(TS_DATE) as tsMonth,year(TS_DATE) as tsYear,isnull(DAILYTIMING.STATUS,0) as tsSTATUS ,sum(case When DAILYTIMING.Status ='P' Then UNIT_NOS Else 0 End) As 'Present', sum(case When DAILYTIMING.Status ='A' Then NORMAL_UNITS Else 0 End) As 'Absence', sum(case When DAILYTIMING.Status ='H' Then UNIT_NOS Else 0 End) As 'Holidays', sum(case When DAILYTIMING.Status ='S' Then UNIT_NOS Else 0 End) As 'Sick', sum(case When DAILYTIMING.Status ='L' Then UNIT_NOS Else 0 End) As 'Leave' ");  
					query.append(" FROM (DAILYTIMING ");
					query.append("LEFT JOIN EMPMAST ON DAILYTIMING.LABOUR_KEY = EMPMAST.EMP_KEY) "); 
					query.append(" left join ProjectMast on ProjectMast.Project_Key=DAILYTIMING.Project_Key ");
					query.append(" LEFT JOIN USERWEBCOMPANY ON EMPMAST.COMP_KEY=USERWEBCOMPANY.companyid "); 
					query.append(" WHERE USERWEBCOMPANY.USERID ="+userId+" AND  EMPMAST.COMP_KEY ="+compKey+ " ");
					if(projectId>0)
					{
					query.append(" and DAILYTIMING.project_key="+projectId+"  "); 
					}
					if(filterEmpKeys!=null && !"".equalsIgnoreCase(filterEmpKeys))
					{
						query.append("And DAILYTIMING.Labour_Key  in ("+filterEmpKeys+")");
					}
					if(supervisorId>0)
					{
					query.append(" and EMPMAST.SUPERVISOR="+supervisorId+"  "); 
					}
					else if(empKey>0)
					{
						query.append("And DAILYTIMING.Labour_Key in ("+empKey +")");
					}
					query.append(" AND DAILYTIMING.TS_DATE BETWEEN '"+DateFrom+"' AND '"+DateTo+"' group by english_full,emp_no,emp_key,month(TS_DATE),year(TS_DATE),DAILYTIMING.STATUS order by REPLICATE('0',2-LEN(emp_no)) + emp_no ");
					
			return query.toString();
		}
		
		//Assign Employee to Shift		
		
		public String getAssignedEmployeesToShiftQuery(int compId,Date fromDate,Date toDate,String empkeys)
		{
			  query=new StringBuffer();
			  query.append("SELECT EMPSHIFT.*,shiftmast.* ,EmployeeDetails.* FROM EMPSHIFT  INNER JOIN EmployeeDetails ON EMPSHIFT.EMP_KEY = EmployeeDetails.employeeKey INNER JOIN shiftmast ON EMPSHIFT.shift_key = shiftmast.shift_Key "); 
			  query.append(" WHERE EMPSHIFT.COMP_KEY="+compId+" AND ((EMPSHIFT.FROM_DATE BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"') OR (EMPSHIFT.TO_DATE BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"') OR (EMPSHIFT.TO_DATE >= '"+sdf.format(toDate)+"') and ( EMPSHIFT.FROM_DATE <= '"+sdf.format(fromDate)+"'))  ");
			  if(empkeys!=null)
			  query.append(" AND employeekey in (" + empkeys + ")");
			  query.append(" ORDER BY EMPSHIFT.POS_ID,EMPSHIFT.EMP_KEY,EMPSHIFT.FROM_DATE");
			  return query.toString();
		}
		
		public String getNewAssignedEmployeesToShiftQuery(String empkeys)
		{
			  query=new StringBuffer();
			  query.append("SELECT * FROM EmployeeDetails "); 
			  query.append(" WHERE 1=1 ");
			  if(empkeys!=null)
			  query.append(" AND employeekey in (" + empkeys + ")");			
			  return query.toString();
		}
		
		public String getAssignedPositionToShiftQuery(int compId,Date fromDate,Date toDate)
		{
			  query=new StringBuffer();
			  query.append("SELECT EMPSHIFT.*,DESCRIPTION as 'Position' FROM EMPSHIFT  inner join HRLISTVALUES on  EMPSHIFT.POS_ID= HRLISTVALUES.ID "); 
			  query.append(" WHERE  EMP_KEY = 0 and EMPSHIFT.COMP_KEY="+compId+" AND ((EMPSHIFT.FROM_DATE BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"') OR (EMPSHIFT.TO_DATE BETWEEN '"+sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"') OR (EMPSHIFT.TO_DATE >= '"+sdf.format(toDate)+"') and ( EMPSHIFT.FROM_DATE <= '"+sdf.format(fromDate)+"'))  ORDER BY EMPSHIFT.POS_ID,EMPSHIFT.EMP_KEY,EMPSHIFT.FROM_DATE");
			  return query.toString();
		}
		
		public String addEmployeeShiftQuery(EmployeeShiftModel obj)
		{
			query=new StringBuffer();			
			query.append("insert into EMPSHIFT(COMP_KEY,POS_ID,EMP_KEY,FROM_DATE,TO_DATE,SHIFT_KEY) values('%s','%s','%s','%s','%s','%s')");				    
			return query.toString().format(query.toString(),obj.getCompanyKey(),obj.getPositionID(),obj.getEmployeeKey(),obj.getShiftFromDate(),obj.getShiftToDate(),obj.getShiftkey() );		
		}
		public String updateEmployeeShiftQuery(EmployeeShiftModel obj,String shiftNewToDate)
		{
			query=new StringBuffer();			
			query.append(" Update EMPSHIFT Set TO_DATE='%s' Where EMP_KEY='%s' and POS_ID='%s' and SHIFT_KEY='%s' and FROM_DATE='%s' and TO_DATE='%s' ");				    
			return query.toString().format(query.toString(),shiftNewToDate , obj.getEmployeeKey(), obj.getPositionID() ,obj.getShiftkey(), obj.getShiftFromDate(),obj.getShiftToDate() );		
		}
		public String deleteEmployeeShiftQuery(EmployeeShiftModel obj)
		{
			query=new StringBuffer();			
			query.append(" delete from EMPSHIFT Where EMP_KEY='%s' and POS_ID='%s' and SHIFT_KEY='%s' and FROM_DATE='%s' and TO_DATE='%s' ");				    
			return query.toString().format(query.toString() , obj.getEmployeeKey(), obj.getPositionID() ,obj.getShiftkey(), obj.getShiftFromDate(),obj.getShiftToDate() );		
		}
		
		public String getMinMaxAssignedShiftQuery(int empKey,Date fromDate,Date toDate)
		{
			  query=new StringBuffer();
			  query.append("SELECT MIN(FROM_DATE) As minFromDate, MIN(TO_DATE) As minToDate, MAX(FROM_DATE) As maxFromDate,MAX(TO_DATE)   As maxToDate "); 
			  query.append(" FROM EMPSHIFT ");
			  query.append(" where EMP_KEY=" + empKey);
			  query.append(" AND ( ");
			  query.append(" ( FROM_DATE BETWEEN '" + sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"')");
			  query.append(" or ( TO_DATE BETWEEN '"+ sdf.format(fromDate)+"' AND '"+sdf.format(toDate)+"')");
			  query.append(" or  ( FROM_DATE <='" + sdf.format(fromDate) +"' AND  TO_DATE   >= '" + sdf.format(toDate)+"')");
			  query.append(" )");
			  return query.toString();
		}
		
		public String updateEmployeeShiftToDateQuery(EmployeeShiftModel obj,String shiftNewToDate,String tmpMinFromDate,String tmpMinTODate)
		{
			query=new StringBuffer();			
			query.append(" Update EMPSHIFT Set TO_DATE='" + shiftNewToDate +"' Where ( (' " + tmpMinFromDate + "' Between FROM_DATE And TO_DATE) ");
			query.append(" OR ('" + tmpMinTODate + "' Between FROM_DATE And TO_DATE))");
			query.append(" And EMP_KEY=" + obj.getEmployeeKey());			
			return query.toString();		
		}
		
		//Department Setup
		public String getDepartmentListQuery(int fieldId)
		{
		  query=new StringBuffer();
		  query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID from HRLISTVALUES");			
		  query.append(" where FIELD_ID=" + fieldId);						
		  query.append(" order by DESCRIPTION");
		  return query.toString();		
		}
		
		public String getDepartmentPositionListQuery(int COMP_KEY,int departmentID)
		{
		  query=new StringBuffer();
		  query.append("Select  COMP_KEY, DEP_ID, GRADE_ID, CLASS_ID, SECTION_ID, ID, DESCRIPTION, EMP_ALLOWED from COMPSTANDPOS");			
		  query.append(" where COMP_KEY=" + COMP_KEY);						
		  //if(departmentID>0)
			query.append(" and DEP_ID=" + departmentID);  
		  query.append(" order by DESCRIPTION");
		  return query.toString();		
		}
		
		public String updateCOMPSTANDPOSQuery(int compKey,int depId,int posId,int empAllowed)
		{
			query=new StringBuffer();			
			query.append(" Update COMPSTANDPOS Set EMP_ALLOWED='%s' Where COMP_KEY='%s' and DEP_ID='%s' and ID='%s' ");				    
			return query.toString().format(query.toString(),empAllowed , compKey ,depId,posId);		
		}
		
		public String addCOMPSTANDPOSQuery(int compKey,int depId,int posId,int empAllowed,String positionName)
		{
			query=new StringBuffer();			
			query.append("insert into COMPSTANDPOS(COMP_KEY,DEP_ID,GRADE_ID,CLASS_ID,SECTION_ID,ID,DESCRIPTION,EMP_ALLOWED) values('%s','%s','%s','%s','%s','%s','%s','%s')");				    
			return query.toString().format(query.toString(),compKey , depId , 0,0,0,posId ,positionName ,empAllowed );		
		}
		
		public String checkIfDepartmentPositionUsedQuery(int COMP_KEY,int departmentID,int positionId)
		{
		  query=new StringBuffer();
		  query.append("Select  count(*) as cnt FROM EMPMAST ");			
		  query.append(" where COMP_KEY=" + COMP_KEY);								 
		  query.append(" and DEP_ID=" + departmentID);  
		  query.append(" and POS_ID=" + positionId);		  
		  return query.toString();		
		}
		
		public String deleteCOMPSTANDPOSQuery(int COMP_KEY,int departmentID,int positionId)
		{
		query = new StringBuffer();
		query.append("  delete from COMPSTANDPOS ");
		query.append(" where COMP_KEY=" + COMP_KEY);
		query.append(" and DEP_ID=" + departmentID);
		query.append(" and ID=" + positionId);
		return query.toString();	
		}
		public String getTotalCompanyEmployeesQuery(int compKey)
		{
			query=new StringBuffer();
			query.append(" SELECT COUNT(EMP_KEY) AS totEmp FROM EMPMAST WHERE COMP_KEY= "+compKey+"");
			return query.toString();
		}
		public String getTotalEmployeesAllowedQuery(int compKey)
		{
			query=new StringBuffer();
			query.append(" SELECT SUM(EMP_ALLOWED) AS empAllowed FROM COMPSTANDPOS WHERE COMP_KEY= "+compKey+"");
			return query.toString();
		}
		public String getCompanyTotalDepartmentsQuery(int compKey)
		{
			query=new StringBuffer();
			query.append(" SELECT TOT_DEP,TOT_POS,TOT_EMP,TOT_VAC FROM COMPSETUP WHERE COMP_KEY= "+compKey+"");
			return query.toString();
		}
		
		public String getNewCompanyTotalDepartmentsQuery(int compKey)
		{
			query=new StringBuffer();
			query.append(" SELECT count(distinct ID) as 'TotalPositions' , count( distinct DEP_ID) as 'TotalDepartments' from COMPSTANDPOS  WHERE COMP_KEY= "+compKey+"");
			return query.toString();
		}
		public String updateNewCompanySetupQuery(int compKey,int totalDep,int totalPos)
		{
			query=new StringBuffer();			
			query.append(" Update COMPSETUP Set TOT_DEP='%s' , TOT_POS='%s' Where COMP_KEY='%s' ");				    
			return query.toString().format(query.toString(),totalDep ,totalPos, compKey);		
		}
		
		public String getCustomerTasksQuery(int userId,String orderBy)
		{
			query=new StringBuffer();
			query.append("select DISTINCT TASKS.TaskID, TASKS.status,TASKS.TaskName,TASKS.taskNo,TaskDetails.USERiD  from dbo.TaskDetails ");
			query.append("LEFT JOIN TASKS ON TaskDetails.TASKID=TASKS.TASKID");
			if(userId>0)
			query.append(" WHERE TaskDetails.USERiD= "+userId+" ");
			if(orderBy.equalsIgnoreCase("Number"))
				query.append(" order by TASKS.taskId");	
			else
				query.append(" order by TASKS.TaskName");	
				
			return query.toString();
		}
		public String updateCustomerTaskStatusQuery(int taskId,int statusId)
		{
			query=new StringBuffer();			
			query.append(" Update Tasks Set status='%s' Where TaskID='%s' ");				    
			return query.toString().format(query.toString(),statusId,taskId);		
		}
		public String addTaskDetailsQuery(int taskId,int statusId,String notes,int webUserID)
		{
						
		  Calendar cal = Calendar.getInstance(); 
		  Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
				
		  query=new StringBuffer();		 
		  query.append(" Insert into TaskDetails (taskID,actualTime,status,usercomments,DateTime,UserId)");
		  query.append(" values("+taskId+","+"0"+"," + statusId+",'"+notes+"','"+timestamp+"',"+ webUserID +")");
		  return query.toString();
		}
		
		//time sheet attachment
		public String addTimesheetAttachmentQuery(QuotationAttachmentModel obj)
		{
			query=new StringBuffer();		 
			query.append(" Insert into TimeSheetAttachment (recNO,attachmentpath,fileName)");
			query.append(" values("+obj.getSerialNumber()+",'"+obj.getFilepath()+"','"+obj.getFilename()+"')");
			return query.toString();
		}
		public String getTimesheetAttachmentQuery(int recNo)
		{
			query=new StringBuffer();		 
			query.append(" select * from TimeSheetAttachment where recNO =" + recNo);		
			return query.toString();
		}
		public String deleteTimesheetAttachmnetQuery(int recNo,int attachId)
		{
			  query=new StringBuffer();		
			  if(recNo>0)
			  query.append("delete from TimeSheetAttachment where recNO="+recNo+"");
			  if(attachId>0)
				  query.append("delete from TimeSheetAttachment where AttachId="+attachId+"");
			  return query.toString();
		}
		
		//MobileAttendance
		public String getMobileAttendanceQuery(int UserID)
		{
			query=new StringBuffer();		 
			query.append(" select isnull(english_full ,u.USERNAME) as 'EmployeeName' ,m.CheckinTime,m.CheckoutTime,m.CheckinLatitude,m.CheckinLongitude,m.CheckoutLatitude,m.CheckoutLongitude,CheckinNote,CheckoutNote ");
			query.append("  ,Reason,Result,DestinaceKm,DestinaceMeter,CustomerType,CustomerName  ");
			query.append("   from MobileAttendance m inner join users u on m.UserId=u.USERID");
			query.append(" left join empmast e on e.emp_key =u.EMP_KEY  ");
			if(UserID>0)
				query.append(" where m.UserId=" + UserID);
			query.append(" order by m.CheckinTime desc");
		
			query.append(" ");
			return query.toString();
		}
		
		public String getUsersQuery()
		{
			query=new StringBuffer();		 
			query.append(" select isnull(english_full ,u.USERNAME) as 'EmployeeName' ,u.USERID");
			query.append(" from users u  left join empmast e on e.emp_key =u.EMP_KEY   ");
			query.append(" where u.STATUS=1");
			query.append(" order by 1   ");
			return query.toString();
		}
}
