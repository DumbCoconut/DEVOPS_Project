package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RedisLikeServerDataStructuresSet extends Remote {
    int sadd(String key, Object member) throws RemoteException;
}