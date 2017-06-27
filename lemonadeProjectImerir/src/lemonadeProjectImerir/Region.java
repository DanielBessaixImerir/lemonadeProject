package lemonadeProjectImerir;

import com.google.gson.JsonObject;

public class Region {
	private Coord center;
	private Coord span;
	
	public Region(JsonObject json){
		this.center = new Coord(json.get("center").getAsJsonObject(), false);
		this.span = new Coord(json.get("span").getAsJsonObject(), true);
	}
	
	public Coord getCenter() {
		return center;
	}
	public Coord getSpan() {
		return span;
	}
	public void setCenter(Coord center) {
		this.center = center;
	}
	public void setSpan(Coord span) {
		this.span = span;
	}
	public void setRegionJson(JsonObject json){
		Coord center = new Coord(0,0);
		Coord span = new Coord(0,0);
	}

	@Override
	public String toString() {
		return "Region [center=" + center + ", span=" + span + "]";
	}
}
