package login;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.web.fn.ServletFns;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import common.CookieController;
import setup.users.WebusersModel;



public class AuthenticateViewModel 
{
	private Logger logger = Logger.getLogger(AuthenticateViewModel.class);
	private WebusersModel objLoginModel;
	private String message;
	WebusersModel dbUser=null;
	CookieController cookieController=new CookieController();
	//ZKWebSocketServer loginWS;
	
	public AuthenticateViewModel()
	{
		try
		{
		//loginWS=new ZKWebSocketServer(new URI("ws://localhost:8080/sata/websocket"));
			logger.info("AuthenticateViewModel>>>");
		objLoginModel=new WebusersModel();
		
		objLoginModel.setUsername("");
		objLoginModel.setUserpwd("");
		objLoginModel.setCompanyName("DEMO");
		
		String comapnyName=cookieController.getCookie("CompanyName");
		
		objLoginModel.setCompanyName(comapnyName);
		//objLoginModel.setCompanyName("DEMO");
		//objLoginModel.setUsername("chadi");
		//objLoginModel.setUserpwd("123456");
		
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		String type="";
		type=(String)map.get("type");
		if(type!=null && type.equalsIgnoreCase("changePassword"))
		{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		objLoginModel.setCompanyName(dbUser.getCompanyName());
		}
		
		//re-login message display
		Session sess1 = Sessions.getCurrent();
		String reLoginmsg=(String)sess1.getAttribute("re-login");
		if(reLoginmsg!=null && !reLoginmsg.equalsIgnoreCase(""))
		{
			message=reLoginmsg;
			sess1.removeAttribute("re-login");
		}
		}
		catch(Exception ex)
		{
			logger.error("ERROR in AuthenticateViewModel ----> init", ex);
		}
			
		/*
		objLoginModel.setUsername("demo");
		objLoginModel.setUserpwd("demo");
		
		objLoginModel.setCompanyName("Explorer");
		//objLoginModel.setCompanyName("GCC");
		objLoginModel.setUsername("admin");
		objLoginModel.setUserpwd("2");
		*/
	}

