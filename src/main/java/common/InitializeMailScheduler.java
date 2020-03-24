package common;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.zkoss.zul.Messagebox;

@SuppressWarnings("serial")
public class InitializeMailScheduler extends HttpServlet  {

	private Logger logger = Logger.getLogger(this.getClass());
	int i=0;
	 public void init() throws ServletException {
		    
		    try {
		        System.out.println("Initializing Mail Scheduler in Hinawi");
		        
//		        MailScheduler objPlugin = new MailScheduler();
		        
		        logger.info("--------------------------Initializing Mail Scheduler in Hinawi---------------------------------------------");
		        
		    }
		    catch (Exception ex) {
		    	
		    	  StringWriter sw = new StringWriter();
				  ex.printStackTrace(new PrintWriter(sw)); 
				  Messagebox.show(sw.toString());
		    	 logger.error(""+sw.toString()+"InitializeMailScheduler-->execute ");
		    }
		    
		  }

}
