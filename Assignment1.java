import java.io.*;
import javax.swing.JFileChooser;


public class Assignment1 {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + file.getAbsolutePath());
            StringBuilder line = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String text = null;
                int width;
                int height;
                while ((text = reader.readLine()) != null) {
                	if(text.toString().length() > 0 && text.toString().charAt(0) == ('#')) {
                    	System.out.print("Comment");
                        System.out.println(text.toString());
                    	if((text = reader.readLine()) != null) {
                    		String[] strs = text.toString().split(" ");
                    		width = Integer.parseInt(strs[0]);
                    		height = Integer.parseInt(strs[1]);
                    		buildBoard(reader, width, height);
                    	}
                    }
                    //line.append(text)
                    //    .append(System.getProperty("line.separator"));
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void buildBoard(BufferedReader reader, int width, int height) {
    	for(int widthCounter = 0; widthCounter < width; widthCounter++) {
    		for(int heightCounter = 0; heightCounter < height; heightCounter++) {
    			
    		}
    	}
    }
}