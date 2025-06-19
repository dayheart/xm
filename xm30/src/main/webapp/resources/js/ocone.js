 
function OCone(color) {
    this.x = 0;
    this.y = 0;
    this.vx = 0;
    this.vy = 0;

    this.v = 0;
    this.s_x = 0;
    this.align = "left";
    
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.color = color;
    this.lineWidth = 0.5;
    this.lineColor = undefined;

    this.lineCap = 'round';
}

OCone.prototype.draw = function (context) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    // h:40, w:40 left
    context.beginPath();
    
    context.moveTo(0, -20);
    context.lineTo(40, 0);
    context.lineTo(0, 20);
    //context.quadraticCurveTo(-25, -15, -40, 0);
    //context.quadraticCurveTo(-25, 15, 0, 20);
    context.closePath();
    /*
    context.moveTo(0, -20);
    context.quadraticCurveTo(5, 5, 20, 20);
    */

    if(this.color != undefined)
        context.fill();

    if(this.lineWidth>0) {
        context.stroke();
    }

    context.restore();
};

OCone.prototype.draw = function (context, x) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    // h:40, w:40 left
    context.beginPath();
    
    context.moveTo(0, -20);
    context.lineTo(40, 0);
    context.lineTo(0, 20);
    context.closePath();

    context.moveTo(x-10, -20);
    context.quadraticCurveTo(x-5, 10, x+5, 20);

    var x2 = x+20;
    //console.log('x2:' + x2);
    if(x2 < 40) {
        context.moveTo(x+20, -20);
        context.quadraticCurveTo(x+30, 10, x+40, 20);
    } else {
        context.moveTo(x-40, -20);
        context.quadraticCurveTo(x-35, 10, x-25, 20);
    }
    
    //context.lineTo(x+5, 20);
    

    if(this.color != undefined)
        context.fill();

    if(this.lineColor!=undefined) {
        context.strokeStyle = this.lineColor;
    }

    if(this.lineWidth>0) {
        //context.strokeStyle = "#00ff00";
        context.stroke();
    }

    context.restore();
};

OCone.prototype.updateV = function(vx) {
    var old_vx = this.v;

    this.v = vx;
    if(typeof this.onchange==='function' && this.v != old_vx) {
        this.onchange();
    }
};

OCone.prototype.draw2 = function (context) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    context.beginPath();
    context.moveTo(0, -20);
    context.lineTo(40, 0);
    context.lineTo(0, 20);
    context.closePath();
    context.clip();

    // h:40, w:40 left
    context.beginPath();
    context.moveTo(0, -20);
    context.lineTo(40, 0);
    context.lineTo(0, 20);
    context.closePath();
    
    context.moveTo(this.s_x-10, -20);
    context.quadraticCurveTo(this.s_x-5, 10, this.s_x+5, 20);
    
    let x2 = this.s_x+20;
    //console.log('x2:' + x2);
    if(x2 < 40) {
        context.moveTo(this.s_x+20, -20);
        context.quadraticCurveTo(this.s_x+30, 10, this.s_x+40, 20);
    } else {
        context.moveTo(this.s_x-40, -20);
        context.quadraticCurveTo(this.s_x-35, 10, this.s_x-25, 20);
    }

    this.s_x += this.v;
    if(this.s_x > 60) { // forward
        this.s_x = 0;
    } else if (this.s_x < 0) { // reverse
        this.s_x = 60;
    }
    
    //context.lineTo(x+5, 20);
    

    if(this.color != undefined)
        context.fill();

    if(this.lineColor!=undefined) {
        context.strokeStyle = this.lineColor;
    }

    if(this.lineWidth>0) {
        //context.strokeStyle = "#00ff00";
        context.stroke();
    }

    context.restore();
};

OCone.prototype.getBounds = function() {
    return {
        x: this.x,
        y: this.y - 20,
        width: 40,
        height: 40
    };
};


// Italy
function HActEQ(width=100, height=50, color="#7Fff00") {
    this.x = 0;
    this.y = 0;
    
    this.v = 0;

    this.t = 1;
    this.gap = 6;
    this.tot = 0;
    this.n = 0;
    this.w = 0;
    this.f = 0;
    this.width = width;
    this.height = height;
    this.fontColor = "gray"; // "#000000";
    this.font = "italic 8pt Arial, sans-serif"; //"italic small-caps bold 8px Arial";

    this.onchange = null;

    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.lineWidth = 0.5;
    this.lineColor = "gray"; //  "#000000"; //color;

    this.lineCap = 'round';
}

