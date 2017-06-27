package lemonadeProjectImerir.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlPOST {
	 static void sendJSON(String strUrl){
	    	try {
		    	URL url = new URL(strUrl);
		        HttpURLConnection connection;
				
		        connection = (HttpURLConnection) url.openConnection();
				
		        //connection.setConnectTimeout(5000);//5 secs
		       // connection.setReadTimeout(5000);//5 secs
		
		        connection.setRequestMethod("POST");
		        connection.setDoOutput(true);
		        connection.setRequestProperty("Content-Type", "application/json");
		
		        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());  
		        out.write(
		                "[{" +
		                "\"player\":\"dani\"," +
		                "\"item\":\"stand\"," +
		                "\"quantity\":\"5\"" +
		                "}]");
		        out.flush();
		        out.close();
		
		        int res = connection.getResponseCode();
		
		        System.out.println(res);
		
		
		        InputStream is = connection.getInputStream();
		        BufferedReader br = new BufferedReader(new InputStreamReader(is));
		        String line = null;
		        while((line = br.readLine() ) != null) {
		            System.out.println(line);
		        }
		        connection.disconnect();
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

}
