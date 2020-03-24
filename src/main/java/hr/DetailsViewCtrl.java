package hr;

import hr.model.ActivityModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

public class DetailsViewCtrl extends SelectorComposer 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	
	@Wire
	Grid dgDetails;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
	     Date fromdate=(Date) execution.getArg().get("fdate");	       
		 Date todate=(Date) execution.getArg().get("tdate");
		
		EmployeeModel objEmployee=(EmployeeModel)execution.getArg().get("emp");
		
		//dgDetails.setModel((ListModel<ActivityModel>)data.GetEMPSalary(objEmployee.getEmployeeKey(), objEmployee.getEmployeeNo(), sdf.format(fromdate),sdf.format(todate), "Active","ssss"));
		dgDetails.setRowRenderer(new RowRenderer<ActivityModel>(){
			public void render(Row row, final ActivityModel objActivity, int idx) throws Exception {
				new Label(objActivity.getEmpNo()).setParent(row);
				new Label(objActivity.getEmployeeName()).setParent(row);
				new Label(sdf.format(objActivity.getActivityDate())).setParent(row);
				new Label(objActivity.getActivity()).setParent(row);
				new Label(objActivity.getActivityItem()).setParent(row);
				new Label(sdf.format(objActivity.getFromDate())).setParent(row);
				new Label(sdf.format(objActivity.getToDate())).setParent(row);
				new Label(objActivity.getNoofDays()).setParent(row);
				new Label(String.valueOf(objActivity.getAmount())).setParent(row);
				new Label(String.valueOf(objActivity.getBalance())).setParent(row);
				new Label(objActivity.getStatus()).setParent(row);
				new Label(objActivity.getEmployeeStatus()).setParent(row);
			}
			
		});
		
		//Messagebox.show(String.valueOf(dgDetails.getModel().getSize()));		
	}
}
