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
package com.visural.common.cache;

import com.google.inject.Inject;

/**
 * @version $Id: CacheService.java 31 2010-05-21 07:15:23Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public class CacheService implements Cacheable {

    private final CacheData data;

    @Inject
    protected CacheService(CacheData data) {
        this.data = data;
    }

    public CacheData __cacheData() {
        return data;
    }
    
    private int callCounter = 0;

    @Cache(maxEntries = 3, timeToLive = 100)
    public int longServiceToCache_3_100(String argument) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        return callCounter++;
    }

    @Cache(maxEntries = 5)
    public int longServiceToCache_5(String argument) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        return callCounter++;
    }

    @Cache(maxEntries = 5, singletonCache=true)
    public int longSingletonToCache_5(String argument) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException ex) {
        }
        return callCounter++;
    }

    public int getCounter() {
        return callCounter;
    }
}
