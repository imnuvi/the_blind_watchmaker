//main file

var block_size = 160;
let width_count;
let height_count;
var watches;

var x_cap = 90;
var y_cap = 90;
var z_cap = 90;
var u_cap = 90;
var v_cap = 90;
var col_cap = 254;

function create_watches(x_rows,y_rows){
  watches = new Array(x_rows)
  for (let i=0; i<x_rows; i++){
    watches[i] = new Array(y_rows);
  }
  fill_watches([45,45,45,45,45,150,150,150]);
}

function fill_watches(m_lst){
  for (let i=0; i<watches.length; i++){
    for (let j=0; j<watches[0].length; j++){
      // filler = map(random(0,1),0,1,-5,5);
      watches[i][j] = new watch(i,j,m_lst);
      // console.log(filler);
    }
  }
}


function reportsize() {
  resizeCanvas(windowWidth, windowHeight);
  background('black');
  init();
}

window.addEventListener('resize',reportsize);

function mousePressed(){
  for (let i=0; i<watches.length ; i++){
    for (let j=0; j<watches[0].length; j++){
      rect(i*block_size,j*block_size,block_size);
      watches[i][j].selected();
    }
  }
}

function selection(the_watch){
  the_mag = the_watch.mag_lst;
  fill_watches(the_mag);
}

function init(){
  ww = window.innerWidth;
  wh = window.innerHeight;
  canvas = createCanvas(ww,wh);
  background('black');
  canvas.position(0,0);
  canvas.style('z-index','-1');

  width_count = round(ww/block_size) + 2;
  height_count = round(wh/block_size) + 2;
  create_watches(width_count,height_count);
}

function setup(){
  init();
}

function draw(){
  for (let i=0; i<watches.length ; i++){
    for (let j=0; j<watches[0].length; j++){
      fill('white');
      rect(i*block_size,j*block_size,block_size);
      stroke(watches[i][j].mag_lst[-3],watches[i][j].mag_lst[-2],watches[i][j].mag_lst[-1]);
      watches[i][j].show();
    }
  }
    // circle(mouseX,mouseY,20);
}
