package client.requests.dataTypes;

import client.requests.RequestWithKey;

import java.util.ArrayList;

public class RequestDecrBy extends RequestWithKey {
    private String integer;

    public RequestDecrBy(ArrayList<String> tokens) throws Exception {
        super(tokens);
        setNbArgs(2);
        parse();
    }

    public String getInteger() {
        return integer;
    }

    public void parse() throws Exception {
        if (tokens.size() != nbExpectedTokens()) {
            throw new Exception();
        }
        integer = tokens.get(2);
    }

    @Override
    public String toString() {
        return "decrby(\"" + key + "\", " + integer + ")";
    }
}
