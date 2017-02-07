import java.io.BufferedReader;
import java.io.FileReader;

public class Backtrack {
    public static void main(String[] args) {
        processInput(getFileBuffer(args[0]));


    }

    public static BufferedReader getFileBuffer(String path) {
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

    public static void processInput(BufferedReader reader) {
        AkariPuzzle puzz;
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.charAt(0).equals("#"))
            }
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }
}