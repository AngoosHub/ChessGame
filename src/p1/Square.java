/**
 * 
 */
package p1;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import java.io.Serializable;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.FillTransition;

/**
 * The square class creates a tile that becomes a space on the chess board. It
 * can have a piece on it, and knows the location to provide to piece class for
 * interaction.
 * 
 * @author anguslin
 *
 */
public class Square extends Pane implements Serializable {
	private Rectangle r;
	private int checkSquare = 0;
	private Piece currentPiece;
	private Color color;
	private Square[][] chessBoard;
	private int squareColumn;
	private int squareRow;
	private boolean moveSelected;
	//private boolean kingCantMove;

	/**
	 * Constructs a Square on the chess board.
	 * 
	 * @param gc   a copy GUI's graphics context
	 * @param xPos The x-coordinate to build square
	 * @param yPos The y-coordinate to build square
	 * @param c    The color of the square
	 */
	public Square(Square[][] board, int xPos, int yPos, int width, int height, Color c, int col, int row) {
		chessBoard = board;
		squareColumn = col;
		squareRow = row;
		color = c;
		moveSelected = false;
		//kingCantMove = false;

		r = new Rectangle(xPos, yPos, width, height);
		r.setStrokeType(StrokeType.INSIDE);
		r.setStrokeWidth(10);
		r.setStroke(color);
		r.setFill(color);
		Piece piece = new Piece();
		currentPiece = piece;
		this.getChildren().add(r);
		// this.getChildren().add(currentPiece);
		// this.getChildren().add(image);
	}

	/**
	 * Returns data of the piece that currents occupies this square.
	 * 
	 * @return the current Piece
	 */
	public Piece getPiece() {
		return currentPiece;
	}

	/**
	 * Changes the current piece on the square to the new one, removing the old for
	 * the Pane and adding the new one on the Pane if it is not null.
	 * 
	 * @param newPiece New piece added to the square
	 */
	public void setPiece(Piece newPiece) {
		if (newPiece != null) {
			if (currentPiece != null) {
				this.getChildren().remove(currentPiece);
			}
			currentPiece = newPiece;
			this.getChildren().add(currentPiece);
		} else {
			if (currentPiece != null) {
				this.getChildren().remove(currentPiece);
			}
			currentPiece = newPiece;
		}
	}

	/**
	 * Changes the border of the square to indicate its the current square selected.
	 */
	public void clickSelected() {
		r.setFill(Color.GOLD);
	}

	/**
	 * Changes the border of the square to indicate its a valid square to move to.
	 */
	public void moveSelected() {
		r.setFill(Color.SLATEGRAY);
		moveSelected = true;
		//kingCantMove = true;
	}

	/**
	 * Changes the border of the square to indicate its a valid square to capture
	 * the piece.
	 */
	public void captureSelected() {
		r.setFill(Color.LIGHTSALMON);
		moveSelected = true;
		//kingCantMove = true;
	}

	/**
	 * Changes the border of the square to indicate its a valid square to move to.
	 */
	public void kingMoveSelected() {
		r.setFill(Color.SLATEGRAY);
		moveSelected = true;
	}

	/**
	 * Changes the border of the square to indicate its a valid square to capture
	 * the piece.
	 */
	public void kingCaptureSelected() {
		r.setFill(Color.LIGHTSALMON);
		moveSelected = true;
	}

	/**
	 * Changes the border and color of the square back to normal, unselected state.
	 */
	public void resetSelected() {
		r.setFill(color);
		r.setStroke(color);
		moveSelected = false;
		// kingCantMove = false;
		// kingSelected = false;
	}

	public void checkResetSelected() {
		r.setFill(color);
		r.setStroke(color);
		moveSelected = false;
	}

	/**
	 * Flashes and changes square to red to create a check warning.
	 */
	public void kingWarningSelected() {
		r.setFill(Color.LIGHTSALMON);
		FillTransition ft = new FillTransition(Duration.millis(1100), r, Color.RED, color);
		ft.setAutoReverse(false);
		ft.play();
	}

	/**
	 * Flashes a square red briefly to create a wrong player turn warning.
	 */
	public void warningSelected() {
		r.setFill(Color.LIGHTSALMON);
		FillTransition ft = new FillTransition(Duration.millis(500), r, Color.LIGHTSALMON, color);
		ft.setAutoReverse(false);
		ft.play();
	}

//	public void setColor() {
//		//r.setFill(Color.BEIGE);
//		File file = new File("/Users/anguslin/eclipse-workspace/Asn2a/src/chess-pieces/White-Pawn.png");
//		System.out.println(file.toURI().toString());
//        Image image = new Image(file.toURI().toString());
//        ImageView piece = new ImageView(image);
//		//Image image = new Image("White-Pawn.png");
//		piece.setFitWidth(100);
//		piece.setPreserveRatio(true);
//		this.getChildren().add(piece);
//	}
//	
	/**
	 * Retrieves the column of square in chess board.
	 * 
	 * @return column as integer
	 */
	public int getSquareColumn() {
		return squareColumn;
	}

	/**
	 * Retrieves the row of square in chess board.
	 * 
	 * @return row as integer
	 */
	public int getSquareRow() {
		return squareRow;
	}

	/**
	 * Checks if the square is occupied by a piece.
	 * 
	 * @return 0 if no piece, 1 if piece by player 1, 2 if piece by player 2
	 */
	public int checkSqaure() {
		return checkSquare;
	}

	/**
	 * Returns whether the square has been selected/highlighted. Mostly used by Rule
	 * class to determine valid movement.
	 * 
	 * @return value of moveSelected
	 */
	public boolean getMoveSelected() {
		return moveSelected;
	}

	/**
	 * Returns whether the square is valid for the king and not under threat.
	 * @return value of kingCantMove
	 */
//	public boolean getKingCantMove() {
//		return kingCantMove;
//	}

	/**
	 * Sets whether the square is valid for the king and not under threat.
	 * @param value of kingCantMove
	 */
//	public void setKingCantMove(boolean newValue) {
//		kingCantMove = newValue;
//	}
	
	/**
	 * Sets the current state of the square, empty(0), occupied by p1(1), occupied
	 * by p2(2).
	 * 
	 * @param newState the new state of square
	 */
	public void setSquare(int newState) {
		checkSquare = newState;
	}

	/**
	 * Returns the reference to the rectangle object.
	 * 
	 * @return rectangle
	 */
	public Rectangle getRect() {
		return r;
	}

}
