package lemonadeProjectImerir.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

public class HtmlPOST {
	 static void sendJSON(String strUrl, String name, String item, int quantity){
	    	try {
		    	URL url = new URL(strUrl);
		        HttpURLConnection connection;
				
		        connection = (HttpURLConnection) url.openConnection();
				
		        //connection.setConnectTimeout(5000);//5 secs
		       // connection.setReadTimeout(5000);//5 secs
		
		        connection.setRequestMethod("POST");
		        connection.setDoOutput(true);
		        connection.setRequestProperty("Content-Type", "application/json");
		        
		        JsonObject json = new JsonObject();
		        json.addProperty("player", name);
		        json.addProperty("item", item);
		        json.addProperty("quantity", quantity);
		        System.out.println(json.toString());
		        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());  
		        out.write(json.toString());
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
	 public static void sendJSON(String name, String item, int quantity){
		 sendJSON("https://tranquil-reef-75630.herokuapp.com/sales",name,item,quantity);
	 }
	 

}
