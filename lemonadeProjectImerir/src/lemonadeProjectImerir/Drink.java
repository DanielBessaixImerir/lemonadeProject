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
	public void addSells(int i){
		this.sells+=i;
	}
	
	public boolean isWantedDrink(boolean alcohol, boolean cold){
		if (alcohol && this.hasAlcohol()==alcohol){
			return true;
		}
		if (this.isCold()==cold || this.hasAlcohol()==alcohol){
			return true;
		}
		return false;
	}
	public boolean isWantedDrink(int drinkType){
		switch(drinkType){
		case (0):
			return isWantedDrink(false,true);
		case (1):
			return isWantedDrink(false,false);
		case (2):
			return isWantedDrink(true,true);
		default:
			return false;
		}
	}

	@Override
	public String toString() {
		return "Drink [price=" + price + ", name=" + name + ", sells=" + sells + ", hasAlcohol=" + hasAlcohol
				+ ", isCold=" + isCold + "]";
	}

	public boolean priceIndecent() {
		int priceMax=0;
		switch(this.name){
		case "limonade":
			priceMax=2;
			break;
		case "the":
			priceMax=4;
			break;
		case "cocktail":
			priceMax=6;
			break;
		}
		if (this.price>priceMax){
			return true;
		}
		return false;
	}
}
