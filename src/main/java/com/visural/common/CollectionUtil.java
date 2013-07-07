package com.visural.common;

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Utility helper methods for working with collections / iterables.
 *
 * @author Richard Nichols
 */
public abstract class CollectionUtil {

    /**
     * Use the function to generate keys to multimap the given iterable.
     *
     * @param <K>
     * @param <V>
     * @param items
     * @param function
     * @return
     */
    public static <K, V> Multimap<K, V> multimap(Iterable<V> items, com.google.common.base.Function<V, K> function) {
        checkNotNull(items);
        checkNotNull(function);

        Multimap<K, V> result = HashMultimap.<K, V>create();
        for (V item : items) {
            result.put(function.apply(item), item);
        }

        return result;
    }

    /**
     * Use the function to generate keys to map the given iterable.
     *
     * @param <K>
     * @param <V>
     * @param items
     * @param function
     * @return
     */
    public static <K, V> Map<K, V> map(Iterable<V> items, com.google.common.base.Function<V, K> function) {
        checkNotNull(items);
        checkNotNull(function);

        Map<K, V> result = new HashMap<K, V>();
        for (V value : items) {
            result.put(function.apply(value), value);
        }

        return result;
    }

    /**
     * Return if the iterable is null or contains no elements.
     *
     * @param iterable
     * @return
     */
    public static boolean isNullOrEmpty(Iterable<?> iterable) {
        if (iterable == null) {
            return true;
        }
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).isEmpty();
        }
        return !iterable.iterator().hasNext();
    }

    /**
     * Returns the first non-empty iterable.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T extends Iterable<?>> T firstNonEmpty(T... values) {
        for (T value : values) {
            if (!isNullOrEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Returns the first non-null value, or null if there are none.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T> T firstNonNull(Iterable<T> values) {
        checkNotNull(values);

        for (T t : values) {
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    /**
     * Returns the first non-null value, or null if there are none.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T> T firstNonNull(T... values) {
        for (T t : values) {
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    /**
     * Returns
     *
     * @param <T>
     * @param iterable
     * @return
     */
    public static <T> List<T> nonNullElementsIn(Iterable<T> iterable) {
        if (isNullOrEmpty(iterable)) {
            return Collections.<T>emptyList();
        }

        List<T> nonNull = new ArrayList<T>();
        for (T element : iterable) {
            if (element != null) {
                nonNull.add(element);
            }
        }

        return nonNull;
    }

    /**
     * Find minimum based on natural ordering.
     *
     * @param <E>
     * @param iterable
     * @return
     */
    public static <E extends Comparable<?>> Optional<E> min(Iterable<E> iterable) {
        return min(iterable, Ordering.natural());
    }

    /**
     * Find maximum based on natural ordering.
     *
     * @param <E>
     * @param iterable
     * @return
     */
    public static <E extends Comparable<?>> Optional<E> max(Iterable<E> iterable) {
        return max(iterable, Ordering.natural());
    }

    /**
     * Find minimum based on the comparator given.
     *
     * @param <T>
     * @param <E>
     * @param iterable
     * @param comparator
     * @return
     */
    public static <T, E extends T> Optional<E> min(Iterable<E> iterable, Comparator<T> comparator) {
        return min(iterable, Ordering.from(comparator));
    }

    /**
     * Find maximum based on the comparator given.
     *
     * @param <T>
     * @param <E>
     * @param iterable
     * @param comparator
     * @return
     */
    public static <T, E extends T> Optional<E> max(Iterable<E> iterable, Comparator<T> comparator) {
        return max(iterable, Ordering.from(comparator));
    }

    /**
     * Find minimum based on the ordering given.
     *
     * @param <T>
     * @param <E>
     * @param iterable
     * @param ordering
     * @return
     */
    public static <T, E extends T> Optional<E> min(Iterable<E> iterable, Ordering<T> ordering) {
        checkNotNull(iterable);
        checkNotNull(ordering);

        try {
            boolean absent = iterable instanceof Collection && ((Collection) iterable).isEmpty();

            return absent
                    ? Optional.<E>absent()
                    : Optional.of(ordering.min(iterable));

        } catch (NoSuchElementException e) {
            return Optional.<E>absent();
        }
    }

    /**
     * Find maximum based on the ordering given.
     *
     * @param <T>
     * @param <E>
     * @param iterable
     * @param ordering
     * @return
     */
    public static <T, E extends T> Optional<E> max(Iterable<E> iterable, Ordering<T> ordering) {
        checkNotNull(iterable);
        checkNotNull(ordering);

        try {
            boolean absent = iterable instanceof Collection && ((Collection) iterable).isEmpty();

            return absent
                    ? Optional.<E>absent()
                    : Optional.of(ordering.max(iterable));

        } catch (NoSuchElementException e) {
            return Optional.<E>absent();
        }
    }

    /**
     * Adds items to the array list and sorts it based on natural ordering.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T extends Comparable<? super T>> ArrayList<T> newSortedArrayList(Iterable<T> values) {
        return newSortedArrayList(values, Ordering.<T>natural());
    }

    /**
     * Creates a new list and sorts it with the comparator provided.
     *
     * @param <T>
     * @param values
     * @param comparator
     * @return
     */
    public static <T> ArrayList<T> newSortedArrayList(Iterable<T> values, Comparator<T> comparator) {
        checkNotNull(values);
        checkNotNull(comparator);

        ArrayList<T> result = values instanceof Collection
                ? new ArrayList<T>(((Collection) values).size())
                : new ArrayList<T>();

        for (T value : values) {
            result.add(value);
        }

        Collections.sort(result, comparator);

        return result;
    }
}
