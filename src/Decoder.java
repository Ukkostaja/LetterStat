import java.io.*;

public class Decoder {

    static private long totalNames;

    public static long getTotalNames() {
        return totalNames;
    }

    static LetterRootReverse readFile() {
        LetterRootReverse backwardRoot = new LetterRootReverse();
        try(
                FileReader fr = new FileReader("resources/sukunimitilasto.csv");
                BufferedReader br = new BufferedReader(fr);
        ) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                try {
                    long value = Long.valueOf(parts[1]);
                    totalNames+=value;
                    backwardRoot.add(new Word(new StringBuilder(parts[0]).reverse().toString(),value));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid line: "+line);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Total names found: " + totalNames);
        return backwardRoot;
    }
}
