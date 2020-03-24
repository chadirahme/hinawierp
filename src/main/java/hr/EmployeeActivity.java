package hr;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hr.model.LeaveParamsModel;

import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

public class EmployeeActivity 
{

	private Logger logger = Logger.getLogger(EmployeeActivity.class);
	
	//DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	StringBuffer query;
	
	private LeaveParamsModel defLeavePARMS;
	Date clsOPBALEffDate;
	Date OpeningBalanceDate;
	double totalLeaveDaysUsed=0;
	private Date tmpJoiningDate;
	private String NAT_FLAG="";
	private Date LeaveCalcFromDate;
	
	private double tmpTotalYrs4mJoining;
	private String transNextYear="";
	private int Line_No=0;
	private int Rec_No=0;
	
	private double TotalAbsenceDeductionDays=0;
	private boolean DeductAbsenceDays=false;
	private Date DEDUCTABSENCEDATE=null;
	private String EXCLUDEANNUALLEAVEDAYS="";
	
	double TotalAdjustmentDays=0;
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	
	public EmployeeActivity()
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
			logger.error("error in HRData---Init-->" , ex);
		}
	}
	
	public double GetLeaveBalanceDays(int empKey,int leaveId,Date leaveStartDate,int compKey)
	{
		double totalDays=0;
		try 
		{
			if(empKey==0)
				return 0;
			
			String tmpPayMode="";
			double TotalAllowedLeaveDays=0;
			double CalcLeaveBalanceDays=0;
			
			double tmpTotServiceDedDays=0;
			double tmpTotalUnpaidDays=0;
			GetDefLeavePARAMS();
			getEmployeeInfo(empKey);
			getCompnayInfo(compKey);
		
			double OpeningBalanceDays=GetOpeningBAL(empKey, "L", leaveId);
			OpeningBalanceDate=clsOPBALEffDate;
			
			//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
			ResultSet rs = null;
			
			String sqlQuery=BuildSQL4GettingLEAVESetup(empKey, leaveId, leaveStartDate);//nationality problem
			
			
			rs=db.executeNonQuery(sqlQuery);
			query=new StringBuffer();
			while(rs.next())
			{
				transNextYear=rs.getString("TRANS_NEXT_YEAR");	//getting multiple records added break;
				Line_No=rs.getInt("Line_No");
				Rec_No=rs.getInt("Rec_NO");
				break;
			}
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(leaveStartDate);
			query=new StringBuffer();
			query.append(" SELECT SUM(ACTUAL_DAYS) AS DaysUsed FROM EMPLEAVE WHERE EMP_KEY=" + empKey);
			query.append(" AND EMPLEAVE.STATUS <>'V' AND LEAVE_ID=" + leaveId);
			if(transNextYear.equals("N"))
			{
				query.append(" And (Year(FROM_DATE) =" +cal.get(Calendar.YEAR));
				query.append(" or Year(TO_DATE) = " + cal.get(Calendar.YEAR) + ")");
			}
			else
			{
				query.append(" AND FROM_DATE <='" + sdf.format(leaveStartDate) + "'");
			}
			rs=null;
			rs=db.executeNonQuery(query.toString());
			while(rs.next())
			{
				totalLeaveDaysUsed=rs.getDouble("DaysUsed");
			}
			
			if(tmpJoiningDate!=null)
			{
				int[] empPeriod=CalculatePeriod(tmpJoiningDate, leaveStartDate);
				tmpTotalYrs4mJoining=empPeriod[2]  +(double)empPeriod[1]/12 + (double)empPeriod[0]/365;
				DecimalFormat df = new DecimalFormat("#.##");				
				logger.info("tmpTotalYrs4mJoining>>> "  +df.format(tmpTotalYrs4mJoining));
				
			}
			
			if(transNextYear.equals("Y"))
			{
				LeaveCalcFromDate=tmpJoiningDate;
			}
			else if(transNextYear.equals("N"))
			{
				Calendar calStart=Calendar.getInstance();
				calStart.setTime(leaveStartDate);
				
				Calendar cal2=Calendar.getInstance();
				cal2.set(calStart.get(Calendar.YEAR), 0, 1);
				LeaveCalcFromDate =cal2.getTime();
			}
			
			TotalAbsenceDeductionDays=GetABSDeductionDays(transNextYear, empKey, leaveId);
			logger.info("TotalAbsenceDeductionDays>>> "  +TotalAbsenceDeductionDays);			
			
		//	LeaveCalcFromDate = tmpJoiningDate;
			if(LeaveCalcFromDate.before(tmpJoiningDate))
			{
				LeaveCalcFromDate = tmpJoiningDate;
			}
			
			 tmpTotServiceDedDays = TotalServicePeriodDeduction(empKey, compKey, leaveStartDate);
			 logger.info("tmpTotServiceDedDays>>> "  +tmpTotServiceDedDays);
				
			 
			 tmpTotalUnpaidDays=GetOpeningUnpaidDays(empKey);
			 logger.info("tmpTotalUnpaidDays>>> "  +tmpTotalUnpaidDays);
				
			 
			 if(tmpTotServiceDedDays + tmpTotalUnpaidDays > 0)
			 {
				 Calendar calStart=Calendar.getInstance();
			     calStart.setTime(LeaveCalcFromDate);
			     //calStart.add(Calendar.DAY_OF_MONTH, tmpTotServiceDedDays + tmpTotalUnpaidDays);
			     LeaveCalcFromDate=calStart.getTime();
			 }
			 
			 int[] empPeriod=CalculatePeriod(LeaveCalcFromDate, leaveStartDate);
			 int TotalServicePeriodYrs =empPeriod[2];
		     int TotalServicePeriodMonths =empPeriod[1];
		     int TotalServicePeriodDays =empPeriod[0];
		     
		     double tmpDaysWithoutPay=GetLeaveWitoutPay(empKey);
		     logger.info("tmpDaysWithoutPay>>> "  +tmpDaysWithoutPay);
			
		     
		     int tmpOpeningYears=0, tmpOpeningMonths=0, tmpOPeningDays=0;
		     
		     if(OpeningBalanceDays>0 && clsOPBALEffDate!=null)
		     {
		    	 if(tmpTotServiceDedDays>0)
		    	 {
		    		 Calendar calStart=Calendar.getInstance();
				     calStart.setTime(clsOPBALEffDate);
				     //calStart.add(Calendar.DAY_OF_MONTH, tmpTotServiceDedDays);
		    		 clsOPBALEffDate=calStart.getTime();
		    	 }
		    	 int[] empNewPeriod=CalculatePeriod(clsOPBALEffDate, leaveStartDate);
		    	 tmpOPeningDays=empNewPeriod[0];
		    	 tmpOpeningMonths=empNewPeriod[1];
		    	 tmpOpeningYears=empNewPeriod[2];
		     }
		     
		     double tmpLeaveDays=0;
		     double tmpLeaveDuration=0;
		     String tmpDurationMode="";
		     double tmpOpeningLeaveDuration=0;
		     double tmpLeaveBetween=0;
		     
		     double tmpMinWorkPeriod=0;
		     String tmpMinWorkFlag="";
		     double tmpWorkDuration=0;
		     
		     logger.info("transNextYear>>> "  +transNextYear);
		     if(transNextYear.equals("Y"))
		     {
		    	 query=new StringBuffer();
		    	 query.append(" Select MIN_WORK_PERIOD,MIN_WORK_FLAG from LEAVESETUP Where ");
		    	 query.append(" Line_No=" + Line_No);
		    	 query.append(" And Rec_No=" + Rec_No);
		    	 rs=null;
		    	 rs=db.executeNonQuery(query.toString());
		    	 while(rs.next())
		    	 {
		    		 tmpMinWorkPeriod=rs.getDouble("MIN_WORK_PERIOD");
		    		 tmpMinWorkFlag=rs.getString("MIN_WORK_FLAG");
		    	 }
		    	 DecimalFormat df = new DecimalFormat("#.##");
		    	 if(tmpMinWorkFlag.equals("M"))
		    	 {
		    		 tmpWorkDuration=tmpMinWorkPeriod/12;
		    		 tmpWorkDuration=Double.valueOf(df.format(tmpWorkDuration));		    		 		    		 
		    	 }
		    	 else if(tmpMinWorkFlag.equals("D"))
		    	 {
		    		 tmpWorkDuration=tmpMinWorkPeriod/365;
		    		 tmpWorkDuration=Double.valueOf(df.format(tmpWorkDuration));		    		 		    		 
		    	 }
		    	 else
		    	 {
		    		 tmpWorkDuration = tmpMinWorkPeriod;
		    	 }
		    	 
		    	 if(tmpWorkDuration>tmpTotalYrs4mJoining)
		    	 {
		    		 Messagebox.show("Leave is allowed for this employee after " + tmpMinWorkPeriod + " month(s) Please Check Setup");
		    		 return 0;
		    	 }
		     
		     
		     TotalAdjustmentDays=GetAdjustmentDays(empKey, leaveId, leaveStartDate);
		    
				
		     
		     query=new StringBuffer();
		     query.append(" SELECT * FROM LEAVECALCULATION Where ");
	    	 query.append(" CALC_ROW ='Y' ");
	    	 query.append(" And Rec_No=" + Rec_No);
	    	 rs=null;
	    	 rs=db.executeNonQuery(query.toString());
	    	 boolean isFound=false;
		     while(rs.next())
		     {
		    	 isFound=true;
		    	double Duration_Period= rs.getDouble("Duration_Period");
		    	String DURATION_MODE=rs.getString("DURATION_MODE");
		    	if(Duration_Period>0 && DURATION_MODE.length()>0)
		    	{
		    		tmpLeaveDays=rs.getDouble("Days_Allowed");
		    		tmpLeaveDuration=rs.getDouble("Duration_Period");
		    		tmpDurationMode=rs.getString("DURATION_MODE");
		    		
		    		if(tmpDurationMode.equals("M"))
		    		{
		    			 tmpLeaveDuration = TotalServicePeriodMonths;
		    			 if(TotalServicePeriodYrs!=0)
		    			 {
		    				 tmpLeaveDuration = tmpLeaveDuration + (Math.round(TotalServicePeriodYrs) * 12);
		    			 }
		    			 if(TotalServicePeriodDays!=0)
		    			 {
		    				 tmpLeaveDuration = tmpLeaveDuration + Math.round(TotalServicePeriodDays/ 30);
		    			 }
		    			 
		    			 tmpOpeningLeaveDuration = tmpOpeningMonths;
		    			 if(tmpOpeningYears!=0)
		    			 {
		    				 tmpOpeningLeaveDuration = tmpOpeningLeaveDuration + Math.round(tmpOpeningYears * 12);
		    			 }
		    			 if(tmpOPeningDays!=0)
		    			 {
		    				 tmpOpeningLeaveDuration = tmpOpeningLeaveDuration + Math.round(tmpOPeningDays/ 30);
		    			 }
		    		}
		    		else if(tmpDurationMode.equals("Y"))
		    		{
		    			 tmpLeaveDuration = (TotalServicePeriodYrs) + ((double)TotalServicePeriodMonths/ 12) + ((double)TotalServicePeriodDays/ 365);
		    			 tmpOpeningLeaveDuration = tmpOpeningYears +(tmpOpeningMonths/ 12) + (tmpOPeningDays/ 365);
		    			 
		    		}
		    		else if(tmpDurationMode.equals("D"))
		    		{
		    			tmpLeaveDuration = TotalServicePeriodDays;
		    			if(TotalServicePeriodYrs!=0)
		    			{
		    				tmpLeaveDuration = tmpLeaveDuration + (Math.round(TotalServicePeriodYrs) * 365);
		    			}
		    			if(TotalServicePeriodMonths!=0)
		    			{
		    				tmpLeaveDuration = tmpLeaveDuration + (Math.round(TotalServicePeriodMonths * 30));
		    			}
		    			
		    			tmpOpeningLeaveDuration = tmpOPeningDays;
		    			if(tmpOpeningYears!=0)
		    			{
		    				 tmpOpeningLeaveDuration = tmpOpeningLeaveDuration + (Math.round(tmpOpeningYears) * 365);
		    			}
		    			if(tmpOpeningMonths!=0)
		    			{
		    				 tmpOpeningLeaveDuration = tmpOpeningLeaveDuration + (Math.round(tmpOpeningMonths) * 30);
		    			}
		    		}
		    		
		    	}
		    	
		    	logger.info("tmpLeaveDuration>>> "  +tmpLeaveDuration);
		    	logger.info("tmpOpeningLeaveDuration>>> "  +tmpOpeningLeaveDuration);
		    	
		    	
		    	if(tmpDurationMode.equals("M"))
		    	{
		    		tmpLeaveBetween = tmpLeaveDuration;
		    	}
		    	else if(tmpDurationMode.equals("Y"))
		    	{
		    		tmpLeaveBetween = tmpLeaveDuration * 12;
		    	}
		    	
		    	query=new StringBuffer();
		    	query.append(" Select * from LEAVECALCULATION Where REC_NO=" + Rec_No);
		    	query.append(" And (("+ tmpLeaveBetween +" Between Month_From      And Month_To) OR");
		    	query.append(" (Month_From <=" + tmpLeaveBetween + " And Month_TO =0) Or");
		    	query.append(" (Month_From  =1   And  Month_To >=" + tmpLeaveBetween + "))");
		    	ResultSet rs2=null;
		    	rs2=db.executeNonQuery(query.toString());
		    	while(rs2.next())
		    	{
		    		tmpLeaveDays=rs2.getDouble("Days_Allowed");
		    		tmpPayMode=rs2.getString("DURATION_MODE");
		    	}
		    	
		    	if(tmpPayMode.equals("M"))
		    	{
		    		tmpLeaveDuration=tmpLeaveDuration*tmpLeaveDays;
		    	}
		    	else
		    	{
		    		tmpLeaveDuration=tmpLeaveDuration*tmpLeaveDays;
		    	}
		    	
		    	TotalAllowedLeaveDays = Math.round(tmpLeaveDuration);
		    	
		    	
		    	if(OpeningBalanceDays>0 && clsOPBALEffDate!=null)//problem was here with >= which is made to just >
		    	{
		    		tmpOpeningLeaveDuration = tmpOpeningLeaveDuration *tmpLeaveDays;
		    		CalcLeaveBalanceDays = Math.round(tmpOpeningLeaveDuration) + OpeningBalanceDays;
		    	}
		    	else
		    	{
		    		CalcLeaveBalanceDays = TotalAllowedLeaveDays;
		    	}
		     }
		     
		     if(isFound==false)
		     {
		    	 TotalAllowedLeaveDays = 0;
	            CalcLeaveBalanceDays =TotalAdjustmentDays;
		     }
		     
		   }
		     else
		     {
		    	 //'ALLOWED = REQUIRED
		    	 query=new StringBuffer();
		    	 query.append(" Select MIN_WORK_PERIOD,MIN_WORK_FLAG from LEAVESETUP Where ");
		    	 query.append(" Line_No=" + Line_No);
		    	 query.append(" And Rec_No=" + Rec_No);
		    	 rs=null;
		    	 rs=db.executeNonQuery(query.toString());
		    	 while(rs.next())
		    	 {
		    		 tmpMinWorkPeriod=rs.getDouble("MIN_WORK_PERIOD");
		    		 tmpMinWorkFlag=rs.getString("MIN_WORK_FLAG");
		    	 }
		    	 if(tmpMinWorkFlag.equals("M"))
		    	 {
		    		 tmpWorkDuration=tmpMinWorkPeriod/12;		    		 		    		 		    		
		    	 }
		    	 else if(tmpMinWorkFlag.equals("D"))
		    	 {
		    		 tmpWorkDuration=tmpMinWorkPeriod/365;		    		 		    		 		    		
		    	 }
		    	 else
		    	 {
		    		 tmpWorkDuration = tmpMinWorkPeriod;
		    	 }
		    	 
		    	 if(leaveId!=0)
		    	 {
		    		 if(tmpWorkDuration>tmpTotalYrs4mJoining)
		    		 {
		    			 //Messagebox.show("Leave is allowed for this employee after " + tmpMinWorkPeriod + " Please Check Setup");
		    			 Messagebox.show("Leave is allowed for this employee after " + tmpMinWorkPeriod + " month(s) Please Check Setup");
			    		 return 0;
		    		 }
		    	 }
		    		 
		    		 TotalAdjustmentDays=GetAdjustmentDays(empKey, leaveId, leaveStartDate);
		    		 logger.info("TotalAdjustmentDays>>> "  +TotalAdjustmentDays);
		    		
		    		 
				     query=new StringBuffer();
				     query.append(" SELECT * FROM LEAVECALCULATION Where ");
			    	 query.append(" CALC_ROW ='Y' ");
			    	 query.append(" And Rec_No=" + Rec_No);
			    	 rs=null;
			    	 rs=db.executeNonQuery(query.toString());
			    	 while(rs.next())
			    	 {
			    		// tmpLeaveDays=rs.getDouble("Days_Allowed");
			    		 tmpPayMode=rs.getString("DURATION_MODE");
			    		 Calendar calNow=Calendar.getInstance();				    		 
			    		 Calendar cal2=Calendar.getInstance();
					     cal2.set(calNow.get(Calendar.YEAR), 1, 1);
					     Calendar calStartDate=Calendar.getInstance();
					     calStartDate.setTime(leaveStartDate);
			    		 if(leaveId==100 && tmpJoiningDate.after(cal2.getTime()) && calNow.get(Calendar.YEAR)==calStartDate.get(Calendar.YEAR))
			    		 {
			    			int[] r= CalculatePeriod(LeaveCalcFromDate, leaveStartDate);
			    			int tmpNetYears=r[2];
			    			int tmpNetMonths=r[1];
			    			int tmpNetDays=r[0];
			    			tmpLeaveDuration=FindTotalLeaveDuration(tmpNetYears, tmpNetMonths, tmpNetDays, tmpPayMode);
			    			if(tmpPayMode.equals("M"))
			    			{
			    				tmpLeaveBetween = tmpLeaveDuration;
			    				TotalAllowedLeaveDays=Math.round(tmpLeaveBetween * tmpLeaveDays);
			    				
			    			}
			    			else if (tmpPayMode.equals("Y"))
			    			{
			    				 tmpLeaveBetween = tmpLeaveDuration * 12;
			    				 TotalAllowedLeaveDays=Math.round(tmpLeaveBetween * tmpLeaveDays/12);
			    			}
			    			
			    			CalcLeaveBalanceDays=TotalAllowedLeaveDays;
			    			 
			    		 }
			    		 else
			    		 {
			    			 //'Other Type of leaves
			    			 int[] r= CalculatePeriod(LeaveCalcFromDate, leaveStartDate);
			    			 int tmpNetYears=r[2];
			    			 int tmpNetMonths=r[1];
				    		 int tmpNetDays=r[0];
				    		 tmpLeaveDuration=FindTotalLeaveDuration(tmpNetYears, tmpNetMonths, tmpNetDays, tmpPayMode);
				    		 if(tmpPayMode.equals("M"))
				    			{
				    				tmpLeaveBetween = tmpLeaveDuration;				    								    				
				    			}
				    			else if (tmpPayMode.equals("Y"))
				    			{
				    				 tmpLeaveBetween = tmpLeaveDuration * 12;				    				 
				    			}
				    		 
				    		 query=new StringBuffer();
				    		 query.append(" Select * from LEAVECALCULATION Where REC_NO=" + Rec_No);
						     query.append(" And (("+ tmpLeaveBetween +" Between Month_From      And Month_To) OR");
						     query.append(" (Month_From <=" + tmpLeaveBetween + " And Month_TO =0) Or");
						     query.append(" (Month_From  =1   And  Month_To >=" + tmpLeaveBetween + "))");
						    ResultSet rs2=null;
						    rs2=db.executeNonQuery(query.toString());
						     while(rs2.next())
						     {
						    	 tmpLeaveDays=rs2.getDouble("Days_Allowed");
						    	 tmpPayMode=rs2.getString("DURATION_MODE");
						     }
						     
						     if(tmpPayMode.equals("M"))
						     {
						    	 TotalAllowedLeaveDays = tmpLeaveDays * 12;
						     }
						     else
						     {
						    	 TotalAllowedLeaveDays = tmpLeaveDays;
						     }
						     
						     CalcLeaveBalanceDays = TotalAllowedLeaveDays;
				    		 
			    		 }
			    	 }
			    	 
			    	 if(OpeningBalanceDays>0 && clsOPBALEffDate!=null)
			    	 {
			    		 Calendar cal1=Calendar.getInstance();				    		 
			    		 Calendar cal2=Calendar.getInstance();
			    		 cal1.setTime(clsOPBALEffDate);
			    		 cal2.setTime(leaveStartDate);
			    		 if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
			    		 {
			    			 CalcLeaveBalanceDays = OpeningBalanceDays;
			    		 }
			    		 else
			    		 {
			    			 CalcLeaveBalanceDays = CalcLeaveBalanceDays;
			    		 }
			    	 }
		    	 }
		     
		     logger.info("CalcLeaveBalanceDays>>> "  +CalcLeaveBalanceDays);
		     logger.info("TotalAdjustmentDays>>> "  +TotalAdjustmentDays);
		     logger.info("totalLeaveDaysUsed>>> "  +totalLeaveDaysUsed);
		     logger.info("TotalAbsenceDeductionDays>>> "  +TotalAbsenceDeductionDays);
		     
		     totalDays = Math.round(CalcLeaveBalanceDays) + TotalAdjustmentDays - totalLeaveDaysUsed - TotalAbsenceDeductionDays;
		 	logger.info("totalDays>>> "  +totalDays);	 		    	 		     		     		     		    
		}
		catch (Exception ex) {
			logger.error("error in EmployeeActivity---GetLeaveBalanceDays-->" , ex);
		}
		return totalDays;
	}
	private double FindTotalLeaveDuration(int SYears,int SMonths,int Sdays,String strDuration)
	{
		double tmpLDuration=0;
		if(strDuration.equals("M"))
		{
			 tmpLDuration = SMonths;
			 if(SYears!=0)
			 {
				 tmpLDuration = tmpLDuration + (Math.round(SYears) * 12);
			 }
			 if(Sdays!=0)
			 {
				 tmpLDuration = tmpLDuration + Math.round(Sdays / 30);
			 }
		}
		else if(strDuration.equals("Y"))
		{
			  tmpLDuration = SYears + Math.round(((double) SMonths / 12) + ((double) Sdays / 365));
		}
		else if(strDuration.equals("D"))
		{
			   tmpLDuration = Sdays;
			   if(SYears!=0)
			   {
				   tmpLDuration = tmpLDuration + (Math.round(SYears) * 365);
			   }
			   if(SMonths!=0)
			   {
				   tmpLDuration = tmpLDuration + (Math.round(SMonths* 30));
			   }			 
		}
		return tmpLDuration;
		
	}
	private void getEmployeeInfo(int empKey)
	{
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		ResultSet rs = null;
		try 
		{			
			String sqlqQuery="Select Comp_Key,Employeement_Date,NAT_FLAG From EMPMAST Where  EMP_KEY="+empKey;
			
			rs=db.executeNonQuery(sqlqQuery);			
			while(rs.next())
			{														
				tmpJoiningDate=rs.getDate("Employeement_Date");
				NAT_FLAG=rs.getString("NAT_FLAG");
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---getEmployeeInfo-->" , ex);
		}			
	}
	private void getCompnayInfo(int compKey)
	{
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		ResultSet rs = null;
		try 
		{			
			String sqlqQuery="Select  DEDUCTABSENCEDAYS4TS,DEDUCTABSENCEDATE,EXCLUDEANNUALLEAVEDAYS From COMPSETUP Where COMP_KEY="+compKey;
			
			rs=db.executeNonQuery(sqlqQuery);			
			while(rs.next())
			{														
				String 	dEDUCTABSENCEDAYS4TS=rs.getString("DEDUCTABSENCEDAYS4TS");
				DEDUCTABSENCEDATE=rs.getDate("DEDUCTABSENCEDATE");
				EXCLUDEANNUALLEAVEDAYS=rs.getString("EXCLUDEANNUALLEAVEDAYS");
				if(dEDUCTABSENCEDAYS4TS.equals("Y") || dEDUCTABSENCEDAYS4TS.equals(""))
				{
					DeductAbsenceDays=true;
				}
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---getEmployeeInfo-->" , ex);
		}			
	}
	
	private void GetDefLeavePARAMS()
	{
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		ResultSet rs = null;
		defLeavePARMS=new LeaveParamsModel();
			try 
			{			
				String sqlqQuery="Select * from HRCOLUMNS Where HRCOLUMNS.ACTIVITY    = 'LEAVE' Order by COLUMN_ID,REC_NO";				
				rs=db.executeNonQuery(sqlqQuery);			
				while(rs.next())
				{	
					if(rs.getString("FIELD_NAME").equals("LEVEL_ID"))
					{
						defLeavePARMS.setLevelID(rs.getString("DEF_VALUE"));						
					}
					else if(rs.getString("FIELD_NAME").equals("DEP_ID"))
					{
						defLeavePARMS.setDepID(rs.getString("DEF_VALUE"));
					}					
					else if(rs.getString("FIELD_NAME").equals("POS_ID"))
					{
						defLeavePARMS.setPosID(rs.getString("DEF_VALUE"));
					}	
					else if(rs.getString("FIELD_NAME").equals("SECTION_ID"))
					{
						defLeavePARMS.setSecID(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("CLASS_ID"))
					{
						defLeavePARMS.setClassID(rs.getString("DEF_VALUE"));
					}	
					else if(rs.getString("FIELD_NAME").equals("EMP_TYPE"))
					{
						defLeavePARMS.setEmpType(rs.getString("DEF_VALUE"));
					}	
					else if(rs.getString("FIELD_NAME").equals("SEX_ID"))
					{
						defLeavePARMS.setSexID(rs.getString("DEF_VALUE"));
					}	
					else if(rs.getString("FIELD_NAME").equals("MARITAL_ID"))
					{
						defLeavePARMS.setMaritalID(rs.getString("DEF_VALUE"));
					}	
					else if(rs.getString("FIELD_NAME").equals("NATIONALITY"))
					{
						defLeavePARMS.setNationality(rs.getString("DEF_VALUE"));
					}	
					else if(rs.getString("FIELD_NAME").equals("RELIGION_ID"))
					{
						defLeavePARMS.setReligion(rs.getString("DEF_VALUE"));
					}					
										
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in EmployeeActivity---GetDefLeavePARAMS-->" , ex);
			}				
	}
	private double GetOpeningBAL(int empKey,String activity,int activityId)
	{
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		ResultSet rs = null;
		double openBalance=0;
			try 
			{			
				String sqlqQuery="SELECT OUTSTANDING,EFF_DATE FROM OPENINGBAL WHERE EMP_KEY="+empKey + " AND ACTIVITY ='" + activity + "' AND ACTIVITY_ID=" + activityId;
				
				rs=db.executeNonQuery(sqlqQuery);			
				
				if(rs.next())
				{
					while(rs.next())
					{										
						openBalance= rs.getDouble("Outstanding");	
						clsOPBALEffDate=rs.getDate("eff_date");
					}
				}
				//added by iqbal 
				else
				{
					String sqlqQuery1="Select EMPLOYEEMENT_DATE from empmast where EMP_KEY= " + empKey;
					rs=db.executeNonQuery(sqlqQuery1);
					while(rs.next())
					{										
						//openBalance= rs.getDouble("Outstanding");	
						clsOPBALEffDate=rs.getDate("EMPLOYEEMENT_DATE");
					}
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in EmployeeActivity---GetOpeningBAL-->" , ex);
			}				
		return openBalance;
	}
	
	private String BuildSQL4GettingLEAVESetup(int empKey,int leaveId,Date effDate)
	{
		String result="";
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		ResultSet rs = null;
		try
		{
			query=new StringBuffer();
			query.append(" Select EMPMAST.*,(Select Count(*) from EMPFAMILY Where EMPFAMILY.EMP_KEY =EMPMAST.EMP_KEY And Relation_ID in (37,43)) as KidsNos,");
			query.append(" (Select Min(DateDiff(Year,EMPFAMILY.BIRTH_DATE,'" +sdf.format(effDate) +  "'))");
			query.append(" from EMPFAMILY Where EMPFAMILY.EMP_KEY = EMPMAST.EMP_KEY And Relation_ID in (37,43) And EMPFAMILY.Birth_Date<>NULL) As KidsMinAge,");
			query.append(" (Select Max(DateDiff(Year,EMPFAMILY.BIRTH_DATE,'" + sdf.format(effDate) + "')) from EMPFAMILY ");
			query.append(" where EMPFAMILY.EMP_KEY=EMPMAST.EMP_KEY And Relation_ID in (37,43) And EMPFAMILY.Birth_Date<>NULL) As KidsMaxAge,");
			query.append(" (Select Distance from EMPSPOUSE Where EMPSPOUSE.EMP_KEY = EMPMAST.EMP_KEY) as SPOUSEDISTANCE");
			query.append(" from EMPMAST Where EMP_KEY=" +empKey);
			
			rs=db.executeNonQuery(query.toString());
			query=new StringBuffer();
			while(rs.next())
			{
				int compKey=rs.getInt("COMP_KEY");
				query.append(" Select Distinct LEAVESETUP.* from LEAVESETUP Where LEAVESETUP.COMP_KEY =" + compKey);
				query.append(" AND LEAVESETUP.LEAVE_KEY =" +leaveId );
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'Level_ID'  And [Value] in ('" + rs.getString("CLEVEL") + "','" + defLeavePARMS.getLevelID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'DEP_ID'  And [Value] in ('" + rs.getString("DEP_ID") + "','" + defLeavePARMS.getDepID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'POS_ID'  And [Value] in ('" + rs.getString("POS_ID") + "','" + defLeavePARMS.getPosID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'SECTION_ID'  And [Value] in ('" + rs.getString("SECTION_ID") + "','" + defLeavePARMS.getSecID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'CLASS_ID'  And [Value] in ('" + rs.getString("CLASS_ID") + "','" + defLeavePARMS.getClassID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'EMP_TYPE'  And [Value] in ('" + rs.getString("EMP_TYPE") + "','" + defLeavePARMS.getEmpType()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'SEX_ID'  And [Value] in ('" + rs.getString("SEX_ID") + "','" + defLeavePARMS.getSexID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'MARITAL_ID'  And [Value] in ('" + rs.getString("MARITAL_ID") + "','" + defLeavePARMS.getMaritalID()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'NATIONALITY'  And [Value] in ('" + rs.getString("NAT_FLAG") + "','" + defLeavePARMS.getNationality()+"'))");
				query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =" +compKey + " And SETUPCONDITIONS.Activity = 'LEAVE'  + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'RELIGION_ID'  And [Value] in ('" + rs.getString("RELIGION_ID") + "','" + defLeavePARMS.getReligion()+"'))");
								
				query.append(" And LEAVESETUP.EFF_DATE = (Select Max(Eff_Date) from LEAVESETUP LEAVESETUP1 Where");
				query.append(" LEAVESETUP1.EFF_DATE   <= '" + sdf.format(effDate) + "' and ");
				query.append(" LEAVESETUP1.COMP_KEY    = LEAVESETUP.COMP_KEY And LEAVESETUP1.LEAVE_KEY   = LEAVESETUP.LEAVE_KEY) ");
				query.append(" ");
				query.append(" ");
								
			}
			result=query.toString();
			
			
			
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---BuildSQL4GettingLEAVESetup-->" , ex);
		}	
		return result;
	}

	private int[] CalculatePeriod(Date startPeriod,Date endPeriod)
	{
		int retDay=0;
		int retMonth=0;
		int retYear=0;
		int[] result=new int[3];
		
		Calendar calStart=Calendar.getInstance();
		calStart.setTime(startPeriod);
		Calendar calEnd=Calendar.getInstance();
		calEnd.setTime(endPeriod);
		
		int year1=calStart.get(Calendar.YEAR);
		int year2=calEnd.get(Calendar.YEAR);
		
		int month1=calStart.get(Calendar.MONTH);
		int month2=calEnd.get(Calendar.MONTH);
		
		int day1=calStart.get(Calendar.DAY_OF_MONTH);
		int day2=calEnd.get(Calendar.DAY_OF_MONTH);
		
		if(day2<day1)
		{
			int temp=calEnd.getActualMaximum(Calendar.DAY_OF_MONTH);
			month2=month2-1;
			day2=day2+temp;
			retDay=day2-day1;			
		}
		else
		{
			retDay=day2-day1;
		}
		
		if(month2<month1)
		{
			year2=year2-1;
			month2=month2 + 12;
			retMonth=month2-month1;
		}
		else
		{
			retMonth=month2-month1;
		}
		
		retYear=year2-year1;		
		
		if(retDay>1)
			retDay-=1;
		
		result[0]=retDay;
		result[1]=retMonth;
		result[2]=retYear;
		logger.info("day " + retDay);
		logger.info("month " + retMonth);
		logger.info("year " + retYear);
		return result;
	}
	
	private double GetABSDeductionDays(String transNextYear,int empKey,int leaveId)
	{
		query=new StringBuffer();
		if(transNextYear.equals("Y"))
		{
			query.append(" SELECT SUM(EMPABSDEDUCTION.VALUE) as ABSDAYS FROM EMPABSENCE");
			query.append(" INNER JOIN EMPABSDEDUCTION ON EMPABSENCE.REC_NO = EMPABSDEDUCTION.REC_NO");
			query.append(" WHERE EMPABSDEDUCTION.DEDUCT_FROM ='L'");
			query.append(" AND EMPABSDEDUCTION.DEDUCT_ID=" + leaveId);
			query.append("AND EMP_KEY=" + empKey);
		}
		else
		{
			Calendar cal=Calendar.getInstance();
			query.append(" SELECT SUM(EMPABSDEDUCTION.VALUE) as ABSDAYS FROM EMPABSENCE");
			query.append(" INNER JOIN EMPABSDEDUCTION ON EMPABSENCE.REC_NO = EMPABSDEDUCTION.REC_NO");
			query.append(" WHERE EMPABSDEDUCTION.DEDUCT_FROM ='L'");
			query.append(" AND EMPABSDEDUCTION.DEDUCT_ID=" + leaveId);
			query.append(" and EMPABSDEDUCTION.EFF_YEAR=" + cal.get(Calendar.YEAR));
			query.append("AND EMP_KEY=" + empKey);
			
		}
		
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		ResultSet rs = null;
		double tmpABSDeductionDays=0;
		try 
		{						
			rs=db.executeNonQuery(query.toString());			
			while(rs.next())
			{														
				tmpABSDeductionDays=rs.getDouble("ABSDAYS");
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---GetABSDeductionDays-->" , ex);
		}
		return tmpABSDeductionDays;
	}
	
	private double TotalServicePeriodDeduction(int empKey,int compKey,Date effDate)
	{
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		query=new StringBuffer();
		ResultSet rs = null;
		double totalServicePeriodDeduction=0;
		try 
		{			
			query.append(" SELECT Sum(EMPABSENCEDET.DEDUCT_SERVICE_DAYS) as TotDays");
			query.append(" FROM EMPABSENCE INNER JOIN EMPABSENCEDET ON EMPABSENCE.REC_NO = EMPABSENCEDET.REC_NO");
			query.append(" where EMPABSENCE.EMP_KEY=" + empKey);			
			rs=db.executeNonQuery(query.toString());			
			while(rs.next())
			{														
				totalServicePeriodDeduction=rs.getDouble("TotDays");
			}
			
			//get from company info at load
			if(DeductAbsenceDays)
			{				
				query=new StringBuffer();
				query.append(" SELECT Count(*) as TotDays FROM DAILYTS Where  Status ='A' And Unit_Nos=0");
				query.append(" And  LABOUR_KEY =" + empKey);
				query.append(" And TS_DATE >='" +sdf.format(DEDUCTABSENCEDATE) + "'");
				query.append(" And TS_DATE <='" + sdf.format(effDate)+"'");
				rs=null;
				rs=db.executeNonQuery(query.toString());
				while(rs.next())
				{
					totalServicePeriodDeduction=totalServicePeriodDeduction + rs.getDouble("TotDays");
				}
				
				query=new StringBuffer();
				query.append(" Select   Sum(NOTCALCULATE_NOS) As NotCalcTotal, Sum(NORMAL_HRS)/SUM(TOTALDAYS)  As NormalHrs,Labour_Key");
				query.append(" from TSSUMMARY Where Status='A' And Notcalculate_Nos <>0");
				query.append(" And TO_DATE >='" +sdf.format(DEDUCTABSENCEDATE) + "'");
				query.append(" And TO_DATE <='" + sdf.format(effDate)+"'");
				query.append(" And  LABOUR_KEY =" + empKey);
				query.append(" Group by LABOUR_KEY");
				rs=null;
				rs=db.executeNonQuery(query.toString());
				while(rs.next())
				{
					totalServicePeriodDeduction=totalServicePeriodDeduction +Math.round(rs.getDouble("NotCalcTotal") / rs.getDouble("NormalHrs"));
				}
				
			}
			String strLeaveID=GetLEAVETypes4DeductService(compKey, empKey);
			if(strLeaveID.length()>0)
			{
				query=new StringBuffer();
				query.append(" SELECT Sum(EMPLEAVE.ACTUAL_DAYS) as TotDays FROM EMPLEAVE Where");
				query.append(" EMPLEAVE.EMP_KEY=" + empKey);
				query.append(" And EMPLEAVE.LEAVE_ID In(" + strLeaveID + ")");
				query.append(" And EMPLEAVE.STATUS <>'V' ");
				query.append(" And EMPLEAVE.FROM_DATE <'" + sdf.format(effDate) + "'");
				rs=null;
				rs=db.executeNonQuery(query.toString());
				while(rs.next())
				{
					totalServicePeriodDeduction=totalServicePeriodDeduction + rs.getDouble("TotDays");
				}
			}
			if(EXCLUDEANNUALLEAVEDAYS.equals("Y"))
			{
				query=new StringBuffer();
				query.append(" SELECT Sum(EMPLEAVE.ACTUAL_DAYS) as TotDays FROM EMPLEAVE Where");
				query.append(" EMPLEAVE.EMP_KEY=" + empKey);				
				query.append(" And EMPLEAVE.STATUS <>'V' And EMPLEAVE.LEAVE_ID=100");
				query.append(" And EMPLEAVE.FROM_DATE <'" + sdf.format(effDate) + "'");
				rs=null;
				rs=db.executeNonQuery(query.toString());
				while(rs.next())
				{
					totalServicePeriodDeduction=totalServicePeriodDeduction + rs.getDouble("TotDays");
				}
			}
			logger.info("totalServicePeriodDeduction >> " + totalServicePeriodDeduction);
			
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---TotalServicePeriodDeduction-->" , ex);
		}
		return totalServicePeriodDeduction;
	}
	
	private String GetLEAVETypes4DeductService(int compKey,int empKey)
	{
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		query=new StringBuffer();
		ResultSet rs = null;
		String strLeaveType="";
		String strLeaveType1="";
		String result="";
		try
		{
			query.append(" Select * From LEAVE_DEDUCTSERVICE Where DEDUCT_SERVICE ='Y' And NATIONALITY ='A' ");
			query.append(" And Comp_Key=" + compKey);
			rs=db.executeNonQuery(query.toString());
			while(rs.next())
			{
				if(strLeaveType.equals(""))
				{
					strLeaveType=rs.getString("Leave_ID");
				}
				else
				{
					strLeaveType+="," + rs.getString("Leave_ID");
				}
			}
			
			query=new StringBuffer();
			rs=null;
			query.append(" Select * From LEAVE_DEDUCTSERVICE Where DEDUCT_SERVICE ='Y' And NATIONALITY='" + NAT_FLAG+"'");
			query.append(" And Comp_Key=" + compKey);
			rs=db.executeNonQuery(query.toString());
			while(rs.next())
			{
				if(strLeaveType1.equals(""))
				{
					strLeaveType1=rs.getString("Leave_ID");
				}
				else
				{
					strLeaveType1+="," + rs.getString("Leave_ID");
				}
			}
			
			if(strLeaveType.length()>0)
			{
				result=strLeaveType;
				if(strLeaveType1.length()>0)
				{
					result+="," + strLeaveType1;
				}
			}
			else
			{
				result=strLeaveType1;
			}
			
			
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---GetLEAVETypes4DeductService-->" , ex);
		}
		return result;
	}
	
	private double GetOpeningUnpaidDays(int empKey)
	{
		double GetOpeningUnpaidDays=0;
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		query=new StringBuffer();
		ResultSet rs = null;
		try
		{
			query.append(" Select  Used_Days From OPENINGUSEDDAYS Where ");
			query.append(" EMP_KEY=" + empKey);
			rs=db.executeNonQuery(query.toString());
			while(rs.next())
			{
				GetOpeningUnpaidDays+=rs.getDouble("Used_Days");
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---GetOpeningUnpaidDays-->" , ex);
		}
		return GetOpeningUnpaidDays;
	}
	private double GetLeaveWitoutPay(int empKey)
	{
		double GetLeaveWitoutPay=0;
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		query=new StringBuffer();
		ResultSet rs = null;
		try
		{
			query.append(" SELECT SUM(ACTUAL_DAYS) as DaysWithoutPay FROM EMPLEAVE WHERE ");
			query.append(" EMP_KEY=" + empKey);
			query.append(" And EMPLEAVE.STATUS  <>'V' And PAYMENT ='W' ");
			rs=db.executeNonQuery(query.toString());
			while(rs.next())
			{
				GetLeaveWitoutPay+=rs.getDouble("DaysWithoutPay");
			} 
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---GetLeaveWitoutPay-->" , ex);
		}
		return GetLeaveWitoutPay;
	}
	
	private double GetAdjustmentDays(int empKey,int leaveId,Date leaveDate)
	{
		double getAdjustmentDays=0;
		//SQLDBHandler db=new SQLDBHandler("HRONLINE");		
		query=new StringBuffer();
		ResultSet rs = null;
		try
		{
			if(transNextYear.equals("Y"))
			{
			query.append(" SELECT Sum(DAYS) as AdjustDays FROM DAYSADJUSTMENT WHERE ");
			query.append(" EMP_KEY=" + empKey);
			query.append(" And LEAVE_TYPE=" + leaveId);
			query.append(" And ADJUST_DATE <='" + sdf.format(leaveDate) + "'");
			}
			else
			{
				Calendar cal=Calendar.getInstance();
				cal.setTime(leaveDate);
				query.append(" SELECT Sum(DAYS) as AdjustDays FROM DAYSADJUSTMENT WHERE ");
				query.append(" EMP_KEY=" + empKey);
				query.append(" And LEAVE_TYPE=" + leaveId);
				query.append(" And ADJUST_DATE <='" + sdf.format(leaveDate) + "'");
				query.append(" And YEAR(ADJUST_DATE)=" + cal.get(Calendar.YEAR));
			}
			rs=db.executeNonQuery(query.toString());
			while(rs.next())
			{				
			   getAdjustmentDays=Math.round(rs.getDouble("AdjustDays"));				
			}
		   
		}
		catch (Exception ex) 
		{
			logger.error("error in EmployeeActivity---GetAdjustmentDays-->" , ex);
		}
		
		return getAdjustmentDays;
	}
}
