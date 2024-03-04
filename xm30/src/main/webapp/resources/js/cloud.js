function LCloud(color) {
    this.x=0;
    this.y=0;
    this.width = 40;
    this.height = 20;
    this.vx = 0;
    this.vy = 0;
    this.ax = 0;
    this.ay = 0;
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY= 1;
    this.color = (color===undefined)?"#ffffff":utils.parseColor(color);
    this.lineWidth = 0.5;

    this.prevX=0;
    this.prevY=0;
}

LCloud.prototype.draw = function(context) {

    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    var region = new Path2D();
    region.arc(5, 15, 5, 0, Math.PI*2, false);
    region.arc(15, 10, 10, 0, Math.PI*2, false);
    region.arc(27, 10, 7, 0, Math.PI*2, false);
    region.arc(35, 15, 5, 0, Math.PI*2, false);
    region.rect(5, 10, 30, 10);
    context.clip(region);
    
    context.rect(0, 0, this.width, this.height);
    context.fill();
    //context.closePath();
    
    context.restore();

};

LCloud.prototype.getBounds = function() {
    return {
        x: this.x,
        y: this.y,
        width: this.width,
        height: this.height
    };
};


function RCloud(color) {
    this.x=0;
    this.y=0;
    this.width = 40;
    this.height = 20;
    this.vx = 0;
    this.vy = 0;
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY= 1;
    this.color = (color===undefined)?"#ffffff":utils.parseColor(color);
    this.lineWidth = 0.5;
}

RCloud.prototype.draw = function(context) {

    context.save();
    
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    var region = new Path2D();
    region.arc(5, 15, 5, 0, Math.PI*2, false);
    region.arc(13, 10, 7, 0, Math.PI*2, false);
    region.arc(25, 10, 10, 0, Math.PI*2, false);
    region.arc(35, 15, 5, 0, Math.PI*2, false);
    region.rect(5, 10, 30, 10);
    context.clip(region);
    
    context.rect(0, 0, this.width, this.height);
    context.fill();
    //context.closePath();
    
    context.restore();

};


RCloud.prototype.getBounds = function() {
    return {
        x: this.x,
        y: this.y,
        width: this.width,
        height: this.height
    };
};

