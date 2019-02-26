/**
 * 
 */
package p1;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.nio.file.Paths;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;



/**Creates a GUI that builds an interactive chess board, with correct move-set for pieces, capturing,
 * the options to save and load the current arrange of the board, and checking if king is in check
 * or checkmated.
 * @author anguslin
 * @version 2019
 *
 */
public class GUI extends Application {
	/* 
	 * To-Do List:
	 * - Castle king with rook, black and white both king and queen side.
	 * - en passant with pawn when last move is a double
	 * - Undo last move botton
	 * - Promotion with pawn
	 * DONE - When check restrict moves to only those that protects king
	 * Far reaching goals:
	 * - Play with computer, pick color, and have a basic AI
	 */
	
	/*
	 * Checking works but uses up too much memory...
	 * Making multiple instances of chessboard with javafx and rectangle and png image is too memery heavy...
	 * Try building pieces with no png first,
	 * if still slow, as last resort build chess in 2D int array, with it own version of move check functions.
	 */
	
	/*
	 * Castling Plan:
	 * King and Rook not moved yet, black and white, king and queen side castling
	 * the space in between is clear and not threatened by enemies
	 * king and rook move.
	 */
	
	/*
	 * En Passant plan:
	 * Pawn on the 6th? 5th rank? check for black/white
	 * The other pawns first move and on 5th rank? or Pawn makes a double move (y pos abs diff or 2)
	 * then can special capture
	 */
	
	/*
	 * Undo last move plan:
	 * Save a copy of the last chessboard, and if clicked, swap back to it.
	 * Or save and array of boards as list of actions, each click goes back one
	 * Can go forward?
	 * If make move, then overwrite it and delete all future saved boards.
	 */
	
	/*
	 * Promotion plan:
	 * add in a pop dialogue for choosing which piece to swap to.
	 * then set in new piece in its place.
	 * check if pawn is at final square at end action?
	 * pop up has buttons with image of each piece, when click replaces pawn with that piece.
	 */
	
	private GridPane gridPane;
	private ChessBoard chessBoard;
	private BorderPane root;
	private MenuBar menuBar;
	private VBox vBox;
	private Label currentTurn;
	//private int[][] tempBoard;
	private SavedBoard savedBoard;
	

