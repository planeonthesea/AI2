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
}