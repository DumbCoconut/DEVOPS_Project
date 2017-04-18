package client.requests.dataStructures.set;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSAdd extends RequestWithKey {


    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"sadd", "key", "}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestSAdd(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
    }
}
