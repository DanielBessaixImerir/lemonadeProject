$(document).ready(function(){
	//$("#connect").on('submit', createPlayer);
});

function createPlayer(){
	console.log("Trying to connect...");

	$.ajax('https://tranquil-reef-75630.herokuapp.com/players', {
		type: "POST",
		data: '{"name": "'+document.getElementById('name').value+'"}',
		contentType: 'application/json'
		}).done(function(data){
			window.location = "https://tranquil-reef-75630.herokuapp.com/static/home.html";
			console.log("Connected.");
		});;
}



