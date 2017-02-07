import java.io.BufferedReader;
import java.io.FileReader;

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

        puzz.printBoard();
    }
}