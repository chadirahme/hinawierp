package layout;


import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.http.HttpSessionListener;
//import org.zkoss.zkmax.zul.Scrollview;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;


import java.util.ArrayList;
 
public class MenuViewModel {

	
	int x55;
	
	MenuData mData=new MenuData();
	private List<MenuModel> lstMainMenu;
	private List<MenuModel> lstSubMenu;
	private List<MenuModel> lstLastMenu;
	private String selectedMenuid;
	WebusersModel dbUser=null;
    int companyroleid;
	
	public MenuViewModel()
	{
		//mData.getMenuData();
		
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser==null)
		{
			dbUser=new WebusersModel();
			dbUser.setUserid(1);
			dbUser.setFirstname("demo");
 			dbUser.setRoleid(1);
 			companyroleid=1;
 			Sessions.getCurrent().setAttribute("Authentication", dbUser);
 			
 			lstMainMenu=getMenus();
			if(lstMainMenu.size()>0)
			lstMainMenu.get(0).setSclassName("menuitem");
			//Executions.sendRedirect("index.zul");
		}
		else
		{
		//List<MenuModel> lstMenu=mData.getMenuList();
		//HttpSessionListener s=new HttpSessionListener();	
			lstMainMenu=getMenus();
			if(lstMainMenu.size()>0)
			lstMainMenu.get(0).setSclassName("menuitem");
		}				
	}
	@Command
	public void logout()
	{
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("Authentication");
		Executions.sendRedirect("index.zul");
	}
	public List<MenuModel> getMenus()
	{
	     if(dbUser==null) Executions.sendRedirect("index.zul");
	     int userid=0;
	     if(dbUser!=null)
	    	 userid=dbUser.getUserid();
	    	 
		return mData.getMenuList(userid,false);
	}
	
	
	
	@Command
	@NotifyChange({"lstSubMenu","lstLastMenu","lstMainMenu"})
	public void fillSubMenu(@BindingParam("item") MenuModel item)
	{
		//lstSubMenu=new ArrayList<MenuModel>();
		//MenuModel obj=new MenuModel();
		//obj.setTitle("new menu");
		//obj.setChildren(new ArrayList<MenuModel>());
		//lstSubMenu.add(obj);
		
		//Messagebox.show(String.valueOf(item.getMenuid()));
				
		for (MenuModel obj : lstMainMenu) 
		{
		obj.setSclassName("defaultmenuitem");	
		}
		item.setSclassName("menuitem");
			
		lstSubMenu=mData.getSubMenuList(item.getMenuid(),1,companyroleid);
		lstLastMenu=new ArrayList<MenuModel>();
		//Messagebox.show(item.getTitle());
		//BindUtils.postNotifyChange(null, null, this, "*");		
	}
	
	@Command
	@NotifyChange("lstMainMenu")
	public void arrangeMenu(@BindingParam("item") MenuModel item)
	{
		
		//Messagebox.show(item.getHref());
		
		if(item.getMenuid()==-1)
		{
			//Messagebox.show("Arrange");arrangemenu.zul
			 Window window = (Window)Executions.createComponents("arrangemenu.zul", null, null);
		     window.doModal();			
		}
		
		Borderlayout bl = (Borderlayout) Path.getComponent("/mainlayout");
		Center center = bl.getCenter();
		center.getChildren().clear();
		
		// if(item.getMenuid()==1)
		//{
			//bl = (Borderlayout) Path.getComponent("/mainlayout");
			//center = bl.getCenter();
			//center.getChildren().clear();		
			if(!item.getHref().equals(""))
			Executions.createComponents(item.getHref(), center, null);
		//}	
			
			for (MenuModel obj : lstMainMenu) 
			{
			obj.setSclassName("defaultmenuitem");	
			}
			item.setSclassName("menuitem");
	}
	
	@Command
	@NotifyChange({"lstLastMenu"})
	public void fillLastMenu(@BindingParam("item") MenuModel item)
	{
		/*
		for (MenuModel obj : lstSubMenu) 
		{
		obj.setSclassName("defaultmenuitem");	
		}
		item.setSclassName("menuitem");
		*/
		lstLastMenu=mData.getSubMenuList(item.getMenuid(),2,companyroleid);
	}
	
	@Command
	public void getSubMenu(@BindingParam("item") MenuModel item)
	{
		Messagebox.show(item.getHref());
	}
	@Command
	@NotifyChange({"lstLastMenu","lstSubMenu"})
	public void getValue(@BindingParam("item") MenuModel item)
	{
		//lstLastMenu=mData.getSubMenuList(item.getMenuid());
		//Messagebox.show(String.valueOf(item.getLevel()));
		
		if(item.getLevel()==2)
		{
		for (MenuModel obj : lstLastMenu) 
		{
		obj.setSclassName("defaultmenuitem");	
		}
		}
		
		if(item.getLevel()==1)
		{
		for (MenuModel obj : lstSubMenu) 
		{
		obj.setSclassName("defaultmenuitem");	
		}
		}
		
		
		item.setSclassName("menuitem");
		
		//Messagebox.show(item.getHref());
		
		
		Borderlayout bl = (Borderlayout) Path.getComponent("/mainlayout");
		Center center = bl.getCenter();
		center.getChildren().clear();
				
			if(!item.getHref().equals(""))
			Executions.createComponents(item.getHref(), center, null);
			
		
		//	Executions.sendRedirect(item.getHref());
			//Executions.getCurrent().sendRedirect(item.getHref());
/*
		Vlayout vl=(Vlayout)Path.getComponent("/mainlayout");
		Scrollview s=(Scrollview)vl.getFellow("scvCenter");
		s.getChildren().clear();
		if(!item.getHref().equals(""))
		Executions.createComponents(item.getHref(), s, null);
		*/
	}

	
	
	public List<MenuModel> getLstSubMenu() {
		
		return lstSubMenu;
	}

	public void setLstSubMenu(List<MenuModel> lstSubMenu) {
		this.lstSubMenu = lstSubMenu;
	}

	public List<MenuModel> getLstLastMenu() {
		return lstLastMenu;
	}

	public void setLstLastMenu(List<MenuModel> lstLastMenu) {
		this.lstLastMenu = lstLastMenu;
	}

	public String getSelectedMenuid() {
		return selectedMenuid;
	}
	@NotifyChange("lstLastMenu")
	public void setSelectedMenuid(String selectedMenuid) {
		this.selectedMenuid = selectedMenuid;
		//lstLastMenu=mData.getSubMenuList(Integer.parseInt(selectedMenuid));
		Messagebox.show(this.selectedMenuid);
	}
	
	//Arrange Menu
	private List<MenuModel> lstArrangeMenu;
		
	public List<MenuModel> getLstArrangeMenu() 
	{	
		 if(dbUser==null) Executions.sendRedirect("index.zul");
	     int userid=0;
	     if(dbUser!=null)
	    	 userid=dbUser.getUserid();
	    	 
	     lstArrangeMenu= mData.getMenuList(userid,true);
		return lstArrangeMenu;
	}

	public void setLstArrangeMenu(List<MenuModel> lstArrangeMenu) {
		this.lstArrangeMenu = lstArrangeMenu;
	}

	
	@Command
	public void saveArrangeMenu()
	{
		Executions.sendRedirect("demo.zul");	
	}
	
	@Command
	@NotifyChange("lstArrangeMenu")
	public void moveExample(@BindingParam("base") MenuModel base,
	@BindingParam("item") MenuModel item) {
		//Messagebox.show(base.getTitle());
		//Messagebox.show(item.getTitle());
		mData.arrangeUserMenu(base.getMenuid(), item.getMenuid(),dbUser.getUserid());
		lstArrangeMenu= mData.getMenuList(dbUser.getUserid(),true);
	}
	
	@Command	
	public void menuClicked(@BindingParam("pagename") String pagename)
	{
		/*
		Borderlayout bl = (Borderlayout) Path.getComponent("/mainlayout");
		Center center = bl.getCenter();
		center.getChildren().clear();
				
		Executions.createComponents(pagename, center, null);
		//Executions.getCurrent().sendRedirect(pagename);
		//Messagebox.show(pagename);    	
		*/
		
		Vlayout vl=(Vlayout)Path.getComponent("/mainlayout");
		//Scrollview s=(Scrollview)vl.getFellow("scvCenter");
		//Executions.createComponents(pagename, s, null);
			
	}
	
	public List<MenuModel> getLstMainMenu() {
		return lstMainMenu;
	}

	public void setLstMainMenu(List<MenuModel> lstMainMenu) {
		this.lstMainMenu = lstMainMenu;
	}
	
	
}
