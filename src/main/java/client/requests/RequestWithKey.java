package client.requests;

import java.util.ArrayList;

public abstract class RequestWithKey extends Request {
    protected String key;

    public RequestWithKey(ArrayList<String> tokens) throws Exception {
        super(tokens);
        if (tokens.size() < 2) {
            throw new Exception();
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