package ru.t1.java.demo.cache;

import org.example.aspectspringbootstarter.interfaceToMainProject.CacheStoreStarter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheStore implements CacheStoreStarter {//класс управления кэшем
    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();

    @Override
    public void put(String key, Object value, long ttl){//добавление или управлние кэшем
        store.put(key, new CacheEntry(value, ttl));
    }

    @Override
    public Object get(String key){//получение кэша по ключу
        CacheEntry cache = store.get(key);
        if(cache.isExpired() || cache == null){
            store.remove(key);
        }
        return cache;
    }

    public void remove(String key){//удаление по ключу
        store.remove(key);
    }

    public void clear(){//очистка кэша
        store.clear();
    }

}
