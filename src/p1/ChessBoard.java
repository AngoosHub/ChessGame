/**
 * 
 */
package p1;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Generates a starting board or creates a copy of the current instance of a board.
 * 
 * @author anguslin
 *
 */
public class ChessBoard {
	private Square[][] chessBoard;
	private Player player1;
	private Player player2;
	private Color color;
	private Rule rule;
	private Player currentPlayer;
	private GUI gui;
	
	/**
	 * Constructor that generates a starting board for the chess game.
	 * @param myGui gui to generate the board for
	 */
	public ChessBoard(GUI myGui) {
		gui = myGui;
		player1 = new Player(1);
		player2 = new Player(2);
		currentPlayer = player1;
		chessBoard = new Square[8][8];
		generateBoard();
		rule = new Rule(this, player1.getPlayer(), player2.getPlayer());
	}
	
	public ChessBoard(ChessBoard boardToCopy) {
		player1 = new Player(1);
		player2 = new Player(2);
		currentPlayer = player1;
		if (boardToCopy.getCurrentPlayer().getPlayer() == 2) {
			currentPlayer = player2;
		}
		chessBoard = new Square[8][8];
		generateCopyBoard(boardToCopy);
		rule = new Rule(this, player1.getPlayer(), player2.getPlayer());
	}

	/**
	 * Constructor for creating a copy of a chessboard.
	 * @param boardToCopy the object to duplicate
	 */
//	public ChessBoard(ChessBoard boardToCopy) {
//		gui = new GUI();
//		chessBoard = new Square[8][8];
//		player1 = new Player(1);
//		player2 = new Player(2);
//		currentPlayer = new Player(boardToCopy.getCurrentPlayer().getPlayer());
//		//chessBoard = boardToCopy.getBoard();
//		for (int y = 0; y < 8; y++) {
//			System.arraycopy(boardToCopy.getBoard()[y], 0, chessBoard[y], 0, boardToCopy.getBoard()[y].length);
//		}
//	}

	/**
	 * Gets the reference to the square of given coordinate on this chessboard.
	 * @param y the row
	 * @param x the col
	 * @return the Square
	 */
	public Square getSquare(int y, int x) {
		return chessBoard[y][x];
	}

	/**
	 * Changes the square on the board to a new square.
	 * @param newSquare
	 */
	public void setSquare(Square newSquare) {
		chessBoard[newSquare.getSquareRow()][newSquare.getSquareColumn()] = newSquare;
	}

	public Square[][] getBoard() {
		return chessBoard;
	}

	/**
	 * Builds each square of the board and assigns them into an 8 by 8 grid, and
	 * interact when clicked by running through rule class for the correct action.
	 */
	public void generateBoard() {
		int xPos = 0;
		int yPos = 0;
		final int width = 100;
		final int height = 100;
		color = Color.BEIGE;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Square square = new Square(chessBoard, xPos, yPos, width, height, color, x, y);
				swapBoardColor();
				square.setPrefSize(100, 100);
				square.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent mouseEvent) {
						rule.runRule(square.getSquareRow(), square.getSquareColumn());
					}
				});
				Piece newPiece = generatePiece(y, x);
				square.setPiece(newPiece);
				chessBoard[y][x] = square;
			}
			swapBoardColor();
		}
	}
	
	/**
	 * Builds an empty chess board.
	 */
	public void generateCopyBoard(ChessBoard boardToCopy) {
		int xPos = 0;
		int yPos = 0;
		final int width = 100;
		final int height = 100;
		color = Color.BEIGE;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Square square = new Square(chessBoard, xPos, yPos, width, height, color, x, y);
				swapBoardColor();
				square.setPrefSize(100, 100);
//				square.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//					public void handle(MouseEvent mouseEvent) {
//						rule.runRule(square.getSquareRow(), square.getSquareColumn());
//					}
//				});
				if (boardToCopy.getBoard()[y][x].getPiece() != null) {
					Piece copyPiece = makeTestPiece(boardToCopy.getBoard()[y][x].getPiece());
					square.setPiece(copyPiece);
				}
				chessBoard[y][x] = square;
			}
			swapBoardColor();
		}
	}
	
	private Piece makeTestPiece(Piece piece) {
		String name = piece.getPieceName();
		int playerNum = piece.getPlayer();
		Piece copyPiece = null;
		if (name.equals("Pawn")) {
			copyPiece = new Pawn(playerNum);
		} else if (name.equals("Knight")) {
			copyPiece = new Knight(playerNum);
		} else if (name.equals("Rook")) {
			copyPiece = new Rook(playerNum);
		} else if (name.equals("Bishop")) {
			copyPiece = new Bishop(playerNum);
		} else if (name.equals("Queen")) {
			copyPiece = new Queen(playerNum);
		} else if (name.equals("King")) {
			copyPiece = new King(playerNum);
		}
		return copyPiece;
	}
	

	/**
	 * Creates a pieces for the square based on player number.
	 * 
	 * @param row of square to make piece
	 * @param col of square to make piece
	 * @return
	 */
	public Piece generatePiece(int row, int col) {
		Piece piece = null;
		if (row < 2) {
			return player2.generatePiece(row, col);
		} else if (row > 5) {
			return player1.generatePiece(row, col);
		}
		return piece;
	}

	/**
	 * Alters the color to make a checkboard pattern.
	 */
	private void swapBoardColor() {
		if (color == Color.BEIGE) {
			color = Color.DIMGRAY;
		} else {
			color = Color.BEIGE;
		}
	}
	
	/**
	 * Gets the reference to the rule of this chess board.
	 * @return rule
	 */
	public Rule getRule() {
		return rule;
	}
	
	/**
	 * Gets the reference to gui.
	 * @return gui
	 */
	public GUI getGui() {
		return gui;
	}
	
	/**
	 * Gets the current player of this chess board.
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Changes the currentPlayer and also changes the current player label on gui.
	 */
	public void changeCurrentPlayer() {
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}
		gui.changeTurn();
	}
	

}
