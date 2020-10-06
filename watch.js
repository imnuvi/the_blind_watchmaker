function watch(x_count,y_count,randomness){
  this.randomness = randomness;
  this.mag = randomness;
  this.x_position = x_count * block_size;
  this.y_position = y_count * block_size;
  this.center_x = this.x_position + block_size/2;
  this.center_y = this.y_position + block_size/2;
}

watch.prototype.show = function(){
  circle(this.center_x,this.center_y,60+this.mag);
}

watch.prototype.selected = function(){
  m_distance = dist(mouseX,mouseY,this.center_x,this.center_y);
  if (m_distance < block_size){
    selection(this);
  }
}
