/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Rule;
import com.github.cv.htmlcheck.Selector;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.List;

public class BodyIdAttributeRequiredRule implements Rule {

    private final Page page;

    public BodyIdAttributeRequiredRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        Element body = (Element) XPath.selectSingleNode(page.getRoot(), "//body");
        if (body != null && body.getAttributeValue("id", "").equals("")) {
            errors.add(new HtmlCheckError(String.format("NO BODY ID: %s has no id attribute", Selector.from(body))));
        }
    }
}
