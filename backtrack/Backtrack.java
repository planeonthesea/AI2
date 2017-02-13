import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;


public class Backtrack {
	private static final int RANDOM_NODE_HEURISTIC = 0;
	private static final int MOST_CONTRAINED_HEURISTIC = 1;
	private static final int MOST_CONSTRAINING_HEURISTIC = 2;
	private static final String[] HEURITIC_NAMES = {"Random Node", "Most Contrained", "Most Constraining"};
	private static final int MAX_RECURSION_ATTEMPTS = 100000;

	private static int HEURISTIC = RANDOM_NODE_HEURISTIC; // Default to random if user chooses none
	private static int NUM_NODES_GENERATED = 0;

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
						currPuzz+=1;
						NUM_NODES_GENERATED = 0;
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
				solveWithRandom(puzz);
				//solveWithMostConstrained(puzz);
				break;
			case MOST_CONSTRAINING_HEURISTIC:
				solveWithRandom(puzz);
				//solveWithMostConstraining(puzz);
				break;
		}
	}

	private static void solveWithRandom(AkariPuzzle puzz) {
		ArrayList<Coordinate> shuffledCoords = puzz.getShuffledCoordList();
		Random randomGenerator = new Random();
		Boolean solved = false;
		Coordinate rootCoord;
		Node<Coordinate> rootNode;
		int removeNodeNum;
		char[][] initBoardState = puzz.deepCopyGameBoard();

		ArrayList<Coordinate> coordDeepCopy = new ArrayList<Coordinate>();
		for (Coordinate coord : shuffledCoords) {
			coordDeepCopy.add(new Coordinate(coord.x, coord.y));
		}

		// The top level -- Iterate across the shuffled coords as the root 
		// of the Tree
		while(!solved && shuffledCoords.size() > 0) {
			puzz.setGameBoard(initBoardState);
			initBoardState = puzz.deepCopyGameBoard();
			removeNodeNum = findNextNodeRoot(shuffledCoords, puzz);
			rootCoord = shuffledCoords.remove(removeNodeNum);
			rootNode = new Node<Coordinate>(rootCoord);
			rootNode.addChildren(coordDeepCopy);

			if (puzz.placeBulbIfPossible(rootNode.getData())) {
				NUM_NODES_GENERATED+=1;
				if (puzz.isFull() && puzz.isSolved()) {
					solved = true;
				} else {
					solved = solveWithRandom(puzz, rootNode);
				}
			} 
		}

		puzz.removeAsterisks();
		System.out.println(puzz.toString());
		System.out.println("Number of nodes generated: " + NUM_NODES_GENERATED);
	}
	private static int findNextNodeRoot(ArrayList<Coordinate> coords, AkariPuzzle puzz) {
		Random randomGenerator = new Random();
		int nextNode = 0;
		boolean keepGoing;
		char[][] gameBoard = puzz.getGameBoard();
		Coordinate cell;
		int count;
		int emptySpaceCount;
		switch (HEURISTIC) {
		case RANDOM_NODE_HEURISTIC:
			nextNode = randomGenerator.nextInt(coords.size());
			break;
		case MOST_CONSTRAINING_HEURISTIC:
			nextNode = -1;
			int currNode = 0 ;
			int highestNum = -1;
			for(Coordinate c : coords) {
				emptySpaceCount = 0;
				keepGoing = true;
				cell = c;
				count = cell.y;
				while(keepGoing && cell.y - count > 0) {
					if(gameBoard[cell.x][cell.y - count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y - count])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.x;
				while(keepGoing && cell.x - count > 0) {
					if(gameBoard[cell.x - count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x - count][cell.y])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.x + count < puzz.getRows()-1) {
					if(gameBoard[cell.x + count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x + count][cell.y])) {
						keepGoing = false;
					}
					count++;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.y + count < puzz.getCols()-1) {
					if(gameBoard[cell.x][cell.y + count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y + count])) {
						keepGoing = false;
					}
					count++;
				}
				if(nextNode == -1) {
					nextNode = currNode;
					highestNum = emptySpaceCount;
				} else if(emptySpaceCount > highestNum) {
					nextNode = currNode;
					highestNum = emptySpaceCount;
				}
				currNode++;
			}
			break;
		case MOST_CONTRAINED_HEURISTIC:
			nextNode = -1;
			currNode = 0 ;
			int lowestNum;
			lowestNum = Integer.MAX_VALUE;
			for(Coordinate c : coords) {
				emptySpaceCount = 0;
				keepGoing = true;
				cell = c;
				count = cell.y;
				while(keepGoing && cell.y - count > 0) {
					if(gameBoard[cell.x][cell.y - count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y - count])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.x;
				while(keepGoing && cell.x - count > 0) {
					if(gameBoard[cell.x - count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x - count][cell.y])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.x + count < puzz.getRows()-1) {
					if(gameBoard[cell.x + count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x + count][cell.y])) {
						keepGoing = false;
					}
					count++;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.y + count < puzz.getCols()-1) {
					if(gameBoard[cell.x][cell.y + count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y + count])) {
						keepGoing = false;
					}
					count++;
				}
				if(nextNode == -1) {
					nextNode = currNode;
					lowestNum = emptySpaceCount;
				} else if(emptySpaceCount > lowestNum) {
					nextNode = currNode;
					lowestNum = emptySpaceCount;
				}
				currNode++;
			}
			break;
	}
		return nextNode;
	}
	private static int findNextNode(ArrayList<Node<Coordinate>> coords, AkariPuzzle puzz) {
		Random randomGenerator = new Random();
		int nextNode = 0;
		boolean keepGoing;
		char[][] gameBoard = puzz.getGameBoard();
		Coordinate cell;
		int count;
		int emptySpaceCount;
		int currNode;
		switch (HEURISTIC) {
		case RANDOM_NODE_HEURISTIC:
			nextNode = randomGenerator.nextInt(coords.size());
			break;
		case MOST_CONSTRAINING_HEURISTIC:
			nextNode = -1;
			currNode = 0 ;
			int highestNum = -1;
			for(Node<Coordinate> n : coords) {
				emptySpaceCount = 0;
				keepGoing = true;
				cell = n.getData();
				count = cell.y;
				while(keepGoing && cell.y - count > 0) {
					if(gameBoard[cell.x][cell.y - count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y - count])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.x;
				while(keepGoing && cell.x - count > 0) {
					if(gameBoard[cell.x - count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x - count][cell.y])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.x + count < puzz.getRows()-1) {
					if(gameBoard[cell.x + count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x + count][cell.y])) {
						keepGoing = false;
					}
					count++;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.y + count < puzz.getCols()-1) {
					if(gameBoard[cell.x][cell.y + count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y + count])) {
						keepGoing = false;
					}
					count++;
				}
				if(nextNode == -1) {
					nextNode = currNode;
					highestNum = emptySpaceCount;
				} else if(emptySpaceCount > highestNum) {
					nextNode = currNode;
					highestNum = emptySpaceCount;
				}
				currNode++;
			}
			break;
		case MOST_CONTRAINED_HEURISTIC:
			nextNode = -1;
			currNode = 0 ;
			int lowestNum;
			lowestNum = Integer.MAX_VALUE;
			for(Node<Coordinate> n : coords) {
				emptySpaceCount = 0;
				keepGoing = true;
				cell = n.getData();
				count = cell.y;
				while(keepGoing && cell.y - count > 0) {
					if(gameBoard[cell.x][cell.y - count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y - count])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.x;
				while(keepGoing && cell.x - count > 0) {
					if(gameBoard[cell.x - count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x - count][cell.y])) {
						keepGoing = false;
					}
					count--;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.x + count < puzz.getRows()-1) {
					if(gameBoard[cell.x + count][cell.y] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x + count][cell.y])) {
						keepGoing = false;
					}
					count++;
				}
				keepGoing = true;
				count = cell.y;
				while(keepGoing && cell.y + count < puzz.getCols()-1) {
					if(gameBoard[cell.x][cell.y + count] == '_') {
						emptySpaceCount++;
					} else if(Character.isDigit(gameBoard[cell.x][cell.y + count])) {
						keepGoing = false;
					}
					count++;
				}
				if(nextNode == -1) {
					nextNode = currNode;
					lowestNum = emptySpaceCount;
				} else if(emptySpaceCount > lowestNum) {
					nextNode = currNode;
					lowestNum = emptySpaceCount;
				}
				currNode++;
			}
			break;
	}
		return nextNode;
	}
	private static Boolean solveWithRandom(AkariPuzzle puzz, Node<Coordinate> rootNode) {
		Boolean solved = false;
		Node<Coordinate> currNode;
		ArrayList<Node<Coordinate>> children = rootNode.getChildren();
		int nextNode;
		char[][] initBoardState = puzz.deepCopyGameBoard();

		solved = puzz.isSolved();

		while (!solved && children.size() > 0) {
			puzz.setGameBoard(initBoardState);
			initBoardState = puzz.deepCopyGameBoard();
			nextNode = findNextNode(children, puzz);
			currNode = children.remove(nextNode);
			currNode.setChildrenList(children);

			if (puzz.placeBulbIfPossible(currNode.getData())) {
				NUM_NODES_GENERATED+=1;
				if (puzz.isFull() && puzz.isSolved()) {
					solved = true;
				} else {
					solved = solveWithRandom(puzz, currNode);
				}
			}
		}

		return solved;
	}
}
