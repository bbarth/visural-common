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
package com.visural.common.web.lesscss;

import com.visural.common.web.CssColors;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version $Id$
 * @author Richard Nichols
 */
public class Lessify {

    private static final Pattern colorPattern = Pattern.compile("#[0-9a-fA-F]{3,6}[^0-9a-fA-F]");
    private static final Pattern wordColorPattern = Pattern.compile(getGiantCssColorRegex());

    public static String variablizeColors(String css) {
        Set<String> colors = new HashSet<String>();
        css = conformColors(css);
        Matcher m = colorPattern.matcher(css);
        Map<String, Integer> colorCount = new HashMap<String, Integer>();
        while (m.find()) {
            String color = m.group().substring(1, m.group().length() - 1);
            colors.add(color);
            if (colorCount.get(color) == null) {
                colorCount.put(color, 1);
            } else {
                colorCount.put(color, colorCount.get(color) + 1);
            }
        }
        StringBuffer result = new StringBuffer();
        int idx = 0;
        Map<String, String> colorMap = new HashMap<String, String>();
        for (String color : colors) {
            colorMap.put(color, "color" + minLeading(idx, 3));
            idx++;
            result.append("@").append(colorMap.get(color)).append(": #").append(conformColor(color)).append("; /* used ").append(colorCount.get(color)).append(" times */\n");
        }
        for (String color : colors) {
            css = css.replace("#"+color, "@" + colorMap.get(color));
        }
        result.append(css);
        return result.toString();
    }

    private static String conformColors(String css) {
        Matcher m = colorPattern.matcher(css);
        Map<String,String> colGroup = new HashMap<String,String>();
        while (m.find()) {
            String color = m.group().substring(1, m.group().length() - 1);
            colGroup.put(m.group(), conformColor(color));
        }
        m = wordColorPattern.matcher(css);
        while (m.find()) {
            String color = m.group().substring(1, m.group().length() - 1);
            colGroup.put(m.group(), "#"+conformColor(color));
        }
        // conform all
        for (String col : colGroup.keySet()) {            
            css = css.replace(col, col.charAt(0)+colGroup.get(col)+col.charAt(col.length()-1));
        }
        return css;
    }

    private static String conformColor(String color) {
        if (CssColors.forName(color) != null) {
            return CssColors.forName(color).getColorAsHexString().toLowerCase();
        }
        if (color.length() == 3) {
            StringBuffer sb = new StringBuffer();
            for (int n = 0; n < color.length(); n++) {
                sb.append(color.charAt(n));
                sb.append(color.charAt(n));
            }
            return sb.toString().toLowerCase();
        } else {
            return minLeading(color, 6).toLowerCase();
        }
    }

    private static String minLeading(int val, int min) {
        return minLeading(Integer.toString(val), min);
    }

    private static String minLeading(String val, int min) {
        StringBuffer result = new StringBuffer(val);
        while (result.length() < min) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    private static String getGiantCssColorRegex() {
        StringBuffer sb = new StringBuffer("");
        boolean first = true;
        for (CssColors col : CssColors.values()) {
            if (!first) sb.append("|");
            sb.append("(").append(buildRegexForCssColor(col)).append(")");
            first = false;
        }
        return sb.toString();
    }

    private static String buildRegexForCssColor(CssColors col) {
        StringBuffer sb = new StringBuffer("[\\s\\n\\r\\:]");
        for (int n = 0; n < col.name().length(); n++) {
            sb.append("[").append(Character.toLowerCase(col.name().charAt(n)))
                    .append(Character.toUpperCase(col.name().charAt(n))).append("]");
        }
        sb.append("[\\s\\n\\r;]");
        return sb.toString();
    }
}
