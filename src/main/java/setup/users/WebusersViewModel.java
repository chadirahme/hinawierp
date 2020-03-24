package setup.users;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class WebusersViewModel {

	WebusersData _data;
	private ListModelList<WebusersModel> lstDataModel;
	private WebusersModel selectedUser;
	
	public WebusersViewModel()
	{
		_data=new WebusersData();
		lstDataModel=new ListModelList<WebusersModel>(_data.getUsersList());
		
		//Messagebox.show(String.valueOf(lstDataModel.size()));
	}

	public ListModelList<WebusersModel> getLstDataModel() {
		return lstDataModel;
	}

	public WebusersModel getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(WebusersModel selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	@Command 
	@NotifyChange("selectedUser")
	public void updateUser()
	{
		_data.saveWebUser(selectedUser);
		Clients.showNotification("User Saved..");
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
	@NotifyChange("selectedUser")
	public void addUser()
	{
		selectedUser=new WebusersModel();
		lstDataModel.add(selectedUser);
		lstDataModel.addToSelection(selectedUser);
		
	}
	
	@Command 
	@NotifyChange("selectedUser") //use postNotifyChange() to notify dynamically
	public void deleteUser(@BindingParam("todo") final WebusersModel todo){
		//save data
		
		 Messagebox.show("Are you sure to delete this user ?","",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
                 {
					 _data.deleteWebUser(todo);
						//update the model, by using ListModelList, you don't need to notify todoListModel change
						lstDataModel.remove(todo);
						
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
	
	
	//the validator is the class to validate data before set ui data back to todo
		public Validator getTodoValidator(){
			return new AbstractValidator() {							
				public void validate(ValidationContext ctx) {
					//get the form that will be applied to todo
					WebusersModel fx = (WebusersModel)ctx.getProperty().getValue();
					//get filed firstname of the form
					String firstname = fx.getFirstname();
					String lastname = fx.getLastname();
					
					
					if(Strings.isBlank(lastname)){
						Clients.showNotification("Please fill the Last name !!");
						//mark the validation is invalid, so the data will not update to bean
						//and the further command will be skipped.
						ctx.setInvalid();
					}
					
					if(Strings.isBlank(firstname))
					{
						  addInvalidMessage(ctx, "lastnameContentError", "First Name is required !");
					}
				}
			};
		}
		
		@Command 		
		public void userRole(@BindingParam("userid") int userid)
		{				
			//Messagebox.show(String.valueOf(userid));
			 Window window = (Window)Executions.createComponents("/setup/roles.zul", null, null);
		     window.doModal();	
		}
	
	
}
