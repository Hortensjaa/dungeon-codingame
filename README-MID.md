# MVP

## What have been done in MVP? (27.12.2025)
1. Generator is implemented as hybrid of MAP-Elites for behavioral tree creation + constructive methods 
(explained in [main README](/README.md)).
2. In [/levels directory](levels) there are 100 levels generated for different MAP-Elites runs, 
with different room sizes (to check scalability)
3. A* agent implemented that prefer to avoid enemies and try to collect rewards
4. Enemies and rewards are randomly placed in rooms in generation phase; enemies are decreasing score, not kill

## What needs to be done to finish project part?
1. Statistics with quality, diversity etc. of generated levels and presentation

### Small "nice-to-have's"
What might be done quick, and probably will be done in January:
1. Moving enemies
2. Different room shapes (for bigger rooms)
3. Different corridor shapes
4. Better placement of enemies and rewards (not fully random)

## What will be done (but not sure if in project or thesis)?
1. More player's actions (dodging, fighting)
2. Move game from CodinGame to standalone using LibGDX or JavaFX