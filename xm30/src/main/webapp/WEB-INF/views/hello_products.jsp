<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UX-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Hello Products</title>
<!-- script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.1/angular.js"></script -->
<script
	src="${context_path}/js/angular.js"></script>
<script src="${pageContext.request.servletContext.contextPath }/js/utils.js"></script>
<!-- script src="${context_path}/js/utils.js"></script -->
<script src="${context_path}/js/hello.js"></script>
<script src="${context_path}/js/controllers.js"></script>
<style>
.housing {
	display: flex;
	/* justify-content: center; */ /* hc */
	align-items: center; /* vc */
}

.viewport {
	/**
     Position relative so that canvas element inside of it will be relative to the parent
     */
	position: relative;
	width: 60px; /* must be */ /* 27*2 + 6 */
	height: 110px; /* must be */ /* 20+13 + 45 + 12 + 5(pad) + 5 */
	/* background-color: brown; */
}

.viewport canvas {
	/*
     Position absolute provides canvases to be able to be layered on top of each other
     Be sure to remember a z-index!
     */
	position: absolute;
}

#gstick {
	cursor: pointer;
}

/* JET May 2025 */
.columns {
	display: flex;
	/* justify-content: center; */ /* hc */
	/* align-items: center; */ /* vc */
}

label {
	font-size: 8pt;
	/* color: chartreuse; */
	/* crimson #dc143c, orage #ffa500 , chartreuse #7fff00 */
}

.jet_housing {
	display: flex;
	justify-content: center; /* hc */
	align-items: center; /* vc */
}

.inviewport {
	/**
     Position relative so that canvas element inside of it will be relative to the parent
    */
	position: relative;
	width: 200px; /* must be */
	height: 100px; /* must be */
}

.inviewport canvas {
	/*
     Position absolute provides canvases to be able to be layered on top of each other
     Be sure to remember a z-index!
    */
	position: absolute;
	background-color: transparent;
}

.exviewport {
	/**
     Position relative so that canvas element inside of it will be relative to the parent
    */
	position: relative;
	width: 100px; /* must be */
	height: 100px; /* must be */
}

.exviewport canvas {
	/*
     Position absolute provides canvases to be able to be layered on top of each other
     Be sure to remember a z-index!
     */
	position: absolute;
	background-color: transparent;
}

.jet_viewport {
	/**
     Position relative so that canvas element inside of it will be relative to the parent
     */
	position: relative;
	width: 40px; /* must be */
	height: 100px; /* must be */
}

.jet_viewport canvas {
	/*
     Position absolute provides canvases to be able to be layered on top of each other
     Be sure to remember a z-index!
     */
	position: absolute;
	background-color: transparent;
}

.terminal_viewport {
	/**
     Position relative so that canvas element inside of it will be relative to the parent
     */
	position: relative;
	width: 4px; /* must be */
	height: 100px; /* must be */
}

.terminal_viewport canvas {
	/*
     Position absolute provides canvases to be able to be layered on top of each other
     Be sure to remember a z-index!
    */
	position: absolute;
	background-color: transparent;
}

.veq_block {
	display: inline-block;
}

.veq {
	/* text-align: center;  */
	
}

.veq_housing {
	display: flex;
	align-items: flex-end;
	border: 1px solid black;
}

.veq_viewport {
	/**
     Position relative so that canvas element inside of it will be relative to the parent
     */
	position: relative;
	width: 60px; /* must be */
	height: 110px; /* must be */
}

.veq_viewport canvas {
	/*
     Position absolute provides canvases to be able to be layered on top of each other
     Be sure to remember a z-index!
     */
	position: absolute;
	background-color: transparent;
}

.label_container {
	display: flex;
	align-items: flex-start;
	justify-content: center;
	/* text-align: center;  */
	width: 60px; /* must be */
	/*height: 15px; */ /* must be */
}

.label_container lable {
	/*
    display: flex;
    align-items: flex-end;
    */
	font-size: 8pt;
}
</style>
<!-- script src="${context_path}/js/utils.js"></script -->
<script src="${context_path}/js/mt.js"></script>
<script src="${context_path}/js/cweight.js"></script>
<script src="${context_path}/js/crod.js"></script>
<script src="${context_path}/js/piston.js"></script>
<script src="${context_path}/js/icone.js"></script>
<script src="${context_path}/js/ocone.js"></script>
<script src="${context_path}/js/cloud.js"></script>
<script src="${context_path}/js/elt.js"></script>
</head>
<body>
<script>
var productElements = null;
var requests = 5; // total product requests
var req_interval = 0;
var eles = new Array();


let nIntervalId;

window.addEventListener('load', onload, true);

function onload(event) {
	productElements = document.getElementsByClassName("product");
	
	//getElements(productElements);
	
	if(req_interval>0) {
		nIntervalId = setInterval(callElements, req_interval, productElements);
	}
}

