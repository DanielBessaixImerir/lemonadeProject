package lemonadeProjectImerir;

import java.util.ArrayList;
import com.google.gson.JsonObject;

public class Map {
	private Region region;
	private ArrayList<Player> players;
	private Weather forecast;
	private Weather actualWeather;
	
	public Map(JsonObject json, Weather forecast, Weather actualWeather){
		this.region = new Region(json.get("region").getAsJsonObject());
		this.forecast=forecast;
		this.actualWeather=actualWeather;
		
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
}
