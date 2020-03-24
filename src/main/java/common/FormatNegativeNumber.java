package common;


import java.text.DecimalFormat;

public class FormatNegativeNumber 
{
	public static String formatNegativeNumber(double value) 
	{
		String res = "";
		DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
		if(value>=0)
		res=formatter.format(value);
		else
		{
			value=Math.abs(value);
			res="(" + formatter.format(value) + ")";
		}
		return res;
	}
	
	
}
