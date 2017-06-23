package lemonadeProjectImerir.JSON;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonWrite {
		public static String SaleRequest(String player, String drink, int quantity){
			String ret = "[{" +
	                "\"player\":\""+player+"\"," +
	                "\"item\":\""+drink+"\"," +
	                "\"quantity\":\""+quantity+"\"" +
	                "}]";
			return ret;
		}
		
		public static JsonObject stringToJson(String jsonString){
			String string = "abcde"; // The String You Need To Be Converted 
			JsonObject convertedObject = new Gson().fromJson(string, JsonObject.class);
			return convertedObject;
		}
		
			
	}
