package db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import model.CompanyDBModel;

import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

public class SQLDBHandler 
{

	 private Logger logger = Logger.getLogger(SQLDBHandler.class);
	 private StringWriter sw = null;
	 private Connection con = null;
	 DataSource ds = null;
	 private final String dbhost;
	 private final String dbName;
	 private final String dbUser;
	 private final String dbPwd;
	 
	 public SQLDBHandler(String sourceDB)
	 {
		// createPool();
		 dbName=sourceDB;
		 
		 dbhost="";
		 dbUser="";
		 dbPwd="";
	 }
	 public SQLDBHandler(CompanyDBModel objDB)
	 {
		// createPool();
		 dbName=objDB.getDbname();
		 dbhost=objDB.getUserip();
		 dbUser=objDB.getDbuser();
		 dbPwd=objDB.getDbpwd();
	 }
	 
	 public void createPool()
	 {
		 try{
				Context ctx = new InitialContext();
				Context envCtx = (Context) ctx.lookup("java:comp/env");
				ds = (DataSource)envCtx.lookup("HBAConnectionPool");
			}
			catch(Exception e) {
				sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error("| Services Monitor | [DBManager.connect(1)]  ClassNotFoundException:  "+sw.toString());
			}
	 }
	 public void connect() {		
			try{			
				//if (ds != null) 
				//{
			    //    con = ds.getConnection();
				//}
				//String connectionUrl= "jdbc:sqlserver://chadi-pc\\SQLEXPRESS;" +"databaseName=%s;user=sa;password=123456";
				//String connectionUrl1 = "jdbc:sqlserver://hinawi2.dyndns.org;" +"databaseName=%s;user=admin;password=admin123";
				
				//String connectionUrl = "jdbc:sqlserver://seattle.worldispnetwork.com;" +"databaseName=%s;user=hinawi_admin;password=adminuser";
				String connectionUrl="";
				if(dbhost.equals(""))
				{
				 //connectionUrl = "jdbc:sqlserver://seattle.worldispnetwork.com;" +"databaseName=%s;user=hinawi_hruser;password=userone";	
				 connectionUrl = "jdbc:sqlserver://seattle.worldispnetwork.com;" +"databaseName=%s;user=hinawi_hruser;password=host123";	
				//connectionUrl=connectionUrl.format(connectionUrl, "hinawi_hr");				
				connectionUrl=connectionUrl.format(connectionUrl, dbName);
				}
				else
				{
					connectionUrl = "jdbc:sqlserver://%s;" +"databaseName=%s;user=%s;password=%s";
					connectionUrl=connectionUrl.format(connectionUrl,dbhost, dbName,dbUser,dbPwd);					
				}
				//logger.info(connectionUrl);
				
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		          con = DriverManager.getConnection(connectionUrl);
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in DBHandler---connect-->"+sw.toString());
			}
			catch(Exception e) {
				sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error("| Services Monitor | [DBManager.connect(1)]  ClassNotFoundException:  "+sw.toString());
			}
		}
	public CachedRowSetImpl executeNonQuery(String query)
	{
	   logger.info("Executing this query-->"+query);
		CachedRowSetImpl crs = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			connect();
			crs = new CachedRowSetImpl();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			crs.populate(rs);
		}
		catch(SQLException sqlEx)
		{
			sw = new StringWriter();
			sqlEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
		}
		catch(Exception gEx)
		{
			sw = new StringWriter();
			gEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
		}
		finally
		{
		    if(con != null)
		    {
		    	try
		    	{
		    		rs.close();
		    		stmt.close();
		    		con.close();
		    	}
		    	catch(final Exception ex)
		    	{
		    		sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
		    		logger.error("error in DBHandler---executeNonQuery-->"+sw.toString());
		    	}
		    }
		    return crs;	
		}
	}
	
	public int executeUpdateQuery(String query)
	{
	   logger.info("Executing this query-->"+query);
		CachedRowSetImpl crs = null;
		ResultSet rs;
		Statement stmt = null;
		int uId=0;
		try
		{
			connect();
			crs = new CachedRowSetImpl();
			stmt = con.createStatement();
			uId=stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			
			/*if(query.contains("insert"))
			{
				
			rs = stmt.getGeneratedKeys();
			crs.populate(rs);
		    if (crs.next()) {
		        uId = crs.getInt(1);
		    }
		    logger.info("uId1>> " + uId);
			}*/
			logger.info("uId2>> " + uId);
			
		}
		catch(SQLException sqlEx) 
		{
			uId=-1;
			sw = new StringWriter();
			sqlEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
		}
		catch(Exception gEx)
		{				
			sw = new StringWriter();
			gEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
		}
		finally
		{
			if(con != null)
		    {
		    	try
		    	{
		    		con.close();
		    	}
		    	catch(final Exception ex)
		    	{
					sw = new StringWriter();
					ex.printStackTrace(new PrintWriter(sw));
		    		logger.error("error in DBHandler---executeUpdateQuery-->"+sw.toString());
		    	}
		    }
		    return uId;	
		}
	}
	
	
	// for prepared Statement  
	public Connection getConnection()
	{
		connect();
		return con;	
		
	}
	
	
	public boolean callProcedure(String proName,String[]parameters)
	   {
		   
		   boolean executedSuccesfully=false;
		   try
			{
				connect();
				CallableStatement cs;
				StringBuffer parmlist=new StringBuffer("(");
		        for(int i=0;i<parameters.length;i++)
		        {
		        	parmlist.append("?");
		        	parmlist.append(",");
		        }
		        parmlist.deleteCharAt(parmlist.length()-1);
		        parmlist.append(")");
				
		        logger.info("calling "+proName);
		        if(parameters!=null&&parameters.length>0)
		        {
		        	 cs = con.prepareCall("{call "+proName+parmlist.toString()+"}");
		        	for(int i=0;i<parameters.length;i++)
		        	{
		        		cs.setString((i+1), parameters[i]);
		        		//System.out.println( parameters[i]);
		        	}
		        }
		        else
		        {
		        	  cs = con.prepareCall("{call "+proName+"}");
		        }
		        
		        executedSuccesfully=cs.execute();		       		        
			}
		   
		   catch(SQLException sqlEx)
			{
			   sw = new StringWriter();
			   sqlEx.printStackTrace(new PrintWriter(sw));
			   logger.error("callProcedure SQL Exception "+sw.toString());
			}
			catch(Exception gEx)
			{
				sw = new StringWriter();
				gEx.printStackTrace(new PrintWriter(sw));
			logger.error("callProcedure Exception"+sw.toString());
			}
			finally
			{
			    if(con != null)
			    {
			    	try
			    	{
			    		con.close();
			    	}
			    	catch(final Exception ex)
			    	{
			    		sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
			    		logger.error("callProcedure "+sw.toString());
			    	}
			    }
			    return executedSuccesfully;
			}
		   
		   
	   }
	
	
	
}
