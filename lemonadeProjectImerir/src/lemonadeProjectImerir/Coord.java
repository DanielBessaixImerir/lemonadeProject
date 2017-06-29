package lemonadeProjectImerir;

import com.google.gson.JsonObject;

public class Coord {
	public Coord(float longitude, float latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public Coord(JsonObject json, boolean isSpan){
		if(isSpan){
			
			this.latitude = json.get("latitudeSpan").getAsFloat();
			this.longitude = json.get("longitudeSpan").getAsFloat();
			
		}else{
			
			this.latitude = json.get("latitude").getAsFloat();
			this.longitude = json.get("longitude").getAsFloat();
		}
		
	}

	public float getLongitude() {
		return longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	private float longitude;
	private float latitude;
	
	public double distance(Coord coord){
		double latitudeX = this.getLatitude();
		double latitudeY = coord.getLatitude();
		double longitudeX = this.getLongitude();
		double longitudeY = coord.getLongitude();
		double miles = 60*Math.acos(Math.sin(latitudeX)*Math.sin(latitudeY)+Math.cos(latitudeX)*Math.cos(latitudeY)*Math.cos(longitudeY-longitudeX));
		double metre = miles*1869;
		return metre;
	}
	@Override
	public String toString() {
		return "Coord [longitude=" + longitude + ", latitude=" + latitude + "]";
	}

}
