package storage.exceptions;

public class KeyException extends Exception {
    /**
     * The key that triggered the exception.
     */
    private String key;

    /**
     * Constructor
     * @param key The key that triggered the exception.
     */
    public KeyException(String key) {
        super();
        this.key = key;
    }

    /**
     * Return the key that triggered the exception.
     * @return The key that triggered the exception.
     */
    public String getKey() {
        return key;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMessage() {
        return "An operation using the key \"" + key + "\" has encountered an exception.";
    }
}