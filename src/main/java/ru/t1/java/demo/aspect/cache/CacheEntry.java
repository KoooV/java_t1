package ru.t1.java.demo.aspect.cache;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheEntry {// класс который хранит само значение кэша и его актуальность
    private final Object value;// объект кэша
    private final long expireCache;//время через которое кэш станет неактуальным


    public CacheEntry(Object value, long ttl) {
        this.value = value;
        this.expireCache = System.currentTimeMillis() + ttl;//время когда кэш станет неактуальным = текущее + время из yml
    }

    public boolean isExpired(){// получение актуальности кэша
        return System.currentTimeMillis() > expireCache;
    }

}
