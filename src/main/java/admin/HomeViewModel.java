package admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.CompanyDBData;
import setup.users.WebusersModel;

public class HomeViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	WebusersModel dbUser=null;
	private ListModelList<CompanyDBModel> companyList;
	CompanyDBData cdata=new CompanyDBData();
	private List<CompanyDBModel> companyUserList;
	private CompanyDBModel selectedCompany;
	private ListModelList<WebusersModel> lstUsers;
	private WebusersModel selectedUser;
	private List<MenuModel> lstMainMenu;
	private boolean checkAllFile;
	private List<Integer> lstCompanyMainMenu;
	
	//mobile
	private List<CompanyDBModel> lstMobileComapny;
	private CompanyDBModel selectedMobileCompany;
	final MobileData mobileData=new MobileData();
	
	public HomeViewModel()
	{
		try
		{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Admin");
		if(dbUser==null)
		{
			Executions.sendRedirect("/login.zul");
		}
		else
		{
			companyList=new ListModelList<CompanyDBModel>( cdata.getCompanyInfoList());
			for(CompanyDBModel dbModel:companyList)//Temp code
			{
				if(dbModel.getMergedDatabse().equalsIgnoreCase("Yes"))
				{
					dbModel.setMergeChecked(true);
				}
				else
				{
					dbModel.setMergeChecked(false);
				}
			}
			companyUserList=cdata.getCompanyDBList();
			selectedCompany=companyUserList.get(0);			
			fillMobileCompany();
		}
		
		}
		catch (Exception ex)
		{
		logger.error("error at HomeViewModel>>Init>> "+ex.getMessage());
		}
	}
	
	private void fillMobileCompany()
	{
		lstMobileComapny=mobileData.getMobileCompanyList();
		if(lstMobileComapny.size()>0)
			selectedMobileCompany=lstMobileComapny.get(0);			
	}
	
	@Command	
	public void logout()
	{
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("Admin");
		sess.removeAttribute("Authentication");
		sess.removeAttribute("lstDemoMainMenu");
		//Executions.sendRedirect("/admin/login.zul");
		Executions.sendRedirect("/index.zul");
	}
	
	@Command	
	public void menuClicked(@BindingParam("pagename") String pageName)
	{
		Borderlayout bl = (Borderlayout) Path.getComponent("/mainlayout");
		Center center = bl.getCenter();
		center.getChildren().clear();	
		if(!pageName.equals(""))
		Executions.createComponents(pageName, center, null);		  	
	}
	//Company.zul
	@Command
	@NotifyChange("companyList") 
	public void addNewCommand()
	{	
		for (CompanyDBModel item : companyList)
		{
			if(item.getCompanyId()==0)
			{
				Messagebox.show("Please save the added company before add New one","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		CompanyDBModel obj=new CompanyDBModel();	
		obj.setCompanyName("");
		obj.setMobile("");
		obj.setEmail("");
		obj.setMaxNoUsers(10);//default 10 users
		companyList.add(0,obj);	
	}
	
	@Command
	@NotifyChange("companyList") 
	public void saveCommand(@BindingParam("row") CompanyDBModel row)
	{	
		if(row.getCompanyName().equals(""))
		{
			Messagebox.show("Please enter company name","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(row.isMergeChecked())//temporary code
		{
			row.setMergedDatabse("Yes");
		}
		else
		{
			row.setMergedDatabse("No");
		}
		
		for (CompanyDBModel item : companyList)
		{
			if(item.getCompanyId()!=row.getCompanyId())
			{
				if(item.getCompanyName().equals(row.getCompanyName()))
				{
					Messagebox.show("Company Name already exists !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		cdata.saveCompanyInfo(row);
		Messagebox.show("Data Saved..","Setup", Messagebox.OK , Messagebox.EXCLAMATION);		
		companyList=new ListModelList<CompanyDBModel>( cdata.getCompanyInfoList());
		for(CompanyDBModel dbModel:companyList)//Temp code
		{
			if(dbModel.getMergedDatabse().equalsIgnoreCase("Yes"))
			{
				dbModel.setMergeChecked(true);
			}
			else
			{
				dbModel.setMergeChecked(false);
			}
		}
	}
	
	@Command
	@NotifyChange("companyList") 
	public void deleteCommand(@BindingParam("row")final CompanyDBModel row)
	{
		Messagebox.show("Are you sure to delete this company ?","Delete company",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
                {
					 if(row.getCompanyId()>0)
					  cdata.deleteCompanyInfo(row);							
					  companyList.remove(row);										
                }
			 }
		 });				
	}
	
	@Command
	public void changePassword()
	{
		 try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("type","changePassword");
			   Executions.createComponents("/layout/changePassword.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HomeViewModel ----> changePassword", ex);			
			}
	}
	
	public ListModelList<CompanyDBModel> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(ListModelList<CompanyDBModel> companyList) {
		this.companyList = companyList;
	}
	public List<CompanyDBModel> getCompanyUserList() {
		return companyUserList;
	}
	public void setCompanyUserList(List<CompanyDBModel> companyUserList) {
		this.companyUserList = companyUserList;
	}
	public CompanyDBModel getSelectedCompany() {
		return selectedCompany;
	}
	@NotifyChange({"lstUsers","lstMainMenu"}) 
	public void setSelectedCompany(CompanyDBModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		lstUsers=new ListModelList<WebusersModel>(cdata.getUsersList(selectedCompany.getCompanyId()));
		
		lstCompanyMainMenu=cdata.getCompanyFile(selectedCompany.getCompanyId());
		lstMainMenu=cdata.getMainMenuList();
		for (MenuModel item : lstMainMenu)
		{
			if(lstCompanyMainMenu.contains(item.getMenuid()))
				item.setCanView(true);
		}
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
	public void setSelectedUser(WebusersModel selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	@Command
	@NotifyChange("selectedUser") 
	public void addNewUserCommand()
	{	
		if(selectedCompany.getCompanyId()==0)
		{
			Messagebox.show("Please select company before add New User !!","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
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
		selectedUser.setCompanyid(selectedCompany.getCompanyId());
		lstUsers.add(selectedUser);
		lstUsers.addToSelection(selectedUser);			
	}
	
	@Command 
	@NotifyChange("selectedUser")
	public void updateUser()
	{
		for (WebusersModel item : lstUsers)
		{
			if(item.getUserid()!=selectedUser.getUserid())
			{
				if(item.getUsername().equals(selectedUser.getUsername()) || item.getFirstname().equals(selectedUser.getFirstname()))
				{
					Messagebox.show("User already exists !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		cdata.saveWebUser(selectedUser);
		Clients.showNotification("User Saved..");
		lstUsers=new ListModelList<WebusersModel>(cdata.getUsersList(selectedCompany.getCompanyId()));
		//update the model, by using ListModelList, you don't need to notify todoListModel change
		//by reseting an item , it make listbox only refresh one item		
	}
	
	@Command 
	@NotifyChange("selectedUser")
	public void reloadUser()
	{
		//do nothing, the selectedUser will reload by notify change
	}
	
	@Command 
	@NotifyChange("selectedUser") //use postNotifyChange() to notify dynamically
	public void deleteUser(@BindingParam("todo") final WebusersModel todo)
	{		
		if(todo.getAdminuser().equals("1"))
		{
			Messagebox.show("You can't delete account admin !!.","Delete User", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		 Messagebox.show("Are you sure to delete this user ?","Delete User",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
                 {
					 cdata.deleteWebUser(todo);
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
		 });
		
		
	}
	
	//Company File
	public List<MenuModel> getLstMainMenu() {
		return lstMainMenu;
	}
	public void setLstMainMenu(List<MenuModel> lstMainMenu) {
		this.lstMainMenu = lstMainMenu;
	}
	
	@Command
	@NotifyChange("lstMainMenu") 
	public void saveCompanyFileCommand()
	{	
		
		if(selectedCompany.getCompanyId()==0)
		{
			Messagebox.show("Please select company !!","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		List<Integer> lstMenuIds=new ArrayList<Integer>();
		for (MenuModel item : lstMainMenu) 
		{
			if(item.isCanView())
				lstMenuIds.add(item.getMenuid());			
		}
		cdata.saveCompanyFile(selectedCompany.getCompanyId(), lstMenuIds);
		Clients.showNotification("Company access file Saved..");
		
	}
	
	@Command
	@NotifyChange("lstMobileComapny") 
	public void saveMobileCompanyCommand()
	{	
		
		if(selectedMobileCompany.getCompanyId()==0)
		{
			Messagebox.show("Please select company !!","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		mobileData.UpdateMobileCompany(selectedMobileCompany);
		Clients.showNotification("Company info is Saved..");		
	}
	
	
	
	public boolean isCheckAllFile() {
		return checkAllFile;
	}
	
	@NotifyChange("lstMainMenu") 
	public void setCheckAllFile(boolean checkAllFile) 
	{
		this.checkAllFile = checkAllFile;
		if(checkAllFile)
		{
			for (MenuModel item : lstMainMenu) 
			{
				item.setCanView(true);
			}
		}
		else
		{
			for (MenuModel item : lstMainMenu) 
			{
				item.setCanView(false);
			}
		}
	}
	public List<CompanyDBModel> getLstMobileComapny() {
		return lstMobileComapny;
	}
	public void setLstMobileComapny(List<CompanyDBModel> lstMobileComapny) {
		this.lstMobileComapny = lstMobileComapny;
	}
	public CompanyDBModel getSelectedMobileCompany() {
		return selectedMobileCompany;
	}
	public void setSelectedMobileCompany(CompanyDBModel selectedMobileCompany) {
		this.selectedMobileCompany = selectedMobileCompany;
	}
	
	
	
}
