package reports;

import hr.HRQueries;
import hr.model.CompanyModel;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.CompanyDBModel;
import model.SalaryMasterModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import company.CompanyData;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class SalarySheetDetailedData {

	
	private Logger logger = Logger.getLogger(this.getClass());
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//LiveDBHandler db=new LiveDBHandler("");
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	int parentID=1;
	
	public SalarySheetDetailedData()
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
			logger.error("error in SalarySheetDetailedData---Init-->" , ex);
		}
	}
	
	
	public List<AllowanceModel> retrivingSalaryItems(int comapnyId,int month)
	{
		List<AllowanceModel> lst=new ArrayList<AllowanceModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		ResultSet rs = null;
		try
		{
		rs=db.executeNonQuery(query.retrivingSalaryItems(comapnyId, month));
		while(rs.next())
		{
			AllowanceModel obj=new AllowanceModel();
			obj.setAllowanceId(rs.getInt("allowance_id"));					
			obj.setAllowanceName(rs.getString("allowancedesc"));
			obj.setAllowanceNameAr(rs.getString("allowancedescar"));					
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retrivingSalaryItems-->" , ex);
		}
		return lst;
	}
	
	public List<AdditionModel> retrivingAdditionColumns(int comapnyId)
	{
		List<AdditionModel> lst=new ArrayList<AdditionModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		ResultSet rs = null;
		try
		{
		rs=db.executeNonQuery(query.retrivingAdditionColumns(comapnyId));
		while(rs.next())
		{
			AdditionModel obj=new AdditionModel();
			obj.setAdd_Id(rs.getInt("ad_id"));					
			obj.setDescription(rs.getString("description"));
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retrivingAdditionColumns-->" , ex);
		}
		return lst;
	}
	
	public List<DiductionModel> retrivingDiductionColumns(int comapnyId)
	{
		List<DiductionModel> lst=new ArrayList<DiductionModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		ResultSet rs = null;
		try
		{
		rs=db.executeNonQuery(query.retrivingDiductionColumns(comapnyId));
		while(rs.next())
		{
			DiductionModel obj=new DiductionModel();
			obj.setAdd_id(rs.getInt("ad_id"));					
			obj.setDescription(rs.getString("description"));
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retrivingDiductionColumns-->" , ex);
		}
		return lst;
	}
	
	public List<OverTimeModel> retriveOT(int comapnyId)
	{
		List<OverTimeModel> lst=new ArrayList<OverTimeModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		ResultSet rs = null;
		try
		{
		rs=db.executeNonQuery(query.retriveOT(comapnyId));
		while(rs.next())
		{
			OverTimeModel obj=new OverTimeModel();
			obj.setOvertimeRate(rs.getDouble("ot_rate"));					
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retriveOT-->" , ex);
		}
		return lst;
	}
	
	
	public List<SalaryMasterModel> retriveTheMainreportData(int comapnyId,int month,int year,String salarystatus,int empKey,String filterEmpKeys,int supervisorId)
	{
		List<SalaryMasterModel> lst=new ArrayList<SalaryMasterModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		
		List<AllowanceModel> items=new ArrayList<AllowanceModel>();
		List<AllowanceModel> itemsColumn=new ArrayList<AllowanceModel>();
		List<OverTimeModel> overTime=new ArrayList<OverTimeModel>();
		List<OverTimeModel> overTimeColumn=new ArrayList<OverTimeModel>();
		List<AdditionModel> addition=new ArrayList<AdditionModel>();
		List<AdditionModel> additionData=new ArrayList<AdditionModel>();
		List<DiductionModel> dedcution=new ArrayList<DiductionModel>();
		List<DiductionModel> dedcutionData=new ArrayList<DiductionModel>();
		ResultSet rs = null;
		ResultSet rsAdd = null;
		ResultSet rsdiductn = null;
		try
		{
			items=retriveSalaryAmount();
			itemsColumn=retrivingSalaryItems(comapnyId,month);
			overTimeColumn=retriveOTHoursAndRate();
			overTime=retriveOT(comapnyId);
			addition=retrivingAdditionColumns(comapnyId);
			dedcution=retrivingDiductionColumns(comapnyId);
			
			rsAdd=db.executeNonQuery(query.subQueryAddition(month,year));
			while(rsAdd.next())
			{
				AdditionModel additionModel=new AdditionModel();
				additionModel.setEmpKey(rsAdd.getInt("emp_key"));
				additionModel.setAdd_Id(rsAdd.getInt("ad_id"));
				additionModel.setAdAmount(Double.parseDouble(new DecimalFormat("#.##").format(rsAdd.getDouble("adAmount"))));
				additionModel.setAhValueFlag(rsAdd.getString("ah_value_flag"));
				additionModel.setAhValue(Double.parseDouble(new DecimalFormat("#.##").format(rsAdd.getDouble("ah_value"))));
				additionModel.setDescription(rsAdd.getString("description"));
				additionData.add(additionModel);
				
			}
			
			rsdiductn=db.executeNonQuery(query.subQueryDeduction(month,year)); 
			while(rsdiductn.next())
			{
				DiductionModel diductionModel=new DiductionModel();
				diductionModel.setEmpKey(rsdiductn.getInt("emp_key"));
				diductionModel.setAdd_id(rsdiductn.getInt("ad_id"));
				diductionModel.setAdAmount(Double.parseDouble(new DecimalFormat("#.##").format(rsdiductn.getDouble("adAmount"))));
				diductionModel.setAhValueFlag(rsdiductn.getString("ah_value_flag"));
				diductionModel.setAhValue(Double.parseDouble(new DecimalFormat("#.##").format(rsdiductn.getDouble("ah_value"))));
				diductionModel.setDescription(rsdiductn.getString("description"));
				dedcutionData.add(diductionModel);
			}
			
		rs=db.executeNonQuery(query.retriveTheMainreportData(comapnyId,month,year,salarystatus,empKey,filterEmpKeys,supervisorId));
		while(rs.next())
		{
			SalaryMasterModel obj=new SalaryMasterModel();
			obj.setWorkingDays(rs.getInt("working_days"));	
			obj.setTotalworkHours(rs.getInt("total_workhrs"));
			obj.setBasicSalary(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("basic_salary"))));
			obj.setTotalAllowance(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("total_allowance"))));
			obj.setTotalOT(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("total_ot"))));
			obj.setTotalAddition(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("total_addition"))));
			obj.setTotalDeduction(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("total_deduction"))));
			obj.setTaxAmountMax(rs.getInt("tax_amount"));
			obj.setNetToPay(rs.getDouble("net2pay"));
			obj.setEnglishName(rs.getString("english_full"));
			obj.setArabicArabic(rs.getString("arabic_full"));
			obj.setEmpKey(rs.getInt("emp_key"));
			obj.setCompId(rs.getInt("comp_key"));
			obj.setDepartment(rs.getString("depdesc"));
			obj.setDepartmentArabic(rs.getString("depdescar"));
			obj.setPostion(rs.getString("posdesc"));
			obj.setPositionArabic(rs.getString("posdescar"));
			obj.setEmpNo(rs.getString("emp_no"));
			obj.setRecNo(rs.getInt("rec_no"));
			obj.setMonth(rs.getInt("salary_month"));
			obj.setYear(rs.getInt("salary_year"));
			obj.setTotalLoan(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("total_loan"))));
			obj.setBankId(rs.getInt("bank_id"));
			obj.setBranchId(rs.getInt("branch_id"));
			obj.setBankName(rs.getString("bankName")==null?"":rs.getString("bankName"));
			obj.setBranchName(rs.getString("branchName")==null?"":rs.getString("branchName"));
			obj.setAccountNumber(rs.getString("account_no"));
			obj.setCurrancyId(rs.getInt("currency_id"));
			String slary_status=rs.getString("salary_status");
			if(slary_status.equalsIgnoreCase("A"))
			{
				obj.setSalaryStatus("Approved");
			}
			else if(slary_status.equalsIgnoreCase("C"))
			{
				obj.setSalaryStatus("Created");
			}
			else if(slary_status.equalsIgnoreCase("P"))
			{
				obj.setSalaryStatus("Paid");
			}
			//obj.setExchnageRate(rs.getDouble("exchange_rate"));
			obj.setExchnageRate(3.14);
			obj.setPaidAmount(Double.parseDouble(new DecimalFormat("#.##").format(rs.getDouble("paid_amount"))));
			obj.setCurrancy("Dollars");
			obj.setBalance(Double.parseDouble(new DecimalFormat("#.##").format(obj.getNetToPay()-obj.getPaidAmount())));
			obj.setNetpayInDhrms(Double.parseDouble(new DecimalFormat("#.##").format(obj.getNetToPay()*obj.getExchnageRate())));
			obj.setBalanceInDhr(Double.parseDouble(new DecimalFormat("#.##").format(obj.getBalance()*obj.getExchnageRate())));
			List<AllowanceModel> allw=new ArrayList<AllowanceModel>();
			List<OverTimeModel> ovrtm=new ArrayList<OverTimeModel>();
			List<AdditionModel> addtn=new ArrayList<AdditionModel>();
			List<DiductionModel> diductn=new ArrayList<DiductionModel>();
			for(AllowanceModel model:items)
			{
				for(AllowanceModel modelColumn:itemsColumn)
				{
					if(model.getRecNowithId().equalsIgnoreCase(obj.getRecNo()+"-"+ modelColumn.getAllowanceId()))
					{
						AllowanceModel allowanceModel=new AllowanceModel();
						allowanceModel.setAmount(Double.parseDouble(new DecimalFormat("#.##").format(model.getAmount())));
						allowanceModel.setAllowanceName(model.getAllowanceName());
						allw.add(allowanceModel);
						
					}
				}
				 
			}
			obj.setAllowanceModels(allw);
			for(OverTimeModel model:overTimeColumn)
			{
				for(OverTimeModel modelColumn:overTime)
				{
					if((model.getRecNo()==obj.getRecNo() && modelColumn.getOvertimeRate()==model.getOvertimeRate()))
					{
							OverTimeModel overTimeModel=new OverTimeModel();
							overTimeModel.setOvertimeRate(model.getOvertimeRate());
							overTimeModel.setOtHours(model.getOtHours());
							overTimeModel.setAmount(Double.parseDouble(new DecimalFormat("#.##").format(model.getAmount())));
							ovrtm.add(overTimeModel);
					}
				}
				 
			}
			obj.setOverTimeModels(ovrtm);
			for(AdditionModel model:addition)
			{
					for(AdditionModel modelAdd:additionData)
					{
						if(obj.getEmpKey()==modelAdd.getEmpKey() && model.getAdd_Id()==modelAdd.getAdd_Id())
						{
							AdditionModel additionModel=new AdditionModel();
							additionModel.setAdd_Id(model.getAdd_Id());
							additionModel.setAdAmount(modelAdd.getAdAmount());
							additionModel.setAhValueFlag(modelAdd.getAhValueFlag());
							additionModel.setAhValue(modelAdd.getAhValue());
							additionModel.setDescription(modelAdd.getDescription());
							addtn.add(additionModel);
						}
					}
			}
			obj.setAdditionModels(addtn);
			for(DiductionModel model:dedcution)
			{
					for(DiductionModel modelDid:dedcutionData)
					{
						if(obj.getEmpKey()==modelDid.getEmpKey() && model.getAdd_id()==modelDid.getAdd_id())
						{
							DiductionModel diductionModel=new DiductionModel();
							diductionModel.setDescription(model.getDescription());
							diductionModel.setAdd_id(model.getAdd_id());
							diductionModel.setAdAmount(modelDid.getAdAmount());
							diductionModel.setAhValueFlag(modelDid.getAhValueFlag());
							diductionModel.setAhValue(modelDid.getAhValue());
							diductionModel.setDescription(modelDid.getDescription());
							diductn.add(diductionModel);
						}
					}
			}
			obj.setDiductionModels(diductn);
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retriveTheMainreportData-->" , ex);
		}
		return lst;
	}
	
	
	public List<AllowanceModel> retriveSalaryAmount()
	{
		List<AllowanceModel> lst=new ArrayList<AllowanceModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		ResultSet rs = null;
		try
		{
		rs=db.executeNonQuery(query.retriveSalaryAmount());
		while(rs.next())
		{
			AllowanceModel obj=new AllowanceModel();
			obj.setAllowanceId(rs.getInt("allowance_id"));		
			obj.setRecNowithId(rs.getString("recNoWithId"));
			obj.setAmount(rs.getDouble("amount"));
			obj.setAllowanceName(rs.getString("DESCRIPTION"));
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retriveSalaryAmount-->" , ex);
		}
		return lst;
	}
	
	public List<OverTimeModel> retriveOTHoursAndRate()
	{
		List<OverTimeModel> lst=new ArrayList<OverTimeModel>();
		SalarySheetDetailedQueries query=new SalarySheetDetailedQueries();
		ResultSet rs = null;
		try
		{
		rs=db.executeNonQuery(query.retriveOTHoursAndRate(0,0));
		while(rs.next())
		{
			OverTimeModel obj=new OverTimeModel();
			obj.setRecNo(rs.getInt("rec_No"));		
			obj.setMonthNumber(rs.getInt("month_no"));
			obj.setYearNumber(rs.getInt("year_no"));
			obj.setOvertimeRate(rs.getDouble("otrate"));
			obj.setOtHours(rs.getDouble("othrs"));
			obj.setAmount(rs.getDouble("amount"));
			lst.add(obj);
		}
		}
		catch (Exception ex) {
			logger.error("error in SalarySheetDetailedData---retriveOTHoursAndRate-->" , ex);
		}
		return lst;
	}
	
	
}
