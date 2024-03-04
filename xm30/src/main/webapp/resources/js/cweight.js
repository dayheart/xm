function CounterWeight(color, width=13) {
    this.x = 0;
    this.y = 0;
    this.width = width;
    //this.height = height;
    this.vx = 0;
    this.vy = 0;
    this.va = 0;
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.color = (color === undefined) ? "#ffffff" : utils.parseColor(color);
    this.lineWidth = 0.5;

    this.onchange=null;
}

CounterWeight.prototype.draw = function(context) {
    
    context.save();
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;
    
    context.beginPath();
    context.moveTo(-14, 14);
    context.arc(0,0, 20, (Math.PI*3/4), (Math.PI*5/4), false);
    context.lineTo(0, -10);
    context.quadraticCurveTo(20, -10, 20, 0);
    context.quadraticCurveTo(20, 10, 0, 10);
    //ontext.lineTo(0, 10);
    context.closePath();
    context.fill();
    context.stroke();

    // pin
    context.beginPath();
    context.arc(0, 0, 5, 0, (Math.PI*2), true);
    context.closePath();
    context.stroke();

    context.beginPath();
    context.arc(13, 0, 5, 0, (Math.PI*2), true);
    context.closePath();
    context.stroke();

    context.restore()
};

CounterWeight.prototype.updateValue = function(va) {
    var old_value = this.va;

    if(va == 6) {
        this.va = -0.1;
    } else {
        this.va = va/10;
    }

    if(typeof this.onchange==='function' && this.va !== old_value) {
        this.onchange();
    }
};

CounterWeight.prototype.getPin = function() {
    return {
        x: this.x + Math.cos(this.rotation)*this.width,
        y: this.y + Math.sin(this.rotation)*this.width
    };
};