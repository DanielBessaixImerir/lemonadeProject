package lemonadeProjectImerir;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lemonadeProjectImerir.JSON.HtmlGET;
import lemonadeProjectImerir.JSON.JsonWrite;

public class Map {
	private Region region;
	private ArrayList<Player> players;
	private Weather forecast;
	private Weather actualWeather;
	private int hour;
	
	public Map(JsonObject map, JsonObject metrologie){
		this.region = new Region(map.get("region").getAsJsonObject());
		JsonArray jsonRanking;
		jsonRanking = map.getAsJsonArray("ranking");
		JsonObject jsonPlayerInfo;
		jsonPlayerInfo = map.getAsJsonObject("playerInfo");
		this.players = new ArrayList<Player>();
		for (int i=0;i<jsonRanking.size();i++){
			String playerName=jsonRanking.get(i).getAsString();
			System.out.println(jsonPlayerInfo.getAsJsonObject(playerName));
			System.out.println(map.getAsJsonObject("itemsByPlayer").getAsJsonArray(playerName));
			this.players.add(new Player(playerName,jsonPlayerInfo.getAsJsonObject(playerName),map.getAsJsonObject("itemsByPlayer").getAsJsonArray(playerName)));
			System.out.println(map.getAsJsonObject("itemsByPlayer").getAsJsonArray(playerName));
		}
		this.setHour(metrologie.get("timestamp").getAsInt());
		String weather;
		if ( metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("dfn").getAsInt()==0){
			weather = metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.actualWeather=Weather.valueOf(weather);
			weather = metrologie.get("weather").getAsJsonArray().get(1).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.forecast=Weather.valueOf(weather);
		}
		else{
			weather = metrologie.get("weather").getAsJsonArray().get(1).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.actualWeather=Weather.valueOf(weather);
			weather = metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.forecast=Weather.valueOf(weather);
		}
			
	}
	
	public void majMap(JsonObject map, JsonObject metrologie){
		JsonArray jsonRanking;
		jsonRanking = map.getAsJsonArray("ranking");
		JsonObject jsonPlayerInfo;
		jsonPlayerInfo = map.getAsJsonObject("playerInfo");
		for (int i=0; i<this.players.size();i++){                         // Suppression des joueurs inexistant
			if (!playerExistInRanking(this.players.get(i).getNamePlayer(), jsonRanking)){
				ArrayList<Player> newArrayPlayer = new ArrayList<Player>();
				for (int j=0;j<this.players.size();j++){
					if (i!=j){
						newArrayPlayer.add(this.players.get(i));
					}
				}
				this.players=newArrayPlayer;
			}
		}
		for (int i =0 ; i<jsonRanking.size();i++){                      // ajout ou maj des joueurs
			if (!playerExistInArray(jsonRanking.get(i).getAsString(),this.getPlayers())){
				this.players.add(new Player(jsonRanking.get(i).getAsString(),jsonPlayerInfo.getAsJsonObject(jsonRanking.get(i).getAsString()),map.getAsJsonObject("itemsByPlayer").getAsJsonArray(jsonRanking.get(i).getAsString())));
			}
			else{
				this.getPlayerByName(jsonRanking.get(i).getAsString()).majPlayer(jsonPlayerInfo.getAsJsonObject(jsonRanking.get(i).getAsString()),map.getAsJsonObject("itemsByPlayer").getAsJsonArray(jsonRanking.get(i).getAsString()));
			}
		}
		this.setHour(metrologie.get("timestamp").getAsInt());
		String weather;
		if ( metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("dfn").getAsInt()==0){
			weather = metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.actualWeather=Weather.valueOf(weather);
			weather = metrologie.get("weather").getAsJsonArray().get(1).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.forecast=Weather.valueOf(weather);
		}
		else{
			weather = metrologie.get("weather").getAsJsonArray().get(1).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.actualWeather=Weather.valueOf(weather);
			weather = metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("weather").getAsString().toUpperCase();
			this.forecast=Weather.valueOf(weather);
		}
				
	}
	
