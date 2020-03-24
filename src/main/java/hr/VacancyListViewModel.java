package hr;

import hr.model.CompanyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.EmployeeFilter;
import model.EmployeeModel;
import model.HRListValuesModel;
import model.VacancyModel;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Tabbox;

import setup.users.WebusersModel;

public class VacancyListViewModel {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	HRData data=new HRData();
	VacancyListData vancancyData=new VacancyListData();
	private List<VacancyModel> lstVacancies;
	private VacancyModel selectedVacacies;
	private EmployeeFilter employeeFilter=new EmployeeFilter();
	private List<VacancyModel> lstAllVacacies;
	private static final String footerMessage = "Total Companies : %d";
	private static final String footerMessage1 = "Toatal Departments : %d";
	private static final String footerMessage2 = "Total Positions :%d ";
	private static final String footerMessage3 = "Total Available :%d ";
	private static final String footerMessage4 = "Total Vacancies : %d";
	private static final String footerMessage5 = "Total Cvs : %d";
	private static final String footerMessage6 = "Total Max.Allowed : %d";
	private List<Integer> lstPageSize;
	private Integer selectedPageSize;
	private List<String> lstAllPageSize;
	private String selectedAllPageSize;
	private boolean adminUser;
	private List<CompanyModel> lstComapnies;
	private CompanyModel selectedCompany;
	WebusersModel dbUser;
	int positions=0;
	String postionCompare="";
	int totlDepatment=0;
	String departmentCompare="";
	int totlAvailabe=0;	
	int totlMaxAllowed=0;
	int totlVacancies=0;
	int totlCvs=0;	
	
	public VacancyListViewModel()
	{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
		if(dbUser!=null)
		{
			adminUser=dbUser.getFirstname().equals("admin");
		}
		int defaultCompanyId=0;
		//defaultCompanyId=data.getDefaultCompanyID(dbUser.getUserid());
		lstComapnies=data.getCompanyList(dbUser.getUserid());
		/*for (CompanyModel item : lstComapnies) 
		{
		if(item.getCompKey()==defaultCompanyId)
			selectedCompany=item; 
		}
		if(lstComapnies.size()>1 && defaultCompanyId==0)*/		
		selectedCompany=lstComapnies.get(0);
		
		
		lstVacancies=vancancyData.getMainQuerry(selectedCompany.getCompKey(),dbUser.getUserid());
		
		for(VacancyModel model:lstVacancies)
		{
			if(!model.getDepartment().equalsIgnoreCase(departmentCompare))
			{
				departmentCompare=model.getDepartment();
				totlDepatment=totlDepatment+1;
			}
			if(!model.getPosition().equalsIgnoreCase(postionCompare))
			{
				postionCompare=model.getPosition();
				positions=positions+1;
			}
			
			totlAvailabe=totlAvailabe+model.getAvailable();
			totlMaxAllowed=totlMaxAllowed+model.getEmpAllowed();
			totlVacancies=totlVacancies+model.getVacancies();
			totlCvs=totlCvs+model.getCvs();
			
			
			
		}
		
		if(lstVacancies.size()>0)
			selectedVacacies=lstVacancies.get(0);
		lstAllVacacies=lstVacancies;	
		
		lstPageSize=new ArrayList<Integer>();
		lstPageSize.add(15);
		lstPageSize.add(30);
		lstPageSize.add(50);
		lstPageSize.add(lstVacancies.size());
		
		selectedPageSize=lstPageSize.get(2);
		
		lstAllPageSize=new ArrayList<String>();
		lstAllPageSize.add("15");
		lstAllPageSize.add("30");
		lstAllPageSize.add("50");
		lstAllPageSize.add("All");
		selectedAllPageSize=lstAllPageSize.get(2);
		//dateofbirth=selectedEmployee.getDateOfBirth();
		
	}

	
		
