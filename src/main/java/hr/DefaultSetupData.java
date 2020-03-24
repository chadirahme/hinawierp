package hr;

import hr.model.CompanyModel;
import hr.model.LeaveParamsModel;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.AllowanceModel;
import model.CompanyDBModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class DefaultSetupData {

	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db;
	DefaultSetupQueries query=new DefaultSetupQueries();
	
	public DefaultSetupData() {
		try {
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb = new DBHandler();
			ResultSet rs = null;
			CompanyDBModel obj = new CompanyDBModel();
			WebusersModel dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			if (dbUser != null) {
				HRQueries query = new HRQueries();
				rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
						.getCompanyid()));
				while (rs.next()) {
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));
				}
				db = new SQLDBHandler(obj);
			}
		} catch (Exception ex) {
			logger.error("error in DefaultSetupData---Init-->", ex);
		}
	}

	public List<HRListValuesModel> getHRAllowncesListValues()
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();		 		 
			HRListValuesModel obj=new HRListValuesModel();					
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getHRAllowncesListValuesQuery());
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
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getHRAllowncesListValues-->" , ex);
			}
		 return lst;
	}
	
	public List<AllowanceModel> getCompanyAllowance(int companyKey,int allowanceID,String itemDesc)
	{
		 List<AllowanceModel> lst=new ListModelList<AllowanceModel>();
		 AllowanceModel obj;
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.getCompanyAllowanceQuery(companyKey, allowanceID));
				while(rs.next())
				{
					obj=new AllowanceModel();
					obj.setEffectiveDate(rs.getDate("EFF_DATE"));
					obj.setRecNo(rs.getInt("REC_NO"));
					obj.setLineNo(rs.getInt("LINE_NO"));
					obj.setAllowanceId(rs.getInt("ALLOWANCE_ID"));
					obj.setCompanyKey(rs.getInt("COMP_KEY"));
					obj.setAllowanceType(rs.getInt("ALLOWANCE_TYPE"));
					obj.setSalaryId(allowanceID);
					obj.setSalaryItem(itemDesc);	
					//obj.setPensionEmployee(rs.getInt("PENSION_EMP"));
					//obj.setPensionCompany(rs.getInt("PENSION_COMP"));
					//obj.setMinimum(rs.getInt("MINIMUM"));
					//obj.setMaximum(rs.getInt("MAXIMUM"));
					obj.setEditPension(obj.getPensionEmployee()>0 || obj.getPensionCompany() >0);
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getCompanyAllowance-->" , ex);
			}
		 
		 return lst;
	}
	
	public boolean geSalaryFromHRCondition(int companyKey,int allowanceID,boolean isSalary,String activity)
	{
		boolean hasSetup=false;			
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.geSalaryFromHRConditionQuery(companyKey, allowanceID,isSalary,activity));
				while(rs.next())
				{
					return true;
				}							
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---geSalaryFromHRCondition-->" , ex);
			}
		 
		 return hasSetup;
	}
	
	public int checkAllowncesAdvanced(int companyKey,int salaryId)
	{
		 int size= 0;		
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.checkAllowncesAdvancedQuery(companyKey, salaryId));
				if (rs != null)   
				{  
				  rs.beforeFirst();  
				  rs.last();  
				  size = rs.getRow();  				 
				}  
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---checkAllowncesAdvanced-->" , ex);
			}
		 
		 return size;
	}
	public AllowanceModel getAllowncesAdvanced(int companyKey,int salaryId)
	{
		 int size= 0;		
		 AllowanceModel obj=new AllowanceModel();
		 ResultSet rs = null;
		 boolean tmpLEVEL=false,tmpDEP=false,tmpPOS=false,tmpSECTION=false,tmpCLASS=false,tmpEMPTYPE=false,tmpSEX=false,tmpMARITAL=false,
				 tmpRELIGION=false,tmpNATIONALITY=false,tmpAmountChanged=false;
		 int tmpPensionEmp=0,tmpPensionComp=0,tmpMinAmount=0,tmpMaxAmount=0;
	   
		 try 
			{			 	
				rs=db.executeNonQuery(query.getAllowncesAdvancedQuery(companyKey, salaryId));
				if (rs != null)   
				{  
				  rs.beforeFirst();  
				  rs.last();  
				  size = rs.getRow();  				
				}
				 rs.beforeFirst();  
				 if(size==1)
				 {
					 while(rs.next())
					 {
						 tmpLEVEL= rs.getInt("LEVEL_ID")!=-1;
						 tmpDEP= rs.getInt("DEP_ID")!=-1;
						 tmpPOS= rs.getInt("POS_ID")!=-1;
						 tmpSECTION= rs.getInt("SECTION_ID")!=-1;
						 tmpCLASS= rs.getInt("CLASS_ID")!=-1;
						 tmpEMPTYPE= rs.getString("EMP_TYPE").equals("A")?false:true;
						 tmpSEX= rs.getInt("SEX_ID")!=-1;
						 tmpMARITAL= rs.getInt("MARITAL_ID")!=-1;
						 tmpRELIGION= rs.getInt("RELIGION_ID")!=-1;
						 tmpNATIONALITY= rs.getString("NATIONALITY").equals("A")?false:true;	
						 tmpPensionComp=0;
						 tmpPensionEmp = 0;
						 tmpMinAmount=rs.getInt("Minimum");
						 tmpMaxAmount=rs.getInt("Maximum");
					 }
					 
					 if(tmpLEVEL==false && tmpDEP==false && tmpPOS==false && tmpSECTION==false &&  tmpCLASS==false && tmpEMPTYPE==false && tmpSEX==false &&tmpMARITAL==false 
							 && tmpRELIGION==false &&tmpNATIONALITY==false)
					 {
						 obj.setEditPension(false);
						 obj.setMinimum(tmpMinAmount);
						 obj.setMaximum(tmpMaxAmount);
					 }
					 else
					 {
						 obj.setChangeFromAdvanced(true);
					 }
				 }
				 else if(size==2)
				 {
					 int tmpCounter=0;
					 while(rs.next())
					 {
						 tmpCounter++;
						 tmpLEVEL= rs.getInt("LEVEL_ID")!=-1;
						 tmpDEP= rs.getInt("DEP_ID")!=-1;
						 tmpPOS= rs.getInt("POS_ID")!=-1;
						 tmpSECTION= rs.getInt("SECTION_ID")!=-1;
						 tmpCLASS= rs.getInt("CLASS_ID")!=-1;
						 tmpEMPTYPE= rs.getString("EMP_TYPE").equals("A")?false:true;
						 tmpSEX= rs.getInt("SEX_ID")!=-1;
						 tmpMARITAL= rs.getInt("MARITAL_ID")!=-1;
						 tmpRELIGION= rs.getInt("RELIGION_ID")!=-1;
						 String tmpNat=rs.getString("NATIONALITY");
						 if(!tmpNat.equals("L") && !tmpNat.equals("N"))
						 {
							 tmpNATIONALITY=true;
						 }
						if(tmpNat.equals("L"))
						{
							tmpPensionEmp=rs.getInt("PENSION_EMP");
							tmpPensionComp=rs.getInt("PENSION_COMP");
						}
						if(tmpCounter==2)
						{
							if(tmpMinAmount!=rs.getInt("Minimum"))
								tmpAmountChanged=true;
							if(tmpMaxAmount!=rs.getInt("Maximum"))
								tmpAmountChanged=true;
						}
						 tmpMinAmount=rs.getInt("Minimum");
						 tmpMaxAmount=rs.getInt("Maximum");
						
					 }
					 
					 if(tmpLEVEL==false && tmpDEP==false && tmpPOS==false && tmpSECTION==false &&  tmpCLASS==false && tmpEMPTYPE==false && tmpSEX==false &&tmpMARITAL==false 
							 && tmpRELIGION==false &&tmpNATIONALITY==false && tmpAmountChanged==false)
					 {
						 obj.setEditPension(true);
						 obj.setMinimum(tmpMinAmount);
						 obj.setMaximum(tmpMaxAmount);
						 obj.setPensionEmployee(tmpPensionEmp);
						 obj.setPensionCompany(tmpPensionComp);
						 
					 }
					 else
					 {
						 obj.setChangeFromAdvanced(true);
					 }
				 }
				 else
				 {
					 obj.setChangeFromAdvanced(true);
				 }
				
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getAllowncesAdvanced-->" , ex);
			}
		 
		 return obj;
	}
	
	public int checkLeavesAdvanced(int companyKey,int leaveID)
	{
		 int size= 0;		
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.checkLeavesAdvancedQuery(companyKey, leaveID));
				if (rs != null)   
				{  
				  rs.beforeFirst();  
				  rs.last();  
				  size = rs.getRow();  				 
				}  
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---checkLeavesAdvanced-->" , ex);
			}
		 
		 return size;
	}
	
	public AllowanceModel getLeavesAdvanced(int companyKey,int leaveID)
	{
		 int size= 0;		
		 AllowanceModel obj=new AllowanceModel();
		 ResultSet rs = null;
		 boolean tmpLEVEL=false,tmpDEP=false,tmpPOS=false,tmpSECTION=false,tmpCLASS=false,tmpEMPTYPE=false,tmpSEX=false,tmpMARITAL=false,
				 tmpRELIGION=false,tmpNATIONALITY=false,tmpAmountChanged=false,tmpMinWorkFlag=false,tmpMinWorkPeriod=false,tmpTransferNextYear=false,
						 tmpSalaryItem=false,tmpEncashItem=false;
		 int tmpPensionEmp=0,tmpPensionComp=0,tmpMinAmount=0,tmpMaxAmount=0,tmpLRecNo=0;
		try
		{
			rs=db.executeNonQuery(query.getLeavesAdvancedQuery(companyKey, leaveID));
			if (rs != null)   
			{  
			  rs.beforeFirst();  
			  rs.last();  
			  size = rs.getRow();  				
			}
			 rs.beforeFirst();  
			 if(size==1)
			 {
				 while(rs.next())
				 {
					 tmpLEVEL= rs.getInt("LEVEL_ID")!=-1;
					 tmpDEP= rs.getInt("DEP_ID")!=-1;
					 tmpPOS= rs.getInt("POS_ID")!=-1;
					 tmpSECTION= rs.getInt("SECTION_ID")!=-1;
					 tmpCLASS= rs.getInt("CLASS_ID")!=-1;
					 tmpEMPTYPE= rs.getString("EMP_TYPE").equals("A")?false:true;
					 tmpSEX= rs.getInt("SEX_ID")!=-1;
					 tmpMARITAL= rs.getInt("MARITAL_ID")!=-1;
					 tmpRELIGION= rs.getInt("RELIGION_ID")!=-1;
					 tmpNATIONALITY= rs.getString("NATIONALITY").equals("A")?false:true;	
					 tmpMinWorkFlag= rs.getString("MIN_WORK_FLAG").equals("M")?false:true;	
					 tmpMinWorkPeriod=rs.getDouble("MIN_WORK_PERIOD")!=6;
					 if(leaveID==100)
					 {
						 tmpTransferNextYear=rs.getString("TRANS_NEXT_YEAR").equals("Y")?false:true;
					 }
					 else
					 {
						 tmpTransferNextYear=rs.getString("TRANS_NEXT_YEAR").equals("N")?false:true;
					 }
					 tmpSalaryItem=rs.getInt("SALARY_ITEM")!=-1;
					 tmpEncashItem=rs.getInt("SALARY_ITEM")!=-1;
					 tmpLRecNo=rs.getInt("Rec_No");
					 
					 if(tmpLEVEL==false && tmpDEP==false && tmpPOS==false && tmpSECTION==false &&  tmpCLASS==false && tmpEMPTYPE==false && tmpSEX==false &&tmpMARITAL==false 
							 && tmpRELIGION==false &&tmpNATIONALITY==false && tmpMinWorkFlag==false && tmpTransferNextYear==false 
							 && tmpSalaryItem==false && tmpEncashItem==false && tmpMinWorkPeriod==false)
					 {
						 obj.setDaysAllowed(getLeavesCalcualtionByRecNo(tmpLRecNo));
					 }
					 else
					 {
						 obj.setChangeFromAdvanced(true);
					 }
				 }
			 }
			 else
			 {
				 obj.setChangeFromAdvanced(true);
			 }
			 
		}
		catch (Exception ex) {
			logger.error("error in DefaultSetupData---getLeavesAdvanced-->" , ex);
		}
		return obj;
	}
	
	private int getLeavesCalcualtionByRecNo(int recNo)
	{
		int days=0;		
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.getLeavesCalcualtionByRecNoQuery(recNo));
				while(rs.next())
				{
					if(rs.getString("DURATION_MODE").equals("Y"))
					{
						//days=rs.getDouble("DAYS_ALLOWED");
					}
					else
					{
						//days=rs.getDouble("DAYS_ALLOWED")*12;
					}
				}							
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getLeavesCalcualtionByRecNo-->" , ex);
			}
		 return days;
		 
	}
	public boolean getHRAdditionSetup(int companyKey)
	{
		boolean hasSetup=false;			
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.getHRAdditionSetupQuery(companyKey));
				while(rs.next())
				{
					return true;
				}							
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getHRAdditionSetup-->" , ex);
			}
		 
		 return hasSetup;
	}
	
	public List<HRListValuesModel> getHRLeaveListValues()
	{
		 	List<HRListValuesModel> lst=new ArrayList<HRListValuesModel>();		 		 
			HRListValuesModel obj=new HRListValuesModel();					
			ResultSet rs = null;
			try 
			{
				rs=db.executeNonQuery(query.getHRLeaveListValuesQuery());
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
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getHRLeaveListValues-->" , ex);
			}
		 return lst;
	}
	
	public int getMaxID(String tableName,String fieldName)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getMaxIDQuery(tableName, fieldName));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---getMaxID-->" , ex);
		}	
		return result;
	}
	
	public int generateRecNo(String tableName,String fieldName,String activity,int salaryItem)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.generateRecNoQuery(tableName, fieldName,activity,salaryItem));
			while(rs.next())
			{
				result=rs.getInt(1);
			}						
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---generateRecNo-->" , ex);
		}	
		return result;
	}
	public int generateLineNo(String tableName,String fieldName,int recNo)
	{
		int result=0;			
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.generateLineNoQuery(tableName, fieldName,recNo));
			while(rs.next())
			{
				result=rs.getInt(1)+1;
			}
			if(result==0)
				result=1;
			
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---generateLineNo-->" , ex);
		}	
		return result;
	}
	
	
	public List<AllowanceModel> getHRColumns(String activity)
	{
		 List<AllowanceModel> lst=new ListModelList<AllowanceModel>();
		 AllowanceModel obj;
		 ResultSet rs = null;
		 try 
			{
				rs=db.executeNonQuery(query.getHRColumnsQuery(activity));
				while(rs.next())
				{
					obj=new AllowanceModel();
					obj.setColumnId(rs.getInt("Column_ID"));
					obj.setAllowMultiple(rs.getString("ALLOW_MULTIPLE"));					
					lst.add(obj);
				}
			}
			catch (Exception ex) {
				logger.error("error in DefaultSetupData---getHRColumns-->" , ex);
			}
		 
		 return lst;
	}
	
	public int addHRConditions(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addHRConditionsQuery(obj));
			//logger.info(" addHRConditions Rows >>>>>>> " + result);			
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addHRConditions-->" , ex);
		}	
		return result;
	}
	
	public int deleteHRSetup(int recNo)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.deleteHRSetupQuery(recNo));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---deleteHRSetup-->" , ex);
		}	
		return result;
	}
	
	public int addHRSetup(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addHRSetupQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addHRSetup-->" , ex);
		}	
		return result;
	}
	
	public int addCompanySalary(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addCompanySalaryQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addCompanySalary-->" , ex);
		}	
		return result;
	}
	
	public int deleteHRSetupConditionsQuery(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.deleteHRSetupConditionsQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---deleteHRSetupConditions-->" , ex);
		}	
		return result;
	}
	public int addHRSetupConditions(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addHRSetupConditionsQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addHRSetupConditions-->" , ex);
		}	
		return result;
	}
	
	public int addCompanyAllowances(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addCompanyAllowancesQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addCompanyAllowances-->" , ex);
		}	
		return result;
	}
	
	public int addLeaveSetup(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addLeaveSetupQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addLeaveSetup-->" , ex);
		}	
		return result;
	}
	public int addLeaveCalculation(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addLeaveCalculationQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addLeaveCalculation-->" , ex);
		}	
		return result;
	}
	
	public int addAdditionSetup(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addAdditionSetupQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addAdditionSetup-->" , ex);
		}	
		return result;
	}
	
	public int addAbsenceSetup(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addAbsenceSetupQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addAbsenceSetup-->" , ex);
		}	
		return result;
	}
	
	public int addAbsenceCalculation(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addAbsenceCalculationQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addAbsenceCalculation-->" , ex);
		}	
		return result;
	}
	
	public int addEOSSetup(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addEOSSetupQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addEOSSetup-->" , ex);
		}	
		return result;
	}
	
	public int addEOSCalculation(AllowanceModel obj)
	{
		int result=0;				
		try 
		{				
			result=db.executeUpdateQuery(query.addEOSCalculationQuery(obj));					
		}
		catch (Exception ex) 
		{
			logger.error("error in DefaultSetupData---addEOSCalculation-->" , ex);
		}	
		return result;
	}
	
	public LeaveParamsModel GetDefLeavePARAMS()
	{	
		ResultSet rs = null;	
		LeaveParamsModel defLeavePARMS=new LeaveParamsModel();
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
					else if(rs.getString("FIELD_NAME").equals("MIN_WORK_PERIOD"))
					{
						defLeavePARMS.setMinWorkPeriod(rs.getDouble("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MIN_WORK_FLAG"))
					{
						defLeavePARMS.setMinWorkMode(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("TRANS_NEXT_YEAR"))
					{
						defLeavePARMS.setTransfer2NextYear(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MONTH_FROM"))
					{
						defLeavePARMS.setMonthFrom(rs.getDouble("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MONTH_TO"))
					{
						defLeavePARMS.setMonthTo(rs.getDouble("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DAYS_ALLOWED"))
					{
						defLeavePARMS.setDaysAllowed(rs.getDouble("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DURATION_PERIOD"))
					{
						defLeavePARMS.setDuration(rs.getDouble("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DURATION_MODE"))
					{
						defLeavePARMS.setMode(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("CALCULATE"))
					{
						defLeavePARMS.setCalculate(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("SALARY_PER"))
					{
						defLeavePARMS.setSalaryPER(rs.getDouble("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("SALARY_ITEM"))
					{
						defLeavePARMS.setSalaryItem(rs.getInt("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("ENCASHITEM"))
					{
						defLeavePARMS.setEncashItem(rs.getInt("DEF_VALUE"));
					}
										
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in DefaultSetupData---GetDefLeavePARAMS-->" , ex);
			}
			return defLeavePARMS;
	}
	
	public LeaveParamsModel GetDefAllowancePARAMS()
	{	
		ResultSet rs = null;	
		LeaveParamsModel defLeavePARMS=new LeaveParamsModel();
			try 
			{			
				String sqlqQuery="Select * from HRCOLUMNS Where HRCOLUMNS.ACTIVITY = 'ALLOWANCE' Order by COLUMN_ID,REC_NO";				
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
					else if(rs.getString("FIELD_NAME").equals("KIDS_NOS"))
					{
						defLeavePARMS.setKidsNo(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("KIDS_AGE_FROM"))
					{
						defLeavePARMS.setKidsAgeFrom(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("KIDS_AGE_TO"))
					{
						defLeavePARMS.setKidsAgeTo(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("ALLOWANCE_TYPE"))
					{
						defLeavePARMS.setAllowanceType(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("HOUSE_TYPE"))
					{
						defLeavePARMS.setHouseType(rs.getString("DEF_VALUE")==null?"":rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("ROOM_NOS"))
					{
						defLeavePARMS.setRoomsNo(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("SALARY_NOS"))
					{
						defLeavePARMS.setSalaryNos(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("FIXED"))
					{
						defLeavePARMS.setFixed(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MINIMUM"))
					{
						defLeavePARMS.setMinimum(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MAXIMUM"))
					{
						defLeavePARMS.setMaximum(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("BASIC_PER"))
					{
						defLeavePARMS.setBasicPer(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("PAY_PERIOD"))
					{
						defLeavePARMS.setPayPeriod(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("PAY_MODE"))
					{
						defLeavePARMS.setPayMode(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DISTANSE"))
					{
						defLeavePARMS.setDistance(rs.getString("DEF_VALUE"));
					}
					
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in DefaultSetupData---GetDefAllowancePARAMS-->" , ex);
			}
			return defLeavePARMS;
	}
	
	public LeaveParamsModel GetDefAbsencePARAMS()
	{	
		ResultSet rs = null;	
		LeaveParamsModel defLeavePARMS=new LeaveParamsModel();
			try 
			{			
				String sqlqQuery="Select * from HRCOLUMNS Where HRCOLUMNS.ACTIVITY = 'ABSENCE' Order by COLUMN_ID,REC_NO";				
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
					else if(rs.getString("FIELD_NAME").equals("EXCUSE"))
					{
						defLeavePARMS.setExecuse(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MAX_ALLOWED"))
					{
						defLeavePARMS.setMaxAllowed(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("ABSENCE_TYPE"))
					{
						defLeavePARMS.setAbsenceType(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MH_FLAG"))
					{
						defLeavePARMS.setCalculateIn(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DEDUCTION_RATE"))
					{
						defLeavePARMS.setDblRate(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("DEDUCTION_NOS"))
					{
						defLeavePARMS.setNos(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("DEDUCT_SERVICE"))
					{
						defLeavePARMS.setDeductService(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("CALCULATE"))
					{
						defLeavePARMS.setCalculate(rs.getString("DEF_VALUE"));
					}
					
					else if(rs.getString("FIELD_NAME").equals("DEDUCT_FROM"))
					{
						defLeavePARMS.setDeductFrom(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DEDUCT_ID"))
					{
						defLeavePARMS.setDeductionItem(Integer.parseInt(rs.getString("DEF_VALUE")));
					}
															
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in DefaultSetupData---GetDefAbsencePARAMS-->" , ex);
			}
			return defLeavePARMS;
	}
	
	public LeaveParamsModel GetDefEOSPARAMS()
	{	
		ResultSet rs = null;	
		LeaveParamsModel defLeavePARMS=new LeaveParamsModel();
			try 
			{			
				String sqlqQuery="Select * from HRCOLUMNS Where HRCOLUMNS.ACTIVITY = 'EOS' Order by COLUMN_ID,REC_NO";				
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
					else if(rs.getString("FIELD_NAME").equals("ADDITIONS"))
					{
						defLeavePARMS.setAdditions(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("DEDUCTONS"))
					{
						defLeavePARMS.setDeductions(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("EOS_REASON"))
					{
						defLeavePARMS.setEosReason(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("CONTRACT_TYPE"))
					{
						defLeavePARMS.setContractType(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("MAXIMUM_VALUE"))
					{
						defLeavePARMS.setMaximumValue(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("MAXIMUM_TYPE"))
					{
						defLeavePARMS.setMaximumType(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("SALARY_ITEM"))
					{
						defLeavePARMS.setSalaryItem(Integer.parseInt(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("SALARY_EOS"))
					{
						defLeavePARMS.setSalaryEOS(rs.getString("DEF_VALUE"));
					}
					else if(rs.getString("FIELD_NAME").equals("PERIOD_FROM"))
					{
						defLeavePARMS.setPeriodFrom(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("PERIOD_TO"))
					{
						defLeavePARMS.setPeriodTo(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("CALC_DAYS"))
					{
						defLeavePARMS.setSalaryDays(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("RATE"))
					{
						defLeavePARMS.setDblRate(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("BASE_DAYS"))
					{
						defLeavePARMS.setEosBases(Double.parseDouble(rs.getString("DEF_VALUE")));
					}
					else if(rs.getString("FIELD_NAME").equals("ISDEPENDABLE"))
					{
						defLeavePARMS.setEosIsDependable(rs.getString("DEF_VALUE"));
					}														
															
				}
			}
			catch (Exception ex) 
			{
				logger.error("error in DefaultSetupData---GetDefEOSPARAMS-->" , ex);
			}
			return defLeavePARMS;
	}
}
