# PCG of dungeons in roguelike games
Presentation and evaluation of hybrid procedural generation algorithm for dungeons in roguelike games.

## Overview and goal
Creating good levels in roguelike games are challenging, as they need to be:
- **diverse**, like in random(-like) approaches (BSP, agent based generation), but...
- **controllable and high quality**, like in evolutionary or quality-diversity based approaches, but...
- also **fast to generate**, like in constructive approaches (grammar based generation), but...
- diverse (again), and so on.

In project and then engineering thesis, I will implement hybrid procedural generation algorithm, that will fit those
needs by combining MAP-Elites tree creation with constructive (but with constrained randomness) dungeon generation from those trees.
Additionally, generated dungeons will be evaluated based on they quality, diversity and solvability and 
conclusions will be presented at the end of the semester and then explored further in my engineering thesis.

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
- https://dl.acm.org/doi/epdf/10.1145/3402942.3402945 (hybrid approach - grammar + cellular automata)
- maybe https://reingold.co/tidier-drawings.pdf for generating map from trees

## Plan
- [] Implement agents in CodinGame SDK
    - [x] Random
    - [x] Greedy
    - [x] A* (ignore enemies, minimalise path)
       - [x] consider enemies, minimalise risk
       - [.] consider enemies and dodging
       - [.] killer: prefer fighting over dodging
       - [.] collector: maximise reward (if points added)
- [ ] Implement hybrid dungeon generator in CodinGame SDK
    - [x] add fitness functions for generators
        - [x] quality
        - [ ] diversity
        - [x] generation ability (control)
    - [x] Map-Elites
    - [x] random tree + generator from tree
        - [.] diverse corridor shapes
        - [.] diverse room shapes
- [ ] Add features to dungeons
    - [x] enemies
        - [x] implement onCollision() 
        - [x] just standing in place
        - [.] movement paths
        - [.] different types
    - [x] rewards
        - [x] points
        - [.] potions
    - [ ] dodging enemies with stamina
    - [.] fighting enemies with mana
    - [.] potions
    - [.] collectables
- [x] trees serialization (or save in json?)
