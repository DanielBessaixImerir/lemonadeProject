package lemonadeProjectImerir;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
			this.players.add(new Player(playerName,jsonPlayerInfo.getAsJsonObject(playerName),map.getAsJsonObject("itemsByPlayer").getAsJsonArray(playerName)));
		}
		this.setHour(metrologie.get("timestamp").getAsInt());
		String weather;
		weather = metrologie.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("weather").getAsString().toUpperCase();
		this.actualWeather=Weather.valueOf(weather);
		weather = metrologie.get("weather").getAsJsonArray().get(1).getAsJsonObject().get("weather").getAsString().toUpperCase();
		this.forecast=Weather.valueOf(weather);
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
	 * Is used to simulate client on a basis that there are one client each "pers" degree, It use the function chooseStand
	 * @param pers
	 */
	public void simulateClients(int pers){
		float latitude;
		float longitude;
		Coord coordSeller = new Coord(0, 0);
		for (float i=0; i<this.region.getSpan().getLatitude();i+=pers){
			for (float j=0; j<this.region.getSpan().getLongitude();j+=pers){
				latitude=this.region.getCenter().getLatitude()-(this.region.getSpan().getLatitude()/2);
				longitude=this.region.getCenter().getLongitude()-(this.region.getSpan().getLongitude()/2);
				coordSeller.setLatitude(latitude);
				coordSeller.setLongitude(longitude);
				
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
				if (coord.distance(this.players.get(i).getMapItem().get(j).getLocation())<this.players.get(i).getMapItem().get(j).getInfluence()){
					player.add(this.players.get(i));
					j=this.players.get(i).getMapItem().size();
				}
				j++;
			}
			i++;
		}
		return player;
	}
	
	/*private ArrayList<Player> getArrayOfInterstedShopByDrinks(){
		ArrayList<Player> shop = new ArrayList<Player>();
		boolean wantedColdDrinks=true;
		boolean wantedAlcohol=false;
		if (this.hour<6||this.hour>18){
			wantedColdDrinks=false;
			wantedAlcohol=true;
		}
		shop.add(e)
		return shop;
		
	}*/
	
	
	private void chooseStand(Coord coordSeller){
		ArrayList<Player> InterestedShop;
		InterestedShop = getArrayOfInterestedShopByInfluence(coordSeller);
		if (this.actualWeather==Weather.HEATWAVE){
			int i=0;
		}
		if (InterestedShop.size()>0){
			if(InterestedShop.size()>1){
				double distanceTotal=0;
				for (int i=0; i<InterestedShop.size();i++){
					distanceTotal +=InterestedShop.get(i).getStand().getLocation().distance(coordSeller);
				}
				int[] proba = new int[InterestedShop.size()];
				int total=0;
				for (int i=0; i<InterestedShop.size();i++){
					proba[i]=(int) (distanceTotal/InterestedShop.get(i).getStand().getLocation().distance(coordSeller));
					total+=proba[i];
				}
				for (int i=0; i<InterestedShop.size();i++){
					proba[i]=proba[i]/total;
				}
			}
		}
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
