//main file

var block_size = 160;


function reportsize() {
  resizeCanvas(windowWidth, windowHeight);

  background('black');
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

  for (let i=0; i<width_count; i++){
    for (let j=0; j<height_count; j++){
      rect(i*block_size,j*block_size,block_size);
    }
  }
}

function setup(){
  init();
}

function draw(){
  circle(mouseX,mouseY,20);
}
