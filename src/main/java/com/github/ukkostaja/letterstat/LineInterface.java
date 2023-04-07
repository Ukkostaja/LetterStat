package com.github.ukkostaja.letterstat;

public interface LineInterface {

    boolean handleLine(String line) throws LineException;

    class LineException extends RuntimeException {

    }
}
