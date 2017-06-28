#!/usr/bin/env python2.7
# coding: utf-8

from flask import Flask, request
from flask_cors import CORS
import json
import random
import psycopg2
from db import Db

app = Flask(__name__)
app.debug = True
CORS(app)

################################################################################
##### Quelques constantes

COST_PER_GLASS  = 0.15 # le cout de production
PRICE_PER_GLASS = 0.35 # le prix de vente
HOUR_OF_DAY = 0
WEATHER_OF_DAY = ""
FORECAST_OF_DAY = "None"

# les conditions meteo
WEATHER_VALUES = ["SUNNY AND HOT", "SUNNY", "CLOUDY", "RAINY"]

# la probabilite maximale (entre 0 et 1) de vente pour chaque condition meteo.
SALES_MAX = {
  "SUNNY AND HOT" : 1.0, 
  "SUNNY"         : 0.8,
  "CLOUDY"        : 0.5,
  "RAINY"         : 0.1
}

# la probabilite minimale (entre 0 et 1) de vente pour chaque condition meteo.
SALES_MIN = {
  "SUNNY AND HOT" : 0.6, 
  "SUNNY"         : 0.2,
  "CLOUDY"        : 0.0,
  "RAINY"         : 0.0
}

################################################################################
chat = []
################################################################################
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@app.route('/')
def homePage():
	return "Bonjour je suis une page de garde."

#-------------------------PLAYERS INFOS-------------------------
@app.route('/players', methods=['GET'])
def getPlayers():
	db = Db()
	nbPlayers = db.select("SELECT COUNT(*) FROM player")[0]["count"]
	playerList = db.select("SELECT * FROM player ORDER BY player_budget DESC;")
	
	player={"name":"", "budget":""}
	players = []
	for i in range(0, nbPlayers):
		players.append({"name": playerList[i]["player_name"], "budget": playerList[i]["player_budget"]})

	db.close()

	return json.dumps(players), 200, {'Content-Type': 'application/json'}

@app.route('/players', methods=['POST'])
def createPlayer():
	db = Db()
	name = request.get_json()['name']
	player = {"name":"", "location":{"x":"", "y":""}}
	player['location']['y'] = random.randint(0, 475)
	player['location']['x'] = random.randint(0, 730)
	player['name'] = name
	randx = player['location']['y']
	randy = player['location']['x']
	strRandX = str(randx)
	strRandY = str(randy)
	print "THE GENERATED LOCATION : " + strRandX + "," + strRandY
	db.execute("""
				INSERT INTO Player(player_name, player_budget) VALUES(@(name), @(budget));
				INSERT INTO Stand(player_id, stand_influence) VALUES((SELECT player_id FROM Player WHERE player_name= @(name)), @(influence));
				INSERT INTO Coordinates(longitude,latitude,stand_id) VALUES(@(strRandX),@(strRandY),(SELECT stand_id FROM Stand 					WHERE player_id=(SELECT player_id FROM Player WHERE player_name= @(name))));
				UPDATE Player SET stand_id=(SELECT stand_id FROM Stand WHERE player_id=(SELECT player_id FROM Player 
				WHERE player_name= @(name))) WHERE player_id=(SELECT player_id FROM Player WHERE player_name= @(name));""", 
	{
				'name': name,'budget':10, 'influence':100, 'strRandX': strRandX, 'strRandY': strRandY
	})
	db.close()

	return json.dumps(player), 200, {'Content-Type': 'application/json'}

#-------------------------ARDUINO(weather/hour) INFOS---------------------------
#GET THE TIME & WEATHER INFORMATIONS
@app.route('/metrology', methods=['GET'])
def getMetrology():
	db = Db()
	currentHour = db.select("SELECT current_hour FROM current_day")[0]['current_hour']
	print currentHour
	currentWeather = db.select("SELECT current_weather FROM current_day")[0]['current_weather']
	currentForecast = db.select("SELECT current_forecast FROM current_day")[0]['current_forecast']
	db.close()
	
	situation = {"timestamp":"", "weather":[{"dfn":"0", "weather":""},{"dfn":"1","weather":""}]}
	situation['timestamp'] = currentHour
	situation['weather'][0]['weather'] = currentWeather
	situation['weather'][1]['weather'] = currentForecast

	return json.dumps(situation), 200, {'Content-Type': 'application/json'}

