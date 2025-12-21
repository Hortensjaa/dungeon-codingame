# PCG of dungeons in games
Comprehension of different techniques by quality, diversity and solvability by different agents

## Overview and goal
In project and then engineering thesis, I will implement hybrid procedural generation algorithm, that will fit needs
for roguelike games - fast generation and great diversity.
Additionally, generated dungeons will be evaluated based on they quality, diversity and solvability. 
Conclusions will be presented at the end of the semester and then explored further in my engineering thesis.

## MVP
Dungeon as group of rooms connected by corridors, with enemies (standing or moving in the line and back) needed to be 
avoided to safely pass through exit. 
Player at exit = win.
### Extras
Some ideas, in order corresponding to how likely it is to be implemented in the project part:
1. Procedural generation of enemies paths of movement
2. Option of dodging enemies, but with limited stamina
3. Option of fighting enemies, but with limited mana
4. Potions (add extra life, add mana)
5. Create optimisation problem out of it (e.g. collect as many coins as you can and then leave dungeon safely)

--- content under this line has minimal chance and is more my idea to develop project further into standalone game
- More advanced stats and types of enemies and player like HP, ATK etc.
- Collecting items that boost those stats
- Doors and keys, doors that only opens form one side (can be added in generator)

## Part 1: AI4Games PCG project
The project will be the contribution to CodinGame to take advantage of provided system of testing many agents on different levels.
Also, I will sum up the statistics and present them at the end of the semester along with some screenshots of generated 
grids vs. trees they were generated from.

## Part 2: Engineering thesis
Probably will include more complicated and bigger (without grid size constraint from CodinGame) dungeons, with more features.
The most important part will be the analysis of my generator and its outputs.

## Sources
- Procedural Content Generation in Games - a comprehensive textbook edited by Noor Shaker, Julian Togelius, and Mark J. Nelson (https://www.pcgbook.com/)
- https://www.researchgate.net/publication/353921862_Procedural_Dungeon_Generation_A_Survey (overview paper)
- https://arxiv.org/abs/2202.09301 (map elites in dungeon generation)
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
        - [ ] diverse corridor shapes
        - [ ] diverse room shapes
- [ ] Add features to dungeons
    - [ ] enemies
        - [ ] just standing in place
        - [ ] movement paths
        - [.] different types
    - [ ] dodging enemies with stamina
    - [.] fighting enemies with mana
    - [.] potions
    - [.] collectables