function callElements(elements) {
	if(requests > 0 && elements!=null) {
		if(elements.length!=undefined) {
			
			if(eles.length>0) {
				for(var j=0; j<eles.length; j++) {
					eles.pop().bgColor = '';
				}
			}
			
			for(var i=0; i<requests; i++) {
				const ele_idx = utils.getRandom(0, productElements.length-1);
				if(ele_idx!=NaN) {
					var ele =  elements[ele_idx];
					ele.bgColor = 'chartreuse';
					//console.log(ele.bgColor);
					//console.log(ele.id);
					
					const eleId = ele.id; // REI-2A45C
					const productCode = eleId.split('-', 2);
					//console.log(productCode);

					if(productCode.length>1) {
						const mfrId = productCode[0];
						const productId = productCode[1];
						
						const xhr = callProduct(mfrId, productId, '${context_path }/api/product', );
						xhr["MFR_ID"] = mfrId;
						xhr["PRODUCT_ID"] = productId;
						//console.dir(xhr);
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
                            // fire an Event
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
                                /*
                                let activesMap = pActMap.get(pkey);
                                let activeNormalMap = activesMap.get("normal");
                                let activeWarnMap = activesMap.get("warn");
                                let activeFatalMap = activesMap.get("fatal");
                                eq.n = activeNormalMap.size;
                                eq.w = activeWarnMap.size;
                                eq.f = activeFatalMap.size;
                                */
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

						    startTimedWorker();
						} // end of if(xhr)
					}
					
					eles.push(ele);
                    console.log("Array eles' lenght: " + eles.length)
				}
			} // for
		}
	} // requests
} // function callElements

function stopBgColor() {
	/////console.log("CLEAR_INTERVAL: " + nIntervalId);
	clearInterval(nIntervalId);
	nIntervalId = null;
}



</script>
<div class="columns">
	<div name="left">
	<div class="housing">
		<!--
            <div>
                <label id="shift_level">0</label>
            </div>
        -->
		<div id="engine_div" draggable="true">
			<div class="viewport">
				<canvas id="engine_bg" width="60" height="110"></canvas>
				<canvas id="engine_it" width="60" height="110"></canvas>
				<canvas id="engine_fg" width="60" height="110"></canvas>
			</div>
		</div>
		<div>
			<canvas id="gstick" width="150" height="200"></canvas>
		</div>
	</div>
	<div >
		<section class="container" ng-app="productsApp">
			<div ng-controller="productsCtrl"
				ng-init="initProducts('${context_path }/api/products')">
				<table border="1">
					<tr>
						<th>PRODUCT</th>
						<th>DESCRIPTION</th>
						<th>PRICE</th>
						<th>QTY</th>
					</tr>
					<tr ng-repeat="product in products" >
						<td class="product" id="{{product.MFR_ID}}-{{product.PRODUCT_ID}}"
							title="{{product.MFR_ID}}-{{product.PRODUCT_ID}}" ng-click="ngClickItem(product, '${context_path }/api/product')">{{product.MFR_ID}}-{{product.PRODUCT_ID}}</td>
						<td>{{product.DESCRIPTION}}</td>
						<td>{{product.PRICE}}</td>
						<td>{{product.QTY_ON_HAND}}</td>
					</tr>
				</table>
			</div>
		</section>
	</div>
	</div> <!-- left column -->



<!-- JET May 2025 -->
        <div name="right">
        <div class="jet_housing">
            <div id="intake" draggable="true">
                <label id="intake_lable"></label>
                <div class="inviewport">
                    <canvas id="intake_bg" width="240" height="100"></canvas>
                    <canvas id="intake_blades" width="240" height="100"></canvas>
                    <canvas id="intake_glow" width="240" height="100"></canvas>
                </div>
                <label id="intake_cnt" class="veq_label"></label>
            </div>
            <div id="icone" draggable="true">
                <label id="icone_lable"></label>
                <div class="jet_viewport">
                    <canvas id="icone_bg" width="40" height="100"></canvas>
                    <canvas id="icone_blades" width="40" height="100"></canvas>
                    <canvas id="icone_glow" width="40" height="100"></canvas>
                </div>
                <label id="icone_cnt"></label>
            </div>
            <div id="flames" draggable="true">
                <label id="flames_lable"></label>
                <div id="flames_div" class="exviewport">
                    <canvas id="flames_bg" width="100" height="100"></canvas>
                    <canvas id="flames_blades" width="100" height="100"></canvas>
                    <canvas id="flames_glow" width="100" height="100"></canvas>
                </div>
                <label id="flames_cnt"></label>
            </div>
            <div id="ocone" draggable="true">
                <label id="ocone_lable"></label>
                <div class="jet_viewport">
                    <canvas id="ocone_bg" width="40" height="100"></canvas>
                    <canvas id="ocone_blades" width="40" height="100"></canvas>
                    <canvas id="ocone_glow" width="40" height="100"></canvas>
                </div>
                <label id="ocone_cnt"></label>
            </div>
            <div id="nozzle" draggable="true">
                <label id="nozzle_lable"></label>
                <div class="terminal_viewport">
                    <canvas id="nozzle_bg" width="200" height="100"></canvas>
                    <canvas id="nozzle_blades" width="200" height="100"></canvas>
                    <canvas id="nozzle_glow" width="200" height="100"></canvas>
                </div>
                <label id="nozzle_cnt"></label>
            </div>
        </div> <!-- jet_housing -->
        <label>SHIFT</label>
        <input type="number" id="vx" name="vx" min="-1" max="5" step="1" value="0"/>
        <label>NOR</label>
        <input type="number" id="normal" name="normal" min="0" max="50" step="1" value="0"/>
        <label>WARN</label>
        <input type="number" id="warn" name="warn" min="0" max="50" step="1" value="0"/>
        <label>FATAL</label>
        <input type="number" id="fatal" name="fatal" min="0" max="50" step="1" value="0"/>

        <div id="veq_list" class="veq_list"> 