	@Command	
	@NotifyChange("message")
	public void loginClicked()
	{
		//Messagebox.show(objLoginModel.getUserpwd());
		//getEndUserInfo();
		try{
		message="";
		if(this.objLoginModel != null)
	    {
			LoginData _data=new LoginData();
			
			 cookieController.setCookie("CompanyName", objLoginModel.getCompanyName(), 999999999);			
			//check if company Exist
		int companyID=_data.isCompanyExsists(objLoginModel.getCompanyName());
		if(companyID==0)
		{
			message="Company is not exist !";
			return;
		}
			
			objLoginModel.setCompanyid(companyID);
			WebusersModel dbUser=_data.getUserLogin(objLoginModel);	
			dbUser.setLanguage("Arabic");
									
			if(dbUser.getUserid()!=0)
			{
				boolean isMobile = false;
				logger.info(Executions.getCurrent().getBrowser());
				//double x= Executions.getCurrent().getBrowser ("mobile");
				//logger.info("xxxxxx >> " + x) ;
				
				//ServletRequest request = ServletFns.getCurrentRequest();			   
				/*isMobile = Servlets.getBrowser(request, "mobile") != null;
			    if (isMobile)
			    {
			    	Sessions.getCurrent().setAttribute("Authentication", dbUser);
					this.setMessage("Logging In....");	
					Executions.sendRedirect("mobile.zul");
					return;
			    }*/
			    
				
				if(dbUser.getAdminuser().equals("1"))
				{
					dbUser.setUsername("Admin");
				}
				
				//loginWS.sendMessage(dbUser.getUsername() + " form " + dbUser.getCompanyName() + " was login.. ");
				//Map args = new HashMap();
				//args.put("employeeEmail", "1111");		
				//BindUtils.postGlobalCommand(null, null, "refreshAlarm", args);
				
				Sessions.getCurrent().setAttribute("Authentication", dbUser);
				if(dbUser.getRoleid()==1)
				{
				Sessions.getCurrent().setAttribute("Admin", dbUser);					
				Executions.sendRedirect("admin/home.zul");
				}
				else
				{				
					//here i need to check if employee then take him to dashboard
					if(dbUser.getEmployeeKey()>0 && companyID==4)
					{
						HttpServletRequest request=(HttpServletRequest) Executions.getCurrent().getNativeRequest();
						HttpSession session = request.getSession();
						session.setAttribute("CompanyName", objLoginModel.getCompanyName());
						request.setAttribute("Authentication", dbUser);						
						Executions.getCurrent().sendRedirect("dashboard.html");
						 //Executions.sendRedirect("dashboard.html");
					}
					//else to old company file
					else
					{
			     Executions.sendRedirect("companyfile.zul");
			    //Executions.sendRedirect("demo.zul");
					}
				}
			}
			else
			{				
			message="Invalid User Name / Password !";
			}
	    }	
		}
		catch(Exception ex)
		{
			logger.error("ERROR in AuthenticateViewModel ----> loginClicked", ex);
		}
	}
	
	
	@Command	
	@NotifyChange("message")
	public void changePassword()
	{
		//Messagebox.show(objLoginModel.getUserpwd());
		//getEndUserInfo();
		try{
		message="";
		if(dbUser.getFirstname().equalsIgnoreCase("Demo"))
		{
			message="Sorry you cannot change password for Account ("+dbUser.getFirstname()+")";//  demo password cannot be changed
			return;
		}
		if(this.objLoginModel != null)
	    {
			LoginData _data=new LoginData();
			//check if company Exist
		int companyID=	_data.isCompanyExsists(objLoginModel.getCompanyName());
		if(companyID==0)
		{
			message="Company is not exist !";
			return;
		}
		
		if(objLoginModel.getOldpwd()==null)
		{
			message="Invalid old password !";
			return;
		}
		
		objLoginModel.setCompanyid(companyID);
		String oldpass=	_data.getOldPassword(objLoginModel.getOldpwd(),dbUser.getUserid());
		if(oldpass.equalsIgnoreCase("") || oldpass==null)
		{
			message="Invalid old password !";
			return;
		}
		
		if(objLoginModel.getNewPassWrd()==null)
		{
			message="Invalid New password !";
			return;
		}
		
		if(objLoginModel.getConfirmPasssword()==null)
		{
			message="Invalid Confirm password !";
			return;
		}
		
		if(objLoginModel.getNewPassWrd()!=null && objLoginModel.getNewPassWrd()!=null)
		{
			if(!objLoginModel.getNewPassWrd().equalsIgnoreCase(objLoginModel.getConfirmPasssword()))
			{
				message="New password and Confirmed password does not match!";
				return;
			}
		}
		objLoginModel.setUserid(dbUser.getUserid());
		_data.changePassword(objLoginModel);
			if(dbUser.getUserid()!=0)
			{
				String msg="The Password has been changed Successfully.Please re-login";
				Sessions.getCurrent().setAttribute("re-login", msg);
				Executions.sendRedirect("/login.zul");
				Session sess = Sessions.getCurrent();
				sess.removeAttribute("Authentication");
				sess.removeAttribute("lstDemoMainMenu");
				sess.removeAttribute("lstCompanyMainMenu");	
				sess.removeAttribute("language");
				sess.removeAttribute("Admin");
				
			}
			else
			{				
			message="Invalid User Name / Password !";
			}
	    }	
	}
	catch(Exception ex)
	{
		logger.error("ERROR in AuthenticateViewModel ----> changePassword", ex);
	}
	}
	
	
	public WebusersModel getObjLoginModel() {
		return objLoginModel;
	}

	public void setObjLoginModel(WebusersModel objLoginModel) {
		this.objLoginModel = objLoginModel;
	}

	public String getMessage() {
		return message;
	}

	@NotifyChange("message")
	public void setMessage(String message) {
		this.message = message;
	}
}
