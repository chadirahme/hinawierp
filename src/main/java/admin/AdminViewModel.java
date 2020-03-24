package admin;

import login.LoginData;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class AdminViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	private WebusersModel objLoginModel;
	private String message;
	public AdminViewModel()
	{
		objLoginModel=new WebusersModel();
	}
	
	@Command	
	@NotifyChange("message")
	public void loginClicked()
	{		
		message="";
		if(this.objLoginModel != null)
	    {
			LoginData _data=new LoginData();
			WebusersModel dbUser=_data.getUserLogin(objLoginModel);	
			if(dbUser.getUserid()!=0)
			{
				Sessions.getCurrent().setAttribute("Admin", dbUser);
				if(dbUser.getRoleid()==1)
				Executions.sendRedirect("/admin/home.zul");
				else
				message="You don't have access to this page !";
			}
			else
			{				
			message="Invalid User Name / Password !";
			}
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

	public void setMessage(String message) {
		this.message = message;
	}
}
