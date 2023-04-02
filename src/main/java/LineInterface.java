public interface LineInterface {

    boolean handleLine(String line) throws LineException;

    class LineException extends RuntimeException {

    }
}
