function watch(x_count,y_count,mag_lst){
  // this.randomness = randomness;
  this.mag_x = mag_lst[0];
  this.mag_y = mag_lst[1];
  this.x_position = x_count * block_size;
  this.y_position = y_count * block_size;
  this.center_x = this.x_position + block_size/2;
  this.center_y = this.y_position + block_size/2;
  this.mag_lst = [this.mag_x + this.rander(),this.mag_y + this.rander()];
}

watch.prototype.rander = function(){
    return randomGaussian(0,10);
}

watch.prototype.show = function(){
  ellipse(this.center_x,this.center_y,this.mag_lst[0],this.mag_lst[1]);
}

watch.prototype.selected = function(){
  m_distance = dist(mouseX,mouseY,this.center_x,this.center_y);
  if (m_distance < block_size){
    selection(this);
  }
}
