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

package org.cleanlogic.sxf4j.format;

import org.cleanlogic.sxf4j.enums.SemanticType;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordSemantic {
    /**
     * Semantic code
     */
    public int code;
    /**
     * Semantic type
     */
    public SemanticType type;
    /**
     * Semantic scale
     */
    public int scale;
    /**
     * Semantic value
     */
    public String value;

    public void print() {
        print(this);
    }

    public static void print(SXFRecordSemantic sxfRecordSemantic) {
        System.out.printf("Semantic:\n");
        System.out.printf("\t\tCode:\t%d\n", sxfRecordSemantic.code);
        System.out.printf("\t\tType:\t%s (%s)\n", sxfRecordSemantic.type, sxfRecordSemantic.type.getName());
        System.out.printf("\t\tScale:\t%d\n", sxfRecordSemantic.scale);
        System.out.printf("\t\tValue:\t%s\n", sxfRecordSemantic.value);
    }
}
