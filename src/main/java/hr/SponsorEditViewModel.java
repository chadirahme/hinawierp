package hr;

import hr.model.SponsorModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AccountsModel;
import model.BanksModel;

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
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class SponsorEditViewModel {
	
	private Logger logger = Logger.getLogger(this.getClass());
	SponsorData sponsorsData =new SponsorData();
	private SponsorModel selectedSponsor;
	private boolean canSave;
	private boolean showBankFields;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	
	public SponsorEditViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int sponsorKey=0;//map.get("sponsorKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/sponsorListModalDialog");
			if(type.equals("edit"))
			{
			canSave=true;
			win.setTitle("Edit Sponsor");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add Sponsor");
			}
			else
			{
				win.setTitle("View Sponsor");
			}
			
			if(sponsorKey>0)
			{
				selectedSponsor=sponsorsData.getSponsorById(sponsorKey);
			
			}
			else
			{
				selectedSponsor=new SponsorModel();
				selectedSponsor.setSponsorKey(0);
				selectedSponsor.setSponsorName("");
				selectedSponsor.setSponsorNameArabic("");
				selectedSponsor.setTelePhone("");
				selectedSponsor.setFax("");
				selectedSponsor.setNotes("");
				selectedSponsor.setCompanyId("");
				selectedSponsor.setBankCode("");
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
				SponsorModel fx = (SponsorModel)ctx.getProperty().getValue();				
				String name = fx.getSponsorName();
																		
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
	   @NotifyChange({"lstItems","footer"})
	   public void updateSponsorList(@BindingParam("cmp") Window x) throws ParseException
	   {
		 int result=0;
			 
		 if(selectedSponsor.getSponsorName().equalsIgnoreCase(""))
		 {
			 Messagebox.show("Please Enter the Sponsor Name.","Sponsor List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 List<SponsorModel> sopnsorNames=sponsorsData.getNameFromSponsorForValidation();
		
		 if(selectedSponsor.getSponsorKey()>0)
		 {
			 for(SponsorModel ValidationName:sopnsorNames)
				{
				 if(selectedSponsor.getSponsorName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getSponsorName().replaceAll("\\s","")) && (selectedSponsor.getSponsorKey()!=ValidationName.getSponsorKey()))
					{
						Messagebox.show("The Sponsor Name already exist.","Sponsor List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				 if(selectedSponsor.getSponsorNameArabic().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getSponsorNameArabic().replaceAll("\\s","")) && (selectedSponsor.getSponsorKey()!=ValidationName.getSponsorKey()))
					{
						Messagebox.show("The Sponsor Name-Arabic already exist.","Sponsor List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 		result= sponsorsData.updateSponsor(selectedSponsor);
		 }
		 else
		 {
			 for(SponsorModel ValidationName:sopnsorNames)
				{
					if(selectedSponsor.getSponsorName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getSponsorName().replaceAll("\\s","")))
					{
						Messagebox.show("The Sponsor Name already exist.","Sponsor List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
					 if(selectedSponsor.getSponsorNameArabic().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getSponsorNameArabic().replaceAll("\\s","")))
						{
							Messagebox.show("The Sponsor Name-Arabic already exist.","Sponsor List",Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
				}
			 int tmpRecNo=sponsorsData.getSponsorMaxRecQuerry();
			 result=sponsorsData.inserBankNameQuerry(selectedSponsor,tmpRecNo);
		 }
		 
		if(result==1)
		{
		
			if(selectedSponsor.getSponsorKey()>0)
			{
					Clients.showNotification("The Sponsor Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}else
			{
				 Clients.showNotification("The Sponsor Has Been Saved  Successfully.",
				 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		 Map args = new HashMap();
		 args.put("type", "BankName");		
		 BindUtils.postGlobalCommand(null, null, "refreshParentSponsor", args);
		 x.detach();
		}
		else
			Clients.showNotification("Error at Sponsor Name !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
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

	/**
	 * @return the selectedSponsor
	 */
	public SponsorModel getSelectedSponsor() {
		return selectedSponsor;
	}

	/**
	 * @param selectedSponsor the selectedSponsor to set
	 */
	public void setSelectedSponsor(SponsorModel selectedSponsor) {
		this.selectedSponsor = selectedSponsor;
	}

}
