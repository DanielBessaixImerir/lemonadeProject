package lemonadeProjectImerir.JSON;
public class Main {

	public static void main(String[] args) {
		//getPersonnes();
		//HtmlPOST.sendJSON("https://tranquil-reef-75630.herokuapp.com/");
		//Gson g = new GsonBuilder().create();
		//System.out.println(JsonWrite.SaleRequest("Daniel","margarita",3));
		System.out.println(JsonWrite.stringToJson("{\"player\":\"dani\",\"item\":\"stand\",\"quantity\":\"5\"}").toString());
	}
	
}

