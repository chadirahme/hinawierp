package payroll;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.DraftSalaryModel;
import model.EmployeeModel;


public class DraftSalarySheetQueries 
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public String getDBCompany(int companyId)
	{
		 String sql="Select * from companiesdb where dbtype='HR' and companyid=" + companyId ;
		 return sql;
	}
	public String getCompanyListQuery(int UserId)
	{
		 query=new StringBuffer();
		 query.append("SELECT Create_Date,TOT_DEP,TOT_POS,COMPSETUP.COMP_KEY,COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR FROM ");
		 query.append(" COMPSETUP Inner Join Userwebcompany ON COMPSETUP.COMP_KEY =Userwebcompany.companyId");
		 query.append(" where USERID ="  + UserId);
		 query.append(" ORDER BY COMPSETUP.COMP_NAME");
		 return query.toString();		
	}
	
	public String getTotalCompanyEmployeesQuery()
	{
		query=new StringBuffer();
		query.append("Select Count(*) as 'TotalEmployees' ,COMP_KEY from EMPMAST group by COMP_KEY");
		return query.toString();			
	}
	
	public String getCompanySalaryStatusQuery(int companyKey,String type)
	{
		query=new StringBuffer();
		query.append("Select Max(Cast(Cast(SALARY_YEAR As VarChar) +  '-'+ Cast(SALARY_MONTH As VarChar) + '-1' as DateTime)) As LASTDATE ");
		query.append(" from COMPSALSTATUS Where COMP_KEY =" + companyKey);
		if(type.equals("C"))
		query.append(" And Not CREATE_NOS=0");
		else if(type.equals("A"))
			query.append(" And Not APPROVE_NOS=0");
		if(type.equals("P"))
			query.append(" And Not PAID_NOS=0");
		
		return query.toString();			
	}
	
	public String getCompanyEmployeesQuery(int companyKey)
	{
		query=new StringBuffer();
		query.append("Select EMP_KEY,EMP_NO, ENGLISH_FULL ,EMPLOYEEMENT_DATE,PRIORITY_NO,");
		query.append(" HRLISTVALUES_Department.DESCRIPTION AS Department,  HRLISTVALUES_Position.DESCRIPTION AS Position");
		query.append(" FROM EMPMAST");
		query.append(" LEFT OUTER JOIN HRLISTVALUES AS HRLISTVALUES_Department ON EMPMAST.DEP_ID = HRLISTVALUES_Department.ID");
		query.append(" LEFT OUTER JOIN HRLISTVALUES AS HRLISTVALUES_Position ON EMPMAST.POS_ID = HRLISTVALUES_Position.ID");
		query.append(" where COMP_KEY=" + companyKey);
		query.append(" ORDER BY EMPMAST.EMP_NO");
		return query.toString();			
	}
	
	public String updateEmployeePriorityQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		query.append("update EMPMAST set PRIORITY_NO='"+obj.getPriorityNo()+"' where EMP_KEY ="+obj.getEmployeeKey());		
		return query.toString();
	}
	
	public String getCompanySetupQuery(int compKey)
	{
		query=new StringBuffer();
		query.append("Select * from COMPSETUP Where COMP_KEY=" + compKey);
		return query.toString();			
	}
	
	public String getTimeSheetHistoryEmployeeQuery(int compKey,int month,int year,int status,String empKeys)
	{
		query=new StringBuffer();
		query.append("  select fullname,count(*) as totalDays,employeeno,employeekey,Department,position ,EMPLOYEEMENTDATE,");
		query.append(" month(TS_DATE) as tsMonth,year(TS_DATE) as tsYear,isnull(TS_STATUS,0) as TS_STATUS ,");
		query.append(" sum(case when DAILYTS.Status<>'L' then 1 else 0 end) as 'Present' , sum(case When DAILYTS.Status ='L' Then 1 Else 0 End) As 'Leave' ");
		query.append("  from DAILYTS inner join EmployeeDetails on DAILYTS.LABOUR_KEY=EmployeeDetails.employeekey");
		query.append(" where DAILYTS.COMP_KEY=" + compKey);
		query.append(" and year(TS_DATE)="+year);
		if(month!=0)
		query.append(" and month(TS_DATE)=" + month);		
		if(status!=2)
			query.append(" and isnull(TS_STATUS,0)=" + status);
		if(!empKeys.equals(""))
			query.append(" and DAILYTS.LABOUR_KEY in (" + empKeys + ")");
		query.append(" group by fullname,employeeno,employeekey,month(TS_DATE),year(TS_DATE),TS_STATUS,Department,position,EMPLOYEEMENTDATE");
		query.append(" order by REPLICATE('0',2-LEN(employeeNo)) + employeeNo");
		return query.toString();		
	}
	public String getEmployeeSalaryQuery(int empKey,Date tsDate)
	{
		query=new StringBuffer();
		query.append(" GetSalarySummary " + empKey + " , '" + sdf.format(tsDate) + "'");				
		return query.toString();
	}
	public String getEmployeeOTQuery(int month,int year,String empKeys)
	{
		query=new StringBuffer();
		//query.append("SELECT labour_key, ROUND(sum(AMOUNT),2) as 'TotalAmount' ");
		query.append("SELECT LABOUR_KEY,Calculation,Sum(Units) as OTUnits,ROUND(sum(AMOUNT),2) as OTAmount ");
		query.append(" FROM  DAILYOT Where");
		query.append(" year(TS_DATE)="+year + " and month(TS_DATE)=" + month);
		if(!empKeys.equals(""))
			query.append(" and labour_key in (" + empKeys + ")");
		query.append(" group by labour_key,Calculation");
		return query.toString();			
	}
	
	//Create Salary Sheet
	public String getTimeSheetEmployeeQuery(int compKey,int month,int year,int status,String empKeys)
	{
		query=new StringBuffer();
		query.append("  Select Distinct LABOUR_KEY");		
		query.append("  from DAILYTS");
		query.append(" where DAILYTS.COMP_KEY=" + compKey);
		query.append(" and year(TS_DATE)="+year);
		if(month!=0)
		query.append(" and month(TS_DATE)=" + month);		
		if(status!=2)
			query.append(" and isnull(TS_STATUS,0)=" + status);
		if(!empKeys.equals(""))
			query.append(" and DAILYTS.LABOUR_KEY in (" + empKeys + ")");	
		return query.toString();		
	}
	
	public String checkLeavePaymentsQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  Select * From  PVLEAVEDRAFT");		
		query.append("  Inner join EMPMAST On EMPMAST.EMP_KEY = PVLEAVEDRAFT.EMP_KEY ");
		query.append(" 	where EMPMAST.COMP_KEY=" + compKey);
		query.append(" and PVLEAVEDRAFT.STATUS ='N' and EMPMAST.SALARY_CREATE ='T' ");	
		query.append(" and PVLEAVEDRAFT.SALARY_MONTH=" + month);				
		query.append(" and PVLEAVEDRAFT.SALARY_YEAR=" + year);
		if(!empKeys.equals(""))
			query.append(" and PVLEAVEDRAFT.EMP_KEY in (" + empKeys + ")");	
		return query.toString();		
	}
	public String checkAdditionsPaymentsQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  Select EMPADTRX.* From  EMPADTRX");		
		query.append("  Inner join EMPMAST On EMPMAST.EMP_KEY = EMPADTRX.EMP_KEY ");
		query.append(" 	where EMPADTRX.COMP_KEY=" + compKey);
		query.append(" and EMPADTRX.STATUS  in ('C','H') and EMPMAST.SALARY_CREATE ='T' ");	
		query.append(" and EMPADTRX.MONTHNO=" + month);				
		query.append(" and EMPADTRX.YEARNO=" + year);
		if(!empKeys.equals(""))
			query.append(" and EMPADTRX.EMP_KEY in (" + empKeys + ")");	
		return query.toString();		
	}
	public String checkLoanPaymentsQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  Select LOANMAST.* From  LOANMAST");		
		query.append("  Inner join EMPMAST On EMPMAST.EMP_KEY = LOANMAST.EMP_KEY ");
		query.append(" 	where EMPMAST.COMP_KEY=" + compKey);
		query.append(" and LOANMAST.STATUS  in ('C','H') and EMPMAST.SALARY_CREATE ='T' ");	
		query.append(" and LOANMAST.INST_MONTH=" + month);				
		query.append(" and LOANMAST.INST_YEAR=" + year);
		if(!empKeys.equals(""))
			query.append(" and LOANMAST.EMP_KEY in (" + empKeys + ")");	
		return query.toString();		
	}
	public String checkEOSQuery(int compKey,int month,int year,String empKeys)
	{
		Date _fromDate,_toDate=null;
		Calendar c = Calendar.getInstance();	
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		c.set(year,month-1,1);
		int maxMonthDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(year,month-1,maxMonthDays);
		try {
			_toDate=df.parse(sdf.format(c.getTime()));
		} catch (ParseException e) {
			
		}
		query=new StringBuffer();
		query.append("  Select EMPEOS.* From  EMPEOS");		
		query.append("  Inner join EMPMAST On EMPMAST.EMP_KEY = EMPEOS.EMP_KEY ");
		query.append(" 	where EMPEOS.STATUS in ('C')");
		query.append("  and EMPMAST.SALARY_CREATE ='T' ");	
		query.append(" and EOS_DATE<='" + df.format(_toDate) + "'");				
		
		if(!empKeys.equals(""))
			query.append(" and EMPEOS.EMP_KEY in (" + empKeys + ")");	
		else
			query.append(" 	and EMPMAST.COMP_KEY=" + compKey);	
		return query.toString();		
	}
	
	public String getTimeSheetTotalDaysQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append(" Select LABOUR_KEY,Count(Distinct DAILYTIMING.REC_NO) AS TotalDays");
		
		query.append("  from DAILYTIMING");
		query.append(" where ");
		query.append(" year(TS_DATE)="+year);		
		query.append(" and month(TS_DATE)=" + month);		
		
		if(!empKeys.equals(""))
			query.append(" and LABOUR_KEY in (" + empKeys + ")");
		query.append(" and  DAILYTIMING.STATUS      In('P','H') ");
		query.append(" and  DAILYTIMING.Calc_Flag   ='Y'");
		query.append(" and  Not DAILYTIMING.Unit_Nos= 0 ");
		query.append(" group by LABOUR_KEY");	
		return query.toString();		
	}
	
	public String getSummaryTimeSheetHistoryEmployeeQuery(int compKey,int month,int year,int status,String empKeys)
	{
		query=new StringBuffer();
		query.append("  select fullname,Count(Distinct DAILYTS.REC_NO) AS TotalDays,employeeno,employeekey,Department,position ,EMPLOYEEMENTDATE,");
		query.append(" Sum(Normal_Hrs) As ActualHrs, Sum(Unit_Nos) As WorkHrs,");
		query.append(" Sum(CASE Normal_Hrs WHEN 0 THEN 0 ELSE 24 END) As SalaryActualHrs, ");
		query.append("  Sum(CASE WHEN Unit_Nos = 0 or Normal_Hrs=0 THEN Unit_Nos ELSE 24/(Normal_Hrs/Unit_Nos) END) As SalaryWorkHrs");
	
		query.append("  from DAILYTS inner join EmployeeDetails on DAILYTS.LABOUR_KEY=EmployeeDetails.employeekey");
		query.append(" where DAILYTS.COMP_KEY=" + compKey);
		query.append(" and year(TS_DATE)="+year);
		if(month!=0)
		query.append(" and month(TS_DATE)=" + month);		
		if(status!=2)
			query.append(" and isnull(TS_STATUS,0)=" + status);
		if(!empKeys.equals(""))
			query.append(" and DAILYTS.LABOUR_KEY in (" + empKeys + ")");
		query.append(" and  DAILYTS.STATUS      In('P','H') ");
		query.append(" and  DAILYTS.Calc_Flag   ='Y'");
		query.append(" and  Not DAILYTS.Unit_Nos= 0 ");
		query.append(" group by fullname,employeeno,employeekey,Department,position,EMPLOYEEMENTDATE");
		query.append(" order by REPLICATE('0',2-LEN(employeeNo)) + employeeNo");
		return query.toString();		
	}
	public String getActualHoursSummaryTimeSheetHistoryEmployeeQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  select Labour_Key,");
		query.append(" Sum(CASE Normal_Hrs WHEN 0 THEN 0 ELSE 24 END) As SalaryActualHrs,");
		query.append(" Sum(CASE WHEN Unit_Nos = 0 or Normal_Hrs=0 THEN Unit_Nos ELSE 24/(Normal_Hrs/Unit_Nos) END) As SalaryWorkHrs, ");
		query.append(" Sum(Normal_Hrs) As ActualHrs, Sum(Unit_Nos) As WorkHrs");
	
		query.append("  from DAILYTS");
		query.append(" where DAILYTS.COMP_KEY=" + compKey);
		query.append(" and year(TS_DATE)="+year);
		if(month!=0)
		query.append(" and month(TS_DATE)=" + month);				
		if(!empKeys.equals(""))
			query.append(" and DAILYTS.LABOUR_KEY in (" + empKeys + ")");	
		query.append(" group by LABOUR_KEY");	
		return query.toString();		
	}
	public String getTotalDaysQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  select LABOUR_KEY,Count(Distinct DAILYTIMING.REC_NO) AS TotalDays ");		
		query.append("  from DAILYTIMING ");
		query.append(" 	where LABOUR_KEY in (" + empKeys + ")");	
		query.append(" and DAILYTIMING.STATUS In('P','H')  and  DAILYTIMING.Calc_Flag   ='Y' ");	
		query.append(" and month(TS_DATE)=" + month);				
		query.append(" and year(TS_DATE)=" + year);	
		query.append(" and  Not DAILYTIMING.Unit_Nos= 0");	
		query.append(" Group by LABOUR_KEY");
		return query.toString();		
	}
	
	public String getTotalHoursQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  select LABOUR_KEY, ");		
		query.append(" Sum(CASE Normal_Hrs WHEN 0 THEN 0 ELSE 24 END) As SalaryActualHrs,");
		query.append(" Sum(CASE WHEN Unit_Nos = 0 or Normal_Hrs=0 THEN Unit_Nos ELSE 24/(Normal_Hrs/Unit_Nos) END) As SalaryWorkHrs,");
		query.append(" Sum(Normal_Hrs) As ActualHrs, Sum(Unit_Nos) As WorkHrs");
		query.append("  from dailyTS ");
		query.append(" 	where LABOUR_KEY in (" + empKeys + ")");			
		query.append(" and month(TS_DATE)=" + month);				
		query.append(" and year(TS_DATE)=" + year);			
		query.append(" Group by LABOUR_KEY");
		return query.toString();		
	}
	
	public String getCompanyOTCalculationQuery(int compKey)
	{
		query=new StringBuffer();
		query.append("Select Distinct OT_RATE from OTCALCULATION Where OT_RATE <>0 and COMP_KEY=" + compKey);
		query.append(" Order by OT_RATE");
		return query.toString();			
	}
	
	public String getEmployeeSalaryQuery(int compKey,int empKey,int month,int year)
	{
		query=new StringBuffer();
		query.append("Select * from SALARYMASTTS ");
		query.append(" 	where COMP_KEY=" + compKey);		
		query.append(" and EMP_KEY=" + empKey);	
		query.append(" and SALARY_MONTH=" + month);				
		query.append(" and SALARY_YEAR=" + year);				
		return query.toString();			
	}
	
	public String getEmployeeAdditionDeductionQuery(int compKey,int month,int year)
	{
		query=new StringBuffer();
		query.append("Select EMP_KEY ,");
		query.append(" round(Sum(CASE AD_FLAG WHEN 'A' THEN AMT_PAYABLE ELSE 0 END),2) AS APayable,");
		query.append(" round(Sum(CASE AD_FLAG WHEN 'D' THEN AMT_PAYABLE ELSE 0 END),2) AS DPayable  ");
		query.append(" FROM EMPADTRX");		
		query.append(" 	where COMP_KEY=" + compKey);				
		query.append(" and MonthNo=" + month);				
		query.append(" and YearNo=" + year);	
		query.append(" Group By EMP_KEY");
		return query.toString();			
	}
	
	public String checkTotalDaysQuery(int compKey,int month,int year,String empKeys)
	{
		query=new StringBuffer();
		query.append("  select LABOUR_KEY,count(*) As TOTEntrys,employeeno,fullname ");		
		query.append("  from DAILYTS ");
		query.append("  inner join EmployeeDetails on DAILYTS.LABOUR_KEY=EmployeeDetails.employeekey");
		query.append(" 	where COMP_KEY =" + compKey);			
		query.append(" and month(TS_DATE)=" + month);				
		query.append(" and year(TS_DATE)=" + year);		
		if(!empKeys.equals(""))
			query.append(" and DAILYTS.LABOUR_KEY in (" + empKeys + ")");
		query.append(" Group by LABOUR_KEY,employeeno,fullname");
		return query.toString();		
	}
	
	public String getEmployeeDetailQuery(String empKeys)
	{
		query=new StringBuffer();
		query.append("  select fullname,employeeno,employeekey");	
		query.append("  from EmployeeDetails");
		query.append(" where 1=1 ");		
		if(!empKeys.equals(""))
			query.append(" and employeekey in (" + empKeys + ")");		
		return query.toString();		
	}
	
	public String getEmployeeLoansPaymentTempQuery(String empKeys,int month,int year)
	{
		query=new StringBuffer();
		query.append("  select Sum(INST_AMOUNT) As LoanInstAmt , EMP_KEY ");	
		query.append("  from LOANREPAYMENTTEMP");
		query.append(" where 1=1 ");		
		if(!empKeys.equals(""))
			query.append(" and EMP_KEY in (" + empKeys + ")");		
		query.append(" and SALARY_MONTH=" + month);				
		query.append(" and SALARY_YEAR=" + year);	
		query.append(" Group by EMP_KEY");
		return query.toString();		
	}
	
	//recalculate Loan install amount
	public String deleteLoanRepaymentTempQuery(int empKey,int month,int year)
	{
		query=new StringBuffer();
		query.append("Delete From  LOANREPAYMENTTEMP where EMP_KEY=" + empKey);
		query.append(" and SALARY_MONTH=" + month);
		query.append(" and SALARY_YEAR=" + year);		
		return query.toString();			
	}
	public String updateLoanRepaymentQuery(int empKey,int month,int year,int loanRecNo)
	{
		query=new StringBuffer();
		query.append("UPDATE LOANREPAYMENT SET Inst_Amount= 0  where EMP_KEY=" + empKey);
		if(loanRecNo!=0)
			query.append(" and LOAN_REC_NO=" + loanRecNo);
		query.append(" and SALARY_MONTH=" + month);
		query.append(" and SALARY_YEAR=" + year);		
		return query.toString();			
	}
	public String getLoanMastQuery(int empKey,int month,int year)
	{
		query=new StringBuffer();
		String _fromDate=null;
		Calendar c = Calendar.getInstance();			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		c.set(year,month-1,1);					
		_fromDate=sdf.format(c.getTime());
		
		query.append("SELECT * FROM LOANMAST  where EMP_KEY=" + empKey);
		query.append(" and STATUS IN ('P','C','A')");
		query.append(" and CAST(CAST(INST_YEAR AS VARCHAR) + '-' + CAST(INST_MONTH AS VARCHAR) + '-1' AS DATETIME)  <='" + _fromDate+"'");	
		query.append(" and LOAN_AMOUNT-Isnull(RCVD_AMOUNT,0)>0");
		return query.toString();			
	}
	public String getLoanRepaymentQuery(int loanRecNo,int month,int year,int empKey)
	{
		query=new StringBuffer();
		query.append("SELECT * from LOANREPAYMENT where 1=1");
		if(loanRecNo!=0)
			query.append(" and LOAN_REC_NO=" + loanRecNo);
		else if(empKey!=0)
			query.append(" and EMP_KEY=" + empKey);
		
		query.append(" and SALARY_MONTH=" + month);
		query.append(" and SALARY_YEAR=" + year);		
		return query.toString();			
	}
	public String getNewInstallNoQuery(int loanRecNo,String strTableName)
	{
		query=new StringBuffer();
		query.append("SELECT MAX(INST_NO) As InstNo FROM " + strTableName);
		query.append(" where LOAN_REC_NO=" + loanRecNo);			
		return query.toString();			
	}
	public String generateIDQuery(String strTableName,String strIDField)
	{
		query=new StringBuffer();
		query.append("SELECT MAX(" + strIDField +")");
		query.append(" from " + strTableName);			
		return query.toString();			
	}
	public String insertLoanRepaymentQuery(int recNo,String today,int loanRecNO ,int empKey,int month,int year,int installNo,double installAmount)
	{
		query=new StringBuffer();
		query.append("INSERT INTO LOANREPAYMENT (REC_NO,INST_DATE,LOAN_REC_NO,EMP_KEY,SALARY_MONTH,SALARY_YEAR,INST_NO,INST_AMOUNT,STATUS)");
		query.append(" values('%s','%s','%s','%s','%s','%s','%s','%s','%s')");
		return query.toString().format(query.toString(),recNo,today,loanRecNO,empKey,month,year,installNo,installAmount,"C");						
	}
	
	public String insertLoanRepaymentTempQuery(int empKey,int month,int year)
	{
		query=new StringBuffer();
		query.append("Insert InTo LOANREPAYMENTTEMP(REC_NO,INST_DATE,LOAN_REC_NO,EMP_KEY,SALARY_MONTH,SALARY_YEAR,INST_NO,INST_AMOUNT,STATUS)");
		query.append(" Select REC_NO  , INST_DATE , LOAN_REC_NO ,EMP_KEY , SALARY_MONTH  , SALARY_YEAR ,INST_NO , INST_AMOUNT   , STATUS From LOANREPAYMENT");
		query.append(" where EMP_KEY=" + empKey);
		query.append(" and SALARY_MONTH=" + month);
		query.append(" and SALARY_YEAR=" + year);		
		return query.toString();			
	}
	
	public String getLoanRepaymentTempQuery(int loanRecNo,int month,int year,int empKey)
	{
		query=new StringBuffer();
		query.append("SELECT * from LOANREPAYMENTTEMP where 1=1");
		if(loanRecNo!=0)
			query.append(" and LOAN_REC_NO=" + loanRecNo);
		else if(empKey!=0)
			query.append(" and EMP_KEY=" + empKey);
		
		query.append(" and SALARY_MONTH=" + month);
		query.append(" and SALARY_YEAR=" + year);		
		return query.toString();			
	}
	
	public String insertNewLoanRepaymentTempQuery(int recNo,String today,int loanRecNO ,int empKey,int month,int year,int installNo,double installAmount)
	{
		query=new StringBuffer();
		query.append("Insert InTo LOANREPAYMENTTEMP(REC_NO,INST_DATE,LOAN_REC_NO,EMP_KEY,SALARY_MONTH,SALARY_YEAR,INST_NO,INST_AMOUNT,STATUS)");
		query.append(" values('%s','%s','%s','%s','%s','%s','%s','%s','%s')");
		return query.toString().format(query.toString(),recNo,today,loanRecNO,empKey,month,year,installNo,installAmount,"C");		
				
	}
	
	
	//save salary
	public String getEmployeeMasterQuery(String empKeys)
	{
		query=new StringBuffer();
		query.append("  select *");	
		query.append("  from EMPMAST");
		query.append(" where 1=1 ");		
		if(!empKeys.equals(""))
			query.append(" and EMP_KEY in (" + empKeys + ")");		
		return query.toString();		
	}
	
	public String deleteSalaryOTQuery(int recNo)
	{
		query=new StringBuffer();
		query.append("Delete From  SALARYOT where REC_NO=" + recNo);		
		return query.toString();			
	}
	public String insertSalaryMastQuery(DraftSalaryModel obj,int month,int year,String today,int compKey)
	{
		query=new StringBuffer();
		query.append("Insert InTo SALARYMASTTS(REC_NO,SALARY_MONTH,SALARY_YEAR,FROM_DATE,TO_DATE,ADD_DAYS,EMP_KEY,CREATE_DATE," +
				"COMP_KEY,WORKING_DAYS,NORMAL_WORKHRS,TOTAL_WORKHRS,BASIC_SALARY,TOTAL_ALLOWANCE,TOTAL_OT,TOTAL_LOAN,TOTAL_ADDITION,TOTAL_DEDUCTION," +
				"NET2PAY,PAID_AMOUNT,TEMP_PAID,SALARY_STATUS,PAY_MODE,BANK_ID,BRANCH_ID,ACCOUNT_NO,SAL_DEPID,SAL_POSID,SAL_GRADEID,SAL_SECTIONID," +
				"SAL_CLASSID,SIF_STATUS,NET_BEFORE_TAXES,CURRENCY_ID,EXCHANGE_RATE)");
		query.append(" values('%s','%s','%s','%s','%s','%s','%s','%s',"+
				"'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',"+
				"'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s',"+
				"'%s','%s','%s','%s','%s')");
		
		return query.toString().format(query.toString(),obj.getRecNo(),month,year,obj.getFromDate(),obj.getToDate(),0,obj.getEmpKey(),today,
				compKey,obj.getTotalDays(), obj.getTotalActualUnits(), obj.getTotalWorkingUnits(),obj.getBasicSalary(),obj.getTotalAllowance(),obj.getOtAmount(),obj.getLoans(),obj.getAdditions(),obj.getDeductions(),
				obj.getNetToPay() ,0,0,"C",obj.getPayMode(),obj.getBankId(),obj.getBranchId(),obj.getAccountNO(),obj.getDepId(),obj.getPosId(),obj.getGradeId(),obj.getSectionId(),
				obj.getClassId(),obj.getSifStatus(),obj.getNetToPay(),0,0);		
				
	}
	
	public String updateSalaryMastQuery(DraftSalaryModel obj,int month,int year,String today,int compKey)
	{
		query=new StringBuffer();
		query.append("UPDATE SALARYMASTTS SET WORKING_DAYS='%s' , NORMAL_WORKHRS='%s' ,TOTAL_WORKHRS='%s' , BASIC_SALARY='%s' , TOTAL_ALLOWANCE='%s' , " +
			" TOTAL_OT ='%s' , TOTAL_LOAN='%s' , TOTAL_ADDITION='%s' , TOTAL_DEDUCTION='%s' , NET2PAY='%s', PAY_MODE='%s' , BANK_ID='%s' ," +
			 "  BRANCH_ID ='%s', ACCOUNT_NO ='%s', SAL_DEPID='%s' , SAL_POSID ='%s', SAL_GRADEID='%s', SAL_SECTIONID='%s' , SAL_CLASSID ='%s', SIF_STATUS='%s' , NET_BEFORE_TAXES='%s'  " +			
			 " Where REC_NO=" + obj.getRecNo());		
		
		return query.toString().format(query.toString(),obj.getTotalDays(),obj.getTotalActualUnits(), obj.getTotalWorkingUnits(),obj.getBasicSalary(),obj.getTotalAllowance(),
				obj.getOtAmount(),obj.getLoans(),obj.getAdditions(),obj.getDeductions(),obj.getNetToPay(),obj.getPayMode(),obj.getBankId(),
				obj.getBranchId(),obj.getAccountNO(),obj.getDepId(),obj.getPosId(),obj.getGradeId(),obj.getSectionId(),obj.getClassId(),obj.getSifStatus(),obj.getNetToPay());		
				
	}
		
	public String deleteSalaryAllowancesQuery(int recNo)
	{
		query=new StringBuffer();
		query.append("Delete From  SALARYALLOWANCETS where REC_NO=" + recNo);		
		return query.toString();			
	}
	
	public String insertSalaryOTQuery(int recNo ,int month,int year,double otRate,double otHours, double otAmount)
	{
		query=new StringBuffer();
		query.append("Insert InTo SALARYOT(REC_NO,MONTH_NO,YEAR_NO,OTRATE,OTHRS,AMOUNT)");
		query.append(" values('%s','%s','%s','%s','%s','%s')");
		return query.toString().format(query.toString(),recNo,month,year,otRate,otHours,otAmount);		
				
	}
	
	//Approve Salary
	public String getEmployeeApproveSalaryQuery(int compKey,String empKeys,int month,int year)
	{
		query=new StringBuffer();
		query.append("Select * from SALARYMASTTS ");
		query.append(" inner join EmployeeDetails on EmployeeDetails.employeekey=SALARYMASTTS.emp_key ");
		query.append(" 	where COMP_KEY=" + compKey);			
		query.append(" and SALARY_MONTH=" + month);				
		query.append(" and SALARY_YEAR=" + year);			
		if(!empKeys.equals(""))
			query.append(" and EMP_KEY in (" + empKeys + ")");	
		query.append("order by REPLICATE('0',2-LEN(EmployeeDetails.employeeNo)) + EmployeeDetails.employeeNo");
		
		return query.toString();			
	}
	
	public String approveSalaryQuery(int recNo)
	{
		query=new StringBuffer();
		query.append("update SALARYMASTTS set SALARY_STATUS='A' where REC_NO=" + recNo);		
		return query.toString();			
	}
	public String approveSalaryAllowancesQuery(int recNo)
	{
		query=new StringBuffer();
		query.append("update SALARYALLOWANCE set [STATUS]='A' where REC_NO=" + recNo);		
		return query.toString();			
	}
	public String updateCompanySalaryStatusQuery(int compKey,int month,int year)
	{
		query=new StringBuffer();
		query.append("update COMPSALSTATUS set APPROVE_NOS= APPROVE_NOS + 1 where COMP_KEY=" + compKey );	
		query.append(" and SALARY_MONTH=" + month);
		query.append(" and SALARY_YEAR=" + year);
		return query.toString();			
	}
}
