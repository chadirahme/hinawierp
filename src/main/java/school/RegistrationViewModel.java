package school;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.ApplicantModel;
import model.CountryModel;
import model.GradeModel;
import model.HRListValuesModel;
import model.ProgramModel;
import model.SchoolModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.util.Clients;

import setup.users.WebusersModel;

public class RegistrationViewModel 
{

	private Logger logger = Logger.getLogger(this.getClass());
	private String viewType;
	RegistrationData data=new RegistrationData();
	SchoolData setupData=new SchoolData();
	
	private List<SchoolModel> lstSchool;
	private SchoolModel selectedSchool;
	private List<ProgramModel> lstPrograms;
	private ProgramModel selectedProgram;
	private List<GradeModel> lstGrades;
	private GradeModel selectedGrade;
	
	private List<String> lstDays;
	private List<String> lstMonths;
	private List<String> lstYears;
	
	private String selectedDay;
	private String selectedMonth;
	private String selectedYear;
	
	private List<HRListValuesModel> lstNationality;
	private HRListValuesModel selectedNationality;
	private List<HRListValuesModel> lstCountryofBirth;
	private HRListValuesModel selectedCountryofBirth;
	private List<HRListValuesModel> lstReligion;
	private HRListValuesModel selectedReligion;
	
	private String selectedNative;
	private String selectedGender;
	
	private ApplicantModel objApplicant;
	@Init	
	public void init(@BindingParam("type") String type)
	{
		try
		{
			viewType=type ==null ? "" : type;
			if(viewType.equals("newapplicant"))
			{
			lstSchool=setupData.getSchoolList();				
			selectedSchool=lstSchool.get(0);
			
			lstPrograms=setupData.getProgramsList("Select");
			selectedProgram=lstPrograms.get(0);
			
			fillDOBDDL();
			selectedNative="1";
			selectedGender="1";
			objApplicant=new ApplicantModel();
			
			}
			
		}
		catch(Exception e)
		{
			logger.error("error at RegistrationViewModel >>> Init >>> " ,e);
		}
	}

