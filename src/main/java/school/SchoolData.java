package school;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;
import fixedasset.FixedAssetData;
import model.*;

public class SchoolData 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SQLDBHandler db=new SQLDBHandler("hinawi_school");
	 SchoolQueries query=new SchoolQueries();
	public SchoolData()
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
				SchoolQueries query=new SchoolQueries();
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
			logger.error("error in SchoolData---Init-->" , ex);
		}
	}
	
	public List<SchoolModel> getSchoolList()
	{
		 List<SchoolModel> lst=new ArrayList<SchoolModel>();
		// SQLDBHandler db=new SQLDBHandler("hinawi_school");
		 
		 ResultSet rs = null;
			try 
			{
				SchoolModel obj=new SchoolModel();
				obj.setSchoolid(-1);
				obj.setEnschoolname("Select");
				lst.add(obj);
				rs=db.executeNonQuery(query.getSchoolListQuery());
				while(rs.next())
				{					
					obj=new SchoolModel();
					obj.setSchoolid(rs.getInt("SchoolId"));
					obj.setEnschoolname(rs.getString("EnSchoolName"));
					obj.setArschoolname(rs.getString("ArSchoolName"));
					obj.setEnaddress(rs.getString("EnAddress"));
					obj.setAraddress(rs.getString("ArAddress"));
					obj.setPhoneno(rs.getString("PhoneNo"));
					obj.setFaxno(rs.getString("FaxNo"));
					obj.setEmail(rs.getString("Email"));
					obj.setWebsite(rs.getString("WebSite"));
					obj.setPobox(rs.getString("Pobox"));
					lst.add(obj);
					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---getSchoolList-->" , ex);
			}
		 return lst;
	}
	
	public int SaveShoolData(SchoolModel obj)
	{
		int schoolId=0;
		try		
		{
			if(obj.getSchoolid()==0)
			{
				db.executeUpdateQuery(query.AddNewSchoolQuery(obj));
			}
			else
			{
				db.executeUpdateQuery(query.UpdateSchoolQuery(obj));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---SaveShoolData-->" , ex);
		}
		return schoolId;
	}
	
	public List<ProgramModel> getProgramsList(String type)
	{
		 List<ProgramModel> lst=new ArrayList<ProgramModel>();
		// SQLDBHandler db=new SQLDBHandler("SMDBLive");
		 SchoolQueries query=new SchoolQueries();
		 ResultSet rs = null;
			try 
			{
				if(!type.equals(""))
				{
					ProgramModel obj=new ProgramModel();
					obj.setProgramid(0);
					obj.setEnprogramname(type);
					lst.add(obj);
				}		
								
				rs=db.executeNonQuery(query.getProgramsListQuery());
				while(rs.next())
				{					
					ProgramModel obj=new ProgramModel();
					obj.setProgramid(rs.getInt("ProgramId"));
					obj.setEnprogramname(rs.getString("EnProgramName"));
					obj.setArprogramname(rs.getString("ArProgramName"));					
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---getProgramsList-->" , ex);
			}
		 return lst;
	}
	
	public int SaveProgramData(ProgramModel obj)
	{
		int programId=0;
		try		
		{
			if(obj.getProgramid()==0)
			{
				programId=db.executeUpdateQuery(query.AddNewProgramQuery(obj));
			}
			else
			{
				db.executeUpdateQuery(query.UpdateProgramQuery(obj));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---SaveProgramData-->" , ex);
		}
		return programId;
	}
	
	
	public List<GradeModel> getGradeList(String type)
	{
		 List<GradeModel> lst=new ArrayList<GradeModel>();				 
		 ResultSet rs = null;
			try 
			{
				if(!type.equals(""))
				{
					GradeModel obj=new GradeModel();
					obj.setGradeId(0);
					obj.setEnGradeName(type);
					lst.add(obj);
				}				
				rs=db.executeNonQuery(query.getGradesListQuery());
				while(rs.next())
				{					
					GradeModel obj=new GradeModel();
					obj.setGradeId(rs.getInt("GradeId"));
					obj.setEnGradeName(rs.getString("EnGradeName"));
					obj.setArGradeName(rs.getString("ArGradeName"));					
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---getGradeList-->" , ex);
			}
		 return lst;
	}
	
	public int SaveGradeData(GradeModel obj)
	{
		int gradeId=0;
		try		
		{
			
		  db.executeUpdateQuery(query.UpdateGradeQuery(obj));
			
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---SaveGradeData-->" , ex);
		}
		return gradeId;
	}
	
	
	public List<SectionModel> getSectionList(String type)
	{
		 List<SectionModel> lst=new ArrayList<SectionModel>();				 
		 ResultSet rs = null;
			try 
			{
				if(!type.equals(""))
				{
					SectionModel obj=new SectionModel();
					obj.setSectionId(0);
					obj.setEnSectionName(type);
					lst.add(obj);
				}				
				rs=db.executeNonQuery(query.getSectionsListQuery());
				while(rs.next())
				{					
					SectionModel obj=new SectionModel();
					obj.setSectionId(rs.getInt("SectionId"));
					obj.setEnSectionName(rs.getString("EnSectionName"));
					obj.setArSectionName(rs.getString("ArSectionName"));					
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---getSectionList-->" , ex);
			}
		 return lst;
	}
	
	public int SaveSectionData(SectionModel obj)
	{
		int sectionId=0;
		try		
		{
		
			if(obj.getSectionId()==0)
				 db.executeUpdateQuery(query.AddNewSectionQuery(obj));
			else if(obj.getSectionId()>0)
		  db.executeUpdateQuery(query.UpdateSectionQuery(obj));
			
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---SaveSectionData-->" , ex);
		}
		return sectionId;
	}
	
	public List<SchoolProgramModel> getSchoolProgramList(int schoolId)
	{
		 List<SchoolProgramModel> lst=new ArrayList<SchoolProgramModel>();				 
		 ResultSet rs = null;
			try 
			{					
				rs=db.executeNonQuery(query.getShoolProgramListQuery(schoolId));
				while(rs.next())
				{					
					SchoolProgramModel obj=new SchoolProgramModel();
					obj.setSchoolProgramId(rs.getInt("SchoolProgramId"));
					obj.setSchoolId(rs.getInt("SchoolID"));
					obj.setProgramId(rs.getInt("ProgramID"));
					obj.setFromGradeId(rs.getInt("FromGradeID"));
					obj.setToGradeId(rs.getInt("ToGradeID"));
					obj.setGender(rs.getInt("Gender"));
					obj.setProgramName(rs.getString("EnProgramName"));
					obj.setFromGradeName(rs.getString("FromGradeName"));
					obj.setToGradeName(rs.getString("ToGradeName"));
					obj.setSchoolName(rs.getString("EnSchoolName"));
					obj.setGenderName(obj.getGender()==1?"Male" : obj.getGender()==2 ? "Female" : "Both");
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---getSchoolProgramList-->" , ex);
			}
		 return lst;
	}
	
	public int SaveSchoolProgram(SchoolProgramModel obj)
	{
		int programId=0;
		try		
		{
			if(obj.getSchoolProgramId()==0)
			{
				programId=db.executeUpdateQuery(query.AddNewSchoolProgramQuery(obj));
			}
			else
			{
				db.executeUpdateQuery(query.updateSchoolProgramQuery(obj));
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---SaveSchoolProgram-->" , ex);
		}
		return programId;
	}
	
	public int deleteSchoolProgram(SchoolProgramModel obj)
	{
		int programId=0;
		try		
		{
			
		  db.executeUpdateQuery(query.deleteSchoolProgramQuery(obj));
			
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---deleteSchoolProgram-->" , ex);
		}
		return programId;
	}
	
	
	public List<ProgramModel> GetProgramInSchool(int schoolId,String type)
	{
		 List<ProgramModel> lst=new ArrayList<ProgramModel>();		
		 SchoolQueries query=new SchoolQueries();
		 ResultSet rs = null;
			try 
			{
				if(!type.equals(""))
				{
					ProgramModel obj=new ProgramModel();
					obj.setProgramid(0);
					obj.setEnprogramname(type);
					lst.add(obj);
				}		
								
				rs=db.executeNonQuery(query.GetProgramInSchoolQuery(schoolId));
				while(rs.next())
				{					
					ProgramModel obj=new ProgramModel();
					obj.setProgramid(rs.getInt("ProgramId"));
					obj.setEnprogramname(rs.getString("EnProgramName"));
					obj.setArprogramname(rs.getString("ArProgramName"));					
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---GetProgramInSchool-->" , ex);
			}
		 return lst;
	}
	
	public List<GradeModel> GetGradeInSchool(int schoolId,int ProgramId,String type)
	{
		 List<GradeModel> lst=new ArrayList<GradeModel>();				 
		 ResultSet rs,rs1 = null;
			try 
			{
				if(!type.equals(""))
				{
					GradeModel obj=new GradeModel();
					obj.setGradeId(0);
					obj.setEnGradeName(type);
					lst.add(obj);
				}				
				rs=db.executeNonQuery(query.GetGradeInSchoolQuery(schoolId, ProgramId));
				while(rs.next())
				{	
					rs1=db.executeNonQuery(query.GetGradeNamesInSchoolQuery(rs.getInt("FromGradeID") , rs.getInt("ToGradeID")));
					while(rs1.next())
					{
					
					GradeModel obj=new GradeModel();
					obj.setGradeId(rs1.getInt("GradeId"));
					obj.setEnGradeName(rs1.getString("EnGradeName"));
					obj.setArGradeName(rs1.getString("ArGradeName"));					
					lst.add(obj);	
					}
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---GetGradeInSchool-->" , ex);
			}
		 return lst;
	}
	
	public List<SectionsInGradeModel> GetSectionsInGrades(int SchoolID, int ProgramID, int GradeID)
	{
		 List<SectionsInGradeModel> lst=new ArrayList<SectionsInGradeModel>();				 
		 ResultSet rs = null;
			try 
			{
				CallableStatement cs = null;
				//String SPsql = "EXEC SSP_GetSectionsInGrades ?,?,?";
				cs=db.getConnection().prepareCall("{call SSP_GetSectionsInGrades(?,?,?)}");
				cs.setInt(1, SchoolID);
				cs.setInt(2, ProgramID);
				cs.setInt(3, GradeID);								
				rs=cs.executeQuery();
				while(rs.next())
				{										
					SectionsInGradeModel obj=new SectionsInGradeModel();
					obj.setSchoolId(SchoolID);
					obj.setProgramId(ProgramID);
					obj.setGradeId(rs.getInt("GradeId"));
					obj.setEnGradeName(rs.getString("EnGradeName"));
					obj.setSectionId(rs.getInt("SectionID"));
					obj.setEnSectionName(rs.getString("EnSectionName"));	
					obj.setMaxStudents(rs.getInt("MaxStudents"));
					lst.add(obj);	
					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---GetSectionsInGrades-->" , ex);
			}
		 return lst;
	}
	
	public void saveAssigendSections(SectionsInGradeModel obj,Set<SectionModel> selectedAssignedSections)
	{
		try
		{
			for (SectionModel item : selectedAssignedSections)
			{
				String[] parm={obj.getSchoolId()+"",obj.getProgramId()+"" ,obj.getGradeId()+"",item.getSectionId()+"",obj.getMaxStudents()+""};
				db.callProcedure("SSP_AddNewSectionInGrades", parm);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---saveAssigendSections-->" , ex);
		}
	}
	
	public void deleteAssigendSections(SectionsInGradeModel obj)
	{
		try
		{
			//stop the check if section exsits from tbm_Registration
			String[] parm={obj.getSchoolId()+"",obj.getProgramId()+"" ,obj.getGradeId()+"",obj.getSectionId()+""};
			db.callProcedure("SSP_DeleteSectionInGrade", parm);
			
		}
		catch (Exception ex) 
		{
			logger.error("error in SchoolData---deleteAssigendSections-->" , ex);
		}
	}
	
	public List<StudentModel> getStudentList()
	{
		 List<StudentModel> lst=new ArrayList<StudentModel>();
		// SQLDBHandler db=new SQLDBHandler("SMDBLive");
		 SchoolQueries query=new SchoolQueries();
		 ResultSet rs = null;
			try 
			{				
				rs=db.executeNonQuery(query.getStudentListQuery());
				while(rs.next())
				{										
					StudentModel obj=new StudentModel();
					obj.setApplicationNo(rs.getString("ApplicationNo"));
					obj.setAcademicYear(rs.getString("AcademicYear"));
					obj.setGenderID(rs.getString("GenderID"));
					obj.setEnFullName(rs.getString("EnFullName"));
					obj.setArFullName(rs.getString("ArFullName"));
					obj.setHomePhone(rs.getString("HomePhone"));
					obj.setOfficePhone(rs.getString("OfficePhone"));
					obj.setEnSchoolName(rs.getString("EnSchoolName"));
					obj.setEnProgramName(rs.getString("EnProgramName"));
					obj.setEnGradeName(rs.getString("EnGradeName"));
					obj.setEnSectionName(rs.getString("EnSectionName"));
					obj.setEnCountryName(rs.getString("EnCountryName"));
					obj.setDateBirth(rs.getDate("DateBirth"));
					lst.add(obj);					
				}
			}
			catch (Exception ex) {
				logger.error("error in SchoolData---getStudentList-->" , ex);
			}
		 return lst;
	}
	
}
