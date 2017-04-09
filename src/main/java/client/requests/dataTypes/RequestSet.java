package client.requests.dataTypes;

import client.requests.RequestWithKey;

import java.util.ArrayList;

public class RequestSet extends RequestWithKey {
    private Object o;

    public RequestSet(ArrayList<String> tokens) throws Exception {
        super(tokens);
        setNbArgs(2);
        parse();
    }

    public Object getObject() {
        return o;
    }

    public void parse() throws Exception {
        if (tokens.size() != nbExpectedTokens()) {
            throw new Exception();
        }
        o = tokens.get(2);
    }

    @Override
    public String toString() {
        return "set(\"" + key + "\", " + o + ")";
    }
}
