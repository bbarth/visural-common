/*
 *  Copyright 2012 Richard Nichols.
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
package com.visural.common.collection;

import com.visural.common.apacherepack.Validate;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Richard Nichols
 */
public class FluentLists {
    
    public static <T> List<T> set(List<T> list, int index, T element) {
        Validate.notNull(list);
        index = basicAdjustIndex(index, list);
        list.set(index, element);
        return list;
    }
    
    public static <T> List<T> add(List<T> list, int index, T element) {
        Validate.notNull(list);
        index = basicAdjustIndex(index, list);
        list.add(index, element);
        return list;
    }    
    
    public static <T> T remove(List<T> list, int index) {
        Validate.notNull(list);
        index = basicAdjustIndex(index, list);
        return list.remove(index);
    }
    
    public static <T> List<T> addAll(List<T> list, int index, Iterable<? extends T> iterable) {
        Validate.notNull(list);
        index = basicAdjustIndex(index, list);
        for (T element : iterable) {
            list.add(index++, element);
        }
        return list;
    }
    
    public static <T> List<T> addAll(List<T> list, int index, Collection<? extends T> collection) {
        Validate.notNull(list);
        index = basicAdjustIndex(index, list);
        list.addAll(index, collection);
        return list;
    }

    public static <T> T get(List<T> list, int index) {
        Validate.notNull(list);
        index = basicAdjustIndex(index, list);
        return list.get(index);
    }
    
    /**
     * Allows you to use a negative indices to generate subLists.
     * 
     * e.g. 
     * subList(list, 0, 1) returns the first element of the list
     * subList(list, 0, -1) returns the list without the last element
     * subList(list, -3, 0) returns the last 3 elements of the list
     * 
     * @param <T>
     * @param list
     * @param fromIndex
     * @param toIndex
     * @return 
     */
    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        Validate.notNull(list);
        fromIndex = basicAdjustIndex(fromIndex, list);
        toIndex = endIndexAdjust(toIndex, fromIndex, list);
        return list.subList(fromIndex, toIndex);
    }

    protected static <T> int basicAdjustIndex(int index, List<T> list) {
        if (index < 0) {
            index = list.size() + index;
        }
        return index;
    }

    protected static <T> int endIndexAdjust(int toIndex, int fromIndex, List<T> list) {
        if (toIndex < 0 || (toIndex == 0 && fromIndex != 0)) {
            toIndex = list.size() + toIndex;
        }
        return toIndex;
    }
}
