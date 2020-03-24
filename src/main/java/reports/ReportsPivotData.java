package reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import model.AccountsModel;
import org.apache.log4j.Logger;

public class ReportsPivotData
{
	private Logger logger = Logger.getLogger(this.getClass());
	//ChartOFAccountData chartOfAccountData=new ChartOFAccountData();
	private List<AccountsModel> lstAccounts =new ArrayList<AccountsModel>();
	
	private void fillAccountsData()
	{
		try
		{
			//lstAccounts=chartOfAccountData.fillChartofAccounts("",true);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReportsPivotData ----> fillAccountsData", ex);			
		}
	}
	
	public List<List<Object>> getAccountsPivotData()
	{
		List<List<Object>> list = new ArrayList<List<Object>>();
		try
		{
		//lstAccounts=chartOfAccountData.fillChartofAccounts("Y",true);
		
		List<Object[]> tempList = new ArrayList<Object[]>();
		for (AccountsModel item : lstAccounts) 
		{
			Object[] tempArr = new Object[3];
			tempArr[0] = item.getAccountName();
			tempArr[1]=item.getAccountType();
			tempArr[2]=item.getTotalBalance();			
			tempList.add(tempArr);
		} 
		
		for (int i = 0; i < tempList.size(); i++) {

			list.add(Arrays.asList(tempList.get(i)));
		}
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in ReportsPivotData ----> getAccountsPivotData", ex);			
		}
		return list;
	}
	
	public List<String> getAccountsColumns() 
	{
        return Arrays.asList(new String[] { "Name", "Type", "Balance"});
    }
}
