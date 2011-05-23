/*
 *  Copyright 2009 Richard Nichols.
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
package com.visural.common.collection.readonly;

import java.util.Collection;
import java.util.List;

/**
 * A list which wraps another list, but throws a
 * `UnsupportedOperationException` if the consumer attempts to modify it.
 *
 * @version $Id: ReadOnlyList.java 2 2009-11-17 12:26:31Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public class ReadOnlyList<T> implements List<T> {

    private final List<T> baseCollection;

    public ReadOnlyList(List<T> baseCollection) {
        if (baseCollection == null) {
            throw new IllegalArgumentException("Base collection is required.");
        }
        this.baseCollection = baseCollection;
    }

    public int size() {
        return baseCollection.size();
    }

    public boolean isEmpty() {
        return baseCollection.isEmpty();
    }

    public boolean contains(Object o) {
        return baseCollection.contains(o);
    }

    public ReadOnlyIterator<T> iterator() {
        return new ReadOnlyIterator<T>(baseCollection.iterator());
    }

    public Object[] toArray() {
        return baseCollection.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return baseCollection.toArray(a);
    }

    public boolean add(T e) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public boolean containsAll(Collection<?> c) {
        return baseCollection.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public T get(int index) {
        return baseCollection.get(index);
    }

    public T set(int index, T element) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public void add(int index, T element) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public T remove(int index) {
        throw new UnsupportedOperationException("Invalid operation - read only collection.");
    }

    public int indexOf(Object o) {
        return baseCollection.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return baseCollection.lastIndexOf(o);
    }

    public ReadOnlyListIterator<T> listIterator() {
        return new ReadOnlyListIterator(baseCollection.listIterator());
    }

    public ReadOnlyListIterator<T> listIterator(int index) {
        return new ReadOnlyListIterator(baseCollection.listIterator(index));
    }

    public ReadOnlyList<T> subList(int fromIndex, int toIndex) {
        return new ReadOnlyList(baseCollection.subList(fromIndex, toIndex));
    }
}
