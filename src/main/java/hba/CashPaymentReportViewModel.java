package hba;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import layout.MenuData;
import layout.MenuModel;
import model.CashInvoiceSalesReportModel;
import model.CashModel;
import model.DataFilter;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

public class CashPaymentReportViewModel 
{
	private Logger logger = Logger.getLogger(this.getClass());
	HBAData data=new HBAData();
	private MenuModel companyRole;
	private boolean posItems;
	private Date fromDate;
	private Date toDate;
	private boolean adminUser;
	private int webUserID=0;
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat dcf=new DecimalFormat("0.00");

	DecimalFormat formatter = new DecimalFormat("#,###.00");
	private List<CashModel> lstCashPayment;
	private List<CashModel> lstAllCashPayment;
	private DataFilter filter=new DataFilter();
	List<MenuModel> list;
	MenuData menuData=new MenuData();
	
	public CashPaymentReportViewModel()
	{
		try
		{
			Session sess = Sessions.getCurrent();		
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				adminUser=dbUser.getFirstname().equals("admin");

				if(adminUser)
				{
					webUserID=0;
				}
				else
				{
					webUserID=dbUser.getUserid();
				}
			}
			Calendar c = Calendar.getInstance();			
			c.set(Calendar.DAY_OF_MONTH, 1);
			fromDate=df.parse(sdf.format(c.getTime()));
			c = Calendar.getInstance();	
			toDate=df.parse(sdf.format(c.getTime()));
			posItems=true;
			
			list=menuData.getMenuList(dbUser.getUserid(),false);
			getCompanyRolePermessions(dbUser.getCompanyroleid(),4);
			
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashPaymentReportViewModel ----> init", ex);			
		}
	}
	
	private void getCompanyRolePermessions(int companyRoleId,int parentId)
	{
		companyRole=new MenuModel();

		List<MenuModel> lstRoles= data.getMenuRoles(companyRoleId,parentId);
		for (MenuModel item : lstRoles) 
		{
			if(item.getMenuid()==52)
			{
				companyRole=item;
				break;
			}
		}
	}
	
	@Command
	@NotifyChange({"lstCashPayment"})
	public void searchCommand()
	{
		try
		{			
			lstCashPayment= data.getCashPaymentReport(fromDate,toDate,webUserID);
			lstAllCashPayment=lstCashPayment;
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashPaymentReportViewModel ----> searchCommand", ex);			
		}
	}
	
	@Command
	public void createCashPayment()
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("creditInvoiceKey",0);
			arg.put("type", "create");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			//arg.put("objPrint", objPrint);
			arg.put("posItems", posItems);
			Executions.createComponents("/hba/payments/cashpayment.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashPaymentReportViewModel ----> createCashPayment", ex);			
		}
	}
	
	@Command
	public void editCashPayment(@BindingParam("row") CashModel row)
	{
		try
		{
			Map<String,Object> arg = new HashMap<String,Object>();
			arg.put("cashPyamentKey",(int)row.getRecNo());
			arg.put("type", "edit");
			arg.put("canModify",companyRole.isCanModify());
			arg.put("canView", companyRole.isCanView());
			arg.put("canPrint",companyRole.isCanPrint());
			arg.put("canCreate",companyRole.isCanAdd());
			//arg.put("objPrint", objPrint);
			arg.put("posItems", posItems);
			Executions.createComponents("/hba/payments/cashpayment.zul", null,arg);
		}
		catch (Exception ex)
		{	
			logger.error("ERROR in CashPaymentReportViewModel ----> createCashPayment", ex);			
		}
	}
	
	
	@Command
	@NotifyChange({"lstCashPayment"})
	public void changeFilter() 
	{
		try
		{
			lstCashPayment=filterData();			

		}
		catch (Exception ex) {
			logger.error("error in CashPaymentReportViewModel---changeFilter-->" , ex);
		}

	}
	private List<CashModel> filterData()
	{
		lstCashPayment=lstAllCashPayment;
		List<CashModel>  lst=new ArrayList<CashModel>();
		if(lstCashPayment!=null && lstCashPayment.size()>0)
		{
			for (Iterator<CashModel> i = lstCashPayment.iterator(); i.hasNext();)
			{
				CashModel tmp=i.next();				
				if(tmp.getPrintName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
						&&tmp.getPvNo().toLowerCase().contains(filter.getInvoiceNumber().toLowerCase())&&
						tmp.getStatus().toLowerCase().contains(filter.getStatus().toLowerCase())&&
						tmp.getPvDateStr().toLowerCase().contains(filter.getInvoiceDate().toLowerCase())&&
						tmp.getPayeeType().toLowerCase().contains(filter.getPaymentType().toLowerCase())&&
						(tmp.getAmount()+"").toLowerCase().contains(filter.getInvoiceAmount().toLowerCase())
						)
				{
					lst.add(tmp);
				}
			}
		}
		return lst;

	}

	public MenuModel getCompanyRole() {
		return companyRole;
	}

	public void setCompanyRole(MenuModel companyRole) {
		this.companyRole = companyRole;
	}

	public boolean isPosItems() {
		return posItems;
	}

	public void setPosItems(boolean posItems) {
		this.posItems = posItems;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<CashModel> getLstCashPayment() {
		return lstCashPayment;
	}

	public void setLstCashPayment(List<CashModel> lstCashPayment) {
		this.lstCashPayment = lstCashPayment;
	}

	public DataFilter getFilter() {
		return filter;
	}

	public void setFilter(DataFilter filter) {
		this.filter = filter;
	}
}
