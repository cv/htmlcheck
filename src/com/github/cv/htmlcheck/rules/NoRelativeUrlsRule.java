/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.LinkCheckRule;
import com.github.cv.htmlcheck.Selector;
import org.jdom.Attribute;

import java.util.List;

public class NoRelativeUrlsRule extends LinkCheckRule {

    public NoRelativeUrlsRule(Page page) {
        super(page);
    }

    @Override
    public void addErrorsTo(List<HtmlCheckError> errors, List<Attribute> forTheseLinks) throws Exception {
        for (Attribute attr : forTheseLinks) {
            if (!attr.getValue().startsWith("http")) {
                errors.add(new HtmlCheckError(String.format("INVALID URL: %s links to '%s', which is not absolute", Selector.from(attr.getParent()), attr.getValue())));
            }
        }
    }

}