<!--
            <div id="DAYHEART_block" draggable="true" class="veq_block">
                <div class="veq">
                    <label id="DAYHEART_topl"> </label>
                    <div id="DAYHEART_viewport" class="veq_viewport">
                        <canvas id="DAYHEART_bg" width="60" height="110"></canvas>
                        <canvas id="DAYHEART_blades" width="60" height="110"></canvas>
                        <canvas id="DAYHEART_glow" width="60" height="110"></canvas>
                    </div>
                    <div class="label_container">
                        <label id="DAYHEART_bottoml">DAYHEART</label>
                    </div>
                </div>
            </div>
<!--
        </div> 
        </div> <!-- right -->
</div> <!-- columns -->





<!-- JET May 2025 START-->
<script>
//drawVEQ("DAYHEART_blades"); // TEST

const FPS = 1000/60;

var tartget_engine = null;
            
window.addEventListener('load', init_engine_controller, true);
            
function init_engine_controller() {
    //utils.drawCross('canvas');
    init_gstick('gstick', 'engine_it');
    init_engine_bg('engine_bg');
    init_engine('engine_it');
}

function init_engine(eleId) {
    var canvas = document.getElementById(eleId),
    ctx = canvas.getContext('2d'),
    cw = new CounterWeight(),
    cr = new ConnectingRod(),
    piston = new Piston();

    var angle = 0;

    cw.x = canvas.width/2;
    cw.y = canvas.height - 30; // case r:27 + 3padd

    tartget_engine = cw;

    cw.onchange = drawFrame;

    let s_timestamp, p_timestamp, virgin = true;

    drawFrame();

    function drawFrame(timestamp=window.performance.now()) {

        if(p_timestamp === undefined) {
            p_timestamp = timestamp;
        }

        let elapsed = timestamp - p_timestamp;
        let isDrawing = true;

        if(elapsed >= FPS) {
            p_timestamp = timestamp;
            isDrawing = true;
        } else {
            isDrawing = false;
        }
                        
        if(cw.va!=0 && elapsed!=0) {
            //window.requestAnimationFrame(drawFrame, canvas);
            window.requestAnimationFrame(drawFrame);
            // ctx.clearRect(0, 0, canvas.width, canvas.height);
            angle += cw.va;
        }


        if(isDrawing||virgin) {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            //ctx.clearRect(0, 0, canvas.width, canvas.height);
            //cr.rotation = -16*Math.PI/180;
            cw.rotation =  angle; //-90*Math.PI/180;//-1*Math.PI/2;// Math.PI/2;

            cr.x = cw.getPin().x;
            cr.y = cw.getPin().y;

            /**************/
            var dx = cw.x - cw.getPin().x;
            //console.log('dx: ' + dx);

            var cosA = dx / cr.height;
            //console.log(cosA*180/Math.PI);
            cr.rotation = cosA;

            piston.x = cr.getPin().x;
            piston.y = cr.getPin().y;

            cw.draw(ctx);
            cr.draw(ctx);
            piston.draw(ctx);
        }

        virgin = false;
    } // end of function drawFrame

    window.requestAnimationFrame(drawFrame);
}

function init_engine_bg(eleId) {
    var canvas = document.getElementById(eleId),
    ctx = canvas.getContext('2d'),
    cw = new CounterWeight();

    drawEBlock();

    function drawEBlock() {
        var cwc_r = 27;
        ctx.lineWidth = 0.3;
        ctx.beginPath();
        ctx.arc(canvas.width/2, canvas.height-30, 27, Math.PI*7/4, Math.PI * 5/4, false);
        //console.log('cos(45):' + Math.cos(45*Math.PI/180));
        //console.log('sin(45):' + Math.sin(45*Math.PI/180));
        //console.log('cy_r:' + 27*Math.cos(45*Math.PI/180));
        var cy_r = 27*Math.cos(45*Math.PI/180);
        ctx.lineTo(canvas.width/2-cy_r, 5);
        //ctx.lineTo(canvas.width/2+cy_r, 5);
        ctx.quadraticCurveTo(canvas.width/2, 0, canvas.width/2+cy_r, 5);
        ctx.closePath();
        ctx.stroke();
        } // end of function drawEBlock
}

