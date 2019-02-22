# ChessGame

This is a chess game prototype made with Java and JavaFx using Eclipse IDE.
The goal is to reach as close to a fully functionable and playable chess game as possible.
Currently the proper moveset, pieces capture, turn change, save and load a game, and validing check and checkmate has been implemented.


## Folders & Classes Breakdown

In src file, the png images for chess pieces are stored in chess-pieces, and the code is stored inside p1.<br/>
When a game is saved, it is saved as a file called tempboard in the directory, and can be loaded to resume a game.<br/><br/>
The <b>GUI</b> class is the main class that executes the program. It extends Java Application, initializes all other classes, builds the board.
<br/><br/>
The <b>Square</b> class represents one of the tiles on the board, and extends Pane. Its either stores a pieces or represents no piece with null, and has functions to call the piece stored in it, and to change its color to highlight itself and give graphical indications to help the player.
<br/><br/>
The <b>Piece</b> class represents a chess piece by holding a png image. It contains functions to validate possible moves for the pieces, and if there are any opponent pieces it can capture.
<br/><br/>
Classes <b>Pawn ~ King</b> inherit the Piece class and overrides its methods to import the correct png image with its own move and capture validations.
<br/><br/>
The <b>Player</b> class represents a black or white player, and contains functions to build its own colored pieces on their starting locations on the board when called.
<br/><br/>
The <b>Rule</b> class holds the rules for validating each player's move on their turn. It is called whenever a square is clicked, and uses its coordinates to find it in a 2D Square array. It calls square's piece's "validMove"s and the square's "moveSelected" functions to highlight available moves and ensure the player makes a valid action. When an action is made, it calls endTurn to change the turn, and check if other player's King has been checked or checkmated.


## Authors
* **Angus Lin** - [AngoosHub](http://github.com/AngoosHub)

