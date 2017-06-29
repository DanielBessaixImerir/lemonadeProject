$(document).ready(function(){
	//SETUP AND NECESSARY START-UP FUNCTIONS
	setup();
//	drinks();
//	prices();
	updatePLayerList();
	updateSituation();
	checkAvailableDrinks();
	
	//EVENT LISTENERS
	//document.getElementsByName("quantity1Input")[0].addEventListener('change', drinks);
	//document.getElementsByName("quantity2Input")[0].addEventListener('change', drinks);
	//document.getElementsByName("quantity3Input")[0].addEventListener('change', drinks);
	document.getElementsByName("adSizeInput")[0].addEventListener('change', adPrice);
	$('reset').click(resetGame);
	initDrawingListeners(); //drawing on canvas listeners, to use later.

	//BACKGROUND TASKS
	setInterval(updateSituation, 2000);
	setInterval(updatePLayerList, 2000);
	setInterval(refreshFSChat, 1000);
	setInterval(updateInformations, 2000);
	setInterval(updateMap, 2000);
	//setInterval(checkAvailableDrinks, 2000); not yet
});

function setup(){
	document.getElementById("quantity1Input").value = 0;
	document.getElementById("quantity2Input").value = 0;
	document.getElementById("quantity3Input").value = 0;
	document.getElementById("price1Input").value = 0;
	document.getElementById("price2Input").value = 0;
	document.getElementById("price3Input").value = 0;
	$.ajax('https://tranquil-reef-75630.herokuapp.com/players')
		.done(function(data) {
			var currentPlayer = ""
			for (var i = 0; i < data.length; i++) {
			    currentPlayer = data[i]['name'];
			}
			document.getElementById("currentPlayer").innerHTML = currentPlayer;
		});;
}

function resetGame(){
	$.ajax('https://tranquil-reef-75630.herokuapp.com/reset')
		.done(function(data) {
			location.href = "https://tranquil-reef-75630.herokuapp.com/static/index.html";
		});;
}

//function drinks(){
//	var nbDrinks = Number(document.getElementById("quantity1Input").value) + Number(document.getElementById("quantity2Input").value) 
//					+ Number(document.getElementById("quantity3Input").value)
//	document.getElementById("drinks").innerHTML = nbDrinks.toFixed(2);
//	prices();
//}

//function prices(){
//	var sellPrice = (Number(document.getElementById("price1Input").value) * Number(document.getElementById("quantity1Input").value)) 
//					+ (Number(document.getElementById("price2Input").value) * Number(document.getElementById("quantity2Input").value)) 
//					+ (Number(document.getElementById("price3Input").value) * Number(document.getElementById("quantity3Input").value))
//	document.getElementById("cost").innerHTML = sellPrice.toFixed(2);
//}

function adPrice(){
	var nbDrinks = Number(document.getElementById("adSize").value) * 4;//4 = arbitrary price
//	document.getElementById("adSize").innerHTML = nbDrinks;
}

//get metrology
function updateSituation(){
	$.ajax('https://tranquil-reef-75630.herokuapp.com/metrology')
		.done(function(data) {
			document.getElementById("day").innerHTML = parseInt(data['timestamp']/24);
			document.getElementById("hour").innerHTML = data['timestamp'] % 24;
			document.getElementById("weather").innerHTML = data['weather'][0]['weather'];
			document.getElementById("forecast").innerHTML = data['weather'][1]['weather'];
		});;
}