function init_gstick(eleId, targetId) {
    var canvas = document.getElementById(eleId),
    ctx = canvas.getContext('2d'),
    gstick = new MTGear();

    gstick.x = canvas.width/2;
    gstick.y = canvas.height/2;
    gstick.captureMouse(canvas);
    gstick.onchange = drawFrame;

    const $vx = document.getElementById('vx');

    var e_target = document.getElementById(targetId);
    e_target.addEventListener('mtshift', (e) => { //console.log(e.detail.shift);
        //console.log(e.target);         
        //console.log(nIntervalId);
        stopBgColor();

        if(tartget_engine!==null) {
            tartget_engine.updateValue(e.detail.shift);
                        
            //a bad way
            var t_shift = e.detail.shift;
            //console.log("T_SHIFT : " + t_shift);
            
            if(t_shift == 6 || t_shift==0) {
                t_shift = 0; // reverse
                req_interval = 0;      	
            } else {
                //req_interval = 550 - t_shift*100; // origin
                req_interval = 3000 - t_shift*10;
            }
                        
            if(req_interval>0) {
                nIntervalId = setInterval(callElements, req_interval, productElements);
            }
        }
                    
        ///console.log("A:" + nIntervalId);
    });

    function drawFrame() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        gstick.draw(ctx);
        //console.log('redraw');

        var mtshift = new CustomEvent("mtshift", {
            detail: {
                shift: this.shift
            }
        });
        $vx.value = this.shift;
        let shiftEvent = new Event('change');
        $vx.dispatchEvent(shiftEvent);

        e_target.dispatchEvent(mtshift);
    }

    drawFrame();
}

// <!-- JET May 2025 END-->

/*** 2025.05.29 start ****/
// just in time
function responseHandler(xhr, event) {
	//console.dir(xhr);
	if(xhr) {
		const k = xhr.MFR_ID + "-" + xhr.PRODUCT_ID + "@" + xhr.timestamp_s;
		const p = xhr.MFR_ID + "-" + xhr.PRODUCT_ID;
		let pAct = pActMap.get(p);
		let actMap;
		const now = new Date().getTime();
		let diff = now - xhr.timestamp_s;

		if( typeof(normalMap.get(k)) !==  "undefined") {
			normalMap.delete(k);
			//console.log("DEL:" + k + " NOR_SIZE: " + normalMap.size + ", " + (xhr.timestamp_e - xhr.timestamp_s) );
			if(typeof(pAct) !== "undefined" ) {
				actMap = pAct.get("normal");
			}
		} else if (typeof(warningMap.get(k)) !==  "undefined") {
		        warningMap.delete(k);
		        //console.log("DEL:" + k + " WARN_SIZE: " + warningMap.size + ", " + (xhr.timestamp_e - xhr.timestamp_s) );
			if(typeof(pAct) !== "undefined" ) {
				actMap = pAct.get("warn");
			}
		} else {
		        fatalMap.delete(k);
		        //console.log("DEL:" + k + " FATAL_SIZE: " + fatalMap.size + ", " + (xhr.timestamp_e - xhr.timestamp_s) );
			if(typeof(pAct) !== "undefined" ) {
				actMap = pAct.get("fatal");
			}
		} 

		if(typeof(actMap) !== "undefined") {
			actMap.delete(k);
			//console.log("DEL:" + k + " ACT_MAP: " + actMap.size + ", " + (xhr.timestamp_e - xhr.timestamp_s) );
		}

	}
}


var total_requests = 0;
const normalMap = new Map();
const warningMap = new Map();
const fatalMap = new Map();

const pActMap = new Map();
const veqMap = new Map();

function updateTotalActiveValues() {
	let normalCount = normalMap.size;
	let warnCount = warningMap.size;
	let fatalCount = fatalMap.size;
	total_requests = normalCount + warnCount + fatalCount;
	//console.log("total_requests! " + total_requests);
	//console.log("fatal_requests! " + fatalCount);
	//console.log("warning_requests! " + warnCount);
	//console.log("normal_requests! " + normalCount);

	const $normal = document.getElementById('normal');
	$normal.value = normalCount;
	const normalEvent = new Event('change');
	$normal.dispatchEvent(normalEvent);

	const $warning = document.getElementById('warn');
	$warning.value = warnCount;
	const warnEvent = new Event('change');
	$warning.dispatchEvent(warnEvent);

	const $fatal = document.getElementById('fatal');
	$fatal.value = fatalCount;
	const fatalEvent = new Event('change');
	$fatal.dispatchEvent(fatalEvent);
}

var veq_list = document.getElementById("veq_list");

let timedWork;

