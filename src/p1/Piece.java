/**
 * 
 */
package p1;


import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;


/**
 * @author anguslin
 *
 */
public class Piece extends ImageView {
	private String pieceName;
	private int player;
	private URL url;
	private Square[][] myChessBoard;
	
	/**
	 * Constructor for a piece.
	 * @param playerNum owner of the piece.
	 */
	public Piece() {
//		getFile();
//		createPiece();
	}
	
	/**
	 * creates a testing version of the piece with no png image to save memory space.
	 * @param test
	 */
	public Piece(String test) {
		
	}
	
	/**
	 * Builds the piece using a chess image.
	 */
	public void createPiece() {
		try {
			//File file = new File("/Users/anguslin/eclipse-workspace/Asn2a/src/chess-pieces/White-Pawn.png");
			File file = new File(url.getPath());
			System.out.println(file.toURI().toString());
	        Image image = new Image(file.toURI().toString());
	        this.setImage(image);
			this.setFitWidth(100);
			this.setPreserveRatio(true);
			} catch (Exception e) {
				e.printStackTrace(); 
			}
	}
	
	/**
	 * Retrieves the url to the chess piece image.
	 */
	protected void getFile() {
		url = getClass().getResource("../chess-pieces/White-Pawn.png");
	}
	
	/**
	 * Gets the player number.
	 * @return
	 */
	public int getPlayer() {
		return player;
	}
	
	/**
	 * Checks the valid moves allowed to this piece. Makes sure the piece is not blocked by
	 * and ally and cannot move through pieces.
	 * @param chessBoard the current arrangement of the board
	 * @param row of the square the piece is on
	 * @param col of the square the piece is on
	 */
	public void validMove(Square[][] chessBoard, int row, int col) {
		// pass 2-d square array, row, and col in.
		// piece will for loop? +/- the row and col for valid moves.
		// if the array's square has a friendly piece in it, end loop no including this square.
		// if the array's square has an unfriendly piece in it, end loop including this square.
		// set the set of each square to moveStroke that is valid
		// use if have move stroke is valid square to move to
		myChessBoard = chessBoard;
		int y = row;
		int x = col;
		int check = 0;
		while (y < 8 && x < 8 && check == 0) {
			y++;
			checkSquare(y, x);
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
	 * Compares the new square's piece with the old square piece and checks if its a valid move. 
	 * @param row of square to check
	 * @param col of square to check
	 */
	public void checkSquare(int row, int col) {
		if (myChessBoard[row][col].getPiece() == null) {
			myChessBoard[row][col].moveSelected();
		} else if (myChessBoard[row][col].getPiece().getPlayer() == this.getPlayer()) {
			
		} else if (myChessBoard[row][col].getPiece().getPlayer() != this.getPlayer()) {
			myChessBoard[row][col].moveSelected();
		}
	}
}
