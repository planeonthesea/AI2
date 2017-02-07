import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AkariPuzzle {
	private int rows;
	private int cols;
	private char[][] gameBoard;

	public AkariPuzzle(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.gameBoard = new char[this.rows][this.cols];
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public char[][] getGameBoard() {
		return this.gameBoard;
	}

	public void setGameBoard(char[][] board) {
		this.gameBoard = board;
	}

	public void setGameBoardCell(int row, int col, char character) {
		this.gameBoard[row][col] = character;
	}

	public void printBoard() {
		System.out.println("=============================");
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				System.out.print(this.gameBoard[i][j]);
			}
			System.out.println();
		}
		System.out.println("=============================");
	}

	public ArrayList<Coordinate> getCoordList() {
		// Gets all available coordinates on the board in a shuffled array
		// Used for "Random Node Selection" heuristic
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();

		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				// Populate ArrayList with empty cells (i.e., cells with no wall)
				if (!Character.isDigit(gameBoard[i][j])) {
					coords.add(new Coordinate(i, j));
				}
			}
		}

		return coords;
	}

	public ArrayList<Coordinate> getShuffledCoordList() {
		ArrayList<Coordinate> coords = getCoordList();
		long seed = System.nanoTime();
		Collections.shuffle(coords, new Random(seed));

		return coords;
	}

	public void fillColUp() {
		// TODO
	}

	public void fillColDown() {
		// TODO
	}

	public void fillRowLeft() {
		// TODO
	}

	public void fillRowRight() {
		// TODO
	}
}