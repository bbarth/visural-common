/*
 *  Copyright 2010 Visural.
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

import com.google.common.collect.ForwardingList;
import com.visural.common.BeanUtil;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wrapper for a List of JavaBeans, which allows sorting on different
 * properties.
 *
 * @author Richard Nichols
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanList<T> extends ForwardingList<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final List<T> list;
    private final Map<Class, Comparator> typeComparators = new HashMap<Class, Comparator>();
    private final Map<String, Comparator> propertyComparators = new HashMap<String, Comparator>();
    
    private int numberOfSorts = 1;
    private List<String> sortProperties = new ArrayList<String>();
    private Nulls nullHandling = Nulls.HandledByComparators;

    public BeanList(List<T> real) {
        this.list = real;
    }

    public BeanList(List<T> real, int numberOfSorts) {
        this.list = real;
        this.numberOfSorts = numberOfSorts;
    }

    public BeanList(List<T> real, int numberOfSorts, String... initialSort) {
        this(real, numberOfSorts);
        sortByProperties(initialSort);
    }

    public BeanList(List<T> real, int numberOfSorts, Nulls nullHandling, String... initialSort) {
        this(real, numberOfSorts);
        setNullHandling(nullHandling);
        sortByProperties(initialSort);
    }

    public final void sortByProperties(String... properties) {
        for (int n = properties.length - 1; n >= 0; n--) {
            popIfRequiredThenAdd(properties[n]);
        }
        sort(Arrays.asList(properties));
    }

    public BeanList<T> resortByProperty(String property) {
        popIfRequiredThenAdd(property);
        sort(sortProperties);
        return this;
    }

    private void popIfRequiredThenAdd(String property) {
        if (sortProperties.size() > 0 && sortProperties.size() >= numberOfSorts) {
            String propAdj = property.startsWith("-") ? property.substring(1) : property;
            if (sortProperties.contains(propAdj)) {
                // remove old sort on this property
                sortProperties.remove(propAdj);
            } else if (sortProperties.contains("-" + propAdj)) {
                sortProperties.remove("-" + propAdj);
            } else {
                // sort pops oldest sort from end
                sortProperties.remove(sortProperties.size() - 1);
            }
        }
        sortProperties.add(0, property);
    }

    public BeanList<T> resort() {
        sort(sortProperties);
        return this;
    }

    private BeanList<T> sort(final Iterable<String> properties) {
        Collections.sort(list, new Comparator<T>() {
            public int compare(T o1, T o2) {
                try {
                    for (String property : properties) {
                        boolean ascending = true;
                        if (property.startsWith("-")) {
                            ascending = false;
                            property = property.substring(1);
                        }
                        Class type = BeanUtil.getPropertyType(o1, property);
                        Method getter1 = BeanUtil.findGetter(o1.getClass(), property);
                        if (getter1 == null) {
                            throw new IllegalStateException(String.format("Property '%s' on class '%s' can not be resolved.", property, o1.getClass().getName()));
                        }
                        Method getter2 = BeanUtil.findGetter(o2.getClass(), property);
                        if (getter2 == null) {
                            throw new IllegalStateException(String.format("Property '%s' on class '%s' can not be resolved.", property, o2.getClass().getName()));
                        }
                        Object val1 = getter1.invoke(o1);
                        Object val2 = getter2.invoke(o2);

                        int result = 0;

                        switch (nullHandling) {
                            case AreLess:
                                if (val1 == null && val2 == null) {
                                    continue;
                                } else if (val1 == null && val2 != null) {
                                    return ascending ? -1 : 1;
                                } else if (val1 != null && val2 == null) {
                                    return ascending ? 1 : -1;
                                }
                                break;
                            case AreMore:
                                if (val1 == null && val2 == null) {
                                    continue;
                                } else if (val1 == null && val2 != null) {
                                    return ascending ? 1 : -1;
                                } else if (val1 != null && val2 == null) {
                                    return ascending ? -1 : 1;
                                }
                                break;
                        }

                        result = propertyCompare(property, type, result, val1, val2);

                        if (result < 0 || result > 0) {
                            return ascending ? result : -result;
                        }
                        // if result == 0 we continue for next property comparison
                    }
                    // if all properties return 0, we return 0 too
                    return 0;
                } catch (Exception e) {
                    throw new IllegalStateException("Error invoking methods.", e);
                }
            }

            private int propertyCompare(String property, Class type, int result, Object val1, Object val2) throws IllegalStateException {
                Comparator comparator = getComparator(property, type);
                if (comparator != null) {
                    result = comparator.compare(val1, val2);
                } else if (Comparable.class.isAssignableFrom(type)) {
                    if (val1 != null) {
                        result = ((Comparable) val1).compareTo(val2);
                    } else if (val2 != null) {
                        result = ((Comparable) val2).compareTo(val1);
                    } else {
                        result = 0;
                    }
                } else {
                    throw new IllegalStateException("Don't know how to compare type '" + type.getName() + "'");
                }
                return result;
            }
        });
        return this;
    }

    private Comparator getComparator(String property, Class type) {
        Comparator c = propertyComparators.get(property);
        if (c == null) {
            c = typeComparators.get(type);
        }
        return c;
    }

    public BeanList<T> registerTypeComparator(Class type, Comparator comparator) {
        typeComparators.put(type, comparator);
        return this;
    }

    public BeanList<T> registerPropertyComparator(String property, Comparator comparator) {
        propertyComparators.put(property, comparator);
        return this;
    }

    public int getNumberOfSorts() {
        return numberOfSorts;
    }

    public void setNumberOfSorts(int numberOfSorts) {
        this.numberOfSorts = numberOfSorts;
        while (numberOfSorts < sortProperties.size()) {
            sortProperties.remove(sortProperties.size() - 1);
        }
    }

    /**
     * Returns an unmodifiable list of the current sort properties.
     *
     * @return
     */
    public List<String> getSortProperties() {
        return Collections.unmodifiableList(sortProperties);
    }

    public Nulls getNullHandling() {
        return nullHandling;
    }

    public final void setNullHandling(Nulls nullHandling) {
        this.nullHandling = nullHandling;
    }

    @Override
    public String toString() {
        return "Sort" + sortProperties + "-" + list.toString();
    }

    @Override
    protected List<T> delegate() {
        return list;
    }

    public enum Nulls {

        AreLess,
        AreMore,
        HandledByComparators
    }
}
