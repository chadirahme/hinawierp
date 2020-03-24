package hr;

import java.util.ArrayList;
import java.util.List;

import layout.MenuModel;
import model.AppraisalModel;
import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class AppraisalViewModel {

	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	private List<EmployeeModel> lstCompEmployees;
	private EmployeeModel selectedCompEmployee;
	private List<AppraisalModel> lstAppraisal;
	
	public AppraisalViewModel()
	{
		try
		{
			lstCompEmployees=data.getEmployeeList(1,"Select","A",0);
			selectedCompEmployee=lstCompEmployees.get(0);	
			fillAppraisalList();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AppraisalViewModel ----> init", ex);			
		}
	}
	
	private void fillAppraisalList()
	{
		lstAppraisal=new ArrayList<AppraisalModel>();
		lstAppraisal.add(new AppraisalModel("Creativity "));
		lstAppraisal.add(new AppraisalModel("Time management "));
		lstAppraisal.add(new AppraisalModel("Reporting and administration "));
		lstAppraisal.add(new AppraisalModel("Communication skills "));
		lstAppraisal.add(new AppraisalModel("Delegation skills  "));
		lstAppraisal.add(new AppraisalModel("Product/technical knowledge "));
		lstAppraisal.add(new AppraisalModel("Planning, budgeting and forecasting"));
		lstAppraisal.add(new AppraisalModel("IT/equipment/machinery skills "));
		lstAppraisal.add(new AppraisalModel("Meeting deadlines/commitments "));
	}
	
	@Command
	public void searchCommand()
	{
		
	}
	
	
	@Command 
	@NotifyChange({"lstAppraisal"})
	public void checkAllAddCommand(@BindingParam("row") AppraisalModel row ,@BindingParam("chk") boolean chk,@BindingParam("type") String type)
	{
		for (AppraisalModel item : lstAppraisal)
		{
			if(item.getTitle().equals(row.getTitle()))
			{
				if(type.equals("poor"))
				{
					item.setExcellent(false);
					item.setSatisfactory(false);
					item.setGood(false);				
				}
				if(type.equals("good"))
				{
					item.setExcellent(false);
					item.setSatisfactory(false);
					item.setPoor(false);				
				}
				if(type.equals("satisfactory"))
				{
					item.setExcellent(false);
					item.setGood(false);
					item.setPoor(false);				
				}
				if(type.equals("excellent"))
				{
					item.setGood(false);
					item.setSatisfactory(false);
					item.setPoor(false);				
				}
				
			}
		}
	}

	public List<EmployeeModel> getLstCompEmployees() {
		return lstCompEmployees;
	}

	public void setLstCompEmployees(List<EmployeeModel> lstCompEmployees) {
		this.lstCompEmployees = lstCompEmployees;
	}

	public EmployeeModel getSelectedCompEmployee() {
		return selectedCompEmployee;
	}

	public void setSelectedCompEmployee(EmployeeModel selectedCompEmployee) {
		this.selectedCompEmployee = selectedCompEmployee;
	}

	public List<AppraisalModel> getLstAppraisal() {
		return lstAppraisal;
	}

	public void setLstAppraisal(List<AppraisalModel> lstAppraisal) {
		this.lstAppraisal = lstAppraisal;
	}
}