HActEQ.prototype.draw = function (context) {
    
    const MAX_WIDTH = 896;

    const color_f = "rgba(220, 20, 60, 1)"; // crimson #dc143c crimpson
    const color_w = "rgba(255, 165, 0, 1)"; // orage #ffa500 coral
    const color_n = "rgba(127, 255, 0, 1)"; // chartreuse #7fff00 dodgerblud

    context.save();

    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.strokeStyle = this.lineColor;

    /*
    context.rect(0, -50, this.width, this.height);
    context.stroke();
    
    let metricPath = new Path2D();
    metricPath.rect(0, -50, this.width, this.height);
    console.log(this.height);
    context.clip(metricPath, "evenodd");
    */
    
    /************************* */
    if(this.width > MAX_WIDTH) {
        //this.width = MAX_WIDTH; // and so on
    }
    /************************* */

    context.rect(0, -50, this.width, 25);
    
    let cnt = Math.floor(this.width / this.gap); // 
    //let slotPath = new Path2D();
    for(let i=0; i<cnt; i++) {
        //slotPath.rect(i*this.gap, -25, this.t, 50);
        context.rect(i*this.gap+2, -25, this.t, 50);
    }
    //context.clip(slotPath);
    context.clip();

    /*
    slotPath.rect(0, -60, this.width, 10);
    context.clip(slotPath, "evenodd");
    */
    
    /*
    let flamePath = new Path2D();
    //flamePath.beginPath();
    flamePath.moveTo(0, -25);
    flamePath.quadraticCurveTo(this.width, -25, this.width, -15);
    flamePath.lineTo(this.width, 15);
    flamePath.quadraticCurveTo(this.width, 25, 0, 25);
    flamePath.closePath();
    context.clip(flamePath);
    */

    
    //context.fillRect(0, -50, this.width, 50);

    let px_f=0, px_w=0, px_n=0, w_f=0, w_w=0, w_n=0;

    //this.tot = 16;
    //console.log(this.tot);
    
    if(this.tot>0) {
        context.beginPath();
        context.moveTo(0, -45);
        context.lineTo(0, -30);
        context.stroke();    
    }
    

    //this.f = 3;
    if(this.f!==undefined && this.f>0) {
        px_f = 0; 
        if(this.f > 30 )
        	w_f = this.gap*30;
	else 
        	w_f = this.gap*this.f;

        context.fillStyle = "hotpink"; //"crimson";  // deeppink #ff1493
        //context.fillRect(0, -25, this.gap*this.f, this.height);    
        context.fillRect(px_f, -25, w_f, this.height);

        context.beginPath();
        context.moveTo(0, -40);
        
        // H
        let px_end = w_f;
        context.lineTo(px_end, -40);
        context.stroke();

        let min_w = w_f;
        let px_start = px_f;
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -40);
            context.lineTo(px_start + 3, -43);
            context.lineTo(px_start + 3, -37);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "gray"; //  "black";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -40);
            context.lineTo(px_end - 3, -43);
            context.lineTo(px_end - 3, -37);
            context.closePath();
            context.fillStyle = "gray"; // "black";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        let px_font = w_f/2-3;
        context.font = this.font;
        context.fillStyle = "gray"; // "black"; // "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.f, px_font, -42);
    }

    //this.w = 5;
    if(this.w!==undefined && this.w>0) {
        px_w = px_f + w_f; //console.log(p_w);
        if(this.w > 30 )
        	w_w = this.gap*30
        else
        	w_w = this.gap*this.w;  //console.log(w_w);

        context.fillStyle = "goldenrod"; //"coral"; // "darkorange";  // orage #ff7f50
        context.fillRect(px_w, -25, w_w, this.height);    

        context.beginPath();
        context.moveTo(px_w, -40);
        
        // H
        let px_end = px_w + w_w;
        context.lineTo(px_end, -40);
        context.stroke();

        let min_w = w_w;
        let px_start = px_w;
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -40);
            context.lineTo(px_start + 3, -43);
            context.lineTo(px_start + 3, -37);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = this.fontColor; // "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -40);
            context.lineTo(px_end - 3, -43);
            context.lineTo(px_end - 3, -37);
            context.closePath();
            context.fillStyle = this.fontColor//"#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        context.font = this.font;
        let px_font = px_w + w_w/2 -3;
        context.fillStyle = "gray"; // "black";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.w, px_font, -42);

    }

    //this.n = 8;
    if(this.n!==undefined && this.n>0) {
        px_n = px_f + w_f + w_w; //console.log(px_n);
        if(this.n > 30 )
        	w_n = this.gap*30;
        else
        	w_n = this.gap*this.n;   

        context.fillStyle = "deepskyblue"; //"royalblue"; //"cornflowerblue"; //"dodgerblue"; // "#1e90ff";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillRect(px_n, -25, w_n, this.height);

        context.beginPath();
        context.moveTo(px_n, -40);
        
        // H
        let px_end = px_n + w_n; 
        //console.log("HActEQ this.n px_end: " + px_end);
        context.lineTo(px_end, -40);
        context.stroke();

        let min_w = w_n;
        let px_start = px_n;
        
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -40);
            context.lineTo(px_start + 3, -43);
            context.lineTo(px_start + 3, -37);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "gray"; // "black";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -40);
            context.lineTo(px_end - 3, -43);
            context.lineTo(px_end - 3, -37);
            //context.lineTo(px_end, -35)
            context.closePath();
            context.fillStyle = "gray"; // "black";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        
        context.font = this.font;
        let px_font = px_n + w_n/2 -3;
        context.fillStyle = "gray"; // "black";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.n, px_font, -42);
        
    }
    
    /*
    context.fillStyle = "rgba(127, 255, 0, 1)";  
    context.fillRect(0, -25, this.width, this.height);
    */

    
    context.restore();
};


