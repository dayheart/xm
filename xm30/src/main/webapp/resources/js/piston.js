function Piston(color, width=20) {
    this.x = 0;
    this.y = 0;
    this.width = width;
    //this.height = height;
    this.vx = 0;
    this.vy = 0;
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.color = (color === undefined) ? "#ffffff" : utils.parseColor(color);
    this.lineWidth = 0.5;
}

Piston.prototype.draw = function(context) {
    
    context.save();
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;

    context.beginPath();
    context.arc(0, 34, 26, (Math.PI+Math.PI*58/180), (Math.PI+Math.PI*122/180), false);
    context.lineTo(16, 12);
    context.lineTo(16, -12);
    context.lineTo(-16, -12);
    context.lineTo(-16, 12);
    context.closePath();
    context.fill();
    context.stroke();
    

    // ring
    context.beginPath();
    context.moveTo(-16, -8);
    context.lineTo(16, -8);
    context.moveTo(-16, -6);
    context.lineTo(16, -6);
    context.stroke();

  
    // pin
    context.beginPath();
    context.arc(0, 0, 3, 0, (Math.PI*2), false);
    context.closePath();
    context.stroke();

    context.restore()
};

/*
Piston.prototype.getPin = function() {
    return {
        x: this.x + Math.cos(this.rotation)*this.width,
        y: this.y + Math.sin(this.rotation)*this.width
    };
};
*/