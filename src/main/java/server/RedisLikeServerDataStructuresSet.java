package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RedisLikeServerDataStructuresSet extends Remote {
    int sadd(String key, Object member) throws RemoteException;

    int scard(String key) throws RemoteException;

    int srem(String key, Object member) throws RemoteException;

    int sismember(String key, Object member) throws RemoteException;
}