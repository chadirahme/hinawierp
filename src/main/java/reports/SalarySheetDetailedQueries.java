package reports;

import java.text.SimpleDateFormat;

public class SalarySheetDetailedQueries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public String retrivingSalaryItems(int comapnyId,int month)
	{
		query=new StringBuffer();
		query.append("SELECT DISTINCT ALLOWANCE_ID, HRLISTVALUES.DESCRIPTION as ALLOWANCEDESC,HRLISTVALUES.ARABIC as ALLOWANCEDESCAR FROM CompAllowance INNER JOIN HRLISTVALUES ON CompAllowance.ALLOWANCE_ID = HRLISTVALUES.ID WHERE COMP_KEY ="+comapnyId+" AND PAY_MODE = 'M' AND PAY_PERIOD = "+month+" ORDER BY HRLISTVALUES.DESCRIPTION");
		return query.toString();
	}
	
	public String retrivingAdditionColumns(int comapnyId)
	{
		query=new StringBuffer();
		query.append("Select DISTINCT AD_ID,HRLISTVALUES.DESCRIPTION As Description FROM (EMPADTRX INNER JOIN EMPMAST ON EMPADTRX.EMP_KEY=EMPMAST.EMP_KEY) LEFT JOIN HRLISTVALUES ON EMPADTRX.AD_ID=HRLISTVALUES.ID Where  AD_FLAG='A' AND EMPMAST.SALARY_CREATE='T' AND EMPMAST.COMP_KEY="+comapnyId+"");
		return query.toString();
	}
	
	public String retrivingDiductionColumns(int comapnyId)
	{
		
		query=new StringBuffer();
		query.append("Select DISTINCT AD_ID,HRLISTVALUES.DESCRIPTION As Description FROM (EMPADTRX INNER JOIN EMPMAST ON EMPADTRX.EMP_KEY         =EMPMAST.EMP_KEY) LEFT JOIN HRLISTVALUES ON EMPADTRX.AD_ID           =HRLISTVALUES.ID Where  AD_FLAG='D' AND EMPMAST.SALARY_CREATE='T' AND EMPMAST.COMP_KEY="+comapnyId+"");
		return query.toString();
		
	}
	
	public String retriveOT(int comapnyId)
	{
		query=new StringBuffer();
		query.append("Select Distinct OT_RATE from OTCALCULATION WHERE COMP_KEY="+comapnyId+" Order by OT_RATE");

		return query.toString();
	}
		
	public String retriveTheMainreportData(int comapnyId,int month,int year,String salarystatus,int empKey,String filterEmpKeys,int supervisorId)
	{
		query=new StringBuffer();
		query.append("SELECT HRListValues_bank.description as bankname,HRListValues_branch.description as branchname,SALARYMASTTS.WORKING_DAYS, SALARYMASTTS.TOTAL_WORKHRS,");
		query.append("SALARYMASTTS.BASIC_SALARY,SALARYMASTTS.TOTAL_ALLOWANCE, SALARYMASTTS.TOTAL_OT,"); 
		query.append("SALARYMASTTS.TOTAL_ADDITION,salarymastts.salary_status,SALARYMASTTS.TOTAL_DEDUCTION,");
		query.append("SALARYMASTTS.TAx_AMount,SALARYMASTTS.NET2PAY,EMPMAST.supervisor,EMPMAST.ENGLISH_FULL,"); 
		query.append("EMPMAST.ARABIC_FULL, EMPMAST.EMP_KEY, EMPMAST.COMP_KEY,"); 
		query.append("HRListValues_Dep.Description As DepDesc,HRListValues_Dep.Arabic As DepDescAR,");
		query.append("HRListValues_Pos.Description As PosDesc,HRListValues_Pos.Arabic As PosDescAR, EMPMAST.EMP_NO,"); 
		query.append("SALARYMASTTS.REC_NO,SALARYMASTTS.SALARY_MONTH,SALARYMASTTS.SALARY_YEAR,");
		query.append("SALARYMASTTS.TOTAL_LOAN,SALARYMASTTS.BANK_ID,SALARYMASTTS.BRANCH_ID,");
		query.append("SALARYMASTTS.ACCOUNT_NO,SALARYMASTTS.CURRENCY_ID,SALARYMASTTS.EXCHANGE_RATE,");
		query.append("SALARYMASTTS.PAID_AMOUNT FROM SALARYMASTTS "); 
		query.append("INNER JOIN EMPMAST ON SALARYMASTTS.EMP_KEY = EMPMAST.EMP_KEY LEFT JOIN HRLISTVALUES As HRListValues_bank On SALARYMASTTS.bank_id=HRListValues_bank.ID");
		query.append(" LEFT JOIN HRLISTVALUES As HRListValues_branch On SALARYMASTTS.branch_id=HRListValues_branch.ID LEFT JOIN "); 
		query.append("HRLISTVALUES As HRListValues_Dep On SALARYMASTTS.SAL_DEPID=HRListValues_Dep.ID LEFT JOIN ");
		query.append("HRLISTVALUES As HRListValues_Pos On SALARYMASTTS.SAL_POSID=HRListValues_Pos.ID WHERE "); 
		query.append("SALARYMASTTS.COMP_KEY="+comapnyId+" AND  SALARYMASTTS.SALARY_MONTH  ="+month+" And ");
		query.append("SALARYMASTTS.SALARY_YEAR ="+year+" ");
		if(salarystatus.equalsIgnoreCase("Approved"))
		{
			query.append("and salarymastts.salary_status='A'");
		}
		else if(salarystatus.equalsIgnoreCase("Created"))
		{
		query.append("and salarymastts.salary_status='C'");
		}
		else if(salarystatus.equalsIgnoreCase("Paid"))
		{
			query.append("and salarymastts.salary_status='P'");
		}
		if(empKey>0)
		{
		query.append("and EMPMAST.EMP_KEY="+empKey+"");
		}
		if(filterEmpKeys!=null && !"".equalsIgnoreCase(filterEmpKeys))
		{
			query.append("and EMPMAST.EMP_KEY in ("+filterEmpKeys+")");
		}
		if(supervisorId>0)
		{
			query.append(" and EMPMAST.supervisor=" + supervisorId);
		}
		query.append("ORDER BY EMP_NO,From_Date Desc");
		return query.toString();
	}
		
	public String retriveSalaryAmount()
	{
		query=new StringBuffer();
		query.append("SELECT  CAST(REC_NO AS VARCHAR) + '-' + CAST(ALLOWANCE_ID AS VARCHAR) As RecNoWithID,ALLOWANCE_ID,AMOUNT,HRLISTVALUES.DESCRIPTION FROM SALARYALLOWANCETS INNER JOIN HRLISTVALUES ON SALARYALLOWANCETS.ALLOWANCE_ID = HRLISTVALUES.ID");
		return query.toString();
	}

	public String retriveOTHoursAndRate(int recNo,double rate)
	{
		query=new StringBuffer();
		query.append("SELECT * FROM SALARYOT ");
		//WHERE REC_NO="+recNo+" And OTRATE ="+rate+"");
		return query.toString();
	}
	
	public String subQueryAddition(int month,int year)
	{
		query=new StringBuffer();
		query.append("SELECT Sum(AMT_PAYABLE) as ADAmount,emp_key,ad_id,AH_VALUE_FLAG,AH_VALUE,HRLISTVALUES.DESCRIPTION As Description FROM EMPADTRX LEFT JOIN HRLISTVALUES ON EMPADTRX.AD_ID=HRLISTVALUES.ID WHERE  MONTHNO="+month+" And YEARNO="+year+" Group By AH_VALUE_FLAG,emp_key,ad_id,AH_VALUE,HRLISTVALUES.DESCRIPTION Order By AH_VALUE_FLAG");
		return query.toString();
	}
	
	public String subQueryDeduction(int month,int year)
	{
		query=new StringBuffer();
		query.append("SELECT Sum(AMT_PAYABLE) as ADAmount,emp_key,ad_id,AH_VALUE_FLAG,AH_VALUE,HRLISTVALUES.DESCRIPTION As Description FROM EMPADTRX LEFT JOIN HRLISTVALUES ON EMPADTRX.AD_ID=HRLISTVALUES.ID WHERE  MONTHNO="+month+" And YEARNO="+year+" Group By AH_VALUE_FLAG,emp_key,ad_id,AH_VALUE,HRLISTVALUES.DESCRIPTION Order By AH_VALUE_FLAG");
		return query.toString();
	}
}
