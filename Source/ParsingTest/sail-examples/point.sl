enum color {
    Red,
    Black
}

method min(x : int, y : int, z : int) : int {
    if (x < y)
        if (y < z) return x
        else if (x < z) return x else return z
    else
        if (x < z) return y
        else if (y < z) return y else return z
}

struct point {x:int, y:int, c:color}

process Main(){
    var p : point = point {x:5, y:7, c:Red};
    var y : int = p.x + p.y;
    var z : color = p.c
}