package client.requests.exceptions;

public class NoTokensException extends Exception {
    public NoTokensException() {}

    @Override
    public String getMessage() {
        return "(error) no tokens were provided to the request.";
    }
}
