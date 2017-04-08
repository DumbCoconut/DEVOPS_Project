package client.requests;

import java.util.ArrayList;

public abstract class Request {
    protected ArrayList<String> tokens;
    private int nbArgs;

    public Request(ArrayList<String> tokens) throws Exception {
        this.tokens = tokens;
        nbArgs = tokens.size() - 1;
        if (tokens.isEmpty()) {
            throw new Exception();
        }
    }

    public int getNbArgs() {
        return nbArgs;
    }

    protected int nbExpectedTokens() {
        return nbArgs + 1;
    }

    protected void setNbArgs(int nbArgs) throws IllegalArgumentException {
        if (nbArgs < 0) {
            throw new IllegalArgumentException("The number of arguments must be >= 0.");
        }
        this.nbArgs = nbArgs;
    }

    @Override
    abstract public String toString();
}