package realestate;

import hba.HBAQueries;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.*;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import setup.users.WebusersModel;

import db.DBHandler;
import db.SQLDBHandler;

public class RealEstateData 
{
	private Logger logger = Logger.getLogger(RealEstateData.class);
	SQLDBHandler db=new SQLDBHandler("hinawi_hba");
	
	public RealEstateData()
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
			logger.error("error in RealEstateData---Init-->" , ex);
		}
	}
	public List<RealEstateModel> getRealEstateUnitList()
	{
		List<RealEstateModel> lst=new ArrayList<RealEstateModel>();
		//SQLDBHandler db=new SQLDBHandler("HBAONLINE");		
		RealEstateQueries query=new RealEstateQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getRealEstateUnitListQuery());
			while(rs.next())
			{
				RealEstateModel obj=new RealEstateModel();
				obj.setFlatName(rs.getString("FlatName")==null?"":rs.getString("FlatName"));
				obj.setBuildingName(rs.getString("BuildingName")==null?"":rs.getString("BuildingName"));
				obj.setStatus(rs.getString("Status")==null?"":rs.getString("Status"));
				obj.setRent(rs.getString("Rent")==null?"":rs.getString("Rent"));
				obj.setFlatDesc(rs.getString("FlatDesc"));
				obj.setDeposit(rs.getString("Deposit")==null?"":rs.getString("Deposit"));
				obj.setFlatNo(rs.getString("FlatNo"));
				obj.setFlatSize(rs.getString("Size"));
				obj.setNoOfRoom(rs.getString("NoofRooms"));
				obj.setNoOfBalcony(rs.getString("NoofBalcony"));
				obj.setNoOfBathRoom(rs.getString("NoofBathRooms"));
				obj.setFlatTypeDesc(rs.getString("FlatTypeDesc"));
				obj.setSizeTypeDesc(rs.getString("SizeTypeDesc"));
				obj.setPurposeDesc(rs.getString("PurposeDesc"));
				obj.setFloorDesc(rs.getString("FloorDesc"));
				
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in RealEstateData---getRealEstateUnitList-->" , ex);
		}
		return lst;
	}
	
	public List<RealEstateModel> getVacantList()
	{
		List<RealEstateModel> lst=new ArrayList<RealEstateModel>();
		
		RealEstateQueries query=new RealEstateQueries();
		ResultSet rs = null;
		try 
		{
			rs=db.executeNonQuery(query.getVacantListQuery());
			while(rs.next())
			{
				RealEstateModel obj=new RealEstateModel();
				obj.setFlatName(rs.getString("CFlatNo")==null?"":rs.getString("CFlatNo"));
				obj.setBuildingName(rs.getString("CFullName")==null?"":rs.getString("CFullName"));
				obj.setStatus(rs.getString("CStatus")==null?"":rs.getString("CStatus"));				
				obj.setFlatDesc(rs.getString("FlatDesc")==null?"":rs.getString("FlatDesc"));			
				obj.setFlatTypeDesc(rs.getString("FlatTypeDesc")==null?"":rs.getString("FlatTypeDesc"));
				obj.setSizeTypeDesc(rs.getString("SizeTypeDesc"));
				obj.setPurposeDesc(rs.getString("PurposeDesc"));
				obj.setFloorDesc(rs.getString("FloorDesc"));				
				lst.add(obj);
			}
			
		}
		catch (Exception ex) {
			logger.error("error in RealEstateData---getVacantList-->" , ex);
		}
		return lst;
	}
}
