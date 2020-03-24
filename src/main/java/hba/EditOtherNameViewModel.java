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
import model.OtherNamesModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditOtherNameViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	OtherNameListData otherNameListData =new OtherNameListData();
	HBAData data=new HBAData();
	private OtherNamesModel selectedOtherName;
	private boolean canSave;
	private boolean showBankFields;
	private boolean activeOtherName;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	
	private MenuModel companyRole;
	
	private boolean adminUser;
	
	
	public EditOtherNameViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int otherNameKey=(Integer)map.get("otherNameKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/otherNameListModalDialog");
			if(type.equals("edit"))
			{
			canSave=true;
			win.setTitle("Edit Other Name Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add New Other Name Info");
			}
			else
			{
				win.setTitle("View Other Name Info");
			}
			
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");
			}
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			if(otherNameKey>0)
			{
				selectedOtherName=otherNameListData.getOtherNameListByID(otherNameKey);
				if(selectedOtherName.getIsactive().equals("N"))
				{
					activeOtherName=true;
				}
				else
				{
					activeOtherName=false;
				}
			}
			else
			{
				selectedOtherName=new OtherNamesModel();
				selectedOtherName.setName("");
				selectedOtherName.setListid("");
				selectedOtherName.setArName("");
				selectedOtherName.setCompanyName("");
				selectedOtherName.setPhone("");
				selectedOtherName.setAltphone("");
				selectedOtherName.setFax("");
				selectedOtherName.setEmail("");
				selectedOtherName.setContact("");
				selectedOtherName.setAccountNumber("");
				selectedOtherName.setSkypeID("");
				selectedOtherName.setAlternateContact("");
				selectedOtherName.setIsactive("Y");
				activeOtherName=false;
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCustomerViewModel ----> init", ex);			
		}
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				OtherNamesModel fx = (OtherNamesModel)ctx.getProperty().getValue();				
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
	   @NotifyChange({"lstOtherNames","footer"})
	   public void updateOtherNameList(@BindingParam("cmp") Window x) throws ParseException
	   {
		 int result=0;
		 if(selectedOtherName.getName().equalsIgnoreCase(""))
		 {
			 Messagebox.show("Please Enter the Name.","Other Name List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 List<QbListsModel> QbListNames=data.getNameFromQbListForValidation();
		 if(activeOtherName)
		 {
			 selectedOtherName.setIsactive("N");
		 }
		 else
		 {
			 selectedOtherName.setIsactive("Y");
		 }
		 if(selectedOtherName.getCustkey()>0)
		 {
			 for(QbListsModel ValidationName:QbListNames)
				{
				 if(selectedOtherName.getName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getName().replaceAll("\\s","")) && (selectedOtherName.getCustkey()!=ValidationName.getRecNo()))
					{
						Messagebox.show("The name already exist.","Other Name List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
					creationdate=df.parse(sdf.format(c.getTime()));
					selectedOtherName.setModifiedDate(creationdate);
			 		result= otherNameListData.updateOtherNameList(selectedOtherName);
		 }
		 else
		 {
			 for(QbListsModel ValidationName:QbListNames)
				{
					if(selectedOtherName.getName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getName().replaceAll("\\s","")))
					{
						Messagebox.show("The name already exist.","Other Name List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 int tmpRecNo=data.GetOtherNameListRecNoQuery();
			 creationdate=df.parse(sdf.format(c.getTime()));
			 selectedOtherName.setModifiedDate(creationdate);
			 selectedOtherName.setCreatedDate(creationdate);
			 selectedOtherName.setFullName(selectedOtherName.getName());
			 int resultNew=0;
			 
			 resultNew=data.OtherNameListInsertQbListQuery(selectedOtherName,tmpRecNo);
			 
			 if(resultNew>0)
			 {
			 result=data.OtherNameListInsertQuery(selectedOtherName,tmpRecNo);
			 }
			 	
		 		//result= otherNameListData.addOtherNameList(selectedOtherName);
			 
		 }
		 
		if(result==1)
		{
		
			if(selectedOtherName.getCustkey()>0)
			{
					Clients.showNotification("The Other Name Record Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}else
			{
				 Clients.showNotification("The Other Name Record Has Been Saved  Successfully.",
				 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		 Map args = new HashMap();
		 args.put("type", "OtherName");		
		 BindUtils.postGlobalCommand(null, null, "refreshParent", args);
		 x.detach();
		}
		else
			Clients.showNotification("Error at OtherNameList !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
		x.detach();
		
	   }
	   
	   public void onModifySelectedList(ForwardEvent event){
			Checkbox checkbox = (Checkbox) event.getOrigin().getTarget();		
			if (checkbox.isChecked()); 
			
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
	 * @return the selectedOtherName
	 */
	public OtherNamesModel getSelectedOtherName() {
		return selectedOtherName;
	}

	/**
	 * @param selectedOtherName the selectedOtherName to set
	 */
	public void setSelectedOtherName(OtherNamesModel selectedOtherName) {
		this.selectedOtherName = selectedOtherName;
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

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==242)
			{
				companyRole=item;
				break;
			}
		}
	}

	/**
	 * @return the activeOtherName
	 */
	public boolean isActiveOtherName() {
		return activeOtherName;
	}

	/**
	 * @param activeOtherName the activeOtherName to set
	 */
	public void setActiveOtherName(boolean activeOtherName) {
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && activeOtherName==false)
		{
			Clients.showNotification("You are not allowed to Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && activeOtherName==true)
		{
			Clients.showNotification("You are not allowed to In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.activeOtherName = activeOtherName;
		}
	}

	

	
}
