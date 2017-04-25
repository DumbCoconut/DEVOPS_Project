package client.requests.client;

import client.requests.Request;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSetServer extends Request {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The remote's ip.
     */
    private String ip;

    /**
     * The remote's name.
     */
    private String name;

    /**
     * Constructor.
     * @param tokens The different words of the request, e.g {"SET_SERVER", "127.0.0.1", "server_0"}.
     * @throws NoTokensException When no tokens are provided to the request.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public RequestSetServer(ArrayList<String> tokens) throws NoTokensException, InvalidNbArgException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Parse the tokens and retrieve both the IP and name of the server.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        ip = tokens.get(1);
        name = tokens.get(2);
    }

    /**
     * Get the remote's IP.
     * @return The remote's IP.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Get the remote's name.
     * @return The remote's name.
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ip + ":" + name;
    }
}
