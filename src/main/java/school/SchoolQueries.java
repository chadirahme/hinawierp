package school;

import model.GradeModel;
import model.ProgramModel;
import model.SchoolModel;
import model.SchoolProgramModel;
import model.SectionModel;

public class SchoolQueries 
{
	StringBuffer query;
	public String getDBCompany(int companyId)
	{
		 String sql="Select * from companiesdb where dbtype='School' and companyid=" + companyId ;
		 return sql;
	}
	public String getSchoolListQuery()
	{
		query=new StringBuffer();
		 query.append("Select SchoolId, EnSchoolName, ArSchoolName, EnAddress, ArAddress, PhoneNo, FaxNo, Email, WebSite, Pobox ");
		 query.append(" from school");
		 return query.toString();		
	}
	
	public String AddNewSchoolQuery(SchoolModel obj)
	{
		query=new StringBuffer();
		query.append("insert into school(EnSchoolName, ArSchoolName, EnAddress, ArAddress, PhoneNo, FaxNo, Email, WebSite, Pobox,"
				+ "SchoolType,AcademicYear,ChangeDateAt,EntrExamNeeded,DependsOnFees,ExamFees,ShowMarks) " +
				"values('%s','%s','%s','%s','%s','%s','%s','%s','%s' ,1,'2015/2016',getDate(),1,1,0,1)");				    
		return query.toString().format(query.toString(),obj.getEnschoolname() , obj.getArschoolname(),obj.getEnaddress(),obj.getAraddress(),
				obj.getPhoneno(),obj.getFaxno(),obj.getEmail(),obj.getWebsite(),obj.getPobox());
	
	}
	
	public String UpdateSchoolQuery(SchoolModel obj)
	{
		query=new StringBuffer();
		query.append("update school set EnSchoolName ='%s', ArSchoolName='%s', EnAddress='%s', ArAddress='%s', PhoneNo='%s',"
				+ " FaxNo='%s', Email='%s', WebSite='%s', Pobox='%s' where SchoolId='%s'");				    
		return query.toString().format(query.toString(),obj.getEnschoolname() , obj.getArschoolname(),obj.getEnaddress(),obj.getAraddress(),
				obj.getPhoneno(),obj.getFaxno(),obj.getEmail(),obj.getWebsite(),obj.getPobox() ,obj.getSchoolid());
	
	}
	
	public String getProgramsListQuery()
	{
		query=new StringBuffer();
		 query.append("Select   ProgramId, EnProgramName, ArProgramName ");
		 query.append(" from Programs");
		 return query.toString();		
	}
	
	public String AddNewProgramQuery(ProgramModel obj)
	{
		query=new StringBuffer();
		query.append("insert into Programs(EnProgramName, ArProgramName) " +
				"values('%s','%s')");				    
		return query.toString().format(query.toString(),obj.getEnprogramname() , obj.getArprogramname());
	
	}
	public String UpdateProgramQuery(ProgramModel obj)
	{
		query=new StringBuffer();
		query.append("update Programs set EnProgramName ='%s', ArProgramName='%s' "
				+ "  where ProgramId='%s'");				    
		return query.toString().format(query.toString(),obj.getEnprogramname() , obj.getArprogramname(),obj.getProgramid());
	
	}
	
	
	public String getGradesListQuery()
	{
		query=new StringBuffer();
		 query.append("Select  GradeId, EnGradeName, ArGradeName ");
		 query.append(" from Grades");
		 return query.toString();		
	}
	
	
	public String UpdateGradeQuery(GradeModel obj)
	{
		query=new StringBuffer();
		query.append("update Grades set EnGradeName ='%s', ArGradeName='%s' "
				+ "  where GradeId='%s'");				    
		return query.toString().format(query.toString(),obj.getEnGradeName() , obj.getArGradeName(),obj.getGradeId());
	
	}
	
	
	public String getSectionsListQuery()
	{
		query=new StringBuffer();
		 query.append("SELECT SectionId, EnSectionName, ArSectionName");
		 query.append(" from Sections");
		 query.append(" order by EnSectionName");
		 return query.toString();		
	}
	
	public String AddNewSectionQuery(SectionModel obj)
	{
		query=new StringBuffer();
		query.append("insert into Sections(EnSectionName, ArSectionName) " +
				"values('%s','%s')");				    
		return query.toString().format(query.toString(),obj.getEnSectionName().toUpperCase() , obj.getArSectionName().toUpperCase());
	
	}
	
