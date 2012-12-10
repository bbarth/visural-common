package com.visural.common;

import static com.visural.common.StringUtil.lpad;
import static com.visural.common.StringUtil.rpad;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Richard Nichols
 */
public class StringUtilTest {

    @Test
    public void testLpad() {
        assertEquals(null, lpad(null, 1, '0'));
        assertEquals("000123", lpad("123", 6, '0'));
        assertEquals("1000123", lpad("1000123", 6, '0'));
    }
    
    @Test
    public void testRpad() {
        assertEquals(null, rpad(null, 1, '0'));
        assertEquals("123000", rpad("123", 6, '0'));
        assertEquals("1000123", rpad("1000123", 6, '0'));
    }
    
//
//
//    @Test
//    public void testValidFilename() {
//        System.out.println("validFilename");
//        String filename = "";
//        boolean expResult = false;
//        boolean result = StringUtil.validFilename(filename);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testContainsAnyChar() {
//        System.out.println("containsAnyChar");
//        String string = "";
//        char[] chars = null;
//        boolean expResult = false;
//        boolean result = StringUtil.containsAnyChar(string, chars);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetStackTrace() {
//        System.out.println("getStackTrace");
//        Throwable t = null;
//        String expResult = "";
//        String result = StringUtil.getStackTrace(t);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testByteArrayToHexString() {
//        System.out.println("byteArrayToHexString");
//        byte[] raw = null;
//        String expResult = "";
//        String result = StringUtil.byteArrayToHexString(raw);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }

    @Test
    public void testEmptyToNulls() {
        System.out.println("emptyToNulls");
        String[] strings = new String[]{"a", "b", null, "", "", "c", "  "};
        String[] expResult = new String[]{"a", "b", null, null, null, "c", "  "};
        String[] result = StringUtil.emptyToNull(strings);
        assertTrue(Arrays.equals(result, expResult));
    }

    @Test
    public void testBlankToNulls() {
        System.out.println("emptyToNulls");
        String[] strings = new String[]{"a", "b", null, "", "", "c", "  "};
        String[] expResult = new String[]{"a", "b", null, null, null, "c", null};
        String[] result = StringUtil.blankToNull(strings);
        assertTrue(Arrays.equals(result, expResult));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        assertFalse(StringUtil.isEmpty(" "));
        assertFalse(StringUtil.isEmpty("a"));
    }
//
//    @Test
//    public void testIsBlankStr() {
//        System.out.println("isBlankStr");
//        String str = "";
//        boolean expResult = false;
//        boolean result = StringUtil.isBlankStr(str);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testisNotEmpty() {
//        System.out.println("isNotEmpty");
//        String str = "";
//        boolean expResult = false;
//        boolean result = StringUtil.isNotEmpty(str);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testisNotBlank() {
//        System.out.println("isNotBlank");
//        String str = "";
//        boolean expResult = false;
//        boolean result = StringUtil.isNotBlank(str);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testParseBoolean_String() {
//        System.out.println("parseBoolean");
//        String str = "";
//        boolean expResult = false;
//        boolean result = StringUtil.parseBoolean(str);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testParseBoolean_String_boolean() {
//        System.out.println("parseBoolean");
//        String str = "";
//        boolean whenBlank = false;
//        boolean expResult = false;
//        boolean result = StringUtil.parseBoolean(str, whenBlank);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testTrimToLength() {
//        System.out.println("trimToLength");
//        String str = "";
//        int len = 0;
//        String expResult = "";
//        String result = StringUtil.trimToLength(str, len);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testBlockIndent() {
//        System.out.println("blockIndent");
//        String str = "";
//        int indent = 0;
//        String expResult = "";
//        String result = StringUtil.blockIndent(str, indent);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testHtmlEscape_String_boolean() {
//        System.out.println("htmlEscape");
//        String text = "";
//        boolean convertNewlines = false;
//        String expResult = "";
//        String result = StringUtil.htmlEscape(text, convertNewlines);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testHtmlEscape_String() {
//        System.out.println("htmlEscape");
//        String text = "";
//        String expResult = "";
//        String result = StringUtil.htmlEscape(text);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testHtmlAttributeEscape() {
//        System.out.println("htmlAttributeEscape");
//        String text = "";
//        String expResult = "";
//        String result = StringUtil.htmlAttributeEscape(text);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFormatDecimal_double_int() {
//        System.out.println("formatDecimal");
//        double number = 0.0;
//        int numPlaces = 0;
//        String expResult = "";
//        String result = StringUtil.formatDecimal(number, numPlaces);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFormatDecimal_3args() {
//        System.out.println("formatDecimal");
//        double number = 0.0;
//        int minPlaces = 0;
//        int maxPlaces = 0;
//        String expResult = "";
//        String result = StringUtil.formatDecimal(number, minPlaces, maxPlaces);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testReverse() {
//        System.out.println("reverse");
//        String str = "";
//        String expResult = "";
//        String result = StringUtil.reverse(str);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testRotate() {
//        System.out.println("rotate");
//        String str = "";
//        int offset = 0;
//        String expResult = "";
//        String result = StringUtil.rotate(str, offset);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testWordLineBreak() {
//        System.out.println("wordLineBreak");
//        String str = "";
//        int charsPerLine = 0;
//        String expResult = "";
//        String result = StringUtil.wordLineBreak(str, charsPerLine);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testCountCharInstances() {
//        System.out.println("countCharInstances");
//        String s = "";
//        char c = ' ';
//        int expResult = 0;
//        int result = StringUtil.countCharInstances(s, c);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testPrettifyMonoCasedString() {
//        System.out.println("prettifyMonoCasedString");
//        String monoCased = "";
//        String expResult = "";
//        String result = StringUtil.prettifyMonoCasedString(monoCased);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testEditDistance() {
//        System.out.println("editDistance");
//        String s1 = "";
//        String s2 = "";
//        int expResult = 0;
//        int result = StringUtil.editDistance(s1, s2);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testDelimitObjectsToString_String_GenericType() {
//        System.out.println("delimitObjectsToString");
//        String delim = "";
//        T[] objects = null;
//        String expResult = "";
//        String result = StringUtil.delimitObjectsToString(delim, objects);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testDelimitObjectsToString_3args() {
//        System.out.println("delimitObjectsToString");
//        String delim = "";
//        String lastDelim = "";
//        T[] objects = null;
//        String expResult = "";
//        String result = StringUtil.delimitObjectsToString(delim, lastDelim, objects);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testDelimitObjectsToString_4args() {
//        System.out.println("delimitObjectsToString");
//        String delim = "";
//        String lastDelim = "";
//        boolean skipNulls = false;
//        T[] objects = null;
//        String expResult = "";
//        String result = StringUtil.delimitObjectsToString(delim, lastDelim, skipNulls, objects);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testDelimitObjectsToString_5args() {
//        System.out.println("delimitObjectsToString");
//        String delim = "";
//        String lastDelim = "";
//        boolean skipNulls = false;
//        StringConverter<T> conv = null;
//        T[] objects = null;
//        String expResult = "";
//        String result = StringUtil.delimitObjectsToString(delim, lastDelim, skipNulls, conv, objects);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testUrlEncode() {
//        System.out.println("urlEncode");
//        String str = "";
//        String expResult = "";
//        String result = StringUtil.urlEncode(str);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
//    }
}
