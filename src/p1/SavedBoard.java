/**
 * 
 */
package p1;

import java.io.Serializable;

/** Saves the current arrangement of the board by coding the pieces location into a 2D
 * integer array. Which can then be decoded to reinsert into the chessBoard.
 * @author anguslin
 *
 */
public class SavedBoard implements Serializable {
	private int[][] tempBoard;
	private int currentPlayer;
	
	public SavedBoard(ChessBoard chessBoard) {
		tempBoard = new int[8][8];
		currentPlayer = chessBoard.getCurrentPlayer().getPlayer();
		tempBoard = codeChessBoard(chessBoard);
	}
	
//	public SavedBoard(int[][] tempChessBoard, ChessBoard chessBoard) {
//		tempBoard = tempChessBoard;
//		processChessBoard(chessBoard);
//	}
	
	/**
	 * Converts the current pieces on the board into a number representation in a
	 * 2-D int array. 
	 * @return the coded chess board
	 */
	public int[][] codeChessBoard(ChessBoard chessBoard) {
		for (int y = 0; y < chessBoard.getBoard().length; y++) {
			for (int x = 0; x < chessBoard.getBoard()[y].length; x++) {
				if (chessBoard.getBoard()[y][x].getPiece() == null) {
					tempBoard[y][x] = 0;
				} else {
					if (chessBoard.getBoard()[y][x].getPiece().getPlayer() == 1) {
						tempBoard[y][x] = 10;
					} else {
						tempBoard[y][x] = 20;
					}
					
					if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Pawn") {
						tempBoard[y][x] += 1;
					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Knight") {
						tempBoard[y][x] += 2;
					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Bishop") {
						tempBoard[y][x] += 3;
					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Rook") {
						tempBoard[y][x] += 4;
					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Queen") {
						tempBoard[y][x] += 5;
					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "King") {
						tempBoard[y][x] += 6;
					} else {
						tempBoard[y][x] = 0;
					}
						
				}
			}
		}
		
		return tempBoard;
	}
	
	/**
	 * Clears the current board, then processes the coded chess board and uses the numbers
	 * to rebuild the correct pieces in the right location on the chess board.
	 */
	public void processChessBoard(ChessBoard chessBoard) {
		int player = 0;
		for (int y = 0; y < tempBoard.length; y++) {
			for (int x = 0; x < tempBoard[y].length; x++) {
				if (tempBoard[y][x] == 0) {
					chessBoard.getBoard()[y][x].setPiece(null);
				} else {
					if (tempBoard[y][x] < 20) {
						player = 1;
						tempBoard[y][x] -= 10;
					} else {
						player = 2;
						tempBoard[y][x] -= 20;
					}
					
					if (tempBoard[y][x] == 1) {
						chessBoard.getBoard()[y][x].setPiece(new Pawn(player));
					} else if (tempBoard[y][x] == 2) {
						chessBoard.getBoard()[y][x].setPiece(new Knight(player));
					} else if (tempBoard[y][x] == 3) {
						chessBoard.getBoard()[y][x].setPiece(new Bishop(player));
					} else if (tempBoard[y][x] == 4) {
						chessBoard.getBoard()[y][x].setPiece(new Rook(player));
					} else if (tempBoard[y][x] == 5) {
						chessBoard.getBoard()[y][x].setPiece(new Queen(player));
					} else if (tempBoard[y][x] == 6) {
						chessBoard.getBoard()[y][x].setPiece(new King(player));
					} else {
						
					}
						
				}
			}
		}
		if (chessBoard.getCurrentPlayer().getPlayer() != currentPlayer) {
			chessBoard.getRule().endAction();
		}
	}
	
	public int[][] getTempBoard() {
		return tempBoard;
	}
	
}
