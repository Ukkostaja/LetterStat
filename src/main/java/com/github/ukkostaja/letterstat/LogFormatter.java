package com.github.ukkostaja.letterstat;

import java.time.Instant;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

    private String formatInstant(Instant instant) {
        //TODO: improve
        return instant.toString();
    }

    private static String SEPARATOR = " : ";
    private static String METHOD_SEPARATOR = ".";

    @Override
    public String format(LogRecord record) {
        StringBuilder logbuilder = new StringBuilder();
        logbuilder.append(formatInstant(record.getInstant()))
                .append(SEPARATOR)
                .append(record.getLevel().getName())
                .append(SEPARATOR)
                .append(record.getSourceClassName())
                .append(METHOD_SEPARATOR)
                .append(record.getSourceMethodName())
                .append(SEPARATOR)
                .append(record.getMessage())
                .append(System.lineSeparator())
                ;
        return logbuilder.toString();
    }
}
