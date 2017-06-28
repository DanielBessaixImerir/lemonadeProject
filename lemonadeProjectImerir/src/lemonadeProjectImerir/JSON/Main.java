package lemonadeProjectImerir.JSON;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lemonadeProjectImerir.Map;
import lemonadeProjectImerir.Weather;

public class Main {

	public static void main(String[] args) {
		//getPersonnes();
		//HtmlPOST.sendJSON("https://tranquil-reef-75630.herokuapp.com/");
		//Gson g = new GsonBuilder().create();
		/*System.out.println(JsonWrite.SaleRequest("Daniel","margarita",3));
		JsonObject json = new JsonObject();
		String test ="cloudy";
		System.out.println(test);
		test = test.toUpperCase();
		System.out.println(test);
		if (Weather.CLOUDY == Weather.valueOf(test)){
			System.out.println("coucou");
		}*/
		System.out.println(HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology"));	
	}
}