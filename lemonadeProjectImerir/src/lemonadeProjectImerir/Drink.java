package lemonadeProjectImerir;

import com.google.gson.JsonObject;

public class Drink {
	private float price;
	private String name;
	private int sells;
	private boolean hasAlcohol;
	private boolean isCold;
	
	public Drink(JsonObject json){
		this.price = json.get("price").getAsFloat();
		this.name = json.get("name").getAsString();
		this.hasAlcohol = json.get("hasAlcohol").getAsBoolean();
		this.isCold = json.get("isCold").getAsBoolean();
		this.sells = 0;
	}
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSells() {
		return sells;
	}
	public void setSells(int sells) {
		this.sells = sells;
	}
	public boolean hasAlcohol() {
		return hasAlcohol;
	}
	public void setHasAlcohol(boolean hasAlcohol) {
		this.hasAlcohol = hasAlcohol;
	}
	public boolean isCold() {
		return isCold;
	}
	public void setCold(boolean isCold) {
		this.isCold = isCold;
	}
	
	public boolean isWantedDrink(boolean alcohol, boolean cold){
		if (this.isCold()==cold || this.hasAlcohol()==alcohol){
			return true;
		}
		return false;
	}
}
