package com.visural.common.cache;

import com.visural.common.cache.Cache;
import com.visural.common.cache.EvictionStrategy;
import java.io.Serializable;

public class CacheSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    private int timeToLive;
    private int maxEntries;
    private EvictionStrategy evictionStrategy;   
    private boolean softValues;   

    public CacheSettings(Cache settings) {
        timeToLive = settings.timeToLive();
        maxEntries = settings.maxEntries();
        evictionStrategy = settings.evictionStrategy();
        softValues = settings.softValues();
    }

    public EvictionStrategy getEvictionStrategy() {
        return evictionStrategy;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public boolean isSoftValues() {
        return softValues;
    }
    
}
