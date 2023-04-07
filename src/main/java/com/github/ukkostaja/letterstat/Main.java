package com.github.ukkostaja.letterstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

public class Main {


    private static Logger logger;

    public static void main(String[] args) {
        installLog();

        try {
            run(args);
        } catch (Exception e) {
            logger.error("Unhandled exception", e);
        }
    }

    public static void run(String[] args) {

        logger.info("Program starting!");
        logger.info("Root building...");

        List<Root> roots = new ArrayList<>();
        roots.add(new LetterRootReverse());

        String url = Decoder.getFileUrl();
        if (url.length() > 0) {
            Decoder.downloadAndImportFile(url,roots);
        }

        logger.info("Root built!");

        logger.info("Interactive mode engaged.");
        //LetterRootReverse root = Decoder.readFileCSV();
        Reader reader = new Reader(roots.get(0).getLetters());
        reader.startReading();

        logger.info("Program Finished!");
    }

    public static void installLog() {
        // Configure JUL to use SLF4J

        // Load the logging properties file
        InputStream is = Main.class.getClassLoader().getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            System.out.println("Unable to load logger!");
            throw new RuntimeException(e);
        }
        logger = LoggerFactory.getLogger(Main.class);

    }

}

