package hba;
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
import model.FixedAssetModel;

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

public class EditBanksNameList 
{
	private Logger logger = Logger.getLogger(this.getClass());
	BankNamesData bankData =new BankNamesData();
	private BanksModel selectedBanksName;
	private boolean canSave;
	private boolean showBankFields;
	
	private List<AccountsModel> lstaccounts;
	private AccountsModel selectedAccount;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	
	public EditBanksNameList()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int bankKey=(Integer)map.get("bankKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/bankListModalDialog");
			lstaccounts=bankData.fillAccountsdropDownForBank();
			if(type.equals("edit"))
			{
			canSave=true;
			win.setTitle("Edit Bank Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add New Bank Info");
			}
			else
			{
				win.setTitle("View Bank Info");
			}
			
			
			if(bankKey>0)
			{
				selectedBanksName=bankData.getBanksNameNameListByID(bankKey);
				
				for(AccountsModel model:lstaccounts)
				{
					if(model!=null)
					{
					if(model.getRec_No()==selectedBanksName.getAccountRefKey())
					{
						selectedAccount=model;
						break;
					}
					}
				}
			
			}
			else
			{
				selectedBanksName=new BanksModel();
				selectedBanksName.setAccountRefKey(0);
				selectedBanksName.setActName("");
				selectedBanksName.setActNumber("");
				selectedBanksName.setAttn_Name("");
				selectedBanksName.setAttn_Position("");
				selectedBanksName.setBankName("");
				selectedBanksName.setBranch("");
				selectedBanksName.setIBANNo("");
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
				BanksModel fx = (BanksModel) ctx.getProperty().getValue();			
				String name = fx.getBankName();
																		
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
	   public void updateBanksNameList(@BindingParam("cmp") Window x) throws ParseException
	   {
		 int result=0;
		 if(selectedAccount!=null)
		 {
			 selectedBanksName.setAccountRefKey(selectedAccount.getRec_No());
		 }
			 
		 if(selectedBanksName.getBankName().equalsIgnoreCase(""))
		 {
			 Messagebox.show("Please Enter the Bank Name.","Bank List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 List<BanksModel> QbListNames=bankData.getNameFromBankListForValidation();
		
		 if(selectedBanksName.getRecno()>0)
		 {
			 for(BanksModel ValidationName:QbListNames)
				{
				 if(selectedBanksName.getBankName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getBankName().replaceAll("\\s","")) && (selectedBanksName.getRecno()!=ValidationName.getRecno()))
					{
						Messagebox.show("The Bank Name already exist.","Other Name List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 		result= bankData.updateBankNmaeList(selectedBanksName);
		 }
		 else
		 {
			 for(BanksModel ValidationName:QbListNames)
				{
					if(selectedBanksName.getBankName().replaceAll("\\s","").equalsIgnoreCase(ValidationName.getBankName().replaceAll("\\s","")))
					{
						Messagebox.show("The Bank Name already exist.","Bank List",Messagebox.OK , Messagebox.INFORMATION);
						return;
					}
				}
			 int tmpRecNo=bankData.GetBankListRecNoQuery();
			 result=bankData.inserBankNameQuerry(selectedBanksName,tmpRecNo);
		 }
		 
		if(result==1)
		{
		
			if(selectedBanksName.getRecno()>0)
			{
					Clients.showNotification("The Bank Name Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}else
			{
				 Clients.showNotification("The Bank Name Has Been Saved  Successfully.",
				 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
		 Map args = new HashMap();
		 args.put("type", "BankName");		
		 BindUtils.postGlobalCommand(null, null, "refreshParentBank", args);
		 x.detach();
		}
		else
			Clients.showNotification("Error at Bank Nam !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
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
	 * @return the selectedBanksName
	 */
	public BanksModel getSelectedBanksName() {
		return selectedBanksName;
	}

	/**
	 * @param selectedBanksName the selectedBanksName to set
	 */
	public void setSelectedBanksName(BanksModel selectedBanksName) {
		this.selectedBanksName = selectedBanksName;
	}

	/**
	 * @return the lstaccounts
	 */
	public List<AccountsModel> getLstaccounts() {
		return lstaccounts;
	}

	/**
	 * @param lstaccounts the lstaccounts to set
	 */
	public void setLstaccounts(List<AccountsModel> lstaccounts) {
		this.lstaccounts = lstaccounts;
	}

	/**
	 * @return the selectedAccount
	 */
	public AccountsModel getSelectedAccount() {
		return selectedAccount;
	}

	/**
	 * @param selectedAccount the selectedAccount to set
	 */
	public void setSelectedAccount(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}

	
	

	
}