	private void fillDOBDDL()
	{
		try
		{
			lstDays=new ArrayList<String>();
			lstMonths=new ArrayList<String>();
			lstYears=new ArrayList<String>();
			
			lstDays.add("Select");
			lstYears.add("Select");
			lstMonths.add("Select");
			for (int i = 1; i < 32; i++) 
			{				
				lstDays.add(i+"");
			}
								
			for (int i = 2011; i > 1990; i--) 
			{
				lstYears.add(i+"");
			}
			String[] months = new DateFormatSymbols().getMonths();
	        for (int i = 0; i < months.length; i++)
	        {
	            String month = months[i];
	           lstMonths.add(month);
	        }
	        
	        selectedDay=lstDays.get(0);
	        selectedMonth=lstMonths.get(0);
	        selectedYear=lstYears.get(0);
	        
	        lstNationality=data.getHRListValuesList("Select", 1);
	        selectedNationality=lstNationality.get(0);
	        lstCountryofBirth=data.getHRListValuesList("Select", 2);
	        selectedCountryofBirth=lstCountryofBirth.get(0);
	        lstReligion=data.getHRListValuesList("Select", 25);
	        selectedReligion=lstReligion.get(0);
	        		

		}
		catch(Exception e)
		{
			logger.error("error at RegistrationViewModel >>> fillDOBDDL >>> " ,e);
		}
	}
	
	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) 
			{				
				//logger.info(ctx.getProperty().getValue());
				//get the form that will be applied to todo
				//ApplicantModel fx = (ApplicantModel)ctx.getProperty().getValue();
				//get filed firstname of the form
				String firstname =  (String)ctx.getProperties("enFirstName")[0].getValue();
				String lastname =  (String)ctx.getProperties("enLastName")[0].getValue();
				String enMiddleName =  (String)ctx.getProperties("enMiddleName")[0].getValue();
				
				String arFirstName =  (String)ctx.getProperties("arFirstName")[0].getValue();
				String arMiddleName =  (String)ctx.getProperties("arMiddleName")[0].getValue();
				String arLastName =  (String)ctx.getProperties("arLastName")[0].getValue();
				
				String fatherMobile =  (String)ctx.getProperties("fatherMobile")[0].getValue();
				String fatherEmail =  (String)ctx.getProperties("fatherEmail")[0].getValue();
				String motherMobile =  (String)ctx.getProperties("motherMobile")[0].getValue();
				String motherEmail =  (String)ctx.getProperties("motherEmail")[0].getValue();
				
				/*if(Strings.isBlank(lastname)){
					Clients.showNotification("Please fill the Last name !!");
					//mark the validation is invalid, so the data will not update to bean
					//and the further command will be skipped.
					ctx.setInvalid();
				}*/
								
				if(Strings.isBlank(firstname))
				{
					  addInvalidMessage(ctx, "English First Name is required !");
				}
				if(Strings.isBlank(enMiddleName))
				{
					addInvalidMessage(ctx, "English Middle Name is required !");
				}
				if(Strings.isBlank(lastname))
				{
					addInvalidMessage(ctx, "English Last Name is required !");
				}
				
				if(Strings.isBlank(arFirstName))
				{
					  addInvalidMessage(ctx, "Arabic First Name is required !");
				}
				if(Strings.isBlank(arMiddleName))
				{
					addInvalidMessage(ctx, "Arabic Middle Name is required !");
				}
				if(Strings.isBlank(arLastName))
				{
					addInvalidMessage(ctx, "Arabic Last Name is required !");
				}
				
				if(selectedSchool.getSchoolid()==0)
				{
					addInvalidMessage(ctx, "School is required !");
				}
				
				if(selectedProgram.getProgramid()==0)
				{
					addInvalidMessage(ctx, "Program is required !");
				}
				if(selectedGrade==null)
				{
					addInvalidMessage(ctx, "Grade is required !");
					return;
				}
				
				if(selectedGrade.getGradeId()==0)
				{
					addInvalidMessage(ctx, "Grade is required !");
				}
				
				if(Strings.isBlank(fatherMobile))
				{
					addInvalidMessage(ctx, "Father Mobile is required !");
				}
				if(Strings.isBlank(fatherEmail))
				{
					addInvalidMessage(ctx, "Father Email is required !");
				}
				if(Strings.isBlank(motherMobile))
				{
					addInvalidMessage(ctx, "Mother Mobile is required !");
				}
				if(Strings.isBlank(motherEmail))
				{
					addInvalidMessage(ctx, "Mother Email is required !");
				}
				
				if(selectedCountryofBirth.getFieldId()==0)
				{
					addInvalidMessage(ctx, "Country of Birth is required !");
				}
				if(selectedNationality.getFieldId()==0)
				{
					addInvalidMessage(ctx, "Nationality is required !");
				}
				if(selectedReligion.getFieldId()==0)
				{
					addInvalidMessage(ctx, "Religion is required !");
				}
				
				if(selectedYear.equals("Select") || selectedMonth.equals("Select") || selectedDay.equals("Select"))
				{
					addInvalidMessage(ctx, "Invalid Date of Birth !");				
				}
				else
				{
				 try
				 {
				
				int day = Integer.parseInt(selectedDay);  //25
				//int month = Integer.parseInt(selectedMonth); //12
				int year = Integer.parseInt(selectedYear); // 1988
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMMM/dd");
				
				String date = year + "/" + selectedMonth + "/" + day;
				objApplicant.setDateOfBirth(formatter.parse(date));
				//Calendar c = Calendar.getInstance();
				//c.set(year, month, day, 0, 0);  
				
				}
				
				 catch(Exception e)
				 {
						logger.error("error at RegistrationViewModel >>> validation >>> " ,e);
				 }
				}
								 
			}
		};			
	}
	
	@Command
	@NotifyChange({"objApplicant"})
	public void saveApplicantCommand()
	{
		try		
		{
			objApplicant.setIsArab(selectedNative.equals("1")?true:false);
			objApplicant.setGenderID(Integer.parseInt(selectedGender));
			objApplicant.setCountryOfBirthId(selectedCountryofBirth.getFieldId());
			objApplicant.setNationalityId(selectedNationality.getFieldId());
			objApplicant.setReligionId(selectedReligion.getFieldId());
			objApplicant.setRejectReason("");
			objApplicant.setAcademicYear("2015-2016");
			objApplicant.setAppStatus(1);
			objApplicant.setApplicationNumber("");
			objApplicant.setUserId(1);
			
			int appId=data.AddNewApplicant(objApplicant);
			if(appId>0)
			{
			Clients.showNotification("Application Saved take id = " + appId);
			objApplicant=new ApplicantModel();
			}
			else
			{
			 Clients.showNotification("Error at Application Saved..");
			}
			
		}
		catch(Exception e)
		{
			logger.error("error at RegistrationViewModel >>> saveApplicantCommand >>> " ,e);
		}
	}
	
	
	public List<SchoolModel> getLstSchool() {
		return lstSchool;
	}

	public void setLstSchool(List<SchoolModel> lstSchool) {
		this.lstSchool = lstSchool;
	}

	public SchoolModel getSelectedSchool() {
		return selectedSchool;
	}

	public void setSelectedSchool(SchoolModel selectedSchool) {
		this.selectedSchool = selectedSchool;
	}

	public List<ProgramModel> getLstPrograms() {
		return lstPrograms;
	}

	public void setLstPrograms(List<ProgramModel> lstPrograms) {
		this.lstPrograms = lstPrograms;
	}

	public ProgramModel getSelectedProgram() {
		return selectedProgram;
	}

	@NotifyChange({"lstGrades","selectedGrade"})
	public void setSelectedProgram(ProgramModel selectedProgram)
	{
		try
		{
		this.selectedProgram = selectedProgram;
		
		
		if(viewType.equals("newapplicant"))
 		{
			lstGrades=setupData.GetGradeInSchool(selectedSchool.getSchoolid(), selectedProgram.getProgramid(), "Select");	
			selectedGrade=lstGrades.get(0);
 		}
		}
		
		catch(Exception e)
		{
			logger.error("error at RegistrationViewModel >>> setSelectedProgram >>> " ,e);
		}
	}

	public List<GradeModel> getLstGrades() {
		return lstGrades;
	}

	public void setLstGrades(List<GradeModel> lstGrades) {
		this.lstGrades = lstGrades;
	}

	public GradeModel getSelectedGrade() {
		return selectedGrade;
	}

	public void setSelectedGrade(GradeModel selectedGrade) {
		this.selectedGrade = selectedGrade;
	}

	public List<String> getLstDays() {
		return lstDays;
	}

	public void setLstDays(List<String> lstDays) {
		this.lstDays = lstDays;
	}

	public List<String> getLstMonths() {
		return lstMonths;
	}

	public void setLstMonths(List<String> lstMonths) {
		this.lstMonths = lstMonths;
	}

	public List<String> getLstYears() {
		return lstYears;
	}

	public void setLstYears(List<String> lstYears) {
		this.lstYears = lstYears;
	}

	public String getSelectedDay() {
		return selectedDay;
	}

	public void setSelectedDay(String selectedDay) {
		this.selectedDay = selectedDay;
	}

	public String getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(String selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public String getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	public List<HRListValuesModel> getLstNationality() {
		return lstNationality;
	}

	public void setLstNationality(List<HRListValuesModel> lstNationality) {
		this.lstNationality = lstNationality;
	}

	public HRListValuesModel getSelectedNationality() {
		return selectedNationality;
	}

	public void setSelectedNationality(HRListValuesModel selectedNationality) {
		this.selectedNationality = selectedNationality;
	}

	public List<HRListValuesModel> getLstCountryofBirth() {
		return lstCountryofBirth;
	}

	public void setLstCountryofBirth(List<HRListValuesModel> lstCountryofBirth) {
		this.lstCountryofBirth = lstCountryofBirth;
	}

	public HRListValuesModel getSelectedCountryofBirth() {
		return selectedCountryofBirth;
	}

	public void setSelectedCountryofBirth(HRListValuesModel selectedCountryofBirth) {
		this.selectedCountryofBirth = selectedCountryofBirth;
	}

	public List<HRListValuesModel> getLstReligion() {
		return lstReligion;
	}

	public void setLstReligion(List<HRListValuesModel> lstReligion) {
		this.lstReligion = lstReligion;
	}

	public HRListValuesModel getSelectedReligion() {
		return selectedReligion;
	}

	public void setSelectedReligion(HRListValuesModel selectedReligion) {
		this.selectedReligion = selectedReligion;
	}

	public ApplicantModel getObjApplicant() {
		return objApplicant;
	}

	public void setObjApplicant(ApplicantModel objApplicant) {
		this.objApplicant = objApplicant;
	}

	public String getSelectedNative() {
		return selectedNative;
	}

	public void setSelectedNative(String selectedNative) {
		this.selectedNative = selectedNative;
	}

	public String getSelectedGender() {
		return selectedGender;
	}

	public void setSelectedGender(String selectedGender) {
		this.selectedGender = selectedGender;
	}
}
