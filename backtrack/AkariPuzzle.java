import java.util.concurrent.ThreadLocalRandom;

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

	// For random choice heuristic
	public Coordinate getRandomCell() {
		int randRow, randCol;
		char cell = '#';

		randRow = randCol = -1;
		while (cell != '_') {
			// Generate random indeces
			randRow = ThreadLocalRandom.current().nextInt(0, rows + 1);
			randCol = ThreadLocalRandom.current().nextInt(0, cols + 1);

			cell = gameBoard[randRow][randCol];
		}

		return new Coordinate(randRow, randCol);
	}
}