#POST THE TIME & WEATHER INFORMATIONS
@app.route('/metrology', methods=['POST'])
def postMetrology():
	jsonSituation = request.get_json()
	print "METROLOGY : " + str(jsonSituation)
	insertedHour = ""
	insertedWeather = ""
	insertedForecast = ""
	
	insertedHour = jsonSituation['timestamp']
	strInsertedHour = str(insertedHour)

	if jsonSituation['weather'][0]['dfn'] == 0:
		insertedWeather = jsonSituation['weather'][0]['weather']
		insertedForecast = jsonSituation['weather'][1]['weather']
	if jsonSituation['weather'][0]['dfn'] == 1:
		insertedWeather = jsonSituation['weather'][1]['weather']
		insertedForecast = jsonSituation['weather'][0]['weather']

	db = Db()

	db.execute("""
						UPDATE current_day SET current_hour=@(insertedHour) WHERE day_name = 'today'; 
						UPDATE current_day 
						SET current_forecast=@(insertedForecast) WHERE day_name = 'today';
						UPDATE current_day 
						SET current_weather=@(insertedWeather) WHERE day_name = 'today';""", 
	{
						'insertedHour': strInsertedHour,'insertedForecast': insertedForecast, 'insertedWeather': insertedWeather
	})

	db.close()

	return json.dumps(jsonSituation), 200, {'Content-Type': 'application/json'}

#-------------------------JAVAS INFOS---------------------------
#POST SALES INFORMATIONS
@app.route('/sales', methods = ['POST'])
def postSales():
    jsonSale = request.get_json()

    jsonReturn = {'sales':0,'stock':0}

    name = jsonSale['player']
    item = jsonSale['item']
    quantity = jsonSale['quantity']
    
    db=Db()

    today = int(db.select("SELECT current_hour FROM current_day WHERE day_name = 'today'")[0]['current_hour']/24)
    standId = db.select("SELECT stand_id FROM Player WHERE player_name= '"+name+"'")[0]['stand_id']
    drinkId = db.select("SELECT drink_id FROM drink WHERE drink_name='"+ item +"'")[0]['drink_id']
	
    stockId=db.select("""
							SELECT stock_id FROM stock
							WHERE day_value=@(today)
							AND stand_id=@(standId)
							AND drink_id=@(drinkId);
    """, { 'today':today, 'standId':standId, 'drinkId':drinkId })[0]['stock_id']

    stockList = db.select("SELECT * FROM stock WHERE stand_id="+ str(standId) +" AND day_value="+ str(today) +";")
    for drink in stockList:
        jsonReturn['stock'] += drink['stock_quantity_before']

    db.execute("""
                    UPDATE stock SET stock_sales=@(salestot)
                    WHERE stock_id = @(stockId);""",
					{'salestot': str(quantity), 'stockId': str(stockId)})
    salesList = db.select("SELECT * FROM stock WHERE stand_id="+ str(standId) +" AND day_value="+ str(today) +";")
    for sales in stockList:
        jsonReturn['sales'] += sales['stock_sales']	

    db.close()

    return json.dumps(jsonReturn), 200, {'Content-Type': 'application/json'}

#-------------------------WEB INFOS---------------------------
@app.route('/reset', methods=['GET'])
def resetGame():
	db = Db()

	db.execute("DELETE FROM ad_panel WHERE panel_id>0;")
	db.execute("DELETE FROM coordinates;")
	db.execute(""" 
					UPDATE current_day SET current_hour=0 WHERE day_name='today';
					UPDATE current_day SET current_weather='Not yet' WHERE day_name='today';
					UPDATE current_day SET current_forecast='Not yet' WHERE day_name='today';
	""")
	db.execute("DELETE FROM drink WHERE drink_id>0;")
	db.execute("""
					INSERT INTO drink(drink_name, drink_type_hasalcool, drink_type_cold, drink_production_cost)
					VALUES ('limonade', false, true, 0.15);
					INSERT INTO drink(drink_name, drink_type_hasalcool, drink_type_cold, drink_production_cost)
					VALUES ('the', false, false, 0.3);
					INSERT INTO drink(drink_name, drink_type_hasalcool, drink_type_cold, drink_production_cost)
					VALUES ('Cocktail', true, true, 0.5);	
	""")
	db.execute("DELETE FROM ingredient WHERE ingredient_id>0;")
	db.execute("""
				INSERT INTO ingredient(ingredient_name, ingredient_cost, hasalcohol, iscold)
				VALUES ('Water', 0, false, true);
				INSERT INTO ingredient(ingredient_name, ingredient_cost, hasalcohol, iscold)
				VALUES ('Lemon', 0.05, false, true);
				INSERT INTO ingredient(ingredient_name, ingredient_cost, hasalcohol, iscold)
				VALUES ('Tea Leaves', 0.15, false, false);
				INSERT INTO ingredient(ingredient_name, ingredient_cost, hasalcohol, iscold)
				VALUES ('Alcohol', 0.2, true, true);
				INSERT INTO ingredient(ingredient_name, ingredient_cost, hasalcohol, iscold)
				VALUES ('Fruit Mix', 0.25, false, true);
				INSERT INTO ingredient(ingredient_name, ingredient_cost, hasalcohol, iscold)
				VALUES ('Sugar', 0.02, false, false);
	""")
	db.execute("DELETE FROM player WHERE player_id>0;")
	db.execute("DELETE FROM stand WHERE stand_id>0;")
	db.execute("DELETE FROM stock WHERE stock_id>0;")

	db.close()

	return "Game reseted"

