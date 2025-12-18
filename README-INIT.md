# PCG of dungeons in games
Comprehension of different techniques by quality, diversity and solvability by different agents

## Overview and goal
In project and then engineering thesis, I will test different dungeon generation methods against different AI agents (project part) by CodinGame SDK. 
Additionally, generated dungeons will be evaluated based on they quality and diversity. Coclusions will be presented at the end of the semester and then explored further in my engineering thesis.

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

## Part 1: AI4Games PCG project
The project will be the contribution to CodinGame to take advantage of provided system of testing many agents on different levels.

## Part 2: Engineering thesis
Probably will include more complicated dungeons, but that isn't the point. Most importantly, it will comprehend different approaches to procedurally generated dungeons with their pros, cons, examples and potential problems, based on different metricts OR if I find some algorithm that is jest the best in my opinion, it will be overview of it :).

## Sources
- Procedural Content Generation in Games - a comprehensive textbook edited by Noor Shaker, Julian Togelius, and Mark J. Nelson (https://www.pcgbook.com/)
- https://research.tudelft.nl/en/publications/procedural-generation-of-dungeons/
- https://www.researchgate.net/publication/353921862_Procedural_Dungeon_Generation_A_Survey
- https://arxiv.org/abs/2003.03377 (map elites with constraints)
- https://arxiv.org/abs/2202.09301 (also map elites)
- https://arxiv.org/abs/2107.06638 (behavior trees)
- https://aeau.github.io/assets/papers/2020/tolFlod2020-chiplay01.pdf (about making sense, not only correct layout - interesting)
- https://openresearch-repository.anu.edu.au/items/abb3546d-81e0-4d12-aefa-714b1c29cff6 (hybrid approach - grammar + cellular automata)
- https://pure.ul.ie/en/publications/procedural-content-generation-for-games-using-grammatical-evoluti/ (evolution on grammars) 

## Plan
- [] Implement agents in CodinGame SDK
    - [x] Random
    - [x] Greedy
    - [x] A* (ignore , minimalise path)
       - [ ] consider enemies, minimalise risk
       - [ ] consider enemies and dodging
       - [.] killer: prefer fighting over dodging
       - [.] collector: maximise reward (if points added)
- [ ] Implement dungeon generators in CodinGame SDK
    - [x] Random
    - [x] Binary space partitioning
    - [.] Cellular automata
    - [ ] add fitness functions for generators
        - [ ] quality
        - [ ] diversity
        - [ ] solvability by different agents
    - [ ] Map-Elites
    - [ ] add grammar-based generator
    - [ ] (goal) implement hybrid generator, with map-elites for grammar rules and then some online algorithm generation,
- [ ] Add features to dungeons
    - [ ] enemies
        - [ ] just standing in place
        - [ ] movement paths
        - [.] different types
    - [ ] dodging enemies with stamina
    - [.] fighting enemies with mana
    - [.] potions
    - [.] collectables
