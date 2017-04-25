package client.requests;

import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public abstract class Request {
    /**
     * The number of arguments required to properly construct the request.
     */
    private int nbArgs;

    /**
     * The different words of the request, e.g {"get", "key"}.
     */
    protected ArrayList<String> tokens;

    /**
     * Constructor.
     * @param tokens The different words of the request, e.g {"get", "key"}.
     * @throws NoTokensException When no tokens are provided to the request.
     */
    public Request(ArrayList<String> tokens) throws NoTokensException {
        this.tokens = tokens;
        nbArgs = tokens.size() - 1;
        if (tokens.isEmpty()) {
            throw new NoTokensException();
        }
    }

    /**
     * Get the number of arguments required to properly construct the request.
     * @return The number of arguments required to properly construct the request.
     */
    public int getNbArgs() {
        return nbArgs;
    }

    /**
     * Return the number of expected tokens (i.e request's name + arguments).
     * @return The number of expected tokens.
     */
    protected int nbExpectedTokens() {
        return nbArgs + 1;
    }

    /**
     * Set the number of arguments required to properly construct the request.
     * @param nbArgs the number of arguments required to properly construct the request.
     * @throws IllegalArgumentException When a negative number of arguments is given.
     */
    public void setNbArgs(int nbArgs) throws IllegalArgumentException {
        if (nbArgs < 0) {
            throw new IllegalArgumentException("The number of arguments must be >= 0.");
        }
        this.nbArgs = nbArgs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    abstract public String toString();
}