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

package org.cleanlogic.sxf4j;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXF {
    /**
     * Binary file of SXF format
     */
    public static final int FILE_SXF = 0x00465853;
    /**
     * Text file of SXF format
     */
    public static final int FILE_TXT = 0x4658532E;
    /**
     * Data descriptor of SXF format
     */
    public static final int DAT_SXF = 0x00544144;
    public static final int DAT_3_LENGTH = 44;
    public static final int DAT_4_LENGTH = 52;
    /**
     * Record descriptor of SXF format
     */
    public static final int RECORD_SXF = 0x7FFF7FFF;
    /**
     * 3 version redaction of SXF format
     */
    public static final int VERSION_3 = 0x00000300;
    /**
     * Length of SXF Passport for version 3;
     */
    public static final int PASSPORT_3_LENGHT = 256;
    /**
     * 4 version redaction of SXF format
     */
    public static final int VERSION_4 = 0x00040000;
    /**
     * Length of SXF Passport for version 4;
     */
    public static final int PASSPORT_4_LENGHT = 400;
}
