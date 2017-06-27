package lemonadeProjectImerir;

import com.google.gson.JsonObject;

public class MapItem {
	private Coord location;
	private KindItem kind;
	private float influence;
	private String Owner;
	
	public MapItem(JsonObject json){
		this.location = new Coord(json.get("location").getAsJsonObject(),false);
		if(json.get("Kind").getAsString().equals(KindItem.AD.toString())){
			this.kind = KindItem.AD;
		}else{
			this.kind = KindItem.STAND;
		}
		this.influence = json.get("influence").getAsFloat();
		this.setOwner(json.get("owner").getAsString());
	}

	public Coord getLocation() {
		return location;
	}

	public void setLocation(Coord location) {
		this.location = location;
	}

	public KindItem getKind() {
		return kind;
	}

	public void setKind(KindItem kind) {
		this.kind = kind;
	}

	public float getInfluence() {
		return influence;
	}

	public void setInfluence(float influence) {
		this.influence = influence;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		Owner = owner;
	}
}
