package client.requests.dataStructures.set;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSMove extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 3;


    /**
     * The key of the destination set.
     */
    private String dstKey;

    /**
     * The member we want to move.
     */
    private String member;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"smove", "srckey", "dstkey", "member"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestSMove(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the key of the destination set.
     * @return The key of the destination set.
     */
    public String getDstkey() {
        return dstKey;
    }

    /**
     * Get the member we want to move.
     * @return The member we want to move.
     */
    public String getMember() {
        return member;
    }

    /**
     * Parse the tokens and retrieve both the key and the string.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        dstKey = tokens.get(2);
        member = tokens.get(3);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "smove(\"" + key + "\", " + dstKey + ", " + member + ")";
    }
}
