package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.CompSetupModel;
import model.HRListValuesModel;
import model.ShiftModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class OverTimeViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private int UserId;
	private int supervisorID;
	
	HRData hrdata=new HRData();
	TimeSheetData tsdata=new TimeSheetData();
	ShiftCreationData data=new ShiftCreationData();

	private ListModelList<ShiftModel> lstOverTime;
	private ListModelList<ShiftModel> lstOverTimeDetails;
	
	private List<String> lstDayType;
	private List<String> lstAutoFill;
	private List<String> lstAutoCalculate;
	private List<HRListValuesModel> lstSalaryItemsHRValues;
	private List<ShiftModel> lstShiftMaster;
	
	private ShiftModel selectedOverTime;
	
	private CompSetupModel objCompanySetup;
	private boolean allowChangeOvertime;
	private boolean automaticallAdjustTime;
	
	int menuID=289;
	private MenuModel companyRole;
	
	@Init
    public void init(@BindingParam("type") ShiftModel row)
 	{		
		try
		{
		if(row!=null)
		{
 		logger.info(row.getShiftCode());
 		lstDayType=new ArrayList<String>();
 		lstDayType.add("All");
 		lstDayType.add("Normal Days");
 		lstDayType.add("Holidays");
 		
 		lstAutoFill=new ArrayList<String>();
 		lstAutoFill.add("Yes");
 		lstAutoFill.add("No");
 		
 		lstAutoCalculate=new ArrayList<String>();
 		lstAutoCalculate.add("Yes");
 		lstAutoCalculate.add("No");
 		
 		lstSalaryItemsHRValues=data.getSalaryItemsHRListValues();
 		
 		lstOverTimeDetails= new ListModelList<ShiftModel>(data.getOverTimeCalculation(row.getCompKey(),row.getRecNo())) ;
 		for (ShiftModel item : lstOverTimeDetails)
 		{
 			item.setHrSalaryItem(bindSalaryItem(item));
		}
 		
 		 		
		}
		else
		{			
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			supervisorID=dbUser.getSupervisor();
			UserId=dbUser.getUserid();
			//getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			int defaultCompanyId=0;
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=tsdata.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);
			
			lstShiftMaster=data.getShiftMasterList(selectedCompany.getCompKey(),"0");
			bindddl();
			objCompanySetup=data.getCompanySetup(selectedCompany.getCompKey());	
			allowChangeOvertime=objCompanySetup.getTimesheetOTChange().equals("Y");
			automaticallAdjustTime=objCompanySetup.getTimesheetTimeAuto().equals("Y");
			fillOverTimeData();
			
			getCompanyRolePermessions(dbUser.getCompanyroleid());
		}
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in OverTimeViewModel ----> init", ex);			
		}
 	}
	private void bindddl()
	{
		lstDayType=new ArrayList<String>();
 		lstDayType.add("All");
 		lstDayType.add("Normal Days");
 		lstDayType.add("Holidays");
 		
 		lstAutoFill=new ArrayList<String>();
 		lstAutoFill.add("Yes");
 		lstAutoFill.add("No");
 		
 		lstAutoCalculate=new ArrayList<String>();
 		lstAutoCalculate.add("Yes");
 		lstAutoCalculate.add("No");
 		
 		lstSalaryItemsHRValues=data.getSalaryItemsHRListValues();
	}
	private void getCompanyRolePermessions(int companyRoleId)
	{
		setCompanyRole(new MenuModel());
		
		List<MenuModel> lstRoles= tsdata.getTimeSheetRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				setCompanyRole(item);
				break;
			}
		}
	}
	
	public OverTimeViewModel()
	{
		try
		{
			/*
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			supervisorID=dbUser.getSupervisor();
			UserId=dbUser.getUserid();
			//getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			int defaultCompanyId=0;
			defaultCompanyId=hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies=tsdata.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) 
			{
			if(item.getCompKey()==defaultCompanyId)
				selectedCompany=item;
			}
			if(lstComapnies.size()>=1 && selectedCompany==null)		
			selectedCompany=lstComapnies.get(0);
			
			fillOverTimeData();
			*/
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in OverTimeViewModel ----> init", ex);			
		}
	}

	private HRListValuesModel bindSalaryItem(ShiftModel row)
	{
		HRListValuesModel result=null;
		for (HRListValuesModel item : lstSalaryItemsHRValues)
		{
			if(item.getListId()==row.getSalaryItem())
			{
			  result=item;
			  return result;
			}
		}
		
		return result;
	}
	private ShiftModel bindShiftType(ShiftModel row)
	{
		ShiftModel result=null;
		for (ShiftModel item : lstShiftMaster)
		{
			if(item.getShiftKey()==row.getShiftKey())
			{
			  result=item;
			  return result;
			}
		}
		
		return result;
	}
	
	private void fillOverTimeData()
	{
		lstOverTime=new ListModelList<ShiftModel>(data.getOverTimeSetup(selectedCompany.getCompKey()));
		
		for (ShiftModel item : lstOverTime)
		{
			 item.setShiftType(bindShiftType(item));
			// lstOverTimeDetails= new ListModelList<ShiftModel>(data.getOverTimeCalculation(item.getCompKey(),item.getRecNo())) ;
			 item.setLstOverTime(data.getOverTimeCalculation(item.getCompKey(),item.getRecNo()));
			 for (ShiftModel detail : item.getLstOverTime()) 
			 {
				 detail.setHrSalaryItem(bindSalaryItem(detail));
			}
		}		
	}
	
	 @Command
	 @NotifyChange({"lstOverTime"})
	public void addshiftOTCommand()
	{
		 for (ShiftModel item : lstOverTime)
		 {
			if(item.getRecNo()==0)
			{
				 Messagebox.show("Please save the added shift !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			}
		 }
		 ShiftModel obj=new ShiftModel();
		 //obj.setShiftKey(lstOverTime.get(lstOverTime.size()-1).getRecNo()+1);//use as temp		
		 obj.setCompKey(selectedCompany.getCompKey());			 
		 obj.setLstOverTime(data.getOverTimeCalculation(obj.getCompKey(),obj.getRecNo()));
		 lstOverTime.add(obj);		
	}
	
	@Command
	@NotifyChange({"lstOverTime"})
	 public void insertOTDetailRow()
	 {
		 if(selectedOverTime!=null)
		 {
			 //Messagebox.show(selectedOverTime.getOvertimeNo() + "","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			 for (ShiftModel item : lstOverTime)
			 {
				if(item.getRecNo()==selectedOverTime.getRecNo())
				{
					ShiftModel obj=new ShiftModel();
					obj.setRecNo(item.getRecNo());
					obj.setOvertimeNo(getLastOverTimeNumber(item.getLstOverTime()));
					item.getLstOverTime().add(obj);
				}
			 }
		 }
	 }
	 private int getLastOverTimeNumber(List<ShiftModel> lstOverTime)
	 {
		 int result=0;
		 if(lstOverTime!=null)
		 {
			 for (ShiftModel item : lstOverTime) 
			 {
				 if(item.getOvertimeNo()>result)
				result=item.getOvertimeNo();
			 }
		 }
		 
		 return result+1;
	 }
	 @Command
	 @NotifyChange({"lstOverTime"})
	 public void deleteOTDetailRow()
	 {
		 if(selectedOverTime!=null)
		 {
			 for (ShiftModel item : lstOverTime)				 
			 {
				 for (ShiftModel details : item.getLstOverTime()) 
				 {
					 if(details.getRecNo()==selectedOverTime.getRecNo() && details.getOvertimeNo()==selectedOverTime.getOvertimeNo())
						{
						 item.getLstOverTime().remove(details);				
						 return;
						}
				 }				 				
			 }
		 }
	 }
	 
	 @Command
	 @NotifyChange({"lstOverTime"})
	 public void saveOTCommand()
	 {		 
		 try
		 {
			 if(isValidShift()==false)
					return;
			 
			 int overtimeNo=1;
			 objCompanySetup.setTimesheetOTChange(allowChangeOvertime?"Y":"N");
			 objCompanySetup.setTimesheetTimeAuto(automaticallAdjustTime?"Y":"N");
			 data.deleteTSSetup(selectedCompany.getCompKey(),objCompanySetup);
			 for (ShiftModel item : lstOverTime)
			 {
				 //if(item.getRecNo()==0)
				 {					
				
				int recNo= data.insertTSSetup(item);
				if(item.isCalculateOT())
				 {
					logger.info("overtime detail size >>> " + item.getLstOverTime().size());
					
					for (ShiftModel detail : item.getLstOverTime())
					{
						detail.setRecNo(recNo);
						detail.setOvertimeNo(overtimeNo);
						detail.setCompKey(item.getCompKey());
						data.insertOTCalculation(detail);
						overtimeNo++;
					}
				  }
				 }				 				
			 }
			 
			 Messagebox.show("Shift is saved !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			 fillOverTimeData();
		 }
		 
		  catch (Exception ex)
		  {	
			logger.error("ERROR in OverTimeViewModel ----> saveOTCommand", ex);			
		  }
	 }
	 private boolean isValidShift()
	 {
		 boolean isValid=true;
		 for (ShiftModel item : lstOverTime)
		 {
			
				if(item.getShiftType()==null)
				{
					 Messagebox.show("You should select the Shift Type !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);						
					 return false;
				}
				if(item.isCalculateOT())
				{
					for (ShiftModel detail : item.getLstOverTime())
					{
						if(detail.getDayType()==null)
						{
							 Messagebox.show("You should select the Day Type !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);						
							 return false;
						}
						if(detail.getAutoFill()==null)
						{
							 Messagebox.show("You should select Auto Fill Type !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);						
							 return false;
						}
						if(detail.getCalculate()==null)
						{
							 Messagebox.show("You should select Calculate Type !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);						
							 return false;
						}
						if(detail.getHrSalaryItem()==null)
						{
							 Messagebox.show("You should select Salary Item !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);						
							 return false;
						}
					}
			  }
		 }							 					
		 
		 return isValid;
	 }
	 @Command
	 @NotifyChange({"lstOverTime"})
	 public void deleteShiftCommand(@BindingParam("row") final ShiftModel row)
	 {
		 try
		 {			 
	         Messagebox.show("Are you sure to delete this Shift ?","Time Sheet Setup",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
				 {
					 public void onEvent(Event e)
					 {
						 if (Messagebox.ON_YES.equals(e.getName()))
		               {									
							lstOverTime.remove(row);						
		               }
					 }
				 });		
		 }
		 catch (Exception ex)
		 {	
		   logger.error("ERROR in OverTimeViewModel ----> deleteShiftCommand", ex);			
		 }
	 }
	 @Command
	 @NotifyChange({"lstOverTime"})
	 public void selectShiftCommand(@BindingParam("row") ShiftModel row)
	 {
		 for (ShiftModel item : lstOverTime)
		 {
			 if(item.getRecNo()!=row.getRecNo())
			 {
			 if(item.getShiftType().getShiftKey()==row.getShiftType().getShiftKey())
			 {
				 Messagebox.show("Shift should not be Duplicate !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				 row.setShiftType(null);
				 return;
			 }
			 
			 }
		 }
	 }
	 @Command
	 @NotifyChange({"lstOverTime"})
	 public void changeOTHoursCommand(@BindingParam("row") ShiftModel row)
	 {
		 double tmpTOTHrs=0;
		 
		 for (ShiftModel item : lstOverTime)
		 {
			 if(item.getRecNo()==row.getRecNo())
			 {
				 int MaxOTHour=item.getMaxOT();
				 for (ShiftModel detail : item.getLstOverTime())
				 {
					 tmpTOTHrs+= detail.getOtHours();
					 
					 if(tmpTOTHrs>MaxOTHour)
					 {
						 Messagebox.show("OT Hours should be less than Max. OT Hrs !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						 detail.setOtHours(0);
						 return;
					 }
				 }				 				
			 }			 
		 }
	 }
	 
	 @Command
	 @NotifyChange({"lstOverTime"})
	 public void selectDayTypeCommand(@BindingParam("row") ShiftModel row)//not used for now
	 {
		 for (ShiftModel item : lstOverTime)
		 {
			 if(item.getRecNo()==row.getRecNo())
			 {
				 for (ShiftModel detail : item.getLstOverTime())
				 {
					 if(detail.getOvertimeNo()!=row.getOvertimeNo())
					 {
					 if(detail.getDayType().equals(row.getDayType()))
					 {
						 Messagebox.show("Day type should not duplicate !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setDayType(null);
						 return;
					 }
					 
					}
				 }			
			 }
		 }
		 
	 }
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}
	
	@NotifyChange({"lstOverTime","allowChangeOvertime","automaticallAdjustTime"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		lstShiftMaster=data.getShiftMasterList(selectedCompany.getCompKey(),"0");
		objCompanySetup=data.getCompanySetup(selectedCompany.getCompKey());	
		allowChangeOvertime=objCompanySetup.getTimesheetOTChange().equals("Y");
		automaticallAdjustTime=objCompanySetup.getTimesheetTimeAuto().equals("Y");
		fillOverTimeData();
	}


	public ListModelList<ShiftModel> getLstOverTime() {
		return lstOverTime;
	}

	public void setLstOverTime(ListModelList<ShiftModel> lstOverTime) {
		this.lstOverTime = lstOverTime;
	}
	public ListModelList<ShiftModel> getLstOverTimeDetails() {
		return lstOverTimeDetails;
	}
	public void setLstOverTimeDetails(ListModelList<ShiftModel> lstOverTimeDetails) {
		this.lstOverTimeDetails = lstOverTimeDetails;
	}
	public List<String> getLstDayType() {
		return lstDayType;
	}
	public void setLstDayType(List<String> lstDayType) {
		this.lstDayType = lstDayType;
	}
	public List<String> getLstAutoFill() {
		return lstAutoFill;
	}
	public void setLstAutoFill(List<String> lstAutoFill) {
		this.lstAutoFill = lstAutoFill;
	}
	public List<String> getLstAutoCalculate() {
		return lstAutoCalculate;
	}
	public void setLstAutoCalculate(List<String> lstAutoCalculate) {
		this.lstAutoCalculate = lstAutoCalculate;
	}
	public List<HRListValuesModel> getLstSalaryItemsHRValues() {
		return lstSalaryItemsHRValues;
	}
	public void setLstSalaryItemsHRValues(List<HRListValuesModel> lstSalaryItemsHRValues) {
		this.lstSalaryItemsHRValues = lstSalaryItemsHRValues;
	}
	public List<ShiftModel> getLstShiftMaster() {
		return lstShiftMaster;
	}
	public void setLstShiftMaster(List<ShiftModel> lstShiftMaster) {
		this.lstShiftMaster = lstShiftMaster;
	}
	public ShiftModel getSelectedOverTime() {
		return selectedOverTime;
	}
	public void setSelectedOverTime(ShiftModel selectedOverTime) {
		this.selectedOverTime = selectedOverTime;
	}
	public boolean isAllowChangeOvertime() {
		return allowChangeOvertime;
	}
	public void setAllowChangeOvertime(boolean allowChangeOvertime) {
		this.allowChangeOvertime = allowChangeOvertime;
	}
	public boolean isAutomaticallAdjustTime() {
		return automaticallAdjustTime;
	}
	public void setAutomaticallAdjustTime(boolean automaticallAdjustTime) {
		this.automaticallAdjustTime = automaticallAdjustTime;
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
}
