var canvas;
var context;

var quarter = Math.PI * 0.5;
var wsUri = getRootUri() + "/time";
var even;
var odd;
var lastTick = 0;

var size;

function getRootUri() {
	return "ws://"
			+ (document.location.hostname == "" ? "localhost"
					: document.location.hostname) + ":"
			+ (document.location.port == "" ? "8080" : document.location.port);
}

window.onload = function() {

	try {
		even = new Audio('assets/sound/even.mp3');
		odd = new Audio('assets/sound/odd.mp3');
	} catch (err) {

	}

	websocket = new WebSocket(wsUri);
	websocket.onmessage = function(evt) {
		onMessage(evt)
	};
}

function onMessage(evt) {
	if (canvas != document.getElementById('myCanvas')) {
		try {
			canvas = document.getElementById('myCanvas');
			context = canvas.getContext('2d');
		} catch (err) {
			console.log(err);
		}
	}
	try {
		size = Math.min(canvas.height, canvas.width);
		var tick = JSON.parse(evt.data);
		if (lastTick != tick.remaining) {
			lastTick = tick.remaining
			if (tick.remaining <= 50 && tick.total > 0) {
				if (even && isEven(tick.remaining)) {
					even.play();
				} else if (odd) {
					odd.play();
				}
			}
		}
		draw(tick);
	} catch (err) {
		console.log(err);
	}
}

function draw(tick) {
	clear();
	if (tick.total > 0) {
		drawTrack();
		drawTick(tick);
		drawText(tick);
	} else {
		drawEmptyTrack();
	}
}

function clear() {
	context.clearRect(0, 0, canvas.width, canvas.height);
}

function drawText(tick) {
	var txt;
	if (tick.remaining < 60) {
		txt = tick.remaining;
	} else {

		var minutes = Math.floor(tick.remaining / 60);
		var seconds = tick.remaining % 60;
		if (seconds < 10) {
			seconds = "0" + seconds;
		}
		txt = minutes + ':' + seconds;
	}
	// BAD BAD BAD PRACTICWE
	if (size > 450) {
		context.fillStyle = 'black';
	} else {
		context.fillStyle = 'white';
	}
	var fontsize = size / 5.5;
	context.font = fontsize + "px Lato";
	context.textAlign = 'center';
	context.textBaseline = 'middle';
	context.fillText(txt, size / 2, size / 2);
}

function drawTrack() {
	context.strokeStyle = 'rgb(190, 69, 69)';
	context.lineWidth = size / 13;
	context.beginPath();
	var diff = (size / 11 - size / 13) / 2;
	context.arc(size / 2, size / 2, (size / 2 - context.lineWidth / 2) - diff,
			0, Math.PI * 2);
	context.stroke();
}

function drawEmptyTrack() {
	context.strokeStyle = 'rgb(16, 72, 107)';
	context.lineWidth = size / 13;
	context.beginPath();
	var diff = (size / 11 - size / 13) / 2;
	context.arc(size / 2, size / 2, (size / 2 - context.lineWidth / 2) - diff,
			0, Math.PI * 2);
	context.stroke();
}

function drawTick(tick) {
	var progress = tick.remaining / tick.total
	context.strokeStyle = 'rgb(85, 200, 108)';
	context.lineWidth = size / 11;
	context.beginPath();
	var test = Math.PI * 2 / progress;
	context.arc(size / 2, size / 2, size / 2 - context.lineWidth / 2,
			0 - quarter, (Math.PI * 2 * progress) - quarter);
	context.stroke();
}

function isEven(n) {
	return isNumber(n) && (n % 2 == 0);
}

function isOdd(n) {
	return isNumber(n) && (Math.abs(n) % 2 == 1);
}

function isNumber(n) {
	return n == parseFloat(n);
}