# the_blind_watchmaker
Blind watch maker app concept by Rickard dawkins using p5js<br>
play here [The Blind Watchmaker](https://cosmoglint.github.io/the_blind_watchmaker/)

![screenshot](./screenshot/shot.png)

What all of this means:

  the screen displays the sample space for each generation. Each cell is a living organism. Clicking on a cell means only that organism gets to the next generation. The next generation will have similar organisms with slight variations in their characteristics. If you desire a specific characteristic(color, orientation of any two arms, or the angle between branches) keep on selecting organisms that have that particular feature in subsequent generations. at one point the generations will smoothen out with only those with your desired characteristic. This best works if only one feature(characteristic) is chosen or preferred at a time.


The Blind Watchmaker:

  You are the selector, and decide which organism gets to see the next generation. random factors change the organism a little bit every generation, such that only those with features that please you as the selector survive subsequent generations evolve those features that help them survive.

  Change the rate of mutations by altering the mutation rate value in watch.js file


still in build phase, feel free to direct it in whichever way you want




Android app

Done:
  - Basic working version with fractals

TO-DO:
  - make mutation move forward in the next generations
  - store previous mutation stages and the previous parent
  - settings page for confiring mutation rate and other stuff( colors maybe?? )
  - scroll through infinite plane of organisms ( far into the future )


Implementation:
  - implementing the mutation propagation: 
      Instead of storing the entire object in memory, we just store a config for each branch, which contains the angle and the 
      length of itself, with which we can recreate the history ( history count can be decided later based on performance )
      so for a watchboard with 10 watches, 10 config objects will be saved and rendered based on the config
