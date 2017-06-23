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
	
	public Player(String name, JsonObject json){
		this.cash = json.get("cash").getAsInt();
		this.sales = json.get("sales").getAsInt();
		this.profit = json.get("profit").getAsFloat();
		this.drinks = new ArrayList<Drink>();
		JsonArray jsonArrayDrink = json.get("drinksOffered").getAsJsonArray();
		for (int i=0;i<jsonArrayDrink.size();i++){
			this.drinks.add(new Drink(jsonArrayDrink.get(i).getAsJsonObject()));
		}
	}
}