HActEQ.prototype.getBounds = function() {
    return {
        x: this.x,
        y: this.y - 50,
        width: this.width,
        height: this.height
    };
};

HActEQ.prototype.updateV = function() {
    //let old_v = this.v;

    //if(typeof this.onchange==='function' && this.v != old_vx) {
    if(typeof this.onchange==='function') {
        this.onchange();
    }
};

HActEQ.prototype.drawG = function(context) {
    const MAX_WIDTH = 896;

    const color_f = "rgba(220, 20, 60, 1)"; // crimson #dc143c
    const color_w = "rgba(255, 165, 0, 1)"; // orage #ffa500
    const color_n = "rgba(127, 255, 0, 1)"; // chartreuse #7fff00

    context.save();

    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.strokeStyle = this.lineColor;

    /************************* */
    if(this.width > MAX_WIDTH) {
        //this.width = MAX_WIDTH; // and so on
    }
    /************************* */

    context.rect(0, -50, this.width, 25);
    
    let cnt = Math.floor(this.width) / this.gap; // 
    //let slotPath = new Path2D();
    for(let i=0; i<cnt; i++) {
        //slotPath.rect(i*this.gap, -25, this.t, 50);
        context.rect(i*this.gap+2, -25, this.t, 50);
    }
    //context.clip(slotPath);
    context.clip();

    /*
    slotPath.rect(0, -60, this.width, 10);
    context.clip(slotPath, "evenodd");
    */
    
    /*
    let flamePath = new Path2D();
    //flamePath.beginPath();
    flamePath.moveTo(0, -25);
    flamePath.quadraticCurveTo(this.width, -25, this.width, -15);
    flamePath.lineTo(this.width, 15);
    flamePath.quadraticCurveTo(this.width, 25, 0, 25);
    flamePath.closePath();
    context.clip(flamePath);
    */

    
    //context.fillRect(0, -50, this.width, 50);

    let px_f=0, px_w=0, px_n=0, w_f=0, w_w=0, w_n=0;

    //this.tot = 16;
    //console.log(this.tot);
    if(this.tot>0) {
        context.beginPath();
        context.moveTo(0, -45);
        context.lineTo(0, -30);
        context.stroke();    
    }

    //this.f = 3;
    if(this.f!==undefined && this.f>0) {
        px_f = 0; 
        w_f = this.gap*this.f;
        context.fillStyle = "red";  // deeppink #ff1493
        //context.fillRect(0, -25, this.gap*this.f, this.height);    
        context.fillRect(px_f, -25, w_f, this.height);

        context.beginPath();
        context.moveTo(0, -35);
        
        // H
        let px_end = w_f;
        context.lineTo(px_end, -35);
        context.stroke();

        let min_w = w_f;
        let px_start = px_f;
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -35);
            context.lineTo(px_start + 3, -38);
            context.lineTo(px_start + 3, -32);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -35);
            context.lineTo(px_end - 3, -38);
            context.lineTo(px_end - 3, -32);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        let px_font = w_f/2-3;
        context.font = this.font;
        context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.f, px_font, -40);
    }

    //this.w = 5;
    if(this.w!==undefined && this.w>0) {
        px_w = px_f + w_f; //console.log(p_w);
        w_w = this.gap*this.w;  //console.log(w_w);
        context.fillStyle = "#ffa500";  // orage #ff7f50
        context.fillRect(px_w, -25, w_w, this.height);    

        context.beginPath();
        context.moveTo(px_w, -35);
        
        // H
        let px_end = px_w + w_w;
        context.lineTo(px_end, -35);
        context.stroke();

        let min_w = w_w;
        let px_start = px_w;
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -35);
            context.lineTo(px_start + 3, -38);
            context.lineTo(px_start + 3, -32);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -35);
            context.lineTo(px_end - 3, -38);
            context.lineTo(px_end - 3, -32);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        context.font = this.font;
        let px_font = px_w + w_w/2 -3;
        context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.w, px_font, -40);

    }

    //this.n = 8;
    if(this.n!==undefined && this.n>0) {
        px_n = px_f + w_f + w_w; //console.log(px_n);
        w_n = this.gap*this.n;   
        context.fillStyle = "#1e90ff";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillRect(px_n, -25, w_n, this.height);

        context.beginPath();
        context.moveTo(px_n, -35);
        
        // H
        let px_end = px_n + w_n; console.log(px_end);
        context.lineTo(px_end, -35);
        context.stroke();

        let min_w = w_n;
        let px_start = px_n;
        
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -35);
            context.lineTo(px_start + 3, -38);
            context.lineTo(px_start + 3, -32);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -35);
            context.lineTo(px_end - 3, -38);
            context.lineTo(px_end - 3, -32);
            //context.lineTo(px_end, -35)
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        
        context.font = this.font;
        let px_font = px_n + w_n/2 -3;
        context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.n, px_font, -40);
        
    }
    
    /*
    context.fillStyle = "rgba(127, 255, 0, 1)";  
    context.fillRect(0, -25, this.width, this.height);
    */

    
    context.restore();
};



