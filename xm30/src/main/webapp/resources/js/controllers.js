

var productsApp = angular.module('productsApp', []);

productsApp.controller('productsCtrl', function($scope, $http) {
	$scope.initProducts = function(uri) {
		$scope.refreshProducts(uri);
	};

	$scope.refreshProducts = function(uri) {
		$http.get(uri).success(function(data) {
			$scope.products = data;
		});
	};
	
	var selectedElement = null;
	$scope.ngClickItem = function(obj, actionUri) {
		//console.log(product);
		// {PRICE: 652, QTY_ON_HAND: 3, DESCRIPTION: 'Handle', MFR_ID: 'BIC', PRODUCT_ID: '41003', …}
		
		if(selectedElement!=null) {
			selectedElement.bgColor = '';
		}
		
		selectedElement = document.getElementById(obj.MFR_ID + "-" + obj.PRODUCT_ID);
		if(selectedElement!=null) {
			selectedElement.bgColor = 'chartreuse';
		}
		
		const xhr = callProduct(obj.MFR_ID, obj.PRODUCT_ID, actionUri);
		xhr["MFR_ID"] = obj.MFR_ID;
		xhr["PRODUCT_ID"] = obj.PRODUCT_ID;
		if(xhr) { 
			const k = xhr.MFR_ID + "-" + xhr.PRODUCT_ID + "@" + xhr.timestamp_s;
			normalMap.set(k, xhr);
			//console.log("SET:" + k + " NOR_SIZE: " + normalMap.size); 

			const pkey = xhr.MFR_ID + "-" + xhr.PRODUCT_ID;
			let actMap = pActMap.get(pkey);
			//console.log("actMap: " + actMap);
			
			let normalAct;
			if(typeof(actMap) !== "undefined") {
				normalAct = actMap.get("normal");
				//console.log("REUSE_ACT_MAP: " + actMap);
			} else {
				actMap = new Map();

				normalAct = new Map();
				actMap.set("normal", normalAct);

				let warnAct = new Map();
				actMap.set("warn", warnAct);

				let fatalAct = new Map();
				actMap.set("fatal", fatalAct);

				pActMap.set(pkey, actMap);
				//console.log("CREATE_NEW_ACT_MAP: " + actMap);
			} 
			// #### fire an Event
			normalAct.set(k, xhr);



/*
        <div id="veq_list" class="veq_list">
<!--
            <div id="DAYHEART_block" draggable="true" class="veq_block">
                <div class="veq">
                    <label id="DAYHEART_topl"> </label>
                    <div id="DAYHEART_viewport" class="veq_viewport">
                        <canvas id="DAYHEART_bg" width="60" height="100"></canvas>
                        <canvas id="DAYHEART_blades" width="60" height="100"></canvas>
                        <canvas id="DAYHEART_glow" width="60" height="100"></canvas>
                    </div>
                    <div class="label_container">
                        <label id="DAYHEART_bottoml">DAYHEART</label>
                    </div>
                </div>
            </div>
-->
        </div>
*/

			let veq_block = document.getElementById(pkey + "_block");
			//console.log("VEQ_BLOCK: " + veq_block ); // null
			if(veq_block == null) {
				let bg_canvas = elt("canvas", {id: pkey + "_bg", width:"60", height:"110" });
				let blades_canvas = elt("canvas", {id: pkey + "_blades", width:"60", height:"110" });
				let glow_canvas = elt("canvas", {id: pkey + "_glow", width:"60", height:"110"});
				let viewport = elt("div", {id: pkey + "_viewport", class: "veq_viewport"}, bg_canvas, blades_canvas, glow_canvas);

				let bottom_label = elt("lable", {id: pkey + "_bottoml"});
				bottom_label.textContent = pkey;

				let label_container = elt("div", { class: "label_container" }, bottom_label );

				let veq = elt("div", { class: "veq" }, viewport, label_container);

				let veq_block = elt("div", {id: pkey + "_block", draggable: "true", class: "veq_block"}, veq );

				let context = blades_canvas.getContext('2d');
				let bgContext = bg_canvas.getContext('2d');
				//console.dir(blades_canvas);
				//console.dir(context);
				let eq = new VActEQ();
				eq.x = blades_canvas.width/2;
				eq.y = blades_canvas.height;
				//eq.n = 1;
				
				eq.context = context;
				eq.bgContext = bgContext;
				veqMap.set(pkey, eq);

				veq_list.appendChild(veq_block);
				//drawVEQ(pkey + "_blaes");
			}
			let eq = veqMap.get(pkey);
			let activesMap = pActMap.get(pkey);
			let activeNormalMap = activesMap.get("normal");
			let activeWarnMap = activesMap.get("warn");
			let activeFatalMap = activesMap.get("fatal");
			eq.n = activeNormalMap.size;
			eq.w = activeWarnMap.size;
			eq.f = activeFatalMap.size;
			eq.drawBF();
			eq.draw0();

			// user event update just in time 
			updateTotalActiveValues();

			startTimedWorker();

			//let active = pActMap.get(pkey + "_blades");
			//active.draw0();

		} // end of if(xhr)
		/*
		//console.dir(xhr); 
		MFR_ID : "QSA"
		PRODUCT_ID : "XK47"
		timestamp_e : 1748706741512
		timestamp_s : 1748706740633
		var now = new Date().getTime();
		//console.log(now);
		*/
	};

});

function responseHandler(response, event) {
	//console.log(response);
	// 0: {PRICE: 79, QTYONHAND: 210, DESCRIPTION: 'Ratchet Link', MFRID: 'REI', PRODUCTID: '2A45C'}
	//console.dir(event);
	/*
	target: XMLHttpRequest {timestamp_s: 1707961016155, timestamp_e: 1707961017521, readyState: 4, timeout: 0, onreadystatechange: ƒ, …}
	timeStamp: 3529.0999999940395
	type: "readystatechange"
	*/
	
}

function callProduct(mfrId, productId, the_uri, callback=responseHandler) {
	var xhr = new XMLHttpRequest();

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {

			var timdstamp_e = new Date();
			/////console.log("END[" + timdstamp_e + "]");
			xhr.timestamp_e = timdstamp_e.getTime();


			/////console.log("ELAPSED_TIME [" + (xhr.timestamp_e - xhr.timestamp_s) + "]");
			//callback(xhr.response, event);
			callback(xhr, event);
		}
	}

	var jsonData = { "mfrId": mfrId, "productId": productId };

	// ProductRestController 호출
	// with FILE EXTENSION the_uri = window.location.protocol + "//" + window.location.host + window.location.pathname + the_uri + '.pwkjson';
	the_uri = window.location.protocol + "//" + window.location.host + the_uri;
	
	/////console.log(the_uri);

	xhr.open("GET", the_uri, true); // true for asynchronous

	xhr.setRequestHeader("Accept", "application/json");
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.setRequestHeader("mfr-id", mfrId);
	xhr.setRequestHeader("product-id", productId);

	// controllers.js:53  Uncaught DOMException: Failed to set the 'responseType' property on 'XMLHttpRequest': 
	//The response type cannot be changed for synchronous requests made from a document.
	xhr.responseType = "json";

	var timestamp_s = new Date();
	/////console.log("START[" + timestamp_s + "]");
	/////console.log("START[" + timestamp_s.getTime() + "]");
	/////console.dir(timestamp_s);

	xhr.timestamp_s = timestamp_s.getTime();
	/////console.dir(xhr);
	
	// GET
	//xhr.send(null);
	xhr.send(JSON.stringify(jsonData)); // if 500 error

	return xhr;
}
