package client.requests;

public enum RequestNames {
    GET("get"),
    SET("set"),
    TYPE("type"),
    DECR("decr"),
    DECRBY("decrby"),
    INCR("incr"),
    INCRBY("incrby"),
    DEL("del");

    private String name;

    RequestNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