/* ***** V EQ ***** */
function VActEQ(context=null, width=60, height=110, color="#7Fff00") {
    this.context = context;
    this.bgContext ;
    this.fgContext ;
    this.x = 0;
    this.y = 0;
    
    this.v = 1;

    this.t = 1;
    this.gap = 3
    this.tot = 0;
    this.n = 0;
    this.w = 0;
    this.f = 0;


    // the locaton of font
    this.fx = 0; // font x
    this.fy = 0; // font y
    this.angle = utils.getRandom(0, 3.28);
    this.speed = 0.3;
    this.range = 3; // font height 14

    this.bobbingOffset = 0; 
    
    this.width = width;
    this.height = height;

    // later, add resizing condition
    //this.theFirst = true;  // out size clearRect, no need
    
    this.left = -1*this.width/2 + 5; // 5 + 50 + 5 
    this.farLeft = -1*this.width/2;
    this.right = this.width-5;
    this.top = -1 * this.height + 20;
    this.bottom = 0;

    // later, add resizing condition

    this.fontColor = "black";  //"gray"; // "#000000";
    this.font = "italic 8pt Arial, sans-serif"; //"italic small-caps bold 8px Arial";

    this.onchange = null;

    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.lineWidth = 1;
    this.lineColor = "gray"; //  "#000000"; //color;

    this.lineCap = 'round';
}

