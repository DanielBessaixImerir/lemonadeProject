package lemonadeProjectImerir.JSON;


import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.*;

public class Main {

	public static void main(String[] args) {
		//getPersonnes();
		HtmlPOST.sendJSON("https://tranquil-reef-75630.herokuapp.com/test");
		Gson g = new GsonBuilder().create();
	}
	
}