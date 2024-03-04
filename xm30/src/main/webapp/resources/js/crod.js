function ConnectingRod(color, height=45) {
    this.x = 0;
    this.y = 0;
    this.height = height;
    this.vx = 0;
    this.vy = 0;
    this.rotation = 0;
    this.scaleX = 1;
    this.scaleY = 1;
    this.color = (color === undefined) ? "#ffffff" : utils.parseColor(color);
    this.lineWidth = 0.5;
}

ConnectingRod.prototype.draw = function(context) {
    
    context.save();
    context.translate(this.x, this.y);
    context.rotate(this.rotation);
    context.scale(this.scaleX, this.scaleY);
    context.lineWidth = this.lineWidth;
    context.fillStyle = this.color;
    
    var s_a = (60*Math.PI/180), e_a = (120*Math.PI/180), rod_r = 10;
    var s_x = rod_r * Math.cos(s_a), s_y = rod_r*Math.sin(s_a); 
    context.beginPath();
    context.arc(0, 0, rod_r, s_a, e_a, false);
    var left=-1*s_x-5, right=s_x+5;
    context.lineTo(left, s_y);
    context.lineTo(left, -1*s_y);
    //context.lineTo(-1*s_x, -1*s_y);
    context.quadraticCurveTo(-3, -10, -5, -1*this.height); // CW Max 20+13
    //context.lineTo(5, -35);
    context.arc(0, -1*this.height, 5, Math.PI, 2*Math.PI, false);
    context.quadraticCurveTo(3, -10, right, -1*s_y);
    context.lineTo(right, s_y);
    context.closePath();
    context.fill();
    context.stroke();

    // the center x, y
    context.beginPath();
    context.arc(0, 0, 5, 0, (Math.PI*2), false);
    context.closePath();
    context.stroke();

    // the pin
    context.beginPath();
    context.arc(0, -1*this.height, 2, 0, 2*Math.PI, false);
    context.stroke();

    /*
    context.lineTo(0, -10);
    context.quadraticCurveTo(20, -10, 20, 0);
    context.quadraticCurveTo(20, 10, 0, 10);
    //ontext.lineTo(0, 10);
    context.closePath();
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
    */

    context.restore()
};


ConnectingRod.prototype.getPin = function() {
    return {
        x: this.x + Math.sin(this.rotation)*this.height,
        y: this.y - Math.cos(this.rotation)*this.height
    };
};