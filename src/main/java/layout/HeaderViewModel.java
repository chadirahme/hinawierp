package layout;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

public class HeaderViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	MenuData mData=new MenuData();
	WebusersModel dbUser=null;
	private List<MenuModel> lstMainMenu;
	
	public HeaderViewModel()
	{
		try
		{
	     logger.info("URL>> testing");
			
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		lstMainMenu=(List<MenuModel>)sess.getAttribute("lstDemoMainMenu");
		
		String url=Executions.getCurrent().getDesktop().getRequestPath();
		//logger.info("URL>>" + url);
		
		String language= (String) sess.getAttribute("language");
		if(language==null)
		{
			language="en";
			Sessions.getCurrent().setAttribute("language", language);
		}
		
		if(dbUser==null)//|| lstMainMenu==null)
		{
			
			dbUser=new WebusersModel();
			dbUser.setUserid(1);
			dbUser.setFirstname("Demo");
 			dbUser.setRoleid(1);
 			dbUser.setCompanyid(1);
 			dbUser.setCompanyName("Hinawi Software");
 			dbUser.setUseremail("hinawi@eim.ae");
 			dbUser.setLanguage("Arabic");
 			dbUser.setMergedDatabse("Yes");
 			dbUser.setCompanyroleid(1);
 			dbUser.setProfileText(dbUser.getFirstname());
 			dbUser.setUsername("Demo"); 			
 			
 			Sessions.getCurrent().setAttribute("Authentication", dbUser);
 			
 			lstMainMenu=getMenus();
 			Sessions.getCurrent().setAttribute("lstDemoMainMenu", lstMainMenu);
 			
			if(lstMainMenu.size()>0)
			lstMainMenu.get(0).setSclassName("menuitem");
			
			//mData.checkSQLServerConnection();
			
			//Executions.sendRedirect("/index.zul");
		}
		
		if(dbUser.getUserid()==1)
		{
		if(lstMainMenu==null)
		{
			lstMainMenu=getMenus();
 			Sessions.getCurrent().setAttribute("lstDemoMainMenu", lstMainMenu);
		}
		else
		{
		//List<MenuModel> lstMenu=mData.getMenuList();
		//HttpSessionListener s=new HttpSessionListener();	
		lstMainMenu=(List<MenuModel>)sess.getAttribute("lstDemoMainMenu");
		///lstMainMenu=getMenus();
		//if(lstMainMenu.size()>0)
		//lstMainMenu.get(0).setSclassName("menuitem");
		}			
	   }
		
		else
		{
			lstMainMenu=(List<MenuModel>)sess.getAttribute("lstCompanyMainMenu");
			//will add this for dashboard
			if(lstMainMenu==null)
			{
				lstMainMenu=getMenus();
	 			Sessions.getCurrent().setAttribute("lstCompanyMainMenu", lstMainMenu);
			}
		}
		
		if(lstMainMenu==null)
		{
			sess = Sessions.getCurrent();
			sess.removeAttribute("Authentication");
			Executions.sendRedirect("/index.zul");
			return;
		}
		
		for (MenuModel obj : lstMainMenu) 
		{
			
		if(obj.getHref().equals(url))
			 obj.setSclassName("menuitem");
		else
			obj.setSclassName("defaultmenuitem");
		}
		
		}
		catch (Exception ex)
		{
		logger.error("error at HeaderViewModel>>Init>> ",ex);
		} 
		
	}
	
	@Command
	public void changeLanguage()
	{
		Session sess = Sessions.getCurrent();
		String language= (String) sess.getAttribute("language");
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(language==null)
		{
			language="en";
			Sessions.getCurrent().setAttribute("language", language);
		}
		
		if(language.equals("en"))
		{
			language="ar";
			Sessions.getCurrent().setAttribute("language", language);
			dbUser.setLanguage("English"); 
			if(dbUser.getUserid()==1)
			dbUser.setCompanyName("برامج الحناوي - إصدار الإنترنت - تجريبي");
 			Sessions.getCurrent().setAttribute("Authentication", dbUser);
		}
		else if(language.equals("ar"))
		{
			language="en";
			Sessions.getCurrent().setAttribute("language", language);
			dbUser.setLanguage("Arabic"); 	
			if(dbUser.getUserid()==1)
			dbUser.setCompanyName("Hinawi Software");
 			Sessions.getCurrent().setAttribute("Authentication", dbUser);
		}
		
		Locale prefer_locale= new Locale(language);		
		Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, prefer_locale);
		
		//if(language.equals("ar"))
		//Executions.sendRedirect("/ardemo.zul");
		//else
		Executions.sendRedirect("");	
	}
	
	@Command
	@NotifyChange({"lstMainMenu"})
	public void openPage(@BindingParam("item") MenuModel item)
	{
		for (MenuModel obj : lstMainMenu) 
		{
		obj.setSclassName("defaultmenuitem");	
		}
		
		 item.setSclassName("menuitem");
		 Executions.getCurrent().sendRedirect(item.getHref());
		 
	}
	
	@Command
	public void logout()
	{
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("Authentication");
		sess.removeAttribute("lstDemoMainMenu");
		sess.removeAttribute("lstCompanyMainMenu");	
		sess.removeAttribute("language");
		sess.removeAttribute("Admin");
		Executions.sendRedirect("/login.zul");
		
		//Executions.sendRedirect("http://hinawi.com");
		
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
				logger.error("ERROR in HeaderViewModel ----> changePassword", ex);			
			}
	}
	
	@Command
	public void sendSMSCommand()
	{
		try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("type","sms");
			   Executions.createComponents("/company/sms.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in CompanyHeaderViewModel ----> sendSMSCommand", ex);		
			}
	}
	
	
	@GlobalCommand 
	@NotifyChange({"lstEmployees"})
     public void refreshAlarm(@BindingParam("employeeEmail")String employeeEmail)
	 {		
		try
		{
			logger.info("refresh alaram...." + employeeEmail);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CompanyHeaderViewModel ----> sendSMSCommand", ex);		
		}
	 }	
	
	
	private List<MenuModel> getMenus()
	{
	     if(dbUser==null) Executions.sendRedirect("/index.zul");
	     int userid=0;
	     if(dbUser!=null)
	    	 userid=dbUser.getUserid();
	    	 
		//return mData.getMenuList(userid,false);
		 return mData.getMenuCompanyRoleList(dbUser.getCompanyroleid());
	}
	
	
	public List<MenuModel> getLstMainMenu() {
		return lstMainMenu;
	}

	public void setLstMainMenu(List<MenuModel> lstMainMenu) {
		this.lstMainMenu = lstMainMenu;
	}
	
}