function startTimedWorker() {

        if(typeof(Worker) !== "undefined") {
                if(typeof(timedWork) == "undefined") {
                        timedWork = new Worker("${context_path}/js/timed_workers.js");
                        //console.log("Create workers!!!");

                        timedWork.onmessage = function(event) {
                                const theTime = new Date(event.data);
                                //console.log("a message is received! " + theTime);

                                normalMap.forEach(function(value, key) {
                                        let diff = theTime - value.timestamp_s;
                                        if(diff >= 3000) {
                                                warningMap.set(key, value);
                                                normalMap.delete(key);


                                                let pkey = key.slice(0, key.indexOf("@"));
                                                //console.log("FOUND_NORMAL_PKEY: " + pkey);
                                                // FOUND_NORMAL_PKEY: BIC-41003
                                                let pmap = pActMap.get(pkey);
                                                //console.log("FOUND_NORMAL_PMAP: " + pmap);
                                                let pNormalMap = pmap.get("normal");
                                                let pWarningMap = pmap.get("warn");
                                                pWarningMap.set(key, value);
                                                //pmap.set("warn", pWarningMap);
                                                //console.log("SET_WARN_PMAP: " + pWarningMap);

                                                pNormalMap.delete(key);
                                                //pmap.set("normal", pNormalMap);
                                                //console.log("DEL_NORMAL_PMAP: " + pNormalMap);

                                        }
                                });
                                warningMap.forEach(function(value, key) {
                                        let diff = theTime - value.timestamp_s;
                                        if(diff >= 6000) {
                                                fatalMap.set(key, value);
                                                warningMap.delete(key);

                                                let pkey = key.slice(0, key.indexOf("@"));
                                                //console.log("FOUND_WARN_PKEY: " + pkey);
                                                let pmap = pActMap.get(pkey);
                                                //console.log("FOUND_WARN_PMAP: " + pmap);
                                                let pWarningMap = pmap.get("warn");
                                                let pFatalMap = pmap.get("fatal");
                                                pFatalMap.set(key, value);
                                                //pmap.set("fatal", pFatalMap);
                                                //console.log("SET_FATAL_PMAP: " + pFatalMap);

                                                pWarningMap.delete(key);
                                                //pmap.set("warn", pWarningMap);
                                                //console.log("DEL_WARN_PMAP: " + pWarningMap);

                                        }
                                });

				// update just in time
				updateTotalActiveValues();

                                veqMap.forEach(function(value, key) {

				        //let eq = veqMap.get(pkey);
				        let eq = value;
				        let activesMap = pActMap.get(key);
				        let activeNormalMap = activesMap.get("normal");
				        let activeWarnMap = activesMap.get("warn");
				        let activeFatalMap = activesMap.get("fatal");
				        eq.n = activeNormalMap.size;
				        eq.w = activeWarnMap.size;
				        eq.f = activeFatalMap.size;
				        eq.drawBF();
				        eq.draw0();
                                });

                                if(total_requests == 0) {
                                        stopTimedWorker();
                                }
                        };
                }

        } else {
                document.title = "No Web Workers support!";
        }
}

function stopTimedWorker() {
        //console.log("Stop timedWorker!!!");
        timedWork.terminate();
        timedWork = undefined;
}


function initAnimationFrame() {

    //console.log("LoAD_ANIMATION: " + new Date());
    let p_timestamp = window.performance.now(); 
    let virgin = true;

    function drawFrame(timestamp=window.performance.now()) {
        if(p_timestamp === undefined) {
            p_timestamp = timestamp;
        }

        let elapsed = timestamp - p_timestamp;
        let isDrawing;
                    
        if(elapsed >= FPS) {
            p_timestamp = timestamp;
            isDrawing = true;
        } else {
            isDrawing = false;
        }
                    
        if(elapsed!=0) {
            //console.log("ANIMATION_TIME: " + new Date());
            window.requestAnimationFrame(drawFrame);
        }

        if(isDrawing || virgin) {

            veqMap.forEach(function(value, key) {

                //let eq = veqMap.get(pkey);
                let eq = value;
                let activesMap = pActMap.get(key);
                let activeNormalMap = activesMap.get("normal");
                let activeWarnMap = activesMap.get("warn");
                let activeFatalMap = activesMap.get("fatal");
                eq.n = activeNormalMap.size;
                eq.w = activeWarnMap.size;
                eq.f = activeFatalMap.size;

                let eq_tot = eq.n + eq.w + eq.f;
                if(eq_tot>0) {
                    eq.drawBF();
                }
            }); // forEach



//console.log("###############:" + total_requests);
            let in_cloud_canvas = document.getElementById("intake_blades"),
            out_cloud_canvas = document.getElementById("nozzle_blades");

               	

            if(total_requests > 0 ) {

                NO_CLOUDS = false;
                
                IN_CLOUD.context.clearRect(IN_CLOUD.prevX, IN_CLOUD.prevY, IN_CLOUD.width, IN_CLOUD.height);
                
                if(IN_CLOUD.x > in_cloud_canvas.width) {
                    IN_CLOUD.prevX = IN_CLOUD.x = -1*IN_CLOUD.width;
                    IN_CLOUD.prevY = IN_CLOUD.y = in_cloud_canvas.height / 2 + utils.getRandom(-20, 5);
                    IN_CLOUD.vx = 2;
                    IN_CLOUD.ax = 0.5;
                }
                IN_CLOUD.prevX = IN_CLOUD.x;
                IN_CLOUD.x += IN_CLOUD.vx;
                IN_CLOUD.vx += IN_CLOUD.ax;
                IN_CLOUD.draw0();

                OUT_CLOUD.context.clearRect(OUT_CLOUD.prevX, OUT_CLOUD.prevY, OUT_CLOUD.width, OUT_CLOUD.height);
                if(OUT_CLOUD.x > out_cloud_canvas.width) {
                    OUT_CLOUD.prevX = OUT_CLOUD.x = -1*OUT_CLOUD.width;
                    OUT_CLOUD.prevY = OUT_CLOUD.y = out_cloud_canvas.height / 2 + utils.getRandom(-20, 5);
                    OUT_CLOUD.vx = 2;
                    OUT_CLOUD.ax = 0.5;
                }
                OUT_CLOUD.prevX = OUT_CLOUD.x;
                OUT_CLOUD.x += OUT_CLOUD.vx;
                OUT_CLOUD.vx += OUT_CLOUD.ax;
                OUT_CLOUD.draw0();

            } else if(total_requests == 0 && !NO_CLOUDS ) {
                IN_CLOUD.context.clearRect(IN_CLOUD.x, IN_CLOUD.y, IN_CLOUD.width, IN_CLOUD.height);
                OUT_CLOUD.context.clearRect(OUT_CLOUD.x, OUT_CLOUD.y, OUT_CLOUD.width, OUT_CLOUD.height);
                NO_CLOUDS = true;
                //console.log("CLEAR...");
            } 
        }

        virgin = false;
    } // drawFrame

    window.requestAnimationFrame(drawFrame);
} // fuction initAnimationFrame 

