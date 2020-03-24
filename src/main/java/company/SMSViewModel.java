package company;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

public class SMSViewModel 
{

	//http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
	
	 public static final String API_KEY = "edf0e27b";
	 public static final String API_SECRET = "d6ad3a854c40acfa";
	 public static final String SMS_FROM = "12345";
	 public static final String SMS_TO = "447777111222";
	 public static final String SMS_TEXT = "Hello World!";
	    
	    
	private Logger logger = Logger.getLogger(this.getClass());
	private String sender;
	private String to;
	private String message;
	private String result;
		
	public SMSViewModel()
	{
		try
		{
			
		}
		
		catch (Exception ex)
		{
		logger.error("error at SMSViewModel>>Init>> ",ex);		
		} 
	}

	
	@Command
	@NotifyChange({"result"})
	public void sendSMSCommand()
	{
		try
		{
			result="send";
			//int x=Integer.parseInt(to);
			
			String url = "https://rest.nexmo.com/sms/json";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			String urlParameters = "api_key=%s&api_secret=%s&from=%s&to=%s&text=%s";
			urlParameters=String.format(urlParameters, API_KEY,API_SECRET,sender,to,message);
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			//print result
			System.out.println(response.toString());
			
		}
		
		catch (Exception ex)
		{
		result="Error at send SMS !!";
		logger.info("error at SMSViewModel>>sendSMSCommand>> ",ex);
		} 
	}


	public String getSender() {
		return sender;
	}



	public void setSender(String sender) {
		this.sender = sender;
	}



	public String getTo() {
		return to;
	}



	public void setTo(String to) {
		this.to = to;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getResult() {
		return result;
	}



	public void setResult(String result) {
		this.result = result;
	}
}
