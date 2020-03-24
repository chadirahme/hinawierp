package fixedasset;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AccountsModel;
import model.FixedAssetModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class EditFixedAssetViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	FixedAssetData data=new FixedAssetData();
	private FixedAssetModel selectedFixedAsset;
	private boolean canSave;
	
	private List<HRListValuesModel> lstUnit;
	private HRListValuesModel selectedUnit;
	private List<HRListValuesModel> lstCategory;
	private HRListValuesModel selectedCategory;
	private List<HRListValuesModel> lstCountry;
	private HRListValuesModel selectedCountry;
	private List<HRListValuesModel> lstCity;
	private HRListValuesModel selectedCity;
	private List<HRListValuesModel> lstDepartment;
	private HRListValuesModel selectedDepartment;
	private List<HRListValuesModel> lstSection;
	private HRListValuesModel selectedSection;
	private List<AccountsModel> lstAccounts;
	private AccountsModel selectedAccount;
	
	
	public EditFixedAssetViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int assetId=0;//map.get("assetId");
			String type=(String)map.get("type");
			if(!type.equals("view"))
				canSave=true;
			
			lstUnit=data.getHRListValues(77, "",0);
			lstCategory=data.getHRListValues(70, "",0);
			lstCountry=data.getHRListValues(2, "",0);
			
			//lstCity=data.getHRListValues(3, "",0);
			lstDepartment=data.getHRListValues(6, "",0);
			lstSection=data.getHRListValues(49, "",0);
			lstAccounts=data.fillAccounts();
			
			if(assetId>0)
			{
				selectedFixedAsset=data.getFixedAssetById(assetId);
				fillAssetInfo();
			}
			else
			{
				selectedFixedAsset=new FixedAssetModel();
				selectedFixedAsset.setAssetCode("");
				selectedFixedAsset.setAssetName("");
				selectedFixedAsset.setAssetMasterDesc("");
				selectedFixedAsset.setUsed("New");
				selectedFixedAsset.setAssetOpenBalance(0);
				Calendar c = Calendar.getInstance();		
				selectedFixedAsset.setAssetOpenBalDate(c.getTime());
			}
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditFixedAssetViewModel ----> init", ex);			
		}
	}
	
	private void fillAssetInfo()
	{
		
		for (HRListValuesModel item : lstUnit) 
		{
		if(item.getListId()==selectedFixedAsset.getUnitId())
		{
			selectedUnit=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstCategory) 
		{
		if(item.getListId()==selectedFixedAsset.getCategoryId())
		{
			selectedCategory=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstCountry) 
		{
		if(item.getListId()==selectedFixedAsset.getCountryId())
		{
			selectedCountry=item;
			break;
		}
		
		}
		
		if(selectedCountry!=null)
		{
			lstCity=data.getHRListValues(3, "",selectedCountry.getListId());
		}
		if(lstCity!=null)
		{
		for (HRListValuesModel item : lstCity) 
		{
		if(item.getListId()==selectedFixedAsset.getCityId())
		{
			selectedCity=item;
			break;
		}
		
		}
		}
		
		
		for (HRListValuesModel item : lstDepartment) 
		{
		if(item.getListId()==selectedFixedAsset.getDepartmentId())
		{
			selectedDepartment=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstSection) 
		{
		if(item.getListId()==selectedFixedAsset.getSectionId())
		{
			selectedSection=item;
			break;
		}
		
		}
		
		for (AccountsModel item : lstAccounts) 
		{
		if(item.getRec_No()==selectedFixedAsset.getAccountNumber())
		{
			selectedAccount=item;
			break;
		}
		
		}
		
	}
	
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				//Form fx = (Form)ctx.getProperty().getValue();
				//get filed firstname of the form
				
				//FixedAssetModel fx= (FixedAssetModel) ctx.getProperty().getValue();
				
				String assetName =(String)ctx.getProperties("assetName")[0].getValue(); //(String)fx.getField("assetName");	//fx.getAssetName();//
				String assetCode=(String)ctx.getProperties("assetCode")[0].getValue(); //(String)fx.getField("assetCode"); //fx.getAssetCode();//
				
				if(Strings.isBlank(assetName) || Strings.isBlank(assetCode))
				{
					Clients.showNotification("Please fill all the required fields (*) !!");
					ctx.setInvalid();
				}
				
				if(selectedAccount==null)
				{
					Clients.showNotification("Please select account name !!");
					ctx.setInvalid();
				}
				
				if(selectedUnit==null)
				{
					Clients.showNotification("Please select unit !!");
					ctx.setInvalid();
				}
				
				if(selectedCategory==null)
				{
					Clients.showNotification("Please select category !!");
					ctx.setInvalid();
				}
			}
		};
	}
	
	@Command
	public void saveCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			if(selectedUnit!=null)
			selectedFixedAsset.setUnitId(selectedUnit.getListId());
			if(selectedCategory!=null)
			selectedFixedAsset.setCategoryId(selectedCategory.getListId());
			if(selectedCountry!=null)
			selectedFixedAsset.setCountryId(selectedCountry.getListId());
			if(selectedCity!=null)
				selectedFixedAsset.setCityId(selectedCity.getListId());
			if(selectedDepartment!=null)
				selectedFixedAsset.setDepartmentId(selectedDepartment.getListId());
			if(selectedSection!=null)
			selectedFixedAsset.setSectionId(selectedSection.getListId());
			if(selectedAccount!=null)
			selectedFixedAsset.setAccountNumber(selectedAccount.getRec_No());	
			
			int result=0;
			if(selectedFixedAsset.getAssetid()>0)
			result=data.editAsset(selectedFixedAsset);
			else
			result=data.editAsset(selectedFixedAsset);	
			
			if(result==1)
			{
			Clients.showNotification("Asset data is saved..");
			Map args = new HashMap();
			//args.put("compKey", selectedCompany.getCompKey());		
			BindUtils.postGlobalCommand(null, null, "refreshParent", args);
			}
			else
			{
			Clients.showNotification("Erro at save Asset !!");
			}
			comp.detach();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in FixedAssetViewModel ----> saveCommand", ex);			
		}
	}
	
	public FixedAssetModel getSelectedFixedAsset() {
		return selectedFixedAsset;
	}

	public void setSelectedFixedAsset(FixedAssetModel selectedFixedAsset) {
		this.selectedFixedAsset = selectedFixedAsset;
	}


	public boolean isCanSave() {
		return canSave;
	}


	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}


	public List<HRListValuesModel> getLstUnit() {
		return lstUnit;
	}


	public void setLstUnit(List<HRListValuesModel> lstUnit) {
		this.lstUnit = lstUnit;
	}


	public HRListValuesModel getSelectedUnit() {
		return selectedUnit;
	}


	public void setSelectedUnit(HRListValuesModel selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	public List<HRListValuesModel> getLstCategory() {
		return lstCategory;
	}

	public void setLstCategory(List<HRListValuesModel> lstCategory) {
		this.lstCategory = lstCategory;
	}

	public HRListValuesModel getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(HRListValuesModel selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<HRListValuesModel> getLstCountry() {
		return lstCountry;
	}

	public void setLstCountry(List<HRListValuesModel> lstCountry) {
		this.lstCountry = lstCountry;
	}

	public HRListValuesModel getSelectedCountry() {
		return selectedCountry;
	}

	@NotifyChange({"lstCity"})
	public void setSelectedCountry(HRListValuesModel selectedCountry) 
	{		
		this.selectedCountry = selectedCountry;
		lstCity=data.getHRListValues(3, "",selectedCountry.getListId());
		
	}

	public List<HRListValuesModel> getLstCity() {
		return lstCity;
	}

	public void setLstCity(List<HRListValuesModel> lstCity) {
		this.lstCity = lstCity;
	}

	public HRListValuesModel getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(HRListValuesModel selectedCity) {
		this.selectedCity = selectedCity;
	}

	public List<HRListValuesModel> getLstDepartment() {
		return lstDepartment;
	}

	public void setLstDepartment(List<HRListValuesModel> lstDepartment) {
		this.lstDepartment = lstDepartment;
	}

	public HRListValuesModel getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(HRListValuesModel selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public List<HRListValuesModel> getLstSection() {
		return lstSection;
	}

	public void setLstSection(List<HRListValuesModel> lstSection) {
		this.lstSection = lstSection;
	}

	public HRListValuesModel getSelectedSection() {
		return selectedSection;
	}

	public void setSelectedSection(HRListValuesModel selectedSection) {
		this.selectedSection = selectedSection;
	}

	public List<AccountsModel> getLstAccounts() {
		return lstAccounts;
	}

	public void setLstAccounts(List<AccountsModel> lstAccounts) {
		this.lstAccounts = lstAccounts;
	}

	public AccountsModel getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(AccountsModel selectedAccount) {
		this.selectedAccount = selectedAccount;
	}
}
