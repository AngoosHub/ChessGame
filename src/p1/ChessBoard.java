/**
 * 
 */
package p1;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Creates a blank board or a copy of the current instance of a board.
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
		chessBoard = boardToCopy.getBoard();
	}

	public Square getSquare(int y, int x) {
		return chessBoard[y][x];
	}

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
	
	public Rule getRule() {
		return rule;
	}
	
	public GUI getGui() {
		return gui;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void changeCurrentPlayer() {
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}
		gui.changeTurn();
	}
	

}
