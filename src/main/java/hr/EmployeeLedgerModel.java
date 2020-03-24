package hr;

import hr.model.ActivityModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.GroupsModelArray;

public class EmployeeLedgerModel extends GroupsModelArray<ActivityModel, String[], String[], Object>
{
	 private static final long serialVersionUID = 1L;
     
	private static final String footerString = "Total Transactions: %d ";
	private boolean showGroup;
	
	
	public EmployeeLedgerModel(List<ActivityModel> data,Comparator<ActivityModel> cmpr,boolean showGroup)
	{
		 super(data.toArray(new ActivityModel[0]), cmpr);
		 this.showGroup = showGroup;
	}
	
	protected String[] createGroupHead(ActivityModel[] groupdata, int index, int col) 
	{
        String ret = "";
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] header=new String[7];
        if (groupdata.length > 0) 
        {
        	//header[0] ="Emp. NO : " +groupdata[0].getEmpNo() + " Employee Name : "  + groupdata[0].getEmployeeName() + " Status :" + groupdata[0].getEmployeeStatus();
        	header[0]=groupdata[0].getEmpNo();
        	header[1]=groupdata[0].getEmployeeName();
        	header[2]=groupdata[0].getEmployeeStatus();
        	header[3]=groupdata[0].getDepartment();
        	header[4]=groupdata[0].getPosition();
        	header[5]=groupdata[0].getNationality();
        	if(groupdata[0].getEmployeementDate()!=null)
        	header[6]=df.format(groupdata[0].getEmployeementDate());        	
        }
 
        return header;
    }
 
    protected String[] createGroupFoot(ActivityModel[] groupdata, int index, int col) 
    {
    	String[] footer=new String[2];
    	footer[0]=String.format(footerString, groupdata.length);
    	double total=0;
    	for (ActivityModel item : groupdata)
    	{
		total+=item.getAmount();	
		}
    	 DecimalFormat df = new DecimalFormat("###,###,##0.00");
    	 
    	footer[1]=df.format(groupdata[groupdata.length-1].getBalance());//String.valueOf(groupdata[groupdata.length-1].getBalance());
        return footer;
    }
 
    public boolean hasGroupfoot(int groupIndex) {
        boolean retBool = false;
         
        if(showGroup) {
            retBool = super.hasGroupfoot(groupIndex);
        }
         
        return retBool;
    }
}
