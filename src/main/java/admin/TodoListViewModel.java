package admin;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.EmployeeModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import db.DBHandler;

public class TodoListViewModel 
{

	//http://books.zkoss.org/wiki/ZK_Getting_Started/Creating_a_Database-driven_Application#Your_first_ZK_application_.22To-do.22_list
	private Logger logger = Logger.getLogger(this.getClass().getName());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private ListModelList<TasksModel> lstTasks;
	private TasksModel selectedTask;
	private List<EmployeeModel> lstEmployees;
	private EmployeeModel selectedEmployee;
	private List<String> statusList;
	private String selectedStatus;
	private List<Integer> priorityList;
	private int selectedPriority;
	private TasksModel newTask;
	
	public TodoListViewModel()
	{
		try
		{
			statusList=new ListModelList<String>();
			statusList.add("New");
			statusList.add("Inprogress");
			statusList.add("Done");
			selectedStatus=statusList.get(0);
			
			priorityList=new ListModelList<Integer>();
			for (int i =1; i < 11; i++) 
			{
				priorityList.add(i);
			}
			selectedPriority=priorityList.get(0);
			getEmployeeList();
			
			getTasksList();
		}
		catch (Exception ex) 
		{

			logger.info("Exception in TodoListViewModel ===>Init>> " + ex);
		}
	}
	private void getEmployeeList()
	{
		try
		{
			lstEmployees=new ListModelList<EmployeeModel>();
			 DBHandler db=new DBHandler();
			 ResultSet rs=null;
			 String sql="SELECT * from employee";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 EmployeeModel obj=new EmployeeModel();					
				 obj.setEmployeeKey(rs.getInt("employeeid"));				 				
				 obj.setFullName(rs.getString("employeename"));				 
				 lstEmployees.add(obj);
			 }
		}
		