/*** 2025.05.29 end ****/


const IN_CLOUD = new RCloud("rgba(224, 224, 224, 0.8)");
const OUT_CLOUD = new RCloud("rgba(224, 224, 224, 0.8)");
var NO_CLOUDS = true;

//<!-- JET May 2025 -->

window.addEventListener('load', init_jet, true);
window.addEventListener('load', initAnimationFrame, true);

function init_jet() {
    drawReducerHousing('flames_bg', 15, 0, false);
    drawReducerHousing('ocone_bg', 15, -10, false);
                

    drawReducerGlow('flames_glow', 15, 0);
    drawReducerGlow('ocone_glow', 15, -10);
                

    init_clouds('intake_blades');
    init_clouds('nozzle_blades');

                 
    function init_clouds(eleId) {
        let canvas = document.getElementById(eleId),
        ctx = canvas.getContext('2d');
                
        let cloud;
        if(eleId == "intake_blades") {
            cloud = IN_CLOUD;
        } else {
            cloud = OUT_CLOUD;
        } 
                
        cloud.elementId = eleId;        
        cloud.context = ctx;
        cloud.prevX = cloud.x = -1*cloud.width;
        cloud.prevY = cloud.y = canvas.height / 2 + utils.getRandom(-30, 20);
        cloud.vx = 2;
        cloud.ax = 0.5;
            /* 
                (function drawFrame(){
                    window.requestAnimationFrame(drawFrame, canvas);
                    ctx.clearRect(cloud.prevX, cloud.prevY, cloud.width, cloud.height);

                    if(cloud.x > canvas.width) {
                        cloud.prevX = cloud.x = -1*cloud.width;
                        cloud.prevY = cloud.y = canvas.height / 2 + utils.getRandom(-20, 5);
                        cloud.vx = 2;
                        //cloud.ax = 0.5;
                    }

                    cloud.prevX = cloud.x;
                    cloud.x += cloud.vx;
                    cloud.vx += cloud.ax;
                    cloud.draw(ctx);
                }());
            */
	} // init_clouds


    const icone = new ICone();
    init_cone('icone_blades', icone);
    init_iconeG('icone_glow');

    const ocone = new OCone();
    init_cone('ocone_blades', ocone);
                

    /*
    const chl_blades = init_blades('chl_blades');
    const mci_blades = init_blades('mci_blades');
    const esb_blades = init_blades('esb_blades');
    const cor_blades = init_blades('cor_blades');
    const eai_blades = init_blades('eai_blades');
    const fep_blades = init_blades('fep_blades', true);
    */

    const flames = init_flames('flames_blades');
    const $flames_bg = document.getElementById('flames_bg');
    const $icone_lable = document.getElementById('icone_lable');
    const $icone_cnt = document.getElementById('icone_cnt');

    const $vx = document.getElementById('vx');

    $vx.addEventListener('change', onchangeV, true);

    function onchangeV(e) {
        icone.updateV(Number(e.target.value));
        /*
        chl_blades.updateV(Number(e.target.value));
        mci_blades.updateV(Number(e.target.value));
        esb_blades.updateV(Number(e.target.value));
        cor_blades.updateV(Number(e.target.value));
        eai_blades.updateV(Number(e.target.value));
        fep_blades.updateV(Number(e.target.value));
        */
        ocone.updateV(Number(e.target.value));
    }

    const $normal = document.getElementById('normal');
    $normal.addEventListener('change', onchangeEQ, true);
    const $warn = document.getElementById('warn');
    $warn.addEventListener('change', onchangeEQ, true);
    const $fatal = document.getElementById('fatal');
    $fatal.addEventListener('change', onchangeEQ, true);
                
    const $flames_div = document.getElementById('flames_div');
    //console.log($flames_div);

    function onchangeEQ(e) {
        let targetId = e.target["id"];
        switch(targetId){
            case "normal" :
                flames.n = Number(e.target.value);
            break;

            case "warn" :
                flames.w = Number(e.target.value);
            break;

            case "fatal" :
                flames.f = Number(e.target.value);
            break;

            default :
            break;
        }
        let w_n = (flames.n > 30)?30:flames.n
        let w_w = (flames.w > 30)?30:flames.w
        let w_f = (flames.f > 30)?30:flames.f
        flames.tot = w_n + w_w + w_f;
        let w_temp = flames.tot * flames.gap;

        //console.log('in pixel : ' + flames_div.clientWidth);
        //console.log('onchangeEQ $frames_bg : ' + flames_bg.clientWidth);
                    
        if(w_temp > 100) { // default width is 100
            let pixel = new String(w_temp + 'px');
            //console.log(pixel);
            flames_div.style.width = pixel;
            flames_bg.width = w_temp;
            flames_glow.width = w_temp;

            drawReducerHousing('flames_bg', 15, 0, false);
            drawReducerGlow('flames_glow', 15, 0);
        }

        flames.updateV();
    } // onchageEQ
}; // init_jet

            
function init_flames(eleId) {
    const canvas = document.getElementById(eleId),
        ctx = canvas.getContext('2d'),
        flames = new HActEQ();

    flames.x = 0;
    flames.y = canvas.height / 2;
    flames.onchange = drawFrame;
    
    function drawFrame() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        let w_temp = flames.tot * flames.gap;
        if(w_temp > canvas.width) {
            flames.width = canvas.width = w_temp;
        }

        flames.draw(ctx);
    }
    //flames.draw(ctx);

    return flames;
}

