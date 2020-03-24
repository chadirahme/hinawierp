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
import model.ClassModel;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditClassViewModel {
	
	
	
	private Logger logger = Logger.getLogger(this.getClass());
	ClassData classData =new ClassData();
	HBAData data=new HBAData();
	private ClassModel selectedClass;
	private boolean canSave;
	private boolean showBankFields;
	private boolean selectedCheckBox=false;
	private boolean disableSubOf=true;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	private List<ClassModel> fillsubOfClass;
	
	private ClassModel tempFillsubOfClass= new ClassModel();
	
	private MenuModel companyRole;
	
	private boolean statusActive;
	
	public EditClassViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int classKey=(Integer)map.get("classKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/classModalDialog");
			if(type.equals("edit"))
			{
			canSave=true;
			win.setTitle("Edit Class Name Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add New Class Name Info");
			}
			else
			{
				win.setTitle("View Class Name Info");
			}
			fillsubOfClass=classData.getClassSubOFValues();
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			
			if(classKey>0)
			{
				selectedClass=classData.getClassById(classKey);
				if(selectedClass.getIsActive().equals("N"))
				{
					statusActive=true;
				}
				else
				{
					statusActive=false;
				}
				for(ClassModel model:fillsubOfClass)
				{
					if(model.getClass_Key()==selectedClass.getSubofKey())
					{
						selectedClass.setSlectedSubOfClass(model);
						tempFillsubOfClass=model;
						if(tempFillsubOfClass.getName().equalsIgnoreCase("none"))
						{
							selectedCheckBox=false;
						}
						else{
							selectedCheckBox=true;
						}
						
						doCheckedClass();
						break;
					}
				}
			}
			else
			{
				selectedClass=new ClassModel();
				selectedClass.setClass_Key(0);
				selectedClass.setName("");
				selectedClass.setFullName("");
				selectedClass.setIsActive("false");
				selectedClass.setSublevel(0);
				selectedClass.setSlectedSubOfClass(fillsubOfClass.get(0));
				statusActive=false;
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditClassViewModel ----> init", ex);			
		}
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				ClassModel fx = (ClassModel)ctx.getProperty().getValue();				
				String name = fx.getFullName();
																		
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
	   @NotifyChange({"disableSubOf","slectedSubOfClass","fillsubOfClass","selectedClass"})
	   public void doCheckedClass()
	 	{
		  if (selectedCheckBox==true)
		  {
			  disableSubOf=false;
			  selectedClass.setSlectedSubOfClass(tempFillsubOfClass);
			  
		  }
		  else
		  {
			  disableSubOf=true;
			  selectedClass.setSlectedSubOfClass(fillsubOfClass.get(0));
		  }
		  
	 	}

	   @Command
	   @NotifyChange({"lstClass","footer"})
	   public void updateClassList(@BindingParam("cmp") Window x) throws ParseException
	   {
		 int result=0;
		 if(selectedClass.getName().equalsIgnoreCase(""))
		 {
			 Messagebox.show("Please Enter the Class Name.","Class List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 if(selectedCheckBox==true && selectedClass.getSlectedSubOfClass()==null)
		 {
			 Messagebox.show("Please select the sub of.","Class List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 
		 if(selectedClass.getSlectedSubOfClass()!=null && selectedClass.getSlectedSubOfClass().getName().equalsIgnoreCase(selectedClass.getName())&& selectedClass.getSlectedSubOfClass().getClass_Key()==selectedClass.getClass_Key())
		 {
			 Messagebox.show("You cannot make an item a subitem of itself.","Item List", Messagebox.OK , Messagebox.INFORMATION);
				return;
		 }
		 List<ClassModel> classNames=classData.getNameFromClassListForValidation();
		 if(statusActive)
		 {
			 selectedClass.setIsActive("N");
		 }
		 else
		 {
			 selectedClass.setIsActive("Y");
		 }
		 if(null!=selectedClass.getSlectedSubOfClass() && selectedClass.getSlectedSubOfClass().getName().equalsIgnoreCase("none"))
		 {
			 ClassModel subIytem=new ClassModel();
			 selectedClass.setSlectedSubOfClass(subIytem);
		 }
		 if(selectedClass.getClass_Key()>0)
		 {
			 for(ClassModel ValidationName:classNames)
				{
				 if(selectedClass.getName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getName().replaceAll("\\s","")) && (selectedClass.getClass_Key()!=ValidationName.getClass_Key()))
					{
						Messagebox.show("The class name already exist.","Class List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 		result= classData.updateClass(selectedClass);
		 }
		 else
		 {
			 for(ClassModel ValidationName:classNames)
				{
					if(selectedClass.getName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getName().replaceAll("\\s","")))
					{
						Messagebox.show("The class name already exist.","Other Name List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 	
		 	result= classData.addClass(selectedClass);
			 
		 }
		 
		if(result==1)
		{
		
			if(selectedClass.getClass_Key()>0)
			{
					Clients.showNotification("The Class Name  Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}else
			{
				 Clients.showNotification("The Class Name  Has Been Saved  Successfully.",
				 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		 Map args = new HashMap();
		 args.put("type", "class");		
		 BindUtils.postGlobalCommand(null, null, "refreshParentClass", args);
		 x.detach();
		}
		else
			Clients.showNotification("Error at class Name list !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
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
	 * @return the selectedClass
	 */
	public ClassModel getSelectedClass() {
		return selectedClass;
	}

	/**
	 * @param selectedClass the selectedClass to set
	 */
	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}

	public boolean isShowBankFields() {
		return showBankFields;
	}

	/**
	 * @param showBankFields the showBankFields to set
	 */
	public void setShowBankFields(boolean showBankFields) {
		this.showBankFields = showBankFields;
	}

	/**
	 * @return the selectedCheckBox
	 */
	public boolean isSelectedCheckBox() {
		return selectedCheckBox;
	}

	/**
	 * @param selectedCheckBox the selectedCheckBox to set
	 */
	public void setSelectedCheckBox(boolean selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
	}

	/**
	 * @return the disableSubOf
	 */
	public boolean isDisableSubOf() {
		return disableSubOf;
	}

	/**
	 * @param disableSubOf the disableSubOf to set
	 */
	public void setDisableSubOf(boolean disableSubOf) {
		this.disableSubOf = disableSubOf;
	}

	/**
	 * @return the fillsubOfClass
	 */
	public List<ClassModel> getFillsubOfClass() {
		return fillsubOfClass;
	}

	/**
	 * @param fillsubOfClass the fillsubOfClass to set
	 */
	public void setFillsubOfClass(List<ClassModel> fillsubOfClass) {
		this.fillsubOfClass = fillsubOfClass;
	}

	/**
	 * @return the tempFillsubOfClass
	 */
	public ClassModel getTempFillsubOfClass() {
		return tempFillsubOfClass;
	}

	/**
	 * @param tempFillsubOfClass the tempFillsubOfClass to set
	 */
	public void setTempFillsubOfClass(ClassModel tempFillsubOfClass) {
		this.tempFillsubOfClass = tempFillsubOfClass;
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

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==244)
			{
				companyRole=item;
				break;
			}
		}
	}

	/**
	 * @return the statusActive
	 */
	public boolean isStatusActive() {
		return statusActive;
	}

	/**
	 * @param statusActive the statusActive to set
	 */
	public void setStatusActive(boolean statusActive) {
		
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && statusActive==false)
		{
			Clients.showNotification("You are not allowed to Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && statusActive==true)
		{
			Clients.showNotification("You are not allowed to In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.statusActive = statusActive;
		}
	}

	
}
