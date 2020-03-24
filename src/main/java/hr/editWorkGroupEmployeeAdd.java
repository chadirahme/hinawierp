package hr;

import hr.model.WorkGroupModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EmployeeModel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class editWorkGroupEmployeeAdd {
	
	private WorkGroupModel selectedWorkGroup;
	private List<EmployeeModel> lstSupervisor;
	private List<EmployeeModel> lstAllSupervisor=new ArrayList<EmployeeModel>();
	private EmployeeModel selectedSupervisor;
	private String selectedRadio;
	WorkGroupData workGroupData=new WorkGroupData();
	String SelectedCompanyName="";
	int selectedCompanyId=0;
	
	public editWorkGroupEmployeeAdd()
	{
		Execution exec = Executions.getCurrent();
		Map map = exec.getArg();
		String CompanyName=(String) map.get("compName");
		int comapnyId=0;//=map.get("compKey");
		selectedCompanyId=comapnyId;
		SelectedCompanyName=CompanyName;
		Window win = (Window)Path.getComponent("/workGroupAddEmployeeModalDialog");
		selectedWorkGroup=new WorkGroupModel();
		win.setTitle("Add Employee");
		lstSupervisor=workGroupData.getSuperVisorNameDropDown(comapnyId);
		lstAllSupervisor.addAll(lstSupervisor);
		selectedRadio="B";
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Command
	public void saveEmployeeSelected(@BindingParam("cmp") Window x)
	{
		if(selectedSupervisor!=null)
		{
		Map args = new HashMap();
		args.put("selectedSupervisorWorkgroup", selectedSupervisor);
		BindUtils.postGlobalCommand(null, null, "insertemployeeForWorkGroup", args);
		x.detach();
		}
		else
		{
			Clients.showNotification("Please Select The Employee.",Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
			return;
		}
	}

	/**
	 * @return the selectedWorkGroup
	 */
	public WorkGroupModel getSelectedWorkGroup() {
		return selectedWorkGroup;
	}

	/**
	 * @param selectedWorkGroup the selectedWorkGroup to set
	 */
	public void setSelectedWorkGroup(WorkGroupModel selectedWorkGroup) {
		this.selectedWorkGroup = selectedWorkGroup;
	}



	/**
	 * @return the lstSupervisor
	 */
	public List<EmployeeModel> getLstSupervisor() {
		return lstSupervisor;
	}



	/**
	 * @param lstSupervisor the lstSupervisor to set
	 */
	public void setLstSupervisor(List<EmployeeModel> lstSupervisor) {
		this.lstSupervisor = lstSupervisor;
	}



	/**
	 * @return the selectedSupervisor
	 */
	public EmployeeModel getSelectedSupervisor() {
		return selectedSupervisor;
	}



	/**
	 * @param selectedSupervisor the selectedSupervisor to set
	 */
	public void setSelectedSupervisor(EmployeeModel selectedSupervisor) {
		this.selectedSupervisor = selectedSupervisor;
	}



	/**
	 * @return the selectedCompanyName
	 */
	public String getSelectedCompanyName() {
		return SelectedCompanyName;
	}



	/**
	 * @param selectedCompanyName the selectedCompanyName to set
	 */
	public void setSelectedCompanyName(String selectedCompanyName) {
		SelectedCompanyName = selectedCompanyName;
	}



	/**
	 * @return the selectedRadio
	 */
	public String getSelectedRadio() {
		return selectedRadio;
	}



	/**
	 * @param selectedRadio the selectedRadio to set
	 */
	@NotifyChange({"lstSupervisor"})
	public void setSelectedRadio(String selectedRadio) {
		this.selectedRadio = selectedRadio;
		if(selectedRadio.equalsIgnoreCase("A"))
		{
			for(EmployeeModel groupModel:lstSupervisor)
			{
				groupModel.setFullName(groupModel.getEmployeeNo());
			}
		}
		else
		{
			lstSupervisor=workGroupData.getSuperVisorNameDropDown(selectedCompanyId);
		}
		
		
	}

	/**
	 * @return the lstAllSupervisor
	 */
	public List<EmployeeModel> getLstAllSupervisor() {
		return lstAllSupervisor;
	}

	/**
	 * @param lstAllSupervisor the lstAllSupervisor to set
	 */
	public void setLstAllSupervisor(List<EmployeeModel> lstAllSupervisor) {
		this.lstAllSupervisor = lstAllSupervisor;
	}

}
