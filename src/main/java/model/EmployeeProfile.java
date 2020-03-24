package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EmployeeProfile 
{

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	private int employeeKey;
	private String employeeNo;
	private String fullName;
	//@JsonSerialize(using = JsonDateSerializer.class)
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	private String empDateofBirth;
	private String companyName;
	
	
	
	public int getEmployeeKey() {
		return employeeKey;
	}
	public void setEmployeeKey(int employeeKey) {
		this.employeeKey = employeeKey;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	//@JsonSerialize(using = JsonDateSerializer.class)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEmpDateofBirth() {
		
		return formatter.format(dateOfBirth);		
	}
	public void setEmpDateofBirth(String empDateofBirth) {
		this.empDateofBirth = empDateofBirth;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
