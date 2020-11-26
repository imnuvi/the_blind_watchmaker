//main file

var block_size = 160;
let width_count;
let height_count;
var watches;

var x_cap = 360;
var y_cap = 360;
var z_cap = 360;
var u_cap = 360;
var v_cap = 360;
var col_cap = 254;

var mutation_rate = 0.02;

var rand_running = false;
var color_mode = "black";
var alt_color_mode = "white";



function start_random(){
  if (rand_running == true){
    frameRate(60);
    rand_running = false;
  }
  else{
    frameRate(10);
    rand_running = true;
  }
}

function flip_color_mode(){
  if (color_mode == "black"){
    // color_mode_button.style('value',"Dark theme");
    color_mode = "light";
    fill(color_mode);
    stroke('white');

  }
  else{
    // color_mode_button.style('value', "Light theme");
    color_mode = "black";
    fill(color_mode);
    stroke('black');
  }
}



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
  // resizeCanvas(windowWidth, windowHeight);
  // background('black');
  init();
}

window.addEventListener('resize',reportsize);

function mousePressed(){
  if (!rand_running){
    for (let i=0; i<watches.length ; i++){
      for (let j=0; j<watches[0].length; j++){
        rect(i*block_size,j*block_size,block_size);
        watches[i][j].selected();
      }
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
  var select_random = createButton("select random each generation");
  select_random.mousePressed(start_random);

  var color_mode_button = createButton("Light theme");
  color_mode_button.mousePressed(flip_color_mode);
  init();
  frameRate(30);
}

function draw(){
  if (rand_running == true){
    selection(watches[round(random(0, watches.length-1))][round(random(0,watches[0].length-1))])
  }
  if (color_mode == "black"){
    fill(color_mode);
    stroke('white');

  }
  else{
    fill(color_mode);
    stroke('black');
  }
  for (let i=0; i<watches.length ; i++){
    for (let j=0; j<watches[0].length; j++){
        // fill(color_mode);
        // stroke('white');
      rect(i*block_size,j*block_size,block_size);
      // stroke(watches[i][j].mag_lst[-3],watches[i][j].mag_lst[-2],watches[i][j].mag_lst[-1]);
      watches[i][j].show();
    }
  }
    // circle(mouseX,mouseY,20);
}
