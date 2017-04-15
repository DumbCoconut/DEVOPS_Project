package storage.exceptions;

public class NonExistentKeyException extends KeyException {
    /**
     * Constructor.
     * @param key The key that triggered the exception.
     */
    public NonExistentKeyException(String key) {
        super(key);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMessage() {
        return "The key \"" + getKey() + "\" does not exist in the cache.";
    }
}