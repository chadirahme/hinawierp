package school;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zul.Messagebox;






import model.GradeModel;
import model.ProgramModel;
import model.SchoolModel;
import model.SchoolProgramModel;
import model.SectionModel;
import model.SectionsInGradeModel;
import model.StudentFilter;
import model.StudentModel;


public class SchoolViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	SchoolData data=new SchoolData();
	private List<SchoolModel> lstSchool;
	private SchoolModel selectedSchool;
	private SchoolModel objSchool;
	
	private List<ProgramModel> lstPrograms;
	private ProgramModel selectedProgram;
	private ProgramModel objProgram;
	
	
	private List<GradeModel> lstGrades;
	private List<SectionModel> lstSections;
	
	private  List<SchoolProgramModel> lstSchoolPrograms;
	private SchoolProgramModel selectedSchoolProgram;
	private List<GradeModel> lstFromGrades;
	private GradeModel selectedFromGrade;
	private List<GradeModel> lstToGrades;
	private GradeModel selectedToGrade;
	private String selectedGender;
	
	private List<SectionsInGradeModel> lstSectionsInGrade;
	private Set<SectionModel> selectedAssignedSections;
	private int maxNoOfStudents;
	  
	private List<StudentModel> lstStudents;
	private List<StudentModel> lstAllStudents;
	private StudentFilter studentFilter=new StudentFilter();
	private static final String footerStudentMessage = "A Total of %d Students";
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private String viewType;
	@Init	
	public void init(@BindingParam("type") String type)
	{
		try
		{
			viewType=type ==null ? "" : type;
			if(viewType.equals("school"))
	 		{
				
				lstSchool=data.getSchoolList();	
				if(lstSchool.size()>0)
				selectedSchool=lstSchool.get(0);
	 		}
			if(viewType.equals("program"))
	 		{
				lstPrograms=data.getProgramsList("");
				if(lstPrograms.size()>0)
				selectedProgram=lstPrograms.get(0);
				
				objProgram=new ProgramModel();
	 		}
			
			if(viewType.equals("grades"))
	 		{
				lstGrades=data.getGradeList("");
				
	 		}
			
			if(viewType.equals("sections"))
			{
				lstSections=data.getSectionList("");
			}
			
			if(viewType.equals("schoolprogram"))
			{
			selectedGender="1";
			lstSchool=data.getSchoolList();	
			if(lstSchool.size()>0)
			selectedSchool=lstSchool.get(0);
			lstPrograms=data.getProgramsList("Select");
			if(lstPrograms.size()>0)
			selectedProgram=lstPrograms.get(0);
			lstFromGrades=data.getGradeList("Select");
			if(lstFromGrades.size()>0)
			selectedFromGrade=lstFromGrades.get(0);
			lstToGrades=data.getGradeList("Select");
			if(lstToGrades.size()>0)
			selectedToGrade=lstToGrades.get(0);
			
			lstSchoolPrograms=data.getSchoolProgramList(0);	
			}
			
			if(viewType.equals("assignsection"))
			{
				lstSchool=data.getSchoolList();	
				if(lstSchool.size()>0)
				selectedSchool=lstSchool.get(0);
				//lstPrograms=data.getProgramsList("Select");
				//selectedProgram=lstPrograms.get(0);
				//lstFromGrades=data.getGradeList("Select");
				//selectedFromGrade=lstFromGrades.get(0);
				lstSections=data.getSectionList("");
				
			}
			
			if(viewType.equals("students"))
			{
				lstPageSize=new ArrayList<Integer>();
				lstPageSize.add(15);
				lstPageSize.add(30);
				lstPageSize.add(50);
				
				selectedPageSize=lstPageSize.get(0);
								
				lstStudents=data.getStudentList();
				lstAllStudents=lstStudents;
				
			}
		}
		
		catch(Exception e)
		{
			logger.error("error at SchoolViewModel >>> Init >>> " ,e);
		}
	}
	
	public SchoolViewModel()
	{
	
		
		/*lstPrograms=data.getProgramsList();
		selectedProgram=lstPrograms.get(0);
		objProgram=new ProgramModel();
		
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		
		selectedPageSize=lstPageSize.get(0);*/
	}
	
	@Command
	@NotifyChange({"objSchool"})
	public void addNewSchoolCommand()
	{
		objSchool=new SchoolModel();	
		objSchool.setEnschoolname("");
		objSchool.setArschoolname("");
		objSchool.setEnaddress("");
		objSchool.setAraddress("");
		objSchool.setWebsite("");
		objSchool.setEmail("");
		objSchool.setPhoneno("");
		objSchool.setPobox("");
		objSchool.setMobile("");
		objSchool.setFaxno("");
	
	}
	public String getStudentFooter() {
        return String.format(footerStudentMessage, lstStudents.size());
    }
	
	private List<StudentModel> filterData()
	{
		lstStudents=lstAllStudents;
		
		List<StudentModel> lst=new ArrayList<StudentModel>();
		for (Iterator<StudentModel> i = lstStudents.iterator(); i.hasNext();)
		{
			StudentModel tmp=i.next();				
			if(tmp.getApplicationNo().toLowerCase().contains(studentFilter.getApplicationNo())&&
					tmp.getEnFullName().toLowerCase().contains(studentFilter.getEnFullName().toLowerCase())&&										
					tmp.getArFullName().toLowerCase().contains(studentFilter.getArFullName().toLowerCase()))
			{
				lst.add(tmp);
			}
		}
		return lst;
		
	}
	
	  @Command
	    @NotifyChange({"lstStudents","studentFooter"})
	    public void changeFilter() 
	    {	      
		  lstStudents=filterData();
	    }
	 
	@Command	
	 @NotifyChange({"lstSchool","selectedSchool"})
	public void saveSchoolCommand()
	{
		try
		{
			if(objSchool.getEnschoolname().equals(""))
			{
				Messagebox.show("Please enter English school name!!","School Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(objSchool.getArschoolname().equals(""))
			{
				Messagebox.show("Please enter Arabic school name!!","School Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			data.SaveShoolData(objSchool);
			lstSchool=data.getSchoolList();				
			selectedSchool=lstSchool.get(0);
			Messagebox.show("School is saved.","School Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			addNewSchoolCommand();
		}
		catch(Exception e)
		{
			logger.error("error at SchoolViewModel >>> saveSchoolCommand >>> " ,e);
		}
		
	}
	
	@Command
	@NotifyChange({"lstPrograms"})
	public void changeProgramEditableStatus(@BindingParam("row") ProgramModel item)
	{
		item.setEditingStatus(!item.isEditingStatus());	
	}
	
	@Command
	@NotifyChange({"lstPrograms"})
	public void addNewProgramCommand()
	{
		for (ProgramModel item : lstPrograms) 
		{
			if(item.getProgramid()==0)
			{
				Messagebox.show("Please save the added program first !!","Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		
		ProgramModel obj=new ProgramModel();
		obj.setProgramid(0);
		obj.setEnprogramname("");
		obj.setArprogramname("");
		obj.setEditingStatus(true);
		lstPrograms.add(0, obj);
				
	}
	@Command
	@NotifyChange({"lstPrograms","selectedProgram"})
	public void saveProgramCommand(@BindingParam("row") ProgramModel item)
	{
		try
		{
			if(item.getEnprogramname().equals(""))
			{
				Messagebox.show("Please enter English program name!!","Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(item.getArprogramname().equals(""))
			{
				Messagebox.show("Please enter Arabic program name!!","Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			for (ProgramModel obj : lstPrograms) 
			{
				if ( obj!=item && ( item.getEnprogramname().equalsIgnoreCase(obj.getEnprogramname())
						|| item.getArprogramname().equalsIgnoreCase(
								obj.getArprogramname()))) 
				{
					
						Messagebox.show("Program already exists !!",
								"Program Setup", Messagebox.OK,
								Messagebox.EXCLAMATION);
						return;
					
				}
								
			}
									
			data.SaveProgramData(item);
			lstPrograms=data.getProgramsList("");
			//selectedProgram=lstPrograms.get(0);
			//addNewProgramCommand();
			Messagebox.show("Program is saved.","Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			//addNewSchoolCommand();
		}
		catch(Exception e)
		{
			logger.error("error at SchoolViewModel >>> saveProgramCommand >>> " ,e);
		}
	}
	
	@Command
	@NotifyChange({"lstGrades"})
    public void changeEditableStatus(@BindingParam("row") GradeModel item) 
	{
		item.setEditingStatus(!item.isEditingStatus());		
    }
	
	@Command
	@NotifyChange({"lstGrades"})
	public void saveGradeCommand(@BindingParam("row") GradeModel item)
	{
		if(item.getEnGradeName().equals(""))
		{
			Messagebox.show("Please enter English grade name!!","Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(item.getArGradeName().equals(""))
		{
			Messagebox.show("Please enter Arabic grade name!!","Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		data.SaveGradeData(item);
		lstGrades=data.getGradeList("");
		Messagebox.show("Grade is saved.","Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
	}
	
	@Command
	@NotifyChange({"lstSections"})
	public void changeSectionEditableStatus(@BindingParam("row") SectionModel item)
	{
		item.setEditingStatus(!item.isEditingStatus());	
	}
	
	@Command
	@NotifyChange({"lstSections"})
	public void saveSectionCommand(@BindingParam("row") SectionModel item)
	{
		if(item.getEnSectionName().equals(""))
		{
			Messagebox.show("Please enter English section name!!","Section Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		if(item.getArSectionName().equals(""))
		{
			Messagebox.show("Please enter Arabic section name!!","Section Setup", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		
			for (SectionModel obj : lstSections) 
			{
				if ( obj!=item && ( item.getEnSectionName().equalsIgnoreCase(obj.getEnSectionName())
						|| item.getArSectionName().equalsIgnoreCase(
								obj.getArSectionName()))) 
				{
					
						Messagebox.show("Section already exists !!",
								"Section Setup", Messagebox.OK,
								Messagebox.EXCLAMATION);
						return;
					
				}
								
			}
		
		
		
		data.SaveSectionData(item);
		lstSections=data.getSectionList("");
		Messagebox.show("Section is saved.","Section Setup", Messagebox.OK , Messagebox.EXCLAMATION);
	}
	
	@Command
	@NotifyChange({"lstSections"})
	public void addNewSectionCommand()
	{
		for (SectionModel item : lstSections) 
		{
			if(item.getSectionId()==0)
			{
				Messagebox.show("Please save the added section first !!","Section Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		
		SectionModel obj=new SectionModel();
		obj.setSectionId(0);
		obj.setEnSectionName("");
		obj.setArSectionName("");
		obj.setEditingStatus(true);
		lstSections.add(0, obj);
		
	}
	
	@Command
	@NotifyChange({"lstSchoolPrograms"})
	public void saveSchoolProgramCommand()
	{
		try
		{
			SchoolProgramModel obj=new SchoolProgramModel();
			obj.setSchoolId(selectedSchool.getSchoolid());
			obj.setProgramId(selectedProgram.getProgramid());
			obj.setFromGradeId(selectedFromGrade.getGradeId());
			obj.setToGradeId(selectedToGrade.getGradeId());
			obj.setGender(Integer.parseInt(selectedGender));
			
			if(obj.getSchoolId()==0)
			{
				Messagebox.show("Please select school first !!","School Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(obj.getProgramId()==0)
			{
				Messagebox.show("Please select program first !!","School Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(obj.getFromGradeId()==0)
			{
				Messagebox.show("Please select from grade first !!","School Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			if(obj.getToGradeId()==0)
			{
				Messagebox.show("Please select to grade first !!","School Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			
			obj.setSchoolProgramId(selectedSchoolProgram.getSchoolProgramId());
			data.SaveSchoolProgram(obj);
			lstSchoolPrograms=data.getSchoolProgramList(0);	
			Messagebox.show("Program Grades is saved.","School Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
		}
		catch(Exception e)
		{
			logger.error("error at SchoolViewModel >>> saveSchoolProgramCommand >>> " ,e);
		}
	}
	
	@Command
	@NotifyChange({"selectedSchool","selectedProgram","selectedFromGrade","selectedToGrade"})
	public void addNewSchoolProgramCommand()
	{
		selectedSchoolProgram=new SchoolProgramModel();
		selectedSchool=lstSchool.get(0);
		selectedProgram=lstPrograms.get(0);		
		selectedFromGrade=lstFromGrades.get(0);	
		selectedToGrade=lstToGrades.get(0);
	}
	
	@Command
	@NotifyChange({"selectedSchool","selectedProgram","selectedFromGrade","selectedToGrade","selectedGender"})
	public void editSchoolProgramCommand(@BindingParam("row") SchoolProgramModel item)
	{
		selectedSchoolProgram=item;
		for (SchoolModel obj : lstSchool)
		{
			if(obj.getSchoolid()==item.getSchoolId())
			{
				selectedSchool=obj;
			}
		}
		for (ProgramModel obj : lstPrograms)
		{
			if(obj.getProgramid()==item.getProgramId())
			{
				selectedProgram=obj;
			}
		}
		
		for (GradeModel obj : lstFromGrades)
		{
			if(obj.getGradeId()==item.getFromGradeId())
			{
				selectedFromGrade=obj;
			}
		}
		
		for (GradeModel obj : lstToGrades)
		{
			if(obj.getGradeId()==item.getToGradeId())
			{
				selectedToGrade=obj;
			}
		}
		
		selectedGender=item.getGender()+"";
	}
	
	@Command
	@NotifyChange({"lstSchoolPrograms"})
	public void deleteSchoolProgramCommand(@BindingParam("row") SchoolProgramModel item)
	{
		data.deleteSchoolProgram(item);
		lstSchoolPrograms=data.getSchoolProgramList(0);
		Messagebox.show("Program Grades is deleted.","School Program Setup", Messagebox.OK , Messagebox.EXCLAMATION);
	}
	
	@Command
	@NotifyChange({"lstSectionsInGrade"})
	public void saveAssignedSectionsCommand()
	{
		try 
		{	
			if(selectedProgram==null)
			{
				Messagebox.show("Please select program first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(selectedFromGrade==null)
			{
				Messagebox.show("Please select grade first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			 SectionsInGradeModel obj=new SectionsInGradeModel();
			 obj.setSchoolId(selectedSchool.getSchoolid());
			 obj.setProgramId(selectedProgram.getProgramid());
			 obj.setGradeId(selectedFromGrade.getGradeId());
			 obj.setMaxStudents(maxNoOfStudents);
			  if(obj.getSchoolId()==0)
				{
					Messagebox.show("Please select school first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(obj.getProgramId()==0)
				{
					Messagebox.show("Please select program first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(obj.getGradeId()==0)
				{
					Messagebox.show("Please select grade first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
				}
				if(selectedAssignedSections==null)
				{
					 Messagebox.show("Please select sections first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 return; 
				}
				if(selectedAssignedSections.size()==0)
				{
					 Messagebox.show("Please select sections first !!","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.EXCLAMATION);
					 return; 
				}
				
			
			data.saveAssigendSections(obj, selectedAssignedSections);			
			lstSectionsInGrade=data.GetSectionsInGrades(selectedSchool.getSchoolid(), selectedProgram.getProgramid(),0);
			Messagebox.show("Assigned sections saved.","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.INFORMATION);
		} 
		catch(Exception e)
		{
			logger.error("error at SchoolViewModel >>> saveAssignedSectionsCommand >>> " ,e);
		}
	}
	@Command
	@NotifyChange({"lstSectionsInGrade"})
	public void deleteAssignedSectionsCommand(@BindingParam("row") SectionsInGradeModel item)
	{
		data.deleteAssigendSections(item);
		lstSectionsInGrade=data.GetSectionsInGrades(selectedSchool.getSchoolid(), selectedProgram.getProgramid(),0);
		Messagebox.show("Assigned sections deleted.","Assign Sections to Grade Setup", Messagebox.OK , Messagebox.INFORMATION);
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

	
	@NotifyChange({"objSchool","lstPrograms","selectedProgram","lstSectionsInGrade"})
	public void setSelectedSchool(SchoolModel selectedSchool) 
	{
		
		this.selectedSchool = selectedSchool;	
		if(viewType.equals("school"))
 		{
		if(this.selectedSchool.getSchoolid()!=0)
			objSchool=this.selectedSchool;
		else
			objSchool=new SchoolModel();
 		}
		
		else if(viewType.equals("assignsection"))
 		{
			lstPrograms=data.GetProgramInSchool(selectedSchool.getSchoolid(),"Select");
			selectedProgram=lstPrograms.get(0);
			lstSectionsInGrade=null;
						
 		}
				
	}
	
	public SchoolModel getObjSchool() {
		return objSchool;
	}

	public void setObjSchool(SchoolModel objSchool) {
		this.objSchool = objSchool;
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

	@NotifyChange({"objProgram","lstFromGrades","lstSectionsInGrade"})
	public void setSelectedProgram(ProgramModel selectedProgram) 
	{
		try
		{
		this.selectedProgram = selectedProgram;
		
		if(viewType.equals("school"))
 		{
		if(this.selectedProgram.getProgramid()!=0)
		{
		setObjProgram(this.selectedProgram);	
		}
		else
		{
			setObjProgram(new ProgramModel());
		}
 		}
		
		else if(viewType.equals("assignsection"))
 		{
			lstFromGrades=data.GetGradeInSchool(selectedSchool.getSchoolid(), selectedProgram.getProgramid(), "");	
			lstSectionsInGrade=data.GetSectionsInGrades(selectedSchool.getSchoolid(), selectedProgram.getProgramid(),0);
 		}
		}
		
		catch(Exception e)
		{
			logger.error("error at SchoolViewModel >>> setSelectedProgram >>> " ,e);
		}
				
	}	
	
	public Validator getFormValidator()
	 {
		 return new AbstractValidator() 
		 {
			 public void validate(ValidationContext ctx) 
			 {
				 String enprogramname = (String)ctx.getProperties("enprogramname")[0].getValue();
				 String arprogramname = (String)ctx.getProperties("arprogramname")[0].getValue();
				 
				 if (enprogramname == null || enprogramname.isEmpty()) 
				 {
	                    // put error message into validationMessages map
	                    addInvalidMessage(ctx, "enprogramnameContentError", "English program name is required ");
	             }
				 if (arprogramname == null || arprogramname.isEmpty()) 
				 {
	                    // put error message into validationMessages map
	                    addInvalidMessage(ctx, "enprogramnameContentError", "Arabic program name is required ");
	             }
			 }
			 
		 };
	 }

	public ProgramModel getObjProgram() {
		return objProgram;
	}

	public void setObjProgram(ProgramModel objProgram) {
		this.objProgram = objProgram;
	}

	public List<StudentModel> getLstStudents() 
	{
		
		return lstStudents;
	}

	public void setLstStudents(List<StudentModel> lstStudents) {
		this.lstStudents = lstStudents;
	}

	public StudentFilter getStudentFilter() {
		return studentFilter;
	}

	public void setStudentFilter(StudentFilter studentFilter) {
		this.studentFilter = studentFilter;
	}

	public List<Integer> getLstPageSize() {
		return lstPageSize;
	}

	public void setLstPageSize(List<Integer> lstPageSize) {
		this.lstPageSize = lstPageSize;
	}

	public Integer getSelectedPageSize() {
		return selectedPageSize;
	}

	public void setSelectedPageSize(Integer selectedPageSize) {
		this.selectedPageSize = selectedPageSize;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public List<GradeModel> getLstGrades() {
		return lstGrades;
	}

	public void setLstGrades(List<GradeModel> lstGrades) {
		this.lstGrades = lstGrades;
	}

	public List<SectionModel> getLstSections() {
		return lstSections;
	}

	public void setLstSections(List<SectionModel> lstSections) {
		this.lstSections = lstSections;
	}

	public List<SchoolProgramModel> getLstSchoolPrograms() {
		return lstSchoolPrograms;
	}

	public void setLstSchoolPrograms(List<SchoolProgramModel> lstSchoolPrograms) {
		this.lstSchoolPrograms = lstSchoolPrograms;
	}

	public List<GradeModel> getLstFromGrades() {
		return lstFromGrades;
	}

	public void setLstFromGrades(List<GradeModel> lstFromGrades) {
		this.lstFromGrades = lstFromGrades;
	}

	public GradeModel getSelectedFromGrade() {
		return selectedFromGrade;
	}

	//@NotifyChange({"lstSections"})
	public void setSelectedFromGrade(GradeModel selectedFromGrade) 
	{
		this.selectedFromGrade = selectedFromGrade;
		//if(viewType.equals("assignsection"))
		//{
		//lstSections=data.getSectionList("");
		//}
	}

	public List<GradeModel> getLstToGrades() {
		return lstToGrades;
	}

	public void setLstToGrades(List<GradeModel> lstToGrades) {
		this.lstToGrades = lstToGrades;
	}

	public GradeModel getSelectedToGrade() {
		return selectedToGrade;
	}

	public void setSelectedToGrade(GradeModel selectedToGrade) {
		this.selectedToGrade = selectedToGrade;
	}

	public String getSelectedGender() {
		return selectedGender;
	}

	public void setSelectedGender(String selectedGender) {
		this.selectedGender = selectedGender;
	}

	public List<SectionsInGradeModel> getLstSectionsInGrade() {
		return lstSectionsInGrade;
	}

	public void setLstSectionsInGrade(List<SectionsInGradeModel> lstSectionsInGrade) {
		this.lstSectionsInGrade = lstSectionsInGrade;
	}

	public Set<SectionModel> getSelectedAssignedSections() {
		return selectedAssignedSections;
	}

	public void setSelectedAssignedSections(Set<SectionModel> selectedAssignedSections) {
		this.selectedAssignedSections = selectedAssignedSections;
	}

	public int getMaxNoOfStudents() {
		return maxNoOfStudents;
	}

	public void setMaxNoOfStudents(int maxNoOfStudents) {
		this.maxNoOfStudents = maxNoOfStudents;
	}

}
