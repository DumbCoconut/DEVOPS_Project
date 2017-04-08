package client.requests;

import java.util.ArrayList;

public class RequestIncrBy extends Request {
    private String key;
    private String integer;

    public RequestIncrBy(ArrayList<String> tokens) throws Exception {
        super(tokens);
        setNbArgs(2);
        parse();
    }


    public String getKey() {
        return key;
    }

    public String getInteger() {
        return integer;
    }

    private void parse() throws Exception {
        if (tokens.size() != nbExpectedTokens()) {
            throw new Exception();
        }

        key = tokens.get(1);
        integer = tokens.get(2);
    }

    @Override
    public String toString() {
        return "incrBy(\"" + key + "\", " + integer + ")";
    }
}