VActEQ.prototype.drawBF = function () {

    this.tot = this.n+this.w+this.f;

    this.bgContext.save();

    this.bgContext.translate(this.x, this.y);
    this.bgContext.rotate(this.rotation);
    this.bgContext.scale(this.scaleX, this.scaleY);
    
    this.bgContext.lineWidth = this.lineWidth;
    this.bgContext.strokeStyle = this.lineColor;


    this.bgContext.clearRect(this.farLeft, this.fy-20, this.width, 30); // widly clear for bobbing range



    let bobbing = Math.sin(this.angle)*this.range;

    // refresh the previous location
    if(this.tot == 0 )
       this.fy = this.gap*30*this.tot + 5;
    else if(this.tot > 30 )
       this.fy = this.gap*30 + 7 + bobbing;
    else 
       this.fy = this.gap*this.tot + 7 + bobbing;
    this.fy *= -1;

    this.angle += this.speed;

    //console.log(this.x, py);
    // Label
    this.bgContext.font = this.font;
    this.bgContext.textAlign = "center";
    this.bgContext.fillStyle = this.fontColor;  // chartreuse #7fff00 dodgerblue #1e90ff
    this.bgContext.fillText(this.tot, 0, this.fy);


    this.bgContext.restore();
};



VActEQ.prototype.draw0 = function () {

    this.tot = this.n+this.w+this.f;

    const MAX_WIDTH = 100;
    const MAX_HEIGHT = 200;

    const color_f = "rgba(220, 20, 60, 1)"; // crimson #dc143c crimpson
    const color_w = "rgba(255, 165, 0, 1)"; // orage #ffa500 coral
    const color_n = "rgba(127, 255, 0, 1)"; // chartreuse #7fff00 dodgerblud

    this.context.save();

    this.context.translate(this.x, this.y);
    this.context.rotate(this.rotation);
    this.context.scale(this.scaleX, this.scaleY);
    
    this.context.lineWidth = this.lineWidth;
    this.context.strokeStyle = this.lineColor;

    /*
    this.context.moveTo(0, -90);
    this.context.lineTo(30, -90);
    this.context.stroke();
    return;
    */


    this.context.clearRect(this.left, this.top, -1*this.left*2, -1*this.top);

    /*
    this.context.rect(0, -50, this.width, this.height);
    this.context.stroke();
    
    let metricPath = new Path2D();
    metricPath.rect(0, -50, this.width, this.height);
    console.log(this.height);
    this.context.clip(metricPath, "evenodd");
    */
    
    /************************* */
    if(this.height > MAX_HEIGHT) {
        //this.width = MAX_WIDTH; // and so on
    }
    /************************* */

    //console.log(this.left, this.top, this.width, this.height);
    //context.fillRect(this.left, this.top, this.width, this.height);
    //return;


    //context.rect(this.left, this.top, this.width, this.height);
    
    let cnt = Math.floor((this.height-10)/ this. gap) ;
    for(let i=0; i<=cnt; i++) {
        this.context.rect(this.left, -1 * i*(this.gap) + this.t, this.width-10, this.t);
    }
    
    this.context.clip();
    
    let px_f=0, px_w=0, px_n=0, h_f = 0, h_w = 0, h_n = 0;


    //MAX this.tot == 30
    //this.tot = 16;
    //console.log(this.tot);


        px_f = 0;
        if(this.f > 30 )
                w_f = this.gap*30;
        else
                w_f = this.gap*this.f;
    
    //this.f = 3;
    if(this.f!==undefined && this.f>0) {

        if(this.f > 30 )
            h_f = this.gap*30;
        else
            h_f = this.gap*this.f;

        px_f = -1 * h_f;
        this.context.fillStyle = "hotpink"; //"crimson";  // deeppink #ff1493
        //this.context.fillRect(0, -25, this.gap*this.f, this.height);    
        this.context.fillRect(this.left, px_f, this.width, h_f);
    }

    //this.w = 5;
    if(this.w!==undefined && this.w>0) {

        if(this.f < 30 ) {
            let d = 30 - (this.f+this.w);
            if(d<0) {
                d = 30 - this.f;
                h_w = this.gap * d;
            } else {
                h_w = this.gap * this.w; 
            } 
            // h_w = this.gap * this.w; 
            px_w = -1*(h_f+h_w); 
            this.context.fillStyle = "goldenrod"; //"coral"; // "darkorange";  // orage #ff7f50
            this.context.fillRect(this.left, px_w, this.width, h_w);
        }
    }

    //this.n = 8;
    if(this.n!==undefined && this.n>0) {
        let t_sum = this.f + this.w;
        if((t_sum) < 30) {
            let d = 30 - (this.tot);
            if(d<0) {
                d = 30 - t_sum;
                h_n = this.gap * d;
            } else {
                h_n = this.gap * this.n;
            } 


            // h_n = this.gap * this.n;
            //px_n = -1*(h_f + h_w + h_n) + this.bobbingOffset;
            px_n = -1*(h_f + h_w + h_n);
            this.context.fillStyle = "deepskyblue"; //"royalblue"; //"cornflowerblue"; //"dodgerblue"; // "#1e90ff";  // chartreuse #7fff00 dodgerblue #1e90ff
            this.context.fillRect(this.left, px_n, this.width, h_n);        
        }
    }
    
    /*
    this.context.fillStyle = "rgba(127, 255, 0, 1)";  
    this.context.fillRect(0, -25, this.width, this.height);
    */ 
    
    this.context.restore();

};



