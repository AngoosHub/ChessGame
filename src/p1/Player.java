/**
 * 
 */
package p1;



/**Player is able to build each of the pieces in chess and assign ownership to its pieces.
 * @author anguslin
 *
 */
public class Player {
	// 1 is player, 2 is player, 0 for none??
	private int player;
	
	/**
	 * The constructor builds a player
	 * @param setPlayer
	 * @param myGUI
	 */
	public Player(int setPlayer) {
		player = setPlayer;
		
	}
	
	/**
	 * Gets the player number.
	 * @return player number as int
	 */
	public int getPlayer() {
		return player;
	}
	
	/**
	 * Makes a Pawn.
	 * @return the Pawn.
	 */
	public Piece buildPawn() {
		Pawn pawn = new Pawn(player);
		return pawn;
	}
	
	/**
	 * Makes a Knight.
	 * @return the Knight.
	 */
	public Piece buildKnight() {
		Knight knight = new Knight(player);
		return knight;
	}
	
	/**
	 * Makes a Bishop.
	 * @return the Bishop.
	 */
	public Piece buildBishop() {
		Bishop bishop = new Bishop(player);
		return bishop;
	}
	
	/**
	 * Makes a Rook.
	 * @return the Rook.
	 */
	public Piece buildRook() {
		Rook rook = new Rook(player);
		return rook;
	}
	
	/**
	 * Makes a Queen.
	 * @return the Queen.
	 */
	public Piece buildQueen() {
		Queen queen = new Queen(player);
		return queen;
	}
	
	/**
	 * Makes a King.
	 * @return the King.
	 */
	public Piece buildKing() {
		King king = new King(player);
		return king;
	}
	
	/**
	 * Builds the player's chess pieces and assigns location based on the player number.
	 * @param row of square to build piece
	 * @param col of square to build piece
	 * @return the piece built
	 */
	public Piece generatePiece(int row, int col) {
		Piece piece = null;
		if (row == 1 || row == 6) {
			return buildPawn();
		} else if (col == 0 || col == 7) {
			return buildRook();
		} else if (col == 1 || col == 6) {
			return buildKnight();
		} else if (col == 2 || col == 5) {
			return buildBishop();
		} else if (col == 3) {
			return buildQueen();
		} else if (col == 4) {
			return buildKing();
		}
		return piece;
	}
	
}
