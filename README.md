HorsePower
==========

HorsePower
A Horse Power Program to evaluate horse power.
 +***--- todo 4/24/14 ---***
 +
 +-Implemented Alpha beta pruning (minimax with more variables).
 +
 +*evaluation method needs to be worked on. Currently no idea on how to evaluate each move.
 +perhaps on a priority/best move type of deal?:
 +Checkers priorities:
 +
 +if next move = Get King -> evaluation = 10
 +if next move = Jump -> evaluation = INF (must take, board won't return a list of moves if a jump is available)
 +if next move can jump 2x -> evaluation = 20 (move list will show a double jump if it is avail)
 +if next move can jump 3x -> evaluation = 30 etc.
 +
 +
 +
  
  ***--- todo 4/23/14 ---***
  
  
  * implement min max -focus-
* research alpha beta pruning
* implement heuristic evaluation function -focus-
* research and implement learning
* research distributed systems - BECAT ; <- tyler
* research self made libraries - no premade libraries for opening/end moves
* look into playing against itself/others to learn
* 

* possibly look into an 'illegal move' error. Looks right on string output and move output