#THE ACTIONS A PLAYER AN DO : PRODUCE DRINKS & PLACE AD SIGNS
@app.route('/actions/<string:playername>', methods=['POST'])
def postActions(playername):
	#PlayerActionAd{"kind":"ad", "location":coordinates,"radius":float}
	#Coordinates{"latitude":float, "longitude":float}
	print "THE PLAYERNAME IS " + playername
	action = request.get_json()

	db = Db()
	nextDay = int(db.select("SELECT current_hour FROM current_day WHERE day_name = 'today'")[0]['current_hour']/24) + 1
	playerId = db.select("SELECT player_id FROM player WHERE player_name = '"+ playername +"'")[0]['player_id']
	standId = db.select("SELECT stand_id FROM player WHERE player_name = '"+ playername +"'")[0]['stand_id']
	playerBudget = db.select("SELECT player_budget FROM player WHERE player_name = '"+ playername +"'")[0]['player_budget']
	
	if action['kind'] == 'drinks':
		drinkId = db.select("SELECT drink_id FROM drink WHERE drink_name = '"+ action['prepare']['name'] +"'")[0]['drink_id']
		nbDrinks = action['prepare']['number']
		drinkPrice = action['price']['price']

		drinkProductionCost = 0
		totalProductionCost = 0		
		if action['prepare']['name'] == 'limonade':
			drinkProductionCost = 0.15
			totalProductionCost = nbDrinks * 0.15
		if action['prepare']['name'] == 'the':
			drinkProductionCost = 0.3
			totalProductionCost = nbDrinks * 0.3
		if action['prepare']['name'] == 'Cocktail':
			drinkProductionCost = 0.5
			totalProductionCost = nbDrinks * 0.5


		if (playerBudget - totalProductionCost) >= 0:
			print "THE DRINK ID IS " + str(drinkId)
			stockCount = db.select("SELECT COUNT(*) FROM stock WHERE stand_id="+ str(standId) +" AND day_value="+ str(nextDay) +" AND drink_id="+ str(drinkId) +";")[0]['count']
			newBudget = playerBudget
			if stockCount > 0:
				stockQuantity = db.select("SELECT stock_quantity_before FROM stock WHERE stand_id="+ str(standId) +" AND day_value="+ str(nextDay) +" AND drink_id="+ str(drinkId) +";")[0]['stock_quantity_before']
				print playerBudget
				print stockQuantity
				print drinkProductionCost
				print str(playerId)
				newBudget = playerBudget+(stockQuantity*drinkProductionCost)
				db.execute("UPDATE player SET player_budget="+ str(newBudget) +" WHERE player_id="+ str(playerId)+";")#refund
				db.execute("DELETE FROM stock WHERE drink_id="+ str(drinkId) +" AND day_value="+ str(nextDay) +";")#delete
			db.execute("""
				INSERT INTO Stock(day_value,stand_id,drink_id,stock_quantity_before,stock_drink_cost)
	            VALUES (@(current_day_id),@(stand_id),@(drink_id),@(stock_quantity_before),@(stock_drink_cost));""", 
			{
				'current_day_id': nextDay,'stand_id':standId,'drink_id': drinkId,'stock_quantity_before':nbDrinks,'stock_drink_cost':drinkPrice
			})#put new production

			db.execute("UPDATE player SET player_budget="+ str(newBudget - totalProductionCost) +" WHERE stand_id="+ str(standId) +";")
			actionReturn = {'sufficientFunds':"true", 'totalCost': totalProductionCost}
		else:
			actionReturn = {'sufficientFunds':"false", 'totalCost': totalProductionCost}
	
	if action['kind'] == 'ad':
		latitude = action['location']['latitude']
		longitude = action['location']['longitude']
		radius = action['radius']
		price = float(action['radius'])/10
		if (playerBudget - price) >= 0:
			db.execute("""
							INSERT INTO ad_panel(panel_influence, player_id, ad_panel_day, longitude, latitude)
							VALUES (@(radius),@(playerId), @(nextDay), @(longitude), @(latitude))
			""",{ 'radius':str(radius),'playerId': str(playerId), 'nextDay':str(nextDay), 'longitude':longitude, 'latitude':latitude })

			db.execute("UPDATE player SET player_budget="+ str(playerBudget - price) +" WHERE player_id="+ str(playerId) +"")
			
			actionReturn = {'sufficientFunds':"true", 'totalCost': price}
		else:
			actionReturn = {'sufficientFunds':"false", 'totalCost': price}

	print actionReturn
	return json.dumps(actionReturn), 200, {'Content-Type': 'application/json'}

