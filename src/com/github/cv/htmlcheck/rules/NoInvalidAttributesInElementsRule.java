/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.jdom.Attribute;
import org.jdom.xpath.XPath;

import java.util.Arrays;
import java.util.List;

public class NoInvalidAttributesInElementsRule implements Rule {

    private final List<String> INVALID = Arrays.asList("//div/@type", "//script/@style", "//script/@class", "//*/@align");

    private final Page page;

    public NoInvalidAttributesInElementsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        for (String invalid : INVALID) {
            @SuppressWarnings("unchecked")
            List<Attribute> invalidAttrs = XPath.selectNodes(page.getRoot(), invalid);
            for (Attribute attribute : invalidAttrs) {
                errors.add(new HtmlCheckError(String.format("BAD ATTRIBUTE: %s cannot have the '%s' attribute", Selector.from(attribute.getParent()), attribute.getName())));
            }
        }
    }
}
