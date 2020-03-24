package hr;

import hr.model.CompanyModel;
import hr.model.SponsorModel;
import hr.model.WorkGroupModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.CashInvoiceGridData;
import model.EmployeeFilter;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditWorkGroupViewModel {
	
	private Logger logger = Logger.getLogger(this.getClass());
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	WorkGroupData workGroupData=new WorkGroupData();
	private WorkGroupModel selectedWorkGroup;
	private List<EmployeeModel> lstSupervisor=new ArrayList<EmployeeModel>();
	private EmployeeModel selectedSupervisor;
	HRData data = new HRData();
	private boolean canSave;
	private boolean showBankFields;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	int selectedComapnyId=0;
	List<EmployeeModel> lstEmployees=new ArrayList<EmployeeModel>();
	private List<EmployeeModel> lstAllEmployees;
	private EmployeeModel selectedEmployee;
	String SelectedCompanyName="";
	List<CompanyModel> lstComapnies=new ArrayList<CompanyModel>();
	private int footer;
	
	private String supervisorStatus;
	
	private List<String> lstSattus;
	private String selectedStatus;
	
	private boolean adminUser;
	WebusersModel dbUser;
	
	@SuppressWarnings("unchecked")
	public EditWorkGroupViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			dbUser = (WebusersModel) sess.getAttribute("Authentication");
			if (dbUser != null) {
				adminUser = dbUser.getFirstname().equals("admin");
			}
			lstSattus=new ArrayList<String>();
			lstSattus.add("All");
			lstSattus.add("Active");
			lstSattus.add("INActive");
			selectedStatus=lstSattus.get(0);
			
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int workGroupKey=(Integer) map.get("workGroup");
			int comapnyId=(Integer) map.get("compKey");
			String CompanyName=(String) map.get("compName");
			SelectedCompanyName=CompanyName;
			String type=(String)map.get("type");
			selectedComapnyId=comapnyId;
			Window win = (Window)Path.getComponent("/workGroupListModalDialog");
			if(type.equals("edit"))
			{
			canSave=false;
			win.setTitle("Edit Team Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add Team Info");
			}
			else
			{
				win.setTitle("View Team Info");
			}
			lstComapnies = data.getCompanyList(dbUser.getUserid());
			for(CompanyModel model:lstComapnies)
			{
				List<EmployeeModel> supervisorBassedOnComapny=new ArrayList<EmployeeModel>();
				supervisorBassedOnComapny=workGroupData.getSuperVisorNameDropDown(model.getCompKey());
				lstSupervisor.addAll(supervisorBassedOnComapny);
			}
			if(workGroupKey>0)
			{
				Map<String, Object> mapWorkGroup=new HashMap<String, Object>();
				mapWorkGroup=workGroupData.getWorkGroupById(workGroupKey);
				if(mapWorkGroup!=null && mapWorkGroup.isEmpty()!=true)
				{
				selectedWorkGroup=(WorkGroupModel) mapWorkGroup.get("workGroupModel");
				if(selectedWorkGroup.getSupervisorName()!=null && !selectedWorkGroup.getSupervisorName().equalsIgnoreCase(""))
				{
				lstEmployees=(List<EmployeeModel>) mapWorkGroup.get("empList");
				boolean issuperSuperVisor=workGroupData.checkIfSuperSupervisor(Integer.parseInt(selectedWorkGroup.getSupervisor()));
				if(issuperSuperVisor)
				{
					selectedWorkGroup.setWetherSupersupervisor(true);
				}
				else
				{
					selectedWorkGroup.setWetherSupersupervisor(false);
				}
				}
				lstAllEmployees=lstEmployees;
				footer=lstEmployees.size();
				
				if(selectedWorkGroup.getCompKey()==0)
				{
					mapWorkGroup=workGroupData.getWorkGroupByIdForZeroEmployees(workGroupKey,comapnyId);
					selectedWorkGroup=(WorkGroupModel) mapWorkGroup.get("workGroupModel");
				}
				}
			
				
				if(null!=lstSupervisor)
				{
					for(EmployeeModel groupModel:lstSupervisor)
					{
						if(null!=selectedWorkGroup.getSupervisorName()&& selectedWorkGroup.getSupervisorName().equalsIgnoreCase(groupModel.getFullName()))
						{
							selectedSupervisor=groupModel;
							break;
						}
						
					}
				}
				if(selectedWorkGroup.getIsActive().equalsIgnoreCase("Y"))
				{
					selectedWorkGroup.setIsActive("false");
				}
				else
				{
					selectedWorkGroup.setIsActive("true");
				}
				String ProjectName="";
				if(null!=selectedSupervisor.getProjectName() && !"".equalsIgnoreCase(selectedSupervisor.getProjectName()))
				{
					ProjectName=selectedSupervisor.getProjectName();
				}
				else
				{
					ProjectName="None";
				}
				
				supervisorStatus=selectedSupervisor.getStatus()+"     , Supervisor Project/Location : " +ProjectName;
			
			}
			else
			{
				selectedWorkGroup=new WorkGroupModel();
				selectedWorkGroup.setRecno(0);
				selectedWorkGroup.setGroupName("");
				selectedWorkGroup.setGroupNameAR("");
				selectedWorkGroup.setIsActive("false");
				selectedWorkGroup.setCompKey(selectedComapnyId);
				selectedWorkGroup.setSupervisor("");
				selectedWorkGroup.setSupervisorName("");
				selectedWorkGroup.setSupervisorNameAR("");
				
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditWorkGroupViewModel ----> init", ex);			
		}
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				WorkGroupModel fx = (WorkGroupModel)ctx.getProperty().getValue();				
				String name = fx.getGroupName();
																		
				if(Strings.isBlank(name))
				{
					Clients.showNotification("Please fill all the required fields (*)  !!");
					//mark the validation is invalid, so the data will not update to bean
					//and the further command will be skipped.
					ctx.setInvalid();
				}										
			}
		};
	}
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange({"lstSupervisor","selectedSupervisor","supervisorStatus"})
	public void CheckSupervisorStatus(@BindingParam("cmp") final Window x)
	{
		
		 if(selectedSupervisor!=null && selectedSupervisor.getStatus().equalsIgnoreCase("inactive"))
		 {
			 
			 Messagebox.show("You have selected the InActive supervisor, Do you want to make active and continue.? ","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
						new org.zkoss.zk.ui.event.EventListener() {						
				    public void onEvent(Event evt) throws InterruptedException {
				        if (evt.getName().equals("onYes")) 
				        {
				        	workGroupData.updateEmployeeStatus(selectedSupervisor.getEmployeeKey());
				        	lstSupervisor=new ArrayList<EmployeeModel>();
				        	for(CompanyModel model:lstComapnies)
							{
								List<EmployeeModel> supervisorBassedOnComapny=new ArrayList<EmployeeModel>();
								supervisorBassedOnComapny=workGroupData.getSuperVisorNameDropDown(model.getCompKey());
								lstSupervisor.addAll(supervisorBassedOnComapny);
								
							}
				        	/*Clients.showNotification("The employee status has been updated to active successfully.",
						            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				        	*/
				        	Clients.showNotification("Re open the add team window and select again the supervisor you have activated.",
						            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				        	x.detach();
				        }
				        else 
				        {		 
				        	return;
				        }
				    }
				
				});
			 
		 }
		
	}

	   @SuppressWarnings("unchecked")
	@Command
	   @NotifyChange({"lstWorkGroup","footer"})
	   public void updateWorkGroupList(@BindingParam("cmp") final Window x) throws ParseException
	   {
		 
		 
		 if(selectedWorkGroup.getIsActive().equalsIgnoreCase("false"))
			{
				selectedWorkGroup.setIsActive("Y");
			}
			else
			{
				selectedWorkGroup.setIsActive("N");
			}
		 
		 if(selectedSupervisor!=null && selectedSupervisor.getStatus().equalsIgnoreCase("inactive"))
		 {
			 Messagebox.show("You have selected the InActive supervisor you connot continue.","Team List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
			 
		 if(selectedWorkGroup.getGroupName().equalsIgnoreCase(""))
		 {
			 Messagebox.show("Please Enter the Team Name.","Team List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 if(selectedSupervisor!=null)
		 {
			 selectedWorkGroup.setSupervisor(""+selectedSupervisor.getEmployeeKey());
		 }
		 else
		 {
			 
			 lstEmployees= new ArrayList<EmployeeModel>();
		 }
		 if(selectedWorkGroup.isWetherSupersupervisor())
		 {
			 selectedWorkGroup.setSupersupervisor(selectedSupervisor.getEmployeeKey());
		 }
		
		 List<WorkGroupModel> workGroupNames=workGroupData.fillWorkGroupInfo(selectedComapnyId,"");
		
		 if(selectedWorkGroup.getRecno()>0)
		 {
			 int result=0;
			 for(WorkGroupModel ValidationName:workGroupNames)
				{
				 if(selectedWorkGroup.getGroupName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getGroupName().replaceAll("\\s","")) && (selectedWorkGroup.getRecno()!=ValidationName.getRecno()))
					{
						Messagebox.show("The Team Name already exist.","Team List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				 if(!selectedWorkGroup.getGroupNameAR().equalsIgnoreCase("")&& !ValidationName.getGroupNameAR().equalsIgnoreCase("") &&selectedWorkGroup.getGroupNameAR().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getGroupNameAR().replaceAll("\\s","")) && (selectedWorkGroup.getRecno()!=ValidationName.getRecno()))
					{
						Messagebox.show("The Team Name-Arabic already exist.","Team List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			  
			 if(selectedSupervisor==null)
				{
				 Messagebox.show("Please select a supervisor for team ! you connot continue.","Team List",Messagebox.OK , Messagebox.INFORMATION);
				 return;
					/*Messagebox.show("You have not selected any supervisor for team, Do you want to continue.? ","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
	 				    public void onEvent(Event evt) throws InterruptedException {
	 				        if (evt.getName().equals("onYes")) 
	 				        {
	 				        	int result=0;
	 				        	result= workGroupData.updatetWorkGroup(selectedWorkGroup,lstEmployees);
	 				        	sucessMessage(result,x);
	 				        }
	 				        else 
	 				        {		 
	 				        	return;
	 				        }
	 				    }
	 				
					});*/
				}
				else if(lstEmployees.size()==0)
				{
					Messagebox.show("Please select the employees for selected supervisor! you connot continue.","Team List",Messagebox.OK , Messagebox.INFORMATION);
					 return;
					/*Messagebox.show("You have not selected employees for team, Do you want to continue.? ","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
	 				    public void onEvent(Event evt) throws InterruptedException {
	 				        if (evt.getName().equals("onYes")) 
	 				        {
	 				        	int result=0;
	 				        	result= workGroupData.updatetWorkGroup(selectedWorkGroup,lstEmployees);
	 				        	sucessMessage(result,x);
	 				        }
	 				        else 
	 				        {		 
	 				        	return;
	 				        }
	 				    }
	 				
					});*/
				}
				else{
					result= workGroupData.updatetWorkGroup(selectedWorkGroup,lstEmployees);
					sucessMessage(result,x);
				}
			 		
		 }
		 else
		 {
			 int result=0;
			 for(WorkGroupModel ValidationName:workGroupNames)
				{
					if(selectedWorkGroup.getGroupName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getGroupName().replaceAll("\\s","")))
					{
						Messagebox.show("The Team Name already exist.","Team List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
					 if(!selectedWorkGroup.getGroupNameAR().equalsIgnoreCase("")&& !ValidationName.getGroupNameAR().equalsIgnoreCase("") && selectedWorkGroup.getGroupNameAR().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getGroupNameAR().replaceAll("\\s","")))
						{
							Messagebox.show("The Team Name-Arabic already exist.","Team List",Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
					
				}
			 if(selectedSupervisor==null)
				{
				 Messagebox.show("Please select a supervisor for team ! you connot continue.","Team List",Messagebox.OK , Messagebox.INFORMATION);
				 return;
					/*Messagebox.show("You have not selected any supervisor for team, Do you want to continue.? ","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
	 				    public void onEvent(Event evt) throws InterruptedException {
	 				        if (evt.getName().equals("onYes")) 
	 				        {
	 				        	int result=0;
	 				        	 int tmpRecNo=workGroupData.getWorkGroupMaxRecQuerry();
	 							 result=workGroupData.insertWorkGroup(selectedWorkGroup,tmpRecNo,lstEmployees);
	 							 sucessMessage(result,x);
	 				        }
	 				        else 
	 				        {		 
	 				        	return;
	 				        }
	 				    }
	 				
					});*/
				}
				else if(lstEmployees.size()==0)
				{
					Messagebox.show("Please select the employees for selected supervisor! you connot continue.","Team List",Messagebox.OK , Messagebox.INFORMATION);
					 return;
					/*Messagebox.show("You have not selected employees for team, Do you want to continue.? ","Quantity", Messagebox.YES | Messagebox.NO  , Messagebox.QUESTION,
							new org.zkoss.zk.ui.event.EventListener() {						
	 				    public void onEvent(Event evt) throws InterruptedException {
	 				        if (evt.getName().equals("onYes")) 
	 				        {
	 				        	int result=0;
	 				        	 int tmpRecNo=workGroupData.getWorkGroupMaxRecQuerry();
	 							 result=workGroupData.insertWorkGroup(selectedWorkGroup,tmpRecNo,lstEmployees);
	 							 sucessMessage(result,x);
	 				        }
	 				        else 
	 				        {		 
	 				        	return;
	 				        }
	 				    }
	 				
					});*/
				}
				else{
					 int tmpRecNo=workGroupData.getWorkGroupMaxRecQuerry();
					 result=workGroupData.insertWorkGroup(selectedWorkGroup,tmpRecNo,lstEmployees);
					 sucessMessage(result,x);
				}
			
		 }
		 
		
	   }
	   
	  public void sucessMessage(int result,Window x)
	  {
		  if(result==1)
			{
			
				if(selectedWorkGroup.getRecno()>0)
				{
						Clients.showNotification("The Team "+selectedWorkGroup.getGroupName()+" Has Been Updated Successfully.",
			            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}else
				{
					 Clients.showNotification("The Team "+selectedWorkGroup.getGroupName()+" Has Been Saved  Successfully.",
					 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}
			 Map args = new HashMap();
			 args.put("type", "workGroup");		
			 BindUtils.postGlobalCommand(null, null, "refreshParentWorkGroup", args);
			 x.detach();
			}
			else
				Clients.showNotification("Error at Team Saving !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			x.detach();
			
	  }
	   
	   @Command
	    @NotifyChange({"lstEmployees","footer"})
	    public void changeFilterEdit() 
	    {	      
	    	lstEmployees=filterDataEdit();
	    	footer=lstEmployees.size();
	    }
	 
	 	private List<EmployeeModel> filterDataEdit()
		{
			lstEmployees=lstAllEmployees;
					
			List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
			if(null!=lstEmployees && lstEmployees.size()>0)
			{
			for (Iterator<EmployeeModel> i = lstEmployees.iterator(); i.hasNext();)
			{
				EmployeeModel tmp=i.next();				
						if(tmp.getFullName().toLowerCase().contains(employeeFilter.getFullName().toLowerCase())&&
						tmp.getDepartment().toLowerCase().contains(employeeFilter.getDepartment().toLowerCase())&&
						tmp.getPosition().toLowerCase().contains(employeeFilter.getPosition().toLowerCase())&&
						tmp.getStatus().toLowerCase().contains(employeeFilter.getStatus().toLowerCase())&&
						//tmp.getCountry().toLowerCase().contains(employeeFilter.getCountry().toLowerCase())&&
						//tmp.getAge().toLowerCase().startsWith(employeeFilter.getAge().toLowerCase())&&
						tmp.getEmployeeNo().toLowerCase().contains(employeeFilter.getEmployeeNo().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
			}
			return lst;
			
		}
	   
	   @GlobalCommand 
	   @NotifyChange({"lstEmployees","footer"})
		    public void getemployeesId(@BindingParam("myData")String empKeys)
				  {		
					 try
					  {
						 int i=0;
						 String reg=",";
						 String[] tokens = empKeys.split(reg);
						 boolean messageFlag=false;
						 boolean SameSupervisorinSameTeam=false;
							 	List<EmployeeModel> employeeModels=new ArrayList<EmployeeModel>();
								employeeModels=workGroupData.getEmployeeById(selectedComapnyId);
								
								for(EmployeeModel employeeItr:employeeModels)
								{
									for(i=0;i<tokens.length;i++)
									 {
											
											if(Integer.parseInt(tokens[i])==employeeItr.getEmployeeKey())
											{
												//messageFlag=false;
												EmployeeModel employeeModel=new EmployeeModel();
												HashSet<EmployeeModel> hs = new HashSet<EmployeeModel>();
												employeeModel=employeeItr;
												
												if(lstEmployees.size()==0)
												{
													if(employeeModel.getWorkGroupId()>0)
													{
														messageFlag=true;
														//Messagebox.show("Employee  "+employeeModel.getFullName()+"has already been assigned to Team Name "+employeeModel.getWorkGroupName()+"","Team List",Messagebox.OK , Messagebox.INFORMATION);
													}
													else
													{
														employeeModel.setLineNo(lstEmployees.size()+1);
														lstEmployees.add(employeeModel);
													}
													
												}
												else
												{
													EmployeeModel employeeModelNew=new EmployeeModel();
													employeeModelNew=employeeItr;
													
													
													for(EmployeeModel model:lstEmployees)
													{
														boolean flag=false;
														if(model.getEmployeeNo().equalsIgnoreCase(employeeModelNew.getEmployeeNo()))
														{
															messageFlag=true;
															//Messagebox.show("Employee  "+employeeModelNew.getFullName()+"  is already exist.","Team List", Messagebox.OK , Messagebox.EXCLAMATION);
														}
														else if(selectedSupervisor!=null && selectedSupervisor.getEmployeeKey()==employeeItr.getEmployeeKey())
														{
															SameSupervisorinSameTeam=true;
														}
														else
														{
															if(employeeModelNew.getWorkGroupId()>0)
															{
																messageFlag=true;
																//Messagebox.show("Employee  "+employeeModelNew.getFullName()+"has already been assigned to Team Name "+employeeModelNew.getWorkGroupName()+"","Team List",Messagebox.OK , Messagebox.INFORMATION);
															}
															else
															{
																for(EmployeeModel modelNew:lstEmployees)
																{
																	if(modelNew.getEmployeeNo().equalsIgnoreCase(employeeModelNew.getEmployeeNo()))
																	{
																		messageFlag=true;
																		flag=true;
																	}
																	else if(selectedSupervisor!=null && selectedSupervisor.getEmployeeKey()==employeeModelNew.getEmployeeKey())
																	{
																		SameSupervisorinSameTeam=true;
																		flag=true;
																	}
																}
																if(!flag==true)
																{
																	employeeModelNew.setLineNo(lstEmployees.size()+1);
																	hs.add(employeeModelNew);
																}
																
															}
														}
														
												 
													}
													
													lstEmployees.addAll(hs);
												
												}
											}
									 }
							 }
							if(messageFlag==true)
							{
									Messagebox.show("One or more employees has already been assigned to same team or different team ","Team List",Messagebox.OK , Messagebox.INFORMATION);
							}
							if(SameSupervisorinSameTeam==true)
							{
									Messagebox.show("You cannot add "+ selectedSupervisor.getFullName() +" in same team, As he is Supervisor for the same Team","Team List",Messagebox.OK , Messagebox.INFORMATION);
							}
							 
						
						/*hs.addAll(lstEmployees);
						lstEmployees.clear();
						lstEmployees.addAll(hs);*/
						lstAllEmployees=lstEmployees;
						footer=lstEmployees.size();
					  }
					 catch (Exception ex)
						{	
						logger.error("ERROR in EditWorkGroupViewModel ----> getemployeesId", ex);			
						}
				  }
	
	   @Command
	   public void findEmployeeCommand(){
		   
		   if(selectedSupervisor==null)
		   {
			   Messagebox.show("Please select the supervisor","Team List",Messagebox.OK , Messagebox.INFORMATION);
				return;
		   }
		   Map<String,Object> arg = new HashMap<String,Object>();
		   arg.put("compKey", selectedComapnyId);
		   arg.put("type", "");
		   Executions.createComponents("/timesheet/searchemployee.zul", null,arg);
			
		}
	   
	   @GlobalCommand 
	    @NotifyChange({"lstEmployees","footer"})
		public void insertemployeeForWorkGroup(@BindingParam("selectedSupervisorWorkgroup") EmployeeModel row)
		{
			if(row!=null)
			{
				List<EmployeeModel> employeeModels=new ArrayList<EmployeeModel>();
				employeeModels=workGroupData.getEmployeeById(selectedComapnyId);
				EmployeeModel employeeModel=new EmployeeModel();
				for(EmployeeModel employeeItr:employeeModels)
				{
					if(row.getEmployeeKey()==employeeItr.getEmployeeKey())
					{
						employeeModel=employeeItr;
						employeeModel.setLineNo(lstEmployees.size()+1);
					}
				}
				
				if(lstEmployees.size()==0)
				{
					if(employeeModel.getWorkGroupId()>0)
					{
						Messagebox.show("Employee  '"+employeeModel.getFullName()+"' has already been assigned to Team Name '"+employeeModel.getWorkGroupName()+"'","Team List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
					lstEmployees.add(employeeModel);
				}
				else
				{
				for(EmployeeModel model:lstEmployees)
				 {
					 if(model.getEmployeeNo().equalsIgnoreCase(employeeModel.getEmployeeNo()))
					 {
						 Messagebox.show("Employee  "+employeeModel.getFullName()+"  is already exist","Team List", Messagebox.OK , Messagebox.EXCLAMATION);
						 return;
					 }
					 
					 if(employeeModel.getWorkGroupId()>0)
						{
							Messagebox.show("Employee  '"+employeeModel.getFullName()+"' has already been assigned to Team Name '"+employeeModel.getWorkGroupName()+"'","Team List",Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
				 }
				lstEmployees.add(employeeModel);
				}
				lstAllEmployees=lstEmployees;
				 footer=lstEmployees.size();
			}
			
		}
	   
	   
	   @Command   
		@NotifyChange({"lstEmployees","footer"})
		public void deleteEmployee(@BindingParam("row") EmployeeModel row)
		{
		   
		   int result=0;
		   
		   if(row!=null)
		   workGroupData.deleteemployesFromEmpMast(row.getEmployeeKey(),selectedWorkGroup.getRecno(),selectedSupervisor.getEmployeeKey());
		   
		   if(lstEmployees.size()==0)
			{
				Clients.showNotification("No Recors to delete.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				return;
			}
			if(selectedEmployee!=null)
			{
				
				lstEmployees.remove(selectedEmployee);
			
			int srNo=0;
			for (EmployeeModel item : lstEmployees)
			{
				srNo++;
				item.setLineNo(srNo);
			}
			
			}
			if(result>0)
			{
					Clients.showNotification("The Employee Has Been Successfully Deleted.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			lstAllEmployees=lstEmployees;
			 footer=lstEmployees.size();
			
		}
		
		@Command
	    @NotifyChange({"lstEmployees"})
		public void insertEmployeeForm()
		{
			 try
			   {
				   Map<String,Object> arg = new HashMap<String,Object>();
				   arg.put("compName",SelectedCompanyName);
				   arg.put("compKey", selectedComapnyId);
				   arg.put("type","view");
				   Executions.createComponents("/hr/list/workGroupAddEmployeeList.zul", null,arg);
			   }
			   catch (Exception ex)
				{	
					logger.error("ERROR in EditWorkGroupViewModel ----> insertEmployeeForm", ex);			
				}
		}
		
	   
	/**
	 * @return the selectedChatOfAccounts
	 */
	
	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

		/**
	 * @return the showBankFields
	 */
	public boolean isShowBankFields() {
		return showBankFields;
	}

	/**
	 * @param showBankFields the showBankFields to set
	 */
	public void setShowBankFields(boolean showBankFields) {
		this.showBankFields = showBankFields;
	}


	/**
	 * @return the selectedWorkGroup
	 */
	public WorkGroupModel getSelectedWorkGroup() {
		return selectedWorkGroup;
	}

	/**
	 * @param selectedWorkGroup the selectedWorkGroup to set
	 */
	public void setSelectedWorkGroup(WorkGroupModel selectedWorkGroup) {
		this.selectedWorkGroup = selectedWorkGroup;
	}

	/**
	 * @return the lstSupervisor
	 */
	public List<EmployeeModel> getLstSupervisor() {
		return lstSupervisor;
	}

	/**
	 * @param lstSupervisor the lstSupervisor to set
	 */
	public void setLstSupervisor(List<EmployeeModel> lstSupervisor) {
		this.lstSupervisor = lstSupervisor;
	}

	/**
	 * @return the selectedSupervisor
	 */
	public EmployeeModel getSelectedSupervisor() {
		return selectedSupervisor;
	}

	/**
	 * @param selectedSupervisor the selectedSupervisor to set
	 */
	 @NotifyChange({"supervisorStatus"})
	public void setSelectedSupervisor(EmployeeModel selectedSupervisor) {
		this.selectedSupervisor = selectedSupervisor;
		if(selectedSupervisor==null)
		{
			Messagebox.show("Please Select a valid Supervisor","Team List",Messagebox.OK , Messagebox.INFORMATION);
			return;
		}
		String ProjectName="";
		if(null!=selectedSupervisor.getProjectName() && !"".equalsIgnoreCase(selectedSupervisor.getProjectName()))
		{
			ProjectName=selectedSupervisor.getProjectName();
		}
		else
		{
			ProjectName="None";
		}
		supervisorStatus=selectedSupervisor.getStatus()+"     , Supervisor Project/Location : " +ProjectName;
	}

	/**
	 * @return the lstEmployees
	 */
	public List<EmployeeModel> getLstEmployees() {
		return lstEmployees;
	}

	/**
	 * @param lstEmployees the lstEmployees to set
	 */
	public void setLstEmployees(List<EmployeeModel> lstEmployees) {
		this.lstEmployees = lstEmployees;
	}

	/**
	 * @return the employeeFilter
	 */
	public EmployeeFilter getEmployeeFilter() {
		return employeeFilter;
	}

	/**
	 * @param employeeFilter the employeeFilter to set
	 */
	public void setEmployeeFilter(EmployeeFilter employeeFilter) {
		this.employeeFilter = employeeFilter;
	}

	/**
	 * @return the selectedEmployee
	 */
	public EmployeeModel getSelectedEmployee() {
		return selectedEmployee;
	}

	/**
	 * @param selectedEmployee the selectedEmployee to set
	 */
	public void setSelectedEmployee(EmployeeModel selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	/**
	 * @return the selectedCompanyName
	 */
	public String getSelectedCompanyName() {
		return SelectedCompanyName;
	}

	/**
	 * @param selectedCompanyName the selectedCompanyName to set
	 */
	public void setSelectedCompanyName(String selectedCompanyName) {
		SelectedCompanyName = selectedCompanyName;
	}

	/**
	 * @return the footer
	 */
	public int getFooter() {
		return footer;
	}

	/**
	 * @param footer the footer to set
	 */
	public void setFooter(int footer) {
		this.footer = footer;
	}

	/**
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * @return the dbUser
	 */
	public WebusersModel getDbUser() {
		return dbUser;
	}

	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(WebusersModel dbUser) {
		this.dbUser = dbUser;
	}

	/**
	 * @return the supervisorStatus
	 */
	public String getSupervisorStatus() {
		return supervisorStatus;
	}

	/**
	 * @param supervisorStatus the supervisorStatus to set
	 */
	public void setSupervisorStatus(String supervisorStatus) {
		this.supervisorStatus = supervisorStatus;
	}

	/**
	 * @return the lstSattus
	 */
	public List<String> getLstSattus() {
		return lstSattus;
	}

	/**
	 * @param lstSattus the lstSattus to set
	 */
	public void setLstSattus(List<String> lstSattus) {
		this.lstSattus = lstSattus;
	}

	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * @param selectedStatus the selectedStatus to set
	 */
	@NotifyChange({"lstEmployees","footer"})
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		lstEmployees=lstAllEmployees;
		
		List<EmployeeModel> lst=new ArrayList<EmployeeModel>();
		if(null!=lstEmployees && lstEmployees.size()>0)
		{
			if(selectedStatus.equalsIgnoreCase("ALL"))
			{
				lst.addAll(lstAllEmployees);
			}
			else
			{
				for (Iterator<EmployeeModel> i = lstEmployees.iterator(); i.hasNext();)
				{
					EmployeeModel tmp=i.next();	
					if(tmp.getStatus().equalsIgnoreCase(selectedStatus))
					{
						lst.add(tmp);
					}
			
				}
			}
		lstEmployees=lst;
		footer=lstEmployees.size();
		}
		
		
	}



}
