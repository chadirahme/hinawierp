package hr;

import hr.model.CompanyModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EmployeeModel;
import model.HRListValuesModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Form;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import setup.users.WebusersModel;

public class EditEmployeeViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	String HREMPSerialNOS ="EMPNOS";
	boolean SelCompWise=false;
	
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private EmployeeModel selectedEmployee;
	
	private List<HRListValuesModel> lstDepartment;
	private HRListValuesModel selectedDepartment;
	
	private List<HRListValuesModel> lstPosition;
	private HRListValuesModel selectedPosition;
	
	private List<HRListValuesModel> lstNationality;
	private HRListValuesModel selectedNationality;
	
	private List<HRListValuesModel> lstGender;
	private HRListValuesModel selectedGender;
	
	private List<HRListValuesModel> lstBlood;
	private HRListValuesModel selectedBlood;
	
	private List<HRListValuesModel> lstMarital;
	private HRListValuesModel selectedMarital;
	
	private List<HRListValuesModel> lstStatus;
	private HRListValuesModel selectedStatus;
	
	private List<HRListValuesModel> lstCountryOfBirth;
	private HRListValuesModel selectedCountryOfBirth;
	
	private List<HRListValuesModel> lstReligion;
	private HRListValuesModel selectedReligion;
	
	private Date dateofbirth;
	private int age;
	private String employeeNo;
	private boolean canSave;
	int compKey=0;
	private String employeeEmail;
	private int employeeKey;
	public EditEmployeeViewModel()
	{
		try
		{
			Execution exec = Executions.getCurrent();
			Session sess = Sessions.getCurrent();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			Map map = exec.getArg();
			int empKey=(Integer) map.get("empKey");
			compKey=(Integer) map.get("compKey");
			String type=(String)map.get("type");
			if(!type.equals("view"))
				canSave=true;
			//logger.info(empKey+"");
			
			if(type.equals("email"))
			{
				employeeEmail="";
				employeeKey=empKey;
				return;
			}
			
			lstComapnies=data.getCompanyList(dbUser.getUserid());
			lstDepartment=data.getDepartemnet(compKey);
			if(lstDepartment.size()>0)
			selectedDepartment=lstDepartment.get(0);
			lstNationality=data.getHRListValues(1,"");
			lstGender=data.getHRListValues(4, "");		
			lstBlood=data.getHRListValues(5, "");
			lstMarital=data.getHRListValues(9, "");
			lstStatus=data.getHRListValues(28, "");
			lstCountryOfBirth=data.getHRListValues(2, "");
			lstReligion=data.getHRListValues(25, "");
			
			if(empKey>0)
			{
			selectedEmployee=data.getEmployeeByKey(empKey);
			employeeNo=selectedEmployee.getEmployeeNo();
			lstPosition=data.getPostion(selectedDepartment.getListId(),compKey);
			for (CompanyModel item : lstComapnies) 
			{
				if(item.getCompKey()==selectedEmployee.getCompanyID())
					selectedCompany=item;
			}
			if(lstPosition!=null && lstPosition.size()>0)
			selectedPosition=lstPosition.get(0);
			fillEmployeeInfo();
			}
			else
			{
			selectedEmployee=new EmployeeModel();
			selectedEmployee.setCompanyID(compKey);
			for (CompanyModel item : lstComapnies) 
			{
				if(item.getCompKey()==selectedEmployee.getCompanyID())
					selectedCompany=item;
			}
			lstPosition=data.getPostion(selectedDepartment.getListId(),compKey);
			if(lstPosition!=null &&lstPosition.size()>0)
			{
			selectedPosition=lstPosition.get(0);
			}
			selectedEmployee.setLocal("1");
			selectedEmployee.setEnMiddleName("");
			selectedEmployee.setEnLastName("");
			selectedEmployee.setArFirstName("");
			selectedEmployee.setArMiddleName("");
			selectedEmployee.setArLastName("");
			selectedEmployee.setPlaceOfBirth("");
			
			//GetMaXEMPNO
			selectedEmployee.setEmployeeNo(GetMaXEMPNO(compKey));
			employeeNo=selectedEmployee.getEmployeeNo();
					
			}						
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditEmployeeViewModel ----> init", ex);			
		}
	}
	
	private String GetMaXEMPNO(int compKey)
	{
		String GENERATE_EMPNO=data.GetMaXEMPNO();
		//boolean SelCompWise=false;
		if(GENERATE_EMPNO.equals("G"))
			SelCompWise=false;
		else
		   SelCompWise=true;
			
		String GetMaXEMPNO="";
		if(SelCompWise)
		{
			GetMaXEMPNO=GetSerialNumber(HREMPSerialNOS, compKey);
		}
		else
		{
			GetMaXEMPNO=GetSerialNumber(HREMPSerialNOS, 0);
		}
		return GetMaXEMPNO;
	}
	private String GetSerialNumber(String field,int compId)
	{
		String tmpField="";
		String prefix="";
		String tmpSeralNo="";
		if(compId==0)
			tmpField=field;
		else
			tmpField=field+"-"+compId;
		
		if(compId>0)
		{
			prefix=data.GetEmpNoPrefix(compId);
		}
		
		String LastNumber=data.GetSystemSerialNos(tmpField);
		if(!LastNumber.equals(""))
			tmpSeralNo=LastNumber;
		else
			tmpSeralNo=prefix+"1";
		
		//ConfigSerialNumber("", tmpSeralNo, 0);
		return tmpSeralNo;
	}
	
	private void ConfigSerialNumber(String field,String SerialNumber,int compId)
	{
		try
		{
		String tmpField="";		
		String findNumber="";
		String prefix="";
		
		if(compId==0)
			tmpField=field;
		else
			tmpField=field+"-"+compId;
		
		for(int i=0;i<SerialNumber.length();i++)
		{
		   if(Character.isDigit(SerialNumber.charAt(i)))
			   findNumber+=SerialNumber.charAt(i);
		}
		logger.info(">>>> " +findNumber);
		int newEmpNo=Integer.parseInt(findNumber)+1;
		
		if(compId>0)
		{
			prefix=data.GetEmpNoPrefix(compId);
		}
		
		String tmpSeralNo=prefix+newEmpNo;
		String LastNumber=data.GetSystemSerialNos(tmpField);
		if(LastNumber.equals(""))
		data.UpdateSystemSerialNo(tmpField, tmpSeralNo,true);
		else
		data.UpdateSystemSerialNo(tmpField, tmpSeralNo,false);	
		
		
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditEmployeeViewModel ----> ConfigSerialNumber", ex);			
		}
		
	}
	private void fillEmployeeInfo()
	{
		dateofbirth=selectedEmployee.getDateOfBirth();
		age=Integer.valueOf(selectedEmployee.getAge());
		
		for (CompanyModel item : lstComapnies) 
		{
			if(item.getCompKey()==selectedEmployee.getCompanyID())
				selectedCompany=item;
		}
		for (HRListValuesModel item : lstDepartment) 
		{
		if(item.getListId()==selectedEmployee.getDepartmentID())
		{
			selectedDepartment=item;
			setSelectedDepartment(selectedDepartment);
			break;
		}
		
		}
		for (HRListValuesModel item : lstPosition) 
		{
			if(item.getListId()==selectedEmployee.getPositionID())
			{
				selectedPosition=item;
				break;
			}
		}
		
		for (HRListValuesModel item : lstNationality) 
		{
		if(item.getListId()==selectedEmployee.getNationalityID())	
		{
			selectedNationality=item;
			break;
		}
		}
		
		for (HRListValuesModel item : lstGender) 
		{
		if(item.getListId()==selectedEmployee.getSexId())	
		{
			selectedGender=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstBlood) 
		{
		if(item.getListId()==selectedEmployee.getBloodType())	
		{
			selectedBlood=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstMarital) 
		{
		if(item.getListId()==selectedEmployee.getMaritalID())	
		{
			selectedMarital=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstStatus) 
		{
		if(item.getListId()==selectedEmployee.getStatusId())	
		{
			selectedStatus=item;
			break;
		}
		
		}
		
		for (HRListValuesModel item : lstReligion) 
		{
		if(item.getListId()==selectedEmployee.getReligionId())	
		{
			selectedReligion=item;
			break;
		}
		
		}
		
		if(selectedEmployee.getCountryOfBirth()>0)
		{
		for (HRListValuesModel item : lstCountryOfBirth) 
		{
		if(item.getListId()==selectedEmployee.getCountryOfBirth())	
		{
			selectedCountryOfBirth=item;
			break;
		}		
		}
		}
		
		
	}
	@Command
	public void saveEmployeeEmailCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		 if(employeeEmail == null || !employeeEmail.matches(".+@.+\\.[a-z]+")) 
		 {
			 Messagebox.show("Please enter a valid email!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
			 return;
		 }
		else
		{
			if(employeeKey>0)
			data.addEmployeeEmail(employeeKey, employeeEmail);
			
			Map args = new HashMap();
			args.put("employeeEmail", employeeEmail);		
			BindUtils.postGlobalCommand(null, null, "refreshEmailParent", args);
			
			comp.detach();
		}
	}
	@Command
	public void saveEmployeeCommand(@ContextParam(ContextType.VIEW) Window comp)
	{
		try
		{
			int employeeid=0;
			selectedEmployee.setCompKey(selectedCompany.getCompKey());
			selectedEmployee.setDepartmentID(selectedDepartment.getListId());
			selectedEmployee.setPositionID(selectedPosition.getListId());
			selectedEmployee.setReligionId(selectedReligion.getListId());
			selectedEmployee.setSexId(selectedGender.getListId());
			selectedEmployee.setStatusId(selectedStatus.getListId());
			selectedEmployee.setMaritalID(selectedMarital.getListId());
			selectedEmployee.setEmployeeNo(employeeNo);
			
			if(selectedBlood!=null)
			selectedEmployee.setBloodType(selectedBlood.getListId());
			if(selectedCountryOfBirth!=null)
			selectedEmployee.setCountryOfBirth(selectedCountryOfBirth.getListId());
			selectedEmployee.setDateOfBirth(dateofbirth);
			if(selectedNationality!=null)
			selectedEmployee.setNationalityID(selectedNationality.getListId());
			selectedEmployee.setAge(String.valueOf(age));
			
			
			List<EmployeeModel> lst=data.checkEmployeeNoExist(selectedEmployee.getCompKey());
			if (selectedEmployee.getEmployeeKey() ==0)
			{
				for(EmployeeModel employeeModel:lst)
				{
					if(employeeModel.getEmployeeNo().replaceAll("\\s+","").equalsIgnoreCase(employeeNo.replaceAll("\\s+","")))
					{
						Messagebox.show("Employee number exist for this company  !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
						return;
					}
				}
			}
			else
			{
				for(EmployeeModel employeeModel:lst)
				{
					if(employeeModel.getEmployeeNo().replaceAll("\\s+","").equalsIgnoreCase(employeeNo.replaceAll("\\s+","")) && employeeModel.getEmployeeKey()!=selectedEmployee.getEmployeeKey())
					{
					Messagebox.show("Employee number exist for this company  !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
					return;
					}
				}
					
				
			}
			
			if(age==0)
			{
				Messagebox.show("Please set a valid date of birth  !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(selectedEmployee.getEmail()==null || selectedEmployee.getEmail().equalsIgnoreCase(""))
			{
				Messagebox.show("Please set email for employee  !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			Date startBusinessDate=data.getCompanyStartBussiness(selectedCompany.getCompKey());
			if(selectedEmployee.getEmployeementDate().compareTo(df.parse(sdf1.format(startBusinessDate.getTime())))==-1)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
				Messagebox.show("Start work date should be greater than Business Start Date " +sdf.format(startBusinessDate),"Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(selectedEmployee.getEmployeementDate().compareTo(selectedEmployee.getDateOfBirth())==-1)
			{
				Messagebox.show("Start Work Date should be greater than Date of Birth  !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			
			int result=0;
			employeeid=selectedEmployee.getEmployeeKey();
			if(selectedEmployee.getEmployeeKey()>0)
			result=data.editEmployees(selectedEmployee);
			else
			{
			result=data.addNewEmployee(selectedEmployee);
			//update new employee No
			if(result>0)
			{
				if(SelCompWise)
				{
					ConfigSerialNumber(HREMPSerialNOS, selectedEmployee.getEmployeeNo(), selectedCompany.getCompKey());
				}
				else
				{
					ConfigSerialNumber(HREMPSerialNOS, selectedEmployee.getEmployeeNo(), 0);
				}
			
			}
			}
			
			if(result==1)
			{
				if(employeeid>0)
				{
					 Clients.showNotification("The Employee Has Been Updated Successfully.",
					            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}
				else
				{
					 Clients.showNotification("The Employee Has Been Created Successfully.",
					            Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 10000,true);
				}
				 
				
			Map args = new HashMap();
			args.put("compKey", selectedCompany.getCompKey());		
			BindUtils.postGlobalCommand(null, null, "refreshParent", args);
			}
			else
			{
			Clients.showNotification("Erro at save Employee !!");
			}
			comp.detach();
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in EditEmployeeViewModel ----> saveEmployee", ex);			
		}
		
	}
	
	@Command
	@NotifyChange({"age"})	
	public void changeBirthDate()
	{	
		setAge(0);
		Calendar dob = Calendar.getInstance();
		dob.setTime(dateofbirth);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
		age--;
		
		if(age<=0)
			age=0;
		
		int intAgeFrom=0;
		int intAgeTo=0;
		boolean blnExistEmpSetup=false;
		
		List<Integer> lstAges=data.GetEmployeeAgeRange(selectedCompany.getCompKey());
		if(lstAges.size()>1)
		{
			intAgeFrom=lstAges.get(0);
			intAgeTo=lstAges.get(1);
			blnExistEmpSetup=true;
		}
		if(blnExistEmpSetup)
		{
		if(age<intAgeFrom || age>intAgeTo)
		{
			Messagebox.show("The Age Limit must between the Range from " + intAgeFrom + " to " + intAgeTo,"Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
			return;
		}
		
		}
		else
		{
			if(age<16)
			{
				Messagebox.show("Age must be greater than or equal to 16 !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
			if(age>65)
			{
				Messagebox.show("Age must be less than or equal to 65 !!","Edit Employee", Messagebox.OK , Messagebox.EXCLAMATION);
				return;
			}
		}
		
		setAge(age);
		//selectedEmployee.setAge(String.valueOf(age));
	}

	public Validator getTodoValidator(){
		return new AbstractValidator() {							
			public void validate(ValidationContext ctx) {
				//get the form that will be applied to todo
				//EmployeeModel fx = (EmployeeModel)ctx.getProperty().getValue();							
				//get filed firstname of the form
				 String employeeType =(String)ctx.getProperties("employeeType")[0].getValue();// fx.getEmployeeType();
				 String local =(String)ctx.getProperties("local")[0].getValue(); //fx.getLocal();
				//Date employeementDate=fx.getEmployeementDate();
				
				String enFirstName =(String)ctx.getProperties("enFirstName")[0].getValue();
				String enMiddleName =(String)ctx.getProperties("enMiddleName")[0].getValue();// fx.getEnMiddleName();
				String enLastName =(String)ctx.getProperties("enLastName")[0].getValue(); //fx.getEnLastName();
				Date employeementDate =(Date)ctx.getProperties("employeementDate")[0].getValue();
				
				if(Strings.isBlank(enFirstName))
				{
					Clients.showNotification("English First Name Required !!");
					ctx.setInvalid();
				}
				if(Strings.isBlank(enMiddleName) && Strings.isBlank(enLastName))
				{
					Clients.showNotification("English Middle Name or Last Name Required !!");
					ctx.setInvalid();
				}
				if(employeementDate==null)
				{
					Clients.showNotification("Employeement Start Work Date Required !!");
					ctx.setInvalid();
				}
				
				if(local.equals("0"))
				{
					if(selectedNationality==null)
					{
						Clients.showNotification("Please select Nationality !!");
						ctx.setInvalid();
					}
				}
				
				
				
				if(Strings.isBlank(employeeType) ||selectedDepartment==null ||selectedPosition==null ||selectedReligion==null ||selectedGender==null
						|| selectedMarital==null || selectedStatus==null || dateofbirth==null )//|| selectedEmployee.getEmployeementDate()==null)
				{
					Clients.showNotification("Please fill all the required fields (*)  !!");
					//mark the validation is invalid, so the data will not update to bean
					//and the further command will be skipped.
					ctx.setInvalid();
				}										
			}
		};
	}
	
	public List<CompanyModel> getLstComapnies() {
		return lstComapnies;
	}

	public void setLstComapnies(List<CompanyModel> lstComapnies) {
		this.lstComapnies = lstComapnies;
	}

	public CompanyModel getSelectedCompany() {
		return selectedCompany;
	}

	@NotifyChange({"employeeNo"})	
	public void setSelectedCompany(CompanyModel selectedCompany) 
	{
		this.selectedCompany = selectedCompany;
		if(selectedEmployee.getEmployeeKey()==0)
		{
			selectedEmployee.setEmployeeNo(GetMaXEMPNO(selectedCompany.getCompKey()));
			employeeNo=selectedEmployee.getEmployeeNo();
		}
		
	}

	public EmployeeModel getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(EmployeeModel selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<HRListValuesModel> getLstDepartment() {
		return lstDepartment;
	}

	public void setLstDepartment(List<HRListValuesModel> lstDepartment) {
		this.lstDepartment = lstDepartment;
	}

	public HRListValuesModel getSelectedDepartment() {
		return selectedDepartment;
	}


	@NotifyChange({"lstPosition","selectedPosition"})	
	public void setSelectedDepartment(HRListValuesModel selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
		lstPosition=data.getPostion(selectedDepartment.getListId(),compKey);
		selectedPosition=lstPosition.get(0);
	}

	public List<HRListValuesModel> getLstPosition() {
		return lstPosition;
	}

	public void setLstPosition(List<HRListValuesModel> lstPosition) {
		this.lstPosition = lstPosition;
	}

	public HRListValuesModel getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(HRListValuesModel selectedPosition) {
		this.selectedPosition = selectedPosition;
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

	public List<HRListValuesModel> getLstGender() {
		return lstGender;
	}

	public void setLstGender(List<HRListValuesModel> lstGender) {
		this.lstGender = lstGender;
	}

	public HRListValuesModel getSelectedGender() {
		return selectedGender;
	}

	public void setSelectedGender(HRListValuesModel selectedGender) {
		this.selectedGender = selectedGender;
	}

	public List<HRListValuesModel> getLstBlood() {
		return lstBlood;
	}

	public void setLstBlood(List<HRListValuesModel> lstBlood) {
		this.lstBlood = lstBlood;
	}

	public HRListValuesModel getSelectedBlood() {
		return selectedBlood;
	}

	public void setSelectedBlood(HRListValuesModel selectedBlood) {
		this.selectedBlood = selectedBlood;
	}

	public List<HRListValuesModel> getLstMarital() {
		return lstMarital;
	}

	public void setLstMarital(List<HRListValuesModel> lstMarital) {
		this.lstMarital = lstMarital;
	}

	public HRListValuesModel getSelectedMarital() {
		return selectedMarital;
	}

	public void setSelectedMarital(HRListValuesModel selectedMarital) {
		this.selectedMarital = selectedMarital;
	}

	public List<HRListValuesModel> getLstStatus() {
		return lstStatus;
	}

	public void setLstStatus(List<HRListValuesModel> lstStatus) {
		this.lstStatus = lstStatus;
	}

	public HRListValuesModel getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(HRListValuesModel selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public List<HRListValuesModel> getLstCountryOfBirth() {
		return lstCountryOfBirth;
	}

	public void setLstCountryOfBirth(List<HRListValuesModel> lstCountryOfBirth) {
		this.lstCountryOfBirth = lstCountryOfBirth;
	}

	public HRListValuesModel getSelectedCountryOfBirth() {
		return selectedCountryOfBirth;
	}

	public void setSelectedCountryOfBirth(HRListValuesModel selectedCountryOfBirth) {
		this.selectedCountryOfBirth = selectedCountryOfBirth;
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

	public Date getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public boolean isCanSave() {
		return canSave;
	}

	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}
}