	public String UpdateSectionQuery(SectionModel obj)
	{
		query=new StringBuffer();
		query.append("update Sections set EnSectionName ='%s', ArSectionName='%s' "
				+ "  where SectionId='%s'");				    
		return query.toString().format(query.toString(),obj.getEnSectionName().toUpperCase() , obj.getArSectionName().toUpperCase(),obj.getSectionId());
	
	}
	
	
	public String getShoolProgramListQuery(int schoolId)
	{
		query=new StringBuffer();
		 query.append("Select SchoolProgramId,SchoolPrograms.SchoolID,SchoolPrograms.ProgramID,SchoolPrograms.FromGradeID,SchoolPrograms.ToGradeID,Gender, ");
		 query.append("  Grades.EnGradeName as 'FromGradeName', Grades_1.EnGradeName AS 'ToGradeName', Programs.EnProgramName,School.EnSchoolName");
		 query.append(" from SchoolPrograms INNER JOIN Programs ON SchoolPrograms.ProgramID = Programs.ProgramId");
		 query.append(" INNER JOIN Grades ON Grades.GradeId = SchoolPrograms.FromGradeID INNER JOIN Grades AS Grades_1 ON SchoolPrograms.ToGradeID = Grades_1.GradeId");
		 query.append("  inner join School on School.SchoolId=SchoolPrograms.SchoolID");
		 if(schoolId>0)
		 { 
			 query.append(" where SchoolPrograms.SchoolID=" + schoolId	);
			 
		 }
		 return query.toString();		
	}
	
	public String AddNewSchoolProgramQuery(SchoolProgramModel obj)
	{
		query=new StringBuffer();
		query.append("insert into SchoolPrograms(SchoolID, ProgramID,FromGradeID,ToGradeID,Gender) " +
				"values('%s','%s','%s','%s','%s')");				    
		return query.toString().format(query.toString(),obj.getSchoolId(),obj.getProgramId(),obj.getFromGradeId(),obj.getToGradeId(),obj.getGender());
	
	}
	public String updateSchoolProgramQuery(SchoolProgramModel obj)
	{
		query=new StringBuffer();
		query.append("update SchoolPrograms set SchoolID='%s', ProgramID='%s',FromGradeID='%s',ToGradeID='%s',Gender='%s' " +
				" where SchoolProgramId='%s'");				    
		return query.toString().format(query.toString(),obj.getSchoolId(),obj.getProgramId(),obj.getFromGradeId(),obj.getToGradeId(),obj.getGender(),
				obj.getSchoolProgramId());
	
	}
	
	public String deleteSchoolProgramQuery(SchoolProgramModel obj)
	{
		query=new StringBuffer();
		query.append("delete from  SchoolPrograms  "
				+ "  where SchoolProgramId='%s'");				    
		return query.toString().format(query.toString(),obj.getSchoolProgramId());
	
	}
	
	public String GetProgramInSchoolQuery(int schoolId)
	{
		query=new StringBuffer();
		 query.append("SELECT DISTINCT Programs.ProgramId, Programs.EnProgramName, Programs.ArProgramName, SchoolPrograms.SchoolID");
		 query.append(" FROM Programs INNER JOIN SchoolPrograms ON Programs.ProgramId = SchoolPrograms.ProgramID");
		 query.append(" WHERE  SchoolPrograms.SchoolID = " + schoolId);
		 return query.toString();		
	}
	
	public String GetGradeInSchoolQuery(int schoolId,int ProgramId)
	{
		query=new StringBuffer();
		 query.append("SELECT FromGradeID, ToGradeID");
		 query.append(" FROM  SchoolPrograms");	
		 query.append(" WHERE (SchoolPrograms.SchoolID = '%s') AND (SchoolPrograms.ProgramID = '%s') ");
		 query.append(" order by FromGradeID");
		 return query.toString().format(query.toString(),schoolId,ProgramId);		
	}
	public String GetGradeNamesInSchoolQuery(int fromGradeId,int toGradeId)
	{
		query=new StringBuffer();
		 query.append("SELECT GradeId, EnGradeName, ArGradeName");
		 query.append(" FROM  Grades");	
		 query.append(" WHERE GradeId between '%s' AND  '%s' ");
		 query.append(" order by GradeId");
		 return query.toString().format(query.toString(),fromGradeId,toGradeId);		
	}
	
	public String GetSectionsInGradesQuery(int SchoolID, int ProgramID, int GradeID)
	{
		query=new StringBuffer();
		query.append("{call  SSP_GetSectionsInGrades(?,?,?) }");		
		return query.toString().format(query.toString(),SchoolID,ProgramID,GradeID);		
	}
	
	
	
	
	public String getStudentListQuery()
	{
		query=new StringBuffer();
		 query.append("Select top 100 ApplicationNo,AcademicYear,GenderID,EnFullName,ArFullName,HomePhone,OfficePhone,EnSchoolName,EnProgramName,EnGradeName,EnSectionName,EnCountryName,DateBirth ");
		 query.append(" from SVU_NewRegisterDetails");
		 return query.toString();		
	}
	
	
}
