package com.visural.common.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 *
 * @author Richard Nichols
 */
public class IteratorToEnumeration<T> implements Enumeration<T> {

    private final Iterator<T> iterator;

    public IteratorToEnumeration(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    public T nextElement() {
        return iterator.next();
    }
}
