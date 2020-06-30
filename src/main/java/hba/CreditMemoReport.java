package hba;

import layout.MenuModel;
import model.*;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Tabbox;
import setup.users.WebusersModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class CreditMemoReport {

    private Logger logger = Logger.getLogger(this.getClass());
    HBAData data=new HBAData();


    private List<CashInvoiceSalesReportModel> invoiceSalesReport;
    private DataFilter filter=new DataFilter();
    private List<CashInvoiceSalesReportModel> allInvoiceSalesReport;

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat dcf=new DecimalFormat("0.00");

    DecimalFormat formatter = new DecimalFormat("#,###.00");

    private Date fromDate;
    private Date toDate;

    private int webUserID=0;
    private MenuModel companyRole;

    public CreditMemoReport(){
        try
        {
            Session sess = Sessions.getCurrent();
            WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
            companyRole=new MenuModel();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            fromDate=df.parse(sdf.format(c.getTime()));
            c = Calendar.getInstance();
            toDate=df.parse(sdf.format(c.getTime()));

        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditMemoReport ----> init", ex);
        }
    }

    @Command
    public void resetCreditMemoReport()
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
            logger.error("ERROR in CreditMemoReport ----> resetCreditMemoReport", ex);
        }
    }

    @Command
    @NotifyChange({"invoiceSalesReport"})
    public void searchCommand()
    {
        try
        {
            invoiceSalesReport= data.getCreditMemoeReport(fromDate,toDate,webUserID);
            allInvoiceSalesReport=invoiceSalesReport;
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditMemoReport ----> searchCommand", ex);
        }
    }


    @Command
    public void createCreditInvoice()
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
            Executions.createComponents("/hba/payments/editCreditMemo.zul", null,arg);
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditMemoReport ----> createCreditInvoice", ex);
        }
    }


    @Command
    public void editCreditInvoice(@BindingParam("row") CashInvoiceSalesReportModel row)
    {
        try
        {
            Map<String,Object> arg = new HashMap<String,Object>();
            arg.put("creditInvoiceKey", row.getRecNO());
            arg.put("type", "edit");
            arg.put("canModify",companyRole.isCanModify());
            arg.put("canView", companyRole.isCanView());
            arg.put("canPrint",companyRole.isCanPrint());
            arg.put("canCreate",companyRole.isCanAdd());
            Executions.createComponents("/hba/payments/editCreditMemo.zul", null,arg);
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditMemoReport ----> editCreditInvoice", ex);
        }
    }


    @Command
    public void viewCreditInvoice(@BindingParam("row") CashInvoiceSalesReportModel row)
    {
        try
        {
            Map<String,Object> arg = new HashMap<String,Object>();
            arg.put("creditInvoiceKey", row.getRecNO());
            arg.put("type", "view");
            arg.put("canModify",companyRole.isCanModify());
            arg.put("canView", companyRole.isCanView());
            arg.put("canPrint",companyRole.isCanPrint());
            arg.put("canCreate",companyRole.isCanAdd());
            Executions.createComponents("/hba/payments/editCreditMemo.zul", null,arg);
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditMemoReport ----> viewCreditInvoice", ex);
        }
    }

    @Command
    @NotifyChange({"invoiceSalesReport"})
    public void changeFilter()
    {
        try
        {
            invoiceSalesReport=filterData();

        }
        catch (Exception ex) {
            logger.error("error in CreditMemoReport---changeFilter-->" , ex);
        }

    }

    private List<CashInvoiceSalesReportModel> filterData()
    {
        invoiceSalesReport=allInvoiceSalesReport;
        List<CashInvoiceSalesReportModel>  lst=new ArrayList<CashInvoiceSalesReportModel>();
        if(invoiceSalesReport!=null && invoiceSalesReport.size()>0)
        {
            for (Iterator<CashInvoiceSalesReportModel> i = invoiceSalesReport.iterator(); i.hasNext();)
            {
                CashInvoiceSalesReportModel tmp=i.next();
                if(tmp.getCustomerName().toLowerCase().contains(filter.getCustomerName().toLowerCase())
                        &&tmp.getInvoiceNumber().toLowerCase().contains(filter.getInvoiceNumber().toLowerCase())&&
                        tmp.getSalesRep().toLowerCase().contains(filter.getSalesRep().toLowerCase())&&
                        tmp.getNotes().toLowerCase().contains(filter.getNote().toLowerCase())&&
//                        tmp.getUserName().toLowerCase().contains(filter.getUserName().toLowerCase())&&
                        tmp.getDepositeTo().toLowerCase().contains(filter.getDepositeTo().toLowerCase())&&
                        tmp.getInvoiceDateStr().toLowerCase().contains(filter.getInvoiceDate().toLowerCase())&&
                        (tmp.getInvoiceAmount()+"").toLowerCase().contains(filter.getInvoiceAmount().toLowerCase())&&
                        (tmp.getVatAmount()+"").toLowerCase().contains(filter.getVatAmount().toLowerCase())
                        )
                {
                    lst.add(tmp);
                }
            }
        }
        return lst;

    }

    @GlobalCommand
    @NotifyChange({"invoiceSalesReport"})
    public void refreshParentCreditInvoice(@BindingParam("type")String type)
    {
        try
        {
            invoiceSalesReport= data.getCreditMemoeReport(fromDate,toDate,webUserID);
            allInvoiceSalesReport=invoiceSalesReport;
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditInvoiceReport ----> refreshParentCreditInvoice", ex);
        }
    }


    public List<CashInvoiceSalesReportModel> getInvoiceSalesReport() {
        return invoiceSalesReport;
    }

    public void setInvoiceSalesReport(List<CashInvoiceSalesReportModel> invoiceSalesReport) {
        this.invoiceSalesReport = invoiceSalesReport;
    }

    public DataFilter getFilter() {
        return filter;
    }

    public void setFilter(DataFilter filter) {
        this.filter = filter;
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


}
