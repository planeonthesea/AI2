import java.io.BufferedReader;
import java.io.FileReader;

public class Backtrack {
    public static void main(String[] args) {
        processInput(getFileBuffer(args[0]));


    }

    private static BufferedReader getFileBuffer(String path) {
        assert(!path.equals(""));
        assert(path != null);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            System.out.println(e);
        }

        return reader;
    }

    private static void processInput(BufferedReader reader) {
        AkariPuzzle puzz = null;
        String line;

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
                } else if (tokens.length == 2) {
                    // rows + cols definition
                    // Instantiate new puzzle
                    int row = Integer.parseInt(tokens[0]);
                    int col = Integer.parseInt(tokens[1]);
                    puzz = new AkariPuzzle(row, col);
                } else {
                    addPuzzleRow(puzz, line);
                }
            }

        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }

    private static void addPuzzleRow(AkariPuzzle puzz, String row) {
        System.out.println("Add row:");
        System.out.println(row);
    }

    private static void solve(AkariPuzzle puzz) {
        System.out.println("Solving Puzzle...");
    }
}