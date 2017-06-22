package lemonadeProjectImerir.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

	public static void main(String[] args) {
		//getPersonnes();
		sendJSON("https://tranquil-reef-75630.herokuapp.com/heure");

	}
	/**
     * Récupère une liste de personnes.
     * @return ArrayList<Personne>: ou autre type de données.
     * @author François http://www.francoiscolin.fr/
     */
    public static String getPersonnes() {
        
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
    
    static void sendJSON(String strUrl){
    	try {
	    	URL url = new URL(strUrl);
	        HttpURLConnection connection;
			
				connection = (HttpURLConnection) url.openConnection();
			
	        connection.setConnectTimeout(5000);//5 secs
	        connection.setReadTimeout(5000);//5 secs
	
	        connection.setRequestMethod("POST");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/json");
	
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());  
	        out.write(
	                "[ " +
	                "\"the fox jumps over the lazy dog\"," +
	                "\"another thing here\" " +
	                "]");
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