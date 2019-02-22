/**
 * 
 */
package p1;

import javafx.event.*;
import javafx.scene.paint.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.io.ObjectOutputStream;
import java.util.Optional;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.stage.Stage;
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
public class GUI extends Application implements Serializable {
	/* 
	 * To-Do List:
	 * - Castle king with rook, black and white both king and queen side.
	 * - en passante with pawn when last move is a double
	 * - Undo last move botton
	 * - Promotion with pawn
	 * - When check restrict moves to only those that protects king
	 * Far reaching goals:
	 * - Play with computer, pick color, and have a basic AI
	 */
	
	private GridPane gridPane;
	private Canvas canvas;
	private static Stage window;
	private Square[][] chessBoard;
	private Player player1;
	private Player player2;
	private Color color;
	private Rule rule;
	private BorderPane root;
	private MenuBar menuBar;
	private VBox vBox;
	private Label currentTurn;
	private int[][] tempBoard;
	private int currentPlayer;
	

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
		window = primaryStage;
		chessBoard = new Square[8][8];
		player1 = new Player(1, this);
		player2 = new Player(2, this);
		currentPlayer = player1.getPlayer();
		rule = new Rule(this, chessBoard, player1.getPlayer(), player2.getPlayer(), currentPlayer);
		tempBoard = new int[8][8];
		
		
		createMenu(primaryStage);
		generateBoard();
		root.setTop(vBox);
		root.setCenter(gridPane);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
	/**
	 * Builds each square of the board and assigns them into an 8 by 8 grid, and interact
	 * when clicked by running through rule class for the correct action.
	 */
	public void generateBoard() { 
		int xPos = 0;
		int yPos = 0;
		final int width = 100;
		final int height = 100;
		color = Color.BEIGE;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Square square = new Square(chessBoard, xPos, yPos, width, height, color, x, y);
				//xPos += 100;
				swapBoardColor();
				square.setPrefSize(100, 100);
				square.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent mouseEvent) {
						rule.runRule(square.getSquareRow(), square.getSquareColumn());
					} 
				});
				//root.getChildren().add(square);
				Piece newPiece = generatePiece(y, x);
				square.setPiece(newPiece);
				chessBoard[y][x] = square;
				gridPane.add(chessBoard[y][x], x, y);
			}
			swapBoardColor();
			//xPos = 0;
			//yPos += 100;
		}
	}
	
	/**
	 * Creates a pieces for the square based on player number.
	 * @param row of square to make piece
	 * @param col of square to make piece
	 * @return
	 */
	public Piece generatePiece(int row, int col) {
		Piece piece = null;
		if (row < 2) {
			return player2.generatePiece(row, col);
		} else if (row > 5) {
			return player1.generatePiece(row, col);
		}
		return piece;
	}
	
	/**
	 * Alters the color to make a checkboard pattern.
	 */
	private void swapBoardColor() {
		if (color == Color.BEIGE) {
			color = Color.DIMGRAY;
		} else {
			color = Color.BEIGE;
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
				codeChessBoard();
				FileOutputStream file = new FileOutputStream("tempBoard");
				ObjectOutputStream out = new ObjectOutputStream(file);
				out.writeObject(tempBoard);
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
				tempBoard = (int[][]) in.readObject();
				processChessBoard();
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
	private int[][] codeChessBoard() {
		for (int y = 0; y < chessBoard.length; y++) {
			for (int x = 0; x < chessBoard[y].length; x++) {
				if (chessBoard[y][x].getPiece() == null) {
					tempBoard[y][x] = 0;
				} else {
					if (chessBoard[y][x].getPiece().getPlayer() == 1) {
						tempBoard[y][x] = 10;
					} else {
						tempBoard[y][x] = 20;
					}
					
					if (chessBoard[y][x].getPiece().getPieceName() == "Pawn") {
						tempBoard[y][x] += 1;
					} else if (chessBoard[y][x].getPiece().getPieceName() == "Knight") {
						tempBoard[y][x] += 2;
					} else if (chessBoard[y][x].getPiece().getPieceName() == "Bishop") {
						tempBoard[y][x] += 3;
					} else if (chessBoard[y][x].getPiece().getPieceName() == "Rook") {
						tempBoard[y][x] += 4;
					} else if (chessBoard[y][x].getPiece().getPieceName() == "Queen") {
						tempBoard[y][x] += 5;
					} else if (chessBoard[y][x].getPiece().getPieceName() == "King") {
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
	private void processChessBoard() {
		int player = 0;
		clearBoard();
		for (int y = 0; y < tempBoard.length; y++) {
			for (int x = 0; x < tempBoard[y].length; x++) {
				if (tempBoard[y][x] == 0) {
					chessBoard[y][x] = null;
				} else {
					if (tempBoard[y][x] < 20) {
						player = 1;
						tempBoard[y][x] -= 10;
					} else {
						player = 2;
						tempBoard[y][x] -= 20;
					}
					
					if (tempBoard[y][x] == 1) {
						chessBoard[y][x].setPiece(new Pawn(player));
					} else if (tempBoard[y][x] == 2) {
						chessBoard[y][x].setPiece(new Knight(player));
					} else if (tempBoard[y][x] == 3) {
						chessBoard[y][x].setPiece(new Bishop(player));
					} else if (tempBoard[y][x] == 4) {
						chessBoard[y][x].setPiece(new Rook(player));
					} else if (tempBoard[y][x] == 5) {
						chessBoard[y][x].setPiece(new Queen(player));
					} else if (tempBoard[y][x] == 6) {
						chessBoard[y][x].setPiece(new King(player, currentPlayer));
					} else {
						chessBoard[y][x] = null;
					}
						
				}
			}
		}
	}
	
	/**
	 * Empties the board of its current pieces.
	 */
	private void clearBoard() {
		for (int y = 0; y < chessBoard.length; y++) {
			for (int x = 0; x < chessBoard[y].length; x++) {
				chessBoard[y][x].setPiece(null);
			}
		}
	}
	
	/**
	 * Gets the player's who has the current turn.
	 * @return number of current player
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Ends the match and declare the winner.
	 */
	public void checkMate() {
		if (currentPlayer == 1) {
			currentTurn.setText("Black Wins!");
			currentTurn.setStyle(" -fx-text-fill: white; -fx-background-color: black;");
		} else {
			currentTurn.setText("White Wins!");
			currentTurn.setStyle(" -fx-text-fill: black; -fx-background-color: white;");
		}
		
	}
	
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
