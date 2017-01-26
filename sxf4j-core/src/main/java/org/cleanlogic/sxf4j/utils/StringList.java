/*
 * Copyright 2017 iserge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
