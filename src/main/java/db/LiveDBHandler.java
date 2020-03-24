package db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

public class LiveDBHandler {


	 private Logger logger = Logger.getLogger(LiveDBHandler.class);
	 private StringWriter sw = null;
	 private Connection con = null;
	 DataSource ds = null;
	 private final String dbName;
	 public LiveDBHandler(String sourceDB)
	 {
		// createPool();
		 dbName=sourceDB;
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
				//String connectionUrl= "jdbc:sqlserver://chadi-pc\\SQLEXPRESS;" +"databaseName=HRONLINE;user=sa;password=123456";
				//String connectionUrl = "jdbc:sqlserver://xdbs4.dailyrazor.com;" +"databaseName=dailyrazor_vfljjodh;user=vfljjodh;password=nUSMw%Ul";
				//String connectionUrl = "jdbc:sqlserver://hinawi2.dyndns.org;" +"databaseName=%s;user=admin;password=admin123";
				
				//connectionUrl=connectionUrl.format(connectionUrl, "dailyrazor_vfljjodh");
				
				String connectionUrl = "jdbc:sqlserver://seattle.worldispnetwork.com;" +"databaseName=%s;user=hinawi_admin;password=adminuser";
				//connectionUrl=connectionUrl.format(connectionUrl, "dailyrazor_vfljjodh");
				
				connectionUrl=connectionUrl.format(connectionUrl, dbName);
				
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		          con = DriverManager.getConnection(connectionUrl);
			}
			catch(SQLException sqlEx)
			{
				sw = new StringWriter();
				sqlEx.printStackTrace(new PrintWriter(sw));
				logger.error("error in LiveDBHandler---connect-->"+sw.toString());
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
			logger.error("error in LiveDBHandler---executeNonQuery-->"+sw.toString());
		}
		catch(Exception gEx)
		{
			sw = new StringWriter();
			gEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in LiveDBHandler---executeNonQuery-->"+sw.toString());
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
		    		logger.error("error in LiveDBHandler---executeNonQuery-->"+sw.toString());
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
			
			/*
			rs = stmt.getGeneratedKeys();
			crs.populate(rs);
		    if (crs.next()) {
		        uId = crs.getInt(1);
		    } 
			*/
			
		}
		catch(SQLException sqlEx) 
		{
			uId=-1;
			sw = new StringWriter();
			sqlEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in LiveDBHandler---executeUpdateQuery-->"+sw.toString());
		}
		catch(Exception gEx)
		{				
			sw = new StringWriter();
			gEx.printStackTrace(new PrintWriter(sw));
			logger.error("error in LiveDBHandler---executeUpdateQuery-->"+sw.toString());
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
		    		logger.error("error in LiveDBHandler---executeUpdateQuery-->"+sw.toString());
		    	}
		    }
		    return uId;	
		}
	}
}

