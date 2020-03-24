package hba;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.CustomerContact;
import model.QbListsModel;
import model.VendorModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditVendorViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	VendorsData data=new VendorsData();
	HBAData dataHba=new HBAData();
	private VendorModel selectedVendor;
	private boolean activeVendor;
	private boolean canSave;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();
	Date creationdate;
	
	private MenuModel companyRole;
	private CustomerContact compSetting;
	
	public EditVendorViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int vendKey=(Integer)map.get("vendKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/vendorModalDialog");
			if(type.equals("edit"))
			{
			canSave=true;
			win.setTitle("Edit Vendor Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add New Vendor Info");
			}
			else
			{
				win.setTitle("View Vendor Info");
			}
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			CustomerData cdata=new CustomerData();
			compSetting = cdata.getCOMPANYSETTINGS();
			
			if(vendKey>0)
			{
				selectedVendor=data.getVendorByKey(vendKey);
				activeVendor=selectedVendor.getIsActive().equals("Y");
				if(selectedVendor.getIsActive().equals("N"))
				{
					activeVendor=true;
				}
				else
				{
					activeVendor=false;
				}
			}
			else
			{
				selectedVendor=new VendorModel();
				selectedVendor.setName("");
				selectedVendor.setArName("");
				selectedVendor.setPhone("");
				selectedVendor.setCompanyName("");
				selectedVendor.setBillAddress1("");
				selectedVendor.setPhone("");
				selectedVendor.setFax("");
				selectedVendor.setEmail("");
				selectedVendor.setWebSite("");
				selectedVendor.setContact("");
				selectedVendor.setPrintChequeAs("");
				selectedVendor.setBankName("");
				selectedVendor.setBranchName("");
				selectedVendor.setIbanNo("");
				selectedVendor.setAltContact("");
				selectedVendor.setAltPhone("");
				selectedVendor.setSkypeId("");
				selectedVendor.setMiddleName("");
				selectedVendor.setFirstName("");
				selectedVendor.setAccountName("");
				selectedVendor.setAccountNumber("");
				selectedVendor.setSalutation("");
				selectedVendor.setLastName("");
				selectedVendor.setIsActive("false");
				activeVendor=false;
				
			}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditVendorViewModel ----> init", ex);			
		}
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				VendorModel fx = (VendorModel)ctx.getProperty().getValue();				
				String name = fx.getName();
																		
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
	
	  @Command	  
	   public void updateVendor(@ContextParam(ContextType.VIEW) Window comp) throws ParseException
	   {
		 int result=0;
		 if(selectedVendor.getName().equalsIgnoreCase(""))
		 {
			 Messagebox.show("Please Enter the Vendor Name.","Vendor List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 List<QbListsModel> QbListNames=dataHba.getNameFromQbListForValidation();
		 if(activeVendor)
		 {
			 selectedVendor.setIsActive("N");
		 }
		 else
		 {
			 selectedVendor.setIsActive("Y");
		 }
		 if(selectedVendor.getVend_Key()>0)
		 {
			 for(QbListsModel ValidationName:QbListNames)
				{
					if(selectedVendor.getName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getName().replaceAll("\\s","")) && (selectedVendor.getVend_Key()!=ValidationName.getRecNo()))
					{
						Messagebox.show("The name already exist.","Vendor List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 creationdate=df.parse(sdf.format(c.getTime()));
			 selectedVendor.setTimeModified(creationdate);
			 result=data.UpdateVendorData(selectedVendor);
		 }
		 else
		 {
			 for(QbListsModel ValidationName:QbListNames)
				{
					if(selectedVendor.getName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getName().replaceAll("\\s","")))
					{
						Messagebox.show("The name already exist.","Vendor List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 result=data.addVendorData(selectedVendor);
		 }
		 
			if(result==1)
			{
				
				if(selectedVendor.getVend_Key()>0)
				{
						Clients.showNotification("The Vendor Has Been Updated Successfully.",
			            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}else
				{
					 Clients.showNotification("The Vendor Has Been Saved  Successfully.",
					 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}
			Map args = new HashMap();
			args.put("type", "vendor");		
			BindUtils.postGlobalCommand(null, null, "refreshParentVendor", args);
			comp.detach();
			}
			else
			Clients.showNotification("Erro at save Vendor.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		 comp.detach();
		 
	   }


	public VendorModel getSelectedVendor() {
		return selectedVendor;
	}


	public void setSelectedVendor(VendorModel selectedVendor) {
		this.selectedVendor = selectedVendor;
	}


	public boolean isCanSave() {
		return canSave;
	}


	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}


	public boolean isActiveVendor() {
		return activeVendor;
	}


	public void setActiveVendor(boolean activeVendor) {
		
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && activeVendor==false)
		{
			Clients.showNotification("You are not allowed to Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && activeVendor==true)
		{
			Clients.showNotification("You are not allowed to In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.activeVendor = activeVendor;
		}
	}
	
	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= dataHba.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==74)
			{
				companyRole=item;
				break;
			}
		}
	}

	/**
	 * @return the companyRole
	 */
	public MenuModel getCompanyRole() {
		return companyRole;
	}

	/**
	 * @param companyRole the companyRole to set
	 */
	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public CustomerContact getCompSetting() {
		return compSetting;
	}

	public void setCompSetting(CustomerContact compSetting) {
		this.compSetting = compSetting;
	}
	
	
}
