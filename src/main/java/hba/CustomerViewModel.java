package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;
import layout.MenuModel;
import model.CustomerModel;
import model.DataFilter;

public class CustomerViewModel {

	HBAData data = new HBAData();
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
	private List<CustomerModel> lstAllCustomers = new ArrayList<CustomerModel>();
	private CustomerModel selectedCustomer;
	private boolean adminUser;
	private String footer;
	private List<String> lstStatus;
	private String selectedStatus;

	private DataFilter filter = new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;

	private Set<CustomerModel> selectedCutomerEntities;

	private boolean hideEmail = false;
	
	private MenuModel companyRole;
	
	private boolean allowToSelect=true;
	

	@Init
	public void init(@BindingParam("type") String type) {

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

		lstCustomers = data.getCustomerList("");
		lstAllCustomers.addAll(lstCustomers);
		if (lstCustomers.size() > 0)
			selectedCustomer = lstCustomers.get(0);

		selectedPageSize = lstPageSize.get(2);

		Session sess = Sessions.getCurrent();
		WebusersModel dbUser = (WebusersModel) sess
				.getAttribute("Authentication");
		if (dbUser != null) {
			adminUser = dbUser.getFirstname().equals("admin");
		}
		getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		String otherForms = (String) map.get("allowSelect");
		if (otherForms!=null && otherForms.equalsIgnoreCase("false")) {
			allowToSelect = false;
			lstCustomers.clear();
			lstCustomers.addAll((List) map.get("selectedCustomers"));
			if(lstCustomers!=null && lstCustomers.size()>0)
			{
				lstAllCustomers.addAll(lstCustomers);
				if (lstCustomers.size() > 0)
					selectedCustomer = lstCustomers.get(0);
			}
			
		} else {
			//i add this here 
			List<CustomerModel> lst=new ArrayList<CustomerModel>();
			lst=(List) map.get("selectedCustomers");
			selectedCutomerEntities=new HashSet<CustomerModel>();
			if(lst!=null)
			{
				for (CustomerModel item : lstCustomers) 
				{	
					for (CustomerModel item2 : lst)
					{
						if(item.getCustkey()==item2.getCustkey())
						{
							item.setSelected(true);
							selectedCutomerEntities.add(item);
						}
						
					}
				}
			}
			//logger.info(lst.size() + " name : "  + lst.get(0).getFullName());
			
			allowToSelect = true;
		}
		footer = "Total No. of Customer " + lstCustomers.size();
		// Messagebox.show(type);
	}

	private void FillStatusList() {
		lstStatus = new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Active");
		lstStatus.add("InActive");
		lstStatus.add("Priority");
		selectedStatus = lstStatus.get(0);
	}

	private List<CustomerModel> filterData() {
		lstCustomers.clear();
		lstCustomers.addAll(lstAllCustomers);
		List<CustomerModel> lst = new ArrayList<CustomerModel>();
		for (Iterator<CustomerModel> i = lstCustomers.iterator(); i.hasNext();) {
			CustomerModel tmp = i.next();
			if (tmp.getName().toLowerCase()
					.contains(filter.getName().toLowerCase())
					&& tmp.getArName().toLowerCase()
							.contains(filter.getArname().toLowerCase())
					&& tmp.getPhone().toLowerCase()
							.contains(filter.getPhone().toLowerCase())
					&& tmp.getAltphone().toLowerCase()
							.contains(filter.getMobile().toLowerCase())
					&& tmp.getFax().toLowerCase()
							.contains(filter.getFax().toLowerCase())
					&& tmp.getEmail().toLowerCase()
							.contains(filter.getEmail().toLowerCase())
					&& tmp.getContact().toLowerCase()
							.contains(filter.getContact().toLowerCase())
					&& tmp.getIsactive().toLowerCase()
							.contains(filter.getIsActive().toLowerCase())
					&& tmp.getNote().toLowerCase()
							.contains(filter.getNote().toLowerCase())		
					) 
			{
				lst.add(tmp);
			}
		}
		return lst;

	}

	@Command
	@NotifyChange({ "lstCustomers", "footer" })
	public void changeFilter() {
		try {
			lstCustomers = filterData();
			footer = "Total no. of Customer " + lstCustomers.size();

		} catch (Exception ex) {
			logger.error("error in CustomerViewModel---changeFilter-->", ex);
		}

	}

