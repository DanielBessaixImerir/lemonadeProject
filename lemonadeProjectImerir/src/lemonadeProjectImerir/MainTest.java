package lemonadeProjectImerir;

import com.google.gson.JsonObject;
import java.util.concurrent.TimeUnit;

import lemonadeProjectImerir.JSON.HtmlGET;
import lemonadeProjectImerir.JSON.JsonWrite;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/map");
		//String jsonString="{\"region\": {\"center\": {\"latitude\": 0, \"longitude\": 0}, \"span\": {\"latitudeSpan\": 1000.0, \"longitudeSpan\": 1000.0}}, \"ranking\": [\"Sitiel\", \"Tacos\"],\"itemsByPlayer\": {\"Sitiel\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 388.68290845865, \"longitude\": 385.779888526111}, \"owner\": \"Sitiel\"}], \"Tacos\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 751.437348914844, \"longitude\": 990.830151637961}, \"owner\": \"Tacos\"}]}, \"playerInfo\": {\"Sitiel\": {\"cash\": 650000.1, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"profit\": 0, \"sales\": 0}, \"Tacos\": {\"cash\": 124999.5, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}], \"profit\": 0, \"sales\": 0}},\"drinksByPlayer\": {\"Sitiel\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"Tacos\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}]}}";
		JsonObject jsonMap = JsonWrite.stringToJson(jsonString);
		//System.out.println(jsonMap.toString());
		String metrologieString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology");
		//String metrologieString = "{\"timestamp\": 264,\"weather\": [{\"dfn\": 0,\"weather\": \"cloudy\"},{\"dfn\": 1,\"weather\": \"cloudy\"}]}";
		JsonObject jsonMetrologie = JsonWrite.stringToJson(metrologieString);
		System.out.println(jsonMetrologie.toString());
		
		Map map = new Map(jsonMap,jsonMetrologie);
		//System.out.println(map.toString());
		Player player = findPlayerByName("Yannick",map);
		map.simulateClients(200);
		//player.getDrinks().get(0).addSells(60);
		//System.out.println(map.toString());
		for (int i=0; i<map.getPlayers().size();i++){
			map.getPlayers().get(i).sendSales();
		}
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/map");
		//String jsonString="{\"region\": {\"center\": {\"latitude\": 0, \"longitude\": 0}, \"span\": {\"latitudeSpan\": 1000.0, \"longitudeSpan\": 1000.0}}, \"ranking\": [\"Sitiel\", \"Tacos\"],\"itemsByPlayer\": {\"Sitiel\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 388.68290845865, \"longitude\": 385.779888526111}, \"owner\": \"Sitiel\"}], \"Tacos\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 751.437348914844, \"longitude\": 990.830151637961}, \"owner\": \"Tacos\"}]}, \"playerInfo\": {\"Sitiel\": {\"cash\": 650000.1, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"profit\": 0, \"sales\": 0}, \"Tacos\": {\"cash\": 124999.5, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}], \"profit\": 0, \"sales\": 0}},\"drinksByPlayer\": {\"Sitiel\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"Tacos\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}]}}";
		jsonMap = JsonWrite.stringToJson(jsonString);
		metrologieString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology");
		//String metrologieString = "{\"timestamp\": 264,\"weather\": [{\"dfn\": 0,\"weather\": \"cloudy\"},{\"dfn\": 1,\"weather\": \"cloudy\"}]}";
		jsonMetrologie = JsonWrite.stringToJson(metrologieString);
		map.majMap(jsonMap, jsonMetrologie);
		//System.out.println(map.toString());
	}
	
	public static Player findPlayerByName(String name, Map map){
		Player player = null;
		for (int i=0; i<map.getPlayers().size();i++){
			if (map.getPlayers().get(i).getNamePlayer().equals(name)){
				player=map.getPlayers().get(i);
			}
		}
		return player;
	}
}