package reports;

import org.apache.log4j.Logger;
import org.zkoss.pivot.PivotField;
import org.zkoss.pivot.impl.TabularPivotModel;

public class AccountsPivotViewModel
{
	private Logger logger = Logger.getLogger(this.getClass());
	private TabularPivotModel pivotModel;
	ReportsPivotData data=new ReportsPivotData();
	private AccountsPivotRender render;
	public AccountsPivotViewModel()
	{
		try
		{
			render=new AccountsPivotRender();
			pivotModel = new TabularPivotModel(data.getAccountsPivotData(),data.getAccountsColumns());		
			pivotModel.setFieldType("Type", PivotField.Type.ROW);
			pivotModel.setFieldType("Name", PivotField.Type.ROW);			
			pivotModel.setFieldType("Balance", PivotField.Type.DATA);
				
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in AccountsPivotViewModel ----> init", ex);			
		}
	}

	public TabularPivotModel getPivotModel() {
		return pivotModel;
	}

	public void setPivotModel(TabularPivotModel pivotModel) {
		this.pivotModel = pivotModel;
	}

	public AccountsPivotRender getRender() {
		return render;
	}

	public void setRender(AccountsPivotRender render) {
		this.render = render;
	}
}
