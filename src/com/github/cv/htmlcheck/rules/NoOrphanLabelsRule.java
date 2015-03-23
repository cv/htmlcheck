/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.github.cv.htmlcheck.rules;

import com.github.cv.htmlcheck.HtmlCheckError;
import com.github.cv.htmlcheck.Page;
import com.github.cv.htmlcheck.Selector;
import com.github.cv.htmlcheck.Rule;
import org.jdom.Element;
import org.jdom.xpath.XPath;

import java.util.List;

public class NoOrphanLabelsRule implements Rule {

    private final Page page;

    public NoOrphanLabelsRule(Page page) {
        this.page = page;
    }

    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        @SuppressWarnings("unchecked")
        List<Element> labels = XPath.selectNodes(page.getRoot(), "//label[@for]");

        for (Element label : labels) {
            String id = label.getAttributeValue("for");
            Element elem = (Element) XPath.selectSingleNode(page.getRoot(), String.format("//*[@id='%s']", id));
            if (elem == null) {
                errors.add(new HtmlCheckError(String.format("ORPHAN LABEL: %s points to element with id '%s', which doesn't exist in this document", Selector.from(label), id)));
            }
        }
    }

}
