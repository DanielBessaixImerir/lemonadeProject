package lemonadeProjectImerir;

import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import lemonadeProjectImerir.JSON.HtmlGET;
import lemonadeProjectImerir.JSON.JsonWrite;
import sun.misc.Cleaner;


public class Treeview extends Application {
	int height = 482;
	int width = 744;
	int height_h=50;
	int width_t = 200;
	int r,g,b;
	Map map;
	TreeTableView<String> treeTableView;
	TreeItem<String> playTree;
	int hour=-1;
	 
	public static void main(String[] args) 
		{
				/*// TODO Auto-generated method stub
				String jsonString=HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/map");
				//String jsonString="{\"region\": {\"center\": {\"latitude\": 500, \"longitude\": 500}, \"span\": {\"latitudeSpan\": 1000.0, \"longitudeSpan\": 1000.0}}, \"ranking\": [\"Sitiel\", \"Tacos\"],\"itemsByPlayer\": {\"Sitiel\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 388.68290845865, \"longitude\": 385.779888526111}, \"owner\": \"Sitiel\"}], \"Tacos\": [{\"influence\": 10.0, \"kind\": \"stand\", \"location\": {\"latitude\": 751.437348914844, \"longitude\": 990.830151637961}, \"owner\": \"Tacos\"}]}, \"playerInfo\": {\"Sitiel\": {\"cash\": 650000.1, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"profit\": 0, \"sales\": 0}, \"Tacos\": {\"cash\": 124999.5, \"drinksOffered\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}], \"profit\": 0, \"sales\": 0}},\"drinksByPlayer\": {\"Sitiel\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"10000\"}], \"Tacos\": [{\"hasAlcohol\": false, \"isCold\": true, \"name\": \"Limonade\", \"price\": \"15000\"}]}}";
				JsonObject jsonMap = JsonWrite.stringToJson(jsonString);
				System.out.println(jsonMap.toString());
				String metrologieString =HtmlGET.getGETResponse("https://tranquil-reef-75630.herokuapp.com/metrology");
				//String metrologieString = "{\"timestamp\": 264,\"weather\": [{\"dfn\": 0,\"weather\": \"cloudy\"},{\"dfn\": 1,\"weather\": \"cloudy\"}]}";
				JsonObject jsonMetrologie = JsonWrite.stringToJson(metrologieString);
				System.out.println(jsonMetrologie.toString());
				map = new Map(jsonMap,jsonMetrologie);
				
				System.out.println(map.toString());*/
			
				Application.launch(args);
		}

  @Override
  public void start(Stage stage) 
  	{
	     
		    final Group root = new Group();
		    final Group rootc = new Group();
		    final StackPane pop = new StackPane();
		    final Canvas canvas = new Canvas(width, height);
		    final Canvas g_canvas = new Canvas(width+ width_t+50, height_h);
		    final GraphicsContext gc = canvas.getGraphicsContext2D();
		    final GraphicsContext gd = g_canvas.getGraphicsContext2D();
		    
		    map = Map.createMap();
		    

		    Image img = new Image("file:img/map.png");
		    gc.drawImage(img, 0, 0);
		    
		   
		    weather_infos(gd);
		    
		    player_infos(gc);
		    rootc.getChildren().addAll(canvas,treeTableView);
		    rootc.setLayoutY(height_h);
		    pop.getChildren().addAll(g_canvas);
		    pop.setStyle("-fx-background-color: #47acff");
		    root.getChildren().addAll(rootc,pop);
		    
		    stage.setTitle("Iyokan");
		    stage.setScene(new Scene(root));
		    stage.show();
		    new Timer().schedule(
		            new TimerTask() 
		            	{
		                @Override
		                public void run() 
		                {		                	
		                	weather_infos(gd);
		                	map.updateMap();
		                	if (hour<map.getHour()){
		                		hour=map.getHour();
		                		map.simulateClients(100);
		                		map.sendSales();
			                	System.out.println(map.toString());
			                			                
		                	}
		                	player_infos(gc);
		                }
		            	},0, 3000);  //30000 = TOUTES LES 30000 MILLISECONDES
  	}
	  private void drawShapes(GraphicsContext gc,MapItem kind,Coord location) {
		  int x = XLongitude(location.getLongitude());
		  int y = YLatitude(location.getLatitude());

		  System.out.println("x:"+x+"y:"+y);
		  int influence = (int)(kind.getInfluence());
		  int radiusY= (int) (influence* height/ map.getRegion().getSpan().getLatitude() );
		  int radiusX= (int) (influence*width/map.getRegion().getSpan().getLongitude());
		  
		gc.setFill(Color.BLUE);
	    
	    if(kind.getKind()==KindItem.STAND){
	    	gc.fillText("Stand", y, x);
	    }else{
	    	gc.fillText("Ad", y, x);
	    }
	    
	    gc.setStroke(Color.rgb(r,g,b));
	    gc.setLineWidth(2);
	    gc.strokeOval( y-radiusY/2,x-radiusX/2, radiusY, radiusX);
		
		}
	  