VActEQ.prototype.draw = function (context) {
    
    const MAX_WIDTH = 100;
    const MAX_HEIGHT = 200;

    const color_f = "rgba(220, 20, 60, 1)"; // crimson #dc143c crimpson
    const color_w = "rgba(255, 165, 0, 1)"; // orage #ffa500 coral
    const color_n = "rgba(127, 255, 0, 1)"; // chartreuse #7fff00 dodgerblud

    context.save();

    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.strokeStyle = this.lineColor;

    /*
    context.moveTo(0, -90);
    context.lineTo(30, -90);
    context.stroke();
    return;
    */



    /*
    context.rect(0, -50, this.width, this.height);
    context.stroke();
    
    let metricPath = new Path2D();
    metricPath.rect(0, -50, this.width, this.height);
    console.log(this.height);
    context.clip(metricPath, "evenodd");
    */
    
    /************************* */
    if(this.height > MAX_HEIGHT) {
        //this.width = MAX_WIDTH; // and so on
    }
    /************************* */

    //console.log(this.left, this.top, this.width, this.height);
    //context.fillRect(this.left, this.top, this.width, this.height);
    //return;


    //context.rect(this.left, this.top, this.width, this.height);
    
    let cnt = Math.floor((this.height-10)/ this. gap) ;
    for(let i=0; i<=cnt; i++) {
        context.rect(this.left, -1 * i*(this.gap) + this.t, this.width-20, this.t);
    }
    
    context.clip();
    
    let px_f=0, px_w=0, px_n=0, h_f = 0, h_w = 0, h_n = 0;


    //MAX this.tot == 30
    //this.tot = 16;
    //console.log(this.tot);


        px_f = 0;
        if(this.f > 30 )
                w_f = this.gap*30;
        else
                w_f = this.gap*this.f;
    
    //this.f = 3;
    if(this.f!==undefined && this.f>0) {

        if(this.f > 30 )
            h_f = this.gap*30;
        else
            h_f = this.gap*this.f;

        px_f = -1 * h_f;
        context.fillStyle = "hotpink"; //"crimson";  // deeppink #ff1493
        //context.fillRect(0, -25, this.gap*this.f, this.height);    
        context.fillRect(this.left, px_f, this.width, h_f);
    }

    //this.w = 5;
    if(this.w!==undefined && this.w>0) {

        if(this.f < 30 ) {
            let d = 30 - (this.f+this.w);
            if(d<0) {
                d = 30 - this.f;
                h_w = this.gap * d;
            } else {
                h_w = this.gap * this.w; 
            } 
            // h_w = this.gap * this.w; 
            px_w = -1*(h_f+h_w); 
            context.fillStyle = "goldenrod"; //"coral"; // "darkorange";  // orage #ff7f50
            context.fillRect(this.left, px_w, this.width, h_w);
        }
    }

    //this.n = 8;
    if(this.n!==undefined && this.n>0) {
        let t_sum = this.f + this.w;
        if((t_sum) < 30) {
            let d = 30 - (this.tot);
            if(d<0) {
                d = 30 - t_sum;
                h_n = this.gap * d;
            } else {
                h_n = this.gap * this.n;
            } 


            // h_n = this.gap * this.n;
            //px_n = -1*(h_f + h_w + h_n) + this.bobbingOffset;
            px_n = -1*(h_f + h_w + h_n);
            context.fillStyle = "deepskyblue"; //"royalblue"; //"cornflowerblue"; //"dodgerblue"; // "#1e90ff";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fillRect(this.left, px_n, this.width, h_n);        
        }
    }
    
    /*
    context.fillStyle = "rgba(127, 255, 0, 1)";  
    context.fillRect(0, -25, this.width, this.height);
    */ 
    
    context.restore();
};



