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
package com.visural.common;

import com.google.inject.Injector;
import com.google.inject.Scope;
import com.google.inject.spi.BindingScopingVisitor;

/**
 * Utilities for Guice projects.
 *
 * @author Richard Nichols
 */
public class GuiceUtil {

    /**
     * Finds the actual concrete class for a proxied runtime Guice'd class.
     * @param clazz
     * @return
     */
    public static <T> Class<? super T> unproxyClass(T instance) {
        @SuppressWarnings("unchecked")
        Class<T> instanceClass = (Class<T>) instance.getClass();
        return unproxyClass(instanceClass);
    }

    /**
     * Finds the actual concrete class for a proxied runtime Guice'd class.
     * @param clazz
     * @return 
     */
    public static <T> Class<? super T> unproxyClass(Class<T> clazz) {
        Class<? super T> result = clazz;
        if (result == null) {
            return null;
        }        
        if (result.getName().contains("$$EnhancerByGuice")) {
            result = result.getSuperclass();
        }
        return result;
    }

    /**
     * Attempts to return a description of the scope for a given class.
     *
     * @param i
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getScopeDesc(Injector i, Class clazz) {
        final Holder h = new Holder();
        i.getBinding(clazz).acceptScopingVisitor(new BindingScopingVisitor() {

            public Object visitEagerSingleton() {
                h.desc = "Singleton";
                return null;
            }

            public Object visitScope(Scope scope) {
                h.desc = "" + scope;
                return null;
            }

            public Object visitScopeAnnotation(Class type) {
                h.desc = "" + type;
                return null;
            }

            public Object visitNoScoping() {
                h.desc = "Unscoped";
                return null;
            }
        });
        return h.desc;
    }

    private static class Holder {

        public String desc = "unknown";
    }
}
