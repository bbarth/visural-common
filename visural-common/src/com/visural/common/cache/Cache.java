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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to tag a method that should cache its results.
 * 
 * @version $Id: Cache.java 38 2010-05-24 11:39:51Z tibes80@gmail.com $
 * @author Richard Nichols
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

    /**
     * Time in milliseconds that cached results remain valid. 0 (default) is that
     * they never expire.
     * @return
     */
    int timeToLive() default 0;

    /**
     * Maximum number of results to cache. Results are evicted based on {@link LRUCache}
     * @return
     */
    int maxEntries() default 0;

    /**
     * Default is that caches relate only to the local object instance (i.e. use
     * declaring object's scope). If you want the cached results to apply to all
     * objects of this class, then declare as a singleton cache = true.
     * @return
     */
    boolean singletonCache() default false;
}
