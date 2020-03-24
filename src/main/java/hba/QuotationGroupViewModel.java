package hba;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

import model.CashInvoiceModel;
import model.RecieptVoucherReportModel;
import model.TimeSheetDataModel;
import model.TimeSheetGridModel;

import org.zkoss.exporter.util.GroupsModelArrayAdapter;


public class QuotationGroupViewModel  extends GroupsModelArrayAdapter<CashInvoiceModel, String[], String[], Object>
{

	 private static final long serialVersionUID = 1L;
     
		private static final String footerString = "Total No Of Days: %d ";
		private boolean showGroup;
		
		
		public QuotationGroupViewModel(List<CashInvoiceModel> data,Comparator<CashInvoiceModel> cmpr,boolean showGroup)
		{
			 super(data.toArray(new CashInvoiceModel[0]), cmpr);
			 this.showGroup = showGroup;
		}
		
		protected String[] createGroupHead(CashInvoiceModel[] groupdata, int index, int col) 
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String[] header=new String[20];
	        int serailnumber=0;
	        if (groupdata.length > 0) 
	        {
	        	header[0]=groupdata[0].getInvoiceSaleNo();
	        	header[1]=groupdata[0].getCustomerName();
	        	header[2]=groupdata[0].getInvoiceDateStr();
	        	header[7]=""+groupdata[0].getAmount();
	        	header[8]=""+groupdata[0].getRecNo();
	        	header[9]=""+groupdata[0].getCustomerRefKey();
	        	serailnumber=serailnumber+1;
	        	header[10]=""+serailnumber;
	        }
	        return header;
	    }
	 
	    protected String[] createGroupFoot(CashInvoiceModel[] groupdata, int index, int col) 
	    {
	    	String[] footer=new String[20];
	    	footer[0]=String.format(footerString, groupdata.length);
	    	  if (groupdata.length > 0) 
		        {
	    		  footer[1]=""+groupdata[0].getAmount();
		        }
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
