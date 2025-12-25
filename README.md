# PCG of dungeons in roguelike games
Presentation and evaluation of hybrid procedural generation algorithm for dungeons in roguelike games.

## Overview and goal
Creating good levels in roguelike games are challenging, as they need to be:
- **diverse**, like in random(-like) approaches (BSP, agent based generation), but...
- **controllable and high quality**, like in evolutionary or quality-diversity based approaches, but...
- also **fast to generate**, like in constructive approaches (grammar based generation), but...
- diverse (again), and so on.
  In project and then engineering thesis, I will implement hybrid procedural generation algorithm, that will fit those
  needs by combining MAP-Elites tree creation with grammar based grid mapping.

# Hybrid algorithm
The goal is to create different levels fast. One of the possible approaches is to split generation into two parts:
generating tree structure level "story" and then mapping it on grid. 
Tree structures can be generated using many different offline algorithms - in my case it will be Map-Elites - and 
results will be saved in files, so the time-consuming process of MAP-elites run is not a problem anymore.
Then, during the game, tree will be mapped on grid, which is a lot faster process with combining backtracking
and grammar based generation.

## Step 1: Generating tree structure of dungeon
In the first step of the algorithm, we generate tree structures using MAP-Elites algorithm and save it to `/levels` 
directory. It is crucial, to think about those trees as "user stories" and not grid layouts. 
Each node of the tree represents something that user "can do" on this path. E.g.:
<figure>
  <img src="images/tree.png" alt="tree">
  <figcaption><em>img 1: Example tree structure</em></figcaption>
</figure>
In this case, user starts in `S` (start) and needs to go to `X` (exit). 
However, player has multiple options to play this level:

* go to `E1` (enemy room 1) to get the treasure `T1` (treasure room 1), then go back to `S` and progress
* collect loot from `T2` (treasure room 2) and then go back to `S` and progress
* collect both treasures from `T1` and `T2` before progressing
* skip both treasures and go directly to `E2` (enemy room 2) and then to `X` (exit)
* from `E2`, user can also go fight optional enemy in `E3` (enemy room 3) before going to `X`.

Every time users plays the game, one tree saved from `/levels` directory is chosen (randomly or based on some criteria)
and then mapped on layout.

## Step 2: Layout
In the second step of the algorithm, chosen tree is map on layout. Layout is an intermediate representation of the level,
which is later mapped on grid. Layout consists of rooms as single grid cells and corridors as "pointers" to its parents.
The important thing is the fact, that from single tree, multiple layouts can be generated. For example, take a look at
2 example layouts created from tree from img 1:
<figure>
  <img src="images/layout1.png" alt="layout1">
  <figcaption><em>img 2: Example layout created from tree 1</em></figcaption>
</figure>
<figure>
  <img src="images/layout2.png" alt="layout2">
  <figcaption><em>img 3: Different layout created from tree 1</em></figcaption>
</figure>
Layout generation uses randomized backtracking algorithm, which tries to place rooms on grid one by one, starting from 
the root of the tree. MAP-Elites trees are created in a way, that they are always possible to be mapped on grid *easily*.

## Step 3: Mapping on grid
From grid layout, final grid representation of the level is created. Based on the trimmed layout 
(only rooms and corridors, trimmed "margins"), space of the grid is partitioned to give every node the same amount of space.
Then, every room is generated using grammar based generation, which allows to create rooms of different shapes and
with different placements of enemies and treasures inside.
<figure>
  <img src="images/grid.png" alt="grid">
  <figcaption><em>img 4: Example grid created from layout 1</em></figcaption>
</figure>

# Possible usage
After testing my algorithm on CodinGame platform, I would like to explore possibility of using it in standalone game.
Roguelike games are great fit for procedural generation, as they usually require many different levels to be played one
after another. Additionally, procedural generation can add to replayability of such games, as every new run can be 
different from the previous one. E.g. if I have 100 trees from MAP-Elites saved in the files and every game run
consists of 5 dungeons to be completed one after another (progressively harder), I can have ~20 trees for every
difficulty level; then every tree can be mapped to a grid with some randomness included.
This way, it is a great chance for every run to be unique and statistically 4/5 (~77%) of levels will have a different
parent from the previous ones.