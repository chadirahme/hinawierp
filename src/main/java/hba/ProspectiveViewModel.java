package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import layout.MenuModel;
import model.CustomerModel;
import model.DataFilter;
import model.ProspectiveContactDetailsModel;
import model.ProspectiveModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class ProspectiveViewModel {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	ProspectiveData data = new ProspectiveData();
	private HBAData hbadata = new HBAData();
	List<ProspectiveModel> lstProspect = new ArrayList<ProspectiveModel>();
	ListModel<ProspectiveModel> lstSearch;// =new
											// SimpleListModel<ProspectiveModel>();
	ListModel<ProspectiveModel> lstAllSearch;
	ProspectiveModel prospectiveModel;
	private int selectedIndex;
	private ProspectiveModel selectedProspectiveResult;
	private String prospectiveTxt;
	List<ProspectiveModel> lstProspectiveSearchRes;
	List<ProspectiveModel> lstAllProspectiveSearchRes;
	List<ProspectiveContactDetailsModel> lstProspectiveContact;
	private Integer selectedPageSize;
	ProspectiveModel prospect = new ProspectiveModel();
	Map<String, ProspectiveModel> arg = new HashMap<String, ProspectiveModel>();
	Window window;
	private DataFilter filter = new DataFilter();

	private List<Integer> lstPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private int totalProspective = 0;
	private List<String> lstStatus;
	private String selectedIsActive;
	private Boolean selectedStatus;

	private Set<ProspectiveModel> selectedProspectiveEntities;
	
	
	private boolean adminUser;
	
	private MenuModel companyRole;

	public ProspectiveViewModel() {
		try {
			FillStatusList();

			lstPageSize = new ArrayList<Integer>();
			lstPageSize.add(15);
			lstPageSize.add(30);
			lstPageSize.add(50);

			lstAllPageSize = new ArrayList<String>();
			lstAllPageSize.add("15");
			lstAllPageSize.add("30");
			lstAllPageSize.add("50");
			lstAllPageSize.add("All");
			selectedAllPageSize = lstAllPageSize.get(2);

			
			lstProspectiveSearchRes = data.getProspectiveSearchRes("", 0);
			//lstProspectiveSearchRes.addAll(lstProspectiveSearchRes);
			if (lstProspectiveSearchRes.size() > 0)
				selectedProspectiveResult = lstProspectiveSearchRes.get(0);

			selectedPageSize = lstPageSize.get(2);

			lstAllProspectiveSearchRes = new ListModelList<ProspectiveModel>(
					lstProspectiveSearchRes);
			if (lstProspectiveSearchRes != null)
				totalProspective = lstProspectiveSearchRes.size();
			
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			
			
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			String otherForms = (String) map.get("allowSelect");
			if (otherForms!=null && otherForms.equalsIgnoreCase("false")) {
				lstProspectiveSearchRes.clear();
				lstProspectiveSearchRes.addAll((List) map.get("selectedProspective"));
				if(lstProspectiveSearchRes!=null && lstProspectiveSearchRes.size()>0)
				{
					lstAllProspectiveSearchRes.addAll(lstProspectiveSearchRes);
					if (lstProspectiveSearchRes.size() > 0)
						selectedProspectiveResult = lstProspectiveSearchRes.get(0);
				}
				
			} else {
				//i add this here 
				List<ProspectiveModel> lst=new ArrayList<ProspectiveModel>();
				lst=(List) map.get("selectedProspective");
				selectedProspectiveEntities=new HashSet<ProspectiveModel>();
				if(lst!=null)
				{
					for (ProspectiveModel item : lstProspectiveSearchRes) 
					{	
						for (ProspectiveModel item2 : lst)
						{
							if(item.getRecNo()==item2.getRecNo())
							{
								item.setSelected(true);		
								selectedProspectiveEntities.add(item);
							}
						}
					}
				}
			}
			int webUserID = 0;
			if (dbUser != null) {
				adminUser = dbUser.getFirstname().equals("admin");

				if (adminUser) {
					webUserID = 0;
				} else {
					webUserID = dbUser.getUserid();
				}
			}
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		} catch (Exception ex) {
			logger.error("error in ProspectiveViewModel---Init-->", ex);
		}
	}

	private void FillStatusList() {
		lstStatus = new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		lstStatus.add("Priority");
		selectedIsActive = lstStatus.get(0);
	}

	@NotifyChange({ "lstProspectiveSearchRes" })
	@Command
	public void getSearchResult() {
		String searchby = "";
		if (selectedProspectiveResult != null
				&& selectedProspectiveResult.getRecNo() > 0) {
			if (selectedIndex == 1 || selectedIndex == 2 || selectedIndex == 3)
				searchby = String.valueOf(selectedProspectiveResult.getRecNo());
		}
		if (selectedIndex == 4 || selectedIndex == 5)
			searchby = prospectiveTxt;

		lstProspectiveSearchRes = data.getProspectiveSearchRes(searchby,
				selectedIndex);

	}

	@Command
	@NotifyChange({ "lstProspect" })
	public void updateProspective(
			@BindingParam("prospectives") ProspectiveModel prospective) {

		this.setProspect(prospective);
		arg.put("prospectives", prospective);
		window = (Window) Executions.createComponents(
				"/hba/list/prospectiveDetails.zul", null, arg);
		window.doModal();

	}

	@Command
	@NotifyChange({ "lstProspectiveSearchRes", "totalProspective" })
	public void refreshCommand() {
		lstProspectiveSearchRes = data.getProspectiveSearchRes("", 0);
		lstAllProspectiveSearchRes = new ListModelList<ProspectiveModel>(
				lstProspectiveSearchRes);
		if (lstProspectiveSearchRes != null)
			totalProspective = lstProspectiveSearchRes.size();
	}

	@Command
	@NotifyChange({ "lstProspectiveSearchRes", "totalProspective" })
	public void changeFilter() {
		// lstAllSearch=lstSearch;
		List<ProspectiveModel> lsttmp = new ArrayList<ProspectiveModel>();
		List<ProspectiveModel> lst = (List<ProspectiveModel>) lstAllProspectiveSearchRes;
		for (Iterator<ProspectiveModel> i = lst.iterator(); i.hasNext();) {
			ProspectiveModel tmp = i.next();
			if (tmp.getName().toLowerCase()
					.contains(filter.getName().toLowerCase())
					&& tmp.getCreated_date().toLowerCase()
							.contains(filter.getCreated_date().toLowerCase())
					&& tmp.getCompanyName().toLowerCase()
							.contains(filter.getCompanyName().toLowerCase())
					&& tmp.getCategory().toLowerCase()
							.contains(filter.getCategory().toLowerCase())
					&& tmp.getEmail().toLowerCase()
							.contains(filter.getEmail().toLowerCase())
					&& tmp.getWebsite().toLowerCase()
							.contains(filter.getWebsite().toLowerCase())
					&& tmp.getAltPhone().toLowerCase()
							.contains(filter.getAltphone().toLowerCase())
					/*
					 * && tmp.getPhone().toLowerCase()
					 * .contains(filter.getPhone().toLowerCase())
					 */
					&& tmp.getOther1().toLowerCase()
							.contains(filter.getOther1().toLowerCase())
					&& tmp.getOther2().toLowerCase()
							.contains(filter.getOther2().toLowerCase())
					&& tmp.getContactPerson().toLowerCase()
							.contains(filter.getContactPerson().toLowerCase())
					&& tmp.getAdress1().toLowerCase()
							.contains(filter.getAdress1().toLowerCase()))
				lsttmp.add(tmp);
		}

		lstProspectiveSearchRes = new ListModelList<ProspectiveModel>(lsttmp);
		if (lstProspectiveSearchRes != null)
			totalProspective = lstProspectiveSearchRes.size();

	}

	public ProspectiveModel getProspect() {
		return prospect;
	}

	public void setProspect(ProspectiveModel prospect) {
		this.prospect = prospect;
	}

	public List<ProspectiveModel> getLstProspectiveSearchRes() {
		return lstProspectiveSearchRes;
	}

	public void setLstProspectiveSearchRes(
			List<ProspectiveModel> lstProspectiveSearchRes) {
		this.lstProspectiveSearchRes = lstProspectiveSearchRes;
	}

	public String getProspectiveTxt() {
		return prospectiveTxt;
	}

	public void setProspectiveTxt(String prospectiveTxt) {
		this.prospectiveTxt = prospectiveTxt;
	}

	public ProspectiveModel getSelectedProspectiveResult() {
		return selectedProspectiveResult;
	}

	@NotifyChange({ "lstProspectiveSearchRes" })
	public void setSelectedProspectiveResult(
			ProspectiveModel selectedProspectiveResult) {
		this.selectedProspectiveResult = selectedProspectiveResult;
		String searchby = "";
		if (selectedProspectiveResult.getRecNo() > 0) {
			if (selectedIndex == 1 || selectedIndex == 2 || selectedIndex == 3)
				searchby = String.valueOf(selectedProspectiveResult.getRecNo());
			else
				searchby = prospectiveTxt;
			lstProspectiveSearchRes = data.getProspectiveSearchRes(searchby,
					selectedIndex);
		}

	}

	public List<ProspectiveModel> getLstProspect() {
		return lstProspect;
	}

	public void setLstProspect(List<ProspectiveModel> lstProspect) {
		this.lstProspect = lstProspect;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	@NotifyChange({ "lstSearch" })
	public void setSelectedIndex(int selectedIndex) {
		try {
			this.selectedIndex = selectedIndex;
			List<ProspectiveModel> lstProspective = new ArrayList<ProspectiveModel>();
			if (this.getSelectedIndex() == 1)
				lstProspective = data.getFullName();

			else if (this.getSelectedIndex() == 2)
				lstProspective = data.getCategory();

			else if (this.getSelectedIndex() == 3)
				lstProspective = data.getHowYouKnow();

			// this.setLstProspect(lstProspective);
			lstSearch = new ListModelList<ProspectiveModel>(lstProspective);
			lstAllSearch = lstSearch;
			if (lstProspective.size() > 0) {
				// selectedProspectiveResult=lstProspective.get(0);
			}
		} catch (Exception ex) {
			logger.error("error in ProspectiveViewModel---setSelectedIndex-->",
					ex);
		}
	}

	public ListModel<ProspectiveModel> getLstSearch() {
		return lstSearch;
	}

	public void setLstSearch(ListModelList<ProspectiveModel> lstSearch) {
		this.lstSearch = lstSearch;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public int getTotalProspective() {
		return totalProspective;
	}

	public void setTotalProspective(int totalProspective) {
		this.totalProspective = totalProspective;
	}

	@Command
	public void addProspectiveCommand() {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("prospectiveKey", 0);
			arg.put("compKey", 0);
			arg.put("type", "add");
			Executions.createComponents("/hba/list/editProspective.zul", null,
					arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in ProspectiveViewModel ----> addProspectiveCommand",
					ex);
		}
	}

	@Command
	public void editProspectiveCommand(@BindingParam("row") ProspectiveModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("prospectiveKey", row.getRecNo());
			arg.put("compKey", 0);
			arg.put("type", "edit");
			Executions.createComponents("/hba/list/editProspective.zul", null,
					arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in ProspectiveViewModel ----> editProspectiveCommand",
					ex);
		}
	}

	@Command
	public void viewProspectiveCommand(@BindingParam("row") ProspectiveModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("prospectiveKey", row.getRecNo());
			arg.put("compKey", 0);
			arg.put("type", "view");
			Executions.createComponents("/hba/list/editProspective.zul", null,
					arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in ProspectiveViewModel ----> editProspectiveCommand",
					ex);
		}
	}

	@Command
	public void ProspectiveSendEmail(@BindingParam("row") ProspectiveModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("id", row.getRecNo());
			arg.put("feedBackKey", 0);
			arg.put("formType", "Prospective");
			arg.put("type", "OtherForms");
			Executions.createComponents("/crm/editCustomerFeedbackSend.zul",
					null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerFeebackSend ----> viewCustomerFeedback",
					ex);
		}
	}

	@Command
	public void addTask(@BindingParam("row") ProspectiveModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("taskKey", 0);
			arg.put("customerKey", row.getRecNo());
			arg.put("cutomerType", "P");// for prospective
			arg.put("type", "OtherForms");
			// arg.put("memo",row.getMemo());
			// arg.put("attchmnt",row.getLstAtt());
			Executions.createComponents("/hba/list/editTask.zul", null, arg);
		} catch (Exception ex) {
			logger.error("ERROR in prospectiveViewModel ----> addTask", ex);
		}
	}

	@Command
	public void selectProspectiveSendEmail(@BindingParam("cmp") Window comp) {
		List<Integer> lstProspKey = new ArrayList<Integer>();
		String prospKeys = "";
		if (selectedProspectiveEntities != null) {
			for (ProspectiveModel item : selectedProspectiveEntities) {
				lstProspKey.add(item.getRecNo());
			}

			for (Integer custKey : lstProspKey) {
				if (prospKeys.equals(""))
					prospKeys += String.valueOf(custKey);
				else
					prospKeys += "," + String.valueOf(custKey);
			}

		}

		else if (lstProspectiveSearchRes.size() == 1) {
			prospKeys = String.valueOf(lstProspectiveSearchRes.get(0)
					.getRecNo());
		}

		if (prospKeys.equals("")) {
			Messagebox.show("Please select Prospective!!", "Propsective",
					Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		Map args = new HashMap();
		args.put("myData", prospKeys);
		args.put("slectedProspObject", selectedProspectiveEntities);
		BindUtils.postGlobalCommand(null, null,
				"getProspectiveIDsForSendEmail", args);
		BindUtils.postGlobalCommand(null, null,
				"getProspectiveIdsForReminder", args);
		BindUtils.postGlobalCommand(null, null,
				"getProspectiveIDsForRemindersEmailSelection", args);
		
		comp.detach();
	}

	/*
	 * @Command
	 * 
	 * @NotifyChange({"lstCustomers"}) public void hideCutomersWithNoEmails() {
	 * try {
	 * 
	 * List<CustomerModel> tempCustList=new ArrayList<CustomerModel>();
	 * tempCustList.addAll(lstAllCustomers); if(hideEmail) {
	 * lstCustomers.clear(); for(CustomerModel customerModel:tempCustList) {
	 * if(customerModel.getEmail()!=null &&
	 * !customerModel.getEmail().equalsIgnoreCase("")) {
	 * lstCustomers.add(customerModel); } } lstAllCustomers.clear();
	 * lstAllCustomers.addAll(lstCustomers); } else { String status="";
	 * if(selectedStatus.equalsIgnoreCase("Active")) status="Y"; else
	 * if(selectedStatus.equalsIgnoreCase("InActive")) status="N";
	 * lstCustomers.clear(); lstCustomers=data.getCustomerList(status);
	 * lstAllCustomers.clear(); lstAllCustomers.addAll(lstCustomers); }
	 * 
	 * } catch (Exception ex) {
	 * logger.error("ERROR in CustomerViewModel ----> hideCutomersWithNoEmails",
	 * ex); } }
	 */

	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	@NotifyChange({ "selectedPageSize" })
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if (selectedAllPageSize.equalsIgnoreCase("All")) {
			selectedPageSize = lstProspectiveSearchRes.size();

		} else
			selectedPageSize = Integer.parseInt(selectedAllPageSize);
	}

	public ProspectiveModel getProspectiveModel() {
		return prospectiveModel;
	}

	public void setProspectiveModel(ProspectiveModel prospectiveModel) {
		this.prospectiveModel = prospectiveModel;
	}

	public List<String> getLstStatus() {
		return lstStatus;
	}

	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	public String getSelectedIsActive() {
		return selectedIsActive;

	}

	@NotifyChange({ "lstProspectiveSearchRes","totalProspective"})
	public void setSelectedIsActive(String selectedIsActive) {
		this.selectedIsActive = selectedIsActive;
		String isActive = "";
		if (selectedIsActive.equalsIgnoreCase("Active"))
			isActive = "Y";
		else if (selectedIsActive.equalsIgnoreCase("InActive"))
			isActive = "N";
		else if (selectedIsActive.equalsIgnoreCase("Priority"))
			isActive = "P";
		lstProspectiveSearchRes.clear();
		lstProspectiveSearchRes = data.getProspectiveSearchRes(isActive, 6);
		lstAllProspectiveSearchRes.clear();
		lstAllProspectiveSearchRes.addAll(lstProspectiveSearchRes);
		totalProspective = lstProspectiveSearchRes.size();
	}

	public Boolean getSelectedStatus() {
		return selectedStatus;
	}

	@NotifyChange({ "lstProspectiveSearchRes","totalProspective" })
	public void setSelectedStatus(Boolean selectedStatus) {
		this.selectedStatus = selectedStatus;
		String status = "";

		if (selectedStatus == true) {
			status = "C";
		} else {
			status = "A";
		}
		lstProspectiveSearchRes.clear();
		lstProspectiveSearchRes = data.getProspectiveSearchRes(status, 7);
		lstAllProspectiveSearchRes.clear();
		lstAllProspectiveSearchRes.addAll(lstProspectiveSearchRes);
		totalProspective = lstProspectiveSearchRes.size();
	}

	/**
	 * @return the selectedProspectiveEntities
	 */
	public Set<ProspectiveModel> getSelectedProspectiveEntities() {
		return selectedProspectiveEntities;
	}

	/**
	 * @param selectedProspectiveEntities
	 *            the selectedProspectiveEntities to set
	 */
	public void setSelectedProspectiveEntities(
			Set<ProspectiveModel> selectedProspectiveEntities) {
		this.selectedProspectiveEntities = selectedProspectiveEntities;
	}
	
	@Command
	public void AddQuotation(@BindingParam("row") ProspectiveModel row) {
		try {
			
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("cashInvoiceKey", 0+"");
			arg.put("type", "OtherForms");
			arg.put("ClientType", "P");
			arg.put("customerKey", row.getRecNo());
			
			Executions.createComponents("/hba/payments/editQuotation.zul",
					null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> AddQuotation ",
					ex);
		}
	}
	
	@GlobalCommand
	@NotifyChange({ "lstProspectiveSearchRes" })
	public void refreshProspectiveList(@BindingParam("type") String type) {
		try {
			lstProspectiveSearchRes.clear();
			lstProspectiveSearchRes = data.getProspectiveSearchRes("", 0);
			lstAllProspectiveSearchRes.clear();
			lstAllProspectiveSearchRes.addAll(lstProspectiveSearchRes);
		} catch (Exception ex) {
			logger.error("ERROR in ProspectiveViewModel ----> refreshProspectiveList", ex);
		}
	}
	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= hbadata.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==85)
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
	
	

}
