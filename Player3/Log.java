import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {

    static BufferedWriter fileOut = null;


    public static void print(String line) throws IOException {

        if (fileOut == null) {
            FileWriter fstream = new FileWriter("Igralec.Log");
            fileOut = new BufferedWriter(fstream);
        }

        if (line.charAt(line.length() - 1) != '\n') {
            line += "\n";
        }

        fileOut.write(line);
        fileOut.flush();
    }


    public static void closeFile() throws IOException {
        fileOut.close();
    }



}
