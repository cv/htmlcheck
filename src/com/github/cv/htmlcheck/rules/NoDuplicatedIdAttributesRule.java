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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoDuplicatedIdAttributesRule implements Rule {

    private final Page page;

    public NoDuplicatedIdAttributesRule(Page page) {
        this.page = page;
    }

    @SuppressWarnings("unchecked")
    public void addErrorsTo(List<HtmlCheckError> errors) throws Exception {
        List<Element> allElementsWithId = XPath.selectNodes(page.getRoot(), "//*[@id]");
        Map<String, Element> map = new HashMap<String, Element>();

        for (Element current : allElementsWithId) {
            Element previous = map.get(current.getAttribute("id").getValue());
            if (previous == null) {
                map.put(current.getAttribute("id").getValue(), current);
            } else {
                errors.add(new HtmlCheckError(String.format("DUPLICATED ID: %s has the same id attribute as %s", Selector.from(previous), Selector.from(current))));
            }
        }
    }
}
