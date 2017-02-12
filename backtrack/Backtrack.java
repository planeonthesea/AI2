import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Backtrack {
	private static final int RANDOM_NODE_HEURISTIC = 0;
	private static final int MOST_CONTRAINED_HEURISTIC = 1;
	private static final int MOST_CONSTRAINING_HEURISTIC = 2;
	private static final String[] HEURITIC_NAMES = {"Random Node", "Most Contrained", "Most Constraining"};
	private static final int MAX_RECURSION_ATTEMPTS = 100000;

	private static int HEURISTIC = RANDOM_NODE_HEURISTIC; // Default to random if user chooses none
	private static int NUM_ATTEMPTS = 0;

	public static void main(String[] args) {
		if (args.length >= 2) {
			switch (args[1]) {
				case "--random":
					HEURISTIC = RANDOM_NODE_HEURISTIC;
					break;
				case "--most-constrained":
					HEURISTIC = MOST_CONTRAINED_HEURISTIC;
					break;
				case "--most-constraining":
					HEURISTIC = MOST_CONSTRAINING_HEURISTIC;
					break;
			} 
		}

		processInput(getFileBuffer(args[0]));
	}

	private static BufferedReader getFileBuffer(String path) {
		assert(!path.equals(""));
		assert(path != null);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reader;
	}

	private static void processInput(BufferedReader reader) {
		AkariPuzzle puzz = null;
		String line;
		int currRow = 0;
		int currPuzz = 0;

		try {

			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split(" ");

				if (line.charAt(0) == '#' || tokens[0].equals("#")) {
					// start of new puzzle / end of current puzzle
					// process gameboard
					if (puzz != null) {
						System.out.println("Solving puzzle #" + currPuzz +" with '" + HEURITIC_NAMES[HEURISTIC] + "' heuristic...");
						currPuzz+=1;
						solve(puzz);
					}
					puzz = null;
					currRow = 0;
				} else if (tokens.length == 2) {
					// rows + cols definition
					// Instantiate new puzzle
					int row = Integer.parseInt(tokens[0]);
					int col = Integer.parseInt(tokens[1]);
					puzz = new AkariPuzzle(row, col);
				} else {
					addPuzzleRow(puzz, line, currRow);
					currRow += 1;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addPuzzleRow(AkariPuzzle puzz, String line, int row) {

		// Iterate over string and add to row in puzzle char by char
		int col = 0;
		for (char ch : line.toCharArray()) {
			if (ch == '_' || Character.isDigit(ch)) { // ignore trailing whitespace, etc.
				puzz.setGameBoardCell(row, col, ch);
			}
			col += 1;
		}
	}

	private static void solve(AkariPuzzle puzz) {

		switch (HEURISTIC) {
			case RANDOM_NODE_HEURISTIC:
				solveWithRandom(puzz);
				break;
			case MOST_CONTRAINED_HEURISTIC:
				solveWithMostConstrained(puzz);
				break;
			case MOST_CONSTRAINING_HEURISTIC:
				solveWithMostConstraining(puzz);
				break;
		}
	}

	private static void solveWithRandom(AkariPuzzle puzz) {
		ArrayList<Coordinate> shuffledCoords = puzz.getShuffledCoordList();
		Boolean solved = false;
		Coordinate rootCoord;
		Node<Coordinate> rootNode;
		char[][] initBoardState = puzz.deepCopyGameBoard();

		System.out.println("Initial State:");
		puzz.printBoard();

		ArrayList<Coordinate> coordDeepCopy = new ArrayList<Coordinate>();
		for (Coordinate coord : shuffledCoords) {
			coordDeepCopy.add(new Coordinate(coord.x, coord.y));
		}

		// The top level -- Iterate across the shuffled coords as the root 
		// of the Tree
		while(!solved && shuffledCoords.size() > 0) {
			rootCoord = shuffledCoords.remove(0);
			rootNode = new Node<Coordinate>(rootCoord);
			rootNode.addChildren(shuffledCoords);

			if (puzz.placeBulbIfPossible(rootNode.getData())) {
				if (puzz.isFull() && puzz.isSolved()) {
					solved = true;
				} else if (puzz.isFull() && !puzz.isSolved()) {
					puzz.setGameBoard(initBoardState);
				} else {
					solved = solveWithRandom(puzz, shuffledCoords, rootNode);
				}
			} 
		}

		puzz.printBoard();
	}

	private static Boolean solveWithRandom(AkariPuzzle puzz, 
		ArrayList<Coordinate> shuffledCoords, Node<Coordinate> rootNode) {
		Boolean solved = false;
		Node<Coordinate> currNode;
		ArrayList<Node<Coordinate>> children = rootNode.getChildren();

		char[][] initBoardState;

		puzz.printBoard();
		solved = puzz.isSolved();

		while (!solved && children.size() > 0) {
			initBoardState = puzz.deepCopyGameBoard();
			currNode = children.remove(0);
			currNode.setChildrenList(children);

			if (puzz.placeBulbIfPossible(currNode.getData())) {
				if (puzz.isFull() && puzz.isSolved()) {
					solved = true;
				} else if (puzz.isFull() && !puzz.isSolved()) {
					puzz.setGameBoard(initBoardState);
				} else {
					solved = solveWithRandom(puzz, shuffledCoords, currNode);
				}
			}
		}

		return solved;
	}

	private static AkariPuzzle recursiveRandom(ArrayList<Coordinate> openSpaces, AkariPuzzle puzz) {
		if (NUM_ATTEMPTS > MAX_RECURSION_ATTEMPTS) {
			return puzz;
		}
		NUM_ATTEMPTS += 1;

		char[][] copyBoard; 
		AkariPuzzle copyPuzzle;
		AkariPuzzle solvedPuzzle = null;
		ArrayList<Coordinate> openSpacesCopy = new ArrayList<Coordinate>();
		Coordinate currCoord;
		int pick = 0;
		currCoord = null;
		boolean keepGoing = true;
		openSpacesCopy.clear();
		for(Coordinate c : openSpaces) {
			openSpacesCopy.add(new Coordinate(c.x, c.y));
		}
		while(pick < openSpacesCopy.size() && solvedPuzzle == null && keepGoing) {
			if(!openSpaces.isEmpty()) {
				copyBoard = puzz.deepCopyGameBoard();
				copyPuzzle = new AkariPuzzle(puzz.getRows(), puzz.getCols());
				copyPuzzle.setGameBoard(copyBoard);
				currCoord = openSpacesCopy.remove(pick);
				if (copyPuzzle.checkNeighbours(currCoord)) {
					copyPuzzle.placeBulbIfPossible(currCoord);
				}
				if(copyPuzzle.isFull()) {
					if(!copyPuzzle.isSolved()) {
						keepGoing = false;
					} else {
						puzz = copyPuzzle;
						solvedPuzzle = copyPuzzle;
					}
				} else {
					solvedPuzzle = recursiveRandom(openSpacesCopy, copyPuzzle);
					if(solvedPuzzle != null) {
						// copyPuzzle.printBoard();
					}
					pick++;
				}
			} else {
				keepGoing = false;
			}
		}
		return solvedPuzzle;
	}


	private static void solveWithMostConstrained(AkariPuzzle puzz) {

	}

	private static void solveWithMostConstraining(AkariPuzzle puzz) {
		
	}

	// TODO REMOVE
	private static void print2dArray(char[][] array) {
		System.out.println(Arrays.deepToString(array));
	}
}