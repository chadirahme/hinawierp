package payroll;

import hr.model.CompanyModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.CompanyDBModel;
import model.DraftSalaryModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class DraftSalarySheetData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DraftSalarySheetQueries query=new DraftSalarySheetQueries();
	public DraftSalarySheetData()
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
			logger.error("error in DraftSalarySheetData---Init-->" , ex);
		}
	}
	
	public List<CompanyModel> getCompanyList(int userID)
	{
		  List<CompanyModel> lst=new ArrayList<CompanyModel>();		 			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getCompanyListQuery(userID));
				while(rs.next())
				{
					CompanyModel obj=new CompanyModel();
					obj.setCompKey(rs.getInt("COMP_KEY"));					
					obj.setEnCompanyName(rs.getString("COMP_NAME"));
					obj.setArCompanyName(rs.getString("COMP_NAME_AR"));	
					obj.setCreateDate(rs.getDate("Create_Date"));
					obj.setTotalDepartment(rs.getInt("TOT_DEP"));
					obj.setTotalPositions(rs.getInt("TOT_POS"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in DraftSalarySheetData---getCompanyList-->" , ex);
			}
		 return lst;
	}
	
	public List<DraftSalaryModel> getTotalCompanyEmployees()
	{
		  List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();		 			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getTotalCompanyEmployeesQuery());
				while(rs.next())
				{
					DraftSalaryModel obj=new DraftSalaryModel();
					obj.setCompKey(rs.getInt("COMP_KEY"));										
					obj.setTotalEmployees(rs.getInt("TotalEmployees"));
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in DraftSalarySheetData---getTotalCompanyEmployees-->" , ex);
			}
		 return lst;
	}
	
	public String getLastCompanySalaryDate(int companyKey,String type)
	{
		String result="";
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getCompanySalaryStatusQuery(companyKey, type));
			while(rs.next())
			{
				Date lastDate=rs.getDate("LASTDATE");	
				if(lastDate!=null)
				{
					  Calendar c = Calendar.getInstance();	
					  c.setTime(lastDate);
					  String monthName =new SimpleDateFormat("MMMM").format(c.getTime());
					  int year= c.get(Calendar.YEAR);	
					  result=monthName + "/" + String.valueOf(year) ;
				}
			}
			
			if(result.equals("") && type.equals("C"))
			{
				result="Not yet created";
			}
			if(result.equals("") && type.equals("A"))
			{
				result="Not yet Approved";
			}
			if(result.equals("") && type.equals("P"))
			{
				result="Not yet Paid";
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getLastCompanySalaryDate-->" , ex);
		}
		
		return result;
	}
	
	public List<EmployeeModel> getCompanyEmployees(int compKey)
	{
		List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
		try
		{
			ResultSet rs = null;
			int srNO=0;
			rs=db.executeNonQuery(query.getCompanyEmployeesQuery(compKey));
			while(rs.next())
			{
				srNO++;
				EmployeeModel obj=new EmployeeModel();
				obj.setSrNo(srNO);
				obj.setEmployeeKey(rs.getInt("EMP_KEY"));
				obj.setEmployeeNo(rs.getString("EMP_NO"));
				obj.setFullName(rs.getString("ENGLISH_FULL"));
				obj.setEmployeementDate(rs.getDate("EMPLOYEEMENT_DATE"));
				if(obj.getEmployeementDate()!=null)
				{
					obj.setEmployeementDateString(df.format(obj.getEmployeementDate()));
				}
				obj.setDepartment(rs.getString("Department"));
				obj.setPosition(rs.getString("Position"));
				obj.setPriorityNo(rs.getInt("PRIORITY_NO"));
				lstEmployees.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getCompanyEmployees-->" , ex);
		}
		return lstEmployees;
		
	}
	
	public int updateEmployeePriority(EmployeeModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.updateEmployeePriorityQuery(obj));
			result=1;
		}
		catch (Exception ex) 
		{
			logger.error("error in DraftSalarySheetData---updateEmployeePriority-->" , ex);
		}	
		return result;
	}
	
	public CompanyModel getCompanySetup(int compKey)
	{
			CompanyModel obj=new CompanyModel();	 			
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getCompanySetupQuery(compKey));
				while(rs.next())
				{
					obj.setDayWorkHours(rs.getInt("DAY_WORK_HRS"));
					obj.setSalaryCalcDays(rs.getInt("SALARY_CALC_DAYS"));
					obj.setShowSalaryColumn(rs.getString("SHOWSALARYCOLUMN"));
					obj.setEmpSalaryCalcDay(rs.getInt("EMPSALARY_CALC_DAY"));
					obj.setYearPayroll(rs.getInt("YEAR_PAYROLL"));
					obj.setMonthPayroll(rs.getInt("MONTH_PAYROLL"));
					obj.setCalculateActualDays(rs.getString("CALCULATEACTUALDAYS"));
					obj.setIncludeHolidayUnit(rs.getString("INCLUDEHOLIDAY_UNIT")==null?"N":rs.getString("INCLUDEHOLIDAY_UNIT"));									
				}
			}
			catch (Exception ex) {
				logger.error("error in DraftSalarySheetData---getCompanySetup-->" , ex);
			}
		 return obj;
	}
	
	public List<DraftSalaryModel> getTimeSheetHistoryEmployee(int compKey,int month,int year,int status,String empKeys)
	{
		List<DraftSalaryModel> lst =new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			Calendar c = Calendar.getInstance();	
			c.set(year,month-1,1);
			List<DraftSalaryModel> lstOT=getEmployeeOT(month, year, empKeys);
			rs=db.executeNonQuery(query.getTimeSheetHistoryEmployeeQuery(compKey, month, year, status,empKeys));
			int srNO=0;
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				srNO++;
				obj.setSrNO(srNO);
				obj.setEmpKey(rs.getInt("employeekey"));
				obj.setEmpNo(rs.getString("employeeno"));
				obj.setEnFullName(rs.getString("fullname"));
				obj.setDepartment(rs.getString("Department"));
				obj.setPosition(rs.getString("position"));
				obj.setEmployeementDate(rs.getDate("EMPLOYEEMENTDATE"));
				obj.setTotalDays(rs.getInt("totalDays"));
				obj.setTotalPresentDays(rs.getInt("Present"));
				obj.setTotalLeaveDays(rs.getInt("Leave"));
				double[] BasicSalary=getEmployeeSalary(obj.getEmpKey(), c.getTime());
				obj.setBasicSalary(BasicSalary[0]);
				obj.setTotalAllowance(BasicSalary[1]);
				obj.setTotalSalary(obj.getBasicSalary() + obj.getTotalAllowance());
				for (DraftSalaryModel otobj : lstOT) 
				{
					if(otobj.getEmpKey()==obj.getEmpKey())
					{
						obj.setOtAmount(otobj.getOtAmount());
						break;
					}
				}
				obj.setNetToPay(obj.getTotalSalary() + obj.getOtAmount());
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getTimeSheetHistoryEmployee-->" , ex);
		}
		return lst;
	}
	public double[] getEmployeeSalary(int empKey,Date tsDate)
	{	
		double[] result=new double[2];		
		ResultSet rs = null;	
			try 
			{			
				rs=db.executeNonQuery(query.getEmployeeSalaryQuery(empKey, tsDate));		
				while(rs.next())
				{										
					result[0]= rs.getDouble("BasicSal");
					result[1]= rs.getDouble("TotAllowance");
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in DraftSalarySheetData---getEmployeeSalary-->" , ex);
			}				
		return result;
	}
	
	public List<DraftSalaryModel> getEmployeeOT(int month,int year,String empKeys)
	{
		List<DraftSalaryModel> lst =new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeOTQuery(month, year, empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("labour_key"));
				obj.setOtAmount(rs.getDouble("OTAmount"));
				obj.setOtUnits(rs.getDouble("OTUnits"));
				obj.setOtCalculation(rs.getDouble("Calculation"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeOT-->" , ex);
		}
		return lst;
		
	}
	
	//Create Salary Sheet
	public List<DraftSalaryModel> getTimeSheetEmployee(int compKey,int month,int year,int status,String empKeys)
	{
		List<DraftSalaryModel> lst =new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;			
			rs=db.executeNonQuery(query.getTimeSheetEmployeeQuery(compKey, month, year, status,empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("LABOUR_KEY"));
				lst.add(obj);
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getTimeSheetEmployee-->" , ex);
		}
		return lst;
	}
	
	public boolean checkEmployeeSalary(int compKey,int month,int year,String empKeys,String type)
	{
		boolean isConditionFound=false;
		try
		{
			ResultSet rs = null;
			if(type.equals("Leave"))
			rs=db.executeNonQuery(query.checkLeavePaymentsQuery(compKey, month, year,empKeys));
			else if(type.equals("Additions"))
				rs=db.executeNonQuery(query.checkAdditionsPaymentsQuery(compKey, month, year,empKeys));
			else if(type.equals("Loans"))
				rs=db.executeNonQuery(query.checkLoanPaymentsQuery(compKey, month, year,empKeys));
			else if(type.equals("EOS"))
				rs=db.executeNonQuery(query.checkEOSQuery(compKey, month, year,empKeys));
			
			if(type.equals("EOS"))
			{
				while(rs.next())
				{
					Date eos=rs.getDate("EOS_DATE");
					Calendar c = Calendar.getInstance();
					c.setTime(eos);
					logger.info("EOS Month>>>" + (c.get(Calendar.MONTH)+1));
					if(c.get(Calendar.MONTH)+1!=month || c.get(Calendar.YEAR) != year)
					{
						isConditionFound=true;
					}
				}
			}
			else
			{
			while(rs.next())
			{
				isConditionFound=true;
			}
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---checkEmployeeSalary-->" , ex);
		}
		return isConditionFound;
	}
	
	public List<DraftSalaryModel> lstMissing=new ArrayList<DraftSalaryModel>();
	public List<DraftSalaryModel> getSummaryTimeSheetHistoryEmployee(int compKey,int month,int year,int status,String empKeys)
	{
		List<DraftSalaryModel> lst =new ArrayList<DraftSalaryModel>();
		
		try
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			DecimalFormat formatter = new DecimalFormat("#.##");	
			ResultSet rs = null;
			Date _fromDate=null;
			Date  _toDate=null;
			int  tmpMaxmonthDays=0;
			Calendar c = Calendar.getInstance();	
			c.set(year,month-1,1);
			_fromDate=df.parse(sdf.format(c.getTime()));	
			int maxMonthDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			c.set(year,month-1,maxMonthDays);
			_toDate=df.parse(sdf.format(c.getTime()));	
			tmpMaxmonthDays=maxMonthDays;//set as default
			
			List<Double> lstOTCalc=new ArrayList<Double>();
			rs=db.executeNonQuery(query.getCompanyOTCalculationQuery(compKey));
			while(rs.next())
			{
				lstOTCalc.add(rs.getDouble("OT_RATE"));
			}
			List<Integer> lstEmpKeys=new ArrayList<Integer>();
			
			List<DraftSalaryModel> lstTotalDays= getTimeSheetTotalDays(compKey, month, year, empKeys);
			List<DraftSalaryModel> lstAD=getEmployeeAdditionDeduction(compKey, month, year);
			List<DraftSalaryModel> lstOT=getEmployeeOT(month, year, empKeys);
			List<DraftSalaryModel> lstChkDays= checkTotalDays(compKey, month, year,empKeys);
			List<DraftSalaryModel> lstActualWorkingHours=getActualHoursSummaryTimeSheetHistoryEmployee(compKey, month, year, empKeys);
			//List<DraftSalaryModel> lsttmpLoans= getEmployeeLoansPaymentTemp(month, year,empKeys);
			rs=db.executeNonQuery(query.getSummaryTimeSheetHistoryEmployeeQuery(compKey, month, year, status,empKeys));
			int srNO=0;
			int missSrNO=0;
			//tmpMaxmonthDays=maxMonthDays;//set as default
			while(rs.next())
			{	
				tmpMaxmonthDays=maxMonthDays;//set as default
				Date employmentDate=df.parse(sdf.format(rs.getDate("EMPLOYEEMENTDATE")));
				if(employmentDate.after(_fromDate))
				{
					 int dayFrom=0;			 			 
					 int dayTo=0;
					 int diffDay=0;
					  
					  Calendar ctmp = Calendar.getInstance();	
					  ctmp.setTime(employmentDate);
					  dayFrom=ctmp.get(Calendar.DAY_OF_MONTH);			
					  ctmp.setTime(_toDate);				
					  dayTo=ctmp.get(Calendar.DAY_OF_MONTH);			  
					  diffDay=dayTo-dayFrom +1;	
					  tmpMaxmonthDays=diffDay;
				}
				
				boolean missingDays=false;								
				for (DraftSalaryModel item : lstChkDays) 
				{					
					if(item.getEmpKey()==rs.getInt("employeekey"))
					{					
						if(item.getTotalDays()<tmpMaxmonthDays)
						{
							missingDays=true;
						}
					}
				}
				
				if(missingDays)
				{
					missSrNO++;
					DraftSalaryModel obj=new DraftSalaryModel();
					obj.setSrNO(missSrNO);
					obj.setEmpKey(rs.getInt("employeekey"));
					obj.setEmpNo(rs.getString("employeeno"));
					obj.setEnFullName(rs.getString("fullname"));
					obj.setTotalDays(rs.getInt("TotalDays"));
					obj.setMissingDays(tmpMaxmonthDays - obj.getTotalDays());
					lstEmpKeys.add(obj.getEmpKey());
					lstMissing.add(obj);
					continue;
				}
				double otUnits[]=new double[lstOTCalc.size()];
				double otAmounts[]=new double[lstOTCalc.size()];
				DraftSalaryModel obj=new DraftSalaryModel();
				srNO++;
				obj.setSrNO(srNO);
				obj.setEmpKey(rs.getInt("employeekey"));
				obj.setEmpNo(rs.getString("employeeno"));
				obj.setEnFullName(rs.getString("fullname"));
				obj.setDepartment(rs.getString("Department"));
				obj.setPosition(rs.getString("position"));
				obj.setEmployeementDate(rs.getDate("EMPLOYEEMENTDATE"));
				obj.setTotalDays(rs.getInt("TotalDays"));
				for (DraftSalaryModel item : lstTotalDays) 
				{
					if(item.getEmpKey()==obj.getEmpKey())
					{
						obj.setTotalDays(item.getTotalDays());
					}
				}
				
				DraftSalaryModel objActSalary=getEmployeeActualSalryHours(obj.getEmpKey(),lstActualWorkingHours);
				if(objActSalary!=null)
				{
				obj.setTotalWorkingUnits(objActSalary.getTotalWorkingUnits());
				obj.setTotalActualUnits(objActSalary.getTotalActualUnits());
				obj.setSalaryActualHours(objActSalary.getSalaryActualHours());
				obj.setSalaryWorkHours(objActSalary.getSalaryWorkHours());
				}
				
				double[] BasicSalary=getEmployeeSalary(obj.getEmpKey(), c.getTime());
				obj.setBasicSalary(BasicSalary[0]);
				obj.setTotalAllowance(BasicSalary[1]);
				obj.setTotalSalary(obj.getBasicSalary() + obj.getTotalAllowance());
				
				//if(obj.getTotalDays() < maxMonthDays)
				if(obj.getTotalDays()!=0 && obj.getBasicSalary()!=0 && obj.getSalaryActualHours()!=0)
				{					
					
					//double salaryPerDay=obj.getBasicSalary()/maxMonthDays;
					double basicSalary=0; //salaryPerDay*obj.getTotalDays() ;
					double tmp=(obj.getBasicSalary()/obj.getSalaryActualHours())*obj.getSalaryWorkHours(); //(obj.getBasicSalary() /maxMonthDays) * obj.getTotalDays();
					tmp= (((obj.getBasicSalary()/maxMonthDays)*tmpMaxmonthDays)/obj.getSalaryActualHours())*obj.getSalaryWorkHours();
					basicSalary=tmp;//(((obj.getBasicSalary()/maxMonthDays)*tmpMaxmonthDays)/obj.getSalaryActualHours()) * obj.getSalaryWorkHours();
									
					obj.setBasicSalary( Double.valueOf(formatter.format(basicSalary)));
					
					tmp=(obj.getTotalAllowance()/obj.getSalaryActualHours())*obj.getSalaryWorkHours();
					tmp=(((obj.getTotalAllowance() /maxMonthDays)*tmpMaxmonthDays)/obj.getSalaryActualHours())*obj.getSalaryWorkHours();
					obj.setTotalAllowance( Double.valueOf(formatter.format(tmp)));
					
				}
				
				int i=0;
				double totalOTAmount=0;
				for (DraftSalaryModel otobj : lstOT) 
				{
					if(otobj.getEmpKey()==obj.getEmpKey())
					{
						i=0;
						//otUnits=new double[lstOTCalc.size()];
						for (Double otCal : lstOTCalc) 
						{
							if(otCal==otobj.getOtCalculation())
							{
								otUnits[i]=otobj.getOtUnits();
								otAmounts[i]=otobj.getOtAmount();
								totalOTAmount+=otobj.getOtAmount();
							}
							i++;
						}
						//obj.setOtAmount(otobj.getOtAmount());
						//obj.setOtUnits(otobj.getOtUnits());
						//break;
					}
				}
				obj.setOtArrayUnits(otUnits);
				obj.setOtArrayAmount(otAmounts);
				obj.setOtAmount(totalOTAmount);
				obj.setAdditions(0);
				obj.setDeductions(0);
				obj.setSubTotal(obj.getBasicSalary() + obj.getOtAmount() + obj.getTotalAllowance());
				obj.setSubTotal( Double.valueOf(formatter.format(obj.getSubTotal())));
				obj.setLoans(0);
				for (DraftSalaryModel item : lstAD) 
				{
					if(item.getEmpKey()==obj.getEmpKey())
					{
						obj.setAdditions(item.getAdditions());
						obj.setDeductions(item.getDeductions());
					}
				}
				recalculateLoanAmount(obj.getEmpKey(), month, year, obj.getSalaryWorkHours());
				List<DraftSalaryModel> lsttmpLoans= getEmployeeLoansPaymentTemp(month, year,empKeys);
				for (DraftSalaryModel item : lsttmpLoans) 
				{
					if(item.getEmpKey()==obj.getEmpKey())
					{
						obj.setLoans(item.getLoans());
					}
				}
				
				obj.setActualSalary(obj.getSubTotal() + obj.getAdditions() - obj.getDeductions()  - obj.getLoans());
				obj.setNetToPay(obj.getActualSalary());
				obj.setPaidAmount(0);
				
				obj.setRemarks("");
								
				DraftSalaryModel objSalary=getEmployeeSalary(compKey, obj.getEmpKey(), month, year);
				obj.setPaidAmount(objSalary.getPaidAmount());
				double tmp=obj.getNetToPay() - obj.getPaidAmount();				
				obj.setBalance(Double.valueOf(formatter.format(tmp)));
				obj.setRemarks(objSalary.getRemarks());
				obj.setSalaryStatus(objSalary.getSalaryStatus());
				lstEmpKeys.add(obj.getEmpKey());
				lst.add(obj);
			}
			
			//check the missing entries
			for (DraftSalaryModel item : lstChkDays) 
			{
				if(!lstEmpKeys.contains(item.getEmpKey()))
				{
					missSrNO++;
					DraftSalaryModel obj=new DraftSalaryModel();
					obj.setSrNO(missSrNO);
					obj.setEmpKey(item.getEmpKey());
					obj.setEmpNo(item.getEmpNo());
					obj.setEnFullName(item.getEnFullName());
					obj.setTotalDays(item.getTotalDays());
					if(obj.getTotalDays() > tmpMaxmonthDays)
						obj.setMissingDays(0);
					else
					obj.setMissingDays(tmpMaxmonthDays - obj.getTotalDays());
					obj.setRemarks("Not Approved");
					lstEmpKeys.add(obj.getEmpKey());
					lstMissing.add(obj);					
				}
			}
			
			//check if employee select from filter employee and no data found
			if(empKeys!=null && !empKeys.equals(""))
			{
			List<DraftSalaryModel> lstEmp= getEmployeeDetail(empKeys);
			String[] arrEmpKeys= empKeys.split(",");
			List<String> tmpKeysList = Arrays.asList(arrEmpKeys); 
			for (String tmpKey : tmpKeysList) 
			{
			Integer emptmpKey=Integer.parseInt(tmpKey);
			if(!lstEmpKeys.contains(emptmpKey))
			{
				missSrNO++;
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setSrNO(missSrNO);
				obj.setEmpKey(emptmpKey);
				for (DraftSalaryModel objEmp : lstEmp) 
				{
					if(objEmp.getEmpKey()==obj.getEmpKey())
					{
						obj.setEmpNo(objEmp.getEmpNo());
						obj.setEnFullName(objEmp.getEnFullName());
						break;
					}
				}						
				obj.setTotalDays(0);
				obj.setMissingDays(tmpMaxmonthDays - obj.getTotalDays());
				obj.setRemarks("Timesheet is not created for the month.");
				lstEmpKeys.add(obj.getEmpKey());
				lstMissing.add(obj);	
			}
		   }			
		  }
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getSummaryTimeSheetHistoryEmployee-->" , ex);
		}
		return lst;
	}
	
	public List<String> getCompanyOTCalculation(int compKey)
	{
		List<String> lst=new ArrayList<String>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getCompanyOTCalculationQuery(compKey));
			while(rs.next())
			{
				lst.add(rs.getString("OT_RATE"));
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getCompanyOTCalculation-->" , ex);
		}
		return lst;
	}
	
	public DraftSalaryModel getEmployeeSalary(int compKey,int empKey,int month,int year)
	{
		DraftSalaryModel obj=new DraftSalaryModel();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeSalaryQuery(compKey, empKey, month, year));
			while(rs.next())
			{
				obj.setEmpKey(rs.getInt("EMP_KEY"));
				obj.setRecNo(rs.getInt("Rec_No"));
				String status=rs.getString("SALARY_STATUS")==null?"":rs.getString("SALARY_STATUS");
				obj.setSalaryStatus(status);
				if(status.equals("A"))
				obj.setRemarks("Approved");
				else if(status.equals("C"))
					obj.setRemarks("Created");
				
				obj.setPaidAmount(rs.getDouble("PAID_AMOUNT"));	
				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeSalary-->" , ex);
		}
		return obj;
	}
	
	public List<DraftSalaryModel> getTimeSheetTotalDays(int compKey,int month,int year,String empKeys)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getTimeSheetTotalDaysQuery(compKey, month, year,empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("LABOUR_KEY"));
				obj.setTotalDays(rs.getInt("TotalDays"));					
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getTimeSheetTotalDays-->" , ex);
		}
		return lst;
	}
	
	public List<DraftSalaryModel> getActualHoursSummaryTimeSheetHistoryEmployee(int compKey,int month,int year,String empKeys)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getActualHoursSummaryTimeSheetHistoryEmployeeQuery(compKey, month, year,empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("LABOUR_KEY"));
				obj.setTotalWorkingUnits(rs.getDouble("WorkHrs"));
				obj.setTotalActualUnits(rs.getDouble("ActualHrs"));
				obj.setSalaryActualHours(rs.getDouble("SalaryActualHrs"));
				obj.setSalaryWorkHours(rs.getDouble("SalaryWorkHrs"));					
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getActualHoursSummaryTimeSheetHistoryEmployee-->" , ex);
		}
		return lst;
	}
	public List<DraftSalaryModel> getEmployeeDetail(String empKeys)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeDetailQuery(empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("employeekey"));
				obj.setEmpNo(rs.getString("employeeno"));
				obj.setEnFullName(rs.getString("fullname"));
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeDetailQuery-->" , ex);
		}
		return lst;
	}
	private DraftSalaryModel getEmployeeActualSalryHours(int empKey,List<DraftSalaryModel> lstSalary)
	{
		DraftSalaryModel obj=null;
		for (DraftSalaryModel item : lstSalary)
		{
		 if(item.getEmpKey()==empKey)
			return item;
		}
		return obj;
	}
	
	public List<DraftSalaryModel> getEmployeeAdditionDeduction(int compKey,int month,int year)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeAdditionDeductionQuery(compKey, month, year));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("EMP_KEY"));
				obj.setAdditions(rs.getDouble("APayable"));
				obj.setDeductions(rs.getDouble("DPayable"));
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeAdditionDeduction-->" , ex);
		}
		return lst;
	}
	
	public List<DraftSalaryModel> checkTotalDays(int compKey,int month,int year,String empKeys)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.checkTotalDaysQuery(compKey, month, year,empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("LABOUR_KEY"));
				obj.setTotalDays(rs.getInt("TOTEntrys"));
				obj.setEmpNo(rs.getString("employeeno"));
				obj.setEnFullName(rs.getString("fullname"));				
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---checkTotalDays-->" , ex);
		}
		return lst;
	}
	
	public List<DraftSalaryModel> getEmployeeLoansPaymentTemp(int month,int year,String empKeys)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeLoansPaymentTempQuery(empKeys, month, year));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setEmpKey(rs.getInt("EMP_KEY"));
				obj.setLoans(rs.getDouble("LoanInstAmt"));				
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeLoansPaymentTemp-->" , ex);
		}
		return lst;
	}
	
	//recalculate Loan install amount
	public void recalculateLoanAmount(int empKey,int month,int year,double salaryWorkHrs)
	{
		try
		{
			logger.info("salaryWorkHrs>> " + salaryWorkHrs);
			ResultSet rs,rs1 = null;
			int loanRecNo=0,loanRepRecNo=0;
			Calendar c = Calendar.getInstance();	
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today=sdf.format(c.getTime());	
			
			db.executeUpdateQuery(query.deleteLoanRepaymentTempQuery(empKey, month, year));
			if(salaryWorkHrs==0)
			{
				db.executeUpdateQuery(query.updateLoanRepaymentQuery(empKey, month, year, 0));
				rs=db.executeNonQuery(query.getLoanMastQuery(empKey, month, year));
				while(rs.next())
				{
					loanRecNo=rs.getInt("rec_No");
					rs1=db.executeNonQuery(query.getLoanRepaymentQuery(loanRecNo, month, year, empKey));
					while(rs1.next())
					{
						loanRepRecNo=rs1.getInt("rec_No");											
					}
					if(loanRepRecNo==0)
					{
						int tmpInstNo=getInsallNo(loanRecNo, "LOANREPAYMENT");
						int tmpRepayRecNo=GenerateID("LOANREPAYMENT", "REC_NO");						
						db.executeUpdateQuery(query.insertLoanRepaymentQuery(tmpRepayRecNo, today, loanRecNo, empKey, month, year, tmpInstNo, 0));
					}
					else
					{
						db.executeUpdateQuery(query.updateLoanRepaymentQuery(empKey, month, year, loanRecNo));
					}
				}				
			}
			
			rs=db.executeNonQuery(query.getLoanRepaymentQuery(0, month, year, empKey));
			while(rs.next())
			{
				db.executeUpdateQuery(query.insertLoanRepaymentTempQuery(empKey, month, year));				
			}
			rs=db.executeNonQuery(query.getLoanMastQuery(empKey, month, year));
			while(rs.next())
			{
				loanRecNo=rs.getInt("rec_No");
				double loanAmount=rs.getDouble("LOAN_AMOUNT");
				double rcvdAmount=rs.getDouble("RCVD_AMOUNT");
				double instAmount=rs.getDouble("INST_AMOUNT");
				double tmpInstBalance=0;
				rs1=db.executeNonQuery(query.getLoanRepaymentTempQuery(loanRecNo, month, year, 0));
				if(rs1.next()==false)
				{
					int tmpInstNo=getInsallNo(loanRecNo, "LOANREPAYMENTTEMP");
					int tmpRepayRecNo=GenerateID("LOANREPAYMENTTEMP", "REC_NO");
					if(loanAmount-rcvdAmount>=instAmount)
						tmpInstBalance=instAmount;
					else
						tmpInstBalance=loanAmount-rcvdAmount;
					
					db.executeUpdateQuery(query.insertNewLoanRepaymentTempQuery(tmpRepayRecNo, today, loanRecNo, empKey, month, year, tmpInstNo, tmpInstBalance));
				}
				
			}
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---recalculateLoanAmount-->" , ex);
		}
		
	}
	private int getInsallNo(int loanRecNo,String strTableName)
	{
		int result=1;
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getNewInstallNoQuery(loanRecNo, strTableName));
			while(rs.next())
			{
				result=rs.getInt("InstNo")+1;
			}
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getInsallNo-->" , ex);
		}
		return result;
	}
	public int GenerateID(String strTableName,String strIDField)
	{
		int result=1;
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.generateIDQuery(strTableName,strIDField));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getInsallNo-->" , ex);
		}
		return result;
	}
	
	//save salary
	public List<DraftSalaryModel> getEmployeeMaster(String empKeys)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			rs=db.executeNonQuery(query.getEmployeeMasterQuery(empKeys));
			while(rs.next())
			{
				DraftSalaryModel obj=new DraftSalaryModel();				
				obj.setEmpKey(rs.getInt("EMP_KEY"));
				obj.setDepId(rs.getInt("DEP_ID"));
				obj.setPayMode(rs.getString("SALARY_PAYMODE")==null?"C":rs.getString("SALARY_PAYMODE"));
				obj.setBankId(rs.getInt("BANK_ID"));
				obj.setBranchId(rs.getInt("BRANCH_ID"));
				obj.setAccountNO(rs.getString("Account_No"));
				obj.setPosId(rs.getInt("POS_ID"));
				obj.setGradeId(rs.getInt("CLEVEL"));
				obj.setSectionId(rs.getInt("Section_ID"));
				obj.setClassId(rs.getInt("CLASS_ID"));
				obj.setSifStatus(rs.getBoolean("SIF_STATUS")?1:0);								
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeMaster-->" , ex);
		}
		return lst;
	}
	public void deleteSalaryOT(int recNo)
	{		
		try
		{			
			db.executeUpdateQuery(query.deleteSalaryOTQuery(recNo));		
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---deleteSalaryOT-->" , ex);
		}
		
	}
	
	public void insertSalaryMaster(DraftSalaryModel obj,int month,int year,String today,int compKey)
	{		
		try
		{			
			db.executeUpdateQuery(query.insertSalaryMastQuery(obj, month, year, today, compKey));
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---insertSalaryMaster-->" , ex);
		}
		
	}
	public void updateSalaryMaster(DraftSalaryModel obj,int month,int year,String today,int compKey)
	{		
		try
		{			
			db.executeUpdateQuery(query.updateSalaryMastQuery(obj, month, year, today, compKey));
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---updateSalaryMaster-->" , ex);
		}
		
	}
	
	
	public void deleteSalaryAllowances(int recNo)
	{		
		try
		{			
			db.executeUpdateQuery(query.deleteSalaryAllowancesQuery(recNo));		
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---deleteSalaryAllowances-->" , ex);
		}
		
	}
	
	public void insertSalaryOT(int recNo ,int month,int year,double otRate,double otHours, double otAmount)
	{		
		try
		{			
			db.executeUpdateQuery(query.insertSalaryOTQuery(recNo, month, year, otRate, otHours, otAmount));
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---insertSalaryOT-->" , ex);
		}
		
	}
	
	//Approve salary
	public List<DraftSalaryModel> getEmployeeApproveSalary(int compKey,String empKeys,int month,int year)
	{
		List<DraftSalaryModel> lst=new ArrayList<DraftSalaryModel>();
		try
		{
			ResultSet rs = null;
			DecimalFormat formatter = new DecimalFormat("#.##");	
			int srNo=0;
			rs=db.executeNonQuery(query.getEmployeeApproveSalaryQuery(compKey, empKeys, month, year));
			while(rs.next())
			{
				srNo++;				
				DraftSalaryModel obj=new DraftSalaryModel();
				obj.setSrNO(srNo);
				obj.setEmpKey(rs.getInt("employeekey"));
				obj.setEmpNo(rs.getString("employeeno"));
				obj.setEnFullName(rs.getString("fullname"));
				obj.setDepartment(rs.getString("Department"));
				obj.setPosition(rs.getString("position"));
				obj.setEmployeementDate(rs.getDate("EMPLOYEEMENTDATE"));				
				obj.setRecNo(rs.getInt("Rec_No"));
				String status=rs.getString("SALARY_STATUS")==null?"":rs.getString("SALARY_STATUS");
				obj.setSalaryStatus(status);
				if(status.equals("A"))
				obj.setRemarks("Approved");
				else if(status.equals("C"))
					obj.setRemarks("Created");
				
				obj.setTotalDays(rs.getInt("WORKING_DAYS"));
				obj.setTotalWorkingUnits(rs.getDouble("TOTAL_WORKHRS"));
				obj.setBasicSalary(rs.getDouble("BASIC_SALARY"));
				obj.setTotalAllowance(rs.getDouble("TOTAL_ALLOWANCE"));
				obj.setOtAmount(rs.getDouble("TOTAL_OT"));
				obj.setAdditions(rs.getDouble("TOTAL_ADDITION"));
				obj.setDeductions(rs.getDouble("TOTAL_DEDUCTION"));
				obj.setLoans(rs.getDouble("TOTAL_LOAN"));
				obj.setNetToPay(rs.getDouble("NET2PAY"));
				obj.setActualSalary(obj.getNetToPay());
				obj.setPaidAmount(rs.getDouble("PAID_AMOUNT"));	
				obj.setSubTotal(obj.getBasicSalary() + obj.getOtAmount() + obj.getTotalAllowance());
				obj.setSubTotal( Double.valueOf(formatter.format(obj.getSubTotal())));
				double tmp=obj.getNetToPay() - obj.getPaidAmount();				
				obj.setBalance(Double.valueOf(formatter.format(tmp)));
				lst.add(obj);				
			}
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---getEmployeeApproveSalary-->" , ex);
		}
		return lst;
	}
	
	public void approveSalary(int recNo,int compKey,int month,int year)
	{		
		try
		{			
			db.executeUpdateQuery(query.approveSalaryQuery(recNo));	
			db.executeUpdateQuery(query.approveSalaryAllowancesQuery(recNo));
			db.executeUpdateQuery(query.updateCompanySalaryStatusQuery(compKey, month, year));
			
		}
		catch (Exception ex) {
			logger.error("error in DraftSalarySheetData---approveSalary-->" , ex);
		}
		
	}
	
}
