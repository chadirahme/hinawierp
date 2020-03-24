package hr;

import hr.model.CompanyModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class EmployeeLedgerGroupViewModel 
{
	private boolean showGroup = true;
	private Logger logger = Logger.getLogger(this.getClass());
	HRData data=new HRData();
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	private List<String> lstPeriods;
	private String selectedPeriods;
	private List<String> lstStatus;
	private String selectedStatus;
	private Date fromDate;
	private Date toDate;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public EmployeeLedgerGroupViewModel() throws ParseException
	{
		Calendar c = Calendar.getInstance();			
		fromDate=df.parse("01/01/2010");//df.parse(sdf.format(c.getTime()));		
		toDate=df.parse("09/09/2010");//df.parse(sdf.format(c.getTime()));
	}
	
	public EmployeeLedgerModel getActivityModel()
	{
		return null;
		//return new EmployeeLedgerModel(data.GetEMPSalary(192, "192", sdf.format(fromDate),sdf.format(toDate), "Active","Chadi Rahme"), new ActivityComparator(), showGroup);
		
	}
}
