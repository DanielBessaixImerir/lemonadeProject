package lemonadeProjectImerir;

import com.google.gson.JsonObject;

import lemonadeProjectImerir.JSON.HtmlGET;
import lemonadeProjectImerir.JSON.JsonWrite;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/map");
		//String jsonString="{\"region\": {\"center\": {\"latitude\": 0, \"longitude\": 0}, \"span\": {\"latitudeSpan\": 1000.0, \"longitudeSpan\": 1000.0}}, \"ranking\": [\"Sitiel\", \"Tacos\"],\"itemsByPlayer\": {\"Sitiel\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 388.68290845865, \"longitude\": 385.779888526111}, \"owner\": \"Sitiel\"}], \"Tacos\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 751.437348914844, \"longitude\": 990.830151637961}, \"owner\": \"Tacos\"}]}, \"playerInfo\": {\"Sitiel\": {\"cash\": 650000.1, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"profit\": 0, \"sales\": 0}, \"Tacos\": {\"cash\": 124999.5, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}], \"profit\": 0, \"sales\": 0}},\"drinksByPlayer\": {\"Sitiel\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"Tacos\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}]}}";
		JsonObject jsonMap = JsonWrite.stringToJson(jsonString);
		System.out.println(jsonMap.toString());
		String metrologieString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology");
		//String metrologieString = "{\"timestamp\": 264,\"weather\": [{\"dfn\": 0,\"weather\": \"cloudy\"},{\"dfn\": 1,\"weather\": \"cloudy\"}]}";
		JsonObject jsonMetrologie = JsonWrite.stringToJson(metrologieString);
		System.out.println(jsonMetrologie.toString());
		
		Map map = new Map(jsonMap,jsonMetrologie);
		System.out.println(map.toString());
	}
}