package timesheet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hr.HRData;

import model.EmployeeModel;
import model.HRListValuesModel;
import model.ProjectModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class AutoFillViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	TimeSheetData data=new TimeSheetData();
	HRData hrdata=new HRData();
	private ListModelList<ProjectModel> lstProject;
	private ProjectModel selectedProject;
	private List<HRListValuesModel> lstPositions;
	private HRListValuesModel selectedPosition;
	private  List<EmployeeModel> lstEmployees;
	private Set<EmployeeModel> selectedEntities;
	
	private int ot1;
	private int ot2;
	private int ot3;
	
	private Date fromDate;
	private Date toDate;
	
	private Date tsFromDate;
	private Date tsToDate;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private int supervisorID;
	
	public AutoFillViewModel()
	{
		try		
		{
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			supervisorID=dbUser.getSupervisor();
			
			Execution exec = Executions.getCurrent();
			Map map = exec.getArg();
			int compKey=(Integer)map.get("compKey");
			fromDate=(Date)map.get("fromDate");
			toDate=(Date)map.get("toDate");
			List<Integer> lstEmpKeys=(List<Integer>)map.get("lstEmpKeys");
			
			tsFromDate=fromDate;
			tsToDate=toDate;
			
			lstProject=new ListModelList<ProjectModel>(data.getProjectList(compKey,"Transfer",true,supervisorID));
			if(lstProject.size()>0)
				selectedProject=lstProject.get(0);
			
			lstPositions=data.getHRListValues(47,"Select");
			if(lstPositions.size()>0)
				selectedPosition=lstPositions.get(0);
			
			lstEmployees=data.getAutoFillEmployeeList(compKey,"T",lstEmpKeys,dbUser.getSupervisor());
			
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AutoFillViewModel ----> init", ex);			
		}
	}
	
	@Command
	//@NotifyChange({"lstProject"})
    public void okCommand(@ContextParam(ContextType.VIEW) Window comp) 
	{
		/*if(selectedProject.getProjectKey()==0)
		{
			Messagebox.show("Please select project !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}*/
		
		List<Integer> lstEmployeeId=new ArrayList<Integer>();
		if(selectedEntities!=null)
		{
			for (EmployeeModel item : selectedEntities) 
			{
				lstEmployeeId.add(item.getEmployeeKey());
			}				
		}
		
		if(lstEmployeeId.size()==0)
		{
			Messagebox.show("Please select Employee !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		Map args = new HashMap();
		args.put("projectId", selectedProject.getProjectKey());
		args.put("positionId", selectedPosition.getListId());
		args.put("fromDate", fromDate);
		args.put("toDate", toDate);
		args.put("lstEmployeeId", lstEmployeeId);
		args.put("ot1", ot1);
		args.put("ot2", ot2);
		args.put("ot3", ot3);
		BindUtils.postGlobalCommand(null, null, "autofillClose", args);		
		comp.detach();
    }
	
	
	public ListModelList<ProjectModel> getLstProject() {
		return lstProject;
	}
	public void setLstProject(ListModelList<ProjectModel> lstProject) {
		this.lstProject = lstProject;
	}

	public ProjectModel getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(ProjectModel selectedProject) {
		this.selectedProject = selectedProject;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) 
	{	
		if(fromDate.before(tsFromDate) || fromDate.after(tsToDate))
		{
			Messagebox.show("Date not in time sheet dates !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) 
	{
		if(toDate.after(tsToDate) || toDate.before(tsFromDate))
		{
			Messagebox.show("Date not in time sheet dates !!","Time Sheet", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		this.toDate = toDate;
	}

	public List<HRListValuesModel> getLstPositions() {
		return lstPositions;
	}

	public void setLstPositions(List<HRListValuesModel> lstPositions) {
		this.lstPositions = lstPositions;
	}

	public HRListValuesModel getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(HRListValuesModel selectedPosition) {
		this.selectedPosition = selectedPosition;
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

	public int getOt1() {
		return ot1;
	}

	public void setOt1(int ot1) {
		this.ot1 = ot1;
	}

	public int getOt2() {
		return ot2;
	}

	public void setOt2(int ot2) {
		this.ot2 = ot2;
	}

	public int getOt3() {
		return ot3;
	}

	public void setOt3(int ot3) {
		this.ot3 = ot3;
	}
}