#GET A PLAYER'S INFORMATIONS AND GENERAL MAP INFORMATIONS
@app.route('/map/<string:playername>', methods=['GET'])
def getInformations(playername):
	db = Db()
	informationsReturn = {'availableIngredients':[],'map':{'region':{'coordinates':{'latitude':0, 'longitude':0},'span':{'latitudeSpan':0, 'longitudeSpan':0}},'ranking':[], 'itemsByPlayer':{}}, 'playerInfo':{'cash':0,'sales':0,'profit':0,'drinksOffered':[]}}
	
	today = int(db.select("SELECT current_hour FROM current_day WHERE day_name = 'today'")[0]['current_hour']/24)
	playerExists = db.select("SELECT COUNT(*) FROM player WHERE player_name='"+ playername +"'")[0]['count']
	if playerExists >= 1:

		informationsReturn['map']['region']['coordinates']['latitude'] = db.select("SELECT latitude FROM region WHERE region_name = 1")[0]['latitude']
		informationsReturn['map']['region']['coordinates']['longitude'] = db.select("SELECT longitude FROM region WHERE region_name = 1")[0]['longitude']
		informationsReturn['map']['region']['span']['latitudeSpan'] = db.select("SELECT latitude_span FROM region WHERE region_name = 1")[0]['latitude_span']
		informationsReturn['map']['region']['span']['longitudeSpan'] = db.select("SELECT longitude_span FROM region WHERE region_name = 1")[0]['longitude_span']

		playerList = db.select("SELECT * FROM player ORDER BY player_budget DESC;")
		for checked in playerList:
			player = {'kind':'stand', 'location':{'longitude':0, 'latitude':0}, 'influence':0}
			owner = checked['player_name']
			standId = db.select("SELECT stand_id FROM player WHERE player_name = '"+ owner +"';")[0]['stand_id']
			player['location']['latitude'] = db.select("SELECT latitude FROM coordinates WHERE stand_id = "+ str(standId) +";")[0]['latitude']
			player['location']['longitude'] = db.select("SELECT longitude FROM coordinates WHERE stand_id = "+ str(standId) +";")[0]['longitude']
			player['influence'] = db.select("SELECT stand_influence FROM stand WHERE stand_id = "+ str(standId) +";")[0]['stand_influence']
			informationsReturn['map']['itemsByPlayer'][owner] = player
			informationsReturn['map']['ranking'].append(checked['player_name'])
		
		limonade = {'name':'limonade', 'price':0.15, 'hasAlcohol': False, 'isCold': True}
		the = {'name':'the', 'price':0.3, 'hasAlcohol': False, 'isCold': False}
		Cocktail = {'name':'Cocktail', 'price':0.5, 'hasAlcohol': True, 'isCold': True}
	
		informationsReturn['playerInfo']['cash'] = db.select("SELECT player_budget FROM player WHERE player_name = '"+ checked['player_name'] +"';")[0]['player_budget']
		print "THE PLAYERNAME ASKED IS " + playername
		playerStand = db.select("SELECT stand_id FROM player WHERE player_name = '"+ playername +"';")[0]['stand_id']

		salesList = db.select("SELECT * FROM stock WHERE stand_id="+ str(playerStand) +" AND day_value="+ str(today) +";")		
		for sales in salesList:
			informationsReturn['playerInfo']['sales'] += sales['stock_sales']
    
		stockList = db.select("SELECT * FROM stock;")
		for stock in stockList:
			informationsReturn['playerInfo']['profit'] += stock['stock_sales'] * stock['stock_drink_cost']
	
		informationsReturn['playerInfo']['drinksOffered'].append(limonade)
		informationsReturn['playerInfo']['drinksOffered'].append(the)
		informationsReturn['playerInfo']['drinksOffered'].append(Cocktail)
	
		db.close()
	else:
		return json.dumps("Inexistant Player."), 200, {'Content-Type': 'application/json'}
	
	return json.dumps(informationsReturn), 200, {'Content-Type': 'application/json'}

