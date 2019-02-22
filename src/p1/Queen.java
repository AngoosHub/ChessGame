/**
 * 
 */
package p1;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

import javafx.scene.image.Image;

/**
 * @author anguslin
 *
 */
public class Queen extends Piece implements Serializable {
	private String pieceName;
	private int player;
	private URL url;
	private boolean notBlocked;
	private Square[][] myChessBoard;
	
	/**
	 * Constructor for Queen piece.
	 * 
	 * @param playerNum owner of the piece.
	 */
	public Queen(int playerNum) {
		pieceName = "Queen";
		player = playerNum;
		getFile();
		createPiece();
	}

	/**
	 * Builds the piece using a chess image.
	 */
	public void createPiece() {
		try {
			// File file = new
			// File("/Users/anguslin/eclipse-workspace/Asn2a/src/chess-pieces/White-Pawn.png");
			File file = new File(url.getPath());
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
			url = getClass().getResource("../chess-pieces/Black-Queen.png");
		} else {
			url = getClass().getResource("../chess-pieces/White-Queen.png");
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
	public void validMove(Square[][] chessBoard, final int row, final int col) {
		myChessBoard = chessBoard;
		notBlocked = true;
		int y = row;
		int x = col;
		// Diagonal Down-Right check
		while (y < 8 && x < 8 && notBlocked) {
			if (y != row) {
				checkSquare(y, x);
			}
			y++;
			x++;
		}

		// Diagonal Down-Left check
		y = row;
		x = col;
		notBlocked = true;
		while (y < 8 && x > -1 && notBlocked) {
			if (y != row) {
				checkSquare(y, x);
			}
			y++;
			x--;
		}

		// Diagonal Up-Right check
		y = row;
		x = col;
		notBlocked = true;
		while (y > -1 && x < 8 && notBlocked) {
			if (y != row) {
				checkSquare(y, x);
			}
			y--;
			x++;
		}

		// Diagonal Up-Left check
		y = row;
		x = col;
		notBlocked = true;
		while (y > -1 && x > -1 && notBlocked) {
			if (y != row) {
				checkSquare(y, x);
			}
			y--;
			x--;
		}

		// Vertical Downward check
		y = row;
		x = col;
		notBlocked = true;
		while (y < 8 && notBlocked) {
			if (y != row) {
				checkSquare(y, x);
			}
			y++;
		}

		// Vertical Upward check
		y = row;
		x = col;
		notBlocked = true;
		while (y > -1 && notBlocked) {
			if (y != row) {
				checkSquare(y, x);
			}
			y--;
		}

		// Horizontal Right check
		y = row;
		x = col;
		notBlocked = true;
		while (x < 8 && notBlocked) {
			if (x != col) {
				checkSquare(y, x);
			}
			x++;
		}

		// Horizontal Left check
		y = row;
		x = col;
		notBlocked = true;
		while (x > -1 && notBlocked) {
			if (x != col) {
				checkSquare(y, x);
			}
			x--;
		}
	}
	
	/**
	 * Gets the name of the piece.
	 * @return piece name
	 */
	public String getPieceName() {
		return pieceName;
	}

	/**
	 * Compares the new square's piece with the old square piece and return
	 * comparison result.
	 * 
	 * @param row of square to check
	 * @param col of square to check
	 * @return int of 0-3 depend on relationship of pieces compared
	 */
	public void checkSquare(int row, int col) {
		if (myChessBoard[row][col].getPiece() == null) {
			myChessBoard[row][col].moveSelected();
		} else if (myChessBoard[row][col].getPiece().getPlayer() == this.getPlayer()) {
			myChessBoard[row][col].setKingCantMove(true);
			notBlocked = false;
		} else if (myChessBoard[row][col].getPiece().getPlayer() != this.getPlayer()) {
			myChessBoard[row][col].captureSelected();
			if (!(myChessBoard[row][col].getPiece().getPieceName() == "King")) {
				notBlocked = false;
			}
		}
	}
}
