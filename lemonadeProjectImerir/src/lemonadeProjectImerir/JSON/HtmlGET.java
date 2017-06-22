package lemonadeProjectImerir.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlGET {
	 public static String getGETResponse() {
	        
	        String ret ="";
	         
	        try {
	        	// url a voir
	            String myurl= "https://tranquil-reef-75630.herokuapp.com/heure";
	            // création de l'url
	            URL url = new URL(myurl);
	            // preparation de la connection
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            // connection
	            connection.connect();
	            // recuperation des données
	            InputStream inputStream = connection.getInputStream();
	            //transformation des données en string
	            String result = InputStreamToString(inputStream,1024);
	            ret = result;

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        // On retourne la liste des personnes
	        System.out.println(ret);
	        return ret;
	    }
	 
	 public static String InputStreamToString (InputStream in, int bufSize) {         
	        final StringBuilder out = new StringBuilder(); 
	        final byte[] buffer = new byte[bufSize]; 
	        try {
	        	// lecture byte par bytes puis ajouts dans un string
	            for (int ctr; (ctr = in.read(buffer)) != -1;) {
	                out.append(new String(buffer, 0, ctr));
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("Cannot convert stream to string", e);
	        }
	        // On retourne la chaine contenant les donnees de l'InputStream
	        return out.toString(); 
	    }
}
