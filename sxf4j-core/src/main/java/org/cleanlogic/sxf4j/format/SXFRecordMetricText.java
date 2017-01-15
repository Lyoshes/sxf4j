package org.cleanlogic.sxf4j.format;

import org.cleanlogic.sxf4j.enums.TextMetricAlign;

/**
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFRecordMetricText {
    public String text;
    public TextMetricAlign align;
    public SXFRecordMetricText(String text, TextMetricAlign align) {
        this.text = text;
        this.align = align;
    }

    @Override
    public String toString() {
        return String.format("{\"%s\",\"%d\"}", text, align.getValue());
    }
}
