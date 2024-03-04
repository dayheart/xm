var utils = {};

/**
 * document 요소의 마우스 x, y 좌표를 구한다.
 * @param {*} element 
 * @returns 
 */
utils.captureMouse = function(element) {
    var mouse = {x:0, y:0};

    element.addEventListener('mousemove', function(event){

        var x, y;

        if(event.pageX || pageY) {
            x = event.pageX;
            y = event.pageY;
        } else {
            x = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
            y = event.clientY + document.body.scrollTop + document.documentElement.scrollTo;
        }

        x -= element.offsetLeft;
        y -= element.offsetTop;

        mouse.x = x;
        mouse.y = y;
    },false);

    return mouse;
};

utils.captueTouch = function(element) {
    var touch = {x:null, y:null, isPressed:false};

    element.addEventListener('touchstart', function(event) {
        touch.isPressed = true;
    }, false);

    element.addEventListener('touchend', function(event) {
        touch.isPressed = false;
        touch.x = null;
        touch.y = null;
    }, false);

    element.addEventListener('touchmove', function(event) {
        var x, y, 
            touch_event = event.touches[0]; // first touch

        if( touch_event.pageX || touch_event.pageY ) {
            x = touch_event.pageX;
            y = touch_evnet.pageY;
        } else {
            x = touch_event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
            y = touch_event.clientY + document.body.scrollTop + document.documentElement.scrollTop;
        }

        x -= offsetLeft;
        y -= offsetTop;

        touch.x = x;
        touch.y = y;

    }, false);

    return touch;
};


utils.colorToRGC = function(color, alpha) {
    // if string format, convert to number
    if(typeof color === 'string' && color[0] === '#') {
        color = window.parseInt(color.slice(1), 16);
    }

    alpha = (alpha===undefined)?1:alpha;

    var r = color >> 16 & 0xff,
        g = color >> 8 & 0xff,
        b = color & 0xff,
        a = (alpha<0)?0:((alpha>1)?1:alpha); 

        if(a===1) {
            return "rgb(" + r + "," + g +"," + b + ")";
        } else {
            return "rgba(" + r + "," + g +"," + b + "," + a + ")";
        }
};


utils.parseColor = function(color, toNumber) {
    if(toNumber === true) {
        if(typeof color === 'number') {
            return (color|0); //chop off decimal
        }

        if(typeof color === 'string' && color[0] === '#') {
            color = color.slice(1);
        }
        return window.parseInt(color, 16);
    } else {
        if(typeof color === 'number') {
            color = "#" + ('00000' +(color|0).toString(16)).substr(-6);
        }
        return color;
    }
};


utils.containsPoint = function(rect, x, y) {
    return !(   x<rect.x || // over the left
                x>rect.x+rect.width ||  // over the right
                y<rect.y || // over the top
                y>rect.y+rect.height); // over the bottom
};

utils.intersects = function(rectA, rectB) {
    return !(rectA.x + rectA.width < rectB.x 
        ||   rectB.x + rectB.width < rectA.x
        ||   rectA.y + rectA.height < rectB.y
        ||   rectB.y + rectB.height < rectA.y);
};

utils.drawCross = function(eleId) {
    var canvas = document.getElementById(eleId),
        context = canvas.getContext('2d');

    context.save();

    context.lineWidth = 0.1;

    context.beginPath();
    context.moveTo(0, canvas.height/2);
    context.lineTo(canvas.width, canvas.height/2);
    context.moveTo(canvas.width/2, 0);
    context.lineTo(canvas.width/2, canvas.height);
    context.stroke();

    context.restore();
};

utils.getRandom = function(min, max) {
    return Math.floor((Math.random()*(max-min+1))+min);
};

utils.getRandomArray = function(min, max, count) {
    if(max-min+1<count)
        return;

    var arr = [];
    while(true) {
        var idx = utils.getRandom(min, max);

        // dup
        if(arr.indexOf(idx)>-1) {
            continue;
        }

        arr.push(idx);

        if(arr.length == count) {
            break;
        }
    }

    return arr.sort(function(a, b) {
        return a - b;
    });
};