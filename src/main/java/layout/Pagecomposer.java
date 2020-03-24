package layout;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Vlayout;

import setup.users.WebusersModel;

public class Pagecomposer extends SelectorComposer 
{
	private Logger logger = Logger.getLogger(this.getClass());
	@Wire
	Tabbox mainContentTabbox;
	
	@Wire
	Tabs contentTabs;
	
	@Wire
	Tabpanels contentTabpanels;
	
	
	 private String sessLang;
	 int companyroleid;
	 WebusersModel dbUser=null;
	 
	 public void doAfterCompose(Component comp) throws Exception 
	 {		    
		   super.doAfterCompose(comp);	
	 }
	 
	 public Pagecomposer()
	 {
		 	Session sess = Sessions.getCurrent();
			sessLang= (String) sess.getAttribute("language");
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				companyroleid=dbUser.getCompanyroleid();
			}
			
			if(sessLang==null)
			{
				sessLang="en";
				Sessions.getCurrent().setAttribute("language", sessLang);
			}
			language=sessLang;
	 }
	 
	MenuData mData=new MenuData();
	private List<MenuModel> lsthbaMenu;
	private List<MenuModel> lstfixedAssetMenu;
	private List<MenuModel> lsthrMenu;
	private List<MenuModel> lsttimesheetMenu;
	private List<MenuModel> lstRealEstateMenu;
	private List<MenuModel> lstGarageMenu;
	private List<MenuModel> lstVisitorsMenu;
	private List<MenuModel> lstSchoolMenu;
	private List<MenuModel> lstCRMMenu;
	private List<MenuModel> lstReportsMenu;
	private List<MenuModel> lstListsMenu;
	private List<MenuModel> lstPayRollMenu;
	private List<MenuModel> lstCompanyMenu;
	private String language;
	 	
	Label LastlblItem;
	
	@Command	
	public void menuClicked(@BindingParam("pagename")  MenuModel item ,@BindingParam("label") Label lblItem,@BindingParam("menuType") String menuType )
	{
		
		//item.setSclassName("selectedMenu");
		
		/*
		if(menuType!=null && menuType.equals("Timesheet"))
		{
			for (MenuModel defItem : lsttimesheetMenu) 
			{
				for (MenuModel subItem : defItem.getChildren()) {
					  if(subItem.getMenuid()==item.getMenuid())
					  {
						  logger.info(" >> " + subItem.getMenuid());
						  subItem.setSclassName("selectedMenu");						  
					  }
					   else
				 subItem.setSclassName("defaultMenu");	
					
				}
			 
			}
		}*/
		
		if(LastlblItem!=null)
		{
			LastlblItem.setSclass("defaultMenu");
		}
		if(lblItem!=null)
		{
		lblItem.setSclass("selectedMenu");
		LastlblItem=lblItem;
		}
		
	
		//Executions.getCurrent().sendRedirect(pagename);
		//Messagebox.show(pagename);  

		Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
		Center center = bl.getCenter();
					
		center.getChildren().clear();	
		if(!item.getHref().equals(""))
		Executions.createComponents(item.getHref(), center, null);
		
	}
	
	@Command	
	public void menuHRClicked(@BindingParam("pagename")  MenuModel item,@BindingParam("label") Label lblItem)
	{
		try
		{
		if(LastlblItem!=null)
		{
			LastlblItem.setSclass("defaultMenu");
		}
		if(lblItem!=null)
		{
		lblItem.setSclass("selectedMenu");
		LastlblItem=lblItem;
		}
		
		Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
		Center center = bl.getCenter();
		
		Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				
		Tabs contentTabs=(Tabs)tabbox.getFellow("contentTabs");
		Tabpanels contentTabpanels=(Tabpanels)tabbox.getFellow("contentTabpanels");
		
		for (Component oldTab : contentTabs.getChildren()) 
		{
			if(oldTab instanceof Tab)
			{
				if(language.equals("en"))
				{
				if( ((Tab)oldTab).getLabel().equals(item.getTitle()))
				{
					((Tab) oldTab).setSelected(true);
					return;
				}
					
				}
				else
				{
				if( ((Tab)oldTab).getLabel().equals(item.getArtitle()))
				{
					((Tab) oldTab).setSelected(true);
					return;
				}
					
				}
				
			}
		}
		Tab newTab = new Tab();
		if(language.equals("en"))
		newTab.setLabel(item.getTitle());
		else
		newTab.setLabel(item.getArtitle());	
		
		newTab.setClosable(true);
		Tabpanel newTabpanel = new Tabpanel();
		Include incContentPage = new Include();
		//logger.info(item.getHref());
		
		incContentPage.setSrc(item.getHref());
		incContentPage.setParent(newTabpanel);
		newTabpanel.setParent(contentTabpanels);
		newTab.setParent(contentTabs);
		newTab.setSelected(true);
		newTab.setVflex("1");
			
		/*
		center.getChildren().clear();	
		if(!item.getHref().equals(""))
		Executions.createComponents(item.getHref(), center, null);
		//Executions.getCurrent().sendRedirect(pagename);
		//Messagebox.show(pagename);   		  	
		 */
		
		}
		catch (Exception ex) {
			logger.error("error in Pagecomposer---menuHRClicked-->" , ex);
		}
	}

	public List<MenuModel> getLsthbaMenu() 
	{
		lsthbaMenu=mData.getSideBarSubMenuList(4,1,companyroleid);
		return lsthbaMenu;
	}

	public void setLsthbaMenu(List<MenuModel> lsthbaMenu) {
		this.lsthbaMenu = lsthbaMenu;
	}

	public List<MenuModel> getLstfixedAssetMenu() 
	{
		lstfixedAssetMenu=mData.getSideBarSubMenuList(5,1,companyroleid);
		return lstfixedAssetMenu;
	}

	public void setLstfixedAssetMenu(List<MenuModel> lstfixedAssetMenu) {
		this.lstfixedAssetMenu = lstfixedAssetMenu;
	}

	public List<MenuModel> getLsthrMenu() 
	{
		lsthrMenu=mData.getSideBarSubMenuList(1,1,companyroleid);
		return lsthrMenu;
	}

	public void setLsthrMenu(List<MenuModel> lsthrMenu) {
		this.lsthrMenu = lsthrMenu;
	}

	public List<MenuModel> getLsttimesheetMenu() 
	{
		lsttimesheetMenu=mData.getSideBarSubMenuList(2,1,companyroleid);
		return lsttimesheetMenu;
	}

	public void setLsttimesheetMenu(List<MenuModel> lsttimesheetMenu) {
		this.lsttimesheetMenu = lsttimesheetMenu;
	}

	public List<MenuModel> getLstRealEstateMenu() 
	{
		lstRealEstateMenu=mData.getSideBarSubMenuList(6,1,companyroleid);
		return lstRealEstateMenu;
	}

	public void setLstRealEstateMenu(List<MenuModel> lstRealEstateMenu) {
		this.lstRealEstateMenu = lstRealEstateMenu;
	}

	public List<MenuModel> getLstGarageMenu() 
	{
		lstGarageMenu=mData.getSideBarSubMenuList(7,1,companyroleid);
		return lstGarageMenu;
	}

	public void setLstGarageMenu(List<MenuModel> lstGarageMenu) {
		this.lstGarageMenu = lstGarageMenu;
	}

	public List<MenuModel> getLstVisitorsMenu() 
	{
		lstVisitorsMenu=mData.getSideBarSubMenuList(9,1,companyroleid);
		return lstVisitorsMenu;
	}

	public void setLstVisitorsMenu(List<MenuModel> lstVisitorsMenu) {
		this.lstVisitorsMenu = lstVisitorsMenu;
	}

	public List<MenuModel> getLstSchoolMenu() 
	{
		lstSchoolMenu=mData.getSideBarSubMenuList(8,1,companyroleid);
		return lstSchoolMenu;
	}

	public void setLstSchoolMenu(List<MenuModel> lstSchoolMenu) {
		this.lstSchoolMenu = lstSchoolMenu;
	}

	public List<MenuModel> getLstCRMMenu() {
		lstCRMMenu=mData.getSideBarSubMenuList(80,1,companyroleid);
		return lstCRMMenu;
	}

	public void setLstCRMMenu(List<MenuModel> lstCRMMenu) {
		this.lstCRMMenu = lstCRMMenu;
	}

	public List<MenuModel> getLstReportsMenu() 
	{
		lstReportsMenu=mData.getSideBarSubMenuList(10,1,companyroleid);
		return lstReportsMenu;
	}

	public void setLstReportsMenu(List<MenuModel> lstReportsMenu) {
		this.lstReportsMenu = lstReportsMenu;
	}

	public List<MenuModel> getLstListsMenu() 
	{
		lstListsMenu=mData.getSideBarSubMenuList(107,1,companyroleid);		
		return lstListsMenu;
	}

	public void setLstListsMenu(List<MenuModel> lstListsMenu) {
		this.lstListsMenu = lstListsMenu;
	}

	public List<MenuModel> getLstPayRollMenu() 
	{
		lstPayRollMenu=mData.getSideBarSubMenuList(3,1,companyroleid);		
		return lstPayRollMenu;
	}

	public void setLstPayRollMenu(List<MenuModel> lstPayRollMenu) {
		this.lstPayRollMenu = lstPayRollMenu;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<MenuModel> getLstCompanyMenu() 
	{
		lstCompanyMenu=mData.getSideBarSubMenuList(272, 1,companyroleid);
		return lstCompanyMenu;
	}

	public void setLstCompanyMenu(List<MenuModel> lstCompanyMenu) {
		this.lstCompanyMenu = lstCompanyMenu;
	}
	
	
}
