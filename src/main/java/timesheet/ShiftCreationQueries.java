package timesheet;

import java.text.SimpleDateFormat;

import model.CompSetupModel;
import model.ShiftModel;

public class ShiftCreationQueries 
{
	StringBuffer query;
	
	public String getCompanySetupQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append(" SELECT WEEK_START,DEF_HRS,change_holiday,INCLUDEHOLIDAY_UNIT,TIMESHEET_OTCHANGE,TIMESHEET_TIMEAUTO,TIMESHEET_CHANGETIME,TIMESHEET_CALCEXTRAHOURS FROM COMPSETUP WHERE  COMP_KEY=" +companyKey);		
		return query.toString();
	}
	public String updateCompanySetupQuery(CompSetupModel obj)
	{
		query=new StringBuffer();
		query.append("UPDATE COMPSETUP SET WEEK_START='%s' ,CHANGE_HOLIDAY ='%s', DEF_HRS='%s',INCLUDEHOLIDAY_UNIT='%s',TIMESHEET_CHANGETIME='%s',TIMESHEET_CALCEXTRAHOURS='%s' where  COMP_KEY='%s'");
		return query.toString().format(query.toString(), obj.getWeekStart(),obj.getChangeHoliday(),obj.getDefHours(),obj.getIncludeholidayUnit() , obj.getTimesheetChangeTime()?"Y":"N",obj.getTimesheetCalculateExtraHours()?"Y":"N" ,obj.getCompanyKey());
	}
	
	public String getShifTypeQuery()
	{
		query=new StringBuffer();
		query.append(" select * from UNITMAST");		
		return query.toString();
	}
	public String getShiftListQuery(int companyKey)
	{
		query=new StringBuffer();
		 query.append(" SELECT SHIFTMAST.REC_NO, SHIFTMAST.COMP_KEY, SHIFTMAST.SHIFT_KEY, SHIFTMAST.UNIT_KEY, SHIFTMAST.SHIFT_CODE, SHIFTMAST.NO_OF_SHIFTS, ");
		 query.append(" SHIFTMAST.TIMING_FLAG, UNITMAST.UNIT_NAME, UNITMAST.TIMING , SHIFTDET.DEF_HOURS, SHIFTDET.DEF_FROMTIME, SHIFTDET.DEF_TOTIME,SHIFTDET.Rec_NO as 'shiftDetRecNo'");
		 query.append(" FROM SHIFTMAST INNER JOIN UNITMAST ON SHIFTMAST.UNIT_KEY = UNITMAST.UNIT_KEY");
		 query.append(" INNER JOIN SHIFTDET ON SHIFTMAST.SHIFT_KEY = SHIFTDET.SHIFT_KEY");
		 query.append(" where SHIFTMAST.COMP_KEY ="  + companyKey);	
		 query.append(" ORDER BY SHIFT_KEY");
		 return query.toString();		
	}
	public String getCompanyShiftSetupQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append(" SELECT * FROM COMPSHIFT WHERE  COMP_KEY=" +companyKey + " ORDER BY REC_NO");		
		return query.toString();
	}
	public String getMaxRectQuery(String tableName,String columnName)
	{
		query=new StringBuffer();
		query.append(" Select Max(" + columnName + ") from " + tableName);
		return query.toString();
	}
	
	public String insertShiftMastQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("INSERT INTO SHIFTMAST(REC_NO,COMP_KEY,SHIFT_KEY,UNIT_KEY,SHIFT_CODE,NO_OF_SHIFTS,TIMING_FLAG) VALUES ('%s','%s','%s','%s','%s','%s','%s') ");
		return query.toString().format(query.toString(), obj.getRecNo(),obj.getCompKey(),obj.getShiftKey(),obj.getUnitKey(),obj.getShiftCode(),obj.getNoOfShifts(),obj.getTimingFlag());
	}
	public String updateShiftMastQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("UPDATE SHIFTMAST SET UNIT_KEY='%s' ,SHIFT_CODE ='%s', NO_OF_SHIFTS='%s',TIMING_FLAG='%s' where  REC_NO='%s'");
		return query.toString().format(query.toString(), obj.getUnitKey(),obj.getShiftCode(),obj.getNoOfShifts(),obj.getTimingFlag() , obj.getRecNo());
	}
	public String deleteShiftDetQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("DELETE FROM SHIFTDET WHERE SHIFT_KEY='%s'");
		return query.toString().format(query.toString(), obj.getShiftKey());
	}
	
	public String insertShiftDetQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		if(obj.isTimingShift())
		{
		String fromtime=tdf.format(obj.getFromTime());
		String toTime=tdf.format(obj.getToTime());
		query.append("INSERT INTO SHIFTDET(REC_NO,SHIFT_KEY,DEF_HOURS,DEF_FROMTIME,DEF_TOTIME) VALUES ('%s','%s','%s','%s','%s') ");
		return query.toString().format(query.toString(), obj.getShiftDetRecNo(),obj.getShiftKey(),obj.getUnits(),fromtime,toTime);
		}
		else
		{
			query.append("INSERT INTO SHIFTDET(REC_NO,SHIFT_KEY,DEF_HOURS) VALUES ('%s','%s','%s') ");
			return query.toString().format(query.toString(), obj.getShiftDetRecNo(),obj.getShiftKey(),obj.getUnits());
		}
	
	}
	
	public String checkIfShiftTSUsedQuery(int shiftKey)
	{
		query=new StringBuffer();
		query.append("SELECT count(*) as cnt FROM TSSETUP WHERE  POS_ID='%s'");
		return query.toString().format(query.toString(), shiftKey);
	}
	
	public String checkIfEMPSHIFTUsedQuery(int shiftKey)
	{
		query=new StringBuffer();
		query.append("SELECT count(*) as cnt FROM EMPSHIFT WHERE  SHIFT_KEY='%s'");
		return query.toString().format(query.toString(), shiftKey);
	}
	
	public String deleteShiftMasterQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("DELETE FROM SHIFTMAST WHERE SHIFT_KEY='%s'");
		return query.toString().format(query.toString(), obj.getShiftKey());
	}
	public String deleteCompanyShiftQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("DELETE FROM COMPSHIFT WHERE SHIFT_KEY='%s'");
		return query.toString().format(query.toString(), obj.getShiftKey());
	}
	public String deleteCompanyHolidayQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("DELETE FROM COMPHOLIDAYSDET WHERE SHIFT_KEY='%s'");
		return query.toString().format(query.toString(), obj.getShiftKey());
	}
	
	//save SetShifts
	public String deleteAllCompanyShiftQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append("DELETE FROM COMPSHIFT WHERE COMP_KEY='%s'");
		return query.toString().format(query.toString(), companyKey);
	}
	
	public String insertCompanyShiftQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
		String fromtime="NULL";
		String toTime="NULL";
		
			if(obj.getFromTime()!=null && obj.getToTime()!=null)
			{
				fromtime=tdf.format(obj.getFromTime());
				toTime=tdf.format(obj.getToTime());
			
		query.append("INSERT INTO COMPSHIFT(REC_NO,COMP_KEY,SHIFT_KEY,SHIFT_CODE,DAY_NO,HOURS,FROM_TIME,TO_TIME,OFFDAY,PAIDNORMAL,PAIDIFWORK) " +
				"						VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') ");
		return query.toString().format(query.toString(), obj.getRecNo(),obj.getCompKey(),obj.getShiftKey(),obj.getShiftCode(),obj.getDayNo(),obj.getUnits(),
						fromtime,toTime,obj.isOffDay()?"Y":"N" ,obj.isOffDay()?"Y":"","N" );
			}
			else
			{
				query.append("INSERT INTO COMPSHIFT(REC_NO,COMP_KEY,SHIFT_KEY,SHIFT_CODE,DAY_NO,HOURS,OFFDAY,PAIDNORMAL,PAIDIFWORK) " +
						"						VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s') ");
				return query.toString().format(query.toString(), obj.getRecNo(),obj.getCompKey(),obj.getShiftKey(),obj.getShiftCode(),obj.getDayNo(),obj.getUnits(),
								obj.isOffDay()?"Y":"N" ,obj.isOffDay()?"Y":"","N" );
			}				
	}
	
	//Holidays
	public String getCompanyHolidaysQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append(" SELECT REC_NO,COMP_KEY,HOLIDAY_TYPE,FROM_DATE,TO_DATE,TOTDAYS,HRLISTVALUES.DESCRIPTION 'holidayDesc'");
		query.append(" FROM  COMPHOLIDAYS INNER JOIN HRLISTVALUES ON HRLISTVALUES.ID = COMPHOLIDAYS.DESCRIPTION WHERE  COMP_KEY=" +companyKey);
		query.append(" order by HOLIDAY_TYPE desc ,FROM_DATE");
		return query.toString();
	}
	
	public String getCompanyHolidayDetailsQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append(" SELECT COMPHOLIDAYSDET.REC_NO, COMPHOLIDAYSDET.COMP_KEY, COMPHOLIDAYSDET.SHIFT_KEY,  SHIFTMAST.SHIFT_CODE ");
		query.append(" FROM   COMPHOLIDAYSDET INNER JOIN SHIFTMAST ON COMPHOLIDAYSDET.SHIFT_KEY = SHIFTMAST.SHIFT_KEY WHERE COMPHOLIDAYSDET.COMP_KEY=" +companyKey);		
		query.append(" ORDER BY SHIFT_KEY");
		return query.toString();
	}
	
	//OverTime
	public String getShiftMasterQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append(" SELECT REC_NO, COMP_KEY, SHIFT_KEY, UNIT_KEY, SHIFT_CODE, NO_OF_SHIFTS, TIMING_FLAG "); 	
		query.append(" FROM SHIFTMAST WHERE  COMP_KEY=" +companyKey);	
		query.append(" ORDER BY SHIFT_KEY");
		return query.toString();
	}
	public String getSalaryItemsHRListValuesQuery()
	{
		query=new StringBuffer();
		 query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID from HRLISTVALUES");
		 query.append(" where FIELD_ID in (30,31) ");
		 query.append(" order by DESCRIPTION");
		return query.toString();		
	}
	public String getOverTimeSetupQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append(" SELECT  TSSETUP.COMP_KEY, TSSETUP.REC_NO, POS_ID, UNIT_ID, NORMAL_HRS, MAX_OT, CALCULATE, CALC_HRS,SHIFT_CODE ");
		query.append(" FROM   TSSETUP INNER JOIN SHIFTMAST ON TSSETUP.POS_ID = SHIFTMAST.SHIFT_KEY WHERE TSSETUP.COMP_KEY=" +companyKey);		
		return query.toString();
	}
	
	public String getOverTimeCalculationQuery(int companyKey,int recNo)
	{
		query=new StringBuffer();
		query.append(" SELECT COMP_KEY, REC_NO, OT_NO, DAY_TYPE, OT_RATE, HOURS, AUTO_FILL, CALCULATE, SALARY_ITEM ");
		query.append(" FROM  OTCALCULATION WHERE COMP_KEY=" +companyKey + " and REC_NO=" + recNo);		
		return query.toString();
	}
	
	public String insertTSSetupQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("INSERT INTO TSSETUP(COMP_KEY,REC_NO,POS_ID,UNIT_ID,NORMAL_HRS,MAX_OT,CALCULATE,CALC_HRS) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s') ");
		return query.toString().format(query.toString(),obj.getCompKey(),obj.getRecNo(),obj.getShiftKey(),0,0,obj.getMaxOT(),obj.getCalculate(),obj.getCalcHours());		
	}
	
	public String insertOTCalculationQuery(ShiftModel obj)
	{
		query=new StringBuffer();
		query.append("INSERT INTO OTCALCULATION(COMP_KEY,REC_NO,OT_NO,DAY_TYPE,OT_RATE,HOURS,AUTO_FILL,CALCULATE,SALARY_ITEM) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s') ");
		return query.toString().format(query.toString(),obj.getCompKey(),obj.getRecNo(),obj.getOvertimeNo() ,obj.getDayType(),obj.getOtRate(),obj.getOtHours(),obj.getAutoFill(),obj.getCalculate(),obj.getSalaryItem());		
	}
	
	public String deleteTSSetupQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append("DELETE FROM TSSETUP WHERE COMP_KEY='%s'");
		return query.toString().format(query.toString(), companyKey);
	}
	public String deleteOTCalculationQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append("DELETE FROM OTCALCULATION WHERE COMP_KEY='%s'");
		return query.toString().format(query.toString(), companyKey);
	}
	
	public String updateTSCompanySetupQuery(CompSetupModel obj)
	{
		query=new StringBuffer();
		query.append("UPDATE COMPSETUP SET TIMESHEET_OTCHANGE='%s' ,TIMESHEET_TIMEAUTO ='%s' where  COMP_KEY='%s'");
		return query.toString().format(query.toString(), obj.getTimesheetOTChange(),obj.getTimesheetTimeAuto(), obj.getCompanyKey());
	}
}
