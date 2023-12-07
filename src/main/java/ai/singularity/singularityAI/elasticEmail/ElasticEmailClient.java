package ai.singularity.singularityAI.elasticEmail;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ElasticEmailClient {
	
	public static String Send(String from, String fromName, String subject, String body, String to, String isTransactional) {
		 try {
		 
			 String encoding = "UTF-8";
			 String apiKey = "E7BDC898FD914C4F913A65762BA37786480D67D15701D9366F9DED89C9D9E3FB6668D33700783EEC77EAFDA28CB2FBD0";
			 
			 String data = "apikey=" + URLEncoder.encode(apiKey, encoding);
			 data += "&from=" + URLEncoder.encode(from, encoding);
			 data += "&fromName=" + URLEncoder.encode(fromName, encoding);
			 data += "&subject=" + URLEncoder.encode(subject, encoding);
			 data += "&bodyHtml=" + URLEncoder.encode(body, encoding);
			 data += "&to=" + URLEncoder.encode(to, encoding);
			 data += "&isTransactional=" + URLEncoder.encode(isTransactional, encoding);
			 
			 URL url = new URL("https://api.elasticemail.com/v2/email/send");
			 URLConnection conn = url.openConnection();
			 conn.setDoOutput(true);
			 OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			 wr.write(data);
			 wr.flush();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
			 String result = rd.readLine();
			 wr.close();
			 rd.close();

			 return result;
		 }
		 
		 catch(Exception e) {
			 e.printStackTrace();
			 return e.getMessage();
		 }
	}
}
