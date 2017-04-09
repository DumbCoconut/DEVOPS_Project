package client.requests.dataTypes;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSet extends RequestWithKey {
    private Object o;
    private final int minNbArgs = 2;

    public RequestSet(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    public Object getObject() {
        return o;
    }

    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        o = tokens.get(2);
    }

    @Override
    public String toString() {
        return "set(\"" + key + "\", " + o + ")";
    }
}
