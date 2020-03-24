package timesheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hr.HRData;

import model.EmployeeModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class EmployeeFilterViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	//HRData hrdata=new HRData();
	
	private List<HRListValuesModel> lstDepartment;
	private HRListValuesModel selectedDepartment;
	
	private List<HRListValuesModel> lstPosition;
	private HRListValuesModel selectedPosition;
	
	private int selectedEmployeeType;
	private String employeeNo;
	private String employeeName;
	
	private  List<EmployeeModel> lstEmployees;
	private List<EmployeeModel> lstAllEmployees;
	private Set<EmployeeModel> selectedEntities;	
	
	int _compKey;
	String _type;
	
	@Init
	public void init(@BindingParam("compKey")int compKey,@BindingParam("type")String type)
	{				
		_compKey=compKey;
		_type=type;
		//lstEmployees=data.getEmployeeList(compKey,type);
		//lstAllEmployees=lstEmployees;	
	}
	
	public EmployeeFilterViewModel()
	{
		try
		{			
			lstDepartment=data.getHRListValues(6,"All");
			selectedDepartment=lstDepartment.get(0);
			lstPosition=data.getHRListValues(7,"All");
			selectedPosition=lstPosition.get(0);
			
			selectedEmployeeType=0;
			employeeNo="";
			employeeName="";
			
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in EmployeeFilterViewModel ----> Init", ex);			
		}		
	}
	
	@Command
	@NotifyChange({"lstEmployees"})
	public void searchCommand()
	{
		String employeeType="";
		if(selectedEmployeeType==1)
			employeeType="E";
		if(selectedEmployeeType==2)
			employeeType="L";
		
		lstEmployees=data.searchAdvancedEmployee(_compKey,_type,employeeType,employeeNo,employeeName,selectedDepartment.getListId(),selectedPosition.getListId());
	}
	
	@Command
	@NotifyChange({"employeeNo","employeeName","selectedEmployeeType","selectedDepartment","selectedPosition","lstEmployees"})
	public void clearCommand()
	{
		employeeNo="";
		employeeName="";
		selectedEmployeeType=0;
		selectedDepartment=lstDepartment.get(0);
		selectedPosition=lstPosition.get(0);
		lstEmployees=new ArrayList<EmployeeModel>();
	}
	
	@Command
	public void sendData(@ContextParam(ContextType.VIEW) Window comp) 
	{
		try
		{
		List<Integer> lstEmployeeId=new ArrayList<Integer>();
		String empKeys="";
				
		if(selectedEntities!=null)
		{
			for (EmployeeModel item : selectedEntities) 
			{
				lstEmployeeId.add(item.getEmployeeKey());
			}
			
		for (Integer empKey : lstEmployeeId) 
		{
			if(empKeys.equals(""))
			empKeys+=String.valueOf(empKey);
			else
			empKeys+=","+String.valueOf(empKey);
		}					
		
		}
		
		else if(lstEmployees.size()==1)
		{
			empKeys=String.valueOf(lstEmployees.get(0).getEmployeeKey());
		}
		
		if(empKeys.equals(""))
		{
			Messagebox.show("Please select employee!!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		Map args = new HashMap();
		args.put("myData", empKeys);		
		BindUtils.postGlobalCommand(null, null, "filterWindowClose", args);
		
		comp.detach();
		}
		catch (Exception ex)
		{	
		logger.error("ERROR in EmployeeFilterViewModel ----> sendData", ex);			
		}	
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

	public List<HRListValuesModel> getLstPosition() {
		return lstPosition;
	}

	public void setLstPosition(List<HRListValuesModel> lstPosition) {
		this.lstPosition = lstPosition;
	}

	public HRListValuesModel getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(HRListValuesModel selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public int getSelectedEmployeeType() {
		return selectedEmployeeType;
	}

	public void setSelectedEmployeeType(int selectedEmployeeType) {
		this.selectedEmployeeType = selectedEmployeeType;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public List<EmployeeModel> getLstEmployees() {
		return lstEmployees;
	}

	public void setLstEmployees(List<EmployeeModel> lstEmployees) {
		this.lstEmployees = lstEmployees;
	}

	public Set<EmployeeModel> getSelectedEntities() {
		return selectedEntities;
	}

	public void setSelectedEntities(Set<EmployeeModel> selectedEntities) {
		this.selectedEntities = selectedEntities;
	}
}