#GET MAP INFORMATIONS
@app.route('/map', methods =['GET'])
def getMap():
	db = Db()

	rMap = {'region':{'center':{'latitude':0, 'longitude':0},'span':{'latitudeSpan':0, 'longitudeSpan':0}},'ranking':[],'itemsByPlayer':{}, 'playerInfo':{}, 'drinksByPlayer':{}}
	
	region = db.select("SELECT * FROM region WHERE region_name='1'")[0]
	rMap['region']['center']['latitude'] = region['latitude']
	rMap['region']['center']['longitude'] = region['longitude']
	rMap['region']['span']['latitudeSpan'] = region['latitude_span']
	rMap['region']['span']['longitudeSpan'] = region['longitude_span']

	limonade = {'name':'limonade', 'price':0.15, 'hasAlcohol': False, 'isCold': True}
	the = {'name':'the', 'price':0.3, 'hasAlcohol': False, 'isCold': False}
	Cocktail = {'name':'Cocktail', 'price':0.5, 'hasAlcohol': True, 'isCold': True}

	today = int(db.select("SELECT current_hour FROM current_day WHERE day_name = 'today'")[0]['current_hour']/24)

	playerList = db.select("SELECT * FROM player ORDER BY player_budget DESC;")
	for checked in playerList:
		player = {'kind':'stand','owner':"", 'location':{'longitude':0, 'latitude':0}, 'influence':0}
		owner = checked['player_name']
		playerId = db.select("SELECT player_id FROM player WHERE player_name = '"+ owner +"';")[0]['player_id']
		standId = db.select("SELECT stand_id FROM player WHERE player_name = '"+ owner +"';")[0]['stand_id']
		player['owner'] = owner		
		player['location']['latitude'] = db.select("SELECT latitude FROM coordinates WHERE stand_id = "+ str(standId) +";")[0]['latitude']
		player['location']['longitude'] = db.select("SELECT longitude FROM coordinates WHERE stand_id = "+ str(standId) +";")[0]['longitude']
		player['influence'] = db.select("SELECT stand_influence FROM stand WHERE stand_id = "+ str(standId) +";")[0]['stand_influence']
		
		playerItem = []
		playerItem.append(player)
		
		adPanelList = db.select("SELECT * FROM ad_panel WHERE player_id="+ str(playerId) +" AND ad_panel_day="+ str(today) +"")
		for cPanel in adPanelList:
			aPanel = {'kind':'ad','owner':"", 'location':{'longitude':0, 'latitude':0}, 'influence':0}
			aPanel['owner'] = owner					
			aPanel['location']['latitude'] = db.select("SELECT latitude FROM ad_panel WHERE player_id = "+ str(playerId) +" AND ad_panel_day="+str(today)+";")[0]['latitude']
			aPanel['location']['longitude'] = db.select("SELECT longitude FROM ad_panel WHERE player_id = "+ str(playerId) +" AND ad_panel_day="+str(today)+";")[0]['longitude']
			aPanel['influence'] = db.select("SELECT panel_influence FROM ad_panel WHERE player_id = "+ str(playerId) +" AND ad_panel_day="+str(today)+";")[0]['panel_influence']
			playerItem.append(aPanel)

		rMap['itemsByPlayer'][owner] = playerItem
		rMap['ranking'].append(checked['player_name'])

		rMap['playerInfo'][owner] = {'cash':0, 'sales':0, 'profit':0, 'drinksOffered':[]}
		rMap['playerInfo'][owner]['cash'] = db.select("SELECT player_budget FROM player WHERE player_name = '"+ owner +"';")[0]['player_budget']
		playerStand = db.select("SELECT stand_id FROM player WHERE player_name = '"+ owner +"';")[0]['stand_id']
		rMap['playerInfo'][owner]['sales'] = db.select("SELECT stand_sell FROM stand WHERE stand_id="+ str(standId) +";")[0]['stand_sell']

		stockList = db.select("SELECT * FROM stock;")
		for stock in stockList:
			rMap['playerInfo'][owner]['profit'] += stock['stock_sales'] * stock['stock_drink_cost']

		todaysDrink = db.select("SELECT * FROM stock WHERE stand_id="+ str(standId) +" AND day_value = "+ str(today) +"")
		drinkTable = []
		for cDrink in todaysDrink:
			aDrink = {'name':"",'price':0, 'hasAlcohol':False, 'isCold':False}
			aDrink['name'] = db.select("SELECT drink_name FROM drink WHERE drink_id="+ str(cDrink['drink_id']) +"")[0]
			aDrink['price'] = cDrink['stock_drink_cost']
			aDrink['hasAlcohol'] = db.select("SELECT drink_type_hasalcool FROM drink WHERE drink_id="+ str(cDrink['drink_id']) +"")[0]
			aDrink['isCold'] = db.select("SELECT drink_type_cold FROM drink WHERE drink_id="+ str(cDrink['drink_id']) +"")[0]
			aDrink2 = {'name':"",'price':0, 'hasAlcohol':False, 'isCold':False}
			aDrink2['name'] = db.select("SELECT drink_name FROM drink WHERE drink_id="+ str(cDrink['drink_id']) +"")[0]['drink_name']
			aDrink2['price'] = cDrink['stock_drink_cost']
			aDrink2['hasAlcohol'] = db.select("SELECT drink_type_hasalcool FROM drink WHERE drink_id="+ str(cDrink['drink_id']) +"")[0]['drink_type_hasalcool']
			aDrink2['isCold'] = db.select("SELECT drink_type_cold FROM drink WHERE drink_id="+ str(cDrink['drink_id']) +"")[0]['drink_type_cold']
			drinkTable.append(aDrink2)
			rMap['playerInfo'][owner]['drinksOffered'].append(aDrink2)
		rMap['drinksByPlayer'][owner] = drinkTable
		
	db.close()
	
	return json.dumps(rMap), 200, {'Content-Type': 'application/json'}

