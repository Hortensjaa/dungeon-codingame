# PCG of dungeons in games
Comprehension of different techniques by quality, diversity and solvability by different agents

## Overview and goal
In project and then engineering thesis, I will implement hybrid procedural generation algorithm, that will fit needs
for roguelike games - fast generation and great diversity.
Additionally, generated dungeons will be evaluated based on they quality and diversity. Conclusions will be presented at the end of the semester and then explored further in my engineering thesis.

## MVP
Dungeon as group of rooms connected by corridors, with enemies (standing or moving in the line and back) needed to be avoided to safely pass through exit. 
Player at exit = win.
### Extras
Some ideas, in order corresponding to how likely it is to be implemented in the project part:
1. Procedural generation of enemies paths of movement
2. Option of dodging enemies, but with limited stamina
3. Option of fighting enemies, but with limited mana
4. Potions (add extra life, add mana)
5. Create optimisation problem out of it (e.g. collect as many coins as you can and then leave dungeon safetly)

--- content under this line has minimal chance and is more my idea to develop project further into standalone game
- More advanced stats and types of enemies and player like HP, ATK etc.
- Collecting items that boost those stats
- Doors and keys, doors that only opens form one side (can be added in generator)

## Part 1: AI4Games PCG project
The project will be the contribution to CodinGame to take advantage of provided system of testing many agents on different levels.

## Part 2: Engineering thesis
Probably will include more complicated dungeons, but that isn't the point. Most importantly, it will comprehend different approaches to procedurally generated dungeons with their pros, cons, examples and potential problems, based on different metricts OR if I find some algorithm that is jest the best in my opinion, it will be overview of it :).

## Sources
- Procedural Content Generation in Games - a comprehensive textbook edited by Noor Shaker, Julian Togelius, and Mark J. Nelson (https://www.pcgbook.com/)
- https://research.tudelft.nl/en/publications/procedural-generation-of-dungeons/ (link: https://www.pcgbook.com/)
- https://www.researchgate.net/publication/353921862_Procedural_Dungeon_Generation_A_Survey (overview paper)
- https://arxiv.org/abs/2202.09301 (also map elites)
- https://arxiv.org/abs/2107.06638 (behavior trees)
- https://aeau.github.io/assets/papers/2020/tolFlod2020-chiplay01.pdf (about making sense, not only correct layout - interesting)
- https://openresearch-repository.anu.edu.au/items/abb3546d-81e0-4d12-aefa-714b1c29cff6 (hybrid approach - grammar + cellular automata)
- https://pure.ul.ie/en/publications/procedural-content-generation-for-games-using-grammatical-evoluti/ (evolution on grammars) 
- maybe https://reingold.co/tidier-drawings.pdf for generating map from trees

## Plan
- [] Implement agents in CodinGame SDK
    - [x] Random
    - [x] Greedy
    - [x] A* (ignore enemies, minimalise path)
       - [ ] consider enemies, minimalise risk
       - [ ] consider enemies and dodging
       - [.] killer: prefer fighting over dodging
       - [.] collector: maximise reward (if points added)
- [ ] Implement hybrid dungeon generator in CodinGame SDK
    - [ ] add fitness functions for generators
        - [ ] quality
        - [ ] diversity
        - [ ] solvability by different agents
    - [ ] Map-Elites
    - [x] random tree + generator from tree
- [ ] Add features to dungeons
    - [ ] enemies
        - [ ] just standing in place
        - [ ] movement paths
        - [.] different types
    - [ ] dodging enemies with stamina
    - [.] fighting enemies with mana
    - [.] potions
    - [.] collectables

## Hybrid algorithm
The goal is to create fast a lot of different levels. One of the possible approaches is to split generation into two stages:
generating tree structure of dungeon and then mapping it on grid. Tree structures can be generated using many different
offline algorithms - in my case it will be Map-Elites and results will be saved in files. 
Then, during the game, tree will be mapped on grid, which is a lot faster process, but still can include some
randomness and diversity like corridors placement, room shapes and exact contents (e.g. many different rooms with
3 enemies are possible to be generated).
### Stage one: Generating tree structure of dungeon
#### Generating family of trees
Map-Elites
#### Calculating space needed
recursion from leaves to root calculating how much place on the left, right, top and bottom is needed to draw subtrees;
in this calculation, we treat space needed for every node as 1 - it will be mapped on grid in next space.
```
leaf.spaceRight = 0
leaf.spaceLeft = 0
leaf.spaceTop = 0
leaf.spaceDown = 0
...
node.spaceRight = max(rightChild.spaceRight + 1, leftChild.spaceRight - 1, topChild.spaceRight, bottomChild.spaceRight)
node.spaceLeft = max(rightChild.spaceLeft - 1, leftChild.spaceLeft + 1, topChild.spaceLeft, bottomChild.spaceLeft)
node.spaceTop = max(rightChild.spaceTop, leftChild.spaceTop, topChild.spaceTop + 1, bottomChild.spaceTop - 1)
node.spaceDown = max(rightChild.spaceDown, leftChild.spaceDown, topChild.spaceDown - 1, bottomChild.spaceDown + 1)
```
### Generator
1. Pass tree to generator
2. Partition grid based on space needed for whole tree
3. Place rooms inside partitions and connect them with corridors
4. * Optional: do minor changes to improve diversity between dungeons generated from the same tree
    - swap exit and player start
    - rotate whole grid
    - mirror whole grid
    - swap leaves (excluding exit)
