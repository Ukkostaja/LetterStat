package com.github.ukkostaja.letterstat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    static String getFileUrl(){
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


    static String processUrl(String urlToConnect, LineInterface caller) {

        try {
            HttpURLConnection con = getConnection(urlToConnect);
            StringBuilder result = new StringBuilder();
            boolean cont = true;
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(con.getInputStream());
                while (scanner.hasNextLine() && cont) {
                    String line = scanner.nextLine();
                    cont = caller.handleLine(line);
                }
                scanner.close();
            } else {
                String errorMessage = "HTTP error code: " + responseCode;
                logger.error(errorMessage);
                return null;
            }
            return result.toString();

        } catch (IOException e) {
            logger.error("IOException", e);
        }
        return "";
    }

    static void downloadAndImportFile(String urlToConnect, List<Root> roots) {
        URL url;
        try {
            logger.info("Trying to dowload file at {}", urlToConnect);
            url = new URL(urlToConnect);
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException", e);
            return;
        }
        logger.info("Opening connection");

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // set connection properties, such as request method and headers
            con.setRequestMethod("GET");

            // read response data
            try (InputStream inputStream = con.getInputStream()) {
                logger.info("Stream open");
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

                // Get the first sheet from the workbook
                Sheet sheet = workbook.getSheetAt(0);
                logger.info("Sheet get");

                // Iterate through each row in the sheet
                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    logger.trace("Parsed row: {}",row.getRowNum());
                    // Iterate through each cell in the row
                    Word word = null;
                    try {
                        word = new Word(
                                new StringBuilder(row.getCell(0).toString()).reverse().toString(),
                                Math.round(row.getCell(1).getNumericCellValue()));
                    } catch (Exception e) {
                        logger.warn("Unable to parse row: {}", errorRow(row));
                    }
                    if(word != null) {
                        for(Root root : roots) {
                            root.addWord(word);
                        }
                    }

                }

            }
        } catch (IOException e) {
            logger.error("IOException", e);
            return;
        }



    }
    private static String errorRow(Row row){
        StringBuilder line = new StringBuilder();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();

            // Print the cell value
            line.append(cell.toString())
                    .append(":");
        }
        return line.toString();
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
