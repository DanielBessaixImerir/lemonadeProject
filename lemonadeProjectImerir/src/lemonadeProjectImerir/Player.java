package lemonadeProjectImerir;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lemonadeProjectImerir.JSON.HtmlPOST;

public class Player {
	private String namePlayer;
	private int cash;
	private int sales;
	private float profit;
	private ArrayList<Drink> drinks;
	private ArrayList<MapItem> mapItem;
	private Color color;
	
	public Player(String name, JsonObject jsonPlayer, JsonArray jsonArrayMapItem){
		this.namePlayer = name;
		this.cash = jsonPlayer.get("cash").getAsInt();
		this.sales = jsonPlayer.get("sales").getAsInt();
		this.profit = jsonPlayer.get("profit").getAsFloat();
		this.drinks = new ArrayList<Drink>();
		this.mapItem = new ArrayList<MapItem>();
		
		JsonArray jsonArrayDrink = jsonPlayer.get("drinksOffered").getAsJsonArray();
		for (int i=0;i<jsonArrayDrink.size();i++){
			this.drinks.add(new Drink(jsonArrayDrink.get(i).getAsJsonObject()));
		}
		for (int i=0;i<jsonArrayMapItem.size();i++){
			this.mapItem.add(new MapItem(jsonArrayMapItem.get(i).getAsJsonObject()));
		}
		this.color = new Color();
	}
	

	public void majPlayer(JsonObject jsonPlayer, JsonArray jsonArrayMapItem) {
		this.cash = jsonPlayer.get("cash").getAsInt();
		this.sales = jsonPlayer.get("sales").getAsInt();
		this.profit = jsonPlayer.get("profit").getAsFloat();
		this.drinks = new ArrayList<Drink>();
		this.mapItem = new ArrayList<MapItem>();
		
		JsonArray jsonArrayDrink = jsonPlayer.get("drinksOffered").getAsJsonArray();
		for (int i=0;i<jsonArrayDrink.size();i++){
			this.drinks.add(new Drink(jsonArrayDrink.get(i).getAsJsonObject()));
		}
		for (int i=0;i<jsonArrayMapItem.size();i++){
			this.mapItem.add(new MapItem(jsonArrayMapItem.get(i).getAsJsonObject()));
		}
	}
	
	public Player(String name, JsonObject jsonPlayer){
		this.namePlayer = name;
		this.cash = jsonPlayer.get("cash").getAsInt();
		this.sales = jsonPlayer.get("sales").getAsInt();
		this.profit = jsonPlayer.get("profit").getAsFloat();
		this.drinks = new ArrayList<Drink>();
		JsonArray jsonArrayDrink = jsonPlayer.get("drinksOffered").getAsJsonArray();
		for (int i=0;i<jsonArrayDrink.size();i++){
			this.drinks.add(new Drink(jsonArrayDrink.get(i).getAsJsonObject()));
		}
		mapItem = new ArrayList<MapItem>();
		this.color = new Color();
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
	
	public ArrayList<MapItem> getAd(){
		int i=0;
		ArrayList<MapItem> ret = new ArrayList<MapItem>();
		while (i<this.mapItem.size()){
			if(this.mapItem.get(i).getKind()==KindItem.AD){
				if(this.mapItem.get(i).getOwner().equals(this.namePlayer)){
					ret.add(this.mapItem.get(i));
				}
			}
			i++;
		}
		return ret;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean hasWantedDrinks(boolean alcohol, boolean cold){
	boolean ret=false;
	int i=0;
		while (i<this.drinks.size()){
			if (this.drinks.get(i).isWantedDrink(alcohol, cold)){
				ret = true;
			}
			i++;
		}
	return ret;
	}
	
	public void buyWantedDrink(int wantedDrinksType){
		int i=0;
		boolean find=false;
		while (i<this.drinks.size()&&!find){
			if (this.drinks.get(i).isWantedDrink(wantedDrinksType)){
				this.drinks.get(i).addSells(1);
				this.sales+=1;
				find=true;
			}
			i++;
		}
	}
	
	public float getPriceForWantedDrink(int wantedDrinksType){
		int i=0;
		while (i<this.drinks.size()){
			if (this.drinks.get(i).isWantedDrink(wantedDrinksType)){
				return this.drinks.get(i).getPrice();
			}
			i++;
		}
		return -1;
	}
	
	public void sendSales(){
		for (int i=0; i<this.drinks.size();i++){
			if (this.drinks.get(i).getSells()>0){
				HtmlPOST.sendJSON(this.namePlayer,this.drinks.get(i).getName(),this.drinks.get(i).getSells());
			}
		}
	}

	@Override
	public String toString() {
		String mapItem = "";
		for (int i=0;i<this.mapItem.size();i++){
			mapItem = mapItem + this.mapItem.get(i).toString();
		}
		return "Player [namePlayer=" + namePlayer + ", cash=" + cash + ", sales=" + sales + ", profit=" + profit
				+ ", drinks=" + drinks + ", mapItem=" + mapItem + "]";
	}

}
