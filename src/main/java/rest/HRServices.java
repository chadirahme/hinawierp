package rest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import restdata.HRServiceData;
import model.EmployeeModel;
import model.EmployeeProfile;
import model.TimeSheetDataModel;


@Path("/HRServices") 
public class HRServices {

	@Context private HttpServletRequest servletRequest;
		
	   @GET 
	   @Path("/users") 
	   @Produces(MediaType.APPLICATION_JSON) 
	   public String getUsers(){ 
	      return "chadiiiii"; 
	   }  
	   
	   @GET
	    @Path("ping")
	    public Response getServerTime() throws Exception {		 
		  
		   
	       System.out.println("RESTful Service 'MessageService' is running ==> ping");		
	       try
	       {
	    	  HttpSession session = servletRequest.getSession(true);
	    	  String name=(String) session.getAttribute("CompanyName");
	    	  System.out.println("RESTful Service 'MessageService' is running ==> " + name);		
	    	// HRServiceData data=new HRServiceData();
	        //return "received ping on "+new Date().toString();
	        return Response.ok("String in JSON format").build();
	       }
	       catch(Exception ex)
	       {
	    	   //return Response.seeOther(another URI here).build();
	    	// And if you just want to return a status code, something like...
               return Response.status(Response.Status.UNAUTHORIZED).build();
	       }
	    }
	   
	   
	   @GET
	   @Path("getEmployeeProfile")
	   @Produces(MediaType.APPLICATION_JSON) 
	    public Response getEmployeeProfile() throws Exception 
	   {
		   try
		   {
		    HRServiceData data=new HRServiceData(servletRequest);
	        System.out.println(" 'HRServices' is running ==> getEmployeeProfile");
	        //employeeKey take from dbUser session
	        
	        EmployeeProfile profile=new EmployeeProfile();
	        EmployeeModel emp=data.getEmployeeProfile(0);
	        
	        profile.setEmployeeNo(emp.getEmployeeNo());
	        profile.setEmployeeKey(emp.getEmployeeKey());
	        profile.setFullName(emp.getFullName());
	        profile.setDateOfBirth(emp.getDateOfBirth());
	        profile.setCompanyName(emp.getCompanyName());
	        
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(emp.getDateOfBirth());
	        emp.setDateOfBirth(cal.getTime());
	        cal.clear();
	        if(emp.getJoiningDate()!=null)
	        cal.setTime(emp.getJoiningDate());
	        
	        emp.setJoiningDate(cal.getTime());
	        
	        cal.clear();
	        if(emp.getEmployeementDate()!=null)
	        cal.setTime(emp.getEmployeementDate());
	        emp.setEmployeementDate(cal.getTime());
	        	        	       
	        
	        //ObjectMapper mapper = new ObjectMapper();
	       // Calendar c = Calendar.getInstance();
	        //c.clear();
	       // c.set(emp.getDateOfBirth().getYear() ,emp.getDateOfBirth().getMonth(),emp.getDateOfBirth().getDay());
	        //profile.setDateOfBirth(c.getTime());
	        
	       
	        return Response.ok(emp).build();
	        //return data.getEmployeeProfile(0);
		   }
		   catch(Exception ex)
	       {
	    	   //return Response.seeOther(another URI here).build();
	    	// And if you just want to return a status code, something like...
               return Response.status(Response.Status.UNAUTHORIZED).build();
	       }
		   
	    }
	
	   
	   @GET
	   @Path("getTomorrowPlanTimeSheet")
	   @Produces(MediaType.APPLICATION_JSON) 
	    public Response getTomorrowPlanTimeSheet() throws Exception 
	   {
		   try
		   {
		    HRServiceData data=new HRServiceData(servletRequest);
	        System.out.println(" 'HRServices' is running ==> getTomorrowPlanTimeSheet");
	        //employeeKey take from dbUser session
	        	       	        
	        return Response.ok(data.getTomorrowPlanTimeSheet(0)).build();
	        //return data.getEmployeeProfile(0);
		   }
		   catch(Exception ex)
	       {
	    	   //return Response.seeOther(another URI here).build();
	    	// And if you just want to return a status code, something like...
               return Response.status(Response.Status.UNAUTHORIZED).build();
	       }
		   
	    }
	   
	   
	   @GET
	   @Path("getLeaveRequest")
	   @Produces(MediaType.APPLICATION_JSON) 
	    public Response getLeaveRequest() throws Exception 
	   {
		   try
		   {
		    HRServiceData data=new HRServiceData(servletRequest);
	        System.out.println(" 'HRServices' is running ==> getLeaveRequest");
	        //employeeKey take from dbUser session
	        	       	        
	        return Response.ok(data.getLeaveRequest(0)).build();
	        //return data.getEmployeeProfile(0);
		   }
		   catch(Exception ex)
	       {
	    	   //return Response.seeOther(another URI here).build();
	    	// And if you just want to return a status code, something like...
               return Response.status(Response.Status.UNAUTHORIZED).build();
	       }
		   
	    }
	   
	   @GET
	   @Path("getUserTaskList")
	   @Produces(MediaType.APPLICATION_JSON) 
	    public Response getUserTaskList() throws Exception 
	   {
		   try
		   {
		    HRServiceData data=new HRServiceData(servletRequest);
	        System.out.println(" 'HRServices' is running ==> getLeaveRequest");
	        //employeeKey take from dbUser session
	        	       	        
	        return Response.ok(data.getUserTaskList()).build();
	        //return data.getEmployeeProfile(0);
		   }
		   catch(Exception ex)
	       {
	    	   //return Response.seeOther(another URI here).build();
	    	// And if you just want to return a status code, something like...
               return Response.status(Response.Status.UNAUTHORIZED).build();
	       }
		   
	    }
}