	@Command
	public void viewCustomerCommand(@BindingParam("row") CustomerModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("custKey", row.getCustkey());
			arg.put("compKey", 0);
			arg.put("type", "view");
			Executions
					.createComponents("/hba/list/editcustomer.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> viewCustomerCommand", ex);
		}
	}

	@GlobalCommand
	@NotifyChange({ "lstCustomers" })
	public void refreshParent(@BindingParam("type") String type) {
		try {
			String status = "";
			if (selectedStatus.equals("Active"))
				status = "Y";
			else if (selectedStatus.equals("Inactive"))
				status = "N";
			else
				status = "";
			lstCustomers.clear();
			lstCustomers = data.getCustomerList(status);
			lstAllCustomers.clear();
			lstAllCustomers.addAll(lstCustomers);
		} catch (Exception ex) {
			logger.error("ERROR in CustomerViewModel ----> refreshParent", ex);
		}
	}

	@Command
	public void editCustomerCommand(@BindingParam("row") CustomerModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("custKey", row.getCustkey());
			arg.put("compKey", 0);
			arg.put("type", "edit");
			Executions
					.createComponents("/hba/list/editcustomer.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> editCustomerCommand", ex);
		}
	}

	@Command
	public void addCustomerCommand() {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("custKey", 0);
			arg.put("compKey", 0);
			arg.put("type", "add");
			Executions
					.createComponents("/hba/list/editcustomer.zul", null, arg);
		} catch (Exception ex) {
			logger.error("ERROR in CustomerViewModel ----> addCustomerCommand",
					ex);
		}
	}

	@Command
	public void resetCustomer() {
		try {
			Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
			Center center = bl.getCenter();
			Tabbox tabbox = (Tabbox) center.getFellow("mainContentTabbox");
			tabbox.getSelectedPanel().getLastChild().invalidate();
		} catch (Exception ex) {
			logger.error("ERROR in CustomerViewModel ----> resetCustomer", ex);
		}
	}

	@Command
	public void selectTaskCutomers(@BindingParam("cmp") Window comp) {
		List<Integer> lstCutomerKey = new ArrayList<Integer>();
		String custKeys = "";
		if (selectedCutomerEntities != null) {
			for (CustomerModel item : selectedCutomerEntities) {
				lstCutomerKey.add(item.getCustkey());
			}

			for (Integer custKey : lstCutomerKey) {
				if (custKeys.equals(""))
					custKeys += String.valueOf(custKey);
				else
					custKeys += "," + String.valueOf(custKey);
			}

		}

		else if (lstCustomers.size() == 1) {
			custKeys = String.valueOf(lstCustomers.get(0).getCustkey());
		}

		if (custKeys.equals("")) {
			Messagebox.show("Please select Customers!!", "Customers",
					Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		Map args = new HashMap();
		args.put("myData", custKeys);
		args.put("slectedCustomerObject", selectedCutomerEntities);
		BindUtils.postGlobalCommand(null, null,
				"getCutomerIDsFormGroupOfTasks", args);
		BindUtils.postGlobalCommand(null, null,
				"getCutomerIDsForFeedbackSendTocustomer", args);
		BindUtils.postGlobalCommand(null, null,
				"getCutomerIDsForCustomerReminderEmailSelection", args);
		BindUtils.postGlobalCommand(null, null,
				"getCustomerIdsForReminders", args);
		comp.detach();
	}

	@Command
	@NotifyChange({ "lstCustomers" })
	public void hideCutomersWithNoEmails() {
		try {

			List<CustomerModel> tempCustList = new ArrayList<CustomerModel>();
			tempCustList.addAll(lstAllCustomers);
			if (hideEmail) {
				lstCustomers.clear();
				for (CustomerModel customerModel : tempCustList) {
					if (customerModel.getEmail() != null
							&& !customerModel.getEmail().equalsIgnoreCase("")) {
						lstCustomers.add(customerModel);
					}
				}
				lstAllCustomers.clear();
				lstAllCustomers.addAll(lstCustomers);
			} else {
				String status = "";
				if (selectedStatus.equalsIgnoreCase("Active"))
					status = "Y";
				else if (selectedStatus.equalsIgnoreCase("InActive"))
					status = "N";
				lstCustomers.clear();
				lstCustomers = data.getCustomerList(status);
				lstAllCustomers.clear();
				lstAllCustomers.addAll(lstCustomers);
			}

		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> hideCutomersWithNoEmails",
					ex);
		}
	}

	/**
	 * @return the lstCustomers
	 */
	public List<CustomerModel> getLstCustomers() {
		return lstCustomers;
	}

	/**
	 * @param lstCustomers
	 *            the lstCustomers to set
	 */
	public void setLstCustomers(List<CustomerModel> lstCustomers) {
		this.lstCustomers = lstCustomers;
	}

	/**
	 * @return the lstAllCustomers
	 */
	public List<CustomerModel> getLstAllCustomers() {
		return lstAllCustomers;
	}

	/**
	 * @param lstAllCustomers
	 *            the lstAllCustomers to set
	 */
	public void setLstAllCustomers(List<CustomerModel> lstAllCustomers) {
		this.lstAllCustomers = lstAllCustomers;
	}

	/**
	 * @return the selectedCustomer
	 */
	public CustomerModel getSelectedCustomer() {
		return selectedCustomer;
	}

	/**
	 * @param selectedCustomer
	 *            the selectedCustomer to set
	 */
	public void setSelectedCustomer(CustomerModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	/**
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser
	 *            the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * @return the lstStatus
	 */
	public List<String> getLstStatus() {
		return lstStatus;
	}

	/**
	 * @param lstStatus
	 *            the lstStatus to set
	 */
	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;

	}

	/**
	 * @param selectedStatus
	 *            the selectedStatus to set
	 */
	@NotifyChange({ "lstCustomers" })
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		String status = "";
		if (selectedStatus.equalsIgnoreCase("Active"))
			status = "Y";
		else if (selectedStatus.equalsIgnoreCase("InActive"))
			status = "N";
		else if (selectedStatus.equalsIgnoreCase("Priority"))
			status = "P";
		lstCustomers.clear();
		lstCustomers = data.getCustomerList(status);
		lstAllCustomers.clear();
		lstAllCustomers.addAll(lstCustomers);
	}

	/**
	 * @return the lstPageSize
	 */
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	/**
	 * @param lstPageSize
	 *            the lstPageSize to set
	 */
	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	/**
	 * @return the selectedPageSize
	 */
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	/**
	 * @param selectedPageSize
	 *            the selectedPageSize to set
	 */
	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	/**
	 * @return the lstAllPageSize
	 */
	public List<String> getLstAllPageSize() {
		return lstAllPageSize;
	}

	/**
	 * @param lstAllPageSize
	 *            the lstAllPageSize to set
	 */
	public void setLstAllPageSize(List<String> lstAllPageSize) {
		this.lstAllPageSize = lstAllPageSize;
	}

	/**
	 * @return the selectedAllPageSize
	 */
	public String getSelectedAllPageSize() {
		return selectedAllPageSize;
	}

	/**
	 * @param selectedAllPageSize
	 *            the selectedAllPageSize to set
	 */
	@NotifyChange({ "selectedPageSize" })
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
		if (selectedAllPageSize.equalsIgnoreCase("All")) {
			selectedPageSize = lstCustomers.size();

		} else
			selectedPageSize = Integer.parseInt(selectedAllPageSize);
	}

	/**
	 * @return the filter
	 */
	public DataFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return the selectedCutomerEntities
	 */
	public Set<CustomerModel> getSelectedCutomerEntities() {
		return selectedCutomerEntities;
	}

	/**
	 * @param selectedCutomerEntities
	 *            the selectedCutomerEntities to set
	 */
	public void setSelectedCutomerEntities(
			Set<CustomerModel> selectedCutomerEntities) {
		this.selectedCutomerEntities = selectedCutomerEntities;
	}

	/**
	 * @return the hideEmail
	 */
	public boolean isHideEmail() {
		return hideEmail;
	}

	/**
	 * @param hideEmail
	 *            the hideEmail to set
	 */
	public void setHideEmail(boolean hideEmail) {
		this.hideEmail = hideEmail;
	}
	
	@Command
	public void CustomerSendEmail(@BindingParam("row") CustomerModel row) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("id", row.getCustkey());
			arg.put("feedBackKey", 0);
			arg.put("formType", "Customer");
			arg.put("type", "OtherForms");
			Executions.createComponents("/crm/editCustomerFeedbackSend.zul",
					null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> CustomerSendEmail",
					ex);
		}
	}

	@Command
	public void addTask(@BindingParam("row") CustomerModel row) {
		try {
			
			if(row.getIsactive()=="InActive")
			{
				Clients.showNotification("Can't Create a Task for InActive Customer",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				return;
			}
			else
			{
			
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("taskKey", 0);
			arg.put("customerKey", row.getCustkey());
			arg.put("cutomerType", "Customer");
			arg.put("type", "OtherForms");
			// arg.put("memo",row.getMemo());
			// arg.put("attchmnt",row.getLstAtt());
			Executions.createComponents("/hba/list/editTask.zul", null, arg);
			}
		} catch (Exception ex) {
			logger.error("ERROR in CustomerViewModel ----> addTask", ex);
		}
	}
	
	@Command
	public void AddQuotation(@BindingParam("row") CustomerModel row) {
		try {
			if(row.getIsactive()=="InActive")
			{
				Clients.showNotification("Can't Create a Quotation for InActive Customer",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				return;
			}
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("cashInvoiceKey", 0+"");
			arg.put("type", "OtherForms");
			arg.put("ClientType", "C");
			arg.put("customerKey", row.getCustkey());
			
			Executions.createComponents("/hba/payments/editQuotation.zul",
					null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> AddQuotation ",
					ex);
		}
	}
	
	@Command
	public void CashChequeReceiptVoucher(@BindingParam("row") CustomerModel row) {
		try {
			if(row.getIsactive()=="InActive")
			{
				Clients.showNotification("Can't Create a Receipt Voucher for InActive Customer",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				return;
			}
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("reciptVoiucherKey", 0+"");
			arg.put("type", "OtherForms");
			arg.put("customerKey", row.getCustkey());
			arg.put("canModify", true);
			arg.put("canView", true);
			arg.put("canPrint", true);
			arg.put("canCreate", true);
			Executions.createComponents(
					"/hba/payments/editReciptVoucher.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> CashChequeReceiptVoucher ",
					ex);
		}
	}
	
	@Command
	public void AddCashInvoice(@BindingParam("row") CustomerModel row) {
		try {
			if(row.getIsactive()=="InActive")
			{
				Clients.showNotification("Can't Create a Cash Invoice for InActive Customer",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				return;
			}
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("cashInvoiceKey", 0);
			arg.put("type", "OtherForms");
			arg.put("customerKey", row.getCustkey());
			arg.put("canModify", true);
			arg.put("canView", true);
			arg.put("canPrint", true);
			arg.put("canCreate", true);
			Executions.createComponents(
					"/hba/payments/editCashInvoice.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> AddCashInvoice ",
					ex);
		}
	}
	
	@Command
	public void AddCreditInvoice(@BindingParam("row") CustomerModel row) {
		try {
			if(row.getIsactive()=="InActive")
			{
				Clients.showNotification("Can't Create a Credit Invoice for InActive Customer",
						Clients.NOTIFICATION_TYPE_INFO, null, "middle_center",
						10000, true);
				return;
			}
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("creditInvoiceKey", 0);
			arg.put("type", "OtherForms");
			arg.put("customerKey", row.getCustkey());
			arg.put("canModify", true);
			arg.put("canView", true);
			arg.put("canPrint", true);
			arg.put("canCreate", true);
			Executions.createComponents(
					"/hba/payments/editCreditInvoice.zul", null, arg);
		} catch (Exception ex) {
			logger.error(
					"ERROR in CustomerViewModel ----> AddCashInvoice ",
					ex);
		}
	}
	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==73)
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

	/**
	 * @return the allowToSelect
	 */
	public boolean isAllowToSelect() {
		return allowToSelect;
	}

	/**
	 * @param allowToSelect the allowToSelect to set
	 */
	public void setAllowToSelect(boolean allowToSelect) {
		this.allowToSelect = allowToSelect;
	}
	
	

}
