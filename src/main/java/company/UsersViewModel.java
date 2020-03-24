package company;

import hr.HRData;
import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.CompanyDBModel;
import model.CompanyRoleModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.CompanyDBData;
import setup.users.WebusersModel;

public class UsersViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	CompanyData data=new CompanyData();
	WebusersModel dbUser=null;
	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;
	private List<MenuModel> lstMainUserMenu;
	
	CompanyDBData companyDbData=new CompanyDBData();
	
	HRData dataHr = new HRData();
	
	private List<CompanyModel> lstCompanies;
	private List<EmployeeModel> lstSuperVisors;
	private List<CompanyModel> lstdefaultCompanies;
	private CompanyModel selectedDeafultCompanies=new CompanyModel();
	private EmployeeModel selectedSuperVisor;
	private boolean supervisorUser;
	private boolean employeeUser;
	
	private List<EmployeeModel> lstEmployees;
	private EmployeeModel selectedEmployee;
	
	
	private List<MenuModel> lstCompanyFileMainMenu;	
	//added by iqbal for removing admin from list temporary
	private List<MenuModel> tmpLstCompanyFileMainMenu;	
	
	private List<MenuModel> lstSubCompanyFileMainMenu;
	private MenuGroupAdapter lstSubMenuGroup;
	
	private boolean checkAllView;
	private boolean checkAllModify;
	private boolean checkAllDelete;
	private boolean checkAllAdd;
	private boolean checkAllExport;
	private boolean checkAllPrint;
	
	private boolean onlyAccountingAccess=false;
	
	private ListModelList<CompanyDBModel> companyAccessList;
	
	private ListModelList<CompanyRoleModel> lstRoles;
	private List<CompanyRoleModel> lstCompanyRoles;
	private List<CompanyRoleModel> templstCompanyRoles;
	private CompanyRoleModel selectedCompanyRole;
	int parentId=0;
	int maxUsers;
	private String maxUsersAllowed;
	
	private boolean ishba=false;
	private boolean isHr=false;
	private boolean isBothHrAndHba=false;
	
	private  String listType;
	@Init
    public void init(@BindingParam("type") String type)
	{
		try
		{				
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser==null)
		{
			Executions.sendRedirect("/login.zul");
		}
		
		companyAccessList=new ListModelList<CompanyDBModel>(companyDbData.getCompanyDBAccessList(dbUser.getCompanyid()));
		for (CompanyDBModel item : companyAccessList) 
		{
			if(item.getDbtype().equalsIgnoreCase("hba"))
			{
				ishba=true;
			}
			else if(item.getDbtype().equalsIgnoreCase("hr"))
			{
				isHr=true;
			}
			
			if(ishba && isHr)
			{
				ishba=false;
				isBothHrAndHba=true;
			}
			
		}
		
		listType=type;		
		if(listType.equals("users"))
		{
			maxUsers=data.getMaxUsersAllowed(dbUser.getCompanyid());
			lstUsers=new ListModelList<WebusersModel>(data.getUsersList(dbUser.getCompanyid()));
			if(lstUsers.size()>0)			
				selectedUser=lstUsers.get(0);
			
			maxUsersAllowed="Total users " + lstUsers.size() + " . Max. users allowed to create is " + maxUsers;
			
			CompanyDBModel objDB =data.getCompanyDataBase(dbUser.getCompanyid());
			
			
			if(!ishba)
			{
				lstSuperVisors=data.getSupervisorList(objDB);
				if(lstSuperVisors!=null)
				{
					selectedSuperVisor=lstSuperVisors.get(0);
				}
				lstCompanies=data.getCompanyList(objDB);							
			}
			lstCompanyRoles=data.getCompanyRolesList(dbUser.getCompanyid());
			lstCompanyRoles.add(0, new CompanyRoleModel(0,0,"Select"));
			templstCompanyRoles=new ArrayList<CompanyRoleModel>();
			templstCompanyRoles.addAll(lstCompanyRoles);
			lstCompanyRoles.clear();
			for(CompanyRoleModel modelItr:templstCompanyRoles)
			{
				if(!modelItr.getRolename().equalsIgnoreCase("Admin"))
				{
					lstCompanyRoles.add(modelItr);
				}
			}
			
			
			if(isHr)
			{
				lstEmployees=data.getEmployeeList(objDB);
				if(lstEmployees.size()>0)
					selectedEmployee=lstEmployees.get(0);
			}
			
			if(selectedSuperVisor!=null)
			{
				if(selectedUser.getSupervisor()>0)
				{
					supervisorUser=true;
					setSelectedUser(selectedUser);
				}
				else if(selectedUser.getCompanyroleid()>0)
				{
					setSelectedUser(selectedUser);
				}	
			}
			
			if(ishba)
			{
				if(selectedUser.getCompanyroleid()>0)
				{
					setSelectedUser(selectedUser);
				}	
			}
			
			
			
		}
		
		else if(listType.equals("menu"))
		{
			lstUsers=new ListModelList<WebusersModel>(data.getUsersList(dbUser.getCompanyid()));
			WebusersModel obj=new WebusersModel();
			obj.setUserid(0);
			obj.setFirstname("Select");
			lstUsers.add(0,obj);
			selectedUser=lstUsers.get(0);
			lstMainUserMenu=data.getUserMenuList(dbUser.getUserid());
			lstCompanyFileMainMenu=data.getCompanyFileMenuList(dbUser.getCompanyid());
		//	chnages by iqbal for removing  admin 
			tmpLstCompanyFileMainMenu=new ArrayList<MenuModel>();
			tmpLstCompanyFileMainMenu.addAll(lstCompanyFileMainMenu);
			lstCompanyFileMainMenu.clear();
			for(MenuModel modelItr:tmpLstCompanyFileMainMenu)
			{
				//if(!modelItr.getTitle().equalsIgnoreCase("Admin"))
				{
					lstCompanyFileMainMenu.add(modelItr);
				}
			}
			lstCompanyRoles=data.getCompanyRolesList(dbUser.getCompanyid());
			lstCompanyRoles.add(0, new CompanyRoleModel(0,0,"Select"));
			selectedCompanyRole=lstCompanyRoles.get(0);
		}
		
		else if(listType.equals("company"))
		{
			lstUsers=new ListModelList<WebusersModel>(data.getUsersList(dbUser.getCompanyid()));
			WebusersModel obj=new WebusersModel();
			obj.setUserid(0);
			obj.setFirstname("Select");
			lstUsers.add(0,obj);
			selectedUser=lstUsers.get(0);
			CompanyDBModel objDB =data.getCompanyDataBase(dbUser.getCompanyid());		
			if(!ishba)
			{
			lstCompanies=data.getCompanyList(objDB);
			}
		
		}
		
		else if(listType.equals("role"))
		{
			lstRoles=new ListModelList<CompanyRoleModel>(data.getCompanyRolesList(dbUser.getCompanyid()));
		}
		
					
	   }
		catch (Exception ex)
		{
		logger.error("error at UsersViewModel>>Init>> " +ex.getMessage(),ex);
		}			
	}
	
	public UsersViewModel()	
	{
		try
		{			
			
		}		
		catch (Exception ex)
		{
		logger.error("error at UsersViewModel>>Init>> "+ex.getMessage());
		}		
	}
	
	@Command
	@NotifyChange("lstRoles") 
	public void addNewRoleCommand()
	{	
		for (CompanyRoleModel item : lstRoles)
		{
			if(item.getCompanyroleid()==0)
			{
				Messagebox.show("Please save the added role before add New one","Roles", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		CompanyRoleModel obj=new CompanyRoleModel();	
		obj.setCompanyid(dbUser.getCompanyid());
		obj.setRolename("");
		lstRoles.add(obj);	
	}
	
	
	@Command
	@NotifyChange({"supervisorUser","employeeUser"}) 
	public void unCheck()
	{	
		supervisorUser=false;
		employeeUser=false;
	}
	
	
	
	
	@Command
	@NotifyChange("lstRoles") 
	public void deleteRoleCommand(@BindingParam("row")final CompanyRoleModel row)
	{
		//check if roles used
		List<Integer> lstRoleUsed=data.getCompanyRolesUsed();
		if(lstRoleUsed.contains(row.getCompanyroleid()))
		{
			Messagebox.show("You can't delete this role, already used !! ","Roles", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		Messagebox.show("Are you sure to delete this role ?","Delete Role",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
               {
					 if(row.getCompanyroleid()>0)
					  data.deleteCompanyRole(row.getCompanyroleid());							
					  lstRoles.remove(row);										
               }
			 }
		 });		
	}
	
	@Command
	@NotifyChange("lstRoles") 
	public void saveRoleCommand(@BindingParam("row")final CompanyRoleModel row)
	{
		if(row.getRolename().equals(""))
		{
			Messagebox.show("Please enter role name","Roles", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		for (CompanyRoleModel item : lstRoles)
		{
			if(item.getCompanyroleid()!=row.getCompanyroleid())
			{
				if(item.getRolename().equals(row.getRolename()))
				{
					Messagebox.show("Role Name already exists !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		data.saveCompanyRole(row);
		Messagebox.show("Data Saved..","Roles", Messagebox.OK , Messagebox.EXCLAMATION);
		lstRoles=new ListModelList<CompanyRoleModel>(data.getCompanyRolesList(dbUser.getCompanyid()));
		
	}
	
	
	@Command
	@NotifyChange({"selectedUser","selectedSuperVisor","selectedEmployee","supervisorUser","employeeUser"}) 
	public void addNewUserCommand()
	{	
		if(lstUsers.size()>=maxUsers)
		{
			Messagebox.show("You can't add more users, please contact Explorer Company  !!","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		for (WebusersModel item : lstUsers)
		{
			if(item.getUserid()==0)
			{
				Messagebox.show("Please save the added user before add New one !!","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		selectedUser=new WebusersModel();
		selectedUser.setCompanyid(dbUser.getCompanyid());
		selectedUser.setSupervisor(0);
		selectedUser.setUseremail("");
		selectedUser.setUsermobile("");
		selectedUser.setCompanyroleid(0);
		selectedUser.setAdminuser("0");
		lstUsers.add(selectedUser);
		lstUsers.addToSelection(selectedUser);	
		if(lstSuperVisors!=null)
		selectedSuperVisor=lstSuperVisors.get(0);
		if(lstEmployees!=null)
		selectedEmployee=lstEmployees.get(0);
		unCheck();
	}
	@Command 
	@NotifyChange({"selectedUser","lstUsers","maxUsersAllowed"})
	public void updateUser()
	{
		if(selectedCompanyRole.getCompanyroleid()==0)
		{
			Messagebox.show("Please select a role to this user !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		for (WebusersModel item : lstUsers)
		{
			if(item.getUserid()!=selectedUser.getUserid() && item.getUserid()>0 && selectedUser.getUserid()>0)
			{
				if(item.getUsername().equals(selectedUser.getUsername()) || item.getFirstname().equals(selectedUser.getFirstname()))
				{
					Messagebox.show("User already exists !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		if(employeeUser)
		{
			if(selectedEmployee.getEmployeeKey()==0)
			{
				Messagebox.show("Please select employee!!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			else
			{
				selectedUser.setEmployeeKey(selectedEmployee.getEmployeeKey());
			}
		}
		else
		{
			selectedUser.setEmployeeKey(0);
		}
		
		if(supervisorUser)
		{
			selectedUser.setSupervisor(selectedSuperVisor.getEmployeeKey());
			selectedUser.setFirstname(selectedSuperVisor.getFullName());
		}
		else
		{
		  selectedUser.setSupervisor(0);
		}
		
		if(selectedUser.getAdminuser().equals("1"))
		{
			selectedUser.setFirstname("admin");
			selectedUser.setUsername("admin");
		}
		
		selectedUser.setCompanyroleid(selectedCompanyRole.getCompanyroleid());
		int userId=data.saveWebUser(selectedUser);
		if(userId>0)
		{
		selectedUser.setUserid(userId);
		CompanyDBModel objDB =data.getCompanyDataBase(dbUser.getCompanyid());
		if(!ishba)
		{
		data.addUserCompanyAccess(selectedUser.getUserid(), lstCompanies,objDB,selectedUser.getUsername());
		}
		Clients.showNotification("User Saved..");
		lstUsers=new ListModelList<WebusersModel>(data.getUsersList(dbUser.getCompanyid()));
		maxUsersAllowed="Total users " + lstUsers.size() + " . Max. users allowed to create is " + maxUsers;	
		}
		else
		{
			Messagebox.show("Error in saving user !!.","Setup", Messagebox.OK , Messagebox.ERROR);
		}
	}
	
	@Command 
	@NotifyChange("selectedUser")
	public void reloadUser()
	{
		//do nothing, the selectedUser will reload by notify change
	}
	
	@Command 
	@NotifyChange({"selectedUser","lstUsers"}) //use postNotifyChange() to notify dynamically
	public void deleteUser(@BindingParam("todo") WebusersModel todo)
	{	
		if(todo.getAdminuser().equals("1"))
		{
			Messagebox.show("You can't dis active account admin !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(todo.getIsactive())
		{
			data.updateUserStatus(todo.getUserid(), 0);
			todo.setIsactive(false);
		}
		else
		{
			data.updateUserStatus(todo.getUserid(), 1);
			todo.setIsactive(true);
		}
		
		/* Messagebox.show("Are you sure to delete this user ?","Delete User",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
                 {
					 data.deleteWebUser(todo);
						//update the model, by using ListModelList, you don't need to notify todoListModel change
					 lstUsers.remove(todo);
						
						if(todo.equals(selectedUser)){
							//refresh selected todo view
							selectedUser = null;
							//for the case that notification is decided dynamically
							//BindUtils.postNotifyChange(null, null, this, "selectedUser");
						}
                 }
			 }
		 });*/
		
		
	}
	
	
	//Menu
	@Command 
	@NotifyChange("lstMainUserMenu")
	public void viewMenuCommand()
	{
        List<MenuModel> lstUserMenu=data.getUserMenuList(selectedUser.getUserid());
        List<Integer> lstMenuId=new ArrayList<Integer>();
        
		for (MenuModel item : lstUserMenu) 
		{
			lstMenuId.add(item.getMenuid());
		}
		
		for (MenuModel item : lstMainUserMenu) 
		{
			if(lstMenuId.contains(item.getMenuid()))
				item.setCanView(true);
			else
			item.setCanView(false);	
		}
	}
	
	@Command 
	@NotifyChange("lstMainUserMenu")
	public void saveMenuCommand()
	{
		data.addUserMenuAccess(selectedUser.getUserid(), lstMainUserMenu);
		Clients.showNotification("User access saved !! ");
	}
	
	
	@Command 
	@NotifyChange({"lstCompanies","lstdefaultCompanies"})
	public void viewCompanyCommand()
	{
		 List<Integer> lstCompId=new ArrayList<Integer>();
		 lstCompId= data.getUserCompanyList(selectedUser.getUserid());
		 
		 for (CompanyModel item : lstCompanies) 
		 {
			if(lstCompId.contains(item.getCompKey()))
				item.setCanView(true);
			else
				item.setCanView(false);
		 }
		 
		 lstdefaultCompanies=new ArrayList<CompanyModel>();
		 for (CompanyModel item : lstCompanies) 
		 {
			 if(lstCompId.contains(item.getCompKey()) || item.getCompKey()==0)
			lstdefaultCompanies.add(item);
		 }
		 
	}
	
	@Command 
	@NotifyChange("lstCompanies")
	public void saveCompanyCommand()
	{
		CompanyDBModel objDB =data.getCompanyDataBase(dbUser.getCompanyid());	
		data.addUserCompanyAccess(selectedUser.getUserid(), lstCompanies,objDB,selectedUser.getUsername());
		Clients.showNotification("User access saved !! ");
	}

	public ListModelList<WebusersModel> getLstUsers() {
		return lstUsers;
	}

	public void setLstUsers(ListModelList<WebusersModel> lstUsers) {
		this.lstUsers = lstUsers;
	}

	public WebusersModel getSelectedUser() {
		return selectedUser;
	}

	@NotifyChange({"selectedSuperVisor","supervisorUser","selectedUser","lstCompanies","lstMainUserMenu","selectedCompanyRole","employeeUser","selectedEmployee","lstdefaultCompanies","selectedDeafultCompanies","lstCompanyRoles"})
	public void setSelectedUser(WebusersModel selectedUser) 
	{
		this.selectedUser = selectedUser;
		if(listType.equals("users"))
		{
		if(!ishba)
		{
		supervisorUser=false;
		employeeUser=false;
		selectedSuperVisor=lstSuperVisors.get(0);
		selectedEmployee=lstEmployees.get(0);
		 if(selectedUser.getSupervisor()>0)
		 {
			for (EmployeeModel item : lstSuperVisors) 
			{
				if(item.getEmployeeKey()==selectedUser.getSupervisor())
				{
					selectedSuperVisor=item;
					supervisorUser=true;
					break;
				}
			}
		 }
		 
		 if(selectedUser.getEmployeeKey()>0)
		 {
			 for (EmployeeModel item : lstEmployees) 
				{
					if(item.getEmployeeKey()==selectedUser.getEmployeeKey())
					{
						selectedEmployee=item;
						employeeUser=true;
						break;
					}
				}
		 }
		
		 
		}
		
		
		
		  List<Integer> lstCompId=new ArrayList<Integer>();
		  lstCompId= data.getUserCompanyList(selectedUser.getUserid());
		  if(!ishba)
			{
			  for (CompanyModel item : lstCompanies) 
			  {
				  if(lstCompId.contains(item.getCompKey()))
					  item.setCanView(true);
				  else
					  item.setCanView(false);
			  }
			}
		 	//Roles
		  if(selectedUser.getRoleName()!=null && selectedUser.getRoleName().equalsIgnoreCase("Admin"))
		  {
			  lstCompanyRoles=data.getCompanyRolesList(dbUser.getCompanyid());
		  }
		  else
		  {
			  lstCompanyRoles=data.getCompanyRolesList(dbUser.getCompanyid());
				lstCompanyRoles.add(0, new CompanyRoleModel(0,0,"Select"));
				templstCompanyRoles=new ArrayList<CompanyRoleModel>();
				templstCompanyRoles.addAll(lstCompanyRoles);
				lstCompanyRoles.clear();
				for(CompanyRoleModel modelItr:templstCompanyRoles)
				{
					if(!modelItr.getRolename().equalsIgnoreCase("Admin"))
					{
						lstCompanyRoles.add(modelItr);
					}
				}
		  }
			
		 	selectedCompanyRole=lstCompanyRoles.get(0);		 	
		 	if(selectedUser.getCompanyroleid()>0)
			{
				for (CompanyRoleModel item : lstCompanyRoles) 
				{
					if(item.getCompanyroleid()==selectedUser.getCompanyroleid())
						selectedCompanyRole=item;
				}
			}
			
		 
	   }
		
		if(listType.equals("company"))
		{
			  List<Integer> lstCompId=new ArrayList<Integer>();
			  lstCompId= data.getUserCompanyList(selectedUser.getUserid());
			 
			 for (CompanyModel item : lstCompanies) 
			 {
				if(lstCompId.contains(item.getCompKey()))
					item.setCanView(true);
				else
					item.setCanView(false);
			 }
			 
			 lstdefaultCompanies=new ArrayList<CompanyModel>();
			 for (CompanyModel item : lstCompanies) 
			 {
				if(lstCompId.contains(item.getCompKey()) || item.getCompKey()==0)
				lstdefaultCompanies.add(item);
			 }
			 selectedDeafultCompanies=null;
				int defaultCompanyId=dataHr.getDefaultCompanyID(selectedUser.getUserid());
				for (CompanyModel item : lstdefaultCompanies) 
				{
					if(item.getCompKey()==defaultCompanyId)
					selectedDeafultCompanies=item;
				}
		}
		
		if(listType.equals("menu"))
		{
			List<MenuModel> lstUserMenu=data.getUserMenuList(selectedUser.getUserid());
	        List<Integer> lstMenuId=new ArrayList<Integer>();
	        
			for (MenuModel item : lstUserMenu) 
			{
				lstMenuId.add(item.getMenuid());
			}
			
			for (MenuModel item : lstMainUserMenu) 
			{
				if(lstMenuId.contains(item.getMenuid()))
					item.setCanView(true);
				else
				item.setCanView(false);	
			}
		}
	}

	@Command 
	@NotifyChange({"lstSubMenuGroup","checkAllAdd","checkAllDelete","checkAllExport","checkAllModify","checkAllPrint","checkAllView"})
	public void getCompanySubMenu(@BindingParam("pagename")  MenuModel item)
	{
		if(selectedCompanyRole.getCompanyroleid()==0)
		{
			Messagebox.show("Please select role first !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		checkAllAdd=false;
		checkAllDelete=false;
		checkAllExport=false;
		checkAllModify=false;
		checkAllPrint=false;
		checkAllView=false;
		
		lstSubCompanyFileMainMenu=data.getSubMenuList(item.getMenuid());
		parentId=item.getMenuid();
		
		if(selectedCompanyRole.getCompanyroleid()>0)
		{
			List<MenuModel> lstRolesCred=data.getRoleCredentials(selectedCompanyRole.getCompanyroleid(),parentId);
			
			for (MenuModel mainMenu : lstSubCompanyFileMainMenu)
			{
				for (MenuModel children : mainMenu.getChildren()) 
				{
					for (MenuModel role : lstRolesCred)
					{
						if(role.getMenuid()==children.getMenuid())
						{
							children.setCanView(role.isCanView());
							children.setCanModify(role.isCanModify());
							children.setCanDelete(role.isCanDelete());
							children.setCanAdd(role.isCanAdd());
							children.setCanExport(role.isCanExport());
							children.setCanPrint(role.isCanPrint());
							children.setCanAllowToSeeAccountingTrasaction(role.isCanAllowToSeeAccountingTrasaction());
							children.setAllowToActive(role.isAllowToActive());
							children.setAllowToInActive(role.isAllowToInActive());
							children.setCanApprove(role.isCanApprove());
							children.setCanChangeStatus(role.isCanChangeStatus());
							children.setCanConvert(role.isCanConvert());							
							if(parentId==4)//if Accounting
							{
								onlyAccountingAccess=true;
							}
							else
							{
								onlyAccountingAccess=false;
							}
							break;
						}
					}					
				}			
			}
		}
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);	
	}

	@Command 
	@NotifyChange({"lstSubMenuGroup"})
	public void checkAllAddCommand(@BindingParam("row")  MenuModel row ,@BindingParam("chk") boolean chk,@BindingParam("type") String type)
	{
		//Messagebox.show(item.getTitle());
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			if(item.getTitle().equals(row.getTitle()))
			{
			for (MenuModel children : item.getChildren()) 
			{
				if(type.equals("add"))
				children.setCanAdd(chk);
				else if(type.equals("edit"))
					children.setCanModify(chk);
				else if(type.equals("view"))
					children.setCanView(chk);
				else if(type.equals("delete"))
					children.setCanDelete(chk);
				else if(type.equals("export"))
					children.setCanExport(chk);
				else if(type.equals("print"))
					children.setCanPrint(chk);
				else if(type.equals("otherTrasction"))
					children.setCanAllowToSeeAccountingTrasaction(chk);
				else if(type.equals("allowActive"))
					children.setAllowToActive(chk);
				else if(type.equals("allowInActive"))
					children.setAllowToInActive(chk);
				else if(type.equals("approve"))
					children.setCanApprove(chk);
				else if(type.equals("changeStatus"))
					children.setCanChangeStatus(chk);
				else if(type.equals("convert"))
					children.setCanConvert(chk);								
				
			}
		  }
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);
	}
	
	@Command 
	@NotifyChange("lstMainUserMenu")
	public void saveUserMenuCommand()
	{
		if(selectedCompanyRole.getCompanyroleid()==0)
		{
			Messagebox.show("Please select a role !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		data.deleteUserRoleCredential(selectedCompanyRole.getCompanyroleid(),parentId);
		boolean isAccess=false;
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				if(children.isCanView() || children.isCanModify() || children.isCanDelete() || children.isCanAdd() || children.isCanExport() || children.isCanPrint())
					isAccess=true;
			}
			if(isAccess)
			{
				isAccess=false;
				//data.addParentUserMenuAccess(selectedUser.getUserid(), item);
				//data.addFullUserMenuAccess(selectedUser.getUserid(), item.getChildren());
				if(item.isAddReportMenu())
				{
					data.addMainMenuToRolesCredentials(dbUser.getUserid(), selectedCompanyRole.getCompanyroleid(), item,parentId);
				}
				else
				{
				data.addMainMenuToRolesCredentials(dbUser.getUserid(), selectedCompanyRole.getCompanyroleid(), item,parentId);
				data.addFullUserRolesCredentials(dbUser.getUserid(), selectedCompanyRole.getCompanyroleid(), item.getChildren(),parentId);
				}
			}
			 
		}
		
		Clients.showNotification("User access saved !! ");
	}
	
	
	public List<MenuModel> getLstMainUserMenu() {
		return lstMainUserMenu;
	}


	public void setLstMainUserMenu(List<MenuModel> lstMainUserMenu) {
		this.lstMainUserMenu = lstMainUserMenu;
	}

	public List<CompanyModel> getLstCompanies() {
		return lstCompanies;
	}

	public void setLstCompanies(List<CompanyModel> lstCompanies) {
		this.lstCompanies = lstCompanies;
	}

	public List<EmployeeModel> getLstSuperVisors() {
		return lstSuperVisors;
	}

	public void setLstSuperVisors(List<EmployeeModel> lstSuperVisors) {
		this.lstSuperVisors = lstSuperVisors;
	}

	public EmployeeModel getSelectedSuperVisor() {
		return selectedSuperVisor;
	}

	@NotifyChange("selectedUser") 
	public void setSelectedSuperVisor(EmployeeModel selectedSuperVisor) 
	{
		this.selectedSuperVisor = selectedSuperVisor;
		if(selectedSuperVisor.getEmployeeKey()>0)
		selectedUser.setFirstname(selectedSuperVisor.getFullName());
	}

	public boolean isSupervisorUser() {
		return supervisorUser;
	}

	@NotifyChange({"employeeUser"})
	public void setSupervisorUser(boolean supervisorUser) {
		this.supervisorUser = supervisorUser;
		if(supervisorUser=true)
		{
			employeeUser=false;
		}
		else
		{
			employeeUser=true;
		}
	}

	public List<MenuModel> getLstCompanyFileMainMenu() {
		return lstCompanyFileMainMenu;
	}

	public void setLstCompanyFileMainMenu(List<MenuModel> lstCompanyFileMainMenu) {
		this.lstCompanyFileMainMenu = lstCompanyFileMainMenu;
	}

	public List<MenuModel> getLstSubCompanyFileMainMenu() {
		return lstSubCompanyFileMainMenu;
	}

	public void setLstSubCompanyFileMainMenu(
			List<MenuModel> lstSubCompanyFileMainMenu) {
		this.lstSubCompanyFileMainMenu = lstSubCompanyFileMainMenu;
	}

	public MenuGroupAdapter getLstSubMenuGroup() {
		return lstSubMenuGroup;
	}

	public void setLstSubMenuGroup(MenuGroupAdapter lstSubMenuGroup) {
		this.lstSubMenuGroup = lstSubMenuGroup;
	}

	public boolean isCheckAllView() {
		return checkAllView;
	}

	@NotifyChange({"lstSubMenuGroup"})
	public void setCheckAllView(boolean checkAllView) 
	{
		this.checkAllView = checkAllView;
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				children.setCanView(checkAllView);
			}			
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);
	}

	public boolean isCheckAllModify() {
		return checkAllModify;
	}
	
	@NotifyChange({"lstSubMenuGroup"})
	public void setCheckAllModify(boolean checkAllModify) 
	{
		this.checkAllModify = checkAllModify;
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				children.setCanModify(checkAllModify);
			}			
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);
	}

	public boolean isCheckAllDelete() {
		return checkAllDelete;
	}

	@NotifyChange({"lstSubMenuGroup"})
	public void setCheckAllDelete(boolean checkAllDelete) 
	{
		this.checkAllDelete = checkAllDelete;
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				children.setCanDelete(checkAllDelete);
			}			
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);
	}

	public ListModelList<CompanyRoleModel> getLstRoles() {
		return lstRoles;
	}

	public void setLstRoles(ListModelList<CompanyRoleModel> lstRoles) {
		this.lstRoles = lstRoles;
	}

	public CompanyRoleModel getSelectedCompanyRole() {
		return selectedCompanyRole;
	}
	
	@NotifyChange({"lstSubMenuGroup"})
	public void setSelectedCompanyRole(CompanyRoleModel selectedCompanyRole) 
	{
		lstSubCompanyFileMainMenu=new ArrayList<MenuModel>();
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);	
		this.selectedCompanyRole = selectedCompanyRole;
	}

	public List<CompanyRoleModel> getLstCompanyRoles() {
		return lstCompanyRoles;
	}

	public void setLstCompanyRoles(List<CompanyRoleModel> lstCompanyRoles) {
		this.lstCompanyRoles = lstCompanyRoles;
	}

	public String getMaxUsersAllowed() {
		return maxUsersAllowed;
	}

	public void setMaxUsersAllowed(String maxUsersAllowed) {
		this.maxUsersAllowed = maxUsersAllowed;
	}

	public boolean isCheckAllAdd() {
		return checkAllAdd;
	}

	//@NotifyChange({"lstSubMenuGroup"})
	public void setCheckAllAdd(boolean checkAllAdd) 
	{
		this.checkAllAdd = checkAllAdd;
		/*for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				children.setCanAdd(checkAllAdd);
			}			
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);*/
	}

	public boolean isCheckAllExport() {
		return checkAllExport;
	}

	@NotifyChange({"lstSubMenuGroup"})
	public void setCheckAllExport(boolean checkAllExport) 
	{
		this.checkAllExport = checkAllExport;
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				children.setCanExport(checkAllExport);
			}			
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);
	}

	public boolean isCheckAllPrint() {
		return checkAllPrint;
	}

	@NotifyChange({"lstSubMenuGroup"})
	public void setCheckAllPrint(boolean checkAllPrint) 
	{
		this.checkAllPrint = checkAllPrint;
		for (MenuModel item : lstSubCompanyFileMainMenu)
		{
			for (MenuModel children : item.getChildren()) 
			{
				children.setCanPrint(checkAllPrint);
			}			
		}
		
		lstSubMenuGroup=new MenuGroupAdapter(lstSubCompanyFileMainMenu, new MenuComparator(), false);
	}

	public ListModelList<CompanyDBModel> getCompanyAccessList() {
		return companyAccessList;
	}

	public void setCompanyAccessList(ListModelList<CompanyDBModel> companyAccessList) {
		this.companyAccessList = companyAccessList;
	}

	public boolean isIshba() {
		return ishba;
	}

	public void setIshba(boolean ishba) {
		this.ishba = ishba;
	}

	public boolean isHr() {
		return isHr;
	}

	public void setHr(boolean isHr) {
		this.isHr = isHr;
	}

	public boolean isBothHrAndHba() {
		return isBothHrAndHba;
	}

	public void setBothHrAndHba(boolean isBothHrAndHba) {
		this.isBothHrAndHba = isBothHrAndHba;
	}

	public List<EmployeeModel> getLstEmployees() {
		return lstEmployees;
	}

	public void setLstEmployees(List<EmployeeModel> lstEmployees) {
		this.lstEmployees = lstEmployees;
	}

	public EmployeeModel getSelectedEmployee() {
		return selectedEmployee;
	}

	@NotifyChange({"selectedUser","selectedDeafultCompanies"}) 
	public void setSelectedEmployee(EmployeeModel selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
		if(selectedEmployee.getEmployeeKey()>0)
			selectedUser.setFirstname(selectedEmployee.getFullName());
	}

	public boolean isEmployeeUser() {
		return employeeUser;
	}

	@NotifyChange("supervisorUser")
	public void setEmployeeUser(boolean employeeUser) {
		this.employeeUser = employeeUser;
		if(employeeUser==true)
		{
			supervisorUser=false;
		}
		else
		{
			supervisorUser=true;
		}
	}

	public List<CompanyModel> getLstdefaultCompanies() {
		return lstdefaultCompanies;
	}

	public void setLstdefaultCompanies(List<CompanyModel> lstdefaultCompanies) {
		this.lstdefaultCompanies = lstdefaultCompanies;
	}

	public CompanyModel getSelectedDeafultCompanies() {
		return selectedDeafultCompanies;
	}

	public void setSelectedDeafultCompanies(CompanyModel selectedDeafultCompanies) {
		this.selectedDeafultCompanies = selectedDeafultCompanies;
	}

	
	@Command 
	@NotifyChange("lstdefaultCompanies")
	public void saveDefaultCompanyCommand()
	{
		
		if(lstdefaultCompanies.size()==0)
		{
			Clients.showNotification("There are no users ");
			return;
		}
		
		if(selectedDeafultCompanies==null)
		{
			Clients.showNotification("Please select the Default Company ");
			return;
		}
		CompanyDBModel objDB =data.getCompanyDataBase(dbUser.getCompanyid());	
		data.updateDefaultComapny(selectedUser.getUserid(), selectedDeafultCompanies,objDB);
		Clients.showNotification("Deafult company for user is saved !! ");
	}

	public List<CompanyRoleModel> getTemplstCompanyRoles() {
		return templstCompanyRoles;
	}

	public void setTemplstCompanyRoles(List<CompanyRoleModel> templstCompanyRoles) {
		this.templstCompanyRoles = templstCompanyRoles;
	}

	public List<MenuModel> getTmpLstCompanyFileMainMenu() {
		return tmpLstCompanyFileMainMenu;
	}

	public void setTmpLstCompanyFileMainMenu(
			List<MenuModel> tmpLstCompanyFileMainMenu) {
		this.tmpLstCompanyFileMainMenu = tmpLstCompanyFileMainMenu;
	}

	/**
	 * @return the onlyAccountingAccess
	 */
	public boolean isOnlyAccountingAccess() {
		return onlyAccountingAccess;
	}

	/**
	 * @param onlyAccountingAccess the onlyAccountingAccess to set
	 */
	public void setOnlyAccountingAccess(boolean onlyAccountingAccess) {
		this.onlyAccountingAccess = onlyAccountingAccess;
	}
	
	
	
}
