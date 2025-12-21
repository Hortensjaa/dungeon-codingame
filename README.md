# Hybrid algorithm
The goal is to create different levels fast. One of the possible approaches is to split generation into two stages:
generating tree structure of dungeon and then mapping it on grid. Tree structures can be generated using many different
offline algorithms - in my case it will be Map-Elites - and results will be saved in files.
Then, during the game, tree will be mapped on grid, which is a lot faster process, but still can include some
randomness and diversity like corridors placement, room shapes and exact content (e.g. many different rooms with
3 enemies are possible to be generated).

## Stage one: Generating tree structure of dungeon
### Generating family of trees
I will be generation family of trees using Map-Elites algorithm, with following features:
- todo
### Calculating space needed
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
## Step 2: Generator
1. Generator takes tree structure as input
2. Partition grid based on space needed for whole tree
3. Place rooms inside partitions and connect them with corridors
4. * Optional: do minor changes to improve diversity between dungeons generated from the same tree
    - swap exit and player start
    - rotate whole grid
    - mirror whole grid
    - swap leaves (excluding exit)

## Possible usage
After testing my algorithm on CodinGame platform, I would like to explore possibility of using it in standalone game.
Roguelike games are great fit for procedural generation, as they usually require many different levels to be played one
after another. Additionally, procedural generation can add to replayability of such games, as every new run can be 
different from the previous one. E.g. if I have 100 trees from MAP-Elites saved in the files and every game run
consists of 5 dungeons to be completed one after another (progressively harder), I can have ~20 trees for every
difficulty level; then every tree can be mapped to a grid with some randomness included.
This way, it is a great chance for every run to be unique and statistically 4/5 (~77%) of levels will have a different
parent from the previous ones.