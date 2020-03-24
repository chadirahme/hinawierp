package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.CompSetupModel;
import model.CompanyShiftModel;
import model.ShiftModel;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class ShiftCreationViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private int UserId;
	private int supervisorID;
	
	HRData hrdata=new HRData();
	TimeSheetData tsdata=new TimeSheetData();
	ShiftCreationData data=new ShiftCreationData();
	private ListModelList<ShiftModel> lstShift;
	private List<ShiftModel> lstShiftType;
	private ShiftModel selectedShiftType;
	boolean changeShiftType;
	DateFormat hdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
	
	private CompSetupModel objCompanySetup;
	private List<String> lstDayOfWeek;
	private String selectedDayOfWeek;
	private ListModelList<ShiftModel> lstSetShift;
	private List<CompanyShiftModel> lstCompanyShiftSetup;
	private int defaultHours;
	
	private List<ShiftModel> lstHolidays;
	private List<String> columnList;
	private int selectedTab;
	
	int menuID=288;
	private MenuModel companyRole;
	
	public ShiftCreationViewModel()
	{
		try
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
			
			lstShiftType=data.getShiftTypeList();
			lstShift=new ListModelList<ShiftModel>( data.getShiftList(selectedCompany.getCompKey()));
			for (ShiftModel item : lstShift)
			{
				item.setShiftType(bindShiftType(item));
			}
			
			objCompanySetup=data.getCompanySetup(selectedCompany.getCompKey());	
			defaultHours=objCompanySetup.getDefHours();
			lstCompanyShiftSetup=data.getCompanyShiftSetupList(selectedCompany.getCompKey());
			fillDayOfWeek();
			fillSetShifts();
			
			fillHolidays();
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ShiftCreationViewModel ----> init", ex);			
		}
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
	
	private void fillDayOfWeek()
	{
		lstDayOfWeek=new ArrayList<String>();
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        String[] weekdays = dateFormatSymbols.getWeekdays();
 
        for (int i = 1; i < weekdays.length; i++)
        {
            String day = weekdays[i];
            lstDayOfWeek.add(day);
            if(objCompanySetup.getWeekStart()==i)
            	selectedDayOfWeek=day;
        }        	
	}
	
	private ShiftModel bindShiftType(ShiftModel row)
	{
		ShiftModel result=null;
		for (ShiftModel item : lstShiftType)
		{
			if(item.getUnitKey()==row.getUnitKey())
			{
			  result=item;
			  return result;
			}
		}
		
		return result;
	}
	
	 @Command
	 @NotifyChange({"lstShift","lstSetShift"})
	public void addshiftCommand()
	{
		
		 for (ShiftModel item : lstShift)
		 {
			if(item.getShiftKey()==0)
			{
				 Messagebox.show("Please save the added shift !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				 return;
			}
		  }
		 		 
		 ShiftModel obj=new ShiftModel();
		 if(lstShift.size()>0)
		 obj.setRecNo(lstShift.get(lstShift.size()-1).getRecNo()+1);
		 else
		 obj.setRecNo(1);	 
		 obj.setShiftKey(0);
		 obj.setCompKey(selectedCompany.getCompKey());
		 obj.setShiftCode("");
		 obj.setShiftType(lstShiftType.get(1));
		 obj.setUnitKey(obj.getShiftType().getUnitKey());
		 obj.setNoOfShifts(1);
		 obj.setUnits(1);
		 obj.setTimingShift(false);
		 lstShift.add(obj);		
		 
		 fillSetShifts();
	}
	 
	 @Command
	 @NotifyChange({"lstShift"})
	 public void changeNoofShift(@BindingParam("row") ShiftModel row)
	 {		
		// if(changeShiftType==true)
		// return;
		 
		// logger.info("changeNoofShift");
		 int shiftDetRecNo=0;
		 List<ShiftModel> lstOldShifts=new ArrayList<ShiftModel>();
		 if(row.getShiftType().isTimingShift())
		 {
			 for (ShiftModel item : lstShift) 
				{
					if(item.getShiftKey()==row.getShiftKey())
					{
						lstOldShifts.add(item);						
					}
				}
		 }
		 
		 lstShift.removeAll(lstOldShifts);
		 Date lastShiftTime=null;
		 for (int i = 0; i < row.getNoOfShifts(); i++)
		 {	
			if(lstOldShifts.size()>i)
			{
				ShiftModel oldShift=lstOldShifts.get(i);
				lstShift.add(oldShift);
				if(oldShift.getShiftDetRecNo()>shiftDetRecNo)
				shiftDetRecNo=oldShift.getShiftDetRecNo();
				if(oldShift.isTimingShift())
				{
					lastShiftTime=oldShift.getToTime();
				}
			}
			else
			{
				 ShiftModel obj=new ShiftModel();
				 obj.setRecNo(row.getRecNo());
				 obj.setShiftKey(row.getShiftKey());
				 obj.setShiftCode(row.getShiftCode());
				 obj.setCompKey(selectedCompany.getCompKey());
				 obj.setShiftType(lstShiftType.get(0));
				 obj.setUnitKey(obj.getShiftType().getUnitKey());
				 obj.setNoOfShifts(1);
				 obj.setUnits(1);
				 obj.setTimingShift(true);
				 obj.setShiftDetRecNo(shiftDetRecNo+1);
				 if(lastShiftTime!=null)
				 {
				 obj.setFromTime(lastShiftTime);
				 Calendar cFrom = Calendar.getInstance();
				 cFrom.setTime(lastShiftTime);
				 cFrom.set(1900,1, 1, cFrom.get(Calendar.HOUR),0,0);
				 cFrom.add(Calendar.HOUR, 1);
				 obj.setToTime(convertDateToTime(cFrom.getTime()));
				 }
				 lstShift.add(obj);	
			}
		 }
	 }
	 
	 @Command
	 @NotifyChange({"lstShift","lstShiftType"})
	 public void changeShiftType(@BindingParam("row") ShiftModel row)
	 {
		 changeShiftType=true;
		 List<ShiftModel> lstOldShifts=new ArrayList<ShiftModel>();		
		 for (ShiftModel item : lstShift) 
			{
				if(item.getShiftKey()==row.getShiftKey())
				{
					lstOldShifts.add(item);						
				}
			}		 		
		 lstShift.removeAll(lstOldShifts);
		 
		 ShiftModel obj=new ShiftModel();
		 obj.setRecNo(row.getRecNo());
		 obj.setShiftKey(row.getShiftKey());
		 obj.setShiftCode(row.getShiftCode());
		 obj.setCompKey(selectedCompany.getCompKey());
		 
		// logger.info("unit name >>>"  + row.getShiftType().getUnitName());
		 if(row.getShiftType().isTimingShift())
		 {
			 obj.setTimingShift(true);
			 obj.setFirstRecord(true);
			// obj.setUnitKey(row.getShiftType().getUnitKey());
			// obj.setShiftType(bindShiftType(row));
			 obj.setUnits(8);
			 obj.setNoOfShifts(1);
			 
			 if(obj.getFromTime()==null)
			 {
				 Calendar cFrom = Calendar.getInstance();
				 cFrom.set(1900,1, 1, 8,0,0);	
				 //obj.setFromTime(cFrom.getTime());
				 obj.setFromTime(convertDateToTime(cFrom.getTime()));
				 cFrom.add(Calendar.HOUR, 8);
				 //obj.setToTime(cFrom.getTime());		
				 obj.setToTime(convertDateToTime(cFrom.getTime()));
			 }			 
		 }
		 else			 
		 {
			 obj.setTimingShift(false);
			 obj.setFirstRecord(false);				
			// obj.setUnitKey(row.getShiftType().getUnitKey());
			// obj.setShiftType(bindShiftType(row));	
			 obj.setUnits(1);
			 obj.setNoOfShifts(1);	
		 }
		 obj.setUnitKey(row.getShiftType().getUnitKey());
		 obj.setShiftType(bindShiftType(obj));
		 lstShift.add(obj);
		 changeShiftType=false;
	 }
	 @Command
	 @NotifyChange({"lstShift"})
	 public void changeFromTime(@BindingParam("row") ShiftModel row,@BindingParam("type") int type)
	 {
		 try
		 {
			// if(changeShiftType==true)
			//	 return;
			 //logger.info("changeFromTime");
			 
		if(row.isTimingShift())
		{			
			//DateFormat hdf = new SimpleDateFormat("hh:mm a");
			//SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss");
			if(row.getFromTime()==null)
			 {
				 Calendar cFrom = Calendar.getInstance();
				 cFrom.set(1900,1, 1, 8,0,0);	
				 //row.setFromTime(cFrom.getTime());
				 row.setFromTime(convertDateToTime(cFrom.getTime()));
			 }	
			
			Calendar cFrom = Calendar.getInstance();
			//Calendar cTo = Calendar.getInstance();
			cFrom.setTime(hdf.parse(hdf.format(row.getFromTime())));
			//cTo.setTime(hdf.parse(hdf.format(row.getToTime())));			
			
			
			
			if(type==1)//Units
			{
				int totalUnits=0;
				if(row.getUnits()<=0)
				{
					 Messagebox.show("Only positive Units is allowed !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 row.setUnits(8);				
				}
				for (ShiftModel item : lstShift) 
				{
					if(item.getShiftKey()==row.getShiftKey())
					totalUnits+=item.getUnits();
					if(totalUnits>24)
					{
						 Messagebox.show("Total Hours should not exceed 24 Hrs !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setUnits(1);
					}
				}
				if(row.getUnits()>0)
				{
					if(CheckDuplicateTime(row))
					{
						 Messagebox.show("Time should not be Duplicate !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						 row.setToTime(null);
						 return;
					}
					double tmpMinutes=row.getUnits()*60;
					cFrom.add(Calendar.MINUTE, (int) tmpMinutes);
					//row.setToTime(cFrom.getTime());
					row.setToTime(convertDateToTime(cFrom.getTime()));
				}
			}
			else if(type==2)//fromTime
			{		
				if(CheckDuplicateTime(row))
				{
					 Messagebox.show("Time should not be Duplicate !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 row.setToTime(null);
					 return;
				}
				double tmpMinutes=row.getUnits()*60;
				cFrom.add(Calendar.MINUTE, (int) tmpMinutes);
				//row.setToTime(cFrom.getTime());
				row.setToTime(convertDateToTime(cFrom.getTime()));
			}
			//int diffHours=cTo.get(Calendar.HOUR) -  cFrom.get(Calendar.HOUR);
			//long timeDifInMilliSec=cTo.getTimeInMillis() - cFrom.getTimeInMillis();
			//long timeDifHours = timeDifInMilliSec / (60 * 60 * 1000);
			//long timeDifMinutes = timeDifInMilliSec / (60 * 1000);
			
			//long diffMinutes = (long) (row.getUnits()*60 / (60 * 1000) % 60);
			
			//logger.info("diffHours >> " + timeDifHours + " >> timeDifMinutes >> " + timeDifMinutes + " >>diffMinutes >" + diffMinutes);
			//row.setUnits(timeDifHours);
			fillSetShifts();
		}
		 }
		 catch (Exception ex)
			{	
				logger.error("ERROR in ShiftCreationViewModel ----> changeFromTime", ex);			
			}
	 }
	 private Date convertDateToTime(Date initDate)
	 {		 
		
		 Date result=null;
		 try
		 {
			 if(initDate==null)
				 return result;
		 //Calendar cInit = Calendar.getInstance();
		 //cInit.setTime(hdf.parse(hdf.format(initDate))); //(1900,1, 1, 8,0,0);	
		 //logger.info(hdf.format(cInit.getTime()));
		 Calendar cInit =  Calendar.getInstance();;
		 cInit.setTime(initDate);
		 Calendar cResult= Calendar.getInstance();
		 cResult.set(1900, 0,1,cInit.get(Calendar.HOUR_OF_DAY),cInit.get(Calendar.MINUTE),0);
		 result=cResult.getTime();
		 //logger.info("result>> " +  hdf.format(result));
		 }
		 catch (Exception ex)
			{	
				logger.error("ERROR in ShiftCreationViewModel ----> convertDateToTime", ex);			
			}
		 return result;
	 }
	 private boolean CheckDuplicateName(ShiftModel row)
	 {
		 boolean isDublicate=false;
		 for (ShiftModel item : lstShift) 
		{
		   if(item.getShiftKey()!=row.getShiftKey())
			{
			   if(item.getShiftCode().equals(row.getShiftCode()))
			   {
				   isDublicate=true;
			   }
			}
		}
		 
		 return isDublicate;
	 }
	 private boolean CheckDuplicateTime(ShiftModel row)
	 {
		 boolean isDublicate=false;
		 try
		 {
		
		 for (ShiftModel item : lstShift) 
		 {
		  if(item.isTimingShift() && item.getShiftKey()==row.getShiftKey())
		   {			  
			//logger.info("row" + row.getShiftDetRecNo());
			if(item.getShiftDetRecNo()!=row.getShiftDetRecNo())
			{
			
			row.setFromTime(convertDateToTime(row.getFromTime()));
			row.setToTime(convertDateToTime(row.getToTime()));
			
			item.setFromTime(convertDateToTime(item.getFromTime()));
			item.setToTime(convertDateToTime(item.getToTime()));
			
			 Calendar cItemFrom= Calendar.getInstance();
			 Calendar cItemTo= Calendar.getInstance();
			 
			 if(item.getFromTime()==null)
				 return false;
			 
			 
			 cItemFrom.setTime(convertDateToTime(item.getFromTime()));
			 cItemTo.setTime(convertDateToTime(item.getToTime()));
			 
			 int hItemFrom=cItemFrom.get(Calendar.HOUR_OF_DAY);
			 int mItemFrom=cItemFrom.get(Calendar.MINUTE);
			 
			 Calendar cRowFrom= Calendar.getInstance();
			 cRowFrom.setTime(convertDateToTime(row.getFromTime()));
			 int hRowFrom=cRowFrom.get(Calendar.HOUR_OF_DAY);
			 int mRowFrom=cRowFrom.get(Calendar.MINUTE);

			// logger.info("hRowFrom >> "  +  hRowFrom);
			// logger.info("hItemFrom >> "  +  hItemFrom);
			 
			// logger.info("cItemTo >> "  +  cItemTo.get(Calendar.HOUR_OF_DAY));
			
			 
			 if(hRowFrom > hItemFrom && hRowFrom<cItemTo.get(Calendar.HOUR_OF_DAY))    // .equals(item.getFromTime()) || row.getFromTime().equals(item.getToTime()))
			 {
			 isDublicate=true;
			 }
		 
		 if(row.getFromTime().after(item.getFromTime()) && row.getFromTime().before(item.getToTime()))
		 {
			// isDublicate=true;
		 }
		}
			
	      }
		}
	   }
		 catch (Exception ex)
		   {	
			   logger.error("ERROR in ShiftCreationViewModel ----> CheckDuplicateTime", ex);			
			}
		 return isDublicate;
	 }
	 @Command
	 @NotifyChange({"lstShift","lstSetShift"})
	 public void saveShiftCommand()
	 {
		 try
		 {			
			ShiftModel tmpShift;
			int shiftKey=0;
			int tmpRecNo=0;
			if(isValidShift()==false)
				return;
			
			
			
			for (ShiftModel item : lstShift)
			{
				//if(CheckDuplicateName(item))
				//{
				//	 Messagebox.show("Shift Code Should not be Duplicate !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				//	 return;
				//}
				if(CheckDuplicateTime(item))
				{
					 Messagebox.show("Time should not be Duplicate !! !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 return;
				}
				tmpShift=item;
				
				if(item.getShiftKey()==0)
				{
					if(shiftKey==0 && item.getShiftCode().equals(""))
					{
						 Messagebox.show("Shift Code Should not be blank !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						 return;
					}
					else
					{
						if(item.getShiftType().isTimingShift()==false)
						{
							shiftKey=data.insertShiftMaster(tmpShift,false);
							data.insertShiftMaster(tmpShift,true);
							break;
						}
						else
						{
							if(shiftKey==0)
							{
							shiftKey=data.insertShiftMaster(tmpShift,false);
							data.insertShiftMaster(tmpShift,true);	
							}
							else
							{
							tmpShift.setShiftKey(shiftKey);
						    data.insertShiftMaster(tmpShift,true);
							}
						}
					}
				}
				else
				{
					if(tmpRecNo!=item.getRecNo())
					{
					tmpRecNo=item.getRecNo();
					data.updateShiftMaster(item);
					data.deleteShiftDet(item);					
					}
					data.insertShiftMaster(tmpShift,true);
				}
			}
			
			Messagebox.show("Shift is saved !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			lstShift=new ListModelList<ShiftModel>( data.getShiftList(selectedCompany.getCompKey()));
			for (ShiftModel item : lstShift)
			{
				item.setShiftType(bindShiftType(item));
			}
			
			 
		 }	 
		 catch (Exception ex)
		 {	
		   logger.error("ERROR in ShiftCreationViewModel ----> saveShiftCommand", ex);			
		  }
	 }
	
	 private boolean isValidShift()
	 {
		 boolean isValid=true;
		 int shiftKey=0;
		 for (ShiftModel item : lstShift)
		  {
			 if(item.isTimingShift()==false)
			 {
			 if(item.getShiftCode().equals(""))
				{
				   Messagebox.show("Shift Code Should not be blank !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				   return false;
				}
			 }
			 else
			 {
				 if(shiftKey!=item.getShiftKey())
				 {
					 shiftKey=item.getShiftKey();
					 if(item.getShiftCode().equals(""))
						{
						   Messagebox.show("Shift Code Should not be blank !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						   return false;
						}
				 }
			 }			 			
		  }
		 
		 //check dublicate Name
		 shiftKey=0;
		 for (ShiftModel item : lstShift)
		 {
			 if(shiftKey!=item.getShiftKey())
			 {
				 shiftKey=item.getShiftKey();
				 if(CheckDuplicateName(item))				 
					{
					 logger.info("ssssssss> " + shiftKey);
					   Messagebox.show("Shift Code Should not be Duplicate !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					   return false;
					}
			 }
		 }
		 
		 //check empty time
		 for (ShiftModel item : lstShift)
		  {
			 if(item.isTimingShift())
			 {
			 if(item.getFromTime()==null || item.getToTime()==null)
				{
				   Messagebox.show("Time should not be blank !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				   return false;
				}
			 }
		  }
		 return isValid;
	 }
	 
	 @Command
	 @NotifyChange({"lstShift"})
	 public void deleteShiftCommand(@BindingParam("row") final ShiftModel row)
	 {
		 try
		 {
			 //check if shift is used
			 if(row.getShiftKey()>0)
			 {
			    if(data.checkIfShiftUsed(row.getShiftKey()))
			 	{
				  Messagebox.show("You cannot Delete this Shift.Data Exists for this Shift !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				   return;
			 	}
			 }
			 			
				 Messagebox.show("Are you sure to delete this Shift ?","Time Sheet Setup",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
				 {
					 public void onEvent(Event e)
					 {
						 if (Messagebox.ON_YES.equals(e.getName()))
		               {					 
						
							if(row.getShiftKey()>0)
							{
							  data.deleteShift(row);
							 // lstShift=new ListModelList<ShiftModel>( data.getShiftList(selectedCompany.getCompKey()));
							 // Messagebox.show("Shift is deleted !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
							}
							
								if(row.isTimingShift()==false)
									lstShift.remove(row);
								else
								{
									 List<ShiftModel> lstOldShifts=new ArrayList<ShiftModel>();		
									 for (ShiftModel item : lstShift) 
										{
											if(item.getShiftKey()==row.getShiftKey())
											{
												lstOldShifts.add(item);						
											}
										}		 		
									 lstShift.removeAll(lstOldShifts);
								}
													
		               }
					 }
				 });		
			 
		 }
		 catch (Exception ex)
		 {	
		   logger.error("ERROR in ShiftCreationViewModel ----> deleteShiftCommand", ex);			
		 }
	 }
	 @Command
	 @NotifyChange({"lstSetShift"})
	 public void changeIncludeHoliday()
	 {
		 fillSetShifts();
	 }
	 @Command
	 @NotifyChange({"lstSetShift"})
	 public void checkOffDayCommand(@BindingParam("row") ShiftModel row)
	 {
		 //onCheck="@command('checkOffDayCommand' ,row=each)"
		/* for (ShiftModel item : lstShift)
		 {
			 if(item.getShiftKey()==row.getShiftKey())
			 {
				 if(row.isOffDay()==false)
				 {
					 row.setUnits(item.getUnits());
					 //Messagebox.show("You cannot Delete this Shift.Data Exists for this Shift !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				 }
			 }
		 }*/
		 
		 for (ShiftModel item : lstSetShift)
		 {
			 if(item.getShiftKey()==row.getShiftKey() && item.getDayNo()==row.getDayNo())
			 {
				 item.setUnits(11);
			 }
		 }
	 }
	 
	 //Set Shifts
	 private void fillSetShifts()
	 {
		 int shiftMastKey=0;
		 lstSetShift=new ListModelList<ShiftModel>(); //ArrayList<ShiftModel>();
		 if(lstShift.size()==1)//add this for only first time add shift 
		 {
			 getShiftDet(lstShift.get(0));
		 }
		 
		 else
		 {
		 for (ShiftModel item : lstShift)
		 {
			 if(shiftMastKey!=item.getShiftKey())
			 {
				 shiftMastKey=item.getShiftKey();
				// if(item.getShiftKey()==1)
				 getShiftDet(item);
			 }
		 }
		}
	 }
	 private void getShiftDet(ShiftModel objMast)
	 {		
		 DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
	     String[] weekdays = dateFormatSymbols.getWeekdays();
	     int tmpDayNo=0;  
	     if(objMast.isTimingShift()==false)
	     {
		 for (ShiftModel item : lstShift)
		 {
			 if(item.getShiftKey()==objMast.getShiftKey())
			 {
				 for (int i = 0; i < weekdays.length-1; i++) 
				 {
					 tmpDayNo = objCompanySetup.getWeekStart();
					 tmpDayNo=tmpDayNo+i;
					 if(tmpDayNo>7)
						 tmpDayNo=tmpDayNo-7;
					 
					 ShiftModel obj=new ShiftModel();
					 if(i==0)
						 obj.setFirstRecord(true);
					 obj.setShiftCodeRowspan(7);
					 obj.setDayRowspan(1);
					 obj.setShiftCode(objMast.getShiftCode());
					 obj.setDayName(weekdays[tmpDayNo]);//.substring(0,3) + " " +tmpDayNo);
					 obj.setShiftKey(objMast.getShiftKey());
					 obj.setTimingFlag(item.getTimingFlag());
					 obj.setTimingShift(item.isTimingShift());
					 obj.setUnits(item.getUnits());
					 obj.setFromTime(item.getFromTime());
					 obj.setToTime(item.getToTime());
					 obj.setDayNo(tmpDayNo);
					 obj.setCompKey(item.getCompKey());
					 CompanyShiftModel setupItem= getSetupShift(obj);
					 if(setupItem!=null)
					 {
						 obj.setOffDay(setupItem.getOffDay().equals("Y"));						
						 if(obj.isOffDay() && objCompanySetup.isCanIncludeholidayUnit()==false)
						 {							 
							 obj.setUnits(0);
							 obj.setFromTime(null);
							 obj.setToTime(null);
						 }
					 }
					 lstSetShift.add(obj);
				 }
			 }
		 }
	    }
	     else
	     {
	    	 boolean isCodeFirstRecord=true;
	    	 for (int i = 0; i < weekdays.length-1; i++) 
			 {
	    		 boolean isDayFirstRecord=true;	    		
				 tmpDayNo = objCompanySetup.getWeekStart();
				 tmpDayNo=tmpDayNo+i;
				 if(tmpDayNo>7)
					 tmpDayNo=tmpDayNo-7;
				 
				 for (ShiftModel item : lstShift)
				 {
					 if(item.getShiftKey()==objMast.getShiftKey())
					 {
						 ShiftModel obj=new ShiftModel();
						 if(i==0)
							 obj.setFirstRecord(isCodeFirstRecord);
						 obj.setDayfirstRecord(isDayFirstRecord);
						 obj.setShiftCodeRowspan(item.getNoOfShifts()*7);
						 obj.setDayRowspan(item.getNoOfShifts());
						 obj.setShiftCode(objMast.getShiftCode());
						 obj.setDayName(weekdays[tmpDayNo]);//.substring(0,3) + " " +tmpDayNo);
						 obj.setShiftKey(objMast.getShiftKey());
						 obj.setTimingFlag(item.getTimingFlag());
						 obj.setTimingShift(item.isTimingShift());
						 obj.setUnits(item.getUnits());
						 obj.setFromTime(item.getFromTime());
						 obj.setToTime(item.getToTime());
						 obj.setDayNo(tmpDayNo);
						 obj.setCompKey(item.getCompKey());
						 CompanyShiftModel setupItem= getSetupShift(obj);
						 if(setupItem!=null)
						 {
							 obj.setOffDay(setupItem.getOffDay().equals("Y"));
							 if(obj.isOffDay() && objCompanySetup.isCanIncludeholidayUnit()==false)
							 {
								 obj.setUnits(0);
								 obj.setFromTime(null);
								 obj.setToTime(null);
							 }
						 }
						 lstSetShift.add(obj);
						 isDayFirstRecord=false;
						 isCodeFirstRecord=false;
						 
					 }
				 }
				 
			 }
	    	 
	     }
	 }
	 private CompanyShiftModel getSetupShift(ShiftModel objShift)//int shiftKey,int dayNo)
	 {
		 CompanyShiftModel obj=null;
		 DateFormat hdf = new SimpleDateFormat("hh:mm a");
		 if(objShift.isTimingShift()==false)
		 {
		 for (CompanyShiftModel item : lstCompanyShiftSetup) 
		 {
			if(item.getShiftKey()==objShift.getShiftKey() && item.getDayNo()==objShift.getDayNo())
				return item;
		 }
		 }
		 else
		 {
			 for (CompanyShiftModel item : lstCompanyShiftSetup) 
			 {
				 if(item.getShiftKey()==objShift.getShiftKey() && item.getDayNo()==objShift.getDayNo())
				 {
					 if(item.getFromTime()!=null)
					 {
						 if(hdf.format(objShift.getFromTime()).equals(hdf.format(item.getFromTime())))
								return item;
					 }
					 else
					 {
						 return item;
					 }
				 }
				/*if(item.getFromTime()!=null)
				if(item.getShiftKey()==objShift.getShiftKey() && item.getDayNo()==objShift.getDayNo() && hdf.format(objShift.getFromTime()).equals(hdf.format(item.getFromTime())))
					return item;*/
			 }
		 }
		 
		 return obj;
	 }
	 
	 @Command
	 @NotifyChange({"lstSetShift"})
	 public void saveSetShiftCommand()
	 {
		 try
		 {
			 data.deleteAllCompanyShift(selectedCompany.getCompKey());
			 for (ShiftModel item : lstSetShift)
			 {								
			   data.insertCompanyShift(item,objCompanySetup);				
			 }
			 Messagebox.show("Set Shifts is saved !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
		 }
		 catch (Exception ex)
		 {	
		   logger.error("ERROR in ShiftCreationViewModel ----> saveSetShiftCommand", ex);			
		 }
	 }
	 
	 @Command
	 @NotifyChange({"lstShift","lstSetShift"})
	 public void saveALLCommand()
	 {
		 try
		 {
		 	ShiftModel tmpShift;
			int shiftKey=0;
			int tmpRecNo=0;
			if(isValidShift()==false)
				return;
			
			//fillSetShifts();
			boolean isSetOffDay=true;
			
			for (ShiftModel item : lstSetShift)//check if new shift is added
			{
				if(item.getShiftKey()==0)
				{	
					isSetOffDay=false;
				}
			}
			
			if(isSetOffDay==false)
			{
				for (ShiftModel item : lstSetShift)
				{
					if(item.getShiftKey()==0 && item.isOffDay())
					{	
						isSetOffDay=true;					
					}
				}
			}
			
			if(isSetOffDay==false)
			{
				Messagebox.show("Please go set Shift and assign off days !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			//if(true)
			//return;
			
			
			//update companySetup
			objCompanySetup.setDefHours(defaultHours);
			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
	        String[] weekdays = dateFormatSymbols.getWeekdays();
	        for (int i = 1; i < weekdays.length; i++)
	        {
	            String day = weekdays[i];	
	            if(selectedDayOfWeek==day)
	               objCompanySetup.setWeekStart(i);	            
	        }     	        
			data.updateCompanySetup(objCompanySetup);
			
			
			//save shifts data
			 for (ShiftModel item : lstShift)
			 {
				 if(CheckDuplicateTime(item))
					{
						 Messagebox.show("Time should not be Duplicate !! !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
						 return;
					}
				 
				 tmpShift=item;
				 if(item.getShiftKey()==0)
				  {
						if(shiftKey==0 && item.getShiftCode().equals(""))
						{
							 Messagebox.show("Shift Code Should not be blank !!","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);
							 return;
						}
						else
						{
							if(item.getShiftType().isTimingShift()==false)
							{
								shiftKey=data.insertShiftMaster(tmpShift,false);
								data.insertShiftMaster(tmpShift,true);
								break;
							}
							else
							{
								if(shiftKey==0)
								{
								shiftKey=data.insertShiftMaster(tmpShift,false);
								data.insertShiftMaster(tmpShift,true);	
								}
								else
								{
								tmpShift.setShiftKey(shiftKey);
							    data.insertShiftMaster(tmpShift,true);
								}
							}
						}
					}
				 
				 	else
					{
						if(tmpRecNo!=item.getRecNo())
						{
						tmpRecNo=item.getRecNo();
						data.updateShiftMaster(item);
						data.deleteShiftDet(item);					
						}
						data.insertShiftMaster(tmpShift,true);
					}				 				
			 }
			 
			 //save SetShifts
			 data.deleteAllCompanyShift(selectedCompany.getCompKey());
			 for (ShiftModel item : lstSetShift)
			 {
				if(item.getShiftKey()==0)
					item.setShiftKey(shiftKey);
			   data.insertCompanyShift(item,objCompanySetup);				
			 }
			 
			 
			
			 lstShift=new ListModelList<ShiftModel>( data.getShiftList(selectedCompany.getCompKey()));
			 for (ShiftModel item : lstShift)
			 {
				 item.setShiftType(bindShiftType(item));
			 }
			 lstCompanyShiftSetup=data.getCompanyShiftSetupList(selectedCompany.getCompKey());
			 fillSetShifts();
			 Messagebox.show("Data Saved Successfully.","Time Sheet Setup", Messagebox.OK , Messagebox.EXCLAMATION);	
		 }
		 catch (Exception ex)
		 {	
		   logger.error("ERROR in ShiftCreationViewModel ----> saveALLCommand", ex);			
		 }
	 }
	 
	private void fillHolidays()
	{
		lstHolidays=data.getCompanyHolidays(selectedCompany.getCompKey());
		columnList=new ArrayList<String>();
		for (ShiftModel item : lstShift)	
		{
			if(!columnList.contains(item.getShiftCode()))
				columnList.add(item.getShiftCode().trim());
		}
		
		List<ShiftModel> lstHolidayDetail=data.getCompanyHolidayDetails(selectedCompany.getCompKey());
		for (ShiftModel item : lstHolidays)
		{
			//List<ShiftModel> lstDetail=getShiftHolidayDetRec(lstHolidayDetail,item.getRecNo());
			
			boolean shiftHolidays[]=new boolean[columnList.size()];
			String[] labelColor=new String[3];
			int i=0;
			for (String col : columnList) 
			{
				ShiftModel objDetail=getShiftHolidayDetRec(lstHolidayDetail,item.getRecNo(),col);
				if(objDetail!=null)
				{
					 shiftHolidays[i]=true;						
				}
				i++;
			}
			
			
			/*for (ShiftModel b : lstDetail)
			{
				i++;
				for (String col : columnList) 
				{
					if(b.getShiftCode().trim().equals(col))
					{
						 labelColor[i]=b.getShiftCode();
						 shiftHolidays[i]=true;						 
						// i++;
						continue;
					}
					else
					{
					shiftHolidays[i]=false;
					//break;
					//labelColor[i]=b.getShiftCode()+"#" + col;
					//i++;
					}
					//i++;
				}
				//i++;
			}*/
			
			/*for (String col : columnList) 
			{								
				for (ShiftModel b : lstDetail)
				{
					if(b.getShiftCode().trim().equals(col))
					{
					 shiftHolidays[i]=true;
					 labelColor[i]=b.getShiftCode();
					 
					}
					 else
					 {
					shiftHolidays[i]=false;
					labelColor[i]=b.getShiftCode()+"#" + col;
					
					}
					i++;
				}
				
			}*/
			
			
			//Map map = new LinkedHashMap();
			//map.put(item.getRecNo(), shiftHolidays);
			//item.setMapShifts(map);
			item.setLabelColor(labelColor);
			item.setShiftHolidays(shiftHolidays);
		}
		
	}
	private  ShiftModel getShiftHolidayDetRec(List<ShiftModel> lstHolidayDetail ,int recNo,String colunmName)
	{
		ShiftModel lst=null;
		for (ShiftModel item : lstHolidayDetail) 
		{
			if(item.getRecNo()==recNo && item.getShiftCode().equals(colunmName))
				return item;
		}
		return lst;
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

	@NotifyChange({"lstShift","lstDayOfWeek","lstSetShift","lstHolidays","objCompanySetup"})
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		lstShift=new ListModelList<ShiftModel>( data.getShiftList(selectedCompany.getCompKey()));
		for (ShiftModel item : lstShift)
		{
			item.setShiftType(bindShiftType(item));
		}

		//setShifts
		objCompanySetup=data.getCompanySetup(selectedCompany.getCompKey());	
		lstCompanyShiftSetup=data.getCompanyShiftSetupList(selectedCompany.getCompKey());
		fillDayOfWeek();
		fillSetShifts();

		fillHolidays();
		
	}

	public ListModelList<ShiftModel> getLstShift() {
		return lstShift;
	}

	public void setLstShift(ListModelList<ShiftModel> lstShift) {
		this.lstShift = lstShift;
	}

	public List<ShiftModel> getLstShiftType() {
		return lstShiftType;
	}

	public void setLstShiftType(List<ShiftModel> lstShiftType) {
		this.lstShiftType = lstShiftType;
	}

	public ShiftModel getSelectedShiftType() {
		return selectedShiftType;
	}

	public void setSelectedShiftType(ShiftModel selectedShiftType) {
		this.selectedShiftType = selectedShiftType;
	}

	public List<String> getLstDayOfWeek() {
		return lstDayOfWeek;
	}

	public void setLstDayOfWeek(List<String> lstDayOfWeek) {
		this.lstDayOfWeek = lstDayOfWeek;
	}
	public ListModelList<ShiftModel> getLstSetShift() {
		return lstSetShift;
	}
	public void setLstSetShift(ListModelList<ShiftModel> lstSetShift) {
		this.lstSetShift = lstSetShift;
	}
	public CompSetupModel getObjCompanySetup() {
		return objCompanySetup;
	}
	public void setObjCompanySetup(CompSetupModel objCompanySetup) {
		this.objCompanySetup = objCompanySetup;
	}
	public String getSelectedDayOfWeek() {
		return selectedDayOfWeek;
	}
	public void setSelectedDayOfWeek(String selectedDayOfWeek) {
		this.selectedDayOfWeek = selectedDayOfWeek;
	}
	public int getDefaultHours() {
		return defaultHours;
	}
	public void setDefaultHours(int defaultHours) {
		this.defaultHours = defaultHours;
	}
	public List<ShiftModel> getLstHolidays() {
		return lstHolidays;
	}
	public void setLstHolidays(List<ShiftModel> lstHolidays) {
		this.lstHolidays = lstHolidays;
	}
	public List<String> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}
	public MenuModel getCompanyRole() {
		return companyRole;
	}
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
	public int getSelectedTab() {
		return selectedTab;
	}
	@NotifyChange({"lstSetShift"})
	public void setSelectedTab(int selectedTab) 
	{
		this.selectedTab = selectedTab;
		if(selectedTab==1)
		{
			fillSetShifts();
		}
	}
}
