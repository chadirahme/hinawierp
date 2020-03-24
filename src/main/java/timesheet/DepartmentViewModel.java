package timesheet;

import hr.HRData;
import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import setup.users.WebusersModel;

public class DepartmentViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data = new TimeSheetData();
	HRData hrdata = new HRData();

	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;

	private List<HRListValuesModel> lstDepartment;
	private HRListValuesModel selectedDepartment;

	private List<HRListValuesModel> lstHRPositions;
	private HRListValuesModel selectedHRPositions;
	
	private ListModelList<HRListValuesModel> lstPositions;
	
	private int totalDepartments;
	private int totalPositions;
	private int totalEmployeeAllowd;
	private int totalCompanyEmployee;
	private int totalVacancies;
	
	int menuID=294;
	private MenuModel companyRole;
	
	public DepartmentViewModel() {
		try {
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser = (WebusersModel) sess.getAttribute("Authentication");
			getCompanyRolePermessions(dbUser.getCompanyroleid());
			
			int defaultCompanyId = 0;
			defaultCompanyId = hrdata.getDefaultCompanyID(dbUser.getUserid());
			lstComapnies = data.getCompanyList(dbUser.getUserid());
			for (CompanyModel item : lstComapnies) {
				if (item.getCompKey() == defaultCompanyId)
					selectedCompany = item;
			}
			if (lstComapnies.size() >= 1 && selectedCompany == null)
				selectedCompany = lstComapnies.get(0);

			lstDepartment = data.getDepartmentList(6, "Select");
			if (lstDepartment != null)
				selectedDepartment = lstDepartment.get(0);
			
			lstHRPositions=data.getDepartmentList(7, "");
			getCompanySetup(selectedCompany.getCompKey());

		} catch (Exception ex) {
			logger.error("ERROR in DepartmentViewModel ----> init", ex);
		}
	}
	private void getCompanyRolePermessions(int companyRoleId)
	{
		setCompanyRole(new MenuModel());
		
		List<MenuModel> lstRoles= hrdata.getHRRoles(companyRoleId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==menuID)
			{
				setCompanyRole(item);
				break;
			}
		}
	}
	
	@NotifyChange({ "totalCompanyEmployee" ,"totalEmployeeAllowd" , "totalDepartments" ,"totalPositions" ,"totalVacancies"})
	private void getCompanySetup(int compKey)
	{
		totalCompanyEmployee=data.getTotalCompanyEmployees(compKey);
		totalEmployeeAllowd=data.getTotalEmployeesAllowed(compKey);
		Integer[] result=data.getCompanyTotalDepartments(compKey);
		totalDepartments=result[0];
		totalPositions=result[1];
		totalVacancies=result[3];
	}
	@Command
	@NotifyChange({ "lstPositions","totalCompanyEmployee" ,"totalEmployeeAllowd" , "totalDepartments" ,"totalPositions" ,"totalVacancies" })
	public void saveCommand() 
	{
		try
		{
		for (HRListValuesModel item : lstPositions) {
			if(item.getListId()>0)
			{
			data.updateCOMPSTANDPOS(selectedCompany.getCompKey(),
					selectedDepartment.getListId(), item.getListId(),
					item.getFieldId(), item.getEnDescription());
			}
			else
			{
				if(item.getSelectedHRValue()!=null)
				data.addCOMPSTANDPOS(selectedCompany.getCompKey(), selectedDepartment.getListId(), item.getSelectedHRValue().getListId(),
						item.getFieldId(), item.getSelectedHRValue().getEnDescription());
			}
		}

		Integer[] result=data.getNewCompanyTotalDepartments(selectedCompany.getCompKey());
		data.updateNewCompanySetup(selectedCompany.getCompKey(), result[0], result[1]);
		
		lstPositions =new ListModelList<HRListValuesModel>( data.getDepartmentPositionList(
				selectedCompany.getCompKey(), selectedDepartment.getListId()));
		
		getCompanySetup(selectedCompany.getCompKey());
		
		Messagebox.show("Records Saved Successfully.", "Department Setup",
				Messagebox.OK, Messagebox.INFORMATION);
		}
		
		catch (Exception ex) {
			logger.error("ERROR in DepartmentViewModel ----> saveCommand", ex);
		}
	}

	@Command
	@NotifyChange({ "lstPositions" })
	public void addPositionCommand() {
		if (selectedDepartment.getListId() == 0) {
			Messagebox.show("Please select Department !! ", "Department Setup",
					Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}

		HRListValuesModel obj = new HRListValuesModel();
		obj.setEditingStatus(true);
		obj.setListId(0);
		obj.setFieldId(0);// emp allowed
		obj.setSubId(selectedDepartment.getListId());
		lstPositions.add(0, obj);
	}
	
	@Command
	@NotifyChange({ "lstPositions" })
	public void selectPositionCommand(@BindingParam("row") HRListValuesModel row)
	{
		
		for (HRListValuesModel item : lstPositions)
		{
			if(item.getListId()>0 && row.getSelectedHRValue().getListId()==item.getListId())
			{
				Messagebox.show("This position already selected !! ", "Department Setup",
						Messagebox.OK, Messagebox.EXCLAMATION);
				row.setSelectedHRValue(null);
				return;
			}
			else if(item.getListId()==0 && row!=item && item.getSelectedHRValue()!=null)
			{
				if(item.getSelectedHRValue().getListId() == row.getSelectedHRValue().getListId())
				{
					Messagebox.show("This position already selected !! ", "Department Setup",
							Messagebox.OK, Messagebox.EXCLAMATION);
					row.setSelectedHRValue(null);
					return;
				}
			}
		}
		
		
		/*List<Integer> lstPosIDs=new ArrayList<Integer>();
		for (HRListValuesModel item : lstPositions)
		{
			if(item.getListId()>0)
			{
			if(!lstPosIDs.contains(item.getListId()))
				lstPosIDs.add(item.getListId());
			}
			else
			{
				if(item.getSelectedHRValue()!=null)
				{
					if(!lstPosIDs.contains(item.getSelectedHRValue().getListId()))
						lstPosIDs.add(item.getSelectedHRValue().getListId());
				}				
			}
		}
		logger.info(">> " + lstPosIDs.size());
		int count=0;
		for (Integer posID : lstPosIDs) 
		{
			if(row.getSelectedHRValue()!=null)
			{
			if(posID == row.getSelectedHRValue().getListId())
				count++;
			logger.info("count >> " + count);
			}
		}
		
			if(count>1)
			{
				Messagebox.show("This position already selected !! ", "Department Setup",
						Messagebox.OK, Messagebox.EXCLAMATION);
				row.setSelectedHRValue(null);
				return;
			}*/
		
	}

	@Command
	@NotifyChange({ "lstPositions","totalCompanyEmployee" ,"totalEmployeeAllowd" , "totalDepartments" ,"totalPositions" ,"totalVacancies" })
	public void deleteCommand(@BindingParam("row") final HRListValuesModel row)
	{
		if (row.getListId() == 0) {
			lstPositions.remove(row);
		} else {
			// chk if used
			int count = data.checkIfDepartmentPositionUsed(
					selectedCompany.getCompKey(),
					selectedDepartment.getListId(), row.getListId());
			if (count > 0) {
				Messagebox
						.show("You cannot Delete this position.Data already exists !! ",
								"Department Setup", Messagebox.OK,
								Messagebox.EXCLAMATION);
				return;
			}

			else {
				Messagebox.show("Are you sure to remove this position? ",
						"Department Setup", Messagebox.YES | Messagebox.NO,
						Messagebox.QUESTION, new EventListener() {
							public void onEvent(Event e) {
								if (Messagebox.ON_YES.equals(e.getName())) {

									data.deleteCOMPSTANDPOS(selectedCompany.getCompKey(),selectedDepartment.getListId(), row.getListId());		
									Integer[] result=data.getNewCompanyTotalDepartments(selectedCompany.getCompKey());
									data.updateNewCompanySetup(selectedCompany.getCompKey(), result[0], result[1]);
									BindUtils.postGlobalCommand(null, null, "resetGrid", null);					
									lstPositions.remove(row);
									
								}
							}
						});
				
			}
			
		}
	}
	@GlobalCommand("resetGrid")
	@NotifyChange({"totalCompanyEmployee" ,"totalEmployeeAllowd" , "totalDepartments" ,"totalPositions" ,"totalVacancies"})
	public void resetGrid() 
	{
		getCompanySetup(selectedCompany.getCompKey());	
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

	@NotifyChange({ "lstPositions" })
	public void setSelectedDepartment(HRListValuesModel selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
		lstPositions =new ListModelList<HRListValuesModel>( data.getDepartmentPositionList(
				selectedCompany.getCompKey(), selectedDepartment.getListId()));
	}

	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}

	@NotifyChange({ "lstPositions", "selectedDepartment" ,"totalCompanyEmployee" ,"totalEmployeeAllowd" , "totalDepartments" ,"totalPositions" ,"totalVacancies"})
	public void setSelectedCompany(CompanyModel selectedCompany) {
		this.selectedCompany = selectedCompany;
		lstPositions = new ListModelList<HRListValuesModel>();
		if (lstDepartment != null)
			selectedDepartment = lstDepartment.get(0);
		getCompanySetup(selectedCompany.getCompKey());
	}

	public ListModelList<HRListValuesModel> getLstPositions() {
		return lstPositions;
	}

	public void setLstPositions(ListModelList<HRListValuesModel> lstPositions) {
		this.lstPositions = lstPositions;
	}

	public List<HRListValuesModel> getLstHRPositions() {
		return lstHRPositions;
	}

	public void setLstHRPositions(List<HRListValuesModel> lstHRPositions) {
		this.lstHRPositions = lstHRPositions;
	}

	public HRListValuesModel getSelectedHRPositions() {
		return selectedHRPositions;
	}

	public void setSelectedHRPositions(HRListValuesModel selectedHRPositions) {
		this.selectedHRPositions = selectedHRPositions;
	}

	public int getTotalDepartments() {
		return totalDepartments;
	}

	public void setTotalDepartments(int totalDepartments) {
		this.totalDepartments = totalDepartments;
	}

	public int getTotalPositions() {
		return totalPositions;
	}

	public void setTotalPositions(int totalPositions) {
		this.totalPositions = totalPositions;
	}

	public int getTotalEmployeeAllowd() {
		return totalEmployeeAllowd;
	}

	public void setTotalEmployeeAllowd(int totalEmployeeAllowd) {
		this.totalEmployeeAllowd = totalEmployeeAllowd;
	}

	public int getTotalCompanyEmployee() {
		return totalCompanyEmployee;
	}

	public void setTotalCompanyEmployee(int totalCompanyEmployee) {
		this.totalCompanyEmployee = totalCompanyEmployee;
	}

	public int getTotalVacancies() {
		return totalVacancies;
	}

	public void setTotalVacancies(int totalVacancies) {
		this.totalVacancies = totalVacancies;
	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}
}
