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
import java.text.SimpleDateFormat;
import java.util.*;

public class CreditBillReportViewModel {

    private Logger logger = Logger.getLogger(this.getClass());
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    HBAData data=new HBAData();
    BillData billdata=new BillData();
    private int webUserID=0;

    private DataFilter filter=new DataFilter();
    private Date creationdate;
    private Date fromDate;
    private Date toDate;
    private List<BillReportModel> billreport;
    private List<BillReportModel> allBillreport;
    private MenuModel companyRole;

    public CreditBillReportViewModel(){

        try
        {
            Session sess = Sessions.getCurrent();
            WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
            Calendar c = Calendar.getInstance();
            creationdate=df.parse(sdf.format(c.getTime()));//2012-03-31
            c.set(Calendar.DAY_OF_MONTH, 1);
            fromDate=df.parse(sdf.format(c.getTime()));
            c = Calendar.getInstance();
            toDate=df.parse(sdf.format(c.getTime()));
            companyRole=new MenuModel();

        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditBillReportViewModel ----> init", ex);
        }
    }



    @Command
    public void resetReport()
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
            logger.error("ERROR in CreditBillReportViewModel ----> resetReport", ex);
        }
    }

    @Command
    @NotifyChange({"billreport"})
    public void searchCommand()
    {
        try
        {
            billreport=billdata.getCreditBillReport(fromDate,toDate);
            allBillreport=billreport;
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditBillReportViewModel ----> searchCommand", ex);
        }
    }

    @Command
    @NotifyChange({"billreport"})
    public void changeFilter()
    {
        try
        {
            billreport=filterData();

        }
        catch (Exception ex) {
            logger.error("error in CreditBillReportViewModel---changeFilter-->" , ex);
        }
    }

    private List<BillReportModel> filterData()
    {
        billreport=allBillreport;
        List<BillReportModel>  lst=new ArrayList<BillReportModel>();
        if(billreport!=null && billreport.size()>0)
        {
            for (Iterator<BillReportModel> i = billreport.iterator(); i.hasNext();)
            {
                BillReportModel tmp=i.next();
                if(tmp.getBillNo().toLowerCase().contains(filter.getBillNo().toLowerCase())
                        &&tmp.getTxnDate().toLowerCase().contains(filter.getTxnDate().toLowerCase())&&
                        tmp.getDueDate().toLowerCase().contains(filter.getIrLocalNo().toLowerCase())&&
                        tmp.getVendor().toLowerCase().contains(filter.getDueDate().toLowerCase())
                        &&(tmp.getStatus()).toLowerCase().contains(filter.getStatus().toLowerCase())&&
                        (tmp.getMainMemo()).toLowerCase().contains(filter.getMainMemo().toLowerCase())&&
                        //	(tmp.getItemName()).toLowerCase().contains(filter.getItemName().toLowerCase())&&
                        //	(tmp.getDescription()).toLowerCase().contains(filter.getDescription().toLowerCase())&&
                        (tmp.getQuantity()+"").toLowerCase().contains(filter.getQuantity().toLowerCase())&&
                        tmp.getAccountName().toLowerCase().contains(filter.getAccountName().toLowerCase())&&
                        tmp.getAccountNUmber().toLowerCase().contains(filter.getAccountNUmber().toLowerCase())&&
                        //	tmp.getClassName().toLowerCase().contains(filter.getClassName().toLowerCase())&&
                        (tmp.getAmount()+"").toLowerCase().contains(filter.getAmount().toLowerCase())&&
                        (tmp.getVatAmount()+"").toLowerCase().contains(filter.getVatAmount().toLowerCase())
                        )
                {
                    lst.add(tmp);
                }
            }
        }
        return lst;
    }

    @Command
    public void createBill()
    {
        try
        {
            Map<String,Object> arg = new HashMap<String,Object>();
            arg.put("editBillKey",0);
            arg.put("type", "create");
            arg.put("CR_Flag", "R");
            arg.put("canModify",companyRole.isCanModify());
            arg.put("canView", companyRole.isCanView());
            arg.put("canPrint",companyRole.isCanPrint());
            arg.put("canCreate",companyRole.isCanAdd());
            Executions.createComponents("/hba/payments/editCreditBill.zul", null,arg);
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditBillReportViewModel ----> createBill", ex);
        }
    }


    @Command
    public void editBill(@BindingParam("row") BillReportModel row)
    {
        try
        {
            Map<String,Object> arg = new HashMap<String,Object>();
            arg.put("editBillKey", row.getMastRecNo());
            arg.put("type", "edit");
            arg.put("CR_Flag", "R");
            arg.put("canModify",companyRole.isCanModify());
            arg.put("canView", companyRole.isCanView());
            arg.put("canPrint",companyRole.isCanPrint());
            arg.put("canCreate",companyRole.isCanAdd());
            Executions.createComponents("/hba/payments/editCreditBill.zul", null,arg);
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditBillReportViewModel ----> editBill", ex);
        }
    }


    @Command
    public void viewBill(@BindingParam("row") BillReportModel row)
    {
        try
        {
            Map<String,Object> arg = new HashMap<String,Object>();
            arg.put("editBillKey", row.getMastRecNo());
            arg.put("type", "view");
            arg.put("CR_Flag", "R");
            arg.put("canModify",companyRole.isCanModify());
            arg.put("canView", companyRole.isCanView());
            arg.put("canPrint",companyRole.isCanPrint());
            arg.put("canCreate",companyRole.isCanAdd());
            Executions.createComponents("/hba/payments/editCreditBill.zul", null,arg);
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditBillReportViewModel ----> viewBill", ex);
        }
    }


    @GlobalCommand
    @NotifyChange({"billreport"})
    public void refreshParentBill(@BindingParam("type")String type)
    {
        try
        {
            billreport=billdata.getCreditBillReport(fromDate,toDate);
            allBillreport=billreport;
        }
        catch (Exception ex)
        {
            logger.error("ERROR in CreditBillReportViewModel ----> refreshParentBill", ex);
        }
    }

    public DataFilter getFilter() {
        return filter;
    }

    public void setFilter(DataFilter filter) {
        this.filter = filter;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
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

    public List<BillReportModel> getBillreport() {
        return billreport;
    }

    public void setBillreport(List<BillReportModel> billreport) {
        this.billreport = billreport;
    }
}
