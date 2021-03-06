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

/**
 * Converts a {@link MethodCall} into a String key for cache.
 * 
 * @version $Id: KeyProvider.java 38 2010-05-24 11:39:51Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public interface KeyProvider {

    /**
     * Convert {@link MethodCall} into a key for cache.
     * @param methodCall
     * @return
     */
    String getKey(MethodCall methodCall);
}
