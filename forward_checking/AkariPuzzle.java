import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Arrays;

public class AkariPuzzle {
	private int rows;
	private int cols;
	private char[][] gameBoard;
	private ArrayList<Coordinate> placedBulbs = new ArrayList<Coordinate>();

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

	public Boolean checkNeighbours(Coordinate cell) {
		// Checks neighbours and returns a boolean to indicate whether or not a bulb
		// can be placed
		Boolean placeBulb = true;

		if (!checkUpperNeighbour(cell) ||
			!checkLowerNeighbour(cell) ||
			!checkLeftNeighbour(cell) ||
			!checkRightNeighbour(cell)) {
			placeBulb = false;
		} else if (Character.isDigit(gameBoard[cell.x][cell.y])) {
			placeBulb = false;
		}

		return placeBulb;

	}

	private Boolean checkUpperNeighbour(Coordinate cell) {
		Coordinate upperNeighbour = new Coordinate(cell.x - 1, cell.y);
		Boolean placeBulb = true;

		if (upperNeighbour.x >= 0) { // Make sure not out of bounds
			if (Character.isDigit(gameBoard[upperNeighbour.x][upperNeighbour.y])) {
				int surrBulbs = countSurroundingBulbs(upperNeighbour);
				if (surrBulbs >= Character.getNumericValue(gameBoard[upperNeighbour.x][upperNeighbour.y])) {
					placeBulb = false;
				}
			} 
		}

		return placeBulb;
	}

	private Boolean checkLowerNeighbour(Coordinate cell) {
		Coordinate lowerNeighbour = new Coordinate(cell.x + 1, cell.y);
		Boolean placeBulb = true;

		if (lowerNeighbour.x < this.rows) { // Make sure not out of bounds
			if (Character.isDigit(gameBoard[lowerNeighbour.x][lowerNeighbour.y])) {
				int surrBulbs = countSurroundingBulbs(lowerNeighbour);
				if (surrBulbs >= Character.getNumericValue(gameBoard[lowerNeighbour.x][lowerNeighbour.y])) {
					placeBulb = false;
				}
			} 
		}
		return placeBulb;
	}

	private Boolean checkLeftNeighbour(Coordinate cell) {
		Coordinate leftNeighbour = new Coordinate(cell.x, cell.y - 1);
		Boolean placeBulb = true;

		if (leftNeighbour.y >= 0) { // Make sure not out of bounds
			if (Character.isDigit(gameBoard[leftNeighbour.x][leftNeighbour.y])) {
				int surrBulbs = countSurroundingBulbs(leftNeighbour);
				if (surrBulbs >= Character.getNumericValue(gameBoard[leftNeighbour.x][leftNeighbour.y])) {
					placeBulb = false;
				}
			} 
		}
		return placeBulb;
	}

	private Boolean checkRightNeighbour(Coordinate cell) {
		Coordinate rightNeighbour = new Coordinate(cell.x, cell.y + 1);
		Boolean placeBulb = true;

		if (rightNeighbour.y < this.cols) { // Make sure not out of bounds
			if (Character.isDigit(gameBoard[rightNeighbour.x][rightNeighbour.y])) {
				int surrBulbs = countSurroundingBulbs(rightNeighbour);
				if (surrBulbs >= Character.getNumericValue(gameBoard[rightNeighbour.x][rightNeighbour.y])) {
					placeBulb = false;
				}
			} 
		}
		return placeBulb;
	}

	private int countSurroundingBulbs(Coordinate cell) {
		int bulbCount = 0;

		if (cell.x > 0) {
			if (gameBoard[cell.x - 1][cell.y] == 'b') {
				bulbCount += 1;
			}
		}

		if (cell.y > 0) {
			if (gameBoard[cell.x][cell.y - 1] == 'b') {
				bulbCount += 1;
			}
		}

		if (cell.x < this.rows - 1) {
			if (gameBoard[cell.x + 1][cell.y] == 'b') {
				bulbCount += 1;
			}
		}

		if (cell.y < this.cols - 1) {
			if (gameBoard[cell.x][cell.y + 1] == 'b') {
				bulbCount += 1;
			}
		}

		return bulbCount;
	}

	public Boolean placeBulbIfPossible(Coordinate initCell) {
		Boolean fillSuccess = true;
		char[][] gameBoardCopy = deepCopyGameBoard();

		if (!fillColUp(initCell)) {
			fillSuccess = false;
		}
		if (!fillColDown(initCell)) {
			fillSuccess = false;
		}
		if (!fillRowLeft(initCell)) {
			fillSuccess = false;
		}
		if (!fillRowRight(initCell)) {
			fillSuccess = false;
		}

		if (fillSuccess) {
			this.gameBoard[initCell.x][initCell.y] = 'b';
			placedBulbs.add(initCell);
		} else {
			this.gameBoard = gameBoardCopy;
		}

		return fillSuccess;
	}

	private Boolean fillColUp(Coordinate initCell) {
		// Fills cells with '*' to represent light in the upwards direction

		// Returns true if no constraints violated
		// Returns false if constraint violated
		final int OFFSET = 1;

		Boolean keepGoing = true;
		Boolean constraintViolated = false;
		Coordinate currCell = new Coordinate(initCell.x - OFFSET, initCell.y);

		while (keepGoing) {
			if (currCell.x >= 0) {
				if (this.gameBoard[currCell.x][currCell.y] == '_' || this.gameBoard[currCell.x][currCell.y] == '*') {
					this.gameBoard[currCell.x][currCell.y] = '*';
					currCell = new Coordinate(currCell.x - OFFSET, currCell.y); // Move curr Cell up by one
				} else if (Character.isDigit(this.gameBoard[currCell.x][currCell.y])) {
					keepGoing = false;
				} else if (this.gameBoard[currCell.x][currCell.y] == 'b') {
					constraintViolated = true;
					keepGoing = false;
				}
			} else {
				keepGoing = false;
			}
		}
		return !constraintViolated;
	}

