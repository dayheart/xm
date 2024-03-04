<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UX-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Hello Products</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.1/angular.js"></script>
<script src="${pageContext.request.servletContext.contextPath }/js/utils.js"></script>
<script src="${context_path}/js/utils.js"></script>
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
</style>
<script src="${context_path}/js/utils.js"></script>
<script src="${context_path}/js/mt.js"></script>
<script src="${context_path}/js/cweight.js"></script>
<script src="${context_path}/js/crod.js"></script>
<script src="${context_path}/js/piston.js"></script>
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
					if(productCode.length>1) {
						const mfrId = productCode[0];
						const productId = productCode[1];
						
						callProduct(mfrId, productId, '${context_path }/api/product', );
					}
					
					eles.push(ele);
				}
			} // end of request
		}
	}
}

function stopBgColor() {
	/////console.log("CLEAR_INTERVAL: " + nIntervalId);
	clearInterval(nIntervalId);
	nIntervalId = null;
}

</script>
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
	
	<script>

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
                        window.requestAnimationFrame(drawFrame, canvas);
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
                }

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
                    

                }
            }

            function init_gstick(eleId, targetId) {
                var canvas = document.getElementById(eleId),
                    ctx = canvas.getContext('2d'),
                    gstick = new MTGear();

                gstick.x = canvas.width/2;
                gstick.y = canvas.height/2;
                gstick.captureMouse(canvas);
                gstick.onchange = drawFrame;


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
                        	req_interval = 550 - t_shift*100;
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

                    e_target.dispatchEvent(mtshift);
                }

                drawFrame();
            }
        </script>
</body>
</html>