package storage.exceptions;

public class KeyException extends Exception {
    private String key;

    public KeyException(String key) {
        super();
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return "An operation using the key " + key + " has encountered an exception.";
    }
}