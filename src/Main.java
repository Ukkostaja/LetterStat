import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println(new File(".").getAbsolutePath());
        System.out.println("Program starting!");
        System.out.println("Root building...");

        LetterRootReverse root = Decoder.readFile();
        Reader reader = new Reader(root);
        reader.startReading();
/*
        String find = "nen";


*/


        System.out.println("Root built!");
        System.out.println("Program Finished!");
    }
}