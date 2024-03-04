

var hello = {};

hello.debug = true;


hello.xlog = function(msg) {
	if (hello.debug) {
		console.log(msg);
	}
};

hello.xdir = function(obj) {
	if (hello.debug) {
		console.dir(obj);
	}
};


hello.httpGetAsync = function(url, callback) {
	var xhr = new XMLHttpRequest();


	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {

			var timdstamp_e = new Date();
			hello.xlog("END[" + timdstamp_e + "]");
			xhr.timestamp_e = timdstamp_e.getTime();


			hello.xlog("ELAPSED_TIME [" + (xhr.timestamp_e - xhr.timestamp_s) + "]");
			callback(xhr.response, event);
		}
	}


	xhr.open('GET', url, true); // true for asynchronous

	xhr.setRequestHeader("Accept", "application/json");
	xhr.setRequestHeader("Content-Type", "application/json");

	xhr.responseType = "json";

	// GET
	xhr.send(null);

	var timestamp_s = new Date();
	hello.xlog("START[" + timestamp_s + "]");
	hello.xlog("START[" + timestamp_s.getTime() + "]");
	hello.xdir(timestamp_s);

	xhr.timestamp_s = timestamp_s.getTime();
	hello.xdir(xhr);
	// POST
	//xhr.send(JSON.stringify(jsonData)); // if 500 error
};