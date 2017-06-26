package lemonadeProjectImerir;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Player {
	private String namePlayer;
	private int cash;
	private int sales;
	private float profit;
	private ArrayList<Drink> drinks;
	private ArrayList<MapItem> mapItem;
	
	public Player(String name, JsonObject jsonPlayer, JsonObject jsonMapItem){
		this.cash = jsonPlayer.get("cash").getAsInt();
		this.sales = jsonPlayer.get("sales").getAsInt();
		this.profit = jsonPlayer.get("profit").getAsFloat();
		this.drinks = new ArrayList<Drink>();
		JsonArray jsonArrayDrink = jsonPlayer.get("drinksOffered").getAsJsonArray();
		for (int i=0;i<jsonArrayDrink.size();i++){
			this.drinks.add(new Drink(jsonArrayDrink.get(i).getAsJsonObject()));
		}
		JsonArray jsonArrayMapItem = jsonMapItem.getAsJsonArray();
		for (int i=0;i<jsonArrayMapItem.size();i++){
			this.mapItem.add(new MapItem(jsonArrayMapItem.get(i).getAsJsonObject()));
		}
	}
	
	public Player(String name, JsonObject jsonPlayer){
		this.cash = jsonPlayer.get("cash").getAsInt();
		this.sales = jsonPlayer.get("sales").getAsInt();
		this.profit = jsonPlayer.get("profit").getAsFloat();
		this.drinks = new ArrayList<Drink>();
		JsonArray jsonArrayDrink = jsonPlayer.get("drinksOffered").getAsJsonArray();
		for (int i=0;i<jsonArrayDrink.size();i++){
			this.drinks.add(new Drink(jsonArrayDrink.get(i).getAsJsonObject()));
		}
		mapItem = new ArrayList<MapItem>();
	}
	
	public void addMapItem(JsonObject mapItem){
		this.mapItem.add(new MapItem(mapItem));
	}
	
	public void addDrinks(JsonObject drinks){
		this.drinks.add(new Drink(drinks));
	}

	public String getNamePlayer() {
		return namePlayer;
	}

	public int getCash() {
		return cash;
	}

	public int getSales() {
		return sales;
	}

	public float getProfit() {
		return profit;
	}

	public ArrayList<Drink> getDrinks() {
		return drinks;
	}

	public ArrayList<MapItem> getMapItem() {
		return mapItem;
	}

	public void setNamePlayer(String namePlayer) {
		this.namePlayer = namePlayer;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	public void setProfit(float profit) {
		this.profit = profit;
	}

	public void setDrinks(ArrayList<Drink> drinks) {
		this.drinks = drinks;
	}

	public void setMapItem(ArrayList<MapItem> mapItem) {
		this.mapItem = mapItem;
	}
	
	public MapItem getStand(){
		int i=0;
		boolean found=false;
		while (i<this.mapItem.size() && !found){
			if(this.mapItem.get(i).getKind()==KindItem.STAND){
				found=true;
			}
			i++;
		}
		return mapItem.get(i-1);
	}
}
