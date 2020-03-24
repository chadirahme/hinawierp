package payroll;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import model.DraftSalaryModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class SalaryDetailViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());	
	DraftSalarySheetData data=new DraftSalarySheetData();
	private List<String> lstOTCalculation;
	private ListModelList<String> columnUnitList;
	private ListModelList<String> columnAmountList;
	private List<DraftSalaryModel> lstEmployeeDraftSalary;
	private List<DraftSalaryModel> lstMissedEmployeeDraftSalary;
	private Set<DraftSalaryModel> selectedEntities;	
	
	int companyKey;
	int month;
	int year;
	String empkeys;
	private boolean missingListHasData;
	
	private double totalSalary;
	private double totalBasicSalary;
	private double totalAllowances;
	private double totalOTAmount;
	private double totalSubTotal;
	private double totalLoans;
	private double totalAdditions;
	private double totalDeductions;
	private double totalActualSalary;
	private double totalNetPay;
	private double totalPaidAmount;
	private double totalBalance;
	

	@Init
	public void init(@BindingParam("compKey")int compKey,@BindingParam("month")int passmonth,@BindingParam("year")int passyear,@BindingParam("empkeys")String passempkeys)
	{
		companyKey=compKey;
		month=passmonth;
		year=passyear;
		empkeys=passempkeys;
		logger.info(" empkeys>>" + empkeys);
		grdSalarySheet_Refresh();
	}
	private void grdSalarySheet_Refresh()
	{
	
	  try
	  {
	  lstOTCalculation=data.getCompanyOTCalculation(companyKey);
	  columnUnitList=new ListModelList<String>();
	  columnAmountList=new ListModelList<String>();
	  
	  for (String item : lstOTCalculation)	
		{
			//if(!columnList.contains(item))
		  columnUnitList.add("Hrs. (" + item + ")");
		}
	  
	  for (String item : lstOTCalculation)	
		{
			//if(!columnList.contains(item))
		  columnAmountList.add("Amt. (" + item + ")");
		}
	  
	  lstEmployeeDraftSalary=data.getSummaryTimeSheetHistoryEmployee(companyKey, month, year, 1,empkeys);
	  for (DraftSalaryModel item : lstEmployeeDraftSalary)
	  {
		totalSalary+=item.getTotalSalary();
		totalBasicSalary+=item.getBasicSalary();
		totalAllowances+=item.getTotalAllowance();
		totalOTAmount+=item.getOtAmount();
		totalSubTotal+=item.getSubTotal();
		totalLoans+=item.getLoans();
		totalAdditions+=item.getAdditions();
		totalDeductions+=item.getDeductions();
		totalActualSalary+=item.getActualSalary();
		totalNetPay+=item.getNetToPay();
		totalPaidAmount+=item.getPaidAmount();
		totalBalance+=item.getBalance();
		
	  }
	  lstMissedEmployeeDraftSalary=data.lstMissing;
	  if(lstMissedEmployeeDraftSalary.size()>0)
		  missingListHasData=true;
	  }
	  catch (Exception ex) {
			logger.error("error in SalaryDetailViewModel---grdSalarySheet_Refresh-->" , ex);
		}
	  	  
	}
	
	@Command
	@NotifyChange({"*"})
	public void clearCommand()
	{
		lstEmployeeDraftSalary=new ArrayList<DraftSalaryModel>();
		lstMissedEmployeeDraftSalary=new ArrayList<DraftSalaryModel>();
		totalSalary=0;
		 totalBasicSalary=0;
		totalAllowances=0;
		totalOTAmount=0;
		totalSubTotal=0;
		totalLoans=0;
		totalAdditions=0;
		totalDeductions=0;
		totalActualSalary=0;
		totalNetPay=0;
		totalPaidAmount=0;
		totalBalance=0;
	}
	
	@Command
	@NotifyChange({"lstEmployeeDraftSalary","lstMissedEmployeeDraftSalary"})
	public void saveCommand()
	{
		try
		{
			String fromDate,toDate;
			Date _fromDate=null , _toDate=null;
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			
			Calendar c = Calendar.getInstance();	
			c.set(year,month-1,1);
			_fromDate=df.parse(sdf.format(c.getTime()));	
			fromDate=sdf1.format(c.getTime());
			int maxMonthDays=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			c.set(year,month-1,maxMonthDays);
			_toDate=df.parse(sdf.format(c.getTime()));	
			toDate=sdf1.format(c.getTime());
			int tmpSaveEmployees=0;
			
			Calendar c1 = Calendar.getInstance();	
			
			String today=sdf1.format(c1.getTime());	
			
			boolean isvalidData=true;
			List<Integer> lstEmployeeId=new ArrayList<Integer>();			
			if(selectedEntities!=null)
			{
				for (DraftSalaryModel item : selectedEntities) 
				{
					lstEmployeeId.add(item.getEmpKey());
				}					
			}
			
			if(lstEmployeeId.size()==0)
			{
				Messagebox.show("Please Check at least one employee to save !!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			
			for (DraftSalaryModel item : lstEmployeeDraftSalary) 
			{
				if(item.getTotalDays() > maxMonthDays)
				{
					isvalidData=false;
					break;
				}
			}
			
			if(isvalidData==false)
			{
				 Messagebox.show("Please Check the Total Number of Days!!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			for (DraftSalaryModel item : lstEmployeeDraftSalary) 
			{
				if(item.getNetToPay() <0)
				{
					isvalidData=false;
					break;
				}
			}
			if(isvalidData==false)
			{
				Messagebox.show("Please Check Net to pay!!","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			List<DraftSalaryModel> lstEmpMaster=data.getEmployeeMaster(empkeys);
			int tmpRecNo=0;
			for (DraftSalaryModel item : lstEmployeeDraftSalary) 
			{
				if(!lstEmployeeId.contains(item.getEmpKey()))
				{
					continue;
				}
				
				int salaryRecNo=0;
				DraftSalaryModel obj=data.getEmployeeSalary(companyKey, item.getEmpKey(), month, year);
				salaryRecNo=obj.getRecNo();
				if(salaryRecNo>0 && obj.getSalaryStatus().equals("P"))
				{
					continue;
				}
				
				if(salaryRecNo>0 && obj.getSalaryStatus().equals("A"))
				{
					continue;
				}
				
				tmpSaveEmployees++;
				
				if(salaryRecNo>0)
				{
					//update old salary
					data.deleteSalaryOT(salaryRecNo);
					
					item.setRecNo(salaryRecNo);
					tmpRecNo=salaryRecNo;
					for (DraftSalaryModel objEmployee : lstEmpMaster) 
					{
						if(objEmployee.getEmpKey()==item.getEmpKey())
						{
							item.setPayMode(objEmployee.getPayMode());
							item.setBankId(objEmployee.getBankId());
							item.setBranchId(objEmployee.getBranchId());
							item.setAccountNO(objEmployee.getAccountNO());							
							item.setDepId(objEmployee.getDepId());
							item.setPosId(objEmployee.getPosId());
							item.setGradeId(objEmployee.getGradeId());
							item.setSectionId(objEmployee.getSectionId());
							item.setSectionId(objEmployee.getSectionId());
							item.setSifStatus(objEmployee.getSifStatus());							
						}
					}
					
					data.updateSalaryMaster(item, month, year, today, companyKey);
					
				}
				else
				{
					tmpRecNo=data.GenerateID("SALARYMASTTS", "Rec_No");
					item.setRecNo(tmpRecNo);
					item.setFromDate(fromDate);
					item.setToDate(toDate);
					for (DraftSalaryModel objEmployee : lstEmpMaster) 
					{
						if(objEmployee.getEmpKey()==item.getEmpKey())
						{
							item.setPayMode(objEmployee.getPayMode());
							item.setBankId(objEmployee.getBankId());
							item.setBranchId(objEmployee.getBranchId());
							item.setAccountNO(objEmployee.getAccountNO());							
							item.setDepId(objEmployee.getDepId());
							item.setPosId(objEmployee.getPosId());
							item.setGradeId(objEmployee.getGradeId());
							item.setSectionId(objEmployee.getSectionId());
							item.setSectionId(objEmployee.getSectionId());
							item.setSifStatus(objEmployee.getSifStatus());							
						}
					}
					
					data.insertSalaryMaster(item, month, year, today, companyKey);
					
				}								
				data.deleteSalaryAllowances(tmpRecNo);
				//save OT
				List<DraftSalaryModel> lstOT=data.getEmployeeOT(month, year, item.getEmpKey()+"");
				for (DraftSalaryModel otObj : lstOT) 
				{
					data.insertSalaryOT(tmpRecNo, month, year, otObj.getOtCalculation(), otObj.getOtUnits(), otObj.getOtAmount());
				}
				
				
			}
			
			 Messagebox.show("Salary Sheet Saved Successfully !! ","Salary Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			
		}
		catch (Exception ex) {
			logger.error("error in SalaryDetailViewModel---saveCommand-->" , ex);
		}
	}
	
	@Command
	public void approvedCommand()
	{
		 Executions.createComponents("/timesheet/approvetimesheet.zul", null, null);
	}
	
	@Command
	@NotifyChange({"lstEmployeeDraftSalary","lstMissedEmployeeDraftSalary"})
	public void refreshCommand()
	{
		data.lstMissing=new ArrayList<DraftSalaryModel>();
		grdSalarySheet_Refresh();
	}
	
	public ListModelList<String> getColumnUnitList() {
		return columnUnitList;
	}
	public void setColumnUnitList(ListModelList<String> columnUnitList) {
		this.columnUnitList = columnUnitList;
	}
	public ListModelList<String> getColumnAmountList() {
		return columnAmountList;
	}
	public void setColumnAmountList(ListModelList<String> columnAmountList) {
		this.columnAmountList = columnAmountList;
	}
	public List<DraftSalaryModel> getLstEmployeeDraftSalary() {
		return lstEmployeeDraftSalary;
	}
	public void setLstEmployeeDraftSalary(List<DraftSalaryModel> lstEmployeeDraftSalary) {
		this.lstEmployeeDraftSalary = lstEmployeeDraftSalary;
	}
	public List<DraftSalaryModel> getLstMissedEmployeeDraftSalary() {
		return lstMissedEmployeeDraftSalary;
	}
	public void setLstMissedEmployeeDraftSalary(
			List<DraftSalaryModel> lstMissedEmployeeDraftSalary) {
		this.lstMissedEmployeeDraftSalary = lstMissedEmployeeDraftSalary;
	}
	public boolean isMissingListHasData() {
		return missingListHasData;
	}
	public void setMissingListHasData(boolean missingListHasData) {
		this.missingListHasData = missingListHasData;
	}
	public double getTotalSalary() {
		return totalSalary;
	}
	public void setTotalSalary(double totalSalary) {
		this.totalSalary = totalSalary;
	}
	public double getTotalBasicSalary() {
		return totalBasicSalary;
	}
	public void setTotalBasicSalary(double totalBasicSalary) {
		this.totalBasicSalary = totalBasicSalary;
	}
	public double getTotalAllowances() {
		return totalAllowances;
	}
	public void setTotalAllowances(double totalAllowances) {
		this.totalAllowances = totalAllowances;
	}
	public double getTotalOTAmount() {
		return totalOTAmount;
	}
	public void setTotalOTAmount(double totalOTAmount) {
		this.totalOTAmount = totalOTAmount;
	}
	public double getTotalSubTotal() {
		return totalSubTotal;
	}
	public void setTotalSubTotal(double totalSubTotal) {
		this.totalSubTotal = totalSubTotal;
	}
	public double getTotalLoans() {
		return totalLoans;
	}
	public void setTotalLoans(double totalLoans) {
		this.totalLoans = totalLoans;
	}
	public double getTotalAdditions() {
		return totalAdditions;
	}
	public void setTotalAdditions(double totalAdditions) {
		this.totalAdditions = totalAdditions;
	}
	public double getTotalDeductions() {
		return totalDeductions;
	}
	public void setTotalDeductions(double totalDeductions) {
		this.totalDeductions = totalDeductions;
	}
	public double getTotalActualSalary() {
		return totalActualSalary;
	}
	public void setTotalActualSalary(double totalActualSalary) {
		this.totalActualSalary = totalActualSalary;
	}
	public double getTotalNetPay() {
		return totalNetPay;
	}
	public void setTotalNetPay(double totalNetPay) {
		this.totalNetPay = totalNetPay;
	}
	public double getTotalPaidAmount() {
		return totalPaidAmount;
	}
	public void setTotalPaidAmount(double totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}
	public double getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(double totalBalance) {
		this.totalBalance = totalBalance;
	}
	public Set<DraftSalaryModel> getSelectedEntities() {
		return selectedEntities;
	}
	public void setSelectedEntities(Set<DraftSalaryModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
	}
	
	
}
