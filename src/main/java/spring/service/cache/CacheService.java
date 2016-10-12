package spring.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.service.session.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by xsu on 16/10/12.
 * it's the cache service
 */
@Service
public class CacheService {

    @Autowired
    private SessionManager sessionManager;

    private Map<String, UserStatus> old, young;
    private ReadWriteLock readWriteLock;

    public CacheService() {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.old = new HashMap<>();
        this.young = new HashMap<>();

        Thread thread = new Thread() {
            @Override
            public void run() {
                //noinspection InfiniteLoopStatement
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    update();
                }
            }
        };
        thread.start();
    }

    public void updateUserStatus(String weChatId, UserStatus userStatus) {
        readWriteLock.writeLock().lock();
        if (old.containsKey(weChatId)) {
            old.put(weChatId, userStatus);
        } else if (young.containsKey(weChatId)) {
            young.put(weChatId, userStatus);
        }
        update(weChatId);
        readWriteLock.writeLock().unlock();
    }

    public UserStatus getUserStatus(String weChatId) {
        readWriteLock.readLock().lock();
        if (old.containsKey(weChatId)) {
            UserStatus userStatus = new UserStatus(old.get(weChatId));
            readWriteLock.readLock().unlock();
            update(weChatId);
            return userStatus;
        } else if (young.containsKey(weChatId)) {
            UserStatus userStatus = new UserStatus(young.get(weChatId));
            readWriteLock.readLock().unlock();
            return userStatus;
        } else {
            readWriteLock.readLock().unlock();
            return createCache(weChatId);
        }
    }

    private void update() {
        readWriteLock.writeLock().lock();
        old.clear();
        old.putAll(young);
        young.clear();
        readWriteLock.writeLock().unlock();
    }

    private void update(String weChatId) {
        readWriteLock.writeLock().lock();
        if (old.containsKey(weChatId)) {
            young.put(weChatId, old.get(weChatId));
            old.remove(weChatId);
        }
        readWriteLock.writeLock().unlock();
    }

    private UserStatus createCache(String weChatId) {
        readWriteLock.writeLock().lock();
        UserStatus userStatus = new UserStatus(sessionManager);
        young.put(weChatId, userStatus);
        readWriteLock.writeLock().unlock();
        return new UserStatus(userStatus);
    }

}
