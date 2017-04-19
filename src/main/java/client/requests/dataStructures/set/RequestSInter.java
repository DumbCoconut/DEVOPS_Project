package client.requests.dataStructures.set;

import client.requests.Request;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RequestSInter extends Request {
    /**
     * All the keys we want to inter
     */
    String[] keys;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"sinter", "key", "key2", ...}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestSInter(ArrayList<String> tokens) throws NoTokensException {
        super(tokens);
        parse();
    }

    /**
     * Parse the tokens and retrieve the keys.
     */
    public void parse() {
        /* sublist because we skip "sinter" */
        /* remove the duplicates */
        ArrayList<String> tempKeys = (ArrayList<String>) tokens.subList(1, tokens.size()).stream()
                                                                   .distinct()
                                                                   .collect(Collectors.toList());

        int len = tempKeys.size();
        keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = tempKeys.get(i);
        }
    }


    /**
     * Get all the keys we want to inter
     * @return all the keys we want to inter
     */
    public String[] getKeys() {
        return keys;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        String res = "sinter(";
        for (String k : keys) {
            res += "\"" + k + "\"" + ", ";
        }
        res = res.substring(0, res.length() - 2);
        res += ")";
        return res;
    }
}