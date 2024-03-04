function MTGear() {
    this.shift = 0; // the gear shift val
    this.onchange = null;

    this.x = 0;
    this.y = 0;
    this.width = 50;
    this.height = 50;

    this.lineCap = 'round';

    this.backColor = "#cccccc";
    this.backBorderColor = "#999999";

    //this.handleColor = "#eeeeee";
    this.handleColor = "#333";
    this.handleBorderColor = "#cccccc";
    this.handleX = 0;
    this.handleY = 0;
    this.handleRadius = 25;

    //this.updatePosition();
}

const SHIFT_PATTERNS = new Array({x:25, y:25}, {x:0, y:0}, {x:0, y:50}, {x:25, y:0}, {x:25, y:50}, {x:50, y:0}, {x:50, y:50});

MTGear.prototype.draw = function(context) {
    context.save();
    context.translate(this.x, this.y); // the left top is 0,0

    // for DEBUG
    this.ctx = context;

    // draw gear dragging line;
    context.fillStyle = this.backColor;
    context.beginPath();
    // column first
    context.fillRect(-2, 0, 4, this.height);
    context.fillRect(23, 0, 4, this.height);
    context.fillRect(48, 0, 4, this.height);

    // row
    context.fillRect(0, 23, this.width, 4);

    var tm = SHIFT_PATTERNS[this.shift];
    this.handleX = tm.x;
    this.handleY = tm.y;

    // draw handle
    context.strokeStyle = this.handleBorderColor;
    context.fillStyle = this.handleColor;
    //context.beginPath();
    //x, y, radius, start_angle, end_angle, anti-clockwise
    context.arc(this.handleX, this.handleY, this.handleRadius, 0, (Math.PI * 2), true);
    //context.closePath();
    context.fill();
    context.stroke();

    context.font = "10px Arial";
    context.textAlign = "center";
    context.fillStyle = "white";
    context.fillText("1", this.handleX-10, this.handleY-10);
    context.fillText("3", this.handleX, this.handleY-10);
    context.fillText("4", this.handleX, this.handleY+18);
    context.fillText("2", this.handleX-10, this.handleY+18);
    context.fillText("5", this.handleX+10, this.handleY-10);
    context.fillText("R", this.handleX+10, this.handleY+18);

    //context.beginPath();
    context.moveTo(this.handleX-10, this.handleY);
    context.lineTo(this.handleX+10, this.handleY);

    context.moveTo(this.handleX, this.handleY-10);
    context.lineTo(this.handleX, this.handleY+10);

    context.moveTo(this.handleX-10, this.handleY-10);
    context.lineTo(this.handleX-10, this.handleY+10);

    context.moveTo(this.handleX+10, this.handleY-10);
    context.lineTo(this.handleX+10, this.handleY+10);
    context.stroke();
    

    context.beginPath();
    context.arc(this.handleX-3, this.handleY-3, this.handleRadius-7, 0, 2*Math.PI, false);
    context.fillStyle = "rgba(255,255,255,0.2)";
    context.fill();

    context.beginPath();
    context.arc(this.handleX-5, this.handleY-5, this.handleRadius-10, 0, (2*Math.PI), false);
    context.fillStyle = "rgba(255,255,255,0.2)";
    context.fill();

    context.beginPath();
    context.arc(this.handleX-7, this.handleY-7, this.handleRadius-15, 0, (2*Math.PI), false);
    context.fillStyle = "rgba(255,255,255,0.2)";
    context.fill();

    context.restore();
};

MTGear.prototype.updateShift = function() {
    var old_shift = this.shift;

    //this.shift = 1;
    if(this.handleX == 0) {
        if(this.handleY == this.height) {
            this.shift = 2;
        } else {
            this.shift = 1;
        }
    } else if(this.handleX == 25) {
        if(this.handleY == this.height) {
            this.shift = 4;
        } else if(this.handleY == 25) {
            this.shift = 0; // neutral
        } else {
            this.shift = 3;
        }
    } else {
        if(this.handleY == this.height) {
            this.shift = 6;
        } else {
            this.shift = 5;
        }
    }
    
    if(typeof this.onchange === 'function' && this.shift !== old_shift) {
        this.onchange();
        var mtshift = new CustomEvent("mtshift", {
            detail: {
                shift: this.shift
            }
        });
    }
};

// shift 값에 의해서 ...
MTGear.prototype.updatePosition = function() {
    this.tm = 1;
    //this.handleX = 0;
    //this.handleY = 0;
}

MTGear.prototype.captureMouse = function(element) {
    var self = this,
        mouse = utils.captureMouse(element),
        bounds = {};
    
    //console.log(this);

    //console.log('captureMouse: ' + element);

    setHandleBounds();

    element.addEventListener('mousedown', function() {

        if(utils.containsPoint(bounds, mouse.x, mouse.y)) {
            element.addEventListener('mouseup', onMouseUp, false);
            element.addEventListener('mousemove', onMouseMove, false);
            //console.log('addEventListener');
        }
    }, false);

    function onMouseUp() {
        element.removeEventListener('mousemove', onMouseMove, false);
        element.removeEventListener('mouseup', onMouseUp, false);
        setHandleBounds();

        //console.log('removeEventListener');
    }

    function onMouseMove() {
        var pos_y = mouse.y - self.y,
            pos_x = mouse.x - self.x;

        self.handleX = Math.min(self.width, Math.max(pos_x, 0));
        if(self.handleX < 25) {
            self.handleX = 0;
        } else if(self.handleX < 50) {
            self.handleX = 25;
        } else {
            self.handleX = 50;
        }
        self.handleY = Math.min(self.height, Math.max(pos_y, 0));
        if(self.handleY < 25 ) {
            self.handleY = 0;
        } else if(self.handleY < 50){
            self.handleY = 25;
            self.handleX = 25; // neutral
        } else {
            self.handleY = 50;
        }
        // 핸들의 중심이 아무리커도 self.height

        self.updateShift();
    }

    function setHandleBounds() {
        bounds.x = self.x + self.handleX - self.handleRadius;
        bounds.y = self.y + self.handleY - self.handleRadius;
        bounds.width = 50;
        bounds.height = 50;

        //console.log(bounds); 정해진 위치에서만 
    }
};