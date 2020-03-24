package hr;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.CompanyDBModel;
import model.EmployeeModel;
import model.VacancyModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class VacancyListData {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//LiveDBHandler db=new LiveDBHandler("");
	SQLDBHandler db=new SQLDBHandler("hinawi_hr");
	
	public VacancyListData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HRQueries query=new HRQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in HRData---Init-->" , ex);
		}
	}
	
	public List<VacancyModel> getMainQuerry(int compkey,int loginUserId)
	{
		List<VacancyModel> lstEmployees=new ArrayList<VacancyModel>();
		//
		VacancyListQuerries query=new VacancyListQuerries();
		ResultSet rs = null;
		ResultSet rsCvs = null;
		ResultSet rsNoofEmp = null;
		try 
		{
			rs=db.executeNonQuery(query.getMainQuerry(compkey,loginUserId));
			while(rs.next())
			{
				VacancyModel obj=new VacancyModel();
				obj.setCompanyID(rs.getInt("comp_key"));
				obj.setDepartmentID(rs.getInt("dep_id"));
				obj.setGradeID(rs.getInt("grade_id"));
				obj.setClassID(rs.getInt("class_id"));
				obj.setSectionID(rs.getInt("section_id"));
				obj.setPositionID(rs.getInt("id"));
				obj.setId(rs.getInt("id"));
				obj.setDescription(rs.getString("description"));
				obj.setEmpAllowed(rs.getInt("emp_allowed"));
				obj.setDepartment(rs.getString("departmentname"));
				obj.setDepartmentNameArabic(rs.getString("departmentarabic"));
				obj.setPosition(rs.getString("postionName"));
				obj.setPositionNameArabic(rs.getString("postionnamearabic"));
				obj.setCompanyName(rs.getString("comp_name"));
				obj.setCompanyNameArabic(rs.getString("comp_name_ar"));
				rsCvs=db.executeNonQuery(query.getCvs(obj.getPositionID()));
				while(rsCvs.next())
				{
					obj.setCvs(rsCvs.getInt("MaxCount"));
				}
				rsNoofEmp=db.executeNonQuery(query.actualNumberOfEmployees(compkey,obj.getDepartmentID(),obj.getPositionID()));
				while(rsNoofEmp.next())
				{
					obj.setAvailable(rsNoofEmp.getInt("MaxCount"));
				}
				
				if(obj.getEmpAllowed()>0)
					obj.setVacancies(obj.getEmpAllowed()-obj.getAvailable());
				lstEmployees.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in HRData---getEmployeeList-->" , ex);
		}
		return lstEmployees;
	}

}
