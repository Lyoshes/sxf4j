package org.cleanlogic.sxf4j.utils;

import java.util.ArrayList;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class StringList extends ArrayList<String> {
    @Override
    public boolean contains(Object o) {
        for (String string : this) {
            if (((String) o).equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
}
