package client.requests.exceptions;

public class NoTokensException extends Exception {
    /**
     * Constructor.
     */
    public NoTokensException() {}

    /**
     * @inheritDoc
     */
    @Override
    public String getMessage() {
        return "(error) no tokens were provided to the request.";
    }
}