VActEQ.prototype.draw2 = function (context) {
    
    const MAX_WIDTH = 100;
    const MAX_HEIGHT = 200;

    const color_f = "rgba(220, 20, 60, 1)"; // crimson #dc143c crimpson
    const color_w = "rgba(255, 165, 0, 1)"; // orage #ffa500 coral
    const color_n = "rgba(127, 255, 0, 1)"; // chartreuse #7fff00 dodgerblud

    context.save();

    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.strokeStyle = this.lineColor;

    /*
    context.rect(0, -50, this.width, this.height);
    context.stroke();
    
    let metricPath = new Path2D();
    metricPath.rect(0, -50, this.width, this.height);
    console.log(this.height);
    context.clip(metricPath, "evenodd");
    */
    
    /************************* */
    if(this.height > MAX_HEIGHT) {
        //this.width = MAX_WIDTH; // and so on
    }
    /************************* */

    context.rect(this.left, this.top, this.width, this.height);
    let cnt = Math.floor(this.height) / this. gap;
    for(let i=0; i<cnt; i++) {
        context.rect(this.left, -1 * i*(this.gap+2), this.width, this.t);
    }
    context.clip();
    
    let px_f=0, px_w=0, px_n=0, h_f = 0, h_w = 0, h_n = 0;

    //this.tot = 16;
    //console.log(this.tot);
    
    //this.f = 3;
    if(this.f!==undefined && this.f>0) {
        h_f = this.gap*this.f;
        px_f = -1 * h_f;
        context.fillStyle = "hotpink"; //"crimson";  // deeppink #ff1493
        //context.fillRect(0, -25, this.gap*this.f, this.height);    
        context.fillRect(this.left, px_f, this.width, h_f);
    }

    //this.w = 5;
    if(this.w!==undefined && this.w>0) {
        h_w = this.gqp * this.w;
        px_w = px_f - h_f; //console.log(p_w);
        context.fillStyle = "goldenrod"; //"coral"; // "darkorange";  // orage #ff7f50
        context.fillRect(this.left, px_w, this.width, h_w);
    }

    //this.n = 8;
    if(this.n!==undefined && this.n>0) {
        h_n = this.gap * this.n;
        px_n = -1*(h_f + h_w + h_n);
        context.fillStyle = "deepskyblue"; //"royalblue"; //"cornflowerblue"; //"dodgerblue"; // "#1e90ff";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillRect(this.left, px_n, this.width, h_n);        
    }
    
    /*
    context.fillStyle = "rgba(127, 255, 0, 1)";  
    context.fillRect(0, -25, this.width, this.height);
    */ 
    context.restore();
};

VActEQ.prototype.getBounds = function() {
    return {
        x: -1*(this.width/2),
        y: -1*(this.height),
        width: this.width,
        height: this.height
    };
};

VActEQ.prototype.updateV = function() {
    //let old_v = this.v;

    //if(typeof this.onchange==='function' && this.v != old_vx) {
    if(typeof this.onchange==='function') {
        this.onchange();
    }
};

