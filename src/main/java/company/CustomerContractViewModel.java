package company;

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

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class CustomerContractViewModel 
{
	//HBAData data = new HBAData();
	private Logger logger = Logger.getLogger(this.getClass());
	private List<CustomerModel> lstCustomers = new ArrayList<CustomerModel>();
	private List<CustomerModel> lstAllCustomers = new ArrayList<CustomerModel>();
	//private CustomerModel selectedCustomer;
	private boolean adminUser;
	private String footer;
	private String selectCount;
	private List<String> lstStatus;
	private String selectedStatus;

	private DataFilter filter = new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	
	private Set<CustomerModel> selectedCutomerEntities;

	@Init
	public void init(@BindingParam("type") String type) 
	{
		try
		{
			FillStatusList();
			lstPageSize = new ArrayList<Integer>();
			lstPageSize.add(15);
			lstPageSize.add(30);
			lstPageSize.add(50);
			selectedPageSize = lstPageSize.get(2);
			
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			String selectedReminderTypes = (String) map.get("selectedReminderTypes");
			selectedStatus=selectedReminderTypes;
			
			if(selectedReminderTypes.equals("Quotations"))
			{
//				List<Integer> lstCustKeys=data.getSalesRepNamesForReminder("Y");
//				lstCustomers = data.getCustomersQuoataionList(lstCustKeys);
//				lstAllCustomers.addAll(lstCustomers);
			}
			else
			{
//				lstCustomers = data.getCustomersContractExpiryList(selectedReminderTypes);
				//lstAllCustomers.addAll(lstCustomers);
			}
										
			//get the saved customers
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
			
			footer = "Total No. of Customer " + lstCustomers.size();
			selectCount="selected customers = " + selectedCutomerEntities.size();
		}
		catch (Exception ex)
		{
			logger.error("error in CustomerContractViewModel---init-->", ex);
		}
	}
	
	private void FillStatusList() 
	{
		lstStatus = new ArrayList<String>();
		lstStatus.add("All");
		lstStatus.add("Customer Contract Expiry Within 2 Months");
		lstStatus.add("Customer Contract Expired Before 2 Years");
		lstStatus.add("Customer Contract Expired More than 2 Years");
		lstStatus.add("Customer Balance");
		selectedStatus = lstStatus.get(0);	
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
					&& tmp.getFax().toLowerCase()
							.contains(filter.getFax().toLowerCase())
					&& tmp.getEmail().toLowerCase()
							.contains(filter.getEmail().toLowerCase())
					&& tmp.getContact().toLowerCase()
							.contains(filter.getContact().toLowerCase())
					&& tmp.getCustomerContactExpiryDateStr()
							.contains(filter.getIsActive().toLowerCase())) {
				lst.add(tmp);
			}
		}
		return lst;

	}
	
	@Command
	public void selectCutomersCommand(@BindingParam("cmp") Window comp)
	{
		List<Integer> lstCutomerKey = new ArrayList<Integer>();
		String custKeys = "";
		if (selectedCutomerEntities != null) 
		{
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

		else if (lstCustomers.size() == 1) 
		{
			custKeys = String.valueOf(lstCustomers.get(0).getCustkey());
		}

		if (custKeys.equals("")) {
			Messagebox.show("Please select Customers!!", "Customers",
					Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Map args = new HashMap();
		args.put("myData", custKeys);
		args.put("count", lstAllCustomers.size());
		//args.put("slectedCustomerObject", selectedCutomerEntities);
				
		BindUtils.postGlobalCommand(null, null,
				"getCustomerIdsForReminders", args);
		comp.detach();
	}
	
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}
	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}
	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}
	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	public List<String> getLstStatus() {
		return lstStatus;
	}

	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	@NotifyChange({ "lstCustomers", "footer" })
	public void setSelectedStatus(String selectedStatus) 
	{
		this.selectedStatus = selectedStatus;
		try {
			//lstCustomers = data.getCustomersContractExpiryList(selectedStatus);
			lstAllCustomers.clear();
			lstAllCustomers.addAll(lstCustomers);
			footer = "Total no. of Customer " + lstCustomers.size();

		} catch (Exception ex) {
			logger.error("error in CustomerViewModel---setSelectedStatus-->", ex);
		}
		
	}

	public List<CustomerModel> getLstCustomers() {
		return lstCustomers;
	}

	public void setLstCustomers(List<CustomerModel> lstCustomers) {
		this.lstCustomers = lstCustomers;
	}

	public Set<CustomerModel> getSelectedCutomerEntities() {
		return selectedCutomerEntities;
	}

	@NotifyChange({ "selectCount", "footer" })
	public void setSelectedCutomerEntities(Set<CustomerModel> selectedCutomerEntities) 
	{
		this.selectedCutomerEntities = selectedCutomerEntities;
		selectCount="selected customers = " + selectedCutomerEntities.size();
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
}
