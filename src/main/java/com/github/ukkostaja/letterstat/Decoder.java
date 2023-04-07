package com.github.ukkostaja.letterstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class Decoder {

    public static final String URL_TO_GET = "https://www.avoindata.fi/data/fi/dataset/none";
    private static Logger logger = LoggerFactory.getLogger(Decoder.class);
    static private long totalNames;

    public static long getTotalNames() {
        return totalNames;
    }

    static HttpURLConnection getConnection(String urlToConnect) throws IOException {
        URL url = new URL(urlToConnect);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
    }

    static String getFileUrl() throws IOException {
        StringBuilder result = new StringBuilder();
        processUrl(
            URL_TO_GET,
            (String line) -> {
                if (line.contains("href=\"") &&
                        line.contains("sukunimitilasto") &&
                        line.contains(".xlsx")) {
                    int beginning = line.indexOf("\"")+1;
                    int end = line.indexOf("\"",beginning);
                    result.append(line.substring(beginning,end));
                    return false;
                } else return true;
            }
        );
        return result.toString();
    }


    static String processUrl(String urlToConnect, LineInterface caller) throws IOException {

        HttpURLConnection con = getConnection(urlToConnect);
        StringBuilder result = new StringBuilder();
        boolean cont = true;
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(con.getInputStream());
            while (scanner.hasNextLine() && cont) {
                String line =scanner.nextLine();
                cont = caller.handleLine(line);
            }
            scanner.close();
        } else {
            String errorMessage = "HTTP error code: " + responseCode;
            logger.error(errorMessage);
            throw new IOException(errorMessage);
        }

        return result.toString();
    }

    static void downloadAndImportFile(String url) {

    }


    static LetterRootReverse readFileCSV() {
        LetterRootReverse backwardRoot = new LetterRootReverse();
        try(
                InputStream fr = Decoder.class.getResourceAsStream("/sukunimitilasto.csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(fr));
        ) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                try {
                    long value = Long.valueOf(parts[1]);
                    totalNames+=value;
                    backwardRoot.add(new Word(new StringBuilder(parts[0]).reverse().toString(),value));
                } catch (NumberFormatException e) {
                    logger.info("Invalid line: "+line);
                }
            }

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException",e);
        } catch (IOException e) {
            logger.error("IOException",e);
        }
        logger.info("Total names found: " + totalNames);
        return backwardRoot;
    }
}
