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
package com.visural.common;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Functional utilities - primarily shorthand language notation.
 * 
 * @version $Id: Function.java 25 2010-03-07 00:03:41Z tibes80@gmail.com $
 * @author Richard Nichols
 */
public class Function {

    /**
     * Clamps the value to within the range min..max
     * 
     * @param value
     * @param min
     * @param max
     * @return 
     */
    public static int clamp(int value, int min, int max) {
        return value < min 
                ? min 
                : value > max 
                    ? max 
                    : value;
    }
    /**
     * Clamps the value to within the range min..max
     * 
     * @param value
     * @param min
     * @param max
     * @return 
     */
    public static long clamp(long value, long min, long max) {
        return value < min 
                ? min 
                : value > max 
                    ? max 
                    : value;
    }
    /**
     * Clamps the value to within the range min..max
     * 
     * @param value
     * @param min
     * @param max
     * @return 
     */
    public static double clamp(double value, double min, double max) {
        return value < min 
                ? min 
                : value > max 
                    ? max 
                    : value;
    }
    /**
     * Clamps the value to within the range min..max
     * 
     * @param value
     * @param min
     * @param max
     * @return 
     */
    public static float clamp(float value, float min, float max) {
        return value < min 
                ? min 
                : value > max 
                    ? max 
                    : value;
    }

    /**
     * Returns true if any of the conditions is met
     * 
     * @param conditions
     * @return 
     */
    public static boolean any(boolean... conditions) {
        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any of the conditions is met
     * 
     * @param conditions
     * @return 
     */
    public static boolean all(boolean... conditions) {
        for (boolean condition : conditions) {
            if (!condition) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns an array with #entries elements, with the given value copied to each element.
     * 
     * @param <T>
     * @param clazz
     * @param value
     * @param entries
     * @return 
     */
    public static <T> T[] arrayify(Class<T> clazz, T value, int entries) {
        ArrayList<T> result = new ArrayList<T>();
        for (int n = 0; n < entries; n++) {
            result.add(value);
        }
        return result.toArray((T[])Array.newInstance(clazz, 0));       
    }

    public static boolean allNotNull(Object... objs) {
        for (Object o : objs) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }    

    /**
     * Allows a list of values to be provided, the first non-null value in
     * the list is returned as the result. (Same as the PL/SQL nvl() function)
     * @param <T>
     * @param mainValue
     * @param fallbackValues
     * @return
     */
    public static <T> T nvl(T... values) {
        if (values.length == 0) {
            return null;
        }
        T result = values[0];
        int idx = 1;
        while (result == null && idx < values.length) {
            result = values[idx++];
        }
        return result;
    }


    /**
     * @param clazz
     * @return true if the given Class is abstract
     */
    public static boolean isAbstractClass(Class clazz) {
        return (clazz != null && Modifier.isAbstract(clazz.getModifiers()));
    }


    /**
     * Returns the minimum value in the set based on the comparator provided
     * @param <T>
     * @param comparator
     * @param first
     * @param nextVals
     * @return
     */
    public static <T> T min(Comparator<T> comparator, T... vals) {
        if (vals == null || vals.length == 0) {
            return null;
        }
        T result = vals[0];
        for (T next : vals) {
            if (comparator.compare(next, result) < 0) {
                result = next;
            }
        }
        return result;
    }

    /**
     * Returns the minimum of the given comparable values.
     * @param <T>
     * @param vals
     * @return 
     */
    public static <T extends Comparable> T min(T... vals) {
        if (vals == null || vals.length == 0) {
            return null;
        }
        T result = vals[0];
        for (T next : vals) {
            if (next.compareTo(result) < 0) {
                result = next;
            }
        }
        return result;
    }

    
    /**
     * Returns the maximum value in the set based on the comparator provided
     * @param <T>
     * @param comparator
     * @param first
     * @param nextVals
     * @return
     */
    public static <T> T max(Comparator<T> comparator, T... vals) {
        if (vals == null || vals.length == 0) {
            return null;
        }
        T result = vals[0];
        for (T next : vals) {
            if (comparator.compare(next, result) > 0) {
                result = next;
            }
        }
        return result;
    }    
    
    /**
     * Returns the maximum of the given comparable values.
     * @param <T>
     * @param vals
     * @return 
     */
    public static <T extends Comparable> T max(T... vals) {
        if (vals == null || vals.length == 0) {
            return null;
        }
        T result = vals[0];
        for (T next : vals) {
            if (next.compareTo(result) > 0) {
                result = next;
            }
        }
        return result;
    }

    /**
     * Return the minimum of the primitive ints
     * @param vals
     * @return 
     */
    public static int minInt(int... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to min()");
        }
        int result = vals[0];
        for (int next : vals) {
            if (next < result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the maximum of the primitive ints
     * @param vals
     * @return 
     */
    public static int maxInt(int... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to max()");
        }
        int result = vals[0];
        for (int next : vals) {
            if (next > result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the minimum of the primitive floats
     * @param vals
     * @return 
     */
    public static float minFloat(float... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to min()");
        }
        float result = vals[0];
        for (float next : vals) {
            if (next < result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the maximum of the primitive floats
     * @param vals
     * @return 
     */
    public static float maxFloat(float... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to max()");
        }
        float result = vals[0];
        for (float next : vals) {
            if (next > result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the minimum of the primitive longs
     * @param vals
     * @return 
     */
    public static long minLong(long... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to min()");
        }
        long result = vals[0];
        for (long next : vals) {
            if (next < result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the maximum of the primitive longs
     * @param vals
     * @return 
     */
    public static long maxLong(long... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to max()");
        }
        long result = vals[0];
        for (long next : vals) {
            if (next > result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the minimum of the primitive doubles
     * @param vals
     * @return 
     */
    public static double minDouble(double... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to min()");
        }
        double result = vals[0];
        for (double next : vals) {
            if (next < result) {
                result = next;
            }
        }
        return result;
    }
    
    /**
     * Return the maximum of the primitive doubles
     * @param vals
     * @return 
     */
    public static double maxDouble(double... vals) {
        if (vals.length == 0) {
            throw new IllegalArgumentException("No parameters provided to max()");
        }
        double result = vals[0];
        for (double next : vals) {
            if (next > result) {
                result = next;
            }
        }
        return result;
    }
}
