package hr;

import hr.model.LeaveapproveOrRejectModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import model.CustomerModel;
import model.EmailSettingsModel;
import model.EmployeeModel;
import model.ExpirySettingsModel;

public class HRQueries 
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	WebusersModel dbUser=null;
	
	
	public HRQueries()
	{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
	}
	
	public String getDBCompany(int companyId)
	{
		 String sql="Select * from companiesdb where dbtype='HR' and companyid=" + companyId ;
		 return sql;
	}
	
	public String getDefaultCompanyQuery(int userID)
	{
		query=new StringBuffer();
		query.append("SELECT defaultCompanyId as DEFAULT_COMP from Userwebcompany where USERID="+userID);
		//query.append(" FROM Users INNER JOIN COMPSETUP ON");
		//query.append(" USERS.DEFAULT_COMP=COMPSETUP.COMP_KEY WHERE USERID =" + );		 		
		return query.toString();	
	}
	
	public String getEmployeesQuery(int compkey,String status,int supervisorId)
	{
		query=new StringBuffer();
		 query.append("Select * from EmployeeDetails");
		 if(compkey!=0)
		 {
			query.append(" where compkey=" + compkey);			
		 }
		 else
		 {
			 query.append(" where compkey!=" + compkey);			
		 }
		 if(!status.equals(""))
		 {
			 if(status.equals("Y"))
			 {
				 query.append(" AND (ACTIVE='I'  and  TERMINATE='Y') "); 
			 }
			 else
			 {
			 query.append(" and Active='" + status + "'");
			 }
		 }
		 if(supervisorId>0)
		 {
				query.append(" and (SupervisorID=" + supervisorId+" or ( SuperSupervisorId ="+supervisorId+" or(employeeKey="+supervisorId+" ");
				if(compkey!=0)
				 {
		 	query.append(" and CompKey="+compkey+")))");
				 }
				else
				{
					query.append(" and CompKey !="+compkey+")))");
				}
		 }
		 query.append(" order by REPLICATE('0',2-LEN(employeeNo)) + employeeNo");
		return query.toString();		
	}
	
	public String getLocalItemListValuesQuery(int itemTypeRef )
	{
		query=new StringBuffer();
		 query.append("Select * from LocalItem");
		 query.append(" where ItemTypeRef=" + itemTypeRef);
		 query.append(" order by FullName,PRIORITY_ID ");
		return query.toString();		
	}
	
	public String getHRListValuesQuery(int fieldId )
	{
		query=new StringBuffer();
		 query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID,notes from HRLISTVALUES");
		 query.append(" where FIELD_ID=" + fieldId);
		 query.append(" order by DESCRIPTION,PRIORITY_ID ");
		return query.toString();		
	}
	
	public String getHRListValuesWithSubQuery(int fieldId,int subId )
	{
		query=new StringBuffer();
		 query.append("Select ID, QBListID, QBEditSequance, CODE, DESCRIPTION, ARABIC, SUB_ID, FIELD_ID, FIELD_NAME, DEF_VALUE, REQUIRED, PRIORITY_ID,notes from HRLISTVALUES");
		 query.append(" where FIELD_ID=" + fieldId);
		 query.append(" and sub_id=" + subId);
		 query.append(" order by PRIORITY_ID ,DESCRIPTION ");
		return query.toString();		
	}
	
	public String getDepartemnet(int compID)
	{
		query=new StringBuffer();
		 query.append("select Distinct HRLISTVALUES.DESCRIPTION as dept,dep_id from dbo.COMPSTANDPOS Left Join HRLISTVALUES On COMPSTANDPOS.dep_id=HRLISTVALUES.id where comp_key="+compID+"");
		return query.toString();		
	}
	
	public String getPostion(int deptId,int compId)
	{
		query=new StringBuffer();
		 query.append("select *,HRLISTVALUES.DESCRIPTION as dept from dbo.COMPSTANDPOS Left Join HRLISTVALUES On COMPSTANDPOS.dep_id=HRLISTVALUES.id ");
		 if(deptId>0)
			 query.append(" where dep_id="+deptId+"");
		 if(compId>0)
			 query.append(" and comp_key="+compId+"");
		return query.toString();		
	}
	
	public String updateEmployeesQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		 query.append("update EMPMAST SET emp_no="+obj.getEmployeeNo()+", DEP_ID =" + obj.getDepartmentID() + ", ENGLISH_FIRST='" + obj.getEnFirstName() + "' , ENGLISH_MIDDLE='" + obj.getEnMiddleName()+"'");
		 query.append(", ENGLISH_LAST='" + obj.getEnLastName() + "' ,ENGLISH_FULL='" + obj.getFullName() + "'");
		 query.append(", ARABIC_FIRST='" + obj.getArFirstName() + "' ,ARABIC_MIDDLE='" + obj.getArMiddleName() + "' ,ARABIC_LAST='" +obj.getArLastName() + "' ,ARABIC_FULL='" + obj.getArabicName() + "'");
		 query.append(", POS_ID=" + obj.getPositionID() + " ,NAT_ID=" + obj.getNationalityID() + " , BIRTH_DATE='" +  sdf.format(obj.getDateOfBirth()) + "'");
		 query.append(", age=" + obj.getAge());
		 query.append(" WHERE EMP_KEY =" + obj.getEmployeeKey());
		return query.toString();		
	}
	
	//Employee Ledger
	public String getCompanyListQuery(int UserId)
	{
		query=new StringBuffer();
		 query.append("SELECT COMPSETUP.COMP_KEY,COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR FROM ");
		 query.append(" COMPSETUP Inner  Join Userwebcompany ON COMPSETUP.COMP_KEY =Userwebcompany.companyId");
		 query.append(" where USERID ="  + UserId);
		 query.append(" ORDER BY COMPSETUP.COMP_NAME");
		 return query.toString();		
	}
	
	public String GetEmployeeLedgerQuery(int CompKey,String EmpFilterStatus,boolean includeEOS,int DEP_ID,int POS_ID)
	{
		query=new StringBuffer();
		 query.append(" SELECT EMPMAST.*,HRLISTVALUES.DESCRIPTION As EMP_DEP,HRLISTVALUES.Arabic As EMP_DEPAR,");
		 query.append(" HRLISTVALUES_Position.DESCRIPTION AS Position,HRLISTVALUES_Nationality.DESCRIPTION AS Country,");
		 query.append(" COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR");
		 query.append(" FROM (COMPSETUP INNER JOIN EMPMAST ON COMPSETUP.COMP_KEY = EMPMAST.COMP_KEY)");
		 query.append(" INNER JOIN HRLISTVALUES ON EMPMAST.DEP_ID = HRLISTVALUES.ID");
		 query.append(" LEFT OUTER JOIN HRLISTVALUES as HRLISTVALUES_Position ON EMPMAST.POS_ID = HRLISTVALUES_Position.ID");
		 query.append(" LEFT OUTER JOIN HRLISTVALUES AS HRLISTVALUES_Nationality ON dbo.EMPMAST.NAT_ID = HRLISTVALUES_Nationality.ID");		 
		 query.append(" WHERE COMPSETUP.COMP_KEY =" + CompKey);
		 
		 if(EmpFilterStatus.equals("Active"))			 
		 query.append(" AND EMPMAST.ACTIVE='A' ");
		 else if(EmpFilterStatus.equals("Inactive"))
		 {
			 query.append(" AND EMPMAST.ACTIVE='I' ");
			 if(includeEOS==false)
				 query.append(" AND EMPMAST.ACTIVE='I'  AND (TERMINATED='N' or TERMINATED IS NULL)  ");
			 else
				 query.append(" AND (EMPMAST.ACTIVE='I'  OR  TERMINATED='Y') ");			 
		 }
		 		 
		 else if(EmpFilterStatus.equals("EOS"))			 
			 query.append(" AND EMPMAST.TERMINATED='Y' ");
		 
		 else
		 {
			 if(includeEOS==false)
			 query.append(" AND (TERMINATED='N' or TERMINATED IS NULL) ");	
		 }
		 if(DEP_ID!=0)
		 {
			 query.append(" AND DEP_ID=" + DEP_ID);
		 }
		 if(POS_ID!=0)
		 {
			 query.append(" AND POS_ID=" + POS_ID);
		 }
		 
		// query.append(" AND EMP_KEY in (190,191,192)");
		 query.append(" ORDER BY COMPSETUP.COMP_NAME,HRLISTVALUES.PRIORITY_ID,HRLISTVALUES.DESCRIPTION,EMPMAST.EMP_NO");
		return query.toString();
	}
	
	public String getEmpStatusDescriptionforInactive(int empId)
	{
		query=new StringBuffer();
		 query.append(" SELECT * FROM EMPSTATUS WHERE STATUS_ID  <>3 And STATUS  ='A' And  ACTIVE           ='Y'");
		return query.toString();
	}
	
	public String getEmpStatusDescriptionForActive(int empId,Date today)
	{
		query=new StringBuffer();
		 query.append("SELECT EMPLEAVE.*,Description As LeaveDesc FROM EMPLEAVE LEFT JOIN HRLISTVALUES ON EMPLEAVE.LEAVE_ID=HRLISTVALUES.ID WHERE EMPLEAVE.STATUS<>'V' AND EMPLEAVE.ENCASH_STATUS<>'Y'   AND ((FROM_DATE           Between '"+sdf.format(today)+"' And '"+sdf.format(today)+"') OR  (TO_DATE  Between '"+sdf.format(today)+"' And '"+sdf.format(today)+"') OR (('"+sdf.format(today)+"' BETWEEN FROM_DATE AND TO_DATE ) OR ('"+sdf.format(today)+"' BETWEEN FROM_DATE AND TO_DATE )) OR (RETURN_STATUS='False' AND TO_DATE<='"+sdf.format(today)+"'))");
		return query.toString();
	}
	
	public String GetAllEMPSalaryQuery(String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select distinct EMP_KEY from SALARYMAST Where");
		 query.append(" ((FROM_DATE Between '" +fromDate+"' And '" + toDate +"') OR (TO_DATE Between '" +fromDate+"' And '" + toDate +"'))");		 		
		return query.toString();
	}
	
	public String GetEMPSalaryQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select * from SALARYMAST Where");
		 query.append(" ((FROM_DATE Between '" +fromDate+"' And '" + toDate +"') OR (TO_DATE Between '" +fromDate+"' And '" + toDate +"'))");		
		 query.append(" And EMP_KEY=" +empkey);
		 query.append(" ");
		 query.append(" ");
		 query.append(" ");
		return query.toString();
	}
	
	public String GETAllEmpABSENCEQuery(String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" SELECT distinct EMP_KEY");
		 query.append(" FROM");
		 query.append(" EMPABSENCE LEFT JOIN EMPABSENCEDET ON EMPABSENCE.REC_NO = EMPABSENCEDET.REC_NO");
		 query.append(" where ");
		 query.append(" REQ_DATE Between '" +fromDate+"' And '" + toDate +"'");		
		 query.append(" and EMPABSENCE.STATUS  <>'C'");		
		return query.toString();
	}
	
	public String GETEmpABSENCEQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" SELECT EMPABSENCE.*, EMPABSENCEDET.EXCUSE as 'AbsExecuse',EMPABSENCEDET.ABS_FROM,ABS_TO,");
		 query.append(" EMPABSENCEDET.AMOUNT,EMPABSENCEDET.ABS_TOTAL,EMPABSENCEDET.ABS_TYPE FROM");
		 query.append(" EMPABSENCE LEFT JOIN EMPABSENCEDET ON EMPABSENCE.REC_NO = EMPABSENCEDET.REC_NO");
		 query.append(" where EMP_KEY=" +empkey);
		 query.append(" and REQ_DATE Between '" +fromDate+"' And '" + toDate +"'");
		
		 query.append(" and EMPABSENCE.STATUS  <>'C'");
		 query.append(" ");
		 query.append(" ");
		return query.toString();
	}
	
	public String GetAllEMPLeaveQuery(String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append("Select distinct EMP_KEY from EMPLEAVE");
		 query.append(" Left join HRLISTVALUES on HRLISTVALUES.[ID] = EMPLEAVE.LEAVE_ID");		
		 query.append(" where ");
		 query.append(" EMPLEAVE.STATUS  <>'V' AND EMPLEAVE.STATUS<>'C'");
		 query.append(" and ((FROM_DATE Between '" +fromDate+"' And '" + toDate +"') OR (TO_DATE Between '" +fromDate+"' And '" + toDate +"'))");						
		return query.toString();
	}
	
	public String GetEMPLeaveQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append("Select EMPLEAVE.*,HRLISTVALUES.DESCRIPTION,HRLISTVALUES.ARABIC from EMPLEAVE");
		 query.append(" Left join HRLISTVALUES on HRLISTVALUES.[ID] = EMPLEAVE.LEAVE_ID");		
		 query.append(" where EMP_KEY=" +empkey);
		 query.append(" and EMPLEAVE.STATUS  <>'V' AND EMPLEAVE.STATUS<>'C'");
		 query.append(" and ((FROM_DATE Between '" +fromDate+"' And '" + toDate +"') OR (TO_DATE Between '" +fromDate+"' And '" + toDate +"'))");				
		 query.append(" ");
		 query.append(" ");
		return query.toString();
	}
	
	public String GetAllEMPLoanQuery(String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select distinct EMP_KEY from LOANMAST");
		 query.append(" Right join HRLISTVALUES on HRLISTVALUES.[ID] = LOANMAST.LOAN_REASON");		
		 query.append(" where ");
		 query.append(" Not LOANMAST.STATUS='C'");
		 query.append(" and LOAN_DATE Between '" +fromDate+"' And '" + toDate +"'");				
		
		return query.toString();
	}
	
	public String GetEMPLoanQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select LOANMAST.*,HRLISTVALUES.DESCRIPTION,HRLISTVALUES.ARABIC from LOANMAST");
		 query.append(" Right join HRLISTVALUES on HRLISTVALUES.[ID] = LOANMAST.LOAN_REASON");		
		 query.append(" where EMP_KEY=" +empkey);
		 query.append(" And Not LOANMAST.STATUS='C'");
		 query.append(" and LOAN_DATE Between '" +fromDate+"' And '" + toDate +"'");				
		 query.append(" ");
		 query.append(" ");
		return query.toString();
	}
		
	public String GetLoanRepaymentQuery(int empkey ,int recNo)
	{
		query=new StringBuffer();
		 query.append(" Select LOANREPAYMENT.*,OPENING_ENTRY from LOANREPAYMENT");
		 query.append(" Inner join LOANMAST on LOANMAST.REC_NO = LOANREPAYMENT.LOAN_REC_NO");		
		 query.append(" where LOANMAST.EMP_KEY=" +empkey);
		 query.append(" And LOANMAST.REC_NO=" + recNo);		 			
		 query.append("  Order by INST_NO");
		 query.append(" ");
		return query.toString();
	}
	
	public String GetAllEMPEOSQuery(String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select distinct EMP_KEY from EMPEOS");		
		 query.append(" where ");
		 query.append(" Not STATUS='C'");
		 query.append(" and EOS_DATE Between '" +fromDate+"' And '" + toDate +"'");					
		return query.toString();
	}
	
	public String GetEMPeosQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select EMPEOS.* from EMPEOS");		
		 query.append(" where EMP_KEY=" +empkey);
		 query.append(" And Not STATUS='C'");
		 query.append(" and EOS_DATE Between '" +fromDate+"' And '" + toDate +"'");				
		 query.append(" ");
		 query.append(" ");
		return query.toString();
	}
	
	public String GetAllEMPADTrxQuery(String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select distinct EMP_KEY from EMPADTRX");	
		 query.append(" Left Join HRLISTVALUES on HRLISTVALUES.[ID] = EMPADTRX.AD_ID");
		 query.append(" where ");
		 query.append(" EMPADTRX.STATUS Not In ('C','V')");
		 query.append(" and AD_DATE Between '" +fromDate+"' And '" + toDate +"'");				
		
		return query.toString();
	}
	
	public String GetEMPADTrxQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select EMPADTRX.*,HRLISTVALUES.DESCRIPTION,HRLISTVALUES.ARABIC from EMPADTRX");	
		 query.append(" Left Join HRLISTVALUES on HRLISTVALUES.[ID] = EMPADTRX.AD_ID");
		 query.append(" where EMP_KEY=" +empkey);
		 query.append(" And EMPADTRX.STATUS Not In ('C','V')");
		 query.append(" and AD_DATE Between '" +fromDate+"' And '" + toDate +"'");				
		 query.append(" ");
		 query.append(" ");
		return query.toString();
	}
	public String GetPAIDSalaryQuery(int empkey ,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" SELECT SUM(TOTAMOUNT_TOPAY)AS PaidAmt,Salary_Month,Salary_Year,PVDATE ");	
		 query.append(" FROM PVTRANSACTION INNER JOIN (PVTRANSACTIONDET INNER JOIN SALARYMAST ON");
		 query.append(" PVTRANSACTIONDET.Rec_No = SALARYMAST.REC_NO) ON PVTRANSACTION.PVNO      = PVTRANSACTIONDET.PVNO");		 
		 query.append(" where EMP_KEY=" +empkey);
		 query.append(" And SALARY_PAY ='Y' AND PV_STATUS='P'");
		 query.append(" and PVTRANSACTION.PVDATE Between '" +fromDate+"' And '" + toDate +"'");				
		 query.append(" Group By Salary_Month,Salary_Year,PVDATE");
		 query.append(" ");
		return query.toString();
	}
	
	
	//Passport Request
	public String GetEmployeePassportQuery(int empkey)
	{
		query=new StringBuffer();
		 query.append(" select REC_NO, EMP_KEY, PASSPORT_NO, ISSUE_CTRY, ISSUE_PLACE, ISSUE_DATE, FILE_NO, EXPIRY_DATE, EMPPASSPORT.NOTES, STATUS, REM_DAYS, PASSPORT_LOC, ");
		 query.append(" HRLISTVALUES_CurCountry.DESCRIPTION as 'country', HRLISTVALUES_City.DESCRIPTION as 'city'");
		 query.append(" from EMPPASSPORT LEFT OUTER JOIN HRLISTVALUES AS HRLISTVALUES_CurCountry ON EMPPASSPORT.ISSUE_CTRY = HRLISTVALUES_CurCountry.ID");
		 query.append(" LEFT OUTER JOIN HRLISTVALUES AS HRLISTVALUES_City ON EMPPASSPORT.ISSUE_PLACE = HRLISTVALUES_City.ID");
		 query.append(" ");
		 query.append(" where ");
		 query.append(" status='A'");
		 query.append(" and emp_key=" + empkey);					
		return query.toString();
	}
	
	
	public String GetEmployeeContactQuery(int empkey)
	{
		query=new StringBuffer();
		 query.append(" select EMP_KEY, CONTACT_ID, DETAILS, EMPCONTACT.NOTES as notes1,HRLISTVALUES.DESCRIPTION ");		
		 query.append(" FROM EMPCONTACT LEFT JOIN HRLISTVALUES ON EMPCONTACT.CONTACT_ID=HRLISTVALUES.ID");		 
		 query.append(" where ");		
		 query.append(" emp_key=" + empkey);
		 query.append("  Order By Contact_ID");
		return query.toString();
	}
	
	public String GetEmployeeSponsorQuery(int empkey)
	{
		query=new StringBuffer();
		 query.append(" Select EMPMAST.SPOTYPE,EMPMAST.COMPSPOID, SPONSORINFO.SPONSOR_NAME , ");
		 query.append(" SPONSORINFO.SPONSOR_NAMEAR,COMPSETUP.COMP_NAME,COMPSETUP.COMP_NAME_AR, VISA_EXPDATE, VISA_TYPE ");
		 query.append(" FROM EMPMAST LEFT JOIN SPONSORINFO ON EMPMAST.SPONSOR_NAMEID=SPONSORINFO.SPONSOR_KEY ");
		 query.append(" LEFT JOIN COMPSETUP ON EMPMAST.COMPSPOID=COMPSETUP.COMP_KEY");
		 query.append(" where ");		
		 query.append(" emp_key=" + empkey);		 
		return query.toString();
	}
	
	public String checkIfEmployeeHasPassportRequestQuery(int empkey)
	{
		query=new StringBuffer();
		 query.append(" Select Count(*) as RequestCount from EmployeeASkingPassport");	
		 query.append(" where Status <> 'R'");		
		 query.append(" and emp_key=" + empkey);		 
		return query.toString();
	}
	public String getNewRecNoPassportRequestQuery()
	{
		query=new StringBuffer();
		 query.append(" select Max(rec_no) as 'RecNO' from EMPLOYEEASKINGPASSPORT");				
		return query.toString();
	}
	public String InsertPassportRequestQuery1(int recNo,int EMP_KEY,Date RequestDate,Date ExpextedReturnDate,int NoOfDays,int Reason,String notes)
	{
		query=new StringBuffer();
		 query.append(" Insert Into EMPLOYEEASKINGPASSPORT(REC_NO,EMP_KEY,RequestDate,ExpextedReturnDate,NoOfDays,Reason,MoreDetails) ");
		 query.append(" values (" + recNo +", " + EMP_KEY + ", '" + sdf.format(RequestDate) + "', '" + sdf.format(ExpextedReturnDate) + "', " + NoOfDays + ", ");
		 query.append(" " + Reason + ", ");
		 query.append("'" + notes + "'");
		 query.append(" )");		
		return query.toString();
	}
	public String checkIfEmployeeHasOnlinePassportRequestQuery(int empkey)
	{
		query=new StringBuffer();
		 query.append(" Select Count(*) as RequestCount from PASSPORTREQUEST");	
		 query.append(" where Status <> 'R'");		
		 query.append(" and emp_key=" + empkey);		 
		return query.toString();
	}
	public String InsertPassportRequestQuery(int EMP_KEY,Date RequestDate,Date ExpextedReturnDate,int NoOfDays,int Reason,String notes)
	{
		query=new StringBuffer();
		 query.append(" Insert Into PASSPORTREQUEST(EMP_KEY,REQUEST_START_DATE,EXPECTED_RECIEVE_DATE,NUMBER_OF_DAYS,REASON_ID,NOTES,DATE) ");
		 query.append(" values (" + EMP_KEY + ", '" + sdf.format(RequestDate) + "', '" + sdf.format(ExpextedReturnDate) + "', " + NoOfDays + ", ");
		 query.append(" " + Reason + ", ");
		 query.append("'" + notes + "', getdate() ");
		 query.append(" )");		
		return query.toString();
	}
	
	//Leave Request
	public String GetEmployeeByEmployeeNumberQuery(String empNO)
	{
		query=new StringBuffer();
		query.append(" Select * from EMPMAST Where");		
		query.append(" EMP_NO='" +empNO + "'");		
		return query.toString();
	}
	
	public String GetCompanySetupQuery(int compkey)
	{
		query=new StringBuffer();
		 query.append(" Select ALLOWMINUSLEAVE,MAXLEAVEDAYS,CalculateActualLeaveDays, ");		
		 query.append(" AddLeaveSalary2TS,LEAVE_BASIS,USE_ENCASH,LEAVE_BEFORE_RETURN,email2,email3,edmail,email_reqd , ");		 
		 //add this for Salary certificate
		 query.append(" COMP_NAME, PHONE1 , PO_BOX , FAX1 ");
		 query.append(" FROM COMPSETUP Where ");		
		 query.append(" COMP_KEY=" + compkey);		 
		return query.toString();
	}
	public String GetLastEmployeeLeaveQuery(int empKey)
	{
		query=new StringBuffer();
		 query.append(" SELECT top 1 EMPLEAVE.* ,HRLISTVALUES.Description  ");		
		 query.append(" FROM EMPLEAVE  ");
		 query.append(" LEFT JOIN HRLISTVALUES ON EMPLEAVE.LEAVE_ID=HRLISTVALUES.ID");		 
		 query.append(" Where RETURN_STATUS ='True' and return_date is not null");		
		 query.append(" and EMP_KEY=" + empKey);
		 query.append(" order by return_date desc");
		 return query.toString();
	}
	
	public String getEmployeeLeavesAllowedQuery(int empKey,String fromDate)
	{
		query=new StringBuffer();
		 query.append(" Select Distinct LEAVESETUP.LEAVE_KEY,HRLISTVALUES.DESCRIPTION ,HRLISTVALUES.ARABIC from LEAVESETUP  ");		
		 query.append(" INNER JOIN EMPMAST on EMPMAST.COMP_KEY = LEAVESETUP.COMP_KEY  ");
		 query.append(" INNER JOIN HRLISTVALUES ON LEAVESETUP.LEAVE_KEY = HRLISTVALUES.ID");		 
		 query.append(" Where LEAVESETUP.COMP_KEY =EMPMAST.COMP_KEY ");		
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'Level_ID'    And [Value] in (EMPMAST.CLevel,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'DEP_ID'      And [Value] in (EMPMAST.DEP_ID,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'POS_ID'      And [Value] in (EMPMAST.POS_ID,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'SECTION_ID'  And [Value] in (EMPMAST.SECTION_ID,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'CLASS_ID'    And [Value] in (EMPMAST.CLASS_ID,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'EMP_TYPE'    And [Value] in (EMPMAST.EMP_TYPE,'A'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'SEX_ID'      And [Value] in (EMPMAST.SEX_ID,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'MARITAL_ID'  And [Value] in (EMPMAST.MARITAL_ID,'-1'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'NATIONALITY' And [Value] in (EMPMAST.NAT_FLAG,'A'))");
		 query.append(" And LEAVESETUP.REC_NO in (SELECT REC_NO FROM SETUPCONDITIONS Where COMP_KEY =EMPMAST.COMP_KEY And SETUPCONDITIONS.Activity = 'LEAVE' + Cast(LEAVESETUP.LEAVE_KEY As varChar) And SETUPCONDITIONS.Condition = 'RELIGION_ID' And [Value] in (EMPMAST.RELIGION_ID,'-1'))");
		 query.append(" And LEAVESETUP.EFF_DATE   = (Select Max(Eff_Date) from LEAVESETUP LEAVESETUP1 ");
		 query.append(" Where LEAVESETUP1.EFF_DATE   <= '" + fromDate+"'");
		 query.append(" And   LEAVESETUP1.COMP_KEY    = LEAVESETUP.COMP_KEY");
		 query.append(" And   LEAVESETUP1.LEAVE_KEY   = LEAVESETUP.LEAVE_KEY)");
		 query.append(" AND EMPMAST.EMP_KEY =" + empKey);
		 query.append(" AND EMPMAST.EMPLOYEEMENT_DATE     <='" + fromDate+"'");
		 query.append(" AND Isnull(EMPMAST.TERMINATED,'N') = 'N' ");
		 query.append(" ORDER BY HRLISTVALUES.DESCRIPTION");		
		return query.toString();
	}
	
	public String GetEmployeeLeaveQuery(int LEAVE_ID,int empKey)
	{
		query=new StringBuffer();
		 query.append(" Select REC_NO,EMP_KEY,LEAVE_ID,STATUS,HRLISTVALUES.Description  ");		
		 query.append(" FROM EMPLEAVE  ");
		 query.append(" LEFT JOIN HRLISTVALUES ON EMPLEAVE.LEAVE_ID=HRLISTVALUES.ID");		 
		 query.append(" Where ");		
		 query.append(" LEAVE_ID=" + LEAVE_ID);
		 query.append(" and EMP_KEY=" + empKey);
		return query.toString();
	}
	public String checkIfLeaveTakenQuery(int LEAVE_ID,int empKey)
	{
		query=new StringBuffer();
		 query.append(" Select REC_NO,EMP_KEY,LEAVE_ID,STATUS,HRLISTVALUES.Description ");		
		 query.append(" FROM EMPLEAVE  ");
		 query.append(" LEFT JOIN HRLISTVALUES ON EMPLEAVE.LEAVE_ID=HRLISTVALUES.ID");
		 query.append(" where STATUS<>'V' AND PAYMENT <>'L' and RETURN_STATUS ='False' ");		
		 if(LEAVE_ID!=0)
		 query.append(" and LEAVE_ID=" + LEAVE_ID);
		 query.append(" and EMP_KEY=" + empKey);
		return query.toString();
	}
	
	public String checkIfOnlineLeaveTakenQuery(int LEAVE_ID,int empKey)
	{
		query=new StringBuffer();
		 query.append(" Select * ");		
		 query.append(" FROM LEAVEREQUEST  ");
		 query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.LEAVE_type_ID=HRLISTVALUES.ID");
		 query.append(" ");		 
		 query.append(" where STATUS <>'R' ");		
		 if(LEAVE_ID!=0)
		 query.append(" and LEAVE_TYPE_ID=" + LEAVE_ID);
		 query.append(" and EMP_KEY=" + empKey);
		return query.toString();
	}
	
	public String GetOverLapDateEmployeeLeaveQuery(int empKey,String fromDate,String toDate)
	{
		query=new StringBuffer();
		 query.append(" Select EMPABSENCEDET.* from EMPABSENCEDET ");		
		 query.append(" Inner join EMPABSENCE on EMPABSENCE.REC_NO = EMPABSENCEDET.REC_NO  ");							
		 query.append(" Where EMP_KEY=" + empKey);
		 query.append(" and (  ");
		 query.append(" (ABS_TYPE='H'  and ABS_DATE between '" +fromDate + "' and '" +toDate +"') ");
		 query.append(" or (");
		 query.append(" (ABS_TYPE='D'  and ABS_FROM between '" +fromDate + "' and '" +toDate +"') ");
		 query.append(" or (ABS_TO between '" +fromDate + "' and '" +toDate +"') ");
		 query.append(" or ('"+fromDate +"' between ABS_FROM And ABS_TO )");
		 query.append(" or ('"+toDate +"' between ABS_FROM And ABS_TO )");		 
		 query.append(" )");
		 query.append(" )");
		 return query.toString();
	}
	
	public String checkDupliacteLeaveQuery(int empKey,String fromDate,String toDate)
	{
	 query=new StringBuffer();
	 query.append(" Select * ");		
	 query.append(" FROM LEAVEREQUEST  ");
	 query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.LEAVE_type_ID=HRLISTVALUES.ID");
	 query.append(" ");		 
	 query.append(" where STATUS in ('SA','C','SSA')");		
	 query.append(" and EMP_KEY=" + empKey);
	 query.append(" and ((Leave_Start_date between '" +fromDate + "' and '" +toDate +"') ");
	 query.append(" or (Leave_end_date between '" +fromDate + "' and '" +toDate +"') ");
	 query.append(" or ('"+fromDate +"' between Leave_Start_date And Leave_end_date )");
	 query.append(" or ('"+toDate +"' between Leave_Start_date And Leave_end_date ))");	
	 return query.toString();
	}
	
	public String getEmployeeOutStandingLoansQuery(int empKey)
	{
		query=new StringBuffer();
		 query.append(" Select Count(*) As LoanNos, Sum(Loan_Amount)-Sum(RCVD_AMOUNT) As OutstandingAMT,Sum(Inst_Amount)As OutstandingINST ");		
		 query.append(" FROM LOANMAST ");							
		 query.append(" Where EMP_KEY=" + empKey);
		 query.append(" and LOAN_AMOUNT - RCVD_AMOUNT  > 0");
		 return query.toString();
	}
	
	public String InsertLeaveRequestQuery(int EMP_KEY,Date startDate,Date ExpextedReturnDate,int reasonId,String notes,int leaveTypeId,String other_leave_reason,String checkUseEncashValue,Date requestDate,String status,String attachment_path)
	{
		if(reasonId==-1)
			reasonId=0;
		
		query=new StringBuffer();
		 query.append(" Insert Into LEAVEREQUEST(EMP_KEY,LEAVE_START_DATE,LEAVE_END_DATE,REASON_ID,NOTES,DATE,STATUS,LEAVE_TYPE_ID,other_leave_reason,encash_status,attachment_path) ");
		 query.append(" values (" + EMP_KEY + ", '" + sdf.format(startDate) + "', '" + sdf.format(ExpextedReturnDate) + "', ");
		 query.append(" " + reasonId + ", ");
		 query.append("'" + notes + "','"+ sdf.format(requestDate)+"','"+status+"'," + leaveTypeId + ", '" + other_leave_reason +"','"+checkUseEncashValue+"','"+attachment_path+"'");
		 query.append(" )");		
		return query.toString();
	}
	
	public String updateLeaveRequestQuery(int Id,Date startDate,Date ExpextedReturnDate,int reasonId,String notes,int leaveTypeId,String other_leave_reason,
			String checkUseEncashValue)
	{
		if(reasonId==-1)
			reasonId=0;
		
		if(notes!=null)
		{
			notes=notes.replaceAll("'","`");
		}
				
		query=new StringBuffer();
		query.append(" update LEAVEREQUEST set LEAVE_START_DATE ='%s' , LEAVE_END_DATE='%s' , REASON_ID ='%s' , "
		 		+ " NOTES ='%s', LEAVE_TYPE_ID='%s', other_leave_reason='%s', encash_status='%s' ");		 	
		 query.append(" where ID = "  + Id);		
	
		return query.toString().format(query.toString() ,sdf.format(startDate),sdf.format(ExpextedReturnDate),
				reasonId, notes,leaveTypeId,other_leave_reason,checkUseEncashValue);
								
	}
	
	//Loan Request
	public String getEmployeeSalaryQuery(int empKey,Date RequestDate)
	{
		query=new StringBuffer();
		query.append(" GetSalarySummary " + empKey + " , '" + sdf.format(RequestDate) + "'");				
		return query.toString();
	}
	
	public String getEmployeeLastSalaryQuery(int empKey,String status)
	{
		query=new StringBuffer();
		query.append(" SELECT MAX(CAST(CAST(SALARY_YEAR AS VARCHAR) + '/' + CAST(SALARY_MONTH AS VARCHAR) + '/1' AS DATETIME)) as MaxDate, SALARY_STATUS,'S' As FormName");
		query.append(" FROM SALARYMAST WHERE  EMP_KEY =" + empKey);
		query.append(" AND SALARY_STATUS='" + status + "'");
		query.append(" GROUP BY SALARY_STATUS ");
		query.append(" union ");
		query.append(" SELECT MAX(CAST(CAST(SALARY_YEAR AS VARCHAR) + '/' + CAST(SALARY_MONTH AS VARCHAR) + '/1' AS DATETIME)) as MaxDate,SALARY_STATUS,'T' As FormName");
		query.append(" FROM SALARYMASTTS WHERE  EMP_KEY  =" + empKey);
		query.append(" AND SALARY_STATUS='" + status + "'");
		query.append(" GROUP BY SALARY_STATUS");
		query.append(" Order By MAX(CAST(CAST(SALARY_YEAR AS VARCHAR) + '/' + CAST(SALARY_MONTH AS VARCHAR) + '/1' AS DATETIME)) Desc");
		return query.toString();
	}
	
	public String InsertLoanRequestQuery(int EMP_KEY,int reasonId,double loanAmount,int month,int year,int installNo,double installAmount, String notes)
	{
		query=new StringBuffer();
		 query.append(" Insert Into LOANREQUEST(EMP_KEY,DATE,STATUS,REASON_ID,LOAN_AMOUNT,START_FROM_MONTH,START_FROM_YEAR,INSTALLMENTS_NO,INSTALLMENTS_AMOUNT,NOTES) ");
		 query.append(" values (" + EMP_KEY + ", getdate() , 'C' , " + reasonId + " ," +loanAmount + ", " +month + " , " + year + ", ");
		 query.append(" " + installNo + ", "  + installAmount + " , ");
		 query.append("'" + notes + "'");
		 query.append(" )");		
		return query.toString();
	}
	
	public String getLoanHistoryQuery(int empKey)
	{
		query=new StringBuffer();
		query.append(" select LOANREQUEST.*,DESCRIPTION from LOANREQUEST");
		query.append(" inner join HRLISTVALUES on LOANREQUEST.REASON_ID=HRLISTVALUES.ID");
		 query.append(" Where EMP_KEY=" + empKey);
		query.append(" order by date");
		return query.toString();
	}
	
	//Import Employee
	public String getMaxIDQuery(String tableName,String fieldName)
	{
		query=new StringBuffer();
		query.append(" select max("+ fieldName +") from "+ tableName);
		return query.toString();
	}
	
	public String addNewHRListValueQuery(int ID,String DeptName,String DeptNameAr,String fieldName,String fieldID)
	{
		query=new StringBuffer();
		query.append(" insert into HRLISTVALUES (ID,DESCRIPTION,ARABIC,SUB_ID,FIELD_ID,FIELD_NAME,DEF_VALUE,REQUIRED)");
		query.append(" values ( "+ID+",'"+DeptName+"','"+DeptNameAr+"',0,"+ fieldID +", '" +fieldName + "','','N');");
		return query.toString();
	}
	public String updateHRListValueQuery(int ID,String DeptName,String DeptNameAr)
	{
		query=new StringBuffer();
		query.append(" update HRLISTVALUES set DESCRIPTION='" + DeptName + "' , ARABIC='" + DeptNameAr + "'");
		query.append(" where ID="+ID);
		return query.toString();
	}
	public String insertNewEmployeeQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		query.append(" INSERT INTO EMPMAST (EMP_KEY,COMP_KEY,DEP_ID,POS_ID,LOC_ID,EMP_NO,ENGLISH_FIRST,ENGLISH_MIDDLE,ENGLISH_LAST,ENGLISH_FULL,ARABIC_FIRST,ARABIC_MIDDLE,ARABIC_LAST "+
				",ARABIC_FULL,NAT_ID,BIRTH_DATE,SEX_ID,RELIGION_ID,MARITAL_ID,EMPLOYEEMENT_DATE , CREATE_DATE,EMP_TYPE,ACTIVE) " +
				"VALUES  ("+obj.getEmployeeKey() +","+obj.getCompKey() +","+obj.getDepartmentID()+","+obj.getPositionID()+","+obj.getLocationId()+",'"+obj.getEmployeeNo()
					+"' ,'"+obj.getEnFirstName() +"', '"+obj.getEnMiddleName()+"' , '"+obj.getEnLastName()+"' , '"+obj.getFullName()+"' , '"+obj.getArFirstName()+
						"' , '"+obj.getArMiddleName()+"' , '"+obj.getArLastName()+"' , '"+obj.getArabicName()+"' , "+obj.getNationalityID()+
						", '"+sdf.format(obj.getDateOfBirth())+"' , "+obj.getSexId()+","+obj.getReligionId()+ "," +obj.getMaritalID()
						 +", '"+sdf.format(obj.getJoiningDate()) + "' , getdate() , 'E','A' "  +" )");
		
		return query.toString();
	}
	
	//Edit Employee
	public String GetGenerateNoTypeQuery()
	{
		query=new StringBuffer();
		query.append(" SELECT GENERATE_EMPNO FROM SYSTEMTABLE");			
		return query.toString();
	}
	public String GetEmpNoPrefixQuery(int compKey)
	{
		query=new StringBuffer();
		query.append(" Select PREFIX_TEXT FROM COMPSETUP Where COMP_KEY=" + compKey);			
		return query.toString();
	}
	public String SystemSerialNosQuery(String serialField)
	{
		query=new StringBuffer();
		query.append(" Select * from SystemSerialNos Where SerialField='" + serialField+"'");			
		return query.toString();
	}
	
	public String UpdateSystemSerialNoQuery(String SerialField,String LastNumber,boolean isNew)
	{
		query=new StringBuffer();
		if(isNew)
		{
		query.append(" Insert into SystemSerialNos(SerialField,LastNumber) Values('"+SerialField+"' , '"+LastNumber+"')");				
		}
		else
		{
			query.append("Update SystemSerialNos set LastNumber='"+LastNumber + "' where SerialField='" + SerialField +"'");	
		}
		
		return query.toString();
	}
	
	public String GetEmployeeByEmployeeKeyQuery(int empKey)
	{
		query=new StringBuffer();
		query.append(" Select * from EMPMAST Where");		
		query.append(" emp_key='" +empKey + "'");		
		return query.toString();
	}
	
	public String GetEmployeeDeatailsByEmployeeKeyQuery(int empKey)
	{
		query=new StringBuffer();
		query.append(" Select * from EmployeeDetails Where");		
		query.append(" EmployeeKey='" +empKey + "'");		
		return query.toString();
	}
	
	public String GetEmployeeAgeRangeQuery(int COMP_KEY)
	{
		query=new StringBuffer();
		query.append(" SELECT AGE_FROM,AGE_TO FROM EMPSETUP WHERE ");		
		query.append(" COMP_KEY='" +COMP_KEY + "'");		
		return query.toString();
	}
	
	public String checkEmployeeNoExistQuery(int compKey)
	{
		query=new StringBuffer();
		query.append(" Select * from EMPMAST Where");		
		query.append(" COMP_KEY='" +compKey + "'");		
		return query.toString();
	}
	
	public String getCompanyStartBussinessQuery(int compKey)
	{
		query=new StringBuffer();
		query.append(" Select YEAR_BUSINESS,MONTH_BUSINESS from COMPSETUP Where");		
		query.append(" COMP_KEY='" +compKey + "'");		
		return query.toString();
	}
	
	public String editEmployeesQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		obj.setFullName(obj.getEnFirstName() + " " + obj.getEnMiddleName() + " " + obj.getEnLastName());
		obj.setArabicName(obj.getArFirstName() + " " + obj.getArMiddleName() + " " + obj.getArLastName());
		if(obj.getLocal().equals("1"))
		{
			obj.setNationalityID(0);
			obj.setLocal("L");
		}
		else
		{
			obj.setLocal("N");
		}
		
		 query.append("update EMPMAST SET emp_no='"+obj.getEmployeeNo()+"' , DEP_ID =" + obj.getDepartmentID() + ", ENGLISH_FIRST='" + obj.getEnFirstName() + "' , ENGLISH_MIDDLE='" + obj.getEnMiddleName()+"'");
		 query.append(", ENGLISH_LAST='" + obj.getEnLastName() + "' ,ENGLISH_FULL='" + obj.getFullName() + "'");
		 query.append(", ARABIC_FIRST='" + obj.getArFirstName() + "' ,ARABIC_MIDDLE='" + obj.getArMiddleName() + "' ,ARABIC_LAST='" +obj.getArLastName() + "' ,ARABIC_FULL='" + obj.getArabicName() + "'");
		 query.append(", POS_ID=" + obj.getPositionID() + " ,NAT_ID=" + obj.getNationalityID() + " ,RELIGION_ID= " + obj.getReligionId());
		 query.append(", EMP_TYPE='" + obj.getEmployeeType() + "' , SEX_ID=" + obj.getSexId() + ", BLOOD_TYPE=" + obj.getBloodType());
		 query.append(",BIRTH_PLACE='" + obj.getPlaceOfBirth() + "' , BIRTH_COUNTRY=" + obj.getCountryOfBirth() + ", NAT_FLAG='" + obj.getLocal() +"'");
		 query.append(", Birth_Date='" + sdf.format(obj.getDateOfBirth()) +"' , Age='" + obj.getAge()
				 + "' ,EMPLOYEEMENT_DATE='" +sdf.format(obj.getEmployeementDate()) +"' ,STATUS=" + obj.getStatusId() + " ,MARITAL_ID=" + obj.getMaritalID()+",std_no='"+obj.getStandardNo()+"'");
		 		 
		 query.append(" WHERE EMP_KEY =" + obj.getEmployeeKey());
		return query.toString();		
	}
	
	public String addNewEmployeeQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		obj.setFullName(obj.getEnFirstName() + " " + obj.getEnMiddleName() + " " + obj.getEnLastName());
		obj.setArabicName(obj.getArFirstName() + " " + obj.getArMiddleName() + " " + obj.getArLastName());
		if(obj.getLocal().equals("1"))
		{
			obj.setNationalityID(0);
			obj.setLocal("L");
		}
		else
		{
			obj.setLocal("N");
		}
		
		query.append(" INSERT INTO EMPMAST (EMP_KEY,COMP_KEY,DEP_ID,POS_ID,LOC_ID,EMP_NO,ENGLISH_FIRST,ENGLISH_MIDDLE,ENGLISH_LAST,ENGLISH_FULL,ARABIC_FIRST,ARABIC_MIDDLE,ARABIC_LAST "+
				",ARABIC_FULL,NAT_ID,BIRTH_DATE,SEX_ID,RELIGION_ID,MARITAL_ID,EMPLOYEEMENT_DATE , CREATE_DATE,EMP_TYPE,ACTIVE,NAT_FLAG,BIRTH_PLACE,BIRTH_COUNTRY,STATUS,Age,std_no ");
		  if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
		  {
			  query.append(",qblistemp_key ");
		  }
		
		query.append(") " +
				"VALUES  ("+obj.getEmployeeKey() +","+obj.getCompKey() +","+obj.getDepartmentID()+","+obj.getPositionID()+","+obj.getLocationId()+",'"+obj.getEmployeeNo()
					+"' ,'"+obj.getEnFirstName() +"', '"+obj.getEnMiddleName()+"' , '"+obj.getEnLastName()+"' , '"+obj.getFullName()+"' , '"+obj.getArFirstName()+
						"' , '"+obj.getArMiddleName()+"' , '"+obj.getArLastName()+"' , '"+obj.getArabicName()+"' , "+obj.getNationalityID()+
						", '"+sdf.format(obj.getDateOfBirth())+"' , "+obj.getSexId()+","+obj.getReligionId()+ "," +obj.getMaritalID()
						 +", '"+sdf.format(obj.getEmployeementDate()) + "' , getdate() , '"+obj.getEmployeeType()+"','A', '"+obj.getLocal() +"' , '"+
						obj.getPlaceOfBirth() +"' , "+obj.getCountryOfBirth() + ","+obj.getStatusId() +","+obj.getAge() +",'"+obj.getStandardNo()+"'");
		 if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
		  {
			  query.append(","+obj.getQblistEmpKey()+"");
		  }
		query.append(")");
		
		return query.toString();
	}
	
	public String addEmial(int empKey,String email)
	{
		int contct_id=622;
		query=new StringBuffer();
		query.append("insert into empcontact (emp_key,contact_id,details) values ("+empKey+","+contct_id+",'"+email+"')");		
		return query.toString();
	}
	
	public String updateEmail(int empKey,String email)
	{	
		query=new StringBuffer();
		query.append("update empcontact set details='"+email+"' where contact_id=622 and  emp_Key="+empKey);		
		return query.toString();
	}
	
	public String getEmail(int empKey)
	{
		int contct_id=622;
		query=new StringBuffer();
		query.append("select * from empcontact where contact_id="+contct_id+" and emp_key="+empKey);		
		return query.toString();
	}
	
	public String getLeaveRequestByIdQuery(int leaveId)
	{
	
		query=new StringBuffer();
		query.append("select LEAVEREQUEST.*,HRLISTVALUES.description as leaveReason,empmast.supervisor,empmast.emp_no,empmast.english_full as empName from (select LEAVEREQUEST.*,HRLISTVALUES.description as leaveType from LEAVEREQUEST ");
		query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.Leave_type_id=HRLISTVALUES.id)LEAVEREQUEST "); 
		query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.reason_id=HRLISTVALUES.id ");
		query.append(" LEFT JOIN empmast ON LEAVEREQUEST.emp_key=empmast.emp_key  where ");		
		query.append("LEAVEREQUEST.ID='"+leaveId+"' ");
	
		return query.toString();
	}
	
	public String leaveapproveOrRejectList(int superviosrID,int compKey,String status,boolean isSuper_supervisor1)
	{
	
		query=new StringBuffer();
		query.append("select LEAVEREQUEST.*,HRLISTVALUES.description as leaveReason,empmast.supervisor,empmast.emp_no,empmast.english_full as empName from (select LEAVEREQUEST.*,HRLISTVALUES.description as leaveType from LEAVEREQUEST ");
		query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.Leave_type_id=HRLISTVALUES.id)LEAVEREQUEST "); 
		query.append(" LEFT JOIN HRLISTVALUES ON LEAVEREQUEST.reason_id=HRLISTVALUES.id ");
		query.append(" LEFT JOIN empmast ON LEAVEREQUEST.emp_key=empmast.emp_key  where ");
		if(!status.equalsIgnoreCase(""))
		query.append("LEAVEREQUEST.status='"+status+"' and ");
		else if(!status.equalsIgnoreCase("") && superviosrID>0)
		{
			query.append("(LEAVEREQUEST.status!='D' and LEAVEREQUEST.status!='SSA') and ");
		}
		else
		{
			query.append("(LEAVEREQUEST.status!='D') and ");
		}
		query.append(" empmast.comp_key="+compKey);
		if(superviosrID>0)
		query.append(" and (empmast.supervisor="+superviosrID+" or empmast.SuperSupervisorId ="+superviosrID+")");
		query.append(" order by LEAVEREQUEST.date desc");
		return query.toString();
	}
	
	
	public String updateLeaveForApproveOrReject(LeaveapproveOrRejectModel obj)
	{
		query=new StringBuffer();
		query.append("update LEAVEREQUEST set status='"+obj.getStatus()+"',notes='"+obj.getReason()+"' where id ="+obj.getId());		
		return query.toString();
	}
	
	public String getEmployeePassportExpiryDate(String status ,int empKEy)
	{
		query=new StringBuffer();
		query.append("select * from EMPPASSPORT where status='"+status+"' and emp_key="+empKEy+"");		
		return query.toString();
	}
	
	public String getEmployeeResidanceExpiryDate(String status ,int empKEy)
	{
		query=new StringBuffer();
		query.append("select * from EMPRESIDENCE where status='"+status+"' and emp_key="+empKEy+"");		
		return query.toString();
	}
	
	public String getEmployeeLabourCardExpiryDate(String status ,int empKEy)
	{
		query=new StringBuffer();
		query.append("	select * from EMPLABORCARD where status='"+status+"' and emp_key="+empKEy+"");		
		return query.toString();
	}
	
	
	public String addNewEmployeeinHBATableQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		obj.setFullName(obj.getEnFirstName() + " " + obj.getEnMiddleName() + " " + obj.getEnLastName());
		obj.setArabicName(obj.getArFirstName() + " " + obj.getArMiddleName() + " " + obj.getArLastName());
		if(obj.getLocal().equals("1"))
		{
			obj.setNationalityID(0);
			obj.setLocal("L");
		}
		else
		{
			obj.setLocal("N");
		}
		
		query.append(" INSERT INTO employee (ListID,EditSequence,TimeCreated,Emp_Key,Name,FirstName,MiddleName,LastName,ArName,FullName,SubLevel,IsActive "+
				",BillAddress1,PrintChequeAs,SsNo,Gender,Birth_Date,MarStatus,PriorityID,POSITION,NATIONALITY,JOINDATE,EMP_NO,Email,SkypeID,ActName,IBANNo,Emp_Type,LoanAccount,BlackListed,Proj_ID,Notes,HREmp_Key,PrintChequeAsAR");
		query.append(") " +
				"VALUES  ('','',getdate(),"+obj.getQblistEmpKey()+",'"+obj.getFullName()+"','"+obj.getEnFirstName()
					+"' ,'"+obj.getEnMiddleName() +"', '"+obj.getEnLastName()+"' , '"+obj.getArabicName()+"' , '"+obj.getFullName()+"' , "+0+
						", 'Y' , '' , '"+obj.getFullName()+"' , '"+obj.getStandardNo()+"',"+obj.getSexId()+
						", '"+sdf.format(obj.getDateOfBirth())+"' , "+obj.getMaritalID()+","+0+ "," +obj.getPositionID()+ ","+obj.getNationalityID() 
						 +",'"+sdf.format(obj.getEmployeementDate())+"','"+obj.getEmployeeNo() +"', '"+obj.getEmail() +"' , '' , '"+obj.getFullName()+"','','"+obj.getEmployeeType()+"','','',"+obj.getProjectKey()+",'',"+obj.getEmployeeKey()+",''");
		query.append(")");
		
		return query.toString();
	}
	
	
	public String updateEmployeesHBATableQuery(EmployeeModel obj)
	{
		query=new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		obj.setFullName(obj.getEnFirstName() + " " + obj.getEnMiddleName() + " " + obj.getEnLastName());
		obj.setArabicName(obj.getArFirstName() + " " + obj.getArMiddleName() + " " + obj.getArLastName());
		if(obj.getLocal().equals("1"))
		{
			obj.setNationalityID(0);
			obj.setLocal("L");
		}
		else
		{
			obj.setLocal("N");
		}
		
		 query.append("update employee SET ListID='' , EditSequence ='', TimeCreated= getdate()");
		 query.append(", Name='" + obj.getFullName() + "' ,FirstName='" + obj.getEnFirstName() + "', MiddleName='" + obj.getEnMiddleName() + "' , LastName='" + obj.getEnLastName() + "' ");
		 query.append(", ArName='" + obj.getArabicName() + "' ,FullName='" + obj.getFullName() + "' ,SubLevel=0 ,IsActive='Y' , BillAddress1='' , PrintChequeAs=''");
		 query.append(", POSITION=" + obj.getPositionID() + " ,NATIONALITY=" + obj.getNationalityID() + "");
		 query.append(", EMP_TYPE='" + obj.getEmployeeType() + "' , Gender=" + obj.getSexId() + ", JOINDATE='"+sdf.format(obj.getEmployeementDate())+"'");
		 query.append(",EMP_NO='" + obj.getEmployeeNo() + "' , Email='"+obj.getEmail()+"', ActName='" + obj.getFullName() +"'");
		 query.append(", Birth_Date='" + sdf.format(obj.getDateOfBirth()) +"',SsNo='"+obj.getStandardNo()+""
				+"' ,MarStatus=" + obj.getMaritalID()+",HREmp_Key="+obj.getEmployeeKey() );
		 		 
		 query.append(" WHERE EMP_KEY =" + obj.getQblistEmpKey());
		return query.toString();		
	}
	
	
	public String addQBListEmployee(EmployeeModel obj)
	{
		query=new StringBuffer();		
		String listType="Employee";
		query.append("INSERT INTO qblists(listType,listId,recNo,Name,ArName,FullName,Parent,IsActive,Sublevel,hremp_key)");
		query.append(" VALUES( '" + listType + "','', " + obj.getQblistEmpKey()+" , '" + obj.getFullName()+"' , ");
		query.append(" '"+obj.getArabicName()+"' , '"+obj.getFullName()+"' , '', 'Y' , " +0 +","+obj.getEmployeeKey()+"");
		query.append(" )");
		return query.toString();		
	}
	
	public String updateQBListEmployee(EmployeeModel obj)
	{
		query=new StringBuffer();		
		String listType="Employee";
		query.append("update qblists set listType='"+listType+"',listId='',Name='"+obj.getFullName()+"',ArName='"+obj.getArabicName()+"',FullName='"+obj.getFullName()+"',Parent='',IsActive='Y',Sublevel="+0+",hremp_key="+obj.getEmployeeKey()+" where recNo="+obj.getQblistEmpKey()+"");
		return query.toString();		
	}
	
	
	//Expiry Settings leave and loan 
		public String getexpirySettings(int compId)
		{
			query=new StringBuffer();
			query.append("SELECT * FROM EXPIRYSETTINGS ");
			query.append(" where compId ="  + compId);
			return query.toString();		
		}
		
		public String deleteExpirySettings(int compId)
		{
			query=new StringBuffer();
			query.append("Delete FROM EXPIRYSETTINGS ");
			query.append(" where compId ="  + compId);
			return query.toString();		
		}
		
		public String saveExpirySettings(ExpirySettingsModel obj,int compId)
		{
			query=new StringBuffer();
			query.append("INSERT INTO EXPIRYSETTINGS(leaveVisa,leavePassport,leaveResidence,leaveLabourCard,leaveGovtId,leaveHealthCard,loanVisa,loanPassport,loanResidence,loanLabourCard,loanGovtId,loanVisaHeathCard,compId,noOfdays)");
			query.append(" VALUES( '" + obj.getStrLeaveVisa() + "','"+obj.getStrLeavePassport()+"','" + obj.getStrLeaveResidence()+"', '" + obj.getStrLeaveLabourCrad()+"' , '" + obj.getStrLeaveGovtId()+"' , '" + obj.getStrLeaveHealthCard()+"' ,");
			query.append(" '"+obj.getStrLoanVisa()+"' , '"+obj.getStrLoanPassport()+"' , '"+obj.getStrLoanResidence()+"', '"+obj.getStrLoanLabourCrad()+"' , '"+obj.getStrLoanGovtId()+"','"+obj.getStrLoanHealthCard()+"',"+compId+","+obj.getTotalDays()+"");
			query.append(" )");
			return query.toString();		
		}
		
		public String getEmailSettings(int compId)
		{
			query=new StringBuffer();
			query.append("SELECT EMAIL_REQD,EDMAIL,Email2,email3 FROM compsetup ");
			query.append(" where comp_key ="  + compId);
			return query.toString();		
		}
		
		
		public String updateEmailSettings(EmailSettingsModel obj,int compId)
		{
			query=new StringBuffer();		
			String listType="Employee";
			query.append("update compSetup set EMAIL_REQD='"+obj.getEnalbeMail()+"',EDMAIL='"+obj.getEmail1()+"',Email2='"+obj.getEmail2()+"',email3='"+obj.getEmail3()+"',emailhost='"+obj.getEmailhost()  +"' where comp_key="+compId+"");
			return query.toString();		
		}
		
		
		public String getEmpMastList()
		{
			query=new StringBuffer();
			query.append("select * from empcontact where contact_id in (622)");
			return query.toString();		
			
		}	
		//get from stored procedure GetSalarySummary
		public String GetSalarySummaryQuery(int empKey)
		{
			query=new StringBuffer();
			query.append("Select EMP_KEY,SUM(CASE SALARY_ID WHEN 0 THEN AMOUNT ELSE 0 END) AS BasicSal, ");
			query.append(" SUM(CASE WHEN SALARY_ID != 0 AND ALLOWANCE_TYPE = 0 THEN AMOUNT ELSE 0 END) AS TotAllowance ");
			query.append(" from EMPSALARY ");
			query.append(" where EMP_KEY ="  + empKey);
			query.append(" AND EFF_DATE <= dateadd(year,100,getdate())");
			query.append(" And EFF_DATE >=(SELECT MAX(EFF_DATE) FROM EMPSALARY EMPSALARY1 WHERE ");
			query.append(" EMPSALARY.EMP_KEY=EMPSALARY1.EMP_KEY AND EMPSALARY1.SALARY_ID   =EMPSALARY.SALARY_ID ");
			query.append(" AND EMPSALARY1.EFF_DATE    <= dateadd(year,100,getdate())) ");
			query.append(" GROUP BY EMP_KEY");
			return query.toString();		
			
		}	
	
	
}
