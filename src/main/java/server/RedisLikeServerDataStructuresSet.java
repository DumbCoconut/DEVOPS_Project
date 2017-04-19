package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RedisLikeServerDataStructuresSet extends Remote {
    int sadd(String key, Object member) throws RemoteException;

    int scard(String key) throws RemoteException;

    int srem(String key, Object member) throws RemoteException;

    int sismember(String key, Object member) throws RemoteException;

    List<Object> smembers(String key) throws RemoteException;

    List<Object> sinter(String[] keys) throws RemoteException;
}