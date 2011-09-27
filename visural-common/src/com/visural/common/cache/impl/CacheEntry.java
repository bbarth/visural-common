/*
 *  Copyright 2010 Richard Nichols.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.visural.common.cache.impl;

import java.lang.ref.SoftReference;

/**
 * Stateless wrapper for a cached result, which keeps created and ttl data.
 * 
 * @version $Id: CacheEntry.java 38 2010-05-24 11:39:51Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public class CacheEntry {
    private final String key;

    private final long created;
    private final int ttl;
    private final int timeCost;
    private final Object result;
    private final boolean softRef;
    private int uses = 1;

    public CacheEntry(String key, long created, long ttl, long timeCost, Object result) {
        this.key = key;
        this.created = created;        
        this.ttl = (int)ttl;
        this.timeCost = (int)timeCost;
        this.result = result;
        softRef = false;
    }

    public CacheEntry(String key, long created, long ttl, long timeCost, SoftReference result) {
        this.key = key;
        this.created = created;        
        this.ttl = (int)ttl;
        this.timeCost = (int)timeCost;
        softRef = true;
        this.result = (result.get() == null ? null : result);
    }
    
    public String getKey() {
        return key;
    }
    
    /**
     * Increments the usage counter for this entry.
     * NOTE this is not synchronized, and *could be* to ensure thread safety,
     * however it would reduce performance since get() on the cache would 
     * require thread sync. To improve performance (at the cost of slightly 
     * inaccurate usage counting) this is left non-blocking.
     */
    public void incrementUses() {
        uses++;
    }

    public int getUses() {
        return uses;
    }

    public long getCreated() {
        return created;
    }

    public Object getResult() {
        if (softRef) {
            if (result == null) return null;
            
            Object r = ((SoftReference)result).get();
            if (r == null) {
                throw new IllegalStateException("Expired soft reference can not be returned.");
            } else {
                return r;
            }
        } else {
            return result;
        }
    }

    public int getTimeCost() {
        return timeCost;
    }           

    public int getTtl() {
        return ttl;
    }
    
    public int getUsesByTimecost() {
        return uses*(timeCost+1);
    }

    /**
     * Returns whether the data has passed its expiry. Note that the cache will
     * not return data which is expired. This is for internal use.
     * @return
     */
    public boolean isExpired() {
        // Note: ttl == 0 means does not expire
        return (ttl > 0 && System.currentTimeMillis() > created + ttl) 
            || (softRef && result != null && ((SoftReference)result).get() == null);
    }

    @Override
    public String toString() {
        return "[expired: " + isExpired() + ", " + result + "]";
    }
    
}