	  	/*
		 * Cette fonction permet de convertir la latitude en coordonée X pour l'affichage sur le canvas
		 */
		private int YLatitude(float latitude){
			int retour = (int) (height*(latitude-(map.getRegion().getCenter().getLatitude()-(map.getRegion().getSpan().getLatitude()/2)))/map.getRegion().getSpan().getLatitude());
			return retour;
		}
		
		/*
		 * Cette fonction permet de convertir la longitude en coordonée Y pour l'affichage sur le canvas
		 */
		private int XLongitude(float longitude){
			int retour = (int) (width*(longitude-(map.getRegion().getCenter().getLongitude()-(map.getRegion().getSpan().getLongitude()/2)))/map.getRegion().getSpan().getLongitude());
			return retour;
		}
		private void weather_infos(GraphicsContext gd){
			
			gd.clearRect(0, 0, width_t+width, height_h);
			gd.setTextAlign(TextAlignment.CENTER);
			gd.setTextBaseline(VPos.CENTER);
			gd.setFont(Font.font ("Verdana", 20));
			gd.fillText("Timestamp : "+map.getHour()+"    Weather : "+map.getActualWeather()+"           Forecast : "+map.getForecast(), Math.round(gd.getCanvas().getWidth()/2),Math.round(gd.getCanvas().getHeight()/2));
		}
		
		
	  
	private void player_infos(GraphicsContext gc){
		//this.treeTableView=null;
		//playTree = null;
	  	
		playTree = new TreeItem<>(""+map.getHour());
		for(int i=0; i<map.getPlayers().size();i++)
	    {	
			TreeItem<String> player_info = null;
		  	TreeItem<String> cash = null;
		  	TreeItem<String> stand = null;
		  	TreeItem<String> coord_stand = null ;
		  	TreeItem<String> ad_panel = null;
		  	TreeItem<String> coord_ad_panel = null;
		    TreeItem<String> drinks = null;
		  	TreeItem<String> drink_name = null;
		  	
		  	r=map.getPlayers().get(i).getColor().getR();
		  	g=map.getPlayers().get(i).getColor().getG();
		  	b=map.getPlayers().get(i).getColor().getB();
		  	
			player_info = new TreeItem<>(map.getPlayers().get(i).getNamePlayer());
			cash = new TreeItem<>("Cash : "+map.getPlayers().get(i).getCash());
			stand = new TreeItem<>("Stand");
			coord_stand = new TreeItem<>("X : "+map.getPlayers().get(i).getStand().getLocation().getLongitude()+
	        		" _ Y : "+map.getPlayers().get(i).getStand().getLocation().getLatitude()+"\n Influence : "+map.getPlayers().get(i).getStand().getInfluence());
						drawShapes(gc,map.getPlayers().get(i).getStand(),map.getPlayers().get(i).getStand().getLocation());
	        
			ad_panel = new TreeItem<>("Ad_panel");
	        for(int j=0; j<map.getPlayers().get(i).getAd().size();j++){
	        	
	        	coord_ad_panel = new TreeItem<>("X : "+map.getPlayers().get(i).getAd().get(j).getLocation().getLongitude()+" _ Y : "+map.getPlayers().get(i).getAd().get(j).getLocation().getLatitude()+"\n Influence : "+map.getPlayers().get(i).getAd().get(j).getInfluence());
	        	drawShapes(gc,map.getPlayers().get(i).getAd().get(j),map.getPlayers().get(i).getAd().get(j).getLocation());
	        }
	        
	        drinks = new TreeItem<>("Drinks");
	        for(int j=0; j<map.getPlayers().get(i).getDrinks().size();j++){
	        	drink_name = new TreeItem<>(""+map.getPlayers().get(i).getDrinks().get(j).getName()+" : "+map.getPlayers().get(i).getDrinks().get(j).getPrice());        	
	        }
	        
	        //player_info.setExpanded(true);
	        //stand.setExpanded(false);
	        stand.getChildren().setAll(coord_stand);
	        //ad_panel.setExpanded(false);
	        ad_panel.getChildren().setAll(coord_ad_panel);
	        //drinks.setExpanded(false);
	        drinks.getChildren().setAll(drink_name);
	        player_info.getChildren().setAll(cash, stand, ad_panel,drinks);
	        playTree.getChildren().setAll(player_info);
	        
	    }
		
			TreeTableColumn<String, String> column = new TreeTableColumn<>("Player Infos");
		    column.setPrefWidth(width_t);
		
		    column.setCellValueFactory((CellDataFeatures<String, String> p) -> new ReadOnlyStringWrapper(
		            p.getValue().getValue()));
		    playTree.setExpanded(true);
		    treeTableView.refresh();
		    //treeTableView = new TreeTableView<>(playTree);
		    treeTableView = new TreeTableView<>();
		    treeTableView.setRoot(playTree);
		    //treeTableView.getColumns().setAll(column);
		    treeTableView.getColumns().add(column);
		    //treeTableView.refresh();
		    treeTableView.setLayoutX(width+1);
		    System.out.println("ok");
		    //treeTableView.getColumns().get(0).setVisible(false); 
		    //treeTableView.getColumns().get(0).setVisible(true);   
		}
	
}
