package common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;

import setup.users.WebusersModel;

public class MDCUserServletFilter implements Filter
{
	//int x=0;
    public void init(FilterConfig arg0) throws ServletException
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
    	 String userName=null;
    	 String companyName=null;
    	 String host =null;
    	 String userId="0";
    			      	 
    	HttpServletRequest httprequest = (HttpServletRequest) request;
        WebusersModel dbUser=(WebusersModel)httprequest.getSession().getAttribute("Authentication");
        if(dbUser!=null)
        {
        userName = dbUser.getUsername();
        companyName=dbUser.getCompanyName();
        userId =String.valueOf(dbUser.getUserid());
         host = (String)httprequest.getRemoteAddr();
        // System.out.println("----host>>---" + host);
        }
        boolean bUserAdded = false;

        try
        {
	        if (userName != null & companyName!=null)
	        {
	                // Put the principal's name into the message diagnostic
	                // context. May be shown using %X{username} in the layout
	                // pattern.
	                MDC.put("username", userName);
	                MDC.put("userid", userId);
	                MDC.put("companyName", companyName);
	                MDC.put("userhost", host);
	                bUserAdded = true;
	        		
	        }            // Continue processing the rest of the filter chain.
	        if(chain!=null)
	        	chain.doFilter(request, response);

        }
        finally
        {
            if (bUserAdded)
            {
                // Remove the added element again - only if added.
                MDC.remove("username");
                MDC.remove("userid");
                MDC.remove("companyName");
                MDC.remove("userhost");
            }
        }
    }

    public void destroy()
    {
    }

}
