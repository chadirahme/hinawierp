package hba;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.MenuModel;
import model.CompSetupModel;
import model.QbListsModel;
import model.SalesRepModel;

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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditSalesRepViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SalesRepData bankData =new SalesRepData();
	HBAData data=new HBAData();
	private SalesRepModel selectedSalesRep;
	private boolean canSave;
	private boolean showBankFields;
	
	private List<QbListsModel> lstSalesRepName;
	private QbListsModel selectedSalesRepName;
	

	
	boolean useCommistion=false;
	boolean fixedCommistion=false;
	boolean variantCommistion=false;
	boolean amountCommistion=false;
	boolean salerepStatus=false;
	
	private List<String> lstAllVariant;
	private String selectedVariant;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();		
	Date creationdate;
	
	private CompSetupModel compSetup;
	
	private MenuModel companyRole;
	
	public EditSalesRepViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int salesrepKey=(Integer)map.get("salesRepKey");
			String type=(String)map.get("type");
			Window win = (Window)Path.getComponent("/salesRepListModalDialog");
			compSetup=bankData.getDefaultSetUpInfoForSalesRep();
			lstSalesRepName=bankData.fillSalesNameDropDown();
			getVarianatValues();
			if(type.equals("edit"))
			{
			canSave=true;
			win.setTitle("Edit Sales Rep Info");
			}
			else if(type.equalsIgnoreCase("Add"))
			{
				canSave=true;
				win.setTitle("Add New Sales Rep Info");
			}
			else
			{
				win.setTitle("View Sales Rep Info");
			}
			
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			
			
			if(salesrepKey>0)
			{
				selectedSalesRep=bankData.getsalesRepListByID(salesrepKey);
				if(selectedSalesRep.getIsActive().equalsIgnoreCase("y"))
				{
					salerepStatus=false;
				}
				else
				{
					salerepStatus=true;
				}
				
				if(null!= selectedSalesRep.getCommissionUsed() && selectedSalesRep.getCommissionUsed().equalsIgnoreCase("y"))
				{
					useCommistion=true;
				}
				
				if(null!=selectedSalesRep.getCommissionFlag())
				{
				if(selectedSalesRep.getCommissionFlag().equalsIgnoreCase("F"))
				{
					fixedCommistion=true;
				}
				else if(selectedSalesRep.getCommissionFlag().equalsIgnoreCase("V"))
				{
					variantCommistion=true;
					for(String model:lstAllVariant)
					{
						if(model.equalsIgnoreCase(Integer.toString((int) selectedSalesRep.getCommissionPercent())))
						{
							selectedSalesRep.setCommissionPercent(0);
							int index = Integer.parseInt(model);
							selectedVariant=lstAllVariant.get(index-1);
						}
					}
				}
				else if(selectedSalesRep.getCommissionFlag().equalsIgnoreCase("A")){
					amountCommistion=true;
				}
				}
				
				for(QbListsModel model:lstSalesRepName)
				{
					if(model!=null)
					{
					if(model.getRecNo()==selectedSalesRep.getQbListKey())
					{
						selectedSalesRepName=model;
						break;
					}
					}
				}
			
			}
			else
			{
				selectedSalesRep=new SalesRepModel();
				selectedSalesRep.setSlaesRepKey(0);
				selectedSalesRep.setSalesRepName("");
				selectedSalesRep.setSalesRepType("");
				selectedSalesRep.setCommissionPaidBy("");
				selectedSalesRep.setCommissionFlag("");
				selectedSalesRep.setCommissionPercent(0.0);
				selectedSalesRep.setCommissionUsed("");
				selectedSalesRep.setIntials("");
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditCustomerViewModel ----> init", ex);			
		}
	}
	public void getVarianatValues()
	{
		int i;
		lstAllVariant=new ArrayList<String>();
		for(i=1;i<=99;i++)
		{
			lstAllVariant.add(""+i+"");
		}
		selectedVariant=lstAllVariant.get(0);
			
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				SalesRepModel fx = (SalesRepModel)ctx.getProperty().getValue();				
				String name = fx.getSalesRepName();
																		
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

	   @SuppressWarnings("unchecked")
	@Command
	   @NotifyChange({"lstItems","footer"})
	   public void updateSalesRepList(@BindingParam("cmp") final Window x) throws ParseException
	   {
		 if(selectedSalesRepName==null)
		 {
			 Messagebox.show("Please Select The Sales Rep Name","Sales Rep List",Messagebox.OK , Messagebox.INFORMATION);
			 return;
		 }
		 
		 if(useCommistion==false)
		 {
		 Messagebox.show("You did not enter the commission details,Do you want to Continue?", "Confirm Save", Messagebox.YES | Messagebox.NO , Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener() {
			    public void onEvent(Event evt) throws InterruptedException {
			        if (evt.getName().equals("onYes")) 
			        {	
			        	 int result=0;
			        	 if(salerepStatus==true)
			    		 {
			    			 selectedSalesRep.setIsActive("N");
			    		 }
			    		 else
			    		 {
			    			 selectedSalesRep.setIsActive("Y");
			    		 }
			    		 if(useCommistion==true)
			    		 {
			    			 selectedSalesRep.setCommissionUsed("Y");
			    		 }
			    		 else
			    		 {
			    			 selectedSalesRep.setCommissionUsed("N");
			    		 }
			    		 
			    		 if(fixedCommistion==true)
			    		 {
			    			 selectedSalesRep.setCommissionFlag("F");
			    			 double d=Double.parseDouble(compSetup.getUseDefaultMaxCommission());
			    			 if(selectedSalesRep.getCommissionPercent()>d)
			    			 {
			    				 Messagebox.show("You can't assign a value grater than "+compSetup.getUseDefaultMaxCommission()+"","Bank List",Messagebox.OK , Messagebox.INFORMATION);
			    				 selectedSalesRep.setCommissionPercent(0);
			    				 return;
			    			 }
			    		 }
			    		 else if(variantCommistion==true)
			    		 {
			    			 selectedSalesRep.setCommissionFlag("V");
			    			 selectedSalesRep.setCommissionPercent(Double.parseDouble(selectedVariant));
			    			 double d=Double.parseDouble(compSetup.getUseDefaultMaxCommission());
			    			 if(Double.parseDouble(selectedVariant)>d)
			    			 {
			    				 Messagebox.show("You can't assign a value grater than "+compSetup.getUseDefaultMaxCommission()+"","Bank List",Messagebox.OK , Messagebox.INFORMATION);
			    				 selectedVariant=lstAllVariant.get(0);
			    				 selectedSalesRep.setCommissionPercent(0);
			    				 return;
			    			 }
			    			 
			    		 }
			    		 else if(amountCommistion==true)
			    		 {
			    			 selectedSalesRep.setCommissionFlag("A");
			    			 selectedSalesRep.setCommissionPercent(0);
			    		 }
			    		
			    		 if(selectedSalesRep.getSlaesRepKey()>0)
			    		 {
			    			 		result= bankData.updateSalesRepList(selectedSalesRep);
			    		 }
			    		 else
			    		 {
			    			 int tmpRecNo=bankData.getMaxSalesrepId();
			    			 result=bankData.inserSalesRepQuerry(selectedSalesRep,tmpRecNo);
			    		 }
			    		 
			    		if(result==1)
			    		{
			    		
			    			if(selectedSalesRep.getSlaesRepKey()>0)
			    			{
			    					Clients.showNotification("The Sales Rep Has Been Updated Successfully.",
			    		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			    			}else
			    			{
			    				 Clients.showNotification("The Sales Rep Has Been Saved  Successfully.",
			    				 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			    			}
			    		 Map args = new HashMap();
			    		 args.put("type", "SalesRep");		
			    		 BindUtils.postGlobalCommand(null, null, "refreshParentSalesRep", args);
			    		 x.detach();
			    		}
			    		else
			    			Clients.showNotification("Error at Sales Rep !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			    		x.detach();		        					        
			        }
			        else 
			        {				        	
			        	return;
			        }
			    }
			
			});
		 }
		 else{
			 int result=0;
        	 if(salerepStatus==true)
    		 {
    			 selectedSalesRep.setIsActive("N");
    		 }
    		 else
    		 {
    			 selectedSalesRep.setIsActive("Y");
    		 }
    		 if(useCommistion==true)
    		 {
    			 selectedSalesRep.setCommissionUsed("Y");
    		 }
    		 else
    		 {
    			 selectedSalesRep.setCommissionUsed("N");
    		 }
    		 
    		 if(fixedCommistion==true)
    		 {
    			 selectedSalesRep.setCommissionFlag("F");
    			 double d=Double.parseDouble(compSetup.getUseDefaultMaxCommission());
    			 if(selectedSalesRep.getCommissionPercent()>d)
    			 {
    				 Messagebox.show("You can't assign a value grater than "+compSetup.getUseDefaultMaxCommission()+"","Bank List",Messagebox.OK , Messagebox.INFORMATION);
    				 selectedSalesRep.setCommissionPercent(0);
    				 return;
    			 }
    		 }
    		 else if(variantCommistion==true)
    		 {
    			 selectedSalesRep.setCommissionFlag("V");
    			 selectedSalesRep.setCommissionPercent(Double.parseDouble(selectedVariant));
    			 double d=Double.parseDouble(compSetup.getUseDefaultMaxCommission());
    			 if(Double.parseDouble(selectedVariant)>d)
    			 {
    				 Messagebox.show("You can't assign a value grater than "+compSetup.getUseDefaultMaxCommission()+"","Bank List",Messagebox.OK , Messagebox.INFORMATION);
    				 selectedVariant=lstAllVariant.get(0);
    				 selectedSalesRep.setCommissionPercent(0);
    				 return;
    			 }
    			 
    		 }
    		 else if(amountCommistion==true)
    		 {
    			 selectedSalesRep.setCommissionFlag("A");
    			 selectedSalesRep.setCommissionPercent(0);
    		 }
    		
    		 if(selectedSalesRep.getSlaesRepKey()>0)
    		 {
    			 		result= bankData.updateSalesRepList(selectedSalesRep);
    		 }
    		 else
    		 {
    			 int tmpRecNo=bankData.getMaxSalesrepId();
    			 result=bankData.inserSalesRepQuerry(selectedSalesRep,tmpRecNo);
    		 }
    		 
    		if(result==1)
    		{
    		
    			if(selectedSalesRep.getSlaesRepKey()>0)
    			{
    					Clients.showNotification("The Sales Rep Has Been Updated Successfully.",
    		            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
    			}else
    			{
    				 Clients.showNotification("The Sales Rep Has Been Saved  Successfully.",
    				 Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
    			}
    		 Map args = new HashMap();
    		 args.put("type", "SalesRep");		
    		 BindUtils.postGlobalCommand(null, null, "refreshParentSalesRep", args);
    		 x.detach();
    		}
    		else
    			Clients.showNotification("Error at Sales Rep !!.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
    		x.detach();		        
		 }
		
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
	 * @return the selectedSalesRep
	 */
	public SalesRepModel getSelectedSalesRep() {
		return selectedSalesRep;
	}

	/**
	 * @param selectedSalesRep the selectedSalesRep to set
	 */
	public void setSelectedSalesRep(SalesRepModel selectedSalesRep) {
		this.selectedSalesRep = selectedSalesRep;
	}

	/**
	 * @return the lstSalesRepName
	 */
	public List<QbListsModel> getLstSalesRepName() {
		return lstSalesRepName;
	}

	/**
	 * @param lstSalesRepName the lstSalesRepName to set
	 */
	public void setLstSalesRepName(List<QbListsModel> lstSalesRepName) {
		this.lstSalesRepName = lstSalesRepName;
	}

	/**
	 * @return the selectedSalesRepName
	 */
	public QbListsModel getSelectedSalesRepName() {
		return selectedSalesRepName;
	}

	/**
	 * @param selectedSalesRepName the selectedSalesRepName to set
	 */
	 @NotifyChange({"selectedSalesRep"})
	public void setSelectedSalesRepName(QbListsModel selectedSalesRepName) {
		this.selectedSalesRepName = selectedSalesRepName;
		selectedSalesRep.setSalesRepType(selectedSalesRepName.getListType());
		selectedSalesRep.setQbListKey(selectedSalesRepName.getRecNo());
	}

	/**
	 * @return the useCommistion
	 */
	public boolean isUseCommistion() {
		return useCommistion;
	}

	/**
	 * @param useCommistion the useCommistion to set
	 */
	@NotifyChange({"variantCommistion","amountCommistion","fixedCommistion"})
	public void setUseCommistion(boolean useCommistion) {
		this.useCommistion = useCommistion;
		if(useCommistion==false)
		{
		fixedCommistion=false;
		variantCommistion=false;
		amountCommistion=false;
		}
	}

	/**
	 * @return the fixedCommistion
	 */
	public boolean isFixedCommistion() {
		return fixedCommistion;
	}

	/**
	 * @param fixedCommistion the fixedCommistion to set
	 */
	@NotifyChange({"variantCommistion","amountCommistion"})
	public void setFixedCommistion(boolean fixedCommistion) {
		this.fixedCommistion = fixedCommistion;
		 variantCommistion=false;
		 amountCommistion=false;
	}

	/**
	 * @return the variantCommistion
	 */
	public boolean isVariantCommistion() {
		return variantCommistion;
	}

	/**
	 * @param variantCommistion the variantCommistion to set
	 */
	@NotifyChange({"fixedCommistion","amountCommistion"})
	public void setVariantCommistion(boolean variantCommistion) {
		this.variantCommistion = variantCommistion;
		 fixedCommistion=false;
		 amountCommistion=false;
	}

	/**
	 * @return the amountCommistion
	 */
	public boolean isAmountCommistion() {
		return amountCommistion;
	}

	/**
	 * @param amountCommistion the amountCommistion to set
	 */
	@NotifyChange({"fixedCommistion","variantCommistion"})
	public void setAmountCommistion(boolean amountCommistion) {
		this.amountCommistion = amountCommistion;
		fixedCommistion=false;
		variantCommistion=false;
	}

	/**
	 * @return the lstAllVariant
	 */
	public List<String> getLstAllVariant() {
		return lstAllVariant;
	}

	/**
	 * @param lstAllVariant the lstAllVariant to set
	 */
	public void setLstAllVariant(List<String> lstAllVariant) {
		this.lstAllVariant = lstAllVariant;
	}

	/**
	 * @return the selectedVariant
	 */
	public String getSelectedVariant() {
		return selectedVariant;
	}

	/**
	 * @param selectedVariant the selectedVariant to set
	 */
	public void setSelectedVariant(String selectedVariant) {
		this.selectedVariant = selectedVariant;
	}
	/**
	 * @return the salerepStatus
	 */
	public boolean isSalerepStatus() {
		return salerepStatus;
	}
	/**
	 * @param salerepStatus the salerepStatus to set
	 */
	public void setSalerepStatus(boolean salerepStatus) {
		if(!companyRole.isAllowToActive() && !companyRole.isAllowToInActive())
		{
			Clients.showNotification("You are not allowed to Active or In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToActive() && salerepStatus==false)
		{
			Clients.showNotification("You are not allowed to Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else if(!companyRole.isAllowToInActive() && salerepStatus==true)
		{
			Clients.showNotification("You are not allowed to In-Active the status.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
		else
		{
			this.salerepStatus = salerepStatus;
		}
	}
	/**
	 * @return the compSetup
	 */
	public CompSetupModel getCompSetup() {
		return compSetup;
	}
	/**
	 * @param compSetup the compSetup to set
	 */
	public void setCompSetup(CompSetupModel compSetup) {
		this.compSetup = compSetup;
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
			if(item.getMenuid()==243)
			{
				companyRole=item;
				break;
			}
		}
	}


	
}
