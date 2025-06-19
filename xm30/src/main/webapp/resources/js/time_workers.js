
var timestamp;
var timeOffset = 3000;

function timedWork() {
	timestamp = new Date();

	postMessage(timestamp);
	setTimeout("timedWork()", timeOffset);
}

timedWork();
