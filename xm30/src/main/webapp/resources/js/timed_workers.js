var longtime;

function timedStatus() {
	//console.log("called timedStatus()!!!");
	longtime = new Date().getTime();

	postMessage(longtime);
	setTimeout("timedStatus()", 3000);
}

timedStatus();
