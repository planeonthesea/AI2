import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Backtrack {
    private static final int RANDOM_NODE_HEURISTIC = 0;
    private static final int MOST_CONTRAINED_HEURISTIC = 1;
    private static final int MOST_CONSTRAINING_HEURISTIC = 2;
    private static final String[] HEURITIC_NAMES = {"Random Node", "Most Contrained", "Most Constraining"};

    private static int HEURISTIC = RANDOM_NODE_HEURISTIC; // Default to random if user chooses none

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

        try {

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");

                if (line.charAt(0) == '#' || tokens[0].equals("#")) {
                    // start of new puzzle / end of current puzzle
                    // process gameboard
                    if (puzz != null) {
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
        System.out.println("Solving Puzzle with '" + HEURITIC_NAMES[HEURISTIC] + "' heuristic...");

        //puzz.printBoard();
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
        char[][] board = puzz.getGameBoard();
        Coordinate coord = new Coordinate(2, 3);
        ArrayList<Coordinate> openSpaces = new ArrayList<Coordinate>();
        for(int i = 0; i < puzz.getRows(); i++) {
            for(int k = 0; k < puzz.getCols(); k++) {
            	if(!Character.isDigit(board[i][k])) {
            		openSpaces.add(new Coordinate(i, k));
            	}
            }
        }
        puzz = recursiveRandom(openSpaces, puzz);
        System.out.println("here i am");
		puzz.printBoard();
        System.out.println(puzz.isSolved());


    }
    private static AkariPuzzle recursiveRandom(ArrayList<Coordinate> openSpaces, AkariPuzzle puzz) {
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
		    			copyPuzzle.printBoard();
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
}