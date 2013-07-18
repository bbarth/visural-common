package com.visural.common.collection;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Arrays;
import java.util.Enumeration;

/**
 *
 * @author Richard Nichols
 */
public class Enumerations {

    public static <T> Enumeration<T> of(Iterable<T> iterable) {
        checkNotNull(iterable);
        return new IteratorToEnumeration<T>(iterable.iterator());
    }
    
    public static <T> Enumeration<T> of(T... items) {
        return new IteratorToEnumeration<T>(Arrays.asList(items).iterator());
    }
}
