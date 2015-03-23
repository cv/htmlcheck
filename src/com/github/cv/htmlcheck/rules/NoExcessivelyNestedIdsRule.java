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

public class NoExcessivelyNestedIdsRule implements Rule {

    private final Page page;

    public NoExcessivelyNestedIdsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> elements = XPath.selectNodes(page.getRoot(), "//body//*[@id]/*[@id]/*[@id]/*[@id]/*[@id]");

        for (Element element : elements) {
            errors.add(new HtmlCheckError(String.format("ID ABUSE: %s has four or more parents which already have id attributes", Selector.from(element))));
        }
    }
}
