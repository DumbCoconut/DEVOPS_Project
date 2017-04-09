package client.requests.exceptions;

public class InvalidNbArgException extends Exception {
    private int expected;
    private int given;

    public InvalidNbArgException(int given, int expected) {
        this.given = given;
        this.expected = expected;
    }

    @Override
    public String getMessage() {
        return "(error) wrong number of arguments (given " + given + ", expected " + expected + ")";
    }
}
