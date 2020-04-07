package common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDateText 
{

	public static String formatDate(Date value) 
	{
		String res = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		
		res=df.format(value);		
		return res;
	}

	public static boolean notEmpty(String str) {

		if (str != null && str.length()>0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.length()==0) {
			return true;
		} else {
			return false;
		}
	}
}
