# Hybrid generator idea

## Step 1: generate tree of nodes
This part can be done randomly, with ES, with SA... -> goal: do it with MAP-elites.

## Step 2: generate layout from tree
Place rooms on grid, using tree definition. Problems:
- overlapping rooms
- corridors crossing rooms -> go around?
- rooms out of bounds -> cut? but what, if it was exit?