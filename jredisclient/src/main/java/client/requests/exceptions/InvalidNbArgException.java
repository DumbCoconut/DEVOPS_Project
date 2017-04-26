package client.requests.exceptions;

public class InvalidNbArgException extends Exception {
    /**
     * Expected number of arguments.
     */
    private int expected;

    /**
     * Given number of arguments.
     */
    private int given;

    /**
     * Constructor.
     * @param given Expected number of arguments.
     * @param expected Given number of arguments.
     */
    public InvalidNbArgException(int given, int expected) {
        this.given = given;
        this.expected = expected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return "(error) wrong number of arguments (given " + given + ", expected " + expected + ")";
    }
}
