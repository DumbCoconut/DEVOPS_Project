package storage.exceptions;

public class DuplicatedKeyException extends KeyException {
    /**
     * Constructor.
     * @param key The key that triggered the exception.
     */
    public DuplicatedKeyException(String key) {
        super(key);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMessage() {
        return "The key \"" + getKey() + "\" is already used in the cache.";
    }
}