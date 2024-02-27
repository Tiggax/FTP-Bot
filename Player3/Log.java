import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Log {

    static BufferedWriter fileOut = null;
    static private boolean enable = false;


    public static void print(String line) throws IOException {

        if (!enable)return;

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


    public static void enable(){
        enable = true;
    }


    public static void closeFile() throws IOException {
        fileOut.close();
    }



}
