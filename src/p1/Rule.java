/**
 * 
 */
package p1;



/**
 * The Rule class manges all the interactions between squares and pieces when
 * clicked. It is a large event handler that allows movement, changing selection
 * between friendly pieces, and allowing capture of enemy pieces.
 * 
 * @author anguslin
 *
 */
public class Rule {
	private ChessBoard chessBoard;
	private Square currentSquare;
	private int player1;
	private int player2;
	private int currentPlayer;

	/**
	 * Constructor for Rule, takes in the array of chess board from GUI to keep
	 * track of pieces.
	 * 
	 * @param chessBoard the 2-D array of squares representing chess board
	 */
	public Rule(ChessBoard theChessBoard, int p1, int p2) {
		chessBoard = theChessBoard;
		currentSquare = null;
		player1 = p1;
		player2 = p2;
		currentPlayer = theChessBoard.getCurrentPlayer().getPlayer();
	}

	/**
	 * Process the square clicked to move a piece if there is a valid square to move
	 * to.
	 * 
	 * @param row of the square to interact
	 * @param col of the square to interact
	 */
	public void runRule(int row, int col) {
		Square newSquare = chessBoard.getSquare(row, col);
		
		if (newSquare.getPiece() == null && currentSquare == null) {
			/*
			 * Do nothing as there is not piece to move.
			 */
		} else if (currentSquare == null) {
			/*
			 * If no previous square clicked, make this square the current square.
			 */
			if (newSquare.getPiece().getPlayer() == currentPlayer) {
				currentSquare = newSquare;
				currentSquare.clickSelected();
				testValidMoves(currentSquare, currentSquare.getSquareRow(), currentSquare.getSquareColumn());
			} else {
				newSquare.warningSelected();
			}

		} else {
			/*
			 * If there is previous square clicked, check if new square has a piece on it.
			 * If it doesn't, move the piece to new square by adding it to the new clicked
			 * square and removing it from the old one. Then set currentSquare back to null
			 * as the action is over.
			 */
			if (newSquare.getPiece() != null && newSquare.getPiece().getPlayer() == currentPlayer) {
				/*
				 * If the new selected square contains a friendly piece, transfer the current
				 * selected over to it.
				 */
				unselectAll();
				currentSquare = newSquare;
				currentSquare.clickSelected();
				testValidMoves(currentSquare, currentSquare.getSquareRow(), currentSquare.getSquareColumn());
			} else if (!newSquare.getMoveSelected()) {
				newSquare.warningSelected();
			} else if (newSquare.getPiece() == null) {
				newSquare.setPiece(currentSquare.getPiece());
				currentSquare.setPiece(null);
				endAction();
			} else if (newSquare.getPiece().getPlayer() != currentSquare.getPiece().getPlayer()) {
				/*
				 * If the new selected square contains an unfriendly piece, capture it and
				 * replace with current piece.
				 */
				newSquare.setPiece(currentSquare.getPiece());
				currentSquare.setPiece(null);
				currentSquare = newSquare;
				endAction();
			}
		}
	}

	/**
	 * shows the valid moves for the piece.
	 * 
	 * @param row of square who's piece to check
	 * @param col of square who's piece to check
	 */
	public void showValidMoves(Square square, int row, int col) {
		square.getPiece().validMove(chessBoard.getBoard(), row, col);
	}
	
	
/**
 * Creates a duplicate of the current chessboard to play the move and check if it
 * is valid and won't endanger the king, if so restricts the move.
 * @param square
 * @param row
 * @param col
 */
	public void testValidMoves(Square square, int row, int col) {
		square.getPiece().validMove(chessBoard.getBoard(), row, col);
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (chessBoard.getBoard()[y][x].getMoveSelected()) {					
					ChessBoard tempBoard = new ChessBoard(chessBoard);
					tempBoard.getBoard()[y][x].setPiece(tempBoard.getBoard()[row][col].getPiece());
					
					tempBoard.getBoard()[row][col].setPiece(null);
					boolean safe = tempBoard.getRule().checkKing();
					if (!safe) {
						chessBoard.getBoard()[y][x].resetSelected();
					}
				}
			}
		}
	}
	

	/**
	 * Sets currentSquare back to null to end this turn, select all the attacked
	 * squares for next turn's player to check if king is attacked, and end the turn
	 * to switch to next player.
	 */
	public void endAction() {
		currentSquare = null;
		unselectAll();
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}
		chessBoard.changeCurrentPlayer();
//		allDangerousSquares();
//		if (checkKing()) {
//			unselectAll();
//		}
	}

	/**
	 * Unselects all squares and resets their select state.
	 */
	public void unselectAll() {
		for (Square[] squareY : chessBoard.getBoard()) {
			for (Square squareX : squareY) {
				squareX.resetSelected();
			}
		}
	}

	/**
	 * Shows the attacked squares from each of the enemies' pieces to check if
	 * current player king cannot move.
	 */
	public boolean checkKing() {
		Square king = null;
		for (int y = 0; y < chessBoard.getBoard().length; y++) {
			for (int x = 0; x < chessBoard.getBoard()[y].length; x++) {
				if (chessBoard.getBoard()[y][x].getPiece() == null) {
					/* If no piece do nothing */
				} else if (chessBoard.getBoard()[y][x].getPiece().getPlayer() != currentPlayer) {
					showValidMoves(chessBoard.getBoard()[y][x], y, x);
				} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName().equals("King")
						&& chessBoard.getBoard()[y][x].getPiece().getPlayer() == currentPlayer) {
					king = chessBoard.getBoard()[y][x];
				}
			}
		}
		boolean safe = true;
		if (king == null) {
			System.out.println("Warning King is null!");
		}
		if (king != null && king.getMoveSelected()) {
			king.kingWarningSelected();
			safe = false;
		}
		//unselectAll();
		return safe;
	}
	
	/**
	 * Unselects all squares and resets some of their values to select state, but
	 * keeping values to prevent king's movement.
	 */
//	public void checkUnselectAll() {
//		for (Square[] squareY : chessBoard.getBoard()) {
//			for (Square squareX : squareY) {
//				squareX.checkResetSelected();
//			}
//		}
//	}

	/**
	 * Checks if the current player's king is currently checked and must be moved.
	 */
//	public boolean checkKing() {
//		boolean safe = true;
//		for (Square[] squareY : myChessBoard) {
//			for (Square squareX : squareY) {
//				if (squareX.getPiece() != null && squareX.getPiece().getPieceName() == "King"
//						&& squareX.getPiece().getPlayer() == currentPlayer) {
//					if (squareX.getKingCantMove()) {
//						squareX.kingWarningSelected();
//						currentSquare = squareX;
//						showValidMoves(currentSquare, currentSquare.getSquareRow(), currentSquare.getSquareColumn());
//						kingChecked = true;
//						safe = false;
//						checkCheckMate();
//					}
//				}
//			}
//		}
//		return safe;
//	}

	/**
	 * Checks if the king has any valid spots to move to on the board, if there is
	 * none, declare checkmate.
	 */
//	public void checkCheckMate() {
//		boolean moveFound = false;
//		for (Square[] squareY : myChessBoard) {
//			for (Square squareX : squareY) {
//				if (squareX.getMoveSelected()) {
//					moveFound = true;
//				}
//			}
//		}
//		if (!moveFound) {
//			gui.checkMate();
//		} else {
//			unselectAll();
//		}
//	}

}
