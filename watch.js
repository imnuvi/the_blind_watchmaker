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
  this.size_ratio = mag_lst[8];
  this.mutation_rate = mutation_rate;
  this.x_position = x_count * block_size;
  this.y_position = y_count * block_size;
  this.center_x = this.x_position + block_size/2;
  this.center_y = this.y_position + block_size/2;

  this.x_mixval = this.rander(this.mag_x, 1);
  this.y_mixval = this.rander(this.mag_y, 1);
  this.z_mixval = this.rander(this.mag_z, 1);
  this.u_mixval = this.rander(this.mag_u, 1);
  this.v_mixval = this.rander(this.mag_v, 1);
  this.x_inv_mixval = this.rander(this.mag_x, 1);
  this.y_inv_mixval = this.rander(this.mag_y, 1);
  this.z_inv_mixval = this.rander(this.mag_z, 1);
  this.u_inv_mixval = this.rander(this.mag_u, 1);
  this.v_inv_mixval = this.rander(this.mag_v, 1);
  this.col_r_mixval = this.rander(this.col_r,100);
  this.col_g_mixval = this.rander(this.col_g,100);
  this.col_b_mixval = this.rander(this.col_b,100);
  this.size_ratio_mixval = this.rander(this.size_ratio,0.02);

  this.x_val = this.check_range(this.x_mixval, 0, x_cap);
  this.y_val = this.check_range(this.y_mixval, 0, y_cap);
  this.z_val = this.check_range(this.z_mixval, 0, z_cap);
  this.u_val = this.check_range(this.u_mixval, 0, u_cap);
  this.v_val = this.check_range(this.v_mixval, 0, v_cap);
  this.x_inv_val = this.check_range(this.x_inv_mixval, 0, x_cap);
  this.y_inv_val = this.check_range(this.y_inv_mixval, 0, y_cap);
  this.z_inv_val = this.check_range(this.z_inv_mixval, 0, z_cap);
  this.u_inv_val = this.check_range(this.u_inv_mixval, 0, u_cap);
  this.v_inv_val = this.check_range(this.v_inv_mixval, 0, v_cap);
  this.col_r_val = this.check_range(this.col_r_mixval, 20, col_cap);
  this.col_g_val = this.check_range(this.col_g_mixval, 20, col_cap);
  this.col_b_val = this.check_range(this.col_b_mixval, 20, col_cap);
  this.size_ratio_val = this.check_range(this.size_ratio_mixval, size_ratio_dip, size_ratio_cap);

  // test_value for asymmetric renders find this in the branch called asymmetric
  this.test_val = ((this.v_mixval < v_cap) && (this.v_mixval > 0)) ? this.v_mixval : ((this.v_mixval > v_cap) ? v_cap : 0 );


  this.mag_lst = [ this.x_val , this.y_val , this.z_val, this.u_val, this.v_val, this.col_r_val, this.col_g_val, this.col_b_val, this.size_ratio_val];
  this.inv_mag_lst = [ this.x_inv_val , this.y_inv_val , this.z_inv_val, this.u_inv_val, this.v_inv_val, this.col_r_val, this.col_g_val, this.col_b_val, this.size_ratio_val];
  // this.root = new branch(createVector(block_size/2,block_size/2),createVector(block_size/2,block_size/8 + this.rander()));
  // this.root = new branch(createVector(this.center_x,this.center_y),createVector(this.center_x + block_size/2,this.center_y + block_size/8 + this.rander()));
  this.root = new branch(createVector(this.center_x,this.center_y - block_size/4),createVector(this.center_x, this.center_y ), this.size_ratio_val);
  this.branches = [this.root.brancher(this.v_val),this.root.brancher(-this.v_val)]
  for (let i=0; i<4; i++){
    for (let j=this.branches.length-1; j>=0; j--){
      if (!this.branches[j].finished){
        if (asymmetric){
          // print("yuh")
          this.branches.push(this.branches[j].brancher(this.mag_lst[i]));
          // this.branches.push(this.branches[j].brancher(this.inv_mag_lst[i]));
          // this.branches.push(this.branches[j].brancher(-this.mag_lst[i]));
          this.branches.push(this.branches[j].brancher(this.inv_mag_lst[i]));
        }
        else{
          this.branches.push(this.branches[j].brancher(this.mag_lst[i]));
          this.branches.push(this.branches[j].brancher(-this.mag_lst[i]))
        }
      }
      this.branches[j].finished = true;
    }
  }
}


watch.prototype.check_range = function(val, min_val, max_val){
  res = ((val < max_val) && (val > min_val)) ? val : ((val > max_val) ? max_val : min_val );
  return res
}

watch.prototype.rander = function(mag, deviation=100){
    rand = randomGaussian(0,10);
    val = mag + rand * deviation * mutation_rate;
    return val
}



// watch.prototype.rander = function(){
//     return randomGaussian(0,10);
// }

watch.prototype.show = function(){
  // fill(this.mag_lst[2],this.mag_lst[3],this.mag_lst[4]);
  // ellipse(this.center_x,this.center_y,this.mag_lst[0],this.mag_lst[1]);
  thel = this.mag_lst.length;
  stroke(this.mag_lst[5],this.mag_lst[6],this.mag_lst[7]);
  // stroke('red');
  // strokeWeight(5);
  for (var i=0; i<this.branches.length; i++){
    this.branches[i].show();
  }
}

watch.prototype.selected = function(){
  m_distance = dist(mouseX,mouseY,this.center_x,this.center_y);
  if (m_distance < block_size/2){
    selection(this);
  }
}
