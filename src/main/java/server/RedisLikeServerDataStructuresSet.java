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

    int sinterstore(String[] keys) throws RemoteException;

    Object spop(String key) throws RemoteException;

    Object srandmember(String key) throws RemoteException;

    int smove(String srckey, String dstkey, Object member) throws RemoteException;

    List<Object> sunion(String[] keys) throws RemoteException;

    int sunionstore(String[] keys) throws RemoteException;
}