function watch(x_count,y_count,mag_lst){
  // this.randomness = randomness;
  this.mag_x = mag_lst[0];
  this.mag_y = mag_lst[1];
  this.mag_z = mag_lst[2];
  this.mag_u = mag_lst[3];
  this.mag_v = mag_lst[4];
  this.col_r = mag_lst[5];
  this.col_g = mag_lst[6];
  this.col_b = mag_lst[7];
  this.x_position = x_count * block_size;
  this.y_position = y_count * block_size;
  this.center_x = this.x_position + block_size/2;
  this.center_y = this.y_position + block_size/2;

  this.x_mixval = this.mag_x + this.rander()*0.2;
  this.y_mixval = this.mag_y + this.rander()*0.2;
  this.z_mixval = this.mag_z + this.rander()*0.2;
  this.u_mixval = this.mag_u + this.rander()*0.2;
  this.v_mixval = this.mag_v + this.rander()*0.2;
  this.col_r_mixval = this.col_r + this.rander()*2;
  this.col_g_mixval = this.col_g + this.rander()*2;
  this.col_b_mixval = this.col_b + this.rander()*2;

  this.x_val = ((this.x_mixval < x_cap) && (this.x_mixval > 0)) ? this.x_mixval : ((this.x_mixval > x_cap) ? x_cap : 0 );
  this.y_val = ((this.y_mixval < y_cap) && (this.y_mixval > 0)) ? this.y_mixval : ((this.y_mixval > y_cap) ? y_cap : 0 );
  this.z_val = ((this.z_mixval < z_cap) && (this.z_mixval > 0)) ? this.z_mixval : ((this.z_mixval > z_cap) ? z_cap : 0 );
  this.u_val = ((this.u_mixval < u_cap) && (this.u_mixval > 0)) ? this.u_mixval : ((this.u_mixval > u_cap) ? u_cap : 0 );
  this.v_val = ((this.v_mixval < v_cap) && (this.v_mixval > 0)) ? this.v_mixval : ((this.v_mixval > v_cap) ? v_cap : 0 );
  this.col_r_val = ((this.col_r_mixval < col_cap) && (this.col_r_mixval > 0)) ? this.col_r_mixval : ((this.col_r_mixval > col_cap) ? col_cap : 10 );
  this.col_g_val = ((this.col_g_mixval < col_cap) && (this.col_g_mixval > 0)) ? this.col_g_mixval : ((this.col_g_mixval > col_cap) ? col_cap : 10 );
  this.col_b_val = ((this.col_b_mixval < col_cap) && (this.col_b_mixval > 0)) ? this.col_b_mixval : ((this.col_b_mixval > col_cap) ? col_cap : 10 );
  this.mag_lst = [ this.x_val , this.y_val , this.z_val, this.u_val, this.v_val, this.col_r_val, this.col_g_val, this.col_b_val];
  // this.root = new branch(createVector(block_size/2,block_size/2),createVector(block_size/2,block_size/8 + this.rander()));
  // this.root = new branch(createVector(this.center_x,this.center_y),createVector(this.center_x + block_size/2,this.center_y + block_size/8 + this.rander()));
  this.root = new branch(createVector(this.center_x,this.center_y),createVector(this.center_x, this.center_y + block_size/4));
  this.branches = [this.root.brancher(this.u_val),this.root.brancher(this.v_val)]
  for (let i=0; i<4; i++){
    for (let j=this.branches.length-1; j>=0; j--){
      if (!this.branches[j].finished){
        this.branches.push(this.branches[j].brancher(this.mag_lst[i]));
        this.branches.push(this.branches[j].brancher(-this.mag_lst[i]));
      }
      this.branches[j].finished = true;
    }
  }
}

watch.prototype.rander = function(){
    return randomGaussian(0,10);
}

watch.prototype.show = function(){
  // fill(this.mag_lst[2],this.mag_lst[3],this.mag_lst[4]);
  // ellipse(this.center_x,this.center_y,this.mag_lst[0],this.mag_lst[1]);
  thel = this.mag_lst.length;
  stroke(this.mag_lst[thel-3],this.mag_lst[thel-2],this.mag_lst[thel-1]);
  // stroke('red');
  // strokeWeight(5);
  for (var i=0; i<this.branches.length; i++){
    this.branches[i].show();
  }
}

watch.prototype.selected = function(){
  m_distance = dist(mouseX,mouseY,this.center_x,this.center_y);
  if (m_distance < block_size){
    selection(this);
  }
}
