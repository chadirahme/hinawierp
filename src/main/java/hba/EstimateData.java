package hba;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.CompanyDBModel;
import model.CustomerModel;
import model.EstimateModel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;
import db.DBHandler;
import db.SQLDBHandler;

public class EstimateData {

	private Logger logger = Logger.getLogger(this.getClass());
	EstimateQueries query=new EstimateQueries();
	SQLDBHandler db = new SQLDBHandler("hinawi_hba");
	public EstimateData() {
		try {
			Session sess = Sessions.getCurrent();
			DBHandler mysqldb = new DBHandler();
			ResultSet rs = null;
			CompanyDBModel obj = new CompanyDBModel();
			WebusersModel dbUser = (WebusersModel) sess
					.getAttribute("Authentication");
			if (dbUser != null) {
				HBAQueries query = new HBAQueries();
				rs = mysqldb.executeNonQuery(query.getDBCompany(dbUser
						.getCompanyid()));
				while (rs.next()) {
					obj.setCompanyId(rs.getInt("companyid"));
					obj.setDbid(rs.getInt("dbid"));
					obj.setUserip(rs.getString("userip"));
					obj.setDbname(rs.getString("dbname"));
					obj.setDbuser(rs.getString("dbuser"));
					obj.setDbpwd(rs.getString("dbpwd"));
					obj.setDbtype(rs.getString("dbtype"));
				}
				db = new SQLDBHandler(obj);
			}
		} catch (Exception ex) {
			logger.error("error in EstimateData---Init-->", ex);
		}
	}
	
	public List<EstimateModel> GetBOQEstimateMaterialsList() 
	{
		ResultSet rs = null;
		List<EstimateModel> lstEstimate = new ArrayList<EstimateModel>();
		try {
			rs = db.executeNonQuery(query.GetBOQEstimateMaterialsQuery());
			while (rs.next()) {
				EstimateModel obj=new EstimateModel();
				obj.setEstimateNo(rs.getInt("Estimateno"));
				obj.setEstimateName(rs.getString("Estimatename"));
				obj.setCustomerName(rs.getString("CustName"));
				obj.setIndustryTypeEN(rs.getString("IndustryTypeEn"));
				obj.setItemName(rs.getString("ItemName") == null ? "" : rs.getString("ItemName") );
				obj.setQuantity(rs.getDouble("Quantity"));
				obj.setRequestQuantity(0);
				lstEstimate.add(obj);							
			}
		}
		catch (Exception ex) {
			logger.error("error in EstimateData---GetBOQEstimateMaterialsList-->", ex);
		}
		return lstEstimate;
	}

}
