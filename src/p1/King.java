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
public class King extends Piece implements Serializable {
	private String pieceName;
	private int player;
	private URL url;
	private int count;
	private Square[][] myChessBoard;
	//private int currentPlayer;
	
	/**
	 * Constructor for King piece.
	 * @param playerNum owner of the piece.
	 */
	public King(int playerNum) {
		pieceName = "King";
		player = playerNum;
		getFile();
		createPiece();
	}
	
	/**
	 * Builds the piece using a chess image.
	 */
	public void createPiece() {
		try {
			//File file = new File("/Users/anguslin/eclipse-workspace/Asn2a/src/chess-pieces/White-Pawn.png");
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
			url = getClass().getResource("../chess-pieces/Black-King.png");
		} else {
			url = getClass().getResource("../chess-pieces/White-King.png");
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
		count = 0;
		int y = row;
		int x = col;
		// Diagonal Down-Right check
		while (y < 8 && x < 8 && count < 2) {
			if (y != row) {
				checkSquare(y, x);
			}
			y++;
			x++;
			count++;
		}

		// Diagonal Down-Left check
		y = row;
		x = col;
		count = 0;
		while (y < 8 && x > -1 && count < 2) {
			if (y != row) {
				checkSquare(y, x);
			}
			y++;
			x--;
			count++;
		}

		// Diagonal Up-Right check
		y = row;
		x = col;
		count = 0;
		while (y > -1 && x < 8 && count < 2) {
			if (y != row) {
				checkSquare(y, x);
			}
			y--;
			x++;
			count++;
		}

		// Diagonal Up-Left check
		y = row;
		x = col;
		count = 0;
		while (y > -1 && x > -1 && count < 2) {
			if (y != row) {
				checkSquare(y, x);
			}
			y--;
			x--;
			count++;
		}

		// Vertical Downward check
		y = row;
		x = col;
		count = 0;
		while (y < 8 && count < 2) {
			if (y != row) {
				checkSquare(y, x);
			}
			y++;
			count++;
		}

		// Vertical Upward check
		y = row;
		x = col;
		count = 0;
		while (y > -1 && count < 2) {
			if (y != row) {
				checkSquare(y, x);
			}
			y--;
			count++;
		}

		// Horizontal Right check
		y = row;
		x = col;
		count = 0;
		while (x < 8 && count < 2) {
			if (x != col) {
				checkSquare(y, x);
			}
			x++;
			count++;
		}

		// Horizontal Left check
		y = row;
		x = col;
		count = 0;
		while (x > -1 && count < 2) {
			if (x != col) {
				checkSquare(y, x);
			}
			x--;
			count++;
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
//			if (!myChessBoard[row][col].getKingCantMove()) {
//				myChessBoard[row][col].moveSelected();
//			}
//			if (player != currentPlayer) {
//				myChessBoard[row][col].setKingCantMove(false);
//			}
			
		} else if (myChessBoard[row][col].getPiece().getPlayer() == this.getPlayer()) {
			//myChessBoard[row][col].setKingCantMove(true);
			
		} else if (myChessBoard[row][col].getPiece().getPlayer() != this.getPlayer()) {
			myChessBoard[row][col].captureSelected();
//			if (player != currentPlayer) {
//				myChessBoard[row][col].setKingCantMove(false);
//			}
		}
	}
}
