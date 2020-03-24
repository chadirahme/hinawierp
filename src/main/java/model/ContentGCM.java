package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContentGCM  implements Serializable
{
	 private List<String> registration_ids;
	 private Map<String,String> data;
	 private String employeeName;
	 private String position;
	 private String department;
	 private String descreption;
	 private String tomorrowPlan;
	 

	    public void addRegId(String regId){
	        if(registration_ids == null)
	            registration_ids = new LinkedList<String>();
	        registration_ids.add(regId);
	    }

	    public void createData(String title, String message)
	    {
	        if(data == null)
	            data = new HashMap<String,String>();

	        data.put("title", title);
	        data.put("message", message);
	        data.put("id", "6767");
	        data.put("employeeName", employeeName);
	        data.put("position", position);
	        data.put("department", department);
	        data.put("descreption", descreption);
	        data.put("tomorrowPlan", tomorrowPlan);
	    }

		public String getEmployeeName() {
			return employeeName;
		}

		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getDescreption() {
			return descreption;
		}

		public void setDescreption(String descreption) {
			this.descreption = descreption;
		}

		public String getTomorrowPlan() {
			return tomorrowPlan;
		}

		public void setTomorrowPlan(String tomorrowPlan) {
			this.tomorrowPlan = tomorrowPlan;
		}
}
