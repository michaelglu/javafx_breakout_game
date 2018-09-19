game
====

This CS308 project implements the game of Breakout.

Name: Michael Glushakov

### Timeline

Start Date: 8/31/2018

Finish Date: 9/6/2018

Hours Spent: 15-20

### Resources Used
   - lab_bounce code as a starting point, as well as the graphics included in the lab_bounce resource folder
   - Microsoft Word Shapes and icons to generate extra graphics
   - Office Hours on 9/3
### Running the Program

Main class: Main.java

Data files needed: 
- resources folder contains graphics

Key/Mouse inputs:
- Left/Right arrow keys move paddle
- Space dismisses splash screen and lets the ball go

Cheat keys:

- 2: skip to level 2
- 3: skip to level 3
- E: enter endless mode
- I: the ball can bounce off of the bottom wall

Known Bugs:

Extra credit:
- Endless mode: when there's less than 5 blocks left, new blocks and powerups are generated so the player can keep playing until they run out of lives 

### Notes
 - The blocks are created using an algorithm which loops through a 2D array, filling every other cell with a block; The rest of the cells have a 60% chance of being filled and a 30% chance of being a powerup block;
 - The number of lives resets every level
 - The effects of the powerups reset every level
 - There is no limit to the effects of powerups (in theory the paddle can get infinitely long, ball can get infinitely fast, you can get infinitely many lives)
 - Ball speed resets after a life is lost

### Impressions
- This project took a long time to debug, because JavaFX is probably not the best solution for game development. The biggest problem was the intersects() method which would fire not only when the shapes are touching but when they are also intersecting. This complicated the logic behing the ball-block collisions
- Overall this project was a good introduction to JavaFX
