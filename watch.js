function watch(x_position,y_position){
  this.x_position = x_position;
  this.y_position = y_position
}

this.prototype.show = function(){
  circle(this.x_position,this.y_position,30);
}
