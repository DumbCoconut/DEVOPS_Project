package client.requests.dataTypes;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestIncrBy extends RequestWithKey {
    private String integer;
    private final int minNbArgs = 2;

    public RequestIncrBy(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    public String getInteger() {
        return integer;
    }

    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        integer = tokens.get(2);
    }

    @Override
    public String toString() {
        return "incrby(\"" + key + "\", " + integer + ")";
    }
}
