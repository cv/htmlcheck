/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.List;

public class HeaderWordLimitRule implements Rule {

    private final String header;
    private final Page page;
    private final int wordLimit;

    public HeaderWordLimitRule(Page page, String header, int wordLimit) {
        this.page = page;
        this.header = header;
        this.wordLimit = wordLimit;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> headers = XPath.selectNodes(page.getRoot(), "//" + header);

        for (Element header : headers) {
            int foundLength = header.getText().split("\\W+").length;
            if (foundLength > wordLimit) {
                errors.add(new HtmlCheckError(String.format("HEADER WORD LIMIT: %s should have at most %d words, but had %d (%s)", Selector.from(header), wordLimit, foundLength, StringUtils
                        .abbreviate(header.getText(), 60))));
            }
        }
    }
}
