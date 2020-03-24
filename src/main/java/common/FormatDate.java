package common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate 
{
	public static String formatDate(Date value) 
	{
		String res = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		
		res=df.format(value);		
		return res;
	}
}