		catch (Exception ex) 
		{
			logger.info("Exception in TodoListViewModel ===>getEmployeeList>> " + ex);
		}
	}
	private void getTasksList()
	{
		try
		{
			lstTasks=new ListModelList<TasksModel>();
			 DBHandler db=new DBHandler();
			 ResultSet rs=null;
			 String sql="SELECT t.*,p.employeename FROM tasks t INNER JOIN employee p ON t.employeeid=p.employeeid";
			 rs=db.executeNonQuery(sql);
			 while(rs.next())
			 {
				 TasksModel obj=new TasksModel();
				 obj.setEmployeeName(rs.getString("employeename"));				 
				 obj.setTaskid(rs.getInt("taskid"));
				 obj.setEmployeeid(rs.getInt("employeeid"));
				 obj.setPriority(rs.getInt("priority"));
				 obj.setDescription(rs.getString("description"));
				 obj.setStatus(rs.getString("status"));
				 obj.setEmployee(getEmployee(rs.getInt("employeeid")));
				 obj.setOpenDate(rs.getDate("opendate"));
				 obj.setCloseDate(rs.getDate("closedate"));
				obj.setCreationDate(rs.getDate("creationdate"));
				 lstTasks.add(obj);
			 }
		}
		
		catch (Exception ex) 
		{
			logger.info("Exception in TodoListViewModel ===>getTasksList>> " + ex);
		}
	}
	private EmployeeModel getEmployee(int employeeId)
	{
		for (EmployeeModel item : lstEmployees) 
		{
			if(item.getEmployeeKey()==employeeId)
			{
				return item;
			}
		}
		
		return new EmployeeModel();
	}
	
	public TasksModel getSelectedTask() {
		return selectedTask;
	}

	//@NotifyChange({ "selectedEmployee","selectedPriority","selectedStatus"})
	public void setSelectedTask(TasksModel selectedTask) 
	{
		this.selectedTask = selectedTask;
		/*for (EmployeeModel item : lstEmployees) 
		{
			if(item.getEmployeeKey()==selectedTask.getEmployeeid())
			{
				selectedEmployee=item;
			}
		}
		selectedPriority=selectedTask.getPriority();
		selectedStatus=selectedTask.getStatus();*/
	}

	@Command
	@NotifyChange({"lstTasks","selectedTask"})
	public void addTask() 
	{
		newTask=new TasksModel();
		newTask.setPriority(1);
		newTask.setStatus("New");
		newTask.setDescription("");
		selectedTask=newTask;
		lstTasks.add(newTask);			
	}
	
	@Command
	@NotifyChange({"lstTasks","selectedTask"})
	public void updateTask() 
	{
		saveTask();
		getTasksList();
		selectedTask=null;
	}
	
	private void saveTask()
	{
		DBHandler db=new DBHandler();
		String sql="";
		 try
		{
		 String openDate="";
			String closeDate="";
			if(selectedTask.getOpenDate()!=null)
			{
				openDate=sdf.format(selectedTask.getOpenDate());
			}
			if(selectedTask.getCloseDate()!=null)
			{
				closeDate=sdf.format(selectedTask.getCloseDate());
			}
				
		if(selectedTask.getTaskid()==0)
		{
			
			sql="insert into tasks( employeeid,priority,description,status,opendate,closedate) values("+selectedTask.getEmployee().getEmployeeKey()+" , " 
		   +selectedTask.getPriority() + ",'" + selectedTask.getDescription()+"' , '" + selectedTask.getStatus() + "'";
			if(openDate.equals(""))
			{
				sql+=",Null";
			}
			else
			{
				sql+=",'" + openDate+"'";
			}
			if(closeDate.equals(""))
			{
				sql+=",Null";
			}
			else
			{
				sql+=",'" + closeDate+"'";
			}
			sql+=")";
			db.executeUpdateQuery(sql);
			Messagebox.show("New Task is added..","Tasks",Messagebox.OK , Messagebox.INFORMATION);
		}
		else
		{
			sql="update tasks set employeeid=" + selectedTask.getEmployee().getEmployeeKey() + ", priority=" + selectedTask.getPriority() + " , description='" + selectedTask.getDescription()+"',";
			sql+="status='" + selectedTask.getStatus()+"'";
			if(openDate.equals(""))
			{
				sql+=",opendate=Null";
			}
			else
			{
				sql+=",opendate='" + openDate+"'";
			}
			if(closeDate.equals(""))
			{
				sql+=",closedate=Null";
			}
			else
			{
				sql+=",closedate='" + closeDate+"'";
			}
			sql+=" where taskid=" + selectedTask.getTaskid();
			db.executeUpdateQuery(sql);
			Messagebox.show("Task is updated..","Tasks",Messagebox.OK , Messagebox.INFORMATION);
		}
		
		}
		 catch (Exception ex) 
			{
				logger.error("Exception in TodoListViewModel ===>saveTask>> ", ex);
			}
	}
	
	@Command
	@NotifyChange({"lstTasks","selectedTask"})
	public void deleteTask() 
	{
		try
		{
			DBHandler db=new DBHandler();
			String sql="delete from tasks where taskid=" + selectedTask.getTaskid();	
			db.executeUpdateQuery(sql);
			Messagebox.show("Task is deleted..","Tasks",Messagebox.OK , Messagebox.INFORMATION);
			selectedTask=null;
		}
		 catch (Exception ex) 
		{
				logger.error("Exception in TodoListViewModel ===>deleteTask>> ", ex);
		}
		getTasksList();
	}
	
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				EmployeeModel fx = (EmployeeModel)ctx.getProperty().getValue();
				//get filed firstname of the form
				String  description =fx.getStatusDescription();
				String  status =fx.getStatus();
				Date  openDate =fx.getJoiningDate();
				Date  closeDate =fx.getJoiningDate();//dunmmy
				
				if(description == null || "".equals(description))
					this.addInvalidMessage(ctx, "description", "You must enter a description");
				if(status.equals("Inprogress"))
				{
					if(openDate==null || "".equals(openDate))
					{
						addInvalidMessage(ctx, "date", "Start Date is required !");
					}
				}
				if(status.equals("Done"))
				{
					if(closeDate==null || "".equals(closeDate))
					{
						addInvalidMessage(ctx, "date", "Close Date is required !");
					}
				}
			}
		};
	}
	
	public List<EmployeeModel> getLstEmployees() {
		return lstEmployees;
	}

	public void setLstEmployees(List<EmployeeModel> lstEmployees) {
		this.lstEmployees = lstEmployees;
	}

	public EmployeeModel getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(EmployeeModel selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public List<Integer> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(List<Integer> priorityList) {
		this.priorityList = priorityList;
	}

	public int getSelectedPriority() {
		return selectedPriority;
	}

	public void setSelectedPriority(int selectedPriority) {
		this.selectedPriority = selectedPriority;
	}

	

	public ListModelList<TasksModel> getLstTasks() {
		return lstTasks;
	}

	public void setLstTasks(ListModelList<TasksModel> lstTasks) {
		this.lstTasks = lstTasks;
	}
	public TasksModel getNewTask() {
		return newTask;
	}
	public void setNewTask(TasksModel newTask) {
		this.newTask = newTask;
	}
}
