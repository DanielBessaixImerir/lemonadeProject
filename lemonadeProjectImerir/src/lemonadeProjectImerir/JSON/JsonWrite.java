package lemonadeProjectImerir.JSON;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonWrite {
		public static String SaleRequest(String player, String drink, int quantity){
			String ret = "{" +
	                "\"player\":\""+player+"\"," +
	                "\"item\":\""+drink+"\"," +
	                "\"quantity\":\""+quantity+"\"" +
	                "}";
			return ret;
		}
		
		public static JsonObject stringToJson(String jsonString){
			JsonObject ret = new JsonParser().parse(jsonString).getAsJsonObject();
			return ret;
		}
		
			
	}
