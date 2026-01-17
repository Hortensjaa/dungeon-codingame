# MVP

> [!WARNING]  
> New repo containing code migrated from CodinGameSDK to standalone LibGDX project: https://github.com/Hortensjaa/DungeonGame

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
1. [x] Moving enemies
2. [x] Different room shapes (for bigger rooms)
3. [x] Different corridor shapes

## What will be done (but not sure if in project or thesis)?
1. [ ] Better logic of room creation than random placement of enemies and rewards
