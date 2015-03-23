/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.LinkCheckRule;
import org.jdom.Attribute;

import java.util.List;

public class HyphenLimitInURLSegmentRule extends LinkCheckRule {

    private final int limit;

    public HyphenLimitInURLSegmentRule(Page page, int limit) {
        super(page);
        this.limit = limit;
    }

    @Override
    public void addErrorsTo(List<HtmlCheckError> errors, List<Attribute> forTheseLinks) throws Exception {
        for (Attribute link : forTheseLinks) {
            for (String segment : link.getValue().split("/")) {
                int count = 0;
                for (char c : segment.toCharArray()) {
                    if (c == '-') {
                        count++;
                    }
                }
                if (count > limit) {
                    errors.add(new HtmlCheckError(String.format("EXCESS HYPHENS: more than %d hyphens used in URL segment: %s", limit, link.getValue())));
                }
            }
        }
    }
}
