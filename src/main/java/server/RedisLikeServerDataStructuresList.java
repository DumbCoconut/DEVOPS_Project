package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RedisLikeServerDataStructuresList extends Remote {
    Object lindex(String key, int index) throws RemoteException;

    int llen(String key) throws RemoteException;

    Object lpop(String key) throws RemoteException;

    boolean lpush(String key, Object value) throws RemoteException;

    ArrayList<Object> lrange(String key, int start, int end) throws RemoteException;

    int lrem(String key, int count, Object value) throws RemoteException;

    boolean lset(String key, int index, Object value) throws RemoteException;

    boolean ltrim(String key, int start, int end) throws RemoteException;

    Object rpop(String key) throws RemoteException;

    boolean rpush(String key, Object value) throws RemoteException;
}
