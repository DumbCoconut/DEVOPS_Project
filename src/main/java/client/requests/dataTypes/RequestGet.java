package client.requests.dataTypes;

import client.requests.RequestWithKey;

import java.util.ArrayList;

public class RequestGet extends RequestWithKey {
    public RequestGet(ArrayList<String> tokens) throws Exception {
        super(tokens);
        setNbArgs(1);
        parse();
    }

    public void parse() throws Exception {
        if (tokens.size() != nbExpectedTokens()) {
            throw new Exception();
        }
    }

    @Override
    public String toString() {
        return "get(\"" + key + "\")";
    }
}
