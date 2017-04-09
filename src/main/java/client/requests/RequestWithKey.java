package client.requests;

import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public abstract class RequestWithKey extends Request {
    protected String key;
    private final int minNbArgs = 1;

    public RequestWithKey(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        if (tokens.size() < minNbArgs + 1) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        key = tokens.get(1);
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "request(\"" + key + "\")";
    }
}