package timesheet;

import java.util.Comparator;
import java.util.List;

import model.EmployeeShiftModel;

import org.zkoss.exporter.util.GroupsModelArrayAdapter;

public class EmployeeShiftGroupAdapter extends GroupsModelArrayAdapter<EmployeeShiftModel, String[], String[], Object>
{
	private static final long serialVersionUID = 1L;
    
	private static final String footerString = "Total Transactions: %d ";
	private boolean showGroup;
	
	
	public EmployeeShiftGroupAdapter(List<EmployeeShiftModel> data,Comparator<EmployeeShiftModel> cmpr,boolean showGroup)
	{
		 super(data.toArray(new EmployeeShiftModel[0]), cmpr);
		 this.showGroup = showGroup;
	}
	
	protected String[] createGroupHead(EmployeeShiftModel[] groupdata, int index, int col) 
	{		
        String[] header=new String[8];
        if (groupdata.length > 0) 
        {        
        	header[0]=groupdata[0].getEmployeeNo();
        	header[1]=groupdata[0].getEnglishName();
        	header[2]=groupdata[0].getPosition();        	
        }
 
        return header;
    }
 
    protected String[] createGroupFoot(EmployeeShiftModel[] groupdata, int index, int col) 
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