#RETURNS AN ARRAY OF INGREDIENTS
@app.route('/ingredients', methods=['GET'])
def getIngredients():
	jsonIngredient = {'ingredients':[]}
	ingredientTable = []
	
	db = Db()
	ingredientsList = db.select("SELECT * FROM ingredient;")

	for checkedIngredient in ingredientsList:
		print checkedIngredient
		ingredient = {'name':checkedIngredient['ingredient_name'],'cost':checkedIngredient['ingredient_cost'],'hasAlcohol':checkedIngredient['hasalcohol'],'isCold':checkedIngredient['iscold']}
		ingredientTable.append(ingredient)

	jsonIngredient['ingredients'] = ingredientTable

	db.close()

	return json.dumps(jsonIngredient), 200, {'Content-Type': 'application/json'}

#DECORATES THE AVAILABLE DRINKS DROPDOWN BOXES [TO USE ONCE CREATING DRINKS IS AVAILABLE]
@app.route('/checkDrinks', methods=['GET']) #<string:playername> FOR LATER (SEE COMMENT BELOW)
def getAvailableDrinks():
	db = Db()
	drinksList = db.select("SELECT drink_name FROM drink") #THE DB DOESN'T TAKE IN ACCOUNT WHICH PLAYER THEY BELONG TO YET
	nbDrinks = int(db.select("SELECT COUNT(*) FROM drink;")[0]['count'])
	
	return json.dumps(drinksList), 200, {'Content-Type': 'application/json'}

#-------------------------BONUS----------------------
@app.route('/chat/post', methods=['POST'])
def postMessage():
	global chat
	message = {"text":""}
	message['text'] = request.get_json()['text']
	chat.append(message)
	return "Message sent"

#clears the chat entirely
@app.route('/chat/delete')
def deleteChat():
	global chat
	chat = ""
	return "Chat Cleared"

@app.route('/chat', methods=['GET'])
def refreshChat():
	global chat
	return json.dumps(chat), 200, {'Content-Type': 'application/json'}

#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
if __name__ == "__main__":
  app.run()