//UPDATE MAP
function updateMap(){
	var mapCanvas =document.getElementById('mapCanvas');
	var ctx = mapCanvas.getContext("2d");
	var url = "https://tranquil-reef-75630.herokuapp.com/map";
	var rect = document.getElementById('mapCanvas').getBoundingClientRect();

	$.ajax(url)
		.done(function(data) {
			ctx.clearRect(0,0,mapCanvas.width, mapCanvas.height);
			itemList = data['itemsByPlayer'];
			for (var items in itemList) {
				for(var i = 0; i<itemList[items].length; i++){
					var longitude = itemList[items][i]['location']['longitude'];
					var latitude = itemList[items][i]['location']['latitude'];
					var longitude_span = data['region']['span']['longitudeSpan'];
					var latitude_span = data['region']['span']['latitudeSpan'];
					var longitude_center = data['region']['center']['longitude'];
					var latitude_center = data['region']['center']['latitude'];
					var radius = itemList[items][i]['influence'];
					ctx.beginPath();
					ctx.lineWidth = 1;
					ctx.strokeStyle = '#32CD32';
					console.log(longitude);
					console.log(latitude);
					//var x = (itemList[items][i]['location']['longitude'] * rect.width) / $(document).width();
					//var y = (itemList[items][i]['location']['latitude'] * rect.height) / $(document).height();
					//var x = (744 * (longitude-(longitude_center - (longitude_span/2))))/longitude_span
					//var y = (482 * (latitude-(latitude_center - (latitude_span/2))))/latitude_span
					var x = latitude
					var y = longitude
					console.log(x);
					console.log(y);
					ctx.arc(x,y,radius,0,2*Math.PI);
					ctx.stroke();
				}
			}
		});;
}

//DRINKS AND PROD COST
function updateInformations(){
	var currentPlayer = document.getElementById("currentPlayer").innerHTML;
	var url = "https://tranquil-reef-75630.herokuapp.com/map/" + currentPlayer;
	$.ajax(url)
		.done(function(data) {
			//document.getElementById("drinks").innerHTML = data['nbDrinks']
			//document.getElementById("cost").innerHTML = data['prodCost']
			document.getElementById("budget").innerHTML = data['playerInfo']['cash']
			document.getElementById("sold").innerHTML = data['playerInfo']['sales']
		});;
}

function updatePLayerList(){
	$.ajax('https://tranquil-reef-75630.herokuapp.com/players')
		.done(function(data) {
			var playerList = ""
			for (var i = 0; i < data.length; i++) {
			    playerList += data[i]['name'] + " ["+ data[i]['budget'] +"€]"+" ; ";
			}
			document.getElementById("playerList").innerHTML = playerList;
		});;
}

function refreshFSChat(){
//	var currentPlayer = ""
//	$.ajax('https://tranquil-reef-75630.herokuapp.com/currentPlayer')
//		.done(function(data) {
//			if (data =="")
//				currentPlayer = "Invited";
//			else:
//				currentlayer = data;
//		});;
	$.ajax('https://tranquil-reef-75630.herokuapp.com/chat')
		.done(function(data) {
			var chat = ""
			document.getElementById("chat").innerHTML = "";
			for (var i = 0; i < data.length; i++) {
			    chat = data[i]['text'];
				document.getElementById("chat").innerHTML += chat;
				document.getElementById("chat").innerHTML += "<br>";
				document.getElementById("chat").innerHTML += "---------------------------------";
				document.getElementById("chat").innerHTML += "<br>";
			}
			var objDiv = document.getElementById("chat");
			objDiv.scrollTop = objDiv.scrollHeight;
		});;
}

function applyDrinks(n){
	var selector = document.getElementById("drinkSelect" + Number(n))
	var selectedRecipe = selector.value;

	var numberInput = document.getElementById("quantity"+Number(n)+"Input");
	var number = numberInput.value;

	var priceInput = document.getElementById("price"+Number(n)+"Input");
	var drinkPrice = priceInput.value;

	var currentPlayer = document.getElementById("currentPlayer").innerHTML;
	var url = "https://tranquil-reef-75630.herokuapp.com/actions/" + currentPlayer;
	
	$.ajax(url, {
		type: "POST",
		data: '{"kind":"drinks", "prepare":{"name":"'+ selectedRecipe +'", "number":'+number+'}, "price":{"name":"'+selectedRecipe+'","price":'+drinkPrice+'}}',
		contentType: 'application/json'
		})
		.done(function(data){
			if(data['sufficientFunds'] == "true"){
				alert("Applied for tommorow!");
			}else{alert("Insuficient funds!");}
		});;	
}

