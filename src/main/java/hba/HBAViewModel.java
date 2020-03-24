package hba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

import model.ClassModel;
import model.CustomerModel;
import model.DataFilter;
import model.PropertyModel;
import model.VehicleModel;
import model.VendorModel;

public class HBAViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();
	private List<CustomerModel> lstCustomers;
	private List<CustomerModel> lstAllCustomers;
	private CustomerModel selectedCustomer;
	
	private List<CustomerModel> lstOtherNames;
	private List<CustomerModel> lstAllOtherNames;
	private CustomerModel selectedOtherNames;
	
	private List<ClassModel> lstClass;
	private List<ClassModel> lstAllClass;
	private ClassModel selectedClass;
	
	private List<PropertyModel> lstProperty;
	private List<PropertyModel> lstAllProperty;
	private PropertyModel selectedProperty;
	
	private List<VehicleModel> lstVehicle;
	private List<VehicleModel> lstAllVehicle;
	private VehicleModel selectedVehicle;
	
	private List<String> lstStatus;
	private String selectedStatus;
	
	private DataFilter filter=new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	private boolean adminUser;

	private List<VendorModel> lstVendors;
	private List<VendorModel> lstAllVendors;
	private VendorModel selectedVendor;
	
	private  String listType;
	@Init
    public void init(@BindingParam("type") String type)
	{
		listType=type;
		
		FillStatusList();
		
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		
		if(listType.equals("customer"))
		{
		lstCustomers=data.getCustomerList("Y");
		lstAllCustomers=lstCustomers;
		if(lstCustomers.size()>0)
		selectedCustomer=lstCustomers.get(0);
		//lstPageSize.add(lstCustomers.size());
		}
		
		if(listType.equals("vendor"))
		{
		lstVendors=data.getVendorList();
		lstAllVendors=lstVendors;
		if(lstVendors.size()>0)
		selectedVendor=lstVendors.get(0);
		//lstPageSize.add(lstVendors.size());
		}				
		
		if(listType.equals("otherName"))
		{
			lstOtherNames=data.getOtherNameList();
			lstAllOtherNames=lstOtherNames;
		if(lstOtherNames.size()>0)
			selectedOtherNames=lstOtherNames.get(0);		
		}
		
		if(listType.equals("class"))
		{
			lstClass=data.fillClassList("");
			lstAllClass=lstClass;
		if(lstAllClass.size()>0)
			selectedClass=lstClass.get(0);
		}
		
		if(listType.equals("property"))
		{
			lstProperty=data.fillPropertyList();
			lstAllProperty=lstProperty;
		if(lstAllProperty.size()>0)
			selectedProperty=lstProperty.get(0);
		}
		
		if(listType.equals("vehicle"))
		{
			lstVehicle=data.fillVehicleList();
			lstAllVehicle=lstVehicle;
		if(lstAllVehicle.size()>0)
			selectedVehicle=lstVehicle.get(0);
		}
		
		selectedPageSize=lstPageSize.get(2);
		
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
		}
		//Messagebox.show(type);
    }
	
	public HBAViewModel()
	{
		
	}

	private void FillStatusList()
	{
	lstStatus=new ArrayList<String>();
	lstStatus.add("Active");
	lstStatus.add("Inactive");
	lstStatus.add("All");
	selectedStatus=lstStatus.get(0);
	}
	
	public List<CustomerModel> getLstCustomers() {
		return lstCustomers;
	}

	public void setLstCustomers(List<CustomerModel> lstCustomers) {
		this.lstCustomers = lstCustomers;
	}

	public CustomerModel getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(CustomerModel selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}
	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
	
	private List<CustomerModel> filterData()
	{
		lstCustomers=lstAllCustomers;
		List<CustomerModel>  lst=new ArrayList<CustomerModel>();
		for (Iterator<CustomerModel> i = lstCustomers.iterator(); i.hasNext();)
		{
			CustomerModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getArName().toLowerCase().contains(filter.getArname().toLowerCase())&&
					tmp.getPhone().toLowerCase().contains(filter.getPhone().toLowerCase())&&
					tmp.getFax().toLowerCase().contains(filter.getFax().toLowerCase())&&
					tmp.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase())&&
					tmp.getContact().toLowerCase().contains(filter.getContact().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	private List<VendorModel> filterVendorData()
	{
		lstVendors=lstAllVendors;
		List<VendorModel>  lst=new ArrayList<VendorModel>();
		for (Iterator<VendorModel> i = lstVendors.iterator(); i.hasNext();)
		{
			VendorModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getArName().toLowerCase().contains(filter.getArname().toLowerCase())&&
					tmp.getPhone().toLowerCase().contains(filter.getPhone().toLowerCase())&&
					tmp.getFax().toLowerCase().contains(filter.getFax().toLowerCase())&&
					tmp.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase())&&
					tmp.getContact().toLowerCase().contains(filter.getContact().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	private List<CustomerModel> filterOtherNameData()
	{
		lstOtherNames=lstAllOtherNames;
		List<CustomerModel>  lst=new ArrayList<CustomerModel>();
		for (Iterator<CustomerModel> i = lstOtherNames.iterator(); i.hasNext();)
		{
			CustomerModel tmp=i.next();				
			if(tmp.getName().toLowerCase().contains(filter.getName().toLowerCase())&&
					tmp.getArName().toLowerCase().contains(filter.getArname().toLowerCase())&&
					//tmp.getCreatDate().(filter.getCreatedDate()) &&
					tmp.getCompanyName().toLowerCase().contains(filter.getCompanyName().toLowerCase())&&
					tmp.getBillCountry().toLowerCase().contains(filter.getBillCountry().toLowerCase())&&
					tmp.getPhone().toLowerCase().contains(filter.getPhone().toLowerCase())&&
					tmp.getFax().toLowerCase().contains(filter.getFax().toLowerCase())&&
					tmp.getEmail().toLowerCase().contains(filter.getEmail().toLowerCase())&&
					tmp.getContact().toLowerCase().contains(filter.getContact().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	private List<ClassModel> filterClassData()
	{
		lstClass=lstAllClass;
		List<ClassModel>  lst=new ArrayList<ClassModel>();		
		for (Iterator<ClassModel> i = lstClass.iterator(); i.hasNext();)
		{
			ClassModel tmp=i.next();				
			if(tmp.getFullName().toLowerCase().contains(filter.getFullName().toLowerCase()))
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}
	private List<PropertyModel> filterPropertyData()
	{
		lstProperty=lstAllProperty;
		List<PropertyModel>  lst=new ArrayList<PropertyModel>();
		for (Iterator<PropertyModel> i = lstProperty.iterator(); i.hasNext();)
		{
			PropertyModel tmp=i.next();				
			if(tmp.getPropetyName().toLowerCase().contains(filter.getPropetyName().toLowerCase())&&
					//tmp.getCreatedDate().() //(filter.getCreatedDate()) &&
					tmp.getPropetyType().toLowerCase().contains(filter.getPropetyType().toLowerCase())&&
					tmp.getPlotNo().toLowerCase().contains(filter.getPlotNo().toLowerCase())&&
					tmp.getAge().toLowerCase().contains(filter.getAge().toLowerCase())&&
					tmp.getNoOfunits().toLowerCase().contains(filter.getNoOfunits().toLowerCase())&&					
					tmp.getOwner().toLowerCase().contains(filter.getOwner().toLowerCase())&&
					tmp.getWatchman().toLowerCase().contains(filter.getWatchman().toLowerCase())&&
					tmp.getWatchmanPhone().toLowerCase().contains(filter.getWatchmanPhone().toLowerCase())&&
					tmp.getCity().toLowerCase().contains(filter.getCity().toLowerCase())&&
					tmp.getStreet().toLowerCase().contains(filter.getStreet().toLowerCase())&&
					tmp.getCountry().toLowerCase().contains(filter.getCountry().toLowerCase())&&
					tmp.getConatactMaintanace().toLowerCase().contains(filter.getConatactMaintanace().toLowerCase())&&
					tmp.getLandLord().toLowerCase().contains(filter.getLandLord().toLowerCase())
					
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	private List<VehicleModel> filterVehicleData()
	{
		lstVehicle=lstAllVehicle;
		List<VehicleModel>  lst=new ArrayList<VehicleModel>();
		for (Iterator<VehicleModel> i = lstVehicle.iterator(); i.hasNext();)
		{
			VehicleModel tmp=i.next();				
			if(tmp.getChassisNumber().toLowerCase().contains(filter.getChassisNumber().toLowerCase())&&
					//tmp.getCreatedDate().() //(filter.getCreatedDate()) &&
					tmp.getVehicleType().toLowerCase().contains(filter.getVehicleType().toLowerCase())&&
					tmp.getRegNumber().toLowerCase().contains(filter.getRegNumber().toLowerCase())&&
					tmp.getBrand().toLowerCase().contains(filter.getBrand().toLowerCase())&&
					tmp.getType().toLowerCase().contains(filter.getType().toLowerCase())&&
					tmp.getOwnerName().toLowerCase().contains(filter.getOwnerName().toLowerCase())&&
					tmp.getPhone().toLowerCase().contains(filter.getPhone().toLowerCase())&&
					tmp.getMobile().toLowerCase().contains(filter.getMobile().toLowerCase())&&
					tmp.getAssetCode().toLowerCase().contains(filter.getAssetCode().toLowerCase())&&
					tmp.getAssetName().toLowerCase().contains(filter.getAssetName().toLowerCase())&&
					tmp.getColor().toLowerCase().contains(filter.getColor().toLowerCase())&&
					tmp.getPower().toLowerCase().contains(filter.getPower().toLowerCase())&&
					tmp.getOdoMeter().toLowerCase().contains(filter.getOdoMeter().toLowerCase())&&
					tmp.getYear().toLowerCase().contains(filter.getYear().toLowerCase())&&
					tmp.getEngine().toLowerCase().contains(filter.getEngine().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	    @Command
	    @NotifyChange({"lstCustomers","footer","lstVendors","lstOtherNames","lstClass","lstProperty","lstVehicle"})
	    public void changeFilter() 
	    {
	    	try
	    	{
		   if(listType.equals("customer"))
		   lstCustomers=filterData();
		   else if(listType.equalsIgnoreCase("otherName"))
			   lstOtherNames=filterOtherNameData();
		   else if(listType.equalsIgnoreCase("class"))
			   lstClass=filterClassData();
		 else if(listType.equalsIgnoreCase("property"))
			   lstProperty=filterPropertyData();
		   else if(listType.equalsIgnoreCase("vehicle"))
			   lstVehicle=filterVehicleData();
		   else
		   lstVendors=filterVendorData();
	    	}
	    	catch (Exception ex) {
				logger.error("error in HBAData---fillPropertyList-->" , ex);
			}
	    	
	    }
	   
	   @Command
	   @NotifyChange({"lstCustomers","footer"})
	   public void updateCustomer()
	   {
		data.UpdateCustomers(selectedCustomer.getCustkey(), selectedCustomer.getCompanyName(),selectedCustomer.getName(),selectedCustomer.getArName());   
		Messagebox.show("Customer is Updated..","Update Customer",Messagebox.OK , Messagebox.INFORMATION);
		
	   }
	   
	   @Command
	   public void viewCustomerCommand(@BindingParam("row") CustomerModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("custKey", row.getCustkey());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/hba/list/editcustomer.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HBAViewModel ----> viewCustomerCommand", ex);			
			}
	   }
	   
	   @GlobalCommand 
	  @NotifyChange({"lstCustomers","lstVendors"})
	    public void refreshParent(@BindingParam("type")String type)
			  {		
				 try
				  {
					 if(type.equals("customer"))
					 {
					 String status="";
					 if(selectedStatus.equals("Active"))
					  status="Y";
					  else if(selectedStatus.equals("Inactive"))
					  status="N";
					 
					lstCustomers=data.getCustomerList(status);
					lstAllCustomers=lstCustomers;
					 }
					 else if(type.equals("vendor"))
					 {
						lstVendors=data.getVendorList();
						lstAllVendors=lstVendors;
					 }
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in HBAViewModel ----> refreshParent", ex);			
					}
			  }
	   
	   @Command
	   public void editCustomerCommand(@BindingParam("row") CustomerModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("custKey", row.getCustkey());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editcustomer.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HBAViewModel ----> editCustomerCommand", ex);			
			}
	   }
	   
	   @Command
	   public void addCustomerCommand()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("custKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editcustomer.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HBAViewModel ----> addCustomerCommand", ex);			
			}
	   }
	   //edit vendor list
	   @Command
	   public void editVendorCommand(@BindingParam("row") VendorModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("vendKey", row.getVend_Key());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editvendor.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HBAViewModel ----> editVendorCommand", ex);			
			}
	   }
	   
	   @Command
	   public void viewVendorCommand(@BindingParam("row") VendorModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("vendKey", row.getVend_Key());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/hba/list/editvendor.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HBAViewModel ----> editVendorCommand", ex);			
			}
	   }
	   @Command
	   public void addVendorCommand()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("vendKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/hba/list/editvendor.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in HBAViewModel ----> editVendorCommand", ex);			
			}
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

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public List<VendorModel> getLstVendors() {
		return lstVendors;
	}

	public void setLstVendors(List<VendorModel> lstVendors) {
		this.lstVendors = lstVendors;
	}

	public VendorModel getSelectedVendor() {
		return selectedVendor;
	}

	public void setSelectedVendor(VendorModel selectedVendor) {
		this.selectedVendor = selectedVendor;
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

	@NotifyChange({"selectedPageSize"})	
	public void setSelectedAllPageSize(String selectedAllPageSize) 
	{
		this.selectedAllPageSize = selectedAllPageSize;
		if(selectedAllPageSize.equals("All"))
		{
			if(listType.equals("customer"))
			{
				selectedPageSize=lstCustomers.size();
			}
			if(listType.equals("vendor"))
			{
				selectedPageSize=lstVendors.size();
			}
			if(listType.equalsIgnoreCase("otherName"))
			{
				selectedPageSize=lstOtherNames.size();
			}
			if(listType.equalsIgnoreCase("class"))
			{
				selectedPageSize=lstClass.size();
			}
			if(listType.equalsIgnoreCase("property"))
			{
				selectedPageSize=lstProperty.size();
			}
			if(listType.equalsIgnoreCase("vehicle"))
			{
				selectedPageSize=lstVehicle.size();
			}
			
		}
		else
			selectedPageSize=Integer.parseInt(selectedAllPageSize);
	}

	public List<CustomerModel> getLstOtherNames() {
		return lstOtherNames;
	}

	public void setLstOtherNames(List<CustomerModel> lstOtherNames) {
		this.lstOtherNames = lstOtherNames;
	}

	public List<CustomerModel> getLstAllOtherNames() {
		return lstAllOtherNames;
	}

	public void setLstAllOtherNames(List<CustomerModel> lstAllOtherNames) {
		this.lstAllOtherNames = lstAllOtherNames;
	}

	public CustomerModel getSelectedOtherNames() {
		return selectedOtherNames;
	}

	public void setSelectedOtherNames(CustomerModel selectedOtherNames) {
		this.selectedOtherNames = selectedOtherNames;
	}
	
	public List<ClassModel> getLstClass() {
		return lstClass;
	}

	public void setLstClass(List<ClassModel> lstClass) {
		this.lstClass = lstClass;
	}

	public List<ClassModel> getLstAllClass() {
		return lstAllClass;
	}

	public void setLstAllClass(List<ClassModel> lstAllClass) {
		this.lstAllClass = lstAllClass;
	}

	public ClassModel getSelectedClass() {
		return selectedClass;
	}

	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}

	public List<PropertyModel> getLstProperty() {
		return lstProperty;
	}

	public void setLstProperty(List<PropertyModel> lstProperty) {
		this.lstProperty = lstProperty;
	}

	public List<PropertyModel> getLstAllProperty() {
		return lstAllProperty;
	}

	public void setLstAllProperty(List<PropertyModel> lstAllProperty) {
		this.lstAllProperty = lstAllProperty;
	}

	public PropertyModel getSelectedProperty() {
		return selectedProperty;
	}

	public void setSelectedProperty(PropertyModel selectedProperty) {
		this.selectedProperty = selectedProperty;
	}

	public List<VehicleModel> getLstVehicle() {
		return lstVehicle;
	}

	public void setLstVehicle(List<VehicleModel> lstVehicle) {
		this.lstVehicle = lstVehicle;
	}

	public List<VehicleModel> getLstAllVehicle() {
		return lstAllVehicle;
	}

	public void setLstAllVehicle(List<VehicleModel> lstAllVehicle) {
		this.lstAllVehicle = lstAllVehicle;
	}

	public VehicleModel getSelectedVehicle() {
		return selectedVehicle;
	}

	public void setSelectedVehicle(VehicleModel selectedVehicle) {
		this.selectedVehicle = selectedVehicle;
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

	@NotifyChange({"lstCustomers"})
	public void setSelectedStatus(String selectedStatus) 
	{
		this.selectedStatus = selectedStatus;
		String status="";
		if(selectedStatus.equals("Active"))
			status="Y";
		else if(selectedStatus.equals("Inactive"))
			status="N";
		lstCustomers=data.getCustomerList(status);
		lstAllCustomers=lstCustomers;
		
		
	}
}
