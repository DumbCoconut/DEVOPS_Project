package client.requests.client;

import client.requests.Request;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestAddServer extends Request {
    private String ip;
    private String name;
    private final int minNbArgs = 2;

    public RequestAddServer(ArrayList<String> tokens) throws NoTokensException, InvalidNbArgException {
        super(tokens);
        parse();
    }

    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        ip = tokens.get(1);
        name = tokens.get(2);
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return ip + ":" + name;
    }
}
