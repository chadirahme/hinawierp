package layout;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

public class CompanyHeaderViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	MenuData mData=new MenuData();
	WebusersModel dbUser=null;
	private List<MenuModel> lstMainMenu;
		
	public CompanyHeaderViewModel()
	{
		try
		{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		lstMainMenu=(List<MenuModel>)sess.getAttribute("lstCompanyMainMenu");
		String url=Executions.getCurrent().getDesktop().getRequestPath();
		
		//String id=Executions.getCurrent().getParameter("id");
		//logger.info("url>>> " + id);
		
		
		String language= (String) sess.getAttribute("language");
		if(language==null)
		{
			language="en";
			Sessions.getCurrent().setAttribute("language", language);
		}
		
		if(dbUser==null)
		{
			Executions.sendRedirect("/index.zul");
		}
		if(lstMainMenu==null)
		{
			logger.info("lstCompanyMainMenu is null>> ");
			lstMainMenu=getMenus();
 			Sessions.getCurrent().setAttribute("lstCompanyMainMenu", lstMainMenu);
		}
		else
		{
			lstMainMenu=(List<MenuModel>)sess.getAttribute("lstCompanyMainMenu");
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
		//logger.error("error at CompanyHeaderViewModel>>init>> "+ex.getMessage());
		//logger.error("error in CompanyHeaderViewModel---int-->" , ex);\
		logger.info("error in CompanyHeaderViewModel---int-->" , ex);
		}
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
				logger.error("ERROR in CompanyHeaderViewModel ----> changePassword", ex);		
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
	
	@Command
	public void logout()
	{
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("Authentication");
		sess.removeAttribute("lstCompanyMainMenu");	
		sess.removeAttribute("lstDemoMainMenu");
		sess.removeAttribute("language");	
		sess.removeAttribute("Admin");
		Executions.sendRedirect("/index.zul");
		//Executions.sendRedirect("http://hinawi.com");
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
 			Sessions.getCurrent().setAttribute("Authentication", dbUser);
		}
		else if(language.equals("ar"))
		{
			language="en";
			Sessions.getCurrent().setAttribute("language", language);
			dbUser.setLanguage("Arabic"); 			
 			Sessions.getCurrent().setAttribute("Authentication", dbUser);
		}
		
		Locale prefer_locale= new Locale(language);		
		Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, prefer_locale);
		Executions.sendRedirect("");
	}
	private List<MenuModel> getMenus()
	{
	     if(dbUser==null) Executions.sendRedirect("/index.zul");
	     int userid=0;
	     if(dbUser!=null)
	    	 userid=dbUser.getUserid();
	    	 
	     //get menu from rolescredentials
	      return mData.getMenuCompanyRoleList(dbUser.getCompanyroleid());
		 //return mData.getMenuList(userid,false);
	}
	
	
	public List<MenuModel> getLstMainMenu() {
		return lstMainMenu;
	}

	public void setLstMainMenu(List<MenuModel> lstMainMenu) {
		this.lstMainMenu = lstMainMenu;
	}
}
