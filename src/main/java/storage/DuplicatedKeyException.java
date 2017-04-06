package storage;

public class DuplicatedKeyException extends KeyException {
    public DuplicatedKeyException(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        return "The key '" + getKey() + "' is already used in the cache.";
    }
}