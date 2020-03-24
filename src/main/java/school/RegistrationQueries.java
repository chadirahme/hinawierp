package school;

import java.text.SimpleDateFormat;

import model.ApplicantModel;

public class RegistrationQueries 
{
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public String getDBCompany(int companyId)
	{
		 String sql="Select * from companiesdb where dbtype='School' and companyid=" + companyId ;
		 return sql;
	}
	
	public String getNationalityQuery(int fieldID)
	{
		 query=new StringBuffer();
		 query.append("select * from HRListValues where FIELD_ID='%s' order by DESCRIPTION") ;
		 return query.toString().format(query.toString(),fieldID);
	}
	
	public String AddNewApplicantQuery(ApplicantModel obj)
	{		
		query=new StringBuffer();
		query.append("insert into Applicant(ApplicationNumber,SchoolId,ProgramId, AcademicYear, GradeId, EnFirstName, EnMiddleName, EnLastName,"
				+ " ArFirstName, ArLastName, ArMiddleName,IsArab,GenderID, ReligionId, DateOfBirth, "
				+ " CountryOfBirthId, NationalityId, FatherMobile, FatherEmail, MotherMobile, MotherEmail, "
				+ "AppStatus, UserId, CreationDate, ModifiedDate, RejectReason) " +
				"values('%s','%s','%s','%s','%s','%s','%s','%s',"
				+ " '%s','%s','%s','%s','%s','%s','%s',"
				+ "'%s','%s','%s','%s','%s','%s',"
				+ "'%s','%s',getDate(),getDate(),'%s')");				    
		return query.toString().format(query.toString(),obj.getApplicationNumber(),obj.getSchoolId(),obj.getProgramId(),obj.getAcademicYear(),
				obj.getGradeId(),obj.getEnFirstName(),obj.getEnMiddleName(),obj.getEnLastName(),obj.getArFirstName(),obj.getArLastName(),obj.getArMiddleName(),
				obj.getIsArab(),obj.getGenderID(),obj.getReligionId(),sdf.format(obj.getDateOfBirth()),obj.getCountryOfBirthId(),obj.getNationalityId(),
				obj.getFatherMobile(),obj.getFatherEmail(),obj.getMotherMobile(),obj.getMotherEmail(),obj.getAppStatus(),obj.getUserId(),
				obj.getRejectReason());
	
	}
	
	
}
