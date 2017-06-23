package lemonadeProjectImerir;

import java.util.ArrayList;

import com.google.gson.JsonObject;

public class MapItem {
	private Coord location;
	private KindItem kind;
	private float influence;
	
	public MapItem(JsonObject json){
		this.location = new Coord(json.get("location").getAsJsonObject(),false);
		if(json.get("Kind").getAsString().equals(KindItem.AD.toString())){
			this.kind = KindItem.AD;
		}else{
			this.kind = KindItem.STAND;
		}
		this.influence = json.get("influence").getAsFloat();
	}
}