function init_cone(eleId, cone) {
    const canvas = document.getElementById(eleId),
        context = canvas.getContext('2d');
        //ic = new ICone();

    const $icone_lable = document.getElementById('icone_lable');
    const $icone_cnt = document.getElementById('icone_cnt');

    if(cone.align == "right") {
        cone.x = canvas.width;
    } else if(cone.align = "left") {
        cone.x = 0;
    }
    
    cone.y = canvas.height / 2;
    // the color of cone
    //cone.lineColor = '#7FFF00';
    cone.onchange = drawFrame;
    cone.v = 0;

    let s_timestamp, p_timestamp; 
    let virgin = true;

    function drawFrame(timestamp=window.performance.now()) {
        if(p_timestamp === undefined) {
            p_timestamp = timestamp;
        }

        let elapsed = timestamp - p_timestamp;
        let isDrawing;
        
        if(elapsed >= FPS) {
            p_timestamp = timestamp;
            isDrawing = true;
        } else {
            isDrawing = false;
        }
        
        if(cone.v!=0 && elapsed!=0) {

            //window.requestAnimationFrame(drawFrame, canvas);
            window.requestAnimationFrame(drawFrame);
        }

        if(isDrawing || virgin) {
            context.clearRect(cone.getBounds().x, cone.getBounds().y, cone.getBounds().width, cone.getBounds().height);
            cone.draw2(context);
            $icone_lable.innerText = cone.v;
            $icone_cnt.innerText = cone.s_x;
        }

        virgin = false;
    }

    window.requestAnimationFrame(drawFrame);

    //return ic;
}

function init_iconeG(eleId) {
    const canvas = document.getElementById(eleId),
        context = canvas.getContext('2d'),
        ic = new ICone();
    
    ic.x = canvas.width;
    ic.y = canvas.height / 2;
    ic.lineColor = '#7FFF00';

    ic.drawG2(context);
}

// INTAKE CONE

function init_blades(eleId, reverse=false) {
    const canvas = document.getElementById(eleId),
        ctx = canvas.getContext('2d'),
        obj = new CDrum();
    obj.x = 0;
    obj.y = 0;
    obj.isReverse = reverse;

    //obj.draw2(ctx);
    /*
    ctx.beginPath();
    ctx.rect(0, 0, canvas.width, canvas.height);
    ctx.moveTo(0, 8);
    ctx.lineTo(canvas.width, 8);
    ctx.stroke();
    */

    obj.onchange = drawFrame;
    obj.v = 0;

    let s_timestamp, p_timestamp; 
    let virgin = true;

    function drawFrame(timestamp=window.performance.now()) {
        if(p_timestamp === undefined) {
            p_timestamp = timestamp;
        }

        let elapsed = timestamp - p_timestamp;
        let isDrawing = true;
        
        if(elapsed >= FPS) {
            p_timestamp = timestamp;
            isDrawing = true;
        } else {
            isDrawing = false;
        }
        
        if(obj.v!=0 && elapsed!=0) {
            //window.requestAnimationFrame(drawFrame, canvas);
            window.requestAnimationFrame(drawFrame);
        }

        if(isDrawing || virgin) {
            ctx.clearRect(obj.getBounds().x, obj.getBounds().y, obj.getBounds().width, obj.getBounds().height);
            obj.draw2(ctx);
        }

        virgin = false;
    }

    window.requestAnimationFrame(drawFrame);

    return obj;
}


