package client.requests;

import java.util.ArrayList;

public class RequestSet extends Request {
    private String key;
    private Object o;

    public RequestSet(ArrayList<String> tokens) throws Exception {
        super(tokens);
        setNbArgs(2);
        parse();
    }

    public String getKey() {
        return key;
    }

    public Object getObject() {
        return o;
    }

    private void parse() throws Exception {
        if (tokens.size() != nbExpectedTokens()) {
            throw new Exception();
        }

        key = tokens.get(1);
        o = tokens.get(2);
    }

    @Override
    public String toString() {
        return "set(\"" + key + "\", " + o + ")";
    }
}
