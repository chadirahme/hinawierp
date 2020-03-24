package hba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import model.CustomerModel;
import model.DataFilter;
import model.JobModel;

public class GarageViewModel 
{
	HBAData data=new HBAData();
	private List<JobModel> lstJobs;
	private List<JobModel> lstAllJobs;
	private JobModel selectedJob;
	
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private DataFilter filter=new DataFilter();
	
	public GarageViewModel()
	{
		Calendar c = Calendar.getInstance();
		filter=new DataFilter();
		filter.setCreatedDate(c.getTime());
		
		
		lstJobs=data.getJobGarageList();
		lstAllJobs=lstJobs;
		
		if(lstJobs.size()>0)
		selectedJob=lstJobs.get(0);
		
	}
	
	private List<JobModel> filterData()
	{
		lstJobs=lstAllJobs;
		List<JobModel> lst=new ArrayList<JobModel>();
		for (Iterator<JobModel> i = lstJobs.iterator(); i.hasNext();)
		{
			JobModel tmp=i.next();				
			 if(String.valueOf(tmp.getRefNo()).contains(filter.getJobCardNO())&&
			  df.format(tmp.getTxnDate()).contains(filter.getTxnDate())&&
			  df.format(tmp.getTxnTime()).contains(filter.getTxnTime())&&
			   tmp.getSaName().toLowerCase().contains(filter.getName().toLowerCase())&&
			   df.format(tmp.getWorkStart()).contains(filter.getRecieveDate())&&
			   
			    tmp.getChasisNo().toLowerCase().contains(filter.getArname().toLowerCase())&&			  
				tmp.getRegNo().toLowerCase().contains(filter.getFullName().toLowerCase())&&			  
				tmp.getEngineNo().toLowerCase().contains(filter.getCompanyName().toLowerCase())&&			  
				tmp.getFullName().toLowerCase().contains(filter.getBillCountry().toLowerCase())&&		
				df.format(tmp.getWorkStart()).contains(filter.getWorkStartDate())&&
				df.format(tmp.getWorkEnd()).contains(filter.getWorkEndDate())&&
				String.valueOf(tmp.getOdometer()).contains(filter.getOdometer().toLowerCase())&&		
				tmp.getRoutNo().toLowerCase().contains(filter.getRouteNo().toLowerCase())&&		
				
				tmp.getStatus().toLowerCase().contains(filter.getStatus().toLowerCase())&&	
				tmp.getBrand().toLowerCase().contains(filter.getBrand().toLowerCase())&&
				tmp.getSeries().toLowerCase().contains(filter.getJobType().toLowerCase())&&
				tmp.getNotes().toLowerCase().contains(filter.getNote().toLowerCase())						
				
					)
			{
				lst.add(tmp);
			}
		}
		
		return lst;
	}
	
	@Command
    @NotifyChange({"lstJobs"})
    public void changeFilter() 
    {	      
	 lstJobs=filterData();
    }

	public List<JobModel> getLstJobs() {
		return lstJobs;
	}

	public void setLstJobs(List<JobModel> lstJobs) {
		this.lstJobs = lstJobs;
	}

	public JobModel getSelectedJob() {
		return selectedJob;
	}

	public void setSelectedJob(JobModel selectedJob) {
		this.selectedJob = selectedJob;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}

	public List<JobModel> getLstAllJobs() {
		return lstAllJobs;
	}

	public void setLstAllJobs(List<JobModel> lstAllJobs) {
		this.lstAllJobs = lstAllJobs;
	}
}