// GLOW 
function drawReducerGlow(eleId, dy, reducer=0) {
    var canvas = document.getElementById(eleId),
        ctx = canvas.getContext('2d');
    /*
    ctx.beginPath();
    ctx.rect(0, 0, canvas.width, canvas.height);
    ctx.moveTo(0, 2*dy);
    ctx.lineTo(canvas.width, 2*dy);
    ctx.stroke();
    */

    // INNER TOP CLIP
    ctx.beginPath();
    ctx.moveTo(0, dy);
    ctx.lineTo(canvas.width, dy -1*reducer);
    ctx.lineTo(canvas.width, canvas.height - dy +reducer);
    ctx.lineTo(0, canvas.height - dy);
    ctx.closePath();
    ctx.clip();
    //ctx.stroke();

    /* upper section
    ctx.fillStyle = "rgba(240,240,240,0.1)";
    ctx.fillRect(0, 2*dy, canvas.width,30);
    ctx.fillStyle = "rgba(240,240,240,0.1)";
    ctx.fillRect(0, 2*dy, canvas.width,20);
    ctx.fillStyle = "rgba(240,240,240,0.1)";
    ctx.fillRect(0, 2*dy, canvas.width,10);
    */
    ctx.fillStyle = "rgba(127,127,127,0.1)";
    ctx.fillRect(0, 50, canvas.width,40);
    ctx.fillStyle = "rgba(127,127,127,0.1)";
    ctx.fillRect(0, 60, canvas.width,30);
    ctx.fillStyle = "rgba(127,127,127,0.1)";
    ctx.fillRect(0, 70, canvas.width,20);
    ctx.fillStyle = "rgba(127,127,127,0.1)";
    ctx.fillRect(0, 80, canvas.width,10);
    
}

            
// HOUSING
function drawReducerHousing(eleId, dy, reducer, stator) {
    var canvas = document.getElementById(eleId),
        ctx = canvas.getContext('2d');
    const T = 2;

    //console.log('drawReducerHousing ['+ eleId + '] canvas.width: ' + canvas.width);
    //ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.lineWidth = 0.5;

    // INNER TOP CLIP
    ctx.beginPath();
    ctx.moveTo(0, dy);
    ctx.lineTo(canvas.width, dy -1*reducer);
    ctx.lineTo(canvas.width, canvas.height - dy +reducer);
    ctx.lineTo(0, canvas.height - dy);
    ctx.closePath();
    //ctx.stroke();
    ctx.clip();

    //ctx.strokeStyle = "#7Fff00";
    ctx.strokeStyle = "#000000";


    ctx.beginPath();
    // OUTLINE TOP
    ctx.moveTo(0, dy);
    ctx.lineTo(canvas.width, dy -1*reducer);

    // OUTLINE BOTTOM
    ctx.moveTo(0, canvas.height - dy);
    ctx.lineTo(canvas.width, canvas.height - dy +reducer);
    
    // INNER TOP
    ctx.moveTo(0, dy+T);
    ctx.lineTo(canvas.width, dy -1*reducer + T);

    // INNER BOTTOM
    ctx.moveTo(0, canvas.height - dy - T);
    ctx.lineTo(canvas.width, canvas.height - dy +reducer -T);
    //ctx.strokeStyle = "#00ff00";
    ctx.stroke();

    if(stator) {
        ctx.lineWidth = 0.2;
        var tx = canvas.width/2;
        var ty = canvas.height/2-20;

        ctx.moveTo(tx, 0);
        ctx.lineTo(tx + 6, ty);
        ctx.lineTo(tx + 12, ty);
        ctx.lineTo(canvas.width-5, 0);
        ctx.closePath();

        ty = canvas.height/2-10;
        ctx.moveTo(20, ty);
        ctx.lineTo(canvas.width, ty);
        ty += 20;
        ctx.moveTo(20, ty);
        ctx.lineTo(canvas.width, ty);

        ty = canvas.height/2+20;
        ctx.moveTo(tx, canvas.height);
        ctx.lineTo(tx + 6, ty);
        ctx.lineTo(tx + 12, ty);
        ctx.lineTo(canvas.width-5, canvas.height);
        ctx.closePath();
        ctx.stroke();
    }
}

</script>
<!-- JET May 2025 -->
</body>
</html>
