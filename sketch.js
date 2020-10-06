//main file

var block_size = 160;
let width_count;
let height_count;
var watches;

function create_watches(width_n,height_n){
  watches = new Array(width_n);
  for (let j=0; j<width_n; j++){
      watches[j] = new Array(height_n);
    }
  fill_watches();
}


function fill_watches(){
  fill('white');
  for (let i=0; i<width_count; i++){
    for (let j=0; j<height_count; j++){
      watches[i][j] = new watch(i*block_size,j*block_size);
    }
  }
}

function reportsize() {
  resizeCanvas(windowWidth, windowHeight);
  background('black');
  init();
}

window.addEventListener('resize',reportsize);


function init(){
  ww = window.innerWidth;
  wh = window.innerHeight;
  canvas = createCanvas(ww,wh);
  background('black');
  canvas.position(0,0);
  canvas.style('z-index','-1');

  width_count = ww/block_size;
  height_count = wh/block_size;
  create_watches();

}

function setup(){
  init();
}

function draw(){
  circle(mouseX,mouseY,20);

  for (let i=0; i<width_count; i++){
    for (let j=0; j<height_count; j++){
      watches[i][j].show();
    }
  }
}