VActEQ.prototype.drawG = function(context) {
    const MAX_WIDTH = 896;

    const color_f = "rgba(220, 20, 60, 1)"; // crimson #dc143c
    const color_w = "rgba(255, 165, 0, 1)"; // orage #ffa500
    const color_n = "rgba(127, 255, 0, 1)"; // chartreuse #7fff00

    context.save();

    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.strokeStyle = this.lineColor;

    /************************* */
    if(this.width > MAX_WIDTH) {
        //this.width = MAX_WIDTH; // and so on
    }
    /************************* */

    context.rect(0, -50, this.width, 25);
    
    let cnt = Math.floor(this.width) / this.gap; // 
    //let slotPath = new Path2D();
    for(let i=0; i<cnt; i++) {
        //slotPath.rect(i*this.gap, -25, this.t, 50);
        context.rect(i*this.gap+2, -25, this.t, 50);
    }
    //context.clip(slotPath);
    context.clip();

    /*
    slotPath.rect(0, -60, this.width, 10);
    context.clip(slotPath, "evenodd");
    */
    
    /*
    let flamePath = new Path2D();
    //flamePath.beginPath();
    flamePath.moveTo(0, -25);
    flamePath.quadraticCurveTo(this.width, -25, this.width, -15);
    flamePath.lineTo(this.width, 15);
    flamePath.quadraticCurveTo(this.width, 25, 0, 25);
    flamePath.closePath();
    context.clip(flamePath);
    */

    
    //context.fillRect(0, -50, this.width, 50);

    let px_f=0, px_w=0, px_n=0, w_f=0, w_w=0, w_n=0;

    //this.tot = 16;
    //console.log(this.tot);
    if(this.tot>0) {
        context.beginPath();
        context.moveTo(0, -45);
        context.lineTo(0, -30);
        context.stroke();    
    }

    //this.f = 3;
    if(this.f!==undefined && this.f>0) {
        px_f = 0; 
        w_f = this.gap*this.f;
        context.fillStyle = "red";  // deeppink #ff1493
        //context.fillRect(0, -25, this.gap*this.f, this.height);    
        context.fillRect(px_f, -25, w_f, this.height);

        context.beginPath();
        context.moveTo(0, -35);
        
        // H
        let px_end = w_f;
        context.lineTo(px_end, -35);
        context.stroke();

        let min_w = w_f;
        let px_start = px_f;
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -35);
            context.lineTo(px_start + 3, -38);
            context.lineTo(px_start + 3, -32);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -35);
            context.lineTo(px_end - 3, -38);
            context.lineTo(px_end - 3, -32);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        let px_font = w_f/2-3;
        context.font = this.font;
        context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.f, px_font, -40);
    }

    //this.w = 5;
    if(this.w!==undefined && this.w>0) {
        px_w = px_f + w_f; //console.log(p_w);
        w_w = this.gap*this.w;  //console.log(w_w);
        context.fillStyle = "#ffa500";  // orage #ff7f50
        context.fillRect(px_w, -25, w_w, this.height);    

        context.beginPath();
        context.moveTo(px_w, -35);
        
        // H
        let px_end = px_w + w_w;
        context.lineTo(px_end, -35);
        context.stroke();

        let min_w = w_w;
        let px_start = px_w;
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -35);
            context.lineTo(px_start + 3, -38);
            context.lineTo(px_start + 3, -32);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -35);
            context.lineTo(px_end - 3, -38);
            context.lineTo(px_end - 3, -32);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        context.font = this.font;
        let px_font = px_w + w_w/2 -3;
        context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.w, px_font, -40);

    }

    //this.n = 8;
    if(this.n!==undefined && this.n>0) {
        px_n = px_f + w_f + w_w; //console.log(px_n);
        w_n = this.gap*this.n;   
        context.fillStyle = "#1e90ff";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillRect(px_n, -25, w_n, this.height);

        context.beginPath();
        context.moveTo(px_n, -35);
        
        // H
        let px_end = px_n + w_n; console.log(px_end);
        context.lineTo(px_end, -35);
        context.stroke();

        let min_w = w_n;
        let px_start = px_n;
        
        if(min_w >= 10) {
            context.beginPath();
            context.moveTo(px_start, -35);
            context.lineTo(px_start + 3, -38);
            context.lineTo(px_start + 3, -32);
            //context.lineTo(0, -35);
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();

            context.beginPath();
            context.moveTo(px_end, -35);
            context.lineTo(px_end - 3, -38);
            context.lineTo(px_end - 3, -32);
            //context.lineTo(px_end, -35)
            context.closePath();
            context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
            context.fill();
        }

        // V
        context.beginPath();
        context.moveTo(px_end, -45);
        context.lineTo(px_end, -30);
        context.stroke();

        // Label
        
        context.font = this.font;
        let px_font = px_n + w_n/2 -3;
        context.fillStyle = "#7fff00";  // chartreuse #7fff00 dodgerblue #1e90ff
        context.fillText(this.n, px_font, -40);
        
    }
    
    /*
    context.fillStyle = "rgba(127, 255, 0, 1)";  
    context.fillRect(0, -25, this.width, this.height);
    */

    
    context.restore();
};