	/**
	 * Builds the stage where the chess board, squares, pieces, and rules will be placed onto,
	 * and displayed to user.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chess");
		root = new BorderPane();
		gridPane = new GridPane();
		gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		chessBoard = new ChessBoard(this);
		
		
		createMenu(primaryStage);
		generateBoard();
		root.setTop(vBox);
		root.setCenter(gridPane);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
	/**
	 * Generates the a physical representation of the board by attaching the Squares
	 * in ChessBoard class to the correct coordinate on gridpane.
	 */
	public void generateBoard() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				gridPane.add(chessBoard.getBoard()[y][x], x, y);
			}
		}
	}
	
	/**
	 * Changes the color of label under menu bar that signals the current color's turn.
	 */
	public void changeTurn() {
		if (currentTurn.getText().equals("White's Turn")) {
			currentTurn.setText("Black's Turn");
			currentTurn.setStyle(" -fx-text-fill: white; -fx-background-color: black;");
		} else {
			currentTurn.setText("White's Turn");
			currentTurn.setStyle("-fx-text-fill: black; -fx-background-color: white;");
		}
	}
	
	public String getCurrentTurn() {
		return currentTurn.getText();
	}
	
	/**
	 * Creates a Menu Bar with the options to save the current chess board arrangement,
	 * and load a chess board arrangement onto the chess board. Also shows the current
	 * turn as black or white's turn.
	 * @return a VBox holding Menus
	 */
	private void createMenu(Stage primaryStage) {
		currentTurn = new Label("White's Turn");
		currentTurn.prefWidthProperty().bind(primaryStage.widthProperty());
		currentTurn.setAlignment(Pos.CENTER);
		currentTurn.setStyle("-fx-font-weight: bold");
		
		
		menuBar = new MenuBar();
		Menu options = new Menu("Options");
		MenuItem save = new MenuItem("Save");
		save.setOnAction(e -> {
			try {
				savedBoard = new SavedBoard(chessBoard);
				FileOutputStream file = new FileOutputStream("SavedGame");
				ObjectOutputStream out = new ObjectOutputStream(file);
				out.writeObject(savedBoard);
				out.flush();
				out.close();
				file.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		});
		options.getItems().add(save);
		
		MenuItem load = new MenuItem("Load");
		load.setOnAction(e -> {
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File");
				String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
				fileChooser.setInitialDirectory(new File(currentPath));
				File selectedFile = fileChooser.showOpenDialog(primaryStage);
				
				FileInputStream inputFile = new FileInputStream(selectedFile);
				ObjectInputStream in = new ObjectInputStream(inputFile);
				savedBoard = (SavedBoard) in.readObject();
				clearBoard();
				savedBoard.processChessBoard(chessBoard);
				in.close();
				inputFile.close();
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		});
		options.getItems().add(load);

		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		menuBar.getMenus().add(options);
		vBox = new VBox(menuBar, currentTurn);
	}
	
	/**
	 * Converts the current pieces on the board into a number representation in a
	 * 2-D int array. 
	 * @return the coded chess board
	 */
//	private int[][] codeChessBoard() {
//		for (int y = 0; y < chessBoard.getBoard().length; y++) {
//			for (int x = 0; x < chessBoard.getBoard()[y].length; x++) {
//				if (chessBoard.getBoard()[y][x].getPiece() == null) {
//					tempBoard[y][x] = 0;
//				} else {
//					if (chessBoard.getBoard()[y][x].getPiece().getPlayer() == 1) {
//						tempBoard[y][x] = 10;
//					} else {
//						tempBoard[y][x] = 20;
//					}
//					
//					if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Pawn") {
//						tempBoard[y][x] += 1;
//					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Knight") {
//						tempBoard[y][x] += 2;
//					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Bishop") {
//						tempBoard[y][x] += 3;
//					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Rook") {
//						tempBoard[y][x] += 4;
//					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "Queen") {
//						tempBoard[y][x] += 5;
//					} else if (chessBoard.getBoard()[y][x].getPiece().getPieceName() == "King") {
//						tempBoard[y][x] += 6;
//					} else {
//						tempBoard[y][x] = 0;
//					}
//						
//				}
//			}
//		}
//		
//		return tempBoard;
//	}
	
	/**
	 * Clears the current board, then processes the coded chess board and uses the numbers
	 * to rebuild the correct pieces in the right location on the chess board.
	 */
//	private void processChessBoard() {
//		int player = 0;
//		clearBoard();
//		for (int y = 0; y < tempBoard.length; y++) {
//			for (int x = 0; x < tempBoard[y].length; x++) {
//				if (tempBoard[y][x] == 0) {
//					chessBoard.getBoard()[y][x].setPiece(null);
//				} else {
//					if (tempBoard[y][x] < 20) {
//						player = 1;
//						tempBoard[y][x] -= 10;
//					} else {
//						player = 2;
//						tempBoard[y][x] -= 20;
//					}
//					
//					if (tempBoard[y][x] == 1) {
//						chessBoard.getBoard()[y][x].setPiece(new Pawn(player));
//					} else if (tempBoard[y][x] == 2) {
//						chessBoard.getBoard()[y][x].setPiece(new Knight(player));
//					} else if (tempBoard[y][x] == 3) {
//						chessBoard.getBoard()[y][x].setPiece(new Bishop(player));
//					} else if (tempBoard[y][x] == 4) {
//						chessBoard.getBoard()[y][x].setPiece(new Rook(player));
//					} else if (tempBoard[y][x] == 5) {
//						chessBoard.getBoard()[y][x].setPiece(new Queen(player));
//					} else if (tempBoard[y][x] == 6) {
//						chessBoard.getBoard()[y][x].setPiece(new King(player));
//					} else {
//						
//					}
//						
//				}
//			}
//		}
//	}
	
	/**
	 * Empties the board of its current pieces.
	 */
	private void clearBoard() {
		chessBoard.getRule().unselectAll();
		for (int y = 0; y < chessBoard.getBoard().length; y++) {
			for (int x = 0; x < chessBoard.getBoard()[y].length; x++) {
				chessBoard.getBoard()[y][x].setPiece(null);
			}
		}
	}
	
	/**
	 * Gets the player's who has the current turn.
	 * @return number of current player
	 */
//	public int getCurrentPlayer() {
//		return currentPlayer;
//	}
	
	/**
	 * Ends the match and declare the winner.
	 */
//	public void checkMate() {
//		if (currentPlayer == 1) {
//			currentTurn.setText("Black Wins!");
//			currentTurn.setStyle(" -fx-text-fill: white; -fx-background-color: black;");
//		} else {
//			currentTurn.setText("White Wins!");
//			currentTurn.setStyle(" -fx-text-fill: black; -fx-background-color: white;");
//		}
//		
//	}
//	
	/**
	 * Launches the GUI.
	 */
	public void run() {
		launch();
	}

	/**
	 * temporary test launcher.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
