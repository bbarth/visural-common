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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Richard Nichols
 */
public class FluentListsTest {

    private List<String> getOneTwoThree() {
        return new ArrayList<String>(Arrays.asList("1", "2", "3"));
    }

    @Test
    public void testSubList() {
        assertEquals(Arrays.asList("1", "2"), FluentLists.subList(getOneTwoThree(), 0, -1));
        assertEquals(Arrays.asList("3"), FluentLists.subList(getOneTwoThree(), -1, 0));
        assertEquals(Collections.emptyList(), FluentLists.subList(getOneTwoThree(), 0, 0));
    }

    
    @Test
    public void testSet() {
        assertEquals(Arrays.asList("1", "2", "4"), FluentLists.set(getOneTwoThree(), -1, "4"));
        assertEquals(Arrays.asList("1", "2", "4"), FluentLists.set(getOneTwoThree(), 2, "4"));
        assertEquals(Arrays.asList("4", "2", "3"), FluentLists.set(getOneTwoThree(), 0, "4"));
    }   
    
    @Test
    public void testRemove() {
        assertEquals("3", FluentLists.remove(getOneTwoThree(), -1));
        assertEquals("2", FluentLists.remove(getOneTwoThree(), -2));
        assertEquals("3", FluentLists.remove(getOneTwoThree(), 2));
        assertEquals("1", FluentLists.remove(getOneTwoThree(), 0));
    }

    @Test
    public void testAdd() {
        assertEquals(Arrays.asList("1", "2", "4", "3"), FluentLists.add(getOneTwoThree(), -1, "4"));
        assertEquals(Arrays.asList("1", "4", "2", "3"), FluentLists.add(getOneTwoThree(), 1, "4"));
        assertEquals(Arrays.asList("4", "1", "2", "3"), FluentLists.add(getOneTwoThree(), -3, "4"));
    }

    @Test
    public void testAddAll_Iterable() {
        assertEquals(Arrays.asList("1", "2", "4", "5", "6", "3"), FluentLists.addAll(getOneTwoThree(), -1, (Iterable<String>)Arrays.asList("4", "5", "6")));
        assertEquals(Arrays.asList("1", "4", "5", "6", "2", "3"), FluentLists.addAll(getOneTwoThree(), 1, (Iterable<String>)Arrays.asList("4", "5", "6")));
        assertEquals(Arrays.asList("4", "5", "6", "1", "2", "3"), FluentLists.addAll(getOneTwoThree(), -3, (Iterable<String>)Arrays.asList("4", "5", "6")));
    }

    @Test
    public void testAddAll_Collection() {
        assertEquals(Arrays.asList("1", "2", "4", "5", "6", "3"), FluentLists.addAll(getOneTwoThree(), -1, Arrays.asList("4", "5", "6")));
        assertEquals(Arrays.asList("1", "4", "5", "6", "2", "3"), FluentLists.addAll(getOneTwoThree(), 1, Arrays.asList("4", "5", "6")));
        assertEquals(Arrays.asList("4", "5", "6", "1", "2", "3"), FluentLists.addAll(getOneTwoThree(), -3, Arrays.asList("4", "5", "6")));
    }

    @Test
    public void testGet() {
        assertEquals("3", FluentLists.get(getOneTwoThree(), -1));
        assertEquals("2", FluentLists.get(getOneTwoThree(), -2));
        assertEquals("1", FluentLists.get(getOneTwoThree(), 0));        
    }
}
