package setup.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.CompanyDBModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

public class CompanyDBViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	CompanyDBData data=new CompanyDBData();
	private List<CompanyDBModel> companyList;
	private CompanyDBModel selectedCompany;
	
	private ListModelList<CompanyDBModel> companyAccessList;
	private boolean displayEdit = true;
	 
	private List<String> dbTypeList;
	private String selectedDBType;
	
	
	public CompanyDBViewModel()
	{
		try
		{
			companyList=data.getCompanyDBList();
			selectedCompany=companyList.get(0);
			
			dbTypeList=new ArrayList<String>();
			dbTypeList.add("Select");
			dbTypeList.add("HR");
			dbTypeList.add("HBA");
			dbTypeList.add("School");
			selectedDBType=dbTypeList.get(0);			
		}
		 catch (Exception ex) 
		{
		 logger.error("error in CompanyDBViewModel---Init-->" , ex);			
		}
	}
	
	@Command
	@NotifyChange("companyAccessList") 
	public void addNewHostCommand()
	{
	if(selectedCompany.getCompanyId()==0)
	{
		Messagebox.show("Please select company","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
		return;
	}
	
	for (CompanyDBModel item : companyAccessList)
	{
		if(item.getDbid()==0)
		{
			Messagebox.show("Please save the added host before add New one","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
	}
	
	
		CompanyDBModel obj=new CompanyDBModel();
		obj.setDbtype("Select");
		obj.setUserip("");
		obj.setDbname("");
		obj.setDbuser("");
		obj.setDbpwd("");
		
		companyAccessList.add(obj);
	
	}
	
	
	@Command
	@NotifyChange("companyAccessList") 
	public void saveConnectionCommand(@BindingParam("row") CompanyDBModel row)
	{
		if(row.getDbtype()==null || row.getDbtype().equals("Select"))
		{
			Messagebox.show("Please select database type","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		if(row.getUserip().equals(""))
		{
			Messagebox.show("Please enter host/ip","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(row.getDbname().equals(""))
		{
			Messagebox.show("Please enter database name","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(row.getDbuser().equals(""))
		{
			Messagebox.show("Please enter Login ID","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(row.getDbpwd().equals(""))
		{
			Messagebox.show("Please enter password","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		for (CompanyDBModel item : companyAccessList)
		{
			if(item.getDbid()>0 && row.getDbid()==0)
			{
				if(item.getDbtype().equals(row.getDbtype()))
				{
					Messagebox.show("Database type already exists !!.","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
			}
		}
		
		row.setCompanyId(selectedCompany.getCompanyId());
		data.saveHostInfo(row);
		Messagebox.show("Data Saved..","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
		
		companyAccessList=new ListModelList<CompanyDBModel>(data.getCompanyDBAccessList(selectedCompany.getCompanyId()));
		for (CompanyDBModel item : companyAccessList) 
		{
			item.setServerUp(checkSQLServerConnection(item));
			item.setServerStatus(item.isServerUp()?"Up" : "Down");
		}
	}
	
	@Command
	@NotifyChange("companyAccessList") 
	public void deleteConnectionCommand(@BindingParam("row") final CompanyDBModel row)
	{		
		Messagebox.show("Are you sure to delete this connection ?","Delete",Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener()
		 {
			 public void onEvent(Event e)
			 {
				 if (Messagebox.ON_YES.equals(e.getName()))
                {					
					 if(row.getDbid()>0)
					 data.deleteHostInfo(row);							
					companyAccessList.remove(row);
                }
			 }
			
		 });			
	}
	
	@Command
	public void testConnectionCommand(@BindingParam("row") CompanyDBModel row)
	{
		try
		{
			if(checkSQLServerConnection(row))
			Messagebox.show("Server is up","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			else
				Messagebox.show("Server is down","Setup", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		catch (Exception ex) 
		{
		 logger.error("error in testConnectionCommand---Init-->" , ex);			
		}
		
	}
	
	private  boolean checkSQLServerConnection(CompanyDBModel row)
	{
		boolean isConnect=false;
		Connection con = null;
	    Statement stmt = null;
	    ResultSet rs = null;
		try
		{					  
			String connectionUrl = "jdbc:sqlserver://%s;" +"databaseName=%s;user=%s;password=%s";
			connectionUrl=connectionUrl.format(connectionUrl, row.getUserip(),row.getDbname(),row.getDbuser(),row.getDbpwd());
			
			 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         con = DriverManager.getConnection(connectionUrl);
	         
	         String SQL = "SELECT 1";
	         stmt = con.createStatement();
	         rs = stmt.executeQuery(SQL);	          		
			 while(rs.next())
			{
				isConnect=true;				
			}						
		}
		 catch (Exception ex) 
		{
		   logger.error("error in MenuData---checkSQLServerConnection-->" , ex);	
		   Messagebox.show(ex.getMessage(),"Setup", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		finally {
	          if (rs != null) try { rs.close(); } catch(Exception e) {}
	          if (stmt != null) try { stmt.close(); } catch(Exception e) {}
	          if (con != null) try { con.close(); } catch(Exception e) {}
	       }			
		return isConnect;
	}
	
	public List<CompanyDBModel> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<CompanyDBModel> companyList) {
		this.companyList = companyList;
	}

	public CompanyDBModel getSelectedCompany() {
		return selectedCompany;
	}

	@NotifyChange("companyAccessList") 
	public void setSelectedCompany(CompanyDBModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		companyAccessList=new ListModelList<CompanyDBModel>(data.getCompanyDBAccessList(selectedCompany.getCompanyId()));
		for (CompanyDBModel item : companyAccessList) 
		{
			item.setServerUp(checkSQLServerConnection(item));
			item.setServerStatus(item.isServerUp()?"Up" : "Down");
		}
	}

	public ListModelList<CompanyDBModel> getCompanyAccessList() {
		return companyAccessList;
	}

	public void setCompanyAccessList(ListModelList<CompanyDBModel> companyAccessList) {
		this.companyAccessList = companyAccessList;
	}

	public boolean isDisplayEdit() {
		return displayEdit;
	}

	public void setDisplayEdit(boolean displayEdit) {
		this.displayEdit = displayEdit;
	}

	public List<String> getDbTypeList() {
		return dbTypeList;
	}

	public void setDbTypeList(List<String> dbTypeList) {
		this.dbTypeList = dbTypeList;
	}

	public String getSelectedDBType() {
		return selectedDBType;
	}

	public void setSelectedDBType(String selectedDBType) {
		this.selectedDBType = selectedDBType;
	}
}
