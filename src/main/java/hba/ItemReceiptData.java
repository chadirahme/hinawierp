package hba;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.CompanyDBModel;
import model.QbListsModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class ItemReceiptData {
	
	private Logger logger = Logger.getLogger(HBAData.class);
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	//DateFormat tf = new SimpleDateFormat("dd/MM/yyyy HH:mm");	
	//SimpleDateFormat tsdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	DecimalFormat dcf=new DecimalFormat("0.00");
	
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	
	
	public ItemReceiptData()
	{
		try
		{
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb=new DBHandler();
			ResultSet rs=null;
			CompanyDBModel obj=new CompanyDBModel();
			WebusersModel dbUser=(WebusersModel)sess.getAttribute("Authentication");
			if(dbUser!=null)
			{
				HBAQueries query=new HBAQueries();
				rs=mysqldb.executeNonQuery(query.getDBCompany(dbUser.getCompanyid()));
				 while(rs.next())
				 {						
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));						
				 }
				  db=new SQLDBHandler(obj);
			}
		}
		catch (Exception ex) 
		{
			logger.error("error in ItemReceiptData---Init-->" , ex);
		}
	}
	
	
	
	public List<QbListsModel> getVendor()
	{
		
		List<QbListsModel> lst=new ArrayList<QbListsModel>();
		QbListsModel obj=new QbListsModel();
		lst.add(obj);
		return lst;
		
	}
	

}
