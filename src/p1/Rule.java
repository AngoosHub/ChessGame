/**
 * 
 */
package p1;

import javafx.scene.layout.GridPane;

import java.io.Serializable;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * The Rule class manges all the interactions between squares and pieces when
 * clicked. It is a large event handler that allows movement, changing selection
 * between friendly pieces, and allowing capture of enemy pieces.
 * 
 * @author anguslin
 *
 */
public class Rule implements Serializable {
	private GUI gui;
	private Square[][] myChessBoard;
	private Square currentSquare;
	private int player1;
	private int player2;
	private int currentPlayer;
	private boolean kingChecked;

	/**
	 * Constructor for Rule, takes in the array of chess board from GUI to keep
	 * track of pieces.
	 * 
	 * @param chessBoard the 2-D array of squares representing chess board
	 */
	public Rule(GUI myGUI, Square[][] chessBoard, int p1, int p2, int theCurrentPlayer) {
		gui = myGUI;
		myChessBoard = chessBoard;
		currentSquare = null;
		player1 = p1;
		player2 = p2;
		currentPlayer = theCurrentPlayer;
		kingChecked = false;
	}

	/**
	 * Process the square clicked to move a piece if there is a valid square to move
	 * to.
	 * 
	 * @param row of the square to interact
	 * @param col of the square to interact
	 */
	public void runRule(int row, int col) {
		Square newSquare = getSquareByRowColumn(myChessBoard, row, col);
		/* If the king is checked, force king to make a valid move */
//		if (kingChecked) {
//			if (newSquare.getMoveSelected()) {
//				newSquare.setPiece(currentSquare.getPiece());
//				currentSquare.setPiece(null);
//				endAction();
//			} else {
//				newSquare.warningSelected();
//			}
//		} else 
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
				if (newSquare.getPiece().getPieceName() == "King") {
					allDangerousSquares();
				}
				currentSquare.clickSelected();
				showValidMoves(currentSquare, currentSquare.getSquareRow(), currentSquare.getSquareColumn());
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
				if (newSquare.getPiece().getPieceName() == "King") {
					allDangerousSquares();
				}
				currentSquare.clickSelected();
				showValidMoves(currentSquare, currentSquare.getSquareRow(), currentSquare.getSquareColumn());
			} else if (!newSquare.getMoveSelected()) {
				newSquare.warningSelected();
			} else if (newSquare.getPiece() == null) {
				newSquare.setPiece(currentSquare.getPiece());
				currentSquare.setPiece(null);
				// unselectAll();
				endAction();
			} else if (newSquare.getPiece().getPlayer() != currentSquare.getPiece().getPlayer()) {
				/*
				 * If the new selected square contains an unfriendly piece, capture it and
				 * replace with current piece.
				 */
				newSquare.setPiece(currentSquare.getPiece());
				currentSquare.setPiece(null);
				currentSquare = newSquare;
				// unselectAll();
				endAction();
			}
		}
	}

	/**
	 * Finds and retrieves the Square at the coordinates given.
	 * 
	 * @param chessBoard the 2D array of squares
	 * @param row        the y coordinate of square
	 * @param col        the x coordinate of square
	 * @return
	 */
	public Square getSquareByRowColumn(final Square[][] chessBoard, final int row, final int col) {

		for (Square[] squareY : chessBoard) {
			for (Square squareX : squareY) {
				if (squareX.getSquareRow() == row && squareX.getSquareColumn() == col) {
					return squareX;
				}
			}
		}
		return null;
	}

	/**
	 * shows the valid moves for the piece.
	 * 
	 * @param row of square who's piece to check
	 * @param col of square who's piece to check
	 */
	public void showValidMoves(Square square, int row, int col) {
		// currentSquare.getPiece().validMove(myChessBoard, row, col);
		square.getPiece().validMove(myChessBoard, row, col);
	}

	/**
	 * Sets currentSquare back to null to end this turn, select all the attacked
	 * squares for next turn's player to check if king is attacked, and end the turn
	 * to switch to next player.
	 */
	public void endAction() {
		currentSquare = null;
		kingChecked = false;
		unselectAll();
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}
		gui.changeTurn();
		allDangerousSquares();
		if (checkKing()) {
			unselectAll();
		}
	}

	/**
	 * Unselects all squares and resets their select state.
	 */
	public void unselectAll() {
		for (Square[] squareY : myChessBoard) {
			for (Square squareX : squareY) {
				squareX.resetSelected();
			}
		}
	}

	/**
	 * Unselects all squares and resets some of their values to select state, but
	 * keeping values to prevent king's movement.
	 */
	public void checkUnselectAll() {
		for (Square[] squareY : myChessBoard) {
			for (Square squareX : squareY) {
				squareX.checkResetSelected();
			}
		}
	}

	/**
	 * Shows the attacked squares from each of the enemies' pieces to check if
	 * current player king cannot move.
	 */
	public void allDangerousSquares() {
		for (int y = 0; y < myChessBoard.length; y++) {
			for (int x = 0; x < myChessBoard[y].length; x++) {
				if (myChessBoard[y][x].getPiece() != null
						&& myChessBoard[y][x].getPiece().getPlayer() != currentPlayer) {
					showValidMoves(myChessBoard[y][x], y, x);
				}
			}
		}
		checkUnselectAll();
	}

	/**
	 * Checks if the current player's king is currently checked and must be moved.
	 */
	public boolean checkKing() {
		boolean safe = true;
		for (Square[] squareY : myChessBoard) {
			for (Square squareX : squareY) {
				if (squareX.getPiece() != null && squareX.getPiece().getPieceName() == "King"
						&& squareX.getPiece().getPlayer() == currentPlayer) {
					if (squareX.getKingCantMove()) {
						squareX.kingWarningSelected();
						currentSquare = squareX;
						showValidMoves(currentSquare, currentSquare.getSquareRow(), currentSquare.getSquareColumn());
						kingChecked = true;
						safe = false;
						checkCheckMate();
					}
				}
			}
		}
		return safe;
	}

	/**
	 * Checks if the king has any valid spots to move to on the board, if there is
	 * none, declare checkmate.
	 */
	public void checkCheckMate() {
		boolean moveFound = false;
		for (Square[] squareY : myChessBoard) {
			for (Square squareX : squareY) {
				if (squareX.getMoveSelected()) {
					moveFound = true;
				}
			}
		}
		if (!moveFound) {
			gui.checkMate();
		} else {
			unselectAll();
		}
	}

}
