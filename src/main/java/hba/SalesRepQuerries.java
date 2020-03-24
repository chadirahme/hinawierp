package hba;

import java.text.SimpleDateFormat;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import model.BanksModel;
import model.SalesRepModel;

public class SalesRepQuerries {
	
	StringBuffer query;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	WebusersModel dbUser=null;
	
	
	public SalesRepQuerries()
	{
		Session sess = Sessions.getCurrent();
		dbUser=(WebusersModel)sess.getAttribute("Authentication");
	}
	
	public String fillSalesRepList(String status)
	{
		query=new StringBuffer();		
		query.append("SELECT SalesRep.*,QBLists.FullName AS SalesRep,QBLists.ListType as SalesRepType  from SalesRep  Left Join QBLists On SalesRep.QBListKey= QBLists.RecNo ");
		if(status.equalsIgnoreCase(""))
		{
		query.append("");
		}
		else
		{
		query.append("where SalesRep.IsActive='"+status+"'");
		}
		return query.toString();		
	}
	
	public String getsalesRepListByID(int salesrepKey)
	{
		  query=new StringBuffer();
		  query.append("SELECT SalesRep.*,QBLists.FullName AS SalesRep,QBLists.ListType as SalesRepType  from SalesRep  Left Join QBLists On SalesRep.QBListKey= QBLists.RecNo  where SalesRep.salesrep_key="+salesrepKey+"");		
		  return query.toString();
	}
	
	public String getDefaultSetUpInfoForSalesRep()
	{
		query = new StringBuffer();
		query.append("Select maxcommission,usesalesrepcommission ");
		  if(dbUser.getMergedDatabse().equalsIgnoreCase("Yes"))
		  {
			  query.append(" From companySettings");
		  }
		  else
		  {
			  query.append(" From COMPSETUP");
		  }
		
		return query.toString();
	}
	
	public String updateSalesRepList(SalesRepModel obj)
	{
		query=new StringBuffer();		
		query.append("update SalesRep set initial='"+obj.getIntials()+"',isActive='"+obj.getIsActive() +"',qblistkey='"+obj.getQbListKey()+"',cmnflag='"+obj.getCommissionFlag()+"',cmnpaidby='"+obj.getCommissionPaidBy()+"',cmnpercentage="+obj.getCommissionPercent()+",cmnused='"+obj.getCommissionUsed()+"' Where  salesrep_key='" + obj.getSlaesRepKey()+"'");
		return query.toString();		
	}
	
	public String getMaxSalesrepId()
	{
		  query=new StringBuffer();
		  query.append("SELECT max(salesRep_key) as MaxRecNo from SalesRep");		
		  return query.toString();
	}
	
	public String inserSalesRepQuerry(SalesRepModel obj,int recNo)
	{
		  query=new StringBuffer();
		  query.append("insert into SalesRep (salesrep_key,initial,isActive,qblistkey,cmnflag,cmnpaidby,cmnpercentage,cmnused)values(");
		  query.append(recNo +",'" + obj.getIntials() +"' , '" + obj.getIsActive() + "', " + obj.getQbListKey() + ", '" + obj.getCommissionFlag() + "','"+obj.getCommissionPaidBy()+"','"+obj.getCommissionPercent()+"','"+obj.getCommissionUsed()+"')");
		  return query.toString();
	}
	
	public String fillSalesNameDropDown()
	{
		query=new StringBuffer();
		 query.append("Select Name,RecNo,ListType,SubLevel,ListID,FullName from QbLists Where ListType in('Employee','OtherNames','Vendor','SalesRepType') AND IsActive in ('Y','N') order by ListType,FullName");
		 return query.toString();		
	}

}
