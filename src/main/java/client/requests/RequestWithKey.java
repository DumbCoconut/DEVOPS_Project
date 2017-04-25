package client.requests;

import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public abstract class RequestWithKey extends Request {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 1;

    /**
     * The key used by the request.
     */
    protected String key;

    /**
     * Constructor.
     * @param tokens The different words of the request, e.g {"get", "key"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException When no tokens are provided to the request.
     */
    public RequestWithKey(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        if (tokens.size() < minNbArgs + 1) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        key = tokens.get(1);
    }

    /**
     * Return the key used by the request.
     * @return The key used by the request.
     */
    public String getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "request(\"" + key + "\")";
    }
}