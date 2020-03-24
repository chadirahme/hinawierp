package hba;

import hr.HRData;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import model.HRListValuesModel;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;

public class ProspectiveDetailsViewModel {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	ProspectiveData data = new ProspectiveData();
	private ProspectiveModel prospectiveModel;
	private Integer prospectivePriority;
	private boolean selectedCheckBox = false;
	private boolean disableSubOf = true;
	private String attFile4;
	List<HRListValuesModel> countries;
	List<HRListValuesModel> cities;
	HRListValuesModel selectedCountry;
	HRListValuesModel selectedCity;
	List<HRListValuesModel> howDid;
	HRListValuesModel selectedHowDid;
	List<HRListValuesModel> streets;
	HRListValuesModel selectedStreet;

	private HRData hrData = new HRData();
	private List<ProspectiveContactDetailsModel> lstProspectiveContact;
	private ProspectiveContactDetailsModel contactDetailsModel;
	List<ProspectiveContactDetailsModel> selectedProspectiveContact;

	public boolean isSelectedCheckBox() {
		return selectedCheckBox;
	}

	public void setSelectedCheckBox(boolean selectedCheckBox) {
		this.selectedCheckBox = selectedCheckBox;
	}

	private ProspectiveContactDetailsModel prospectiveContactModel;

	public ProspectiveDetailsViewModel() {
		try {
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			prospectiveModel = (ProspectiveModel) map.get("prospectives");
			prospectiveContactModel = data
					.getProspectiveDetails(prospectiveModel.getRecNo());
			countries = hrData.getHRListValues(2, "");// get country
			cities=hrData.getHRListValues(3, "");
			howDid=hrData.getHRListValues(140, "");
			streets=hrData.getHRListValues(51, "");
			for(HRListValuesModel listValuesModel:countries)
			{
				if(prospectiveModel.getCountry()!=null && prospectiveModel.getCountry()==listValuesModel.getListId())
				{
					selectedCountry=listValuesModel;
					break;
				}
			}
			
			for(HRListValuesModel model:cities)
			{
				if(prospectiveModel.getCity()!=null && prospectiveModel.getCity()==model.getListId())
				{
					selectedCity=model;
					break;
				}
			}
			
			for(HRListValuesModel model:howDid)
			{
				if(prospectiveModel.getHowdidYouknowus()!=null && prospectiveModel.getHowdidYouknowus()==model.getListId())
				{
					selectedHowDid=model;
					break;
				}
			}
			
			for(HRListValuesModel model:streets)
			{
				if(prospectiveModel.getStreet()!=null && prospectiveModel.getStreet()==model.getListId())
				{
					selectedStreet=model;
					break;
				}
			}

		} catch (Exception ex) {
			logger.error("error in ProspectiveDetailsViewModel---Init-->", ex);
		}
	}

	@Command
	public void updateProspectiveModel() {
		int res = 0;
		//use the attach in EditProspectiveViewModel
		res = data.updateProspectiveModel(this.getProspectiveModel(), null);

		if (res == 1)
			Messagebox.show("Prospective updated Successfully", "Information",
					Messagebox.OK, Messagebox.INFORMATION);
		else if (res == -1)
			Messagebox.show("An error Occured during update", "Information",
					Messagebox.OK, Messagebox.INFORMATION);

	}

	public ProspectiveModel getProspectiveModel() {
		return prospectiveModel;
	}

	public void setProspectiveModel(ProspectiveModel prospectiveModel) {
		this.prospectiveModel = prospectiveModel;
	}

	public ProspectiveContactDetailsModel getProspectiveContactModel() {
		return prospectiveContactModel;
	}

	public void setProspectiveContactModel(
			ProspectiveContactDetailsModel prospectiveContactModel) {
		this.prospectiveContactModel = prospectiveContactModel;
	}

	public Integer getProspectivePriority() {
		return prospectivePriority;
	}

	public void setProspectivePriority(Integer prospectivePriority) {
		this.prospectivePriority = prospectivePriority;
	}

	public boolean isDisableSubOf() {
		return disableSubOf;
	}

	public void setDisableSubOf(boolean disableSubOf) {
		this.disableSubOf = disableSubOf;
	}

	public String getAttFile4() {
		return attFile4;
	}

	public void setAttFile4(String attFile4) {
		this.attFile4 = attFile4;
	}

	public List<ProspectiveContactDetailsModel> getLstProspectiveContact() {
		return lstProspectiveContact;
	}

	public void setLstProspectiveContact(
			List<ProspectiveContactDetailsModel> lstProspectiveContact) {
		this.lstProspectiveContact = lstProspectiveContact;
	}

	public List<ProspectiveContactDetailsModel> getSelectedProspectiveContact() {
		return selectedProspectiveContact;
	}

	public void setSelectedProspectiveContact(
			List<ProspectiveContactDetailsModel> selectedProspectiveContact) {
		this.selectedProspectiveContact = selectedProspectiveContact;
	}

	public List<HRListValuesModel> getCountries() {
		return countries;
	}

	public void setCountries(List<HRListValuesModel> countries) {
		this.countries = countries;
	}

	public List<HRListValuesModel> getCities() {
		return cities;
	}

	public void setCities(List<HRListValuesModel> cities) {
		this.cities = cities;
	}

	public HRListValuesModel getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(HRListValuesModel selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public HRListValuesModel getSelectedCity() {
		return selectedCity;
	}

	public void setSelectedCity(HRListValuesModel selectedCity) {
		this.selectedCity = selectedCity;
	}

	public List<HRListValuesModel> getHowDid() {
		return howDid;
	}

	public void setHowDid(List<HRListValuesModel> howDid) {
		this.howDid = howDid;
	}

	public HRListValuesModel getSelectedHowDid() {
		return selectedHowDid;
	}

	public void setSelectedHowDid(HRListValuesModel selectedHowDid) {
		this.selectedHowDid = selectedHowDid;
	}

	public List<HRListValuesModel> getStreets() {
		return streets;
	}

	public void setStreets(List<HRListValuesModel> streets) {
		this.streets = streets;
	}

	public HRListValuesModel getSelectedStreet() {
		return selectedStreet;
	}

	public void setSelectedStreet(HRListValuesModel selectedStreet) {
		this.selectedStreet = selectedStreet;
	}

	public ProspectiveContactDetailsModel getContactDetailsModel() {
		return contactDetailsModel;
	}

	public void setContactDetailsModel(
			ProspectiveContactDetailsModel contactDetailsModel) {
		this.contactDetailsModel = contactDetailsModel;
	}

}
