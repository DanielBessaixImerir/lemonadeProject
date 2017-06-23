package lemonadeProjectImerir.JSON;


public class JsonWrite {
		public static String SaleRequest(String player, String drink, int quantity){
			String ret = "[{" +
	                "\"player\":\""+player+"\"," +
	                "\"item\":\""+drink+"\"," +
	                "\"quantity\":\""+quantity+"\"" +
	                "}]";
			return ret;
		}
			
	}
