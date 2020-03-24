package timesheet;

import java.util.Comparator;
import java.util.List;



import model.TimeSheetGridModel;

import org.zkoss.zul.GroupsModelArray;

public class TimeSheetGroupModel  extends GroupsModelArray<TimeSheetGridModel, String[], String[], Object>
{
	 private static final long serialVersionUID = 1L;
     
		private static final String footerString = "Total Transactions: %d ";
		private boolean showGroup;
		
		
		public TimeSheetGroupModel(List<TimeSheetGridModel> data,Comparator<TimeSheetGridModel> cmpr,boolean showGroup)
		{
			 super(data.toArray(new TimeSheetGridModel[0]), cmpr);
			 this.showGroup = showGroup;
		}
		
		protected String[] createGroupHead(TimeSheetGridModel[] groupdata, int index, int col) 
		{
	       
	        String[] header=new String[7];
	        if (groupdata.length > 0) 
	        {
	        	//header[0] ="Emp. NO : " +groupdata[0].getEmpNo() + " Employee Name : "  + groupdata[0].getEmployeeName() + " Status :" + groupdata[0].getEmployeeStatus();
	        	header[0]=groupdata[0].getEmpNo();
	        	header[1]=groupdata[0].getEnFullName();
	        	header[2]=groupdata[0].getStatus();
	        	header[3]=String.valueOf(groupdata[0].getSrNo());
	        	header[4]=groupdata[0].getSalaryStatus();
	        }
	 
	        return header;
	    }
	 
	    protected String[] createGroupFoot(TimeSheetGridModel[] groupdata, int index, int col) 
	    {
	    	String[] footer=new String[2];
	    	footer[0]=String.format(footerString, groupdata.length);	    	
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