		private List<VacancyModel> filterData()
		{
			lstVacancies=lstAllVacacies;
					
			List<VacancyModel> lst=new ArrayList<VacancyModel>();
			for (Iterator<VacancyModel> i = lstVacancies.iterator(); i.hasNext();)
			{
				VacancyModel tmp=i.next();				
						if(tmp.getDepartment().toLowerCase().contains(employeeFilter.getDepartment().toLowerCase())&&
						tmp.getPosition().toLowerCase().contains(employeeFilter.getPosition().toLowerCase())
								&&
								Integer.toString(tmp.getEmpAllowed()).toLowerCase().contains(employeeFilter.getEmpAllowed().toLowerCase())
										&&
										Integer.toString(tmp.getAvailable()).toLowerCase().contains(employeeFilter.getAvailable().toLowerCase())
												&&
												Integer.toString(tmp.getVacancies()).toLowerCase().contains(employeeFilter.getVacancies().toLowerCase())
												&&
												Integer.toString(tmp.getCvs()).toLowerCase().contains(employeeFilter.getCvs().toLowerCase())
												&&
												tmp.getCompanyName().toLowerCase().contains(employeeFilter.getCompanyName().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
			return lst;
			
		}
		
		 @Command
		   public void resetVacany()
		   {
			   try
			   {
				   Borderlayout bl = (Borderlayout) Path.getComponent("/hbaSideBar");
					 Center center = bl.getCenter();
					 Tabbox tabbox=(Tabbox)center.getFellow("mainContentTabbox");
					 tabbox.getSelectedPanel().getLastChild().invalidate();
			   }
			   catch (Exception ex)
				{	
					logger.error("ERROR in VacancyListViewModel ----> resetVacany", ex);			
				}
		   }
		
		
		public String getFooter() {
	        return String.format(footerMessage, 1);
	    }
		public String getFooter1() {
	        return String.format(footerMessage1, totlDepatment );
	    }
		public String getFooter2() {
	        return String.format(footerMessage2, positions);
	    }
		public String getFooter3() {
	        return String.format(footerMessage3, totlAvailabe);
	    }
		public String getFooter4() {
	        return String.format(footerMessage4, totlVacancies);
	    }
		public String getFooter5() {
	        return String.format(footerMessage5,totlCvs);
	    }
		public String getFooter6() {
	        return String.format(footerMessage6,totlMaxAllowed);
	    }
		
		@Command
	    @NotifyChange({"lstVacancies","footer"})
	    public void changeFilter() 
	    {	      
			lstVacancies=filterData();
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

		public boolean isAdminUser() {
			return adminUser;
		}

		public void setAdminUser(boolean adminUser) {
			this.adminUser = adminUser;
		}

	

		public List<String> getLstAllPageSize() {
			return lstAllPageSize;
		}

		public void setLstAllPageSize(List<String> lstAllPageSize) {
			this.lstAllPageSize = lstAllPageSize;
		}

		public String getSelectedAllPageSize() {
			return selectedAllPageSize;
		}
		@NotifyChange({"selectedPageSize"})	
		public void setSelectedAllPageSize(String selectedAllPageSize) 
		{
			this.selectedAllPageSize = selectedAllPageSize;
			if(selectedAllPageSize.equals("All"))
				selectedPageSize=lstVacancies.size();
			else
				selectedPageSize=Integer.parseInt(selectedAllPageSize);
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

		@NotifyChange({"lstVacancies","selectedVacacies","footer","totlDepatment","positions","footer1","footer2","footer3","footer4","footer5","footer6"})	
		public void setSelectedCompany(CompanyModel selectedCompany) 
		{
			Session sess = Sessions.getCurrent();
			dbUser=(WebusersModel)sess.getAttribute("Authentication");
			this.selectedCompany = selectedCompany;
			lstVacancies=vancancyData.getMainQuerry(selectedCompany.getCompKey(),dbUser.getUserid());			
			if(lstVacancies.size()>0)
				selectedVacacies=lstVacancies.get(0);
			lstAllVacacies=lstVacancies;	
			postionCompare="";
			departmentCompare="";
			positions=0;
			totlDepatment=0;
			totlAvailabe=0;
			totlMaxAllowed=0;
			totlVacancies=0;
			totlCvs=0;
			for(VacancyModel model:lstVacancies)
			{
				if(!model.getDepartment().equalsIgnoreCase(departmentCompare))
				{
					departmentCompare=model.getDepartment();
					totlDepatment=totlDepatment+1;
				}
				if(!model.getPosition().equalsIgnoreCase(postionCompare))
				{
					postionCompare=model.getPosition();
					positions=positions+1;
				}
				
				totlAvailabe=totlAvailabe+model.getAvailable();
				totlMaxAllowed=totlMaxAllowed+model.getEmpAllowed();
				totlVacancies=totlVacancies+model.getVacancies();
				totlCvs=totlCvs+model.getCvs();
				
			}
			getFooter();
			getFooter1();
			getFooter2();
			getFooter3();
			getFooter4();
			getFooter5();
			getFooter6();
		}

		
		

		/**
		 * @return the lstVacancies
		 */
		public List<VacancyModel> getLstVacancies() {
			return lstVacancies;
		}



		/**
		 * @param lstVacancies the lstVacancies to set
		 */
		public void setLstVacancies(List<VacancyModel> lstVacancies) {
			this.lstVacancies = lstVacancies;
		}



		/**
		 * @return the selectedVacacies
		 */
		public VacancyModel getSelectedVacacies() {
			return selectedVacacies;
		}



		/**
		 * @param selectedVacacies the selectedVacacies to set
		 */
		public void setSelectedVacacies(VacancyModel selectedVacacies) {
			this.selectedVacacies = selectedVacacies;
		}



		/**
		 * @return the employeeFilter
		 */
		public EmployeeFilter getEmployeeFilter() {
			return employeeFilter;
		}

		/**
		 * @param employeeFilter the employeeFilter to set
		 */
		public void setEmployeeFilter(EmployeeFilter employeeFilter) {
			this.employeeFilter = employeeFilter;
		}



		/**
		 * @return the lstAllVacacies
		 */
		public List<VacancyModel> getLstAllVacacies() {
			return lstAllVacacies;
		}



		/**
		 * @param lstAllVacacies the lstAllVacacies to set
		 */
		public void setLstAllVacacies(List<VacancyModel> lstAllVacacies) {
			this.lstAllVacacies = lstAllVacacies;
		}

		

	
		
		
		
		

}
