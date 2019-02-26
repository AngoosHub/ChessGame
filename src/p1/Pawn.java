/**
 * 
 */
package p1;

import java.io.File;
import java.net.URL;

import javafx.scene.image.Image;

/**
 * @author anguslin
 *
 */
public class Pawn extends Piece {
	private String pieceName;
	private int player;
	private URL url;
	private Square[][] myChessBoard;

	/**
	 * Constructor for Pawn piece.
	 * 
	 * @param playerNum owner of the piece.
	 */
	public Pawn(int playerNum) {
		pieceName = "Pawn";
		player = playerNum;
		getFile();
		createPiece();
	}
	
	/**
	 * Creates a test version of the piece skipping processing image file.
	 * @param playerNum
	 * @param test
	 */
	public Pawn(int playerNum, String test) {
		pieceName = "Pawn";
		player = playerNum;
	}

	/**
	 * Builds the piece using a chess image.
	 */
	public void createPiece() {
		try {
			// File file = new
			// File("/Users/anguslin/eclipse-workspace/Asn2a/src/chess-pieces/White-Pawn.png");
			File file = new File(url.getPath());
			// System.out.println(file.toURI().toString());
			Image image = new Image(file.toURI().toString());
			this.setImage(image);
			this.setFitWidth(100);
			this.setPreserveRatio(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the correct image file based on player color.
	 */
	protected void getFile() {
		if (player == 2) {
			url = getClass().getResource("../chess-pieces/Black-Pawn.png");
		} else {
			url = getClass().getResource("../chess-pieces/White-Pawn.png");
		}
	}

	/**
	 * Gets the player number.
	 */
	public int getPlayer() {
		return player;
	}

	/**
	 * Checks the valid moves allowed to this piece. Makes sure the piece is not
	 * blocked by and ally and cannot move through pieces.
	 * 
	 * @param chessBoard the current arrangement of the board
	 * @param row        of the square the piece is on
	 * @param col        of the square the piece is on
	 */
	public void validMove(Square[][] chessBoard, int row, int col) {
		if (getPlayer() == 2) {
			blackPawnMove(chessBoard, row, col);
		} else {
			whitePawnMove(chessBoard, row, col);
		}
	}

	/**
	 * Validates the moves and capture for the black pawn, including diagonal
	 * capture.
	 * 
	 * @param chessBoard current state of board
	 * @param row        of the square
	 * @param col        of the square
	 */
	public void blackPawnMove(Square[][] chessBoard, int row, int col) {
		myChessBoard = chessBoard;
		int y = row;
		int x = col;
		int check = 0;
		if (row != 7) {
			if (y == 1) {
				while (y < 3 && x < 8 && check == 0) {
					y++;
					checkSquare(y, x);
				}
			} else {
				y++;
				checkSquare(y, x);
			}
			if (row < 7 && col < 7 && col >= 0) {
				y = row + 1;
				x = col + 1;
				checkCapture(y, x);
			}
			if (row < 7 && col < 8 && col > 0) {
				y = row + 1;
				x = col - 1;
				checkCapture(y, x);
			}
		}

	}

	/**
	 * Validates the moves and capture for the white pawn, including diagonal
	 * capture.
	 * 
	 * @param chessBoard current state of board
	 * @param row        of the square
	 * @param col        of the square
	 */
	public void whitePawnMove(Square[][] chessBoard, int row, int col) {
		myChessBoard = chessBoard;
		int y = row;
		int x = col;
		int check = 0;
		if (row != 0) {
			if (y == 6) {
				while (y > 4 && x < 8 && check == 0) {
					y--;
					checkSquare(y, x);
				}
			} else {
				y--;
				checkSquare(y, x);
			}
			if (row > 0 && col < 7 && col >= 0) {
				y = row - 1;
				x = col + 1;
				checkCapture(y, x);
			}
			if (row > 0 && col < 8 && col > 0) {
				y = row - 1;
				x = col - 1;
				checkCapture(y, x);
			}
		}

	}

	/**
	 * Compares the new square's piece with the old square piece and checks if its a
	 * valid move.
	 * 
	 * @param row of square to check
	 * @param col of square to check
	 */
	public void checkSquare(int row, int col) {
		if (myChessBoard[row][col].getPiece() == null) {
			myChessBoard[row][col].moveSelected();
			
		} else if (myChessBoard[row][col].getPiece().getPlayer() == this.getPlayer()) {

		} else if (myChessBoard[row][col].getPiece().getPlayer() != this.getPlayer()) {

		}
	}

	/**
	 * Gets the name of the piece.
	 * 
	 * @return piece name
	 */
	public String getPieceName() {
		return pieceName;
	}

	/**
	 * Compares the new square's piece with the old square piece and checks if its a
	 * valid move.
	 * 
	 * @param row of square to check
	 * @param col of square to check
	 */
	public void checkCapture(int row, int col) {
		if (myChessBoard[row][col].getPiece() == null) {
			

		} else if (myChessBoard[row][col].getPiece().getPlayer() == this.getPlayer()) {
			

		} else if (myChessBoard[row][col].getPiece().getPlayer() != this.getPlayer()) {
			myChessBoard[row][col].captureSelected();
		}

	}
}
