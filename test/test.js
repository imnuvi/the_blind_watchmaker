function polymorph(){
  this.height = 80;
  this.angle_1 = 45;
  this.root = new branch(createVector(80,160),createVector(80,80));
  this.branches = [this.root,this.root.brancher(45),this.root.brancher(-45)];

}

polymorph.prototype.show = function(){
  for (var i=0; i<this.branches.length; i++){
    this.branches[i].show();
  }
}


function branch(begin,end){
  this.begin = begin;
  this.end = end;

  this.show = function(){
    line(this.begin.x,this.begin.y,this.end.x,this.end.y);
  }

  this.brancher = function(ang){
      var dir = p5.Vector.sub(this.end, this.begin);
      dir.rotate(ang);
      dir.mult(0.6);
      var new_end = p5.Vector.add(this.end,dir);

      var r = new branch(this.end,new_end);
      return r;
  }

  // this.branchb = function(){
  //     var dir = p5.Vector.sub(this.end, this.begin);
  //     dir.rotate(-ang);
  //     var new_end = p5.Vector.add(this.end,dir*0.5);
  //
  //     var l = new branch(this.end,new_end);
  //     return l;
  // }
}


function setup(){
  canvas = createCanvas(160,160);
  background('white');
  morph = new polymorph();
}
function draw(){
  // circle(80,80,80);
  morph.show();
}
