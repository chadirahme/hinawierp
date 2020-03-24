package reports;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import model.TimeSheetDataModel;
import model.TimeSheetGridModel;

import org.zkoss.exporter.util.GroupsModelArrayAdapter;


public class ReportsGroupAdapter  extends GroupsModelArrayAdapter<TimeSheetDataModel, String[], String[], Object>
{

	 private static final long serialVersionUID = 1L;
     
		private static final String footerString = "Total No Of Days: %d ";
		private boolean showGroup;
		
		
		public ReportsGroupAdapter(List<TimeSheetDataModel> data,Comparator<TimeSheetDataModel> cmpr,boolean showGroup)
		{
			 super(data.toArray(new TimeSheetDataModel[0]), cmpr);
			 this.showGroup = showGroup;
		}
		
		protected String[] createGroupHead(TimeSheetDataModel[] groupdata, int index, int col) 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String[] header=new String[20];
	        int serailnumber=0;
	        /*int totalOT=0;
			 int totalPresent=0;
			 int totalLeave=0;
			 int tolalabsence=0;
			 int totalHoliday=0;*/
	        if (groupdata.length > 0) 
	        {
	        	//header[0] ="Emp. NO : " +groupdata[0].getEmpNo() + " Employee Name : "  + groupdata[0].getEmployeeName() + " Status :" + groupdata[0].getEmployeeStatus();
	        	header[0]=groupdata[0].getEmpNo();
	        	header[1]=groupdata[0].getEnFullName();
	        	header[2]=groupdata[0].getTsStatus();
	        	//header[3]=String.valueOf(groupdata[0].getSrNo());
	        //	header[4]=groupdata[0].getSalaryStatus();
	        	header[5]=groupdata[0].getEnPositionName();
	        	//header[6]=groupdata[0].getApproved()==1?" (Approved)":" (Created)";
	        	header[7]=""+groupdata[0].getTotalOverTime();
	        	serailnumber=serailnumber+1;
	        	header[10]=""+serailnumber;
	        }
	       /* for(int i=0;i<groupdata.length;i++)
	        {
	        	if(groupdata[i].getStatus().equalsIgnoreCase("P"))
				 {
					 totalPresent= (totalPresent + groupdata[i].getUnits());
				 }
				 else if(groupdata[i].getStatus().equalsIgnoreCase("A"))
				 {
					 tolalabsence= (tolalabsence + groupdata[i].getUnitNO());
				 }
				else if(groupdata[i].getStatus().equalsIgnoreCase("L"))
				{
						totalLeave= (totalLeave + groupdata[i].getUnitNO());
				}
				else if(groupdata[i].getStatus().equalsIgnoreCase("S"))
				{
						totalLeave= (totalLeave + groupdata[i].getUnitNO());
				}
				else
				{
					totalHoliday= (totalHoliday + groupdata[i].getUnitNO());
				}
	        	totalOT=totalOT+groupdata[i].getTotalOverTime();
	        }
	        header[11]=""+totalPresent;
	        header[12]=""+tolalabsence;
	        header[13]=""+totalLeave;
	        header[14]=""+totalHoliday;
	        header[15]=""+totalOT;*/
	        
	 
	        return header;
	    }
	 
	    protected String[] createGroupFoot(TimeSheetDataModel[] groupdata, int index, int col) 
	    {
	    	 String[] footer=new String[20];
		        double totalOT=0;
				 double totalTempPresent=0;
				 double totalTempLeave=0;
				 double tolaTemplabsence=0;
				 double totalTempHoliday=0;
				 
				 int dummyLopp=1;
				 int empKey=0;
				 int totoatlNoofdays=1;
				 Date tempDate = null;
	    	
	    	for(int i=0;i<groupdata.length;i++)
	        {
	    		if(dummyLopp==1)
	    		{
	    			empKey=groupdata[i].getEmpKey();
	    			tempDate=groupdata[i].getTsDate();
	    		   	dummyLopp=dummyLopp+1;
	    		}
	    		
	    		if(!groupdata[i].getTsDate().equals(tempDate))
	    		{
	    			totoatlNoofdays=totoatlNoofdays+1;
	    			tempDate=groupdata[i].getTsDate();
	    		}
	        	if(groupdata[i].getStatus().equalsIgnoreCase("P"))
				 {
	        		totalTempPresent= (totalTempPresent + groupdata[i].getUnits());
				 }
				 else if(groupdata[i].getStatus().equalsIgnoreCase("A"))
				 {
					 tolaTemplabsence= (tolaTemplabsence + groupdata[i].getUnitNO());
				 }
				else if(groupdata[i].getStatus().equalsIgnoreCase("L"))
				{
					totalTempLeave=(totalTempLeave + groupdata[i].getUnitNO());
				}
				else if(groupdata[i].getStatus().equalsIgnoreCase("S"))
				{
					totalTempLeave= (totalTempLeave + groupdata[i].getUnitNO());
				}
				else if(groupdata[i].getStatus().equalsIgnoreCase("H") && groupdata[i].getCalcFlag().equalsIgnoreCase("Yes"))
				{
					totalTempHoliday=(totalTempHoliday + groupdata[i].getUnitNO());
				}
	        	totalOT=totalOT+groupdata[i].getTotalOverTime();
	        }
	    	footer[11]=""+ Double.parseDouble(new DecimalFormat("#.##").format(totalTempPresent));
	    	footer[12]=""+ Double.parseDouble(new DecimalFormat("#.##").format(totalTempLeave));
	    	footer[13]=""+ Double.parseDouble(new DecimalFormat("#.##").format(tolaTemplabsence));
	    	footer[14]=""+ Double.parseDouble(new DecimalFormat("#.##").format(totalTempHoliday));
	    	footer[15]=""+ Double.parseDouble(new DecimalFormat("#.##").format(totalOT));
	    	footer[0]=String.format(footerString, totoatlNoofdays);	
	        
	 
	        return footer;
	    }
	 
	    public boolean hasGroupfoot(int groupIndex) 
	    {
	        boolean retBool = false;
	         
	        if(showGroup) {
	            retBool = super.hasGroupfoot(groupIndex);
	        }
	         
	        return retBool;
	    }
}
