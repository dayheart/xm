 
function ICone(color) {
    this.a = 0;
    this.x = 0;
    this.y = 0;
    this.vx = 0;
    this.v = 0;
    this.s_x = -30; // spiral x
    this.onchange = null;

    this.align = "right";

    //this.mass = 1;
    
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.color = color;
    this.lineWidth = 0.5;
    this.lineColor = undefined;

    this.lineCap = 'round';
}

ICone.prototype.draw2 = function (context) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    context.beginPath();
    
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    context.clip();

    context.lineWidth = this.lineWidth;
    // h:40, w:40 right
    context.beginPath();
    
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    //context.lineTo(9, -10);
    context.closePath();

    // draw spiral
    //context.moveTo(-10, -20);
    //context.quadraticCurveTo(-15, 5, -25, 20);

    context.moveTo(this.s_x, -20);
    context.quadraticCurveTo(this.s_x, -10, this.s_x-15, 20);
    //context.quadraticCurveTo(this.s_x, -10, this.s_x-15, 20);
    
    
    let x2 = this.s_x+30;
    //console.log("icons.js draw2 x2:" + x2);
    if(this.s_x < -20 ) { 
        context.moveTo(x2, -20);
        context.quadraticCurveTo(x2, -10, x2-15, 20);
    } else {
        context.moveTo(this.s_x-30, -20);
        context.quadraticCurveTo(this.s_x-30, -10, this.s_x-45, 20);
    }
    
    this.s_x += this.v;

    if(this.s_x > 30) {
        this.s_x = -30; // cone tip
    } else if(this.s_x < -40) {
        this.s_x = 20; // reverse
    }

    if(this.color != undefined)
        context.fill();

    if(this.lineColor != undefined) {
        context.strokeStyle = this.lineColor;
    }

    if(this.lineWidth>0) {
        context.stroke();
    }

    context.restore();
};



ICone.prototype.updateV = function(vx) {
    var old_vx = this.v;

    this.v = vx;
    if(typeof this.onchange==='function' && this.v != old_vx) {
        this.onchange();
    }
};


ICone.prototype.drawG2 = function (context) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    
    context.beginPath();
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    context.clip();

    // h:40, w:40 right
    
    //context.fillStyle = "red";
    //context.beginPath();
    //context.arc(0, 0, 5, 0, 2 * Math.PI);
    //context.fill();
    context.fillStyle = "rgba(127,127,127,0.1)";
    context.fillRect(-40, 0, 40, 20);
    context.fillStyle = "rgba(127,127,127,0.1)";
    context.fillRect(-40, 10, 40, 10);
    
    context.restore();
};

ICone.prototype.drawG = function (context) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    
    context.beginPath();
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    context.clip();

    // h:40, w:40 right
    context.beginPath();
    context.moveTo(-2, -18);
    context.quadraticCurveTo(-23, -14, -38, 0);
    context.quadraticCurveTo(-23, 8, -2, 10);
    //context.lineTo(0, 0);
    context.closePath();
    context.fillStyle = 'rgba(240,240,240,0.1)';
    context.fill();
    //context.stroke();

    context.beginPath();
    context.moveTo(-2, -18);
    context.quadraticCurveTo(-21, -12, -38, 0);
    context.quadraticCurveTo(-21, 3, -2, 3);
    //context.lineTo(-2, 0);
    context.closePath();
    context.fillStyle = 'rgba(127,127,127,0.2)';
    context.fill();
    //context.stroke();
    
    context.restore();
};

ICone.prototype.draw2_1 = function (context) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    context.beginPath();
    
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    context.clip();

    context.lineWidth = 1;
    // h:40, w:40 right
    context.beginPath();
    
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    //context.lineTo(9, -10);
    context.closePath();

    // draw spiral
    //context.moveTo(-10, -20);
    //context.quadraticCurveTo(-15, 5, -25, 20);

    context.moveTo(this.s_x, -20);
    context.quadraticCurveTo(this.s_x-5, 5, this.s_x-15, 20);

    this.s_x += this.v;
    
    if(this.s_x > 20) {
        this.s_x = -30; // cone tip
    } else if(this.s_x < -30) {
        this.s_x = 10; // reverse
    }

    if(this.color != undefined)
        context.fill();

    if(this.lineColor != undefined) {
        context.strokeStyle = this.lineColor;
    }

    if(this.lineWidth>0) {
        context.stroke();
    }

    context.restore();
};

ICone.prototype.draw = function (context, x) {
    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    // h:40, w:40 right
    context.beginPath();
    
    context.moveTo(0, -20);
    context.quadraticCurveTo(-25, -15, -40, 0);
    context.quadraticCurveTo(-25, 15, 0, 20);
    //context.lineTo(9, -10);
    context.closePath();

    // draw spiral
    context.moveTo(x-30, -20);
    context.quadraticCurveTo(x-35, 5, x-45, 20);

    if(this.color != undefined) {
        context.fill();
    }

    if(this.lineColor != undefined) {
        context.strokeStyle = this.lineColor;
    }

    if(this.lineWidth>0) {
        //context.strokeStyle = "#00ff00";
        context.stroke();
    }

    context.restore();
};



ICone.prototype.getBounds = function() {
    return {
        x: this.x-40, // width
        y: this.y-20, // height
        width: 40,
        height: 40
    };
};


