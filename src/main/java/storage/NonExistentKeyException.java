package storage;

public class NonExistentKeyException extends KeyException {
    public NonExistentKeyException(String key) {
        super(key);
    }

    @Override
    public String getMessage() {
        return "The key " + getKey() + " does not exist in the cache.";
    }
}