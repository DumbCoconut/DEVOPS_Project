package client.requests;

import java.util.ArrayList;

public class RequestDecr extends Request {
    private String key;

    public RequestDecr(ArrayList<String> tokens) throws Exception {
        super(tokens);
        setNbArgs(1);
        parse();
    }


    public String getKey() {
        return key;
    }

    private void parse() throws Exception {
        if (tokens.size() != nbExpectedTokens()) {
            throw new Exception();
        }
        key = tokens.get(1);
    }

    @Override
    public String toString() {
        return "decr(\"" + key + "\")";
    }
}
