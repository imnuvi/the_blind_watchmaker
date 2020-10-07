function watch(x_count,y_count,mag_lst){
  // this.randomness = randomness;
  this.mag_x = mag_lst[0];
  this.mag_y = mag_lst[1];
  this.col_r = mag_lst[2];
  this.col_g = mag_lst[3];
  this.col_b = mag_lst[4];
  this.x_position = x_count * block_size;
  this.y_position = y_count * block_size;
  this.center_x = this.x_position + block_size/2;
  this.center_y = this.y_position + block_size/2;

  this.x_mixval = this.mag_x + this.rander();
  this.y_mixval = this.mag_y + this.rander();
  this.col_r_mixval = this.col_r + this.rander()*2;
  this.col_g_mixval = this.col_g + this.rander()*2;
  this.col_b_mixval = this.col_b + this.rander()*2;

  this.x_val = ((this.x_mixval < x_cap) && (this.x_mixval > 0)) ? this.x_mixval : ((this.x_mixval > x_cap) ? x_cap : 0 );
  this.y_val = ((this.y_mixval < y_cap) && (this.y_mixval > 0)) ? this.y_mixval : ((this.y_mixval > x_cap) ? y_cap : 0 );
  this.col_r_val = ((this.col_r_mixval < col_cap) && (this.col_r_mixval > 0)) ? this.col_r_mixval : ((this.col_r_mixval > col_cap) ? col_cap : 10 );
  this.col_g_val = ((this.col_g_mixval < col_cap) && (this.col_g_mixval > 0)) ? this.col_g_mixval : ((this.col_g_mixval > col_cap) ? col_cap : 10 );
  this.col_b_val = ((this.col_b_mixval < col_cap) && (this.col_b_mixval > 0)) ? this.col_b_mixval : ((this.col_b_mixval > col_cap) ? col_cap : 10 );
  this.mag_lst = [ this.x_val , this.y_val , this.col_r_val, this.col_g_val, this.col_b_val];
}

watch.prototype.rander = function(){
    return randomGaussian(0,10);
}

watch.prototype.show = function(){
  fill(this.mag_lst[2],this.mag_lst[3],this.mag_lst[4]);
  ellipse(this.center_x,this.center_y,this.mag_lst[0],this.mag_lst[1]);
}

watch.prototype.selected = function(){
  m_distance = dist(mouseX,mouseY,this.center_x,this.center_y);
  if (m_distance < block_size){
    selection(this);
  }
}
