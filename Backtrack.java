import java.io.BufferedReader;
import java.io.FileReader;

public class Backtrack {
    public static void main(String[] args) {
        BufferedReader reader = getFileBuffer(args[0]);
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }

    public static BufferedReader getFileBuffer(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            System.out.println(e);
        }

        return reader;
    }
}