	public Region getRegion() {
		return region;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public Weather getForecast() {
		return forecast;
	}
	public Weather getActualWeather() {
		return actualWeather;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public void setForecast(Weather forecast) {
		this.forecast = forecast;
	}
	public void setActualWeather(Weather actualWeather) {
		this.actualWeather = actualWeather;
	}
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}
	
	/**
	 * Is used to simulate client on a basis that there are one client each "pers" degree, It use the function chooseStand 7
	 * pers conseillé = 33
	 * @param pers
	 */
	public void simulateClients(int pers){
		float latitude;
		float longitude;
		Player shop;
		Coord coordSeller = new Coord(0, 0);
		for (float i=0; i<this.region.getSpan().getLatitude();i+=pers){
			for (float j=0; j<this.region.getSpan().getLongitude();j+=pers){
				latitude=this.region.getCenter().getLatitude()+(i*pers)-(this.region.getSpan().getLatitude()/2);
				longitude=this.region.getCenter().getLongitude()+(j*pers)-(this.region.getSpan().getLongitude()/2);
				coordSeller.setLatitude(latitude);
				coordSeller.setLongitude(longitude);
				if (probaToBuy()){
					//System.out.println("probaToBuy");
					shop = getInterstedShop(coordSeller);
					if(shop!=null){
						shop.buyWantedDrink(getWantedDrink());
					}

				}
			}
		}
	}
	/**
	 * this function is used to get an ArrayList of shop who have an influence on a coord
	 * @param coord
	 * @return
	 */
	private ArrayList<Player> getArrayOfInterestedShopByInfluence(Coord coord){
		ArrayList<Player> player = new ArrayList<Player>();
		int i=0; int j=0;
		while (i<this.players.size()){
			j=0;
			while (j<this.players.get(i).getMapItem().size()){
				if (coord.distance(this.players.get(i).getMapItem().get(j).getLocation())<this.players.get(i).getMapItem().get(j).getInfluence()*1869){ //getinfluence*quoi? a changer
					player.add(this.players.get(i));
					j=this.players.get(i).getMapItem().size();
				}
				j++;
			}
			i++;
		}
		System.out.println("coord:"+coord.toString()+" nbShop: "+player.size());
		return player;
	}
	
	/**
	 * return 0 for a cold drink, 1 for a hot one, 2 for a alcoholised one 
	 * @return
	 */
	private int getWantedDrink(){
		int typeDrink = 0;
		int alcoholProba=0, coldProba=0;
		if (this.hour<6||this.hour>20){
			alcoholProba++;
		}
		if (this.getActualWeather().equals(Weather.HEATWAVE)){
			return 0;
		}
		if (this.getActualWeather().equals(Weather.THUNDERSTORM)||this.getActualWeather().equals(Weather.RAINY)){
			coldProba--;
		}
		else{
			coldProba++;
		}
		int rand = (int)(Math.random()*10);
		boolean willBeCold=false;
		boolean wouldHaveAlcohol=false;
		switch(coldProba){
			case(-1):
				if (rand>1){
					willBeCold=false;
				}
				break;
			case(0):
				if (rand>4){
					willBeCold=false;
				}
				break;
			case(1):
				if (rand>7){
					willBeCold=false;
				}
				break;
		}
		rand = (int)(Math.random()*4);
		switch(alcoholProba){
		case(0):
			if (rand>2){
				wouldHaveAlcohol=true;
			}
			break;
		case(1):
			if (rand>0){
				wouldHaveAlcohol=true;
			}
		}
		rand = (int)(Math.random()*2);
		if (wouldHaveAlcohol){
			if (rand==1){
				return 2;
			}
		}
		if(willBeCold){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	private ArrayList<Player> getArrayOfInterstedShopByDrinks(ArrayList<Player> shop){
		ArrayList<Player> retour = new ArrayList<Player>();
		boolean alcohol=false, cold=false;
		switch (getWantedDrink()){
		case(0):
			cold = true;
		break;
		case(2):
			alcohol = true;
		break;
		}
		for (int i=0; i<shop.size();i++){
			if (shop.get(i).hasWantedDrinks(alcohol, cold)){
				retour.add(shop.get(i));
			}
		}
		return retour;
	}
	
	private Player getNearestShop(Coord coordseller, ArrayList<Player> shop){
		double distance = coordseller.distance(shop.get(0).getStand().getLocation());
		int nearestShopIndex = 0;
		for (int i=0; i<shop.size(); i++){
			if (distance>coordseller.distance(shop.get(i).getStand().getLocation())){
				nearestShopIndex=i;
			}
		}
		return shop.get(nearestShopIndex);
	}
	
	private Player getInterstedShop(Coord coordSeller){
		ArrayList<Player> shop = new ArrayList<Player>();
		int shopRet=0;
		shop = getArrayOfInterestedShopByInfluence(coordSeller);
		//System.out.println("nbshop="+shop.size());
		shop = getArrayOfInterstedShopByDrinks(shop);
		if (shop.size()>1){
			System.out.println("nbshop>1");
			if (this.forecast==Weather.HEATWAVE){
				shop.set(0, getNearestShop(coordSeller,shop));
			}
			else{
				double distanceTotal=0;
				for (int i=0; i<shop.size();i++){
					distanceTotal +=shop.get(i).getStand().getLocation().distance(coordSeller);
				}
				int[] probaDistance = new int[shop.size()];
				int[] probaPrice = new int[shop.size()];
				int[] proba = new int[shop.size()];
				int totalDistance=0;
				for (int i=0; i<shop.size();i++){
					probaDistance[i]=(int) (distanceTotal/shop.get(i).getStand().getLocation().distance(coordSeller));
					totalDistance+=probaDistance[i];
				}
				int totalProbDistance=0;
				for (int i=0; i<shop.size();i++){
					probaDistance[i]=(int)(1-(probaDistance[i]/totalDistance)*100);
					totalProbDistance+=probaDistance[i];
				}
				float totalPrice=0;
				for (int i=0; i<shop.size();i++){
					probaPrice[i]+=shop.get(i).getPriceForWantedDrink(getWantedDrink());
				}
				int priceTotal=0;
				for (int i=0; i<shop.size();i++){
					probaPrice[i]=(int)(totalPrice/shop.get(i).getPriceForWantedDrink(getWantedDrink()));
					priceTotal+=probaPrice[i];
				}
				int totalProbPrice=0;
				for (int i=0; i<shop.size();i++){
					probaPrice[i]=(int)(1-(probaPrice[i]/totalDistance)*100);
					totalProbPrice+=probaPrice[i];
				}
				for (int i=0; i<shop.size();i++){
					probaDistance[i]=(int)((probaDistance[i]/totalProbDistance)*100);
					probaPrice[i]=(int)((probaPrice[i]/totalProbPrice)*100);
					if (i>0){
						proba[i]=proba[i-1]+probaPrice[i]+probaDistance[i];
					}
					else{
						proba[0]=probaPrice[0]+probaDistance[0];
					}
				}
				int rand=(int)(Math.random()*proba[proba.length-1]);
				int i=0;
				while (rand<probaDistance[i]){
					i++;
				}
				shop.set(0,shop.get(i));
			}
		}
		else{
			if(shop.size()==0){
				shop.add(null);
				shopRet=0;
			}
		}
		return shop.get(shopRet);
	}
	
	private boolean probaToBuy(){
		int rand = (int)(Math.random()*100);
		switch(this.actualWeather){
		case CLOUDY:
			if (rand<31){
				return true;
			}
			break;
		case HEATWAVE:
			return true;
		case RAINY:
			if (rand<16){
				return true;
			}
			break;
		case SUNNY:
			if (rand<76){
				return true;
			}
			break;
		case THUNDERSTORM:
			if (rand<6){
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}
	
	private boolean playerExistInArray(String name, ArrayList<Player> players){
		int i=0;
		boolean found=false;
		while(!found && i<players.size()){
			if (players.get(i).getNamePlayer().equals(name)){
				found = true;
			}
			i++;
		}
		return found;
	}
	
	private boolean playerExistInRanking(String name, JsonArray ranking){
		int i=0;
		boolean found=false;
		while(!found && i<ranking.size()){
			if (ranking.get(i).equals(name)){
				found = true;
			}
			i++;
		}
		return found;
	}
	
	public static Map createMap(){
		String jsonString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/map");
		JsonObject jsonMap = JsonWrite.stringToJson(jsonString);
		String metrologieString =HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology");
		JsonObject jsonMetrologie = JsonWrite.stringToJson(metrologieString);
		System.out.println(jsonMetrologie.toString());
		return new Map(jsonMap,jsonMetrologie);
	}
	
	public void updateMap(){
		String jsonString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/map");
		JsonObject jsonMap = JsonWrite.stringToJson(jsonString);
		//System.out.println(jsonMap.toString());
		String metrologieString =HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology");
		JsonObject jsonMetrologie = JsonWrite.stringToJson(metrologieString);
		//System.out.println(jsonMetrologie.toString());
		this.majMap(jsonMap,jsonMetrologie);
		System.out.println("hour : "+this.getHour());
	}
	
	public void sendSales(){
		for (int i=0; i<this.getPlayers().size();i++){
			this.getPlayers().get(i).sendSales();
		}	
	}
	
	public Player getPlayerByName(String name){
		for (int i =0; i<this.players.size();i++){
			if (this.players.get(i).getNamePlayer().equals(name)){
				return this.players.get(i);
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		String players ="\n		";
		for (int i=0;i<this.players.size();i++){
			players = players + this.players.get(i).toString()+"\n		";
		}
		return "Map [region=" + region + ",\n players=" + players + ",\n forecast=" + forecast + ",\n actualWeather="
				+ actualWeather + ",\n hour=" + hour + "]";
	}
}
