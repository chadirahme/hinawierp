package hba;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import layout.MenuModel;
import model.AccountsModel;

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
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditChartOfAccountViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	ChartOFAccountData chartOfAccountData=new ChartOFAccountData();
	HBAData data = new HBAData();
	private AccountsModel selectedChatOfAccounts;
	private boolean canSave;
	private boolean showBankFields;
	private boolean activeChatOfAccounts;
	private boolean showBalance=true;
	private boolean disableBalnceDate=false;
	private boolean disableSubOf=true;
	private boolean diabaleAccountType=false;
	AccountsModel tempModel=new AccountsModel();
	
	private boolean selectedCheckBox=false;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	
	private String selectedAccountTypeModel;
	List<String> banknameList=new ArrayList<String>();
	List<String> accountTypeList=new ArrayList<String>();
	List<AccountsModel> subOfList=new ArrayList<AccountsModel>();
	
	private MenuModel companyRole;
	
	public EditChartOfAccountViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int charOfaccountKey=(Integer)map.get("charOfaccountKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/modalDialog");
		
			if(type.equals("edit"))
			{
			canSave=true;
			disableBalnceDate=true;
			win.setTitle("Edit Account Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				disableSubOf=true;
				win.setTitle("Add New Account Info");
			}
			else
			{
				win.setTitle("View Account Info");
			}
			
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			
			if(charOfaccountKey>0)
			{
				selectedChatOfAccounts=chartOfAccountData.getChartofAccountsByID(charOfaccountKey);
				
				int indx=0;
				String Match="";
				banknameList=chartOfAccountData.getBankNamesForChartofAccounts();
				accountTypeList=chartOfAccountData.getAllAccountTypeFromChartOfAccount();
				subOfList=chartOfAccountData.getsubOfOnEditChartOFAccount(selectedChatOfAccounts.getAccountType());
				selectedChatOfAccounts.setSubOfdropDown(subOfList);
				selectedChatOfAccounts.setBankNameList(banknameList);
				selectedChatOfAccounts.setAccountTypedropDown(accountTypeList);
				if(selectedChatOfAccounts.getBankName()!=null)
				indx=banknameList.indexOf(selectedChatOfAccounts.getBankName());
				if(!selectedChatOfAccounts.getAccountType().equalsIgnoreCase("bank"))
				{
					showBankFields=true;
					 win.setWidth("770px");
					  win.setHeight("470px");
				}
			  else
				{
					  showBankFields=false;
					  win.setWidth("770px");
					  win.setHeight("550px");
					 
				}
				if(selectedChatOfAccounts.getAccountType()!=null)
				{
				selectedAccountTypeModel=accountTypeList.get(accountTypeList.indexOf(selectedChatOfAccounts.getAccountType()));
				if(selectedChatOfAccounts.getAccountType().equalsIgnoreCase("AccountsReceivable") || selectedChatOfAccounts.getAccountType().equalsIgnoreCase("AccountsPayable"))
				{
					diabaleAccountType=true;
				}
				}
				else
					selectedAccountTypeModel=accountTypeList.get(0);
				selectedChatOfAccounts.setSelectedBankName(banknameList.get(indx));
				activeChatOfAccounts=selectedChatOfAccounts.getIsActive().equals("N");
				
				selectedChatOfAccounts.setSelectedSubOf(subOfList.get(0));
				
				List<AccountsModel> getSubOfCurentSelection=new ArrayList<AccountsModel>();
				getSubOfCurentSelection=chartOfAccountData.getSubOfCurrentSelection(selectedChatOfAccounts.getAccountName(),false);
				if(getSubOfCurentSelection.size()>=2)
				{
					showBalance=false;
				}
								
				if(selectedChatOfAccounts.getSubLevel()>0)
				{
					disableSubOf=false;
					selectedCheckBox=true;
					
					Pattern patternnew = Pattern.compile("(.*?):");
			    	Matcher matchernew = patternnew.matcher(selectedChatOfAccounts.getaCTLEVELSwithNO());
			    	if (matchernew.find())
			    	{
			    		Match=matchernew.group(1);
			    		
			    	}
				}
				
				for(AccountsModel model:subOfList)
				{
					if(model!=null)
					{
					String slectedAccountName =model.getAccountName().replaceAll("^\\s+", "").replaceAll("\\s+$", "");
					if(slectedAccountName.equalsIgnoreCase(Match))
					{
						selectedChatOfAccounts.setSelectedSubOf(model);
						tempModel=model;
						break;
					}
					}
				}
			}
			else
			{
				showBankFields=true;
				selectedChatOfAccounts=new AccountsModel();
				banknameList=chartOfAccountData.getBankNamesForChartofAccounts();
				selectedChatOfAccounts.setBankNameList(banknameList);
				selectedChatOfAccounts.setSelectedBankName(banknameList.get(0));
				accountTypeList=chartOfAccountData.getAllAccountTypeFromChartOfAccount();
				selectedAccountTypeModel=accountTypeList.get(0);
				selectedChatOfAccounts.setAccountTypedropDown(accountTypeList);
				subOfList=chartOfAccountData.getsubOfOnEditChartOFAccount(selectedAccountTypeModel);
				selectedChatOfAccounts.setSubOfdropDown(subOfList);
				selectedChatOfAccounts.setSelectedSubOf(subOfList.get(0));
				selectedChatOfAccounts.setName("");
				selectedChatOfAccounts.setDescription("");
				selectedChatOfAccounts.setAccountName("");
				selectedChatOfAccounts.setAccountNumber("");
				selectedChatOfAccounts.setAccountType("");
				selectedChatOfAccounts.setBalance(0.0);
				selectedChatOfAccounts.setNotes("");
				selectedChatOfAccounts.setBankAccountName("");
				selectedChatOfAccounts.setBankAcountNumber("");
				selectedChatOfAccounts.setBankName("");
				selectedChatOfAccounts.setBranchName("");
				selectedChatOfAccounts.setiBanNumber("");
				selectedChatOfAccounts.setBalance(0.0);
				creationdate=df.parse(sdf.format(c.getTime()));
				selectedChatOfAccounts.setBalaceDate(creationdate);
				selectedChatOfAccounts.setCreatedDate(creationdate);
				selectedChatOfAccounts.setNotes("");
				selectedChatOfAccounts.setIsActive("Y");
				activeChatOfAccounts=false;
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
				AccountsModel fx = (AccountsModel)ctx.getProperty().getValue();				
				String name = fx.getAccountName();
																		
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
	   @NotifyChange({"disableSubOf"})
	   public void doChecked()
	 	{
		  if (selectedCheckBox==true)
		  {
			  disableSubOf=false;
			  selectedChatOfAccounts.setSelectedSubOf(tempModel);
		  }
		  else
		  {
			  disableSubOf=true;
			  selectedChatOfAccounts.setSelectedSubOf(subOfList.get(0));
		  }
		  
	 	}
	
	   @Command
	   @NotifyChange({"lstItems","footer"})
	   public void updateChatOfAccounts(@BindingParam("cmp") Window x) throws ParseException
	   {
		 int result=0;
		 boolean exist=false;
		 boolean exceedsSubLevels=false;
		 String slectedAccountName="";
		 selectedChatOfAccounts.setSelectedAccountType(selectedAccountTypeModel);
		 
		 if(selectedChatOfAccounts.getName().equals(""))
		 {
			Messagebox.show("Please enter the Account Name","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
			return;
		 }
		 if(selectedChatOfAccounts.getAccountNumber().equals(""))
		 {
			 Messagebox.show("Please enter the Account Number","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
			return;
		 }
		 
		 
		 List<AccountsModel> chartOfAccountsFullNames=chartOfAccountData.getFullNameFromChartOfAccountForValidation();
		 
		 List<AccountsModel> chartOfAccountsAccountNumber=chartOfAccountData.getAccountNumberFromChartOfAccountForValidation();
		 
		 if(null!=selectedChatOfAccounts.getSelectedSubOf() && selectedChatOfAccounts.getSelectedSubOf().getAccountName().equalsIgnoreCase("none"))
		 {
			 selectedChatOfAccounts.setSelectedSubOf(null);
		 }
		 if(activeChatOfAccounts)
		 {
			 selectedChatOfAccounts.setIsActive("N");
		 }
		 else
		 {
			 selectedChatOfAccounts.setIsActive("Y");
		 }
		 if(selectedChatOfAccounts.getSelectedAccountType().equalsIgnoreCase("select") || selectedChatOfAccounts.getSelectedAccountType().equalsIgnoreCase(""))
		 {
			Messagebox.show("Please select the Account Type","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
			return;
		 }
		
		 if(selectedChatOfAccounts.getSelectedSubOf()!=null)
		{
			 slectedAccountName =selectedChatOfAccounts.getSelectedSubOf().getAccountName().replaceAll("^\\s+", "").replaceAll("\\s+$", "");
		 if(!selectedChatOfAccounts.getSelectedAccountType().equalsIgnoreCase(selectedChatOfAccounts.getSelectedSubOf().getAccountType()))
		 {
			 Messagebox.show("You have selected a Sub Account which has Different Account Type.","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
			return;
		 }
		}
		
		 if(selectedChatOfAccounts.getRec_No()>0)
		 {
			 //String chartOfAccountsAccountType=chartOfAccountData.getAccountTypeFromChartOfAccountForValidation(selectedChatOfAccounts.getAccountType());
			 for(AccountsModel NameValidation:chartOfAccountsFullNames)
				{
						if(selectedChatOfAccounts.getName().replaceAll("\\s","").equalsIgnoreCase(NameValidation.getName().replaceAll("\\s","")) && (NameValidation.getRec_No()!=selectedChatOfAccounts.getRec_No()))
						{
							 Messagebox.show("The name already exist.","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
				}
			
			
				for(AccountsModel numberValidation:chartOfAccountsAccountNumber)
				{
						if(selectedChatOfAccounts.getAccountNumber().replaceAll("\\s","").equalsIgnoreCase(numberValidation.getAccountNumber().replaceAll("\\s","")) && (numberValidation.getRec_No()!=selectedChatOfAccounts.getRec_No()))
						{
							 Messagebox.show("The number already exist","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
				}
			 
			 	List<Integer> currnetSubLevels=new ArrayList<Integer>();
			 	List<Integer> currnetSubLevelsForSub=new ArrayList<Integer>();
			 	currnetSubLevels.add(0);
			 	currnetSubLevelsForSub.add(0);
				List<AccountsModel> subOfcuurentSelctedRecord=new ArrayList<AccountsModel>();
				subOfcuurentSelctedRecord=chartOfAccountData.getSubOfCurrentSelection(selectedChatOfAccounts.getAccountName(),true);
				
				for(AccountsModel accountIrtn:subOfcuurentSelctedRecord)
				{
					if(accountIrtn.getAccountName().equalsIgnoreCase(slectedAccountName))
					{
						 exist=true;
					
						 break;
					}
					 currnetSubLevels.add(accountIrtn.getSubLevel());
					
				}
				
				
				Collections.max(currnetSubLevels);
				if(selectedChatOfAccounts.getSelectedSubOf()!=null)
				{
				List<AccountsModel> subOfcuurentSelctedRecordNew=new ArrayList<AccountsModel>();
				subOfcuurentSelctedRecordNew=chartOfAccountData.getSubOfCurrentSelection(selectedChatOfAccounts.getSelectedSubOf().getAccountName().replaceAll("^\\s+", "").replaceAll("\\s+$", ""),true);
				for(AccountsModel accountIrtnnew:subOfcuurentSelctedRecordNew)
				{
					currnetSubLevelsForSub.add(accountIrtnnew.getSubLevel());
				}
				
				if((Collections.max(currnetSubLevels)+Collections.max(currnetSubLevelsForSub))>4)
				{
					exceedsSubLevels=true;
				}
				}
				if(exceedsSubLevels)
				{
					Messagebox.show("This account cannot be used here.It will create too many levels of accounts.You can make only up to 5 levels of accounts.","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
				//	Clients.showNotification("You can make only up to 5 Sublevels !!");
					return;
				}
				else
				if(exist)
				{
					Messagebox.show("You cannot make an account a subaccount of itself.","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
					//Clients.showNotification("You cannot make an account a subaccount of itself.",Clients.NOTIFICATION_TYPE_INFO, null, "center", 6000);
					return;
				}
				else
				{
					creationdate=df.parse(sdf.format(c.getTime()));
					selectedChatOfAccounts.setModifiedDate(creationdate);
					result= chartOfAccountData.UpdateAccount(selectedChatOfAccounts);
				}
			 
		 }
		 else
		 {
			 
			 for(AccountsModel NameValidation:chartOfAccountsFullNames)
				{
						if(selectedChatOfAccounts.getName().replaceAll("\\s","").equalsIgnoreCase(NameValidation.getName().replaceAll("\\s","")))
						{
							 Messagebox.show("The name already exist.","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
				}
			
			
				for(AccountsModel numberValidation:chartOfAccountsAccountNumber)
				{
						if(selectedChatOfAccounts.getAccountNumber().replaceAll("\\s","").equalsIgnoreCase(numberValidation.getAccountNumber().replaceAll("\\s","")))
						{
							 Messagebox.show("The number already exist","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
							return;
						}
				}
			 	creationdate=df.parse(sdf.format(c.getTime()));
				selectedChatOfAccounts.setModifiedDate(creationdate);
				result= chartOfAccountData.addAccount(selectedChatOfAccounts);
		 }
				 
		if(result==1)
		{
			AccountsModel slectedRecNo=selectedChatOfAccounts;
			if(selectedChatOfAccounts.getRec_No()>0)
			{
		 Clients.showNotification("The Account Has Been Updated Successfully.",
		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}else
			{
				 Clients.showNotification("The Account Has Been Saved  Successfully.",
				            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			}
			
		 Map args = new HashMap();
		// args.put("type", "ChartOfAccount");	
		 args.put("slectedGridId", slectedRecNo);
		 BindUtils.postGlobalCommand(null, null, "refreshParentChart", args);
		 x.detach();
		}
		else
		Clients.showNotification("Error Saving Account.", Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);	
		 x.detach();
		
	   }
	

	/**
	 * @return the selectedChatOfAccounts
	 */
	public AccountsModel getSelectedChatOfAccounts() {
		return selectedChatOfAccounts;
	}

	/**
	 * @param selectedChatOfAccounts the selectedChatOfAccounts to set
	 */
	public void setSelectedChatOfAccounts(AccountsModel selectedChatOfAccounts) {
		this.selectedChatOfAccounts = selectedChatOfAccounts;
	}

	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	/**
	 * @return the activeChatOfAccounts
	 */
	public boolean isActiveChatOfAccounts() {
		return activeChatOfAccounts;
	}

	/**
	 * @param activeChatOfAccounts the activeChatOfAccounts to set
	 */
	public void setActiveChatOfAccounts(boolean activeChatOfAccounts) {
		
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && activeChatOfAccounts==false)
		{
			Clients.showNotification("You are not allowed to Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && activeChatOfAccounts==true)
		{
			Clients.showNotification("You are not allowed to In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.activeChatOfAccounts = activeChatOfAccounts;
		}
		
		
		
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
	 * @return the selectedAccountTypeModel
	 */
	public String getSelectedAccountTypeModel() {
		return selectedAccountTypeModel;
	}

	/**
	 * @param selectedAccountTypeModel the selectedAccountTypeModel to set
	 */
	  @NotifyChange({"subOfdropDown","showBankFields","selectedChatOfAccounts"})
	public void setSelectedAccountTypeModel(String selectedAccountTypeModel) {
		  Window win = (Window)Path.getComponent("/modalDialog");
		  if(selectedChatOfAccounts.getSubLevel()!=0)
		  {
			  Messagebox.show("You cannot change the type of subaccount.It must be the same type as its parent account.","Chart Of Account", Messagebox.OK , Messagebox.INFORMATION);
				//	Clients.showNotification("You can make only up to 5 Sublevels !!");
					return;
		  }
		  else{
			  subOfList=chartOfAccountData.getsubOfOnEditChartOFAccount(selectedAccountTypeModel);
			  selectedChatOfAccounts.setSubOfdropDown(subOfList);
			  selectedChatOfAccounts.setSelectedAccountType(selectedAccountTypeModel);
			  if(!selectedAccountTypeModel.equalsIgnoreCase("bank"))
			  {
				  showBankFields=true;
				  win.setWidth("750px");
				  win.setHeight("400px");
				  
			}
			 else
			{
				  showBankFields=false;
				  win.setWidth("750px");
				  win.setHeight("550px");
				  
			}		
		  }
		 
		  
		this.selectedAccountTypeModel = selectedAccountTypeModel;
	}

	/**
	 * @return the showBalance
	 */
	public boolean isShowBalance() {
		return showBalance;
	}

	/**
	 * @param showBalance the showBalance to set
	 */
	public void setShowBalance(boolean showBalance) {
		this.showBalance = showBalance;
	}

	/**
	 * @return the disableBalnceDate
	 */
	public boolean isDisableBalnceDate() {
		return disableBalnceDate;
	}

	/**
	 * @param disableBalnceDate the disableBalnceDate to set
	 */
	public void setDisableBalnceDate(boolean disableBalnceDate) {
		this.disableBalnceDate = disableBalnceDate;
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
	 * @return the selectedCheckBox
	 */
	public boolean getSelectedCheckBox() {
		return selectedCheckBox;
	}

	/**
	 * @param selectedCheckBox the selectedCheckBox to set
	 */
	public void setSelectedCheckBox(boolean selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
	}

	/**
	 * @return the diabaleAccountType
	 */
	public boolean isDiabaleAccountType() {
		return diabaleAccountType;
	}

	/**
	 * @param diabaleAccountType the diabaleAccountType to set
	 */
	public void setDiabaleAccountType(boolean diabaleAccountType) {
		this.diabaleAccountType = diabaleAccountType;
	}

	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==76)
			{
				companyRole=item;
				break;
			}
		}
	}	

	
}