	private Boolean fillRowRight(Coordinate initCell) {
		// Fills cells with '*' to represent light in the upwards direction

		// Returns true if no constraints violated
		// Returns false if constraint violated
		final int OFFSET = 1;

		Boolean keepGoing = true;
		Boolean constraintViolated = false;
		Coordinate currCell = new Coordinate(initCell.x, initCell.y + OFFSET);

		while (keepGoing) {

			if (currCell.y < this.cols) {
				if (this.gameBoard[currCell.x][currCell.y] == '_' || this.gameBoard[currCell.x][currCell.y] == '*') {
					this.gameBoard[currCell.x][currCell.y] = '*';
					currCell = new Coordinate(currCell.x, currCell.y + OFFSET); // Move curr Cell up by one
				} else if (Character.isDigit(this.gameBoard[currCell.x][currCell.y])) {
					keepGoing = false;
				} else if (this.gameBoard[currCell.x][currCell.y] == 'b') {
					constraintViolated = true;
					keepGoing = false;
				}
			} else {
				keepGoing = false;
			}
		}
		return !constraintViolated;
	}

	private Boolean fillRowLeft(Coordinate initCell) {
		// Fills cells with '*' to represent light in the upwards direction

		// Returns true if no constraints violated
		// Returns false if constraint violated
		final int OFFSET = 1;

		Boolean keepGoing = true;
		Boolean constraintViolated = false;
		Coordinate currCell = new Coordinate(initCell.x, initCell.y - OFFSET);

		while (keepGoing) {
			if (currCell.y >= 0) {
				if (this.gameBoard[currCell.x][currCell.y] == '_' || this.gameBoard[currCell.x][currCell.y] == '*') {
					this.gameBoard[currCell.x][currCell.y] = '*';
					currCell = new Coordinate(currCell.x, currCell.y - OFFSET); // Move curr Cell up by one
				} else if (Character.isDigit(this.gameBoard[currCell.x][currCell.y])) {
					keepGoing = false;
				} else if (this.gameBoard[currCell.x][currCell.y] == 'b') {
					constraintViolated = true;
					keepGoing = false;
				}
			} else {
				keepGoing = false;
			}
		}
		return !constraintViolated;
	}

	private Boolean fillColDown(Coordinate initCell) {
		// Fills cells with '*' to represent light in the upwards direction

		// Returns true if no constraints violated
		// Returns false if constraint violated
		final int OFFSET = 1;

		Boolean keepGoing = true;
		Boolean constraintViolated = false;
		Coordinate currCell = new Coordinate(initCell.x + OFFSET, initCell.y);

		while (keepGoing) {
			if (currCell.x < this.cols) {
				if (this.gameBoard[currCell.x][currCell.y] == '_' || this.gameBoard[currCell.x][currCell.y] == '*') {
					this.gameBoard[currCell.x][currCell.y] = '*';
					currCell = new Coordinate(currCell.x + OFFSET, currCell.y); // Move curr Cell up by one
				} else if (Character.isDigit(this.gameBoard[currCell.x][currCell.y])) {
					keepGoing = false;
				} else if (this.gameBoard[currCell.x][currCell.y] == 'b') {
					constraintViolated = true;
					keepGoing = false;
				}
			} else {
				keepGoing = false;
			}
		}
		return !constraintViolated;
	}

	public char[][] deepCopyGameBoard() {
		if (this.gameBoard == null) {
			return null;
		}

		final char[][] result = new char[this.gameBoard.length][];
		for (int i = 0; i < this.gameBoard.length; i++) {
			result[i] = Arrays.copyOf(this.gameBoard[i], this.gameBoard[i].length);
		}

		return result;
	}
	public Boolean isSolved() {
		boolean solved = true;
		// TODO implement method to check if puzzle is solved
		// If it is, great. If not, backtrack and try again.
		for(int i = 0; i < rows && solved; i++) {
			for(int k = 0; k < cols && solved; k++) {
				if(gameBoard[i][k] == '_') {
					solved = false;
				} else if(Character.isDigit(gameBoard[i][k])) {
					int num = Character.getNumericValue(gameBoard[i][k]);
					int bulbCount = 0;
					if(i+1 < rows && gameBoard[i+1][k] == 'b') {
						bulbCount++;
					}
					if(k+1 < cols && gameBoard[i][k+1] == 'b') {
						bulbCount++;
					}
					if(i-1 >= 0 && gameBoard[i-1][k] == 'b') {
						bulbCount++;
					}
					if(k-1 >= 0 && gameBoard[i][k-1] == 'b') {
						bulbCount++;
					}
					if(bulbCount != num) {
						solved = false;
					}
				}
			}
		}
		return solved;
	}
	public Boolean isFull() {
		boolean solved = true;
		for(int i = 0; i < rows && solved; i++) {
			for(int k = 0; k < cols && solved; k++) {
				if(gameBoard[i][k] == '_') {
					solved = false;
				}
			}
		}
		return solved;
	}
}