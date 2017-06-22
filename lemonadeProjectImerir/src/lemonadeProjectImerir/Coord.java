package lemonadeProjectImerir;

public class Coord {
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
}