function checkAvailableDrinks(){
//	var currentPlayer = document.getElementById("currentPlayer").innerHTML;
//	var url = "https://tranquil-reef-75630.herokuapp.com/availableDrinks/" + currentPlayer;
	$.ajax('https://tranquil-reef-75630.herokuapp.com/checkDrinks')
		.done(function(data) {
			var option = '';
			for (var i=0;i<data.length;i++){
				var drink = data[i]['drink_name'];
			   option += '<option value="'+ data[i]['drink_name'] + '">' + data[i]['drink_name'] + '</option>';
			}
			$('#drinkSelect1').append(option);
			$('#drinkSelect2').append(option);
			$('#drinkSelect3').append(option);
		});;
}

function applyAdPanels(){
	var latitude = document.getElementById("adLatitudeInput").value;
	var longitude = document.getElementById("adLongitudeInput").value;
	var radius = document.getElementById("adSizeInput").value;
	var currentPlayer = document.getElementById("currentPlayer").innerHTML;
	var url = "https://tranquil-reef-75630.herokuapp.com/actions/" + currentPlayer;

	$.ajax(url, {
		type: "POST",
		data: '{"kind":"ad", "location":{"latitude":"'+ latitude +'", "longitude":"'+ longitude +'"}, "radius":"'+ radius +'"}',
		contentType: 'application/json'
		})
		.done(function(data){
			if(data['sufficientFunds'] == "true"){
				alert("Applied for tommorow!");
				refreshInformations(currentPlayer);
			}else{alert("Insuficient funds!");}
		});;	
}

function getMapItems(){
//	var rect = document.getElementById("mapCanvas").getBoundingClientRect();
//	var x = coordX + rect.left
//	var y = coordY + rect.top
}

function postMessageOnChat(){
	$.ajax('https://tranquil-reef-75630.herokuapp.com/chat/post', {
		type: "POST",
		data: '{"text": "'+$('#message').val()+'"}',
		contentType: 'application/json'
		})
		.done(function(data){
			document.getElementById('message').value = ''
		});;
}

function initDrawingListeners(){
	var mapCanvas = document.getElementById("mapCanvas");
	var ctx = mapCanvas.getContext('2d');
	var circle = {};
	var drag = false;


	//MOUSE DOWN EVENT
	mapCanvas.addEventListener('mousedown',function (e){
		circle.startX = e.pageX - mapCanvas.offsetLeft;
	    circle.startY = e.pageY - mapCanvas.offsetTop;
	
	    circle.X = circle.startX;
	    circle.Y = circle.startY;
	
    	circle.radius = 0;
	
    	drag = true;
		document.getElementById("mouseEvent").innerHTML = drag.toString();
		}, true);
	//MOUSE MOVE EVENT
	mapCanvas.addEventListener('mousemove', function (e){
		if (drag) {
	      circle.X = e.pageX - mapCanvas.offsetLeft;
	      circle.Y = e.pageY - mapCanvas.offsetTop;
	      circle.radius = Math.sqrt(Math.pow((circle.X - circle.startX), 2) + Math.pow((circle.Y - circle.startY), 2));
	      ctx.clearRect(0, 0, canvas.width, canvas.height);
	      draw(e);
	    }
		var rect = document.getElementById("mapCanvas").getBoundingClientRect();
		var x = (e.clientX - rect.left)
		var y = (e.clientY - rect.top)
		document.getElementById("latitudeSpan").innerHTML = y.toFixed(2);
		document.getElementById("longitudeSpan").innerHTML = x.toFixed(2);

		}, true);
	//MOUSE UP EVENT
	mapCanvas.addEventListener('mouseup', function (){
		drag = false;
		document.getElementById("mouseEvent").innerHTML = drag.toString();
		}, true);
}

function draw(e){
	e.beginPath(e);
    e.arc(circle.X, circle.Y, circle.radius, 0, 2.0 * Math.PI);
    e.stroke();
	document.getElementById("mouseEvent").innerHTML = 'drawing';
}
