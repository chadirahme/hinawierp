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
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;
import layout.MenuModel;
import model.ClassModel;
import model.CustomerModel;
import model.DataFilter;
import model.VendorModel;

public class ClassViewModel {
	private Logger logger = Logger.getLogger(this.getClass());
	ClassData data=new ClassData();
	HBAData dataHba=new HBAData();
	private boolean adminUser;
	
	private List<ClassModel> lstClass;
	private List<ClassModel> lstAllClass;
	private ClassModel selectedClass;
	
	private DataFilter filter=new DataFilter();
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	
	private List<String> lstStatus;
	private String selectedStatus;
	
	private MenuModel companyRole;
	
	@Init
    public void init()
	{
		
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
		
		lstClass=data.fillClassList("","");
		lstAllClass=lstClass;
		if(lstAllClass.size()>0)
			selectedClass=lstClass.get(0);
		selectedPageSize=lstPageSize.get(2);
		Session sess = Sessions.getCurrent();
		WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
		}
		getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
		//Messagebox.show(type);
    }
	
	public ClassViewModel()
	{
		
	}

	private void FillStatusList()
	{
	lstStatus=new ArrayList<String>();
	lstStatus.add("All");
	lstStatus.add("Active");
	lstStatus.add("InActive");
	selectedStatus=lstStatus.get(0);
	}
	
	
	private List<ClassModel> filterClassData()
	{
		lstClass=lstAllClass;
		List<ClassModel>  lst=new ArrayList<ClassModel>();		
		for (Iterator<ClassModel> i = lstClass.iterator(); i.hasNext();)
		{
			ClassModel tmp=i.next();				
			if(tmp.getFullName().toLowerCase().contains(filter.getFullName().toLowerCase()) &&
					tmp.getIsActive().toLowerCase().contains(filter.getIsActive().toLowerCase())
					)
			{
				lst.add(tmp);
			}
		}
		return lst;		
	}

	
	@Command
    @NotifyChange({"footer","lstClass"})
    public void changeFilter() 
    {
    	try
    	{
		   lstClass=filterClassData();
	    	}
    	catch (Exception ex) {
			logger.error("error in ClassViewModel---fillPropertyList-->" , ex);
		}
    	
    }
	
	 @Command
	   @NotifyChange({"lstClass","footer"})
	   public void updateCustomer()
	   {
	//	data.UpdateCustomers(selectedCustomer.getCustkey(), selectedCustomer.getCompanyName(),selectedCustomer.getName(),selectedCustomer.getArName());   
		Messagebox.show("Customer is Updated..","Update Customer",Messagebox.OK , Messagebox.INFORMATION);
		
	   }
	 
	  @GlobalCommand 
	  @NotifyChange({"lstClass"})
	    public void refreshParentClass(@BindingParam("type")String type)
			  {		
				 try
				  {
					lstClass=data.fillClassList("","");
					lstAllClass=lstClass;
					if(lstAllClass.size()>0)
						selectedClass=lstClass.get(0);
				  }
				 catch (Exception ex)
					{	
					logger.error("ERROR in ClassViewModel ----> refreshParentClass", ex);			
					}
			  }
	  
	  
	  @Command
	   public void editClassCommand(@BindingParam("row") ClassModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("classKey", row.getClass_Key());
			   arg.put("compKey",0);
			   arg.put("type","edit");
			   Executions.createComponents("/lists/editClassList.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ClassViewModel ----> editClassCommand", ex);			
			}
	   }
	   
	   @Command
	   public void addClassCommand()
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("classKey", 0);
			   arg.put("compKey",0);
			   arg.put("type","Add");
			   Executions.createComponents("/lists/editClassList.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ClassViewModel ----> addClassCommand", ex);			
			}
	   }
	   
	   @Command
	   public void resetClass()
	   {
		   try
		   {
			   Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
				 Center center = bl.getCenter();
				 Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
				 tabbox.getSelectedPanel().getLastChild().invalidate();
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ClassViewModel ----> resetClass", ex);			
			}
	   }
	   
	   @Command
	   public void viewClassCommand(@BindingParam("row") ClassModel row)
	   {
		   try
		   {
			   Map<String,Object> arg = new HashMap<String,Object>();
			   arg.put("classKey", row.getClass_Key());
			   arg.put("compKey",0);
			   arg.put("type","view");
			   Executions.createComponents("/lists/editClassList.zul", null,arg);
		   }
		   catch (Exception ex)
			{	
				logger.error("ERROR in ClassViewModel ----> viewClassCommand", ex);			
			}
	   }
	  
	/**
	 * @return the adminUser
	 */
	public boolean isAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * @return the lstClass
	 */
	public List<ClassModel> getLstClass() {
		return lstClass;
	}

	/**
	 * @param lstClass the lstClass to set
	 */
	public void setLstClass(List<ClassModel> lstClass) {
		this.lstClass = lstClass;
	}

	/**
	 * @return the lstAllClass
	 */
	public List<ClassModel> getLstAllClass() {
		return lstAllClass;
	}

	/**
	 * @param lstAllClass the lstAllClass to set
	 */
	public void setLstAllClass(List<ClassModel> lstAllClass) {
		this.lstAllClass = lstAllClass;
	}

	/**
	 * @return the selectedClass
	 */
	public ClassModel getSelectedClass() {
		return selectedClass;
	}

	/**
	 * @param selectedClass the selectedClass to set
	 */
	public void setSelectedClass(ClassModel selectedClass) {
		this.selectedClass = selectedClass;
	}

	/**
	 * @return the lstPageSize
	 */
	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	/**
	 * @param lstPageSize the lstPageSize to set
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
	 * @param selectedPageSize the selectedPageSize to set
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
	 * @param lstAllPageSize the lstAllPageSize to set
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
	 * @param selectedAllPageSize the selectedAllPageSize to set
	 */
	public void setSelectedAllPageSize(String selectedAllPageSize) {
		this.selectedAllPageSize = selectedAllPageSize;
	}

	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * @param selectedStatus the selectedStatus to set
	 */
	@NotifyChange({"lstClass"})
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
		String status="";
		if(selectedStatus.equalsIgnoreCase("Active"))
			status="Y";
		else if(selectedStatus.equalsIgnoreCase("InActive"))
			status="N";
		lstClass=data.fillClassList("",status);
		lstAllClass=lstClass;
	}

	/**
	 * @return the lstStatus
	 */
	public List<String> getLstStatus() {
		return lstStatus;
	}

	/**
	 * @param lstStatus the lstStatus to set
	 */
	public void setLstStatus(List<String> lstStatus) {
		this.lstStatus = lstStatus;
	}

	/**
	 * @return the filter
	 */
	public DataFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(DataFilter filter) {
		this.filter = filter;
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

		List<MenuModel> lstRoles= dataHba.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==244)
			{
				companyRole=item;
				break;
			}
		}
	}


}
