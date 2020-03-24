package timesheet;

import hr.HRQueries;
import hr.model.CompanyModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.CompSetupModel;
import model.CompanyDBModel;
import model.CompanyShiftModel;
import model.HRListValuesModel;
import model.ShiftModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ShiftCreationData
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	ShiftCreationQueries query=new ShiftCreationQueries();
	public ShiftCreationData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HRQueries query=new HRQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in ShiftCreationData---Init-->" , ex);
		}
	}
	public CompSetupModel getCompanySetup(int companyKey)
	{
		CompSetupModel obj=new CompSetupModel();			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCompanySetupQuery(companyKey));
			while(rs.next())
			{
				obj.setCompanyKey(companyKey);
				obj.setWeekStart(rs.getInt("WEEK_START"));
				obj.setDefHours(rs.getInt("DEF_HRS"));
				obj.setChangeHoliday(rs.getString("change_holiday")==null?"N":rs.getString("change_holiday"));
				obj.setIncludeholidayUnit(rs.getString("INCLUDEHOLIDAY_UNIT")==null?"Y":rs.getString("INCLUDEHOLIDAY_UNIT"));
				obj.setCanChangeHoliday(obj.getChangeHoliday().equals("Y")?true:false);
				obj.setCanIncludeholidayUnit(obj.getIncludeholidayUnit().equals("Y")?true:false);
				if(obj.getWeekStart()==0)
					obj.setWeekStart(7);
				obj.setTimesheetOTChange(rs.getString("TIMESHEET_OTCHANGE"));
				obj.setTimesheetTimeAuto(rs.getString("TIMESHEET_TIMEAUTO"));
				obj.setTimesheetChangeTime(rs.getString("TIMESHEET_CHANGETIME").equals("Y")?true:false);
				obj.setTimesheetCalculateExtraHours(rs.getString("TIMESHEET_CALCEXTRAHOURS").equals("Y")?true:false);
			}			
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getCompanySetup-->" , ex);
		}
		return obj;
	}
	public void updateCompanySetup(CompSetupModel obj)
	{
		try
		{			
			if(obj.isCanChangeHoliday())
				obj.setChangeHoliday("Y");
			else
				obj.setChangeHoliday("N");
			
			if(obj.isCanIncludeholidayUnit())
				obj.setIncludeholidayUnit("Y");
			else
			   obj.setIncludeholidayUnit("N");
			
			 db.executeUpdateQuery(query.updateCompanySetupQuery(obj));
			 
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---updateCompanySetup-->" , ex);
		}
		
	}
	public List<ShiftModel> getShiftTypeList()
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getShifTypeQuery());
			while(rs.next())
			{
				ShiftModel obj=new ShiftModel();
				obj.setUnitKey(rs.getInt("UNIT_KEY"));
				obj.setUnitName(rs.getString("UNIT_NAME"));
				obj.setTimingFlag(rs.getString("TIMING"));
				obj.setTimingShift(obj.getTimingFlag().equals("Y"));
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getShiftTypeList-->" , ex);
		}
		return lst;
	}
	public List<ShiftModel> getShiftList(int companyKey)
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;
		DateFormat hdf = new SimpleDateFormat("hh:mm a");
		int tmpRecNo=0;
		try 
		{
			rs=db.executeNonQuery(query.getShiftListQuery(companyKey));
			while(rs.next())
			{
				ShiftModel obj=new ShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setCompKey(rs.getInt("COMP_KEY"));	
				obj.setShiftKey(rs.getInt("SHIFT_KEY"));
				obj.setUnitKey(rs.getInt("UNIT_KEY"));
				obj.setShiftCode(rs.getString("SHIFT_CODE"));
				obj.setNoOfShifts(rs.getInt("NO_OF_SHIFTS"));								
				obj.setTimingFlag(rs.getString("TIMING_FLAG"));
				obj.setUnitName(rs.getString("UNIT_NAME"));		
				obj.setUnits(rs.getDouble("DEF_HOURS"));
				obj.setFromTime(rs.getDate("DEF_FROMTIME"));
				obj.setToTime(rs.getDate("DEF_TOTIME"));
				obj.setTimingShift(obj.getTimingFlag().equals("Y"));
				obj.setShiftDetRecNo(rs.getInt("shiftDetRecNo"));
				if(tmpRecNo!=obj.getRecNo() && obj.isTimingShift())
				{
					tmpRecNo=obj.getRecNo();
					obj.setFirstRecord(true);
				}
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getShiftList-->" , ex);
		}
		return lst;
	}
	
	public List<CompanyShiftModel> getCompanyShiftSetupList(int companyKey)
	{
		List<CompanyShiftModel> lst=new ArrayList<CompanyShiftModel>();			
		ResultSet rs = null;				
		try 
		{
			rs=db.executeNonQuery(query.getCompanyShiftSetupQuery(companyKey));
			while(rs.next())
			{
				CompanyShiftModel obj=new CompanyShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setComp_key(rs.getInt("COMP_KEY"));	
				obj.setShiftKey(rs.getInt("SHIFT_KEY"));
				obj.setDayNo(rs.getInt("Day_No"));
				obj.setOffDay(rs.getString("OFFDAY"));
				obj.setPaidIfWork(rs.getString("PAIDIFWORK"));
				obj.setPaidNormal(rs.getString("PaidNormal"));	
				obj.setFromTime(rs.getDate("FROM_TIME"));
				obj.setToTime(rs.getDate("TO_TIME"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getCompanyShiftSetupList-->" , ex);
		}
		return lst;
	}
	
	
	public int getMaxRexNO(String tableName,String columnName)
	{
		int recNo=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxRectQuery(tableName, columnName));
			while(rs.next())
			{
				recNo=rs.getInt(1);
			}
			recNo=recNo+1;
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getMaxRexNO-->" , ex);
		}
		return recNo;
	}
	
	public int insertShiftMaster(ShiftModel obj,boolean isDetail)
	{
		int result=0;			
		try 
		{						
			if(isDetail==false)
			{
			obj.setRecNo(getMaxRexNO("SHIFTMAST", "REC_NO"));
			obj.setShiftKey(getMaxRexNO("SHIFTMAST", "SHIFT_KEY"));	
			obj.setTimingFlag(obj.getShiftType().isTimingShift()?"Y" : "N");
			result=db.executeUpdateQuery(query.insertShiftMastQuery(obj));
			result=obj.getShiftKey();
			}
			else
			{				
				obj.setShiftDetRecNo(getMaxRexNO("SHIFTDET", "REC_NO"));
				result=db.executeUpdateQuery(query.insertShiftDetQuery(obj));
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---insertShiftMaster-->" , ex);
		}
		return result;		
	}
	
	public int updateShiftMaster(ShiftModel obj)
	{
		int result=0;			
		try 
		{						
			obj.setTimingFlag(obj.getShiftType().isTimingShift()?"Y" : "N");
			result=db.executeUpdateQuery(query.updateShiftMastQuery(obj));
			result=obj.getShiftKey();						
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---updateShiftMaster-->" , ex);
		}
		return result;		
	}
	
	public int deleteShiftDet(ShiftModel obj)
	{
		int result=0;			
		try 
		{									
			result=db.executeUpdateQuery(query.deleteShiftDetQuery(obj));							
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---deleteShiftDet-->" , ex);
		}
		return result;		
	}
	
	public boolean checkIfShiftUsed(int shiftKey)
	{
		int recNo=0;
		boolean isUsed=false;
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.checkIfShiftTSUsedQuery(shiftKey));
			while(rs.next())
			{
				recNo=rs.getInt(1);
			}
			if(recNo>0)
			{
				return true;
			}
			
			rs=db.executeNonQuery(query.checkIfEMPSHIFTUsedQuery(shiftKey));
			while(rs.next())
			{
				recNo=rs.getInt(1);
			}
			if(recNo>0)
			{
				return true;
			}
			
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---checkIfShiftUsed-->" , ex);
		}
		return isUsed;
	}
	
	public int deleteShift(ShiftModel obj)
	{
		int result=0;			
		try 
		{		
			db.executeUpdateQuery(query.deleteShiftMasterQuery(obj));	
			db.executeUpdateQuery(query.deleteShiftDetQuery(obj));	
			db.executeUpdateQuery(query.deleteCompanyShiftQuery(obj));
			db.executeUpdateQuery(query.deleteCompanyHolidayQuery(obj));
			
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---deleteShift-->" , ex);
		}
		return result;	
	}
	
	//save SetShifts
	public int deleteAllCompanyShift(int companyKey)
	{
		int result=0;			
		try 
		{		
			db.executeUpdateQuery(query.deleteAllCompanyShiftQuery(companyKey));							
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---deleteAllCompanyShift-->" , ex);
		}
		return result;	
	}
	
	public int insertCompanyShift(ShiftModel obj,CompSetupModel objCompanySetup)
	{
		int result=0;			
		try 
		{									
			obj.setRecNo(getMaxRexNO("COMPSHIFT", "REC_NO"));	
			if(objCompanySetup.isCanChangeHoliday()==false && obj.isOffDay())
			{
				obj.setFromTime(null);
				obj.setToTime(null);
			}
			
			result=db.executeUpdateQuery(query.insertCompanyShiftQuery(obj));								
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---insertCompanyShift-->" , ex);
		}
		return result;		
	}
	
	//Holidays
	public List<ShiftModel> getCompanyHolidays(int companyKey)
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		
		try 
		{
			rs=db.executeNonQuery(query.getCompanyHolidaysQuery(companyKey));
			while(rs.next())
			{
				ShiftModel obj=new ShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setCompKey(rs.getInt("COMP_KEY"));	
				obj.setHolidayType(rs.getString("HOLIDAY_TYPE").equals("F")?"Fixed":"Variant");
				obj.setHolidayDescription(rs.getString("holidayDesc"));
				obj.setFromDate(df.format(rs.getDate("FROM_DATE")));
				obj.setToDate(df.format(rs.getDate("TO_DATE")));
				obj.setTotalDays(rs.getInt("TOTDAYS"));		
				if(obj.getHolidayType().equals("Fixed"))
				{
					if(obj.getFromDate().length()>5)
					{
					obj.setFromDate(obj.getFromDate().substring(0, 5));
					obj.setToDate(obj.getToDate().substring(0, 5));
					}
				}
					lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getCompanyHolidays-->" , ex);
		}
		return lst;
	}
	
	public List<ShiftModel> getCompanyHolidayDetails(int companyKey)
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;		
		try 
		{
			rs=db.executeNonQuery(query.getCompanyHolidayDetailsQuery(companyKey));
			while(rs.next())
			{
				ShiftModel obj=new ShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setCompKey(rs.getInt("COMP_KEY"));	
				obj.setShiftKey(rs.getInt("SHIFT_KEY"));
				obj.setShiftCode(rs.getString("SHIFT_CODE"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getCompanyHolidayDetails-->" , ex);
		}
		return lst;
	}
	
	//OverTime
	public List<HRListValuesModel> getSalaryItemsHRListValues()
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();
		 		 
			HRListValuesModel obj=new HRListValuesModel();			
			ResultSet rs = null;
			try 
			{
			obj.setListId(0);
			obj.setEnDescription("Basic Salary");
			lst.add(obj);
			
				rs=db.executeNonQuery(query.getSalaryItemsHRListValuesQuery());
				while(rs.next())
				{
					obj=new HRListValuesModel();
					obj.setListId(rs.getInt("ID"));					
					obj.setEnDescription(rs.getString("DESCRIPTION"));
					obj.setArDescription(rs.getString("ARABIC"));
					obj.setSubId(rs.getInt("SUB_ID"));
					obj.setFieldId(rs.getInt("FIELD_ID"));
					obj.setFieldName(rs.getString("FIELD_NAME"));
					obj.setPriorityId(rs.getInt("PRIORITY_ID"));
					obj.setRequired(rs.getString("REQUIRED"));
					lst.add(obj);
				}
				
				obj=new HRListValuesModel();
				obj.setListId(-1);
				obj.setEnDescription("ALL");
				lst.add(obj);
			}
			catch (Exception ex) {
				logger.error("error in ShiftCreationData---getSalaryItemsHRListValues-->" , ex);
			}
		 return lst;
	}
	public List<ShiftModel> getShiftMasterList(int companyKey,String selectedViewType)
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;		
		try 
		{
			ShiftModel obj=new ShiftModel();
			if(selectedViewType.equals("1"))
			{
			obj.setShiftCode("EmploeeNo");
			lst.add(obj);
			obj=new ShiftModel();
			obj.setShiftCode("Name");
			lst.add(obj);
			}
			else if(selectedViewType.equals("2"))
			{
				obj.setShiftCode("Position");
				lst.add(obj);				
			}
			rs=db.executeNonQuery(query.getShiftMasterQuery(companyKey));
			while(rs.next())
			{
				obj=new ShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setCompKey(rs.getInt("COMP_KEY"));	
				obj.setShiftKey(rs.getInt("SHIFT_KEY"));
				obj.setShiftCode(rs.getString("SHIFT_CODE"));
				obj.setUnitKey(rs.getInt("UNIT_KEY"));
				obj.setNoOfShifts(rs.getInt("NO_OF_SHIFTS"));
				obj.setTimingFlag(rs.getString("TIMING_FLAG"));
				obj.setTimingShift(obj.getTimingFlag().equals("Y"));
				obj.setShiftHeaderChecked(false);
				lst.add(obj);
			}
			if(!selectedViewType.equals("0"))
			{
			obj=new ShiftModel();
			obj.setShiftCode("From");
			lst.add(obj);
			obj=new ShiftModel();
			obj.setShiftCode("To");
			lst.add(obj);
			}
												
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getShiftMasterList-->" , ex);
		}
		return lst;
	}
	
	public List<ShiftModel> getOverTimeSetup(int companyKey)
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;		
		try 
		{
			rs=db.executeNonQuery(query.getOverTimeSetupQuery(companyKey));
			while(rs.next())
			{
				ShiftModel obj=new ShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setCompKey(rs.getInt("COMP_KEY"));	
				obj.setShiftKey(rs.getInt("POS_ID"));
				obj.setShiftCode(rs.getString("SHIFT_CODE"));
				obj.setUnitId(rs.getInt("UNIT_ID"));
				obj.setNormalHours(rs.getInt("NORMAL_HRS"));
				obj.setMaxOT(rs.getInt("MAX_OT"));
				obj.setCalculate(rs.getString("CALCULATE"));
				obj.setCalcHours(rs.getDouble("CALC_HRS"));
				obj.setCalculateOT(obj.getCalculate().equals("Y")?true : false);
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getOverTimeSetup-->" , ex);
		}
		return lst;
	}
	
	public List<ShiftModel> getOverTimeCalculation(int companyKey,int recNo)
	{
		List<ShiftModel> lst=new ArrayList<ShiftModel>();			
		ResultSet rs = null;		
		try 
		{
			rs=db.executeNonQuery(query.getOverTimeCalculationQuery(companyKey,recNo));
			while(rs.next())
			{
				ShiftModel obj=new ShiftModel();
				obj.setRecNo(rs.getInt("REC_NO"));
				obj.setCompKey(rs.getInt("COMP_KEY"));	
				obj.setOvertimeNo(rs.getInt("OT_NO"));
				obj.setDayType(rs.getString("DAY_TYPE"));
				if(obj.getDayType().equals("N"))
					obj.setDayType("Normal Days");
				else if(obj.getDayType().equals("H"))
					obj.setDayType("Holidays");
				else
				   obj.setDayType("All");
				
				obj.setOtRate(rs.getDouble("OT_RATE"));
				obj.setOtHours(rs.getDouble("HOURS"));
				obj.setAutoFill(rs.getString("AUTO_FILL").equals("Y") ? "Yes" : "No");
				
				obj.setCalculate(rs.getString("CALCULATE").equals("Y") ? "Yes" : "No");
				obj.setSalaryItem((int) rs.getDouble("SALARY_ITEM"));
				//obj.setCalculateOT(obj.getCalculate().equals("Y")?true : false);
				lst.add(obj);
			}
			
			if(lst.size()==0)
			{
				ShiftModel obj=new ShiftModel();
				obj.setRecNo(recNo);
				obj.setCompKey(companyKey);
				obj.setOvertimeNo(1);
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---getOverTimeCalculation-->" , ex);
		}
		return lst;
	}
	
	public int insertTSSetup(ShiftModel obj)
	{
		int result=0;			
		try 
		{											
			obj.setRecNo(getMaxRexNO("TSSETUP", "REC_NO"));
			obj.setShiftKey(obj.getShiftType().getShiftKey());
			obj.setCalculate(obj.isCalculateOT()?"Y":"N");
			db.executeUpdateQuery(query.insertTSSetupQuery(obj));
			result=obj.getRecNo();
			
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---insertTSSetup-->" , ex);
		}
		return result;		
	}
	
	public int insertOTCalculation(ShiftModel obj)
	{
		int result=0;			
		try 
		{													
			if(obj.getDayType().equals("Normal Days"))
				obj.setDayType("N");
			else if(obj.getDayType().equals("Holidays"))
				obj.setDayType("H");
			else
			   obj.setDayType("");
			
			obj.setAutoFill(obj.getAutoFill().equals("Yes")?"Y":"N");
			obj.setCalculate(obj.getCalculate().equals("Yes")?"Y":"N");
			if(obj.getCalculate().equals("N"))
				obj.setSalaryItem(0);
			else
			obj.setSalaryItem(obj.getHrSalaryItem().getListId());
			db.executeUpdateQuery(query.insertOTCalculationQuery(obj));
			result=obj.getRecNo();
			
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---insertOTCalculation-->" , ex);
		}
		return result;		
	}
	public int deleteTSSetup(int companyKey,CompSetupModel obj)
	{
		int result=0;			
		try 
		{	
			db.executeUpdateQuery(query.updateTSCompanySetupQuery(obj));	
			db.executeUpdateQuery(query.deleteTSSetupQuery(companyKey));	
			db.executeUpdateQuery(query.deleteOTCalculationQuery(companyKey));				
		}
		catch (Exception ex) {
			logger.error("error in ShiftCreationData---deleteTSSetup-->" , ex);
		}
		return result;	
	}
}
